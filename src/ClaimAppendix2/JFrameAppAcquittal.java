/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClaimAppendix2;

import ClaimPlanPerDiem.JFrameMnthPlanPerDiemEdit;
import ClaimPlanPerDiem.JFrameMnthPlanPerDiemCreate;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import static java.lang.Math.abs;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.sql.SQLException;
import java.awt.Color;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.connCred;
import utils.timeHost;
import utils.connCred;
import utils.connSaveFile;
import utils.savePDFToDB;
import utils.StockVehicleMgt;
import utils.emailSend;

/**
 *
 * @author cgoredema
 */
public class JFrameAppAcquittal extends javax.swing.JFrame {

    timeHost tH = new timeHost();
    connCred c = new connCred();
    emailSend emSend = new emailSend();
    connSaveFile attL = new connSaveFile();
    savePDFToDB pdfDB = new savePDFToDB();
    PreparedStatement pst = null;
    PreparedStatement pst1 = null;
    int itmNum = 1;
    int month, finyear, count, newAcqRefNum, maxAcqRefNum, bankAccLen;
    int numsearchRef = 0;
    int clickCount = 0;
    Date curYear = new Date();
    int checkRefCount = 0;
    int genCount = 0;
    int itmCount = 0;
    int wfCount = 0;
    int docAttDoc = 0;
    int refCheck = 0;
    int sqlError = 0;
    int minWkNum = 1;
    int maxWkNum = 1;
    int countChkWkNum = 0;
    int existCountWkNum = 0;
    int docVerPlan = 0;
    int vehCount = 0;
    double allActTot = 0;
    double wk1Amt = 0;
    double wk2Amt = 0;
    double wk3Amt = 0;
    double wk4Amt = 0;
    SimpleDateFormat df = new SimpleDateFormat("yyyy");
    SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
    DefaultTableModel model, modelAcq, modelTrip, modelAtt;
    String sendTo, createUsrNam, supUsrMail, breakfastAll, lunchAll, lunchNPAll, lunchPAll, dinnerAll,
            incidentalAll, unProvedAll, provedAll, date1, date2, usrnam, searchRef, authUsrNam, authUsrNamAll,
            authNam1, authNam2, usrGrp, empNum, empNam, oldRefNum, oldRegBudCode, acqSta, lowDate,
            empOff, branchCode, prjCode, taskCode, donorCode, grantCode, accCode, prjProgCode, donor, budLine, subBudLine,
            donorName, accCodeName, prjCodeName, prjProgCodeName, budLineName, budcode, taskDonor, supNam, supEmpNum,
            accomodation, empAccNum;
    String dayRegAfter = "2020-01-01";
    String depSlip = "N";
    String str = "";
    String payRec = "N";
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

    String filename = null;
    byte[] person_image = null;
    String filename2 = null;
    byte[] person_image2 = null;
    String filename3 = null;
    byte[] person_image3 = null;
    String filename4 = null;
    byte[] person_image4 = null;
    String filename5 = null;
    byte[] person_image5 = null;

    /**
     * Creates new form JFrameTabApp1
     */
    public JFrameAppAcquittal() {
        initComponents();
        //    showDate();
        showTime();
        computerName();
        allowanceRate();

        model = (DefaultTableModel) jTableActivityReq.getModel();
        modelAcq = (DefaultTableModel) jTableActivityAcq.getModel();
        modelTrip = (DefaultTableModel) jTableTripDetails.getModel();
        modelAtt = (DefaultTableModel) jTableAcquittalDocAtt.getModel();
//        jTableTripDetails.getColumnModel().getColumn(1).setMinWidth(0);
//        jTableTripDetails.getColumnModel().getColumn(1).setMaxWidth(0);

        jMenuItemSubmit.setVisible(false);
        jTextAttDocFilePath.setVisible(false);
        jSeparator25.setVisible(false);
        //  jLabelUnprovedAcq.setVisible(false);
        jToggleButtonNoActivity.setText("All Activities Not Done");
        jToggleButtonAllActivities.setText("All Activities Done As Per Request");
        jTabbedPaneAcqAtt.setTitleAt(0, "Activity Summary Report");
        jTabbedPaneAcqAtt.setTitleAt(1, "E-Log Book");
        jTabbedPaneAcqAtt.setTitleAt(2, "Vehicle Log Sheet");
        jTabbedPaneAcqAtt.setTitleAt(3, "Refund Deposit Slip");
        jTabbedPaneAcqAtt.setTitleAt(4, "Other e.g. Log Book Extra Page");
        jButtonChooseAtt1.setText("Select Log Sheet");
        jButtonDeleteAtt1.setText("Delete Log Sheet");
        jButtonChooseAtt2.setText("Select Refund Deposit Slip");
        jButtonDeleteAtt2.setText("Delete Refund Deposit Slip");
        jButtonChooseAtt3.setText("Select Other Documents");
        jButtonDeleteAtt3.setText("Delete Other Documents");
        jLabelVehReg.setText("<html>Vehicle Reg No. </html>");
//        jTableTripDetails.getColumnModel().getColumn(1).setMinWidth(0);
//        jTableTripDetails.getColumnModel().getColumn(1).setMaxWidth(0);

//      
    }

    public JFrameAppAcquittal(String usrLogNum) {
        initComponents();
        try {
            // showDate();
            tH.intShowDate();
        } catch (IOException ex) {
            Logger.getLogger(JFrameAppAcquittal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(JFrameAppAcquittal.class.getName()).log(Level.SEVERE, null, ex);
        }
        jLabelGenDate.setText(tH.internetDate);
        jLabelLineDate.setText(tH.internetDate);
        jLabelLineDate1.setText(tH.internetDate);
        jLabelLineDate2.setText(tH.internetDate);
        jLabelLineDate3.setText(tH.internetDate);
        jLabelLineDate4.setText(tH.internetDate);
        jLabelLineDate5.setText(tH.internetDate);
        showTime();
        computerName();
        model = (DefaultTableModel) jTableActivityReq.getModel();
        modelAcq = (DefaultTableModel) jTableActivityAcq.getModel();
        modelTrip = (DefaultTableModel) jTableTripDetails.getModel();
        modelAtt = (DefaultTableModel) jTableAcquittalDocAtt.getModel();
        jLabelAppWk1TotReq.setVisible(false);
        jLabelAcqAppWk1TotReqCost.setVisible(false);
        jLabelAppWk2TotReq.setVisible(false);
        jLabelAcqAppWk2TotReqCost.setVisible(false);
        jLabelAppWk3TotReq.setVisible(false);
        jLabelAcqAppWk3TotReqCost.setVisible(false);
        jLabelAppWk4TotReq.setVisible(false);
        jLabelAcqAppWk4TotReqCost.setVisible(false);
        jLabelAppWk5TotReq.setVisible(false);
        jLabelAcqAppWk5TotReqCost.setVisible(false);
        jTextAreaNamTravel.setLineWrap(true);
        jTextAreaNamTravel.setWrapStyleWord(true);
        jLabelEmp.setText(usrLogNum);
        jLabelEmp.setVisible(false);
        jTextAttDocFilePath.setVisible(false);
        jToggleButtonNoActivity.setText("All Activities Not Done");
        jToggleButtonAllActivities.setText("All Activities Done As Per Request");
        jTabbedPaneAcqAtt.setTitleAt(0, "Activity Summary Report");
        jTabbedPaneAcqAtt.setTitleAt(1, "E-Log Book");
        jTabbedPaneAcqAtt.setTitleAt(2, "Vehicle Log Sheet");
        jTabbedPaneAcqAtt.setTitleAt(3, "Refund Deposit Slip");
        jTabbedPaneAcqAtt.setTitleAt(4, "Other e.g. Log Book Extra Page");
        jButtonChooseAtt1.setText("Select Log Sheet");
        jButtonDeleteAtt1.setText("Delete Log Sheet");
        jButtonChooseAtt2.setText("Select Refund Deposit Slip");
        jButtonDeleteAtt2.setText("Delete Refund Deposit Slip");
        jButtonChooseAtt3.setText("Select Other Documents");
        jButtonDeleteAtt3.setText("Delete Other Documents");
        jLabelVehReg.setText("<html>Vehicle Reg No. </html>");
        jTabbedPaneAcqAtt.setEnabledAt(1, false);
//        jTableTripDetails.getColumnModel().getColumn(1).setMinWidth(0);
//        jTableTripDetails.getColumnModel().getColumn(1).setMaxWidth(0);

        findUser();
        findUserGrp();
        findBankName();
        findProvince();

        allowanceRate();

        if (!"Administrator".equals(usrGrp)) {
            jMenuItemUserProfUpd.setEnabled(false);
            jMenuItemUserCreate.setEnabled(false);
        }

    }
    String hostName = "";

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

            }
        }) {

        }.start();

    }

    void allowanceRate() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT Lunch,Dinner,Incidental,Unproved_Accommodation "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAllowanceTab] ");

            while (r.next()) {

                lunchAll = r.getString(1);
                dinnerAll = r.getString(2);
                incidentalAll = r.getString(3);
                unProvedAll = r.getString(4);

            }
            //                 conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void computerName() {

        try {
            java.net.InetAddress addr = InetAddress.getLocalHost();
            hostName = addr.getHostName();
            //  JOptionPane.showMessageDialog(this, "CurBal" + curBalDays + "Days" + Double.parseDouble(jLabelDays1.getText())

        } catch (Exception ex) {
            System.out.println("Hostname can not be resolved");
        }
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

    void findAccCode() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            jComboAccountCode.setSelectedIndex(-1);

            ResultSet r = st.executeQuery("SELECT concat(ACC_CODE,' ',ACC_DESC) FROM [ClaimsAppSysZvandiri].[dbo].[BudAccCodTab] order by 1 desc");

            while (r.next()) {

                jComboAccountCode.addItem(r.getString(1));

            }

            conn.close();
        } catch (Exception e) {
            System.out.println();
        }
    }

    void findDonorCode() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            jComboDonor.setSelectedIndex(-1);

            ResultSet r = st.executeQuery("SELECT distinct concat(DONOR_CODE,' ',DONOR_DESC) FROM [ClaimsAppSysZvandiri].[dbo].[BudDonPrjTab] order by 1");

            while (r.next()) {

                jComboDonor.addItem(r.getString(1));

            }

            conn.close();
        } catch (Exception e) {
            System.out.println();
        }
    }

    void findPrjProgCode() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            jComboProjectCodeProgramming.setSelectedIndex(-1);

            ResultSet r = st.executeQuery("SELECT distinct concat(PRJ_CODE_PROG,' ',PRJ_PROG_DESC) "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[BudPrjProgTab] order by 1");

            while (r.next()) {

                jComboProjectCodeProgramming.addItem(r.getString(1));

            }

            conn.close();
        } catch (Exception e) {
            System.out.println();
        }
    }

    void findPrjCode(String donorCode) {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            int itemCount = jComboProjectCodeGL.getItemCount();

            for (int i = 0; i < itemCount; i++) {
                jComboProjectCodeGL.removeItemAt(0);
            }

            Statement st = conn.createStatement();
//            jComboBudMainCode.setSelectedIndex(-1);

            ResultSet r = st.executeQuery("SELECT distinct concat(PRJ_CODE,' ',PRJ_DESC) FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[BudDonPrjTab] where DONOR_CODE = '" + donorCode + "' order by 1");

            while (r.next()) {

                jComboProjectCodeGL.addItem(r.getString(1));
            }

            conn.close();
        } catch (Exception e) {
            System.out.println();
        }
    }

    void findGrantBud() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT GRANT_CODE  FROM [ClaimsAppSysZvandiri].[dbo].[BudGrantTab]");

            while (r.next()) {

                grantCode = r.getString(1);

            }

            ResultSet r1 = st1.executeQuery("SELECT concat(BUD_CODE,' ',BUD_DESC) FROM [ClaimsAppSysZvandiri].[dbo].[BudMainCodTab] order by 1");

            while (r1.next()) {

                jComboBudMainCode.addItem(r1.getString(1));

            }

            conn.close();
        } catch (Exception e) {
            System.out.println();
        }
    }

    void facilityPOP() {
        jDialogFacility.setVisible(true);
        jDialogFacility.setVisible(false);
        jDialogFacility.setVisible(true);
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
                jLabelLineLogNam1.setText(r.getString(1));
                jLabelLineLogNam4.setText(r.getString(1));
                jLabelLineLogNam5.setText(r.getString(1));
                jLabelLineLogNam7.setText(r.getString(1));
                jLabelLineLogNam8.setText(r.getString(1));;
//                jLabelEmpNum.setText(jLabelEmp.getText());
                supEmpNum = r.getString(2);
                supNam = r.getString(3);
//                jLabelBankName.setText(r.getString(7));
                createUsrNam = r.getString(1);
                supUsrMail = r.getString(4);
                empOff = r.getString(5);

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

            ResultSet r = st.executeQuery("SELECT min(act_date) FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                    + "where concat(SERIAL,REF_NUM) = 'R" + jTextAcqRegNum.getText() + "' and ACT_REC_STA = 'A'");

            while (r.next()) {
                lowDate = r.getString(1);
            }

            //                 conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void getTripDetails() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();

            st.executeQuery("SELECT project,upper(vehicle_number),employee_number,upper(location),"
                    + " upper(end_trip_location),upper(trip_purpose),convert(varchar(10),date_started,23),"
                    + "convert(varchar(10),date_started,24), start_mileage,convert(varchar(10),"
                    + "date_ended,23),convert(varchar(10),date_ended,24),end_mileage,"
                    + " CAST(end_mileage AS  int) - CAST(start_mileage as int) "
                    + "FROM [OphidLogBook].[dbo].[vehicle_trips] where upper(vehicle_number) =upper('" + str + "') "
                    + "and convert(varchar(10),date_started,23) >= '" + dfDate.format(jDateTripFrom.getDate()) + "' "
                    + "and convert(varchar(10),date_ended,23) <='" + dfDate.format(jDateTripTo.getDate()) + "' "
                    + "order by CAST(start_mileage as int)");

            ResultSet r = st.getResultSet();
            while (r.next()) {
                modelTrip.insertRow(modelTrip.getRowCount(), new Object[]{r.getString(1), r.getString(2), r.getString(3),
                    r.getString(4), r.getString(5), r.getString(6), r.getString(7), r.getString(8), r.getString(9),
                    r.getString(10), r.getString(11), r.getString(12), r.getString(13)});

            }

            //                 conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findBankName() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            //  jComboBankNam.setSelectedIndex(-1);
            ResultSet r = st.executeQuery("SELECT ACCOUNT_NAME FROM [ClaimsAppSysZvandiri].[dbo].[refundBankAccTab]");
//jComboBankNam.removeAllItems();
            while (r.next()) {

                jComboBankNam.addItem(r.getString("ACCOUNT_NAME"));

            }

            jLabelBankAccNo.setText("");
            jTextPaidAmt.setText("0.00");

            //                 conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Connect to Database (Bank Name).Try Again",
                    "Error Connection", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
    }

    void findBankAcc() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            //  jComboBankNam.setSelectedIndex(-1);
            ResultSet r = st.executeQuery("SELECT ACCOUNT FROM [ClaimsAppSysZvandiri].[dbo].[refundBankAccTab] "
                    + "where ACCOUNT_NAME = '" + jComboBankNam.getSelectedItem().toString() + "'");

            while (r.next()) {

                jLabelBankAccNo.setText(r.getString(1));

            }

            //                 conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Connect to Database (Bank Name).Try Again",
                    "Error Connection", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
    }

    void refNumUpdate() {
        try {
            System.out.println("ref num " + jLabelRegNum.getText());
            String sql1 = "update [ClaimsAppSysZvandiri].[dbo].[refNumTab] set "
                    + "REF_NUM ='" + jLabelRegNum.getText() + "' where REF_YEAR ='" + finyear + "' and SERIAL = 'A'";

            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            pst = conn.prepareStatement(sql1);
            pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    void wk1Amt() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT CLEARED_AMT FROM [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab] "
                    + "where concat(PREV_SERIAL,PREV_REF_NUM) ='" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "' "
                    + "and PLAN_WK =1 and ACQ_STA='C'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                wk1Amt = r.getDouble(1);

            }

            jLabelAppWk1TotReq.setVisible(true);
            jLabelAcqAppWk1TotReqCost.setVisible(true);
            jLabelAppWk1TotReq.setText("Total Cleared Week 1");
            jLabelAcqAppWk1TotReqCost.setText(String.format("%.2f", wk1Amt));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void wk2Amt() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT CLEARED_AMT FROM [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab]"
                    + " where concat(PREV_SERIAL,PREV_REF_NUM) ='" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "' "
                    + "and PLAN_WK =2 and ACQ_STA='C'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                wk2Amt = r.getDouble(1);

            }
            jLabelAppWk2TotReq.setVisible(true);
            jLabelAcqAppWk2TotReqCost.setVisible(true);
            jLabelAppWk2TotReq.setText("Total Cleared Week 2");
            jLabelAcqAppWk2TotReqCost.setText(String.format("%.2f", wk2Amt));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void wk3Amt() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT CLEARED_AMT FROM [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab]"
                    + " where concat(PREV_SERIAL,PREV_REF_NUM) ='" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "' "
                    + "and PLAN_WK =3 and ACQ_STA='C'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                wk3Amt = r.getDouble(1);

            }
            jLabelAppWk3TotReq.setVisible(true);
            jLabelAcqAppWk3TotReqCost.setVisible(true);
            jLabelAppWk3TotReq.setText("Total Cleared Week 3");
            jLabelAcqAppWk3TotReqCost.setText(String.format("%.2f", wk3Amt));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void wk4Amt() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT CLEARED_AMT FROM [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab]"
                    + " where concat(PREV_SERIAL,PREV_REF_NUM) ='" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "' "
                    + "and PLAN_WK =4 and ACQ_STA='C'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                wk4Amt = r.getDouble(1);

            }
            jLabelAppWk4TotReq.setVisible(true);
            jLabelAcqAppWk4TotReqCost.setVisible(true);
            jLabelAppWk4TotReq.setText("Total Cleared Week 4");
            jLabelAcqAppWk4TotReqCost.setText(String.format("%.2f", wk4Amt));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void balAmtFinal() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();

            st1.executeQuery("SELECT ACT_TOT_AMT - b.CLEAR FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a "
                    + "join (SELECT PREV_SERIAL,PREV_REF_NUM,sum(CLEARED_AMT) 'CLEAR' "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab] where concat(PREV_SERIAL,PREV_REF_NUM) ="
                    + "  '" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "' "
                    + "and ACQ_STA = 'C' group by PREV_SERIAL,PREV_REF_NUM) b "
                    + "on a.SERIAL = b.PREV_SERIAL and a.REF_NUM = b.PREV_REF_NUM "
                    + "where concat(a.SERIAL,a.REF_NUM) = '" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "'"
                    + " and a.ACT_REC_STA = 'A'");

            ResultSet rs1 = st1.getResultSet();

            while (rs1.next()) {
                balAmt = rs1.getDouble(1);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void recMinMax() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();
            Statement st3 = conn.createStatement();
            Statement st4 = conn.createStatement();

            st1.executeQuery("SELECT min([PLAN_WK]),max([PLAN_WK]) FROM [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab]"
                    + " where concat(SERIAL,REF_NUM) =  'R" + jTextAcqRegNum.getText() + "' and ACQ_STA = 'A'");

            ResultSet rs1 = st1.getResultSet();

            while (rs1.next()) {

                minWkNum = Integer.parseInt(rs1.getString(1));
                maxWkNum = Integer.parseInt(rs1.getString(2));

            }

            st3.executeQuery("SELECT ACQ_STA,DOC_VER+1 FROM [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab]"
                    + " where concat(SERIAL,REF_NUM) =  'R" + jTextAcqRegNum.getText() + "' and ACQ_STA = 'A'");

            ResultSet rs3 = st3.getResultSet();

            while (rs3.next()) {
                acqSta = rs3.getString(1);
                docVerPlan = rs3.getInt(2);

            }

            st4.executeQuery("SELECT count(*) FROM [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab]"
                    + " where concat(PREV_SERIAL,PREV_REF_NUM) =  'R" + jTextAcqRegNum.getText() + "' and ACQ_STA = 'C'");

            ResultSet rs4 = st4.getResultSet();

            while (rs4.next()) {
                existCountWkNum = Integer.parseInt(rs4.getString(1));
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void refreshFields() {
        jTextAcqRegNum.setText("");
        jLabelAcqEmpNum.setText("");
        jLabelRegDateAcq.setText("");
        jLabelRegNum.setText("");
        jLabelRegYear.setText("");
        jLabelSerialAcq.setText("");
        jLabelAcqEmpNam.setText("");
        jLabelAcqEmpTitle.setText("");
        jLabelAcqProvince.setText("");
        jLabelAcqOffice.setText("");
        jLabelAcqBankName.setText("");
        jLabelAcqActMainPurpose.setText("");
        jLabelAcqAccNum.setText("");

        DefaultTableModel dmAcq = (DefaultTableModel) jTableActivityAcq.getModel();
        while (dmAcq.getRowCount() > 0) {
            dmAcq.removeRow(0);
        }

        DefaultTableModel dm = (DefaultTableModel) jTableActivityReq.getModel();
        while (dm.getRowCount() > 0) {
            dm.removeRow(0);
        }
        jLabelBreakFastSubTot.setText("0.00");
        jLabelLunchSubTot.setText("0.00");
        jLabelDinnerSubTot.setText("0.00");
        jLabelIncidentalSubTot.setText("0.00");
        jLabelMiscSubTot.setText("0.00");
        jLabelAccUnprovedSubTot.setText("0.00");
        jLabelAccProvedSubTot.setText("0.00");
        jLabelAcqBreakFastSubTot.setText("0.00");
        jLabelAcqLunchSubTot.setText("0.00");
        jLabelAcqDinnerSubTot.setText("0.00");
        jLabelAcqIncidentalSubTot.setText("0.00");
        jLabelAcqMiscSubTot.setText("0.00");
        jLabelAcqAccUnprovedSubTot.setText("0.00");
        jLabelAcqAccProvedSubTot.setText("0.00");
        jLabelAcqBreakFastSubTotBal.setText("0.00");
        jLabelAcqLunchSubTotBal.setText("0.00");
        jLabelAcqDinnerSubTotBal.setText("0.00");
        jLabelAcqIncidentalSubTotBal.setText("0.00");
        jLabelAcqMiscSubTotBal.setText("0.00");
        jLabelAcqAccUnprovedSubTotBal.setText("0.00");
        jLabelAcqAccProvedSubTotBal.setText("0.00");
        jLabelAcqAppTotReqCost.setText("0.00");
        jLabelAcqAppWk1TotReqCost.setText("0.00");
        jLabelAcqAppWk2TotReqCost.setText("0.00");
        jLabelAcqAppWk3TotReqCost.setText("0.00");
        jTextAreaNamTravel.setText("");

    }

    void regInitCheck() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            st.executeQuery("SELECT COUNT(*) FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] "
                    + "where concat(PREV_SERIAL,PREV_REF_NUM) = 'R" + jTextAcqRegNum.getText() + "'"
                    + " and REF_NUM not in (select distinct REF_NUM from "
                    + "[ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] where DOC_STATUS "
                    + "  like '%Dis%' and SERIAL = 'A')");

            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                count = Integer.parseInt(rs.getString(1));

            }

            st1.executeQuery("SELECT count(*) FROM [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab]"
                    + " where concat(SERIAL,REF_NUM) =  'R" + jTextAcqRegNum.getText() + "' and ACQ_STA = 'A'");

            ResultSet rs1 = st1.getResultSet();

            while (rs1.next()) {
                countChkWkNum = Integer.parseInt(rs1.getString(1));
            }

            if ((count > 0) && (countChkWkNum == 0)) {
                refreshFields();
                JOptionPane.showMessageDialog(this, "Perdiem already acquitted or has been regstered for acquittal");
                jTextAcqRegNum.requestFocusInWindow();
                jTextAcqRegNum.setFocusable(true);
            } else {

                DefaultTableModel dm = (DefaultTableModel) jTableActivityReq.getModel();
                DefaultTableModel dmAcq = (DefaultTableModel) jTableActivityAcq.getModel();
                while (dm.getRowCount() > 0) {
                    dm.removeRow(0);
                }
                while (dmAcq.getRowCount() > 0) {
                    dmAcq.removeRow(0);
                }

                jTextAreaNamTravel.setText("");
                recMinMax();
                fetchdata();
                mainPageTotUpdate();
                findAccCode();
                findDonorCode();
                findGrantBud();
                findPrjProgCode();

            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    void initValues() {
        breakfastsubtotalAcq = 0;
        lunchsubtotalAcq = 0;
        dinnersubtotalAcq = 0;
        incidentalsubtotalAcq = 0;
        miscSubTotAcq = 0;
        provedSubTotAcq = 0;
        unprovedSubTotAcq = 0;
        breakfastsubtotal = 0;
        lunchsubtotal = 0;
        dinnersubtotal = 0;
        incidentalsubtotal = 0;
        miscSubTot = 0;
        provedSubTot = 0;
        unprovedSubTot = 0;
        allTotalAcq = 0;
        allTotal = 0;
    }

    void imgChooserFile1() {

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("images", "jpg", "gif", "png", "jpeg");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        filename = f.getAbsolutePath();
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (!(f.getName().endsWith(".jpg") || f.getName().endsWith(".gif") || f.getName().endsWith(".png") || f.getName().endsWith(".jpeg"))) {
                JOptionPane.showMessageDialog(null, "Please select file with .jpg, .bmp, .png, .jpeg and .gif extension only.");
            }
        }

        ImageIcon imageIcon = new ImageIcon(new ImageIcon(filename).getImage().getScaledInstance(jLabelImgFile1.getWidth(), jLabelImgFile1.getHeight(), Image.SCALE_SMOOTH));
        jLabelImgFile1.setIcon(imageIcon);

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
                jLabelImgFile1.setIcon(null);
            } else {
                person_image = bos.toByteArray();
            }

        } catch (Exception e) {
            System.out.println(e);

        }
    }

    void imgChooserFile2() {

        JFileChooser chooser2 = new JFileChooser();
        FileNameExtensionFilter filter2 = new FileNameExtensionFilter("images", "jpg", "gif", "png", "jpeg");
        chooser2.setFileFilter(filter2);
        int returnVal2 = chooser2.showOpenDialog(null);
        File f2 = chooser2.getSelectedFile();
        filename2 = f2.getAbsolutePath();
        if (returnVal2 == JFileChooser.APPROVE_OPTION) {
            if (!(f2.getName().endsWith(".jpg") || f2.getName().endsWith(".gif") || f2.getName().endsWith(".png") || f2.getName().endsWith(".jpeg"))) {
                JOptionPane.showMessageDialog(null, "Please select file with .jpg, .bmp, .png, .jpeg and .gif extension only.");
            }
        }

        ImageIcon imageIcon2 = new ImageIcon(new ImageIcon(filename2).getImage().getScaledInstance(jLabelImgFile2.getWidth(), jLabelImgFile2.getHeight(), Image.SCALE_SMOOTH));
        jLabelImgFile2.setIcon(imageIcon2);
        try {
            File image2 = new File(filename2);
            FileInputStream fis2 = new FileInputStream(image2);
            ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
            byte[] buf2 = new byte[128];

            for (int readNum; (readNum = fis2.read(buf2)) != -1;) {
                bos2.write(buf2, 0, readNum);
            }

            int size = bos2.toByteArray().length;
            if (size > 1000000) {
                JOptionPane.showMessageDialog(this, "Attached file is too big. Attached file should be less that 1Mb. Please rescan and attach again.");
                jLabelImgFile2.setIcon(null);
            } else {
                person_image2 = bos2.toByteArray();
            }

        } catch (Exception e) {
            System.out.println(e);

        }
    }

    void imgChooserFile3() {

        JFileChooser chooser3 = new JFileChooser();
        FileNameExtensionFilter filter3 = new FileNameExtensionFilter("images", "jpg", "gif", "png", "jpeg");
        chooser3.setFileFilter(filter3);
        int returnVal3 = chooser3.showOpenDialog(null);
        File f3 = chooser3.getSelectedFile();
        filename3 = f3.getAbsolutePath();
        if (returnVal3 == JFileChooser.APPROVE_OPTION) {
            if (!(f3.getName().endsWith(".jpg") || f3.getName().endsWith(".gif") || f3.getName().endsWith(".png") || f3.getName().endsWith(".jpeg"))) {
                JOptionPane.showMessageDialog(null, "Please select file with .jpg, .bmp, .png, .jpeg and .gif extension only.");
            }
        }

        ImageIcon imageIcon3 = new ImageIcon(new ImageIcon(filename3).getImage().getScaledInstance(jLabelImgFile3.getWidth(), jLabelImgFile3.getHeight(), Image.SCALE_SMOOTH));
        jLabelImgFile3.setIcon(imageIcon3);
        try {
            File image3 = new File(filename3);
            FileInputStream fis3 = new FileInputStream(image3);
            ByteArrayOutputStream bos3 = new ByteArrayOutputStream();
            byte[] buf3 = new byte[128];

            for (int readNum; (readNum = fis3.read(buf3)) != -1;) {
                bos3.write(buf3, 0, readNum);
            }

            int size = bos3.toByteArray().length;

            if (size > 1000000) {
                JOptionPane.showMessageDialog(this, "Attached file is too big. Attached file should be less that 1Mb. Please rescan and attach again.");
                jLabelImgFile3.setIcon(null);
            } else {
                person_image3 = bos3.toByteArray();
            }

        } catch (Exception e) {
            System.out.println(e);

        }
    }

    void imgSaveFile1() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            if (jLabelImgFile1.getIcon().toString().trim().length() > 0) {
                String sqlAttDoc1 = " INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimDocAttachTab] "
                        + "(DOC_ATT_NUM,SERIAL,REF_NUM,ATT_IMAGE,IMG_VERSION)"
                        + "VALUES (?,?,?,?,?)";

                pst = conn.prepareStatement(sqlAttDoc1);

                pst.setString(1, "1");
                pst.setString(2, jLabelSerialAcq.getText());
                pst.setString(3, String.valueOf(jLabelRegNum.getText()));
                pst.setBytes(4, person_image);
                pst.setString(5, "1");
                pst.executeUpdate();

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void imgSaveFile2() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            if (jLabelImgFile2.getIcon().toString().trim().length() > 0) {
                String sqlAttDoc2 = " INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimDocAttachTab] "
                        + "(DOC_ATT_NUM,SERIAL,REF_NUM,ATT_IMAGE,IMG_VERSION)"
                        + "VALUES (?,?,?,?,?)";

                pst = conn.prepareStatement(sqlAttDoc2);

                pst.setString(1, "2");
                pst.setString(2, jLabelSerialAcq.getText());
                pst.setString(3, String.valueOf(jLabelRegNum.getText()));
                pst.setBytes(4, person_image2);
                pst.setString(5, "1");
                pst.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void imgSaveFile3() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            if (!("".equals(jLabelImgFile3.getIcon().toString()))) {
                String sqlAttDoc3 = " INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimDocAttachTab] "
                        + "(DOC_ATT_NUM,SERIAL,REF_NUM,ATT_IMAGE,IMG_VERSION)"
                        + "VALUES (?,?,?,?,?)";

                pst = conn.prepareStatement(sqlAttDoc3);

                pst.setString(1, "3");
                pst.setString(2, jLabelSerialAcq.getText());
                pst.setString(3, String.valueOf(jLabelRegNum.getText()));
                pst.setBytes(4, person_image3);
                pst.setString(5, "1");

                pst.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void mainPageTotUpdate() {
        breakfastsubtotal = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double breakfastamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 10));
            breakfastsubtotal += breakfastamount;

        }
        jLabelBreakFastSubTot.setText(String.format("%.2f", breakfastsubtotal));

        lunchsubtotal = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double lunchamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 11));
            lunchsubtotal += lunchamount;
        }
        jLabelLunchSubTot.setText(String.format("%.2f", lunchsubtotal));

        dinnersubtotal = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double dinneramount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 12));
            dinnersubtotal += dinneramount;
        }
        jLabelDinnerSubTot.setText(String.format("%.2f", dinnersubtotal));

        incidentalsubtotal = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double incidentalamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 13));
            incidentalsubtotal += incidentalamount;
        }
        jLabelIncidentalSubTot.setText(String.format("%.2f", incidentalsubtotal));

        miscSubTot = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double miscamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 15));
            miscSubTot += miscamount;
        }
        jLabelMiscSubTot.setText(String.format("%.2f", miscSubTot));

        unprovedSubTot = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double unprovedamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 16));
            unprovedSubTot += unprovedamount;
        }
        jLabelAccUnprovedSubTot.setText(String.format("%.2f", unprovedSubTot));

        provedSubTot = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double provedamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 17));
            provedSubTot += provedamount;
        }
        jLabelAccProvedSubTot.setText(String.format("%.2f", provedSubTot));

        allTotal = 0;
        allTotal = unprovedSubTot + provedSubTot + miscSubTot + incidentalsubtotal
                + dinnersubtotal + lunchsubtotal + breakfastsubtotal;

        System.out.println("req amt " + allTotal + " main tot " + allActTot);

    }

    void mainPageTotUpdateAcq() {
        breakfastsubtotalAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double breakfastamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 10));
            breakfastsubtotalAcq += breakfastamountAcq;

        }
        jLabelAcqBreakFastSubTot.setText(String.format("%.2f", breakfastsubtotalAcq));

        lunchsubtotalAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double lunchamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 11));
            lunchsubtotalAcq += lunchamountAcq;

        }
        jLabelAcqLunchSubTot.setText(String.format("%.2f", lunchsubtotalAcq));

        dinnersubtotalAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double dinneramountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 12));
            dinnersubtotalAcq += dinneramountAcq;
        }
        jLabelAcqDinnerSubTot.setText(String.format("%.2f", dinnersubtotalAcq));

        incidentalsubtotalAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double incidentalamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 13));
            incidentalsubtotalAcq += incidentalamountAcq;
        }
        jLabelAcqIncidentalSubTot.setText(String.format("%.2f", incidentalsubtotalAcq));

        miscSubTotAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double miscamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 15));
            miscSubTotAcq += miscamountAcq;
        }
        jLabelAcqMiscSubTot.setText(String.format("%.2f", miscSubTotAcq));

        unprovedSubTotAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double unprovedamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 16));
            unprovedSubTotAcq += unprovedamountAcq;
        }

        jLabelAcqAccUnprovedSubTot.setText(String.format("%.2f", unprovedSubTotAcq));

        provedSubTotAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double provedamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 17));
            provedSubTotAcq += provedamountAcq;
        }

        jLabelAcqAccProvedSubTot.setText(String.format("%.2f", provedSubTotAcq));

        allTotalAcq = provedSubTotAcq + unprovedSubTotAcq + miscSubTotAcq + incidentalsubtotalAcq
                + dinnersubtotalAcq + lunchsubtotalAcq + breakfastsubtotalAcq;
        jLabelAcqBreakFastSubTotBal.setText(Double.toString(breakfastsubtotal - breakfastsubtotalAcq));

        jLabelAcqLunchSubTotBal.setText(Double.toString(lunchsubtotal - lunchsubtotalAcq));
        jLabelAcqDinnerSubTotBal.setText(Double.toString(dinnersubtotal - dinnersubtotalAcq));
        jLabelAcqIncidentalSubTotBal.setText(Double.toString(incidentalsubtotal - incidentalsubtotalAcq));
        jLabelAcqMiscSubTotBal.setText(Double.toString(miscSubTot - miscSubTotAcq));
        jLabelAcqAccUnprovedSubTotBal.setText(Double.toString(unprovedSubTot - unprovedSubTotAcq));
        jLabelAcqAccProvedSubTotBal.setText(Double.toString(provedSubTot - provedSubTotAcq));
        // jLabelAcqAppTotReqCost.setText(Integer.toString(allActTot));

        double totDiff = allTotal - allTotalAcq;

