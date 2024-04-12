/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClaimAppendix2;

import ClaimPlanPerDiem.JFrameMnthPlanPerDiemEdit;
import ClaimPlanPerDiem.JFrameMnthPlanPerDiemCreate;
import ClaimAppendix1.JFrameAccMgrAppList;
import ClaimAppendix1.JFrameReqHeadAppList;
import ClaimAppendix1.JFrameReqHQPayScheduleApp;
import ClaimAppendix1.JFrameSupAppList;
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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.timeHost;
import utils.connCred;
import utils.connSaveFile;
import utils.savePDFToDB;
import utils.StockVehicleMgt;
import utils.connCred;

/**
 *
 * @author cgoredema
 */
public class JFrameAppEditAcquittal extends javax.swing.JFrame {

    timeHost tH = new timeHost();
    connCred c = new connCred();
    PreparedStatement pst = null;
    PreparedStatement pst1 = null;
    PreparedStatement pst2 = null;
    int itmNum = 1;
    int month, finyear, count;
    int numsearchRef = 0;
    int clickCount = 0;
    Date curYear = new Date();
    int checkRefCount = 0;
    int imgAttCount = 0;
    int imgAttCount2 = 0;
    int imgAttCount3 = 0;
    int imgAttCount4 = 0;
    int imgAttCount5 = 0;
    int oldImgAttVer = 0;
    int pdCount = 0;
    int vehCount = 0;
    boolean ignoreInput = false;
    int charMax = 200;
    int existCountWkNum = 0;
    SimpleDateFormat df = new SimpleDateFormat("yyyy");
    SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
    DefaultTableModel model, modelAcq, modelTrip;
    String sendTo, createUsrNam, supUsrMail, breakfastAll, lunchAll, lunchNPAll, lunchPAll, dinnerAll,
            incidentalAll, unProvedAll, date1, date2, usrnam, searchRef, authUsrNam, authUsrNamAll,
            authNam1, authNam2, regDocVersion, oldRegDocVersion, usrGrp, imgAttVer, actVersion, ackStatus, wkNum,
            paraone, paratwo, parathree, wkNumRep, curSta, docVerClearOld, docVerClearNew, lowDate, empOff;
    String ipAdd = "ophidutilapp.southafricanorth.cloudapp.azure.com:16432";
    String usrName = "appfin";
    String usrPass = "542ytDYvynv$TVYb";
    String mailUsrNam = "finance@ophid.co.zw";
    String mailUsrPass = "MgqM5utyUr43x#";
    String actRef = "";
    double balAmt = 0;
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
    String filechange = "N";
    String startSym1 = "N";
    String startSym2 = "N";
    String startSym3 = "N";
    String startSym4 = "N";
    String startSym5 = "N";
    String fileDel1 = "N";
    String fileDel2 = "N";
    String fileDel3 = "N";
    String fileDel4 = "N";
    String fileDel5 = "N";
    String fileTripDel = "Y";
    String fileTripChange = "Y";
    String createRef = "";
    String createAct = "";
    String str = "";

    /**
     * Creates new form JFrameTabApp1
     */
    public JFrameAppEditAcquittal() {
        initComponents();
        findProvince();
        jTabbedPaneAcqAtt.setTitleAt(0, "");
        jTabbedPaneAcqAtt.setTitleAt(1, "");
        jTabbedPaneAcqAtt.setTitleAt(2, "");
        jTabbedPaneAcqAtt.setTitleAt(3, "");
        jTabbedPaneAcqAtt.setTitleAt(4, "");
        jTabbedPaneAcqAtt.setTitleAt(5, "");
        jTabbedPaneAcqAtt.setEnabledAt(0, false);
        jTabbedPaneAcqAtt.setEnabledAt(1, false);
        jTabbedPaneAcqAtt.setEnabledAt(2, false);
        jTabbedPaneAcqAtt.setEnabledAt(3, false);
        jTabbedPaneAcqAtt.setEnabledAt(4, false);
        jTabbedPaneAcqAtt.setEnabledAt(5, false);

    }

    public JFrameAppEditAcquittal(String usrLogNum) {
        initComponents();
        // showDate();
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
        showTime();
        computerName();
        allowanceRate();
        jRadioNormal.setVisible(false);
        jRadioSpecial.setVisible(false);
        model = (DefaultTableModel) jTableActivityReq.getModel();
        modelAcq = (DefaultTableModel) jTableActivityAcq.getModel();
        modelTrip = (DefaultTableModel) jTableTripDetails.getModel();
        jTableTripDetails.getColumnModel().getColumn(1).setMinWidth(0);
        jTableTripDetails.getColumnModel().getColumn(1).setMaxWidth(0);
        jTextAcqBreakfast.setText("");
        jTextAcqLunch.setText("");
        jTextAcqDinner.setText("");
        jTextAcqIncidental.setText("");
        jTextMiscAmtAcq.setText("");
        jTextAccProvedAcq.setText("");
        jTextAccUnprovedAcq.setText("");
        jTextAccProvedAcq.setVisible(false);
        jTextAccUnprovedAcq.setVisible(false);
        jLabelEmp.setText(usrLogNum);
        jLabelSpecial.setText("");
        jLabelSpecial1.setText("");
        jLabelSpecial2.setText("");
        jLabelSpecial3.setText("");
        jLabelRemain.setVisible(false);
        jTextAcqBreakfast.setEnabled(false);
        jTextAcqLunch.setEnabled(false);
        jTextAcqDinner.setEnabled(false);
        jTextAcqIncidental.setEnabled(false);
        jTextAccUnprovedAcq.setEnabled(false);
        jLabelEmp.setVisible(false);

        findBankName();
        findUser();
        findUserGrp();
        findProvince();
        jTabbedPaneAcqAtt.setTitleAt(0, "");
        jTabbedPaneAcqAtt.setTitleAt(1, "");
        jTabbedPaneAcqAtt.setTitleAt(2, "");
        jTabbedPaneAcqAtt.setTitleAt(3, "");
        jTabbedPaneAcqAtt.setTitleAt(4, "");
        jTabbedPaneAcqAtt.setTitleAt(5, "");
        jTabbedPaneAcqAtt.setEnabledAt(0, false);
        jTabbedPaneAcqAtt.setEnabledAt(1, false);
        jTabbedPaneAcqAtt.setEnabledAt(2, false);
        jTabbedPaneAcqAtt.setEnabledAt(3, false);
        jTabbedPaneAcqAtt.setEnabledAt(4, false);
        jTabbedPaneAcqAtt.setEnabledAt(5, false);
        jRadioLunch3.setEnabled(false);

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
            }
        }) {

        }.start();

    }

//    void showDate() {
//        Date d = new Date();
//        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
//        jLabelGenDate.setText(s.format(d));
//        jLabelLineDate.setText(s.format(d));
//
//    }
    void supName() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT *   FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] "
                    + "where EMP_NUM = '" + jLabelEmp.getText() + "'");
            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                sendTo = rs.getString(5);
                createUsrNam = rs.getString(2);

            }

        } catch (Exception e) {

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

                jMenuItemSupApp.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuItemAcqSupApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuMonPlanEdit.setEnabled(false);
                jMenuItemPlanView.setEnabled(false);
                jMenuItemPlanSupApproval.setEnabled(false);
                jMenuItemPlanHODApproval.setEnabled(false);

            }

            if ("usrFinAcq".equals(usrGrp)) {

                jMenuItemSupApp.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuItemAcqSupApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuMonPlanEdit.setEnabled(false);
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

    void ackStatus() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT REC_ACK FROM [ClaimsAppSysZvandiri].[dbo].[ClaimPayRecTab] "
                    + "where concat(serial,ref_num)='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "'");
            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                ackStatus = rs.getString(1);

            }

        } catch (Exception e) {

        }
    }

    void refreshTab() {

        jLabelAcqEmpNum.setText("");
        jLabelAcqDateAcq.setText("");
        jLabelAcqRefNum.setText("");
        jLabelAcqYear.setText("");
        jLabelAcqSerial.setText("");
        jLabelAcqEmpNam.setText("");
        jLabelAcqEmpTitle.setText("");
        jLabelAcqProvince.setText("");
        jLabelAcqOffice.setText("");
        jLabelAcqBankName.setText("");
        jLabelAcqBudCode.setText("");
        jLabelAcqBudCode.setText("");
        jLabelAcqActMainPurpose.setText("");
        jLabelAcqAccNum.setText("");

        jLabelSpecial.setVisible(false);
        jLabelSpecial1.setVisible(false);
        jLabelSpecial2.setVisible(false);
        jLabelSpecial3.setVisible(false);

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
    }

    void allowanceRate() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAllowanceTab] "
                    + "where RateOrg = 'OPHID' and RateStatus ='A'");

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

    void computerName() {

        try {
            java.net.InetAddress addr = InetAddress.getLocalHost();
            hostName = addr.getHostName();
            //  JOptionPane.showMessageDialog(this, "CurBal" + curBalDays + "Days" + Double.parseDouble(jLabelDays1.getText())

        } catch (Exception ex) {
            System.out.println("Hostname can not be resolved");
        }
    }

    void getLowestDate() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
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
                jLabelLineLogNam4.setText(r.getString(2));
                jLabelLineLogNam5.setText(r.getString(2));
                jLabelLineLogNam7.setText(r.getString(2));
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

    void findAuthorisation() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT count(*),AUTH_NAM,AUTH_ALL_NAM FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] where (AUTH_NAM != ' '  "
                    + "or AUTH_ALL_NAM != ' ') and  concat(SERIAL,REF_NUM) ='"
                    + jLabelSerial.getText() + jTextAcqRegNum.getText() + "' "
                    + "group by AUTH_NAM,AUTH_ALL_NAM");

            while (r.next()) {

                checkRefCount = r.getInt(1);
                authNam1 = r.getString(2);
                authNam2 = r.getString(3);

            }

            //                 conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Connect to Database (Province)."
                    + "Try Again", "Error Connection", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
    }

    void refNumUpdate() {
        try {

            String sql1 = "update [ClaimsAppSysZvandiri].[dbo].[refNumTab] set "
                    + "REF_NUM ='" + jLabelAcqRefNum.getText() + "' where REF_YEAR ='" + finyear + "' and SERIAL = 'A'";

            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            pst = conn.prepareStatement(sql1);
            pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    void regInitCheck() {
        try {
            imgCount();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            st.executeQuery("SELECT count(*),a.REF_NUM FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a,[ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] "
                    + "b where (a.REF_NUM = b.REF_NUM and a.DOC_VER =b.DOC_VER) "
                    + "and a.EMP_NUM ='" + jLabelEmp.getText() + "' "
                    + "and b.DOC_STATUS = 'AcqRegistered' and a.ACT_REC_STA = 'Q' and "
                    + "concat(PREV_SERIAL,PREV_REF_NUM)  = '" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "' group by a.REF_NUM");

            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                count = Integer.parseInt(rs.getString(1));

            }

            // conn.close();
            if (count > 0) {

                if (("R".equals(jLabelSerial.getText())) && (existCountWkNum > 1)) {
                    jDialogWkDisplay.setVisible(true);
                } else {
                    getAcqWeek();
                    DefaultTableModel dm = (DefaultTableModel) jTableActivityReq.getModel();
                    DefaultTableModel dmAcq = (DefaultTableModel) jTableActivityAcq.getModel();
                    while (dm.getRowCount() > 0) {
                        dm.removeRow(0);
                    }
                    while (dmAcq.getRowCount() > 0) {
                        dmAcq.removeRow(0);
                    }
                    fetchdata();
//                    mainPageTotUpdate();
//                    mainPageTotUpdateAcq();
//                    fetchImgCount();
//                    imgCount();

                }
            } else {
                JOptionPane.showMessageDialog(this, "Reference number is invalid. Please check your reference number.");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void balAmtFinal() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

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

    void getAcqWeek() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT distinct PLAN_WK FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a,[ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] b "
                    + "where (a.REF_NUM = b.REF_NUM and a.DOC_VER =b.DOC_VER) and a.ACT_REC_STA = 'Q' and "
                    + " concat(PREV_SERIAL,PREV_REF_NUM)  = '" + jLabelSerial.getText() + jTextAcqRegNum.getText() + " '");

            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                wkNum = rs.getString(1);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void acqWeek() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();

            st1.executeQuery("SELECT distinct PLAN_WK FROM [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab]"
                    + " where concat(PREV_SERIAL,PREV_REF_NUM) =  'R"
                    + jTextAcqRegNum.getText() + "' and ACQ_STA = 'C' order by 1");

            ResultSet rs1 = st1.getResultSet();

            while (rs1.next()) {
                jComboWk.addItem("Week " + rs1.getString("PLAN_WK"));

            }

            st2.executeQuery("SELECT count(*) FROM [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab]"
                    + " where concat(PREV_SERIAL,PREV_REF_NUM) =  'R" + jTextAcqRegNum.getText() + "' and ACQ_STA = 'C'");

            ResultSet rs2 = st2.getResultSet();

            while (rs2.next()) {
                existCountWkNum = Integer.parseInt(rs2.getString(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void fetchdataWk() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            Statement st2 = conn.createStatement();
            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            Statement st3 = conn.createStatement();
            Statement st4 = conn.createStatement();
            Statement st5 = conn.createStatement();
            Statement st6 = conn.createStatement();
            Statement st7 = conn.createStatement();

            st2.executeQuery("SELECT count(*)  "
                    + " FROM ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab , ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab , ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab "
                    + "where (ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM = ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.REF_NUM "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.SERIAL "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER =ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.DOC_VER "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM = ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.REF_NUM "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.SERIAL "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER =ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.DOC_VER) "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.DOC_STATUS='HeadApprove' and "
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.ACT_REC_STA = 'A' and ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.DOC_STATUS='HeadApprove' "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.ACT_REC_STA = 'A'  "
                    + "and concat(ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM) "
                    + "='" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "'"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM in (Select REF_NUM from ClaimsAppSysZimTTECH.dbo.BatRunTab where STATUS = 'Paid') ");

            ResultSet r2 = st2.getResultSet();

            while (r2.next()) {
                numsearchRef = r2.getInt(1);
            }

            if (numsearchRef == 0) {

                JOptionPane.showMessageDialog(this, "Reference number is invalid . Please check your reference number.");

                new JFrameMain(jLabelEmp.getText()).setVisible(true);
                setVisible(false);

            } else {

                try {
                    findAuthorisation();

                    st1.executeQuery("select top 1 SERIAL,REF_YEAR, REF_NUM,REF_DAT,EMP_NUM,EMP_NAM,"
                            + "EMP_TTL,EMP_PROV,EMP_OFF,EMP_BNK_NAM,ACC_NUM,BUD_COD,ACT_MAIN_PUR,"
                            + "ACT_TOT_AMT,PREV_REF_NUM,PREV_REF_DAT,ACT_REC_STA,REG_MOD_VER +1,REG_MOD_VER,DOC_VER "
                            + "FROM ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab "
                            + "where concat(SERIAL,REF_NUM) = '" + searchRef + "'"
                            + " and ACT_REC_STA = 'Q' ");

                    ResultSet r1 = st1.getResultSet();

                    while (r1.next()) {
                        jLabelAcqSerial.setText(r1.getString(1));
                        jLabelAcqYear.setText(r1.getString(2));
                        jLabelAcqRefNum.setText(r1.getString(3));
                        jLabelAcqDateAcq.setText(r1.getString(4));
                        jLabelAcqEmpNum.setText(r1.getString(5));
                        jLabelAcqEmpNam.setText(r1.getString(6));
                        jLabelAcqEmpTitle.setText(r1.getString(7));
                        jLabelAcqProvince.setText(r1.getString(8));
                        jLabelAcqOffice.setText(r1.getString(9));
                        jLabelAcqBankName.setText(r1.getString(10));
                        jLabelAcqAccNum.setText(r1.getString(11));
                        jLabelAcqActMainPurpose.setText(r1.getString(13));
                        jLabelAcqAppTotReqCostCleared.setText(r1.getString(14));
                        //jLabelReqRefNum.setText(r1.getString(14));
                        jLabelRegDateAcq.setText(r1.getString(16));
//                province = r1.getString(7);
//                searchRef = r1.getString(2);
                        oldRegDocVersion = r1.getString(19);
                        regDocVersion = r1.getString(18);

                    }

                    st.executeQuery("SELECT ACT_DATE,ACT_DEST,"
                            + "ACT_DIST,ACT_ITM_PUR,LN_BUD_CODE,BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,"
                            + "MSC_AMT,ACC_UNPROV_AMT,ACT_ITM_TOT FROM "
                            + "[ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                            + "  where concat(SERIAL,REF_NUM)='" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "' "
                            + "and ACT_REC_STA='A' and PLAN_WK='" + wkNum + "'"
                            + "order by ACT_DATE");

                    ResultSet r = st.getResultSet();

                    while (r.next()) {
                        model.insertRow(model.getRowCount(), new Object[]{r.getString(1), r.getString(2), r.getString(3),
                            r.getString(4), r.getString(5), r.getString(6), r.getString(7), r.getString(8), r.getString(9),
                            r.getString(10), r.getString(11), r.getString(12), r.getString(13)});

                    }

                    st3.executeQuery("SELECT ACT_DATE,ACT_DEST,"
                            + "ACT_DIST,ACT_ITM_PUR,LN_BUD_CODE,BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,"
                            + "MSC_AMT,ACC_UNPROV_AMT,ACT_ITM_TOT FROM "
                            + "[ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                            + "  where concat(SERIAL,REF_NUM)='" + searchRef + "' "
                            + "and ACT_REC_STA='Q' "
                            + "order by ACT_DATE");

                    ResultSet r3 = st3.getResultSet();

                    while (r3.next()) {
                        modelAcq.insertRow(modelAcq.getRowCount(), new Object[]{r3.getString(1), r3.getString(2), r3.getString(3),
                            r3.getString(4), r3.getString(5), r3.getString(6), r3.getString(7), r3.getString(8), r3.getString(9),
                            r3.getString(10), r3.getString(11), r3.getString(12), r3.getString(13)});

                    }

                    st6.executeQuery("SELECT ACTION_VER + 1 from [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] "
                            + "where concat(SERIAL,REF_NUM)='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "'"
                            + " and ACT_REC_STA = 'Q'");

                    ResultSet r6 = st6.getResultSet();

                    while (r6.next()) {
                        actVersion = r6.getString(1);
                    }

                    st7.executeQuery("SELECT NAM_TRAVEL,NAM_VISITED,TRIP_BRIEF FROM"
                            + " [ClaimsAppSysZvandiri].[dbo].[ClaimTripReport] where concat"
                            + "(SERIAL,REF_NUM)='" + searchRef + "' "
                            + "and ACT_REC_STA = 'Q'");

                    ResultSet r7 = st7.getResultSet();

                    while (r7.next()) {
                        jTextAreaNamTravel.setText(r7.getString(1));
                        jTextAreaNamVisited.setText(r7.getString(2));
                        jTextAreaTripReport.setText(r7.getString(3));
                    }

                    st5.executeQuery("SELECT DOC_VER,DOC_VER+1 FROM [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab]"
                            + "WHERE CONCAT (SERIAL,REF_NUM)='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "' "
                            + "AND ACQ_STA = 'C' and PLAN_WK='" + wkNum + "'");

                    ResultSet r5 = st5.getResultSet();
                    while (r5.next()) {
                        docVerClearOld = r5.getString(1);
                        docVerClearNew = r5.getString(2);

                    }

                    fetchTripDet();

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
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

    void mainPageTotUpdate() {
        breakfastsubtotal = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double breakfastamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 5));
            breakfastsubtotal += breakfastamount;

        }
        jLabelBreakFastSubTot.setText(Double.toString(breakfastsubtotal));

        lunchsubtotal = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double lunchamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 6));
            lunchsubtotal += lunchamount;
        }
        jLabelLunchSubTot.setText(Double.toString(lunchsubtotal));

        dinnersubtotal = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double dinneramount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 7));
            dinnersubtotal += dinneramount;
        }
        jLabelDinnerSubTot.setText(Double.toString(dinnersubtotal));

        incidentalsubtotal = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double incidentalamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 8));
            incidentalsubtotal += incidentalamount;
        }
        jLabelIncidentalSubTot.setText(Double.toString(incidentalsubtotal));

        miscSubTot = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double miscamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 10));
            miscSubTot += miscamount;
        }
        jLabelMiscSubTot.setText(Double.toString(miscSubTot));

        unprovedSubTot = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double unprovedamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 11));
            unprovedSubTot += unprovedamount;
        }
        jLabelAccUnprovedSubTot.setText(Double.toString(unprovedSubTot));

        allTotal = 0;
        allTotal = unprovedSubTot + provedSubTot + miscSubTot + incidentalsubtotal
                + dinnersubtotal + lunchsubtotal + breakfastsubtotal;

    }

    void mainPageTotUpdateAcq() {
        breakfastsubtotalAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double breakfastamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 5));
            breakfastsubtotalAcq += breakfastamountAcq;

        }
        jLabelAcqBreakFastSubTot.setText(Double.toString(breakfastsubtotalAcq));

        lunchsubtotalAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double lunchamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 6));
            lunchsubtotalAcq += lunchamountAcq;

        }
        jLabelAcqLunchSubTot.setText(Double.toString(lunchsubtotalAcq));

        dinnersubtotalAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double dinneramountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 7));
            dinnersubtotalAcq += dinneramountAcq;
        }
        jLabelAcqDinnerSubTot.setText(Double.toString(dinnersubtotalAcq));

        incidentalsubtotalAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double incidentalamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 8));
            incidentalsubtotalAcq += incidentalamountAcq;
        }
        jLabelAcqIncidentalSubTot.setText(Double.toString(incidentalsubtotalAcq));

        miscSubTotAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double miscamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 10));
            miscSubTotAcq += miscamountAcq;
        }
        jLabelAcqMiscSubTot.setText(Double.toString(miscSubTotAcq));

        unprovedSubTotAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double unprovedamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 11));
            unprovedSubTotAcq += unprovedamountAcq;

        }
        jLabelAcqAccUnprovedSubTot.setText(Double.toString(unprovedSubTotAcq));

        allTotalAcq = unprovedSubTotAcq + miscSubTotAcq + incidentalsubtotalAcq
                + dinnersubtotalAcq + lunchsubtotalAcq + breakfastsubtotalAcq;

        jLabelAcqBreakFastSubTotBal.setText(Double.toString(breakfastsubtotal - breakfastsubtotalAcq));
        jLabelAcqLunchSubTotBal.setText(Double.toString(lunchsubtotal - lunchsubtotalAcq));
        jLabelAcqDinnerSubTotBal.setText(Double.toString(dinnersubtotal - dinnersubtotalAcq));
        jLabelAcqIncidentalSubTotBal.setText(Double.toString(incidentalsubtotal - incidentalsubtotalAcq));
        jLabelAcqMiscSubTotBal.setText(Double.toString(miscSubTot - miscSubTotAcq));
        jLabelAcqAccUnprovedSubTotBal.setText(Double.toString(unprovedSubTot - unprovedSubTotAcq));

        jLabelAcqAppTotReqCostCleared.setText(Double.toString(allTotalAcq));
        jLabelAcqAppTotReqCost.setText(Double.toString(allTotal - allTotalAcq));
        double totDiff = allTotal - allTotalAcq;
