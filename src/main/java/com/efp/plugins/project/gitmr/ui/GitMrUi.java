package com.efp.plugins.project.gitmr.ui;

import com.efp.common.notifier.NotificationHelper;
import com.efp.plugins.project.gitmr.bean.AppInfo;
import com.efp.plugins.project.gitmr.bean.AppInfoTableModel;
import com.efp.plugins.project.gitmr.bean.CommitInfo;
import com.efp.plugins.project.gitmr.bean.GitProjectInfo;
import com.efp.plugins.project.gitmr.constant.GitMrConstant;
import com.efp.plugins.project.util.GitMrUtils;
import com.intellij.ide.util.PropertiesComponent;
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
import java.util.Map;
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
    private JTextArea console;
    private JButton saveButton;
    private JButton createMrButton;

    private Project project;

    private static final List<CommitInfo> commitInfos = new ArrayList<>();

    public GitMrUi(@Nullable Project project) {
        super(project);
        this.setTitle("MR助手");
        this.project = project;
        this.setOKActionEnabled(false);
        this.setCancelButtonText("关闭");
        console.setLineWrap(true);
        //表格数据初始化
        appInfoTable.setRowSelectionAllowed(true);
        appInfoTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        List<AppInfo> appInfoList = new ArrayList<>();
        for (GitProjectInfo gitProjectInfo : GitMrConstant.GIT_PROJECT_INFOS) {
            AppInfo appInfo = new AppInfo();
            appInfo.setProjectName(gitProjectInfo.getProjectName());
            appInfo.setProjectId(gitProjectInfo.getProjectId());
            appInfo.setCompareStatus("未校对");
            appInfoList.add(appInfo);
        }
        appInfoTable.setModel(new AppInfoTableModel(appInfoList));
        //touser
        toUserComboBox.removeAll();
        for (Map.Entry<String, String> stringStringEntry : GitMrConstant.ASSIGN_MAPS.entrySet()) {
            String key = stringStringEntry.getKey();
            toUserComboBox.addItem(key);
        }
        //按钮事件
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
            console.setText("");
            //登陆gitlab
            try {
                boolean b = paramCheck();
                if (!b) {
                    return;
                }
                console.append("开始登录" + "\r\n");
                GitLabApi gitLabApi = GitLabApi.oauth2Login(GitMrConstant.GIT_LAB_APIURL, gitAccount.getText(), gitPassword.getText());
                console.setText("登录成功");
                console.setText("开始获取提交记录");
                setCommitInfo(gitLabApi);
                console.setText("获取提交记录完成");
            } catch (GitLabApiException | ParseException ex) {
                ex.printStackTrace();
                NotificationHelper.getInstance().notifyError(ex.getMessage(), project);
                console.append("异常：" + "\r\n");
                console.append(ex.getMessage() + "\r\n");
            }
        });
        saveButton.addActionListener(e -> cacheConfig());

        createMrButton.addActionListener(e -> createMrAction());

        initConfig();

        init();
    }

    private void createMrAction() {
        GitLabApi gitLabApi = null;
        try {
            console.append("-------------创建MR开始---------" + "\r\n");
            //登陆gitlab
            gitLabApi = GitLabApi.oauth2Login(GitMrConstant.GIT_LAB_APIURL, gitAccount.getText(), gitPassword.getText());
            createMrRequest(gitLabApi);
            console.append("-------------创建MR结束---------" + "\r\n");
        } catch (GitLabApiException e) {
            e.printStackTrace();
            console.append("-------------创建MR异常，信息如下：---------" + "\r\n");
            console.append(e.getMessage() + "\r\n");
        } finally {
            if (gitLabApi != null) {
                gitLabApi.close();
            }

        }
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

    public void createMrRequest(GitLabApi gitLabApi) throws GitLabApiException {
        //如果数据为空不做处理
        if (commitInfos.isEmpty()) {
            NotificationHelper.getInstance().notifyInfo("数据为空或者未进行提交信息校对", project);
            console.append("数据为空或者未进行提交信息校对" + "\r\n");
            return;
        }
        if (appInfoTable.getSelectedRows().length < 1) {
            NotificationHelper.getInstance().notifyInfo("未选择项目", project);
            console.append("未选择项目" + "\r\n");
            return;
        }
        for (CommitInfo commitInfo : commitInfos) {
            //提交合并请求
            //判断是否被选择
            boolean b = checkAppChoose(commitInfo);
            if (!b) {
                continue;
            }
            NotificationHelper.getInstance().notifyInfo("开始处理模块[" + commitInfo.getProjectName() + "]代码合并-----------", project);
            console.append("开始处理模块[" + commitInfo.getProjectName() + "]代码合并-----------" + "\r\n");
            try {
                gitLabApi.getMergeRequestApi().createMergeRequest(
                        commitInfo.getProjectId(),
                        GitMrUtils.getBranchName(1, GitMrUtils.getProjectInfoById(commitInfo.getProjectId())),
                        GitMrUtils.getBranchName(2, GitMrUtils.getProjectInfoById(commitInfo.getProjectId())),
                        "【s1-》uat】【账号：" + gitAccount.getText() + " 时间：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "】",
                        commitInfo.getCommitMessages().stream().collect(Collectors.joining("/r/n")),
                        getAssignIdBySelect(),
                        Integer.valueOf(commitInfo.getProjectId()),
                        new String[]{},
                        -1,
                        false
                );
                NotificationHelper.getInstance().notifyInfo("模块[" + commitInfo.getProjectName() + "]代码合并成功", project);
                console.append("模块[" + commitInfo.getProjectName() + "]代码合并成功" + "\r\n");
            } catch (GitLabApiException e) {
                e.printStackTrace();
                NotificationHelper.getInstance().notifyInfo("模块[" + commitInfo.getProjectName() + "]代码合并失败：" + e.getMessage(), project);
                console.append("模块[" + commitInfo.getProjectName() + "]代码合并失败：" + e.getMessage() + "\r\n");
            }
        }
    }

    public void setCommitInfo(GitLabApi gitLabApi) throws GitLabApiException, ParseException {
        AppInfoTableModel tableModel = (AppInfoTableModel) appInfoTable.getModel();
        List<AppInfo> appInfoList = tableModel.getAppInfoList();
        //获取配置的项目
        for (AppInfo appInfo : appInfoList) {
            List<Commit> commits = gitLabApi.getCommitsApi().getCommits(
                    appInfo.getProjectId(),
                    GitMrUtils.getBranchName(1, GitMrUtils.getProjectInfoById(appInfo.getProjectId())),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime.getText()),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime.getText()));
            if (commits.isEmpty()) {
                appInfo.setCompareStatus("无差异");
                continue;
            }
            appInfo.setCompareStatus("有差异");
            commitInfos.clear();
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
        String s = GitMrConstant.ASSIGN_MAPS.get(selectedItem);
        return Integer.valueOf(s);
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

    private void initConfig() {
        gitAccount.setText(PropertiesComponent.getInstance(project).getValue("GIT_MR_CACH_EKEY_1"));
        gitPassword.setText(PropertiesComponent.getInstance(project).getValue("GIT_MR_CACH_EKEY_2"));
        startTime.setText(PropertiesComponent.getInstance(project).getValue("GIT_MR_CACH_EKEY_3"));
        endTime.setText(PropertiesComponent.getInstance(project).getValue("GIT_MR_CACH_EKEY_4"));
    }

    private void cacheConfig() {
        PropertiesComponent.getInstance(project).setValue("GIT_MR_CACH_EKEY_1", gitAccount.getText());
        PropertiesComponent.getInstance(project).setValue("GIT_MR_CACH_EKEY_2", gitPassword.getText());
        PropertiesComponent.getInstance(project).setValue("GIT_MR_CACH_EKEY_3", startTime.getText());
        PropertiesComponent.getInstance(project).setValue("GIT_MR_CACH_EKEY_4", endTime.getText());
    }

}
