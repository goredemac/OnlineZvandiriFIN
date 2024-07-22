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
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import static com.itextpdf.text.html.HtmlTags.SRC;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileOutputStream;
import java.util.Locale;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.timeHost;
import utils.connCred;
import utils.connSaveFile;
import utils.savePDFToDB;
import utils.StockVehicleMgt;

/**
 *
 * @author cgoredema
 */
public class JFrameAppAcquittalView extends javax.swing.JFrame {

    connCred c = new connCred();
    timeHost tH = new timeHost();
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
    int existCountWkNum = 0;
    int countWkNum = 0;
    SimpleDateFormat df = new SimpleDateFormat("yyyy");
    DefaultTableModel model;
    DefaultTableModel modelAcq;
    DefaultTableModel modelTrip;
    DefaultTableModel modelMonAttRep;
    String sendTo, createUsrNam, breakfastAll, lunchAll, lunchNPAll, lunchPAll, dinnerAll,
            incidentalAll, unProvedAll, date1, date2, usrnam, searchRef, authUsrNam, authUsrNamAll,
            authNam1, authNam2, regDocVersion, oldRegDocVersion, usrGrp, imgAttVer, curSta, checkRef, wkNum,
            paraone, paratwo, parathree, wkNumRep, wk1Amt, wk2Amt, wk3Amt, rejComments, rejStatus,
            searchSerial, refRM, empOff;
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

    /**
     * Creates new form JFrameTabApp1
     */
    public JFrameAppAcquittalView() {
        initComponents();
        showDate();
        showTime();
        computerName();
        allowanceRate();
        model = (DefaultTableModel) jTableActivityReqAcq.getModel();
        modelAcq = (DefaultTableModel) jTableActivityAcq.getModel();
        modelMonAttRep = (DefaultTableModel) jTableMonDistAcquitalAttRep.getModel();

    }

