/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClaimPlanPerDiem;

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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.timeHost;
import ClaimPlan.*;
import ClaimPlan.JFrameMnthReqGenList;
import ClaimPlan.JFrameMnthSupList;
import ClaimPlan.JFrameMnthViewList;
import utils.StockVehicleMgt;
import utils.connCred;
import utils.connSaveFile;
import utils.emailSend;
import utils.savePDFToDB;

/**
 *
 * @author goredemac
 */
public class JFrameMnthDistrictAcquittal extends javax.swing.JFrame {

    connCred c = new connCred();
    timeHost tH = new timeHost();
    connSaveFile attL = new connSaveFile();
    savePDFToDB pdfDB = new savePDFToDB();
    emailSend emSend = new emailSend();
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
    int itmDateCount = 0;
    int itmNumAtt = 0;
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
    String breakfastAll, lunchAll, lunchNPAll, lunchPAll, dinnerAll, EmpBankNam, EmpAccNum,
            incidentalAll, unProvedAll, provedAll, date1, date2, usrnam, docVer, docNextVer, usrGrp,
            actStartDate, actEndDate, sendTo, createUsrNam, supUsrMail, oldRefNum, digSign,
            SearchRef, supNam, supEmpNum, empOff, attFileDesc, attFileName, branchCode, prjCode, taskCode, lowDate,
            accomodationProven, accomodationUnProven, accomodation;
    PreparedStatement pst1 = null;
    PreparedStatement pst = null;
    PreparedStatement pst3 = null;
    PreparedStatement pst4 = null;
    int itmNum = 1;
    int newPlanRefNum, maxPlanRefNum;
    DefaultTableModel model, modelAcq, modelAtt;

    /**
     * Creates new form JFrameMnthPlanPerDiemCreate
     */
    public JFrameMnthDistrictAcquittal() {
        initComponents();
        //showDate();
        showTime();
        computerName();
        findUser();
        model = (DefaultTableModel) jTableReqActivities.getModel();
        modelAcq = (DefaultTableModel) jTableActivityAcq.getModel();
        modelAtt = (DefaultTableModel) jTableAcquittalDocAtt.getModel();

        jLabelAcqEmpNum.setText("test");
        jLabelAcqEmpNam.setText("test");
        jLabelAcqProvince.setText("test");
        jLabelAcqOffice.setText("test");
        jLabelAcqAccNum.setText("test");
        jLabelAcqBankName.setText("test");
        jLabelAcqActMainPurpose.setText("test");

    }

