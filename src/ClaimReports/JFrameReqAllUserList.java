/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClaimReports;

import ClaimPlanPerDiem.JFrameMnthPlanPerDiemCreate;
import ClaimPlanPerDiem.JFrameMnthPlanPerDiemEdit;
import ClaimAppendix1.*;
import ClaimAppendix2.*;
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
import ClaimPlan.*;
import javax.swing.table.TableColumn;
import ClaimPlan.*;
import utils.StockVehicleMgt;
import utils.connCred;

/**
 *
 * @author cgoredema
 */
public class JFrameReqAllUserList extends javax.swing.JFrame {

    PreparedStatement pst = null;
    PreparedStatement pst1 = null;
    connCred c = new connCred();
    DefaultTableModel model, model1;
    int itmNum = 1;
    int month, finyear;
    Date curYear = new Date();
    double totSeg1 = 0;
    double totSeg2 = 0;
    String sendTo, createUsrNam, breakfastAll, lunchAll, dinnerAll, incidentalAll,
            unProvedAll, batNum, batVer, usrCount, usrBatNum, usrBatVer, newBatVer,
            batCount, regStatus, regCount, usrGrp, actStatus, reqSerial;
    SimpleDateFormat df = new SimpleDateFormat("yyyy");
    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
    String hostName = "";

    /**
     * Creates new form JFrameTabApp1
     */
    public JFrameReqAllUserList() {
        initComponents();

    }

