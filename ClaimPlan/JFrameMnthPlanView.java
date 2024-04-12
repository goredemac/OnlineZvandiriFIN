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
import ClaimAppendix1.*;
import ClaimAppendix2.*;
import ClaimReports.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.Date;
import javax.swing.Timer;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 *
 * @author goredemac
 */
public class JFrameMnthPlanView extends javax.swing.JFrame {

    String ipAdd =    "ophidutilapp.southafricanorth.cloudapp.azure.com:16432"       ;
    String usrName = "appfin";
    String usrPass = "542ytDYvynv$TVYb";
    String mailUsrNam = "finance@ophid.co.zw";
    String mailUsrPass = "MgqM5utyUr43x#";
    boolean ignoreInput = false;
    String filename = null;
    int charMax = 200;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String wk1Site = "N";
    String wk2Site = "N";
    String wk3Site = "N";
    String hostName = "";
    String breakfastAll, lunchAll, lunchNPAll, lunchPAll, dinnerAll, docVer, docNextVer, usrGrp,
            incidentalAll, unProvedAll, SearchRef, createUsrNam, usrMail, action, status,planStatus;
    PreparedStatement pst = null;
    PreparedStatement pst1 = null;
    int itmNum = 1;

    /**
     * Creates new form JFrameMnthPlan
     */
    public JFrameMnthPlanView() {
        initComponents();
        //  jTabbedPaneMain.remove(jTabbedPaneComments);
        jTabbedPaneMain.setEnabledAt(3, false);

    }