//        double subTotDiff = allActTot - (abs(allTotalAcq) + abs(Double.parseDouble(jLabelAcqAppWk1TotReqCost.getText()))
//                + abs(Double.parseDouble(jLabelAcqAppWk2TotReqCost.getText())) + +abs(Double.parseDouble(jLabelAcqAppWk3TotReqCost.getText()))
//                + abs(Double.parseDouble(jLabelAcqAppWk4TotReqCost.getText())));
        double subTotDiff = allTotalAcq - allActTot;

        System.out.println("alltotacq " + allTotalAcq + " allacttot " + allActTot + " alltotal" + allTotal);

        System.out.println("value tot " + (abs(allTotalAcq) + abs(Double.parseDouble(jLabelAcqAppWk1TotReqCost.getText()))
                + abs(Double.parseDouble(jLabelAcqAppWk2TotReqCost.getText())) + +abs(Double.parseDouble(jLabelAcqAppWk3TotReqCost.getText()))
                + abs(Double.parseDouble(jLabelAcqAppWk4TotReqCost.getText()))));

        System.out.println("line item " + abs(allTotalAcq) + " " + abs(Double.parseDouble(jLabelAcqAppWk1TotReqCost.getText())) + " "
                + abs(Double.parseDouble(jLabelAcqAppWk2TotReqCost.getText())) + " " + abs(Double.parseDouble(jLabelAcqAppWk3TotReqCost.getText()))
                + " " + abs(Double.parseDouble(jLabelAcqAppWk4TotReqCost.getText())));

