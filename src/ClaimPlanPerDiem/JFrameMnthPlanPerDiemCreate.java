/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClaimPlanPerDiem;

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
import java.net.InetAddress;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import ClaimAppendix1.*;
import ClaimAppendix2.*;
import ClaimReports.*;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.GregorianCalendar;
import utils.connCred;
import utils.timeHost;
import utils.emailSend;
import utils.StockVehicleMgt;
import ClaimPlan.JFrameMnthFinanceList;
import ClaimPlan.JFrameMnthHODList;
import ClaimPlan.JFrameMnthReqGenList;
import ClaimPlan.JFrameMnthSupList;
import ClaimPlan.JFrameMnthViewList;
import ClaimPlanPerDiem.JFrameMnthPlanPerDiemEdit;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import utils.connSaveFile;
import utils.savePDFToDB;

/**
 *
 * @author goredemac
 */
public class JFrameMnthPlanPerDiemCreate extends javax.swing.JFrame {

    connCred c = new connCred();
    connSaveFile attL = new connSaveFile();
    emailSend emSend = new emailSend();
    timeHost tH = new timeHost();
    DefaultTableModel modelWk1, modelWk2, modelWk3, modelWk4, modelWk5, modelAtt,
            modelAttWk1Main, modelAttWk2Main, modelAttWk3Main, modelAttWk4Main, modelAttWk5Main;
    boolean ignoreInput = false;
    String filename = null;
    int charMaxWk1 = 200;
    int charMaxWk2 = 200;
    int charMaxWk3 = 200;
    int charMaxWk4 = 200;
    int charMaxWk5 = 200;
    int countPlanAct = 0;
    int countPlanPeriod = 0;
    int countWk1 = 0;
    int countWk2 = 0;
    int countWk3 = 0;
    int countWk4 = 0;
    int countWk5 = 0;

    int month, finyear;
    Date curYear = new Date();
    double totReqAmt = 0;
    double totBudgetLnReqAmt = 0;
    long noOfFinDays1 = 0;
    long noOfFinDays2 = 0;
    long noOfFinDays3 = 0;
    long noOfFinDays4 = 0;
    long noOfFinDays5 = 0;
    LocalDate minDateWk1, maxDateWk1, minDateWk2, maxDateWk2, minDateWk3, maxDateWk3, minDateWk4, maxDateWk4, minDateWk5, maxDateWk5;
    Date minDateComWk1, maxDateComWk1, minDateComWk2, maxDateComWk2, minDateComWk3, maxDateComWk3, minDateComWk4, maxDateComWk4,
            minDateComWk5, maxDateComWk5;
    ZoneId zoneId = ZoneId.systemDefault();
    SimpleDateFormat df = new SimpleDateFormat("yyyy");
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String wk1Site = "N";
    String wk2Site = "N";
    String wk3Site = "N";
    String wk4Site = "N";
    String wk5Site = "N";
    String hostName = "";

    String breakfastAll, lunchAll, lunchNPAll, lunchPAll, dinnerAll,
            incidentalAll, unProvedAll, provedAll, date1, date2, usrnam, sendToProvMgr, supNam, supEmpNum,
            supUsrMail, provMgrMail, createUsrNam, usrRecNam, usrActType, UsrRecWk, actDate, duplicateUser1Count,
            duplicateUser2Count, duplicateUser3Count, duplicateUser4Count, duplicateUser5Count, usrGrp,
            planWk, empNamNum1, empNam1, empNamNum2, empNam2, empNamNum3, empNam3, empNamNum4,
            empNam4, planDate, SupNamSend, branchCode, prjCode, taskCode, donorCode, grantCode, accCode, prjProgCode, donor, budLine, subBudLine,
            donorName, accCodeName, prjCodeName, prjProgCodeName, budLineName, budcode, taskDonor,
            staffName1, staffName2, staffName3, staffName4, lastDateofMonth;
    /*To Change*/
    PreparedStatement pst1 = null;
    PreparedStatement pst = null;
    int itmNum = 1;
    int maxPlanRefNum, newPlanRefNum;

    /**
     * Creates new form JFrame3WkPlan
     */
    public JFrameMnthPlanPerDiemCreate() {
        initComponents();
        modelWk1 = (DefaultTableModel) jTableWk1Activities.getModel();
        modelWk2 = (DefaultTableModel) jTableWk2Activities.getModel();
        modelWk3 = (DefaultTableModel) jTableWk3Activities.getModel();
        modelWk4 = (DefaultTableModel) jTableWk4Activities.getModel();
        modelWk5 = (DefaultTableModel) jTableWk5Activities.getModel();
        //  makeFrameFullSize(this);
        findProvince();
        allowanceRate();
        //  showDate();
        showTime();
        computerName();
        jLabelDialogDepartActivityDate.setText("<html>Depature Date and <br> depature time</html>");
        jLabelDialogReturnActivityDate.setText("<html>Return Date and time<br>of arrival</html>");
    }

