/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClaimPlan;

import java.awt.Image;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.time.LocalDate;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import ClaimAppendix1.*;
import ClaimAppendix2.*;
import ClaimReports.*;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
public class JFrameMnthRequest extends javax.swing.JFrame {

    String ipAdd =    "ophidutilapp.southafricanorth.cloudapp.azure.com:16432"       ;
    String usrName = "appfin";
    String usrPass = "542ytDYvynv$TVYb";
    String mailUsrNam = "finance@ophid.co.zw";
    String mailUsrPass = "MgqM5utyUr43x#";
    boolean ignoreInput = false;
    String filename = null;
    int charMax = 200;
    int requestDays = 0;
    int sqlError = 0;
    int genCount = 0;
    int itmCount = 0;
    int wfCount = 0;
    int docAttDoc = 0;
    int refCheck = 0;
    int month, finyear;
    double totSeg1 = 0;
    double totSeg2 = 0;
    Date curYear = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatterGen = new SimpleDateFormat("dd MMM yyyy");
    SimpleDateFormat df = new SimpleDateFormat("yyyy");
    String wk1Site = "N";
    String wk2Site = "N";
    String wk3Site = "N";
    String hostName = "";
    byte[] person_image = null;
    String breakfastAll, lunchAll, lunchNPAll, lunchPAll, dinnerAll,
            incidentalAll, unProvedAll, date1, date2, usrnam, docVer, docNextVer, usrGrp,
            actStartDate, actEndDate, sendTo, createUsrNam, supUsrMail, oldRefNum, digSign, SearchRef;
    PreparedStatement pst1 = null;
    PreparedStatement pst = null;
    PreparedStatement pst3 = null;
    PreparedStatement pst4 = null;
    int itmNum = 1;

    /**
     * Creates new form JFrameMnthPlan
     */
    public JFrameMnthRequest() {
        initComponents();
        showDate();
        showTime();
        computerName();
        findUser();
        UserDet();

    }

    public JFrameMnthRequest(String ref, String usrLogNam) {
        initComponents();
        SearchRef = ref;
        jLabelEmp.setText(usrLogNam);
        jLabelEmp.setVisible(false);
 jMenuItemRequest.setEnabled(false);
 jMenuItemSpecialAcquittal.setEnabled(false);
        showDate();
        showTime();
        computerName();
        findUser();
        UserDet();
         findUserGrp();
        fetchdataGenWk1();
        fetchdataWk1();
        fetchdataWk2();
        fetchdataWk3();
        calTotWk1();
        calTotWk2();
        calTotWk3();
        mainPageTotInsert();

        //checkOutstandingRequest();
        // initUserDet();
    }

    void computerName() {

        try {
            java.net.InetAddress addr = InetAddress.getLocalHost();
            hostName = addr.getHostName();

        } catch (Exception ex) {
            System.out.println("Hostname can not be resolved");
        }
    }