    public JFrameReqAllUserList(String usrLogNum) {
        initComponents();
        showDate();
        showTime();
        model = (DefaultTableModel) jTableActivityAllUser.getModel();
        jLabelEmp.setText(usrLogNum);
        jLabelEmp.setVisible(false);
        jTextSearchEmpNam.setEditable(false);
        jLabelXtrsRef.setVisible(false);
        jCheckBoxAllEmp.setSelected(false);
        jRadioButtonNormal.setSelected(true);
        jRadioButtonSpecial.setSelected(false);
        jRadioButtonNormal.setVisible(false);
        jRadioButtonSpecial.setVisible(false);
        jSeparator22.setVisible(false);
        jMenuItemUserProfUpd.setVisible(false);
        findUser();
        findUserGrp();
        computerName();

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

    void radioNormSpec() {
        if (jRadioButtonNormal.isSelected()) {
            actStatus = "A";
            reqSerial = "R";
        } else if (jRadioButtonSpecial.isSelected()) {
            actStatus = "Q";
            reqSerial = "S";
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
    }

    void fetchdataAllbyUser() {
        radioNormSpec();
        if ((jDateFrom.getDate() == null) || (jDateTo.getDate() == null)) {
            JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
            jDateFrom.requestFocusInWindow();
            jDateFrom.setFocusable(true);
        } else if ((jDateFrom.getDate().after(jDateTo.getDate())) && (!(jDateFrom.getDate().equals(jDateTo.getDate())))) {
            JOptionPane.showMessageDialog(this, "End date cannot be lower",
                    "Error", JOptionPane.ERROR_MESSAGE);
            jDateTo.requestFocusInWindow();
            jDateTo.setFocusable(true);

        } else {
            try {
                clearTable();
                jTableActivityAllUser.getColumnModel().getColumn(6).setHeaderValue("Status");
                jTableActivityAllUser.getColumnModel().getColumn(7).setHeaderValue("Actioned By");
                jTableActivityAllUser.getColumnModel().getColumn(8).setHeaderValue("Action Date");
                Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                        + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

                Statement st = conn.createStatement();

                st.executeQuery("SELECT distinct a.SERIAL,a.REF_NUM ,a.REF_DAT,a.EMP_NAM,a.ACT_TOT_AMT,a.ACT_MAIN_PUR,b.USR_ACTION,ACTIONED_BY_NAM,ACTIONED_ON_DATE,DATEDIFF(day, a.REF_DAT, getDate())  ,b.ACTIONED_ON_DATE,DATEDIFF(day, b.ACTIONED_ON_DATE, getDate()) "
                        + " FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] b"
                        + " on a.serial = b.serial and a.ref_num = b.ref_num and a.ACT_REC_STA = b.ACT_REC_STA and a.doc_ver = b.doc_ver  "
                        + "where (a.ACT_REC_STA = '" + actStatus + "' and a.SERIAL = '" + reqSerial + "')   "
                        + "and a.EMP_NUM = '" + jLabelEmp.getText() + "'"
                        + "and a.REF_DAT between '" + s.format(jDateFrom.getDate()) + "' and '" + s.format(jDateTo.getDate()) + "'"
                        + "order by 2");

                ResultSet r = st.getResultSet();

                while (r.next()) {
                    model.insertRow(model.getRowCount(), new Object[]{r.getString(1), r.getString(2), r.getString(3),
                        r.getString(4), r.getString(5), r.getString(6), r.getString(7), r.getString(8), r.getString(9)});

                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    void fetchdataOutstandingbyUser() {
        clearTable();
        if ((jDateFrom.getDate() == null) || (jDateTo.getDate() == null)) {
            JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
            jDateFrom.requestFocusInWindow();
            jDateFrom.setFocusable(true);
        } else if ((jDateFrom.getDate().after(jDateTo.getDate())) && (!(jDateFrom.getDate().equals(jDateTo.getDate())))) {
            JOptionPane.showMessageDialog(this, "End date cannot be lower",
                    "Error", JOptionPane.ERROR_MESSAGE);
            jDateTo.requestFocusInWindow();
            jDateTo.setFocusable(true);

        } else {
            try {

                jTableActivityAllUser.getColumnModel().getColumn(6).setHeaderValue("Activity Week");
                jTableActivityAllUser.getColumnModel().getColumn(7).setHeaderValue("");
                jTableActivityAllUser.getColumnModel().getColumn(8).setHeaderValue("");

                Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                        + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

                Statement st = conn.createStatement();

                st.executeQuery("select * from (SELECT a.REF_YEAR \n"
                        + "      ,a.SERIAL \n"
                        + "      ,a.REF_NUM,b.REF_DAT,b.EMP_NAM,a.PLAN_WK \"Week\" \n"
                        + "      ,sum(a.ACT_ITM_TOT) \"Total\",b.ACT_MAIN_PUR,a.PLAN_WK       \n"
                        + "  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] a join [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] b \n"
                        + "  on a.serial = b.serial and a.DOC_VER = b.DOC_VER and  a.ACT_REC_STA = b.ACT_REC_STA and a.REF_NUM = b.REF_NUM \n"
                        + "  where a.serial = 'R' \n"
                        + "   and a.DOC_VER = 5 \n"
                        + "  and a.ACT_REC_STA = 'A' \n"
                        + "  and a.ACT_DATE < GETDATE() and b.EMP_NUM ='" + jLabelEmp.getText() + "' and "
                        + "b.REF_DAT between '" + s.format(jDateFrom.getDate()) + "' and '" + s.format(jDateTo.getDate()) + "'\n"
                        + "  and CONCAT(a.Serial,a.ref_num)  not in (SELECT concat(SERIAL,REF_NUM) \n"
                        + "        FROM [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] \n"
                        + "  where USR_ACTION like '%Reject%'  and SERIAL = 'R') \n"
                        + "  and concat(a.SERIAL,a.REF_NUM) in (SELECT distinct concat(SERIAL,REF_NUM) \n"
                        + "  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] where  SERIAL = 'R') \n"
                        + "  group by b.REF_DAT,a.REF_YEAR,a.SERIAL,a.REF_NUM,a.PLAN_WK,b.EMP_NAM,b.ACT_MAIN_PUR,a.PLAN_WK) y \n"
                        + "  left join (SELECT a.REF_YEAR \n"
                        + "      ,a.SERIAL \n"
                        + "      ,a.REF_NUM,b.EMP_NAM,b.PREV_REF_NUM,a.PLAN_WK \"Week\"\n"
                        + "    ,sum(a.ACT_ITM_TOT) \"Total\"        \n"
                        + "  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] a join [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] b \n"
                        + "  on a.serial = b.serial and a.DOC_VER = b.DOC_VER and  a.ACT_REC_STA = b.ACT_REC_STA and a.REF_NUM = b.REF_NUM \n"
                        + "  where a.serial = 'A' \n"
                        + "  and a.ACT_REC_STA = 'Q' \n"
                        + "  and CONCAT(a.Serial,a.ref_num)  not in (SELECT concat(SERIAL,REF_NUM) \n"
                        + "        FROM [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] \n"
                        + "  where USR_ACTION like '%Reject%'  and SERIAL = 'A') \n"
                        + "  and concat(a.SERIAL,a.REF_NUM) in (SELECT distinct concat(SERIAL,REF_NUM) \n"
                        + "  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] where SERIAL = 'A' ) \n"
                        + "    group by a.REF_YEAR,a.SERIAL,a.REF_NUM,a.PLAN_WK,b.EMP_NAM,b.PREV_REF_NUM) z \n"
                        + "	on (y.REF_NUM = z.PREV_REF_NUM and y.Week = z.Week) \n"
                        + "	where z.PREV_REF_NUM is null  and y.REF_NUM not in (SELECT PREV_REF_NUM FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppManualGenTab])");
                ResultSet r = st.getResultSet();

                while (r.next()) {
                    model.insertRow(model.getRowCount(), new Object[]{r.getString(2), r.getString(3), r.getString(4),
                        r.getString(5), r.getString(7), r.getString(8), "Week " + r.getString(6)});

                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    void fetchdataAcquittedbyUser() {
        radioNormSpec();
        if ((jDateFrom.getDate() == null) || (jDateTo.getDate() == null)) {
            JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
            jDateFrom.requestFocusInWindow();
            jDateFrom.setFocusable(true);
        } else if ((jDateFrom.getDate().after(jDateTo.getDate())) && (!(jDateFrom.getDate().equals(jDateTo.getDate())))) {
            JOptionPane.showMessageDialog(this, "End date cannot be lower",
                    "Error", JOptionPane.ERROR_MESSAGE);
            jDateTo.requestFocusInWindow();
            jDateTo.setFocusable(true);

        } else {
            try {
                clearTable();
                Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                        + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

                Statement st = conn.createStatement();

                st.executeQuery("SELECT distinct a.SERIAL,a.REF_NUM ,a.REF_DAT,a.EMP_NAM,a.ACT_TOT_AMT,a.ACT_MAIN_PUR,b.USR_ACTION,"
                        + "ACTIONED_BY_NAM,ACTIONED_ON_DATE,DATEDIFF"
                        + "(day, a.REF_DAT, getDate())  ,b.ACTIONED_ON_DATE,DATEDIFF(day, b.ACTIONED_ON_DATE, getDate()),a.PREV_SERIAL,a.PREV_REF_NUM ,a.PREV_REF_DAT "
                        + " FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] b"
                        + " on a.serial = b.serial and a.ref_num = b.ref_num and a.ACT_REC_STA = b.ACT_REC_STA and a.doc_ver = b.doc_ver  "
                        + "where (a.ACT_REC_STA = 'Q' and a.SERIAL = 'A')"
                        + "and a.EMP_NAM = '" + jLabelEmp.getText() + "'"
                        + "and a.REF_DAT between '" + s.format(jDateFrom.getDate()) + "' and '" + s.format(jDateTo.getDate()) + "'"
                        + " and a.PREV_REF_DAT != '1900-01-01' and a.PREV_REF_NUM != 0"
                        + "order by 2");

                ResultSet r = st.getResultSet();

                while (r.next()) {
                    model.insertRow(model.getRowCount(), new Object[]{r.getString(1), r.getString(2), r.getString(3),
                        r.getString(4), r.getString(5), r.getString(6), r.getString(7), r.getString(8), r.getString(9),
                        r.getString(13), r.getString(14), r.getString(15)});

                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    void fetchdataMgtAllbyUser() {
        radioNormSpec();
        clearTable();
        if ((jDateFrom.getDate() == null) || (jDateTo.getDate() == null)) {
            JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
            jDateFrom.requestFocusInWindow();
            jDateFrom.setFocusable(true);
        } else if ((jDateFrom.getDate().after(jDateTo.getDate())) && (!(jDateFrom.getDate().equals(jDateTo.getDate())))) {
            JOptionPane.showMessageDialog(this, "End date cannot be lower",
                    "Error", JOptionPane.ERROR_MESSAGE);
            jDateTo.requestFocusInWindow();
            jDateTo.setFocusable(true);

        } else if ("".equals(jTextSearchEmpNam.getText())) {
            JOptionPane.showMessageDialog(this, "User name cannot be blank",
                    "Error", JOptionPane.ERROR_MESSAGE);
            jDateTo.requestFocusInWindow();
            jDateTo.setFocusable(true);

        } else {
            try {
                clearTable();
                radioNormSpec();
                Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                        + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

                Statement st = conn.createStatement();

                st.executeQuery("SELECT distinct a.SERIAL,a.REF_NUM ,a.REF_DAT,a.EMP_NAM,a.ACT_TOT_AMT,a.ACT_MAIN_PUR,b.USR_ACTION,ACTIONED_BY_NAM,ACTIONED_ON_DATE,DATEDIFF(day, a.REF_DAT, getDate())  ,b.ACTIONED_ON_DATE,DATEDIFF(day, b.ACTIONED_ON_DATE, getDate()) "
                        + " FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] b"
                        + " on a.serial = b.serial and a.ref_num = b.ref_num and a.ACT_REC_STA = b.ACT_REC_STA and a.doc_ver = b.doc_ver  "
                        + "where (a.ACT_REC_STA = '" + actStatus + "' and a.SERIAL = '" + reqSerial + "')   "
                        + "and a.EMP_NAM = '" + jTextSearchEmpNam.getText() + "'"
                        + "and a.REF_DAT between '" + s.format(jDateFrom.getDate()) + "' and '" + s.format(jDateTo.getDate()) + "'"
                        + "order by 2");

                ResultSet r = st.getResultSet();

                while (r.next()) {
                    model.insertRow(model.getRowCount(), new Object[]{r.getString(1), r.getString(2), r.getString(3),
                        r.getString(4), r.getString(5), r.getString(6), r.getString(7), r.getString(8), r.getString(9)});

                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    void fetchdataMgtOutstandingbyUser() {
        clearTable();
        if ((jDateFrom.getDate() == null) || (jDateTo.getDate() == null)) {
            JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
            jDateFrom.requestFocusInWindow();
            jDateFrom.setFocusable(true);
        } else if ((jDateFrom.getDate().after(jDateTo.getDate())) && (!(jDateFrom.getDate().equals(jDateTo.getDate())))) {
            JOptionPane.showMessageDialog(this, "End date cannot be lower",
                    "Error", JOptionPane.ERROR_MESSAGE);
            jDateTo.requestFocusInWindow();
            jDateTo.setFocusable(true);

        } else if ("".equals(jTextSearchEmpNam.getText())) {
            JOptionPane.showMessageDialog(this, "User name cannot be blank",
                    "Error", JOptionPane.ERROR_MESSAGE);
            jDateTo.requestFocusInWindow();
            jDateTo.setFocusable(true);

        } else {
            try {

                jTableActivityAllUser.getColumnModel().getColumn(6).setHeaderValue("Activity Week");
                jTableActivityAllUser.getColumnModel().getColumn(7).setHeaderValue("");
                jTableActivityAllUser.getColumnModel().getColumn(8).setHeaderValue("");

                Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                        + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

                Statement st = conn.createStatement();

                st.executeQuery("select * from (SELECT a.REF_YEAR ,a.SERIAL ,a.REF_NUM,b.REF_DAT,b.EMP_NAM,a.PLAN_WK \"Week\" ,"
                        + "sum(a.ACT_ITM_TOT) \"Total\",b.ACT_MAIN_PUR,a.PLAN_WK FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] a "
                        + "join [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] b on a.serial = b.serial and a.DOC_VER = b.DOC_VER and "
                        + " a.ACT_REC_STA = b.ACT_REC_STA and a.REF_NUM = b.REF_NUM  where a.serial = 'R' and a.DOC_VER = 4 "
                        + "  and a.ACT_REC_STA = 'A' and a.ACT_DATE < GETDATE() and b.EMP_NAM = '" + jTextSearchEmpNam.getText() + "' "
                        + "and b.REF_DAT between '" + s.format(jDateFrom.getDate()) + "' and '" + s.format(jDateTo.getDate()) + "'  "
                        + "and CONCAT(a.Serial,a.ref_num)  not in (SELECT concat(SERIAL,REF_NUM) FROM [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] "
                        + "where USR_ACTION like '%Reject%'  and SERIAL = 'R') and concat(a.SERIAL,a.REF_NUM) in (SELECT distinct concat(SERIAL,REF_NUM) "
                        + "  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] where  SERIAL = 'R')  group by b.REF_DAT,a.REF_YEAR,a.SERIAL,"
                        + "a.REF_NUM,a.PLAN_WK,b.EMP_NAM,b.ACT_MAIN_PUR,a.PLAN_WK) y left join (SELECT a.REF_YEAR,a.SERIAL ,a.REF_NUM,b.EMP_NAM,"
                        + "b.PREV_REF_NUM,a.PLAN_WK \"Week\",sum(a.ACT_ITM_TOT) \"Total\" FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] a "
                        + "join [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] b on a.serial = b.serial and a.DOC_VER = b.DOC_VER "
                        + "and  a.ACT_REC_STA = b.ACT_REC_STA and a.REF_NUM = b.REF_NUM  where a.serial = 'A'  and a.ACT_REC_STA = 'Q' "
                        + "and CONCAT(a.Serial,a.ref_num)  not in (SELECT concat(SERIAL,REF_NUM) FROM [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] "
                        + " where USR_ACTION like '%Reject%'  and SERIAL = 'A') and concat(a.SERIAL,a.REF_NUM) in (SELECT distinct concat(SERIAL,REF_NUM) \n"
                        + "  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] where SERIAL = 'A' ) group by a.REF_YEAR,a.SERIAL,a.REF_NUM,"
                        + "a.PLAN_WK,b.EMP_NAM,b.PREV_REF_NUM) z on (y.REF_NUM = z.PREV_REF_NUM and y.Week = z.Week) "
                        + "where z.PREV_REF_NUM is null  and y.REF_NUM not in (SELECT PREV_REF_NUM FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppManualGenTab])");

                ResultSet r = st.getResultSet();

                while (r.next()) {
                    model.insertRow(model.getRowCount(), new Object[]{r.getString(2), r.getString(3), r.getString(4),
                        r.getString(5), r.getString(7), r.getString(8), "Week " + r.getString(6)});

                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    void fetchdataMgtAcquittedbyUser() {
        clearTable();
        if ((jDateFrom.getDate() == null) || (jDateTo.getDate() == null)) {
            JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
            jDateFrom.requestFocusInWindow();
            jDateFrom.setFocusable(true);
        } else if ((jDateFrom.getDate().after(jDateTo.getDate())) && (!(jDateFrom.getDate().equals(jDateTo.getDate())))) {
            JOptionPane.showMessageDialog(this, "End date cannot be lower",
                    "Error", JOptionPane.ERROR_MESSAGE);
            jDateTo.requestFocusInWindow();
            jDateTo.setFocusable(true);

        } else if ("".equals(jTextSearchEmpNam.getText())) {
            JOptionPane.showMessageDialog(this, "User name cannot be blankeee",
                    "Error", JOptionPane.ERROR_MESSAGE);
            jDateTo.requestFocusInWindow();
            jDateTo.setFocusable(true);

        } else {
            try {
                clearTable();
                Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                        + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

                Statement st = conn.createStatement();

                st.executeQuery("SELECT distinct a.SERIAL,a.REF_NUM ,a.REF_DAT,a.EMP_NAM,a.ACT_TOT_AMT,a.ACT_MAIN_PUR,b.USR_ACTION,"
                        + "ACTIONED_BY_NAM,ACTIONED_ON_DATE,DATEDIFF"
                        + "(day, a.REF_DAT, getDate())  ,b.ACTIONED_ON_DATE,DATEDIFF(day, b.ACTIONED_ON_DATE, getDate()),a.PREV_SERIAL,a.PREV_REF_NUM ,a.PREV_REF_DAT "
                        + " FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] b"
                        + " on a.serial = b.serial and a.ref_num = b.ref_num and a.ACT_REC_STA = b.ACT_REC_STA and a.doc_ver = b.doc_ver  "
                        + "where (a.ACT_REC_STA = 'Q' and a.SERIAL = 'A')"
                        + "and a.EMP_NAM = '" + jTextSearchEmpNam.getText() + "'"
                        + "and a.REF_DAT between '" + s.format(jDateFrom.getDate()) + "' and '" + s.format(jDateTo.getDate()) + "'"
                        + " and a.PREV_REF_DAT != '1900-01-01' and a.PREV_REF_NUM != 0"
                        + "order by 2");

                ResultSet r = st.getResultSet();

                while (r.next()) {
                    model.insertRow(model.getRowCount(), new Object[]{r.getString(1), r.getString(2), r.getString(3),
                        r.getString(4), r.getString(5), r.getString(6), r.getString(7), r.getString(8), r.getString(9),
                        r.getString(13), r.getString(14), r.getString(15)});

                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    void fetchdataAllUsers() {
        radioNormSpec();
        clearTable();
        if ((jDateFrom.getDate() == null) || (jDateTo.getDate() == null)) {
            JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
            jDateFrom.requestFocusInWindow();
            jDateFrom.setFocusable(true);
        } else if ((jDateFrom.getDate().after(jDateTo.getDate())) && (!(jDateFrom.getDate().equals(jDateTo.getDate())))) {
            JOptionPane.showMessageDialog(this, "End date cannot be lower",
                    "Error", JOptionPane.ERROR_MESSAGE);
            jDateTo.requestFocusInWindow();
            jDateTo.setFocusable(true);

        } else {
            try {
                clearTable();
                radioNormSpec();
                System.out.println("statu " + actStatus + " kk " + reqSerial + "");
                Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                        + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

                Statement st = conn.createStatement();

                st.executeQuery("SELECT distinct a.SERIAL,a.REF_NUM ,a.REF_DAT,a.EMP_NAM,a.ACT_TOT_AMT,a.ACT_MAIN_PUR,b.USR_ACTION,ACTIONED_BY_NAM,ACTIONED_ON_DATE,DATEDIFF(day, a.REF_DAT, getDate())  ,b.ACTIONED_ON_DATE,DATEDIFF(day, b.ACTIONED_ON_DATE, getDate()) "
                        + " FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] b"
                        + " on a.serial = b.serial and a.ref_num = b.ref_num and a.ACT_REC_STA = b.ACT_REC_STA and a.doc_ver = b.doc_ver  "
                        + "where (a.ACT_REC_STA = '" + actStatus + "' and a.SERIAL = '" + reqSerial + "')  "
                        + "and a.REF_DAT between '" + s.format(jDateFrom.getDate()) + "' and '" + s.format(jDateTo.getDate()) + "'"
                        + "order by 2");

                ResultSet r = st.getResultSet();

                while (r.next()) {
                    model.insertRow(model.getRowCount(), new Object[]{r.getString(1), r.getString(2), r.getString(3),
                        r.getString(4), r.getString(5), r.getString(6), r.getString(7), r.getString(8), r.getString(9)});

                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    void fetchdataMgtPaySch() {
        radioNormSpec();
        clearTable();
        if ((jDateFrom.getDate() == null) || (jDateTo.getDate() == null)) {
            JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
            jDateFrom.requestFocusInWindow();
            jDateFrom.setFocusable(true);
        } else if ((jDateFrom.getDate().after(jDateTo.getDate())) && (!(jDateFrom.getDate().equals(jDateTo.getDate())))) {
            JOptionPane.showMessageDialog(this, "End date cannot be lower",
                    "Error", JOptionPane.ERROR_MESSAGE);
            jDateTo.requestFocusInWindow();
            jDateTo.setFocusable(true);

        } else {
            try {
                clearTable();
                radioNormSpec();
                Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                        + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

                Statement st = conn.createStatement();

                st.executeQuery("SELECT distinct a.SERIAL,a.REF_NUM ,a.REF_DAT,a.EMP_NAM,a.ACT_TOT_AMT,a.ACT_MAIN_PUR,'Pay Schedule ',c.RUN_BY,c.RUN_DAT\n"
                        + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] b\n"
                        + "on a.serial = b.serial and a.ref_num = b.ref_num and a.ACT_REC_STA = b.ACT_REC_STA and a.doc_ver = b.doc_ver  \n"
                        + "join [ClaimsAppSysZvandiri].[dbo].[BatRunTab] c\n"
                        + "on b.serial = c.serial and b.ref_num = c.ref_num\n"
                        + "where (a.ACT_REC_STA = 'A' and a.SERIAL = 'R')\n"
                        + "and CONCAT(a.SERIAL,a.REF_NUM) in (SELECT distinct concat(SERIAL,REF_NUM)\n"
                        + "FROM [ClaimsAppSysZvandiri].[dbo].[BatRunTab]\n"
                        + "where SERIAL = 'R' and STATUS = 'Paid'\n"
                        + "and a.EMP_NAM = '" + jTextSearchEmpNam.getText() + "' and RUN_DAT between '" + s.format(jDateFrom.getDate()) + "' and '" + s.format(jDateTo.getDate()) + "') order by 2	");

                ResultSet r = st.getResultSet();

                while (r.next()) {
                    model.insertRow(model.getRowCount(), new Object[]{r.getString(1), r.getString(2), r.getString(3),
                        r.getString(4), r.getString(5), r.getString(6), r.getString(7), r.getString(8), r.getString(9)});

                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    void fetchdataAllPaySch() {
        radioNormSpec();
        clearTable();
        if ((jDateFrom.getDate() == null) || (jDateTo.getDate() == null)) {
            JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
            jDateFrom.requestFocusInWindow();
            jDateFrom.setFocusable(true);
        } else if ((jDateFrom.getDate().after(jDateTo.getDate())) && (!(jDateFrom.getDate().equals(jDateTo.getDate())))) {
            JOptionPane.showMessageDialog(this, "End date cannot be lower",
                    "Error", JOptionPane.ERROR_MESSAGE);
            jDateTo.requestFocusInWindow();
            jDateTo.setFocusable(true);

        } else {
            try {
                clearTable();
                radioNormSpec();
                Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                        + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

                Statement st = conn.createStatement();

                st.executeQuery("SELECT distinct a.SERIAL,a.REF_NUM ,a.REF_DAT,a.EMP_NAM,a.ACT_TOT_AMT,a.ACT_MAIN_PUR,'Pay Schedule ',c.RUN_BY,c.RUN_DAT\n"
                        + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] b\n"
                        + "on a.serial = b.serial and a.ref_num = b.ref_num and a.ACT_REC_STA = b.ACT_REC_STA and a.doc_ver = b.doc_ver  \n"
                        + "join [ClaimsAppSysZvandiri].[dbo].[BatRunTab] c\n"
                        + "on b.serial = c.serial and b.ref_num = c.ref_num\n"
                        + "where (a.ACT_REC_STA = 'A' and a.SERIAL = 'R')\n"
                        + "and CONCAT(a.SERIAL,a.REF_NUM) in (SELECT distinct concat(SERIAL,REF_NUM)\n"
                        + "FROM [ClaimsAppSysZvandiri].[dbo].[BatRunTab]\n"
                        + "where SERIAL = 'R' and STATUS = 'Paid'\n"
                        + "and RUN_DAT between '" + s.format(jDateFrom.getDate()) + "' and '" + s.format(jDateTo.getDate()) + "') order by 2	");

                ResultSet r = st.getResultSet();

                while (r.next()) {
                    model.insertRow(model.getRowCount(), new Object[]{r.getString(1), r.getString(2), r.getString(3),
                        r.getString(4), r.getString(5), r.getString(6), r.getString(7), r.getString(8), r.getString(9)});

                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    void fetchdataAllOutstandingbyUser() {
        clearTable();
        if ((jDateFrom.getDate() == null) || (jDateTo.getDate() == null)) {
            JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
            jDateFrom.requestFocusInWindow();
            jDateFrom.setFocusable(true);
        } else if ((jDateFrom.getDate().after(jDateTo.getDate())) && (!(jDateFrom.getDate().equals(jDateTo.getDate())))) {
            JOptionPane.showMessageDialog(this, "End date cannot be lower",
                    "Error", JOptionPane.ERROR_MESSAGE);
            jDateTo.requestFocusInWindow();
            jDateTo.setFocusable(true);

        } else {
            try {

                jTableActivityAllUser.getColumnModel().getColumn(6).setHeaderValue("Activity Week");
                jTableActivityAllUser.getColumnModel().getColumn(7).setHeaderValue("");
//                jTableActivityAllUser.getColumnModel().getColumn(7).setMaxWidth(0);
//                jTableActivityAllUser.getColumnModel().getColumn(7).setMinWidth(0);
                jTableActivityAllUser.getColumnModel().getColumn(8).setHeaderValue("");
//                jTableActivityAllUser.getColumnModel().getColumn(8).setMaxWidth(0);
//                jTableActivityAllUser.getColumnModel().getColumn(8).setMinWidth(0);

                Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                        + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

                Statement st = conn.createStatement();

                st.executeQuery("select * from (SELECT a.REF_YEAR ,a.SERIAL ,a.REF_NUM,b.REF_DAT,b.EMP_NAM,a.PLAN_WK \"Week\" ,"
                        + "sum(a.ACT_ITM_TOT) \"Total\",b.ACT_MAIN_PUR,a.PLAN_WK FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] a "
                        + "join [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] b on a.serial = b.serial and a.DOC_VER = b.DOC_VER and "
                        + " a.ACT_REC_STA = b.ACT_REC_STA and a.REF_NUM = b.REF_NUM  where a.serial = 'R' and a.DOC_VER = 4 "
                        + "  and a.ACT_REC_STA = 'A' and a.ACT_DATE < GETDATE() "
                        + "and b.REF_DAT between '" + s.format(jDateFrom.getDate()) + "' and '" + s.format(jDateTo.getDate()) + "'  "
                        + "and CONCAT(a.Serial,a.ref_num)  not in (SELECT concat(SERIAL,REF_NUM) FROM [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] "
                        + "where USR_ACTION like '%Reject%'  and SERIAL = 'R') and concat(a.SERIAL,a.REF_NUM) in (SELECT distinct concat(SERIAL,REF_NUM) "
                        + "  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] where  SERIAL = 'R')  group by b.REF_DAT,a.REF_YEAR,a.SERIAL,"
                        + "a.REF_NUM,a.PLAN_WK,b.EMP_NAM,b.ACT_MAIN_PUR,a.PLAN_WK) y left join (SELECT a.REF_YEAR,a.SERIAL ,a.REF_NUM,b.EMP_NAM,"
                        + "b.PREV_REF_NUM,a.PLAN_WK \"Week\",sum(a.ACT_ITM_TOT) \"Total\" FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] a "
                        + "join [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] b on a.serial = b.serial and a.DOC_VER = b.DOC_VER "
                        + "and  a.ACT_REC_STA = b.ACT_REC_STA and a.REF_NUM = b.REF_NUM  where a.serial = 'A'  and a.ACT_REC_STA = 'Q' "
                        + "and CONCAT(a.Serial,a.ref_num)  not in (SELECT concat(SERIAL,REF_NUM) FROM [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] "
                        + " where USR_ACTION like '%Reject%'  and SERIAL = 'A') and concat(a.SERIAL,a.REF_NUM) in (SELECT distinct concat(SERIAL,REF_NUM) \n"
                        + "  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] where SERIAL = 'A' ) group by a.REF_YEAR,a.SERIAL,a.REF_NUM,"
                        + "a.PLAN_WK,b.EMP_NAM,b.PREV_REF_NUM) z on (y.REF_NUM = z.PREV_REF_NUM and y.Week = z.Week) "
                        + "where z.PREV_REF_NUM is null  and y.REF_NUM not in (SELECT PREV_REF_NUM FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppManualGenTab])");
                ResultSet r = st.getResultSet();

                while (r.next()) {
                    model.insertRow(model.getRowCount(), new Object[]{r.getString(2), r.getString(3), r.getString(4),
                        r.getString(5), r.getString(7), r.getString(8), "Week " + r.getString(6)});

                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    void fetchdataAllAcquittedbyUser() {
        clearTable();
        if ((jDateFrom.getDate() == null) || (jDateTo.getDate() == null)) {
            JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
            jDateFrom.requestFocusInWindow();
            jDateFrom.setFocusable(true);
        } else if ((jDateFrom.getDate().after(jDateTo.getDate())) && (!(jDateFrom.getDate().equals(jDateTo.getDate())))) {
            JOptionPane.showMessageDialog(this, "End date cannot be lower",
                    "Error", JOptionPane.ERROR_MESSAGE);
            jDateTo.requestFocusInWindow();
            jDateTo.setFocusable(true);

        } else {
            try {
                clearTable();
                Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                        + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

                Statement st = conn.createStatement();
                st.executeQuery("SELECT distinct a.SERIAL,a.REF_NUM ,a.REF_DAT,a.EMP_NAM,a.ACT_TOT_AMT,a.ACT_MAIN_PUR,b.USR_ACTION,"
                        + "ACTIONED_BY_NAM,ACTIONED_ON_DATE,DATEDIFF"
                        + "(day, a.REF_DAT, getDate())  ,b.ACTIONED_ON_DATE,DATEDIFF(day, b.ACTIONED_ON_DATE, getDate()),a.PREV_SERIAL,a.PREV_REF_NUM ,a.PREV_REF_DAT "
                        + " FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] b"
                        + " on a.serial = b.serial and a.ref_num = b.ref_num and a.ACT_REC_STA = b.ACT_REC_STA and a.doc_ver = b.doc_ver  "
                        + "where (a.ACT_REC_STA = 'Q' and a.SERIAL = 'A')"
                        + "and a.REF_DAT between '" + s.format(jDateFrom.getDate()) + "' and '" + s.format(jDateTo.getDate()) + "'"
                        + " and a.PREV_REF_DAT != '1900-01-01' and a.PREV_REF_NUM != 0"
                        + "order by 2");

                ResultSet r = st.getResultSet();

                while (r.next()) {
                    model.insertRow(model.getRowCount(), new Object[]{r.getString(1), r.getString(2), r.getString(3),
                        r.getString(4), r.getString(5), r.getString(6), r.getString(7), r.getString(8), r.getString(9),
                        r.getString(13), r.getString(14), r.getString(15)});

                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    void fetchdataUser() {

        if ((jDateFrom.getDate() == null) || (jDateTo.getDate() == null)) {
            JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
            jDateFrom.requestFocusInWindow();
            jDateFrom.setFocusable(true);
        } else if ((jDateFrom.getDate().after(jDateTo.getDate())) && (!(jDateFrom.getDate().equals(jDateTo.getDate())))) {
            JOptionPane.showMessageDialog(this, "End date cannot be lower",
                    "Error", JOptionPane.ERROR_MESSAGE);
            jDateTo.requestFocusInWindow();
            jDateTo.setFocusable(true);
        } else if ("".equals(jTextSearchEmpNam.getText())) {
            JOptionPane.showMessageDialog(this, "User name cannot be blank",
                    "Error", JOptionPane.ERROR_MESSAGE);
            jDateTo.requestFocusInWindow();
            jDateTo.setFocusable(true);

        } else {
            radioNormSpec();
            try {
                clearTable();
                Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                        + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

                Statement st = conn.createStatement();

                st.executeQuery("SELECT distinct a.SERIAL,a.REF_NUM ,a.REF_DAT,a.EMP_NAM,a.ACT_TOT_AMT,a.ACT_MAIN_PUR,"
                        + "b.USR_ACTION,ACTIONED_BY_NAM,ACTIONED_ON_DATE,DATEDIFF(day, a.REF_DAT, getDate())  ,b.ACTIONED_ON_DATE,DATEDIFF(day, b.ACTIONED_ON_DATE, getDate()) "
                        + " FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a join [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] b"
                        + " on a.serial = b.serial and a.ref_num = b.ref_num and a.ACT_REC_STA = b.ACT_REC_STA and a.doc_ver = b.doc_ver  "
                        + "where (a.ACT_REC_STA = '" + actStatus + "' and a.SERIAL = '" + reqSerial + "')   "
                        + "and a.EMP_NAM = '" + jTextSearchEmpNam.getText() + "'"
                        + "and a.REF_DAT between '" + s.format(jDateFrom.getDate()) + "' and '" + s.format(jDateTo.getDate()) + "'"
                        + "order by 2");

                ResultSet r = st.getResultSet();

                while (r.next()) {
                    model.insertRow(model.getRowCount(), new Object[]{r.getString(1), r.getString(2), r.getString(3),
                        r.getString(4), r.getString(5), r.getString(6), r.getString(7), r.getString(8), r.getString(9)});

                }

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

        jDialogSearchID = new javax.swing.JDialog();
        jPanelSearchID = new javax.swing.JPanel();
        jTextFieldSearchNam = new javax.swing.JTextField();
        jButtonSearch = new javax.swing.JButton();
        jComboBoxSearchResult = new javax.swing.JComboBox<>();
        jButtonOk = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        buttonGroupReqType = new javax.swing.ButtonGroup();
        jPanelItem1 = new javax.swing.JPanel();
        jLabelLogo = new javax.swing.JLabel();
        jLabelHeaderLine = new javax.swing.JLabel();
        jLabelDate = new javax.swing.JLabel();
        jLabelTime = new javax.swing.JLabel();
        jLabellogged1 = new javax.swing.JLabel();
        jLabelGenLogNam = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableActivityAllUser = new javax.swing.JTable();
        jLabelRangeSelect = new javax.swing.JLabel();
        jLabelEmp = new javax.swing.JLabel();
        jComboStatus = new javax.swing.JComboBox<>();
        jLabelStatus = new javax.swing.JLabel();
        jLabelXtrsRef = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabelDateFrom = new javax.swing.JLabel();
        jDateFrom = new com.toedter.calendar.JDateChooser();
        jLabelDateTo = new javax.swing.JLabel();
        jDateTo = new com.toedter.calendar.JDateChooser();
        jComboBoxStatus = new javax.swing.JComboBox<>();
        jLabelSearchEmpName = new javax.swing.JLabel();
        jButtonSearchEmp = new javax.swing.JButton();
        jTextSearchEmpNam = new javax.swing.JTextField();
        jButtonDisplayResults = new javax.swing.JButton();
        jRadioButtonNormal = new javax.swing.JRadioButton();
        jRadioButtonSpecial = new javax.swing.JRadioButton();
        jCheckBoxAllEmp = new javax.swing.JCheckBox();
        jLabelAllEmployee = new javax.swing.JLabel();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuNew = new javax.swing.JMenu();
        jMenuItemPlanPerDiem = new javax.swing.JMenuItem();
        jSeparator13 = new javax.swing.JPopupMenu.Separator();
        jMenuItemPerDiemAcquittal = new javax.swing.JMenuItem();
        jSeparator16 = new javax.swing.JPopupMenu.Separator();
        jMenuEdit = new javax.swing.JMenu();
        jMenuAcqEdit = new javax.swing.JMenuItem();
        jSeparator30 = new javax.swing.JPopupMenu.Separator();
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

        jDialogSearchID.setTitle("Search Employee ID");
        jDialogSearchID.setAlwaysOnTop(true);
        jDialogSearchID.setBackground(new java.awt.Color(153, 204, 0));
        jDialogSearchID.setLocation(new java.awt.Point(450, 150));
        jDialogSearchID.setMinimumSize(new java.awt.Dimension(400, 150));

        jPanelSearchID.setBackground(new java.awt.Color(7, 163, 163));
        jPanelSearchID.setMinimumSize(new java.awt.Dimension(400, 200));
        jPanelSearchID.setLayout(null);

        jTextFieldSearchNam.setPreferredSize(new java.awt.Dimension(50, 30));
        jTextFieldSearchNam.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldSearchNamFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldSearchNamFocusLost(evt);
            }
        });
        jTextFieldSearchNam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldSearchNamActionPerformed(evt);
            }
        });
        jPanelSearchID.add(jTextFieldSearchNam);
        jTextFieldSearchNam.setBounds(10, 22, 204, 30);

        jButtonSearch.setText("Search");
        jButtonSearch.setPreferredSize(new java.awt.Dimension(65, 30));
        jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchActionPerformed(evt);
            }
        });
        jPanelSearchID.add(jButtonSearch);
        jButtonSearch.setBounds(256, 22, 90, 30);

        jComboBoxSearchResult.setPreferredSize(new java.awt.Dimension(70, 30));
        jPanelSearchID.add(jComboBoxSearchResult);
        jComboBoxSearchResult.setBounds(10, 70, 204, 30);

        jButtonOk.setText("OK");
        jButtonOk.setPreferredSize(new java.awt.Dimension(73, 30));
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });
        jPanelSearchID.add(jButtonOk);
        jButtonOk.setBounds(256, 70, 90, 30);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Enter search term e.g. Joe");
        jPanelSearchID.add(jLabel2);
        jLabel2.setBounds(12, 3, 204, 13);

        javax.swing.GroupLayout jDialogSearchIDLayout = new javax.swing.GroupLayout(jDialogSearchID.getContentPane());
        jDialogSearchID.getContentPane().setLayout(jDialogSearchIDLayout);
        jDialogSearchIDLayout.setHorizontalGroup(
            jDialogSearchIDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelSearchID, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogSearchIDLayout.setVerticalGroup(
            jDialogSearchIDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelSearchID, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PERDIEM LIST");
        setResizable(false);
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        jPanelItem1.setBackground(new java.awt.Color(25, 75, 224));
        jPanelItem1.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jPanelItem1.setPreferredSize(new java.awt.Dimension(1280, 677));
        jPanelItem1.setLayout(null);

        jLabelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelItem1.add(jLabelLogo);
        jLabelLogo.setBounds(10, 10, 220, 100);

        jLabelHeaderLine.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeaderLine.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelItem1.add(jLabelHeaderLine);
        jLabelHeaderLine.setBounds(320, 40, 610, 40);

        jLabelDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelDate.setText("29 November 2018");
        jPanelItem1.add(jLabelDate);
        jLabelDate.setBounds(1120, 0, 110, 30);

        jLabelTime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelTime.setText("15:20:30");
        jPanelItem1.add(jLabelTime);
        jLabelTime.setBounds(1260, 0, 80, 30);

        jLabellogged1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged1.setText("Logged In");
        jPanelItem1.add(jLabellogged1);
        jLabellogged1.setBounds(1120, 40, 80, 30);

        jLabelGenLogNam.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam.setForeground(new java.awt.Color(255, 255, 255));
        jPanelItem1.add(jLabelGenLogNam);
        jLabelGenLogNam.setBounds(1220, 40, 140, 30);

        jTableActivityAllUser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jTableActivityAllUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Serial", "Reference No.", "Registration Date", "Employee Name", "Requested Amount", "Activity Main Description", "Current Status", "Last Actioned  By", "Last Action Date", "Request Serial", "Request Ref. No.", "Request Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableActivityAllUser.setDragEnabled(true);
        jTableActivityAllUser.setGridColor(new java.awt.Color(255, 255, 255));
        jTableActivityAllUser.setMinimumSize(new java.awt.Dimension(180, 64));
        jTableActivityAllUser.setRowHeight(25);
        jTableActivityAllUser.setSelectionBackground(new java.awt.Color(153, 153, 0));
        jTableActivityAllUser.setSelectionForeground(new java.awt.Color(0, 0, 0));
        jTableActivityAllUser.getTableHeader().setReorderingAllowed(false);
        jTableActivityAllUser.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTableActivityAllUserFocusLost(evt);
            }
        });
        jTableActivityAllUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableActivityAllUserMouseClicked(evt);
            }
        });
        jTableActivityAllUser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTableActivityAllUserKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTableActivityAllUserKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTableActivityAllUserKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(jTableActivityAllUser);

        jPanelItem1.add(jScrollPane2);
        jScrollPane2.setBounds(230, 115, 1130, 560);

        jLabelRangeSelect.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelRangeSelect.setForeground(new java.awt.Color(255, 255, 255));
        jLabelRangeSelect.setText("Please Select Option");
        jPanelItem1.add(jLabelRangeSelect);
        jLabelRangeSelect.setBounds(10, 680, 140, 25);

        jLabelEmp.setForeground(new java.awt.Color(255, 255, 255));
        jPanelItem1.add(jLabelEmp);
        jLabelEmp.setBounds(1110, 70, 50, 20);

        jComboStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All" }));
        jPanelItem1.add(jComboStatus);
        jComboStatus.setBounds(610, 680, 280, 30);

        jLabelStatus.setText("Status");
        jPanelItem1.add(jLabelStatus);
        jLabelStatus.setBounds(540, 680, 60, 25);
        jPanelItem1.add(jLabelXtrsRef);
        jLabelXtrsRef.setBounds(860, 90, 90, 30);

        jPanel1.setLayout(null);

        jLabelDateFrom.setText("Date From");
        jPanel1.add(jLabelDateFrom);
        jLabelDateFrom.setBounds(5, 10, 60, 25);

        jDateFrom.setDateFormatString("dd-MM-yyyy");
        jPanel1.add(jDateFrom);
        jDateFrom.setBounds(70, 10, 140, 25);

        jLabelDateTo.setText("Date To");
        jPanel1.add(jLabelDateTo);
        jLabelDateTo.setBounds(5, 50, 60, 25);

        jDateTo.setDateFormatString("dd-MM-yyyy");
        jPanel1.add(jDateTo);
        jDateTo.setBounds(70, 50, 140, 25);

        jComboBoxStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Created Requests", "Acquitted Requests", "Outstanding Requests" }));
        jComboBoxStatus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBoxStatusMouseClicked(evt);
            }
        });
        jComboBoxStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxStatusActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBoxStatus);
        jComboBoxStatus.setBounds(5, 100, 200, 25);

        jLabelSearchEmpName.setText("Select Employee");
        jPanel1.add(jLabelSearchEmpName);
        jLabelSearchEmpName.setBounds(5, 150, 90, 25);

        jButtonSearchEmp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.jpg"))); // NOI18N
        jButtonSearchEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchEmpActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonSearchEmp);
        jButtonSearchEmp.setBounds(110, 150, 30, 25);
        jPanel1.add(jTextSearchEmpNam);
        jTextSearchEmpNam.setBounds(5, 180, 215, 25);

        jButtonDisplayResults.setBackground(new java.awt.Color(41, 158, 30));
        jButtonDisplayResults.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonDisplayResults.setForeground(new java.awt.Color(255, 255, 255));
        jButtonDisplayResults.setText("Display Results ");
        jButtonDisplayResults.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDisplayResultsActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonDisplayResults);
        jButtonDisplayResults.setBounds(40, 280, 120, 25);

        buttonGroupReqType.add(jRadioButtonNormal);
        jRadioButtonNormal.setSelected(true);
        jRadioButtonNormal.setText(" Normal");
        jPanel1.add(jRadioButtonNormal);
        jRadioButtonNormal.setBounds(10, 400, 80, 25);

        buttonGroupReqType.add(jRadioButtonSpecial);
        jRadioButtonSpecial.setText(" Special");
        jPanel1.add(jRadioButtonSpecial);
        jRadioButtonSpecial.setBounds(100, 400, 89, 25);

        jCheckBoxAllEmp.setFont(new java.awt.Font("Tahoma", 3, 10)); // NOI18N
        jCheckBoxAllEmp.setForeground(new java.awt.Color(204, 0, 0));
        jCheckBoxAllEmp.setText("   ALL Employees");
        jCheckBoxAllEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxAllEmpActionPerformed(evt);
            }
        });
        jPanel1.add(jCheckBoxAllEmp);
        jCheckBoxAllEmp.setBounds(10, 240, 130, 21);

        jLabelAllEmployee.setFont(new java.awt.Font("Tahoma", 3, 10)); // NOI18N
        jLabelAllEmployee.setText("Tick to Display ALL Employees");
        jPanel1.add(jLabelAllEmployee);
        jLabelAllEmployee.setBounds(10, 220, 170, 20);

        jPanelItem1.add(jPanel1);
        jPanel1.setBounds(3, 115, 220, 560);

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


    private void jMenuPlanApprovalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuPlanApprovalActionPerformed

    }//GEN-LAST:event_jMenuPlanApprovalActionPerformed

    private void jTextFieldSearchNamFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldSearchNamFocusGained

    }//GEN-LAST:event_jTextFieldSearchNamFocusGained

    private void jTextFieldSearchNamFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldSearchNamFocusLost

    }//GEN-LAST:event_jTextFieldSearchNamFocusLost

    private void jTextFieldSearchNamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldSearchNamActionPerformed

    }//GEN-LAST:event_jTextFieldSearchNamActionPerformed

    private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchActionPerformed

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();

            int itemCount = jComboBoxSearchResult.getItemCount();

            for (int i = 0; i < itemCount; i++) {
                jComboBoxSearchResult.removeItemAt(0);
            }
            jComboBoxSearchResult.setSelectedIndex(-1);
            String searchNam = jTextFieldSearchNam.getText();
            st.executeQuery("SELECT distinct [EMP_NAM]\n"
                    + "  FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] WHERE [EMP_NAM] like upper(\n" + "'%" + searchNam + "%')");
            ResultSet r = st.getResultSet();

            while (r.next()) {
                jComboBoxSearchResult.addItem(r.getString(1));
                jButtonOk.setVisible(true);
                //                jButtonSearch.setVisible(false);

            }

            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Connect to Database.Try again.", "Error Connection", JOptionPane.WARNING_MESSAGE);
            // System.exit(1);
        }
    }//GEN-LAST:event_jButtonSearchActionPerformed

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        String str = jComboBoxSearchResult.getSelectedItem().toString();
        jTextSearchEmpNam.setText(str);
        jDialogSearchID.dispose();
        jCheckBoxAllEmp.setSelected(false);

    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonDisplayResultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDisplayResultsActionPerformed

        /**
         * removed to be added Acquitted Requests Scheduled For Payment *
         */
        clearTable();

        if (("All Created Requests".equals(jComboBoxStatus.getSelectedItem().toString()))) {

            jTableActivityAllUser.getColumnModel().getColumn(0).setHeaderValue("Serial");
            jTableActivityAllUser.getColumnModel().getColumn(1).setHeaderValue("Reference No.");
            jTableActivityAllUser.getColumnModel().getColumn(2).setHeaderValue("Registration Date");
            jTableActivityAllUser.getColumnModel().getColumn(3).setHeaderValue("Employee Name");
            jTableActivityAllUser.getColumnModel().getColumn(4).setHeaderValue("Requested Amount");
            jTableActivityAllUser.getColumnModel().getColumn(5).setHeaderValue("Activity Main Description");
            jTableActivityAllUser.getColumnModel().getColumn(6).setHeaderValue("Status");
            jTableActivityAllUser.getColumnModel().getColumn(7).setHeaderValue("Actioned By");
            jTableActivityAllUser.getColumnModel().getColumn(8).setHeaderValue("Action Date");

        } else {
            jTableActivityAllUser.getColumnModel().getColumn(0).setHeaderValue("Serial");
            jTableActivityAllUser.getColumnModel().getColumn(1).setHeaderValue("Reference No.");
            jTableActivityAllUser.getColumnModel().getColumn(2).setHeaderValue("Registration Date");
            jTableActivityAllUser.getColumnModel().getColumn(3).setHeaderValue("Employee Name");
            jTableActivityAllUser.getColumnModel().getColumn(4).setHeaderValue("Requested Amount");
            jTableActivityAllUser.getColumnModel().getColumn(5).setHeaderValue("Activity Main Description");
            jTableActivityAllUser.getColumnModel().getColumn(6).setHeaderValue("Activity Week");
            jTableActivityAllUser.getColumnModel().getColumn(7).setHeaderValue("");
            jTableActivityAllUser.getColumnModel().getColumn(8).setHeaderValue("");
        }

        if ((("usrGen").equals(usrGrp)) && ("Created Requests".equals(jComboBoxStatus.getSelectedItem().toString()))) {
            fetchdataAllbyUser();
        } else if ((("usrGen").equals(usrGrp)) && ("Outstanding Requests".equals(jComboBoxStatus.getSelectedItem().toString()))) {
            fetchdataOutstandingbyUser();
        } else if ((("usrGen").equals(usrGrp)) && ("Acquitted Requests".equals(jComboBoxStatus.getSelectedItem().toString()))) {
            fetchdataAcquittedbyUser();
        } else if ((!(("usrGen").equals(usrGrp))) && ("Created Requests".equals(jComboBoxStatus.getSelectedItem().toString()))
                && (!(jCheckBoxAllEmp.isSelected()))) {
            fetchdataMgtAllbyUser();
        } else if ((!(("usrGen").equals(usrGrp))) && ("Outstanding Requests".equals(jComboBoxStatus.getSelectedItem().toString()))
                && (!(jCheckBoxAllEmp.isSelected()))) {
            fetchdataMgtOutstandingbyUser();
        } else if ((!(("usrGen").equals(usrGrp))) && ("Acquitted Requests".equals(jComboBoxStatus.getSelectedItem().toString()))
                && (!(jCheckBoxAllEmp.isSelected()))) {
            fetchdataMgtAcquittedbyUser();
        } else if ((!(("usrGen").equals(usrGrp))) && ("Created Requests".equals(jComboBoxStatus.getSelectedItem().toString()))
                && (jCheckBoxAllEmp.isSelected()) && ("".equals(jTextSearchEmpNam.getText()))) {
            fetchdataAllUsers();
        } else if ((!(("usrGen").equals(usrGrp))) && ("Outstanding Requests".equals(jComboBoxStatus.getSelectedItem().toString()))
                && (jCheckBoxAllEmp.isSelected()) && ("".equals(jTextSearchEmpNam.getText()))) {
            fetchdataAllOutstandingbyUser();
        } else if ((!(("usrGen").equals(usrGrp))) && ("Acquitted Requests".equals(jComboBoxStatus.getSelectedItem().toString()))
                && (jCheckBoxAllEmp.isSelected()) && ("".equals(jTextSearchEmpNam.getText()))) {
            fetchdataAllAcquittedbyUser();
        } else if ((!(("usrGen").equals(usrGrp))) && ("Scheduled For Payment".equals(jComboBoxStatus.getSelectedItem().toString()))
                && (jCheckBoxAllEmp.isSelected()) && ("".equals(jTextSearchEmpNam.getText()))) {
            fetchdataAllPaySch();
        } else if ((!(("usrGen").equals(usrGrp))) && ("Scheduled For Payment".equals(jComboBoxStatus.getSelectedItem().toString()))
                && (!(jCheckBoxAllEmp.isSelected()))) {
            fetchdataMgtPaySch();
        }


    }//GEN-LAST:event_jButtonDisplayResultsActionPerformed

    private void jButtonSearchEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchEmpActionPerformed
        jDialogSearchID.setVisible(true);
    }//GEN-LAST:event_jButtonSearchEmpActionPerformed

    private void jComboBoxStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxStatusActionPerformed

    }//GEN-LAST:event_jComboBoxStatusActionPerformed

    private void jCheckBoxAllEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxAllEmpActionPerformed
        jTextSearchEmpNam.setText("");
    }//GEN-LAST:event_jCheckBoxAllEmpActionPerformed

    private void jTableActivityAllUserKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableActivityAllUserKeyTyped

    }//GEN-LAST:event_jTableActivityAllUserKeyTyped

    private void jTableActivityAllUserKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableActivityAllUserKeyReleased

    }//GEN-LAST:event_jTableActivityAllUserKeyReleased

    private void jTableActivityAllUserKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableActivityAllUserKeyPressed

    }//GEN-LAST:event_jTableActivityAllUserKeyPressed

    private void jTableActivityAllUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableActivityAllUserMouseClicked
        if (evt.getClickCount() == 2) {
            int row = jTableActivityAllUser.getSelectedRow();

            int col = 0;
            int col1 = 1;
            int col6 = 6;
            int col10 = 10;
            Object id = jTableActivityAllUser.getValueAt(row, col);
            Object id1 = jTableActivityAllUser.getValueAt(row, col1);
            Object id2 = jTableActivityAllUser.getValueAt(row, col6);
            Object id3 = jTableActivityAllUser.getValueAt(row, col10);

            String Serial = id.toString();
            String ref = id1.toString();
            String str = id2.toString().substring(0, 9);

//            System.out.println("ref "+ref +" serial "+ Serial+" "+refAcq);
//
//            // jLabeltest.setText(tt + " " + tt1);
//           
//            jLabelXtrsRef.setText(yearRef);
            if ("A".equals(Serial)) {
                String refAcq = id3.toString();
                new JFrameAppRepAcquittalView(jLabelEmp.getText(), refAcq).setVisible(true);
                setVisible(false);
            } else {
                String yearRef = (ref + Serial);
                new JFrameReqRepViewApp(jLabelEmp.getText(), ref).setVisible(true);
                setVisible(false);
            }
        }
    }//GEN-LAST:event_jTableActivityAllUserMouseClicked

    private void jTableActivityAllUserFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTableActivityAllUserFocusLost

    }//GEN-LAST:event_jTableActivityAllUserFocusLost

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

    private void jComboBoxStatusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBoxStatusMouseClicked

    }//GEN-LAST:event_jComboBoxStatusMouseClicked

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
            java.util.logging.Logger.getLogger(JFrameReqAllUserList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameReqAllUserList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameReqAllUserList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameReqAllUserList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new JFrameReqAllUserList().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupReqType;
    private javax.swing.JButton jButtonDisplayResults;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JButton jButtonSearchEmp;
    private javax.swing.JCheckBox jCheckBoxAllEmp;
    private javax.swing.JComboBox<String> jComboBoxSearchResult;
    private javax.swing.JComboBox<String> jComboBoxStatus;
    private javax.swing.JComboBox<String> jComboStatus;
    private com.toedter.calendar.JDateChooser jDateFrom;
    private com.toedter.calendar.JDateChooser jDateTo;
    private javax.swing.JDialog jDialogSearchID;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelAllEmployee;
    private javax.swing.JLabel jLabelDate;
    private javax.swing.JLabel jLabelDateFrom;
    private javax.swing.JLabel jLabelDateTo;
    private javax.swing.JLabel jLabelEmp;
    private javax.swing.JLabel jLabelGenLogNam;
    private javax.swing.JLabel jLabelHeaderLine;
    private javax.swing.JLabel jLabelLogo;
    private javax.swing.JLabel jLabelRangeSelect;
    private javax.swing.JLabel jLabelSearchEmpName;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JLabel jLabelTime;
    private javax.swing.JLabel jLabelXtrsRef;
    private javax.swing.JLabel jLabellogged1;
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
    private javax.swing.JPanel jPanelItem1;
    private javax.swing.JPanel jPanelSearchID;
    private javax.swing.JRadioButton jRadioButtonNormal;
    private javax.swing.JRadioButton jRadioButtonSpecial;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator11;
    private javax.swing.JPopupMenu.Separator jSeparator13;
    private javax.swing.JPopupMenu.Separator jSeparator16;
    private javax.swing.JPopupMenu.Separator jSeparator18;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator22;
    private javax.swing.JPopupMenu.Separator jSeparator23;
    private javax.swing.JPopupMenu.Separator jSeparator26;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator30;
    private javax.swing.JPopupMenu.Separator jSeparator34;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator62;
    private javax.swing.JPopupMenu.Separator jSeparator63;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JTable jTableActivityAllUser;
    private javax.swing.JTextField jTextFieldSearchNam;
    private javax.swing.JTextField jTextSearchEmpNam;
    // End of variables declaration//GEN-END:variables
}
