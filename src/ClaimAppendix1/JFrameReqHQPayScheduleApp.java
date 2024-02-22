/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClaimAppendix1;

import ClaimPlanPerDiem.JFrameMnthPlanPerDiemEdit;
import ClaimPlanPerDiem.JFrameMnthPlanPerDiemCreate;
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
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import ClaimAppendix2.*;
import ClaimReports.*;
import ClaimPlan.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import utils.connCred;
import utils.connSaveFile;
import utils.savePDFToDB;
import utils.StockVehicleMgt;
import utils.recruitRecSend;

/**
 *
 * @author cgoredema
 */
public class JFrameReqHQPayScheduleApp extends javax.swing.JFrame {

    recruitRecSend recSend = new recruitRecSend();
    connCred c = new connCred();
    PreparedStatement pst = null;
    PreparedStatement pst1 = null;
    DefaultTableModel model, modelBank;
    int itmNum = 1;
    int month, finyear;
    int batVer = 1;
    int batCount = 0;
    Date curYear = new Date();
    double totSeg1 = 0;
    double totSeg2 = 0;
    String sendTo, createUsrNam, breakfastAll, lunchAll, dinnerAll, incidentalAll,
            unProvedAll, batNum, batNumSp, usrCount, usrBatNum, usrBatVer, newBatVer,
            batCountSp, regStatus, regCount, usrGrp, provNam, provNam1, provNam2,
            empOff, empProvince, empDistrict, provedAll;
    SimpleDateFormat df = new SimpleDateFormat("yyyy");
    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
    String hostName = "";

    /**
     * Creates new form JFrameTabApp1
     */
    public JFrameReqHQPayScheduleApp() {
        initComponents();
        showDate();
        showTime();
        model = (DefaultTableModel) jTableActivityFinSchedule.getModel();
        modelBank = (DefaultTableModel) jTableActivityFinBankSchedule.getModel();
        computerName();
        folderCreate();

    }

