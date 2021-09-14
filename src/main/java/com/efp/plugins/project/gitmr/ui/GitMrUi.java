package com.efp.plugins.project.gitmr.ui;

import com.efp.common.constant.PluginContants;
import com.efp.common.notifier.NotificationHelper;
import com.efp.plugins.project.gitmr.bean.AppInfo;
import com.efp.plugins.project.gitmr.bean.AppInfoTableModel;
import com.efp.plugins.project.gitmr.bean.CommitInfo;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.apache.commons.lang3.StringUtils;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.Diff;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author primerxiao
 */
public class GitMrUi extends DialogWrapper {
    private JTextField gitAccount;
    private JPasswordField gitPassword;
    private JTextField startTime;
    private JTextField endTime;
    private JButton last30MinuteButton;
    private JButton last2HourButton;
    private JButton currentDateButton;
    private JComboBox<String> toUserComboBox;
    private JTable appInfoTable;
    private JPanel jPanel;
    private JButton fetchCommitInfoButton;

    private Project project;

    private static final List<CommitInfo> commitInfos = new ArrayList<>();

    public GitMrUi(@Nullable Project project) {
        super(project);
        this.setTitle("MR助手");
        this.project = project;
        this.setOKActionEnabled(true);
        this.setOKButtonText("提交MR");
        this.setCancelButtonText("取消");
        //表格数据初始化
        appInfoTable.setRowSelectionAllowed(true);
        appInfoTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        List<AppInfo> appInfoList = new ArrayList<>();
        for (String chooseModuleName : PluginContants.CHOOSE_MODULE_NAMES) {
            AppInfo appInfo = new AppInfo();
            appInfo.setProjectName(chooseModuleName);
            appInfo.setCompareStatus("未校对");
            appInfoList.add(appInfo);
        }
        appInfoTable.setModel(new AppInfoTableModel(appInfoList));

        //按钮事件1
        last30MinuteButton.addActionListener(e -> {
            LocalDateTime now = LocalDateTime.now();
            String nowFormart = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime pre = now.plusMinutes(-30);
            String preFormart = pre.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            startTime.setText(preFormart);
            endTime.setText(nowFormart);
        });
        last2HourButton.addActionListener(e -> {
            LocalDateTime now = LocalDateTime.now();
            String nowFormart = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime pre = now.plusHours(-2);
            String preFormart = pre.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            startTime.setText(preFormart);
            endTime.setText(nowFormart);
        });
        currentDateButton.addActionListener(e -> {
            LocalDateTime now = LocalDateTime.now();
            String nowFormart = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDate nowDate = LocalDate.now();
            String nowDateFormat = nowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            startTime.setText(nowDateFormat + " 00:00:00");
            endTime.setText(nowFormart);
        });
        fetchCommitInfoButton.addActionListener(e -> {
            //登陆gitlab
            try {
                boolean b = paramCheck();
                if (!b) {
                    return;
                }
                GitLabApi gitLabApi = GitLabApi.oauth2Login("http://10.139.6.26:7077/", gitAccount.getText(), gitPassword.getText());
                setCommitInfo(gitLabApi);
            } catch (GitLabApiException | ParseException ex) {
                ex.printStackTrace();
            }
        });
        init();
    }

    private boolean paramCheck() {
        String gitAccountText = gitAccount.getText();
        String gitPasswordText = gitPassword.getText();
        String startTimeText = startTime.getText();
        String endTimeText = endTime.getText();

        if (StringUtils.isEmpty(gitAccountText) ||
                StringUtils.isEmpty(gitPasswordText) ||
                StringUtils.isEmpty(startTimeText) ||
                StringUtils.isEmpty(endTimeText)
        ) {
            NotificationHelper.getInstance().notifyError("参数不能为空", project);
            return false;
        }

        return true;
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return jPanel;
    }

