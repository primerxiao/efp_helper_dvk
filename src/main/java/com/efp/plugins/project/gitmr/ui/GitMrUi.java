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
import com.intellij.ui.JBColor;
import org.apache.commons.lang3.StringUtils;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.CompareResults;
import org.gitlab4j.api.models.Diff;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.text.ParseException;
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
    private JComboBox<String> toUserComboBox;
    private JTable appInfoTable;
    private JPanel jPanel;
    private JButton fetchCommitInfoButton;
    private JTextArea console;
    private JButton saveButton;
    private JButton createMrButton;
    private JButton clearConsoleButton;

    private Project project;

    private static final List<GitProjectInfo> diffGitProject = new ArrayList<>();

    public GitMrUi(@Nullable Project project) {
        super(project);
        this.setTitle("MR助手");
        this.project = project;
        this.setOKActionEnabled(false);
        this.setCancelButtonText("关闭");
        console.setLineWrap(true);
        console.setEditable(false);
        console.setBackground(JBColor.GRAY);
        console.setSelectedTextColor(JBColor.GREEN);
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
        AppInfoTableModel model = (AppInfoTableModel) appInfoTable.getModel();
        model.fireTableDataChanged();
        //touser
        toUserComboBox.removeAll();
        for (Map.Entry<String, String> stringStringEntry : GitMrConstant.ASSIGN_MAPS.entrySet()) {
            String key = stringStringEntry.getKey();
            toUserComboBox.addItem(key);
        }
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

        clearConsoleButton.addActionListener(e -> console.setText(""));

        initConfig();

        init();
    }

    private void createMrAction() {
        GitLabApi gitLabApi = null;
        console.setText("");
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

        if (StringUtils.isEmpty(gitAccountText) ||
                StringUtils.isEmpty(gitPasswordText)
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
        if (diffGitProject.isEmpty()) {
            NotificationHelper.getInstance().notifyError("差异数据为空或者未进行提交信息校对", project);
            console.append("差异数据为空或者未进行提交信息校对" + "\r\n");
            return;
        }
        int[] selectedRows = appInfoTable.getSelectedRows();
        List<String> currentSelectList = new ArrayList<>();
        AppInfoTableModel appInfoTableModel = (AppInfoTableModel) appInfoTable.getModel();
        List<AppInfo> appInfoList = appInfoTableModel.getAppInfoList();
        for (int selectedRow : selectedRows) {
            AppInfo appInfo = appInfoList.get(selectedRow);
            if (!appInfo.getCompareStatus().equals("有差异")) {
                continue;
            }
            currentSelectList.add(appInfo.getProjectId());
        }
        for (GitProjectInfo commitInfo : diffGitProject) {
            //判断是否被选择 没选择 默认提交所有差异
            if (selectedRows.length >= 1) {
                if (!currentSelectList.contains(commitInfo.getProjectId())) {
                    continue;
                }
            }
            NotificationHelper.getInstance().notifyError("开始处理模块[" + commitInfo.getProjectName() + "]代码合并-----------", project);
            console.append("开始处理模块[" + commitInfo.getProjectName() + "]代码合并-----------" + "\r\n");
            try {
                gitLabApi.getMergeRequestApi().createMergeRequest(
                        commitInfo.getProjectId(),
                        GitMrUtils.getBranchName(1, GitMrUtils.getProjectInfoById(commitInfo.getProjectId())),
                        GitMrUtils.getBranchName(2, GitMrUtils.getProjectInfoById(commitInfo.getProjectId())),
                        "【s1-》uat】【账号：" + gitAccount.getText() + " 时间：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "】",
                        "代码合并申请",
                        getAssignIdBySelect(),
                        Integer.valueOf(commitInfo.getProjectId()),
                        new String[]{},
                        -1,
                        false
                );
                NotificationHelper.getInstance().notifyError("模块[" + commitInfo.getProjectName() + "]代码合并成功", project);
                console.append("模块[" + commitInfo.getProjectName() + "]代码合并成功" + "\r\n");
            } catch (GitLabApiException e) {
                e.printStackTrace();
                NotificationHelper.getInstance().notifyError("模块[" + commitInfo.getProjectName() + "]代码合并失败：" + e.getMessage(), project);
                console.append("模块[" + commitInfo.getProjectName() + "]代码合并失败：" + e.getMessage() + "\r\n");
            }
        }
    }

    public void setCommitInfo(GitLabApi gitLabApi) throws GitLabApiException, ParseException {
        diffGitProject.clear();
        AppInfoTableModel tableModel = (AppInfoTableModel) appInfoTable.getModel();
        List<AppInfo> appInfoList = tableModel.getAppInfoList();
        //获取配置的项目
        for (AppInfo appInfo : appInfoList) {
            //s1和uat分支比较是否有差异
            CompareResults compare = gitLabApi.getRepositoryApi().compare(appInfo.getProjectId(), GitMrUtils.getBranchName(1, GitMrUtils.getProjectInfoById(appInfo.getProjectId())), GitMrUtils.getBranchName(2, GitMrUtils.getProjectInfoById(appInfo.getProjectId())), true);
            List<Diff> diffs = compare.getDiffs();

            if (diffs.isEmpty()) {
                appInfo.setCompareStatus("无差异");
                continue;
            }
            appInfo.setCompareStatus("有差异");
            diffGitProject.add(GitMrUtils.getProjectInfoById(appInfo.getProjectId()));
        }
        tableModel.fireTableDataChanged();
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
    }

    private void cacheConfig() {
        PropertiesComponent.getInstance(project).setValue("GIT_MR_CACH_EKEY_1", gitAccount.getText());
        PropertiesComponent.getInstance(project).setValue("GIT_MR_CACH_EKEY_2", gitPassword.getText());
    }

}
