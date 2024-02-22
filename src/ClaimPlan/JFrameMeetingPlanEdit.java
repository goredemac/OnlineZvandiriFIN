/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClaimPlan;

import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.Timer;
import java.util.Date;
import java.awt.event.ActionListener;
import java.util.Date;
import java.awt.event.ActionEvent;
import java.net.InetAddress;
import java.time.LocalDate;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.Properties;
import ClaimAppendix1.*;
import ClaimAppendix2.*;
import ClaimReports.*;
import java.time.temporal.ChronoUnit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.text.DateFormat;
import java.time.Period;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.connCred;
import utils.timeHost;

/**
 *
 * @author goredemac
 */
public class JFrameMeetingPlanEdit extends javax.swing.JFrame {

    connCred c = new connCred();
    timeHost tH = new timeHost();
    DefaultTableModel modelWk1, modelWk2, modelWk3, modelWk4, modelWk5;
    boolean ignoreInput = false;
    String filename = null;
    int charMaxWk1 = 200;
    int charMaxWk2 = 200;
    int charMaxWk3 = 200;
    int charMaxWk4 = 200;
    int charMaxWk5 = 200;
    double totReqAmt = 0;
    double totBudgetLnReqAmt = 0;
    Date curYear = new Date();
    int row, col, tabNam, finyear, month;
    SimpleDateFormat df = new SimpleDateFormat("yyyy");
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String wk1Site = "N";
    String wk2Site = "N";
    String wk3Site = "N";
    String wk4Site = "N";
    String wk5Site = "N";
    String meetBudWk1 = "N";
    String meetBudWk2 = "N";
    String meetBudWk3 = "N";
    String meetBudWk4 = "N";
    String meetBudWk5 = "N";
    String perRespWk1 = "N";
    String perRespWk2 = "N";
    String perRespWk3 = "N";
    String perRespWk4 = "N";
    String perRespWk5 = "N";
    String hostName = "";
    String breakfastAll, lunchAll, lunchNPAll, lunchPAll, dinnerAll, usrGrp, supNam, supUsrMail,
            incidentalAll, unProvedAll, provedAll, date1, date2, usrnam, docVer, docNextVer, editName, planStatus,
            sendToProvMgr, provMgrMail, usrRecNam, UsrRecWk, actDate, SupNamSend, usrActType,staffName1, staffName2,
            staffName3, staffName4,branchCode, prjCode, taskCode,lastDateofMonth;
    PreparedStatement pst1 = null;
    PreparedStatement pst = null;
    int itmNum = 1;
    int planRefCount = 0;
    java.util.Date origDate;

    /**
     * Creates new form JFrameMnthPlanPerDiemCreate
     */
    public JFrameMeetingPlanEdit() {
        initComponents();
        findProvince();
        allowanceRate();
        // showDate();
        showTime();
        computerName();

    }

    public JFrameMeetingPlanEdit(String usrLogNam) {
        initComponents();
        modelWk1 = (DefaultTableModel) jTableWk1Activities.getModel();
        modelWk2 = (DefaultTableModel) jTableWk2Activities.getModel();
        modelWk3 = (DefaultTableModel) jTableWk3Activities.getModel();
        modelWk4 = (DefaultTableModel) jTableWk4Activities.getModel();
        modelWk5 = (DefaultTableModel) jTableWk5Activities.getModel();
        findProvince();
        allowanceRate();
        try {
            //showDate();
            tH.intShowDate();
        } catch (IOException ex) {
            Logger.getLogger(JFrameMeetingPlanEdit.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(JFrameMeetingPlanEdit.class.getName()).log(Level.SEVERE, null, ex);
        }
        showTime();
        computerName();
        jLabelEmp.setText(usrLogNam);
        jLabelEmp.setVisible(false);
        jMenuItemRequest.setEnabled(false);
        jMenuItemSpecialAcquittal.setEnabled(false);
        jLabelWk1Name1Del.setVisible(false);
        jLabelWk1Name2Del.setVisible(false);
        jLabelWk1Name3Del.setVisible(false);
        jLabelWk1Name4Del.setVisible(false);

        jLabelPostAppMod1.setText("");
        jLabelPostAppMod2.setText("");
        jLabelPostAppMod3.setText("");
        jLabelPostAppMod4.setText("");
        jLabelPostAppMod5.setText("");
        jLabelLineDate.setText(tH.internetDate);
        jLabelLineDate1.setText(tH.internetDate);
        jLabelLineDate2.setText(tH.internetDate);
        jLabelLineDate3.setText(tH.internetDate);
        jLabelLineDate4.setText(tH.internetDate);
        jLabelLineDate5.setText(tH.internetDate);
        findUser();
        findUserGrp();
        getYear();
        findMeeting();
        findProject();
    }

    void computerName() {

        try {
            java.net.InetAddress addr = InetAddress.getLocalHost();
            hostName = addr.getHostName();

        } catch (Exception ex) {
            System.out.println("Hostname can not be resolved");
        }
    }

    void getYear() {
        GregorianCalendar date = new GregorianCalendar();
        month = date.get(Calendar.MONTH);
        month = month + 1;

        if ((month >= 1) && (month <= 9)) {
            finyear = Integer.parseInt(df.format(curYear));
        } else {
            finyear = Integer.parseInt(df.format(curYear)) + 1;
        }
    }

    void showTime() {
        new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date d = new Date();
                SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss a");

                jLabelLineTime.setText(s.format(d));
                jLabelLineTime1.setText(s.format(d));
                jLabelLineTime2.setText(s.format(d));
                jLabelLineTime3.setText(s.format(d));
                jLabelLineTime4.setText(s.format(d));
                jLabelLineTime5.setText(s.format(d));

            }
        }) {

        }.start();

    }

    void findUserGrp() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT *  FROM [ClaimsAppSysZvandiri].[dbo].[EmpLogGrpTab]"
                    + "where emp_num ='" + jLabelEmp.getText() + "'");

            while (r.next()) {

                usrGrp = r.getString(3);

            }

            if ("usrGenSp".equals(usrGrp)) {

                jMenuItemSupApp.setEnabled(false);
                jMenuItemAccMgrRev.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuPlanApproval.setEnabled(false);
                jMenuItemSchGen.setEnabled(false);
                jMenuItemAcqSupApp.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuItemAcqFinApp.setEnabled(false);
                jMenuItemAcqSchGen.setEnabled(false);
                jMenuAgeAlluser.setEnabled(false);
                jMenuAgeByUser.setEnabled(false);

            }

            if ("usrGen".equals(usrGrp)) {

                jMenuItemSupApp.setEnabled(false);
                jMenuItemAccMgrRev.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuPlanApproval.setEnabled(false);
                jMenuItemSchGen.setEnabled(false);
                jMenuItemAcqSupApp.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuItemAcqFinApp.setEnabled(false);
                jMenuItemAcqSchGen.setEnabled(false);
                jMenuAgeAlluser.setEnabled(false);
                jMenuAgeByUser.setEnabled(false);
                jMenuItemSpecialAcquittal.setEnabled(false);
                jMenuProcessTimeline.setEnabled(false);
                jMenuMonPlanEdit.setEnabled(false);
                jMenuItem3WkPlan.setEnabled(false);
               
                jMenuItemPlanView.setEnabled(false);
            }

            if ("usrGenPlan".equals(usrGrp)) {

                jMenuItemSupApp.setEnabled(false);
                jMenuItemAccMgrRev.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuPlanApproval.setEnabled(false);
                jMenuItemSchGen.setEnabled(false);
                jMenuItemAcqSupApp.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuItemAcqFinApp.setEnabled(false);
                jMenuItemAcqSchGen.setEnabled(false);
                jMenuAgeAlluser.setEnabled(false);
                jMenuAgeByUser.setEnabled(false);
                jMenuItemSpecialAcquittal.setEnabled(false);
                jMenuProcessTimeline.setEnabled(false);
               
                jMenuItemPlanView.setEnabled(false);
            }

            if ("usrSup".equals(usrGrp)) {

                jMenuItemAccMgrRev.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuPlanApproval.setEnabled(false);
                jMenuItemSchGen.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuItemAcqFinApp.setEnabled(false);
                jMenuItemAcqSchGen.setEnabled(false);
            }

            if ("usrProvMgr".equals(usrGrp)) {

                jMenuItemAccMgrRev.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuPlanApproval.setEnabled(false);
                jMenuItemSchGen.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuItemAcqFinApp.setEnabled(false);
                jMenuItemAcqSchGen.setEnabled(false);

            }
            if ("usrSupAcc".equals(usrGrp)) {

                jMenuItemAccMgrRev.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
               

            }

            if ("usrAccMgr".equals(usrGrp)) {

                jMenuItemSupApp.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuPlanApproval.setEnabled(false);
                jMenuItemSchGen.setEnabled(false);
                jMenuItemAcqSupApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuItemAcqFinApp.setEnabled(false);
                jMenuItemAcqSchGen.setEnabled(false);
               

            }

            if ("usrHead".equals(usrGrp)) {

                jMenuItemAccMgrRev.setEnabled(false);
                jMenuPlanApproval.setEnabled(false);
                jMenuItemSchGen.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqFinApp.setEnabled(false);
                jMenuItemAcqSchGen.setEnabled(false);
            }

            if ("usrFinReq".equals(usrGrp)) {

                jMenuItemSupApp.setEnabled(false);
                jMenuItemAccMgrRev.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuItemAcqSupApp.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuMonPlanEdit.setEnabled(false);
                jMenuItem3WkPlan.setEnabled(false);
               
                jMenuItemPlanView.setEnabled(false);

            }

            if ("usrFinAcq".equals(usrGrp)) {

                jMenuItemSupApp.setEnabled(false);
                jMenuItemAccMgrRev.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuPlanApproval.setEnabled(false);
                jMenuItemSchGen.setEnabled(false);
                jMenuItemAcqSupApp.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuMonPlanEdit.setEnabled(false);
                jMenuItem3WkPlan.setEnabled(false);
               
                jMenuItemPlanView.setEnabled(false);

            }

            if ("usrAdm".equals(usrGrp)) {

                jMenuItemSupApp.setEnabled(false);
                jMenuItemAccMgrRev.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuPlanApproval.setEnabled(false);
                jMenuItemSchGen.setEnabled(false);
                jMenuItemAcqSupApp.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuItemAcqFinApp.setEnabled(false);
                jMenuItemAcqSchGen.setEnabled(false);

            }
        } catch (Exception e) {
            //  JOptionPane.showMessageDialog(null, "Failed to Connect to Database (Province).Try Again", "Error Connection", JOptionPane.WARNING_MESSAGE);
            System.out.println(e);
        }
    }

    void findProvMan() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT EMP_NAM  ,Emp_Mail FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[ProvManTab] where  province ='"
                    + jLabelProvince.getText() + "'");
            while (r.next()) {
                sendToProvMgr = r.getString(1);
                provMgrMail = r.getString(2);

            }

            //                 conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Connect to Database (Province).Try Again", "Error Connection", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
    }

    void findUser() {
        try {

Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select * from [ClaimsAppSysZvandiri].[dbo]."
                    + "[EmpDetTab] where emp_num ='" + jLabelEmp.getText() + "'");

            while (r.next()) {

                jLabelLineLogNam.setText(r.getString(2));
                jLabelLineLogNam1.setText(r.getString(2));
                jLabelLineLogNam2.setText(r.getString(2));
                jLabelLineLogNam3.setText(r.getString(2));
                jLabelLineLogNam4.setText(r.getString(2));
                jLabelLineLogNam5.setText(r.getString(2));

//                sendTo = r.getString(5);
//                createUsrNam = r.getString(2);
                supUsrMail = r.getString(6);
                supNam = r.getString(5);
            }

            //                 conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Connect to Database (Province).Try Again", "Error Connection", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
    }

    void regInitCheck() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            
            Statement st = conn.createStatement();

            st.executeQuery("SELECT count(*) FROM [ClaimsAppSysZvandiri].[dbo].[PlanActTab] "
                    + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'A' "
                    + "and DOC_STATUS = 'Amend Request'");

            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                planRefCount = rs.getInt(1);

            }

            Statement st1 = conn.createStatement();

            st1.executeQuery("SELECT DOC_STATUS,ACTION_BY,ACT_REC_STA FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[PlanActTab] where PLAN_REF_NUM =" + jTextFieldRefNum.getText()
                    + "and ACT_REC_STA = 'C'");

            ResultSet rs1 = st1.getResultSet();

            while (rs1.next()) {
                planStatus = rs1.getString(1);
                editName = rs1.getString(2);
            }

            if (planRefCount == 1) {
                fetchdataWk1();
                fetchdataWk2();
                fetchdataWk3();
                fetchdataWk4();
                fetchdataWk5();
                fetchdataMeetWk1();
                fetchdataMeetWk2();
                fetchdataMeetWk3();
                fetchdataMeetWk4();
                fetchdataMeetWk5();
                fetchdataGenWk1();
                findProvMan();
            } else if ("Approved".equals(planStatus) && jLabelLineLogNam.getText().equals(editName)) {
                fetchdataWkC1();
                fetchdataWkC2();
                fetchdataWkC3();
                fetchdataWkC4();
                fetchdataWkC5();
                fetchdataMeetWkC1();
                fetchdataMeetWkC2();
                fetchdataMeetWkC3();
                fetchdataMeetWkC4();
                fetchdataMeetWkC5();
                fetchdataGenWkC1();
                findProvMan();
            } else {
                JOptionPane.showMessageDialog(null, "Plan number is invalid or cannot be editted.",
                        "Invalid Reference Number", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findProvince() {
        try {

Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            //jComboProvince.setSelectedIndex(-1);
            ResultSet r = st.executeQuery("select distinct(province) from [ClaimsAppSysZvandiri].[dbo].[DistFacTab]");

            while (r.next()) {

                //   jComboProvince.addItem(r.getString("province"));
                jComboProvinceFacility.addItem(r.getString("province"));
//                jComboProvince.addItem(r.getString("province"));

            }

            //                 conn.close();
        } catch (Exception e) {

        }
    }

    void facDist() {
        try {

            if (((jComboDistrictFacility.getSelectedItem().toString().equals(jComboFacility.getSelectedItem().toString()))
                    || ("Central Office".equals(jLabelProvince.getText())) || ("Harare Province".equals(jLabelProvince.getText()))) && ("".equals(jTextDistDest.getText()))) {
                jLabelDist.setVisible(true);
                jTextDistDest.setVisible(true);
                jTextDistDest.setText("");
                jTextDistDest.requestFocusInWindow();
                jTextDistDest.setFocusable(true);
            } else if (!(jComboDistrictFacility.getSelectedItem().toString().equals(jComboFacility.getSelectedItem().toString()))
                    || !("Central Office".equals(jLabelProvince.getText())) || !("Harare Province".equals(jLabelProvince.getText()))) {
                jLabelDist.setVisible(false);
                jTextDistDest.setVisible(false);
                jTextDistDest.setText("");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void facilityPOP() {
        jDialogFacility.setVisible(true);
        jDialogFacility.setVisible(false);
        jDialogFacility.setVisible(true);
    }

    void budgetPOP() {
        jDialogBudget.setVisible(true);
        jDialogBudget.setVisible(false);
        jDialogBudget.setVisible(true);
    }

    void findProject() {
        try {

     Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
     
            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            Statement st3 = conn.createStatement();
            Statement st4 = conn.createStatement();
            Statement st5 = conn.createStatement();

            jComboActivityProjectWk1.setSelectedIndex(-1);

            ResultSet r = st.executeQuery("SELECT distinct (PROJ_NAME)  FROM "
                    + "[HRLeaveSysZvandiri].[dbo].[RecProjectTab] order by 1");

            while (r.next()) {

                jComboActivityProjectWk1.addItem(r.getString("PROJ_NAME"));

            }

            jComboActivityProjectWk2.setSelectedIndex(-1);

            ResultSet r1 = st1.executeQuery("SELECT distinct (PROJ_NAME)  FROM "
                    + "[HRLeaveSysZvandiri].[dbo].[RecProjectTab] order by 1");

            while (r1.next()) {

                jComboActivityProjectWk2.addItem(r1.getString("PROJ_NAME"));

            }

            jComboActivityProjectWk3.setSelectedIndex(-1);

            ResultSet r3 = st3.executeQuery("SELECT distinct (PROJ_NAME)  FROM "
                    + "[HRLeaveSysZvandiri].[dbo].[RecProjectTab] order by 1");

            while (r3.next()) {

                jComboActivityProjectWk3.addItem(r3.getString("PROJ_NAME"));

            }

            jComboActivityProjectWk4.setSelectedIndex(-1);

            ResultSet r4 = st4.executeQuery("SELECT distinct (PROJ_NAME)  FROM "
                    + "[HRLeaveSysZvandiri].[dbo].[RecProjectTab] order by 1");

            while (r4.next()) {

                jComboActivityProjectWk4.addItem(r4.getString("PROJ_NAME"));

            }

            jComboActivityProjectWk5.setSelectedIndex(-1);

            ResultSet r5 = st5.executeQuery("SELECT distinct (PROJ_NAME)  FROM "
                    + "[HRLeaveSysZvandiri].[dbo].[RecProjectTab] order by 1");

            while (r5.next()) {

                jComboActivityProjectWk5.addItem(r5.getString("PROJ_NAME"));

            }

            conn.close();
        } catch (Exception e) {
            System.out.println();
        }
    }

      void findTask(String prjCode) {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            jComboProjectTask.setSelectedIndex(-1);

            ResultSet r = st.executeQuery("SELECT concat(PRJ_TASK_CODE,' ',TASK_DESC)  "
                    + "FROM [HRLeaveSysZvandiri].[dbo].[ProjectTaskTab]  "
                    + "where PRJ_CODE ='" + prjCode + "' order by 1");

            while (r.next()) {

                jComboProjectTask.addItem(r.getString(1));

            }

            conn.close();
        } catch (Exception e) {
            System.out.println();
        }
    }

    void findBranch(String taskCode) {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT distinct BRANCH  FROM [ClaimsAppSysZvandiri].[dbo].[BudCodeTab] "
                    + "where PRJ_TASK_CODE ='" + taskCode + "' order by 1");

            while (r.next()) {

                branchCode = r.getString(1);

            }

            conn.close();
        } catch (Exception e) {
            System.out.println();
        }
    }

    void findLastMonthDay() {
        try {
            Date today = jDateChooserWk1From.getDate();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);

            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);

            Date lastDayOfMonth = calendar.getTime();

            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            lastDateofMonth = sdf.format(lastDayOfMonth);
            System.out.println("Today            : " + sdf.format(today));
            System.out.println("Last Day of Month: " + sdf.format(lastDayOfMonth));
        } catch (Exception e) {
        }
    }

    void findMeeting() {
        try {

    Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
    
            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            jComboMeetingWk1.setSelectedIndex(-1);
            jComboMeetingWk2.setSelectedIndex(-1);
            jComboMeetingWk3.setSelectedIndex(-1);
            jComboMeetingWk4.setSelectedIndex(-1);
            jComboMeetingWk5.setSelectedIndex(-1);

            ResultSet r = st.executeQuery("SELECT distinct (ACTIVITY_NAM)  FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[PlanMeetingActTab] where ACTIVITY_TYPE = 'Meeting' order by 1");

            while (r.next()) {

                jComboMeetingWk1.addItem(r.getString("ACTIVITY_NAM"));
                jComboMeetingWk2.addItem(r.getString("ACTIVITY_NAM"));
                jComboMeetingWk3.addItem(r.getString("ACTIVITY_NAM"));
                jComboMeetingWk4.addItem(r.getString("ACTIVITY_NAM"));
                jComboMeetingWk5.addItem(r.getString("ACTIVITY_NAM"));

            }

            jComboMeetAllowanceWk1.setSelectedIndex(-1);
            jComboMeetAllowanceWk2.setSelectedIndex(-1);
            jComboMeetAllowanceWk3.setSelectedIndex(-1);
            jComboMeetAllowanceWk4.setSelectedIndex(-1);
            jComboMeetAllowanceWk5.setSelectedIndex(-1);

            ResultSet r1 = st.executeQuery("SELECT distinct (ACTIVITY_NAM)  FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[PlanMeetingActTab] where ACTIVITY_TYPE = 'Allowance' order by 1");

            while (r1.next()) {

                jComboMeetAllowanceWk1.addItem(r1.getString("ACTIVITY_NAM"));
                jComboMeetAllowanceWk2.addItem(r1.getString("ACTIVITY_NAM"));
                jComboMeetAllowanceWk3.addItem(r1.getString("ACTIVITY_NAM"));
                jComboMeetAllowanceWk4.addItem(r1.getString("ACTIVITY_NAM"));
                jComboMeetAllowanceWk5.addItem(r1.getString("ACTIVITY_NAM"));

            }
            conn.close();
        } catch (Exception e) {
            System.out.println();
        }
    }

    void allowanceRate() {
        try {
            String rateCat;
            if ("National Office".equals(jLabelDistrict.getText())) {
                rateCat = "A";
            } else {
                rateCat = "B";
            }
           
       Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT Breakfast,Lunch,Dinner,Incidental,"
                    + "Proved_Acc ,Unproved_Acc FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAllowanceTab] "
                    + "where RateOrg = 'ZimTTECH' and RateStatus ='" + rateCat + "'");

            while (r.next()) {

                breakfastAll = r.getString(1);
                lunchAll = r.getString(2);
                dinnerAll = r.getString(3);
                incidentalAll = r.getString(4);
                provedAll = r.getString(5);
                unProvedAll = r.getString(6);

            }
                         conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void UsrRecEditUpd() {
        try {
           Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
           
            String sql1 = "update [ClaimsAppSysZvandiri].[dbo].[PlanUsrRecTab] set "
                    + "STATUS ='P' where PLAN_REF_NUM=" + jTextFieldRefNum.getText();

            pst = conn.prepareStatement(sql1);
            pst.executeUpdate();

            for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
                if (!("".equals(jTableWk1Activities.getValueAt(i, 16).toString()))) {
                    usrRecNam = jTableWk1Activities.getValueAt(i, 16).toString();
                    actDate = jTableWk1Activities.getValueAt(i, 0).toString();
                    UsrRecWk = "1";
                    usrActType = "Per Diem";
                    WkUsrRecUpd();
                }
            }

            for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
                if (!("".equals(jTableWk1Activities.getValueAt(i, 17).toString()))) {
                    usrRecNam = jTableWk1Activities.getValueAt(i, 17).toString();
                    actDate = jTableWk1Activities.getValueAt(i, 0).toString();
                    UsrRecWk = "1";
                    usrActType = "Per Diem";
                    WkUsrRecUpd();
                }
            }
            for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
                if (!("".equals(jTableWk1Activities.getValueAt(i, 18).toString()))) {
                    usrRecNam = jTableWk1Activities.getValueAt(i, 18).toString();
                    actDate = jTableWk1Activities.getValueAt(i, 0).toString();
                    UsrRecWk = "1";
                    usrActType = "Per Diem";
                    WkUsrRecUpd();
                }
            }
            for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
                if (!("".equals(jTableWk1Activities.getValueAt(i, 19).toString()))) {
                    usrRecNam = jTableWk1Activities.getValueAt(i, 19).toString();
                    actDate = jTableWk1Activities.getValueAt(i, 0).toString();
                    UsrRecWk = "1";
                    usrActType = "Per Diem";
                    WkUsrRecUpd();
                }
            }

            for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
                if (!("".equals(jTableWk2Activities.getValueAt(i, 16).toString()))) {
                    usrRecNam = jTableWk2Activities.getValueAt(i, 16).toString();
                    actDate = jTableWk2Activities.getValueAt(i, 0).toString();
                    UsrRecWk = "2";
                    usrActType = "Per Diem";
                    WkUsrRecUpd();
                }
            }

            for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
                if (!("".equals(jTableWk2Activities.getValueAt(i, 17).toString()))) {
                    usrRecNam = jTableWk2Activities.getValueAt(i, 17).toString();
                    actDate = jTableWk2Activities.getValueAt(i, 0).toString();
                    UsrRecWk = "2";
                    usrActType = "Per Diem";
                    WkUsrRecUpd();
                }
            }
            for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
                if (!("".equals(jTableWk2Activities.getValueAt(i, 18).toString()))) {
                    usrRecNam = jTableWk2Activities.getValueAt(i, 18).toString();
                    actDate = jTableWk2Activities.getValueAt(i, 0).toString();
                    UsrRecWk = "2";
                    usrActType = "Per Diem";
                    WkUsrRecUpd();
                }
            }
            for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
                if (!("".equals(jTableWk2Activities.getValueAt(i, 19).toString()))) {
                    usrRecNam = jTableWk2Activities.getValueAt(i, 19).toString();
                    actDate = jTableWk2Activities.getValueAt(i, 0).toString();
                    UsrRecWk = "2";
                    usrActType = "Per Diem";
                    WkUsrRecUpd();
                }
            }

            for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
                if (!("".equals(jTableWk3Activities.getValueAt(i, 16).toString()))) {
                    usrRecNam = jTableWk3Activities.getValueAt(i, 16).toString();
                    actDate = jTableWk3Activities.getValueAt(i, 0).toString();
                    UsrRecWk = "3";
                    usrActType = "Per Diem";
                    WkUsrRecUpd();
                }
            }

            for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
                if (!("".equals(jTableWk3Activities.getValueAt(i, 17).toString()))) {
                    usrRecNam = jTableWk3Activities.getValueAt(i, 17).toString();
                    actDate = jTableWk3Activities.getValueAt(i, 0).toString();
                    UsrRecWk = "3";
                    usrActType = "Per Diem";
                    WkUsrRecUpd();
                }
            }
            for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
                if (!("".equals(jTableWk3Activities.getValueAt(i, 18).toString()))) {
                    usrRecNam = jTableWk3Activities.getValueAt(i, 18).toString();
                    actDate = jTableWk3Activities.getValueAt(i, 0).toString();
                    UsrRecWk = "3";
                    usrActType = "Per Diem";
                    WkUsrRecUpd();
                }
            }
            for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
                if (!("".equals(jTableWk3Activities.getValueAt(i, 19).toString()))) {
                    usrRecNam = jTableWk3Activities.getValueAt(i, 19).toString();
                    actDate = jTableWk3Activities.getValueAt(i, 0).toString();
                    UsrRecWk = "3";
                    usrActType = "Per Diem";
                    WkUsrRecUpd();
                }
            }

            for (int i = 0; i < jTableWk4Activities.getRowCount(); i++) {
                if (!("".equals(jTableWk4Activities.getValueAt(i, 16).toString()))) {
                    usrRecNam = jTableWk4Activities.getValueAt(i, 16).toString();
                    actDate = jTableWk4Activities.getValueAt(i, 0).toString();
                    UsrRecWk = "4";
                    usrActType = "Per Diem";
                    WkUsrRecUpd();
                }
            }

            for (int i = 0; i < jTableWk4Activities.getRowCount(); i++) {
                if (!("".equals(jTableWk4Activities.getValueAt(i, 17).toString()))) {
                    usrRecNam = jTableWk4Activities.getValueAt(i, 17).toString();
                    actDate = jTableWk4Activities.getValueAt(i, 0).toString();
                    UsrRecWk = "4";
                    usrActType = "Per Diem";
                    WkUsrRecUpd();
                }
            }
            for (int i = 0; i < jTableWk4Activities.getRowCount(); i++) {
                if (!("".equals(jTableWk4Activities.getValueAt(i, 18).toString()))) {
                    usrRecNam = jTableWk4Activities.getValueAt(i, 18).toString();
                    actDate = jTableWk4Activities.getValueAt(i, 0).toString();
                    UsrRecWk = "4";
                    usrActType = "Per Diem";
                    WkUsrRecUpd();
                }
            }
            for (int i = 0; i < jTableWk4Activities.getRowCount(); i++) {
                if (!("".equals(jTableWk4Activities.getValueAt(i, 19).toString()))) {
                    usrRecNam = jTableWk4Activities.getValueAt(i, 19).toString();
                    actDate = jTableWk4Activities.getValueAt(i, 0).toString();
                    UsrRecWk = "4";
                    usrActType = "Per Diem";
                    WkUsrRecUpd();
                }
            }

            for (int i = 0; i < jTableWk5Activities.getRowCount(); i++) {
                if (!("".equals(jTableWk5Activities.getValueAt(i, 16).toString()))) {
                    usrRecNam = jTableWk5Activities.getValueAt(i, 16).toString();
                    actDate = jTableWk5Activities.getValueAt(i, 0).toString();
                    UsrRecWk = "5";
                    usrActType = "Per Diem";
                    WkUsrRecUpd();
                }
            }

            for (int i = 0; i < jTableWk5Activities.getRowCount(); i++) {
                if (!("".equals(jTableWk5Activities.getValueAt(i, 17).toString()))) {
                    usrRecNam = jTableWk5Activities.getValueAt(i, 17).toString();
                    actDate = jTableWk5Activities.getValueAt(i, 0).toString();
                    UsrRecWk = "5";
                    usrActType = "Per Diem";
                    WkUsrRecUpd();
                }
            }
            for (int i = 0; i < jTableWk5Activities.getRowCount(); i++) {
                if (!("".equals(jTableWk5Activities.getValueAt(i, 18).toString()))) {
                    usrRecNam = jTableWk5Activities.getValueAt(i, 18).toString();
                    actDate = jTableWk5Activities.getValueAt(i, 0).toString();
                    UsrRecWk = "5";
                    usrActType = "Per Diem";
                    WkUsrRecUpd();
                }
            }
            for (int i = 0; i < jTableWk5Activities.getRowCount(); i++) {
                if (!("".equals(jTableWk5Activities.getValueAt(i, 19).toString()))) {
                    usrRecNam = jTableWk5Activities.getValueAt(i, 19).toString();
                    actDate = jTableWk5Activities.getValueAt(i, 0).toString();
                    UsrRecWk = "5";
                    usrActType = "Per Diem";
                    WkUsrRecUpd();
                }
            }

            for (int i = 0; i < jTableMeetReqWk1.getRowCount(); i++) {
                if (!("".equals(jTableMeetReqWk1.getValueAt(i, 11).toString()))) {
                    usrRecNam = jTableMeetReqWk1.getValueAt(i, 11).toString();
                    actDate = jTableMeetReqWk1.getValueAt(i, 1).toString();
                    UsrRecWk = "1";
                    usrActType = "Meeting";
                    WkUsrRecUpd();
                }
            }

            for (int i = 0; i < jTableMeetReqWk2.getRowCount(); i++) {
                if (!("".equals(jTableMeetReqWk2.getValueAt(i, 11).toString()))) {
                    usrRecNam = jTableMeetReqWk2.getValueAt(i, 11).toString();
                    actDate = jTableMeetReqWk2.getValueAt(i, 1).toString();
                    UsrRecWk = "2";
                    usrActType = "Meeting";
                    WkUsrRecUpd();
                }
            }

            for (int i = 0; i < jTableMeetReqWk3.getRowCount(); i++) {
                if (!("".equals(jTableMeetReqWk3.getValueAt(i, 11).toString()))) {
                    usrRecNam = jTableMeetReqWk3.getValueAt(i, 11).toString();
                    actDate = jTableMeetReqWk3.getValueAt(i, 1).toString();
                    UsrRecWk = "3";
                    usrActType = "Meeting";
                    WkUsrRecUpd();
                }
            }

            for (int i = 0; i < jTableMeetReqWk4.getRowCount(); i++) {
                if (!("".equals(jTableMeetReqWk4.getValueAt(i, 11).toString()))) {
                    usrRecNam = jTableMeetReqWk4.getValueAt(i, 11).toString();
                    actDate = jTableMeetReqWk4.getValueAt(i, 1).toString();
                    UsrRecWk = "4";
                    usrActType = "Meeting";
                    WkUsrRecUpd();
                }
            }

            for (int i = 0; i < jTableMeetReqWk5.getRowCount(); i++) {
                if (!("".equals(jTableMeetReqWk5.getValueAt(i, 11).toString()))) {
                    usrRecNam = jTableMeetReqWk5.getValueAt(i, 11).toString();
                    actDate = jTableMeetReqWk5.getValueAt(i, 1).toString();
                    UsrRecWk = "5";
                    usrActType = "Meeting";
                    WkUsrRecUpd();
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void computeMeetDaysCheckListen() {

        jDateFromWk1.getDateEditor().addPropertyChangeListener(
                new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ((!(jDateFromWk1.getDate() == null))
                        && (formatter.format(jDateFromWk1.getDate()).compareTo(formatter.format(jDateChooserWk1From.getDate())) < 0)) {
                    JOptionPane.showMessageDialog(jDialogMeetingWk1, "Meeting Activity date cannot be outside the specified week date range.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    jDateFromWk1.setDate(null);
                    jDateFromWk1.requestFocusInWindow();
                    jDateFromWk1.setFocusable(true);

                }
            }
        });

        jDateToWk1.getDateEditor().addPropertyChangeListener(
                new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ((!(jDateToWk1.getDate() == null))
                        && (formatter.format(jDateToWk1.getDate()).compareTo(formatter.format(jDateChooserWk1To.getDate())) > 0)) {
                    JOptionPane.showMessageDialog(jDialogMeetingWk1, "Meeting Activity date cannot be outside the specified week date range.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    jDateToWk1.setDate(null);
                    jDateToWk1.requestFocusInWindow();
                    jDateToWk1.setFocusable(true);
                }
            }
        });

        jDateFromWk2.getDateEditor().addPropertyChangeListener(
                new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ((!(jDateFromWk2.getDate() == null))
                        && (formatter.format(jDateFromWk2.getDate()).compareTo(formatter.format(jDateChooserWk2From.getDate())) < 0)) {
                    JOptionPane.showMessageDialog(jDialogMeetingWk2, "Meeting Activity date cannot be outside the specified week date range.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    jDateFromWk2.setDate(null);
                    jDateFromWk2.requestFocusInWindow();
                    jDateFromWk2.setFocusable(true);

                }
            }
        });

        jDateToWk2.getDateEditor().addPropertyChangeListener(
                new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ((!(jDateToWk2.getDate() == null))
                        && (formatter.format(jDateToWk2.getDate()).compareTo(formatter.format(jDateChooserWk2To.getDate())) > 0)) {
                    JOptionPane.showMessageDialog(jDialogMeetingWk2, "Meeting Activity date cannot be outside the specified week date range.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    jDateToWk2.setDate(null);
                    jDateToWk2.requestFocusInWindow();
                    jDateToWk2.setFocusable(true);
                }
            }
        });

        jDateFromWk3.getDateEditor().addPropertyChangeListener(
                new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ((!(jDateFromWk3.getDate() == null))
                        && (formatter.format(jDateFromWk3.getDate()).compareTo(formatter.format(jDateChooserWk3From.getDate())) < 0)) {
                    JOptionPane.showMessageDialog(jDialogMeetingWk3, "Meeting Activity date cannot be outside the specified week date range.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    jDateFromWk3.setDate(null);
                    jDateFromWk3.requestFocusInWindow();
                    jDateFromWk3.setFocusable(true);

                }
            }
        });

        jDateToWk3.getDateEditor().addPropertyChangeListener(
                new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ((!(jDateToWk3.getDate() == null))
                        && (formatter.format(jDateToWk3.getDate()).compareTo(formatter.format(jDateChooserWk3To.getDate())) > 0)) {
                    JOptionPane.showMessageDialog(jDialogMeetingWk3, "Meeting Activity date cannot be outside the specified week date range.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    jDateToWk3.setDate(null);
                    jDateToWk3.requestFocusInWindow();
                    jDateToWk3.setFocusable(true);
                }
            }
        });

        jDateFromWk4.getDateEditor().addPropertyChangeListener(
                new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ((!(jDateFromWk4.getDate() == null))
                        && (formatter.format(jDateFromWk4.getDate()).compareTo(formatter.format(jDateChooserWk4From.getDate())) < 0)) {
                    JOptionPane.showMessageDialog(jDialogMeetingWk4, "Meeting Activity date cannot be outside the specified week date range.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    jDateFromWk4.setDate(null);
                    jDateFromWk4.requestFocusInWindow();
                    jDateFromWk4.setFocusable(true);

                }
            }
        });

        jDateToWk4.getDateEditor().addPropertyChangeListener(
                new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ((!(jDateToWk4.getDate() == null))
                        && (formatter.format(jDateToWk4.getDate()).compareTo(formatter.format(jDateChooserWk4To.getDate())) > 0)) {
                    JOptionPane.showMessageDialog(jDialogMeetingWk4, "Meeting Activity date cannot be outside the specified week date range.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    jDateToWk4.setDate(null);
                    jDateToWk4.requestFocusInWindow();
                    jDateToWk4.setFocusable(true);
                }
            }
        });

        jDateFromWk5.getDateEditor().addPropertyChangeListener(
                new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ((!(jDateFromWk5.getDate() == null))
                        && (formatter.format(jDateFromWk5.getDate()).compareTo(formatter.format(jDateChooserWk5From.getDate())) < 0)) {
                    JOptionPane.showMessageDialog(jDialogMeetingWk5, "Meeting Activity date cannot be outside the specified week date range.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    jDateFromWk5.setDate(null);
                    jDateFromWk5.requestFocusInWindow();
                    jDateFromWk5.setFocusable(true);

                }
            }
        });

        jDateToWk5.getDateEditor().addPropertyChangeListener(
                new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ((!(jDateToWk5.getDate() == null))
                        && (formatter.format(jDateToWk5.getDate()).compareTo(formatter.format(jDateChooserWk5To.getDate())) > 0)) {
                    JOptionPane.showMessageDialog(jDialogMeetingWk5, "Meeting Activity date cannot be outside the specified week date range.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    jDateToWk5.setDate(null);
                    jDateToWk5.requestFocusInWindow();
                    jDateToWk5.setFocusable(true);
                }
            }
        });

        jComboMeetAllowanceWk1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ("Busfare".equals(jComboMeetAllowanceWk1.getSelectedItem().toString())) {
                    jLabelBusFareWk1.setVisible(true);
                    jTextBusFareWk1.setVisible(true);
                    jTextUnitCostWk1.setText("1");
                    jTextUnitCostWk1.setEnabled(false);
                    jLabelNumDaysWk1.setVisible(false);
                    jTextNumDaysWk1.setVisible(false);
                } else {
                    jLabelBusFareWk1.setVisible(false);
                    jTextBusFareWk1.setVisible(false);
                    jLabelNumDaysWk1.setVisible(true);
                    jTextNumDaysWk1.setVisible(true);
                    jTextUnitCostWk1.setText("");
                    jTextUnitCostWk1.setEnabled(true);
                }
            }
        });

        jComboMeetAllowanceWk2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ("Busfare".equals(jComboMeetAllowanceWk2.getSelectedItem().toString())) {
                    jLabelBusFareWk2.setVisible(true);
                    jTextBusFareWk2.setVisible(true);
                    jTextUnitCostWk2.setText("1");
                    jTextUnitCostWk2.setEnabled(false);
                    jLabelNumDaysWk2.setVisible(false);
                    jTextNumDaysWk2.setVisible(false);
                } else {
                    jLabelBusFareWk2.setVisible(false);
                    jTextBusFareWk2.setVisible(false);
                    jLabelNumDaysWk2.setVisible(true);
                    jTextNumDaysWk2.setVisible(true);
                    jTextUnitCostWk2.setText("");
                    jTextUnitCostWk2.setEnabled(true);
                }
            }
        });

        jComboMeetAllowanceWk3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ("Busfare".equals(jComboMeetAllowanceWk3.getSelectedItem().toString())) {
                    jLabelBusFareWk3.setVisible(true);
                    jTextBusFareWk3.setVisible(true);
                    jTextUnitCostWk3.setText("1");
                    jTextUnitCostWk3.setEnabled(false);
                    jLabelNumDaysWk3.setVisible(false);
                    jTextNumDaysWk3.setVisible(false);
                } else {
                    jLabelBusFareWk3.setVisible(false);
                    jTextBusFareWk3.setVisible(false);
                    jLabelNumDaysWk3.setVisible(true);
                    jTextNumDaysWk3.setVisible(true);
                    jTextUnitCostWk3.setText("");
                    jTextUnitCostWk3.setEnabled(true);
                }
            }
        });

        jComboMeetAllowanceWk4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ("Busfare".equals(jComboMeetAllowanceWk4.getSelectedItem().toString())) {
                    jLabelBusFareWk4.setVisible(true);
                    jTextBusFareWk4.setVisible(true);
                    jTextUnitCostWk4.setText("1");
                    jTextUnitCostWk4.setEnabled(false);
                    jLabelNumDaysWk4.setVisible(false);
                    jTextNumDaysWk4.setVisible(false);
                } else {
                    jLabelBusFareWk4.setVisible(false);
                    jTextBusFareWk4.setVisible(false);
                    jLabelNumDaysWk4.setVisible(true);
                    jTextNumDaysWk4.setVisible(true);
                    jTextUnitCostWk4.setText("");
                    jTextUnitCostWk4.setEnabled(true);
                }
            }
        });

        jComboMeetAllowanceWk5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ("Busfare".equals(jComboMeetAllowanceWk5.getSelectedItem().toString())) {
                    jLabelBusFareWk5.setVisible(true);
                    jTextBusFareWk5.setVisible(true);
                    jTextUnitCostWk5.setText("1");
                    jTextUnitCostWk5.setEnabled(false);
                    jLabelNumDaysWk5.setVisible(false);
                    jTextNumDaysWk5.setVisible(false);
                } else {
                    jLabelBusFareWk5.setVisible(false);
                    jTextBusFareWk5.setVisible(false);
                    jLabelNumDaysWk5.setVisible(true);
                    jTextNumDaysWk5.setVisible(true);
                    jTextUnitCostWk5.setText("");
                    jTextUnitCostWk5.setEnabled(true);
                }
            }
        });

        jDateToWk1.getDateEditor().addPropertyChangeListener(
                new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ((jDateFromWk1.getDate() != null) && (!(jDateFromWk1.getDate().after(jDateToWk1.getDate())))) {
                    computeMeetDaysWk1();
                    calcBudTotWk1();
                } else {
                    jTextNumDaysWk1.setText("");
                }
            }
        });

        jDateToWk2.getDateEditor().addPropertyChangeListener(
                new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ((jDateFromWk2.getDate() != null) && (!(jDateFromWk2.getDate().after(jDateToWk2.getDate())))) {
                    computeMeetDaysWk2();
                    calcBudTotWk2();
                } else {
                    jTextNumDaysWk2.setText("");
                }
            }
        });

        jDateToWk3.getDateEditor().addPropertyChangeListener(
                new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ((jDateFromWk3.getDate() != null) && (!(jDateFromWk3.getDate().after(jDateToWk3.getDate())))) {
                    computeMeetDaysWk3();
                    calcBudTotWk3();
                } else {
                    jTextNumDaysWk3.setText("");
                }
            }
        });

        jDateToWk4.getDateEditor().addPropertyChangeListener(
                new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ((jDateFromWk4.getDate() != null) && (!(jDateFromWk4.getDate().after(jDateToWk4.getDate())))) {
                    computeMeetDaysWk4();
                    calcBudTotWk4();
                } else {
                    jTextNumDaysWk4.setText("");
                }
            }
        });

        jDateToWk5.getDateEditor().addPropertyChangeListener(
                new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ((jDateFromWk5.getDate() != null) && (!(jDateFromWk5.getDate().after(jDateToWk5.getDate())))) {
                    computeMeetDaysWk5();
                    calcBudTotWk5();
                } else {
                    jTextNumDaysWk5.setText("");
                }
            }
        });
    }

    void computeMeetDaysWk1() {
        DateFormat dfYear = new SimpleDateFormat("yyyy");
        DateFormat dfMon = new SimpleDateFormat("MM");
        DateFormat dfDay = new SimpleDateFormat("dd");

        int yearF = Integer.parseInt(dfYear.format(jDateFromWk1.getDate()));
        int monF = Integer.parseInt(dfMon.format(jDateFromWk1.getDate()));
        int dayF = Integer.parseInt(dfDay.format(jDateFromWk1.getDate()));

        int yearT = Integer.parseInt(dfYear.format(jDateToWk1.getDate()));
        int monT = Integer.parseInt(dfMon.format(jDateToWk1.getDate()));
        int dayT = Integer.parseInt(dfDay.format(jDateToWk1.getDate()));
        LocalDate pdate = LocalDate.of(yearF, monF, dayF);
        LocalDate now = LocalDate.of(yearT, monT, dayT);

        Period diff = Period.between(pdate, now);

        //  jLabelCalMonths.setText(Integer.toString(diff.getMonths()));
        jTextNumDaysWk1.setText(Integer.toString(diff.getDays() + 1));
        jTextNumDaysWk1.setEnabled(false);

    }

    void calcBudTotWk1() {
        totReqAmt = 0;
        if ((jTextUnitCostWk1.getText().trim().length() > 0) && (jTextNumPpleWk1.getText().trim().length() > 0)
                && (jTextNumDaysWk1.getText().trim().length() > 0)) {
            if ("Busfare".equals(jComboMeetAllowanceWk1.getSelectedItem().toString())) {
                totBudgetLnReqAmt = Double.parseDouble(jTextBusFareWk1.getText());
            } else {
                totBudgetLnReqAmt = Double.parseDouble(jTextUnitCostWk1.getText()) * Double.parseDouble(jTextNumPpleWk1.getText()) * Double.parseDouble(jTextNumDaysWk1.getText());
            }

            totReqAmt = totBudgetLnReqAmt;
            jLabelTotalAmtWk1.setText(String.format("%.2f", totReqAmt));
        }
    }

    void computeMeetDaysWk2() {
        DateFormat dfYear = new SimpleDateFormat("yyyy");
        DateFormat dfMon = new SimpleDateFormat("MM");
        DateFormat dfDay = new SimpleDateFormat("dd");

        int yearF = Integer.parseInt(dfYear.format(jDateFromWk2.getDate()));
        int monF = Integer.parseInt(dfMon.format(jDateFromWk2.getDate()));
        int dayF = Integer.parseInt(dfDay.format(jDateFromWk2.getDate()));

        int yearT = Integer.parseInt(dfYear.format(jDateToWk2.getDate()));
        int monT = Integer.parseInt(dfMon.format(jDateToWk2.getDate()));
        int dayT = Integer.parseInt(dfDay.format(jDateToWk2.getDate()));
        LocalDate pdate = LocalDate.of(yearF, monF, dayF);
        LocalDate now = LocalDate.of(yearT, monT, dayT);

        Period diff = Period.between(pdate, now);

        //  jLabelCalMonths.setText(Integer.toString(diff.getMonths()));
        jTextNumDaysWk2.setText(Integer.toString(diff.getDays() + 1));
        jTextNumDaysWk2.setEnabled(false);

    }

    void computeMeetDaysWk3() {
        DateFormat dfYear = new SimpleDateFormat("yyyy");
        DateFormat dfMon = new SimpleDateFormat("MM");
        DateFormat dfDay = new SimpleDateFormat("dd");

        int yearF = Integer.parseInt(dfYear.format(jDateFromWk3.getDate()));
        int monF = Integer.parseInt(dfMon.format(jDateFromWk3.getDate()));
        int dayF = Integer.parseInt(dfDay.format(jDateFromWk3.getDate()));

        int yearT = Integer.parseInt(dfYear.format(jDateToWk3.getDate()));
        int monT = Integer.parseInt(dfMon.format(jDateToWk3.getDate()));
        int dayT = Integer.parseInt(dfDay.format(jDateToWk3.getDate()));
        LocalDate pdate = LocalDate.of(yearF, monF, dayF);
        LocalDate now = LocalDate.of(yearT, monT, dayT);

        Period diff = Period.between(pdate, now);

        //  jLabelCalMonths.setText(Integer.toString(diff.getMonths()));
        jTextNumDaysWk3.setText(Integer.toString(diff.getDays() + 1));
        jTextNumDaysWk3.setEnabled(false);

    }

    void computeMeetDaysWk4() {
        DateFormat dfYear = new SimpleDateFormat("yyyy");
        DateFormat dfMon = new SimpleDateFormat("MM");
        DateFormat dfDay = new SimpleDateFormat("dd");

        int yearF = Integer.parseInt(dfYear.format(jDateFromWk4.getDate()));
        int monF = Integer.parseInt(dfMon.format(jDateFromWk4.getDate()));
        int dayF = Integer.parseInt(dfDay.format(jDateFromWk4.getDate()));

        int yearT = Integer.parseInt(dfYear.format(jDateToWk4.getDate()));
        int monT = Integer.parseInt(dfMon.format(jDateToWk4.getDate()));
        int dayT = Integer.parseInt(dfDay.format(jDateToWk4.getDate()));
        LocalDate pdate = LocalDate.of(yearF, monF, dayF);
        LocalDate now = LocalDate.of(yearT, monT, dayT);

        Period diff = Period.between(pdate, now);

        //  jLabelCalMonths.setText(Integer.toString(diff.getMonths()));
        jTextNumDaysWk4.setText(Integer.toString(diff.getDays() + 1));
        jTextNumDaysWk4.setEnabled(false);

    }

    void computeMeetDaysWk5() {
        DateFormat dfYear = new SimpleDateFormat("yyyy");
        DateFormat dfMon = new SimpleDateFormat("MM");
        DateFormat dfDay = new SimpleDateFormat("dd");

        int yearF = Integer.parseInt(dfYear.format(jDateFromWk5.getDate()));
        int monF = Integer.parseInt(dfMon.format(jDateFromWk5.getDate()));
        int dayF = Integer.parseInt(dfDay.format(jDateFromWk5.getDate()));

        int yearT = Integer.parseInt(dfYear.format(jDateToWk5.getDate()));
        int monT = Integer.parseInt(dfMon.format(jDateToWk5.getDate()));
        int dayT = Integer.parseInt(dfDay.format(jDateToWk5.getDate()));
        LocalDate pdate = LocalDate.of(yearF, monF, dayF);
        LocalDate now = LocalDate.of(yearT, monT, dayT);

        Period diff = Period.between(pdate, now);

        //  jLabelCalMonths.setText(Integer.toString(diff.getMonths()));
        jTextNumDaysWk5.setText(Integer.toString(diff.getDays() + 1));
        jTextNumDaysWk5.setEnabled(false);

    }

    void calcBudTotWk2() {
        totReqAmt = 0;
        if ((jTextUnitCostWk2.getText().trim().length() > 0) && (jTextNumPpleWk2.getText().trim().length() > 0)
                && (jTextNumDaysWk2.getText().trim().length() > 0)) {
            if ("Busfare".equals(jComboMeetAllowanceWk2.getSelectedItem().toString())) {
                totBudgetLnReqAmt = Double.parseDouble(jTextBusFareWk2.getText());
            } else {
                totBudgetLnReqAmt = Double.parseDouble(jTextUnitCostWk2.getText()) * Double.parseDouble(jTextNumPpleWk2.getText()) * Double.parseDouble(jTextNumDaysWk2.getText());
            }

            totReqAmt = totBudgetLnReqAmt;
            jLabelTotalAmtWk2.setText(String.format("%.2f", totReqAmt));
        }
    }

    void calcBudTotWk3() {
        totReqAmt = 0;
        if ((jTextUnitCostWk3.getText().trim().length() > 0) && (jTextNumPpleWk3.getText().trim().length() > 0)
                && (jTextNumDaysWk3.getText().trim().length() > 0)) {
            if ("Busfare".equals(jComboMeetAllowanceWk3.getSelectedItem().toString())) {
                totBudgetLnReqAmt = Double.parseDouble(jTextBusFareWk3.getText());
            } else {
                totBudgetLnReqAmt = Double.parseDouble(jTextUnitCostWk3.getText()) * Double.parseDouble(jTextNumPpleWk3.getText()) * Double.parseDouble(jTextNumDaysWk3.getText());
            }

            totReqAmt = totBudgetLnReqAmt;
            jLabelTotalAmtWk3.setText(String.format("%.2f", totReqAmt));
        }
    }

    void calcBudTotWk4() {
        totReqAmt = 0;
        if ((jTextUnitCostWk4.getText().trim().length() > 0) && (jTextNumPpleWk4.getText().trim().length() > 0)
                && (jTextNumDaysWk4.getText().trim().length() > 0)) {
            if ("Busfare".equals(jComboMeetAllowanceWk4.getSelectedItem().toString())) {
                totBudgetLnReqAmt = Double.parseDouble(jTextBusFareWk4.getText());
            } else {
                totBudgetLnReqAmt = Double.parseDouble(jTextUnitCostWk4.getText()) * Double.parseDouble(jTextNumPpleWk4.getText()) * Double.parseDouble(jTextNumDaysWk4.getText());
            }

            totReqAmt = totBudgetLnReqAmt;
            jLabelTotalAmtWk4.setText(String.format("%.2f", totReqAmt));
        }
    }

    void calcBudTotWk5() {
        totReqAmt = 0;
        if ((jTextUnitCostWk5.getText().trim().length() > 0) && (jTextNumPpleWk5.getText().trim().length() > 0)
                && (jTextNumDaysWk5.getText().trim().length() > 0)) {
            if ("Busfare".equals(jComboMeetAllowanceWk5.getSelectedItem().toString())) {
                totBudgetLnReqAmt = Double.parseDouble(jTextBusFareWk5.getText());
            } else {
                totBudgetLnReqAmt = Double.parseDouble(jTextUnitCostWk5.getText()) * Double.parseDouble(jTextNumPpleWk5.getText()) * Double.parseDouble(jTextNumDaysWk5.getText());
            }

            totReqAmt = totBudgetLnReqAmt;
            jLabelTotalAmtWk5.setText(String.format("%.2f", totReqAmt));
        }
    }

    void Wk1MeetAddItm() {
        DefaultTableModel model = (DefaultTableModel) jTableMeetReqWk1.getModel();
        if (jDateFromWk1.getDate() == null) {
            JOptionPane.showMessageDialog(jDialogMeetingWk1, "Date cannot be blank. Please check your dates");
            jDateFromWk1.requestFocusInWindow();
            jDateFromWk1.setFocusable(true);
        } else if (jDateToWk1.getDate() == null) {
            JOptionPane.showMessageDialog(jDialogMeetingWk1, "Date cannot be blank. Please check your dates");
            jDateToWk1.requestFocusInWindow();
            jDateToWk1.setFocusable(true);
        } else if (jDateFromWk1.getDate().after(jDateToWk1.getDate())) {
            JOptionPane.showMessageDialog(jDialogMeetingWk1, "End of cctivity date cannot be lower than start date. Please check your dates");
            jDateFromWk1.requestFocusInWindow();
            jDateFromWk1.setFocusable(true);
        } else if (jTextFieldBudgetWk1.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk1, "Please check budget code field");
            jTextFieldBudgetWk1.requestFocusInWindow();
            jTextFieldBudgetWk1.setFocusable(true);
        } else if (jTextAreaDetailDescriptionWk1.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk1, "Please check activity detailed description field");
            jTextAreaDetailDescriptionWk1.requestFocusInWindow();
            jTextAreaDetailDescriptionWk1.setFocusable(true);
        } else if (jTextPersonResWk1.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk1, "Please check responsible person field");
            jTextPersonResWk1.requestFocusInWindow();
            jTextPersonResWk1.setFocusable(true);
        } else if (jTextUnitCostWk1.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk1, "Please check unit cost field");
            jTextUnitCostWk1.requestFocusInWindow();
            jTextUnitCostWk1.setFocusable(true);
        } else if (jTextNumPpleWk1.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk1, "Please check number of people field");
            jTextNumPpleWk1.requestFocusInWindow();
            jTextNumPpleWk1.setFocusable(true);
        } else if (jTextNumDaysWk1.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk1, "Please check number of days field");
            jTextNumDaysWk1.requestFocusInWindow();
            jTextNumDaysWk1.setFocusable(true);
        } else if (("Busfare".equals(jComboMeetAllowanceWk1.getSelectedItem().toString())) && (jTextBusFareWk1.getText().trim().length() == 0)) {
            JOptionPane.showMessageDialog(jDialogMeetingWk1, "Please check number of days field");
            jTextBusFareWk1.requestFocusInWindow();
            jTextBusFareWk1.setFocusable(true);
        } else {

            calcBudTotWk1();
            model.addRow(new Object[]{jComboActivityProjectWk1.getSelectedItem().toString(), formatter.format(jDateFromWk1.getDate()),
                formatter.format(jDateToWk1.getDate()), jTextFieldBudgetWk1.getText(), jComboMeetingWk1.getSelectedItem().toString(),
                jTextAreaDetailDescriptionWk1.getText(), jComboMeetAllowanceWk1.getSelectedItem().toString(), jTextUnitCostWk1.getText(),
                jTextNumPpleWk1.getText(), jTextNumDaysWk1.getText(), String.format("%.2f", totBudgetLnReqAmt), jTextPersonResWk1.getText()});

            resetFieldsMeet();
            addMeetItemWk1();

        }
    }

    void Wk2MeetAddItm() {
        DefaultTableModel model = (DefaultTableModel) jTableMeetReqWk2.getModel();
        if (jDateFromWk2.getDate() == null) {
            JOptionPane.showMessageDialog(jDialogMeetingWk2, "Date cannot be blank. Please check your dates");
            jDateFromWk2.requestFocusInWindow();
            jDateFromWk2.setFocusable(true);
        } else if (jDateToWk2.getDate() == null) {
            JOptionPane.showMessageDialog(jDialogMeetingWk2, "Date cannot be blank. Please check your dates");
            jDateToWk2.requestFocusInWindow();
            jDateToWk2.setFocusable(true);
        } else if (jDateFromWk2.getDate().after(jDateToWk2.getDate())) {
            JOptionPane.showMessageDialog(jDialogMeetingWk2, "End of cctivity date cannot be lower than start date. Please check your dates");
            jDateFromWk2.requestFocusInWindow();
            jDateFromWk2.setFocusable(true);
        } else if (jTextFieldBudgetWk2.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk2, "Please check budget code field");
            jTextFieldBudgetWk2.requestFocusInWindow();
            jTextFieldBudgetWk2.setFocusable(true);
        } else if (jTextAreaDetailDescriptionWk2.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk2, "Please check activity detailed description field");
            jTextAreaDetailDescriptionWk2.requestFocusInWindow();
            jTextAreaDetailDescriptionWk2.setFocusable(true);
        } else if (jTextPersonResWk2.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk2, "Please check responsible person field");
            jTextPersonResWk2.requestFocusInWindow();
            jTextPersonResWk2.setFocusable(true);
        } else if (jTextUnitCostWk2.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk2, "Please check unit cost field");
            jTextUnitCostWk2.requestFocusInWindow();
            jTextUnitCostWk2.setFocusable(true);
        } else if (jTextNumPpleWk2.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk2, "Please check number of people field");
            jTextNumPpleWk2.requestFocusInWindow();
            jTextNumPpleWk2.setFocusable(true);
        } else if (jTextNumDaysWk2.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk2, "Please check number of days field");
            jTextNumDaysWk2.requestFocusInWindow();
            jTextNumDaysWk2.setFocusable(true);
        } else if (("Busfare".equals(jComboMeetAllowanceWk2.getSelectedItem().toString())) && (jTextBusFareWk2.getText().trim().length() == 0)) {
            JOptionPane.showMessageDialog(jDialogMeetingWk2, "Please check number of days field");
            jTextBusFareWk2.requestFocusInWindow();
            jTextBusFareWk2.setFocusable(true);
        } else {
            calcBudTotWk2();
            model.addRow(new Object[]{jComboActivityProjectWk2.getSelectedItem().toString(), formatter.format(jDateFromWk2.getDate()),
                formatter.format(jDateToWk2.getDate()), jTextFieldBudgetWk2.getText(), jComboMeetingWk2.getSelectedItem().toString(),
                jTextAreaDetailDescriptionWk2.getText(), jComboMeetAllowanceWk2.getSelectedItem().toString(), jTextUnitCostWk2.getText(),
                jTextNumPpleWk2.getText(), jTextNumDaysWk2.getText(), String.format("%.2f", totBudgetLnReqAmt), jTextPersonResWk2.getText()});
            resetFieldsMeet();
            addMeetItemWk2();
        }
    }

    void Wk3MeetAddItm() {
        DefaultTableModel model = (DefaultTableModel) jTableMeetReqWk3.getModel();
        if (jDateFromWk3.getDate() == null) {
            JOptionPane.showMessageDialog(jDialogMeetingWk3, "Date cannot be blank. Please check your dates");
            jDateFromWk3.requestFocusInWindow();
            jDateFromWk3.setFocusable(true);
        } else if (jDateToWk3.getDate() == null) {
            JOptionPane.showMessageDialog(jDialogMeetingWk3, "Date cannot be blank. Please check your dates");
            jDateToWk3.requestFocusInWindow();
            jDateToWk3.setFocusable(true);
        } else if (jDateFromWk3.getDate().after(jDateToWk3.getDate())) {
            JOptionPane.showMessageDialog(jDialogMeetingWk3, "End of cctivity date cannot be lower than start date. Please check your dates");
            jDateFromWk3.requestFocusInWindow();
            jDateFromWk3.setFocusable(true);
        } else if (jTextFieldBudgetWk3.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk3, "Please check budget code field");
            jTextFieldBudgetWk3.requestFocusInWindow();
            jTextFieldBudgetWk3.setFocusable(true);
        } else if (jTextAreaDetailDescriptionWk3.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk3, "Please check activity detailed description field");
            jTextAreaDetailDescriptionWk3.requestFocusInWindow();
            jTextAreaDetailDescriptionWk3.setFocusable(true);
        } else if (jTextPersonResWk3.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk3, "Please check responsible person field");
            jTextPersonResWk3.requestFocusInWindow();
            jTextPersonResWk3.setFocusable(true);
        } else if (jTextUnitCostWk3.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk3, "Please check unit cost field");
            jTextUnitCostWk3.requestFocusInWindow();
            jTextUnitCostWk3.setFocusable(true);
        } else if (jTextNumPpleWk3.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk3, "Please check number of people field");
            jTextNumPpleWk3.requestFocusInWindow();
            jTextNumPpleWk3.setFocusable(true);
        } else if (jTextNumDaysWk3.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk3, "Please check number of days field");
            jTextNumDaysWk3.requestFocusInWindow();
            jTextNumDaysWk3.setFocusable(true);
        } else if (("Busfare".equals(jComboMeetAllowanceWk3.getSelectedItem().toString())) && (jTextBusFareWk3.getText().trim().length() == 0)) {
            JOptionPane.showMessageDialog(jDialogMeetingWk3, "Please check number of days field");
            jTextBusFareWk3.requestFocusInWindow();
            jTextBusFareWk3.setFocusable(true);
        } else {
            calcBudTotWk3();
            model.addRow(new Object[]{jComboActivityProjectWk3.getSelectedItem().toString(), formatter.format(jDateFromWk3.getDate()),
                formatter.format(jDateToWk3.getDate()), jTextFieldBudgetWk3.getText(), jComboMeetingWk3.getSelectedItem().toString(),
                jTextAreaDetailDescriptionWk3.getText(), jComboMeetAllowanceWk3.getSelectedItem().toString(), jTextUnitCostWk3.getText(),
                jTextNumPpleWk3.getText(), jTextNumDaysWk3.getText(), String.format("%.2f", totBudgetLnReqAmt), jTextPersonResWk3.getText()});
            resetFieldsMeet();
            addMeetItemWk3();
        }
    }

    void Wk4MeetAddItm() {
        DefaultTableModel model = (DefaultTableModel) jTableMeetReqWk4.getModel();
        if (jDateFromWk4.getDate() == null) {
            JOptionPane.showMessageDialog(jDialogMeetingWk4, "Date cannot be blank. Please check your dates");
            jDateFromWk4.requestFocusInWindow();
            jDateFromWk4.setFocusable(true);
        } else if (jDateToWk4.getDate() == null) {
            JOptionPane.showMessageDialog(jDialogMeetingWk4, "Date cannot be blank. Please check your dates");
            jDateToWk4.requestFocusInWindow();
            jDateToWk4.setFocusable(true);
        } else if (jDateFromWk4.getDate().after(jDateToWk4.getDate())) {
            JOptionPane.showMessageDialog(jDialogMeetingWk4, "End of cctivity date cannot be lower than start date. Please check your dates");
            jDateFromWk4.requestFocusInWindow();
            jDateFromWk4.setFocusable(true);
        } else if (jTextFieldBudgetWk4.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk4, "Please check budget code field");
            jTextFieldBudgetWk4.requestFocusInWindow();
            jTextFieldBudgetWk4.setFocusable(true);
        } else if (jTextAreaDetailDescriptionWk4.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk4, "Please check activity detailed description field");
            jTextAreaDetailDescriptionWk4.requestFocusInWindow();
            jTextAreaDetailDescriptionWk4.setFocusable(true);
        } else if (jTextPersonResWk4.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk4, "Please check responsible person field");
            jTextPersonResWk4.requestFocusInWindow();
            jTextPersonResWk4.setFocusable(true);
        } else if (jTextUnitCostWk4.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk4, "Please check unit cost field");
            jTextUnitCostWk4.requestFocusInWindow();
            jTextUnitCostWk4.setFocusable(true);
        } else if (jTextNumPpleWk4.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk4, "Please check number of people field");
            jTextNumPpleWk4.requestFocusInWindow();
            jTextNumPpleWk4.setFocusable(true);
        } else if (jTextNumDaysWk4.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk4, "Please check number of days field");
            jTextNumDaysWk4.requestFocusInWindow();
            jTextNumDaysWk4.setFocusable(true);
        } else if (("Busfare".equals(jComboMeetAllowanceWk4.getSelectedItem().toString())) && (jTextBusFareWk4.getText().trim().length() == 0)) {
            JOptionPane.showMessageDialog(jDialogMeetingWk4, "Please check number of days field");
            jTextBusFareWk4.requestFocusInWindow();
            jTextBusFareWk4.setFocusable(true);
        } else {
            calcBudTotWk4();
            model.addRow(new Object[]{jComboActivityProjectWk4.getSelectedItem().toString(), formatter.format(jDateFromWk4.getDate()),
                formatter.format(jDateToWk4.getDate()), jTextFieldBudgetWk4.getText(), jComboMeetingWk4.getSelectedItem().toString(),
                jTextAreaDetailDescriptionWk4.getText(), jComboMeetAllowanceWk4.getSelectedItem().toString(), jTextUnitCostWk4.getText(),
                jTextNumPpleWk4.getText(), jTextNumDaysWk4.getText(), String.format("%.2f", totBudgetLnReqAmt), jTextPersonResWk4.getText()});
            resetFieldsMeet();
            addMeetItemWk4();
        }
    }

    void Wk5MeetAddItm() {
        DefaultTableModel model = (DefaultTableModel) jTableMeetReqWk5.getModel();
        if (jDateFromWk5.getDate() == null) {
            JOptionPane.showMessageDialog(jDialogMeetingWk5, "Date cannot be blank. Please check your dates");
            jDateFromWk5.requestFocusInWindow();
            jDateFromWk5.setFocusable(true);
        } else if (jDateToWk5.getDate() == null) {
            JOptionPane.showMessageDialog(jDialogMeetingWk5, "Date cannot be blank. Please check your dates");
            jDateToWk5.requestFocusInWindow();
            jDateToWk5.setFocusable(true);
        } else if (jDateFromWk5.getDate().after(jDateToWk5.getDate())) {
            JOptionPane.showMessageDialog(jDialogMeetingWk5, "End of cctivity date cannot be lower than start date. Please check your dates");
            jDateFromWk5.requestFocusInWindow();
            jDateFromWk5.setFocusable(true);
        } else if (jTextFieldBudgetWk5.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk5, "Please check budget code field");
            jTextFieldBudgetWk5.requestFocusInWindow();
            jTextFieldBudgetWk5.setFocusable(true);
        } else if (jTextAreaDetailDescriptionWk5.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk5, "Please check activity detailed description field");
            jTextAreaDetailDescriptionWk5.requestFocusInWindow();
            jTextAreaDetailDescriptionWk5.setFocusable(true);
        } else if (jTextPersonResWk5.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk5, "Please check responsible person field");
            jTextPersonResWk5.requestFocusInWindow();
            jTextPersonResWk5.setFocusable(true);
        } else if (jTextUnitCostWk5.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk5, "Please check unit cost field");
            jTextUnitCostWk5.requestFocusInWindow();
            jTextUnitCostWk5.setFocusable(true);
        } else if (jTextNumPpleWk5.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk5, "Please check number of people field");
            jTextNumPpleWk5.requestFocusInWindow();
            jTextNumPpleWk5.setFocusable(true);
        } else if (jTextNumDaysWk5.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(jDialogMeetingWk5, "Please check number of days field");
            jTextNumDaysWk5.requestFocusInWindow();
            jTextNumDaysWk5.setFocusable(true);
        } else if (("Busfare".equals(jComboMeetAllowanceWk5.getSelectedItem().toString())) && (jTextBusFareWk5.getText().trim().length() == 0)) {
            JOptionPane.showMessageDialog(jDialogMeetingWk5, "Please check number of days field");
            jTextBusFareWk5.requestFocusInWindow();
            jTextBusFareWk5.setFocusable(true);
        } else {
            calcBudTotWk5();
            model.addRow(new Object[]{jComboActivityProjectWk5.getSelectedItem().toString(), formatter.format(jDateFromWk5.getDate()),
                formatter.format(jDateToWk5.getDate()), jTextFieldBudgetWk5.getText(), jComboMeetingWk5.getSelectedItem().toString(),
                jTextAreaDetailDescriptionWk5.getText(), jComboMeetAllowanceWk5.getSelectedItem().toString(), jTextUnitCostWk5.getText(),
                jTextNumPpleWk5.getText(), jTextNumDaysWk5.getText(), String.format("%.2f", totBudgetLnReqAmt), jTextPersonResWk5.getText()});
            resetFieldsMeet();
            addMeetItemWk5();
        }
    }

    void addMeetItemWk1() {

        int selectedOption = JOptionPane.showConfirmDialog(jDialogMeetingWk1,
                "Do want to add another partcipant activity line?",
                "Choose",
                JOptionPane.YES_NO_OPTION);
        if ((selectedOption == JOptionPane.YES_OPTION) && ("Y".equals(wk1Site)) && ("Y".equals(meetBudWk1))) {
            resetFieldsMeet();
            meetBudWk1 = "Y";
            perRespWk1 = "Y";
        } else {
            meetBudWk1 = "N";
            jDialogMeetingWk1.setVisible(false);
            jDialogWk1.setVisible(true);

        }

        /*To Change*/
    }

    void addMeetItemWk2() {

        int selectedOption = JOptionPane.showConfirmDialog(jDialogMeetingWk2,
                "Do want to add another partcipant activity line?",
                "Choose",
                JOptionPane.YES_NO_OPTION);
        if ((selectedOption == JOptionPane.YES_OPTION) && ("Y".equals(wk2Site)) && ("Y".equals(meetBudWk2))) {
            resetFieldsMeet();
            meetBudWk2 = "Y";
            perRespWk2 = "Y";
        } else {
            meetBudWk2 = "N";
            jDialogMeetingWk2.setVisible(false);
//            jDialogWk2.setVisible(true);

        }

        /*To Change*/
    }

    void addMeetItemWk3() {

        int selectedOption = JOptionPane.showConfirmDialog(jDialogMeetingWk1,
                "Do want to add another partcipant activity line?",
                "Choose",
                JOptionPane.YES_NO_OPTION);

        if ((selectedOption == JOptionPane.YES_OPTION) && ("Y".equals(wk3Site)) && ("Y".equals(meetBudWk3))) {
            resetFieldsMeet();
            meetBudWk3 = "Y";
            perRespWk3 = "Y";
        } else {
            meetBudWk3 = "N";
            jDialogMeetingWk3.setVisible(false);
//            jDialogWk3.setVisible(true);

        }

        /*To Change*/
    }

    void addMeetItemWk4() {

        int selectedOption = JOptionPane.showConfirmDialog(jDialogMeetingWk1,
                "Do want to add another partcipant activity line?",
                "Choose",
                JOptionPane.YES_NO_OPTION);

        if ((selectedOption == JOptionPane.YES_OPTION) && ("Y".equals(wk4Site)) && ("Y".equals(meetBudWk4))) {
            resetFieldsMeet();
            meetBudWk4 = "Y";
            perRespWk4 = "Y";
        } else {
            meetBudWk4 = "N";
            jDialogMeetingWk4.setVisible(false);
//            jDialogWk4.setVisible(true);

        }


        /*To Change*/
    }

    void addMeetItemWk5() {

        int selectedOption = JOptionPane.showConfirmDialog(jDialogMeetingWk1,
                "Do want to add another partcipant activity line?",
                "Choose",
                JOptionPane.YES_NO_OPTION);

        if ((selectedOption == JOptionPane.YES_OPTION) && ("Y".equals(wk5Site)) && ("Y".equals(meetBudWk5))) {
            resetFieldsMeet();
            meetBudWk5 = "Y";
            perRespWk5 = "Y";
        } else {
            meetBudWk5 = "N";
            jDialogMeetingWk5.setVisible(false);
//            jDialogWk5.setVisible(true);

        }

        /*To Change*/
    }

    void resetDateFrom() {
        jDateFromWk1.setCalendar(null);
        jDateFromWk2.setCalendar(null);
        jDateFromWk3.setCalendar(null);
        jDateFromWk4.setCalendar(null);
        jDateFromWk5.setCalendar(null);
    }

    void resetDateTo() {
        jDateToWk1.setCalendar(null);
        jDateToWk2.setCalendar(null);
        jDateToWk3.setCalendar(null);
        jDateToWk4.setCalendar(null);
        jDateToWk5.setCalendar(null);
    }

    void resetFieldsMeet() {
        try {
            jTextUnitCostWk1.setText("");
            jTextNumPpleWk1.setText("");
            jTextNumDaysWk1.setText("");
            jTextBusFareWk1.setText("");
            jTextPersonResWk1.setText("");
            jTextFieldBudgetWk1.setText("");
            jTextAreaDetailDescriptionWk1.setText("");
            jLabelTotalAmtWk1.setText("");
            jTextUnitCostWk2.setText("");
            jTextNumPpleWk2.setText("");
            jTextNumDaysWk2.setText("");
            jTextNumDaysWk2.setText("");
            jTextBusFareWk2.setText("");
            jTextPersonResWk2.setText("");
            jTextFieldBudgetWk2.setText("");
            jTextAreaDetailDescriptionWk2.setText("");
            jLabelTotalAmtWk2.setText("");
            jTextUnitCostWk3.setText("");
            jTextNumPpleWk3.setText("");
            jTextNumDaysWk3.setText("");
            jTextNumDaysWk3.setText("");
            jTextBusFareWk3.setText("");
            jTextPersonResWk3.setText("");
            jTextFieldBudgetWk3.setText("");
            jTextAreaDetailDescriptionWk3.setText("");
            jLabelTotalAmtWk3.setText("");
            jTextUnitCostWk4.setText("");
            jTextNumPpleWk4.setText("");
            jTextNumDaysWk4.setText("");
            jTextNumDaysWk4.setText("");
            jTextBusFareWk4.setText("");
            jTextPersonResWk4.setText("");
            jTextFieldBudgetWk4.setText("");
            jTextAreaDetailDescriptionWk4.setText("");
            jLabelTotalAmtWk4.setText("");
            jTextUnitCostWk5.setText("");
            jTextNumPpleWk5.setText("");
            jTextNumDaysWk5.setText("");
            jTextNumDaysWk5.setText("");
            jTextBusFareWk5.setText("");
            jTextPersonResWk5.setText("");
            jTextFieldBudgetWk5.setText("");
            jTextAreaDetailDescriptionWk5.setText("");
            jLabelTotalAmtWk5.setText("");
            resetDateFrom();
        } catch (Exception e) {
            System.out.println(e);
        }
        resetDateTo();
    }

    void resetFieldsBud() {
        try {
            if ("Busfare".equals(jComboMeetAllowanceWk1.getSelectedItem().toString())) {
                jTextUnitCostWk1.setText("1");
            } else {
                jTextUnitCostWk1.setText("");
            }
            jTextNumPpleWk1.setText("");
            jTextBusFareWk1.setText("");
            jLabelTotalAmtWk1.setText("");
            if ("Busfare".equals(jComboMeetAllowanceWk2.getSelectedItem().toString())) {
                jTextUnitCostWk2.setText("1");
            } else {
                jTextUnitCostWk2.setText("");
            }
            jTextNumPpleWk2.setText("");
            jTextBusFareWk2.setText("");
            jLabelTotalAmtWk2.setText("");
            if ("Busfare".equals(jComboMeetAllowanceWk3.getSelectedItem().toString())) {
                jTextUnitCostWk3.setText("1");
            } else {
                jTextUnitCostWk3.setText("");
            }
            jTextNumPpleWk3.setText("");
            jTextBusFareWk3.setText("");
            jLabelTotalAmtWk3.setText("");
            if ("Busfare".equals(jComboMeetAllowanceWk4.getSelectedItem().toString())) {
                jTextUnitCostWk4.setText("1");
            } else {
                jTextUnitCostWk4.setText("");
            }
            jTextNumPpleWk4.setText("");
            jTextBusFareWk4.setText("");
            jLabelTotalAmtWk4.setText("");
            if ("Busfare".equals(jComboMeetAllowanceWk5.getSelectedItem().toString())) {
                jTextUnitCostWk5.setText("1");
            } else {
                jTextUnitCostWk5.setText("");
            }
            jTextNumPpleWk5.setText("");
            jTextBusFareWk5.setText("");
            jLabelTotalAmtWk5.setText("");

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    void WkUsrRecUpd() {

        try {

       Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            String sqlrecplan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanUsrRecTab] "
                    + "(SERIAL,PLAN_REF_NUM,ACT_DATE,EMP_NAM,PLAN_WK,STATUS,ACT_TYPE) VALUES (?,?,?,?,?,?,?)";
            pst1 = conn.prepareStatement(sqlrecplan);
            pst1.setString(1, "P");
            pst1.setString(2, jTextFieldRefNum.getText());
            pst1.setString(3, actDate);
            pst1.setString(4, usrRecNam);
            pst1.setString(5, UsrRecWk);
            pst1.setString(6, "A");
            pst1.setString(7, usrActType);
            pst1.executeUpdate();

//            String sql1 = "update [ClaimsAppSysZvandiri].[dbo].[PlanUsrRecTab] set "
//                    + "STATUS ='P' where concat(PLAN_REF_NUM,EMP_NAM)=(SELECT concat(PLAN_REF_NUM,EMP_NAM) "
//                    + "FROM [ClaimsAppSysZvandiri].[dbo].[PlanReqClearTab] "
//                    + "where concat(PLAN_REF_NUM,EMP_NAM) = '" + jTextFieldRefNum.getText() + usrRecNam + "' "
//                    + "and ACT_REC_STA = 'A')";
//
//            pst = conn.prepareStatement(sql1);
//            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateWk1Plan() {
        try {
     Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {

                String sqlwk1plan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] "
                        + "(SERIAL,PLAN_REF_NUM,ACT_DATE,ACT_SITE,SITE_CLASSIFICATION,CONSO_RANK,"
                        + "TRAV_DIST,ACT_DESC,ACT_JUSTIFCATION,PER_DIEM_REQ,BUD_COD,"
                        + "BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,MSC_AMT,ACC_UNPROV_AMT,"
                        + "EMP_NAM1,EMP_NAM2,EMP_NAM3,EMP_NAM4,ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlwk1plan);
                pst1.setString(1, "P");
                pst1.setString(2, jTextFieldRefNum.getText());
                pst1.setString(3, jTableWk1Activities.getValueAt(i, 0).toString());
                pst1.setString(4, jTableWk1Activities.getValueAt(i, 1).toString());
                pst1.setString(5, jTableWk1Activities.getValueAt(i, 2).toString());
                pst1.setString(6, jTableWk1Activities.getValueAt(i, 3).toString());
                pst1.setString(7, jTableWk1Activities.getValueAt(i, 4).toString());
                pst1.setString(8, jTableWk1Activities.getValueAt(i, 5).toString());
                pst1.setString(9, jTableWk1Activities.getValueAt(i, 6).toString());
                pst1.setString(10, jTableWk1Activities.getValueAt(i, 7).toString());
                pst1.setString(11, jTableWk1Activities.getValueAt(i, 8).toString());
                pst1.setString(12, String.valueOf(jTableWk1Activities.getValueAt(i, 9).toString()));
                pst1.setString(13, String.valueOf(jTableWk1Activities.getValueAt(i, 10).toString()));
                pst1.setString(14, String.valueOf(jTableWk1Activities.getValueAt(i, 11).toString()));
                pst1.setString(15, String.valueOf(jTableWk1Activities.getValueAt(i, 12).toString()));
                pst1.setString(16, jTableWk1Activities.getValueAt(i, 13).toString());
                pst1.setString(17, String.valueOf(jTableWk1Activities.getValueAt(i, 14).toString()));
                pst1.setString(18, String.valueOf(jTableWk1Activities.getValueAt(i, 15).toString()));
                pst1.setString(19, jTableWk1Activities.getValueAt(i, 16).toString());
                pst1.setString(20, jTableWk1Activities.getValueAt(i, 17).toString());
                pst1.setString(21, jTableWk1Activities.getValueAt(i, 18).toString());
                pst1.setString(22, jTableWk1Activities.getValueAt(i, 19).toString());
                pst1.setString(23, docNextVer);
                pst1.setString(24, "1");
                pst1.setString(25, "A");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateWk2Plan() {
        try {
       Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {

                String sqlWk2plan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] "
                        + "(SERIAL,PLAN_REF_NUM,ACT_DATE,ACT_SITE,SITE_CLASSIFICATION,CONSO_RANK,"
                        + "TRAV_DIST,ACT_DESC,ACT_JUSTIFCATION,PER_DIEM_REQ,BUD_COD,"
                        + "BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,MSC_AMT,ACC_UNPROV_AMT,"
                        + "EMP_NAM1,EMP_NAM2,EMP_NAM3,EMP_NAM4,ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWk2plan);
                pst1.setString(1, "P");
                pst1.setString(2, jTextFieldRefNum.getText());
                pst1.setString(3, jTableWk2Activities.getValueAt(i, 0).toString());
                pst1.setString(4, jTableWk2Activities.getValueAt(i, 1).toString());
                pst1.setString(5, jTableWk2Activities.getValueAt(i, 2).toString());
                pst1.setString(6, jTableWk2Activities.getValueAt(i, 3).toString());
                pst1.setString(7, jTableWk2Activities.getValueAt(i, 4).toString());
                pst1.setString(8, jTableWk2Activities.getValueAt(i, 5).toString());
                pst1.setString(9, jTableWk2Activities.getValueAt(i, 6).toString());
                pst1.setString(10, jTableWk2Activities.getValueAt(i, 7).toString());
                pst1.setString(11, jTableWk2Activities.getValueAt(i, 8).toString());
                pst1.setString(12, String.valueOf(jTableWk2Activities.getValueAt(i, 9).toString()));
                pst1.setString(13, String.valueOf(jTableWk2Activities.getValueAt(i, 10).toString()));
                pst1.setString(14, String.valueOf(jTableWk2Activities.getValueAt(i, 11).toString()));
                pst1.setString(15, String.valueOf(jTableWk2Activities.getValueAt(i, 12).toString()));
                pst1.setString(16, jTableWk2Activities.getValueAt(i, 13).toString());
                pst1.setString(17, String.valueOf(jTableWk2Activities.getValueAt(i, 14).toString()));
                pst1.setString(18, String.valueOf(jTableWk2Activities.getValueAt(i, 15).toString()));
                pst1.setString(19, jTableWk2Activities.getValueAt(i, 16).toString());
                pst1.setString(20, jTableWk2Activities.getValueAt(i, 17).toString());
                pst1.setString(21, jTableWk2Activities.getValueAt(i, 18).toString());
                pst1.setString(22, jTableWk2Activities.getValueAt(i, 19).toString());
                pst1.setString(23, docNextVer);
                pst1.setString(24, "1");
                pst1.setString(25, "A");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateWk3Plan() {
        try {
         Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {

                String sqlWk3plan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] "
                        + "(SERIAL,PLAN_REF_NUM,ACT_DATE,ACT_SITE,SITE_CLASSIFICATION,CONSO_RANK,"
                        + "TRAV_DIST,ACT_DESC,ACT_JUSTIFCATION,PER_DIEM_REQ,BUD_COD,"
                        + "BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,MSC_AMT,ACC_UNPROV_AMT,"
                        + "EMP_NAM1,EMP_NAM2,EMP_NAM3,EMP_NAM4,ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWk3plan);
                pst1.setString(1, "P");
                pst1.setString(2, jTextFieldRefNum.getText());
                pst1.setString(3, jTableWk3Activities.getValueAt(i, 0).toString());
                pst1.setString(4, jTableWk3Activities.getValueAt(i, 1).toString());
                pst1.setString(5, jTableWk3Activities.getValueAt(i, 2).toString());
                pst1.setString(6, jTableWk3Activities.getValueAt(i, 3).toString());
                pst1.setString(7, jTableWk3Activities.getValueAt(i, 4).toString());
                pst1.setString(8, jTableWk3Activities.getValueAt(i, 5).toString());
                pst1.setString(9, jTableWk3Activities.getValueAt(i, 6).toString());
                pst1.setString(10, jTableWk3Activities.getValueAt(i, 7).toString());
                pst1.setString(11, jTableWk3Activities.getValueAt(i, 8).toString());
                pst1.setString(12, String.valueOf(jTableWk3Activities.getValueAt(i, 9).toString()));
                pst1.setString(13, String.valueOf(jTableWk3Activities.getValueAt(i, 10).toString()));
                pst1.setString(14, String.valueOf(jTableWk3Activities.getValueAt(i, 11).toString()));
                pst1.setString(15, String.valueOf(jTableWk3Activities.getValueAt(i, 12).toString()));
                pst1.setString(16, jTableWk3Activities.getValueAt(i, 13).toString());
                pst1.setString(17, String.valueOf(jTableWk3Activities.getValueAt(i, 14).toString()));
                pst1.setString(18, String.valueOf(jTableWk3Activities.getValueAt(i, 15).toString()));
                pst1.setString(19, jTableWk3Activities.getValueAt(i, 16).toString());
                pst1.setString(20, jTableWk3Activities.getValueAt(i, 17).toString());
                pst1.setString(21, jTableWk3Activities.getValueAt(i, 18).toString());
                pst1.setString(22, jTableWk3Activities.getValueAt(i, 19).toString());
                pst1.setString(23, docNextVer);
                pst1.setString(24, "1");
                pst1.setString(25, "A");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateWk4Plan() {
        try {
     Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            for (int i = 0; i < jTableWk4Activities.getRowCount(); i++) {

                String sqlWk4plan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab] "
                        + "(SERIAL,PLAN_REF_NUM,ACT_DATE,ACT_SITE,SITE_CLASSIFICATION,CONSO_RANK,"
                        + "TRAV_DIST,ACT_DESC,ACT_JUSTIFCATION,PER_DIEM_REQ,BUD_COD,"
                        + "BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,MSC_AMT,ACC_UNPROV_AMT,"
                        + "EMP_NAM1,EMP_NAM2,EMP_NAM3,EMP_NAM4,ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWk4plan);
                pst1.setString(1, "P");
                pst1.setString(2, jTextFieldRefNum.getText());
                pst1.setString(3, jTableWk4Activities.getValueAt(i, 0).toString());
                pst1.setString(4, jTableWk4Activities.getValueAt(i, 1).toString());
                pst1.setString(5, jTableWk4Activities.getValueAt(i, 2).toString());
                pst1.setString(6, jTableWk4Activities.getValueAt(i, 3).toString());
                pst1.setString(7, jTableWk4Activities.getValueAt(i, 4).toString());
                pst1.setString(8, jTableWk4Activities.getValueAt(i, 5).toString());
                pst1.setString(9, jTableWk4Activities.getValueAt(i, 6).toString());
                pst1.setString(10, jTableWk4Activities.getValueAt(i, 7).toString());
                pst1.setString(11, jTableWk4Activities.getValueAt(i, 8).toString());
                pst1.setString(12, String.valueOf(jTableWk4Activities.getValueAt(i, 9).toString()));
                pst1.setString(13, String.valueOf(jTableWk4Activities.getValueAt(i, 10).toString()));
                pst1.setString(14, String.valueOf(jTableWk4Activities.getValueAt(i, 11).toString()));
                pst1.setString(15, String.valueOf(jTableWk4Activities.getValueAt(i, 12).toString()));
                pst1.setString(16, jTableWk4Activities.getValueAt(i, 13).toString());
                pst1.setString(17, String.valueOf(jTableWk4Activities.getValueAt(i, 14).toString()));
                pst1.setString(18, String.valueOf(jTableWk4Activities.getValueAt(i, 15).toString()));
                pst1.setString(19, jTableWk4Activities.getValueAt(i, 16).toString());
                pst1.setString(20, jTableWk4Activities.getValueAt(i, 17).toString());
                pst1.setString(21, jTableWk4Activities.getValueAt(i, 18).toString());
                pst1.setString(22, jTableWk4Activities.getValueAt(i, 19).toString());
                pst1.setString(23, docNextVer);
                pst1.setString(24, "1");
                pst1.setString(25, "A");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateWk5Plan() {
        try {
      Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            for (int i = 0; i < jTableWk5Activities.getRowCount(); i++) {

                String sqlWk5plan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab] "
                        + "(SERIAL,PLAN_REF_NUM,ACT_DATE,ACT_SITE,SITE_CLASSIFICATION,CONSO_RANK,"
                        + "TRAV_DIST,ACT_DESC,ACT_JUSTIFCATION,PER_DIEM_REQ,BUD_COD,"
                        + "BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,MSC_AMT,ACC_UNPROV_AMT,"
                        + "EMP_NAM1,EMP_NAM2,EMP_NAM3,EMP_NAM4,ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWk5plan);
                pst1.setString(1, "P");
                pst1.setString(2, jTextFieldRefNum.getText());
                pst1.setString(3, jTableWk5Activities.getValueAt(i, 0).toString());
                pst1.setString(4, jTableWk5Activities.getValueAt(i, 1).toString());
                pst1.setString(5, jTableWk5Activities.getValueAt(i, 2).toString());
                pst1.setString(6, jTableWk5Activities.getValueAt(i, 3).toString());
                pst1.setString(7, jTableWk5Activities.getValueAt(i, 4).toString());
                pst1.setString(8, jTableWk5Activities.getValueAt(i, 5).toString());
                pst1.setString(9, jTableWk5Activities.getValueAt(i, 6).toString());
                pst1.setString(10, jTableWk5Activities.getValueAt(i, 7).toString());
                pst1.setString(11, jTableWk5Activities.getValueAt(i, 8).toString());
                pst1.setString(12, String.valueOf(jTableWk5Activities.getValueAt(i, 9).toString()));
                pst1.setString(13, String.valueOf(jTableWk5Activities.getValueAt(i, 10).toString()));
                pst1.setString(14, String.valueOf(jTableWk5Activities.getValueAt(i, 11).toString()));
                pst1.setString(15, String.valueOf(jTableWk5Activities.getValueAt(i, 12).toString()));
                pst1.setString(16, jTableWk5Activities.getValueAt(i, 13).toString());
                pst1.setString(17, String.valueOf(jTableWk5Activities.getValueAt(i, 14).toString()));
                pst1.setString(18, String.valueOf(jTableWk5Activities.getValueAt(i, 15).toString()));
                pst1.setString(19, jTableWk5Activities.getValueAt(i, 16).toString());
                pst1.setString(20, jTableWk5Activities.getValueAt(i, 17).toString());
                pst1.setString(21, jTableWk5Activities.getValueAt(i, 18).toString());
                pst1.setString(22, jTableWk5Activities.getValueAt(i, 19).toString());
                pst1.setString(23, docNextVer);
                pst1.setString(24, "1");
                pst1.setString(25, "A");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateMeetWk1() {
        itmNum = 1;
        try {
      Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            for (int i = 0; i < jTableMeetReqWk1.getRowCount(); i++) {

                String sqlWk1PlanMeet = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanMeetWk1Tab] "
                        + "(REF_YEAR,SERIAL,PLAN_REF_NUM,ITM_NUM,ACT_PROJ,ACT_DAT_FRO,ACT_DAT_TO,LN_BUD_CODE,"
                        + "ACT_ITM_PUR,ACT_DETAILED_PUR,BUD_ITM,UNIT_COST,"
                        + "UNIT_PPLE,UNIT_DAYS,ACT_ITM_TOT,RESP_EMP_NAM,ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst = conn.prepareStatement(sqlWk1PlanMeet);
                pst.setString(1, Integer.toString(finyear));
                pst.setString(2, "P");
                pst.setString(3, jTextFieldRefNum.getText());
                pst.setString(4, Integer.toString(itmNum));
                pst.setString(5, jTableMeetReqWk1.getValueAt(i, 0).toString());
                pst.setString(6, jTableMeetReqWk1.getValueAt(i, 1).toString());
                pst.setString(7, jTableMeetReqWk1.getValueAt(i, 2).toString());
                pst.setString(8, jTableMeetReqWk1.getValueAt(i, 3).toString());
                pst.setString(9, jTableMeetReqWk1.getValueAt(i, 4).toString());
                pst.setString(10, jTableMeetReqWk1.getValueAt(i, 5).toString());
                pst.setString(11, jTableMeetReqWk1.getValueAt(i, 6).toString());
                pst.setString(12, jTableMeetReqWk1.getValueAt(i, 7).toString());
                pst.setString(13, String.valueOf(jTableMeetReqWk1.getValueAt(i, 8).toString()));
                pst.setString(14, String.valueOf(jTableMeetReqWk1.getValueAt(i, 9).toString()));
                pst.setString(15, String.valueOf(jTableMeetReqWk1.getValueAt(i, 10).toString()));
                pst.setString(16, String.valueOf(jTableMeetReqWk1.getValueAt(i, 11).toString()));
                pst.setString(17, docNextVer);
                pst.setString(18, "1");
                pst.setString(19, "A");

                pst.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateMeetWk2() {
        itmNum = 1;
        try {
         Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            for (int i = 0; i < jTableMeetReqWk2.getRowCount(); i++) {

                String sqlWk2PlanMeet = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanMeetWk2Tab] "
                        + "(REF_YEAR,SERIAL,PLAN_REF_NUM,ITM_NUM,ACT_PROJ,ACT_DAT_FRO,ACT_DAT_TO,LN_BUD_CODE,"
                        + "ACT_ITM_PUR,ACT_DETAILED_PUR,BUD_ITM,UNIT_COST,"
                        + "UNIT_PPLE,UNIT_DAYS,ACT_ITM_TOT,RESP_EMP_NAM,ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst = conn.prepareStatement(sqlWk2PlanMeet);
                pst.setString(1, Integer.toString(finyear));
                pst.setString(2, "P");
                pst.setString(3, jTextFieldRefNum.getText());
                pst.setString(4, Integer.toString(itmNum));
                pst.setString(5, jTableMeetReqWk2.getValueAt(i, 0).toString());
                pst.setString(6, jTableMeetReqWk2.getValueAt(i, 1).toString());
                pst.setString(7, jTableMeetReqWk2.getValueAt(i, 2).toString());
                pst.setString(8, jTableMeetReqWk2.getValueAt(i, 3).toString());
                pst.setString(9, jTableMeetReqWk2.getValueAt(i, 4).toString());
                pst.setString(10, jTableMeetReqWk2.getValueAt(i, 5).toString());
                pst.setString(11, jTableMeetReqWk2.getValueAt(i, 6).toString());
                pst.setString(12, jTableMeetReqWk2.getValueAt(i, 7).toString());
                pst.setString(13, String.valueOf(jTableMeetReqWk2.getValueAt(i, 8).toString()));
                pst.setString(14, String.valueOf(jTableMeetReqWk2.getValueAt(i, 9).toString()));
                pst.setString(15, String.valueOf(jTableMeetReqWk2.getValueAt(i, 10).toString()));
                pst.setString(16, String.valueOf(jTableMeetReqWk2.getValueAt(i, 11).toString()));
                pst.setString(17, docNextVer);
                pst.setString(18, "1");
                pst.setString(19, "A");

                pst.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateMeetWk3() {
        itmNum = 1;
        try {
           Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            for (int i = 0; i < jTableMeetReqWk3.getRowCount(); i++) {

                String sqlWk3PlanMeet = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanMeetWk3Tab] "
                        + "(REF_YEAR,SERIAL,PLAN_REF_NUM,ITM_NUM,ACT_PROJ,ACT_DAT_FRO,ACT_DAT_TO,LN_BUD_CODE,"
                        + "ACT_ITM_PUR,ACT_DETAILED_PUR,BUD_ITM,UNIT_COST,"
                        + "UNIT_PPLE,UNIT_DAYS,ACT_ITM_TOT,RESP_EMP_NAM,ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst = conn.prepareStatement(sqlWk3PlanMeet);
                pst.setString(1, Integer.toString(finyear));
                pst.setString(2, "P");
                pst.setString(3, jTextFieldRefNum.getText());
                pst.setString(4, Integer.toString(itmNum));
                pst.setString(5, jTableMeetReqWk3.getValueAt(i, 0).toString());
                pst.setString(6, jTableMeetReqWk3.getValueAt(i, 1).toString());
                pst.setString(7, jTableMeetReqWk3.getValueAt(i, 2).toString());
                pst.setString(8, jTableMeetReqWk3.getValueAt(i, 3).toString());
                pst.setString(9, jTableMeetReqWk3.getValueAt(i, 4).toString());
                pst.setString(10, jTableMeetReqWk3.getValueAt(i, 5).toString());
                pst.setString(11, jTableMeetReqWk3.getValueAt(i, 6).toString());
                pst.setString(12, jTableMeetReqWk3.getValueAt(i, 7).toString());
                pst.setString(13, String.valueOf(jTableMeetReqWk3.getValueAt(i, 8).toString()));
                pst.setString(14, String.valueOf(jTableMeetReqWk3.getValueAt(i, 9).toString()));
                pst.setString(15, String.valueOf(jTableMeetReqWk3.getValueAt(i, 10).toString()));
                pst.setString(16, String.valueOf(jTableMeetReqWk3.getValueAt(i, 11).toString()));
                pst.setString(17, docNextVer);
                pst.setString(18, "1");
                pst.setString(19, "A");

                pst.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateMeetWk4() {
        itmNum = 1;
        try {
         Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            for (int i = 0; i < jTableMeetReqWk4.getRowCount(); i++) {

                String sqlWk4PlanMeet = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanMeetWk4Tab] "
                        + "(REF_YEAR,SERIAL,PLAN_REF_NUM,ITM_NUM,ACT_PROJ,ACT_DAT_FRO,ACT_DAT_TO,LN_BUD_CODE,"
                        + "ACT_ITM_PUR,ACT_DETAILED_PUR,BUD_ITM,UNIT_COST,"
                        + "UNIT_PPLE,UNIT_DAYS,ACT_ITM_TOT,RESP_EMP_NAM,ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst = conn.prepareStatement(sqlWk4PlanMeet);
                pst.setString(1, Integer.toString(finyear));
                pst.setString(2, "P");
                pst.setString(3, jTextFieldRefNum.getText());
                pst.setString(4, Integer.toString(itmNum));
                pst.setString(5, jTableMeetReqWk4.getValueAt(i, 0).toString());
                pst.setString(6, jTableMeetReqWk4.getValueAt(i, 1).toString());
                pst.setString(7, jTableMeetReqWk4.getValueAt(i, 2).toString());
                pst.setString(8, jTableMeetReqWk4.getValueAt(i, 3).toString());
                pst.setString(9, jTableMeetReqWk4.getValueAt(i, 4).toString());
                pst.setString(10, jTableMeetReqWk4.getValueAt(i, 5).toString());
                pst.setString(11, jTableMeetReqWk4.getValueAt(i, 6).toString());
                pst.setString(12, jTableMeetReqWk4.getValueAt(i, 7).toString());
                pst.setString(13, String.valueOf(jTableMeetReqWk4.getValueAt(i, 8).toString()));
                pst.setString(14, String.valueOf(jTableMeetReqWk4.getValueAt(i, 9).toString()));
                pst.setString(15, String.valueOf(jTableMeetReqWk4.getValueAt(i, 10).toString()));
                pst.setString(16, String.valueOf(jTableMeetReqWk4.getValueAt(i, 11).toString()));
                pst.setString(17, docNextVer);
                pst.setString(18, "1");
                pst.setString(19, "A");

                pst.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateMeetWk5() {
        itmNum = 1;
        try {
         Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            for (int i = 0; i < jTableMeetReqWk5.getRowCount(); i++) {

                String sqlWk5PlanMeet = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanMeetWk5Tab] "
                        + "(REF_YEAR,SERIAL,PLAN_REF_NUM,ITM_NUM,ACT_PROJ,ACT_DAT_FRO,ACT_DAT_TO,LN_BUD_CODE,"
                        + "ACT_ITM_PUR,ACT_DETAILED_PUR,BUD_ITM,UNIT_COST,"
                        + "UNIT_PPLE,UNIT_DAYS,ACT_ITM_TOT,RESP_EMP_NAM,ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst = conn.prepareStatement(sqlWk5PlanMeet);
                pst.setString(1, Integer.toString(finyear));
                pst.setString(2, "P");
                pst.setString(3, jTextFieldRefNum.getText());
                pst.setString(4, Integer.toString(itmNum));
                pst.setString(5, jTableMeetReqWk5.getValueAt(i, 0).toString());
                pst.setString(6, jTableMeetReqWk5.getValueAt(i, 1).toString());
                pst.setString(7, jTableMeetReqWk5.getValueAt(i, 2).toString());
                pst.setString(8, jTableMeetReqWk5.getValueAt(i, 3).toString());
                pst.setString(9, jTableMeetReqWk5.getValueAt(i, 4).toString());
                pst.setString(10, jTableMeetReqWk5.getValueAt(i, 5).toString());
                pst.setString(11, jTableMeetReqWk5.getValueAt(i, 6).toString());
                pst.setString(12, jTableMeetReqWk5.getValueAt(i, 7).toString());
                pst.setString(13, String.valueOf(jTableMeetReqWk5.getValueAt(i, 8).toString()));
                pst.setString(14, String.valueOf(jTableMeetReqWk5.getValueAt(i, 9).toString()));
                pst.setString(15, String.valueOf(jTableMeetReqWk5.getValueAt(i, 10).toString()));
                pst.setString(16, String.valueOf(jTableMeetReqWk5.getValueAt(i, 11).toString()));
                pst.setString(17, docNextVer);
                pst.setString(18, "1");
                pst.setString(19, "A");

                pst.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updatePrevRecord() {
        try {
          Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
          String sqlPlanAct = "update [ClaimsAppSysZvandiri].[dbo].[PlanActTab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA in ('A','C') ";

            pst = conn.prepareStatement(sqlPlanAct);
            pst.executeUpdate();

            String sqlPlanPeriod = "update [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA in ('A','C') ";

            pst = conn.prepareStatement(sqlPlanPeriod);
            pst.executeUpdate();

            String sqlPlanWk1 = "update [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA in ('A','C') ";

            pst = conn.prepareStatement(sqlPlanWk1);
            pst.executeUpdate();

            String sqlPlanWk2 = "update [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA in ('A','C') ";

            pst = conn.prepareStatement(sqlPlanWk2);
            pst.executeUpdate();

            String sqlPlanWk3 = "update [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA in ('A','C') ";

            pst = conn.prepareStatement(sqlPlanWk3);
            pst.executeUpdate();

            String sqlPlanWk4 = "update [ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA in ('A','C') ";

            pst = conn.prepareStatement(sqlPlanWk4);
            pst.executeUpdate();

            String sqlPlanWk5 = "update [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA in ('A','C') ";

            pst = conn.prepareStatement(sqlPlanWk5);
            pst.executeUpdate();

            String sqlPlanMeetWk1 = "update [ClaimsAppSysZvandiri].[dbo].[PlanMeetWk1Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA in ('A','C') ";

            pst = conn.prepareStatement(sqlPlanMeetWk1);
            pst.executeUpdate();

            String sqlPlanMeetWk2 = "update [ClaimsAppSysZvandiri].[dbo].[PlanMeetWk2Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA in ('A','C') ";

            pst = conn.prepareStatement(sqlPlanMeetWk2);
            pst.executeUpdate();

            String sqlPlanMeetWk3 = "update [ClaimsAppSysZvandiri].[dbo].[PlanMeetWk3Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA in ('A','C') ";

            pst = conn.prepareStatement(sqlPlanMeetWk3);
            pst.executeUpdate();

            String sqlPlanMeetWk4 = "update [ClaimsAppSysZvandiri].[dbo].[PlanMeetWk4Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA in ('A','C') ";

            pst = conn.prepareStatement(sqlPlanMeetWk4);
            pst.executeUpdate();

            String sqlPlanMeetWk5 = "update [ClaimsAppSysZvandiri].[dbo].[PlanMeetWk5Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA in ('A','C') ";

            pst = conn.prepareStatement(sqlPlanMeetWk5);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    void updateWkPlanPeriod() {
        try {

         Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            if (jTableWk1Activities.getRowCount() > 0 && jTableWk2Activities.getRowCount() == 0
                    && jTableWk3Activities.getRowCount() == 0 && jTableWk4Activities.getRowCount() == 0
                    && jTableWk5Activities.getRowCount() == 0) {

                String sqlWkplanperiod = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] "
                        + "(SERIAL,PLAN_REF_NUM,PROVINCE,DISTRICT,Wk1_PLAN_START_DATE,Wk1_PLAN_END_DATE,"
                        + "ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWkplanperiod);
                pst1.setString(1, "P");
                pst1.setString(2, jTextFieldRefNum.getText());
                pst1.setString(3, jLabelProvince.getText());
                pst1.setString(4, jLabelDistrict.getText());
                pst1.setString(5, formatter.format(jDateChooserWk1From.getDate()));
                pst1.setString(6, formatter.format(jDateChooserWk1To.getDate()));
                pst1.setString(7, docNextVer);
                pst1.setString(8, "1");
                pst1.setString(9, "A");

                pst1.executeUpdate();

            } else if (jTableWk2Activities.getRowCount() > 0 && jTableWk3Activities.getRowCount() == 0
                    && jTableWk4Activities.getRowCount() == 0 && jTableWk5Activities.getRowCount() == 0) {

                String sqlWkplanperiod = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] "
                        + "(SERIAL,PLAN_REF_NUM,PROVINCE,DISTRICT,Wk1_PLAN_START_DATE,Wk1_PLAN_END_DATE,"
                        + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,"
                        + "ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWkplanperiod);
                pst1.setString(1, "P");
                pst1.setString(2, jTextFieldRefNum.getText());
                pst1.setString(3, jLabelProvince.getText());
                pst1.setString(4, jLabelDistrict.getText());
                pst1.setString(5, formatter.format(jDateChooserWk1From.getDate()));
                pst1.setString(6, formatter.format(jDateChooserWk1To.getDate()));
                pst1.setString(7, formatter.format(jDateChooserWk2From.getDate()));
                pst1.setString(8, formatter.format(jDateChooserWk2To.getDate()));
                pst1.setString(9, docNextVer);
                pst1.setString(10, "1");
                pst1.setString(11, "A");

                pst1.executeUpdate();
            } else if (jTableWk3Activities.getRowCount() > 0 && jTableWk4Activities.getRowCount() == 0
                    && jTableWk5Activities.getRowCount() == 0) {

                String sqlWkplanperiod = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] "
                        + "(SERIAL,PLAN_REF_NUM,PROVINCE,DISTRICT,Wk1_PLAN_START_DATE,Wk1_PLAN_END_DATE,"
                        + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,Wk3_PLAN_START_DATE,Wk3_PLAN_END_DATE,"
                        + "ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWkplanperiod);
                pst1.setString(1, "P");
                pst1.setString(2, jTextFieldRefNum.getText());
                pst1.setString(3, jLabelProvince.getText());
                pst1.setString(4, jLabelDistrict.getText());
                pst1.setString(5, formatter.format(jDateChooserWk1From.getDate()));
                pst1.setString(6, formatter.format(jDateChooserWk1To.getDate()));
                pst1.setString(7, formatter.format(jDateChooserWk2From.getDate()));
                pst1.setString(8, formatter.format(jDateChooserWk2To.getDate()));
                pst1.setString(9, formatter.format(jDateChooserWk3From.getDate()));
                pst1.setString(10, formatter.format(jDateChooserWk3To.getDate()));
                pst1.setString(11, docNextVer);
                pst1.setString(12, "1");
                pst1.setString(13, "A");

                pst1.executeUpdate();

            } else if (jTableWk4Activities.getRowCount() > 0 && jTableWk5Activities.getRowCount() == 0) {

                String sqlWkplanperiod = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] "
                        + "(SERIAL,PLAN_REF_NUM,PROVINCE,DISTRICT,Wk1_PLAN_START_DATE,Wk1_PLAN_END_DATE,"
                        + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,Wk3_PLAN_START_DATE,Wk3_PLAN_END_DATE,"
                        + "Wk4_PLAN_START_DATE,Wk4_PLAN_END_DATE,ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWkplanperiod);
                pst1.setString(1, "P");
                pst1.setString(2, jTextFieldRefNum.getText());
                pst1.setString(3, jLabelProvince.getText());
                pst1.setString(4, jLabelDistrict.getText());
                pst1.setString(5, formatter.format(jDateChooserWk1From.getDate()));
                pst1.setString(6, formatter.format(jDateChooserWk1To.getDate()));
                pst1.setString(7, formatter.format(jDateChooserWk2From.getDate()));
                pst1.setString(8, formatter.format(jDateChooserWk2To.getDate()));
                pst1.setString(9, formatter.format(jDateChooserWk3From.getDate()));
                pst1.setString(10, formatter.format(jDateChooserWk3To.getDate()));
                pst1.setString(11, formatter.format(jDateChooserWk4From.getDate()));
                pst1.setString(12, formatter.format(jDateChooserWk4To.getDate()));
                pst1.setString(13, docNextVer);
                pst1.setString(14, "1");
                pst1.setString(15, "A");

                pst1.executeUpdate();

            } else if (jTableWk5Activities.getRowCount() > 0) {

                String sqlWkplanperiod = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] "
                        + "(SERIAL,PLAN_REF_NUM,PROVINCE,DISTRICT,Wk1_PLAN_START_DATE,Wk1_PLAN_END_DATE,"
                        + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,Wk3_PLAN_START_DATE,Wk3_PLAN_END_DATE,"
                        + "Wk4_PLAN_START_DATE,Wk4_PLAN_END_DATE,Wk5_PLAN_START_DATE,Wk5_PLAN_END_DATE,ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWkplanperiod);
                pst1.setString(1, "P");
                pst1.setString(2, jTextFieldRefNum.getText());
                pst1.setString(3, jLabelProvince.getText());
                pst1.setString(4, jLabelDistrict.getText());
                pst1.setString(5, formatter.format(jDateChooserWk1From.getDate()));
                pst1.setString(6, formatter.format(jDateChooserWk1To.getDate()));
                pst1.setString(7, formatter.format(jDateChooserWk2From.getDate()));
                pst1.setString(8, formatter.format(jDateChooserWk2To.getDate()));
                pst1.setString(9, formatter.format(jDateChooserWk3From.getDate()));
                pst1.setString(10, formatter.format(jDateChooserWk3To.getDate()));
                pst1.setString(11, formatter.format(jDateChooserWk4From.getDate()));
                pst1.setString(12, formatter.format(jDateChooserWk4To.getDate()));
                pst1.setString(13, formatter.format(jDateChooserWk4From.getDate()));
                pst1.setString(14, formatter.format(jDateChooserWk4To.getDate()));
                pst1.setString(15, docNextVer);
                pst1.setString(16, "1");
                pst1.setString(17, "A");

                pst1.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateWkPlanAction() {
        try {
         Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            String sqlWkplanperiod = "insert into [ClaimsAppSysZvandiri].[dbo].[PlanActTab] (SERIAL,PLAN_REF_NUM,USR_ACTION,DOC_STATUS,"
                    + "ACTION_BY,SEND_TO,ACTION_DATE,ACTION_TIME,"
                    + "ACTIONED_ON_COMPUTER,COMMENTS,ACT_VER,DOC_VER,ACT_REC_STA)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

            pst1 = conn.prepareStatement(sqlWkplanperiod);
            pst1.setString(1, "P");
            pst1.setString(2, jTextFieldRefNum.getText());
            pst1.setString(3, "Plan - Created");
            pst1.setString(4, "Created");
            pst1.setString(5, jLabelLineLogNam.getText());
            if ((sendToProvMgr.equals(jLabelLineLogNam.getText())) || ("Central Office".equals(jLabelProvince.getText())) || ("Harare Province".equals(jLabelProvince.getText()))) {
                pst1.setString(6, supNam);
            } else {
                pst1.setString(6, sendToProvMgr);
            }
            pst1.setString(7, jLabelLineDate.getText());
            pst1.setString(8, jLabelLineTime.getText());
            pst1.setString(9, hostName);
            pst1.setString(10, docNextVer);
            pst1.setString(10, jTextAreaComments.getText());
            pst1.setString(11, docNextVer);
            pst1.setString(12, "1");
            pst1.setString(13, "A");

            pst1.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

 void resetField() {

        jDateChooserDialogWk1ActivityDate.setDate(null);
        jTextFieldDialogWk1Site.setText("");
        jTextFieldWk1DialogActivityDesc.setText("");
        jTextAreaWk1DialogJustification.setText("");
        jTextFieldWk1DialogStaffName1.setText("");
        jTextFieldWk1DialogStaffName2.setText("");
        jTextFieldWk1DialogStaffName3.setText("");
        jTextFieldWk1DialogStaffName4.setText("");
        jCheckBoxDialogWk1BrkFast.setEnabled(true);
        jCheckBoxDialogWk1Dinner.setEnabled(true);
        jCheckBoxDialogWk1AccProved.setEnabled(true);
        jCheckBoxDialogWk1Inc.setEnabled(true);
        jCheckBoxDialogWk1Misc.setEnabled(true);
        jCheckBoxDialogWk1BrkFast.setSelected(false);
        jCheckBoxDialogWk1Lunch.setSelected(false);
        jCheckBoxDialogWk1Dinner.setSelected(false);
        jCheckBoxDialogWk1AccProved.setSelected(false);
        jCheckBoxDialogWk1Inc.setSelected(false);
        jCheckBoxDialogWk1Misc.setSelected(false);
        jCheckBoxDialogWk1AccUnProved.setSelected(false);
        jCheckBoxDialogWk1AccProved.setSelected(false);
        jTextFieldWk1Misc.setVisible(false);
        jLabelWk1Misc.setVisible(false);
        jTextFieldWk1Misc.setText("");
        jLabelWk1MiscAmt.setVisible(false);
        jTextFieldWk1MiscAmt.setVisible(false);
        jTextFieldWk1MiscAmt.setText("");

    }

    void fetchdataGenWkC1() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            st.executeQuery("SELECT PROVINCE ,DISTRICT ,WK1_PLAN_START_DATE ,WK1_PLAN_END_DATE,"
                    + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,Wk3_PLAN_START_DATE,Wk3_PLAN_END_DATE, "
                    + "Wk4_PLAN_START_DATE,Wk4_PLAN_END_DATE,Wk5_PLAN_START_DATE,Wk5_PLAN_END_DATE "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab]"
                    + " where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'C'");

            ResultSet r = st.getResultSet();

            while (r.next()) {

                jLabelProvince.setText(r.getString(1));
                jLabelDistrict.setText(r.getString(2));

                if ((!("".equals(r.getString(3)))) && (r.getString(5) == null) && (r.getString(7) == null)
                        && (r.getString(9) == null) && (r.getString(11) == null)) {
                    java.util.Date dateWk1From = formatter.parse(r.getString(3));
                    java.util.Date dateWk1To = formatter.parse(r.getString(4));

                    jDateChooserWk1From.setDate(dateWk1From);
                    jDateChooserWk1To.setDate(dateWk1To);
                } else if ((!("".equals(r.getString(3)))) && (!("".equals(r.getString(5)))) && (r.getString(7) == null)
                        && (r.getString(9) == null) && (r.getString(11) == null)) {
                    java.util.Date dateWk1From = formatter.parse(r.getString(3));
                    java.util.Date dateWk1To = formatter.parse(r.getString(4));
                    java.util.Date dateWk2From = formatter.parse(r.getString(5));
                    java.util.Date dateWk2To = formatter.parse(r.getString(6));

                    jDateChooserWk1From.setDate(dateWk1From);
                    jDateChooserWk1To.setDate(dateWk1To);
                    jDateChooserWk2From.setDate(dateWk2From);
                    jDateChooserWk2To.setDate(dateWk2To);

                } else if ((!("".equals(r.getString(3)))) && (!("".equals(r.getString(5)))) && (!("".equals(r.getString(7))))
                        && (r.getString(9) == null) && (r.getString(11) == null)) {
                    java.util.Date dateWk1From = formatter.parse(r.getString(3));
                    java.util.Date dateWk1To = formatter.parse(r.getString(4));
                    java.util.Date dateWk2From = formatter.parse(r.getString(5));
                    java.util.Date dateWk2To = formatter.parse(r.getString(6));
                    java.util.Date dateWk3From = formatter.parse(r.getString(7));
                    java.util.Date dateWk3To = formatter.parse(r.getString(8));

                    jDateChooserWk1From.setDate(dateWk1From);
                    jDateChooserWk1To.setDate(dateWk1To);
                    jDateChooserWk2From.setDate(dateWk2From);
                    jDateChooserWk2To.setDate(dateWk2To);
                    jDateChooserWk3From.setDate(dateWk3From);
                    jDateChooserWk3To.setDate(dateWk3To);

                } else if ((!("".equals(r.getString(3)))) && (!("".equals(r.getString(5)))) && (!("".equals(r.getString(7))))
                        && (!("".equals(r.getString(9)))) && (r.getString(11) == null)) {
                    java.util.Date dateWk1From = formatter.parse(r.getString(3));
                    java.util.Date dateWk1To = formatter.parse(r.getString(4));
                    java.util.Date dateWk2From = formatter.parse(r.getString(5));
                    java.util.Date dateWk2To = formatter.parse(r.getString(6));
                    java.util.Date dateWk3From = formatter.parse(r.getString(7));
                    java.util.Date dateWk3To = formatter.parse(r.getString(8));
                    java.util.Date dateWk4From = formatter.parse(r.getString(9));
                    java.util.Date dateWk4To = formatter.parse(r.getString(10));

                    jDateChooserWk1From.setDate(dateWk1From);
                    jDateChooserWk1To.setDate(dateWk1To);
                    jDateChooserWk2From.setDate(dateWk2From);
                    jDateChooserWk2To.setDate(dateWk2To);
                    jDateChooserWk3From.setDate(dateWk3From);
                    jDateChooserWk3To.setDate(dateWk3To);
                    jDateChooserWk4From.setDate(dateWk4From);
                    jDateChooserWk4To.setDate(dateWk4To);

                } else {
                    java.util.Date dateWk1From = formatter.parse(r.getString(3));
                    java.util.Date dateWk1To = formatter.parse(r.getString(4));
                    java.util.Date dateWk2From = formatter.parse(r.getString(5));
                    java.util.Date dateWk2To = formatter.parse(r.getString(6));
                    java.util.Date dateWk3From = formatter.parse(r.getString(7));
                    java.util.Date dateWk3To = formatter.parse(r.getString(8));
                    java.util.Date dateWk4From = formatter.parse(r.getString(9));
                    java.util.Date dateWk4To = formatter.parse(r.getString(10));
                    java.util.Date dateWk5From = formatter.parse(r.getString(11));
                    java.util.Date dateWk5To = formatter.parse(r.getString(12));

                    jDateChooserWk1From.setDate(dateWk1From);
                    jDateChooserWk1To.setDate(dateWk1To);
                    jDateChooserWk2From.setDate(dateWk2From);
                    jDateChooserWk2To.setDate(dateWk2To);
                    jDateChooserWk3From.setDate(dateWk3From);
                    jDateChooserWk3To.setDate(dateWk3To);
                    jDateChooserWk4From.setDate(dateWk4From);
                    jDateChooserWk4To.setDate(dateWk4To);
                    jDateChooserWk5From.setDate(dateWk4From);
                    jDateChooserWk5To.setDate(dateWk4To);
                }
                origDate = jDateChooserWk1From.getDate();

            }

            if (jTableWk1Activities.getRowCount() > 0) {

                st1.executeQuery("SELECT distinct ACT_VER,ACT_VER + 1  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] "
                        + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'C'");

                ResultSet r1 = st1.getResultSet();

                while (r1.next()) {
                    docVer = r1.getString(1);
                    docNextVer = r1.getString(2);
                }
            } else if (jTableWk2Activities.getRowCount() > 0) {

                st1.executeQuery("SELECT distinct ACT_VER,ACT_VER + 1  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] "
                        + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'C'");

                ResultSet r1 = st1.getResultSet();

                while (r1.next()) {
                    docVer = r1.getString(1);
                    docNextVer = r1.getString(2);
                }
            } else if (jTableWk3Activities.getRowCount() > 0) {

                st1.executeQuery("SELECT distinct ACT_VER,ACT_VER + 1  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] "
                        + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'C'");

                ResultSet r1 = st1.getResultSet();

                while (r1.next()) {
                    docVer = r1.getString(1);
                    docNextVer = r1.getString(2);
                }
            } else if (jTableWk4Activities.getRowCount() > 0) {

                st1.executeQuery("SELECT distinct ACT_VER,ACT_VER + 1  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab] "
                        + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'C'");

                ResultSet r1 = st1.getResultSet();

                while (r1.next()) {
                    docVer = r1.getString(1);
                    docNextVer = r1.getString(2);
                }
            } else if (jTableWk5Activities.getRowCount() > 0) {

                st1.executeQuery("SELECT distinct ACT_VER,ACT_VER + 1  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab] "
                        + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'C'");

                ResultSet r1 = st1.getResultSet();

                while (r1.next()) {
                    docVer = r1.getString(1);
                    docNextVer = r1.getString(2);
                }
            }

            st1.executeQuery("SELECT COMMENTS FROM [ClaimsAppSysZvandiri].[dbo].[PlanActTab]"
                    + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'C'");

            ResultSet r1 = st1.getResultSet();

            while (r1.next()) {
                jTextAreaComments.setText(r1.getString(1));
            }
            //    jTabbedPaneMain.setSelectedIndex(0);
            jTextAreaComments.requestFocusInWindow();
            jTextAreaComments.setFocusable(true);
            jTextAreaComments.setLineWrap(true);
            jTextAreaComments.setWrapStyleWord(true);
            jTextAreaComments.setEditable(false);
            jLabelPostAppMod1.setText("Post Approval Modification");
            jLabelPostAppMod2.setText("Post Approval Modification");
            jLabelPostAppMod3.setText("Post Approval Modification");
            jLabelPostAppMod4.setText("Post Approval Modification");
            jLabelPostAppMod5.setText("Post Approval Modification");
        } catch (Exception e) {

            System.out.println(e);
        }
    }

    void fetchdataWkC1() {
        try {

           Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] "
                    + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'C'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk1.insertRow(modelWk1.getRowCount(), new Object[]{r.getString(3), r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22)});

            }

        } catch (Exception e) {

        }
    }

    void fetchdataWkC2() {
        try {

          Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] "
                    + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'C'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk2.insertRow(modelWk2.getRowCount(), new Object[]{r.getString(3), r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22)});

            }

        } catch (Exception e) {

        }
    }

    void fetchdataWkC3() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] "
                    + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'C'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk3.insertRow(modelWk3.getRowCount(), new Object[]{r.getString(3), r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22)});

            }

        } catch (Exception e) {

        }
    }

    void fetchdataWkC4() {
        try {

           Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab] "
                    + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'C'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk4.insertRow(modelWk4.getRowCount(), new Object[]{r.getString(3), r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22)});

            }

        } catch (Exception e) {

        }
    }

    void fetchdataWkC5() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab] "
                    + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'C'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk5.insertRow(modelWk5.getRowCount(), new Object[]{r.getString(3), r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22)});

            }

        } catch (Exception e) {

        }
    }

    void fetchdataGenWk1() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            st.executeQuery("SELECT PROVINCE ,DISTRICT ,WK1_PLAN_START_DATE ,WK1_PLAN_END_DATE,"
                    + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,Wk3_PLAN_START_DATE,Wk3_PLAN_END_DATE,"
                    + "Wk4_PLAN_START_DATE,Wk4_PLAN_END_DATE,Wk5_PLAN_START_DATE,Wk5_PLAN_END_DATE "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab]"
                    + " where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'A'");

            ResultSet r = st.getResultSet();

            while (r.next()) {

                jLabelProvince.setText(r.getString(1));
                jLabelDistrict.setText(r.getString(2));

                if ((!("".equals(r.getString(3)))) && (r.getString(5) == null) && (r.getString(7) == null)
                        && (r.getString(9) == null) && (r.getString(11) == null)) {
                    java.util.Date dateWk1From = formatter.parse(r.getString(3));
                    java.util.Date dateWk1To = formatter.parse(r.getString(4));

                    jDateChooserWk1From.setDate(dateWk1From);
                    jDateChooserWk1To.setDate(dateWk1To);
                } else if ((!("".equals(r.getString(3)))) && (!("".equals(r.getString(5)))) && (r.getString(7) == null)
                        && (r.getString(9) == null) && (r.getString(11) == null)) {
                    java.util.Date dateWk1From = formatter.parse(r.getString(3));
                    java.util.Date dateWk1To = formatter.parse(r.getString(4));
                    java.util.Date dateWk2From = formatter.parse(r.getString(5));
                    java.util.Date dateWk2To = formatter.parse(r.getString(6));

                    jDateChooserWk1From.setDate(dateWk1From);
                    jDateChooserWk1To.setDate(dateWk1To);
                    jDateChooserWk2From.setDate(dateWk2From);
                    jDateChooserWk2To.setDate(dateWk2To);

                } else if ((!("".equals(r.getString(3)))) && (!("".equals(r.getString(5)))) && (!("".equals(r.getString(7))))
                        && (r.getString(9) == null) && (r.getString(11) == null)) {

                    java.util.Date dateWk1From = formatter.parse(r.getString(3));
                    java.util.Date dateWk1To = formatter.parse(r.getString(4));
                    java.util.Date dateWk2From = formatter.parse(r.getString(5));
                    java.util.Date dateWk2To = formatter.parse(r.getString(6));
                    java.util.Date dateWk3From = formatter.parse(r.getString(7));
                    java.util.Date dateWk3To = formatter.parse(r.getString(8));

                    jDateChooserWk1From.setDate(dateWk1From);
                    jDateChooserWk1To.setDate(dateWk1To);
                    jDateChooserWk2From.setDate(dateWk2From);
                    jDateChooserWk2To.setDate(dateWk2To);
                    jDateChooserWk3From.setDate(dateWk3From);
                    jDateChooserWk3To.setDate(dateWk3To);

                } else if ((!("".equals(r.getString(3)))) && (!("".equals(r.getString(5)))) && (!("".equals(r.getString(7))))
                        && (!("".equals(r.getString(9)))) && (r.getString(11) == null)) {
                    java.util.Date dateWk1From = formatter.parse(r.getString(3));
                    java.util.Date dateWk1To = formatter.parse(r.getString(4));
                    java.util.Date dateWk2From = formatter.parse(r.getString(5));
                    java.util.Date dateWk2To = formatter.parse(r.getString(6));
                    java.util.Date dateWk3From = formatter.parse(r.getString(7));
                    java.util.Date dateWk3To = formatter.parse(r.getString(8));
                    java.util.Date dateWk4From = formatter.parse(r.getString(9));
                    java.util.Date dateWk4To = formatter.parse(r.getString(10));

                    jDateChooserWk1From.setDate(dateWk1From);
                    jDateChooserWk1To.setDate(dateWk1To);
                    jDateChooserWk2From.setDate(dateWk2From);
                    jDateChooserWk2To.setDate(dateWk2To);
                    jDateChooserWk3From.setDate(dateWk3From);
                    jDateChooserWk3To.setDate(dateWk3To);
                    jDateChooserWk4From.setDate(dateWk4From);
                    jDateChooserWk4To.setDate(dateWk4To);

                } else {
                    java.util.Date dateWk1From = formatter.parse(r.getString(3));
                    java.util.Date dateWk1To = formatter.parse(r.getString(4));
                    java.util.Date dateWk2From = formatter.parse(r.getString(5));
                    java.util.Date dateWk2To = formatter.parse(r.getString(6));
                    java.util.Date dateWk3From = formatter.parse(r.getString(7));
                    java.util.Date dateWk3To = formatter.parse(r.getString(8));
                    java.util.Date dateWk4From = formatter.parse(r.getString(9));
                    java.util.Date dateWk4To = formatter.parse(r.getString(10));
                    java.util.Date dateWk5From = formatter.parse(r.getString(11));
                    java.util.Date dateWk5To = formatter.parse(r.getString(12));

                    jDateChooserWk1From.setDate(dateWk1From);
                    jDateChooserWk1To.setDate(dateWk1To);
                    jDateChooserWk2From.setDate(dateWk2From);
                    jDateChooserWk2To.setDate(dateWk2To);
                    jDateChooserWk3From.setDate(dateWk3From);
                    jDateChooserWk3To.setDate(dateWk3To);
                    jDateChooserWk4From.setDate(dateWk4From);
                    jDateChooserWk4To.setDate(dateWk4To);
                    jDateChooserWk5From.setDate(dateWk5From);
                    jDateChooserWk5To.setDate(dateWk5To);
                }
                origDate = jDateChooserWk1From.getDate();

            }

            if (jTableWk1Activities.getRowCount() > 0) {

                st1.executeQuery("SELECT distinct ACT_VER,ACT_VER + 1  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] "
                        + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'A'");

                ResultSet r1 = st1.getResultSet();

                while (r1.next()) {
                    docVer = r1.getString(1);
                    docNextVer = r1.getString(2);
                }
            } else if (jTableWk2Activities.getRowCount() > 0) {

                st1.executeQuery("SELECT distinct ACT_VER,ACT_VER + 1  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] "
                        + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'A'");

                ResultSet r1 = st1.getResultSet();

                while (r1.next()) {
                    docVer = r1.getString(1);
                    docNextVer = r1.getString(2);
                }
            } else if (jTableWk3Activities.getRowCount() > 0) {

                st1.executeQuery("SELECT distinct ACT_VER,ACT_VER + 1  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] "
                        + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'A'");

                ResultSet r1 = st1.getResultSet();

                while (r1.next()) {
                    docVer = r1.getString(1);
                    docNextVer = r1.getString(2);
                }
            } else if (jTableWk4Activities.getRowCount() > 0) {

                st1.executeQuery("SELECT distinct ACT_VER,ACT_VER + 1  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab] "
                        + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'A'");

                ResultSet r1 = st1.getResultSet();

                while (r1.next()) {
                    docVer = r1.getString(1);
                    docNextVer = r1.getString(2);
                }
            } else if (jTableWk5Activities.getRowCount() > 0) {

                st1.executeQuery("SELECT distinct ACT_VER,ACT_VER + 1  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab] "
                        + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'A'");

                ResultSet r1 = st1.getResultSet();

                while (r1.next()) {
                    docVer = r1.getString(1);
                    docNextVer = r1.getString(2);
                }
            }

            st1.executeQuery("SELECT COMMENTS FROM [ClaimsAppSysZvandiri].[dbo].[PlanActTab]"
                    + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'A'");

            ResultSet r1 = st1.getResultSet();

            while (r1.next()) {
                jTextAreaComments.setText(r1.getString(1));
            }
            jTabbedPaneMain.setSelectedIndex(5);
            jTextAreaComments.requestFocusInWindow();
            jTextAreaComments.setFocusable(true);
            jTextAreaComments.setLineWrap(true);
            jTextAreaComments.setWrapStyleWord(true);
            jTextAreaComments.setEditable(false);
        } catch (Exception e) {

            System.out.println(e);
        }
    }

    void fetchdataWk1() {
        try {

           Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] "
                    + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'A'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk1.insertRow(modelWk1.getRowCount(), new Object[]{r.getString(3), r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22)});

            }

        } catch (Exception e) {

        }
    }

    void fetchdataWk2() {
        try {

            DefaultTableModel modelWk2 = (DefaultTableModel) jTableWk2Activities.getModel();
                  Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
     
            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] "
                    + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'A'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk2.insertRow(modelWk2.getRowCount(), new Object[]{r.getString(3), r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22)});

            }

        } catch (Exception e) {

        }
    }

    void fetchdataWk3() {
        try {

           Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] "
                    + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'A'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk3.insertRow(modelWk3.getRowCount(), new Object[]{r.getString(3), r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22)});

            }

        } catch (Exception e) {

        }
    }

    void fetchdataWk4() {
        try {

           Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab] "
                    + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'A'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk4.insertRow(modelWk4.getRowCount(), new Object[]{r.getString(3), r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22)});

            }

        } catch (Exception e) {

        }
    }

    void fetchdataWk5() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab] "
                    + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'A'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk5.insertRow(modelWk5.getRowCount(), new Object[]{r.getString(3), r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22)});

            }

        } catch (Exception e) {

        }
    }

    void fetchdataMeetWk1() {
        try {

            DefaultTableModel modelMeetWk1 = (DefaultTableModel) jTableMeetReqWk1.getModel();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanMeetWk1Tab] "
                    + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'A' order by 6");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelMeetWk1.insertRow(modelMeetWk1.getRowCount(), new Object[]{r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12), r.getString(13),
                    r.getString(14), r.getString(15), r.getString(16)});

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdataMeetWk2() {
        try {

            DefaultTableModel modelMeetWk2 = (DefaultTableModel) jTableMeetReqWk2.getModel();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanMeetWk2Tab] "
                    + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'A' order by 6");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelMeetWk2.insertRow(modelMeetWk2.getRowCount(), new Object[]{r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12), r.getString(13),
                    r.getString(14), r.getString(15), r.getString(16)});

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdataMeetWk3() {
        try {

            DefaultTableModel modelMeetWk3 = (DefaultTableModel) jTableMeetReqWk3.getModel();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanMeetWk3Tab] "
                    + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'A' order by 6");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelMeetWk3.insertRow(modelMeetWk3.getRowCount(), new Object[]{r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12), r.getString(13),
                    r.getString(14), r.getString(15), r.getString(16)});

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdataMeetWk4() {
        try {

            DefaultTableModel modelMeetWk4 = (DefaultTableModel) jTableMeetReqWk4.getModel();
           Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanMeetWk4Tab] "
                    + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'A' order by 6");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelMeetWk4.insertRow(modelMeetWk4.getRowCount(), new Object[]{r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12), r.getString(13),
                    r.getString(14), r.getString(15), r.getString(16)});

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdataMeetWk5() {
        try {

            DefaultTableModel modelMeetWk5 = (DefaultTableModel) jTableMeetReqWk5.getModel();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanMeetWk5Tab] "
                    + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'A' order by 6");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelMeetWk5.insertRow(modelMeetWk5.getRowCount(), new Object[]{r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12), r.getString(13),
                    r.getString(14), r.getString(15), r.getString(16)});

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdataMeetWkC1() {
        try {

            DefaultTableModel modelMeetWk1 = (DefaultTableModel) jTableMeetReqWk1.getModel();
           Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanMeetWk1Tab] "
                    + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'C' order by 6");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelMeetWk1.insertRow(modelMeetWk1.getRowCount(), new Object[]{r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12), r.getString(13),
                    r.getString(14), r.getString(15), r.getString(16)});

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdataMeetWkC2() {
        try {

            DefaultTableModel modelMeetWk2 = (DefaultTableModel) jTableMeetReqWk2.getModel();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanMeetWk2Tab] "
                    + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'C' order by 6");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelMeetWk2.insertRow(modelMeetWk2.getRowCount(), new Object[]{r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12), r.getString(13),
                    r.getString(14), r.getString(15), r.getString(16)});

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdataMeetWkC3() {
        try {

            DefaultTableModel modelMeetWk3 = (DefaultTableModel) jTableMeetReqWk3.getModel();
           Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanMeetWk3Tab] "
                    + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'C' order by 6");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelMeetWk3.insertRow(modelMeetWk3.getRowCount(), new Object[]{r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12), r.getString(13),
                    r.getString(14), r.getString(15), r.getString(16)});

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdataMeetWkC4() {
        try {

            DefaultTableModel modelMeetWk4 = (DefaultTableModel) jTableMeetReqWk4.getModel();
           Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanMeetWk4Tab] "
                    + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'C' order by 6");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelMeetWk4.insertRow(modelMeetWk4.getRowCount(), new Object[]{r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12), r.getString(13),
                    r.getString(14), r.getString(15), r.getString(16)});

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdataMeetWkC5() {
        try {

            DefaultTableModel modelMeetWk5 = (DefaultTableModel) jTableMeetReqWk5.getModel();
         Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
       
            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanMeetWk5Tab] "
                    + "where PLAN_REF_NUM =" + jTextFieldRefNum.getText() + " and ACT_REC_STA = 'C' order by 6");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelMeetWk5.insertRow(modelMeetWk5.getRowCount(), new Object[]{r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12), r.getString(13),
                    r.getString(14), r.getString(15), r.getString(16)});

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void mailUpdate() {
        try {

            JOptionPane.showMessageDialog(this, "<html>Plan Ref No. <b>" + jLabelSerial.getText() + " " + jTextFieldRefNum.getText()
                    + "</b> successfully updated.</html>");

            sendMail();
            if ((sendToProvMgr.equals(jLabelLineLogNam.getText())) || ("Central Office".equals(jLabelProvince.getText())) || ("Harare Province".equals(jLabelProvince.getText()))) {
                SupNamSend = supNam;

            } else {
                SupNamSend = sendToProvMgr;

            }
            jDialogWaitingEmail.setVisible(false);
            JOptionPane.showMessageDialog(this, "An email has been sent to " + SupNamSend);
            new JFrameMeetingPlanEdit(jLabelEmp.getText()).setVisible(true);
            setVisible(false);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    void clearFields() {
        try {

            DefaultTableModel modelWk1 = (DefaultTableModel) this.jTableWk1Activities.getModel();
            DefaultTableModel modelWk2 = (DefaultTableModel) this.jTableWk2Activities.getModel();
            DefaultTableModel modelWk3 = (DefaultTableModel) this.jTableWk3Activities.getModel();
            DefaultTableModel modelWk4 = (DefaultTableModel) this.jTableWk4Activities.getModel();
            DefaultTableModel modelWk5 = (DefaultTableModel) this.jTableWk5Activities.getModel();
            DefaultTableModel modelMeetWk1 = (DefaultTableModel) jTableMeetReqWk1.getModel();
            DefaultTableModel modelMeetWk2 = (DefaultTableModel) jTableMeetReqWk2.getModel();
            DefaultTableModel modelMeetWk3 = (DefaultTableModel) jTableMeetReqWk3.getModel();
            DefaultTableModel modelMeetWk4 = (DefaultTableModel) jTableMeetReqWk4.getModel();
            DefaultTableModel modelMeetWk5 = (DefaultTableModel) jTableMeetReqWk5.getModel();

            jLabelProvince.setText("");
            jLabelDistrict.setText("");
            jDateChooserWk1From.setDate(null);
            jDateChooserWk1To.setDate(null);
            jDateChooserWk2From.setDate(null);
            jDateChooserWk2To.setDate(null);
            jDateChooserWk3From.setDate(null);
            jDateChooserWk3To.setDate(null);
            jDateChooserWk4From.setDate(null);
            jDateChooserWk4To.setDate(null);
            jDateChooserWk5From.setDate(null);
            jDateChooserWk5To.setDate(null);

            while (modelWk1.getRowCount() > 0) {
                modelWk1.removeRow(0);
            }

            while (modelWk2.getRowCount() > 0) {
                modelWk2.removeRow(0);
            }

            while (modelWk3.getRowCount() > 0) {
                modelWk3.removeRow(0);
            }

            while (modelWk4.getRowCount() > 0) {
                modelWk4.removeRow(0);
            }

            while (modelWk5.getRowCount() > 0) {
                modelWk5.removeRow(0);
            }

            while (modelMeetWk1.getRowCount() > 0) {
                modelMeetWk1.removeRow(0);
            }

            while (modelMeetWk2.getRowCount() > 0) {
                modelMeetWk2.removeRow(0);
            }

            while (modelMeetWk3.getRowCount() > 0) {
                modelMeetWk3.removeRow(0);
            }

            while (modelMeetWk4.getRowCount() > 0) {
                modelMeetWk4.removeRow(0);
            }

            while (modelMeetWk5.getRowCount() > 0) {
                modelMeetWk5.removeRow(0);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

   
void addItem() {
        int selectedOption = JOptionPane.showConfirmDialog(jDialogWk1,
                "Do want to add another  activity line?",
                "Choose",
                JOptionPane.YES_NO_OPTION);

        if ((selectedOption == JOptionPane.NO_OPTION)) {

//            wk2Site = "N";
//            perRespWk2 = "N";
            jDialogWk1.setVisible(false);
        }


        /*To Change*/
    }

      void searchName1() {
        try {
            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            
            Statement st = conn.createStatement();

            int itemCount = jComboBoxSearchResult1.getItemCount();

            for (int i = 0; i < itemCount; i++) {
                jComboBoxSearchResult1.removeItemAt(0);
            }
            jComboBoxSearchResult1.setSelectedIndex(-1);
            String searchNam = jTextFieldSearchNam1.getText();
            st.executeQuery("SELECT distinct [EMP_NAM]\n"
                    + "  FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] WHERE [EMP_NAM] like upper(\n" + "'%" + searchNam + "%')");
            ResultSet r = st.getResultSet();

            while (r.next()) {
                jComboBoxSearchResult1.addItem(r.getString(1));
                jButtonOk1.setVisible(true);
                //                jButtonSearch.setVisible(false);

            }

            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Connect to Database.Try again.", "Error Connection", JOptionPane.WARNING_MESSAGE);
            // System.exit(1);
        }
    }

    void sendMail() {

        String planSupNam, planSupEmail;

        if ((sendToProvMgr.equals(jLabelLineLogNam.getText())) || ("Central Office".equals(jLabelProvince.getText())) || ("Harare Province".equals(jLabelProvince.getText()))) {
            planSupNam = supNam;
            planSupEmail = supUsrMail;
        } else {
            planSupNam = sendToProvMgr;
            planSupEmail = provMgrMail;
        }

       final String username = c.FinMailUsrN;
        final String password = c.FinMailUsrP;
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            jDialogWaitingEmail.setVisible(true);
            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(mailUsrNam)); to edit
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(provMgrMail));
            message.setSubject("Plan Approval Request.Plan Reference No. " + jLabelSerial.getText() + " " + jTextFieldRefNum.getText());

            message.setContent("<html><body> Dear " + planSupNam + "<br><br>" + jLabelLineLogNam.getText()
                    + " has submitted an editted monthly plan for your approval. <br><br>Please check "
                    + "your inbox and action.<br><br> Kind Regards <br><br>"
                    + " Perdiem Management System </body></html>", "text/html;charset=utf-8");

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialogBudget = new javax.swing.JDialog();
        jPanel11 = new javax.swing.JPanel();
        jLabelHeader2 = new javax.swing.JLabel();
        jButtonOkFacility1 = new javax.swing.JButton();
        jButtonCancelBudget = new javax.swing.JButton();
        jLabelBudgetCode = new javax.swing.JLabel();
        jComboBudgetCode = new javax.swing.JComboBox<>();
        jDialogWaiting = new javax.swing.JDialog();
        jDialogFacility = new javax.swing.JDialog();
        jPanel10 = new javax.swing.JPanel();
        jLabelHeader1 = new javax.swing.JLabel();
        jButtonOkFacility = new javax.swing.JButton();
        jButtonCancelFacility = new javax.swing.JButton();
        jLabelProvinceFacility = new javax.swing.JLabel();
        jComboProvinceFacility = new javax.swing.JComboBox<>();
        jComboDistrictFacility = new javax.swing.JComboBox<>();
        jLabelDistrictFacility = new javax.swing.JLabel();
        jLabelFacility = new javax.swing.JLabel();
        jComboFacility = new javax.swing.JComboBox<>();
        jLabelDist = new javax.swing.JLabel();
        jTextDistDest = new javax.swing.JTextField();
        jDialogWaitingEmail = new javax.swing.JDialog();
        jDialogMeetingWk1 = new javax.swing.JDialog();
        jPanelMeetingWk1 = new javax.swing.JPanel();
        jPanelAmtWk1 = new javax.swing.JPanel();
        jLabelUnitCostWk1 = new javax.swing.JLabel();
        jTextUnitCostWk1 = new javax.swing.JTextField();
        jLabelNumPpleWk1 = new javax.swing.JLabel();
        jTextNumPpleWk1 = new javax.swing.JTextField();
        jTextNumDaysWk1 = new javax.swing.JTextField();
        jLabelNumDaysWk1 = new javax.swing.JLabel();
        jLabelBusFareWk1 = new javax.swing.JLabel();
        jTextBusFareWk1 = new javax.swing.JTextField();
        jLabelRequestionTotalWk1 = new javax.swing.JLabel();
        jLabel$Wk1 = new javax.swing.JLabel();
        jLabelTotalAmtWk1 = new javax.swing.JLabel();
        jLabelBudgetHeadWk1 = new javax.swing.JLabel();
        jLabelPersonRespWk1 = new javax.swing.JLabel();
        jTextPersonResWk1 = new javax.swing.JTextField();
        jComboMeetAllowanceWk1 = new javax.swing.JComboBox<>();
        jPanelDescWk1 = new javax.swing.JPanel();
        jLabelFromWk1 = new javax.swing.JLabel();
        jDateFromWk1 = new com.toedter.calendar.JDateChooser();
        jLabelToWk1 = new javax.swing.JLabel();
        jDateToWk1 = new com.toedter.calendar.JDateChooser();
        jLabelActivityProjectWk1 = new javax.swing.JLabel();
        jTextFieldBudgetWk1 = new javax.swing.JTextField();
        jLabelBudgetLineWk1 = new javax.swing.JLabel();
        jLabelMainPurposeWk1 = new javax.swing.JLabel();
        jScrollPane18 = new javax.swing.JScrollPane();
        jTextAreaDetailDescriptionWk1 = new javax.swing.JTextArea();
        jLabelDetailDescriptionWk1 = new javax.swing.JLabel();
        jLabelActivityDateWk1 = new javax.swing.JLabel();
        jComboActivityProjectWk1 = new javax.swing.JComboBox<>();
        jButtonAddActivityWk1 = new javax.swing.JButton();
        jButtonMeetExitActivityWk1 = new javax.swing.JButton();
        jButtonReturnActivityWk1 = new javax.swing.JButton();
        jLabeMeetlHeadWk1 = new javax.swing.JLabel();
        jComboMeetingWk1 = new javax.swing.JComboBox<>();
        jDialogMeetingWk2 = new javax.swing.JDialog();
        jPanelMeetingWk2 = new javax.swing.JPanel();
        jPanelAmtWk2 = new javax.swing.JPanel();
        jLabelUnitCostWk2 = new javax.swing.JLabel();
        jTextUnitCostWk2 = new javax.swing.JTextField();
        jLabelNumPpleWk2 = new javax.swing.JLabel();
        jTextNumPpleWk2 = new javax.swing.JTextField();
        jTextNumDaysWk2 = new javax.swing.JTextField();
        jLabelNumDaysWk2 = new javax.swing.JLabel();
        jLabelBusFareWk2 = new javax.swing.JLabel();
        jTextBusFareWk2 = new javax.swing.JTextField();
        jLabelRequestionTotalWk2 = new javax.swing.JLabel();
        jLabel$Wk2 = new javax.swing.JLabel();
        jLabelTotalAmtWk2 = new javax.swing.JLabel();
        jLabelBudgetHeadWk2 = new javax.swing.JLabel();
        jLabelPersonRespWk2 = new javax.swing.JLabel();
        jTextPersonResWk2 = new javax.swing.JTextField();
        jComboMeetAllowanceWk2 = new javax.swing.JComboBox<>();
        jPanelDescWk2 = new javax.swing.JPanel();
        jLabelFromWk2 = new javax.swing.JLabel();
        jDateFromWk2 = new com.toedter.calendar.JDateChooser();
        jLabelToWk2 = new javax.swing.JLabel();
        jDateToWk2 = new com.toedter.calendar.JDateChooser();
        jLabelActivityProjectWk2 = new javax.swing.JLabel();
        jTextFieldBudgetWk2 = new javax.swing.JTextField();
        jLabelBudgetLineWk2 = new javax.swing.JLabel();
        jLabelMainPurposeWk2 = new javax.swing.JLabel();
        jScrollPane19 = new javax.swing.JScrollPane();
        jTextAreaDetailDescriptionWk2 = new javax.swing.JTextArea();
        jLabelDetailDescriptionWk2 = new javax.swing.JLabel();
        jLabelActivityDateWk2 = new javax.swing.JLabel();
        jComboActivityProjectWk2 = new javax.swing.JComboBox<>();
        jButtonAddActivityWk2 = new javax.swing.JButton();
        jButtonMeetExitActivityWk2 = new javax.swing.JButton();
        jButtonReturnActivityWk2 = new javax.swing.JButton();
        jLabeMeetlHeadWk2 = new javax.swing.JLabel();
        jComboMeetingWk2 = new javax.swing.JComboBox<>();
        jDialogMeetingWk3 = new javax.swing.JDialog();
        jPanelMeetingWk3 = new javax.swing.JPanel();
        jPanelAmtWk3 = new javax.swing.JPanel();
        jLabelUnitCostWk3 = new javax.swing.JLabel();
        jTextUnitCostWk3 = new javax.swing.JTextField();
        jLabelNumPpleWk3 = new javax.swing.JLabel();
        jTextNumPpleWk3 = new javax.swing.JTextField();
        jTextNumDaysWk3 = new javax.swing.JTextField();
        jLabelNumDaysWk3 = new javax.swing.JLabel();
        jLabelBusFareWk3 = new javax.swing.JLabel();
        jTextBusFareWk3 = new javax.swing.JTextField();
        jLabelRequestionTotalWk3 = new javax.swing.JLabel();
        jLabel$Wk3 = new javax.swing.JLabel();
        jLabelTotalAmtWk3 = new javax.swing.JLabel();
        jLabelBudgetHeadWk3 = new javax.swing.JLabel();
        jLabelPersonRespWk3 = new javax.swing.JLabel();
        jTextPersonResWk3 = new javax.swing.JTextField();
        jComboMeetAllowanceWk3 = new javax.swing.JComboBox<>();
        jPanelDescWk3 = new javax.swing.JPanel();
        jLabelFromWk3 = new javax.swing.JLabel();
        jDateFromWk3 = new com.toedter.calendar.JDateChooser();
        jLabelToWk3 = new javax.swing.JLabel();
        jDateToWk3 = new com.toedter.calendar.JDateChooser();
        jLabelActivityProjectWk3 = new javax.swing.JLabel();
        jTextFieldBudgetWk3 = new javax.swing.JTextField();
        jLabelBudgetLineWk3 = new javax.swing.JLabel();
        jLabelMainPurposeWk3 = new javax.swing.JLabel();
        jScrollPane20 = new javax.swing.JScrollPane();
        jTextAreaDetailDescriptionWk3 = new javax.swing.JTextArea();
        jLabelDetailDescriptionWk3 = new javax.swing.JLabel();
        jLabelActivityDateWk3 = new javax.swing.JLabel();
        jComboActivityProjectWk3 = new javax.swing.JComboBox<>();
        jButtonAddActivityWk3 = new javax.swing.JButton();
        jButtonMeetExitActivityWk3 = new javax.swing.JButton();
        jButtonReturnActivityWk3 = new javax.swing.JButton();
        jLabeMeetlHeadWk3 = new javax.swing.JLabel();
        jComboMeetingWk3 = new javax.swing.JComboBox<>();
        jDialogMeetingWk4 = new javax.swing.JDialog();
        jPanelMeetingWk4 = new javax.swing.JPanel();
        jPanelAmtWk4 = new javax.swing.JPanel();
        jLabelUnitCostWk4 = new javax.swing.JLabel();
        jTextUnitCostWk4 = new javax.swing.JTextField();
        jLabelNumPpleWk4 = new javax.swing.JLabel();
        jTextNumPpleWk4 = new javax.swing.JTextField();
        jTextNumDaysWk4 = new javax.swing.JTextField();
        jLabelNumDaysWk4 = new javax.swing.JLabel();
        jLabelBusFareWk4 = new javax.swing.JLabel();
        jTextBusFareWk4 = new javax.swing.JTextField();
        jLabelRequestionTotalWk4 = new javax.swing.JLabel();
        jLabel$Wk4 = new javax.swing.JLabel();
        jLabelTotalAmtWk4 = new javax.swing.JLabel();
        jLabelBudgetHeadWk4 = new javax.swing.JLabel();
        jLabelPersonRespWk4 = new javax.swing.JLabel();
        jTextPersonResWk4 = new javax.swing.JTextField();
        jComboMeetAllowanceWk4 = new javax.swing.JComboBox<>();
        jPanelDescWk4 = new javax.swing.JPanel();
        jLabelFromWk4 = new javax.swing.JLabel();
        jDateFromWk4 = new com.toedter.calendar.JDateChooser();
        jLabelToWk4 = new javax.swing.JLabel();
        jDateToWk4 = new com.toedter.calendar.JDateChooser();
        jLabelActivityProjectWk4 = new javax.swing.JLabel();
        jTextFieldBudgetWk4 = new javax.swing.JTextField();
        jLabelBudgetLineWk4 = new javax.swing.JLabel();
        jLabelMainPurposeWk4 = new javax.swing.JLabel();
        jScrollPane21 = new javax.swing.JScrollPane();
        jTextAreaDetailDescriptionWk4 = new javax.swing.JTextArea();
        jLabelDetailDescriptionWk4 = new javax.swing.JLabel();
        jLabelActivityDateWk4 = new javax.swing.JLabel();
        jComboActivityProjectWk4 = new javax.swing.JComboBox<>();
        jButtonAddActivityWk4 = new javax.swing.JButton();
        jButtonMeetExitActivityWk4 = new javax.swing.JButton();
        jButtonReturnActivityWk4 = new javax.swing.JButton();
        jLabeMeetlHeadWk4 = new javax.swing.JLabel();
        jComboMeetingWk4 = new javax.swing.JComboBox<>();
        jDialogMeetingWk5 = new javax.swing.JDialog();
        jPanelMeetingWk5 = new javax.swing.JPanel();
        jPanelAmtWk5 = new javax.swing.JPanel();
        jLabelUnitCostWk5 = new javax.swing.JLabel();
        jTextUnitCostWk5 = new javax.swing.JTextField();
        jLabelNumPpleWk5 = new javax.swing.JLabel();
        jTextNumPpleWk5 = new javax.swing.JTextField();
        jTextNumDaysWk5 = new javax.swing.JTextField();
        jLabelNumDaysWk5 = new javax.swing.JLabel();
        jLabelBusFareWk5 = new javax.swing.JLabel();
        jTextBusFareWk5 = new javax.swing.JTextField();
        jLabelRequestionTotalWk5 = new javax.swing.JLabel();
        jLabel$Wk5 = new javax.swing.JLabel();
        jLabelTotalAmtWk5 = new javax.swing.JLabel();
        jLabelBudgetHeadWk5 = new javax.swing.JLabel();
        jLabelPersonRespWk5 = new javax.swing.JLabel();
        jTextPersonResWk5 = new javax.swing.JTextField();
        jComboMeetAllowanceWk5 = new javax.swing.JComboBox<>();
        jPanelDescWk5 = new javax.swing.JPanel();
        jLabelFromWk5 = new javax.swing.JLabel();
        jDateFromWk5 = new com.toedter.calendar.JDateChooser();
        jLabelToWk5 = new javax.swing.JLabel();
        jDateToWk5 = new com.toedter.calendar.JDateChooser();
        jLabelActivityProjectWk5 = new javax.swing.JLabel();
        jTextFieldBudgetWk5 = new javax.swing.JTextField();
        jLabelBudgetLineWk5 = new javax.swing.JLabel();
        jLabelMainPurposeWk5 = new javax.swing.JLabel();
        jScrollPane22 = new javax.swing.JScrollPane();
        jTextAreaDetailDescriptionWk5 = new javax.swing.JTextArea();
        jLabelDetailDescriptionWk5 = new javax.swing.JLabel();
        jLabelActivityDateWk5 = new javax.swing.JLabel();
        jComboActivityProjectWk5 = new javax.swing.JComboBox<>();
        jButtonAddActivityWk5 = new javax.swing.JButton();
        jButtonMeetExitActivityWk5 = new javax.swing.JButton();
        jButtonReturnActivityWk5 = new javax.swing.JButton();
        jLabeMeetlHeadWk5 = new javax.swing.JLabel();
        jComboMeetingWk5 = new javax.swing.JComboBox<>();
        jDialogSearchName1 = new javax.swing.JDialog();
        jPanelSearchID = new javax.swing.JPanel();
        jTextFieldSearchNam1 = new javax.swing.JTextField();
        jButtonSearch1 = new javax.swing.JButton();
        jComboBoxSearchResult1 = new javax.swing.JComboBox<>();
        jButtonOk1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jDialogWk1 = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabelDialogWk1ActivityDate = new javax.swing.JLabel();
        jDateChooserDialogWk1ActivityDate = new com.toedter.calendar.JDateChooser();
        jLabelDialogWk1Site = new javax.swing.JLabel();
        jTextFieldDialogWk1Site = new javax.swing.JTextField();
        jLabelWk1DialogJustification = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldWk1DialogActivityDesc = new javax.swing.JTextField();
        jLabelWk1DialogActivityDesc = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaWk1DialogJustification = new javax.swing.JTextArea();
        jSeparator3 = new javax.swing.JSeparator();
        jComboBoxDialogWk1PerDiem = new javax.swing.JComboBox<>();
        jLabelDialogPerDiem = new javax.swing.JLabel();
        jLabelWk1DialogStaffName = new javax.swing.JLabel();
        jLabelWk1DialogStaffName1 = new javax.swing.JLabel();
        jLabelWk1DialogStaffName2 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jTextFieldWk1DialogStaffName4 = new javax.swing.JTextField();
        jTextFieldWk1DialogStaffName1 = new javax.swing.JTextField();
        jTextFieldWk1DialogStaffName2 = new javax.swing.JTextField();
        jTextFieldWk1DialogStaffName3 = new javax.swing.JTextField();
        jButtonDialogWk1Reset = new javax.swing.JButton();
        jButtonDialogWk1Add = new javax.swing.JButton();
        jCheckBoxDialogWk1BrkFast = new javax.swing.JCheckBox();
        jCheckBoxDialogWk1Lunch = new javax.swing.JCheckBox();
        jCheckBoxDialogWk1Dinner = new javax.swing.JCheckBox();
        jCheckBoxDialogWk1AccProved = new javax.swing.JCheckBox();
        jLabelRemain = new javax.swing.JLabel();
        jCheckBoxDialogWk1Misc = new javax.swing.JCheckBox();
        jCheckBoxDialogWk1Inc = new javax.swing.JCheckBox();
        jLabelWk1Misc = new javax.swing.JLabel();
        jTextFieldWk1Misc = new javax.swing.JTextField();
        jButtonDialogWk1Close = new javax.swing.JButton();
        jLabelWk1MiscAmt = new javax.swing.JLabel();
        jTextFieldWk1MiscAmt = new javax.swing.JTextField();
        jLabelWk1Name4Del = new javax.swing.JLabel();
        jLabelWk1Name1Del = new javax.swing.JLabel();
        jLabelWk1Name2Del = new javax.swing.JLabel();
        jLabelWk1Name3Del = new javax.swing.JLabel();
        jLabelProjectName = new javax.swing.JLabel();
        jComboProjectName = new javax.swing.JComboBox<>();
        jLabelProjectTask = new javax.swing.JLabel();
        jComboProjectTask = new javax.swing.JComboBox<>();
        jCheckBoxDialogWk1AccUnProved = new javax.swing.JCheckBox();
        jTabbedPaneMain = new javax.swing.JTabbedPane();
        jPanelWkOne = new javax.swing.JPanel();
        jLabelWkDuration = new javax.swing.JLabel();
        jDateChooserWk1From = new com.toedter.calendar.JDateChooser();
        jLabelWk1From = new javax.swing.JLabel();
        jLabelWk1To = new javax.swing.JLabel();
        jDateChooserWk1To = new com.toedter.calendar.JDateChooser();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabelLogo1 = new javax.swing.JLabel();
        jButtonWk1DelActivity = new javax.swing.JButton();
        jLabelProvince1 = new javax.swing.JLabel();
        jLabelOffice1 = new javax.swing.JLabel();
        jLabelPlanRefNo = new javax.swing.JLabel();
        jLabelProvince = new javax.swing.JLabel();
        jLabelDistrict = new javax.swing.JLabel();
        jTextFieldRefNum = new javax.swing.JTextField();
        jLabelSerial = new javax.swing.JLabel();
        jLabelGenLogNam1 = new javax.swing.JLabel();
        jLabelLineLogNam = new javax.swing.JLabel();
        jLabelLineTime = new javax.swing.JLabel();
        jLabelLineDate = new javax.swing.JLabel();
        jLabelEmp = new javax.swing.JLabel();
        jLabelHeaderGen6 = new javax.swing.JLabel();
        jLabelPostAppMod1 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTableMeetReqWk1 = new javax.swing.JTable();
        jButtonWk1DelMeetActivity = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableWk1Activities = new javax.swing.JTable();
        jButtonWk1AddActivity = new javax.swing.JButton();
        jPanelWkTwo = new javax.swing.JPanel();
        jLabelWkDuration1 = new javax.swing.JLabel();
        jDateChooserWk2From = new com.toedter.calendar.JDateChooser();
        jLabelWk1From1 = new javax.swing.JLabel();
        jLabelWk1To1 = new javax.swing.JLabel();
        jDateChooserWk2To = new com.toedter.calendar.JDateChooser();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jLabelLogo2 = new javax.swing.JLabel();
        jButtonWk2DelActivity = new javax.swing.JButton();
        jLabelLineTime1 = new javax.swing.JLabel();
        jLabelLineDate1 = new javax.swing.JLabel();
        jLabelGenLogNam3 = new javax.swing.JLabel();
        jLabelLineLogNam1 = new javax.swing.JLabel();
        jLabelHeaderGen7 = new javax.swing.JLabel();
        jLabelPostAppMod2 = new javax.swing.JLabel();
        jButtonWk2DelMeetActivity = new javax.swing.JButton();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTableMeetReqWk2 = new javax.swing.JTable();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTableWk2Activities = new javax.swing.JTable();
        jButtonWk2AddActivity = new javax.swing.JButton();
        jPanelWkThree = new javax.swing.JPanel();
        jLabelWkDuration2 = new javax.swing.JLabel();
        jDateChooserWk3From = new com.toedter.calendar.JDateChooser();
        jLabelWk3From = new javax.swing.JLabel();
        jLabelWk3To = new javax.swing.JLabel();
        jDateChooserWk3To = new com.toedter.calendar.JDateChooser();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jLabelLogo3 = new javax.swing.JLabel();
        jButtonWk3DelActivity = new javax.swing.JButton();
        jLabelLineDate2 = new javax.swing.JLabel();
        jLabelLineTime2 = new javax.swing.JLabel();
        jLabelLineLogNam2 = new javax.swing.JLabel();
        jLabelGenLogNam5 = new javax.swing.JLabel();
        jLabelHeaderGen4 = new javax.swing.JLabel();
        jLabelPostAppMod3 = new javax.swing.JLabel();
        jButtonWk3DelMeetActivity = new javax.swing.JButton();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTableMeetReqWk3 = new javax.swing.JTable();
        jScrollPane23 = new javax.swing.JScrollPane();
        jTableWk3Activities = new javax.swing.JTable();
        jButtonWk3AddActivity = new javax.swing.JButton();
        jPanelWkFour = new javax.swing.JPanel();
        jLabelWkDuration4 = new javax.swing.JLabel();
        jDateChooserWk4From = new com.toedter.calendar.JDateChooser();
        jLabelWk4From = new javax.swing.JLabel();
        jLabelWk4To = new javax.swing.JLabel();
        jDateChooserWk4To = new com.toedter.calendar.JDateChooser();
        jSeparator41 = new javax.swing.JSeparator();
        jSeparator42 = new javax.swing.JSeparator();
        jLabelLogo5 = new javax.swing.JLabel();
        jButtonWk4DelActivity = new javax.swing.JButton();
        jLabelGenLogNam7 = new javax.swing.JLabel();
        jLabelLineLogNam4 = new javax.swing.JLabel();
        jLabelLineTime4 = new javax.swing.JLabel();
        jLabelLineDate4 = new javax.swing.JLabel();
        jLabelHeaderGen9 = new javax.swing.JLabel();
        jLabelPostAppMod4 = new javax.swing.JLabel();
        jButtonWk4DelMeetActivity = new javax.swing.JButton();
        jScrollPane16 = new javax.swing.JScrollPane();
        jTableMeetReqWk4 = new javax.swing.JTable();
        jScrollPane24 = new javax.swing.JScrollPane();
        jTableWk4Activities = new javax.swing.JTable();
        jButtonWk4AddActivity = new javax.swing.JButton();
        jPanelWkFive = new javax.swing.JPanel();
        jLabelWkDuration5 = new javax.swing.JLabel();
        jDateChooserWk5From = new com.toedter.calendar.JDateChooser();
        jLabelWk5From = new javax.swing.JLabel();
        jLabelWk5To = new javax.swing.JLabel();
        jDateChooserWk5To = new com.toedter.calendar.JDateChooser();
        jSeparator43 = new javax.swing.JSeparator();
        jSeparator44 = new javax.swing.JSeparator();
        jLabelLogo6 = new javax.swing.JLabel();
        jButtonWk5DelActivity = new javax.swing.JButton();
        jLabelGenLogNam8 = new javax.swing.JLabel();
        jLabelLineLogNam5 = new javax.swing.JLabel();
        jLabelLineTime5 = new javax.swing.JLabel();
        jLabelLineDate5 = new javax.swing.JLabel();
        jLabelHeaderGen10 = new javax.swing.JLabel();
        jLabelPostAppMod5 = new javax.swing.JLabel();
        jButtonWk5DelMeetActivity = new javax.swing.JButton();
        jScrollPane17 = new javax.swing.JScrollPane();
        jTableMeetReqWk5 = new javax.swing.JTable();
        jScrollPane25 = new javax.swing.JScrollPane();
        jTableWk5Activities = new javax.swing.JTable();
        jButtonWk5AddActivity = new javax.swing.JButton();
        jPanelWkComments = new javax.swing.JPanel();
        jSeparator35 = new javax.swing.JSeparator();
        jLabelLogo4 = new javax.swing.JLabel();
        jLabelLineLogNam3 = new javax.swing.JLabel();
        jLabelGenLogNam6 = new javax.swing.JLabel();
        jLabelLineDate3 = new javax.swing.JLabel();
        jLabelLineTime3 = new javax.swing.JLabel();
        jLabelHeaderGen8 = new javax.swing.JLabel();
        jLabelCommentsHeading = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextAreaComments = new javax.swing.JTextArea();
        jSeparator36 = new javax.swing.JSeparator();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuNew = new javax.swing.JMenu();
        jMenuItemRequest = new javax.swing.JMenuItem();
        jSeparator27 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcquittal = new javax.swing.JMenuItem();
        jSeparator14 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSpecialAcquittal = new javax.swing.JMenuItem();
        jSeparator19 = new javax.swing.JPopupMenu.Separator();
        jMenuItemRequestMOH = new javax.swing.JMenuItem();
        jSeparator28 = new javax.swing.JPopupMenu.Separator();
        jMenuItemMeetAcquittal = new javax.swing.JMenuItem();
        jSeparator39 = new javax.swing.JPopupMenu.Separator();
        jMenuItem3WkPlan = new javax.swing.JMenuItem();
        jSeparator40 = new javax.swing.JPopupMenu.Separator();
        jMenuEdit = new javax.swing.JMenu();
        jMenuReqEdit = new javax.swing.JMenuItem();
        jSeparator29 = new javax.swing.JPopupMenu.Separator();
        jMenuAcqEdit = new javax.swing.JMenuItem();
        jSeparator30 = new javax.swing.JPopupMenu.Separator();
        jMenuMonPlanEdit = new javax.swing.JMenuItem();
        jSeparator47 = new javax.swing.JPopupMenu.Separator();
        jMenuItemClose1 = new javax.swing.JMenuItem();
        jSeparator55 = new javax.swing.JPopupMenu.Separator();
        jMenuItemClose = new javax.swing.JMenuItem();
        jSeparator48 = new javax.swing.JPopupMenu.Separator();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuRequest = new javax.swing.JMenu();
        jMenuItemSupApp = new javax.swing.JMenuItem();
        jSeparator51 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAccMgrRev = new javax.swing.JMenuItem();
        jSeparator52 = new javax.swing.JPopupMenu.Separator();
        jMenuItemHeadApp = new javax.swing.JMenuItem();
        jSeparator53 = new javax.swing.JPopupMenu.Separator();
        jMenuPlanApproval = new javax.swing.JMenuItem();
        jSeparator54 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSchGen = new javax.swing.JMenuItem();
        jSeparator13 = new javax.swing.JPopupMenu.Separator();
        jMenuItemView = new javax.swing.JMenuItem();
        jMenuAcquittal = new javax.swing.JMenu();
        jMenuItemAcqSupApp = new javax.swing.JMenuItem();
        jSeparator22 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqAccApp = new javax.swing.JMenuItem();
        jSeparator23 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqHeadApp = new javax.swing.JMenuItem();
        jSeparator24 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqFinApp = new javax.swing.JMenuItem();
        jSeparator25 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqSchGen = new javax.swing.JMenuItem();
        jSeparator26 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqView = new javax.swing.JMenuItem();
        jMenuMonthlyPlan = new javax.swing.JMenu();
        jMenuItemProvMgrApproval = new javax.swing.JMenuItem();
        jSeparator49 = new javax.swing.JPopupMenu.Separator();
        jMenuItemReqGenLst = new javax.swing.JMenuItem();
        jSeparator34 = new javax.swing.JPopupMenu.Separator();
        jMenuItemMeetGenLst = new javax.swing.JMenuItem();
        jSeparator50 = new javax.swing.JPopupMenu.Separator();
        jMenuItemPlanView = new javax.swing.JMenuItem();
        jMenuReports = new javax.swing.JMenu();
        jMenuItemReqSubmitted = new javax.swing.JMenuItem();
        jSeparator31 = new javax.swing.JPopupMenu.Separator();
        jMenuAgeAlluser = new javax.swing.JMenuItem();
        jSeparator32 = new javax.swing.JPopupMenu.Separator();
        jMenuAgeByUser = new javax.swing.JMenuItem();
        jSeparator33 = new javax.swing.JPopupMenu.Separator();
        jMenuProcessTimeline = new javax.swing.JMenuItem();

        jDialogBudget.setAlwaysOnTop(true);
        jDialogBudget.setLocation(new java.awt.Point(450, 300));
        jDialogBudget.setMinimumSize(new java.awt.Dimension(600, 300));
        jDialogBudget.setResizable(false);

        jPanel11.setBackground(new java.awt.Color(204, 153, 14));
        jPanel11.setLayout(null);

        jLabelHeader2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelHeader2.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeader2.setText("ACTIVITY BUDGET LINE");
        jPanel11.add(jLabelHeader2);
        jLabelHeader2.setBounds(170, 10, 230, 40);

        jButtonOkFacility1.setText("Ok ");
        jButtonOkFacility1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkFacility1ActionPerformed(evt);
            }
        });
        jPanel11.add(jButtonOkFacility1);
        jButtonOkFacility1.setBounds(180, 200, 80, 21);

        jButtonCancelBudget.setText("Cancel");
        jButtonCancelBudget.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelBudgetActionPerformed(evt);
            }
        });
        jPanel11.add(jButtonCancelBudget);
        jButtonCancelBudget.setBounds(300, 200, 80, 21);

        jLabelBudgetCode.setText("Budget Code");
        jPanel11.add(jLabelBudgetCode);
        jLabelBudgetCode.setBounds(5, 80, 80, 30);

        jComboBudgetCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jComboBudgetCodeFocusGained(evt);
            }
        });
        jComboBudgetCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jComboBudgetCodeMouseEntered(evt);
            }
        });
        jPanel11.add(jComboBudgetCode);
        jComboBudgetCode.setBounds(5, 120, 590, 30);

        javax.swing.GroupLayout jDialogBudgetLayout = new javax.swing.GroupLayout(jDialogBudget.getContentPane());
        jDialogBudget.getContentPane().setLayout(jDialogBudgetLayout);
        jDialogBudgetLayout.setHorizontalGroup(
            jDialogBudgetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
        );
        jDialogBudgetLayout.setVerticalGroup(
            jDialogBudgetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        jDialogWaiting.setTitle("         Processing. Please Wait !!!!!");
        jDialogWaiting.setAlwaysOnTop(true);
        jDialogWaiting.setBackground(new java.awt.Color(51, 255, 51));
        jDialogWaiting.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jDialogWaiting.setIconImages(null);
        jDialogWaiting.setLocation(new java.awt.Point(500, 150));
        jDialogWaiting.setMinimumSize(new java.awt.Dimension(300, 50));
        jDialogWaiting.setResizable(false);

        javax.swing.GroupLayout jDialogWaitingLayout = new javax.swing.GroupLayout(jDialogWaiting.getContentPane());
        jDialogWaiting.getContentPane().setLayout(jDialogWaitingLayout);
        jDialogWaitingLayout.setHorizontalGroup(
            jDialogWaitingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        jDialogWaitingLayout.setVerticalGroup(
            jDialogWaitingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        jDialogFacility.setAlwaysOnTop(true);
        jDialogFacility.setLocation(new java.awt.Point(450, 300));
        jDialogFacility.setMinimumSize(new java.awt.Dimension(600, 300));
        jDialogFacility.setResizable(false);

        jPanel10.setBackground(new java.awt.Color(204, 153, 14));
        jPanel10.setLayout(null);

        jLabelHeader1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelHeader1.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeader1.setText("FACILITY TO VISIT");
        jPanel10.add(jLabelHeader1);
        jLabelHeader1.setBounds(170, 10, 230, 40);

        jButtonOkFacility.setText("Ok ");
        jButtonOkFacility.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkFacilityActionPerformed(evt);
            }
        });
        jPanel10.add(jButtonOkFacility);
        jButtonOkFacility.setBounds(180, 220, 80, 21);

        jButtonCancelFacility.setText("Cancel");
        jButtonCancelFacility.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelFacilityActionPerformed(evt);
            }
        });
        jPanel10.add(jButtonCancelFacility);
        jButtonCancelFacility.setBounds(300, 220, 80, 21);

        jLabelProvinceFacility.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelProvinceFacility.setText("Province");
        jPanel10.add(jLabelProvinceFacility);
        jLabelProvinceFacility.setBounds(20, 70, 70, 30);

        jComboProvinceFacility.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jComboProvinceFacilityMouseEntered(evt);
            }
        });
        jComboProvinceFacility.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboProvinceFacilityActionPerformed(evt);
            }
        });
        jComboProvinceFacility.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboProvinceFacilityKeyPressed(evt);
            }
        });
        jPanel10.add(jComboProvinceFacility);
        jComboProvinceFacility.setBounds(150, 70, 230, 30);

        jComboDistrictFacility.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jComboDistrictFacilityMouseEntered(evt);
            }
        });
        jComboDistrictFacility.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboDistrictFacilityActionPerformed(evt);
            }
        });
        jPanel10.add(jComboDistrictFacility);
        jComboDistrictFacility.setBounds(150, 110, 230, 30);

        jLabelDistrictFacility.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDistrictFacility.setText("District");
        jPanel10.add(jLabelDistrictFacility);
        jLabelDistrictFacility.setBounds(20, 110, 70, 30);

        jLabelFacility.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelFacility.setText("Facility");
        jPanel10.add(jLabelFacility);
        jLabelFacility.setBounds(20, 150, 70, 30);

        jComboFacility.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jComboFacilityFocusGained(evt);
            }
        });
        jComboFacility.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jComboFacilityMouseEntered(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jComboFacilityMouseReleased(evt);
            }
        });
        jComboFacility.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboFacilityActionPerformed(evt);
            }
        });
        jPanel10.add(jComboFacility);
        jComboFacility.setBounds(150, 150, 320, 30);

        jLabelDist.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDist.setText("Distance ");
        jPanel10.add(jLabelDist);
        jLabelDist.setBounds(410, 110, 60, 30);
        jPanel10.add(jTextDistDest);
        jTextDistDest.setBounds(490, 110, 50, 30);

        javax.swing.GroupLayout jDialogFacilityLayout = new javax.swing.GroupLayout(jDialogFacility.getContentPane());
        jDialogFacility.getContentPane().setLayout(jDialogFacilityLayout);
        jDialogFacilityLayout.setHorizontalGroup(
            jDialogFacilityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
        );
        jDialogFacilityLayout.setVerticalGroup(
            jDialogFacilityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        jDialogWaitingEmail.setTitle("                    Connecting to server and sending email.  Please Wait.");
        jDialogWaitingEmail.setAlwaysOnTop(true);
        jDialogWaitingEmail.setBackground(new java.awt.Color(51, 255, 51));
        jDialogWaitingEmail.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jDialogWaitingEmail.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jDialogWaitingEmail.setForeground(new java.awt.Color(255, 0, 0));
        jDialogWaitingEmail.setIconImages(null);
        jDialogWaitingEmail.setLocation(new java.awt.Point(500, 150));
        jDialogWaitingEmail.setMinimumSize(new java.awt.Dimension(500, 50));
        jDialogWaitingEmail.setResizable(false);

        javax.swing.GroupLayout jDialogWaitingEmailLayout = new javax.swing.GroupLayout(jDialogWaitingEmail.getContentPane());
        jDialogWaitingEmail.getContentPane().setLayout(jDialogWaitingEmailLayout);
        jDialogWaitingEmailLayout.setHorizontalGroup(
            jDialogWaitingEmailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 453, Short.MAX_VALUE)
        );
        jDialogWaitingEmailLayout.setVerticalGroup(
            jDialogWaitingEmailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        jDialogMeetingWk1.setTitle("MEETING PLANNER WEEK 1");
        jDialogMeetingWk1.setLocation(new java.awt.Point(300, 100));
        jDialogMeetingWk1.setMinimumSize(new java.awt.Dimension(850, 650));
        jDialogMeetingWk1.getContentPane().setLayout(null);

        jPanelMeetingWk1.setBackground(new java.awt.Color(255, 255, 255));
        jPanelMeetingWk1.setMinimumSize(new java.awt.Dimension(850, 650));
        jPanelMeetingWk1.setPreferredSize(new java.awt.Dimension(825, 650));

        jPanelAmtWk1.setBackground(new java.awt.Color(255, 255, 255));
        jPanelAmtWk1.setLayout(null);

        jLabelUnitCostWk1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelUnitCostWk1.setText("Unit Cost");
        jLabelUnitCostWk1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelAmtWk1.add(jLabelUnitCostWk1);
        jLabelUnitCostWk1.setBounds(20, 200, 110, 30);

        jTextUnitCostWk1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextUnitCostWk1ActionPerformed(evt);
            }
        });
        jTextUnitCostWk1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextUnitCostWk1KeyReleased(evt);
            }
        });
        jPanelAmtWk1.add(jTextUnitCostWk1);
        jTextUnitCostWk1.setBounds(170, 200, 50, 30);

        jLabelNumPpleWk1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelNumPpleWk1.setText("No. Of People");
        jLabelNumPpleWk1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelAmtWk1.add(jLabelNumPpleWk1);
        jLabelNumPpleWk1.setBounds(20, 240, 110, 30);

        jTextNumPpleWk1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextNumPpleWk1ActionPerformed(evt);
            }
        });
        jTextNumPpleWk1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextNumPpleWk1KeyReleased(evt);
            }
        });
        jPanelAmtWk1.add(jTextNumPpleWk1);
        jTextNumPpleWk1.setBounds(170, 240, 50, 30);
        jPanelAmtWk1.add(jTextNumDaysWk1);
        jTextNumDaysWk1.setBounds(170, 280, 50, 30);

        jLabelNumDaysWk1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelNumDaysWk1.setText("No. Of Days");
        jLabelNumDaysWk1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelAmtWk1.add(jLabelNumDaysWk1);
        jLabelNumDaysWk1.setBounds(20, 280, 110, 30);

        jLabelBusFareWk1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelBusFareWk1.setText("Total Busfare");
        jLabelBusFareWk1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelAmtWk1.add(jLabelBusFareWk1);
        jLabelBusFareWk1.setBounds(20, 320, 110, 30);

        jTextBusFareWk1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextBusFareWk1ActionPerformed(evt);
            }
        });
        jTextBusFareWk1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextBusFareWk1KeyReleased(evt);
            }
        });
        jPanelAmtWk1.add(jTextBusFareWk1);
        jTextBusFareWk1.setBounds(170, 320, 50, 30);

        jLabelRequestionTotalWk1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelRequestionTotalWk1.setText("Activity Line Total");
        jPanelAmtWk1.add(jLabelRequestionTotalWk1);
        jLabelRequestionTotalWk1.setBounds(20, 390, 130, 30);

        jLabel$Wk1.setText("$");
        jPanelAmtWk1.add(jLabel$Wk1);
        jLabel$Wk1.setBounds(150, 390, 20, 30);

        jLabelTotalAmtWk1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelAmtWk1.add(jLabelTotalAmtWk1);
        jLabelTotalAmtWk1.setBounds(170, 390, 80, 30);

        jLabelBudgetHeadWk1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelBudgetHeadWk1.setText("Budget Details");
        jPanelAmtWk1.add(jLabelBudgetHeadWk1);
        jLabelBudgetHeadWk1.setBounds(70, 10, 170, 30);

        jLabelPersonRespWk1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelPersonRespWk1.setText("Person Responsible");
        jLabelPersonRespWk1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelAmtWk1.add(jLabelPersonRespWk1);
        jLabelPersonRespWk1.setBounds(20, 60, 150, 30);

        jTextPersonResWk1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jTextPersonResWk1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextPersonResWk1MouseClicked(evt);
            }
        });
        jTextPersonResWk1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextPersonResWk1ActionPerformed(evt);
            }
        });
        jTextPersonResWk1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextPersonResWk1KeyReleased(evt);
            }
        });
        jPanelAmtWk1.add(jTextPersonResWk1);
        jTextPersonResWk1.setBounds(20, 100, 250, 30);

        jComboMeetAllowanceWk1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboMeetAllowanceWk1MouseClicked(evt);
            }
        });
        jComboMeetAllowanceWk1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboMeetAllowanceWk1ActionPerformed(evt);
            }
        });
        jPanelAmtWk1.add(jComboMeetAllowanceWk1);
        jComboMeetAllowanceWk1.setBounds(20, 150, 250, 30);

        jPanelDescWk1.setBackground(new java.awt.Color(204, 204, 0));
        jPanelDescWk1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelDescWk1.setLayout(null);

        jLabelFromWk1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelFromWk1.setText("From");
        jPanelDescWk1.add(jLabelFromWk1);
        jLabelFromWk1.setBounds(30, 130, 40, 30);

        jDateFromWk1.setDateFormatString("yyyy-MM-dd");
        jPanelDescWk1.add(jDateFromWk1);
        jDateFromWk1.setBounds(70, 130, 125, 30);

        jLabelToWk1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelToWk1.setText("To");
        jPanelDescWk1.add(jLabelToWk1);
        jLabelToWk1.setBounds(200, 130, 20, 30);

        jDateToWk1.setDateFormatString("yyyy-MM-dd");
        jDateToWk1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jDateToWk1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jDateToWk1MouseEntered(evt);
            }
        });
        jDateToWk1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jDateToWk1KeyReleased(evt);
            }
        });
        jPanelDescWk1.add(jDateToWk1);
        jDateToWk1.setBounds(220, 130, 125, 30);

        jLabelActivityProjectWk1.setText("Project");
        jLabelActivityProjectWk1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelDescWk1.add(jLabelActivityProjectWk1);
        jLabelActivityProjectWk1.setBounds(30, 55, 90, 30);

        jTextFieldBudgetWk1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldBudgetWk1MouseClicked(evt);
            }
        });
        jTextFieldBudgetWk1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldBudgetWk1KeyTyped(evt);
            }
        });
        jPanelDescWk1.add(jTextFieldBudgetWk1);
        jTextFieldBudgetWk1.setBounds(30, 210, 310, 30);

        jLabelBudgetLineWk1.setText("Budget Line");
        jLabelBudgetLineWk1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelDescWk1.add(jLabelBudgetLineWk1);
        jLabelBudgetLineWk1.setBounds(30, 170, 90, 30);

        jLabelMainPurposeWk1.setText("Activity Main Purpose");
        jLabelMainPurposeWk1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelDescWk1.add(jLabelMainPurposeWk1);
        jLabelMainPurposeWk1.setBounds(30, 250, 150, 30);

        jTextAreaDetailDescriptionWk1.setColumns(20);
        jTextAreaDetailDescriptionWk1.setRows(5);
        jScrollPane18.setViewportView(jTextAreaDetailDescriptionWk1);

        jPanelDescWk1.add(jScrollPane18);
        jScrollPane18.setBounds(30, 360, 310, 100);

        jLabelDetailDescriptionWk1.setText("Activity Detailed Description");
        jLabelDetailDescriptionWk1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelDescWk1.add(jLabelDetailDescriptionWk1);
        jLabelDetailDescriptionWk1.setBounds(30, 320, 170, 30);

        jLabelActivityDateWk1.setText("Activity Date");
        jLabelActivityDateWk1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelDescWk1.add(jLabelActivityDateWk1);
        jLabelActivityDateWk1.setBounds(30, 95, 90, 30);

        jPanelDescWk1.add(jComboActivityProjectWk1);
        jComboActivityProjectWk1.setBounds(140, 55, 200, 30);

        jButtonAddActivityWk1.setText("Add Budget Line");
        jButtonAddActivityWk1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActivityWk1ActionPerformed(evt);
            }
        });
        jPanelDescWk1.add(jButtonAddActivityWk1);
        jButtonAddActivityWk1.setBounds(30, 500, 130, 30);

        jButtonMeetExitActivityWk1.setText("Exit");
        jButtonMeetExitActivityWk1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMeetExitActivityWk1ActionPerformed(evt);
            }
        });
        jPanelDescWk1.add(jButtonMeetExitActivityWk1);
        jButtonMeetExitActivityWk1.setBounds(370, 500, 120, 30);

        jButtonReturnActivityWk1.setText("Return to Weekly Plan");
        jButtonReturnActivityWk1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReturnActivityWk1ActionPerformed(evt);
            }
        });
        jPanelDescWk1.add(jButtonReturnActivityWk1);
        jButtonReturnActivityWk1.setBounds(190, 500, 160, 30);

        jLabeMeetlHeadWk1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabeMeetlHeadWk1.setText("Meeting Details - Week One");
        jPanelDescWk1.add(jLabeMeetlHeadWk1);
        jLabeMeetlHeadWk1.setBounds(110, 10, 270, 30);

        jPanelDescWk1.add(jComboMeetingWk1);
        jComboMeetingWk1.setBounds(30, 280, 440, 30);

        javax.swing.GroupLayout jPanelMeetingWk1Layout = new javax.swing.GroupLayout(jPanelMeetingWk1);
        jPanelMeetingWk1.setLayout(jPanelMeetingWk1Layout);
        jPanelMeetingWk1Layout.setHorizontalGroup(
            jPanelMeetingWk1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMeetingWk1Layout.createSequentialGroup()
                .addComponent(jPanelDescWk1, javax.swing.GroupLayout.PREFERRED_SIZE, 526, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelAmtWk1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelMeetingWk1Layout.setVerticalGroup(
            jPanelMeetingWk1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMeetingWk1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelMeetingWk1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelDescWk1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelAmtWk1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jDialogMeetingWk1.getContentPane().add(jPanelMeetingWk1);
        jPanelMeetingWk1.setBounds(0, 0, 850, 650);

        jDialogMeetingWk2.setTitle("MEETING PLANNER WEEK 2");
        jDialogMeetingWk2.setLocation(new java.awt.Point(300, 100));
        jDialogMeetingWk2.setMinimumSize(new java.awt.Dimension(850, 650));
        jDialogMeetingWk2.getContentPane().setLayout(null);

        jPanelMeetingWk2.setBackground(new java.awt.Color(255, 255, 255));
        jPanelMeetingWk2.setMinimumSize(new java.awt.Dimension(850, 650));
        jPanelMeetingWk2.setPreferredSize(new java.awt.Dimension(825, 650));

        jPanelAmtWk2.setBackground(new java.awt.Color(255, 255, 255));
        jPanelAmtWk2.setLayout(null);

        jLabelUnitCostWk2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelUnitCostWk2.setText("Unit Cost");
        jLabelUnitCostWk2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelAmtWk2.add(jLabelUnitCostWk2);
        jLabelUnitCostWk2.setBounds(20, 200, 110, 30);

        jTextUnitCostWk2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextUnitCostWk2ActionPerformed(evt);
            }
        });
        jTextUnitCostWk2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextUnitCostWk2KeyReleased(evt);
            }
        });
        jPanelAmtWk2.add(jTextUnitCostWk2);
        jTextUnitCostWk2.setBounds(170, 200, 50, 30);

        jLabelNumPpleWk2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelNumPpleWk2.setText("No. Of People");
        jLabelNumPpleWk2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelAmtWk2.add(jLabelNumPpleWk2);
        jLabelNumPpleWk2.setBounds(20, 240, 110, 30);

        jTextNumPpleWk2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextNumPpleWk2ActionPerformed(evt);
            }
        });
        jTextNumPpleWk2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextNumPpleWk2KeyReleased(evt);
            }
        });
        jPanelAmtWk2.add(jTextNumPpleWk2);
        jTextNumPpleWk2.setBounds(170, 240, 50, 30);
        jPanelAmtWk2.add(jTextNumDaysWk2);
        jTextNumDaysWk2.setBounds(170, 280, 50, 30);

        jLabelNumDaysWk2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelNumDaysWk2.setText("No. Of Days");
        jLabelNumDaysWk2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelAmtWk2.add(jLabelNumDaysWk2);
        jLabelNumDaysWk2.setBounds(20, 280, 110, 30);

        jLabelBusFareWk2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelBusFareWk2.setText("Total Busfare");
        jLabelBusFareWk2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelAmtWk2.add(jLabelBusFareWk2);
        jLabelBusFareWk2.setBounds(20, 320, 110, 30);

        jTextBusFareWk2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextBusFareWk2ActionPerformed(evt);
            }
        });
        jTextBusFareWk2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextBusFareWk2KeyReleased(evt);
            }
        });
        jPanelAmtWk2.add(jTextBusFareWk2);
        jTextBusFareWk2.setBounds(170, 320, 50, 30);

        jLabelRequestionTotalWk2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelRequestionTotalWk2.setText("Activity Line Total");
        jPanelAmtWk2.add(jLabelRequestionTotalWk2);
        jLabelRequestionTotalWk2.setBounds(20, 390, 130, 30);

        jLabel$Wk2.setText("$");
        jPanelAmtWk2.add(jLabel$Wk2);
        jLabel$Wk2.setBounds(150, 390, 20, 30);

        jLabelTotalAmtWk2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelAmtWk2.add(jLabelTotalAmtWk2);
        jLabelTotalAmtWk2.setBounds(170, 390, 80, 30);

        jLabelBudgetHeadWk2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelBudgetHeadWk2.setText("Budget Details");
        jPanelAmtWk2.add(jLabelBudgetHeadWk2);
        jLabelBudgetHeadWk2.setBounds(70, 10, 170, 30);

        jLabelPersonRespWk2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelPersonRespWk2.setText("Person Responsible");
        jLabelPersonRespWk2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelAmtWk2.add(jLabelPersonRespWk2);
        jLabelPersonRespWk2.setBounds(20, 60, 150, 30);

        jTextPersonResWk2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jTextPersonResWk2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextPersonResWk2MouseClicked(evt);
            }
        });
        jTextPersonResWk2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextPersonResWk2ActionPerformed(evt);
            }
        });
        jTextPersonResWk2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextPersonResWk2KeyReleased(evt);
            }
        });
        jPanelAmtWk2.add(jTextPersonResWk2);
        jTextPersonResWk2.setBounds(20, 100, 250, 30);

        jComboMeetAllowanceWk2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboMeetAllowanceWk2ActionPerformed(evt);
            }
        });
        jPanelAmtWk2.add(jComboMeetAllowanceWk2);
        jComboMeetAllowanceWk2.setBounds(20, 150, 250, 30);

        jPanelDescWk2.setBackground(new java.awt.Color(204, 204, 0));
        jPanelDescWk2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelDescWk2.setLayout(null);

        jLabelFromWk2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelFromWk2.setText("From");
        jPanelDescWk2.add(jLabelFromWk2);
        jLabelFromWk2.setBounds(30, 130, 40, 30);

        jDateFromWk2.setDateFormatString("yyyy-MM-dd");
        jPanelDescWk2.add(jDateFromWk2);
        jDateFromWk2.setBounds(70, 130, 125, 30);

        jLabelToWk2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelToWk2.setText("To");
        jPanelDescWk2.add(jLabelToWk2);
        jLabelToWk2.setBounds(200, 130, 20, 30);

        jDateToWk2.setDateFormatString("yyyy-MM-dd");
        jDateToWk2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jDateToWk2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jDateToWk2MouseEntered(evt);
            }
        });
        jDateToWk2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jDateToWk2KeyReleased(evt);
            }
        });
        jPanelDescWk2.add(jDateToWk2);
        jDateToWk2.setBounds(220, 130, 125, 30);

        jLabelActivityProjectWk2.setText("Project");
        jLabelActivityProjectWk2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelDescWk2.add(jLabelActivityProjectWk2);
        jLabelActivityProjectWk2.setBounds(30, 55, 90, 30);

        jTextFieldBudgetWk2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldBudgetWk2MouseClicked(evt);
            }
        });
        jTextFieldBudgetWk2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldBudgetWk2KeyTyped(evt);
            }
        });
        jPanelDescWk2.add(jTextFieldBudgetWk2);
        jTextFieldBudgetWk2.setBounds(30, 210, 310, 30);

        jLabelBudgetLineWk2.setText("Budget Line");
        jLabelBudgetLineWk2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelDescWk2.add(jLabelBudgetLineWk2);
        jLabelBudgetLineWk2.setBounds(30, 170, 90, 30);

        jLabelMainPurposeWk2.setText("Activity Main Purpose");
        jLabelMainPurposeWk2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelDescWk2.add(jLabelMainPurposeWk2);
        jLabelMainPurposeWk2.setBounds(30, 250, 150, 30);

        jTextAreaDetailDescriptionWk2.setColumns(20);
        jTextAreaDetailDescriptionWk2.setRows(5);
        jScrollPane19.setViewportView(jTextAreaDetailDescriptionWk2);

        jPanelDescWk2.add(jScrollPane19);
        jScrollPane19.setBounds(30, 360, 310, 100);

        jLabelDetailDescriptionWk2.setText("Activity Detailed Description");
        jLabelDetailDescriptionWk2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelDescWk2.add(jLabelDetailDescriptionWk2);
        jLabelDetailDescriptionWk2.setBounds(30, 320, 170, 30);

        jLabelActivityDateWk2.setText("Activity Date");
        jLabelActivityDateWk2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelDescWk2.add(jLabelActivityDateWk2);
        jLabelActivityDateWk2.setBounds(30, 95, 90, 30);

        jPanelDescWk2.add(jComboActivityProjectWk2);
        jComboActivityProjectWk2.setBounds(140, 55, 200, 30);

        jButtonAddActivityWk2.setText("Add Budget Line");
        jButtonAddActivityWk2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActivityWk2ActionPerformed(evt);
            }
        });
        jPanelDescWk2.add(jButtonAddActivityWk2);
        jButtonAddActivityWk2.setBounds(30, 500, 130, 30);

        jButtonMeetExitActivityWk2.setText("Exit");
        jButtonMeetExitActivityWk2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMeetExitActivityWk2ActionPerformed(evt);
            }
        });
        jPanelDescWk2.add(jButtonMeetExitActivityWk2);
        jButtonMeetExitActivityWk2.setBounds(370, 500, 120, 30);

        jButtonReturnActivityWk2.setText("Return to Weekly Plan");
        jButtonReturnActivityWk2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReturnActivityWk2ActionPerformed(evt);
            }
        });
        jPanelDescWk2.add(jButtonReturnActivityWk2);
        jButtonReturnActivityWk2.setBounds(190, 500, 160, 30);

        jLabeMeetlHeadWk2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabeMeetlHeadWk2.setText("Meeting Details - Week Two");
        jPanelDescWk2.add(jLabeMeetlHeadWk2);
        jLabeMeetlHeadWk2.setBounds(110, 10, 270, 30);

        jPanelDescWk2.add(jComboMeetingWk2);
        jComboMeetingWk2.setBounds(30, 280, 440, 30);

        javax.swing.GroupLayout jPanelMeetingWk2Layout = new javax.swing.GroupLayout(jPanelMeetingWk2);
        jPanelMeetingWk2.setLayout(jPanelMeetingWk2Layout);
        jPanelMeetingWk2Layout.setHorizontalGroup(
            jPanelMeetingWk2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMeetingWk2Layout.createSequentialGroup()
                .addComponent(jPanelDescWk2, javax.swing.GroupLayout.PREFERRED_SIZE, 526, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelAmtWk2, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelMeetingWk2Layout.setVerticalGroup(
            jPanelMeetingWk2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMeetingWk2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelMeetingWk2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelDescWk2, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE)
                    .addComponent(jPanelAmtWk2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jDialogMeetingWk2.getContentPane().add(jPanelMeetingWk2);
        jPanelMeetingWk2.setBounds(0, 0, 850, 650);

        jDialogMeetingWk3.setTitle("MEETING PLANNER WEEK 3");
        jDialogMeetingWk3.setLocation(new java.awt.Point(300, 100));
        jDialogMeetingWk3.setMinimumSize(new java.awt.Dimension(850, 650));
        jDialogMeetingWk3.getContentPane().setLayout(null);

        jPanelMeetingWk3.setBackground(new java.awt.Color(255, 255, 255));
        jPanelMeetingWk3.setMinimumSize(new java.awt.Dimension(850, 650));
        jPanelMeetingWk3.setPreferredSize(new java.awt.Dimension(825, 650));

        jPanelAmtWk3.setBackground(new java.awt.Color(255, 255, 255));
        jPanelAmtWk3.setLayout(null);

        jLabelUnitCostWk3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelUnitCostWk3.setText("Unit Cost");
        jLabelUnitCostWk3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelAmtWk3.add(jLabelUnitCostWk3);
        jLabelUnitCostWk3.setBounds(20, 200, 110, 30);

        jTextUnitCostWk3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextUnitCostWk3ActionPerformed(evt);
            }
        });
        jTextUnitCostWk3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextUnitCostWk3KeyReleased(evt);
            }
        });
        jPanelAmtWk3.add(jTextUnitCostWk3);
        jTextUnitCostWk3.setBounds(170, 200, 50, 30);

        jLabelNumPpleWk3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelNumPpleWk3.setText("No. Of People");
        jLabelNumPpleWk3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelAmtWk3.add(jLabelNumPpleWk3);
        jLabelNumPpleWk3.setBounds(20, 240, 110, 30);

        jTextNumPpleWk3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextNumPpleWk3ActionPerformed(evt);
            }
        });
        jTextNumPpleWk3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextNumPpleWk3KeyReleased(evt);
            }
        });
        jPanelAmtWk3.add(jTextNumPpleWk3);
        jTextNumPpleWk3.setBounds(170, 240, 50, 30);
        jPanelAmtWk3.add(jTextNumDaysWk3);
        jTextNumDaysWk3.setBounds(170, 280, 50, 30);

        jLabelNumDaysWk3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelNumDaysWk3.setText("No. Of Days");
        jLabelNumDaysWk3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelAmtWk3.add(jLabelNumDaysWk3);
        jLabelNumDaysWk3.setBounds(20, 280, 110, 30);

        jLabelBusFareWk3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelBusFareWk3.setText("Total Busfare");
        jLabelBusFareWk3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelAmtWk3.add(jLabelBusFareWk3);
        jLabelBusFareWk3.setBounds(20, 320, 110, 30);

        jTextBusFareWk3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextBusFareWk3ActionPerformed(evt);
            }
        });
        jTextBusFareWk3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextBusFareWk3KeyReleased(evt);
            }
        });
        jPanelAmtWk3.add(jTextBusFareWk3);
        jTextBusFareWk3.setBounds(170, 320, 50, 30);

        jLabelRequestionTotalWk3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelRequestionTotalWk3.setText("Activity Line Total");
        jPanelAmtWk3.add(jLabelRequestionTotalWk3);
        jLabelRequestionTotalWk3.setBounds(20, 390, 130, 30);

        jLabel$Wk3.setText("$");
        jPanelAmtWk3.add(jLabel$Wk3);
        jLabel$Wk3.setBounds(150, 390, 20, 30);

        jLabelTotalAmtWk3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelAmtWk3.add(jLabelTotalAmtWk3);
        jLabelTotalAmtWk3.setBounds(170, 390, 80, 30);

        jLabelBudgetHeadWk3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelBudgetHeadWk3.setText("Budget Details");
        jPanelAmtWk3.add(jLabelBudgetHeadWk3);
        jLabelBudgetHeadWk3.setBounds(70, 10, 170, 30);

        jLabelPersonRespWk3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelPersonRespWk3.setText("Person Responsible");
        jLabelPersonRespWk3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelAmtWk3.add(jLabelPersonRespWk3);
        jLabelPersonRespWk3.setBounds(20, 60, 150, 30);

        jTextPersonResWk3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jTextPersonResWk3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextPersonResWk3MouseClicked(evt);
            }
        });
        jTextPersonResWk3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextPersonResWk3ActionPerformed(evt);
            }
        });
        jTextPersonResWk3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextPersonResWk3KeyReleased(evt);
            }
        });
        jPanelAmtWk3.add(jTextPersonResWk3);
        jTextPersonResWk3.setBounds(20, 100, 250, 30);

        jComboMeetAllowanceWk3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboMeetAllowanceWk3ActionPerformed(evt);
            }
        });
        jPanelAmtWk3.add(jComboMeetAllowanceWk3);
        jComboMeetAllowanceWk3.setBounds(20, 150, 250, 30);

        jPanelDescWk3.setBackground(new java.awt.Color(204, 204, 0));
        jPanelDescWk3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelDescWk3.setLayout(null);

        jLabelFromWk3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelFromWk3.setText("From");
        jPanelDescWk3.add(jLabelFromWk3);
        jLabelFromWk3.setBounds(30, 130, 40, 30);

        jDateFromWk3.setDateFormatString("yyyy-MM-dd");
        jPanelDescWk3.add(jDateFromWk3);
        jDateFromWk3.setBounds(70, 130, 125, 30);

        jLabelToWk3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelToWk3.setText("To");
        jPanelDescWk3.add(jLabelToWk3);
        jLabelToWk3.setBounds(200, 130, 20, 30);

        jDateToWk3.setDateFormatString("yyyy-MM-dd");
        jDateToWk3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jDateToWk3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jDateToWk3MouseEntered(evt);
            }
        });
        jDateToWk3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jDateToWk3KeyReleased(evt);
            }
        });
        jPanelDescWk3.add(jDateToWk3);
        jDateToWk3.setBounds(220, 130, 125, 30);

        jLabelActivityProjectWk3.setText("Project");
        jLabelActivityProjectWk3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelDescWk3.add(jLabelActivityProjectWk3);
        jLabelActivityProjectWk3.setBounds(30, 55, 90, 30);

        jTextFieldBudgetWk3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldBudgetWk3MouseClicked(evt);
            }
        });
        jTextFieldBudgetWk3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldBudgetWk3KeyTyped(evt);
            }
        });
        jPanelDescWk3.add(jTextFieldBudgetWk3);
        jTextFieldBudgetWk3.setBounds(30, 210, 310, 30);

        jLabelBudgetLineWk3.setText("Budget Line");
        jLabelBudgetLineWk3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelDescWk3.add(jLabelBudgetLineWk3);
        jLabelBudgetLineWk3.setBounds(30, 170, 90, 30);

        jLabelMainPurposeWk3.setText("Activity Main Purpose");
        jLabelMainPurposeWk3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelDescWk3.add(jLabelMainPurposeWk3);
        jLabelMainPurposeWk3.setBounds(30, 250, 150, 30);

        jTextAreaDetailDescriptionWk3.setColumns(20);
        jTextAreaDetailDescriptionWk3.setRows(5);
        jScrollPane20.setViewportView(jTextAreaDetailDescriptionWk3);

        jPanelDescWk3.add(jScrollPane20);
        jScrollPane20.setBounds(30, 360, 310, 100);

        jLabelDetailDescriptionWk3.setText("Activity Detailed Description");
        jLabelDetailDescriptionWk3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelDescWk3.add(jLabelDetailDescriptionWk3);
        jLabelDetailDescriptionWk3.setBounds(30, 320, 170, 30);

        jLabelActivityDateWk3.setText("Activity Date");
        jLabelActivityDateWk3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelDescWk3.add(jLabelActivityDateWk3);
        jLabelActivityDateWk3.setBounds(30, 95, 90, 30);

        jPanelDescWk3.add(jComboActivityProjectWk3);
        jComboActivityProjectWk3.setBounds(140, 55, 200, 30);

        jButtonAddActivityWk3.setText("Add Budget Line");
        jButtonAddActivityWk3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActivityWk3ActionPerformed(evt);
            }
        });
        jPanelDescWk3.add(jButtonAddActivityWk3);
        jButtonAddActivityWk3.setBounds(30, 500, 130, 30);

        jButtonMeetExitActivityWk3.setText("Exit");
        jButtonMeetExitActivityWk3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMeetExitActivityWk3ActionPerformed(evt);
            }
        });
        jPanelDescWk3.add(jButtonMeetExitActivityWk3);
        jButtonMeetExitActivityWk3.setBounds(370, 500, 120, 30);

        jButtonReturnActivityWk3.setText("Return to Weekly Plan");
        jButtonReturnActivityWk3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReturnActivityWk3ActionPerformed(evt);
            }
        });
        jPanelDescWk3.add(jButtonReturnActivityWk3);
        jButtonReturnActivityWk3.setBounds(190, 500, 160, 30);

        jLabeMeetlHeadWk3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabeMeetlHeadWk3.setText("Meeting Details - Week Three");
        jPanelDescWk3.add(jLabeMeetlHeadWk3);
        jLabeMeetlHeadWk3.setBounds(110, 10, 300, 30);

        jPanelDescWk3.add(jComboMeetingWk3);
        jComboMeetingWk3.setBounds(30, 280, 440, 30);

        javax.swing.GroupLayout jPanelMeetingWk3Layout = new javax.swing.GroupLayout(jPanelMeetingWk3);
        jPanelMeetingWk3.setLayout(jPanelMeetingWk3Layout);
        jPanelMeetingWk3Layout.setHorizontalGroup(
            jPanelMeetingWk3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMeetingWk3Layout.createSequentialGroup()
                .addComponent(jPanelDescWk3, javax.swing.GroupLayout.PREFERRED_SIZE, 526, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelAmtWk3, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelMeetingWk3Layout.setVerticalGroup(
            jPanelMeetingWk3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMeetingWk3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelMeetingWk3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelDescWk3, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE)
                    .addComponent(jPanelAmtWk3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jDialogMeetingWk3.getContentPane().add(jPanelMeetingWk3);
        jPanelMeetingWk3.setBounds(0, 0, 850, 650);

        jDialogMeetingWk4.setTitle("MEETING PLANNER WEEK 4");
        jDialogMeetingWk4.setLocation(new java.awt.Point(300, 100));
        jDialogMeetingWk4.setMinimumSize(new java.awt.Dimension(850, 650));
        jDialogMeetingWk4.getContentPane().setLayout(null);

        jPanelMeetingWk4.setBackground(new java.awt.Color(255, 255, 255));
        jPanelMeetingWk4.setMinimumSize(new java.awt.Dimension(850, 650));
        jPanelMeetingWk4.setPreferredSize(new java.awt.Dimension(825, 650));

        jPanelAmtWk4.setBackground(new java.awt.Color(255, 255, 255));
        jPanelAmtWk4.setLayout(null);

        jLabelUnitCostWk4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelUnitCostWk4.setText("Unit Cost");
        jLabelUnitCostWk4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelAmtWk4.add(jLabelUnitCostWk4);
        jLabelUnitCostWk4.setBounds(20, 200, 110, 30);

        jTextUnitCostWk4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextUnitCostWk4ActionPerformed(evt);
            }
        });
        jTextUnitCostWk4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextUnitCostWk4KeyReleased(evt);
            }
        });
        jPanelAmtWk4.add(jTextUnitCostWk4);
        jTextUnitCostWk4.setBounds(170, 200, 50, 30);

        jLabelNumPpleWk4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelNumPpleWk4.setText("No. Of People");
        jLabelNumPpleWk4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelAmtWk4.add(jLabelNumPpleWk4);
        jLabelNumPpleWk4.setBounds(20, 240, 110, 30);

        jTextNumPpleWk4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextNumPpleWk4ActionPerformed(evt);
            }
        });
        jTextNumPpleWk4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextNumPpleWk4KeyReleased(evt);
            }
        });
        jPanelAmtWk4.add(jTextNumPpleWk4);
        jTextNumPpleWk4.setBounds(170, 240, 50, 30);
        jPanelAmtWk4.add(jTextNumDaysWk4);
        jTextNumDaysWk4.setBounds(170, 280, 50, 30);

        jLabelNumDaysWk4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelNumDaysWk4.setText("No. Of Days");
        jLabelNumDaysWk4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelAmtWk4.add(jLabelNumDaysWk4);
        jLabelNumDaysWk4.setBounds(20, 280, 110, 30);

        jLabelBusFareWk4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelBusFareWk4.setText("Total Busfare");
        jLabelBusFareWk4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelAmtWk4.add(jLabelBusFareWk4);
        jLabelBusFareWk4.setBounds(20, 320, 110, 30);

        jTextBusFareWk4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextBusFareWk4ActionPerformed(evt);
            }
        });
        jTextBusFareWk4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextBusFareWk4KeyReleased(evt);
            }
        });
        jPanelAmtWk4.add(jTextBusFareWk4);
        jTextBusFareWk4.setBounds(170, 320, 50, 30);

        jLabelRequestionTotalWk4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelRequestionTotalWk4.setText("Activity Line Total");
        jPanelAmtWk4.add(jLabelRequestionTotalWk4);
        jLabelRequestionTotalWk4.setBounds(20, 390, 130, 30);

        jLabel$Wk4.setText("$");
        jPanelAmtWk4.add(jLabel$Wk4);
        jLabel$Wk4.setBounds(150, 390, 20, 30);

        jLabelTotalAmtWk4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelAmtWk4.add(jLabelTotalAmtWk4);
        jLabelTotalAmtWk4.setBounds(170, 390, 80, 30);

        jLabelBudgetHeadWk4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelBudgetHeadWk4.setText("Budget Details");
        jPanelAmtWk4.add(jLabelBudgetHeadWk4);
        jLabelBudgetHeadWk4.setBounds(70, 10, 170, 30);

        jLabelPersonRespWk4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelPersonRespWk4.setText("Person Responsible");
        jLabelPersonRespWk4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelAmtWk4.add(jLabelPersonRespWk4);
        jLabelPersonRespWk4.setBounds(20, 60, 150, 30);

        jTextPersonResWk4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jTextPersonResWk4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextPersonResWk4MouseClicked(evt);
            }
        });
        jTextPersonResWk4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextPersonResWk4ActionPerformed(evt);
            }
        });
        jTextPersonResWk4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextPersonResWk4KeyReleased(evt);
            }
        });
        jPanelAmtWk4.add(jTextPersonResWk4);
        jTextPersonResWk4.setBounds(20, 100, 250, 30);

        jComboMeetAllowanceWk4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboMeetAllowanceWk4ActionPerformed(evt);
            }
        });
        jPanelAmtWk4.add(jComboMeetAllowanceWk4);
        jComboMeetAllowanceWk4.setBounds(20, 150, 250, 30);

        jPanelDescWk4.setBackground(new java.awt.Color(204, 204, 0));
        jPanelDescWk4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelDescWk4.setLayout(null);

        jLabelFromWk4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelFromWk4.setText("From");
        jPanelDescWk4.add(jLabelFromWk4);
        jLabelFromWk4.setBounds(30, 130, 40, 30);

        jDateFromWk4.setDateFormatString("yyyy-MM-dd");
        jPanelDescWk4.add(jDateFromWk4);
        jDateFromWk4.setBounds(70, 130, 125, 30);

        jLabelToWk4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelToWk4.setText("To");
        jPanelDescWk4.add(jLabelToWk4);
        jLabelToWk4.setBounds(200, 130, 20, 30);

        jDateToWk4.setDateFormatString("yyyy-MM-dd");
        jDateToWk4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jDateToWk4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jDateToWk4MouseEntered(evt);
            }
        });
        jDateToWk4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jDateToWk4KeyReleased(evt);
            }
        });
        jPanelDescWk4.add(jDateToWk4);
        jDateToWk4.setBounds(220, 130, 125, 30);

        jLabelActivityProjectWk4.setText("Project");
        jLabelActivityProjectWk4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelDescWk4.add(jLabelActivityProjectWk4);
        jLabelActivityProjectWk4.setBounds(30, 55, 90, 30);

        jTextFieldBudgetWk4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldBudgetWk4MouseClicked(evt);
            }
        });
        jTextFieldBudgetWk4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldBudgetWk4KeyTyped(evt);
            }
        });
        jPanelDescWk4.add(jTextFieldBudgetWk4);
        jTextFieldBudgetWk4.setBounds(30, 210, 310, 30);

        jLabelBudgetLineWk4.setText("Budget Line");
        jLabelBudgetLineWk4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelDescWk4.add(jLabelBudgetLineWk4);
        jLabelBudgetLineWk4.setBounds(30, 170, 90, 30);

        jLabelMainPurposeWk4.setText("Activity Main Purpose");
        jLabelMainPurposeWk4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelDescWk4.add(jLabelMainPurposeWk4);
        jLabelMainPurposeWk4.setBounds(30, 250, 150, 30);

        jTextAreaDetailDescriptionWk4.setColumns(20);
        jTextAreaDetailDescriptionWk4.setRows(5);
        jScrollPane21.setViewportView(jTextAreaDetailDescriptionWk4);

        jPanelDescWk4.add(jScrollPane21);
        jScrollPane21.setBounds(30, 360, 310, 100);

        jLabelDetailDescriptionWk4.setText("Activity Detailed Description");
        jLabelDetailDescriptionWk4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelDescWk4.add(jLabelDetailDescriptionWk4);
        jLabelDetailDescriptionWk4.setBounds(30, 320, 170, 30);

        jLabelActivityDateWk4.setText("Activity Date");
        jLabelActivityDateWk4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelDescWk4.add(jLabelActivityDateWk4);
        jLabelActivityDateWk4.setBounds(30, 95, 90, 30);

        jPanelDescWk4.add(jComboActivityProjectWk4);
        jComboActivityProjectWk4.setBounds(140, 55, 200, 30);

        jButtonAddActivityWk4.setText("Add Budget Line");
        jButtonAddActivityWk4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActivityWk4ActionPerformed(evt);
            }
        });
        jPanelDescWk4.add(jButtonAddActivityWk4);
        jButtonAddActivityWk4.setBounds(30, 500, 130, 30);

        jButtonMeetExitActivityWk4.setText("Exit");
        jButtonMeetExitActivityWk4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMeetExitActivityWk4ActionPerformed(evt);
            }
        });
        jPanelDescWk4.add(jButtonMeetExitActivityWk4);
        jButtonMeetExitActivityWk4.setBounds(370, 500, 120, 30);

        jButtonReturnActivityWk4.setText("Return to Weekly Plan");
        jButtonReturnActivityWk4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReturnActivityWk4ActionPerformed(evt);
            }
        });
        jPanelDescWk4.add(jButtonReturnActivityWk4);
        jButtonReturnActivityWk4.setBounds(190, 500, 160, 30);

        jLabeMeetlHeadWk4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabeMeetlHeadWk4.setText("Meeting Details - Week Four");
        jPanelDescWk4.add(jLabeMeetlHeadWk4);
        jLabeMeetlHeadWk4.setBounds(110, 10, 270, 30);

        jPanelDescWk4.add(jComboMeetingWk4);
        jComboMeetingWk4.setBounds(30, 280, 440, 30);

        javax.swing.GroupLayout jPanelMeetingWk4Layout = new javax.swing.GroupLayout(jPanelMeetingWk4);
        jPanelMeetingWk4.setLayout(jPanelMeetingWk4Layout);
        jPanelMeetingWk4Layout.setHorizontalGroup(
            jPanelMeetingWk4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMeetingWk4Layout.createSequentialGroup()
                .addComponent(jPanelDescWk4, javax.swing.GroupLayout.PREFERRED_SIZE, 526, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelAmtWk4, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelMeetingWk4Layout.setVerticalGroup(
            jPanelMeetingWk4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMeetingWk4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelMeetingWk4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelDescWk4, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE)
                    .addComponent(jPanelAmtWk4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jDialogMeetingWk4.getContentPane().add(jPanelMeetingWk4);
        jPanelMeetingWk4.setBounds(0, 0, 850, 650);

        jDialogMeetingWk5.setTitle("MEETING PLANNER WEEK 5");
        jDialogMeetingWk5.setLocation(new java.awt.Point(300, 100));
        jDialogMeetingWk5.setMinimumSize(new java.awt.Dimension(850, 650));
        jDialogMeetingWk5.getContentPane().setLayout(null);

        jPanelMeetingWk5.setBackground(new java.awt.Color(255, 255, 255));
        jPanelMeetingWk5.setMinimumSize(new java.awt.Dimension(850, 650));
        jPanelMeetingWk5.setPreferredSize(new java.awt.Dimension(825, 650));

        jPanelAmtWk5.setBackground(new java.awt.Color(255, 255, 255));
        jPanelAmtWk5.setLayout(null);

        jLabelUnitCostWk5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelUnitCostWk5.setText("Unit Cost");
        jLabelUnitCostWk5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelAmtWk5.add(jLabelUnitCostWk5);
        jLabelUnitCostWk5.setBounds(20, 200, 110, 30);

        jTextUnitCostWk5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextUnitCostWk5ActionPerformed(evt);
            }
        });
        jTextUnitCostWk5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextUnitCostWk5KeyReleased(evt);
            }
        });
        jPanelAmtWk5.add(jTextUnitCostWk5);
        jTextUnitCostWk5.setBounds(170, 200, 50, 30);

        jLabelNumPpleWk5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelNumPpleWk5.setText("No. Of People");
        jLabelNumPpleWk5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelAmtWk5.add(jLabelNumPpleWk5);
        jLabelNumPpleWk5.setBounds(20, 240, 110, 30);

        jTextNumPpleWk5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextNumPpleWk5ActionPerformed(evt);
            }
        });
        jTextNumPpleWk5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextNumPpleWk5KeyReleased(evt);
            }
        });
        jPanelAmtWk5.add(jTextNumPpleWk5);
        jTextNumPpleWk5.setBounds(170, 240, 50, 30);
        jPanelAmtWk5.add(jTextNumDaysWk5);
        jTextNumDaysWk5.setBounds(170, 280, 50, 30);

        jLabelNumDaysWk5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelNumDaysWk5.setText("No. Of Days");
        jLabelNumDaysWk5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelAmtWk5.add(jLabelNumDaysWk5);
        jLabelNumDaysWk5.setBounds(20, 280, 110, 30);

        jLabelBusFareWk5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelBusFareWk5.setText("Total Busfare");
        jLabelBusFareWk5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelAmtWk5.add(jLabelBusFareWk5);
        jLabelBusFareWk5.setBounds(20, 320, 110, 30);

        jTextBusFareWk5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextBusFareWk5ActionPerformed(evt);
            }
        });
        jTextBusFareWk5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextBusFareWk5KeyReleased(evt);
            }
        });
        jPanelAmtWk5.add(jTextBusFareWk5);
        jTextBusFareWk5.setBounds(170, 320, 50, 30);

        jLabelRequestionTotalWk5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelRequestionTotalWk5.setText("Activity Line Total");
        jPanelAmtWk5.add(jLabelRequestionTotalWk5);
        jLabelRequestionTotalWk5.setBounds(20, 390, 130, 30);

        jLabel$Wk5.setText("$");
        jPanelAmtWk5.add(jLabel$Wk5);
        jLabel$Wk5.setBounds(150, 390, 20, 30);

        jLabelTotalAmtWk5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelAmtWk5.add(jLabelTotalAmtWk5);
        jLabelTotalAmtWk5.setBounds(170, 390, 80, 30);

        jLabelBudgetHeadWk5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelBudgetHeadWk5.setText("Budget Details");
        jPanelAmtWk5.add(jLabelBudgetHeadWk5);
        jLabelBudgetHeadWk5.setBounds(70, 10, 170, 30);

        jLabelPersonRespWk5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelPersonRespWk5.setText("Person Responsible");
        jLabelPersonRespWk5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelAmtWk5.add(jLabelPersonRespWk5);
        jLabelPersonRespWk5.setBounds(20, 60, 150, 30);

        jTextPersonResWk5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jTextPersonResWk5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextPersonResWk5MouseClicked(evt);
            }
        });
        jTextPersonResWk5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextPersonResWk5ActionPerformed(evt);
            }
        });
        jTextPersonResWk5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextPersonResWk5KeyReleased(evt);
            }
        });
        jPanelAmtWk5.add(jTextPersonResWk5);
        jTextPersonResWk5.setBounds(20, 100, 250, 30);

        jComboMeetAllowanceWk5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboMeetAllowanceWk5ActionPerformed(evt);
            }
        });
        jPanelAmtWk5.add(jComboMeetAllowanceWk5);
        jComboMeetAllowanceWk5.setBounds(20, 150, 250, 30);

        jPanelDescWk5.setBackground(new java.awt.Color(204, 204, 0));
        jPanelDescWk5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelDescWk5.setLayout(null);

        jLabelFromWk5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelFromWk5.setText("From");
        jPanelDescWk5.add(jLabelFromWk5);
        jLabelFromWk5.setBounds(30, 130, 40, 30);

        jDateFromWk5.setDateFormatString("yyyy-MM-dd");
        jPanelDescWk5.add(jDateFromWk5);
        jDateFromWk5.setBounds(70, 130, 125, 30);

        jLabelToWk5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelToWk5.setText("To");
        jPanelDescWk5.add(jLabelToWk5);
        jLabelToWk5.setBounds(200, 130, 20, 30);

        jDateToWk5.setDateFormatString("yyyy-MM-dd");
        jDateToWk5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jDateToWk5MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jDateToWk5MouseEntered(evt);
            }
        });
        jDateToWk5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jDateToWk5KeyReleased(evt);
            }
        });
        jPanelDescWk5.add(jDateToWk5);
        jDateToWk5.setBounds(220, 130, 125, 30);

        jLabelActivityProjectWk5.setText("Project");
        jLabelActivityProjectWk5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelDescWk5.add(jLabelActivityProjectWk5);
        jLabelActivityProjectWk5.setBounds(30, 55, 90, 30);

        jTextFieldBudgetWk5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldBudgetWk5MouseClicked(evt);
            }
        });
        jTextFieldBudgetWk5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldBudgetWk5KeyTyped(evt);
            }
        });
        jPanelDescWk5.add(jTextFieldBudgetWk5);
        jTextFieldBudgetWk5.setBounds(30, 210, 310, 30);

        jLabelBudgetLineWk5.setText("Budget Line");
        jLabelBudgetLineWk5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelDescWk5.add(jLabelBudgetLineWk5);
        jLabelBudgetLineWk5.setBounds(30, 170, 90, 30);

        jLabelMainPurposeWk5.setText("Activity Main Purpose");
        jLabelMainPurposeWk5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelDescWk5.add(jLabelMainPurposeWk5);
        jLabelMainPurposeWk5.setBounds(30, 250, 150, 30);

        jTextAreaDetailDescriptionWk5.setColumns(20);
        jTextAreaDetailDescriptionWk5.setRows(5);
        jScrollPane22.setViewportView(jTextAreaDetailDescriptionWk5);

        jPanelDescWk5.add(jScrollPane22);
        jScrollPane22.setBounds(30, 360, 310, 100);

        jLabelDetailDescriptionWk5.setText("Activity Detailed Description");
        jLabelDetailDescriptionWk5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelDescWk5.add(jLabelDetailDescriptionWk5);
        jLabelDetailDescriptionWk5.setBounds(30, 320, 170, 30);

        jLabelActivityDateWk5.setText("Activity Date");
        jLabelActivityDateWk5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelDescWk5.add(jLabelActivityDateWk5);
        jLabelActivityDateWk5.setBounds(30, 95, 90, 30);

        jPanelDescWk5.add(jComboActivityProjectWk5);
        jComboActivityProjectWk5.setBounds(140, 55, 200, 30);

        jButtonAddActivityWk5.setText("Add Budget Line");
        jButtonAddActivityWk5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActivityWk5ActionPerformed(evt);
            }
        });
        jPanelDescWk5.add(jButtonAddActivityWk5);
        jButtonAddActivityWk5.setBounds(30, 500, 130, 30);

        jButtonMeetExitActivityWk5.setText("Exit");
        jButtonMeetExitActivityWk5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMeetExitActivityWk5ActionPerformed(evt);
            }
        });
        jPanelDescWk5.add(jButtonMeetExitActivityWk5);
        jButtonMeetExitActivityWk5.setBounds(370, 500, 120, 30);

        jButtonReturnActivityWk5.setText("Return to Weekly Plan");
        jButtonReturnActivityWk5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReturnActivityWk5ActionPerformed(evt);
            }
        });
        jPanelDescWk5.add(jButtonReturnActivityWk5);
        jButtonReturnActivityWk5.setBounds(190, 500, 160, 30);

        jLabeMeetlHeadWk5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabeMeetlHeadWk5.setText("Meeting Details - Week Five");
        jPanelDescWk5.add(jLabeMeetlHeadWk5);
        jLabeMeetlHeadWk5.setBounds(110, 10, 270, 30);

        jPanelDescWk5.add(jComboMeetingWk5);
        jComboMeetingWk5.setBounds(30, 280, 440, 30);

        javax.swing.GroupLayout jPanelMeetingWk5Layout = new javax.swing.GroupLayout(jPanelMeetingWk5);
        jPanelMeetingWk5.setLayout(jPanelMeetingWk5Layout);
        jPanelMeetingWk5Layout.setHorizontalGroup(
            jPanelMeetingWk5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMeetingWk5Layout.createSequentialGroup()
                .addComponent(jPanelDescWk5, javax.swing.GroupLayout.PREFERRED_SIZE, 526, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelAmtWk5, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelMeetingWk5Layout.setVerticalGroup(
            jPanelMeetingWk5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMeetingWk5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelMeetingWk5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelDescWk5, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE)
                    .addComponent(jPanelAmtWk5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jDialogMeetingWk5.getContentPane().add(jPanelMeetingWk5);
        jPanelMeetingWk5.setBounds(0, 0, 850, 650);

        jDialogSearchName1.setTitle("Search Employee ID");
        jDialogSearchName1.setAlwaysOnTop(true);
        jDialogSearchName1.setAutoRequestFocus(false);
        jDialogSearchName1.setBackground(new java.awt.Color(153, 204, 0));
        jDialogSearchName1.setLocation(new java.awt.Point(700, 400));
        jDialogSearchName1.setMinimumSize(new java.awt.Dimension(400, 200));

        jPanelSearchID.setBackground(new java.awt.Color(7, 163, 163));
        jPanelSearchID.setMinimumSize(new java.awt.Dimension(400, 200));
        jPanelSearchID.setLayout(null);

        jTextFieldSearchNam1.setPreferredSize(new java.awt.Dimension(50, 30));
        jTextFieldSearchNam1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldSearchNam1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldSearchNam1FocusLost(evt);
            }
        });
        jTextFieldSearchNam1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldSearchNam1ActionPerformed(evt);
            }
        });
        jPanelSearchID.add(jTextFieldSearchNam1);
        jTextFieldSearchNam1.setBounds(10, 22, 204, 30);

        jButtonSearch1.setText("Search");
        jButtonSearch1.setPreferredSize(new java.awt.Dimension(65, 30));
        jButtonSearch1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearch1ActionPerformed(evt);
            }
        });
        jPanelSearchID.add(jButtonSearch1);
        jButtonSearch1.setBounds(256, 22, 90, 30);

        jComboBoxSearchResult1.setPreferredSize(new java.awt.Dimension(70, 30));
        jPanelSearchID.add(jComboBoxSearchResult1);
        jComboBoxSearchResult1.setBounds(10, 70, 204, 30);

        jButtonOk1.setText("OK");
        jButtonOk1.setPreferredSize(new java.awt.Dimension(73, 30));
        jButtonOk1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOk1ActionPerformed(evt);
            }
        });
        jPanelSearchID.add(jButtonOk1);
        jButtonOk1.setBounds(256, 70, 90, 30);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Enter search term e.g. Joe and click search button");
        jPanelSearchID.add(jLabel4);
        jLabel4.setBounds(12, 3, 270, 13);

        javax.swing.GroupLayout jDialogSearchName1Layout = new javax.swing.GroupLayout(jDialogSearchName1.getContentPane());
        jDialogSearchName1.getContentPane().setLayout(jDialogSearchName1Layout);
        jDialogSearchName1Layout.setHorizontalGroup(
            jDialogSearchName1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelSearchID, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogSearchName1Layout.setVerticalGroup(
            jDialogSearchName1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelSearchID, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jDialogWk1.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jDialogWk1.setTitle("ACTIVITY PLANNER WEEK 1");
        jDialogWk1.setAlwaysOnTop(true);
        jDialogWk1.setBackground(new java.awt.Color(255, 255, 204));
        jDialogWk1.setLocation(new java.awt.Point(300, 100));
        jDialogWk1.setName("dialogWk1"); // NOI18N
        jDialogWk1.setResizable(false);
        jDialogWk1.setSize(new java.awt.Dimension(850, 650));

        jPanel1.setBackground(new java.awt.Color(237, 235, 92));
        jPanel1.setPreferredSize(new java.awt.Dimension(890, 650));
        jPanel1.setVerifyInputWhenFocusTarget(false);
        jPanel1.setLayout(null);

        jLabelDialogWk1ActivityDate.setText("Activity Date");
        jPanel1.add(jLabelDialogWk1ActivityDate);
        jLabelDialogWk1ActivityDate.setBounds(10, 80, 90, 30);

        jDateChooserDialogWk1ActivityDate.setDateFormatString("yyyy-MM-dd");
        jPanel1.add(jDateChooserDialogWk1ActivityDate);
        jDateChooserDialogWk1ActivityDate.setBounds(125, 80, 120, 30);

        jLabelDialogWk1Site.setText("Site");
        jPanel1.add(jLabelDialogWk1Site);
        jLabelDialogWk1Site.setBounds(10, 240, 40, 30);

        jTextFieldDialogWk1Site.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldDialogWk1SiteMouseClicked(evt);
            }
        });
        jTextFieldDialogWk1Site.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldDialogWk1SiteActionPerformed(evt);
            }
        });
        jTextFieldDialogWk1Site.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldDialogWk1SiteKeyTyped(evt);
            }
        });
        jPanel1.add(jTextFieldDialogWk1Site);
        jTextFieldDialogWk1Site.setBounds(70, 240, 440, 30);

        jLabelWk1DialogJustification.setText("Justification for Choice of Activity ");
        jPanel1.add(jLabelWk1DialogJustification);
        jLabelWk1DialogJustification.setBounds(10, 360, 340, 30);

        jLabel10.setText("4.");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(540, 180, 20, 30);
        jPanel1.add(jTextFieldWk1DialogActivityDesc);
        jTextFieldWk1DialogActivityDesc.setBounds(10, 330, 490, 30);

        jLabelWk1DialogActivityDesc.setText("Activity Descrpition");
        jPanel1.add(jLabelWk1DialogActivityDesc);
        jLabelWk1DialogActivityDesc.setBounds(10, 290, 130, 30);

        jTextAreaWk1DialogJustification.setColumns(20);
        jTextAreaWk1DialogJustification.setRows(5);
        jTextAreaWk1DialogJustification.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextAreaWk1DialogJustificationKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(jTextAreaWk1DialogJustification);

        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(10, 400, 500, 96);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel1.add(jSeparator3);
        jSeparator3.setBounds(530, 10, 20, 490);

        jComboBoxDialogWk1PerDiem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Per Diem", "Meetings" }));
        jComboBoxDialogWk1PerDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxDialogWk1PerDiemActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBoxDialogWk1PerDiem);
        jComboBoxDialogWk1PerDiem.setBounds(125, 30, 200, 30);

        jLabelDialogPerDiem.setText("Per Diem/ Meetings");
        jPanel1.add(jLabelDialogPerDiem);
        jLabelDialogPerDiem.setBounds(10, 30, 110, 30);

        jLabelWk1DialogStaffName.setText("Staff Details");
        jPanel1.add(jLabelWk1DialogStaffName);
        jLabelWk1DialogStaffName.setBounds(540, 30, 130, 30);

        jLabelWk1DialogStaffName1.setText("1.");
        jPanel1.add(jLabelWk1DialogStaffName1);
        jLabelWk1DialogStaffName1.setBounds(540, 60, 20, 30);

        jLabelWk1DialogStaffName2.setText("2.");
        jPanel1.add(jLabelWk1DialogStaffName2);
        jLabelWk1DialogStaffName2.setBounds(540, 100, 20, 30);

        jLabel17.setText("3.");
        jPanel1.add(jLabel17);
        jLabel17.setBounds(540, 140, 20, 30);

        jTextFieldWk1DialogStaffName4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk1DialogStaffName4MouseClicked(evt);
            }
        });
        jTextFieldWk1DialogStaffName4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldWk1DialogStaffName4ActionPerformed(evt);
            }
        });
        jTextFieldWk1DialogStaffName4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk1DialogStaffName4KeyTyped(evt);
            }
        });
        jPanel1.add(jTextFieldWk1DialogStaffName4);
        jTextFieldWk1DialogStaffName4.setBounds(560, 180, 220, 30);

        jTextFieldWk1DialogStaffName1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk1DialogStaffName1MouseClicked(evt);
            }
        });
        jTextFieldWk1DialogStaffName1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldWk1DialogStaffName1ActionPerformed(evt);
            }
        });
        jTextFieldWk1DialogStaffName1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk1DialogStaffName1KeyTyped(evt);
            }
        });
        jPanel1.add(jTextFieldWk1DialogStaffName1);
        jTextFieldWk1DialogStaffName1.setBounds(560, 60, 220, 30);

        jTextFieldWk1DialogStaffName2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk1DialogStaffName2MouseClicked(evt);
            }
        });
        jTextFieldWk1DialogStaffName2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk1DialogStaffName2KeyTyped(evt);
            }
        });
        jPanel1.add(jTextFieldWk1DialogStaffName2);
        jTextFieldWk1DialogStaffName2.setBounds(560, 100, 220, 30);

        jTextFieldWk1DialogStaffName3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk1DialogStaffName3MouseClicked(evt);
            }
        });
        jTextFieldWk1DialogStaffName3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk1DialogStaffName3KeyTyped(evt);
            }
        });
        jPanel1.add(jTextFieldWk1DialogStaffName3);
        jTextFieldWk1DialogStaffName3.setBounds(560, 140, 220, 30);

        jButtonDialogWk1Reset.setText("Reset");
        jButtonDialogWk1Reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDialogWk1ResetActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonDialogWk1Reset);
        jButtonDialogWk1Reset.setBounds(650, 460, 70, 30);

        jButtonDialogWk1Add.setText("Add Activity");
        jButtonDialogWk1Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDialogWk1AddActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonDialogWk1Add);
        jButtonDialogWk1Add.setBounds(540, 460, 100, 30);

        jCheckBoxDialogWk1BrkFast.setText(" Breakfast");
        jPanel1.add(jCheckBoxDialogWk1BrkFast);
        jCheckBoxDialogWk1BrkFast.setBounds(550, 230, 90, 21);

        jCheckBoxDialogWk1Lunch.setText("Lunch");
        jPanel1.add(jCheckBoxDialogWk1Lunch);
        jCheckBoxDialogWk1Lunch.setBounds(670, 230, 80, 21);

        jCheckBoxDialogWk1Dinner.setText(" Dinner");
        jPanel1.add(jCheckBoxDialogWk1Dinner);
        jCheckBoxDialogWk1Dinner.setBounds(550, 270, 90, 21);

        jCheckBoxDialogWk1AccProved.setText(" Proved Acc");
        jPanel1.add(jCheckBoxDialogWk1AccProved);
        jCheckBoxDialogWk1AccProved.setBounds(670, 310, 130, 21);

        jLabelRemain.setFont(new java.awt.Font("Tahoma", 3, 10)); // NOI18N
        jPanel1.add(jLabelRemain);
        jLabelRemain.setBounds(20, 440, 250, 20);

        jCheckBoxDialogWk1Misc.setText("Miscellaneous");
        jCheckBoxDialogWk1Misc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDialogWk1MiscActionPerformed(evt);
            }
        });
        jPanel1.add(jCheckBoxDialogWk1Misc);
        jCheckBoxDialogWk1Misc.setBounds(550, 350, 140, 21);

        jCheckBoxDialogWk1Inc.setText("Incidental");
        jPanel1.add(jCheckBoxDialogWk1Inc);
        jCheckBoxDialogWk1Inc.setBounds(550, 310, 110, 21);

        jLabelWk1Misc.setText("Miscellaneous Desc");
        jPanel1.add(jLabelWk1Misc);
        jLabelWk1Misc.setBounds(560, 370, 160, 30);

        jTextFieldWk1Misc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldWk1MiscActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFieldWk1Misc);
        jTextFieldWk1Misc.setBounds(560, 400, 110, 30);

        jButtonDialogWk1Close.setText("Close");
        jButtonDialogWk1Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDialogWk1CloseActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonDialogWk1Close);
        jButtonDialogWk1Close.setBounds(730, 460, 70, 30);

        jLabelWk1MiscAmt.setText("$");
        jPanel1.add(jLabelWk1MiscAmt);
        jLabelWk1MiscAmt.setBounds(690, 400, 30, 30);
        jPanel1.add(jTextFieldWk1MiscAmt);
        jTextFieldWk1MiscAmt.setBounds(730, 400, 70, 30);

        jLabelWk1Name4Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk1Name4Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk1Name4DelMouseClicked(evt);
            }
        });
        jPanel1.add(jLabelWk1Name4Del);
        jLabelWk1Name4Del.setBounds(780, 180, 30, 30);

        jLabelWk1Name1Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk1Name1Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk1Name1DelMouseClicked(evt);
            }
        });
        jPanel1.add(jLabelWk1Name1Del);
        jLabelWk1Name1Del.setBounds(780, 60, 30, 30);

        jLabelWk1Name2Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk1Name2Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk1Name2DelMouseClicked(evt);
            }
        });
        jPanel1.add(jLabelWk1Name2Del);
        jLabelWk1Name2Del.setBounds(780, 100, 30, 30);

        jLabelWk1Name3Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk1Name3Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk1Name3DelMouseClicked(evt);
            }
        });
        jPanel1.add(jLabelWk1Name3Del);
        jLabelWk1Name3Del.setBounds(780, 140, 30, 30);

        jLabelProjectName.setText("Project Name");
        jPanel1.add(jLabelProjectName);
        jLabelProjectName.setBounds(10, 140, 80, 30);

        jComboProjectName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboProjectNameMouseClicked(evt);
            }
        });
        jComboProjectName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboProjectNameActionPerformed(evt);
            }
        });
        jPanel1.add(jComboProjectName);
        jComboProjectName.setBounds(125, 140, 390, 30);

        jLabelProjectTask.setText("Project Task");
        jPanel1.add(jLabelProjectTask);
        jLabelProjectTask.setBounds(10, 190, 80, 30);

        jComboProjectTask.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboProjectTaskMouseClicked(evt);
            }
        });
        jComboProjectTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboProjectTaskActionPerformed(evt);
            }
        });
        jPanel1.add(jComboProjectTask);
        jComboProjectTask.setBounds(125, 190, 390, 30);

        jCheckBoxDialogWk1AccUnProved.setText(" Unproved Acc");
        jPanel1.add(jCheckBoxDialogWk1AccUnProved);
        jCheckBoxDialogWk1AccUnProved.setBounds(670, 270, 130, 21);

        javax.swing.GroupLayout jDialogWk1Layout = new javax.swing.GroupLayout(jDialogWk1.getContentPane());
        jDialogWk1.getContentPane().setLayout(jDialogWk1Layout);
        jDialogWk1Layout.setHorizontalGroup(
            jDialogWk1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
        );
        jDialogWk1Layout.setVerticalGroup(
            jDialogWk1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanelWkOne.setBackground(new java.awt.Color(255, 255, 204));
        jPanelWkOne.setLayout(null);

        jLabelWkDuration.setText("Week Duration");
        jPanelWkOne.add(jLabelWkDuration);
        jLabelWkDuration.setBounds(30, 170, 90, 30);

        jDateChooserWk1From.setDateFormatString("yyyy-MM-dd");
        jPanelWkOne.add(jDateChooserWk1From);
        jDateChooserWk1From.setBounds(210, 170, 120, 30);

        jLabelWk1From.setText("From");
        jPanelWkOne.add(jLabelWk1From);
        jLabelWk1From.setBounds(150, 170, 41, 30);

        jLabelWk1To.setText("To");
        jPanelWkOne.add(jLabelWk1To);
        jLabelWk1To.setBounds(390, 170, 41, 30);

        jDateChooserWk1To.setDateFormatString("yyyy-MM-dd");
        jPanelWkOne.add(jDateChooserWk1To);
        jDateChooserWk1To.setBounds(440, 170, 120, 30);
        jPanelWkOne.add(jSeparator1);
        jSeparator1.setBounds(30, 210, 1280, 10);
        jPanelWkOne.add(jSeparator2);
        jSeparator2.setBounds(30, 160, 1280, 5);

        jLabelLogo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelWkOne.add(jLabelLogo1);
        jLabelLogo1.setBounds(10, 10, 220, 100);

        jButtonWk1DelActivity.setBackground(new java.awt.Color(204, 0, 0));
        jButtonWk1DelActivity.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonWk1DelActivity.setForeground(new java.awt.Color(255, 255, 255));
        jButtonWk1DelActivity.setText("Delete Activity");
        jButtonWk1DelActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk1DelActivityActionPerformed(evt);
            }
        });
        jPanelWkOne.add(jButtonWk1DelActivity);
        jButtonWk1DelActivity.setBounds(1030, 170, 150, 30);

        jLabelProvince1.setText("Province");
        jLabelProvince1.setMinimumSize(new java.awt.Dimension(130, 130));
        jLabelProvince1.setName(""); // NOI18N
        jPanelWkOne.add(jLabelProvince1);
        jLabelProvince1.setBounds(320, 120, 70, 30);

        jLabelOffice1.setText("District");
        jLabelOffice1.setMinimumSize(new java.awt.Dimension(130, 130));
        jLabelOffice1.setName(""); // NOI18N
        jPanelWkOne.add(jLabelOffice1);
        jLabelOffice1.setBounds(820, 120, 70, 30);

        jLabelPlanRefNo.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelPlanRefNo.setText("Plan Ref No.");
        jPanelWkOne.add(jLabelPlanRefNo);
        jLabelPlanRefNo.setBounds(30, 120, 100, 30);
        jPanelWkOne.add(jLabelProvince);
        jLabelProvince.setBounds(440, 120, 320, 30);
        jPanelWkOne.add(jLabelDistrict);
        jLabelDistrict.setBounds(930, 120, 230, 30);

        jTextFieldRefNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldRefNumActionPerformed(evt);
            }
        });
        jPanelWkOne.add(jTextFieldRefNum);
        jTextFieldRefNum.setBounds(190, 120, 70, 30);

        jLabelSerial.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelSerial.setText("P");
        jPanelWkOne.add(jLabelSerial);
        jLabelSerial.setBounds(140, 120, 20, 30);

        jLabelGenLogNam1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam1.setText("Logged In");
        jPanelWkOne.add(jLabelGenLogNam1);
        jLabelGenLogNam1.setBounds(1090, 30, 100, 30);

        jLabelLineLogNam.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkOne.add(jLabelLineLogNam);
        jLabelLineLogNam.setBounds(1180, 30, 160, 30);

        jLabelLineTime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkOne.add(jLabelLineTime);
        jLabelLineTime.setBounds(1250, 0, 80, 30);

        jLabelLineDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkOne.add(jLabelLineDate);
        jLabelLineDate.setBounds(1090, 0, 110, 30);
        jPanelWkOne.add(jLabelEmp);
        jLabelEmp.setBounds(1180, 66, 70, 30);

        jLabelHeaderGen6.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen6.setText("MONTHLY  PLAN ");
        jPanelWkOne.add(jLabelHeaderGen6);
        jLabelHeaderGen6.setBounds(450, 40, 390, 40);

        jLabelPostAppMod1.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jPanelWkOne.add(jLabelPostAppMod1);
        jLabelPostAppMod1.setBounds(480, 80, 240, 30);

        jTableMeetReqWk1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jTableMeetReqWk1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Project", "Date From", "Date To", "Budget Line", "Activity Main Purpose", "Activity Detail", "Item", "Unit Cost", "No. of Persons", "No. of Days", "Line Total", "Pesron Responsible"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane13.setViewportView(jTableMeetReqWk1);

        jPanelWkOne.add(jScrollPane13);
        jScrollPane13.setBounds(30, 500, 1290, 160);

        jButtonWk1DelMeetActivity.setBackground(new java.awt.Color(204, 0, 0));
        jButtonWk1DelMeetActivity.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonWk1DelMeetActivity.setForeground(new java.awt.Color(255, 255, 255));
        jButtonWk1DelMeetActivity.setText("Delete Meeting Activity");
        jButtonWk1DelMeetActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk1DelMeetActivityActionPerformed(evt);
            }
        });
        jPanelWkOne.add(jButtonWk1DelMeetActivity);
        jButtonWk1DelMeetActivity.setBounds(1010, 465, 220, 30);

        jTableWk1Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Branch", "Project Code", "Task Code", "Site to Visit", "Activity", "Justification for Choice of Activity ", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Staff Member 1", "Staff Member 2", "Staff Member 3", "Staff Member 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableWk1Activities);

        jPanelWkOne.add(jScrollPane1);
        jScrollPane1.setBounds(30, 220, 1290, 240);

        jButtonWk1AddActivity.setBackground(new java.awt.Color(0, 153, 51));
        jButtonWk1AddActivity.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonWk1AddActivity.setForeground(new java.awt.Color(255, 255, 255));
        jButtonWk1AddActivity.setText("Add Activity");
        jButtonWk1AddActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk1AddActivityActionPerformed(evt);
            }
        });
        jPanelWkOne.add(jButtonWk1AddActivity);
        jButtonWk1AddActivity.setBounds(750, 165, 150, 30);

        jTabbedPaneMain.addTab("Week One", jPanelWkOne);

        jPanelWkTwo.setBackground(new java.awt.Color(95, 222, 184));
        jPanelWkTwo.setLayout(null);

        jLabelWkDuration1.setText("Week Duration");
        jPanelWkTwo.add(jLabelWkDuration1);
        jLabelWkDuration1.setBounds(30, 160, 90, 30);

        jDateChooserWk2From.setDateFormatString("yyyy-MM-dd");
        jPanelWkTwo.add(jDateChooserWk2From);
        jDateChooserWk2From.setBounds(210, 160, 120, 30);

        jLabelWk1From1.setText("From");
        jPanelWkTwo.add(jLabelWk1From1);
        jLabelWk1From1.setBounds(150, 160, 41, 30);

        jLabelWk1To1.setText("To");
        jPanelWkTwo.add(jLabelWk1To1);
        jLabelWk1To1.setBounds(390, 160, 41, 30);

        jDateChooserWk2To.setDateFormatString("yyyy-MM-dd");
        jPanelWkTwo.add(jDateChooserWk2To);
        jDateChooserWk2To.setBounds(440, 160, 120, 30);
        jPanelWkTwo.add(jSeparator4);
        jSeparator4.setBounds(30, 200, 1280, 10);
        jPanelWkTwo.add(jSeparator5);
        jSeparator5.setBounds(30, 150, 1280, 10);

        jLabelLogo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelWkTwo.add(jLabelLogo2);
        jLabelLogo2.setBounds(10, 10, 220, 100);

        jButtonWk2DelActivity.setBackground(new java.awt.Color(204, 0, 0));
        jButtonWk2DelActivity.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonWk2DelActivity.setForeground(new java.awt.Color(255, 255, 255));
        jButtonWk2DelActivity.setText("Delete Activity");
        jButtonWk2DelActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk2DelActivityActionPerformed(evt);
            }
        });
        jPanelWkTwo.add(jButtonWk2DelActivity);
        jButtonWk2DelActivity.setBounds(1030, 160, 150, 30);

        jLabelLineTime1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkTwo.add(jLabelLineTime1);
        jLabelLineTime1.setBounds(1250, 0, 80, 30);

        jLabelLineDate1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkTwo.add(jLabelLineDate1);
        jLabelLineDate1.setBounds(1090, 0, 110, 30);

        jLabelGenLogNam3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam3.setText("Logged In");
        jPanelWkTwo.add(jLabelGenLogNam3);
        jLabelGenLogNam3.setBounds(1090, 30, 100, 30);

        jLabelLineLogNam1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam1.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkTwo.add(jLabelLineLogNam1);
        jLabelLineLogNam1.setBounds(1180, 30, 160, 30);

        jLabelHeaderGen7.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen7.setText("MONTHLY  PLAN ");
        jPanelWkTwo.add(jLabelHeaderGen7);
        jLabelHeaderGen7.setBounds(450, 40, 420, 40);

        jLabelPostAppMod2.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jPanelWkTwo.add(jLabelPostAppMod2);
        jLabelPostAppMod2.setBounds(480, 80, 240, 30);

        jButtonWk2DelMeetActivity.setBackground(new java.awt.Color(204, 0, 0));
        jButtonWk2DelMeetActivity.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonWk2DelMeetActivity.setForeground(new java.awt.Color(255, 255, 255));
        jButtonWk2DelMeetActivity.setText("Delete Meeting Activity");
        jButtonWk2DelMeetActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk2DelMeetActivityActionPerformed(evt);
            }
        });
        jPanelWkTwo.add(jButtonWk2DelMeetActivity);
        jButtonWk2DelMeetActivity.setBounds(1020, 465, 200, 30);

        jTableMeetReqWk2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jTableMeetReqWk2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Project", "Date From", "Date To", "Budget Line", "Activity Main Purpose", "Activity Detail", "Item", "Unit Cost", "No. of Persons", "No. of Days", "Line Total", "Pesron Responsible"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane14.setViewportView(jTableMeetReqWk2);

        jPanelWkTwo.add(jScrollPane14);
        jScrollPane14.setBounds(30, 500, 1290, 160);

        jTableWk2Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Branch", "Project Code", "Task Code", "Site to Visit", "Activity", "Justification for Choice of Activity ", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Staff Member 1", "Staff Member 2", "Staff Member 3", "Staff Member 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane12.setViewportView(jTableWk2Activities);

        jPanelWkTwo.add(jScrollPane12);
        jScrollPane12.setBounds(30, 220, 1290, 240);

        jButtonWk2AddActivity.setBackground(new java.awt.Color(0, 153, 51));
        jButtonWk2AddActivity.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonWk2AddActivity.setForeground(new java.awt.Color(255, 255, 255));
        jButtonWk2AddActivity.setText("Add Activity");
        jButtonWk2AddActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk2AddActivityActionPerformed(evt);
            }
        });
        jPanelWkTwo.add(jButtonWk2AddActivity);
        jButtonWk2AddActivity.setBounds(820, 160, 150, 30);

        jTabbedPaneMain.addTab("Week Two", jPanelWkTwo);

        jPanelWkThree.setBackground(new java.awt.Color(29, 109, 222));
        jPanelWkThree.setLayout(null);

        jLabelWkDuration2.setText("Week Duration");
        jPanelWkThree.add(jLabelWkDuration2);
        jLabelWkDuration2.setBounds(30, 160, 90, 30);

        jDateChooserWk3From.setDateFormatString("yyyy-MM-dd");
        jPanelWkThree.add(jDateChooserWk3From);
        jDateChooserWk3From.setBounds(210, 160, 120, 30);

        jLabelWk3From.setText("From");
        jPanelWkThree.add(jLabelWk3From);
        jLabelWk3From.setBounds(150, 160, 41, 30);

        jLabelWk3To.setText("To");
        jPanelWkThree.add(jLabelWk3To);
        jLabelWk3To.setBounds(390, 160, 41, 30);

        jDateChooserWk3To.setDateFormatString("yyyy-MM-dd");
        jPanelWkThree.add(jDateChooserWk3To);
        jDateChooserWk3To.setBounds(440, 160, 120, 30);
        jPanelWkThree.add(jSeparator7);
        jSeparator7.setBounds(30, 200, 1280, 10);
        jPanelWkThree.add(jSeparator8);
        jSeparator8.setBounds(30, 150, 1280, 10);

        jLabelLogo3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelWkThree.add(jLabelLogo3);
        jLabelLogo3.setBounds(10, 10, 220, 100);

        jButtonWk3DelActivity.setBackground(new java.awt.Color(204, 0, 0));
        jButtonWk3DelActivity.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonWk3DelActivity.setForeground(new java.awt.Color(255, 255, 255));
        jButtonWk3DelActivity.setText("Delete Activity");
        jButtonWk3DelActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk3DelActivityActionPerformed(evt);
            }
        });
        jPanelWkThree.add(jButtonWk3DelActivity);
        jButtonWk3DelActivity.setBounds(1030, 160, 150, 30);

        jLabelLineDate2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkThree.add(jLabelLineDate2);
        jLabelLineDate2.setBounds(1090, 0, 110, 30);

        jLabelLineTime2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkThree.add(jLabelLineTime2);
        jLabelLineTime2.setBounds(1250, 0, 80, 30);

        jLabelLineLogNam2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkThree.add(jLabelLineLogNam2);
        jLabelLineLogNam2.setBounds(1180, 30, 160, 30);

        jLabelGenLogNam5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam5.setForeground(new java.awt.Color(255, 255, 255));
        jLabelGenLogNam5.setText("Logged In");
        jPanelWkThree.add(jLabelGenLogNam5);
        jLabelGenLogNam5.setBounds(1090, 30, 100, 30);

        jLabelHeaderGen4.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen4.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeaderGen4.setText("MONTHLY  PLAN ");
        jPanelWkThree.add(jLabelHeaderGen4);
        jLabelHeaderGen4.setBounds(450, 40, 420, 40);

        jLabelPostAppMod3.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabelPostAppMod3.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkThree.add(jLabelPostAppMod3);
        jLabelPostAppMod3.setBounds(480, 80, 240, 30);

        jButtonWk3DelMeetActivity.setBackground(new java.awt.Color(204, 0, 0));
        jButtonWk3DelMeetActivity.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonWk3DelMeetActivity.setForeground(new java.awt.Color(255, 255, 255));
        jButtonWk3DelMeetActivity.setText("Delete Meeting Activity");
        jButtonWk3DelMeetActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk3DelMeetActivityActionPerformed(evt);
            }
        });
        jPanelWkThree.add(jButtonWk3DelMeetActivity);
        jButtonWk3DelMeetActivity.setBounds(1020, 465, 200, 30);

        jTableMeetReqWk3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jTableMeetReqWk3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Project", "Date From", "Date To", "Budget Line", "Activity Main Purpose", "Activity Detail", "Item", "Unit Cost", "No. of Persons", "No. of Days", "Line Total", "Pesron Responsible"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane15.setViewportView(jTableMeetReqWk3);

        jPanelWkThree.add(jScrollPane15);
        jScrollPane15.setBounds(30, 500, 1290, 160);

        jTableWk3Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Branch", "Project Code", "Task Code", "Site to Visit", "Activity", "Justification for Choice of Activity ", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Staff Member 1", "Staff Member 2", "Staff Member 3", "Staff Member 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane23.setViewportView(jTableWk3Activities);

        jPanelWkThree.add(jScrollPane23);
        jScrollPane23.setBounds(30, 220, 1290, 240);

        jButtonWk3AddActivity.setBackground(new java.awt.Color(0, 153, 51));
        jButtonWk3AddActivity.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonWk3AddActivity.setForeground(new java.awt.Color(255, 255, 255));
        jButtonWk3AddActivity.setText("Add Activity");
        jButtonWk3AddActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk3AddActivityActionPerformed(evt);
            }
        });
        jPanelWkThree.add(jButtonWk3AddActivity);
        jButtonWk3AddActivity.setBounds(810, 160, 150, 30);

        jTabbedPaneMain.addTab("Week Three", jPanelWkThree);

        jPanelWkFour.setBackground(new java.awt.Color(255, 204, 204));
        jPanelWkFour.setLayout(null);

        jLabelWkDuration4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWkDuration4.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWkDuration4.setText("Week Duration");
        jPanelWkFour.add(jLabelWkDuration4);
        jLabelWkDuration4.setBounds(30, 160, 100, 30);

        jDateChooserWk4From.setDateFormatString("yyyy-MM-dd");
        jPanelWkFour.add(jDateChooserWk4From);
        jDateChooserWk4From.setBounds(210, 160, 120, 30);

        jLabelWk4From.setText("From");
        jPanelWkFour.add(jLabelWk4From);
        jLabelWk4From.setBounds(150, 160, 41, 30);

        jLabelWk4To.setText("To");
        jPanelWkFour.add(jLabelWk4To);
        jLabelWk4To.setBounds(390, 160, 41, 30);

        jDateChooserWk4To.setDateFormatString("yyyy-MM-dd");
        jPanelWkFour.add(jDateChooserWk4To);
        jDateChooserWk4To.setBounds(440, 160, 120, 30);
        jPanelWkFour.add(jSeparator41);
        jSeparator41.setBounds(30, 200, 1280, 10);
        jPanelWkFour.add(jSeparator42);
        jSeparator42.setBounds(30, 150, 1280, 10);

        jLabelLogo5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelWkFour.add(jLabelLogo5);
        jLabelLogo5.setBounds(10, 10, 220, 100);

        jButtonWk4DelActivity.setBackground(new java.awt.Color(204, 0, 0));
        jButtonWk4DelActivity.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonWk4DelActivity.setForeground(new java.awt.Color(255, 255, 255));
        jButtonWk4DelActivity.setText("Delete Activity");
        jButtonWk4DelActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk4DelActivityActionPerformed(evt);
            }
        });
        jPanelWkFour.add(jButtonWk4DelActivity);
        jButtonWk4DelActivity.setBounds(1030, 160, 150, 30);

        jLabelGenLogNam7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam7.setForeground(new java.awt.Color(255, 255, 255));
        jLabelGenLogNam7.setText("Logged In");
        jPanelWkFour.add(jLabelGenLogNam7);
        jLabelGenLogNam7.setBounds(1090, 30, 100, 30);

        jLabelLineLogNam4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam4.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFour.add(jLabelLineLogNam4);
        jLabelLineLogNam4.setBounds(1180, 30, 160, 30);

        jLabelLineTime4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime4.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFour.add(jLabelLineTime4);
        jLabelLineTime4.setBounds(1250, 0, 80, 30);

        jLabelLineDate4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate4.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFour.add(jLabelLineDate4);
        jLabelLineDate4.setBounds(1090, 0, 110, 30);

        jLabelHeaderGen9.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen9.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeaderGen9.setText("MONTHLY  PLAN ");
        jPanelWkFour.add(jLabelHeaderGen9);
        jLabelHeaderGen9.setBounds(450, 40, 420, 40);

        jLabelPostAppMod4.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabelPostAppMod4.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFour.add(jLabelPostAppMod4);
        jLabelPostAppMod4.setBounds(480, 80, 240, 30);

        jButtonWk4DelMeetActivity.setBackground(new java.awt.Color(204, 0, 0));
        jButtonWk4DelMeetActivity.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonWk4DelMeetActivity.setForeground(new java.awt.Color(255, 255, 255));
        jButtonWk4DelMeetActivity.setText("Delete Meeting Activity");
        jButtonWk4DelMeetActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk4DelMeetActivityActionPerformed(evt);
            }
        });
        jPanelWkFour.add(jButtonWk4DelMeetActivity);
        jButtonWk4DelMeetActivity.setBounds(1020, 465, 200, 30);

        jTableMeetReqWk4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jTableMeetReqWk4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Project", "Date From", "Date To", "Budget Line", "Activity Main Purpose", "Activity Detail", "Item", "Unit Cost", "No. of Persons", "No. of Days", "Line Total", "Pesron Responsible"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane16.setViewportView(jTableMeetReqWk4);

        jPanelWkFour.add(jScrollPane16);
        jScrollPane16.setBounds(30, 500, 1290, 160);

        jTableWk4Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Branch", "Project Code", "Task Code", "Site to Visit", "Activity", "Justification for Choice of Activity ", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Staff Member 1", "Staff Member 2", "Staff Member 3", "Staff Member 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane24.setViewportView(jTableWk4Activities);

        jPanelWkFour.add(jScrollPane24);
        jScrollPane24.setBounds(30, 220, 1290, 240);

        jButtonWk4AddActivity.setBackground(new java.awt.Color(0, 153, 51));
        jButtonWk4AddActivity.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonWk4AddActivity.setForeground(new java.awt.Color(255, 255, 255));
        jButtonWk4AddActivity.setText("Add Activity");
        jButtonWk4AddActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk4AddActivityActionPerformed(evt);
            }
        });
        jPanelWkFour.add(jButtonWk4AddActivity);
        jButtonWk4AddActivity.setBounds(820, 160, 150, 30);

        jTabbedPaneMain.addTab("Week Four", jPanelWkFour);

        jPanelWkFive.setBackground(new java.awt.Color(204, 204, 255));
        jPanelWkFive.setLayout(null);

        jLabelWkDuration5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWkDuration5.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWkDuration5.setText("Week Duration");
        jPanelWkFive.add(jLabelWkDuration5);
        jLabelWkDuration5.setBounds(30, 160, 100, 30);

        jDateChooserWk5From.setDateFormatString("yyyy-MM-dd");
        jPanelWkFive.add(jDateChooserWk5From);
        jDateChooserWk5From.setBounds(210, 160, 120, 30);

        jLabelWk5From.setText("From");
        jPanelWkFive.add(jLabelWk5From);
        jLabelWk5From.setBounds(150, 160, 41, 30);

        jLabelWk5To.setText("To");
        jPanelWkFive.add(jLabelWk5To);
        jLabelWk5To.setBounds(390, 160, 41, 30);

        jDateChooserWk5To.setDateFormatString("yyyy-MM-dd");
        jPanelWkFive.add(jDateChooserWk5To);
        jDateChooserWk5To.setBounds(440, 160, 120, 30);
        jPanelWkFive.add(jSeparator43);
        jSeparator43.setBounds(30, 200, 1280, 10);
        jPanelWkFive.add(jSeparator44);
        jSeparator44.setBounds(30, 150, 1280, 10);

        jLabelLogo6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelWkFive.add(jLabelLogo6);
        jLabelLogo6.setBounds(10, 10, 220, 100);

        jButtonWk5DelActivity.setBackground(new java.awt.Color(204, 0, 0));
        jButtonWk5DelActivity.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonWk5DelActivity.setForeground(new java.awt.Color(255, 255, 255));
        jButtonWk5DelActivity.setText("Delete Activity");
        jButtonWk5DelActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk5DelActivityActionPerformed(evt);
            }
        });
        jPanelWkFive.add(jButtonWk5DelActivity);
        jButtonWk5DelActivity.setBounds(1030, 160, 150, 30);

        jLabelGenLogNam8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam8.setForeground(new java.awt.Color(255, 255, 255));
        jLabelGenLogNam8.setText("Logged In");
        jPanelWkFive.add(jLabelGenLogNam8);
        jLabelGenLogNam8.setBounds(1090, 30, 100, 30);

        jLabelLineLogNam5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam5.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFive.add(jLabelLineLogNam5);
        jLabelLineLogNam5.setBounds(1180, 30, 160, 30);

        jLabelLineTime5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime5.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFive.add(jLabelLineTime5);
        jLabelLineTime5.setBounds(1250, 0, 80, 30);

        jLabelLineDate5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate5.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFive.add(jLabelLineDate5);
        jLabelLineDate5.setBounds(1090, 0, 110, 30);

        jLabelHeaderGen10.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen10.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeaderGen10.setText("MONTHLY  PLAN ");
        jPanelWkFive.add(jLabelHeaderGen10);
        jLabelHeaderGen10.setBounds(450, 40, 420, 40);

        jLabelPostAppMod5.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jLabelPostAppMod5.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFive.add(jLabelPostAppMod5);
        jLabelPostAppMod5.setBounds(480, 80, 240, 30);

        jButtonWk5DelMeetActivity.setBackground(new java.awt.Color(204, 0, 0));
        jButtonWk5DelMeetActivity.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonWk5DelMeetActivity.setForeground(new java.awt.Color(255, 255, 255));
        jButtonWk5DelMeetActivity.setText("Delete Meeting Activity");
        jButtonWk5DelMeetActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk5DelMeetActivityActionPerformed(evt);
            }
        });
        jPanelWkFive.add(jButtonWk5DelMeetActivity);
        jButtonWk5DelMeetActivity.setBounds(1020, 465, 200, 30);

        jTableMeetReqWk5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jTableMeetReqWk5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Project", "Date From", "Date To", "Budget Line", "Activity Main Purpose", "Activity Detail", "Item", "Unit Cost", "No. of Persons", "No. of Days", "Line Total", "Pesron Responsible"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane17.setViewportView(jTableMeetReqWk5);

        jPanelWkFive.add(jScrollPane17);
        jScrollPane17.setBounds(30, 500, 1290, 160);

        jTableWk5Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Branch", "Project Code", "Task Code", "Site to Visit", "Activity", "Justification for Choice of Activity ", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Staff Member 1", "Staff Member 2", "Staff Member 3", "Staff Member 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane25.setViewportView(jTableWk5Activities);

        jPanelWkFive.add(jScrollPane25);
        jScrollPane25.setBounds(30, 220, 1290, 240);

        jButtonWk5AddActivity.setBackground(new java.awt.Color(0, 153, 51));
        jButtonWk5AddActivity.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonWk5AddActivity.setForeground(new java.awt.Color(255, 255, 255));
        jButtonWk5AddActivity.setText("Add Activity");
        jButtonWk5AddActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk5AddActivityActionPerformed(evt);
            }
        });
        jPanelWkFive.add(jButtonWk5AddActivity);
        jButtonWk5AddActivity.setBounds(820, 160, 150, 30);

        jTabbedPaneMain.addTab("Week Five", jPanelWkFive);

        jPanelWkComments.setBackground(new java.awt.Color(209, 54, 127));
        jPanelWkComments.setLayout(null);
        jPanelWkComments.add(jSeparator35);
        jSeparator35.setBounds(30, 150, 1280, 10);

        jLabelLogo4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelWkComments.add(jLabelLogo4);
        jLabelLogo4.setBounds(10, 10, 220, 100);

        jLabelLineLogNam3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam3.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkComments.add(jLabelLineLogNam3);
        jLabelLineLogNam3.setBounds(1180, 30, 160, 30);

        jLabelGenLogNam6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam6.setForeground(new java.awt.Color(255, 255, 255));
        jLabelGenLogNam6.setText("Logged In");
        jPanelWkComments.add(jLabelGenLogNam6);
        jLabelGenLogNam6.setBounds(1090, 30, 100, 30);

        jLabelLineDate3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate3.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkComments.add(jLabelLineDate3);
        jLabelLineDate3.setBounds(1090, 0, 110, 30);

        jLabelLineTime3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime3.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkComments.add(jLabelLineTime3);
        jLabelLineTime3.setBounds(1250, 0, 80, 30);

        jLabelHeaderGen8.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen8.setText("MONTHLY  PLAN ");
        jPanelWkComments.add(jLabelHeaderGen8);
        jLabelHeaderGen8.setBounds(450, 40, 420, 40);

        jLabelCommentsHeading.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelCommentsHeading.setForeground(new java.awt.Color(255, 255, 255));
        jLabelCommentsHeading.setText("Areas that require your attention ");
        jPanelWkComments.add(jLabelCommentsHeading);
        jLabelCommentsHeading.setBounds(30, 160, 590, 40);

        jTextAreaComments.setColumns(20);
        jTextAreaComments.setRows(5);
        jScrollPane7.setViewportView(jTextAreaComments);

        jPanelWkComments.add(jScrollPane7);
        jScrollPane7.setBounds(30, 220, 780, 280);
        jPanelWkComments.add(jSeparator36);
        jSeparator36.setBounds(30, 200, 1280, 10);

        jTabbedPaneMain.addTab("Comments", jPanelWkComments);

        jMenuFile.setText("File");
        jMenuFile.setPreferredSize(new java.awt.Dimension(60, 19));
        jMenuFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuFileActionPerformed(evt);
            }
        });

        jMenuNew.setText("New");

        jMenuItemRequest.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemRequest.setText("Perdiem Request");
        jMenuItemRequest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRequestActionPerformed(evt);
            }
        });
        jMenuNew.add(jMenuItemRequest);
        jMenuNew.add(jSeparator27);

        jMenuItemAcquittal.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemAcquittal.setText("Perdiem Acquittal");
        jMenuItemAcquittal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcquittalActionPerformed(evt);
            }
        });
        jMenuNew.add(jMenuItemAcquittal);
        jMenuNew.add(jSeparator14);

        jMenuItemSpecialAcquittal.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSpecialAcquittal.setText("Perdiem Acquittal - Special");
        jMenuItemSpecialAcquittal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSpecialAcquittalActionPerformed(evt);
            }
        });
        jMenuNew.add(jMenuItemSpecialAcquittal);
        jMenuNew.add(jSeparator19);

        jMenuItemRequestMOH.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemRequestMOH.setText("Acquittal - MOHCC Personnel");
        jMenuItemRequestMOH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRequestMOHActionPerformed(evt);
            }
        });
        jMenuNew.add(jMenuItemRequestMOH);
        jMenuNew.add(jSeparator28);

        jMenuItemMeetAcquittal.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemMeetAcquittal.setText("Acquittal - Participants Allowances");
        jMenuItemMeetAcquittal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemMeetAcquittalActionPerformed(evt);
            }
        });
        jMenuNew.add(jMenuItemMeetAcquittal);
        jMenuNew.add(jSeparator39);

        jMenuItem3WkPlan.setText("Monthly Plan");
        jMenuItem3WkPlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3WkPlanActionPerformed(evt);
            }
        });
        jMenuNew.add(jMenuItem3WkPlan);

        jMenuFile.add(jMenuNew);
        jMenuFile.add(jSeparator40);

        jMenuEdit.setText("Edit");

        jMenuReqEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuReqEdit.setText("Request");
        jMenuReqEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuReqEditActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuReqEdit);
        jMenuEdit.add(jSeparator29);

        jMenuAcqEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuAcqEdit.setText("Acquittal");
        jMenuAcqEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuAcqEditActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuAcqEdit);
        jMenuEdit.add(jSeparator30);

        jMenuMonPlanEdit.setText("Monthly Plan");
        jMenuMonPlanEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuMonPlanEditActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuMonPlanEdit);

        jMenuFile.add(jMenuEdit);
        jMenuFile.add(jSeparator47);

        jMenuItemClose1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemClose1.setText("Modify");
        jMenuItemClose1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemClose1ActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemClose1);
        jMenuFile.add(jSeparator55);

        jMenuItemClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemClose.setText("Close");
        jMenuItemClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemClose);
        jMenuFile.add(jSeparator48);

        jMenuItemExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemExit.setText("Exit");
        jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExitActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemExit);

        jMenuBar.add(jMenuFile);

        jMenuRequest.setText("Request");
        jMenuRequest.setPreferredSize(new java.awt.Dimension(80, 19));

        jMenuItemSupApp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSupApp.setText("Supervisor Approval");
        jMenuItemSupApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSupAppActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemSupApp);
        jMenuRequest.add(jSeparator51);

        jMenuItemAccMgrRev.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemAccMgrRev.setText("Account Manager Verification");
        jMenuItemAccMgrRev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAccMgrRevActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemAccMgrRev);
        jMenuRequest.add(jSeparator52);

        jMenuItemHeadApp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemHeadApp.setText("Head Approval");
        jMenuItemHeadApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemHeadAppActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemHeadApp);
        jMenuRequest.add(jSeparator53);

        jMenuPlanApproval.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuPlanApproval.setText("Central Finance Payment ");
        jMenuPlanApproval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuPlanApprovalActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuPlanApproval);
        jMenuRequest.add(jSeparator54);

        jMenuItemSchGen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSchGen.setText("Schedule Generation");
        jMenuItemSchGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSchGenActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemSchGen);
        jMenuRequest.add(jSeparator13);

        jMenuItemView.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemView.setText("View Request");
        jMenuItemView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemViewActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemView);

        jMenuBar.add(jMenuRequest);

        jMenuAcquittal.setText("Acquittal");
        jMenuAcquittal.setPreferredSize(new java.awt.Dimension(80, 19));

        jMenuItemAcqSupApp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        jMenuItemAcqSupApp.setText("Supervisor Approval");
        jMenuItemAcqSupApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqSupAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqSupApp);
        jMenuAcquittal.add(jSeparator22);

        jMenuItemAcqAccApp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        jMenuItemAcqAccApp.setText("Account Manager Verification");
        jMenuItemAcqAccApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqAccAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqAccApp);
        jMenuAcquittal.add(jSeparator23);

        jMenuItemAcqHeadApp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        jMenuItemAcqHeadApp.setText("Head Approval");
        jMenuItemAcqHeadApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqHeadAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqHeadApp);
        jMenuAcquittal.add(jSeparator24);

        jMenuItemAcqFinApp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        jMenuItemAcqFinApp.setText("Central Finance Payment ");
        jMenuItemAcqFinApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqFinAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqFinApp);
        jMenuAcquittal.add(jSeparator25);

        jMenuItemAcqSchGen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        jMenuItemAcqSchGen.setText("Acquittals Schedule Generation");
        jMenuItemAcqSchGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqSchGenActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqSchGen);
        jMenuAcquittal.add(jSeparator26);

        jMenuItemAcqView.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        jMenuItemAcqView.setText("View Acquittal");
        jMenuItemAcqView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqViewActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqView);

        jMenuBar.add(jMenuAcquittal);

        jMenuMonthlyPlan.setText("Monthly Plan");
        jMenuMonthlyPlan.setPreferredSize(new java.awt.Dimension(105, 19));

        jMenuItemProvMgrApproval.setText("Provincial Mgr Approval");
        jMenuItemProvMgrApproval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemProvMgrApprovalActionPerformed(evt);
            }
        });
        jMenuMonthlyPlan.add(jMenuItemProvMgrApproval);
        jMenuMonthlyPlan.add(jSeparator49);

        jMenuItemReqGenLst.setText("Request Generation list");
        jMenuItemReqGenLst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemReqGenLstActionPerformed(evt);
            }
        });
        jMenuMonthlyPlan.add(jMenuItemReqGenLst);
        jMenuMonthlyPlan.add(jSeparator34);

        jMenuItemMeetGenLst.setText("Meetings Generation list");
        jMenuItemMeetGenLst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemMeetGenLstActionPerformed(evt);
            }
        });
        jMenuMonthlyPlan.add(jMenuItemMeetGenLst);
        jMenuMonthlyPlan.add(jSeparator50);

        jMenuItemPlanView.setText("View Plan");
        jMenuItemPlanView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPlanViewActionPerformed(evt);
            }
        });
        jMenuMonthlyPlan.add(jMenuItemPlanView);

        jMenuBar.add(jMenuMonthlyPlan);

        jMenuReports.setText("Reports");
        jMenuReports.setPreferredSize(new java.awt.Dimension(59, 19));

        jMenuItemReqSubmitted.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemReqSubmitted.setText("Staff Perdiem Requests ");
        jMenuItemReqSubmitted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemReqSubmittedActionPerformed(evt);
            }
        });
        jMenuReports.add(jMenuItemReqSubmitted);
        jMenuReports.add(jSeparator31);

        jMenuAgeAlluser.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuAgeAlluser.setText("Age Analysis All Users");
        jMenuAgeAlluser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuAgeAlluserActionPerformed(evt);
            }
        });
        jMenuReports.add(jMenuAgeAlluser);
        jMenuReports.add(jSeparator32);

        jMenuAgeByUser.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuAgeByUser.setText("Age Analysis by User");
        jMenuAgeByUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuAgeByUserActionPerformed(evt);
            }
        });
        jMenuReports.add(jMenuAgeByUser);
        jMenuReports.add(jSeparator33);

        jMenuProcessTimeline.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuProcessTimeline.setText("Request Process Timeline");
        jMenuProcessTimeline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuProcessTimelineActionPerformed(evt);
            }
        });
        jMenuReports.add(jMenuProcessTimeline);

        jMenuBar.add(jMenuReports);

        setJMenuBar(jMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneMain)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneMain)
        );

        setSize(new java.awt.Dimension(1376, 760));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelBudgetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelBudgetActionPerformed
        jDialogBudget.setVisible(false);
    }//GEN-LAST:event_jButtonCancelBudgetActionPerformed

    private void jButtonOkFacility1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkFacility1ActionPerformed
        if ("Y".equals(meetBudWk1)) {
            jTextFieldBudgetWk1.setText(jComboBudgetCode.getSelectedItem().toString());
        } else if ("Y".equals(meetBudWk2)) {
            jTextFieldBudgetWk2.setText(jComboBudgetCode.getSelectedItem().toString());
        } else if ("Y".equals(meetBudWk3)) {
            jTextFieldBudgetWk3.setText(jComboBudgetCode.getSelectedItem().toString());
        } else if ("Y".equals(meetBudWk4)) {
            jTextFieldBudgetWk4.setText(jComboBudgetCode.getSelectedItem().toString());
        } else if ("Y".equals(meetBudWk5)) {
            jTextFieldBudgetWk5.setText(jComboBudgetCode.getSelectedItem().toString());
        }
       
        jDialogBudget.setVisible(false);
    }//GEN-LAST:event_jButtonOkFacility1ActionPerformed

    private void jComboBudgetCodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBudgetCodeFocusGained
//        try {
//
//            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
//                + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
//
//            Statement st = conn.createStatement();
//
//            int itemCount = jComboBudgetCode.getItemCount();
//
//            for (int i = 0; i < itemCount; i++) {
//                jComboBudgetCode.removeItemAt(0);
//            }
//            //jComboBudgetCode.setSelectedIndex(-1);
//            String offnam = jLabelEmpOffice.getText();
//            String provnam = jLabelEmpProvince.getText();
//            st.executeQuery("SELECT BudDesc"
//                + "  FROM [ClaimsAppSysZvandiri].[dbo].[BudCodeTab] WHERE (Province ='" + provnam + "'"
//                + "and Office='" + offnam + "' and BudDesc is not Null) order by 1");
//            ResultSet r = st.getResultSet();
//
//            while (r.next()) {
//
//                jComboBudgetCode.addItem(r.getString("BudDesc"));
//                // jComboBudgetCode.addItem(r.getString("Desc1"));
//            }
//
//            conn.close();
//        } catch (Exception e) {
//            //  JOptionPane.showMessageDialog(null, "Failed to Connect to Database(Budget code)", "Error Connection", JOptionPane.WARNING_MESSAGE);
//            //            System.exit(0);
//        }
    }//GEN-LAST:event_jComboBudgetCodeFocusGained

    private void jComboBudgetCodeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBudgetCodeMouseEntered
        try {

                Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
     

            Statement st = conn.createStatement();

            int itemCount = jComboBudgetCode.getItemCount();

            for (int i = 0; i < itemCount; i++) {
                jComboBudgetCode.removeItemAt(0);
            }
            jComboBudgetCode.setSelectedIndex(-1);
            String offnam = jLabelDistrict.getText();
            String provnam = jLabelProvince.getText();
            st.executeQuery("SELECT BudDesc"
                    + "  FROM [ClaimsAppSysZvandiri].[dbo].[BudCodeTab] WHERE (Province ='" + provnam + "'"
                    + "and Office='" + offnam + "')");
            ResultSet r = st.getResultSet();

            while (r.next()) {

                jComboBudgetCode.addItem(r.getString("BudDesc"));
                // jComboBudgetCode.addItem(r.getString("Desc1"));
            }

            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Connect to Database(Budget code)", "Error Connection", JOptionPane.WARNING_MESSAGE);
            //            System.exit(0);
        }
    }//GEN-LAST:event_jComboBudgetCodeMouseEntered

    private void jButtonWk1DelActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk1DelActivityActionPerformed
        DefaultTableModel model = (DefaultTableModel) this.jTableWk1Activities.getModel();
        int selectedOption = JOptionPane.showConfirmDialog(jTableWk1Activities,
                "Do you want to delete selected activity line?",
                "Choose",
                JOptionPane.YES_NO_OPTION);
        if (selectedOption == JOptionPane.YES_OPTION) {

            int[] rows = jTableWk1Activities.getSelectedRows();
            for (int i = 0; i < rows.length; i++) {
                model.removeRow(rows[i] - i);
            }
        } else {
            jButtonWk1DelActivity.requestFocusInWindow();
            jButtonWk1DelActivity.setFocusable(true);
        }
    }//GEN-LAST:event_jButtonWk1DelActivityActionPerformed

    private void jButtonWk2DelActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk2DelActivityActionPerformed
        DefaultTableModel modelWk2 = (DefaultTableModel) this.jTableWk2Activities.getModel();
        int selectedOption = JOptionPane.showConfirmDialog(jTableWk2Activities,
                "Do you want to delete selected activity line?",
                "Choose",
                JOptionPane.YES_NO_OPTION);
        if (selectedOption == JOptionPane.YES_OPTION) {

            int[] rows = jTableWk2Activities.getSelectedRows();
            for (int i = 0; i < rows.length; i++) {
                modelWk2.removeRow(rows[i] - i);
            }
        } else {
            jButtonWk2DelActivity.requestFocusInWindow();
            jButtonWk2DelActivity.setFocusable(true);
        }
    }//GEN-LAST:event_jButtonWk2DelActivityActionPerformed

    private void jButtonWk3DelActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk3DelActivityActionPerformed
        DefaultTableModel modelWk3 = (DefaultTableModel) this.jTableWk3Activities.getModel();
        int selectedOption = JOptionPane.showConfirmDialog(jTableWk3Activities,
                "Do you want to delete selected activity line?",
                "Choose",
                JOptionPane.YES_NO_OPTION);
        if (selectedOption == JOptionPane.YES_OPTION) {

            int[] rows = jTableWk3Activities.getSelectedRows();
            for (int i = 0; i < rows.length; i++) {
                modelWk3.removeRow(rows[i] - i);
            }
        } else {
            jButtonWk3DelActivity.requestFocusInWindow();
            jButtonWk3DelActivity.setFocusable(true);
        }
    }//GEN-LAST:event_jButtonWk3DelActivityActionPerformed

    private void jTextFieldRefNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldRefNumActionPerformed
        clearFields();
        regInitCheck();

    }//GEN-LAST:event_jTextFieldRefNumActionPerformed

    private void jMenuItemAcqSupAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAcqSupAppActionPerformed
        new JFrameSupAcqList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemAcqSupAppActionPerformed

    private void jMenuItemAcqAccAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAcqAccAppActionPerformed
        new JFrameAccAcqList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemAcqAccAppActionPerformed

    private void jMenuItemAcqHeadAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAcqHeadAppActionPerformed
        new JFrameHeadAcqList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemAcqHeadAppActionPerformed

    private void jMenuItemAcqFinAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAcqFinAppActionPerformed
       
    }//GEN-LAST:event_jMenuItemAcqFinAppActionPerformed

    private void jMenuItemAcqSchGenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAcqSchGenActionPerformed

        new JFrameReqHQAcqScheduleApp(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemAcqSchGenActionPerformed

    private void jMenuItemAcqViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAcqViewActionPerformed
        new JFrameAppAcquittalView(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemAcqViewActionPerformed

    private void jMenuItemReqSubmittedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReqSubmittedActionPerformed
        new JFrameReqAllUserList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemReqSubmittedActionPerformed

    private void jMenuAgeAlluserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuAgeAlluserActionPerformed
        
    }//GEN-LAST:event_jMenuAgeAlluserActionPerformed

    private void jMenuAgeByUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuAgeByUserActionPerformed
       
    }//GEN-LAST:event_jMenuAgeByUserActionPerformed

    private void jMenuProcessTimelineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuProcessTimelineActionPerformed
        
    }//GEN-LAST:event_jMenuProcessTimelineActionPerformed

    private void jButtonOkFacilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkFacilityActionPerformed
        
    }//GEN-LAST:event_jButtonOkFacilityActionPerformed

    private void jButtonCancelFacilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelFacilityActionPerformed
        jDialogFacility.setVisible(false);
    }//GEN-LAST:event_jButtonCancelFacilityActionPerformed

    private void jComboProvinceFacilityMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboProvinceFacilityMouseEntered
        //        jComboDistrict.removeAllItems();
        //        jComboFacility.removeAllItems();
    }//GEN-LAST:event_jComboProvinceFacilityMouseEntered

    private void jComboProvinceFacilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboProvinceFacilityActionPerformed
        try {

        Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            int itemCount = jComboDistrictFacility.getItemCount();

            for (int i = 0; i < itemCount; i++) {
                jComboDistrictFacility.removeItemAt(0);
            }
            jComboDistrictFacility.setSelectedIndex(-1);
            String ProvNam = jComboProvinceFacility.getSelectedItem().toString();
            st.executeQuery("SELECT distinct [district]\n"
                    + "  FROM [ClaimsAppSysZvandiri].[dbo].[DistFacTab] WHERE [province] = \n" + "'" + ProvNam + "'");
            ResultSet r = st.getResultSet();

            while (r.next()) {

                jComboDistrictFacility.addItem(r.getString("district"));
            }

            conn.close();
        } catch (Exception e) {

        }
    }//GEN-LAST:event_jComboProvinceFacilityActionPerformed

    private void jComboProvinceFacilityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboProvinceFacilityKeyPressed

    }//GEN-LAST:event_jComboProvinceFacilityKeyPressed

    private void jComboDistrictFacilityMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboDistrictFacilityMouseEntered
        //        try {
        //
        //            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
        //                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
        //
        //            Statement st = conn.createStatement();
        //
        //            int itemCount = jComboDistrictFacility.getItemCount();
        //
        //            for (int i = 0; i < itemCount; i++) {
        //                jComboDistrictFacility.removeItemAt(0);
        //            }
        //            jComboDistrictFacility.setSelectedIndex(-1);
        //            String ProvNam = jComboProvinceFacility.getSelectedItem().toString();
        //            st.executeQuery("SELECT distinct [district]\n"
        //                    + "  FROM [ClaimsAppSysZvandiri].[dbo].[DistFacTab] WHERE [province] = \n" + "'" + ProvNam + "'");
        //            ResultSet r = st.getResultSet();
        //
        //            while (r.next()) {
        //
        //                jComboDistrictFacility.addItem(r.getString("district"));
        //            }
        //
        //            conn.close();
        //        } catch (Exception e) {
        //
        //        }
    }//GEN-LAST:event_jComboDistrictFacilityMouseEntered

    private void jComboDistrictFacilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboDistrictFacilityActionPerformed
        try {

           Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            int itemCount = jComboFacility.getItemCount();

            for (int i = 0; i < itemCount; i++) {
                jComboFacility.removeItemAt(0);
            }
            jComboFacility.setSelectedIndex(-1);
            String FacilityNam = jComboDistrictFacility.getSelectedItem().toString();
            st.executeQuery("SELECT distinct [facility]\n"
                    + "  FROM [ClaimsAppSysZvandiri].[dbo].[DistFacTab] WHERE [district] = \n" + "'" + FacilityNam + "'");
            ResultSet r = st.getResultSet();

            while (r.next()) {
                // jLabelFacDist.setText(r.getString(1));
                jComboFacility.addItem(r.getString("facility"));
            }

        } catch (Exception e) {

        }
    }//GEN-LAST:event_jComboDistrictFacilityActionPerformed

    private void jComboFacilityFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboFacilityFocusGained

    }//GEN-LAST:event_jComboFacilityFocusGained

    private void jComboFacilityMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboFacilityMouseEntered
        //        try {
        //
        //            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
        //                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
        //
        //            Statement st = conn.createStatement();
        //            Statement st1 = conn.createStatement();
        //            int itemCount = jComboFacility.getItemCount();
        //
        //            for (int i = 0; i < itemCount; i++) {
        //                jComboFacility.removeItemAt(0);
        //            }
        //            jComboFacility.setSelectedIndex(-1);
        //            String FacilityNam = jComboDistrictFacility.getSelectedItem().toString();
        //            st.executeQuery("SELECT distinct [facility]\n"
        //                    + "  FROM [ClaimsAppSysZvandiri].[dbo].[DistFacTab] WHERE [district] = \n" + "'" + FacilityNam + "'");
        //            ResultSet r = st.getResultSet();
        //
        //            while (r.next()) {
        //                // jLabelFacDist.setText(r.getString(1));
        //                jComboFacility.addItem(r.getString("facility"));
        //            }
        //
        //        } catch (Exception e) {
        //
        //        }
    }//GEN-LAST:event_jComboFacilityMouseEntered

    private void jComboFacilityMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboFacilityMouseReleased

    }//GEN-LAST:event_jComboFacilityMouseReleased

    private void jComboFacilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboFacilityActionPerformed
        facDist();
    }//GEN-LAST:event_jComboFacilityActionPerformed

    private void jButtonWk4DelActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk4DelActivityActionPerformed
        DefaultTableModel modelWk4 = (DefaultTableModel) this.jTableWk4Activities.getModel();
        int selectedOption = JOptionPane.showConfirmDialog(jTableWk4Activities,
                "Do you want to delete selected activity line?",
                "Choose",
                JOptionPane.YES_NO_OPTION);
        if (selectedOption == JOptionPane.YES_OPTION) {

            int[] rows = jTableWk4Activities.getSelectedRows();
            for (int i = 0; i < rows.length; i++) {
                modelWk4.removeRow(rows[i] - i);
            }
        } else {
            jButtonWk4DelActivity.requestFocusInWindow();
            jButtonWk4DelActivity.setFocusable(true);
        }
    }//GEN-LAST:event_jButtonWk4DelActivityActionPerformed

    private void jButtonWk5DelActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk5DelActivityActionPerformed
        DefaultTableModel modelWk5 = (DefaultTableModel) this.jTableWk5Activities.getModel();
        int selectedOption = JOptionPane.showConfirmDialog(jTableWk5Activities,
                "Do you want to delete selected activity line?",
                "Choose",
                JOptionPane.YES_NO_OPTION);
        if (selectedOption == JOptionPane.YES_OPTION) {

            int[] rows = jTableWk5Activities.getSelectedRows();
            for (int i = 0; i < rows.length; i++) {
                modelWk5.removeRow(rows[i] - i);
            }
        } else {
            jButtonWk5DelActivity.requestFocusInWindow();
            jButtonWk5DelActivity.setFocusable(true);
        }
    }//GEN-LAST:event_jButtonWk5DelActivityActionPerformed

    private void jMenuItemRequestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRequestActionPerformed
       
    }//GEN-LAST:event_jMenuItemRequestActionPerformed

    private void jMenuItemAcquittalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAcquittalActionPerformed
        new JFrameAppAcquittal(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemAcquittalActionPerformed

    private void jMenuItemSpecialAcquittalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSpecialAcquittalActionPerformed
       

    }//GEN-LAST:event_jMenuItemSpecialAcquittalActionPerformed

    private void jMenuItemRequestMOHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRequestMOHActionPerformed
        
    }//GEN-LAST:event_jMenuItemRequestMOHActionPerformed

    private void jMenuItemMeetAcquittalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemMeetAcquittalActionPerformed
       
    }//GEN-LAST:event_jMenuItemMeetAcquittalActionPerformed

    private void jMenuItem3WkPlanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3WkPlanActionPerformed
//        new JFrameMnthPlanPerDiemCreate(jLabelEmp.getText()).setVisible(true);
//        setVisible(false);
    }//GEN-LAST:event_jMenuItem3WkPlanActionPerformed

    private void jMenuReqEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuReqEditActionPerformed
       
    }//GEN-LAST:event_jMenuReqEditActionPerformed

    private void jMenuAcqEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuAcqEditActionPerformed
        new JFrameAppEditAcquittal(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuAcqEditActionPerformed

    private void jMenuMonPlanEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuMonPlanEditActionPerformed
        new JFrameMeetingPlanEdit(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuMonPlanEditActionPerformed

    private void jMenuItemCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCloseActionPerformed
        new JFrameFinSysLogin().setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemCloseActionPerformed

    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItemExitActionPerformed

    private void jMenuFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuFileActionPerformed

    }//GEN-LAST:event_jMenuFileActionPerformed

    private void jMenuItemProvMgrApprovalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemProvMgrApprovalActionPerformed
        new JFrameMnthSupList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemProvMgrApprovalActionPerformed

    private void jMenuItemReqGenLstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReqGenLstActionPerformed
        new JFrameMnthReqGenList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemReqGenLstActionPerformed

    private void jMenuItemMeetGenLstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemMeetGenLstActionPerformed
      
    }//GEN-LAST:event_jMenuItemMeetGenLstActionPerformed

    private void jMenuItemPlanViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPlanViewActionPerformed
        new JFrameMnthViewList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemPlanViewActionPerformed

    private void jMenuItemSupAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSupAppActionPerformed
        //        JOptionPane.showMessageDialog(this, "Please note that approval check boxes have been moved to the first tab (User and Accounting Details)");
        new JFrameSupAppList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemSupAppActionPerformed

    private void jMenuItemAccMgrRevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAccMgrRevActionPerformed
        //        JOptionPane.showMessageDialog(this, "Please note that approval check boxes have been moved to the first tab (User and Accounting Details)");
        new JFrameAccMgrAppList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemAccMgrRevActionPerformed

    private void jMenuItemHeadAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemHeadAppActionPerformed
        //        JOptionPane.showMessageDialog(this, "Please note that approval check boxes have been moved to the first tab (User and Accounting Details)");
        new JFrameReqHeadAppList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemHeadAppActionPerformed

    private void jMenuPlanApprovalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuPlanApprovalActionPerformed
       
    }//GEN-LAST:event_jMenuPlanApprovalActionPerformed

    private void jMenuItemSchGenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSchGenActionPerformed
        new JFrameReqHQPayScheduleApp(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemSchGenActionPerformed

    private void jMenuItemViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemViewActionPerformed
        new JFrameReqViewApp(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemViewActionPerformed

    private void jMenuItemClose1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemClose1ActionPerformed
        updatePrevRecord();
        updateWk1Plan();
        updateWk2Plan();
        updateWk3Plan();
        updateWk4Plan();
        updateWk5Plan();
        updateMeetWk1();
        updateMeetWk2();
        updateMeetWk3();
        updateMeetWk4();
        updateMeetWk5();
        updateWkPlanPeriod();
        updateWkPlanAction();
        UsrRecEditUpd();
        mailUpdate();
    }//GEN-LAST:event_jMenuItemClose1ActionPerformed

    private void jButtonWk1DelMeetActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk1DelMeetActivityActionPerformed
        DefaultTableModel modelMeetWk1 = (DefaultTableModel) this.jTableMeetReqWk1.getModel();

        if (jTableMeetReqWk1.getRowCount() > 0) {
            int selectedOption = JOptionPane.showConfirmDialog(jTableMeetReqWk1,
                    "Do you want to delete selected activity line?",
                    "Choose",
                    JOptionPane.YES_NO_OPTION);
            if (selectedOption == JOptionPane.YES_OPTION) {

                int[] rows = jTableMeetReqWk1.getSelectedRows();
                for (int i = 0; i < rows.length; i++) {
                    modelMeetWk1.removeRow(rows[i] - i);
                }
            } else {
                jButtonWk1DelActivity.requestFocusInWindow();
                jButtonWk1DelActivity.setFocusable(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Table empty. No records to delete.");
        }
    }//GEN-LAST:event_jButtonWk1DelMeetActivityActionPerformed

    private void jButtonWk2DelMeetActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk2DelMeetActivityActionPerformed
        DefaultTableModel modelMeetWk2 = (DefaultTableModel) this.jTableMeetReqWk2.getModel();

        if (jTableMeetReqWk2.getRowCount() > 0) {
            int selectedOption = JOptionPane.showConfirmDialog(jTableMeetReqWk2,
                    "Do you want to delete selected activity line?",
                    "Choose",
                    JOptionPane.YES_NO_OPTION);
            if (selectedOption == JOptionPane.YES_OPTION) {

                int[] rows = jTableMeetReqWk2.getSelectedRows();
                for (int i = 0; i < rows.length; i++) {
                    modelMeetWk2.removeRow(rows[i] - i);
                }
            } else {
                jButtonWk2DelActivity.requestFocusInWindow();
                jButtonWk2DelActivity.setFocusable(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Table empty. No records to delete.");
        }
    }//GEN-LAST:event_jButtonWk2DelMeetActivityActionPerformed

    private void jButtonWk3DelMeetActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk3DelMeetActivityActionPerformed
        DefaultTableModel modelMeetWk3 = (DefaultTableModel) this.jTableMeetReqWk3.getModel();

        if (jTableMeetReqWk3.getRowCount() > 0) {
            int selectedOption = JOptionPane.showConfirmDialog(jTableMeetReqWk3,
                    "Do you want to delete selected activity line?",
                    "Choose",
                    JOptionPane.YES_NO_OPTION);
            if (selectedOption == JOptionPane.YES_OPTION) {

                int[] rows = jTableMeetReqWk3.getSelectedRows();
                for (int i = 0; i < rows.length; i++) {
                    modelMeetWk3.removeRow(rows[i] - i);
                }
            } else {
                jButtonWk3DelActivity.requestFocusInWindow();
                jButtonWk3DelActivity.setFocusable(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Table empty. No records to delete.");
        }
    }//GEN-LAST:event_jButtonWk3DelMeetActivityActionPerformed

    private void jButtonWk4DelMeetActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk4DelMeetActivityActionPerformed
        DefaultTableModel modelMeetWk4 = (DefaultTableModel) this.jTableMeetReqWk4.getModel();

        if (jTableMeetReqWk4.getRowCount() > 0) {
            int selectedOption = JOptionPane.showConfirmDialog(jTableMeetReqWk4,
                    "Do you want to delete selected activity line?",
                    "Choose",
                    JOptionPane.YES_NO_OPTION);
            if (selectedOption == JOptionPane.YES_OPTION) {

                int[] rows = jTableMeetReqWk4.getSelectedRows();
                for (int i = 0; i < rows.length; i++) {
                    modelMeetWk4.removeRow(rows[i] - i);
                }
            } else {
                jButtonWk4DelActivity.requestFocusInWindow();
                jButtonWk4DelActivity.setFocusable(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Table empty. No records to delete.");
        }
    }//GEN-LAST:event_jButtonWk4DelMeetActivityActionPerformed

    private void jButtonWk5DelMeetActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk5DelMeetActivityActionPerformed
        DefaultTableModel modelMeetWk5 = (DefaultTableModel) this.jTableMeetReqWk5.getModel();

        if (jTableMeetReqWk5.getRowCount() > 0) {
            int selectedOption = JOptionPane.showConfirmDialog(jTableMeetReqWk5,
                    "Do you want to delete selected activity line?",
                    "Choose",
                    JOptionPane.YES_NO_OPTION);
            if (selectedOption == JOptionPane.YES_OPTION) {

                int[] rows = jTableMeetReqWk5.getSelectedRows();
                for (int i = 0; i < rows.length; i++) {
                    modelMeetWk5.removeRow(rows[i] - i);
                }
            } else {
                jButtonWk5DelActivity.requestFocusInWindow();
                jButtonWk5DelActivity.setFocusable(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Table empty. No records to delete.");
        }
    }//GEN-LAST:event_jButtonWk5DelMeetActivityActionPerformed

    private void jTextUnitCostWk1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextUnitCostWk1ActionPerformed

    }//GEN-LAST:event_jTextUnitCostWk1ActionPerformed

    private void jTextUnitCostWk1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextUnitCostWk1KeyReleased
        calcBudTotWk1();
    }//GEN-LAST:event_jTextUnitCostWk1KeyReleased

    private void jTextNumPpleWk1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextNumPpleWk1ActionPerformed

    }//GEN-LAST:event_jTextNumPpleWk1ActionPerformed

    private void jTextNumPpleWk1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextNumPpleWk1KeyReleased
        calcBudTotWk1();
    }//GEN-LAST:event_jTextNumPpleWk1KeyReleased

    private void jTextBusFareWk1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextBusFareWk1ActionPerformed

    }//GEN-LAST:event_jTextBusFareWk1ActionPerformed

    private void jTextBusFareWk1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextBusFareWk1KeyReleased
        calcBudTotWk1();
    }//GEN-LAST:event_jTextBusFareWk1KeyReleased

    private void jTextPersonResWk1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextPersonResWk1MouseClicked
        perRespWk1 = "Y";
        jDialogSearchName1.setVisible(true);
    }//GEN-LAST:event_jTextPersonResWk1MouseClicked

    private void jTextPersonResWk1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextPersonResWk1ActionPerformed

    }//GEN-LAST:event_jTextPersonResWk1ActionPerformed

    private void jTextPersonResWk1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextPersonResWk1KeyReleased
        perRespWk1 = "Y";
        jDialogSearchName1.setVisible(true);
    }//GEN-LAST:event_jTextPersonResWk1KeyReleased

    private void jComboMeetAllowanceWk1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboMeetAllowanceWk1MouseClicked
        resetFieldsBud();
    }//GEN-LAST:event_jComboMeetAllowanceWk1MouseClicked

    private void jComboMeetAllowanceWk1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboMeetAllowanceWk1ActionPerformed
        resetFieldsBud();
    }//GEN-LAST:event_jComboMeetAllowanceWk1ActionPerformed

    private void jDateToWk1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jDateToWk1MouseClicked

    }//GEN-LAST:event_jDateToWk1MouseClicked

    private void jDateToWk1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jDateToWk1MouseEntered
        // computeMeetDays();        
    }//GEN-LAST:event_jDateToWk1MouseEntered

    private void jDateToWk1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jDateToWk1KeyReleased

    }//GEN-LAST:event_jDateToWk1KeyReleased

    private void jTextFieldBudgetWk1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldBudgetWk1MouseClicked
        budgetPOP();
        meetBudWk1 = "Y";
    }//GEN-LAST:event_jTextFieldBudgetWk1MouseClicked

    private void jTextFieldBudgetWk1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldBudgetWk1KeyTyped
        budgetPOP();
        meetBudWk1 = "Y";
    }//GEN-LAST:event_jTextFieldBudgetWk1KeyTyped

    private void jButtonAddActivityWk1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActivityWk1ActionPerformed
        Wk1MeetAddItm();
    }//GEN-LAST:event_jButtonAddActivityWk1ActionPerformed

    private void jButtonMeetExitActivityWk1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMeetExitActivityWk1ActionPerformed
        jDialogWk1.setVisible(true);
        jDialogMeetingWk1.dispose();
        meetBudWk1 = "N";

    }//GEN-LAST:event_jButtonMeetExitActivityWk1ActionPerformed

    private void jButtonReturnActivityWk1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReturnActivityWk1ActionPerformed
        jDialogWk1.setVisible(true);
        jDialogMeetingWk1.dispose();
        meetBudWk1 = "N";
    }//GEN-LAST:event_jButtonReturnActivityWk1ActionPerformed

    private void jTextUnitCostWk2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextUnitCostWk2ActionPerformed

    }//GEN-LAST:event_jTextUnitCostWk2ActionPerformed

    private void jTextUnitCostWk2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextUnitCostWk2KeyReleased
        calcBudTotWk2();
    }//GEN-LAST:event_jTextUnitCostWk2KeyReleased

    private void jTextNumPpleWk2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextNumPpleWk2ActionPerformed

    }//GEN-LAST:event_jTextNumPpleWk2ActionPerformed

    private void jTextNumPpleWk2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextNumPpleWk2KeyReleased
        calcBudTotWk2();
    }//GEN-LAST:event_jTextNumPpleWk2KeyReleased

    private void jTextBusFareWk2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextBusFareWk2ActionPerformed

    }//GEN-LAST:event_jTextBusFareWk2ActionPerformed

    private void jTextBusFareWk2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextBusFareWk2KeyReleased
        calcBudTotWk2();
    }//GEN-LAST:event_jTextBusFareWk2KeyReleased

    private void jTextPersonResWk2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextPersonResWk2MouseClicked
        perRespWk1 = "N";
        perRespWk2 = "N";
        perRespWk3 = "N";
        perRespWk4 = "N";
        perRespWk5 = "N";
        perRespWk2 = "Y";
        jDialogSearchName1.setVisible(true);
    }//GEN-LAST:event_jTextPersonResWk2MouseClicked

    private void jTextPersonResWk2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextPersonResWk2ActionPerformed

    }//GEN-LAST:event_jTextPersonResWk2ActionPerformed

    private void jTextPersonResWk2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextPersonResWk2KeyReleased
        perRespWk1 = "N";
        perRespWk2 = "N";
        perRespWk3 = "N";
        perRespWk4 = "N";
        perRespWk5 = "N";
        perRespWk2 = "Y";
        jDialogSearchName1.setVisible(true);
    }//GEN-LAST:event_jTextPersonResWk2KeyReleased

    private void jComboMeetAllowanceWk2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboMeetAllowanceWk2ActionPerformed
        resetFieldsBud();
    }//GEN-LAST:event_jComboMeetAllowanceWk2ActionPerformed

    private void jDateToWk2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jDateToWk2MouseClicked

    }//GEN-LAST:event_jDateToWk2MouseClicked

    private void jDateToWk2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jDateToWk2MouseEntered

    }//GEN-LAST:event_jDateToWk2MouseEntered

    private void jDateToWk2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jDateToWk2KeyReleased

    }//GEN-LAST:event_jDateToWk2KeyReleased

    private void jTextFieldBudgetWk2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldBudgetWk2MouseClicked
        budgetPOP();
        meetBudWk2 = "Y";
    }//GEN-LAST:event_jTextFieldBudgetWk2MouseClicked

    private void jTextFieldBudgetWk2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldBudgetWk2KeyTyped
        budgetPOP();
        meetBudWk2 = "Y";
    }//GEN-LAST:event_jTextFieldBudgetWk2KeyTyped

    private void jButtonAddActivityWk2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActivityWk2ActionPerformed
        Wk2MeetAddItm();
    }//GEN-LAST:event_jButtonAddActivityWk2ActionPerformed

    private void jButtonMeetExitActivityWk2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMeetExitActivityWk2ActionPerformed
//        jDialogWk2.setVisible(true);
        jDialogMeetingWk2.dispose();
        meetBudWk2 = "N";
    }//GEN-LAST:event_jButtonMeetExitActivityWk2ActionPerformed

    private void jButtonReturnActivityWk2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReturnActivityWk2ActionPerformed
//        jDialogWk2.setVisible(true);
        jDialogMeetingWk2.dispose();
        meetBudWk2 = "N";
    }//GEN-LAST:event_jButtonReturnActivityWk2ActionPerformed

    private void jTextUnitCostWk3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextUnitCostWk3ActionPerformed

    }//GEN-LAST:event_jTextUnitCostWk3ActionPerformed

    private void jTextUnitCostWk3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextUnitCostWk3KeyReleased
        calcBudTotWk3();
    }//GEN-LAST:event_jTextUnitCostWk3KeyReleased

    private void jTextNumPpleWk3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextNumPpleWk3ActionPerformed

    }//GEN-LAST:event_jTextNumPpleWk3ActionPerformed

    private void jTextNumPpleWk3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextNumPpleWk3KeyReleased
        calcBudTotWk3();
    }//GEN-LAST:event_jTextNumPpleWk3KeyReleased

    private void jTextBusFareWk3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextBusFareWk3ActionPerformed

    }//GEN-LAST:event_jTextBusFareWk3ActionPerformed

    private void jTextBusFareWk3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextBusFareWk3KeyReleased
        calcBudTotWk3();
    }//GEN-LAST:event_jTextBusFareWk3KeyReleased

    private void jTextPersonResWk3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextPersonResWk3MouseClicked
        perRespWk1 = "N";
        perRespWk2 = "N";
        perRespWk3 = "N";
        perRespWk4 = "N";
        perRespWk5 = "N";
        perRespWk3 = "Y";
        jDialogSearchName1.setVisible(true);
    }//GEN-LAST:event_jTextPersonResWk3MouseClicked

    private void jTextPersonResWk3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextPersonResWk3ActionPerformed

    }//GEN-LAST:event_jTextPersonResWk3ActionPerformed

    private void jTextPersonResWk3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextPersonResWk3KeyReleased
        perRespWk1 = "N";
        perRespWk2 = "N";
        perRespWk3 = "N";
        perRespWk4 = "N";
        perRespWk5 = "N";
        perRespWk3 = "Y";
        jDialogSearchName1.setVisible(true);
    }//GEN-LAST:event_jTextPersonResWk3KeyReleased

    private void jComboMeetAllowanceWk3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboMeetAllowanceWk3ActionPerformed
        resetFieldsBud();
    }//GEN-LAST:event_jComboMeetAllowanceWk3ActionPerformed

    private void jDateToWk3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jDateToWk3MouseClicked

    }//GEN-LAST:event_jDateToWk3MouseClicked

    private void jDateToWk3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jDateToWk3MouseEntered

    }//GEN-LAST:event_jDateToWk3MouseEntered

    private void jDateToWk3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jDateToWk3KeyReleased

    }//GEN-LAST:event_jDateToWk3KeyReleased

    private void jTextFieldBudgetWk3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldBudgetWk3MouseClicked
        budgetPOP();
        meetBudWk3 = "Y";
    }//GEN-LAST:event_jTextFieldBudgetWk3MouseClicked

    private void jTextFieldBudgetWk3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldBudgetWk3KeyTyped
        budgetPOP();
        meetBudWk3 = "Y";
    }//GEN-LAST:event_jTextFieldBudgetWk3KeyTyped

    private void jButtonAddActivityWk3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActivityWk3ActionPerformed
        Wk3MeetAddItm();
    }//GEN-LAST:event_jButtonAddActivityWk3ActionPerformed

    private void jButtonMeetExitActivityWk3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMeetExitActivityWk3ActionPerformed
//        jDialogWk3.setVisible(true);
        jDialogMeetingWk3.dispose();
        meetBudWk3 = "N";
        perRespWk3 = "N";
    }//GEN-LAST:event_jButtonMeetExitActivityWk3ActionPerformed

    private void jButtonReturnActivityWk3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReturnActivityWk3ActionPerformed
//        jDialogWk3.setVisible(true);
        jDialogMeetingWk3.dispose();
        meetBudWk3 = "N";
        perRespWk3 = "N";
    }//GEN-LAST:event_jButtonReturnActivityWk3ActionPerformed

    private void jTextUnitCostWk4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextUnitCostWk4ActionPerformed

    }//GEN-LAST:event_jTextUnitCostWk4ActionPerformed

    private void jTextUnitCostWk4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextUnitCostWk4KeyReleased
        calcBudTotWk4();
    }//GEN-LAST:event_jTextUnitCostWk4KeyReleased

    private void jTextNumPpleWk4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextNumPpleWk4ActionPerformed

    }//GEN-LAST:event_jTextNumPpleWk4ActionPerformed

    private void jTextNumPpleWk4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextNumPpleWk4KeyReleased
        calcBudTotWk4();
    }//GEN-LAST:event_jTextNumPpleWk4KeyReleased

    private void jTextBusFareWk4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextBusFareWk4ActionPerformed

    }//GEN-LAST:event_jTextBusFareWk4ActionPerformed

    private void jTextBusFareWk4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextBusFareWk4KeyReleased
        calcBudTotWk4();
    }//GEN-LAST:event_jTextBusFareWk4KeyReleased

    private void jTextPersonResWk4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextPersonResWk4MouseClicked
        perRespWk1 = "N";
        perRespWk2 = "N";
        perRespWk3 = "N";
        perRespWk4 = "N";
        perRespWk5 = "N";
        perRespWk4 = "Y";
        jDialogSearchName1.setVisible(true);
    }//GEN-LAST:event_jTextPersonResWk4MouseClicked

    private void jTextPersonResWk4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextPersonResWk4ActionPerformed

    }//GEN-LAST:event_jTextPersonResWk4ActionPerformed

    private void jTextPersonResWk4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextPersonResWk4KeyReleased
        perRespWk1 = "N";
        perRespWk2 = "N";
        perRespWk3 = "N";
        perRespWk4 = "N";
        perRespWk5 = "N";
        perRespWk4 = "Y";
        jDialogSearchName1.setVisible(true);
    }//GEN-LAST:event_jTextPersonResWk4KeyReleased

    private void jComboMeetAllowanceWk4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboMeetAllowanceWk4ActionPerformed
        resetFieldsBud();
    }//GEN-LAST:event_jComboMeetAllowanceWk4ActionPerformed

    private void jDateToWk4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jDateToWk4MouseClicked

    }//GEN-LAST:event_jDateToWk4MouseClicked

    private void jDateToWk4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jDateToWk4MouseEntered

    }//GEN-LAST:event_jDateToWk4MouseEntered

    private void jDateToWk4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jDateToWk4KeyReleased

    }//GEN-LAST:event_jDateToWk4KeyReleased

    private void jTextFieldBudgetWk4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldBudgetWk4MouseClicked
        budgetPOP();
        meetBudWk4 = "Y";
    }//GEN-LAST:event_jTextFieldBudgetWk4MouseClicked

    private void jTextFieldBudgetWk4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldBudgetWk4KeyTyped
        budgetPOP();
        meetBudWk4 = "Y";
    }//GEN-LAST:event_jTextFieldBudgetWk4KeyTyped

    private void jButtonAddActivityWk4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActivityWk4ActionPerformed
        Wk4MeetAddItm();
    }//GEN-LAST:event_jButtonAddActivityWk4ActionPerformed

    private void jButtonMeetExitActivityWk4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMeetExitActivityWk4ActionPerformed
//        jDialogWk4.setVisible(true);
        jDialogMeetingWk4.dispose();
        meetBudWk4 = "N";
        perRespWk4 = "N";
    }//GEN-LAST:event_jButtonMeetExitActivityWk4ActionPerformed

    private void jButtonReturnActivityWk4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReturnActivityWk4ActionPerformed
//        jDialogWk4.setVisible(true);
        jDialogMeetingWk4.dispose();
        meetBudWk4 = "N";
        perRespWk4 = "N";
    }//GEN-LAST:event_jButtonReturnActivityWk4ActionPerformed

    private void jTextUnitCostWk5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextUnitCostWk5ActionPerformed

    }//GEN-LAST:event_jTextUnitCostWk5ActionPerformed

    private void jTextUnitCostWk5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextUnitCostWk5KeyReleased
        calcBudTotWk5();
    }//GEN-LAST:event_jTextUnitCostWk5KeyReleased

    private void jTextNumPpleWk5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextNumPpleWk5ActionPerformed

    }//GEN-LAST:event_jTextNumPpleWk5ActionPerformed

    private void jTextNumPpleWk5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextNumPpleWk5KeyReleased
        calcBudTotWk5();
    }//GEN-LAST:event_jTextNumPpleWk5KeyReleased

    private void jTextBusFareWk5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextBusFareWk5ActionPerformed

    }//GEN-LAST:event_jTextBusFareWk5ActionPerformed

    private void jTextBusFareWk5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextBusFareWk5KeyReleased
        calcBudTotWk5();
    }//GEN-LAST:event_jTextBusFareWk5KeyReleased

    private void jTextPersonResWk5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextPersonResWk5MouseClicked
        perRespWk1 = "N";
        perRespWk2 = "N";
        perRespWk3 = "N";
        perRespWk4 = "N";
        perRespWk5 = "N";
        perRespWk5 = "Y";
        jDialogSearchName1.setVisible(true);
    }//GEN-LAST:event_jTextPersonResWk5MouseClicked

    private void jTextPersonResWk5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextPersonResWk5ActionPerformed

    }//GEN-LAST:event_jTextPersonResWk5ActionPerformed

    private void jTextPersonResWk5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextPersonResWk5KeyReleased
        perRespWk1 = "N";
        perRespWk2 = "N";
        perRespWk3 = "N";
        perRespWk4 = "N";
        perRespWk5 = "N";
        perRespWk5 = "Y";
        jDialogSearchName1.setVisible(true);
    }//GEN-LAST:event_jTextPersonResWk5KeyReleased

    private void jComboMeetAllowanceWk5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboMeetAllowanceWk5ActionPerformed
        resetFieldsBud();
    }//GEN-LAST:event_jComboMeetAllowanceWk5ActionPerformed

    private void jDateToWk5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jDateToWk5MouseClicked

    }//GEN-LAST:event_jDateToWk5MouseClicked

    private void jDateToWk5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jDateToWk5MouseEntered

    }//GEN-LAST:event_jDateToWk5MouseEntered

    private void jDateToWk5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jDateToWk5KeyReleased

    }//GEN-LAST:event_jDateToWk5KeyReleased

    private void jTextFieldBudgetWk5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldBudgetWk5MouseClicked
        budgetPOP();
        meetBudWk5 = "Y";
    }//GEN-LAST:event_jTextFieldBudgetWk5MouseClicked

    private void jTextFieldBudgetWk5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldBudgetWk5KeyTyped
        budgetPOP();
        meetBudWk5 = "Y";
    }//GEN-LAST:event_jTextFieldBudgetWk5KeyTyped

    private void jButtonAddActivityWk5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActivityWk5ActionPerformed
        Wk5MeetAddItm();
    }//GEN-LAST:event_jButtonAddActivityWk5ActionPerformed

    private void jButtonMeetExitActivityWk5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMeetExitActivityWk5ActionPerformed
//        jDialogWk5.setVisible(true);
        jDialogMeetingWk5.dispose();
        meetBudWk5 = "N";
        perRespWk5 = "N";
    }//GEN-LAST:event_jButtonMeetExitActivityWk5ActionPerformed

    private void jButtonReturnActivityWk5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReturnActivityWk5ActionPerformed
//        jDialogWk5.setVisible(true);
        jDialogMeetingWk5.dispose();
        meetBudWk5 = "N";
        perRespWk5 = "N";
    }//GEN-LAST:event_jButtonReturnActivityWk5ActionPerformed

    private void jTextFieldSearchNam1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldSearchNam1FocusGained

    }//GEN-LAST:event_jTextFieldSearchNam1FocusGained

    private void jTextFieldSearchNam1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldSearchNam1FocusLost

    }//GEN-LAST:event_jTextFieldSearchNam1FocusLost

    private void jTextFieldSearchNam1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldSearchNam1ActionPerformed

    }//GEN-LAST:event_jTextFieldSearchNam1ActionPerformed

    private void jButtonSearch1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearch1ActionPerformed

        searchName1();
    }//GEN-LAST:event_jButtonSearch1ActionPerformed

    private void jButtonOk1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOk1ActionPerformed

        String str = jComboBoxSearchResult1.getSelectedItem().toString();
        if ("Y".equals(perRespWk1)) {
            jTextPersonResWk1.setText(str);
        } else if ("Y".equals(perRespWk2)) {
            jTextPersonResWk2.setText(str);
        } else if ("Y".equals(perRespWk3)) {
            jTextPersonResWk3.setText(str);
        } else if ("Y".equals(perRespWk4)) {
            jTextPersonResWk4.setText(str);
        } else if ("Y".equals(perRespWk5)) {
            jTextPersonResWk5.setText(str);
        }
        if ("Y".equals(staffName1)) {
            jTextFieldWk1DialogStaffName1.setText(str);
            jLabelWk1Name1Del.setVisible(true);
        }

        jTextFieldSearchNam1.setText("");
        perRespWk1 = "N";
        perRespWk2 = "N";
        perRespWk3 = "N";
        perRespWk4 = "N";
        perRespWk5 = "N";
        jDialogSearchName1.dispose();
    }//GEN-LAST:event_jButtonOk1ActionPerformed

    private void jTextFieldDialogWk1SiteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk1SiteMouseClicked
        facilityPOP();
    }//GEN-LAST:event_jTextFieldDialogWk1SiteMouseClicked

    private void jTextFieldDialogWk1SiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk1SiteActionPerformed

    }//GEN-LAST:event_jTextFieldDialogWk1SiteActionPerformed

    private void jTextFieldDialogWk1SiteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk1SiteKeyTyped
        facilityPOP();
    }//GEN-LAST:event_jTextFieldDialogWk1SiteKeyTyped

    private void jTextAreaWk1DialogJustificationKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAreaWk1DialogJustificationKeyTyped
        jTextAreaWk1DialogJustification.setLineWrap(true);
        jTextAreaWk1DialogJustification.setWrapStyleWord(true);

        String charsRemaining = " characters remaining";
        int newLen = 0;

        int currLen = jTextAreaWk1DialogJustification.getText().length();

        if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            newLen = currLen - 1;
            ignoreInput = false;
        } else {
            newLen = currLen + 1;
        }

        if (newLen < 0) {
            newLen = 0;
        }

        if (newLen == 0) {
            jLabelRemain.setText(charMaxWk1 + " characters maximum!");
        } else if (newLen >= 0 && newLen < charMaxWk1) {
            jLabelRemain.setText((charMaxWk1 - newLen) + charsRemaining);
        } else if (newLen >= charMaxWk1) {
            try {
                ignoreInput = true;
                jLabelRemain.setText("0 " + charsRemaining);
                JOptionPane.showMessageDialog(jDialogWk1, "Maximum allowed characters reached.",
                        "Word Count Warning", JOptionPane.WARNING_MESSAGE);
                jTextAreaWk1DialogJustification.requestFocusInWindow();
                jTextAreaWk1DialogJustification.setFocusable(true);
            } catch (Exception e) {
                System.out.println(e);
            }

        }
        if ((charMaxWk1 - newLen) > 15) {
            jLabelRemain.setForeground(new java.awt.Color(0, 102, 0));
        } else {
            jLabelRemain.setForeground(new java.awt.Color(255, 51, 51));
        }
    }//GEN-LAST:event_jTextAreaWk1DialogJustificationKeyTyped

    private void jComboBoxDialogWk1PerDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxDialogWk1PerDiemActionPerformed

        if ("Meetings".equals(jComboBoxDialogWk1PerDiem.getSelectedItem().toString())) {
            jDialogWk1.dispose();
            jDialogMeetingWk1.setVisible(true);
            // computeMeetDaysCheckListen();
            // computeMeetDaysListen();

        } else {
            jTextFieldWk1DialogStaffName1.setVisible(true);
            jTextFieldWk1DialogStaffName2.setVisible(true);
            jTextFieldWk1DialogStaffName3.setVisible(true);
            jTextFieldWk1DialogStaffName4.setVisible(true);
            jCheckBoxDialogWk1BrkFast.setEnabled(true);
            jCheckBoxDialogWk1Lunch.setEnabled(true);
            jCheckBoxDialogWk1Dinner.setEnabled(true);
            jCheckBoxDialogWk1AccProved.setEnabled(true);
            jCheckBoxDialogWk1Inc.setEnabled(true);
            jCheckBoxDialogWk1Misc.setEnabled(true);

        }
    }//GEN-LAST:event_jComboBoxDialogWk1PerDiemActionPerformed

    private void jTextFieldWk1DialogStaffName4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName4MouseClicked

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName4MouseClicked

    private void jTextFieldWk1DialogStaffName4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName4ActionPerformed

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName4ActionPerformed

    private void jTextFieldWk1DialogStaffName4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName4KeyTyped

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName4KeyTyped

    private void jTextFieldWk1DialogStaffName1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName1MouseClicked
        staffName1 = "Y";
        staffName2 = "N";
        staffName3 = "N";
        staffName4 = "N";
        jDialogSearchName1.setVisible(true);
        jDialogSearchName1.setVisible(false);
        jDialogSearchName1.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk1DialogStaffName1MouseClicked

    private void jTextFieldWk1DialogStaffName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName1ActionPerformed

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName1ActionPerformed

    private void jTextFieldWk1DialogStaffName1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName1KeyTyped
        staffName1 = "Y";
        staffName2 = "N";
        staffName3 = "N";
        staffName4 = "N";
         jDialogSearchName1.setVisible(true);
        jDialogSearchName1.setVisible(false);
        jDialogSearchName1.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk1DialogStaffName1KeyTyped

    private void jTextFieldWk1DialogStaffName2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName2MouseClicked
        staffName1 = "N";
        staffName2 = "Y";
        staffName3 = "N";
        staffName4 = "N";
       
    }//GEN-LAST:event_jTextFieldWk1DialogStaffName2MouseClicked

    private void jTextFieldWk1DialogStaffName2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName2KeyTyped
        staffName1 = "N";
        staffName2 = "Y";
        staffName3 = "N";
        staffName4 = "N";
      
    }//GEN-LAST:event_jTextFieldWk1DialogStaffName2KeyTyped

    private void jTextFieldWk1DialogStaffName3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName3MouseClicked

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName3MouseClicked

    private void jTextFieldWk1DialogStaffName3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName3KeyTyped

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName3KeyTyped

    private void jButtonDialogWk1ResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDialogWk1ResetActionPerformed
        resetField();
    }//GEN-LAST:event_jButtonDialogWk1ResetActionPerformed

    private void jButtonDialogWk1AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDialogWk1AddActionPerformed

        String Wk1Brk = "0.00";
        String Wk1Lnch = "0.00";
        String Wk1Dinner = "0.00";
        String Wk1ProvedAcc = "0.00";
        String Wk1UnProvedAcc = "0.00";
        String Wk1Inc = "0.00";
        String Wk1Misc = "0.00";
        String Wk1MiscDesc = "";

//        if (!(jTextFieldWk1DialogStaffName1.getText().length() == 0)) {
//            duplicateUser1Count = null;
//            planWk = "PlanWk1Tab";
//            planDate = formatter.format(jDateChooserDialogWk1ActivityDate.getDate());
//            empNamNum1 = "EMP_NAM1";
//            empNam1 = jTextFieldWk1DialogStaffName1.getText();
//            empNamNum2 = "EMP_NAM2";
//            empNam2 = jTextFieldWk1DialogStaffName2.getText();
//            empNamNum3 = "EMP_NAM3";
//            empNam3 = jTextFieldWk1DialogStaffName3.getText();
//            empNamNum4 = "EMP_NAM4";
//            empNam4 = jTextFieldWk1DialogStaffName4.getText();
//
//            CheckDuplicateUser1();
//            CheckDuplicateUser2();
//            CheckDuplicateUser3();
//            CheckDuplicateUser4();
//            CheckDuplicateUser5();
//        }

        try {
            if (jCheckBoxDialogWk1BrkFast.isSelected()) {
                Wk1Brk = breakfastAll;
            } else {
                Wk1Brk = "0.00";
            }

            if (jCheckBoxDialogWk1Lunch.isSelected()) {
                Wk1Lnch = lunchAll;
            } else {
                Wk1Lnch = "0.00";
            }

            if (jCheckBoxDialogWk1Dinner.isSelected()) {
                Wk1Dinner = dinnerAll;
            } else {
                Wk1Dinner = "0.00";
            }

            if (jCheckBoxDialogWk1AccUnProved.isSelected()) {
                Wk1UnProvedAcc = unProvedAll;
            } else {
                Wk1UnProvedAcc = "0.00";
            }
            if (jCheckBoxDialogWk1AccProved.isSelected()) {
                Wk1ProvedAcc = provedAll;
            } else {
                Wk1ProvedAcc = "0.00";
            }

            if (jCheckBoxDialogWk1Inc.isSelected()) {
                Wk1Inc = incidentalAll;
            } else {
                Wk1Inc = "0.00";
            }

            if (jCheckBoxDialogWk1Misc.isSelected()) {
                Wk1MiscDesc = jTextFieldWk1Misc.getText();
                Wk1Misc = jTextFieldWk1MiscAmt.getText();
            } else {
                Wk1Misc = "0.00";
            }

            if (jDateChooserDialogWk1ActivityDate.getDate() == null) {
                JOptionPane.showMessageDialog(jDialogWk1, "Date cannot be blank. Please check your dates");
                jDateChooserDialogWk1ActivityDate.requestFocusInWindow();
                jDateChooserDialogWk1ActivityDate.setFocusable(true);
            } else if (("Y".equals(wk1Site)) && ((formatter.format(jDateChooserDialogWk1ActivityDate.getDate()).compareTo(formatter.format(jDateChooserWk1From.getDate())) < 0)
                    || (formatter.format(jDateChooserDialogWk1ActivityDate.getDate()).compareTo(formatter.format(jDateChooserWk1To.getDate())) > 0))) {
                JOptionPane.showMessageDialog(jDialogWk1, "Activity date cannot be outside the specified week date range.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserDialogWk1ActivityDate.setDate(null);
                jDateChooserDialogWk1ActivityDate.requestFocusInWindow();
                jDateChooserDialogWk1ActivityDate.setFocusable(true);
            } else if (("Y".equals(wk2Site)) && ((formatter.format(jDateChooserDialogWk1ActivityDate.getDate()).compareTo(formatter.format(jDateChooserWk2From.getDate())) < 0)
                    || (formatter.format(jDateChooserDialogWk1ActivityDate.getDate()).compareTo(formatter.format(jDateChooserWk2To.getDate())) > 0))) {
                JOptionPane.showMessageDialog(jDialogWk1, "Activity date cannot be outside the specified week date range.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserDialogWk1ActivityDate.setDate(null);
                jDateChooserDialogWk1ActivityDate.requestFocusInWindow();
                jDateChooserDialogWk1ActivityDate.setFocusable(true);
            } else if (("Y".equals(wk3Site)) && ((formatter.format(jDateChooserDialogWk1ActivityDate.getDate()).compareTo(formatter.format(jDateChooserWk3From.getDate())) < 0)
                    || (formatter.format(jDateChooserDialogWk1ActivityDate.getDate()).compareTo(formatter.format(jDateChooserWk3To.getDate())) > 0))) {
                JOptionPane.showMessageDialog(jDialogWk1, "Activity date cannot be outside the specified week date range.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserDialogWk1ActivityDate.setDate(null);
                jDateChooserDialogWk1ActivityDate.requestFocusInWindow();
                jDateChooserDialogWk1ActivityDate.setFocusable(true);
            } else if (("Y".equals(wk4Site)) && ((formatter.format(jDateChooserDialogWk1ActivityDate.getDate()).compareTo(formatter.format(jDateChooserWk4From.getDate())) < 0)
                    || (formatter.format(jDateChooserDialogWk1ActivityDate.getDate()).compareTo(formatter.format(jDateChooserWk4To.getDate())) > 0))) {
                JOptionPane.showMessageDialog(jDialogWk1, "Activity date cannot be outside the specified week date range.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserDialogWk1ActivityDate.setDate(null);
                jDateChooserDialogWk1ActivityDate.requestFocusInWindow();
                jDateChooserDialogWk1ActivityDate.setFocusable(true);
            } else if (("Y".equals(wk2Site)) && ((formatter.format(jDateChooserDialogWk1ActivityDate.getDate()).compareTo(formatter.format(jDateChooserWk5From.getDate())) < 0)
                    || (formatter.format(jDateChooserDialogWk1ActivityDate.getDate()).compareTo(formatter.format(jDateChooserWk5To.getDate())) > 0))) {
                JOptionPane.showMessageDialog(jDialogWk1, "Activity date cannot be outside the specified week date range.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserDialogWk1ActivityDate.setDate(null);
                jDateChooserDialogWk1ActivityDate.requestFocusInWindow();
                jDateChooserDialogWk1ActivityDate.setFocusable(true);
            } else if ("".equals(jTextFieldDialogWk1Site.getText())) {
                JOptionPane.showMessageDialog(jDialogWk1, "Site cannot be blank. Please check and correct");
                jTextFieldDialogWk1Site.requestFocusInWindow();
                jTextFieldDialogWk1Site.setFocusable(true);
            } else if ("".equals(jTextFieldWk1DialogActivityDesc.getText())) {
                JOptionPane.showMessageDialog(jDialogWk1, "Activity description cannot be blank. Please check and correct");
                jTextFieldWk1DialogActivityDesc.requestFocusInWindow();
                jTextFieldWk1DialogActivityDesc.setFocusable(true);
            } else if ("".equals(jTextAreaWk1DialogJustification.getText())) {
                JOptionPane.showMessageDialog(jDialogWk1, "Activity justification cannot be blank. Please check and correct");
                jTextAreaWk1DialogJustification.requestFocusInWindow();
                jTextAreaWk1DialogJustification.setFocusable(true);
            } else if ((jCheckBoxDialogWk1BrkFast.isSelected() || jCheckBoxDialogWk1Lunch.isSelected()
                    || jCheckBoxDialogWk1Dinner.isSelected() || jCheckBoxDialogWk1AccProved.isSelected()
                    || jCheckBoxDialogWk1Inc.isSelected() || jCheckBoxDialogWk1Misc.isSelected())
                    && ("".equals(jTextFieldWk1DialogStaffName1.getText()))) {
                JOptionPane.showMessageDialog(jDialogWk1, "You have select an allowance.Please enter at least one staff name");
                jTextFieldWk1DialogStaffName1.requestFocusInWindow();
                jTextFieldWk1DialogStaffName1.setFocusable(true);

            } else {

                if ("Y".equals(wk1Site)) {
                    modelWk1.addRow(new Object[]{formatter.format(jDateChooserDialogWk1ActivityDate.getDate()), branchCode, prjCode, taskCode,
                        jTextFieldDialogWk1Site.getText(), jTextFieldWk1DialogActivityDesc.getText(),
                        jTextAreaWk1DialogJustification.getText(), Wk1Brk, Wk1Lnch, Wk1Dinner, Wk1Inc, Wk1MiscDesc, Wk1Misc, Wk1UnProvedAcc,
                        Wk1ProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                        jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});
                    addItem();
                    resetField();

                }
                if ("Y".equals(wk2Site)) {

                    modelWk2.addRow(new Object[]{formatter.format(jDateChooserDialogWk1ActivityDate.getDate()), branchCode, prjCode, taskCode,
                        jTextFieldDialogWk1Site.getText(), jTextFieldWk1DialogActivityDesc.getText(),
                        jTextAreaWk1DialogJustification.getText(), Wk1Brk, Wk1Lnch, Wk1Dinner, Wk1Inc, Wk1MiscDesc, Wk1Misc, Wk1UnProvedAcc,
                        Wk1ProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                        jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});
                    addItem();
                    resetField();
                }

                if ("Y".equals(wk3Site)) {

                    modelWk3.addRow(new Object[]{formatter.format(jDateChooserDialogWk1ActivityDate.getDate()), branchCode, prjCode, taskCode,
                        jTextFieldDialogWk1Site.getText(), jTextFieldWk1DialogActivityDesc.getText(),
                        jTextAreaWk1DialogJustification.getText(), Wk1Brk, Wk1Lnch, Wk1Dinner, Wk1Inc, Wk1MiscDesc, Wk1Misc, Wk1UnProvedAcc,
                        Wk1ProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                        jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});
                    addItem();
                    resetField();
                }

                if ("Y".equals(wk4Site)) {

                    modelWk4.addRow(new Object[]{formatter.format(jDateChooserDialogWk1ActivityDate.getDate()), branchCode, prjCode, taskCode,
                        jTextFieldDialogWk1Site.getText(), jTextFieldWk1DialogActivityDesc.getText(),
                        jTextAreaWk1DialogJustification.getText(), Wk1Brk, Wk1Lnch, Wk1Dinner, Wk1Inc, Wk1MiscDesc, Wk1Misc, Wk1UnProvedAcc,
                        Wk1ProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                        jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});
                    addItem();
                    resetField();
                }

                if ("Y".equals(wk5Site)) {

                    modelWk5.addRow(new Object[]{formatter.format(jDateChooserDialogWk1ActivityDate.getDate()), branchCode, prjCode, taskCode,
                        jTextFieldDialogWk1Site.getText(), jTextFieldWk1DialogActivityDesc.getText(),
                        jTextAreaWk1DialogJustification.getText(), Wk1Brk, Wk1Lnch, Wk1Dinner, Wk1Inc, Wk1MiscDesc, Wk1Misc, Wk1UnProvedAcc,
                        Wk1ProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                        jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});
                    addItem();
                    resetField();
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
        //}
    }//GEN-LAST:event_jButtonDialogWk1AddActionPerformed

    private void jCheckBoxDialogWk1MiscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxDialogWk1MiscActionPerformed
        if (jCheckBoxDialogWk1Misc.isSelected()) {
            jTextFieldWk1Misc.setVisible(true);
            jLabelWk1Misc.setVisible(true);
            jLabelWk1MiscAmt.setVisible(true);
            jTextFieldWk1MiscAmt.setVisible(true);
        } else {
            jTextFieldWk1Misc.setVisible(false);
            jLabelWk1Misc.setVisible(false);
            jTextFieldWk1Misc.setText("");
            jLabelWk1MiscAmt.setVisible(false);
            jTextFieldWk1MiscAmt.setVisible(false);
        }
    }//GEN-LAST:event_jCheckBoxDialogWk1MiscActionPerformed

    private void jTextFieldWk1MiscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWk1MiscActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldWk1MiscActionPerformed

    private void jButtonDialogWk1CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDialogWk1CloseActionPerformed
        wk1Site = "N";
        perRespWk1 = "N";
        jDialogWk1.setVisible(false);
    }//GEN-LAST:event_jButtonDialogWk1CloseActionPerformed

    private void jLabelWk1Name4DelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelWk1Name4DelMouseClicked
        jTextFieldWk1DialogStaffName4.setText("");
        jLabelWk1Name4Del.setVisible(false);
    }//GEN-LAST:event_jLabelWk1Name4DelMouseClicked

    private void jLabelWk1Name1DelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelWk1Name1DelMouseClicked
        jTextFieldWk1DialogStaffName1.setText("");
        jLabelWk1Name1Del.setVisible(false);
    }//GEN-LAST:event_jLabelWk1Name1DelMouseClicked

    private void jLabelWk1Name2DelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelWk1Name2DelMouseClicked
        jTextFieldWk1DialogStaffName2.setText("");
        jLabelWk1Name2Del.setVisible(false);
    }//GEN-LAST:event_jLabelWk1Name2DelMouseClicked

    private void jLabelWk1Name3DelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelWk1Name3DelMouseClicked
        jTextFieldWk1DialogStaffName3.setText("");
        jLabelWk1Name3Del.setVisible(false);
    }//GEN-LAST:event_jLabelWk1Name3DelMouseClicked

    private void jComboProjectNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboProjectNameMouseClicked

    }//GEN-LAST:event_jComboProjectNameMouseClicked

    private void jComboProjectNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboProjectNameActionPerformed
        try {
            int itemCount = jComboProjectTask.getItemCount();

            for (int i = 0; i < itemCount; i++) {
                jComboProjectTask.removeItemAt(0);
            }
            String prjName = jComboProjectName.getSelectedItem().toString();
            String prjParts[] = prjName.split(" ", 2);
            prjCode = prjParts[0];
            findTask(prjCode);
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_jComboProjectNameActionPerformed

    private void jComboProjectTaskMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboProjectTaskMouseClicked

        //        String prjName = jComboProjectName.getSelectedItem().toString();
        //        String prjParts[] = prjName.split(" ", 2);
        //        String prjCode = prjParts[0];
        //        findTask(prjCode);
    }//GEN-LAST:event_jComboProjectTaskMouseClicked

    private void jComboProjectTaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboProjectTaskActionPerformed
        try {

            String taskName = jComboProjectTask.getSelectedItem().toString();
            String taskParts[] = taskName.split(" ", 2);
            taskCode = taskParts[0];
            findBranch(taskCode);
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_jComboProjectTaskActionPerformed

    private void jButtonWk5AddActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk5AddActivityActionPerformed
        try {
            jLabelWk1Name1Del.setVisible(false);
            jLabelWk1Name2Del.setVisible(false);
            jLabelWk1Name3Del.setVisible(false);
            jLabelWk1Name4Del.setVisible(false);
            java.util.Date actPlanDate = jDateChooserWk5From.getDate();
            java.util.Date todayDate = new Date();

            long noOfDaysBetweenWk5 = 0;
            if ((!(jDateChooserWk5From.getDate() == null)) && (!(jDateChooserWk5To.getDate() == null))) {
                LocalDate dateBefore = LocalDate.parse(formatter.format(jDateChooserWk5From.getDate()));
                LocalDate dateAfter = LocalDate.parse(formatter.format(jDateChooserWk5To.getDate()));

                //calculating number of days in between
                noOfDaysBetweenWk5 = ChronoUnit.DAYS.between(dateBefore, dateAfter) + 1;
            }

            if ((jDateChooserWk4From.getDate() == null) || (jDateChooserWk4To.getDate() == null) || (jDateChooserWk3From.getDate() == null) || (jDateChooserWk3To.getDate() == null) || (jDateChooserWk2From.getDate() == null) || (jDateChooserWk2To.getDate() == null) || (jDateChooserWk2From.getDate() == null) || (jDateChooserWk2To.getDate() == null)) {
                JOptionPane.showMessageDialog(this, "Please complete week four before completing week five");
                jDateChooserWk5From.setDate(null);
                jDateChooserWk5To.setDate(null);
                jDateChooserWk5From.requestFocusInWindow();
                jDateChooserWk5From.setFocusable(true);
            } else if (jDateChooserWk5From.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
                jDateChooserWk5From.requestFocusInWindow();
                jDateChooserWk5From.setFocusable(true);
            } else if (formatter.format(jDateChooserWk5From.getDate()).compareTo(tH.internetDate) < 0) {
                JOptionPane.showMessageDialog(this, "Activity date cannot be lower than today's date. Please check your dates");
                jDateChooserWk5From.requestFocusInWindow();
                jDateChooserWk5From.setFocusable(true);
            } else if (jDateChooserWk5To.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
                jDateChooserWk5To.requestFocusInWindow();
                jDateChooserWk5To.setFocusable(true);
            } else if (jDateChooserWk5From.getDate().after(jDateChooserWk5To.getDate())) {
                JOptionPane.showMessageDialog(this, "End Date cannot be lower than start date.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk5From.setDate(null);
                jDateChooserWk5To.setDate(null);
                jDateChooserWk5From.requestFocusInWindow();
                jDateChooserWk5From.setFocusable(true);
            } else if (formatter.format(jDateChooserWk5From.getDate()).equals(formatter.format((jDateChooserWk4From.getDate())))) {
                JOptionPane.showMessageDialog(this, "Date cannot be equal to previous week.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk5From.setDate(null);
                jDateChooserWk5To.setDate(null);
                jDateChooserWk5From.requestFocusInWindow();
                jDateChooserWk5From.setFocusable(true);
            } else if (jDateChooserWk4To.getDate().after(jDateChooserWk5From.getDate())) {
                JOptionPane.showMessageDialog(this, "Date cannot be lower than the previous week.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk5From.setDate(null);
                jDateChooserWk5To.setDate(null);
                jDateChooserWk5From.requestFocusInWindow();
                jDateChooserWk5From.setFocusable(true);
            } else if (noOfDaysBetweenWk5 > 7) {
                JOptionPane.showMessageDialog(this, "Weekly Plan days cannot be more than seven(7) days",
                    "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk5From.setDate(null);
                jDateChooserWk5To.setDate(null);
                jDateChooserWk5From.requestFocusInWindow();
                jDateChooserWk5From.setFocusable(true);
            } else {
                wk1Site = "N";
                wk2Site = "N";
                wk3Site = "N";
                wk4Site = "N";
                wk5Site = "Y";

                jDialogWk1.setTitle("Per Diem Week 5");
                resetField();
                jDialogWk1.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonWk5AddActivityActionPerformed

    private void jButtonWk4AddActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk4AddActivityActionPerformed
        try {
            jLabelWk1Name1Del.setVisible(false);
            jLabelWk1Name2Del.setVisible(false);
            jLabelWk1Name3Del.setVisible(false);
            jLabelWk1Name4Del.setVisible(false);
            java.util.Date actPlanDate = jDateChooserWk4From.getDate();
            java.util.Date todayDate = new Date();

            long noOfDaysBetweenWk4 = 0;
            if ((!(jDateChooserWk4From.getDate() == null)) && (!(jDateChooserWk4To.getDate() == null))) {
                LocalDate dateBefore = LocalDate.parse(formatter.format(jDateChooserWk4From.getDate()));
                LocalDate dateAfter = LocalDate.parse(formatter.format(jDateChooserWk4To.getDate()));

                //calculating number of days in between
                noOfDaysBetweenWk4 = ChronoUnit.DAYS.between(dateBefore, dateAfter) + 1;
            }

            if ((jDateChooserWk3From.getDate() == null) || (jDateChooserWk3To.getDate() == null) || (jDateChooserWk2From.getDate() == null) || (jDateChooserWk2To.getDate() == null) || (jDateChooserWk1From.getDate() == null) || (jDateChooserWk1To.getDate() == null)) {
                JOptionPane.showMessageDialog(this, "Please complete week three before completing week four");
                jDateChooserWk4From.setDate(null);
                jDateChooserWk4To.setDate(null);
                jDateChooserWk4From.requestFocusInWindow();
                jDateChooserWk4From.setFocusable(true);
            } else if (jDateChooserWk4From.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
                jDateChooserWk4From.requestFocusInWindow();
                jDateChooserWk4From.setFocusable(true);
            } else if (formatter.format(jDateChooserWk4From.getDate()).compareTo(tH.internetDate) < 0) {
                JOptionPane.showMessageDialog(this, "Activity date cannot be lower than today's date. Please check your dates");
                jDateChooserWk4From.requestFocusInWindow();
                jDateChooserWk4From.setFocusable(true);
            } else if (jDateChooserWk4To.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
                jDateChooserWk4To.requestFocusInWindow();
                jDateChooserWk4To.setFocusable(true);
            } else if (jDateChooserWk4From.getDate().after(jDateChooserWk4To.getDate())) {
                JOptionPane.showMessageDialog(this, "End Date cannot be lower than start date.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk4From.setDate(null);
                jDateChooserWk4To.setDate(null);
                jDateChooserWk4From.requestFocusInWindow();
                jDateChooserWk4From.setFocusable(true);
            } else if (formatter.format(jDateChooserWk3From.getDate()).equals(formatter.format((jDateChooserWk4From.getDate())))) {
                JOptionPane.showMessageDialog(this, "Date cannot be equal to previous week.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk4From.setDate(null);
                jDateChooserWk4To.setDate(null);
                jDateChooserWk4From.requestFocusInWindow();
                jDateChooserWk4From.setFocusable(true);
            } else if (formatter.format(jDateChooserWk3To.getDate()).equals(formatter.format(jDateChooserWk4From.getDate()))) {
                JOptionPane.showMessageDialog(this, "Date cannot be equal to previous week.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk4From.setDate(null);
                jDateChooserWk4To.setDate(null);
                jDateChooserWk4From.requestFocusInWindow();
                jDateChooserWk4From.setFocusable(true);
            } else if (jDateChooserWk3To.getDate().after(jDateChooserWk4From.getDate())) {
                JOptionPane.showMessageDialog(this, "Date cannot be lower than the previous week.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk4From.setDate(null);
                jDateChooserWk4To.setDate(null);
                jDateChooserWk4From.requestFocusInWindow();
                jDateChooserWk4From.setFocusable(true);
            } else if (noOfDaysBetweenWk4 > 7) {
                JOptionPane.showMessageDialog(this, "Weekly Plan days cannot be more than seven(7) days",
                    "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk4From.setDate(null);
                jDateChooserWk4To.setDate(null);
                jDateChooserWk4From.requestFocusInWindow();
                jDateChooserWk4From.setFocusable(true);
            } else {
                wk1Site = "N";
                wk2Site = "N";
                wk3Site = "N";
                wk4Site = "Y";
                wk5Site = "N";

                jDialogWk1.setTitle("Per Diem Week 4");
                resetField();
                jDialogWk1.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonWk4AddActivityActionPerformed

    private void jButtonWk3AddActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk3AddActivityActionPerformed
        try {
            jLabelWk1Name1Del.setVisible(false);
            jLabelWk1Name2Del.setVisible(false);
            jLabelWk1Name3Del.setVisible(false);
            jLabelWk1Name4Del.setVisible(false);
            java.util.Date actPlanDate = jDateChooserWk3From.getDate();
            java.util.Date todayDate = new Date();

            long noOfDaysBetweenWk3 = 0;
            if ((!(jDateChooserWk3From.getDate() == null)) && (!(jDateChooserWk3To.getDate() == null))) {
                LocalDate dateBefore = LocalDate.parse(formatter.format(jDateChooserWk3From.getDate()));
                LocalDate dateAfter = LocalDate.parse(formatter.format(jDateChooserWk3To.getDate()));

                //calculating number of days in between
                noOfDaysBetweenWk3 = ChronoUnit.DAYS.between(dateBefore, dateAfter) + 1;
            }

            if ((jDateChooserWk2From.getDate() == null) || (jDateChooserWk2To.getDate() == null) || (jDateChooserWk1From.getDate() == null) || (jDateChooserWk1To.getDate() == null)) {
                JOptionPane.showMessageDialog(this, "Please complete week two before completing week three");
                jDateChooserWk3From.setDate(null);
                jDateChooserWk3To.setDate(null);
                jDateChooserWk1From.requestFocusInWindow();
                jDateChooserWk1From.setFocusable(true);
            } else if (jDateChooserWk3From.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
                jDateChooserWk3From.requestFocusInWindow();
                jDateChooserWk3From.setFocusable(true);
            } else if (formatter.format(jDateChooserWk3From.getDate()).compareTo(tH.internetDate) < 0) {
                JOptionPane.showMessageDialog(this, "Activity date cannot be lower than today's date. Please check your dates");
                jDateChooserWk3From.requestFocusInWindow();
                jDateChooserWk3From.setFocusable(true);
            } else if (jDateChooserWk3To.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
                jDateChooserWk3To.requestFocusInWindow();
                jDateChooserWk3To.setFocusable(true);
            } else if (jDateChooserWk3From.getDate().after(jDateChooserWk3To.getDate())) {
                JOptionPane.showMessageDialog(this, "End Date cannot be lower than start date.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk3From.setDate(null);
                jDateChooserWk3To.setDate(null);
                jDateChooserWk3From.requestFocusInWindow();
                jDateChooserWk3From.setFocusable(true);
            } else if (formatter.format(jDateChooserWk2From.getDate()).equals(formatter.format((jDateChooserWk3From.getDate())))) {
                JOptionPane.showMessageDialog(this, "Date cannot be equal to previous week.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk3From.setDate(null);
                jDateChooserWk3To.setDate(null);
                jDateChooserWk3From.requestFocusInWindow();
                jDateChooserWk3From.setFocusable(true);
            } else if (formatter.format(jDateChooserWk2To.getDate()).equals(formatter.format(jDateChooserWk3From.getDate()))) {
                JOptionPane.showMessageDialog(this, "Date cannot be equal to previous week.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk3From.setDate(null);
                jDateChooserWk3To.setDate(null);
                jDateChooserWk3From.requestFocusInWindow();
                jDateChooserWk3From.setFocusable(true);
            } else if (jDateChooserWk2To.getDate().after(jDateChooserWk3From.getDate())) {
                JOptionPane.showMessageDialog(this, "Date cannot be lower than the previous week.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk3From.setDate(null);
                jDateChooserWk3To.setDate(null);
                jDateChooserWk3From.requestFocusInWindow();
                jDateChooserWk3From.setFocusable(true);
            } else if (noOfDaysBetweenWk3 > 7) {
                JOptionPane.showMessageDialog(this, "Weekly Plan days cannot be more than seven(7) days",
                    "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk3From.setDate(null);
                jDateChooserWk3To.setDate(null);
                jDateChooserWk3From.requestFocusInWindow();
                jDateChooserWk3From.setFocusable(true);
            } else {
                wk1Site = "N";
                wk2Site = "N";
                wk3Site = "Y";
                wk4Site = "N";
                wk5Site = "N";

                jDialogWk1.setTitle("Per Diem Week 3");
                resetField();
                jDialogWk1.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonWk3AddActivityActionPerformed

    private void jButtonWk2AddActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk2AddActivityActionPerformed
        try {
            java.util.Date actPlanDate = jDateChooserWk2From.getDate();
            java.util.Date todayDate = new Date();

            long noOfDaysBetweenWk2 = 0;
            if ((!(jDateChooserWk2From.getDate() == null)) && (!(jDateChooserWk2To.getDate() == null))) {
                LocalDate dateBefore = LocalDate.parse(formatter.format(jDateChooserWk2From.getDate()));
                LocalDate dateAfter = LocalDate.parse(formatter.format(jDateChooserWk2To.getDate()));

                //calculating number of days in between
                noOfDaysBetweenWk2 = ChronoUnit.DAYS.between(dateBefore, dateAfter) + 1;
            }

            if (jDateChooserWk2From.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
                jDateChooserWk2From.requestFocusInWindow();
                jDateChooserWk2From.setFocusable(true);
            } else if (formatter.format(jDateChooserWk2From.getDate()).compareTo(tH.internetDate) < 0) {
                JOptionPane.showMessageDialog(this, "Activity date cannot be lower than today's date. Please check your dates");
                jDateChooserWk2From.requestFocusInWindow();
                jDateChooserWk2From.setFocusable(true);
            } else if ((jDateChooserWk1From.getDate() == null) || (jDateChooserWk1To.getDate() == null)) {
                JOptionPane.showMessageDialog(this, "Please complete week one before completing week two");
                jDateChooserWk2From.setDate(null);
                jDateChooserWk2To.setDate(null);
                jDateChooserWk1From.requestFocusInWindow();
                jDateChooserWk1From.setFocusable(true);
            } else if (jDateChooserWk2To.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
                jDateChooserWk2To.requestFocusInWindow();
                jDateChooserWk2To.setFocusable(true);
            } else if (jDateChooserWk2From.getDate().after(jDateChooserWk2To.getDate())) {
                JOptionPane.showMessageDialog(this, "End Date cannot be lower than start date.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk2From.setDate(null);
                jDateChooserWk2To.setDate(null);
                jDateChooserWk2From.requestFocusInWindow();
                jDateChooserWk2From.setFocusable(true);
            } else if (jDateChooserWk1To.getDate().after(jDateChooserWk2From.getDate())) {
                JOptionPane.showMessageDialog(this, "Date cannot be lower than the previous week.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk2From.setDate(null);
                jDateChooserWk2To.setDate(null);
                jDateChooserWk2From.requestFocusInWindow();
                jDateChooserWk2From.setFocusable(true);
            } else if (formatter.format(jDateChooserWk1From.getDate()).equals(formatter.format((jDateChooserWk2From.getDate())))) {
                JOptionPane.showMessageDialog(this, "Date cannot be equal to previous week.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk2From.setDate(null);
                jDateChooserWk2To.setDate(null);
                jDateChooserWk2From.requestFocusInWindow();
                jDateChooserWk2From.setFocusable(true);
            } else if (formatter.format(jDateChooserWk1To.getDate()).equals(formatter.format(jDateChooserWk2From.getDate()))) {
                JOptionPane.showMessageDialog(this, "Date cannot be equal to previous week.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk2From.setDate(null);
                jDateChooserWk2To.setDate(null);
                jDateChooserWk2From.requestFocusInWindow();
                jDateChooserWk2From.setFocusable(true);
            } else if (noOfDaysBetweenWk2 > 7) {
                JOptionPane.showMessageDialog(this, "Weekly Plan days cannot be more than seven(7) days",
                    "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk2From.setDate(null);
                jDateChooserWk2To.setDate(null);
                jDateChooserWk2From.requestFocusInWindow();
                jDateChooserWk2From.setFocusable(true);
            } else {

                wk1Site = "N";
                wk2Site = "Y";
                wk3Site = "N";
                wk4Site = "N";
                wk5Site = "N";
                jDialogWk1.setTitle("Per Diem Week 2");
                resetField();
                jDialogWk1.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonWk2AddActivityActionPerformed

    private void jButtonWk1AddActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk1AddActivityActionPerformed
        try {
            jLabelWk1Name1Del.setVisible(false);
            jLabelWk1Name2Del.setVisible(false);
            jLabelWk1Name3Del.setVisible(false);
            jLabelWk1Name4Del.setVisible(false);
            findLastMonthDay();
            java.util.Date actPlanDate = jDateChooserWk1From.getDate();
            java.util.Date todayDate = new Date();
            long noOfDaysBetweenWk1 = 0;
            if ((!(jDateChooserWk1From.getDate() == null)) && (!(jDateChooserWk1To.getDate() == null))) {
                LocalDate dateBefore = LocalDate.parse(formatter.format(jDateChooserWk1From.getDate()));
                LocalDate dateAfter = LocalDate.parse(formatter.format(jDateChooserWk1To.getDate()));

                //calculating number of days in between
                noOfDaysBetweenWk1 = ChronoUnit.DAYS.between(dateBefore, dateAfter) + 1;
            }
            if (jDateChooserWk1From.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
                jDateChooserWk1From.requestFocusInWindow();
                jDateChooserWk1From.setFocusable(true);
            } else if (jDateChooserWk1To.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
                jDateChooserWk1To.requestFocusInWindow();
                jDateChooserWk1To.setFocusable(true);
                //            } else if (formatter.format(jDateChooserWk1From.getDate()).compareTo(formatter.format(todayDate)) < 0) {
            } else if (formatter.format(jDateChooserWk1From.getDate()).compareTo(tH.internetDate) < 0) {
                JOptionPane.showMessageDialog(this, "Activity date cannot be lower than today's date.Please check your dates");
                jDateChooserWk1From.requestFocusInWindow();
                jDateChooserWk1From.setFocusable(true);

            } else if (jDateChooserWk1From.getDate().after(jDateChooserWk1To.getDate())) {
                JOptionPane.showMessageDialog(this, "End Date cannot be lower than start date.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk1From.setDate(null);
                jDateChooserWk1To.setDate(null);
                jDateChooserWk1From.requestFocusInWindow();
                jDateChooserWk1From.setFocusable(true);
            } else if (("National Office".equals(jLabelDistrict.getText())) && (noOfDaysBetweenWk1 > 7)) {
                JOptionPane.showMessageDialog(this, "Weekly Plan days cannot be more than seven(7) days haere",
                    "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk1From.setDate(null);
                jDateChooserWk1To.setDate(null);
                jDateChooserWk1From.requestFocusInWindow();
                jDateChooserWk1From.setFocusable(true);
            } else if ((!"National Office".equals(jLabelDistrict.getText())) && (lastDateofMonth.compareTo(formatter.format(jDateChooserWk1To.getDate())) < 0)) {
                JOptionPane.showMessageDialog(this, "Activity date cannot be greater than last day of the month.Please check your dates");
                jDateChooserWk1From.requestFocusInWindow();
                jDateChooserWk1From.setFocusable(true);
            } else {
                wk1Site = "Y";
                wk2Site = "N";
                wk3Site = "N";
                wk4Site = "N";
                wk5Site = "N";

                if (("National Office".equals(jLabelDistrict.getText()))) {
                    jDialogWk1.setTitle("Per Diem Week 1");
                } else {
                    jDialogWk1.setTitle("Month Per Diem ");
                }
                resetField();
                jDialogWk1.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonWk1AddActivityActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFrameMeetingPlanEdit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameMeetingPlanEdit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameMeetingPlanEdit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameMeetingPlanEdit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrameMeetingPlanEdit().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddActivityWk1;
    private javax.swing.JButton jButtonAddActivityWk2;
    private javax.swing.JButton jButtonAddActivityWk3;
    private javax.swing.JButton jButtonAddActivityWk4;
    private javax.swing.JButton jButtonAddActivityWk5;
    private javax.swing.JButton jButtonCancelBudget;
    private javax.swing.JButton jButtonCancelFacility;
    private javax.swing.JButton jButtonDialogWk1Add;
    private javax.swing.JButton jButtonDialogWk1Close;
    private javax.swing.JButton jButtonDialogWk1Reset;
    private javax.swing.JButton jButtonMeetExitActivityWk1;
    private javax.swing.JButton jButtonMeetExitActivityWk2;
    private javax.swing.JButton jButtonMeetExitActivityWk3;
    private javax.swing.JButton jButtonMeetExitActivityWk4;
    private javax.swing.JButton jButtonMeetExitActivityWk5;
    private javax.swing.JButton jButtonOk1;
    private javax.swing.JButton jButtonOkFacility;
    private javax.swing.JButton jButtonOkFacility1;
    private javax.swing.JButton jButtonReturnActivityWk1;
    private javax.swing.JButton jButtonReturnActivityWk2;
    private javax.swing.JButton jButtonReturnActivityWk3;
    private javax.swing.JButton jButtonReturnActivityWk4;
    private javax.swing.JButton jButtonReturnActivityWk5;
    private javax.swing.JButton jButtonSearch1;
    private javax.swing.JButton jButtonWk1AddActivity;
    private javax.swing.JButton jButtonWk1DelActivity;
    private javax.swing.JButton jButtonWk1DelMeetActivity;
    private javax.swing.JButton jButtonWk2AddActivity;
    private javax.swing.JButton jButtonWk2DelActivity;
    private javax.swing.JButton jButtonWk2DelMeetActivity;
    private javax.swing.JButton jButtonWk3AddActivity;
    private javax.swing.JButton jButtonWk3DelActivity;
    private javax.swing.JButton jButtonWk3DelMeetActivity;
    private javax.swing.JButton jButtonWk4AddActivity;
    private javax.swing.JButton jButtonWk4DelActivity;
    private javax.swing.JButton jButtonWk4DelMeetActivity;
    private javax.swing.JButton jButtonWk5AddActivity;
    private javax.swing.JButton jButtonWk5DelActivity;
    private javax.swing.JButton jButtonWk5DelMeetActivity;
    private javax.swing.JCheckBox jCheckBoxDialogWk1AccProved;
    private javax.swing.JCheckBox jCheckBoxDialogWk1AccUnProved;
    private javax.swing.JCheckBox jCheckBoxDialogWk1BrkFast;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Dinner;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Inc;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Lunch;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Misc;
    private javax.swing.JComboBox<String> jComboActivityProjectWk1;
    private javax.swing.JComboBox<String> jComboActivityProjectWk2;
    private javax.swing.JComboBox<String> jComboActivityProjectWk3;
    private javax.swing.JComboBox<String> jComboActivityProjectWk4;
    private javax.swing.JComboBox<String> jComboActivityProjectWk5;
    private javax.swing.JComboBox<String> jComboBoxDialogWk1PerDiem;
    private javax.swing.JComboBox<String> jComboBoxSearchResult1;
    private javax.swing.JComboBox<String> jComboBudgetCode;
    private javax.swing.JComboBox<String> jComboDistrictFacility;
    private javax.swing.JComboBox<String> jComboFacility;
    private javax.swing.JComboBox<String> jComboMeetAllowanceWk1;
    private javax.swing.JComboBox<String> jComboMeetAllowanceWk2;
    private javax.swing.JComboBox<String> jComboMeetAllowanceWk3;
    private javax.swing.JComboBox<String> jComboMeetAllowanceWk4;
    private javax.swing.JComboBox<String> jComboMeetAllowanceWk5;
    private javax.swing.JComboBox<String> jComboMeetingWk1;
    private javax.swing.JComboBox<String> jComboMeetingWk2;
    private javax.swing.JComboBox<String> jComboMeetingWk3;
    private javax.swing.JComboBox<String> jComboMeetingWk4;
    private javax.swing.JComboBox<String> jComboMeetingWk5;
    private javax.swing.JComboBox<String> jComboProjectName;
    private javax.swing.JComboBox<String> jComboProjectTask;
    private javax.swing.JComboBox<String> jComboProvinceFacility;
    private com.toedter.calendar.JDateChooser jDateChooserDialogWk1ActivityDate;
    private com.toedter.calendar.JDateChooser jDateChooserWk1From;
    private com.toedter.calendar.JDateChooser jDateChooserWk1To;
    private com.toedter.calendar.JDateChooser jDateChooserWk2From;
    private com.toedter.calendar.JDateChooser jDateChooserWk2To;
    private com.toedter.calendar.JDateChooser jDateChooserWk3From;
    private com.toedter.calendar.JDateChooser jDateChooserWk3To;
    private com.toedter.calendar.JDateChooser jDateChooserWk4From;
    private com.toedter.calendar.JDateChooser jDateChooserWk4To;
    private com.toedter.calendar.JDateChooser jDateChooserWk5From;
    private com.toedter.calendar.JDateChooser jDateChooserWk5To;
    private com.toedter.calendar.JDateChooser jDateFromWk1;
    private com.toedter.calendar.JDateChooser jDateFromWk2;
    private com.toedter.calendar.JDateChooser jDateFromWk3;
    private com.toedter.calendar.JDateChooser jDateFromWk4;
    private com.toedter.calendar.JDateChooser jDateFromWk5;
    private com.toedter.calendar.JDateChooser jDateToWk1;
    private com.toedter.calendar.JDateChooser jDateToWk2;
    private com.toedter.calendar.JDateChooser jDateToWk3;
    private com.toedter.calendar.JDateChooser jDateToWk4;
    private com.toedter.calendar.JDateChooser jDateToWk5;
    private javax.swing.JDialog jDialogBudget;
    private javax.swing.JDialog jDialogFacility;
    private javax.swing.JDialog jDialogMeetingWk1;
    private javax.swing.JDialog jDialogMeetingWk2;
    private javax.swing.JDialog jDialogMeetingWk3;
    private javax.swing.JDialog jDialogMeetingWk4;
    private javax.swing.JDialog jDialogMeetingWk5;
    private javax.swing.JDialog jDialogSearchName1;
    private javax.swing.JDialog jDialogWaiting;
    private javax.swing.JDialog jDialogWaitingEmail;
    private javax.swing.JDialog jDialogWk1;
    private javax.swing.JLabel jLabeMeetlHeadWk1;
    private javax.swing.JLabel jLabeMeetlHeadWk2;
    private javax.swing.JLabel jLabeMeetlHeadWk3;
    private javax.swing.JLabel jLabeMeetlHeadWk4;
    private javax.swing.JLabel jLabeMeetlHeadWk5;
    private javax.swing.JLabel jLabel$Wk1;
    private javax.swing.JLabel jLabel$Wk2;
    private javax.swing.JLabel jLabel$Wk3;
    private javax.swing.JLabel jLabel$Wk4;
    private javax.swing.JLabel jLabel$Wk5;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelActivityDateWk1;
    private javax.swing.JLabel jLabelActivityDateWk2;
    private javax.swing.JLabel jLabelActivityDateWk3;
    private javax.swing.JLabel jLabelActivityDateWk4;
    private javax.swing.JLabel jLabelActivityDateWk5;
    private javax.swing.JLabel jLabelActivityProjectWk1;
    private javax.swing.JLabel jLabelActivityProjectWk2;
    private javax.swing.JLabel jLabelActivityProjectWk3;
    private javax.swing.JLabel jLabelActivityProjectWk4;
    private javax.swing.JLabel jLabelActivityProjectWk5;
    private javax.swing.JLabel jLabelBudgetCode;
    private javax.swing.JLabel jLabelBudgetHeadWk1;
    private javax.swing.JLabel jLabelBudgetHeadWk2;
    private javax.swing.JLabel jLabelBudgetHeadWk3;
    private javax.swing.JLabel jLabelBudgetHeadWk4;
    private javax.swing.JLabel jLabelBudgetHeadWk5;
    private javax.swing.JLabel jLabelBudgetLineWk1;
    private javax.swing.JLabel jLabelBudgetLineWk2;
    private javax.swing.JLabel jLabelBudgetLineWk3;
    private javax.swing.JLabel jLabelBudgetLineWk4;
    private javax.swing.JLabel jLabelBudgetLineWk5;
    private javax.swing.JLabel jLabelBusFareWk1;
    private javax.swing.JLabel jLabelBusFareWk2;
    private javax.swing.JLabel jLabelBusFareWk3;
    private javax.swing.JLabel jLabelBusFareWk4;
    private javax.swing.JLabel jLabelBusFareWk5;
    private javax.swing.JLabel jLabelCommentsHeading;
    private javax.swing.JLabel jLabelDetailDescriptionWk1;
    private javax.swing.JLabel jLabelDetailDescriptionWk2;
    private javax.swing.JLabel jLabelDetailDescriptionWk3;
    private javax.swing.JLabel jLabelDetailDescriptionWk4;
    private javax.swing.JLabel jLabelDetailDescriptionWk5;
    private javax.swing.JLabel jLabelDialogPerDiem;
    private javax.swing.JLabel jLabelDialogWk1ActivityDate;
    private javax.swing.JLabel jLabelDialogWk1Site;
    private javax.swing.JLabel jLabelDist;
    private javax.swing.JLabel jLabelDistrict;
    private javax.swing.JLabel jLabelDistrictFacility;
    private javax.swing.JLabel jLabelEmp;
    private javax.swing.JLabel jLabelFacility;
    private javax.swing.JLabel jLabelFromWk1;
    private javax.swing.JLabel jLabelFromWk2;
    private javax.swing.JLabel jLabelFromWk3;
    private javax.swing.JLabel jLabelFromWk4;
    private javax.swing.JLabel jLabelFromWk5;
    private javax.swing.JLabel jLabelGenLogNam1;
    private javax.swing.JLabel jLabelGenLogNam3;
    private javax.swing.JLabel jLabelGenLogNam5;
    private javax.swing.JLabel jLabelGenLogNam6;
    private javax.swing.JLabel jLabelGenLogNam7;
    private javax.swing.JLabel jLabelGenLogNam8;
    private javax.swing.JLabel jLabelHeader1;
    private javax.swing.JLabel jLabelHeader2;
    private javax.swing.JLabel jLabelHeaderGen10;
    private javax.swing.JLabel jLabelHeaderGen4;
    private javax.swing.JLabel jLabelHeaderGen6;
    private javax.swing.JLabel jLabelHeaderGen7;
    private javax.swing.JLabel jLabelHeaderGen8;
    private javax.swing.JLabel jLabelHeaderGen9;
    private javax.swing.JLabel jLabelLineDate;
    private javax.swing.JLabel jLabelLineDate1;
    private javax.swing.JLabel jLabelLineDate2;
    private javax.swing.JLabel jLabelLineDate3;
    private javax.swing.JLabel jLabelLineDate4;
    private javax.swing.JLabel jLabelLineDate5;
    private javax.swing.JLabel jLabelLineLogNam;
    private javax.swing.JLabel jLabelLineLogNam1;
    private javax.swing.JLabel jLabelLineLogNam2;
    private javax.swing.JLabel jLabelLineLogNam3;
    private javax.swing.JLabel jLabelLineLogNam4;
    private javax.swing.JLabel jLabelLineLogNam5;
    private javax.swing.JLabel jLabelLineTime;
    private javax.swing.JLabel jLabelLineTime1;
    private javax.swing.JLabel jLabelLineTime2;
    private javax.swing.JLabel jLabelLineTime3;
    private javax.swing.JLabel jLabelLineTime4;
    private javax.swing.JLabel jLabelLineTime5;
    private javax.swing.JLabel jLabelLogo1;
    private javax.swing.JLabel jLabelLogo2;
    private javax.swing.JLabel jLabelLogo3;
    private javax.swing.JLabel jLabelLogo4;
    private javax.swing.JLabel jLabelLogo5;
    private javax.swing.JLabel jLabelLogo6;
    private javax.swing.JLabel jLabelMainPurposeWk1;
    private javax.swing.JLabel jLabelMainPurposeWk2;
    private javax.swing.JLabel jLabelMainPurposeWk3;
    private javax.swing.JLabel jLabelMainPurposeWk4;
    private javax.swing.JLabel jLabelMainPurposeWk5;
    private javax.swing.JLabel jLabelNumDaysWk1;
    private javax.swing.JLabel jLabelNumDaysWk2;
    private javax.swing.JLabel jLabelNumDaysWk3;
    private javax.swing.JLabel jLabelNumDaysWk4;
    private javax.swing.JLabel jLabelNumDaysWk5;
    private javax.swing.JLabel jLabelNumPpleWk1;
    private javax.swing.JLabel jLabelNumPpleWk2;
    private javax.swing.JLabel jLabelNumPpleWk3;
    private javax.swing.JLabel jLabelNumPpleWk4;
    private javax.swing.JLabel jLabelNumPpleWk5;
    private javax.swing.JLabel jLabelOffice1;
    private javax.swing.JLabel jLabelPersonRespWk1;
    private javax.swing.JLabel jLabelPersonRespWk2;
    private javax.swing.JLabel jLabelPersonRespWk3;
    private javax.swing.JLabel jLabelPersonRespWk4;
    private javax.swing.JLabel jLabelPersonRespWk5;
    private javax.swing.JLabel jLabelPlanRefNo;
    private javax.swing.JLabel jLabelPostAppMod1;
    private javax.swing.JLabel jLabelPostAppMod2;
    private javax.swing.JLabel jLabelPostAppMod3;
    private javax.swing.JLabel jLabelPostAppMod4;
    private javax.swing.JLabel jLabelPostAppMod5;
    private javax.swing.JLabel jLabelProjectName;
    private javax.swing.JLabel jLabelProjectTask;
    private javax.swing.JLabel jLabelProvince;
    private javax.swing.JLabel jLabelProvince1;
    private javax.swing.JLabel jLabelProvinceFacility;
    private javax.swing.JLabel jLabelRemain;
    private javax.swing.JLabel jLabelRequestionTotalWk1;
    private javax.swing.JLabel jLabelRequestionTotalWk2;
    private javax.swing.JLabel jLabelRequestionTotalWk3;
    private javax.swing.JLabel jLabelRequestionTotalWk4;
    private javax.swing.JLabel jLabelRequestionTotalWk5;
    private javax.swing.JLabel jLabelSerial;
    private javax.swing.JLabel jLabelToWk1;
    private javax.swing.JLabel jLabelToWk2;
    private javax.swing.JLabel jLabelToWk3;
    private javax.swing.JLabel jLabelToWk4;
    private javax.swing.JLabel jLabelToWk5;
    private javax.swing.JLabel jLabelTotalAmtWk1;
    private javax.swing.JLabel jLabelTotalAmtWk2;
    private javax.swing.JLabel jLabelTotalAmtWk3;
    private javax.swing.JLabel jLabelTotalAmtWk4;
    private javax.swing.JLabel jLabelTotalAmtWk5;
    private javax.swing.JLabel jLabelUnitCostWk1;
    private javax.swing.JLabel jLabelUnitCostWk2;
    private javax.swing.JLabel jLabelUnitCostWk3;
    private javax.swing.JLabel jLabelUnitCostWk4;
    private javax.swing.JLabel jLabelUnitCostWk5;
    private javax.swing.JLabel jLabelWk1DialogActivityDesc;
    private javax.swing.JLabel jLabelWk1DialogJustification;
    private javax.swing.JLabel jLabelWk1DialogStaffName;
    private javax.swing.JLabel jLabelWk1DialogStaffName1;
    private javax.swing.JLabel jLabelWk1DialogStaffName2;
    private javax.swing.JLabel jLabelWk1From;
    private javax.swing.JLabel jLabelWk1From1;
    private javax.swing.JLabel jLabelWk1Misc;
    private javax.swing.JLabel jLabelWk1MiscAmt;
    private javax.swing.JLabel jLabelWk1Name1Del;
    private javax.swing.JLabel jLabelWk1Name2Del;
    private javax.swing.JLabel jLabelWk1Name3Del;
    private javax.swing.JLabel jLabelWk1Name4Del;
    private javax.swing.JLabel jLabelWk1To;
    private javax.swing.JLabel jLabelWk1To1;
    private javax.swing.JLabel jLabelWk3From;
    private javax.swing.JLabel jLabelWk3To;
    private javax.swing.JLabel jLabelWk4From;
    private javax.swing.JLabel jLabelWk4To;
    private javax.swing.JLabel jLabelWk5From;
    private javax.swing.JLabel jLabelWk5To;
    private javax.swing.JLabel jLabelWkDuration;
    private javax.swing.JLabel jLabelWkDuration1;
    private javax.swing.JLabel jLabelWkDuration2;
    private javax.swing.JLabel jLabelWkDuration4;
    private javax.swing.JLabel jLabelWkDuration5;
    private javax.swing.JMenuItem jMenuAcqEdit;
    private javax.swing.JMenu jMenuAcquittal;
    private javax.swing.JMenuItem jMenuAgeAlluser;
    private javax.swing.JMenuItem jMenuAgeByUser;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenu jMenuEdit;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenuItem jMenuItem3WkPlan;
    private javax.swing.JMenuItem jMenuItemAccMgrRev;
    private javax.swing.JMenuItem jMenuItemAcqAccApp;
    private javax.swing.JMenuItem jMenuItemAcqFinApp;
    private javax.swing.JMenuItem jMenuItemAcqHeadApp;
    private javax.swing.JMenuItem jMenuItemAcqSchGen;
    private javax.swing.JMenuItem jMenuItemAcqSupApp;
    private javax.swing.JMenuItem jMenuItemAcqView;
    private javax.swing.JMenuItem jMenuItemAcquittal;
    private javax.swing.JMenuItem jMenuItemClose;
    private javax.swing.JMenuItem jMenuItemClose1;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuPlanApproval;
    private javax.swing.JMenuItem jMenuItemHeadApp;
    private javax.swing.JMenuItem jMenuItemMeetAcquittal;
    private javax.swing.JMenuItem jMenuItemMeetGenLst;
    private javax.swing.JMenuItem jMenuItemPlanView;
    private javax.swing.JMenuItem jMenuItemProvMgrApproval;
    private javax.swing.JMenuItem jMenuItemReqGenLst;
    private javax.swing.JMenuItem jMenuItemReqSubmitted;
    private javax.swing.JMenuItem jMenuItemRequest;
    private javax.swing.JMenuItem jMenuItemRequestMOH;
    private javax.swing.JMenuItem jMenuItemSchGen;
    private javax.swing.JMenuItem jMenuItemSpecialAcquittal;
    private javax.swing.JMenuItem jMenuItemSupApp;
    private javax.swing.JMenuItem jMenuItemView;
    private javax.swing.JMenuItem jMenuMonPlanEdit;
    private javax.swing.JMenu jMenuMonthlyPlan;
    private javax.swing.JMenu jMenuNew;
    private javax.swing.JMenuItem jMenuProcessTimeline;
    private javax.swing.JMenu jMenuReports;
    private javax.swing.JMenuItem jMenuReqEdit;
    private javax.swing.JMenu jMenuRequest;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanelAmtWk1;
    private javax.swing.JPanel jPanelAmtWk2;
    private javax.swing.JPanel jPanelAmtWk3;
    private javax.swing.JPanel jPanelAmtWk4;
    private javax.swing.JPanel jPanelAmtWk5;
    private javax.swing.JPanel jPanelDescWk1;
    private javax.swing.JPanel jPanelDescWk2;
    private javax.swing.JPanel jPanelDescWk3;
    private javax.swing.JPanel jPanelDescWk4;
    private javax.swing.JPanel jPanelDescWk5;
    private javax.swing.JPanel jPanelMeetingWk1;
    private javax.swing.JPanel jPanelMeetingWk2;
    private javax.swing.JPanel jPanelMeetingWk3;
    private javax.swing.JPanel jPanelMeetingWk4;
    private javax.swing.JPanel jPanelMeetingWk5;
    private javax.swing.JPanel jPanelSearchID;
    private javax.swing.JPanel jPanelWkComments;
    private javax.swing.JPanel jPanelWkFive;
    private javax.swing.JPanel jPanelWkFour;
    private javax.swing.JPanel jPanelWkOne;
    private javax.swing.JPanel jPanelWkThree;
    private javax.swing.JPanel jPanelWkTwo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator13;
    private javax.swing.JPopupMenu.Separator jSeparator14;
    private javax.swing.JPopupMenu.Separator jSeparator19;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator22;
    private javax.swing.JPopupMenu.Separator jSeparator23;
    private javax.swing.JPopupMenu.Separator jSeparator24;
    private javax.swing.JPopupMenu.Separator jSeparator25;
    private javax.swing.JPopupMenu.Separator jSeparator26;
    private javax.swing.JPopupMenu.Separator jSeparator27;
    private javax.swing.JPopupMenu.Separator jSeparator28;
    private javax.swing.JPopupMenu.Separator jSeparator29;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator30;
    private javax.swing.JPopupMenu.Separator jSeparator31;
    private javax.swing.JPopupMenu.Separator jSeparator32;
    private javax.swing.JPopupMenu.Separator jSeparator33;
    private javax.swing.JPopupMenu.Separator jSeparator34;
    private javax.swing.JSeparator jSeparator35;
    private javax.swing.JSeparator jSeparator36;
    private javax.swing.JPopupMenu.Separator jSeparator39;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator40;
    private javax.swing.JSeparator jSeparator41;
    private javax.swing.JSeparator jSeparator42;
    private javax.swing.JSeparator jSeparator43;
    private javax.swing.JSeparator jSeparator44;
    private javax.swing.JPopupMenu.Separator jSeparator47;
    private javax.swing.JPopupMenu.Separator jSeparator48;
    private javax.swing.JPopupMenu.Separator jSeparator49;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator50;
    private javax.swing.JPopupMenu.Separator jSeparator51;
    private javax.swing.JPopupMenu.Separator jSeparator52;
    private javax.swing.JPopupMenu.Separator jSeparator53;
    private javax.swing.JPopupMenu.Separator jSeparator54;
    private javax.swing.JPopupMenu.Separator jSeparator55;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JTabbedPane jTabbedPaneMain;
    private javax.swing.JTable jTableMeetReqWk1;
    private javax.swing.JTable jTableMeetReqWk2;
    private javax.swing.JTable jTableMeetReqWk3;
    private javax.swing.JTable jTableMeetReqWk4;
    private javax.swing.JTable jTableMeetReqWk5;
    private javax.swing.JTable jTableWk1Activities;
    private javax.swing.JTable jTableWk2Activities;
    private javax.swing.JTable jTableWk3Activities;
    private javax.swing.JTable jTableWk4Activities;
    private javax.swing.JTable jTableWk5Activities;
    private javax.swing.JTextArea jTextAreaComments;
    private javax.swing.JTextArea jTextAreaDetailDescriptionWk1;
    private javax.swing.JTextArea jTextAreaDetailDescriptionWk2;
    private javax.swing.JTextArea jTextAreaDetailDescriptionWk3;
    private javax.swing.JTextArea jTextAreaDetailDescriptionWk4;
    private javax.swing.JTextArea jTextAreaDetailDescriptionWk5;
    private javax.swing.JTextArea jTextAreaWk1DialogJustification;
    private javax.swing.JTextField jTextBusFareWk1;
    private javax.swing.JTextField jTextBusFareWk2;
    private javax.swing.JTextField jTextBusFareWk3;
    private javax.swing.JTextField jTextBusFareWk4;
    private javax.swing.JTextField jTextBusFareWk5;
    private javax.swing.JTextField jTextDistDest;
    private javax.swing.JTextField jTextFieldBudgetWk1;
    private javax.swing.JTextField jTextFieldBudgetWk2;
    private javax.swing.JTextField jTextFieldBudgetWk3;
    private javax.swing.JTextField jTextFieldBudgetWk4;
    private javax.swing.JTextField jTextFieldBudgetWk5;
    private javax.swing.JTextField jTextFieldDialogWk1Site;
    private javax.swing.JTextField jTextFieldRefNum;
    private javax.swing.JTextField jTextFieldSearchNam1;
    private javax.swing.JTextField jTextFieldWk1DialogActivityDesc;
    private javax.swing.JTextField jTextFieldWk1DialogStaffName1;
    private javax.swing.JTextField jTextFieldWk1DialogStaffName2;
    private javax.swing.JTextField jTextFieldWk1DialogStaffName3;
    private javax.swing.JTextField jTextFieldWk1DialogStaffName4;
    private javax.swing.JTextField jTextFieldWk1Misc;
    private javax.swing.JTextField jTextFieldWk1MiscAmt;
    private javax.swing.JTextField jTextNumDaysWk1;
    private javax.swing.JTextField jTextNumDaysWk2;
    private javax.swing.JTextField jTextNumDaysWk3;
    private javax.swing.JTextField jTextNumDaysWk4;
    private javax.swing.JTextField jTextNumDaysWk5;
    private javax.swing.JTextField jTextNumPpleWk1;
    private javax.swing.JTextField jTextNumPpleWk2;
    private javax.swing.JTextField jTextNumPpleWk3;
    private javax.swing.JTextField jTextNumPpleWk4;
    private javax.swing.JTextField jTextNumPpleWk5;
    private javax.swing.JTextField jTextPersonResWk1;
    private javax.swing.JTextField jTextPersonResWk2;
    private javax.swing.JTextField jTextPersonResWk3;
    private javax.swing.JTextField jTextPersonResWk4;
    private javax.swing.JTextField jTextPersonResWk5;
    private javax.swing.JTextField jTextUnitCostWk1;
    private javax.swing.JTextField jTextUnitCostWk2;
    private javax.swing.JTextField jTextUnitCostWk3;
    private javax.swing.JTextField jTextUnitCostWk4;
    private javax.swing.JTextField jTextUnitCostWk5;
    // End of variables declaration//GEN-END:variables
}