    public JFrameMnthPlanPerDiemCreate(String usrLogNam) {
        initComponents();
        findProvince();
        modelWk1 = (DefaultTableModel) jTableWk1Activities.getModel();
        modelWk2 = (DefaultTableModel) jTableWk2Activities.getModel();
        modelWk3 = (DefaultTableModel) jTableWk3Activities.getModel();
        modelWk4 = (DefaultTableModel) jTableWk4Activities.getModel();
        modelWk5 = (DefaultTableModel) jTableWk5Activities.getModel();
        modelAtt = (DefaultTableModel) jTableDocAtt.getModel();
        modelAttWk1Main = (DefaultTableModel) jTableDocAttWk1Main.getModel();
        modelAttWk2Main = (DefaultTableModel) jTableDocAttWk2Main.getModel();
        modelAttWk3Main = (DefaultTableModel) jTableDocAttWk3Main.getModel();
        modelAttWk4Main = (DefaultTableModel) jTableDocAttWk4Main.getModel();
        modelAttWk5Main = (DefaultTableModel) jTableDocAttWk5Main.getModel();
        jTableDocAtt.getColumnModel().getColumn(0).setMinWidth(0);
        jTableDocAtt.getColumnModel().getColumn(0).setMaxWidth(0);
        jTableDocAtt.getColumnModel().getColumn(2).setMinWidth(0);
        jTableDocAtt.getColumnModel().getColumn(2).setMaxWidth(0);

        jTableWk1Activities.getTableHeader().setReorderingAllowed(false);
        jTableWk2Activities.getTableHeader().setReorderingAllowed(false);
        jTableWk3Activities.getTableHeader().setReorderingAllowed(false);
        jTableWk4Activities.getTableHeader().setReorderingAllowed(false);
        jTableWk5Activities.getTableHeader().setReorderingAllowed(false);
        jTableWk1Activities.getColumnModel().getColumn(2).setMinWidth(0);
        jTableWk1Activities.getColumnModel().getColumn(2).setMaxWidth(0);
        jTableWk1Activities.getColumnModel().getColumn(4).setMinWidth(0);
        jTableWk1Activities.getColumnModel().getColumn(4).setMaxWidth(0);
        jTableWk1Activities.getColumnModel().getColumn(7).setMinWidth(0);
        jTableWk1Activities.getColumnModel().getColumn(7).setMaxWidth(0);
        jTableWk1Activities.getColumnModel().getColumn(10).setMinWidth(0);
        jTableWk1Activities.getColumnModel().getColumn(10).setMaxWidth(0);
        jTableWk1Activities.getColumnModel().getColumn(11).setMinWidth(0);
        jTableWk1Activities.getColumnModel().getColumn(11).setMaxWidth(0);
        jTableWk1Activities.getColumnModel().getColumn(18).setMinWidth(0);
        jTableWk1Activities.getColumnModel().getColumn(18).setMaxWidth(0);
        jTableWk2Activities.getColumnModel().getColumn(2).setMinWidth(0);
        jTableWk2Activities.getColumnModel().getColumn(2).setMaxWidth(0);
        jTableWk2Activities.getColumnModel().getColumn(4).setMinWidth(0);
        jTableWk2Activities.getColumnModel().getColumn(4).setMaxWidth(0);
        jTableWk2Activities.getColumnModel().getColumn(7).setMinWidth(0);
        jTableWk2Activities.getColumnModel().getColumn(7).setMaxWidth(0);
        jTableWk2Activities.getColumnModel().getColumn(10).setMinWidth(0);
        jTableWk2Activities.getColumnModel().getColumn(10).setMaxWidth(0);
        jTableWk2Activities.getColumnModel().getColumn(11).setMinWidth(0);
        jTableWk2Activities.getColumnModel().getColumn(11).setMaxWidth(0);
        jTableWk2Activities.getColumnModel().getColumn(18).setMinWidth(0);
        jTableWk2Activities.getColumnModel().getColumn(18).setMaxWidth(0);
        jTableWk3Activities.getColumnModel().getColumn(2).setMinWidth(0);
        jTableWk3Activities.getColumnModel().getColumn(2).setMaxWidth(0);
        jTableWk3Activities.getColumnModel().getColumn(4).setMinWidth(0);
        jTableWk3Activities.getColumnModel().getColumn(4).setMaxWidth(0);
        jTableWk3Activities.getColumnModel().getColumn(7).setMinWidth(0);
        jTableWk3Activities.getColumnModel().getColumn(7).setMaxWidth(0);
        jTableWk3Activities.getColumnModel().getColumn(10).setMinWidth(0);
        jTableWk3Activities.getColumnModel().getColumn(10).setMaxWidth(0);
        jTableWk3Activities.getColumnModel().getColumn(11).setMinWidth(0);
        jTableWk3Activities.getColumnModel().getColumn(11).setMaxWidth(0);
        jTableWk3Activities.getColumnModel().getColumn(18).setMinWidth(0);
        jTableWk3Activities.getColumnModel().getColumn(18).setMaxWidth(0);
        jTableWk4Activities.getColumnModel().getColumn(2).setMinWidth(0);
        jTableWk4Activities.getColumnModel().getColumn(2).setMaxWidth(0);
        jTableWk4Activities.getColumnModel().getColumn(4).setMinWidth(0);
        jTableWk4Activities.getColumnModel().getColumn(4).setMaxWidth(0);
        jTableWk4Activities.getColumnModel().getColumn(7).setMinWidth(0);
        jTableWk4Activities.getColumnModel().getColumn(7).setMaxWidth(0);
        jTableWk4Activities.getColumnModel().getColumn(10).setMinWidth(0);
        jTableWk4Activities.getColumnModel().getColumn(10).setMaxWidth(0);
        jTableWk4Activities.getColumnModel().getColumn(11).setMinWidth(0);
        jTableWk4Activities.getColumnModel().getColumn(11).setMaxWidth(0);
        jTableWk4Activities.getColumnModel().getColumn(18).setMinWidth(0);
        jTableWk4Activities.getColumnModel().getColumn(18).setMaxWidth(0);
        jTableWk5Activities.getColumnModel().getColumn(2).setMinWidth(0);
        jTableWk5Activities.getColumnModel().getColumn(2).setMaxWidth(0);
        jTableWk5Activities.getColumnModel().getColumn(4).setMinWidth(0);
        jTableWk5Activities.getColumnModel().getColumn(4).setMaxWidth(0);
        jTableWk5Activities.getColumnModel().getColumn(7).setMinWidth(0);
        jTableWk5Activities.getColumnModel().getColumn(7).setMaxWidth(0);
        jTableWk5Activities.getColumnModel().getColumn(10).setMinWidth(0);
        jTableWk5Activities.getColumnModel().getColumn(10).setMaxWidth(0);
        jTableWk5Activities.getColumnModel().getColumn(11).setMinWidth(0);
        jTableWk5Activities.getColumnModel().getColumn(11).setMaxWidth(0);
        jTableWk5Activities.getColumnModel().getColumn(18).setMinWidth(0);
        jTableWk5Activities.getColumnModel().getColumn(18).setMaxWidth(0);
        jTableDocAttWk1Main.getColumnModel().getColumn(2).setMinWidth(0);
        jTableDocAttWk1Main.getColumnModel().getColumn(2).setMaxWidth(0);
        jTableDocAttWk2Main.getColumnModel().getColumn(2).setMinWidth(0);
        jTableDocAttWk2Main.getColumnModel().getColumn(2).setMaxWidth(0);
        jTableDocAttWk3Main.getColumnModel().getColumn(2).setMinWidth(0);
        jTableDocAttWk3Main.getColumnModel().getColumn(2).setMaxWidth(0);
        jTableDocAttWk4Main.getColumnModel().getColumn(2).setMinWidth(0);
        jTableDocAttWk4Main.getColumnModel().getColumn(2).setMaxWidth(0);
        jTableDocAttWk5Main.getColumnModel().getColumn(2).setMinWidth(0);
        jTableDocAttWk5Main.getColumnModel().getColumn(2).setMaxWidth(0);
        try {
            // showDate();
            tH.intShowDate();
        } catch (IOException ex) {
            Logger.getLogger(JFrameMnthPlanPerDiemCreate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(JFrameMnthPlanPerDiemCreate.class.getName()).log(Level.SEVERE, null, ex);
        }
        showTime();
        computerName();
        jLabelEmp.setText(usrLogNam);
        jLabelEmp.setVisible(false);
        findUser();
        allowanceRate();
        findUserGrp();
        findAccCode();
        findDonorCode();
        findGrantBud();
        findPrjProgCode();
        jLabelWk1Name1Del.setVisible(false);
        jLabelWk1Name2Del.setVisible(false);
        jLabelWk1Name3Del.setVisible(false);
        jLabelWk1Name4Del.setVisible(false);
        jTextAttDocName.setEditable(false);
        jTextAttDocNameWk1Main.setEditable(false);
        jTextAttDocPath.setVisible(false);
        jPanelAddStaff.setVisible(false);
        jTextAttDocPathWk1Main.setVisible(false);
        jTextAttDocPathWk2Main.setVisible(false);
        jTextAttDocPathWk3Main.setVisible(false);
        jTextAttDocPathWk4Main.setVisible(false);
        jTextAttDocPathWk5Main.setVisible(false);
        jLabelPrjCodeProgramming.setVisible(false);
        jComboProjectCodeProgramming.setVisible(false);

        findUserGrp();
        jLabelLineDate.setText(tH.internetDate);
        jLabelLineDate1.setText(tH.internetDate);
        jLabelLineDate2.setText(tH.internetDate);
        jLabelLineDate3.setText(tH.internetDate);
        jLabelLineDate4.setText(tH.internetDate);

//        if (!"National Office".equals(jLabelDistrict.getText())) {
//            jTabbedPane1.setTitleAt(0, "Month Plan");
//            jTabbedPane1.setEnabledAt(1, false);
//            jTabbedPane1.setTitleAt(1, "");
//            jTabbedPane1.setEnabledAt(2, false);
//            jTabbedPane1.setTitleAt(2, "");
//            jTabbedPane1.setEnabledAt(3, false);
//            jTabbedPane1.setTitleAt(3, "");
//            jTabbedPane1.setEnabledAt(4, false);
//            jTabbedPane1.setTitleAt(4, "");
//        }
        if (!"Administrator".equals(usrGrp)) {
            jMenuItemUserProfUpd.setEnabled(false);
            jMenuItemUserCreate.setEnabled(false);
        }

        jLabelDialogDepartActivityDate.setText("<html>Depature Date and <br> depature time</html>");
        jLabelDialogReturnActivityDate.setText("<html>Return Date and <br> time of arrival</html>");
    }

    private void makeFrameFullSize(JFrameMnthPlanPerDiemCreate aFrame) {
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        aFrame.setSize(screenSize.width, screenSize.height);
//        aFrame.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    }

    void computerName() {

        try {
            java.net.InetAddress addr = InetAddress.getLocalHost();
            hostName = addr.getHostName();

        } catch (Exception ex) {
            System.out.println("Hostname can not be resolved");
        }
    }

    void finDayCalcWk1() {
        noOfFinDays1 = 0;
        try {
            ArrayList<LocalDate> list = new ArrayList<LocalDate>();
            for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
                String reqDay = jTableWk1Activities.getValueAt(i, 0).toString();
                LocalDate reqDate = LocalDate.parse(reqDay);
                list.add(reqDate);
            }

            minDateWk1 = Collections.min(list);
            maxDateWk1 = Collections.max(list);
            noOfFinDays1 = ChronoUnit.DAYS.between(minDateWk1, maxDateWk1) + 1;

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void finDayCalcWk2() {
        noOfFinDays2 = 0;
        try {
            ArrayList<LocalDate> list = new ArrayList<LocalDate>();
            for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
                String reqDay = jTableWk2Activities.getValueAt(i, 0).toString();
                LocalDate reqDate = LocalDate.parse(reqDay);
                list.add(reqDate);
            }

            minDateWk2 = Collections.min(list);
            maxDateWk2 = Collections.max(list);
            noOfFinDays2 = ChronoUnit.DAYS.between(minDateWk2, maxDateWk2) + 1;

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void finDayCalcWk3() {
        noOfFinDays3 = 0;
        try {
            ArrayList<LocalDate> list = new ArrayList<LocalDate>();
            for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
                String reqDay = jTableWk3Activities.getValueAt(i, 0).toString();
                LocalDate reqDate = LocalDate.parse(reqDay);
                list.add(reqDate);
            }

            minDateWk3 = Collections.min(list);
            maxDateWk3 = Collections.max(list);
            noOfFinDays3 = ChronoUnit.DAYS.between(minDateWk3, maxDateWk3) + 1;

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void finDayCalcWk4() {
        noOfFinDays4 = 0;
        try {
            ArrayList<LocalDate> list = new ArrayList<LocalDate>();
            for (int i = 0; i < jTableWk4Activities.getRowCount(); i++) {
                String reqDay = jTableWk4Activities.getValueAt(i, 0).toString();
                LocalDate reqDate = LocalDate.parse(reqDay);
                list.add(reqDate);
            }

            minDateWk4 = Collections.min(list);
            maxDateWk4 = Collections.max(list);
            noOfFinDays4 = ChronoUnit.DAYS.between(minDateWk4, maxDateWk4) + 1;

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void finDayCalcWk5() {
        noOfFinDays5 = 0;
        try {
            ArrayList<LocalDate> list = new ArrayList<LocalDate>();
            for (int i = 0; i < jTableWk5Activities.getRowCount(); i++) {
                String reqDay = jTableWk5Activities.getValueAt(i, 0).toString();
                LocalDate reqDate = LocalDate.parse(reqDay);
                list.add(reqDate);
            }

            minDateWk5 = Collections.min(list);
            maxDateWk5 = Collections.max(list);
            noOfFinDays5 = ChronoUnit.DAYS.between(minDateWk5, maxDateWk5) + 1;

        } catch (Exception e) {
            System.out.println(e);
        }
    }

//    void localToDate() {
//
//        minDateComWk1 = Date.from(minDateWk1.atStartOfDay(zoneId).toInstant());
//        maxDateComWk1 = Date.from(maxDateWk1.atStartOfDay(zoneId).toInstant());
//        minDateComWk2 = Date.from(minDateWk2.atStartOfDay(zoneId).toInstant());
//        maxDateComWk2 = Date.from(maxDateWk2.atStartOfDay(zoneId).toInstant());
//        minDateComWk3 = Date.from(minDateWk3.atStartOfDay(zoneId).toInstant());
//        maxDateComWk3 = Date.from(maxDateWk3.atStartOfDay(zoneId).toInstant());
//        minDateComWk4 = Date.from(minDateWk4.atStartOfDay(zoneId).toInstant());
//        maxDateComWk4 = Date.from(maxDateWk4.atStartOfDay(zoneId).toInstant());
//        minDateComWk5 = Date.from(minDateWk5.atStartOfDay(zoneId).toInstant());
//        maxDateComWk5 = Date.from(maxDateWk5.atStartOfDay(zoneId).toInstant());
//
//    }
    void CheckDuplicateUser1() {
        try {

            duplicateUser1Count = null;

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT distinct PLAN_REF_NUM,ACT_DATE,count(act_date) FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[" + planWk + "]  where ACT_DATE = '"
                    + planDate + "' "
                    + "and  (" + empNamNum1 + " = '" + empNam1 + "' or  " + empNamNum2 + " = '" + empNam1 + ""
                    + "' or " + empNamNum3 + " = '" + empNam1 + "' or " + empNamNum4 + " = '" + empNam1 + "')"
                    + "AND ACT_REC_STA in ('C','A') group by PLAN_REF_NUM,ACT_DATE");

            while (r.next()) {

                duplicateUser1Count = r.getString(3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void CheckDuplicateUser2() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT distinct PLAN_REF_NUM,ACT_DATE,count(act_date) FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[" + planWk + "]  where ACT_DATE = '"
                    + planDate + "' "
                    + "and  (" + empNamNum1 + " = '" + empNam1 + "' or  " + empNamNum2 + " = '" + empNam2 + "' or " + empNamNum3 + " = '" + empNam3 + "' or " + empNamNum4 + " = '" + empNam4 + "')"
                    + "AND ACT_REC_STA in ('C','A') group by PLAN_REF_NUM,ACT_DATE");

            while (r.next()) {

                duplicateUser2Count = r.getString(3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void CheckDuplicateUser3() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT distinct PLAN_REF_NUM,ACT_DATE,count(act_date) FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[" + planWk + "]  where ACT_DATE = '"
                    + planDate + "' "
                    + "and  (" + empNamNum1 + " = '" + empNam1 + "' or  " + empNamNum2 + " = '" + empNam2 + "' or " + empNamNum3 + " = '" + empNam3 + "' or " + empNamNum4 + " = '" + empNam4 + "')"
                    + "AND ACT_REC_STA in ('C','A') group by PLAN_REF_NUM,ACT_DATE");

            while (r.next()) {

                duplicateUser3Count = r.getString(3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void CheckDuplicateUser4() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT distinct PLAN_REF_NUM,ACT_DATE,count(act_date) FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[" + planWk + "]  where ACT_DATE = '"
                    + planDate + "' "
                    + "and  (" + empNamNum1 + " = '" + empNam1 + "' or  " + empNamNum2 + " = '" + empNam2 + "' or " + empNamNum3 + " = '" + empNam3 + "' or " + empNamNum4 + " = '" + empNam4 + "')"
                    + "AND ACT_REC_STA in ('C','A') group by PLAN_REF_NUM,ACT_DATE");

            while (r.next()) {

                duplicateUser4Count = r.getString(3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void CheckDuplicateUser5() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT distinct PLAN_REF_NUM,ACT_DATE,count(act_date) FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[" + planWk + "]  where ACT_DATE = '"
                    + planDate + "' "
                    + "and  (" + empNamNum1 + " = '" + empNam1 + "' or  " + empNamNum2 + " = '" + empNam2 + "' "
                    + "or " + empNamNum3 + " = '" + empNam3 + "' or " + empNamNum4 + " = '" + empNam4 + "')"
                    + "AND ACT_REC_STA in ('C','A') group by PLAN_REF_NUM,ACT_DATE");

            while (r.next()) {

                duplicateUser5Count = r.getString(3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void findUser() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();
            System.out.println("user ");
            ResultSet r = st.executeQuery("select EMP_NAM,SUP_EMP_NUM,SUP_NAM,EMP_SUP_MAIL,"
                    + "EMP_PROVINCE,EMP_OFFICE from [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] a "
                    + "join [ClaimsAppSysZvandiri].[dbo].[EmpDetWS] b on a.EMP_NUM=b.EMP_NUM "
                    + "where a.EMP_NUM='" + jLabelEmp.getText() + "'");

            while (r.next()) {

                jLabelLineLogNam.setText(r.getString(1));
                jLabelLineLogNam1.setText(r.getString(1));
                jLabelLineLogNam2.setText(r.getString(1));
                jLabelLineLogNam3.setText(r.getString(1));
                jLabelLineLogNam4.setText(r.getString(1));
                createUsrNam = r.getString(1);
                supEmpNum = r.getString(2);
                supNam = r.getString(3);
                supUsrMail = r.getString(4);
                jLabelProvince.setText(r.getString(5));
                jLabelDistrict.setText(r.getString(6));
            }

            //                 conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void SerialCheck() {

        int count = 0;
        do {

            try {

                Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                        + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

                String sqlDeleteBlankLock = "delete [ClaimsAppSysZvandiri].[dbo].[SerialLock] where (SERIAL = ' ' or SERIAL is null or REF_NUM = 0)";

                pst = conn.prepareStatement(sqlDeleteBlankLock);
                pst.executeUpdate();

                Statement st = conn.createStatement();

                st.executeQuery("SELECT count(*) FROM [ClaimsAppSysZvandiri].[dbo].[SerialLock] where SERIAL = 'P'");

                ResultSet rs = st.getResultSet();
                //  jLabelRegNum.setVisible(false);
                while (rs.next()) {
                    count = Integer.parseInt(rs.getString(1));

                }
                // conn.close();

                if (count > 0) {
                    try {

                        Statement st1 = conn.createStatement();

                        st1.executeQuery("SELECT USR_NAM,LCK_DATE_TIME FROM [ClaimsAppSysZvandiri].[dbo].[SerialLock] where SERIAL = 'P'");

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

                        if ((jLabelLineLogNam.getText().equals(usrnam)) && (seconds >= 1)) {

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
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlDeleteLock = "delete [ClaimsAppSysZvandiri].[dbo].[SerialLock] where SERIAL = "
                    + "'P'";

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

            st.executeQuery("SELECT REF_NUM + 1,SERIAL,REF_NUM FROM [ClaimsAppSysZvandiri].[dbo].[refNumTab] where SERIAL = 'P'");

            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                jLabelRefNum.setText(rs.getString(1));
                jLabelSerial.setText(rs.getString(2));
                newPlanRefNum = rs.getInt(1);

            }
            pst = conn.prepareStatement(sqlSerialLock);
            pst.setString(1, jLabelSerial.getText());
            pst.setString(2, jLabelRefNum.getText());
            pst.setString(3, jLabelLineLogNam.getText());

            pst.executeUpdate();

            String sqlSerialLockUpdate = "update [ClaimsAppSysZvandiri].[dbo].[SerialLock]"
                    + " set LCK_DATE_TIME = ( SELECT SYSDATETIME()) where "
                    + "concat(SERIAL,REF_NUM) = '" + jLabelSerial.getText() + jLabelRefNum.getText() + "' ";

            pst1 = conn.prepareStatement(sqlSerialLockUpdate);
            pst1.executeUpdate();

            jLabelSerial.setVisible(false);
            jLabelRefNum.setVisible(false);

            st1.executeQuery("SELECT max(PLAN_REF_NUM) FROM [ClaimsAppSysZvandiri].[dbo].[PlanCreatorTab]");

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
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sql1 = "update [ClaimsAppSysZvandiri].[dbo].[refNumTab] set "
                    + "REF_NUM ='" + jLabelRefNum.getText() + "' where SERIAL = 'P'";

            pst = conn.prepareStatement(sql1);
            pst.executeUpdate();

            deleteLongLock();

            jLabelSerial.setVisible(true);
            jLabelRefNum.setVisible(true);

            JOptionPane.showMessageDialog(this, "<html> Plan Ref No. <b>" + jLabelSerial.getText() + " " + jLabelRefNum.getText()
                    + "</b> successfully registered.</html>");

            jDialogWaitingEmail.setVisible(true);

            String mailMsg = "<html><body> Dear " + supNam + "<br><br><b>"
                    + jLabelLineLogNam.getText() + "</b> has submitted a plan for your review and approval.<br><br>"
                    + "Please check your Finance System inbox and action<br><br> Kind Regards <br><br>"
                    + " Finance Management System </body></html>";

            String MailMsgTitle = "Plan Reference No. " + jLabelSerial.getText() + " " + jLabelRefNum.getText() + "  Approval.";

            emSend.sendMail(MailMsgTitle, supUsrMail, mailMsg, "");

            jDialogWaitingEmail.setVisible(false);
            JOptionPane.showMessageDialog(this, "An email has been sent to " + supNam + " for processing.");
            new JFrameMnthPlanPerDiemCreate(jLabelEmp.getText()).setVisible(true);
            setVisible(false);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
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
                jLabelLineTime4.setText(s.format(d));
                /*To Change*/

            }
        }) {

        }.start();

    }

//    void showDate() {
//        Date d = new Date();
//        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
//
//        jLabelLineDate.setText(s.format(d));
//        jLabelLineDate1.setText(s.format(d));
//        jLabelLineDate2.setText(s.format(d));
//        jLabelLineDate3.setText(s.format(d));
//        jLabelLineDate4.setText(s.format(d));
//        /*To Change*/
//
//    }
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

    void findProvMan() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT EMP_NAM,EMP_MAIL  FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab]  "
                    + "where EMP_NUM = (SELECT EMP_NUM  FROM [ClaimsAppSysZvandiri].[dbo].[ProvManTab] "
                    + "where Province = '" + jLabelProvince.getText() + "')");

            while (r.next()) {
                sendToProvMgr = r.getString(1);
                provMgrMail = r.getString(2);
            }

            //                 conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findProvince() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            //jComboProvince.setSelectedIndex(-1);
            ResultSet r = st.executeQuery("select distinct(province) from [ClaimsAppSysZvandiri].[dbo].[DistFacTab]");

            while (r.next()) {

                //   jComboProvince.addItem(r.getString("province"));
                jComboProvinceFacility.addItem(r.getString("province"));
//                jComboProvince.addItem(r.getString("province"));

            }

            //                 conn.close();
        } catch (Exception e) {

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

    void findLastMonthDay() {
        try {
            Date today = jDateChooserWk1From.getDate();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);

            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);

            Date lastDayOfMonth = calendar.getTime();

            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            lastDateofMonth = sdf.format(lastDayOfMonth);
            System.out.println("Today            : " + sdf.format(today));
            System.out.println("Last Day of Month: " + sdf.format(lastDayOfMonth));
        } catch (Exception e) {
        }
    }

    void facilityPOP() {
        jDialogFacility.setVisible(true);
        jDialogFacility.setVisible(false);
        jDialogFacility.setVisible(true);
    }

    void budCreate() {
        String taskAccCode = jComboAccountCode.getSelectedItem().toString();
        String taskPartsAccCode[] = taskAccCode.split(" ", 2);
        accCode = taskPartsAccCode[0];
        accCodeName = taskPartsAccCode[1];

        taskDonor = jComboDonor.getSelectedItem().toString();
        String taskPartsDonor[] = taskDonor.split(" ", 2);
        donor = taskPartsDonor[0];
        donorName = taskPartsDonor[1];

        String taskPrjCode = jComboProjectCodeGL.getSelectedItem().toString();
        String taskPartsPrjCode[] = taskPrjCode.split(" ", 2);
        prjCode = taskPartsPrjCode[0];
        prjCodeName = taskPartsPrjCode[1];

        String taskPrjCodeProg = jComboProjectCodeProgramming.getSelectedItem().toString();
        String taskPartsPrjCodeProg[] = taskPrjCodeProg.split(" ", 2);
        prjProgCode = taskPartsPrjCodeProg[0];
        prjProgCodeName = taskPartsPrjCodeProg[1];

        String taskBudLine = jComboBudMainCode.getSelectedItem().toString();
        String taskPartsBudLine[] = taskBudLine.split(" ", 2);
        budLine = taskPartsBudLine[0];
        budLineName = taskPartsBudLine[1];

        subBudLine = "S071";

        budcode = accCode + "/ZW/" + donor + "/" + prjCode + "/" + grantCode + "/" + budLine + "/" + subBudLine + "/NAT1";

        String taskBudCode = budcode;
        String taskPartsBudCode[] = taskBudCode.split("/", 5);
        String budCodeStr = taskPartsBudCode[3];

//        System.out.println("Budcode " + accCode + "/ZW/" + donor + "/" + prjCode + "/" + grantCode + "/" + budLine + "/" + subBudLine + "/NAT1" + "  " + accCodeName);
        System.out.println("kvll " + budcode);
        System.out.println("kvllgg " + budCodeStr);
    }

    void allowanceRate() {
        try {
            String rateCat;
            if ("National Office".equals(jLabelDistrict.getText())) {
                rateCat = "A";
            } else {
                rateCat = "B";
            }
            System.out.println("cat " + rateCat + " " + jLabelDistrict.getText());
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT Lunch,Dinner,Incidental,Unproved_Accommodation,Proved_Accommodation  "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAllowanceTab] ");

            while (r.next()) {

                lunchAll = r.getString(1);
                dinnerAll = r.getString(2);
                incidentalAll = r.getString(3);
                unProvedAll = r.getString(4);
                provedAll = r.getString(5);

            }
            //                 conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void deleteFileAtt() {
        int selectedOption = JOptionPane.showConfirmDialog(null,
                "Do you want to delete selected casual attachement?",
                "Choose", JOptionPane.YES_NO_OPTION);

        if (selectedOption == JOptionPane.YES_OPTION) {

            int[] rows = jTableDocAtt.getSelectedRows();
            if (rows.length > 0) {
                for (int i = 0; i < rows.length; i++) {
                    modelAtt.removeRow(rows[i] - i);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please note that the table is empty.");

            }

        }
    }

    void deleteFileAttWk1Main() {
        int selectedOption = JOptionPane.showConfirmDialog(this,
                "Do you want to delete selected casual attachement?",
                "Choose", JOptionPane.YES_NO_OPTION);

        if (selectedOption == JOptionPane.YES_OPTION) {

            int[] rows = jTableDocAttWk1Main.getSelectedRows();
            if (rows.length > 0) {
                for (int i = 0; i < rows.length; i++) {
                    modelAttWk1Main.removeRow(rows[i] - i);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please note that the table is empty.");

            }

        }
    }

    void deleteFileAttWk2Main() {
        int selectedOption = JOptionPane.showConfirmDialog(this,
                "Do you want to delete selected casual attachement?",
                "Choose", JOptionPane.YES_NO_OPTION);

        if (selectedOption == JOptionPane.YES_OPTION) {

            int[] rows = jTableDocAttWk2Main.getSelectedRows();
            if (rows.length > 0) {
                for (int i = 0; i < rows.length; i++) {
                    modelAttWk2Main.removeRow(rows[i] - i);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please note that the table is empty.");

            }

        }
    }

    void deleteFileAttWk3Main() {
        int selectedOption = JOptionPane.showConfirmDialog(this,
                "Do you want to delete selected casual attachement?",
                "Choose", JOptionPane.YES_NO_OPTION);

        if (selectedOption == JOptionPane.YES_OPTION) {

            int[] rows = jTableDocAttWk3Main.getSelectedRows();
            if (rows.length > 0) {
                for (int i = 0; i < rows.length; i++) {
                    modelAttWk3Main.removeRow(rows[i] - i);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please note that the table is empty.");

            }

        }
    }

    void deleteFileAttWk4Main() {
        int selectedOption = JOptionPane.showConfirmDialog(this,
                "Do you want to delete selected casual attachement?",
                "Choose", JOptionPane.YES_NO_OPTION);

        if (selectedOption == JOptionPane.YES_OPTION) {

            int[] rows = jTableDocAttWk4Main.getSelectedRows();
            if (rows.length > 0) {
                for (int i = 0; i < rows.length; i++) {
                    modelAttWk4Main.removeRow(rows[i] - i);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please note that the table is empty.");

            }

        }
    }

    void deleteFileAttWk5Main() {
        int selectedOption = JOptionPane.showConfirmDialog(this,
                "Do you want to delete selected casual attachement?",
                "Choose", JOptionPane.YES_NO_OPTION);

        if (selectedOption == JOptionPane.YES_OPTION) {

            int[] rows = jTableDocAttWk5Main.getSelectedRows();
            if (rows.length > 0) {
                for (int i = 0; i < rows.length; i++) {
                    modelAttWk5Main.removeRow(rows[i] - i);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please note that the table is empty.");

            }

        }
    }

    void UsrRecUpd() {

        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk1Activities.getValueAt(i, 19).toString()))) {
                usrRecNam = jTableWk1Activities.getValueAt(i, 19).toString();
                actDate = jTableWk1Activities.getValueAt(i, 0).toString();
                UsrRecWk = "1";
                usrActType = "Per Diem";
                WkUsrRecUpd();
            }
        }

        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk1Activities.getValueAt(i, 20).toString()))) {
                usrRecNam = jTableWk1Activities.getValueAt(i, 20).toString();
                actDate = jTableWk1Activities.getValueAt(i, 0).toString();
                UsrRecWk = "1";
                usrActType = "Per Diem";
                WkUsrRecUpd();
            }
        }

        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk1Activities.getValueAt(i, 21).toString()))) {
                usrRecNam = jTableWk1Activities.getValueAt(i, 21).toString();
                actDate = jTableWk1Activities.getValueAt(i, 0).toString();
                UsrRecWk = "1";
                usrActType = "Per Diem";
                WkUsrRecUpd();
            }
        }
        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk1Activities.getValueAt(i, 22).toString()))) {
                usrRecNam = jTableWk1Activities.getValueAt(i, 22).toString();
                actDate = jTableWk1Activities.getValueAt(i, 0).toString();
                UsrRecWk = "1";
                usrActType = "Per Diem";
                WkUsrRecUpd();
            }
        }

        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk2Activities.getValueAt(i, 19).toString()))) {
                usrRecNam = jTableWk2Activities.getValueAt(i, 19).toString();
                actDate = jTableWk2Activities.getValueAt(i, 0).toString();
                UsrRecWk = "2";
                usrActType = "Per Diem";
                WkUsrRecUpd();
            }
        }

        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk2Activities.getValueAt(i, 20).toString()))) {
                usrRecNam = jTableWk2Activities.getValueAt(i, 20).toString();
                actDate = jTableWk2Activities.getValueAt(i, 0).toString();
                UsrRecWk = "2";
                usrActType = "Per Diem";
                WkUsrRecUpd();
            }
        }
        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk2Activities.getValueAt(i, 21).toString()))) {
                usrRecNam = jTableWk2Activities.getValueAt(i, 21).toString();
                actDate = jTableWk2Activities.getValueAt(i, 0).toString();
                UsrRecWk = "2";
                usrActType = "Per Diem";
                WkUsrRecUpd();
            }
        }
        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk2Activities.getValueAt(i, 22).toString()))) {
                usrRecNam = jTableWk2Activities.getValueAt(i, 22).toString();
                actDate = jTableWk2Activities.getValueAt(i, 0).toString();
                UsrRecWk = "2";
                usrActType = "Per Diem";
                WkUsrRecUpd();
            }
        }

        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk3Activities.getValueAt(i, 19).toString()))) {
                usrRecNam = jTableWk3Activities.getValueAt(i, 19).toString();
                actDate = jTableWk3Activities.getValueAt(i, 0).toString();
                UsrRecWk = "3";
                usrActType = "Per Diem";
                WkUsrRecUpd();
            }
        }

        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk3Activities.getValueAt(i, 20).toString()))) {
                usrRecNam = jTableWk3Activities.getValueAt(i, 20).toString();
                actDate = jTableWk3Activities.getValueAt(i, 0).toString();
                UsrRecWk = "3";
                usrActType = "Per Diem";
                WkUsrRecUpd();
            }
        }
        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk3Activities.getValueAt(i, 21).toString()))) {
                usrRecNam = jTableWk3Activities.getValueAt(i, 21).toString();
                actDate = jTableWk3Activities.getValueAt(i, 0).toString();
                UsrRecWk = "3";
                usrActType = "Per Diem";
                WkUsrRecUpd();
            }
        }
        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk3Activities.getValueAt(i, 22).toString()))) {
                usrRecNam = jTableWk3Activities.getValueAt(i, 22).toString();
                actDate = jTableWk3Activities.getValueAt(i, 0).toString();
                UsrRecWk = "3";
                usrActType = "Per Diem";
                WkUsrRecUpd();
            }
        }

        for (int i = 0; i < jTableWk4Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk4Activities.getValueAt(i, 19).toString()))) {
                usrRecNam = jTableWk4Activities.getValueAt(i, 19).toString();
                actDate = jTableWk4Activities.getValueAt(i, 0).toString();
                UsrRecWk = "4";
                usrActType = "Per Diem";
                WkUsrRecUpd();
            }
        }

        for (int i = 0; i < jTableWk4Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk4Activities.getValueAt(i, 20).toString()))) {
                usrRecNam = jTableWk4Activities.getValueAt(i, 20).toString();
                actDate = jTableWk4Activities.getValueAt(i, 0).toString();
                UsrRecWk = "4";
                usrActType = "Per Diem";

                WkUsrRecUpd();
            }
        }
        for (int i = 0; i < jTableWk4Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk4Activities.getValueAt(i, 21).toString()))) {
                usrRecNam = jTableWk4Activities.getValueAt(i, 21).toString();
                actDate = jTableWk4Activities.getValueAt(i, 0).toString();
                UsrRecWk = "4";
                usrActType = "Per Diem";
                WkUsrRecUpd();
            }
        }
        for (int i = 0; i < jTableWk4Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk4Activities.getValueAt(i, 22).toString()))) {
                usrRecNam = jTableWk4Activities.getValueAt(i, 22).toString();
                actDate = jTableWk4Activities.getValueAt(i, 0).toString();
                UsrRecWk = "4";
                usrActType = "Per Diem";
                WkUsrRecUpd();
            }
        }

        for (int i = 0; i < jTableWk5Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk5Activities.getValueAt(i, 19).toString()))) {
                usrRecNam = jTableWk5Activities.getValueAt(i, 19).toString();
                actDate = jTableWk5Activities.getValueAt(i, 0).toString();
                UsrRecWk = "5";
                usrActType = "Per Diem";
                WkUsrRecUpd();
            }
        }

        for (int i = 0; i < jTableWk5Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk5Activities.getValueAt(i, 20).toString()))) {
                usrRecNam = jTableWk5Activities.getValueAt(i, 20).toString();
                actDate = jTableWk5Activities.getValueAt(i, 0).toString();
                UsrRecWk = "5";
                usrActType = "Per Diem";
                WkUsrRecUpd();
            }
        }
        for (int i = 0; i < jTableWk5Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk5Activities.getValueAt(i, 21).toString()))) {
                usrRecNam = jTableWk5Activities.getValueAt(i, 21).toString();
                actDate = jTableWk5Activities.getValueAt(i, 0).toString();
                UsrRecWk = "5";
                usrActType = "Per Diem";
                WkUsrRecUpd();
            }
        }
        for (int i = 0; i < jTableWk5Activities.getRowCount(); i++) {
            if (!("".equals(jTableWk5Activities.getValueAt(i, 22).toString()))) {
                usrRecNam = jTableWk5Activities.getValueAt(i, 22).toString();
                actDate = jTableWk5Activities.getValueAt(i, 0).toString();
                UsrRecWk = "5";
                usrActType = "Per Diem";
                WkUsrRecUpd();
            }
        }

    }

    void WkUsrRecUpd() {

        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlrecplan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanUsrRecTab] VALUES (?,?,?,?,?,?,?,?,?)";
            pst1 = conn.prepareStatement(sqlrecplan);
            pst1.setString(1, "P");
            pst1.setString(2, jLabelRefNum.getText());
            pst1.setString(3, actDate);
            pst1.setString(4, usrRecNam);
            pst1.setString(5, UsrRecWk);
            pst1.setString(6, usrActType);
            pst1.setString(7, "1");
            pst1.setString(8, "1");
            pst1.setString(9, "A");
            pst1.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void createAttDocWk1Main() {
        try {
            int itmNumAtt = 1;
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysFB;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");
            for (int i = 0; i < modelAttWk1Main.getRowCount(); i++) {
                String imgFileValue = modelAttWk1Main.getValueAt(i, 2).toString();
                String imgFileDsc = modelAttWk1Main.getValueAt(i, 0).toString();
                String imgFileName = modelAttWk1Main.getValueAt(i, 1).toString();

                if (imgFileValue.trim().length() > 0) {

                    attL.insertAttWkDoc(conn, imgFileName, imgFileDsc, imgFileValue,
                            itmNumAtt, jLabelRefNum.getText(), "1", "1", "ClaimAttDocJustTabWk1", finyear);

                }

                itmNumAtt = itmNumAtt + 1;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void createAttDocWk2Main() {
        try {
            int itmNumAtt = 1;
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysFB;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");
            for (int i = 0; i < modelAttWk2Main.getRowCount(); i++) {
                String imgFileValue = modelAttWk2Main.getValueAt(i, 2).toString();
                String imgFileDsc = modelAttWk2Main.getValueAt(i, 0).toString();
                String imgFileName = modelAttWk2Main.getValueAt(i, 1).toString();

                if (imgFileValue.trim().length() > 0) {

                    attL.insertAttWkDoc(conn, imgFileName, imgFileDsc, imgFileValue,
                            itmNumAtt, jLabelRefNum.getText(), "1", "1", "ClaimAttDocJustTabWk2", finyear);

                }

                itmNumAtt = itmNumAtt + 1;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void createAttDocWk3Main() {
        try {
            int itmNumAtt = 1;
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysFB;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");
            for (int i = 0; i < modelAttWk3Main.getRowCount(); i++) {
                String imgFileValue = modelAttWk3Main.getValueAt(i, 2).toString();
                String imgFileDsc = modelAttWk3Main.getValueAt(i, 0).toString();
                String imgFileName = modelAttWk3Main.getValueAt(i, 1).toString();

                if (imgFileValue.trim().length() > 0) {

                    attL.insertAttWkDoc(conn, imgFileName, imgFileDsc, imgFileValue,
                            itmNumAtt, jLabelRefNum.getText(), "1", "1", "ClaimAttDocJustTabWk3", finyear);

                }

                itmNumAtt = itmNumAtt + 1;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void createAttDocWk4Main() {
        try {
            int itmNumAtt = 1;
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysFB;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");
            for (int i = 0; i < modelAttWk4Main.getRowCount(); i++) {
                String imgFileValue = modelAttWk4Main.getValueAt(i, 2).toString();
                String imgFileDsc = modelAttWk4Main.getValueAt(i, 0).toString();
                String imgFileName = modelAttWk4Main.getValueAt(i, 1).toString();

                if (imgFileValue.trim().length() > 0) {

                    attL.insertAttWkDoc(conn, imgFileName, imgFileDsc, imgFileValue,
                            itmNumAtt, jLabelRefNum.getText(), "1", "1", "ClaimAttDocJustTabWk4", finyear);

                }

                itmNumAtt = itmNumAtt + 1;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void createAttDocWk5Main() {
        try {
            int itmNumAtt = 1;
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysFB;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");
            for (int i = 0; i < modelAttWk5Main.getRowCount(); i++) {
                String imgFileValue = modelAttWk5Main.getValueAt(i, 2).toString();
                String imgFileDsc = modelAttWk5Main.getValueAt(i, 0).toString();
                String imgFileName = modelAttWk5Main.getValueAt(i, 1).toString();

                if (imgFileValue.trim().length() > 0) {

                    attL.insertAttWkDoc(conn, imgFileName, imgFileDsc, imgFileValue,
                            itmNumAtt, jLabelRefNum.getText(), "1", "1", "ClaimAttDocJustTabWk5", finyear);

                }

                itmNumAtt = itmNumAtt + 1;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void createWk1Plan() {
        itmNum = 1;
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {

                String sqlwk1plan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] "
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlwk1plan);
                pst1.setString(1, "P");
                pst1.setString(2, jLabelRefNum.getText());
                pst1.setString(3, Integer.toString(itmNum));
                pst1.setString(4, jTableWk1Activities.getValueAt(i, 0).toString());
                pst1.setString(5, jTableWk1Activities.getValueAt(i, 1).toString());
                pst1.setString(6, jTableWk1Activities.getValueAt(i, 2).toString());
                pst1.setString(7, jTableWk1Activities.getValueAt(i, 3).toString());
                pst1.setString(8, jTableWk1Activities.getValueAt(i, 4).toString());
                pst1.setString(9, jTableWk1Activities.getValueAt(i, 5).toString());
                pst1.setString(10, jTableWk1Activities.getValueAt(i, 6).toString());
                pst1.setString(11, jTableWk1Activities.getValueAt(i, 7).toString());
                pst1.setString(12, jTableWk1Activities.getValueAt(i, 8).toString());
                pst1.setString(13, jTableWk1Activities.getValueAt(i, 9).toString());
                pst1.setString(14, jTableWk1Activities.getValueAt(i, 10).toString());
                pst1.setString(15, String.valueOf(jTableWk1Activities.getValueAt(i, 11).toString()));
                pst1.setString(16, String.valueOf(jTableWk1Activities.getValueAt(i, 12).toString()));
                pst1.setString(17, String.valueOf(jTableWk1Activities.getValueAt(i, 13).toString()));
                pst1.setString(18, String.valueOf(jTableWk1Activities.getValueAt(i, 14).toString()));
                pst1.setString(19, jTableWk1Activities.getValueAt(i, 15).toString());
                pst1.setString(20, String.valueOf(jTableWk1Activities.getValueAt(i, 16).toString()));
                pst1.setString(21, String.valueOf(jTableWk1Activities.getValueAt(i, 17).toString()));
                pst1.setString(22, String.valueOf(jTableWk1Activities.getValueAt(i, 18).toString()));
                pst1.setString(23, jTableWk1Activities.getValueAt(i, 19).toString());
                pst1.setString(24, jTableWk1Activities.getValueAt(i, 20).toString());
                pst1.setString(25, jTableWk1Activities.getValueAt(i, 21).toString());
                pst1.setString(26, jTableWk1Activities.getValueAt(i, 22).toString());
                pst1.setString(27, "1");
                pst1.setString(28, "1");
                pst1.setString(29, "A");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void addWkItmLine() {
        try {

            String Wk1Brk = "0.00";
            String Wk1Lnch = "0.00";
            String Wk1Dinner = "0.00";
            String Wk1UnProvedAcc = "0.00";
            String Wk1Inc = "0.00";
            String Wk1Misc = "0.00";
            String Wk1MiscDesc = "";
            String WkDTBrk = "0.00";
            String WkDTLnch = "0.00";
            String WkDTDinner = "0.00";
            String WkRTBrk = "0.00";
            String WkRTLnch = "0.00";
            String WkRTDinner = "0.00";
            String Wk1ProvedAcc = "0.00";
            String WkRTUnProvedAcc = "0.00";
            String WkRTProvedAcc = "0.00";
            String WkRTInc = "0.00";

            if (jDateChooserDialogActivityDateFrom.getDate() == null) {
                JOptionPane.showMessageDialog(jDialogWk1, "Date cannot be blank. Please check your dates");
                jDateChooserDialogActivityDateFrom.requestFocusInWindow();
                jDateChooserDialogActivityDateFrom.setFocusable(true);
            } else if (jDateChooserDialogActivityDateTo.getDate() == null) {
                JOptionPane.showMessageDialog(jDialogWk1, "Date cannot be blank. Please check your dates");
                jDateChooserDialogActivityDateTo.requestFocusInWindow();
                jDateChooserDialogActivityDateTo.setFocusable(true);
            } else {
                if (!(jTextFieldWk1DialogStaffName1.getText().length() == 0)) {
                    duplicateUser1Count = null;
                    planWk = "PlanWk1Tab";
                    planDate = formatter.format(jDateChooserDialogActivityDateFrom.getDate());
                    empNamNum1 = "EMP_NAM1";
                    empNam1 = jTextFieldWk1DialogStaffName1.getText();
                    empNamNum2 = "EMP_NAM2";
                    empNam2 = jTextFieldWk1DialogStaffName2.getText();
                    empNamNum3 = "EMP_NAM3";
                    empNam3 = jTextFieldWk1DialogStaffName3.getText();
                    empNamNum4 = "EMP_NAM4";
                    empNam4 = jTextFieldWk1DialogStaffName4.getText();

                    CheckDuplicateUser1();
                    CheckDuplicateUser2();
                    CheckDuplicateUser3();
                    CheckDuplicateUser4();
                    CheckDuplicateUser5();
                }
            }

            try {
                if (jCheckBoxDialogWk1BrkFast.isSelected()) {
                    Wk1Brk = breakfastAll;
                } else {
                    Wk1Brk = "0.00";
                }

                if (jCheckBoxDialogWk1Lunch.isSelected()) {
                    Wk1Lnch = lunchAll;
                } else {
                    Wk1Lnch = "0.00";
                }

                if (jCheckBoxDialogWk1Dinner.isSelected()) {
                    Wk1Dinner = dinnerAll;
                } else {
                    Wk1Dinner = "0.00";
                }

                if (jCheckBoxDialogWk1AccUnProved.isSelected()) {
                    Wk1UnProvedAcc = unProvedAll;
                } else {
                    Wk1UnProvedAcc = "0.00";
                }
                if (jCheckBoxDialogWk1AccProved.isSelected()) {
                    Wk1ProvedAcc = provedAll;
                } else {
                    Wk1ProvedAcc = "0.00";
                }

                if (jCheckBoxDialogWk1Inc.isSelected()) {
                    Wk1Inc = incidentalAll;
                } else {
                    Wk1Inc = "0.00";
                }

                if (jCheckBoxDialogWk1Misc.isSelected()) {
                    Wk1MiscDesc = jTextFieldWk1Misc.getText();
                    Wk1Misc = jTextFieldWk1MiscAmt.getText();
                } else {
                    Wk1Misc = "0.00";
                }

                if (jDateChooserDialogActivityDateFrom.getDate() == null) {
                    JOptionPane.showMessageDialog(jDialogWk1, "Date cannot be blank. Please check your dates");
                    jDateChooserDialogActivityDateFrom.requestFocusInWindow();
                    jDateChooserDialogActivityDateFrom.setFocusable(true);
                } else if (jDateChooserDialogActivityDateTo.getDate() == null) {
                    JOptionPane.showMessageDialog(jDialogWk1, "Date cannot be blank. Please check your dates");
                    jDateChooserDialogActivityDateTo.requestFocusInWindow();
                    jDateChooserDialogActivityDateTo.setFocusable(true);
                } else if (jDateChooserDialogActivityDateFrom.getDate().after(jDateChooserDialogActivityDateTo.getDate())) {
                    JOptionPane.showMessageDialog(jDialogWk1, "End Date cannot be lower than start date.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    jDateChooserDialogActivityDateFrom.setDate(null);
                    jDateChooserDialogActivityDateTo.setDate(null);
                    jDateChooserDialogActivityDateFrom.requestFocusInWindow();
                    jDateChooserDialogActivityDateFrom.setFocusable(true);
                } else if (("Y".equals(wk1Site)) && ((formatter.format(jDateChooserDialogActivityDateFrom.getDate()).compareTo(formatter.format(jDateChooserWk1From.getDate())) < 0)
                        || (formatter.format(jDateChooserDialogActivityDateFrom.getDate()).compareTo(formatter.format(jDateChooserWk1To.getDate())) > 0))) {
                    JOptionPane.showMessageDialog(jDialogWk1, "Activity date cannot be outside the specified  date range.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    jDateChooserDialogActivityDateFrom.setDate(null);
                    jDateChooserDialogActivityDateTo.setDate(null);
                    jDateChooserDialogActivityDateFrom.requestFocusInWindow();
                    jDateChooserDialogActivityDateFrom.setFocusable(true);
                } else if (jTextAttDocName.getText().trim().length() > 0) {
                    JOptionPane.showMessageDialog(jDialogWk1, "Please commit attached file by clicking add file. ");
                    jTextAttDocDesc.requestFocusInWindow();
                    jTextAttDocDesc.setFocusable(true);
                } else if (("Y".equals(wk2Site)) && ((formatter.format(jDateChooserDialogActivityDateFrom.getDate()).compareTo(formatter.format(jDateChooserWk2From.getDate())) < 0)
                        || (formatter.format(jDateChooserDialogActivityDateFrom.getDate()).compareTo(formatter.format(jDateChooserWk2To.getDate())) > 0))) {
                    JOptionPane.showMessageDialog(jDialogWk1, "Activity date cannot be outside the specified  date range.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    jDateChooserDialogActivityDateFrom.setDate(null);
                    jDateChooserDialogActivityDateTo.setDate(null);
                    jDateChooserDialogActivityDateFrom.requestFocusInWindow();
                    jDateChooserDialogActivityDateFrom.setFocusable(true);
                } else if (("Y".equals(wk3Site)) && ((formatter.format(jDateChooserDialogActivityDateFrom.getDate()).compareTo(formatter.format(jDateChooserWk3From.getDate())) < 0)
                        || (formatter.format(jDateChooserDialogActivityDateFrom.getDate()).compareTo(formatter.format(jDateChooserWk3To.getDate())) > 0))) {
                    JOptionPane.showMessageDialog(jDialogWk1, "Activity date cannot be outside the specified  date range.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    jDateChooserDialogActivityDateFrom.setDate(null);
                    jDateChooserDialogActivityDateTo.setDate(null);
                    jDateChooserDialogActivityDateFrom.requestFocusInWindow();
                    jDateChooserDialogActivityDateFrom.setFocusable(true);
                } else if (("Y".equals(wk4Site)) && ((formatter.format(jDateChooserDialogActivityDateFrom.getDate()).compareTo(formatter.format(jDateChooserWk4From.getDate())) < 0)
                        || (formatter.format(jDateChooserDialogActivityDateFrom.getDate()).compareTo(formatter.format(jDateChooserWk4To.getDate())) > 0))) {
                    JOptionPane.showMessageDialog(jDialogWk1, "Activity date cannot be outside the specified  date range.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    jDateChooserDialogActivityDateFrom.setDate(null);
                    jDateChooserDialogActivityDateTo.setDate(null);
                    jDateChooserDialogActivityDateFrom.requestFocusInWindow();
                    jDateChooserDialogActivityDateFrom.setFocusable(true);
                } else if (("Y".equals(wk5Site)) && ((formatter.format(jDateChooserDialogActivityDateFrom.getDate()).compareTo(formatter.format(jDateChooserWk5From.getDate())) < 0)
                        || (formatter.format(jDateChooserDialogActivityDateFrom.getDate()).compareTo(formatter.format(jDateChooserWk5To.getDate())) > 0))) {
                    JOptionPane.showMessageDialog(jDialogWk1, "Activity date cannot be outside the specified  date range.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    jDateChooserDialogActivityDateFrom.setDate(null);
                    jDateChooserDialogActivityDateTo.setDate(null);
                    jDateChooserDialogActivityDateFrom.requestFocusInWindow();
                    jDateChooserDialogActivityDateFrom.setFocusable(true);
                } else if (("Y".equals(wk1Site)) && ((formatter.format(jDateChooserDialogActivityDateFrom.getDate()).compareTo(formatter.format(jDateChooserWk1From.getDate())) < 0)
                        || (formatter.format(jDateChooserDialogActivityDateTo.getDate()).compareTo(formatter.format(jDateChooserWk1To.getDate())) > 0))) {
                    JOptionPane.showMessageDialog(jDialogWk1, "Activity date cannot be outside the specified date range.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    jDateChooserDialogActivityDateFrom.setDate(null);
                    jDateChooserDialogActivityDateTo.setDate(null);
                    jDateChooserDialogActivityDateFrom.requestFocusInWindow();
                    jDateChooserDialogActivityDateFrom.setFocusable(true);
                } else if (("Y".equals(wk2Site)) && ((formatter.format(jDateChooserDialogActivityDateFrom.getDate()).compareTo(formatter.format(jDateChooserWk2From.getDate())) < 0)
                        || (formatter.format(jDateChooserDialogActivityDateTo.getDate()).compareTo(formatter.format(jDateChooserWk2To.getDate())) > 0))) {
                    JOptionPane.showMessageDialog(jDialogWk1, "Activity date cannot be outside the specified date range.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    jDateChooserDialogActivityDateFrom.setDate(null);
                    jDateChooserDialogActivityDateTo.setDate(null);
                    jDateChooserDialogActivityDateFrom.requestFocusInWindow();
                    jDateChooserDialogActivityDateFrom.setFocusable(true);
                } else if (("Y".equals(wk3Site)) && ((formatter.format(jDateChooserDialogActivityDateFrom.getDate()).compareTo(formatter.format(jDateChooserWk3From.getDate())) < 0)
                        || (formatter.format(jDateChooserDialogActivityDateTo.getDate()).compareTo(formatter.format(jDateChooserWk3To.getDate())) > 0))) {
                    JOptionPane.showMessageDialog(jDialogWk1, "Activity date cannot be outside the specified date range.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    jDateChooserDialogActivityDateFrom.setDate(null);
                    jDateChooserDialogActivityDateTo.setDate(null);
                    jDateChooserDialogActivityDateFrom.requestFocusInWindow();
                    jDateChooserDialogActivityDateFrom.setFocusable(true);
                } else if (("Y".equals(wk4Site)) && ((formatter.format(jDateChooserDialogActivityDateFrom.getDate()).compareTo(formatter.format(jDateChooserWk4From.getDate())) < 0)
                        || (formatter.format(jDateChooserDialogActivityDateTo.getDate()).compareTo(formatter.format(jDateChooserWk4To.getDate())) > 0))) {
                    JOptionPane.showMessageDialog(jDialogWk1, "Activity date cannot be outside the specified date range.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    jDateChooserDialogActivityDateFrom.setDate(null);
                    jDateChooserDialogActivityDateTo.setDate(null);
                    jDateChooserDialogActivityDateFrom.requestFocusInWindow();
                    jDateChooserDialogActivityDateFrom.setFocusable(true);
                } else if (("Y".equals(wk5Site)) && ((formatter.format(jDateChooserDialogActivityDateFrom.getDate()).compareTo(formatter.format(jDateChooserWk5From.getDate())) < 0)
                        || (formatter.format(jDateChooserDialogActivityDateTo.getDate()).compareTo(formatter.format(jDateChooserWk5To.getDate())) > 0))) {
                    JOptionPane.showMessageDialog(jDialogWk1, "Activity date cannot be outside the specified date range.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    jDateChooserDialogActivityDateFrom.setDate(null);
                    jDateChooserDialogActivityDateTo.setDate(null);
                    jDateChooserDialogActivityDateFrom.requestFocusInWindow();
                    jDateChooserDialogActivityDateFrom.setFocusable(true);
                } else if ("".equals(jTextFieldWk1DialogActivityDesc.getText())) {
                    JOptionPane.showMessageDialog(jDialogWk1, "Activity description cannot be blank. Please check and correct");
                    jTextFieldWk1DialogActivityDesc.requestFocusInWindow();
                    jTextFieldWk1DialogActivityDesc.setFocusable(true);
                } else if ("".equals(jTextAreaWk1DialogJustification.getText()) && jTableDocAtt.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(jDialogWk1, "Activity justification cannot be blank. Please type your justfication or attach your supporting documents or both.");
                    jTextAreaWk1DialogJustification.requestFocusInWindow();
                    jTextAreaWk1DialogJustification.setFocusable(true);
                } else if ((jCheckBoxDialogWk1BrkFast.isSelected() || jCheckBoxDialogWk1Lunch.isSelected()
                        || jCheckBoxDialogWk1Dinner.isSelected() || jCheckBoxDialogWk1AccProved.isSelected()
                        || jCheckBoxDialogWk1Inc.isSelected() || jCheckBoxDialogWk1Misc.isSelected()
                        || jCheckBoxDialogWk1AccUnProved.isSelected() || jCheckBoxNoAcc.isSelected())
                        && ("".equals(jTextFieldWk1DialogStaffName1.getText()))) {
                    JOptionPane.showMessageDialog(jDialogWk1, "You have select an allowance.Please enter at least one staff name");
                    jTextFieldWk1DialogStaffName1.requestFocusInWindow();
                    jTextFieldWk1DialogStaffName1.setFocusable(true);

                } else {

                    DateFormat dfYear = new SimpleDateFormat("yyyy");
                    DateFormat dfMon = new SimpleDateFormat("MM");
                    DateFormat dfDay = new SimpleDateFormat("dd");
                    int yearF = Integer.parseInt(dfYear.format(jDateChooserDialogActivityDateFrom.getDate()));
                    int monF = Integer.parseInt(dfMon.format(jDateChooserDialogActivityDateFrom.getDate()));
                    int dayF = Integer.parseInt(dfDay.format(jDateChooserDialogActivityDateFrom.getDate()));
                    int yearT = Integer.parseInt(dfYear.format(jDateChooserDialogActivityDateTo.getDate()));
                    int monT = Integer.parseInt(dfMon.format(jDateChooserDialogActivityDateTo.getDate()));
                    int dayT = Integer.parseInt(dfDay.format(jDateChooserDialogActivityDateTo.getDate()));
                    LocalDate start = LocalDate.of(yearF, monF, dayF);
                    LocalDate end = LocalDate.of(yearT, monT, dayT).minusDays(1);
                    String filenameDT = departTime.getSelectedTimeDB();     // full file name
                    String[] partsDT = filenameDT.split("\\:"); // String array, each element is text between dots

                    String beforeFirstDotDT = partsDT[0];

                    String filenameRT = returnTime.getSelectedTimeDB();     // full file name
                    String[] partsRT = filenameRT.split("\\:"); // String array, each element is text between dots

                    String beforeFirstDotRT = partsRT[0];

                    if (Integer.parseInt(beforeFirstDotDT) < 7) {
                        WkDTBrk = Wk1Brk;

                    }

                    if (Integer.parseInt(beforeFirstDotDT) < 13) {
                        WkDTLnch = Wk1Lnch;

                    }

                    if (Integer.parseInt(beforeFirstDotRT) >= 14) {
                        WkRTLnch = Wk1Lnch;

                    }

                    if (Integer.parseInt(beforeFirstDotRT) >= 19) {
                        WkRTDinner = Wk1Dinner;

                    }

                    if ((!"D036 CDC-Zim-TTECH".equals(taskDonor))
                            && (!"D032 ZHI".equals(taskDonor))
                            && (!"D022 CDC".equals(taskDonor))) {
                        String donorName = jComboDonor.getSelectedItem().toString();
                        prjProgCodeName = "";
                        prjProgCode = "";

                    }

                    addFileAttToTabWk1Main();

                    if ("Y".equals(wk1Site)) {
                        if (modelWk1.getRowCount() == 0 && formatter.format(jDateChooserDialogActivityDateFrom.getDate()).equals(formatter.format(jDateChooserDialogActivityDateTo.getDate()))) {

                            modelWk1.addRow(new Object[]{formatter.format(jDateChooserDialogActivityDateFrom.getDate()), accCodeName, donorName, prjCodeName,
                                prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), WkDTBrk, WkRTLnch, WkRTDinner, WkRTInc, Wk1MiscDesc, Wk1Misc, WkRTUnProvedAcc,
                                WkRTProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});

                        } else if ((modelWk1.getRowCount() > 0) && !jCheckAddStaff.isSelected()) {
                            LocalDate endWk = LocalDate.of(yearT, monT, dayT);
                            Calendar c = Calendar.getInstance();
                            Date startDate = jDateChooserDialogActivityDateFrom.getDate();
                            for (LocalDate date = start; date.isBefore(endWk); date = date.plusDays(1)) {
                                c.setTime(startDate);

                                modelWk1.addRow(new Object[]{formatter.format(startDate), accCodeName, donorName, prjCodeName,
                                    prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), Wk1Brk, Wk1Lnch, Wk1Dinner, Wk1Inc, Wk1MiscDesc, Wk1Misc, Wk1UnProvedAcc,
                                    Wk1ProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                    jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});
                                c.add(Calendar.DAY_OF_MONTH, 1);
                                startDate = c.getTime();

                            }

                            modelWk1.addRow(new Object[]{formatter.format(startDate), accCodeName, donorName, prjCodeName,
                                prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), Wk1Brk, Wk1Lnch, Wk1Dinner, Wk1Inc, Wk1MiscDesc, Wk1Misc, Wk1UnProvedAcc,
                                Wk1ProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});

                        } else if ((modelWk1.getRowCount() == 0) || jCheckAddStaff.isSelected()) {
                            modelWk1.addRow(new Object[]{formatter.format(jDateChooserDialogActivityDateFrom.getDate()), accCodeName, donorName, prjCodeName,
                                prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), WkDTBrk, WkDTLnch, Wk1Dinner, Wk1Inc, Wk1MiscDesc, Wk1Misc, Wk1UnProvedAcc,
                                Wk1ProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});

                            Calendar c = Calendar.getInstance();
                            Date startDate = jDateChooserDialogActivityDateFrom.getDate();

                            c.setTime(startDate);
                            c.add(Calendar.DATE, 1);
                            startDate = c.getTime();

                            for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
                                c.setTime(startDate);

                                modelWk1.addRow(new Object[]{formatter.format(startDate), accCodeName, donorName, prjCodeName,
                                    prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), Wk1Brk, Wk1Lnch, Wk1Dinner, Wk1Inc, Wk1MiscDesc, Wk1Misc, Wk1UnProvedAcc,
                                    Wk1ProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                    jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});
                                c.add(Calendar.DAY_OF_MONTH, 1);
                                startDate = c.getTime();

                            }

                            modelWk1.addRow(new Object[]{formatter.format(jDateChooserDialogActivityDateTo.getDate()), accCodeName, donorName, prjCodeName,
                                prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), Wk1Brk, WkRTLnch, WkRTDinner, WkRTInc, Wk1MiscDesc, Wk1Misc, WkRTUnProvedAcc,
                                WkRTProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});
                        }
                        addItem();
                        resetField();

                    }

                    if ("Y".equals(wk2Site)) {
                        if (modelWk2.getRowCount() == 0 && formatter.format(jDateChooserDialogActivityDateFrom.getDate()).equals(formatter.format(jDateChooserDialogActivityDateTo.getDate()))) {

                            modelWk2.addRow(new Object[]{formatter.format(jDateChooserDialogActivityDateFrom.getDate()), accCodeName, donorName, prjCodeName,
                                prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), WkDTBrk, WkRTLnch, WkRTDinner, WkRTInc, Wk1MiscDesc, Wk1Misc, WkRTUnProvedAcc,
                                WkRTProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});

                        } else if ((modelWk2.getRowCount() > 0) && !jCheckAddStaff.isSelected()) {
                            LocalDate endWk = LocalDate.of(yearT, monT, dayT);
                            Calendar c = Calendar.getInstance();
                            Date startDate = jDateChooserDialogActivityDateFrom.getDate();
                            for (LocalDate date = start; date.isBefore(endWk); date = date.plusDays(1)) {
                                c.setTime(startDate);

                                modelWk2.addRow(new Object[]{formatter.format(startDate), accCodeName, donorName, prjCodeName,
                                    prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), Wk1Brk, Wk1Lnch, Wk1Dinner, Wk1Inc, Wk1MiscDesc, Wk1Misc, Wk1UnProvedAcc,
                                    Wk1ProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                    jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});
                                c.add(Calendar.DAY_OF_MONTH, 1);
                                startDate = c.getTime();

                            }

                            modelWk2.addRow(new Object[]{formatter.format(startDate), accCodeName, donorName, prjCodeName,
                                prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), Wk1Brk, Wk1Lnch, Wk1Dinner, Wk1Inc, Wk1MiscDesc, Wk1Misc, Wk1UnProvedAcc,
                                Wk1ProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});

                        } else if ((modelWk2.getRowCount() == 0) || jCheckAddStaff.isSelected()) {
                            modelWk2.addRow(new Object[]{formatter.format(jDateChooserDialogActivityDateFrom.getDate()), accCodeName, donorName, prjCodeName,
                                prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), WkDTBrk, WkDTLnch, Wk1Dinner, Wk1Inc, Wk1MiscDesc, Wk1Misc, Wk1UnProvedAcc,
                                Wk1ProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});

                            Calendar c = Calendar.getInstance();
                            Date startDate = jDateChooserDialogActivityDateFrom.getDate();

                            c.setTime(startDate);
                            c.add(Calendar.DATE, 1);
                            startDate = c.getTime();

                            for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
                                c.setTime(startDate);

                                modelWk2.addRow(new Object[]{formatter.format(startDate), accCodeName, donorName, prjCodeName,
                                    prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), Wk1Brk, Wk1Lnch, Wk1Dinner, Wk1Inc, Wk1MiscDesc, Wk1Misc, Wk1UnProvedAcc,
                                    Wk1ProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                    jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});
                                c.add(Calendar.DAY_OF_MONTH, 1);
                                startDate = c.getTime();

                            }

                            modelWk2.addRow(new Object[]{formatter.format(jDateChooserDialogActivityDateTo.getDate()), accCodeName, donorName, prjCodeName,
                                prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), Wk1Brk, WkRTLnch, WkRTDinner, WkRTInc, Wk1MiscDesc, Wk1Misc, WkRTUnProvedAcc,
                                WkRTProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});
                        }
                        addItem();
                        resetField();

                    }

                    if ("Y".equals(wk3Site)) {
                        if (modelWk3.getRowCount() == 0 && formatter.format(jDateChooserDialogActivityDateFrom.getDate()).equals(formatter.format(jDateChooserDialogActivityDateTo.getDate()))) {

                            modelWk3.addRow(new Object[]{formatter.format(jDateChooserDialogActivityDateFrom.getDate()), accCodeName, donorName, prjCodeName,
                                prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), WkDTBrk, WkRTLnch, WkRTDinner, WkRTInc, Wk1MiscDesc, Wk1Misc, WkRTUnProvedAcc,
                                WkRTProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});

                        } else if ((modelWk3.getRowCount() > 0) && !jCheckAddStaff.isSelected()) {
                            LocalDate endWk = LocalDate.of(yearT, monT, dayT);
                            Calendar c = Calendar.getInstance();
                            Date startDate = jDateChooserDialogActivityDateFrom.getDate();
                            for (LocalDate date = start; date.isBefore(endWk); date = date.plusDays(1)) {
                                c.setTime(startDate);

                                modelWk3.addRow(new Object[]{formatter.format(startDate), accCodeName, donorName, prjCodeName,
                                    prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), Wk1Brk, Wk1Lnch, Wk1Dinner, Wk1Inc, Wk1MiscDesc, Wk1Misc, Wk1UnProvedAcc,
                                    Wk1ProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                    jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});
                                c.add(Calendar.DAY_OF_MONTH, 1);
                                startDate = c.getTime();

                            }

                            modelWk3.addRow(new Object[]{formatter.format(startDate), accCodeName, donorName, prjCodeName,
                                prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), Wk1Brk, Wk1Lnch, Wk1Dinner, Wk1Inc, Wk1MiscDesc, Wk1Misc, Wk1UnProvedAcc,
                                Wk1ProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});

                        } else if ((modelWk3.getRowCount() == 0) || jCheckAddStaff.isSelected()) {
                            modelWk3.addRow(new Object[]{formatter.format(jDateChooserDialogActivityDateFrom.getDate()), accCodeName, donorName, prjCodeName,
                                prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), WkDTBrk, WkDTLnch, Wk1Dinner, Wk1Inc, Wk1MiscDesc, Wk1Misc, Wk1UnProvedAcc,
                                Wk1ProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});

                            Calendar c = Calendar.getInstance();
                            Date startDate = jDateChooserDialogActivityDateFrom.getDate();

                            c.setTime(startDate);
                            c.add(Calendar.DATE, 1);
                            startDate = c.getTime();

                            for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
                                c.setTime(startDate);

                                modelWk3.addRow(new Object[]{formatter.format(startDate), accCodeName, donorName, prjCodeName,
                                    prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), Wk1Brk, Wk1Lnch, Wk1Dinner, Wk1Inc, Wk1MiscDesc, Wk1Misc, Wk1UnProvedAcc,
                                    Wk1ProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                    jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});
                                c.add(Calendar.DAY_OF_MONTH, 1);
                                startDate = c.getTime();

                            }

                            modelWk3.addRow(new Object[]{formatter.format(jDateChooserDialogActivityDateTo.getDate()), accCodeName, donorName, prjCodeName,
                                prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), Wk1Brk, WkRTLnch, WkRTDinner, WkRTInc, Wk1MiscDesc, Wk1Misc, WkRTUnProvedAcc,
                                WkRTProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});
                        }
                        addItem();
                        resetField();

                    }

                    if ("Y".equals(wk4Site)) {
                        if (modelWk4.getRowCount() == 0 && formatter.format(jDateChooserDialogActivityDateFrom.getDate()).equals(formatter.format(jDateChooserDialogActivityDateTo.getDate()))) {

                            modelWk4.addRow(new Object[]{formatter.format(jDateChooserDialogActivityDateFrom.getDate()), accCodeName, donorName, prjCodeName,
                                prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), WkDTBrk, WkRTLnch, WkRTDinner, WkRTInc, Wk1MiscDesc, Wk1Misc, WkRTUnProvedAcc,
                                WkRTProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});

                        } else if ((modelWk4.getRowCount() > 0) && !jCheckAddStaff.isSelected()) {
                            LocalDate endWk = LocalDate.of(yearT, monT, dayT);
                            Calendar c = Calendar.getInstance();
                            Date startDate = jDateChooserDialogActivityDateFrom.getDate();
                            for (LocalDate date = start; date.isBefore(endWk); date = date.plusDays(1)) {
                                c.setTime(startDate);

                                modelWk4.addRow(new Object[]{formatter.format(startDate), accCodeName, donorName, prjCodeName,
                                    prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), Wk1Brk, Wk1Lnch, Wk1Dinner, Wk1Inc, Wk1MiscDesc, Wk1Misc, Wk1UnProvedAcc,
                                    Wk1ProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                    jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});
                                c.add(Calendar.DAY_OF_MONTH, 1);
                                startDate = c.getTime();

                            }

                            modelWk4.addRow(new Object[]{formatter.format(startDate), accCodeName, donorName, prjCodeName,
                                prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), Wk1Brk, Wk1Lnch, Wk1Dinner, Wk1Inc, Wk1MiscDesc, Wk1Misc, Wk1UnProvedAcc,
                                Wk1ProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});

                        } else if ((modelWk4.getRowCount() == 0) || jCheckAddStaff.isSelected()) {
                            modelWk4.addRow(new Object[]{formatter.format(jDateChooserDialogActivityDateFrom.getDate()), accCodeName, donorName, prjCodeName,
                                prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), WkDTBrk, WkDTLnch, Wk1Dinner, Wk1Inc, Wk1MiscDesc, Wk1Misc, Wk1UnProvedAcc,
                                Wk1ProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});

                            Calendar c = Calendar.getInstance();
                            Date startDate = jDateChooserDialogActivityDateFrom.getDate();

                            c.setTime(startDate);
                            c.add(Calendar.DATE, 1);
                            startDate = c.getTime();

                            for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
                                c.setTime(startDate);

                                modelWk4.addRow(new Object[]{formatter.format(startDate), accCodeName, donorName, prjCodeName,
                                    prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), Wk1Brk, Wk1Lnch, Wk1Dinner, Wk1Inc, Wk1MiscDesc, Wk1Misc, Wk1UnProvedAcc,
                                    Wk1ProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                    jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});
                                c.add(Calendar.DAY_OF_MONTH, 1);
                                startDate = c.getTime();

                            }

                            modelWk4.addRow(new Object[]{formatter.format(jDateChooserDialogActivityDateTo.getDate()), accCodeName, donorName, prjCodeName,
                                prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), Wk1Brk, WkRTLnch, WkRTDinner, WkRTInc, Wk1MiscDesc, Wk1Misc, WkRTUnProvedAcc,
                                WkRTProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});
                        }
                        addItem();
                        resetField();

                    }

                    if ("Y".equals(wk5Site)) {
                        if (modelWk5.getRowCount() == 0 && formatter.format(jDateChooserDialogActivityDateFrom.getDate()).equals(formatter.format(jDateChooserDialogActivityDateTo.getDate()))) {

                            modelWk5.addRow(new Object[]{formatter.format(jDateChooserDialogActivityDateFrom.getDate()), accCodeName, donorName, prjCodeName,
                                prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), WkDTBrk, WkRTLnch, WkRTDinner, WkRTInc, Wk1MiscDesc, Wk1Misc, WkRTUnProvedAcc,
                                WkRTProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});

                        } else if ((modelWk5.getRowCount() > 0) && !jCheckAddStaff.isSelected()) {
                            LocalDate endWk = LocalDate.of(yearT, monT, dayT);
                            Calendar c = Calendar.getInstance();
                            Date startDate = jDateChooserDialogActivityDateFrom.getDate();
                            for (LocalDate date = start; date.isBefore(endWk); date = date.plusDays(1)) {
                                c.setTime(startDate);

                                modelWk5.addRow(new Object[]{formatter.format(startDate), accCodeName, donorName, prjCodeName,
                                    prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), Wk1Brk, Wk1Lnch, Wk1Dinner, Wk1Inc, Wk1MiscDesc, Wk1Misc, Wk1UnProvedAcc,
                                    Wk1ProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                    jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});
                                c.add(Calendar.DAY_OF_MONTH, 1);
                                startDate = c.getTime();

                            }

                            modelWk5.addRow(new Object[]{formatter.format(startDate), accCodeName, donorName, prjCodeName,
                                prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), Wk1Brk, Wk1Lnch, Wk1Dinner, Wk1Inc, Wk1MiscDesc, Wk1Misc, Wk1UnProvedAcc,
                                Wk1ProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});

                        } else if ((modelWk5.getRowCount() == 0) || jCheckAddStaff.isSelected()) {
                            modelWk5.addRow(new Object[]{formatter.format(jDateChooserDialogActivityDateFrom.getDate()), accCodeName, donorName, prjCodeName,
                                prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), WkDTBrk, WkDTLnch, Wk1Dinner, Wk1Inc, Wk1MiscDesc, Wk1Misc, Wk1UnProvedAcc,
                                Wk1ProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});

                            Calendar c = Calendar.getInstance();
                            Date startDate = jDateChooserDialogActivityDateFrom.getDate();

                            c.setTime(startDate);
                            c.add(Calendar.DATE, 1);
                            startDate = c.getTime();

                            for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
                                c.setTime(startDate);

                                modelWk5.addRow(new Object[]{formatter.format(startDate), accCodeName, donorName, prjCodeName,
                                    prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), Wk1Brk, Wk1Lnch, Wk1Dinner, Wk1Inc, Wk1MiscDesc, Wk1Misc, Wk1UnProvedAcc,
                                    Wk1ProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                    jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});
                                c.add(Calendar.DAY_OF_MONTH, 1);
                                startDate = c.getTime();

                            }

                            modelWk5.addRow(new Object[]{formatter.format(jDateChooserDialogActivityDateTo.getDate()), accCodeName, donorName, prjCodeName,
                                prjProgCode, prjProgCodeName, budLineName, budcode, jTextFieldDialogWkSite.getText(), jTextFieldWk1DialogActivityDesc.getText(), jTextAreaWk1DialogJustification.getText(), Wk1Brk, WkRTLnch, WkRTDinner, WkRTInc, Wk1MiscDesc, Wk1Misc, WkRTUnProvedAcc,
                                WkRTProvedAcc, jTextFieldWk1DialogStaffName1.getText(),
                                jTextFieldWk1DialogStaffName2.getText(), jTextFieldWk1DialogStaffName3.getText(), jTextFieldWk1DialogStaffName4.getText()});
                        }
                        addItem();
                        resetField();

                    }

                }

            } catch (Exception e) {
                System.out.println(e);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    void createWk2Plan() {
        itmNum = 1;
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {

                String sqlwk2plan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] "
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlwk2plan);
                pst1.setString(1, "P");
                pst1.setString(2, jLabelRefNum.getText());
                pst1.setString(3, Integer.toString(itmNum));
                pst1.setString(4, jTableWk2Activities.getValueAt(i, 0).toString());
                pst1.setString(5, jTableWk2Activities.getValueAt(i, 1).toString());
                pst1.setString(6, jTableWk2Activities.getValueAt(i, 2).toString());
                pst1.setString(7, jTableWk2Activities.getValueAt(i, 3).toString());
                pst1.setString(8, jTableWk2Activities.getValueAt(i, 4).toString());
                pst1.setString(9, jTableWk2Activities.getValueAt(i, 5).toString());
                pst1.setString(10, jTableWk2Activities.getValueAt(i, 6).toString());
                pst1.setString(11, jTableWk2Activities.getValueAt(i, 7).toString());
                pst1.setString(12, jTableWk2Activities.getValueAt(i, 8).toString());
                pst1.setString(13, jTableWk2Activities.getValueAt(i, 9).toString());
                pst1.setString(14, jTableWk2Activities.getValueAt(i, 10).toString());
                pst1.setString(15, String.valueOf(jTableWk2Activities.getValueAt(i, 11).toString()));
                pst1.setString(16, String.valueOf(jTableWk2Activities.getValueAt(i, 12).toString()));
                pst1.setString(17, String.valueOf(jTableWk2Activities.getValueAt(i, 13).toString()));
                pst1.setString(18, String.valueOf(jTableWk2Activities.getValueAt(i, 14).toString()));
                pst1.setString(19, jTableWk2Activities.getValueAt(i, 15).toString());
                pst1.setString(20, String.valueOf(jTableWk2Activities.getValueAt(i, 16).toString()));
                pst1.setString(21, String.valueOf(jTableWk2Activities.getValueAt(i, 17).toString()));
                pst1.setString(22, String.valueOf(jTableWk2Activities.getValueAt(i, 18).toString()));
                pst1.setString(23, jTableWk2Activities.getValueAt(i, 19).toString());
                pst1.setString(24, jTableWk2Activities.getValueAt(i, 20).toString());
                pst1.setString(25, jTableWk2Activities.getValueAt(i, 21).toString());
                pst1.setString(26, jTableWk2Activities.getValueAt(i, 22).toString());
                pst1.setString(27, "1");
                pst1.setString(28, "1");
                pst1.setString(29, "A");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void createWk3Plan() {
        itmNum = 1;
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {

                String sqlwk3plan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] "
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlwk3plan);
                pst1.setString(1, "P");
                pst1.setString(2, jLabelRefNum.getText());
                pst1.setString(3, Integer.toString(itmNum));
                pst1.setString(4, jTableWk3Activities.getValueAt(i, 0).toString());
                pst1.setString(5, jTableWk3Activities.getValueAt(i, 1).toString());
                pst1.setString(6, jTableWk3Activities.getValueAt(i, 2).toString());
                pst1.setString(7, jTableWk3Activities.getValueAt(i, 3).toString());
                pst1.setString(8, jTableWk3Activities.getValueAt(i, 4).toString());
                pst1.setString(9, jTableWk3Activities.getValueAt(i, 5).toString());
                pst1.setString(10, jTableWk3Activities.getValueAt(i, 6).toString());
                pst1.setString(11, jTableWk3Activities.getValueAt(i, 7).toString());
                pst1.setString(12, jTableWk3Activities.getValueAt(i, 8).toString());
                pst1.setString(13, jTableWk3Activities.getValueAt(i, 9).toString());
                pst1.setString(14, jTableWk3Activities.getValueAt(i, 10).toString());
                pst1.setString(15, String.valueOf(jTableWk3Activities.getValueAt(i, 11).toString()));
                pst1.setString(16, String.valueOf(jTableWk3Activities.getValueAt(i, 12).toString()));
                pst1.setString(17, String.valueOf(jTableWk3Activities.getValueAt(i, 13).toString()));
                pst1.setString(18, String.valueOf(jTableWk3Activities.getValueAt(i, 14).toString()));
                pst1.setString(19, jTableWk3Activities.getValueAt(i, 15).toString());
                pst1.setString(20, String.valueOf(jTableWk3Activities.getValueAt(i, 16).toString()));
                pst1.setString(21, String.valueOf(jTableWk3Activities.getValueAt(i, 17).toString()));
                pst1.setString(22, String.valueOf(jTableWk3Activities.getValueAt(i, 18).toString()));
                pst1.setString(23, jTableWk3Activities.getValueAt(i, 19).toString());
                pst1.setString(24, jTableWk3Activities.getValueAt(i, 20).toString());
                pst1.setString(25, jTableWk3Activities.getValueAt(i, 21).toString());
                pst1.setString(26, jTableWk3Activities.getValueAt(i, 22).toString());
                pst1.setString(27, "1");
                pst1.setString(28, "1");
                pst1.setString(29, "A");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void createWk4Plan() {
        itmNum = 1;
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            for (int i = 0; i < jTableWk4Activities.getRowCount(); i++) {

                String sqlwk4plan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab] "
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlwk4plan);
                pst1.setString(1, "P");
                pst1.setString(2, jLabelRefNum.getText());
                pst1.setString(3, Integer.toString(itmNum));
                pst1.setString(4, jTableWk4Activities.getValueAt(i, 0).toString());
                pst1.setString(5, jTableWk4Activities.getValueAt(i, 1).toString());
                pst1.setString(6, jTableWk4Activities.getValueAt(i, 2).toString());
                pst1.setString(7, jTableWk4Activities.getValueAt(i, 3).toString());
                pst1.setString(8, jTableWk4Activities.getValueAt(i, 4).toString());
                pst1.setString(9, jTableWk4Activities.getValueAt(i, 5).toString());
                pst1.setString(10, jTableWk4Activities.getValueAt(i, 6).toString());
                pst1.setString(11, jTableWk4Activities.getValueAt(i, 7).toString());
                pst1.setString(12, jTableWk4Activities.getValueAt(i, 8).toString());
                pst1.setString(13, jTableWk4Activities.getValueAt(i, 9).toString());
                pst1.setString(14, jTableWk4Activities.getValueAt(i, 10).toString());
                pst1.setString(15, String.valueOf(jTableWk4Activities.getValueAt(i, 11).toString()));
                pst1.setString(16, String.valueOf(jTableWk4Activities.getValueAt(i, 12).toString()));
                pst1.setString(17, String.valueOf(jTableWk4Activities.getValueAt(i, 13).toString()));
                pst1.setString(18, String.valueOf(jTableWk4Activities.getValueAt(i, 14).toString()));
                pst1.setString(19, jTableWk4Activities.getValueAt(i, 15).toString());
                pst1.setString(20, String.valueOf(jTableWk4Activities.getValueAt(i, 16).toString()));
                pst1.setString(21, String.valueOf(jTableWk4Activities.getValueAt(i, 17).toString()));
                pst1.setString(22, String.valueOf(jTableWk4Activities.getValueAt(i, 18).toString()));
                pst1.setString(23, jTableWk4Activities.getValueAt(i, 19).toString());
                pst1.setString(24, jTableWk4Activities.getValueAt(i, 20).toString());
                pst1.setString(25, jTableWk4Activities.getValueAt(i, 21).toString());
                pst1.setString(26, jTableWk4Activities.getValueAt(i, 22).toString());
                pst1.setString(27, "1");
                pst1.setString(28, "1");
                pst1.setString(29, "A");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void createWk5Plan() {
        itmNum = 1;
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            for (int i = 0; i < jTableWk5Activities.getRowCount(); i++) {

                String sqlwk5plan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab] "
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlwk5plan);
                pst1.setString(1, "P");
                pst1.setString(2, jLabelRefNum.getText());
                pst1.setString(3, Integer.toString(itmNum));
                pst1.setString(4, jTableWk5Activities.getValueAt(i, 0).toString());
                pst1.setString(5, jTableWk5Activities.getValueAt(i, 1).toString());
                pst1.setString(6, jTableWk5Activities.getValueAt(i, 2).toString());
                pst1.setString(7, jTableWk5Activities.getValueAt(i, 3).toString());
                pst1.setString(8, jTableWk5Activities.getValueAt(i, 4).toString());
                pst1.setString(9, jTableWk5Activities.getValueAt(i, 5).toString());
                pst1.setString(10, jTableWk5Activities.getValueAt(i, 6).toString());
                pst1.setString(11, jTableWk5Activities.getValueAt(i, 7).toString());
                pst1.setString(12, jTableWk5Activities.getValueAt(i, 8).toString());
                pst1.setString(13, jTableWk5Activities.getValueAt(i, 9).toString());
                pst1.setString(14, jTableWk5Activities.getValueAt(i, 10).toString());
                pst1.setString(15, String.valueOf(jTableWk5Activities.getValueAt(i, 11).toString()));
                pst1.setString(16, String.valueOf(jTableWk5Activities.getValueAt(i, 12).toString()));
                pst1.setString(17, String.valueOf(jTableWk5Activities.getValueAt(i, 13).toString()));
                pst1.setString(18, String.valueOf(jTableWk5Activities.getValueAt(i, 14).toString()));
                pst1.setString(19, jTableWk5Activities.getValueAt(i, 15).toString());
                pst1.setString(20, String.valueOf(jTableWk5Activities.getValueAt(i, 16).toString()));
                pst1.setString(21, String.valueOf(jTableWk5Activities.getValueAt(i, 17).toString()));
                pst1.setString(22, String.valueOf(jTableWk5Activities.getValueAt(i, 18).toString()));
                pst1.setString(23, jTableWk5Activities.getValueAt(i, 19).toString());
                pst1.setString(24, jTableWk5Activities.getValueAt(i, 20).toString());
                pst1.setString(25, jTableWk5Activities.getValueAt(i, 21).toString());
                pst1.setString(26, jTableWk5Activities.getValueAt(i, 22).toString());
                pst1.setString(27, "1");
                pst1.setString(28, "1");
                pst1.setString(29, "A");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void createWkPlanCreator() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlWkplancreator = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanCreatorTab] "
                    + "(SERIAL,PLAN_REF_NUM,CREATED_ON,PROVINCE,DISTRICT,CREATOR) "
                    + " VALUES (?,?,?,?,?,?)";

            pst1 = conn.prepareStatement(sqlWkplancreator);
            pst1.setString(1, "P");
            pst1.setString(2, jLabelRefNum.getText());
            pst1.setString(3, jLabelLineDate.getText());
            pst1.setString(4, jLabelProvince.getText());
            pst1.setString(5, jLabelDistrict.getText());
            pst1.setString(6, jLabelLineLogNam.getText());

            pst1.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void createWkPlanPeriod() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            if ((jTableWk1Activities.getRowCount() > 0)
                    && jTableWk2Activities.getRowCount() == 0
                    && jTableWk3Activities.getRowCount() == 0
                    && jTableWk4Activities.getRowCount() == 0
                    && jTableWk5Activities.getRowCount() == 0) {

                String sqlWkplanperiod = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] "
                        + "(SERIAL,PLAN_REF_NUM,PROVINCE,DISTRICT,Wk1_PLAN_START_DATE,Wk1_PLAN_END_DATE,"
                        + "ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWkplanperiod);
                pst1.setString(1, "P");
                pst1.setString(2, jLabelRefNum.getText());
                pst1.setString(3, jLabelProvince.getText());
                pst1.setString(4, jLabelDistrict.getText());
                pst1.setString(5, formatter.format(jDateChooserWk1From.getDate()));
                pst1.setString(6, formatter.format(jDateChooserWk1To.getDate()));
                pst1.setString(7, "1");
                pst1.setString(8, "1");
                pst1.setString(9, "A");

                pst1.executeUpdate();

            } else if ((jTableWk2Activities.getRowCount() > 0)
                    && jTableWk3Activities.getRowCount() == 0
                    && jTableWk4Activities.getRowCount() == 0
                    && jTableWk5Activities.getRowCount() == 0) {

                String sqlWkplanperiod = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] "
                        + "(SERIAL,PLAN_REF_NUM,PROVINCE,DISTRICT,Wk1_PLAN_START_DATE,Wk1_PLAN_END_DATE,"
                        + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,"
                        + "ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWkplanperiod);
                pst1.setString(1, "P");
                pst1.setString(2, jLabelRefNum.getText());
                pst1.setString(3, jLabelProvince.getText());
                pst1.setString(4, jLabelDistrict.getText());
                pst1.setString(5, formatter.format(jDateChooserWk1From.getDate()));
                pst1.setString(6, formatter.format(jDateChooserWk1To.getDate()));
                pst1.setString(7, formatter.format(jDateChooserWk2From.getDate()));
                pst1.setString(8, formatter.format(jDateChooserWk2To.getDate()));
                pst1.setString(9, "1");
                pst1.setString(10, "1");
                pst1.setString(11, "A");

                pst1.executeUpdate();
            } else if ((jTableWk3Activities.getRowCount() > 0)
                    && jTableWk4Activities.getRowCount() == 0
                    && jTableWk5Activities.getRowCount() == 0) {

                String sqlWkplanperiod = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] "
                        + "(SERIAL,PLAN_REF_NUM,PROVINCE,DISTRICT,Wk1_PLAN_START_DATE,Wk1_PLAN_END_DATE,"
                        + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,Wk3_PLAN_START_DATE,Wk3_PLAN_END_DATE,"
                        + "ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWkplanperiod);
                pst1.setString(1, "P");
                pst1.setString(2, jLabelRefNum.getText());
                pst1.setString(3, jLabelProvince.getText());
                pst1.setString(4, jLabelDistrict.getText());
                pst1.setString(5, formatter.format(jDateChooserWk1From.getDate()));
                pst1.setString(6, formatter.format(jDateChooserWk1To.getDate()));
                pst1.setString(7, formatter.format(jDateChooserWk2From.getDate()));
                pst1.setString(8, formatter.format(jDateChooserWk2To.getDate()));
                pst1.setString(9, formatter.format(jDateChooserWk3From.getDate()));
                pst1.setString(10, formatter.format(jDateChooserWk3To.getDate()));
                pst1.setString(11, "1");
                pst1.setString(12, "1");
                pst1.setString(13, "A");

                pst1.executeUpdate();
            } else if ((jTableWk4Activities.getRowCount() > 0)
                    && jTableWk5Activities.getRowCount() == 0) {

                String sqlWkplanperiod = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] "
                        + "(SERIAL,PLAN_REF_NUM,PROVINCE,DISTRICT,Wk1_PLAN_START_DATE,Wk1_PLAN_END_DATE,"
                        + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,Wk3_PLAN_START_DATE,Wk3_PLAN_END_DATE,Wk4_PLAN_START_DATE,Wk4_PLAN_END_DATE,"
                        + "ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWkplanperiod);
                pst1.setString(1, "P");
                pst1.setString(2, jLabelRefNum.getText());
                pst1.setString(3, jLabelProvince.getText());
                pst1.setString(4, jLabelDistrict.getText());
                pst1.setString(5, formatter.format(jDateChooserWk1From.getDate()));
                pst1.setString(6, formatter.format(jDateChooserWk1To.getDate()));
                pst1.setString(7, formatter.format(jDateChooserWk2From.getDate()));
                pst1.setString(8, formatter.format(jDateChooserWk2To.getDate()));
                pst1.setString(9, formatter.format(jDateChooserWk3From.getDate()));
                pst1.setString(10, formatter.format(jDateChooserWk3To.getDate()));
                pst1.setString(11, formatter.format(jDateChooserWk4From.getDate()));
                pst1.setString(12, formatter.format(jDateChooserWk4To.getDate()));
                pst1.setString(13, "1");
                pst1.setString(14, "1");
                pst1.setString(15, "A");

                pst1.executeUpdate();
            } else if (jTableWk5Activities.getRowCount() > 0) {

                String sqlWkplanperiod = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] "
                        + "(SERIAL,PLAN_REF_NUM,PROVINCE,DISTRICT,Wk1_PLAN_START_DATE,Wk1_PLAN_END_DATE,"
                        + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,Wk3_PLAN_START_DATE,Wk3_PLAN_END_DATE,Wk4_PLAN_START_DATE,Wk4_PLAN_END_DATE,Wk5_PLAN_START_DATE,Wk5_PLAN_END_DATE,"
                        + "ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWkplanperiod);
                pst1.setString(1, "P");
                pst1.setString(2, jLabelRefNum.getText());
                pst1.setString(3, jLabelProvince.getText());
                pst1.setString(4, jLabelDistrict.getText());
                pst1.setString(5, formatter.format(jDateChooserWk1From.getDate()));
                pst1.setString(6, formatter.format(jDateChooserWk1To.getDate()));
                pst1.setString(7, formatter.format(jDateChooserWk2From.getDate()));
                pst1.setString(8, formatter.format(jDateChooserWk2To.getDate()));
                pst1.setString(9, formatter.format(jDateChooserWk3From.getDate()));
                pst1.setString(10, formatter.format(jDateChooserWk3To.getDate()));
                pst1.setString(11, formatter.format(jDateChooserWk4From.getDate()));
                pst1.setString(12, formatter.format(jDateChooserWk4To.getDate()));
                pst1.setString(13, formatter.format(jDateChooserWk5From.getDate()));
                pst1.setString(14, formatter.format(jDateChooserWk5To.getDate()));
                pst1.setString(15, "1");
                pst1.setString(16, "1");
                pst1.setString(17, "A");

                pst1.executeUpdate();

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void createWkPlanAction() {
        try {
            findProvMan();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlWkplanperiod = "insert into [ClaimsAppSysZvandiri].[dbo].[PlanActTab] "
                    + "(SERIAL,PLAN_REF_NUM,USR_ACTION,DOC_STATUS,"
                    + "ACTIONED_BY_EMP_NUM,ACTIONED_BY_NAM,SEND_TO_EMP_NUM,SEND_TO_NAM,"
                    + "ACTIONED_ON_DATE,ACTIONED_ON_TIME,"
                    + "ACTIONED_ON_COMPUTER,ACT_VER,DOC_VER,ACT_REC_STA)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            pst1 = conn.prepareStatement(sqlWkplanperiod);
            pst1.setString(1, "P");
            pst1.setString(2, jLabelRefNum.getText());
            pst1.setString(3, "Plan - Created");
            pst1.setString(4, "Created");
            pst1.setString(5, jLabelEmp.getText());
            pst1.setString(6, jLabelLineLogNam.getText());
            pst1.setString(7, supEmpNum);
            pst1.setString(8, supNam);
            pst1.setString(9, jLabelLineDate.getText());
            pst1.setString(10, jLabelLineTime.getText());
            pst1.setString(11, hostName);
            pst1.setString(12, "1");
            pst1.setString(13, "1");
            pst1.setString(14, "A");

            pst1.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void getRegDet() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();
            Statement st3 = conn.createStatement();
            Statement st4 = conn.createStatement();
            Statement st5 = conn.createStatement();
            Statement st6 = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk1tab]  "
                    + "where PLAN_REF_NUM ='" + newPlanRefNum + "'");

            while (r.next()) {

                countWk1 = r.getInt(1);

            }

            ResultSet r1 = st1.executeQuery("SELECT count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab]  "
                    + "where PLAN_REF_NUM ='" + newPlanRefNum + "'");

            while (r1.next()) {

                countWk2 = r1.getInt(1);

            }

            ResultSet r2 = st2.executeQuery("SELECT count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab]  "
                    + "where PLAN_REF_NUM ='" + newPlanRefNum + "'");

            while (r2.next()) {

                countWk3 = r2.getInt(1);

            }

            ResultSet r3 = st3.executeQuery("SELECT count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab]  "
                    + "where PLAN_REF_NUM ='" + newPlanRefNum + "'");

            while (r3.next()) {

                countWk4 = r3.getInt(1);

            }

            ResultSet r4 = st4.executeQuery("SELECT count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab]  "
                    + "where PLAN_REF_NUM ='" + newPlanRefNum + "'");

            while (r4.next()) {

                countWk5 = r4.getInt(1);

            }

            ResultSet r5 = st5.executeQuery("SELECT count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab]  "
                    + "where PLAN_REF_NUM ='" + newPlanRefNum + "'");

            while (r5.next()) {

                countPlanPeriod = r5.getInt(1);

            }

            ResultSet r6 = st6.executeQuery("SELECT count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[PlanActTab]  "
                    + "where PLAN_REF_NUM ='" + newPlanRefNum + "'");

            while (r6.next()) {

                countPlanAct = r6.getInt(1);

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void regRB() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlDelWK1 = "delete [ClaimsAppSysZvandiri].[dbo].[PlanWk1tab] where PLAN_REF_NUM ='" + newPlanRefNum + "'";

            pst = conn.prepareStatement(sqlDelWK1);
            pst.executeUpdate();

            String sqlDelWK2 = "delete [ClaimsAppSysZvandiri].[dbo].[PlanWk2tab] where PLAN_REF_NUM ='" + newPlanRefNum + "'";

            pst = conn.prepareStatement(sqlDelWK2);
            pst.executeUpdate();

            String sqlDelWK3 = "delete [ClaimsAppSysZvandiri].[dbo].[PlanWk3tab] where PLAN_REF_NUM ='" + newPlanRefNum + "'";

            pst = conn.prepareStatement(sqlDelWK3);
            pst.executeUpdate();

            String sqlDelWK4 = "delete [ClaimsAppSysZvandiri].[dbo].[PlanWk4tab] where PLAN_REF_NUM ='" + newPlanRefNum + "'";

            pst = conn.prepareStatement(sqlDelWK4);
            pst.executeUpdate();

            String sqlDelWK5 = "delete [ClaimsAppSysZvandiri].[dbo].[PlanWk5tab] where PLAN_REF_NUM ='" + newPlanRefNum + "'";

            pst = conn.prepareStatement(sqlDelWK5);
            pst.executeUpdate();

            String sqlDelPlanAct = "delete [ClaimsAppSysZvandiri].[dbo].[PlanActTab] where PLAN_REF_NUM ='" + newPlanRefNum + "'";

            pst = conn.prepareStatement(sqlDelPlanAct);
            pst.executeUpdate();

            String sqlDelPlanPeriod = "delete [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] where PLAN_REF_NUM ='" + newPlanRefNum + "'";

            pst = conn.prepareStatement(sqlDelPlanPeriod);
            pst.executeUpdate();

            String sqlDelPlanUsrRec = "delete [ClaimsAppSysZvandiri].[dbo].[PlanUsrRecTab] where PLAN_REF_NUM ='" + newPlanRefNum + "'";

            pst = conn.prepareStatement(sqlDelPlanUsrRec);
            pst.executeUpdate();

            String sqlDelDocWK1 = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimAttDocJustTabWk1] where REF_NUM ='" + newPlanRefNum + "'";

            pst = conn.prepareStatement(sqlDelDocWK1);
            pst.executeUpdate();

            String sqlDelDocWK2 = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimAttDocJustTabWk2] where REF_NUM ='" + newPlanRefNum + "'";

            pst = conn.prepareStatement(sqlDelDocWK2);
            pst.executeUpdate();

            String sqlDelDocWK3 = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimAttDocJustTabWk3] where REF_NUM ='" + newPlanRefNum + "'";

            pst = conn.prepareStatement(sqlDelDocWK3);
            pst.executeUpdate();

            String sqlDelDocWK4 = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimAttDocJustTabWk4] where REF_NUM ='" + newPlanRefNum + "'";

            pst = conn.prepareStatement(sqlDelDocWK4);
            pst.executeUpdate();

            String sqlDelDocWK5 = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimAttDocJustTabWk5] where REF_NUM ='" + newPlanRefNum + "'";

            pst = conn.prepareStatement(sqlDelDocWK5);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void resetField() {

        jDateChooserDialogActivityDateFrom.setDate(null);
        jDateChooserDialogActivityDateTo.setDate(null);
        jTextFieldWk1DialogActivityDesc.setText("");
        jTextAreaWk1DialogJustification.setText("");
        jTextFieldWk1DialogStaffName1.setText("");
        jTextFieldWk1DialogStaffName2.setText("");
        jTextFieldWk1DialogStaffName3.setText("");
        jTextFieldWk1DialogStaffName4.setText("");
        jCheckBoxDialogWk1BrkFast.setEnabled(true);
        jCheckBoxDialogWk1Dinner.setEnabled(true);
        jCheckBoxDialogWk1AccProved.setEnabled(true);
        jCheckBoxDialogWk1Inc.setEnabled(true);
        jCheckBoxDialogWk1Misc.setEnabled(true);
        jCheckBoxDialogWk1BrkFast.setEnabled(false);
        jCheckBoxDialogWk1Lunch.setSelected(true);
        jCheckBoxDialogWk1Dinner.setSelected(true);
        jCheckBoxDialogWk1AccProved.setSelected(false);
        jCheckBoxDialogWk1Inc.setSelected(true);
        jCheckBoxDialogWk1Misc.setSelected(false);
        jCheckBoxDialogWk1AccUnProved.setSelected(true);
        jCheckBoxDialogWk1AccProved.setEnabled(false);
        jCheckBoxNoAcc.setEnabled(false);
        jTextFieldWk1Misc.setVisible(false);
        jLabelWk1Misc.setVisible(false);
        jTextFieldWk1Misc.setText("");
        jLabelWk1MiscAmt.setVisible(false);
        jTextFieldWk1MiscAmt.setVisible(false);
        jTextFieldWk1MiscAmt.setText("");
        jTextFieldDialogWkSite.setText("");
        while (jTableDocAtt.getRowCount()>0){
        modelAtt.removeRow(0);
        }
    }

    void addItem() {
        int selectedOption = JOptionPane.showConfirmDialog(jDialogWk1,
                "Do want to add another  activity line?",
                "Choose",
                JOptionPane.YES_NO_OPTION);

        if ((selectedOption == JOptionPane.NO_OPTION)) {

            jDialogWk1.setVisible(false);
        } else {
            addStaff();
        }

    }

    void refreshAttFields() {
        jTextAttDocDesc.setText("");
        jTextAttDocName.setText("");
        jTextAttDocPath.setText("");
        jTextAttDocDescWk1Main.setText("");
        jTextAttDocNameWk1Main.setText("");
        jTextAttDocPathWk1Main.setText("");
        jTextAttDocDescWk2Main.setText("");
        jTextAttDocNameWk2Main.setText("");
        jTextAttDocPathWk2Main.setText("");
        jTextAttDocDescWk3Main.setText("");
        jTextAttDocNameWk3Main.setText("");
        jTextAttDocPathWk3Main.setText("");
        jTextAttDocDescWk4Main.setText("");
        jTextAttDocNameWk4Main.setText("");
        jTextAttDocPathWk4Main.setText("");
        jTextAttDocDescWk5Main.setText("");
        jTextAttDocNameWk5Main.setText("");
        jTextAttDocPathWk5Main.setText("");

    }

    void addStaff() {
        try {

            if (modelWk1.getRowCount() > 0) {
                jPanelAddStaff.setVisible(true);
            } else {
                jPanelAddStaff.setVisible(false);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void addFileAtt() {

        modelAtt.addRow(new Object[]{jTextAttDocDesc.getText(), jTextAttDocName.getText(), jTextAttDocPath.getText()});
    }

    void addFileAttToTabWk1Main() {

        System.out.println("wkYN 1 " + wk1Site + " -2 " + wk2Site + " -3 " + wk3Site + " -4 " + wk4Site + " -5 " + wk5Site + " - ");
        if ("Y".equals(wk1Site)) {
            for (int i = 0; i < modelAtt.getRowCount(); i++) {

                modelAttWk1Main.addRow(new Object[]{modelAtt.getValueAt(i, 0), modelAtt.getValueAt(i, 1), modelAtt.getValueAt(i, 2)});

            }
        }

        if ("Y".equals(wk2Site)) {
            for (int i = 0; i < modelAtt.getRowCount(); i++) {

                modelAttWk2Main.addRow(new Object[]{modelAtt.getValueAt(i, 0), modelAtt.getValueAt(i, 1), modelAtt.getValueAt(i, 2)});

            }
        }

        if ("Y".equals(wk3Site)) {
            for (int i = 0; i < modelAtt.getRowCount(); i++) {

                modelAttWk3Main.addRow(new Object[]{modelAtt.getValueAt(i, 0), modelAtt.getValueAt(i, 1), modelAtt.getValueAt(i, 2)});

            }
        }

        if ("Y".equals(wk4Site)) {
            for (int i = 0; i < modelAtt.getRowCount(); i++) {

                modelAttWk4Main.addRow(new Object[]{modelAtt.getValueAt(i, 0), modelAtt.getValueAt(i, 1), modelAtt.getValueAt(i, 2)});

            }
        }

        if ("Y".equals(wk5Site)) {
            for (int i = 0; i < modelAtt.getRowCount(); i++) {

                modelAttWk5Main.addRow(new Object[]{modelAtt.getValueAt(i, 0), modelAtt.getValueAt(i, 1), modelAtt.getValueAt(i, 2)});

            }
        }
    }

    void addFileAttWk1Main() {

        modelAttWk1Main.addRow(new Object[]{jTextAttDocDescWk1Main.getText(), jTextAttDocNameWk1Main.getText(), jTextAttDocPathWk1Main.getText()});
    }

    void addFileAttWk2Main() {

        modelAttWk2Main.addRow(new Object[]{jTextAttDocDescWk2Main.getText(), jTextAttDocNameWk2Main.getText(), jTextAttDocPathWk2Main.getText()});
    }

    void addFileAttWk3Main() {

        modelAttWk3Main.addRow(new Object[]{jTextAttDocDescWk3Main.getText(), jTextAttDocNameWk3Main.getText(), jTextAttDocPathWk3Main.getText()});
    }

    void addFileAttWk4Main() {

        modelAttWk4Main.addRow(new Object[]{jTextAttDocDescWk4Main.getText(), jTextAttDocNameWk4Main.getText(), jTextAttDocPathWk4Main.getText()});
    }

    void addFileAttWk5Main() {

        modelAttWk5Main.addRow(new Object[]{jTextAttDocDescWk5Main.getText(), jTextAttDocNameWk5Main.getText(), jTextAttDocPathWk5Main.getText()});
    }

    void searchName1() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();

            int itemCount = jComboBoxSearchResult1.getItemCount();

            for (int i = 0; i < itemCount; i++) {
                jComboBoxSearchResult1.removeItemAt(0);
            }
            jComboBoxSearchResult1.setSelectedIndex(-1);
            String searchNam = jTextFieldSearchNam1.getText();
            st.executeQuery("SELECT distinct [EMP_NAM]\n"
                    + "  FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] WHERE [EMP_NAM] like upper(\n" + "'%" + searchNam + "%')");
            ResultSet r = st.getResultSet();

            while (r.next()) {
                jComboBoxSearchResult1.addItem(r.getString(1));
                jButtonOk1.setVisible(true);
                //                jButtonSearch.setVisible(false);

            }

            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Connect to Database.Try again.", "Error Connection", JOptionPane.WARNING_MESSAGE);
            // System.exit(1);
        }
    }

    void sendMail() {

        final String username = c.FinMailUsrN;
        final String password = c.FinMailUsrP;
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
            String planSupNam, planSupEmail;

            if ((sendToProvMgr.equals(jLabelLineLogNam.getText())) || ("Central Office".equals(jLabelProvince.getText())) || ("Harare Province".equals(jLabelProvince.getText()))) {
                planSupNam = supNam;
                planSupEmail = supUsrMail;
            } else {
                planSupNam = sendToProvMgr;
                planSupEmail = provMgrMail;
            }
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(c.FinMailUsrN));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(planSupEmail));
            message.setSubject("Plan Approval Request.Plan Reference No. " + jLabelSerial.getText() + " " + jLabelRefNum.getText());

            message.setContent("<html><body> Dear " + planSupNam + "<br><br>" + createUsrNam
                    + " has submitted a monthly plan for your approval. <br><br>Please check "
                    + "your inbox and action.<br><br> Kind Regards <br><br>"
                    + " Perdiem Management System </body></html>", "text/html;charset=utf-8");

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    void regCheck() {
        try {
            System.out.println("runn1");
            countPlanAct = 0;
            countPlanPeriod = 0;
            countWk1 = 0;
            countWk2 = 0;
            countWk3 = 0;
            countWk4 = 0;
            countWk5 = 0;
            System.out.println("runn2");

            if ("National Office".equals(jLabelDistrict.getText()) && jTableWk1Activities.getRowCount() > 0) {
                finDayCalcWk1();
                minDateComWk1 = Date.from(minDateWk1.atStartOfDay(zoneId).toInstant());
                maxDateComWk1 = Date.from(maxDateWk1.atStartOfDay(zoneId).toInstant());

            }

            if ("National Office".equals(jLabelDistrict.getText()) && jTableWk2Activities.getRowCount() > 0) {
                finDayCalcWk2();
                minDateComWk2 = Date.from(minDateWk2.atStartOfDay(zoneId).toInstant());
                maxDateComWk2 = Date.from(maxDateWk2.atStartOfDay(zoneId).toInstant());

            }
            if ("National Office".equals(jLabelDistrict.getText()) && jTableWk3Activities.getRowCount() > 0) {
                finDayCalcWk3();
                minDateComWk3 = Date.from(minDateWk3.atStartOfDay(zoneId).toInstant());
                maxDateComWk3 = Date.from(maxDateWk3.atStartOfDay(zoneId).toInstant());

            }
            if ("National Office".equals(jLabelDistrict.getText()) && jTableWk4Activities.getRowCount() > 0) {
                finDayCalcWk4();
                minDateComWk4 = Date.from(minDateWk4.atStartOfDay(zoneId).toInstant());
                maxDateComWk4 = Date.from(maxDateWk4.atStartOfDay(zoneId).toInstant());

            }
            if ("National Office".equals(jLabelDistrict.getText()) && jTableWk5Activities.getRowCount() > 0) {
                finDayCalcWk5();
                minDateComWk5 = Date.from(minDateWk5.atStartOfDay(zoneId).toInstant());
                maxDateComWk5 = Date.from(maxDateWk5.atStartOfDay(zoneId).toInstant());

            }
            System.out.println("runn3");
            if (jTableWk1Activities.getRowCount() == 0 && jTableWk2Activities.getRowCount() == 0
                    && jTableWk3Activities.getRowCount() == 0 && jTableWk4Activities.getRowCount() == 0
                    && jTableWk5Activities.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "At least one activity should be completed.");
                jTabbedPane1.setSelectedIndex(0);
                jDateChooserWk1From.requestFocusInWindow();
                jDateChooserWk1From.setFocusable(true);

            } else if ((jDateChooserWk1From.getDate() == null) || (jDateChooserWk1To.getDate() == null)) {
                JOptionPane.showMessageDialog(this, "Date for week 1 cannot be blank. Please check your dates");
                jDateChooserWk1From.requestFocusInWindow();
                jDateChooserWk1From.setFocusable(true);
            } else if (noOfFinDays1 > 7) {
                JOptionPane.showMessageDialog(this, "Weekly Plan days for week 1 cannot be more than seven(7) days",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk1From.setDate(null);
                jDateChooserWk1To.setDate(null);
                jDateChooserWk1From.requestFocusInWindow();
                jDateChooserWk1From.setFocusable(true);
            } else if (((jDateChooserWk2From.getDate() == null) || (jDateChooserWk2To.getDate() == null)) && noOfFinDays2 > 0) {
                JOptionPane.showMessageDialog(this, "Date for week 2 cannot be blank. Please check your dates");
                jDateChooserWk2From.requestFocusInWindow();
                jDateChooserWk2From.setFocusable(true);
            } else if (noOfFinDays2 > 7) {
                JOptionPane.showMessageDialog(this, "Weekly Plan days for week 2 cannot be more than seven(7) days",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk2From.setDate(null);
                jDateChooserWk2To.setDate(null);
                jDateChooserWk2From.requestFocusInWindow();
                jDateChooserWk2From.setFocusable(true);
            } else if (((jDateChooserWk3From.getDate() == null) || (jDateChooserWk3To.getDate() == null)) && noOfFinDays3 > 0) {
                JOptionPane.showMessageDialog(this, "Date for week 3 cannot be blank. Please check your dates");
                jDateChooserWk3From.requestFocusInWindow();
                jDateChooserWk3From.setFocusable(true);
            } else if (noOfFinDays3 > 7) {

                JOptionPane.showMessageDialog(this, "Weekly Plan days for week 3 cannot be more than seven(7) days",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk3From.setDate(null);
                jDateChooserWk3To.setDate(null);
                jDateChooserWk3From.requestFocusInWindow();
                jDateChooserWk3From.setFocusable(true);
            } else if (((jDateChooserWk4From.getDate() == null) || (jDateChooserWk4To.getDate() == null)) && noOfFinDays4 > 0) {
                JOptionPane.showMessageDialog(this, "Date for week 4 cannot be blank. Please check your dates");
                jDateChooserWk4From.requestFocusInWindow();
                jDateChooserWk4From.setFocusable(true);
            } else if (noOfFinDays4 > 7) {

                JOptionPane.showMessageDialog(this, "Weekly Plan days for week 4 cannot be more than seven(7) days",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk4From.setDate(null);
                jDateChooserWk4To.setDate(null);
                jDateChooserWk4From.requestFocusInWindow();
                jDateChooserWk4From.setFocusable(true);
            } else if (((jDateChooserWk5From.getDate() == null) || (jDateChooserWk5To.getDate() == null)) && noOfFinDays5 > 0) {
                JOptionPane.showMessageDialog(this, "Date for week 5 cannot be blank. Please check your dates");
                jDateChooserWk5From.requestFocusInWindow();
                jDateChooserWk5From.setFocusable(true);
            } else if (noOfFinDays5 > 7) {

                JOptionPane.showMessageDialog(this, "Weekly Plan days for week 5 cannot be more than seven(7) days",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk5From.setDate(null);
                jDateChooserWk5To.setDate(null);
                jDateChooserWk5From.requestFocusInWindow();
                jDateChooserWk5From.setFocusable(true);
            } else if (("National Office".equals(jLabelDistrict.getText())) && jTableWk1Activities.getRowCount() > 0 && (formatter.format(jDateChooserWk1From.getDate()).compareTo(formatter.format(minDateComWk1)) > 0)) {
                JOptionPane.showMessageDialog(this, "Minimum activity date for week 1 cannot be lower than start date.Please check your dates");
                jDateChooserWk1From.requestFocusInWindow();
                jDateChooserWk1From.setFocusable(true);

            } else if (("National Office".equals(jLabelDistrict.getText())) && jTableWk1Activities.getRowCount() > 0 && (formatter.format(jDateChooserWk1To.getDate()).compareTo(formatter.format(maxDateComWk1)) < 0)) {
                JOptionPane.showMessageDialog(this, "Maximum activity date for week 1 cannot be greater than end date.Please check your dates");
                jDateChooserWk1To.requestFocusInWindow();
                jDateChooserWk1To.setFocusable(true);

            } else if ("National Office".equals(jLabelDistrict.getText()) && jTableWk2Activities.getRowCount() > 0 && (formatter.format(jDateChooserWk2From.getDate()).compareTo(formatter.format(minDateComWk2)) > 0)) {
                JOptionPane.showMessageDialog(this, "Minimum activity date for week 2 cannot be lower than start date.Please check your dates");
                jDateChooserWk2From.requestFocusInWindow();
                jDateChooserWk2From.setFocusable(true);

            } else if (("National Office".equals(jLabelDistrict.getText())) && jTableWk2Activities.getRowCount() > 0 && (formatter.format(jDateChooserWk2To.getDate()).compareTo(formatter.format(maxDateComWk2)) < 0)) {
                JOptionPane.showMessageDialog(this, "Maximum activity date for week 2 cannot be greater than end date.Please check your dates");
                jDateChooserWk2To.requestFocusInWindow();
                jDateChooserWk2To.setFocusable(true);

            } else if (("National Office".equals(jLabelDistrict.getText())) && jTableWk3Activities.getRowCount() > 0 && (formatter.format(jDateChooserWk3From.getDate()).compareTo(formatter.format(minDateComWk3)) > 0)) {
                JOptionPane.showMessageDialog(this, "Minimum activity date for week 3 cannot be lower than start date.Please check your dates");
                jDateChooserWk3From.requestFocusInWindow();
                jDateChooserWk3From.setFocusable(true);

            } else if (("National Office".equals(jLabelDistrict.getText())) && jTableWk3Activities.getRowCount() > 0 && (formatter.format(jDateChooserWk3To.getDate()).compareTo(formatter.format(maxDateComWk3)) < 0)) {
                JOptionPane.showMessageDialog(this, "Maximum activity date for week 3 cannot be greater than end date.Please check your dates");
                jDateChooserWk3To.requestFocusInWindow();
                jDateChooserWk3To.setFocusable(true);

            } else if ((!"National Office".equals(jLabelDistrict.getText())) && jTableWk4Activities.getRowCount() > 0 && (formatter.format(jDateChooserWk4From.getDate()).compareTo(formatter.format(minDateComWk4)) > 0)) {
                JOptionPane.showMessageDialog(this, "Minimum activity date for week 4 cannot be lower than start date.Please check your dates");
                jDateChooserWk4From.requestFocusInWindow();
                jDateChooserWk4From.setFocusable(true);

            } else if (("National Office".equals(jLabelDistrict.getText())) && jTableWk4Activities.getRowCount() > 0 && (formatter.format(jDateChooserWk4To.getDate()).compareTo(formatter.format(maxDateComWk4)) < 0)) {
                JOptionPane.showMessageDialog(this, "Maximum activity date for week 4 cannot be greater than end date.Please check your dates");
                jDateChooserWk4To.requestFocusInWindow();
                jDateChooserWk4To.setFocusable(true);

            } else if ((!"National Office".equals(jLabelDistrict.getText())) && jTableWk5Activities.getRowCount() > 0 && (formatter.format(jDateChooserWk5From.getDate()).compareTo(formatter.format(minDateComWk5)) > 0)) {
                JOptionPane.showMessageDialog(this, "Minimum activity date for week 5cannot be lower than start date.Please check your dates");
                jDateChooserWk5From.requestFocusInWindow();
                jDateChooserWk5From.setFocusable(true);

            } else if (("National Office".equals(jLabelDistrict.getText())) && jTableWk5Activities.getRowCount() > 0 && (formatter.format(jDateChooserWk5To.getDate()).compareTo(formatter.format(maxDateComWk5)) < 0)) {
                JOptionPane.showMessageDialog(this, "Maximum activity date for week 5 cannot be greater than end date.Please check your dates");
                jDateChooserWk5To.requestFocusInWindow();
                jDateChooserWk5To.setFocusable(true);

            } else {

                saveRec();

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void saveRec() {
        try {
            jMenuItemSubmit.setEnabled(false);
            jDialogWaiting.setVisible(true);
            SerialCheck();
            RefNumAllocation();

            if (maxPlanRefNum > newPlanRefNum) {
                JOptionPane.showMessageDialog(null, "<html> Registration failure. "
                        + "Registration falure can be caused by slow network speeds.<br><br> "
                        + "Please try again. If the problem persist contact IT.</html>");
            } else {
                createWk1Plan();
                createWk2Plan();
                createWk3Plan();
                createWk4Plan();
                createWk5Plan();
                createWkPlanPeriod();
                createWkPlanAction();
                createWkPlanCreator();
                createAttDocWk1Main();
                createAttDocWk2Main();
                createAttDocWk3Main();
                createAttDocWk4Main();
                createAttDocWk5Main();
                UsrRecUpd();
                getRegDet();
                jDialogWaiting.setVisible(false);
                if ((countWk1 == 0 && countWk2 == 0 && countWk3 == 0 && countWk4 == 0 && countWk5 == 0)
                        || countPlanAct == 0 || countPlanPeriod == 0) {
                    JOptionPane.showMessageDialog(null, "<html> Registration failure. "
                            + "Registration falure can be caused by slow network speeds.MIR.<br><br> "
                            + "Please try again. If the problem persist contact IT.</html>");
                    regRB();
                } else {
                    refNumUpdate();
                }

            }
            jMenuItemSubmit.setEnabled(true);
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
        jDialogSearchName1 = new javax.swing.JDialog();
        jPanelSearchID = new javax.swing.JPanel();
        jTextFieldSearchNam1 = new javax.swing.JTextField();
        jButtonSearch1 = new javax.swing.JButton();
        jComboBoxSearchResult1 = new javax.swing.JComboBox<>();
        jButtonOk1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jDialogWaiting = new javax.swing.JDialog();
        jDialogWk1 = new javax.swing.JDialog();
        jPanelActivity = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabelDialogPerDiem = new javax.swing.JLabel();
        jLabelWk1DialogStaffName = new javax.swing.JLabel();
        jLabelWk1DialogStaffName1 = new javax.swing.JLabel();
        jLabelWk1DialogStaffName2 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jTextFieldWk1DialogStaffName4 = new javax.swing.JTextField();
        jTextFieldWk1DialogStaffName1 = new javax.swing.JTextField();
        jTextFieldWk1DialogStaffName2 = new javax.swing.JTextField();
        jTextFieldWk1DialogStaffName3 = new javax.swing.JTextField();
        jButtonDialogWk1Reset = new javax.swing.JButton();
        jButtonDialogWk1Add = new javax.swing.JButton();
        jCheckBoxDialogWk1BrkFast = new javax.swing.JCheckBox();
        jCheckBoxDialogWk1Lunch = new javax.swing.JCheckBox();
        jCheckBoxDialogWk1Dinner = new javax.swing.JCheckBox();
        jCheckBoxDialogWk1Misc = new javax.swing.JCheckBox();
        jCheckBoxDialogWk1Inc = new javax.swing.JCheckBox();
        jLabelWk1Misc = new javax.swing.JLabel();
        jTextFieldWk1Misc = new javax.swing.JTextField();
        jButtonDialogWk1Close = new javax.swing.JButton();
        jLabelWk1MiscAmt = new javax.swing.JLabel();
        jTextFieldWk1MiscAmt = new javax.swing.JTextField();
        jLabelWk1Name4Del = new javax.swing.JLabel();
        jLabelWk1Name1Del = new javax.swing.JLabel();
        jLabelWk1Name2Del = new javax.swing.JLabel();
        jLabelWk1Name3Del = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jCheckBoxDialogWk1AccUnProved = new javax.swing.JCheckBox();
        jCheckBoxDialogWk1AccProved = new javax.swing.JCheckBox();
        jCheckBoxNoAcc = new javax.swing.JCheckBox();
        jPanelDepartureDetails = new javax.swing.JPanel();
        jLabelDialogDepartActivityDate = new javax.swing.JLabel();
        jDateChooserDialogActivityDateFrom = new com.toedter.calendar.JDateChooser();
        departTime = new cambodia.raven.Time();
        jPanel3 = new javax.swing.JPanel();
        jLabelDialogReturnActivityDate = new javax.swing.JLabel();
        jDateChooserDialogActivityDateTo = new com.toedter.calendar.JDateChooser();
        returnTime = new cambodia.raven.Time();
        jPanelDooAtt = new javax.swing.JPanel();
        jPanelAttach = new javax.swing.JPanel();
        jLabelMOHCCConfirmation = new javax.swing.JLabel();
        jToggleButtonMOHCCConfirmation = new javax.swing.JToggleButton();
        jToggleButtonMOHCCConfirmation2 = new javax.swing.JToggleButton();
        jTextAttDocDesc = new javax.swing.JTextField();
        jButtonSelectFile = new javax.swing.JButton();
        jTextAttDocName = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableDocAtt = new javax.swing.JTable();
        jTextAttDocPath = new javax.swing.JTextField();
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
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaWk1DialogJustification = new javax.swing.JTextArea();
        jLabelWk1DialogActivityDesc = new javax.swing.JLabel();
        jTextFieldWk1DialogActivityDesc = new javax.swing.JTextField();
        jLabelJustfy = new javax.swing.JLabel();
        jLabelPrjCodeProgramming = new javax.swing.JLabel();
        jComboProjectCodeProgramming = new javax.swing.JComboBox<>();
        jLabelRemain = new javax.swing.JLabel();
        jPanelAddStaff = new javax.swing.JPanel();
        jCheckAddStaff = new javax.swing.JCheckBox();
        jDialogWaitingEmail = new javax.swing.JDialog();
        buttonGroupAcc = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanelWkOne = new javax.swing.JPanel();
        jLabelWkDuration = new javax.swing.JLabel();
        jDateChooserWk1From = new com.toedter.calendar.JDateChooser();
        jLabelWk1From = new javax.swing.JLabel();
        jLabelWk1To = new javax.swing.JLabel();
        jDateChooserWk1To = new com.toedter.calendar.JDateChooser();
        jButtonWk1AddActivity = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPaneWk1 = new javax.swing.JScrollPane();
        jTableWk1Activities = new javax.swing.JTable();
        jLabelLogo1 = new javax.swing.JLabel();
        jButtonWk1DelActivity = new javax.swing.JButton();
        jLabelProvince1 = new javax.swing.JLabel();
        jLabelOffice1 = new javax.swing.JLabel();
        jLabelRef = new javax.swing.JLabel();
        jLabelSerial = new javax.swing.JLabel();
        jLabelRefNum = new javax.swing.JLabel();
        jLabelHeaderGen4 = new javax.swing.JLabel();
        jLabelEmp = new javax.swing.JLabel();
        jLabelGenLogNam1 = new javax.swing.JLabel();
        jLabelLineLogNam = new javax.swing.JLabel();
        jLabelLineTime = new javax.swing.JLabel();
        jLabelLineDate = new javax.swing.JLabel();
        jLabelProvince = new javax.swing.JLabel();
        jLabelDistrict = new javax.swing.JLabel();
        jPanelDooAttWk1Main = new javax.swing.JPanel();
        jPanelAttachWk1Main = new javax.swing.JPanel();
        jLabelConfirmationWk1Main = new javax.swing.JLabel();
        jTextAttDocDescWk1Main = new javax.swing.JTextField();
        jButtonSelectFileWk1Main = new javax.swing.JButton();
        jTextAttDocNameWk1Main = new javax.swing.JTextField();
        jButtonWk1Add = new javax.swing.JButton();
        jButtonWk1Delete = new javax.swing.JButton();
        jScrollPaneWk1Att = new javax.swing.JScrollPane();
        jTableDocAttWk1Main = new javax.swing.JTable();
        jTextAttDocPathWk1Main = new javax.swing.JTextField();
        jPanelSave = new javax.swing.JPanel();
        jPanelWkTwo = new javax.swing.JPanel();
        jLabelWkDuration1 = new javax.swing.JLabel();
        jDateChooserWk2From = new com.toedter.calendar.JDateChooser();
        jLabelWk1From1 = new javax.swing.JLabel();
        jLabelWk1To1 = new javax.swing.JLabel();
        jDateChooserWk2To = new com.toedter.calendar.JDateChooser();
        jButtonWk2AddActivity = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jLabelLogo2 = new javax.swing.JLabel();
        jButtonWk2DelActivity = new javax.swing.JButton();
        jLabelGenLogNam3 = new javax.swing.JLabel();
        jLabelLineDate1 = new javax.swing.JLabel();
        jLabelLineTime1 = new javax.swing.JLabel();
        jLabelLineLogNam1 = new javax.swing.JLabel();
        jLabelHeaderGen7 = new javax.swing.JLabel();
        jPanelDooAttWk2Main = new javax.swing.JPanel();
        jPanelAttachWk1Main1 = new javax.swing.JPanel();
        jLabelConfirmationWk2Main = new javax.swing.JLabel();
        jTextAttDocDescWk2Main = new javax.swing.JTextField();
        jButtonSelectFileWk2Main = new javax.swing.JButton();
        jTextAttDocNameWk2Main = new javax.swing.JTextField();
        jButtonWk2Add = new javax.swing.JButton();
        jButtonWk2Delete = new javax.swing.JButton();
        jScrollPaneWk2Att = new javax.swing.JScrollPane();
        jTableDocAttWk2Main = new javax.swing.JTable();
        jTextAttDocPathWk2Main = new javax.swing.JTextField();
        jPanelSave1 = new javax.swing.JPanel();
        jScrollPaneWk2 = new javax.swing.JScrollPane();
        jTableWk2Activities = new javax.swing.JTable();
        jPanelWkThree = new javax.swing.JPanel();
        jLabelWkDuration2 = new javax.swing.JLabel();
        jDateChooserWk3From = new com.toedter.calendar.JDateChooser();
        jLabelWk3From = new javax.swing.JLabel();
        jLabelWk3To = new javax.swing.JLabel();
        jDateChooserWk3To = new com.toedter.calendar.JDateChooser();
        jButtonWk3AddActivity = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jLabelLogo3 = new javax.swing.JLabel();
        jButtonWk3DelActivity = new javax.swing.JButton();
        jLabelGenLogNam5 = new javax.swing.JLabel();
        jLabelLineLogNam2 = new javax.swing.JLabel();
        jLabelLineTime2 = new javax.swing.JLabel();
        jLabelLineDate2 = new javax.swing.JLabel();
        jLabelHeaderGen8 = new javax.swing.JLabel();
        jPanelDooAttWk3Main = new javax.swing.JPanel();
        jPanelAttachWk3Main = new javax.swing.JPanel();
        jLabelConfirmationWk3Main = new javax.swing.JLabel();
        jTextAttDocDescWk3Main = new javax.swing.JTextField();
        jButtonSelectFileWk3Main = new javax.swing.JButton();
        jTextAttDocNameWk3Main = new javax.swing.JTextField();
        jButtonWk3Add = new javax.swing.JButton();
        jButtonWk3Delete = new javax.swing.JButton();
        jScrollPaneWk3Att = new javax.swing.JScrollPane();
        jTableDocAttWk3Main = new javax.swing.JTable();
        jTextAttDocPathWk3Main = new javax.swing.JTextField();
        jPanelSave2 = new javax.swing.JPanel();
        jScrollPaneWk3 = new javax.swing.JScrollPane();
        jTableWk3Activities = new javax.swing.JTable();
        jPanelWkFour = new javax.swing.JPanel();
        jLabelWkDuration4 = new javax.swing.JLabel();
        jDateChooserWk4From = new com.toedter.calendar.JDateChooser();
        jLabelWk4From = new javax.swing.JLabel();
        jLabelWk4To = new javax.swing.JLabel();
        jDateChooserWk4To = new com.toedter.calendar.JDateChooser();
        jButtonWk4AddActivity = new javax.swing.JButton();
        jSeparator37 = new javax.swing.JSeparator();
        jSeparator38 = new javax.swing.JSeparator();
        jLabelLogo4 = new javax.swing.JLabel();
        jButtonWk4DelActivity = new javax.swing.JButton();
        jLabelGenLogNam6 = new javax.swing.JLabel();
        jLabelLineLogNam3 = new javax.swing.JLabel();
        jLabelLineTime3 = new javax.swing.JLabel();
        jLabelLineDate3 = new javax.swing.JLabel();
        jLabelHeaderGen9 = new javax.swing.JLabel();
        jTextAttDocPathWk4Main = new javax.swing.JTextField();
        jPanelDooAttWk4Main = new javax.swing.JPanel();
        jPanelAttachWk4Main = new javax.swing.JPanel();
        jLabelConfirmationWk4Main = new javax.swing.JLabel();
        jTextAttDocDescWk4Main = new javax.swing.JTextField();
        jButtonSelectFileWk4Main = new javax.swing.JButton();
        jTextAttDocNameWk4Main = new javax.swing.JTextField();
        jButtonWk4Add = new javax.swing.JButton();
        jButtonWk4Delete = new javax.swing.JButton();
        jScrollPaneWk4Att = new javax.swing.JScrollPane();
        jTableDocAttWk4Main = new javax.swing.JTable();
        jPanelSave3 = new javax.swing.JPanel();
        jScrollPaneWk4 = new javax.swing.JScrollPane();
        jTableWk4Activities = new javax.swing.JTable();
        jPanelWkFive = new javax.swing.JPanel();
        jLabelWkDuration5 = new javax.swing.JLabel();
        jDateChooserWk5From = new com.toedter.calendar.JDateChooser();
        jLabelWk5From = new javax.swing.JLabel();
        jLabelWk5To = new javax.swing.JLabel();
        jDateChooserWk5To = new com.toedter.calendar.JDateChooser();
        jButtonWk5AddActivity = new javax.swing.JButton();
        jSeparator39 = new javax.swing.JSeparator();
        jSeparator40 = new javax.swing.JSeparator();
        jLabelLogo5 = new javax.swing.JLabel();
        jButtonWk5DelActivity = new javax.swing.JButton();
        jLabelGenLogNam7 = new javax.swing.JLabel();
        jLabelLineLogNam4 = new javax.swing.JLabel();
        jLabelLineTime4 = new javax.swing.JLabel();
        jLabelLineDate4 = new javax.swing.JLabel();
        jLabelHeaderGen10 = new javax.swing.JLabel();
        jPanelDooAttWk5Main = new javax.swing.JPanel();
        jPanelAttachWk5Main = new javax.swing.JPanel();
        jLabelConfirmationWk5Main = new javax.swing.JLabel();
        jTextAttDocDescWk5Main = new javax.swing.JTextField();
        jButtonSelectFileWk5Main = new javax.swing.JButton();
        jTextAttDocNameWk5Main = new javax.swing.JTextField();
        jButtonWk5Add = new javax.swing.JButton();
        jButtonWk5Delete = new javax.swing.JButton();
        jScrollPaneWk5Att = new javax.swing.JScrollPane();
        jTableDocAttWk5Main = new javax.swing.JTable();
        jTextAttDocPathWk5Main = new javax.swing.JTextField();
        jPanelSave4 = new javax.swing.JPanel();
        jScrollPaneWk5 = new javax.swing.JScrollPane();
        jTableWk5Activities = new javax.swing.JTable();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuNew = new javax.swing.JMenu();
        jMenuItemPlanPerDiem = new javax.swing.JMenuItem();
        jSeparator14 = new javax.swing.JPopupMenu.Separator();
        jMenuItemPerDiemAcquittal = new javax.swing.JMenuItem();
        jSeparator11 = new javax.swing.JPopupMenu.Separator();
        jMenuEdit = new javax.swing.JMenu();
        jMenuMonPlanEdit = new javax.swing.JMenuItem();
        jSeparator12 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSubmit = new javax.swing.JMenuItem();
        jSeparator13 = new javax.swing.JPopupMenu.Separator();
        jMenuItemClose = new javax.swing.JMenuItem();
        jSeparator33 = new javax.swing.JPopupMenu.Separator();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuRequest = new javax.swing.JMenu();
        jMenuItemSupApp = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAccMgrRev = new javax.swing.JMenuItem();
        jSeparator17 = new javax.swing.JPopupMenu.Separator();
        jMenuItemHeadApp = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSchPerDiem = new javax.swing.JMenuItem();
        jSeparator23 = new javax.swing.JPopupMenu.Separator();
        jMenuItemView = new javax.swing.JMenuItem();
        jMenuAcquittal = new javax.swing.JMenu();
        jMenuItemAcqSupApp = new javax.swing.JMenuItem();
        jSeparator18 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqAccApp = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqHeadApp = new javax.swing.JMenuItem();
        jSeparator19 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqSchGen = new javax.swing.JMenuItem();
        jSeparator20 = new javax.swing.JPopupMenu.Separator();
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
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jComboFacilityMouseReleased(evt);
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

        jDialogSearchName1.setTitle("Search Employee ID");
        jDialogSearchName1.setAlwaysOnTop(true);
        jDialogSearchName1.setAutoRequestFocus(false);
        jDialogSearchName1.setBackground(new java.awt.Color(153, 204, 0));
        jDialogSearchName1.setLocation(new java.awt.Point(700, 400));
        jDialogSearchName1.setMinimumSize(new java.awt.Dimension(400, 200));

        jPanelSearchID.setBackground(new java.awt.Color(7, 163, 163));
        jPanelSearchID.setMinimumSize(new java.awt.Dimension(400, 200));
        jPanelSearchID.setLayout(null);

        jTextFieldSearchNam1.setPreferredSize(new java.awt.Dimension(50, 30));
        jTextFieldSearchNam1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldSearchNam1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldSearchNam1FocusLost(evt);
            }
        });
        jTextFieldSearchNam1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldSearchNam1ActionPerformed(evt);
            }
        });
        jPanelSearchID.add(jTextFieldSearchNam1);
        jTextFieldSearchNam1.setBounds(10, 22, 204, 30);

        jButtonSearch1.setText("Search");
        jButtonSearch1.setPreferredSize(new java.awt.Dimension(65, 30));
        jButtonSearch1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearch1ActionPerformed(evt);
            }
        });
        jPanelSearchID.add(jButtonSearch1);
        jButtonSearch1.setBounds(256, 22, 90, 30);

        jComboBoxSearchResult1.setPreferredSize(new java.awt.Dimension(70, 30));
        jPanelSearchID.add(jComboBoxSearchResult1);
        jComboBoxSearchResult1.setBounds(10, 70, 204, 30);

        jButtonOk1.setText("OK");
        jButtonOk1.setPreferredSize(new java.awt.Dimension(73, 30));
        jButtonOk1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOk1ActionPerformed(evt);
            }
        });
        jPanelSearchID.add(jButtonOk1);
        jButtonOk1.setBounds(256, 70, 90, 30);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Enter search term e.g. Joe and click search button");
        jPanelSearchID.add(jLabel4);
        jLabel4.setBounds(12, 3, 270, 13);

        javax.swing.GroupLayout jDialogSearchName1Layout = new javax.swing.GroupLayout(jDialogSearchName1.getContentPane());
        jDialogSearchName1.getContentPane().setLayout(jDialogSearchName1Layout);
        jDialogSearchName1Layout.setHorizontalGroup(
            jDialogSearchName1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelSearchID, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogSearchName1Layout.setVerticalGroup(
            jDialogSearchName1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelSearchID, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        jDialogWk1.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jDialogWk1.setTitle("ACTIVITY PLANNER WEEK 1");
        jDialogWk1.setBackground(new java.awt.Color(255, 255, 204));
        jDialogWk1.setLocation(new java.awt.Point(200, 50));
        jDialogWk1.setMinimumSize(new java.awt.Dimension(890, 750));
        jDialogWk1.setName("dialogWk1"); // NOI18N
        jDialogWk1.setResizable(false);
        jDialogWk1.setSize(new java.awt.Dimension(850, 750));

        jPanelActivity.setBackground(new java.awt.Color(237, 235, 92));
        jPanelActivity.setMinimumSize(new java.awt.Dimension(890, 750));
        jPanelActivity.setPreferredSize(new java.awt.Dimension(890, 750));
        jPanelActivity.setVerifyInputWhenFocusTarget(false);
        jPanelActivity.setLayout(null);

        jLabel10.setText("4.");
        jPanelActivity.add(jLabel10);
        jLabel10.setBounds(530, 300, 20, 30);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanelActivity.add(jSeparator3);
        jSeparator3.setBounds(530, 140, 0, 510);

        jLabelDialogPerDiem.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelDialogPerDiem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelDialogPerDiem.setText("Activity Details");
        jPanelActivity.add(jLabelDialogPerDiem);
        jLabelDialogPerDiem.setBounds(10, 0, 460, 30);

        jLabelWk1DialogStaffName.setText("Staff Details");
        jPanelActivity.add(jLabelWk1DialogStaffName);
        jLabelWk1DialogStaffName.setBounds(530, 150, 130, 30);

        jLabelWk1DialogStaffName1.setText("1.");
        jPanelActivity.add(jLabelWk1DialogStaffName1);
        jLabelWk1DialogStaffName1.setBounds(530, 180, 20, 30);

        jLabelWk1DialogStaffName2.setText("2.");
        jPanelActivity.add(jLabelWk1DialogStaffName2);
        jLabelWk1DialogStaffName2.setBounds(530, 220, 20, 30);

        jLabel17.setText("3.");
        jPanelActivity.add(jLabel17);
        jLabel17.setBounds(530, 260, 20, 30);

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
        jPanelActivity.add(jTextFieldWk1DialogStaffName4);
        jTextFieldWk1DialogStaffName4.setBounds(550, 300, 220, 30);

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
        jPanelActivity.add(jTextFieldWk1DialogStaffName1);
        jTextFieldWk1DialogStaffName1.setBounds(550, 180, 220, 30);

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
        jPanelActivity.add(jTextFieldWk1DialogStaffName2);
        jTextFieldWk1DialogStaffName2.setBounds(550, 220, 220, 30);

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
        jPanelActivity.add(jTextFieldWk1DialogStaffName3);
        jTextFieldWk1DialogStaffName3.setBounds(550, 260, 220, 30);

        jButtonDialogWk1Reset.setText("Reset");
        jButtonDialogWk1Reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDialogWk1ResetActionPerformed(evt);
            }
        });
        jPanelActivity.add(jButtonDialogWk1Reset);
        jButtonDialogWk1Reset.setBounds(660, 580, 70, 30);

        jButtonDialogWk1Add.setText("Add Activity");
        jButtonDialogWk1Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDialogWk1AddActionPerformed(evt);
            }
        });
        jPanelActivity.add(jButtonDialogWk1Add);
        jButtonDialogWk1Add.setBounds(540, 580, 100, 30);

        jCheckBoxDialogWk1BrkFast.setText(" Breakfast");
        jPanelActivity.add(jCheckBoxDialogWk1BrkFast);
        jCheckBoxDialogWk1BrkFast.setBounds(540, 350, 90, 21);

        jCheckBoxDialogWk1Lunch.setText("Lunch");
        jPanelActivity.add(jCheckBoxDialogWk1Lunch);
        jCheckBoxDialogWk1Lunch.setBounds(660, 350, 80, 21);

        jCheckBoxDialogWk1Dinner.setText(" Dinner");
        jPanelActivity.add(jCheckBoxDialogWk1Dinner);
        jCheckBoxDialogWk1Dinner.setBounds(540, 390, 90, 21);

        jCheckBoxDialogWk1Misc.setText("Miscellaneous");
        jCheckBoxDialogWk1Misc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDialogWk1MiscActionPerformed(evt);
            }
        });
        jPanelActivity.add(jCheckBoxDialogWk1Misc);
        jCheckBoxDialogWk1Misc.setBounds(540, 470, 110, 21);

        jCheckBoxDialogWk1Inc.setText("Incidental");
        jPanelActivity.add(jCheckBoxDialogWk1Inc);
        jCheckBoxDialogWk1Inc.setBounds(540, 430, 110, 21);

        jLabelWk1Misc.setText("Miscellaneous Desc");
        jPanelActivity.add(jLabelWk1Misc);
        jLabelWk1Misc.setBounds(540, 490, 160, 30);

        jTextFieldWk1Misc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldWk1MiscActionPerformed(evt);
            }
        });
        jPanelActivity.add(jTextFieldWk1Misc);
        jTextFieldWk1Misc.setBounds(540, 520, 110, 30);

        jButtonDialogWk1Close.setText("Close");
        jButtonDialogWk1Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDialogWk1CloseActionPerformed(evt);
            }
        });
        jPanelActivity.add(jButtonDialogWk1Close);
        jButtonDialogWk1Close.setBounds(740, 580, 70, 30);

        jLabelWk1MiscAmt.setText("$");
        jPanelActivity.add(jLabelWk1MiscAmt);
        jLabelWk1MiscAmt.setBounds(670, 520, 30, 30);
        jPanelActivity.add(jTextFieldWk1MiscAmt);
        jTextFieldWk1MiscAmt.setBounds(710, 520, 70, 30);

        jLabelWk1Name4Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk1Name4Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk1Name4DelMouseClicked(evt);
            }
        });
        jPanelActivity.add(jLabelWk1Name4Del);
        jLabelWk1Name4Del.setBounds(770, 300, 30, 30);

        jLabelWk1Name1Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk1Name1Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk1Name1DelMouseClicked(evt);
            }
        });
        jPanelActivity.add(jLabelWk1Name1Del);
        jLabelWk1Name1Del.setBounds(770, 180, 30, 30);

        jLabelWk1Name2Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk1Name2Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk1Name2DelMouseClicked(evt);
            }
        });
        jPanelActivity.add(jLabelWk1Name2Del);
        jLabelWk1Name2Del.setBounds(770, 220, 30, 30);

        jLabelWk1Name3Del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/delete.png"))); // NOI18N
        jLabelWk1Name3Del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelWk1Name3DelMouseClicked(evt);
            }
        });
        jPanelActivity.add(jLabelWk1Name3Del);
        jLabelWk1Name3Del.setBounds(770, 260, 30, 30);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(null);

        jCheckBoxDialogWk1AccUnProved.setText(" Unproved Acc");
        jPanel2.add(jCheckBoxDialogWk1AccUnProved);
        jCheckBoxDialogWk1AccUnProved.setBounds(0, 0, 130, 21);

        jCheckBoxDialogWk1AccProved.setText(" Proved Acc");
        jPanel2.add(jCheckBoxDialogWk1AccProved);
        jCheckBoxDialogWk1AccProved.setBounds(0, 40, 130, 21);

        jCheckBoxNoAcc.setText("No Acc Required");
        jPanel2.add(jCheckBoxNoAcc);
        jCheckBoxNoAcc.setBounds(0, 80, 130, 21);

        jPanelActivity.add(jPanel2);
        jPanel2.setBounds(670, 390, 130, 100);

        jPanelDepartureDetails.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDepartureDetails.setLayout(null);
        jPanelDepartureDetails.add(jLabelDialogDepartActivityDate);
        jLabelDialogDepartActivityDate.setBounds(5, 15, 140, 30);

        jDateChooserDialogActivityDateFrom.setDateFormatString("yyyy-MM-dd");
        jPanelDepartureDetails.add(jDateChooserDialogActivityDateFrom);
        jDateChooserDialogActivityDateFrom.setBounds(150, 15, 120, 30);

        departTime.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDepartureDetails.add(departTime);
        departTime.setBounds(280, -10, 122, 70);

        jPanelActivity.add(jPanelDepartureDetails);
        jPanelDepartureDetails.setBounds(10, 35, 400, 60);

        jPanel3.setLayout(null);
        jPanel3.add(jLabelDialogReturnActivityDate);
        jLabelDialogReturnActivityDate.setBounds(10, 20, 140, 30);

        jDateChooserDialogActivityDateTo.setDateFormatString("yyyy-MM-dd");
        jPanel3.add(jDateChooserDialogActivityDateTo);
        jDateChooserDialogActivityDateTo.setBounds(150, 15, 120, 30);
        jPanel3.add(returnTime);
        returnTime.setBounds(280, -10, 122, 70);

        jPanelActivity.add(jPanel3);
        jPanel3.setBounds(460, 35, 400, 60);

        jPanelDooAtt.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDooAtt.setLayout(null);

        jPanelAttach.setBackground(new java.awt.Color(255, 255, 255));
        jPanelAttach.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelAttach.setLayout(null);

        jLabelMOHCCConfirmation.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelMOHCCConfirmation.setText(" Enter Attachment Description below and select File");
        jLabelMOHCCConfirmation.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelAttach.add(jLabelMOHCCConfirmation);
        jLabelMOHCCConfirmation.setBounds(0, 0, 280, 25);

        jToggleButtonMOHCCConfirmation.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jToggleButtonMOHCCConfirmation.setText("Delete File");
        jToggleButtonMOHCCConfirmation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonMOHCCConfirmationActionPerformed(evt);
            }
        });
        jPanelAttach.add(jToggleButtonMOHCCConfirmation);
        jToggleButtonMOHCCConfirmation.setBounds(150, 85, 90, 20);

        jToggleButtonMOHCCConfirmation2.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jToggleButtonMOHCCConfirmation2.setText("Add File");
        jToggleButtonMOHCCConfirmation2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonMOHCCConfirmation2ActionPerformed(evt);
            }
        });
        jPanelAttach.add(jToggleButtonMOHCCConfirmation2);
        jToggleButtonMOHCCConfirmation2.setBounds(30, 85, 90, 20);

        jTextAttDocDesc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocDescActionPerformed(evt);
            }
        });
        jPanelAttach.add(jTextAttDocDesc);
        jTextAttDocDesc.setBounds(5, 30, 170, 25);

        jButtonSelectFile.setText("Select File");
        jButtonSelectFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectFileActionPerformed(evt);
            }
        });
        jPanelAttach.add(jButtonSelectFile);
        jButtonSelectFile.setBounds(180, 30, 95, 25);

        jTextAttDocName.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextAttDocName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocNameActionPerformed(evt);
            }
        });
        jPanelAttach.add(jTextAttDocName);
        jTextAttDocName.setBounds(5, 60, 270, 20);

        jPanelDooAtt.add(jPanelAttach);
        jPanelAttach.setBounds(0, 0, 280, 110);

        jTableDocAtt.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Description", "File Name", "File Path"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTableDocAtt);

        jPanelDooAtt.add(jScrollPane3);
        jScrollPane3.setBounds(280, 0, 230, 110);

        jPanelActivity.add(jPanelDooAtt);
        jPanelDooAtt.setBounds(10, 580, 510, 110);

        jTextAttDocPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocPathActionPerformed(evt);
            }
        });
        jPanelActivity.add(jTextAttDocPath);
        jTextAttDocPath.setBounds(620, 630, 230, 25);

        jPanelActivityInfo.setBackground(new java.awt.Color(237, 235, 92));
        jPanelActivityInfo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(204, 204, 204), null, null));
        jPanelActivityInfo.setLayout(null);

        jLabeAccountCode.setText("Account Code");
        jPanelActivityInfo.add(jLabeAccountCode);
        jLabeAccountCode.setBounds(10, 0, 495, 20);

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
        jComboAccountCode.setBounds(10, 20, 495, 30);

        jLabelDonor.setText("Donor");
        jPanelActivityInfo.add(jLabelDonor);
        jLabelDonor.setBounds(10, 50, 495, 20);

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
        jComboDonor.setBounds(10, 70, 495, 30);

        jLabelPrjCodeGL.setText("Project Code (GL)");
        jPanelActivityInfo.add(jLabelPrjCodeGL);
        jLabelPrjCodeGL.setBounds(10, 100, 495, 20);

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
        jComboProjectCodeGL.setBounds(10, 120, 495, 30);

        jLabelBudMainCode.setText("Budget line");
        jPanelActivityInfo.add(jLabelBudMainCode);
        jLabelBudMainCode.setBounds(10, 200, 495, 20);

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
        jComboBudMainCode.setBounds(10, 220, 495, 30);

        jLabelDialogWk1Site.setText("Site to be Visited");
        jPanelActivityInfo.add(jLabelDialogWk1Site);
        jLabelDialogWk1Site.setBounds(10, 250, 495, 20);

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
        jTextFieldDialogWkSite.setBounds(10, 270, 495, 30);

        jTextAreaWk1DialogJustification.setColumns(20);
        jTextAreaWk1DialogJustification.setRows(5);
        jTextAreaWk1DialogJustification.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextAreaWk1DialogJustificationKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(jTextAreaWk1DialogJustification);

        jPanelActivityInfo.add(jScrollPane2);
        jScrollPane2.setBounds(10, 370, 490, 70);

        jLabelWk1DialogActivityDesc.setText("Activity Descrpition");
        jPanelActivityInfo.add(jLabelWk1DialogActivityDesc);
        jLabelWk1DialogActivityDesc.setBounds(10, 300, 130, 20);
        jPanelActivityInfo.add(jTextFieldWk1DialogActivityDesc);
        jTextFieldWk1DialogActivityDesc.setBounds(10, 320, 490, 30);

        jLabelJustfy.setText("Justification for Choice of Activity ");
        jPanelActivityInfo.add(jLabelJustfy);
        jLabelJustfy.setBounds(10, 350, 200, 20);

        jLabelPrjCodeProgramming.setText("Project Code (Programming)");
        jPanelActivityInfo.add(jLabelPrjCodeProgramming);
        jLabelPrjCodeProgramming.setBounds(10, 150, 495, 20);

        jComboProjectCodeProgramming.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboProjectCodeProgrammingMouseClicked(evt);
            }
        });
        jPanelActivityInfo.add(jComboProjectCodeProgramming);
        jComboProjectCodeProgramming.setBounds(10, 170, 495, 30);

        jLabelRemain.setFont(new java.awt.Font("Tahoma", 3, 9)); // NOI18N
        jPanelActivityInfo.add(jLabelRemain);
        jLabelRemain.setBounds(280, 350, 210, 20);

        jPanelActivity.add(jPanelActivityInfo);
        jPanelActivityInfo.setBounds(0, 110, 510, 450);

        jPanelAddStaff.setBackground(new java.awt.Color(255, 255, 255));
        jPanelAddStaff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelAddStaff.setLayout(null);

        jCheckAddStaff.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jCheckAddStaff.setText(" Additional Staff");
        jPanelAddStaff.add(jCheckAddStaff);
        jCheckAddStaff.setBounds(10, 5, 220, 21);

        jPanelActivity.add(jPanelAddStaff);
        jPanelAddStaff.setBounds(530, 120, 320, 30);

        javax.swing.GroupLayout jDialogWk1Layout = new javax.swing.GroupLayout(jDialogWk1.getContentPane());
        jDialogWk1.getContentPane().setLayout(jDialogWk1Layout);
        jDialogWk1Layout.setHorizontalGroup(
            jDialogWk1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelActivity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogWk1Layout.setVerticalGroup(
            jDialogWk1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelActivity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        setMinimumSize(new java.awt.Dimension(425, 749));

        jPanelWkOne.setBackground(new java.awt.Color(255, 255, 204));
        jPanelWkOne.setLayout(null);

        jLabelWkDuration.setText("Week Duration");
        jPanelWkOne.add(jLabelWkDuration);
        jLabelWkDuration.setBounds(30, 165, 90, 30);

        jDateChooserWk1From.setDateFormatString("yyyy-MM-dd");
        jPanelWkOne.add(jDateChooserWk1From);
        jDateChooserWk1From.setBounds(210, 165, 120, 30);

        jLabelWk1From.setText("From");
        jPanelWkOne.add(jLabelWk1From);
        jLabelWk1From.setBounds(150, 165, 41, 30);

        jLabelWk1To.setText("To");
        jPanelWkOne.add(jLabelWk1To);
        jLabelWk1To.setBounds(390, 165, 41, 30);

        jDateChooserWk1To.setDateFormatString("yyyy-MM-dd");
        jPanelWkOne.add(jDateChooserWk1To);
        jDateChooserWk1To.setBounds(440, 165, 120, 30);

        jButtonWk1AddActivity.setBackground(new java.awt.Color(0, 153, 51));
        jButtonWk1AddActivity.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonWk1AddActivity.setForeground(new java.awt.Color(255, 255, 255));
        jButtonWk1AddActivity.setText("Add Activity");
        jButtonWk1AddActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk1AddActivityActionPerformed(evt);
            }
        });
        jPanelWkOne.add(jButtonWk1AddActivity);
        jButtonWk1AddActivity.setBounds(750, 165, 150, 30);
        jPanelWkOne.add(jSeparator1);
        jSeparator1.setBounds(30, 205, 1280, 10);
        jPanelWkOne.add(jSeparator2);
        jSeparator2.setBounds(30, 155, 1280, 10);

        jTableWk1Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Account Code", "Donor", "Project Code GL", "Project Code Program", "Project Name Program", "Budget Line", "Budget Code", "Site to Visit", "Activity", "Justification for Choice of Activity ", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Staff Member 1", "Staff Member 2", "Staff Member 3", "Staff Member 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk1.setViewportView(jTableWk1Activities);

        jPanelWkOne.add(jScrollPaneWk1);
        jScrollPaneWk1.setBounds(30, 220, 1290, 310);

        jLabelLogo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelWkOne.add(jLabelLogo1);
        jLabelLogo1.setBounds(10, 5, 220, 115);

        jButtonWk1DelActivity.setBackground(new java.awt.Color(204, 0, 0));
        jButtonWk1DelActivity.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonWk1DelActivity.setForeground(new java.awt.Color(255, 255, 255));
        jButtonWk1DelActivity.setText("Delete Per Diem  Activity");
        jButtonWk1DelActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk1DelActivityActionPerformed(evt);
            }
        });
        jPanelWkOne.add(jButtonWk1DelActivity);
        jButtonWk1DelActivity.setBounds(1020, 165, 210, 30);

        jLabelProvince1.setText("Province");
        jLabelProvince1.setMinimumSize(new java.awt.Dimension(130, 130));
        jLabelProvince1.setName(""); // NOI18N
        jPanelWkOne.add(jLabelProvince1);
        jLabelProvince1.setBounds(30, 120, 70, 30);

        jLabelOffice1.setText("District");
        jLabelOffice1.setMinimumSize(new java.awt.Dimension(130, 130));
        jLabelOffice1.setName(""); // NOI18N
        jPanelWkOne.add(jLabelOffice1);
        jLabelOffice1.setBounds(530, 120, 70, 30);

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

        jLabelHeaderGen4.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen4.setText("MONTHLY  PLAN ");
        jPanelWkOne.add(jLabelHeaderGen4);
        jLabelHeaderGen4.setBounds(450, 40, 420, 40);
        jPanelWkOne.add(jLabelEmp);
        jLabelEmp.setBounds(1180, 66, 70, 30);

        jLabelGenLogNam1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam1.setText("Logged In");
        jPanelWkOne.add(jLabelGenLogNam1);
        jLabelGenLogNam1.setBounds(1090, 30, 100, 30);

        jLabelLineLogNam.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkOne.add(jLabelLineLogNam);
        jLabelLineLogNam.setBounds(1180, 30, 160, 30);

        jLabelLineTime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkOne.add(jLabelLineTime);
        jLabelLineTime.setBounds(1250, 0, 80, 30);

        jLabelLineDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkOne.add(jLabelLineDate);
        jLabelLineDate.setBounds(1090, 0, 110, 30);
        jPanelWkOne.add(jLabelProvince);
        jLabelProvince.setBounds(120, 120, 370, 30);
        jPanelWkOne.add(jLabelDistrict);
        jLabelDistrict.setBounds(670, 120, 360, 30);

        jPanelDooAttWk1Main.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDooAttWk1Main.setLayout(null);

        jPanelAttachWk1Main.setBackground(new java.awt.Color(255, 255, 255));
        jPanelAttachWk1Main.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelAttachWk1Main.setLayout(null);

        jLabelConfirmationWk1Main.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelConfirmationWk1Main.setText(" Enter Attachment Description below and select File");
        jLabelConfirmationWk1Main.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelAttachWk1Main.add(jLabelConfirmationWk1Main);
        jLabelConfirmationWk1Main.setBounds(0, 0, 280, 25);

        jTextAttDocDescWk1Main.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocDescWk1MainActionPerformed(evt);
            }
        });
        jPanelAttachWk1Main.add(jTextAttDocDescWk1Main);
        jTextAttDocDescWk1Main.setBounds(5, 30, 170, 25);

        jButtonSelectFileWk1Main.setText("Select File");
        jButtonSelectFileWk1Main.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectFileWk1MainActionPerformed(evt);
            }
        });
        jPanelAttachWk1Main.add(jButtonSelectFileWk1Main);
        jButtonSelectFileWk1Main.setBounds(180, 30, 95, 25);

        jTextAttDocNameWk1Main.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextAttDocNameWk1Main.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocNameWk1MainActionPerformed(evt);
            }
        });
        jPanelAttachWk1Main.add(jTextAttDocNameWk1Main);
        jTextAttDocNameWk1Main.setBounds(5, 60, 270, 25);

        jButtonWk1Add.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonWk1Add.setText("Add File");
        jButtonWk1Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk1AddActionPerformed(evt);
            }
        });
        jPanelAttachWk1Main.add(jButtonWk1Add);
        jButtonWk1Add.setBounds(30, 95, 73, 20);

        jButtonWk1Delete.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonWk1Delete.setText("Delete File");
        jButtonWk1Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk1DeleteActionPerformed(evt);
            }
        });
        jPanelAttachWk1Main.add(jButtonWk1Delete);
        jButtonWk1Delete.setBounds(170, 95, 85, 20);

        jPanelDooAttWk1Main.add(jPanelAttachWk1Main);
        jPanelAttachWk1Main.setBounds(0, 0, 280, 140);

        jTableDocAttWk1Main.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Description", "File Name", "File Path"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk1Att.setViewportView(jTableDocAttWk1Main);

        jPanelDooAttWk1Main.add(jScrollPaneWk1Att);
        jScrollPaneWk1Att.setBounds(280, 0, 460, 140);

        jPanelWkOne.add(jPanelDooAttWk1Main);
        jPanelDooAttWk1Main.setBounds(25, 530, 740, 140);

        jTextAttDocPathWk1Main.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocPathWk1MainActionPerformed(evt);
            }
        });
        jPanelWkOne.add(jTextAttDocPathWk1Main);
        jTextAttDocPathWk1Main.setBounds(210, 670, 230, 25);

        jPanelSave.setBackground(new java.awt.Color(255, 255, 255));
        jPanelSave.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelSave.setLayout(null);
        jPanelWkOne.add(jPanelSave);
        jPanelSave.setBounds(765, 530, 555, 140);

        jTabbedPane1.addTab("Week One", jPanelWkOne);

        jPanelWkTwo.setBackground(new java.awt.Color(95, 222, 184));
        jPanelWkTwo.setLayout(null);

        jLabelWkDuration1.setText("Week Duration");
        jPanelWkTwo.add(jLabelWkDuration1);
        jLabelWkDuration1.setBounds(30, 160, 90, 30);

        jDateChooserWk2From.setDateFormatString("yyyy-MM-dd");
        jPanelWkTwo.add(jDateChooserWk2From);
        jDateChooserWk2From.setBounds(210, 160, 120, 30);

        jLabelWk1From1.setText("From");
        jPanelWkTwo.add(jLabelWk1From1);
        jLabelWk1From1.setBounds(150, 160, 41, 30);

        jLabelWk1To1.setText("To");
        jPanelWkTwo.add(jLabelWk1To1);
        jLabelWk1To1.setBounds(390, 160, 41, 30);

        jDateChooserWk2To.setDateFormatString("yyyy-MM-dd");
        jPanelWkTwo.add(jDateChooserWk2To);
        jDateChooserWk2To.setBounds(440, 160, 120, 30);

        jButtonWk2AddActivity.setBackground(new java.awt.Color(0, 153, 51));
        jButtonWk2AddActivity.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonWk2AddActivity.setForeground(new java.awt.Color(255, 255, 255));
        jButtonWk2AddActivity.setText("Add Activity");
        jButtonWk2AddActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk2AddActivityActionPerformed(evt);
            }
        });
        jPanelWkTwo.add(jButtonWk2AddActivity);
        jButtonWk2AddActivity.setBounds(820, 160, 150, 30);
        jPanelWkTwo.add(jSeparator4);
        jSeparator4.setBounds(30, 200, 1280, 10);
        jPanelWkTwo.add(jSeparator5);
        jSeparator5.setBounds(30, 150, 1280, 10);

        jLabelLogo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelWkTwo.add(jLabelLogo2);
        jLabelLogo2.setBounds(5, 10, 220, 115);

        jButtonWk2DelActivity.setBackground(new java.awt.Color(204, 0, 0));
        jButtonWk2DelActivity.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonWk2DelActivity.setForeground(new java.awt.Color(255, 255, 255));
        jButtonWk2DelActivity.setText("Delete Per Diem  Activity");
        jButtonWk2DelActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk2DelActivityActionPerformed(evt);
            }
        });
        jPanelWkTwo.add(jButtonWk2DelActivity);
        jButtonWk2DelActivity.setBounds(1030, 160, 210, 30);

        jLabelGenLogNam3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam3.setText("Logged In");
        jPanelWkTwo.add(jLabelGenLogNam3);
        jLabelGenLogNam3.setBounds(1090, 30, 100, 30);

        jLabelLineDate1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkTwo.add(jLabelLineDate1);
        jLabelLineDate1.setBounds(1090, 0, 110, 30);

        jLabelLineTime1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkTwo.add(jLabelLineTime1);
        jLabelLineTime1.setBounds(1250, 0, 80, 30);

        jLabelLineLogNam1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam1.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkTwo.add(jLabelLineLogNam1);
        jLabelLineLogNam1.setBounds(1180, 30, 160, 30);

        jLabelHeaderGen7.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen7.setText("MONTHLY  PLAN ");
        jPanelWkTwo.add(jLabelHeaderGen7);
        jLabelHeaderGen7.setBounds(450, 40, 420, 40);

        jPanelDooAttWk2Main.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDooAttWk2Main.setLayout(null);

        jPanelAttachWk1Main1.setBackground(new java.awt.Color(255, 255, 255));
        jPanelAttachWk1Main1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelAttachWk1Main1.setLayout(null);

        jLabelConfirmationWk2Main.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelConfirmationWk2Main.setText(" Enter Attachment Description below and select File");
        jLabelConfirmationWk2Main.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelAttachWk1Main1.add(jLabelConfirmationWk2Main);
        jLabelConfirmationWk2Main.setBounds(0, 0, 280, 25);

        jTextAttDocDescWk2Main.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocDescWk2MainActionPerformed(evt);
            }
        });
        jPanelAttachWk1Main1.add(jTextAttDocDescWk2Main);
        jTextAttDocDescWk2Main.setBounds(5, 30, 170, 25);

        jButtonSelectFileWk2Main.setText("Select File");
        jButtonSelectFileWk2Main.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectFileWk2MainActionPerformed(evt);
            }
        });
        jPanelAttachWk1Main1.add(jButtonSelectFileWk2Main);
        jButtonSelectFileWk2Main.setBounds(180, 30, 95, 25);

        jTextAttDocNameWk2Main.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextAttDocNameWk2Main.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocNameWk2MainActionPerformed(evt);
            }
        });
        jPanelAttachWk1Main1.add(jTextAttDocNameWk2Main);
        jTextAttDocNameWk2Main.setBounds(5, 60, 270, 25);

        jButtonWk2Add.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonWk2Add.setText("Add File");
        jButtonWk2Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk2AddActionPerformed(evt);
            }
        });
        jPanelAttachWk1Main1.add(jButtonWk2Add);
        jButtonWk2Add.setBounds(30, 95, 73, 20);

        jButtonWk2Delete.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonWk2Delete.setText("Delete File");
        jButtonWk2Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk2DeleteActionPerformed(evt);
            }
        });
        jPanelAttachWk1Main1.add(jButtonWk2Delete);
        jButtonWk2Delete.setBounds(170, 95, 85, 20);

        jPanelDooAttWk2Main.add(jPanelAttachWk1Main1);
        jPanelAttachWk1Main1.setBounds(0, 0, 280, 140);

        jTableDocAttWk2Main.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Description", "File Name", "File Path"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk2Att.setViewportView(jTableDocAttWk2Main);

        jPanelDooAttWk2Main.add(jScrollPaneWk2Att);
        jScrollPaneWk2Att.setBounds(280, 0, 460, 140);

        jPanelWkTwo.add(jPanelDooAttWk2Main);
        jPanelDooAttWk2Main.setBounds(30, 530, 740, 140);

        jTextAttDocPathWk2Main.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocPathWk2MainActionPerformed(evt);
            }
        });
        jPanelWkTwo.add(jTextAttDocPathWk2Main);
        jTextAttDocPathWk2Main.setBounds(210, 650, 230, 25);

        jPanelSave1.setBackground(new java.awt.Color(255, 255, 255));
        jPanelSave1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelSave1.setLayout(null);
        jPanelWkTwo.add(jPanelSave1);
        jPanelSave1.setBounds(765, 530, 555, 140);

        jTableWk2Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Account Code", "Donor", "Project Code GL", "Project Code Program", "Project Name Program", "Budget Line", "Budget Code", "Site to Visit", "Activity", "Justification for Choice of Activity ", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Staff Member 1", "Staff Member 2", "Staff Member 3", "Staff Member 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk2.setViewportView(jTableWk2Activities);

        jPanelWkTwo.add(jScrollPaneWk2);
        jScrollPaneWk2.setBounds(30, 220, 1290, 310);

        jTabbedPane1.addTab("Week Two", jPanelWkTwo);

        jPanelWkThree.setBackground(new java.awt.Color(29, 109, 222));
        jPanelWkThree.setLayout(null);

        jLabelWkDuration2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWkDuration2.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWkDuration2.setText("Week Duration");
        jPanelWkThree.add(jLabelWkDuration2);
        jLabelWkDuration2.setBounds(30, 160, 100, 30);

        jDateChooserWk3From.setDateFormatString("yyyy-MM-dd");
        jPanelWkThree.add(jDateChooserWk3From);
        jDateChooserWk3From.setBounds(210, 160, 120, 30);

        jLabelWk3From.setText("From");
        jPanelWkThree.add(jLabelWk3From);
        jLabelWk3From.setBounds(150, 160, 41, 30);

        jLabelWk3To.setText("To");
        jPanelWkThree.add(jLabelWk3To);
        jLabelWk3To.setBounds(390, 160, 41, 30);

        jDateChooserWk3To.setDateFormatString("yyyy-MM-dd");
        jPanelWkThree.add(jDateChooserWk3To);
        jDateChooserWk3To.setBounds(440, 160, 120, 30);

        jButtonWk3AddActivity.setBackground(new java.awt.Color(0, 153, 51));
        jButtonWk3AddActivity.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonWk3AddActivity.setForeground(new java.awt.Color(255, 255, 255));
        jButtonWk3AddActivity.setText("Add Activity");
        jButtonWk3AddActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk3AddActivityActionPerformed(evt);
            }
        });
        jPanelWkThree.add(jButtonWk3AddActivity);
        jButtonWk3AddActivity.setBounds(810, 160, 150, 30);
        jPanelWkThree.add(jSeparator7);
        jSeparator7.setBounds(30, 200, 1280, 10);
        jPanelWkThree.add(jSeparator8);
        jSeparator8.setBounds(30, 150, 1280, 10);

        jLabelLogo3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelWkThree.add(jLabelLogo3);
        jLabelLogo3.setBounds(5, 10, 220, 115);

        jButtonWk3DelActivity.setBackground(new java.awt.Color(204, 0, 0));
        jButtonWk3DelActivity.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonWk3DelActivity.setForeground(new java.awt.Color(255, 255, 255));
        jButtonWk3DelActivity.setText("Delete Per Diem  Activity");
        jButtonWk3DelActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk3DelActivityActionPerformed(evt);
            }
        });
        jPanelWkThree.add(jButtonWk3DelActivity);
        jButtonWk3DelActivity.setBounds(1030, 160, 210, 30);

        jLabelGenLogNam5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam5.setForeground(new java.awt.Color(255, 255, 255));
        jLabelGenLogNam5.setText("Logged In");
        jPanelWkThree.add(jLabelGenLogNam5);
        jLabelGenLogNam5.setBounds(1090, 30, 100, 30);

        jLabelLineLogNam2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkThree.add(jLabelLineLogNam2);
        jLabelLineLogNam2.setBounds(1180, 30, 160, 30);

        jLabelLineTime2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkThree.add(jLabelLineTime2);
        jLabelLineTime2.setBounds(1250, 0, 80, 30);

        jLabelLineDate2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkThree.add(jLabelLineDate2);
        jLabelLineDate2.setBounds(1090, 0, 110, 30);

        jLabelHeaderGen8.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen8.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeaderGen8.setText("MONTHLY  PLAN ");
        jPanelWkThree.add(jLabelHeaderGen8);
        jLabelHeaderGen8.setBounds(450, 40, 420, 40);

        jPanelDooAttWk3Main.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDooAttWk3Main.setLayout(null);

        jPanelAttachWk3Main.setBackground(new java.awt.Color(255, 255, 255));
        jPanelAttachWk3Main.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelAttachWk3Main.setLayout(null);

        jLabelConfirmationWk3Main.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelConfirmationWk3Main.setText(" Enter Attachment Description below and select File");
        jLabelConfirmationWk3Main.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelAttachWk3Main.add(jLabelConfirmationWk3Main);
        jLabelConfirmationWk3Main.setBounds(0, 0, 280, 25);

        jTextAttDocDescWk3Main.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocDescWk3MainActionPerformed(evt);
            }
        });
        jPanelAttachWk3Main.add(jTextAttDocDescWk3Main);
        jTextAttDocDescWk3Main.setBounds(5, 30, 170, 25);

        jButtonSelectFileWk3Main.setText("Select File");
        jButtonSelectFileWk3Main.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectFileWk3MainActionPerformed(evt);
            }
        });
        jPanelAttachWk3Main.add(jButtonSelectFileWk3Main);
        jButtonSelectFileWk3Main.setBounds(180, 30, 95, 25);

        jTextAttDocNameWk3Main.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextAttDocNameWk3Main.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocNameWk3MainActionPerformed(evt);
            }
        });
        jPanelAttachWk3Main.add(jTextAttDocNameWk3Main);
        jTextAttDocNameWk3Main.setBounds(5, 60, 270, 25);

        jButtonWk3Add.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonWk3Add.setText("Add File");
        jButtonWk3Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk3AddActionPerformed(evt);
            }
        });
        jPanelAttachWk3Main.add(jButtonWk3Add);
        jButtonWk3Add.setBounds(30, 95, 73, 20);

        jButtonWk3Delete.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonWk3Delete.setText("Delete File");
        jButtonWk3Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk3DeleteActionPerformed(evt);
            }
        });
        jPanelAttachWk3Main.add(jButtonWk3Delete);
        jButtonWk3Delete.setBounds(170, 95, 85, 20);

        jPanelDooAttWk3Main.add(jPanelAttachWk3Main);
        jPanelAttachWk3Main.setBounds(0, 0, 280, 140);

        jTableDocAttWk3Main.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Description", "File Name", "File Path"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk3Att.setViewportView(jTableDocAttWk3Main);

        jPanelDooAttWk3Main.add(jScrollPaneWk3Att);
        jScrollPaneWk3Att.setBounds(280, 0, 460, 140);

        jPanelWkThree.add(jPanelDooAttWk3Main);
        jPanelDooAttWk3Main.setBounds(30, 530, 740, 140);

        jTextAttDocPathWk3Main.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocPathWk3MainActionPerformed(evt);
            }
        });
        jPanelWkThree.add(jTextAttDocPathWk3Main);
        jTextAttDocPathWk3Main.setBounds(210, 650, 230, 25);

        jPanelSave2.setBackground(new java.awt.Color(255, 255, 255));
        jPanelSave2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelSave2.setLayout(null);
        jPanelWkThree.add(jPanelSave2);
        jPanelSave2.setBounds(765, 530, 555, 140);

        jTableWk3Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Account Code", "Donor", "Project Code GL", "Project Code Program", "Project Name Program", "Budget Line", "Budget Code", "Site to Visit", "Activity", "Justification for Choice of Activity ", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Staff Member 1", "Staff Member 2", "Staff Member 3", "Staff Member 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk3.setViewportView(jTableWk3Activities);

        jPanelWkThree.add(jScrollPaneWk3);
        jScrollPaneWk3.setBounds(30, 220, 1290, 310);

        jTabbedPane1.addTab("Week Three", jPanelWkThree);

        jPanelWkFour.setBackground(new java.awt.Color(255, 204, 204));
        jPanelWkFour.setLayout(null);

        jLabelWkDuration4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWkDuration4.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWkDuration4.setText("Week Duration");
        jPanelWkFour.add(jLabelWkDuration4);
        jLabelWkDuration4.setBounds(30, 160, 100, 30);

        jDateChooserWk4From.setDateFormatString("yyyy-MM-dd");
        jPanelWkFour.add(jDateChooserWk4From);
        jDateChooserWk4From.setBounds(210, 160, 120, 30);

        jLabelWk4From.setText("From");
        jPanelWkFour.add(jLabelWk4From);
        jLabelWk4From.setBounds(150, 160, 41, 30);

        jLabelWk4To.setText("To");
        jPanelWkFour.add(jLabelWk4To);
        jLabelWk4To.setBounds(390, 160, 41, 30);

        jDateChooserWk4To.setDateFormatString("yyyy-MM-dd");
        jPanelWkFour.add(jDateChooserWk4To);
        jDateChooserWk4To.setBounds(440, 160, 120, 30);

        jButtonWk4AddActivity.setBackground(new java.awt.Color(0, 153, 51));
        jButtonWk4AddActivity.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonWk4AddActivity.setForeground(new java.awt.Color(255, 255, 255));
        jButtonWk4AddActivity.setText("Add Activity");
        jButtonWk4AddActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk4AddActivityActionPerformed(evt);
            }
        });
        jPanelWkFour.add(jButtonWk4AddActivity);
        jButtonWk4AddActivity.setBounds(820, 160, 150, 30);
        jPanelWkFour.add(jSeparator37);
        jSeparator37.setBounds(30, 200, 1280, 10);
        jPanelWkFour.add(jSeparator38);
        jSeparator38.setBounds(30, 150, 1280, 10);

        jLabelLogo4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelWkFour.add(jLabelLogo4);
        jLabelLogo4.setBounds(5, 10, 220, 115);

        jButtonWk4DelActivity.setBackground(new java.awt.Color(204, 0, 0));
        jButtonWk4DelActivity.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonWk4DelActivity.setForeground(new java.awt.Color(255, 255, 255));
        jButtonWk4DelActivity.setText("Delete Per Diem  Activity");
        jButtonWk4DelActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk4DelActivityActionPerformed(evt);
            }
        });
        jPanelWkFour.add(jButtonWk4DelActivity);
        jButtonWk4DelActivity.setBounds(1030, 160, 220, 30);

        jLabelGenLogNam6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam6.setForeground(new java.awt.Color(255, 255, 255));
        jLabelGenLogNam6.setText("Logged In");
        jPanelWkFour.add(jLabelGenLogNam6);
        jLabelGenLogNam6.setBounds(1090, 30, 100, 30);

        jLabelLineLogNam3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam3.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFour.add(jLabelLineLogNam3);
        jLabelLineLogNam3.setBounds(1180, 30, 160, 30);

        jLabelLineTime3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime3.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFour.add(jLabelLineTime3);
        jLabelLineTime3.setBounds(1250, 0, 80, 30);

        jLabelLineDate3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate3.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFour.add(jLabelLineDate3);
        jLabelLineDate3.setBounds(1090, 0, 110, 30);

        jLabelHeaderGen9.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen9.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeaderGen9.setText("MONTHLY  PLAN ");
        jPanelWkFour.add(jLabelHeaderGen9);
        jLabelHeaderGen9.setBounds(450, 40, 420, 40);

        jTextAttDocPathWk4Main.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocPathWk4MainActionPerformed(evt);
            }
        });
        jPanelWkFour.add(jTextAttDocPathWk4Main);
        jTextAttDocPathWk4Main.setBounds(210, 650, 230, 25);

        jPanelDooAttWk4Main.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDooAttWk4Main.setLayout(null);

        jPanelAttachWk4Main.setBackground(new java.awt.Color(255, 255, 255));
        jPanelAttachWk4Main.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelAttachWk4Main.setLayout(null);

        jLabelConfirmationWk4Main.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelConfirmationWk4Main.setText(" Enter Attachment Description below and select File");
        jLabelConfirmationWk4Main.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelAttachWk4Main.add(jLabelConfirmationWk4Main);
        jLabelConfirmationWk4Main.setBounds(0, 0, 280, 25);

        jTextAttDocDescWk4Main.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocDescWk4MainActionPerformed(evt);
            }
        });
        jPanelAttachWk4Main.add(jTextAttDocDescWk4Main);
        jTextAttDocDescWk4Main.setBounds(5, 30, 170, 25);

        jButtonSelectFileWk4Main.setText("Select File");
        jButtonSelectFileWk4Main.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectFileWk4MainActionPerformed(evt);
            }
        });
        jPanelAttachWk4Main.add(jButtonSelectFileWk4Main);
        jButtonSelectFileWk4Main.setBounds(180, 30, 95, 25);

        jTextAttDocNameWk4Main.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextAttDocNameWk4Main.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocNameWk4MainActionPerformed(evt);
            }
        });
        jPanelAttachWk4Main.add(jTextAttDocNameWk4Main);
        jTextAttDocNameWk4Main.setBounds(5, 60, 270, 25);

        jButtonWk4Add.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonWk4Add.setText("Add File");
        jButtonWk4Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk4AddActionPerformed(evt);
            }
        });
        jPanelAttachWk4Main.add(jButtonWk4Add);
        jButtonWk4Add.setBounds(30, 95, 73, 20);

        jButtonWk4Delete.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonWk4Delete.setText("Delete File");
        jButtonWk4Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk4DeleteActionPerformed(evt);
            }
        });
        jPanelAttachWk4Main.add(jButtonWk4Delete);
        jButtonWk4Delete.setBounds(170, 95, 85, 20);

        jPanelDooAttWk4Main.add(jPanelAttachWk4Main);
        jPanelAttachWk4Main.setBounds(0, 0, 280, 140);

        jTableDocAttWk4Main.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Description", "File Name", "File Path"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk4Att.setViewportView(jTableDocAttWk4Main);

        jPanelDooAttWk4Main.add(jScrollPaneWk4Att);
        jScrollPaneWk4Att.setBounds(280, 0, 460, 140);

        jPanelWkFour.add(jPanelDooAttWk4Main);
        jPanelDooAttWk4Main.setBounds(30, 530, 740, 140);

        jPanelSave3.setBackground(new java.awt.Color(255, 255, 255));
        jPanelSave3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelSave3.setLayout(null);
        jPanelWkFour.add(jPanelSave3);
        jPanelSave3.setBounds(765, 530, 555, 140);

        jTableWk4Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Account Code", "Donor", "Project Code GL", "Project Code Program", "Project Name Program", "Budget Line", "Budget Code", "Site to Visit", "Activity", "Justification for Choice of Activity ", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Staff Member 1", "Staff Member 2", "Staff Member 3", "Staff Member 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk4.setViewportView(jTableWk4Activities);

        jPanelWkFour.add(jScrollPaneWk4);
        jScrollPaneWk4.setBounds(30, 220, 1290, 310);

        jTabbedPane1.addTab("Week Four", jPanelWkFour);

        jPanelWkFive.setBackground(new java.awt.Color(204, 204, 255));
        jPanelWkFive.setLayout(null);

        jLabelWkDuration5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWkDuration5.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWkDuration5.setText("Week Duration");
        jPanelWkFive.add(jLabelWkDuration5);
        jLabelWkDuration5.setBounds(30, 160, 100, 30);

        jDateChooserWk5From.setDateFormatString("yyyy-MM-dd");
        jPanelWkFive.add(jDateChooserWk5From);
        jDateChooserWk5From.setBounds(210, 160, 120, 30);

        jLabelWk5From.setText("From");
        jPanelWkFive.add(jLabelWk5From);
        jLabelWk5From.setBounds(150, 160, 41, 30);

        jLabelWk5To.setText("To");
        jPanelWkFive.add(jLabelWk5To);
        jLabelWk5To.setBounds(390, 160, 41, 30);

        jDateChooserWk5To.setDateFormatString("yyyy-MM-dd");
        jPanelWkFive.add(jDateChooserWk5To);
        jDateChooserWk5To.setBounds(440, 160, 120, 30);

        jButtonWk5AddActivity.setBackground(new java.awt.Color(0, 153, 51));
        jButtonWk5AddActivity.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonWk5AddActivity.setForeground(new java.awt.Color(255, 255, 255));
        jButtonWk5AddActivity.setText("Add Activity");
        jButtonWk5AddActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk5AddActivityActionPerformed(evt);
            }
        });
        jPanelWkFive.add(jButtonWk5AddActivity);
        jButtonWk5AddActivity.setBounds(820, 160, 150, 30);
        jPanelWkFive.add(jSeparator39);
        jSeparator39.setBounds(30, 200, 1280, 10);
        jPanelWkFive.add(jSeparator40);
        jSeparator40.setBounds(30, 150, 1280, 10);

        jLabelLogo5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelWkFive.add(jLabelLogo5);
        jLabelLogo5.setBounds(5, 10, 220, 115);

        jButtonWk5DelActivity.setBackground(new java.awt.Color(204, 0, 0));
        jButtonWk5DelActivity.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButtonWk5DelActivity.setForeground(new java.awt.Color(255, 255, 255));
        jButtonWk5DelActivity.setText("Delete Per Diem  Activity");
        jButtonWk5DelActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk5DelActivityActionPerformed(evt);
            }
        });
        jPanelWkFive.add(jButtonWk5DelActivity);
        jButtonWk5DelActivity.setBounds(1030, 160, 220, 30);

        jLabelGenLogNam7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam7.setForeground(new java.awt.Color(255, 255, 255));
        jLabelGenLogNam7.setText("Logged In");
        jPanelWkFive.add(jLabelGenLogNam7);
        jLabelGenLogNam7.setBounds(1090, 30, 100, 30);

        jLabelLineLogNam4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam4.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFive.add(jLabelLineLogNam4);
        jLabelLineLogNam4.setBounds(1180, 30, 160, 30);

        jLabelLineTime4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime4.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFive.add(jLabelLineTime4);
        jLabelLineTime4.setBounds(1250, 0, 80, 30);

        jLabelLineDate4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate4.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFive.add(jLabelLineDate4);
        jLabelLineDate4.setBounds(1090, 0, 110, 30);

        jLabelHeaderGen10.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen10.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeaderGen10.setText("MONTHLY  PLAN ");
        jPanelWkFive.add(jLabelHeaderGen10);
        jLabelHeaderGen10.setBounds(450, 40, 420, 40);

        jPanelDooAttWk5Main.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDooAttWk5Main.setLayout(null);

        jPanelAttachWk5Main.setBackground(new java.awt.Color(255, 255, 255));
        jPanelAttachWk5Main.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelAttachWk5Main.setLayout(null);

        jLabelConfirmationWk5Main.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelConfirmationWk5Main.setText(" Enter Attachment Description below and select File");
        jLabelConfirmationWk5Main.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelAttachWk5Main.add(jLabelConfirmationWk5Main);
        jLabelConfirmationWk5Main.setBounds(0, 0, 280, 25);

        jTextAttDocDescWk5Main.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocDescWk5MainActionPerformed(evt);
            }
        });
        jPanelAttachWk5Main.add(jTextAttDocDescWk5Main);
        jTextAttDocDescWk5Main.setBounds(5, 30, 170, 25);

        jButtonSelectFileWk5Main.setText("Select File");
        jButtonSelectFileWk5Main.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectFileWk5MainActionPerformed(evt);
            }
        });
        jPanelAttachWk5Main.add(jButtonSelectFileWk5Main);
        jButtonSelectFileWk5Main.setBounds(180, 30, 95, 25);

        jTextAttDocNameWk5Main.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextAttDocNameWk5Main.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocNameWk5MainActionPerformed(evt);
            }
        });
        jPanelAttachWk5Main.add(jTextAttDocNameWk5Main);
        jTextAttDocNameWk5Main.setBounds(5, 60, 270, 25);

        jButtonWk5Add.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonWk5Add.setText("Add File");
        jButtonWk5Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk5AddActionPerformed(evt);
            }
        });
        jPanelAttachWk5Main.add(jButtonWk5Add);
        jButtonWk5Add.setBounds(30, 95, 73, 20);

        jButtonWk5Delete.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonWk5Delete.setText("Delete File");
        jButtonWk5Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWk5DeleteActionPerformed(evt);
            }
        });
        jPanelAttachWk5Main.add(jButtonWk5Delete);
        jButtonWk5Delete.setBounds(170, 95, 85, 20);

        jPanelDooAttWk5Main.add(jPanelAttachWk5Main);
        jPanelAttachWk5Main.setBounds(0, 0, 280, 140);

        jTableDocAttWk5Main.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Description", "File Name", "File Path"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk5Att.setViewportView(jTableDocAttWk5Main);

        jPanelDooAttWk5Main.add(jScrollPaneWk5Att);
        jScrollPaneWk5Att.setBounds(280, 0, 460, 140);

        jPanelWkFive.add(jPanelDooAttWk5Main);
        jPanelDooAttWk5Main.setBounds(30, 530, 740, 140);

        jTextAttDocPathWk5Main.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocPathWk5MainActionPerformed(evt);
            }
        });
        jPanelWkFive.add(jTextAttDocPathWk5Main);
        jTextAttDocPathWk5Main.setBounds(210, 650, 230, 25);

        jPanelSave4.setBackground(new java.awt.Color(255, 255, 255));
        jPanelSave4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelSave4.setLayout(null);
        jPanelWkFive.add(jPanelSave4);
        jPanelSave4.setBounds(765, 530, 555, 140);

        jTableWk5Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Account Code", "Donor", "Project Code GL", "Project Code Program", "Project Name Program", "Budget Line", "Budget Code", "Site to Visit", "Activity", "Justification for Choice of Activity ", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Staff Member 1", "Staff Member 2", "Staff Member 3", "Staff Member 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk5.setViewportView(jTableWk5Activities);

        jPanelWkFive.add(jScrollPaneWk5);
        jScrollPaneWk5.setBounds(30, 220, 1290, 310);

        jTabbedPane1.addTab("Week Five", jPanelWkFive);

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
        jMenuNew.add(jSeparator14);

        jMenuItemPerDiemAcquittal.setText("Acquittal - Per Diem ");
        jMenuItemPerDiemAcquittal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPerDiemAcquittalActionPerformed(evt);
            }
        });
        jMenuNew.add(jMenuItemPerDiemAcquittal);

        jMenuFile.add(jMenuNew);
        jMenuFile.add(jSeparator11);

        jMenuEdit.setText("Edit");

        jMenuMonPlanEdit.setText("Per Diem Monthly Plan");
        jMenuMonPlanEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuMonPlanEditActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuMonPlanEdit);

        jMenuFile.add(jMenuEdit);
        jMenuFile.add(jSeparator12);

        jMenuItemSubmit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSubmit.setText("Submit ");
        jMenuItemSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSubmitActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemSubmit);
        jMenuFile.add(jSeparator13);

        jMenuItemClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemClose.setText("Close");
        jMenuItemClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemClose);
        jMenuFile.add(jSeparator33);

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
        jMenuRequest.add(jSeparator9);

        jMenuItemAccMgrRev.setText("Finance Team Approval");
        jMenuItemAccMgrRev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAccMgrRevActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemAccMgrRev);
        jMenuRequest.add(jSeparator17);

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
        jMenuAcquittal.add(jSeparator18);

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
        jMenuAcquittal.add(jSeparator19);

        jMenuItemAcqSchGen.setText("Acquittals Schedule Generation");
        jMenuItemAcqSchGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqSchGenActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqSchGen);
        jMenuAcquittal.add(jSeparator20);

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
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 727, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1376, 786));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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

    private void jComboFacilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboFacilityActionPerformed

    }//GEN-LAST:event_jComboFacilityActionPerformed

    private void jTextFieldSearchNam1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldSearchNam1FocusGained

    }//GEN-LAST:event_jTextFieldSearchNam1FocusGained

    private void jTextFieldSearchNam1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldSearchNam1FocusLost

    }//GEN-LAST:event_jTextFieldSearchNam1FocusLost

    private void jTextFieldSearchNam1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldSearchNam1ActionPerformed

    }//GEN-LAST:event_jTextFieldSearchNam1ActionPerformed

    private void jButtonSearch1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearch1ActionPerformed

        searchName1();
    }//GEN-LAST:event_jButtonSearch1ActionPerformed

    private void jButtonOk1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOk1ActionPerformed

        String str = jComboBoxSearchResult1.getSelectedItem().toString();
        if ("Y".equals(staffName1)) {
            jTextFieldWk1DialogStaffName1.setText(str);
            jLabelWk1Name1Del.setVisible(true);
        }
        if ("Y".equals(staffName2)) {
            jTextFieldWk1DialogStaffName2.setText(str);
            jLabelWk1Name2Del.setVisible(true);
        }
        if ("Y".equals(staffName3)) {
            jTextFieldWk1DialogStaffName3.setText(str);
            jLabelWk1Name3Del.setVisible(true);
        }
        if ("Y".equals(staffName4)) {
            jTextFieldWk1DialogStaffName4.setText(str);
            jLabelWk1Name4Del.setVisible(true);
        }
        jTextFieldSearchNam1.setText("");

        jDialogSearchName1.dispose();

    }//GEN-LAST:event_jButtonOk1ActionPerformed

    private void jButtonWk1DelActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk1DelActivityActionPerformed
        DefaultTableModel model = (DefaultTableModel) this.jTableWk1Activities.getModel();

        int selectedOption = JOptionPane.showConfirmDialog(jTableWk1Activities,
                "Do you want to delete selected activity line?",
                "Choose",
                JOptionPane.YES_NO_OPTION);
        if (selectedOption == JOptionPane.YES_OPTION) {

            int[] rows = jTableWk1Activities.getSelectedRows();
            for (int i = 0; i < rows.length; i++) {
                model.removeRow(rows[i] - i);
            }
        } else {
            jButtonWk1DelActivity.requestFocusInWindow();
            jButtonWk1DelActivity.setFocusable(true);
        }
    }//GEN-LAST:event_jButtonWk1DelActivityActionPerformed

    private void jButtonWk1AddActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk1AddActivityActionPerformed
        try {
            jLabelWk1Name1Del.setVisible(false);
            jLabelWk1Name2Del.setVisible(false);
            jLabelWk1Name3Del.setVisible(false);
            jLabelWk1Name4Del.setVisible(false);
            findLastMonthDay();
            java.util.Date actPlanDate = jDateChooserWk1From.getDate();
            java.util.Date todayDate = new Date();
            long noOfDaysBetweenWk1 = 0;
            if ((!(jDateChooserWk1From.getDate() == null)) && (!(jDateChooserWk1To.getDate() == null))) {
                LocalDate dateBefore = LocalDate.parse(formatter.format(jDateChooserWk1From.getDate()));
                LocalDate dateAfter = LocalDate.parse(formatter.format(jDateChooserWk1To.getDate()));

                //calculating number of days in between
                noOfDaysBetweenWk1 = ChronoUnit.DAYS.between(dateBefore, dateAfter) + 1;
            }
            if (jDateChooserWk1From.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
                jDateChooserWk1From.requestFocusInWindow();
                jDateChooserWk1From.setFocusable(true);
            } else if (jDateChooserWk1To.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
                jDateChooserWk1To.requestFocusInWindow();
                jDateChooserWk1To.setFocusable(true);
//            } else if (formatter.format(jDateChooserWk1From.getDate()).compareTo(formatter.format(todayDate)) < 0) {
            } else if (formatter.format(jDateChooserWk1From.getDate()).compareTo(tH.internetDate) < 0) {
                JOptionPane.showMessageDialog(this, "Activity date cannot be lower than today's date.Please check your dates");
                jDateChooserWk1From.requestFocusInWindow();
                jDateChooserWk1From.setFocusable(true);

            } else if (jDateChooserWk1From.getDate().after(jDateChooserWk1To.getDate())) {
                JOptionPane.showMessageDialog(this, "End Date cannot be lower than start date.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk1From.setDate(null);
                jDateChooserWk1To.setDate(null);
                jDateChooserWk1From.requestFocusInWindow();
                jDateChooserWk1From.setFocusable(true);
            } else if (noOfDaysBetweenWk1 > 7) {
                JOptionPane.showMessageDialog(this, "Weekly Plan days cannot be more than seven(7) days here",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk1From.setDate(null);
                jDateChooserWk1To.setDate(null);
                jDateChooserWk1From.requestFocusInWindow();
                jDateChooserWk1From.setFocusable(true);
            } //            else if ((!"National Office".equals(jLabelDistrict.getText())) && (lastDateofMonth.compareTo(formatter.format(jDateChooserWk1To.getDate())) < 0)) {
            //                JOptionPane.showMessageDialog(this, "Activity date cannot be greater than last day of the month.Please check your dates");
            //                jDateChooserWk1From.requestFocusInWindow();
            //                jDateChooserWk1From.setFocusable(true);
            //            } 
            else {
                wk1Site = "Y";
                wk2Site = "N";
                wk3Site = "N";
                wk4Site = "N";
                wk5Site = "N";

//                if (("National Office".equals(jLabelDistrict.getText()))) {
                jDialogWk1.setTitle("Per Diem Week 1");
//                } else {
//                    jDialogWk1.setTitle("Month Per Diem ");
//                }
                resetField();
                refreshAttFields();
                modelAtt.setRowCount(0);
                jDialogWk1.setVisible(true);
                addStaff();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonWk1AddActivityActionPerformed

    private void jButtonWk2DelActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk2DelActivityActionPerformed
        DefaultTableModel modelWk2 = (DefaultTableModel) this.jTableWk2Activities.getModel();
        int selectedOption = JOptionPane.showConfirmDialog(jTableWk2Activities,
                "Do you want to delete selected activity line?",
                "Choose",
                JOptionPane.YES_NO_OPTION);
        if (selectedOption == JOptionPane.YES_OPTION) {

            int[] rows = jTableWk2Activities.getSelectedRows();
            for (int i = 0; i < rows.length; i++) {
                modelWk2.removeRow(rows[i] - i);
            }
        } else {
            jButtonWk2DelActivity.requestFocusInWindow();
            jButtonWk2DelActivity.setFocusable(true);
        }
    }//GEN-LAST:event_jButtonWk2DelActivityActionPerformed

    private void jButtonWk3AddActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk3AddActivityActionPerformed
        try {
            jLabelWk1Name1Del.setVisible(false);
            jLabelWk1Name2Del.setVisible(false);
            jLabelWk1Name3Del.setVisible(false);
            jLabelWk1Name4Del.setVisible(false);
            java.util.Date actPlanDate = jDateChooserWk3From.getDate();
            java.util.Date todayDate = new Date();

            long noOfDaysBetweenWk3 = 0;
            if ((!(jDateChooserWk3From.getDate() == null)) && (!(jDateChooserWk3To.getDate() == null))) {
                LocalDate dateBefore = LocalDate.parse(formatter.format(jDateChooserWk3From.getDate()));
                LocalDate dateAfter = LocalDate.parse(formatter.format(jDateChooserWk3To.getDate()));

                //calculating number of days in between
                noOfDaysBetweenWk3 = ChronoUnit.DAYS.between(dateBefore, dateAfter) + 1;
            }

            if ((jDateChooserWk2From.getDate() == null) || (jDateChooserWk2To.getDate() == null) || (jDateChooserWk1From.getDate() == null) || (jDateChooserWk1To.getDate() == null)) {
                JOptionPane.showMessageDialog(this, "Please complete week two before completing week three");
                jDateChooserWk3From.setDate(null);
                jDateChooserWk3To.setDate(null);
                jDateChooserWk1From.requestFocusInWindow();
                jDateChooserWk1From.setFocusable(true);
            } else if (jDateChooserWk3From.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
                jDateChooserWk3From.requestFocusInWindow();
                jDateChooserWk3From.setFocusable(true);
            } else if (formatter.format(jDateChooserWk3From.getDate()).compareTo(tH.internetDate) < 0) {
                JOptionPane.showMessageDialog(this, "Activity date cannot be lower than today's date. Please check your dates");
                jDateChooserWk3From.requestFocusInWindow();
                jDateChooserWk3From.setFocusable(true);
            } else if (jDateChooserWk3To.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
                jDateChooserWk3To.requestFocusInWindow();
                jDateChooserWk3To.setFocusable(true);
            } else if (jDateChooserWk3From.getDate().after(jDateChooserWk3To.getDate())) {
                JOptionPane.showMessageDialog(this, "End Date cannot be lower than start date.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk3From.setDate(null);
                jDateChooserWk3To.setDate(null);
                jDateChooserWk3From.requestFocusInWindow();
                jDateChooserWk3From.setFocusable(true);
            } else if (formatter.format(jDateChooserWk2From.getDate()).equals(formatter.format((jDateChooserWk3From.getDate())))) {
                JOptionPane.showMessageDialog(this, "Date cannot be equal to previous week.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk3From.setDate(null);
                jDateChooserWk3To.setDate(null);
                jDateChooserWk3From.requestFocusInWindow();
                jDateChooserWk3From.setFocusable(true);
            } else if (formatter.format(jDateChooserWk2To.getDate()).equals(formatter.format(jDateChooserWk3From.getDate()))) {
                JOptionPane.showMessageDialog(this, "Date cannot be equal to previous week.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk3From.setDate(null);
                jDateChooserWk3To.setDate(null);
                jDateChooserWk3From.requestFocusInWindow();
                jDateChooserWk3From.setFocusable(true);
            } else if (jDateChooserWk2To.getDate().after(jDateChooserWk3From.getDate())) {
                JOptionPane.showMessageDialog(this, "Date cannot be lower than the previous week.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk3From.setDate(null);
                jDateChooserWk3To.setDate(null);
                jDateChooserWk3From.requestFocusInWindow();
                jDateChooserWk3From.setFocusable(true);
            } else if (noOfDaysBetweenWk3 > 7) {
                JOptionPane.showMessageDialog(this, "Weekly Plan days cannot be more than seven(7) days",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk3From.setDate(null);
                jDateChooserWk3To.setDate(null);
                jDateChooserWk3From.requestFocusInWindow();
                jDateChooserWk3From.setFocusable(true);
            } else {
                wk1Site = "N";
                wk2Site = "N";
                wk3Site = "Y";
                wk4Site = "N";
                wk5Site = "N";

                jDialogWk1.setTitle("Per Diem Week 3");
                resetField();
                modelAtt.setRowCount(0);
                jDialogWk1.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonWk3AddActivityActionPerformed

    private void jButtonWk3DelActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk3DelActivityActionPerformed
        DefaultTableModel modelWk3 = (DefaultTableModel) this.jTableWk3Activities.getModel();
        int selectedOption = JOptionPane.showConfirmDialog(jTableWk3Activities,
                "Do you want to delete selected activity line?",
                "Choose",
                JOptionPane.YES_NO_OPTION);
        if (selectedOption == JOptionPane.YES_OPTION) {

            int[] rows = jTableWk3Activities.getSelectedRows();
            for (int i = 0; i < rows.length; i++) {
                modelWk3.removeRow(rows[i] - i);
            }
        } else {
            jButtonWk3DelActivity.requestFocusInWindow();
            jButtonWk3DelActivity.setFocusable(true);
        }
    }//GEN-LAST:event_jButtonWk3DelActivityActionPerformed

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

    private void jMenuItemSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSubmitActionPerformed
        regCheck();
    }//GEN-LAST:event_jMenuItemSubmitActionPerformed

    private void jComboFacilityMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboFacilityMouseReleased

    }//GEN-LAST:event_jComboFacilityMouseReleased

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

    private void jTextFieldWk1DialogStaffName4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName4MouseClicked
        staffName1 = "N";
        staffName2 = "N";
        staffName3 = "N";
        staffName4 = "Y";
        jDialogSearchName1.setVisible(true);
        jDialogSearchName1.setVisible(false);
        jDialogSearchName1.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk1DialogStaffName4MouseClicked

    private void jTextFieldWk1DialogStaffName4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName4ActionPerformed
        staffName1 = "N";
        staffName2 = "N";
        staffName3 = "N";
        staffName4 = "Y";
        jDialogSearchName1.setVisible(true);
        jDialogSearchName1.setVisible(false);
        jDialogSearchName1.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk1DialogStaffName4ActionPerformed

    private void jTextFieldWk1DialogStaffName4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName4KeyTyped
        staffName1 = "N";
        staffName2 = "N";
        staffName3 = "Y";
        staffName4 = "N";
        jDialogSearchName1.setVisible(true);
        jDialogSearchName1.setVisible(false);
        jDialogSearchName1.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk1DialogStaffName4KeyTyped

    private void jTextFieldWk1DialogStaffName1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName1MouseClicked
        staffName1 = "Y";
        staffName2 = "N";
        staffName3 = "N";
        staffName4 = "N";

        jDialogSearchName1.setVisible(true);
        jDialogSearchName1.setVisible(false);
        jDialogSearchName1.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk1DialogStaffName1MouseClicked

    private void jTextFieldWk1DialogStaffName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName1ActionPerformed

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName1ActionPerformed

    private void jTextFieldWk1DialogStaffName1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName1KeyTyped
        staffName1 = "Y";
        staffName2 = "N";
        staffName3 = "N";
        staffName4 = "N";

        jDialogSearchName1.setVisible(true);
        jDialogSearchName1.setVisible(false);
        jDialogSearchName1.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk1DialogStaffName1KeyTyped

    private void jTextFieldWk1DialogStaffName2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName2MouseClicked
        staffName1 = "N";
        staffName2 = "Y";
        staffName3 = "N";
        staffName4 = "N";
        jDialogSearchName1.setVisible(true);
        jDialogSearchName1.setVisible(false);
        jDialogSearchName1.setVisible(true);

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName2MouseClicked

    private void jTextFieldWk1DialogStaffName2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName2KeyTyped
        staffName1 = "N";
        staffName2 = "Y";
        staffName3 = "N";
        staffName4 = "N";
        jDialogSearchName1.setVisible(true);
        jDialogSearchName1.setVisible(false);
        jDialogSearchName1.setVisible(true);

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName2KeyTyped

    private void jTextFieldWk1DialogStaffName3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName3MouseClicked
        staffName1 = "N";
        staffName2 = "N";
        staffName3 = "Y";
        staffName4 = "N";
        jDialogSearchName1.setVisible(true);
        jDialogSearchName1.setVisible(false);
        jDialogSearchName1.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk1DialogStaffName3MouseClicked

    private void jTextFieldWk1DialogStaffName3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName3KeyTyped
        staffName1 = "N";
        staffName2 = "N";
        staffName3 = "Y";
        staffName4 = "N";
        jDialogSearchName1.setVisible(true);
        jDialogSearchName1.setVisible(false);
        jDialogSearchName1.setVisible(true);
    }//GEN-LAST:event_jTextFieldWk1DialogStaffName3KeyTyped

    private void jButtonDialogWk1ResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDialogWk1ResetActionPerformed
        resetField();
    }//GEN-LAST:event_jButtonDialogWk1ResetActionPerformed

    private void jButtonDialogWk1AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDialogWk1AddActionPerformed
//        addFileAttToTabWk1Main();
        budCreate();
        addWkItmLine();


    }//GEN-LAST:event_jButtonDialogWk1AddActionPerformed

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

    private void jButtonDialogWk1CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDialogWk1CloseActionPerformed
        wk1Site = "N";
        jDialogWk1.setVisible(false);
    }//GEN-LAST:event_jButtonDialogWk1CloseActionPerformed

    private void jLabelWk1Name4DelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelWk1Name4DelMouseClicked
        jTextFieldWk1DialogStaffName4.setText("");
        jLabelWk1Name4Del.setVisible(false);
    }//GEN-LAST:event_jLabelWk1Name4DelMouseClicked

    private void jLabelWk1Name1DelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelWk1Name1DelMouseClicked
        jTextFieldWk1DialogStaffName1.setText("");
        jLabelWk1Name1Del.setVisible(false);
    }//GEN-LAST:event_jLabelWk1Name1DelMouseClicked

    private void jLabelWk1Name2DelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelWk1Name2DelMouseClicked
        jTextFieldWk1DialogStaffName2.setText("");
        jLabelWk1Name2Del.setVisible(false);
    }//GEN-LAST:event_jLabelWk1Name2DelMouseClicked

    private void jLabelWk1Name3DelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelWk1Name3DelMouseClicked
        jTextFieldWk1DialogStaffName3.setText("");
        jLabelWk1Name3Del.setVisible(false);
    }//GEN-LAST:event_jLabelWk1Name3DelMouseClicked

    private void jButtonWk4AddActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk4AddActivityActionPerformed
        try {
            jLabelWk1Name1Del.setVisible(false);
            jLabelWk1Name2Del.setVisible(false);
            jLabelWk1Name3Del.setVisible(false);
            jLabelWk1Name4Del.setVisible(false);
            java.util.Date actPlanDate = jDateChooserWk4From.getDate();
            java.util.Date todayDate = new Date();

            long noOfDaysBetweenWk4 = 0;
            if ((!(jDateChooserWk4From.getDate() == null)) && (!(jDateChooserWk4To.getDate() == null))) {
                LocalDate dateBefore = LocalDate.parse(formatter.format(jDateChooserWk4From.getDate()));
                LocalDate dateAfter = LocalDate.parse(formatter.format(jDateChooserWk4To.getDate()));

                //calculating number of days in between
                noOfDaysBetweenWk4 = ChronoUnit.DAYS.between(dateBefore, dateAfter) + 1;
            }

            if ((jDateChooserWk3From.getDate() == null) || (jDateChooserWk3To.getDate() == null) || (jDateChooserWk2From.getDate() == null) || (jDateChooserWk2To.getDate() == null) || (jDateChooserWk1From.getDate() == null) || (jDateChooserWk1To.getDate() == null)) {
                JOptionPane.showMessageDialog(this, "Please complete week three before completing week four");
                jDateChooserWk4From.setDate(null);
                jDateChooserWk4To.setDate(null);
                jDateChooserWk4From.requestFocusInWindow();
                jDateChooserWk4From.setFocusable(true);
            } else if (jDateChooserWk4From.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
                jDateChooserWk4From.requestFocusInWindow();
                jDateChooserWk4From.setFocusable(true);
            } else if (formatter.format(jDateChooserWk4From.getDate()).compareTo(tH.internetDate) < 0) {
                JOptionPane.showMessageDialog(this, "Activity date cannot be lower than today's date. Please check your dates");
                jDateChooserWk4From.requestFocusInWindow();
                jDateChooserWk4From.setFocusable(true);
            } else if (jDateChooserWk4To.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
                jDateChooserWk4To.requestFocusInWindow();
                jDateChooserWk4To.setFocusable(true);
            } else if (jDateChooserWk4From.getDate().after(jDateChooserWk4To.getDate())) {
                JOptionPane.showMessageDialog(this, "End Date cannot be lower than start date.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk4From.setDate(null);
                jDateChooserWk4To.setDate(null);
                jDateChooserWk4From.requestFocusInWindow();
                jDateChooserWk4From.setFocusable(true);
            } else if (formatter.format(jDateChooserWk3From.getDate()).equals(formatter.format((jDateChooserWk4From.getDate())))) {
                JOptionPane.showMessageDialog(this, "Date cannot be equal to previous week.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk4From.setDate(null);
                jDateChooserWk4To.setDate(null);
                jDateChooserWk4From.requestFocusInWindow();
                jDateChooserWk4From.setFocusable(true);
            } else if (formatter.format(jDateChooserWk3To.getDate()).equals(formatter.format(jDateChooserWk4From.getDate()))) {
                JOptionPane.showMessageDialog(this, "Date cannot be equal to previous week.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk4From.setDate(null);
                jDateChooserWk4To.setDate(null);
                jDateChooserWk4From.requestFocusInWindow();
                jDateChooserWk4From.setFocusable(true);
            } else if (jDateChooserWk3To.getDate().after(jDateChooserWk4From.getDate())) {
                JOptionPane.showMessageDialog(this, "Date cannot be lower than the previous week.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk4From.setDate(null);
                jDateChooserWk4To.setDate(null);
                jDateChooserWk4From.requestFocusInWindow();
                jDateChooserWk4From.setFocusable(true);
            } else if (noOfDaysBetweenWk4 > 7) {
                JOptionPane.showMessageDialog(this, "Weekly Plan days cannot be more than seven(7) days",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk4From.setDate(null);
                jDateChooserWk4To.setDate(null);
                jDateChooserWk4From.requestFocusInWindow();
                jDateChooserWk4From.setFocusable(true);
            } else {
                wk1Site = "N";
                wk2Site = "N";
                wk3Site = "N";
                wk4Site = "Y";
                wk5Site = "N";

                jDialogWk1.setTitle("Per Diem Week 4");
                resetField();
                modelAtt.setRowCount(0);
                jDialogWk1.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonWk4AddActivityActionPerformed

    private void jButtonWk4DelActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk4DelActivityActionPerformed
        DefaultTableModel modelWk4 = (DefaultTableModel) this.jTableWk4Activities.getModel();
        int selectedOption = JOptionPane.showConfirmDialog(jTableWk4Activities,
                "Do you want to delete selected activity line?",
                "Choose",
                JOptionPane.YES_NO_OPTION);
        if (selectedOption == JOptionPane.YES_OPTION) {

            int[] rows = jTableWk4Activities.getSelectedRows();
            for (int i = 0; i < rows.length; i++) {
                modelWk4.removeRow(rows[i] - i);
            }
        } else {
            jButtonWk4DelActivity.requestFocusInWindow();
            jButtonWk4DelActivity.setFocusable(true);
        }
    }//GEN-LAST:event_jButtonWk4DelActivityActionPerformed

    private void jButtonWk5AddActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk5AddActivityActionPerformed
        try {
            jLabelWk1Name1Del.setVisible(false);
            jLabelWk1Name2Del.setVisible(false);
            jLabelWk1Name3Del.setVisible(false);
            jLabelWk1Name4Del.setVisible(false);
            java.util.Date actPlanDate = jDateChooserWk5From.getDate();
            java.util.Date todayDate = new Date();

            long noOfDaysBetweenWk5 = 0;
            if ((!(jDateChooserWk5From.getDate() == null)) && (!(jDateChooserWk5To.getDate() == null))) {
                LocalDate dateBefore = LocalDate.parse(formatter.format(jDateChooserWk5From.getDate()));
                LocalDate dateAfter = LocalDate.parse(formatter.format(jDateChooserWk5To.getDate()));

                //calculating number of days in between
                noOfDaysBetweenWk5 = ChronoUnit.DAYS.between(dateBefore, dateAfter) + 1;
            }

            if ((jDateChooserWk4From.getDate() == null) || (jDateChooserWk4To.getDate() == null) || (jDateChooserWk3From.getDate() == null) || (jDateChooserWk3To.getDate() == null) || (jDateChooserWk2From.getDate() == null) || (jDateChooserWk2To.getDate() == null) || (jDateChooserWk2From.getDate() == null) || (jDateChooserWk2To.getDate() == null)) {
                JOptionPane.showMessageDialog(this, "Please complete week four before completing week five");
                jDateChooserWk5From.setDate(null);
                jDateChooserWk5To.setDate(null);
                jDateChooserWk5From.requestFocusInWindow();
                jDateChooserWk5From.setFocusable(true);
            } else if (jDateChooserWk5From.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
                jDateChooserWk5From.requestFocusInWindow();
                jDateChooserWk5From.setFocusable(true);
            } else if (formatter.format(jDateChooserWk5From.getDate()).compareTo(tH.internetDate) < 0) {
                JOptionPane.showMessageDialog(this, "Activity date cannot be lower than today's date. Please check your dates");
                jDateChooserWk5From.requestFocusInWindow();
                jDateChooserWk5From.setFocusable(true);
            } else if (jDateChooserWk5To.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
                jDateChooserWk5To.requestFocusInWindow();
                jDateChooserWk5To.setFocusable(true);
            } else if (jDateChooserWk5From.getDate().after(jDateChooserWk5To.getDate())) {
                JOptionPane.showMessageDialog(this, "End Date cannot be lower than start date.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk5From.setDate(null);
                jDateChooserWk5To.setDate(null);
                jDateChooserWk5From.requestFocusInWindow();
                jDateChooserWk5From.setFocusable(true);
            } else if (formatter.format(jDateChooserWk5From.getDate()).equals(formatter.format((jDateChooserWk4From.getDate())))) {
                JOptionPane.showMessageDialog(this, "Date cannot be equal to previous week.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk5From.setDate(null);
                jDateChooserWk5To.setDate(null);
                jDateChooserWk5From.requestFocusInWindow();
                jDateChooserWk5From.setFocusable(true);
            } else if (jDateChooserWk4To.getDate().after(jDateChooserWk5From.getDate())) {
                JOptionPane.showMessageDialog(this, "Date cannot be lower than the previous week.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk5From.setDate(null);
                jDateChooserWk5To.setDate(null);
                jDateChooserWk5From.requestFocusInWindow();
                jDateChooserWk5From.setFocusable(true);
            } else if (noOfDaysBetweenWk5 > 7) {
                JOptionPane.showMessageDialog(this, "Weekly Plan days cannot be more than seven(7) days",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk5From.setDate(null);
                jDateChooserWk5To.setDate(null);
                jDateChooserWk5From.requestFocusInWindow();
                jDateChooserWk5From.setFocusable(true);
            } else {
                wk1Site = "N";
                wk2Site = "N";
                wk3Site = "N";
                wk4Site = "N";
                wk5Site = "Y";

                jDialogWk1.setTitle("Per Diem Week 5");
                resetField();
                modelAtt.setRowCount(0);
                jDialogWk1.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonWk5AddActivityActionPerformed

    private void jButtonWk5DelActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk5DelActivityActionPerformed
        DefaultTableModel modelWk5 = (DefaultTableModel) this.jTableWk5Activities.getModel();
        int selectedOption = JOptionPane.showConfirmDialog(jTableWk5Activities,
                "Do you want to delete selected activity line?",
                "Choose",
                JOptionPane.YES_NO_OPTION);
        if (selectedOption == JOptionPane.YES_OPTION) {

            int[] rows = jTableWk5Activities.getSelectedRows();
            for (int i = 0; i < rows.length; i++) {
                modelWk5.removeRow(rows[i] - i);
            }
        } else {
            jButtonWk5DelActivity.requestFocusInWindow();
            jButtonWk5DelActivity.setFocusable(true);
        }
    }//GEN-LAST:event_jButtonWk5DelActivityActionPerformed

    private void jTextFieldWk1MiscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWk1MiscActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldWk1MiscActionPerformed

    private void jButtonWk2AddActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk2AddActivityActionPerformed
        try {
            java.util.Date actPlanDate = jDateChooserWk2From.getDate();
            java.util.Date todayDate = new Date();

            long noOfDaysBetweenWk2 = 0;
            if ((!(jDateChooserWk2From.getDate() == null)) && (!(jDateChooserWk2To.getDate() == null))) {
                LocalDate dateBefore = LocalDate.parse(formatter.format(jDateChooserWk2From.getDate()));
                LocalDate dateAfter = LocalDate.parse(formatter.format(jDateChooserWk2To.getDate()));

                //calculating number of days in between
                noOfDaysBetweenWk2 = ChronoUnit.DAYS.between(dateBefore, dateAfter) + 1;
            }

            if (jDateChooserWk2From.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
                jDateChooserWk2From.requestFocusInWindow();
                jDateChooserWk2From.setFocusable(true);
            } else if (formatter.format(jDateChooserWk2From.getDate()).compareTo(tH.internetDate) < 0) {
                JOptionPane.showMessageDialog(this, "Activity date cannot be lower than today's date. Please check your dates");
                jDateChooserWk2From.requestFocusInWindow();
                jDateChooserWk2From.setFocusable(true);
            } else if ((jDateChooserWk1From.getDate() == null) || (jDateChooserWk1To.getDate() == null)) {
                JOptionPane.showMessageDialog(this, "Please complete week one before completing week two");
                jDateChooserWk2From.setDate(null);
                jDateChooserWk2To.setDate(null);
                jDateChooserWk1From.requestFocusInWindow();
                jDateChooserWk1From.setFocusable(true);
            } else if (jDateChooserWk2To.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Date cannot be blank. Please check your dates");
                jDateChooserWk2To.requestFocusInWindow();
                jDateChooserWk2To.setFocusable(true);
            } else if (jDateChooserWk2From.getDate().after(jDateChooserWk2To.getDate())) {
                JOptionPane.showMessageDialog(this, "End Date cannot be lower than start date.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk2From.setDate(null);
                jDateChooserWk2To.setDate(null);
                jDateChooserWk2From.requestFocusInWindow();
                jDateChooserWk2From.setFocusable(true);
            } else if (jDateChooserWk1To.getDate().after(jDateChooserWk2From.getDate())) {
                JOptionPane.showMessageDialog(this, "Date cannot be lower than the previous week.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk2From.setDate(null);
                jDateChooserWk2To.setDate(null);
                jDateChooserWk2From.requestFocusInWindow();
                jDateChooserWk2From.setFocusable(true);
            } else if (formatter.format(jDateChooserWk1From.getDate()).equals(formatter.format((jDateChooserWk2From.getDate())))) {
                JOptionPane.showMessageDialog(this, "Date cannot be equal to previous week.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk2From.setDate(null);
                jDateChooserWk2To.setDate(null);
                jDateChooserWk2From.requestFocusInWindow();
                jDateChooserWk2From.setFocusable(true);
            } else if (formatter.format(jDateChooserWk1To.getDate()).equals(formatter.format(jDateChooserWk2From.getDate()))) {
                JOptionPane.showMessageDialog(this, "Date cannot be equal to previous week.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk2From.setDate(null);
                jDateChooserWk2To.setDate(null);
                jDateChooserWk2From.requestFocusInWindow();
                jDateChooserWk2From.setFocusable(true);
            } else if (noOfDaysBetweenWk2 > 7) {
                JOptionPane.showMessageDialog(this, "Weekly Plan days cannot be more than seven(7) days",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateChooserWk2From.setDate(null);
                jDateChooserWk2To.setDate(null);
                jDateChooserWk2From.requestFocusInWindow();
                jDateChooserWk2From.setFocusable(true);
            } else {

                wk1Site = "N";
                wk2Site = "Y";
                wk3Site = "N";
                wk4Site = "N";
                wk5Site = "N";
                jDialogWk1.setTitle("Per Diem Week 2");
                resetField();
                modelAtt.setRowCount(0);
                jDialogWk1.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonWk2AddActivityActionPerformed

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

    private void jToggleButtonMOHCCConfirmationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonMOHCCConfirmationActionPerformed
        deleteFileAtt();
    }//GEN-LAST:event_jToggleButtonMOHCCConfirmationActionPerformed

    private void jToggleButtonMOHCCConfirmation2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonMOHCCConfirmation2ActionPerformed
        if ("".equals(jTextAttDocDesc.getText())) {
            JOptionPane.showMessageDialog(jDialogWk1, "Please enter attached file description.");
            jTextAttDocDesc.requestFocusInWindow();
            jTextAttDocDesc.setFocusable(true);
        } else if ("".equals(jTextAttDocName.getText())) {
            JOptionPane.showMessageDialog(this, "Please note that no file has been selected.");
            jTextAttDocName.requestFocusInWindow();
            jTextAttDocName.setFocusable(true);
        } else {
            addFileAtt();
            refreshAttFields();

        }
    }//GEN-LAST:event_jToggleButtonMOHCCConfirmation2ActionPerformed

    private void jTextAttDocDescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocDescActionPerformed

    }//GEN-LAST:event_jTextAttDocDescActionPerformed

    private void jButtonSelectFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectFileActionPerformed

        savePDFToDB pdfDB = new savePDFToDB();
        pdfDB.pdfChooser();
        String attFileName = pdfDB.fileName;

        File file = new File(attFileName);
        long attFileLength = file.length();
        if (attFileLength < 512000) {
            jTextAttDocName.setText(file.getName());
            jTextAttDocPath.setText(attFileName);

        } else if (attFileLength > 512000) {
            JOptionPane.showMessageDialog(this, "File size cannot be greater than 512Kb. ");
        }
    }//GEN-LAST:event_jButtonSelectFileActionPerformed

    private void jTextAttDocNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocNameActionPerformed

    }//GEN-LAST:event_jTextAttDocNameActionPerformed

    private void jTextAttDocPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocPathActionPerformed

    }//GEN-LAST:event_jTextAttDocPathActionPerformed

    private void jTextAttDocDescWk1MainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocDescWk1MainActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocDescWk1MainActionPerformed

    private void jButtonSelectFileWk1MainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectFileWk1MainActionPerformed
        if (modelWk1.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "File attachment is not possible if activities have not been added. Please add activities first. ");
        } else {
            savePDFToDB pdfDB = new savePDFToDB();
            pdfDB.pdfChooser();
            String attFileName = pdfDB.fileName;

            File file = new File(attFileName);
            long attFileLength = file.length();
            if (attFileLength < 512000) {
                jTextAttDocNameWk1Main.setText(file.getName());
                jTextAttDocPathWk1Main.setText(attFileName);

            } else if (attFileLength > 512000) {
                JOptionPane.showMessageDialog(this, "File size cannot be greater than 512Kb. ");
            }
        }
    }//GEN-LAST:event_jButtonSelectFileWk1MainActionPerformed

    private void jTextAttDocNameWk1MainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocNameWk1MainActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocNameWk1MainActionPerformed

    private void jTextAttDocPathWk1MainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocPathWk1MainActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocPathWk1MainActionPerformed

    private void jButtonWk1AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk1AddActionPerformed
        if ("".equals(jTextAttDocDescWk1Main.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter attached file description.");
            jTextAttDocDescWk1Main.requestFocusInWindow();
            jTextAttDocDescWk1Main.setFocusable(true);
        } else if ("".equals(jTextAttDocNameWk1Main.getText())) {
            JOptionPane.showMessageDialog(this, "Please note that no file has been selected.");
            jTextAttDocNameWk1Main.requestFocusInWindow();
            jTextAttDocNameWk1Main.setFocusable(true);
        } else {
            addFileAttWk1Main();
            refreshAttFields();

        }

    }//GEN-LAST:event_jButtonWk1AddActionPerformed

    private void jButtonWk1DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk1DeleteActionPerformed
        deleteFileAttWk1Main();
    }//GEN-LAST:event_jButtonWk1DeleteActionPerformed

    private void jTextAttDocDescWk2MainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocDescWk2MainActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocDescWk2MainActionPerformed

    private void jButtonSelectFileWk2MainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectFileWk2MainActionPerformed
        if (modelWk2.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "File attachment is not possible if activities have not been added. Please add activities first. ");
        } else {
            savePDFToDB pdfDB = new savePDFToDB();
            pdfDB.pdfChooser();
            String attFileName = pdfDB.fileName;

            File file = new File(attFileName);
            long attFileLength = file.length();
            if (attFileLength < 512000) {
                jTextAttDocNameWk2Main.setText(file.getName());
                jTextAttDocPathWk2Main.setText(attFileName);

            } else if (attFileLength > 512000) {
                JOptionPane.showMessageDialog(this, "File size cannot be greater than 512Kb. ");
            }
        }
    }//GEN-LAST:event_jButtonSelectFileWk2MainActionPerformed

    private void jTextAttDocNameWk2MainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocNameWk2MainActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocNameWk2MainActionPerformed

    private void jButtonWk2AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk2AddActionPerformed
        if ("".equals(jTextAttDocDescWk2Main.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter attached file description.");
            jTextAttDocDescWk2Main.requestFocusInWindow();
            jTextAttDocDescWk2Main.setFocusable(true);
        } else if ("".equals(jTextAttDocNameWk2Main.getText())) {
            JOptionPane.showMessageDialog(this, "Please note that no file has been selected.");
            jTextAttDocNameWk2Main.requestFocusInWindow();
            jTextAttDocNameWk2Main.setFocusable(true);
        } else {
            addFileAttWk2Main();
            refreshAttFields();

        }
    }//GEN-LAST:event_jButtonWk2AddActionPerformed

    private void jButtonWk2DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk2DeleteActionPerformed
        deleteFileAttWk2Main();
    }//GEN-LAST:event_jButtonWk2DeleteActionPerformed

    private void jTextAttDocPathWk2MainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocPathWk2MainActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocPathWk2MainActionPerformed

    private void jTextAttDocDescWk3MainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocDescWk3MainActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocDescWk3MainActionPerformed

    private void jButtonSelectFileWk3MainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectFileWk3MainActionPerformed
        if (modelWk3.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "File attachment is not possible if activities have not been added. Please add activities first. ");
        } else {
            savePDFToDB pdfDB = new savePDFToDB();
            pdfDB.pdfChooser();
            String attFileName = pdfDB.fileName;

            File file = new File(attFileName);
            long attFileLength = file.length();
            if (attFileLength < 512000) {
                jTextAttDocNameWk3Main.setText(file.getName());
                jTextAttDocPathWk3Main.setText(attFileName);

            } else if (attFileLength > 512000) {
                JOptionPane.showMessageDialog(this, "File size cannot be greater than 512Kb. ");
            }
        }
    }//GEN-LAST:event_jButtonSelectFileWk3MainActionPerformed

    private void jTextAttDocNameWk3MainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocNameWk3MainActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocNameWk3MainActionPerformed

    private void jButtonWk3AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk3AddActionPerformed
        if ("".equals(jTextAttDocDescWk3Main.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter attached file description.");
            jTextAttDocDescWk3Main.requestFocusInWindow();
            jTextAttDocDescWk3Main.setFocusable(true);
        } else if ("".equals(jTextAttDocNameWk3Main.getText())) {
            JOptionPane.showMessageDialog(this, "Please note that no file has been selected.");
            jTextAttDocNameWk3Main.requestFocusInWindow();
            jTextAttDocNameWk3Main.setFocusable(true);
        } else {
            addFileAttWk3Main();
            refreshAttFields();

        }
    }//GEN-LAST:event_jButtonWk3AddActionPerformed

    private void jButtonWk3DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk3DeleteActionPerformed
        deleteFileAttWk3Main();
    }//GEN-LAST:event_jButtonWk3DeleteActionPerformed

    private void jTextAttDocPathWk3MainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocPathWk3MainActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocPathWk3MainActionPerformed

    private void jTextAttDocPathWk4MainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocPathWk4MainActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocPathWk4MainActionPerformed

    private void jTextAttDocDescWk4MainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocDescWk4MainActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocDescWk4MainActionPerformed

    private void jButtonSelectFileWk4MainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectFileWk4MainActionPerformed
        if (modelWk4.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "File attachment is not possible if activities have not been added. Please add activities first. ");
        } else {
            savePDFToDB pdfDB = new savePDFToDB();
            pdfDB.pdfChooser();
            String attFileName = pdfDB.fileName;

            File file = new File(attFileName);
            long attFileLength = file.length();
            if (attFileLength < 512000) {
                jTextAttDocNameWk4Main.setText(file.getName());
                jTextAttDocPathWk4Main.setText(attFileName);

            } else if (attFileLength > 512000) {
                JOptionPane.showMessageDialog(this, "File size cannot be greater than 512Kb. ");
            }
        }
    }//GEN-LAST:event_jButtonSelectFileWk4MainActionPerformed

    private void jTextAttDocNameWk4MainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocNameWk4MainActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocNameWk4MainActionPerformed

    private void jButtonWk4AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk4AddActionPerformed
        if ("".equals(jTextAttDocDescWk4Main.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter attached file description.");
            jTextAttDocDescWk4Main.requestFocusInWindow();
            jTextAttDocDescWk4Main.setFocusable(true);
        } else if ("".equals(jTextAttDocNameWk4Main.getText())) {
            JOptionPane.showMessageDialog(this, "Please note that no file has been selected.");
            jTextAttDocNameWk4Main.requestFocusInWindow();
            jTextAttDocNameWk4Main.setFocusable(true);
        } else {
            addFileAttWk4Main();
            refreshAttFields();

        }
    }//GEN-LAST:event_jButtonWk4AddActionPerformed

    private void jButtonWk4DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk4DeleteActionPerformed
        deleteFileAttWk4Main();
    }//GEN-LAST:event_jButtonWk4DeleteActionPerformed

    private void jTextAttDocDescWk5MainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocDescWk5MainActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocDescWk5MainActionPerformed

    private void jButtonSelectFileWk5MainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectFileWk5MainActionPerformed
        if (modelWk5.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "File attachment is not possible if activities have not been added. Please add activities first. ");
        } else {
            savePDFToDB pdfDB = new savePDFToDB();
            pdfDB.pdfChooser();
            String attFileName = pdfDB.fileName;

            File file = new File(attFileName);
            long attFileLength = file.length();
            if (attFileLength < 512000) {
                jTextAttDocNameWk5Main.setText(file.getName());
                jTextAttDocPathWk5Main.setText(attFileName);

            } else if (attFileLength > 512000) {
                JOptionPane.showMessageDialog(this, "File size cannot be greater than 512Kb. ");
            }
        }
    }//GEN-LAST:event_jButtonSelectFileWk5MainActionPerformed

    private void jTextAttDocNameWk5MainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocNameWk5MainActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocNameWk5MainActionPerformed

    private void jButtonWk5AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk5AddActionPerformed
        if ("".equals(jTextAttDocDescWk5Main.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter attached file description.");
            jTextAttDocDescWk5Main.requestFocusInWindow();
            jTextAttDocDescWk5Main.setFocusable(true);
        } else if ("".equals(jTextAttDocNameWk5Main.getText())) {
            JOptionPane.showMessageDialog(this, "Please note that no file has been selected.");
            jTextAttDocNameWk5Main.requestFocusInWindow();
            jTextAttDocNameWk5Main.setFocusable(true);
        } else {
            addFileAttWk5Main();
            refreshAttFields();

        }
    }//GEN-LAST:event_jButtonWk5AddActionPerformed

    private void jButtonWk5DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWk5DeleteActionPerformed
        deleteFileAttWk5Main();
    }//GEN-LAST:event_jButtonWk5DeleteActionPerformed

    private void jTextAttDocPathWk5MainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocPathWk5MainActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocPathWk5MainActionPerformed

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

    private void jTextAreaWk1DialogJustificationKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAreaWk1DialogJustificationKeyTyped
        jTextAreaWk1DialogJustification.setLineWrap(true);
        jTextAreaWk1DialogJustification.setWrapStyleWord(true);

        String charsRemaining = " characters remaining";
        int newLen = 0;

        int currLen = jTextAreaWk1DialogJustification.getText().length();

        if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            newLen = currLen - 1;
            ignoreInput = false;
        } else {
            newLen = currLen + 1;
        }

        if (newLen < 0) {
            newLen = 0;
        }

        if (newLen == 0) {
            jLabelRemain.setText(jLabelRemain.getText() + "   " + charMaxWk1 + " characters maximum!");
        } else if (newLen >= 0 && newLen < charMaxWk1) {
            jLabelRemain.setText(jLabelRemain.getText() + "   " + (charMaxWk1 - newLen) + charsRemaining);
        } else if (newLen >= charMaxWk1) {
            try {
                ignoreInput = true;
                jLabelRemain.setText(jLabelRemain.getText() + "   " + "0 " + charsRemaining);
                JOptionPane.showMessageDialog(jDialogWk1, "Maximum allowed characters reached.",
                        "Word Count Warning", JOptionPane.WARNING_MESSAGE);
                jTextAreaWk1DialogJustification.requestFocusInWindow();
                jTextAreaWk1DialogJustification.setFocusable(true);
            } catch (Exception e) {
                System.out.println(e);
            }

        }
        if ((charMaxWk1 - newLen) > 15) {
            jLabelRemain.setForeground(new java.awt.Color(0, 102, 0));
        } else {
            jLabelRemain.setForeground(new java.awt.Color(255, 51, 51));
        }
    }//GEN-LAST:event_jTextAreaWk1DialogJustificationKeyTyped

    private void jComboProjectCodeProgrammingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboProjectCodeProgrammingMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboProjectCodeProgrammingMouseClicked

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
            java.util.logging.Logger.getLogger(JFrameMnthPlanPerDiemCreate.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthPlanPerDiemCreate.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthPlanPerDiemCreate.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthPlanPerDiemCreate.class
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
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
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
                new JFrameMnthPlanPerDiemCreate().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupAcc;
    private cambodia.raven.Time departTime;
    private javax.swing.JButton jButtonCancelFacility;
    private javax.swing.JButton jButtonDialogWk1Add;
    private javax.swing.JButton jButtonDialogWk1Close;
    private javax.swing.JButton jButtonDialogWk1Reset;
    private javax.swing.JButton jButtonOk1;
    private javax.swing.JButton jButtonOkFacility;
    private javax.swing.JButton jButtonSearch1;
    private javax.swing.JButton jButtonSelectFile;
    private javax.swing.JButton jButtonSelectFileWk1Main;
    private javax.swing.JButton jButtonSelectFileWk2Main;
    private javax.swing.JButton jButtonSelectFileWk3Main;
    private javax.swing.JButton jButtonSelectFileWk4Main;
    private javax.swing.JButton jButtonSelectFileWk5Main;
    private javax.swing.JButton jButtonWk1Add;
    private javax.swing.JButton jButtonWk1AddActivity;
    private javax.swing.JButton jButtonWk1DelActivity;
    private javax.swing.JButton jButtonWk1Delete;
    private javax.swing.JButton jButtonWk2Add;
    private javax.swing.JButton jButtonWk2AddActivity;
    private javax.swing.JButton jButtonWk2DelActivity;
    private javax.swing.JButton jButtonWk2Delete;
    private javax.swing.JButton jButtonWk3Add;
    private javax.swing.JButton jButtonWk3AddActivity;
    private javax.swing.JButton jButtonWk3DelActivity;
    private javax.swing.JButton jButtonWk3Delete;
    private javax.swing.JButton jButtonWk4Add;
    private javax.swing.JButton jButtonWk4AddActivity;
    private javax.swing.JButton jButtonWk4DelActivity;
    private javax.swing.JButton jButtonWk4Delete;
    private javax.swing.JButton jButtonWk5Add;
    private javax.swing.JButton jButtonWk5AddActivity;
    private javax.swing.JButton jButtonWk5DelActivity;
    private javax.swing.JButton jButtonWk5Delete;
    private javax.swing.JCheckBox jCheckAddStaff;
    private javax.swing.JCheckBox jCheckBoxDialogWk1AccProved;
    private javax.swing.JCheckBox jCheckBoxDialogWk1AccUnProved;
    private javax.swing.JCheckBox jCheckBoxDialogWk1BrkFast;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Dinner;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Inc;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Lunch;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Misc;
    private javax.swing.JCheckBox jCheckBoxNoAcc;
    private javax.swing.JComboBox<String> jComboAccountCode;
    private javax.swing.JComboBox<String> jComboBoxSearchResult1;
    private javax.swing.JComboBox<String> jComboBudMainCode;
    private javax.swing.JComboBox<String> jComboDistrictFacility;
    private javax.swing.JComboBox<String> jComboDonor;
    private javax.swing.JComboBox<String> jComboFacility;
    private javax.swing.JComboBox<String> jComboProjectCodeGL;
    private javax.swing.JComboBox<String> jComboProjectCodeProgramming;
    private javax.swing.JComboBox<String> jComboProvinceFacility;
    private com.toedter.calendar.JDateChooser jDateChooserDialogActivityDateFrom;
    private com.toedter.calendar.JDateChooser jDateChooserDialogActivityDateTo;
    private com.toedter.calendar.JDateChooser jDateChooserWk1From;
    private com.toedter.calendar.JDateChooser jDateChooserWk1To;
    private com.toedter.calendar.JDateChooser jDateChooserWk2From;
    private com.toedter.calendar.JDateChooser jDateChooserWk2To;
    private com.toedter.calendar.JDateChooser jDateChooserWk3From;
    private com.toedter.calendar.JDateChooser jDateChooserWk3To;
    private com.toedter.calendar.JDateChooser jDateChooserWk4From;
    private com.toedter.calendar.JDateChooser jDateChooserWk4To;
    private com.toedter.calendar.JDateChooser jDateChooserWk5From;
    private com.toedter.calendar.JDateChooser jDateChooserWk5To;
    private javax.swing.JDialog jDialogFacility;
    private javax.swing.JDialog jDialogSearchName1;
    private javax.swing.JDialog jDialogWaiting;
    private javax.swing.JDialog jDialogWaitingEmail;
    private javax.swing.JDialog jDialogWk1;
    private javax.swing.JLabel jLabeAccountCode;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelBudMainCode;
    private javax.swing.JLabel jLabelConfirmationWk1Main;
    private javax.swing.JLabel jLabelConfirmationWk2Main;
    private javax.swing.JLabel jLabelConfirmationWk3Main;
    private javax.swing.JLabel jLabelConfirmationWk4Main;
    private javax.swing.JLabel jLabelConfirmationWk5Main;
    private javax.swing.JLabel jLabelDialogDepartActivityDate;
    private javax.swing.JLabel jLabelDialogPerDiem;
    private javax.swing.JLabel jLabelDialogReturnActivityDate;
    private javax.swing.JLabel jLabelDialogWk1Site;
    private javax.swing.JLabel jLabelDistrict;
    private javax.swing.JLabel jLabelDistrictFacility;
    private javax.swing.JLabel jLabelDonor;
    private javax.swing.JLabel jLabelEmp;
    private javax.swing.JLabel jLabelFacility;
    private javax.swing.JLabel jLabelGenLogNam1;
    private javax.swing.JLabel jLabelGenLogNam3;
    private javax.swing.JLabel jLabelGenLogNam5;
    private javax.swing.JLabel jLabelGenLogNam6;
    private javax.swing.JLabel jLabelGenLogNam7;
    private javax.swing.JLabel jLabelHeader1;
    private javax.swing.JLabel jLabelHeaderGen10;
    private javax.swing.JLabel jLabelHeaderGen4;
    private javax.swing.JLabel jLabelHeaderGen7;
    private javax.swing.JLabel jLabelHeaderGen8;
    private javax.swing.JLabel jLabelHeaderGen9;
    private javax.swing.JLabel jLabelJustfy;
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
    private javax.swing.JLabel jLabelMOHCCConfirmation;
    private javax.swing.JLabel jLabelOffice1;
    private javax.swing.JLabel jLabelPrjCodeGL;
    private javax.swing.JLabel jLabelPrjCodeProgramming;
    private javax.swing.JLabel jLabelProvince;
    private javax.swing.JLabel jLabelProvince1;
    private javax.swing.JLabel jLabelProvinceFacility;
    private javax.swing.JLabel jLabelRef;
    private javax.swing.JLabel jLabelRefNum;
    private javax.swing.JLabel jLabelRemain;
    private javax.swing.JLabel jLabelSerial;
    private javax.swing.JLabel jLabelWk1DialogActivityDesc;
    private javax.swing.JLabel jLabelWk1DialogStaffName;
    private javax.swing.JLabel jLabelWk1DialogStaffName1;
    private javax.swing.JLabel jLabelWk1DialogStaffName2;
    private javax.swing.JLabel jLabelWk1From;
    private javax.swing.JLabel jLabelWk1From1;
    private javax.swing.JLabel jLabelWk1Misc;
    private javax.swing.JLabel jLabelWk1MiscAmt;
    private javax.swing.JLabel jLabelWk1Name1Del;
    private javax.swing.JLabel jLabelWk1Name2Del;
    private javax.swing.JLabel jLabelWk1Name3Del;
    private javax.swing.JLabel jLabelWk1Name4Del;
    private javax.swing.JLabel jLabelWk1To;
    private javax.swing.JLabel jLabelWk1To1;
    private javax.swing.JLabel jLabelWk3From;
    private javax.swing.JLabel jLabelWk3To;
    private javax.swing.JLabel jLabelWk4From;
    private javax.swing.JLabel jLabelWk4To;
    private javax.swing.JLabel jLabelWk5From;
    private javax.swing.JLabel jLabelWk5To;
    private javax.swing.JLabel jLabelWkDuration;
    private javax.swing.JLabel jLabelWkDuration1;
    private javax.swing.JLabel jLabelWkDuration2;
    private javax.swing.JLabel jLabelWkDuration4;
    private javax.swing.JLabel jLabelWkDuration5;
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
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelActivity;
    private javax.swing.JPanel jPanelActivityInfo;
    private javax.swing.JPanel jPanelAddStaff;
    private javax.swing.JPanel jPanelAttach;
    private javax.swing.JPanel jPanelAttachWk1Main;
    private javax.swing.JPanel jPanelAttachWk1Main1;
    private javax.swing.JPanel jPanelAttachWk3Main;
    private javax.swing.JPanel jPanelAttachWk4Main;
    private javax.swing.JPanel jPanelAttachWk5Main;
    private javax.swing.JPanel jPanelDepartureDetails;
    private javax.swing.JPanel jPanelDooAtt;
    private javax.swing.JPanel jPanelDooAttWk1Main;
    private javax.swing.JPanel jPanelDooAttWk2Main;
    private javax.swing.JPanel jPanelDooAttWk3Main;
    private javax.swing.JPanel jPanelDooAttWk4Main;
    private javax.swing.JPanel jPanelDooAttWk5Main;
    private javax.swing.JPanel jPanelSave;
    private javax.swing.JPanel jPanelSave1;
    private javax.swing.JPanel jPanelSave2;
    private javax.swing.JPanel jPanelSave3;
    private javax.swing.JPanel jPanelSave4;
    private javax.swing.JPanel jPanelSearchID;
    private javax.swing.JPanel jPanelWkFive;
    private javax.swing.JPanel jPanelWkFour;
    private javax.swing.JPanel jPanelWkOne;
    private javax.swing.JPanel jPanelWkThree;
    private javax.swing.JPanel jPanelWkTwo;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPaneWk1;
    private javax.swing.JScrollPane jScrollPaneWk1Att;
    private javax.swing.JScrollPane jScrollPaneWk2;
    private javax.swing.JScrollPane jScrollPaneWk2Att;
    private javax.swing.JScrollPane jScrollPaneWk3;
    private javax.swing.JScrollPane jScrollPaneWk3Att;
    private javax.swing.JScrollPane jScrollPaneWk4;
    private javax.swing.JScrollPane jScrollPaneWk4Att;
    private javax.swing.JScrollPane jScrollPaneWk5;
    private javax.swing.JScrollPane jScrollPaneWk5Att;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator11;
    private javax.swing.JPopupMenu.Separator jSeparator12;
    private javax.swing.JPopupMenu.Separator jSeparator13;
    private javax.swing.JPopupMenu.Separator jSeparator14;
    private javax.swing.JPopupMenu.Separator jSeparator17;
    private javax.swing.JPopupMenu.Separator jSeparator18;
    private javax.swing.JPopupMenu.Separator jSeparator19;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator20;
    private javax.swing.JPopupMenu.Separator jSeparator22;
    private javax.swing.JPopupMenu.Separator jSeparator23;
    private javax.swing.JPopupMenu.Separator jSeparator24;
    private javax.swing.JPopupMenu.Separator jSeparator26;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator33;
    private javax.swing.JPopupMenu.Separator jSeparator34;
    private javax.swing.JSeparator jSeparator37;
    private javax.swing.JSeparator jSeparator38;
    private javax.swing.JSeparator jSeparator39;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator40;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator62;
    private javax.swing.JPopupMenu.Separator jSeparator63;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableDocAtt;
    private javax.swing.JTable jTableDocAttWk1Main;
    private javax.swing.JTable jTableDocAttWk2Main;
    private javax.swing.JTable jTableDocAttWk3Main;
    private javax.swing.JTable jTableDocAttWk4Main;
    private javax.swing.JTable jTableDocAttWk5Main;
    private javax.swing.JTable jTableWk1Activities;
    private javax.swing.JTable jTableWk2Activities;
    private javax.swing.JTable jTableWk3Activities;
    private javax.swing.JTable jTableWk4Activities;
    private javax.swing.JTable jTableWk5Activities;
    private javax.swing.JTextArea jTextAreaWk1DialogJustification;
    private javax.swing.JTextField jTextAttDocDesc;
    private javax.swing.JTextField jTextAttDocDescWk1Main;
    private javax.swing.JTextField jTextAttDocDescWk2Main;
    private javax.swing.JTextField jTextAttDocDescWk3Main;
    private javax.swing.JTextField jTextAttDocDescWk4Main;
    private javax.swing.JTextField jTextAttDocDescWk5Main;
    private javax.swing.JTextField jTextAttDocName;
    private javax.swing.JTextField jTextAttDocNameWk1Main;
    private javax.swing.JTextField jTextAttDocNameWk2Main;
    private javax.swing.JTextField jTextAttDocNameWk3Main;
    private javax.swing.JTextField jTextAttDocNameWk4Main;
    private javax.swing.JTextField jTextAttDocNameWk5Main;
    private javax.swing.JTextField jTextAttDocPath;
    private javax.swing.JTextField jTextAttDocPathWk1Main;
    private javax.swing.JTextField jTextAttDocPathWk2Main;
    private javax.swing.JTextField jTextAttDocPathWk3Main;
    private javax.swing.JTextField jTextAttDocPathWk4Main;
    private javax.swing.JTextField jTextAttDocPathWk5Main;
    private javax.swing.JTextField jTextFieldDialogWkSite;
    private javax.swing.JTextField jTextFieldSearchNam1;
    private javax.swing.JTextField jTextFieldWk1DialogActivityDesc;
    private javax.swing.JTextField jTextFieldWk1DialogStaffName1;
    private javax.swing.JTextField jTextFieldWk1DialogStaffName2;
    private javax.swing.JTextField jTextFieldWk1DialogStaffName3;
    private javax.swing.JTextField jTextFieldWk1DialogStaffName4;
    private javax.swing.JTextField jTextFieldWk1Misc;
    private javax.swing.JTextField jTextFieldWk1MiscAmt;
    public javax.swing.JToggleButton jToggleButtonMOHCCConfirmation;
    public javax.swing.JToggleButton jToggleButtonMOHCCConfirmation2;
    private cambodia.raven.Time returnTime;
    // End of variables declaration//GEN-END:variables
}