//        if (totDiff > 0) {
//            jLabelAppTotReq.setText("Total (Change)");
//        } else if (totDiff < 0) {
//            jLabelAppTotReq.setText("Total (Shortfall)");
//            jLabelPay.setText("");
//            jLabelAcqPayBack.setText("");
//            jLabelAcqAmtPayBack.setText("");
//        } else if (totDiff == 0) {
//            jLabelAppTotReq.setText("Total (Balancing)");
//            jLabelPay.setText("");
//            jLabelAcqPayBack.setText("");
//            jLabelAcqAmtPayBack.setText("");
//        }
//        balAmtFinal();
//        jLabelAppTotReq1.setText("Total Week " + wkNum);
//        jLabelAcqAppTotReqCost1.setText(String.format("%.2f", allTotalAcq));
        //       jLabelAcqAppTotReqCost.setText(Double.toString(balAmt));
//        double totDiff = balAmt;
        if (("R".equals((jLabelSerial.getText()))) || (Integer.parseInt(jTextAcqRegNum.getText()) <= 24)) {
            if (totDiff > 0) {
                jLabelAppTotReq.setText("Grand Total (Change)");
            } else if (totDiff < 0) {
                jLabelAppTotReq.setText("Grand Total (Shortfall)");
            } else if (totDiff == 0) {
                jLabelAppTotReq.setText("Grand Total (Balancing)");
            }
        } else {
            jLabelAcqAppTotReqCost.setText(Double.toString(allTotalAcq));
        }
    }