    void showTime() {
        new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date d = new Date();
                SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss a");
                jLabelGenTime.setText(s.format(d));
                jLabelLineTime.setText(s.format(d));
                jLabelLineTime1.setText(s.format(d));
                jLabelLineTime2.setText(s.format(d));
                jLabelLineTime3.setText(s.format(d));
            }
        }) {

        }.start();

    }

    void showDate() {
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        jLabelGenDate.setText(s.format(d));
        jLabelLineDate.setText(s.format(d));
        jLabelLineDate1.setText(s.format(d));
        jLabelLineDate2.setText(s.format(d));
        jLabelLineDate3.setText(s.format(d));

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

    void findUser() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select * from [ClaimsAppSysZvandiri].[dbo]."
                    + "[EmpDetTab] where emp_num ='" + jLabelEmp.getText() + "'");

            while (r.next()) {

                jLabelGenLogNam.setText(r.getString(2));
                jLabelLineLogNam.setText(r.getString(2));
                jLabelLineLogNam1.setText(r.getString(2));
                jLabelLineLogNam2.setText(r.getString(2));
                jLabelLineLogNam3.setText(r.getString(2));

                sendTo = r.getString(5);
                createUsrNam = r.getString(2);
                supUsrMail = r.getString(6);

            }

            //                 conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Connect to Database (Province).Try Again", "Error Connection", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
    }

    void UserDet() {

        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT *   FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] "
                    + "where EMP_NUM = '" + jLabelEmp.getText() + "'");
            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                jLabelEmpNam.setText(rs.getString(2));
                jLabelEmpNum.setText(rs.getString(1));
                //  jLabelEmpTitle.setText(rs.getString(3));

            }

            Statement st1 = conn.createStatement();

            st1.executeQuery("SELECT EMP_BNK_NAM,ACC_NUM FROM [ClaimsAppSysZvandiri].[dbo].[EmpBankDetTab] "
                    + "where EMP_NUM = '" + jLabelEmp.getText() + "'");
            ResultSet rs1 = st1.getResultSet();

            while (rs1.next()) {
                jLabelBankName.setText(rs1.getString(1));
                jLabelAccNum.setText(rs1.getString(2));

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        jLabelEmpProvince.requestFocusInWindow();
        jLabelEmpProvince.setFocusable(true);

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
                pst1.setString(24, "1");
                pst1.setString(25, "A");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
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
                pst1.setString(3, jLabelEmpProvince.getText());
                pst1.setString(4, jLabelEmpOffice.getText());
                pst1.setString(5, formatter.format(jLabelWk1From.getText()));
                pst1.setString(6, formatter.format(jLabelWk1DateTo.getText()));
                pst1.setString(7, docNextVer);
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
                pst1.setString(2, SearchRef);
                pst1.setString(3, jLabelEmpProvince.getText());
                pst1.setString(4, jLabelEmpOffice.getText());
                pst1.setString(5, formatter.format(jLabelWk1From.getText()));
                pst1.setString(6, formatter.format(jLabelWk1DateTo.getText()));
                pst1.setString(7, formatter.format(jLabelWk2DateFrom.getText()));
                pst1.setString(8, formatter.format(jLabelWk2DateTo.getText()));
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
                pst1.setString(2, SearchRef);
                pst1.setString(3, jLabelEmpProvince.getText());
                pst1.setString(4, jLabelEmpOffice.getText());
                pst1.setString(5, formatter.format(jLabelWk1From.getText()));
                pst1.setString(6, formatter.format(jLabelWk1DateTo.getText()));
                pst1.setString(7, formatter.format(jLabelWk2DateFrom.getText()));
                pst1.setString(8, formatter.format(jLabelWk2DateTo.getText()));
                pst1.setString(9, formatter.format(jLabelWk3From.getText()));
                pst1.setString(10, formatter.format(jLabelWk3DateTo.getText()));
                pst1.setString(11, "1");
                pst1.setString(12, "1");
                pst1.setString(13, "A");

                pst1.executeUpdate();

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void PlanReqClearAction() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            
            
            String sqlWkplanperiod = "insert into [ClaimsAppSysZvandiri].[dbo].[PlanReqClearTab] (SERIAL,PLAN_REF_NUM,REQ_SERIAL,"
                    + "REF_NUM,REQ_DATE,EMP_NAM,DOC_VER,ACT_REC_STA)"
                    + " VALUES (?,?,?,?,?,?,?,?)";

            pst1 = conn.prepareStatement(sqlWkplanperiod);
            pst1.setString(1, "P");
            pst1.setString(2, SearchRef);
            pst1.setString(3, jLabelSerial.getText());
            pst1.setString(4, jLabelRegNum.getText());
            pst1.setString(5, jLabelGenDate.getText());
            pst1.setString(6, jLabelLineLogNam.getText());
            pst1.setString(7, "1");
            pst1.setString(8, "A");

            pst1.executeUpdate();

        } catch (Exception e) {
          e.printStackTrace();
        }
    }

    void fetchdataGenWk1() {
        try {

            DefaultTableModel modelWk1 = (DefaultTableModel) jTableWk1Activities.getModel();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT PROVINCE ,DISTRICT ,WK1_PLAN_START_DATE ,WK1_PLAN_END_DATE,"
                    + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,Wk3_PLAN_START_DATE,Wk3_PLAN_END_DATE "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab]"
                    + " where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'C'");

            ResultSet r = st.getResultSet();

            while (r.next()) {

                jLabelEmpProvince.setText(r.getString(1));
                jLabelEmpOffice.setText(r.getString(2));

                if ((!("".equals(r.getString(3)))) && (r.getString(5) == null) && (r.getString(7) == null)) {

                    jLabelWk1DateFrom.setText(r.getString(3));
                    jLabelWk1DateTo.setText(r.getString(4));
                    actStartDate = r.getString(3);
                    actEndDate = r.getString(4);

                } else if ((!("".equals(r.getString(3)))) && (!("".equals(r.getString(5)))) && (r.getString(7) == null)) {

                    jLabelWk1DateFrom.setText(r.getString(3));
                    jLabelWk1DateTo.setText(r.getString(4));
                    jLabelWk2DateFrom.setText(r.getString(5));
                    jLabelWk2DateTo.setText(r.getString(6));
                    actStartDate = r.getString(3);
                    actEndDate = r.getString(6);

                } else {

                    jLabelWk1DateFrom.setText(r.getString(3));
                    jLabelWk1DateTo.setText(r.getString(4));
                    jLabelWk2DateFrom.setText(r.getString(5));
                    jLabelWk2DateTo.setText(r.getString(6));
                    jLabelWk3DateFrom.setText(r.getString(7));
                    jLabelWk3DateTo.setText(r.getString(8));
                    actStartDate = r.getString(3);
                    actEndDate = r.getString(8);
                }

            }
            jTextActMainPurpose.setEditable(false);
            jTextActMainPurpose.setText("Activities for " + jLabelEmpNam.getText() + " period : " + actStartDate + " to " + actEndDate);

        } catch (Exception e) {

        }
    }

    void fetchdataWk1() {
        try {

            DefaultTableModel modelWk1 = (DefaultTableModel) jTableWk1Activities.getModel();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            st.executeQuery(" SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'C'\n"
                    + "  and act_date in (SELECT ACT_DATE\n"
                    + "       FROM [ClaimsAppSysZvandiri].[dbo].[PlanUsrRecTab]\n"
                    + "  where status = 'A'\n"
                    + "  and PLAN_WK = 1)\n"
                    + "  and (EMP_NAM1 = '" + jLabelGenLogNam.getText() + "'\n"
                    + "  or EMP_NAM2 = '" + jLabelGenLogNam.getText() + "'\n"
                    + "  or EMP_NAM3 = '" + jLabelGenLogNam.getText() + "'\n"
                    + "  or EMP_NAM4 = '" + jLabelGenLogNam.getText() + "')");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk1.insertRow(modelWk1.getRowCount(), new Object[]{r.getString(3), r.getString(4), r.getString(7),
                    r.getString(8), r.getString(11), r.getString(12), r.getString(13), r.getString(14), r.getString(15), r.getString(16),
                    r.getString(17), r.getString(18)});

            }

            st1.executeQuery("SELECT distinct ACT_VER,ACT_VER + 1  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] "
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A'");

            ResultSet r1 = st1.getResultSet();

            while (r1.next()) {
                docVer = r1.getString(1);
                docNextVer = r1.getString(2);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdataWk2() {
        try {

            DefaultTableModel modelWk2 = (DefaultTableModel) jTableWk2Activities.getModel();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            st.executeQuery(" SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'C'\n"
                    + "  and act_date in (SELECT ACT_DATE\n"
                    + "       FROM [ClaimsAppSysZvandiri].[dbo].[PlanUsrRecTab]\n"
                    + "  where status = 'A'\n"
                    + "  and PLAN_WK = 2)\n"
                    + "  and (EMP_NAM1 = '" + jLabelGenLogNam.getText() + "'\n"
                    + "  or EMP_NAM2 = '" + jLabelGenLogNam.getText() + "'\n"
                    + "  or EMP_NAM3 = '" + jLabelGenLogNam.getText() + "'\n"
                    + "  or EMP_NAM4 = '" + jLabelGenLogNam.getText() + "')");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk2.insertRow(modelWk2.getRowCount(), new Object[]{r.getString(3), r.getString(4), r.getString(7),
                    r.getString(8), r.getString(11), r.getString(12), r.getString(13), r.getString(14), r.getString(15), r.getString(16),
                    r.getString(17), r.getString(18)});

            }

            st1.executeQuery("SELECT distinct ACT_VER,ACT_VER + 1  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] "
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'C'");

            ResultSet r1 = st1.getResultSet();

            while (r1.next()) {
                docVer = r1.getString(1);
                docNextVer = r1.getString(2);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdataWk3() {
        try {

            DefaultTableModel modelWk3 = (DefaultTableModel) jTableWk3Activities.getModel();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            st.executeQuery(" SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'C'\n"
                    + "  and act_date in (SELECT ACT_DATE\n"
                    + "       FROM [ClaimsAppSysZvandiri].[dbo].[PlanUsrRecTab]\n"
                    + "  where status = 'A'\n"
                    + "  and PLAN_WK = 3)\n"
                    + "  and (EMP_NAM1 = '" + jLabelGenLogNam.getText() + "'\n"
                    + "  or EMP_NAM2 = '" + jLabelGenLogNam.getText() + "'\n"
                    + "  or EMP_NAM3 = '" + jLabelGenLogNam.getText() + "'\n"
                    + "  or EMP_NAM4 = '" + jLabelGenLogNam.getText() + "')");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk3.insertRow(modelWk3.getRowCount(), new Object[]{r.getString(3), r.getString(4), r.getString(7),
                    r.getString(8), r.getString(11), r.getString(12), r.getString(13), r.getString(14), r.getString(15), r.getString(16),
                    r.getString(17), r.getString(18)});

            }

            st1.executeQuery("SELECT distinct ACT_VER,ACT_VER + 1  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] "
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'C'");

            ResultSet r1 = st1.getResultSet();

            while (r1.next()) {
                docVer = r1.getString(1);
                docNextVer = r1.getString(2);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void deleteLongLock() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            String sqlDeleteLock = "delete [ClaimsAppSysZvandiri].[dbo].[SerialLock] where SERIAL = "
                    + "'R'";

            pst = conn.prepareStatement(sqlDeleteLock);
            pst.executeUpdate();

        } catch (Exception e) {

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

                st.executeQuery("SELECT count(*) FROM [ClaimsAppSysZvandiri].[dbo].[SerialLock] where SERIAL = 'R'");

                ResultSet rs = st.getResultSet();
                //  jLabelRegNum.setVisible(false);
                while (rs.next()) {
                    count = Integer.parseInt(rs.getString(1));

                }
                // conn.close();

                if (count > 0) {
                    try {

                        Statement st1 = conn.createStatement();

                        st1.executeQuery("SELECT USR_NAM,LCK_DATE_TIME FROM [ClaimsAppSysZvandiri].[dbo].[SerialLock]");

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

                        if ((jLabelEmpNam.getText().equals(usrnam)) && (seconds >= 1)) {

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

    void RefNumAllocation() {

        GregorianCalendar date = new GregorianCalendar();
        month = date.get(Calendar.MONTH);
        month = month + 1;

        if ((month >= 1) && (month <= 9)) {
            finyear = Integer.parseInt(df.format(curYear));
        } else {
            finyear = Integer.parseInt(df.format(curYear)) + 1;
        }
        try {

            String sqlSerialLock = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[SerialLock] "
                    + "(SERIAL,REF_NUM,USR_NAM) VALUES (?,?,?)";

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            Statement st = conn.createStatement();

            st.executeQuery("SELECT REF_NUM + 1,SERIAL,REF_NUM FROM [ClaimsAppSysZvandiri].[dbo].[refNumTab] where REF_YEAR ='" + finyear + "' and SERIAL = 'R'");

            ResultSet rs = st.getResultSet();

            jLabelRegYear.setText(Integer.toString(finyear));
            while (rs.next()) {
                jLabelRegNum.setText(rs.getString(1));
                jLabelSerial.setText(rs.getString(2));
                oldRefNum = rs.getString(3);

            }
            pst = conn.prepareStatement(sqlSerialLock);
            pst.setString(1, jLabelSerial.getText());
            pst.setString(2, jLabelRegNum.getText());
            pst.setString(3, jLabelEmpNam.getText());

            pst.executeUpdate();

            String sqlSerialLockUpdate = "update [ClaimsAppSysZvandiri].[dbo].[SerialLock] set LCK_DATE_TIME = ( SELECT SYSDATETIME()) where "
                    + "concat(SERIAL,REF_NUM) = '" + jLabelSerial.getText() + jLabelRegNum.getText() + "' ";

            pst1 = conn.prepareStatement(sqlSerialLockUpdate);
            pst1.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void refNumUpdate() {
        try {

            String sql1 = "update [ClaimsAppSysZvandiri].[dbo].[refNumTab] set "
                    + "REF_NUM ='" + jLabelRegNum.getText() + "' where REF_YEAR ='" + finyear + "' and SERIAL = 'R'";

            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            pst = conn.prepareStatement(sql1);
            pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    void createAction() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            String sqlcreate = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] "
                    + "(YEAR,SERIAL,REF_NUM,USR_ACTION, DOC_STATUS,ACTIONED_BY, "
                    + "SEND_TO, ACTIONED_ON_DATE, ACTIONED_ON_TIME, DOC_VER,"
                    + " ACTION_VER,ACTIONED_ON_COMPUTER,REG_MOD_VER,ACT_REC_STA)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            //String createUsrNam = jLabelGenLogNam.getText();
            String actionCode = "Request - Created";
            String statusCode = "Registered";

            //    String sendTo = "Supervisor";
            pst1 = conn.prepareStatement(sqlcreate);

            pst1.setString(1, df.format(curYear));
            pst1.setString(2, jLabelSerial.getText());
            pst1.setString(3, jLabelRegNum.getText());
            pst1.setString(4, actionCode);
            pst1.setString(5, statusCode);
            pst1.setString(6, createUsrNam);
            pst1.setString(7, sendTo);
            pst1.setString(8, jLabelGenDate.getText());
            pst1.setString(9, jLabelGenTime.getText());
            pst1.setString(10, "1");
            pst1.setString(11, "1");
            pst1.setString(12, hostName);
            pst1.setString(13, "1");
            pst1.setString(14, "A");
            pst1.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    void checkOutstandingRequest() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select  min(days) from (SELECT ref_num,  \n"
                    + "DATEDIFF(dw,SYSDATETIME(), MAX(ACT_DATE)) \"days\" FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] \n"
                    + "where REF_NUM in (SELECT distinct REF_NUM FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] \n"
                    + "where REF_NUM not in (select distinct PREV_REF_NUM from [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] \n"
                    + "where PREV_REF_NUM is not null ) and REF_NUM not in (SELECT REF_NUM FROM [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab]\n"
                    + "where DOC_STATUS like upper('%Dis%') and SERIAL = 'R') \n"
                    + "and EMP_NUM = '" + jLabelEmp.getText() + "' and SERIAL = 'R' )\n"
                    + " group by REF_NUM) a\n"
                    + " where  REF_NUM in (SELECT REF_NUM FROM [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] where DOC_STATUS = 'HeadApprove' and SERial = 'R')\n");
            //       + "group by REF_NUM");

            while (r.next()) {

                requestDays = r.getInt(1);

            }
         } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Connect to Database (Province).Try Again", "Error Connection", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
    }

    void checkRegistration() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();
            Statement st3 = conn.createStatement();
            Statement st4 = conn.createStatement();
            Statement st5 = conn.createStatement();

            st.executeQuery("SELECT REF_NUM FROM [ClaimsAppSysZvandiri].[dbo].[refNumTab] WHERE concat(SERIAL,REF_NUM) ='" + jLabelSerial.getText() + jLabelRegNum.getText() + "'");
            ResultSet r = st.getResultSet();

            while (r.next()) {
                refCheck = r.getInt(1);
            }

            st2.executeQuery("SELECT count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] where concat(SERIAL,REF_NUM) ='" + jLabelSerial.getText() + jLabelRegNum.getText() + "'");
            ResultSet r2 = st2.getResultSet();

            while (r2.next()) {
                genCount = r2.getInt(1);
            }

            st3.executeQuery("SELECT count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] where concat(SERIAL,REF_NUM) ='" + jLabelSerial.getText() + jLabelRegNum.getText() + "'");
            ResultSet r3 = st3.getResultSet();

            while (r3.next()) {
                itmCount = r3.getInt(1);
            }

            st4.executeQuery("SELECT  count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] where concat(SERIAL,REF_NUM) ='" + jLabelSerial.getText() + jLabelRegNum.getText() + "'");
            ResultSet r4 = st4.getResultSet();

            while (r4.next()) {
                wfCount = r4.getInt(1);
            }

            st1.executeQuery("SELECT count(*) FROM [ClaimsAppSysZvandiri].[dbo].[DocAttachTab] WHERE concat(SERIAL,REF_NUM) ='" + jLabelSerial.getText() + jLabelRegNum.getText() + "'");
            ResultSet r1 = st1.getResultSet();

            while (r1.next()) {
                docAttDoc = r1.getInt(1);
            }

            if ((refCheck == (Integer.parseInt(jLabelRegNum.getText()))) && (genCount == 1) && (itmCount > 0)
                    && (wfCount == 1) && (docAttDoc == 1)) {

                deleteLongLock();

                sendMail();
                jDialogWaitingEmail.setVisible(false);
                jLabelRegNum.setVisible(true);
                jLabelRegYear.setVisible(true);
                jLabelSerial.setVisible(true);
                JOptionPane.showMessageDialog(null, "Email has been forwarded to " + sendTo + " for approval of your requistion No. "
                        + jLabelRegYear.getText() + " " + jLabelSerial.getText() + " " + jLabelRegNum.getText());

                new JFrameMnthReqGenList(jLabelEmp.getText()).setVisible(true);
                setVisible(false);

            } else {
                jLabelRegNum.setVisible(false);
                jLabelRegYear.setVisible(false);
                jLabelSerial.setVisible(false);
                regFail();
                JOptionPane.showMessageDialog(null, "<html> Registration failure. Registration falure can be caused by slow network speeds.<br><br> Please try again. If the problem persist contact IT.</html>");

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    void regFail() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            String sqlDeleteLock = "delete [ClaimsAppSysZvandiri].[dbo].[SerialLock] where  "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelSerial.getText() + jLabelRegNum.getText() + "'";

            pst = conn.prepareStatement(sqlDeleteLock);
            pst.executeUpdate();

            String sqlDeleteGen = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] where "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelSerial.getText() + jLabelRegNum.getText() + "'";
            pst = conn.prepareStatement(sqlDeleteGen);
            pst.executeUpdate();

            String sqlDeleteItm = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] where "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelSerial.getText() + jLabelRegNum.getText() + "'";
            pst = conn.prepareStatement(sqlDeleteItm);
            pst.executeUpdate();

            String sqlDeleteWF = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] where "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelSerial.getText() + jLabelRegNum.getText() + "'";
            pst = conn.prepareStatement(sqlDeleteWF);
            pst.executeUpdate();

            String sqlDeleteDocAtt = "delete [ClaimsAppSysZvandiri].[dbo].[DocAttachTab] where "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelSerial.getText() + jLabelRegNum.getText() + "'";
            pst = conn.prepareStatement(sqlDeleteDocAtt);
            pst.executeUpdate();

            String sqlDeleteAck = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimsAckTab] where "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelSerial.getText() + jLabelRegNum.getText() + "'";
            pst = conn.prepareStatement(sqlDeleteAck);
            pst.executeUpdate();

            String sqlrefUpdate = "update [ClaimsAppSysZvandiri].[dbo].[refNumTab] set "
                    + "REF_NUM ='" + oldRefNum + "' where "
                    + "SERIAL='R'";

            pst = conn.prepareStatement(sqlrefUpdate);
            pst.executeUpdate();

        } catch (Exception e) {

        }
    }

    void insGenTab() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            String sql = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] "
                    + "(REF_YEAR,SERIAL,REF_NUM,REF_DAT,EMP_NUM,EMP_NAM,EMP_TTL,EMP_PROV,EMP_OFF,"
                    + "EMP_BNK_NAM,ACC_NUM,ACT_MAIN_PUR,ACT_TOT_AMT,ACT_REC_STA,DOC_VER,REG_MOD_VER) "
                    + " VALUES (?,?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";

            pst = conn.prepareStatement(sql);

            pst.setString(1, String.valueOf(jLabelRegYear.getText()));
            pst.setString(2, jLabelSerial.getText());
            pst.setString(3, String.valueOf(jLabelRegNum.getText()));
            pst.setString(4, jLabelGenDate.getText());
            pst.setString(5, jLabelEmpNum.getText());
            pst.setString(6, jLabelEmpNam.getText());
            pst.setString(7, jLabelEmpTitle.getText());
            pst.setString(8, jLabelEmpProvince.getText());
            pst.setString(9, jLabelEmpOffice.getText());
            pst.setString(10, jLabelBankName.getText());
            pst.setString(11, jLabelAccNum.getText());
//            pst.setString(12, jComboBudgetCode.getSelectedItem().toString());
            pst.setString(12, jTextActMainPurpose.getText());
            pst.setString(13, jLabelAppTotReqCost.getText());
            pst.setString(14, "A");
            pst.setString(15, "1");
            pst.setString(16, "1");

            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void insItmWk1Tab() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {

                String sqlitm = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                        + "(REF_YEAR,SERIAL,REF_NUM,ITM_NUM,ACT_DATE,ACT_DEST,ACT_DIST,ACT_ITM_PUR,LN_BUD_CODE,"
                        + "BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,MSC_AMT,"
                        + "ACC_UNPROV_AMT, ACT_ITM_TOT,PLAN_WK,DOC_VER,REG_MOD_VER,ACT_REC_STA)"
                        + " VALUES (?,?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlitm);

                pst1.setString(1, String.valueOf(jLabelRegYear.getText()));
                pst1.setString(2, jLabelSerial.getText());
                pst1.setString(3, String.valueOf(jLabelRegNum.getText()));
                pst1.setString(4, String.valueOf(itmNum));
                pst1.setString(5, jTableWk1Activities.getValueAt(i, 0).toString());
                pst1.setString(6, jTableWk1Activities.getValueAt(i, 1).toString());
                pst1.setString(7, jTableWk1Activities.getValueAt(i, 2).toString());
                pst1.setString(8, jTableWk1Activities.getValueAt(i, 3).toString());
                pst1.setString(9, jTableWk1Activities.getValueAt(i, 4).toString());
                pst1.setString(10, jTableWk1Activities.getValueAt(i, 5).toString());
                pst1.setString(11, jTableWk1Activities.getValueAt(i, 6).toString());
                pst1.setString(12, jTableWk1Activities.getValueAt(i, 7).toString());
                pst1.setString(13, jTableWk1Activities.getValueAt(i, 8).toString());
                pst1.setString(14, jTableWk1Activities.getValueAt(i, 9).toString());
                pst1.setString(15, jTableWk1Activities.getValueAt(i, 10).toString());
                pst1.setString(16, jTableWk1Activities.getValueAt(i, 11).toString());
                pst1.setString(17, jTableWk1Activities.getValueAt(i, 12).toString());
                pst1.setString(18, "1");
                pst1.setString(19, "1");
                pst1.setString(20, "1");
                pst1.setString(21, "A");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void insItmWk2Tab() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {

                String sqlitm = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                        + "(REF_YEAR,SERIAL,REF_NUM,ITM_NUM,ACT_DATE,ACT_DEST,ACT_DIST,ACT_ITM_PUR,LN_BUD_CODE,"
                        + "BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,MSC_AMT,"
                        + "ACC_UNPROV_AMT, ACT_ITM_TOT,PLAN_WK,DOC_VER,REG_MOD_VER,ACT_REC_STA)"
                        + " VALUES (?,?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlitm);

                pst1.setString(1, String.valueOf(jLabelRegYear.getText()));
                pst1.setString(2, jLabelSerial.getText());
                pst1.setString(3, String.valueOf(jLabelRegNum.getText()));
                pst1.setString(4, String.valueOf(itmNum));
                pst1.setString(5, jTableWk2Activities.getValueAt(i, 0).toString());
                pst1.setString(6, jTableWk2Activities.getValueAt(i, 1).toString());
                pst1.setString(7, jTableWk2Activities.getValueAt(i, 2).toString());
                pst1.setString(8, jTableWk2Activities.getValueAt(i, 3).toString());
                pst1.setString(9, jTableWk2Activities.getValueAt(i, 4).toString());
                pst1.setString(10, jTableWk2Activities.getValueAt(i, 5).toString());
                pst1.setString(11, jTableWk2Activities.getValueAt(i, 6).toString());
                pst1.setString(12, jTableWk2Activities.getValueAt(i, 7).toString());
                pst1.setString(13, jTableWk2Activities.getValueAt(i, 8).toString());
                pst1.setString(14, jTableWk2Activities.getValueAt(i, 9).toString());
                pst1.setString(15, jTableWk2Activities.getValueAt(i, 10).toString());
                pst1.setString(16, jTableWk2Activities.getValueAt(i, 11).toString());
                pst1.setString(17, jTableWk2Activities.getValueAt(i, 12).toString());
                pst1.setString(18, "2");
                pst1.setString(19, "1");
                pst1.setString(20, "1");
                pst1.setString(21, "A");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void insItmWk3Tab() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {

                String sqlitm = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                        + "(REF_YEAR,SERIAL,REF_NUM,ITM_NUM,ACT_DATE,ACT_DEST,ACT_DIST,ACT_ITM_PUR,LN_BUD_CODE,"
                        + "BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,MSC_AMT,"
                        + "ACC_UNPROV_AMT, ACT_ITM_TOT,PLAN_WK,DOC_VER,REG_MOD_VER,ACT_REC_STA)"
                        + " VALUES (?,?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlitm);

                pst1.setString(1, String.valueOf(jLabelRegYear.getText()));
                pst1.setString(2, jLabelSerial.getText());
                pst1.setString(3, String.valueOf(jLabelRegNum.getText()));
                pst1.setString(4, String.valueOf(itmNum));
                pst1.setString(5, jTableWk3Activities.getValueAt(i, 0).toString());
                pst1.setString(6, jTableWk3Activities.getValueAt(i, 1).toString());
                pst1.setString(7, jTableWk3Activities.getValueAt(i, 2).toString());
                pst1.setString(8, jTableWk3Activities.getValueAt(i, 3).toString());
                pst1.setString(9, jTableWk3Activities.getValueAt(i, 4).toString());
                pst1.setString(10, jTableWk3Activities.getValueAt(i, 5).toString());
                pst1.setString(11, jTableWk3Activities.getValueAt(i, 6).toString());
                pst1.setString(12, jTableWk3Activities.getValueAt(i, 7).toString());
                pst1.setString(13, jTableWk3Activities.getValueAt(i, 8).toString());
                pst1.setString(14, jTableWk3Activities.getValueAt(i, 9).toString());
                pst1.setString(15, jTableWk3Activities.getValueAt(i, 10).toString());
                pst1.setString(16, jTableWk3Activities.getValueAt(i, 11).toString());
                pst1.setString(17, jTableWk3Activities.getValueAt(i, 12).toString());
                pst1.setString(18, "3");
                pst1.setString(19, "1");
                pst1.setString(20, "1");
                pst1.setString(21, "A");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void insAckTab() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();

            String sqlAckUsr = "INSERT ClaimsAppSysZimTTECH.dbo.ClaimsAckTab (SERIAL,REF_NUM,"
                    + "EMP_NUM,ACK_SUBMIT,ACK_SAL_DEDUCT,ACK_DATE)"
                    + "VALUES (?,?, ?,?, ?, ?)";

            pst3 = conn.prepareStatement(sqlAckUsr);
            pst3.setString(1, jLabelSerial.getText());
            pst3.setString(2, jLabelRegNum.getText());
            pst3.setString(3, jLabelEmpNum.getText());
            pst3.setString(4, "Y");
            pst3.setString(5, "Y");
            pst3.setString(6, jLabelGenDate.getText());

            pst3.executeUpdate();

            st.executeQuery("select convert (varchar(50),DECRYPTBYPASSPHRASE('password',usrencrypass)) "
                    + "from [HRLeaveSysZvandiri].[dbo].[usrlogin] where EmpNum='" + jLabelEmpNum.getText() + "'");
            ResultSet r = st.getResultSet();

            while (r.next()) {
                digSign = r.getString(1);
            }

            String sqlDigSignUpdate = "update [ClaimsAppSysZvandiri].[dbo].[ClaimsAckTab] "
                    + "set ACK_DIG_SIGN =ENCRYPTBYPASSPHRASE('password', '" + digSign + "')"
                    + "where concat(SERIAL,REF_NUM) = '" + jLabelSerial.getText() + jLabelRegNum.getText() + "' ";

            pst4 = conn.prepareStatement(sqlDigSignUpdate);
            pst4.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void WkPlanActionUpd() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            String sqlPlanAct = "update [ClaimsAppSysZvandiri].[dbo].[PlanUsrRecTab] set "
                    + "STATUS ='P' where PLAN_REF_NUM =" + SearchRef + " and STATUS= 'A' and EMP_NAM = '" + jLabelGenLogNam.getText() + "'";

            pst = conn.prepareStatement(sqlPlanAct);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void submitSaveRequest() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            SerialCheck();

            RefNumAllocation();

            insGenTab();

            imgSave();

            if (jTableWk1Activities.getRowCount() > 0) {
                insItmWk1Tab();
            }

            if (jTableWk2Activities.getRowCount() > 0) {
                insItmWk2Tab();
            }

            if (jTableWk3Activities.getRowCount() > 0) {
                insItmWk3Tab();
            }
            createAction();

            WkPlanActionUpd();
            
            PlanReqClearAction();
            
            refNumUpdate();

            checkRegistration();

        } catch (SQLException e) {

            sqlError = e.getErrorCode();
            if (sqlError == 2627) {
                checkRegistration();
            }

        }

    }

    void imgSave() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            if (!("".equals(jLabelImg.getIcon().toString()))) {
                String sqlAttDoc = " INSERT INTO [ClaimsAppSysZvandiri].[dbo].[DocAttachTab] (DOC_ATT_NUM,SERIAL,REF_NUM,ATT_IMAGE,IMG_VERSION)"
                        + "VALUES (?,?,?,?,?)";

                pst = conn.prepareStatement(sqlAttDoc);

                pst.setString(1, "1");
                pst.setString(2, jLabelSerial.getText());
                pst.setString(3, String.valueOf(jLabelRegNum.getText()));
                pst.setBytes(4, person_image);
                pst.setString(5, "1");

                pst.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void imgChooser() {

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("images", "jpg", "gif", "png", "jpeg");
        chooser.setFileFilter(filter);
        chooser.setMultiSelectionEnabled(true);
        int returnVal = chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        filename = f.getAbsolutePath();

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (!(f.getName().endsWith(".jpg") || f.getName().endsWith(".gif") || f.getName().endsWith(".png") || f.getName().endsWith(".jpeg"))) {
                JOptionPane.showMessageDialog(null, "Please select file with .jpg, .bmp, .png, .jpeg and .gif extension only.");
            }
        }

        ImageIcon imageIcon = new ImageIcon(new ImageIcon(filename).getImage().getScaledInstance(jLabelImg.getWidth(), jLabelImg.getHeight(), Image.SCALE_SMOOTH));
        jLabelImg.setIcon(imageIcon);

        try {
            File image = new File(filename);
            FileInputStream fis = new FileInputStream(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[128];

            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);

            }

            int size = bos.toByteArray().length;
            if (size > 1000000) {
                JOptionPane.showMessageDialog(this, "Attached file is too big. Attached file should be less that 1Mb. Please rescan and attach again.");
                jLabelImg.setIcon(null);
            } else {
                person_image = bos.toByteArray();
            }

        } catch (Exception e) {
            System.out.println(e);

        }
    }

    void calTotWk1() {
        Double lineTot = 0.00;
        for (int row = 0; row < jTableWk1Activities.getRowCount(); row++) {
            lineTot = Double.parseDouble((String) jTableWk1Activities.getValueAt(row, 5))
                    + Double.parseDouble((String) jTableWk1Activities.getValueAt(row, 6))
                    + Double.parseDouble((String) jTableWk1Activities.getValueAt(row, 7))
                    + Double.parseDouble((String) jTableWk1Activities.getValueAt(row, 8))
                    + Double.parseDouble((String) jTableWk1Activities.getValueAt(row, 10))
                    + Double.parseDouble((String) jTableWk1Activities.getValueAt(row, 11));
            jTableWk1Activities.setValueAt((String.format("%.2f", lineTot)), row, 12);

        }

    }

    void calTotWk2() {
        Double lineTot = 0.00;
        for (int row = 0; row < jTableWk2Activities.getRowCount(); row++) {
            lineTot = Double.parseDouble((String) jTableWk2Activities.getValueAt(row, 5))
                    + Double.parseDouble((String) jTableWk2Activities.getValueAt(row, 6))
                    + Double.parseDouble((String) jTableWk2Activities.getValueAt(row, 7))
                    + Double.parseDouble((String) jTableWk2Activities.getValueAt(row, 8))
                    + Double.parseDouble((String) jTableWk2Activities.getValueAt(row, 10))
                    + Double.parseDouble((String) jTableWk2Activities.getValueAt(row, 11));
            jTableWk2Activities.setValueAt((String.format("%.2f", lineTot)), row, 12);

          
        }

    }

    void mainPageTotInsert() {

        DecimalFormat numFormat;
        numFormat = new DecimalFormat("000.##");
        double breakfastsubtotal = 0;
        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            double breakfastamount = Double.parseDouble((String) jTableWk1Activities.getValueAt(i, 5));
            breakfastsubtotal += breakfastamount;
        }

        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            double breakfastamount = Double.parseDouble((String) jTableWk2Activities.getValueAt(i, 5));
            breakfastsubtotal += breakfastamount;
        }

        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            double breakfastamount = Double.parseDouble((String) jTableWk3Activities.getValueAt(i, 5));
            breakfastsubtotal += breakfastamount;
        }

        jLabelBreakFastSubTot.setText(Double.toString(breakfastsubtotal));

        double lunchsubtotal = 0;
        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            double lunchamount = Double.parseDouble((String) jTableWk1Activities.getValueAt(i, 6));
            lunchsubtotal += lunchamount;
        }

        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            double lunchamount = Double.parseDouble((String) jTableWk2Activities.getValueAt(i, 6));
            lunchsubtotal += lunchamount;
        }

        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            double lunchamount = Double.parseDouble((String) jTableWk3Activities.getValueAt(i, 6));
            lunchsubtotal += lunchamount;
        }

        jLabelLunchSubTot.setText(Double.toString(lunchsubtotal));

        double dinnersubtotal = 0;
        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            double dinneramount = Double.parseDouble((String) jTableWk1Activities.getValueAt(i, 7));
            dinnersubtotal += dinneramount;
        }

        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            double dinneramount = Double.parseDouble((String) jTableWk2Activities.getValueAt(i, 7));
            dinnersubtotal += dinneramount;
        }

        jLabelDinnerSubTot.setText(Double.toString(dinnersubtotal));

        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            double dinneramount = Double.parseDouble((String) jTableWk3Activities.getValueAt(i, 7));
            dinnersubtotal += dinneramount;
        }

        jLabelDinnerSubTot.setText(Double.toString(dinnersubtotal));

        double incidentalsubtotal = 0;
        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            double incidentalamount = Double.parseDouble((String) jTableWk1Activities.getValueAt(i, 8));
            incidentalsubtotal += incidentalamount;
        }

        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            double incidentalamount = Double.parseDouble((String) jTableWk2Activities.getValueAt(i, 8));
            incidentalsubtotal += incidentalamount;
        }

        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            double incidentalamount = Double.parseDouble((String) jTableWk3Activities.getValueAt(i, 8));
            incidentalsubtotal += incidentalamount;
        }
        jLabelIncidentalSubTot.setText(Double.toString(incidentalsubtotal));

        double miscSubTot = 0;
        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            double miscamount = Double.parseDouble((String) jTableWk1Activities.getValueAt(i, 10));
            miscSubTot += miscamount;
        }

        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            double miscamount = Double.parseDouble((String) jTableWk2Activities.getValueAt(i, 10));
            miscSubTot += miscamount;
        }

        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            double miscamount = Double.parseDouble((String) jTableWk3Activities.getValueAt(i, 10));
            miscSubTot += miscamount;
        }
        jLabelMiscSubTot.setText(Double.toString(miscSubTot));

        double unprovedSubTot = 0;
        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            double unprovedamount = Double.parseDouble((String) jTableWk1Activities.getValueAt(i, 11));
            unprovedSubTot += unprovedamount;
        }

        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            double unprovedamount = Double.parseDouble((String) jTableWk2Activities.getValueAt(i, 11));
            unprovedSubTot += unprovedamount;
        }

        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            double unprovedamount = Double.parseDouble((String) jTableWk3Activities.getValueAt(i, 11));
            unprovedSubTot += unprovedamount;
        }
        jLabelAccUnprovedSubTot.setText(Double.toString(unprovedSubTot));

        double allTotal = unprovedSubTot + miscSubTot + incidentalsubtotal
                + dinnersubtotal + lunchsubtotal + breakfastsubtotal;
        numFormat.format(allTotal);

        jLabelAppTotReqCost.setText(String.format("%.2f", allTotal));

    }

    void calTotWk3() {
        Double lineTot = 0.00;
        for (int row = 0; row < jTableWk3Activities.getRowCount(); row++) {
            lineTot = Double.parseDouble((String) jTableWk3Activities.getValueAt(row, 5))
                    + Double.parseDouble((String) jTableWk3Activities.getValueAt(row, 6))
                    + Double.parseDouble((String) jTableWk3Activities.getValueAt(row, 7))
                    + Double.parseDouble((String) jTableWk3Activities.getValueAt(row, 8))
                    + Double.parseDouble((String) jTableWk3Activities.getValueAt(row, 10))
                    + Double.parseDouble((String) jTableWk3Activities.getValueAt(row, 11));
            jTableWk3Activities.setValueAt((String.format("%.2f", lineTot)), row, 12);

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
                    InternetAddress.parse(supUsrMail));
            message.setSubject("Approval Request.Perdiem Request Application Reference # R " + jLabelRegNum.getText());

            message.setContent("<html><body> Dear " + sendTo + "<br><br>" + createUsrNam
                    + " has submitted a perdiem requestion for your approval. <br><br>Please check "
                    + "your supervisor list and action.<br><br> Kind Regards <br><br>"
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
        jDialogBudget = new javax.swing.JDialog();
        jPanel11 = new javax.swing.JPanel();
        jLabelHeader2 = new javax.swing.JLabel();
        jButtonOkFacility1 = new javax.swing.JButton();
        jButtonCancelBudget = new javax.swing.JButton();
        jLabelBudgetCode = new javax.swing.JLabel();
        jComboBudgetCode = new javax.swing.JComboBox<>();
        jDialogWaiting = new javax.swing.JDialog();
        jDialogAckSalDeduct = new javax.swing.JDialog();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jCheckBoxSalAckAgree = new javax.swing.JCheckBox();
        jButtonAckSalCont = new javax.swing.JButton();
        jButtonSalAckCancel = new javax.swing.JButton();
        jLabelHeaderAck1 = new javax.swing.JLabel();
        jDialogFacility1 = new javax.swing.JDialog();
        jPanel12 = new javax.swing.JPanel();
        jLabelHeader3 = new javax.swing.JLabel();
        jButtonOkFacility2 = new javax.swing.JButton();
        jButtonCancelFacility1 = new javax.swing.JButton();
        jLabelProvinceFacility1 = new javax.swing.JLabel();
        jComboProvinceFacility1 = new javax.swing.JComboBox<>();
        jComboDistrictFacility1 = new javax.swing.JComboBox<>();
        jLabelDistrictFacility1 = new javax.swing.JLabel();
        jLabelFacility1 = new javax.swing.JLabel();
        jComboFacility1 = new javax.swing.JComboBox<>();
        jLabelDisFacilty = new javax.swing.JLabel();
        jLabelDisProvince = new javax.swing.JLabel();
        jLabelDisDistrict = new javax.swing.JLabel();
        jLabelFacDist = new javax.swing.JLabel();
        jDialogAckSubmit = new javax.swing.JDialog();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jCheckBoxSalSubmitAgree = new javax.swing.JCheckBox();
        jButtonAckSubmitCont = new javax.swing.JButton();
        jButtonSalSubmitCancel = new javax.swing.JButton();
        jLabelHeaderAck = new javax.swing.JLabel();
        jDialogWk1 = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabelDialogWk1Site = new javax.swing.JLabel();
        jTextFieldDialogWk1Site = new javax.swing.JTextField();
        jTextFieldWk1DialogActivityDesc = new javax.swing.JTextField();
        jLabelWk1DialogActivityDesc = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
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
        jLabelWk1LnActivity = new javax.swing.JLabel();
        jDialogWk3 = new javax.swing.JDialog();
        jPanel6 = new javax.swing.JPanel();
        jLabelDialogWk3Site = new javax.swing.JLabel();
        jTextFieldDialogWk3Site = new javax.swing.JTextField();
        jTextFieldWk3DialogActivityDesc = new javax.swing.JTextField();
        jLabelWk3DialogActivityDesc = new javax.swing.JLabel();
        jSeparator36 = new javax.swing.JSeparator();
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
        jLabelWk3LnActivity = new javax.swing.JLabel();
        jDialogWk2 = new javax.swing.JDialog();
        jPanel2 = new javax.swing.JPanel();
        jLabelDialogWk2Site = new javax.swing.JLabel();
        jTextFieldDialogWk2Site = new javax.swing.JTextField();
        jTextFieldWk2DialogActivityDesc = new javax.swing.JTextField();
        jLabelWk2DialogActivityDesc = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
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
        jLabelWk2LnActivity = new javax.swing.JLabel();
        jDialogWaitingEmail = new javax.swing.JDialog();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanelGen = new javax.swing.JPanel();
        jLabelLogo = new javax.swing.JLabel();
        jLabelHeaderGen = new javax.swing.JLabel();
        jLabelGenDate = new javax.swing.JLabel();
        jLabelGenTime = new javax.swing.JLabel();
        jLabelGenLogNam6 = new javax.swing.JLabel();
        jLabelGenLogNam = new javax.swing.JLabel();
        jLabelEmpNumber = new javax.swing.JLabel();
        jLabelEmpNam = new javax.swing.JLabel();
        jSeparator11 = new javax.swing.JSeparator();
        jLabelSepDetails = new javax.swing.JLabel();
        jLabelProvince2 = new javax.swing.JLabel();
        jLabelBank = new javax.swing.JLabel();
        jLabelBankName = new javax.swing.JLabel();
        jSeparator12 = new javax.swing.JSeparator();
        jLabelEmpTitle = new javax.swing.JLabel();
        jLabelFinDetails = new javax.swing.JLabel();
        jLabelActMainPurpose = new javax.swing.JLabel();
        jTextActMainPurpose = new javax.swing.JTextField();
        jPanelAllowanceSubTot = new javax.swing.JPanel();
        jLabelIncidentalSub = new javax.swing.JLabel();
        jLabelIncidentalSubTot = new javax.swing.JLabel();
        jLabelLunchSub = new javax.swing.JLabel();
        jLabelDinnerSub = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabelBreakFastSub = new javax.swing.JLabel();
        jLabelBreakFastSubTot = new javax.swing.JLabel();
        jLabelLunchSubTot = new javax.swing.JLabel();
        jLabelDinnerSubTot = new javax.swing.JLabel();
        jPanelMiscSubTot = new javax.swing.JPanel();
        jLabelMiscSubTot = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabelMscSub = new javax.swing.JLabel();
        jPanelAccSubTot = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabelAccUnprovedSubTot = new javax.swing.JLabel();
        jLabelAccProvedSub = new javax.swing.JLabel();
        jLabelAccProvedSubTot = new javax.swing.JLabel();
        jLabelAccUnprovedSub = new javax.swing.JLabel();
        jLabelAppTotReqCost = new javax.swing.JLabel();
        jLabelAppTotReq = new javax.swing.JLabel();
        jLabelOffice = new javax.swing.JLabel();
        jLabelAccNum = new javax.swing.JLabel();
        jLabelACC = new javax.swing.JLabel();
        jLabelNum = new javax.swing.JLabel();
        jLabelRegNum = new javax.swing.JLabel();
        jLabelRegYear = new javax.swing.JLabel();
        jLabelSerial = new javax.swing.JLabel();
        jLabelEmp = new javax.swing.JLabel();
        jLabelEmpNum = new javax.swing.JLabel();
        jLabelEmpProvince = new javax.swing.JLabel();
        jLabelEmpOffice = new javax.swing.JLabel();
        jPanelWkOne = new javax.swing.JPanel();
        jLabelLineLogNam = new javax.swing.JLabel();
        jLabelGenLogNam1 = new javax.swing.JLabel();
        jLabelLineTime = new javax.swing.JLabel();
        jLabelLineDate = new javax.swing.JLabel();
        jLabelWkDuration = new javax.swing.JLabel();
        jLabelWk1From = new javax.swing.JLabel();
        jLabelWk1To = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableWk1Activities = new javax.swing.JTable();
        jLabelLogo1 = new javax.swing.JLabel();
        jLabelWk1DateFrom = new javax.swing.JLabel();
        jLabelWk1DateTo = new javax.swing.JLabel();
        jLabelHeaderGen6 = new javax.swing.JLabel();
        jPanelWkTwo = new javax.swing.JPanel();
        jLabelLineLogNam1 = new javax.swing.JLabel();
        jLabelGenLogNam3 = new javax.swing.JLabel();
        jLabelLineTime1 = new javax.swing.JLabel();
        jLabelLineDate1 = new javax.swing.JLabel();
        jLabelWkDuration1 = new javax.swing.JLabel();
        jLabelWk1From1 = new javax.swing.JLabel();
        jLabelWk1To1 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableWk2Activities = new javax.swing.JTable();
        jLabelLogo2 = new javax.swing.JLabel();
        jLabelWk2DateFrom = new javax.swing.JLabel();
        jLabelWk2DateTo = new javax.swing.JLabel();
        jLabelHeaderGen7 = new javax.swing.JLabel();
        jPanelWkThree = new javax.swing.JPanel();
        jLabelLineLogNam2 = new javax.swing.JLabel();
        jLabelGenLogNam5 = new javax.swing.JLabel();
        jLabelLineTime2 = new javax.swing.JLabel();
        jLabelLineDate2 = new javax.swing.JLabel();
        jLabelWkDuration2 = new javax.swing.JLabel();
        jLabelWk3From = new javax.swing.JLabel();
        jLabelWk3To = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableWk3Activities = new javax.swing.JTable();
        jLabelLogo3 = new javax.swing.JLabel();
        jLabelWk3DateFrom = new javax.swing.JLabel();
        jLabelWk3DateTo = new javax.swing.JLabel();
        jLabelHeaderGen4 = new javax.swing.JLabel();
        jPanelDocAttach = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jButtonDelete1 = new javax.swing.JButton();
        jButtonChoose1 = new javax.swing.JButton();
        jLabelLogo4 = new javax.swing.JLabel();
        jLabelHeaderLine2 = new javax.swing.JLabel();
        jLabelLineDate3 = new javax.swing.JLabel();
        jLabelLineTime3 = new javax.swing.JLabel();
        jLabellogged2 = new javax.swing.JLabel();
        jLabelLineLogNam3 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jPanel9 = new javax.swing.JPanel();
        jLabelImg = new javax.swing.JLabel();
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
        jSeparator13 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSubmit = new javax.swing.JMenuItem();
        jSeparator35 = new javax.swing.JPopupMenu.Separator();
        jMenuEdit = new javax.swing.JMenu();
        jMenuReqEdit = new javax.swing.JMenuItem();
        jSeparator29 = new javax.swing.JPopupMenu.Separator();
        jMenuAcqEdit = new javax.swing.JMenuItem();
        jSeparator30 = new javax.swing.JPopupMenu.Separator();
        jMenuMonPlanEdit = new javax.swing.JMenuItem();
        jSeparator15 = new javax.swing.JPopupMenu.Separator();
        jMenuItemClose = new javax.swing.JMenuItem();
        jSeparator16 = new javax.swing.JPopupMenu.Separator();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuRequest = new javax.swing.JMenu();
        jMenuItemSupApp = new javax.swing.JMenuItem();
        jSeparator17 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAccMgrRev = new javax.swing.JMenuItem();
        jSeparator18 = new javax.swing.JPopupMenu.Separator();
        jMenuItemHeadApp = new javax.swing.JMenuItem();
        jSeparator20 = new javax.swing.JPopupMenu.Separator();
        jMenuPlanApproval = new javax.swing.JMenuItem();
        jSeparator21 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSchGen = new javax.swing.JMenuItem();
        jSeparator22 = new javax.swing.JPopupMenu.Separator();
        jMenuItemView = new javax.swing.JMenuItem();
        jMenuAcquittal = new javax.swing.JMenu();
        jMenuItemAcqSupApp = new javax.swing.JMenuItem();
        jSeparator23 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqAccApp = new javax.swing.JMenuItem();
        jSeparator24 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqHeadApp = new javax.swing.JMenuItem();
        jSeparator25 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqFinApp = new javax.swing.JMenuItem();
        jSeparator26 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqSchGen = new javax.swing.JMenuItem();
        jSeparator27 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqView = new javax.swing.JMenuItem();
        jMenuMonthlyPlan = new javax.swing.JMenu();
        jMenuItemProvMgrApproval = new javax.swing.JMenuItem();
        jSeparator31 = new javax.swing.JPopupMenu.Separator();
        jMenuItemReqGenLst = new javax.swing.JMenuItem();
        jSeparator38 = new javax.swing.JPopupMenu.Separator();
        jMenuItemPlanView = new javax.swing.JMenuItem();
        jMenuReports = new javax.swing.JMenu();
        jMenuItemReqSubmitted = new javax.swing.JMenuItem();
        jSeparator32 = new javax.swing.JPopupMenu.Separator();
        jMenuAgeAlluser = new javax.swing.JMenuItem();
        jSeparator33 = new javax.swing.JPopupMenu.Separator();
        jMenuAgeByUser = new javax.swing.JMenuItem();
        jSeparator34 = new javax.swing.JPopupMenu.Separator();
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
        jButtonOkFacility.setBounds(180, 220, 80, 25);

        jButtonCancelFacility.setText("Cancel");
        jButtonCancelFacility.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelFacilityActionPerformed(evt);
            }
        });
        jPanel10.add(jButtonCancelFacility);
        jButtonCancelFacility.setBounds(300, 220, 80, 25);

        jLabelProvinceFacility.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelProvinceFacility.setText("Province");
        jPanel10.add(jLabelProvinceFacility);
        jLabelProvinceFacility.setBounds(20, 70, 70, 30);

        jComboProvinceFacility.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jComboProvinceFacilityMouseEntered(evt);
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
        });
        jComboFacility.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboFacilityActionPerformed(evt);
            }
        });
        jPanel10.add(jComboFacility);
        jComboFacility.setBounds(150, 150, 320, 30);

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
        jButtonOkFacility1.setBounds(180, 200, 80, 25);

        jButtonCancelBudget.setText("Cancel");
        jButtonCancelBudget.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelBudgetActionPerformed(evt);
            }
        });
        jPanel11.add(jButtonCancelBudget);
        jButtonCancelBudget.setBounds(300, 200, 80, 25);

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

        jDialogAckSalDeduct.setAlwaysOnTop(true);
        jDialogAckSalDeduct.setLocation(new java.awt.Point(650, 350));
        jDialogAckSalDeduct.setMinimumSize(new java.awt.Dimension(620, 220));
        jDialogAckSalDeduct.setResizable(false);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setMinimumSize(new java.awt.Dimension(620, 220));
        jPanel7.setLayout(null);

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel7.setText("<html>I shall acquit this perdiem request within five days, after the last activity.If I fail to submit an acquittal for this perdiem request in<br> the prescribed period, I authorise OPHID to deduct the unacquitted perdiem request amount from my salary without further notice.<br><br>I acknowledge that failure to submit acquittal for this perdiem request  <b>may</b> result in a displinary action. </html>");
        jLabel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel7.add(jLabel7);
        jLabel7.setBounds(10, 50, 590, 80);

        jCheckBoxSalAckAgree.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jCheckBoxSalAckAgree.setText(" I have read and I agree with the above statement ");
        jCheckBoxSalAckAgree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxSalAckAgreeActionPerformed(evt);
            }
        });
        jPanel7.add(jCheckBoxSalAckAgree);
        jCheckBoxSalAckAgree.setBounds(10, 130, 590, 25);

        jButtonAckSalCont.setText("Continue ");
        jButtonAckSalCont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAckSalContActionPerformed(evt);
            }
        });
        jPanel7.add(jButtonAckSalCont);
        jButtonAckSalCont.setBounds(160, 160, 100, 25);

        jButtonSalAckCancel.setText("Cancel");
        jButtonSalAckCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalAckCancelActionPerformed(evt);
            }
        });
        jPanel7.add(jButtonSalAckCancel);
        jButtonSalAckCancel.setBounds(310, 160, 80, 25);

        jLabelHeaderAck1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelHeaderAck1.setForeground(new java.awt.Color(0, 0, 204));
        jLabelHeaderAck1.setText("ACKNOWLEDGEMENT NOTICE");
        jPanel7.add(jLabelHeaderAck1);
        jLabelHeaderAck1.setBounds(170, 0, 300, 40);

        javax.swing.GroupLayout jDialogAckSalDeductLayout = new javax.swing.GroupLayout(jDialogAckSalDeduct.getContentPane());
        jDialogAckSalDeduct.getContentPane().setLayout(jDialogAckSalDeductLayout);
        jDialogAckSalDeductLayout.setHorizontalGroup(
            jDialogAckSalDeductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 620, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jDialogAckSalDeductLayout.setVerticalGroup(
            jDialogAckSalDeductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jDialogFacility1.setLocation(new java.awt.Point(600, 300));
        jDialogFacility1.setMinimumSize(new java.awt.Dimension(600, 300));
        jDialogFacility1.setResizable(false);

        jPanel12.setBackground(new java.awt.Color(204, 153, 14));
        jPanel12.setLayout(null);

        jLabelHeader3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelHeader3.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeader3.setText("FACILITY TO VISIT");
        jPanel12.add(jLabelHeader3);
        jLabelHeader3.setBounds(170, 10, 230, 40);

        jButtonOkFacility2.setText("Ok ");
        jButtonOkFacility2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkFacility2ActionPerformed(evt);
            }
        });
        jPanel12.add(jButtonOkFacility2);
        jButtonOkFacility2.setBounds(180, 220, 80, 25);

        jButtonCancelFacility1.setText("Cancel");
        jButtonCancelFacility1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelFacility1ActionPerformed(evt);
            }
        });
        jPanel12.add(jButtonCancelFacility1);
        jButtonCancelFacility1.setBounds(300, 220, 80, 25);

        jLabelProvinceFacility1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelProvinceFacility1.setText("Province");
        jPanel12.add(jLabelProvinceFacility1);
        jLabelProvinceFacility1.setBounds(20, 70, 70, 30);

        jComboProvinceFacility1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jComboProvinceFacility1MouseEntered(evt);
            }
        });
        jComboProvinceFacility1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboProvinceFacility1KeyPressed(evt);
            }
        });
        jPanel12.add(jComboProvinceFacility1);
        jComboProvinceFacility1.setBounds(150, 70, 230, 30);

        jComboDistrictFacility1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jComboDistrictFacility1MouseEntered(evt);
            }
        });
        jComboDistrictFacility1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboDistrictFacility1ActionPerformed(evt);
            }
        });
        jPanel12.add(jComboDistrictFacility1);
        jComboDistrictFacility1.setBounds(150, 110, 230, 30);

        jLabelDistrictFacility1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDistrictFacility1.setText("District");
        jPanel12.add(jLabelDistrictFacility1);
        jLabelDistrictFacility1.setBounds(20, 110, 70, 30);

        jLabelFacility1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelFacility1.setText("Facility");
        jPanel12.add(jLabelFacility1);
        jLabelFacility1.setBounds(20, 150, 70, 30);

        jComboFacility1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jComboFacility1FocusGained(evt);
            }
        });
        jComboFacility1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jComboFacility1MouseEntered(evt);
            }
        });
        jComboFacility1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboFacility1ActionPerformed(evt);
            }
        });
        jPanel12.add(jComboFacility1);
        jComboFacility1.setBounds(150, 150, 320, 30);

        jLabelDisFacilty.setText("jLabel4");
        jPanel12.add(jLabelDisFacilty);
        jLabelDisFacilty.setBounds(150, 180, 400, 30);

        jLabelDisProvince.setText("jLabel4");
        jPanel12.add(jLabelDisProvince);
        jLabelDisProvince.setBounds(400, 70, 180, 30);

        jLabelDisDistrict.setText("jLabel4");
        jPanel12.add(jLabelDisDistrict);
        jLabelDisDistrict.setBounds(400, 110, 180, 30);

        jLabelFacDist.setText("jLabel4");
        jPanel12.add(jLabelFacDist);
        jLabelFacDist.setBounds(500, 156, 50, 20);

        javax.swing.GroupLayout jDialogFacility1Layout = new javax.swing.GroupLayout(jDialogFacility1.getContentPane());
        jDialogFacility1.getContentPane().setLayout(jDialogFacility1Layout);
        jDialogFacility1Layout.setHorizontalGroup(
            jDialogFacility1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
        );
        jDialogFacility1Layout.setVerticalGroup(
            jDialogFacility1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        jDialogAckSubmit.setAlwaysOnTop(true);
        jDialogAckSubmit.setLocation(new java.awt.Point(650, 350));
        jDialogAckSubmit.setMinimumSize(new java.awt.Dimension(620, 205));
        jDialogAckSubmit.setResizable(false);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setMinimumSize(new java.awt.Dimension(620, 205));
        jPanel5.setLayout(null);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel3.setText("<html> Submission of a perdiem request does not gaurantee  approval. In the event of a rejection, a new perdiem requested<br> should be submiited within 24hours of the rejection.<br><b><u>NOTE</u></b> No activity shall be carried out without an approved perdiem request from Central Head. </html>");
        jLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.add(jLabel3);
        jLabel3.setBounds(10, 40, 590, 60);

        jCheckBoxSalSubmitAgree.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jCheckBoxSalSubmitAgree.setText(" I have read and I  agree with the above statement ");
        jCheckBoxSalSubmitAgree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxSalSubmitAgreeActionPerformed(evt);
            }
        });
        jPanel5.add(jCheckBoxSalSubmitAgree);
        jCheckBoxSalSubmitAgree.setBounds(10, 100, 590, 25);

        jButtonAckSubmitCont.setText("Continue ");
        jButtonAckSubmitCont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAckSubmitContActionPerformed(evt);
            }
        });
        jPanel5.add(jButtonAckSubmitCont);
        jButtonAckSubmitCont.setBounds(160, 140, 90, 25);

        jButtonSalSubmitCancel.setText("Cancel");
        jButtonSalSubmitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalSubmitCancelActionPerformed(evt);
            }
        });
        jPanel5.add(jButtonSalSubmitCancel);
        jButtonSalSubmitCancel.setBounds(310, 140, 80, 25);

        jLabelHeaderAck.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelHeaderAck.setForeground(new java.awt.Color(0, 0, 204));
        jLabelHeaderAck.setText("ACKNOWLEDGEMENT NOTICE");
        jPanel5.add(jLabelHeaderAck);
        jLabelHeaderAck.setBounds(170, 0, 310, 40);

        javax.swing.GroupLayout jDialogAckSubmitLayout = new javax.swing.GroupLayout(jDialogAckSubmit.getContentPane());
        jDialogAckSubmit.getContentPane().setLayout(jDialogAckSubmitLayout);
        jDialogAckSubmitLayout.setHorizontalGroup(
            jDialogAckSubmitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 620, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jDialogAckSubmitLayout.setVerticalGroup(
            jDialogAckSubmitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jDialogWk1.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialogWk1.setTitle("VIEW ACTIVITY DETAILS");
        jDialogWk1.setAlwaysOnTop(true);
        jDialogWk1.setBackground(new java.awt.Color(255, 255, 204));
        jDialogWk1.setLocation(new java.awt.Point(450, 100));
        jDialogWk1.setName("dialogWk1"); // NOI18N
        jDialogWk1.setSize(new java.awt.Dimension(825, 490));

        jPanel1.setBackground(new java.awt.Color(237, 235, 92));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 490));
        jPanel1.setLayout(null);

        jLabelDialogWk1Site.setText("Site");
        jPanel1.add(jLabelDialogWk1Site);
        jLabelDialogWk1Site.setBounds(20, 100, 40, 16);

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
        jTextFieldDialogWk1Site.setBounds(20, 130, 420, 30);
        jPanel1.add(jTextFieldWk1DialogActivityDesc);
        jTextFieldWk1DialogActivityDesc.setBounds(20, 350, 480, 30);

        jLabelWk1DialogActivityDesc.setText("Activity Descrpition");
        jPanel1.add(jLabelWk1DialogActivityDesc);
        jLabelWk1DialogActivityDesc.setBounds(20, 310, 130, 30);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel1.add(jSeparator3);
        jSeparator3.setBounds(530, 90, 20, 310);

        jLabelWk1DialogBudget.setText("Budget Line");
        jPanel1.add(jLabelWk1DialogBudget);
        jLabelWk1DialogBudget.setBounds(20, 230, 130, 30);

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
        jTextFieldWk1DialogBudget.setBounds(20, 270, 480, 30);

        jCheckBoxDialogWk1BrkFast.setText(" Breakfast");
        jPanel1.add(jCheckBoxDialogWk1BrkFast);
        jCheckBoxDialogWk1BrkFast.setBounds(540, 100, 90, 25);

        jCheckBoxDialogWk1Lunch.setText("Lunch");
        jPanel1.add(jCheckBoxDialogWk1Lunch);
        jCheckBoxDialogWk1Lunch.setBounds(660, 100, 80, 25);

        jCheckBoxDialogWk1Dinner.setText(" Dinner");
        jPanel1.add(jCheckBoxDialogWk1Dinner);
        jCheckBoxDialogWk1Dinner.setBounds(540, 150, 90, 25);

        jCheckBoxDialogWk1Acc.setText(" Unproved Acc");
        jPanel1.add(jCheckBoxDialogWk1Acc);
        jCheckBoxDialogWk1Acc.setBounds(660, 150, 130, 25);

        jLabelDialogWk1Dis.setText("Km");
        jPanel1.add(jLabelDialogWk1Dis);
        jLabelDialogWk1Dis.setBounds(280, 180, 50, 30);
        jPanel1.add(jLabelWk1DialogActDis);
        jLabelWk1DialogActDis.setBounds(210, 180, 70, 30);

        jLabelDialogWk1Dis1.setText("Distance From Base");
        jPanel1.add(jLabelDialogWk1Dis1);
        jLabelDialogWk1Dis1.setBounds(20, 180, 130, 30);

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
        jCheckBoxDialogWk1Misc.setBounds(660, 200, 140, 25);

        jCheckBoxDialogWk1Inc.setText("Incidental");
        jPanel1.add(jCheckBoxDialogWk1Inc);
        jCheckBoxDialogWk1Inc.setBounds(540, 200, 110, 25);

        jLabelWk1Misc.setText("Miscellaneous Desc");
        jPanel1.add(jLabelWk1Misc);
        jLabelWk1Misc.setBounds(550, 260, 160, 30);
        jPanel1.add(jTextFieldWk1Misc);
        jTextFieldWk1Misc.setBounds(550, 290, 110, 30);

        jLabelWk1MiscAmt.setText("$");
        jPanel1.add(jLabelWk1MiscAmt);
        jLabelWk1MiscAmt.setBounds(680, 260, 30, 30);
        jPanel1.add(jTextFieldWk1MiscAmt);
        jTextFieldWk1MiscAmt.setBounds(720, 290, 70, 30);

        jLabelWk1LnActivity.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jPanel1.add(jLabelWk1LnActivity);
        jLabelWk1LnActivity.setBounds(220, 20, 460, 30);

        javax.swing.GroupLayout jDialogWk1Layout = new javax.swing.GroupLayout(jDialogWk1.getContentPane());
        jDialogWk1.getContentPane().setLayout(jDialogWk1Layout);
        jDialogWk1Layout.setHorizontalGroup(
            jDialogWk1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
        );
        jDialogWk1Layout.setVerticalGroup(
            jDialogWk1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogWk1Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jDialogWk3.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialogWk3.setTitle("VIEW ACTIVITY DETAILS");
        jDialogWk3.setAlwaysOnTop(true);
        jDialogWk3.setBackground(new java.awt.Color(255, 255, 204));
        jDialogWk3.setLocation(new java.awt.Point(300, 100));
        jDialogWk3.setName("dialogWk1"); // NOI18N
        jDialogWk3.setSize(new java.awt.Dimension(825, 490));

        jPanel6.setBackground(new java.awt.Color(84, 110, 240));
        jPanel6.setPreferredSize(new java.awt.Dimension(800, 650));
        jPanel6.setLayout(null);

        jLabelDialogWk3Site.setText("Site");
        jPanel6.add(jLabelDialogWk3Site);
        jLabelDialogWk3Site.setBounds(20, 90, 40, 16);

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
        jPanel6.add(jTextFieldDialogWk3Site);
        jTextFieldDialogWk3Site.setBounds(20, 120, 420, 30);
        jPanel6.add(jTextFieldWk3DialogActivityDesc);
        jTextFieldWk3DialogActivityDesc.setBounds(20, 370, 410, 30);

        jLabelWk3DialogActivityDesc.setText("Activity Descrpition");
        jPanel6.add(jLabelWk3DialogActivityDesc);
        jLabelWk3DialogActivityDesc.setBounds(20, 330, 130, 30);

        jSeparator36.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel6.add(jSeparator36);
        jSeparator36.setBounds(500, 100, 20, 340);

        jLabelWk3DialogBudget.setText("Budget Line");
        jPanel6.add(jLabelWk3DialogBudget);
        jLabelWk3DialogBudget.setBounds(20, 240, 130, 30);

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
        jPanel6.add(jTextFieldWk3DialogBudget);
        jTextFieldWk3DialogBudget.setBounds(20, 280, 410, 30);

        jCheckBoxDialogWk3BrkFast.setText(" Breakfast");
        jPanel6.add(jCheckBoxDialogWk3BrkFast);
        jCheckBoxDialogWk3BrkFast.setBounds(550, 110, 90, 25);

        jCheckBoxDialogWk3Lunch.setText("Lunch");
        jPanel6.add(jCheckBoxDialogWk3Lunch);
        jCheckBoxDialogWk3Lunch.setBounds(670, 110, 80, 25);

        jCheckBoxDialogWk3Dinner.setText(" Dinner");
        jPanel6.add(jCheckBoxDialogWk3Dinner);
        jCheckBoxDialogWk3Dinner.setBounds(550, 160, 90, 25);

        jCheckBoxDialogWk3Acc.setText(" Unproved Acc");
        jPanel6.add(jCheckBoxDialogWk3Acc);
        jCheckBoxDialogWk3Acc.setBounds(670, 160, 130, 25);

        jLabelDialogWk2Dis3.setText("Km");
        jPanel6.add(jLabelDialogWk2Dis3);
        jLabelDialogWk2Dis3.setBounds(280, 190, 50, 30);
        jPanel6.add(jLabelWk3DialogActDis);
        jLabelWk3DialogActDis.setBounds(210, 190, 70, 30);

        jLabelDialogWk3Dis.setText("Distance From Base");
        jPanel6.add(jLabelDialogWk3Dis);
        jLabelDialogWk3Dis.setBounds(20, 190, 130, 30);

        jLabelRemain3.setFont(new java.awt.Font("Tahoma", 3, 10)); // NOI18N
        jPanel6.add(jLabelRemain3);
        jLabelRemain3.setBounds(20, 440, 250, 20);

        jCheckBoxDialogWk3Misc.setText("Miscellaneous");
        jCheckBoxDialogWk3Misc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDialogWk3MiscActionPerformed(evt);
            }
        });
        jPanel6.add(jCheckBoxDialogWk3Misc);
        jCheckBoxDialogWk3Misc.setBounds(670, 210, 140, 25);

        jCheckBoxDialogWk3Inc.setText("Incidental");
        jPanel6.add(jCheckBoxDialogWk3Inc);
        jCheckBoxDialogWk3Inc.setBounds(550, 210, 110, 25);

        jLabelWk3Misc.setText("Miscellaneous Desc");
        jPanel6.add(jLabelWk3Misc);
        jLabelWk3Misc.setBounds(550, 280, 160, 30);
        jPanel6.add(jTextFieldWk3Misc);
        jTextFieldWk3Misc.setBounds(550, 330, 110, 30);

        jLabelWk3MiscAmt.setText("$");
        jPanel6.add(jLabelWk3MiscAmt);
        jLabelWk3MiscAmt.setBounds(680, 330, 30, 30);
        jPanel6.add(jTextFieldWk3MiscAmt);
        jTextFieldWk3MiscAmt.setBounds(720, 330, 70, 30);

        jLabelWk3LnActivity.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabelWk3LnActivity.setForeground(new java.awt.Color(255, 255, 255));
        jPanel6.add(jLabelWk3LnActivity);
        jLabelWk3LnActivity.setBounds(220, 20, 460, 30);

        javax.swing.GroupLayout jDialogWk3Layout = new javax.swing.GroupLayout(jDialogWk3.getContentPane());
        jDialogWk3.getContentPane().setLayout(jDialogWk3Layout);
        jDialogWk3Layout.setHorizontalGroup(
            jDialogWk3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
        );
        jDialogWk3Layout.setVerticalGroup(
            jDialogWk3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jDialogWk2.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialogWk2.setTitle("VIEW ACTIVITY DETAILS");
        jDialogWk2.setAlwaysOnTop(true);
        jDialogWk2.setBackground(new java.awt.Color(255, 255, 204));
        jDialogWk2.setLocation(new java.awt.Point(450, 100));
        jDialogWk2.setName("dialogWk1"); // NOI18N
        jDialogWk2.setSize(new java.awt.Dimension(825, 490));

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));
        jPanel2.setPreferredSize(new java.awt.Dimension(800, 650));
        jPanel2.setLayout(null);

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
        jPanel2.add(jTextFieldWk2DialogActivityDesc);
        jTextFieldWk2DialogActivityDesc.setBounds(20, 370, 410, 30);

        jLabelWk2DialogActivityDesc.setText("Activity Descrpition");
        jPanel2.add(jLabelWk2DialogActivityDesc);
        jLabelWk2DialogActivityDesc.setBounds(20, 330, 130, 30);

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel2.add(jSeparator6);
        jSeparator6.setBounds(530, 90, 20, 340);

        jLabelWk2DialogBudget.setText("Budget Line");
        jPanel2.add(jLabelWk2DialogBudget);
        jLabelWk2DialogBudget.setBounds(20, 220, 130, 30);

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
        jTextFieldWk2DialogBudget.setBounds(20, 260, 410, 30);

        jCheckBoxDialogWk2BrkFast.setText(" Breakfast");
        jPanel2.add(jCheckBoxDialogWk2BrkFast);
        jCheckBoxDialogWk2BrkFast.setBounds(560, 110, 90, 25);

        jCheckBoxDialogWk2Lunch.setText("Lunch");
        jPanel2.add(jCheckBoxDialogWk2Lunch);
        jCheckBoxDialogWk2Lunch.setBounds(680, 110, 80, 25);

        jCheckBoxDialogWk2Dinner.setText(" Dinner");
        jPanel2.add(jCheckBoxDialogWk2Dinner);
        jCheckBoxDialogWk2Dinner.setBounds(560, 160, 90, 25);

        jCheckBoxDialogWk2Acc.setText(" Unproved Acc");
        jPanel2.add(jCheckBoxDialogWk2Acc);
        jCheckBoxDialogWk2Acc.setBounds(680, 160, 130, 25);

        jLabelDialogWk2Dis2.setText("Km");
        jPanel2.add(jLabelDialogWk2Dis2);
        jLabelDialogWk2Dis2.setBounds(280, 170, 50, 30);
        jPanel2.add(jLabelWk2DialogActDis);
        jLabelWk2DialogActDis.setBounds(210, 170, 70, 30);

        jLabelDialogWk2Dis.setText("Distance From Base");
        jPanel2.add(jLabelDialogWk2Dis);
        jLabelDialogWk2Dis.setBounds(20, 170, 130, 30);

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
        jCheckBoxDialogWk2Misc.setBounds(680, 210, 140, 25);

        jCheckBoxDialogWk2Inc.setText("Incidental");
        jPanel2.add(jCheckBoxDialogWk2Inc);
        jCheckBoxDialogWk2Inc.setBounds(560, 210, 110, 25);

        jLabelWk2Misc.setText("Miscellaneous Desc");
        jPanel2.add(jLabelWk2Misc);
        jLabelWk2Misc.setBounds(570, 280, 160, 30);
        jPanel2.add(jTextFieldWk2Misc);
        jTextFieldWk2Misc.setBounds(570, 310, 110, 30);

        jLabelWk2MiscAmt.setText("$");
        jPanel2.add(jLabelWk2MiscAmt);
        jLabelWk2MiscAmt.setBounds(700, 310, 30, 30);
        jPanel2.add(jTextFieldWk2MiscAmt);
        jTextFieldWk2MiscAmt.setBounds(740, 310, 70, 30);

        jLabelWk2LnActivity.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jPanel2.add(jLabelWk2LnActivity);
        jLabelWk2LnActivity.setBounds(220, 20, 460, 30);

        javax.swing.GroupLayout jDialogWk2Layout = new javax.swing.GroupLayout(jDialogWk2.getContentPane());
        jDialogWk2.getContentPane().setLayout(jDialogWk2Layout);
        jDialogWk2Layout.setHorizontalGroup(
            jDialogWk2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
        );
        jDialogWk2Layout.setVerticalGroup(
            jDialogWk2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanelGen.setBackground(new java.awt.Color(25, 142, 90));
        jPanelGen.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jPanelGen.setPreferredSize(new java.awt.Dimension(1280, 677));
        jPanelGen.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                jPanelGenComponentAdded(evt);
            }
        });
        jPanelGen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPanelGenFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jPanelGenFocusLost(evt);
            }
        });
        jPanelGen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelGenMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanelGenMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanelGenMousePressed(evt);
            }
        });
        jPanelGen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPanelGenKeyPressed(evt);
            }
        });
        jPanelGen.setLayout(null);

        jLabelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ophidlog.jpg"))); // NOI18N
        jPanelGen.add(jLabelLogo);
        jLabelLogo.setBounds(10, 10, 220, 100);

        jLabelHeaderGen.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelGen.add(jLabelHeaderGen);
        jLabelHeaderGen.setBounds(350, 40, 610, 40);

        jLabelGenDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelGen.add(jLabelGenDate);
        jLabelGenDate.setBounds(1090, 0, 110, 30);

        jLabelGenTime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelGen.add(jLabelGenTime);
        jLabelGenTime.setBounds(1250, 0, 80, 30);

        jLabelGenLogNam6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam6.setText("Logged In");
        jPanelGen.add(jLabelGenLogNam6);
        jLabelGenLogNam6.setBounds(1030, 30, 100, 30);

        jLabelGenLogNam.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelGenLogNam);
        jLabelGenLogNam.setBounds(1120, 30, 220, 30);

        jLabelEmpNumber.setText("Employee Number");
        jPanelGen.add(jLabelEmpNumber);
        jLabelEmpNumber.setBounds(20, 140, 110, 30);
        jPanelGen.add(jLabelEmpNam);
        jLabelEmpNam.setBounds(300, 140, 340, 30);
        jPanelGen.add(jSeparator11);
        jSeparator11.setBounds(10, 210, 1340, 2);

        jLabelSepDetails.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelSepDetails.setForeground(new java.awt.Color(255, 0, 0));
        jLabelSepDetails.setText("Employee Details");
        jPanelGen.add(jLabelSepDetails);
        jLabelSepDetails.setBounds(20, 185, 140, 30);

        jLabelProvince2.setText("Province");
        jPanelGen.add(jLabelProvince2);
        jLabelProvince2.setBounds(20, 220, 70, 30);

        jLabelBank.setText("Bank");
        jPanelGen.add(jLabelBank);
        jLabelBank.setBounds(20, 260, 40, 30);
        jPanelGen.add(jLabelBankName);
        jLabelBankName.setBounds(150, 260, 220, 30);
        jPanelGen.add(jSeparator12);
        jSeparator12.setBounds(10, 330, 1340, 2);
        jPanelGen.add(jLabelEmpTitle);
        jLabelEmpTitle.setBounds(760, 140, 400, 30);

        jLabelFinDetails.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelFinDetails.setForeground(new java.awt.Color(255, 0, 0));
        jLabelFinDetails.setText("Financial Details");
        jPanelGen.add(jLabelFinDetails);
        jLabelFinDetails.setBounds(20, 300, 110, 30);

        jLabelActMainPurpose.setText("Activity Main Purpose");
        jPanelGen.add(jLabelActMainPurpose);
        jLabelActMainPurpose.setBounds(10, 380, 130, 30);

        jTextActMainPurpose.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextActMainPurposeFocusGained(evt);
            }
        });
        jTextActMainPurpose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextActMainPurposeActionPerformed(evt);
            }
        });
        jPanelGen.add(jTextActMainPurpose);
        jTextActMainPurpose.setBounds(150, 380, 760, 30);

        jPanelAllowanceSubTot.setBackground(new java.awt.Color(255, 255, 255));
        jPanelAllowanceSubTot.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPanelAllowanceSubTotFocusGained(evt);
            }
        });
        jPanelAllowanceSubTot.setLayout(null);

        jLabelIncidentalSub.setText("Incidental");
        jPanelAllowanceSubTot.add(jLabelIncidentalSub);
        jLabelIncidentalSub.setBounds(10, 120, 60, 25);

        jLabelIncidentalSubTot.setText("0.00");
        jPanelAllowanceSubTot.add(jLabelIncidentalSubTot);
        jLabelIncidentalSubTot.setBounds(140, 120, 60, 25);

        jLabelLunchSub.setText("Lunch");
        jPanelAllowanceSubTot.add(jLabelLunchSub);
        jLabelLunchSub.setBounds(10, 60, 60, 25);

        jLabelDinnerSub.setText("Dinner");
        jPanelAllowanceSubTot.add(jLabelDinnerSub);
        jLabelDinnerSub.setBounds(10, 90, 60, 25);

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel4.setLayout(null);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel18.setText("Allowances Totals ");
        jPanel4.add(jLabel18);
        jLabel18.setBounds(10, 10, 120, 20);

        jLabel19.setText("Incidental");
        jPanel4.add(jLabel19);
        jLabel19.setBounds(10, 130, 60, 20);

        jLabel20.setText("Breakfast");
        jPanel4.add(jLabel20);
        jLabel20.setBounds(10, 40, 60, 20);

        jLabel21.setText("Lunch");
        jPanel4.add(jLabel21);
        jLabel21.setBounds(10, 70, 60, 20);

        jLabel22.setText("Dinner");
        jPanel4.add(jLabel22);
        jLabel22.setBounds(10, 100, 60, 20);

        jPanelAllowanceSubTot.add(jPanel4);
        jPanel4.setBounds(20, 410, 320, 160);

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel23.setText("Allowances Totals ");
        jPanelAllowanceSubTot.add(jLabel23);
        jLabel23.setBounds(8, 5, 120, 25);

        jLabelBreakFastSub.setText("Breakfast");
        jPanelAllowanceSubTot.add(jLabelBreakFastSub);
        jLabelBreakFastSub.setBounds(10, 30, 60, 25);

        jLabelBreakFastSubTot.setText("0.00");
        jPanelAllowanceSubTot.add(jLabelBreakFastSubTot);
        jLabelBreakFastSubTot.setBounds(140, 30, 60, 25);

        jLabelLunchSubTot.setText("0.00");
        jPanelAllowanceSubTot.add(jLabelLunchSubTot);
        jLabelLunchSubTot.setBounds(140, 60, 60, 25);

        jLabelDinnerSubTot.setText("0.00");
        jPanelAllowanceSubTot.add(jLabelDinnerSubTot);
        jLabelDinnerSubTot.setBounds(140, 90, 60, 25);

        jPanelGen.add(jPanelAllowanceSubTot);
        jPanelAllowanceSubTot.setBounds(20, 470, 320, 150);

        jPanelMiscSubTot.setBackground(new java.awt.Color(255, 255, 255));
        jPanelMiscSubTot.setLayout(null);

        jLabelMiscSubTot.setText("0.00");
        jPanelMiscSubTot.add(jLabelMiscSubTot);
        jLabelMiscSubTot.setBounds(100, 30, 70, 25);

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel29.setText("Miscellaneous  Totals ");
        jPanelMiscSubTot.add(jLabel29);
        jLabel29.setBounds(8, 5, 140, 25);

        jLabelMscSub.setText("Miscellaneous");
        jPanelMiscSubTot.add(jLabelMscSub);
        jLabelMscSub.setBounds(8, 30, 80, 25);

        jPanelGen.add(jPanelMiscSubTot);
        jPanelMiscSubTot.setBounds(400, 470, 210, 150);

        jPanelAccSubTot.setBackground(new java.awt.Color(255, 255, 255));
        jPanelAccSubTot.setLayout(null);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setText("Accomodation Totals ");
        jPanelAccSubTot.add(jLabel11);
        jLabel11.setBounds(10, 10, 150, 20);

        jLabelAccUnprovedSubTot.setText("0.00");
        jPanelAccSubTot.add(jLabelAccUnprovedSubTot);
        jLabelAccUnprovedSubTot.setBounds(90, 30, 70, 25);

        jLabelAccProvedSub.setText("Proved");
        jPanelAccSubTot.add(jLabelAccProvedSub);
        jLabelAccProvedSub.setBounds(10, 60, 70, 25);

        jLabelAccProvedSubTot.setText("0.00");
        jPanelAccSubTot.add(jLabelAccProvedSubTot);
        jLabelAccProvedSubTot.setBounds(90, 60, 70, 25);

        jLabelAccUnprovedSub.setText("Unproved");
        jPanelAccSubTot.add(jLabelAccUnprovedSub);
        jLabelAccUnprovedSub.setBounds(8, 30, 70, 25);

        jPanelGen.add(jPanelAccSubTot);
        jPanelAccSubTot.setBounds(670, 470, 190, 150);

        jLabelAppTotReqCost.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelAppTotReqCost.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAppTotReqCost.setText("0");
        jPanelGen.add(jLabelAppTotReqCost);
        jLabelAppTotReqCost.setBounds(1140, 590, 100, 30);

        jLabelAppTotReq.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelAppTotReq.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAppTotReq.setText("Total Requseted Amount");
        jPanelGen.add(jLabelAppTotReq);
        jLabelAppTotReq.setBounds(940, 590, 180, 30);

        jLabelOffice.setText("Office");
        jPanelGen.add(jLabelOffice);
        jLabelOffice.setBounds(570, 220, 70, 30);
        jPanelGen.add(jLabelAccNum);
        jLabelAccNum.setBounds(700, 260, 130, 30);

        jLabelACC.setText("Account No.");
        jPanelGen.add(jLabelACC);
        jLabelACC.setBounds(570, 260, 80, 30);

        jLabelNum.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNum.setText("Reference No.");
        jPanelGen.add(jLabelNum);
        jLabelNum.setBounds(1000, 80, 100, 30);

        jLabelRegNum.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelRegNum.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelRegNum);
        jLabelRegNum.setBounds(1160, 80, 60, 30);

        jLabelRegYear.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelRegYear.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelRegYear);
        jLabelRegYear.setBounds(1110, 80, 30, 30);

        jLabelSerial.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelSerial);
        jLabelSerial.setBounds(1140, 80, 10, 30);
        jPanelGen.add(jLabelEmp);
        jLabelEmp.setBounds(1110, 60, 50, 20);
        jPanelGen.add(jLabelEmpNum);
        jLabelEmpNum.setBounds(150, 140, 70, 30);
        jPanelGen.add(jLabelEmpProvince);
        jLabelEmpProvince.setBounds(120, 220, 370, 30);
        jPanelGen.add(jLabelEmpOffice);
        jLabelEmpOffice.setBounds(670, 220, 360, 30);

        jTabbedPane1.addTab("User and Accounting Details", jPanelGen);

        jPanelWkOne.setBackground(new java.awt.Color(255, 255, 204));
        jPanelWkOne.setLayout(null);

        jLabelLineLogNam.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkOne.add(jLabelLineLogNam);
        jLabelLineLogNam.setBounds(1180, 30, 160, 30);

        jLabelGenLogNam1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam1.setText("Logged In");
        jPanelWkOne.add(jLabelGenLogNam1);
        jLabelGenLogNam1.setBounds(1090, 30, 100, 30);

        jLabelLineTime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkOne.add(jLabelLineTime);
        jLabelLineTime.setBounds(1250, 0, 80, 30);

        jLabelLineDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkOne.add(jLabelLineDate);
        jLabelLineDate.setBounds(1090, 0, 110, 30);

        jLabelWkDuration.setText("Week Duration");
        jPanelWkOne.add(jLabelWkDuration);
        jLabelWkDuration.setBounds(30, 160, 90, 30);

        jLabelWk1From.setText("From");
        jPanelWkOne.add(jLabelWk1From);
        jLabelWk1From.setBounds(150, 160, 41, 30);

        jLabelWk1To.setText("To");
        jPanelWkOne.add(jLabelWk1To);
        jLabelWk1To.setBounds(390, 160, 41, 30);
        jPanelWkOne.add(jSeparator1);
        jSeparator1.setBounds(30, 200, 1280, 10);
        jPanelWkOne.add(jSeparator2);
        jSeparator2.setBounds(30, 150, 1280, 10);

        jTableWk1Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Site", "Distance", "Activity", "Budget Line", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc ", "Unproved Acc", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
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
        jScrollPane1.setBounds(30, 220, 1290, 400);

        jLabelLogo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ophidlog.jpg"))); // NOI18N
        jPanelWkOne.add(jLabelLogo1);
        jLabelLogo1.setBounds(10, 10, 220, 100);
        jPanelWkOne.add(jLabelWk1DateFrom);
        jLabelWk1DateFrom.setBounds(200, 160, 100, 30);
        jPanelWkOne.add(jLabelWk1DateTo);
        jLabelWk1DateTo.setBounds(440, 160, 100, 30);

        jLabelHeaderGen6.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen6.setText("MONTHLY  PLAN ");
        jPanelWkOne.add(jLabelHeaderGen6);
        jLabelHeaderGen6.setBounds(450, 40, 420, 40);

        jTabbedPane1.addTab("Week One", jPanelWkOne);

        jPanelWkTwo.setBackground(new java.awt.Color(95, 222, 184));
        jPanelWkTwo.setLayout(null);

        jLabelLineLogNam1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam1.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkTwo.add(jLabelLineLogNam1);
        jLabelLineLogNam1.setBounds(1180, 30, 160, 30);

        jLabelGenLogNam3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam3.setText("Logged In");
        jPanelWkTwo.add(jLabelGenLogNam3);
        jLabelGenLogNam3.setBounds(1090, 30, 100, 30);

        jLabelLineTime1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkTwo.add(jLabelLineTime1);
        jLabelLineTime1.setBounds(1250, 0, 80, 30);

        jLabelLineDate1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkTwo.add(jLabelLineDate1);
        jLabelLineDate1.setBounds(1090, 0, 110, 30);

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
                "Date", "Site", "Distance", "Activity", "Budget Line", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc", "Unproved Acc", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
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
        jPanelWkTwo.add(jLabelWk2DateFrom);
        jLabelWk2DateFrom.setBounds(200, 160, 100, 30);
        jPanelWkTwo.add(jLabelWk2DateTo);
        jLabelWk2DateTo.setBounds(440, 160, 100, 30);

        jLabelHeaderGen7.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen7.setText("MONTHLY  PLAN ");
        jPanelWkTwo.add(jLabelHeaderGen7);
        jLabelHeaderGen7.setBounds(450, 40, 420, 40);

        jTabbedPane1.addTab("Week Two", jPanelWkTwo);

        jPanelWkThree.setBackground(new java.awt.Color(29, 109, 222));
        jPanelWkThree.setLayout(null);

        jLabelLineLogNam2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkThree.add(jLabelLineLogNam2);
        jLabelLineLogNam2.setBounds(1180, 30, 160, 30);

        jLabelGenLogNam5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam5.setForeground(new java.awt.Color(255, 255, 255));
        jLabelGenLogNam5.setText("Logged In");
        jPanelWkThree.add(jLabelGenLogNam5);
        jLabelGenLogNam5.setBounds(1090, 30, 100, 30);

        jLabelLineTime2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkThree.add(jLabelLineTime2);
        jLabelLineTime2.setBounds(1250, 0, 80, 30);

        jLabelLineDate2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkThree.add(jLabelLineDate2);
        jLabelLineDate2.setBounds(1090, 0, 110, 30);

        jLabelWkDuration2.setText("Week Duration");
        jPanelWkThree.add(jLabelWkDuration2);
        jLabelWkDuration2.setBounds(30, 160, 90, 30);

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
                "Date", "Site", "Distance", "Activity", "Budget Line", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc", "Unproved Acc", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
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
        jPanelWkThree.add(jLabelWk3DateFrom);
        jLabelWk3DateFrom.setBounds(200, 160, 100, 30);
        jPanelWkThree.add(jLabelWk3DateTo);
        jLabelWk3DateTo.setBounds(440, 160, 100, 30);

        jLabelHeaderGen4.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen4.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeaderGen4.setText("MONTHLY  PLAN ");
        jPanelWkThree.add(jLabelHeaderGen4);
        jLabelHeaderGen4.setBounds(450, 40, 420, 40);

        jTabbedPane1.addTab("Week Three", jPanelWkThree);

        jPanelDocAttach.setBackground(new java.awt.Color(204, 204, 204));
        jPanelDocAttach.setLayout(null);

        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel8.setLayout(null);

        jButtonDelete1.setText("Delete  Requistion Attachment");
        jButtonDelete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDelete1ActionPerformed(evt);
            }
        });
        jPanel8.add(jButtonDelete1);
        jButtonDelete1.setBounds(10, 210, 205, 25);

        jButtonChoose1.setText("Choose Requistion");
        jButtonChoose1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonChoose1ActionPerformed(evt);
            }
        });
        jPanel8.add(jButtonChoose1);
        jButtonChoose1.setBounds(11, 126, 205, 25);

        jPanelDocAttach.add(jPanel8);
        jPanel8.setBounds(5, 115, 240, 530);

        jLabelLogo4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ophidlog.jpg"))); // NOI18N
        jPanelDocAttach.add(jLabelLogo4);
        jLabelLogo4.setBounds(10, 10, 220, 100);

        jLabelHeaderLine2.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine2.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelDocAttach.add(jLabelHeaderLine2);
        jLabelHeaderLine2.setBounds(350, 40, 610, 40);

        jLabelLineDate3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelDocAttach.add(jLabelLineDate3);
        jLabelLineDate3.setBounds(1130, 0, 110, 30);

        jLabelLineTime3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelDocAttach.add(jLabelLineTime3);
        jLabelLineTime3.setBounds(1250, 0, 80, 30);

        jLabellogged2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged2.setText("Logged In");
        jPanelDocAttach.add(jLabellogged2);
        jLabellogged2.setBounds(1070, 40, 80, 30);

        jLabelLineLogNam3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelDocAttach.add(jLabelLineLogNam3);
        jLabelLineLogNam3.setBounds(1170, 40, 170, 30);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addComponent(jLabelImg, javax.swing.GroupLayout.DEFAULT_SIZE, 927, Short.MAX_VALUE)
                .addContainerGap(315, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabelImg, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
                .addGap(0, 95, Short.MAX_VALUE))
        );

        jScrollPane7.setViewportView(jPanel9);

        jPanelDocAttach.add(jScrollPane7);
        jScrollPane7.setBounds(250, 115, 1100, 530);

        jTabbedPane1.addTab("Perdiem Request Attachments", jPanelDocAttach);

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
        jMenuFile.add(jSeparator13);

        jMenuItemSubmit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSubmit.setText("Submit");
        jMenuItemSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSubmitActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemSubmit);
        jMenuFile.add(jSeparator35);

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
        jMenuFile.add(jSeparator15);

        jMenuItemClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemClose.setText("Close");
        jMenuItemClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemClose);
        jMenuFile.add(jSeparator16);

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
        jMenuRequest.add(jSeparator17);

        jMenuItemAccMgrRev.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemAccMgrRev.setText("Account Manager Verification");
        jMenuItemAccMgrRev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAccMgrRevActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemAccMgrRev);
        jMenuRequest.add(jSeparator18);

        jMenuItemHeadApp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemHeadApp.setText("Head Approval");
        jMenuItemHeadApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemHeadAppActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemHeadApp);
        jMenuRequest.add(jSeparator20);

        jMenuPlanApproval.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuPlanApproval.setText("Central Finance Payment ");
        jMenuPlanApproval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuPlanApprovalActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuPlanApproval);
        jMenuRequest.add(jSeparator21);

        jMenuItemSchGen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSchGen.setText("Schedule Generation");
        jMenuItemSchGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSchGenActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemSchGen);
        jMenuRequest.add(jSeparator22);

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
        jMenuAcquittal.add(jSeparator23);

        jMenuItemAcqAccApp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        jMenuItemAcqAccApp.setText("Account Manager Verification");
        jMenuItemAcqAccApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqAccAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqAccApp);
        jMenuAcquittal.add(jSeparator24);

        jMenuItemAcqHeadApp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        jMenuItemAcqHeadApp.setText("Head Approval");
        jMenuItemAcqHeadApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqHeadAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqHeadApp);
        jMenuAcquittal.add(jSeparator25);

        jMenuItemAcqFinApp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        jMenuItemAcqFinApp.setText("Central Finance Payment ");
        jMenuItemAcqFinApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqFinAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqFinApp);
        jMenuAcquittal.add(jSeparator26);

        jMenuItemAcqSchGen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        jMenuItemAcqSchGen.setText("Acquittals Schedule Generation");
        jMenuItemAcqSchGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqSchGenActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqSchGen);
        jMenuAcquittal.add(jSeparator27);

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
        jMenuMonthlyPlan.add(jSeparator31);

        jMenuItemReqGenLst.setText("Request Generation list");
        jMenuItemReqGenLst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemReqGenLstActionPerformed(evt);
            }
        });
        jMenuMonthlyPlan.add(jMenuItemReqGenLst);
        jMenuMonthlyPlan.add(jSeparator38);

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
        jMenuReports.add(jSeparator32);

        jMenuAgeAlluser.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuAgeAlluser.setText("Age Analysis All Users");
        jMenuAgeAlluser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuAgeAlluserActionPerformed(evt);
            }
        });
        jMenuReports.add(jMenuAgeAlluser);
        jMenuReports.add(jSeparator33);

        jMenuAgeByUser.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuAgeByUser.setText("Age Analysis by User");
        jMenuAgeByUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuAgeByUserActionPerformed(evt);
            }
        });
        jMenuReports.add(jMenuAgeByUser);
        jMenuReports.add(jSeparator34);

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
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 692, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1376, 760));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonOkFacilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkFacilityActionPerformed

    }//GEN-LAST:event_jButtonOkFacilityActionPerformed

    private void jButtonCancelFacilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelFacilityActionPerformed

    }//GEN-LAST:event_jButtonCancelFacilityActionPerformed

    private void jComboProvinceFacilityMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboProvinceFacilityMouseEntered
        //        jComboDistrict.removeAllItems();
        //        jComboFacility.removeAllItems();
    }//GEN-LAST:event_jComboProvinceFacilityMouseEntered

    private void jComboProvinceFacilityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboProvinceFacilityKeyPressed

    }//GEN-LAST:event_jComboProvinceFacilityKeyPressed

    private void jComboDistrictFacilityMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboDistrictFacilityMouseEntered
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
    }//GEN-LAST:event_jComboDistrictFacilityMouseEntered

    private void jComboDistrictFacilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboDistrictFacilityActionPerformed
       
    }//GEN-LAST:event_jComboDistrictFacilityActionPerformed

    private void jComboFacilityFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboFacilityFocusGained
       
    }//GEN-LAST:event_jComboFacilityFocusGained

    private void jComboFacilityMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboFacilityMouseEntered
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
    }//GEN-LAST:event_jComboFacilityMouseEntered

    private void jComboFacilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboFacilityActionPerformed

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
            String offnam = jLabelEmpOffice.getText();
            String provnam = jLabelEmpProvince.getText();
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

    private void jTextActMainPurposeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextActMainPurposeFocusGained
       
    }//GEN-LAST:event_jTextActMainPurposeFocusGained

    private void jTextActMainPurposeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextActMainPurposeActionPerformed

    }//GEN-LAST:event_jTextActMainPurposeActionPerformed

    private void jPanelAllowanceSubTotFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanelAllowanceSubTotFocusGained

    }//GEN-LAST:event_jPanelAllowanceSubTotFocusGained

    private void jPanelGenComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_jPanelGenComponentAdded

    }//GEN-LAST:event_jPanelGenComponentAdded

    private void jPanelGenFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanelGenFocusGained

    }//GEN-LAST:event_jPanelGenFocusGained

    private void jPanelGenFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanelGenFocusLost

    }//GEN-LAST:event_jPanelGenFocusLost

    private void jPanelGenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelGenMouseClicked
       
    }//GEN-LAST:event_jPanelGenMouseClicked

    private void jPanelGenMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelGenMouseEntered
        // initUserDet();
    }//GEN-LAST:event_jPanelGenMouseEntered

    private void jPanelGenMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelGenMousePressed
       
    }//GEN-LAST:event_jPanelGenMousePressed

    private void jPanelGenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPanelGenKeyPressed

    }//GEN-LAST:event_jPanelGenKeyPressed

    private void jButtonDelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDelete1ActionPerformed
        jLabelImg.setIcon(null);
    }//GEN-LAST:event_jButtonDelete1ActionPerformed

    private void jButtonChoose1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonChoose1ActionPerformed
        imgChooser();
    }//GEN-LAST:event_jButtonChoose1ActionPerformed

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

    private void jCheckBoxSalAckAgreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxSalAckAgreeActionPerformed
       
    }//GEN-LAST:event_jCheckBoxSalAckAgreeActionPerformed

    private void jButtonAckSalContActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAckSalContActionPerformed
        if (!(jCheckBoxSalAckAgree.isSelected())) {
            JOptionPane.showMessageDialog(this, "Please click the check box to agree with the condition.",
                    "Check box blank", JOptionPane.WARNING_MESSAGE);
        } else {
            jDialogAckSalDeduct.setVisible(false);
            submitSaveRequest();
        }
    }//GEN-LAST:event_jButtonAckSalContActionPerformed

    private void jButtonSalAckCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalAckCancelActionPerformed
        jDialogAckSalDeduct.setVisible(false);
        JOptionPane.showMessageDialog(this, "<html>Please note that by <b>not agreeing to the terms and conditions</b> your perdiem request is not regestered.</html>");
        jTextActMainPurpose.requestFocusInWindow();
        jTextActMainPurpose.setFocusable(true);

    }//GEN-LAST:event_jButtonSalAckCancelActionPerformed

    private void jButtonOkFacility2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkFacility2ActionPerformed