    public JFrameReqHQPayScheduleApp(String usrLogNum) {
        initComponents();
        showDate();
        showTime();
        model = (DefaultTableModel) jTableActivityFinSchedule.getModel();
        modelBank = (DefaultTableModel) jTableActivityFinBankSchedule.getModel();
        jLabelEmp.setVisible(false);
        jLabelEmp.setText(usrLogNum);

        findUser();
        findUserGrp();
        computerName();
        folderCreate();
        
        jButtonDisplaySpecialAcqResults.setVisible(false);
        jButtonGenExcelSpecialAcquittal.setVisible(false);

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
                jLabelTime.setText(s.format(d));
                jLabelTime.setText(s.format(d));
            }
        }) {

        }.start();

    }

    void showDate() {
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        jLabelDate.setText(s.format(d));

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

    void findUser() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select EMP_NAM,SUP_EMP_NUM,SUP_NAM,EMP_SUP_MAIL,"
                    + "EMP_PROVINCE,EMP_OFFICE from [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] a "
                    + "join [ClaimsAppSysZvandiri].[dbo].[EmpDetWS] b on a.EMP_NUM=b.EMP_NUM "
                    + "where a.EMP_NUM='" + jLabelEmp.getText() + "'");

            while (r.next()) {

                jLabelGenLogNam.setText(r.getString(1));
                empProvince = r.getString(5);
                empDistrict = r.getString(6);
            }

            //                 conn.close();
        } catch (Exception e) {
            System.out.println(e);

        }
    }

    void allowanceRate() {
        try {
            String rateCat;
            if ("NATIONAL".equals(empDistrict)) {
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
            //                 conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findBatNum() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT MAX (BAT_NUM) + 1  FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[BatRunTab] where BAT_TYP = 'payBat' ");

            while (r.next()) {

                batNum = r.getString(1);

            }

            //                 conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findBatNumSpecial() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT MAX (BAT_NUM) + 1  FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[BatRunTab] where BAT_TYP = 'payBatSp' ");

            while (r.next()) {

                batNumSp = r.getString(1);

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

    void clearTable() {
        int rowCount = model.getRowCount();
        //Remove rows one by one from the end of the table
        for (int i = rowCount - 1; i >= 0; i--) {
            model.removeRow(i);
        }

        int rowCountBank = modelBank.getRowCount();
        //Remove rows one by one from the end of the table
        for (int i = rowCount - 1; i >= 0; i--) {
            modelBank.removeRow(i);
        }
    }

    void fetchdata() {
        try {
            clearTable();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            st.executeQuery("SELECT 'Prepayment' Type,'' \"Reference Nbr\",'MAIN' Branch,CONVERT(varchar(20), GETDATE(), 103) \"Application Date\", "
                    + "concat(MONTH(GETDATE()),'-', YEAR(GETDATE())) \"Application Period\", concat(a.SERIAL,' ',a.REF_NUM) \"Payment Ref\","
                    + "VENDOR_NUM \"Vendor Num\", IIF(a.EMP_BNK_NAM='Mukuru', 'Cash', 'Bank') 'Payment Method','' 'Cash Account','USD' 'Currency',"
                    + "a.ACT_MAIN_PUR Description, (sum(b.BRK_AMT) + sum(b.LNC_AMT) +sum(b.DIN_AMT) + sum(b.INC_AMT) + sum(b.MSC_AMT)+  sum(b.ACC_PRO_AMT) +  sum(b.ACC_UNPROV_AMT)) 'Payment Amount' ,"
                    + "'USD' 'Currency ID' FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a  join [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] b "
                    + "on a.SERIAL=b.SERIAL and a.REF_NUM=b.REF_NUM and a.ACT_REC_STA = b.ACT_REC_STA  join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] c "
                    + "on b.SERIAL=c.SERIAL and b.REF_NUM=c.REF_NUM and b.ACT_REC_STA = c.ACT_REC_STA join [ClaimsAppSysZvandiri].[dbo].[EmpBankDetTab] d "
                    + "on a.EMP_NUM=d.EMP_NUM where c.DOC_STATUS='HODApprove'  and a.ACT_REC_STA = 'A' and a.SERIAL = 'R' and a.REF_NUM not in "
                    + "(SELECT distinct  REF_NUM FROM [ClaimsAppSysZvandiri].[dbo].[BatRunTab] where SERIAL = 'R' and STATUS in ('Paid','Not Paid')) "
                    + "group by a.SERIAL,a.REF_NUM,a.REF_DAT,VENDOR_NUM,a.EMP_BNK_NAM,a.ACT_MAIN_PUR");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                model.insertRow(model.getRowCount(), new Object[]{r.getString(1), r.getString(2), r.getString(3),
                    r.getString(4), r.getString(5), r.getString(6), r.getString(7), r.getString(8), r.getString(9),
                    r.getString(10), r.getString(11), r.getString(12), r.getString(13)});

            }

            st1.executeQuery("SELECT a.EMP_NAM,a.ACC_NUM, (sum(b.BRK_AMT) + sum(b.LNC_AMT)+ sum(b.DIN_AMT) + "
                    + "sum(b.INC_AMT)+sum(b.MSC_AMT)  + sum(b.ACC_PRO_AMT) + sum(b.ACC_UNPROV_AMT)) 'Payment Amount' ,"
                    + " concat('Per Diems ',a.EMP_NAM) FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a "
                    + " join [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] b on a.SERIAL=b.SERIAL and a.REF_NUM=b.REF_NUM "
                    + "and a.ACT_REC_STA = b.ACT_REC_STA  join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] c "
                    + "on b.SERIAL=c.SERIAL and b.REF_NUM=c.REF_NUM and b.ACT_REC_STA = c.ACT_REC_STA "
                    + "join [ClaimsAppSysZvandiri].[dbo].[EmpBankDetTab] d on a.EMP_NUM=d.EMP_NUM "
                    + "where c.DOC_STATUS='HODApprove'  and a.ACT_REC_STA = 'A' and a.SERIAL = 'R' and a.REF_NUM "
                    + "not in (SELECT distinct  REF_NUM FROM [ClaimsAppSysZvandiri].[dbo].[BatRunTab] where SERIAL = 'R' "
                    + "and STATUS in ('Paid','Not Paid')) group by a.EMP_NAM,a.ACC_NUM order by 1");

            ResultSet r1 = st1.getResultSet();

            while (r1.next()) {
                modelBank.insertRow(modelBank.getRowCount(), new Object[]{r1.getString(1), r1.getString(2), r1.getString(3),
                    r1.getString(4)});

            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No records available to display.");

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void retriveData() {
        try {

            findBatNum();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            st.executeQuery("SELECT 'Prepayment' Type,'' \"Reference Nbr\",'MAIN' Branch,CONVERT(varchar(20), GETDATE(), 103) \"Application Date\", "
                    + "concat(MONTH(GETDATE()),'-', YEAR(GETDATE())) \"Application Period\", concat(a.SERIAL,' ',a.REF_NUM) \"Payment Ref\","
                    + "VENDOR_NUM \"Vendor Num\", IIF(a.EMP_BNK_NAM='Mukuru', 'Cash', 'Bank') 'Payment Method','' 'Cash Account','USD' 'Currency',"
                    + "a.ACT_MAIN_PUR Description, (sum(b.BRK_AMT) + sum(b.LNC_AMT) +sum(b.DIN_AMT) + sum(b.INC_AMT) + sum(b.MSC_AMT) +  sum(b.ACC_PRO_AMT) "
                    + "+  sum(b.ACC_UNPROV_AMT)) 'Payment Amount' ,"
                    + "'USD' 'Currency ID' FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a  join [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] b "
                    + "on a.SERIAL=b.SERIAL and a.REF_NUM=b.REF_NUM and a.ACT_REC_STA = b.ACT_REC_STA  join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] c "
                    + "on b.SERIAL=c.SERIAL and b.REF_NUM=c.REF_NUM and b.ACT_REC_STA = c.ACT_REC_STA join [ClaimsAppSysZvandiri].[dbo].[EmpBankDetTab] d "
                    + "on a.EMP_NUM=d.EMP_NUM where c.DOC_STATUS='HODApprove'  and a.ACT_REC_STA = 'A' and a.SERIAL = 'R' and a.REF_NUM not in "
                    + "(SELECT distinct  REF_NUM FROM [ClaimsAppSysZvandiri].[dbo].[BatRunTab] where SERIAL = 'R' and STATUS in ('Paid','Not Paid')) "
                    + "group by a.SERIAL,a.REF_NUM,a.REF_DAT,VENDOR_NUM,a.EMP_BNK_NAM,a.ACT_MAIN_PUR");

            ResultSet rs = st.getResultSet();

            st1.executeQuery("SELECT a.EMP_NAM,a.ACC_NUM, (sum(b.BRK_AMT) + sum(b.LNC_AMT)+ sum(b.DIN_AMT) + "
                    + "sum(b.INC_AMT)+sum(b.MSC_AMT)  + sum(b.ACC_PRO_AMT) + sum(b.ACC_UNPROV_AMT)) 'Payment Amount' ,"
                    + " concat('Per Diems ',a.EMP_NAM) FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a "
                    + " join [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] b on a.SERIAL=b.SERIAL and a.REF_NUM=b.REF_NUM "
                    + "and a.ACT_REC_STA = b.ACT_REC_STA  join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] c "
                    + "on b.SERIAL=c.SERIAL and b.REF_NUM=c.REF_NUM and b.ACT_REC_STA = c.ACT_REC_STA "
                    + "join [ClaimsAppSysZvandiri].[dbo].[EmpBankDetTab] d on a.EMP_NUM=d.EMP_NUM "
                    + "where c.DOC_STATUS='HODApprove'  and a.ACT_REC_STA = 'A' and a.SERIAL = 'R' and a.REF_NUM "
                    + "not in (SELECT distinct  REF_NUM FROM [ClaimsAppSysZvandiri].[dbo].[BatRunTab] where SERIAL = 'R' "
                    + "and STATUS in ('Paid','Not Paid')) group by a.EMP_NAM,a.ACC_NUM order by 1");

            ResultSet rs1 = st1.getResultSet();

            HSSFWorkbook workbook = new HSSFWorkbook();

            HSSFSheet sheet = workbook.createSheet("Fin - For ERP");
            HSSFSheet sheet2 = workbook.createSheet("Fin - For Bank Payment");

            HSSFRow rowhead1 = sheet.createRow((short) 0);
            HSSFRow rowhead2 = sheet2.createRow((short) 0);

            rowhead1.createCell((short) 8).setCellValue("Payment Batch Number " + batNum + " Version 1");

            HSSFRow rowhead = sheet.createRow((short) 2);

            rowhead.createCell((short) 0).setCellValue("Type");
            rowhead.createCell((short) 1).setCellValue("Reference No.");
            rowhead.createCell((short) 2).setCellValue("Branch");
            rowhead.createCell((short) 3).setCellValue("Application Date");
            rowhead.createCell((short) 4).setCellValue("Application Period");
            rowhead.createCell((short) 5).setCellValue("Payment Ref");
            rowhead.createCell((short) 6).setCellValue("Vendor");
            rowhead.createCell((short) 7).setCellValue("Payment Method");
            rowhead.createCell((short) 8).setCellValue("Cash Account");
            rowhead.createCell((short) 9).setCellValue("Currency");
            rowhead.createCell((short) 10).setCellValue("Description");
            rowhead.createCell((short) 11).setCellValue("Payment Amount");
            rowhead.createCell((short) 12).setCellValue("Currency ID");

            rowhead2.createCell((short) 0).setCellValue("Name and Surname");
            rowhead2.createCell((short) 1).setCellValue("Account Number");
            rowhead2.createCell((short) 2).setCellValue("Payment Amount");
            rowhead2.createCell((short) 2).setCellValue("Description");

            int i = 3;
            while (rs.next()) {

                HSSFRow row = sheet.createRow((short) i);
                row.createCell((short) 0).setCellValue(rs.getString(1));
                row.createCell((short) 1).setCellValue(rs.getString(2));
                row.createCell((short) 2).setCellValue(rs.getString(3));
                row.createCell((short) 3).setCellValue(rs.getString(4));
                row.createCell((short) 4).setCellValue(rs.getString(5));
                row.createCell((short) 5).setCellValue(rs.getString(6));
                row.createCell((short) 6).setCellValue(rs.getString(7));
                row.createCell((short) 7).setCellValue(rs.getString(8));
                row.createCell((short) 8).setCellValue(rs.getString(9));
                row.createCell((short) 9).setCellValue(rs.getString(10));
                row.createCell((short) 10).setCellValue(rs.getString(11));
                row.createCell((short) 11).setCellValue(rs.getString(12));
                row.createCell((short) 12).setCellValue(rs.getString(13));

                i++;
            }

            int x = 1;

            while (rs1.next()) {
                HSSFRow row2 = sheet2.createRow((short) x);
                row2.createCell((short) 0).setCellValue(rs1.getString(1));
                row2.createCell((short) 1).setCellValue(rs1.getInt(2));
                row2.createCell((short) 2).setCellValue(rs1.getString(3));
                row2.createCell((short) 3).setCellValue(rs1.getString(4));

                x++;
            }

            String reportFile = "C:\\LeaveReport\\PaymentSchBat #" + batNum + " Version 1.xls";
            FileOutputStream fileOut = new FileOutputStream(reportFile);
            workbook.write(fileOut);
            fileOut.close();
            String fileName = "PaymentSchBat #" + batNum + " Version 1.xls";
            String mailHeader = "Request extraction batch# " + batNum + " version " + batVer;
            String mailMsg = "Please find request schedule attachment for batch# "
                    + "<b>" + batNum + "</b> version <b>" + batVer + "</b> generated by<b> " + jLabelGenLogNam.getText() + "</b>.";

            //batchUpd();
//            recSend.sendMailExtract(mailHeader, reportFile, mailMsg, fileName);
            jDialogWaiting.setVisible(false);
            JOptionPane.showMessageDialog(this, "Your excel file PaymentSchBat # " + batNum + " version  " + batVer + ".xls has been generated"
                    + " and email sent to the finance team");
            new JFrameReqHQPayScheduleApp(jLabelEmp.getText()).setVisible(true);
            setVisible(false);
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (FileNotFoundException e2) {
            jDialogWaiting.setVisible(false);
            e2.printStackTrace();
        } catch (IOException e3) {
            jDialogWaiting.setVisible(false);
            e3.printStackTrace();
        }
    }

    void batchUpd() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            st.executeQuery("SELECT count(*) FROM  ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab "
                    + "where DOC_STATUS='HODApprove' and ACT_REC_STA = 'A' and SERIAL = 'R' "
                    + "and REF_NUM not in (SELECT distinct  REF_NUM FROM [ClaimsAppSysZvandiri].[dbo].[BatRunTab] "
                    + "where SERIAL = 'R' and STATUS in ('Paid','Not Paid'))");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                batCount = r.getInt(1);
            }

            if (batCount == 0) {
                JOptionPane.showMessageDialog(this, "Batch run has no records. Please check dates and run again.");
//                jDateFrom.requestFocusInWindow();
//                jDateFrom.setFocusable(true);

            } else {
                try {
                    retriveData();
                    System.out.println("errf " + batNum + " " + jLabelDate.getText() + " " + jLabelTime.getText() + " " + hostName);
                    String sql = "insert into [ClaimsAppSysZvandiri].[dbo].[BatRunTab] ( SERIAL,REF_NUM,BAT_TYP ,BAT_NUM,BAT_VER,RUN_BY,STATUS,RUN_DAT,RUN_TIME,ACTIONED_ON_COMPUTER)"
                            + "SELECT a.SERIAL,a.REF_NUM,'payBat','" + batNum + "','1','" + jLabelGenLogNam.getText() + "' ,'Paid','" + jLabelDate.getText() + "','" + jLabelTime.getText() + "','" + hostName + "'"
                            + "FROM ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab a,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab b, ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab c "
                            + "where (a.REF_NUM = b.REF_NUM and a.DOC_VER =b.DOC_VER and a.REF_NUM = c.REF_NUM and a.DOC_VER =c.DOC_VER) and c.DOC_STATUS='HODApprove' "
                            + "and a.ACT_REC_STA = 'A'"
                            + "and a.SERIAL = 'R'"
                            + "and a.REF_NUM not in (SELECT distinct  REF_NUM FROM [ClaimsAppSysZvandiri].[dbo].[BatRunTab] where SERIAL = 'R' and STATUS in ('Paid','Not Paid'))"
                            //                            + "and c.ACTIONED_ON_DATE between '" + s.format(jDateFrom.getDate()) + "' and '" + s.format(jDateTo.getDate()) + "'"
                            + "group by a.SERIAL,a.REF_NUM,a.REF_DAT,a.EMP_NUM,concat('E',a.EMP_NUM) ,a.EMP_NAM,a.EMP_BNK_NAM,a.ACC_NUM,a.EMP_PROV,a.ACT_MAIN_PUR";

                    pst = conn.prepareStatement(sql);

                    pst.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e1) {
            System.out.println(e1);
        }
    }

    void folderCreate() {
        File tmpDir = new File("C:\\LeaveReport\\EngagementPDF");
        boolean exists = tmpDir.exists();
        if (exists) {

            System.out.println("Folder exists");

        } else {
            try {
                Path path = Paths.get("C:\\LeaveReport\\EngagementPDF");

                Files.createDirectories(path);

            } catch (Exception e) {
                System.out.println(e);
            }
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

        buttonGroupAcc = new javax.swing.ButtonGroup();
        jDialogWaiting = new javax.swing.JDialog();
        jPanelItem1 = new javax.swing.JPanel();
        jLabelLogo = new javax.swing.JLabel();
        jLabelHeaderLine = new javax.swing.JLabel();
        jLabelDate = new javax.swing.JLabel();
        jLabelTime = new javax.swing.JLabel();
        jLabellogged1 = new javax.swing.JLabel();
        jLabelGenLogNam = new javax.swing.JLabel();
        jButtonGenExcel = new javax.swing.JButton();
        jButtonDisplayResults = new javax.swing.JButton();
        jLabelEmp = new javax.swing.JLabel();
        jButtonDisplaySpecialAcqResults = new javax.swing.JButton();
        jButtonGenExcelSpecialAcquittal = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableActivityFinSchedule = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableActivityFinBankSchedule = new javax.swing.JTable();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuNew = new javax.swing.JMenu();
        jMenuItemPlanPerDiem = new javax.swing.JMenuItem();
        jSeparator13 = new javax.swing.JPopupMenu.Separator();
        jMenuItemPerDiemAcquittal = new javax.swing.JMenuItem();
        jSeparator16 = new javax.swing.JPopupMenu.Separator();
        jMenuEdit = new javax.swing.JMenu();
        jMenuMonPlanEdit = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItemClose = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuRequest = new javax.swing.JMenu();
        jMenuItemSupApp = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAccMgrRev = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMenuItemHeadApp = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSchPerDiem = new javax.swing.JMenuItem();
        jSeparator23 = new javax.swing.JPopupMenu.Separator();
        jMenuItemView = new javax.swing.JMenuItem();
        jMenuAcquittal = new javax.swing.JMenu();
        jMenuItemAcqSupApp = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
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
        jDialogWaiting.setPreferredSize(new java.awt.Dimension(300, 50));

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

        jDialogWaiting.getAccessibleContext().setAccessibleName("            Processing. Please Wait !!!!!");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PERDIEM PAYMENT SCHEDULE");
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        jPanelItem1.setBackground(new java.awt.Color(204, 204, 204));
        jPanelItem1.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jPanelItem1.setPreferredSize(new java.awt.Dimension(1280, 677));
        jPanelItem1.setLayout(null);

        jLabelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelItem1.add(jLabelLogo);
        jLabelLogo.setBounds(10, 10, 220, 100);

        jLabelHeaderLine.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelItem1.add(jLabelHeaderLine);
        jLabelHeaderLine.setBounds(350, 40, 610, 40);

        jLabelDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelDate.setText("29 November 2018");
        jPanelItem1.add(jLabelDate);
        jLabelDate.setBounds(1080, 0, 110, 30);

        jLabelTime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelTime.setText("15:20:30");
        jPanelItem1.add(jLabelTime);
        jLabelTime.setBounds(1250, 0, 80, 30);

        jLabellogged1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged1.setText("Logged In");
        jPanelItem1.add(jLabellogged1);
        jLabellogged1.setBounds(1080, 40, 80, 30);

        jLabelGenLogNam.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam.setForeground(new java.awt.Color(255, 255, 255));
        jPanelItem1.add(jLabelGenLogNam);
        jLabelGenLogNam.setBounds(1170, 40, 190, 30);

        jButtonGenExcel.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonGenExcel.setText("<html>Generate batch <br> Excel Spreadsheet </html>");
        jButtonGenExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenExcelActionPerformed(evt);
            }
        });
        jPanelItem1.add(jButtonGenExcel);
        jButtonGenExcel.setBounds(320, 630, 160, 40);

        jButtonDisplayResults.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonDisplayResults.setText("<html>Display Perdiem <br>Request Results </html> ");
        jButtonDisplayResults.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDisplayResultsActionPerformed(evt);
            }
        });
        jPanelItem1.add(jButtonDisplayResults);
        jButtonDisplayResults.setBounds(70, 630, 160, 40);

        jLabelEmp.setForeground(new java.awt.Color(255, 255, 255));
        jPanelItem1.add(jLabelEmp);
        jLabelEmp.setBounds(1110, 70, 50, 20);

        jButtonDisplaySpecialAcqResults.setBackground(new java.awt.Color(153, 153, 153));
        jButtonDisplaySpecialAcqResults.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonDisplaySpecialAcqResults.setForeground(new java.awt.Color(255, 51, 0));
        jButtonDisplaySpecialAcqResults.setText("<html>Display Special <br> Acquittal Results </html>");
        jButtonDisplaySpecialAcqResults.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDisplaySpecialAcqResultsActionPerformed(evt);
            }
        });
        jPanelItem1.add(jButtonDisplaySpecialAcqResults);
        jButtonDisplaySpecialAcqResults.setBounds(520, 630, 180, 40);

        jButtonGenExcelSpecialAcquittal.setBackground(new java.awt.Color(153, 153, 153));
        jButtonGenExcelSpecialAcquittal.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonGenExcelSpecialAcquittal.setForeground(new java.awt.Color(255, 0, 0));
        jButtonGenExcelSpecialAcquittal.setText("<html>Generate Special Acquittal <br> batch  Excel Spreadsheet </html>");
        jButtonGenExcelSpecialAcquittal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenExcelSpecialAcquittalActionPerformed(evt);
            }
        });
        jPanelItem1.add(jButtonGenExcelSpecialAcquittal);
        jButtonGenExcelSpecialAcquittal.setBounds(660, 630, 180, 40);

        jTableActivityFinSchedule.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jTableActivityFinSchedule.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Type", "Reference No.", "Branch", "Application Date", "Application Period", "Payment Ref", "Vendor", "Payment Method", "Cash Account", "Currency", "Description", "Payment Amount", "Currency ID"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableActivityFinSchedule.setDragEnabled(true);
        jTableActivityFinSchedule.setGridColor(new java.awt.Color(255, 255, 255));
        jTableActivityFinSchedule.setMinimumSize(new java.awt.Dimension(180, 64));
        jTableActivityFinSchedule.setRowHeight(25);
        jTableActivityFinSchedule.setSelectionBackground(new java.awt.Color(214, 246, 247));
        jTableActivityFinSchedule.getTableHeader().setReorderingAllowed(false);
        jTableActivityFinSchedule.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTableActivityFinScheduleFocusLost(evt);
            }
        });
        jTableActivityFinSchedule.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableActivityFinScheduleMouseClicked(evt);
            }
        });
        jTableActivityFinSchedule.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTableActivityFinScheduleKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTableActivityFinScheduleKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTableActivityFinScheduleKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(jTableActivityFinSchedule);

        jTabbedPane1.addTab("ERP", jScrollPane2);

        jPanel1.setLayout(null);

        jTableActivityFinBankSchedule.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name and Surname", "Account No.", "Payment Amount", "Description"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableActivityFinBankSchedule);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(0, 10, 1320, 460);

        jTabbedPane1.addTab("Bank & Cash", jPanel1);

        jPanelItem1.add(jTabbedPane1);
        jTabbedPane1.setBounds(10, 120, 1330, 500);

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
        jMenuFile.add(jSeparator2);

        jMenuItemClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemClose.setText("Close");
        jMenuItemClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemClose);
        jMenuFile.add(jSeparator3);

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
        jMenuRequest.add(jSeparator4);

        jMenuItemAccMgrRev.setText("Finance Team Approval");
        jMenuItemAccMgrRev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAccMgrRevActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemAccMgrRev);
        jMenuRequest.add(jSeparator5);

        jMenuItemHeadApp.setText("Project HOD Approval");
        jMenuItemHeadApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemHeadAppActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemHeadApp);
        jMenuRequest.add(jSeparator6);

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
        jMenuAcquittal.add(jSeparator8);

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
            .addComponent(jPanelItem1, javax.swing.GroupLayout.DEFAULT_SIZE, 1360, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelItem1, javax.swing.GroupLayout.DEFAULT_SIZE, 680, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1376, 739));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained

    }//GEN-LAST:event_formFocusGained


    private void jTableActivityFinScheduleFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTableActivityFinScheduleFocusLost

    }//GEN-LAST:event_jTableActivityFinScheduleFocusLost

    private void jTableActivityFinScheduleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableActivityFinScheduleMouseClicked

    }//GEN-LAST:event_jTableActivityFinScheduleMouseClicked

    private void jTableActivityFinScheduleKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableActivityFinScheduleKeyPressed

    }//GEN-LAST:event_jTableActivityFinScheduleKeyPressed

    private void jTableActivityFinScheduleKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableActivityFinScheduleKeyReleased

    }//GEN-LAST:event_jTableActivityFinScheduleKeyReleased

    private void jTableActivityFinScheduleKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableActivityFinScheduleKeyTyped

    }//GEN-LAST:event_jTableActivityFinScheduleKeyTyped

    private void jButtonGenExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenExcelActionPerformed
        //  retriveData();
        jDialogWaiting.setVisible(true);
        batchUpd();
    }//GEN-LAST:event_jButtonGenExcelActionPerformed

    private void jMenuPlanApprovalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuPlanApprovalActionPerformed
   
    }//GEN-LAST:event_jMenuPlanApprovalActionPerformed

    private void jButtonDisplaySpecialAcqResultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDisplaySpecialAcqResultsActionPerformed