    public JFrameMnthDistrictAcquittal(String ref, String usrLogNam) {
        initComponents();

        model = (DefaultTableModel) jTableReqActivities.getModel();
        modelAcq = (DefaultTableModel) jTableActivityAcq.getModel();
        modelAtt = (DefaultTableModel) jTableAcquittalDocAtt.getModel();

        SearchRef = ref;
        jLabelEmp.setText(usrLogNam);
        jLabelEmp.setVisible(false);
        jMenuItemRequest.setEnabled(false);
        jMenuItemSpecialAcquittal.setEnabled(false);
        jTextBankNam.setVisible(false);
        jLabelPhoneDet.setVisible(false);
        jMenuItemSubmit.setVisible(false);
        jSeparator35.setVisible(false);
        jTextAttDocFilePath.setVisible(false);
        try {
            ///  showDate();
            tH.intShowDate();
        } catch (IOException ex) {
            Logger.getLogger(JFrameMnthDistrictAcquittal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(JFrameMnthDistrictAcquittal.class.getName()).log(Level.SEVERE, null, ex);
        }
        jLabelGenDate.setText(tH.internetDate);
        jLabelLineDate.setText(tH.internetDate);

        showTime();
        computerName();
        findUser();
        findUserGrp();
        findProject();
        findProvince();
        allowanceRate();
        getLowestDate();
        fetchdataGen();
        fetchReqItmData();
        calTot();
        mainPageTotInsert();
        jTabbedPaneAcqAtt.setTitleAt(1, "E-Log Book");
        jToggleButtonNoActivity.setText("All Activities Not Done");
        jToggleButtonAllActivities.setText("All Activities Done As Per Request");

        if (!"Administrator".equals(usrGrp)) {
            jMenuItemUserProfUpd.setEnabled(false);
            jMenuItemUserCreate.setEnabled(false);
        }

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
            }
        }) {

        }.start();

    }

    void findUserGrp() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT EMP_GRP  FROM [ClaimsAppSysZvandiri].[dbo].[EmpLogGrpTab]"
                    + "where emp_num ='" + jLabelEmp.getText() + "'");

            while (r.next()) {

                usrGrp = r.getString(1);

            }

            if ("usrGenSp".equals(usrGrp)) {

                jMenuItemSupApp.setEnabled(false);
                jMenuItemAccMgrRev.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuItemAcqSupApp.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuItemAcqSchGen.setEnabled(false);
                jMenuItemSchPerDiem.setEnabled(false);
                jMenuItemPlanSupApproval.setEnabled(false);
                jMenuItemPlanHODApproval.setEnabled(false);
                jMenuItemPlanFinApproval.setEnabled(false);

            }

            if ("usrGen".equals(usrGrp)) {

                jMenuItemSupApp.setEnabled(false);
                jMenuItemAccMgrRev.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuItemAcqSupApp.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuItemAcqSchGen.setEnabled(false);
                jMenuItemPlanView.setEnabled(false);
                jMenuItemSchPerDiem.setEnabled(false);
                jMenuItemPlanSupApproval.setEnabled(false);
                jMenuItemPlanHODApproval.setEnabled(false);
                jMenuItemPlanFinApproval.setEnabled(false);
            }

            if ("usrGenPlan".equals(usrGrp)) {

                jMenuItemSupApp.setEnabled(false);
                jMenuItemAccMgrRev.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuItemAcqSupApp.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuItemAcqSchGen.setEnabled(false);
                jMenuItemPlanView.setEnabled(false);
                jMenuItemSchPerDiem.setEnabled(false);
                jMenuItemPlanSupApproval.setEnabled(false);
                jMenuItemPlanHODApproval.setEnabled(false);
                jMenuItemPlanFinApproval.setEnabled(false);
            }

            if ("usrSup".equals(usrGrp)) {

                jMenuItemAccMgrRev.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuItemAcqSchGen.setEnabled(false);
                jMenuItemSchPerDiem.setEnabled(false);
                jMenuItemPlanFinApproval.setEnabled(false);
                jMenuItemPlanHODApproval.setEnabled(false);

            }

            if ("usrProvMgr".equals(usrGrp)) {

                jMenuItemAccMgrRev.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuItemAcqSchGen.setEnabled(false);
                jMenuItemSchPerDiem.setEnabled(false);
                jMenuItemPlanFinApproval.setEnabled(false);
                jMenuItemPlanHODApproval.setEnabled(false);

            }

            if ("usrSupAcc".equals(usrGrp)) {

                jMenuItemHeadApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuItemPlanHODApproval.setEnabled(false);

            }

            if ("usrAccMgr".equals(usrGrp)) {

                jMenuItemSupApp.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuItemAcqSupApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuItemPlanHODApproval.setEnabled(false);

            }

            if ("usrHead".equals(usrGrp)) {

                jMenuItemAccMgrRev.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqSchGen.setEnabled(false);
                jMenuItemSchPerDiem.setEnabled(false);
                jMenuItemPlanFinApproval.setEnabled(false);

            }

            if ("usrFinReq".equals(usrGrp)) {
                
                
            
                jMenuItemSupApp.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuItemAcqSupApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuItemPlanView.setEnabled(false);
                jMenuItemPlanSupApproval.setEnabled(false);
                jMenuItemPlanHODApproval.setEnabled(false);

            }

            if ("usrFinSup".equals(usrGrp)) {
                
                
            
                jMenuItemHeadApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuItemPlanView.setEnabled(false);
                jMenuItemPlanSupApproval.setEnabled(false);
                jMenuItemPlanHODApproval.setEnabled(false);

            }

            if ("usrFinAcq".equals(usrGrp)) {

                jMenuItemSupApp.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuItemAcqSupApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuItemPlanView.setEnabled(false);
                jMenuItemPlanSupApproval.setEnabled(false);
                jMenuItemPlanHODApproval.setEnabled(false);

            }

            if ("usrAdm".equals(usrGrp)) {

                jMenuItemSupApp.setEnabled(false);
                jMenuItemAccMgrRev.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuItemAcqSupApp.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuItemAcqSchGen.setEnabled(false);
                jMenuItemSchPerDiem.setEnabled(false);
                jMenuItemPlanSupApproval.setEnabled(false);
                jMenuItemPlanHODApproval.setEnabled(false);
                jMenuItemPlanFinApproval.setEnabled(false);

            }
        } catch (Exception e) {
            //  JOptionPane.showMessageDialog(null, "Failed to Connect to Database (Province).Try Again", "Error Connection", JOptionPane.WARNING_MESSAGE);
            System.out.println(e);
        }
    }

    void findUser() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select EMP_NAM,SUP_EMP_NUM,SUP_NAM,EMP_SUP_MAIL, "
                    + "EMP_PROVINCE,EMP_OFFICE,EMP_BNK_NAM,ACC_NUM,EMP_TTL from [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] a "
                    + "join [ClaimsAppSysZvandiri].[dbo].[EmpDetWS] b on a.EMP_NUM=b.EMP_NUM join "
                    + "[ClaimsAppSysZvandiri].[dbo].[EmpBankDetTab] c on a.EMP_NUM=c.EMP_NUM "
                    + "where a.EMP_NUM='" + jLabelEmp.getText() + "'");

            while (r.next()) {
                jLabelGenLogNam.setText(r.getString(1));
                jLabelLineLogNam.setText(r.getString(1));
                jLabelAcqEmpNam.setText(r.getString(1));
                jLabelAcqEmpNum.setText(jLabelEmp.getText());

                jLabelAcqBankName.setText(r.getString(7));
                jLabelAcqAccNum.setText(r.getString(8));
                jLabelAcqEmpTitle.setText(r.getString(9));
                createUsrNam = r.getString(1);
                supEmpNum = r.getString(2);
                supNam = r.getString(3);
                supUsrMail = r.getString(4);
                EmpBankNam = r.getString(7);
                EmpAccNum = r.getString(8);
                empOff = r.getString(6);

            }

            //                 conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void getLowestDate() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT min(act_date) FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab]  "
                    + " where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'C'");

            while (r.next()) {
                lowDate = r.getString(1);
            }

            //                 conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void allowanceRate() {
        try {
            String rateCat;
            if ("National Office".equals(empOff)) {
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

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findProvince() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            jComboProvinceFacility.setSelectedIndex(-1);
            ResultSet r = st.executeQuery("select distinct(province) from [ClaimsAppSysZvandiri].[dbo].[DistFacTab]");

            while (r.next()) {

                jComboProvinceFacility.addItem(r.getString("province"));

            }

            //                 conn.close();
        } catch (Exception e) {

        }
    }

    void findProject() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");

            Statement st = conn.createStatement();

            jComboProjectName.setSelectedIndex(-1);

            ResultSet r = st.executeQuery("SELECT concat(PROJ_ID,' ',PROJ_NAME) "
                    + "FROM [HRLeaveSysZvandiri].[dbo].[ProjectTab] order by 1");

            while (r.next()) {

                jComboProjectName.addItem(r.getString(1));

            }

            conn.close();
        } catch (Exception e) {
            System.out.println();
        }
    }

    void findTask(String prjCode) {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");

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

    void updateWk1Plan() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            for (int i = 0; i < jTableReqActivities.getRowCount(); i++) {

                String sqlwk1plan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] "
                        + "(SERIAL,PLAN_REF_NUM,ACT_DATE,ACT_SITE,SITE_CLASSIFICATION,CONSO_RANK,"
                        + "TRAV_DIST,ACT_DESC,ACT_JUSTIFCATION,PER_DIEM_REQ,BUD_COD,"
                        + "BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,MSC_AMT,ACC_UNPROV_AMT,"
                        + "EMP_NAM1,EMP_NAM2,EMP_NAM3,EMP_NAM4,ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlwk1plan);
                pst1.setString(1, "P");
                pst1.setString(2, SearchRef);
                pst1.setString(3, jTableReqActivities.getValueAt(i, 0).toString());
                pst1.setString(4, jTableReqActivities.getValueAt(i, 1).toString());
                pst1.setString(5, jTableReqActivities.getValueAt(i, 2).toString());
                pst1.setString(6, jTableReqActivities.getValueAt(i, 3).toString());
                pst1.setString(7, jTableReqActivities.getValueAt(i, 4).toString());
                pst1.setString(8, jTableReqActivities.getValueAt(i, 5).toString());
                pst1.setString(9, jTableReqActivities.getValueAt(i, 6).toString());
                pst1.setString(10, jTableReqActivities.getValueAt(i, 7).toString());
                pst1.setString(11, jTableReqActivities.getValueAt(i, 8).toString());
                pst1.setString(12, String.valueOf(jTableReqActivities.getValueAt(i, 9).toString()));
                pst1.setString(13, String.valueOf(jTableReqActivities.getValueAt(i, 10).toString()));
                pst1.setString(14, String.valueOf(jTableReqActivities.getValueAt(i, 11).toString()));
                pst1.setString(15, String.valueOf(jTableReqActivities.getValueAt(i, 12).toString()));
                pst1.setString(16, jTableReqActivities.getValueAt(i, 13).toString());
                pst1.setString(17, String.valueOf(jTableReqActivities.getValueAt(i, 14).toString()));
                pst1.setString(18, String.valueOf(jTableReqActivities.getValueAt(i, 15).toString()));
                pst1.setString(19, jTableReqActivities.getValueAt(i, 16).toString());
                pst1.setString(20, jTableReqActivities.getValueAt(i, 17).toString());
                pst1.setString(21, jTableReqActivities.getValueAt(i, 18).toString());
                pst1.setString(22, jTableReqActivities.getValueAt(i, 19).toString());
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
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

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

            String sqlPlanWk4 = "update [ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' ";

            pst = conn.prepareStatement(sqlPlanWk4);
            pst.executeUpdate();

            String sqlPlanWk5 = "update [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' ";

            pst = conn.prepareStatement(sqlPlanWk5);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    void updatePlanPeriod() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlWkplanperiod = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] "
                    + "(SERIAL,PLAN_REF_NUM,PROVINCE,DISTRICT,Wk1_PLAN_START_DATE,Wk1_PLAN_END_DATE,"
                    + "ACT_VER,DOC_VER,ACT_REC_STA)"
                    + " VALUES (?,?,?,?,?,?,?,?,?)";

            pst1 = conn.prepareStatement(sqlWkplanperiod);
            pst1.setString(1, "P");
            pst1.setString(2, SearchRef);
            pst1.setString(3, jLabelAcqProvince.getText());
            pst1.setString(4, jLabelAcqOffice.getText());
            pst1.setString(5, formatter.format(jLabelWk1From.getText()));
            pst1.setString(6, formatter.format(jLabelDateTo.getText()));
            pst1.setString(7, docNextVer);
            pst1.setString(8, "1");
            pst1.setString(9, "A");

            pst1.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void PlanReqClearAction() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlWkplanperiod = "insert into [ClaimsAppSysZvandiri].[dbo].[PlanReqClearTab] (SERIAL,PLAN_REF_NUM,REQ_SERIAL,"
                    + "REF_NUM,REQ_DATE,EMP_NAM,DOC_VER,ACT_REC_STA,ACT_TYPE)"
                    + " VALUES (?,?,?,?,?,?,?,?,?)";

            pst = conn.prepareStatement(sqlWkplanperiod);
            pst.setString(1, "P");
            pst.setString(2, SearchRef);
            pst.setString(3, jLabelSerialAcq.getText());
            pst.setString(4, jLabelRegNum.getText());
            pst.setString(5, jLabelGenDate.getText());
            pst.setString(6, jLabelLineLogNam.getText());
            pst.setString(7, "1");
            pst.setString(8, "A");
            pst.setString(9, "Per Diem");

            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void fetchdataGen() {
        try {

            System.out.println("ddd " + SearchRef);

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT PROVINCE ,DISTRICT ,WK1_PLAN_START_DATE ,WK1_PLAN_END_DATE "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab]"
                    + " where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'C'");

            ResultSet r = st.getResultSet();

            while (r.next()) {

                jLabelAcqProvince.setText(r.getString(1));
                jLabelAcqOffice.setText(r.getString(2));
                jLabelDateFrom.setText(r.getString(3));
                jLabelDateTo.setText(r.getString(4));
                actStartDate = r.getString(3);
                actEndDate = r.getString(4);

            }
            jLabelAcqActMainPurpose.setEditable(false);
            jLabelAcqActMainPurpose.setText("Activities for " + jLabelAcqEmpNam.getText() + " period : " + actStartDate + " to " + actEndDate);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchReqItmData() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] "
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'C' "
                    + "  and act_date in (SELECT ACT_DATE FROM [ClaimsAppSysZvandiri].[dbo].[PlanUsrRecTab] "
                    + "where status = 'A' and PLAN_WK = 1) and "
                    + "(EMP_NAM1 = '" + jLabelGenLogNam.getText() + "' or "
                    + "EMP_NAM2 = '" + jLabelGenLogNam.getText() + "' or"
                    + " EMP_NAM3 = '" + jLabelGenLogNam.getText() + "'  or "
                    + "EMP_NAM4 = '" + jLabelGenLogNam.getText() + "')");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                model.insertRow(model.getRowCount(), new Object[]{r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18)});

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

    void fetchAcqItmData() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] "
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'C' "
                    + "  and act_date in (SELECT ACT_DATE FROM [ClaimsAppSysZvandiri].[dbo].[PlanUsrRecTab] "
                    + "where status = 'A' and PLAN_WK = 1) and "
                    + "(EMP_NAM1 = '" + jLabelGenLogNam.getText() + "' or "
                    + "EMP_NAM2 = '" + jLabelGenLogNam.getText() + "' or"
                    + " EMP_NAM3 = '" + jLabelGenLogNam.getText() + "'  or "
                    + "EMP_NAM4 = '" + jLabelGenLogNam.getText() + "')");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelAcq.insertRow(modelAcq.getRowCount(), new Object[]{r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18)});

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fillDataNoAct() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] "
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'C' "
                    + "  and act_date in (SELECT ACT_DATE FROM [ClaimsAppSysZvandiri].[dbo].[PlanUsrRecTab] "
                    + "where status = 'A' and PLAN_WK = 1) and "
                    + "(EMP_NAM1 = '" + jLabelGenLogNam.getText() + "' or "
                    + "EMP_NAM2 = '" + jLabelGenLogNam.getText() + "' or"
                    + " EMP_NAM3 = '" + jLabelGenLogNam.getText() + "'  or "
                    + "EMP_NAM4 = '" + jLabelGenLogNam.getText() + "')");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelAcq.insertRow(modelAcq.getRowCount(), new Object[]{r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), "0", "0",
                    "0", "0", r.getString(15), "0", "0", "0"});

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void checkDupDate() {
        try {

            for (int i = 0; i < model.getRowCount(); i++) {

                Object id = model.getValueAt(i, 0);
                String refDate = id.toString();

                Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                        + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

                Statement st = conn.createStatement();

                st.executeQuery("SELECT count(*) FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] a join [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] b "
                        + "on a.REF_NUM = b.REF_NUM and a.DOC_VER = b.DOC_VER and a.ACT_REC_STA=b.ACT_REC_STA and a.SERIAL=b.SERIAL "
                        + "where ACT_DATE = '" + refDate + "' and b.EMP_NUM = '" + jLabelAcqEmpNum.getText() + "' and concat(a.serial,a.REF_NUM) not in "
                        + "(select concat(serial,REF_NUM) from [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] where USR_ACTION like '%rejected%' "
                        + "and SERIAL ='MA')");

                ResultSet r = st.getResultSet();

                while (r.next()) {
                    itmDateCount = r.getInt(1);

                }

                if (itmDateCount > 0) {
                    JOptionPane.showMessageDialog(this, "<html>Requesting of per diem "
                            + "twice for one day is not allowed. Another Perdiem request with activity date <b>"
                            + refDate + "</b> has already been registered in the system.Please check and correct.</html>", "Error Connection", JOptionPane.ERROR_MESSAGE);

                    break;
                }
            }

            if (itmDateCount == 0) {
                submitSaveRequest();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void deleteLongLock() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlDeleteLock = "delete [ClaimsAppSysZvandiri].[dbo].[SerialLock] where SERIAL = "
                    + "'MA'";

            pst = conn.prepareStatement(sqlDeleteLock);
            pst.executeUpdate();

        } catch (Exception e) {

        }
    }

    void SerialCheck() {

        int count = 0;
        do {

            try {

                Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                        + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

                String sqlDeleteBlankLock = "delete [ClaimsAppSysZvandiri].[dbo].[SerialLock] "
                        + "where (SERIAL = ' ' or SERIAL is null or REF_NUM = 0)";

                pst = conn.prepareStatement(sqlDeleteBlankLock);
                pst.executeUpdate();

                Statement st = conn.createStatement();

                st.executeQuery("SELECT count(*) FROM [ClaimsAppSysZvandiri].[dbo].[SerialLock] where SERIAL = 'MA'");

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

                        if ((jLabelAcqEmpNam.getText().equals(usrnam)) && (seconds >= 1)) {

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

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            st.executeQuery("SELECT REF_NUM + 1,SERIAL,REF_NUM FROM [ClaimsAppSysZvandiri].[dbo].[refNumTab] where  SERIAL = 'MA'");

            ResultSet rs = st.getResultSet();

            jLabelRegYear.setText(Integer.toString(finyear));
            while (rs.next()) {
                jLabelRegNum.setText(rs.getString(1));
                jLabelSerialAcq.setText(rs.getString(2));
                oldRefNum = rs.getString(3);
                newPlanRefNum = rs.getInt(1);

            }
            pst = conn.prepareStatement(sqlSerialLock);
            pst.setString(1, jLabelSerialAcq.getText());
            pst.setString(2, jLabelRegNum.getText());
            pst.setString(3, jLabelAcqEmpNam.getText());

            pst.executeUpdate();

            String sqlSerialLockUpdate = "update [ClaimsAppSysZvandiri].[dbo].[SerialLock] set LCK_DATE_TIME = ( select convert(varchar, getdate(), 20)) where "
                    + "concat(SERIAL,REF_NUM) = '" + jLabelSerialAcq.getText() + jLabelRegNum.getText() + "' ";

            pst1 = conn.prepareStatement(sqlSerialLockUpdate);
            pst1.executeUpdate();

            st1.executeQuery("SELECT max(REF_NUM) FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] WHERE SERIAL='MA'");

            ResultSet rs1 = st1.getResultSet();

            while (rs1.next()) {
                maxPlanRefNum = rs1.getInt(1);

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void refNumUpdate() {
        try {

            String sql1 = "update [ClaimsAppSysZvandiri].[dbo].[refNumTab] set "
                    + "REF_NUM ='" + jLabelRegNum.getText() + "' where REF_YEAR ='" + finyear + "' and SERIAL = 'MA'";

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            pst = conn.prepareStatement(sql1);
            pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    void insGenTab() {
        try {
            String sql = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] "
                    + "(REF_YEAR,SERIAL,REF_NUM,REF_DAT,EMP_NUM,EMP_NAM,EMP_TTL,EMP_PROV,EMP_OFF,"
                    + "EMP_BNK_NAM,ACC_NUM,ACT_MAIN_PUR,ACT_TOT_AMT,PREV_SERIAL,PREV_REF_NUM,"
                    + "PREV_REF_DAT,REG_MOD_VER,DOC_VER,ACT_REC_STA) "
                    + " VALUES (?,?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?)";

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            pst = conn.prepareStatement(sql);
            pst.setString(1, jLabelRegYear.getText());
            pst.setString(2, jLabelSerialAcq.getText());
            pst.setString(3, jLabelRegNum.getText());
            pst.setString(4, jLabelGenDate.getText());
            pst.setString(5, jLabelAcqEmpNum.getText());
            pst.setString(6, jLabelAcqEmpNam.getText());
            pst.setString(7, jLabelAcqEmpTitle.getText());
            pst.setString(8, jLabelAcqProvince.getText());
            pst.setString(9, jLabelAcqOffice.getText());
            pst.setString(10, jLabelAcqBankName.getText());
            pst.setString(11, jLabelAcqAccNum.getText());
            pst.setString(12, jLabelAcqActMainPurpose.getText());
            pst.setString(13, jLabelAcqAppTotReqCost.getText());
            pst.setString(14, jLabelSerialAcq.getText());
            pst.setString(15, jLabelRegNum.getText());
            pst.setString(16, jLabelGenDate.getText());
            pst.setString(17, "1");
            pst.setString(18, "1");
            pst.setString(19, "Q");

            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void insItmTab() {
        itmNum = 1;
        try {
            for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {

                String sqlitm = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                        + "(REF_YEAR ,SERIAL ,REF_NUM ,ITM_NUM ,ACT_DATE ,BRANCH ,PROJ_ID ,"
                        + "PRJ_TASK_CODE ,ACT_SITE ,ACT_ITM_PUR ,BRK_AMT ,LNC_AMT ,DIN_AMT ,"
                        + "INC_AMT ,MSC_ACT ,MSC_AMT ,ACC_UNPROV_AMT ,ACC_PRO_AMT ,ACT_ITM_TOT ,"
                        + "PLAN_WK ,REG_MOD_VER ,DOC_VER ,ACT_REC_STA)"
                        + " VALUES (?,?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?)";

                Connection conn1 = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                        + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
                pst1 = conn1.prepareStatement(sqlitm);
                // JOptionPane.showMessageDialog(null, jTextYear.getText());
                pst1.setString(1, jLabelRegYear.getText());
                pst1.setString(2, jLabelSerialAcq.getText());
                pst1.setString(3, jLabelRegNum.getText());
                pst1.setString(4, Integer.toString(itmNum));
                pst1.setString(5, jTableActivityAcq.getValueAt(i, 0).toString());
                pst1.setString(6, jTableActivityAcq.getValueAt(i, 1).toString());
                pst1.setString(7, jTableActivityAcq.getValueAt(i, 2).toString());
                pst1.setString(8, jTableActivityAcq.getValueAt(i, 3).toString());
                pst1.setString(9, jTableActivityAcq.getValueAt(i, 4).toString());
                pst1.setString(10, jTableActivityAcq.getValueAt(i, 5).toString());
                pst1.setString(11, jTableActivityAcq.getValueAt(i, 6).toString());
                pst1.setString(12, jTableActivityAcq.getValueAt(i, 7).toString());
                pst1.setString(13, jTableActivityAcq.getValueAt(i, 8).toString());
                pst1.setString(14, jTableActivityAcq.getValueAt(i, 9).toString());
                pst1.setString(15, jTableActivityAcq.getValueAt(i, 10).toString());
                pst1.setString(16, jTableActivityAcq.getValueAt(i, 11).toString());
                pst1.setString(17, jTableActivityAcq.getValueAt(i, 12).toString());
                pst1.setString(18, jTableActivityAcq.getValueAt(i, 13).toString());
                pst1.setString(19, jTableActivityAcq.getValueAt(i, 14).toString());
                pst1.setString(20, "1");
                pst1.setString(21, "1");
                pst1.setString(22, "1");
                pst1.setString(23, "Q");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void createAction() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlcreate = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] "
                    + "(YEAR,SERIAL,REF_NUM,USR_ACTION, DOC_STATUS,ACTIONED_BY_EMP_NUM ,"
                    + "ACTIONED_BY_NAM ,SEND_TO_EMP_NUM,SEND_TO_NAM,ACTIONED_ON_DATE, ACTIONED_ON_TIME,"
                    + "ACTIONED_ON_COMPUTER,REG_MOD_VER,ACTION_VER, DOC_VER ,ACT_REC_STA)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            String actionCode = "Acquittal Created";
            String statusCode = "AcqRegistered";

            pst1 = conn.prepareStatement(sqlcreate);

            pst1.setString(1, jLabelRegYear.getText());
            pst1.setString(2, jLabelSerialAcq.getText());
            pst1.setString(3, jLabelRegNum.getText());
            pst1.setString(4, actionCode);
            pst1.setString(5, statusCode);
            pst1.setString(6, jLabelEmp.getText());
            pst1.setString(7, createUsrNam);
            pst1.setString(8, supEmpNum);
            pst1.setString(9, supNam);
            pst1.setString(10, jLabelGenDate.getText());
            pst1.setString(11, jLabelGenTime.getText());
            pst1.setString(12, hostName);
            pst1.setString(13, "1");
            pst1.setString(14, "1");
            pst1.setString(15, "1");
            pst1.setString(16, "Q");
            pst1.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void createReport() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlRepCreate = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimTripReport] "
                    + "(PREV_SERIAL,PREV_REF_NUM,SERIAL,REF_NUM,NAM_TRAVEL,PLAN_WK,DOC_VER,REG_MOD_VER,ACT_REC_STA)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

            pst1 = conn.prepareStatement(sqlRepCreate);

            pst1.setString(1, jLabelSerialAcq.getText());
            pst1.setString(2, jLabelRegNum.getText());
            pst1.setString(3, jLabelSerialAcq.getText());
            pst1.setString(4, jLabelRegNum.getText());
            pst1.setString(5, jTextAreaNamTravel.getText());
            pst1.setString(6, "1");
            pst1.setString(7, "1");
            pst1.setString(8, "1");
            pst1.setString(9, "Q");
            pst1.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void createAttDoc() {
        try {
            itmNumAtt = 1;

            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            for (int i = 0; i < modelAtt.getRowCount(); i++) {
                String imgFileValue = modelAtt.getValueAt(i, 2).toString();
                String imgFileDsc = modelAtt.getValueAt(i, 0).toString();
                String imgFileName = modelAtt.getValueAt(i, 1).toString();

                if (imgFileValue.trim().length() > 0) {

                    attL.insertPDF7(conn, jLabelRegYear.getText(), jLabelRegNum.getText(), itmNumAtt, imgFileValue,
                            imgFileDsc, imgFileName, jLabelSerialAcq.getText());

                }
                itmNumAtt = itmNumAtt + 1;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void checkOutstandingRequest() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select count(*) from (SELECT a.REF_YEAR ,a.SERIAL ,a.REF_NUM,"
                    + "b.EMP_NAM,a.PLAN_WK \"Week\" ,sum(a.ACT_ITM_TOT) \"Total \" "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] a join [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] b "
                    + "on a.serial = b.serial and a.DOC_VER = b.DOC_VER and  a.ACT_REC_STA = b.ACT_REC_STA "
                    + "and a.REF_NUM = b.REF_NUM where a.serial = 'R' and a.DOC_VER = 5 and a.ACT_REC_STA = 'A' "
                    + "and CONCAT(a.Serial,a.ref_num)  not in (SELECT concat(SERIAL,REF_NUM) "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] where USR_ACTION like '%Reject%'  and SERIAL = 'R') "
                    + "and concat(a.SERIAL,a.REF_NUM) in (SELECT distinct concat(SERIAL,REF_NUM) "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] where  SERIAL = 'R') "
                    + "group by a.REF_YEAR,a.SERIAL,a.REF_NUM,a.PLAN_WK,b.EMP_NAM) y left "
                    + "join (SELECT a.REF_YEAR ,a.SERIAL ,a.REF_NUM,b.EMP_NAM,b.PREV_REF_NUM,a.PLAN_WK \"Week\" ,"
                    + "sum(a.ACT_ITM_TOT) \"Total \" FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] a "
                    + "join [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] b on a.serial = b.serial "
                    + "and a.DOC_VER = b.DOC_VER and  a.ACT_REC_STA = b.ACT_REC_STA and a.REF_NUM = b.REF_NUM "
                    + "where a.serial = 'A' and a.DOC_VER = 5 and b.PREV_REF_NUM > 0 and a.ACT_REC_STA = 'Q' "
                    + "and CONCAT(a.Serial,a.ref_num)  not in (SELECT concat(SERIAL,REF_NUM) "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] where USR_ACTION like '%Reject%'  and SERIAL = 'A') "
                    + "and concat(a.SERIAL,a.REF_NUM) in (SELECT distinct concat(SERIAL,REF_NUM) "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] where SERIAL = 'A' ) "
                    + "group by a.REF_YEAR,a.SERIAL,a.REF_NUM,a.PLAN_WK,b.EMP_NAM,b.PREV_REF_NUM) z "
                    + "on (y.REF_NUM = z.PREV_REF_NUM and y.Week = z.Week) where z.PREV_REF_NUM is null "
                    + "and y.EMP_NAM ='" + jLabelGenLogNam.getText() + "'");

            while (r.next()) {

                requestDays = r.getInt(1);

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void checkRegistration() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();
            Statement st3 = conn.createStatement();
            Statement st4 = conn.createStatement();
            Statement st5 = conn.createStatement();

            st.executeQuery("SELECT REF_NUM FROM [ClaimsAppSysZvandiri].[dbo].[refNumTab] "
                    + "WHERE concat(SERIAL,REF_NUM) ='" + jLabelSerialAcq.getText() + jLabelRegNum.getText() + "' "
                    + " and SERIAL = 'MA'");
            ResultSet r = st.getResultSet();

            while (r.next()) {
                refCheck = r.getInt(1);
            }

            st2.executeQuery("SELECT count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab]"
                    + " where concat(SERIAL,REF_NUM) ='" + jLabelSerialAcq.getText() + jLabelRegNum.getText() + "' "
                    + "and SERIAL = 'MA'");
            ResultSet r2 = st2.getResultSet();

            while (r2.next()) {
                genCount = r2.getInt(1);
            }

            st3.executeQuery("SELECT count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] where "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelSerialAcq.getText() + jLabelRegNum.getText() + "' "
                    + "and SERIAL = 'MA'");
            ResultSet r3 = st3.getResultSet();

            while (r3.next()) {
                itmCount = r3.getInt(1);
            }

            st4.executeQuery("SELECT  count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] "
                    + "where concat(SERIAL,REF_NUM) ='" + jLabelSerialAcq.getText() + jLabelRegNum.getText() + "' "
                    + "and SERIAL = 'MA'");
            ResultSet r4 = st4.getResultSet();

            while (r4.next()) {
                wfCount = r4.getInt(1);
            }
            System.out.println("ref " + refCheck + " " + jLabelRegNum.getText() + " gen " + genCount + " itm " + itmCount + " wf " + wfCount);
            if ((refCheck == (Integer.parseInt(jLabelRegNum.getText()))) && (genCount == 1) && (itmCount > 0)
                    && (wfCount == 1)) {

                deleteLongLock();

                jDialogWaitingEmail.setVisible(true);

                String mailMsg = "<html><body> Dear " + supNam + "<br><br><b>"
                        + jLabelLineLogNam.getText() + "</b> has submitted a per diem request for your review and approval.<br><br>"
                        + "Please check your Finance System inbox and action<br><br> Kind Regards <br><br>"
                        + " Finance Management System </body></html>";

                String MailMsgTitle = "Per Diem Approval Request - Reference No. " + jLabelSerialAcq.getText() + " " + jLabelRegNum.getText() + " ";

                emSend.sendMail(MailMsgTitle, supUsrMail, mailMsg, "");

                jDialogWaitingEmail.setVisible(false);

                jLabelRegNum.setVisible(true);
                jLabelRegYear.setVisible(true);
                jLabelSerialAcq.setVisible(true);
                JOptionPane.showMessageDialog(null, "Email has been forwarded to " + supNam + " for approval of your requistion No. "
                        + jLabelRegYear.getText() + " " + jLabelSerialAcq.getText() + " " + jLabelRegNum.getText());

                new JFrameMnthReqGenList(jLabelEmp.getText()).setVisible(true);
                setVisible(false);

            } else {
                jLabelRegNum.setVisible(false);
                jLabelRegYear.setVisible(false);
                jLabelSerialAcq.setVisible(false);
                regFail();
                JOptionPane.showMessageDialog(null, "<html> Registration failure. "
                        + "Registration falure can be caused by slow network speeds.<br><br> Please try again. If the problem persist contact IT.</html>");

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void regFail() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlDeleteLock = "delete [ClaimsAppSysZvandiri].[dbo].[SerialLock] where  "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelSerialAcq.getText() + jLabelRegNum.getText() + "'";

            pst = conn.prepareStatement(sqlDeleteLock);
            pst.executeUpdate();

            String sqlDeleteGen = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] where "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelSerialAcq.getText() + jLabelRegNum.getText() + "'";
            pst = conn.prepareStatement(sqlDeleteGen);
            pst.executeUpdate();

            String sqlDeleteItm = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] where "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelSerialAcq.getText() + jLabelRegNum.getText() + "'";
            pst = conn.prepareStatement(sqlDeleteItm);
            pst.executeUpdate();

            String sqlDeleteWF = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] where "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelSerialAcq.getText() + jLabelRegNum.getText() + "'";
            pst = conn.prepareStatement(sqlDeleteWF);
            pst.executeUpdate();

            String sqlDeleteDocAtt = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimDocAttachTab] where "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelSerialAcq.getText() + jLabelRegNum.getText() + "'";
            pst = conn.prepareStatement(sqlDeleteDocAtt);
            pst.executeUpdate();

            String sqlDeleteAck = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimsAckTab] where "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelSerialAcq.getText() + jLabelRegNum.getText() + "'";
            pst = conn.prepareStatement(sqlDeleteAck);
            pst.executeUpdate();

            String sqlDeletePlanClr = "delete [ClaimsAppSysZvandiri].[dbo].[PlanReqClearTab] where "
                    + "concat(REQ_SERIAL,REF_NUM) ='" + jLabelSerialAcq.getText() + jLabelRegNum.getText() + "'";
            pst = conn.prepareStatement(sqlDeletePlanClr);
            pst.executeUpdate();

            String sqlrefUpdate = "update [ClaimsAppSysZvandiri].[dbo].[refNumTab] set "
                    + "REF_NUM ='" + oldRefNum + "' where "
                    + "SERIAL='MA'";

            pst = conn.prepareStatement(sqlrefUpdate);
            pst.executeUpdate();

            String sqlPlanRec = "update [ClaimsAppSysZvandiri].[dbo].[PlanUsrRecTab] set STATUS ='A' "
                    + "where PLAN_REF_NUM =" + SearchRef + " and EMP_NAM = '" + jLabelGenLogNam.getText() + "'"
                    + " and ACT_TYPE='Per Diem'";
            pst = conn.prepareStatement(sqlPlanRec);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void insAckTab() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            String sqlAckUsr = "INSERT ClaimsAppSysZimTTECH.dbo.ClaimsAckTab (SERIAL,REF_NUM,"
                    + "EMP_NUM,ACK_SUBMIT,ACK_SAL_DEDUCT,ACK_DATE)"
                    + "VALUES (?,?, ?,?, ?, ?)";

            pst3 = conn.prepareStatement(sqlAckUsr);
            pst3.setString(1, jLabelSerialAcq.getText());
            pst3.setString(2, jLabelRegNum.getText());
            pst3.setString(3, jLabelAcqEmpNum.getText());
            pst3.setString(4, "Y");
            pst3.setString(5, "Y");
            pst3.setString(6, jLabelGenDate.getText());

            pst3.executeUpdate();

            st.executeQuery("select convert (varchar(50),DECRYPTBYPASSPHRASE('password',usrencrypass)) "
                    + "from [HRLeaveSysZvandiri].[dbo].[usrlogin] where EmpNum='" + jLabelAcqEmpNum.getText() + "'");
            ResultSet r = st.getResultSet();

            while (r.next()) {
                digSign = r.getString(1);
            }

            String sqlDigSignUpdate = "update [ClaimsAppSysZvandiri].[dbo].[ClaimsAckTab] "
                    + "set ACK_DIG_SIGN =ENCRYPTBYPASSPHRASE('password', '" + digSign + "')"
                    + "where concat(SERIAL,REF_NUM) = '" + jLabelSerialAcq.getText() + jLabelRegNum.getText() + "' ";

            pst4 = conn.prepareStatement(sqlDigSignUpdate);
            pst4.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void WkPlanActionUpd() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlPlanAct = "update [ClaimsAppSysZvandiri].[dbo].[PlanUsrRecTab] set "
                    + "STATUS ='P' where PLAN_REF_NUM =" + SearchRef + " and STATUS= 'A' "
                    + "and EMP_NAM = '" + jLabelGenLogNam.getText() + "' and ACT_TYPE='Per Diem'";

            pst = conn.prepareStatement(sqlPlanAct);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void submitSaveRequest() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            jMenuItemSubmit.setEnabled(false);
            jButtonSave.setEnabled(false);
            SerialCheck();

            RefNumAllocation();

            if (maxPlanRefNum > newPlanRefNum) {
                JOptionPane.showMessageDialog(this, "<html> Registration failure.Max Reg. "
                        + "Registration falure can be caused by slow network speeds.<br><br> "
                        + "Please try again. If the problem persist contact IT.</html>");
                jMenuItemSubmit.setEnabled(true);
                jButtonSave.setEnabled(true);

            } else {

                insGenTab();

                insItmTab();

                createAction();

                createReport();

                createAttDoc();

//                insAckTab();
                WkPlanActionUpd();

                PlanReqClearAction();

                refNumUpdate();

                checkRegistration();
            }
        } catch (SQLException e) {

            sqlError = e.getErrorCode();
            if (sqlError == 2627) {
                checkRegistration();
            }

        }

    }

    void calTot() {
        Double lineTot = 0.00;
        for (int row = 0; row < jTableReqActivities.getRowCount(); row++) {
            lineTot = Double.parseDouble((String) jTableReqActivities.getValueAt(row, 6))
                    + Double.parseDouble((String) jTableReqActivities.getValueAt(row, 7))
                    + Double.parseDouble((String) jTableReqActivities.getValueAt(row, 8))
                    + Double.parseDouble((String) jTableReqActivities.getValueAt(row, 9))
                    + Double.parseDouble((String) jTableReqActivities.getValueAt(row, 11))
                    + Double.parseDouble((String) jTableReqActivities.getValueAt(row, 12))
                    + Double.parseDouble((String) jTableReqActivities.getValueAt(row, 13));
            jTableReqActivities.setValueAt((String.format("%.2f", lineTot)), row, 14);

        }

    }

    void calTotAcq() {
        Double lineTot = 0.00;
        for (int row = 0; row < jTableActivityAcq.getRowCount(); row++) {
            lineTot = Double.parseDouble((String) jTableActivityAcq.getValueAt(row, 6))
                    + Double.parseDouble((String) jTableActivityAcq.getValueAt(row, 7))
                    + Double.parseDouble((String) jTableActivityAcq.getValueAt(row, 8))
                    + Double.parseDouble((String) jTableActivityAcq.getValueAt(row, 9))
                    + Double.parseDouble((String) jTableActivityAcq.getValueAt(row, 11))
                    + Double.parseDouble((String) jTableActivityAcq.getValueAt(row, 12))
                    + Double.parseDouble((String) jTableActivityAcq.getValueAt(row, 13));
            jTableActivityAcq.setValueAt((String.format("%.2f", lineTot)), row, 14);

        }

    }

    void mainPageTotInsert() {

        DecimalFormat numFormat;
        numFormat = new DecimalFormat("000.##");
        double breakfastsubtotal = 0;
        for (int i = 0; i < jTableReqActivities.getRowCount(); i++) {
            double breakfastamount = Double.parseDouble((String) jTableReqActivities.getValueAt(i, 6));
            breakfastsubtotal += breakfastamount;
        }

        jLabelBreakFastSubTot.setText(String.format("%.2f", breakfastsubtotal));

        double lunchsubtotal = 0;
        for (int i = 0; i < jTableReqActivities.getRowCount(); i++) {
            double lunchamount = Double.parseDouble((String) jTableReqActivities.getValueAt(i, 7));
            lunchsubtotal += lunchamount;
        }

        jLabelLunchSubTot.setText(String.format("%.2f", lunchsubtotal));

        double dinnersubtotal = 0;
        for (int i = 0; i < jTableReqActivities.getRowCount(); i++) {
            double dinneramount = Double.parseDouble((String) jTableReqActivities.getValueAt(i, 8));
            dinnersubtotal += dinneramount;
        }

        jLabelDinnerSubTot.setText(String.format("%.2f", dinnersubtotal));

        double incidentalsubtotal = 0;
        for (int i = 0; i < jTableReqActivities.getRowCount(); i++) {
            double incidentalamount = Double.parseDouble((String) jTableReqActivities.getValueAt(i, 9));
            incidentalsubtotal += incidentalamount;
        }

        jLabelIncidentalSubTot.setText(String.format("%.2f", incidentalsubtotal));

        double miscSubTot = 0;
        for (int i = 0; i < jTableReqActivities.getRowCount(); i++) {
            double miscamount = Double.parseDouble((String) jTableReqActivities.getValueAt(i, 11));
            miscSubTot += miscamount;
        }

        jLabelMiscSubTot.setText(String.format("%.2f", miscSubTot));

        double unprovedSubTot = 0;
        for (int i = 0; i < jTableReqActivities.getRowCount(); i++) {
            double unprovedamount = Double.parseDouble((String) jTableReqActivities.getValueAt(i, 12));
            unprovedSubTot += unprovedamount;
        }

        jLabelAccUnprovedSubTot.setText(String.format("%.2f", unprovedSubTot));

        double provedSubTot = 0;
        for (int i = 0; i < jTableReqActivities.getRowCount(); i++) {
            double provedamount = Double.parseDouble((String) jTableReqActivities.getValueAt(i, 13));
            provedSubTot += provedamount;
        }

        jLabelAccProvedSubTot.setText(String.format("%.2f", provedSubTot));

        double allReqTotal = unprovedSubTot + miscSubTot + incidentalsubtotal
                + dinnersubtotal + lunchsubtotal + breakfastsubtotal + provedSubTot;
        numFormat.format(allReqTotal);

        jLabelAcqAppTotPlannedCost.setText(String.format("%.2f", allReqTotal));

    }

    void mainPageTotAcq() {

        DecimalFormat numFormat;
        numFormat = new DecimalFormat("000.##");
        double breakfastsubtotal = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double breakfastamount = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 6));
            breakfastsubtotal += breakfastamount;
        }

        jLabelAcqBreakFastSubTot.setText(String.format("%.2f", breakfastsubtotal));

        double lunchsubtotal = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double lunchamount = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 7));
            lunchsubtotal += lunchamount;
        }

        jLabelAcqLunchSubTot.setText(String.format("%.2f", lunchsubtotal));

        double dinnersubtotal = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double dinneramount = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 8));
            dinnersubtotal += dinneramount;
        }

        jLabelAcqDinnerSubTot.setText(String.format("%.2f", dinnersubtotal));

        double incidentalsubtotal = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double incidentalamount = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 9));
            incidentalsubtotal += incidentalamount;
        }

        jLabelAcqIncidentalSubTot.setText(String.format("%.2f", incidentalsubtotal));

        double miscSubTot = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double miscamount = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 11));
            miscSubTot += miscamount;
        }

        jLabelAcqMiscSubTot.setText(String.format("%.2f", miscSubTot));

        double unprovedSubTot = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double unprovedamount = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 12));
            unprovedSubTot += unprovedamount;
        }

        jLabelAcqAccUnprovedSubTot.setText(String.format("%.2f", unprovedSubTot));

        double provedSubTot = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double provedamount = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 13));
            provedSubTot += provedamount;
        }

        jLabelAcqAccProvedSubTot.setText(String.format("%.2f", provedSubTot));

        double allTotal = unprovedSubTot + miscSubTot + incidentalsubtotal
                + dinnersubtotal + lunchsubtotal + breakfastsubtotal + provedSubTot;
        numFormat.format(allTotal);

        jLabelAcqAppTotReqCost.setText(String.format("%.2f", allTotal));

    }

    void addFileAtt() {

        attFileDesc = jTextAttDocDesc.getText();

        modelAtt.addRow(new Object[]{attFileDesc,
            jTextAttAcqDocName.getText(), jTextAttDocFilePath.getText()});
    }

    void deleteFileAttReport() {
        int selectedOption = JOptionPane.showConfirmDialog(null,
                "Do you want to delete selected attachement?",
                "Choose", JOptionPane.YES_NO_OPTION);

        if (selectedOption == JOptionPane.YES_OPTION) {

            int[] rows = jTableAcquittalDocAtt.getSelectedRows();
            if (rows.length > 0) {
                for (int i = 0; i < rows.length; i++) {
                    modelAtt.removeRow(rows[i] - i);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please note that the table is empty.");

            }

        }
    }

    void deleteFileAtt() {
        int selectedOption = JOptionPane.showConfirmDialog(null,
                "Do you want to delete selected attachement?",
                "Choose", JOptionPane.YES_NO_OPTION);

        if (selectedOption == JOptionPane.YES_OPTION) {

            int[] rows = jTableAcquittalDocAtt.getSelectedRows();
            if (rows.length > 0) {
                for (int i = 0; i < rows.length; i++) {
                    modelAtt.removeRow(rows[i] - i);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please note that the table is empty.");

            }

        }
    }

    void refreshAttFields() {
        jTextAttDocDesc.setText("");
        jTextAttAcqDocName.setText("");
        jTextAttDocFilePath.setText("");
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
        jDialogPayType = new javax.swing.JDialog();
        jLabelPayTypeHeader = new javax.swing.JLabel();
        jLabelPhoneDet = new javax.swing.JLabel();
        jPanellPayType = new javax.swing.JPanel();
        jRadioMukuru = new javax.swing.JRadioButton();
        jRadioBank = new javax.swing.JRadioButton();
        jTextAccNum = new javax.swing.JTextField();
        jTextAccDet1 = new javax.swing.JTextField();
        jLabellPayType = new javax.swing.JLabel();
        jButtonOKPayType = new javax.swing.JButton();
        jButtonCancelPayType = new javax.swing.JButton();
        jTextBankNam = new javax.swing.JTextField();
        buttonGroupPayType = new javax.swing.ButtonGroup();
        buttonGroupAcc = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanelGen = new javax.swing.JPanel();
        jLabelLogo = new javax.swing.JLabel();
        jLabelHeaderGen = new javax.swing.JLabel();
        jLabelGenDate = new javax.swing.JLabel();
        jLabelGenTime = new javax.swing.JLabel();
        jLabelGenLogNam6 = new javax.swing.JLabel();
        jLabelGenLogNam = new javax.swing.JLabel();
        jLabelEmpNumber = new javax.swing.JLabel();
        jLabelAcqEmpNam = new javax.swing.JLabel();
        jSeparator11 = new javax.swing.JSeparator();
        jLabelSepDetails = new javax.swing.JLabel();
        jLabelProvince2 = new javax.swing.JLabel();
        jLabelBank = new javax.swing.JLabel();
        jLabelAcqBankName = new javax.swing.JLabel();
        jSeparator12 = new javax.swing.JSeparator();
        jLabelAcqEmpTitle = new javax.swing.JLabel();
        jLabelFinDetails = new javax.swing.JLabel();
        jLabelActMainPurpose = new javax.swing.JLabel();
        jLabelAcqActMainPurpose = new javax.swing.JTextField();
        jLabelAcqAppTotReqCost = new javax.swing.JLabel();
        jLabelAppTotReq = new javax.swing.JLabel();
        jLabelOffice = new javax.swing.JLabel();
        jLabelAcqAccNum = new javax.swing.JLabel();
        jLabelACC = new javax.swing.JLabel();
        jLabelNum = new javax.swing.JLabel();
        jLabelRegNum = new javax.swing.JLabel();
        jLabelRegYear = new javax.swing.JLabel();
        jLabelSerialAcq = new javax.swing.JLabel();
        jLabelEmp = new javax.swing.JLabel();
        jLabelAcqEmpNum = new javax.swing.JLabel();
        jLabelAcqProvince = new javax.swing.JLabel();
        jLabelAcqOffice = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabelIncidentalSub = new javax.swing.JLabel();
        jLabelIncidentalSubTot = new javax.swing.JLabel();
        jLabelLunchSub = new javax.swing.JLabel();
        jLabelDinnerSub = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabelBreakFastSub = new javax.swing.JLabel();
        jLabelAcqBreakFastSubTot = new javax.swing.JLabel();
        jLabelLunchSubTot = new javax.swing.JLabel();
        jLabelDinnerSubTot = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jLabelBreakFastSubTot = new javax.swing.JLabel();
        jLabelAcqLunchSubTot = new javax.swing.JLabel();
        jLabelAcqDinnerSubTot = new javax.swing.JLabel();
        jLabelAcqIncidentalSubTot = new javax.swing.JLabel();
        jLabelAllReq = new javax.swing.JLabel();
        jLabelAllAcq = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabelMiscSubTot = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabelMscSub = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabelAcqMiscSubTot = new javax.swing.JLabel();
        jLabelMiscReq = new javax.swing.JLabel();
        jLabelMiscAcq = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabelAccUnprovedSubTot = new javax.swing.JLabel();
        jLabelAccProvedSub = new javax.swing.JLabel();
        jLabelAccProvedSubTot = new javax.swing.JLabel();
        jLabelAccUnprovedSub = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        jLabelAcqAccProvedSubTot = new javax.swing.JLabel();
        jLabelAccAcq = new javax.swing.JLabel();
        jLabelAccReq = new javax.swing.JLabel();
        jLabelAcqAccUnprovedSubTot = new javax.swing.JLabel();
        jLabelAppTotPlannedReq = new javax.swing.JLabel();
        jLabelAcqAppTotPlannedCost = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jButtonSave = new javax.swing.JButton();
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
        jLabelLogo1 = new javax.swing.JLabel();
        jLabelDateFrom = new javax.swing.JLabel();
        jLabelDateTo = new javax.swing.JLabel();
        jLabelHeaderGen1 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTableReqActivities = new javax.swing.JTable();
        jPanelAcquittal = new javax.swing.JPanel();
        jLabelLogo2 = new javax.swing.JLabel();
        jLabelHeaderLine1 = new javax.swing.JLabel();
        jLabelLineDate1 = new javax.swing.JLabel();
        jLabelLineTime1 = new javax.swing.JLabel();
        jLabellogged1 = new javax.swing.JLabel();
        jLabelLineLogNam1 = new javax.swing.JLabel();
        jPanelCaptureDetails = new javax.swing.JPanel();
        jDateChooserActivityAcq = new com.toedter.calendar.JDateChooser();
        jLabelActDate = new javax.swing.JLabel();
        jTextPurpose = new javax.swing.JTextField();
        jLabelBreakfast = new javax.swing.JLabel();
        jTextAcqBreakfast = new javax.swing.JTextField();
        jTextAcqLunch = new javax.swing.JTextField();
        jTextAcqDinner = new javax.swing.JTextField();
        jTextMiscAmtAcq = new javax.swing.JTextField();
        jLabelMisc = new javax.swing.JLabel();
        jTextMiscActivityAcq = new javax.swing.JTextField();
        jLabelSubAcc = new javax.swing.JLabel();
        jTextAcqIncidental = new javax.swing.JTextField();
        jButtonAddActivity = new javax.swing.JButton();
        jButtonDeleted = new javax.swing.JButton();
        jLabelActLinePurpose = new javax.swing.JLabel();
        jCheckBreakfast = new javax.swing.JCheckBox();
        jCheckIncidental = new javax.swing.JCheckBox();
        jCheckDinner = new javax.swing.JCheckBox();
        jLabelBreakfast1 = new javax.swing.JLabel();
        jLabelBreakfast2 = new javax.swing.JLabel();
        jLabelBreakfast3 = new javax.swing.JLabel();
        jRadioAccUnproved = new javax.swing.JRadioButton();
        jLabelUnproved = new javax.swing.JLabel();
        jTextAccUnprovedAcq = new javax.swing.JTextField();
        jRadioAccProved = new javax.swing.JRadioButton();
        jLabelProved = new javax.swing.JLabel();
        jTextAccProvedAcq = new javax.swing.JTextField();
        jLabelProjectName = new javax.swing.JLabel();
        jComboProjectName = new javax.swing.JComboBox<>();
        jLabelProjectTask = new javax.swing.JLabel();
        jComboProjectTask = new javax.swing.JComboBox<>();
        jLabelDialogWk1Site1 = new javax.swing.JLabel();
        jTextFieldDialogSite = new javax.swing.JTextField();
        jCheckLunch = new javax.swing.JCheckBox();
        jRadioNoAcc = new javax.swing.JRadioButton();
        jLabelProved1 = new javax.swing.JLabel();
        jTextNoAcc = new javax.swing.JTextField();
        jToggleButtonNoActivity = new javax.swing.JToggleButton();
        jToggleButtonAllActivities = new javax.swing.JToggleButton();
        jLabelAcqWk2 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTableActivityAcq = new javax.swing.JTable();
        jTabbedPaneAcqAtt = new javax.swing.JTabbedPane();
        jPanelReport = new javax.swing.JPanel();
        jPanelReportDetails = new javax.swing.JPanel();
        jLabelNamTravel = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextAreaNamTravel = new javax.swing.JTextArea();
        jPanel14 = new javax.swing.JPanel();
        jPanelAttach = new javax.swing.JPanel();
        jLabelAttach = new javax.swing.JLabel();
        jButtonSelectFile = new javax.swing.JButton();
        jTextAttDocDesc = new javax.swing.JTextField();
        jTextAttDocFilePath = new javax.swing.JTextField();
        jTextAttAcqDocName = new javax.swing.JTextField();
        jButtonAdd = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTableAcquittalDocAtt = new javax.swing.JTable();
        jPanelAcqELog = new javax.swing.JPanel();
        jPanelAtt4 = new javax.swing.JPanel();
        jLabelDateRange = new javax.swing.JLabel();
        jLabelVehReg = new javax.swing.JLabel();
        jTextVehReg = new javax.swing.JTextField();
        jLabelTripDetails = new javax.swing.JLabel();
        jLabelFrom = new javax.swing.JLabel();
        jLabelTo = new javax.swing.JLabel();
        jDateTripFrom = new com.toedter.calendar.JDateChooser();
        jDateTripTo = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jButtonGetDetails1 = new javax.swing.JButton();
        jButtonDeleteDetails = new javax.swing.JButton();
        jLabelLogo6 = new javax.swing.JLabel();
        jLabelHeaderLine3 = new javax.swing.JLabel();
        jLabelLineDate3 = new javax.swing.JLabel();
        jLabelLineTime3 = new javax.swing.JLabel();
        jLabellogged3 = new javax.swing.JLabel();
        jLabelLineLogNam8 = new javax.swing.JLabel();
        jLabelAcqWk4 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTableTripDetails = new javax.swing.JTable();
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
        jMenuEdit = new javax.swing.JMenu();
        jMenuMonPlanEdit = new javax.swing.JMenuItem();
        jSeparator35 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSubmit = new javax.swing.JMenuItem();
        jSeparator15 = new javax.swing.JPopupMenu.Separator();
        jMenuItemClose = new javax.swing.JMenuItem();
        jSeparator16 = new javax.swing.JPopupMenu.Separator();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuRequest = new javax.swing.JMenu();
        jMenuItemSupApp = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAccMgrRev = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        jMenuItemHeadApp = new javax.swing.JMenuItem();
        jSeparator21 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSchPerDiem = new javax.swing.JMenuItem();
        jSeparator23 = new javax.swing.JPopupMenu.Separator();
        jMenuItemView = new javax.swing.JMenuItem();
        jMenuAcquittal = new javax.swing.JMenu();
        jMenuItemAcqSupApp = new javax.swing.JMenuItem();
        jSeparator31 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqAccApp = new javax.swing.JMenuItem();
        jSeparator37 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqHeadApp = new javax.swing.JMenuItem();
        jSeparator38 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqSchGen = new javax.swing.JMenuItem();
        jSeparator18 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqView = new javax.swing.JMenuItem();
        jMenuMonthlyPlan = new javax.swing.JMenu();
        jMenuPlanApproval = new javax.swing.JMenu();
        jMenuItemPlanSupApproval = new javax.swing.JMenuItem();
        jSeparator62 = new javax.swing.JPopupMenu.Separator();
        jMenuItemPlanFinApproval = new javax.swing.JMenuItem();
        jSeparator63 = new javax.swing.JPopupMenu.Separator();
        jMenuItemPlanHODApproval = new javax.swing.JMenuItem();
        jSeparator26 = new javax.swing.JPopupMenu.Separator();
        jMenuItemReqGenLst = new javax.swing.JMenuItem();
        jSeparator34 = new javax.swing.JPopupMenu.Separator();
        jMenuItemPlanView = new javax.swing.JMenuItem();
        jMenuReports = new javax.swing.JMenu();
        jMenuItemReqSubmitted = new javax.swing.JMenuItem();
        jSeparator22 = new javax.swing.JPopupMenu.Separator();
        jMenuItemUserCreate = new javax.swing.JMenuItem();
        jSeparator24 = new javax.swing.JPopupMenu.Separator();
        jMenuItemUserProfUpd = new javax.swing.JMenuItem();

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
        jDialogWaiting.setLocation(new java.awt.Point(500, 350));
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

        jDialogAckSalDeduct.setLocation(new java.awt.Point(450, 250));
        jDialogAckSalDeduct.setMinimumSize(new java.awt.Dimension(620, 250));
        jDialogAckSalDeduct.setResizable(false);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setMinimumSize(new java.awt.Dimension(620, 250));
        jPanel7.setLayout(null);

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel7.setText("<html>I shall acquit this perdiem request within five days, after the last activity.If I fail to submit an acquittal for this perdiem request in<br> the prescribed period, I authorise Zim-TTECH to deduct the unacquitted perdiem request amount from my salary without further notice.<br><br>I acknowledge that failure to submit acquittal for this perdiem request  <b>may</b> result in a displinary action. </html>");
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
        jCheckBoxSalAckAgree.setBounds(10, 130, 590, 21);

        jButtonAckSalCont.setText("Continue ");
        jButtonAckSalCont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAckSalContActionPerformed(evt);
            }
        });
        jPanel7.add(jButtonAckSalCont);
        jButtonAckSalCont.setBounds(160, 160, 100, 21);

        jButtonSalAckCancel.setText("Cancel");
        jButtonSalAckCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalAckCancelActionPerformed(evt);
            }
        });
        jPanel7.add(jButtonSalAckCancel);
        jButtonSalAckCancel.setBounds(310, 160, 80, 21);

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

        jDialogAckSubmit.setLocation(new java.awt.Point(450, 250));
        jDialogAckSubmit.setMinimumSize(new java.awt.Dimension(620, 250));
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
        jCheckBoxSalSubmitAgree.setBounds(10, 100, 590, 21);

        jButtonAckSubmitCont.setText("Continue ");
        jButtonAckSubmitCont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAckSubmitContActionPerformed(evt);
            }
        });
        jPanel5.add(jButtonAckSubmitCont);
        jButtonAckSubmitCont.setBounds(160, 140, 90, 21);

        jButtonSalSubmitCancel.setText("Cancel");
        jButtonSalSubmitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalSubmitCancelActionPerformed(evt);
            }
        });
        jPanel5.add(jButtonSalSubmitCancel);
        jButtonSalSubmitCancel.setBounds(310, 140, 80, 21);

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
        jLabelDialogWk1Site.setBounds(20, 100, 40, 13);

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
        jCheckBoxDialogWk1BrkFast.setBounds(540, 100, 90, 21);

        jCheckBoxDialogWk1Lunch.setText("Lunch");
        jPanel1.add(jCheckBoxDialogWk1Lunch);
        jCheckBoxDialogWk1Lunch.setBounds(660, 100, 80, 21);

        jCheckBoxDialogWk1Dinner.setText(" Dinner");
        jPanel1.add(jCheckBoxDialogWk1Dinner);
        jCheckBoxDialogWk1Dinner.setBounds(540, 150, 90, 21);

        jCheckBoxDialogWk1Acc.setText(" Unproved Acc");
        jPanel1.add(jCheckBoxDialogWk1Acc);
        jCheckBoxDialogWk1Acc.setBounds(660, 150, 130, 21);

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
        jCheckBoxDialogWk1Misc.setBounds(660, 200, 140, 21);

        jCheckBoxDialogWk1Inc.setText("Incidental");
        jPanel1.add(jCheckBoxDialogWk1Inc);
        jCheckBoxDialogWk1Inc.setBounds(540, 200, 110, 21);

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
        jLabelDialogWk3Site.setBounds(20, 90, 40, 13);

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
        jCheckBoxDialogWk3BrkFast.setBounds(550, 110, 90, 21);

        jCheckBoxDialogWk3Lunch.setText("Lunch");
        jPanel6.add(jCheckBoxDialogWk3Lunch);
        jCheckBoxDialogWk3Lunch.setBounds(670, 110, 80, 21);

        jCheckBoxDialogWk3Dinner.setText(" Dinner");
        jPanel6.add(jCheckBoxDialogWk3Dinner);
        jCheckBoxDialogWk3Dinner.setBounds(550, 160, 90, 21);

        jCheckBoxDialogWk3Acc.setText(" Unproved Acc");
        jPanel6.add(jCheckBoxDialogWk3Acc);
        jCheckBoxDialogWk3Acc.setBounds(670, 160, 130, 21);

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
        jCheckBoxDialogWk3Misc.setBounds(670, 210, 140, 21);

        jCheckBoxDialogWk3Inc.setText("Incidental");
        jPanel6.add(jCheckBoxDialogWk3Inc);
        jCheckBoxDialogWk3Inc.setBounds(550, 210, 110, 21);

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
        jCheckBoxDialogWk2BrkFast.setBounds(560, 110, 90, 21);

        jCheckBoxDialogWk2Lunch.setText("Lunch");
        jPanel2.add(jCheckBoxDialogWk2Lunch);
        jCheckBoxDialogWk2Lunch.setBounds(680, 110, 80, 21);

        jCheckBoxDialogWk2Dinner.setText(" Dinner");
        jPanel2.add(jCheckBoxDialogWk2Dinner);
        jCheckBoxDialogWk2Dinner.setBounds(560, 160, 90, 21);

        jCheckBoxDialogWk2Acc.setText(" Unproved Acc");
        jPanel2.add(jCheckBoxDialogWk2Acc);
        jCheckBoxDialogWk2Acc.setBounds(680, 160, 130, 21);

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
        jCheckBoxDialogWk2Misc.setBounds(680, 210, 140, 21);

        jCheckBoxDialogWk2Inc.setText("Incidental");
        jPanel2.add(jCheckBoxDialogWk2Inc);
        jCheckBoxDialogWk2Inc.setBounds(560, 210, 110, 21);

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
        jDialogWaitingEmail.setLocation(new java.awt.Point(500, 350));
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

        jDialogPayType.setTitle("Payment Method Selection");
        jDialogPayType.setBackground(new java.awt.Color(153, 153, 153));
        jDialogPayType.setLocation(new java.awt.Point(400, 300));
        jDialogPayType.setMinimumSize(new java.awt.Dimension(550, 245));
        jDialogPayType.getContentPane().setLayout(null);

        jLabelPayTypeHeader.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabelPayTypeHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelPayTypeHeader.setText("Payment Method");
        jLabelPayTypeHeader.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jDialogPayType.getContentPane().add(jLabelPayTypeHeader);
        jLabelPayTypeHeader.setBounds(150, 10, 170, 40);

        jLabelPhoneDet.setFont(new java.awt.Font("Tahoma", 3, 10)); // NOI18N
        jLabelPhoneDet.setForeground(new java.awt.Color(255, 0, 0));
        jLabelPhoneDet.setText("Enter Mukuru Payment Phone Number e.g. 263772485123");
        jDialogPayType.getContentPane().add(jLabelPhoneDet);
        jLabelPhoneDet.setBounds(10, 140, 340, 25);

        jPanellPayType.setBackground(new java.awt.Color(255, 255, 255));
        jPanellPayType.setLayout(null);

        buttonGroupPayType.add(jRadioMukuru);
        jRadioMukuru.setText(" Mukuru");
        jRadioMukuru.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jRadioMukuruMouseClicked(evt);
            }
        });
        jPanellPayType.add(jRadioMukuru);
        jRadioMukuru.setBounds(10, 10, 80, 30);

        buttonGroupPayType.add(jRadioBank);
        jRadioBank.setText("Bank Account");
        jRadioBank.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jRadioBankMouseClicked(evt);
            }
        });
        jPanellPayType.add(jRadioBank);
        jRadioBank.setBounds(109, 10, 100, 30);

        jTextAccNum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextAccNumKeyPressed(evt);
            }
        });
        jPanellPayType.add(jTextAccNum);
        jTextAccNum.setBounds(240, 10, 190, 30);

        jTextAccDet1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextAccDet1KeyPressed(evt);
            }
        });
        jPanellPayType.add(jTextAccDet1);
        jTextAccDet1.setBounds(240, 10, 190, 30);

        jDialogPayType.getContentPane().add(jPanellPayType);
        jPanellPayType.setBounds(10, 90, 470, 50);

        jLabellPayType.setFont(new java.awt.Font("Tahoma", 3, 10)); // NOI18N
        jLabellPayType.setText("Please select preffered payment method");
        jDialogPayType.getContentPane().add(jLabellPayType);
        jLabellPayType.setBounds(10, 60, 310, 25);

        jButtonOKPayType.setText("Ok");
        jButtonOKPayType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOKPayTypeActionPerformed(evt);
            }
        });
        jDialogPayType.getContentPane().add(jButtonOKPayType);
        jButtonOKPayType.setBounds(120, 170, 90, 30);

        jButtonCancelPayType.setText("Cancel");
        jButtonCancelPayType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelPayTypeActionPerformed(evt);
            }
        });
        jDialogPayType.getContentPane().add(jButtonCancelPayType);
        jButtonCancelPayType.setBounds(260, 170, 90, 30);

        jTextBankNam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextBankNamKeyPressed(evt);
            }
        });
        jDialogPayType.getContentPane().add(jTextBankNam);
        jTextBankNam.setBounds(320, 60, 160, 30);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PER DIEM REQUEST - CREATE");

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

        jLabelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelGen.add(jLabelLogo);
        jLabelLogo.setBounds(10, 10, 220, 100);

        jLabelHeaderGen.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen.setForeground(new java.awt.Color(255, 255, 255));
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
        jPanelGen.add(jLabelAcqEmpNam);
        jLabelAcqEmpNam.setBounds(300, 140, 340, 30);
        jPanelGen.add(jSeparator11);
        jSeparator11.setBounds(10, 195, 1340, 2);

        jLabelSepDetails.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelSepDetails.setForeground(new java.awt.Color(255, 0, 0));
        jLabelSepDetails.setText("Employee Details");
        jPanelGen.add(jLabelSepDetails);
        jLabelSepDetails.setBounds(20, 165, 140, 30);

        jLabelProvince2.setText("Province");
        jPanelGen.add(jLabelProvince2);
        jLabelProvince2.setBounds(20, 200, 70, 30);

        jLabelBank.setText("Bank");
        jPanelGen.add(jLabelBank);
        jLabelBank.setBounds(20, 240, 40, 30);

        jLabelAcqBankName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelAcqBankNameMouseClicked(evt);
            }
        });
        jPanelGen.add(jLabelAcqBankName);
        jLabelAcqBankName.setBounds(150, 240, 300, 30);
        jPanelGen.add(jSeparator12);
        jSeparator12.setBounds(10, 310, 1340, 2);
        jPanelGen.add(jLabelAcqEmpTitle);
        jLabelAcqEmpTitle.setBounds(760, 140, 400, 30);

        jLabelFinDetails.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelFinDetails.setForeground(new java.awt.Color(255, 0, 0));
        jLabelFinDetails.setText("Financial Details");
        jPanelGen.add(jLabelFinDetails);
        jLabelFinDetails.setBounds(20, 280, 110, 30);

        jLabelActMainPurpose.setText("Activity Main Purpose");
        jPanelGen.add(jLabelActMainPurpose);
        jLabelActMainPurpose.setBounds(10, 320, 130, 30);

        jLabelAcqActMainPurpose.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jLabelAcqActMainPurposeFocusGained(evt);
            }
        });
        jLabelAcqActMainPurpose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jLabelAcqActMainPurposeActionPerformed(evt);
            }
        });
        jPanelGen.add(jLabelAcqActMainPurpose);
        jLabelAcqActMainPurpose.setBounds(150, 320, 760, 30);

        jLabelAcqAppTotReqCost.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelAcqAppTotReqCost.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAcqAppTotReqCost.setText("0");
        jPanelGen.add(jLabelAcqAppTotReqCost);
        jLabelAcqAppTotReqCost.setBounds(1220, 500, 100, 30);

        jLabelAppTotReq.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelAppTotReq.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAppTotReq.setText("Total Claimed Amount");
        jPanelGen.add(jLabelAppTotReq);
        jLabelAppTotReq.setBounds(1020, 500, 180, 30);

        jLabelOffice.setText("Office");
        jPanelGen.add(jLabelOffice);
        jLabelOffice.setBounds(570, 200, 70, 30);
        jPanelGen.add(jLabelAcqAccNum);
        jLabelAcqAccNum.setBounds(700, 240, 130, 30);

        jLabelACC.setText("Account No.");
        jPanelGen.add(jLabelACC);
        jLabelACC.setBounds(570, 240, 80, 30);

        jLabelNum.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNum.setText("Reference No.");
        jPanelGen.add(jLabelNum);
        jLabelNum.setBounds(1000, 80, 100, 30);

        jLabelRegNum.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelRegNum.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelRegNum);
        jLabelRegNum.setBounds(1180, 80, 50, 25);

        jLabelRegYear.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelRegYear.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelRegYear);
        jLabelRegYear.setBounds(1110, 80, 30, 25);

        jLabelSerialAcq.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelSerialAcq);
        jLabelSerialAcq.setBounds(1150, 80, 20, 25);
        jPanelGen.add(jLabelEmp);
        jLabelEmp.setBounds(1110, 60, 50, 20);
        jPanelGen.add(jLabelAcqEmpNum);
        jLabelAcqEmpNum.setBounds(150, 140, 70, 30);
        jPanelGen.add(jLabelAcqProvince);
        jLabelAcqProvince.setBounds(120, 200, 370, 30);
        jPanelGen.add(jLabelAcqOffice);
        jLabelAcqOffice.setBounds(670, 200, 360, 30);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPanel3FocusGained(evt);
            }
        });
        jPanel3.setLayout(null);

        jLabelIncidentalSub.setText("Incidental");
        jPanel3.add(jLabelIncidentalSub);
        jLabelIncidentalSub.setBounds(10, 120, 60, 25);

        jLabelIncidentalSubTot.setText("0.00");
        jPanel3.add(jLabelIncidentalSubTot);
        jLabelIncidentalSubTot.setBounds(100, 120, 50, 25);

        jLabelLunchSub.setText("Lunch");
        jPanel3.add(jLabelLunchSub);
        jLabelLunchSub.setBounds(10, 60, 60, 25);

        jLabelDinnerSub.setText("Dinner");
        jPanel3.add(jLabelDinnerSub);
        jLabelDinnerSub.setBounds(10, 90, 60, 25);

        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel8.setLayout(null);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel17.setText("Allowances Totals ");
        jPanel8.add(jLabel17);
        jLabel17.setBounds(10, 10, 120, 20);

        jLabel18.setText("Incidental");
        jPanel8.add(jLabel18);
        jLabel18.setBounds(10, 130, 60, 20);

        jLabel19.setText("Breakfast");
        jPanel8.add(jLabel19);
        jLabel19.setBounds(10, 40, 60, 20);

        jLabel20.setText("Lunch");
        jPanel8.add(jLabel20);
        jLabel20.setBounds(10, 70, 60, 20);

        jLabel21.setText("Dinner");
        jPanel8.add(jLabel21);
        jLabel21.setBounds(10, 100, 60, 20);

        jPanel3.add(jPanel8);
        jPanel8.setBounds(20, 410, 320, 160);

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel22.setText("Allowances ");
        jPanel3.add(jLabel22);
        jLabel22.setBounds(8, 5, 80, 25);

        jLabelBreakFastSub.setText("Breakfast");
        jPanel3.add(jLabelBreakFastSub);
        jLabelBreakFastSub.setBounds(10, 30, 60, 25);

        jLabelAcqBreakFastSubTot.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqBreakFastSubTot.setForeground(new java.awt.Color(0, 204, 0));
        jLabelAcqBreakFastSubTot.setText("0.00");
        jPanel3.add(jLabelAcqBreakFastSubTot);
        jLabelAcqBreakFastSubTot.setBounds(200, 30, 50, 25);

        jLabelLunchSubTot.setText("0.00");
        jPanel3.add(jLabelLunchSubTot);
        jLabelLunchSubTot.setBounds(100, 60, 50, 25);

        jLabelDinnerSubTot.setText("0.00");
        jPanel3.add(jLabelDinnerSubTot);
        jLabelDinnerSubTot.setBounds(100, 90, 50, 25);

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel3.add(jSeparator4);
        jSeparator4.setBounds(170, 20, 5, 120);

        jLabelBreakFastSubTot.setText("0.00");
        jPanel3.add(jLabelBreakFastSubTot);
        jLabelBreakFastSubTot.setBounds(100, 30, 50, 25);

        jLabelAcqLunchSubTot.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqLunchSubTot.setForeground(new java.awt.Color(0, 204, 0));
        jLabelAcqLunchSubTot.setText("0.00");
        jPanel3.add(jLabelAcqLunchSubTot);
        jLabelAcqLunchSubTot.setBounds(200, 60, 50, 25);

        jLabelAcqDinnerSubTot.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqDinnerSubTot.setForeground(new java.awt.Color(0, 204, 0));
        jLabelAcqDinnerSubTot.setText("0.00");
        jPanel3.add(jLabelAcqDinnerSubTot);
        jLabelAcqDinnerSubTot.setBounds(200, 90, 50, 25);

        jLabelAcqIncidentalSubTot.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqIncidentalSubTot.setForeground(new java.awt.Color(0, 204, 0));
        jLabelAcqIncidentalSubTot.setText("0.00");
        jPanel3.add(jLabelAcqIncidentalSubTot);
        jLabelAcqIncidentalSubTot.setBounds(200, 120, 50, 25);

        jLabelAllReq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAllReq.setForeground(new java.awt.Color(0, 102, 0));
        jLabelAllReq.setText("Planned ");
        jPanel3.add(jLabelAllReq);
        jLabelAllReq.setBounds(100, 5, 60, 25);

        jLabelAllAcq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAllAcq.setForeground(new java.awt.Color(0, 204, 0));
        jLabelAllAcq.setText("Actual");
        jPanel3.add(jLabelAllAcq);
        jLabelAllAcq.setBounds(191, 5, 70, 25);

        jPanelGen.add(jPanel3);
        jPanel3.setBounds(20, 380, 280, 150);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setLayout(null);

        jLabelMiscSubTot.setText("0.00");
        jPanel13.add(jLabelMiscSubTot);
        jLabelMiscSubTot.setBounds(110, 30, 50, 25);

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel29.setText("Miscellaneous ");
        jPanel13.add(jLabel29);
        jLabel29.setBounds(8, 5, 90, 25);

        jLabelMscSub.setText("Miscellaneous");
        jPanel13.add(jLabelMscSub);
        jLabelMscSub.setBounds(8, 30, 80, 25);

        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel13.add(jSeparator5);
        jSeparator5.setBounds(170, 20, 5, 120);

        jLabelAcqMiscSubTot.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqMiscSubTot.setForeground(new java.awt.Color(0, 204, 0));
        jLabelAcqMiscSubTot.setText("0.00");
        jPanel13.add(jLabelAcqMiscSubTot);
        jLabelAcqMiscSubTot.setBounds(200, 30, 50, 25);

        jLabelMiscReq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelMiscReq.setForeground(new java.awt.Color(0, 102, 0));
        jLabelMiscReq.setText("Planned");
        jPanel13.add(jLabelMiscReq);
        jLabelMiscReq.setBounds(110, 5, 70, 25);

        jLabelMiscAcq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelMiscAcq.setForeground(new java.awt.Color(0, 204, 0));
        jLabelMiscAcq.setText("Actual");
        jPanel13.add(jLabelMiscAcq);
        jLabelMiscAcq.setBounds(200, 5, 70, 25);

        jPanelGen.add(jPanel13);
        jPanel13.setBounds(360, 380, 290, 150);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(null);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setText("Accomodation");
        jPanel4.add(jLabel11);
        jLabel11.setBounds(10, 5, 90, 20);

        jLabelAccUnprovedSubTot.setText("0.00");
        jPanel4.add(jLabelAccUnprovedSubTot);
        jLabelAccUnprovedSubTot.setBounds(110, 30, 50, 25);

        jLabelAccProvedSub.setText("Proved");
        jPanel4.add(jLabelAccProvedSub);
        jLabelAccProvedSub.setBounds(10, 60, 70, 25);

        jLabelAccProvedSubTot.setText("0.00");
        jPanel4.add(jLabelAccProvedSubTot);
        jLabelAccProvedSubTot.setBounds(110, 60, 50, 25);

        jLabelAccUnprovedSub.setText("Unproved");
        jPanel4.add(jLabelAccUnprovedSub);
        jLabelAccUnprovedSub.setBounds(8, 30, 70, 25);

        jSeparator9.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel4.add(jSeparator9);
        jSeparator9.setBounds(170, 20, 5, 120);

        jLabelAcqAccProvedSubTot.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqAccProvedSubTot.setForeground(new java.awt.Color(0, 204, 0));
        jLabelAcqAccProvedSubTot.setText("0.00");
        jPanel4.add(jLabelAcqAccProvedSubTot);
        jLabelAcqAccProvedSubTot.setBounds(200, 60, 50, 25);

        jLabelAccAcq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAccAcq.setForeground(new java.awt.Color(0, 204, 0));
        jLabelAccAcq.setText("Actual");
        jPanel4.add(jLabelAccAcq);
        jLabelAccAcq.setBounds(200, 5, 60, 25);

        jLabelAccReq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAccReq.setForeground(new java.awt.Color(0, 102, 0));
        jLabelAccReq.setText("Planned");
        jPanel4.add(jLabelAccReq);
        jLabelAccReq.setBounds(110, 5, 50, 25);

        jLabelAcqAccUnprovedSubTot.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqAccUnprovedSubTot.setForeground(new java.awt.Color(0, 204, 0));
        jLabelAcqAccUnprovedSubTot.setText("0.00");
        jPanel4.add(jLabelAcqAccUnprovedSubTot);
        jLabelAcqAccUnprovedSubTot.setBounds(200, 30, 50, 25);

        jPanelGen.add(jPanel4);
        jPanel4.setBounds(700, 380, 300, 150);

        jLabelAppTotPlannedReq.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelAppTotPlannedReq.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAppTotPlannedReq.setText("Total Planned Amount");
        jPanelGen.add(jLabelAppTotPlannedReq);
        jLabelAppTotPlannedReq.setBounds(1020, 470, 180, 30);

        jLabelAcqAppTotPlannedCost.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelAcqAppTotPlannedCost.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAcqAppTotPlannedCost.setText("0");
        jPanelGen.add(jLabelAcqAppTotPlannedCost);
        jLabelAcqAppTotPlannedCost.setBounds(1220, 470, 100, 30);

        jPanel9.setBackground(new java.awt.Color(0, 153, 51));
        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        jPanel9.setForeground(new java.awt.Color(0, 153, 0));
        jPanel9.setLayout(null);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Record Validation");
        jLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel9.add(jLabel4);
        jLabel4.setBounds(520, 10, 190, 25);

        jButtonSave.setBackground(new java.awt.Color(255, 255, 255));
        jButtonSave.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButtonSave.setForeground(new java.awt.Color(0, 153, 0));
        jButtonSave.setText("Submit");
        jButtonSave.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        jPanel9.add(jButtonSave);
        jButtonSave.setBounds(420, 40, 380, 50);

        jPanelGen.add(jPanel9);
        jPanel9.setBounds(20, 560, 1320, 100);

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

        jLabelWkDuration.setText("Duration");
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

        jLabelLogo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelWkOne.add(jLabelLogo1);
        jLabelLogo1.setBounds(10, 10, 220, 100);
        jPanelWkOne.add(jLabelDateFrom);
        jLabelDateFrom.setBounds(200, 160, 100, 30);
        jPanelWkOne.add(jLabelDateTo);
        jLabelDateTo.setBounds(440, 160, 100, 30);

        jLabelHeaderGen1.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen1.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelWkOne.add(jLabelHeaderGen1);
        jLabelHeaderGen1.setBounds(350, 40, 610, 40);

        jTableReqActivities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Branch", "Project Code", "Task Code", "Site to Visit", "Activity", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Line Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableReqActivities.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableReqActivitiesMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(jTableReqActivities);

        jPanelWkOne.add(jScrollPane8);
        jScrollPane8.setBounds(20, 260, 1320, 410);

        jTabbedPane1.addTab("Month Plan - Request", jPanelWkOne);

        jPanelAcquittal.setBackground(new java.awt.Color(235, 185, 235));
        jPanelAcquittal.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jPanelAcquittal.setPreferredSize(new java.awt.Dimension(1280, 677));
        jPanelAcquittal.setLayout(null);

        jLabelLogo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelAcquittal.add(jLabelLogo2);
        jLabelLogo2.setBounds(10, 10, 220, 100);

        jLabelHeaderLine1.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine1.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelAcquittal.add(jLabelHeaderLine1);
        jLabelHeaderLine1.setBounds(350, 40, 610, 40);

        jLabelLineDate1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate1.setText("29 November 2018");
        jPanelAcquittal.add(jLabelLineDate1);
        jLabelLineDate1.setBounds(1080, 0, 110, 30);

        jLabelLineTime1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime1.setText("15:20:30");
        jPanelAcquittal.add(jLabelLineTime1);
        jLabelLineTime1.setBounds(1220, 0, 80, 30);

        jLabellogged1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged1.setText("Logged In");
        jPanelAcquittal.add(jLabellogged1);
        jLabellogged1.setBounds(1080, 40, 80, 30);

        jLabelLineLogNam1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam1.setText("User Name");
        jPanelAcquittal.add(jLabelLineLogNam1);
        jLabelLineLogNam1.setBounds(1160, 40, 190, 30);

        jPanelCaptureDetails.setLayout(null);

        jDateChooserActivityAcq.setDateFormatString("yyyy-MM-dd");
        jPanelCaptureDetails.add(jDateChooserActivityAcq);
        jDateChooserActivityAcq.setBounds(110, 4, 130, 25);

        jLabelActDate.setText("Activity Date");
        jPanelCaptureDetails.add(jLabelActDate);
        jLabelActDate.setBounds(10, 4, 70, 25);
        jPanelCaptureDetails.add(jTextPurpose);
        jTextPurpose.setBounds(10, 210, 260, 25);

        jLabelBreakfast.setText("  $");
        jPanelCaptureDetails.add(jLabelBreakfast);
        jLabelBreakfast.setBounds(180, 330, 20, 20);

        jTextAcqBreakfast.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextAcqBreakfastFocusLost(evt);
            }
        });
        jTextAcqBreakfast.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextAcqBreakfastMouseClicked(evt);
            }
        });
        jTextAcqBreakfast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAcqBreakfastActionPerformed(evt);
            }
        });
        jTextAcqBreakfast.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextAcqBreakfastKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextAcqBreakfastKeyTyped(evt);
            }
        });
        jPanelCaptureDetails.add(jTextAcqBreakfast);
        jTextAcqBreakfast.setBounds(200, 240, 70, 25);

        jTextAcqLunch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextAcqLunchFocusLost(evt);
            }
        });
        jTextAcqLunch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextAcqLunchMouseClicked(evt);
            }
        });
        jTextAcqLunch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextAcqLunchKeyPressed(evt);
            }
        });
        jPanelCaptureDetails.add(jTextAcqLunch);
        jTextAcqLunch.setBounds(200, 270, 70, 25);

        jTextAcqDinner.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextAcqDinnerFocusLost(evt);
            }
        });
        jTextAcqDinner.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextAcqDinnerMouseClicked(evt);
            }
        });
        jTextAcqDinner.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextAcqDinnerKeyPressed(evt);
            }
        });
        jPanelCaptureDetails.add(jTextAcqDinner);
        jTextAcqDinner.setBounds(200, 300, 70, 25);

        jTextMiscAmtAcq.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextMiscAmtAcqFocusLost(evt);
            }
        });
        jTextMiscAmtAcq.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextMiscAmtAcqKeyPressed(evt);
            }
        });
        jPanelCaptureDetails.add(jTextMiscAmtAcq);
        jTextMiscAmtAcq.setBounds(200, 470, 70, 25);

        jLabelMisc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelMisc.setText("Miscellanous Activity & Amount");
        jPanelCaptureDetails.add(jLabelMisc);
        jLabelMisc.setBounds(10, 440, 190, 25);

        jTextMiscActivityAcq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextMiscActivityAcqActionPerformed(evt);
            }
        });
        jPanelCaptureDetails.add(jTextMiscActivityAcq);
        jTextMiscActivityAcq.setBounds(10, 470, 140, 25);

        jLabelSubAcc.setText("    $");
        jPanelCaptureDetails.add(jLabelSubAcc);
        jLabelSubAcc.setBounds(160, 460, 30, 25);

        jTextAcqIncidental.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextAcqIncidentalFocusLost(evt);
            }
        });
        jTextAcqIncidental.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextAcqIncidentalMouseClicked(evt);
            }
        });
        jTextAcqIncidental.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAcqIncidentalActionPerformed(evt);
            }
        });
        jTextAcqIncidental.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextAcqIncidentalKeyPressed(evt);
            }
        });
        jPanelCaptureDetails.add(jTextAcqIncidental);
        jTextAcqIncidental.setBounds(200, 330, 70, 25);

        jButtonAddActivity.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonAddActivity.setText("Add Activity");
        jButtonAddActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActivityActionPerformed(evt);
            }
        });
        jPanelCaptureDetails.add(jButtonAddActivity);
        jButtonAddActivity.setBounds(10, 500, 130, 23);

        jButtonDeleted.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonDeleted.setText("Delete Activity");
        jButtonDeleted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeletedActionPerformed(evt);
            }
        });
        jPanelCaptureDetails.add(jButtonDeleted);
        jButtonDeleted.setBounds(160, 500, 117, 23);

        jLabelActLinePurpose.setText("Purpose of Activity");
        jPanelCaptureDetails.add(jLabelActLinePurpose);
        jLabelActLinePurpose.setBounds(10, 190, 120, 20);

        jCheckBreakfast.setText("  Breakfast");
        jCheckBreakfast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBreakfastActionPerformed(evt);
            }
        });
        jPanelCaptureDetails.add(jCheckBreakfast);
        jCheckBreakfast.setBounds(10, 240, 90, 25);

        jCheckIncidental.setText("  Incidental");
        jCheckIncidental.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckIncidentalActionPerformed(evt);
            }
        });
        jPanelCaptureDetails.add(jCheckIncidental);
        jCheckIncidental.setBounds(10, 330, 130, 25);

        jCheckDinner.setText("   Dinner");
        jCheckDinner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckDinnerActionPerformed(evt);
            }
        });
        jPanelCaptureDetails.add(jCheckDinner);
        jCheckDinner.setBounds(10, 300, 90, 25);

        jLabelBreakfast1.setText("  $");
        jPanelCaptureDetails.add(jLabelBreakfast1);
        jLabelBreakfast1.setBounds(180, 240, 20, 20);

        jLabelBreakfast2.setText("  $");
        jPanelCaptureDetails.add(jLabelBreakfast2);
        jLabelBreakfast2.setBounds(180, 270, 20, 20);

        jLabelBreakfast3.setText("  $");
        jPanelCaptureDetails.add(jLabelBreakfast3);
        jLabelBreakfast3.setBounds(180, 300, 20, 20);

        buttonGroupAcc.add(jRadioAccUnproved);
        jRadioAccUnproved.setText("Unproved Accomodation ");
        jRadioAccUnproved.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jRadioAccUnprovedFocusGained(evt);
            }
        });
        jRadioAccUnproved.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jRadioAccUnprovedMouseClicked(evt);
            }
        });
        jRadioAccUnproved.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioAccUnprovedActionPerformed(evt);
            }
        });
        jPanelCaptureDetails.add(jRadioAccUnproved);
        jRadioAccUnproved.setBounds(10, 360, 160, 25);

        jLabelUnproved.setText("$");
        jPanelCaptureDetails.add(jLabelUnproved);
        jLabelUnproved.setBounds(180, 360, 20, 20);

        jTextAccUnprovedAcq.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextAccUnprovedAcqFocusLost(evt);
            }
        });
        jTextAccUnprovedAcq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAccUnprovedAcqActionPerformed(evt);
            }
        });
        jTextAccUnprovedAcq.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextAccUnprovedAcqKeyPressed(evt);
            }
        });
        jPanelCaptureDetails.add(jTextAccUnprovedAcq);
        jTextAccUnprovedAcq.setBounds(200, 360, 70, 25);

        buttonGroupAcc.add(jRadioAccProved);
        jRadioAccProved.setText("Proved Accomodation");
        jRadioAccProved.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jRadioAccProvedFocusGained(evt);
            }
        });
        jRadioAccProved.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jRadioAccProvedMouseClicked(evt);
            }
        });
        jRadioAccProved.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioAccProvedActionPerformed(evt);
            }
        });
        jPanelCaptureDetails.add(jRadioAccProved);
        jRadioAccProved.setBounds(10, 390, 160, 25);

        jLabelProved.setText("$");
        jPanelCaptureDetails.add(jLabelProved);
        jLabelProved.setBounds(180, 390, 20, 20);

        jTextAccProvedAcq.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextAccProvedAcqFocusLost(evt);
            }
        });
        jTextAccProvedAcq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAccProvedAcqActionPerformed(evt);
            }
        });
        jTextAccProvedAcq.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextAccProvedAcqKeyPressed(evt);
            }
        });
        jPanelCaptureDetails.add(jTextAccProvedAcq);
        jTextAccProvedAcq.setBounds(200, 390, 70, 25);

        jLabelProjectName.setText("Project Name");
        jPanelCaptureDetails.add(jLabelProjectName);
        jLabelProjectName.setBounds(10, 40, 80, 20);

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
        jPanelCaptureDetails.add(jComboProjectName);
        jComboProjectName.setBounds(10, 60, 260, 25);

        jLabelProjectTask.setText("Project Task");
        jPanelCaptureDetails.add(jLabelProjectTask);
        jLabelProjectTask.setBounds(10, 90, 80, 20);

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
        jPanelCaptureDetails.add(jComboProjectTask);
        jComboProjectTask.setBounds(10, 110, 260, 25);

        jLabelDialogWk1Site1.setText("Site");
        jPanelCaptureDetails.add(jLabelDialogWk1Site1);
        jLabelDialogWk1Site1.setBounds(10, 140, 40, 20);

        jTextFieldDialogSite.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldDialogSiteMouseClicked(evt);
            }
        });
        jTextFieldDialogSite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldDialogSiteActionPerformed(evt);
            }
        });
        jTextFieldDialogSite.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldDialogSiteKeyTyped(evt);
            }
        });
        jPanelCaptureDetails.add(jTextFieldDialogSite);
        jTextFieldDialogSite.setBounds(10, 160, 260, 25);

        jCheckLunch.setText("   Lunch");
        jCheckLunch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckLunchActionPerformed(evt);
            }
        });
        jPanelCaptureDetails.add(jCheckLunch);
        jCheckLunch.setBounds(10, 270, 90, 25);

        buttonGroupAcc.add(jRadioNoAcc);
        jRadioNoAcc.setText(" No Accomodation");
        jRadioNoAcc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jRadioNoAccMouseClicked(evt);
            }
        });
        jPanelCaptureDetails.add(jRadioNoAcc);
        jRadioNoAcc.setBounds(10, 420, 160, 21);

        jLabelProved1.setText("$");
        jPanelCaptureDetails.add(jLabelProved1);
        jLabelProved1.setBounds(180, 420, 20, 20);

        jTextNoAcc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextNoAccFocusLost(evt);
            }
        });
        jTextNoAcc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextNoAccActionPerformed(evt);
            }
        });
        jTextNoAcc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextNoAccKeyPressed(evt);
            }
        });
        jPanelCaptureDetails.add(jTextNoAcc);
        jTextNoAcc.setBounds(200, 420, 70, 25);

        jPanelAcquittal.add(jPanelCaptureDetails);
        jPanelCaptureDetails.setBounds(0, 115, 280, 530);

        jToggleButtonNoActivity.setBackground(new java.awt.Color(255, 0, 0));
        jToggleButtonNoActivity.setFont(new java.awt.Font("Verdana", 1, 9)); // NOI18N
        jToggleButtonNoActivity.setForeground(new java.awt.Color(255, 255, 255));
        jToggleButtonNoActivity.setText("No Activity");
        jToggleButtonNoActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonNoActivityActionPerformed(evt);
            }
        });
        jPanelAcquittal.add(jToggleButtonNoActivity);
        jToggleButtonNoActivity.setBounds(870, 85, 220, 25);

        jToggleButtonAllActivities.setBackground(new java.awt.Color(51, 204, 0));
        jToggleButtonAllActivities.setFont(new java.awt.Font("Verdana", 1, 9)); // NOI18N
        jToggleButtonAllActivities.setText("All Activity");
        jToggleButtonAllActivities.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonAllActivitiesActionPerformed(evt);
            }
        });
        jPanelAcquittal.add(jToggleButtonAllActivities);
        jToggleButtonAllActivities.setBounds(1100, 85, 230, 25);

        jLabelAcqWk2.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        jLabelAcqWk2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelAcquittal.add(jLabelAcqWk2);
        jLabelAcqWk2.setBounds(550, 80, 270, 25);

        jTableActivityAcq.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Branch", "Project Code", "Task Code", "Site to Visit", "Activity", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Line Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableActivityAcq.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableActivityAcqMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(jTableActivityAcq);

        jPanelAcquittal.add(jScrollPane7);
        jScrollPane7.setBounds(290, 120, 1060, 520);

        jTabbedPane1.addTab("Perdiem Acquittal", jPanelAcquittal);

        jPanelReportDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelReportDetailsMouseClicked(evt);
            }
        });
        jPanelReportDetails.setLayout(null);

        jLabelNamTravel.setText("Who Travelled (Full Name and Contact Details)");
        jPanelReportDetails.add(jLabelNamTravel);
        jLabelNamTravel.setBounds(0, 0, 310, 30);

        jTextAreaNamTravel.setColumns(20);
        jTextAreaNamTravel.setRows(5);
        jScrollPane5.setViewportView(jTextAreaNamTravel);

        jPanelReportDetails.add(jScrollPane5);
        jScrollPane5.setBounds(0, 30, 1360, 96);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setLayout(null);

        jPanelAttach.setBackground(new java.awt.Color(255, 255, 255));
        jPanelAttach.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelAttach.setLayout(null);

        jLabelAttach.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelAttach.setText("                     Acquittal Attachments");
        jLabelAttach.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelAttach.add(jLabelAttach);
        jLabelAttach.setBounds(5, 5, 290, 25);

        jButtonSelectFile.setFont(new java.awt.Font("Tahoma", 3, 10)); // NOI18N
        jButtonSelectFile.setText("Select File");
        jButtonSelectFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectFileActionPerformed(evt);
            }
        });
        jPanelAttach.add(jButtonSelectFile);
        jButtonSelectFile.setBounds(5, 70, 290, 25);

        jTextAttDocDesc.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextAttDocDesc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocDescActionPerformed(evt);
            }
        });
        jPanelAttach.add(jTextAttDocDesc);
        jTextAttDocDesc.setBounds(90, 40, 205, 25);

        jTextAttDocFilePath.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextAttDocFilePath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocFilePathActionPerformed(evt);
            }
        });
        jPanelAttach.add(jTextAttDocFilePath);
        jTextAttDocFilePath.setBounds(10, 170, 270, 25);

        jTextAttAcqDocName.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextAttAcqDocName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttAcqDocNameActionPerformed(evt);
            }
        });
        jPanelAttach.add(jTextAttAcqDocName);
        jTextAttAcqDocName.setBounds(5, 100, 290, 25);

        jButtonAdd.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonAdd.setText("Add File ");
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });
        jPanelAttach.add(jButtonAdd);
        jButtonAdd.setBounds(30, 135, 100, 25);

        jButtonDelete.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonDelete.setText("Delete File ");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });
        jPanelAttach.add(jButtonDelete);
        jButtonDelete.setBounds(170, 135, 100, 25);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel1.setText("Report Desc.");
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelAttach.add(jLabel1);
        jLabel1.setBounds(5, 40, 80, 25);

        jPanel14.add(jPanelAttach);
        jPanelAttach.setBounds(5, 20, 300, 230);

        jTableAcquittalDocAtt.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Attachment Description", "Attachment File Name", "Attachement File Path"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane10.setViewportView(jTableAcquittalDocAtt);

        jPanel14.add(jScrollPane10);
        jScrollPane10.setBounds(310, 20, 1020, 230);

        jPanelReportDetails.add(jPanel14);
        jPanel14.setBounds(0, 180, 1360, 260);

        javax.swing.GroupLayout jPanelReportLayout = new javax.swing.GroupLayout(jPanelReport);
        jPanelReport.setLayout(jPanelReportLayout);
        jPanelReportLayout.setHorizontalGroup(
            jPanelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelReportDetails, javax.swing.GroupLayout.DEFAULT_SIZE, 1352, Short.MAX_VALUE)
        );
        jPanelReportLayout.setVerticalGroup(
            jPanelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelReportDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPaneAcqAtt.addTab("Activity Report", jPanelReport);

        jPanelAcqELog.setBackground(new java.awt.Color(0, 204, 255));
        jPanelAcqELog.setMinimumSize(new java.awt.Dimension(1270, 671));
        jPanelAcqELog.setLayout(null);

        jPanelAtt4.setBackground(new java.awt.Color(255, 255, 255));
        jPanelAtt4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelAtt4.setLayout(null);

        jLabelDateRange.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelDateRange.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelDateRange.setText("Date Range");
        jLabelDateRange.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelAtt4.add(jLabelDateRange);
        jLabelDateRange.setBounds(10, 130, 130, 25);

        jLabelVehReg.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelVehReg.setText("jLabel2");
        jLabelVehReg.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelAtt4.add(jLabelVehReg);
        jLabelVehReg.setBounds(10, 60, 110, 25);

        jTextVehReg.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextVehReg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextVehRegActionPerformed(evt);
            }
        });
        jPanelAtt4.add(jTextVehReg);
        jTextVehReg.setBounds(140, 60, 80, 25);

        jLabelTripDetails.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelTripDetails.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTripDetails.setText("Trip Details");
        jLabelTripDetails.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelAtt4.add(jLabelTripDetails);
        jLabelTripDetails.setBounds(10, 10, 220, 25);

        jLabelFrom.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelFrom.setForeground(new java.awt.Color(0, 102, 51));
        jLabelFrom.setText("Date From");
        jLabelFrom.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelAtt4.add(jLabelFrom);
        jLabelFrom.setBounds(10, 170, 70, 25);

        jLabelTo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelTo.setForeground(new java.awt.Color(255, 51, 0));
        jLabelTo.setText("Date To");
        jLabelTo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelAtt4.add(jLabelTo);
        jLabelTo.setBounds(10, 220, 70, 25);

        jDateTripFrom.setDateFormatString("d, MMM y");
        jPanelAtt4.add(jDateTripFrom);
        jDateTripFrom.setBounds(100, 170, 130, 26);

        jDateTripTo.setDateFormatString("d, MMM y");
        jPanelAtt4.add(jDateTripTo);
        jDateTripTo.setBounds(100, 220, 130, 26);

        jLabel2.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 153, 153));
        jLabel2.setText("e.g. AAA0847  ");
        jPanelAtt4.add(jLabel2);
        jLabel2.setBounds(10, 85, 130, 20);

        jButtonGetDetails1.setBackground(new java.awt.Color(0, 153, 51));
        jButtonGetDetails1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonGetDetails1.setText("<html> <center> Get Details</center></html>");
        jButtonGetDetails1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGetDetails1ActionPerformed(evt);
            }
        });
        jPanelAtt4.add(jButtonGetDetails1);
        jButtonGetDetails1.setBounds(10, 300, 90, 40);

        jButtonDeleteDetails.setBackground(new java.awt.Color(255, 51, 0));
        jButtonDeleteDetails.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonDeleteDetails.setText("<html> <center> Delete All Details</center></html>");
        jButtonDeleteDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteDetailsActionPerformed(evt);
            }
        });
        jPanelAtt4.add(jButtonDeleteDetails);
        jButtonDeleteDetails.setBounds(120, 300, 110, 40);

        jPanelAcqELog.add(jPanelAtt4);
        jPanelAtt4.setBounds(5, 120, 240, 490);

        jLabelLogo6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelAcqELog.add(jLabelLogo6);
        jLabelLogo6.setBounds(10, 10, 220, 100);

        jLabelHeaderLine3.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine3.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeaderLine3.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelAcqELog.add(jLabelHeaderLine3);
        jLabelHeaderLine3.setBounds(350, 40, 610, 40);

        jLabelLineDate3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate3.setText("29 November 2018");
        jPanelAcqELog.add(jLabelLineDate3);
        jLabelLineDate3.setBounds(1080, 0, 110, 30);

        jLabelLineTime3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime3.setText("15:20:30");
        jPanelAcqELog.add(jLabelLineTime3);
        jLabelLineTime3.setBounds(1220, 0, 80, 30);

        jLabellogged3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged3.setText("Logged In");
        jPanelAcqELog.add(jLabellogged3);
        jLabellogged3.setBounds(1080, 40, 80, 30);

        jLabelLineLogNam8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam8.setText("User Name");
        jPanelAcqELog.add(jLabelLineLogNam8);
        jLabelLineLogNam8.setBounds(1160, 40, 190, 30);

        jLabelAcqWk4.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        jLabelAcqWk4.setForeground(new java.awt.Color(0, 102, 51));
        jPanelAcqELog.add(jLabelAcqWk4);
        jLabelAcqWk4.setBounds(550, 80, 270, 25);

        jTableTripDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Project", "Vehicle Reg. No.", "Driver Emp No.", "Departing From", "Arriving At", "Trip Purpose", "Depart Date", "Depart Time", "Start Millage", "Arrival Date", "Arrival Time", "End Millage", "Distance"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(jTableTripDetails);

        jPanelAcqELog.add(jScrollPane6);
        jScrollPane6.setBounds(250, 120, 1090, 490);

        jTabbedPaneAcqAtt.addTab("Attachment 1", jPanelAcqELog);

        jTabbedPane1.addTab("Report & Attachments", jTabbedPaneAcqAtt);

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

        jMenuEdit.setText("Edit");

        jMenuMonPlanEdit.setText("Per Diem Monthly Plan");
        jMenuMonPlanEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuMonPlanEditActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuMonPlanEdit);

        jMenuFile.add(jMenuEdit);
        jMenuFile.add(jSeparator35);

        jMenuItemSubmit.setText("Submit");
        jMenuItemSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSubmitActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemSubmit);
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

        jMenuItemSupApp.setText("Supervisor Approval");
        jMenuItemSupApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSupAppActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemSupApp);
        jMenuRequest.add(jSeparator7);

        jMenuItemAccMgrRev.setText("Finance Team Approval");
        jMenuItemAccMgrRev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAccMgrRevActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemAccMgrRev);
        jMenuRequest.add(jSeparator8);

        jMenuItemHeadApp.setText("Project HOD Approval");
        jMenuItemHeadApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemHeadAppActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemHeadApp);
        jMenuRequest.add(jSeparator21);

        jMenuItemSchPerDiem.setText("Schedule Generation - Per Diem");
        jMenuItemSchPerDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSchPerDiemActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemSchPerDiem);
        jMenuRequest.add(jSeparator23);

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

        jMenuItemAcqSupApp.setText("Supervisor Approval");
        jMenuItemAcqSupApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqSupAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqSupApp);
        jMenuAcquittal.add(jSeparator31);

        jMenuItemAcqAccApp.setText("Finance Team Approval");
        jMenuItemAcqAccApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqAccAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqAccApp);
        jMenuAcquittal.add(jSeparator37);

        jMenuItemAcqHeadApp.setText("Project HOD Approval");
        jMenuItemAcqHeadApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqHeadAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqHeadApp);
        jMenuAcquittal.add(jSeparator38);

        jMenuItemAcqSchGen.setText("Acquittals Schedule Generation");
        jMenuItemAcqSchGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqSchGenActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqSchGen);
        jMenuAcquittal.add(jSeparator18);

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

        jMenuPlanApproval.setText("Plan Approval");

        jMenuItemPlanSupApproval.setText("Supervisor Approval");
        jMenuItemPlanSupApproval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPlanSupApprovalActionPerformed(evt);
            }
        });
        jMenuPlanApproval.add(jMenuItemPlanSupApproval);
        jMenuPlanApproval.add(jSeparator62);

        jMenuItemPlanFinApproval.setText("Finance Team Approval");
        jMenuItemPlanFinApproval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPlanFinApprovalActionPerformed(evt);
            }
        });
        jMenuPlanApproval.add(jMenuItemPlanFinApproval);
        jMenuPlanApproval.add(jSeparator63);

        jMenuItemPlanHODApproval.setText("Project HOD Approval");
        jMenuItemPlanHODApproval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPlanHODApprovalActionPerformed(evt);
            }
        });
        jMenuPlanApproval.add(jMenuItemPlanHODApproval);

        jMenuMonthlyPlan.add(jMenuPlanApproval);
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
        jMenuReports.setMaximumSize(new java.awt.Dimension(100, 32767));
        jMenuReports.setPreferredSize(new java.awt.Dimension(100, 20));

        jMenuItemReqSubmitted.setText("Staff Perdiem Requests ");
        jMenuItemReqSubmitted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemReqSubmittedActionPerformed(evt);
            }
        });
        jMenuReports.add(jMenuItemReqSubmitted);
        jMenuReports.add(jSeparator22);

        jMenuItemUserCreate.setText("Profile - User Create");
        jMenuItemUserCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemUserCreateActionPerformed(evt);
            }
        });
        jMenuReports.add(jMenuItemUserCreate);
        jMenuReports.add(jSeparator24);

        jMenuItemUserProfUpd.setText("Profile Update");
        jMenuItemUserProfUpd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemUserProfUpdActionPerformed(evt);
            }
        });
        jMenuReports.add(jMenuItemUserProfUpd);

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
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 725, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1376, 760));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonOkFacilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkFacilityActionPerformed
        jTextFieldDialogSite.setText(jComboFacility.getSelectedItem().toString());
        jDialogFacility.setVisible(false);
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
    }//GEN-LAST:event_jComboDistrictFacilityMouseEntered

    private void jComboDistrictFacilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboDistrictFacilityActionPerformed

    }//GEN-LAST:event_jComboDistrictFacilityActionPerformed

    private void jComboFacilityFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboFacilityFocusGained

    }//GEN-LAST:event_jComboFacilityFocusGained

    private void jComboFacilityMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboFacilityMouseEntered
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

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            int itemCount = jComboBudgetCode.getItemCount();

            for (int i = 0; i < itemCount; i++) {
                jComboBudgetCode.removeItemAt(0);
            }
            jComboBudgetCode.setSelectedIndex(-1);
            String offnam = jLabelAcqOffice.getText();
            String provnam = jLabelAcqProvince.getText();
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

    private void jLabelAcqActMainPurposeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jLabelAcqActMainPurposeFocusGained

    }//GEN-LAST:event_jLabelAcqActMainPurposeFocusGained

    private void jLabelAcqActMainPurposeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jLabelAcqActMainPurposeActionPerformed

    }//GEN-LAST:event_jLabelAcqActMainPurposeActionPerformed

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

    private void jMenuItem3WkPlanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3WkPlanActionPerformed
        new JFrameMnthPlanPerDiemCreate(jLabelEmp.getText()).setVisible(true);
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

    private void jMenuPlanApprovalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuPlanApprovalActionPerformed

    }//GEN-LAST:event_jMenuPlanApprovalActionPerformed

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
        jLabelAcqActMainPurpose.requestFocusInWindow();
        jLabelAcqActMainPurpose.setFocusable(true);

    }//GEN-LAST:event_jButtonSalAckCancelActionPerformed

    private void jCheckBoxSalSubmitAgreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxSalSubmitAgreeActionPerformed

    }//GEN-LAST:event_jCheckBoxSalSubmitAgreeActionPerformed

    private void jButtonAckSubmitContActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAckSubmitContActionPerformed
        if (!(jCheckBoxSalSubmitAgree.isSelected())) {
            JOptionPane.showMessageDialog(jDialogAckSubmit, "Please click the check box to agree with the condition.",
                    "Check box blank", JOptionPane.WARNING_MESSAGE);
        } else {
            jDialogAckSubmit.setVisible(false);
            jDialogAckSalDeduct.setVisible(true);
        }
    }//GEN-LAST:event_jButtonAckSubmitContActionPerformed

    private void jButtonSalSubmitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalSubmitCancelActionPerformed
        jDialogAckSubmit.setVisible(false);
        JOptionPane.showMessageDialog(jDialogAckSalDeduct, "<html>Please note that by <b>not agreeing "
                + "to the terms and conditions</b> your perdiem request is not regestered.</html>");
        jLabelAcqActMainPurpose.requestFocusInWindow();
        jLabelAcqActMainPurpose.setFocusable(true);
    }//GEN-LAST:event_jButtonSalSubmitCancelActionPerformed

    private void jMenuItemSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSubmitActionPerformed

        if (modelAcq.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Please complete the acquittal activity tab.");
            jTextAreaNamTravel.requestFocusInWindow();
            jTextAreaNamTravel.setFocusable(true);
        } else if (jTextAreaNamTravel.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "Please record names of people who travelled");
            jTextAreaNamTravel.requestFocusInWindow();
            jTextAreaNamTravel.setFocusable(true);
        } else {
            checkDupDate();
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

    private void jButtonCancelPayTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelPayTypeActionPerformed
        jDialogPayType.dispose();
    }//GEN-LAST:event_jButtonCancelPayTypeActionPerformed

    private void jRadioMukuruMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioMukuruMouseClicked
        jTextAccNum.setEditable(true);
        jLabelPhoneDet.setVisible(true);
        jTextAccNum.setText("");
    }//GEN-LAST:event_jRadioMukuruMouseClicked

    private void jTextAccNumKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAccNumKeyPressed
        int key = evt.getKeyCode();
        if (jRadioMukuru.isSelected() && (!((key >= evt.VK_0 && key <= evt.VK_9) || (key >= evt.VK_NUMPAD0 && key <= evt.VK_NUMPAD9)))) {
            JOptionPane.showMessageDialog(this, "Field allows only digits.");
            jTextAccNum.setText("");
            jTextAccNum.requestFocusInWindow();
            jTextAccNum.setFocusable(true);
        }
    }//GEN-LAST:event_jTextAccNumKeyPressed

    private void jRadioBankMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioBankMouseClicked
        jTextAccNum.setEditable(false);
        jTextAccNum.setText("");
        findUser();
        jTextAccNum.setText(EmpAccNum);
        jTextBankNam.setText(EmpBankNam);
        jLabelPhoneDet.setVisible(false);
    }//GEN-LAST:event_jRadioBankMouseClicked

    private void jTextAccDet1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAccDet1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAccDet1KeyPressed

    private void jTextBankNamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextBankNamKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextBankNamKeyPressed

    private void jButtonOKPayTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOKPayTypeActionPerformed
        if (jRadioBank.isSelected()) {
            jLabelAcqBankName.setText(jTextBankNam.getText());

        } else {
            jLabelAcqBankName.setText("Mukuru");
        }
        jLabelAcqAccNum.setText(jTextAccNum.getText());
        jDialogPayType.setVisible(false);
    }//GEN-LAST:event_jButtonOKPayTypeActionPerformed

    private void jLabelAcqBankNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelAcqBankNameMouseClicked
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            jDialogPayType.setVisible(true);
        }
    }//GEN-LAST:event_jLabelAcqBankNameMouseClicked

    private void jTextAcqBreakfastFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextAcqBreakfastFocusLost
        //        if (jTextFieldBreakfast.getText().trim().length() == 0) {
        //            jTextFieldBreakfast.setText("0.00");
        //        }
    }//GEN-LAST:event_jTextAcqBreakfastFocusLost

    private void jTextAcqBreakfastMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextAcqBreakfastMouseClicked
        //        jTextFieldBreakfast.setText("");
    }//GEN-LAST:event_jTextAcqBreakfastMouseClicked

    private void jTextAcqBreakfastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAcqBreakfastActionPerformed

    }//GEN-LAST:event_jTextAcqBreakfastActionPerformed

    private void jTextAcqBreakfastKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAcqBreakfastKeyPressed
        //        int key = evt.getKeyCode();
        //        if ((key >= evt.VK_0 && key <= evt.VK_9) || (key >= evt.VK_NUMPAD0 && key <= evt.VK_NUMPAD9)
        //            || key == KeyEvent.VK_BACK_SPACE || key == KeyEvent.VK_DECIMAL) {
        //            jTextAcqBreakfast.setEditable(true);
        //
        //        } else {
        //            jTextAcqBreakfast.setEditable(false);
        //
        //        }
    }//GEN-LAST:event_jTextAcqBreakfastKeyPressed

    private void jTextAcqBreakfastKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAcqBreakfastKeyTyped
        //       char c = evt.getKeyChar();
        //        if (!(Character.isDigit(c)) || (c == KeyEvent.VK_BACK_SPACE)
        //                || (c == KeyEvent.VK_DELETE) ||  !(c == KeyEvent.VK_PERIOD)) {
        //            getToolkit().beep();
        //            evt.consume();
        //        }
    }//GEN-LAST:event_jTextAcqBreakfastKeyTyped

    private void jTextAcqLunchFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextAcqLunchFocusLost
        //        if (jTextFieldLunch.getText().trim().length() == 0) {
        //            jTextFieldLunch.setText("0.00");
        //        }
    }//GEN-LAST:event_jTextAcqLunchFocusLost

    private void jTextAcqLunchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextAcqLunchMouseClicked
        //        jTextFieldLunch.setText("");
    }//GEN-LAST:event_jTextAcqLunchMouseClicked

    private void jTextAcqLunchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAcqLunchKeyPressed
        int key = evt.getKeyCode();
        if ((key >= evt.VK_0 && key <= evt.VK_9) || (key >= evt.VK_NUMPAD0 && key <= evt.VK_NUMPAD9)
                || key == KeyEvent.VK_BACK_SPACE || key == KeyEvent.VK_DECIMAL) {
            jTextAcqLunch.setEditable(true);

        } else {
            jTextAcqLunch.setEditable(false);

        }
    }//GEN-LAST:event_jTextAcqLunchKeyPressed

    private void jTextAcqDinnerFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextAcqDinnerFocusLost
        //        if (jTextFieldDinner.getText().trim().length() == 0) {
        //            jTextFieldDinner.setText("0.00");
        //        }
    }//GEN-LAST:event_jTextAcqDinnerFocusLost

    private void jTextAcqDinnerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextAcqDinnerMouseClicked
        //        jTextFieldDinner.setText("");
    }//GEN-LAST:event_jTextAcqDinnerMouseClicked

    private void jTextAcqDinnerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAcqDinnerKeyPressed
        int key = evt.getKeyCode();
        if ((key >= evt.VK_0 && key <= evt.VK_9) || (key >= evt.VK_NUMPAD0 && key <= evt.VK_NUMPAD9)
                || key == KeyEvent.VK_BACK_SPACE || key == KeyEvent.VK_DECIMAL) {
            jTextAcqDinner.setEditable(true);

        } else {
            jTextAcqDinner.setEditable(false);

        }
    }//GEN-LAST:event_jTextAcqDinnerKeyPressed

    private void jTextMiscAmtAcqFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextMiscAmtAcqFocusLost
        //        if (jTextFieldMiscAmt.getText().trim().length() == 0) {
        //            jTextFieldMiscAmt.setText("0.00");
        //        }
    }//GEN-LAST:event_jTextMiscAmtAcqFocusLost

    private void jTextMiscAmtAcqKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextMiscAmtAcqKeyPressed
        int key = evt.getKeyCode();
        if ((key >= evt.VK_0 && key <= evt.VK_9) || (key >= evt.VK_NUMPAD0 && key <= evt.VK_NUMPAD9)
                || key == KeyEvent.VK_BACK_SPACE || key == KeyEvent.VK_DECIMAL) {
            jTextMiscAmtAcq.setEditable(true);

        } else {
            jTextMiscAmtAcq.setEditable(false);

        }
    }//GEN-LAST:event_jTextMiscAmtAcqKeyPressed

    private void jTextMiscActivityAcqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextMiscActivityAcqActionPerformed

    }//GEN-LAST:event_jTextMiscActivityAcqActionPerformed

    private void jTextAcqIncidentalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextAcqIncidentalFocusLost
        //        if (jTextFieldIncidental.getText().trim().length() == 0) {
        //            jTextFieldIncidental.setText("0.00");
        //        }
    }//GEN-LAST:event_jTextAcqIncidentalFocusLost

    private void jTextAcqIncidentalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextAcqIncidentalMouseClicked
        //        jTextFieldIncidental.setText("");
    }//GEN-LAST:event_jTextAcqIncidentalMouseClicked

    private void jTextAcqIncidentalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAcqIncidentalActionPerformed

    }//GEN-LAST:event_jTextAcqIncidentalActionPerformed

    private void jTextAcqIncidentalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAcqIncidentalKeyPressed
        int key = evt.getKeyCode();
        if ((key >= evt.VK_0 && key <= evt.VK_9) || (key >= evt.VK_NUMPAD0 && key <= evt.VK_NUMPAD9)
                || key == KeyEvent.VK_BACK_SPACE || key == KeyEvent.VK_DECIMAL) {
            jTextAcqIncidental.setEditable(true);

        } else {
            jTextAcqIncidental.setEditable(false);

        }
    }//GEN-LAST:event_jTextAcqIncidentalKeyPressed

    private void jButtonAddActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActivityActionPerformed
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        DefaultTableModel model = (DefaultTableModel) jTableActivityAcq.getModel();
        int rowCount = jTableActivityAcq.getRowCount();

        if (jDateChooserActivityAcq.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
            jDateChooserActivityAcq.requestFocusInWindow();
            jDateChooserActivityAcq.setFocusable(true);
        } else if (formatter.format(jDateChooserActivityAcq.getDate()).compareTo(lowDate) < 0) {
            JOptionPane.showMessageDialog(this, "Activity date cannot be lower than the request lowest date. Please check your dates");
            jDateChooserActivityAcq.requestFocusInWindow();
            jDateChooserActivityAcq.setFocusable(true);
        } else if (jTextFieldDialogSite.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "Please check day district");
            jTextFieldDialogSite.requestFocusInWindow();
            jTextFieldDialogSite.setFocusable(true);
        } else if (jTextPurpose.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "Please check day activity field");
            jTextPurpose.requestFocusInWindow();
            jTextPurpose.setFocusable(true);
        } else if ((jTextMiscActivityAcq.getText().trim().length() > 0)
                && (jTextMiscAmtAcq.getText().trim().length() == 0)) {
            JOptionPane.showMessageDialog(this, "Please misc amt detials");
            jTextMiscAmtAcq.requestFocusInWindow();
            jTextMiscAmtAcq.setFocusable(true);
        }
        if (!jRadioAccUnproved.isSelected() && !jRadioAccProved.isSelected() && !jRadioNoAcc.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please select accomodation type.");
            jTextAccUnprovedAcq.requestFocusInWindow();
            jTextAccUnprovedAcq.setFocusable(true);
        } else {
            //jTableActivityAcq.setEnabled(true);
            java.util.Date jDateActivity = jDateChooserActivityAcq.getDate();
//            jLabelAcqPayBack.setText("");
//            jLabelAcqAmtPayBack.setText("");
//            jLabelBankAccNo.setText("");
//            jTextPaidAmt.setText("0.00");
            String dateDayActivity = formatter.format(jDateActivity);
            if (jTextAcqBreakfast.getText().trim().length() == 0) {
                jTextAcqBreakfast.setText("0.00");
            }
            if (jTextAcqLunch.getText().trim().length() == 0) {
                jTextAcqLunch.setText("0.00");
            }
            if (jTextAcqDinner.getText().trim().length() == 0) {
                jTextAcqDinner.setText("0.00");
            }
            if (jTextAcqIncidental.getText().trim().length() == 0) {
                jTextAcqIncidental.setText("0.00");
            }
            if (jTextMiscAmtAcq.getText().trim().length() == 0) {
                jTextMiscAmtAcq.setText("0.00");
            }
            if (jTextAccProvedAcq.getText().trim().length() == 0) {
                jTextAccProvedAcq.setText("0.00");
            }
            if (jTextAccUnprovedAcq.getText().trim().length() == 0) {
                jTextAccUnprovedAcq.setText("0.00");
            }

            if (jRadioAccUnproved.isSelected()) {
                accomodationUnProven = jTextAccUnprovedAcq.getText();
                accomodation = accomodationUnProven;
            }

            if (jRadioAccProved.isSelected()) {
                accomodationProven = jTextAccProvedAcq.getText();
                accomodation = accomodationProven;
            }

            if (jRadioNoAcc.isSelected()) {
                accomodationUnProven = jTextNoAcc.getText();
                accomodationProven = jTextNoAcc.getText();
                accomodation = "0.00";
            }
            model.addRow(new Object[]{dateDayActivity, branchCode, prjCode, taskCode,
                jTextFieldDialogSite.getText(), jTextPurpose.getText(), jTextAcqBreakfast.getText(), jTextAcqLunch.getText(),
                jTextAcqDinner.getText(), jTextAcqIncidental.getText(),
                jTextMiscActivityAcq.getText(), jTextMiscAmtAcq.getText(), jTextAccUnprovedAcq.getText(), jTextAccProvedAcq.getText(),
                Double.toString((Double.parseDouble(jTextAcqBreakfast.getText()) + Double.parseDouble(jTextAcqLunch.getText())
                + Double.parseDouble(jTextAcqDinner.getText()) + Double.parseDouble(jTextAcqIncidental.getText())
                + Double.parseDouble(jTextMiscAmtAcq.getText()) + Double.parseDouble(accomodation)))});

            accomodation = "0.00";
            calTotAcq();
            mainPageTotAcq();
//    mainPageTotUpdateAcq();
//    /**
//    * **** updating general segment
//    */
//
            jDateChooserActivityAcq.setDate(null);

            jTextPurpose.setText("");
            jTextAcqBreakfast.setText("0.00");
            jTextAcqLunch.setText("0.00");
            jTextAcqDinner.setText("0.00");
            jTextAcqIncidental.setText("0.00");
            jTextMiscAmtAcq.setText("0.00");
            jTextAccUnprovedAcq.setText("0.00");
            jTextAccProvedAcq.setText("0.00");
            jTextMiscActivityAcq.setText("");
            jTextFieldDialogSite.setText("");
//
//    //            buttonGroupPayRec.clearSelection();
            jCheckBreakfast.setSelected(false);
            jCheckDinner.setSelected(false);
            jCheckLunch.setSelected(false);

            jCheckDinner.setSelected(false);
            jCheckIncidental.setSelected(false);

            jRadioAccUnproved.setSelected(true);
//
        }
    }//GEN-LAST:event_jButtonAddActivityActionPerformed

    private void jButtonDeletedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeletedActionPerformed
        DefaultTableModel model = (DefaultTableModel) this.jTableActivityAcq.getModel();

        int[] rows = jTableActivityAcq.getSelectedRows();

        if (rows.length == 0) {
            JOptionPane.showMessageDialog(this, "No activity to delete. Please check and try again.");
        } else {
            int selectedOption = JOptionPane.showConfirmDialog(null,
                    "Do you want to delete selected activity line?",
                    "Choose",
                    JOptionPane.YES_NO_OPTION);
            if (selectedOption == JOptionPane.YES_OPTION) {

                for (int i = 0; i < rows.length; i++) {
                    model.removeRow(rows[i] - i);
                }
            } else {
                jButtonDeleted.requestFocusInWindow();
                jButtonDeleted.setFocusable(true);
            }
        }
        //mainPageTotUpdate();
