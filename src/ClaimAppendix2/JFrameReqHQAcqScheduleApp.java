/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClaimAppendix2;

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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import ClaimAppendix1.*;
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
public class JFrameReqHQAcqScheduleApp extends javax.swing.JFrame {

    connCred c = new connCred();
    recruitRecSend recSend = new recruitRecSend();
    PreparedStatement pst = null;
    PreparedStatement pst1 = null;
    DefaultTableModel model;
    int itmNum = 1;
    int month, finyear;
    int batVer = 1;
    Date curYear = new Date();
    double totSeg1 = 0;
    double totSeg2 = 0;
    String sendTo, createUsrNam, breakfastAll, lunchAll, dinnerAll, incidentalAll,
            unProvedAll, batNum, usrCount, usrBatNum, usrBatVer, newBatVer,
            batCount, usrGrp, provNam, provNam1, provNam2, empOff;
    SimpleDateFormat df = new SimpleDateFormat("yyyy");
    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sDateTime = new SimpleDateFormat("ddMMyyyy_HHMMSS");
    String hostName = "";

    /**
     * Creates new form JFrameTabApp1
     */
    public JFrameReqHQAcqScheduleApp() {
        initComponents();
        showDate();
        showTime();
        model = (DefaultTableModel) jTableActivityFinSchedule.getModel();
        computerName();
        jDateFrom.setVisible(false);
        jDateTo.setVisible(false);
        jLabelRangeSelect.setVisible(false);
        jLabelDateFrom.setVisible(false);
        jLabelDateTo.setVisible(false);
    }

    public JFrameReqHQAcqScheduleApp(String usrLogNum) {
        initComponents();
        showDate();
        showTime();
        model = (DefaultTableModel) jTableActivityFinSchedule.getModel();
        jLabelEmp.setText(usrLogNum);
        jLabelEmp.setVisible(false);
        findUser();
        findUserGrp();
        computerName();
        jDateFrom.setVisible(false);
        jDateTo.setVisible(false);
        jLabelRangeSelect.setVisible(false);
        jLabelDateFrom.setVisible(false);
        jLabelDateTo.setVisible(false);
        jLabelSingleUser.setVisible(false);
        jLabelWeek.setVisible(false);
        jTextWeek.setVisible(false);
        jLabelUserRef.setVisible(false);
        jTextAcqRegNum.setVisible(false);
        jButtonUsrFile.setVisible(false);
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

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select * from [ClaimsAppSysZvandiri].[dbo]."
                    + "[EmpDetTab] where emp_num ='" + jLabelEmp.getText() + "'");

            while (r.next()) {

                jLabelGenLogNam.setText(r.getString(2));
//                provNam = r.getString(16);
//                provNam1 = r.getString(18);
//                provNam2 = r.getString(19);

            }

            //                 conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findBatNum() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT MAX (BAT_NUM) + 1  FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[BatRunTab] where BAT_TYP = 'acqBat' ");

            while (r.next()) {

                batNum = r.getString(1);

            }

            //                 conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findBatNumUsr() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT MAX (BAT_VER),BAT_NUM  FROM [ClaimsAppSysZvandiri].[dbo].[BatRunTab]"
                    + " where BAT_TYP = 'acqBat' and concat(REF_NUM,PLAN_WK) ='" + jTextAcqRegNum.getText() + jTextWeek.getText() + "' group by BAT_NUM");

            while (r.next()) {

                usrBatNum = r.getString(2);
                usrBatVer = r.getString(1);
                newBatVer = Integer.toString(Integer.parseInt(usrBatVer) + 1);

            }

            //                 conn.close();
        } catch (Exception e) {

            System.out.println(e);
        }
    }

    void findExistUsr() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT count(*) FROM ClaimsAppSysZvandiri.dbo.ClaimAppGenTab "
                    + "a,ClaimsAppSysZvandiri.dbo.ClaimAppItmTab b, ClaimsAppSysZvandiri.dbo.ClaimsWFActTab c "
                    + "where (a.SERIAL = b.SERIAL and a.REF_NUM = b.REF_NUM and "
                    + "a.DOC_VER =b.DOC_VER and a.SERIAL = c.SERIAL and a.REF_NUM = c.REF_NUM and "
                    + "a.DOC_VER =c.DOC_VER) and c.DOC_STATUS='HeadApprove' "
                    + "and a.ACT_REC_STA = 'Q'"
                    + "and a.PREV_REF_NUM ='" + jTextAcqRegNum.getText() + "'"
                    + "and b.PLAN_WK ='" + jTextWeek.getText() + "'"
                    + "and concat(a.PREV_REF_NUM,b.PLAN_WK) in (SELECT distinct  concat(REF_NUM,PLAN_WK) FROM [ClaimsAppSysZvandiri].[dbo].[BatRunTab])");

            while (r.next()) {

                usrCount = r.getString(1);

            }

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
    }

    void fetchdata() {
        try {
            clearTable();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            st.executeQuery("SELECT a.PREV_SERIAL,a.PREV_REF_NUM,a.PREV_REF_DAT,concat('E',a.EMP_NUM),a.EMP_NAM,a.EMP_PROV,a.EMP_OFF,BUD_CODE,a.ACT_MAIN_PUR,b.PLAN_WK,\n"
                    + "                     (sum(b.BRK_AMT) + sum(b.LNC_AMT) + sum(b.DIN_AMT) + sum(b.INC_AMT) + sum(b.MSC_AMT)+ sum(b.ACC_UNPROV_AMT)) \"total\"\n"
                    + "                     FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a inner join [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] b on a.SERIAL=b.SERIAL and a.REF_NUM=b.REF_NUM \n"
                    + "                     and a.ACT_REC_STA = b.ACT_REC_STA  inner join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab]c on b.SERIAL=c.SERIAL and b.REF_NUM=c.REF_NUM \n"
                    + "                     and b.ACT_REC_STA = c.ACT_REC_STA \n"
                    + "                     where c.DOC_STATUS='HODAcqApprove' \n"
                    + "                     and a.ACT_REC_STA = 'Q' and a.SERIAL = 'A'\n"
                    + "                              and concat(a.PREV_REF_NUM,b.PLAN_WK) not in (SELECT distinct  concat(REF_NUM,PLAN_WK) FROM [ClaimsAppSysZvandiri].[dbo].[BatRunTab] where SERIAL = 'R' and STATUS = 'Acquitted'\n"
                    + "                     )\n"
                    + "                      group by a.PREV_SERIAL,a.PREV_REF_NUM,a.PREV_REF_DAT,a.EMP_NUM,a.EMP_NUM,a.EMP_NAM,a.EMP_BNK_NAM,a.ACC_NUM,a.EMP_PROV,a.EMP_OFF,BUD_CODE,a.ACT_MAIN_PUR,b.PLAN_WK");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                model.insertRow(model.getRowCount(), new Object[]{r.getString(1), r.getString(2), r.getString(3),
                    r.getString(4), r.getString(5), r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11)});

            }
