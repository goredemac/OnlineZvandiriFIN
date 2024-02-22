/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, cforhoose Tools | Templates
 * and open the template in the editor.
 */
package ClaimAppendix2;

import ClaimPlanPerDiem.JFrameMnthPlanPerDiemEdit;
import ClaimPlanPerDiem.JFrameMnthPlanPerDiemCreate;
import ClaimAppendix1.JFrameAccMgrAppList;
import ClaimAppendix1.JFrameReqHeadAppList;
import ClaimAppendix1.JFrameReqHQPayScheduleApp;
import ClaimAppendix1.JFrameSupAppList;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import ClaimAppendix1.*;
import ClaimReports.*;
import ClaimPlan.*;
import java.awt.Image;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.ImageIcon;
import javax.swing.WindowConstants;
import utils.connCred;
import utils.connSaveFile;
import utils.savePDFToDB;
import utils.StockVehicleMgt;
import utils.emailSend;

/**
 *
 * @author cgoredema
 */
public class JFrameAppSupAcquittal extends javax.swing.JFrame {

    connCred c = new connCred();
    emailSend emSend = new emailSend();
    PreparedStatement pst = null;
    PreparedStatement pst1 = null;
    int itmNum = 1;
    int month, finyear, count;
    Date curYear = new Date();
    int initdocVersion = 0;
    int checkRefCount = 0;
    int pdCount = 0;
    int minWkNum = 1;
    int countWkNum = 1;
    int docVerPlan = 0;
    int genCount = 0;
    int itmCount = 0;
    int wfCount = 0;
    SimpleDateFormat df = new SimpleDateFormat("yyyy");
    DefaultTableModel model;
    DefaultTableModel modelAcq;
    DefaultTableModel modelTrip;
    DefaultTableModel modelMonAttRep;
    String sendTo, sendToProv, sendToFin, reqUsrMail, finUsrMail, createUsrNam, breakfastAll, lunchAll,
            docVersion, actVersion, oldDocVersion, preModNum, dinnerAll, incidentalAll, unProvedAll,
            searchRef, statusCodeApp, statusCodeDisApp, checkRef, amtReq,
            authNam1, authNam2, usrGrp, reqUsrNam, usrRejNam, searchEmpNam, empNum, empNam, empOff;
    String province = "";
    String actRef = "";
    String actRef1 = "";
    String actRef2 = "";
    String actRef3 = "";
    String actRef4 = "";
    double breakfastsubtotalAcq = 0;
    double lunchsubtotalAcq = 0;
    double dinnersubtotalAcq = 0;
    double incidentalsubtotalAcq = 0;
    double miscSubTotAcq = 0;
    double provedSubTotAcq = 0;
    double unprovedSubTotAcq = 0;
    double breakfastsubtotal = 0;
    double lunchsubtotal = 0;
    double dinnersubtotal = 0;
    double incidentalsubtotal = 0;
    double miscSubTot = 0;
    double provedSubTot = 0;
    double unprovedSubTot = 0;
    double allTotalAcq = 0;
    double allTotal = 0;
    double totSeg1 = 0;
    double totSeg2 = 0;
    double balAmt = 0;
    String hostName = "";
    int oldImgAttVer = 0;

    /**
     * Creates new form JFrameTabApp1
     */
    public JFrameAppSupAcquittal() {
        initComponents();
        showDate();
        showTime();
        computerName();
//        allowanceRate();

        model = (DefaultTableModel) jTableActivityReq.getModel();
        modelAcq = (DefaultTableModel) jTableActivityAcq.getModel();
        modelMonAttRep = (DefaultTableModel) jTableMonDistAcquitalAttRep.getModel();
        fetchdata();
        //  mainPageTotUpdate();
        //mainPageTotUpdateAcq();
    }

    public JFrameAppSupAcquittal(String yearRef, String usrLogNum) {
        initComponents();
        showDate();
        showTime();
        computerName();
        jButtonSave.setVisible(false);
        jButtonClearBud.setVisible(false);
        jLabelBudUpd.setVisible(false);
        jLabelBudCod.setVisible(false);
        jLabelEmp.setText(usrLogNum);
        jLabelEmp.setVisible(false);
        jLabelReqRefNum.setText(yearRef);
        model = (DefaultTableModel) jTableActivityReq.getModel();
        modelAcq = (DefaultTableModel) jTableActivityAcq.getModel();
        modelTrip = (DefaultTableModel) jTableTripDetails.getModel();
         modelMonAttRep = (DefaultTableModel) jTableMonDistAcquitalAttRep.getModel();
        jTableTripDetails.getColumnModel().getColumn(1).setMinWidth(0);
        jTableTripDetails.getColumnModel().getColumn(1).setMaxWidth(0);
        checkRef = yearRef;
        System.out.println("check ref " + checkRef);
        jTextAreaNamTravel.setLineWrap(true);
        jTextAreaNamTravel.setWrapStyleWord(true);
        jTextAreaNamTravel.setEditable(false);
        jLabelAcqAppTotPlannedCost.setVisible(false);
        jLabelAppTotPlannedReq.setVisible(false);
        jTabbedPaneAcqAtt.setEnabledAt(0, true);
        jTabbedPaneAcqAtt.setEnabledAt(1, false);
        jTabbedPaneAcqAtt.setEnabledAt(2, true);
        jTabbedPaneAcqAtt.setEnabledAt(3, true);
        jTabbedPaneAcqAtt.setEnabledAt(4, true);
        jTabbedPaneAcqAtt.setEnabledAt(5, false);
        jTabbedPaneAcqAtt.setEnabledAt(6, false);
        jTabbedPaneAcqAtt.setTitleAt(0, "Activity Summary Report");
        jTabbedPaneAcqAtt.setTitleAt(1, "");
        jTabbedPaneAcqAtt.setTitleAt(2, "Vehicle Log Sheet");
        jTabbedPaneAcqAtt.setTitleAt(3, "Proven Expenses");
        jTabbedPaneAcqAtt.setTitleAt(4, "Other e.g. Log Book Extra Page");
        jTabbedPaneAcqAtt.setTitleAt(5, "");
        jTabbedPaneAcqAtt.setTitleAt(6, "");
        findUser();
        findUserGrp();

        fetchdata();
        findChgPaid();
        if ("MA".equals(jLabelAcqSerial.getText())) {
            fetchReqItmData();
//            fetchMonDistAttDoc(jLabelAcqRefNum.getText());
            getMonEnvSet();
            jLabelAcqAppTotPlannedCost.setVisible(true);
            jLabelAppTotPlannedReq.setVisible(true);
            jTabbedPaneAppSys.setTitleAt(3, "Report & Attachments");
            jTabbedPaneAcqAtt.setEnabledAt(0, true);
            jTabbedPaneAcqAtt.setEnabledAt(1, false);
            jTabbedPaneAcqAtt.setEnabledAt(2, false);
            jTabbedPaneAcqAtt.setEnabledAt(3, false);
            jTabbedPaneAcqAtt.setEnabledAt(4, false);
            jTabbedPaneAcqAtt.setEnabledAt(5, false);
            jTabbedPaneAcqAtt.setEnabledAt(6, false);
            jTabbedPaneAcqAtt.setTitleAt(0, "Activity Summary Report");
            jTabbedPaneAcqAtt.setTitleAt(1, "");
            jTabbedPaneAcqAtt.setTitleAt(2, "");
            jTabbedPaneAcqAtt.setTitleAt(3, "");
            jTabbedPaneAcqAtt.setTitleAt(4, "");
            jTabbedPaneAcqAtt.setTitleAt(5, "");
            jTabbedPaneAcqAtt.setTitleAt(6, "");
        }
        fetchMonDistAttDocRep();
        mainPageTotUpdate();
        mainPageTotUpdateAcq();
        findApplicantUser();

        if (!"Administrator".equals(usrGrp)) {
            jMenuItemUserProfUpd.setEnabled(false);
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
                jLabelLineTime4.setText(s.format(d));
                jLabelLineTime5.setText(s.format(d));
                jLabelLineTime6.setText(s.format(d));
                jLabelLineTime7.setText(s.format(d));
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
        jLabelLineDate4.setText(s.format(d));
        jLabelLineDate5.setText(s.format(d));
        jLabelLineDate6.setText(s.format(d));
        jLabelLineDate7.setText(s.format(d));

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
                jMenuItemAccMgrRev.setEnabled(false);//can remove
                jMenuItemAcqAccApp.setEnabled(false);//can remove
                jMenuItemPlanFinApproval.setEnabled(false);//can remove
                jMenuItemSupApp.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuItemAcqSupApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuItemPlanView.setEnabled(false);
                jMenuItemPlanSupApproval.setEnabled(false);
                jMenuItemPlanHODApproval.setEnabled(false);

            }

            if ("usrFinSup".equals(usrGrp)) {
                jMenuItemAccMgrRev.setEnabled(false);//can remove
                jMenuItemAcqAccApp.setEnabled(false);//can remove
                jMenuItemPlanFinApproval.setEnabled(false);//can remove
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

    void findApplicantUser() {
        try {

            if ("S".equals(jLabelReqSerial.getText())) {
                searchEmpNam = jLabelEmp.getText();
            } else {
                searchEmpNam = jLabelAcqEmpNum.getText();
            }
//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();
            ResultSet r = st.executeQuery("select EMP_MAIL,EMP_NAM from [ClaimsAppSysZvandiri].[dbo]."
                    + "[EmpDetTab] where emp_num ='" + searchEmpNam + "'");

            while (r.next()) {

                reqUsrMail = r.getString(1);
                reqUsrNam = r.getString(2);

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findUser() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select * from [ClaimsAppSysZvandiri].[dbo]."
                    + "[EmpDetTab] where emp_num ='" + jLabelEmp.getText() + "'");

            while (r.next()) {

                jLabelGenLogNam.setText(r.getString(2));
                jLabelLineLogNam.setText(r.getString(2));
                jLabelLineLogNam1.setText(r.getString(2));
                jLabelLineLogNam3.setText(r.getString(2));
                jLabelLineLogNam4.setText(r.getString(2));
                jLabelLineLogNam5.setText(r.getString(2));
                jLabelLineLogNam6.setText(r.getString(2));
                jLabelLineLogNam7.setText(r.getString(2));
                jLabelLineLogNam8.setText(r.getString(2));
                createUsrNam = r.getString(2);

            }

            //                 conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Connect to Database (Province).Try Again", "Error Connection", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
    }

    void getMonEnvSet() {
        jLabelAllBal.setVisible(false);
        jLabelAcqBreakFastSubTotBal.setVisible(false);
        jLabelAcqLunchSubTotBal.setVisible(false);
        jLabelAcqDinnerSubTotBal.setVisible(false);
        jLabelAcqIncidentalSubTotBal.setVisible(false);
        jLabelMscBal.setVisible(false);
        jLabelAcqMiscSubTotBal.setVisible(false);
        jLabelAccBal.setVisible(false);
        jLabelAcqAccUnprovedSubTotBal.setVisible(false);
        jLabelAcqAccProvedSubTotBal.setVisible(false);
        jSeparator8.setVisible(false);
        jSeparator7.setVisible(false);
        jSeparator6.setVisible(false);
        jLabelAllReq.setText("Planned");
        jLabelAllAcq.setText("Actual");
        jLabelMiscReq.setText("Planned");
        jLabelMiscAcq.setText("Actual");
        jLabelAccReq.setText("Planned");
        jLabelAccAcq.setText("Actual");
        jLabelAllReq.setForeground(new java.awt.Color(0, 102, 0));
        jLabelAllAcq.setForeground(new java.awt.Color(0, 204, 0));
        jLabelMiscReq.setForeground(new java.awt.Color(0, 102, 0));
        jLabelMiscAcq.setForeground(new java.awt.Color(0, 204, 0));
        jLabelAccReq.setForeground(new java.awt.Color(0, 102, 0));
        jLabelAccAcq.setForeground(new java.awt.Color(0, 204, 0));
        jLabelAcqBreakFastSubTot.setForeground(new java.awt.Color(0, 204, 0));
        jLabelAcqLunchSubTot.setForeground(new java.awt.Color(0, 204, 0));
        jLabelAcqDinnerSubTot.setForeground(new java.awt.Color(0, 204, 0));
        jLabelAcqIncidentalSubTot.setForeground(new java.awt.Color(0, 204, 0));
        jLabelAcqMiscSubTot.setForeground(new java.awt.Color(0, 204, 0));
        jLabelAcqAccUnprovedSubTot.setForeground(new java.awt.Color(0, 204, 0));
        jLabelAcqAccProvedSubTot.setForeground(new java.awt.Color(0, 204, 0));
        jLabelAllReq.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabelAllAcq.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabelMiscReq.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabelMiscAcq.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabelAccReq.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabelAccAcq.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabelAcqBreakFastSubTot.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabelAcqLunchSubTot.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabelAcqDinnerSubTot.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabelAcqIncidentalSubTot.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabelAcqMiscSubTot.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabelAcqAccUnprovedSubTot.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabelAcqAccProvedSubTot.setFont(new java.awt.Font("Tahoma", 1, 10));
        jLabelAppTotReq.setText("Total Claimed");

    }

    void findMOHCCCreator() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT b.EMP_MAIL,a.EMP_NAM_CREATOR  FROM [ClaimsAppSysZvandiri].[dbo].[CreatorMOHCCTAB] a "
                    + "join [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] b on a.emp_num_creator = b.emp_num "
                    + "where ref_num ='" + jLabelReqRefNum.getText() + "'");

            while (r.next()) {

                reqUsrMail = r.getString(1);
                reqUsrNam = r.getString(2);
            }

            //                 conn.close();
        } catch (Exception e) {

        }
    }

    void updateRejComments() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlRejUpdate = "UPDATE [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] set REJECT_COMMENTS ='" + jTextReasonReject.getText() + "' where "
                    + "concat(SERIAL,REF_NUM)='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "' and DOC_VER ='" + docVersion + "'";

            pst = conn.prepareStatement(sqlRejUpdate);
            pst.executeUpdate();

            if ((jCheckBoxDisAgree.isSelected()) && ("R".equals(jLabelReqSerial.getText()))) {
                wkClearedUpdate();
            }

            if ("MA".equals(jLabelAcqSerial.getText())) {

                monClearedUpdate();
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void imgDisplayFile1() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String imgDisFile1 = "";

            if ("S".equals(checkRef.substring(0, 1))) {
                imgDisFile1 = jLabelReqSerial.getText() + jLabelReqRefNum.getText();
            } else {
                imgDisFile1 = jLabelAcqSerial.getText() + jLabelAcqRefNum.getText();
            }

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT SERIAL,REF_NUM,ATT_IMAGE,IMG_VERSION "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimDocAttachTab] where concat(SERIAL,REF_NUM)="
                    + "'" + imgDisFile1 + "'"
                    + " and IMG_VERSION ='" + oldImgAttVer + "'"
                    + "and DOC_ATT_NUM = '1'");

            while (r.next()) {

                byte[] img = r.getBytes("ATT_IMAGE");

                ImageIcon imageIcon = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(600, 500, Image.SCALE_SMOOTH));
                jLabelImgFile1.setIcon(imageIcon);

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void imgDisplayFile2() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String imgDisFile2 = "";

