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
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import ClaimAppendix1.*;
import ClaimAppendix2.*;
import ClaimReports.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import utils.timeHost;

/**
 *
 * @author goredemac
 */
public class JFrameMnthPlan extends javax.swing.JFrame {

    timeHost tH = new timeHost();
    String ipAdd =    "ophidutilapp.southafricanorth.cloudapp.azure.com:16432"       ;
    String usrName = "appfin";
    String usrPass = "542ytDYvynv$TVYb";
    String mailUsrNam = "finance@ophid.co.zw";
    String mailUsrPass = "MgqM5utyUr43x#";
    boolean ignoreInput = false;
    String filename = null;
    int charMaxWk1 = 200;
    int charMaxWk2 = 200;
    int charMaxWk3 = 200;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String wk1Site = "N";
    String wk2Site = "N";
    String wk3Site = "N";
    String hostName = "";
    String breakfastAll, lunchAll, lunchNPAll, lunchPAll, dinnerAll,
            incidentalAll, unProvedAll, date1, date2, usrnam, sendToProvMgr, supNam, supUsrMail,
            provMgrMail, createUsrNam, usrRecNam, UsrRecWk, actDate, duplicateUser1Count, duplicateUser2Count,
            duplicateUser3Count, duplicateUser4Count, usrGrp,
            planWk, empNamNum1, empNam1, empNamNum2, empNam2, empNamNum3, empNam3, empNamNum4, empNam4, planDate;
    PreparedStatement pst1 = null;
    PreparedStatement pst = null;
    int itmNum = 1;

    /**
     * Creates new form JFrame3WkPlan
     */
    public JFrameMnthPlan() {
        initComponents();
        findProvince();
        allowanceRate();
        showDate();
        showTime();
        computerName();
       

    }

    public JFrameMnthPlan(String usrLogNam) {
        initComponents();
        findProvince();
        allowanceRate();
        showDate();
        showTime();
        computerName();
        jLabelEmp.setText(usrLogNam);
        jLabelEmp.setVisible(false);
 jMenuItemRequest.setEnabled(false);
 jMenuItemSpecialAcquittal.setEnabled(false);
        findUser();
        jLabelDist.setVisible(true);
        jTextDistDest.setVisible(true);
        jTextDistDest.setText("");
        empProvOffTTL();
        jLabelWk1Name1Del.setVisible(false);
        jLabelWk1Name2Del.setVisible(false);
        jLabelWk1Name3Del.setVisible(false);
        jLabelWk1Name4Del.setVisible(false);
        jLabelWk2Name1Del.setVisible(false);
        jLabelWk2Name2Del.setVisible(false);
        jLabelWk2Name3Del.setVisible(false);
        jLabelWk2Name4Del.setVisible(false);
        jLabelWk3Name1Del.setVisible(false);
        jLabelWk3Name2Del.setVisible(false);
        jLabelWk3Name3Del.setVisible(false);
        jLabelWk3Name4Del.setVisible(false);
        findUserGrp();

    }

    void computerName() {

        try {
            java.net.InetAddress addr = InetAddress.getLocalHost();
            hostName = addr.getHostName();

        } catch (Exception ex) {
            System.out.println("Hostname can not be resolved");
        }
    }

    void CheckDuplicateUser1() {
        try {

            duplicateUser1Count = null;

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT distinct PLAN_REF_NUM,ACT_DATE,count(act_date) FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[" + planWk + "]  where ACT_DATE = '"
                    + planDate + "' "
                    + "and  (" + empNamNum1 + " = '" + empNam1 + "' or  " + empNamNum2 + " = '" + empNam1 + ""
                    + "' or " + empNamNum3 + " = '" + empNam1 + "' or " + empNamNum4 + " = '" + empNam1 + "')"
                    + "AND ACT_REC_STA in ('C','A') group by PLAN_REF_NUM,ACT_DATE");

            while (r.next()) {

                duplicateUser1Count = r.getString(3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void CheckDuplicateUser2() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT distinct PLAN_REF_NUM,ACT_DATE,count(act_date) FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[" + planWk + "]  where ACT_DATE = '"
                    + planDate + "' "
                    + "and  (" + empNamNum1 + " = '" + empNam1 + "' or  " + empNamNum2 + " = '" + empNam2 + "' or " + empNamNum3 + " = '" + empNam3 + "' or " + empNamNum4 + " = '" + empNam4 + "')"
                    + "AND ACT_REC_STA in ('C','A') group by PLAN_REF_NUM,ACT_DATE");

            while (r.next()) {

                duplicateUser2Count = r.getString(3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void CheckDuplicateUser3() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT distinct PLAN_REF_NUM,ACT_DATE,count(act_date) FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[" + planWk + "]  where ACT_DATE = '"
                    + planDate + "' "
                    + "and  (" + empNamNum1 + " = '" + empNam1 + "' or  " + empNamNum2 + " = '" + empNam2 + "' or " + empNamNum3 + " = '" + empNam3 + "' or " + empNamNum4 + " = '" + empNam4 + "')"
                    + "AND ACT_REC_STA in ('C','A') group by PLAN_REF_NUM,ACT_DATE");

            while (r.next()) {

                duplicateUser3Count = r.getString(3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void CheckDuplicateUser4() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT distinct PLAN_REF_NUM,ACT_DATE,count(act_date) FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[" + planWk + "]  where ACT_DATE = '"
                    + planDate + "' "
                    + "and  (" + empNamNum1 + " = '" + empNam1 + "' or  " + empNamNum2 + " = '" + empNam2 + "' or " + empNamNum3 + " = '" + empNam3 + "' or " + empNamNum4 + " = '" + empNam4 + "')"
                    + "AND ACT_REC_STA in ('C','A') group by PLAN_REF_NUM,ACT_DATE");

            while (r.next()) {

                duplicateUser4Count = r.getString(3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void findUser() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select * from [ClaimsAppSysZvandiri].[dbo]."
                    + "[EmpDetTab] where emp_num ='" + jLabelEmp.getText() + "'");

            while (r.next()) {

                jLabelLineLogNam.setText(r.getString(2));
                jLabelLineLogNam1.setText(r.getString(2));
                jLabelLineLogNam2.setText(r.getString(2));
                createUsrNam = r.getString(2);
//                sendTo = r.getString(5);
                supNam = r.getString(5);
                supUsrMail = r.getString(6);
            }

            //                 conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Connect to Database (Province).Try Again", "Error Connection", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
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

    void SerialCheck() {

        int count = 0;
        do {

            try {

                Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                        + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

                String sqlDeleteBlankLock = "delete [ClaimsAppSysZvandiri].[dbo].[SerialLock] where (SERIAL = ' ' or SERIAL is null or REF_NUM = 0)";

                pst = conn.prepareStatement(sqlDeleteBlankLock);
                pst.executeUpdate();

                Statement st = conn.createStatement();

                st.executeQuery("SELECT count(*) FROM [ClaimsAppSysZvandiri].[dbo].[SerialLock] where SERIAL = 'P'");

                ResultSet rs = st.getResultSet();
                //  jLabelRegNum.setVisible(false);
                while (rs.next()) {
                    count = Integer.parseInt(rs.getString(1));

                }
                // conn.close();

                if (count > 0) {
                    try {

                        Statement st1 = conn.createStatement();

                        st1.executeQuery("SELECT USR_NAM,LCK_DATE_TIME FROM [ClaimsAppSysZvandiri].[dbo].[SerialLock] where SERIAL = 'P'");

                        ResultSet rs1 = st1.getResultSet();

                        while (rs1.next()) {
                            usrnam = rs1.getString(1);
                            date1 = rs1.getString(2);
                        }

                        Statement st2 = conn.createStatement();

                        st2.executeQuery("select SYSDATETIME()");

                        ResultSet rs2 = st2.getResultSet();

                        while (rs2.next()) {
                            date2 = rs2.getString(1);
                        }

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date d1 = null;
                        Date d2 = null;
                        String result = null;

                        try {
                            d1 = format.parse(date1);
                            d2 = format.parse(date2);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        long diff = d2.getTime() - d1.getTime();

                        /**
                         * remove the milliseconds part
                         */
                        diff = diff / 1000;

                        long days = diff / (24 * 60 * 60);
                        long hours = diff / (60 * 60) % 24;
                        long minutes = diff / 60 % 60;
                        long seconds = diff % 60;

                        result = days + " days, ";
                        result += hours + " hours, ";
                        result += minutes + " minutes, ";
                        result += seconds + " seconds.";

                        if ((jLabelLineLogNam.getText().equals(usrnam)) && (seconds >= 1)) {

                            deleteLongLock();
                        } else if ((minutes >= 5) || (hours >= 1) || (days >= 1)) {

                            deleteLongLock();
                        }

                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, e1);
                    }
                }
                jDialogWaiting.setVisible(true);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } while (count > 0);

        jDialogWaiting.setVisible(false);

    }

    void deleteLongLock() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            String sqlDeleteLock = "delete [ClaimsAppSysZvandiri].[dbo].[SerialLock] where SERIAL = "
                    + "'P'";

            pst = conn.prepareStatement(sqlDeleteLock);
            pst.executeUpdate();

        } catch (Exception e) {

        }
    }

    void RefNumAllocation() {

        try {

            String sqlSerialLock = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[SerialLock] "
                    + "(SERIAL,REF_NUM,USR_NAM) VALUES (?,?,?)";

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            Statement st = conn.createStatement();

            st.executeQuery("SELECT REF_NUM + 1,SERIAL,REF_NUM FROM [ClaimsAppSysZvandiri].[dbo].[refNumTab] where SERIAL = 'P'");

            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                jLabelRefNum.setText(rs.getString(1));
                jLabelSerial.setText(rs.getString(2));
                //    oldRefNum = rs.getString(3);

            }
            pst = conn.prepareStatement(sqlSerialLock);
            pst.setString(1, jLabelSerial.getText());
            pst.setString(2, jLabelRefNum.getText());
            pst.setString(3, jLabelLineLogNam.getText());

            pst.executeUpdate();

            String sqlSerialLockUpdate = "update [ClaimsAppSysZvandiri].[dbo].[SerialLock]"
                    + " set LCK_DATE_TIME = ( SELECT SYSDATETIME()) where "
                    + "concat(SERIAL,REF_NUM) = '" + jLabelSerial.getText() + jLabelRefNum.getText() + "' ";

            pst1 = conn.prepareStatement(sqlSerialLockUpdate);
            pst1.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void refNumUpdate() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            String sql1 = "update [ClaimsAppSysZvandiri].[dbo].[refNumTab] set "
                    + "REF_NUM ='" + jLabelRefNum.getText() + "' where SERIAL = 'P'";

            pst = conn.prepareStatement(sql1);
            pst.executeUpdate();

            deleteLongLock();

            JOptionPane.showMessageDialog(this, "<html>Plan Ref No. <b>" + jLabelSerial.getText() + " " + jLabelRefNum.getText()
                    + "</b> successfully registered.</html>");

            sendMail();
            jDialogWaitingEmail.setVisible(false);
            JOptionPane.showMessageDialog(this, "An email has been sent to the Provincial Manager.");
            new JFrameMnthPlan(jLabelEmp.getText()).setVisible(true);
            setVisible(false);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

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

            }
        }) {

        }.start();

    }

    void showDate() {
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");

        jLabelLineDate.setText(s.format(d));
        jLabelLineDate1.setText(s.format(d));
        jLabelLineDate2.setText(s.format(d));

    }

    void findUserGrp() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
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
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
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

    void empProvOffTTL() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT EMP_NUM ,EMP_PROVINCE,EMP_OFFICE,EMP_TTL "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetWS] where EMP_NUM ='" + jLabelEmp.getText() + "'");

            while (r.next()) {
                jLabelProvince.setText(r.getString(2));
                jLabelDistrict.setText(r.getString(3));

            }

            //                 conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findProvince() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

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

    void findRankCat() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();

            //jComboProvince.setSelectedIndex(-1);
            ResultSet r = st.executeQuery("SELECT [RANK],[DIS_BASE],[CATEGORY]"
                    + " FROM [ClaimsAppSysZvandiri].[dbo].[DistFacTab] where "
                    + "FACILITY = '" + jComboFacility.getSelectedItem().toString() + "'");

            while (r.next()) {
                if ("Y".equals(wk1Site)) {
                    jLabelDialogWk1ActSiteClass.setText(r.getString(3));
                    jLabelWk1DialogActRank.setText(r.getString(1));
                    if (((jComboDistrictFacility.getSelectedItem().toString().equals(jComboFacility.getSelectedItem().toString()))
                            || ("Central Office".equals(jLabelProvince.getText())) || ("Harare Province".equals(jLabelProvince.getText())))) {
                        jLabelWk1DialogActDis.setText(jTextDistDest.getText());
                    } else {
                        jLabelWk1DialogActDis.setText(r.getString(2));
                    }
                    Wk1DistLess100();
                } else if ("Y".equals(wk2Site)) {
                    jLabelDialogWk2ActSiteClass.setText(r.getString(3));
                    jLabelWk2DialogActRank.setText(r.getString(1));
                    if (((jComboDistrictFacility.getSelectedItem().toString().equals(jComboFacility.getSelectedItem().toString()))
                            || ("Central Office".equals(jLabelProvince.getText())) || ("Harare Province".equals(jLabelProvince.getText())))) {
                        jLabelWk2DialogActDis.setText(jTextDistDest.getText());
                    } else {
                        jLabelWk2DialogActDis.setText(r.getString(2));
                    }
                    Wk2DistLess100();
                } else if ("Y".equals(wk3Site)) {
                    jLabelDialogWk3ActSiteClass.setText(r.getString(3));
                    jLabelWk3DialogActRank.setText(r.getString(1));
                    if (((jComboDistrictFacility.getSelectedItem().toString().equals(jComboFacility.getSelectedItem().toString()))
                            || ("Central Office".equals(jLabelProvince.getText())) || ("Harare Province".equals(jLabelProvince.getText())))) {
                        jLabelWk3DialogActDis.setText(jTextDistDest.getText());
                    } else {
                        jLabelWk3DialogActDis.setText(r.getString(2));
                    }
                    Wk3DistLess100();
                }

            }