//    void mainPageTotUpdate() {
//        breakfastsubtotal = 0;
//        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
//            double breakfastamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 4));
//            breakfastsubtotal += breakfastamount;
//
//        }
//        jLabelBreakFastSubTot.setText(Double.toString(breakfastsubtotal));
//
//        lunchsubtotal = 0;
//        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
//            double lunchamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 5));
//            lunchsubtotal += lunchamount;
//        }
//        jLabelLunchSubTot.setText(Double.toString(lunchsubtotal));
//
//        dinnersubtotal = 0;
//        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
//            double dinneramount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 6));
//            dinnersubtotal += dinneramount;
//        }
//        jLabelDinnerSubTot.setText(Double.toString(dinnersubtotal));
//
//        incidentalsubtotal = 0;
//        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
//            double incidentalamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 7));
//            incidentalsubtotal += incidentalamount;
//        }
//        jLabelIncidentalSubTot.setText(Double.toString(incidentalsubtotal));
//
//        miscSubTot = 0;
//        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
//            double miscamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 9));
//            miscSubTot += miscamount;
//        }
//        jLabelMiscSubTot.setText(Double.toString(miscSubTot));
//
//        provedSubTot = 0;
//        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
//            double provedamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 10));
//            provedSubTot += provedamount;
//        }
//        jLabelAccProvedSubTot.setText(Double.toString(provedSubTot));
//
//        unprovedSubTot = 0;
//        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
//            double unprovedamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 11));
//            unprovedSubTot += unprovedamount;
//        }
//        jLabelAccUnprovedSubTot.setText(Double.toString(unprovedSubTot));
//
//        allTotal = 0;
//        allTotal = unprovedSubTot + provedSubTot + miscSubTot + incidentalsubtotal
//                + dinnersubtotal + lunchsubtotal + breakfastsubtotal;
//
//    }
//
//    void mainPageTotUpdateAcq() {
//        breakfastsubtotalAcq = 0;
//        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
//            double breakfastamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 4));
//            breakfastsubtotalAcq += breakfastamountAcq;
//
//        }
//        jLabelAcqBreakFastSubTot.setText(Double.toString(breakfastsubtotalAcq));
//
//        lunchsubtotalAcq = 0;
//        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
//            double lunchamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 5));
//            lunchsubtotalAcq += lunchamountAcq;
//
//        }
//        jLabelAcqLunchSubTot.setText(Double.toString(lunchsubtotalAcq));
//
//        dinnersubtotalAcq = 0;
//        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
//            double dinneramountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 6));
//            dinnersubtotalAcq += dinneramountAcq;
//        }
//        jLabelAcqDinnerSubTot.setText(Double.toString(dinnersubtotalAcq));
//
//        incidentalsubtotalAcq = 0;
//        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
//            double incidentalamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 7));
//            incidentalsubtotalAcq += incidentalamountAcq;
//        }
//        jLabelAcqIncidentalSubTot.setText(Double.toString(incidentalsubtotalAcq));
//
//        miscSubTotAcq = 0;
//        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
//            double miscamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 9));
//            miscSubTotAcq += miscamountAcq;
//        }
//        jLabelAcqMiscSubTot.setText(Double.toString(miscSubTotAcq));
//
//        provedSubTotAcq = 0;
//        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
//            double provedamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 10));
//            provedSubTotAcq += provedamountAcq;
//        }
//        jLabelAcqAccProvedSubTot.setText(Double.toString(provedSubTotAcq));
//
//        unprovedSubTotAcq = 0;
//        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
//            double unprovedamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 11));
//            unprovedSubTotAcq += unprovedamountAcq;
//        }
//        jLabelAcqAccUnprovedSubTotBal.setText(Double.toString(unprovedSubTotAcq));
//
//        allTotalAcq = unprovedSubTotAcq + provedSubTotAcq + miscSubTotAcq + incidentalsubtotalAcq
//                + dinnersubtotalAcq + lunchsubtotalAcq + breakfastsubtotalAcq;
//
//        jLabelAcqBreakFastSubTotBal.setText(Double.toString(breakfastsubtotal - breakfastsubtotalAcq));
//        jLabelAcqLunchSubTotBal.setText(Double.toString(lunchsubtotal - lunchsubtotalAcq));
//        jLabelAcqDinnerSubTotBal.setText(Double.toString(dinnersubtotal - dinnersubtotalAcq));
//        jLabelAcqIncidentalSubTotBal.setText(Double.toString(incidentalsubtotal - incidentalsubtotalAcq));
//        jLabelAcqMiscSubTotBal.setText(Double.toString(miscSubTot - miscSubTotAcq));
//        jLabelAcqAccUnprovedSubTotBal.setText(Double.toString(unprovedSubTot - unprovedSubTotAcq));
//        jLabelAcqAccProvedSubTotBal.setText(Double.toString(provedSubTot - provedSubTotAcq));
//
//        jLabelAcqAppTotReqCost.setText(Double.toString(allTotal - allTotalAcq));
//        double totDiff = allTotal - allTotalAcq;
//        if (totDiff > 0) {
//            jLabelAppTotReq.setText("Total (Change)");
//        } else if (totDiff < 0) {
//            jLabelAppTotReq.setText("Total (Shortfall)");
//            jLabelPay.setText("");
//            jLabelAcqPayBack.setText("");
//            jLabelAcqAmtPayBack.setText("");
//        } else if (totDiff == 0) {
//            jLabelAppTotReq.setText("Total (Balancing)");
//            jLabelPay.setText("");
//            jLabelAcqPayBack.setText("");
//            jLabelAcqAmtPayBack.setText("");
//        }
//
//    }
    void fetchDataForAcq() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            st.executeQuery("SELECT distinct ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_YEAR,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_DAT, ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ITM_NUM,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_DATE,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_DEST, \n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_DIST,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_ITM_PUR,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.BRK_AMT,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.LNC_AMT,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.DIN_AMT,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.INC_AMT,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.MSC_ACT,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.MSC_AMT,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACC_PRO_AMT, ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACC_UNPROV_AMT, \n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_ITM_TOT,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.PREV_REF_NUM,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.PREV_REF_DAT FROM ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab, \n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab , ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab where \n"
                    + "(ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM = ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.REF_NUM \n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.SERIAL \n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER =ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.DOC_VER \n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM = ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.REF_NUM \n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.SERIAL \n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER =ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.DOC_VER\n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REG_MOD_VER= ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.REG_MOD_VER\n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REG_MOD_VER= ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.REG_MOD_VER) and  \n"
                    + "(ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.DOC_STATUS='AcqRegistered' and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.ACT_REC_STA = 'Q' \n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = 'A')  and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REG_MOD_VER =(select max(REG_MOD_VER) from ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab\n"
                    + "where concat(SERIAL,REF_NUM)='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "')\n"
                    + "and concat(ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.PREV_SERIAL,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.PREV_REF_NUM) \n"
                    + "= '" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "' order by\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_DATE");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelAcq.insertRow(modelAcq.getRowCount(), new Object[]{r.getString(5), r.getString(6), r.getString(7),
                    r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12), r.getString(13),
                    r.getString(14), r.getString(15), r.getString(16), r.getString(17)});

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findChgPaid() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            st.executeQuery("SELECT count(*) FROM[ClaimsAppSysZimTTECH].[dbo].[ChgPaidTab]"
                    + " where concat(PREV_SERIAL,PREV_REF_NUM) = "
                    + "'" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "' "
                    + "and REG_MOD_VER = (select REG_MOD_VER FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] "
                    + "where concat(PREV_SERIAL,PREV_REF_NUM) ="
                    + "'" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "' and ACT_REC_STA = 'Q' "
                    + "and concat (SERIAL,REF_NUM) = '" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "' )");

            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                pdCount = rs.getInt(1);

            }

            if (pdCount > 0) {

                st1.executeQuery("SELECT *  FROM [ClaimsAppSysZvandiri].[dbo].[ChgPaidTab] "
                        + "  Where concat(PREV_SERIAL,PREV_REF_NUM) ="
                        + " '" + jLabelSerial.getText() + jTextAcqRegNum.getText()
                        + "'  and concat (SERIAL,REF_NUM) = '" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "' "
                        + "and REG_MOD_VER = (select max(REG_MOD_VER) from [ClaimsAppSysZvandiri].[dbo].[ChgPaidTab] "
                        + "where concat(PREV_SERIAL,PREV_REF_NUM) = "
                        + "'" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "' "
                        + "and concat (SERIAL,REF_NUM) = '" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "')");

                ResultSet rs1 = st1.getResultSet();

                while (rs1.next()) {
                    jLabelPay.setText("Paid Back A/C");
                    jLabelAcqPayBack.setText(rs1.getString(6));
                    jLabelAcqAmtPayBack.setText(rs1.getString(7));
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void chgBank() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            String sqlUpdate = "UPDATE [ClaimsAppSysZvandiri].[dbo].[ChgPaidTab] set ACT_REC_STA ='QM' where "
                    + "REF_NUM='" + jLabelAcqRefNum.getText() + "'";

            pst1 = conn.prepareStatement(sqlUpdate);
            pst1.executeUpdate();

            String sqlbank = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ChgPaidTab] "
                    + "( PREV_SERIAL,PREV_REF_NUM,SERIAL,REF_NUM,BANK_NAME,"
                    + "ACCOUNT,AMOUNT,ACT_REC_STA,DOC_STATUS,REG_MOD_VER) VALUES (?,?,?,?,?,?,?,?,?,?)";

            pst = conn.prepareStatement(sqlbank);
            pst.setString(1, jLabelSerial.getText());
            pst.setString(2, jTextAcqRegNum.getText());
            pst.setString(3, jLabelAcqSerial.getText());
            pst.setString(4, jLabelAcqRefNum.getText());
            pst.setString(5, jComboBankNam.getSelectedItem().toString());
            pst.setString(6, jLabelAcqPayBack.getText());
            pst.setString(7, jLabelAcqAmtPayBack.getText());
            pst.setString(8, "Q");
            pst.setString(9, "1");
            pst.setString(10, regDocVersion);

            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findBankName() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT ACCOUNT_NAME FROM [ClaimsAppSysZvandiri].[dbo].[OPHIDBankAccTab]");

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
            message.setSubject("Approval Request.Perdiem Acquittal Application Reference # A " + jLabelAcqRefNum.getText());

            message.setContent("<html><body> Dear " + sendTo + "<br><br>" + createUsrNam
                    + " has modified and resubmitted a perdiem acquittal for your approval. <br><br>Please check "
                    + "your supervisor list and action.<br><br> Kind Regards <br><br>"
                    + " Perdiem Management System </body></html>", "text/html;charset=utf-8");

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    void findBankAcc() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();

            //  jComboBankNam.setSelectedIndex(-1);
            ResultSet r = st.executeQuery("SELECT ACCOUNT FROM [ClaimsAppSysZvandiri].[dbo].[OPHIDBankAccTab] "
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

    void fetchdata() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            Statement st2 = conn.createStatement();
            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            Statement st3 = conn.createStatement();
            Statement st4 = conn.createStatement();
            Statement st5 = conn.createStatement();
            Statement st6 = conn.createStatement();
            Statement st7 = conn.createStatement();
            Statement st9 = conn.createStatement();

            st2.executeQuery("SELECT count(*)  FROM ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab a, "
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab b, ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab c "
                    + "where (a.REF_NUM = b.REF_NUM and a.SERIAL = b.SERIAL and a.DOC_VER =b.DOC_VER and "
                    + "a.REF_NUM = c.REF_NUM and a.SERIAL = c.SERIAL and a.DOC_VER =c.DOC_VER) and "
                    + "c.DOC_STATUS='HODApprove' and a.ACT_REC_STA = 'A' and c.DOC_STATUS='HODApprove' and "
                    + "a.ACT_REC_STA = 'A'  and concat(a.SERIAL,a.REF_NUM) ='" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "'"
                    + " and a.REF_NUM in (Select REF_NUM from ClaimsAppSysZimTTECH.dbo.BatRunTab where STATUS = 'Paid') ");

            ResultSet r2 = st2.getResultSet();

            while (r2.next()) {
                numsearchRef = r2.getInt(1);
            }

            if (numsearchRef == 0) {

                JOptionPane.showMessageDialog(this, "Reference number is invalid . Please check your reference number.");

                new JFrameMain(jLabelEmp.getText()).setVisible(true);
                setVisible(false);

            } else {

                try {
                   

                    st4.executeQuery("SELECT distinct a.REF_YEAR,a.SERIAL, a.REF_NUM,a.REF_DAT, "
                            + "a.DOC_VER +1,a.DOC_VER,a.REG_MOD_VER + 1, a.REG_MOD_VER,c.ACTION_VER + 1 "
                            + "FROM ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab a, ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab b,"
                            + " ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab c where (a.REF_NUM = b.REF_NUM and "
                            + "a.SERIAL = b.SERIAL and a.DOC_VER =b.DOC_VER and a.REF_NUM = c.REF_NUM and "
                            + "a.SERIAL = c.SERIAL and a.DOC_VER =c.DOC_VER) and  (c.DOC_STATUS='AcqRegistered' "
                            + "and a.ACT_REC_STA = 'Q' and a.SERIAL = 'A')   and concat(a.PREV_SERIAL, "
                            + "a.PREV_REF_NUM) = '"+jLabelSerial.getText()+jTextAcqRegNum.getText()+"'");

                    ResultSet r4 = st4.getResultSet();

                    while (r4.next()) {
                        jLabelAcqYear.setText(r4.getString(1));
                        jLabelAcqSerial.setText(r4.getString(2));
                        jLabelAcqRefNum.setText(r4.getString(3));
                        regDocVersion = r4.getString(7);
                        jLabelAcqDateAcq.setText(r4.getString(4));
                        oldRegDocVersion = r4.getString(8);
                        actVersion = r4.getString(9);

                    }

                    st1.executeQuery("select top 1 SERIAL,REF_YEAR, REF_NUM,REF_DAT,EMP_NUM,EMP_NAM,"
                            + "EMP_TTL,EMP_PROV,EMP_OFF,EMP_BNK_NAM,ACC_NUM,BUD_COD,ACT_MAIN_PUR,"
                            + "ACT_TOT_AMT,PREV_REF_NUM,PREV_REF_DAT,ACT_REC_STA,REG_MOD_VER +1,REG_MOD_VER,DOC_VER "
                            + "FROM ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab "
                            + "where concat(SERIAL,REF_NUM) ='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "' "
                            + " and ACT_REC_STA = 'Q' ");

                    ResultSet r1 = st1.getResultSet();

                    while (r1.next()) {
                        jLabelAcqSerial.setText(r1.getString(1));
                        jLabelAcqYear.setText(r1.getString(2));
                        jLabelAcqRefNum.setText(r1.getString(3));
                        jLabelAcqDateAcq.setText(r1.getString(4));
                        jLabelAcqEmpNum.setText(r1.getString(5));
                        jLabelAcqEmpNam.setText(r1.getString(6));
                        jLabelAcqEmpTitle.setText(r1.getString(7));
                        jLabelAcqProvince.setText(r1.getString(8));
                        jLabelAcqOffice.setText(r1.getString(9));
                        jLabelAcqBankName.setText(r1.getString(10));
                        jLabelAcqAccNum.setText(r1.getString(11));
                        jLabelAcqActMainPurpose.setText(r1.getString(13));
                        jLabelAcqAppTotReqCostCleared.setText(r1.getString(14));
                        //jLabelReqRefNum.setText(r1.getString(14));
                        jLabelRegDateAcq.setText(r1.getString(16));
//                province = r1.getString(7);
//                searchRef = r1.getString(2);
                        oldRegDocVersion = r1.getString(19);
                        regDocVersion = r1.getString(18);

                    }

                    st.executeQuery("SELECT ACT_DATE,ACT_DEST,"
                            + "ACT_DIST,ACT_ITM_PUR,LN_BUD_CODE,BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,"
                            + "MSC_AMT,ACC_UNPROV_AMT,ACT_ITM_TOT FROM "
                            + "[ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                            + "  where concat(SERIAL,REF_NUM)='" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "' "
                            + "and ACT_REC_STA='A' and PLAN_WK='" + wkNum + "'"
                            + "order by ACT_DATE");

                    ResultSet r = st.getResultSet();

                    while (r.next()) {
                        model.insertRow(model.getRowCount(), new Object[]{r.getString(1), r.getString(2), r.getString(3),
                            r.getString(4), r.getString(5), r.getString(6), r.getString(7), r.getString(8), r.getString(9),
                            r.getString(10), r.getString(11), r.getString(12), r.getString(13)});

                    }

                    st3.executeQuery("SELECT ACT_DATE,ACT_DEST,"
                            + "ACT_DIST,ACT_ITM_PUR,LN_BUD_CODE,BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,"
                            + "MSC_AMT,ACC_UNPROV_AMT,ACT_ITM_TOT FROM "
                            + "[ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                            + "  where concat(SERIAL,REF_NUM)='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "' "
                            + "and ACT_REC_STA='Q' and PLAN_WK='" + wkNum + "'"
                            + "order by ACT_DATE");

                    ResultSet r3 = st3.getResultSet();

                    while (r3.next()) {
                        modelAcq.insertRow(modelAcq.getRowCount(), new Object[]{r3.getString(1), r3.getString(2), r3.getString(3),
                            r3.getString(4), r3.getString(5), r3.getString(6), r3.getString(7), r3.getString(8), r3.getString(9),
                            r3.getString(10), r3.getString(11), r3.getString(12), r3.getString(13)});

                    }

                    st7.executeQuery("SELECT NAM_TRAVEL,NAM_VISITED,TRIP_BRIEF,PLAN_WK FROM"
                            + " [ClaimsAppSysZvandiri].[dbo].[ClaimTripReport] where concat"
                            + "(SERIAL,REF_NUM)='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "'"
                            + "and ACT_REC_STA='Q' ");

                    ResultSet r7 = st7.getResultSet();

                    while (r7.next()) {
                        jTextAreaNamTravel.setText(r7.getString(1));
                        jTextAreaNamVisited.setText(r7.getString(2));
                        jTextAreaTripReport.setText(r7.getString(3));
                        paraone = r7.getString(1);
                        paratwo = r7.getString(2);
                        parathree = r7.getString(3);
                        wkNumRep = r7.getString(4);

                    }

                    st6.executeQuery("SELECT DOC_VER,DOC_VER+1 FROM [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab]"
                            + "WHERE CONCAT (SERIAL,REF_NUM)='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText()
                            + "' AND ACQ_STA = 'C' and PLAN_WK='" + wkNum + "'");

                    ResultSet r6 = st6.getResultSet();
                    while (r6.next()) {
                        docVerClearOld = r6.getString(1);
                        docVerClearNew = r6.getString(2);

                    }

                    getLowestDate();
//                    fetchTripDet();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void clearTable() {

        modelTrip.getDataVector().removeAllElements();
        modelTrip.fireTableDataChanged();
    }

    void getTripDetails() {
        try {
            String str = jTextVehReg.getText();
            str = str.replaceAll("\\s", "");
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

    void deleteFileAtt() {

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

    void fetchTripDet() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();
            st.executeQuery("SELECT ACT_PROJ,VEH_REG_NUM,EMP_NUM,DEPART_LOC,ARRIVAL_LOC,TRIP_PUR,"
                    + "DEPART_DATE,CONVERT(VARCHAR(5),DEPART_TIME),DEPART_MILAGE,"
                    + "ARRIVAL_DATE,CONVERT(VARCHAR(5),ARRIVAL_TIME),ARRIVAL_MILAGE,"
                    + "LIN_TOT_DIS FROM [ClaimsAppSysZvandiri].[dbo].[ClaimTripMilageTab] "
                    + "  where concat(SERIAL,REF_NUM)='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "' "
                    + "and ACT_REC_STA='Q' and PLAN_WK='" + wkNum + "'"
                    + "order by DEPART_MILAGE");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelTrip.insertRow(modelTrip.getRowCount(), new Object[]{r.getString(1), r.getString(2), r.getString(3),
                    r.getString(4), r.getString(5), r.getString(6), r.getString(7), r.getString(8), r.getString(9),
                    r.getString(10), r.getString(11), r.getString(12), r.getString(13)});

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdataSpecial() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();
            Statement st3 = conn.createStatement();
            Statement st4 = conn.createStatement();
            Statement st7 = conn.createStatement();

            st1.executeQuery("SELECT top 1 ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_YEAR,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_DAT, \n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.EMP_NUM,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.EMP_NAM,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.EMP_TTL, ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.EMP_PROV,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.EMP_OFF,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.EMP_BNK_NAM,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.ACC_NUM,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.BUD_COD,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.ACT_MAIN_PUR,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.ACT_TOT_AMT,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.ACT_REC_STA,ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.SPECIAL_REASON,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.USR_ACTION,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REG_MOD_VER  +1,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER,ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.ACTION_VER + 1,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REG_MOD_VER,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.PLAN_WK\n"
                    + "FROM ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab , ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab , ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab \n"
                    + "where (ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM = ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.REF_NUM \n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.SERIAL \n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER =ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.DOC_VER \n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM = ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.REF_NUM \n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REG_MOD_VER = ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.REG_MOD_VER\n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.SERIAL \n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER =ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.DOC_VER\n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REG_MOD_VER = ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.REG_MOD_VER ) and\n"
                    + "(ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.DOC_STATUS='AcqRegistered' and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.ACT_REC_STA = 'Q' \n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = 'S') \n"
                    + "and concat(ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM) \n"
                    + "='" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "'");

            ResultSet r1 = st1.getResultSet();

            while (r1.next()) {
                jLabelAcqSerial.setText(r1.getString(1));
                jLabelAcqYear.setText(r1.getString(2));
                jLabelAcqRefNum.setText(r1.getString(3));
                jLabelRegDateAcq.setText(r1.getString(4));
                jLabelAcqEmpNum.setText(r1.getString(5));
                jLabelAcqEmpNam.setText(r1.getString(6));
                jLabelAcqEmpTitle.setText(r1.getString(7));
                jLabelAcqProvince.setText(r1.getString(8));
                jLabelAcqOffice.setText(r1.getString(9));
                jLabelAcqBankName.setText(r1.getString(10));
                jLabelAcqAccNum.setText(r1.getString(11));
                jLabelAcqBudCode.setText(r1.getString(12));
                jLabelAcqActMainPurpose.setText(r1.getString(13));
                jLabelAcqAppTotReqCost.setText(r1.getString(14));
                jLabelAcqDateAcq.setText(r1.getString(4));
                searchRef = r1.getString(3);
                regDocVersion = r1.getString(18);
                oldRegDocVersion = r1.getString(21);
                actVersion = r1.getString(20);
                wkNum = r1.getString(22);

            }

            model.insertRow(model.getRowCount(), new Object[]{"1901-01-01", "Special", "0",
                "Special", "", "0", "0", "0", "0", "",
                "0", "0", "0"});

            st2.executeQuery("SELECT distinct ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_YEAR,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_DAT, ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ITM_NUM,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_DATE,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_DEST, \n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_DIST,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_ITM_PUR,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.LN_BUD_CODE,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.BRK_AMT,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.LNC_AMT,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.DIN_AMT,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.INC_AMT,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.MSC_ACT,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.MSC_AMT,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACC_UNPROV_AMT, \n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_ITM_TOT,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.PREV_REF_NUM,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.PREV_REF_DAT FROM ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab, \n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab , ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab where \n"
                    + "(ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM = ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.REF_NUM \n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.SERIAL \n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER =ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.DOC_VER \n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM = ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.REF_NUM \n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.SERIAL \n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER =ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.DOC_VER\n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REG_MOD_VER= ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.REG_MOD_VER\n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REG_MOD_VER= ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.REG_MOD_VER) and  \n"
                    + "(ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.DOC_STATUS='AcqRegistered' and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.ACT_REC_STA = 'Q' \n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = 'S')  and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REG_MOD_VER =(select max(REG_MOD_VER) from ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab\n"
                    + "where concat(SERIAL,REF_NUM)='" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "')"
                    + "and concat(ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM)='" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "' order by ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_DATE");

            ResultSet r2 = st2.getResultSet();

            while (r2.next()) {
                modelAcq.insertRow(modelAcq.getRowCount(), new Object[]{r2.getString(5), r2.getString(6), r2.getString(7),
                    r2.getString(8), r2.getString(9), r2.getString(10), r2.getString(11), r2.getString(12), r2.getString(13),
                    r2.getString(14), r2.getString(15), r2.getString(16), r2.getString(17)});

            }

            st7.executeQuery("SELECT NAM_TRAVEL,NAM_VISITED,TRIP_BRIEF FROM"
                    + " [ClaimsAppSysZvandiri].[dbo].[ClaimTripReport] where concat"
                    + "(SERIAL,REF_NUM)='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "' "
                    + "and ACT_REC_STA = 'Q'");

            ResultSet r7 = st7.getResultSet();

            while (r7.next()) {
                jTextAreaNamTravel.setText(r7.getString(1));
                jTextAreaNamVisited.setText(r7.getString(2));
                jTextAreaTripReport.setText(r7.getString(3));
            }
//
            fetchImgCount();
            imgCount();

        } catch (Exception e) {
            System.out.println(e);
        }
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
            byte[] buf = new byte[512];

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
            if (!(f2.getName().endsWith(".jpg") || f2.getName().endsWith(".gif")
                    || f2.getName().endsWith(".png") || f2.getName().endsWith(".jpeg"))) {
                JOptionPane.showMessageDialog(null, "Please select file with .jpg, .bmp, .png, .jpeg and .gif extension only.");
            }
        }

        ImageIcon imageIcon2 = new ImageIcon(new ImageIcon(filename2).getImage().getScaledInstance(jLabelImgFile2.getWidth(), jLabelImgFile2.getHeight(), Image.SCALE_SMOOTH));
        jLabelImgFile2.setIcon(imageIcon2);
        try {
            File image2 = new File(filename2);
            FileInputStream fis2 = new FileInputStream(image2);
            ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
            byte[] buf2 = new byte[1024];

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
            if (!(f3.getName().endsWith(".jpg") || f3.getName().endsWith(".gif")
                    || f3.getName().endsWith(".png") || f3.getName().endsWith(".jpeg"))) {
                JOptionPane.showMessageDialog(null, "Please select file with .jpg, .bmp, .png, .jpeg and .gif extension only.");
            }
        }

        ImageIcon imageIcon3 = new ImageIcon(new ImageIcon(filename3).getImage().getScaledInstance(jLabelImgFile3.getWidth(), jLabelImgFile3.getHeight(), Image.SCALE_SMOOTH));
        jLabelImgFile3.setIcon(imageIcon3);
        try {
            File image3 = new File(filename3);
            FileInputStream fis3 = new FileInputStream(image3);
            ByteArrayOutputStream bos3 = new ByteArrayOutputStream();
            byte[] buf3 = new byte[1034];

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

    void imgChooser4() {

        JFileChooser chooser4 = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("images", "jpg", "gif", "png", "jpeg");
        chooser4.setFileFilter(filter);
        chooser4.setMultiSelectionEnabled(true);
        int returnVal = chooser4.showOpenDialog(null);
        File f4 = chooser4.getSelectedFile();
        filename4 = f4.getAbsolutePath();
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (!(f4.getName().endsWith(".jpg") || f4.getName().endsWith(".gif")
                    || f4.getName().endsWith(".png") || f4.getName().endsWith(".jpeg"))) {
                JOptionPane.showMessageDialog(null, "Please select file with .jpg, .bmp, .png, .jpeg and .gif extension only.");
            }
        }

        ImageIcon imageIcon = new ImageIcon(new ImageIcon(filename4).getImage().getScaledInstance(jLabelImgFile4.getWidth(), jLabelImgFile4.getHeight(), Image.SCALE_SMOOTH));
        jLabelImgFile4.setIcon(imageIcon);

        try {
            File image = new File(filename4);
            FileInputStream fis = new FileInputStream(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[128];

            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
            }
            person_image4 = bos.toByteArray();

        } catch (Exception e) {
            System.out.println(e);

        }
    }

    void imgChooser5() {

        JFileChooser chooser5 = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("images", "jpg", "gif", "png", "jpeg");
        chooser5.setFileFilter(filter);
        chooser5.setMultiSelectionEnabled(true);
        int returnVal = chooser5.showOpenDialog(null);
        File f5 = chooser5.getSelectedFile();
        filename5 = f5.getAbsolutePath();
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (!(f5.getName().endsWith(".jpg") || f5.getName().endsWith(".gif")
                    || f5.getName().endsWith(".png") || f5.getName().endsWith(".jpeg"))) {
                JOptionPane.showMessageDialog(null, "Please select file with .jpg, .bmp, .png, .jpeg and .gif extension only.");
            }
        }

        ImageIcon imageIcon = new ImageIcon(new ImageIcon(filename5).getImage().getScaledInstance(jLabelImgFile5.getWidth(), jLabelImgFile5.getHeight(), Image.SCALE_SMOOTH));
        jLabelImgFile5.setIcon(imageIcon);

        try {
            File image = new File(filename5);
            FileInputStream fis = new FileInputStream(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[128];

            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
            }
            person_image5 = bos.toByteArray();

        } catch (Exception e) {
            System.out.println(e);

        }
    }

    void imgDisplayFile1() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            String imgDisFile1 = "";

            if ("S".equals(jLabelSerial.getText())) {
                imgDisFile1 = jLabelSerial.getText() + jTextAcqRegNum.getText();
            } else {
                imgDisFile1 = jLabelAcqSerial.getText() + jLabelAcqRefNum.getText();
            }

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT SERIAL,REF_NUM,ATT_IMAGE,IMG_VERSION "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[DocAttachTab] where concat(SERIAL,REF_NUM)="
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
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            String imgDisFile2 = "";

            if ("S".equals(jLabelSerial.getText())) {
                imgDisFile2 = jLabelSerial.getText() + jTextAcqRegNum.getText();
            } else {
                imgDisFile2 = jLabelAcqSerial.getText() + jLabelAcqRefNum.getText();
            }

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT SERIAL,REF_NUM,ATT_IMAGE,IMG_VERSION "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[DocAttachTab] where concat(SERIAL,REF_NUM)="
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
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            String imgDisFile3 = "";

            if ("S".equals(jLabelSerial.getText())) {
                imgDisFile3 = jLabelSerial.getText() + jTextAcqRegNum.getText();
            } else {
                imgDisFile3 = jLabelAcqSerial.getText() + jLabelAcqRefNum.getText();
            }

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT SERIAL,REF_NUM,ATT_IMAGE,IMG_VERSION "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[DocAttachTab] where concat(SERIAL,REF_NUM)="
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
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            String imgDisFile4 = "";

            if ("S".equals(jLabelSerial.getText())) {
                imgDisFile4 = jLabelSerial.getText() + jTextAcqRegNum.getText();
            } else {
                imgDisFile4 = jLabelAcqSerial.getText() + jLabelAcqRefNum.getText();
            }

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT SERIAL,REF_NUM,ATT_IMAGE,IMG_VERSION "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[DocAttachTab] where concat(SERIAL,REF_NUM)="
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
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            String imgDisFile5 = "";

            if ("S".equals(jLabelSerial.getText())) {
                imgDisFile5 = jLabelSerial.getText() + jTextAcqRegNum.getText();
            } else {
                imgDisFile5 = jLabelAcqSerial.getText() + jLabelAcqRefNum.getText();
            }

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT SERIAL,REF_NUM,ATT_IMAGE,IMG_VERSION "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[DocAttachTab] where concat(SERIAL,REF_NUM)="
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

    void saveAcqEdit() {
        ackStatus();
        if ((("Total (Change)").equals(jLabelAppTotReq.getText())) && ("Y".equals(ackStatus))
                && (("".equals(jLabelAcqPayBack.getText())) || ("".equals(jLabelAcqAmtPayBack.getText()))
                || ("0.00".equals(jLabelAcqAmtPayBack.getText())))) {
            // findBankName();
            jDialogChgPaid.setVisible(true);

        } else {
            try {

                Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                        + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

                String sql = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] "
                        + "(REF_YEAR,SERIAL,REF_NUM,REF_DAT,EMP_NUM,EMP_NAM,EMP_TTL,EMP_PROV,EMP_OFF,"
                        + "EMP_BNK_NAM,ACC_NUM,BUD_COD,ACT_MAIN_PUR,ACT_TOT_AMT,ACT_REC_STA,DOC_VER,"
                        + "PREV_SERIAL,PREV_REF_NUM,PREV_REF_DAT,REG_MOD_VER) "
                        + " VALUES (?,?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?)";
                pst = conn.prepareStatement(sql);

                if ("S".equals(jLabelSerial.getText())) {
                    pst.setString(1, df.format(curYear));
                    pst.setString(2, jLabelSerial.getText());
                    pst.setString(3, jTextAcqRegNum.getText());
                    pst.setString(4, jLabelRegDateAcq.getText());
                } else {
                    pst.setString(1, jLabelAcqYear.getText());
                    pst.setString(2, jLabelAcqSerial.getText());
                    pst.setString(3, jLabelAcqRefNum.getText());
                    pst.setString(4, jLabelAcqDateAcq.getText());
                }
                pst.setString(5, jLabelAcqEmpNum.getText());
                pst.setString(6, jLabelAcqEmpNam.getText());
                pst.setString(7, jLabelAcqEmpTitle.getText());
                pst.setString(8, jLabelAcqProvince.getText());
                pst.setString(9, jLabelAcqOffice.getText());
                pst.setString(10, jLabelAcqBankName.getText());
                pst.setString(11, jLabelAcqAccNum.getText());
                pst.setString(12, jLabelAcqBudCode.getText());
                pst.setString(13, jLabelAcqActMainPurpose.getText());
                pst.setString(14, jLabelAcqAppTotReqCostCleared.getText());
                pst.setString(15, "Q");
                pst.setString(16, "1");
                pst.setString(17, jLabelSerial.getText());
                pst.setString(18, String.valueOf(jTextAcqRegNum.getText()));
                pst.setString(19, jLabelRegDateAcq.getText());
                pst.setString(20, regDocVersion);
                pst.executeUpdate();

                for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {

                    String sqlitm = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                            + "(REF_YEAR,SERIAL,REF_NUM,ITM_NUM,ACT_DATE,ACT_DEST,ACT_DIST,ACT_ITM_PUR,"
                            + "LN_BUD_CODE,BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,MSC_AMT,"
                            + "ACC_UNPROV_AMT, ACT_ITM_TOT,DOC_VER,REG_MOD_VER,ACT_REC_STA,PLAN_WK)"
                            + " VALUES (?,?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?)";

                    Connection conn1 = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                            + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
                    pst1 = conn1.prepareStatement(sqlitm);

                    if ("S".equals(jLabelSerial.getText())) {
                        pst1.setString(1, df.format(curYear));
                        pst1.setString(2, jLabelSerial.getText());
                        pst1.setString(3, jTextAcqRegNum.getText());

                    } else {
                        pst1.setString(1, jLabelAcqYear.getText());
                        pst1.setString(2, jLabelAcqSerial.getText());
                        pst1.setString(3, jLabelAcqRefNum.getText());

                    }

                    pst1.setString(4, String.valueOf(itmNum));
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
                    pst1.setString(18, "1");
                    pst1.setString(19, regDocVersion);
                    pst1.setString(20, "Q");
                    pst1.setString(21, wkNum);

                    pst1.executeUpdate();
                    itmNum = itmNum + 1;

                }

                if (!("N".equals(fileDel1))) {
                    if (("Y".equals(filechange)) && ("Y".equals(startSym1))) {
                        imgSave1();
                    } else if (("Y".equals(filechange)) && ("N".equals(startSym1)) && ("N".equals(fileDel1))) {
                        imgInsert1();
                    }
                }

                if (("N".equals(fileDel1))) {
                    if (("Y".equals(filechange)) && ("Y".equals(startSym1))) {
                        imgSave1();
                    } else if (("Y".equals(filechange)) && ("N".equals(startSym1)) && ("N".equals(fileDel1))) {
                        imgInsert1();
                    }
                }

                if (!("N".equals(fileDel2))) {
                    if (("Y".equals(filechange)) && ("Y".equals(startSym2))) {
                        imgSave2();
                    } else if (("Y".equals(filechange)) && ("N".equals(startSym2)) && ("N".equals(fileDel2))) {
                        imgInsert2();
                    }
                }

                if (("N".equals(fileDel2))) {
                    if (("Y".equals(filechange)) && ("Y".equals(startSym2))) {
                        imgSave2();
                    } else if (("Y".equals(filechange)) && ("N".equals(startSym2)) && ("N".equals(fileDel2))) {
                        imgInsert2();
                    }
                }

                if (!("N".equals(fileDel3))) {
                    if (("Y".equals(filechange)) && ("Y".equals(startSym3))) {
                        imgSave3();
                    } else if (("Y".equals(filechange)) && ("N".equals(startSym3)) && ("N".equals(fileDel3))) {
                        imgInsert3();
                    }
                }

                if (("N".equals(fileDel3))) {
                    if (("Y".equals(filechange)) && ("Y".equals(startSym3))) {
                        imgSave3();
                    } else if (("Y".equals(filechange)) && ("N".equals(startSym3)) && ("N".equals(fileDel3))) {
                        imgInsert3();
                    }
                }

                if (("N".equals(fileDel4))) {
                    if (("Y".equals(filechange)) && ("Y".equals(startSym4))) {
                        imgSave4();
                    } else if (("Y".equals(filechange)) && ("N".equals(startSym4)) && ("N".equals(fileDel4))) {
                        imgInsert4();
                    }
                }

                if (("N".equals(fileDel5))) {
                    if (("Y".equals(filechange)) && ("Y".equals(startSym5))) {
                        imgSave5();
                    } else if (("Y".equals(filechange)) && ("N".equals(startSym5)) && ("N".equals(fileDel5))) {
                        imgInsert5();
                    }
                }

                createAction();

                createActionUpd();

                wkClearedUpdate();

                createReport();

                if ("S".equals(jLabelSerial.getText())) {
                    actRef = jLabelSerial.getText() + " " + jTextAcqRegNum.getText();

                } else {
                    actRef = jLabelAcqSerial.getText() + " " + jLabelAcqRefNum.getText();
                }

                if (!("".equals(jLabelAcqPayBack.getText()))) {
                    chgBank();
                }
                System.out.println("fileTripChange " + fileTripChange);
                if (("Y".equals(fileTripChange)) || ("Y".equals(fileTripDel))) {
                    createETrip();
                }

                sendMail();
                jDialogWaitingEmail.setVisible(false);

                JOptionPane.showMessageDialog(null, "Acquttal for perdiem  "
                        + actRef + " has been modified successfully  ");

                new JFrameMain(jLabelEmp.getText()).setVisible(true);
                setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    void fetchImgCount() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();

            String imgCount = "";
            if ("S".equals(jLabelSerial.getText())) {
                imgCount = jLabelSerial.getText() + jTextAcqRegNum.getText();
            } else {
                imgCount = jLabelAcqSerial.getText() + jLabelAcqRefNum.getText();
            }
            st.executeQuery("SELECT  max(IMG_VERSION)  FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[DocAttachTab] "
                    + "where concat(SERIAL,REF_NUM)="
                    + "'" + imgCount + "'");

            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                oldImgAttVer = rs.getInt(1);
            }

            imgDisplayFile1();
            imgDisplayFile2();
            imgDisplayFile3();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void imgCount() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            String imgCount = "";

            if ("S".equals(jLabelSerial.getText())) {
                imgCount = jLabelSerial.getText() + jTextAcqRegNum.getText();
            } else {
                imgCount = jLabelAcqSerial.getText() + jLabelAcqRefNum.getText();
            }

            st.executeQuery("SELECT count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[DocAttachTab] "
                    + "where concat(SERIAL,REF_NUM)="
                    + "'" + imgCount + "'");

            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                imgAttCount = rs.getInt(1);
            }

            if (imgAttCount > 0) {
                st1.executeQuery("SELECT max(IMG_VERSION)+1, max(IMG_VERSION)  FROM [ClaimsAppSysZvandiri].[dbo].[DocAttachTab] "
                        + "where concat(SERIAL,REF_NUM)="
                        + "'" + imgCount + "'");

                ResultSet rs1 = st1.getResultSet();

                while (rs1.next()) {
                    imgAttVer = rs1.getString(1);

                }
            } else {
                imgAttVer = "1";
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void imgInsert1() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            String imgInsFile1 = "";

            if ("S".equals(jLabelSerial.getText())) {
                imgInsFile1 = jLabelSerial.getText() + jTextAcqRegNum.getText();
            } else {
                imgInsFile1 = jLabelAcqSerial.getText() + jLabelAcqRefNum.getText();
            }

            String sqlInsAttDoc = " INSERT INTO [ClaimsAppSysZvandiri].[dbo].[DocAttachTab]"
                    + "select [DOC_ATT_NUM],[SERIAL],[REF_NUM],[ATT_IMAGE],"
                    + "'" + imgAttVer + "' FROM [ClaimsAppSysZvandiri].[dbo].[DocAttachTab] "
                    + "where concat(SERIAL,REF_NUM)= "
                    + "'" + imgInsFile1 + "' "
                    + "and DOC_ATT_NUM = 1 and IMG_VERSION ='" + oldImgAttVer + "' ";

            pst = conn.prepareStatement(sqlInsAttDoc);

            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void imgInsert2() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            String imgInsFile2 = "";

            if ("S".equals(jLabelSerial.getText())) {
                imgInsFile2 = jLabelSerial.getText() + jTextAcqRegNum.getText();
            } else {
                imgInsFile2 = jLabelAcqSerial.getText() + jLabelAcqRefNum.getText();
            }

            String sqlInsAttDoc = " INSERT INTO [ClaimsAppSysZvandiri].[dbo].[DocAttachTab]"
                    + "select [DOC_ATT_NUM],[SERIAL],[REF_NUM],[ATT_IMAGE],"
                    + "'" + imgAttVer + "' FROM [ClaimsAppSysZvandiri].[dbo].[DocAttachTab] "
                    + "where concat(SERIAL,REF_NUM)= "
                    + "'" + imgInsFile2
                    + "' and DOC_ATT_NUM = 2 and IMG_VERSION ='" + oldImgAttVer + "' ";

            pst = conn.prepareStatement(sqlInsAttDoc);

            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void imgInsert3() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            String imgInsFile3 = "";

            if ("S".equals(jLabelSerial.getText())) {
                imgInsFile3 = jLabelSerial.getText() + jTextAcqRegNum.getText();
            } else {
                imgInsFile3 = jLabelAcqSerial.getText() + jLabelAcqRefNum.getText();
            }

            String sqlInsAttDoc = " INSERT INTO [ClaimsAppSysZvandiri].[dbo].[DocAttachTab]"
                    + "select [DOC_ATT_NUM],[SERIAL],[REF_NUM],[ATT_IMAGE],"
                    + "'" + imgAttVer + "' FROM [ClaimsAppSysZvandiri].[dbo].[DocAttachTab] "
                    + "where concat(SERIAL,REF_NUM)= "
                    + "'" + imgInsFile3
                    + "' and DOC_ATT_NUM = 3 and IMG_VERSION ='" + oldImgAttVer + "' ";

            pst = conn.prepareStatement(sqlInsAttDoc);

            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void imgInsert4() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            String imgInsFile4 = "";

            if ("S".equals(jLabelSerial.getText())) {
                imgInsFile4 = jLabelSerial.getText() + jTextAcqRegNum.getText();
            } else {
                imgInsFile4 = jLabelAcqSerial.getText() + jLabelAcqRefNum.getText();
            }

            String sqlInsAttDoc = " INSERT INTO [ClaimsAppSysZvandiri].[dbo].[DocAttachTab]"
                    + "select [DOC_ATT_NUM],[SERIAL],[REF_NUM],[ATT_IMAGE],"
                    + "'" + imgAttVer + "' FROM [ClaimsAppSysZvandiri].[dbo].[DocAttachTab] "
                    + "where concat(SERIAL,REF_NUM)= "
                    + "'" + imgInsFile4 + "'"
                    + "' and DOC_ATT_NUM = 4 and IMG_VERSION ='" + oldImgAttVer + "' ";

            pst = conn.prepareStatement(sqlInsAttDoc);

            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void imgInsert5() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            String imgInsFile5 = "";

            if ("S".equals(jLabelSerial.getText())) {
                imgInsFile5 = jLabelSerial.getText() + jTextAcqRegNum.getText();
            } else {
                imgInsFile5 = jLabelAcqSerial.getText() + jLabelAcqRefNum.getText();
            }

            String sqlInsAttDoc = " INSERT INTO [ClaimsAppSysZvandiri].[dbo].[DocAttachTab]"
                    + "select [DOC_ATT_NUM],[SERIAL],[REF_NUM],[ATT_IMAGE],"
                    + "'" + imgAttVer + "' FROM [ClaimsAppSysZvandiri].[dbo].[DocAttachTab] "
                    + "where concat(SERIAL,REF_NUM)= "
                    + "'" + imgInsFile5
                    + "' and DOC_ATT_NUM = 5 and IMG_VERSION ='" + oldImgAttVer + "' ";

            pst = conn.prepareStatement(sqlInsAttDoc);

            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void imgSave1() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            if (!("".equals(jLabelImgFile1.getIcon().toString()))) {
                Statement st = conn.createStatement();
                Statement st1 = conn.createStatement();

                String sqlAttDoc = " INSERT INTO [ClaimsAppSysZvandiri].[dbo].[DocAttachTab] (DOC_ATT_NUM,SERIAL,REF_NUM,ATT_IMAGE,IMG_VERSION)"
                        + "VALUES (?,?,?,?,?)";

                pst = conn.prepareStatement(sqlAttDoc);

                pst.setString(1, "1");
                if ("S".equals(jLabelSerial.getText())) {
                    pst.setString(2, jLabelSerial.getText());
                    pst.setString(3, String.valueOf(jTextAcqRegNum.getText()));
                } else {
                    pst.setString(2, jLabelAcqSerial.getText());
                    pst.setString(3, String.valueOf(jLabelAcqRefNum.getText()));
                }
                pst.setBytes(4, person_image);
                pst.setString(5, imgAttVer);

                pst.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void imgSave2() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            if (!("".equals(jLabelImgFile2.getIcon().toString()))) {
                Statement st = conn.createStatement();
                Statement st1 = conn.createStatement();

                String sqlAttDoc = " INSERT INTO [ClaimsAppSysZvandiri].[dbo].[DocAttachTab] (DOC_ATT_NUM,SERIAL,REF_NUM,ATT_IMAGE,IMG_VERSION)"
                        + "VALUES (?,?,?,?,?)";

                pst = conn.prepareStatement(sqlAttDoc);

                pst.setString(1, "2");
                if ("S".equals(jLabelSerial.getText())) {
                    pst.setString(2, jLabelSerial.getText());
                    pst.setString(3, String.valueOf(jTextAcqRegNum.getText()));
                } else {
                    pst.setString(2, jLabelAcqSerial.getText());
                    pst.setString(3, String.valueOf(jLabelAcqRefNum.getText()));
                }
                pst.setBytes(4, person_image2);
                pst.setString(5, imgAttVer);

                pst.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void imgSave3() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            if (!("".equals(jLabelImgFile3.getIcon().toString()))) {
                Statement st = conn.createStatement();
                Statement st1 = conn.createStatement();

                String sqlAttDoc = " INSERT INTO [ClaimsAppSysZvandiri].[dbo].[DocAttachTab] (DOC_ATT_NUM,SERIAL,REF_NUM,ATT_IMAGE,IMG_VERSION)"
                        + "VALUES (?,?,?,?,?)";

                pst = conn.prepareStatement(sqlAttDoc);

                pst.setString(1, "3");
                if ("S".equals(jLabelSerial.getText())) {
                    pst.setString(2, jLabelSerial.getText());
                    pst.setString(3, String.valueOf(jTextAcqRegNum.getText()));
                } else {
                    pst.setString(2, jLabelAcqSerial.getText());
                    pst.setString(3, String.valueOf(jLabelAcqRefNum.getText()));
                }
                pst.setBytes(4, person_image3);
                pst.setString(5, imgAttVer);

                pst.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void imgSave4() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            if (!("".equals(jLabelImgFile4.getIcon().toString()))) {
                String sqlAttDoc = " INSERT INTO [ClaimsAppSysZvandiri].[dbo].[DocAttachTab] (DOC_ATT_NUM,SERIAL,REF_NUM,ATT_IMAGE,IMG_VERSION)"
                        + "VALUES (?,?,?,?,?)";

                pst = conn.prepareStatement(sqlAttDoc);

                pst.setString(1, "4");
                pst.setString(2, jLabelAcqSerial.getText());
                pst.setString(3, String.valueOf(jTextAcqRegNum.getText()));
                pst.setBytes(4, person_image4);
                pst.setString(5, "1");

                pst.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void imgSave5() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            if (!("".equals(jLabelImgFile5.getIcon().toString()))) {
                String sqlAttDoc = " INSERT INTO [ClaimsAppSysZvandiri].[dbo].[DocAttachTab] (DOC_ATT_NUM,SERIAL,REF_NUM,ATT_IMAGE,IMG_VERSION)"
                        + "VALUES (?,?,?,?,?)";

                pst = conn.prepareStatement(sqlAttDoc);

                pst.setString(1, "5");
                pst.setString(2, jLabelAcqSerial.getText());
                pst.setString(3, String.valueOf(jTextAcqRegNum.getText()));
                pst.setBytes(4, person_image5);
                pst.setString(5, "1");

                pst.executeUpdate();
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
                Statement st = conn.createStatement();

                st.executeQuery("SELECT count(*) FROM [ClaimsAppSysZvandiri].[dbo].[SerialLock] where SERIAL = 'A'");

                ResultSet rs = st.getResultSet();

                while (rs.next()) {
                    count = Integer.parseInt(rs.getString(1));

                }
                conn.close();

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

    void deleteLongLock() {
        try {

            String sqlDeleteLock = "delete [ClaimsAppSysZvandiri].[dbo].[SerialLock] where SERIAL = "
                    + "'A'";

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
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

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT REF_NUM + 1,SERIAL FROM [ClaimsAppSysZvandiri].[dbo].[refNumTab] where REF_YEAR ='" + finyear + "' and SERIAL = 'A'");

            ResultSet rs = st.getResultSet();
            jLabelAcqYear.setText(Integer.toString(finyear));
            while (rs.next()) {

                jLabelAcqSerial.setText(rs.getString(2));
                jLabelAcqRefNum.setText(rs.getString(1));
            }

            pst = conn.prepareStatement(sqlSerialLock);

            pst.setString(1, jLabelAcqSerial.getText());
            pst.setString(2, jLabelAcqRefNum.getText());
            pst.setString(3, jLabelGenLogNam.getText());

            pst.executeUpdate();

            String sqlSerialLockUpdate = "update [ClaimsAppSysZvandiri].[dbo].[SerialLock] set LCK_DATE_TIME = ( SELECT SYSDATETIME()) where "
                    + "concat(SERIAL,REF_NUM) = '" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "' ";

            pst1 = conn.prepareStatement(sqlSerialLockUpdate);
            pst1.executeUpdate();

            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    void budgetPOP() {
        jDialogBudget.setVisible(true);
        jDialogBudget.setVisible(false);
        jDialogBudget.setVisible(true);
    }

    void facDist() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st1 = conn.createStatement();

            st1.executeQuery("SELECT DIS_BASE FROM [ClaimsAppSysZvandiri].[dbo].[DistFacTab] "
                    + "where province = '" + jComboProvinceFacility.getSelectedItem().toString() + "' "
                    + "and district = '" + jComboDistrictFacility.getSelectedItem().toString() + "' "
                    + "and Facility = '" + jComboFacility.getSelectedItem().toString() + "'");

            ResultSet r1 = st1.getResultSet();

            if ((jComboDistrictFacility.getSelectedItem().toString().equals(jComboFacility.getSelectedItem().toString()))) {
                jLabelKMDisDB.setVisible(false);
                jTextDistDest.setVisible(true);
                jTextDistDest.setText("");
                // jTextDistDest.setEditable(true);
                jLabelKMDisDB.setText(jTextDistDest.getText());
            } else {
                jTextDistDest.setVisible(false);
                jTextDistDest.setText("");
                jLabelKMDis.setVisible(true);
                // jTextDistDest.setEditable(false);
                while (r1.next()) {
                    int DisBase = r1.getInt(1);
//                   jTextDistDest.setText(Integer.toString(DisBase));
                    jLabelKMDisDB.setText(Integer.toString(DisBase));
                    jLabelKMDisDB.setVisible(false);
                    jLabelKMDis.setText(Integer.toString(DisBase));

                }
            }

        } catch (Exception e) {

        }
    }

    void findProvince() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

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

    void createReport() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            String sqlwkUpdateQP = "update [ClaimsAppSysZvandiri].[dbo].[ClaimTripReport] set "
                    + "ACT_REC_STA ='P' where "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "'"
                    + "and PLAN_WK =" + wkNum + " and  ACT_REC_STA = 'Q'";
            pst = conn.prepareStatement(sqlwkUpdateQP);
            pst.executeUpdate();

            String sqlRepCreate = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimTripReport] "
                    + "(PREV_SERIAL,PREV_REF_NUM,SERIAL,REF_NUM,NAM_TRAVEL,NAM_VISITED,TRIP_BRIEF,PLAN_WK,DOC_VER,REG_MOD_VER,ACT_REC_STA)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

            pst1 = conn.prepareStatement(sqlRepCreate);

            pst1.setString(1, jLabelSerial.getText());
            pst1.setString(2, jTextAcqRegNum.getText());
            pst1.setString(3, jLabelAcqSerial.getText());
            pst1.setString(4, jLabelAcqRefNum.getText());
            pst1.setString(5, jTextAreaNamTravel.getText());
            pst1.setString(6, jTextAreaNamVisited.getText());
            pst1.setString(7, jTextAreaTripReport.getText());
            pst1.setString(8, wkNum);
            pst1.setString(9, "1");
            pst1.setString(10, regDocVersion);
            pst1.setString(11, "Q");
            pst1.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void createAction() {

        try {

            String sqlcreate = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] "
                    + "(YEAR,SERIAL,REF_NUM,USR_ACTION, DOC_STATUS,ACTIONED_BY, "
                    + "SEND_TO, ACTIONED_ON_DATE, ACTIONED_ON_TIME, DOC_VER,"
                    + " ACTION_VER,ACTIONED_ON_COMPUTER,AUTH_NAM,AUTH_ALL_NAM,REG_MOD_VER,ACT_REC_STA,SPECIAL_REASON)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

//            String createRef = "";
//            String createAct = "";
            if ("S".equals(jLabelSerial.getText())) {
                createRef = jTextAcqRegNum.getText();
                createAct = "S";
            } else {
                createRef = jLabelAcqRefNum.getText();
                createAct = "A";
            }

            String actionCode = "Acquittal Created";
            String statusCode = "AcqRegistered";
            supName();

            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            pst1 = conn.prepareStatement(sqlcreate);

            if ("S".equals(jLabelSerial.getText())) {
                pst1.setString(1, jLabelAcqYear.getText());
                pst1.setString(2, jLabelSerial.getText());
                pst1.setString(3, jTextAcqRegNum.getText());

            } else {
                pst1.setString(1, jLabelAcqYear.getText());
                pst1.setString(2, jLabelAcqSerial.getText());
                pst1.setString(3, jLabelAcqRefNum.getText());

            }

            pst1.setString(4, actionCode);
            pst1.setString(5, statusCode);
            pst1.setString(6, createUsrNam);
            pst1.setString(7, sendTo);
            pst1.setString(8, jLabelGenDate.getText());
            pst1.setString(9, jLabelGenTime.getText());
            pst1.setString(10, "1");
            pst1.setString(11, actVersion);
            pst1.setString(12, hostName);
//            pst1.setString(13, jLabelAuthNam1.getText());
//            pst1.setString(14, jLabelAuthNam2.getText());
            pst1.setString(15, regDocVersion);
            pst1.setString(16, "Q");

            if ("S".equals(jLabelSerial.getText())) {
//                pst1.setString(17, jTextAreaSpecialReason.getText());
            } else {
                pst1.setString(17, "");
            }
            pst1.executeUpdate();

            String sqlUpdate = "UPDATE [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] set ACT_REC_STA ='QM' where "
                    + "REF_NUM='" + createRef + "' and REG_MOD_VER ='" + oldRegDocVersion + "'"
                    + " and SERIAL ='" + createAct + "'";

            String sqlWFUpdate = "UPDATE [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] set ACT_REC_STA ='QM' where "
                    + "REF_NUM='" + createRef + "' and REG_MOD_VER ='" + oldRegDocVersion + "' "
                    + "and SERIAL ='" + createAct + "'";

            String sqlItmUpdate = "UPDATE [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] set ACT_REC_STA ='QM' where "
                    + "REF_NUM='" + createRef + "' and REG_MOD_VER ='" + oldRegDocVersion + "' "
                    + "and SERIAL ='" + createAct + "'";

            pst = conn.prepareStatement(sqlUpdate);
            pst.executeUpdate();

            pst = conn.prepareStatement(sqlItmUpdate);
            pst.executeUpdate();

            pst = conn.prepareStatement(sqlWFUpdate);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    void createETrip() {
//        try {
//            itmNum = 1;
//            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
//                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
//            System.out.println("ffii " + createRef + " " + oldRegDocVersion + " " + createAct);
////
////            String sqlUpdate = "UPDATE [ClaimsAppSysZvandiri].[dbo].[ClaimTripMilageTab] set ACT_REC_STA ='QM' where "
////                    + "REF_NUM='" + createRef + "' and SERIAL ='" + createAct + "'";
////
////            pst1 = conn.prepareStatement(sqlUpdate);
////            pst1.executeUpdate();
//
//            for (int i = 0; i < jTableTripDetails.getRowCount(); i++) {
//                System.out.println("ttts vscgg " + itmNum);
//                String sqlCreateETrip = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimTripMilageTab] "
//                        + " VALUES (?,?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?)";
//
//             
//
//                pst = conn.prepareStatement(sqlCreateETrip);
//
//                pst.setString(1, jLabelAcqYear.getText());
//                pst.setString(2, jLabelSerial.getText());
//                pst.setString(3, jTextAcqRegNum.getText());
//                pst.setString(4, jTableTripDetails.getValueAt(i, 0).toString());
//                pst.setString(5, jTableTripDetails.getValueAt(i, 1).toString());
//                pst.setString(6, jTableTripDetails.getValueAt(i, 2).toString());
//                pst.setString(7, jTableTripDetails.getValueAt(i, 3).toString());
//                pst.setString(8, jTableTripDetails.getValueAt(i, 4).toString());
//                pst.setString(9, jTableTripDetails.getValueAt(i, 5).toString());
//                pst.setString(10, jTableTripDetails.getValueAt(i, 6).toString());
//                pst.setString(11, jTableTripDetails.getValueAt(i, 7).toString());
//                pst.setString(12, jTableTripDetails.getValueAt(i, 8).toString());
//                pst.setString(13, jTableTripDetails.getValueAt(i, 9).toString());
//                pst.setString(14, jTableTripDetails.getValueAt(i, 10).toString());
//                pst.setString(15, jTableTripDetails.getValueAt(i, 11).toString());
//                pst.setString(16, wkNum);
//                pst.setString(17, regDocVersion);
//                pst.setString(18, "1");
//                pst.setString(19, "Q");
//
//                pst.executeUpdate();
//                itmNum = itmNum + 1;
//
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
    void createETrip() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            String sqlUpdate = "UPDATE [ClaimsAppSysZvandiri].[dbo].[ClaimTripMilageTab] set ACT_REC_STA ='QM' where "
                    + "REF_NUM='" + createRef + "' and SERIAL ='" + createAct + "'";

            pst = conn.prepareStatement(sqlUpdate);
            pst.executeUpdate();

            for (int i = 0; i < jTableTripDetails.getRowCount(); i++) {

                String sqlCreateETrip = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimTripMilageTab] "
                        + " VALUES (?,?, ?,?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?,?,?,?,?,?)";

                pst = conn.prepareStatement(sqlCreateETrip);

                pst.setString(1, jLabelAcqYear.getText());
                pst.setString(2, jLabelAcqSerial.getText());
                pst.setString(3, jLabelAcqRefNum.getText());
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
                pst.setString(17, wkNum);
                pst.setString(18, regDocVersion);
                pst.setString(19, "1");
                pst.setString(20, "Q");

                pst.executeUpdate();
                itmNum = itmNum + 1;

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void createActionUpd() {

        try {

            if ("S".equals(jLabelSerial.getText())) {
                createRef = jTextAcqRegNum.getText();
                createAct = "S";
            } else {
                createRef = jLabelAcqRefNum.getText();
                createAct = "A";
            }

            String actionCode = "Acquittal Created";
            String statusCode = "AcqRegistered";
            supName();

            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            String sqlUpdate = "UPDATE [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] set ACT_REC_STA ='QM' where "
                    + "REF_NUM='" + createRef + "' and REG_MOD_VER ='" + oldRegDocVersion + "'"
                    + " and SERIAL ='" + createAct + "'";

            String sqlWFUpdate = "UPDATE [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] set ACT_REC_STA ='QM' where "
                    + "REF_NUM='" + createRef + "' and REG_MOD_VER ='" + oldRegDocVersion + "' "
                    + "and SERIAL ='" + createAct + "'";

            String sqlItmUpdate = "UPDATE [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] set ACT_REC_STA ='QM' where "
                    + "REF_NUM='" + createRef + "' and REG_MOD_VER ='" + oldRegDocVersion + "' "
                    + "and SERIAL ='" + createAct + "'";

            pst = conn.prepareStatement(sqlUpdate);
            pst.executeUpdate();

            pst = conn.prepareStatement(sqlItmUpdate);
            pst.executeUpdate();

            pst = conn.prepareStatement(sqlWFUpdate);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void wkClearedUpdate() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            String sqlwkUpdateQP = "update [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab] set "
                    + "ACQ_STA ='P' where "
                    + "concat(SERIAL,REF_NUM) ='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "'"
                    + "and PLAN_WK =" + wkNum + " and  ACQ_STA = 'C'";
            pst = conn.prepareStatement(sqlwkUpdateQP);
            pst.executeUpdate();

            String sqlWkCleared = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab] "
                    + "(PREV_SERIAL,PREV_REF_NUM,SERIAL,REF_NUM,PLAN_WK,CLEARED_AMT,REQ_STA,ACQ_STA,DOC_VER)"
                    + " VALUES (?,?,?,?,?,?,?,?,?)";

            pst1 = conn.prepareStatement(sqlWkCleared);

            pst1.setString(1, jLabelSerial.getText());
            pst1.setString(2, jTextAcqRegNum.getText());
            pst1.setString(3, jLabelAcqSerial.getText());
            pst1.setString(4, jLabelAcqRefNum.getText());
            pst1.setString(5, wkNum);
            pst1.setString(6, jLabelAcqAppTotReqCostCleared.getText());
            pst1.setString(7, "R");
            pst1.setString(8, "C");
            pst1.setString(9, docVerClearNew);
            pst1.executeUpdate();
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
        jDialogWaitingEmail = new javax.swing.JDialog();
        buttonGroupSpecial = new javax.swing.ButtonGroup();
        jDialogFacility = new javax.swing.JDialog();
        jPanel11 = new javax.swing.JPanel();
        jLabelHeader1 = new javax.swing.JLabel();
        jButtonOkFacility = new javax.swing.JButton();
        jButtonCancelFacility = new javax.swing.JButton();
        jLabelProvinceFacility = new javax.swing.JLabel();
        jComboProvinceFacility = new javax.swing.JComboBox<>();
        jComboDistrictFacility = new javax.swing.JComboBox<>();
        jLabelDistrictFacility = new javax.swing.JLabel();
        jLabelFacility = new javax.swing.JLabel();
        jComboFacility = new javax.swing.JComboBox<>();
        jLabelDisFacilty = new javax.swing.JLabel();
        jLabelDisProvince = new javax.swing.JLabel();
        jLabelDisDistrict = new javax.swing.JLabel();
        jLabelFacDist = new javax.swing.JLabel();
        jDialogWkDisplay = new javax.swing.JDialog();
        jPanel12 = new javax.swing.JPanel();
        jLabelBankHeader1 = new javax.swing.JLabel();
        jLabelBankMsg1 = new javax.swing.JLabel();
        jButtonBankOk1 = new javax.swing.JButton();
        jButtonBankCancel1 = new javax.swing.JButton();
        jComboWk = new javax.swing.JComboBox<>();
        jLabelBankAccNo1 = new javax.swing.JLabel();
        jDialogBudget = new javax.swing.JDialog();
        jPanel13 = new javax.swing.JPanel();
        jLabelHeader2 = new javax.swing.JLabel();
        jButtonOkFacility1 = new javax.swing.JButton();
        jButtonCancelBudget = new javax.swing.JButton();
        jLabelBudgetCode1 = new javax.swing.JLabel();
        jComboBudgetCode = new javax.swing.JComboBox<>();
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
        jLabelBudgetCode = new javax.swing.JLabel();
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
        jLabelAcqAppTotReqCost = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabelAppTotReq = new javax.swing.JLabel();
        jLabelOffice = new javax.swing.JLabel();
        jLabelAcqAccNum = new javax.swing.JLabel();
        jLabelACC = new javax.swing.JLabel();
        jLabelAcqActMainPurpose = new javax.swing.JLabel();
        jLabelAcqProvince = new javax.swing.JLabel();
        jLabelAcqOffice = new javax.swing.JLabel();
        jLabelAcqBudCode = new javax.swing.JLabel();
        jLabelAcqEmpNum = new javax.swing.JLabel();
        jLabelAcqRefNum = new javax.swing.JLabel();
        jLabelAcqYear = new javax.swing.JLabel();
        jLabelNum1 = new javax.swing.JLabel();
        jLabelAcqSerial = new javax.swing.JLabel();
        jLabelEmp = new javax.swing.JLabel();
        jLabelAcqPayBack = new javax.swing.JLabel();
        jLabelAcqAmtPayBack = new javax.swing.JLabel();
        jLabelPay = new javax.swing.JLabel();
        jLabelSerial = new javax.swing.JLabel();
        jLabelAcqDateAcq = new javax.swing.JLabel();
        jLabelNum = new javax.swing.JLabel();
        jRadioSpecial = new javax.swing.JRadioButton();
        jRadioNormal = new javax.swing.JRadioButton();
        jLabelRemain = new javax.swing.JLabel();
        jLabelSpecial = new javax.swing.JLabel();
        jLabelRegDateAcq = new javax.swing.JLabel();
        jTextAcqRegNum = new javax.swing.JTextField();
        jLabelAppTotCleared = new javax.swing.JLabel();
        jLabelAcqAppTotReqCostCleared = new javax.swing.JLabel();
        jPanelRequest = new javax.swing.JPanel();
        jLabelLogo1 = new javax.swing.JLabel();
        jLabelHeaderLine = new javax.swing.JLabel();
        jLabelLineDate = new javax.swing.JLabel();
        jLabelLineTime = new javax.swing.JLabel();
        jLabellogged = new javax.swing.JLabel();
        jLabelLineLogNam = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableActivityReq = new javax.swing.JTable();
        jLabelSpecial1 = new javax.swing.JLabel();
        jPanelAcquittal = new javax.swing.JPanel();
        jLabelLogo2 = new javax.swing.JLabel();
        jLabelHeaderLine1 = new javax.swing.JLabel();
        jLabelLineDate1 = new javax.swing.JLabel();
        jLabelLineTime1 = new javax.swing.JLabel();
        jLabellogged1 = new javax.swing.JLabel();
        jLabelLineLogNam1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableActivityAcq = new javax.swing.JTable();
        jPanelCaptureDetails = new javax.swing.JPanel();
        jLabelDestination = new javax.swing.JLabel();
        jDateChooserActivityAcq = new com.toedter.calendar.JDateChooser();
        jLabelActDate = new javax.swing.JLabel();
        jTextDistDest = new javax.swing.JTextField();
        jLabelDistDest = new javax.swing.JLabel();
        addition = new javax.swing.JLabel();
        jTextAcqDestination = new javax.swing.JTextField();
        jLabelKm = new javax.swing.JLabel();
        jLabelKMDisDB = new javax.swing.JLabel();
        jLabelKMDis = new javax.swing.JLabel();
        jLabelWk1DialogBudget = new javax.swing.JLabel();
        jTextFieldBudget = new javax.swing.JTextField();
        jLabelActLinePurpose = new javax.swing.JLabel();
        jTextPurpose = new javax.swing.JTextField();
        jCheckBreakfast = new javax.swing.JCheckBox();
        jLabelBreakfast1 = new javax.swing.JLabel();
        jTextAcqBreakfast = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jRadioLunch3 = new javax.swing.JRadioButton();
        jRadioLunch10 = new javax.swing.JRadioButton();
        jRadioLunch5 = new javax.swing.JRadioButton();
        jTextAcqLunch = new javax.swing.JTextField();
        jLabelBreakfast2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jCheckDinner = new javax.swing.JCheckBox();
        jCheckIncidental = new javax.swing.JCheckBox();
        jLabelBreakfast3 = new javax.swing.JLabel();
        jTextAcqDinner = new javax.swing.JTextField();
        jTextAcqIncidental = new javax.swing.JTextField();
        jLabelBreakfast = new javax.swing.JLabel();
        jTextAccUnprovedAcq = new javax.swing.JTextField();
        jTextAccProvedAcq = new javax.swing.JTextField();
        jTextMiscAmtAcq = new javax.swing.JTextField();
        jButtonDeleted = new javax.swing.JButton();
        jLabelSubAcc = new javax.swing.JLabel();
        jLabelProved = new javax.swing.JLabel();
        jLabelUnproved = new javax.swing.JLabel();
        jRadioAccUnproved = new javax.swing.JRadioButton();
        jRadioAccProved = new javax.swing.JRadioButton();
        jLabelMisc = new javax.swing.JLabel();
        jTextMiscActivityAcq = new javax.swing.JTextField();
        jButtonAddActivity = new javax.swing.JButton();
        jCheckReturnSameDay = new javax.swing.JCheckBox();
        jButtonBnkUpd = new javax.swing.JButton();
        jLabelSpecial2 = new javax.swing.JLabel();
        jLabelDistrict = new javax.swing.JLabel();
        jTabbedPaneAcqAtt = new javax.swing.JTabbedPane();
        jPanelReport = new javax.swing.JPanel();
        jPanelReportDetails = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextAreaTripReport = new javax.swing.JTextArea();
        jLabelTripReport = new javax.swing.JLabel();
        jLabelNamVisited = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTextAreaNamVisited = new javax.swing.JTextArea();
        jLabelNamTravel = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTextAreaNamTravel = new javax.swing.JTextArea();
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
        jButtonDeleteDetails = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButtonGetDetails = new javax.swing.JButton();
        jLabelLogo8 = new javax.swing.JLabel();
        jLabelHeaderLine3 = new javax.swing.JLabel();
        jLabelLineDate3 = new javax.swing.JLabel();
        jLabelLineTime3 = new javax.swing.JLabel();
        jLabellogged3 = new javax.swing.JLabel();
        jLabelLineLogNam10 = new javax.swing.JLabel();
        jLabelAcqWk4 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
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
        jLabelSpecial3 = new javax.swing.JLabel();
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
        jPanelDocAttach4 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jButtonDeleteAtt4 = new javax.swing.JButton();
        jButtonChooseAtt4 = new javax.swing.JButton();
        jLabelLogo6 = new javax.swing.JLabel();
        jLabelHeaderLine7 = new javax.swing.JLabel();
        jLabelLineDate8 = new javax.swing.JLabel();
        jLabelLineTime8 = new javax.swing.JLabel();
        jLabellogged7 = new javax.swing.JLabel();
        jLabelLineLogNam9 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jPanel15 = new javax.swing.JPanel();
        jLabelImgFile4 = new javax.swing.JLabel();
        jPanelDocAttach5 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jButtonDeleteAtt5 = new javax.swing.JButton();
        jButtonChooseAtt5 = new javax.swing.JButton();
        jLabelLogo7 = new javax.swing.JLabel();
        jLabelHeaderLine6 = new javax.swing.JLabel();
        jLabelLineDate7 = new javax.swing.JLabel();
        jLabelLineTime7 = new javax.swing.JLabel();
        jLabellogged6 = new javax.swing.JLabel();
        jLabelLineLogNam8 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jPanel17 = new javax.swing.JPanel();
        jLabelImgFile5 = new javax.swing.JLabel();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuNew = new javax.swing.JMenu();
        jMenuItemPlanPerDiem = new javax.swing.JMenuItem();
        jSeparator13 = new javax.swing.JPopupMenu.Separator();
        jMenuItemPerDiemAcquittal = new javax.swing.JMenuItem();
        jSeparator17 = new javax.swing.JPopupMenu.Separator();
        jMenuEdit = new javax.swing.JMenu();
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

        jDialogChgPaid.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
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

        jDialogFacility.setLocation(new java.awt.Point(600, 300));
        jDialogFacility.setMinimumSize(new java.awt.Dimension(600, 300));
        jDialogFacility.setResizable(false);

        jPanel11.setBackground(new java.awt.Color(204, 153, 14));
        jPanel11.setLayout(null);

        jLabelHeader1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelHeader1.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeader1.setText("FACILITY TO VISIT");
        jPanel11.add(jLabelHeader1);
        jLabelHeader1.setBounds(170, 10, 230, 40);

        jButtonOkFacility.setText("Ok ");
        jButtonOkFacility.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkFacilityActionPerformed(evt);
            }
        });
        jPanel11.add(jButtonOkFacility);
        jButtonOkFacility.setBounds(180, 220, 80, 21);

        jButtonCancelFacility.setText("Cancel");
        jButtonCancelFacility.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelFacilityActionPerformed(evt);
            }
        });
        jPanel11.add(jButtonCancelFacility);
        jButtonCancelFacility.setBounds(300, 220, 80, 21);

        jLabelProvinceFacility.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelProvinceFacility.setText("Province");
        jPanel11.add(jLabelProvinceFacility);
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
        jPanel11.add(jComboProvinceFacility);
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
        jPanel11.add(jComboDistrictFacility);
        jComboDistrictFacility.setBounds(150, 110, 230, 30);

        jLabelDistrictFacility.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDistrictFacility.setText("District");
        jPanel11.add(jLabelDistrictFacility);
        jLabelDistrictFacility.setBounds(20, 110, 70, 30);

        jLabelFacility.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelFacility.setText("Facility");
        jPanel11.add(jLabelFacility);
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
        jPanel11.add(jComboFacility);
        jComboFacility.setBounds(150, 150, 320, 30);

        jLabelDisFacilty.setText("jLabel4");
        jPanel11.add(jLabelDisFacilty);
        jLabelDisFacilty.setBounds(150, 180, 400, 30);

        jLabelDisProvince.setText("jLabel4");
        jPanel11.add(jLabelDisProvince);
        jLabelDisProvince.setBounds(400, 70, 180, 30);

        jLabelDisDistrict.setText("jLabel4");
        jPanel11.add(jLabelDisDistrict);
        jLabelDisDistrict.setBounds(400, 110, 180, 30);

        jLabelFacDist.setText("jLabel4");
        jPanel11.add(jLabelFacDist);
        jLabelFacDist.setBounds(500, 156, 50, 20);

        javax.swing.GroupLayout jDialogFacilityLayout = new javax.swing.GroupLayout(jDialogFacility.getContentPane());
        jDialogFacility.getContentPane().setLayout(jDialogFacilityLayout);
        jDialogFacilityLayout.setHorizontalGroup(
            jDialogFacilityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
        );
        jDialogFacilityLayout.setVerticalGroup(
            jDialogFacilityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        jDialogWkDisplay.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jDialogWkDisplay.setLocation(new java.awt.Point(550, 250));
        jDialogWkDisplay.setMinimumSize(new java.awt.Dimension(290, 180));
        jDialogWkDisplay.setResizable(false);

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setMinimumSize(new java.awt.Dimension(290, 180));
        jPanel12.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPanel12FocusGained(evt);
            }
        });
        jPanel12.setLayout(null);

        jLabelBankHeader1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelBankHeader1.setForeground(new java.awt.Color(0, 0, 204));
        jLabelBankHeader1.setText("ACQUITTAL WEEK");
        jPanel12.add(jLabelBankHeader1);
        jLabelBankHeader1.setBounds(40, 0, 190, 30);

        jLabelBankMsg1.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabelBankMsg1.setText("Please select week to view");
        jPanel12.add(jLabelBankMsg1);
        jLabelBankMsg1.setBounds(60, 30, 160, 30);

        jButtonBankOk1.setText("Ok ");
        jButtonBankOk1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBankOk1ActionPerformed(evt);
            }
        });
        jPanel12.add(jButtonBankOk1);
        jButtonBankOk1.setBounds(30, 100, 80, 21);

        jButtonBankCancel1.setText("Cancel");
        jButtonBankCancel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBankCancel1ActionPerformed(evt);
            }
        });
        jPanel12.add(jButtonBankCancel1);
        jButtonBankCancel1.setBounds(150, 100, 80, 21);

        jComboWk.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jComboWk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboWkActionPerformed(evt);
            }
        });
        jPanel12.add(jComboWk);
        jComboWk.setBounds(30, 60, 210, 30);
        jPanel12.add(jLabelBankAccNo1);
        jLabelBankAccNo1.setBounds(200, 150, 280, 30);

        javax.swing.GroupLayout jDialogWkDisplayLayout = new javax.swing.GroupLayout(jDialogWkDisplay.getContentPane());
        jDialogWkDisplay.getContentPane().setLayout(jDialogWkDisplayLayout);
        jDialogWkDisplayLayout.setHorizontalGroup(
            jDialogWkDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jDialogWkDisplayLayout.setVerticalGroup(
            jDialogWkDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jDialogBudget.setAlwaysOnTop(true);
        jDialogBudget.setLocation(new java.awt.Point(450, 300));
        jDialogBudget.setMinimumSize(new java.awt.Dimension(600, 300));
        jDialogBudget.setResizable(false);

        jPanel13.setBackground(new java.awt.Color(204, 153, 14));
        jPanel13.setLayout(null);

        jLabelHeader2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelHeader2.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeader2.setText("ACTIVITY BUDGET LINE");
        jPanel13.add(jLabelHeader2);
        jLabelHeader2.setBounds(170, 10, 230, 40);

        jButtonOkFacility1.setText("Ok ");
        jButtonOkFacility1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkFacility1ActionPerformed(evt);
            }
        });
        jPanel13.add(jButtonOkFacility1);
        jButtonOkFacility1.setBounds(180, 200, 80, 21);

        jButtonCancelBudget.setText("Cancel");
        jButtonCancelBudget.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelBudgetActionPerformed(evt);
            }
        });
        jPanel13.add(jButtonCancelBudget);
        jButtonCancelBudget.setBounds(300, 200, 80, 21);

        jLabelBudgetCode1.setText("Budget Code");
        jPanel13.add(jLabelBudgetCode1);
        jLabelBudgetCode1.setBounds(5, 80, 80, 30);

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
        jPanel13.add(jComboBudgetCode);
        jComboBudgetCode.setBounds(5, 120, 590, 30);

        javax.swing.GroupLayout jDialogBudgetLayout = new javax.swing.GroupLayout(jDialogBudget.getContentPane());
        jDialogBudget.getContentPane().setLayout(jDialogBudgetLayout);
        jDialogBudgetLayout.setHorizontalGroup(
            jDialogBudgetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
        );
        jDialogBudgetLayout.setVerticalGroup(
            jDialogBudgetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PERDIEM ACQUITTAL -  MODIFICATION");
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
        jLabelGenDate.setBounds(1060, 0, 110, 30);

        jLabelGenTime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelGen.add(jLabelGenTime);
        jLabelGenTime.setBounds(1200, 0, 80, 30);

        jLabelGenLogNam1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam1.setText("Logged In");
        jPanelGen.add(jLabelGenLogNam1);
        jLabelGenLogNam1.setBounds(1060, 40, 100, 30);

        jLabelGenLogNam.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelGen.add(jLabelGenLogNam);
        jLabelGenLogNam.setBounds(1160, 40, 190, 30);

        jLabelEmpNum.setText("Employee Number");
        jPanelGen.add(jLabelEmpNum);
        jLabelEmpNum.setBounds(20, 200, 110, 30);

        jLabelAcqEmpNam.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqEmpNam);
        jLabelAcqEmpNam.setBounds(370, 200, 340, 30);
        jPanelGen.add(jSeparator1);
        jSeparator1.setBounds(10, 190, 1460, 2);

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
        jSeparator2.setBounds(10, 340, 1460, 2);

        jLabelAcqEmpTitle.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqEmpTitle);
        jLabelAcqEmpTitle.setBounds(750, 200, 400, 30);

        jLabel4.setText("Financial Details");
        jPanelGen.add(jLabel4);
        jLabel4.setBounds(20, 310, 110, 30);

        jLabelBudgetCode.setText("Budget Code");
        jPanelGen.add(jLabelBudgetCode);
        jLabelBudgetCode.setBounds(20, 350, 80, 30);

        jLabelActMainPurpose.setText("Activity Main Purpose");
        jPanelGen.add(jLabelActMainPurpose);
        jLabelActMainPurpose.setBounds(20, 410, 130, 30);

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
        jPanel1.setBounds(20, 450, 310, 150);

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
        jPanel3.setBounds(370, 450, 300, 150);

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
        jPanel4.setBounds(710, 450, 310, 150);

        jLabelAcqAppTotReqCost.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelAcqAppTotReqCost.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAcqAppTotReqCost.setText("0");
        jLabelAcqAppTotReqCost.setMinimumSize(null);
        jPanelGen.add(jLabelAcqAppTotReqCost);
        jLabelAcqAppTotReqCost.setBounds(1280, 595, 60, 25);

        jLabel35.setText("Miscellaneous");
        jPanelGen.add(jLabel35);
        jLabel35.setBounds(8, 30, 70, 25);

        jLabelAppTotReq.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelAppTotReq.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAppTotReq.setText("Total ");
        jPanelGen.add(jLabelAppTotReq);
        jLabelAppTotReq.setBounds(1060, 595, 200, 25);

        jLabelOffice.setText("Office");
        jPanelGen.add(jLabelOffice);
        jLabelOffice.setBounds(580, 240, 70, 30);

        jLabelAcqAccNum.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqAccNum);
        jLabelAcqAccNum.setBounds(710, 270, 130, 30);

        jLabelACC.setText("Account No.");
        jPanelGen.add(jLabelACC);
        jLabelACC.setBounds(580, 270, 80, 30);

        jLabelAcqActMainPurpose.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqActMainPurpose);
        jLabelAcqActMainPurpose.setBounds(160, 410, 750, 30);

        jLabelAcqProvince.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqProvince);
        jLabelAcqProvince.setBounds(130, 230, 210, 30);

        jLabelAcqOffice.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqOffice);
        jLabelAcqOffice.setBounds(710, 240, 210, 30);

        jLabelAcqBudCode.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqBudCode);
        jLabelAcqBudCode.setBounds(160, 350, 750, 30);

        jLabelAcqEmpNum.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqEmpNum);
        jLabelAcqEmpNum.setBounds(130, 200, 60, 30);

        jLabelAcqRefNum.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqRefNum.setForeground(new java.awt.Color(255, 51, 0));
        jPanelGen.add(jLabelAcqRefNum);
        jLabelAcqRefNum.setBounds(900, 130, 60, 30);

        jLabelAcqYear.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqYear.setForeground(new java.awt.Color(255, 51, 0));
        jPanelGen.add(jLabelAcqYear);
        jLabelAcqYear.setBounds(830, 130, 30, 30);

        jLabelNum1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNum1.setForeground(new java.awt.Color(0, 102, 0));
        jLabelNum1.setText("Acquittal No.");
        jPanelGen.add(jLabelNum1);
        jLabelNum1.setBounds(730, 130, 100, 30);

        jLabelAcqSerial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqSerial.setForeground(new java.awt.Color(255, 51, 0));
        jPanelGen.add(jLabelAcqSerial);
        jLabelAcqSerial.setBounds(870, 130, 10, 30);
        jPanelGen.add(jLabelEmp);
        jLabelEmp.setBounds(1010, 120, 80, 30);

        jLabelAcqPayBack.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabelAcqPayBack.setForeground(new java.awt.Color(255, 180, 40));
        jPanelGen.add(jLabelAcqPayBack);
        jLabelAcqPayBack.setBounds(1160, 625, 110, 25);

        jLabelAcqAmtPayBack.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabelAcqAmtPayBack.setForeground(new java.awt.Color(255, 180, 40));
        jPanelGen.add(jLabelAcqAmtPayBack);
        jLabelAcqAmtPayBack.setBounds(1280, 625, 60, 25);

        jLabelPay.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabelPay.setForeground(new java.awt.Color(255, 180, 40));
        jPanelGen.add(jLabelPay);
        jLabelPay.setBounds(1060, 625, 80, 25);

        jLabelSerial.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSerial.setText("R");
        jPanelGen.add(jLabelSerial);
        jLabelSerial.setBounds(110, 130, 20, 30);

        jLabelAcqDateAcq.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqDateAcq);
        jLabelAcqDateAcq.setBounds(970, 130, 160, 30);

        jLabelNum.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNum.setText("Request No.");
        jPanelGen.add(jLabelNum);
        jLabelNum.setBounds(20, 130, 90, 30);

        buttonGroupSpecial.add(jRadioSpecial);
        jRadioSpecial.setFont(new java.awt.Font("Tahoma", 3, 10)); // NOI18N
        jRadioSpecial.setForeground(new java.awt.Color(255, 0, 0));
        jRadioSpecial.setText("  Special");
        jRadioSpecial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioSpecialActionPerformed(evt);
            }
        });
        jPanelGen.add(jRadioSpecial);
        jRadioSpecial.setBounds(630, 140, 80, 30);

        buttonGroupSpecial.add(jRadioNormal);
        jRadioNormal.setFont(new java.awt.Font("Tahoma", 3, 10)); // NOI18N
        jRadioNormal.setForeground(new java.awt.Color(255, 255, 255));
        jRadioNormal.setSelected(true);
        jRadioNormal.setText("  Normal");
        jRadioNormal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioNormalActionPerformed(evt);
            }
        });
        jPanelGen.add(jRadioNormal);
        jRadioNormal.setBounds(540, 140, 80, 30);

        jLabelRemain.setFont(new java.awt.Font("Tahoma", 3, 10)); // NOI18N
        jPanelGen.add(jLabelRemain);
        jLabelRemain.setBounds(1060, 450, 250, 20);

        jLabelSpecial.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabelSpecial.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSpecial.setText("Special  Perdiem Acquittal");
        jPanelGen.add(jLabelSpecial);
        jLabelSpecial.setBounds(550, 80, 290, 30);

        jLabelRegDateAcq.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelRegDateAcq);
        jLabelRegDateAcq.setBounds(180, 130, 160, 30);

        jTextAcqRegNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAcqRegNumActionPerformed(evt);
            }
        });
        jPanelGen.add(jTextAcqRegNum);
        jTextAcqRegNum.setBounds(130, 130, 40, 30);

        jLabelAppTotCleared.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelAppTotCleared.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAppTotCleared.setText("Total Cleared");
        jPanelGen.add(jLabelAppTotCleared);
        jLabelAppTotCleared.setBounds(1060, 570, 200, 25);

        jLabelAcqAppTotReqCostCleared.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelAcqAppTotReqCostCleared.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAcqAppTotReqCostCleared.setText("0");
        jLabelAcqAppTotReqCostCleared.setMinimumSize(null);
        jPanelGen.add(jLabelAcqAppTotReqCostCleared);
        jLabelAcqAppTotReqCostCleared.setBounds(1280, 570, 60, 25);

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
        jLabelHeaderLine.setBounds(400, 40, 610, 40);

        jLabelLineDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate.setText("29 November 2018");
        jPanelRequest.add(jLabelLineDate);
        jLabelLineDate.setBounds(1070, 0, 110, 30);

        jLabelLineTime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime.setText("15:20:30");
        jPanelRequest.add(jLabelLineTime);
        jLabelLineTime.setBounds(1210, 0, 80, 30);

        jLabellogged.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged.setText("Logged In");
        jPanelRequest.add(jLabellogged);
        jLabellogged.setBounds(1070, 40, 80, 30);

        jLabelLineLogNam.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam.setText("User Name");
        jPanelRequest.add(jLabelLineLogNam);
        jLabelLineLogNam.setBounds(1150, 40, 190, 30);

        jTableActivityReq.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jTableActivityReq.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Destination", "Distance", "Purpose", "Budget Line", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Activity", "Misc Amt", "Unproved", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableActivityReq.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableActivityReq.setGridColor(new java.awt.Color(255, 255, 255));
        jTableActivityReq.setMinimumSize(new java.awt.Dimension(180, 64));
        jTableActivityReq.setRowHeight(40);
        jTableActivityReq.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableActivityReqMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableActivityReq);

        jPanelRequest.add(jScrollPane1);
        jScrollPane1.setBounds(0, 115, 1340, 520);

        jLabelSpecial1.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabelSpecial1.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSpecial1.setText("Special  Perdiem Acquittal");
        jPanelRequest.add(jLabelSpecial1);
        jLabelSpecial1.setBounds(550, 80, 290, 30);

        jTabbedPaneAppSys.addTab("Perdiem Request", jPanelRequest);

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
        jLabelLineDate1.setBounds(1060, 0, 110, 30);

        jLabelLineTime1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime1.setText("15:20:30");
        jPanelAcquittal.add(jLabelLineTime1);
        jLabelLineTime1.setBounds(1200, 0, 80, 30);

        jLabellogged1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged1.setText("Logged In");
        jPanelAcquittal.add(jLabellogged1);
        jLabellogged1.setBounds(1060, 40, 80, 30);

        jLabelLineLogNam1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam1.setText("User Name");
        jPanelAcquittal.add(jLabelLineLogNam1);
        jLabelLineLogNam1.setBounds(1140, 40, 190, 30);

        jTableActivityAcq.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jTableActivityAcq.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Destination", "Distance", "Purpose", "Budget Line", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Activity", "Misc Amt", "Unproved", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableActivityAcq.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableActivityAcq.setGridColor(new java.awt.Color(255, 255, 255));
        jTableActivityAcq.setMinimumSize(new java.awt.Dimension(180, 64));
        jTableActivityAcq.setRowHeight(40);
        jTableActivityAcq.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTableActivityAcqFocusLost(evt);
            }
        });
        jTableActivityAcq.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableActivityAcqMouseClicked(evt);
            }
        });
        jTableActivityAcq.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTableActivityAcqKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTableActivityAcqKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(jTableActivityAcq);

        jPanelAcquittal.add(jScrollPane2);
        jScrollPane2.setBounds(290, 115, 1050, 525);

        jPanelCaptureDetails.setLayout(null);

        jLabelDestination.setText("Destination");
        jPanelCaptureDetails.add(jLabelDestination);
        jLabelDestination.setBounds(10, 30, 70, 25);

        jDateChooserActivityAcq.setDateFormatString("yyyy-MM-dd");
        jPanelCaptureDetails.add(jDateChooserActivityAcq);
        jDateChooserActivityAcq.setBounds(120, 2, 130, 25);

        jLabelActDate.setText("Activity Date");
        jPanelCaptureDetails.add(jLabelActDate);
        jLabelActDate.setBounds(10, 2, 70, 25);

        jTextDistDest.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextDistDestFocusLost(evt);
            }
        });
        jTextDistDest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextDistDestActionPerformed(evt);
            }
        });
        jTextDistDest.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextDistDestKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextDistDestKeyTyped(evt);
            }
        });
        jPanelCaptureDetails.add(jTextDistDest);
        jTextDistDest.setBounds(120, 62, 50, 25);

        jLabelDistDest.setText("<html>Distance from <b>base</html>");
        jPanelCaptureDetails.add(jLabelDistDest);
        jLabelDistDest.setBounds(10, 62, 100, 25);
        jPanelCaptureDetails.add(addition);
        addition.setBounds(180, 420, 60, 20);

        jTextAcqDestination.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextAcqDestinationMouseClicked(evt);
            }
        });
        jTextAcqDestination.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAcqDestinationActionPerformed(evt);
            }
        });
        jTextAcqDestination.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextAcqDestinationKeyPressed(evt);
            }
        });
        jPanelCaptureDetails.add(jTextAcqDestination);
        jTextAcqDestination.setBounds(120, 30, 130, 28);

        jLabelKm.setText("Km");
        jPanelCaptureDetails.add(jLabelKm);
        jLabelKm.setBounds(180, 62, 30, 30);
        jPanelCaptureDetails.add(jLabelKMDisDB);
        jLabelKMDisDB.setBounds(210, 62, 50, 30);

        jLabelKMDis.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jPanelCaptureDetails.add(jLabelKMDis);
        jLabelKMDis.setBounds(120, 70, 50, 25);

        jLabelWk1DialogBudget.setText("Budget Line");
        jPanelCaptureDetails.add(jLabelWk1DialogBudget);
        jLabelWk1DialogBudget.setBounds(10, 110, 130, 20);

        jTextFieldBudget.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldBudgetMouseClicked(evt);
            }
        });
        jTextFieldBudget.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldBudgetKeyTyped(evt);
            }
        });
        jPanelCaptureDetails.add(jTextFieldBudget);
        jTextFieldBudget.setBounds(10, 130, 270, 25);

        jLabelActLinePurpose.setText("Purpose of Activity");
        jPanelCaptureDetails.add(jLabelActLinePurpose);
        jLabelActLinePurpose.setBounds(10, 155, 120, 20);
        jPanelCaptureDetails.add(jTextPurpose);
        jTextPurpose.setBounds(10, 175, 260, 25);

        jCheckBreakfast.setText("  Breakfast");
        jCheckBreakfast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBreakfastActionPerformed(evt);
            }
        });
        jPanelCaptureDetails.add(jCheckBreakfast);
        jCheckBreakfast.setBounds(10, 205, 90, 25);

        jLabelBreakfast1.setText("  $");
        jPanelCaptureDetails.add(jLabelBreakfast1);
        jLabelBreakfast1.setBounds(180, 205, 20, 25);

        jTextAcqBreakfast.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
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
        jTextAcqBreakfast.setBounds(200, 205, 70, 25);

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 2));
        jPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel5MouseClicked(evt);
            }
        });
        jPanel5.setLayout(null);

        jRadioLunch3.setText("  $    3.00");
        jRadioLunch3.setToolTipText("Non-proven Day travel 100km radius");
        jRadioLunch3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jRadioLunch3MouseClicked(evt);
            }
        });
        jRadioLunch3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioLunch3ActionPerformed(evt);
            }
        });
        jPanel5.add(jRadioLunch3);
        jRadioLunch3.setBounds(2, 2, 100, 21);

        jRadioLunch10.setText("   $ 10.00");
        jRadioLunch10.setToolTipText("Lunch for travel over 100km from base");
        jRadioLunch10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jRadioLunch10MouseClicked(evt);
            }
        });
        jRadioLunch10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioLunch10ActionPerformed(evt);
            }
        });
        jPanel5.add(jRadioLunch10);
        jRadioLunch10.setBounds(2, 47, 100, 21);

        jRadioLunch5.setText("   $   5.00");
        jRadioLunch5.setToolTipText("Proven Day travel 100km radius");
        jRadioLunch5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jRadioLunch5MouseClicked(evt);
            }
        });
        jRadioLunch5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioLunch5ActionPerformed(evt);
            }
        });
        jPanel5.add(jRadioLunch5);
        jRadioLunch5.setBounds(2, 25, 100, 21);

        jPanelCaptureDetails.add(jPanel5);
        jPanel5.setBounds(60, 230, 110, 75);

        jTextAcqLunch.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
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
        jTextAcqLunch.setBounds(200, 250, 70, 25);

        jLabelBreakfast2.setText("  $");
        jPanelCaptureDetails.add(jLabelBreakfast2);
        jLabelBreakfast2.setBounds(180, 250, 20, 25);

        jLabel1.setText("Lunch");
        jPanelCaptureDetails.add(jLabel1);
        jLabel1.setBounds(10, 250, 40, 30);

        jCheckDinner.setText("   Dinner");
        jCheckDinner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckDinnerActionPerformed(evt);
            }
        });
        jPanelCaptureDetails.add(jCheckDinner);
        jCheckDinner.setBounds(10, 310, 90, 25);

        jCheckIncidental.setText("  Incidental");
        jCheckIncidental.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckIncidentalActionPerformed(evt);
            }
        });
        jPanelCaptureDetails.add(jCheckIncidental);
        jCheckIncidental.setBounds(10, 340, 130, 25);

        jLabelBreakfast3.setText("  $");
        jPanelCaptureDetails.add(jLabelBreakfast3);
        jLabelBreakfast3.setBounds(180, 310, 20, 25);

        jTextAcqDinner.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
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
        jTextAcqDinner.setBounds(200, 310, 70, 25);

        jTextAcqIncidental.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
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
        jTextAcqIncidental.setBounds(200, 340, 70, 25);

        jLabelBreakfast.setText("  $");
        jPanelCaptureDetails.add(jLabelBreakfast);
        jLabelBreakfast.setBounds(180, 340, 20, 25);

        jTextAccUnprovedAcq.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
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
        jTextAccUnprovedAcq.setBounds(200, 370, 70, 25);

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
        jTextAccProvedAcq.setBounds(200, 400, 70, 25);

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
        jTextMiscAmtAcq.setBounds(200, 460, 70, 25);

        jButtonDeleted.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonDeleted.setText("Delete Activity");
        jButtonDeleted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeletedActionPerformed(evt);
            }
        });
        jPanelCaptureDetails.add(jButtonDeleted);
        jButtonDeleted.setBounds(160, 490, 117, 23);

        jLabelSubAcc.setText("    $");
        jPanelCaptureDetails.add(jLabelSubAcc);
        jLabelSubAcc.setBounds(160, 460, 30, 25);

        jLabelProved.setText("$");
        jPanelCaptureDetails.add(jLabelProved);
        jLabelProved.setBounds(180, 400, 20, 20);

        jLabelUnproved.setText("$");
        jPanelCaptureDetails.add(jLabelUnproved);
        jLabelUnproved.setBounds(180, 370, 10, 20);

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
        jRadioAccUnproved.setBounds(10, 370, 170, 25);

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
        jRadioAccProved.setBounds(10, 400, 160, 25);

        jLabelMisc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelMisc.setText("Miscellanous Activity & Amount");
        jPanelCaptureDetails.add(jLabelMisc);
        jLabelMisc.setBounds(10, 430, 190, 25);

        jTextMiscActivityAcq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextMiscActivityAcqActionPerformed(evt);
            }
        });
        jPanelCaptureDetails.add(jTextMiscActivityAcq);
        jTextMiscActivityAcq.setBounds(10, 460, 140, 25);

        jButtonAddActivity.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonAddActivity.setText("Add Activity");
        jButtonAddActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActivityActionPerformed(evt);
            }
        });
        jPanelCaptureDetails.add(jButtonAddActivity);
        jButtonAddActivity.setBounds(10, 490, 130, 23);

        jCheckReturnSameDay.setBackground(new java.awt.Color(0, 153, 51));
        jCheckReturnSameDay.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jCheckReturnSameDay.setText(" Returning Same Day");
        jCheckReturnSameDay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckReturnSameDayActionPerformed(evt);
            }
        });
        jPanelCaptureDetails.add(jCheckReturnSameDay);
        jCheckReturnSameDay.setBounds(10, 90, 160, 21);

        jPanelAcquittal.add(jPanelCaptureDetails);
        jPanelCaptureDetails.setBounds(0, 115, 280, 525);

        jButtonBnkUpd.setBackground(new java.awt.Color(102, 204, 0));
        jButtonBnkUpd.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButtonBnkUpd.setText("Update Bank Payment");
        jButtonBnkUpd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBnkUpdActionPerformed(evt);
            }
        });
        jPanelAcquittal.add(jButtonBnkUpd);
        jButtonBnkUpd.setBounds(1140, 80, 160, 30);

        jLabelSpecial2.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabelSpecial2.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSpecial2.setText("Special  Perdiem Acquittal");
        jPanelAcquittal.add(jLabelSpecial2);
        jLabelSpecial2.setBounds(550, 80, 290, 30);

        jLabelDistrict.setText("jLabel2");
        jPanelAcquittal.add(jLabelDistrict);
        jLabelDistrict.setBounds(300, 80, 140, 30);

        jTabbedPaneAppSys.addTab("Perdiem Acquittal", jPanelAcquittal);

        jPanelReportDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelReportDetailsMouseClicked(evt);
            }
        });
        jPanelReportDetails.setLayout(null);

        jTextAreaTripReport.setColumns(20);
        jTextAreaTripReport.setRows(5);
        jScrollPane8.setViewportView(jTextAreaTripReport);

        jPanelReportDetails.add(jScrollPane8);
        jScrollPane8.setBounds(10, 290, 1360, 320);

        jLabelTripReport.setText("Activity Summary");
        jPanelReportDetails.add(jLabelTripReport);
        jLabelTripReport.setBounds(10, 260, 200, 30);

        jLabelNamVisited.setText("Whom Did You See At Site (Full Name and Contact Details)");
        jPanelReportDetails.add(jLabelNamVisited);
        jLabelNamVisited.setBounds(0, 130, 380, 30);

        jTextAreaNamVisited.setColumns(20);
        jTextAreaNamVisited.setRows(5);
        jScrollPane9.setViewportView(jTextAreaNamVisited);

        jPanelReportDetails.add(jScrollPane9);
        jScrollPane9.setBounds(0, 160, 1360, 96);

        jLabelNamTravel.setText("Who Travelled (Full Name and Contact Details)");
        jPanelReportDetails.add(jLabelNamTravel);
        jLabelNamTravel.setBounds(0, 0, 280, 30);

        jTextAreaNamTravel.setColumns(20);
        jTextAreaNamTravel.setRows(5);
        jScrollPane10.setViewportView(jTextAreaNamTravel);

        jPanelReportDetails.add(jScrollPane10);
        jScrollPane10.setBounds(0, 30, 1360, 96);

        javax.swing.GroupLayout jPanelReportLayout = new javax.swing.GroupLayout(jPanelReport);
        jPanelReport.setLayout(jPanelReportLayout);
        jPanelReportLayout.setHorizontalGroup(
            jPanelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelReportDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        jLabel2.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 153, 153));
        jLabel2.setText("e.g. AAA0847  ");
        jPanelAtt4.add(jLabel2);
        jLabel2.setBounds(10, 85, 130, 20);

        jButtonGetDetails.setBackground(new java.awt.Color(0, 153, 51));
        jButtonGetDetails.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonGetDetails.setText("<html> <center> Get Details</center></html>");
        jButtonGetDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGetDetailsActionPerformed(evt);
            }
        });
        jPanelAtt4.add(jButtonGetDetails);
        jButtonGetDetails.setBounds(10, 300, 90, 40);

        jPanelAcqELog.add(jPanelAtt4);
        jPanelAtt4.setBounds(5, 120, 240, 490);

        jLabelLogo8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelAcqELog.add(jLabelLogo8);
        jLabelLogo8.setBounds(10, 10, 220, 100);

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

        jLabelLineLogNam10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam10.setText("User Name");
        jPanelAcqELog.add(jLabelLineLogNam10);
        jLabelLineLogNam10.setBounds(1160, 40, 190, 30);

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
                false, true, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane11.setViewportView(jTableTripDetails);

        jPanelAcqELog.add(jScrollPane11);
        jScrollPane11.setBounds(250, 120, 1090, 490);

        jTabbedPaneAcqAtt.addTab("Attachment 1", jPanelAcqELog);

        jPanelAcqDocAtt1.setBackground(new java.awt.Color(226, 255, 255));
        jPanelAcqDocAtt1.setMinimumSize(new java.awt.Dimension(1270, 671));
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
        jPanelAtt1.setBounds(5, 115, 240, 510);

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
        jLabelLineDate2.setBounds(1070, 0, 110, 30);

        jLabelLineTime2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime2.setText("15:20:30");
        jPanelAcqDocAtt1.add(jLabelLineTime2);
        jLabelLineTime2.setBounds(1210, 0, 80, 30);

        jLabellogged2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged2.setText("Logged In");
        jPanelAcqDocAtt1.add(jLabellogged2);
        jLabellogged2.setBounds(1070, 40, 80, 30);

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
                .addGap(259, 259, 259)
                .addComponent(jLabelImgFile1, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                .addContainerGap(209, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabelImgFile1, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
        );

        jScrollPaneAtt1.setViewportView(jPanel9);

        jPanelAcqDocAtt1.add(jScrollPaneAtt1);
        jScrollPaneAtt1.setBounds(260, 115, 1090, 510);

        jLabelSpecial3.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabelSpecial3.setForeground(new java.awt.Color(0, 153, 0));
        jLabelSpecial3.setText("Special  Perdiem Acquittal");
        jPanelAcqDocAtt1.add(jLabelSpecial3);
        jLabelSpecial3.setBounds(550, 80, 290, 30);

        jTabbedPaneAcqAtt.addTab("Attachment 1", jPanelAcqDocAtt1);

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
        jPanelAtt2.setBounds(5, 115, 240, 510);

        jLabelLogo4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelAcqDocAtt2.add(jLabelLogo4);
        jLabelLogo4.setBounds(10, 10, 220, 100);

        jLabelHeaderLine4.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine4.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelAcqDocAtt2.add(jLabelHeaderLine4);
        jLabelHeaderLine4.setBounds(350, 40, 610, 40);

        jLabelLineDate4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate4.setText("29 November 2018");
        jPanelAcqDocAtt2.add(jLabelLineDate4);
        jLabelLineDate4.setBounds(1070, 0, 110, 30);

        jLabelLineTime4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime4.setText("15:20:30");
        jPanelAcqDocAtt2.add(jLabelLineTime4);
        jLabelLineTime4.setBounds(1210, 0, 80, 30);

        jLabellogged4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged4.setText("Logged In");
        jPanelAcqDocAtt2.add(jLabellogged4);
        jLabellogged4.setBounds(1070, 40, 80, 30);

        jLabelLineLogNam4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam4.setText("User Name");
        jPanelAcqDocAtt2.add(jLabelLineLogNam4);
        jLabelLineLogNam4.setBounds(1160, 40, 190, 30);

        jScrollPaneAtt2.setPreferredSize(new java.awt.Dimension(1000, 480));

        jPanel19.setPreferredSize(new java.awt.Dimension(998, 484));

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap(280, Short.MAX_VALUE)
                .addComponent(jLabelImgFile2, javax.swing.GroupLayout.PREFERRED_SIZE, 620, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(188, 188, 188))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addComponent(jLabelImgFile2, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jScrollPaneAtt2.setViewportView(jPanel19);

        jPanelAcqDocAtt2.add(jScrollPaneAtt2);
        jScrollPaneAtt2.setBounds(260, 115, 1090, 510);

        jTabbedPaneAcqAtt.addTab("Attachment 2", jPanelAcqDocAtt2);

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
        jPanelAtt3.setBounds(5, 115, 240, 510);

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
        jLabelLineDate5.setBounds(1050, 0, 110, 30);

        jLabelLineTime5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime5.setText("15:20:30");
        jPanelAcqDocAtt3.add(jLabelLineTime5);
        jLabelLineTime5.setBounds(1190, 0, 80, 30);

        jLabellogged5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged5.setText("Logged In");
        jPanelAcqDocAtt3.add(jLabellogged5);
        jLabellogged5.setBounds(1050, 40, 80, 30);

        jLabelLineLogNam5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam5.setText("User Name");
        jPanelAcqDocAtt3.add(jLabelLineLogNam5);
        jLabelLineLogNam5.setBounds(1140, 40, 190, 30);

        jScrollPaneAtt3.setPreferredSize(new java.awt.Dimension(1000, 480));

        jPanel21.setPreferredSize(new java.awt.Dimension(998, 484));

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(284, 284, 284)
                .addComponent(jLabelImgFile3, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                .addContainerGap(184, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addComponent(jLabelImgFile3, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jScrollPaneAtt3.setViewportView(jPanel21);

        jPanelAcqDocAtt3.add(jScrollPaneAtt3);
        jScrollPaneAtt3.setBounds(260, 115, 1090, 510);

        jTabbedPaneAcqAtt.addTab("Attachment 3", jPanelAcqDocAtt3);

        jPanelDocAttach4.setBackground(new java.awt.Color(204, 255, 255));
        jPanelDocAttach4.setLayout(null);

        jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel14.setLayout(null);

        jButtonDeleteAtt4.setText("Delete  Requistion Attachment 4");
        jButtonDeleteAtt4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteAtt4ActionPerformed(evt);
            }
        });
        jPanel14.add(jButtonDeleteAtt4);
        jButtonDeleteAtt4.setBounds(10, 210, 205, 21);

        jButtonChooseAtt4.setText("Choose Requistion 4");
        jButtonChooseAtt4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonChooseAtt4ActionPerformed(evt);
            }
        });
        jPanel14.add(jButtonChooseAtt4);
        jButtonChooseAtt4.setBounds(11, 126, 205, 21);

        jPanelDocAttach4.add(jPanel14);
        jPanel14.setBounds(5, 115, 240, 515);

        jLabelLogo6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelDocAttach4.add(jLabelLogo6);
        jLabelLogo6.setBounds(10, 10, 220, 100);

        jLabelHeaderLine7.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine7.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelDocAttach4.add(jLabelHeaderLine7);
        jLabelHeaderLine7.setBounds(350, 40, 610, 40);

        jLabelLineDate8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate8.setText("29 November 2018");
        jPanelDocAttach4.add(jLabelLineDate8);
        jLabelLineDate8.setBounds(1070, 0, 110, 30);

        jLabelLineTime8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime8.setText("15:20:30");
        jPanelDocAttach4.add(jLabelLineTime8);
        jLabelLineTime8.setBounds(1210, 0, 80, 30);

        jLabellogged7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged7.setText("Logged In");
        jPanelDocAttach4.add(jLabellogged7);
        jLabellogged7.setBounds(1070, 40, 80, 30);

        jLabelLineLogNam9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam9.setText("User Name");
        jPanelDocAttach4.add(jLabelLineLogNam9);
        jLabelLineLogNam9.setBounds(1160, 40, 190, 30);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(243, 243, 243)
                .addComponent(jLabelImgFile4, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                .addContainerGap(475, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jLabelImgFile4, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                .addGap(0, 103, Short.MAX_VALUE))
        );

        jScrollPane6.setViewportView(jPanel15);

        jPanelDocAttach4.add(jScrollPane6);
        jScrollPane6.setBounds(260, 115, 1320, 520);

        jTabbedPaneAcqAtt.addTab("Attachment 4", jPanelDocAttach4);

        jPanelDocAttach5.setBackground(new java.awt.Color(204, 204, 255));
        jPanelDocAttach5.setLayout(null);

        jPanel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel16.setLayout(null);

        jButtonDeleteAtt5.setText("Delete  Requistion Attachment 5");
        jButtonDeleteAtt5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteAtt5ActionPerformed(evt);
            }
        });
        jPanel16.add(jButtonDeleteAtt5);
        jButtonDeleteAtt5.setBounds(10, 210, 205, 21);

        jButtonChooseAtt5.setText("Choose Requistion 5");
        jButtonChooseAtt5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonChooseAtt5ActionPerformed(evt);
            }
        });
        jPanel16.add(jButtonChooseAtt5);
        jButtonChooseAtt5.setBounds(11, 126, 205, 21);

        jPanelDocAttach5.add(jPanel16);
        jPanel16.setBounds(5, 115, 240, 515);

        jLabelLogo7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelDocAttach5.add(jLabelLogo7);
        jLabelLogo7.setBounds(10, 10, 220, 100);

        jLabelHeaderLine6.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine6.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelDocAttach5.add(jLabelHeaderLine6);
        jLabelHeaderLine6.setBounds(350, 40, 610, 40);

        jLabelLineDate7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate7.setText("29 November 2018");
        jPanelDocAttach5.add(jLabelLineDate7);
        jLabelLineDate7.setBounds(1060, 0, 110, 30);

        jLabelLineTime7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime7.setText("15:20:30");
        jPanelDocAttach5.add(jLabelLineTime7);
        jLabelLineTime7.setBounds(1200, 0, 80, 30);

        jLabellogged6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged6.setText("Logged In");
        jPanelDocAttach5.add(jLabellogged6);
        jLabellogged6.setBounds(1060, 40, 80, 30);

        jLabelLineLogNam8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam8.setText("User Name");
        jPanelDocAttach5.add(jLabelLineLogNam8);
        jLabelLineLogNam8.setBounds(1150, 40, 200, 30);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(244, 244, 244)
                .addComponent(jLabelImgFile5, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                .addContainerGap(474, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabelImgFile5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
        );

        jScrollPane7.setViewportView(jPanel17);

        jPanelDocAttach5.add(jScrollPane7);
        jScrollPane7.setBounds(260, 115, 1090, 500);

        jTabbedPaneAcqAtt.addTab("Attachment 5", jPanelDocAttach5);

        jTabbedPaneAppSys.addTab("Attachments", jTabbedPaneAcqAtt);

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jTabbedPaneAppSys, javax.swing.GroupLayout.DEFAULT_SIZE, 1350, Short.MAX_VALUE)
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


    private void jTableActivityReqMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableActivityReqMouseClicked

        int row = jTableActivityReq.getSelectedRow();
        int col = jTableActivityReq.getSelectedColumn();
        Object id = jTableActivityReq.getValueAt(row, col);
        String tt = id.toString();


    }//GEN-LAST:event_jTableActivityReqMouseClicked

    private void jTableActivityAcqFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTableActivityAcqFocusLost
        mainPageTotUpdateAcq();
    }//GEN-LAST:event_jTableActivityAcqFocusLost

    private void jTableActivityAcqMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableActivityAcqMouseClicked
        int row = jTableActivityAcq.getSelectedRow();
        totSeg1 = 0;
        totSeg2 = 0;
        for (int i = 5; i < 9; i++) {

            if (jTableActivityAcq.getValueAt(row, i).toString().trim().length() == 0) {
                jTableActivityAcq.setValueAt("0", row, i);
            }
            totSeg1 = totSeg1 + (Double.parseDouble((String) jTableActivityAcq.getValueAt(row, i)));

        }
        for (int i = 10; i < 12; i++) {

            if (jTableActivityAcq.getValueAt(row, i).toString().trim().length() == 0) {
                jTableActivityAcq.setValueAt("0", row, i);
                totSeg2 = totSeg2 + (Double.parseDouble((String) jTableActivityAcq.getValueAt(row, i)));
            }

            totSeg2 = totSeg2 + (Double.parseDouble((String) jTableActivityAcq.getValueAt(row, i)));
            jTableActivityAcq.setValueAt((totSeg1 + totSeg2), row, 12);
        }

    }//GEN-LAST:event_jTableActivityAcqMouseClicked

    private void jTableActivityAcqKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableActivityAcqKeyTyped

    }//GEN-LAST:event_jTableActivityAcqKeyTyped

    private void jTableActivityAcqKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableActivityAcqKeyReleased
        int row = jTableActivityAcq.getSelectedRow();
        totSeg1 = 0;
        totSeg2 = 0;
        for (int i = 5; i < 9; i++) {

            if (jTableActivityAcq.getValueAt(row, i).toString().trim().length() == 0) {
                jTableActivityAcq.setValueAt("0", row, i);
            }
            totSeg1 = totSeg1 + (Double.parseDouble((String) jTableActivityAcq.getValueAt(row, i)));

        }
        for (int i = 10; i < 12; i++) {

            if (jTableActivityAcq.getValueAt(row, i).toString().trim().length() == 0) {
                jTableActivityAcq.setValueAt("0", row, i);
                totSeg2 = totSeg2 + (Double.parseDouble((String) jTableActivityAcq.getValueAt(row, i)));
            }

            totSeg2 = totSeg2 + (Double.parseDouble((String) jTableActivityAcq.getValueAt(row, i)));
            jTableActivityAcq.setValueAt((totSeg1 + totSeg2), row, 12);
        }

    }//GEN-LAST:event_jTableActivityAcqKeyReleased

    private void jTextDistDestFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextDistDestFocusLost
        if ((Integer.parseInt(jTextDistDest.getText()) < 101) && jRadioLunch10.isSelected()) {
            int selectedOption = JOptionPane.showConfirmDialog(null,
                    "Do you authorisation to claim $10 lunch?",
                    "Choose",
                    JOptionPane.YES_NO_OPTION);
            if (selectedOption == JOptionPane.YES_OPTION) {
                jDialogAuthority.setVisible(true);
                //                findAuthUser();
            } else {
                JOptionPane.showMessageDialog(this, "Please get authorisation.");
                buttonGroupLunch.clearSelection();
                jTextAcqLunch.setText("");
                authUsrNam = "";
            }
        }
        if (("Central Office".equals(jLabelAcqProvince.getText()))
                || (jComboDistrictFacility.getSelectedItem().toString().equals(jComboFacility.getSelectedItem().toString()))) {
            jLabelKMDisDB.setText(jTextDistDest.getText());
        }
    }//GEN-LAST:event_jTextDistDestFocusLost

    private void jTextDistDestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextDistDestActionPerformed
        if (("Central Office".equals(jLabelAcqProvince.getText()))
                || (jComboDistrictFacility.getSelectedItem().toString().equals(jComboFacility.getSelectedItem().toString()))) {
            jLabelKMDisDB.setText(jTextDistDest.getText());
        }
    }//GEN-LAST:event_jTextDistDestActionPerformed

    private void jTextDistDestKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextDistDestKeyPressed
        int key = evt.getKeyCode();
        if ((key >= evt.VK_0 && key <= evt.VK_9) || (key >= evt.VK_NUMPAD0 && key <= evt.VK_NUMPAD9)
                || key == KeyEvent.VK_BACK_SPACE || key == KeyEvent.VK_DECIMAL) {
            jTextDistDest.setEditable(true);

        } else {
            jTextDistDest.setEditable(false);

        }
    }//GEN-LAST:event_jTextDistDestKeyPressed

    private void jTextDistDestKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextDistDestKeyTyped

    }//GEN-LAST:event_jTextDistDestKeyTyped

    private void jTextAcqDestinationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAcqDestinationActionPerformed

    }//GEN-LAST:event_jTextAcqDestinationActionPerformed

    private void jButtonAuthOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAuthOkActionPerformed

        jDialogAuthority.setVisible(false);
        jTextAcqLunch.setText(lunchAll);
        authUsrNam = jComboAuthNam.getSelectedItem().toString();
    }//GEN-LAST:event_jButtonAuthOkActionPerformed

    private void jButtonAuthCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAuthCancelActionPerformed
        authUsrNam = "";
        jDialogAuthority.setVisible(false);
        buttonGroupLunch.clearSelection();

        //jComboAuthNam.addItem("");
    }//GEN-LAST:event_jButtonAuthCancelActionPerformed

    private void jButtonAuthAllOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAuthAllOkActionPerformed
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        DefaultTableModel model = (DefaultTableModel) jTableActivityAcq.getModel();
        java.util.Date jDateActivity = jDateChooserActivityAcq.getDate();

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

        model.addRow(new Object[]{dateDayActivity, jTextAcqDestination.getText(), jLabelKMDisDB.getText(), jTextPurpose.getText(),
            jTextAcqBreakfast.getText(), jTextAcqLunch.getText(), jTextAcqDinner.getText(), jTextAcqIncidental.getText(),
            jTextMiscActivityAcq.getText(), jTextMiscAmtAcq.getText(), jTextAccProvedAcq.getText(), jTextAccUnprovedAcq.getText(),
            Double.toString((Double.parseDouble(jTextAcqBreakfast.getText()) + Double.parseDouble(jTextAcqLunch.getText())
            + Double.parseDouble(jTextAcqDinner.getText()) + Double.parseDouble(jTextAcqIncidental.getText())
            + Double.parseDouble(jTextMiscAmtAcq.getText()) + Double.parseDouble(jTextAccProvedAcq.getText())
            + Double.parseDouble(jTextAccUnprovedAcq.getText())))});

        mainPageTotUpdateAcq();
        /**
         * **** updating general segment
         */

        jDateChooserActivityAcq.setDate(null);
        jTextDistDest.setText("");
        jLabelKMDis.setVisible(false);
        jTextDistDest.setVisible(true);
        jLabelKMDisDB.setText("");
        jLabelKMDis.setText("");
        jLabelKMDisDB.setVisible(false);
        jTextPurpose.setText("");
        jTextAcqBreakfast.setText("0.00");
        jTextAcqLunch.setText("0.00");
        jTextAcqDinner.setText("0.00");
        jTextAcqIncidental.setText("0.00");
        jTextMiscAmtAcq.setText("0.00");
        jTextAccProvedAcq.setText("0.00");
        jTextAccUnprovedAcq.setText("0.00");
        jTextMiscActivityAcq.setText("");
        jTextAcqDestination.setText("");
        buttonGroupLunch.clearSelection();
        jCheckBreakfast.setSelected(false);
        jRadioLunch3.setSelected(false);
        jRadioLunch5.setSelected(false);
        jRadioLunch10.setSelected(false);
        jCheckDinner.setSelected(false);
        jCheckIncidental.setSelected(false);
        jRadioAccProved.setSelected(false);
        jRadioAccUnproved.setSelected(false);

        authUsrNamAll = jComboAuthNamAll.getSelectedItem().toString();
        jDialogAuthorityAll.setVisible(false);
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
        fileDel1 = "Y";
        filechange = "Y";
    }//GEN-LAST:event_jButtonDeleteAtt1ActionPerformed

    private void jButtonChooseAtt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonChooseAtt1ActionPerformed
        imgChooserFile1();
        filechange = "Y";
        startSym1 = "Y";
    }//GEN-LAST:event_jButtonChooseAtt1ActionPerformed

    private void jButtonDeleteAtt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteAtt2ActionPerformed
        jLabelImgFile2.setIcon(null);
        fileDel2 = "Y";
        filechange = "Y";
    }//GEN-LAST:event_jButtonDeleteAtt2ActionPerformed

    private void jButtonChooseAtt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonChooseAtt2ActionPerformed
        imgChooserFile2();
        filechange = "Y";
        startSym2 = "Y";
    }//GEN-LAST:event_jButtonChooseAtt2ActionPerformed

    private void jButtonDeleteAtt3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteAtt3ActionPerformed
        jLabelImgFile3.setIcon(null);
        fileDel3 = "Y";
        filechange = "Y";
    }//GEN-LAST:event_jButtonDeleteAtt3ActionPerformed

    private void jButtonChooseAtt3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonChooseAtt3ActionPerformed
        imgChooserFile3();
        filechange = "Y";
        startSym3 = "Y";
    }//GEN-LAST:event_jButtonChooseAtt3ActionPerformed

    private void jButtonBankOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBankOkActionPerformed
        if ("Select Bank".equals(jComboBankNam)) {
            JOptionPane.showMessageDialog(this, "Please select bank name.");
        } else if (("".equals(jTextPaidAmt.getText()))
                || ("0.00".equals(jTextPaidAmt.getText()))) {
            JOptionPane.showMessageDialog(this, "Please enter amount banked.");
        } else {

            jDialogChgPaid.setVisible(false);
            jLabelPay.setText("Paid Back A/C");
            jLabelAcqPayBack.setText(jLabelBankAccNo.getText());
            jLabelAcqAmtPayBack.setText(Double.toString(Double.parseDouble(jTextPaidAmt.getText())));

        }
    }//GEN-LAST:event_jButtonBankOkActionPerformed

    private void jButtonBankCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBankCancelActionPerformed
        jDialogChgPaid.setVisible(false);
        jLabelPay.setText("");
        jLabelAcqPayBack.setText("");
        jLabelAcqAmtPayBack.setText("");

    }//GEN-LAST:event_jButtonBankCancelActionPerformed

    private void jComboBankNamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBankNamActionPerformed
        findBankAcc();
    }//GEN-LAST:event_jComboBankNamActionPerformed

    private void jTextPaidAmtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextPaidAmtFocusGained
        jTextPaidAmt.setText("");
    }//GEN-LAST:event_jTextPaidAmtFocusGained

    private void jTextPaidAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextPaidAmtActionPerformed

    }//GEN-LAST:event_jTextPaidAmtActionPerformed

    private void jPanel10FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel10FocusGained

    }//GEN-LAST:event_jPanel10FocusGained

    private void jButtonBnkUpdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBnkUpdActionPerformed
        jDialogChgPaid.setVisible(true);
    }//GEN-LAST:event_jButtonBnkUpdActionPerformed

    private void jRadioSpecialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioSpecialActionPerformed
        if (jRadioSpecial.isSelected()) {
            jLabelSerial.setText("S");
        }
    }//GEN-LAST:event_jRadioSpecialActionPerformed

    private void jRadioNormalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioNormalActionPerformed
        if (jRadioNormal.isSelected()) {
            jLabelSerial.setText("R");
        }
    }//GEN-LAST:event_jRadioNormalActionPerformed

    private void jTextAcqRegNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAcqRegNumActionPerformed

        jTabbedPaneAcqAtt.setEnabledAt(0, true);
        jTabbedPaneAcqAtt.setEnabledAt(1, false);
        jTabbedPaneAcqAtt.setEnabledAt(2, true);
        jTabbedPaneAcqAtt.setEnabledAt(3, true);
        jTabbedPaneAcqAtt.setEnabledAt(4, true);
        jTabbedPaneAcqAtt.setEnabledAt(5, false);
        jTabbedPaneAcqAtt.setEnabledAt(6, false);