            if ("S".equals(checkRef.substring(0, 1))) {
                imgDisFile2 = jLabelReqSerial.getText() + jLabelReqRefNum.getText();
            } else {
                imgDisFile2 = jLabelAcqSerial.getText() + jLabelAcqRefNum.getText();
            }

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT SERIAL,REF_NUM,ATT_IMAGE,IMG_VERSION "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimDocAttachTab] where concat(SERIAL,REF_NUM)="
                    + "'" + imgDisFile2 + "'"
                    + " and IMG_VERSION ='" + oldImgAttVer + "'"
                    + "and DOC_ATT_NUM = '2'");

            while (r.next()) {

                byte[] img2 = r.getBytes("ATT_IMAGE");

                ImageIcon imageIcon2 = new ImageIcon(new ImageIcon(img2).getImage().getScaledInstance(600, 500, Image.SCALE_SMOOTH));
                jLabelImgFile2.setIcon(imageIcon2);

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void imgDisplayFile3() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String imgDisFile3 = "";

            if ("S".equals(checkRef.substring(0, 1))) {
                imgDisFile3 = jLabelReqSerial.getText() + jLabelReqRefNum.getText();
            } else {
                imgDisFile3 = jLabelAcqSerial.getText() + jLabelAcqRefNum.getText();
            }

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT SERIAL,REF_NUM,ATT_IMAGE,IMG_VERSION "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimDocAttachTab] where concat(SERIAL,REF_NUM)="
                    + "'" + imgDisFile3 + "'"
                    + " and IMG_VERSION ='" + oldImgAttVer + "'"
                    + "and DOC_ATT_NUM = '3'");

            while (r.next()) {

                byte[] img3 = r.getBytes("ATT_IMAGE");

                ImageIcon imageIcon3 = new ImageIcon(new ImageIcon(img3).getImage().getScaledInstance(600, 500, Image.SCALE_SMOOTH));
                jLabelImgFile3.setIcon(imageIcon3);

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void imgDisplayFile4() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String imgDisFile4 = "";

            if ("S".equals(checkRef.substring(0, 1))) {
                imgDisFile4 = jLabelReqSerial.getText() + jLabelReqRefNum.getText();
            } else {
                imgDisFile4 = jLabelAcqSerial.getText() + jLabelAcqRefNum.getText();
            }

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT SERIAL,REF_NUM,ATT_IMAGE,IMG_VERSION "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimDocAttachTab] where concat(SERIAL,REF_NUM)="
                    + "'" + imgDisFile4 + "'"
                    + " and IMG_VERSION ='" + oldImgAttVer + "'"
                    + "and DOC_ATT_NUM = '4'");

            while (r.next()) {

                byte[] img4 = r.getBytes("ATT_IMAGE");

                ImageIcon imageIcon4 = new ImageIcon(new ImageIcon(img4).getImage().getScaledInstance(600, 500, Image.SCALE_SMOOTH));
                jLabelImgFile4.setIcon(imageIcon4);

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void imgDisplayFile5() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String imgDisFile5 = "";

            if ("S".equals(checkRef.substring(0, 1))) {
                imgDisFile5 = jLabelReqSerial.getText() + jLabelReqRefNum.getText();
            } else {
                imgDisFile5 = jLabelAcqSerial.getText() + jLabelAcqRefNum.getText();
            }

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT SERIAL,REF_NUM,ATT_IMAGE,IMG_VERSION "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimDocAttachTab] where concat(SERIAL,REF_NUM)="
                    + "'" + imgDisFile5 + "'"
                    + " and IMG_VERSION ='" + oldImgAttVer + "'"
                    + "and DOC_ATT_NUM = '5'");

            while (r.next()) {

                byte[] img5 = r.getBytes("ATT_IMAGE");

                ImageIcon imageIcon5 = new ImageIcon(new ImageIcon(img5).getImage().getScaledInstance(600, 500, Image.SCALE_SMOOTH));
                jLabelImgFile5.setIcon(imageIcon5);

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void insGenTab() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlUpdate = "UPDATE [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] set ACT_REC_STA ='QP' where "
                    + "REF_NUM='" + jLabelAcqRefNum.getText() + "' and SERIAL ='" + jLabelAcqSerial.getText() + "'";
            pst1 = conn.prepareStatement(sqlUpdate);
            pst1.executeUpdate();

            String sql = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] "
                    + "(REF_YEAR,SERIAL,REF_NUM,REF_DAT,EMP_NUM,EMP_NAM,EMP_TTL,EMP_PROV,EMP_OFF,"
                    + "EMP_BNK_NAM,ACC_NUM,ACT_MAIN_PUR,ACT_TOT_AMT,PREV_SERIAL,PREV_REF_NUM,"
                    + "PREV_REF_DAT,REG_MOD_VER,DOC_VER,ACT_REC_STA) "
                    + " VALUES (?,?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?)";

            pst = conn.prepareStatement(sql);
            pst.setString(1, jLabelAcqYear.getText());
            pst.setString(2, jLabelAcqSerial.getText());
            pst.setString(3, jLabelAcqRefNum.getText());
            pst.setString(4, jLabelAcqRegDate.getText());
            pst.setString(5, jLabelAcqEmpNum.getText());
            pst.setString(6, jLabelAcqEmpNam.getText());
            pst.setString(7, jLabelAcqEmpTitle.getText());
            pst.setString(8, jLabelAcqProvince.getText());
            pst.setString(9, jLabelAcqOffice.getText());
            pst.setString(10, jLabelAcqBankName.getText());
            pst.setString(11, jLabelAcqAccNum.getText());
            pst.setString(12, jLabelAcqActMainPurpose.getText());
            pst.setString(13, jLabelAcqAppTotReqCost.getText());
            pst.setString(14, jLabelReqSerial.getText());
            pst.setString(15, String.valueOf(jLabelReqRefNum.getText()));
            pst.setString(16, jLabelRegDate.getText());
            pst.setString(17, preModNum);
            pst.setString(18, docVersion);
            pst.setString(19, "Q");

            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void insItmTab() {

        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlItmUpdate = "UPDATE [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] set ACT_REC_STA ='QP' where "
                    + "REF_NUM='" + jLabelAcqRefNum.getText() + "' and SERIAL ='" + jLabelAcqSerial.getText() + "'";

            pst1 = conn.prepareStatement(sqlItmUpdate);
            pst1.executeUpdate();

            itmNum = 1;

            for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {

                String sqlitm = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                        + "(REF_YEAR ,SERIAL ,REF_NUM ,ITM_NUM ,ACT_DATE ,BRANCH ,PROJ_ID ,"
                        + "PRJ_TASK_CODE ,ACT_SITE ,ACT_ITM_PUR ,BRK_AMT ,LNC_AMT ,DIN_AMT ,"
                        + "INC_AMT ,MSC_ACT ,MSC_AMT ,ACC_UNPROV_AMT ,ACC_PRO_AMT ,ACT_ITM_TOT ,"
                        + "PLAN_WK ,REG_MOD_VER ,DOC_VER ,ACT_REC_STA)"
                        + " VALUES (?,?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?)";

                pst = conn.prepareStatement(sqlitm);
                pst.setString(1, jLabelAcqYear.getText());
                pst.setString(2, jLabelAcqSerial.getText());
                pst.setString(3, jLabelAcqRefNum.getText());
                pst.setString(4, Integer.toString(itmNum));
                pst.setString(5, jTableActivityAcq.getValueAt(i, 0).toString());
                pst.setString(6, jTableActivityAcq.getValueAt(i, 1).toString());
                pst.setString(7, jTableActivityAcq.getValueAt(i, 2).toString());
                pst.setString(8, jTableActivityAcq.getValueAt(i, 3).toString());
                pst.setString(9, jTableActivityAcq.getValueAt(i, 4).toString());
                pst.setString(10, jTableActivityAcq.getValueAt(i, 5).toString());
                pst.setString(11, jTableActivityAcq.getValueAt(i, 6).toString());
                pst.setString(12, jTableActivityAcq.getValueAt(i, 7).toString());
                pst.setString(13, jTableActivityAcq.getValueAt(i, 8).toString());
                pst.setString(14, jTableActivityAcq.getValueAt(i, 9).toString());
                pst.setString(15, jTableActivityAcq.getValueAt(i, 10).toString());
                pst.setString(16, jTableActivityAcq.getValueAt(i, 11).toString());
                pst.setString(17, jTableActivityAcq.getValueAt(i, 12).toString());
                pst.setString(18, jTableActivityAcq.getValueAt(i, 13).toString());
                pst.setString(19, jTableActivityAcq.getValueAt(i, 14).toString());
                pst.setString(20, Integer.toString(minWkNum));
                pst.setString(21, preModNum);
                pst.setString(22, docVersion);
                pst.setString(23, "Q");

                pst.executeUpdate();
                itmNum = itmNum + 1;
                ;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void SaveSupDet() {
        if ((!(jCheckBoxAgreed.isSelected())) && (!(jCheckBoxDisAgree.isSelected()))) {

            JOptionPane.showMessageDialog(this, "Please select if you Agree or Disagree "
                    + "with the perdiem acquital before proceeding",
                    "Error", JOptionPane.ERROR_MESSAGE);
            jCheckBoxAgreed.requestFocusInWindow();
            jCheckBoxAgreed.setFocusable(true);
        } else if (jCheckBoxAgreed.isSelected()) {
            dbUpdRec();
        } else {
            jDialogEmailComments.setVisible(true);
            jDialogEmailComments.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        }
    }

    void saveUpdFinalised() {
        try {

            if (jCheckBoxAgreed.isSelected()) {
                JOptionPane.showMessageDialog(this, "Perdiem Acquittal Application No. "
                        + jLabelAcqSerial.getText() + " " + jLabelAcqRefNum.getText() + " sucessfully saved.");
                jDialogWaitingEmail.setVisible(true);

                String mailMsg;
                if ("R".equals(jLabelReqSerial.getText())) {
                    mailMsg = "<html><body> Dear Finance Team <br><br>Supervisor <b>"
                            + jLabelGenLogNam.getText() + "</b> has approved per diem acquittal No. " + jLabelAcqSerial.getText() + " " + jLabelAcqRefNum.getText() + ".<br><br>"
                            + "Please check your Finance System inbox and action<br><br> Kind Regards <br><br>"
                            + " Finance Management System </body></html>";
                } else {
                    mailMsg = "<html><body> Dear Finance Team <br><br>Supervisor <b>"
                            + jLabelGenLogNam.getText() + "</b> has approved per diem request No. " + jLabelAcqSerial.getText() + " " + jLabelAcqRefNum.getText() + ".<br><br>"
                            + "Please check your Finance System inbox and action<br><br> Kind Regards <br><br>"
                            + " Finance Management System </body></html>";
                }

                String MailMsgTitle = "Per Diem Approval Acquittal - Reference No. " + jLabelAcqSerial.getText() + " " + jLabelAcqRefNum.getText() + " ";

                emSend.sendMail(MailMsgTitle, c.FinGrpMail, mailMsg, reqUsrMail);

                jDialogWaitingEmail.setVisible(false);

                JOptionPane.showMessageDialog(null, "Email has been forwarded to Finance for approval of Acquittal No. "
                        + jLabelAcqSerial.getText() + " " + jLabelAcqRefNum.getText());
                new JFrameSupAcqList(jLabelEmp.getText()).setVisible(true);
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Perdiem Acquittal Application No. "
                        + jLabelAcqSerial.getText() + " " + jLabelAcqRefNum.getText() + " sucessfully saved.");
                jDialogWaitingEmail.setVisible(true);

                String mailMsg = "<html><body> Dear " + reqUsrNam + " <br><br><b>"
                        + jLabelGenLogNam.getText() + "</b> form Finance team has rejected your per diem acquittal No. A " + jLabelAcqRefNum.getText() + ".<br><br>"
                        + "<b>Supervisor Reject Comments</b><br>" + jTextReasonReject.getText() + "<br><br> "
                        + "Kind Regards <br><br>"
                        + " Finance Management System </body></html>";

                String MailMsgTitle = "Per Diem Rejected - Reference No. A" + jLabelAcqRefNum.getText() + " ";

                emSend.sendMail(MailMsgTitle, reqUsrMail, mailMsg, "");

                jDialogWaitingEmail.setVisible(false);

                JOptionPane.showMessageDialog(this, "Email notification has been send to " + reqUsrNam + " for rejection of acquittal No. A "
                        + jLabelAcqRefNum.getText());

                new JFrameAccAcqList(jLabelEmp.getText()).setVisible(true);
                setVisible(false);

            }
        } catch (Exception e) {
            System.out.println(e);
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

    void mainPageTotUpdate() {
        breakfastsubtotal = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double breakfastamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 6));
            breakfastsubtotal += breakfastamount;

        }
        jLabelBreakFastSubTot.setText(Double.toString(breakfastsubtotal));

        lunchsubtotal = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double lunchamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 7));
            lunchsubtotal += lunchamount;
        }
        jLabelLunchSubTot.setText(Double.toString(lunchsubtotal));

        dinnersubtotal = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double dinneramount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 8));
            dinnersubtotal += dinneramount;
        }
        jLabelDinnerSubTot.setText(Double.toString(dinnersubtotal));

        incidentalsubtotal = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double incidentalamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 9));
            incidentalsubtotal += incidentalamount;
        }
        jLabelIncidentalSubTot.setText(Double.toString(incidentalsubtotal));

        miscSubTot = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double miscamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 11));
            miscSubTot += miscamount;
        }
        jLabelMiscSubTot.setText(Double.toString(miscSubTot));

        unprovedSubTot = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double unprovedamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 12));
            unprovedSubTot += unprovedamount;
        }
        jLabelAccUnprovedSubTot.setText(Double.toString(unprovedSubTot));

        provedSubTot = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double provedamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 13));
            provedSubTot += provedamount;
        }
        jLabelAccProvedSubTot.setText(String.format("%.2f", provedSubTot));

        allTotal = 0;
        allTotal = unprovedSubTot + provedSubTot + miscSubTot + incidentalsubtotal
                + dinnersubtotal + lunchsubtotal + breakfastsubtotal;

        jLabelAcqAppTotPlannedCost.setText(String.format("%.2f", allTotal));

    }