//        jLabelAcqAppTotReqCost.setText(String.format("%.2f", totDiff));
        jLabelAcqAppTotReqCost.setText(String.format("%.2f", subTotDiff));

        System.out.println("subto " + subTotDiff);

        if (minWkNum == 1) {
            jLabelAcqAppWk1TotReqCost.setText(String.format("%.2f", allTotalAcq));
            jLabelAppWk1TotReq.setVisible(true);
            jLabelAcqAppWk1TotReqCost.setVisible(true);
            jLabelAppWk1TotReq.setText("Total Cleared Week " + minWkNum);
        } else if (minWkNum == 2) {
            jLabelAcqAppWk2TotReqCost.setText(String.format("%.2f", allTotalAcq));
            jLabelAppWk1TotReq.setVisible(true);
            jLabelAcqAppWk1TotReqCost.setVisible(true);
            jLabelAppWk2TotReq.setVisible(true);
            jLabelAcqAppWk2TotReqCost.setVisible(true);
            // jLabelAppWk1TotReq.setText("Total Week " + minWkNum); values set from table
            jLabelAppWk1TotReq.setText("Total Cleared Week " + (minWkNum - 1));
            jLabelAppWk2TotReq.setText("Total Cleared Week " + minWkNum);
        } else if (minWkNum == 3) {
            jLabelAcqAppWk3TotReqCost.setText(String.format("%.2f", allTotalAcq));
            jLabelAppWk1TotReq.setVisible(true);
            jLabelAcqAppWk1TotReqCost.setVisible(true);
            jLabelAppWk2TotReq.setVisible(true);
            jLabelAcqAppWk2TotReqCost.setVisible(true);
            jLabelAppWk3TotReq.setVisible(true);
            jLabelAcqAppWk3TotReqCost.setVisible(true);
            // jLabelAppWk1TotReq.setText("Total Week " + minWkNum); values set from table
            // jLabelAppWk2TotReq.setText("Total Week " + minWkNum); values set from table
            jLabelAppWk1TotReq.setText("Total Cleared Week " + (minWkNum - 2));
            jLabelAppWk2TotReq.setText("Total Cleared Week " + (minWkNum - 1));
            jLabelAppWk3TotReq.setText("Total Cleared Week " + minWkNum);
        } else if (minWkNum == 4) {
            jLabelAcqAppWk4TotReqCost.setText(String.format("%.2f", allTotalAcq));
            jLabelAppWk1TotReq.setVisible(true);
            jLabelAcqAppWk1TotReqCost.setVisible(true);
            jLabelAppWk2TotReq.setVisible(true);
            jLabelAcqAppWk2TotReqCost.setVisible(true);
            jLabelAppWk3TotReq.setVisible(true);
            jLabelAcqAppWk3TotReqCost.setVisible(true);
            jLabelAppWk4TotReq.setVisible(true);
            jLabelAcqAppWk4TotReqCost.setVisible(true);
            jLabelAppWk1TotReq.setText("Total Cleared Week " + (minWkNum - 3));
            jLabelAppWk2TotReq.setText("Total Cleared Week " + (minWkNum - 2));
            jLabelAppWk3TotReq.setText("Total Cleared Week " + (minWkNum - 1));
            jLabelAppWk4TotReq.setText("Total Cleared Week " + minWkNum);
        } else if (minWkNum == 5) {
            jLabelAcqAppWk5TotReqCost.setText(String.format("%.2f", allTotalAcq));
            jLabelAppWk1TotReq.setVisible(true);
            jLabelAcqAppWk1TotReqCost.setVisible(true);
            jLabelAppWk2TotReq.setVisible(true);
            jLabelAcqAppWk2TotReqCost.setVisible(true);
            jLabelAppWk3TotReq.setVisible(true);
            jLabelAcqAppWk3TotReqCost.setVisible(true);
            jLabelAppWk4TotReq.setVisible(true);
            jLabelAcqAppWk4TotReqCost.setVisible(true);
            jLabelAppWk5TotReq.setVisible(true);
            jLabelAcqAppWk5TotReqCost.setVisible(true);
            jLabelAppWk1TotReq.setText("Total Cleared Week " + (minWkNum - 4));
            jLabelAppWk2TotReq.setText("Total Cleared Week " + (minWkNum - 3));
            jLabelAppWk3TotReq.setText("Total Cleared Week " + (minWkNum - 2));
            jLabelAppWk4TotReq.setText("Total Cleared Week " + (minWkNum - 1));
            jLabelAppWk5TotReq.setText("Total Cleared Week " + minWkNum);
        }

        if (!(minWkNum == maxWkNum)) {
            jLabelAppTotReq.setText("Bal After Week " + minWkNum);
        } else {
            if (Double.parseDouble(jLabelAcqAppTotReqCost.getText()) < 0) {
                jLabelAppTotReq.setText("Total (Change)");
            } else if (Double.parseDouble(jLabelAcqAppTotReqCost.getText()) > 0) {
                jLabelAppTotReq.setText("Total (Shortfall)");
            } else if (Double.parseDouble(jLabelAcqAppTotReqCost.getText()) == 0) {
                jLabelAppTotReq.setText("Total (Balancing)");
            }
        }

    }

    void fetchDataForAcq() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            st.executeQuery("SELECT  b.ITM_NUM,b.ACT_DATE,b.ACC_CODE,b.DONOR ,b.PRJ_CODE_GL , b.PRJ_CODE_PROG ,"
                    + "b.PRJ_NAM_PROG ,"
                    + "b.BUD_LINE ,b.BUD_CODE ,b.ACT_SITE , b.ACT_DESC ,b.BRK_AMT ,b.LNC_AMT ,b.DIN_AMT ,"
                    + "b.INC_AMT ,b.MSC_ACT , b.MSC_AMT ,b.ACC_UNPROV_AMT ,b.ACC_PROV_AMT ,b.ACT_ITM_TOT "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a join "
                    + "[ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] b on a.SERIAL = b.SERIAL and a.REF_NUM=b.REF_NUM "
                    + "and a.DOC_VER=b.DOC_VER and a.ACT_REC_STA=b.ACT_REC_STA join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] c "
                    + "on a.SERIAL = c.SERIAL and a.REF_NUM=c.REF_NUM where c.DOC_STATUS='HODApprove' and a.ACT_REC_STA = 'A' "
                    + "and c.DOC_STATUS='HODApprove' and b.plan_wk =" + minWkNum + "   and a.ACT_REC_STA = 'A'  "
                    + "and concat(a.SERIAL,a.REF_NUM) ='" + jLabelSerial.getText() + jTextAcqRegNum.getText() + " ' order by b.ACT_DATE");

            ResultSet r = st.getResultSet();

            while (r.next()) {

                modelAcq.insertRow(modelAcq.getRowCount(), new Object[]{r.getString(2), r.getString(3),
                    r.getString(4), r.getString(5), r.getString(6), r.getString(7),
                    r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12), r.getString(13),
                    r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20)});

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

            st.executeQuery("SELECT  b.ITM_NUM,b.ACT_DATE,b.ACC_CODE,b.DONOR ,b.PRJ_CODE_GL , b.PRJ_CODE_PROG ,"
                    + "b.PRJ_NAM_PROG ,"
                    + "b.BUD_LINE ,b.BUD_CODE ,b.ACT_SITE , b.ACT_DESC ,b.BRK_AMT ,b.LNC_AMT ,b.DIN_AMT ,"
                    + "b.INC_AMT ,b.MSC_ACT , b.MSC_AMT ,b.ACC_UNPROV_AMT ,b.ACC_PROV_AMT ,b.ACT_ITM_TOT "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a join "
                    + "[ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] b on a.SERIAL = b.SERIAL and a.REF_NUM=b.REF_NUM "
                    + "and a.DOC_VER=b.DOC_VER and a.ACT_REC_STA=b.ACT_REC_STA join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] c "
                    + "on a.SERIAL = c.SERIAL and a.REF_NUM=c.REF_NUM where c.DOC_STATUS='HODApprove' and a.ACT_REC_STA = 'A' "
                    + "and c.DOC_STATUS='HODApprove' and b.plan_wk =" + minWkNum + "   and a.ACT_REC_STA = 'A'  "
                    + "and concat(a.SERIAL,a.REF_NUM) ='" + jLabelSerial.getText() + jTextAcqRegNum.getText() + " ' order by b.ACT_DATE");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelAcq.insertRow(modelAcq.getRowCount(), new Object[]{r.getString(2), r.getString(3),
                    r.getString(4), r.getString(5), r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10),
                    r.getString(11), "0", "0", "0", "0", "", "0", "0", "0", "0"});

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void fetchdata() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st2 = conn.createStatement();

            st2.executeQuery("SELECT count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a "
                    + "join  [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] b on a.SERIAL = b.SERIAL "
                    + "and a.REF_NUM=b.REF_NUM and a.DOC_VER=b.DOC_VER and a.ACT_REC_STA=b.ACT_REC_STA "
                    + "where b.DOC_STATUS='HODApprove' and a.ACT_REC_STA = 'A' "
                    + "and concat(a.SERIAL,a.REF_NUM)='" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "'  "
                    + "and a.EMP_NUM='" + jLabelEmp.getText() + "' and a.REF_NUM in "
                    + "(Select REF_NUM from [ClaimsAppSysZvandiri].[dbo].[BatRunTab] where STATUS = 'Paid')");

            ResultSet r2 = st2.getResultSet();

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            while (r2.next()) {
                numsearchRef = r2.getInt(1);

            }

            if (numsearchRef == 0) {
                refreshFields();
                JOptionPane.showMessageDialog(null, "Reference number cannot be acquitted or is invalid. Please check your reference number.");
                jTextAcqRegNum.requestFocusInWindow();
                jTextAcqRegNum.setFocusable(true);
//                new JFrameMain(jLabelEmp.getText()).setVisible(true);
//                setVisible(false);
            } else {
                jLabelAcqWk.setText("Acquitting Week " + minWkNum + " of Week " + maxWkNum);
                jLabelAcqWk1.setText("Acquitting Week " + minWkNum + " of Week " + maxWkNum);
                jLabelAcqWk2.setText("Acquitting Week " + minWkNum + " of Week " + maxWkNum);
                jLabelAcqWk3.setText("Acquitting Week " + minWkNum + " of Week " + maxWkNum);
                if ((minWkNum == 1) && (existCountWkNum == 0)) {
                    jDialogPayRec.setVisible(true);
                }
                try {

                    st.executeQuery("SELECT  b.ITM_NUM,b.ACT_DATE,b.ACC_CODE,b.DONOR ,b.PRJ_CODE_GL , b.PRJ_CODE_PROG ,"
                            + "b.PRJ_NAM_PROG ,"
                            + "b.BUD_LINE ,b.BUD_CODE ,b.ACT_SITE , b.ACT_DESC ,b.BRK_AMT ,b.LNC_AMT ,b.DIN_AMT ,"
                            + "b.INC_AMT ,b.MSC_ACT , b.MSC_AMT ,b.ACC_UNPROV_AMT ,b.ACC_PROV_AMT ,b.ACT_ITM_TOT "
                            + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a "
                            + "join [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] b on a.SERIAL = b.SERIAL "
                            + "and a.REF_NUM=b.REF_NUM and a.DOC_VER=b.DOC_VER and a.ACT_REC_STA=b.ACT_REC_STA "
                            + "join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] c on a.SERIAL = c.SERIAL "
                            + "and a.REF_NUM=c.REF_NUM where c.DOC_STATUS='HODApprove' and b.plan_wk =" + minWkNum + "    "
                            + "and a.ACT_REC_STA = 'A' and c.DOC_STATUS='HODApprove' and a.ACT_REC_STA = 'A'  "
                            + "and concat(a.SERIAL,a.REF_NUM) ='" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "' "
                            + "and a.REF_NUM in (Select REF_NUM from [ClaimsAppSysZvandiri].[dbo].[BatRunTab] where STATUS = 'Paid') "
                            + "order by b.ACT_DATE"
                    );

                    ResultSet r = st.getResultSet();

                    while (r.next()) {
                        model.insertRow(model.getRowCount(), new Object[]{r.getString(2), r.getString(3),
                            r.getString(4), r.getString(5), r.getString(6), r.getString(7),
                            r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12), r.getString(13),
                            r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                            r.getString(20)});

                    }
//
                    if (minWkNum == 2) {
                        wk1Amt();
                    } else if (minWkNum == 3) {
                        wk1Amt();
                        wk2Amt();
                    } else if (minWkNum == 4) {
                        wk1Amt();
                        wk2Amt();
                        wk3Amt();
                    } else if (minWkNum == 5) {
                        wk1Amt();
                        wk2Amt();
                        wk3Amt();
                        wk4Amt();
                    }

                    st1.executeQuery("SELECT a.REF_YEAR, a.REF_NUM,a.REF_DAT, a.EMP_NUM,a.EMP_NAM,"
                            + " a.EMP_TTL, a.EMP_PROV, a.EMP_OFF,a.EMP_BNK_NAM, a.ACC_NUM,"
                            + " a.ACT_MAIN_PUR,a.ACT_TOT_AMT, a.ACT_REC_STA,a.DOC_VER, a.SERIAL,len(a.ACC_NUM)  "
                            + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a join "
                            + "[ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] b on a.SERIAL = b.SERIAL "
                            + "and a.REF_NUM=b.REF_NUM and a.DOC_VER=b.DOC_VER and "
                            + "a.ACT_REC_STA=b.ACT_REC_STA join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] c "
                            + "on a.SERIAL = c.SERIAL and a.REF_NUM=c.REF_NUM where  "
                            + "c.DOC_STATUS='HODApprove' and a.ACT_REC_STA = 'A'  "
                            + "and concat(a.SERIAL,a.REF_NUM)='" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "'");

                    ResultSet r1 = st1.getResultSet();

                    while (r1.next()) {
                        jLabelRegDateAcq.setText(r1.getString(3));
                        jLabelAcqEmpNum.setText(r1.getString(4));
                        jLabelAcqEmpNam.setText(r1.getString(5));
                        jLabelAcqEmpTitle.setText(r1.getString(6));
                        jLabelAcqProvince.setText(r1.getString(7));
                        jLabelAcqOffice.setText(r1.getString(8));
                        jLabelAcqBankName.setText(r1.getString(9));
//                        jLabelAcqAccNum.setText(r1.getString(10));
                        jLabelAcqActMainPurpose.setText(r1.getString(11));
                        allActTot = r1.getDouble(12);

                        searchRef = r1.getString(2);
                        empAccNum = r1.getString(10);
                        bankAccLen = r1.getInt(16);

                        if (bankAccLen == 13) {
                            maskAcc(empAccNum, "xxxxxxxxx####");
                        } else if (bankAccLen == 11) {
                            maskAcc(empAccNum, "xxxxxxx####");
                        }
                    }

                    if (minWkNum == 1) {
                        if (existCountWkNum > 0) {
                            balAmtFinal();
                            jLabelAcqAppTotReqCost.setText(Double.toString(balAmt));
                        } else {
                            jLabelAcqAppTotReqCost.setText(Double.toString(allActTot));
                        }
                    } else if (minWkNum == 2) {
                        jLabelAcqAppTotReqCost.setText(Double.toString(allActTot - wk1Amt));
                    } else if (minWkNum == 3) {
                        jLabelAcqAppTotReqCost.setText(Double.toString(allActTot - wk1Amt - wk2Amt));
                    } else if (minWkNum == 4) {
                        jLabelAcqAppTotReqCost.setText(Double.toString(allActTot - wk1Amt - wk2Amt - wk3Amt));
                    } else if (minWkNum == 5) {
                        jLabelAcqAppTotReqCost.setText(Double.toString(allActTot - wk1Amt - wk2Amt - wk3Amt - wk4Amt));
                    }

//                    if (!"Central Office".equals(jLabelAcqProvince.getText())) {
//                        jTabbedPaneAcqAtt.setTitleAt(1, "");
//                        jTabbedPaneAcqAtt.setEnabledAt(1, false);
//                    }
                    getLowestDate();
                    allowanceRate();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void maskAcc(String accNumber, String mask) {
        // format the number
        int index = 0;
        StringBuilder maskedNumber = new StringBuilder();
        for (int i = 0; i < mask.length(); i++) {
            char c = mask.charAt(i);
            if (c == '#') {
                maskedNumber.append(accNumber.charAt(index));
                index++;
            } else if (c == 'x') {
                maskedNumber.append(c);
                index++;
            } else {
                maskedNumber.append(c);
            }
        }

        jLabelAcqAccNum.setText(maskedNumber.toString());
    }

    void saveAcqRegisterCheck() {

        if (jTableActivityAcq.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "At least one activity should be completed on the TAB \"Perdiem Acquittal\". Please check and correct.");
            jTabbedPaneAppSys.setSelectedIndex(2);
            jDateChooserDialogActivityDateFrom.requestFocusInWindow();
            jDateChooserDialogActivityDateFrom.setFocusable(true);
        } else if ((("Total (Change)").equals(jLabelAppTotReq.getText())) && (jRadioButtonPayRecYes.isSelected())
                && (("Select Bank".equals(jComboBankNam.getSelectedItem().toString()))
                || ("".equals(jTextPaidAmt.getText()))
                || ("0.00".equals(jTextPaidAmt.getText())))) {
            // findBankName();
            jDialogChgPaid.setVisible(true);
            jLabelAcqPayBack.setText("Paid Back A/C " + jLabelBankAccNo.getText());
            jLabelAcqAmtPayBack.setText(String.format("%.2f", Double.toString(Double.parseDouble(jTextPaidAmt.getText()))));

        } else {
            if (!(jLabelImgFile2.getIcon() == null)) {
                int selectedOption = JOptionPane.showConfirmDialog(null,
                        "You have attached a document on the Refund Depostit slip section. Please confirm that the attached document is a deposit slip for change?",
                        "Choose",
                        JOptionPane.YES_NO_OPTION);
                if (selectedOption == JOptionPane.YES_OPTION) {

                    depSlip = "Y";
                    saveAcqRegister();
                } else {
                    JOptionPane.showMessageDialog(null, "Please remove attachment on the Refund Deposit Slip tab.");
                    jTabbedPaneAppSys.setSelectedIndex(3);
                }
            } else {
                depSlip = "N";
                saveAcqRegister();
            }

        }
    }

    void saveAcqRegister() {

        try {

            jMenuItemSubmit.setEnabled(false);
            jButtonSave.setEnabled(false);
            SerialCheck();

            RefNumAllocation();

            if (maxAcqRefNum > newAcqRefNum) {
                JOptionPane.showMessageDialog(this, "<html> Registration failure.Max Num. "
                        + "Registration falure can be caused by slow network speeds.<br><br> "
                        + "Please try again. If the problem persist contact IT.</html>");
//                jLabelSerialAcq.setText("");
//                jLabelRegNum.setText("");
//                jLabelRegYear.setText("");
                jMenuItemSubmit.setEnabled(true);
                jButtonSave.setEnabled(true);
            } else {

                insGenTab();
                insItmTab();
                imgSaveFile1();
                imgSaveFile2();
                imgSaveFile3();
                createAction();
                createReport();
                createAttDoc();
                wkUpdate();
                wkClearedUpdate();
                payRecAck();

//
                if (!("".equals(jLabelAcqPayBack.getText()))) {
                    chgBank();
                }
//
                if ("Y".equals(depSlip)) {
                    createDepSlipRec();
                }
                refNumUpdate();

                checkRegistration();
            }
        } catch (Exception e) {
//            sqlError = e.getErrorCode();
//            if (sqlError == 2627) {
//                checkRegistration();
//            }
            System.out.println(e);
        }

    }

    void SerialCheck() {

        int count = 0;
        do {

            try {

                Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                        + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
                Statement st = conn.createStatement();

                st.executeQuery("SELECT count(*) FROM [ClaimsAppSysZvandiri].[dbo].[SerialLock] where SERIAL = 'A'");

                ResultSet rs = st.getResultSet();

                while (rs.next()) {
                    count = Integer.parseInt(rs.getString(1));

                }
                // conn.close();

                if (count > 0) {
                    try {

                        Statement st1 = conn.createStatement();

                        st1.executeQuery("SELECT USR_NAM,LCK_DATE_TIME FROM "
                                + "[ClaimsAppSysZvandiri].[dbo].[SerialLock] where SERIAL = 'A'");

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

                        if ((jLabelGenLogNam.getText().equals(usrnam)) && (seconds >= 1)) {

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

    void clearTable() {

        modelTrip.getDataVector().removeAllElements();
        modelTrip.fireTableDataChanged();
    }

    void addTrips() {
        try {

            getTripDetails();
            if (modelTrip.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No record exist for the searched entry.");

                jTextVehReg.requestFocusInWindow();
                jTextVehReg.setFocusable(true);

            }
            jTextVehReg.setText("");
            jDateTripFrom.setDate(null);
            jDateTripTo.setDate(null);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void addFileAtt() {

        String attFileDesc = jTextAttDocDesc.getText();

        modelAtt.addRow(new Object[]{attFileDesc,
            jTextAttAcqDocName.getText(), jTextAttDocFilePath.getText()});
    }

    void deleteFileAttTrip() {

        if (modelTrip.getRowCount() > 0) {
            int selectedOption = JOptionPane.showConfirmDialog(null,
                    "Do you want to delete trip details?",
                    "Choose", JOptionPane.YES_NO_OPTION);

            if (selectedOption == JOptionPane.YES_OPTION) {

                clearTable();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please note that the table is empty.");

        }

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

    void refreshAttFields() {
        jTextAttDocDesc.setText("");
        jTextAttAcqDocName.setText("");
        jTextAttDocFilePath.setText("");
    }

    void findVehicle() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd
                    + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            ResultSet r1 = st1.executeQuery("SELECT count(*)  FROM [OphidLogBook].[dbo].[ophid_vehicles]"
                    + " where upper(license_number) =upper('" + str + "')");

            while (r1.next()) {

                vehCount = r1.getInt(1);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        //   monVehRet();
    }

    void deleteLongLock() {
        try {

            String sqlDeleteLock = "delete [ClaimsAppSysZvandiri].[dbo].[SerialLock] where SERIAL = "
                    + "'A'";

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            pst = conn.prepareStatement(sqlDeleteLock);
            pst.executeUpdate();
        } catch (Exception e) {

        }
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

            st.executeQuery("SELECT REF_NUM + 1,SERIAL,REF_NUM FROM [ClaimsAppSysZvandiri].[dbo].[refNumTab] where  SERIAL = 'A'");

            ResultSet rs = st.getResultSet();
            jLabelRegYear.setText(Integer.toString(finyear));
            while (rs.next()) {

                jLabelSerialAcq.setText(rs.getString(2));
                jLabelRegNum.setText(rs.getString(1));
                oldRefNum = rs.getString(3);
                newAcqRefNum = rs.getInt(1);
            }

            pst = conn.prepareStatement(sqlSerialLock);

            pst.setString(1, jLabelSerialAcq.getText());
            pst.setString(2, jLabelRegNum.getText());
            pst.setString(3, jLabelGenLogNam.getText());

            pst.executeUpdate();

            String sqlSerialLockUpdate = "update [ClaimsAppSysZvandiri].[dbo].[SerialLock] set LCK_DATE_TIME = ( SELECT SYSDATETIME()) where "
                    + "concat(SERIAL,REF_NUM) = '" + jLabelSerialAcq.getText() + jLabelRegNum.getText() + "' ";

            pst1 = conn.prepareStatement(sqlSerialLockUpdate);
            pst1.executeUpdate();

            st1.executeQuery("SELECT max(REF_NUM) FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] WHERE SERIAL='A'");

            ResultSet rs1 = st1.getResultSet();

            while (rs1.next()) {
                maxAcqRefNum = rs1.getInt(1);

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    void wkClearedUpdate() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlWkCleared = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab] "
                    + "(PREV_SERIAL,PREV_REF_NUM,SERIAL,REF_NUM,PLAN_WK,REQ_AMT,CLEARED_AMT,REQ_STA,ACQ_STA,DOC_VER)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?)";

            pst1 = conn.prepareStatement(sqlWkCleared);

            pst1.setString(1, jLabelSerial.getText());
            pst1.setString(2, jTextAcqRegNum.getText());
            pst1.setString(3, jLabelSerialAcq.getText());
            pst1.setString(4, jLabelRegNum.getText());
            pst1.setString(5, Integer.toString(minWkNum));
            pst1.setString(6, Double.toString(allTotal));
            if (minWkNum == 1) {
                pst1.setString(7, jLabelAcqAppWk1TotReqCost.getText());
            } else if (minWkNum == 2) {
                pst1.setString(7, jLabelAcqAppWk2TotReqCost.getText());
            } else if (minWkNum == 3) {
                pst1.setString(7, jLabelAcqAppWk3TotReqCost.getText());
            } else if (minWkNum == 4) {
                pst1.setString(7, jLabelAcqAppWk4TotReqCost.getText());
            } else if (minWkNum == 5) {
                pst1.setString(7, jLabelAcqAppWk5TotReqCost.getText());
            }
            pst1.setString(8, "R");
            pst1.setString(9, "C");
            pst1.setString(10, "1");

            pst1.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void wkUpdate() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlwkUpdateC = "update [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab] set "
                    + "ACQ_STA ='P' where "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "'"
                    + "and PLAN_WK =" + minWkNum;
            pst = conn.prepareStatement(sqlwkUpdateC);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void createETrip() {
        try {
            for (int i = 0; i < jTableTripDetails.getRowCount(); i++) {

                String sqlCreateETrip = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimTripMilageTab] "
                        + " VALUES (?,?, ?,?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?,?,?,?,?,?)";

                Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                        + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
                pst = conn.prepareStatement(sqlCreateETrip);

                pst.setString(1, jLabelRegYear.getText());
                pst.setString(2, jLabelSerialAcq.getText());
                pst.setString(3, jLabelRegNum.getText());
                pst.setString(4, jTableTripDetails.getValueAt(i, 0).toString());
                pst.setString(5, jTableTripDetails.getValueAt(i, 1).toString());
                pst.setString(6, jTableTripDetails.getValueAt(i, 2).toString());
                pst.setString(7, jTableTripDetails.getValueAt(i, 3).toString());
                pst.setString(8, jTableTripDetails.getValueAt(i, 4).toString());
                pst.setString(9, jTableTripDetails.getValueAt(i, 5).toString());
                pst.setString(10, jTableTripDetails.getValueAt(i, 6).toString());
                pst.setString(11, jTableTripDetails.getValueAt(i, 7).toString());
                pst.setString(12, jTableTripDetails.getValueAt(i, 8).toString());
                pst.setString(13, jTableTripDetails.getValueAt(i, 9).toString());
                pst.setString(14, jTableTripDetails.getValueAt(i, 10).toString());
                pst.setString(15, jTableTripDetails.getValueAt(i, 11).toString());
                pst.setString(16, jTableTripDetails.getValueAt(i, 12).toString());
                pst.setString(17, Integer.toString(minWkNum));
                pst.setString(18, "1");
                pst.setString(19, "1");
                pst.setString(20, "Q");

                pst.executeUpdate();
                itmNum = itmNum + 1;

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void createReport() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlRepCreate = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimTripReport] "
                    + "(PREV_SERIAL,PREV_REF_NUM,SERIAL,REF_NUM,NAM_TRAVEL,PLAN_WK,DOC_VER,REG_MOD_VER,ACT_REC_STA)"
                    + " VALUES (?,?,?,?,?,?,?,?,?)";

            pst1 = conn.prepareStatement(sqlRepCreate);

            pst1.setString(1, jLabelSerial.getText());
            pst1.setString(2, jTextAcqRegNum.getText());
            pst1.setString(3, jLabelSerialAcq.getText());
            pst1.setString(4, jLabelRegNum.getText());
            pst1.setString(5, jTextAreaNamTravel.getText());
            pst1.setString(6, Integer.toString(minWkNum));
            pst1.setString(7, "1");
            pst1.setString(8, "1");
            pst1.setString(9, "Q");
            pst1.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
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
            pst.setString(11, empAccNum);
            pst.setString(12, jLabelAcqActMainPurpose.getText());
            if (minWkNum == 1) {
                pst.setString(13, jLabelAcqAppWk1TotReqCost.getText());
            } else if (minWkNum == 2) {
                pst.setString(13, jLabelAcqAppWk2TotReqCost.getText());
            } else if (minWkNum == 3) {
                pst.setString(13, jLabelAcqAppWk3TotReqCost.getText());
            } else if (minWkNum == 4) {
                pst.setString(13, jLabelAcqAppWk4TotReqCost.getText());
            } else if (minWkNum == 5) {
                pst.setString(13, jLabelAcqAppWk5TotReqCost.getText());
            }
            pst.setString(14, jLabelSerial.getText());
            pst.setString(15, String.valueOf(jTextAcqRegNum.getText()));
            pst.setString(16, jLabelRegDateAcq.getText());
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
                pst1.setString(20, Integer.toString(minWkNum));
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

            pst1.setString(1, df.format(curYear));
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

    void createAttDoc() {
        try {
            int itmNumAtt = 1;

            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            for (int i = 0; i < modelAtt.getRowCount(); i++) {
                String imgFileValue = modelAtt.getValueAt(i, 2).toString();
                String imgFileDsc = modelAtt.getValueAt(i, 0).toString();
                String imgFileName = modelAtt.getValueAt(i, 1).toString();

                if (imgFileValue.trim().length() > 0) {

                    attL.insertPDF7(conn, jLabelRegYear.getText(), jLabelRegNum.getText(),
                            itmNumAtt, imgFileValue, imgFileDsc, imgFileName, jLabelSerialAcq.getText());

                }
                itmNumAtt = itmNumAtt + 1;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void chgBank() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlbank = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ChgPaidTab] "
                    + "( PREV_SERIAL,PREV_REF_NUM,SERIAL,REF_NUM,BANK_NAME,"
                    + "ACCOUNT,AMOUNT,REG_MOD_VER,DOC_VER,ACT_REC_STA) VALUES (?,?,?,?,?,?,?,?,?,?)";

            pst = conn.prepareStatement(sqlbank);
            pst.setString(1, jLabelSerial.getText());
            pst.setString(2, jTextAcqRegNum.getText());
            pst.setString(3, jLabelSerialAcq.getText());
            pst.setString(4, jLabelRegNum.getText());
            pst.setString(5, jComboBankNam.getSelectedItem().toString());
            pst.setString(6, jLabelBankAccNo.getText());
            pst.setString(7, jTextPaidAmt.getText());
            pst.setString(8, "1");
            pst.setString(9, "1");
            pst.setString(10, "Q");

            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void noMileageConfirm(String action) {
        try {

            String sqlbank = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimTripNoMilageTab] "
                    + "( PREV_SERIAL,PREV_REF_NUM,SERIAL,REF_NUM,CONFIRM,ACT_REC_STA,"
                    + "DOC_STATUS,REG_MOD_VER) VALUES (?,?,?,?,?,?,?,?)";

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            pst = conn.prepareStatement(sqlbank);
            pst.setString(1, jLabelSerial.getText());
            pst.setString(2, jTextAcqRegNum.getText());
            pst.setString(3, jLabelSerialAcq.getText());
            pst.setString(4, jLabelRegNum.getText());
            pst.setString(5, action);
            pst.setString(6, "Q");
            pst.setString(7, "1");
            pst.setString(8, "1");

            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void createDepSlipRec() {
        try {

            String sqlbank = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimRefundDepositTab] "
                    + "(EMP_NUM,EMP_NAM,PREV_SERIAL,PREV_REF_NUM,SERIAL,REF_NUM,PLAN_WK,DOC_STATUS,DOC_VER)"
                    + " VALUES (?,?,?,?,?,?,?,?,?)";

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            pst = conn.prepareStatement(sqlbank);
            pst.setString(1, jLabelAcqEmpNum.getText());
            pst.setString(2, jLabelAcqEmpNam.getText());
            pst.setString(3, jLabelSerial.getText());
            pst.setString(4, jTextAcqRegNum.getText());
            pst.setString(5, jLabelSerialAcq.getText());
            pst.setString(6, jLabelRegNum.getText());
            pst.setString(7, Integer.toString(minWkNum));
            pst.setString(8, "Q");
            pst.setString(9, "1");

            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void payRecAck() {
        try {

            String sqlbank = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimPayRecTab] "
                    + "( EMP_NUM,EMP_NAM,PREV_SERIAL,PREV_REF_NUM,PREV_REF_DAT,SERIAL,"
                    + "REF_NUM,REF_DAT,REC_ACK,AMT_REC,DATE_REC,"
                    + "ACT_REC_STA,DOC_VER) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            pst = conn.prepareStatement(sqlbank);
            pst.setString(1, jLabelAcqEmpNum.getText());
            pst.setString(2, jLabelAcqEmpNam.getText());
            pst.setString(3, jLabelSerial.getText());
            pst.setString(4, jTextAcqRegNum.getText());
            pst.setString(5, jLabelRegDateAcq.getText());
            pst.setString(6, jLabelSerialAcq.getText());
            pst.setString(7, jLabelRegNum.getText());
            pst.setString(8, jLabelGenDate.getText());
            if (jRadioButtonPayRecYes.isSelected()) {
                pst.setString(9, "Y");
                pst.setString(10, jTextAmtRec.getText());
                java.util.Date jDateRec = jDateChooserDateRec.getDate();
                String dateRec = dfDate.format(jDateRec);
                pst.setString(11, dateRec);
            } else if (jRadioButtonPayRecNo.isSelected()) {
                pst.setString(9, "N");
                pst.setString(10, "0.00");
                pst.setString(11, "1900-01-01");
            } else {
                pst.setString(9, "R");
                pst.setString(10, "0.00");
                pst.setString(11, "1900-01-01");
            }
            pst.setString(12, "Q");
            pst.setString(13, "1");
            pst.executeUpdate();

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
                    + "WHERE SERIAL ='" + jLabelSerialAcq.getText() + "'");
            ResultSet r = st.getResultSet();

            while (r.next()) {
                refCheck = r.getInt(1);
            }

            st2.executeQuery("SELECT count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] "
                    + "where concat(SERIAL,REF_NUM) ='" + jLabelSerialAcq.getText() + jLabelRegNum.getText() + "'");
            ResultSet r2 = st2.getResultSet();

            while (r2.next()) {
                genCount = r2.getInt(1);
            }

            st3.executeQuery("SELECT count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                    + "where concat(SERIAL,REF_NUM) ='" + jLabelSerialAcq.getText() + jLabelRegNum.getText() + "'");
            ResultSet r3 = st3.getResultSet();

            while (r3.next()) {
                itmCount = r3.getInt(1);
            }

            st4.executeQuery("SELECT  count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] "
                    + "where concat(SERIAL,REF_NUM) ='" + jLabelSerialAcq.getText() + jLabelRegNum.getText() + "'");
            ResultSet r4 = st4.getResultSet();

            while (r4.next()) {
                wfCount = r4.getInt(1);
            }

            st1.executeQuery("SELECT count(*) FROM [ClaimsAppSysZvandiri].[dbo].[ClaimDocAttachTab] "
                    + "WHERE concat(SERIAL,REF_NUM) ='" + jLabelSerialAcq.getText() + jLabelRegNum.getText() + "'");
            ResultSet r1 = st1.getResultSet();

            while (r1.next()) {
                docAttDoc = r1.getInt(1);
            }

            st5.executeQuery("SELECT EMP_NUM,EMP_NAM FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] "
                    + "WHERE concat(SERIAL,REF_NUM) ='" + jLabelSerialAcq.getText() + jLabelRegNum.getText() + "'");
            ResultSet r5 = st5.getResultSet();

            while (r5.next()) {
                empNum = r5.getString(1);
                empNam = r5.getString(2);
            }
            System.out.println("ref " + refCheck + " " + jLabelRegNum.getText() + " gen " + genCount + " itm " + itmCount + " wf " + wfCount);

            if ((refCheck == (Integer.parseInt(jLabelRegNum.getText()))) && (genCount == 1) && (itmCount > 0)
                    && (wfCount == 1)
                    //&& (docAttDoc > 0) 
                    && (!("".equals(empNam))) && (!(empNam == null))
                    && (!("0".equals(empNum)))) {

                deleteLongLock();

                //   sendMail();
//                jDialogWaitingEmail.setVisible(false);
                jLabelRegNum.setVisible(true);
                jLabelRegYear.setVisible(true);
                jLabelSerial.setVisible(true);

                JOptionPane.showMessageDialog(this, "Perdiem Acquittal No. "
                        + jLabelRegYear.getText() + " " + jLabelSerialAcq.getText() + " " + jLabelRegNum.getText()
                        + " sucessfully created .");

                jDialogWaitingEmail.setVisible(true);

                String mailMsg = "<html><body> Dear " + supNam + "<br><br><b>"
                        + jLabelLineLogNam.getText() + "</b> has submitted a per diem acquittal for your review and approval.<br><br>"
                        + "Please check your Finance System inbox and action<br><br> Kind Regards <br><br>"
                        + " Finance Management System </body></html>";

                String MailMsgTitle = "Per Diem Acquittal - Reference No. " + jLabelSerial.getText() + " " + jLabelRegNum.getText() + " ";

                emSend.sendMail(MailMsgTitle, supUsrMail, mailMsg, "");

                jDialogWaitingEmail.setVisible(false);

                JOptionPane.showMessageDialog(null, "Email has been forwarded to " + supNam + " for approval of your acquittal No. "
                        + jLabelRegYear.getText() + " " + jLabelSerialAcq.getText() + " " + jLabelRegNum.getText());

                new JFrameAppAcquittal(jLabelEmp.getText()).setVisible(true);
                setVisible(false);

            } else {
                jLabelRegNum.setVisible(false);
                jLabelRegYear.setVisible(false);
                jLabelSerial.setVisible(false);
                regFail();
                JOptionPane.showMessageDialog(null, "<html> Registration failure. Registration falure "
                        + "can be caused by slow network speeds.<br><br> Please try again. "
                        + "If the problem persist contact IT.</html>");
                jMenuItemSubmit.setEnabled(true);

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    void regFail() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlDeleteLock = "delete [ClaimsAppSysZvandiri].[dbo].[SerialLock] where "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelSerialAcq.getText() + jLabelRegNum.getText() + "'";

            pst = conn.prepareStatement(sqlDeleteLock);
            pst.executeUpdate();

            String sqlDeleteGen = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] where "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelSerialAcq.getText() + jLabelRegNum.getText() + "'";
            pst = conn.prepareStatement(sqlDeleteGen);
            pst.executeUpdate();

            String sqlDeleteItm = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] where  "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelSerialAcq.getText() + jLabelRegNum.getText() + "'";
            pst = conn.prepareStatement(sqlDeleteItm);
            pst.executeUpdate();

            String sqlDeleteWF = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] where  "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelSerialAcq.getText() + jLabelRegNum.getText() + "'";
            pst = conn.prepareStatement(sqlDeleteWF);
            pst.executeUpdate();

            String sqlDeleteDocAtt = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimDocAttachTab] where "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelSerialAcq.getText() + jLabelRegNum.getText() + "'";
            pst = conn.prepareStatement(sqlDeleteDocAtt);
            pst.executeUpdate();

            String sqlDeleteWkReq = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab] where "
                    + "concat(PREV_SERIAL,PREV_REF_NUM) ='" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "' and PLAN_WK =" + minWkNum;
            pst = conn.prepareStatement(sqlDeleteWkReq);
            pst.executeUpdate();

            String sqlDeleteTripRep = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimTripReport] where "
                    + "concat(PREV_SERIAL,PREV_REF_NUM) ='" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "'";
            pst = conn.prepareStatement(sqlDeleteTripRep);
            pst.executeUpdate();

            String sqlDeletePayRec = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimPayRecTab] where "
                    + "concat(PREV_SERIAL,PREV_REF_NUM) ='" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "'";
            pst = conn.prepareStatement(sqlDeletePayRec);
            pst.executeUpdate();

            String sqlwkUpdateA = "update [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab] set "
                    + "ACQ_STA ='A' where "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "' and PLAN_WK =" + minWkNum;
            pst = conn.prepareStatement(sqlwkUpdateA);
            pst.executeUpdate();

            String sqlrefUpdate = "update [ClaimsAppSysZvandiri].[dbo].[refNumTab] set "
                    + "REF_NUM ='" + oldRefNum + "' where "
                    + "SERIAL ='A'";
            pst = conn.prepareStatement(sqlrefUpdate);
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
        jDialogWaiting = new javax.swing.JDialog();
        jDialogAuthority = new javax.swing.JDialog();
        jPanel7 = new javax.swing.JPanel();
        jLabelHeader = new javax.swing.JLabel();
        jLabelNotMsg = new javax.swing.JLabel();
        jLabelAutName = new javax.swing.JLabel();
        jButtonAuthOk = new javax.swing.JButton();
        jButtonAuthCancel = new javax.swing.JButton();
        jComboAuthNam = new javax.swing.JComboBox<>();
        jDialogAuthorityAll = new javax.swing.JDialog();
        jPanel8 = new javax.swing.JPanel();
        jLabelHeaderAll = new javax.swing.JLabel();
        jLabelNotMsgAll = new javax.swing.JLabel();
        jLabelAutNameAll = new javax.swing.JLabel();
        jButtonAuthAllOk = new javax.swing.JButton();
        jButtonAuthAllCancel = new javax.swing.JButton();
        jComboAuthNamAll = new javax.swing.JComboBox<>();
        buttonGroupLunch = new javax.swing.ButtonGroup();
        buttonGroupAcc = new javax.swing.ButtonGroup();
        jDialogWaitingEmail = new javax.swing.JDialog();
        buttonGroupChange = new javax.swing.ButtonGroup();
        jDialogChgPaid = new javax.swing.JDialog();
        jPanel10 = new javax.swing.JPanel();
        jLabelBankHeader = new javax.swing.JLabel();
        jLabelBankMsg = new javax.swing.JLabel();
        jButtonBankOk = new javax.swing.JButton();
        jButtonBankCancel = new javax.swing.JButton();
        jLabelBankName = new javax.swing.JLabel();
        jComboBankNam = new javax.swing.JComboBox<>();
        jLabelBankAccNo = new javax.swing.JLabel();
        jLabelPaidAmt = new javax.swing.JLabel();
        jLabelBankAcc = new javax.swing.JLabel();
        jTextPaidAmt = new javax.swing.JTextField();
        jDialogPayRec = new javax.swing.JDialog();
        jPanel11 = new javax.swing.JPanel();
        jLabelPayRecHeader = new javax.swing.JLabel();
        jLabelPayRecQues = new javax.swing.JLabel();
        jButtonPayRecCont = new javax.swing.JButton();
        jLabelDateRec = new javax.swing.JLabel();
        jLabelAmtRec = new javax.swing.JLabel();
        jTextAmtRec = new javax.swing.JTextField();
        jRadioButtonPayRecYes = new javax.swing.JRadioButton();
        jRadioButtonPayRecNo = new javax.swing.JRadioButton();
        jDateChooserDateRec = new com.toedter.calendar.JDateChooser();
        buttonGroupPayRec = new javax.swing.ButtonGroup();
        jDialogFacility = new javax.swing.JDialog();
        jPanel15 = new javax.swing.JPanel();
        jLabelHeader1 = new javax.swing.JLabel();
        jButtonOkFacility = new javax.swing.JButton();
        jButtonCancelFacility = new javax.swing.JButton();
        jLabelProvinceFacility = new javax.swing.JLabel();
        jComboProvinceFacility = new javax.swing.JComboBox<>();
        jComboDistrictFacility = new javax.swing.JComboBox<>();
        jLabelDistrictFacility = new javax.swing.JLabel();
        jLabelFacility = new javax.swing.JLabel();
        jComboFacility = new javax.swing.JComboBox<>();
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
        jLabel4 = new javax.swing.JLabel();
        jLabelActMainPurpose = new javax.swing.JLabel();
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
        jLabelAcqAmtPayBack = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabelAcqPayBack = new javax.swing.JLabel();
        jLabelOffice = new javax.swing.JLabel();
        jLabelAcqAccNum = new javax.swing.JLabel();
        jLabelACC = new javax.swing.JLabel();
        jLabelNum = new javax.swing.JLabel();
        jLabelSerial = new javax.swing.JLabel();
        jLabelAcqActMainPurpose = new javax.swing.JLabel();
        jLabelAcqProvince = new javax.swing.JLabel();
        jLabelAcqOffice = new javax.swing.JLabel();
        jTextAcqRegNum = new javax.swing.JTextField();
        jLabelAcqEmpNum = new javax.swing.JLabel();
        jLabelRegDateAcq = new javax.swing.JLabel();
        jLabelRegNum = new javax.swing.JLabel();
        jLabelRegYear = new javax.swing.JLabel();
        jLabelNum1 = new javax.swing.JLabel();
        jLabelSerialAcq = new javax.swing.JLabel();
        jLabelEmp = new javax.swing.JLabel();
        jLabelAcqAppTotReqCost = new javax.swing.JLabel();
        jLabelAppTotReq = new javax.swing.JLabel();
        jLabelAcqWk = new javax.swing.JLabel();
        jLabelAppWk1TotReq = new javax.swing.JLabel();
        jLabelAcqAppWk1TotReqCost = new javax.swing.JLabel();
        jLabelAppWk2TotReq = new javax.swing.JLabel();
        jLabelAcqAppWk2TotReqCost = new javax.swing.JLabel();
        jLabelAppWk3TotReq = new javax.swing.JLabel();
        jLabelAcqAppWk3TotReqCost = new javax.swing.JLabel();
        jLabelAppWk4TotReq = new javax.swing.JLabel();
        jLabelAcqAppWk4TotReqCost = new javax.swing.JLabel();
        jLabelAppWk5TotReq = new javax.swing.JLabel();
        jLabelAcqAppWk5TotReqCost = new javax.swing.JLabel();
        jLabelNum2 = new javax.swing.JLabel();
        jLabelRegYear1 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jButtonSave = new javax.swing.JButton();
        jPanelRequest = new javax.swing.JPanel();
        jLabelLogo1 = new javax.swing.JLabel();
        jLabelHeaderLine = new javax.swing.JLabel();
        jLabelLineDate = new javax.swing.JLabel();
        jLabelLineTime = new javax.swing.JLabel();
        jLabellogged = new javax.swing.JLabel();
        jLabelLineLogNam = new javax.swing.JLabel();
        jLabelAcqWk1 = new javax.swing.JLabel();
        jScrollPaneWk8 = new javax.swing.JScrollPane();
        jTableActivityReq = new javax.swing.JTable();
        jPanelAcquittal = new javax.swing.JPanel();
        jLabelLogo2 = new javax.swing.JLabel();
        jLabelHeaderLine1 = new javax.swing.JLabel();
        jLabelLineDate1 = new javax.swing.JLabel();
        jLabelLineTime1 = new javax.swing.JLabel();
        jLabellogged1 = new javax.swing.JLabel();
        jLabelLineLogNam1 = new javax.swing.JLabel();
        jToggleButtonNoActivity = new javax.swing.JToggleButton();
        jToggleButtonAllActivities = new javax.swing.JToggleButton();
        jLabelAcqWk2 = new javax.swing.JLabel();
        jScrollPaneWk9 = new javax.swing.JScrollPane();
        jTableActivityAcq = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jPanelActivityInfo = new javax.swing.JPanel();
        jLabeAccountCode = new javax.swing.JLabel();
        jComboAccountCode = new javax.swing.JComboBox<>();
        jLabelDonor = new javax.swing.JLabel();
        jComboDonor = new javax.swing.JComboBox<>();
        jLabelPrjCodeGL = new javax.swing.JLabel();
        jComboProjectCodeGL = new javax.swing.JComboBox<>();
        jLabelBudMainCode = new javax.swing.JLabel();
        jComboBudMainCode = new javax.swing.JComboBox<>();
        jLabelDialogWk1Site = new javax.swing.JLabel();
        jTextFieldDialogWkSite = new javax.swing.JTextField();
        jLabelWk1DialogActivityDesc = new javax.swing.JLabel();
        jTextFieldWk1DialogActivityDesc = new javax.swing.JTextField();
        jLabelPrjCodeProgramming = new javax.swing.JLabel();
        jComboProjectCodeProgramming = new javax.swing.JComboBox<>();
        jLabelRemain = new javax.swing.JLabel();
        jCheckBoxDialogWk1Dinner = new javax.swing.JCheckBox();
        jCheckBoxDialogWk1BrkFast = new javax.swing.JCheckBox();
        jCheckBoxDialogWk1Lunch = new javax.swing.JCheckBox();
        jCheckBoxDialogWk1Inc = new javax.swing.JCheckBox();
        jCheckBoxDialogWk1Misc = new javax.swing.JCheckBox();
        jLabelWk1Misc = new javax.swing.JLabel();
        jTextFieldWk1Misc = new javax.swing.JTextField();
        jLabelWk1MiscAmt = new javax.swing.JLabel();
        jTextFieldWk1MiscAmt = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jCheckBoxDialogWk1AccUnProved = new javax.swing.JCheckBox();
        jCheckBoxDialogWk1AccProved = new javax.swing.JCheckBox();
        jCheckBoxNoAcc = new javax.swing.JCheckBox();
        jLabelWk1Misc1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jDateChooserDialogActivityDateFrom = new com.toedter.calendar.JDateChooser();
        jDateChooserDialogActivityDateTo = new com.toedter.calendar.JDateChooser();
        jLabeAccountCode1 = new javax.swing.JLabel();
        jLabeAccountCode2 = new javax.swing.JLabel();
        jTabbedPaneAcqAtt = new javax.swing.JTabbedPane();
        jPanelReport = new javax.swing.JPanel();
        jPanelReportDetails = new javax.swing.JPanel();
        jLabelNamTravel = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextAreaNamTravel = new javax.swing.JTextArea();
        jLabelSupportAttachDoc = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
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
        jPanelAcqDocAtt1 = new javax.swing.JPanel();
        jPanelAtt1 = new javax.swing.JPanel();
        jButtonDeleteAtt1 = new javax.swing.JButton();
        jButtonChooseAtt1 = new javax.swing.JButton();
        jLabelLogo3 = new javax.swing.JLabel();
        jLabelHeaderLine2 = new javax.swing.JLabel();
        jLabelLineDate2 = new javax.swing.JLabel();
        jLabelLineTime2 = new javax.swing.JLabel();
        jLabellogged2 = new javax.swing.JLabel();
        jLabelLineLogNam7 = new javax.swing.JLabel();
        jScrollPaneAtt1 = new javax.swing.JScrollPane();
        jPanel9 = new javax.swing.JPanel();
        jLabelImgFile1 = new javax.swing.JLabel();
        jLabelAcqWk3 = new javax.swing.JLabel();
        jPanelAcqDocAtt2 = new javax.swing.JPanel();
        jPanelAtt2 = new javax.swing.JPanel();
        jButtonDeleteAtt2 = new javax.swing.JButton();
        jButtonChooseAtt2 = new javax.swing.JButton();
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
        jPanelAtt3 = new javax.swing.JPanel();
        jButtonDeleteAtt3 = new javax.swing.JButton();
        jButtonChooseAtt3 = new javax.swing.JButton();
        jLabelLogo5 = new javax.swing.JLabel();
        jLabelHeaderLine5 = new javax.swing.JLabel();
        jLabelLineDate5 = new javax.swing.JLabel();
        jLabelLineTime5 = new javax.swing.JLabel();
        jLabellogged5 = new javax.swing.JLabel();
        jLabelLineLogNam5 = new javax.swing.JLabel();
        jScrollPaneAtt3 = new javax.swing.JScrollPane();
        jPanel21 = new javax.swing.JPanel();
        jLabelImgFile3 = new javax.swing.JLabel();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuNew = new javax.swing.JMenu();
        jMenuItemPlanPerDiem = new javax.swing.JMenuItem();
        jSeparator13 = new javax.swing.JPopupMenu.Separator();
        jMenuItemPerDiemAcquittal = new javax.swing.JMenuItem();
        jSeparator17 = new javax.swing.JPopupMenu.Separator();
        jMenuEdit = new javax.swing.JMenu();
        jMenuAcqEdit = new javax.swing.JMenuItem();
        jSeparator30 = new javax.swing.JPopupMenu.Separator();
        jMenuMonPlanEdit = new javax.swing.JMenuItem();
        jSeparator25 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSubmit = new javax.swing.JMenuItem();
        jSeparator18 = new javax.swing.JPopupMenu.Separator();
        jMenuItemClose = new javax.swing.JMenuItem();
        jSeparator19 = new javax.swing.JPopupMenu.Separator();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuRequest = new javax.swing.JMenu();
        jMenuItemSupApp = new javax.swing.JMenuItem();
        jSeparator15 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAccMgrRev = new javax.swing.JMenuItem();
        jSeparator20 = new javax.swing.JPopupMenu.Separator();
        jMenuItemHeadApp = new javax.swing.JMenuItem();
        jSeparator21 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSchPerDiem = new javax.swing.JMenuItem();
        jSeparator23 = new javax.swing.JPopupMenu.Separator();
        jMenuItemView = new javax.swing.JMenuItem();
        jMenuAcquittal = new javax.swing.JMenu();
        jMenuItemAcqSupApp = new javax.swing.JMenuItem();
        jSeparator26 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqAccApp = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqHeadApp = new javax.swing.JMenuItem();
        jSeparator11 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqSchGen = new javax.swing.JMenuItem();
        jSeparator27 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqView = new javax.swing.JMenuItem();
        jMenuMonthlyPlan = new javax.swing.JMenu();
        jMenuPlanApproval = new javax.swing.JMenu();
        jMenuItemPlanSupApproval = new javax.swing.JMenuItem();
        jSeparator62 = new javax.swing.JPopupMenu.Separator();
        jMenuItemPlanFinApproval = new javax.swing.JMenuItem();
        jSeparator63 = new javax.swing.JPopupMenu.Separator();
        jMenuItemPlanHODApproval = new javax.swing.JMenuItem();
        jSeparator50 = new javax.swing.JPopupMenu.Separator();
        jMenuItemReqGenLst = new javax.swing.JMenuItem();
        jSeparator34 = new javax.swing.JPopupMenu.Separator();
        jMenuItemPlanView = new javax.swing.JMenuItem();
        jMenuReports = new javax.swing.JMenu();
        jMenuItemReqSubmitted = new javax.swing.JMenuItem();
        jSeparator22 = new javax.swing.JPopupMenu.Separator();
        jMenuItemUserCreate = new javax.swing.JMenuItem();
        jSeparator24 = new javax.swing.JPopupMenu.Separator();
        jMenuItemUserProfUpd = new javax.swing.JMenuItem();

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
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jDialogAuthority.setLocation(new java.awt.Point(400, 150));
        jDialogAuthority.setMinimumSize(new java.awt.Dimension(600, 300));
        jDialogAuthority.setResizable(false);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setLayout(null);

        jLabelHeader.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelHeader.setForeground(new java.awt.Color(0, 0, 204));
        jLabelHeader.setText("VERIFICATION SCREEN");
        jPanel7.add(jLabelHeader);
        jLabelHeader.setBounds(170, 10, 230, 40);

        jLabelNotMsg.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabelNotMsg.setText("<html>Please  note that you require authority to claim $10.00 lunch if your travel radius is less than 100 km from Base.<br><br> If you have authority please select authoriser from list below </html>");
        jPanel7.add(jLabelNotMsg);
        jLabelNotMsg.setBounds(10, 70, 570, 60);

        jLabelAutName.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabelAutName.setText("Name");
        jPanel7.add(jLabelAutName);
        jLabelAutName.setBounds(10, 140, 50, 30);

        jButtonAuthOk.setText("Ok ");
        jButtonAuthOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAuthOkActionPerformed(evt);
            }
        });
        jPanel7.add(jButtonAuthOk);
        jButtonAuthOk.setBounds(180, 190, 80, 21);

        jButtonAuthCancel.setText("Cancel");
        jButtonAuthCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAuthCancelActionPerformed(evt);
            }
        });
        jPanel7.add(jButtonAuthCancel);
        jButtonAuthCancel.setBounds(300, 190, 80, 21);

        jPanel7.add(jComboAuthNam);
        jComboAuthNam.setBounds(160, 140, 320, 30);

        javax.swing.GroupLayout jDialogAuthorityLayout = new javax.swing.GroupLayout(jDialogAuthority.getContentPane());
        jDialogAuthority.getContentPane().setLayout(jDialogAuthorityLayout);
        jDialogAuthorityLayout.setHorizontalGroup(
            jDialogAuthorityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
        );
        jDialogAuthorityLayout.setVerticalGroup(
            jDialogAuthorityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        jDialogAuthorityAll.setLocation(new java.awt.Point(400, 150));
        jDialogAuthorityAll.setMinimumSize(new java.awt.Dimension(600, 300));
        jDialogAuthorityAll.setResizable(false);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPanel8FocusGained(evt);
            }
        });
        jPanel8.setLayout(null);

        jLabelHeaderAll.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelHeaderAll.setForeground(new java.awt.Color(0, 0, 204));
        jLabelHeaderAll.setText("VERIFICATION SCREEN");
        jPanel8.add(jLabelHeaderAll);
        jLabelHeaderAll.setBounds(170, 10, 230, 40);

        jLabelNotMsgAll.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabelNotMsgAll.setText("<html>Please  note that you require authority to claim alowances if your travel radius is less than 100 km from Base.<br><br> If you have authority please select authoriser from list below </html>");
        jPanel8.add(jLabelNotMsgAll);
        jLabelNotMsgAll.setBounds(10, 70, 570, 60);

        jLabelAutNameAll.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabelAutNameAll.setText("Name");
        jPanel8.add(jLabelAutNameAll);
        jLabelAutNameAll.setBounds(10, 140, 50, 30);

        jButtonAuthAllOk.setText("Ok ");
        jButtonAuthAllOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAuthAllOkActionPerformed(evt);
            }
        });
        jPanel8.add(jButtonAuthAllOk);
        jButtonAuthAllOk.setBounds(180, 190, 80, 21);

        jButtonAuthAllCancel.setText("Cancel");
        jButtonAuthAllCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAuthAllCancelActionPerformed(evt);
            }
        });
        jPanel8.add(jButtonAuthAllCancel);
        jButtonAuthAllCancel.setBounds(300, 190, 80, 21);

        jComboAuthNamAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboAuthNamAllActionPerformed(evt);
            }
        });
        jPanel8.add(jComboAuthNamAll);
        jComboAuthNamAll.setBounds(160, 140, 320, 30);

        javax.swing.GroupLayout jDialogAuthorityAllLayout = new javax.swing.GroupLayout(jDialogAuthorityAll.getContentPane());
        jDialogAuthorityAll.getContentPane().setLayout(jDialogAuthorityAllLayout);
        jDialogAuthorityAllLayout.setHorizontalGroup(
            jDialogAuthorityAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
        );
        jDialogAuthorityAllLayout.setVerticalGroup(
            jDialogAuthorityAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
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

        jDialogChgPaid.setLocation(new java.awt.Point(400, 150));
        jDialogChgPaid.setMinimumSize(new java.awt.Dimension(600, 400));
        jDialogChgPaid.setResizable(false);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setMinimumSize(new java.awt.Dimension(600, 400));
        jPanel10.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPanel10FocusGained(evt);
            }
        });
        jPanel10.setLayout(null);

        jLabelBankHeader.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelBankHeader.setForeground(new java.awt.Color(0, 0, 204));
        jLabelBankHeader.setText("ACQUITTAL CHANGE PAID");
        jPanel10.add(jLabelBankHeader);
        jLabelBankHeader.setBounds(170, 10, 260, 40);

        jLabelBankMsg.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabelBankMsg.setText("Please indicate the account and change paid  ");
        jPanel10.add(jLabelBankMsg);
        jLabelBankMsg.setBounds(10, 60, 570, 30);

        jButtonBankOk.setText("Ok ");
        jButtonBankOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBankOkActionPerformed(evt);
            }
        });
        jPanel10.add(jButtonBankOk);
        jButtonBankOk.setBounds(190, 240, 80, 21);

        jButtonBankCancel.setText("Cancel");
        jButtonBankCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBankCancelActionPerformed(evt);
            }
        });
        jPanel10.add(jButtonBankCancel);
        jButtonBankCancel.setBounds(310, 240, 80, 21);

        jLabelBankName.setFont(new java.awt.Font("Times New Roman", 1, 10)); // NOI18N
        jLabelBankName.setText("OPHID Bank Account");
        jPanel10.add(jLabelBankName);
        jLabelBankName.setBounds(20, 110, 110, 30);

        jComboBankNam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Bank" }));
        jComboBankNam.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jComboBankNam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBankNamActionPerformed(evt);
            }
        });
        jPanel10.add(jComboBankNam);
        jComboBankNam.setBounds(200, 110, 280, 30);
        jPanel10.add(jLabelBankAccNo);
        jLabelBankAccNo.setBounds(200, 150, 280, 30);

        jLabelPaidAmt.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelPaidAmt.setText("Amount Paid");
        jPanel10.add(jLabelPaidAmt);
        jLabelPaidAmt.setBounds(20, 190, 110, 30);

        jLabelBankAcc.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelBankAcc.setText("Account Number");
        jPanel10.add(jLabelBankAcc);
        jLabelBankAcc.setBounds(20, 150, 110, 30);

        jTextPaidAmt.setText("0.00");
        jTextPaidAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextPaidAmtFocusGained(evt);
            }
        });
        jTextPaidAmt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextPaidAmtActionPerformed(evt);
            }
        });
        jPanel10.add(jTextPaidAmt);
        jTextPaidAmt.setBounds(200, 190, 100, 30);

        javax.swing.GroupLayout jDialogChgPaidLayout = new javax.swing.GroupLayout(jDialogChgPaid.getContentPane());
        jDialogChgPaid.getContentPane().setLayout(jDialogChgPaidLayout);
        jDialogChgPaidLayout.setHorizontalGroup(
            jDialogChgPaidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
        );
        jDialogChgPaidLayout.setVerticalGroup(
            jDialogChgPaidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 324, Short.MAX_VALUE)
        );

        jDialogPayRec.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jDialogPayRec.setLocation(new java.awt.Point(400, 150));
        jDialogPayRec.setMinimumSize(new java.awt.Dimension(540, 240));
        jDialogPayRec.setResizable(false);

        jPanel11.setBackground(new java.awt.Color(166, 32, 106));
        jPanel11.setMinimumSize(new java.awt.Dimension(540, 240));
        jPanel11.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPanel11FocusGained(evt);
            }
        });
        jPanel11.setLayout(null);

        jLabelPayRecHeader.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelPayRecHeader.setForeground(new java.awt.Color(255, 255, 255));
        jLabelPayRecHeader.setText("PERDIEM RECIEPT  DETAILS");
        jPanel11.add(jLabelPayRecHeader);
        jLabelPayRecHeader.setBounds(120, 0, 280, 40);

        jLabelPayRecQues.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        jLabelPayRecQues.setForeground(new java.awt.Color(255, 255, 255));
        jLabelPayRecQues.setText("Did you recieve your perdiem for this acquittal");
        jPanel11.add(jLabelPayRecQues);
        jLabelPayRecQues.setBounds(20, 50, 240, 25);

        jButtonPayRecCont.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonPayRecCont.setForeground(new java.awt.Color(0, 102, 51));
        jButtonPayRecCont.setText("Continue");
        jButtonPayRecCont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPayRecContActionPerformed(evt);
            }
        });
        jPanel11.add(jButtonPayRecCont);
        jButtonPayRecCont.setBounds(200, 170, 120, 21);

        jLabelDateRec.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        jLabelDateRec.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDateRec.setText("Date Recieved");
        jPanel11.add(jLabelDateRec);
        jLabelDateRec.setBounds(20, 90, 110, 25);

        jLabelAmtRec.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        jLabelAmtRec.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAmtRec.setText("Amount Recieved");
        jPanel11.add(jLabelAmtRec);
        jLabelAmtRec.setBounds(20, 130, 110, 25);

        jTextAmtRec.setText("0.00");
        jTextAmtRec.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextAmtRecFocusGained(evt);
            }
        });
        jTextAmtRec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAmtRecActionPerformed(evt);
            }
        });
        jPanel11.add(jTextAmtRec);
        jTextAmtRec.setBounds(200, 130, 100, 25);

        buttonGroupPayRec.add(jRadioButtonPayRecYes);
        jRadioButtonPayRecYes.setText(" Yes");
        jRadioButtonPayRecYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonPayRecYesActionPerformed(evt);
            }
        });
        jPanel11.add(jRadioButtonPayRecYes);
        jRadioButtonPayRecYes.setBounds(260, 50, 60, 25);

        buttonGroupPayRec.add(jRadioButtonPayRecNo);
        jRadioButtonPayRecNo.setText(" No");
        jRadioButtonPayRecNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonPayRecNoActionPerformed(evt);
            }
        });
        jPanel11.add(jRadioButtonPayRecNo);
        jRadioButtonPayRecNo.setBounds(340, 50, 50, 25);

        jDateChooserDateRec.setDateFormatString("yyyy-MM-dd");
        jPanel11.add(jDateChooserDateRec);
        jDateChooserDateRec.setBounds(200, 90, 130, 25);

        javax.swing.GroupLayout jDialogPayRecLayout = new javax.swing.GroupLayout(jDialogPayRec.getContentPane());
        jDialogPayRec.getContentPane().setLayout(jDialogPayRecLayout);
        jDialogPayRecLayout.setHorizontalGroup(
            jDialogPayRecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jDialogPayRecLayout.setVerticalGroup(
            jDialogPayRecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogPayRecLayout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );

        jDialogFacility.setAlwaysOnTop(true);
        jDialogFacility.setLocation(new java.awt.Point(450, 300));
        jDialogFacility.setMinimumSize(new java.awt.Dimension(600, 300));
        jDialogFacility.setResizable(false);

        jPanel15.setBackground(new java.awt.Color(204, 153, 14));
        jPanel15.setLayout(null);

        jLabelHeader1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelHeader1.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeader1.setText("FACILITY TO VISIT");
        jPanel15.add(jLabelHeader1);
        jLabelHeader1.setBounds(170, 10, 230, 40);

        jButtonOkFacility.setText("Ok ");
        jButtonOkFacility.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkFacilityActionPerformed(evt);
            }
        });
        jPanel15.add(jButtonOkFacility);
        jButtonOkFacility.setBounds(180, 220, 80, 21);

        jButtonCancelFacility.setText("Cancel");
        jButtonCancelFacility.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelFacilityActionPerformed(evt);
            }
        });
        jPanel15.add(jButtonCancelFacility);
        jButtonCancelFacility.setBounds(300, 220, 80, 21);

        jLabelProvinceFacility.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelProvinceFacility.setText("Province");
        jPanel15.add(jLabelProvinceFacility);
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
        jPanel15.add(jComboProvinceFacility);
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
        jPanel15.add(jComboDistrictFacility);
        jComboDistrictFacility.setBounds(150, 110, 230, 30);

        jLabelDistrictFacility.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDistrictFacility.setText("District");
        jPanel15.add(jLabelDistrictFacility);
        jLabelDistrictFacility.setBounds(20, 110, 70, 30);

        jLabelFacility.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelFacility.setText("Facility");
        jPanel15.add(jLabelFacility);
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
        jPanel15.add(jComboFacility);
        jComboFacility.setBounds(150, 150, 320, 30);

        javax.swing.GroupLayout jDialogFacilityLayout = new javax.swing.GroupLayout(jDialogFacility.getContentPane());
        jDialogFacility.getContentPane().setLayout(jDialogFacilityLayout);
        jDialogFacilityLayout.setHorizontalGroup(
            jDialogFacilityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
        );
        jDialogFacilityLayout.setVerticalGroup(
            jDialogFacilityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PERDIEM ACQUITTAL - CREATION");
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
        jLabelLogo.setBounds(10, 5, 220, 115);

        jLabelHeaderGen.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen.setText("TRAVEL AND SUBSISTENCE CLAIM ");
        jPanelGen.add(jLabelHeaderGen);
        jLabelHeaderGen.setBounds(350, 40, 650, 40);

        jLabelGenDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelGen.add(jLabelGenDate);
        jLabelGenDate.setBounds(1050, 0, 110, 30);

        jLabelGenTime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelGen.add(jLabelGenTime);
        jLabelGenTime.setBounds(1190, 0, 80, 30);

        jLabelGenLogNam1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam1.setText("Logged In");
        jPanelGen.add(jLabelGenLogNam1);
        jLabelGenLogNam1.setBounds(1050, 40, 100, 30);

        jLabelGenLogNam.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelGen.add(jLabelGenLogNam);
        jLabelGenLogNam.setBounds(1140, 40, 200, 30);

        jLabelEmpNum.setText("Employee Number");
        jPanelGen.add(jLabelEmpNum);
        jLabelEmpNum.setBounds(20, 190, 110, 30);

        jLabelAcqEmpNam.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqEmpNam);
        jLabelAcqEmpNam.setBounds(300, 200, 340, 30);
        jPanelGen.add(jSeparator1);
        jSeparator1.setBounds(10, 190, 1460, 2);

        jLabelSepDetails.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelSepDetails.setForeground(new java.awt.Color(0, 0, 204));
        jLabelSepDetails.setText("Employee Details");
        jPanelGen.add(jLabelSepDetails);
        jLabelSepDetails.setBounds(20, 167, 140, 30);

        jLabelProvince.setText("Province");
        jPanelGen.add(jLabelProvince);
        jLabelProvince.setBounds(20, 220, 70, 30);

        jLabelBank.setText("Bank");
        jPanelGen.add(jLabelBank);
        jLabelBank.setBounds(20, 250, 40, 30);

        jLabelAcqBankName.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqBankName);
        jLabelAcqBankName.setBounds(130, 250, 220, 30);
        jPanelGen.add(jSeparator2);
        jSeparator2.setBounds(10, 305, 1460, 2);

        jLabelAcqEmpTitle.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqEmpTitle);
        jLabelAcqEmpTitle.setBounds(710, 200, 400, 30);

        jLabel4.setText("Financial Details");
        jPanelGen.add(jLabel4);
        jLabel4.setBounds(20, 280, 110, 30);

        jLabelActMainPurpose.setText("Activity Main Purpose");
        jPanelGen.add(jLabelActMainPurpose);
        jLabelActMainPurpose.setBounds(20, 320, 130, 30);

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
        jLabelAllReq.setText("Req");
        jPanel1.add(jLabelAllReq);
        jLabelAllReq.setBounds(100, 5, 22, 25);

        jLabelAllAcq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAllAcq.setForeground(new java.awt.Color(255, 0, 0));
        jLabelAllAcq.setText("Acq");
        jPanel1.add(jLabelAllAcq);
        jLabelAllAcq.setBounds(170, 5, 21, 25);

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
        jPanel1.setBounds(20, 360, 320, 150);

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
        jLabelMiscReq.setText("Req");
        jPanel3.add(jLabelMiscReq);
        jLabelMiscReq.setBounds(110, 5, 22, 25);

        jLabelMiscAcq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelMiscAcq.setForeground(new java.awt.Color(255, 0, 0));
        jLabelMiscAcq.setText("Acq");
        jPanel3.add(jLabelMiscAcq);
        jLabelMiscAcq.setBounds(180, 5, 21, 25);

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
        jPanel3.setBounds(360, 360, 320, 150);

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
        jLabelAccAcq.setText("Acq");
        jPanel4.add(jLabelAccAcq);
        jLabelAccAcq.setBounds(170, 5, 21, 25);

        jLabelAccBal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAccBal.setText("Balance");
        jPanel4.add(jLabelAccBal);
        jLabelAccBal.setBounds(252, 5, 60, 25);

        jLabelAccReq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAccReq.setForeground(new java.awt.Color(0, 102, 0));
        jLabelAccReq.setText("Req");
        jPanel4.add(jLabelAccReq);
        jLabelAccReq.setBounds(110, 5, 30, 25);

        jLabelAcqAccUnprovedSubTot.setForeground(new java.awt.Color(255, 51, 51));
        jLabelAcqAccUnprovedSubTot.setText("0.00");
        jPanel4.add(jLabelAcqAccUnprovedSubTot);
        jLabelAcqAccUnprovedSubTot.setBounds(170, 30, 50, 25);

        jLabelAcqAccProvedSubTotBal.setText("0.00");
        jPanel4.add(jLabelAcqAccProvedSubTotBal);
        jLabelAcqAccProvedSubTotBal.setBounds(250, 60, 50, 25);

        jPanelGen.add(jPanel4);
        jPanel4.setBounds(700, 360, 320, 150);

        jLabelAcqAmtPayBack.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabelAcqAmtPayBack.setForeground(new java.awt.Color(255, 180, 40));
        jPanelGen.add(jLabelAcqAmtPayBack);
        jLabelAcqAmtPayBack.setBounds(1240, 500, 60, 30);

        jLabel35.setText("Miscellaneous");
        jPanelGen.add(jLabel35);
        jLabel35.setBounds(8, 30, 70, 25);

        jLabelAcqPayBack.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabelAcqPayBack.setForeground(new java.awt.Color(255, 180, 40));
        jLabelAcqPayBack.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jPanelGen.add(jLabelAcqPayBack);
        jLabelAcqPayBack.setBounds(1030, 500, 190, 30);

        jLabelOffice.setText("Office");
        jPanelGen.add(jLabelOffice);
        jLabelOffice.setBounds(580, 220, 70, 30);

        jLabelAcqAccNum.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqAccNum);
        jLabelAcqAccNum.setBounds(710, 250, 130, 30);

        jLabelACC.setText("Account No.");
        jPanelGen.add(jLabelACC);
        jLabelACC.setBounds(580, 250, 80, 30);

        jLabelNum.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNum.setText("Request No.");
        jPanelGen.add(jLabelNum);
        jLabelNum.setBounds(20, 130, 80, 30);

        jLabelSerial.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSerial.setText("R");
        jPanelGen.add(jLabelSerial);
        jLabelSerial.setBounds(110, 130, 20, 30);

        jLabelAcqActMainPurpose.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqActMainPurpose);
        jLabelAcqActMainPurpose.setBounds(160, 320, 750, 30);

        jLabelAcqProvince.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqProvince);
        jLabelAcqProvince.setBounds(130, 220, 210, 30);

        jLabelAcqOffice.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqOffice);
        jLabelAcqOffice.setBounds(710, 220, 290, 30);

        jTextAcqRegNum.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextAcqRegNumFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextAcqRegNumFocusLost(evt);
            }
        });
        jTextAcqRegNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAcqRegNumActionPerformed(evt);
            }
        });
        jTextAcqRegNum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextAcqRegNumKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextAcqRegNumKeyTyped(evt);
            }
        });
        jPanelGen.add(jTextAcqRegNum);
        jTextAcqRegNum.setBounds(140, 130, 50, 30);

        jLabelAcqEmpNum.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqEmpNum);
        jLabelAcqEmpNum.setBounds(130, 200, 60, 30);

        jLabelRegDateAcq.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelRegDateAcq);
        jLabelRegDateAcq.setBounds(210, 130, 160, 30);

        jLabelRegNum.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelRegNum.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelRegNum);
        jLabelRegNum.setBounds(1250, 80, 60, 30);

        jLabelRegYear.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelRegYear.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelRegYear);
        jLabelRegYear.setBounds(1190, 80, 30, 30);

        jLabelNum1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNum1.setText("Reference No.");
        jPanelGen.add(jLabelNum1);
        jLabelNum1.setBounds(1050, 80, 100, 30);

        jLabelSerialAcq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelSerialAcq.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelSerialAcq);
        jLabelSerialAcq.setBounds(1230, 80, 15, 30);
        jPanelGen.add(jLabelEmp);
        jLabelEmp.setBounds(1010, 120, 80, 30);

        jLabelAcqAppTotReqCost.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelAcqAppTotReqCost.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAcqAppTotReqCost.setText("0");
        jPanelGen.add(jLabelAcqAppTotReqCost);
        jLabelAcqAppTotReqCost.setBounds(1240, 470, 60, 30);

        jLabelAppTotReq.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelAppTotReq.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAppTotReq.setText("Total (Cash On hand)");
        jPanelGen.add(jLabelAppTotReq);
        jLabelAppTotReq.setBounds(1030, 470, 140, 30);

        jLabelAcqWk.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        jLabelAcqWk.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqWk);
        jLabelAcqWk.setBounds(550, 90, 270, 25);

        jLabelAppWk1TotReq.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelAppWk1TotReq.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAppWk1TotReq.setText("Total Cleared - Week");
        jPanelGen.add(jLabelAppWk1TotReq);
        jLabelAppWk1TotReq.setBounds(1030, 450, 150, 25);

        jLabelAcqAppWk1TotReqCost.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelAcqAppWk1TotReqCost.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAcqAppWk1TotReqCost.setText("0");
        jPanelGen.add(jLabelAcqAppWk1TotReqCost);
        jLabelAcqAppWk1TotReqCost.setBounds(1240, 450, 60, 25);

        jLabelAppWk2TotReq.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelAppWk2TotReq.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAppWk2TotReq.setText("Total - Week");
        jPanelGen.add(jLabelAppWk2TotReq);
        jLabelAppWk2TotReq.setBounds(1030, 430, 160, 25);

        jLabelAcqAppWk2TotReqCost.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelAcqAppWk2TotReqCost.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAcqAppWk2TotReqCost.setText("0");
        jPanelGen.add(jLabelAcqAppWk2TotReqCost);
        jLabelAcqAppWk2TotReqCost.setBounds(1240, 430, 60, 25);

        jLabelAppWk3TotReq.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelAppWk3TotReq.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAppWk3TotReq.setText("Total - Week");
        jPanelGen.add(jLabelAppWk3TotReq);
        jLabelAppWk3TotReq.setBounds(1030, 410, 160, 25);

        jLabelAcqAppWk3TotReqCost.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelAcqAppWk3TotReqCost.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAcqAppWk3TotReqCost.setText("0");
        jPanelGen.add(jLabelAcqAppWk3TotReqCost);
        jLabelAcqAppWk3TotReqCost.setBounds(1240, 410, 60, 25);

        jLabelAppWk4TotReq.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelAppWk4TotReq.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAppWk4TotReq.setText("Total - Week");
        jPanelGen.add(jLabelAppWk4TotReq);
        jLabelAppWk4TotReq.setBounds(1030, 390, 160, 25);

        jLabelAcqAppWk4TotReqCost.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelAcqAppWk4TotReqCost.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAcqAppWk4TotReqCost.setText("0");
        jPanelGen.add(jLabelAcqAppWk4TotReqCost);
        jLabelAcqAppWk4TotReqCost.setBounds(1240, 390, 60, 25);

        jLabelAppWk5TotReq.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelAppWk5TotReq.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAppWk5TotReq.setText("Total - Week");
        jPanelGen.add(jLabelAppWk5TotReq);
        jLabelAppWk5TotReq.setBounds(1030, 370, 160, 25);

        jLabelAcqAppWk5TotReqCost.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelAcqAppWk5TotReqCost.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAcqAppWk5TotReqCost.setText("0");
        jPanelGen.add(jLabelAcqAppWk5TotReqCost);
        jLabelAcqAppWk5TotReqCost.setBounds(1240, 370, 60, 25);

        jLabelNum2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNum2.setText("Reference No.");
        jPanelGen.add(jLabelNum2);
        jLabelNum2.setBounds(1050, 80, 100, 30);

        jLabelRegYear1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelRegYear1.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelRegYear1);
        jLabelRegYear1.setBounds(1200, 80, 30, 30);

        jPanel12.setBackground(new java.awt.Color(100, 100, 247));
        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        jPanel12.setForeground(new java.awt.Color(0, 153, 0));
        jPanel12.setLayout(null);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Record Validation");
        jLabel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel12.add(jLabel5);
        jLabel5.setBounds(520, 10, 190, 25);

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
        jPanel12.add(jButtonSave);
        jButtonSave.setBounds(420, 40, 380, 50);

        jPanelGen.add(jPanel12);
        jPanel12.setBounds(20, 540, 1320, 100);

        jTabbedPaneAppSys.addTab("User and Accounting Details", jPanelGen);

        jPanelRequest.setBackground(new java.awt.Color(100, 100, 247));
        jPanelRequest.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jPanelRequest.setPreferredSize(new java.awt.Dimension(1280, 677));
        jPanelRequest.setLayout(null);

        jLabelLogo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelRequest.add(jLabelLogo1);
        jLabelLogo1.setBounds(10, 5, 220, 115);

        jLabelHeaderLine.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelRequest.add(jLabelHeaderLine);
        jLabelHeaderLine.setBounds(400, 40, 610, 40);

        jLabelLineDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelRequest.add(jLabelLineDate);
        jLabelLineDate.setBounds(1150, 0, 110, 30);

        jLabelLineTime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelRequest.add(jLabelLineTime);
        jLabelLineTime.setBounds(1290, 0, 80, 30);

        jLabellogged.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged.setText("Logged In");
        jPanelRequest.add(jLabellogged);
        jLabellogged.setBounds(1150, 40, 80, 30);

        jLabelLineLogNam.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam.setText("User Name");
        jPanelRequest.add(jLabelLineLogNam);
        jLabelLineLogNam.setBounds(1230, 40, 250, 30);

        jLabelAcqWk1.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        jLabelAcqWk1.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequest.add(jLabelAcqWk1);
        jLabelAcqWk1.setBounds(550, 80, 270, 25);

        jTableActivityReq.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Account Code", "Donor", "Project Code GL", "Project Code Program", "Project Name Program", "Budget Line", "Budget Code", "Site to Visit", "Activity", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Line Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk8.setViewportView(jTableActivityReq);

        jPanelRequest.add(jScrollPaneWk8);
        jScrollPaneWk8.setBounds(10, 170, 1340, 470);

        jTabbedPaneAppSys.addTab("Perdiem Request", jPanelRequest);

        jPanelAcquittal.setBackground(new java.awt.Color(235, 185, 235));
        jPanelAcquittal.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jPanelAcquittal.setPreferredSize(new java.awt.Dimension(1280, 677));
        jPanelAcquittal.setLayout(null);

        jLabelLogo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelAcquittal.add(jLabelLogo2);
        jLabelLogo2.setBounds(10, 2, 220, 115);

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
                "Date", "Account Code", "Donor", "Project Code GL", "Project Code Program", "Project Name Program", "Budget Line", "Budget Code", "Site to Visit", "Activity", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Line Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk9.setViewportView(jTableActivityAcq);

        jPanelAcquittal.add(jScrollPaneWk9);
        jScrollPaneWk9.setBounds(300, 120, 1060, 530);

        jPanel6.setLayout(null);

        jPanelActivityInfo.setBackground(new java.awt.Color(237, 235, 92));
        jPanelActivityInfo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(204, 204, 204), null, null));
        jPanelActivityInfo.setLayout(null);

        jLabeAccountCode.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabeAccountCode.setText("To");
        jPanelActivityInfo.add(jLabeAccountCode);
        jLabeAccountCode.setBounds(160, 15, 20, 20);

        jComboAccountCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboAccountCodeMouseClicked(evt);
            }
        });
        jComboAccountCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboAccountCodeActionPerformed(evt);
            }
        });
        jPanelActivityInfo.add(jComboAccountCode);
        jComboAccountCode.setBounds(5, 57, 290, 25);

        jLabelDonor.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelDonor.setText("Donor");
        jPanelActivityInfo.add(jLabelDonor);
        jLabelDonor.setBounds(5, 79, 260, 20);

        jComboDonor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboDonorMouseClicked(evt);
            }
        });
        jComboDonor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboDonorActionPerformed(evt);
            }
        });
        jPanelActivityInfo.add(jComboDonor);
        jComboDonor.setBounds(5, 97, 290, 25);

        jLabelPrjCodeGL.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelPrjCodeGL.setText("Project Code (GL)");
        jPanelActivityInfo.add(jLabelPrjCodeGL);
        jLabelPrjCodeGL.setBounds(5, 120, 260, 20);

        jComboProjectCodeGL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboProjectCodeGLMouseClicked(evt);
            }
        });
        jComboProjectCodeGL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboProjectCodeGLActionPerformed(evt);
            }
        });
        jPanelActivityInfo.add(jComboProjectCodeGL);
        jComboProjectCodeGL.setBounds(5, 140, 290, 25);

        jLabelBudMainCode.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelBudMainCode.setText("Budget line");
        jPanelActivityInfo.add(jLabelBudMainCode);
        jLabelBudMainCode.setBounds(5, 205, 260, 20);

        jComboBudMainCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBudMainCodeMouseClicked(evt);
            }
        });
        jComboBudMainCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBudMainCodeActionPerformed(evt);
            }
        });
        jPanelActivityInfo.add(jComboBudMainCode);
        jComboBudMainCode.setBounds(5, 225, 290, 25);

        jLabelDialogWk1Site.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelDialogWk1Site.setText("Site to be Visited");
        jPanelActivityInfo.add(jLabelDialogWk1Site);
        jLabelDialogWk1Site.setBounds(5, 250, 260, 20);

        jTextFieldDialogWkSite.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldDialogWkSiteMouseClicked(evt);
            }
        });
        jTextFieldDialogWkSite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldDialogWkSiteActionPerformed(evt);
            }
        });
        jTextFieldDialogWkSite.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldDialogWkSiteKeyTyped(evt);
            }
        });
        jPanelActivityInfo.add(jTextFieldDialogWkSite);
        jTextFieldDialogWkSite.setBounds(5, 270, 290, 25);

        jLabelWk1DialogActivityDesc.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelWk1DialogActivityDesc.setText("Activity Descrpition");
        jPanelActivityInfo.add(jLabelWk1DialogActivityDesc);
        jLabelWk1DialogActivityDesc.setBounds(5, 292, 260, 20);
        jPanelActivityInfo.add(jTextFieldWk1DialogActivityDesc);
        jTextFieldWk1DialogActivityDesc.setBounds(5, 310, 290, 25);

        jLabelPrjCodeProgramming.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelPrjCodeProgramming.setText("Project Code (Programming)");
        jPanelActivityInfo.add(jLabelPrjCodeProgramming);
        jLabelPrjCodeProgramming.setBounds(5, 161, 260, 20);

        jComboProjectCodeProgramming.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboProjectCodeProgrammingMouseClicked(evt);
            }
        });
        jPanelActivityInfo.add(jComboProjectCodeProgramming);
        jComboProjectCodeProgramming.setBounds(5, 180, 290, 25);

        jLabelRemain.setFont(new java.awt.Font("Tahoma", 3, 9)); // NOI18N
        jPanelActivityInfo.add(jLabelRemain);
        jLabelRemain.setBounds(280, 350, 210, 20);

        jCheckBoxDialogWk1Dinner.setText(" Dinner");
        jPanelActivityInfo.add(jCheckBoxDialogWk1Dinner);
        jCheckBoxDialogWk1Dinner.setBounds(190, 340, 70, 25);

        jCheckBoxDialogWk1BrkFast.setText(" Breakfast");
        jPanelActivityInfo.add(jCheckBoxDialogWk1BrkFast);
        jCheckBoxDialogWk1BrkFast.setBounds(5, 340, 80, 25);

        jCheckBoxDialogWk1Lunch.setText("Lunch");
        jPanelActivityInfo.add(jCheckBoxDialogWk1Lunch);
        jCheckBoxDialogWk1Lunch.setBounds(90, 340, 80, 25);

        jCheckBoxDialogWk1Inc.setText("Incidental");
        jPanelActivityInfo.add(jCheckBoxDialogWk1Inc);
        jCheckBoxDialogWk1Inc.setBounds(5, 370, 110, 25);

        jCheckBoxDialogWk1Misc.setText("Miscellaneous");
        jCheckBoxDialogWk1Misc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDialogWk1MiscActionPerformed(evt);
            }
        });
        jPanelActivityInfo.add(jCheckBoxDialogWk1Misc);
        jCheckBoxDialogWk1Misc.setBounds(130, 370, 110, 25);

        jLabelWk1Misc.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelWk1Misc.setText("Accommodation");
        jPanelActivityInfo.add(jLabelWk1Misc);
        jLabelWk1Misc.setBounds(5, 430, 160, 20);

        jTextFieldWk1Misc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldWk1MiscActionPerformed(evt);
            }
        });
        jPanelActivityInfo.add(jTextFieldWk1Misc);
        jTextFieldWk1Misc.setBounds(5, 407, 110, 25);

        jLabelWk1MiscAmt.setText("$");
        jPanelActivityInfo.add(jLabelWk1MiscAmt);
        jLabelWk1MiscAmt.setBounds(150, 407, 30, 25);
        jPanelActivityInfo.add(jTextFieldWk1MiscAmt);
        jTextFieldWk1MiscAmt.setBounds(190, 407, 70, 25);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setLayout(null);

        jCheckBoxDialogWk1AccUnProved.setText(" Unproved ");
        jPanel13.add(jCheckBoxDialogWk1AccUnProved);
        jCheckBoxDialogWk1AccUnProved.setBounds(0, 5, 80, 21);

        jCheckBoxDialogWk1AccProved.setText(" Proved ");
        jPanel13.add(jCheckBoxDialogWk1AccProved);
        jCheckBoxDialogWk1AccProved.setBounds(100, 5, 80, 21);

        jCheckBoxNoAcc.setText("No Acc ");
        jPanel13.add(jCheckBoxNoAcc);
        jCheckBoxNoAcc.setBounds(190, 5, 80, 21);

        jPanelActivityInfo.add(jPanel13);
        jPanel13.setBounds(2, 450, 290, 30);

        jLabelWk1Misc1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelWk1Misc1.setText("Miscellaneous Desc");
        jPanelActivityInfo.add(jLabelWk1Misc1);
        jLabelWk1Misc1.setBounds(5, 390, 160, 20);

        jButton1.setText("delete");
        jPanelActivityInfo.add(jButton1);
        jButton1.setBounds(130, 490, 90, 30);

        jButton2.setText("Add");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanelActivityInfo.add(jButton2);
        jButton2.setBounds(10, 490, 90, 30);

        jDateChooserDialogActivityDateFrom.setDateFormatString("yyyy-MM-dd");
        jPanelActivityInfo.add(jDateChooserDialogActivityDateFrom);
        jDateChooserDialogActivityDateFrom.setBounds(35, 10, 120, 30);

        jDateChooserDialogActivityDateTo.setDateFormatString("yyyy-MM-dd");
        jPanelActivityInfo.add(jDateChooserDialogActivityDateTo);
        jDateChooserDialogActivityDateTo.setBounds(175, 10, 120, 30);

        jLabeAccountCode1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabeAccountCode1.setText("Account Code");
        jPanelActivityInfo.add(jLabeAccountCode1);
        jLabeAccountCode1.setBounds(5, 40, 260, 20);

        jLabeAccountCode2.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabeAccountCode2.setText("From");
        jPanelActivityInfo.add(jLabeAccountCode2);
        jLabeAccountCode2.setBounds(5, 15, 30, 20);

        jPanel6.add(jPanelActivityInfo);
        jPanelActivityInfo.setBounds(0, 0, 300, 530);

        jPanelAcquittal.add(jPanel6);
        jPanel6.setBounds(0, 120, 300, 530);

        jTabbedPaneAppSys.addTab("Perdiem Acquittal", jPanelAcquittal);

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

        jLabelSupportAttachDoc.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelSupportAttachDoc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelSupportAttachDoc.setText("                                                     Reports Attachments");
        jPanelReportDetails.add(jLabelSupportAttachDoc);
        jLabelSupportAttachDoc.setBounds(280, 140, 860, 25);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(null);

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

        jPanel5.add(jPanelAttach);
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

        jPanel5.add(jScrollPane10);
        jScrollPane10.setBounds(310, 20, 1020, 230);

        jPanelReportDetails.add(jPanel5);
        jPanel5.setBounds(0, 180, 1360, 260);

        javax.swing.GroupLayout jPanelReportLayout = new javax.swing.GroupLayout(jPanelReport);
        jPanelReport.setLayout(jPanelReportLayout);
        jPanelReportLayout.setHorizontalGroup(
            jPanelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelReportDetails, javax.swing.GroupLayout.DEFAULT_SIZE, 1358, Short.MAX_VALUE)
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
        jLabelLogo6.setBounds(10, 5, 220, 115);

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

        jPanelAcqDocAtt1.setBackground(new java.awt.Color(226, 255, 255));
        jPanelAcqDocAtt1.setMinimumSize(new java.awt.Dimension(1270, 671));
        jPanelAcqDocAtt1.setPreferredSize(new java.awt.Dimension(1270, 671));
        jPanelAcqDocAtt1.setLayout(null);

        jPanelAtt1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelAtt1.setLayout(null);

        jButtonDeleteAtt1.setText("Delete  Attachment 1");
        jButtonDeleteAtt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteAtt1ActionPerformed(evt);
            }
        });
        jPanelAtt1.add(jButtonDeleteAtt1);
        jButtonDeleteAtt1.setBounds(10, 210, 205, 21);

        jButtonChooseAtt1.setText("Choose Attachment 1");
        jButtonChooseAtt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonChooseAtt1ActionPerformed(evt);
            }
        });
        jPanelAtt1.add(jButtonChooseAtt1);
        jButtonChooseAtt1.setBounds(11, 126, 205, 21);

        jPanelAcqDocAtt1.add(jPanelAtt1);
        jPanelAtt1.setBounds(5, 115, 240, 505);

        jLabelLogo3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelAcqDocAtt1.add(jLabelLogo3);
        jLabelLogo3.setBounds(10, 5, 220, 115);

        jLabelHeaderLine2.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine2.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelAcqDocAtt1.add(jLabelHeaderLine2);
        jLabelHeaderLine2.setBounds(350, 40, 610, 40);

        jLabelLineDate2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate2.setText("29 November 2018");
        jPanelAcqDocAtt1.add(jLabelLineDate2);
        jLabelLineDate2.setBounds(1080, 0, 110, 30);

        jLabelLineTime2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime2.setText("15:20:30");
        jPanelAcqDocAtt1.add(jLabelLineTime2);
        jLabelLineTime2.setBounds(1220, 0, 80, 30);

        jLabellogged2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged2.setText("Logged In");
        jPanelAcqDocAtt1.add(jLabellogged2);
        jLabellogged2.setBounds(1080, 40, 80, 30);

        jLabelLineLogNam7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam7.setText("User Name");
        jPanelAcqDocAtt1.add(jLabelLineLogNam7);
        jLabelLineLogNam7.setBounds(1160, 40, 190, 30);

        jScrollPaneAtt1.setPreferredSize(new java.awt.Dimension(1000, 480));

        jPanel9.setPreferredSize(new java.awt.Dimension(998, 484));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(244, 244, 244)
                .addComponent(jLabelImgFile1, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                .addContainerGap(224, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabelImgFile1, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)
        );

        jScrollPaneAtt1.setViewportView(jPanel9);

        jPanelAcqDocAtt1.add(jScrollPaneAtt1);
        jScrollPaneAtt1.setBounds(260, 115, 1090, 505);

        jLabelAcqWk3.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        jLabelAcqWk3.setForeground(new java.awt.Color(0, 102, 51));
        jLabelAcqWk3.setText("jLabel2");
        jPanelAcqDocAtt1.add(jLabelAcqWk3);
        jLabelAcqWk3.setBounds(550, 80, 270, 25);

        jTabbedPaneAcqAtt.addTab("Attachment 2", jPanelAcqDocAtt1);

        jPanelAcqDocAtt2.setBackground(new java.awt.Color(226, 255, 226));
        jPanelAcqDocAtt2.setMinimumSize(new java.awt.Dimension(1270, 671));
        jPanelAcqDocAtt2.setLayout(null);

        jPanelAtt2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelAtt2.setLayout(null);

        jButtonDeleteAtt2.setText("Delete Attachment 2");
        jButtonDeleteAtt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteAtt2ActionPerformed(evt);
            }
        });
        jPanelAtt2.add(jButtonDeleteAtt2);
        jButtonDeleteAtt2.setBounds(10, 210, 205, 21);

        jButtonChooseAtt2.setText("Choose Attachment 2");
        jButtonChooseAtt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonChooseAtt2ActionPerformed(evt);
            }
        });
        jPanelAtt2.add(jButtonChooseAtt2);
        jButtonChooseAtt2.setBounds(11, 126, 205, 21);

        jPanelAcqDocAtt2.add(jPanelAtt2);
        jPanelAtt2.setBounds(5, 115, 240, 505);

        jLabelLogo4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelAcqDocAtt2.add(jLabelLogo4);
        jLabelLogo4.setBounds(10, 5, 220, 115);

        jLabelHeaderLine4.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine4.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelAcqDocAtt2.add(jLabelHeaderLine4);
        jLabelHeaderLine4.setBounds(350, 40, 610, 40);

        jLabelLineDate4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate4.setText("29 November 2018");
        jPanelAcqDocAtt2.add(jLabelLineDate4);
        jLabelLineDate4.setBounds(1080, 0, 110, 30);

        jLabelLineTime4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime4.setText("15:20:30");
        jPanelAcqDocAtt2.add(jLabelLineTime4);
        jLabelLineTime4.setBounds(1220, 0, 80, 30);

        jLabellogged4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged4.setText("Logged In");
        jPanelAcqDocAtt2.add(jLabellogged4);
        jLabellogged4.setBounds(1080, 40, 80, 30);

        jLabelLineLogNam4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam4.setText("User Name");
        jPanelAcqDocAtt2.add(jLabelLineLogNam4);
        jLabelLineLogNam4.setBounds(1170, 40, 190, 30);

        jScrollPaneAtt2.setPreferredSize(new java.awt.Dimension(1000, 480));

        jPanel19.setPreferredSize(new java.awt.Dimension(998, 484));

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(244, 244, 244)
                .addComponent(jLabelImgFile2, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
                .addContainerGap(289, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(jLabelImgFile2, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jScrollPaneAtt2.setViewportView(jPanel19);

        jPanelAcqDocAtt2.add(jScrollPaneAtt2);
        jScrollPaneAtt2.setBounds(260, 115, 1090, 505);

        jTabbedPaneAcqAtt.addTab("Attachment 3", jPanelAcqDocAtt2);

        jPanelAcqDocAtt3.setBackground(new java.awt.Color(255, 255, 204));
        jPanelAcqDocAtt3.setMinimumSize(new java.awt.Dimension(1270, 671));
        jPanelAcqDocAtt3.setLayout(null);

        jPanelAtt3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelAtt3.setLayout(null);

        jButtonDeleteAtt3.setText("Delete Attachment 3");
        jButtonDeleteAtt3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteAtt3ActionPerformed(evt);
            }
        });
        jPanelAtt3.add(jButtonDeleteAtt3);
        jButtonDeleteAtt3.setBounds(10, 210, 205, 21);

        jButtonChooseAtt3.setText("Choose Attachment 3");
        jButtonChooseAtt3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonChooseAtt3ActionPerformed(evt);
            }
        });
        jPanelAtt3.add(jButtonChooseAtt3);
        jButtonChooseAtt3.setBounds(11, 126, 205, 21);

        jPanelAcqDocAtt3.add(jPanelAtt3);
        jPanelAtt3.setBounds(5, 115, 240, 505);

        jLabelLogo5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelAcqDocAtt3.add(jLabelLogo5);
        jLabelLogo5.setBounds(10, 5, 220, 115);

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
                .addGap(244, 244, 244)
                .addComponent(jLabelImgFile3, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                .addContainerGap(224, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addComponent(jLabelImgFile3, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
                .addContainerGap())
        );

        jScrollPaneAtt3.setViewportView(jPanel21);

        jPanelAcqDocAtt3.add(jScrollPaneAtt3);
        jScrollPaneAtt3.setBounds(260, 115, 1090, 505);

        jTabbedPaneAcqAtt.addTab("Attachment 4", jPanelAcqDocAtt3);

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
        jMenuFile.add(jSeparator17);

        jMenuEdit.setText("Edit");

        jMenuAcqEdit.setText("Per Diem Acquittal");
        jMenuAcqEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuAcqEditActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuAcqEdit);
        jMenuEdit.add(jSeparator30);

        jMenuMonPlanEdit.setText("Per Diem Monthly Plan");
        jMenuMonPlanEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuMonPlanEditActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuMonPlanEdit);

        jMenuFile.add(jMenuEdit);
        jMenuFile.add(jSeparator25);

        jMenuItemSubmit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSubmit.setText("Submit");
        jMenuItemSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSubmitActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemSubmit);
        jMenuFile.add(jSeparator18);

        jMenuItemClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemClose.setText("Close");
        jMenuItemClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemClose);
        jMenuFile.add(jSeparator19);

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
        jMenuRequest.add(jSeparator15);

        jMenuItemAccMgrRev.setText("Finance Team Approval");
        jMenuItemAccMgrRev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAccMgrRevActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemAccMgrRev);
        jMenuRequest.add(jSeparator20);

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
        jMenuAcquittal.add(jSeparator26);

        jMenuItemAcqAccApp.setText("Finance Team Approval");
        jMenuItemAcqAccApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqAccAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqAccApp);
        jMenuAcquittal.add(jSeparator10);

        jMenuItemAcqHeadApp.setText("Project HOD Approval");
        jMenuItemAcqHeadApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqHeadAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqHeadApp);
        jMenuAcquittal.add(jSeparator11);

        jMenuItemAcqSchGen.setText("Acquittals Schedule Generation");
        jMenuItemAcqSchGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqSchGenActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqSchGen);
        jMenuAcquittal.add(jSeparator27);

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
        jMenuMonthlyPlan.add(jSeparator50);

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
            .addComponent(jTabbedPaneAppSys)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneAppSys, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 702, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1382, 739));
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


    private void jTextAcqRegNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAcqRegNumActionPerformed
        regInitCheck();
    }//GEN-LAST:event_jTextAcqRegNumActionPerformed

    private void jTextAcqRegNumFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextAcqRegNumFocusLost


    }//GEN-LAST:event_jTextAcqRegNumFocusLost

    private void jTextAcqRegNumKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAcqRegNumKeyPressed

//        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
//
//            regInitCheck();
//        }
    }//GEN-LAST:event_jTextAcqRegNumKeyPressed

    private void jTextAcqRegNumKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAcqRegNumKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c)) || (c == KeyEvent.VK_PERIOD) || (c == KeyEvent.VK_BACK_SPACE)
                || (c == KeyEvent.VK_DELETE)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_jTextAcqRegNumKeyTyped

    private void jTextAcqRegNumFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextAcqRegNumFocusGained
//        jTextAcqRegNum.setText("");
//        jLabelAcqEmpNum.setText("");
//        jLabelRegDateAcq.setText("");
//        jLabelRegNum.setText("");
//        jLabelRegYear.setText("");
//        jLabelSerialAcq.setText("");
//        jLabelAcqEmpNam.setText("");
//        jLabelAcqEmpTitle.setText("");
//        jLabelAcqProvince.setText("");
//        jLabelAcqOffice.setText("");
//        jLabelAcqBankName.setText("");
//        jLabelAcqBudCode.setText("");
//        jLabelAcqBudCode.setText("");
//        jLabelAcqActMainPurpose.setText("");
//        jLabelAcqAccNum.setText("");
//
//        DefaultTableModel dmAcq = (DefaultTableModel) jTableActivityAcq.getModel();
//        while (dmAcq.getRowCount() > 0) {
//            dmAcq.removeRow(0);
//        }
//
//        DefaultTableModel dm = (DefaultTableModel) jTableActivityReq.getModel();
//        while (dm.getRowCount() > 0) {
//            dm.removeRow(0);
//        }
//        jLabelBreakFastSubTot.setText("0.00");
//        jLabelLunchSubTot.setText("0.00");
//        jLabelDinnerSubTot.setText("0.00");
//        jLabelIncidentalSubTot.setText("0.00");
//        jLabelMiscSubTot.setText("0.00");
//        jLabelAccUnprovedSubTot.setText("0.00");
//        jLabelAccProvedSubTot.setText("0.00");
//        jLabelAcqBreakFastSubTot.setText("0.00");
//        jLabelAcqLunchSubTot.setText("0.00");
//        jLabelAcqDinnerSubTot.setText("0.00");
//        jLabelAcqIncidentalSubTot.setText("0.00");
//        jLabelAcqMiscSubTot.setText("0.00");
//        jLabelAcqAccUnprovedSubTot.setText("0.00");
//        jLabelAcqAccProvedSubTot.setText("0.00");
//        jLabelAcqBreakFastSubTotBal.setText("0.00");
//        jLabelAcqLunchSubTotBal.setText("0.00");
//        jLabelAcqDinnerSubTotBal.setText("0.00");
//        jLabelAcqIncidentalSubTotBal.setText("0.00");
//        jLabelAcqMiscSubTotBal.setText("0.00");
//        jLabelAcqAccUnprovedSubTotBal.setText("0.00");
//        jLabelAcqAccProvedSubTotBal.setText("0.00");
//        jLabelAcqAppTotReqCost.setText("0.00");
//        //  mainPageTotUpdateAcq();
    }//GEN-LAST:event_jTextAcqRegNumFocusGained

    private void jButtonAuthOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAuthOkActionPerformed


    }//GEN-LAST:event_jButtonAuthOkActionPerformed

    private void jButtonAuthCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAuthCancelActionPerformed
        authUsrNam = "";
        jDialogAuthority.setVisible(false);
        buttonGroupLunch.clearSelection();

        //jComboAuthNam.addItem("");
    }//GEN-LAST:event_jButtonAuthCancelActionPerformed

    private void jButtonAuthAllOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAuthAllOkActionPerformed
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        DefaultTableModel model = (DefaultTableModel) jTableActivityAcq.getModel();
//        java.util.Date jDateActivity = jDateChooserDialogActivityDateFrom.getDate();
//
//        String dateDayActivity = formatter.format(jDateActivity);
//        if (jTextAcqBreakfast.getText().trim().length() == 0) {
//            jTextAcqBreakfast.setText("0.00");
//        }
//        if (jTextAcqLunch.getText().trim().length() == 0) {
//            jTextAcqLunch.setText("0.00");
//        }
//        if (jTextAcqDinner.getText().trim().length() == 0) {
//            jTextAcqDinner.setText("0.00");
//        }
//        if (jTextAcqIncidental.getText().trim().length() == 0) {
//            jTextAcqIncidental.setText("0.00");
//        }
//        if (jTextMiscAmtAcq.getText().trim().length() == 0) {
//            jTextMiscAmtAcq.setText("0.00");
//        }
//        if (jTextAccProvedAcq.getText().trim().length() == 0) {
//            jTextAccProvedAcq.setText("0.00");
//        }
//        if (jTextAccUnprovedAcq.getText().trim().length() == 0) {
//            jTextAccUnprovedAcq.setText("0.00");
//        }
//
////        model.addRow(new Object[]{dateDayActivity, jTextAcqDestination.getText(), jLabelKMDisDB.getText(), jTextPurpose.getText(),
////            jTextAcqBreakfast.getText(), jTextAcqLunch.getText(), jTextAcqDinner.getText(), jTextAcqIncidental.getText(),
////            jTextMiscActivityAcq.getText(), jTextMiscAmtAcq.getText(), jTextAccProvedAcq.getText(), jTextAccUnprovedAcq.getText(),
////            Double.toString((Double.parseDouble(jTextAcqBreakfast.getText()) + Double.parseDouble(jTextAcqLunch.getText())
////            + Double.parseDouble(jTextAcqDinner.getText()) + Double.parseDouble(jTextAcqIncidental.getText())
////            + Double.parseDouble(jTextMiscAmtAcq.getText()) + Double.parseDouble(jTextAccProvedAcq.getText())
////            + Double.parseDouble(jTextAccUnprovedAcq.getText())))});
//        mainPageTotUpdateAcq();
//        /**
//         * **** updating general segment
//         */
//
//        jDateChooserDialogActivityDateFrom.setDate(null);
//        jTextPurpose.setText("");
//        jTextAcqBreakfast.setText("0.00");
//        jTextAcqLunch.setText("0.00");
//        jTextAcqDinner.setText("0.00");
//        jTextAcqIncidental.setText("0.00");
//        jTextMiscAmtAcq.setText("0.00");
//        jTextAccProvedAcq.setText("0.00");
//        jTextAccUnprovedAcq.setText("0.00");
//        jTextMiscActivityAcq.setText("");
//        buttonGroupLunch.clearSelection();
//        jCheckBreakfast.setSelected(false);
//
//        jCheckDinner.setSelected(false);
//        jCheckIncidental.setSelected(false);
//        jRadioAccProved.setSelected(false);
//        jRadioAccUnproved.setSelected(false);
//
//        authUsrNamAll = jComboAuthNamAll.getSelectedItem().toString();
//        jDialogAuthorityAll.setVisible(false);
    }//GEN-LAST:event_jButtonAuthAllOkActionPerformed

    private void jButtonAuthAllCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAuthAllCancelActionPerformed
        authUsrNamAll = "";
        jDialogAuthorityAll.setVisible(false);
    }//GEN-LAST:event_jButtonAuthAllCancelActionPerformed

    private void jPanel8FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel8FocusGained

    }//GEN-LAST:event_jPanel8FocusGained

    private void jMenuPlanApprovalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuPlanApprovalActionPerformed

    }//GEN-LAST:event_jMenuPlanApprovalActionPerformed

    private void jButtonDeleteAtt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteAtt1ActionPerformed
        jLabelImgFile1.setIcon(null);
    }//GEN-LAST:event_jButtonDeleteAtt1ActionPerformed

    private void jButtonChooseAtt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonChooseAtt1ActionPerformed
        imgChooserFile1();
    }//GEN-LAST:event_jButtonChooseAtt1ActionPerformed

    private void jButtonDeleteAtt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteAtt2ActionPerformed
        jLabelImgFile2.setIcon(null);
    }//GEN-LAST:event_jButtonDeleteAtt2ActionPerformed

    private void jButtonChooseAtt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonChooseAtt2ActionPerformed
        imgChooserFile2();
    }//GEN-LAST:event_jButtonChooseAtt2ActionPerformed

    private void jButtonDeleteAtt3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteAtt3ActionPerformed
        jLabelImgFile3.setIcon(null);
    }//GEN-LAST:event_jButtonDeleteAtt3ActionPerformed

    private void jButtonChooseAtt3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonChooseAtt3ActionPerformed
        imgChooserFile3();
    }//GEN-LAST:event_jButtonChooseAtt3ActionPerformed

    private void jComboAuthNamAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboAuthNamAllActionPerformed

    }//GEN-LAST:event_jComboAuthNamAllActionPerformed

    private void jButtonBankOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBankOkActionPerformed
        if ("Select Bank".equals(jComboBankNam)) {
            JOptionPane.showMessageDialog(this, "Please select bank name.");
        } else if (("".equals(jTextPaidAmt.getText()))
                || ("0.00".equals(jTextPaidAmt.getText()))) {
            JOptionPane.showMessageDialog(this, "Please enter amount banked.");
        } else {

            jDialogChgPaid.setVisible(false);
            jToggleButtonNoActivity.setText("Delete Activities (Not Done)");
            jToggleButtonAllActivities.setText("All Activities Done As Per Request");
            jLabelAcqPayBack.setText("Paid Back A/C " + jLabelBankAccNo.getText());
            jLabelAcqAmtPayBack.setText(Double.toString(Double.parseDouble(jTextPaidAmt.getText())));

        }

    }//GEN-LAST:event_jButtonBankOkActionPerformed

    private void jButtonBankCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBankCancelActionPerformed
        jDialogChgPaid.setVisible(false);
        DefaultTableModel dmAcq = (DefaultTableModel) jTableActivityAcq.getModel();
        while (dmAcq.getRowCount() > 0) {
            dmAcq.removeRow(0);
        }
        jToggleButtonAllActivities.setText("All Activities Done As Per Request");
        jToggleButtonNoActivity.setText("All Activities Not Done");

    }//GEN-LAST:event_jButtonBankCancelActionPerformed

    private void jPanel10FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel10FocusGained

    }//GEN-LAST:event_jPanel10FocusGained

    private void jTextPaidAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextPaidAmtActionPerformed

    }//GEN-LAST:event_jTextPaidAmtActionPerformed

    private void jComboBankNamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBankNamActionPerformed
        findBankAcc();
    }//GEN-LAST:event_jComboBankNamActionPerformed

    private void jTextPaidAmtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextPaidAmtFocusGained
        jTextPaidAmt.setText("");
    }//GEN-LAST:event_jTextPaidAmtFocusGained

    private void jToggleButtonNoActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonNoActivityActionPerformed

        if ((jToggleButtonNoActivity.isSelected()) && ("All Activities Not Done".equals(jToggleButtonNoActivity.getText()))) {
            DefaultTableModel dmAcq = (DefaultTableModel) jTableActivityAcq.getModel();
            while (dmAcq.getRowCount() > 0) {
                dmAcq.removeRow(0);
            }
            fillDataNoAct();
            mainPageTotUpdateAcq();

            if ((("Total (Change)").equals(jLabelAppTotReq.getText())) && (jRadioButtonPayRecYes.isSelected())) {

                jLabelBankAccNo.setText("");
                jTextPaidAmt.setText("0.00");
                jDialogChgPaid.setVisible(true);

            }
        } else if ((jToggleButtonNoActivity.isSelected()) && ("Delete Activities (Not Done)".equals(jToggleButtonNoActivity.getText()))) {
            DefaultTableModel dmAcq = (DefaultTableModel) jTableActivityAcq.getModel();
            while (dmAcq.getRowCount() > 0) {
                dmAcq.removeRow(0);
            }
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
            jLabelAcqPayBack.setText("");
            jLabelAcqAmtPayBack.setText("");
            jLabelBankAccNo.setText("");
            jTextPaidAmt.setText("0.00");
            fetchDataForAcq();
            //jTableActivityAcq.setEnabled(false);
            mainPageTotUpdateAcq();
            jToggleButtonAllActivities.setText("Delete All Requested Activities");
            jToggleButtonNoActivity.setText("All Activities Not Done");
        } else if ("Delete All Requested Activities".equals(jToggleButtonAllActivities.getText())) {
            DefaultTableModel dmAcq = (DefaultTableModel) jTableActivityAcq.getModel();
            while (dmAcq.getRowCount() > 0) {
                dmAcq.removeRow(0);
            }

            if (minWkNum == 1) {
                jLabelAcqAppWk1TotReqCost.setText("0.00");
                jLabelAcqAppTotReqCost.setText(String.format("%.2f", allTotalAcq));
            } else if (minWkNum == 2) {
                jLabelAcqAppWk2TotReqCost.setText("0.00");
                double cleredAmtBal = allActTot - Double.parseDouble(jLabelAcqAppWk1TotReqCost.getText());
                jLabelAcqAppTotReqCost.setText(String.format("%.2f", cleredAmtBal));
            } else if (minWkNum == 3) {
                jLabelAcqAppWk3TotReqCost.setText("0.00");
                double cleredAmtBal = allActTot - Double.parseDouble(jLabelAcqAppWk1TotReqCost.getText())
                        - Double.parseDouble(jLabelAcqAppWk2TotReqCost.getText());
                jLabelAcqAppTotReqCost.setText(String.format("%.2f", cleredAmtBal));
            } else if (minWkNum == 4) {
                jLabelAcqAppWk4TotReqCost.setText("0.00");
                double cleredAmtBal = allActTot - Double.parseDouble(jLabelAcqAppWk1TotReqCost.getText())
                        - Double.parseDouble(jLabelAcqAppWk2TotReqCost.getText())
                        - Double.parseDouble(jLabelAcqAppWk3TotReqCost.getText());
                jLabelAcqAppTotReqCost.setText(String.format("%.2f", cleredAmtBal));
            } else if (minWkNum == 5) {
                jLabelAcqAppWk5TotReqCost.setText("0.00");
                double cleredAmtBal = allActTot - Double.parseDouble(jLabelAcqAppWk1TotReqCost.getText())
                        - Double.parseDouble(jLabelAcqAppWk2TotReqCost.getText())
                        - Double.parseDouble(jLabelAcqAppWk3TotReqCost.getText())
                        - Double.parseDouble(jLabelAcqAppWk4TotReqCost.getText());
                jLabelAcqAppTotReqCost.setText(String.format("%.2f", cleredAmtBal));
            }
            jLabelAppTotReq.setText("Total (Cash On hand)");
            jToggleButtonAllActivities.setText("All Activities Done As Per Request");
            jToggleButtonNoActivity.setText("All Activities Not Done");
        }

    }//GEN-LAST:event_jToggleButtonAllActivitiesActionPerformed

    private void jButtonPayRecContActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPayRecContActionPerformed
        java.util.Date jDateRec = jDateChooserDateRec.getDate();
        java.util.Date date = new Date();
        if (jRadioButtonPayRecNo.isSelected()) {
            jDialogPayRec.setVisible(false);
        } else if ((!(jRadioButtonPayRecYes.isSelected())) && (!(jRadioButtonPayRecNo.isSelected()))) {
            JOptionPane.showMessageDialog(this, "Please inidcate with (Y/N) if you recieved your perdiem.");
            jTextAmtRec.requestFocusInWindow();
            jTextAmtRec.setFocusable(true);
        } else if ((jDateChooserDateRec.getDate() == null) && (jRadioButtonPayRecYes.isSelected())) {
            JOptionPane.showMessageDialog(this, "Please record date perdiem was recieved.");
            jDateChooserDateRec.requestFocusInWindow();
            jDateChooserDateRec.setFocusable(true);
        } else if (date.before(jDateRec)) {
            JOptionPane.showMessageDialog(this, "Date recieved cannot be greater than today.");
            jDateChooserDateRec.requestFocusInWindow();
            jDateChooserDateRec.setFocusable(true);
        } else if ((jRadioButtonPayRecYes.isSelected()) && (("0.00".equals(jTextAmtRec.getText()))
                || ("0".equals(jTextAmtRec.getText())) || ("".equals(jTextAmtRec.getText())))) {
            JOptionPane.showMessageDialog(this, "Please record amout recieved");
            jTextAmtRec.requestFocusInWindow();
            jTextAmtRec.setFocusable(true);
        } else {

            jDialogPayRec.setVisible(false);
        }


    }//GEN-LAST:event_jButtonPayRecContActionPerformed

    private void jTextAmtRecFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextAmtRecFocusGained

    }//GEN-LAST:event_jTextAmtRecFocusGained

    private void jTextAmtRecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAmtRecActionPerformed

    }//GEN-LAST:event_jTextAmtRecActionPerformed

    private void jPanel11FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel11FocusGained

    }//GEN-LAST:event_jPanel11FocusGained

    private void jRadioButtonPayRecNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonPayRecNoActionPerformed
        jLabelDateRec.setVisible(false);
        jDateChooserDateRec.setVisible(false);
        jDateChooserDateRec.setDate(null);
        jLabelAmtRec.setVisible(false);
        jTextAmtRec.setVisible(false);
        jTextAmtRec.setText("");
        jButtonPayRecCont.setBounds(200, 90, 130, 25);
    }//GEN-LAST:event_jRadioButtonPayRecNoActionPerformed

    private void jRadioButtonPayRecYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonPayRecYesActionPerformed
        jLabelDateRec.setVisible(true);
        jDateChooserDateRec.setVisible(true);
        jLabelAmtRec.setVisible(true);
        jTextAmtRec.setVisible(true);
        jTextAmtRec.setText("");
        jButtonPayRecCont.setBounds(200, 170, 130, 25);

    }//GEN-LAST:event_jRadioButtonPayRecYesActionPerformed

    private void jPanelReportDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelReportDetailsMouseClicked

    }//GEN-LAST:event_jPanelReportDetailsMouseClicked

    private void jTextVehRegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextVehRegActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextVehRegActionPerformed

    private void jButtonGetDetails1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGetDetails1ActionPerformed
        try {
            str = jTextVehReg.getText();
            str = str.replaceAll("\\s", "");
            findVehicle();

            if (jTextVehReg.getText().trim().length() == 0 || jDateTripFrom.getDate() == null || jDateTripTo.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Please complete all feilds before proceeding");
                jTextVehReg.requestFocusInWindow();
                jTextVehReg.setFocusable(true);
            } else if (((dfDate.format(jDateTripTo.getDate())).compareTo(dfDate.format(jDateTripFrom.getDate())) < 0)) {
                JOptionPane.showMessageDialog(this, "End Date cannot be lower that start date.");
                jTextVehReg.requestFocusInWindow();
                jTextVehReg.setFocusable(true);
            } else if (vehCount == 0) {
                JOptionPane.showMessageDialog(this, "Vehicle registration number does not exist.");
                jTextVehReg.requestFocusInWindow();
                jTextVehReg.setFocusable(true);
            } else if (jLabelAcqActMainPurpose.getText().trim().length() == 0) {
                JOptionPane.showMessageDialog(this, "Please retreive per diem details before getting vehicle mileage");
                jTextVehReg.requestFocusInWindow();
                jTextVehReg.setFocusable(true);
            } else if (!(modelTrip.getRowCount() == 0)) {
                int selectedOption = JOptionPane.showConfirmDialog(null,
                        "Do you want to add trips to the current list of trips?",
                        "Choose",
                        JOptionPane.YES_NO_OPTION);
                if (selectedOption == JOptionPane.YES_OPTION) {
                    addTrips();
                } else {
                    clearTable();
                    addTrips();
                }
            } else {
                addTrips();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }//GEN-LAST:event_jButtonGetDetails1ActionPerformed

    private void jButtonDeleteDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteDetailsActionPerformed
        deleteFileAttTrip();


    }//GEN-LAST:event_jButtonDeleteDetailsActionPerformed

    private void jMenuFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuFileActionPerformed

    }//GEN-LAST:event_jMenuFileActionPerformed

    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItemExitActionPerformed

    private void jMenuItemCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCloseActionPerformed
        new JFrameFinSysLogin().setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemCloseActionPerformed

    private void jMenuItemSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSubmitActionPerformed
        saveAcqRegisterCheck();
    }//GEN-LAST:event_jMenuItemSubmitActionPerformed

    private void jMenuItemPlanPerDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPlanPerDiemActionPerformed
        new JFrameMnthPlanPerDiemCreate(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemPlanPerDiemActionPerformed

    private void jMenuItemPerDiemAcquittalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPerDiemAcquittalActionPerformed
        new JFrameAppAcquittal(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemPerDiemAcquittalActionPerformed

    private void jMenuAcqEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuAcqEditActionPerformed
        new JFrameAppEditAcquittal(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuAcqEditActionPerformed

    private void jMenuMonPlanEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuMonPlanEditActionPerformed
        new JFrameMnthPlanPerDiemEdit(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuMonPlanEditActionPerformed

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

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        try {

            saveAcqRegisterCheck();

        } catch (Exception e1) {
            System.out.println(e1);
        }
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jComboAccountCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboAccountCodeMouseClicked

    }//GEN-LAST:event_jComboAccountCodeMouseClicked

    private void jComboAccountCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboAccountCodeActionPerformed

    }//GEN-LAST:event_jComboAccountCodeActionPerformed

    private void jComboDonorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboDonorMouseClicked

        //        String prjName = jComboProjectName.getSelectedItem().toString();
        //        String prjParts[] = prjName.split(" ", 2);
        //        String prjCode = prjParts[0];
        //        findTask(prjCode);
    }//GEN-LAST:event_jComboDonorMouseClicked

    private void jComboDonorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboDonorActionPerformed
        try {
            if (("D036 CDC-Zim-TTECH".equals(jComboDonor.getSelectedItem().toString()))
                    || ("D032 ZHI".equals(jComboDonor.getSelectedItem().toString()))
                    || ("D022 CDC".equals(jComboDonor.getSelectedItem().toString()))) {
                jLabelPrjCodeProgramming.setVisible(true);
                jComboProjectCodeProgramming.setVisible(true);
            } else {
                jLabelPrjCodeProgramming.setVisible(false);
                jComboProjectCodeProgramming.setVisible(false);
            }
            String taskName = jComboDonor.getSelectedItem().toString();
            String taskParts[] = taskName.split(" ", 2);
            donorCode = taskParts[0];

            findPrjCode(donorCode);
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_jComboDonorActionPerformed

    private void jComboProjectCodeGLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboProjectCodeGLMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboProjectCodeGLMouseClicked

    private void jComboProjectCodeGLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboProjectCodeGLActionPerformed

    }//GEN-LAST:event_jComboProjectCodeGLActionPerformed

    private void jComboBudMainCodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBudMainCodeMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBudMainCodeMouseClicked

    private void jComboBudMainCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBudMainCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBudMainCodeActionPerformed

    private void jTextFieldDialogWkSiteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDialogWkSiteMouseClicked
        facilityPOP();
    }//GEN-LAST:event_jTextFieldDialogWkSiteMouseClicked

    private void jTextFieldDialogWkSiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDialogWkSiteActionPerformed

    }//GEN-LAST:event_jTextFieldDialogWkSiteActionPerformed

    private void jTextFieldDialogWkSiteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDialogWkSiteKeyTyped
        facilityPOP();
    }//GEN-LAST:event_jTextFieldDialogWkSiteKeyTyped

    private void jComboProjectCodeProgrammingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboProjectCodeProgrammingMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboProjectCodeProgrammingMouseClicked

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

    private void jButtonOkFacilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkFacilityActionPerformed
        jTextFieldDialogWkSite.setText(jComboFacility.getSelectedItem().toString());
        jDialogFacility.setVisible(false);
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
        //            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
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
        //            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
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

    }//GEN-LAST:event_jComboFacilityActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(JFrameAppAcquittal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameAppAcquittal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameAppAcquittal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameAppAcquittal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new JFrameAppAcquittal().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupAcc;
    private javax.swing.ButtonGroup buttonGroupAccAcq;
    private javax.swing.ButtonGroup buttonGroupChange;
    private javax.swing.ButtonGroup buttonGroupLunch;
    private javax.swing.ButtonGroup buttonGroupPayRec;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonAuthAllCancel;
    private javax.swing.JButton jButtonAuthAllOk;
    private javax.swing.JButton jButtonAuthCancel;
    private javax.swing.JButton jButtonAuthOk;
    private javax.swing.JButton jButtonBankCancel;
    private javax.swing.JButton jButtonBankOk;
    private javax.swing.JButton jButtonCancelFacility;
    private javax.swing.JButton jButtonChooseAtt1;
    private javax.swing.JButton jButtonChooseAtt2;
    private javax.swing.JButton jButtonChooseAtt3;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonDeleteAtt1;
    private javax.swing.JButton jButtonDeleteAtt2;
    private javax.swing.JButton jButtonDeleteAtt3;
    private javax.swing.JButton jButtonDeleteDetails;
    private javax.swing.JButton jButtonGetDetails1;
    private javax.swing.JButton jButtonOkFacility;
    private javax.swing.JButton jButtonPayRecCont;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonSelectFile;
    private javax.swing.JCheckBox jCheckBoxDialogWk1AccProved;
    private javax.swing.JCheckBox jCheckBoxDialogWk1AccUnProved;
    private javax.swing.JCheckBox jCheckBoxDialogWk1BrkFast;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Dinner;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Inc;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Lunch;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Misc;
    private javax.swing.JCheckBox jCheckBoxNoAcc;
    private javax.swing.JComboBox<String> jComboAccountCode;
    private javax.swing.JComboBox<String> jComboAuthNam;
    private javax.swing.JComboBox<String> jComboAuthNamAll;
    private javax.swing.JComboBox<String> jComboBankNam;
    private javax.swing.JComboBox<String> jComboBudMainCode;
    private javax.swing.JComboBox<String> jComboDistrictFacility;
    private javax.swing.JComboBox<String> jComboDonor;
    private javax.swing.JComboBox<String> jComboFacility;
    private javax.swing.JComboBox<String> jComboProjectCodeGL;
    private javax.swing.JComboBox<String> jComboProjectCodeProgramming;
    private javax.swing.JComboBox<String> jComboProvinceFacility;
    private com.toedter.calendar.JDateChooser jDateChooserDateRec;
    private com.toedter.calendar.JDateChooser jDateChooserDialogActivityDateFrom;
    private com.toedter.calendar.JDateChooser jDateChooserDialogActivityDateTo;
    private com.toedter.calendar.JDateChooser jDateTripFrom;
    private com.toedter.calendar.JDateChooser jDateTripTo;
    private javax.swing.JDialog jDialogAuthority;
    private javax.swing.JDialog jDialogAuthorityAll;
    private javax.swing.JDialog jDialogChgPaid;
    private javax.swing.JDialog jDialogFacility;
    private javax.swing.JDialog jDialogPayRec;
    private javax.swing.JDialog jDialogWaiting;
    private javax.swing.JDialog jDialogWaitingEmail;
    private javax.swing.JLabel jLabeAccountCode;
    private javax.swing.JLabel jLabeAccountCode1;
    private javax.swing.JLabel jLabeAccountCode2;
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
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
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
    private javax.swing.JLabel jLabelAcqAppTotReqCost;
    private javax.swing.JLabel jLabelAcqAppWk1TotReqCost;
    private javax.swing.JLabel jLabelAcqAppWk2TotReqCost;
    private javax.swing.JLabel jLabelAcqAppWk3TotReqCost;
    private javax.swing.JLabel jLabelAcqAppWk4TotReqCost;
    private javax.swing.JLabel jLabelAcqAppWk5TotReqCost;
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
    private javax.swing.JLabel jLabelAcqWk;
    private javax.swing.JLabel jLabelAcqWk1;
    private javax.swing.JLabel jLabelAcqWk2;
    private javax.swing.JLabel jLabelAcqWk3;
    private javax.swing.JLabel jLabelAcqWk4;
    private javax.swing.JLabel jLabelActMainPurpose;
    private javax.swing.JLabel jLabelAllAcq;
    private javax.swing.JLabel jLabelAllBal;
    private javax.swing.JLabel jLabelAllReq;
    private javax.swing.JLabel jLabelAmtRec;
    private javax.swing.JLabel jLabelAppTotReq;
    private javax.swing.JLabel jLabelAppWk1TotReq;
    private javax.swing.JLabel jLabelAppWk2TotReq;
    private javax.swing.JLabel jLabelAppWk3TotReq;
    private javax.swing.JLabel jLabelAppWk4TotReq;
    private javax.swing.JLabel jLabelAppWk5TotReq;
    private javax.swing.JLabel jLabelAttach;
    private javax.swing.JLabel jLabelAutName;
    private javax.swing.JLabel jLabelAutNameAll;
    private javax.swing.JLabel jLabelBank;
    private javax.swing.JLabel jLabelBankAcc;
    private javax.swing.JLabel jLabelBankAccNo;
    private javax.swing.JLabel jLabelBankHeader;
    private javax.swing.JLabel jLabelBankMsg;
    private javax.swing.JLabel jLabelBankName;
    private javax.swing.JLabel jLabelBreakFastSub;
    private javax.swing.JLabel jLabelBreakFastSubTot;
    private javax.swing.JLabel jLabelBudMainCode;
    private javax.swing.JLabel jLabelDateRange;
    private javax.swing.JLabel jLabelDateRec;
    private javax.swing.JLabel jLabelDialogWk1Site;
    private javax.swing.JLabel jLabelDinnerSub;
    private javax.swing.JLabel jLabelDinnerSubTot;
    private javax.swing.JLabel jLabelDistrictFacility;
    private javax.swing.JLabel jLabelDonor;
    private javax.swing.JLabel jLabelEmp;
    private javax.swing.JLabel jLabelEmpNum;
    private javax.swing.JLabel jLabelFacility;
    private javax.swing.JLabel jLabelFrom;
    private javax.swing.JLabel jLabelGenDate;
    private javax.swing.JLabel jLabelGenLogNam;
    private javax.swing.JLabel jLabelGenLogNam1;
    private javax.swing.JLabel jLabelGenTime;
    private javax.swing.JLabel jLabelHeader;
    private javax.swing.JLabel jLabelHeader1;
    private javax.swing.JLabel jLabelHeaderAll;
    private javax.swing.JLabel jLabelHeaderGen;
    private javax.swing.JLabel jLabelHeaderLine;
    private javax.swing.JLabel jLabelHeaderLine1;
    private javax.swing.JLabel jLabelHeaderLine2;
    private javax.swing.JLabel jLabelHeaderLine3;
    private javax.swing.JLabel jLabelHeaderLine4;
    private javax.swing.JLabel jLabelHeaderLine5;
    private javax.swing.JLabel jLabelImgFile1;
    private javax.swing.JLabel jLabelImgFile2;
    private javax.swing.JLabel jLabelImgFile3;
    private javax.swing.JLabel jLabelIncidentalSub;
    private javax.swing.JLabel jLabelIncidentalSubTot;
    private javax.swing.JLabel jLabelLineDate;
    private javax.swing.JLabel jLabelLineDate1;
    private javax.swing.JLabel jLabelLineDate2;
    private javax.swing.JLabel jLabelLineDate3;
    private javax.swing.JLabel jLabelLineDate4;
    private javax.swing.JLabel jLabelLineDate5;
    private javax.swing.JLabel jLabelLineLogNam;
    private javax.swing.JLabel jLabelLineLogNam1;
    private javax.swing.JLabel jLabelLineLogNam4;
    private javax.swing.JLabel jLabelLineLogNam5;
    private javax.swing.JLabel jLabelLineLogNam7;
    private javax.swing.JLabel jLabelLineLogNam8;
    private javax.swing.JLabel jLabelLineTime;
    private javax.swing.JLabel jLabelLineTime1;
    private javax.swing.JLabel jLabelLineTime2;
    private javax.swing.JLabel jLabelLineTime3;
    private javax.swing.JLabel jLabelLineTime4;
    private javax.swing.JLabel jLabelLineTime5;
    private javax.swing.JLabel jLabelLogo;
    private javax.swing.JLabel jLabelLogo1;
    private javax.swing.JLabel jLabelLogo2;
    private javax.swing.JLabel jLabelLogo3;
    private javax.swing.JLabel jLabelLogo4;
    private javax.swing.JLabel jLabelLogo5;
    private javax.swing.JLabel jLabelLogo6;
    private javax.swing.JLabel jLabelLunchSub;
    private javax.swing.JLabel jLabelLunchSubTot;
    private javax.swing.JLabel jLabelMiscAcq;
    private javax.swing.JLabel jLabelMiscReq;
    private javax.swing.JLabel jLabelMiscSubTot;
    private javax.swing.JLabel jLabelMscBal;
    private javax.swing.JLabel jLabelMscSub;
    private javax.swing.JLabel jLabelNamTravel;
    private javax.swing.JLabel jLabelNotMsg;
    private javax.swing.JLabel jLabelNotMsgAll;
    private javax.swing.JLabel jLabelNum;
    private javax.swing.JLabel jLabelNum1;
    private javax.swing.JLabel jLabelNum2;
    private javax.swing.JLabel jLabelOffice;
    private javax.swing.JLabel jLabelPaidAmt;
    private javax.swing.JLabel jLabelPayRecHeader;
    private javax.swing.JLabel jLabelPayRecQues;
    private javax.swing.JLabel jLabelPrjCodeGL;
    private javax.swing.JLabel jLabelPrjCodeProgramming;
    private javax.swing.JLabel jLabelProvince;
    private javax.swing.JLabel jLabelProvinceFacility;
    private javax.swing.JLabel jLabelRegDateAcq;
    private javax.swing.JLabel jLabelRegNum;
    private javax.swing.JLabel jLabelRegYear;
    private javax.swing.JLabel jLabelRegYear1;
    private javax.swing.JLabel jLabelRemain;
    private javax.swing.JLabel jLabelSepDetails;
    private javax.swing.JLabel jLabelSerial;
    private javax.swing.JLabel jLabelSerialAcq;
    private javax.swing.JLabel jLabelSupportAttachDoc;
    private javax.swing.JLabel jLabelTo;
    private javax.swing.JLabel jLabelTripDetails;
    private javax.swing.JLabel jLabelVehReg;
    private javax.swing.JLabel jLabelWk1DialogActivityDesc;
    private javax.swing.JLabel jLabelWk1Misc;
    private javax.swing.JLabel jLabelWk1Misc1;
    private javax.swing.JLabel jLabelWk1MiscAmt;
    private javax.swing.JLabel jLabellogged;
    private javax.swing.JLabel jLabellogged1;
    private javax.swing.JLabel jLabellogged2;
    private javax.swing.JLabel jLabellogged3;
    private javax.swing.JLabel jLabellogged4;
    private javax.swing.JLabel jLabellogged5;
    private javax.swing.JMenuItem jMenuAcqEdit;
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
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelAcqDocAtt1;
    private javax.swing.JPanel jPanelAcqDocAtt2;
    private javax.swing.JPanel jPanelAcqDocAtt3;
    private javax.swing.JPanel jPanelAcqELog;
    private javax.swing.JPanel jPanelAcquittal;
    private javax.swing.JPanel jPanelActivityInfo;
    private javax.swing.JPanel jPanelAtt1;
    private javax.swing.JPanel jPanelAtt2;
    private javax.swing.JPanel jPanelAtt3;
    private javax.swing.JPanel jPanelAtt4;
    private javax.swing.JPanel jPanelAttach;
    private javax.swing.JPanel jPanelGen;
    private javax.swing.JPanel jPanelReport;
    private javax.swing.JPanel jPanelReportDetails;
    private javax.swing.JPanel jPanelRequest;
    private javax.swing.JRadioButton jRadioButtonPayRecNo;
    private javax.swing.JRadioButton jRadioButtonPayRecYes;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPaneAtt1;
    private javax.swing.JScrollPane jScrollPaneAtt2;
    private javax.swing.JScrollPane jScrollPaneAtt3;
    private javax.swing.JScrollPane jScrollPaneWk8;
    private javax.swing.JScrollPane jScrollPaneWk9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator11;
    private javax.swing.JPopupMenu.Separator jSeparator13;
    private javax.swing.JPopupMenu.Separator jSeparator15;
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
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator30;
    private javax.swing.JPopupMenu.Separator jSeparator34;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator50;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator62;
    private javax.swing.JPopupMenu.Separator jSeparator63;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JTabbedPane jTabbedPaneAcqAtt;
    public javax.swing.JTabbedPane jTabbedPaneAppSys;
    private javax.swing.JTable jTableAcquittalDocAtt;
    private javax.swing.JTable jTableActivityAcq;
    private javax.swing.JTable jTableActivityReq;
    private javax.swing.JTable jTableTripDetails;
    private javax.swing.JTextField jTextAcqRegNum;
    private javax.swing.JTextField jTextAmtRec;
    private javax.swing.JTextArea jTextAreaNamTravel;
    private javax.swing.JTextField jTextAttAcqDocName;
    private javax.swing.JTextField jTextAttDocDesc;
    private javax.swing.JTextField jTextAttDocFilePath;
    private javax.swing.JTextField jTextFieldDialogWkSite;
    private javax.swing.JTextField jTextFieldWk1DialogActivityDesc;
    private javax.swing.JTextField jTextFieldWk1Misc;
    private javax.swing.JTextField jTextFieldWk1MiscAmt;
    private javax.swing.JTextField jTextPaidAmt;
    private javax.swing.JTextField jTextVehReg;
    private javax.swing.JToggleButton jToggleButtonAllActivities;
    private javax.swing.JToggleButton jToggleButtonNoActivity;
    // End of variables declaration//GEN-END:variables
}