    public JFrameMnthPlanView(String ref, String usrLogNam) {
        initComponents();
        SearchRef = ref;
        showDate();
        showTime();
        fetchdataGenWk1();
        fetchdataWk1();
        fetchdataWk2();
        fetchdataWk3();
        jLabelEmp.setText(usrLogNam);
        jLabelEmp.setVisible(false);
 jMenuItemRequest.setEnabled(false);
 jMenuItemSpecialAcquittal.setEnabled(false);
        jLabelRefNum.setText(SearchRef);
        findUser();
        findCreator();
        findUserGrp();
        jTabbedPaneMain.setEnabledAt(3, false);

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

            }
        }) {

        }.start();

    }

    void computerName() {

        try {
            java.net.InetAddress addr = InetAddress.getLocalHost();
            hostName = addr.getHostName();

        } catch (Exception ex) {
            System.out.println("Hostname can not be resolved");
        }
    }

    void showDate() {
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");

        jLabelLineDate.setText(s.format(d));
        jLabelLineDate1.setText(s.format(d));
        jLabelLineDate2.setText(s.format(d));
        jLabelLineDate3.setText(s.format(d));

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
                jLabelLineLogNam3.setText(r.getString(2));

//                sendTo = r.getString(5);
//                createUsrNam = r.getString(2);
//                supUsrMail = r.getString(6);
            }

            //                 conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Connect to Database (Province).Try Again", "Error Connection", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
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

    void findCreator() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select ACTION_BY from [ClaimsAppSysZvandiri].[dbo]."
                    + "[PlanActTab] where PLAN_REF_NUM =" + SearchRef + " and ACT_VER = " + docVer);

            while (r.next()) {

                createUsrNam = r.getString(1);
            }
            Statement st1 = conn.createStatement();
            ResultSet r1 = st1.executeQuery("select EMP_MAIL from [ClaimsAppSysZvandiri].[dbo]."
                    + "[EmpDetTab] where EMP_NAM ='" + createUsrNam + "'");

            while (r1.next()) {

                usrMail = r1.getString(1);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void WkPlanActionApproved() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            String sqlWkplanperiod = "insert into [ClaimsAppSysZvandiri].[dbo].[PlanActTab] (SERIAL,PLAN_REF_NUM,USR_ACTION,DOC_STATUS,"
                    + "ACTION_BY,SEND_TO,ACTION_DATE,ACTION_TIME,"
                    + "ACTIONED_ON_COMPUTER,ACT_VER,DOC_VER,ACT_REC_STA)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            action = "Plan - Approved";
            status = "Approved";
            pst1 = conn.prepareStatement(sqlWkplanperiod);
            pst1.setString(1, "P");
            pst1.setString(2, SearchRef);
            pst1.setString(3, action);
            pst1.setString(4, status);
            pst1.setString(5, jLabelLineLogNam.getText());
            pst1.setString(6, createUsrNam);
            pst1.setString(7, jLabelLineDate.getText());
            pst1.setString(8, jLabelLineTime.getText());
            pst1.setString(9, hostName);
            pst1.setString(10, docNextVer);
            pst1.setString(11, "2");
            pst1.setString(12, "C");

            pst1.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void WkPlanActionDisApproved() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            String sqlWkplanperiod = "insert into [ClaimsAppSysZvandiri].[dbo].[PlanActTab] (SERIAL,PLAN_REF_NUM,USR_ACTION,DOC_STATUS,"
                    + "ACTION_BY,SEND_TO,ACTION_DATE,ACTION_TIME,"
                    + "ACTIONED_ON_COMPUTER,COMMENTS,ACT_VER,DOC_VER,ACT_REC_STA)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

            action = "Plan - Approved";
            status = "Not Approved";
            pst1 = conn.prepareStatement(sqlWkplanperiod);
            pst1.setString(1, "P");
            pst1.setString(2, SearchRef);
            pst1.setString(3, action);
            pst1.setString(4, status);
            pst1.setString(5, jLabelLineLogNam.getText());
            pst1.setString(6, createUsrNam);
            pst1.setString(7, jLabelLineDate.getText());
            pst1.setString(8, jLabelLineTime.getText());
            pst1.setString(9, hostName);
            pst1.setString(10, jTextAreaComments.getText());
            pst1.setString(11, docNextVer);
            pst1.setString(12, "2");
            pst1.setString(13, "C");

            pst1.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void WkPlanActionAmendRequest() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            String sqlWkplanperiod = "insert into [ClaimsAppSysZvandiri].[dbo].[PlanActTab] (SERIAL,PLAN_REF_NUM,USR_ACTION,DOC_STATUS,"
                    + "ACTION_BY,SEND_TO,ACTION_DATE,ACTION_TIME,"
                    + "ACTIONED_ON_COMPUTER,COMMENTS,ACT_VER,DOC_VER,ACT_REC_STA)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

            action = "Plan - Amend Request";
            status = "Amend Request";

            pst1 = conn.prepareStatement(sqlWkplanperiod);
            pst1.setString(1, "P");
            pst1.setString(2, SearchRef);
            pst1.setString(3, action);
            pst1.setString(4, status);
            pst1.setString(5, jLabelLineLogNam.getText());
            pst1.setString(6, createUsrNam);
            pst1.setString(7, jLabelLineDate.getText());
            pst1.setString(8, jLabelLineTime.getText());
            pst1.setString(9, hostName);
            pst1.setString(10, jTextAreaComments.getText());
            pst1.setString(11, docNextVer);
            pst1.setString(12, "1");
            pst1.setString(13, "A");

            pst1.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdataGenWk1() {
        try {

            DefaultTableModel modelWk1 = (DefaultTableModel) jTableWk1Activities.getModel();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();

            st.executeQuery("SELECT PROVINCE ,DISTRICT ,WK1_PLAN_START_DATE ,WK1_PLAN_END_DATE,"
                    + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,Wk3_PLAN_START_DATE,Wk3_PLAN_END_DATE "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab]"
                    + " where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A'");

            ResultSet r = st.getResultSet();

            while (r.next()) {

                jLabelProvince.setText(r.getString(1));
                jLabelDistrict.setText(r.getString(2));
                jLabelWk1DateFrom.setText(r.getString(3));
                jLabelWk1DateTo.setText(r.getString(4));
                jLabelWk2DateFrom.setText(r.getString(5));
                jLabelWk2DateTo.setText(r.getString(6));
                jLabelWk3DateFrom.setText(r.getString(7));
                jLabelWk3DateTo.setText(r.getString(8));
            }

            st1.executeQuery("SELECT distinct ACT_VER,ACT_VER + 1  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] "
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A'");

            ResultSet r1 = st1.getResultSet();

            while (r1.next()) {
                docVer = r1.getString(1);
                docNextVer = r1.getString(2);
            }

            if (Integer.parseInt(docVer) == 1) {
                jTabbedPaneMain.remove(jPanelWkPrevComments);
            }

            st2.executeQuery("SELECT COMMENTS FROM [ClaimsAppSysZvandiri].[dbo].[PlanActTab]"
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A'");

            ResultSet r2 = st2.getResultSet();

            while (r2.next()) {
                jTextAreaPrevComments.setText(r2.getString(1));
                
               
            }

            jTextAreaPrevComments.setLineWrap(true);
            jTextAreaPrevComments.setWrapStyleWord(true);
            jTextAreaPrevComments.setEditable(false);
        } catch (Exception e) {

        }
    }

    void fetchdataWk1() {
        try {

            DefaultTableModel modelWk1 = (DefaultTableModel) jTableWk1Activities.getModel();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] "
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A'");

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
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] "
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A'");

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

            DefaultTableModel modelWk3 = (DefaultTableModel) jTableWk3Activities.getModel();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] "
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A'");

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

    void updatePrevRecord() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            String sqlPlanAct = "update [ClaimsAppSysZvandiri].[dbo].[PlanActTab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' ";

            pst = conn.prepareStatement(sqlPlanAct);
            pst.executeUpdate();

            String sqlPlanPeriod = "update [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' ";

            pst = conn.prepareStatement(sqlPlanPeriod);
            pst.executeUpdate();

            String sqlPlanWk1 = "update [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' ";

            pst = conn.prepareStatement(sqlPlanWk1);
            pst.executeUpdate();

            String sqlPlanWk2 = "update [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' ";

            pst = conn.prepareStatement(sqlPlanWk2);
            pst.executeUpdate();

            String sqlPlanWk3 = "update [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' ";

            pst = conn.prepareStatement(sqlPlanWk3);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    void updateWk1Plan() {
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
                pst1.setString(2, SearchRef);
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
                if ("Request Amendment".equals(jComboSupApproval.getSelectedItem().toString())) {
                    pst1.setString(24, "1");
                    pst1.setString(25, "A");
                } else if (("Approved".equals(jComboSupApproval.getSelectedItem().toString()))
                        || ("Not Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
                    pst1.setString(24, "2");
                    pst1.setString(25, "C");
                }
                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateWk2Plan() {
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
                pst1.setString(2, SearchRef);
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
                if ("Request Amendment".equals(jComboSupApproval.getSelectedItem().toString())) {
                    pst1.setString(24, "1");
                    pst1.setString(25, "A");
                } else if (("Approved".equals(jComboSupApproval.getSelectedItem().toString()))
                        || ("Not Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
                    pst1.setString(24, "2");
                    pst1.setString(25, "C");
                }
                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateWk3Plan() {
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
                pst1.setString(2, SearchRef);
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
                if ("Request Amendment".equals(jComboSupApproval.getSelectedItem().toString())) {
                    pst1.setString(24, "1");
                    pst1.setString(25, "A");
                } else if (("Approved".equals(jComboSupApproval.getSelectedItem().toString()))
                        || ("Not Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
                    pst1.setString(24, "2");
                    pst1.setString(25, "C");
                }
                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateWkPlanPeriod() {
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
                pst1.setString(2, SearchRef);
                pst1.setString(3, jLabelProvince.getText());
                pst1.setString(4, jLabelDistrict.getText());
                pst1.setString(5, jLabelWk1DateFrom.getText());
                pst1.setString(6, jLabelWk1DateTo.getText());
                pst1.setString(7, docNextVer);
                if ("Request Amendment".equals(jComboSupApproval.getSelectedItem().toString())) {
                    pst1.setString(8, "1");
                    pst1.setString(9, "A");
                } else if (("Approved".equals(jComboSupApproval.getSelectedItem().toString()))
                        || ("Not Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
                    pst1.setString(8, "2");
                    pst1.setString(9, "C");
                }

                pst1.executeUpdate();

            } else if (jTableWk1Activities.getRowCount() > 0 && jTableWk2Activities.getRowCount() > 0 && jTableWk3Activities.getRowCount() == 0) {

                String sqlWkplanperiod = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] "
                        + "(SERIAL,PLAN_REF_NUM,PROVINCE,DISTRICT,Wk1_PLAN_START_DATE,Wk1_PLAN_END_DATE,"
                        + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,"
                        + "ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWkplanperiod);
                pst1.setString(1, "P");
                pst1.setString(2, SearchRef);
                pst1.setString(3, jLabelProvince.getText());
                pst1.setString(4, jLabelDistrict.getText());
                pst1.setString(5, jLabelWk1DateFrom.getText());
                pst1.setString(6, jLabelWk1DateTo.getText());
                pst1.setString(7, jLabelWk2DateFrom.getText());
                pst1.setString(8, jLabelWk2DateTo.getText());
                pst1.setString(9, docNextVer);
                if ("Request Amendment".equals(jComboSupApproval.getSelectedItem().toString())) {
                    pst1.setString(10, "1");
                    pst1.setString(11, "A");
                } else if (("Approved".equals(jComboSupApproval.getSelectedItem().toString()))
                        || ("Not Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
                    pst1.setString(10, "2");
                    pst1.setString(11, "C");
                }

                pst1.executeUpdate();
            } else {

                String sqlWkplanperiod = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] "
                        + "(SERIAL,PLAN_REF_NUM,PROVINCE,DISTRICT,Wk1_PLAN_START_DATE,Wk1_PLAN_END_DATE,"
                        + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,Wk3_PLAN_START_DATE,Wk3_PLAN_END_DATE,"
                        + "ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWkplanperiod);
                pst1.setString(1, "P");
                pst1.setString(2, SearchRef);
                pst1.setString(3, jLabelProvince.getText());
                pst1.setString(4, jLabelDistrict.getText());
                pst1.setString(5, jLabelWk1DateFrom.getText());
                pst1.setString(6, jLabelWk1DateTo.getText());
                pst1.setString(7, jLabelWk2DateFrom.getText());
                pst1.setString(8, jLabelWk2DateTo.getText());
                pst1.setString(9, jLabelWk3DateFrom.getText());
                pst1.setString(10, jLabelWk3DateTo.getText());
                pst1.setString(11, docNextVer);
                if ("Request Amendment".equals(jComboSupApproval.getSelectedItem().toString())) {
                    pst1.setString(12, "1");
                    pst1.setString(13, "A");
                } else if (("Approved".equals(jComboSupApproval.getSelectedItem().toString()))
                        || ("Not Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
                    pst1.setString(12, "2");
                    pst1.setString(13, "C");
                }
                pst1.executeUpdate();

            }

        } catch (Exception e) {
            System.out.println(e);
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
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailUsrNam));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(usrMail));
            message.setSubject("Plan Amendment Request.Plan Reference No. "
                    + jLabelSerial.getText() + " " + jLabelRefNum.getText());

            message.setContent("<html><body> Dear " + createUsrNam + "<br><br> "
                    + "Your plan has been examined by " + jLabelLineLogNam.getText() + " "
                    + "with outcome <b> plan " + status + ".</b> <br><br>Please check "
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

        jDialogWk1 = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabelDialogWk1ActivityDate = new javax.swing.JLabel();
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
        jLabelDialogPerDiem = new javax.swing.JLabel();
        jLabelWk1DialogStaffName = new javax.swing.JLabel();
        jLabelWk1DialogStaffName1 = new javax.swing.JLabel();
        jLabelWk1DialogStaffName2 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jTextFieldWk1DialogStaffName4 = new javax.swing.JTextField();
        jTextFieldWk1DialogStaffName1 = new javax.swing.JTextField();
        jTextFieldWk1DialogStaffName2 = new javax.swing.JTextField();
        jTextFieldWk1DialogStaffName3 = new javax.swing.JTextField();
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
        jLabelWk1MiscAmt = new javax.swing.JLabel();
        jTextFieldWk1MiscAmt = new javax.swing.JTextField();
        jLabelStaff = new javax.swing.JLabel();
        jLabelActWk1Date = new javax.swing.JLabel();
        jDialogWk2 = new javax.swing.JDialog();
        jPanel2 = new javax.swing.JPanel();
        jLabelDialogWk2ActivityDate = new javax.swing.JLabel();
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
        jLabelDialogPerDiemWk2 = new javax.swing.JLabel();
        jLabelWk2DialogStaffName = new javax.swing.JLabel();
        jLabelWk2DialogStaffName1 = new javax.swing.JLabel();
        jLabelWk2DialogStaffName2 = new javax.swing.JLabel();
        jLabelWk2DialogStaffName3 = new javax.swing.JLabel();
        jTextFieldWk2DialogStaffName4 = new javax.swing.JTextField();
        jTextFieldWk2DialogStaffName1 = new javax.swing.JTextField();
        jTextFieldWk2DialogStaffName2 = new javax.swing.JTextField();
        jTextFieldWk2DialogStaffName3 = new javax.swing.JTextField();
        jLabelWk2DialogBudget = new javax.swing.JLabel();
        jTextFieldWk2DialogBudget = new javax.swing.JTextField();
        jCheckBoxDialogWk2BrkFast = new javax.swing.JCheckBox();
        jCheckBoxDialogWk2Lunch = new javax.swing.JCheckBox();
        jCheckBoxDialogWk2Dinner = new javax.swing.JCheckBox();
        jCheckBoxDialogWk2Acc = new javax.swing.JCheckBox();
        jLabelDialogWk2Dis2 = new javax.swing.JLabel();
        jLabelWk2DialogActDis = new javax.swing.JLabel();
        jLabelDialogWk2Dis = new javax.swing.JLabel();
        jLabelRemain1 = new javax.swing.JLabel();
        jCheckBoxDialogWk2Misc = new javax.swing.JCheckBox();
        jCheckBoxDialogWk2Inc = new javax.swing.JCheckBox();
        jLabelWk2Misc = new javax.swing.JLabel();
        jTextFieldWk2Misc = new javax.swing.JTextField();
        jLabelWk2MiscAmt = new javax.swing.JLabel();
        jTextFieldWk2MiscAmt = new javax.swing.JTextField();
        jLabelStaffWk2 = new javax.swing.JLabel();
        jLabelActWk2Date = new javax.swing.JLabel();
        jDialogWk3 = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        jLabelDialogWk3ActivityDate = new javax.swing.JLabel();
        jLabelDialogWk3Site = new javax.swing.JLabel();
        jTextFieldDialogWk3Site = new javax.swing.JTextField();
        jLabelWk3DialogJustification = new javax.swing.JLabel();
        jLabelDialogWk3SiteClass = new javax.swing.JLabel();
        jLabelDialogWk3ActSiteClass = new javax.swing.JLabel();
        jLabelDialogWk3Rank = new javax.swing.JLabel();
        jLabelWk3DialogStaffName4 = new javax.swing.JLabel();
        jTextFieldWk3DialogActivityDesc = new javax.swing.JTextField();
        jLabelWk3DialogActivityDesc = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextAreaWk3DialogJustification = new javax.swing.JTextArea();
        jSeparator10 = new javax.swing.JSeparator();
        jLabelWk3DialogActRank = new javax.swing.JLabel();
        jLabelDialogPerDiemWk3 = new javax.swing.JLabel();
        jLabelWk3DialogStaffName = new javax.swing.JLabel();
        jLabelWk3DialogStaffName1 = new javax.swing.JLabel();
        jLabelWk3DialogStaffName2 = new javax.swing.JLabel();
        jLabelWk3DialogStaffName3 = new javax.swing.JLabel();
        jTextFieldWk3DialogStaffName4 = new javax.swing.JTextField();
        jTextFieldWk3DialogStaffName1 = new javax.swing.JTextField();
        jTextFieldWk3DialogStaffName2 = new javax.swing.JTextField();
        jTextFieldWk3DialogStaffName3 = new javax.swing.JTextField();
        jLabelWk3DialogBudget = new javax.swing.JLabel();
        jTextFieldWk3DialogBudget = new javax.swing.JTextField();
        jCheckBoxDialogWk3BrkFast = new javax.swing.JCheckBox();
        jCheckBoxDialogWk3Lunch = new javax.swing.JCheckBox();
        jCheckBoxDialogWk3Dinner = new javax.swing.JCheckBox();
        jCheckBoxDialogWk3Acc = new javax.swing.JCheckBox();
        jLabelDialogWk2Dis3 = new javax.swing.JLabel();
        jLabelWk3DialogActDis = new javax.swing.JLabel();
        jLabelDialogWk3Dis = new javax.swing.JLabel();
        jLabelRemain3 = new javax.swing.JLabel();
        jCheckBoxDialogWk3Misc = new javax.swing.JCheckBox();
        jCheckBoxDialogWk3Inc = new javax.swing.JCheckBox();
        jLabelWk3Misc = new javax.swing.JLabel();
        jTextFieldWk3Misc = new javax.swing.JTextField();
        jLabelWk3MiscAmt = new javax.swing.JLabel();
        jTextFieldWk3MiscAmt = new javax.swing.JTextField();
        jLabelStaffWk3 = new javax.swing.JLabel();
        jLabelActWk3Date = new javax.swing.JLabel();
        jDialogWaitingEmail = new javax.swing.JDialog();
        jDialogAmendReqComments = new javax.swing.JDialog();
        jPanelDetUpd2 = new javax.swing.JPanel();
        jLabelRejectHeader1 = new javax.swing.JLabel();
        jLabelAmmendDesc1 = new javax.swing.JLabel();
        jButtonAmmendDialogOk1 = new javax.swing.JButton();
        jTextAmmendRequest = new javax.swing.JTextField();
        jTabbedPaneMain = new javax.swing.JTabbedPane();
        jPanelWkOne = new javax.swing.JPanel();
        jLabelWkDuration = new javax.swing.JLabel();
        jLabelWk1From = new javax.swing.JLabel();
        jLabelWk1To = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableWk1Activities = new javax.swing.JTable();
        jLabelLogo1 = new javax.swing.JLabel();
        jLabelProvince1 = new javax.swing.JLabel();
        jLabelOffice1 = new javax.swing.JLabel();
        jLabelProvince = new javax.swing.JLabel();
        jLabelDistrict = new javax.swing.JLabel();
        jLabelWk1DateTo = new javax.swing.JLabel();
        jLabelWk1DateFrom = new javax.swing.JLabel();
        jLabelEmp = new javax.swing.JLabel();
        jLabelGenLogNam1 = new javax.swing.JLabel();
        jLabelLineDate = new javax.swing.JLabel();
        jLabelLineTime = new javax.swing.JLabel();
        jLabelLineLogNam = new javax.swing.JLabel();
        jComboSupApproval = new javax.swing.JComboBox<>();
        jLabelHeaderGen6 = new javax.swing.JLabel();
        jLabelRef = new javax.swing.JLabel();
        jLabelSerial = new javax.swing.JLabel();
        jLabelRefNum = new javax.swing.JLabel();
        jPanelWkTwo = new javax.swing.JPanel();
        jLabelWkDuration1 = new javax.swing.JLabel();
        jLabelWk1From1 = new javax.swing.JLabel();
        jLabelWk1To1 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableWk2Activities = new javax.swing.JTable();
        jLabelLogo2 = new javax.swing.JLabel();
        jLabelWk2DateTo = new javax.swing.JLabel();
        jLabelWk2DateFrom = new javax.swing.JLabel();
        jLabelLineDate1 = new javax.swing.JLabel();
        jLabelLineTime1 = new javax.swing.JLabel();
        jLabelGenLogNam3 = new javax.swing.JLabel();
        jLabelLineLogNam1 = new javax.swing.JLabel();
        jLabelHeaderGen7 = new javax.swing.JLabel();
        jPanelWkThree = new javax.swing.JPanel();
        jLabelWkDuration2 = new javax.swing.JLabel();
        jLabelWk3From = new javax.swing.JLabel();
        jLabelWk3To = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableWk3Activities = new javax.swing.JTable();
        jLabelLogo3 = new javax.swing.JLabel();
        jLabelWk3DateTo = new javax.swing.JLabel();
        jLabelWk3DateFrom = new javax.swing.JLabel();
        jLabelLineLogNam2 = new javax.swing.JLabel();
        jLabelGenLogNam5 = new javax.swing.JLabel();
        jLabelLineDate2 = new javax.swing.JLabel();
        jLabelLineTime2 = new javax.swing.JLabel();
        jLabelHeaderGen4 = new javax.swing.JLabel();
        jPanelWkComments = new javax.swing.JPanel();
        jSeparator35 = new javax.swing.JSeparator();
        jLabelLogo4 = new javax.swing.JLabel();
        jLabelLineLogNam3 = new javax.swing.JLabel();
        jLabelGenLogNam6 = new javax.swing.JLabel();
        jLabelLineDate3 = new javax.swing.JLabel();
        jLabelLineTime3 = new javax.swing.JLabel();
        jLabelHeaderGen8 = new javax.swing.JLabel();
        jLabelCommentsHeading = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextAreaComments = new javax.swing.JTextArea();
        jSeparator36 = new javax.swing.JSeparator();
        jPanelWkPrevComments = new javax.swing.JPanel();
        jSeparator37 = new javax.swing.JSeparator();
        jLabelLogo5 = new javax.swing.JLabel();
        jLabelLineLogNam4 = new javax.swing.JLabel();
        jLabelGenLogNam7 = new javax.swing.JLabel();
        jLabelLineDate4 = new javax.swing.JLabel();
        jLabelLineTime4 = new javax.swing.JLabel();
        jLabelHeaderGen9 = new javax.swing.JLabel();
        jLabelCommentsHeading1 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextAreaPrevComments = new javax.swing.JTextArea();
        jSeparator38 = new javax.swing.JSeparator();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuNew = new javax.swing.JMenu();
        jMenuItemRequest = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
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
        jMenuItemSupSubmit = new javax.swing.JMenuItem();
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

        jDialogWk1.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialogWk1.setAlwaysOnTop(true);
        jDialogWk1.setBackground(new java.awt.Color(255, 255, 204));
        jDialogWk1.setLocation(new java.awt.Point(300, 100));
        jDialogWk1.setName("dialogWk1"); // NOI18N
        jDialogWk1.setSize(new java.awt.Dimension(825, 650));

        jPanel1.setBackground(new java.awt.Color(237, 235, 92));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 650));
        jPanel1.setLayout(null);

        jLabelDialogWk1ActivityDate.setText("Activity Date");
        jPanel1.add(jLabelDialogWk1ActivityDate);
        jLabelDialogWk1ActivityDate.setBounds(20, 40, 90, 30);

        jLabelDialogWk1Site.setText("Site");
        jPanel1.add(jLabelDialogWk1Site);
        jLabelDialogWk1Site.setBounds(20, 80, 40, 16);

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
        jScrollPane2.setBounds(20, 460, 500, 100);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel1.add(jSeparator3);
        jSeparator3.setBounds(530, 10, 20, 550);
        jPanel1.add(jLabelWk1DialogActRank);
        jLabelWk1DialogActRank.setBounds(210, 190, 130, 30);

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
        jTextFieldWk1DialogStaffName4.setBounds(570, 240, 230, 30);

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
        jTextFieldWk1DialogStaffName1.setBounds(570, 120, 230, 30);

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
        jTextFieldWk1DialogStaffName2.setBounds(570, 160, 230, 30);

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
        jTextFieldWk1DialogStaffName3.setBounds(570, 200, 230, 30);

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
        jCheckBoxDialogWk1BrkFast.setBounds(550, 300, 90, 25);

        jCheckBoxDialogWk1Lunch.setText("Lunch");
        jPanel1.add(jCheckBoxDialogWk1Lunch);
        jCheckBoxDialogWk1Lunch.setBounds(670, 300, 80, 25);

        jCheckBoxDialogWk1Dinner.setText(" Dinner");
        jPanel1.add(jCheckBoxDialogWk1Dinner);
        jCheckBoxDialogWk1Dinner.setBounds(550, 350, 90, 25);

        jCheckBoxDialogWk1Acc.setText(" Unproved Acc");
        jPanel1.add(jCheckBoxDialogWk1Acc);
        jCheckBoxDialogWk1Acc.setBounds(670, 350, 130, 25);

        jLabelDialogWk1Dis.setText("Km");
        jPanel1.add(jLabelDialogWk1Dis);
        jLabelDialogWk1Dis.setBounds(280, 220, 50, 30);
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
        jCheckBoxDialogWk1Misc.setBounds(670, 400, 140, 25);

        jCheckBoxDialogWk1Inc.setText("Incidental");
        jPanel1.add(jCheckBoxDialogWk1Inc);
        jCheckBoxDialogWk1Inc.setBounds(550, 400, 110, 25);

        jLabelWk1Misc.setText("Miscellaneous Desc");
        jPanel1.add(jLabelWk1Misc);
        jLabelWk1Misc.setBounds(560, 430, 160, 30);
        jPanel1.add(jTextFieldWk1Misc);
        jTextFieldWk1Misc.setBounds(560, 460, 110, 30);

        jLabelWk1MiscAmt.setText("$");
        jPanel1.add(jLabelWk1MiscAmt);
        jLabelWk1MiscAmt.setBounds(690, 460, 30, 30);
        jPanel1.add(jTextFieldWk1MiscAmt);
        jTextFieldWk1MiscAmt.setBounds(730, 460, 70, 30);
        jPanel1.add(jLabelStaff);
        jLabelStaff.setBounds(550, 40, 210, 30);
        jPanel1.add(jLabelActWk1Date);
        jLabelActWk1Date.setBounds(120, 40, 110, 30);

        javax.swing.GroupLayout jDialogWk1Layout = new javax.swing.GroupLayout(jDialogWk1.getContentPane());
        jDialogWk1.getContentPane().setLayout(jDialogWk1Layout);
        jDialogWk1Layout.setHorizontalGroup(
            jDialogWk1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
        );
        jDialogWk1Layout.setVerticalGroup(
            jDialogWk1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 615, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jDialogWk2.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialogWk2.setAlwaysOnTop(true);
        jDialogWk2.setBackground(new java.awt.Color(255, 255, 204));
        jDialogWk2.setLocation(new java.awt.Point(300, 100));
        jDialogWk2.setName("dialogWk1"); // NOI18N
        jDialogWk2.setSize(new java.awt.Dimension(825, 650));

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));
        jPanel2.setPreferredSize(new java.awt.Dimension(800, 650));
        jPanel2.setLayout(null);

        jLabelDialogWk2ActivityDate.setText("Activity Date");
        jPanel2.add(jLabelDialogWk2ActivityDate);
        jLabelDialogWk2ActivityDate.setBounds(20, 40, 90, 30);

        jLabelDialogWk2Site.setText("Site");
        jPanel2.add(jLabelDialogWk2Site);
        jLabelDialogWk2Site.setBounds(20, 80, 40, 16);

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
        jPanel2.add(jLabelWk2DialogActRank);
        jLabelWk2DialogActRank.setBounds(210, 190, 130, 30);

        jLabelDialogPerDiemWk2.setText("Require Per Diem");
        jPanel2.add(jLabelDialogPerDiemWk2);
        jLabelDialogPerDiemWk2.setBounds(550, 10, 130, 30);

        jLabelWk2DialogStaffName.setText("OPHID Staff Only");
        jPanel2.add(jLabelWk2DialogStaffName);
        jLabelWk2DialogStaffName.setBounds(550, 90, 130, 30);

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
        jTextFieldWk2DialogStaffName4.setBounds(570, 240, 230, 30);

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
        jTextFieldWk2DialogStaffName1.setBounds(570, 120, 230, 30);

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
        jTextFieldWk2DialogStaffName2.setBounds(570, 160, 230, 30);

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
        jTextFieldWk2DialogStaffName3.setBounds(570, 200, 230, 30);

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
        jCheckBoxDialogWk2BrkFast.setBounds(550, 300, 90, 25);

        jCheckBoxDialogWk2Lunch.setText("Lunch");
        jPanel2.add(jCheckBoxDialogWk2Lunch);
        jCheckBoxDialogWk2Lunch.setBounds(670, 300, 80, 25);

        jCheckBoxDialogWk2Dinner.setText(" Dinner");
        jPanel2.add(jCheckBoxDialogWk2Dinner);
        jCheckBoxDialogWk2Dinner.setBounds(550, 350, 90, 25);

        jCheckBoxDialogWk2Acc.setText(" Unproved Acc");
        jPanel2.add(jCheckBoxDialogWk2Acc);
        jCheckBoxDialogWk2Acc.setBounds(670, 350, 130, 25);

        jLabelDialogWk2Dis2.setText("Km");
        jPanel2.add(jLabelDialogWk2Dis2);
        jLabelDialogWk2Dis2.setBounds(280, 220, 50, 30);
        jPanel2.add(jLabelWk2DialogActDis);
        jLabelWk2DialogActDis.setBounds(210, 220, 70, 30);

        jLabelDialogWk2Dis.setText("Distance From Base");
        jPanel2.add(jLabelDialogWk2Dis);
        jLabelDialogWk2Dis.setBounds(20, 220, 130, 30);

        jLabelRemain1.setFont(new java.awt.Font("Tahoma", 3, 10)); // NOI18N
        jPanel2.add(jLabelRemain1);
        jLabelRemain1.setBounds(20, 440, 250, 20);

        jCheckBoxDialogWk2Misc.setText("Miscellaneous");
        jCheckBoxDialogWk2Misc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDialogWk2MiscActionPerformed(evt);
            }
        });
        jPanel2.add(jCheckBoxDialogWk2Misc);
        jCheckBoxDialogWk2Misc.setBounds(670, 400, 140, 25);

        jCheckBoxDialogWk2Inc.setText("Incidental");
        jPanel2.add(jCheckBoxDialogWk2Inc);
        jCheckBoxDialogWk2Inc.setBounds(550, 400, 110, 25);

        jLabelWk2Misc.setText("Miscellaneous Desc");
        jPanel2.add(jLabelWk2Misc);
        jLabelWk2Misc.setBounds(560, 430, 160, 30);
        jPanel2.add(jTextFieldWk2Misc);
        jTextFieldWk2Misc.setBounds(560, 460, 110, 30);

        jLabelWk2MiscAmt.setText("$");
        jPanel2.add(jLabelWk2MiscAmt);
        jLabelWk2MiscAmt.setBounds(690, 460, 30, 30);
        jPanel2.add(jTextFieldWk2MiscAmt);
        jTextFieldWk2MiscAmt.setBounds(730, 460, 70, 30);
        jPanel2.add(jLabelStaffWk2);
        jLabelStaffWk2.setBounds(550, 40, 210, 30);
        jPanel2.add(jLabelActWk2Date);
        jLabelActWk2Date.setBounds(120, 40, 110, 30);

        javax.swing.GroupLayout jDialogWk2Layout = new javax.swing.GroupLayout(jDialogWk2.getContentPane());
        jDialogWk2.getContentPane().setLayout(jDialogWk2Layout);
        jDialogWk2Layout.setHorizontalGroup(
            jDialogWk2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
        );
        jDialogWk2Layout.setVerticalGroup(
            jDialogWk2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 615, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jDialogWk3.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialogWk3.setAlwaysOnTop(true);
        jDialogWk3.setBackground(new java.awt.Color(255, 255, 204));
        jDialogWk3.setLocation(new java.awt.Point(300, 100));
        jDialogWk3.setName("dialogWk1"); // NOI18N
        jDialogWk3.setSize(new java.awt.Dimension(825, 650));

        jPanel4.setBackground(new java.awt.Color(84, 110, 240));
        jPanel4.setPreferredSize(new java.awt.Dimension(800, 650));
        jPanel4.setLayout(null);

        jLabelDialogWk3ActivityDate.setText("Activity Date");
        jPanel4.add(jLabelDialogWk3ActivityDate);
        jLabelDialogWk3ActivityDate.setBounds(20, 40, 90, 30);

        jLabelDialogWk3Site.setText("Site");
        jPanel4.add(jLabelDialogWk3Site);
        jLabelDialogWk3Site.setBounds(20, 80, 40, 16);

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
        jPanel4.add(jTextFieldDialogWk3Site);
        jTextFieldDialogWk3Site.setBounds(20, 110, 420, 30);

        jLabelWk3DialogJustification.setText("Justification for Choice of Activity ");
        jPanel4.add(jLabelWk3DialogJustification);
        jLabelWk3DialogJustification.setBounds(20, 410, 340, 30);

        jLabelDialogWk3SiteClass.setText("Site Classification");
        jPanel4.add(jLabelDialogWk3SiteClass);
        jLabelDialogWk3SiteClass.setBounds(20, 160, 130, 30);
        jPanel4.add(jLabelDialogWk3ActSiteClass);
        jLabelDialogWk3ActSiteClass.setBounds(210, 160, 130, 30);

        jLabelDialogWk3Rank.setText("Consortium Rank ");
        jPanel4.add(jLabelDialogWk3Rank);
        jLabelDialogWk3Rank.setBounds(20, 190, 130, 30);

        jLabelWk3DialogStaffName4.setText("4.");
        jPanel4.add(jLabelWk3DialogStaffName4);
        jLabelWk3DialogStaffName4.setBounds(550, 240, 20, 30);
        jPanel4.add(jTextFieldWk3DialogActivityDesc);
        jTextFieldWk3DialogActivityDesc.setBounds(20, 370, 410, 30);

        jLabelWk3DialogActivityDesc.setText("Activity Descrpition");
        jPanel4.add(jLabelWk3DialogActivityDesc);
        jLabelWk3DialogActivityDesc.setBounds(20, 330, 130, 30);

        jTextAreaWk3DialogJustification.setColumns(20);
        jTextAreaWk3DialogJustification.setRows(5);
        jTextAreaWk3DialogJustification.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextAreaWk3DialogJustificationKeyTyped(evt);
            }
        });
        jScrollPane7.setViewportView(jTextAreaWk3DialogJustification);

        jPanel4.add(jScrollPane7);
        jScrollPane7.setBounds(20, 460, 500, 96);

        jSeparator10.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel4.add(jSeparator10);
        jSeparator10.setBounds(530, 10, 20, 550);
        jPanel4.add(jLabelWk3DialogActRank);
        jLabelWk3DialogActRank.setBounds(210, 190, 130, 30);

        jLabelDialogPerDiemWk3.setText("Require Per Diem");
        jPanel4.add(jLabelDialogPerDiemWk3);
        jLabelDialogPerDiemWk3.setBounds(550, 10, 130, 30);

        jLabelWk3DialogStaffName.setText("OPHID Staff Only");
        jPanel4.add(jLabelWk3DialogStaffName);
        jLabelWk3DialogStaffName.setBounds(550, 90, 130, 30);

        jLabelWk3DialogStaffName1.setText("1.");
        jPanel4.add(jLabelWk3DialogStaffName1);
        jLabelWk3DialogStaffName1.setBounds(550, 120, 20, 30);

        jLabelWk3DialogStaffName2.setText("2.");
        jPanel4.add(jLabelWk3DialogStaffName2);
        jLabelWk3DialogStaffName2.setBounds(550, 160, 20, 30);

        jLabelWk3DialogStaffName3.setText("3.");
        jPanel4.add(jLabelWk3DialogStaffName3);
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
        jPanel4.add(jTextFieldWk3DialogStaffName4);
        jTextFieldWk3DialogStaffName4.setBounds(570, 240, 230, 30);

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
        jPanel4.add(jTextFieldWk3DialogStaffName1);
        jTextFieldWk3DialogStaffName1.setBounds(570, 120, 230, 30);

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
        jPanel4.add(jTextFieldWk3DialogStaffName2);
        jTextFieldWk3DialogStaffName2.setBounds(570, 160, 230, 30);

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
        jPanel4.add(jTextFieldWk3DialogStaffName3);
        jTextFieldWk3DialogStaffName3.setBounds(570, 200, 230, 30);

        jLabelWk3DialogBudget.setText("Budget Line");
        jPanel4.add(jLabelWk3DialogBudget);
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
        jPanel4.add(jTextFieldWk3DialogBudget);
        jTextFieldWk3DialogBudget.setBounds(20, 290, 410, 30);

        jCheckBoxDialogWk3BrkFast.setText(" Breakfast");
        jPanel4.add(jCheckBoxDialogWk3BrkFast);
        jCheckBoxDialogWk3BrkFast.setBounds(550, 300, 90, 25);

        jCheckBoxDialogWk3Lunch.setText("Lunch");
        jPanel4.add(jCheckBoxDialogWk3Lunch);
        jCheckBoxDialogWk3Lunch.setBounds(670, 300, 80, 25);

        jCheckBoxDialogWk3Dinner.setText(" Dinner");
        jPanel4.add(jCheckBoxDialogWk3Dinner);
        jCheckBoxDialogWk3Dinner.setBounds(550, 350, 90, 25);

        jCheckBoxDialogWk3Acc.setText(" Unproved Acc");
        jPanel4.add(jCheckBoxDialogWk3Acc);
        jCheckBoxDialogWk3Acc.setBounds(670, 350, 130, 25);

        jLabelDialogWk2Dis3.setText("Km");
        jPanel4.add(jLabelDialogWk2Dis3);
        jLabelDialogWk2Dis3.setBounds(280, 220, 50, 30);
        jPanel4.add(jLabelWk3DialogActDis);
        jLabelWk3DialogActDis.setBounds(210, 220, 70, 30);

        jLabelDialogWk3Dis.setText("Distance From Base");
        jPanel4.add(jLabelDialogWk3Dis);
        jLabelDialogWk3Dis.setBounds(20, 220, 130, 30);

        jLabelRemain3.setFont(new java.awt.Font("Tahoma", 3, 10)); // NOI18N
        jPanel4.add(jLabelRemain3);
        jLabelRemain3.setBounds(20, 440, 250, 20);

        jCheckBoxDialogWk3Misc.setText("Miscellaneous");
        jCheckBoxDialogWk3Misc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDialogWk3MiscActionPerformed(evt);
            }
        });
        jPanel4.add(jCheckBoxDialogWk3Misc);
        jCheckBoxDialogWk3Misc.setBounds(670, 400, 140, 25);

        jCheckBoxDialogWk3Inc.setText("Incidental");
        jPanel4.add(jCheckBoxDialogWk3Inc);
        jCheckBoxDialogWk3Inc.setBounds(550, 400, 110, 25);

        jLabelWk3Misc.setText("Miscellaneous Desc");
        jPanel4.add(jLabelWk3Misc);
        jLabelWk3Misc.setBounds(560, 430, 160, 30);
        jPanel4.add(jTextFieldWk3Misc);
        jTextFieldWk3Misc.setBounds(560, 460, 110, 30);

        jLabelWk3MiscAmt.setText("$");
        jPanel4.add(jLabelWk3MiscAmt);
        jLabelWk3MiscAmt.setBounds(690, 460, 30, 30);
        jPanel4.add(jTextFieldWk3MiscAmt);
        jTextFieldWk3MiscAmt.setBounds(730, 460, 70, 30);
        jPanel4.add(jLabelStaffWk3);
        jLabelStaffWk3.setBounds(550, 40, 210, 30);
        jPanel4.add(jLabelActWk3Date);
        jLabelActWk3Date.setBounds(120, 40, 110, 30);

        javax.swing.GroupLayout jDialogWk3Layout = new javax.swing.GroupLayout(jDialogWk3.getContentPane());
        jDialogWk3.getContentPane().setLayout(jDialogWk3Layout);
        jDialogWk3Layout.setHorizontalGroup(
            jDialogWk3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
        );
        jDialogWk3Layout.setVerticalGroup(
            jDialogWk3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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

        jDialogAmendReqComments.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jDialogAmendReqComments.setTitle("Rejection Comments");
        jDialogAmendReqComments.setAlwaysOnTop(true);
        jDialogAmendReqComments.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jDialogAmendReqComments.setLocation(new java.awt.Point(300, 150));
        jDialogAmendReqComments.setMinimumSize(new java.awt.Dimension(890, 480));
        jDialogAmendReqComments.setResizable(false);

        jPanelDetUpd2.setAlignmentX(5.0F);
        jPanelDetUpd2.setAlignmentY(5.0F);
        jPanelDetUpd2.setMinimumSize(new java.awt.Dimension(890, 480));
        jPanelDetUpd2.setLayout(null);

        jLabelRejectHeader1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelRejectHeader1.setForeground(new java.awt.Color(0, 0, 255));
        jLabelRejectHeader1.setText("PLAN AMMENDMENT REQUEST ");
        jPanelDetUpd2.add(jLabelRejectHeader1);
        jLabelRejectHeader1.setBounds(280, 20, 350, 50);

        jLabelAmmendDesc1.setText("Your plan has been examined by the Provincial Manager and needs to be ammended as shown below:");
        jPanelDetUpd2.add(jLabelAmmendDesc1);
        jLabelAmmendDesc1.setBounds(60, 100, 780, 30);

        jButtonAmmendDialogOk1.setBackground(new java.awt.Color(0, 51, 51));
        jButtonAmmendDialogOk1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonAmmendDialogOk1.setForeground(new java.awt.Color(51, 153, 0));
        jButtonAmmendDialogOk1.setText("Click Here to Proceed");
        jButtonAmmendDialogOk1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAmmendDialogOk1ActionPerformed(evt);
            }
        });
        jPanelDetUpd2.add(jButtonAmmendDialogOk1);
        jButtonAmmendDialogOk1.setBounds(360, 200, 180, 30);

        jTextAmmendRequest.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextAmmendRequestKeyTyped(evt);
            }
        });
        jPanelDetUpd2.add(jTextAmmendRequest);
        jTextAmmendRequest.setBounds(60, 140, 780, 30);

        javax.swing.GroupLayout jDialogAmendReqCommentsLayout = new javax.swing.GroupLayout(jDialogAmendReqComments.getContentPane());
        jDialogAmendReqComments.getContentPane().setLayout(jDialogAmendReqCommentsLayout);
        jDialogAmendReqCommentsLayout.setHorizontalGroup(
            jDialogAmendReqCommentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelDetUpd2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogAmendReqCommentsLayout.setVerticalGroup(
            jDialogAmendReqCommentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelDetUpd2, javax.swing.GroupLayout.PREFERRED_SIZE, 307, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanelWkOne.setBackground(new java.awt.Color(255, 255, 204));
        jPanelWkOne.setLayout(null);

        jLabelWkDuration.setText("Week Duration");
        jPanelWkOne.add(jLabelWkDuration);
        jLabelWkDuration.setBounds(30, 220, 90, 30);

        jLabelWk1From.setText("From");
        jPanelWkOne.add(jLabelWk1From);
        jLabelWk1From.setBounds(150, 220, 41, 30);

        jLabelWk1To.setText("To");
        jPanelWkOne.add(jLabelWk1To);
        jLabelWk1To.setBounds(390, 220, 41, 30);
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
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableWk1Activities.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableWk1ActivitiesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableWk1Activities);

        jPanelWkOne.add(jScrollPane1);
        jScrollPane1.setBounds(30, 270, 1290, 400);

        jLabelLogo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ophidlog.jpg"))); // NOI18N
        jPanelWkOne.add(jLabelLogo1);
        jLabelLogo1.setBounds(10, 10, 220, 100);

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
        jPanelWkOne.add(jLabelProvince);
        jLabelProvince.setBounds(150, 160, 320, 30);
        jPanelWkOne.add(jLabelDistrict);
        jLabelDistrict.setBounds(640, 160, 230, 30);
        jPanelWkOne.add(jLabelWk1DateTo);
        jLabelWk1DateTo.setBounds(440, 220, 100, 30);
        jPanelWkOne.add(jLabelWk1DateFrom);
        jLabelWk1DateFrom.setBounds(200, 220, 100, 30);
        jPanelWkOne.add(jLabelEmp);
        jLabelEmp.setBounds(1110, 60, 50, 20);

        jLabelGenLogNam1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam1.setText("Logged In");
        jPanelWkOne.add(jLabelGenLogNam1);
        jLabelGenLogNam1.setBounds(1090, 30, 100, 30);

        jLabelLineDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkOne.add(jLabelLineDate);
        jLabelLineDate.setBounds(1090, 0, 110, 30);

        jLabelLineTime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkOne.add(jLabelLineTime);
        jLabelLineTime.setBounds(1250, 0, 80, 30);

        jLabelLineLogNam.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkOne.add(jLabelLineLogNam);
        jLabelLineLogNam.setBounds(1180, 30, 160, 30);

        jComboSupApproval.setBackground(new java.awt.Color(0, 102, 0));
        jComboSupApproval.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jComboSupApproval.setForeground(new java.awt.Color(255, 255, 255));
        jComboSupApproval.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Approved", "Request Amendment", "Not Approved" }));
        jComboSupApproval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboSupApprovalActionPerformed(evt);
            }
        });
        jPanelWkOne.add(jComboSupApproval);
        jComboSupApproval.setBounds(1060, 220, 200, 30);

        jLabelHeaderGen6.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen6.setText("MONTHLY  PLAN ");
        jPanelWkOne.add(jLabelHeaderGen6);
        jLabelHeaderGen6.setBounds(450, 40, 420, 40);

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

        jTabbedPaneMain.addTab("Week One", jPanelWkOne);

        jPanelWkTwo.setBackground(new java.awt.Color(95, 222, 184));
        jPanelWkTwo.setLayout(null);

        jLabelWkDuration1.setText("Week Duration");
        jPanelWkTwo.add(jLabelWkDuration1);
        jLabelWkDuration1.setBounds(30, 160, 90, 30);

        jLabelWk1From1.setText("From");
        jPanelWkTwo.add(jLabelWk1From1);
        jLabelWk1From1.setBounds(150, 160, 41, 30);

        jLabelWk1To1.setText("To");
        jPanelWkTwo.add(jLabelWk1To1);
        jLabelWk1To1.setBounds(390, 160, 41, 30);
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
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableWk2Activities.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableWk2ActivitiesMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTableWk2Activities);

        jPanelWkTwo.add(jScrollPane3);
        jScrollPane3.setBounds(30, 220, 1290, 400);

        jLabelLogo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ophidlog.jpg"))); // NOI18N
        jPanelWkTwo.add(jLabelLogo2);
        jLabelLogo2.setBounds(10, 10, 220, 100);
        jPanelWkTwo.add(jLabelWk2DateTo);
        jLabelWk2DateTo.setBounds(440, 160, 100, 30);
        jPanelWkTwo.add(jLabelWk2DateFrom);
        jLabelWk2DateFrom.setBounds(200, 160, 100, 30);

        jLabelLineDate1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkTwo.add(jLabelLineDate1);
        jLabelLineDate1.setBounds(1090, 0, 110, 30);

        jLabelLineTime1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkTwo.add(jLabelLineTime1);
        jLabelLineTime1.setBounds(1250, 0, 80, 30);

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

        jTabbedPaneMain.addTab("Week Two", jPanelWkTwo);

        jPanelWkThree.setBackground(new java.awt.Color(29, 109, 222));
        jPanelWkThree.setLayout(null);

        jLabelWkDuration2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWkDuration2.setForeground(java.awt.Color.white);
        jLabelWkDuration2.setText("Week Duration");
        jPanelWkThree.add(jLabelWkDuration2);
        jLabelWkDuration2.setBounds(30, 160, 110, 30);

        jLabelWk3From.setText("From");
        jPanelWkThree.add(jLabelWk3From);
        jLabelWk3From.setBounds(150, 160, 41, 30);

        jLabelWk3To.setText("To");
        jPanelWkThree.add(jLabelWk3To);
        jLabelWk3To.setBounds(390, 160, 41, 30);
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
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableWk3Activities.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableWk3ActivitiesMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jTableWk3Activities);

        jPanelWkThree.add(jScrollPane5);
        jScrollPane5.setBounds(30, 220, 1290, 402);

        jLabelLogo3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ophidlog.jpg"))); // NOI18N
        jPanelWkThree.add(jLabelLogo3);
        jLabelLogo3.setBounds(10, 10, 220, 100);
        jPanelWkThree.add(jLabelWk3DateTo);
        jLabelWk3DateTo.setBounds(440, 160, 100, 30);
        jPanelWkThree.add(jLabelWk3DateFrom);
        jLabelWk3DateFrom.setBounds(200, 160, 100, 30);

        jLabelLineLogNam2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkThree.add(jLabelLineLogNam2);
        jLabelLineLogNam2.setBounds(1180, 30, 160, 30);

        jLabelGenLogNam5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam5.setForeground(new java.awt.Color(255, 255, 255));
        jLabelGenLogNam5.setText("Logged In");
        jPanelWkThree.add(jLabelGenLogNam5);
        jLabelGenLogNam5.setBounds(1090, 30, 100, 30);

        jLabelLineDate2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkThree.add(jLabelLineDate2);
        jLabelLineDate2.setBounds(1090, 0, 110, 30);

        jLabelLineTime2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkThree.add(jLabelLineTime2);
        jLabelLineTime2.setBounds(1250, 0, 80, 30);

        jLabelHeaderGen4.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen4.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeaderGen4.setText("MONTHLY  PLAN ");
        jPanelWkThree.add(jLabelHeaderGen4);
        jLabelHeaderGen4.setBounds(450, 40, 420, 40);

        jTabbedPaneMain.addTab("Week Three", jPanelWkThree);

        jPanelWkComments.setBackground(new java.awt.Color(209, 54, 127));
        jPanelWkComments.setLayout(null);
        jPanelWkComments.add(jSeparator35);
        jSeparator35.setBounds(30, 150, 1280, 10);

        jLabelLogo4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ophidlog.jpg"))); // NOI18N
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
        jLabelCommentsHeading.setText("Please specify areas that require ammendments in the space provided below");
        jPanelWkComments.add(jLabelCommentsHeading);
        jLabelCommentsHeading.setBounds(30, 160, 590, 40);

        jTextAreaComments.setColumns(20);
        jTextAreaComments.setRows(5);
        jScrollPane6.setViewportView(jTextAreaComments);

        jPanelWkComments.add(jScrollPane6);
        jScrollPane6.setBounds(30, 220, 780, 280);
        jPanelWkComments.add(jSeparator36);
        jSeparator36.setBounds(30, 200, 1280, 10);

        jTabbedPaneMain.addTab("Comments", jPanelWkComments);

        jPanelWkPrevComments.setBackground(new java.awt.Color(217, 192, 250));
        jPanelWkPrevComments.setLayout(null);
        jPanelWkPrevComments.add(jSeparator37);
        jSeparator37.setBounds(30, 150, 1280, 10);

        jLabelLogo5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ophidlog.jpg"))); // NOI18N
        jPanelWkPrevComments.add(jLabelLogo5);
        jLabelLogo5.setBounds(10, 10, 220, 100);

        jLabelLineLogNam4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam4.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkPrevComments.add(jLabelLineLogNam4);
        jLabelLineLogNam4.setBounds(1180, 30, 160, 30);

        jLabelGenLogNam7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam7.setForeground(new java.awt.Color(255, 255, 255));
        jLabelGenLogNam7.setText("Logged In");
        jPanelWkPrevComments.add(jLabelGenLogNam7);
        jLabelGenLogNam7.setBounds(1090, 30, 100, 30);

        jLabelLineDate4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate4.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkPrevComments.add(jLabelLineDate4);
        jLabelLineDate4.setBounds(1090, 0, 110, 30);

        jLabelLineTime4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime4.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkPrevComments.add(jLabelLineTime4);
        jLabelLineTime4.setBounds(1250, 0, 80, 30);

        jLabelHeaderGen9.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen9.setText("MONTHLY  PLAN ");
        jPanelWkPrevComments.add(jLabelHeaderGen9);
        jLabelHeaderGen9.setBounds(450, 40, 420, 40);

        jLabelCommentsHeading1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelCommentsHeading1.setForeground(new java.awt.Color(255, 255, 255));
        jLabelCommentsHeading1.setText("Please find below previous comments and/or amendment requests.");
        jPanelWkPrevComments.add(jLabelCommentsHeading1);
        jLabelCommentsHeading1.setBounds(30, 160, 590, 40);

        jTextAreaPrevComments.setColumns(20);
        jTextAreaPrevComments.setRows(5);
        jScrollPane8.setViewportView(jTextAreaPrevComments);

        jPanelWkPrevComments.add(jScrollPane8);
        jScrollPane8.setBounds(30, 220, 780, 280);
        jPanelWkPrevComments.add(jSeparator38);
        jSeparator38.setBounds(30, 200, 1280, 10);

        jTabbedPaneMain.addTab("Previous Comments", jPanelWkPrevComments);

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
        jMenuNew.add(jSeparator9);

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

        jMenuItemSupSubmit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSupSubmit.setText("Submit");
        jMenuItemSupSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSupSubmitActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemSupSubmit);
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
            .addComponent(jTabbedPaneMain)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneMain)
        );

        setSize(new java.awt.Dimension(1376, 760));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldDialogWk1SiteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk1SiteMouseClicked

    }//GEN-LAST:event_jTextFieldDialogWk1SiteMouseClicked

    private void jTextFieldDialogWk1SiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk1SiteActionPerformed
       
    }//GEN-LAST:event_jTextFieldDialogWk1SiteActionPerformed

    private void jTextFieldDialogWk1SiteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk1SiteKeyTyped

    }//GEN-LAST:event_jTextFieldDialogWk1SiteKeyTyped

    private void jTextAreaWk1DialogJustificationKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAreaWk1DialogJustificationKeyTyped
        jTextAreaWk1DialogJustification.setLineWrap(true);
        jTextAreaWk1DialogJustification.setWrapStyleWord(true);
    }//GEN-LAST:event_jTextAreaWk1DialogJustificationKeyTyped

    private void jTextFieldWk1DialogStaffName4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName4MouseClicked

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName4MouseClicked

    private void jTextFieldWk1DialogStaffName4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName4ActionPerformed
       
    }//GEN-LAST:event_jTextFieldWk1DialogStaffName4ActionPerformed

    private void jTextFieldWk1DialogStaffName4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName4KeyTyped

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName4KeyTyped

    private void jTextFieldWk1DialogStaffName1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName1MouseClicked

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName1MouseClicked

    private void jTextFieldWk1DialogStaffName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName1ActionPerformed

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName1ActionPerformed

    private void jTextFieldWk1DialogStaffName1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName1KeyTyped

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName1KeyTyped

    private void jTextFieldWk1DialogStaffName2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName2MouseClicked

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName2MouseClicked

    private void jTextFieldWk1DialogStaffName2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName2KeyTyped

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName2KeyTyped

    private void jTextFieldWk1DialogStaffName3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName3MouseClicked

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName3MouseClicked

    private void jTextFieldWk1DialogStaffName3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName3KeyTyped

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName3KeyTyped

    private void jTextFieldWk1DialogBudgetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogBudgetMouseClicked

    }//GEN-LAST:event_jTextFieldWk1DialogBudgetMouseClicked

    private void jTextFieldWk1DialogBudgetKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogBudgetKeyTyped

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

    private void jTableWk1ActivitiesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableWk1ActivitiesMouseClicked
        if (evt.getClickCount() == 2) {
            int row = jTableWk1Activities.getSelectedRow();

            int col = 0;
            int col1 = 1;
            int col2 = 2;
            int col3 = 3;
            int col4 = 4;
            int col5 = 5;
            int col6 = 6;
            int col7 = 7;
            int col8 = 8;
            int col9 = 9;
            int col10 = 10;
            int col11 = 11;
            int col12 = 12;
            int col13 = 13;
            int col14 = 14;
            int col15 = 15;
            int col16 = 16;
            int col17 = 17;
            int col18 = 18;
            int col19 = 19;
            int col20 = 20;

            Object id = jTableWk1Activities.getValueAt(row, col);
            Object id1 = jTableWk1Activities.getValueAt(row, col1);
            Object id2 = jTableWk1Activities.getValueAt(row, col2);
            Object id3 = jTableWk1Activities.getValueAt(row, col3);
            Object id4 = jTableWk1Activities.getValueAt(row, col4);
            Object id5 = jTableWk1Activities.getValueAt(row, col5);
            Object id6 = jTableWk1Activities.getValueAt(row, col6);
            Object id7 = jTableWk1Activities.getValueAt(row, col7);
            Object id8 = jTableWk1Activities.getValueAt(row, col8);
            Object id9 = jTableWk1Activities.getValueAt(row, col9);
            Object id10 = jTableWk1Activities.getValueAt(row, col10);
            Object id11 = jTableWk1Activities.getValueAt(row, col11);
            Object id12 = jTableWk1Activities.getValueAt(row, col12);
            Object id13 = jTableWk1Activities.getValueAt(row, col13);
            Object id14 = jTableWk1Activities.getValueAt(row, col14);
            Object id15 = jTableWk1Activities.getValueAt(row, col15);
            Object id16 = jTableWk1Activities.getValueAt(row, col16);
            Object id17 = jTableWk1Activities.getValueAt(row, col17);
            Object id18 = jTableWk1Activities.getValueAt(row, col18);
            Object id19 = jTableWk1Activities.getValueAt(row, col19);
            //      Object id20 = jTableWk1Activities.getValueAt(row, col20);

            jLabelActWk1Date.setText(id.toString());
            jTextFieldDialogWk1Site.setText(id1.toString());
            jLabelDialogWk1ActSiteClass.setText(id2.toString());
            jLabelWk1DialogActRank.setText(id3.toString());
            jLabelWk1DialogActDis.setText(id4.toString());
            jTextFieldWk1DialogActivityDesc.setText(id5.toString());
            jTextAreaWk1DialogJustification.setText(id6.toString());
            jLabelStaff.setText(id7.toString());
            jTextFieldWk1DialogBudget.setText(id8.toString());
            if ((Double.parseDouble(id9.toString())) > 0) {
                jCheckBoxDialogWk1BrkFast.setSelected(true);
            }
            if ((Double.parseDouble(id10.toString())) > 0) {
                jCheckBoxDialogWk1Lunch.setSelected(true);
            }
            if ((Double.parseDouble(id11.toString())) > 0) {
                jCheckBoxDialogWk1Dinner.setSelected(true);
            }
            if ((Double.parseDouble(id12.toString())) > 0) {
                jCheckBoxDialogWk1Inc.setSelected(true);
            }

            if ((Double.parseDouble(id14.toString())) > 0) {
                jCheckBoxDialogWk1Misc.setSelected(true);
            }
            jTextFieldWk1Misc.setText(id13.toString());
            jTextFieldWk1MiscAmt.setText(id14.toString());
            if ((Double.parseDouble(id15.toString())) > 0) {
                jCheckBoxDialogWk1Acc.setSelected(true);

            }
            jTextFieldWk1DialogStaffName1.setText(id16.toString());
            jTextFieldWk1DialogStaffName2.setText(id17.toString());
            jTextFieldWk1DialogStaffName3.setText(id18.toString());
            jTextFieldWk1DialogStaffName4.setText(id19.toString());

            jDialogWk1.setVisible(true);
            jTextAreaWk1DialogJustification.setLineWrap(true);
            jTextAreaWk1DialogJustification.setWrapStyleWord(true);
            jTextFieldDialogWk1Site.setEditable(false);
            jTextFieldWk1DialogBudget.setEditable(false);
            jTextFieldWk1DialogActivityDesc.setEditable(false);
            jTextAreaWk1DialogJustification.setEditable(false);
            jTextFieldWk1DialogStaffName1.setEditable(false);
            jTextFieldWk1DialogStaffName2.setEditable(false);
            jTextFieldWk1DialogStaffName3.setEditable(false);
            jTextFieldWk1DialogStaffName4.setEditable(false);
            jTextFieldWk1Misc.setEditable(false);
            jTextFieldWk1MiscAmt.setEditable(false);
            jCheckBoxDialogWk1BrkFast.setEnabled(false);
            jCheckBoxDialogWk1Lunch.setEnabled(false);
            jCheckBoxDialogWk1Dinner.setEnabled(false);
            jCheckBoxDialogWk1Inc.setEnabled(false);
            jCheckBoxDialogWk1Acc.setEnabled(false);
            jCheckBoxDialogWk1Misc.setEnabled(false);

        }
    }//GEN-LAST:event_jTableWk1ActivitiesMouseClicked

    private void jTextFieldDialogWk2SiteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk2SiteMouseClicked
       
    }//GEN-LAST:event_jTextFieldDialogWk2SiteMouseClicked

    private void jTextFieldDialogWk2SiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk2SiteActionPerformed
       
    }//GEN-LAST:event_jTextFieldDialogWk2SiteActionPerformed

    private void jTextFieldDialogWk2SiteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk2SiteKeyTyped
       
    }//GEN-LAST:event_jTextFieldDialogWk2SiteKeyTyped

    private void jTextAreaWk2DialogJustificationKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAreaWk2DialogJustificationKeyTyped
        jTextAreaWk2DialogJustification.setLineWrap(true);
        jTextAreaWk2DialogJustification.setWrapStyleWord(true);
    }//GEN-LAST:event_jTextAreaWk2DialogJustificationKeyTyped

    private void jTextFieldWk2DialogStaffName4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogStaffName4MouseClicked
       
    }//GEN-LAST:event_jTextFieldWk2DialogStaffName4MouseClicked

    private void jTextFieldWk2DialogStaffName4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogStaffName4ActionPerformed
       
    }//GEN-LAST:event_jTextFieldWk2DialogStaffName4ActionPerformed

    private void jTextFieldWk2DialogStaffName4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogStaffName4KeyTyped
       
    }//GEN-LAST:event_jTextFieldWk2DialogStaffName4KeyTyped

    private void jTextFieldWk2DialogStaffName1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogStaffName1MouseClicked
       
    }//GEN-LAST:event_jTextFieldWk2DialogStaffName1MouseClicked

    private void jTextFieldWk2DialogStaffName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogStaffName1ActionPerformed
       
    }//GEN-LAST:event_jTextFieldWk2DialogStaffName1ActionPerformed

    private void jTextFieldWk2DialogStaffName1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogStaffName1KeyTyped
       
    }//GEN-LAST:event_jTextFieldWk2DialogStaffName1KeyTyped

    private void jTextFieldWk2DialogStaffName2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogStaffName2MouseClicked
       
    }//GEN-LAST:event_jTextFieldWk2DialogStaffName2MouseClicked

    private void jTextFieldWk2DialogStaffName2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogStaffName2KeyTyped
       
    }//GEN-LAST:event_jTextFieldWk2DialogStaffName2KeyTyped

    private void jTextFieldWk2DialogStaffName3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogStaffName3MouseClicked
       
    }//GEN-LAST:event_jTextFieldWk2DialogStaffName3MouseClicked

    private void jTextFieldWk2DialogStaffName3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogStaffName3KeyTyped
       
    }//GEN-LAST:event_jTextFieldWk2DialogStaffName3KeyTyped

    private void jTextFieldWk2DialogBudgetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogBudgetMouseClicked
       
    }//GEN-LAST:event_jTextFieldWk2DialogBudgetMouseClicked

    private void jTextFieldWk2DialogBudgetKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogBudgetKeyTyped
       
    }//GEN-LAST:event_jTextFieldWk2DialogBudgetKeyTyped

    private void jCheckBoxDialogWk2MiscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxDialogWk2MiscActionPerformed
       
    }//GEN-LAST:event_jCheckBoxDialogWk2MiscActionPerformed

    private void jTableWk2ActivitiesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableWk2ActivitiesMouseClicked
        if (evt.getClickCount() == 2) {
            int row = jTableWk2Activities.getSelectedRow();

            int col = 0;
            int col1 = 1;
            int col2 = 2;
            int col3 = 3;
            int col4 = 4;
            int col5 = 5;
            int col6 = 6;
            int col7 = 7;
            int col8 = 8;
            int col9 = 9;
            int col10 = 10;
            int col11 = 11;
            int col12 = 12;
            int col13 = 13;
            int col14 = 14;
            int col15 = 15;
            int col16 = 16;
            int col17 = 17;
            int col18 = 18;
            int col19 = 19;
            int col20 = 20;

            Object id = jTableWk2Activities.getValueAt(row, col);
            Object id1 = jTableWk2Activities.getValueAt(row, col1);
            Object id2 = jTableWk2Activities.getValueAt(row, col2);
            Object id3 = jTableWk2Activities.getValueAt(row, col3);
            Object id4 = jTableWk2Activities.getValueAt(row, col4);
            Object id5 = jTableWk2Activities.getValueAt(row, col5);
            Object id6 = jTableWk2Activities.getValueAt(row, col6);
            Object id7 = jTableWk2Activities.getValueAt(row, col7);
            Object id8 = jTableWk2Activities.getValueAt(row, col8);
            Object id9 = jTableWk2Activities.getValueAt(row, col9);
            Object id10 = jTableWk2Activities.getValueAt(row, col10);
            Object id11 = jTableWk2Activities.getValueAt(row, col11);
            Object id12 = jTableWk2Activities.getValueAt(row, col12);
            Object id13 = jTableWk2Activities.getValueAt(row, col13);
            Object id14 = jTableWk2Activities.getValueAt(row, col14);
            Object id15 = jTableWk2Activities.getValueAt(row, col15);
            Object id16 = jTableWk2Activities.getValueAt(row, col16);
            Object id17 = jTableWk2Activities.getValueAt(row, col17);
            Object id18 = jTableWk2Activities.getValueAt(row, col18);
            Object id19 = jTableWk2Activities.getValueAt(row, col19);
            //      Object id20 = jTableWk2Activities.getValueAt(row, col20);

            jLabelActWk2Date.setText(id.toString());
            jTextFieldDialogWk2Site.setText(id1.toString());
            jLabelDialogWk2ActSiteClass.setText(id2.toString());
            jLabelWk2DialogActRank.setText(id3.toString());
            jLabelWk2DialogActDis.setText(id4.toString());
            jTextFieldWk2DialogActivityDesc.setText(id5.toString());
            jTextAreaWk2DialogJustification.setText(id6.toString());
            jLabelStaffWk2.setText(id7.toString());
            jTextFieldWk2DialogBudget.setText(id8.toString());
            if ((Double.parseDouble(id9.toString())) > 0) {
                jCheckBoxDialogWk2BrkFast.setSelected(true);
            }
            if ((Double.parseDouble(id10.toString())) > 0) {
                jCheckBoxDialogWk2Lunch.setSelected(true);
            }
            if ((Double.parseDouble(id11.toString())) > 0) {
                jCheckBoxDialogWk2Dinner.setSelected(true);
            }
            if ((Double.parseDouble(id12.toString())) > 0) {
                jCheckBoxDialogWk2Inc.setSelected(true);
            }

            if ((Double.parseDouble(id14.toString())) > 0) {
                jCheckBoxDialogWk2Misc.setSelected(true);
            }
            jTextFieldWk2Misc.setText(id13.toString());
            jTextFieldWk2MiscAmt.setText(id14.toString());
            if ((Double.parseDouble(id15.toString())) > 0) {
                jCheckBoxDialogWk2Acc.setSelected(true);

            }
            jTextFieldWk2DialogStaffName1.setText(id16.toString());
            jTextFieldWk2DialogStaffName2.setText(id17.toString());
            jTextFieldWk2DialogStaffName3.setText(id18.toString());
            jTextFieldWk2DialogStaffName4.setText(id19.toString());

            jDialogWk2.setVisible(true);
            jTextAreaWk2DialogJustification.setLineWrap(true);
            jTextAreaWk2DialogJustification.setWrapStyleWord(true);
            jTextFieldDialogWk2Site.setEditable(false);
            jTextFieldWk2DialogBudget.setEditable(false);
            jTextFieldWk2DialogActivityDesc.setEditable(false);
            jTextAreaWk2DialogJustification.setEditable(false);
            jTextFieldWk2DialogStaffName1.setEditable(false);
            jTextFieldWk2DialogStaffName2.setEditable(false);
            jTextFieldWk2DialogStaffName3.setEditable(false);
            jTextFieldWk2DialogStaffName4.setEditable(false);
            jTextFieldWk2Misc.setEditable(false);
            jTextFieldWk2MiscAmt.setEditable(false);
            jCheckBoxDialogWk2BrkFast.setEnabled(false);
            jCheckBoxDialogWk2Lunch.setEnabled(false);
            jCheckBoxDialogWk2Dinner.setEnabled(false);
            jCheckBoxDialogWk2Inc.setEnabled(false);
            jCheckBoxDialogWk2Acc.setEnabled(false);
            jCheckBoxDialogWk2Misc.setEnabled(false);

        }
    }//GEN-LAST:event_jTableWk2ActivitiesMouseClicked

    private void jTextFieldDialogWk3SiteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk3SiteMouseClicked
       
    }//GEN-LAST:event_jTextFieldDialogWk3SiteMouseClicked

    private void jTextFieldDialogWk3SiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk3SiteActionPerformed
       
    }//GEN-LAST:event_jTextFieldDialogWk3SiteActionPerformed

    private void jTextFieldDialogWk3SiteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk3SiteKeyTyped
       
    }//GEN-LAST:event_jTextFieldDialogWk3SiteKeyTyped

    private void jTextAreaWk3DialogJustificationKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAreaWk3DialogJustificationKeyTyped
        jTextAreaWk3DialogJustification.setLineWrap(true);
        jTextAreaWk3DialogJustification.setWrapStyleWord(true);
    }//GEN-LAST:event_jTextAreaWk3DialogJustificationKeyTyped

    private void jTextFieldWk3DialogStaffName4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogStaffName4MouseClicked
       
    }//GEN-LAST:event_jTextFieldWk3DialogStaffName4MouseClicked

    private void jTextFieldWk3DialogStaffName4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogStaffName4ActionPerformed
       
    }//GEN-LAST:event_jTextFieldWk3DialogStaffName4ActionPerformed

    private void jTextFieldWk3DialogStaffName4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogStaffName4KeyTyped
       
    }//GEN-LAST:event_jTextFieldWk3DialogStaffName4KeyTyped

    private void jTextFieldWk3DialogStaffName1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogStaffName1MouseClicked
       
    }//GEN-LAST:event_jTextFieldWk3DialogStaffName1MouseClicked

    private void jTextFieldWk3DialogStaffName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogStaffName1ActionPerformed
       
    }//GEN-LAST:event_jTextFieldWk3DialogStaffName1ActionPerformed

    private void jTextFieldWk3DialogStaffName1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogStaffName1KeyTyped
       
    }//GEN-LAST:event_jTextFieldWk3DialogStaffName1KeyTyped

    private void jTextFieldWk3DialogStaffName2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogStaffName2MouseClicked
       
    }//GEN-LAST:event_jTextFieldWk3DialogStaffName2MouseClicked

    private void jTextFieldWk3DialogStaffName2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogStaffName2KeyTyped
       
    }//GEN-LAST:event_jTextFieldWk3DialogStaffName2KeyTyped

    private void jTextFieldWk3DialogStaffName3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogStaffName3MouseClicked
       
    }//GEN-LAST:event_jTextFieldWk3DialogStaffName3MouseClicked

    private void jTextFieldWk3DialogStaffName3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogStaffName3KeyTyped
       
    }//GEN-LAST:event_jTextFieldWk3DialogStaffName3KeyTyped

    private void jTextFieldWk3DialogBudgetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogBudgetMouseClicked
       
    }//GEN-LAST:event_jTextFieldWk3DialogBudgetMouseClicked

    private void jTextFieldWk3DialogBudgetKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogBudgetKeyTyped
       
    }//GEN-LAST:event_jTextFieldWk3DialogBudgetKeyTyped

    private void jCheckBoxDialogWk3MiscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxDialogWk3MiscActionPerformed
       
    }//GEN-LAST:event_jCheckBoxDialogWk3MiscActionPerformed

    private void jTableWk3ActivitiesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableWk3ActivitiesMouseClicked
        if (evt.getClickCount() == 2) {
            int row = jTableWk3Activities.getSelectedRow();

            int col = 0;
            int col1 = 1;
            int col2 = 2;
            int col3 = 3;
            int col4 = 4;
            int col5 = 5;
            int col6 = 6;
            int col7 = 7;
            int col8 = 8;
            int col9 = 9;
            int col10 = 10;
            int col11 = 11;
            int col12 = 12;
            int col13 = 13;
            int col14 = 14;
            int col15 = 15;
            int col16 = 16;
            int col17 = 17;
            int col18 = 18;
            int col19 = 19;
            int col20 = 20;

            Object id = jTableWk3Activities.getValueAt(row, col);
            Object id1 = jTableWk3Activities.getValueAt(row, col1);
            Object id2 = jTableWk3Activities.getValueAt(row, col2);
            Object id3 = jTableWk3Activities.getValueAt(row, col3);
            Object id4 = jTableWk3Activities.getValueAt(row, col4);
            Object id5 = jTableWk3Activities.getValueAt(row, col5);
            Object id6 = jTableWk3Activities.getValueAt(row, col6);
            Object id7 = jTableWk3Activities.getValueAt(row, col7);
            Object id8 = jTableWk3Activities.getValueAt(row, col8);
            Object id9 = jTableWk3Activities.getValueAt(row, col9);
            Object id10 = jTableWk3Activities.getValueAt(row, col10);
            Object id11 = jTableWk3Activities.getValueAt(row, col11);
            Object id12 = jTableWk3Activities.getValueAt(row, col12);
            Object id13 = jTableWk3Activities.getValueAt(row, col13);
            Object id14 = jTableWk3Activities.getValueAt(row, col14);
            Object id15 = jTableWk3Activities.getValueAt(row, col15);
            Object id16 = jTableWk3Activities.getValueAt(row, col16);
            Object id17 = jTableWk3Activities.getValueAt(row, col17);
            Object id18 = jTableWk3Activities.getValueAt(row, col18);
            Object id19 = jTableWk3Activities.getValueAt(row, col19);
            //      Object id20 = jTableWk3Activities.getValueAt(row, col20);

            jLabelActWk3Date.setText(id.toString());
            jTextFieldDialogWk3Site.setText(id1.toString());
            jLabelDialogWk3ActSiteClass.setText(id2.toString());
            jLabelWk3DialogActRank.setText(id3.toString());
            jLabelWk3DialogActDis.setText(id4.toString());
            jTextFieldWk3DialogActivityDesc.setText(id5.toString());
            jTextAreaWk3DialogJustification.setText(id6.toString());
            jLabelStaffWk3.setText(id7.toString());
            jTextFieldWk3DialogBudget.setText(id8.toString());
            if ((Double.parseDouble(id9.toString())) > 0) {
                jCheckBoxDialogWk3BrkFast.setSelected(true);
            }
            if ((Double.parseDouble(id10.toString())) > 0) {
                jCheckBoxDialogWk3Lunch.setSelected(true);
            }
            if ((Double.parseDouble(id11.toString())) > 0) {
                jCheckBoxDialogWk3Dinner.setSelected(true);
            }
            if ((Double.parseDouble(id12.toString())) > 0) {
                jCheckBoxDialogWk3Inc.setSelected(true);
            }

            if ((Double.parseDouble(id14.toString())) > 0) {
                jCheckBoxDialogWk3Misc.setSelected(true);
            }
            jTextFieldWk3Misc.setText(id13.toString());
            jTextFieldWk3MiscAmt.setText(id14.toString());
            if ((Double.parseDouble(id15.toString())) > 0) {
                jCheckBoxDialogWk3Acc.setSelected(true);

            }
            jTextFieldWk3DialogStaffName1.setText(id16.toString());
            jTextFieldWk3DialogStaffName2.setText(id17.toString());
            jTextFieldWk3DialogStaffName3.setText(id18.toString());
            jTextFieldWk3DialogStaffName4.setText(id19.toString());

            jDialogWk3.setVisible(true);
            jTextAreaWk1DialogJustification.setLineWrap(true);
            jTextAreaWk1DialogJustification.setWrapStyleWord(true);
            jTextFieldDialogWk3Site.setEditable(false);
            jTextFieldWk3DialogBudget.setEditable(false);
            jTextFieldWk3DialogActivityDesc.setEditable(false);
            jTextAreaWk3DialogJustification.setEditable(false);
            jTextFieldWk3DialogStaffName1.setEditable(false);
            jTextFieldWk3DialogStaffName2.setEditable(false);
            jTextFieldWk3DialogStaffName3.setEditable(false);
            jTextFieldWk3DialogStaffName4.setEditable(false);
            jTextFieldWk3Misc.setEditable(false);
            jTextFieldWk3MiscAmt.setEditable(false);
            jCheckBoxDialogWk3BrkFast.setEnabled(false);
            jCheckBoxDialogWk3Lunch.setEnabled(false);
            jCheckBoxDialogWk3Dinner.setEnabled(false);
            jCheckBoxDialogWk3Inc.setEnabled(false);
            jCheckBoxDialogWk3Acc.setEnabled(false);
            jCheckBoxDialogWk3Misc.setEnabled(false);

        }
    }//GEN-LAST:event_jTableWk3ActivitiesMouseClicked

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

    private void jMenuItemSupSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSupSubmitActionPerformed

        if ((("Request Amendment".equals(jComboSupApproval.getSelectedItem().toString()))
                || ("Not Approved".equals(jComboSupApproval.getSelectedItem().toString())))
                && "".equals(jTextAreaComments.getText())) {
            JOptionPane.showMessageDialog(this, "<html><b>Comments Tab cannot be blank.Please record comments.</html>");
            jTabbedPaneMain.setSelectedIndex(3);
            jTextAreaComments.requestFocusInWindow();
            jTextAreaComments.setFocusable(true);
        } else if ("Approved".equals(jComboSupApproval.getSelectedItem().toString())) {
            updatePrevRecord();
            updateWk1Plan();
            updateWk2Plan();
            updateWk3Plan();
            updateWkPlanPeriod();
            WkPlanActionApproved();
            JOptionPane.showMessageDialog(this, "<html>Plan reference No. <b>" + jLabelSerial.getText() + " " + jLabelRefNum.getText()
                    + "</b> successfully updated.</html>");
            sendMail();
            jDialogWaitingEmail.setVisible(false);
            new JFrameMnthSupList(jLabelEmp.getText()).setVisible(true);
            setVisible(false);

        } else if ("Request Amendment".equals(jComboSupApproval.getSelectedItem().toString())) {
            // jDialogAmendReqComments.setVisible(true);
            updatePrevRecord();
            updateWk1Plan();
            updateWk2Plan();
            updateWk3Plan();
            updateWkPlanPeriod();
            WkPlanActionAmendRequest();
            JOptionPane.showMessageDialog(this, "<html>Plan reference No. <b>" + jLabelSerial.getText() + " " + jLabelRefNum.getText()
                    + "</b> successfully updated.</html>");
            sendMail();
            jDialogWaitingEmail.setVisible(false);
            new JFrameMnthSupList(jLabelEmp.getText()).setVisible(true);
            setVisible(false);

        } else if ("Not Approved".equals(jComboSupApproval.getSelectedItem().toString())) {
            updatePrevRecord();
            updateWk1Plan();
            updateWk2Plan();
            updateWk3Plan();
            updateWkPlanPeriod();
            WkPlanActionDisApproved();
            JOptionPane.showMessageDialog(this, "<html>Plan reference No. <b>" + jLabelSerial.getText() + " " + jLabelRefNum.getText()
                    + "</b> successfully updated.</html>");
            sendMail();
            jDialogWaitingEmail.setVisible(false);
            new JFrameMnthSupList(jLabelEmp.getText()).setVisible(true);
            setVisible(false);
        }
    }//GEN-LAST:event_jMenuItemSupSubmitActionPerformed

    private void jButtonAmmendDialogOk1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAmmendDialogOk1ActionPerformed

        if ("".equals(jTextAmmendRequest.getText())) {
            JOptionPane.showMessageDialog(this, "Please indicate areas that require ammendements.");

            jTextAmmendRequest.requestFocusInWindow();
            jTextAmmendRequest.setFocusable(true);
        } else {
//            jDialogTSAmmendComments.setVisible(false);
            //ammendRequest();

            //       sendMailAmmnend();
//            jDialogWaitingEmail.setVisible(false);
//
//            JOptionPane.showMessageDialog(this, "<html>TimeSheet successfully actioned with status<b> "
//                + jComboBoxSupApprove.getSelectedItem().toString() + "</html>");
//            new JFrameTSSupList(Integer.parseInt(jLabelLogEmpNum.getText())).setVisible(true);
//            setVisible(false);
        }
    }//GEN-LAST:event_jButtonAmmendDialogOk1ActionPerformed

    private void jTextAmmendRequestKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAmmendRequestKeyTyped
        //   jButtonAmmendDialogOk.setVisible(true);
    }//GEN-LAST:event_jTextAmmendRequestKeyTyped

    private void jComboSupApprovalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboSupApprovalActionPerformed
        if (("Request Amendment".equals(jComboSupApproval.getSelectedItem().toString()))
                || ("Not Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
            jTabbedPaneMain.setEnabledAt(3, true);
            JOptionPane.showMessageDialog(this, "<html><b>Please record comments and/or amendment request in the comments tab.</html>");
            jTabbedPaneMain.setSelectedIndex(3);
            jTextAreaComments.requestFocusInWindow();
            jTextAreaComments.setFocusable(true);
            jTextAreaComments.setLineWrap(true);
            jTextAreaComments.setWrapStyleWord(true);
        } else {
            jTabbedPaneMain.setEnabledAt(3, false);
        }
    }//GEN-LAST:event_jComboSupApprovalActionPerformed

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
        new JFrameMnthViewList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemPlanViewActionPerformed

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
            java.util.logging.Logger.getLogger(JFrameMnthPlanView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthPlanView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthPlanView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthPlanView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
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
                new JFrameMnthPlanView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAmmendDialogOk1;
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
    private javax.swing.JComboBox<String> jComboSupApproval;
    private javax.swing.JDialog jDialogAmendReqComments;
    private javax.swing.JDialog jDialogWaitingEmail;
    private javax.swing.JDialog jDialogWk1;
    private javax.swing.JDialog jDialogWk2;
    private javax.swing.JDialog jDialogWk3;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabelActWk1Date;
    private javax.swing.JLabel jLabelActWk2Date;
    private javax.swing.JLabel jLabelActWk3Date;
    private javax.swing.JLabel jLabelAmmendDesc1;
    private javax.swing.JLabel jLabelCommentsHeading;
    private javax.swing.JLabel jLabelCommentsHeading1;
    private javax.swing.JLabel jLabelDialogPerDiem;
    private javax.swing.JLabel jLabelDialogPerDiemWk2;
    private javax.swing.JLabel jLabelDialogPerDiemWk3;
    private javax.swing.JLabel jLabelDialogWk1ActSiteClass;
    private javax.swing.JLabel jLabelDialogWk1ActivityDate;
    private javax.swing.JLabel jLabelDialogWk1Dis;
    private javax.swing.JLabel jLabelDialogWk1Dis1;
    private javax.swing.JLabel jLabelDialogWk1Rank;
    private javax.swing.JLabel jLabelDialogWk1Site;
    private javax.swing.JLabel jLabelDialogWk1SiteClass;
    private javax.swing.JLabel jLabelDialogWk2ActSiteClass;
    private javax.swing.JLabel jLabelDialogWk2ActivityDate;
    private javax.swing.JLabel jLabelDialogWk2Dis;
    private javax.swing.JLabel jLabelDialogWk2Dis2;
    private javax.swing.JLabel jLabelDialogWk2Dis3;
    private javax.swing.JLabel jLabelDialogWk2Rank;
    private javax.swing.JLabel jLabelDialogWk2Site;
    private javax.swing.JLabel jLabelDialogWk2SiteClass;
    private javax.swing.JLabel jLabelDialogWk3ActSiteClass;
    private javax.swing.JLabel jLabelDialogWk3ActivityDate;
    private javax.swing.JLabel jLabelDialogWk3Dis;
    private javax.swing.JLabel jLabelDialogWk3Rank;
    private javax.swing.JLabel jLabelDialogWk3Site;
    private javax.swing.JLabel jLabelDialogWk3SiteClass;
    private javax.swing.JLabel jLabelDistrict;
    private javax.swing.JLabel jLabelEmp;
    private javax.swing.JLabel jLabelGenLogNam1;
    private javax.swing.JLabel jLabelGenLogNam3;
    private javax.swing.JLabel jLabelGenLogNam5;
    private javax.swing.JLabel jLabelGenLogNam6;
    private javax.swing.JLabel jLabelGenLogNam7;
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
    private javax.swing.JLabel jLabelLineLogNam;
    private javax.swing.JLabel jLabelLineLogNam1;
    private javax.swing.JLabel jLabelLineLogNam2;
    private javax.swing.JLabel jLabelLineLogNam3;
    private javax.swing.JLabel jLabelLineLogNam4;
    private javax.swing.JLabel jLabelLineTime;
    private javax.swing.JLabel jLabelLineTime1;
    private javax.swing.JLabel jLabelLineTime2;
    private javax.swing.JLabel jLabelLineTime3;
    private javax.swing.JLabel jLabelLineTime4;
    private javax.swing.JLabel jLabelLogo1;
    private javax.swing.JLabel jLabelLogo2;
    private javax.swing.JLabel jLabelLogo3;
    private javax.swing.JLabel jLabelLogo4;
    private javax.swing.JLabel jLabelLogo5;
    private javax.swing.JLabel jLabelOffice1;
    private javax.swing.JLabel jLabelProvince;
    private javax.swing.JLabel jLabelProvince1;
    private javax.swing.JLabel jLabelRef;
    private javax.swing.JLabel jLabelRefNum;
    private javax.swing.JLabel jLabelRejectHeader1;
    private javax.swing.JLabel jLabelRemain;
    private javax.swing.JLabel jLabelRemain1;
    private javax.swing.JLabel jLabelRemain3;
    private javax.swing.JLabel jLabelSerial;
    private javax.swing.JLabel jLabelStaff;
    private javax.swing.JLabel jLabelStaffWk2;
    private javax.swing.JLabel jLabelStaffWk3;
    private javax.swing.JLabel jLabelWk1DateFrom;
    private javax.swing.JLabel jLabelWk1DateTo;
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
    private javax.swing.JLabel jLabelWk1To;
    private javax.swing.JLabel jLabelWk1To1;
    private javax.swing.JLabel jLabelWk2DateFrom;
    private javax.swing.JLabel jLabelWk2DateTo;
    private javax.swing.JLabel jLabelWk2DialogActDis;
    private javax.swing.JLabel jLabelWk2DialogActRank;
    private javax.swing.JLabel jLabelWk2DialogActivityDesc;
    private javax.swing.JLabel jLabelWk2DialogBudget;
    private javax.swing.JLabel jLabelWk2DialogJustification;
    private javax.swing.JLabel jLabelWk2DialogStaffName;
    private javax.swing.JLabel jLabelWk2DialogStaffName1;
    private javax.swing.JLabel jLabelWk2DialogStaffName2;
    private javax.swing.JLabel jLabelWk2DialogStaffName3;
    private javax.swing.JLabel jLabelWk2DialogStaffName4;
    private javax.swing.JLabel jLabelWk2Misc;
    private javax.swing.JLabel jLabelWk2MiscAmt;
    private javax.swing.JLabel jLabelWk3DateFrom;
    private javax.swing.JLabel jLabelWk3DateTo;
    private javax.swing.JLabel jLabelWk3DialogActDis;
    private javax.swing.JLabel jLabelWk3DialogActRank;
    private javax.swing.JLabel jLabelWk3DialogActivityDesc;
    private javax.swing.JLabel jLabelWk3DialogBudget;
    private javax.swing.JLabel jLabelWk3DialogJustification;
    private javax.swing.JLabel jLabelWk3DialogStaffName;
    private javax.swing.JLabel jLabelWk3DialogStaffName1;
    private javax.swing.JLabel jLabelWk3DialogStaffName2;
    private javax.swing.JLabel jLabelWk3DialogStaffName3;
    private javax.swing.JLabel jLabelWk3DialogStaffName4;
    private javax.swing.JLabel jLabelWk3From;
    private javax.swing.JLabel jLabelWk3Misc;
    private javax.swing.JLabel jLabelWk3MiscAmt;
    private javax.swing.JLabel jLabelWk3To;
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
    private javax.swing.JMenuItem jMenuItemSupSubmit;
    private javax.swing.JMenuItem jMenuItemView;
    private javax.swing.JMenuItem jMenuMonPlanEdit;
    private javax.swing.JMenu jMenuMonthlyPlan;
    private javax.swing.JMenu jMenuNew;
    private javax.swing.JMenuItem jMenuProcessTimeline;
    private javax.swing.JMenu jMenuReports;
    private javax.swing.JMenuItem jMenuReqEdit;
    private javax.swing.JMenu jMenuRequest;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelDetUpd2;
    private javax.swing.JPanel jPanelWkComments;
    private javax.swing.JPanel jPanelWkOne;
    private javax.swing.JPanel jPanelWkPrevComments;
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
    private javax.swing.JSeparator jSeparator10;
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
    private javax.swing.JSeparator jSeparator37;
    private javax.swing.JSeparator jSeparator38;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    private javax.swing.JTabbedPane jTabbedPaneMain;
    private javax.swing.JTable jTableWk1Activities;
    private javax.swing.JTable jTableWk2Activities;
    private javax.swing.JTable jTableWk3Activities;
    private javax.swing.JTextField jTextAmmendRequest;
    private javax.swing.JTextArea jTextAreaComments;
    private javax.swing.JTextArea jTextAreaPrevComments;
    private javax.swing.JTextArea jTextAreaWk1DialogJustification;
    private javax.swing.JTextArea jTextAreaWk2DialogJustification;
    private javax.swing.JTextArea jTextAreaWk3DialogJustification;
    private javax.swing.JTextField jTextFieldDialogWk1Site;
    private javax.swing.JTextField jTextFieldDialogWk2Site;
    private javax.swing.JTextField jTextFieldDialogWk3Site;
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
    // End of variables declaration//GEN-END:variables
}