//show claimed and actual amount for district

    void mainPageTotUpdateAcq() {
        breakfastsubtotalAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double breakfastamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 6));
            breakfastsubtotalAcq += breakfastamountAcq;

        }
        jLabelAcqBreakFastSubTot.setText(Double.toString(breakfastsubtotalAcq));

        lunchsubtotalAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double lunchamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 7));
            lunchsubtotalAcq += lunchamountAcq;

        }
        jLabelAcqLunchSubTot.setText(Double.toString(lunchsubtotalAcq));

        dinnersubtotalAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double dinneramountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 8));
            dinnersubtotalAcq += dinneramountAcq;
        }
        jLabelAcqDinnerSubTot.setText(Double.toString(dinnersubtotalAcq));

        incidentalsubtotalAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double incidentalamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 9));
            incidentalsubtotalAcq += incidentalamountAcq;
        }
        jLabelAcqIncidentalSubTot.setText(Double.toString(incidentalsubtotalAcq));

        miscSubTotAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double miscamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 11));
            miscSubTotAcq += miscamountAcq;
        }
        jLabelAcqMiscSubTot.setText(Double.toString(miscSubTotAcq));

        unprovedSubTotAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double unprovedamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 12));
            unprovedSubTotAcq += unprovedamountAcq;
        }
        jLabelAcqAccUnprovedSubTot.setText(Double.toString(unprovedSubTotAcq));

        provedSubTotAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double provedamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 13));
            provedSubTotAcq += provedamountAcq;
        }

        jLabelAcqAccProvedSubTot.setText(Double.toString(provedSubTotAcq));

        allTotalAcq = unprovedSubTotAcq + provedSubTotAcq + miscSubTotAcq + incidentalsubtotalAcq
                + dinnersubtotalAcq + lunchsubtotalAcq + breakfastsubtotalAcq;

        jLabelAcqBreakFastSubTotBal.setText(Double.toString(breakfastsubtotal - breakfastsubtotalAcq));
        jLabelAcqLunchSubTotBal.setText(Double.toString(lunchsubtotal - lunchsubtotalAcq));
        jLabelAcqDinnerSubTotBal.setText(Double.toString(dinnersubtotal - dinnersubtotalAcq));
        jLabelAcqIncidentalSubTotBal.setText(Double.toString(incidentalsubtotal - incidentalsubtotalAcq));
        jLabelAcqMiscSubTotBal.setText(Double.toString(miscSubTot - miscSubTotAcq));
        jLabelAcqAccUnprovedSubTotBal.setText(Double.toString(unprovedSubTot - unprovedSubTotAcq));
        jLabelAcqAccProvedSubTotBal.setText(Double.toString(provedSubTot - provedSubTotAcq));

        jLabelAppTotReq1.setVisible(false);
        jLabelAcqAppTotReqCost1.setVisible(false);

        if ((minWkNum == countWkNum) && ("R".equals((jLabelReqSerial.getText())))) {
            balAmtFinal();

            jLabelAppTotReq1.setVisible(true);
            jLabelAcqAppTotReqCost1.setVisible(true);
            jLabelAcqAppTotReqCost1.setText(Double.toString(balAmt));

            System.out.println("bal " + balAmt);
            if (("R".equals((jLabelReqSerial.getText())))) {
                if (balAmt > 0) {
                    jLabelAppTotReq1.setText("Total (Change)");
                } else if (balAmt < 0) {
                    jLabelAppTotReq1.setText("Total (Shortfall)");
                } else if (balAmt == 0) {
                    jLabelAppTotReq1.setText("Total (Balancing)");
                }
            }

        }
    }

    void findChgPaid() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            st.executeQuery("SELECT count(*) FROM[ClaimsAppSysZimTTECH].[dbo].[ChgPaidTab]"
                    + " where concat(PREV_SERIAL,PREV_REF_NUM) = "
                    + "'" + jLabelReqSerial.getText() + jLabelReqRefNum.getText() + "' "
                    + "and REG_MOD_VER = (select REG_MOD_VER FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] "
                    + "where concat(PREV_SERIAL,PREV_REF_NUM) ="
                    + "'" + jLabelReqSerial.getText() + jLabelReqRefNum.getText() + "' and ACT_REC_STA = 'Q' "
                    + "and concat (SERIAL,REF_NUM) = '" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "' )");

            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                pdCount = rs.getInt(1);

            }

            if (pdCount > 0) {

                st1.executeQuery("SELECT *  FROM [ClaimsAppSysZvandiri].[dbo].[ChgPaidTab] "
                        + "  Where concat(PREV_SERIAL,PREV_REF_NUM) ="
                        + " '" + jLabelReqSerial.getText() + jLabelReqRefNum.getText()
                        + "'  and concat (SERIAL,REF_NUM) = '" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "' "
                        + "and REG_MOD_VER = (select max(REG_MOD_VER) from [ClaimsAppSysZvandiri].[dbo].[ChgPaidTab] "
                        + "where concat(PREV_SERIAL,PREV_REF_NUM) = "
                        + "'" + jLabelReqSerial.getText() + jLabelReqRefNum.getText() + "' "
                        + "and concat (SERIAL,REF_NUM) = '" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "')");

                ResultSet rs1 = st1.getResultSet();

                while (rs1.next()) {
                    jLabelAcqPayBack.setText("Paid Back A/C " + rs1.getString(6));
                    jLabelAcqAmtPayBack.setText(rs1.getString(7));
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdataSpecial() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();
            Statement st3 = conn.createStatement();
            Statement st4 = conn.createStatement();
            Statement st5 = conn.createStatement();
            Statement st7 = conn.createStatement();

            st1.executeQuery("select top 1 SERIAL,REF_YEAR, REF_NUM,REF_DAT,EMP_NUM,EMP_NAM,"
                    + "EMP_TTL,EMP_PROV,EMP_OFF,EMP_BNK_NAM,ACC_NUM,BUD_COD,ACT_MAIN_PUR,"
                    + "ACT_TOT_AMT,PREV_REF_NUM,PREV_REF_DAT,ACT_REC_STA,DOC_VER + 1,DOC_VER "
                    + "FROM ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab "
                    + "where concat(SERIAL,REF_NUM) = '" + jLabelReqRefNum.getText() + "'"
                    + " and ACT_REC_STA = 'Q' ");

            ResultSet r1 = st1.getResultSet();

            while (r1.next()) {
                jLabelReqSerial.setText(r1.getString(1));
                jLabelReqYear.setText(r1.getString(2));
                jLabelRegDate.setText(r1.getString(4));
                jLabelAcqSerial.setText(r1.getString(1));
                jLabelAcqRefNum.setText(r1.getString(3));
                jLabelAcqRegDate.setText(r1.getString(4));
                jLabelReqRefNum.setText(r1.getString(3));
                jLabelAcqEmpNum.setText(r1.getString(5));
                jLabelAcqEmpNam.setText(r1.getString(6));
                jLabelAcqEmpTitle.setText(r1.getString(7));
                jLabelAcqProvince.setText(r1.getString(8));
                jLabelAcqOffice.setText(r1.getString(9));
                jLabelAcqBankName.setText(r1.getString(10));
                jLabelAcqAccNum.setText(r1.getString(11));
                jLabelAcqActMainPurpose.setText(r1.getString(13));
                jLabelAcqAppTotReqCost.setText(r1.getString(14));
                docVersion = r1.getString(18);
                oldDocVersion = r1.getString(19);
                province = r1.getString(8);
                searchRef = r1.getString(3);

            }

            model.insertRow(model.getRowCount(), new Object[]{"1901-01-01", "Special", "0",
                "Special", "", "0", "0", "0", "0", "", "0", "0", "0"});

            st2.executeQuery("SELECT ACT_DATE,ACT_DEST,"
                    + "ACT_DIST,ACT_ITM_PUR,LN_BUD_CODE,BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,"
                    + "MSC_AMT,ACC_UNPROV_AMT,ACT_ITM_TOT FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                    + "  where concat(SERIAL,REF_NUM)='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "' "
                    + "and ACT_REC_STA='Q'"
                    + "order by ACT_DATE");

            ResultSet r2 = st2.getResultSet();

            while (r2.next()) {
                modelAcq.insertRow(modelAcq.getRowCount(), new Object[]{r2.getString(1), r2.getString(2), r2.getString(3),
                    r2.getString(4), r2.getString(5), r2.getString(6), r2.getString(7), r2.getString(8), r2.getString(9),
                    r2.getString(10), r2.getString(11), r2.getString(12), r2.getString(13)});

            }

            st7.executeQuery("SELECT NAM_TRAVEL FROM"
                    + " [ClaimsAppSysZvandiri].[dbo].[ClaimTripReport] where concat"
                    + "(SERIAL,REF_NUM)='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "' ");

            ResultSet r7 = st7.getResultSet();

            while (r7.next()) {
                jTextAreaNamTravel.setText(r7.getString(1));
            }
            fetchImgCount();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void fetchdata() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();
            Statement st3 = conn.createStatement();
            Statement st4 = conn.createStatement();
            Statement st5 = conn.createStatement();
            Statement st6 = conn.createStatement();
            Statement st7 = conn.createStatement();
            Statement st8 = conn.createStatement();
            Statement st9 = conn.createStatement();

            st1.executeQuery("select  REF_YEAR, REF_NUM,REF_DAT,EMP_NUM,EMP_NAM,"
                    + "EMP_TTL,EMP_PROV,EMP_OFF,EMP_BNK_NAM,ACC_NUM,ACT_MAIN_PUR,"
                    + "ACT_TOT_AMT,PREV_REF_NUM,PREV_REF_DAT,ACT_REC_STA,DOC_VER + 1,"
                    + "DOC_VER,REG_MOD_VER,SERIAL,PREV_SERIAL "
                    + "FROM ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab "
                    + "where concat(SERIAL,REF_NUM) = '" + jLabelReqRefNum.getText() + "'"
                    + " and ACT_REC_STA = 'Q' ");

            ResultSet r1 = st1.getResultSet();

            while (r1.next()) {
                jLabelAcqYear.setText(r1.getString(1));
                jLabelAcqRefNum.setText(r1.getString(2));
                searchRef = r1.getString(2);
                jLabelAcqRegDate.setText(r1.getString(3));
                jLabelAcqEmpNum.setText(r1.getString(4));
                jLabelAcqEmpNam.setText(r1.getString(5));
                jLabelAcqEmpTitle.setText(r1.getString(6));
                jLabelAcqProvince.setText(r1.getString(7));
                province = r1.getString(7);
                jLabelAcqOffice.setText(r1.getString(8));
                jLabelAcqBankName.setText(r1.getString(9));
                jLabelAcqAccNum.setText(r1.getString(10));
                jLabelAcqActMainPurpose.setText(r1.getString(11));
                jLabelAcqAppTotReqCost.setText(r1.getString(12));
                jLabelReqRefNum.setText(r1.getString(13));
                jLabelRegDate.setText(r1.getString(14));
                docVersion = r1.getString(16);
                oldDocVersion = r1.getString(17);
                actVersion = r1.getString(18);
                preModNum = r1.getString(18);
                jLabelAcqSerial.setText(r1.getString(19));
                jLabelReqSerial.setText(r1.getString(20));

            }

            st2.executeQuery("SELECT  ITM_NUM,ACT_DATE,BRANCH,PROJ_ID,"
                    + "PRJ_TASK_CODE,ACT_SITE,ACT_ITM_PUR, BRK_AMT,"
                    + "LNC_AMT, DIN_AMT,INC_AMT, MSC_ACT,MSC_AMT, "
                    + "ACC_UNPROV_AMT, ACC_PRO_AMT, ACT_ITM_TOT  FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                    + "  where concat(SERIAL,REF_NUM)='" + jLabelReqSerial.getText() + jLabelReqRefNum.getText() + "' "
                    + "and ACT_REC_STA='A' and DOC_VER=4 and PLAN_WK=(select distinct PLAN_WK "
                    + "from [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                    + "where concat(SERIAL,REF_NUM)='" + checkRef + "')"
                    + "order by ACT_DATE");

            ResultSet r2 = st2.getResultSet();

            while (r2.next()) {
                model.insertRow(model.getRowCount(), new Object[]{r2.getString(2), r2.getString(3),
                    r2.getString(4), r2.getString(5), r2.getString(6), r2.getString(7),
                    r2.getString(8), r2.getString(9), r2.getString(10), r2.getString(11), r2.getString(12), r2.getString(13),
                    r2.getString(14), r2.getString(15), r2.getString(16)});
            }

            st3.executeQuery("SELECT  ITM_NUM,ACT_DATE,BRANCH,PROJ_ID,"
                    + "PRJ_TASK_CODE,ACT_SITE,ACT_ITM_PUR, BRK_AMT,"
                    + "LNC_AMT, DIN_AMT,INC_AMT, MSC_ACT,MSC_AMT, "
                    + "ACC_UNPROV_AMT, ACC_PRO_AMT, ACT_ITM_TOT,PLAN_WK  FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                    + "where concat(SERIAL,REF_NUM)='" + checkRef + "' "
                    + "and ACT_REC_STA='Q' order by ACT_DATE");

            ResultSet r3 = st3.getResultSet();

            while (r3.next()) {
                modelAcq.insertRow(modelAcq.getRowCount(), new Object[]{r3.getString(2), r3.getString(3),
                    r3.getString(4), r3.getString(5), r3.getString(6), r3.getString(7),
                    r3.getString(8), r3.getString(9), r3.getString(10), r3.getString(11), r3.getString(12), r3.getString(13),
                    r3.getString(14), r3.getString(15), r3.getString(16)});

                minWkNum = r3.getInt(17);
            }

            st4.executeQuery("SELECT NAM_TRAVEL,NAM_VISITED,TRIP_BRIEF FROM"
                    + " [ClaimsAppSysZvandiri].[dbo].[ClaimTripReport] where concat"
                    + "(SERIAL,REF_NUM)='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "' ");

            ResultSet r4 = st4.getResultSet();

            while (r4.next()) {
                jTextAreaNamTravel.setText(r4.getString(1));
            }

            fetchImgCount();

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
                    + "where PLAN_REF_NUM =(SELECT PLAN_REF_NUM FROM [ClaimsAppSysZvandiri].[dbo].[PlanReqClearTab] "
                    + "where REF_NUM =" + jLabelAcqRefNum.getText() + " and REQ_SERIAL ='MA') and ACT_REC_STA = 'C' "
                    + "  and act_date in (SELECT ACT_DATE FROM [ClaimsAppSysZvandiri].[dbo].[PlanUsrRecTab] "
                    + "where PLAN_WK = 1) and "
                    + "(EMP_NAM1 = '" + jLabelAcqEmpNam.getText() + "' or "
                    + "EMP_NAM2 = '" + jLabelAcqEmpNam.getText() + "' or"
                    + " EMP_NAM3 = '" + jLabelAcqEmpNam.getText() + "'  or "
                    + "EMP_NAM4 = '" + jLabelAcqEmpNam.getText() + "')");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                model.insertRow(model.getRowCount(), new Object[]{r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18)});

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchMonDistAttDocRep() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSys;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            System.out.println("run "+checkRef);
            Statement st = conn.createStatement();
            st.executeQuery("SELECT ACT_ITM,fileName ,attDesc  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimReportAttDocTab] "
                    + " where concat(SERIAL,REF_NUM) ='" + checkRef + "'  and ACT_REC_STA = 'A' ");

            ResultSet r = st.getResultSet();
            while (r.next()) {
                modelMonAttRep.insertRow(modelMonAttRep.getRowCount(), new Object[]{r.getString(1), r.getString(2),
                    r.getString(3)});

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchImgCount() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            String imgCount = "";
            if ("S".equals(checkRef.substring(0, 1))) {
                imgCount = jLabelReqSerial.getText() + jLabelReqRefNum.getText();
            } else {
                imgCount = jLabelAcqSerial.getText() + jLabelAcqRefNum.getText();
            }

            st.executeQuery("SELECT  max(IMG_VERSION)  FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[ClaimDocAttachTab] "
                    + "where concat(SERIAL,REF_NUM)="
                    + "'" + imgCount + "'");

            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                oldImgAttVer = rs.getInt(1);
            }

            imgDisplayFile1();
            imgDisplayFile2();
            imgDisplayFile3();
            imgDisplayFile4();
            imgDisplayFile5();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void createAction() {

        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlWFUpdate = "UPDATE [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] set ACT_REC_STA ='QP' where "
                    + "REF_NUM='" + jLabelAcqRefNum.getText() + "' and SERIAL ='" + jLabelAcqSerial.getText() + "'";

            pst1 = conn.prepareStatement(sqlWFUpdate);
            pst1.executeUpdate();

            String sqlcreate = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] "
                    + "(YEAR,SERIAL,REF_NUM,USR_ACTION, DOC_STATUS,ACTIONED_BY_EMP_NUM ,"
                    + "ACTIONED_BY_NAM ,SEND_TO_EMP_NUM,SEND_TO_NAM,ACTIONED_ON_DATE, ACTIONED_ON_TIME,"
                    + "ACTIONED_ON_COMPUTER,REG_MOD_VER,ACTION_VER, DOC_VER ,ACT_REC_STA)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            String actionCode = "Acquittal- Supervisor Approved";
            String actionCodeReject = "Acquittal- Supervisor Rejected";

            statusCodeApp = "SupAcqApprove";
            statusCodeDisApp = "SupAcqDisAcqApprove";

            pst1 = conn.prepareStatement(sqlcreate);

            pst1 = conn.prepareStatement(sqlcreate);

            pst1.setString(1, jLabelAcqYear.getText());
            pst1.setString(2, jLabelAcqSerial.getText());
            pst1.setString(3, jLabelAcqRefNum.getText());
            if (jCheckBoxAgreed.isSelected()) {
                pst1.setString(4, actionCode);
                pst1.setString(5, statusCodeApp);

            } else if (jCheckBoxDisAgree.isSelected()) {
                pst1.setString(4, actionCodeReject);
                pst1.setString(5, statusCodeDisApp);

            }
            pst1.setString(6, jLabelEmp.getText());
            pst1.setString(7, createUsrNam);
            if (jCheckBoxAgreed.isSelected()) {
                pst1.setString(8, "FINANCE");
                pst1.setString(9, "FINANCE");

            } else if (jCheckBoxDisAgree.isSelected()) {
                pst1.setString(8, jLabelAcqEmpNum.getText());
                pst1.setString(9, jLabelAcqEmpNam.getText());

            }
            pst1.setString(10, jLabelGenDate.getText());
            pst1.setString(11, jLabelGenTime.getText());
            pst1.setString(12, hostName);
            pst1.setString(13, preModNum);
            pst1.setString(14, "1");
            pst1.setString(15, docVersion);
            pst1.setString(16, "Q");

            pst1.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void dbUpdRec() {
        try {

            insGenTab();

            insItmTab();

            createAction();

            checkRegistration();

        } catch (Exception e) {
            e.printStackTrace();
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

            st2.executeQuery("SELECT count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] "
                    + "where concat(SERIAL,REF_NUM) ='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "' "
                    + "and DOC_VER=" + docVersion);
            ResultSet r2 = st2.getResultSet();

            while (r2.next()) {
                genCount = r2.getInt(1);
            }

            st3.executeQuery("SELECT count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                    + "where concat(SERIAL,REF_NUM) ='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "' "
                    + "and DOC_VER=" + docVersion);
            ResultSet r3 = st3.getResultSet();

            while (r3.next()) {
                itmCount = r3.getInt(1);
            }

            st4.executeQuery("SELECT  count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] "
                    + "where concat(SERIAL,REF_NUM) ='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "' "
                    + "and DOC_VER=" + docVersion);
            ResultSet r4 = st4.getResultSet();

            while (r4.next()) {
                wfCount = r4.getInt(1);
            }

            st5.executeQuery("SELECT EMP_NUM,EMP_NAM FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] "
                    + "WHERE concat(SERIAL,REF_NUM) ='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "' and DOC_VER="
                    + docVersion);
            ResultSet r5 = st5.getResultSet();

            while (r5.next()) {
                empNum = r5.getString(1);
                empNam = r5.getString(2);
            }

            if ((genCount == 1) && (itmCount > 0) && (wfCount == 1) && (!("".equals(empNam))) && (!(empNam == null))
                    && (!("0".equals(empNum)))) {

                saveUpdFinalised();

            } else {

                regFail();
                JOptionPane.showMessageDialog(null, "<html> Registration failure. Registration falure "
                        + "can be caused by slow network speeds.<br><br> Please try again. "
                        + "If the problem persist contact IT.</html>");
                jButtonSave.setEnabled(true);

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    void regFail() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlDeleteGen = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] where "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "'"
                    + " and DOC_VER=" + docVersion;
            pst = conn.prepareStatement(sqlDeleteGen);
            pst.executeUpdate();

            String sqlDeleteItm = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] where  "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "' "
                    + "and DOC_VER=" + docVersion;
            pst = conn.prepareStatement(sqlDeleteItm);
            pst.executeUpdate();

            String sqlDeleteWF = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] where  "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "' "
                    + "and DOC_VER=" + docVersion;
            pst = conn.prepareStatement(sqlDeleteWF);
            pst.executeUpdate();

            String sqlUpdateGen = "update [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] set "
                    + "ACT_REC_STA ='Q' where "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "' "
                    + "and DOC_VER=" + oldDocVersion + " and REG_MOD_VER=" + actVersion;
            pst = conn.prepareStatement(sqlUpdateGen);
            pst.executeUpdate();

            String sqlUpdateItm = "update [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] set "
                    + "ACT_REC_STA ='Q' where "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "' "
                    + "and DOC_VER=" + oldDocVersion + " and REG_MOD_VER=" + actVersion;
            pst = conn.prepareStatement(sqlUpdateItm);
            pst.executeUpdate();

            String sqlUpdateWF = "update [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] set "
                    + "ACT_REC_STA ='Q' where "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "'"
                    + " and DOC_VER=" + oldDocVersion + " and REG_MOD_VER=" + actVersion;
            pst = conn.prepareStatement(sqlUpdateWF);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void balAmtFinal() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();
            System.out.println("eff " + jLabelReqSerial.getText() + jLabelReqRefNum.getText());
            st1.executeQuery("SELECT ACT_TOT_AMT - b.CLEAR FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a "
                    + "join (SELECT PREV_SERIAL,PREV_REF_NUM,sum(CLEARED_AMT) 'CLEAR' "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab] where concat(PREV_SERIAL,PREV_REF_NUM) ="
                    + "  '" + jLabelReqSerial.getText() + jLabelReqRefNum.getText() + "' "
                    + "and ACQ_STA = 'C' group by PREV_SERIAL,PREV_REF_NUM) b "
                    + "on a.SERIAL = b.PREV_SERIAL and a.REF_NUM = b.PREV_REF_NUM "
                    + "where concat(a.SERIAL,a.REF_NUM) = '" + jLabelReqSerial.getText() + jLabelReqRefNum.getText() + "'"
                    + " and a.ACT_REC_STA = 'A'");

            ResultSet rs1 = st1.getResultSet();

            while (rs1.next()) {
                balAmt = rs1.getDouble(1);

            }

            st2.executeQuery("SELECT DOC_VER+1,REQ_AMT FROM [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab]"
                    + " where concat(SERIAL,REF_NUM) =  'A" + jLabelAcqRefNum.getText() + "' and ACQ_STA = 'C'");

            ResultSet rs2 = st2.getResultSet();

            while (rs2.next()) {
                docVerPlan = rs2.getInt(1);
                amtReq = rs2.getString(2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void wkClearedUpdate() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            balAmtFinal();

            String sqlwkUpdateP = "update [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab] set "
                    + "ACQ_STA ='P' where "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "'"
                    + "and PLAN_WK =" + minWkNum + " and  ACQ_STA = 'C'";
            pst = conn.prepareStatement(sqlwkUpdateP);
            pst.executeUpdate();

            String sqlWkCleared = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab] "
                    + "(SERIAL,REF_NUM,PLAN_WK,REQ_AMT,REQ_STA,ACQ_STA,DOC_VER)"
                    + " VALUES (?,?,?,?,?,?,?)";

            pst1 = conn.prepareStatement(sqlWkCleared);

            pst1.setString(1, jLabelReqSerial.getText());
            pst1.setString(2, jLabelReqRefNum.getText());
            pst1.setString(3, Integer.toString(minWkNum));
            pst1.setString(4, amtReq);
            pst1.setString(5, "R");
            pst1.setString(6, "A");
            pst1.setString(7, Integer.toString(docVerPlan));

            pst1.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void monClearedUpdate() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlInsMonRej = "insert into [ClaimsAppSysZvandiri].[dbo].[PlanUsrRecTab] "
                    + "SELECT distinct SERIAL,PLAN_REF_NUM,ACT_DATE,EMP_NAM,PLAN_WK,'A',ACT_TYPE "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[PlanUsrRecTab] where PLAN_REF_NUM = "
                    + "(SELECT PLAN_REF_NUM FROM [ClaimsAppSysZvandiri].[dbo].[PlanReqClearTab] "
                    + "where REF_NUM =" + jLabelAcqRefNum.getText() + " and REQ_SERIAL ='MA')";

            pst = conn.prepareStatement(sqlInsMonRej);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code.EMP The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupAccAcq = new javax.swing.ButtonGroup();
        buttonGroupAgreeCheck = new javax.swing.ButtonGroup();
        jDialogDetUpd = new javax.swing.JDialog();
        jPanelDetUpd = new javax.swing.JPanel();
        jLabelProvince1 = new javax.swing.JLabel();
        jComboBudgetCode = new javax.swing.JComboBox<>();
        jLabelBudgetCode1 = new javax.swing.JLabel();
        jLabelActMainPurpose1 = new javax.swing.JLabel();
        jButtonDialogOk = new javax.swing.JButton();
        jButtonDialogCancel = new javax.swing.JButton();
        jTextActMainPurpose = new javax.swing.JTextField();
        jButtonUpdateBud = new javax.swing.JButton();
        jLabelBudUpd = new javax.swing.JLabel();
        jLabelBudCod = new javax.swing.JLabel();
        jButtonClearBud = new javax.swing.JButton();
        jDialogWaitingEmail = new javax.swing.JDialog();
        jDialogEmailComments = new javax.swing.JDialog();
        jPanelDetUpd1 = new javax.swing.JPanel();
        jLabelRejectHeader = new javax.swing.JLabel();
        jLabelReasonDesc = new javax.swing.JLabel();
        jLabelReasonReject = new javax.swing.JLabel();
        jTextReasonReject = new javax.swing.JTextField();
        jButtonReasonDialogOk = new javax.swing.JButton();
        jButtonReasonDialogCancel = new javax.swing.JButton();
        jTabbedPaneAppSys = new javax.swing.JTabbedPane();
        jPanelGen = new javax.swing.JPanel();
        jLabelLogo = new javax.swing.JLabel();
        jLabelHeaderGen = new javax.swing.JLabel();
        jLabelGenDate = new javax.swing.JLabel();
        jLabelGenTime = new javax.swing.JLabel();
        jLabelGenLogNam1 = new javax.swing.JLabel();
        jLabelGenLogNam = new javax.swing.JLabel();
        jLabelEmpNum = new javax.swing.JLabel();
        jLabelAcqEmpNam = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabelSepDetails = new javax.swing.JLabel();
        jLabelProvince = new javax.swing.JLabel();
        jLabelBank = new javax.swing.JLabel();
        jLabelAcqBankName = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabelAcqEmpTitle = new javax.swing.JLabel();
        jLabelFinDetails = new javax.swing.JLabel();
        jLabelActMainPurposeDesc = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabelIncidentalSub = new javax.swing.JLabel();
        jLabelIncidentalSubTot = new javax.swing.JLabel();
        jLabelLunchSub = new javax.swing.JLabel();
        jLabelDinnerSub = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
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
        jSeparator3 = new javax.swing.JSeparator();
        jLabelBreakFastSubTot = new javax.swing.JLabel();
        jLabelAcqLunchSubTot = new javax.swing.JLabel();
        jLabelAcqDinnerSubTot = new javax.swing.JLabel();
        jLabelAcqIncidentalSubTot = new javax.swing.JLabel();
        jLabelAllReq = new javax.swing.JLabel();
        jLabelAllAcq = new javax.swing.JLabel();
        jLabelAllBal = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jLabelAcqBreakFastSubTotBal = new javax.swing.JLabel();
        jLabelAcqLunchSubTotBal = new javax.swing.JLabel();
        jLabelAcqDinnerSubTotBal = new javax.swing.JLabel();
        jLabelAcqIncidentalSubTotBal = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabelMiscSubTot = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabelMscSub = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jLabelAcqMiscSubTot = new javax.swing.JLabel();
        jLabelMiscReq = new javax.swing.JLabel();
        jLabelMiscAcq = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jLabelMscBal = new javax.swing.JLabel();
        jLabelAcqMiscSubTotBal = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabelAccUnprovedSubTot = new javax.swing.JLabel();
        jLabelAccProvedSub = new javax.swing.JLabel();
        jLabelAccProvedSubTot = new javax.swing.JLabel();
        jLabelAccUnprovedSub = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabelAcqAccUnprovedSubTotBal = new javax.swing.JLabel();
        jLabelAcqAccProvedSubTot = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        jLabelAccAcq = new javax.swing.JLabel();
        jLabelAccBal = new javax.swing.JLabel();
        jLabelAccReq = new javax.swing.JLabel();
        jLabelAcqAccUnprovedSubTot = new javax.swing.JLabel();
        jLabelAcqAccProvedSubTotBal = new javax.swing.JLabel();
        jLabelAcqAppTotReqCost = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabelAppTotReq = new javax.swing.JLabel();
        jLabelOffice = new javax.swing.JLabel();
        jLabelAcqAccNum = new javax.swing.JLabel();
        jLabelACC = new javax.swing.JLabel();
        jLabelNum = new javax.swing.JLabel();
        jLabelReqSerial = new javax.swing.JLabel();
        jLabelAcqActMainPurpose = new javax.swing.JLabel();
        jLabelAcqProvince = new javax.swing.JLabel();
        jLabelAcqOffice = new javax.swing.JLabel();
        jLabelAcqEmpNum = new javax.swing.JLabel();
        jLabelRegDate = new javax.swing.JLabel();
        jLabelEmp = new javax.swing.JLabel();
        jLabelAcqRegDate = new javax.swing.JLabel();
        jLabelAcqRefNum = new javax.swing.JLabel();
        jLabelAcqSerial = new javax.swing.JLabel();
        jLabelNumAcq = new javax.swing.JLabel();
        jLabelAcqYear = new javax.swing.JLabel();
        jLabelReqRefNum = new javax.swing.JLabel();
        jLabelAcqAmtPayBack = new javax.swing.JLabel();
        jLabelAcqPayBack = new javax.swing.JLabel();
        jLabelAct = new javax.swing.JLabel();
        jLabelActNam = new javax.swing.JLabel();
        jLabelReqYear = new javax.swing.JLabel();
        jLabelAppTotReq1 = new javax.swing.JLabel();
        jLabelAcqAppTotReqCost1 = new javax.swing.JLabel();
        jLabelSupApp = new javax.swing.JLabel();
        jLabelAcqAppTotPlannedCost = new javax.swing.JLabel();
        jLabelAppTotPlannedReq = new javax.swing.JLabel();
        jPanelRequest = new javax.swing.JPanel();
        jLabelLogo1 = new javax.swing.JLabel();
        jLabelHeaderLine = new javax.swing.JLabel();
        jLabelLineDate = new javax.swing.JLabel();
        jLabelLineTime = new javax.swing.JLabel();
        jLabellogged = new javax.swing.JLabel();
        jLabelLineLogNam = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableActivityReq = new javax.swing.JTable();
        jPanelAcquittal = new javax.swing.JPanel();
        jLabelLogo2 = new javax.swing.JLabel();
        jLabelHeaderLine1 = new javax.swing.JLabel();
        jLabelLineDate1 = new javax.swing.JLabel();
        jLabelLineTime1 = new javax.swing.JLabel();
        jCheckBoxAgreed = new javax.swing.JCheckBox();
        jCheckBoxDisAgree = new javax.swing.JCheckBox();
        jButtonSave = new javax.swing.JButton();
        jLabellogged1 = new javax.swing.JLabel();
        jLabelLineLogNam1 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTableActivityAcq = new javax.swing.JTable();
        jTabbedPaneAcqAtt = new javax.swing.JTabbedPane();
        jPanelReport = new javax.swing.JPanel();
        jPanelReportDetails = new javax.swing.JPanel();
        jLabelNamTravel = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextAreaNamTravel = new javax.swing.JTextArea();
        jPanelAttDocRep = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableMonDistAcquitalAttRep = new javax.swing.JTable();
        jLabelAttDocHeaderRep = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanelAcqELog = new javax.swing.JPanel();
        jLabelLogo8 = new javax.swing.JLabel();
        jLabelHeaderLine7 = new javax.swing.JLabel();
        jLabelLineDate7 = new javax.swing.JLabel();
        jLabelLineTime7 = new javax.swing.JLabel();
        jLabellogged7 = new javax.swing.JLabel();
        jLabelLineLogNam8 = new javax.swing.JLabel();
        jLabelAcqWk4 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTableTripDetails = new javax.swing.JTable();
        jPanelAcqDocAtt1 = new javax.swing.JPanel();
        jLabelLogo3 = new javax.swing.JLabel();
        jLabelHeaderLine2 = new javax.swing.JLabel();
        jLabelLineDate2 = new javax.swing.JLabel();
        jLabelLineTime2 = new javax.swing.JLabel();
        jLabellogged2 = new javax.swing.JLabel();
        jLabelLineLogNam7 = new javax.swing.JLabel();
        jScrollPaneAtt1 = new javax.swing.JScrollPane();
        jPanel9 = new javax.swing.JPanel();
        jLabelImgFile1 = new javax.swing.JLabel();
        jPanelAcqDocAtt2 = new javax.swing.JPanel();
        jLabelLogo4 = new javax.swing.JLabel();
        jLabelHeaderLine4 = new javax.swing.JLabel();
        jLabelLineDate4 = new javax.swing.JLabel();
        jLabelLineTime4 = new javax.swing.JLabel();
        jLabellogged4 = new javax.swing.JLabel();
        jLabelLineLogNam4 = new javax.swing.JLabel();
        jScrollPaneAtt2 = new javax.swing.JScrollPane();
        jPanel19 = new javax.swing.JPanel();
        jLabelImgFile2 = new javax.swing.JLabel();
        jPanelAcqDocAtt3 = new javax.swing.JPanel();
        jLabelLogo5 = new javax.swing.JLabel();
        jLabelHeaderLine5 = new javax.swing.JLabel();
        jLabelLineDate5 = new javax.swing.JLabel();
        jLabelLineTime5 = new javax.swing.JLabel();
        jLabellogged5 = new javax.swing.JLabel();
        jLabelLineLogNam5 = new javax.swing.JLabel();
        jScrollPaneAtt3 = new javax.swing.JScrollPane();
        jPanel21 = new javax.swing.JPanel();
        jLabelImgFile3 = new javax.swing.JLabel();
        jPanelAcqDocAtt4 = new javax.swing.JPanel();
        jLabelLogo6 = new javax.swing.JLabel();
        jLabelHeaderLine6 = new javax.swing.JLabel();
        jLabelLineDate6 = new javax.swing.JLabel();
        jLabelLineTime6 = new javax.swing.JLabel();
        jLabellogged6 = new javax.swing.JLabel();
        jLabelLineLogNam6 = new javax.swing.JLabel();
        jScrollPaneAtt4 = new javax.swing.JScrollPane();
        jPanel23 = new javax.swing.JPanel();
        jLabelImgFile4 = new javax.swing.JLabel();
        jPanelAcqDocAtt5 = new javax.swing.JPanel();
        jLabelLogo7 = new javax.swing.JLabel();
        jLabelHeaderLine3 = new javax.swing.JLabel();
        jLabelLineDate3 = new javax.swing.JLabel();
        jLabelLineTime3 = new javax.swing.JLabel();
        jLabellogged3 = new javax.swing.JLabel();
        jLabelLineLogNam3 = new javax.swing.JLabel();
        jScrollPaneAtt5 = new javax.swing.JScrollPane();
        jPanel26 = new javax.swing.JPanel();
        jLabelImgFile5 = new javax.swing.JLabel();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuNew = new javax.swing.JMenu();
        jMenuItemPlanPerDiem = new javax.swing.JMenuItem();
        jSeparator13 = new javax.swing.JPopupMenu.Separator();
        jMenuItemPerDiemAcquittal = new javax.swing.JMenuItem();
        jSeparator16 = new javax.swing.JPopupMenu.Separator();
        jMenuEdit = new javax.swing.JMenu();
        jMenuMonPlanEdit = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        jMenuItemClose = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JPopupMenu.Separator();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuRequest = new javax.swing.JMenu();
        jMenuItemSupApp = new javax.swing.JMenuItem();
        jSeparator11 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAccMgrRev = new javax.swing.JMenuItem();
        jSeparator12 = new javax.swing.JPopupMenu.Separator();
        jMenuItemHeadApp = new javax.swing.JMenuItem();
        jSeparator14 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSchPerDiem = new javax.swing.JMenuItem();
        jSeparator23 = new javax.swing.JPopupMenu.Separator();
        jMenuItemView = new javax.swing.JMenuItem();
        jMenuAcquittal = new javax.swing.JMenu();
        jMenuItemAcqSupApp = new javax.swing.JMenuItem();
        jSeparator15 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqAccApp = new javax.swing.JMenuItem();
        jSeparator17 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqHeadApp = new javax.swing.JMenuItem();
        jSeparator18 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqSchGen = new javax.swing.JMenuItem();
        jSeparator19 = new javax.swing.JPopupMenu.Separator();
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
        jMenuItemUserProfUpd = new javax.swing.JMenuItem();

        jDialogDetUpd.setTitle("Detials Modification");
        jDialogDetUpd.setLocation(new java.awt.Point(200, 150));
        jDialogDetUpd.setMinimumSize(new java.awt.Dimension(890, 480));
        jDialogDetUpd.setResizable(false);

        jPanelDetUpd.setAlignmentX(5.0F);
        jPanelDetUpd.setAlignmentY(5.0F);
        jPanelDetUpd.setMinimumSize(new java.awt.Dimension(890, 480));
        jPanelDetUpd.setLayout(null);

        jLabelProvince1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelProvince1.setForeground(new java.awt.Color(0, 0, 255));
        jLabelProvince1.setText("BUDGET AND ACTIVITY MAIN PURPOSE MODIFICATION SCREEN");
        jPanelDetUpd.add(jLabelProvince1);
        jLabelProvince1.setBounds(140, 10, 630, 50);

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
        jPanelDetUpd.add(jComboBudgetCode);
        jComboBudgetCode.setBounds(140, 80, 630, 30);

        jLabelBudgetCode1.setText("Budget Code");
        jPanelDetUpd.add(jLabelBudgetCode1);
        jLabelBudgetCode1.setBounds(10, 80, 80, 30);

        jLabelActMainPurpose1.setText("Activity Main Purpose");
        jPanelDetUpd.add(jLabelActMainPurpose1);
        jLabelActMainPurpose1.setBounds(10, 200, 130, 30);

        jButtonDialogOk.setText("Update");
        jButtonDialogOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDialogOkActionPerformed(evt);
            }
        });
        jPanelDetUpd.add(jButtonDialogOk);
        jButtonDialogOk.setBounds(290, 300, 90, 30);

        jButtonDialogCancel.setLabel("Cancel");
        jButtonDialogCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDialogCancelActionPerformed(evt);
            }
        });
        jPanelDetUpd.add(jButtonDialogCancel);
        jButtonDialogCancel.setBounds(510, 300, 80, 30);
        jPanelDetUpd.add(jTextActMainPurpose);
        jTextActMainPurpose.setBounds(140, 200, 690, 30);

        jButtonUpdateBud.setBackground(new java.awt.Color(255, 0, 102));
        jButtonUpdateBud.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonUpdateBud.setForeground(new java.awt.Color(255, 255, 255));
        jButtonUpdateBud.setText("Upload");
        jButtonUpdateBud.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpdateBudActionPerformed(evt);
            }
        });
        jPanelDetUpd.add(jButtonUpdateBud);
        jButtonUpdateBud.setBounds(790, 80, 80, 30);

        jLabelBudUpd.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 2, true));
        jPanelDetUpd.add(jLabelBudUpd);
        jLabelBudUpd.setBounds(140, 130, 630, 30);

        jLabelBudCod.setText("Updated Budget Code");
        jPanelDetUpd.add(jLabelBudCod);
        jLabelBudCod.setBounds(10, 130, 130, 30);

        jButtonClearBud.setBackground(new java.awt.Color(0, 153, 0));
        jButtonClearBud.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonClearBud.setForeground(new java.awt.Color(255, 255, 255));
        jButtonClearBud.setText("Clear");
        jButtonClearBud.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearBudActionPerformed(evt);
            }
        });
        jPanelDetUpd.add(jButtonClearBud);
        jButtonClearBud.setBounds(790, 130, 80, 30);

        javax.swing.GroupLayout jDialogDetUpdLayout = new javax.swing.GroupLayout(jDialogDetUpd.getContentPane());
        jDialogDetUpd.getContentPane().setLayout(jDialogDetUpdLayout);
        jDialogDetUpdLayout.setHorizontalGroup(
            jDialogDetUpdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelDetUpd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogDetUpdLayout.setVerticalGroup(
            jDialogDetUpdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelDetUpd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        jDialogEmailComments.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jDialogEmailComments.setTitle("Rejection Comments");
        jDialogEmailComments.setAlwaysOnTop(true);
        jDialogEmailComments.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jDialogEmailComments.setLocation(new java.awt.Point(300, 150));
        jDialogEmailComments.setMinimumSize(new java.awt.Dimension(890, 480));
        jDialogEmailComments.setResizable(false);

        jPanelDetUpd1.setAlignmentX(5.0F);
        jPanelDetUpd1.setAlignmentY(5.0F);
        jPanelDetUpd1.setMinimumSize(new java.awt.Dimension(890, 480));
        jPanelDetUpd1.setLayout(null);

        jLabelRejectHeader.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelRejectHeader.setForeground(new java.awt.Color(204, 0, 0));
        jLabelRejectHeader.setText("PERDIEM ACQUITTAL REJECTION COMMENTS");
        jPanelDetUpd1.add(jLabelRejectHeader);
        jLabelRejectHeader.setBounds(200, 10, 450, 50);

        jLabelReasonDesc.setText("Please record reason for rejecting this acquittal. The applicant will recieve these comments.");
        jPanelDetUpd1.add(jLabelReasonDesc);
        jLabelReasonDesc.setBounds(190, 70, 560, 30);

        jLabelReasonReject.setText("Reason");
        jPanelDetUpd1.add(jLabelReasonReject);
        jLabelReasonReject.setBounds(20, 140, 120, 30);

        jTextReasonReject.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextReasonRejectKeyTyped(evt);
            }
        });
        jPanelDetUpd1.add(jTextReasonReject);
        jTextReasonReject.setBounds(150, 140, 690, 30);

        jButtonReasonDialogOk.setBackground(new java.awt.Color(0, 51, 51));
        jButtonReasonDialogOk.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonReasonDialogOk.setForeground(new java.awt.Color(51, 153, 0));
        jButtonReasonDialogOk.setText("Continue");
        jButtonReasonDialogOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReasonDialogOkActionPerformed(evt);
            }
        });
        jPanelDetUpd1.add(jButtonReasonDialogOk);
        jButtonReasonDialogOk.setBounds(270, 200, 120, 30);

        jButtonReasonDialogCancel.setBackground(new java.awt.Color(0, 51, 51));
        jButtonReasonDialogCancel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonReasonDialogCancel.setForeground(new java.awt.Color(255, 0, 0));
        jButtonReasonDialogCancel.setText("Cancel");
        jButtonReasonDialogCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReasonDialogCancelActionPerformed(evt);
            }
        });
        jPanelDetUpd1.add(jButtonReasonDialogCancel);
        jButtonReasonDialogCancel.setBounds(460, 200, 120, 30);

        javax.swing.GroupLayout jDialogEmailCommentsLayout = new javax.swing.GroupLayout(jDialogEmailComments.getContentPane());
        jDialogEmailComments.getContentPane().setLayout(jDialogEmailCommentsLayout);
        jDialogEmailCommentsLayout.setHorizontalGroup(
            jDialogEmailCommentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelDetUpd1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogEmailCommentsLayout.setVerticalGroup(
            jDialogEmailCommentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelDetUpd1, javax.swing.GroupLayout.PREFERRED_SIZE, 307, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PERDIEM ACQUITTAL - SUPERVISOR APPROVAL");
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        jTabbedPaneAppSys.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        jPanelGen.setBackground(new java.awt.Color(100, 100, 247));
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
        jLabelHeaderGen.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelGen.add(jLabelHeaderGen);
        jLabelHeaderGen.setBounds(350, 40, 610, 40);

        jLabelGenDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelGen.add(jLabelGenDate);
        jLabelGenDate.setBounds(1080, 0, 110, 30);

        jLabelGenTime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelGen.add(jLabelGenTime);
        jLabelGenTime.setBounds(1220, 0, 80, 30);

        jLabelGenLogNam1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam1.setText("Logged In");
        jPanelGen.add(jLabelGenLogNam1);
        jLabelGenLogNam1.setBounds(1080, 40, 100, 30);

        jLabelGenLogNam.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelGenLogNam);
        jLabelGenLogNam.setBounds(1155, 40, 190, 30);

        jLabelEmpNum.setText("Employee Number");
        jPanelGen.add(jLabelEmpNum);
        jLabelEmpNum.setBounds(20, 200, 110, 30);

        jLabelAcqEmpNam.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqEmpNam);
        jLabelAcqEmpNam.setBounds(370, 200, 340, 30);
        jPanelGen.add(jSeparator1);
        jSeparator1.setBounds(10, 190, 1340, 2);

        jLabelSepDetails.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelSepDetails.setForeground(new java.awt.Color(0, 0, 204));
        jLabelSepDetails.setText("Employee Details");
        jPanelGen.add(jLabelSepDetails);
        jLabelSepDetails.setBounds(20, 167, 140, 30);

        jLabelProvince.setText("Province");
        jPanelGen.add(jLabelProvince);
        jLabelProvince.setBounds(20, 230, 70, 30);

        jLabelBank.setText("Bank");
        jPanelGen.add(jLabelBank);
        jLabelBank.setBounds(20, 260, 40, 30);

        jLabelAcqBankName.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqBankName);
        jLabelAcqBankName.setBounds(130, 260, 220, 30);
        jPanelGen.add(jSeparator2);
        jSeparator2.setBounds(10, 340, 1340, 2);

        jLabelAcqEmpTitle.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqEmpTitle);
        jLabelAcqEmpTitle.setBounds(750, 200, 400, 30);

        jLabelFinDetails.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelFinDetails.setForeground(new java.awt.Color(0, 0, 204));
        jLabelFinDetails.setText("Financial Details");
        jPanelGen.add(jLabelFinDetails);
        jLabelFinDetails.setBounds(20, 310, 110, 30);

        jLabelActMainPurposeDesc.setText("Activity Main Purpose");
        jPanelGen.add(jLabelActMainPurposeDesc);
        jLabelActMainPurposeDesc.setBounds(20, 380, 130, 30);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPanel1FocusGained(evt);
            }
        });
        jPanel1.setLayout(null);

        jLabelIncidentalSub.setText("Incidental");
        jPanel1.add(jLabelIncidentalSub);
        jLabelIncidentalSub.setBounds(10, 120, 60, 25);

        jLabelIncidentalSubTot.setText("0.00");
        jPanel1.add(jLabelIncidentalSubTot);
        jLabelIncidentalSubTot.setBounds(100, 120, 50, 25);

        jLabelLunchSub.setText("Lunch");
        jPanel1.add(jLabelLunchSub);
        jLabelLunchSub.setBounds(10, 60, 60, 25);

        jLabelDinnerSub.setText("Dinner");
        jPanel1.add(jLabelDinnerSub);
        jLabelDinnerSub.setBounds(10, 90, 60, 25);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel2.setLayout(null);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel17.setText("Allowances Totals ");
        jPanel2.add(jLabel17);
        jLabel17.setBounds(10, 10, 120, 20);

        jLabel18.setText("Incidental");
        jPanel2.add(jLabel18);
        jLabel18.setBounds(10, 130, 60, 20);

        jLabel19.setText("Breakfast");
        jPanel2.add(jLabel19);
        jLabel19.setBounds(10, 40, 60, 20);

        jLabel20.setText("Lunch");
        jPanel2.add(jLabel20);
        jLabel20.setBounds(10, 70, 60, 20);

        jLabel21.setText("Dinner");
        jPanel2.add(jLabel21);
        jLabel21.setBounds(10, 100, 60, 20);

        jPanel1.add(jPanel2);
        jPanel2.setBounds(20, 410, 320, 160);

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel22.setText("Allowances ");
        jPanel1.add(jLabel22);
        jLabel22.setBounds(8, 5, 80, 25);

        jLabelBreakFastSub.setText("Breakfast");
        jPanel1.add(jLabelBreakFastSub);
        jLabelBreakFastSub.setBounds(10, 30, 60, 25);

        jLabelAcqBreakFastSubTot.setForeground(new java.awt.Color(255, 0, 0));
        jLabelAcqBreakFastSubTot.setText("0.00");
        jPanel1.add(jLabelAcqBreakFastSubTot);
        jLabelAcqBreakFastSubTot.setBounds(170, 30, 50, 25);

        jLabelLunchSubTot.setText("0.00");
        jPanel1.add(jLabelLunchSubTot);
        jLabelLunchSubTot.setBounds(100, 60, 50, 25);

        jLabelDinnerSubTot.setText("0.00");
        jPanel1.add(jLabelDinnerSubTot);
        jLabelDinnerSubTot.setBounds(100, 90, 50, 25);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel1.add(jSeparator3);
        jSeparator3.setBounds(150, 20, 5, 120);

        jLabelBreakFastSubTot.setText("0.00");
        jPanel1.add(jLabelBreakFastSubTot);
        jLabelBreakFastSubTot.setBounds(100, 30, 50, 25);

        jLabelAcqLunchSubTot.setForeground(new java.awt.Color(255, 51, 51));
        jLabelAcqLunchSubTot.setText("0.00");
        jPanel1.add(jLabelAcqLunchSubTot);
        jLabelAcqLunchSubTot.setBounds(170, 60, 50, 25);

        jLabelAcqDinnerSubTot.setForeground(new java.awt.Color(255, 51, 51));
        jLabelAcqDinnerSubTot.setText("0.00");
        jPanel1.add(jLabelAcqDinnerSubTot);
        jLabelAcqDinnerSubTot.setBounds(170, 90, 50, 25);

        jLabelAcqIncidentalSubTot.setForeground(new java.awt.Color(255, 51, 51));
        jLabelAcqIncidentalSubTot.setText("0.00");
        jPanel1.add(jLabelAcqIncidentalSubTot);
        jLabelAcqIncidentalSubTot.setBounds(170, 120, 50, 25);

        jLabelAllReq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAllReq.setForeground(new java.awt.Color(0, 102, 0));
        jLabelAllReq.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelAllReq.setText("Req");
        jPanel1.add(jLabelAllReq);
        jLabelAllReq.setBounds(82, 5, 60, 25);

        jLabelAllAcq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAllAcq.setForeground(new java.awt.Color(255, 0, 0));
        jLabelAllAcq.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelAllAcq.setText("Acq");
        jPanel1.add(jLabelAllAcq);
        jLabelAllAcq.setBounds(161, 5, 50, 25);

        jLabelAllBal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAllBal.setText("Balance");
        jPanel1.add(jLabelAllBal);
        jLabelAllBal.setBounds(240, 5, 60, 25);

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel1.add(jSeparator6);
        jSeparator6.setBounds(220, 20, 2, 120);

        jLabelAcqBreakFastSubTotBal.setText("0.00");
        jPanel1.add(jLabelAcqBreakFastSubTotBal);
        jLabelAcqBreakFastSubTotBal.setBounds(230, 30, 50, 25);

        jLabelAcqLunchSubTotBal.setText("0.00");
        jPanel1.add(jLabelAcqLunchSubTotBal);
        jLabelAcqLunchSubTotBal.setBounds(230, 60, 50, 25);

        jLabelAcqDinnerSubTotBal.setText("0.00");
        jPanel1.add(jLabelAcqDinnerSubTotBal);
        jLabelAcqDinnerSubTotBal.setBounds(230, 90, 50, 25);

        jLabelAcqIncidentalSubTotBal.setText("0.00");
        jPanel1.add(jLabelAcqIncidentalSubTotBal);
        jLabelAcqIncidentalSubTotBal.setBounds(230, 120, 50, 25);

        jPanelGen.add(jPanel1);
        jPanel1.setBounds(30, 430, 310, 150);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(null);

        jLabelMiscSubTot.setText("0.00");
        jPanel3.add(jLabelMiscSubTot);
        jLabelMiscSubTot.setBounds(110, 30, 50, 25);

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel29.setText("Miscellaneous ");
        jPanel3.add(jLabel29);
        jLabel29.setBounds(8, 5, 90, 25);

        jLabelMscSub.setText("Miscellaneous");
        jPanel3.add(jLabelMscSub);
        jLabelMscSub.setBounds(8, 30, 80, 25);

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel3.add(jSeparator4);
        jSeparator4.setBounds(168, 20, 5, 120);

        jLabelAcqMiscSubTot.setForeground(new java.awt.Color(255, 51, 51));
        jLabelAcqMiscSubTot.setText("0.00");
        jPanel3.add(jLabelAcqMiscSubTot);
        jLabelAcqMiscSubTot.setBounds(180, 30, 50, 25);

        jLabelMiscReq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelMiscReq.setForeground(new java.awt.Color(0, 102, 0));
        jLabelMiscReq.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelMiscReq.setText("Req");
        jPanel3.add(jLabelMiscReq);
        jLabelMiscReq.setBounds(110, 5, 50, 25);

        jLabelMiscAcq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelMiscAcq.setForeground(new java.awt.Color(255, 0, 0));
        jLabelMiscAcq.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelMiscAcq.setText("Acq");
        jPanel3.add(jLabelMiscAcq);
        jLabelMiscAcq.setBounds(170, 5, 60, 25);

        jSeparator7.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel3.add(jSeparator7);
        jSeparator7.setBounds(230, 20, 2, 120);

        jLabelMscBal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelMscBal.setText("Balance");
        jPanel3.add(jLabelMscBal);
        jLabelMscBal.setBounds(250, 5, 50, 25);

        jLabelAcqMiscSubTotBal.setForeground(new java.awt.Color(51, 51, 51));
        jLabelAcqMiscSubTotBal.setText("0.00");
        jPanel3.add(jLabelAcqMiscSubTotBal);
        jLabelAcqMiscSubTotBal.setBounds(250, 30, 50, 25);

        jPanelGen.add(jPanel3);
        jPanel3.setBounds(380, 430, 310, 150);

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

        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel4.add(jSeparator5);
        jSeparator5.setBounds(160, 20, 5, 120);

        jLabelAcqAccUnprovedSubTotBal.setText("0.00");
        jPanel4.add(jLabelAcqAccUnprovedSubTotBal);
        jLabelAcqAccUnprovedSubTotBal.setBounds(250, 30, 50, 25);

        jLabelAcqAccProvedSubTot.setForeground(new java.awt.Color(255, 51, 51));
        jLabelAcqAccProvedSubTot.setText("0.00");
        jPanel4.add(jLabelAcqAccProvedSubTot);
        jLabelAcqAccProvedSubTot.setBounds(170, 60, 50, 25);

        jSeparator8.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel4.add(jSeparator8);
        jSeparator8.setBounds(240, 20, 5, 120);

        jLabelAccAcq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAccAcq.setForeground(new java.awt.Color(255, 51, 51));
        jLabelAccAcq.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelAccAcq.setText("Acq");
        jPanel4.add(jLabelAccAcq);
        jLabelAccAcq.setBounds(170, 5, 60, 25);

        jLabelAccBal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAccBal.setText("Balance");
        jPanel4.add(jLabelAccBal);
        jLabelAccBal.setBounds(252, 5, 60, 25);

        jLabelAccReq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAccReq.setForeground(new java.awt.Color(0, 102, 0));
        jLabelAccReq.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelAccReq.setText("Req");
        jPanel4.add(jLabelAccReq);
        jLabelAccReq.setBounds(110, 5, 50, 25);

        jLabelAcqAccUnprovedSubTot.setForeground(new java.awt.Color(255, 51, 51));
        jLabelAcqAccUnprovedSubTot.setText("0.00");
        jPanel4.add(jLabelAcqAccUnprovedSubTot);
        jLabelAcqAccUnprovedSubTot.setBounds(170, 30, 50, 25);

        jLabelAcqAccProvedSubTotBal.setText("0.00");
        jPanel4.add(jLabelAcqAccProvedSubTotBal);
        jLabelAcqAccProvedSubTotBal.setBounds(250, 60, 50, 25);

        jPanelGen.add(jPanel4);
        jPanel4.setBounds(720, 430, 320, 150);

        jLabelAcqAppTotReqCost.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelAcqAppTotReqCost.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAcqAppTotReqCost.setText("0");
        jPanelGen.add(jLabelAcqAppTotReqCost);
        jLabelAcqAppTotReqCost.setBounds(1240, 520, 90, 30);

        jLabel35.setText("Miscellaneous");
        jPanelGen.add(jLabel35);
        jLabel35.setBounds(8, 30, 70, 25);

        jLabelAppTotReq.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelAppTotReq.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAppTotReq.setText("Total ");
        jPanelGen.add(jLabelAppTotReq);
        jLabelAppTotReq.setBounds(1050, 520, 180, 30);

        jLabelOffice.setText("Office");
        jPanelGen.add(jLabelOffice);
        jLabelOffice.setBounds(530, 230, 70, 30);

        jLabelAcqAccNum.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqAccNum);
        jLabelAcqAccNum.setBounds(660, 260, 130, 30);

        jLabelACC.setText("Account No.");
        jPanelGen.add(jLabelACC);
        jLabelACC.setBounds(530, 260, 80, 30);

        jLabelNum.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNum.setText("Request No.");
        jPanelGen.add(jLabelNum);
        jLabelNum.setBounds(20, 130, 90, 30);

        jLabelReqSerial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelReqSerial.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelReqSerial);
        jLabelReqSerial.setBounds(160, 130, 25, 30);

        jLabelAcqActMainPurpose.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqActMainPurpose);
        jLabelAcqActMainPurpose.setBounds(160, 380, 750, 30);

        jLabelAcqProvince.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqProvince);
        jLabelAcqProvince.setBounds(130, 230, 210, 30);

        jLabelAcqOffice.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqOffice);
        jLabelAcqOffice.setBounds(660, 230, 210, 30);

        jLabelAcqEmpNum.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqEmpNum);
        jLabelAcqEmpNum.setBounds(130, 200, 60, 30);

        jLabelRegDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelRegDate.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelRegDate);
        jLabelRegDate.setBounds(260, 130, 110, 30);
        jPanelGen.add(jLabelEmp);
        jLabelEmp.setBounds(1140, 80, 80, 30);

        jLabelAcqRegDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqRegDate.setForeground(new java.awt.Color(255, 0, 0));
        jPanelGen.add(jLabelAcqRegDate);
        jLabelAcqRegDate.setBounds(950, 130, 110, 30);

        jLabelAcqRefNum.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqRefNum.setForeground(new java.awt.Color(255, 0, 0));
        jPanelGen.add(jLabelAcqRefNum);
        jLabelAcqRefNum.setBounds(890, 130, 50, 30);

        jLabelAcqSerial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqSerial.setForeground(new java.awt.Color(255, 0, 0));
        jPanelGen.add(jLabelAcqSerial);
        jLabelAcqSerial.setBounds(840, 130, 25, 30);

        jLabelNumAcq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNumAcq.setText("Acquittal  No.");
        jPanelGen.add(jLabelNumAcq);
        jLabelNumAcq.setBounds(700, 130, 90, 30);

        jLabelAcqYear.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqYear.setForeground(new java.awt.Color(255, 0, 0));
        jPanelGen.add(jLabelAcqYear);
        jLabelAcqYear.setBounds(790, 130, 40, 30);

        jLabelReqRefNum.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelReqRefNum.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelReqRefNum);
        jLabelReqRefNum.setBounds(200, 130, 40, 30);

        jLabelAcqAmtPayBack.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabelAcqAmtPayBack.setForeground(new java.awt.Color(255, 180, 40));
        jPanelGen.add(jLabelAcqAmtPayBack);
        jLabelAcqAmtPayBack.setBounds(1270, 580, 60, 30);

        jLabelAcqPayBack.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabelAcqPayBack.setForeground(new java.awt.Color(255, 180, 40));
        jPanelGen.add(jLabelAcqPayBack);
        jLabelAcqPayBack.setBounds(1070, 580, 170, 30);

        jLabelAct.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        jLabelAct.setForeground(new java.awt.Color(153, 255, 0));
        jPanelGen.add(jLabelAct);
        jLabelAct.setBounds(1020, 130, 120, 20);

        jLabelActNam.setFont(new java.awt.Font("Tahoma", 2, 13)); // NOI18N
        jLabelActNam.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelActNam);
        jLabelActNam.setBounds(1150, 130, 190, 20);
        jPanelGen.add(jLabelReqYear);
        jLabelReqYear.setBounds(110, 130, 0, 30);

        jLabelAppTotReq1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelAppTotReq1.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAppTotReq1.setText("Total Cleared Week ");
        jPanelGen.add(jLabelAppTotReq1);
        jLabelAppTotReq1.setBounds(1050, 550, 180, 30);

        jLabelAcqAppTotReqCost1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelAcqAppTotReqCost1.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAcqAppTotReqCost1.setText("0");
        jPanelGen.add(jLabelAcqAppTotReqCost1);
        jLabelAcqAppTotReqCost1.setBounds(1240, 550, 90, 30);

        jLabelSupApp.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelSupApp.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSupApp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelSupApp.setText("Supervisor Approval");
        jLabelSupApp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelGen.add(jLabelSupApp);
        jLabelSupApp.setBounds(560, 90, 220, 20);

        jLabelAcqAppTotPlannedCost.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelAcqAppTotPlannedCost.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAcqAppTotPlannedCost.setText("0");
        jPanelGen.add(jLabelAcqAppTotPlannedCost);
        jLabelAcqAppTotPlannedCost.setBounds(1240, 490, 90, 30);

        jLabelAppTotPlannedReq.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelAppTotPlannedReq.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAppTotPlannedReq.setText("Total Planned Amount");
        jPanelGen.add(jLabelAppTotPlannedReq);
        jLabelAppTotPlannedReq.setBounds(1050, 490, 180, 30);

        jTabbedPaneAppSys.addTab("User and Accounting Details", jPanelGen);

        jPanelRequest.setBackground(new java.awt.Color(100, 100, 247));
        jPanelRequest.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jPanelRequest.setPreferredSize(new java.awt.Dimension(1280, 677));
        jPanelRequest.setLayout(null);

        jLabelLogo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelRequest.add(jLabelLogo1);
        jLabelLogo1.setBounds(10, 10, 220, 100);

        jLabelHeaderLine.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelRequest.add(jLabelHeaderLine);
        jLabelHeaderLine.setBounds(350, 40, 610, 40);

        jLabelLineDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate.setText("29 November 2018");
        jPanelRequest.add(jLabelLineDate);
        jLabelLineDate.setBounds(1060, 0, 110, 30);

        jLabelLineTime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime.setText("15:20:30");
        jPanelRequest.add(jLabelLineTime);
        jLabelLineTime.setBounds(1200, 0, 80, 30);

        jLabellogged.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged.setText("Logged In");
        jPanelRequest.add(jLabellogged);
        jLabellogged.setBounds(1060, 40, 80, 30);

        jLabelLineLogNam.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelRequest.add(jLabelLineLogNam);
        jLabelLineLogNam.setBounds(1155, 40, 190, 30);

        jTableActivityReq.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableActivityReq.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableActivityReqMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableActivityReq);

        jPanelRequest.add(jScrollPane1);
        jScrollPane1.setBounds(10, 150, 1340, 480);

        jTabbedPaneAppSys.addTab("Perdiem Request", jPanelRequest);

        jPanelAcquittal.setBackground(new java.awt.Color(100, 100, 247));
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
        jLabelLineDate1.setBounds(1070, 0, 110, 30);

        jLabelLineTime1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime1.setText("15:20:30");
        jPanelAcquittal.add(jLabelLineTime1);
        jLabelLineTime1.setBounds(1210, 0, 80, 30);

        buttonGroupAgreeCheck.add(jCheckBoxAgreed);
        jCheckBoxAgreed.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jCheckBoxAgreed.setText(" I Agree with the activities that are listed above. The application should be processed.");
        jCheckBoxAgreed.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jCheckBoxAgreedFocusGained(evt);
            }
        });
        jCheckBoxAgreed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxAgreedActionPerformed(evt);
            }
        });
        jPanelAcquittal.add(jCheckBoxAgreed);
        jCheckBoxAgreed.setBounds(10, 570, 560, 25);

        buttonGroupAgreeCheck.add(jCheckBoxDisAgree);
        jCheckBoxDisAgree.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jCheckBoxDisAgree.setText(" I DO NOT Agree with the activities that are listed above.Application REJECTED.");
        jCheckBoxDisAgree.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jCheckBoxDisAgreeFocusGained(evt);
            }
        });
        jCheckBoxDisAgree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDisAgreeActionPerformed(evt);
            }
        });
        jPanelAcquittal.add(jCheckBoxDisAgree);
        jCheckBoxDisAgree.setBounds(10, 600, 560, 25);

        jButtonSave.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonSave.setBorderPainted(false);
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        jPanelAcquittal.add(jButtonSave);
        jButtonSave.setBounds(610, 580, 80, 40);

        jLabellogged1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged1.setText("Logged In");
        jPanelAcquittal.add(jLabellogged1);
        jLabellogged1.setBounds(1070, 30, 80, 30);

        jLabelLineLogNam1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelAcquittal.add(jLabelLineLogNam1);
        jLabelLineLogNam1.setBounds(1170, 30, 180, 30);

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
        jScrollPane8.setViewportView(jTableActivityAcq);

        jPanelAcquittal.add(jScrollPane8);
        jScrollPane8.setBounds(0, 120, 1320, 440);

        jTabbedPaneAppSys.addTab("Perdiem Acquittal", jPanelAcquittal);

        jPanelReport.setLayout(null);

        jPanelReportDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelReportDetailsMouseClicked(evt);
            }
        });
        jPanelReportDetails.setLayout(null);

        jLabelNamTravel.setText("Who Travelled");
        jPanelReportDetails.add(jLabelNamTravel);
        jLabelNamTravel.setBounds(0, 0, 200, 30);

        jTextAreaNamTravel.setColumns(20);
        jTextAreaNamTravel.setRows(5);
        jScrollPane6.setViewportView(jTextAreaNamTravel);

        jPanelReportDetails.add(jScrollPane6);
        jScrollPane6.setBounds(0, 30, 1360, 96);

        jPanelAttDocRep.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanelAttDocRep.setLayout(null);

        jTableMonDistAcquitalAttRep.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Attachment Description", "Attachment File Category", "Attachement File Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableMonDistAcquitalAttRep.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableMonDistAcquitalAttRepMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTableMonDistAcquitalAttRep);

        jPanelAttDocRep.add(jScrollPane3);
        jScrollPane3.setBounds(10, 30, 1320, 270);

        jLabelAttDocHeaderRep.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelAttDocHeaderRep.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelAttDocHeaderRep.setText("ATTACHED DOCUMENTS ENTRY");
        jPanelAttDocRep.add(jLabelAttDocHeaderRep);
        jLabelAttDocHeaderRep.setBounds(10, 0, 1230, 30);

        jLabel3.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        jLabel3.setText("*NB: Double Click to View the PDF Document");
        jPanelAttDocRep.add(jLabel3);
        jLabel3.setBounds(10, 300, 450, 20);

        jPanelReportDetails.add(jPanelAttDocRep);
        jPanelAttDocRep.setBounds(0, 160, 1340, 340);

        jPanelReport.add(jPanelReportDetails);
        jPanelReportDetails.setBounds(0, 10, 1352, 610);

        jTabbedPaneAcqAtt.addTab("Activity Report", jPanelReport);

        jPanelAcqELog.setBackground(new java.awt.Color(0, 204, 255));
        jPanelAcqELog.setMinimumSize(new java.awt.Dimension(1270, 671));
        jPanelAcqELog.setLayout(null);

        jLabelLogo8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelAcqELog.add(jLabelLogo8);
        jLabelLogo8.setBounds(10, 10, 220, 100);

        jLabelHeaderLine7.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine7.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeaderLine7.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelAcqELog.add(jLabelHeaderLine7);
        jLabelHeaderLine7.setBounds(350, 40, 610, 40);

        jLabelLineDate7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate7.setText("29 November 2018");
        jPanelAcqELog.add(jLabelLineDate7);
        jLabelLineDate7.setBounds(1080, 0, 110, 30);

        jLabelLineTime7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime7.setText("15:20:30");
        jPanelAcqELog.add(jLabelLineTime7);
        jLabelLineTime7.setBounds(1220, 0, 80, 30);

        jLabellogged7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged7.setText("Logged In");
        jPanelAcqELog.add(jLabellogged7);
        jLabellogged7.setBounds(1080, 40, 80, 30);

        jLabelLineLogNam8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam8.setText("User Name");
        jPanelAcqELog.add(jLabelLineLogNam8);
        jLabelLineLogNam8.setBounds(1160, 40, 190, 30);

        jLabelAcqWk4.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        jLabelAcqWk4.setForeground(new java.awt.Color(0, 102, 51));
        jLabelAcqWk4.setText("jLabel2");
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
        jScrollPane7.setViewportView(jTableTripDetails);

        jPanelAcqELog.add(jScrollPane7);
        jScrollPane7.setBounds(0, 120, 1340, 490);

        jTabbedPaneAcqAtt.addTab("Attachment 1", jPanelAcqELog);

        jPanelAcqDocAtt1.setBackground(new java.awt.Color(226, 255, 255));
        jPanelAcqDocAtt1.setMinimumSize(new java.awt.Dimension(1270, 671));
        jPanelAcqDocAtt1.setLayout(null);

        jLabelLogo3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelAcqDocAtt1.add(jLabelLogo3);
        jLabelLogo3.setBounds(10, 10, 220, 100);

        jLabelHeaderLine2.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine2.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelAcqDocAtt1.add(jLabelHeaderLine2);
        jLabelHeaderLine2.setBounds(350, 40, 610, 40);

        jLabelLineDate2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate2.setText("29 November 2018");
        jPanelAcqDocAtt1.add(jLabelLineDate2);
        jLabelLineDate2.setBounds(1060, 0, 110, 30);

        jLabelLineTime2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime2.setText("15:20:30");
        jPanelAcqDocAtt1.add(jLabelLineTime2);
        jLabelLineTime2.setBounds(1200, 0, 80, 30);

        jLabellogged2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged2.setText("Logged In");
        jPanelAcqDocAtt1.add(jLabellogged2);
        jLabellogged2.setBounds(1060, 40, 80, 30);

        jLabelLineLogNam7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam7.setText("User Name");
        jPanelAcqDocAtt1.add(jLabelLineLogNam7);
        jLabelLineLogNam7.setBounds(1150, 40, 190, 30);

        jScrollPaneAtt1.setPreferredSize(new java.awt.Dimension(1000, 480));

        jPanel9.setPreferredSize(new java.awt.Dimension(998, 484));

        jLabelImgFile1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(387, Short.MAX_VALUE)
                .addComponent(jLabelImgFile1, javax.swing.GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE)
                .addGap(256, 256, 256))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabelImgFile1, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        jScrollPaneAtt1.setViewportView(jPanel9);

        jPanelAcqDocAtt1.add(jScrollPaneAtt1);
        jScrollPaneAtt1.setBounds(10, 115, 1340, 520);

        jTabbedPaneAcqAtt.addTab("Attachment 1", jPanelAcqDocAtt1);

        jPanelAcqDocAtt2.setBackground(new java.awt.Color(226, 255, 226));
        jPanelAcqDocAtt2.setMinimumSize(new java.awt.Dimension(1270, 671));
        jPanelAcqDocAtt2.setLayout(null);

        jLabelLogo4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogoOld (2).jpg"))); // NOI18N
        jPanelAcqDocAtt2.add(jLabelLogo4);
        jLabelLogo4.setBounds(10, 10, 220, 100);

        jLabelHeaderLine4.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine4.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelAcqDocAtt2.add(jLabelHeaderLine4);
        jLabelHeaderLine4.setBounds(350, 40, 610, 40);

        jLabelLineDate4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate4.setText("29 November 2018");
        jPanelAcqDocAtt2.add(jLabelLineDate4);
        jLabelLineDate4.setBounds(1060, 0, 110, 30);

        jLabelLineTime4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime4.setText("15:20:30");
        jPanelAcqDocAtt2.add(jLabelLineTime4);
        jLabelLineTime4.setBounds(1200, 0, 80, 30);

        jLabellogged4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged4.setText("Logged In");
        jPanelAcqDocAtt2.add(jLabellogged4);
        jLabellogged4.setBounds(1060, 40, 80, 30);

        jLabelLineLogNam4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam4.setText("User Name");
        jPanelAcqDocAtt2.add(jLabelLineLogNam4);
        jLabelLineLogNam4.setBounds(1150, 40, 190, 30);

        jScrollPaneAtt2.setPreferredSize(new java.awt.Dimension(1000, 480));

        jPanel19.setPreferredSize(new java.awt.Dimension(998, 484));

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(340, 340, 340)
                .addComponent(jLabelImgFile2, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
                .addContainerGap(308, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(jLabelImgFile2, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jScrollPaneAtt2.setViewportView(jPanel19);

        jPanelAcqDocAtt2.add(jScrollPaneAtt2);
        jScrollPaneAtt2.setBounds(0, 110, 1350, 520);

        jTabbedPaneAcqAtt.addTab("Attachment 2", jPanelAcqDocAtt2);

        jPanelAcqDocAtt3.setBackground(new java.awt.Color(255, 255, 204));
        jPanelAcqDocAtt3.setMinimumSize(new java.awt.Dimension(1270, 671));
        jPanelAcqDocAtt3.setLayout(null);

        jLabelLogo5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelAcqDocAtt3.add(jLabelLogo5);
        jLabelLogo5.setBounds(10, 10, 220, 100);

        jLabelHeaderLine5.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine5.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelAcqDocAtt3.add(jLabelHeaderLine5);
        jLabelHeaderLine5.setBounds(350, 40, 610, 40);

        jLabelLineDate5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate5.setText("29 November 2018");
        jPanelAcqDocAtt3.add(jLabelLineDate5);
        jLabelLineDate5.setBounds(1070, 0, 110, 30);

        jLabelLineTime5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime5.setText("15:20:30");
        jPanelAcqDocAtt3.add(jLabelLineTime5);
        jLabelLineTime5.setBounds(1210, 0, 80, 30);

        jLabellogged5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged5.setText("Logged In");
        jPanelAcqDocAtt3.add(jLabellogged5);
        jLabellogged5.setBounds(1070, 40, 80, 30);

        jLabelLineLogNam5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam5.setText("User Name");
        jPanelAcqDocAtt3.add(jLabelLineLogNam5);
        jLabelLineLogNam5.setBounds(1160, 40, 190, 30);

        jScrollPaneAtt3.setPreferredSize(new java.awt.Dimension(1000, 480));

        jPanel21.setPreferredSize(new java.awt.Dimension(998, 484));

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(323, 323, 323)
                .addComponent(jLabelImgFile3, javax.swing.GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE)
                .addContainerGap(320, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addComponent(jLabelImgFile3, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jScrollPaneAtt3.setViewportView(jPanel21);

        jPanelAcqDocAtt3.add(jScrollPaneAtt3);
        jScrollPaneAtt3.setBounds(0, 115, 1340, 520);

        jTabbedPaneAcqAtt.addTab("Attachment 3", jPanelAcqDocAtt3);

        jPanelAcqDocAtt4.setBackground(new java.awt.Color(227, 227, 249));
        jPanelAcqDocAtt4.setMinimumSize(new java.awt.Dimension(1270, 671));
        jPanelAcqDocAtt4.setLayout(null);

        jLabelLogo6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelAcqDocAtt4.add(jLabelLogo6);
        jLabelLogo6.setBounds(10, 10, 220, 100);

        jLabelHeaderLine6.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine6.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelAcqDocAtt4.add(jLabelHeaderLine6);
        jLabelHeaderLine6.setBounds(350, 40, 610, 40);

        jLabelLineDate6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate6.setText("29 November 2018");
        jPanelAcqDocAtt4.add(jLabelLineDate6);
        jLabelLineDate6.setBounds(1070, 0, 110, 30);

        jLabelLineTime6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime6.setText("15:20:30");
        jPanelAcqDocAtt4.add(jLabelLineTime6);
        jLabelLineTime6.setBounds(1210, 0, 80, 30);

        jLabellogged6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged6.setText("Logged In");
        jPanelAcqDocAtt4.add(jLabellogged6);
        jLabellogged6.setBounds(1070, 40, 80, 30);

        jLabelLineLogNam6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam6.setText("User Name");
        jPanelAcqDocAtt4.add(jLabelLineLogNam6);
        jLabelLineLogNam6.setBounds(1160, 40, 190, 30);

        jScrollPaneAtt4.setPreferredSize(new java.awt.Dimension(1000, 480));

        jPanel23.setPreferredSize(new java.awt.Dimension(998, 484));

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(317, 317, 317)
                .addComponent(jLabelImgFile4, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
                .addGap(251, 251, 251))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addComponent(jLabelImgFile4, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jScrollPaneAtt4.setViewportView(jPanel23);

        jPanelAcqDocAtt4.add(jScrollPaneAtt4);
        jScrollPaneAtt4.setBounds(10, 115, 1330, 520);

        jTabbedPaneAcqAtt.addTab("Attachment 4", jPanelAcqDocAtt4);

        jPanelAcqDocAtt5.setBackground(new java.awt.Color(255, 219, 255));
        jPanelAcqDocAtt5.setMinimumSize(new java.awt.Dimension(1270, 671));
        jPanelAcqDocAtt5.setLayout(null);

        jLabelLogo7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelAcqDocAtt5.add(jLabelLogo7);
        jLabelLogo7.setBounds(10, 10, 220, 100);

        jLabelHeaderLine3.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine3.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelAcqDocAtt5.add(jLabelHeaderLine3);
        jLabelHeaderLine3.setBounds(350, 40, 610, 40);

        jLabelLineDate3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate3.setText("29 November 2018");
        jPanelAcqDocAtt5.add(jLabelLineDate3);
        jLabelLineDate3.setBounds(1060, 0, 110, 30);

        jLabelLineTime3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime3.setText("15:20:30");
        jPanelAcqDocAtt5.add(jLabelLineTime3);
        jLabelLineTime3.setBounds(1200, 0, 80, 30);

        jLabellogged3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged3.setText("Logged In");
        jPanelAcqDocAtt5.add(jLabellogged3);
        jLabellogged3.setBounds(1060, 40, 80, 30);

        jLabelLineLogNam3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam3.setText("User Name");
        jPanelAcqDocAtt5.add(jLabelLineLogNam3);
        jLabelLineLogNam3.setBounds(1150, 40, 190, 30);

        jScrollPaneAtt5.setPreferredSize(new java.awt.Dimension(1000, 480));

        jPanel26.setPreferredSize(new java.awt.Dimension(998, 484));

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGap(328, 328, 328)
                .addComponent(jLabelImgFile5, javax.swing.GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE)
                .addContainerGap(315, Short.MAX_VALUE))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addComponent(jLabelImgFile5, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jScrollPaneAtt5.setViewportView(jPanel26);

        jPanelAcqDocAtt5.add(jScrollPaneAtt5);
        jScrollPaneAtt5.setBounds(0, 115, 1340, 520);

        jTabbedPaneAcqAtt.addTab("Attachment 5", jPanelAcqDocAtt5);

        jTabbedPaneAppSys.addTab("Report & Attachments", jTabbedPaneAcqAtt);

        jMenuFile.setText("File");
        jMenuFile.setPreferredSize(new java.awt.Dimension(60, 19));
        jMenuFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuFileActionPerformed(evt);
            }
        });

        jMenuNew.setText("New");

        jMenuItemPlanPerDiem.setText("Plan - Per Diems");
        jMenuItemPlanPerDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPlanPerDiemActionPerformed(evt);
            }
        });
        jMenuNew.add(jMenuItemPlanPerDiem);
        jMenuNew.add(jSeparator13);

        jMenuItemPerDiemAcquittal.setText("Acquittal - Per Diem ");
        jMenuItemPerDiemAcquittal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPerDiemAcquittalActionPerformed(evt);
            }
        });
        jMenuNew.add(jMenuItemPerDiemAcquittal);

        jMenuFile.add(jMenuNew);
        jMenuFile.add(jSeparator16);

        jMenuEdit.setText("Edit");

        jMenuMonPlanEdit.setText("Per Diem Monthly Plan");
        jMenuMonPlanEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuMonPlanEditActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuMonPlanEdit);

        jMenuFile.add(jMenuEdit);
        jMenuFile.add(jSeparator9);

        jMenuItemClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemClose.setText("Close");
        jMenuItemClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemClose);
        jMenuFile.add(jSeparator10);

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
        jMenuRequest.add(jSeparator11);

        jMenuItemAccMgrRev.setText("Finance Team Approval");
        jMenuItemAccMgrRev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAccMgrRevActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemAccMgrRev);
        jMenuRequest.add(jSeparator12);

        jMenuItemHeadApp.setText("Project HOD Approval");
        jMenuItemHeadApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemHeadAppActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemHeadApp);
        jMenuRequest.add(jSeparator14);

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
        jMenuAcquittal.add(jSeparator15);

        jMenuItemAcqAccApp.setText("Finance Team Approval");
        jMenuItemAcqAccApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqAccAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqAccApp);
        jMenuAcquittal.add(jSeparator17);

        jMenuItemAcqHeadApp.setText("Project HOD Approval");
        jMenuItemAcqHeadApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqHeadAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqHeadApp);
        jMenuAcquittal.add(jSeparator18);

        jMenuItemAcqSchGen.setText("Acquittals Schedule Generation");
        jMenuItemAcqSchGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqSchGenActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqSchGen);
        jMenuAcquittal.add(jSeparator19);

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
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPaneAppSys, javax.swing.GroupLayout.DEFAULT_SIZE, 1340, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneAppSys, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1376, 739));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jPanelGenFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanelGenFocusGained

    }//GEN-LAST:event_jPanelGenFocusGained

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained

    }//GEN-LAST:event_formFocusGained

    private void jPanelGenComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_jPanelGenComponentAdded

    }//GEN-LAST:event_jPanelGenComponentAdded

    private void jPanelGenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPanelGenKeyPressed

    }//GEN-LAST:event_jPanelGenKeyPressed

    private void jPanel1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel1FocusGained

    }//GEN-LAST:event_jPanel1FocusGained


    private void jCheckBoxAgreedFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jCheckBoxAgreedFocusGained

        jCheckBoxAgreed.setForeground(Color.green);
        jCheckBoxDisAgree.setForeground(Color.black);
    }//GEN-LAST:event_jCheckBoxAgreedFocusGained

    private void jCheckBoxDisAgreeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jCheckBoxDisAgreeFocusGained

        jCheckBoxDisAgree.setForeground(Color.red);
        jCheckBoxAgreed.setForeground(Color.black);
    }//GEN-LAST:event_jCheckBoxDisAgreeFocusGained

    private void jCheckBoxAgreedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxAgreedActionPerformed

        if (jCheckBoxAgreed.isSelected()) {
            jButtonSave.setVisible(true);
            jButtonSave.setText("Accept");
            jButtonSave.setBackground(new java.awt.Color(102, 255, 51));
            jButtonSave.setForeground(new java.awt.Color(0, 0, 255));
            jCheckBoxDisAgree.setSelected(false);

        }
    }//GEN-LAST:event_jCheckBoxAgreedActionPerformed

    private void jComboBudgetCodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBudgetCodeFocusGained

    }//GEN-LAST:event_jComboBudgetCodeFocusGained

    private void jComboBudgetCodeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBudgetCodeMouseEntered

    }//GEN-LAST:event_jComboBudgetCodeMouseEntered

    private void jButtonDialogOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDialogOkActionPerformed
//        if (("".equals(jLabelBudUpd.getText())) && (!("".equals(jTextActMainPurpose.getText())))) {
//            jLabelAcqActMainPurpose.setText(jTextActMainPurpose.getText());
//            jLabelComments.setVisible(true);
//            jTextAreaComments.setVisible(true);
//        } 
//        else if ((!("".equals(jLabelBudUpd.getText()))) && (("".equals(jTextActMainPurpose.getText())))) {
//            jLabelAcqBudCode.setText(jLabelBudUpd.getText());
//            jLabelComments.setVisible(true);
//            jTextAreaComments.setVisible(true);
//        } else if ((!("".equals(jLabelBudUpd.getText()))) && (!("".equals(jTextActMainPurpose.getText())))) {
//            jLabelAcqBudCode.setText(jLabelBudUpd.getText());
//            jLabelComments.setVisible(true);
//            jTextAreaComments.setVisible(true);
//            jLabelAcqActMainPurpose.setText(jTextActMainPurpose.getText());
//        } else if ((("".equals(jLabelBudUpd.getText()))) && (("".equals(jTextActMainPurpose.getText())))) {
//            jLabelAcqBudCode.setText(jLabelAcqBudCode.getText());
//            jLabelAcqActMainPurpose.setText(jLabelAcqActMainPurpose.getText());
//            jLabelComments.setVisible(false);
//            jTextAreaComments.setVisible(false);
//        }
//
//        jDialogDetUpd.setVisible(false);
    }//GEN-LAST:event_jButtonDialogOkActionPerformed

    private void jButtonDialogCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDialogCancelActionPerformed
        jDialogDetUpd.setVisible(false);
    }//GEN-LAST:event_jButtonDialogCancelActionPerformed

    private void jButtonUpdateBudActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpdateBudActionPerformed
        jLabelBudUpd.setText(jComboBudgetCode.getSelectedItem().toString());
        jButtonUpdateBud.setVisible(false);
        jLabelBudUpd.setVisible(true);
        jLabelBudCod.setVisible(true);
        jButtonClearBud.setVisible(true);
    }//GEN-LAST:event_jButtonUpdateBudActionPerformed

    private void jButtonClearBudActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearBudActionPerformed
        jLabelBudUpd.setText("");
        jButtonUpdateBud.setVisible(true);
        jButtonClearBud.setVisible(false);
        jLabelBudUpd.setVisible(false);
        jLabelBudCod.setVisible(false);
    }//GEN-LAST:event_jButtonClearBudActionPerformed

    private void jMenuPlanApprovalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuPlanApprovalActionPerformed
       
    }//GEN-LAST:event_jMenuPlanApprovalActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        SaveSupDet();
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jCheckBoxDisAgreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxDisAgreeActionPerformed
        if (jCheckBoxDisAgree.isSelected()) {
            jButtonSave.setVisible(true);
            jButtonSave.setText("Decline");
            jButtonSave.setBackground(new java.awt.Color(255, 0, 0));
            jButtonSave.setForeground(new java.awt.Color(255, 255, 255));
            jCheckBoxAgreed.setSelected(false);

        }
    }//GEN-LAST:event_jCheckBoxDisAgreeActionPerformed

    private void jTextReasonRejectKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextReasonRejectKeyTyped
        jButtonReasonDialogOk.setVisible(true);
    }//GEN-LAST:event_jTextReasonRejectKeyTyped

    private void jTableActivityReqMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableActivityReqMouseClicked
        //        if (evt.getClickCount() == 2) {
        //            resetField();
        //            int row = jTableWk1Activities.getSelectedRow();
        //
        //            int col1 = 1;
        //            int col3 = 3;
        //            int col4 = 4;
        //            int col5 = 5;
        //            int col6 = 6;
        //            int col7 = 7;
        //            int col8 = 8;
        //            int col9 = 9;
        //            int col10 = 10;
        //            int col11 = 11;
        //            int col12 = 12;
        //            int col13 = 13;
        //            int col14 = 14;
        //            int col15 = 15;
        //            int col16 = 16;
        //            int col17 = 17;
        //            int col18 = 18;
        //            int col19 = 19;
        //
        //            Object id1 = jTableWk1Activities.getValueAt(row, col1);
        //            Object id2 = jTableWk1Activities.getValueAt(row, col3);
        //            Object id3 = jTableWk1Activities.getValueAt(row, col4);
        //            Object id4 = jTableWk1Activities.getValueAt(row, col5);
        //            Object id5 = jTableWk1Activities.getValueAt(row, col6);
        //            Object id6 = jTableWk1Activities.getValueAt(row, col7);
        //            Object id7 = jTableWk1Activities.getValueAt(row, col8);
        //            Object id8 = jTableWk1Activities.getValueAt(row, col9);
        //            Object id9 = jTableWk1Activities.getValueAt(row, col10);
        //            Object id10 = jTableWk1Activities.getValueAt(row, col11);
        //            Object id11 = jTableWk1Activities.getValueAt(row, col12);
        //            Object id12 = jTableWk1Activities.getValueAt(row, col13);
        //            Object id13 = jTableWk1Activities.getValueAt(row, col14);
        //            Object id14 = jTableWk1Activities.getValueAt(row, col15);
        //            Object id15 = jTableWk1Activities.getValueAt(row, col16);
        //            Object id16 = jTableWk1Activities.getValueAt(row, col17);
        //            Object id17 = jTableWk1Activities.getValueAt(row, col18);
        //            Object id18 = jTableWk1Activities.getValueAt(row, col19);
        //
        //            jLabelActWk1Date.setText(id1.toString());
        //            //            jLabelPrjNameDet.setText(id2.toString());
        //            //            jLabelPrjTaskDet.setText(id3.toString());
        //            jTextFieldDialogWk1Site.setText(id4.toString());
        //            jTextFieldWk1DialogActivityDesc.setText(id5.toString());
        //            jTextAreaWk1DialogJustification.setText(id6.toString());
        //            //            jTextFieldWk1DialogStaffName1
        //            //            jLabelStaff.setText(id7.toString());
        //
        //            if ((Double.parseDouble(id7.toString())) > 0) {
        //                jCheckBoxDialogWk1BrkFast.setSelected(true);
        //            }
        //            if ((Double.parseDouble(id8.toString())) > 0) {
        //                jCheckBoxDialogWk1Lunch.setSelected(true);
        //            }
        //            if ((Double.parseDouble(id9.toString())) > 0) {
        //                jCheckBoxDialogWk1Dinner.setSelected(true);
        //            }
        //
        //            if ((Double.parseDouble(id10.toString())) > 0) {
        //                jCheckBoxDialogWk1Inc.setSelected(true);
        //            }
        //
        //            if ((Double.parseDouble(id12.toString())) > 0) {
        //                jCheckBoxDialogWk1Misc.setSelected(true);
        //                jTextFieldWk1Misc.setText(id11.toString());
        //                jTextFieldWk1MiscAmt.setText(id12.toString());
        //            }
        //
        //            if ((Double.parseDouble(id13.toString())) > 0) {
        //                jCheckBoxDialogWk1AccUnProved.setSelected(true);
        //                jCheckBoxDialogWk1AccProved.setSelected(false);
        //                jCheckBoxNoAcc.setSelected(false);
        //
        //            }
        //            if ((Double.parseDouble(id14.toString())) > 0) {
        //                jCheckBoxDialogWk1AccProved.setSelected(true);
        //                jCheckBoxNoAcc.setSelected(false);
        //                jCheckBoxDialogWk1AccUnProved.setSelected(false);
        //
        //            }
        //            if (((Double.parseDouble(id13.toString())) == 0) && ((Double.parseDouble(id14.toString())) == 0)) {
        //                jCheckBoxNoAcc.setSelected(true);
        //                jCheckBoxDialogWk1AccUnProved.setSelected(false);
        //                jCheckBoxDialogWk1AccProved.setSelected(false);
        //
        //            }
        //            jTextFieldWk1DialogStaffName1.setText(id15.toString());
        //            jTextFieldWk1DialogStaffName2.setText(id16.toString());
        //            jTextFieldWk1DialogStaffName3.setText(id17.toString());
        //            jTextFieldWk1DialogStaffName4.setText(id18.toString());
        //
        //            jDialogWk1.setVisible(true);
        //            findProject(id2.toString());
        //            findTask(id3.toString());
        //            if (("NATIONAL".equals(jLabelDistrict.getText()))) {
        //                jDialogWk1.setTitle("Per Diem Week 1");
        //            } else {
        //                jDialogWk1.setTitle("Month Per Diem ");
        //            }
        //            jTextAreaWk1DialogJustification.setLineWrap(true);
        //            jTextAreaWk1DialogJustification.setWrapStyleWord(true);
        //            jTextFieldDialogWk1Site.setEditable(false);
        //            jTextFieldWk1DialogActivityDesc.setEditable(false);
        //            jTextAreaWk1DialogJustification.setEditable(false);
        //            jTextFieldWk1DialogStaffName1.setEditable(false);
        //            jTextFieldWk1DialogStaffName2.setEditable(false);
        //            jTextFieldWk1DialogStaffName3.setEditable(false);
        //            jTextFieldWk1DialogStaffName4.setEditable(false);
        //            jTextFieldWk1Misc.setEditable(false);
        //            jTextFieldWk1MiscAmt.setEditable(false);
        //            jCheckBoxDialogWk1BrkFast.setEnabled(false);
        //            jCheckBoxDialogWk1Lunch.setEnabled(false);
        //            jCheckBoxDialogWk1Dinner.setEnabled(false);
        //            jCheckBoxDialogWk1Inc.setEnabled(false);
        //            jCheckBoxDialogWk1AccUnProved.setEnabled(false);
        //            jCheckBoxDialogWk1AccProved.setEnabled(false);
        //            jCheckBoxNoAcc.setEnabled(false);
        //            jCheckBoxDialogWk1Misc.setEnabled(false);
        //
        //        }
    }//GEN-LAST:event_jTableActivityReqMouseClicked

    private void jTableActivityAcqMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableActivityAcqMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableActivityAcqMouseClicked

    private void jButtonReasonDialogOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReasonDialogOkActionPerformed
        if ("".equals(jTextReasonReject.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter reason for rejection before proceeding");

            jTextReasonReject.requestFocusInWindow();
            jTextReasonReject.setFocusable(true);
        } else {
            jDialogEmailComments.setVisible(false);

            dbUpdRec();
            updateRejComments();

        }
    }//GEN-LAST:event_jButtonReasonDialogOkActionPerformed

    private void jButtonReasonDialogCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReasonDialogCancelActionPerformed
        jDialogEmailComments.setVisible(false);
        jTextReasonReject.setText("");
        jCheckBoxDisAgree.setSelected(false);
        jButtonSave.setVisible(false);
    }//GEN-LAST:event_jButtonReasonDialogCancelActionPerformed

    private void jMenuItemPlanPerDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPlanPerDiemActionPerformed
        new JFrameMnthPlanPerDiemCreate(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemPlanPerDiemActionPerformed

    private void jMenuItemPerDiemAcquittalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPerDiemAcquittalActionPerformed
        new JFrameAppAcquittal(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemPerDiemAcquittalActionPerformed

    private void jMenuMonPlanEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuMonPlanEditActionPerformed
        new JFrameMnthPlanPerDiemEdit(jLabelEmp.getText()).setVisible(true);
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

    private void jTableMonDistAcquitalAttRepMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableMonDistAcquitalAttRepMouseClicked
        if (evt.getClickCount() == 2) {
            connSaveFile pdfRetrive = new connSaveFile();
            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                        + "DataBaseName=ClaimsAppSys;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
                int row = jTableMonDistAcquitalAttRep.getSelectedRow();

                int col = 0;
                int col1 = 2;

                Object id = jTableMonDistAcquitalAttRep.getValueAt(row, col);
                Object id1 = jTableMonDistAcquitalAttRep.getValueAt(row, col1);

                String refItmNum = jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + id.toString();
                String pdfName = id1.toString();

                pdfRetrive.getPDFData4(conn, refItmNum, pdfName);

            } catch (Exception e) {
                System.out.println(e);
            }

        }
    }//GEN-LAST:event_jTableMonDistAcquitalAttRepMouseClicked

    private void jPanelReportDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelReportDetailsMouseClicked

    }//GEN-LAST:event_jPanelReportDetailsMouseClicked

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
            java.util.logging.Logger.getLogger(JFrameAppSupAcquittal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameAppSupAcquittal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameAppSupAcquittal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameAppSupAcquittal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new JFrameAppSupAcquittal().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupAccAcq;
    private javax.swing.ButtonGroup buttonGroupAgreeCheck;
    private javax.swing.JButton jButtonClearBud;
    private javax.swing.JButton jButtonDialogCancel;
    private javax.swing.JButton jButtonDialogOk;
    private javax.swing.JButton jButtonReasonDialogCancel;
    private javax.swing.JButton jButtonReasonDialogOk;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonUpdateBud;
    private javax.swing.JCheckBox jCheckBoxAgreed;
    private javax.swing.JCheckBox jCheckBoxDisAgree;
    private javax.swing.JComboBox<String> jComboBudgetCode;
    private javax.swing.JDialog jDialogDetUpd;
    private javax.swing.JDialog jDialogEmailComments;
    private javax.swing.JDialog jDialogWaitingEmail;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabelACC;
    private javax.swing.JLabel jLabelAccAcq;
    private javax.swing.JLabel jLabelAccBal;
    private javax.swing.JLabel jLabelAccProvedSub;
    private javax.swing.JLabel jLabelAccProvedSubTot;
    private javax.swing.JLabel jLabelAccReq;
    private javax.swing.JLabel jLabelAccUnprovedSub;
    private javax.swing.JLabel jLabelAccUnprovedSubTot;
    private javax.swing.JLabel jLabelAcqAccNum;
    private javax.swing.JLabel jLabelAcqAccProvedSubTot;
    private javax.swing.JLabel jLabelAcqAccProvedSubTotBal;
    private javax.swing.JLabel jLabelAcqAccUnprovedSubTot;
    private javax.swing.JLabel jLabelAcqAccUnprovedSubTotBal;
    private javax.swing.JLabel jLabelAcqActMainPurpose;
    private javax.swing.JLabel jLabelAcqAmtPayBack;
    private javax.swing.JLabel jLabelAcqAppTotPlannedCost;
    private javax.swing.JLabel jLabelAcqAppTotReqCost;
    private javax.swing.JLabel jLabelAcqAppTotReqCost1;
    private javax.swing.JLabel jLabelAcqBankName;
    private javax.swing.JLabel jLabelAcqBreakFastSubTot;
    private javax.swing.JLabel jLabelAcqBreakFastSubTotBal;
    private javax.swing.JLabel jLabelAcqDinnerSubTot;
    private javax.swing.JLabel jLabelAcqDinnerSubTotBal;
    private javax.swing.JLabel jLabelAcqEmpNam;
    private javax.swing.JLabel jLabelAcqEmpNum;
    private javax.swing.JLabel jLabelAcqEmpTitle;
    private javax.swing.JLabel jLabelAcqIncidentalSubTot;
    private javax.swing.JLabel jLabelAcqIncidentalSubTotBal;
    private javax.swing.JLabel jLabelAcqLunchSubTot;
    private javax.swing.JLabel jLabelAcqLunchSubTotBal;
    private javax.swing.JLabel jLabelAcqMiscSubTot;
    private javax.swing.JLabel jLabelAcqMiscSubTotBal;
    private javax.swing.JLabel jLabelAcqOffice;
    private javax.swing.JLabel jLabelAcqPayBack;
    private javax.swing.JLabel jLabelAcqProvince;
    private javax.swing.JLabel jLabelAcqRefNum;
    private javax.swing.JLabel jLabelAcqRegDate;
    private javax.swing.JLabel jLabelAcqSerial;
    private javax.swing.JLabel jLabelAcqWk4;
    private javax.swing.JLabel jLabelAcqYear;
    private javax.swing.JLabel jLabelAct;
    private javax.swing.JLabel jLabelActMainPurpose1;
    private javax.swing.JLabel jLabelActMainPurposeDesc;
    private javax.swing.JLabel jLabelActNam;
    private javax.swing.JLabel jLabelAllAcq;
    private javax.swing.JLabel jLabelAllBal;
    private javax.swing.JLabel jLabelAllReq;
    private javax.swing.JLabel jLabelAppTotPlannedReq;
    private javax.swing.JLabel jLabelAppTotReq;
    private javax.swing.JLabel jLabelAppTotReq1;
    private javax.swing.JLabel jLabelAttDocHeaderRep;
    private javax.swing.JLabel jLabelBank;
    private javax.swing.JLabel jLabelBreakFastSub;
    private javax.swing.JLabel jLabelBreakFastSubTot;
    private javax.swing.JLabel jLabelBudCod;
    private javax.swing.JLabel jLabelBudUpd;
    private javax.swing.JLabel jLabelBudgetCode1;
    private javax.swing.JLabel jLabelDinnerSub;
    private javax.swing.JLabel jLabelDinnerSubTot;
    private javax.swing.JLabel jLabelEmp;
    private javax.swing.JLabel jLabelEmpNum;
    private javax.swing.JLabel jLabelFinDetails;
    private javax.swing.JLabel jLabelGenDate;
    private javax.swing.JLabel jLabelGenLogNam;
    private javax.swing.JLabel jLabelGenLogNam1;
    private javax.swing.JLabel jLabelGenTime;
    private javax.swing.JLabel jLabelHeaderGen;
    private javax.swing.JLabel jLabelHeaderLine;
    private javax.swing.JLabel jLabelHeaderLine1;
    private javax.swing.JLabel jLabelHeaderLine2;
    private javax.swing.JLabel jLabelHeaderLine3;
    private javax.swing.JLabel jLabelHeaderLine4;
    private javax.swing.JLabel jLabelHeaderLine5;
    private javax.swing.JLabel jLabelHeaderLine6;
    private javax.swing.JLabel jLabelHeaderLine7;
    private javax.swing.JLabel jLabelImgFile1;
    private javax.swing.JLabel jLabelImgFile2;
    private javax.swing.JLabel jLabelImgFile3;
    private javax.swing.JLabel jLabelImgFile4;
    private javax.swing.JLabel jLabelImgFile5;
    private javax.swing.JLabel jLabelIncidentalSub;
    private javax.swing.JLabel jLabelIncidentalSubTot;
    private javax.swing.JLabel jLabelLineDate;
    private javax.swing.JLabel jLabelLineDate1;
    private javax.swing.JLabel jLabelLineDate2;
    private javax.swing.JLabel jLabelLineDate3;
    private javax.swing.JLabel jLabelLineDate4;
    private javax.swing.JLabel jLabelLineDate5;
    private javax.swing.JLabel jLabelLineDate6;
    private javax.swing.JLabel jLabelLineDate7;
    private javax.swing.JLabel jLabelLineLogNam;
    private javax.swing.JLabel jLabelLineLogNam1;
    private javax.swing.JLabel jLabelLineLogNam3;
    private javax.swing.JLabel jLabelLineLogNam4;
    private javax.swing.JLabel jLabelLineLogNam5;
    private javax.swing.JLabel jLabelLineLogNam6;
    private javax.swing.JLabel jLabelLineLogNam7;
    private javax.swing.JLabel jLabelLineLogNam8;
    private javax.swing.JLabel jLabelLineTime;
    private javax.swing.JLabel jLabelLineTime1;
    private javax.swing.JLabel jLabelLineTime2;
    private javax.swing.JLabel jLabelLineTime3;
    private javax.swing.JLabel jLabelLineTime4;
    private javax.swing.JLabel jLabelLineTime5;
    private javax.swing.JLabel jLabelLineTime6;
    private javax.swing.JLabel jLabelLineTime7;
    private javax.swing.JLabel jLabelLogo;
    private javax.swing.JLabel jLabelLogo1;
    private javax.swing.JLabel jLabelLogo2;
    private javax.swing.JLabel jLabelLogo3;
    private javax.swing.JLabel jLabelLogo4;
    private javax.swing.JLabel jLabelLogo5;
    private javax.swing.JLabel jLabelLogo6;
    private javax.swing.JLabel jLabelLogo7;
    private javax.swing.JLabel jLabelLogo8;
    private javax.swing.JLabel jLabelLunchSub;
    private javax.swing.JLabel jLabelLunchSubTot;
    private javax.swing.JLabel jLabelMiscAcq;
    private javax.swing.JLabel jLabelMiscReq;
    private javax.swing.JLabel jLabelMiscSubTot;
    private javax.swing.JLabel jLabelMscBal;
    private javax.swing.JLabel jLabelMscSub;
    private javax.swing.JLabel jLabelNamTravel;
    private javax.swing.JLabel jLabelNum;
    private javax.swing.JLabel jLabelNumAcq;
    private javax.swing.JLabel jLabelOffice;
    private javax.swing.JLabel jLabelProvince;
    private javax.swing.JLabel jLabelProvince1;
    private javax.swing.JLabel jLabelReasonDesc;
    private javax.swing.JLabel jLabelReasonReject;
    private javax.swing.JLabel jLabelRegDate;
    private javax.swing.JLabel jLabelRejectHeader;
    private javax.swing.JLabel jLabelReqRefNum;
    private javax.swing.JLabel jLabelReqSerial;
    private javax.swing.JLabel jLabelReqYear;
    private javax.swing.JLabel jLabelSepDetails;
    private javax.swing.JLabel jLabelSupApp;
    private javax.swing.JLabel jLabellogged;
    private javax.swing.JLabel jLabellogged1;
    private javax.swing.JLabel jLabellogged2;
    private javax.swing.JLabel jLabellogged3;
    private javax.swing.JLabel jLabellogged4;
    private javax.swing.JLabel jLabellogged5;
    private javax.swing.JLabel jLabellogged6;
    private javax.swing.JLabel jLabellogged7;
    private javax.swing.JMenu jMenuAcquittal;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenu jMenuEdit;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenuItem jMenuItemAccMgrRev;
    private javax.swing.JMenuItem jMenuItemAcqAccApp;
    private javax.swing.JMenuItem jMenuItemAcqHeadApp;
    private javax.swing.JMenuItem jMenuItemAcqSchGen;
    private javax.swing.JMenuItem jMenuItemAcqSupApp;
    private javax.swing.JMenuItem jMenuItemAcqView;
    private javax.swing.JMenuItem jMenuItemClose;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemHeadApp;
    private javax.swing.JMenuItem jMenuItemPerDiemAcquittal;
    private javax.swing.JMenuItem jMenuItemPlanFinApproval;
    private javax.swing.JMenuItem jMenuItemPlanHODApproval;
    private javax.swing.JMenuItem jMenuItemPlanPerDiem;
    private javax.swing.JMenuItem jMenuItemPlanSupApproval;
    private javax.swing.JMenuItem jMenuItemPlanView;
    private javax.swing.JMenuItem jMenuItemReqGenLst;
    private javax.swing.JMenuItem jMenuItemReqSubmitted;
    private javax.swing.JMenuItem jMenuItemSchPerDiem;
    private javax.swing.JMenuItem jMenuItemSupApp;
    private javax.swing.JMenuItem jMenuItemUserProfUpd;
    private javax.swing.JMenuItem jMenuItemView;
    private javax.swing.JMenuItem jMenuMonPlanEdit;
    private javax.swing.JMenu jMenuMonthlyPlan;
    private javax.swing.JMenu jMenuNew;
    private javax.swing.JMenu jMenuPlanApproval;
    private javax.swing.JMenu jMenuReports;
    private javax.swing.JMenu jMenuRequest;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelAcqDocAtt1;
    private javax.swing.JPanel jPanelAcqDocAtt2;
    private javax.swing.JPanel jPanelAcqDocAtt3;
    private javax.swing.JPanel jPanelAcqDocAtt4;
    private javax.swing.JPanel jPanelAcqDocAtt5;
    private javax.swing.JPanel jPanelAcqELog;
    private javax.swing.JPanel jPanelAcquittal;
    private javax.swing.JPanel jPanelAttDocRep;
    private javax.swing.JPanel jPanelDetUpd;
    private javax.swing.JPanel jPanelDetUpd1;
    private javax.swing.JPanel jPanelGen;
    private javax.swing.JPanel jPanelReport;
    private javax.swing.JPanel jPanelReportDetails;
    private javax.swing.JPanel jPanelRequest;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPaneAtt1;
    private javax.swing.JScrollPane jScrollPaneAtt2;
    private javax.swing.JScrollPane jScrollPaneAtt3;
    private javax.swing.JScrollPane jScrollPaneAtt4;
    private javax.swing.JScrollPane jScrollPaneAtt5;
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
    private javax.swing.JPopupMenu.Separator jSeparator22;
    private javax.swing.JPopupMenu.Separator jSeparator23;
    private javax.swing.JPopupMenu.Separator jSeparator26;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator34;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator62;
    private javax.swing.JPopupMenu.Separator jSeparator63;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    private javax.swing.JTabbedPane jTabbedPaneAcqAtt;
    public javax.swing.JTabbedPane jTabbedPaneAppSys;
    private javax.swing.JTable jTableActivityAcq;
    private javax.swing.JTable jTableActivityReq;
    private javax.swing.JTable jTableMonDistAcquitalAttRep;
    private javax.swing.JTable jTableTripDetails;
    private javax.swing.JTextField jTextActMainPurpose;
    private javax.swing.JTextArea jTextAreaNamTravel;
    private javax.swing.JTextField jTextReasonReject;
    // End of variables declaration//GEN-END:variables
}