    public void createMrRequest() throws GitLabApiException {
        //登陆gitlab
        GitLabApi gitLabApi = GitLabApi.oauth2Login("http://10.139.6.26:7077/", gitAccount.getText(), gitPassword.getText());
        //如果数据为空不做处理
        if (commitInfos.isEmpty()) {
            NotificationHelper.getInstance().notifyInfo("数据为空或者未进行提交信息校对", project);
            return;
        }
        if (appInfoTable.getSelectedRows().length < 1) {
            NotificationHelper.getInstance().notifyInfo("未选择项目", project);
            return;
        }
        for (CommitInfo commitInfo : commitInfos) {
            //提交合并请求
            NotificationHelper.getInstance().notifyInfo("开始处理模块[" + commitInfo.getProjectName() + "]代码合并-----------", project);
            //判断是否被选择
            boolean b = checkAppChoose(commitInfo);
            if (!b) {
                return;
            }
            try {
                gitLabApi.getMergeRequestApi().createMergeRequest(
                        commitInfo.getProjectId(),
                        "sourceBranch.getText()",
                        "targetBranch.getText()",
                        gitAccount.getText() + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
                        commitInfo.getCommitMessages().stream().collect(Collectors.joining("/r/n")),
                        getAssignIdBySelect(),
                        Integer.valueOf(commitInfo.getProjectId()),
                        new String[]{},
                        0,
                        false
                );
                NotificationHelper.getInstance().notifyInfo("模块[" + commitInfo.getProjectName() + "]代码合并成功", project);
            } catch (GitLabApiException e) {
                e.printStackTrace();
                NotificationHelper.getInstance().notifyInfo("模块[" + commitInfo.getProjectName() + "]代码合并失败：" + e.getMessage(), project);
            }
        }
        gitLabApi.close();
    }

    public void setCommitInfo(GitLabApi gitLabApi) throws GitLabApiException, ParseException {
        AppInfoTableModel tableModel = (AppInfoTableModel) appInfoTable.getModel();
        List<AppInfo> appInfoList = tableModel.getAppInfoList();
        //获取配置的项目
        for (AppInfo appInfo : appInfoList) {
            List<Commit> commits = gitLabApi.getCommitsApi().getCommits(
                    appInfo.getProjectId(),
                    "sourceBranch.getText()",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime.getText()),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime.getText()));
            if (commits.isEmpty()) {
                appInfo.setCompareStatus("无差异");
                continue;
            }
            appInfo.setCompareStatus("有差异");
            //其它工程
            CommitInfo commitInfo = new CommitInfo();
            commitInfo.setCommitFiles(new ArrayList<>());
            commitInfo.setCommitMessages(new ArrayList<>());
            commitInfo.setCommits(new ArrayList<>());
            //设置项目信息
            commitInfo.setProjectId(appInfo.getProjectId());
            commitInfo.setProjectName(appInfo.getProjectName());
            for (Commit commit : commits) {
//                if (gitAccount.getText().equals(commit.getCommitterName())) {
                String message = commit.getMessage();
                if (message.startsWith("Merge")) {
                    continue;
                }
                //设置提交信息
                if (!commitInfo.getCommitMessages().contains(message)) {
                    commitInfo.getCommitMessages().add(message);
                }
                //设置提交文件清单
                List<Diff> diffs = gitLabApi.getCommitsApi().getDiff(appInfo.getProjectId(), commit.getId());
                for (Diff diff : diffs) {
                    if (!commitInfo.getCommitFiles().contains(diff.getOldPath())) {
                        commitInfo.getCommitFiles().add(diff.getOldPath());
                    }
                    if (!commitInfo.getCommitFiles().contains(diff.getNewPath())) {
                        commitInfo.getCommitFiles().add(diff.getNewPath());
                    }
                }
//                }
            }
            if (!commitInfo.getCommitFiles().isEmpty()) {
                commitInfos.add(commitInfo);
            }
        }
        gitLabApi.close();
    }

    private Integer getAssignIdBySelect() {
        String selectedItem = (String) toUserComboBox.getSelectedItem();
        if (selectedItem.equals("高伟才")) {
            return 001;
        }
        if (selectedItem.equals("高伟才")) {
            return 002;
        }
        return null;
    }

    private boolean checkAppChoose(CommitInfo commitInfo) {
        AppInfoTableModel appInfoTableModel = (AppInfoTableModel) appInfoTable.getModel();
        List<AppInfo> appInfoList = appInfoTableModel.getAppInfoList();
        int[] selectedRows = appInfoTable.getSelectedRows();
        for (int selectedRow : selectedRows) {
            AppInfo appInfo = appInfoList.get(selectedRow);
            if (appInfo.getProjectName().equals(commitInfo.getProjectName())) {
                return true;
            }
        }
        return false;
    }
}