//        mainPageTotUpdateAcq();
        calTotAcq();
        mainPageTotAcq();
    }//GEN-LAST:event_jButtonDeletedActionPerformed

    private void jCheckBreakfastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBreakfastActionPerformed
        if (jCheckBreakfast.isSelected()) {
            jTextAcqBreakfast.setText(breakfastAll);
        } else {
            jTextAcqBreakfast.setText("0.00");
        }
    }//GEN-LAST:event_jCheckBreakfastActionPerformed

    private void jCheckIncidentalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckIncidentalActionPerformed
        if (jCheckIncidental.isSelected()) {
            jTextAcqIncidental.setText(incidentalAll);
        } else {
            jTextAcqIncidental.setText("0.00");
        }
    }//GEN-LAST:event_jCheckIncidentalActionPerformed

    private void jCheckDinnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckDinnerActionPerformed
        if (jCheckDinner.isSelected()) {
            jTextAcqDinner.setText(dinnerAll);
        } else {
            jTextAcqDinner.setText("0.00");
        }
    }//GEN-LAST:event_jCheckDinnerActionPerformed

    private void jRadioAccUnprovedFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jRadioAccUnprovedFocusGained

    }//GEN-LAST:event_jRadioAccUnprovedFocusGained

    private void jRadioAccUnprovedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioAccUnprovedMouseClicked
        jTextAccProvedAcq.setVisible(false);
        jTextAccProvedAcq.setText("0");
        jTextAccUnprovedAcq.setText(unProvedAll);
        jTextAccUnprovedAcq.setVisible(true);
        jTextNoAcc.setText("0");
        jTextNoAcc.setVisible(false);
    }//GEN-LAST:event_jRadioAccUnprovedMouseClicked

    private void jRadioAccUnprovedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioAccUnprovedActionPerformed

    }//GEN-LAST:event_jRadioAccUnprovedActionPerformed

    private void jTextAccUnprovedAcqFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextAccUnprovedAcqFocusLost
        //        if (jTextFieldAccUnproved.getText().trim().length() == 0) {
        //            jTextFieldAccUnproved.setText("0.00");
        //        }
    }//GEN-LAST:event_jTextAccUnprovedAcqFocusLost

    private void jTextAccUnprovedAcqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAccUnprovedAcqActionPerformed

    }//GEN-LAST:event_jTextAccUnprovedAcqActionPerformed

    private void jTextAccUnprovedAcqKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAccUnprovedAcqKeyPressed
        int key = evt.getKeyCode();
        if ((key >= evt.VK_0 && key <= evt.VK_9) || (key >= evt.VK_NUMPAD0 && key <= evt.VK_NUMPAD9)
                || key == KeyEvent.VK_BACK_SPACE || key == KeyEvent.VK_DECIMAL) {
            jTextAccUnprovedAcq.setEditable(true);

        } else {
            jTextAccUnprovedAcq.setEditable(false);

        }
    }//GEN-LAST:event_jTextAccUnprovedAcqKeyPressed

    private void jRadioAccProvedFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jRadioAccProvedFocusGained

    }//GEN-LAST:event_jRadioAccProvedFocusGained

    private void jRadioAccProvedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioAccProvedMouseClicked

        jTextAccProvedAcq.setVisible(true);
        jTextAccProvedAcq.setText(provedAll);
        jTextAccUnprovedAcq.setText("0");
        jTextAccUnprovedAcq.setVisible(false);
        jTextNoAcc.setText("0");
        jTextNoAcc.setVisible(false);

    }//GEN-LAST:event_jRadioAccProvedMouseClicked

    private void jRadioAccProvedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioAccProvedActionPerformed

    }//GEN-LAST:event_jRadioAccProvedActionPerformed

    private void jTextAccProvedAcqFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextAccProvedAcqFocusLost
        //        if ( jTextFieldAccProved.getText().trim().length() == 0) {
        //            jTextFieldAccProved.setText("0.00");
        //        }
    }//GEN-LAST:event_jTextAccProvedAcqFocusLost

    private void jTextAccProvedAcqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAccProvedAcqActionPerformed

    }//GEN-LAST:event_jTextAccProvedAcqActionPerformed

    private void jTextAccProvedAcqKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAccProvedAcqKeyPressed
        int key = evt.getKeyCode();
        if ((key >= evt.VK_0 && key <= evt.VK_9) || (key >= evt.VK_NUMPAD0 && key <= evt.VK_NUMPAD9)
                || key == KeyEvent.VK_BACK_SPACE || key == KeyEvent.VK_DECIMAL) {
            jTextAccProvedAcq.setEditable(true);

        } else {
            jTextAccProvedAcq.setEditable(false);

        }
    }//GEN-LAST:event_jTextAccProvedAcqKeyPressed

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

    private void jTextFieldDialogSiteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDialogSiteMouseClicked
        jDialogFacility.setVisible(true);
    }//GEN-LAST:event_jTextFieldDialogSiteMouseClicked

    private void jTextFieldDialogSiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDialogSiteActionPerformed
        jDialogFacility.setVisible(true);
    }//GEN-LAST:event_jTextFieldDialogSiteActionPerformed

    private void jTextFieldDialogSiteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDialogSiteKeyTyped
        jDialogFacility.setVisible(true);
    }//GEN-LAST:event_jTextFieldDialogSiteKeyTyped

    private void jCheckLunchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckLunchActionPerformed
        if (jCheckLunch.isSelected()) {
            jTextAcqLunch.setText(lunchAll);
        } else {
            jTextAcqLunch.setText("0.00");
        }
    }//GEN-LAST:event_jCheckLunchActionPerformed

    private void jRadioNoAccMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioNoAccMouseClicked
        jTextAccProvedAcq.setVisible(false);
        jTextAccProvedAcq.setText("0");
        jTextAccUnprovedAcq.setText("0");
        jTextAccUnprovedAcq.setVisible(false);
        jTextNoAcc.setText("0");
        jTextNoAcc.setVisible(true);
    }//GEN-LAST:event_jRadioNoAccMouseClicked

    private void jTextNoAccFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextNoAccFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextNoAccFocusLost

    private void jTextNoAccActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextNoAccActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextNoAccActionPerformed

    private void jTextNoAccKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextNoAccKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextNoAccKeyPressed

    private void jToggleButtonNoActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonNoActivityActionPerformed

        if ((jToggleButtonNoActivity.isSelected()) && ("All Activities Not Done".equals(jToggleButtonNoActivity.getText()))) {
            DefaultTableModel dmAcq = (DefaultTableModel) jTableActivityAcq.getModel();
            while (dmAcq.getRowCount() > 0) {
                dmAcq.removeRow(0);
            }
            fillDataNoAct();
            calTotAcq();
            mainPageTotAcq();
//            mainPageTotUpdateAcq();
//
//            if ((("Total (Change)").equals(jLabelAppTotReq.getText())) && (jRadioButtonPayRecYes.isSelected())) {
//
//                jLabelBankAccNo.setText("");
//                jTextPaidAmt.setText("0.00");
//                jDialogChgPaid.setVisible(true);
//
//            }
        } else if ((jToggleButtonNoActivity.isSelected()) && ("Delete Activities (Not Done)".equals(jToggleButtonNoActivity.getText()))) {
            DefaultTableModel dmAcq = (DefaultTableModel) jTableActivityAcq.getModel();
            while (dmAcq.getRowCount() > 0) {
                dmAcq.removeRow(0);
            }
            calTotAcq();
            mainPageTotAcq();
            jToggleButtonNoActivity.setText("All Activities Not Done");
            jToggleButtonAllActivities.setText("All Activities Done As Per Request");
        }
    }//GEN-LAST:event_jToggleButtonNoActivityActionPerformed

    private void jToggleButtonAllActivitiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonAllActivitiesActionPerformed

        if ("All Activities Done As Per Request".equals(jToggleButtonAllActivities.getText())) {
            DefaultTableModel dmAcq = (DefaultTableModel) jTableActivityAcq.getModel();
            while (dmAcq.getRowCount() > 0) {
                dmAcq.removeRow(0);
            }
//            jLabelAcqPayBack.setText("");
//            jLabelAcqAmtPayBack.setText("");
//            jLabelBankAccNo.setText("");
//            jTextPaidAmt.setText("0.00");
            fetchAcqItmData();
            //jTableActivityAcq.setEnabled(false);
            calTotAcq();
            mainPageTotAcq();
            jToggleButtonAllActivities.setText("Delete All Requested Activities");
            jToggleButtonNoActivity.setText("All Activities Not Done");
        } else if ("Delete All Requested Activities".equals(jToggleButtonAllActivities.getText())) {
            DefaultTableModel dmAcq = (DefaultTableModel) jTableActivityAcq.getModel();
            while (dmAcq.getRowCount() > 0) {
                dmAcq.removeRow(0);
            }

            calTotAcq();
            mainPageTotAcq();
//            if (minWkNum == 1) {
//                jLabelAcqAppWk1TotReqCost.setText("0.00");
//                jLabelAcqAppTotReqCost.setText(String.format("%.2f", allTotalAcq));
//            } else if (minWkNum == 2) {
//                jLabelAcqAppWk2TotReqCost.setText("0.00");
//                double cleredAmtBal = allActTot - Double.parseDouble(jLabelAcqAppWk1TotReqCost.getText());
//                jLabelAcqAppTotReqCost.setText(String.format("%.2f", cleredAmtBal));
//            } else if (minWkNum == 3) {
//                jLabelAcqAppWk3TotReqCost.setText("0.00");
//                double cleredAmtBal = allActTot - Double.parseDouble(jLabelAcqAppWk1TotReqCost.getText())
//                - Double.parseDouble(jLabelAcqAppWk2TotReqCost.getText());
//                jLabelAcqAppTotReqCost.setText(String.format("%.2f", cleredAmtBal));
//            } else if (minWkNum == 4) {
//                jLabelAcqAppWk4TotReqCost.setText("0.00");
//                double cleredAmtBal = allActTot - Double.parseDouble(jLabelAcqAppWk1TotReqCost.getText())
//                - Double.parseDouble(jLabelAcqAppWk2TotReqCost.getText())
//                - Double.parseDouble(jLabelAcqAppWk3TotReqCost.getText());
//                jLabelAcqAppTotReqCost.setText(String.format("%.2f", cleredAmtBal));
//            } else if (minWkNum == 5) {
//                jLabelAcqAppWk5TotReqCost.setText("0.00");
//                double cleredAmtBal = allActTot - Double.parseDouble(jLabelAcqAppWk1TotReqCost.getText())
//                - Double.parseDouble(jLabelAcqAppWk2TotReqCost.getText())
//                - Double.parseDouble(jLabelAcqAppWk3TotReqCost.getText())
//                - Double.parseDouble(jLabelAcqAppWk4TotReqCost.getText());
//                jLabelAcqAppTotReqCost.setText(String.format("%.2f", cleredAmtBal));
//            }
//            jLabelAppTotReq.setText("Total (Cash On hand)");
            jToggleButtonAllActivities.setText("All Activities Done As Per Request");
            jToggleButtonNoActivity.setText("All Activities Not Done");
        }
    }//GEN-LAST:event_jToggleButtonAllActivitiesActionPerformed

    private void jTableActivityAcqMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableActivityAcqMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableActivityAcqMouseClicked

    private void jTableReqActivitiesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableReqActivitiesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableReqActivitiesMouseClicked

    private void jPanel3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel3FocusGained

    }//GEN-LAST:event_jPanel3FocusGained

    private void jButtonDeleteDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteDetailsActionPerformed
        //        deleteFileAtt();
    }//GEN-LAST:event_jButtonDeleteDetailsActionPerformed

    private void jButtonGetDetails1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGetDetails1ActionPerformed
        //        try {
        //            str = jTextVehReg.getText();
        //            str = str.replaceAll("\\s", "");
        //            findVehicle();
        //
        //            if (jTextVehReg.getText().trim().length() == 0 || jDateTripFrom.getDate() == null || jDateTripTo.getDate() == null) {
        //                JOptionPane.showMessageDialog(this, "Please complete all feilds before proceeding");
        //                jTextVehReg.requestFocusInWindow();
        //                jTextVehReg.setFocusable(true);
        //            } else if (((dfDate.format(jDateTripTo.getDate())).compareTo(dfDate.format(jDateTripFrom.getDate())) < 0)) {
        //                JOptionPane.showMessageDialog(this, "End Date cannot be lower that start date.");
        //                jTextVehReg.requestFocusInWindow();
        //                jTextVehReg.setFocusable(true);
        //            } else if (vehCount == 0) {
        //                JOptionPane.showMessageDialog(this, "Vehicle registration number does not exist.");
        //                jTextVehReg.requestFocusInWindow();
        //                jTextVehReg.setFocusable(true);
        //            } else if (jLabelAcqActMainPurpose.getText().trim().length() == 0) {
        //                JOptionPane.showMessageDialog(this, "Please retreive per diem details before getting vehicle mileage");
        //                jTextVehReg.requestFocusInWindow();
        //                jTextVehReg.setFocusable(true);
        //            } else if (!(modelTrip.getRowCount() == 0)) {
        //                int selectedOption = JOptionPane.showConfirmDialog(null,
        //                    "Do you want to add trips to the current list of trips?",
        //                    "Choose",
        //                    JOptionPane.YES_NO_OPTION);
        //                if (selectedOption == JOptionPane.YES_OPTION) {
        //                    addTrips();
        //                } else {
        //                    clearTable();
        //                    addTrips();
        //                }
        //            } else {
        //                addTrips();
        //            }
        //        } catch (Exception e) {
        //            System.out.println(e);
        //        }
    }//GEN-LAST:event_jButtonGetDetails1ActionPerformed

    private void jTextVehRegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextVehRegActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextVehRegActionPerformed

    private void jPanelReportDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelReportDetailsMouseClicked

    }//GEN-LAST:event_jPanelReportDetailsMouseClicked

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

    private void jMenuItemSchPerDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSchPerDiemActionPerformed
        new JFrameReqHQPayScheduleApp(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemSchPerDiemActionPerformed

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

    private void jMenuItemAcqSchGenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAcqSchGenActionPerformed

        new JFrameReqHQAcqScheduleApp(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemAcqSchGenActionPerformed

    private void jMenuItemAcqViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAcqViewActionPerformed
        new JFrameAppAcquittalView(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemAcqViewActionPerformed

    private void jMenuItemPlanSupApprovalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPlanSupApprovalActionPerformed
        new JFrameMnthSupList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemPlanSupApprovalActionPerformed

    private void jMenuItemPlanFinApprovalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPlanFinApprovalActionPerformed
        new JFrameMnthFinanceList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemPlanFinApprovalActionPerformed

    private void jMenuItemPlanHODApprovalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPlanHODApprovalActionPerformed
        new JFrameMnthHODList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemPlanHODApprovalActionPerformed

    private void jMenuItemReqGenLstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReqGenLstActionPerformed
        new JFrameMnthReqGenList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemReqGenLstActionPerformed

    private void jMenuItemPlanViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPlanViewActionPerformed
        new JFrameMnthViewList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemPlanViewActionPerformed

    private void jMenuItemReqSubmittedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReqSubmittedActionPerformed
        new JFrameReqAllUserList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemReqSubmittedActionPerformed

    private void jMenuItemUserProfUpdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemUserProfUpdActionPerformed
        new JFrameUserProfileUpdate(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemUserProfUpdActionPerformed

    private void jMenuItemUserCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemUserCreateActionPerformed
        new JFrameFixedUserCreation(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemUserCreateActionPerformed

    private void jMenuMonPlanEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuMonPlanEditActionPerformed
        new JFrameMnthPlanPerDiemEdit(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuMonPlanEditActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        try {

            if (modelAcq.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Please complete the acquittal activity tab.");
                jTextAreaNamTravel.requestFocusInWindow();
                jTextAreaNamTravel.setFocusable(true);
            } else if (jTextAreaNamTravel.getText().trim().length() == 0) {
                JOptionPane.showMessageDialog(this, "Please record names of people who travelled");
                jTextAreaNamTravel.requestFocusInWindow();
                jTextAreaNamTravel.setFocusable(true);
            } else {
                checkDupDate();
            }

        } catch (Exception e1) {
            System.out.println(e1);
        }
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonSelectFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectFileActionPerformed

        pdfDB.pdfChooser();
        String attFileName = pdfDB.fileName;

        File file = new File(attFileName);
        long attFileLength = file.length();
        if (attFileLength < 512000) {
            jTextAttAcqDocName.setText(file.getName());
            jTextAttDocFilePath.setText(attFileName);
            // jTextAttDocPath.setVisible(false);
            //  jButtonMOHCCLeaveAtt.setText("Remove");
        } else if (attFileLength > 512000) {
            JOptionPane.showMessageDialog(this, "File size cannot be greater than 512Kb. ");
        }
    }//GEN-LAST:event_jButtonSelectFileActionPerformed

    private void jTextAttDocDescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocDescActionPerformed

    }//GEN-LAST:event_jTextAttDocDescActionPerformed

    private void jTextAttDocFilePathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocFilePathActionPerformed

    }//GEN-LAST:event_jTextAttDocFilePathActionPerformed

    private void jTextAttAcqDocNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttAcqDocNameActionPerformed

    }//GEN-LAST:event_jTextAttAcqDocNameActionPerformed

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        if ("".equals(jTextAttDocDesc.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter attached file description.");
            jTextAttDocDesc.requestFocusInWindow();
            jTextAttDocDesc.setFocusable(true);
        } else if ("".equals(jTextAttAcqDocName.getText())) {
            JOptionPane.showMessageDialog(this, "Please note that no file has been selected.");
            jTextAttAcqDocName.requestFocusInWindow();
            jTextAttAcqDocName.setFocusable(true);
        } else {
            addFileAtt();
            refreshAttFields();

        }
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        deleteFileAttReport();
    }//GEN-LAST:event_jButtonDeleteActionPerformed

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
            java.util.logging.Logger.getLogger(JFrameMnthDistrictAcquittal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthDistrictAcquittal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthDistrictAcquittal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthDistrictAcquittal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrameMnthDistrictAcquittal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupAcc;
    private javax.swing.ButtonGroup buttonGroupPayType;
    private javax.swing.JButton jButtonAckSalCont;
    private javax.swing.JButton jButtonAckSubmitCont;
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonAddActivity;
    private javax.swing.JButton jButtonCancelBudget;
    private javax.swing.JButton jButtonCancelFacility;
    private javax.swing.JButton jButtonCancelPayType;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonDeleteDetails;
    private javax.swing.JButton jButtonDeleted;
    private javax.swing.JButton jButtonGetDetails1;
    private javax.swing.JButton jButtonOKPayType;
    private javax.swing.JButton jButtonOkFacility;
    private javax.swing.JButton jButtonOkFacility1;
    private javax.swing.JButton jButtonSalAckCancel;
    private javax.swing.JButton jButtonSalSubmitCancel;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonSelectFile;
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
    private javax.swing.JCheckBox jCheckBreakfast;
    private javax.swing.JCheckBox jCheckDinner;
    private javax.swing.JCheckBox jCheckIncidental;
    private javax.swing.JCheckBox jCheckLunch;
    private javax.swing.JComboBox<String> jComboBudgetCode;
    private javax.swing.JComboBox<String> jComboDistrictFacility;
    private javax.swing.JComboBox<String> jComboFacility;
    private javax.swing.JComboBox<String> jComboProjectName;
    private javax.swing.JComboBox<String> jComboProjectTask;
    private javax.swing.JComboBox<String> jComboProvinceFacility;
    private com.toedter.calendar.JDateChooser jDateChooserActivityAcq;
    private com.toedter.calendar.JDateChooser jDateTripFrom;
    private com.toedter.calendar.JDateChooser jDateTripTo;
    private javax.swing.JDialog jDialogAckSalDeduct;
    private javax.swing.JDialog jDialogAckSubmit;
    private javax.swing.JDialog jDialogBudget;
    private javax.swing.JDialog jDialogFacility;
    private javax.swing.JDialog jDialogPayType;
    private javax.swing.JDialog jDialogWaiting;
    private javax.swing.JDialog jDialogWaitingEmail;
    private javax.swing.JDialog jDialogWk1;
    private javax.swing.JDialog jDialogWk2;
    private javax.swing.JDialog jDialogWk3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabelACC;
    private javax.swing.JLabel jLabelAccAcq;
    private javax.swing.JLabel jLabelAccProvedSub;
    private javax.swing.JLabel jLabelAccProvedSubTot;
    private javax.swing.JLabel jLabelAccReq;
    private javax.swing.JLabel jLabelAccUnprovedSub;
    private javax.swing.JLabel jLabelAccUnprovedSubTot;
    private javax.swing.JLabel jLabelAcqAccNum;
    private javax.swing.JLabel jLabelAcqAccProvedSubTot;
    private javax.swing.JLabel jLabelAcqAccUnprovedSubTot;
    private javax.swing.JTextField jLabelAcqActMainPurpose;
    private javax.swing.JLabel jLabelAcqAppTotPlannedCost;
    private javax.swing.JLabel jLabelAcqAppTotReqCost;
    private javax.swing.JLabel jLabelAcqBankName;
    private javax.swing.JLabel jLabelAcqBreakFastSubTot;
    private javax.swing.JLabel jLabelAcqDinnerSubTot;
    private javax.swing.JLabel jLabelAcqEmpNam;
    private javax.swing.JLabel jLabelAcqEmpNum;
    private javax.swing.JLabel jLabelAcqEmpTitle;
    private javax.swing.JLabel jLabelAcqIncidentalSubTot;
    private javax.swing.JLabel jLabelAcqLunchSubTot;
    private javax.swing.JLabel jLabelAcqMiscSubTot;
    private javax.swing.JLabel jLabelAcqOffice;
    private javax.swing.JLabel jLabelAcqProvince;
    private javax.swing.JLabel jLabelAcqWk2;
    private javax.swing.JLabel jLabelAcqWk4;
    private javax.swing.JLabel jLabelActDate;
    private javax.swing.JLabel jLabelActLinePurpose;
    private javax.swing.JLabel jLabelActMainPurpose;
    private javax.swing.JLabel jLabelAllAcq;
    private javax.swing.JLabel jLabelAllReq;
    private javax.swing.JLabel jLabelAppTotPlannedReq;
    private javax.swing.JLabel jLabelAppTotReq;
    private javax.swing.JLabel jLabelAttach;
    private javax.swing.JLabel jLabelBank;
    private javax.swing.JLabel jLabelBreakFastSub;
    private javax.swing.JLabel jLabelBreakFastSubTot;
    private javax.swing.JLabel jLabelBreakfast;
    private javax.swing.JLabel jLabelBreakfast1;
    private javax.swing.JLabel jLabelBreakfast2;
    private javax.swing.JLabel jLabelBreakfast3;
    private javax.swing.JLabel jLabelBudgetCode;
    private javax.swing.JLabel jLabelDateFrom;
    private javax.swing.JLabel jLabelDateRange;
    private javax.swing.JLabel jLabelDateTo;
    private javax.swing.JLabel jLabelDialogWk1Dis;
    private javax.swing.JLabel jLabelDialogWk1Dis1;
    private javax.swing.JLabel jLabelDialogWk1Site;
    private javax.swing.JLabel jLabelDialogWk1Site1;
    private javax.swing.JLabel jLabelDialogWk2Dis;
    private javax.swing.JLabel jLabelDialogWk2Dis2;
    private javax.swing.JLabel jLabelDialogWk2Dis3;
    private javax.swing.JLabel jLabelDialogWk2Site;
    private javax.swing.JLabel jLabelDialogWk3Dis;
    private javax.swing.JLabel jLabelDialogWk3Site;
    private javax.swing.JLabel jLabelDinnerSub;
    private javax.swing.JLabel jLabelDinnerSubTot;
    private javax.swing.JLabel jLabelDistrictFacility;
    private javax.swing.JLabel jLabelEmp;
    private javax.swing.JLabel jLabelEmpNumber;
    private javax.swing.JLabel jLabelFacility;
    private javax.swing.JLabel jLabelFinDetails;
    private javax.swing.JLabel jLabelFrom;
    private javax.swing.JLabel jLabelGenDate;
    private javax.swing.JLabel jLabelGenLogNam;
    private javax.swing.JLabel jLabelGenLogNam1;
    private javax.swing.JLabel jLabelGenLogNam6;
    private javax.swing.JLabel jLabelGenTime;
    private javax.swing.JLabel jLabelHeader1;
    private javax.swing.JLabel jLabelHeader2;
    private javax.swing.JLabel jLabelHeaderAck;
    private javax.swing.JLabel jLabelHeaderAck1;
    private javax.swing.JLabel jLabelHeaderGen;
    private javax.swing.JLabel jLabelHeaderGen1;
    private javax.swing.JLabel jLabelHeaderLine1;
    private javax.swing.JLabel jLabelHeaderLine3;
    private javax.swing.JLabel jLabelIncidentalSub;
    private javax.swing.JLabel jLabelIncidentalSubTot;
    private javax.swing.JLabel jLabelLineDate;
    private javax.swing.JLabel jLabelLineDate1;
    private javax.swing.JLabel jLabelLineDate3;
    private javax.swing.JLabel jLabelLineLogNam;
    private javax.swing.JLabel jLabelLineLogNam1;
    private javax.swing.JLabel jLabelLineLogNam8;
    private javax.swing.JLabel jLabelLineTime;
    private javax.swing.JLabel jLabelLineTime1;
    private javax.swing.JLabel jLabelLineTime3;
    private javax.swing.JLabel jLabelLogo;
    private javax.swing.JLabel jLabelLogo1;
    private javax.swing.JLabel jLabelLogo2;
    private javax.swing.JLabel jLabelLogo6;
    private javax.swing.JLabel jLabelLunchSub;
    private javax.swing.JLabel jLabelLunchSubTot;
    private javax.swing.JLabel jLabelMisc;
    private javax.swing.JLabel jLabelMiscAcq;
    private javax.swing.JLabel jLabelMiscReq;
    private javax.swing.JLabel jLabelMiscSubTot;
    private javax.swing.JLabel jLabelMscSub;
    private javax.swing.JLabel jLabelNamTravel;
    private javax.swing.JLabel jLabelNum;
    private javax.swing.JLabel jLabelOffice;
    private javax.swing.JLabel jLabelPayTypeHeader;
    private javax.swing.JLabel jLabelPhoneDet;
    private javax.swing.JLabel jLabelProjectName;
    private javax.swing.JLabel jLabelProjectTask;
    private javax.swing.JLabel jLabelProved;
    private javax.swing.JLabel jLabelProved1;
    private javax.swing.JLabel jLabelProvince2;
    private javax.swing.JLabel jLabelProvinceFacility;
    private javax.swing.JLabel jLabelRegNum;
    private javax.swing.JLabel jLabelRegYear;
    private javax.swing.JLabel jLabelRemain;
    private javax.swing.JLabel jLabelRemain1;
    private javax.swing.JLabel jLabelRemain3;
    private javax.swing.JLabel jLabelSepDetails;
    private javax.swing.JLabel jLabelSerialAcq;
    private javax.swing.JLabel jLabelSubAcc;
    private javax.swing.JLabel jLabelTo;
    private javax.swing.JLabel jLabelTripDetails;
    private javax.swing.JLabel jLabelUnproved;
    private javax.swing.JLabel jLabelVehReg;
    private javax.swing.JLabel jLabelWk1DialogActDis;
    private javax.swing.JLabel jLabelWk1DialogActivityDesc;
    private javax.swing.JLabel jLabelWk1DialogBudget;
    private javax.swing.JLabel jLabelWk1From;
    private javax.swing.JLabel jLabelWk1LnActivity;
    private javax.swing.JLabel jLabelWk1Misc;
    private javax.swing.JLabel jLabelWk1MiscAmt;
    private javax.swing.JLabel jLabelWk1To;
    private javax.swing.JLabel jLabelWk2DialogActDis;
    private javax.swing.JLabel jLabelWk2DialogActivityDesc;
    private javax.swing.JLabel jLabelWk2DialogBudget;
    private javax.swing.JLabel jLabelWk2LnActivity;
    private javax.swing.JLabel jLabelWk2Misc;
    private javax.swing.JLabel jLabelWk2MiscAmt;
    private javax.swing.JLabel jLabelWk3DialogActDis;
    private javax.swing.JLabel jLabelWk3DialogActivityDesc;
    private javax.swing.JLabel jLabelWk3DialogBudget;
    private javax.swing.JLabel jLabelWk3LnActivity;
    private javax.swing.JLabel jLabelWk3Misc;
    private javax.swing.JLabel jLabelWk3MiscAmt;
    private javax.swing.JLabel jLabelWkDuration;
    private javax.swing.JLabel jLabellPayType;
    private javax.swing.JLabel jLabellogged1;
    private javax.swing.JLabel jLabellogged3;
    private javax.swing.JMenu jMenuAcquittal;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenu jMenuEdit;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenuItem jMenuItem3WkPlan;
    private javax.swing.JMenuItem jMenuItemAccMgrRev;
    private javax.swing.JMenuItem jMenuItemAcqAccApp;
    private javax.swing.JMenuItem jMenuItemAcqHeadApp;
    private javax.swing.JMenuItem jMenuItemAcqSchGen;
    private javax.swing.JMenuItem jMenuItemAcqSupApp;
    private javax.swing.JMenuItem jMenuItemAcqView;
    private javax.swing.JMenuItem jMenuItemAcquittal;
    private javax.swing.JMenuItem jMenuItemClose;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemHeadApp;
    private javax.swing.JMenuItem jMenuItemPlanFinApproval;
    private javax.swing.JMenuItem jMenuItemPlanHODApproval;
    private javax.swing.JMenuItem jMenuItemPlanSupApproval;
    private javax.swing.JMenuItem jMenuItemPlanView;
    private javax.swing.JMenuItem jMenuItemReqGenLst;
    private javax.swing.JMenuItem jMenuItemReqSubmitted;
    private javax.swing.JMenuItem jMenuItemRequest;
    private javax.swing.JMenuItem jMenuItemRequestMOH;
    private javax.swing.JMenuItem jMenuItemSchPerDiem;
    private javax.swing.JMenuItem jMenuItemSpecialAcquittal;
    private javax.swing.JMenuItem jMenuItemSubmit;
    private javax.swing.JMenuItem jMenuItemSupApp;
    private javax.swing.JMenuItem jMenuItemUserCreate;
    private javax.swing.JMenuItem jMenuItemUserProfUpd;
    private javax.swing.JMenuItem jMenuItemView;
    private javax.swing.JMenuItem jMenuMonPlanEdit;
    private javax.swing.JMenu jMenuMonthlyPlan;
    private javax.swing.JMenu jMenuNew;
    private javax.swing.JMenu jMenuPlanApproval;
    private javax.swing.JMenu jMenuReports;
    private javax.swing.JMenu jMenuRequest;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelAcqELog;
    private javax.swing.JPanel jPanelAcquittal;
    private javax.swing.JPanel jPanelAtt4;
    private javax.swing.JPanel jPanelAttach;
    private javax.swing.JPanel jPanelCaptureDetails;
    private javax.swing.JPanel jPanelGen;
    private javax.swing.JPanel jPanelReport;
    private javax.swing.JPanel jPanelReportDetails;
    private javax.swing.JPanel jPanelWkOne;
    private javax.swing.JPanel jPanellPayType;
    private javax.swing.JRadioButton jRadioAccProved;
    private javax.swing.JRadioButton jRadioAccUnproved;
    private javax.swing.JRadioButton jRadioBank;
    private javax.swing.JRadioButton jRadioMukuru;
    private javax.swing.JRadioButton jRadioNoAcc;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JPopupMenu.Separator jSeparator13;
    private javax.swing.JPopupMenu.Separator jSeparator14;
    private javax.swing.JPopupMenu.Separator jSeparator15;
    private javax.swing.JPopupMenu.Separator jSeparator16;
    private javax.swing.JPopupMenu.Separator jSeparator18;
    private javax.swing.JPopupMenu.Separator jSeparator19;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator21;
    private javax.swing.JPopupMenu.Separator jSeparator22;
    private javax.swing.JPopupMenu.Separator jSeparator23;
    private javax.swing.JPopupMenu.Separator jSeparator24;
    private javax.swing.JPopupMenu.Separator jSeparator26;
    private javax.swing.JPopupMenu.Separator jSeparator28;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator31;
    private javax.swing.JPopupMenu.Separator jSeparator34;
    private javax.swing.JPopupMenu.Separator jSeparator35;
    private javax.swing.JSeparator jSeparator36;
    private javax.swing.JPopupMenu.Separator jSeparator37;
    private javax.swing.JPopupMenu.Separator jSeparator38;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator62;
    private javax.swing.JPopupMenu.Separator jSeparator63;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPaneAcqAtt;
    private javax.swing.JTable jTableAcquittalDocAtt;
    private javax.swing.JTable jTableActivityAcq;
    private javax.swing.JTable jTableReqActivities;
    private javax.swing.JTable jTableTripDetails;
    private javax.swing.JTextField jTextAccDet1;
    private javax.swing.JTextField jTextAccNum;
    private javax.swing.JTextField jTextAccProvedAcq;
    private javax.swing.JTextField jTextAccUnprovedAcq;
    private javax.swing.JTextField jTextAcqBreakfast;
    private javax.swing.JTextField jTextAcqDinner;
    private javax.swing.JTextField jTextAcqIncidental;
    private javax.swing.JTextField jTextAcqLunch;
    private javax.swing.JTextArea jTextAreaNamTravel;
    private javax.swing.JTextField jTextAttAcqDocName;
    private javax.swing.JTextField jTextAttDocDesc;
    private javax.swing.JTextField jTextAttDocFilePath;
    private javax.swing.JTextField jTextBankNam;
    private javax.swing.JTextField jTextFieldDialogSite;
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
    private javax.swing.JTextField jTextMiscActivityAcq;
    private javax.swing.JTextField jTextMiscAmtAcq;
    private javax.swing.JTextField jTextNoAcc;
    private javax.swing.JTextField jTextPurpose;
    private javax.swing.JTextField jTextVehReg;
    private javax.swing.JToggleButton jToggleButtonAllActivities;
    private javax.swing.JToggleButton jToggleButtonNoActivity;
    // End of variables declaration//GEN-END:variables
}