//            if (jTableActivityFinSchedule.getRowCount() == 0) {
//            JOptionPane.showMessageDialog(this, "At least one activity should be "
//                    + "completed on the TAB \"Perdiem Request\". Please check and correct.");
//            
//        }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void retriveData() {
        try {
            folderCreate();
            findBatNum();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
//
//            Statement st1 = conn.createStatement();
//            ResultSet rs1 = st1.executeQuery("SELECT a.SERIAL,a.REF_NUM,a.REF_DAT,concat('E',a.EMP_NUM),a.EMP_NAM,a.EMP_BNK_NAM,a.ACC_NUM,a.EMP_PROV,a.EMP_OFF,BUD_CODE,a.ACT_MAIN_PUR,"
//                    + "(sum(b.BRK_AMT)+ sum(b.LNC_AMT)+sum(b.DIN_AMT)+ sum(b.INC_AMT) + sum(b.MSC_AMT) +  sum(b.ACC_UNPROV_AMT)) \"total\""
//                    + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a inner join [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] b on a.SERIAL=b.SERIAL and a.REF_NUM=b.REF_NUM "
//                    + "and a.ACT_REC_STA = b.ACT_REC_STA  inner join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab]c on b.SERIAL=c.SERIAL and b.REF_NUM=c.REF_NUM "
//                    + "and b.ACT_REC_STA = c.ACT_REC_STA "
//                    + "where c.DOC_STATUS='HODApprove' "
//                    + "and a.ACT_REC_STA = 'A'"
//                    + " and a.REF_NUM in (SELECT distinct a.PREV_REF_NUM\n"
//                    + "                     FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a inner join [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] b on a.SERIAL=b.SERIAL and a.REF_NUM=b.REF_NUM \n"
//                    + "                     and a.ACT_REC_STA = b.ACT_REC_STA  inner join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab]c on b.SERIAL=c.SERIAL and b.REF_NUM=c.REF_NUM \n"
//                    + "                     and b.ACT_REC_STA = c.ACT_REC_STA \n"
//                    + "                     where c.DOC_STATUS='HODAcqApprove' \n"
//                    + "                     and a.ACT_REC_STA = 'Q'\n"
//                    + "and concat(a.PREV_REF_NUM,b.PLAN_WK) not in (SELECT distinct  concat(REF_NUM,PLAN_WK) FROM [ClaimsAppSysZvandiri].[dbo].[BatRunTab] where SERIAL = 'R' and STATUS = 'Acquitted')"
//                    + "                    and a.SERIAL = 'A' )"
//                    + "and a.SERIAL = 'R' "
//                    + " group by a.SERIAL,a.REF_NUM,a.REF_DAT,a.EMP_NUM,a.EMP_NUM,a.EMP_NAM,a.EMP_BNK_NAM,a.ACC_NUM,a.EMP_PROV,a.EMP_OFF,BUD_CODE,a.ACT_MAIN_PUR");

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT a.PREV_SERIAL,a.PREV_REF_NUM,a.PREV_REF_DAT,a.SERIAL,a.REF_NUM,a.REF_DAT,concat('E',a.EMP_NUM),a.EMP_NAM,a.EMP_PROV,a.EMP_OFF,BUD_CODE,a.ACT_MAIN_PUR,b.PLAN_WK,"
                    + "(sum(b.BRK_AMT)+ sum(b.LNC_AMT)+sum(b.DIN_AMT)+ sum(b.INC_AMT) + sum(b.MSC_AMT) + sum(b.ACC_UNPROV_AMT)) \"total\""
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a inner join [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] b on a.SERIAL=b.SERIAL and a.REF_NUM=b.REF_NUM "
                    + "and a.ACT_REC_STA = b.ACT_REC_STA  inner join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab]c on b.SERIAL=c.SERIAL and b.REF_NUM=c.REF_NUM "
                    + "and b.ACT_REC_STA = c.ACT_REC_STA "
                    + "where c.DOC_STATUS='HODAcqApprove' "
                    + "and a.ACT_REC_STA = 'Q'"
                    + "and concat(a.PREV_REF_NUM,b.PLAN_WK) not in (SELECT distinct  concat(REF_NUM,PLAN_WK) FROM [ClaimsAppSysZvandiri].[dbo].[BatRunTab] where SERIAL = 'R' and STATUS = 'Acquitted')"
                    + "and a.SERIAL = 'A' "
                    + " group by a.PREV_SERIAL,a.PREV_REF_NUM,a.PREV_REF_DAT,a.SERIAL,a.REF_NUM,a.REF_DAT,a.EMP_NUM,a.EMP_NUM,a.EMP_NAM,a.EMP_BNK_NAM,"
                    + "a.ACC_NUM,a.EMP_PROV,a.EMP_OFF,BUD_CODE,a.ACT_MAIN_PUR,b.PLAN_WK");

            Statement st2 = conn.createStatement();
            ResultSet rs2 = st2.executeQuery("SELECT a.PREV_SERIAL,a.PREV_REF_NUM,a.PREV_REF_DAT,concat('E',a.EMP_NUM),a.EMP_NAM,a.EMP_PROV,a.EMP_OFF,b.ACT_DATE,BUD_CODE,b.ACT_DESC,b.PLAN_WK,\n"
                    + "                     (b.BRK_AMT+ b.LNC_AMT+b.DIN_AMT+ b.INC_AMT + b.MSC_AMT + b.ACC_UNPROV_AMT ) \"total\"\n"
                    + "                    FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a inner join [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] b on a.SERIAL=b.SERIAL and a.REF_NUM=b.REF_NUM \n"
                    + "                     and a.ACT_REC_STA = b.ACT_REC_STA  inner join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab]c on b.SERIAL=c.SERIAL and b.REF_NUM=c.REF_NUM \n"
                    + "                     and b.ACT_REC_STA = c.ACT_REC_STA \n"
                    + "                     where c.DOC_STATUS='HODAcqApprove' \n"
                    + "                     and a.ACT_REC_STA = 'Q'\n"
                    + "                     and concat(a.PREV_REF_NUM,b.PLAN_WK) not in (SELECT distinct  concat(REF_NUM,PLAN_WK) FROM [ClaimsAppSysZvandiri].[dbo].[BatRunTab] where SERIAL = 'R' and STATUS = 'Acquitted')"
                    + "and a.SERIAL = 'A' ");