    public JFrameAppAcquittalView(String usrLogNum) {
        initComponents();
//        showDate();
        showTime();
        computerName();
        allowanceRate();
        jRadioParticpant.setVisible(false);
        jRadioParticpant.setSelected(false);
        jRadioNormal.setSelected(false);
        jRadioDistrict.setVisible(false);
        jRadioDistrict.setSelected(false);
        jLabelSerial.setText("R");
        model = (DefaultTableModel) jTableActivityReqAcq.getModel();
        modelAcq = (DefaultTableModel) jTableActivityAcq.getModel();
        modelTrip = (DefaultTableModel) jTableTripDetails.getModel();
        modelMonAttRep = (DefaultTableModel) jTableMonDistAcquitalAttRep.getModel();
        jTableTripDetails.getColumnModel().getColumn(1).setMinWidth(0);
        jTableTripDetails.getColumnModel().getColumn(1).setMaxWidth(0);
        jLabelEmp.setText(usrLogNum);
        
        jLabelEmp.setVisible(false);
        jMenuItemAcqSchGen.setEnabled(false);
        jTextAreaNamTravel.setLineWrap(true);
        jTextAreaNamTravel.setWrapStyleWord(true);
        jTextAreaNamTravel.setEditable(false);
//        jLabelAcqAppTotPlannedCost.setVisible(false);
       // jLabelAppTotPlannedReq.setVisible(false);
        jTabbedPaneAcqAtt.setEnabledAt(0, true);
        jTabbedPaneAcqAtt.setEnabledAt(1, false);
        jTabbedPaneAcqAtt.setEnabledAt(2, true);
        jTabbedPaneAcqAtt.setEnabledAt(3, true);
        jTabbedPaneAcqAtt.setEnabledAt(4, true);
        jTabbedPaneAcqAtt.setEnabledAt(5, false);
        jTabbedPaneAcqAtt.setEnabledAt(6, false);
        jTabbedPaneAcqAtt.setTitleAt(0, "Activity Summary Report");
//            jTabbedPaneAcqAtt.setTitleAt(1, "E-Log Book");
        jTabbedPaneAcqAtt.setTitleAt(2, "Vehicle Log Sheet");
        jTabbedPaneAcqAtt.setTitleAt(3, "Proven Expenses");
        jTabbedPaneAcqAtt.setTitleAt(4, "Other e.g. Log Book Extra Page");
        jTabbedPaneAcqAtt.setTitleAt(5, "");
        jTabbedPaneAcqAtt.setTitleAt(6, "");
//        jRadioParticpant.setVisible(false);
        try {
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
        jLabelLineDate6.setText(tH.internetDate);
        jLabelLineDate7.setText(tH.internetDate);

        findUser();
        findUserGrp();

        if (!"Administrator".equals(usrGrp)) {
            jMenuItemUserProfUpd.setEnabled(false);
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
        jLabelLineDate6.setText(s.format(d));
        jLabelLineDate5.setText(s.format(d));
        jLabelLineDate4.setText(s.format(d));
        jLabelLineDate3.setText(s.format(d));
        jLabelLineDate2.setText(s.format(d));
        jLabelLineDate7.setText(s.format(d));

    }

    void supName() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

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

    void refreshTab() {
        int itemCount = jComboWk.getItemCount();

        for (int i = 0; i < itemCount; i++) {
            jComboWk.removeItemAt(0);
        }

//        jTextAcqRegNum.setText("");
        jLabelAcqEmpNum.setText("");
        jLabelRegDateAcq.setText("");
        jLabelAcqRefNum.setText("");
        jLabelAcqYear.setText("");
        jLabelAcqSerial.setText("");
        jLabelAcqEmpNam.setText("");
        jLabelAcqEmpTitle.setText("");
        jLabelAcqProvince.setText("");
        jLabelAcqOffice.setText("");
        jLabelAcqBankName.setText("");
        jLabelAcqActMainPurpose.setText("");
        jLabelAcqAccNum.setText("");
        jLabelRejection.setText("");
        jLabelRejectComments.setText("");
        jLabelStatusView.setText("");
        jLabelAcqAppTotReqCostCleared.setText("");
        jLabelAcqWk.setText("");
        jTextAreaNamTravel.setText("");
        jLabelAcqRegDate.setText("");
        jLabelImgFile1.setIcon(null);
        jLabelImgFile2.setIcon(null);
        jLabelImgFile3.setIcon(null);
        jLabelImgFile4.setIcon(null);
        jLabelImgFile5.setIcon(null);
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        while (modelAcq.getRowCount() > 0) {
            modelAcq.removeRow(0);
        }
        while (modelMonAttRep.getRowCount() > 0) {
            modelMonAttRep.removeRow(0);
        }
        jLabelAcqAppTotPlannedCost.setText("0.00");
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
        jLabelAcqAppTotReqCostCleared.setText("0.00");
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
        jLabelAppTotCleared.setText("Total Claimed");

    }

    void allowanceRate() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
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

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void getTrip() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();
            System.out.println("three");
            st.executeQuery("SELECT ACT_PROJ,VEH_REG_NUM,EMP_NUM,DEPART_LOC,ARRIVAL_LOC,TRIP_PUR,"
                    + "DEPART_DATE,DEPART_TIME,DEPART_MILAGE,ARRIVAL_DATE,ARRIVAL_TIME,ARRIVAL_MILAGE,"
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

    void refNumUpdate() {
        try {

            String sql1 = "update [ClaimsAppSysZvandiri].[dbo].[refNumTab] set "
                    + "REF_NUM ='" + jLabelAcqRefNum.getText() + "' where REF_YEAR ='" + finyear + "' and SERIAL = 'A'";

            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            pst = conn.prepareStatement(sql1);
            pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    void regInitCheck() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();

//            st.executeQuery("SELECT COUNT(*) FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] "
//                    + "where concat(PREV_SERIAL,PREV_REF_NUM) = 'R" + jTextAcqRegNum.getText() + "'");
            st.executeQuery("SELECT count(*) FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a,[ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] "
                    + "b where ( a.REF_NUM = b.REF_NUM and a.DOC_VER =b.DOC_VER) "
                    + "and  a.ACT_REC_STA = 'Q' and "
                    + "concat(PREV_SERIAL,PREV_REF_NUM) = '" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "'");

            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                count = Integer.parseInt(rs.getString(1));

            }

            // conn.close();
            System.out.println("cccount " + count);
            if (count > 0) {
                if (("R".equals(jLabelSerial.getText())) && (existCountWkNum > 1)) {
                    jDialogWkDisplay.setVisible(true);
                } else {

                    wkNum = "1";

                    while (model.getRowCount() > 0) {
                        model.removeRow(0);
                    }
                    while (modelAcq.getRowCount() > 0) {
                        modelAcq.removeRow(0);
                    }

//                    if ("R".equals(jLabelSerial.getText())) {
                    countWk();
                    fetchdata();

//                    }
                    mainPageTotUpdate();
                    mainPageTotUpdateAcq();
                    fetchImgCount();
                    imgCount();
                   

                }
            } else {
                JOptionPane.showMessageDialog(this, "Reference number is invalid. Please check your reference number.");

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
        for (int i = 0; i < jTableActivityReqAcq.getRowCount(); i++) {
            double breakfastamount = Double.parseDouble((String) jTableActivityReqAcq.getValueAt(i, 10));
            breakfastsubtotal += breakfastamount;

        }
        jLabelBreakFastSubTot.setText(String.format("%.2f", breakfastsubtotal));

        lunchsubtotal = 0;
        for (int i = 0; i < jTableActivityReqAcq.getRowCount(); i++) {
            double lunchamount = Double.parseDouble((String) jTableActivityReqAcq.getValueAt(i, 11));
            lunchsubtotal += lunchamount;
        }
        jLabelLunchSubTot.setText(String.format("%.2f", lunchsubtotal));

        dinnersubtotal = 0;
        for (int i = 0; i < jTableActivityReqAcq.getRowCount(); i++) {
            double dinneramount = Double.parseDouble((String) jTableActivityReqAcq.getValueAt(i, 12));
            dinnersubtotal += dinneramount;
        }
        jLabelDinnerSubTot.setText(String.format("%.2f", dinnersubtotal));

        incidentalsubtotal = 0;
        for (int i = 0; i < jTableActivityReqAcq.getRowCount(); i++) {
            double incidentalamount = Double.parseDouble((String) jTableActivityReqAcq.getValueAt(i, 13));
            incidentalsubtotal += incidentalamount;
        }
        jLabelIncidentalSubTot.setText(String.format("%.2f", incidentalsubtotal));

        miscSubTot = 0;
        for (int i = 0; i < jTableActivityReqAcq.getRowCount(); i++) {
            double miscamount = Double.parseDouble((String) jTableActivityReqAcq.getValueAt(i, 15));
            miscSubTot += miscamount;
        }
        jLabelMiscSubTot.setText(String.format("%.2f", miscSubTot));

        unprovedSubTot = 0;
        for (int i = 0; i < jTableActivityReqAcq.getRowCount(); i++) {
            double unprovedamount = Double.parseDouble((String) jTableActivityReqAcq.getValueAt(i, 16));
            unprovedSubTot += unprovedamount;
        }
        jLabelAccUnprovedSubTot.setText(String.format("%.2f", unprovedSubTot));

        provedSubTot = 0;
        for (int i = 0; i < jTableActivityReqAcq.getRowCount(); i++) {
            double provedamount = Double.parseDouble((String) jTableActivityReqAcq.getValueAt(i, 17));
            provedSubTot += provedamount;
        }
        jLabelAccProvedSubTot.setText(String.format("%.2f", provedSubTot));

        allTotal = 0;
        allTotal = unprovedSubTot + provedSubTot + miscSubTot + incidentalsubtotal
                + dinnersubtotal + lunchsubtotal + breakfastsubtotal;

        jLabelAcqAppTotPlannedCost.setText(String.format("%.2f", allTotal));

    }

    void mainPageTotUpdateAcq() {
        breakfastsubtotalAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double breakfastamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 10));
            breakfastsubtotalAcq += breakfastamountAcq;

        }
        jLabelAcqBreakFastSubTot.setText(Double.toString(breakfastsubtotalAcq));

        lunchsubtotalAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double lunchamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 11));
            lunchsubtotalAcq += lunchamountAcq;

        }
        jLabelAcqLunchSubTot.setText(Double.toString(lunchsubtotalAcq));

        dinnersubtotalAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double dinneramountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 12));
            dinnersubtotalAcq += dinneramountAcq;
        }
        jLabelAcqDinnerSubTot.setText(Double.toString(dinnersubtotalAcq));

        incidentalsubtotalAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double incidentalamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 13));
            incidentalsubtotalAcq += incidentalamountAcq;
        }
        jLabelAcqIncidentalSubTot.setText(Double.toString(incidentalsubtotalAcq));

        miscSubTotAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double miscamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 15));
            miscSubTotAcq += miscamountAcq;
        }
        jLabelAcqMiscSubTot.setText(Double.toString(miscSubTotAcq));

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
        //    jLabelAcqAppTotReqCost.setText(Double.toString(allTotal - allTotalAcq));
        double totDiff = allTotal - allTotalAcq;

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

    void countWk() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT max(PLAN_WK) FROM [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab]"
                    + " where concat(SERIAL,REF_NUM) =  'R" + jTextAcqRegNum.getText() + "'");

            ResultSet rs = st.getResultSet();

            while (rs.next()) {

                countWkNum = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
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
                wk1Amt = r.getString(1);
            }

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
                wk2Amt = r.getString(1);
            }

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
                wk3Amt = r.getString(1);
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
            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            Statement st3 = conn.createStatement();
            Statement st4 = conn.createStatement();
            Statement st5 = conn.createStatement();
            Statement st6 = conn.createStatement();
            Statement st7 = conn.createStatement();
            Statement st8 = conn.createStatement();

            st2.executeQuery("SELECT count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a "
                    + "join [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] b "
                    + "on a.SERIAL = b.SERIAL and a.REF_NUM=b.REF_NUM and a.DOC_VER=b.DOC_VER "
                    + "and a.ACT_REC_STA=b.ACT_REC_STA join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] c "
                    + "on a.SERIAL = c.SERIAL and a.REF_NUM=c.REF_NUM and a.DOC_VER=b.DOC_VER "
                    + "and a.ACT_REC_STA=c.ACT_REC_STA where c.DOC_STATUS='HODApprove' "
                    + "and a.ACT_REC_STA = 'A' and concat(a.SERIAL,a.REF_NUM)='" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "' "
                    + "and a.REF_NUM in "
                    + "(Select REF_NUM from [ClaimsAppSysZvandiri].[dbo].[BatRunTab] where STATUS = 'Paid') ");

            ResultSet r2 = st2.getResultSet();

            while (r2.next()) {
                numsearchRef = r2.getInt(1);
            }

            if (numsearchRef == 0 && "R".equals(jLabelSerial.getText())) {

                JOptionPane.showMessageDialog(this, "Reference number is invalid . Please check your reference number.");

                new JFrameMain(jLabelEmp.getText()).setVisible(true);
                setVisible(false);

            } else {

                try {

                    System.out.println("dd cc " + jLabelSerial.getText() + jTextAcqRegNum.getText());

                    st4.executeQuery("SELECT distinct a.REF_YEAR,a.SERIAL, a.REF_NUM,a.REF_DAT "
                            + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a join "
                            + "[ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] b on a.SERIAL = b.SERIAL "
                            + "and a.REF_NUM=b.REF_NUM and a.DOC_VER=b.DOC_VER and a.ACT_REC_STA=b.ACT_REC_STA "
                            + "join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] c on a.SERIAL = c.SERIAL "
                            + "and a.REF_NUM=c.REF_NUM and a.DOC_VER=b.DOC_VER and a.ACT_REC_STA=c.ACT_REC_STA "
                            + "where a.ACT_REC_STA = 'Q'   and "
                            + "concat(a.PREV_SERIAL,a.PREV_REF_NUM) = '" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "'");

                    ResultSet r4 = st4.getResultSet();

                    while (r4.next()) {
                        jLabelAcqYear.setText(r4.getString(1));
                        jLabelAcqSerial.setText(r4.getString(2));
                        jLabelAcqRefNum.setText(r4.getString(3));
                        jLabelAcqRegDate.setText(r4.getString(4));

                    }

                    st1.executeQuery("select SERIAL,REF_YEAR, REF_NUM,REF_DAT,EMP_NUM,EMP_NAM,"
                            + "EMP_TTL,EMP_PROV,EMP_OFF,EMP_BNK_NAM,ACC_NUM,ACT_MAIN_PUR,"
                            + "ACT_TOT_AMT,PREV_REF_NUM,PREV_REF_DAT "
                            + "FROM ClaimsAppSysZvandiri.dbo.ClaimAppGenTab "
                            + "where concat(SERIAL,REF_NUM) ='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "' "
                            + " and ACT_REC_STA = 'Q' ");

                    ResultSet r1 = st1.getResultSet();

                    while (r1.next()) {
                        jLabelAcqSerial.setText(r1.getString(1));
                        jLabelAcqYear.setText(r1.getString(2));
                        jLabelAcqRefNum.setText(r1.getString(3));
                        jLabelAcqYear.setText(r1.getString(3));
                        jLabelAcqRegDate.setText(r1.getString(4));
                        jLabelAcqYear.setVisible(false);
                        jLabelAcqEmpNum.setText(r1.getString(5));
                        jLabelAcqEmpNam.setText(r1.getString(6));
                        jLabelAcqEmpTitle.setText(r1.getString(7));
                        jLabelAcqProvince.setText(r1.getString(8));
                        jLabelAcqOffice.setText(r1.getString(9));
                        jLabelAcqBankName.setText(r1.getString(10));
                        jLabelAcqAccNum.setText(r1.getString(11));
                        jLabelAcqActMainPurpose.setText(r1.getString(12));
                        jLabelAcqAppTotReqCostCleared.setText(r1.getString(13));
                        //jLabelReqRefNum.setText(r1.getString(14));
                        jLabelRegDateAcq.setText(r1.getString(15));

                    }

                    fetchItmData(jLabelSerial.getText() + jTextAcqRegNum.getText(), wkNum, "A", "model");

                    fetchItmData(jLabelAcqSerial.getText() + jLabelAcqRefNum.getText(), wkNum, "Q", "modelAq");

                    getRepDet(jLabelAcqSerial.getText() + jLabelAcqRefNum.getText());

                    getColSta(jLabelAcqSerial.getText() + jLabelAcqRefNum.getText());
                    if ("MA".equals(jLabelAcqSerial.getText())) {
                        fetchReqItmData();
                    }
                    fetchMonDistAttDocRep();

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
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

//    void fetchMonDistAttDocRep(String refNum) {
//        try {
//            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
//                    + "DataBaseName=ClaimsAppSys;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
//
//            Statement st = conn.createStatement();
//            st.executeQuery("SELECT ACT_ITM,fileName ,attDesc  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimReportAttDocTab] "
//                    + " where concat(SERIAL,REF_NUM) ='" + refNum + "'  and ACT_REC_STA = 'A' ");
//
//            ResultSet r = st.getResultSet();
//            while (r.next()) {
//                modelMonAttRep.insertRow(modelMonAttRep.getRowCount(), new Object[]{r.getString(1), r.getString(2),
//                    r.getString(3)});
//
//            }
//
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
    
    void fetchMonDistAttDocRep() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            System.out.println("run " + checkRef);
            Statement st = conn.createStatement();
            st.executeQuery("SELECT ACT_ITM,fileName ,attDesc  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimReportAttDocTab] "
                    + " where concat(SERIAL,REF_NUM) ='" +jLabelAcqSerial.getText()+jLabelAcqRefNum.getText() + "'  and ACT_REC_STA = 'Q' ");

            ResultSet r = st.getResultSet();
            while (r.next()) {
                modelMonAttRep.insertRow(modelMonAttRep.getRowCount(), new Object[]{r.getString(1), r.getString(2),
                    r.getString(3)});

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdataWk() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st2 = conn.createStatement();
            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            Statement st3 = conn.createStatement();
            Statement st4 = conn.createStatement();
            Statement st5 = conn.createStatement();
            Statement st6 = conn.createStatement();
            Statement st7 = conn.createStatement();
            Statement st8 = conn.createStatement();

            st2.executeQuery("SELECT count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a "
                    + "join [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] b "
                    + "on a.SERIAL = b.SERIAL and a.REF_NUM=b.REF_NUM and a.DOC_VER=b.DOC_VER "
                    + "and a.ACT_REC_STA=b.ACT_REC_STA join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] c "
                    + "on a.SERIAL = c.SERIAL and a.REF_NUM=c.REF_NUM and a.DOC_VER=b.DOC_VER "
                    + "and a.ACT_REC_STA=c.ACT_REC_STA where c.DOC_STATUS='HODApprove' "
                    + "and a.ACT_REC_STA = 'A' and concat(a.SERIAL,a.REF_NUM)='" + jLabelSerial.getText() + jTextAcqRegNum.getText() + "' "
                    + "and a.REF_NUM in "
                    + "(Select REF_NUM from [ClaimsAppSysZvandiri].[dbo].[BatRunTab] where STATUS = 'Paid')");

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

                    st1.executeQuery("select SERIAL,REF_YEAR, REF_NUM,REF_DAT,EMP_NUM,EMP_NAM,"
                            + "EMP_TTL,EMP_PROV,EMP_OFF,EMP_BNK_NAM,ACC_NUM,ACT_MAIN_PUR,"
                            + "ACT_TOT_AMT,PREV_REF_NUM,PREV_REF_DAT "
                            + "FROM ClaimsAppSysZvandiri.dbo.ClaimAppGenTab "
                            + "where concat(SERIAL,REF_NUM) = '" + searchRef + "'"
                            + " and ACT_REC_STA = 'Q' ");

                    ResultSet r1 = st1.getResultSet();

                    while (r1.next()) {
                        jLabelAcqSerial.setText(r1.getString(1));
                        jLabelAcqYear.setText(r1.getString(2));
                        jLabelAcqRefNum.setText(r1.getString(3));
                        jLabelAcqYear.setText(r1.getString(3));
                        jLabelAcqRegDate.setText(r1.getString(4));
                        jLabelAcqYear.setVisible(false);
                        jLabelAcqEmpNum.setText(r1.getString(5));
                        jLabelAcqEmpNam.setText(r1.getString(6));
                        jLabelAcqEmpTitle.setText(r1.getString(7));
                        jLabelAcqProvince.setText(r1.getString(8));
                        jLabelAcqOffice.setText(r1.getString(9));
                        jLabelAcqBankName.setText(r1.getString(10));
                        jLabelAcqAccNum.setText(r1.getString(11));
                        jLabelAcqActMainPurpose.setText(r1.getString(12));
                        jLabelAcqAppTotReqCostCleared.setText(r1.getString(13));
                        //jLabelReqRefNum.setText(r1.getString(14));
                        jLabelRegDateAcq.setText(r1.getString(15));

                    }

                    fetchItmData(jLabelSerial.getText() + jTextAcqRegNum.getText(), wkNum, "A", "model");

                    fetchItmData(jLabelAcqSerial.getText() + jLabelAcqRefNum.getText(), wkNum, "Q", "modelAcq");

                    getColSta(jLabelAcqSerial.getText() + jLabelAcqRefNum.getText());

                    getRepDet(jLabelAcqSerial.getText() + jLabelAcqRefNum.getText());

                    fetchMonDistAttDocRep();

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchItmData(String refNo, String wkNo, String actSta, String tabModel) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            //  String model = tabModel;
            Statement st = conn.createStatement();
            System.out.println("val " + refNo + " " + actSta + " " + wkNo + " " + tabModel);
            st.executeQuery("SELECT  *  FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                    + "  where concat(SERIAL,REF_NUM)='" + refNo + "' "
                    + "and ACT_REC_STA='" + actSta + "' and PLAN_WK='" + wkNo + "'"
                    + "order by ACT_DATE");

            ResultSet r = st.getResultSet();
            if ("model".equals(tabModel)) {
                while (r.next()) {
                    model.insertRow(model.getRowCount(), new Object[]{r.getString(5),
                        r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                        r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                        r.getString(20), r.getString(21), r.getString(22), r.getString(23)});
                }
            } else {
                while (r.next()) {
                    modelAcq.insertRow(modelAcq.getRowCount(), new Object[]{r.getString(5),
                        r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                        r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                        r.getString(20), r.getString(21), r.getString(22), r.getString(23)});
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void getColSta(String refNo) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            st.executeQuery("SELECT distinct b.USR_ACTION,b.REJECT_COMMENTS FROM ClaimsAppSysZvandiri.dbo.ClaimAppGenTab a,[ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] b\n"
                    + "where  ( a.serial=b.serial and a.REF_NUM = b.REF_NUM and a.DOC_VER=b.DOC_VER) and\n"
                    + "concat(a.SERIAL,a.REF_NUM)='" + refNo + "' \n"
                    + "and a.ACT_REC_STA = 'Q' ");

            ResultSet r = st.getResultSet();

            while (r.next()) {

                rejComments = r.getString(2);

                jLabelStatusView.setText(r.getString(1));
                rejStatus = r.getString(1);

                if ("Acquittal- HOD Approved".equals(jLabelStatusView.getText())) {
                    jLabelStatusView.setVisible(true);
                    jPanelStatusView.setVisible(true);
                    jLabelStatusView.setForeground(new java.awt.Color(255, 255, 255));
                    jPanelStatusView.setVisible(true);
                } else {
                    jLabelStatusView.setVisible(true);
                    jPanelStatusView.setVisible(true);
                    jLabelStatusView.setForeground(new java.awt.Color(255, 0, 0));
                    jPanelStatusView.setVisible(true);
                }

                if (("Acquittal- Supervisor Rejected".equals(rejStatus)) || ("Acquittal - Finance Officer Rejected.".equals(rejStatus))
                        || ("Acquittal- Accounts Manager Rejected".equals(rejStatus)) || ("Acquittal- Head Rejected".equals(rejStatus))) {
                    jLabelRejection.setVisible(true);
                    jLabelRejection.setText("Reason for Rejection");
                    jLabelRejectComments.setVisible(true);
                    jLabelRejectComments.setText(rejComments);

                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void getRepDet(String refNo) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT NAM_TRAVEL,PLAN_WK FROM"
                    + " [ClaimsAppSysZvandiri].[dbo].[ClaimTripReport] where concat"
                    + "(SERIAL,REF_NUM)='" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "'"
                    + "and ACT_REC_STA='Q' ");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                jTextAreaNamTravel.setText(r.getString(1));
                wkNumRep = r.getString(2);

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

            if ("S".equals(jLabelSerial.getText())) {
                imgDisFile1 = jLabelSerial.getText() + jTextAcqRegNum.getText();
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

            if ("S".equals(jLabelSerial.getText())) {
                imgDisFile2 = jLabelSerial.getText() + jTextAcqRegNum.getText();
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

            if ("S".equals(jLabelSerial.getText())) {
                imgDisFile3 = jLabelSerial.getText() + jTextAcqRegNum.getText();
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

            if ("S".equals(jLabelSerial.getText())) {
                imgDisFile4 = jLabelSerial.getText() + jTextAcqRegNum.getText();
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

            if ("S".equals(jLabelSerial.getText())) {
                imgDisFile5 = jLabelSerial.getText() + jTextAcqRegNum.getText();
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

    void fetchImgCount() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            String imgCount = "";
            if ("S".equals(jLabelSerial.getText())) {
                imgCount = jLabelSerial.getText() + jTextAcqRegNum.getText();
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

    void acqWeek() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

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

    void finAM() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();

            st1.executeQuery("SELECT concat(SERIAL,REF_NUM) FROM [ClaimsAppSysZvandiri].[dbo].[ClaimMeetGenTab] "
                    + "where PREV_SERIAL = 'RM' and PREV_REF_NUM = '" + jTextAcqRegNum.getText() + "' "
                    + "and ACT_REC_STA='Q'");

            ResultSet rs1 = st1.getResultSet();

            while (rs1.next()) {
                refRM = rs1.getString(1);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void imgCount() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            st.executeQuery("SELECT count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimDocAttachTab] "
                    + "where concat(SERIAL,REF_NUM)="
                    + "'" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "'");

            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                imgAttCount = rs.getInt(1);
            }

            if (imgAttCount > 0) {
                st1.executeQuery("SELECT max(IMG_VERSION)+1, max(IMG_VERSION)  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimDocAttachTab] "
                        + "where concat(SERIAL,REF_NUM)="
                        + "'" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "'");

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
        jDialogWkDisplay = new javax.swing.JDialog();
        jPanel10 = new javax.swing.JPanel();
        jLabelBankHeader = new javax.swing.JLabel();
        jLabelBankMsg = new javax.swing.JLabel();
        jButtonBankOk = new javax.swing.JButton();
        jButtonBankCancel = new javax.swing.JButton();
        jComboWk = new javax.swing.JComboBox<>();
        jLabelBankAccNo = new javax.swing.JLabel();
        buttonGroupSpecial = new javax.swing.ButtonGroup();
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
        jLabel35 = new javax.swing.JLabel();
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
        jLabelAcqRefNum = new javax.swing.JLabel();
        jLabelAcqYear = new javax.swing.JLabel();
        jLabelNumAcq = new javax.swing.JLabel();
        jLabelAcqSerial = new javax.swing.JLabel();
        jLabelEmp = new javax.swing.JLabel();
        jLabelPay = new javax.swing.JLabel();
        jLabelRejection = new javax.swing.JLabel();
        jLabelRejectComments = new javax.swing.JLabel();
        jPanelStatusView = new javax.swing.JPanel();
        jLabelStatusView = new javax.swing.JLabel();
        jLabelAcqWk = new javax.swing.JLabel();
        jRadioNormal = new javax.swing.JRadioButton();
        jRadioDistrict = new javax.swing.JRadioButton();
        jLabelActNam = new javax.swing.JLabel();
        jLabelAct = new javax.swing.JLabel();
        jLabelAcqRegDate = new javax.swing.JLabel();
        jLabelAppTotCleared = new javax.swing.JLabel();
        jLabelAcqAppTotReqCostCleared = new javax.swing.JLabel();
        jRadioParticpant = new javax.swing.JRadioButton();
        jLabelAppTotPlannedReq = new javax.swing.JLabel();
        jLabelAcqAppTotPlannedCost = new javax.swing.JLabel();
        jPanelRequest = new javax.swing.JPanel();
        jLabelLogo1 = new javax.swing.JLabel();
        jLabelHeaderLine = new javax.swing.JLabel();
        jLabelLineDate = new javax.swing.JLabel();
        jLabelLineTime = new javax.swing.JLabel();
        jLabellogged = new javax.swing.JLabel();
        jLabelLineLogNam = new javax.swing.JLabel();
        jScrollPaneWk8 = new javax.swing.JScrollPane();
        jTableActivityReqAcq = new javax.swing.JTable();
        jPanelAcquittal = new javax.swing.JPanel();
        jLabelLogo2 = new javax.swing.JLabel();
        jLabelHeaderLine1 = new javax.swing.JLabel();
        jLabelLineDate1 = new javax.swing.JLabel();
        jLabelLineTime1 = new javax.swing.JLabel();
        jLabellogged1 = new javax.swing.JLabel();
        jLabelLineLogNam1 = new javax.swing.JLabel();
        jScrollPaneWk9 = new javax.swing.JScrollPane();
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
        jButtonAtt1 = new javax.swing.JButton();
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
        jButtonAtt2 = new javax.swing.JButton();
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
        jButtonAtt3 = new javax.swing.JButton();
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
        jButtonAtt4 = new javax.swing.JButton();
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
        jButtonAtt5 = new javax.swing.JButton();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuNew = new javax.swing.JMenu();
        jMenuItemPlanPerDiem = new javax.swing.JMenuItem();
        jSeparator13 = new javax.swing.JPopupMenu.Separator();
        jMenuItemPerDiemAcquittal = new javax.swing.JMenuItem();
        jSeparator16 = new javax.swing.JPopupMenu.Separator();
        jMenuEdit = new javax.swing.JMenu();
        jMenuMonPlanEdit = new javax.swing.JMenuItem();
        jSeparator20 = new javax.swing.JPopupMenu.Separator();
        jMenuItemClose = new javax.swing.JMenuItem();
        jSeparator31 = new javax.swing.JPopupMenu.Separator();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuRequest = new javax.swing.JMenu();
        jMenuItemSupApp = new javax.swing.JMenuItem();
        jSeparator32 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAccMgrRev = new javax.swing.JMenuItem();
        jSeparator33 = new javax.swing.JPopupMenu.Separator();
        jMenuItemHeadApp = new javax.swing.JMenuItem();
        jSeparator35 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSchPerDiem = new javax.swing.JMenuItem();
        jSeparator23 = new javax.swing.JPopupMenu.Separator();
        jMenuItemView = new javax.swing.JMenuItem();
        jMenuAcquittal = new javax.swing.JMenu();
        jMenuItemAcqSupApp = new javax.swing.JMenuItem();
        jSeparator36 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqAccApp = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqHeadApp = new javax.swing.JMenuItem();
        jSeparator11 = new javax.swing.JPopupMenu.Separator();
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
        jButtonAuthOk.setBounds(180, 190, 80, 25);

        jButtonAuthCancel.setText("Cancel");
        jButtonAuthCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAuthCancelActionPerformed(evt);
            }
        });
        jPanel7.add(jButtonAuthCancel);
        jButtonAuthCancel.setBounds(300, 190, 80, 25);

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
        jButtonAuthAllOk.setBounds(180, 190, 80, 25);

        jButtonAuthAllCancel.setText("Cancel");
        jButtonAuthAllCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAuthAllCancelActionPerformed(evt);
            }
        });
        jPanel8.add(jButtonAuthAllCancel);
        jButtonAuthAllCancel.setBounds(300, 190, 80, 25);

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

        jDialogWkDisplay.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jDialogWkDisplay.setLocation(new java.awt.Point(550, 250));
        jDialogWkDisplay.setMinimumSize(new java.awt.Dimension(290, 180));
        jDialogWkDisplay.setResizable(false);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setMinimumSize(new java.awt.Dimension(290, 180));
        jPanel10.setPreferredSize(new java.awt.Dimension(290, 180));
        jPanel10.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPanel10FocusGained(evt);
            }
        });
        jPanel10.setLayout(null);

        jLabelBankHeader.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelBankHeader.setForeground(new java.awt.Color(0, 0, 204));
        jLabelBankHeader.setText("ACQUITTAL WEEK");
        jPanel10.add(jLabelBankHeader);
        jLabelBankHeader.setBounds(40, 0, 190, 30);

        jLabelBankMsg.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabelBankMsg.setText("Please select week to view");
        jPanel10.add(jLabelBankMsg);
        jLabelBankMsg.setBounds(60, 30, 160, 30);

        jButtonBankOk.setText("Ok ");
        jButtonBankOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBankOkActionPerformed(evt);
            }
        });
        jPanel10.add(jButtonBankOk);
        jButtonBankOk.setBounds(30, 100, 80, 25);

        jButtonBankCancel.setText("Cancel");
        jButtonBankCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBankCancelActionPerformed(evt);
            }
        });
        jPanel10.add(jButtonBankCancel);
        jButtonBankCancel.setBounds(150, 100, 80, 25);

        jComboWk.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jComboWk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboWkActionPerformed(evt);
            }
        });
        jPanel10.add(jComboWk);
        jComboWk.setBounds(30, 60, 210, 30);
        jPanel10.add(jLabelBankAccNo);
        jLabelBankAccNo.setBounds(200, 150, 280, 30);

        javax.swing.GroupLayout jDialogWkDisplayLayout = new javax.swing.GroupLayout(jDialogWkDisplay.getContentPane());
        jDialogWkDisplay.getContentPane().setLayout(jDialogWkDisplayLayout);
        jDialogWkDisplayLayout.setHorizontalGroup(
            jDialogWkDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jDialogWkDisplayLayout.setVerticalGroup(
            jDialogWkDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PERDIEM ACQUITTAL -  VIEWING");
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
        jLabelHeaderGen.setBounds(270, 40, 720, 40);

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
        jLabelGenLogNam.setBounds(1150, 40, 190, 30);

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

        jLabel4.setText("Financial Details");
        jPanelGen.add(jLabel4);
        jLabel4.setBounds(20, 310, 110, 30);

        jLabelActMainPurpose.setText("Activity Main Purpose");
        jPanelGen.add(jLabelActMainPurpose);
        jLabelActMainPurpose.setBounds(20, 360, 130, 30);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPanel1FocusGained(evt);
            }
        });
        jPanel1.setLayout(null);

        jLabelIncidentalSub.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jLabelIncidentalSub.setText("Incidental");
        jPanel1.add(jLabelIncidentalSub);
        jLabelIncidentalSub.setBounds(10, 120, 60, 25);

        jLabelIncidentalSubTot.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelIncidentalSubTot.setText("0.00");
        jPanel1.add(jLabelIncidentalSubTot);
        jLabelIncidentalSubTot.setBounds(100, 120, 50, 25);

        jLabelLunchSub.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jLabelLunchSub.setText("Lunch");
        jPanel1.add(jLabelLunchSub);
        jLabelLunchSub.setBounds(10, 60, 60, 25);

        jLabelDinnerSub.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
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

        jLabelBreakFastSub.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jLabelBreakFastSub.setText("Breakfast");
        jPanel1.add(jLabelBreakFastSub);
        jLabelBreakFastSub.setBounds(10, 30, 60, 25);

        jLabelAcqBreakFastSubTot.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqBreakFastSubTot.setForeground(new java.awt.Color(255, 0, 0));
        jLabelAcqBreakFastSubTot.setText("0.00");
        jPanel1.add(jLabelAcqBreakFastSubTot);
        jLabelAcqBreakFastSubTot.setBounds(170, 30, 50, 25);

        jLabelLunchSubTot.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLunchSubTot.setText("0.00");
        jPanel1.add(jLabelLunchSubTot);
        jLabelLunchSubTot.setBounds(100, 60, 50, 25);

        jLabelDinnerSubTot.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelDinnerSubTot.setText("0.00");
        jPanel1.add(jLabelDinnerSubTot);
        jLabelDinnerSubTot.setBounds(100, 90, 50, 25);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel1.add(jSeparator3);
        jSeparator3.setBounds(150, 20, 5, 120);

        jLabelBreakFastSubTot.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelBreakFastSubTot.setText("0.00");
        jPanel1.add(jLabelBreakFastSubTot);
        jLabelBreakFastSubTot.setBounds(100, 30, 50, 25);

        jLabelAcqLunchSubTot.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqLunchSubTot.setForeground(new java.awt.Color(255, 51, 51));
        jLabelAcqLunchSubTot.setText("0.00");
        jPanel1.add(jLabelAcqLunchSubTot);
        jLabelAcqLunchSubTot.setBounds(170, 60, 50, 25);

        jLabelAcqDinnerSubTot.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqDinnerSubTot.setForeground(new java.awt.Color(255, 51, 51));
        jLabelAcqDinnerSubTot.setText("0.00");
        jPanel1.add(jLabelAcqDinnerSubTot);
        jLabelAcqDinnerSubTot.setBounds(170, 90, 50, 25);

        jLabelAcqIncidentalSubTot.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqIncidentalSubTot.setForeground(new java.awt.Color(255, 51, 51));
        jLabelAcqIncidentalSubTot.setText("0.00");
        jPanel1.add(jLabelAcqIncidentalSubTot);
        jLabelAcqIncidentalSubTot.setBounds(170, 120, 50, 25);

        jLabelAllReq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAllReq.setForeground(new java.awt.Color(0, 102, 0));
        jLabelAllReq.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelAllReq.setText("Req");
        jPanel1.add(jLabelAllReq);
        jLabelAllReq.setBounds(90, 5, 50, 25);

        jLabelAllAcq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAllAcq.setForeground(new java.awt.Color(255, 0, 0));
        jLabelAllAcq.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelAllAcq.setText("Acq");
        jPanel1.add(jLabelAllAcq);
        jLabelAllAcq.setBounds(160, 5, 60, 25);

        jLabelAllBal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAllBal.setText("Balance");
        jPanel1.add(jLabelAllBal);
        jLabelAllBal.setBounds(240, 5, 60, 25);

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel1.add(jSeparator6);
        jSeparator6.setBounds(220, 20, 2, 120);

        jLabelAcqBreakFastSubTotBal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqBreakFastSubTotBal.setText("0.00");
        jPanel1.add(jLabelAcqBreakFastSubTotBal);
        jLabelAcqBreakFastSubTotBal.setBounds(230, 30, 50, 25);

        jLabelAcqLunchSubTotBal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqLunchSubTotBal.setText("0.00");
        jPanel1.add(jLabelAcqLunchSubTotBal);
        jLabelAcqLunchSubTotBal.setBounds(230, 60, 50, 25);

        jLabelAcqDinnerSubTotBal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqDinnerSubTotBal.setText("0.00");
        jPanel1.add(jLabelAcqDinnerSubTotBal);
        jLabelAcqDinnerSubTotBal.setBounds(230, 90, 50, 25);

        jLabelAcqIncidentalSubTotBal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqIncidentalSubTotBal.setText("0.00");
        jPanel1.add(jLabelAcqIncidentalSubTotBal);
        jLabelAcqIncidentalSubTotBal.setBounds(230, 120, 50, 25);

        jPanelGen.add(jPanel1);
        jPanel1.setBounds(10, 420, 320, 150);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(null);

        jLabelMiscSubTot.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jLabelMiscSubTot.setText("0.00");
        jPanel3.add(jLabelMiscSubTot);
        jLabelMiscSubTot.setBounds(110, 30, 50, 25);

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel29.setText("Miscellaneous ");
        jPanel3.add(jLabel29);
        jLabel29.setBounds(8, 5, 90, 25);

        jLabelMscSub.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jLabelMscSub.setText("Miscellaneous");
        jPanel3.add(jLabelMscSub);
        jLabelMscSub.setBounds(8, 30, 80, 25);

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel3.add(jSeparator4);
        jSeparator4.setBounds(168, 20, 5, 120);

        jLabelAcqMiscSubTot.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jLabelAcqMiscSubTot.setForeground(new java.awt.Color(255, 51, 51));
        jLabelAcqMiscSubTot.setText("0.00");
        jPanel3.add(jLabelAcqMiscSubTot);
        jLabelAcqMiscSubTot.setBounds(180, 30, 50, 25);

        jLabelMiscReq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelMiscReq.setForeground(new java.awt.Color(0, 102, 0));
        jLabelMiscReq.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelMiscReq.setText("Req");
        jPanel3.add(jLabelMiscReq);
        jLabelMiscReq.setBounds(100, 5, 60, 25);

        jLabelMiscAcq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelMiscAcq.setForeground(new java.awt.Color(255, 0, 0));
        jLabelMiscAcq.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelMiscAcq.setText("Acq");
        jPanel3.add(jLabelMiscAcq);
        jLabelMiscAcq.setBounds(180, 5, 50, 25);

        jSeparator7.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel3.add(jSeparator7);
        jSeparator7.setBounds(230, 20, 2, 120);

        jLabelMscBal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelMscBal.setText("Balance");
        jPanel3.add(jLabelMscBal);
        jLabelMscBal.setBounds(250, 5, 50, 25);

        jLabelAcqMiscSubTotBal.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jLabelAcqMiscSubTotBal.setForeground(new java.awt.Color(51, 51, 51));
        jLabelAcqMiscSubTotBal.setText("0.00");
        jPanel3.add(jLabelAcqMiscSubTotBal);
        jLabelAcqMiscSubTotBal.setBounds(250, 30, 50, 25);

        jPanelGen.add(jPanel3);
        jPanel3.setBounds(350, 420, 320, 150);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(null);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setText("Accomodation");
        jPanel4.add(jLabel11);
        jLabel11.setBounds(10, 5, 90, 20);

        jLabelAccUnprovedSubTot.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAccUnprovedSubTot.setText("0.00");
        jPanel4.add(jLabelAccUnprovedSubTot);
        jLabelAccUnprovedSubTot.setBounds(110, 30, 50, 25);

        jLabelAccProvedSub.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jLabelAccProvedSub.setText("Proved");
        jPanel4.add(jLabelAccProvedSub);
        jLabelAccProvedSub.setBounds(10, 60, 70, 25);

        jLabelAccProvedSubTot.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAccProvedSubTot.setText("0.00");
        jPanel4.add(jLabelAccProvedSubTot);
        jLabelAccProvedSubTot.setBounds(110, 60, 50, 25);

        jLabelAccUnprovedSub.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jLabelAccUnprovedSub.setText("Unproved");
        jPanel4.add(jLabelAccUnprovedSub);
        jLabelAccUnprovedSub.setBounds(8, 30, 70, 25);

        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel4.add(jSeparator5);
        jSeparator5.setBounds(160, 20, 5, 120);

        jLabelAcqAccUnprovedSubTotBal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqAccUnprovedSubTotBal.setText("0.00");
        jPanel4.add(jLabelAcqAccUnprovedSubTotBal);
        jLabelAcqAccUnprovedSubTotBal.setBounds(250, 30, 50, 25);

        jLabelAcqAccProvedSubTot.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
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
        jLabelAccReq.setBounds(100, 5, 50, 25);

        jLabelAcqAccUnprovedSubTot.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqAccUnprovedSubTot.setForeground(new java.awt.Color(255, 51, 51));
        jLabelAcqAccUnprovedSubTot.setText("0.00");
        jPanel4.add(jLabelAcqAccUnprovedSubTot);
        jLabelAcqAccUnprovedSubTot.setBounds(170, 30, 50, 25);

        jLabelAcqAccProvedSubTotBal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqAccProvedSubTotBal.setText("0.00");
        jPanel4.add(jLabelAcqAccProvedSubTotBal);
        jLabelAcqAccProvedSubTotBal.setBounds(250, 60, 50, 25);

        jPanelGen.add(jPanel4);
        jPanel4.setBounds(690, 420, 320, 150);

        jLabel35.setText("Miscellaneous");
        jPanelGen.add(jLabel35);
        jLabel35.setBounds(8, 30, 70, 25);

        jLabelOffice.setText("Office");
        jPanelGen.add(jLabelOffice);
        jLabelOffice.setBounds(370, 230, 70, 30);

        jLabelAcqAccNum.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqAccNum);
        jLabelAcqAccNum.setBounds(500, 260, 130, 30);

        jLabelACC.setText("Account No.");
        jPanelGen.add(jLabelACC);
        jLabelACC.setBounds(370, 260, 80, 30);

        jLabelNum.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNum.setText("Request No.");
        jPanelGen.add(jLabelNum);
        jLabelNum.setBounds(290, 125, 90, 30);

        jLabelSerial.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelSerial);
        jLabelSerial.setBounds(390, 125, 30, 30);

        jLabelAcqActMainPurpose.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqActMainPurpose);
        jLabelAcqActMainPurpose.setBounds(160, 360, 750, 30);

        jLabelAcqProvince.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqProvince);
        jLabelAcqProvince.setBounds(130, 230, 210, 30);

        jLabelAcqOffice.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqOffice);
        jLabelAcqOffice.setBounds(500, 230, 210, 30);

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
        jTextAcqRegNum.setBounds(430, 125, 50, 30);

        jLabelAcqEmpNum.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqEmpNum);
        jLabelAcqEmpNum.setBounds(130, 200, 60, 30);

        jLabelRegDateAcq.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelRegDateAcq);
        jLabelRegDateAcq.setBounds(500, 125, 130, 30);

        jLabelAcqRefNum.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelGen.add(jLabelAcqRefNum);
        jLabelAcqRefNum.setBounds(840, 125, 60, 30);

        jLabelAcqYear.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelGen.add(jLabelAcqYear);
        jLabelAcqYear.setBounds(740, 125, 50, 30);

        jLabelNumAcq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNumAcq.setForeground(new java.awt.Color(0, 102, 0));
        jLabelNumAcq.setText("Acquittal No.");
        jPanelGen.add(jLabelNumAcq);
        jLabelNumAcq.setBounds(650, 125, 90, 30);

        jLabelAcqSerial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelGen.add(jLabelAcqSerial);
        jLabelAcqSerial.setBounds(800, 125, 30, 30);
        jPanelGen.add(jLabelEmp);
        jLabelEmp.setBounds(960, 80, 80, 30);

        jLabelPay.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabelPay.setForeground(new java.awt.Color(255, 180, 40));
        jPanelGen.add(jLabelPay);
        jLabelPay.setBounds(1050, 620, 80, 30);

        jLabelRejection.setFont(new java.awt.Font("Times New Roman", 1, 10)); // NOI18N
        jLabelRejection.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelRejection);
        jLabelRejection.setBounds(10, 580, 200, 25);

        jLabelRejectComments.setFont(new java.awt.Font("Times New Roman", 1, 10)); // NOI18N
        jLabelRejectComments.setForeground(new java.awt.Color(255, 255, 0));
        jPanelGen.add(jLabelRejectComments);
        jLabelRejectComments.setBounds(240, 580, 690, 25);

        jPanelStatusView.setBackground(new java.awt.Color(0, 0, 0));

        jLabelStatusView.setBackground(new java.awt.Color(0, 0, 0));
        jLabelStatusView.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelStatusView.setForeground(new java.awt.Color(255, 255, 0));

        javax.swing.GroupLayout jPanelStatusViewLayout = new javax.swing.GroupLayout(jPanelStatusView);
        jPanelStatusView.setLayout(jPanelStatusViewLayout);
        jPanelStatusViewLayout.setHorizontalGroup(
            jPanelStatusViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelStatusViewLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelStatusView, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelStatusViewLayout.setVerticalGroup(
            jPanelStatusViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelStatusViewLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabelStatusView, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanelGen.add(jPanelStatusView);
        jPanelStatusView.setBounds(970, 160, 370, 30);

        jLabelAcqWk.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelAcqWk.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqWk);
        jLabelAcqWk.setBounds(510, 80, 330, 30);

        buttonGroupSpecial.add(jRadioNormal);
        jRadioNormal.setFont(new java.awt.Font("Tahoma", 3, 10)); // NOI18N
        jRadioNormal.setForeground(new java.awt.Color(255, 255, 255));
        jRadioNormal.setSelected(true);
        jRadioNormal.setText("National Office");
        jRadioNormal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioNormalActionPerformed(evt);
            }
        });
        jPanelGen.add(jRadioNormal);
        jRadioNormal.setBounds(20, 125, 110, 30);

        buttonGroupSpecial.add(jRadioDistrict);
        jRadioDistrict.setFont(new java.awt.Font("Tahoma", 3, 10)); // NOI18N
        jRadioDistrict.setText("District Office");
        jRadioDistrict.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioDistrictActionPerformed(evt);
            }
        });
        jPanelGen.add(jRadioDistrict);
        jRadioDistrict.setBounds(150, 125, 110, 30);

        jLabelActNam.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelActNam.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelActNam);
        jLabelActNam.setBounds(1080, 118, 270, 30);

        jLabelAct.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        jLabelAct.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAct);
        jLabelAct.setBounds(830, 10, 180, 30);

        jLabelAcqRegDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelGen.add(jLabelAcqRegDate);
        jLabelAcqRegDate.setBounds(920, 125, 120, 30);

        jLabelAppTotCleared.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelAppTotCleared.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAppTotCleared.setText("Total Cleared  ");
        jPanelGen.add(jLabelAppTotCleared);
        jLabelAppTotCleared.setBounds(1040, 540, 170, 25);

        jLabelAcqAppTotReqCostCleared.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelAcqAppTotReqCostCleared.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAcqAppTotReqCostCleared.setText("0");
        jLabelAcqAppTotReqCostCleared.setMinimumSize(null);
        jPanelGen.add(jLabelAcqAppTotReqCostCleared);
        jLabelAcqAppTotReqCostCleared.setBounds(1230, 540, 90, 25);

        buttonGroupSpecial.add(jRadioParticpant);
        jRadioParticpant.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jRadioParticpant.setForeground(new java.awt.Color(0, 153, 0));
        jRadioParticpant.setText("Participants All");
        jRadioParticpant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioParticpantActionPerformed(evt);
            }
        });
        jPanelGen.add(jRadioParticpant);
        jRadioParticpant.setBounds(240, 80, 80, 30);

        jLabelAppTotPlannedReq.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelAppTotPlannedReq.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAppTotPlannedReq.setText("Total Planned Amount");
        jPanelGen.add(jLabelAppTotPlannedReq);
        jLabelAppTotPlannedReq.setBounds(1040, 500, 180, 30);

        jLabelAcqAppTotPlannedCost.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelAcqAppTotPlannedCost.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAcqAppTotPlannedCost.setText("0");
        jPanelGen.add(jLabelAcqAppTotPlannedCost);
        jLabelAcqAppTotPlannedCost.setBounds(1230, 500, 90, 30);

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
        jLabelHeaderLine.setBounds(280, 40, 700, 40);

        jLabelLineDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate.setText("29 November 2018");
        jPanelRequest.add(jLabelLineDate);
        jLabelLineDate.setBounds(1060, 0, 130, 30);

        jLabelLineTime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime.setText("15:20:30");
        jPanelRequest.add(jLabelLineTime);
        jLabelLineTime.setBounds(1200, 0, 100, 30);

        jLabellogged.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged.setText("Logged In");
        jPanelRequest.add(jLabellogged);
        jLabellogged.setBounds(1070, 40, 100, 30);

        jLabelLineLogNam.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam.setText("User Name");
        jPanelRequest.add(jLabelLineLogNam);
        jLabelLineLogNam.setBounds(1150, 40, 190, 30);

        jTableActivityReqAcq.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPaneWk8.setViewportView(jTableActivityReqAcq);

        jPanelRequest.add(jScrollPaneWk8);
        jScrollPaneWk8.setBounds(10, 150, 1340, 490);

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
        jLabelHeaderLine1.setBounds(290, 40, 750, 40);

        jLabelLineDate1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate1.setText("29 November 2018");
        jPanelAcquittal.add(jLabelLineDate1);
        jLabelLineDate1.setBounds(1090, 0, 110, 30);

        jLabelLineTime1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime1.setText("15:20:30");
        jPanelAcquittal.add(jLabelLineTime1);
        jLabelLineTime1.setBounds(1230, 0, 80, 30);

        jLabellogged1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged1.setText("Logged In");
        jPanelAcquittal.add(jLabellogged1);
        jLabellogged1.setBounds(1090, 40, 80, 30);

        jLabelLineLogNam1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam1.setText("User Name");
        jPanelAcquittal.add(jLabelLineLogNam1);
        jLabelLineLogNam1.setBounds(1170, 40, 190, 30);

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
        jScrollPaneWk9.setBounds(10, 120, 1340, 440);

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
        jLabelHeaderLine2.getAccessibleContext().setAccessibleName("400");

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
                .addContainerGap(312, Short.MAX_VALUE)
                .addComponent(jLabelImgFile1, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
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
        jScrollPaneAtt1.setBounds(160, 115, 1190, 520);

        jButtonAtt1.setBackground(new java.awt.Color(0, 102, 0));
        jButtonAtt1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonAtt1.setForeground(new java.awt.Color(255, 255, 255));
        jButtonAtt1.setText("<html><center>Print </center> Attachment 1</html>");
        jButtonAtt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAtt1ActionPerformed(evt);
            }
        });
        jPanelAcqDocAtt1.add(jButtonAtt1);
        jButtonAtt1.setBounds(10, 120, 140, 60);

        jTabbedPaneAcqAtt.addTab("Attachment 1", jPanelAcqDocAtt1);
        jPanelAcqDocAtt1.getAccessibleContext().setAccessibleName("400");

        jPanelAcqDocAtt2.setBackground(new java.awt.Color(226, 255, 226));
        jPanelAcqDocAtt2.setMinimumSize(new java.awt.Dimension(1270, 671));
        jPanelAcqDocAtt2.setLayout(null);

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
                .addComponent(jLabelImgFile2, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                .addContainerGap(228, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(jLabelImgFile2, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jScrollPaneAtt2.setViewportView(jPanel19);

        jPanelAcqDocAtt2.add(jScrollPaneAtt2);
        jScrollPaneAtt2.setBounds(160, 110, 1190, 520);

        jButtonAtt2.setBackground(new java.awt.Color(0, 102, 0));
        jButtonAtt2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonAtt2.setForeground(new java.awt.Color(255, 255, 255));
        jButtonAtt2.setText("<html><center>Print </center> Attachment 2</html>");
        jButtonAtt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAtt2ActionPerformed(evt);
            }
        });
        jPanelAcqDocAtt2.add(jButtonAtt2);
        jButtonAtt2.setBounds(20, 140, 120, 50);

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
                .addComponent(jLabelImgFile3, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                .addContainerGap(245, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addComponent(jLabelImgFile3, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jScrollPaneAtt3.setViewportView(jPanel21);

        jPanelAcqDocAtt3.add(jScrollPaneAtt3);
        jScrollPaneAtt3.setBounds(150, 115, 1190, 520);

        jButtonAtt3.setBackground(new java.awt.Color(0, 102, 0));
        jButtonAtt3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonAtt3.setForeground(new java.awt.Color(255, 255, 255));
        jButtonAtt3.setText("<html><center>Print </center> Attachment 3</html>");
        jButtonAtt3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAtt3ActionPerformed(evt);
            }
        });
        jPanelAcqDocAtt3.add(jButtonAtt3);
        jButtonAtt3.setBounds(20, 120, 110, 60);

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
                .addComponent(jLabelImgFile4, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
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
        jScrollPaneAtt4.setBounds(150, 115, 1190, 520);

        jButtonAtt4.setBackground(new java.awt.Color(0, 102, 0));
        jButtonAtt4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonAtt4.setForeground(new java.awt.Color(255, 255, 255));
        jButtonAtt4.setText("<html><center>Print </center> Attachment 4</html>");
        jButtonAtt4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAtt4ActionPerformed(evt);
            }
        });
        jPanelAcqDocAtt4.add(jButtonAtt4);
        jButtonAtt4.setBounds(10, 140, 120, 50);

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
                .addComponent(jLabelImgFile5, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                .addContainerGap(240, Short.MAX_VALUE))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addComponent(jLabelImgFile5, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jScrollPaneAtt5.setViewportView(jPanel26);

        jPanelAcqDocAtt5.add(jScrollPaneAtt5);
        jScrollPaneAtt5.setBounds(150, 115, 1190, 520);

        jButtonAtt5.setBackground(new java.awt.Color(0, 102, 0));
        jButtonAtt5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonAtt5.setForeground(new java.awt.Color(255, 255, 255));
        jButtonAtt5.setText("<html><center>Print </center> Attachment 5</html>");
        jButtonAtt5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAtt5ActionPerformed(evt);
            }
        });
        jPanelAcqDocAtt5.add(jButtonAtt5);
        jButtonAtt5.setBounds(20, 140, 120, 50);

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
        jMenuFile.add(jSeparator20);

        jMenuItemClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemClose.setText("Close");
        jMenuItemClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemClose);
        jMenuFile.add(jSeparator31);

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
        jMenuRequest.add(jSeparator32);

        jMenuItemAccMgrRev.setText("Finance Team Approval");
        jMenuItemAccMgrRev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAccMgrRevActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemAccMgrRev);
        jMenuRequest.add(jSeparator33);

        jMenuItemHeadApp.setText("Project HOD Approval");
        jMenuItemHeadApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemHeadAppActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemHeadApp);
        jMenuRequest.add(jSeparator35);

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
        jMenuAcquittal.add(jSeparator36);

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
            .addComponent(jTabbedPaneAppSys, javax.swing.GroupLayout.DEFAULT_SIZE, 1360, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneAppSys, javax.swing.GroupLayout.PREFERRED_SIZE, 720, Short.MAX_VALUE)
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


    private void jTextAcqRegNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAcqRegNumActionPerformed
//        if ("S".equals(jLabelSerial.getText())) {
//            jTabbedPaneAcqAtt.setEnabledAt(0, true);
//            jTabbedPaneAcqAtt.setEnabledAt(1, true);
//            jTabbedPaneAcqAtt.setEnabledAt(2, true);
//            jTabbedPaneAcqAtt.setEnabledAt(3, true);
//            jTabbedPaneAcqAtt.setEnabledAt(4, true);
//            jTabbedPaneAcqAtt.setEnabledAt(5, true);
//            jTabbedPaneAcqAtt.setEnabledAt(6, true);
//
//            jTabbedPaneAcqAtt.setTitleAt(0, "Activity Summary Report");
//            jTabbedPaneAcqAtt.setTitleAt(1, "E-Log Book");
//            jTabbedPaneAcqAtt.setTitleAt(2, "Vehicle Log Sheet");
//            jTabbedPaneAcqAtt.setTitleAt(3, "Attendance Register");
//            jTabbedPaneAcqAtt.setTitleAt(4, "Requistion Voucher");
//            jTabbedPaneAcqAtt.setTitleAt(5, "Proof of Active Bank Account");
//            jTabbedPaneAcqAtt.setTitleAt(6, "Other e.g. Log Book, Expenses");
//        } else {
//            jTabbedPaneAcqAtt.setEnabledAt(0, true);
//            jTabbedPaneAcqAtt.setEnabledAt(1, false);
//            jTabbedPaneAcqAtt.setEnabledAt(2, true);
//            jTabbedPaneAcqAtt.setEnabledAt(3, true);
//            jTabbedPaneAcqAtt.setEnabledAt(4, true);
//            jTabbedPaneAcqAtt.setEnabledAt(5, false);
//            jTabbedPaneAcqAtt.setEnabledAt(6, false);
//            jTabbedPaneAcqAtt.setTitleAt(0, "Activity Summary Report");
////            jTabbedPaneAcqAtt.setTitleAt(1, "E-Log Book");
//            jTabbedPaneAcqAtt.setTitleAt(2, "Vehicle Log Sheet");
//            jTabbedPaneAcqAtt.setTitleAt(3, "Proven Expenses");
//            jTabbedPaneAcqAtt.setTitleAt(4, "Other e.g. Log Book Extra Page");
//            jTabbedPaneAcqAtt.setTitleAt(5, "");
//            jTabbedPaneAcqAtt.setTitleAt(6, "");
//
//        }
//        if ("RM".equals(jLabelSerial.getText())) {
//            finAM();
//            new JFrameMeetView(refRM, jLabelEmp.getText()).setVisible(true);
//            setVisible(false);
//        } else {
        refreshTab();
        acqWeek();
        regInitCheck();