//        jLabelDisProvince.setText(jComboProvinceFacility.getSelectedItem().toString());
//        jLabelDisDistrict.setText(jComboDistrictFacility.getSelectedItem().toString());
//        jLabelDisFacilty.setText(jComboFacility.getSelectedItem().toString());
//        jTextDestination.setText(jLabelDisFacilty.getText());
//        facDist();
//        // jTextDistDest.setText(jLabelFacDist.getText());
//        jDialogFacility.setVisible(false);
    }//GEN-LAST:event_jButtonOkFacility2ActionPerformed

    private void jButtonCancelFacility1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelFacility1ActionPerformed
        jDialogFacility.setVisible(false);
    }//GEN-LAST:event_jButtonCancelFacility1ActionPerformed

    private void jComboProvinceFacility1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboProvinceFacility1MouseEntered
        //        jComboDistrict.removeAllItems();
        //        jComboFacility.removeAllItems();
    }//GEN-LAST:event_jComboProvinceFacility1MouseEntered

    private void jComboProvinceFacility1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboProvinceFacility1KeyPressed

    }//GEN-LAST:event_jComboProvinceFacility1KeyPressed

    private void jComboDistrictFacility1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboDistrictFacility1MouseEntered
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
    }//GEN-LAST:event_jComboDistrictFacility1MouseEntered

    private void jComboDistrictFacility1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboDistrictFacility1ActionPerformed
       
    }//GEN-LAST:event_jComboDistrictFacility1ActionPerformed

    private void jComboFacility1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboFacility1FocusGained
       
    }//GEN-LAST:event_jComboFacility1FocusGained

    private void jComboFacility1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboFacility1MouseEntered
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
    }//GEN-LAST:event_jComboFacility1MouseEntered

    private void jComboFacility1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboFacility1ActionPerformed

    }//GEN-LAST:event_jComboFacility1ActionPerformed

    private void jCheckBoxSalSubmitAgreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxSalSubmitAgreeActionPerformed
       
    }//GEN-LAST:event_jCheckBoxSalSubmitAgreeActionPerformed

    private void jButtonAckSubmitContActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAckSubmitContActionPerformed
        if (!(jCheckBoxSalSubmitAgree.isSelected())) {
            JOptionPane.showMessageDialog(this, "Please click the check box to agree with the condition.",
                    "Check box blank", JOptionPane.WARNING_MESSAGE);
        } else {
            jDialogAckSubmit.setVisible(false);
            jDialogAckSalDeduct.setVisible(true);
        }
    }//GEN-LAST:event_jButtonAckSubmitContActionPerformed

    private void jButtonSalSubmitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalSubmitCancelActionPerformed
        jDialogAckSubmit.setVisible(false);
        JOptionPane.showMessageDialog(this, "<html>Please note that by <b>not agreeing "
                + "to the terms and conditions</b> your perdiem request is not regestered.</html>");
        jTextActMainPurpose.requestFocusInWindow();
        jTextActMainPurpose.setFocusable(true);
    }//GEN-LAST:event_jButtonSalSubmitCancelActionPerformed

    private void jMenuItemSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSubmitActionPerformed
        if (jLabelImg.getIcon() == null) {
            JOptionPane.showMessageDialog(this, "Attach document missing. Please check and correct.");
            jLabelImg.requestFocusInWindow();
        } else if (("".equals(jLabelBankName.getText())) || ("".equals(jLabelAccNum.getText())) || ("".equals(jLabelEmpNam.getText()))) {
            JOptionPane.showMessageDialog(this, "Your Employee or Banking details are not complete. Please contact IT to update your details.");
            jTextActMainPurpose.requestFocusInWindow();
            jTextActMainPurpose.setFocusable(true);
        } else {
            jCheckBoxSalAckAgree.setSelected(false);
            jCheckBoxSalSubmitAgree.setSelected(false);
            jLabelDisProvince.setVisible(false);
            jLabelDisDistrict.setVisible(false);
            jLabelDisFacilty.setVisible(false);

            jDialogAckSubmit.setVisible(true);

        }
    }//GEN-LAST:event_jMenuItemSubmitActionPerformed

    private void jTextFieldDialogWk1SiteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk1SiteMouseClicked

    }//GEN-LAST:event_jTextFieldDialogWk1SiteMouseClicked

    private void jTextFieldDialogWk1SiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk1SiteActionPerformed
       
    }//GEN-LAST:event_jTextFieldDialogWk1SiteActionPerformed

    private void jTextFieldDialogWk1SiteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk1SiteKeyTyped

    }//GEN-LAST:event_jTextFieldDialogWk1SiteKeyTyped

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

    private void jTextFieldDialogWk3SiteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk3SiteMouseClicked
       
    }//GEN-LAST:event_jTextFieldDialogWk3SiteMouseClicked

    private void jTextFieldDialogWk3SiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk3SiteActionPerformed
       
    }//GEN-LAST:event_jTextFieldDialogWk3SiteActionPerformed

    private void jTextFieldDialogWk3SiteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk3SiteKeyTyped
       
    }//GEN-LAST:event_jTextFieldDialogWk3SiteKeyTyped

    private void jTextFieldWk3DialogBudgetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogBudgetMouseClicked
       
    }//GEN-LAST:event_jTextFieldWk3DialogBudgetMouseClicked

    private void jTextFieldWk3DialogBudgetKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogBudgetKeyTyped
       
    }//GEN-LAST:event_jTextFieldWk3DialogBudgetKeyTyped

    private void jCheckBoxDialogWk3MiscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxDialogWk3MiscActionPerformed
       
    }//GEN-LAST:event_jCheckBoxDialogWk3MiscActionPerformed

    private void jTextFieldDialogWk2SiteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk2SiteMouseClicked
       
    }//GEN-LAST:event_jTextFieldDialogWk2SiteMouseClicked

    private void jTextFieldDialogWk2SiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk2SiteActionPerformed
       
    }//GEN-LAST:event_jTextFieldDialogWk2SiteActionPerformed

    private void jTextFieldDialogWk2SiteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk2SiteKeyTyped
       
    }//GEN-LAST:event_jTextFieldDialogWk2SiteKeyTyped

    private void jTextFieldWk2DialogBudgetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogBudgetMouseClicked
       
    }//GEN-LAST:event_jTextFieldWk2DialogBudgetMouseClicked

    private void jTextFieldWk2DialogBudgetKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogBudgetKeyTyped
       
    }//GEN-LAST:event_jTextFieldWk2DialogBudgetKeyTyped

    private void jCheckBoxDialogWk2MiscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxDialogWk2MiscActionPerformed
       
    }//GEN-LAST:event_jCheckBoxDialogWk2MiscActionPerformed

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

            jLabelWk1LnActivity.setText("<html>Activity Details For <b>" + (id.toString()) + "</html>");