//        fetchdataSpecial();
    }//GEN-LAST:event_jButtonDisplaySpecialAcqResultsActionPerformed

    private void jButtonGenExcelSpecialAcquittalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenExcelSpecialAcquittalActionPerformed
        jDialogWaiting.setVisible(true);
//        batchUpdSpecial();
    }//GEN-LAST:event_jButtonGenExcelSpecialAcquittalActionPerformed

    private void jButtonDisplayResultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDisplayResultsActionPerformed
        fetchdata();
    }//GEN-LAST:event_jButtonDisplayResultsActionPerformed

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
            java.util.logging.Logger.getLogger(JFrameReqHQPayScheduleApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameReqHQPayScheduleApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameReqHQPayScheduleApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameReqHQPayScheduleApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new JFrameReqHQPayScheduleApp().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.ButtonGroup buttonGroupAcc;
    public javax.swing.JButton jButtonDisplayResults;
    public javax.swing.JButton jButtonDisplaySpecialAcqResults;
    public javax.swing.JButton jButtonGenExcel;
    public javax.swing.JButton jButtonGenExcelSpecialAcquittal;
    public javax.swing.JDialog jDialogWaiting;
    public javax.swing.JLabel jLabelDate;
    public javax.swing.JLabel jLabelEmp;
    public javax.swing.JLabel jLabelGenLogNam;
    public javax.swing.JLabel jLabelHeaderLine;
    public javax.swing.JLabel jLabelLogo;
    public javax.swing.JLabel jLabelTime;
    public javax.swing.JLabel jLabellogged1;
    public javax.swing.JMenu jMenuAcquittal;
    public javax.swing.JMenuBar jMenuBar;
    public javax.swing.JMenu jMenuEdit;
    public javax.swing.JMenu jMenuFile;
    public javax.swing.JMenuItem jMenuItemAccMgrRev;
    public javax.swing.JMenuItem jMenuItemAcqAccApp;
    public javax.swing.JMenuItem jMenuItemAcqHeadApp;
    public javax.swing.JMenuItem jMenuItemAcqSchGen;
    public javax.swing.JMenuItem jMenuItemAcqSupApp;
    public javax.swing.JMenuItem jMenuItemAcqView;
    public javax.swing.JMenuItem jMenuItemClose;
    public javax.swing.JMenuItem jMenuItemExit;
    public javax.swing.JMenuItem jMenuItemHeadApp;
    public javax.swing.JMenuItem jMenuItemPerDiemAcquittal;
    public javax.swing.JMenuItem jMenuItemPlanFinApproval;
    public javax.swing.JMenuItem jMenuItemPlanHODApproval;
    public javax.swing.JMenuItem jMenuItemPlanPerDiem;
    public javax.swing.JMenuItem jMenuItemPlanSupApproval;
    public javax.swing.JMenuItem jMenuItemPlanView;
    public javax.swing.JMenuItem jMenuItemReqGenLst;
    public javax.swing.JMenuItem jMenuItemReqSubmitted;
    public javax.swing.JMenuItem jMenuItemSchPerDiem;
    public javax.swing.JMenuItem jMenuItemSupApp;
    public javax.swing.JMenuItem jMenuItemUserProfUpd;
    public javax.swing.JMenuItem jMenuItemView;
    public javax.swing.JMenuItem jMenuMonPlanEdit;
    public javax.swing.JMenu jMenuMonthlyPlan;
    public javax.swing.JMenu jMenuNew;
    public javax.swing.JMenu jMenuPlanApproval;
    public javax.swing.JMenu jMenuReports;
    public javax.swing.JMenu jMenuRequest;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JPanel jPanelItem1;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JPopupMenu.Separator jSeparator10;
    public javax.swing.JPopupMenu.Separator jSeparator11;
    public javax.swing.JPopupMenu.Separator jSeparator13;
    public javax.swing.JPopupMenu.Separator jSeparator16;
    public javax.swing.JPopupMenu.Separator jSeparator18;
    public javax.swing.JPopupMenu.Separator jSeparator2;
    public javax.swing.JPopupMenu.Separator jSeparator22;
    public javax.swing.JPopupMenu.Separator jSeparator23;
    public javax.swing.JPopupMenu.Separator jSeparator26;
    public javax.swing.JPopupMenu.Separator jSeparator3;
    public javax.swing.JPopupMenu.Separator jSeparator34;
    public javax.swing.JPopupMenu.Separator jSeparator4;
    public javax.swing.JPopupMenu.Separator jSeparator5;
    public javax.swing.JPopupMenu.Separator jSeparator6;
    public javax.swing.JPopupMenu.Separator jSeparator62;
    public javax.swing.JPopupMenu.Separator jSeparator63;
    public javax.swing.JPopupMenu.Separator jSeparator8;
    public javax.swing.JTabbedPane jTabbedPane1;
    public javax.swing.JTable jTableActivityFinBankSchedule;
    public javax.swing.JTable jTableActivityFinSchedule;
    // End of variables declaration//GEN-END:variables
}