//        }
    }//GEN-LAST:event_jTextAcqRegNumActionPerformed

    private void jTextAcqRegNumFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextAcqRegNumFocusLost
        //  refreshTab();
        //regInitCheck();
        //findAuthorisation();
    }//GEN-LAST:event_jTextAcqRegNumFocusLost

    private void jTextAcqRegNumKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAcqRegNumKeyPressed


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

        //  mainPageTotUpdateAcq();
    }//GEN-LAST:event_jTextAcqRegNumFocusGained

    private void jButtonAuthOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAuthOkActionPerformed

        jDialogAuthority.setVisible(false);
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

        mainPageTotUpdateAcq();
        /**
         * **** updating general segment
         */

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

    private void jButtonBankCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBankCancelActionPerformed
        jDialogWkDisplay.setVisible(false);
        jLabelPay.setText("");


    }//GEN-LAST:event_jButtonBankCancelActionPerformed

    private void jComboWkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboWkActionPerformed

    }//GEN-LAST:event_jComboWkActionPerformed

    private void jPanel10FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel10FocusGained

    }//GEN-LAST:event_jPanel10FocusGained

    private void jButtonAtt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAtt1ActionPerformed
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Requsition");

        job.setPrintable(new Printable() {
            public int print(Graphics pg, PageFormat pf, int pageNum) {

                if (pageNum > 0) {
                    return Printable.NO_SUCH_PAGE;
                }
                Graphics2D g2 = (Graphics2D) pg;
                g2.translate(pf.getImageableX(), pf.getImageableY());
                g2.scale(1, 1.2);

                jLabelImgFile1.paint(g2);
                return Printable.PAGE_EXISTS;

            }
        });

        if (!(jLabelImgFile1.getIcon() == null)) {
            boolean ok = job.printDialog();
            if (ok) {
                try {
                    job.print();
                } catch (PrinterException ex) {

                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No attachment to print. Please check and try again.");
        }
    }//GEN-LAST:event_jButtonAtt1ActionPerformed

    private void jButtonAtt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAtt2ActionPerformed
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Requsition");

        job.setPrintable(new Printable() {
            public int print(Graphics pg, PageFormat pf, int pageNum) {

                if (pageNum > 0) {
                    return Printable.NO_SUCH_PAGE;
                }
                Graphics2D g2 = (Graphics2D) pg;
                g2.translate(pf.getImageableX(), pf.getImageableY());
                g2.scale(1, 1.2);

                jLabelImgFile2.paint(g2);
                return Printable.PAGE_EXISTS;

            }
        });

        if (!(jLabelImgFile2.getIcon() == null)) {
            boolean ok = job.printDialog();
            if (ok) {
                try {
                    job.print();
                } catch (PrinterException ex) {

                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No attachment to print. Please check and try again.");
        }
    }//GEN-LAST:event_jButtonAtt2ActionPerformed

    private void jButtonAtt3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAtt3ActionPerformed
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Requsition");

        job.setPrintable(new Printable() {
            public int print(Graphics pg, PageFormat pf, int pageNum) {

                if (pageNum > 0) {
                    return Printable.NO_SUCH_PAGE;
                }
                Graphics2D g2 = (Graphics2D) pg;
                g2.translate(pf.getImageableX(), pf.getImageableY());
                g2.scale(1, 1.2);

                jLabelImgFile3.paint(g2);
                return Printable.PAGE_EXISTS;

            }
        });

        if (!(jLabelImgFile3.getIcon() == null)) {
            boolean ok = job.printDialog();
            if (ok) {
                try {
                    job.print();
                } catch (PrinterException ex) {

                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No attachment to print. Please check and try again.");
        }
    }//GEN-LAST:event_jButtonAtt3ActionPerformed

    private void jButtonAtt4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAtt4ActionPerformed
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Requsition");

        job.setPrintable(new Printable() {
            public int print(Graphics pg, PageFormat pf, int pageNum) {

                if (pageNum > 0) {
                    return Printable.NO_SUCH_PAGE;
                }
                Graphics2D g2 = (Graphics2D) pg;
                g2.translate(pf.getImageableX(), pf.getImageableY());
                g2.scale(1, 1.2);

                jLabelImgFile4.paint(g2);
                return Printable.PAGE_EXISTS;

            }
        });

        if (!(jLabelImgFile4.getIcon() == null)) {
            boolean ok = job.printDialog();
            if (ok) {
                try {
                    job.print();
                } catch (PrinterException ex) {

                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No attachment to print. Please check and try again.");
        }
    }//GEN-LAST:event_jButtonAtt4ActionPerformed

    private void jButtonAtt5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAtt5ActionPerformed
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Requsition");

        job.setPrintable(new Printable() {
            public int print(Graphics pg, PageFormat pf, int pageNum) {

                if (pageNum > 0) {
                    return Printable.NO_SUCH_PAGE;
                }
                Graphics2D g2 = (Graphics2D) pg;
                g2.translate(pf.getImageableX(), pf.getImageableY());
                g2.scale(1, 1.2);

                jLabelImgFile5.paint(g2);
                return Printable.PAGE_EXISTS;

            }
        });

        if (!(jLabelImgFile5.getIcon() == null)) {
            boolean ok = job.printDialog();
            if (ok) {
                try {
                    job.print();
                } catch (PrinterException ex) {

                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No attachment to print. Please check and try again.");
        }
    }//GEN-LAST:event_jButtonAtt5ActionPerformed

    private void jRadioNormalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioNormalActionPerformed
//        if (jRadioNormal.isSelected()) {
        jLabelSerial.setText("R");
        jLabelHeaderGen.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jTextAcqRegNum.setText("");
        jLabelAcqAppTotPlannedCost.setVisible(false);
        jLabelAppTotPlannedReq.setVisible(false);
//            jTabbedPaneAppSys.setEnabledAt(4, false);
//            jTabbedPaneAppSys.setTitleAt(4, "");
        jTabbedPaneAppSys.setEnabledAt(3, true);
        jTabbedPaneAppSys.setTitleAt(3, "Report & Attachments");
        jTabbedPaneAcqAtt.setEnabledAt(0, true);
        jTabbedPaneAcqAtt.setEnabledAt(1, false);
        jTabbedPaneAcqAtt.setEnabledAt(2, true);
        jTabbedPaneAcqAtt.setEnabledAt(3, true);
        jTabbedPaneAcqAtt.setEnabledAt(4, true);
        jTabbedPaneAcqAtt.setEnabledAt(5, false);
        jTabbedPaneAcqAtt.setEnabledAt(6, false);
        jTabbedPaneAcqAtt.setTitleAt(0, "Activity Summary Report");
//            jTabbedPaneAcqAtt.setTitleAt(1, "E-Log Book");
        jTabbedPaneAcqAtt.setTitleAt(2, "Vehicle Log Sheet");
        jTabbedPaneAcqAtt.setTitleAt(3, "Proven Expenses");
        jTabbedPaneAcqAtt.setTitleAt(4, "Other e.g. Log Book Extra Page");
        jTabbedPaneAcqAtt.setTitleAt(5, "");
        jTabbedPaneAcqAtt.setTitleAt(6, "");
        refreshTab();
//        }
    }//GEN-LAST:event_jRadioNormalActionPerformed

    private void jRadioDistrictActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioDistrictActionPerformed
//        if (jRadioDistrict.isSelected()) {
        jLabelSerial.setText("MA");
        jLabelHeaderGen.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jTextAcqRegNum.setText("");
        getMonEnvSet();
        refreshTab();
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
        jLabelAppTotCleared.setText("Total Claimed");

//        }
    }//GEN-LAST:event_jRadioDistrictActionPerformed

    private void jButtonBankOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBankOkActionPerformed
        try {
            DefaultTableModel dm = (DefaultTableModel) jTableActivityReqAcq.getModel();
            DefaultTableModel dmAcq = (DefaultTableModel) jTableActivityAcq.getModel();
            while (dm.getRowCount() > 0) {
                dm.removeRow(0);
            }
            while (dmAcq.getRowCount() > 0) {
                dmAcq.removeRow(0);
            }
            wkNum = jComboWk.getSelectedItem().toString().substring((jComboWk.getSelectedItem().toString()).length() - 1);

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st1 = conn.createStatement();

            st1.executeQuery("SELECT concat(SERIAL,REF_NUM),SERIAL FROM [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab]"
                    + " where concat(PREV_SERIAL,PREV_REF_NUM,PLAN_WK) =  'R"
                    + jTextAcqRegNum.getText() + wkNum + "' and ACQ_STA = 'C' ");

            ResultSet rs1 = st1.getResultSet();

            while (rs1.next()) {
                searchRef = rs1.getString(1);
                searchSerial = rs1.getString(2);

            }
            if ("M".equals(searchSerial)) {
                JOptionPane.showMessageDialog(this, "This transcation was acquitted manually. Please view transaction through the manual view.");
            } else {
                countWk();
                fetchdataWk();
                mainPageTotUpdate();
                mainPageTotUpdateAcq();
                fetchImgCount();
//                imgCount();
            }
            jDialogWkDisplay.setVisible(false);
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_jButtonBankOkActionPerformed

    private void jPanelReportDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelReportDetailsMouseClicked

    }//GEN-LAST:event_jPanelReportDetailsMouseClicked

    private void jRadioParticpantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioParticpantActionPerformed
        jLabelSerial.setText("RM");
        // jRadioPerDiem.setSelected(false);
        jLabelHeaderGen.setText("PARTICIPANTS ALLOWANCES CLAIM");
    }//GEN-LAST:event_jRadioParticpantActionPerformed

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
            java.util.logging.Logger.getLogger(JFrameAppAcquittalView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameAppAcquittalView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameAppAcquittalView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameAppAcquittalView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new JFrameAppAcquittalView().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupAcc;
    private javax.swing.ButtonGroup buttonGroupAccAcq;
    private javax.swing.ButtonGroup buttonGroupLunch;
    private javax.swing.ButtonGroup buttonGroupSpecial;
    private javax.swing.JButton jButtonAtt1;
    private javax.swing.JButton jButtonAtt2;
    private javax.swing.JButton jButtonAtt3;
    private javax.swing.JButton jButtonAtt4;
    private javax.swing.JButton jButtonAtt5;
    private javax.swing.JButton jButtonAuthAllCancel;
    private javax.swing.JButton jButtonAuthAllOk;
    private javax.swing.JButton jButtonAuthCancel;
    private javax.swing.JButton jButtonAuthOk;
    private javax.swing.JButton jButtonBankCancel;
    private javax.swing.JButton jButtonBankOk;
    private javax.swing.JComboBox<String> jComboAuthNam;
    private javax.swing.JComboBox<String> jComboAuthNamAll;
    private javax.swing.JComboBox<String> jComboWk;
    private javax.swing.JDialog jDialogAuthority;
    private javax.swing.JDialog jDialogAuthorityAll;
    private javax.swing.JDialog jDialogWaiting;
    private javax.swing.JDialog jDialogWkDisplay;
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
    private javax.swing.JLabel jLabelAcqAppTotPlannedCost;
    private javax.swing.JLabel jLabelAcqAppTotReqCostCleared;
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
    private javax.swing.JLabel jLabelAcqProvince;
    private javax.swing.JLabel jLabelAcqRefNum;
    private javax.swing.JLabel jLabelAcqRegDate;
    private javax.swing.JLabel jLabelAcqSerial;
    private javax.swing.JLabel jLabelAcqWk;
    private javax.swing.JLabel jLabelAcqWk4;
    private javax.swing.JLabel jLabelAcqYear;
    private javax.swing.JLabel jLabelAct;
    private javax.swing.JLabel jLabelActMainPurpose;
    private javax.swing.JLabel jLabelActNam;
    private javax.swing.JLabel jLabelAllAcq;
    private javax.swing.JLabel jLabelAllBal;
    private javax.swing.JLabel jLabelAllReq;
    private javax.swing.JLabel jLabelAppTotCleared;
    private javax.swing.JLabel jLabelAppTotPlannedReq;
    private javax.swing.JLabel jLabelAttDocHeaderRep;
    private javax.swing.JLabel jLabelAutName;
    private javax.swing.JLabel jLabelAutNameAll;
    private javax.swing.JLabel jLabelBank;
    private javax.swing.JLabel jLabelBankAccNo;
    private javax.swing.JLabel jLabelBankHeader;
    private javax.swing.JLabel jLabelBankMsg;
    private javax.swing.JLabel jLabelBreakFastSub;
    private javax.swing.JLabel jLabelBreakFastSubTot;
    private javax.swing.JLabel jLabelDinnerSub;
    private javax.swing.JLabel jLabelDinnerSubTot;
    private javax.swing.JLabel jLabelEmp;
    private javax.swing.JLabel jLabelEmpNum;
    private javax.swing.JLabel jLabelGenDate;
    private javax.swing.JLabel jLabelGenLogNam;
    private javax.swing.JLabel jLabelGenLogNam1;
    private javax.swing.JLabel jLabelGenTime;
    private javax.swing.JLabel jLabelHeader;
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
    private javax.swing.JLabel jLabelNotMsg;
    private javax.swing.JLabel jLabelNotMsgAll;
    private javax.swing.JLabel jLabelNum;
    private javax.swing.JLabel jLabelNumAcq;
    private javax.swing.JLabel jLabelOffice;
    private javax.swing.JLabel jLabelPay;
    private javax.swing.JLabel jLabelProvince;
    private javax.swing.JLabel jLabelRegDateAcq;
    private javax.swing.JLabel jLabelRejectComments;
    private javax.swing.JLabel jLabelRejection;
    private javax.swing.JLabel jLabelSepDetails;
    private javax.swing.JLabel jLabelSerial;
    private javax.swing.JLabel jLabelStatusView;
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
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelAcqDocAtt1;
    private javax.swing.JPanel jPanelAcqDocAtt2;
    private javax.swing.JPanel jPanelAcqDocAtt3;
    private javax.swing.JPanel jPanelAcqDocAtt4;
    private javax.swing.JPanel jPanelAcqDocAtt5;
    private javax.swing.JPanel jPanelAcqELog;
    private javax.swing.JPanel jPanelAcquittal;
    private javax.swing.JPanel jPanelAttDocRep;
    private javax.swing.JPanel jPanelGen;
    private javax.swing.JPanel jPanelReport;
    private javax.swing.JPanel jPanelReportDetails;
    private javax.swing.JPanel jPanelRequest;
    private javax.swing.JPanel jPanelStatusView;
    private javax.swing.JRadioButton jRadioDistrict;
    private javax.swing.JRadioButton jRadioNormal;
    private javax.swing.JRadioButton jRadioParticpant;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPaneAtt1;
    private javax.swing.JScrollPane jScrollPaneAtt2;
    private javax.swing.JScrollPane jScrollPaneAtt3;
    private javax.swing.JScrollPane jScrollPaneAtt4;
    private javax.swing.JScrollPane jScrollPaneAtt5;
    private javax.swing.JScrollPane jScrollPaneWk8;
    private javax.swing.JScrollPane jScrollPaneWk9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator11;
    private javax.swing.JPopupMenu.Separator jSeparator13;
    private javax.swing.JPopupMenu.Separator jSeparator16;
    private javax.swing.JPopupMenu.Separator jSeparator18;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator20;
    private javax.swing.JPopupMenu.Separator jSeparator22;
    private javax.swing.JPopupMenu.Separator jSeparator23;
    private javax.swing.JPopupMenu.Separator jSeparator26;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator31;
    private javax.swing.JPopupMenu.Separator jSeparator32;
    private javax.swing.JPopupMenu.Separator jSeparator33;
    private javax.swing.JPopupMenu.Separator jSeparator34;
    private javax.swing.JPopupMenu.Separator jSeparator35;
    private javax.swing.JPopupMenu.Separator jSeparator36;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator62;
    private javax.swing.JPopupMenu.Separator jSeparator63;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JTabbedPane jTabbedPaneAcqAtt;
    public javax.swing.JTabbedPane jTabbedPaneAppSys;
    private javax.swing.JTable jTableActivityAcq;
    private javax.swing.JTable jTableActivityReqAcq;
    private javax.swing.JTable jTableMonDistAcquitalAttRep;
    private javax.swing.JTable jTableTripDetails;
    private javax.swing.JTextField jTextAcqRegNum;
    private javax.swing.JTextArea jTextAreaNamTravel;
    // End of variables declaration//GEN-END:variables
}