//            jLabelActDate.setText(id.toString());
            jTextFieldDialogWk1Site.setText(id1.toString());
            jLabelWk1DialogActDis.setText(id2.toString());
            jTextFieldWk1DialogActivityDesc.setText(id3.toString());
            jTextFieldWk1DialogBudget.setText(id4.toString());
            if ((Double.parseDouble(id5.toString())) > 0) {
                jCheckBoxDialogWk1BrkFast.setSelected(true);
            }
            if ((Double.parseDouble(id6.toString())) > 0) {
                jCheckBoxDialogWk1Lunch.setSelected(true);
            }
            if ((Double.parseDouble(id7.toString())) > 0) {
                jCheckBoxDialogWk1Dinner.setSelected(true);
            }
            if ((Double.parseDouble(id8.toString())) > 0) {
                jCheckBoxDialogWk1Inc.setSelected(true);
            }

            if ((Double.parseDouble(id10.toString())) > 0) {
                jCheckBoxDialogWk1Misc.setSelected(true);
            }
            jTextFieldWk1Misc.setText(id9.toString());
            jTextFieldWk1MiscAmt.setText(id10.toString());
            if ((Double.parseDouble(id11.toString())) > 0) {
                jCheckBoxDialogWk1Acc.setSelected(true);

            }

            jDialogWk1.setVisible(true);
            jTextFieldDialogWk1Site.setEditable(false);
            jTextFieldWk1DialogBudget.setEditable(false);
            jTextFieldWk1DialogActivityDesc.setEditable(false);
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

            jLabelWk2LnActivity.setText("<html>Activity Details For <b>" + (id.toString()) + "</html>");