            //                 conn.close();
        } catch (Exception e) {

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

    void allowanceRate() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAllowanceTab]"
                    + " where RateOrg = 'OPHID' and RateStatus ='A'");

            while (r.next()) {

                breakfastAll = r.getString(1);
                lunchAll = r.getString(2);
                lunchNPAll = r.getString(3);
                lunchPAll = r.getString(4);
                dinnerAll = r.getString(5);
                incidentalAll = r.getString(6);
                unProvedAll = r.getString(7);

            }

            //                 conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Connect to Database (Province).Try Again", "Error Connection", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
    }

    void UsrRecUpd() {

        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk1Activities.getValueAt(i, 16).toString()))) {
                usrRecNam = jTableWk1Activities.getValueAt(i, 16).toString();
                actDate = jTableWk1Activities.getValueAt(i, 0).toString();
                UsrRecWk = "1";
                WkUsrRecUpd();
            }
        }

        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk1Activities.getValueAt(i, 17).toString()))) {
                usrRecNam = jTableWk1Activities.getValueAt(i, 17).toString();
                actDate = jTableWk1Activities.getValueAt(i, 0).toString();
                UsrRecWk = "1";
                WkUsrRecUpd();
            }
        }
        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk1Activities.getValueAt(i, 18).toString()))) {
                usrRecNam = jTableWk1Activities.getValueAt(i, 18).toString();
                actDate = jTableWk1Activities.getValueAt(i, 0).toString();
                UsrRecWk = "1";
                WkUsrRecUpd();
            }
        }
        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk1Activities.getValueAt(i, 19).toString()))) {
                usrRecNam = jTableWk1Activities.getValueAt(i, 19).toString();
                actDate = jTableWk1Activities.getValueAt(i, 0).toString();
                UsrRecWk = "1";
                WkUsrRecUpd();
            }
        }

        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk2Activities.getValueAt(i, 16).toString()))) {
                usrRecNam = jTableWk2Activities.getValueAt(i, 16).toString();
                actDate = jTableWk2Activities.getValueAt(i, 0).toString();
                UsrRecWk = "2";
                WkUsrRecUpd();
            }
        }

        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk2Activities.getValueAt(i, 17).toString()))) {
                usrRecNam = jTableWk2Activities.getValueAt(i, 17).toString();
                actDate = jTableWk2Activities.getValueAt(i, 0).toString();
                UsrRecWk = "2";
                WkUsrRecUpd();
            }
        }
        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk2Activities.getValueAt(i, 18).toString()))) {
                usrRecNam = jTableWk2Activities.getValueAt(i, 18).toString();
                actDate = jTableWk2Activities.getValueAt(i, 0).toString();
                UsrRecWk = "2";
                WkUsrRecUpd();
            }
        }
        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk2Activities.getValueAt(i, 19).toString()))) {
                usrRecNam = jTableWk2Activities.getValueAt(i, 19).toString();
                actDate = jTableWk2Activities.getValueAt(i, 0).toString();
                UsrRecWk = "2";
                WkUsrRecUpd();
            }
        }

        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk3Activities.getValueAt(i, 16).toString()))) {
                usrRecNam = jTableWk3Activities.getValueAt(i, 16).toString();
                actDate = jTableWk3Activities.getValueAt(i, 0).toString();
                UsrRecWk = "3";
                WkUsrRecUpd();
            }
        }

        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk3Activities.getValueAt(i, 17).toString()))) {
                usrRecNam = jTableWk3Activities.getValueAt(i, 17).toString();
                actDate = jTableWk3Activities.getValueAt(i, 0).toString();
                UsrRecWk = "3";
                WkUsrRecUpd();
            }
        }
        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk3Activities.getValueAt(i, 18).toString()))) {
                usrRecNam = jTableWk3Activities.getValueAt(i, 18).toString();
                actDate = jTableWk3Activities.getValueAt(i, 0).toString();
                UsrRecWk = "3";
                WkUsrRecUpd();
            }
        }
        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk3Activities.getValueAt(i, 19).toString()))) {
                usrRecNam = jTableWk3Activities.getValueAt(i, 19).toString();
                actDate = jTableWk3Activities.getValueAt(i, 0).toString();
                UsrRecWk = "3";
                WkUsrRecUpd();
            }
        }
    }

    void WkUsrRecUpd() {

        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            String sqlrecplan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanUsrRecTab] "
                    + "(SERIAL,PLAN_REF_NUM,ACT_DATE,EMP_NAM,PLAN_WK,STATUS) VALUES (?,?,?,?,?,?)";
            pst1 = conn.prepareStatement(sqlrecplan);
            pst1.setString(1, "P");
            pst1.setString(2, jLabelRefNum.getText());
            pst1.setString(3, actDate);
            pst1.setString(4, usrRecNam);
            pst1.setString(5, UsrRecWk);
            pst1.setString(6, "A");
            pst1.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void createWk1Plan() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {

                String sqlwk1plan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] "
                        + "(SERIAL,PLAN_REF_NUM,ACT_DATE,ACT_SITE,SITE_CLASSIFICATION,CONSO_RANK,"
                        + "TRAV_DIST,ACT_DESC,ACT_JUSTIFCATION,PER_DIEM_REQ,BUD_COD,"
                        + "BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,MSC_AMT,ACC_UNPROV_AMT,"
                        + "EMP_NAM1,EMP_NAM2,EMP_NAM3,EMP_NAM4,ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlwk1plan);
                pst1.setString(1, "P");
                pst1.setString(2, jLabelRefNum.getText());
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
                pst1.setString(23, "1");
                pst1.setString(24, "1");
                pst1.setString(25, "A");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void createWk2Plan() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {

                String sqlWk2plan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] "
                        + "(SERIAL,PLAN_REF_NUM,ACT_DATE,ACT_SITE,SITE_CLASSIFICATION,CONSO_RANK,"
                        + "TRAV_DIST,ACT_DESC,ACT_JUSTIFCATION,PER_DIEM_REQ,BUD_COD,"
                        + "BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,MSC_AMT,ACC_UNPROV_AMT,"
                        + "EMP_NAM1,EMP_NAM2,EMP_NAM3,EMP_NAM4,ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWk2plan);
                pst1.setString(1, "P");
                pst1.setString(2, jLabelRefNum.getText());
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
                pst1.setString(23, "1");
                pst1.setString(24, "1");
                pst1.setString(25, "A");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void createWk3Plan() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {

                String sqlWk3plan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] "
                        + "(SERIAL,PLAN_REF_NUM,ACT_DATE,ACT_SITE,SITE_CLASSIFICATION,CONSO_RANK,"
                        + "TRAV_DIST,ACT_DESC,ACT_JUSTIFCATION,PER_DIEM_REQ,BUD_COD,"
                        + "BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,MSC_AMT,ACC_UNPROV_AMT,"
                        + "EMP_NAM1,EMP_NAM2,EMP_NAM3,EMP_NAM4,ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWk3plan);
                pst1.setString(1, "P");
                pst1.setString(2, jLabelRefNum.getText());
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
                pst1.setString(23, "1");
                pst1.setString(24, "1");
                pst1.setString(25, "A");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void createWkPlanCreator() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            String sqlWkplancreator = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanCreatorTab] "
                    + "(SERIAL,PLAN_REF_NUM,CREATED_ON,PROVINCE,DISTRICT,CREATOR) "
                    + " VALUES (?,?,?,?,?,?)";

            pst1 = conn.prepareStatement(sqlWkplancreator);
            pst1.setString(1, "P");
            pst1.setString(2, jLabelRefNum.getText());
            pst1.setString(3, jLabelLineDate.getText());
            pst1.setString(4, jLabelProvince.getText());
            pst1.setString(5, jLabelDistrict.getText());
            pst1.setString(6, jLabelLineLogNam.getText());

            pst1.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void createWkPlanPeriod() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            if (jTableWk1Activities.getRowCount() > 0 && jTableWk2Activities.getRowCount() == 0 && jTableWk3Activities.getRowCount() == 0) {

                String sqlWkplanperiod = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] "
                        + "(SERIAL,PLAN_REF_NUM,PROVINCE,DISTRICT,Wk1_PLAN_START_DATE,Wk1_PLAN_END_DATE,"
                        + "ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWkplanperiod);
                pst1.setString(1, "P");
                pst1.setString(2, jLabelRefNum.getText());
                pst1.setString(3, jLabelProvince.getText());
                pst1.setString(4, jLabelDistrict.getText());
                pst1.setString(5, formatter.format(jDateChooserWk1From.getDate()));
                pst1.setString(6, formatter.format(jDateChooserWk1To.getDate()));
                pst1.setString(7, "1");
                pst1.setString(8, "1");
                pst1.setString(9, "A");

                pst1.executeUpdate();

            } else if (jTableWk1Activities.getRowCount() > 0 && jTableWk2Activities.getRowCount() > 0 && jTableWk3Activities.getRowCount() == 0) {

                String sqlWkplanperiod = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] "
                        + "(SERIAL,PLAN_REF_NUM,PROVINCE,DISTRICT,Wk1_PLAN_START_DATE,Wk1_PLAN_END_DATE,"
                        + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,"
                        + "ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWkplanperiod);
                pst1.setString(1, "P");
                pst1.setString(2, jLabelRefNum.getText());
                pst1.setString(3, jLabelProvince.getText());
                pst1.setString(4, jLabelDistrict.getText());
                pst1.setString(5, formatter.format(jDateChooserWk1From.getDate()));
                pst1.setString(6, formatter.format(jDateChooserWk1To.getDate()));
                pst1.setString(7, formatter.format(jDateChooserWk2From.getDate()));
                pst1.setString(8, formatter.format(jDateChooserWk2To.getDate()));
                pst1.setString(9, "1");
                pst1.setString(10, "1");
                pst1.setString(11, "A");

                pst1.executeUpdate();
            } else {

                String sqlWkplanperiod = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] "
                        + "(SERIAL,PLAN_REF_NUM,PROVINCE,DISTRICT,Wk1_PLAN_START_DATE,Wk1_PLAN_END_DATE,"
                        + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,Wk3_PLAN_START_DATE,Wk3_PLAN_END_DATE,"
                        + "ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWkplanperiod);
                pst1.setString(1, "P");
                pst1.setString(2, jLabelRefNum.getText());
                pst1.setString(3, jLabelProvince.getText());
                pst1.setString(4, jLabelDistrict.getText());
                pst1.setString(5, formatter.format(jDateChooserWk1From.getDate()));
                pst1.setString(6, formatter.format(jDateChooserWk1To.getDate()));
                pst1.setString(7, formatter.format(jDateChooserWk2From.getDate()));
                pst1.setString(8, formatter.format(jDateChooserWk2To.getDate()));
                pst1.setString(9, formatter.format(jDateChooserWk3From.getDate()));
                pst1.setString(10, formatter.format(jDateChooserWk3To.getDate()));
                pst1.setString(11, "1");
                pst1.setString(12, "1");
                pst1.setString(13, "A");

                pst1.executeUpdate();

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void createWkPlanAction() {
        try {
            findProvMan();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            String sqlWkplanperiod = "insert into [ClaimsAppSysZvandiri].[dbo].[PlanActTab] (SERIAL,PLAN_REF_NUM,USR_ACTION,DOC_STATUS,"
                    + "ACTION_BY,SEND_TO,ACTION_DATE,ACTION_TIME,"
                    + "ACTIONED_ON_COMPUTER,ACT_VER,DOC_VER,ACT_REC_STA)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

            pst1 = conn.prepareStatement(sqlWkplanperiod);
            pst1.setString(1, "P");
            pst1.setString(2, jLabelRefNum.getText());
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
            pst1.setString(10, "1");
            pst1.setString(11, "1");
            pst1.setString(12, "A");

            pst1.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void resetField() {
        if ("Y".equals(wk1Site)) {
            jDateChooserDialogWk1ActivityDate.setDate(null);
            jTextFieldDialogWk1Site.setText("");
            jLabelDialogWk1ActSiteClass.setText("");
            jLabelWk1DialogActRank.setText("");
            jLabelWk1DialogActDis.setText("");
            jTextFieldWk1DialogBudget.setText("");
            jTextFieldWk1DialogActivityDesc.setText("");
            jTextAreaWk1DialogJustification.setText("");
            jTextFieldWk1DialogStaffName1.setText("");
            jTextFieldWk1DialogStaffName2.setText("");
            jTextFieldWk1DialogStaffName3.setText("");
            jTextFieldWk1DialogStaffName4.setText("");
            jCheckBoxDialogWk1BrkFast.setEnabled(true);
            jCheckBoxDialogWk1Dinner.setEnabled(true);
            jCheckBoxDialogWk1Acc.setEnabled(true);
            jCheckBoxDialogWk1Inc.setEnabled(true);
            jCheckBoxDialogWk1Misc.setEnabled(true);
            jCheckBoxDialogWk1BrkFast.setSelected(false);
            jCheckBoxDialogWk1Lunch.setSelected(false);
            jCheckBoxDialogWk1Dinner.setSelected(false);
            jCheckBoxDialogWk1Acc.setSelected(false);
            jCheckBoxDialogWk1Inc.setSelected(false);
            jCheckBoxDialogWk1Misc.setSelected(false);
            jTextFieldWk1Misc.setVisible(false);
            jLabelWk1Misc.setVisible(false);
            jTextFieldWk1Misc.setText("");
            jLabelWk1MiscAmt.setVisible(false);
            jTextFieldWk1MiscAmt.setVisible(false);
            jTextFieldWk1MiscAmt.setText("");

        } else if ("Y".equals(wk2Site)) {
            jDateChooserDialogWk2ActivityDate.setDate(null);
            jTextFieldDialogWk2Site.setText("");
            jLabelDialogWk2ActSiteClass.setText("");
            jLabelWk2DialogActRank.setText("");
            jLabelWk2DialogActDis.setText("");
            jTextFieldWk2DialogBudget.setText("");
            jTextFieldWk2DialogActivityDesc.setText("");
            jTextAreaWk2DialogJustification.setText("");
            jTextFieldWk2DialogStaffName1.setText("");
            jTextFieldWk2DialogStaffName2.setText("");
            jTextFieldWk2DialogStaffName3.setText("");
            jTextFieldWk2DialogStaffName4.setText("");
            jCheckBoxDialogWk2BrkFast.setEnabled(true);
            jCheckBoxDialogWk2Dinner.setEnabled(true);
            jCheckBoxDialogWk2Acc.setEnabled(true);
            jCheckBoxDialogWk2Inc.setEnabled(true);
            jCheckBoxDialogWk2Misc.setEnabled(true);
            jCheckBoxDialogWk2BrkFast.setSelected(false);
            jCheckBoxDialogWk2Lunch.setSelected(false);
            jCheckBoxDialogWk2Dinner.setSelected(false);
            jCheckBoxDialogWk2Acc.setSelected(false);
            jCheckBoxDialogWk2Inc.setSelected(false);
            jTextFieldWk2Misc.setVisible(false);
            jLabelWk2Misc.setVisible(false);
            jTextFieldWk2Misc.setText("");
            jLabelWk2MiscAmt.setVisible(false);
            jTextFieldWk2MiscAmt.setVisible(false);
            jTextFieldWk2MiscAmt.setText("");
        } else if ("Y".equals(wk3Site)) {
            jDateChooserDialogWk3ActivityDate.setDate(null);
            jTextFieldDialogWk3Site.setText("");
            jLabelDialogWk3ActSiteClass.setText("");
            jLabelWk3DialogActRank.setText("");
            jLabelWk3DialogActDis.setText("");
            jTextFieldWk3DialogBudget.setText("");
            jTextFieldWk3DialogActivityDesc.setText("");
            jTextAreaWk3DialogJustification.setText("");
            jTextFieldWk3DialogStaffName1.setText("");
            jTextFieldWk3DialogStaffName2.setText("");
            jTextFieldWk3DialogStaffName3.setText("");
            jTextFieldWk3DialogStaffName4.setText("");
            jCheckBoxDialogWk3BrkFast.setEnabled(true);
            jCheckBoxDialogWk3Dinner.setEnabled(true);
            jCheckBoxDialogWk3Acc.setEnabled(true);
            jCheckBoxDialogWk3Inc.setEnabled(true);
            jCheckBoxDialogWk3Misc.setEnabled(true);
            jCheckBoxDialogWk3BrkFast.setSelected(false);
            jCheckBoxDialogWk3Lunch.setSelected(false);
            jCheckBoxDialogWk3Dinner.setSelected(false);
            jCheckBoxDialogWk3Acc.setSelected(false);
            jCheckBoxDialogWk3Inc.setSelected(false);
            jTextFieldWk3Misc.setVisible(false);
            jLabelWk3Misc.setVisible(false);
            jTextFieldWk3Misc.setText("");
            jLabelWk3MiscAmt.setVisible(false);
            jTextFieldWk3MiscAmt.setVisible(false);
            jTextFieldWk3MiscAmt.setText("");
        }
    }

    void Wk1DistLess100() {
        if (((Double.parseDouble(jLabelWk1DialogActDis.getText())) > 39)
                && ((Double.parseDouble(jLabelWk1DialogActDis.getText())) < 100)) {
            jCheckBoxDialogWk1BrkFast.setSelected(false);
            jCheckBoxDialogWk1Lunch.setSelected(false);
            jCheckBoxDialogWk1Dinner.setSelected(false);
            jCheckBoxDialogWk1Acc.setSelected(false);
            jCheckBoxDialogWk1Inc.setSelected(false);
            jCheckBoxDialogWk1Misc.setSelected(false);
            jTextFieldWk1Misc.setVisible(false);
            jLabelWk1Misc.setVisible(false);
            jTextFieldWk1Misc.setText("");
            jLabelWk1MiscAmt.setVisible(false);
            jTextFieldWk1MiscAmt.setVisible(false);
            jTextFieldWk1MiscAmt.setText("");
            jTextFieldWk1DialogStaffName1.setText("");
            jTextFieldWk1DialogStaffName2.setText("");
            jTextFieldWk1DialogStaffName3.setText("");
            jTextFieldWk1DialogStaffName4.setText("");
            jCheckBoxDialogWk1BrkFast.setSelected(false);
            jCheckBoxDialogWk1Lunch.setSelected(false);
            jCheckBoxDialogWk1Dinner.setSelected(false);
            jCheckBoxDialogWk1Acc.setSelected(false);
            jCheckBoxDialogWk1Inc.setSelected(false);
            jCheckBoxDialogWk1BrkFast.setEnabled(false);
            jCheckBoxDialogWk1Lunch.setEnabled(true);
            jCheckBoxDialogWk1Dinner.setEnabled(false);
            jCheckBoxDialogWk1Acc.setEnabled(false);
            jCheckBoxDialogWk1Inc.setEnabled(false);

        } else if ((Double.parseDouble(jLabelWk1DialogActDis.getText())) < 40) {
            jCheckBoxDialogWk1BrkFast.setSelected(false);
            jCheckBoxDialogWk1Lunch.setSelected(false);
            jCheckBoxDialogWk1Dinner.setSelected(false);
            jCheckBoxDialogWk1Acc.setSelected(false);
            jCheckBoxDialogWk1Inc.setSelected(false);
            jCheckBoxDialogWk1Misc.setSelected(false);
            jTextFieldWk1Misc.setVisible(false);
            jLabelWk1Misc.setVisible(false);
            jTextFieldWk1Misc.setText("");
            jLabelWk1MiscAmt.setVisible(false);
            jTextFieldWk1MiscAmt.setVisible(false);
            jTextFieldWk1MiscAmt.setText("");
            jTextFieldWk1DialogStaffName1.setText("");
            jTextFieldWk1DialogStaffName2.setText("");
            jTextFieldWk1DialogStaffName3.setText("");
            jTextFieldWk1DialogStaffName4.setText("");
            jCheckBoxDialogWk1BrkFast.setSelected(false);
            jCheckBoxDialogWk1Lunch.setSelected(false);
            jCheckBoxDialogWk1Dinner.setSelected(false);
            jCheckBoxDialogWk1Acc.setSelected(false);
            jCheckBoxDialogWk1Inc.setSelected(false);
            jCheckBoxDialogWk1BrkFast.setEnabled(false);
            jCheckBoxDialogWk1Dinner.setEnabled(false);
            jCheckBoxDialogWk1Lunch.setEnabled(false);
            jCheckBoxDialogWk1Acc.setEnabled(false);
            jCheckBoxDialogWk1Inc.setEnabled(false);

        } else {
            jCheckBoxDialogWk1BrkFast.setSelected(false);
            jCheckBoxDialogWk1Lunch.setSelected(false);
            jCheckBoxDialogWk1Dinner.setSelected(false);
            jCheckBoxDialogWk1Acc.setSelected(false);
            jCheckBoxDialogWk1Inc.setSelected(false);
            jCheckBoxDialogWk1Misc.setSelected(false);
            jTextFieldWk1Misc.setVisible(false);
            jLabelWk1Misc.setVisible(false);
            jTextFieldWk1Misc.setText("");
            jLabelWk1MiscAmt.setVisible(false);
            jTextFieldWk1MiscAmt.setVisible(false);
            jTextFieldWk1MiscAmt.setText("");
            jTextFieldWk1DialogStaffName1.setText("");
            jTextFieldWk1DialogStaffName2.setText("");
            jTextFieldWk1DialogStaffName3.setText("");
            jTextFieldWk1DialogStaffName4.setText("");
            jCheckBoxDialogWk1BrkFast.setSelected(false);
            jCheckBoxDialogWk1Lunch.setSelected(false);
            jCheckBoxDialogWk1Dinner.setSelected(false);
            jCheckBoxDialogWk1Acc.setSelected(false);
            jCheckBoxDialogWk1Inc.setSelected(false);
            jCheckBoxDialogWk1BrkFast.setEnabled(true);
            jCheckBoxDialogWk1Dinner.setEnabled(true);
            jCheckBoxDialogWk1Lunch.setEnabled(true);
            jCheckBoxDialogWk1Acc.setEnabled(true);
            jCheckBoxDialogWk1Inc.setEnabled(true);
        }
    }

    void Wk2DistLess100() {
        if (((Double.parseDouble(jLabelWk2DialogActDis.getText())) > 39)
                && ((Double.parseDouble(jLabelWk2DialogActDis.getText())) < 100)) {
            jCheckBoxDialogWk2BrkFast.setSelected(false);
            jCheckBoxDialogWk2Lunch.setSelected(false);
            jCheckBoxDialogWk2Dinner.setSelected(false);
            jCheckBoxDialogWk2Acc.setSelected(false);
            jCheckBoxDialogWk2Inc.setSelected(false);
            jCheckBoxDialogWk2Misc.setSelected(false);
            jTextFieldWk2Misc.setVisible(false);
            jLabelWk2Misc.setVisible(false);
            jTextFieldWk2Misc.setText("");
            jLabelWk2MiscAmt.setVisible(false);
            jTextFieldWk2MiscAmt.setVisible(false);
            jTextFieldWk2MiscAmt.setText("");
            jTextFieldWk2DialogStaffName1.setText("");
            jTextFieldWk2DialogStaffName2.setText("");
            jTextFieldWk2DialogStaffName3.setText("");
            jTextFieldWk2DialogStaffName4.setText("");
            jCheckBoxDialogWk2BrkFast.setSelected(false);
            jCheckBoxDialogWk2Lunch.setSelected(false);
            jCheckBoxDialogWk2Dinner.setSelected(false);
            jCheckBoxDialogWk2Acc.setSelected(false);
            jCheckBoxDialogWk2Inc.setSelected(false);
            jCheckBoxDialogWk2BrkFast.setEnabled(false);
            jCheckBoxDialogWk2Dinner.setEnabled(false);
            jCheckBoxDialogWk2Acc.setEnabled(false);
            jCheckBoxDialogWk2Inc.setEnabled(false);
            jCheckBoxDialogWk2Lunch.setEnabled(true);

        } else if ((Double.parseDouble(jLabelWk2DialogActDis.getText())) < 40) {
            jCheckBoxDialogWk2BrkFast.setSelected(false);
            jCheckBoxDialogWk2Lunch.setSelected(false);
            jCheckBoxDialogWk2Dinner.setSelected(false);
            jCheckBoxDialogWk2Acc.setSelected(false);
            jCheckBoxDialogWk2Inc.setSelected(false);
            jCheckBoxDialogWk2Misc.setSelected(false);
            jTextFieldWk2Misc.setVisible(false);
            jLabelWk2Misc.setVisible(false);
            jTextFieldWk2Misc.setText("");
            jLabelWk2MiscAmt.setVisible(false);
            jTextFieldWk2MiscAmt.setVisible(false);
            jTextFieldWk2MiscAmt.setText("");
            jTextFieldWk2DialogStaffName1.setText("");
            jTextFieldWk2DialogStaffName2.setText("");
            jTextFieldWk2DialogStaffName3.setText("");
            jTextFieldWk2DialogStaffName4.setText("");
            jCheckBoxDialogWk2BrkFast.setSelected(false);
            jCheckBoxDialogWk2Lunch.setSelected(false);
            jCheckBoxDialogWk2Dinner.setSelected(false);
            jCheckBoxDialogWk2Acc.setSelected(false);
            jCheckBoxDialogWk2Inc.setSelected(false);
            jCheckBoxDialogWk2BrkFast.setEnabled(false);
            jCheckBoxDialogWk2Lunch.setEnabled(false);
            jCheckBoxDialogWk2Dinner.setEnabled(false);
            jCheckBoxDialogWk2Acc.setEnabled(false);
            jCheckBoxDialogWk2Inc.setEnabled(false);

        } else {
            jCheckBoxDialogWk2BrkFast.setSelected(false);
            jCheckBoxDialogWk2Lunch.setSelected(false);
            jCheckBoxDialogWk2Dinner.setSelected(false);
            jCheckBoxDialogWk2Acc.setSelected(false);
            jCheckBoxDialogWk2Inc.setSelected(false);
            jCheckBoxDialogWk2Misc.setSelected(false);
            jTextFieldWk2Misc.setVisible(false);
            jLabelWk2Misc.setVisible(false);
            jTextFieldWk2Misc.setText("");
            jLabelWk2MiscAmt.setVisible(false);
            jTextFieldWk2MiscAmt.setVisible(false);
            jTextFieldWk2MiscAmt.setText("");
            jTextFieldWk2DialogStaffName1.setText("");
            jTextFieldWk2DialogStaffName2.setText("");
            jTextFieldWk2DialogStaffName3.setText("");
            jTextFieldWk2DialogStaffName4.setText("");
            jCheckBoxDialogWk2BrkFast.setSelected(false);
            jCheckBoxDialogWk2Lunch.setSelected(false);
            jCheckBoxDialogWk2Dinner.setSelected(false);
            jCheckBoxDialogWk2Acc.setSelected(false);
            jCheckBoxDialogWk2Inc.setSelected(false);
            jCheckBoxDialogWk2BrkFast.setEnabled(true);
            jCheckBoxDialogWk2Dinner.setEnabled(true);
            jCheckBoxDialogWk2Acc.setEnabled(true);
            jCheckBoxDialogWk2Inc.setEnabled(true);
            jCheckBoxDialogWk2Lunch.setEnabled(true);
        }
    }

    void Wk3DistLess100() {
        if (((Double.parseDouble(jLabelWk3DialogActDis.getText())) > 39)
                && ((Double.parseDouble(jLabelWk3DialogActDis.getText())) < 100)) {
            jCheckBoxDialogWk3BrkFast.setSelected(false);
            jCheckBoxDialogWk3Lunch.setSelected(false);
            jCheckBoxDialogWk3Dinner.setSelected(false);
            jCheckBoxDialogWk3Acc.setSelected(false);
            jCheckBoxDialogWk3Inc.setSelected(false);
            jCheckBoxDialogWk3Misc.setSelected(false);
            jTextFieldWk3Misc.setVisible(false);
            jLabelWk3Misc.setVisible(false);
            jTextFieldWk3Misc.setText("");
            jLabelWk3MiscAmt.setVisible(false);
            jTextFieldWk3MiscAmt.setVisible(false);
            jTextFieldWk3MiscAmt.setText("");
            jTextFieldWk3DialogStaffName1.setText("");
            jTextFieldWk3DialogStaffName2.setText("");
            jTextFieldWk3DialogStaffName3.setText("");
            jTextFieldWk3DialogStaffName4.setText("");
            jCheckBoxDialogWk3BrkFast.setSelected(false);
            jCheckBoxDialogWk3Lunch.setSelected(false);
            jCheckBoxDialogWk3Dinner.setSelected(false);
            jCheckBoxDialogWk3Acc.setSelected(false);
            jCheckBoxDialogWk3Inc.setSelected(false);
            jCheckBoxDialogWk3BrkFast.setEnabled(false);
            jCheckBoxDialogWk3Dinner.setEnabled(false);
            jCheckBoxDialogWk3Acc.setEnabled(false);
            jCheckBoxDialogWk3Inc.setEnabled(false);
            jCheckBoxDialogWk3Lunch.setEnabled(true);

        } else if ((Double.parseDouble(jLabelWk3DialogActDis.getText())) < 40) {
            jCheckBoxDialogWk3BrkFast.setSelected(false);
            jCheckBoxDialogWk3Lunch.setSelected(false);
            jCheckBoxDialogWk3Dinner.setSelected(false);
            jCheckBoxDialogWk3Acc.setSelected(false);
            jCheckBoxDialogWk3Inc.setSelected(false);
            jCheckBoxDialogWk3Misc.setSelected(false);
            jTextFieldWk3Misc.setVisible(false);
            jLabelWk3Misc.setVisible(false);
            jTextFieldWk3Misc.setText("");
            jLabelWk3MiscAmt.setVisible(false);
            jTextFieldWk3MiscAmt.setVisible(false);
            jTextFieldWk3MiscAmt.setText("");
            jTextFieldWk3DialogStaffName1.setText("");
            jTextFieldWk3DialogStaffName2.setText("");
            jTextFieldWk3DialogStaffName3.setText("");
            jTextFieldWk3DialogStaffName4.setText("");
            jCheckBoxDialogWk3BrkFast.setSelected(false);
            jCheckBoxDialogWk3Lunch.setSelected(false);
            jCheckBoxDialogWk3Dinner.setSelected(false);
            jCheckBoxDialogWk3Acc.setSelected(false);
            jCheckBoxDialogWk3Inc.setSelected(false);
            jCheckBoxDialogWk3BrkFast.setEnabled(false);
            jCheckBoxDialogWk3Lunch.setEnabled(false);
            jCheckBoxDialogWk3Dinner.setEnabled(false);
            jCheckBoxDialogWk3Acc.setEnabled(false);
            jCheckBoxDialogWk3Inc.setEnabled(false);

        } else {
            jCheckBoxDialogWk3BrkFast.setSelected(false);
            jCheckBoxDialogWk3Lunch.setSelected(false);
            jCheckBoxDialogWk3Dinner.setSelected(false);
            jCheckBoxDialogWk3Acc.setSelected(false);
            jCheckBoxDialogWk3Inc.setSelected(false);
            jCheckBoxDialogWk3Misc.setSelected(false);
            jTextFieldWk3Misc.setVisible(false);
            jLabelWk3Misc.setVisible(false);
            jTextFieldWk3Misc.setText("");
            jLabelWk3MiscAmt.setVisible(false);
            jTextFieldWk3MiscAmt.setVisible(false);
            jTextFieldWk3MiscAmt.setText("");
            jTextFieldWk3DialogStaffName1.setText("");
            jTextFieldWk3DialogStaffName2.setText("");
            jTextFieldWk3DialogStaffName3.setText("");
            jTextFieldWk3DialogStaffName4.setText("");
            jCheckBoxDialogWk3BrkFast.setSelected(false);
            jCheckBoxDialogWk3Lunch.setSelected(false);
            jCheckBoxDialogWk3Dinner.setSelected(false);
            jCheckBoxDialogWk3Acc.setSelected(false);
            jCheckBoxDialogWk3Inc.setSelected(false);
            jCheckBoxDialogWk3BrkFast.setEnabled(true);
            jCheckBoxDialogWk3Dinner.setEnabled(true);
            jCheckBoxDialogWk3Acc.setEnabled(true);
            jCheckBoxDialogWk3Inc.setEnabled(true);
            jCheckBoxDialogWk3Lunch.setEnabled(true);
        }
    }

    void addItem() {
        int selectedOption = JOptionPane.showConfirmDialog(jDialogWk1,
                "Do want to add another activity line?",
                "Choose",
                JOptionPane.YES_NO_OPTION);
        if ((selectedOption == JOptionPane.YES_OPTION) && ("Y".equals(wk1Site))) {
            jDialogWk1.setVisible(true);

        } else {
            wk1Site = "N";
            jDialogWk1.setVisible(false);

        }
        if ((selectedOption == JOptionPane.YES_OPTION) && ("Y".equals(wk2Site))) {
            jDialogWk2.setVisible(true);
        } else {
            wk2Site = "N";
            jDialogWk2.setVisible(false);
        }
        if ((selectedOption == JOptionPane.YES_OPTION) && ("Y".equals(wk3Site))) {
            jDialogWk3.setVisible(true);
        } else {
            wk3Site = "N";
            jDialogWk3.setVisible(false);
        }
    }

    void searchName1() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
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

    void searchName2() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            Statement st = conn.createStatement();

            int itemCount = jComboBoxSearchResult2.getItemCount();

            for (int i = 0; i < itemCount; i++) {
                jComboBoxSearchResult2.removeItemAt(0);
            }
            jComboBoxSearchResult2.setSelectedIndex(-1);
            String searchNam = jTextFieldSearchNam2.getText();
            st.executeQuery("SELECT distinct [EMP_NAM]\n"
                    + "  FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] WHERE [EMP_NAM] like upper(\n" + "'%" + searchNam + "%')");
            ResultSet r = st.getResultSet();

            while (r.next()) {
                jComboBoxSearchResult2.addItem(r.getString(1));
                jButtonOk2.setVisible(true);
                //                jButtonSearch.setVisible(false);

            }

            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Connect to Database.Try again.", "Error Connection", JOptionPane.WARNING_MESSAGE);
            // System.exit(1);
        }
    }

    void searchName3() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            Statement st = conn.createStatement();

            int itemCount = jComboBoxSearchResult3.getItemCount();

            for (int i = 0; i < itemCount; i++) {
                jComboBoxSearchResult3.removeItemAt(0);
            }
            jComboBoxSearchResult3.setSelectedIndex(-1);
            String searchNam = jTextFieldSearchNam3.getText();
            st.executeQuery("SELECT distinct [EMP_NAM]\n"
                    + "  FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] WHERE [EMP_NAM] like upper(\n" + "'%" + searchNam + "%')");
            ResultSet r = st.getResultSet();

            while (r.next()) {
                jComboBoxSearchResult3.addItem(r.getString(1));
                jButtonOk3.setVisible(true);
                //                jButtonSearch.setVisible(false);

            }

            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Connect to Database.Try again.", "Error Connection", JOptionPane.WARNING_MESSAGE);
            // System.exit(1);
        }
    }

    void searchName4() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            Statement st = conn.createStatement();

            int itemCount = jComboBoxSearchResult4.getItemCount();

            for (int i = 0; i < itemCount; i++) {
                jComboBoxSearchResult4.removeItemAt(0);
            }
            jComboBoxSearchResult4.setSelectedIndex(-1);
            String searchNam = jTextFieldSearchNam4.getText();
            st.executeQuery("SELECT distinct [EMP_NAM]\n"
                    + "  FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] WHERE [EMP_NAM] like upper(\n" + "'%" + searchNam + "%')");
            ResultSet r = st.getResultSet();

            while (r.next()) {
                jComboBoxSearchResult4.addItem(r.getString(1));
                jButtonOk4.setVisible(true);
                //                jButtonSearch.setVisible(false);

            }

            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Connect to Database.Try again.", "Error Connection", JOptionPane.WARNING_MESSAGE);
            // System.exit(1);
        }
    }

    void sendMail() {

        final String username = mailUsrNam;
        final String password = mailUsrPass;
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
            String planSupNam, planSupEmail;

            if ((sendToProvMgr.equals(jLabelLineLogNam.getText())) || ("Central Office".equals(jLabelProvince.getText())) || ("Harare Province".equals(jLabelProvince.getText()))) {
                planSupNam = supNam;
                planSupEmail = supUsrMail;
            } else {
                planSupNam = sendToProvMgr;
                planSupEmail = provMgrMail;
            }
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailUsrNam));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(planSupEmail));
            message.setSubject("Plan Approval Request.Plan Reference No. " + jLabelSerial.getText() + " " + jLabelRefNum.getText());

            message.setContent("<html><body> Dear " + planSupNam + "<br><br>" + createUsrNam
                    + " has submitted a monthly plan for your approval. <br><br>Please check "
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
        jDialogBudget = new javax.swing.JDialog();
        jPanel11 = new javax.swing.JPanel();
        jLabelHeader2 = new javax.swing.JLabel();
        jButtonOkFacility1 = new javax.swing.JButton();
        jButtonCancelBudget = new javax.swing.JButton();
        jLabelBudgetCode = new javax.swing.JLabel();
        jComboBudgetCode = new javax.swing.JComboBox<>();
        jDialogSearchName1 = new javax.swing.JDialog();
        jPanelSearchID = new javax.swing.JPanel();
        jTextFieldSearchNam1 = new javax.swing.JTextField();
        jButtonSearch1 = new javax.swing.JButton();
        jComboBoxSearchResult1 = new javax.swing.JComboBox<>();
        jButtonOk1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jDialogSearchName2 = new javax.swing.JDialog();
        jPanelSearchID1 = new javax.swing.JPanel();
        jTextFieldSearchNam2 = new javax.swing.JTextField();
        jButtonSearch2 = new javax.swing.JButton();
        jComboBoxSearchResult2 = new javax.swing.JComboBox<>();
        jButtonOk2 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jDialogSearchName3 = new javax.swing.JDialog();
        jPanelSearchID2 = new javax.swing.JPanel();
        jTextFieldSearchNam3 = new javax.swing.JTextField();
        jButtonSearch3 = new javax.swing.JButton();
        jComboBoxSearchResult3 = new javax.swing.JComboBox<>();
        jButtonOk3 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jDialogSearchName4 = new javax.swing.JDialog();
        jPanelSearchID4 = new javax.swing.JPanel();
        jTextFieldSearchNam4 = new javax.swing.JTextField();
        jButtonSearch4 = new javax.swing.JButton();
        jComboBoxSearchResult4 = new javax.swing.JComboBox<>();
        jButtonOk4 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jDialogWaiting = new javax.swing.JDialog();
        jDialogWk1 = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabelDialogWk1ActivityDate = new javax.swing.JLabel();
        jDateChooserDialogWk1ActivityDate = new com.toedter.calendar.JDateChooser();
        jLabelDialogWk1Site = new javax.swing.JLabel();
        jTextFieldDialogWk1Site = new javax.swing.JTextField();
        jLabelWk1DialogJustification = new javax.swing.JLabel();
        jLabelDialogWk1SiteClass = new javax.swing.JLabel();
        jLabelDialogWk1ActSiteClass = new javax.swing.JLabel();
        jLabelDialogWk1Rank = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldWk1DialogActivityDesc = new javax.swing.JTextField();
        jLabelWk1DialogActivityDesc = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaWk1DialogJustification = new javax.swing.JTextArea();
        jSeparator3 = new javax.swing.JSeparator();
        jLabelWk1DialogActRank = new javax.swing.JLabel();
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
        jLabelWk1DialogBudget = new javax.swing.JLabel();
        jTextFieldWk1DialogBudget = new javax.swing.JTextField();
        jCheckBoxDialogWk1BrkFast = new javax.swing.JCheckBox();
        jCheckBoxDialogWk1Lunch = new javax.swing.JCheckBox();
        jCheckBoxDialogWk1Dinner = new javax.swing.JCheckBox();
        jCheckBoxDialogWk1Acc = new javax.swing.JCheckBox();
        jLabelDialogWk1Dis = new javax.swing.JLabel();
        jLabelWk1DialogActDis = new javax.swing.JLabel();
        jLabelDialogWk1Dis1 = new javax.swing.JLabel();
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
        jDialogWk2 = new javax.swing.JDialog();
        jPanel2 = new javax.swing.JPanel();
        jLabelDialogWk2ActivityDate = new javax.swing.JLabel();
        jDateChooserDialogWk2ActivityDate = new com.toedter.calendar.JDateChooser();
        jLabelDialogWk2Site = new javax.swing.JLabel();
        jTextFieldDialogWk2Site = new javax.swing.JTextField();
        jLabelWk2DialogJustification = new javax.swing.JLabel();
        jLabelDialogWk2SiteClass = new javax.swing.JLabel();
        jLabelDialogWk2ActSiteClass = new javax.swing.JLabel();
        jLabelDialogWk2Rank = new javax.swing.JLabel();
        jLabelWk2DialogStaffName4 = new javax.swing.JLabel();
        jTextFieldWk2DialogActivityDesc = new javax.swing.JTextField();
        jLabelWk2DialogActivityDesc = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextAreaWk2DialogJustification = new javax.swing.JTextArea();
        jSeparator6 = new javax.swing.JSeparator();
        jLabelWk2DialogActRank = new javax.swing.JLabel();
        jComboBoxDialogWk2PerDiem = new javax.swing.JComboBox<>();
        jLabelDialogWk2PerDiem = new javax.swing.JLabel();
        jLabelWk2DialogStaff = new javax.swing.JLabel();
        jLabelWk2DialogStaffName1 = new javax.swing.JLabel();
        jLabelWk2DialogStaffName2 = new javax.swing.JLabel();
        jLabelWk2DialogStaffName3 = new javax.swing.JLabel();
        jTextFieldWk2DialogStaffName4 = new javax.swing.JTextField();
        jTextFieldWk2DialogStaffName1 = new javax.swing.JTextField();
        jTextFieldWk2DialogStaffName2 = new javax.swing.JTextField();
        jTextFieldWk2DialogStaffName3 = new javax.swing.JTextField();
        jButtonDialogWk2Reset = new javax.swing.JButton();
        jButtonDialogWk2Add = new javax.swing.JButton();
        jLabelWk2DialogBudget = new javax.swing.JLabel();
        jTextFieldWk2DialogBudget = new javax.swing.JTextField();
        jCheckBoxDialogWk2BrkFast = new javax.swing.JCheckBox();
        jCheckBoxDialogWk2Lunch = new javax.swing.JCheckBox();
        jCheckBoxDialogWk2Dinner = new javax.swing.JCheckBox();
        jCheckBoxDialogWk2Acc = new javax.swing.JCheckBox();
        jLabelDialogWk1Dis2 = new javax.swing.JLabel();
        jLabelWk2DialogActDis = new javax.swing.JLabel();
        jLabelDialogWk2Dis = new javax.swing.JLabel();
        jLabelRemain1 = new javax.swing.JLabel();
        jCheckBoxDialogWk2Inc = new javax.swing.JCheckBox();
        jTextFieldWk2Misc = new javax.swing.JTextField();
        jLabelWk2Misc = new javax.swing.JLabel();
        jCheckBoxDialogWk2Misc = new javax.swing.JCheckBox();
        jButtonDialogWk2Close = new javax.swing.JButton();
        jLabelWk2MiscAmt = new javax.swing.JLabel();
        jTextFieldWk2MiscAmt = new javax.swing.JTextField();
        jLabelWk2Name1Del = new javax.swing.JLabel();
        jLabelWk2Name2Del = new javax.swing.JLabel();
        jLabelWk2Name3Del = new javax.swing.JLabel();
        jLabelWk2Name4Del = new javax.swing.JLabel();
        jDialogWk3 = new javax.swing.JDialog();
        jPanel3 = new javax.swing.JPanel();
        jLabelDialogWk3ActivityDate = new javax.swing.JLabel();
        jDateChooserDialogWk3ActivityDate = new com.toedter.calendar.JDateChooser();
        jLabelDialogWk3Site = new javax.swing.JLabel();
        jTextFieldDialogWk3Site = new javax.swing.JTextField();
        jLabelWk3DialogJustification = new javax.swing.JLabel();
        jLabelDialogWk3SiteClass = new javax.swing.JLabel();
        jLabelDialogWk3ActSiteClass = new javax.swing.JLabel();
        jLabelDialogWk3Rank = new javax.swing.JLabel();
        jLabelWk3DialogStaffName4 = new javax.swing.JLabel();
        jTextFieldWk3DialogActivityDesc = new javax.swing.JTextField();
        jLabelWk3DialogActivityDesc = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextAreaWk3DialogJustification = new javax.swing.JTextArea();
        jSeparator9 = new javax.swing.JSeparator();
        jLabelWk3DialogActRank = new javax.swing.JLabel();
        jComboBoxDialogWk3PerDiem = new javax.swing.JComboBox<>();
        jLabelDialogWk2PerDiem1 = new javax.swing.JLabel();
        jLabelWk3DialogStaff = new javax.swing.JLabel();
        jLabelWk3DialogStaffName1 = new javax.swing.JLabel();
        jLabelWk3DialogStaffName2 = new javax.swing.JLabel();
        jLabelWk3DialogStaffName3 = new javax.swing.JLabel();
        jTextFieldWk3DialogStaffName4 = new javax.swing.JTextField();
        jTextFieldWk3DialogStaffName1 = new javax.swing.JTextField();
        jTextFieldWk3DialogStaffName2 = new javax.swing.JTextField();
        jTextFieldWk3DialogStaffName3 = new javax.swing.JTextField();
        jButtonDialogWk3Reset = new javax.swing.JButton();
        jButtonDialogWk3Add = new javax.swing.JButton();
        jLabelWk3DialogBudget = new javax.swing.JLabel();
        jTextFieldWk3DialogBudget = new javax.swing.JTextField();
        jCheckBoxDialogWk3BrkFast = new javax.swing.JCheckBox();
        jCheckBoxDialogWk3Lunch = new javax.swing.JCheckBox();
        jCheckBoxDialogWk3Dinner = new javax.swing.JCheckBox();
        jCheckBoxDialogWk3Acc = new javax.swing.JCheckBox();
        jLabelDialogWk3Dis3 = new javax.swing.JLabel();
        jLabelWk3DialogActDis = new javax.swing.JLabel();
        jLabelDialogWk3Dis = new javax.swing.JLabel();
        jLabelRemain2 = new javax.swing.JLabel();
        jCheckBoxDialogWk3Inc = new javax.swing.JCheckBox();
        jCheckBoxDialogWk3Misc = new javax.swing.JCheckBox();
        jLabelWk3Misc = new javax.swing.JLabel();
        jTextFieldWk3Misc = new javax.swing.JTextField();
        jButtonDialogWk3Close = new javax.swing.JButton();
        jLabelWk3MiscAmt = new javax.swing.JLabel();
        jTextFieldWk3MiscAmt = new javax.swing.JTextField();
        jLabelWk3Name1Del = new javax.swing.JLabel();
        jLabelWk3Name2Del = new javax.swing.JLabel();
        jLabelWk3Name3Del = new javax.swing.JLabel();
        jLabelWk3Name4Del = new javax.swing.JLabel();
        jDialogWk4 = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        jLabelDialogWk4ActivityDate = new javax.swing.JLabel();
        jDateChooserDialogWk4ActivityDate = new com.toedter.calendar.JDateChooser();
        jLabelDialogWk4Site = new javax.swing.JLabel();
        jTextFieldDialogWk4Site = new javax.swing.JTextField();
        jLabelWk4DialogJustification = new javax.swing.JLabel();
        jLabelDialogWk4SiteClass = new javax.swing.JLabel();
        jLabelDialogWk4ActSiteClass = new javax.swing.JLabel();
        jLabelDialogWk4Rank = new javax.swing.JLabel();
        jLabelWk4DialogStaffName4 = new javax.swing.JLabel();
        jTextFieldWk4DialogActivityDesc = new javax.swing.JTextField();
        jLabelWk4DialogActivityDesc = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextAreaWk4DialogJustification = new javax.swing.JTextArea();
        jSeparator35 = new javax.swing.JSeparator();
        jLabelWk4DialogActRank = new javax.swing.JLabel();
        jComboBoxDialogWk4PerDiem = new javax.swing.JComboBox<>();
        jLabelDialogWk4PerDiem = new javax.swing.JLabel();
        jLabelWk4DialogStaff = new javax.swing.JLabel();
        jLabelWk4DialogStaffName1 = new javax.swing.JLabel();
        jLabelWk4DialogStaffName2 = new javax.swing.JLabel();
        jLabelWk4DialogStaffName3 = new javax.swing.JLabel();
        jTextFieldWk4DialogStaffName4 = new javax.swing.JTextField();
        jTextFieldWk4DialogStaffName1 = new javax.swing.JTextField();
        jTextFieldWk4DialogStaffName2 = new javax.swing.JTextField();
        jTextFieldWk4DialogStaffName3 = new javax.swing.JTextField();
        jButtonDialogWk4Reset = new javax.swing.JButton();
        jButtonDialogWk4Add = new javax.swing.JButton();
        jLabelWk4DialogBudget = new javax.swing.JLabel();
        jTextFieldWk4DialogBudget = new javax.swing.JTextField();
        jCheckBoxDialogWk4BrkFast = new javax.swing.JCheckBox();
        jCheckBoxDialogWk4Lunch = new javax.swing.JCheckBox();
        jCheckBoxDialogWk4Dinner = new javax.swing.JCheckBox();
        jCheckBoxDialogWk4Acc = new javax.swing.JCheckBox();
        jLabelDialogWk4Dis4 = new javax.swing.JLabel();
        jLabelWk4DialogActDis = new javax.swing.JLabel();
        jLabelDialogWk4Dis = new javax.swing.JLabel();
        jLabelRemain4 = new javax.swing.JLabel();
        jCheckBoxDialogWk4Inc = new javax.swing.JCheckBox();
        jCheckBoxDialogWk4Misc = new javax.swing.JCheckBox();
        jLabelWk4Misc = new javax.swing.JLabel();
        jTextFieldWk4Misc = new javax.swing.JTextField();
        jButtonDialogWk4Close = new javax.swing.JButton();
        jLabelWk4MiscAmt = new javax.swing.JLabel();
        jTextFieldWk4MiscAmt = new javax.swing.JTextField();
        jLabelWk4Name1Del = new javax.swing.JLabel();
        jLabelWk4Name2Del = new javax.swing.JLabel();
        jLabelWk4Name3Del = new javax.swing.JLabel();
        jLabelWk4Name4Del = new javax.swing.JLabel();
        jDialogWaitingEmail = new javax.swing.JDialog();
        jDialogWk5 = new javax.swing.JDialog();
        jPanel5 = new javax.swing.JPanel();
        jLabelDialogWk5ActivityDate = new javax.swing.JLabel();
        jDateChooserDialogWk5ActivityDate = new com.toedter.calendar.JDateChooser();
        jLabelDialogWk5Site = new javax.swing.JLabel();
        jTextFieldDialogWk5Site = new javax.swing.JTextField();
        jLabelWk5DialogJustification = new javax.swing.JLabel();
        jLabelDialogWk5SiteClass = new javax.swing.JLabel();
        jLabelDialogWk5ActSiteClass = new javax.swing.JLabel();
        jLabelDialogWk5Rank = new javax.swing.JLabel();
        jLabelWk5DialogStaffName4 = new javax.swing.JLabel();
        jTextFieldWk5DialogActivityDesc = new javax.swing.JTextField();
        jLabelWk5DialogActivityDesc = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextAreaWk5DialogJustification = new javax.swing.JTextArea();
        jSeparator36 = new javax.swing.JSeparator();
        jLabelWk5DialogActRank = new javax.swing.JLabel();
        jComboBoxDialogWk5PerDiem = new javax.swing.JComboBox<>();
        jLabelDialogWk5PerDiem = new javax.swing.JLabel();
        jLabelWk5DialogStaff = new javax.swing.JLabel();
        jLabelWk5DialogStaffName1 = new javax.swing.JLabel();
        jLabelWk5DialogStaffName2 = new javax.swing.JLabel();
        jLabelWk5DialogStaffName3 = new javax.swing.JLabel();
        jTextFieldWk5DialogStaffName4 = new javax.swing.JTextField();
        jTextFieldWk5DialogStaffName1 = new javax.swing.JTextField();
        jTextFieldWk5DialogStaffName2 = new javax.swing.JTextField();
        jTextFieldWk5DialogStaffName3 = new javax.swing.JTextField();
        jButtonDialogWk5Reset = new javax.swing.JButton();
        jButtonDialogWk5Add = new javax.swing.JButton();
        jLabelWk5DialogBudget = new javax.swing.JLabel();
        jTextFieldWk5DialogBudget = new javax.swing.JTextField();
        jCheckBoxDialogWk5BrkFast = new javax.swing.JCheckBox();
        jCheckBoxDialogWk5Lunch = new javax.swing.JCheckBox();
        jCheckBoxDialogWk5Dinner = new javax.swing.JCheckBox();
        jCheckBoxDialogWk5Acc = new javax.swing.JCheckBox();
        jLabelDialogWk5Dis5 = new javax.swing.JLabel();
        jLabelWk5DialogActDis = new javax.swing.JLabel();
        jLabelDialogWk5Dis = new javax.swing.JLabel();
        jLabelRemain5 = new javax.swing.JLabel();
        jCheckBoxDialogWk5Inc = new javax.swing.JCheckBox();
        jCheckBoxDialogWk5Misc = new javax.swing.JCheckBox();
        jLabelWk5Misc = new javax.swing.JLabel();
        jTextFieldWk5Misc = new javax.swing.JTextField();
        jButtonDialogWk5Close = new javax.swing.JButton();
        jLabelWk5MiscAmt = new javax.swing.JLabel();
        jTextFieldWk5MiscAmt = new javax.swing.JTextField();
        jLabelWk5Name1Del = new javax.swing.JLabel();
        jLabelWk5Name2Del = new javax.swing.JLabel();
        jLabelWk5Name3Del = new javax.swing.JLabel();
        jLabelWk5Name4Del = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanelWkOne = new javax.swing.JPanel();
        jLabelWkDuration = new javax.swing.JLabel();
        jDateChooserWk1From = new com.toedter.calendar.JDateChooser();
        jLabelWk1From = new javax.swing.JLabel();
        jLabelWk1To = new javax.swing.JLabel();
        jDateChooserWk1To = new com.toedter.calendar.JDateChooser();
        jButtonWk1AddActivity = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableWk1Activities = new javax.swing.JTable();
        jLabelLogo1 = new javax.swing.JLabel();
        jButtonWk1DelActivity = new javax.swing.JButton();
        jLabelProvince1 = new javax.swing.JLabel();
        jLabelOffice1 = new javax.swing.JLabel();
        jLabelRef = new javax.swing.JLabel();
        jLabelSerial = new javax.swing.JLabel();
        jLabelRefNum = new javax.swing.JLabel();
        jLabelHeaderGen4 = new javax.swing.JLabel();
        jLabelEmp = new javax.swing.JLabel();
        jLabelGenLogNam1 = new javax.swing.JLabel();
        jLabelLineLogNam = new javax.swing.JLabel();
        jLabelLineTime = new javax.swing.JLabel();
        jLabelLineDate = new javax.swing.JLabel();
        jLabelProvince = new javax.swing.JLabel();
        jLabelDistrict = new javax.swing.JLabel();
        jPanelWkTwo = new javax.swing.JPanel();
        jLabelWkDuration1 = new javax.swing.JLabel();
        jDateChooserWk2From = new com.toedter.calendar.JDateChooser();
        jLabelWk1From1 = new javax.swing.JLabel();
        jLabelWk1To1 = new javax.swing.JLabel();
        jDateChooserWk2To = new com.toedter.calendar.JDateChooser();
        jButtonWk2AddActivity = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableWk2Activities = new javax.swing.JTable();
        jLabelLogo2 = new javax.swing.JLabel();
        jButtonWk2DelActivity = new javax.swing.JButton();
        jLabelGenLogNam3 = new javax.swing.JLabel();
        jLabelLineDate1 = new javax.swing.JLabel();
        jLabelLineTime1 = new javax.swing.JLabel();
        jLabelLineLogNam1 = new javax.swing.JLabel();
        jLabelHeaderGen7 = new javax.swing.JLabel();
        jPanelWkThree = new javax.swing.JPanel();
        jLabelWkDuration2 = new javax.swing.JLabel();
        jDateChooserWk3From = new com.toedter.calendar.JDateChooser();
        jLabelWk3From = new javax.swing.JLabel();
        jLabelWk3To = new javax.swing.JLabel();
        jDateChooserWk3To = new com.toedter.calendar.JDateChooser();
        jButtonWk3AddActivity = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableWk3Activities = new javax.swing.JTable();
        jLabelLogo3 = new javax.swing.JLabel();
        jButtonWk3DelActivity = new javax.swing.JButton();
        jLabelGenLogNam5 = new javax.swing.JLabel();
        jLabelLineLogNam2 = new javax.swing.JLabel();
        jLabelLineTime2 = new javax.swing.JLabel();
        jLabelLineDate2 = new javax.swing.JLabel();
        jLabelHeaderGen8 = new javax.swing.JLabel();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuNew = new javax.swing.JMenu();
        jMenuItemRequest = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcquittal = new javax.swing.JMenuItem();
        jSeparator14 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSpecialAcquittal = new javax.swing.JMenuItem();
        jSeparator19 = new javax.swing.JPopupMenu.Separator();
        jMenuItemRequestMOH = new javax.swing.JMenuItem();
        jSeparator28 = new javax.swing.JPopupMenu.Separator();
        jMenuItem3WkPlan = new javax.swing.JMenuItem();
        jSeparator11 = new javax.swing.JPopupMenu.Separator();
        jMenuEdit = new javax.swing.JMenu();
        jMenuReqEdit = new javax.swing.JMenuItem();
        jSeparator29 = new javax.swing.JPopupMenu.Separator();
        jMenuAcqEdit = new javax.swing.JMenuItem();
        jSeparator30 = new javax.swing.JPopupMenu.Separator();
        jMenuMonPlanEdit = new javax.swing.JMenuItem();
        jSeparator12 = new javax.swing.JPopupMenu.Separator();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator33 = new javax.swing.JPopupMenu.Separator();
        jMenuItemClose = new javax.swing.JMenuItem();
        jSeparator13 = new javax.swing.JPopupMenu.Separator();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuRequest = new javax.swing.JMenu();
        jMenuItemSupApp = new javax.swing.JMenuItem();
        jSeparator15 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAccMgrRev = new javax.swing.JMenuItem();
        jSeparator16 = new javax.swing.JPopupMenu.Separator();
        jMenuItemHeadApp = new javax.swing.JMenuItem();
        jSeparator17 = new javax.swing.JPopupMenu.Separator();
        jMenuPlanApproval = new javax.swing.JMenuItem();
        jSeparator18 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSchGen = new javax.swing.JMenuItem();
        jSeparator20 = new javax.swing.JPopupMenu.Separator();
        jMenuItemView = new javax.swing.JMenuItem();
        jMenuAcquittal = new javax.swing.JMenu();
        jMenuItemAcqSupApp = new javax.swing.JMenuItem();
        jSeparator21 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqAccApp = new javax.swing.JMenuItem();
        jSeparator22 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqHeadApp = new javax.swing.JMenuItem();
        jSeparator23 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqFinApp = new javax.swing.JMenuItem();
        jSeparator24 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqSchGen = new javax.swing.JMenuItem();
        jSeparator25 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqView = new javax.swing.JMenuItem();
        jMenuMonthlyPlan = new javax.swing.JMenu();
        jMenuItemProvMgrApproval = new javax.swing.JMenuItem();
        jSeparator26 = new javax.swing.JPopupMenu.Separator();
        jMenuItemReqGenLst = new javax.swing.JMenuItem();
        jSeparator34 = new javax.swing.JPopupMenu.Separator();
        jMenuItemPlanView = new javax.swing.JMenuItem();
        jMenuReports = new javax.swing.JMenu();
        jMenuItemReqSubmitted = new javax.swing.JMenuItem();
        jSeparator27 = new javax.swing.JPopupMenu.Separator();
        jMenuAgeAlluser = new javax.swing.JMenuItem();
        jSeparator31 = new javax.swing.JPopupMenu.Separator();
        jMenuAgeByUser = new javax.swing.JMenuItem();
        jSeparator32 = new javax.swing.JPopupMenu.Separator();
        jMenuProcessTimeline = new javax.swing.JMenuItem();

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

        jDialogSearchName2.setTitle("Search Employee ID");
        jDialogSearchName2.setAlwaysOnTop(true);
        jDialogSearchName2.setBackground(new java.awt.Color(153, 204, 0));
        jDialogSearchName2.setLocation(new java.awt.Point(700, 400));
        jDialogSearchName2.setMinimumSize(new java.awt.Dimension(400, 200));

        jPanelSearchID1.setBackground(new java.awt.Color(7, 163, 163));
        jPanelSearchID1.setMinimumSize(new java.awt.Dimension(400, 200));
        jPanelSearchID1.setLayout(null);

        jTextFieldSearchNam2.setPreferredSize(new java.awt.Dimension(50, 30));
        jTextFieldSearchNam2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldSearchNam2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldSearchNam2FocusLost(evt);
            }
        });
        jTextFieldSearchNam2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldSearchNam2ActionPerformed(evt);
            }
        });
        jPanelSearchID1.add(jTextFieldSearchNam2);
        jTextFieldSearchNam2.setBounds(10, 22, 204, 30);

        jButtonSearch2.setText("Search");
        jButtonSearch2.setPreferredSize(new java.awt.Dimension(65, 30));
        jButtonSearch2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearch2ActionPerformed(evt);
            }
        });
        jPanelSearchID1.add(jButtonSearch2);
        jButtonSearch2.setBounds(256, 22, 90, 30);

        jComboBoxSearchResult2.setPreferredSize(new java.awt.Dimension(70, 30));
        jPanelSearchID1.add(jComboBoxSearchResult2);
        jComboBoxSearchResult2.setBounds(10, 70, 204, 30);

        jButtonOk2.setText("OK");
        jButtonOk2.setPreferredSize(new java.awt.Dimension(73, 30));
        jButtonOk2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOk2ActionPerformed(evt);
            }
        });
        jPanelSearchID1.add(jButtonOk2);
        jButtonOk2.setBounds(256, 70, 90, 30);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Enter search term e.g. Joe and click search button");
        jPanelSearchID1.add(jLabel5);
        jLabel5.setBounds(12, 3, 270, 13);

        javax.swing.GroupLayout jDialogSearchName2Layout = new javax.swing.GroupLayout(jDialogSearchName2.getContentPane());
        jDialogSearchName2.getContentPane().setLayout(jDialogSearchName2Layout);
        jDialogSearchName2Layout.setHorizontalGroup(
            jDialogSearchName2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelSearchID1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogSearchName2Layout.setVerticalGroup(
            jDialogSearchName2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelSearchID1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jDialogSearchName3.setTitle("Search Employee ID");
        jDialogSearchName3.setAlwaysOnTop(true);
        jDialogSearchName3.setBackground(new java.awt.Color(153, 204, 0));
        jDialogSearchName3.setLocation(new java.awt.Point(700, 400));
        jDialogSearchName3.setMinimumSize(new java.awt.Dimension(400, 200));

        jPanelSearchID2.setBackground(new java.awt.Color(7, 163, 163));
        jPanelSearchID2.setMinimumSize(new java.awt.Dimension(400, 200));
        jPanelSearchID2.setLayout(null);

        jTextFieldSearchNam3.setPreferredSize(new java.awt.Dimension(50, 30));
        jTextFieldSearchNam3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldSearchNam3FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldSearchNam3FocusLost(evt);
            }
        });
        jTextFieldSearchNam3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldSearchNam3ActionPerformed(evt);
            }
        });
        jPanelSearchID2.add(jTextFieldSearchNam3);
        jTextFieldSearchNam3.setBounds(10, 22, 204, 30);

        jButtonSearch3.setText("Search");
        jButtonSearch3.setPreferredSize(new java.awt.Dimension(65, 30));
        jButtonSearch3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearch3ActionPerformed(evt);
            }
        });
        jPanelSearchID2.add(jButtonSearch3);
        jButtonSearch3.setBounds(256, 22, 90, 30);

        jComboBoxSearchResult3.setPreferredSize(new java.awt.Dimension(70, 30));
        jPanelSearchID2.add(jComboBoxSearchResult3);
        jComboBoxSearchResult3.setBounds(10, 70, 204, 30);

        jButtonOk3.setText("OK");
        jButtonOk3.setPreferredSize(new java.awt.Dimension(73, 30));
        jButtonOk3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOk3ActionPerformed(evt);
            }
        });
        jPanelSearchID2.add(jButtonOk3);
        jButtonOk3.setBounds(256, 70, 90, 30);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Enter search term e.g. Joe and click search button");
        jPanelSearchID2.add(jLabel6);
        jLabel6.setBounds(12, 3, 270, 13);

        javax.swing.GroupLayout jDialogSearchName3Layout = new javax.swing.GroupLayout(jDialogSearchName3.getContentPane());
        jDialogSearchName3.getContentPane().setLayout(jDialogSearchName3Layout);
        jDialogSearchName3Layout.setHorizontalGroup(
            jDialogSearchName3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelSearchID2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogSearchName3Layout.setVerticalGroup(
            jDialogSearchName3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelSearchID2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jDialogSearchName4.setTitle("Search Employee ID");
        jDialogSearchName4.setAlwaysOnTop(true);
        jDialogSearchName4.setBackground(new java.awt.Color(153, 204, 0));
        jDialogSearchName4.setLocation(new java.awt.Point(700, 400));
        jDialogSearchName4.setMinimumSize(new java.awt.Dimension(400, 200));

        jPanelSearchID4.setBackground(new java.awt.Color(7, 163, 163));
        jPanelSearchID4.setMinimumSize(new java.awt.Dimension(400, 200));
        jPanelSearchID4.setLayout(null);

        jTextFieldSearchNam4.setPreferredSize(new java.awt.Dimension(50, 30));
        jTextFieldSearchNam4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldSearchNam4FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldSearchNam4FocusLost(evt);
            }
        });
        jTextFieldSearchNam4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldSearchNam4ActionPerformed(evt);
            }
        });
        jPanelSearchID4.add(jTextFieldSearchNam4);
        jTextFieldSearchNam4.setBounds(10, 22, 204, 30);

        jButtonSearch4.setText("Search");
        jButtonSearch4.setPreferredSize(new java.awt.Dimension(65, 30));
        jButtonSearch4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearch4ActionPerformed(evt);
            }
        });
        jPanelSearchID4.add(jButtonSearch4);
        jButtonSearch4.setBounds(256, 22, 90, 30);

        jComboBoxSearchResult4.setPreferredSize(new java.awt.Dimension(70, 30));
        jPanelSearchID4.add(jComboBoxSearchResult4);
        jComboBoxSearchResult4.setBounds(10, 70, 204, 30);

        jButtonOk4.setText("OK");
        jButtonOk4.setPreferredSize(new java.awt.Dimension(73, 30));
        jButtonOk4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOk4ActionPerformed(evt);
            }
        });
        jPanelSearchID4.add(jButtonOk4);
        jButtonOk4.setBounds(256, 70, 90, 30);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Enter search term e.g. Joe and click search button");
        jPanelSearchID4.add(jLabel8);
        jLabel8.setBounds(12, 3, 270, 13);

        javax.swing.GroupLayout jDialogSearchName4Layout = new javax.swing.GroupLayout(jDialogSearchName4.getContentPane());
        jDialogSearchName4.getContentPane().setLayout(jDialogSearchName4Layout);
        jDialogSearchName4Layout.setHorizontalGroup(
            jDialogSearchName4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelSearchID4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogSearchName4Layout.setVerticalGroup(
            jDialogSearchName4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelSearchID4, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        jDialogWk1.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
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
        jLabelDialogWk1ActivityDate.setBounds(20, 40, 90, 30);

        jDateChooserDialogWk1ActivityDate.setDateFormatString("yyyy-MM-dd");
        jPanel1.add(jDateChooserDialogWk1ActivityDate);
        jDateChooserDialogWk1ActivityDate.setBounds(120, 40, 120, 30);

        jLabelDialogWk1Site.setText("Site");
        jPanel1.add(jLabelDialogWk1Site);
        jLabelDialogWk1Site.setBounds(20, 80, 40, 13);

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
        jTextFieldDialogWk1Site.setBounds(20, 110, 420, 30);

        jLabelWk1DialogJustification.setText("Justification for Choice of Activity ");
        jPanel1.add(jLabelWk1DialogJustification);
        jLabelWk1DialogJustification.setBounds(20, 410, 340, 30);

        jLabelDialogWk1SiteClass.setText("Site Classification");
        jPanel1.add(jLabelDialogWk1SiteClass);
        jLabelDialogWk1SiteClass.setBounds(20, 160, 130, 30);

        jLabelDialogWk1ActSiteClass.setText("Near Site");
        jPanel1.add(jLabelDialogWk1ActSiteClass);
        jLabelDialogWk1ActSiteClass.setBounds(210, 160, 130, 30);

        jLabelDialogWk1Rank.setText("Consortium Rank ");
        jPanel1.add(jLabelDialogWk1Rank);
        jLabelDialogWk1Rank.setBounds(20, 190, 130, 30);

        jLabel10.setText("4.");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(550, 240, 20, 30);
        jPanel1.add(jTextFieldWk1DialogActivityDesc);
        jTextFieldWk1DialogActivityDesc.setBounds(20, 370, 410, 30);

        jLabelWk1DialogActivityDesc.setText("Activity Descrpition");
        jPanel1.add(jLabelWk1DialogActivityDesc);
        jLabelWk1DialogActivityDesc.setBounds(20, 330, 130, 30);

        jTextAreaWk1DialogJustification.setColumns(20);
        jTextAreaWk1DialogJustification.setRows(5);
        jTextAreaWk1DialogJustification.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextAreaWk1DialogJustificationKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(jTextAreaWk1DialogJustification);

        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(20, 460, 500, 96);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel1.add(jSeparator3);
        jSeparator3.setBounds(530, 10, 20, 550);

        jLabelWk1DialogActRank.setText("Near Site");
        jPanel1.add(jLabelWk1DialogActRank);
        jLabelWk1DialogActRank.setBounds(210, 190, 130, 30);

        jComboBoxDialogWk1PerDiem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "OPHID", "MOHCC", "OPHID and MOHCC" }));
        jComboBoxDialogWk1PerDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxDialogWk1PerDiemActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBoxDialogWk1PerDiem);
        jComboBoxDialogWk1PerDiem.setBounds(550, 40, 200, 30);

        jLabelDialogPerDiem.setText("Require Per Diem");
        jPanel1.add(jLabelDialogPerDiem);
        jLabelDialogPerDiem.setBounds(550, 10, 130, 30);

        jLabelWk1DialogStaffName.setText("OPHID Staff Only");
        jPanel1.add(jLabelWk1DialogStaffName);
        jLabelWk1DialogStaffName.setBounds(550, 90, 130, 30);

        jLabelWk1DialogStaffName1.setText("1.");
        jPanel1.add(jLabelWk1DialogStaffName1);
        jLabelWk1DialogStaffName1.setBounds(550, 120, 20, 30);

        jLabelWk1DialogStaffName2.setText("2.");
        jPanel1.add(jLabelWk1DialogStaffName2);
        jLabelWk1DialogStaffName2.setBounds(550, 160, 20, 30);

        jLabel17.setText("3.");
        jPanel1.add(jLabel17);
        jLabel17.setBounds(550, 200, 20, 30);

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
        jTextFieldWk1DialogStaffName4.setBounds(570, 240, 220, 30);

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
        jTextFieldWk1DialogStaffName1.setBounds(570, 120, 220, 30);

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
        jTextFieldWk1DialogStaffName2.setBounds(570, 160, 220, 30);

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
        jTextFieldWk1DialogStaffName3.setBounds(570, 200, 220, 30);

        jButtonDialogWk1Reset.setText("Reset");
        jButtonDialogWk1Reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDialogWk1ResetActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonDialogWk1Reset);
        jButtonDialogWk1Reset.setBounds(645, 520, 70, 30);

        jButtonDialogWk1Add.setText("Add Activity");
        jButtonDialogWk1Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDialogWk1AddActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonDialogWk1Add);
        jButtonDialogWk1Add.setBounds(535, 520, 100, 30);

        jLabelWk1DialogBudget.setText("Budget Line");
        jPanel1.add(jLabelWk1DialogBudget);
        jLabelWk1DialogBudget.setBounds(20, 250, 130, 30);

        jTextFieldWk1DialogBudget.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk1DialogBudgetMouseClicked(evt);
            }
        });
        jTextFieldWk1DialogBudget.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk1DialogBudgetKeyTyped(evt);
            }
        });
        jPanel1.add(jTextFieldWk1DialogBudget);
        jTextFieldWk1DialogBudget.setBounds(20, 290, 410, 30);

        jCheckBoxDialogWk1BrkFast.setText(" Breakfast");
        jPanel1.add(jCheckBoxDialogWk1BrkFast);
        jCheckBoxDialogWk1BrkFast.setBounds(550, 300, 90, 21);

        jCheckBoxDialogWk1Lunch.setText("Lunch");
        jPanel1.add(jCheckBoxDialogWk1Lunch);
        jCheckBoxDialogWk1Lunch.setBounds(670, 300, 80, 21);

        jCheckBoxDialogWk1Dinner.setText(" Dinner");
        jPanel1.add(jCheckBoxDialogWk1Dinner);
        jCheckBoxDialogWk1Dinner.setBounds(550, 350, 90, 21);

        jCheckBoxDialogWk1Acc.setText(" Unproved Acc");
        jPanel1.add(jCheckBoxDialogWk1Acc);
        jCheckBoxDialogWk1Acc.setBounds(670, 350, 130, 21);

        jLabelDialogWk1Dis.setText("Km");
        jPanel1.add(jLabelDialogWk1Dis);
        jLabelDialogWk1Dis.setBounds(280, 220, 50, 30);

        jLabelWk1DialogActDis.setText("Near Site");
        jPanel1.add(jLabelWk1DialogActDis);
        jLabelWk1DialogActDis.setBounds(210, 220, 70, 30);

        jLabelDialogWk1Dis1.setText("Distance From Base");
        jPanel1.add(jLabelDialogWk1Dis1);
        jLabelDialogWk1Dis1.setBounds(20, 220, 130, 30);

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
        jCheckBoxDialogWk1Misc.setBounds(670, 400, 140, 21);

        jCheckBoxDialogWk1Inc.setText("Incidental");
        jPanel1.add(jCheckBoxDialogWk1Inc);
        jCheckBoxDialogWk1Inc.setBounds(550, 400, 110, 21);

        jLabelWk1Misc.setText("Miscellaneous Desc");
        jPanel1.add(jLabelWk1Misc);
        jLabelWk1Misc.setBounds(560, 430, 160, 30);
        jPanel1.add(jTextFieldWk1Misc);
        jTextFieldWk1Misc.setBounds(560, 460, 110, 30);

        jButtonDialogWk1Close.setText("Close");
        jButtonDialogWk1Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDialogWk1CloseActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonDialogWk1Close);
        jButtonDialogWk1Close.setBounds(725, 520, 70, 30);

        jLabelWk1MiscAmt.setText("$");
        jPanel1.add(jLabelWk1MiscAmt);
        jLabelWk1MiscAmt.setBounds(690, 460, 30, 30);
        jPanel1.add(jTextFieldWk1MiscAmt);
        jTextFieldWk1MiscAmt.setBounds(730, 460, 70, 30);

        jLabelWk1Name4Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk1Name4Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk1Name4DelMouseClicked(evt);
            }
        });
        jPanel1.add(jLabelWk1Name4Del);
        jLabelWk1Name4Del.setBounds(795, 240, 30, 30);

        jLabelWk1Name1Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk1Name1Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk1Name1DelMouseClicked(evt);
            }
        });
        jPanel1.add(jLabelWk1Name1Del);
        jLabelWk1Name1Del.setBounds(795, 120, 30, 30);

        jLabelWk1Name2Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk1Name2Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk1Name2DelMouseClicked(evt);
            }
        });
        jPanel1.add(jLabelWk1Name2Del);
        jLabelWk1Name2Del.setBounds(795, 160, 30, 30);

        jLabelWk1Name3Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk1Name3Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk1Name3DelMouseClicked(evt);
            }
        });
        jPanel1.add(jLabelWk1Name3Del);
        jLabelWk1Name3Del.setBounds(795, 200, 30, 30);

        javax.swing.GroupLayout jDialogWk1Layout = new javax.swing.GroupLayout(jDialogWk1.getContentPane());
        jDialogWk1.getContentPane().setLayout(jDialogWk1Layout);
        jDialogWk1Layout.setHorizontalGroup(
            jDialogWk1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
        );
        jDialogWk1Layout.setVerticalGroup(
            jDialogWk1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogWk1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
                .addContainerGap())
        );

        jDialogWk2.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jDialogWk2.setAlwaysOnTop(true);
        jDialogWk2.setBackground(new java.awt.Color(153, 255, 153));
        jDialogWk2.setLocation(new java.awt.Point(300, 100));
        jDialogWk2.setName("dialogWk1"); // NOI18N
        jDialogWk2.setSize(new java.awt.Dimension(850, 650));

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));
        jPanel2.setPreferredSize(new java.awt.Dimension(800, 650));
        jPanel2.setLayout(null);

        jLabelDialogWk2ActivityDate.setText("Activity Date");
        jPanel2.add(jLabelDialogWk2ActivityDate);
        jLabelDialogWk2ActivityDate.setBounds(20, 40, 90, 30);

        jDateChooserDialogWk2ActivityDate.setDateFormatString("yyyy-MM-dd");
        jPanel2.add(jDateChooserDialogWk2ActivityDate);
        jDateChooserDialogWk2ActivityDate.setBounds(120, 40, 120, 30);

        jLabelDialogWk2Site.setText("Site");
        jPanel2.add(jLabelDialogWk2Site);
        jLabelDialogWk2Site.setBounds(20, 80, 40, 13);

        jTextFieldDialogWk2Site.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldDialogWk2SiteMouseClicked(evt);
            }
        });
        jTextFieldDialogWk2Site.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldDialogWk2SiteActionPerformed(evt);
            }
        });
        jTextFieldDialogWk2Site.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldDialogWk2SiteKeyTyped(evt);
            }
        });
        jPanel2.add(jTextFieldDialogWk2Site);
        jTextFieldDialogWk2Site.setBounds(20, 110, 420, 30);

        jLabelWk2DialogJustification.setText("Justification for Choice of Activity ");
        jPanel2.add(jLabelWk2DialogJustification);
        jLabelWk2DialogJustification.setBounds(20, 410, 340, 30);

        jLabelDialogWk2SiteClass.setText("Site Classification");
        jPanel2.add(jLabelDialogWk2SiteClass);
        jLabelDialogWk2SiteClass.setBounds(20, 160, 130, 30);

        jLabelDialogWk2ActSiteClass.setText("Near Site");
        jPanel2.add(jLabelDialogWk2ActSiteClass);
        jLabelDialogWk2ActSiteClass.setBounds(210, 160, 130, 30);

        jLabelDialogWk2Rank.setText("Consortium Rank ");
        jPanel2.add(jLabelDialogWk2Rank);
        jLabelDialogWk2Rank.setBounds(20, 190, 130, 30);

        jLabelWk2DialogStaffName4.setText("4.");
        jPanel2.add(jLabelWk2DialogStaffName4);
        jLabelWk2DialogStaffName4.setBounds(550, 240, 20, 30);
        jPanel2.add(jTextFieldWk2DialogActivityDesc);
        jTextFieldWk2DialogActivityDesc.setBounds(20, 370, 410, 30);

        jLabelWk2DialogActivityDesc.setText("Activity Descrpition");
        jPanel2.add(jLabelWk2DialogActivityDesc);
        jLabelWk2DialogActivityDesc.setBounds(20, 330, 130, 30);

        jTextAreaWk2DialogJustification.setColumns(20);
        jTextAreaWk2DialogJustification.setRows(5);
        jTextAreaWk2DialogJustification.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextAreaWk2DialogJustificationKeyTyped(evt);
            }
        });
        jScrollPane4.setViewportView(jTextAreaWk2DialogJustification);

        jPanel2.add(jScrollPane4);
        jScrollPane4.setBounds(20, 460, 500, 96);

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel2.add(jSeparator6);
        jSeparator6.setBounds(530, 10, 20, 550);

        jLabelWk2DialogActRank.setText("Near Site");
        jPanel2.add(jLabelWk2DialogActRank);
        jLabelWk2DialogActRank.setBounds(210, 190, 130, 30);

        jComboBoxDialogWk2PerDiem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "OPHID", "MOHCC", "OPHID and MOHCC" }));
        jComboBoxDialogWk2PerDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxDialogWk2PerDiemActionPerformed(evt);
            }
        });
        jPanel2.add(jComboBoxDialogWk2PerDiem);
        jComboBoxDialogWk2PerDiem.setBounds(550, 40, 200, 30);

        jLabelDialogWk2PerDiem.setText("Require Per Diem");
        jPanel2.add(jLabelDialogWk2PerDiem);
        jLabelDialogWk2PerDiem.setBounds(550, 10, 130, 30);

        jLabelWk2DialogStaff.setText("OPHID Staff Only");
        jPanel2.add(jLabelWk2DialogStaff);
        jLabelWk2DialogStaff.setBounds(550, 90, 130, 30);

        jLabelWk2DialogStaffName1.setText("1.");
        jPanel2.add(jLabelWk2DialogStaffName1);
        jLabelWk2DialogStaffName1.setBounds(550, 120, 20, 30);

        jLabelWk2DialogStaffName2.setText("2.");
        jPanel2.add(jLabelWk2DialogStaffName2);
        jLabelWk2DialogStaffName2.setBounds(550, 160, 20, 30);

        jLabelWk2DialogStaffName3.setText("3.");
        jPanel2.add(jLabelWk2DialogStaffName3);
        jLabelWk2DialogStaffName3.setBounds(550, 200, 20, 30);

        jTextFieldWk2DialogStaffName4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk2DialogStaffName4MouseClicked(evt);
            }
        });
        jTextFieldWk2DialogStaffName4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldWk2DialogStaffName4ActionPerformed(evt);
            }
        });
        jTextFieldWk2DialogStaffName4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk2DialogStaffName4KeyTyped(evt);
            }
        });
        jPanel2.add(jTextFieldWk2DialogStaffName4);
        jTextFieldWk2DialogStaffName4.setBounds(570, 240, 220, 30);

        jTextFieldWk2DialogStaffName1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk2DialogStaffName1MouseClicked(evt);
            }
        });
        jTextFieldWk2DialogStaffName1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldWk2DialogStaffName1ActionPerformed(evt);
            }
        });
        jTextFieldWk2DialogStaffName1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk2DialogStaffName1KeyTyped(evt);
            }
        });
        jPanel2.add(jTextFieldWk2DialogStaffName1);
        jTextFieldWk2DialogStaffName1.setBounds(570, 120, 220, 30);

        jTextFieldWk2DialogStaffName2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk2DialogStaffName2MouseClicked(evt);
            }
        });
        jTextFieldWk2DialogStaffName2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk2DialogStaffName2KeyTyped(evt);
            }
        });
        jPanel2.add(jTextFieldWk2DialogStaffName2);
        jTextFieldWk2DialogStaffName2.setBounds(570, 160, 220, 30);

        jTextFieldWk2DialogStaffName3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk2DialogStaffName3MouseClicked(evt);
            }
        });
        jTextFieldWk2DialogStaffName3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk2DialogStaffName3KeyTyped(evt);
            }
        });
        jPanel2.add(jTextFieldWk2DialogStaffName3);
        jTextFieldWk2DialogStaffName3.setBounds(570, 200, 220, 30);

        jButtonDialogWk2Reset.setText("Reset");
        jButtonDialogWk2Reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDialogWk2ResetActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonDialogWk2Reset);
        jButtonDialogWk2Reset.setBounds(645, 520, 70, 30);

        jButtonDialogWk2Add.setText("Add Activity");
        jButtonDialogWk2Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDialogWk2AddActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonDialogWk2Add);
        jButtonDialogWk2Add.setBounds(535, 520, 100, 30);

        jLabelWk2DialogBudget.setText("Budget Line");
        jPanel2.add(jLabelWk2DialogBudget);
        jLabelWk2DialogBudget.setBounds(20, 250, 130, 30);

        jTextFieldWk2DialogBudget.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk2DialogBudgetMouseClicked(evt);
            }
        });
        jTextFieldWk2DialogBudget.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk2DialogBudgetKeyTyped(evt);
            }
        });
        jPanel2.add(jTextFieldWk2DialogBudget);
        jTextFieldWk2DialogBudget.setBounds(20, 290, 410, 30);

        jCheckBoxDialogWk2BrkFast.setText(" Breakfast");
        jPanel2.add(jCheckBoxDialogWk2BrkFast);
        jCheckBoxDialogWk2BrkFast.setBounds(550, 300, 71, 21);

        jCheckBoxDialogWk2Lunch.setText("Lunch");
        jPanel2.add(jCheckBoxDialogWk2Lunch);
        jCheckBoxDialogWk2Lunch.setBounds(670, 300, 51, 21);

        jCheckBoxDialogWk2Dinner.setText(" Dinner");
        jPanel2.add(jCheckBoxDialogWk2Dinner);
        jCheckBoxDialogWk2Dinner.setBounds(550, 350, 59, 21);

        jCheckBoxDialogWk2Acc.setText(" Unproved Acc");
        jPanel2.add(jCheckBoxDialogWk2Acc);
        jCheckBoxDialogWk2Acc.setBounds(670, 350, 120, 21);

        jLabelDialogWk1Dis2.setText("Km");
        jPanel2.add(jLabelDialogWk1Dis2);
        jLabelDialogWk1Dis2.setBounds(280, 220, 50, 30);

        jLabelWk2DialogActDis.setText("Near Site");
        jPanel2.add(jLabelWk2DialogActDis);
        jLabelWk2DialogActDis.setBounds(210, 220, 70, 30);

        jLabelDialogWk2Dis.setText("Distance From Base");
        jPanel2.add(jLabelDialogWk2Dis);
        jLabelDialogWk2Dis.setBounds(20, 220, 130, 30);

        jLabelRemain1.setFont(new java.awt.Font("Tahoma", 3, 10)); // NOI18N
        jPanel2.add(jLabelRemain1);
        jLabelRemain1.setBounds(20, 440, 250, 20);

        jCheckBoxDialogWk2Inc.setText("Incidental");
        jPanel2.add(jCheckBoxDialogWk2Inc);
        jCheckBoxDialogWk2Inc.setBounds(550, 400, 100, 21);
        jPanel2.add(jTextFieldWk2Misc);
        jTextFieldWk2Misc.setBounds(560, 460, 110, 30);

        jLabelWk2Misc.setText("Miscellaneous Desc");
        jPanel2.add(jLabelWk2Misc);
        jLabelWk2Misc.setBounds(560, 430, 160, 30);

        jCheckBoxDialogWk2Misc.setText("Miscellaneous");
        jCheckBoxDialogWk2Misc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDialogWk2MiscActionPerformed(evt);
            }
        });
        jPanel2.add(jCheckBoxDialogWk2Misc);
        jCheckBoxDialogWk2Misc.setBounds(680, 400, 130, 21);

        jButtonDialogWk2Close.setText("Close");
        jButtonDialogWk2Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDialogWk2CloseActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonDialogWk2Close);
        jButtonDialogWk2Close.setBounds(725, 520, 70, 30);

        jLabelWk2MiscAmt.setText("$");
        jPanel2.add(jLabelWk2MiscAmt);
        jLabelWk2MiscAmt.setBounds(690, 460, 30, 30);
        jPanel2.add(jTextFieldWk2MiscAmt);
        jTextFieldWk2MiscAmt.setBounds(730, 460, 70, 30);

        jLabelWk2Name1Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk2Name1Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk2Name1DelMouseClicked(evt);
            }
        });
        jPanel2.add(jLabelWk2Name1Del);
        jLabelWk2Name1Del.setBounds(795, 120, 30, 30);

        jLabelWk2Name2Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk2Name2Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk2Name2DelMouseClicked(evt);
            }
        });
        jPanel2.add(jLabelWk2Name2Del);
        jLabelWk2Name2Del.setBounds(795, 160, 30, 30);

        jLabelWk2Name3Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk2Name3Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk2Name3DelMouseClicked(evt);
            }
        });
        jPanel2.add(jLabelWk2Name3Del);
        jLabelWk2Name3Del.setBounds(795, 200, 30, 30);

        jLabelWk2Name4Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk2Name4Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk2Name4DelMouseClicked(evt);
            }
        });
        jPanel2.add(jLabelWk2Name4Del);
        jLabelWk2Name4Del.setBounds(795, 240, 30, 30);

        javax.swing.GroupLayout jDialogWk2Layout = new javax.swing.GroupLayout(jDialogWk2.getContentPane());
        jDialogWk2.getContentPane().setLayout(jDialogWk2Layout);
        jDialogWk2Layout.setHorizontalGroup(
            jDialogWk2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 850, Short.MAX_VALUE)
        );
        jDialogWk2Layout.setVerticalGroup(
            jDialogWk2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 615, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jDialogWk3.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jDialogWk3.setAlwaysOnTop(true);
        jDialogWk3.setBackground(new java.awt.Color(153, 255, 153));
        jDialogWk3.setLocation(new java.awt.Point(300, 100));
        jDialogWk3.setName("dialogWk1"); // NOI18N
        jDialogWk3.setSize(new java.awt.Dimension(850, 650));

        jPanel3.setBackground(new java.awt.Color(84, 110, 240));
        jPanel3.setPreferredSize(new java.awt.Dimension(850, 650));
        jPanel3.setLayout(null);

        jLabelDialogWk3ActivityDate.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDialogWk3ActivityDate.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDialogWk3ActivityDate.setText("Activity Date");
        jPanel3.add(jLabelDialogWk3ActivityDate);
        jLabelDialogWk3ActivityDate.setBounds(20, 40, 90, 30);

        jDateChooserDialogWk3ActivityDate.setDateFormatString("yyyy-MM-dd");
        jPanel3.add(jDateChooserDialogWk3ActivityDate);
        jDateChooserDialogWk3ActivityDate.setBounds(120, 40, 120, 30);

        jLabelDialogWk3Site.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDialogWk3Site.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDialogWk3Site.setText("Site");
        jPanel3.add(jLabelDialogWk3Site);
        jLabelDialogWk3Site.setBounds(20, 80, 50, 16);

        jTextFieldDialogWk3Site.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldDialogWk3SiteMouseClicked(evt);
            }
        });
        jTextFieldDialogWk3Site.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldDialogWk3SiteActionPerformed(evt);
            }
        });
        jTextFieldDialogWk3Site.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldDialogWk3SiteKeyTyped(evt);
            }
        });
        jPanel3.add(jTextFieldDialogWk3Site);
        jTextFieldDialogWk3Site.setBounds(20, 110, 420, 30);

        jLabelWk3DialogJustification.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWk3DialogJustification.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWk3DialogJustification.setText("Justification for Choice of Activity ");
        jPanel3.add(jLabelWk3DialogJustification);
        jLabelWk3DialogJustification.setBounds(20, 410, 340, 30);

        jLabelDialogWk3SiteClass.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDialogWk3SiteClass.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDialogWk3SiteClass.setText("Site Classification");
        jPanel3.add(jLabelDialogWk3SiteClass);
        jLabelDialogWk3SiteClass.setBounds(20, 160, 130, 30);

        jLabelDialogWk3ActSiteClass.setText("Near Site");
        jPanel3.add(jLabelDialogWk3ActSiteClass);
        jLabelDialogWk3ActSiteClass.setBounds(210, 160, 130, 30);

        jLabelDialogWk3Rank.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDialogWk3Rank.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDialogWk3Rank.setText("Consortium Rank ");
        jPanel3.add(jLabelDialogWk3Rank);
        jLabelDialogWk3Rank.setBounds(20, 190, 130, 30);

        jLabelWk3DialogStaffName4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWk3DialogStaffName4.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWk3DialogStaffName4.setText("4.");
        jPanel3.add(jLabelWk3DialogStaffName4);
        jLabelWk3DialogStaffName4.setBounds(550, 240, 20, 30);
        jPanel3.add(jTextFieldWk3DialogActivityDesc);
        jTextFieldWk3DialogActivityDesc.setBounds(20, 370, 410, 30);

        jLabelWk3DialogActivityDesc.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWk3DialogActivityDesc.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWk3DialogActivityDesc.setText("Activity Descrpition");
        jPanel3.add(jLabelWk3DialogActivityDesc);
        jLabelWk3DialogActivityDesc.setBounds(20, 330, 130, 30);

        jTextAreaWk3DialogJustification.setColumns(20);
        jTextAreaWk3DialogJustification.setRows(5);
        jTextAreaWk3DialogJustification.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextAreaWk3DialogJustificationKeyTyped(evt);
            }
        });
        jScrollPane6.setViewportView(jTextAreaWk3DialogJustification);

        jPanel3.add(jScrollPane6);
        jScrollPane6.setBounds(20, 460, 500, 96);

        jSeparator9.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel3.add(jSeparator9);
        jSeparator9.setBounds(530, 10, 20, 550);

        jLabelWk3DialogActRank.setText("Near Site");
        jPanel3.add(jLabelWk3DialogActRank);
        jLabelWk3DialogActRank.setBounds(210, 190, 130, 30);

        jComboBoxDialogWk3PerDiem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "OPHID", "MOHCC", "OPHID and MOHCC" }));
        jComboBoxDialogWk3PerDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxDialogWk3PerDiemActionPerformed(evt);
            }
        });
        jPanel3.add(jComboBoxDialogWk3PerDiem);
        jComboBoxDialogWk3PerDiem.setBounds(550, 40, 200, 30);

        jLabelDialogWk2PerDiem1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDialogWk2PerDiem1.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDialogWk2PerDiem1.setText("Require Per Diem");
        jPanel3.add(jLabelDialogWk2PerDiem1);
        jLabelDialogWk2PerDiem1.setBounds(550, 10, 130, 30);

        jLabelWk3DialogStaff.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWk3DialogStaff.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWk3DialogStaff.setText("OPHID Staff Only");
        jPanel3.add(jLabelWk3DialogStaff);
        jLabelWk3DialogStaff.setBounds(550, 90, 130, 30);

        jLabelWk3DialogStaffName1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWk3DialogStaffName1.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWk3DialogStaffName1.setText("1.");
        jPanel3.add(jLabelWk3DialogStaffName1);
        jLabelWk3DialogStaffName1.setBounds(550, 120, 20, 30);

        jLabelWk3DialogStaffName2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWk3DialogStaffName2.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWk3DialogStaffName2.setText("2.");
        jPanel3.add(jLabelWk3DialogStaffName2);
        jLabelWk3DialogStaffName2.setBounds(550, 160, 20, 30);

        jLabelWk3DialogStaffName3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWk3DialogStaffName3.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWk3DialogStaffName3.setText("3.");
        jPanel3.add(jLabelWk3DialogStaffName3);
        jLabelWk3DialogStaffName3.setBounds(550, 200, 20, 30);

        jTextFieldWk3DialogStaffName4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk3DialogStaffName4MouseClicked(evt);
            }
        });
        jTextFieldWk3DialogStaffName4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldWk3DialogStaffName4ActionPerformed(evt);
            }
        });
        jTextFieldWk3DialogStaffName4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk3DialogStaffName4KeyTyped(evt);
            }
        });
        jPanel3.add(jTextFieldWk3DialogStaffName4);
        jTextFieldWk3DialogStaffName4.setBounds(570, 240, 220, 30);

        jTextFieldWk3DialogStaffName1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk3DialogStaffName1MouseClicked(evt);
            }
        });
        jTextFieldWk3DialogStaffName1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldWk3DialogStaffName1ActionPerformed(evt);
            }
        });
        jTextFieldWk3DialogStaffName1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk3DialogStaffName1KeyTyped(evt);
            }
        });
        jPanel3.add(jTextFieldWk3DialogStaffName1);
        jTextFieldWk3DialogStaffName1.setBounds(570, 120, 220, 30);

        jTextFieldWk3DialogStaffName2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk3DialogStaffName2MouseClicked(evt);
            }
        });
        jTextFieldWk3DialogStaffName2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk3DialogStaffName2KeyTyped(evt);
            }
        });
        jPanel3.add(jTextFieldWk3DialogStaffName2);
        jTextFieldWk3DialogStaffName2.setBounds(570, 160, 220, 30);

        jTextFieldWk3DialogStaffName3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk3DialogStaffName3MouseClicked(evt);
            }
        });
        jTextFieldWk3DialogStaffName3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk3DialogStaffName3KeyTyped(evt);
            }
        });
        jPanel3.add(jTextFieldWk3DialogStaffName3);
        jTextFieldWk3DialogStaffName3.setBounds(570, 200, 220, 30);

        jButtonDialogWk3Reset.setText("Reset");
        jButtonDialogWk3Reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDialogWk3ResetActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonDialogWk3Reset);
        jButtonDialogWk3Reset.setBounds(645, 520, 70, 30);

        jButtonDialogWk3Add.setText("Add Activity");
        jButtonDialogWk3Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDialogWk3AddActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonDialogWk3Add);
        jButtonDialogWk3Add.setBounds(535, 520, 100, 30);

        jLabelWk3DialogBudget.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWk3DialogBudget.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWk3DialogBudget.setText("Budget Line");
        jPanel3.add(jLabelWk3DialogBudget);
        jLabelWk3DialogBudget.setBounds(20, 250, 130, 30);

        jTextFieldWk3DialogBudget.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk3DialogBudgetMouseClicked(evt);
            }
        });
        jTextFieldWk3DialogBudget.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk3DialogBudgetKeyTyped(evt);
            }
        });
        jPanel3.add(jTextFieldWk3DialogBudget);
        jTextFieldWk3DialogBudget.setBounds(20, 290, 410, 30);

        jCheckBoxDialogWk3BrkFast.setText(" Breakfast");
        jPanel3.add(jCheckBoxDialogWk3BrkFast);
        jCheckBoxDialogWk3BrkFast.setBounds(550, 300, 71, 21);

        jCheckBoxDialogWk3Lunch.setText("Lunch");
        jPanel3.add(jCheckBoxDialogWk3Lunch);
        jCheckBoxDialogWk3Lunch.setBounds(670, 300, 51, 21);

        jCheckBoxDialogWk3Dinner.setText(" Dinner");
        jPanel3.add(jCheckBoxDialogWk3Dinner);
        jCheckBoxDialogWk3Dinner.setBounds(550, 350, 59, 21);

        jCheckBoxDialogWk3Acc.setText(" Unproved Acc");
        jPanel3.add(jCheckBoxDialogWk3Acc);
        jCheckBoxDialogWk3Acc.setBounds(670, 350, 120, 21);

        jLabelDialogWk3Dis3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDialogWk3Dis3.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDialogWk3Dis3.setText("Km");
        jPanel3.add(jLabelDialogWk3Dis3);
        jLabelDialogWk3Dis3.setBounds(280, 220, 50, 30);

        jLabelWk3DialogActDis.setText("Near Site");
        jPanel3.add(jLabelWk3DialogActDis);
        jLabelWk3DialogActDis.setBounds(210, 220, 70, 30);

        jLabelDialogWk3Dis.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDialogWk3Dis.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDialogWk3Dis.setText("Distance From Base");
        jPanel3.add(jLabelDialogWk3Dis);
        jLabelDialogWk3Dis.setBounds(20, 220, 130, 30);

        jLabelRemain2.setFont(new java.awt.Font("Tahoma", 3, 10)); // NOI18N
        jPanel3.add(jLabelRemain2);
        jLabelRemain2.setBounds(20, 440, 250, 20);

        jCheckBoxDialogWk3Inc.setText("Incidental");
        jPanel3.add(jCheckBoxDialogWk3Inc);
        jCheckBoxDialogWk3Inc.setBounds(550, 400, 100, 21);

        jCheckBoxDialogWk3Misc.setText("Miscellaneous");
        jCheckBoxDialogWk3Misc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDialogWk3MiscActionPerformed(evt);
            }
        });
        jPanel3.add(jCheckBoxDialogWk3Misc);
        jCheckBoxDialogWk3Misc.setBounds(680, 400, 120, 21);

        jLabelWk3Misc.setText("Miscellaneous Desc");
        jPanel3.add(jLabelWk3Misc);
        jLabelWk3Misc.setBounds(560, 430, 160, 30);
        jPanel3.add(jTextFieldWk3Misc);
        jTextFieldWk3Misc.setBounds(560, 460, 110, 30);

        jButtonDialogWk3Close.setText("Close");
        jButtonDialogWk3Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDialogWk3CloseActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonDialogWk3Close);
        jButtonDialogWk3Close.setBounds(725, 520, 70, 30);

        jLabelWk3MiscAmt.setText("$");
        jPanel3.add(jLabelWk3MiscAmt);
        jLabelWk3MiscAmt.setBounds(690, 460, 30, 30);
        jPanel3.add(jTextFieldWk3MiscAmt);
        jTextFieldWk3MiscAmt.setBounds(730, 460, 70, 30);

        jLabelWk3Name1Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk3Name1Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk3Name1DelMouseClicked(evt);
            }
        });
        jPanel3.add(jLabelWk3Name1Del);
        jLabelWk3Name1Del.setBounds(795, 120, 30, 30);

        jLabelWk3Name2Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk3Name2Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk3Name2DelMouseClicked(evt);
            }
        });
        jPanel3.add(jLabelWk3Name2Del);
        jLabelWk3Name2Del.setBounds(795, 160, 30, 30);

        jLabelWk3Name3Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk3Name3Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk3Name3DelMouseClicked(evt);
            }
        });
        jPanel3.add(jLabelWk3Name3Del);
        jLabelWk3Name3Del.setBounds(795, 200, 30, 30);

        jLabelWk3Name4Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk3Name4Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk3Name4DelMouseClicked(evt);
            }
        });
        jPanel3.add(jLabelWk3Name4Del);
        jLabelWk3Name4Del.setBounds(795, 240, 30, 30);

        javax.swing.GroupLayout jDialogWk3Layout = new javax.swing.GroupLayout(jDialogWk3.getContentPane());
        jDialogWk3.getContentPane().setLayout(jDialogWk3Layout);
        jDialogWk3Layout.setHorizontalGroup(
            jDialogWk3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogWk3Layout.setVerticalGroup(
            jDialogWk3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 615, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jDialogWk4.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jDialogWk4.setAlwaysOnTop(true);
        jDialogWk4.setBackground(new java.awt.Color(153, 255, 153));
        jDialogWk4.setLocation(new java.awt.Point(300, 100));
        jDialogWk4.setName("dialogWk1"); // NOI18N
        jDialogWk4.setSize(new java.awt.Dimension(850, 650));

        jPanel4.setBackground(new java.awt.Color(255, 204, 204));
        jPanel4.setPreferredSize(new java.awt.Dimension(850, 650));
        jPanel4.setLayout(null);

        jLabelDialogWk4ActivityDate.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDialogWk4ActivityDate.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDialogWk4ActivityDate.setText("Activity Date");
        jPanel4.add(jLabelDialogWk4ActivityDate);
        jLabelDialogWk4ActivityDate.setBounds(20, 40, 90, 30);

        jDateChooserDialogWk4ActivityDate.setDateFormatString("yyyy-MM-dd");
        jPanel4.add(jDateChooserDialogWk4ActivityDate);
        jDateChooserDialogWk4ActivityDate.setBounds(120, 40, 120, 30);

        jLabelDialogWk4Site.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDialogWk4Site.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDialogWk4Site.setText("Site");
        jPanel4.add(jLabelDialogWk4Site);
        jLabelDialogWk4Site.setBounds(20, 80, 50, 16);

        jTextFieldDialogWk4Site.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldDialogWk4SiteMouseClicked(evt);
            }
        });
        jTextFieldDialogWk4Site.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldDialogWk4SiteActionPerformed(evt);
            }
        });
        jTextFieldDialogWk4Site.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldDialogWk4SiteKeyTyped(evt);
            }
        });
        jPanel4.add(jTextFieldDialogWk4Site);
        jTextFieldDialogWk4Site.setBounds(20, 110, 420, 30);

        jLabelWk4DialogJustification.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWk4DialogJustification.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWk4DialogJustification.setText("Justification for Choice of Activity ");
        jPanel4.add(jLabelWk4DialogJustification);
        jLabelWk4DialogJustification.setBounds(20, 410, 340, 30);

        jLabelDialogWk4SiteClass.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDialogWk4SiteClass.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDialogWk4SiteClass.setText("Site Classification");
        jPanel4.add(jLabelDialogWk4SiteClass);
        jLabelDialogWk4SiteClass.setBounds(20, 160, 130, 30);

        jLabelDialogWk4ActSiteClass.setText("Near Site");
        jPanel4.add(jLabelDialogWk4ActSiteClass);
        jLabelDialogWk4ActSiteClass.setBounds(210, 160, 130, 30);

        jLabelDialogWk4Rank.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDialogWk4Rank.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDialogWk4Rank.setText("Consortium Rank ");
        jPanel4.add(jLabelDialogWk4Rank);
        jLabelDialogWk4Rank.setBounds(20, 190, 130, 30);

        jLabelWk4DialogStaffName4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWk4DialogStaffName4.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWk4DialogStaffName4.setText("4.");
        jPanel4.add(jLabelWk4DialogStaffName4);
        jLabelWk4DialogStaffName4.setBounds(550, 240, 20, 30);
        jPanel4.add(jTextFieldWk4DialogActivityDesc);
        jTextFieldWk4DialogActivityDesc.setBounds(20, 370, 410, 30);

        jLabelWk4DialogActivityDesc.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWk4DialogActivityDesc.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWk4DialogActivityDesc.setText("Activity Descrpition");
        jPanel4.add(jLabelWk4DialogActivityDesc);
        jLabelWk4DialogActivityDesc.setBounds(20, 330, 130, 30);

        jTextAreaWk4DialogJustification.setColumns(20);
        jTextAreaWk4DialogJustification.setRows(5);
        jTextAreaWk4DialogJustification.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextAreaWk4DialogJustificationKeyTyped(evt);
            }
        });
        jScrollPane7.setViewportView(jTextAreaWk4DialogJustification);

        jPanel4.add(jScrollPane7);
        jScrollPane7.setBounds(20, 460, 500, 96);

        jSeparator35.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel4.add(jSeparator35);
        jSeparator35.setBounds(530, 10, 20, 550);

        jLabelWk4DialogActRank.setText("Near Site");
        jPanel4.add(jLabelWk4DialogActRank);
        jLabelWk4DialogActRank.setBounds(210, 190, 130, 30);

        jComboBoxDialogWk4PerDiem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "OPHID", "MOHCC", "OPHID and MOHCC" }));
        jComboBoxDialogWk4PerDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxDialogWk4PerDiemActionPerformed(evt);
            }
        });
        jPanel4.add(jComboBoxDialogWk4PerDiem);
        jComboBoxDialogWk4PerDiem.setBounds(550, 40, 200, 30);

        jLabelDialogWk4PerDiem.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDialogWk4PerDiem.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDialogWk4PerDiem.setText("Require Per Diem");
        jPanel4.add(jLabelDialogWk4PerDiem);
        jLabelDialogWk4PerDiem.setBounds(550, 10, 130, 30);

        jLabelWk4DialogStaff.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWk4DialogStaff.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWk4DialogStaff.setText("OPHID Staff Only");
        jPanel4.add(jLabelWk4DialogStaff);
        jLabelWk4DialogStaff.setBounds(550, 90, 130, 30);

        jLabelWk4DialogStaffName1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWk4DialogStaffName1.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWk4DialogStaffName1.setText("1.");
        jPanel4.add(jLabelWk4DialogStaffName1);
        jLabelWk4DialogStaffName1.setBounds(550, 120, 20, 30);

        jLabelWk4DialogStaffName2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWk4DialogStaffName2.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWk4DialogStaffName2.setText("2.");
        jPanel4.add(jLabelWk4DialogStaffName2);
        jLabelWk4DialogStaffName2.setBounds(550, 160, 20, 30);

        jLabelWk4DialogStaffName3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWk4DialogStaffName3.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWk4DialogStaffName3.setText("3.");
        jPanel4.add(jLabelWk4DialogStaffName3);
        jLabelWk4DialogStaffName3.setBounds(550, 200, 20, 30);

        jTextFieldWk4DialogStaffName4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk4DialogStaffName4MouseClicked(evt);
            }
        });
        jTextFieldWk4DialogStaffName4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldWk4DialogStaffName4ActionPerformed(evt);
            }
        });
        jTextFieldWk4DialogStaffName4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk4DialogStaffName4KeyTyped(evt);
            }
        });
        jPanel4.add(jTextFieldWk4DialogStaffName4);
        jTextFieldWk4DialogStaffName4.setBounds(570, 240, 220, 30);

        jTextFieldWk4DialogStaffName1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk4DialogStaffName1MouseClicked(evt);
            }
        });
        jTextFieldWk4DialogStaffName1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldWk4DialogStaffName1ActionPerformed(evt);
            }
        });
        jTextFieldWk4DialogStaffName1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk4DialogStaffName1KeyTyped(evt);
            }
        });
        jPanel4.add(jTextFieldWk4DialogStaffName1);
        jTextFieldWk4DialogStaffName1.setBounds(570, 120, 220, 30);

        jTextFieldWk4DialogStaffName2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk4DialogStaffName2MouseClicked(evt);
            }
        });
        jTextFieldWk4DialogStaffName2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk4DialogStaffName2KeyTyped(evt);
            }
        });
        jPanel4.add(jTextFieldWk4DialogStaffName2);
        jTextFieldWk4DialogStaffName2.setBounds(570, 160, 220, 30);

        jTextFieldWk4DialogStaffName3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk4DialogStaffName3MouseClicked(evt);
            }
        });
        jTextFieldWk4DialogStaffName3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk4DialogStaffName3KeyTyped(evt);
            }
        });
        jPanel4.add(jTextFieldWk4DialogStaffName3);
        jTextFieldWk4DialogStaffName3.setBounds(570, 200, 220, 30);

        jButtonDialogWk4Reset.setText("Reset");
        jButtonDialogWk4Reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDialogWk4ResetActionPerformed(evt);
            }
        });
        jPanel4.add(jButtonDialogWk4Reset);
        jButtonDialogWk4Reset.setBounds(645, 520, 70, 30);

        jButtonDialogWk4Add.setText("Add Activity");
        jButtonDialogWk4Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDialogWk4AddActionPerformed(evt);
            }
        });
        jPanel4.add(jButtonDialogWk4Add);
        jButtonDialogWk4Add.setBounds(535, 520, 100, 30);

        jLabelWk4DialogBudget.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWk4DialogBudget.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWk4DialogBudget.setText("Budget Line");
        jPanel4.add(jLabelWk4DialogBudget);
        jLabelWk4DialogBudget.setBounds(20, 250, 130, 30);

        jTextFieldWk4DialogBudget.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk4DialogBudgetMouseClicked(evt);
            }
        });
        jTextFieldWk4DialogBudget.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk4DialogBudgetKeyTyped(evt);
            }
        });
        jPanel4.add(jTextFieldWk4DialogBudget);
        jTextFieldWk4DialogBudget.setBounds(20, 290, 410, 30);

        jCheckBoxDialogWk4BrkFast.setText(" Breakfast");
        jPanel4.add(jCheckBoxDialogWk4BrkFast);
        jCheckBoxDialogWk4BrkFast.setBounds(550, 300, 90, 21);

        jCheckBoxDialogWk4Lunch.setText("Lunch");
        jPanel4.add(jCheckBoxDialogWk4Lunch);
        jCheckBoxDialogWk4Lunch.setBounds(670, 300, 51, 21);

        jCheckBoxDialogWk4Dinner.setText(" Dinner");
        jPanel4.add(jCheckBoxDialogWk4Dinner);
        jCheckBoxDialogWk4Dinner.setBounds(550, 350, 59, 21);

        jCheckBoxDialogWk4Acc.setText(" Unproved Acc");
        jPanel4.add(jCheckBoxDialogWk4Acc);
        jCheckBoxDialogWk4Acc.setBounds(670, 350, 120, 21);

        jLabelDialogWk4Dis4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDialogWk4Dis4.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDialogWk4Dis4.setText("Km");
        jPanel4.add(jLabelDialogWk4Dis4);
        jLabelDialogWk4Dis4.setBounds(280, 220, 50, 30);

        jLabelWk4DialogActDis.setText("Near Site");
        jPanel4.add(jLabelWk4DialogActDis);
        jLabelWk4DialogActDis.setBounds(210, 220, 70, 30);

        jLabelDialogWk4Dis.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDialogWk4Dis.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDialogWk4Dis.setText("Distance From Base");
        jPanel4.add(jLabelDialogWk4Dis);
        jLabelDialogWk4Dis.setBounds(20, 220, 130, 30);

        jLabelRemain4.setFont(new java.awt.Font("Tahoma", 3, 10)); // NOI18N
        jPanel4.add(jLabelRemain4);
        jLabelRemain4.setBounds(20, 440, 250, 20);

        jCheckBoxDialogWk4Inc.setText("Incidental");
        jPanel4.add(jCheckBoxDialogWk4Inc);
        jCheckBoxDialogWk4Inc.setBounds(550, 400, 100, 21);

        jCheckBoxDialogWk4Misc.setText("Miscellaneous");
        jCheckBoxDialogWk4Misc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDialogWk4MiscActionPerformed(evt);
            }
        });
        jPanel4.add(jCheckBoxDialogWk4Misc);
        jCheckBoxDialogWk4Misc.setBounds(680, 400, 120, 21);

        jLabelWk4Misc.setText("Miscellaneous Desc");
        jPanel4.add(jLabelWk4Misc);
        jLabelWk4Misc.setBounds(560, 430, 160, 30);
        jPanel4.add(jTextFieldWk4Misc);
        jTextFieldWk4Misc.setBounds(560, 460, 110, 30);

        jButtonDialogWk4Close.setText("Close");
        jButtonDialogWk4Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDialogWk4CloseActionPerformed(evt);
            }
        });
        jPanel4.add(jButtonDialogWk4Close);
        jButtonDialogWk4Close.setBounds(725, 520, 70, 30);

        jLabelWk4MiscAmt.setText("$");
        jPanel4.add(jLabelWk4MiscAmt);
        jLabelWk4MiscAmt.setBounds(690, 460, 30, 30);
        jPanel4.add(jTextFieldWk4MiscAmt);
        jTextFieldWk4MiscAmt.setBounds(730, 460, 70, 30);

        jLabelWk4Name1Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk4Name1Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk4Name1DelMouseClicked(evt);
            }
        });
        jPanel4.add(jLabelWk4Name1Del);
        jLabelWk4Name1Del.setBounds(795, 120, 30, 30);

        jLabelWk4Name2Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk4Name2Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk4Name2DelMouseClicked(evt);
            }
        });
        jPanel4.add(jLabelWk4Name2Del);
        jLabelWk4Name2Del.setBounds(795, 160, 30, 30);

        jLabelWk4Name3Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk4Name3Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk4Name3DelMouseClicked(evt);
            }
        });
        jPanel4.add(jLabelWk4Name3Del);
        jLabelWk4Name3Del.setBounds(795, 200, 30, 30);

        jLabelWk4Name4Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk4Name4Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk4Name4DelMouseClicked(evt);
            }
        });
        jPanel4.add(jLabelWk4Name4Del);
        jLabelWk4Name4Del.setBounds(795, 240, 30, 30);

        javax.swing.GroupLayout jDialogWk4Layout = new javax.swing.GroupLayout(jDialogWk4.getContentPane());
        jDialogWk4.getContentPane().setLayout(jDialogWk4Layout);
        jDialogWk4Layout.setHorizontalGroup(
            jDialogWk4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogWk4Layout.setVerticalGroup(
            jDialogWk4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 615, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        jDialogWk5.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jDialogWk5.setAlwaysOnTop(true);
        jDialogWk5.setBackground(new java.awt.Color(153, 255, 153));
        jDialogWk5.setLocation(new java.awt.Point(300, 100));
        jDialogWk5.setName("dialogWk1"); // NOI18N
        jDialogWk5.setSize(new java.awt.Dimension(850, 650));

        jPanel5.setBackground(new java.awt.Color(204, 204, 255));
        jPanel5.setPreferredSize(new java.awt.Dimension(850, 650));
        jPanel5.setLayout(null);

        jLabelDialogWk5ActivityDate.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDialogWk5ActivityDate.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDialogWk5ActivityDate.setText("Activity Date");
        jPanel5.add(jLabelDialogWk5ActivityDate);
        jLabelDialogWk5ActivityDate.setBounds(20, 40, 90, 30);

        jDateChooserDialogWk5ActivityDate.setDateFormatString("yyyy-MM-dd");
        jPanel5.add(jDateChooserDialogWk5ActivityDate);
        jDateChooserDialogWk5ActivityDate.setBounds(120, 40, 120, 30);

        jLabelDialogWk5Site.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDialogWk5Site.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDialogWk5Site.setText("Site");
        jPanel5.add(jLabelDialogWk5Site);
        jLabelDialogWk5Site.setBounds(20, 80, 50, 16);

        jTextFieldDialogWk5Site.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldDialogWk5SiteMouseClicked(evt);
            }
        });
        jTextFieldDialogWk5Site.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldDialogWk5SiteActionPerformed(evt);
            }
        });
        jTextFieldDialogWk5Site.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldDialogWk5SiteKeyTyped(evt);
            }
        });
        jPanel5.add(jTextFieldDialogWk5Site);
        jTextFieldDialogWk5Site.setBounds(20, 110, 420, 30);

        jLabelWk5DialogJustification.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWk5DialogJustification.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWk5DialogJustification.setText("Justification for Choice of Activity ");
        jPanel5.add(jLabelWk5DialogJustification);
        jLabelWk5DialogJustification.setBounds(20, 410, 340, 30);

        jLabelDialogWk5SiteClass.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDialogWk5SiteClass.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDialogWk5SiteClass.setText("Site Classification");
        jPanel5.add(jLabelDialogWk5SiteClass);
        jLabelDialogWk5SiteClass.setBounds(20, 160, 130, 30);

        jLabelDialogWk5ActSiteClass.setText("Near Site");
        jPanel5.add(jLabelDialogWk5ActSiteClass);
        jLabelDialogWk5ActSiteClass.setBounds(210, 160, 130, 30);

        jLabelDialogWk5Rank.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDialogWk5Rank.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDialogWk5Rank.setText("Consortium Rank ");
        jPanel5.add(jLabelDialogWk5Rank);
        jLabelDialogWk5Rank.setBounds(20, 190, 130, 30);

        jLabelWk5DialogStaffName4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWk5DialogStaffName4.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWk5DialogStaffName4.setText("4.");
        jPanel5.add(jLabelWk5DialogStaffName4);
        jLabelWk5DialogStaffName4.setBounds(550, 240, 20, 30);
        jPanel5.add(jTextFieldWk5DialogActivityDesc);
        jTextFieldWk5DialogActivityDesc.setBounds(20, 370, 410, 30);

        jLabelWk5DialogActivityDesc.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWk5DialogActivityDesc.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWk5DialogActivityDesc.setText("Activity Descrpition");
        jPanel5.add(jLabelWk5DialogActivityDesc);
        jLabelWk5DialogActivityDesc.setBounds(20, 330, 130, 30);

        jTextAreaWk5DialogJustification.setColumns(20);
        jTextAreaWk5DialogJustification.setRows(5);
        jTextAreaWk5DialogJustification.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextAreaWk5DialogJustificationKeyTyped(evt);
            }
        });
        jScrollPane8.setViewportView(jTextAreaWk5DialogJustification);

        jPanel5.add(jScrollPane8);
        jScrollPane8.setBounds(20, 460, 500, 96);

        jSeparator36.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel5.add(jSeparator36);
        jSeparator36.setBounds(530, 10, 20, 550);

        jLabelWk5DialogActRank.setText("Near Site");
        jPanel5.add(jLabelWk5DialogActRank);
        jLabelWk5DialogActRank.setBounds(210, 190, 130, 30);

        jComboBoxDialogWk5PerDiem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "OPHID", "MOHCC", "OPHID and MOHCC" }));
        jComboBoxDialogWk5PerDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxDialogWk5PerDiemActionPerformed(evt);
            }
        });
        jPanel5.add(jComboBoxDialogWk5PerDiem);
        jComboBoxDialogWk5PerDiem.setBounds(550, 40, 200, 30);

        jLabelDialogWk5PerDiem.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDialogWk5PerDiem.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDialogWk5PerDiem.setText("Require Per Diem");
        jPanel5.add(jLabelDialogWk5PerDiem);
        jLabelDialogWk5PerDiem.setBounds(550, 10, 130, 30);

        jLabelWk5DialogStaff.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWk5DialogStaff.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWk5DialogStaff.setText("OPHID Staff Only");
        jPanel5.add(jLabelWk5DialogStaff);
        jLabelWk5DialogStaff.setBounds(550, 90, 130, 30);

        jLabelWk5DialogStaffName1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWk5DialogStaffName1.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWk5DialogStaffName1.setText("1.");
        jPanel5.add(jLabelWk5DialogStaffName1);
        jLabelWk5DialogStaffName1.setBounds(550, 120, 20, 30);

        jLabelWk5DialogStaffName2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWk5DialogStaffName2.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWk5DialogStaffName2.setText("2.");
        jPanel5.add(jLabelWk5DialogStaffName2);
        jLabelWk5DialogStaffName2.setBounds(550, 160, 20, 30);

        jLabelWk5DialogStaffName3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWk5DialogStaffName3.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWk5DialogStaffName3.setText("3.");
        jPanel5.add(jLabelWk5DialogStaffName3);
        jLabelWk5DialogStaffName3.setBounds(550, 200, 20, 30);

        jTextFieldWk5DialogStaffName4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk5DialogStaffName4MouseClicked(evt);
            }
        });
        jTextFieldWk5DialogStaffName4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldWk5DialogStaffName4ActionPerformed(evt);
            }
        });
        jTextFieldWk5DialogStaffName4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk5DialogStaffName4KeyTyped(evt);
            }
        });
        jPanel5.add(jTextFieldWk5DialogStaffName4);
        jTextFieldWk5DialogStaffName4.setBounds(570, 240, 220, 30);

        jTextFieldWk5DialogStaffName1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk5DialogStaffName1MouseClicked(evt);
            }
        });
        jTextFieldWk5DialogStaffName1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldWk5DialogStaffName1ActionPerformed(evt);
            }
        });
        jTextFieldWk5DialogStaffName1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk5DialogStaffName1KeyTyped(evt);
            }
        });
        jPanel5.add(jTextFieldWk5DialogStaffName1);
        jTextFieldWk5DialogStaffName1.setBounds(570, 120, 220, 30);

        jTextFieldWk5DialogStaffName2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk5DialogStaffName2MouseClicked(evt);
            }
        });
        jTextFieldWk5DialogStaffName2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk5DialogStaffName2KeyTyped(evt);
            }
        });
        jPanel5.add(jTextFieldWk5DialogStaffName2);
        jTextFieldWk5DialogStaffName2.setBounds(570, 160, 220, 30);

        jTextFieldWk5DialogStaffName3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk5DialogStaffName3MouseClicked(evt);
            }
        });
        jTextFieldWk5DialogStaffName3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk5DialogStaffName3KeyTyped(evt);
            }
        });
        jPanel5.add(jTextFieldWk5DialogStaffName3);
        jTextFieldWk5DialogStaffName3.setBounds(570, 200, 220, 30);

        jButtonDialogWk5Reset.setText("Reset");
        jButtonDialogWk5Reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDialogWk5ResetActionPerformed(evt);
            }
        });
        jPanel5.add(jButtonDialogWk5Reset);
        jButtonDialogWk5Reset.setBounds(645, 520, 70, 30);

        jButtonDialogWk5Add.setText("Add Activity");
        jButtonDialogWk5Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDialogWk5AddActionPerformed(evt);
            }
        });
        jPanel5.add(jButtonDialogWk5Add);
        jButtonDialogWk5Add.setBounds(535, 520, 100, 30);

        jLabelWk5DialogBudget.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWk5DialogBudget.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWk5DialogBudget.setText("Budget Line");
        jPanel5.add(jLabelWk5DialogBudget);
        jLabelWk5DialogBudget.setBounds(20, 250, 130, 30);

        jTextFieldWk5DialogBudget.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk5DialogBudgetMouseClicked(evt);
            }
        });
        jTextFieldWk5DialogBudget.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk5DialogBudgetKeyTyped(evt);
            }
        });
        jPanel5.add(jTextFieldWk5DialogBudget);
        jTextFieldWk5DialogBudget.setBounds(20, 290, 410, 30);

        jCheckBoxDialogWk5BrkFast.setText(" Breakfast");
        jPanel5.add(jCheckBoxDialogWk5BrkFast);
        jCheckBoxDialogWk5BrkFast.setBounds(550, 300, 90, 21);

        jCheckBoxDialogWk5Lunch.setText("Lunch");
        jPanel5.add(jCheckBoxDialogWk5Lunch);
        jCheckBoxDialogWk5Lunch.setBounds(670, 300, 51, 21);

        jCheckBoxDialogWk5Dinner.setText(" Dinner");
        jPanel5.add(jCheckBoxDialogWk5Dinner);
        jCheckBoxDialogWk5Dinner.setBounds(550, 350, 59, 21);

        jCheckBoxDialogWk5Acc.setText(" Unproved Acc");
        jPanel5.add(jCheckBoxDialogWk5Acc);
        jCheckBoxDialogWk5Acc.setBounds(670, 350, 120, 21);

        jLabelDialogWk5Dis5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDialogWk5Dis5.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDialogWk5Dis5.setText("Km");
        jPanel5.add(jLabelDialogWk5Dis5);
        jLabelDialogWk5Dis5.setBounds(280, 220, 50, 30);

        jLabelWk5DialogActDis.setText("Near Site");
        jPanel5.add(jLabelWk5DialogActDis);
        jLabelWk5DialogActDis.setBounds(210, 220, 70, 30);

        jLabelDialogWk5Dis.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDialogWk5Dis.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDialogWk5Dis.setText("Distance From Base");
        jPanel5.add(jLabelDialogWk5Dis);
        jLabelDialogWk5Dis.setBounds(20, 220, 130, 30);

        jLabelRemain5.setFont(new java.awt.Font("Tahoma", 3, 10)); // NOI18N
        jPanel5.add(jLabelRemain5);
        jLabelRemain5.setBounds(20, 440, 250, 20);

        jCheckBoxDialogWk5Inc.setText("Incidental");
        jPanel5.add(jCheckBoxDialogWk5Inc);
        jCheckBoxDialogWk5Inc.setBounds(550, 400, 100, 21);

        jCheckBoxDialogWk5Misc.setText("Miscellaneous");
        jCheckBoxDialogWk5Misc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDialogWk5MiscActionPerformed(evt);
            }
        });
        jPanel5.add(jCheckBoxDialogWk5Misc);
        jCheckBoxDialogWk5Misc.setBounds(680, 400, 120, 21);

        jLabelWk5Misc.setText("Miscellaneous Desc");
        jPanel5.add(jLabelWk5Misc);
        jLabelWk5Misc.setBounds(560, 430, 160, 30);
        jPanel5.add(jTextFieldWk5Misc);
        jTextFieldWk5Misc.setBounds(560, 460, 110, 30);

        jButtonDialogWk5Close.setText("Close");
        jButtonDialogWk5Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDialogWk5CloseActionPerformed(evt);
            }
        });
        jPanel5.add(jButtonDialogWk5Close);
        jButtonDialogWk5Close.setBounds(725, 520, 70, 30);

        jLabelWk5MiscAmt.setText("$");
        jPanel5.add(jLabelWk5MiscAmt);
        jLabelWk5MiscAmt.setBounds(690, 460, 30, 30);
        jPanel5.add(jTextFieldWk5MiscAmt);
        jTextFieldWk5MiscAmt.setBounds(730, 460, 70, 30);

        jLabelWk5Name1Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk5Name1Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk5Name1DelMouseClicked(evt);
            }
        });
        jPanel5.add(jLabelWk5Name1Del);
        jLabelWk5Name1Del.setBounds(795, 120, 30, 30);

        jLabelWk5Name2Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk5Name2Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk5Name2DelMouseClicked(evt);
            }
        });
        jPanel5.add(jLabelWk5Name2Del);
        jLabelWk5Name2Del.setBounds(795, 160, 30, 30);

        jLabelWk5Name3Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk5Name3Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk5Name3DelMouseClicked(evt);
            }
        });
        jPanel5.add(jLabelWk5Name3Del);
        jLabelWk5Name3Del.setBounds(795, 200, 30, 30);

        jLabelWk5Name4Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk5Name4Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk5Name4DelMouseClicked(evt);
            }
        });
        jPanel5.add(jLabelWk5Name4Del);
        jLabelWk5Name4Del.setBounds(795, 240, 30, 30);

        javax.swing.GroupLayout jDialogWk5Layout = new javax.swing.GroupLayout(jDialogWk5.getContentPane());
        jDialogWk5.getContentPane().setLayout(jDialogWk5Layout);
        jDialogWk5Layout.setHorizontalGroup(
            jDialogWk5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogWk5Layout.setVerticalGroup(
            jDialogWk5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 615, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanelWkOne.setBackground(new java.awt.Color(255, 255, 204));
        jPanelWkOne.setLayout(null);

        jLabelWkDuration.setText("Week Duration");
        jPanelWkOne.add(jLabelWkDuration);
        jLabelWkDuration.setBounds(30, 220, 90, 30);

        jDateChooserWk1From.setDateFormatString("yyyy-MM-dd");
        jPanelWkOne.add(jDateChooserWk1From);
        jDateChooserWk1From.setBounds(210, 220, 120, 30);

        jLabelWk1From.setText("From");
        jPanelWkOne.add(jLabelWk1From);
        jLabelWk1From.setBounds(150, 220, 41, 30);

        jLabelWk1To.setText("To");
        jPanelWkOne.add(jLabelWk1To);
        jLabelWk1To.setBounds(390, 220, 41, 30);

        jDateChooserWk1To.setDateFormatString("yyyy-MM-dd");
        jPanelWkOne.add(jDateChooserWk1To);
        jDateChooserWk1To.setBounds(440, 220, 120, 30);

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
        jButtonWk1AddActivity.setBounds(820, 220, 150, 30);
        jPanelWkOne.add(jSeparator1);
        jSeparator1.setBounds(30, 255, 1280, 10);
        jPanelWkOne.add(jSeparator2);
        jSeparator2.setBounds(30, 210, 1280, 10);

        jTableWk1Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Site", "Site Classification", "Consortium Rank", "Distance", "Activity", "Justification for Choice of Activity ", "Perdiem Required", "Budget Line", "B", "L", "D", "I", "Misc Desc", "M", "A", "OPHID Staff 1", "OPHID Staff 2", "OPHID Staff 3", "OPHID Staff 4"
            }
        ));
        jScrollPane1.setViewportView(jTableWk1Activities);

        jPanelWkOne.add(jScrollPane1);
        jScrollPane1.setBounds(30, 270, 1290, 400);

        jLabelLogo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ophidlog.jpg"))); // NOI18N
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
        jButtonWk1DelActivity.setBounds(1030, 220, 150, 30);

        jLabelProvince1.setText("Province");
        jLabelProvince1.setMinimumSize(new java.awt.Dimension(130, 130));
        jLabelProvince1.setName(""); // NOI18N
        jPanelWkOne.add(jLabelProvince1);
        jLabelProvince1.setBounds(30, 160, 70, 30);

        jLabelOffice1.setText("District");
        jLabelOffice1.setMinimumSize(new java.awt.Dimension(130, 130));
        jLabelOffice1.setName(""); // NOI18N
        jPanelWkOne.add(jLabelOffice1);
        jLabelOffice1.setBounds(530, 160, 70, 30);

        jLabelRef.setText("Ref. No.");
        jPanelWkOne.add(jLabelRef);
        jLabelRef.setBounds(1090, 100, 60, 30);

        jLabelSerial.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelSerial.setText("P");
        jPanelWkOne.add(jLabelSerial);
        jLabelSerial.setBounds(1170, 100, 30, 30);

        jLabelRefNum.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jPanelWkOne.add(jLabelRefNum);
        jLabelRefNum.setBounds(1215, 100, 50, 30);

        jLabelHeaderGen4.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen4.setText("MONTHLY  PLAN ");
        jPanelWkOne.add(jLabelHeaderGen4);
        jLabelHeaderGen4.setBounds(450, 40, 420, 40);
        jPanelWkOne.add(jLabelEmp);
        jLabelEmp.setBounds(1180, 66, 70, 30);

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
        jPanelWkOne.add(jLabelProvince);
        jLabelProvince.setBounds(120, 160, 370, 30);
        jPanelWkOne.add(jLabelDistrict);
        jLabelDistrict.setBounds(670, 160, 360, 30);

        jTabbedPane1.addTab("Week One", jPanelWkOne);

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
        jPanelWkTwo.add(jSeparator4);
        jSeparator4.setBounds(30, 200, 1280, 10);
        jPanelWkTwo.add(jSeparator5);
        jSeparator5.setBounds(30, 150, 1280, 10);

        jTableWk2Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Site", "Site Classification", "Consortium Rank", "Distance", "Activity", "Justification for Choice of Activity ", "Perdiem Required", "Budget Line", "B", "L", "D", "I", "Misc Desc", "M", "A", "OPHID Staff 1", "OPHID Staff 2", "OPHID Staff 3", "OPHID Staff 4"
            }
        ));
        jScrollPane3.setViewportView(jTableWk2Activities);

        jPanelWkTwo.add(jScrollPane3);
        jScrollPane3.setBounds(30, 220, 1290, 400);

        jLabelLogo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ophidlog.jpg"))); // NOI18N
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

        jLabelGenLogNam3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam3.setText("Logged In");
        jPanelWkTwo.add(jLabelGenLogNam3);
        jLabelGenLogNam3.setBounds(1090, 30, 100, 30);

        jLabelLineDate1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkTwo.add(jLabelLineDate1);
        jLabelLineDate1.setBounds(1090, 0, 110, 30);

        jLabelLineTime1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkTwo.add(jLabelLineTime1);
        jLabelLineTime1.setBounds(1250, 0, 80, 30);

        jLabelLineLogNam1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam1.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkTwo.add(jLabelLineLogNam1);
        jLabelLineLogNam1.setBounds(1180, 30, 160, 30);

        jLabelHeaderGen7.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen7.setText("MONTHLY  PLAN ");
        jPanelWkTwo.add(jLabelHeaderGen7);
        jLabelHeaderGen7.setBounds(450, 40, 420, 40);

        jTabbedPane1.addTab("Week Two", jPanelWkTwo);

        jPanelWkThree.setBackground(new java.awt.Color(29, 109, 222));
        jPanelWkThree.setLayout(null);

        jLabelWkDuration2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWkDuration2.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWkDuration2.setText("Week Duration");
        jPanelWkThree.add(jLabelWkDuration2);
        jLabelWkDuration2.setBounds(30, 160, 100, 30);

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
        jButtonWk3AddActivity.setBounds(820, 160, 150, 30);
        jPanelWkThree.add(jSeparator7);
        jSeparator7.setBounds(30, 200, 1280, 10);
        jPanelWkThree.add(jSeparator8);
        jSeparator8.setBounds(30, 150, 1280, 10);

        jTableWk3Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Site", "Site Classification", "Consortium Rank", "Distance", "Activity", "Justification for Choice of Activity ", "Perdiem Required", "Budget Line", "B", "L", "D", "I", "Misc Desc", "M", "A", "OPHID Staff 1", "OPHID Staff 2", "OPHID Staff 3", "OPHID Staff 4"
            }
        ));
        jScrollPane5.setViewportView(jTableWk3Activities);

        jPanelWkThree.add(jScrollPane5);
        jScrollPane5.setBounds(30, 220, 1290, 402);

        jLabelLogo3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ophidlog.jpg"))); // NOI18N
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

        jLabelGenLogNam5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam5.setForeground(new java.awt.Color(255, 255, 255));
        jLabelGenLogNam5.setText("Logged In");
        jPanelWkThree.add(jLabelGenLogNam5);
        jLabelGenLogNam5.setBounds(1090, 30, 100, 30);

        jLabelLineLogNam2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkThree.add(jLabelLineLogNam2);
        jLabelLineLogNam2.setBounds(1180, 30, 160, 30);

        jLabelLineTime2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkThree.add(jLabelLineTime2);
        jLabelLineTime2.setBounds(1250, 0, 80, 30);

        jLabelLineDate2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkThree.add(jLabelLineDate2);
        jLabelLineDate2.setBounds(1090, 0, 110, 30);

        jLabelHeaderGen8.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen8.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeaderGen8.setText("MONTHLY  PLAN ");
        jPanelWkThree.add(jLabelHeaderGen8);
        jLabelHeaderGen8.setBounds(450, 40, 420, 40);

        jTabbedPane1.addTab("Week Three", jPanelWkThree);

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
        jMenuNew.add(jSeparator10);

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

        jMenuItem3WkPlan.setText("Monthly Plan");
        jMenuItem3WkPlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3WkPlanActionPerformed(evt);
            }
        });
        jMenuNew.add(jMenuItem3WkPlan);

        jMenuFile.add(jMenuNew);
        jMenuFile.add(jSeparator11);

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
        jMenuFile.add(jSeparator12);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Submit ");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItem1);
        jMenuFile.add(jSeparator33);

        jMenuItemClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemClose.setText("Close");
        jMenuItemClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemClose);
        jMenuFile.add(jSeparator13);

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
        jMenuRequest.add(jSeparator15);

        jMenuItemAccMgrRev.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemAccMgrRev.setText("Account Manager Verification");
        jMenuItemAccMgrRev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAccMgrRevActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemAccMgrRev);
        jMenuRequest.add(jSeparator16);

        jMenuItemHeadApp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemHeadApp.setText("Head Approval");
        jMenuItemHeadApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemHeadAppActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemHeadApp);
        jMenuRequest.add(jSeparator17);

        jMenuPlanApproval.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuPlanApproval.setText("Central Finance Payment ");
        jMenuPlanApproval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuPlanApprovalActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuPlanApproval);
        jMenuRequest.add(jSeparator18);

        jMenuItemSchGen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSchGen.setText("Schedule Generation");
        jMenuItemSchGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSchGenActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemSchGen);
        jMenuRequest.add(jSeparator20);

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
        jMenuAcquittal.add(jSeparator21);

        jMenuItemAcqAccApp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        jMenuItemAcqAccApp.setText("Account Manager Verification");
        jMenuItemAcqAccApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqAccAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqAccApp);
        jMenuAcquittal.add(jSeparator22);

        jMenuItemAcqHeadApp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        jMenuItemAcqHeadApp.setText("Head Approval");
        jMenuItemAcqHeadApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqHeadAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqHeadApp);
        jMenuAcquittal.add(jSeparator23);

        jMenuItemAcqFinApp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        jMenuItemAcqFinApp.setText("Central Finance Payment ");
        jMenuItemAcqFinApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqFinAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqFinApp);
        jMenuAcquittal.add(jSeparator24);

        jMenuItemAcqSchGen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        jMenuItemAcqSchGen.setText("Acquittals Schedule Generation");
        jMenuItemAcqSchGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqSchGenActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqSchGen);
        jMenuAcquittal.add(jSeparator25);

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

        jMenuItemProvMgrApproval.setText("Supervisor Approval");
        jMenuItemProvMgrApproval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemProvMgrApprovalActionPerformed(evt);
            }
        });
        jMenuMonthlyPlan.add(jMenuItemProvMgrApproval);
        jMenuMonthlyPlan.add(jSeparator26);

        jMenuItemReqGenLst.setText("Request Generation list");
        jMenuItemReqGenLst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemReqGenLstActionPerformed(evt);
            }
        });
        jMenuMonthlyPlan.add(jMenuItemReqGenLst);
        jMenuMonthlyPlan.add(jSeparator34);

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
        jMenuReports.add(jSeparator27);

        jMenuAgeAlluser.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuAgeAlluser.setText("Age Analysis All Users");
        jMenuAgeAlluser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuAgeAlluserActionPerformed(evt);
            }
        });
        jMenuReports.add(jMenuAgeAlluser);
        jMenuReports.add(jSeparator31);

        jMenuAgeByUser.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuAgeByUser.setText("Age Analysis by User");
        jMenuAgeByUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuAgeByUserActionPerformed(evt);
            }
        });
        jMenuReports.add(jMenuAgeByUser);
        jMenuReports.add(jSeparator32);

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
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        setSize(new java.awt.Dimension(1376, 760));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonOkFacilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkFacilityActionPerformed
        try {
            if (((jComboDistrictFacility.getSelectedItem().toString().equals(jComboFacility.getSelectedItem().toString()))
                    || ("Central Office".equals(jLabelProvince.getText())) || ("Harare Province".equals(jLabelProvince.getText()))) && ("".equals(jTextDistDest.getText()))) {
                JOptionPane.showMessageDialog(jDialogFacility, "Please enter distance to " + jComboFacility.getSelectedItem().toString());
                jTextDistDest.requestFocusInWindow();
                jTextDistDest.setFocusable(true);
            } else if ("Y".equals(wk1Site)) {
                jTextFieldDialogWk1Site.setText(jComboFacility.getSelectedItem().toString());
                findRankCat();
                jDialogFacility.setVisible(false);
            } else if ("Y".equals(wk2Site)) {
                jTextFieldDialogWk2Site.setText(jComboFacility.getSelectedItem().toString());
                findRankCat();
                jDialogFacility.setVisible(false);
            } else if ("Y".equals(wk3Site)) {
                jTextFieldDialogWk3Site.setText(jComboFacility.getSelectedItem().toString());
                findRankCat();
                jDialogFacility.setVisible(false);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_jButtonOkFacilityActionPerformed

    private void jButtonCancelFacilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelFacilityActionPerformed
        jDialogFacility.setVisible(false);
    }//GEN-LAST:event_jButtonCancelFacilityActionPerformed

    private void jComboProvinceFacilityMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboProvinceFacilityMouseEntered
        //        jComboDistrict.removeAllItems();
        //        jComboFacility.removeAllItems();
    }//GEN-LAST:event_jComboProvinceFacilityMouseEntered

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

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

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

    private void jComboFacilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboFacilityActionPerformed
        facDist();
    }//GEN-LAST:event_jComboFacilityActionPerformed

    private void jButtonCancelBudgetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelBudgetActionPerformed
        jDialogBudget.setVisible(false);
    }//GEN-LAST:event_jButtonCancelBudgetActionPerformed

    private void jButtonOkFacility1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkFacility1ActionPerformed
        if ("Y".equals(wk1Site)) {
            jTextFieldWk1DialogBudget.setText(jComboBudgetCode.getSelectedItem().toString());
        } else if ("Y".equals(wk2Site)) {
            jTextFieldWk2DialogBudget.setText(jComboBudgetCode.getSelectedItem().toString());
        } else if ("Y".equals(wk3Site)) {
            jTextFieldWk3DialogBudget.setText(jComboBudgetCode.getSelectedItem().toString());
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

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

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
        if ("Y".equals(wk1Site)) {
            jTextFieldWk1DialogStaffName1.setText(str);
            jLabelWk1Name1Del.setVisible(true);
        } else if ("Y".equals(wk2Site)) {
            jTextFieldWk2DialogStaffName1.setText(str);
            jLabelWk2Name1Del.setVisible(true);
        } else if ("Y".equals(wk3Site)) {
            jTextFieldWk3DialogStaffName1.setText(str);
            jLabelWk3Name1Del.setVisible(true);
        }
        jTextFieldSearchNam1.setText("");
        jDialogSearchName1.dispose();
    }//GEN-LAST:event_jButtonOk1ActionPerformed

    private void jTextFieldSearchNam2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldSearchNam2FocusGained
       
    }//GEN-LAST:event_jTextFieldSearchNam2FocusGained

    private void jTextFieldSearchNam2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldSearchNam2FocusLost
       
    }//GEN-LAST:event_jTextFieldSearchNam2FocusLost

    private void jTextFieldSearchNam2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldSearchNam2ActionPerformed
       
    }//GEN-LAST:event_jTextFieldSearchNam2ActionPerformed

    private void jButtonSearch2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearch2ActionPerformed
        searchName2();
    }//GEN-LAST:event_jButtonSearch2ActionPerformed

    private void jButtonOk2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOk2ActionPerformed
        String str = jComboBoxSearchResult2.getSelectedItem().toString();
        if ("Y".equals(wk1Site)) {
            jTextFieldWk1DialogStaffName2.setText(str);
            jLabelWk1Name2Del.setVisible(true);

        } else if ("Y".equals(wk2Site)) {
            jTextFieldWk2DialogStaffName2.setText(str);
            jLabelWk2Name2Del.setVisible(true);
        } else if ("Y".equals(wk3Site)) {
            jTextFieldWk3DialogStaffName2.setText(str);
            jLabelWk3Name2Del.setVisible(true);
        }
        jTextFieldSearchNam2.setText("");
        jDialogSearchName2.dispose();
    }//GEN-LAST:event_jButtonOk2ActionPerformed

    private void jTextFieldSearchNam3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldSearchNam3FocusGained
       
    }//GEN-LAST:event_jTextFieldSearchNam3FocusGained

    private void jTextFieldSearchNam3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldSearchNam3FocusLost
       
    }//GEN-LAST:event_jTextFieldSearchNam3FocusLost

    private void jTextFieldSearchNam3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldSearchNam3ActionPerformed
       
    }//GEN-LAST:event_jTextFieldSearchNam3ActionPerformed

    private void jButtonSearch3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearch3ActionPerformed
        searchName3();
    }//GEN-LAST:event_jButtonSearch3ActionPerformed

    private void jButtonOk3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOk3ActionPerformed
        String str = jComboBoxSearchResult3.getSelectedItem().toString();
        if ("Y".equals(wk1Site)) {
            jTextFieldWk1DialogStaffName3.setText(str);
            jLabelWk1Name3Del.setVisible(true);
        } else if ("Y".equals(wk2Site)) {
            jTextFieldWk2DialogStaffName3.setText(str);
            jLabelWk2Name3Del.setVisible(true);
        } else if ("Y".equals(wk3Site)) {
            jTextFieldWk3DialogStaffName3.setText(str);
            jLabelWk3Name3Del.setVisible(true);
        }
        jTextFieldSearchNam3.setText("");
        jDialogSearchName3.dispose();
    }//GEN-LAST:event_jButtonOk3ActionPerformed

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

    private void jButtonWk1AddActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk1AddActivityActionPerformed
        try {
            java.util.Date actPlanDate = jDateChooserWk1From.getDate();
            java.util.Date todayDate = new Date();

            LocalDate dateBefore = LocalDate.parse(formatter.format(jDateChooserWk1From.getDate()));
            LocalDate dateAfter = LocalDate.parse(formatter.format(jDateChooserWk1To.getDate()));

            //calculating number of days in between
            long noOfDaysBetweenWk1 = ChronoUnit.DAYS.between(dateBefore, dateAfter) + 1;

            if (formatter.format(jDateChooserWk1From.getDate()).compareTo(formatter.format(todayDate)) < 0) {
                JOptionPane.showMessageDialog(this, "Activity date cannot be lower than today's date. Please check your dates");
                jDateChooserWk1From.requestFocusInWindow();
                jDateChooserWk1From.setFocusable(true);
            } else if (jDateChooserWk1From.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
                jDateChooserWk1From.requestFocusInWindow();
                jDateChooserWk1From.setFocusable(true);
            } else if (jDateChooserWk1To.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
                jDateChooserWk1To.requestFocusInWindow();
                jDateChooserWk1To.setFocusable(true);
            } else if (jDateChooserWk1From.getDate().after(jDateChooserWk1To.getDate())) {
                JOptionPane.showMessageDialog(this, "End Date cannot be lower than start date.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk1From.setDate(null);
                jDateChooserWk1To.setDate(null);
                jDateChooserWk1From.requestFocusInWindow();
                jDateChooserWk1From.setFocusable(true);
            } else if (noOfDaysBetweenWk1 > 7) {
                JOptionPane.showMessageDialog(this, "Weekly Plan days cannot be more than seven(7) days",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk1From.setDate(null);
                jDateChooserWk1To.setDate(null);
                jDateChooserWk1From.requestFocusInWindow();
                jDateChooserWk1From.setFocusable(true);
            } else {
                wk1Site = "Y";
                resetField();
                jDialogWk1.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonWk1AddActivityActionPerformed

    private void jButtonWk2AddActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk2AddActivityActionPerformed
        try {
            java.util.Date actPlanDate = jDateChooserWk2From.getDate();
            java.util.Date todayDate = new Date();

            LocalDate dateBefore = LocalDate.parse(formatter.format(jDateChooserWk2From.getDate()));
            LocalDate dateAfter = LocalDate.parse(formatter.format(jDateChooserWk2To.getDate()));

            //calculating number of days in between
            long noOfDaysBetweenWk2 = ChronoUnit.DAYS.between(dateBefore, dateAfter) + 1;

            if (formatter.format(jDateChooserWk2From.getDate()).compareTo(formatter.format(todayDate)) < 0) {
                JOptionPane.showMessageDialog(this, "Activity date cannot be lower than today's date. Please check your dates");
                jDateChooserWk2From.requestFocusInWindow();
                jDateChooserWk2From.setFocusable(true);
            } else if ((jDateChooserWk1From.getDate() == null) || (jDateChooserWk1To.getDate() == null)) {
                JOptionPane.showMessageDialog(this, "Please complete week one before completing week two");
                jDateChooserWk2From.setDate(null);
                jDateChooserWk2To.setDate(null);
                jDateChooserWk1From.requestFocusInWindow();
                jDateChooserWk1From.setFocusable(true);
            } else if (jDateChooserWk2From.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
                jDateChooserWk2From.requestFocusInWindow();
                jDateChooserWk2From.setFocusable(true);
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
            } else if (noOfDaysBetweenWk2 > 7) {
                JOptionPane.showMessageDialog(this, "Weekly Plan days cannot be more than seven(7) days",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk2From.setDate(null);
                jDateChooserWk2To.setDate(null);
                jDateChooserWk2From.requestFocusInWindow();
                jDateChooserWk2From.setFocusable(true);
            } else {
                wk2Site = "Y";
                resetField();
                jDialogWk2.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonWk2AddActivityActionPerformed

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

    private void jTextFieldSearchNam4FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldSearchNam4FocusGained
       
    }//GEN-LAST:event_jTextFieldSearchNam4FocusGained

    private void jTextFieldSearchNam4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldSearchNam4FocusLost
       
    }//GEN-LAST:event_jTextFieldSearchNam4FocusLost

    private void jTextFieldSearchNam4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldSearchNam4ActionPerformed
       
    }//GEN-LAST:event_jTextFieldSearchNam4ActionPerformed

    private void jButtonSearch4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearch4ActionPerformed
        searchName4();
    }//GEN-LAST:event_jButtonSearch4ActionPerformed

    private void jButtonOk4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOk4ActionPerformed

        String str = jComboBoxSearchResult4.getSelectedItem().toString();
        if ("Y".equals(wk1Site)) {
            jTextFieldWk1DialogStaffName4.setText(str);
            jLabelWk1Name4Del.setVisible(true);
        } else if ("Y".equals(wk2Site)) {
            jTextFieldWk2DialogStaffName4.setText(str);
            jLabelWk2Name4Del.setVisible(true);
        } else if ("Y".equals(wk3Site)) {
            jTextFieldWk3DialogStaffName4.setText(str);
            jLabelWk3Name4Del.setVisible(true);
        }
        jTextFieldSearchNam4.setText("");
        jDialogSearchName4.dispose();
    }//GEN-LAST:event_jButtonOk4ActionPerformed

    private void jButtonWk3AddActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk3AddActivityActionPerformed
        try {
            java.util.Date actPlanDate = jDateChooserWk3From.getDate();
            java.util.Date todayDate = new Date();

            LocalDate dateBefore = LocalDate.parse(formatter.format(jDateChooserWk3From.getDate()));
            LocalDate dateAfter = LocalDate.parse(formatter.format(jDateChooserWk3To.getDate()));

            //calculating number of days in between
            long noOfDaysBetweenWk3 = ChronoUnit.DAYS.between(dateBefore, dateAfter) + 1;

            if (formatter.format(jDateChooserWk3From.getDate()).compareTo(formatter.format(todayDate)) < 0) {
                JOptionPane.showMessageDialog(this, "Activity date cannot be lower than today's date. Please check your dates");
                jDateChooserWk3From.requestFocusInWindow();
                jDateChooserWk3From.setFocusable(true);
            } else if ((jDateChooserWk2From.getDate() == null) || (jDateChooserWk2To.getDate() == null) || (jDateChooserWk1From.getDate() == null) || (jDateChooserWk1To.getDate() == null)) {
                JOptionPane.showMessageDialog(this, "Please complete week two before completing week three");
                jDateChooserWk3From.setDate(null);
                jDateChooserWk3To.setDate(null);
                jDateChooserWk1From.requestFocusInWindow();
                jDateChooserWk1From.setFocusable(true);
            } else if (jDateChooserWk3From.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
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
            } else if (jDateChooserWk1To.getDate().after(jDateChooserWk3From.getDate())) {
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
                wk3Site = "Y";
                resetField();
                jDialogWk3.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonWk3AddActivityActionPerformed

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

    private void jMenuItemRequestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRequestActionPerformed
        new JFrameAppRequest(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemRequestActionPerformed

    private void jMenuItemAcquittalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAcquittalActionPerformed
        new JFrameAppAcquittal(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemAcquittalActionPerformed

    private void jMenuItemSpecialAcquittalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSpecialAcquittalActionPerformed
        new JFrameAcqSpecial(jLabelEmp.getText()).setVisible(true);
        setVisible(false);

    }//GEN-LAST:event_jMenuItemSpecialAcquittalActionPerformed

    private void jMenuItemRequestMOHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRequestMOHActionPerformed
        new JFrameAcqSpecialMOH(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemRequestMOHActionPerformed

    private void jMenuItem3WkPlanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3WkPlanActionPerformed
        new JFrameMnthPlan(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItem3WkPlanActionPerformed

    private void jMenuItemCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCloseActionPerformed
        new JFrameFinSysLogin().setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemCloseActionPerformed

    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItemExitActionPerformed

    private void jMenuFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuFileActionPerformed
       
    }//GEN-LAST:event_jMenuFileActionPerformed

    private void jMenuItemSupAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSupAppActionPerformed
        new JFrameSupAppList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemSupAppActionPerformed

    private void jMenuItemAccMgrRevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAccMgrRevActionPerformed
        new JFrameAccMgrAppList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemAccMgrRevActionPerformed

    private void jMenuItemHeadAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemHeadAppActionPerformed
        new JFrameReqHeadAppList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemHeadAppActionPerformed

    private void jMenuPlanApprovalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuPlanApprovalActionPerformed
        new JFrameHQForPayAppList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuPlanApprovalActionPerformed

    private void jMenuItemSchGenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSchGenActionPerformed
        new JFrameReqHQPayScheduleApp(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemSchGenActionPerformed

    private void jMenuItemViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemViewActionPerformed
        new JFrameReqViewApp(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemViewActionPerformed

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
        new JFrameFinAcqList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
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
        new JFrameReqAllAgeAnalysis(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuAgeAlluserActionPerformed

    private void jMenuAgeByUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuAgeByUserActionPerformed
        new JFrameReqUserAgeAnalysis(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuAgeByUserActionPerformed

    private void jMenuProcessTimelineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuProcessTimelineActionPerformed
        new JFrameReqProcessingTimeline(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuProcessTimelineActionPerformed

    private void jMenuReqEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuReqEditActionPerformed
        new JFrameReqEditApp(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuReqEditActionPerformed

    private void jMenuAcqEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuAcqEditActionPerformed
        new JFrameAppEditAcquittal(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuAcqEditActionPerformed

    private void jMenuMonPlanEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuMonPlanEditActionPerformed
        new JFrameMnthPlanEdit(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuMonPlanEditActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        SerialCheck();
        RefNumAllocation();
        createWk1Plan();
        createWk2Plan();
        createWk3Plan();
        createWkPlanPeriod();
        createWkPlanAction();
        createWkPlanCreator();
        UsrRecUpd();
        refNumUpdate();

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jComboFacilityMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboFacilityMouseReleased

    }//GEN-LAST:event_jComboFacilityMouseReleased

    private void jComboProvinceFacilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboProvinceFacilityActionPerformed
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

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

        if ("MOHCC".equals(jComboBoxDialogWk1PerDiem.getSelectedItem().toString())) {
            jTextFieldWk1DialogStaffName1.setText("");
            jTextFieldWk1DialogStaffName2.setText("");
            jTextFieldWk1DialogStaffName3.setText("");
            jTextFieldWk1DialogStaffName4.setText("");
            jCheckBoxDialogWk1BrkFast.setSelected(false);
            jCheckBoxDialogWk1Lunch.setSelected(false);
            jCheckBoxDialogWk1Dinner.setSelected(false);
            jCheckBoxDialogWk1Acc.setSelected(false);
            jCheckBoxDialogWk1Inc.setSelected(false);
            jTextFieldWk1DialogStaffName1.setVisible(false);
            jTextFieldWk1DialogStaffName2.setVisible(false);
            jTextFieldWk1DialogStaffName3.setVisible(false);
            jTextFieldWk1DialogStaffName4.setVisible(false);
            jCheckBoxDialogWk1BrkFast.setEnabled(false);
            jCheckBoxDialogWk1Lunch.setEnabled(false);
            jCheckBoxDialogWk1Dinner.setEnabled(false);
            jCheckBoxDialogWk1Acc.setEnabled(false);
            jCheckBoxDialogWk1Inc.setEnabled(false);
            jCheckBoxDialogWk1Misc.setEnabled(false);
        } else {
            jTextFieldWk1DialogStaffName1.setVisible(true);
            jTextFieldWk1DialogStaffName2.setVisible(true);
            jTextFieldWk1DialogStaffName3.setVisible(true);
            jTextFieldWk1DialogStaffName4.setVisible(true);
            jCheckBoxDialogWk1BrkFast.setEnabled(true);
            jCheckBoxDialogWk1Lunch.setEnabled(true);
            jCheckBoxDialogWk1Dinner.setEnabled(true);
            jCheckBoxDialogWk1Acc.setEnabled(true);
            jCheckBoxDialogWk1Inc.setEnabled(true);
            jCheckBoxDialogWk1Misc.setEnabled(true);
            Wk1DistLess100();
        }
    }//GEN-LAST:event_jComboBoxDialogWk1PerDiemActionPerformed

    private void jTextFieldWk1DialogStaffName4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName4MouseClicked
        jDialogSearchName4.setVisible(true);
        jDialogSearchName4.setVisible(false);
        jDialogSearchName4.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk1DialogStaffName4MouseClicked

    private void jTextFieldWk1DialogStaffName4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName4ActionPerformed
       
    }//GEN-LAST:event_jTextFieldWk1DialogStaffName4ActionPerformed

    private void jTextFieldWk1DialogStaffName4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName4KeyTyped
        jDialogSearchName4.setVisible(true);
        jDialogSearchName4.setVisible(false);
        jDialogSearchName4.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk1DialogStaffName4KeyTyped

    private void jTextFieldWk1DialogStaffName1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName1MouseClicked
        jDialogSearchName1.setVisible(true);
        jDialogSearchName1.setVisible(false);
        jDialogSearchName1.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk1DialogStaffName1MouseClicked

    private void jTextFieldWk1DialogStaffName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName1ActionPerformed

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName1ActionPerformed

    private void jTextFieldWk1DialogStaffName1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName1KeyTyped
        jDialogSearchName1.setVisible(true);
        jDialogSearchName1.setVisible(false);
        jDialogSearchName1.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk1DialogStaffName1KeyTyped

    private void jTextFieldWk1DialogStaffName2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName2MouseClicked
        jDialogSearchName2.setVisible(true);
        jDialogSearchName2.setVisible(false);
        jDialogSearchName2.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk1DialogStaffName2MouseClicked

    private void jTextFieldWk1DialogStaffName2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName2KeyTyped
        jDialogSearchName2.setVisible(true);
        jDialogSearchName2.setVisible(false);
        jDialogSearchName2.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk1DialogStaffName2KeyTyped

    private void jTextFieldWk1DialogStaffName3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName3MouseClicked
        jDialogSearchName3.setVisible(true);
        jDialogSearchName3.setVisible(false);
        jDialogSearchName3.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk1DialogStaffName3MouseClicked

    private void jTextFieldWk1DialogStaffName3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName3KeyTyped
        jDialogSearchName3.setVisible(true);
        jDialogSearchName3.setVisible(false);
        jDialogSearchName3.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk1DialogStaffName3KeyTyped

    private void jButtonDialogWk1ResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDialogWk1ResetActionPerformed
        resetField();
    }//GEN-LAST:event_jButtonDialogWk1ResetActionPerformed

    private void jButtonDialogWk1AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDialogWk1AddActionPerformed
        DefaultTableModel model = (DefaultTableModel) jTableWk1Activities.getModel();
        String Wk1Brk = "0.00";
        String Wk1Lnch = "0.00";
        String Wk1Dinner = "0.00";
        String Wk1Acc = "0.00";
        String Wk1Inc = "0.00";
        String Wk1Misc = "0.00";
        String Wk1MiscDesc = "";

        if (!(jTextFieldWk1DialogStaffName1.getText().length() == 0)) {
            duplicateUser1Count = null;
            planWk = "PlanWk1Tab";
            planDate = formatter.format(jDateChooserDialogWk1ActivityDate.getDate());
            empNamNum1 = "EMP_NAM1";
            empNam1 = jTextFieldWk1DialogStaffName1.getText();
            empNamNum2 = "EMP_NAM2";
            empNam2 = jTextFieldWk1DialogStaffName2.getText();
            empNamNum3 = "EMP_NAM3";
            empNam3 = jTextFieldWk1DialogStaffName3.getText();
            empNamNum4 = "EMP_NAM4";
            empNam4 = jTextFieldWk1DialogStaffName4.getText();

            CheckDuplicateUser1();

        }

        if (!(jTextFieldWk1DialogStaffName2.getText().length() == 0)) {
            duplicateUser2Count = null;
            planWk = "PlanWk1Tab";
            planDate = formatter.format(jDateChooserDialogWk1ActivityDate.getDate());
            empNamNum1 = "EMP_NAM1";
            empNam1 = jTextFieldWk1DialogStaffName1.getText();
            empNamNum2 = "EMP_NAM2";
            empNam2 = jTextFieldWk1DialogStaffName2.getText();
            empNamNum3 = "EMP_NAM3";
            empNam3 = jTextFieldWk1DialogStaffName3.getText();
            empNamNum4 = "EMP_NAM4";
            empNam4 = jTextFieldWk1DialogStaffName4.getText();

            CheckDuplicateUser2();

        }

        if (!(jTextFieldWk1DialogStaffName3.getText().length() == 0)) {
            duplicateUser3Count = null;
            planWk = "PlanWk1Tab";
            planDate = formatter.format(jDateChooserDialogWk1ActivityDate.getDate());
            empNamNum1 = "EMP_NAM1";
            empNam1 = jTextFieldWk1DialogStaffName1.getText();
            empNamNum2 = "EMP_NAM2";
            empNam2 = jTextFieldWk1DialogStaffName2.getText();
            empNamNum3 = "EMP_NAM3";
            empNam3 = jTextFieldWk1DialogStaffName3.getText();
            empNamNum4 = "EMP_NAM4";
            empNam4 = jTextFieldWk1DialogStaffName4.getText();

            CheckDuplicateUser3();
        }

        if (!(jTextFieldWk1DialogStaffName4.getText().length() == 0)) {
            duplicateUser4Count = null;
            planWk = "PlanWk1Tab";
            planDate = formatter.format(jDateChooserDialogWk1ActivityDate.getDate());
            empNamNum1 = "EMP_NAM1";
            empNam1 = jTextFieldWk1DialogStaffName1.getText();
            empNamNum2 = "EMP_NAM2";
            empNam2 = jTextFieldWk1DialogStaffName2.getText();
            empNamNum3 = "EMP_NAM3";
            empNam3 = jTextFieldWk1DialogStaffName3.getText();
            empNamNum4 = "EMP_NAM4";
            empNam4 = jTextFieldWk1DialogStaffName4.getText();

            CheckDuplicateUser4();
        }

//        if (!(duplicateUser1Count == null) || !(duplicateUser2Count == null) || !(duplicateUser3Count == null) || !(duplicateUser4Count == null)) {
//             JOptionPane.showMessageDialog(jDialogWk1, "Another actvity exist for user specified",
//                    "Error", JOptionPane.ERROR_MESSAGE);
//            jTextFieldWk1DialogStaffName1.requestFocusInWindow();
//            jTextFieldWk1DialogStaffName1.setFocusable(true);
//            //resetField();
//            //    duplicateUserCount=null;
//        } else {
        try {
            if (jCheckBoxDialogWk1BrkFast.isSelected()) {
                Wk1Brk = breakfastAll;
            } else {
                Wk1Brk = "0.00";
            }

            if ((jCheckBoxDialogWk1Lunch.isSelected()) && (Double.parseDouble(jLabelWk1DialogActDis.getText())) > 99) {
                Wk1Lnch = lunchAll;
            } else if ((jCheckBoxDialogWk1Lunch.isSelected()) && (Double.parseDouble(jLabelWk1DialogActDis.getText())) < 100) {
                Wk1Lnch = lunchNPAll;
            } else {
                Wk1Lnch = "0.00";
            }

            if (jCheckBoxDialogWk1Dinner.isSelected()) {
                Wk1Dinner = dinnerAll;
            } else {
                Wk1Dinner = "0.00";
            }

            if (jCheckBoxDialogWk1Acc.isSelected()) {
                Wk1Acc = unProvedAll;
            } else {
                Wk1Acc = "0.00";
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
                JOptionPane.showMessageDialog(jDialogWk1, "Date cannot ddddbe blank. Please check your dates");
                jDateChooserDialogWk1ActivityDate.requestFocusInWindow();
                jDateChooserDialogWk1ActivityDate.setFocusable(true);
            } else if ((formatter.format(jDateChooserDialogWk1ActivityDate.getDate()).compareTo(formatter.format(jDateChooserWk1From.getDate())) < 0)
                    || (formatter.format(jDateChooserDialogWk1ActivityDate.getDate()).compareTo(formatter.format(jDateChooserWk1To.getDate())) > 0)) {
                JOptionPane.showMessageDialog(jDialogWk1, "Activity date cannot be outside the specified week date range.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserDialogWk1ActivityDate.setDate(null);
                jDateChooserDialogWk1ActivityDate.requestFocusInWindow();
                jDateChooserDialogWk1ActivityDate.setFocusable(true);
            } else if ("".equals(jTextFieldDialogWk1Site.getText())) {
                JOptionPane.showMessageDialog(jDialogWk1, "Site cannot be blank. Please check and correct");
                jTextFieldDialogWk1Site.requestFocusInWindow();
                jTextFieldDialogWk1Site.setFocusable(true);
            } else if ("".equals(jTextFieldWk1DialogBudget.getText())) {
                JOptionPane.showMessageDialog(jDialogWk1, "Budget line cannot be blank. Please check and correct");
                jTextFieldWk1DialogBudget.requestFocusInWindow();
                jTextFieldWk1DialogBudget.setFocusable(true);
            } else if ("".equals(jTextFieldWk1DialogActivityDesc.getText())) {
                JOptionPane.showMessageDialog(jDialogWk1, "Activity description cannot be blank. Please check and correct");
                jTextFieldWk1DialogActivityDesc.requestFocusInWindow();
                jTextFieldWk1DialogActivityDesc.setFocusable(true);
            } else if ("".equals(jTextAreaWk1DialogJustification.getText())) {
                JOptionPane.showMessageDialog(jDialogWk1, "Activity justification cannot be blank. Please check and correct");
                jTextAreaWk1DialogJustification.requestFocusInWindow();
                jTextAreaWk1DialogJustification.setFocusable(true);
            } else if ((jCheckBoxDialogWk1BrkFast.isSelected() || jCheckBoxDialogWk1Lunch.isSelected()
                    || jCheckBoxDialogWk1Dinner.isSelected() || jCheckBoxDialogWk1Acc.isSelected()
                    || jCheckBoxDialogWk1Inc.isSelected() || jCheckBoxDialogWk1Misc.isSelected())
                    && ("".equals(jTextFieldWk1DialogStaffName1.getText()))) {
                JOptionPane.showMessageDialog(jDialogWk1, "You have select an allowance.Please enter at least one staff name");
                jTextFieldWk1DialogStaffName1.requestFocusInWindow();
                jTextFieldWk1DialogStaffName1.setFocusable(true);

            } else {
                model.addRow(new Object[]{formatter.format(jDateChooserDialogWk1ActivityDate.getDate()), jTextFieldDialogWk1Site.getText(), jLabelDialogWk1ActSiteClass.getText(),
                    jLabelWk1DialogActRank.getText(), jLabelWk1DialogActDis.getText(), jTextFieldWk1DialogActivityDesc.getText(),
                    jTextAreaWk1DialogJustification.getText(), jComboBoxDialogWk1PerDiem.getSelectedItem().toString(),
                    jTextFieldWk1DialogBudget.getText(), Wk1Brk, Wk1Lnch, Wk1Dinner, Wk1Inc, Wk1MiscDesc, Wk1Misc, Wk1Acc, jTextFieldWk1DialogStaffName1.getText(),
                    jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});
                addItem();
                resetField();

            }

        } catch (Exception e) {
            System.out.println(e);
        }
        //}
    }//GEN-LAST:event_jButtonDialogWk1AddActionPerformed

    private void jTextFieldWk1DialogBudgetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogBudgetMouseClicked
        budgetPOP();
    }//GEN-LAST:event_jTextFieldWk1DialogBudgetMouseClicked

    private void jTextFieldWk1DialogBudgetKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogBudgetKeyTyped
        budgetPOP();
    }//GEN-LAST:event_jTextFieldWk1DialogBudgetKeyTyped

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

    private void jButtonDialogWk1CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDialogWk1CloseActionPerformed
        wk1Site = "N";
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

    private void jTextFieldDialogWk2SiteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk2SiteMouseClicked
        facilityPOP();
    }//GEN-LAST:event_jTextFieldDialogWk2SiteMouseClicked

    private void jTextFieldDialogWk2SiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk2SiteActionPerformed
       
    }//GEN-LAST:event_jTextFieldDialogWk2SiteActionPerformed

    private void jTextFieldDialogWk2SiteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk2SiteKeyTyped
        facilityPOP();
    }//GEN-LAST:event_jTextFieldDialogWk2SiteKeyTyped

    private void jTextAreaWk2DialogJustificationKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAreaWk2DialogJustificationKeyTyped
        jTextAreaWk2DialogJustification.setLineWrap(true);
        jTextAreaWk2DialogJustification.setWrapStyleWord(true);

        String charsRemaining = " characters remaining";
        int newLenWk2 = 0;

        int currLenWk2 = jTextAreaWk2DialogJustification.getText().length();

        if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            newLenWk2 = currLenWk2 - 1;
            ignoreInput = false;
        } else {
            newLenWk2 = currLenWk2 + 1;
        }

        if (newLenWk2 < 0) {
            newLenWk2 = 0;
        }

        if (newLenWk2 == 0) {
            jLabelRemain1.setText(charMaxWk2 + " characters maximum!");
        } else if (newLenWk2 >= 0 && newLenWk2 < charMaxWk2) {
            jLabelRemain1.setText((charMaxWk2 - newLenWk2) + charsRemaining);
        } else if (newLenWk2 >= charMaxWk2) {
            try {
                ignoreInput = true;
                jLabelRemain1.setText("0 " + charsRemaining);
                JOptionPane.showMessageDialog(jDialogWk2, "Maximum allowed characters reached.",
                        "Word Count Warning", JOptionPane.WARNING_MESSAGE);
                jTextAreaWk2DialogJustification.requestFocusInWindow();
                jTextAreaWk2DialogJustification.setFocusable(true);
            } catch (Exception e) {
                System.out.println(e);
            }

        }
        if ((charMaxWk2 - newLenWk2) > 15) {
            jLabelRemain1.setForeground(new java.awt.Color(0, 102, 0));
        } else {
            jLabelRemain1.setForeground(new java.awt.Color(255, 51, 51));
        }
    }//GEN-LAST:event_jTextAreaWk2DialogJustificationKeyTyped

    private void jComboBoxDialogWk2PerDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxDialogWk2PerDiemActionPerformed
        if ("MOHCC".equals(jComboBoxDialogWk2PerDiem.getSelectedItem().toString())) {
            jTextFieldWk2DialogStaffName1.setText("");
            jTextFieldWk2DialogStaffName2.setText("");
            jTextFieldWk2DialogStaffName3.setText("");
            jTextFieldWk2DialogStaffName4.setText("");
            jCheckBoxDialogWk2BrkFast.setSelected(false);
            jCheckBoxDialogWk2Lunch.setSelected(false);
            jCheckBoxDialogWk2Dinner.setSelected(false);
            jCheckBoxDialogWk2Acc.setSelected(false);
            jCheckBoxDialogWk2Inc.setSelected(false);
            jTextFieldWk2DialogStaffName1.setVisible(false);
            jTextFieldWk2DialogStaffName2.setVisible(false);
            jTextFieldWk2DialogStaffName3.setVisible(false);
            jTextFieldWk2DialogStaffName4.setVisible(false);
            jCheckBoxDialogWk2BrkFast.setEnabled(false);
            jCheckBoxDialogWk2Lunch.setEnabled(false);
            jCheckBoxDialogWk2Dinner.setEnabled(false);
            jCheckBoxDialogWk2Acc.setEnabled(false);
            jCheckBoxDialogWk2Inc.setEnabled(false);
        } else {
            jTextFieldWk2DialogStaffName1.setVisible(true);
            jTextFieldWk2DialogStaffName2.setVisible(true);
            jTextFieldWk2DialogStaffName3.setVisible(true);
            jTextFieldWk2DialogStaffName4.setVisible(true);
            jCheckBoxDialogWk2BrkFast.setEnabled(true);
            jCheckBoxDialogWk2Lunch.setEnabled(true);
            jCheckBoxDialogWk2Dinner.setEnabled(true);
            jCheckBoxDialogWk2Acc.setEnabled(true);
            jCheckBoxDialogWk2Inc.setEnabled(true);
            Wk2DistLess100();
        }
    }//GEN-LAST:event_jComboBoxDialogWk2PerDiemActionPerformed

    private void jTextFieldWk2DialogStaffName4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogStaffName4MouseClicked
        jDialogSearchName4.setVisible(true);
        jDialogSearchName4.setVisible(false);
        jDialogSearchName4.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk2DialogStaffName4MouseClicked

    private void jTextFieldWk2DialogStaffName4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogStaffName4ActionPerformed
       
    }//GEN-LAST:event_jTextFieldWk2DialogStaffName4ActionPerformed

    private void jTextFieldWk2DialogStaffName4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogStaffName4KeyTyped
        jDialogSearchName4.setVisible(true);
        jDialogSearchName4.setVisible(false);
        jDialogSearchName4.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk2DialogStaffName4KeyTyped

    private void jTextFieldWk2DialogStaffName1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogStaffName1MouseClicked
        jDialogSearchName1.setVisible(true);
        jDialogSearchName1.setVisible(false);
        jDialogSearchName1.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk2DialogStaffName1MouseClicked

    private void jTextFieldWk2DialogStaffName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogStaffName1ActionPerformed
       
    }//GEN-LAST:event_jTextFieldWk2DialogStaffName1ActionPerformed

    private void jTextFieldWk2DialogStaffName1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogStaffName1KeyTyped
        jDialogSearchName1.setVisible(true);
        jDialogSearchName1.setVisible(false);
        jDialogSearchName1.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk2DialogStaffName1KeyTyped

    private void jTextFieldWk2DialogStaffName2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogStaffName2MouseClicked
        jDialogSearchName2.setVisible(true);
        jDialogSearchName2.setVisible(false);
        jDialogSearchName2.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk2DialogStaffName2MouseClicked

    private void jTextFieldWk2DialogStaffName2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogStaffName2KeyTyped
        jDialogSearchName2.setVisible(true);
        jDialogSearchName2.setVisible(false);
        jDialogSearchName2.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk2DialogStaffName2KeyTyped

    private void jTextFieldWk2DialogStaffName3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogStaffName3MouseClicked
        jDialogSearchName3.setVisible(true);
        jDialogSearchName3.setVisible(false);
        jDialogSearchName3.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk2DialogStaffName3MouseClicked

    private void jTextFieldWk2DialogStaffName3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogStaffName3KeyTyped
        jDialogSearchName3.setVisible(true);
        jDialogSearchName3.setVisible(false);
        jDialogSearchName3.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk2DialogStaffName3KeyTyped

    private void jButtonDialogWk2ResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDialogWk2ResetActionPerformed
        resetField();
    }//GEN-LAST:event_jButtonDialogWk2ResetActionPerformed

    private void jButtonDialogWk2AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDialogWk2AddActionPerformed
        DefaultTableModel modelWk2 = (DefaultTableModel) jTableWk2Activities.getModel();

        String Wk2Brk = "0.00";
        String Wk2Lnch = "0.00";
        String Wk2Dinner = "0.00";
        String Wk2Acc = "0.00";
        String Wk2Inc = "0.00";
        String Wk2Misc = "0.00";
        String Wk2MiscDesc = "";

        try {
            if (jCheckBoxDialogWk2BrkFast.isSelected()) {
                Wk2Brk = breakfastAll;
            } else {
                Wk2Brk = "0.00";
            }

            if ((jCheckBoxDialogWk2Lunch.isSelected()) && (Double.parseDouble(jLabelWk2DialogActDis.getText())) > 99) {
                Wk2Lnch = lunchAll;
            } else if ((jCheckBoxDialogWk2Lunch.isSelected()) && (Double.parseDouble(jLabelWk2DialogActDis.getText())) < 100) {
                Wk2Lnch = lunchNPAll;
            } else {
                Wk2Lnch = "0.00";
            }

            if (jCheckBoxDialogWk2Dinner.isSelected()) {
                Wk2Dinner = dinnerAll;
            } else {
                Wk2Dinner = "0.00";
            }

            if (jCheckBoxDialogWk2Acc.isSelected()) {
                Wk2Acc = unProvedAll;
            } else {
                Wk2Acc = "0.00";
            }

            if (jCheckBoxDialogWk2Inc.isSelected()) {
                Wk2Inc = incidentalAll;
            } else {
                Wk2Inc = "0.00";
            }

            if (jCheckBoxDialogWk2Misc.isSelected()) {
                Wk2MiscDesc = jTextFieldWk2Misc.getText();
                Wk2Misc = jTextFieldWk2MiscAmt.getText();
            } else {
                Wk2Misc = "0.00";
            }

            if (jDateChooserDialogWk2ActivityDate.getDate() == null) {
                JOptionPane.showMessageDialog(jDialogWk2, "Date cannot ddddbe blank. Please check your dates");
                jDateChooserDialogWk2ActivityDate.requestFocusInWindow();
                jDateChooserDialogWk2ActivityDate.setFocusable(true);
            } else if ((formatter.format(jDateChooserDialogWk2ActivityDate.getDate()).compareTo(formatter.format(jDateChooserWk2From.getDate())) < 0)
                    || (formatter.format(jDateChooserDialogWk2ActivityDate.getDate()).compareTo(formatter.format(jDateChooserWk2To.getDate())) > 0)) {
                JOptionPane.showMessageDialog(jDialogWk2, "Activity date cannot be outside the specified week date range.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserDialogWk2ActivityDate.setDate(null);
                jDateChooserDialogWk2ActivityDate.requestFocusInWindow();
                jDateChooserDialogWk2ActivityDate.setFocusable(true);
            } else if ("".equals(jTextFieldDialogWk2Site.getText())) {
                JOptionPane.showMessageDialog(jDialogWk2, "Site cannot be blank. Please check and correct");
                jTextFieldDialogWk2Site.requestFocusInWindow();
                jTextFieldDialogWk2Site.setFocusable(true);
            } else if ("".equals(jTextFieldWk2DialogBudget.getText())) {
                JOptionPane.showMessageDialog(jDialogWk2, "Budget line cannot be blank. Please check and correct");
                jTextFieldWk2DialogBudget.requestFocusInWindow();
                jTextFieldWk2DialogBudget.setFocusable(true);
            } else if ("".equals(jTextFieldWk2DialogBudget.getText())) {
                JOptionPane.showMessageDialog(jDialogWk2, "Budget line cannot be blank. Please check and correct");
                jTextFieldWk2DialogBudget.requestFocusInWindow();
                jTextFieldWk2DialogBudget.setFocusable(true);
            } else if ("".equals(jTextFieldWk2DialogActivityDesc.getText())) {
                JOptionPane.showMessageDialog(jDialogWk2, "Activity description cannot be blank. Please check and correct");
                jTextFieldWk2DialogActivityDesc.requestFocusInWindow();
                jTextFieldWk2DialogActivityDesc.setFocusable(true);
            } else if ((jCheckBoxDialogWk2BrkFast.isSelected() || jCheckBoxDialogWk2Lunch.isSelected()
                    || jCheckBoxDialogWk2Dinner.isSelected() || jCheckBoxDialogWk2Acc.isSelected()
                    || jCheckBoxDialogWk2Inc.isSelected() || jCheckBoxDialogWk2Misc.isSelected())
                    && ("".equals(jTextFieldWk2DialogStaffName1.getText()))) {
                JOptionPane.showMessageDialog(jDialogWk2, "You have select an allowance.Please enter at least one staff name");
                jTextFieldWk2DialogStaffName1.requestFocusInWindow();
                jTextFieldWk2DialogStaffName1.setFocusable(true);

            } else {
                modelWk2.addRow(new Object[]{formatter.format(jDateChooserDialogWk2ActivityDate.getDate()), jTextFieldDialogWk2Site.getText(), jLabelDialogWk2ActSiteClass.getText(),
                    jLabelWk2DialogActRank.getText(), jLabelWk2DialogActDis.getText(), jTextFieldWk2DialogActivityDesc.getText(),
                    jTextAreaWk2DialogJustification.getText(), jComboBoxDialogWk2PerDiem.getSelectedItem().toString(),
                    jTextFieldWk2DialogBudget.getText(), Wk2Brk, Wk2Lnch, Wk2Dinner, Wk2Inc, Wk2MiscDesc, Wk2Misc, Wk2Acc, jTextFieldWk2DialogStaffName1.getText(),
                    jTextFieldWk2DialogStaffName2.getText(), jTextFieldWk2DialogStaffName3.getText(), jTextFieldWk2DialogStaffName4.getText()});
                addItem();
                resetField();

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_jButtonDialogWk2AddActionPerformed

    private void jTextFieldWk2DialogBudgetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogBudgetMouseClicked
        budgetPOP();
    }//GEN-LAST:event_jTextFieldWk2DialogBudgetMouseClicked

    private void jTextFieldWk2DialogBudgetKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogBudgetKeyTyped
        budgetPOP();
    }//GEN-LAST:event_jTextFieldWk2DialogBudgetKeyTyped

    private void jCheckBoxDialogWk2MiscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxDialogWk2MiscActionPerformed
        if (jCheckBoxDialogWk2Misc.isSelected()) {
            jTextFieldWk2Misc.setVisible(true);
            jLabelWk2Misc.setVisible(true);
            jLabelWk2MiscAmt.setVisible(true);
            jTextFieldWk2MiscAmt.setVisible(true);
        } else {
            jTextFieldWk2Misc.setVisible(false);
            jLabelWk2Misc.setVisible(false);
            jTextFieldWk2Misc.setText("");
            jLabelWk2MiscAmt.setVisible(false);
            jTextFieldWk2MiscAmt.setVisible(false);
        }
    }//GEN-LAST:event_jCheckBoxDialogWk2MiscActionPerformed

    private void jButtonDialogWk2CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDialogWk2CloseActionPerformed
        wk2Site = "N";
        jDialogWk2.setVisible(false);
    }//GEN-LAST:event_jButtonDialogWk2CloseActionPerformed

    private void jLabelWk2Name1DelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelWk2Name1DelMouseClicked
        jTextFieldWk2DialogStaffName1.setText("");
        jLabelWk2Name1Del.setVisible(false);
    }//GEN-LAST:event_jLabelWk2Name1DelMouseClicked

    private void jLabelWk2Name2DelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelWk2Name2DelMouseClicked
        jTextFieldWk2DialogStaffName2.setText("");
        jLabelWk2Name2Del.setVisible(false);
    }//GEN-LAST:event_jLabelWk2Name2DelMouseClicked

    private void jLabelWk2Name3DelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelWk2Name3DelMouseClicked
        jTextFieldWk2DialogStaffName3.setText("");
        jLabelWk2Name3Del.setVisible(false);
    }//GEN-LAST:event_jLabelWk2Name3DelMouseClicked

    private void jLabelWk2Name4DelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelWk2Name4DelMouseClicked
        jTextFieldWk2DialogStaffName4.setText("");
        jLabelWk2Name4Del.setVisible(false);
    }//GEN-LAST:event_jLabelWk2Name4DelMouseClicked

    private void jTextFieldDialogWk3SiteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk3SiteMouseClicked
        facilityPOP();
    }//GEN-LAST:event_jTextFieldDialogWk3SiteMouseClicked

    private void jTextFieldDialogWk3SiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk3SiteActionPerformed
       
    }//GEN-LAST:event_jTextFieldDialogWk3SiteActionPerformed

    private void jTextFieldDialogWk3SiteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk3SiteKeyTyped
        facilityPOP();
    }//GEN-LAST:event_jTextFieldDialogWk3SiteKeyTyped

    private void jTextAreaWk3DialogJustificationKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAreaWk3DialogJustificationKeyTyped
        jTextAreaWk3DialogJustification.setLineWrap(true);
        jTextAreaWk3DialogJustification.setWrapStyleWord(true);

        String charsRemaining = " characters remaining";
        int newLenWk3 = 0;

        int currLenWk3 = jTextAreaWk3DialogJustification.getText().length();

        if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            newLenWk3 = currLenWk3 - 1;
            ignoreInput = false;
        } else {
            newLenWk3 = currLenWk3 + 1;
        }

        if (newLenWk3 < 0) {
            newLenWk3 = 0;
        }

        if (newLenWk3 == 0) {
            jLabelRemain2.setText(charMaxWk3 + " characters maximum!");
        } else if (newLenWk3 >= 0 && newLenWk3 < charMaxWk3) {
            jLabelRemain2.setText((charMaxWk3 - newLenWk3) + charsRemaining);
        } else if (newLenWk3 >= charMaxWk3) {
            try {
                ignoreInput = true;
                jLabelRemain2.setText("0 " + charsRemaining);
                JOptionPane.showMessageDialog(jDialogWk3, "Maximum allowed characters reached.",
                        "Word Count Warning", JOptionPane.WARNING_MESSAGE);
                jTextAreaWk3DialogJustification.requestFocusInWindow();
                jTextAreaWk3DialogJustification.setFocusable(true);
            } catch (Exception e) {
                System.out.println(e);
            }

        }
        if ((charMaxWk3 - newLenWk3) > 15) {
            jLabelRemain2.setForeground(new java.awt.Color(0, 102, 0));
        } else {
            jLabelRemain2.setForeground(new java.awt.Color(255, 51, 51));
        }
    }//GEN-LAST:event_jTextAreaWk3DialogJustificationKeyTyped

    private void jComboBoxDialogWk3PerDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxDialogWk3PerDiemActionPerformed
        if ("MOHCC".equals(jComboBoxDialogWk3PerDiem.getSelectedItem().toString())) {
            jTextFieldWk3DialogStaffName1.setText("");
            jTextFieldWk3DialogStaffName2.setText("");
            jTextFieldWk3DialogStaffName3.setText("");
            jTextFieldWk3DialogStaffName4.setText("");
            jCheckBoxDialogWk3BrkFast.setSelected(false);
            jCheckBoxDialogWk3Lunch.setSelected(false);
            jCheckBoxDialogWk3Dinner.setSelected(false);
            jCheckBoxDialogWk3Acc.setSelected(false);
            jCheckBoxDialogWk3Inc.setSelected(false);
            jTextFieldWk3DialogStaffName1.setVisible(false);
            jTextFieldWk3DialogStaffName2.setVisible(false);
            jTextFieldWk3DialogStaffName3.setVisible(false);
            jTextFieldWk3DialogStaffName4.setVisible(false);
            jCheckBoxDialogWk3BrkFast.setEnabled(false);
            jCheckBoxDialogWk3Lunch.setEnabled(false);
            jCheckBoxDialogWk3Dinner.setEnabled(false);
            jCheckBoxDialogWk3Acc.setEnabled(false);
            jCheckBoxDialogWk3Inc.setEnabled(false);
        } else {
            jTextFieldWk3DialogStaffName1.setVisible(true);
            jTextFieldWk3DialogStaffName2.setVisible(true);
            jTextFieldWk3DialogStaffName3.setVisible(true);
            jTextFieldWk3DialogStaffName4.setVisible(true);
            jCheckBoxDialogWk3BrkFast.setEnabled(true);
            jCheckBoxDialogWk3Lunch.setEnabled(true);
            jCheckBoxDialogWk3Dinner.setEnabled(true);
            jCheckBoxDialogWk3Acc.setEnabled(true);
            jCheckBoxDialogWk3Inc.setEnabled(true);
            Wk3DistLess100();
        }
    }//GEN-LAST:event_jComboBoxDialogWk3PerDiemActionPerformed

    private void jTextFieldWk3DialogStaffName4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogStaffName4MouseClicked
        jDialogSearchName4.setVisible(true);
        jDialogSearchName4.setVisible(false);
        jDialogSearchName4.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk3DialogStaffName4MouseClicked

    private void jTextFieldWk3DialogStaffName4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogStaffName4ActionPerformed
        jDialogSearchName4.setVisible(true);
        jDialogSearchName4.setVisible(false);
        jDialogSearchName4.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk3DialogStaffName4ActionPerformed

    private void jTextFieldWk3DialogStaffName4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogStaffName4KeyTyped
       
    }//GEN-LAST:event_jTextFieldWk3DialogStaffName4KeyTyped

    private void jTextFieldWk3DialogStaffName1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogStaffName1MouseClicked
        jDialogSearchName1.setVisible(true);
        jDialogSearchName1.setVisible(false);
        jDialogSearchName1.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk3DialogStaffName1MouseClicked

    private void jTextFieldWk3DialogStaffName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogStaffName1ActionPerformed
       
    }//GEN-LAST:event_jTextFieldWk3DialogStaffName1ActionPerformed

    private void jTextFieldWk3DialogStaffName1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogStaffName1KeyTyped
        jDialogSearchName1.setVisible(true);
        jDialogSearchName1.setVisible(false);
        jDialogSearchName1.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk3DialogStaffName1KeyTyped

    private void jTextFieldWk3DialogStaffName2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogStaffName2MouseClicked
        jDialogSearchName2.setVisible(true);
        jDialogSearchName2.setVisible(false);
        jDialogSearchName2.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk3DialogStaffName2MouseClicked

    private void jTextFieldWk3DialogStaffName2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogStaffName2KeyTyped
        jDialogSearchName2.setVisible(true);
        jDialogSearchName2.setVisible(false);
        jDialogSearchName2.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk3DialogStaffName2KeyTyped

    private void jTextFieldWk3DialogStaffName3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogStaffName3MouseClicked
        jDialogSearchName3.setVisible(true);
        jDialogSearchName3.setVisible(false);
        jDialogSearchName3.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk3DialogStaffName3MouseClicked

    private void jTextFieldWk3DialogStaffName3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogStaffName3KeyTyped
        jDialogSearchName3.setVisible(true);
        jDialogSearchName3.setVisible(false);
        jDialogSearchName3.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk3DialogStaffName3KeyTyped

    private void jButtonDialogWk3ResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDialogWk3ResetActionPerformed
        resetField();
    }//GEN-LAST:event_jButtonDialogWk3ResetActionPerformed

    private void jButtonDialogWk3AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDialogWk3AddActionPerformed
        DefaultTableModel modelWk3 = (DefaultTableModel) jTableWk3Activities.getModel();
        String Wk3Brk = "0.00";
        String Wk3Lnch = "0.00";
        String Wk3Dinner = "0.00";
        String Wk3Acc = "0.00";
        String Wk3Inc = "0.00";
        String Wk3Misc = "0.00";
        String Wk3MiscDesc = "";

        try {
            if (jCheckBoxDialogWk3BrkFast.isSelected()) {
                Wk3Brk = breakfastAll;
            } else {
                Wk3Brk = "0.00";
            }

            if ((jCheckBoxDialogWk3Lunch.isSelected()) && (Double.parseDouble(jLabelWk3DialogActDis.getText())) > 99) {
                Wk3Lnch = lunchAll;
            } else if ((jCheckBoxDialogWk3Lunch.isSelected()) && (Double.parseDouble(jLabelWk3DialogActDis.getText())) < 100) {
                Wk3Lnch = lunchNPAll;
            } else {
                Wk3Lnch = "0.00";
            }

            if (jCheckBoxDialogWk3Dinner.isSelected()) {
                Wk3Dinner = dinnerAll;
            } else {
                Wk3Dinner = "0.00";
            }

            if (jCheckBoxDialogWk3Acc.isSelected()) {
                Wk3Acc = unProvedAll;
            } else {
                Wk3Acc = "0.00";
            }

            if (jCheckBoxDialogWk3Inc.isSelected()) {
                Wk3Inc = incidentalAll;
            } else {
                Wk3Inc = "0.00";
            }

            if (jCheckBoxDialogWk3Misc.isSelected()) {
                Wk3MiscDesc = jTextFieldWk3Misc.getText();
                Wk3Misc = jTextFieldWk3MiscAmt.getText();
            } else {
                Wk3Misc = "0.00";
            }

            if (jDateChooserDialogWk3ActivityDate.getDate() == null) {
                JOptionPane.showMessageDialog(jDialogWk3, "Date cannot ddddbe blank. Please check your dates");
                jDateChooserDialogWk3ActivityDate.requestFocusInWindow();
                jDateChooserDialogWk3ActivityDate.setFocusable(true);
            } else if ((formatter.format(jDateChooserDialogWk3ActivityDate.getDate()).compareTo(formatter.format(jDateChooserWk3From.getDate())) < 0)
                    || (formatter.format(jDateChooserDialogWk3ActivityDate.getDate()).compareTo(formatter.format(jDateChooserWk3To.getDate())) > 0)) {
                JOptionPane.showMessageDialog(jDialogWk3, "Activity date cannot be outside the specified week date range.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserDialogWk3ActivityDate.setDate(null);
                jDateChooserDialogWk3ActivityDate.requestFocusInWindow();
                jDateChooserDialogWk3ActivityDate.setFocusable(true);
            } else if ("".equals(jTextFieldDialogWk3Site.getText())) {
                JOptionPane.showMessageDialog(jDialogWk3, "Site cannot be blank. Please check and correct");
                jTextFieldDialogWk3Site.requestFocusInWindow();
                jTextFieldDialogWk3Site.setFocusable(true);
            } else if ("".equals(jTextFieldWk3DialogBudget.getText())) {
                JOptionPane.showMessageDialog(jDialogWk3, "Budget line cannot be blank. Please check and correct");
                jTextFieldWk3DialogBudget.requestFocusInWindow();
                jTextFieldWk3DialogBudget.setFocusable(true);
            } else if ("".equals(jTextFieldWk3DialogBudget.getText())) {
                JOptionPane.showMessageDialog(jDialogWk3, "Budget line cannot be blank. Please check and correct");
                jTextFieldWk3DialogBudget.requestFocusInWindow();
                jTextFieldWk3DialogBudget.setFocusable(true);
            } else if ("".equals(jTextFieldWk3DialogActivityDesc.getText())) {
                JOptionPane.showMessageDialog(jDialogWk3, "Activity description cannot be blank. Please check and correct");
                jTextFieldWk3DialogActivityDesc.requestFocusInWindow();
                jTextFieldWk3DialogActivityDesc.setFocusable(true);
            } else if ((jCheckBoxDialogWk3BrkFast.isSelected() || jCheckBoxDialogWk3Lunch.isSelected()
                    || jCheckBoxDialogWk3Dinner.isSelected() || jCheckBoxDialogWk3Acc.isSelected()
                    || jCheckBoxDialogWk3Inc.isSelected() || jCheckBoxDialogWk3Misc.isSelected())
                    && ("".equals(jTextFieldWk3DialogStaffName1.getText()))) {
                JOptionPane.showMessageDialog(jDialogWk3, "You have select an allowance.Please enter at least one staff name");
                jTextFieldWk3DialogStaffName1.requestFocusInWindow();
                jTextFieldWk3DialogStaffName1.setFocusable(true);

            } else {
                modelWk3.addRow(new Object[]{formatter.format(jDateChooserDialogWk3ActivityDate.getDate()), jTextFieldDialogWk3Site.getText(), jLabelDialogWk3ActSiteClass.getText(),
                    jLabelWk3DialogActRank.getText(), jLabelWk3DialogActDis.getText(), jTextFieldWk3DialogActivityDesc.getText(),
                    jTextAreaWk3DialogJustification.getText(), jComboBoxDialogWk3PerDiem.getSelectedItem().toString(),
                    jTextFieldWk3DialogBudget.getText(), Wk3Brk, Wk3Lnch, Wk3Dinner, Wk3Inc, Wk3MiscDesc, Wk3Misc, Wk3Acc, jTextFieldWk3DialogStaffName1.getText(),
                    jTextFieldWk3DialogStaffName2.getText(), jTextFieldWk3DialogStaffName3.getText(), jTextFieldWk3DialogStaffName4.getText()});
                addItem();
                resetField();

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_jButtonDialogWk3AddActionPerformed

    private void jTextFieldWk3DialogBudgetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogBudgetMouseClicked
        budgetPOP();
    }//GEN-LAST:event_jTextFieldWk3DialogBudgetMouseClicked

    private void jTextFieldWk3DialogBudgetKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogBudgetKeyTyped
        facilityPOP();
    }//GEN-LAST:event_jTextFieldWk3DialogBudgetKeyTyped

    private void jCheckBoxDialogWk3MiscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxDialogWk3MiscActionPerformed
        if (jCheckBoxDialogWk3Misc.isSelected()) {
            jTextFieldWk3Misc.setVisible(true);
            jLabelWk3Misc.setVisible(true);
            jLabelWk3MiscAmt.setVisible(true);
            jTextFieldWk3MiscAmt.setVisible(true);
        } else {
            jTextFieldWk3Misc.setVisible(false);
            jLabelWk3Misc.setVisible(false);
            jTextFieldWk3Misc.setText("");
            jLabelWk3MiscAmt.setVisible(false);
            jTextFieldWk3MiscAmt.setVisible(false);
        }
    }//GEN-LAST:event_jCheckBoxDialogWk3MiscActionPerformed

    private void jButtonDialogWk3CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDialogWk3CloseActionPerformed
        wk3Site = "N";
        jDialogWk3.setVisible(false);
    }//GEN-LAST:event_jButtonDialogWk3CloseActionPerformed

    private void jLabelWk3Name1DelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelWk3Name1DelMouseClicked
        jTextFieldWk3DialogStaffName1.setText("");
        jLabelWk3Name1Del.setVisible(false);
    }//GEN-LAST:event_jLabelWk3Name1DelMouseClicked

    private void jLabelWk3Name2DelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelWk3Name2DelMouseClicked
        jTextFieldWk3DialogStaffName2.setText("");
        jLabelWk3Name2Del.setVisible(false);
    }//GEN-LAST:event_jLabelWk3Name2DelMouseClicked

    private void jLabelWk3Name3DelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelWk3Name3DelMouseClicked
        jTextFieldWk3DialogStaffName3.setText("");
        jLabelWk3Name3Del.setVisible(false);
    }//GEN-LAST:event_jLabelWk3Name3DelMouseClicked

    private void jLabelWk3Name4DelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelWk3Name4DelMouseClicked
        jTextFieldWk3DialogStaffName4.setText("");
        jLabelWk3Name4Del.setVisible(false);
    }//GEN-LAST:event_jLabelWk3Name4DelMouseClicked

    private void jMenuItemProvMgrApprovalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemProvMgrApprovalActionPerformed
        new JFrameMnthSupList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemProvMgrApprovalActionPerformed

    private void jMenuItemReqGenLstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReqGenLstActionPerformed
        new JFrameMnthReqGenList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemReqGenLstActionPerformed

    private void jMenuItemPlanViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPlanViewActionPerformed
        new JFrameMnthViewList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);

    }//GEN-LAST:event_jMenuItemPlanViewActionPerformed

    private void jTextFieldDialogWk4SiteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk4SiteMouseClicked
       
    }//GEN-LAST:event_jTextFieldDialogWk4SiteMouseClicked

    private void jTextFieldDialogWk4SiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk4SiteActionPerformed
       
    }//GEN-LAST:event_jTextFieldDialogWk4SiteActionPerformed

    private void jTextFieldDialogWk4SiteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk4SiteKeyTyped
       
    }//GEN-LAST:event_jTextFieldDialogWk4SiteKeyTyped

    private void jTextAreaWk4DialogJustificationKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAreaWk4DialogJustificationKeyTyped
       
    }//GEN-LAST:event_jTextAreaWk4DialogJustificationKeyTyped

    private void jComboBoxDialogWk4PerDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxDialogWk4PerDiemActionPerformed
       
    }//GEN-LAST:event_jComboBoxDialogWk4PerDiemActionPerformed

    private void jTextFieldWk4DialogStaffName4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk4DialogStaffName4MouseClicked
       
    }//GEN-LAST:event_jTextFieldWk4DialogStaffName4MouseClicked

    private void jTextFieldWk4DialogStaffName4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWk4DialogStaffName4ActionPerformed
       
    }//GEN-LAST:event_jTextFieldWk4DialogStaffName4ActionPerformed

    private void jTextFieldWk4DialogStaffName4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk4DialogStaffName4KeyTyped
       
    }//GEN-LAST:event_jTextFieldWk4DialogStaffName4KeyTyped

    private void jTextFieldWk4DialogStaffName1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk4DialogStaffName1MouseClicked
       
    }//GEN-LAST:event_jTextFieldWk4DialogStaffName1MouseClicked

    private void jTextFieldWk4DialogStaffName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWk4DialogStaffName1ActionPerformed
       
    }//GEN-LAST:event_jTextFieldWk4DialogStaffName1ActionPerformed

    private void jTextFieldWk4DialogStaffName1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk4DialogStaffName1KeyTyped
       
    }//GEN-LAST:event_jTextFieldWk4DialogStaffName1KeyTyped

    private void jTextFieldWk4DialogStaffName2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk4DialogStaffName2MouseClicked
       
    }//GEN-LAST:event_jTextFieldWk4DialogStaffName2MouseClicked

    private void jTextFieldWk4DialogStaffName2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk4DialogStaffName2KeyTyped
       
    }//GEN-LAST:event_jTextFieldWk4DialogStaffName2KeyTyped

    private void jTextFieldWk4DialogStaffName3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk4DialogStaffName3MouseClicked
       
    }//GEN-LAST:event_jTextFieldWk4DialogStaffName3MouseClicked

    private void jTextFieldWk4DialogStaffName3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk4DialogStaffName3KeyTyped
       
    }//GEN-LAST:event_jTextFieldWk4DialogStaffName3KeyTyped

    private void jButtonDialogWk4ResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDialogWk4ResetActionPerformed
       
    }//GEN-LAST:event_jButtonDialogWk4ResetActionPerformed

    private void jButtonDialogWk4AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDialogWk4AddActionPerformed
       
    }//GEN-LAST:event_jButtonDialogWk4AddActionPerformed

    private void jTextFieldWk4DialogBudgetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk4DialogBudgetMouseClicked
       
    }//GEN-LAST:event_jTextFieldWk4DialogBudgetMouseClicked

    private void jTextFieldWk4DialogBudgetKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk4DialogBudgetKeyTyped
       
    }//GEN-LAST:event_jTextFieldWk4DialogBudgetKeyTyped

    private void jCheckBoxDialogWk4MiscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxDialogWk4MiscActionPerformed
       
    }//GEN-LAST:event_jCheckBoxDialogWk4MiscActionPerformed

    private void jButtonDialogWk4CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDialogWk4CloseActionPerformed
       
    }//GEN-LAST:event_jButtonDialogWk4CloseActionPerformed

    private void jLabelWk4Name1DelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelWk4Name1DelMouseClicked
       
    }//GEN-LAST:event_jLabelWk4Name1DelMouseClicked

    private void jLabelWk4Name2DelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelWk4Name2DelMouseClicked
       
    }//GEN-LAST:event_jLabelWk4Name2DelMouseClicked

    private void jLabelWk4Name3DelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelWk4Name3DelMouseClicked
       
    }//GEN-LAST:event_jLabelWk4Name3DelMouseClicked

    private void jLabelWk4Name4DelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelWk4Name4DelMouseClicked
       
    }//GEN-LAST:event_jLabelWk4Name4DelMouseClicked

    private void jTextFieldDialogWk5SiteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk5SiteMouseClicked
       
    }//GEN-LAST:event_jTextFieldDialogWk5SiteMouseClicked

    private void jTextFieldDialogWk5SiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk5SiteActionPerformed
       
    }//GEN-LAST:event_jTextFieldDialogWk5SiteActionPerformed

    private void jTextFieldDialogWk5SiteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk5SiteKeyTyped
       
    }//GEN-LAST:event_jTextFieldDialogWk5SiteKeyTyped

    private void jTextAreaWk5DialogJustificationKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAreaWk5DialogJustificationKeyTyped
       
    }//GEN-LAST:event_jTextAreaWk5DialogJustificationKeyTyped

    private void jComboBoxDialogWk5PerDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxDialogWk5PerDiemActionPerformed
       
    }//GEN-LAST:event_jComboBoxDialogWk5PerDiemActionPerformed

    private void jTextFieldWk5DialogStaffName4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk5DialogStaffName4MouseClicked
       
    }//GEN-LAST:event_jTextFieldWk5DialogStaffName4MouseClicked

    private void jTextFieldWk5DialogStaffName4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWk5DialogStaffName4ActionPerformed
       
    }//GEN-LAST:event_jTextFieldWk5DialogStaffName4ActionPerformed

    private void jTextFieldWk5DialogStaffName4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk5DialogStaffName4KeyTyped
       
    }//GEN-LAST:event_jTextFieldWk5DialogStaffName4KeyTyped

    private void jTextFieldWk5DialogStaffName1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk5DialogStaffName1MouseClicked
       
    }//GEN-LAST:event_jTextFieldWk5DialogStaffName1MouseClicked

    private void jTextFieldWk5DialogStaffName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWk5DialogStaffName1ActionPerformed
       
    }//GEN-LAST:event_jTextFieldWk5DialogStaffName1ActionPerformed

    private void jTextFieldWk5DialogStaffName1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk5DialogStaffName1KeyTyped
       
    }//GEN-LAST:event_jTextFieldWk5DialogStaffName1KeyTyped

    private void jTextFieldWk5DialogStaffName2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk5DialogStaffName2MouseClicked
       
    }//GEN-LAST:event_jTextFieldWk5DialogStaffName2MouseClicked

    private void jTextFieldWk5DialogStaffName2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk5DialogStaffName2KeyTyped
       
    }//GEN-LAST:event_jTextFieldWk5DialogStaffName2KeyTyped

    private void jTextFieldWk5DialogStaffName3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk5DialogStaffName3MouseClicked
       
    }//GEN-LAST:event_jTextFieldWk5DialogStaffName3MouseClicked

    private void jTextFieldWk5DialogStaffName3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk5DialogStaffName3KeyTyped
       
    }//GEN-LAST:event_jTextFieldWk5DialogStaffName3KeyTyped

    private void jButtonDialogWk5ResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDialogWk5ResetActionPerformed
       
    }//GEN-LAST:event_jButtonDialogWk5ResetActionPerformed

    private void jButtonDialogWk5AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDialogWk5AddActionPerformed
       
    }//GEN-LAST:event_jButtonDialogWk5AddActionPerformed

    private void jTextFieldWk5DialogBudgetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk5DialogBudgetMouseClicked
       
    }//GEN-LAST:event_jTextFieldWk5DialogBudgetMouseClicked

    private void jTextFieldWk5DialogBudgetKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk5DialogBudgetKeyTyped
       
    }//GEN-LAST:event_jTextFieldWk5DialogBudgetKeyTyped

    private void jCheckBoxDialogWk5MiscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxDialogWk5MiscActionPerformed
       
    }//GEN-LAST:event_jCheckBoxDialogWk5MiscActionPerformed

    private void jButtonDialogWk5CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDialogWk5CloseActionPerformed
       
    }//GEN-LAST:event_jButtonDialogWk5CloseActionPerformed

    private void jLabelWk5Name1DelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelWk5Name1DelMouseClicked
       
    }//GEN-LAST:event_jLabelWk5Name1DelMouseClicked

    private void jLabelWk5Name2DelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelWk5Name2DelMouseClicked
       
    }//GEN-LAST:event_jLabelWk5Name2DelMouseClicked

    private void jLabelWk5Name3DelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelWk5Name3DelMouseClicked
       
    }//GEN-LAST:event_jLabelWk5Name3DelMouseClicked

    private void jLabelWk5Name4DelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelWk5Name4DelMouseClicked
       
    }//GEN-LAST:event_jLabelWk5Name4DelMouseClicked

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
            java.util.logging.Logger.getLogger(JFrameMnthPlan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthPlan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthPlan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthPlan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrameMnthPlan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancelBudget;
    private javax.swing.JButton jButtonCancelFacility;
    private javax.swing.JButton jButtonDialogWk1Add;
    private javax.swing.JButton jButtonDialogWk1Close;
    private javax.swing.JButton jButtonDialogWk1Reset;
    private javax.swing.JButton jButtonDialogWk2Add;
    private javax.swing.JButton jButtonDialogWk2Close;
    private javax.swing.JButton jButtonDialogWk2Reset;
    private javax.swing.JButton jButtonDialogWk3Add;
    private javax.swing.JButton jButtonDialogWk3Close;
    private javax.swing.JButton jButtonDialogWk3Reset;
    private javax.swing.JButton jButtonDialogWk4Add;
    private javax.swing.JButton jButtonDialogWk4Close;
    private javax.swing.JButton jButtonDialogWk4Reset;
    private javax.swing.JButton jButtonDialogWk5Add;
    private javax.swing.JButton jButtonDialogWk5Close;
    private javax.swing.JButton jButtonDialogWk5Reset;
    private javax.swing.JButton jButtonOk1;
    private javax.swing.JButton jButtonOk2;
    private javax.swing.JButton jButtonOk3;
    private javax.swing.JButton jButtonOk4;
    private javax.swing.JButton jButtonOkFacility;
    private javax.swing.JButton jButtonOkFacility1;
    private javax.swing.JButton jButtonSearch1;
    private javax.swing.JButton jButtonSearch2;
    private javax.swing.JButton jButtonSearch3;
    private javax.swing.JButton jButtonSearch4;
    private javax.swing.JButton jButtonWk1AddActivity;
    private javax.swing.JButton jButtonWk1DelActivity;
    private javax.swing.JButton jButtonWk2AddActivity;
    private javax.swing.JButton jButtonWk2DelActivity;
    private javax.swing.JButton jButtonWk3AddActivity;
    private javax.swing.JButton jButtonWk3DelActivity;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Acc;
    private javax.swing.JCheckBox jCheckBoxDialogWk1BrkFast;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Dinner;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Inc;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Lunch;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Misc;
    private javax.swing.JCheckBox jCheckBoxDialogWk2Acc;
    private javax.swing.JCheckBox jCheckBoxDialogWk2BrkFast;
    private javax.swing.JCheckBox jCheckBoxDialogWk2Dinner;
    private javax.swing.JCheckBox jCheckBoxDialogWk2Inc;
    private javax.swing.JCheckBox jCheckBoxDialogWk2Lunch;
    private javax.swing.JCheckBox jCheckBoxDialogWk2Misc;
    private javax.swing.JCheckBox jCheckBoxDialogWk3Acc;
    private javax.swing.JCheckBox jCheckBoxDialogWk3BrkFast;
    private javax.swing.JCheckBox jCheckBoxDialogWk3Dinner;
    private javax.swing.JCheckBox jCheckBoxDialogWk3Inc;
    private javax.swing.JCheckBox jCheckBoxDialogWk3Lunch;
    private javax.swing.JCheckBox jCheckBoxDialogWk3Misc;
    private javax.swing.JCheckBox jCheckBoxDialogWk4Acc;
    private javax.swing.JCheckBox jCheckBoxDialogWk4BrkFast;
    private javax.swing.JCheckBox jCheckBoxDialogWk4Dinner;
    private javax.swing.JCheckBox jCheckBoxDialogWk4Inc;
    private javax.swing.JCheckBox jCheckBoxDialogWk4Lunch;
    private javax.swing.JCheckBox jCheckBoxDialogWk4Misc;
    private javax.swing.JCheckBox jCheckBoxDialogWk5Acc;
    private javax.swing.JCheckBox jCheckBoxDialogWk5BrkFast;
    private javax.swing.JCheckBox jCheckBoxDialogWk5Dinner;
    private javax.swing.JCheckBox jCheckBoxDialogWk5Inc;
    private javax.swing.JCheckBox jCheckBoxDialogWk5Lunch;
    private javax.swing.JCheckBox jCheckBoxDialogWk5Misc;
    private javax.swing.JComboBox<String> jComboBoxDialogWk1PerDiem;
    private javax.swing.JComboBox<String> jComboBoxDialogWk2PerDiem;
    private javax.swing.JComboBox<String> jComboBoxDialogWk3PerDiem;
    private javax.swing.JComboBox<String> jComboBoxDialogWk4PerDiem;
    private javax.swing.JComboBox<String> jComboBoxDialogWk5PerDiem;
    private javax.swing.JComboBox<String> jComboBoxSearchResult1;
    private javax.swing.JComboBox<String> jComboBoxSearchResult2;
    private javax.swing.JComboBox<String> jComboBoxSearchResult3;
    private javax.swing.JComboBox<String> jComboBoxSearchResult4;
    private javax.swing.JComboBox<String> jComboBudgetCode;
    private javax.swing.JComboBox<String> jComboDistrictFacility;
    private javax.swing.JComboBox<String> jComboFacility;
    private javax.swing.JComboBox<String> jComboProvinceFacility;
    private com.toedter.calendar.JDateChooser jDateChooserDialogWk1ActivityDate;
    private com.toedter.calendar.JDateChooser jDateChooserDialogWk2ActivityDate;
    private com.toedter.calendar.JDateChooser jDateChooserDialogWk3ActivityDate;
    private com.toedter.calendar.JDateChooser jDateChooserDialogWk4ActivityDate;
    private com.toedter.calendar.JDateChooser jDateChooserDialogWk5ActivityDate;
    private com.toedter.calendar.JDateChooser jDateChooserWk1From;
    private com.toedter.calendar.JDateChooser jDateChooserWk1To;
    private com.toedter.calendar.JDateChooser jDateChooserWk2From;
    private com.toedter.calendar.JDateChooser jDateChooserWk2To;
    private com.toedter.calendar.JDateChooser jDateChooserWk3From;
    private com.toedter.calendar.JDateChooser jDateChooserWk3To;
    private javax.swing.JDialog jDialogBudget;
    private javax.swing.JDialog jDialogFacility;
    private javax.swing.JDialog jDialogSearchName1;
    private javax.swing.JDialog jDialogSearchName2;
    private javax.swing.JDialog jDialogSearchName3;
    private javax.swing.JDialog jDialogSearchName4;
    private javax.swing.JDialog jDialogWaiting;
    private javax.swing.JDialog jDialogWaitingEmail;
    private javax.swing.JDialog jDialogWk1;
    private javax.swing.JDialog jDialogWk2;
    private javax.swing.JDialog jDialogWk3;
    private javax.swing.JDialog jDialogWk4;
    private javax.swing.JDialog jDialogWk5;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabelBudgetCode;
    private javax.swing.JLabel jLabelDialogPerDiem;
    private javax.swing.JLabel jLabelDialogWk1ActSiteClass;
    private javax.swing.JLabel jLabelDialogWk1ActivityDate;
    private javax.swing.JLabel jLabelDialogWk1Dis;
    private javax.swing.JLabel jLabelDialogWk1Dis1;
    private javax.swing.JLabel jLabelDialogWk1Dis2;
    private javax.swing.JLabel jLabelDialogWk1Rank;
    private javax.swing.JLabel jLabelDialogWk1Site;
    private javax.swing.JLabel jLabelDialogWk1SiteClass;
    private javax.swing.JLabel jLabelDialogWk2ActSiteClass;
    private javax.swing.JLabel jLabelDialogWk2ActivityDate;
    private javax.swing.JLabel jLabelDialogWk2Dis;
    private javax.swing.JLabel jLabelDialogWk2PerDiem;
    private javax.swing.JLabel jLabelDialogWk2PerDiem1;
    private javax.swing.JLabel jLabelDialogWk2Rank;
    private javax.swing.JLabel jLabelDialogWk2Site;
    private javax.swing.JLabel jLabelDialogWk2SiteClass;
    private javax.swing.JLabel jLabelDialogWk3ActSiteClass;
    private javax.swing.JLabel jLabelDialogWk3ActivityDate;
    private javax.swing.JLabel jLabelDialogWk3Dis;
    private javax.swing.JLabel jLabelDialogWk3Dis3;
    private javax.swing.JLabel jLabelDialogWk3Rank;
    private javax.swing.JLabel jLabelDialogWk3Site;
    private javax.swing.JLabel jLabelDialogWk3SiteClass;
    private javax.swing.JLabel jLabelDialogWk4ActSiteClass;
    private javax.swing.JLabel jLabelDialogWk4ActivityDate;
    private javax.swing.JLabel jLabelDialogWk4Dis;
    private javax.swing.JLabel jLabelDialogWk4Dis4;
    private javax.swing.JLabel jLabelDialogWk4PerDiem;
    private javax.swing.JLabel jLabelDialogWk4Rank;
    private javax.swing.JLabel jLabelDialogWk4Site;
    private javax.swing.JLabel jLabelDialogWk4SiteClass;
    private javax.swing.JLabel jLabelDialogWk5ActSiteClass;
    private javax.swing.JLabel jLabelDialogWk5ActivityDate;
    private javax.swing.JLabel jLabelDialogWk5Dis;
    private javax.swing.JLabel jLabelDialogWk5Dis5;
    private javax.swing.JLabel jLabelDialogWk5PerDiem;
    private javax.swing.JLabel jLabelDialogWk5Rank;
    private javax.swing.JLabel jLabelDialogWk5Site;
    private javax.swing.JLabel jLabelDialogWk5SiteClass;
    private javax.swing.JLabel jLabelDist;
    private javax.swing.JLabel jLabelDistrict;
    private javax.swing.JLabel jLabelDistrictFacility;
    private javax.swing.JLabel jLabelEmp;
    private javax.swing.JLabel jLabelFacility;
    private javax.swing.JLabel jLabelGenLogNam1;
    private javax.swing.JLabel jLabelGenLogNam3;
    private javax.swing.JLabel jLabelGenLogNam5;
    private javax.swing.JLabel jLabelHeader1;
    private javax.swing.JLabel jLabelHeader2;
    private javax.swing.JLabel jLabelHeaderGen4;
    private javax.swing.JLabel jLabelHeaderGen7;
    private javax.swing.JLabel jLabelHeaderGen8;
    private javax.swing.JLabel jLabelLineDate;
    private javax.swing.JLabel jLabelLineDate1;
    private javax.swing.JLabel jLabelLineDate2;
    private javax.swing.JLabel jLabelLineLogNam;
    private javax.swing.JLabel jLabelLineLogNam1;
    private javax.swing.JLabel jLabelLineLogNam2;
    private javax.swing.JLabel jLabelLineTime;
    private javax.swing.JLabel jLabelLineTime1;
    private javax.swing.JLabel jLabelLineTime2;
    private javax.swing.JLabel jLabelLogo1;
    private javax.swing.JLabel jLabelLogo2;
    private javax.swing.JLabel jLabelLogo3;
    private javax.swing.JLabel jLabelOffice1;
    private javax.swing.JLabel jLabelProvince;
    private javax.swing.JLabel jLabelProvince1;
    private javax.swing.JLabel jLabelProvinceFacility;
    private javax.swing.JLabel jLabelRef;
    private javax.swing.JLabel jLabelRefNum;
    private javax.swing.JLabel jLabelRemain;
    private javax.swing.JLabel jLabelRemain1;
    private javax.swing.JLabel jLabelRemain2;
    private javax.swing.JLabel jLabelRemain4;
    private javax.swing.JLabel jLabelRemain5;
    private javax.swing.JLabel jLabelSerial;
    private javax.swing.JLabel jLabelWk1DialogActDis;
    private javax.swing.JLabel jLabelWk1DialogActRank;
    private javax.swing.JLabel jLabelWk1DialogActivityDesc;
    private javax.swing.JLabel jLabelWk1DialogBudget;
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
    private javax.swing.JLabel jLabelWk2DialogActDis;
    private javax.swing.JLabel jLabelWk2DialogActRank;
    private javax.swing.JLabel jLabelWk2DialogActivityDesc;
    private javax.swing.JLabel jLabelWk2DialogBudget;
    private javax.swing.JLabel jLabelWk2DialogJustification;
    private javax.swing.JLabel jLabelWk2DialogStaff;
    private javax.swing.JLabel jLabelWk2DialogStaffName1;
    private javax.swing.JLabel jLabelWk2DialogStaffName2;
    private javax.swing.JLabel jLabelWk2DialogStaffName3;
    private javax.swing.JLabel jLabelWk2DialogStaffName4;
    private javax.swing.JLabel jLabelWk2Misc;
    private javax.swing.JLabel jLabelWk2MiscAmt;
    private javax.swing.JLabel jLabelWk2Name1Del;
    private javax.swing.JLabel jLabelWk2Name2Del;
    private javax.swing.JLabel jLabelWk2Name3Del;
    private javax.swing.JLabel jLabelWk2Name4Del;
    private javax.swing.JLabel jLabelWk3DialogActDis;
    private javax.swing.JLabel jLabelWk3DialogActRank;
    private javax.swing.JLabel jLabelWk3DialogActivityDesc;
    private javax.swing.JLabel jLabelWk3DialogBudget;
    private javax.swing.JLabel jLabelWk3DialogJustification;
    private javax.swing.JLabel jLabelWk3DialogStaff;
    private javax.swing.JLabel jLabelWk3DialogStaffName1;
    private javax.swing.JLabel jLabelWk3DialogStaffName2;
    private javax.swing.JLabel jLabelWk3DialogStaffName3;
    private javax.swing.JLabel jLabelWk3DialogStaffName4;
    private javax.swing.JLabel jLabelWk3From;
    private javax.swing.JLabel jLabelWk3Misc;
    private javax.swing.JLabel jLabelWk3MiscAmt;
    private javax.swing.JLabel jLabelWk3Name1Del;
    private javax.swing.JLabel jLabelWk3Name2Del;
    private javax.swing.JLabel jLabelWk3Name3Del;
    private javax.swing.JLabel jLabelWk3Name4Del;
    private javax.swing.JLabel jLabelWk3To;
    private javax.swing.JLabel jLabelWk4DialogActDis;
    private javax.swing.JLabel jLabelWk4DialogActRank;
    private javax.swing.JLabel jLabelWk4DialogActivityDesc;
    private javax.swing.JLabel jLabelWk4DialogBudget;
    private javax.swing.JLabel jLabelWk4DialogJustification;
    private javax.swing.JLabel jLabelWk4DialogStaff;
    private javax.swing.JLabel jLabelWk4DialogStaffName1;
    private javax.swing.JLabel jLabelWk4DialogStaffName2;
    private javax.swing.JLabel jLabelWk4DialogStaffName3;
    private javax.swing.JLabel jLabelWk4DialogStaffName4;
    private javax.swing.JLabel jLabelWk4Misc;
    private javax.swing.JLabel jLabelWk4MiscAmt;
    private javax.swing.JLabel jLabelWk4Name1Del;
    private javax.swing.JLabel jLabelWk4Name2Del;
    private javax.swing.JLabel jLabelWk4Name3Del;
    private javax.swing.JLabel jLabelWk4Name4Del;
    private javax.swing.JLabel jLabelWk5DialogActDis;
    private javax.swing.JLabel jLabelWk5DialogActRank;
    private javax.swing.JLabel jLabelWk5DialogActivityDesc;
    private javax.swing.JLabel jLabelWk5DialogBudget;
    private javax.swing.JLabel jLabelWk5DialogJustification;
    private javax.swing.JLabel jLabelWk5DialogStaff;
    private javax.swing.JLabel jLabelWk5DialogStaffName1;
    private javax.swing.JLabel jLabelWk5DialogStaffName2;
    private javax.swing.JLabel jLabelWk5DialogStaffName3;
    private javax.swing.JLabel jLabelWk5DialogStaffName4;
    private javax.swing.JLabel jLabelWk5Misc;
    private javax.swing.JLabel jLabelWk5MiscAmt;
    private javax.swing.JLabel jLabelWk5Name1Del;
    private javax.swing.JLabel jLabelWk5Name2Del;
    private javax.swing.JLabel jLabelWk5Name3Del;
    private javax.swing.JLabel jLabelWk5Name4Del;
    private javax.swing.JLabel jLabelWkDuration;
    private javax.swing.JLabel jLabelWkDuration1;
    private javax.swing.JLabel jLabelWkDuration2;
    private javax.swing.JMenuItem jMenuAcqEdit;
    private javax.swing.JMenu jMenuAcquittal;
    private javax.swing.JMenuItem jMenuAgeAlluser;
    private javax.swing.JMenuItem jMenuAgeByUser;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenu jMenuEdit;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenuItem jMenuItem1;
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
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuPlanApproval;
    private javax.swing.JMenuItem jMenuItemHeadApp;
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanelSearchID;
    private javax.swing.JPanel jPanelSearchID1;
    private javax.swing.JPanel jPanelSearchID2;
    private javax.swing.JPanel jPanelSearchID4;
    private javax.swing.JPanel jPanelWkOne;
    private javax.swing.JPanel jPanelWkThree;
    private javax.swing.JPanel jPanelWkTwo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator11;
    private javax.swing.JPopupMenu.Separator jSeparator12;
    private javax.swing.JPopupMenu.Separator jSeparator13;
    private javax.swing.JPopupMenu.Separator jSeparator14;
    private javax.swing.JPopupMenu.Separator jSeparator15;
    private javax.swing.JPopupMenu.Separator jSeparator16;
    private javax.swing.JPopupMenu.Separator jSeparator17;
    private javax.swing.JPopupMenu.Separator jSeparator18;
    private javax.swing.JPopupMenu.Separator jSeparator19;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator20;
    private javax.swing.JPopupMenu.Separator jSeparator21;
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
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableWk1Activities;
    private javax.swing.JTable jTableWk2Activities;
    private javax.swing.JTable jTableWk3Activities;
    private javax.swing.JTextArea jTextAreaWk1DialogJustification;
    private javax.swing.JTextArea jTextAreaWk2DialogJustification;
    private javax.swing.JTextArea jTextAreaWk3DialogJustification;
    private javax.swing.JTextArea jTextAreaWk4DialogJustification;
    private javax.swing.JTextArea jTextAreaWk5DialogJustification;
    private javax.swing.JTextField jTextDistDest;
    private javax.swing.JTextField jTextFieldDialogWk1Site;
    private javax.swing.JTextField jTextFieldDialogWk2Site;
    private javax.swing.JTextField jTextFieldDialogWk3Site;
    private javax.swing.JTextField jTextFieldDialogWk4Site;
    private javax.swing.JTextField jTextFieldDialogWk5Site;
    private javax.swing.JTextField jTextFieldSearchNam1;
    private javax.swing.JTextField jTextFieldSearchNam2;
    private javax.swing.JTextField jTextFieldSearchNam3;
    private javax.swing.JTextField jTextFieldSearchNam4;
    private javax.swing.JTextField jTextFieldWk1DialogActivityDesc;
    private javax.swing.JTextField jTextFieldWk1DialogBudget;
    private javax.swing.JTextField jTextFieldWk1DialogStaffName1;
    private javax.swing.JTextField jTextFieldWk1DialogStaffName2;
    private javax.swing.JTextField jTextFieldWk1DialogStaffName3;
    private javax.swing.JTextField jTextFieldWk1DialogStaffName4;
    private javax.swing.JTextField jTextFieldWk1Misc;
    private javax.swing.JTextField jTextFieldWk1MiscAmt;
    private javax.swing.JTextField jTextFieldWk2DialogActivityDesc;
    private javax.swing.JTextField jTextFieldWk2DialogBudget;
    private javax.swing.JTextField jTextFieldWk2DialogStaffName1;
    private javax.swing.JTextField jTextFieldWk2DialogStaffName2;
    private javax.swing.JTextField jTextFieldWk2DialogStaffName3;
    private javax.swing.JTextField jTextFieldWk2DialogStaffName4;
    private javax.swing.JTextField jTextFieldWk2Misc;
    private javax.swing.JTextField jTextFieldWk2MiscAmt;
    private javax.swing.JTextField jTextFieldWk3DialogActivityDesc;
    private javax.swing.JTextField jTextFieldWk3DialogBudget;
    private javax.swing.JTextField jTextFieldWk3DialogStaffName1;
    private javax.swing.JTextField jTextFieldWk3DialogStaffName2;
    private javax.swing.JTextField jTextFieldWk3DialogStaffName3;
    private javax.swing.JTextField jTextFieldWk3DialogStaffName4;
    private javax.swing.JTextField jTextFieldWk3Misc;
    private javax.swing.JTextField jTextFieldWk3MiscAmt;
    private javax.swing.JTextField jTextFieldWk4DialogActivityDesc;
    private javax.swing.JTextField jTextFieldWk4DialogBudget;
    private javax.swing.JTextField jTextFieldWk4DialogStaffName1;
    private javax.swing.JTextField jTextFieldWk4DialogStaffName2;
    private javax.swing.JTextField jTextFieldWk4DialogStaffName3;
    private javax.swing.JTextField jTextFieldWk4DialogStaffName4;
    private javax.swing.JTextField jTextFieldWk4Misc;
    private javax.swing.JTextField jTextFieldWk4MiscAmt;
    private javax.swing.JTextField jTextFieldWk5DialogActivityDesc;
    private javax.swing.JTextField jTextFieldWk5DialogBudget;
    private javax.swing.JTextField jTextFieldWk5DialogStaffName1;
    private javax.swing.JTextField jTextFieldWk5DialogStaffName2;
    private javax.swing.JTextField jTextFieldWk5DialogStaffName3;
    private javax.swing.JTextField jTextFieldWk5DialogStaffName4;
    private javax.swing.JTextField jTextFieldWk5Misc;
    private javax.swing.JTextField jTextFieldWk5MiscAmt;
    // End of variables declaration//GEN-END:variables
}