//            Statement st3 = conn.createStatement();
//            ResultSet rs3 = st3.executeQuery("SELECT PREV_SERIAL,PREV_REF_NUM,SERIAL,REF_NUM,concat('E',EMP_NUM),"
//                    + "EMP_NAM,PLAN_WK FROM [ClaimsAppSysZvandiri].[dbo].[ClaimRefundDepositTab] where CONCAT(serial,ref_num) "
//                    + "in (SELECT CONCAT(a.SERIAL,a.REF_NUM) FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a inner join "
//                    + "[ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] b on a.SERIAL=b.SERIAL and a.REF_NUM=b.REF_NUM  and "
//                    + "a.ACT_REC_STA = b.ACT_REC_STA  inner join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] c on b.SERIAL=c.SERIAL "
//                    + "and b.REF_NUM=c.REF_NUM and b.ACT_REC_STA = c.ACT_REC_STA  where c.DOC_STATUS='HODAcqApprove'"
//                    + "and a.ACT_REC_STA = 'Q' and concat(a.PREV_REF_NUM,b.PLAN_WK) not in (SELECT distinct "
//                    + "concat(REF_NUM,PLAN_WK) FROM [ClaimsAppSysZvandiri].[dbo].[BatRunTab] where SERIAL = 'R' and STATUS = 'Acquitted') "
//                    + "and a.SERIAL = 'A' group by  a.SERIAL,a.REF_NUM,a.PREV_SERIAL,a.PREV_REF_NUM,a.PREV_REF_DAT,"
//                    + "a.EMP_NUM,a.EMP_NUM,a.EMP_NAM,a.EMP_BNK_NAM,a.ACC_NUM,a.EMP_PROV,a.EMP_OFF,"
//                    + "BUD_CODE,a.ACT_MAIN_PUR,b.PLAN_WK)");
            HSSFWorkbook workbook = new HSSFWorkbook();
//            HSSFSheet sheet1 = workbook.createSheet("Fin - Request Details");
            HSSFSheet sheet2 = workbook.createSheet("Fin - Acquittal (Summarised)");
            HSSFSheet sheet3 = workbook.createSheet("Fin - Acquittal (Detailed)");
//            HSSFSheet sheet4 = workbook.createSheet("Fin - Refund Deposit Slips");

//            HSSFRow rowhead1 = sheet1.createRow((short) 0);
            HSSFRow rowhead2 = sheet2.createRow((short) 2);
            HSSFRow rowhead3 = sheet2.createRow((short) 0);
            HSSFRow rowhead4 = sheet3.createRow((short) 0);
//            HSSFRow rowhead5 = sheet4.createRow((short) 0);

            rowhead3.createCell((short) 8).setCellValue("Acquittal Batch Number " + batNum + " Version 1");