//            jTabbedPaneAcqAtt.setTitleAt(0, "Activity Summary Report");
//            jTabbedPaneAcqAtt.setTitleAt(1, "Vehicle Log Sheet");
//            jTabbedPaneAcqAtt.setTitleAt(2, "Proven Expenses");
//            jTabbedPaneAcqAtt.setTitleAt(3, "Other e.g. Log Book Extra Page");
        jTabbedPaneAcqAtt.setTitleAt(5, "");
        jTabbedPaneAcqAtt.setTitleAt(6, "");
//            jButtonChooseAtt1.setText("Select Log Sheet");
//            jButtonDeleteAtt1.setText("Delete Log Sheet");
//            jButtonChooseAtt2.setText("Select Proven Expenses");
//            jButtonDeleteAtt2.setText("Delete Proven Expenses");
//            jButtonChooseAtt3.setText("Select Other Documents");
//            jButtonDeleteAtt3.setText("Delete Other Documents");

        jTabbedPaneAcqAtt.setTitleAt(0, "Activity Summary Report");
        jTabbedPaneAcqAtt.setTitleAt(1, "");
        jTabbedPaneAcqAtt.setTitleAt(2, "Vehicle Log Sheet");
        jTabbedPaneAcqAtt.setTitleAt(3, "Refund Deposit Slip");
        jTabbedPaneAcqAtt.setTitleAt(4, "Other e.g. Log Book Extra Page");
        jButtonChooseAtt1.setText("Select Log Sheet");
        jButtonDeleteAtt1.setText("Delete Log Sheet");
        jButtonChooseAtt2.setText("Select Refund Deposit Slip");
        jButtonDeleteAtt2.setText("Delete Refund Deposit Slip");
        jButtonChooseAtt3.setText("Select Other Documents");
        jButtonDeleteAtt3.setText("Delete Other Documents");

        refreshTab();
        acqWeek();
        regInitCheck();
    }//GEN-LAST:event_jTextAcqRegNumActionPerformed

    private void jButtonOkFacilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkFacilityActionPerformed
        jLabelDisProvince.setText(jComboProvinceFacility.getSelectedItem().toString());
        jLabelDisDistrict.setText(jComboDistrictFacility.getSelectedItem().toString());
        jLabelDisFacilty.setText(jComboFacility.getSelectedItem().toString());
        jTextAcqDestination.setText(jLabelDisFacilty.getText());
        facDist();
        // jTextDistDest.setText(jLabelFacDist.getText());
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

    private void jTextAcqDestinationKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAcqDestinationKeyPressed
        jLabelDisProvince.setVisible(false);
        jLabelDisDistrict.setVisible(false);
        jLabelDisFacilty.setVisible(false);
        jLabelFacDist.setVisible(false);
        jDialogFacility.setVisible(true);
        jTextAcqDestination.setText("");
    }//GEN-LAST:event_jTextAcqDestinationKeyPressed

    private void jTextAcqDestinationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextAcqDestinationMouseClicked
        jLabelDisProvince.setVisible(false);
        jLabelDisDistrict.setVisible(false);
        jLabelDisFacilty.setVisible(false);
        jLabelFacDist.setVisible(false);
        jDialogFacility.setVisible(true);
        jTextAcqDestination.setText("");
    }//GEN-LAST:event_jTextAcqDestinationMouseClicked

    private void jPanelReportDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelReportDetailsMouseClicked

    }//GEN-LAST:event_jPanelReportDetailsMouseClicked

    private void jButtonBankOk1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBankOk1ActionPerformed
        try {
            DefaultTableModel dm = (DefaultTableModel) jTableActivityReq.getModel();
            DefaultTableModel dmAcq = (DefaultTableModel) jTableActivityAcq.getModel();
            while (dm.getRowCount() > 0) {
                dm.removeRow(0);
            }
            while (dmAcq.getRowCount() > 0) {
                dmAcq.removeRow(0);
            }
            wkNum = jComboWk.getSelectedItem().toString().substring((jComboWk.getSelectedItem().toString()).length() - 1);

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st1 = conn.createStatement();

            st1.executeQuery("SELECT concat(SERIAL,REF_NUM) FROM [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab]"
                    + " where concat(PREV_SERIAL,PREV_REF_NUM,PLAN_WK) =  'R"
                    + jTextAcqRegNum.getText() + wkNum + "' and ACQ_STA = 'C' ");

            ResultSet rs1 = st1.getResultSet();

            while (rs1.next()) {
                searchRef = rs1.getString(1);

            }

            fetchdataWk();
//            mainPageTotUpdate();
//            mainPageTotUpdateAcq();
//            fetchImgCount();
//            imgCount();
            jDialogWkDisplay.setVisible(false);
        } catch (Exception e) {

        }
    }//GEN-LAST:event_jButtonBankOk1ActionPerformed

    private void jButtonBankCancel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBankCancel1ActionPerformed
        jDialogWkDisplay.setVisible(false);
        jLabelPay.setText("");

    }//GEN-LAST:event_jButtonBankCancel1ActionPerformed

    private void jComboWkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboWkActionPerformed

    }//GEN-LAST:event_jComboWkActionPerformed

    private void jPanel12FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel12FocusGained

    }//GEN-LAST:event_jPanel12FocusGained

    private void jTextFieldBudgetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldBudgetMouseClicked
        budgetPOP();
    }//GEN-LAST:event_jTextFieldBudgetMouseClicked

    private void jTextFieldBudgetKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldBudgetKeyTyped
        budgetPOP();
    }//GEN-LAST:event_jTextFieldBudgetKeyTyped

    private void jCheckBreakfastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBreakfastActionPerformed
        if (jCheckBreakfast.isSelected()) {
            jTextAcqBreakfast.setText(breakfastAll);
        } else {
            jTextAcqBreakfast.setText("0.00");
        }
    }//GEN-LAST:event_jCheckBreakfastActionPerformed

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

    private void jRadioLunch3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioLunch3MouseClicked
        if (jRadioLunch3.isSelected()) {

            if (++clickCount % 2 == 0) {
                buttonGroupLunch.clearSelection();
                jTextAcqLunch.setText("0.00");

            }
        }
    }//GEN-LAST:event_jRadioLunch3MouseClicked

    private void jRadioLunch3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioLunch3ActionPerformed
        if (jRadioLunch3.isSelected()) {
            jTextAcqLunch.setText(lunchNPAll);
        } else {
            jTextAcqLunch.setText("0.00");
        }
    }//GEN-LAST:event_jRadioLunch3ActionPerformed

    private void jRadioLunch10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioLunch10MouseClicked
        if (jRadioLunch10.isSelected()) {

            if (++clickCount % 2 == 0) {
                buttonGroupLunch.clearSelection();
                jTextAcqLunch.setText("0.00");

            }
        }
    }//GEN-LAST:event_jRadioLunch10MouseClicked

    private void jRadioLunch10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioLunch10ActionPerformed

        if (jRadioLunch10.isSelected()) {
            //                authUsrNam = "";
            jTextAcqLunch.setText(lunchAll);
            jRadioLunch5.setSelected(false);
        } else {
            //                authUsrNam = "";
            jTextAcqLunch.setText("0.00");
        }
        //        }
    }//GEN-LAST:event_jRadioLunch10ActionPerformed

    private void jRadioLunch5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioLunch5MouseClicked
        if (jRadioLunch5.isSelected()) {

            if (++clickCount % 2 == 0) {
                buttonGroupLunch.clearSelection();
                jTextAcqLunch.setText("0.00");

            }
        }
    }//GEN-LAST:event_jRadioLunch5MouseClicked

    private void jRadioLunch5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioLunch5ActionPerformed
        if (jRadioLunch5.isSelected()) {
            JOptionPane.showMessageDialog(this, "Proof of purchase is required.");
            jTextAcqLunch.setText(lunchPAll);
            jRadioLunch10.setSelected(false);
        } else {
            jTextAcqLunch.setText("0.00");
        }
    }//GEN-LAST:event_jRadioLunch5ActionPerformed

    private void jPanel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseClicked

    }//GEN-LAST:event_jPanel5MouseClicked

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

    private void jCheckDinnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckDinnerActionPerformed
        if (jCheckDinner.isSelected()) {
            jTextAcqDinner.setText(dinnerAll);
        } else {
            jTextAcqDinner.setText("0.00");
        }
    }//GEN-LAST:event_jCheckDinnerActionPerformed

    private void jCheckIncidentalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckIncidentalActionPerformed
        if (jCheckIncidental.isSelected()) {
            jTextAcqIncidental.setText(incidentalAll);
        } else {
            jTextAcqIncidental.setText("0.00");
        }
    }//GEN-LAST:event_jCheckIncidentalActionPerformed

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

    private void jButtonDeletedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeletedActionPerformed
        DefaultTableModel model = (DefaultTableModel) this.jTableActivityAcq.getModel();
        int selectedOption = JOptionPane.showConfirmDialog(null,
                "Do you want to delete selected activity line?",
                "Choose",
                JOptionPane.YES_NO_OPTION);
        if (selectedOption == JOptionPane.YES_OPTION) {

            int[] rows = jTableActivityAcq.getSelectedRows();
            for (int i = 0; i < rows.length; i++) {
                model.removeRow(rows[i] - i);
            }
        } else {
            jButtonDeleted.requestFocusInWindow();
            jButtonDeleted.setFocusable(true);
        }
        //mainPageTotUpdate();
        mainPageTotUpdateAcq();
    }//GEN-LAST:event_jButtonDeletedActionPerformed

    private void jRadioAccUnprovedFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jRadioAccUnprovedFocusGained

        jLabelProved.setVisible(false);
        jTextAccProvedAcq.setVisible(false);
        jLabelUnproved.setVisible(true);
        jTextAccUnprovedAcq.setVisible(true);
    }//GEN-LAST:event_jRadioAccUnprovedFocusGained

    private void jRadioAccUnprovedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioAccUnprovedMouseClicked
        if (jRadioAccUnproved.isSelected()) {

            if (++clickCount % 2 == 0) {
                buttonGroupAcc.clearSelection();
                jTextAccUnprovedAcq.setText("");

            }
        }
    }//GEN-LAST:event_jRadioAccUnprovedMouseClicked

    private void jRadioAccUnprovedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioAccUnprovedActionPerformed
        if (jRadioAccUnproved.isSelected()) {
            jTextAccUnprovedAcq.setText(unProvedAll);
        } else {
            jTextAccUnprovedAcq.setText("0.00");
        }
    }//GEN-LAST:event_jRadioAccUnprovedActionPerformed

    private void jRadioAccProvedFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jRadioAccProvedFocusGained

        jLabelUnproved.setVisible(false);
        jTextAccUnprovedAcq.setVisible(false);
        jLabelProved.setVisible(true);
        jTextAccProvedAcq.setVisible(true);
    }//GEN-LAST:event_jRadioAccProvedFocusGained

    private void jRadioAccProvedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioAccProvedMouseClicked
        if (jRadioAccProved.isSelected()) {

            if (++clickCount % 2 == 0) {
                buttonGroupAcc.clearSelection();
                jTextAccProvedAcq.setText("");

            }
        }
    }//GEN-LAST:event_jRadioAccProvedMouseClicked

    private void jRadioAccProvedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioAccProvedActionPerformed

    }//GEN-LAST:event_jRadioAccProvedActionPerformed

    private void jTextMiscActivityAcqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextMiscActivityAcqActionPerformed

    }//GEN-LAST:event_jTextMiscActivityAcqActionPerformed

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
        } else if (jTextAcqDestination.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "Please check destination field");
            jTextAcqDestination.requestFocusInWindow();
            jTextAcqDestination.setFocusable(true);
        } else if (jLabelKMDisDB.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "Please enter destination distance");
            jTextDistDest.requestFocusInWindow();
            jTextDistDest.setFocusable(true);
        } else if (jTextPurpose.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "Please check day activity field");
            jTextPurpose.requestFocusInWindow();
            jTextPurpose.setFocusable(true);

        } else if (jTextFieldBudget.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "Please check budget code field");
            jTextFieldBudget.requestFocusInWindow();
            jTextFieldBudget.setFocusable(true);

        } else {
            // jTableActivityAcq.setEnabled(true);
            java.util.Date jDateActivity = jDateChooserActivityAcq.getDate();
            jLabelAcqPayBack.setText("");
            jLabelAcqAmtPayBack.setText("");
            jLabelBankAccNo.setText("");
            jTextPaidAmt.setText("0.00");
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
            model.addRow(new Object[]{dateDayActivity, jTextAcqDestination.getText(), jLabelKMDisDB.getText(), jTextPurpose.getText(),
                jTextFieldBudget.getText(), jTextAcqBreakfast.getText(), jTextAcqLunch.getText(), jTextAcqDinner.getText(), jTextAcqIncidental.getText(),
                jTextMiscActivityAcq.getText(), jTextMiscAmtAcq.getText(), jTextAccUnprovedAcq.getText(),
                Double.toString((Double.parseDouble(jTextAcqBreakfast.getText()) + Double.parseDouble(jTextAcqLunch.getText())
                + Double.parseDouble(jTextAcqDinner.getText()) + Double.parseDouble(jTextAcqIncidental.getText())
                + Double.parseDouble(jTextMiscAmtAcq.getText()) + Double.parseDouble(jTextAccProvedAcq.getText())
                + Double.parseDouble(jTextAccUnprovedAcq.getText())))});

            mainPageTotUpdateAcq();
            /**
             * **** updating general segment
             */

            jDateChooserActivityAcq.setDate(null);
            jTextDistDest.setText("");
            jLabelKMDis.setVisible(false);
            jTextDistDest.setVisible(true);
            jLabelKMDisDB.setText("");
            jLabelKMDis.setText("");
            jLabelKMDisDB.setVisible(false);
            jTextPurpose.setText("");
            jTextAcqBreakfast.setText("0.00");
            jTextAcqLunch.setText("0.00");
            jTextAcqDinner.setText("0.00");
            jTextAcqIncidental.setText("0.00");
            jTextMiscAmtAcq.setText("0.00");
            jTextAccUnprovedAcq.setText("0.00");
            jTextMiscActivityAcq.setText("");
            jTextAcqDestination.setText("");
            jTextFieldBudget.setText("");
            buttonGroupLunch.clearSelection();
            jCheckBreakfast.setSelected(false);
            jRadioLunch3.setSelected(false);
            jRadioLunch5.setSelected(false);
            jRadioLunch10.setSelected(false);
            jCheckDinner.setSelected(false);
            jCheckIncidental.setSelected(false);

            jRadioAccUnproved.setSelected(false);

        }
    }//GEN-LAST:event_jButtonAddActivityActionPerformed

    private void jButtonOkFacility1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkFacility1ActionPerformed
        jTextFieldBudget.setText(jComboBudgetCode.getSelectedItem().toString());
        jDialogBudget.setVisible(false);
    }//GEN-LAST:event_jButtonOkFacility1ActionPerformed

    private void jButtonCancelBudgetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelBudgetActionPerformed
        jDialogBudget.setVisible(false);
    }//GEN-LAST:event_jButtonCancelBudgetActionPerformed

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
            String offnam = jLabelAcqOffice.getText();
            String provnam = jLabelAcqProvince.getText();
            if ("S".equals(jLabelSerial.getText())) {
                st.executeQuery("SELECT BudDesc"
                        + "  FROM [ClaimsAppSysZvandiri].[dbo].[BudCodeTab] WHERE Province ='" + provnam + "'");
            } else {
                st.executeQuery("SELECT BudDesc"
                        + "  FROM [ClaimsAppSysZvandiri].[dbo].[BudCodeTab] WHERE (Province ='" + provnam + "'"
                        + "and Office='" + offnam + "')");
            }
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

    private void jButtonDeleteAtt5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteAtt5ActionPerformed
        jLabelImgFile5.setIcon(null);
    }//GEN-LAST:event_jButtonDeleteAtt5ActionPerformed

    private void jButtonChooseAtt5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonChooseAtt5ActionPerformed
        imgChooser5();
    }//GEN-LAST:event_jButtonChooseAtt5ActionPerformed

    private void jButtonDeleteAtt4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteAtt4ActionPerformed
        jLabelImgFile4.setIcon(null);
    }//GEN-LAST:event_jButtonDeleteAtt4ActionPerformed

    private void jButtonChooseAtt4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonChooseAtt4ActionPerformed
        imgChooser4();
    }//GEN-LAST:event_jButtonChooseAtt4ActionPerformed

    private void jCheckReturnSameDayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckReturnSameDayActionPerformed
        if (jCheckReturnSameDay.isSelected()) {
            jCheckBreakfast.setEnabled(false);
            jRadioLunch5.setEnabled(false);
            jRadioLunch10.setEnabled(false);
            jCheckDinner.setEnabled(false);
            jCheckIncidental.setEnabled(false);
            jRadioAccUnproved.setEnabled(false);
            jRadioAccProved.setEnabled(false);
        } else if (!(jCheckReturnSameDay.isSelected())) {
            jCheckBreakfast.setEnabled(true);
            jRadioLunch5.setEnabled(true);
            jRadioLunch10.setEnabled(true);
            jCheckDinner.setEnabled(true);
            jCheckIncidental.setEnabled(true);
            jRadioAccUnproved.setEnabled(true);
            jRadioAccProved.setEnabled(true);
        }
    }//GEN-LAST:event_jCheckReturnSameDayActionPerformed

    private void jTextVehRegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextVehRegActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextVehRegActionPerformed

    private void jButtonDeleteDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteDetailsActionPerformed
        deleteFileAtt();
        fileTripDel = "Y";


    }//GEN-LAST:event_jButtonDeleteDetailsActionPerformed

    private void jButtonGetDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGetDetailsActionPerformed
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

        fileTripChange = "Y";
    }//GEN-LAST:event_jButtonGetDetailsActionPerformed

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

    private void jMenuItemSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSubmitActionPerformed
//        saveAcqRegisterCheck();
    }//GEN-LAST:event_jMenuItemSubmitActionPerformed

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

    private void jMenuItemUserCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemUserCreateActionPerformed
        new JFrameFixedUserCreation(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemUserCreateActionPerformed

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
            java.util.logging.Logger.getLogger(JFrameAppEditAcquittal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameAppEditAcquittal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameAppEditAcquittal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameAppEditAcquittal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new JFrameAppEditAcquittal().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel addition;
    private javax.swing.ButtonGroup buttonGroupAcc;
    private javax.swing.ButtonGroup buttonGroupAccAcq;
    private javax.swing.ButtonGroup buttonGroupLunch;
    private javax.swing.ButtonGroup buttonGroupSpecial;
    private javax.swing.JButton jButtonAddActivity;
    private javax.swing.JButton jButtonAuthAllCancel;
    private javax.swing.JButton jButtonAuthAllOk;
    private javax.swing.JButton jButtonAuthCancel;
    private javax.swing.JButton jButtonAuthOk;
    private javax.swing.JButton jButtonBankCancel;
    private javax.swing.JButton jButtonBankCancel1;
    private javax.swing.JButton jButtonBankOk;
    private javax.swing.JButton jButtonBankOk1;
    private javax.swing.JButton jButtonBnkUpd;
    private javax.swing.JButton jButtonCancelBudget;
    private javax.swing.JButton jButtonCancelFacility;
    private javax.swing.JButton jButtonChooseAtt1;
    private javax.swing.JButton jButtonChooseAtt2;
    private javax.swing.JButton jButtonChooseAtt3;
    private javax.swing.JButton jButtonChooseAtt4;
    private javax.swing.JButton jButtonChooseAtt5;
    private javax.swing.JButton jButtonDeleteAtt1;
    private javax.swing.JButton jButtonDeleteAtt2;
    private javax.swing.JButton jButtonDeleteAtt3;
    private javax.swing.JButton jButtonDeleteAtt4;
    private javax.swing.JButton jButtonDeleteAtt5;
    private javax.swing.JButton jButtonDeleteDetails;
    private javax.swing.JButton jButtonDeleted;
    private javax.swing.JButton jButtonGetDetails;
    private javax.swing.JButton jButtonOkFacility;
    private javax.swing.JButton jButtonOkFacility1;
    private javax.swing.JCheckBox jCheckBreakfast;
    private javax.swing.JCheckBox jCheckDinner;
    private javax.swing.JCheckBox jCheckIncidental;
    private javax.swing.JCheckBox jCheckReturnSameDay;
    private javax.swing.JComboBox<String> jComboAuthNam;
    private javax.swing.JComboBox<String> jComboAuthNamAll;
    private javax.swing.JComboBox<String> jComboBankNam;
    private javax.swing.JComboBox<String> jComboBudgetCode;
    private javax.swing.JComboBox<String> jComboDistrictFacility;
    private javax.swing.JComboBox<String> jComboFacility;
    private javax.swing.JComboBox<String> jComboProvinceFacility;
    private javax.swing.JComboBox<String> jComboWk;
    private com.toedter.calendar.JDateChooser jDateChooserActivityAcq;
    private com.toedter.calendar.JDateChooser jDateTripFrom;
    private com.toedter.calendar.JDateChooser jDateTripTo;
    private javax.swing.JDialog jDialogAuthority;
    private javax.swing.JDialog jDialogAuthorityAll;
    private javax.swing.JDialog jDialogBudget;
    private javax.swing.JDialog jDialogChgPaid;
    private javax.swing.JDialog jDialogFacility;
    private javax.swing.JDialog jDialogWaiting;
    private javax.swing.JDialog jDialogWaitingEmail;
    private javax.swing.JDialog jDialogWkDisplay;
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
    private javax.swing.JLabel jLabelAcqAppTotReqCostCleared;
    private javax.swing.JLabel jLabelAcqBankName;
    private javax.swing.JLabel jLabelAcqBreakFastSubTot;
    private javax.swing.JLabel jLabelAcqBreakFastSubTotBal;
    private javax.swing.JLabel jLabelAcqBudCode;
    private javax.swing.JLabel jLabelAcqDateAcq;
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
    private javax.swing.JLabel jLabelAcqSerial;
    private javax.swing.JLabel jLabelAcqWk4;
    private javax.swing.JLabel jLabelAcqYear;
    private javax.swing.JLabel jLabelActDate;
    private javax.swing.JLabel jLabelActLinePurpose;
    private javax.swing.JLabel jLabelActMainPurpose;
    private javax.swing.JLabel jLabelAllAcq;
    private javax.swing.JLabel jLabelAllBal;
    private javax.swing.JLabel jLabelAllReq;
    private javax.swing.JLabel jLabelAppTotCleared;
    private javax.swing.JLabel jLabelAppTotReq;
    private javax.swing.JLabel jLabelAutName;
    private javax.swing.JLabel jLabelAutNameAll;
    private javax.swing.JLabel jLabelBank;
    private javax.swing.JLabel jLabelBankAcc;
    private javax.swing.JLabel jLabelBankAccNo;
    private javax.swing.JLabel jLabelBankAccNo1;
    private javax.swing.JLabel jLabelBankHeader;
    private javax.swing.JLabel jLabelBankHeader1;
    private javax.swing.JLabel jLabelBankMsg;
    private javax.swing.JLabel jLabelBankMsg1;
    private javax.swing.JLabel jLabelBankName;
    private javax.swing.JLabel jLabelBreakFastSub;
    private javax.swing.JLabel jLabelBreakFastSubTot;
    private javax.swing.JLabel jLabelBreakfast;
    private javax.swing.JLabel jLabelBreakfast1;
    private javax.swing.JLabel jLabelBreakfast2;
    private javax.swing.JLabel jLabelBreakfast3;
    private javax.swing.JLabel jLabelBudgetCode;
    private javax.swing.JLabel jLabelBudgetCode1;
    private javax.swing.JLabel jLabelDateRange;
    private javax.swing.JLabel jLabelDestination;
    private javax.swing.JLabel jLabelDinnerSub;
    private javax.swing.JLabel jLabelDinnerSubTot;
    private javax.swing.JLabel jLabelDisDistrict;
    private javax.swing.JLabel jLabelDisFacilty;
    private javax.swing.JLabel jLabelDisProvince;
    private javax.swing.JLabel jLabelDistDest;
    private javax.swing.JLabel jLabelDistrict;
    private javax.swing.JLabel jLabelDistrictFacility;
    private javax.swing.JLabel jLabelEmp;
    private javax.swing.JLabel jLabelEmpNum;
    private javax.swing.JLabel jLabelFacDist;
    private javax.swing.JLabel jLabelFacility;
    private javax.swing.JLabel jLabelFrom;
    private javax.swing.JLabel jLabelGenDate;
    private javax.swing.JLabel jLabelGenLogNam;
    private javax.swing.JLabel jLabelGenLogNam1;
    private javax.swing.JLabel jLabelGenTime;
    private javax.swing.JLabel jLabelHeader;
    private javax.swing.JLabel jLabelHeader1;
    private javax.swing.JLabel jLabelHeader2;
    private javax.swing.JLabel jLabelHeaderAll;
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
    private javax.swing.JLabel jLabelKMDis;
    private javax.swing.JLabel jLabelKMDisDB;
    private javax.swing.JLabel jLabelKm;
    private javax.swing.JLabel jLabelLineDate;
    private javax.swing.JLabel jLabelLineDate1;
    private javax.swing.JLabel jLabelLineDate2;
    private javax.swing.JLabel jLabelLineDate3;
    private javax.swing.JLabel jLabelLineDate4;
    private javax.swing.JLabel jLabelLineDate5;
    private javax.swing.JLabel jLabelLineDate7;
    private javax.swing.JLabel jLabelLineDate8;
    private javax.swing.JLabel jLabelLineLogNam;
    private javax.swing.JLabel jLabelLineLogNam1;
    private javax.swing.JLabel jLabelLineLogNam10;
    private javax.swing.JLabel jLabelLineLogNam4;
    private javax.swing.JLabel jLabelLineLogNam5;
    private javax.swing.JLabel jLabelLineLogNam7;
    private javax.swing.JLabel jLabelLineLogNam8;
    private javax.swing.JLabel jLabelLineLogNam9;
    private javax.swing.JLabel jLabelLineTime;
    private javax.swing.JLabel jLabelLineTime1;
    private javax.swing.JLabel jLabelLineTime2;
    private javax.swing.JLabel jLabelLineTime3;
    private javax.swing.JLabel jLabelLineTime4;
    private javax.swing.JLabel jLabelLineTime5;
    private javax.swing.JLabel jLabelLineTime7;
    private javax.swing.JLabel jLabelLineTime8;
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
    private javax.swing.JLabel jLabelMisc;
    private javax.swing.JLabel jLabelMiscAcq;
    private javax.swing.JLabel jLabelMiscReq;
    private javax.swing.JLabel jLabelMiscSubTot;
    private javax.swing.JLabel jLabelMscBal;
    private javax.swing.JLabel jLabelMscSub;
    private javax.swing.JLabel jLabelNamTravel;
    private javax.swing.JLabel jLabelNamVisited;
    private javax.swing.JLabel jLabelNotMsg;
    private javax.swing.JLabel jLabelNotMsgAll;
    private javax.swing.JLabel jLabelNum;
    private javax.swing.JLabel jLabelNum1;
    private javax.swing.JLabel jLabelOffice;
    private javax.swing.JLabel jLabelPaidAmt;
    private javax.swing.JLabel jLabelPay;
    private javax.swing.JLabel jLabelProved;
    private javax.swing.JLabel jLabelProvince;
    private javax.swing.JLabel jLabelProvinceFacility;
    private javax.swing.JLabel jLabelRegDateAcq;
    private javax.swing.JLabel jLabelRemain;
    private javax.swing.JLabel jLabelSepDetails;
    private javax.swing.JLabel jLabelSerial;
    private javax.swing.JLabel jLabelSpecial;
    private javax.swing.JLabel jLabelSpecial1;
    private javax.swing.JLabel jLabelSpecial2;
    private javax.swing.JLabel jLabelSpecial3;
    private javax.swing.JLabel jLabelSubAcc;
    private javax.swing.JLabel jLabelTo;
    private javax.swing.JLabel jLabelTripDetails;
    private javax.swing.JLabel jLabelTripReport;
    private javax.swing.JLabel jLabelUnproved;
    private javax.swing.JLabel jLabelVehReg;
    private javax.swing.JLabel jLabelWk1DialogBudget;
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
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelAcqDocAtt1;
    private javax.swing.JPanel jPanelAcqDocAtt2;
    private javax.swing.JPanel jPanelAcqDocAtt3;
    private javax.swing.JPanel jPanelAcqELog;
    private javax.swing.JPanel jPanelAcquittal;
    private javax.swing.JPanel jPanelAtt1;
    private javax.swing.JPanel jPanelAtt2;
    private javax.swing.JPanel jPanelAtt3;
    private javax.swing.JPanel jPanelAtt4;
    private javax.swing.JPanel jPanelCaptureDetails;
    private javax.swing.JPanel jPanelDocAttach4;
    private javax.swing.JPanel jPanelDocAttach5;
    private javax.swing.JPanel jPanelGen;
    private javax.swing.JPanel jPanelReport;
    private javax.swing.JPanel jPanelReportDetails;
    private javax.swing.JPanel jPanelRequest;
    private javax.swing.JRadioButton jRadioAccProved;
    private javax.swing.JRadioButton jRadioAccUnproved;
    private javax.swing.JRadioButton jRadioLunch10;
    private javax.swing.JRadioButton jRadioLunch3;
    private javax.swing.JRadioButton jRadioLunch5;
    private javax.swing.JRadioButton jRadioNormal;
    private javax.swing.JRadioButton jRadioSpecial;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JScrollPane jScrollPaneAtt1;
    private javax.swing.JScrollPane jScrollPaneAtt2;
    private javax.swing.JScrollPane jScrollPaneAtt3;
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
    private javax.swing.JTable jTableActivityAcq;
    private javax.swing.JTable jTableActivityReq;
    private javax.swing.JTable jTableTripDetails;
    private javax.swing.JTextField jTextAccProvedAcq;
    private javax.swing.JTextField jTextAccUnprovedAcq;
    private javax.swing.JTextField jTextAcqBreakfast;
    private javax.swing.JTextField jTextAcqDestination;
    private javax.swing.JTextField jTextAcqDinner;
    private javax.swing.JTextField jTextAcqIncidental;
    private javax.swing.JTextField jTextAcqLunch;
    private javax.swing.JTextField jTextAcqRegNum;
    private javax.swing.JTextArea jTextAreaNamTravel;
    private javax.swing.JTextArea jTextAreaNamVisited;
    private javax.swing.JTextArea jTextAreaTripReport;
    private javax.swing.JTextField jTextDistDest;
    private javax.swing.JTextField jTextFieldBudget;
    private javax.swing.JTextField jTextMiscActivityAcq;
    private javax.swing.JTextField jTextMiscAmtAcq;
    private javax.swing.JTextField jTextPaidAmt;
    private javax.swing.JTextField jTextPurpose;
    private javax.swing.JTextField jTextVehReg;
    // End of variables declaration//GEN-END:variables
}