//            jLabelActDate.setText(id.toString());
            jTextFieldDialogWk2Site.setText(id1.toString());
            jLabelWk2DialogActDis.setText(id2.toString());
            jTextFieldWk2DialogActivityDesc.setText(id3.toString());
            jTextFieldWk2DialogBudget.setText(id4.toString());
            if ((Double.parseDouble(id5.toString())) > 0) {
                jCheckBoxDialogWk2BrkFast.setSelected(true);
            }
            if ((Double.parseDouble(id6.toString())) > 0) {
                jCheckBoxDialogWk2Lunch.setSelected(true);
            }
            if ((Double.parseDouble(id7.toString())) > 0) {
                jCheckBoxDialogWk2Dinner.setSelected(true);
            }
            if ((Double.parseDouble(id8.toString())) > 0) {
                jCheckBoxDialogWk2Inc.setSelected(true);
            }

            if ((Double.parseDouble(id10.toString())) > 0) {
                jCheckBoxDialogWk2Misc.setSelected(true);
            }
            jTextFieldWk2Misc.setText(id9.toString());
            jTextFieldWk2MiscAmt.setText(id10.toString());
            if ((Double.parseDouble(id11.toString())) > 0) {
                jCheckBoxDialogWk2Acc.setSelected(true);

            }

            jDialogWk2.setVisible(true);
            jTextFieldDialogWk2Site.setEditable(false);
            jTextFieldWk2DialogBudget.setEditable(false);
            jTextFieldWk2DialogActivityDesc.setEditable(false);
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

            jLabelWk3LnActivity.setText("<html>Activity Details For <b>" + (id.toString()) + "</html>");