//            rowhead1.createCell((short) 0).setCellValue("Serial.");
//            rowhead1.createCell((short) 1).setCellValue("Reference No.");
//            rowhead1.createCell((short) 2).setCellValue("Reference Date");
//            rowhead1.createCell((short) 3).setCellValue("Prog Adv Acc");
//            rowhead1.createCell((short) 4).setCellValue("Employee Name");
//            rowhead1.createCell((short) 5).setCellValue("Province");
//            rowhead1.createCell((short) 6).setCellValue("District/Facility");
//            rowhead1.createCell((short) 7).setCellValue("Budget Code");
//            rowhead1.createCell((short) 8).setCellValue("Main Activity Purpose");
//            rowhead1.createCell((short) 9).setCellValue("Total");

            rowhead2.createCell((short) 0).setCellValue("Serial.");
            rowhead2.createCell((short) 1).setCellValue("Reference No.");
            rowhead2.createCell((short) 2).setCellValue("Reference Date");
            rowhead2.createCell((short) 3).setCellValue("Acq. Serial.");
            rowhead2.createCell((short) 4).setCellValue("Acq Ref. No.");
            rowhead2.createCell((short) 5).setCellValue("Acq Ref. Date");
            rowhead2.createCell((short) 6).setCellValue("Prog Adv Acc");
            rowhead2.createCell((short) 7).setCellValue("Employee Name");
            rowhead2.createCell((short) 8).setCellValue("Province");
            rowhead2.createCell((short) 9).setCellValue("District/Facility");
            rowhead2.createCell((short) 10).setCellValue("Budget Code");
            rowhead2.createCell((short) 11).setCellValue("Main Activity Purpose");
            rowhead4.createCell((short) 12).setCellValue("Week Acquitted");
            rowhead2.createCell((short) 13).setCellValue("Total");

            rowhead4.createCell((short) 0).setCellValue("Serial.");
            rowhead4.createCell((short) 1).setCellValue("Reference No.");
            rowhead4.createCell((short) 2).setCellValue("Reference Date");
            rowhead4.createCell((short) 3).setCellValue("Prog Adv Acc");
            rowhead4.createCell((short) 4).setCellValue("Employee Name");
            rowhead4.createCell((short) 5).setCellValue("Province");
            rowhead4.createCell((short) 6).setCellValue("District/Facility");
            rowhead4.createCell((short) 7).setCellValue("Activity Date");
            rowhead4.createCell((short) 8).setCellValue("Activity Budget Code");
            rowhead4.createCell((short) 9).setCellValue("Activity Purpose");
            rowhead4.createCell((short) 10).setCellValue("Week Acquitted");
            rowhead4.createCell((short) 11).setCellValue("Activity Line Total");

//            rowhead5.createCell((short) 0).setCellValue("Request Serial.");
//            rowhead5.createCell((short) 1).setCellValue("Request Reference No.");
//            rowhead5.createCell((short) 2).setCellValue("Acquittal Serial.");
//            rowhead5.createCell((short) 3).setCellValue("Acquittal Reference No.");
//            rowhead5.createCell((short) 4).setCellValue("Prog Adv Acc");
//            rowhead5.createCell((short) 5).setCellValue("Employee Name");
//            rowhead5.createCell((short) 6).setCellValue("Week Acquitted");
            int i = 1;
//            while (rs1.next()) {
//
//                HSSFRow row1 = sheet1.createRow((short) i);
//                row1.createCell((short) 0).setCellValue(rs1.getString(1));
//                row1.createCell((short) 1).setCellValue(Integer.toString(rs1.getInt(2)));
//                row1.createCell((short) 2).setCellValue(rs1.getString(3));
//                row1.createCell((short) 3).setCellValue(rs1.getString(4));
//                row1.createCell((short) 4).setCellValue(rs1.getString(5));
//                row1.createCell((short) 5).setCellValue(rs1.getString(8));
//                row1.createCell((short) 6).setCellValue(rs1.getString(9));
//                row1.createCell((short) 7).setCellValue(rs1.getString(10));
//                row1.createCell((short) 8).setCellValue(rs1.getString(11));
//                row1.createCell((short) 9).setCellValue(rs1.getString(12));
//
//                i++;
//            }

            int n = 3;
            while (rs.next()) {

                HSSFRow row2 = sheet2.createRow((short) n);
                row2.createCell((short) 0).setCellValue(rs.getString(1));
                row2.createCell((short) 1).setCellValue(Integer.toString(rs.getInt(2)));
                row2.createCell((short) 2).setCellValue(rs.getString(3));
                row2.createCell((short) 3).setCellValue(rs.getString(4));
                row2.createCell((short) 4).setCellValue(rs.getString(5));
                row2.createCell((short) 5).setCellValue(rs.getString(6));
                row2.createCell((short) 6).setCellValue(rs.getString(7));
                row2.createCell((short) 7).setCellValue(rs.getString(8));
                row2.createCell((short) 8).setCellValue(rs.getString(9));
                row2.createCell((short) 9).setCellValue(rs.getString(10));
                row2.createCell((short) 10).setCellValue(rs.getString(11));
                row2.createCell((short) 8).setCellValue(rs.getString(12));
                row2.createCell((short) 9).setCellValue(rs.getString(13));
                row2.createCell((short) 10).setCellValue(rs.getString(14));

                n++;
            }

            int x = 1;
            while (rs2.next()) {

                HSSFRow row3 = sheet3.createRow((short) x);
                row3.createCell((short) 0).setCellValue(rs2.getString(1));
                row3.createCell((short) 1).setCellValue(Integer.toString(rs2.getInt(2)));
                row3.createCell((short) 2).setCellValue(rs2.getString(3));
                row3.createCell((short) 3).setCellValue(rs2.getString(4));
                row3.createCell((short) 4).setCellValue(rs2.getString(5));
                row3.createCell((short) 5).setCellValue(rs2.getString(6));
                row3.createCell((short) 6).setCellValue(rs2.getString(7));
                row3.createCell((short) 7).setCellValue(rs2.getString(8));
                row3.createCell((short) 8).setCellValue(rs2.getString(9));
                row3.createCell((short) 9).setCellValue(rs2.getString(10));
                row3.createCell((short) 10).setCellValue(rs2.getString(11));
                row3.createCell((short) 11).setCellValue(rs2.getString(12));

                x++;
            }

            int y = 1;
