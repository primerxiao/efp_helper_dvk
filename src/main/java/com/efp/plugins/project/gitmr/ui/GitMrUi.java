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
import org.gitlab4j.api.RepositoryApi;
import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.CompareResults;
import org.gitlab4j.api.models.Diff;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
    private JTextField sourceBranch;
    private JTextField targetBrance;
    private JTextArea createMrDescTextField;
    private JTextField newBranceNameTextField;
    private JButton createBranceButton;
    private JButton delBranceButton;
    private JCheckBox regularCheckBox;

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
            GitLabApi gitLabApi = null;
            try {
                boolean b = paramCheck();
                if (!b) {
                    return;
                }
                console.append("开始登录" + "\r\n");
                gitLabApi = GitLabApi.oauth2Login(GitMrConstant.GIT_LAB_APIURL, gitAccount.getText(), gitPassword.getText());
                console.setText("登录成功");
                console.setText("开始获取提交记录");
                setCommitInfo(gitLabApi);
                console.setText("获取提交记录完成");
            } catch (GitLabApiException | ParseException ex) {
                ex.printStackTrace();
                NotificationHelper.getInstance().notifyError(ex.getMessage(), project);
                console.append("异常：" + "\r\n");
                console.append(ex.getMessage() + "\r\n");
            } finally {
                if (gitLabApi != null) {
                    gitLabApi.close();
                }

            }
        });
        saveButton.addActionListener(e -> cacheConfig());

        createMrButton.addActionListener(e -> createMrAction());

        clearConsoleButton.addActionListener(e -> console.setText(""));

        createBranceButton.addActionListener(e -> {
            try {
                createBrance(project);
            } catch (GitLabApiException ex) {
                ex.printStackTrace();
                NotificationHelper.getInstance().notifyError(ex.getMessage(), project);
            }
        });

        delBranceButton.addActionListener(e -> {
            try {
                if (regularCheckBox.isSelected()) {
                    delBrance2(project);
                } else {
                    delBrance(project);
                }
            } catch (GitLabApiException ex) {
                ex.printStackTrace();
                NotificationHelper.getInstance().notifyError(ex.getMessage(), project);
            }
        });

        initConfig();

        init();
    }

    private void createBrance(@Nullable Project project) throws GitLabApiException {
        console.setText("");
        String text = newBranceNameTextField.getText().trim();
        if (StringUtils.isEmpty(text)) {
            NotificationHelper.getInstance().notifyError("分支名不能为空", project);
            return;
        }
        List<AppInfo> selectAppInfos = getSelectAppInfos();
        if (selectAppInfos.isEmpty()) {
            NotificationHelper.getInstance().notifyError("请选择项目", project);
            return;
        }
        GitLabApi gitLabApi = GitLabApi.oauth2Login(GitMrConstant.GIT_LAB_APIURL, gitAccount.getText(), gitPassword.getText());
        for (AppInfo selectAppInfo : selectAppInfos) {
            try {
                console.append("-----开始创建分支【" + text + "】，项目【" + selectAppInfo.getProjectName() + "】，源分支【master】\r\n");
                RepositoryApi repositoryApi = gitLabApi.getRepositoryApi();
                repositoryApi.createBranch(selectAppInfo.getProjectId(), text, "master");
                console.append("-----成功创建分支【" + text + "】，项目【" + selectAppInfo.getProjectName() + "】，源分支【master】\r\n");
                console.append("-----\r\n");
            } catch (Exception exception) {
                console.append(exception.getMessage());
                console.append("-----失败创建分支【" + text + "】，项目【" + selectAppInfo.getProjectName() + "】，源分支【master】失败\r\n");
                console.append("-----\r\n");
                continue;
            }
        }
        gitLabApi.close();
    }

    private void delBrance(@Nullable Project project) throws GitLabApiException {
        console.setText("");
        String text = newBranceNameTextField.getText().trim();
        if (StringUtils.isEmpty(text)) {
            NotificationHelper.getInstance().notifyError("分支名不能为空", project);
            return;
        }
        List<AppInfo> selectAppInfos = getSelectAppInfos();
        if (selectAppInfos.isEmpty()) {
            NotificationHelper.getInstance().notifyError("请选择项目", project);
            return;
        }
        GitLabApi gitLabApi = GitLabApi.oauth2Login(GitMrConstant.GIT_LAB_APIURL, gitAccount.getText(), gitPassword.getText());
        for (AppInfo selectAppInfo : selectAppInfos) {
            try {
                console.append("-----开始删除分支【" + text + "】，项目【" + selectAppInfo.getProjectName() + "】\r\n");
                RepositoryApi repositoryApi = gitLabApi.getRepositoryApi();
                repositoryApi.deleteBranch(selectAppInfo.getProjectId(), text);
                console.append("-----成功删除分支【" + text + "】，项目【" + selectAppInfo.getProjectName() + "】\r\n");
                console.append("-----\r\n");
            } catch (Exception exception) {
                console.append(exception.getMessage());
                console.append("-----失败删除分支【" + text + "】，项目【" + selectAppInfo.getProjectName() + "】失败\r\n");
                console.append("-----\r\n");
                continue;
            }
        }
        gitLabApi.close();
    }

    private void delBrance2(@Nullable Project project) throws GitLabApiException {
        console.setText("");
        String text = newBranceNameTextField.getText().trim();
        if (StringUtils.isEmpty(text)) {
            NotificationHelper.getInstance().notifyError("分支名【正则表达式】不能为空", project);
            return;
        }
        List<AppInfo> selectAppInfos = getSelectAppInfos();
        if (selectAppInfos.isEmpty()) {
            NotificationHelper.getInstance().notifyError("请选择项目", project);
            return;
        }
        GitLabApi gitLabApi = GitLabApi.oauth2Login(GitMrConstant.GIT_LAB_APIURL, gitAccount.getText(), gitPassword.getText());
        for (AppInfo selectAppInfo : selectAppInfos) {
            List<Branch> branches = gitLabApi.getRepositoryApi().getBranches(selectAppInfo.getProjectId());
            for (Branch branch : branches) {
                String branchName = branch.getName();
                try {
                    boolean isMatch = Pattern.matches(text, branchName);
                    if (!isMatch) {
                        console.append("-----分支【" + branchName + "】，项目【" + selectAppInfo.getProjectName() + "】不匹配正则表达式\r\n");
                        continue;
                    }
                    //正则模式匹配
                    console.append("-----开始删除分支【" + branchName + "】，项目【" + selectAppInfo.getProjectName() + "】\r\n");
                    RepositoryApi repositoryApi = gitLabApi.getRepositoryApi();
                    repositoryApi.deleteBranch(selectAppInfo.getProjectId(), branchName);
                    console.append("-----成功删除分支【" + branchName + "】，项目【" + selectAppInfo.getProjectName() + "】\r\n");
                    console.append("-----\r\n");
                } catch (Exception exception) {
                    console.append(exception.getMessage());
                    console.append("-----失败删除分支【" + branchName + "】，项目【" + selectAppInfo.getProjectName() + "】失败\r\n");
                    console.append("-----\r\n");
                }

            }
        }
        gitLabApi.close();
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
        String sourceBranchText = sourceBranch.getText();
        String targetBranceText = targetBrance.getText();
        // String createMrDescTextFieldText = createMrDescTextField.getText();
        if (StringUtils.isEmpty(gitAccountText) ||
                StringUtils.isEmpty(gitPasswordText) ||
                StringUtils.isEmpty(sourceBranchText) ||
                StringUtils.isEmpty(targetBranceText)
//                StringUtils.isEmpty(createMrDescTextFieldText) ||
//                createMrDescTextFieldText.equals("这里填写合版说明")
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
        String createMrDescTextFieldText = createMrDescTextField.getText();
        if (StringUtils.isEmpty(createMrDescTextFieldText) || createMrDescTextFieldText.equals("这里填写合版说明")) {
            NotificationHelper.getInstance().notifyError("请填写详细的合版说明", project);
            console.append("请填写详细的合版说明" + "\r\n");
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
                        getBranceName(commitInfo, sourceBranch.getText()),
                        getBranceName(commitInfo, targetBrance.getText()),
                        "【" + getBranceName(commitInfo, sourceBranch.getText()) + "-》" + getBranceName(commitInfo, targetBrance.getText()) + "】【账号：" + gitAccount.getText() + " 时间：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "】",
                        createMrDescTextField.getText(),
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
            try {
                //s1和uat分支比较是否有差异
                CompareResults compare = gitLabApi.getRepositoryApi().compare(
                        appInfo.getProjectId(),
                        getBranceName(appInfo, sourceBranch.getText()),
                        getBranceName(appInfo, targetBrance.getText()),
                        true);
                List<Diff> diffs = compare.getDiffs();

                if (diffs.isEmpty()) {
                    appInfo.setCompareStatus("无差异");
                    continue;
                }
                appInfo.setCompareStatus("有差异");
                diffGitProject.add(GitMrUtils.getProjectInfoById(appInfo.getProjectId()));
            } catch (GitLabApiException e) {
                e.printStackTrace();
            }
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
        sourceBranch.setText(PropertiesComponent.getInstance(project).getValue("GIT_MR_CACH_EKEY_3"));
        targetBrance.setText(PropertiesComponent.getInstance(project).getValue("GIT_MR_CACH_EKEY_4"));
    }

    private void cacheConfig() {
        PropertiesComponent.getInstance(project).setValue("GIT_MR_CACH_EKEY_1", gitAccount.getText());
        PropertiesComponent.getInstance(project).setValue("GIT_MR_CACH_EKEY_2", gitPassword.getText());
        PropertiesComponent.getInstance(project).setValue("GIT_MR_CACH_EKEY_3", sourceBranch.getText());
        PropertiesComponent.getInstance(project).setValue("GIT_MR_CACH_EKEY_4", targetBrance.getText());
    }

    private String getBranceName(GitProjectInfo gitProjectInfo, String preBranceName) {
        if (preBranceName.trim().startsWith("应用名-")) {
            return preBranceName.trim().replace("应用名-", gitProjectInfo.getProjectName().toLowerCase() + "-");
        }
        return preBranceName.trim();
    }

    private String getBranceName(AppInfo appInfo, String preBranceName) {
        if (preBranceName.trim().startsWith("应用名-")) {
            return preBranceName.trim().replace("应用名-", appInfo.getProjectName().toLowerCase() + "-");
        }
        return preBranceName.trim();
    }

    private List<AppInfo> getSelectAppInfos() {
        int[] selectedRows = appInfoTable.getSelectedRows();
        List<AppInfo> currentSelectList = new ArrayList<>();
        AppInfoTableModel appInfoTableModel = (AppInfoTableModel) appInfoTable.getModel();
        List<AppInfo> appInfoList = appInfoTableModel.getAppInfoList();
        for (int selectedRow : selectedRows) {
            currentSelectList.add(appInfoList.get(selectedRow));
        }
        return currentSelectList;
    }

}
