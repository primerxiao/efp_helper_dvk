package com.efp.plugins.general.cron.ui;

import com.efp.common.constant.CronExpTypeEnum;
import com.efp.common.spinnermodel.YearSpinnerModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.text.DateFormatUtil;
import org.jetbrains.annotations.Nullable;
import org.quartz.CronExpression;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CronUi extends DialogWrapper {
    private JPanel jPanel;
    private JTabbedPane tabbedPane;
    private JRadioButton wRadioButtonEvery;
    private JRadioButton wRadioButtonNone;
    private JRadioButton wRadioButtonArround;
    private JRadioButton wRadioButtonInterval;
    private JRadioButton wRadioButtonAssign;
    private JRadioButton monthRadioButtonEvery;
    private JRadioButton dRadioButtonEvery;
    private JRadioButton hRadioButtonEvery;
    private JRadioButton sRadioButtonEvery;
    private JRadioButton mRadioButtonArround;
    private JRadioButton sRadioButtonInterval;
    private JRadioButton sRadioButtonAssign;
    private JRadioButton mRadioButtonEvery;
    private JRadioButton mRadioButtonInterval;
    private JRadioButton mRadioButtonAssign;
    private JRadioButton hRadioButtonArround;
    private JRadioButton hRadioButtonInterval;
    private JRadioButton hRadioButtonAssign;
    private JRadioButton dRadioButtonNone;
    private JRadioButton dRadioButtonArround;
    private JRadioButton dRadioButtonInterval;
    private JRadioButton dRadioButtonAssign;
    private JRadioButton dRadioButtonLastDayOfMOnth;
    private JRadioButton dRadioButtonWdayOfIntervalDay;
    private JRadioButton monthRadioButtonArround;
    private JRadioButton monthRadioButtonInterval;
    private JRadioButton monthRadioButtonAssign;
    private JRadioButton yRadioButtonEvery;
    private JRadioButton yRadioButtonNone;
    private JRadioButton yRadioButtonArround;
    private JRadioButton wRadioButtonLastw;
    private JButton formatToUIButton;
    private JTextField cronExprTextField;
    private JTextArea recTenExcuteTextArea;
    private JCheckBox wAssignCheckBox1;
    private JCheckBox monthAssignCheckBox1;
    private JCheckBox dAssignCheckBox1;
    private JCheckBox hAssignCheckBox0;
    private JCheckBox sAssignCheckBox0;
    private JCheckBox sAssignCheckBox1;
    private JCheckBox sAssignCheckBox2;
    private JCheckBox sAssignCheckBox3;
    private JCheckBox sAssignCheckBox4;
    private JCheckBox sAssignCheckBox5;
    private JCheckBox sAssignCheckBox6;
    private JCheckBox sAssignCheckBox7;
    private JCheckBox sAssignCheckBox8;
    private JCheckBox sAssignCheckBox9;
    private JCheckBox sAssignCheckBox10;
    private JCheckBox sAssignCheckBox11;
    private JCheckBox sAssignCheckBox12;
    private JCheckBox sAssignCheckBox13;
    private JCheckBox sAssignCheckBox14;
    private JCheckBox sAssignCheckBox15;
    private JCheckBox sAssignCheckBox16;
    private JCheckBox sAssignCheckBox17;
    private JCheckBox sAssignCheckBox18;
    private JCheckBox sAssignCheckBox19;
    private JCheckBox sAssignCheckBox20;
    private JCheckBox sAssignCheckBox21;
    private JCheckBox sAssignCheckBox22;
    private JCheckBox sAssignCheckBox23;
    private JCheckBox sAssignCheckBox24;
    private JCheckBox sAssignCheckBox25;
    private JCheckBox sAssignCheckBox26;
    private JCheckBox sAssignCheckBox27;
    private JCheckBox sAssignCheckBox28;
    private JCheckBox sAssignCheckBox29;
    private JCheckBox sAssignCheckBox30;
    private JCheckBox sAssignCheckBox31;
    private JCheckBox sAssignCheckBox32;
    private JCheckBox sAssignCheckBox33;
    private JCheckBox sAssignCheckBox34;
    private JCheckBox sAssignCheckBox35;
    private JCheckBox sAssignCheckBox36;
    private JCheckBox sAssignCheckBox37;
    private JCheckBox sAssignCheckBox38;
    private JCheckBox sAssignCheckBox39;
    private JCheckBox sAssignCheckBox40;
    private JCheckBox sAssignCheckBox41;
    private JCheckBox sAssignCheckBox42;
    private JCheckBox sAssignCheckBox43;
    private JCheckBox sAssignCheckBox44;
    private JCheckBox sAssignCheckBox45;
    private JCheckBox sAssignCheckBox46;
    private JCheckBox sAssignCheckBox47;
    private JCheckBox sAssignCheckBox48;
    private JCheckBox sAssignCheckBox49;
    private JCheckBox sAssignCheckBox50;
    private JCheckBox sAssignCheckBox51;
    private JCheckBox sAssignCheckBox52;
    private JCheckBox sAssignCheckBox53;
    private JCheckBox sAssignCheckBox54;
    private JCheckBox sAssignCheckBox55;
    private JCheckBox sAssignCheckBox56;
    private JCheckBox sAssignCheckBox57;
    private JCheckBox sAssignCheckBox58;
    private JCheckBox sAssignCheckBox59;
    private JSpinner sArroundStartSpinner;
    private JSpinner sArroundEndSpinner;
    private JSpinner sIntervalStartSpinner;
    private JSpinner sIntervalEndSpinner;
    private JSpinner mArroundSpinnerFrom;
    private JSpinner mArroundSpinnerTo;
    private JSpinner mIntervalSpinnerFrom;
    private JSpinner mIntervalSpinnerSpace;
    private JSpinner hspinner1;
    private JSpinner hspinner2;
    private JSpinner hspinner3;
    private JSpinner hspinner4;
    private JSpinner dspinner1;
    private JSpinner dspinner2;
    private JSpinner dspinner3;
    private JSpinner dspinner4;
    private JSpinner dspinner5;
    private JSpinner monthSpinner1;
    private JSpinner monthSpinner2;
    private JSpinner monthSpinner3;
    private JSpinner monthSpinner4;
    private JSpinner wSpinner1;
    private JSpinner wSpinner2;
    private JSpinner wSpinner3;
    private JSpinner wSpinner4;
    private JSpinner ySpinner1;
    private JSpinner ySpinner2;
    private JCheckBox mAssignCheckBox0;
    private JCheckBox mAssignCheckBox10;
    private JCheckBox mAssignCheckBox20;
    private JCheckBox mAssignCheckBox1;
    private JCheckBox mAssignCheckBox2;
    private JCheckBox mAssignCheckBox3;
    private JCheckBox mAssignCheckBox4;
    private JCheckBox mAssignCheckBox5;
    private JCheckBox mAssignCheckBox6;
    private JCheckBox mAssignCheckBox7;
    private JCheckBox mAssignCheckBox8;
    private JCheckBox mAssignCheckBox9;
    private JCheckBox mAssignCheckBox11;
    private JCheckBox mAssignCheckBox12;
    private JCheckBox mAssignCheckBox13;
    private JCheckBox mAssignCheckBox14;
    private JCheckBox mAssignCheckBox15;
    private JCheckBox mAssignCheckBox16;
    private JCheckBox mAssignCheckBox17;
    private JCheckBox mAssignCheckBox18;
    private JCheckBox mAssignCheckBox19;
    private JCheckBox mAssignCheckBox21;
    private JCheckBox mAssignCheckBox22;
    private JCheckBox mAssignCheckBox23;
    private JCheckBox mAssignCheckBox24;
    private JCheckBox mAssignCheckBox25;
    private JCheckBox mAssignCheckBox26;
    private JCheckBox mAssignCheckBox27;
    private JCheckBox mAssignCheckBox28;
    private JCheckBox mAssignCheckBox29;
    private JCheckBox mAssignCheckBox30;
    private JCheckBox mAssignCheckBox31;
    private JCheckBox mAssignCheckBox32;
    private JCheckBox mAssignCheckBox33;
    private JCheckBox mAssignCheckBox34;
    private JCheckBox mAssignCheckBox35;
    private JCheckBox mAssignCheckBox36;
    private JCheckBox mAssignCheckBox37;
    private JCheckBox mAssignCheckBox38;
    private JCheckBox mAssignCheckBox39;
    private JCheckBox mAssignCheckBox40;
    private JCheckBox mAssignCheckBox41;
    private JCheckBox mAssignCheckBox42;
    private JCheckBox mAssignCheckBox43;
    private JCheckBox mAssignCheckBox44;
    private JCheckBox mAssignCheckBox45;
    private JCheckBox mAssignCheckBox46;
    private JCheckBox mAssignCheckBox47;
    private JCheckBox mAssignCheckBox48;
    private JCheckBox mAssignCheckBox49;
    private JCheckBox mAssignCheckBox50;
    private JCheckBox mAssignCheckBox51;
    private JCheckBox mAssignCheckBox52;
    private JCheckBox mAssignCheckBox53;
    private JCheckBox mAssignCheckBox54;
    private JCheckBox mAssignCheckBox55;
    private JCheckBox mAssignCheckBox56;
    private JCheckBox mAssignCheckBox57;
    private JCheckBox mAssignCheckBox58;
    private JCheckBox mAssignCheckBox59;
    private JButton buildCronExpButton;
    private JCheckBox dAssignCheckBox2;
    private JCheckBox dAssignCheckBox3;
    private JCheckBox dAssignCheckBox4;
    private JCheckBox dAssignCheckBox5;
    private JCheckBox dAssignCheckBox6;
    private JCheckBox dAssignCheckBox7;
    private JCheckBox dAssignCheckBox8;
    private JCheckBox dAssignCheckBox9;
    private JCheckBox dAssignCheckBox10;
    private JCheckBox dAssignCheckBox11;
    private JCheckBox dAssignCheckBox12;
    private JCheckBox dAssignCheckBox13;
    private JCheckBox dAssignCheckBox14;
    private JCheckBox dAssignCheckBox15;
    private JCheckBox dAssignCheckBox16;
    private JCheckBox dAssignCheckBox17;
    private JCheckBox dAssignCheckBox18;
    private JCheckBox dAssignCheckBox19;
    private JCheckBox dAssignCheckBox20;
    private JCheckBox dAssignCheckBox21;
    private JCheckBox dAssignCheckBox22;
    private JCheckBox dAssignCheckBox23;
    private JCheckBox dAssignCheckBox24;
    private JCheckBox dAssignCheckBox25;
    private JCheckBox dAssignCheckBox26;
    private JCheckBox dAssignCheckBox27;
    private JCheckBox dAssignCheckBox28;
    private JCheckBox dAssignCheckBox29;
    private JCheckBox dAssignCheckBox30;
    private JCheckBox dAssignCheckBox31;
    private JCheckBox monthAssignCheckBox2;
    private JCheckBox monthAssignCheckBox3;
    private JCheckBox monthAssignCheckBox4;
    private JCheckBox monthAssignCheckBox5;
    private JCheckBox monthAssignCheckBox6;
    private JCheckBox monthAssignCheckBox7;
    private JCheckBox monthAssignCheckBox8;
    private JCheckBox monthAssignCheckBox9;
    private JCheckBox monthAssignCheckBox10;
    private JCheckBox monthAssignCheckBox11;
    private JCheckBox monthAssignCheckBox12;
    private JCheckBox wAssignCheckBox2;
    private JCheckBox wAssignCheckBox3;
    private JCheckBox wAssignCheckBox4;
    private JCheckBox wAssignCheckBox5;
    private JCheckBox wAssignCheckBox6;
    private JCheckBox wAssignCheckBox7;
    private JCheckBox hAssignCheckBox1;
    private JCheckBox hAssignCheckBox2;
    private JCheckBox hAssignCheckBox3;
    private JCheckBox hAssignCheckBox4;
    private JCheckBox hAssignCheckBox5;
    private JCheckBox hAssignCheckBox6;
    private JCheckBox hAssignCheckBox7;
    private JCheckBox hAssignCheckBox8;
    private JCheckBox hAssignCheckBox9;
    private JCheckBox hAssignCheckBox10;
    private JCheckBox hAssignCheckBox12;
    private JCheckBox hAssignCheckBox11;
    private JCheckBox hAssignCheckBox13;
    private JCheckBox hAssignCheckBox14;
    private JCheckBox hAssignCheckBox15;
    private JCheckBox hAssignCheckBox16;
    private JCheckBox hAssignCheckBox17;
    private JCheckBox hAssignCheckBox18;
    private JCheckBox hAssignCheckBox19;
    private JCheckBox hAssignCheckBox20;
    private JCheckBox hAssignCheckBox21;
    private JCheckBox hAssignCheckBox22;
    private JCheckBox hAssignCheckBox23;
    private JSpinner wSpinner5;

    public CronUi(@Nullable Project project) throws Exception {
        super(project);
        setTitle("Cron表达式工具");
        setCancelButtonText("关闭");
        setOKButtonText("刷新运行时间");
        //创建按钮组以及添加按钮到组别
        sArroundStartSpinner.setModel(new SpinnerNumberModel(0, 0, 59, 1));
        sArroundEndSpinner.setModel(new SpinnerNumberModel(0, 0, 59, 1));
        sIntervalStartSpinner.setModel(new SpinnerNumberModel(0, 0, 59, 1));
        sIntervalEndSpinner.setModel(new SpinnerNumberModel(0, 0, 59, 1));
        ySpinner1.setModel(new YearSpinnerModel(new SimpleDateFormat("yyyy-MM-dd").parse("2015-01-01"), Locale.CHINA, TimeZone.getTimeZone(ZoneId.systemDefault())));
        ySpinner2.setModel(new YearSpinnerModel(new SimpleDateFormat("yyyy-MM-dd").parse("2099-01-01"), Locale.CHINA, TimeZone.getTimeZone(ZoneId.systemDefault())));
        setDefaultValue();

        buildCronExpButton.addActionListener(e -> {
            try {
                cronExprTextField.setText(buildCronExp());
                //设置最近10次的运行时间
                setCurTenExcuteTime();
            } catch (Exception ex) {
                Messages.showErrorDialog(ex.getMessage(), "构建Cron表达式异常");
            }
        });
        init();
    }

    @Override
    protected void doOKAction() {
        setCurTenExcuteTime();
    }

    private void setCurTenExcuteTime() {
        try {
            //设置最近10次的运行时间
            CronExpression cronExpression = new CronExpression(cronExprTextField.getText());
            recTenExcuteTextArea.setText("");
            Date date = new Date();
            for (int i = 0; i < 10; i++) {
                Date nextValidTimeAfter = cronExpression.getNextValidTimeAfter(date);
                if (nextValidTimeAfter != null) {
                    recTenExcuteTextArea.append(DateFormatUtil.formatDate(nextValidTimeAfter) + " " + DateFormatUtil.formatTimeWithSeconds(nextValidTimeAfter) + "\n");
                    date = new Date(nextValidTimeAfter.getTime() + 100);
                }
            }
        } catch (Exception ex) {
            Messages.showErrorDialog(ex.getMessage(), "构建Cron表达式异常");
        }
    }

    private void setDefaultValue() {
        sRadioButtonAssign.addChangeListener(e -> {
            if (sRadioButtonAssign.isSelected()) {
                //判断有被选择
                ArrayList<JCheckBox> selectCheckBoxs = null;
                try {
                    selectCheckBoxs = getSelectCheckBoxs(CronExpTypeEnum.SECOND);
                    if (selectCheckBoxs.isEmpty()) {
                        sAssignCheckBox0.setSelected(true);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        mRadioButtonAssign.addChangeListener(e -> {
            if (mRadioButtonAssign.isSelected()) {
                //判断有被选择
                ArrayList<JCheckBox> selectCheckBoxs = null;
                try {
                    selectCheckBoxs = getSelectCheckBoxs(CronExpTypeEnum.MIMUTE);
                    if (selectCheckBoxs.isEmpty()) {
                        mAssignCheckBox0.setSelected(true);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        hRadioButtonAssign.addChangeListener(e -> {
            if (hRadioButtonAssign.isSelected()) {
                //判断有被选择
                ArrayList<JCheckBox> selectCheckBoxs = null;
                try {
                    selectCheckBoxs = getSelectCheckBoxs(CronExpTypeEnum.HOUR);
                    if (selectCheckBoxs.isEmpty()) {
                        hAssignCheckBox0.setSelected(true);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        dRadioButtonAssign.addChangeListener(e -> {
            if (dRadioButtonAssign.isSelected()) {
                //判断有被选择
                ArrayList<JCheckBox> selectCheckBoxs = null;
                try {
                    selectCheckBoxs = getSelectCheckBoxs(CronExpTypeEnum.DAY);
                    if (selectCheckBoxs.isEmpty()) {
                        dAssignCheckBox1.setSelected(true);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        monthRadioButtonAssign.addChangeListener(e -> {
            if (monthRadioButtonAssign.isSelected()) {
                //判断有被选择
                ArrayList<JCheckBox> selectCheckBoxs = null;
                try {
                    selectCheckBoxs = getSelectCheckBoxs(CronExpTypeEnum.MONTH);
                    if (selectCheckBoxs.isEmpty()) {
                        monthAssignCheckBox1.setSelected(true);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        wRadioButtonAssign.addChangeListener(e -> {
            if (wRadioButtonAssign.isSelected()) {
                //判断有被选择
                ArrayList<JCheckBox> selectCheckBoxs = null;
                try {
                    selectCheckBoxs = getSelectCheckBoxs(CronExpTypeEnum.WEEK);
                    if (selectCheckBoxs.isEmpty()) {
                        wAssignCheckBox1.setSelected(true);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return jPanel;
    }


    private String buildCronExp() throws Exception {
        return getSecond() + " "
                + getMinute() + " "
                + getHour() + " "
                + getDay() + " "
                + getMonth() + " "
                + getWeek() + " "
                + getYear();
    }

    /***
     * 获取秒的表达式
     * @return
     */
    private String getSecond() throws Exception {
        //判断哪个按钮选中
        if (sRadioButtonEvery.isSelected()) {
            return "*";
        }
        if (mRadioButtonArround.isSelected()) {
            return sArroundStartSpinner.getValue() + "-" + sArroundEndSpinner.getValue();
        }
        if (sRadioButtonInterval.isSelected()) {
            return sIntervalStartSpinner.getValue() + "/" + sIntervalEndSpinner.getValue();
        }
        if (sRadioButtonAssign.isSelected()) {
            StringBuilder stringBuilder = new StringBuilder();
            ArrayList<JCheckBox> selectCheckBoxs = getSelectCheckBoxs(CronExpTypeEnum.SECOND);
            for (int i = 0; i < selectCheckBoxs.size(); i++) {
                if (i != 0) {
                    stringBuilder.append(",");
                }
                stringBuilder.append(selectCheckBoxs.get(i).getText());
            }
            return stringBuilder.toString();
        }
        throw new RuntimeException("获取选择类型异常");
    }

    /***
     * 获取秒的表达式
     * @return
     */
    private String getMinute() throws Exception {
        //判断哪个按钮选中
        if (mRadioButtonEvery.isSelected()) {
            return "*";
        }
        if (mRadioButtonArround.isSelected()) {
            return sArroundStartSpinner.getValue() + "-" + sArroundEndSpinner.getValue();
        }
        if (mRadioButtonInterval.isSelected()) {
            return sIntervalStartSpinner.getValue() + "/" + sIntervalEndSpinner.getValue();
        }
        if (mRadioButtonAssign.isSelected()) {
            StringBuilder stringBuilder = new StringBuilder();
            ArrayList<JCheckBox> selectCheckBoxs = getSelectCheckBoxs(CronExpTypeEnum.MIMUTE);
            for (int i = 0; i < selectCheckBoxs.size(); i++) {
                if (i != 0) {
                    stringBuilder.append(",");
                }
                stringBuilder.append(selectCheckBoxs.get(i).getText());
            }
            return stringBuilder.toString();
        }
        throw new RuntimeException("获取选择类型异常");
    }

    private String getHour() throws Exception {
        //判断哪个按钮选中
        if (hRadioButtonEvery.isSelected()) {
            return "*";
        }
        if (hRadioButtonArround.isSelected()) {
            return hspinner1.getValue() + "-" + hspinner2.getValue();
        }
        if (hRadioButtonInterval.isSelected()) {
            return hspinner3.getValue() + "/" + hspinner4.getValue();
        }
        if (hRadioButtonAssign.isSelected()) {
            StringBuilder stringBuilder = new StringBuilder();
            ArrayList<JCheckBox> selectCheckBoxs = getSelectCheckBoxs(CronExpTypeEnum.HOUR);
            for (int i = 0; i < selectCheckBoxs.size(); i++) {
                if (i != 0) {
                    stringBuilder.append(",");
                }
                stringBuilder.append(selectCheckBoxs.get(i).getText());
            }
            return stringBuilder.toString();
        }
        throw new RuntimeException("获取选择类型异常");
    }

    private String getDay() throws Exception {
        //判断哪个按钮选中
        if (dRadioButtonEvery.isSelected()) {
            return "*";
        }
        if (dRadioButtonNone.isSelected()) {
            return "?";
        }
        if (dRadioButtonArround.isSelected()) {
            return dspinner1.getValue() + "-" + dspinner2.getValue();
        }
        if (dRadioButtonInterval.isSelected()) {
            return dspinner3.getValue() + "/" + dspinner4.getValue();
        }
        if (dRadioButtonWdayOfIntervalDay.isSelected()) {
            return dspinner5.getValue() + "W";
        }
        if (dRadioButtonLastDayOfMOnth.isSelected()) {
            return "L";
        }
        if (dRadioButtonAssign.isSelected()) {
            StringBuilder stringBuilder = new StringBuilder();
            ArrayList<JCheckBox> selectCheckBoxs = getSelectCheckBoxs(CronExpTypeEnum.DAY);
            for (int i = 0; i < selectCheckBoxs.size(); i++) {
                if (i != 0) {
                    stringBuilder.append(",");
                }
                stringBuilder.append(selectCheckBoxs.get(i).getText());
            }
            return stringBuilder.toString();
        }
        throw new RuntimeException("获取选择类型异常");
    }

    private String getMonth() throws Exception {
        //判断哪个按钮选中
        if (monthRadioButtonEvery.isSelected()) {
            return "*";
        }
        if (monthRadioButtonArround.isSelected()) {
            return monthSpinner1.getValue() + "-" + monthSpinner2.getValue();
        }
        if (monthRadioButtonInterval.isSelected()) {
            return monthSpinner3.getValue() + "/" + monthSpinner4.getValue();
        }
        if (monthRadioButtonAssign.isSelected()) {
            StringBuilder stringBuilder = new StringBuilder();
            ArrayList<JCheckBox> selectCheckBoxs = getSelectCheckBoxs(CronExpTypeEnum.MONTH);
            for (int i = 0; i < selectCheckBoxs.size(); i++) {
                if (i != 0) {
                    stringBuilder.append(",");
                }
                stringBuilder.append(selectCheckBoxs.get(i).getText());
            }
            return stringBuilder.toString();
        }
        throw new RuntimeException("获取选择类型异常");
    }

    private String getWeek() throws Exception {
        //判断哪个按钮选中
        if (wRadioButtonEvery.isSelected()) {
            return "*";
        }
        if (wRadioButtonNone.isSelected()) {
            return "?";
        }
        if (wRadioButtonArround.isSelected()) {
            return wSpinner1.getValue() + "/" + wSpinner2.getValue();
        }
        if (wRadioButtonInterval.isSelected()) {
            return wSpinner3.getValue() + "#" + wSpinner4.getValue();
        }
        if (wRadioButtonLastw.isSelected()) {
            return wSpinner5.getValue() + "L";
        }
        if (wRadioButtonAssign.isSelected()) {
            StringBuilder stringBuilder = new StringBuilder();
            ArrayList<JCheckBox> selectCheckBoxs = getSelectCheckBoxs(CronExpTypeEnum.DAY);
            for (int i = 0; i < selectCheckBoxs.size(); i++) {
                if (i != 0) {
                    stringBuilder.append(",");
                }
                stringBuilder.append(selectCheckBoxs.get(i).getText());
            }
            return stringBuilder.toString();
        }
        throw new RuntimeException("获取选择类型异常");
    }

    private String getYear() throws Exception {
        //判断哪个按钮选中
        if (yRadioButtonNone.isSelected()) {
            return "";
        }
        if (yRadioButtonEvery.isSelected()) {
            return "*";
        }
        if (yRadioButtonArround.isSelected()) {
            return ySpinner1.getValue() + "-" + ySpinner2.getValue();
        }
        throw new RuntimeException("获取选择类型异常");
    }

    /**
     * 获取checkbox选择的值
     *
     * @param cronExpTypeEnum
     * @return
     * @throws Exception
     */
    private ArrayList<JCheckBox> getSelectCheckBoxs(CronExpTypeEnum cronExpTypeEnum) throws Exception {

        ArrayList<JCheckBox> results = new ArrayList<>();

        switch (cronExpTypeEnum) {
            case SECOND:
                for (int i = 0; i < 60; i++) {
                    JCheckBox sAssignCheckBox = (JCheckBox) this.getClass().getDeclaredField("sAssignCheckBox" + i).get(this);
                    if (sAssignCheckBox.isSelected()) {
                        results.add(sAssignCheckBox);
                    }
                }
                break;
            case MIMUTE:
                for (int i = 0; i < 60; i++) {
                    JCheckBox mAssignCheckBox = (JCheckBox) this.getClass().getDeclaredField("mAssignCheckBox" + i).get(this);
                    if (mAssignCheckBox.isSelected()) {
                        results.add(mAssignCheckBox);
                    }
                }
                break;
            case HOUR:
                for (int i = 0; i < 24; i++) {
                    JCheckBox hAssignCheckBox = (JCheckBox) this.getClass().getDeclaredField("hAssignCheckBox" + i).get(this);
                    if (hAssignCheckBox.isSelected()) {
                        results.add(hAssignCheckBox);
                    }
                }
                break;
            case DAY:
                for (int i = 0; i < 31; i++) {
                    JCheckBox dAssignCheckBox = (JCheckBox) this.getClass().getDeclaredField("dAssignCheckBox" + (i + 1)).get(this);
                    if (dAssignCheckBox.isSelected()) {
                        results.add(dAssignCheckBox);
                    }
                }
                break;
            case MONTH:
                for (int i = 0; i < 12; i++) {
                    JCheckBox monthAssignCheckBox = (JCheckBox) this.getClass().getDeclaredField("monthAssignCheckBox" + (i + 1)).get(this);
                    if (monthAssignCheckBox.isSelected()) {
                        results.add(monthAssignCheckBox);
                    }
                }
                break;
            case WEEK:
                for (int i = 0; i < 7; i++) {
                    JCheckBox monthAssignCheckBox = (JCheckBox) this.getClass().getDeclaredField("wAssignCheckBox1" + (i + 1)).get(this);
                    if (monthAssignCheckBox.isSelected()) {
                        results.add(monthAssignCheckBox);
                    }
                }
                break;
            case YEAR:
                break;
            default:
                break;
        }
        return results;
    }

}