//            while (rs3.next()) {
//
//                HSSFRow row4 = sheet4.createRow((short) y);
//                row4.createCell((short) 0).setCellValue(rs3.getString(1));
//                row4.createCell((short) 1).setCellValue(Integer.toString(rs3.getInt(2)));
//                row4.createCell((short) 2).setCellValue(rs3.getString(3));
//                row4.createCell((short) 3).setCellValue(Integer.toString(rs3.getInt(4)));
//                row4.createCell((short) 4).setCellValue(rs3.getString(5));
//                row4.createCell((short) 5).setCellValue(rs3.getString(6));
//                row4.createCell((short) 6).setCellValue(rs3.getString(7));
//
//                y++;
//            }

            Date today = new Date();

            String reportFile = "C:\\Finance System Reports\\Acquittal Schedule\\AcquttalSchBat #" + batNum + " Version 1 - Generated " + sDateTime.format(today) + ".xls";
            FileOutputStream fileOut = new FileOutputStream(reportFile);
            workbook.write(fileOut);
            fileOut.close();
            String fileName = "AcquttalSchBat #" + batNum + " Version 1 - Generated " + sDateTime.format(today) + ".xls";
            String mailHeader = "Acquttal extraction batch# " + batNum + " version " + batVer;
            String mailMsg = "Please find acquttal schedule attachment for batch# "
                    + "<b>" + batNum + "</b> version <b>" + batVer + "</b> generated by<b> " + jLabelGenLogNam.getText() + "</b>.";

            recSend.sendMailExtract(mailHeader, reportFile, mailMsg, fileName);
            jDialogWaiting.setVisible(false);
            JOptionPane.showMessageDialog(this, "Your excel file AcquttalSchBat # " + batNum + " version  " + batVer + " - Generated " + sDateTime.format(today) + ".xls has been generated"
                    + " and email sent to the finance team");

            new JFrameReqHQAcqScheduleApp(jLabelEmp.getText()).setVisible(true);
            setVisible(false);
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (FileNotFoundException e1) {
            jDialogWaiting.setVisible(false);
            JOptionPane.showMessageDialog(this, e1);
        } catch (IOException e1) {
            jDialogWaiting.setVisible(false);
            JOptionPane.showMessageDialog(this, e1);
        }
    }

    void retriveUsrData() {
        try {
            findBatNum();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            st.executeQuery("SELECT a.PREV_SERIAL,a.PREV_REF_NUM,a.PREV_REF_DAT,concat('E',a.EMP_NUM),a.EMP_NAM,a.EMP_PROV,a.EMP_OFF,BUD_CODE,a.ACT_MAIN_PUR,b.PLAN_WK,"
                    + "(sum(b.BRK_AMT)+ sum(b.LNC_AMT)+sum(b.DIN_AMT)+ sum(b.INC_AMT) + sum(b.MSC_AMT) + sum(b.ACC_UNPROV_AMT)) \"total\""
                    + " ,max(a.DOC_VER) "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a inner join [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] b on a.SERIAL=b.SERIAL and a.REF_NUM=b.REF_NUM "
                    + "and a.ACT_REC_STA = b.ACT_REC_STA  inner join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab]c on b.SERIAL=c.SERIAL and b.REF_NUM=c.REF_NUM "
                    + "and b.ACT_REC_STA = c.ACT_REC_STA "
                    + "where c.DOC_STATUS='HeadApprove' "
                    + "and a.ACT_REC_STA = 'Q'"
                    + "and a.PREV_REF_NUM ='" + jTextAcqRegNum.getText() + "'"
                    + "and b.PLAN_WK ='" + jTextWeek.getText() + "'"
                    + " group by a.PREV_SERIAL,a.PREV_REF_NUM,a.PREV_REF_DAT,a.EMP_NUM,concat('E',a.EMP_NUM) ,a.EMP_NAM,a.EMP_BNK_NAM,a.ACC_NUM,a.EMP_PROV,a.EMP_OFF,BUD_CODE,a.ACT_MAIN_PUR,b.PLAN_WK");

            ResultSet rs = st.getResultSet();
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("Fin - For Acquittal Individual");
            HSSFRow rowhead = sheet.createRow((short) 0);
            rowhead.createCell((short) 0).setCellValue("Serial.");
            rowhead.createCell((short) 1).setCellValue("Reference No.");
            rowhead.createCell((short) 2).setCellValue("Reference Date");
            rowhead.createCell((short) 3).setCellValue("Prog Adv Acc");
            rowhead.createCell((short) 4).setCellValue("Employee Name");
            rowhead.createCell((short) 5).setCellValue("Province");
            rowhead.createCell((short) 6).setCellValue("District/Facility");
            rowhead.createCell((short) 7).setCellValue("Budget Code");
            rowhead.createCell((short) 8).setCellValue("Main Activity Purpose");
            rowhead.createCell((short) 9).setCellValue("Acquitted Week");
            rowhead.createCell((short) 10).setCellValue("Total");

            jDialogWaiting.setVisible(true);
            int i = 1;
            while (rs.next()) {

                HSSFRow row = sheet.createRow((short) i);
                row.createCell((short) 0).setCellValue(rs.getString(1));
                row.createCell((short) 1).setCellValue(Integer.toString(rs.getInt(2)));
                row.createCell((short) 2).setCellValue(rs.getString(3));
                row.createCell((short) 3).setCellValue(rs.getString(4));
                row.createCell((short) 4).setCellValue(rs.getString(5));
                row.createCell((short) 5).setCellValue(rs.getString(6));
                row.createCell((short) 6).setCellValue(rs.getString(7));
                row.createCell((short) 7).setCellValue(rs.getString(8));
                row.createCell((short) 8).setCellValue(rs.getString(9));
                row.createCell((short) 9).setCellValue(rs.getString(10));
                row.createCell((short) 10).setCellValue(rs.getString(11));

                i++;
            }

            String reportFile = "C:\\Finance System Reports\\Acquittal Schedule\\AcquttalSchBat # " + usrBatNum + " Version " + newBatVer + ".xls";
            FileOutputStream fileOut = new FileOutputStream(reportFile);
            workbook.write(fileOut);
            fileOut.close();
            jDialogWaiting.setVisible(false);
            //batchUpdUsr();
            JOptionPane.showMessageDialog(this, "Your excel file  AcquttalSchBat #" + usrBatNum + " Version " + newBatVer + ".xls has been generated.");

        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (FileNotFoundException e1) {
            jDialogWaiting.setVisible(false);
            JOptionPane.showMessageDialog(this, e1);
        } catch (IOException e1) {
            jDialogWaiting.setVisible(false);
            JOptionPane.showMessageDialog(this, e1);
        }
    }

    void folderCreate() {
        File tmpDir = new File("C:\\Finance System Reports\\Acquittal Schedule");
        boolean exists = tmpDir.exists();
        if (exists) {

            System.out.println("Folder exists");

        } else {
            try {
                Path path = Paths.get("C:\\Finance System Reports\\Acquittal Schedule");

                Files.createDirectories(path);

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    void batchUpd() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            st.executeQuery("SELECT count(*) "
                    + "FROM ClaimsAppSysZvandiri.dbo.ClaimAppGenTab a,ClaimsAppSysZvandiri.dbo.ClaimAppItmTab b, ClaimsAppSysZvandiri.dbo.ClaimsWFActTab c "
                    + "where (a.REF_NUM = b.REF_NUM and a.DOC_VER =b.DOC_VER and a.REF_NUM = c.REF_NUM and a.DOC_VER =c.DOC_VER) and c.DOC_STATUS='HODAcqApprove' "
                    + "and a.ACT_REC_STA = 'Q'"
                    + "and a.PREV_SERIAL = 'R'  and a.SERIAL = 'A'"
                    + "and concat(a.PREV_REF_NUM,b.PLAN_WK) not in (SELECT distinct concat(REF_NUM,PLAN_WK) FROM [ClaimsAppSysZvandiri].[dbo].[BatRunTab] where SERIAL = 'R' and STATUS = 'Acquitted')"
                    + "group by a.PREV_SERIAL,a.PREV_REF_NUM,a.PREV_REF_DAT,a.EMP_NUM,concat('E',a.EMP_NUM) ,a.EMP_NAM,a.EMP_BNK_NAM,a.ACC_NUM,a.EMP_PROV,BUD_CODE,a.ACT_MAIN_PUR,b.PLAN_WK");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                batCount = r.getString(1);
            }
            if (batCount == null) {
                JOptionPane.showMessageDialog(this, "Batch run has no records. Please check dates and run again.");
                jDateFrom.requestFocusInWindow();
                jDateFrom.setFocusable(true);

            } else {
                try {
                    retriveData();

                    String sql = "insert into [ClaimsAppSysZvandiri].[dbo].[BatRunTab] "
                            + "( SERIAL,REF_NUM,BAT_TYP ,BAT_NUM,BAT_VER,RUN_BY,STATUS,RUN_DAT,RUN_TIME,ACTIONED_ON_COMPUTER,PLAN_WK) "
                            + "SELECT a.PREV_SERIAL,a.PREV_REF_NUM,'acqBat','"+ batNum + "','1','"+ jLabelGenLogNam.getText() +" ' ,"
                            + "'Acquitted','" + jLabelDate.getText() + "',' "+ jLabelTime.getText() + "',' "+ hostName + "',b.PLAN_WK  "
                            + "FROM ClaimsAppSysZvandiri.dbo.ClaimAppGenTab a join ClaimsAppSysZvandiri.dbo.ClaimAppItmTab b "
                            + "on a.Serial=b.SERIAL and a.REF_NUM = b.REF_NUM and a.DOC_VER =b.DOC_VER and a.ACT_REC_STA=b.ACT_REC_STA "
                            + "join ClaimsAppSysZvandiri.dbo.ClaimsWFActTab c on c.Serial=b.SERIAL and c.REF_NUM = b.REF_NUM "
                            + "and c.DOC_VER =b.DOC_VER and c.ACT_REC_STA=b.ACT_REC_STA where  c.DOC_STATUS='HODAcqApprove' "
                            + "and c.DOC_STATUS='HODAcqApprove'  and a.ACT_REC_STA = 'Q' and a.PREV_SERIAL = 'R'  "
                            + "and concat(a.PREV_REF_NUM,b.PLAN_WK) not in (SELECT distinct  concat(REF_NUM,PLAN_WK) "
                            + "FROM [ClaimsAppSysZvandiri].[dbo].[BatRunTab] where SERIAL = 'R' and STATUS = 'Acquitted')  "
                            + "group by a.PREV_SERIAL,a.PREV_REF_NUM,a.PREV_REF_DAT,a.EMP_NUM,concat('E',a.EMP_NUM) ,a.EMP_NAM,"
                            + "a.EMP_BNK_NAM,a.ACC_NUM,a.EMP_PROV,BUD_CODE,a.ACT_MAIN_PUR,b.PLAN_WK;";
                           
                    pst = conn.prepareStatement(sql);

                    pst.executeUpdate();
                    System.out.println("two");
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

        } catch (Exception e1) {
            System.out.println(e1);
        }
    }

    void batchUpdUsr() {
        try {

            String sql = "insert into [ClaimsAppSysZvandiri].[dbo].[BatRunTab] ( SERIAL,REF_NUM,BAT_TYP ,BAT_NUM,BAT_VER,RUN_BY,STATUS,RUN_DAT,RUN_TIME,ACTIONED_ON_COMPUTER,PLAN_WK)"
                    + "values (?,?,?,?,?,?,?,?,?,?,?)";

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            pst = conn.prepareStatement(sql);
            pst.setString(1, "R");
            pst.setString(2, jTextAcqRegNum.getText());
            pst.setString(3, "acqBat");
            pst.setString(4, usrBatNum);
            pst.setString(5, newBatVer);
            pst.setString(6, jLabelGenLogNam.getText());
            pst.setString(7, "Acquitted");
            pst.setString(8, jLabelDate.getText());
            pst.setString(9, jLabelTime.getText());
            pst.setString(10, hostName);
            pst.setString(11, jTextWeek.getText());

            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
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
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableActivityFinSchedule = new javax.swing.JTable();
        jDateTo = new com.toedter.calendar.JDateChooser();
        jLabelRangeSelect = new javax.swing.JLabel();
        jLabelDateTo = new javax.swing.JLabel();
        jLabelDateFrom = new javax.swing.JLabel();
        jDateFrom = new com.toedter.calendar.JDateChooser();
        jButtonGenExcel = new javax.swing.JButton();
        jButtonDisplayResults = new javax.swing.JButton();
        jLabelEmp = new javax.swing.JLabel();
        jSeparator12 = new javax.swing.JSeparator();
        jLabelSingleUser = new javax.swing.JLabel();
        jLabelUserRef = new javax.swing.JLabel();
        jTextAcqRegNum = new javax.swing.JTextField();
        jButtonUsrFile = new javax.swing.JButton();
        jLabelWeek = new javax.swing.JLabel();
        jTextWeek = new javax.swing.JTextField();
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
        setTitle("PERDIEM ACQUITTAL");
        setResizable(false);
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
        jLabelTime.setBounds(1220, 0, 80, 30);

        jLabellogged1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged1.setText("Logged In");
        jPanelItem1.add(jLabellogged1);
        jLabellogged1.setBounds(1080, 30, 80, 30);

        jLabelGenLogNam.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam.setForeground(new java.awt.Color(255, 255, 255));
        jPanelItem1.add(jLabelGenLogNam);
        jLabelGenLogNam.setBounds(1160, 30, 190, 30);

        jTableActivityFinSchedule.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jTableActivityFinSchedule.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Serial", "Reference No.", "Reference Date", "Prog. Adv. Number", "Employee Name", "Province", "District/Facility", "Budget Code", "Main Activity Purpose", "Week Acquitted", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
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
        jTableActivityFinSchedule.setSelectionForeground(new java.awt.Color(0, 0, 0));
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

        jPanelItem1.add(jScrollPane2);
        jScrollPane2.setBounds(0, 115, 1355, 450);

        jDateTo.setDateFormatString("dd-MM-yyyy");
        jPanelItem1.add(jDateTo);
        jDateTo.setBounds(770, 630, 160, 25);

        jLabelRangeSelect.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelRangeSelect.setText("Please Select date Range");
        jPanelItem1.add(jLabelRangeSelect);
        jLabelRangeSelect.setBounds(640, 570, 170, 25);

        jLabelDateTo.setText("Date To");
        jPanelItem1.add(jLabelDateTo);
        jLabelDateTo.setBounds(640, 630, 80, 25);

        jLabelDateFrom.setText("Date From");
        jPanelItem1.add(jLabelDateFrom);
        jLabelDateFrom.setBounds(640, 600, 80, 25);

        jDateFrom.setDateFormatString("dd-MM-yyyy");
        jPanelItem1.add(jDateFrom);
        jDateFrom.setBounds(770, 600, 160, 25);

        jButtonGenExcel.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonGenExcel.setText("<html>Generate batch <br> Excel Spreadsheet </html>");
        jButtonGenExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenExcelActionPerformed(evt);
            }
        });
        jPanelItem1.add(jButtonGenExcel);
        jButtonGenExcel.setBounds(330, 600, 150, 40);

        jButtonDisplayResults.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonDisplayResults.setText("<html>Display Acquittal <br>Results</html>");
        jButtonDisplayResults.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDisplayResultsActionPerformed(evt);
            }
        });
        jPanelItem1.add(jButtonDisplayResults);
        jButtonDisplayResults.setBounds(120, 600, 150, 40);

        jLabelEmp.setForeground(new java.awt.Color(255, 255, 255));
        jPanelItem1.add(jLabelEmp);
        jLabelEmp.setBounds(1110, 70, 50, 20);

        jSeparator12.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanelItem1.add(jSeparator12);
        jSeparator12.setBounds(700, 570, 5, 90);

        jLabelSingleUser.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelSingleUser.setText("Single User File");
        jPanelItem1.add(jLabelSingleUser);
        jLabelSingleUser.setBounds(930, 580, 100, 30);

        jLabelUserRef.setText("Request Ref. Number");
        jPanelItem1.add(jLabelUserRef);
        jLabelUserRef.setBounds(930, 620, 140, 30);

        jTextAcqRegNum.setToolTipText("Enter Perdiem Request Number");
        jPanelItem1.add(jTextAcqRegNum);
        jTextAcqRegNum.setBounds(1080, 620, 59, 30);

        jButtonUsrFile.setText("Generate File");
        jButtonUsrFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUsrFileActionPerformed(evt);
            }
        });
        jPanelItem1.add(jButtonUsrFile);
        jButtonUsrFile.setBounds(1140, 620, 120, 30);

        jLabelWeek.setText("Week No.");
        jPanelItem1.add(jLabelWeek);
        jLabelWeek.setBounds(1140, 580, 60, 30);
        jPanelItem1.add(jTextWeek);
        jTextWeek.setBounds(1210, 580, 40, 30);

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

    private void jButtonDisplayResultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDisplayResultsActionPerformed
        fetchdata();
    }//GEN-LAST:event_jButtonDisplayResultsActionPerformed

    private void jButtonGenExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenExcelActionPerformed
        jDialogWaiting.setVisible(true);
        batchUpd();
    }//GEN-LAST:event_jButtonGenExcelActionPerformed

    private void jButtonUsrFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUsrFileActionPerformed
        if ((jTextAcqRegNum.getText().length() == 0) || ((jTextWeek.getText().length() == 0))) {
            JOptionPane.showMessageDialog(this, "Please enter user reference No.");
            jTextAcqRegNum.requestFocusInWindow();
            jTextAcqRegNum.setFocusable(true);
        } else {
            findExistUsr();
            if ("0".equals(usrCount)) {
                JOptionPane.showMessageDialog(this, "Batch Run has not been done.Please do a batch run first.");
            } else {
                findBatNumUsr();
                int dialogButton = JOptionPane.YES_NO_OPTION;
                JOptionPane.showConfirmDialog(null, "User Acquittal already exist on batch# " + usrBatNum
                        + " version # " + usrBatVer + ". Do you want to generate an individual spreadsheet", "WARNING", dialogButton);
                if (dialogButton == JOptionPane.YES_OPTION) {
                    batchUpdUsr();
                    retriveUsrData();
                    if (dialogButton == JOptionPane.NO_OPTION) {
                        remove(dialogButton);
                    }
                    jTextAcqRegNum.setText("");
                    jTextWeek.setText("");
                }

            }
        }
    }//GEN-LAST:event_jButtonUsrFileActionPerformed

    private void jMenuPlanApprovalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuPlanApprovalActionPerformed

    }//GEN-LAST:event_jMenuPlanApprovalActionPerformed

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
            java.util.logging.Logger.getLogger(JFrameReqHQAcqScheduleApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameReqHQAcqScheduleApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameReqHQAcqScheduleApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameReqHQAcqScheduleApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new JFrameReqHQAcqScheduleApp().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupAcc;
    private javax.swing.JButton jButtonDisplayResults;
    private javax.swing.JButton jButtonGenExcel;
    private javax.swing.JButton jButtonUsrFile;
    private com.toedter.calendar.JDateChooser jDateFrom;
    private com.toedter.calendar.JDateChooser jDateTo;
    private javax.swing.JDialog jDialogWaiting;
    private javax.swing.JLabel jLabelDate;
    private javax.swing.JLabel jLabelDateFrom;
    private javax.swing.JLabel jLabelDateTo;
    private javax.swing.JLabel jLabelEmp;
    private javax.swing.JLabel jLabelGenLogNam;
    private javax.swing.JLabel jLabelHeaderLine;
    private javax.swing.JLabel jLabelLogo;
    private javax.swing.JLabel jLabelRangeSelect;
    private javax.swing.JLabel jLabelSingleUser;
    private javax.swing.JLabel jLabelTime;
    private javax.swing.JLabel jLabelUserRef;
    private javax.swing.JLabel jLabelWeek;
    private javax.swing.JLabel jLabellogged1;
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
    private javax.swing.JPanel jPanelItem1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JPopupMenu.Separator jSeparator13;
    private javax.swing.JPopupMenu.Separator jSeparator16;
    private javax.swing.JPopupMenu.Separator jSeparator18;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator22;
    private javax.swing.JPopupMenu.Separator jSeparator23;
    private javax.swing.JPopupMenu.Separator jSeparator26;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator34;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator62;
    private javax.swing.JPopupMenu.Separator jSeparator63;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JTable jTableActivityFinSchedule;
    private javax.swing.JTextField jTextAcqRegNum;
    private javax.swing.JTextField jTextWeek;
    // End of variables declaration//GEN-END:variables
}