//            jLabelActDate.setText(id.toString());
            jTextFieldDialogWk3Site.setText(id1.toString());
            jLabelWk3DialogActDis.setText(id2.toString());
            jTextFieldWk3DialogActivityDesc.setText(id3.toString());
            jTextFieldWk3DialogBudget.setText(id4.toString());
            if ((Double.parseDouble(id5.toString())) > 0) {
                jCheckBoxDialogWk3BrkFast.setSelected(true);
            }
            if ((Double.parseDouble(id6.toString())) > 0) {
                jCheckBoxDialogWk3Lunch.setSelected(true);
            }
            if ((Double.parseDouble(id7.toString())) > 0) {
                jCheckBoxDialogWk3Dinner.setSelected(true);
            }
            if ((Double.parseDouble(id8.toString())) > 0) {
                jCheckBoxDialogWk3Inc.setSelected(true);
            }

            if ((Double.parseDouble(id10.toString())) > 0) {
                jCheckBoxDialogWk3Misc.setSelected(true);
            }
            jTextFieldWk3Misc.setText(id9.toString());
            jTextFieldWk3MiscAmt.setText(id10.toString());
            if ((Double.parseDouble(id11.toString())) > 0) {
                jCheckBoxDialogWk3Acc.setSelected(true);

            }

            jDialogWk3.setVisible(true);
            jTextFieldDialogWk3Site.setEditable(false);
            jTextFieldWk3DialogBudget.setEditable(false);
            jTextFieldWk3DialogActivityDesc.setEditable(false);
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
            java.util.logging.Logger.getLogger(JFrameMnthRequest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthRequest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthRequest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthRequest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new JFrameMnthRequest().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAckSalCont;
    private javax.swing.JButton jButtonAckSubmitCont;
    private javax.swing.JButton jButtonCancelBudget;
    private javax.swing.JButton jButtonCancelFacility;
    private javax.swing.JButton jButtonCancelFacility1;
    private javax.swing.JButton jButtonChoose1;
    private javax.swing.JButton jButtonDelete1;
    private javax.swing.JButton jButtonOkFacility;
    private javax.swing.JButton jButtonOkFacility1;
    private javax.swing.JButton jButtonOkFacility2;
    private javax.swing.JButton jButtonSalAckCancel;
    private javax.swing.JButton jButtonSalSubmitCancel;
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
    private javax.swing.JCheckBox jCheckBoxSalAckAgree;
    private javax.swing.JCheckBox jCheckBoxSalSubmitAgree;
    private javax.swing.JComboBox<String> jComboBudgetCode;
    private javax.swing.JComboBox<String> jComboDistrictFacility;
    private javax.swing.JComboBox<String> jComboDistrictFacility1;
    private javax.swing.JComboBox<String> jComboFacility;
    private javax.swing.JComboBox<String> jComboFacility1;
    private javax.swing.JComboBox<String> jComboProvinceFacility;
    private javax.swing.JComboBox<String> jComboProvinceFacility1;
    private javax.swing.JDialog jDialogAckSalDeduct;
    private javax.swing.JDialog jDialogAckSubmit;
    private javax.swing.JDialog jDialogBudget;
    private javax.swing.JDialog jDialogFacility;
    private javax.swing.JDialog jDialogFacility1;
    private javax.swing.JDialog jDialogWaiting;
    private javax.swing.JDialog jDialogWaitingEmail;
    private javax.swing.JDialog jDialogWk1;
    private javax.swing.JDialog jDialogWk2;
    private javax.swing.JDialog jDialogWk3;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabelACC;
    private javax.swing.JLabel jLabelAccNum;
    private javax.swing.JLabel jLabelAccProvedSub;
    private javax.swing.JLabel jLabelAccProvedSubTot;
    private javax.swing.JLabel jLabelAccUnprovedSub;
    private javax.swing.JLabel jLabelAccUnprovedSubTot;
    private javax.swing.JLabel jLabelActMainPurpose;
    private javax.swing.JLabel jLabelAppTotReq;
    private javax.swing.JLabel jLabelAppTotReqCost;
    private javax.swing.JLabel jLabelBank;
    private javax.swing.JLabel jLabelBankName;
    private javax.swing.JLabel jLabelBreakFastSub;
    private javax.swing.JLabel jLabelBreakFastSubTot;
    private javax.swing.JLabel jLabelBudgetCode;
    private javax.swing.JLabel jLabelDialogWk1Dis;
    private javax.swing.JLabel jLabelDialogWk1Dis1;
    private javax.swing.JLabel jLabelDialogWk1Site;
    private javax.swing.JLabel jLabelDialogWk2Dis;
    private javax.swing.JLabel jLabelDialogWk2Dis2;
    private javax.swing.JLabel jLabelDialogWk2Dis3;
    private javax.swing.JLabel jLabelDialogWk2Site;
    private javax.swing.JLabel jLabelDialogWk3Dis;
    private javax.swing.JLabel jLabelDialogWk3Site;
    private javax.swing.JLabel jLabelDinnerSub;
    private javax.swing.JLabel jLabelDinnerSubTot;
    private javax.swing.JLabel jLabelDisDistrict;
    private javax.swing.JLabel jLabelDisFacilty;
    private javax.swing.JLabel jLabelDisProvince;
    private javax.swing.JLabel jLabelDistrictFacility;
    private javax.swing.JLabel jLabelDistrictFacility1;
    private javax.swing.JLabel jLabelEmp;
    private javax.swing.JLabel jLabelEmpNam;
    private javax.swing.JLabel jLabelEmpNum;
    private javax.swing.JLabel jLabelEmpNumber;
    private javax.swing.JLabel jLabelEmpOffice;
    private javax.swing.JLabel jLabelEmpProvince;
    private javax.swing.JLabel jLabelEmpTitle;
    private javax.swing.JLabel jLabelFacDist;
    private javax.swing.JLabel jLabelFacility;
    private javax.swing.JLabel jLabelFacility1;
    private javax.swing.JLabel jLabelFinDetails;
    private javax.swing.JLabel jLabelGenDate;
    private javax.swing.JLabel jLabelGenLogNam;
    private javax.swing.JLabel jLabelGenLogNam1;
    private javax.swing.JLabel jLabelGenLogNam3;
    private javax.swing.JLabel jLabelGenLogNam5;
    private javax.swing.JLabel jLabelGenLogNam6;
    private javax.swing.JLabel jLabelGenTime;
    private javax.swing.JLabel jLabelHeader1;
    private javax.swing.JLabel jLabelHeader2;
    private javax.swing.JLabel jLabelHeader3;
    private javax.swing.JLabel jLabelHeaderAck;
    private javax.swing.JLabel jLabelHeaderAck1;
    private javax.swing.JLabel jLabelHeaderGen;
    private javax.swing.JLabel jLabelHeaderGen4;
    private javax.swing.JLabel jLabelHeaderGen6;
    private javax.swing.JLabel jLabelHeaderGen7;
    private javax.swing.JLabel jLabelHeaderLine2;
    private javax.swing.JLabel jLabelImg;
    private javax.swing.JLabel jLabelIncidentalSub;
    private javax.swing.JLabel jLabelIncidentalSubTot;
    private javax.swing.JLabel jLabelLineDate;
    private javax.swing.JLabel jLabelLineDate1;
    private javax.swing.JLabel jLabelLineDate2;
    private javax.swing.JLabel jLabelLineDate3;
    private javax.swing.JLabel jLabelLineLogNam;
    private javax.swing.JLabel jLabelLineLogNam1;
    private javax.swing.JLabel jLabelLineLogNam2;
    private javax.swing.JLabel jLabelLineLogNam3;
    private javax.swing.JLabel jLabelLineTime;
    private javax.swing.JLabel jLabelLineTime1;
    private javax.swing.JLabel jLabelLineTime2;
    private javax.swing.JLabel jLabelLineTime3;
    private javax.swing.JLabel jLabelLogo;
    private javax.swing.JLabel jLabelLogo1;
    private javax.swing.JLabel jLabelLogo2;
    private javax.swing.JLabel jLabelLogo3;
    private javax.swing.JLabel jLabelLogo4;
    private javax.swing.JLabel jLabelLunchSub;
    private javax.swing.JLabel jLabelLunchSubTot;
    private javax.swing.JLabel jLabelMiscSubTot;
    private javax.swing.JLabel jLabelMscSub;
    private javax.swing.JLabel jLabelNum;
    private javax.swing.JLabel jLabelOffice;
    private javax.swing.JLabel jLabelProvince2;
    private javax.swing.JLabel jLabelProvinceFacility;
    private javax.swing.JLabel jLabelProvinceFacility1;
    private javax.swing.JLabel jLabelRegNum;
    private javax.swing.JLabel jLabelRegYear;
    private javax.swing.JLabel jLabelRemain;
    private javax.swing.JLabel jLabelRemain1;
    private javax.swing.JLabel jLabelRemain3;
    private javax.swing.JLabel jLabelSepDetails;
    private javax.swing.JLabel jLabelSerial;
    private javax.swing.JLabel jLabelWk1DateFrom;
    private javax.swing.JLabel jLabelWk1DateTo;
    private javax.swing.JLabel jLabelWk1DialogActDis;
    private javax.swing.JLabel jLabelWk1DialogActivityDesc;
    private javax.swing.JLabel jLabelWk1DialogBudget;
    private javax.swing.JLabel jLabelWk1From;
    private javax.swing.JLabel jLabelWk1From1;
    private javax.swing.JLabel jLabelWk1LnActivity;
    private javax.swing.JLabel jLabelWk1Misc;
    private javax.swing.JLabel jLabelWk1MiscAmt;
    private javax.swing.JLabel jLabelWk1To;
    private javax.swing.JLabel jLabelWk1To1;
    private javax.swing.JLabel jLabelWk2DateFrom;
    private javax.swing.JLabel jLabelWk2DateTo;
    private javax.swing.JLabel jLabelWk2DialogActDis;
    private javax.swing.JLabel jLabelWk2DialogActivityDesc;
    private javax.swing.JLabel jLabelWk2DialogBudget;
    private javax.swing.JLabel jLabelWk2LnActivity;
    private javax.swing.JLabel jLabelWk2Misc;
    private javax.swing.JLabel jLabelWk2MiscAmt;
    private javax.swing.JLabel jLabelWk3DateFrom;
    private javax.swing.JLabel jLabelWk3DateTo;
    private javax.swing.JLabel jLabelWk3DialogActDis;
    private javax.swing.JLabel jLabelWk3DialogActivityDesc;
    private javax.swing.JLabel jLabelWk3DialogBudget;
    private javax.swing.JLabel jLabelWk3From;
    private javax.swing.JLabel jLabelWk3LnActivity;
    private javax.swing.JLabel jLabelWk3Misc;
    private javax.swing.JLabel jLabelWk3MiscAmt;
    private javax.swing.JLabel jLabelWk3To;
    private javax.swing.JLabel jLabelWkDuration;
    private javax.swing.JLabel jLabelWkDuration1;
    private javax.swing.JLabel jLabelWkDuration2;
    private javax.swing.JLabel jLabellogged2;
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
    private javax.swing.JMenuItem jMenuItemSubmit;
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
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelAccSubTot;
    private javax.swing.JPanel jPanelAllowanceSubTot;
    private javax.swing.JPanel jPanelDocAttach;
    private javax.swing.JPanel jPanelGen;
    private javax.swing.JPanel jPanelMiscSubTot;
    private javax.swing.JPanel jPanelWkOne;
    private javax.swing.JPanel jPanelWkThree;
    private javax.swing.JPanel jPanelWkTwo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
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
    private javax.swing.JPopupMenu.Separator jSeparator35;
    private javax.swing.JSeparator jSeparator36;
    private javax.swing.JPopupMenu.Separator jSeparator38;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableWk1Activities;
    private javax.swing.JTable jTableWk2Activities;
    private javax.swing.JTable jTableWk3Activities;
    private javax.swing.JTextField jTextActMainPurpose;
    private javax.swing.JTextField jTextFieldDialogWk1Site;
    private javax.swing.JTextField jTextFieldDialogWk2Site;
    private javax.swing.JTextField jTextFieldDialogWk3Site;
    private javax.swing.JTextField jTextFieldWk1DialogActivityDesc;
    private javax.swing.JTextField jTextFieldWk1DialogBudget;
    private javax.swing.JTextField jTextFieldWk1Misc;
    private javax.swing.JTextField jTextFieldWk1MiscAmt;
    private javax.swing.JTextField jTextFieldWk2DialogActivityDesc;
    private javax.swing.JTextField jTextFieldWk2DialogBudget;
    private javax.swing.JTextField jTextFieldWk2Misc;
    private javax.swing.JTextField jTextFieldWk2MiscAmt;
    private javax.swing.JTextField jTextFieldWk3DialogActivityDesc;
    private javax.swing.JTextField jTextFieldWk3DialogBudget;
    private javax.swing.JTextField jTextFieldWk3Misc;
    private javax.swing.JTextField jTextFieldWk3MiscAmt;
    // End of variables declaration//GEN-END:variables
}
