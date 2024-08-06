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
import java.time.LocalDate;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.Properties;
import ClaimAppendix1.*;
import ClaimAppendix2.*;
import ClaimReports.*;
import java.time.temporal.ChronoUnit;
import ClaimPlan.JFrameMnthFinanceList;
import ClaimPlan.JFrameMnthHODList;
import ClaimPlan.JFrameMnthReqGenList;
import ClaimPlan.JFrameMnthSupList;
import ClaimPlan.JFrameMnthViewList;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.text.DateFormat;
import java.time.Period;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.connCred;
import utils.timeHost;
import utils.emailSend;
import utils.StockVehicleMgt;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import utils.connSaveFile;
import java.io.File;
import java.util.List;
import utils.savePDFToDB;

/**
 *
 * @author goredemac
 */
public class JFrameMnthPlanPerDiemEdit extends javax.swing.JFrame {

    connCred c = new connCred();
    timeHost tH = new timeHost();
    emailSend emSend = new emailSend();
    connSaveFile attL = new connSaveFile();
    List<String> listFetchWk1 = new ArrayList<>();
    List<String> listFetchWk2 = new ArrayList<>();
    List<String> listFetchWk3 = new ArrayList<>();
    List<String> listFetchWk4 = new ArrayList<>();
    List<String> listFetchWk5 = new ArrayList<>();
    DefaultTableModel modelWk1, modelWk2, modelWk3, modelWk4, modelWk5, modelAtt,
            modelAttWk1Main, modelAttWk2Main, modelAttWk3Main, modelAttWk4Main, modelAttWk5Main;
    LocalDate minDateWk1, maxDateWk1, minDateWk2, maxDateWk2, minDateWk3, maxDateWk3, minDateWk4, maxDateWk4, minDateWk5, maxDateWk5;
    Date minDateComWk1, maxDateComWk1, minDateComWk2, maxDateComWk2, minDateComWk3, maxDateComWk3, minDateComWk4, maxDateComWk4, minDateComWk5, maxDateComWk5;
    ZoneId zoneId = ZoneId.systemDefault();
    boolean ignoreInput = false;
    String filename = null;
    int charMaxWk1 = 200;
    int charMaxWk2 = 200;
    int charMaxWk3 = 200;
    int charMaxWk4 = 200;
    int charMaxWk5 = 200;
    int maxItmWk1 = 0;
    int maxItmWk2 = 0;
    int maxItmWk3 = 0;
    int maxItmWk4 = 0;
    int maxItmWk5 = 0;
    long noOfFinDays1 = 0;
    long noOfFinDays2 = 0;
    long noOfFinDays3 = 0;
    long noOfFinDays4 = 0;
    long noOfFinDays5 = 0;
    double totReqAmt = 0;
    double totBudgetLnReqAmt = 0;
    Date curYear = new Date();
    int row, col, tabNam, finyear, month;
    SimpleDateFormat df = new SimpleDateFormat("yyyy");
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String wk1Site = "N";
    String wk2Site = "N";
    String wk3Site = "N";
    String wk4Site = "N";
    String wk5Site = "N";
    String delWk1 = "N";
    String delWk2 = "N";
    String delWk3 = "N";
    String delWk4 = "N";
    String delWk5 = "N";
    String hostName = "";
    String breakfastAll, lunchAll, lunchNPAll, lunchPAll, dinnerAll, usrGrp, supNam, supUsrMail,
            incidentalAll, unProvedAll, provedAll, unProvedZimTTECH, date1, date2, usrnam, docVer, actVer, actNextVer, editName, planStatus,
            sendToProvMgr, provMgrMail, usrRecNam, UsrRecWk, actDate, SupNamSend, usrActType, staffName1, staffName2,
            staffName3, staffName4, branchCode, prjCode, taskCode, lastDateofMonth, createUsrNam, supEmpNum, fetchStringWk1,
            fetchStringWk2, fetchStringWk3, fetchStringWk4, fetchStringWk5, donorName, donorCode, accCodeName,
            prjCodeName, prjProgCodeName, budLineName, budcode, taskDonor, donor, accCode, prjProgCode, budLine, subBudLine, grantCode;
    PreparedStatement pst1 = null;
    PreparedStatement pst = null;
    int itmNum = 1;
    int planRefCount = 0;
    java.util.Date origDate;

    /**
     * Creates new form JFrameMnthPlanPerDiemCreate
     */
    public JFrameMnthPlanPerDiemEdit() {
        initComponents();
        findProvince();
        allowanceRate();
        // showDate();
        showTime();
        computerName();

    }

    public JFrameMnthPlanPerDiemEdit(String usrLogNam) {
        initComponents();
        modelWk1 = (DefaultTableModel) jTableWk1Activities.getModel();
        modelWk2 = (DefaultTableModel) jTableWk2Activities.getModel();
        modelWk3 = (DefaultTableModel) jTableWk3Activities.getModel();
        modelWk4 = (DefaultTableModel) jTableWk4Activities.getModel();
        modelWk5 = (DefaultTableModel) jTableWk5Activities.getModel();
        modelAtt = (DefaultTableModel) jTableDocAtt.getModel();
        modelAttWk1Main = (DefaultTableModel) jTableDocAttWk1.getModel();
        modelAttWk2Main = (DefaultTableModel) jTableDocAttWk2.getModel();
        modelAttWk3Main = (DefaultTableModel) jTableDocAttWk3.getModel();
        modelAttWk4Main = (DefaultTableModel) jTableDocAttWk4.getModel();
        modelAttWk5Main = (DefaultTableModel) jTableDocAttWk5.getModel();
        findProvince();

        try {
            //showDate();
            tH.intShowDate();
        } catch (IOException ex) {
            Logger.getLogger(JFrameMnthPlanPerDiemEdit.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(JFrameMnthPlanPerDiemEdit.class.getName()).log(Level.SEVERE, null, ex);
        }
        showTime();
        computerName();
        jLabelEmp.setText(usrLogNam);
        jLabelEmp.setVisible(false);

        jLabelWk1Name1Del.setVisible(false);
        jLabelWk1Name2Del.setVisible(false);
        jLabelWk1Name3Del.setVisible(false);
        jLabelWk1Name4Del.setVisible(false);

        jLabelPostAppMod1.setText("");
        jLabelLineDate.setText(tH.internetDate);
        jLabelLineDate1.setText(tH.internetDate);
        jLabelLineDate2.setText(tH.internetDate);
        jLabelLineDate3.setText(tH.internetDate);
        jLabelLineDate4.setText(tH.internetDate);
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

        jTableDocAttWk1.getColumnModel().getColumn(0).setMinWidth(0);
        jTableDocAttWk1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTableDocAttWk2.getColumnModel().getColumn(0).setMinWidth(0);
        jTableDocAttWk2.getColumnModel().getColumn(0).setMaxWidth(0);
        jTableDocAttWk3.getColumnModel().getColumn(0).setMinWidth(0);
        jTableDocAttWk3.getColumnModel().getColumn(0).setMaxWidth(0);
        jTableDocAttWk4.getColumnModel().getColumn(0).setMinWidth(0);
        jTableDocAttWk4.getColumnModel().getColumn(0).setMaxWidth(0);
        jTableDocAttWk5.getColumnModel().getColumn(0).setMinWidth(0);
        jTableDocAttWk5.getColumnModel().getColumn(0).setMaxWidth(0);

        jTableDocAttWk1.getColumnModel().getColumn(3).setMinWidth(0);
        jTableDocAttWk1.getColumnModel().getColumn(3).setMaxWidth(0);
        jTableDocAttWk2.getColumnModel().getColumn(3).setMinWidth(0);
        jTableDocAttWk2.getColumnModel().getColumn(3).setMaxWidth(0);
        jTableDocAttWk3.getColumnModel().getColumn(3).setMinWidth(0);
        jTableDocAttWk3.getColumnModel().getColumn(3).setMaxWidth(0);
        jTableDocAttWk4.getColumnModel().getColumn(3).setMinWidth(0);
        jTableDocAttWk4.getColumnModel().getColumn(3).setMaxWidth(0);
        jTableDocAttWk5.getColumnModel().getColumn(3).setMinWidth(0);
        jTableDocAttWk5.getColumnModel().getColumn(3).setMaxWidth(0);

        findUser();
        findUserGrp();
        getYear();
        findAccCode();
        findDonorCode();
        findGrantBud();
        findPrjProgCode();

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

    void getYear() {
        GregorianCalendar date = new GregorianCalendar();
        month = date.get(Calendar.MONTH);
        month = month + 1;

        if ((month >= 1) && (month <= 9)) {
            finyear = Integer.parseInt(df.format(curYear));
        } else {
            finyear = Integer.parseInt(df.format(curYear)) + 1;
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

            ResultSet r = st.executeQuery("SELECT EMP_NAM  ,Emp_Mail FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[ProvManTab] where  province ='"
                    + jLabelProvince.getText() + "'");
            while (r.next()) {
                sendToProvMgr = r.getString(1);
                provMgrMail = r.getString(2);

            }

            //                 conn.close();
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

            ResultSet r = st.executeQuery("select EMP_NAM,SUP_EMP_NUM,SUP_NAM,EMP_SUP_MAIL from [ClaimsAppSysZvandiri].[dbo]."
                    + "[EmpDetTab] where emp_num ='" + jLabelEmp.getText() + "'");

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
            }

            //                 conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void regInitCheck() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT count(*) FROM [ClaimsAppSysZvandiri].[dbo].[PlanActTab] "
                    + "where PLAN_REF_NUM =" + jTextRefNum.getText() + " and ACT_REC_STA = 'A' "
                    + "and DOC_STATUS like  '%AmendRequest%'");

            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                planRefCount = rs.getInt(1);

            }

            Statement st1 = conn.createStatement();

            st1.executeQuery("SELECT DOC_STATUS,ACTIONED_BY_NAM,ACT_REC_STA FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[PlanActTab] where PLAN_REF_NUM =" + jTextRefNum.getText()
                    + "and ACT_REC_STA = 'C'");

            ResultSet rs1 = st1.getResultSet();

            while (rs1.next()) {
                planStatus = rs1.getString(1);
                editName = rs1.getString(2);
            }

            if (planRefCount == 1) {
                fetchdataWk1();
                fetchdataWk2();
                fetchdataWk3();
                fetchdataWk4();
                fetchdataWk5();
                fetchdataGenWk1();
                findProvMan();
                fetchAttDocWk1(jTextRefNum.getText());
            } else if ("Approved".equals(planStatus) && jLabelLineLogNam.getText().equals(editName)) {
                fetchdataWkC1();
                fetchdataWkC2();
                fetchdataWkC3();
                fetchdataWkC4();
                fetchdataWkC5();
                fetchdataGenWkC1();
                findProvMan();
                fetchAttDocWk1(jTextRefNum.getText());
            } else {
                JOptionPane.showMessageDialog(null, "Plan number is invalid or cannot be editted.",
                        "Invalid Reference Number", JOptionPane.WARNING_MESSAGE);
            }
//            if (!"National Office".equals(jLabelDistrict.getText())) {
//                jTabbedPaneMain.setTitleAt(0, "Month Plan");
//                jTabbedPaneMain.setEnabledAt(1, false);
//                jTabbedPaneMain.setTitleAt(1, "");
//                jTabbedPaneMain.setEnabledAt(2, false);
//                jTabbedPaneMain.setTitleAt(2, "");
//                jTabbedPaneMain.setEnabledAt(3, false);
//                jTabbedPaneMain.setTitleAt(3, "");
//                jTabbedPaneMain.setEnabledAt(4, false);
//                jTabbedPaneMain.setTitleAt(4, "");
//            } else {
//                jTabbedPaneMain.setTitleAt(0, "Week One");
//                jTabbedPaneMain.setEnabledAt(1, true);
//                jTabbedPaneMain.setTitleAt(1, "Week Two");
//                jTabbedPaneMain.setEnabledAt(2, true);
//                jTabbedPaneMain.setTitleAt(2, "Week Three");
//                jTabbedPaneMain.setEnabledAt(3, true);
//                jTabbedPaneMain.setTitleAt(3, "Week Four");
//                jTabbedPaneMain.setEnabledAt(4, true);
//                jTabbedPaneMain.setTitleAt(4, "Week Five");
//            }
            allowanceRate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findProvince() {
        try {

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
            System.out.println(e);

        }
    }

    void facilityPOP() {
        jDialogFacility.setVisible(true);
        jDialogFacility.setVisible(false);
        jDialogFacility.setVisible(true);
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

        } catch (Exception e) {
        }
    }

    void findDocVer() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st1 = conn.createStatement();

            if (jTableWk1Activities.getRowCount() > 0) {

                st1.executeQuery("SELECT distinct max(ACT_VER),max(ACT_VER) + 1  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] "
                        + "where PLAN_REF_NUM =" + jTextRefNum.getText() + " and DOC_VER =1");

                ResultSet r1 = st1.getResultSet();

                while (r1.next()) {
                    actVer = r1.getString(1);
                    actNextVer = r1.getString(2);

                }
            } else if (jTableWk2Activities.getRowCount() > 0) {

                st1.executeQuery("SELECT distinct max(ACT_VER),max(ACT_VER) + 1  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] "
                        + "where PLAN_REF_NUM =" + jTextRefNum.getText() + " and DOC_VER =1");

                ResultSet r1 = st1.getResultSet();

                while (r1.next()) {
                    actVer = r1.getString(1);
                    actNextVer = r1.getString(2);

                }
            } else if (jTableWk3Activities.getRowCount() > 0) {

                st1.executeQuery("SELECT distinct max(ACT_VER),max(ACT_VER) + 1  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] "
                        + "where PLAN_REF_NUM =" + jTextRefNum.getText() + " and DOC_VER =1");

                ResultSet r1 = st1.getResultSet();

                while (r1.next()) {
                    actVer = r1.getString(1);
                    actNextVer = r1.getString(2);

                }
            } else if (jTableWk4Activities.getRowCount() > 0) {

                st1.executeQuery("SELECT distinct max(ACT_VER),max(ACT_VER) + 1  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab] "
                        + "where PLAN_REF_NUM =" + jTextRefNum.getText() + " and DOC_VER =1");

                ResultSet r1 = st1.getResultSet();

                while (r1.next()) {
                    actVer = r1.getString(1);
                    actNextVer = r1.getString(2);

                }
            } else if (jTableWk5Activities.getRowCount() > 0) {

                st1.executeQuery("SELECT distinct max(ACT_VER),max(ACT_VER) + 1  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab] "
                        + "where PLAN_REF_NUM =" + jTextRefNum.getText() + " and DOC_VER =1");

                ResultSet r1 = st1.getResultSet();

                while (r1.next()) {
                    actVer = r1.getString(1);
                    actNextVer = r1.getString(2);

                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }

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

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT Lunch,Dinner,Incidental,Unproved_Accommodation,Proved_Accommodation,"
                    + "Unproved_ZimTTECH_Acc  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAllowanceTab] ");

            while (r.next()) {

                lunchAll = r.getString(1);
                dinnerAll = r.getString(2);
                incidentalAll = r.getString(3);
                unProvedAll = r.getString(4);
                provedAll = r.getString(5);
                unProvedZimTTECH = r.getString(6);

            }
            //                 conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void UsrRecEditUpd() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sql1 = "update [ClaimsAppSysZvandiri].[dbo].[PlanUsrRecTab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM=" + jTextRefNum.getText();

            pst = conn.prepareStatement(sql1);
            pst.executeUpdate();

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

        } catch (Exception e) {
            System.out.println(e);
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
            pst1.setString(7, actNextVer);
            pst1.setString(8, "1");
            pst1.setString(9, "A");
            pst1.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateWk1Plan() {
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
                pst1.setString(27, actNextVer);
                pst1.setString(28, "1");
                pst1.setString(29, "A");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateWk2Plan() {
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
                pst1.setString(27, actNextVer);
                pst1.setString(28, "1");
                pst1.setString(29, "A");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateWk3Plan() {
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
                pst1.setString(27, actNextVer);
                pst1.setString(28, "1");
                pst1.setString(29, "A");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateWk4Plan() {
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
                pst1.setString(27, actNextVer);
                pst1.setString(28, "1");
                pst1.setString(29, "A");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateWk5Plan() {
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
                pst1.setString(27, actNextVer);
                pst1.setString(28, "1");
                pst1.setString(29, "A");

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
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + jTextRefNum.getText() + " and ACT_REC_STA in ('A','C') ";

            pst = conn.prepareStatement(sqlPlanAct);
            pst.executeUpdate();

            String sqlPlanPeriod = "update [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + jTextRefNum.getText() + " and ACT_REC_STA in ('A','C') ";

            pst = conn.prepareStatement(sqlPlanPeriod);
            pst.executeUpdate();

            String sqlPlanWk1 = "update [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + jTextRefNum.getText() + " and ACT_REC_STA in ('A','C') ";

            pst = conn.prepareStatement(sqlPlanWk1);
            pst.executeUpdate();

            String sqlPlanWk2 = "update [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + jTextRefNum.getText() + " and ACT_REC_STA in ('A','C') ";

            pst = conn.prepareStatement(sqlPlanWk2);
            pst.executeUpdate();

            String sqlPlanWk3 = "update [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + jTextRefNum.getText() + " and ACT_REC_STA in ('A','C') ";

            pst = conn.prepareStatement(sqlPlanWk3);
            pst.executeUpdate();

            String sqlPlanWk4 = "update [ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + jTextRefNum.getText() + " and ACT_REC_STA in ('A','C') ";

            pst = conn.prepareStatement(sqlPlanWk4);
            pst.executeUpdate();

            String sqlPlanWk5 = "update [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + jTextRefNum.getText() + " and ACT_REC_STA in ('A','C') ";

            pst = conn.prepareStatement(sqlPlanWk5);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    void updateAttDocWk1Main() {
        try {
            System.out.println("running");
            int itmNumAtt = maxItmWk1;
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysFB;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");

            if ("Y".equals(delWk1)) {
                attL.updOldAttDoc(conn, fetchStringWk1, jTextRefNum.getText(), "ClaimAttDocJustTabWk1");
            }

            for (int i = 0; i < modelAttWk1Main.getRowCount(); i++) {
                if (modelAttWk1Main.getValueAt(i, 0).toString().trim().length() == 0) {
                    String imgFileValue = modelAttWk1Main.getValueAt(i, 3).toString();
                    String imgFileDsc = modelAttWk1Main.getValueAt(i, 1).toString();
                    String imgFileName = modelAttWk1Main.getValueAt(i, 2).toString();

                    if (imgFileValue.trim().length() > 0) {

                        attL.insertAttWkDoc(conn, imgFileName, imgFileDsc, imgFileValue,
                                itmNumAtt, jTextRefNum.getText(), actNextVer, "1", "ClaimAttDocJustTabWk1", finyear);

                    }

                    itmNumAtt = itmNumAtt + 1;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateAttDocWk2Main() {
        try {
            int itmNumAtt = maxItmWk2;
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysFB;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");

            if ("Y".equals(delWk2)) {
                attL.updOldAttDoc(conn, fetchStringWk2, jTextRefNum.getText(), "ClaimAttDocJustTabWk2");
            }

            for (int i = 0; i < modelAttWk2Main.getRowCount(); i++) {
                String imgFileValue = modelAttWk2Main.getValueAt(i, 3).toString();
                String imgFileDsc = modelAttWk2Main.getValueAt(i, 1).toString();
                String imgFileName = modelAttWk2Main.getValueAt(i, 2).toString();

                if (imgFileValue.trim().length() > 0) {

                    attL.insertAttWkDoc(conn, imgFileName, imgFileDsc, imgFileValue,
                            itmNumAtt, jTextRefNum.getText(), actNextVer, "1", "ClaimAttDocJustTabWk2", finyear);

                }

                itmNumAtt = itmNumAtt + 1;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateAttDocWk3Main() {
        try {
            int itmNumAtt = maxItmWk3;
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysFB;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");

            if ("Y".equals(delWk3)) {
                attL.updOldAttDoc(conn, fetchStringWk3, jTextRefNum.getText(), "ClaimAttDocJustTabWk3");
            }

            for (int i = 0; i < modelAttWk3Main.getRowCount(); i++) {
                String imgFileValue = modelAttWk3Main.getValueAt(i, 3).toString();
                String imgFileDsc = modelAttWk3Main.getValueAt(i, 1).toString();
                String imgFileName = modelAttWk3Main.getValueAt(i, 2).toString();

                if (imgFileValue.trim().length() > 0) {

                    attL.insertAttWkDoc(conn, imgFileName, imgFileDsc, imgFileValue,
                            itmNumAtt, jTextRefNum.getText(), actNextVer, "1", "ClaimAttDocJustTabWk3", finyear);

                }

                itmNumAtt = itmNumAtt + 1;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateAttDocWk4Main() {
        try {
            int itmNumAtt = maxItmWk4;
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysFB;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");

            if ("Y".equals(delWk4)) {
                attL.updOldAttDoc(conn, fetchStringWk4, jTextRefNum.getText(), "ClaimAttDocJustTabWk4");
            }

            for (int i = 0; i < modelAttWk4Main.getRowCount(); i++) {
                String imgFileValue = modelAttWk4Main.getValueAt(i, 3).toString();
                String imgFileDsc = modelAttWk4Main.getValueAt(i, 1).toString();
                String imgFileName = modelAttWk4Main.getValueAt(i, 2).toString();

                if (imgFileValue.trim().length() > 0) {

                    attL.insertAttWkDoc(conn, imgFileName, imgFileDsc, imgFileValue,
                            itmNumAtt, jTextRefNum.getText(), actNextVer, "1", "ClaimAttDocJustTabWk4", finyear);

                }

                itmNumAtt = itmNumAtt + 1;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateAttDocWk5Main() {
        try {
            int itmNumAtt = maxItmWk5;
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysFB;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");

            if ("Y".equals(delWk5)) {
                attL.updOldAttDoc(conn, fetchStringWk5, jTextRefNum.getText(), "ClaimAttDocJustTabWk5");
            }

            for (int i = 0; i < modelAttWk5Main.getRowCount(); i++) {
                String imgFileValue = modelAttWk5Main.getValueAt(i, 3).toString();
                String imgFileDsc = modelAttWk5Main.getValueAt(i, 1).toString();
                String imgFileName = modelAttWk5Main.getValueAt(i, 2).toString();

                if (imgFileValue.trim().length() > 0) {

                    attL.insertAttWkDoc(conn, imgFileName, imgFileDsc, imgFileValue,
                            itmNumAtt, jTextRefNum.getText(), actNextVer, "1", "ClaimAttDocJustTabWk5", finyear);

                }

                itmNumAtt = itmNumAtt + 1;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateWkPlanPeriod() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            if (jTableWk1Activities.getRowCount() > 0 && jTableWk2Activities.getRowCount() == 0
                    && jTableWk3Activities.getRowCount() == 0 && jTableWk4Activities.getRowCount() == 0
                    && jTableWk5Activities.getRowCount() == 0) {

                String sqlWkplanperiod = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] "
                        + "(SERIAL,PLAN_REF_NUM,PROVINCE,DISTRICT,Wk1_PLAN_START_DATE,Wk1_PLAN_END_DATE,"
                        + "ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWkplanperiod);
                pst1.setString(1, "P");
                pst1.setString(2, jTextRefNum.getText());
                pst1.setString(3, jLabelProvince.getText());
                pst1.setString(4, jLabelDistrict.getText());
                pst1.setString(5, formatter.format(jDateChooserWk1From.getDate()));
                pst1.setString(6, formatter.format(jDateChooserWk1To.getDate()));
                pst1.setString(7, actNextVer);
                pst1.setString(8, "1");
                pst1.setString(9, "A");

                pst1.executeUpdate();

            } else if (jTableWk2Activities.getRowCount() > 0 && jTableWk3Activities.getRowCount() == 0
                    && jTableWk4Activities.getRowCount() == 0 && jTableWk5Activities.getRowCount() == 0) {

                String sqlWkplanperiod = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] "
                        + "(SERIAL,PLAN_REF_NUM,PROVINCE,DISTRICT,Wk1_PLAN_START_DATE,Wk1_PLAN_END_DATE,"
                        + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,"
                        + "ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWkplanperiod);
                pst1.setString(1, "P");
                pst1.setString(2, jTextRefNum.getText());
                pst1.setString(3, jLabelProvince.getText());
                pst1.setString(4, jLabelDistrict.getText());
                pst1.setString(5, formatter.format(jDateChooserWk1From.getDate()));
                pst1.setString(6, formatter.format(jDateChooserWk1To.getDate()));
                pst1.setString(7, formatter.format(jDateChooserWk2From.getDate()));
                pst1.setString(8, formatter.format(jDateChooserWk2To.getDate()));
                pst1.setString(9, actNextVer);
                pst1.setString(10, "1");
                pst1.setString(11, "A");

                pst1.executeUpdate();
            } else if (jTableWk3Activities.getRowCount() > 0 && jTableWk4Activities.getRowCount() == 0
                    && jTableWk5Activities.getRowCount() == 0) {

                String sqlWkplanperiod = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] "
                        + "(SERIAL,PLAN_REF_NUM,PROVINCE,DISTRICT,Wk1_PLAN_START_DATE,Wk1_PLAN_END_DATE,"
                        + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,Wk3_PLAN_START_DATE,Wk3_PLAN_END_DATE,"
                        + "ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWkplanperiod);
                pst1.setString(1, "P");
                pst1.setString(2, jTextRefNum.getText());
                pst1.setString(3, jLabelProvince.getText());
                pst1.setString(4, jLabelDistrict.getText());
                pst1.setString(5, formatter.format(jDateChooserWk1From.getDate()));
                pst1.setString(6, formatter.format(jDateChooserWk1To.getDate()));
                pst1.setString(7, formatter.format(jDateChooserWk2From.getDate()));
                pst1.setString(8, formatter.format(jDateChooserWk2To.getDate()));
                pst1.setString(9, formatter.format(jDateChooserWk3From.getDate()));
                pst1.setString(10, formatter.format(jDateChooserWk3To.getDate()));
                pst1.setString(11, actNextVer);
                pst1.setString(12, "1");
                pst1.setString(13, "A");

                pst1.executeUpdate();

            } else if (jTableWk4Activities.getRowCount() > 0 && jTableWk5Activities.getRowCount() == 0) {

                String sqlWkplanperiod = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] "
                        + "(SERIAL,PLAN_REF_NUM,PROVINCE,DISTRICT,Wk1_PLAN_START_DATE,Wk1_PLAN_END_DATE,"
                        + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,Wk3_PLAN_START_DATE,Wk3_PLAN_END_DATE,"
                        + "Wk4_PLAN_START_DATE,Wk4_PLAN_END_DATE,ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWkplanperiod);
                pst1.setString(1, "P");
                pst1.setString(2, jTextRefNum.getText());
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
                pst1.setString(13, actNextVer);
                pst1.setString(14, "1");
                pst1.setString(15, "A");

                pst1.executeUpdate();

            } else if (jTableWk5Activities.getRowCount() > 0) {

                String sqlWkplanperiod = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] "
                        + "(SERIAL,PLAN_REF_NUM,PROVINCE,DISTRICT,Wk1_PLAN_START_DATE,Wk1_PLAN_END_DATE,"
                        + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,Wk3_PLAN_START_DATE,Wk3_PLAN_END_DATE,"
                        + "Wk4_PLAN_START_DATE,Wk4_PLAN_END_DATE,Wk5_PLAN_START_DATE,Wk5_PLAN_END_DATE,ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWkplanperiod);
                pst1.setString(1, "P");
                pst1.setString(2, jTextRefNum.getText());
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
                pst1.setString(13, formatter.format(jDateChooserWk4From.getDate()));
                pst1.setString(14, formatter.format(jDateChooserWk4To.getDate()));
                pst1.setString(15, actNextVer);
                pst1.setString(16, "1");
                pst1.setString(17, "A");

                pst1.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateWkPlanAction() {
        try {
            findProvMan();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String action = "";
            String docStatus = "";

//            if (Integer.parseInt(docVer) == 1) {
            action = "Plan - Amended";
            docStatus = "Amended";
//            }
            String sqlWkplanperiod = "insert into [ClaimsAppSysZvandiri].[dbo].[PlanActTab] "
                    + "(SERIAL,PLAN_REF_NUM,USR_ACTION,DOC_STATUS,"
                    + "ACTIONED_BY_EMP_NUM,ACTIONED_BY_NAM,SEND_TO_EMP_NUM,SEND_TO_NAM,"
                    + "ACTIONED_ON_DATE,ACTIONED_ON_TIME,"
                    + "ACTIONED_ON_COMPUTER,COMMENTS,ACT_VER,DOC_VER,ACT_REC_STA)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            pst1 = conn.prepareStatement(sqlWkplanperiod);
            pst1.setString(1, "P");
            pst1.setString(2, jTextRefNum.getText());
            pst1.setString(3, action);
            pst1.setString(4, docStatus);
            pst1.setString(5, jLabelEmp.getText());
            pst1.setString(6, jLabelLineLogNam.getText());
            pst1.setString(7, supEmpNum);
            pst1.setString(8, supNam);
            pst1.setString(9, jLabelLineDate.getText());
            pst1.setString(10, jLabelLineTime.getText());
            pst1.setString(11, hostName);
            pst1.setString(12, jTextAreaComments.getText());
            pst1.setString(13, actNextVer);
            pst1.setString(14, "1");
            pst1.setString(15, "A");

            pst1.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void regCheck() {

        try {
            if (jTableWk1Activities.getRowCount() > 0) {
                finDayCalcWk1();
                minDateComWk1 = Date.from(minDateWk1.atStartOfDay(zoneId).toInstant());
                maxDateComWk1 = Date.from(maxDateWk1.atStartOfDay(zoneId).toInstant());

            }

            if (jTableWk2Activities.getRowCount() > 0) {
                finDayCalcWk2();
                minDateComWk2 = Date.from(minDateWk2.atStartOfDay(zoneId).toInstant());
                maxDateComWk2 = Date.from(maxDateWk2.atStartOfDay(zoneId).toInstant());

            }
            if (jTableWk3Activities.getRowCount() > 0) {
                finDayCalcWk3();
                minDateComWk3 = Date.from(minDateWk3.atStartOfDay(zoneId).toInstant());
                maxDateComWk3 = Date.from(maxDateWk3.atStartOfDay(zoneId).toInstant());

            }
            if (jTableWk4Activities.getRowCount() > 0) {
                finDayCalcWk4();
                minDateComWk4 = Date.from(minDateWk4.atStartOfDay(zoneId).toInstant());
                maxDateComWk4 = Date.from(maxDateWk4.atStartOfDay(zoneId).toInstant());

            }
            if (jTableWk5Activities.getRowCount() > 0) {
                finDayCalcWk5();
                minDateComWk5 = Date.from(minDateWk5.atStartOfDay(zoneId).toInstant());
                maxDateComWk5 = Date.from(maxDateWk5.atStartOfDay(zoneId).toInstant());

            }

            if (jTextRefNum.getText().trim().length() == 0 && (jTableWk1Activities.getRowCount() == 0 && jTableWk2Activities.getRowCount() == 0
                    && jTableWk3Activities.getRowCount() == 0 && jTableWk4Activities.getRowCount() == 0
                    && jTableWk5Activities.getRowCount() == 0)) {
                JOptionPane.showMessageDialog(this, "Please retrieve record to be modified before saving.");
                jDateChooserWk1From.requestFocusInWindow();
                jDateChooserWk1From.setFocusable(true);
            } else if (jTableWk1Activities.getRowCount() == 0 && jTableWk2Activities.getRowCount() == 0
                    && jTableWk3Activities.getRowCount() == 0 && jTableWk4Activities.getRowCount() == 0
                    && jTableWk5Activities.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Please retrieve record to be modified before saving.");
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
            } else if (jTableWk1Activities.getRowCount() > 0 && (formatter.format(jDateChooserWk1From.getDate()).compareTo(formatter.format(minDateComWk1)) > 0)) {
                JOptionPane.showMessageDialog(this, "Minimum activity date for week 1 cannot be lower than start date.Please check your dates");
                jDateChooserWk1From.requestFocusInWindow();
                jDateChooserWk1From.setFocusable(true);

            } else if (jTableWk1Activities.getRowCount() > 0 && (formatter.format(jDateChooserWk1To.getDate()).compareTo(formatter.format(maxDateComWk1)) < 0)) {
                JOptionPane.showMessageDialog(this, "Maximum activity date for week 1 cannot be greater than end date.Please check your dates");
                jDateChooserWk1To.requestFocusInWindow();
                jDateChooserWk1To.setFocusable(true);

            } else if (jTableWk2Activities.getRowCount() > 0 && (formatter.format(jDateChooserWk2From.getDate()).compareTo(formatter.format(minDateComWk2)) > 0)) {
                JOptionPane.showMessageDialog(this, "Minimum activity date for week 2 cannot be lower than start date.Please check your dates");
                jDateChooserWk2From.requestFocusInWindow();
                jDateChooserWk2From.setFocusable(true);

            } else if (jTableWk2Activities.getRowCount() > 0 && (formatter.format(jDateChooserWk2To.getDate()).compareTo(formatter.format(maxDateComWk2)) < 0)) {
                JOptionPane.showMessageDialog(this, "Maximum activity date for week 2 cannot be greater than end date.Please check your dates");
                jDateChooserWk2To.requestFocusInWindow();
                jDateChooserWk2To.setFocusable(true);

            } else if (jTableWk3Activities.getRowCount() > 0 && (formatter.format(jDateChooserWk3From.getDate()).compareTo(formatter.format(minDateComWk3)) > 0)) {
                JOptionPane.showMessageDialog(this, "Minimum activity date for week 3 cannot be lower than start date.Please check your dates");
                jDateChooserWk3From.requestFocusInWindow();
                jDateChooserWk3From.setFocusable(true);

            } else if (jTableWk3Activities.getRowCount() > 0 && (formatter.format(jDateChooserWk3To.getDate()).compareTo(formatter.format(maxDateComWk3)) < 0)) {
                JOptionPane.showMessageDialog(this, "Maximum activity date for week 3 cannot be greater than end date.Please check your dates");
                jDateChooserWk3To.requestFocusInWindow();
                jDateChooserWk3To.setFocusable(true);

            } else if (jTableWk4Activities.getRowCount() > 0 && (formatter.format(jDateChooserWk4From.getDate()).compareTo(formatter.format(minDateComWk4)) > 0)) {
                JOptionPane.showMessageDialog(this, "Minimum activity date for week 4 cannot be lower than start date.Please check your dates");
                jDateChooserWk4From.requestFocusInWindow();
                jDateChooserWk4From.setFocusable(true);

            } else if (jTableWk4Activities.getRowCount() > 0 && (formatter.format(jDateChooserWk4To.getDate()).compareTo(formatter.format(maxDateComWk4)) < 0)) {
                JOptionPane.showMessageDialog(this, "Maximum activity date for week 4 cannot be greater than end date.Please check your dates");
                jDateChooserWk4To.requestFocusInWindow();
                jDateChooserWk4To.setFocusable(true);

            } else if (jTableWk5Activities.getRowCount() > 0 && (formatter.format(jDateChooserWk5From.getDate()).compareTo(formatter.format(minDateComWk5)) > 0)) {
                JOptionPane.showMessageDialog(this, "Minimum activity date for week 5cannot be lower than start date.Please check your dates");
                jDateChooserWk5From.requestFocusInWindow();
                jDateChooserWk5From.setFocusable(true);

            } else if (jTableWk5Activities.getRowCount() > 0 && (formatter.format(jDateChooserWk5To.getDate()).compareTo(formatter.format(maxDateComWk5)) < 0)) {
                JOptionPane.showMessageDialog(this, "Maximum activity date for week 5 cannot be greater than end date.Please check your dates");
                jDateChooserWk5To.requestFocusInWindow();
                jDateChooserWk5To.setFocusable(true);

            } else {
                jDialogWaiting.setVisible(true);
                jMenuItemClose1.setEnabled(false);
                saveRec();

            }
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

    }

    void fetchdataGenWkC1() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            st.executeQuery("SELECT PROVINCE ,DISTRICT ,WK1_PLAN_START_DATE ,WK1_PLAN_END_DATE,"
                    + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,Wk3_PLAN_START_DATE,Wk3_PLAN_END_DATE, "
                    + "Wk4_PLAN_START_DATE,Wk4_PLAN_END_DATE,Wk5_PLAN_START_DATE,Wk5_PLAN_END_DATE,PLAN_REF_NUM "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab]"
                    + " where PLAN_REF_NUM =" + jTextRefNum.getText() + " and ACT_REC_STA = 'C'");

            ResultSet r = st.getResultSet();

            while (r.next()) {

                jLabelProvince.setText(r.getString(1));
                jLabelDistrict.setText(r.getString(2));
                jLabelRefNum.setText(r.getString(13));

                if ((!("".equals(r.getString(3)))) && (r.getString(5) == null) && (r.getString(7) == null)
                        && (r.getString(9) == null) && (r.getString(11) == null)) {
                    java.util.Date dateWk1From = formatter.parse(r.getString(3));
                    java.util.Date dateWk1To = formatter.parse(r.getString(4));

                    jDateChooserWk1From.setDate(dateWk1From);
                    jDateChooserWk1To.setDate(dateWk1To);
                } else if ((!("".equals(r.getString(3)))) && (!("".equals(r.getString(5)))) && (r.getString(7) == null)
                        && (r.getString(9) == null) && (r.getString(11) == null)) {
                    java.util.Date dateWk1From = formatter.parse(r.getString(3));
                    java.util.Date dateWk1To = formatter.parse(r.getString(4));
                    java.util.Date dateWk2From = formatter.parse(r.getString(5));
                    java.util.Date dateWk2To = formatter.parse(r.getString(6));

                    jDateChooserWk1From.setDate(dateWk1From);
                    jDateChooserWk1To.setDate(dateWk1To);
                    jDateChooserWk2From.setDate(dateWk2From);
                    jDateChooserWk2To.setDate(dateWk2To);

                } else if ((!("".equals(r.getString(3)))) && (!("".equals(r.getString(5)))) && (!("".equals(r.getString(7))))
                        && (r.getString(9) == null) && (r.getString(11) == null)) {
                    java.util.Date dateWk1From = formatter.parse(r.getString(3));
                    java.util.Date dateWk1To = formatter.parse(r.getString(4));
                    java.util.Date dateWk2From = formatter.parse(r.getString(5));
                    java.util.Date dateWk2To = formatter.parse(r.getString(6));
                    java.util.Date dateWk3From = formatter.parse(r.getString(7));
                    java.util.Date dateWk3To = formatter.parse(r.getString(8));

                    jDateChooserWk1From.setDate(dateWk1From);
                    jDateChooserWk1To.setDate(dateWk1To);
                    jDateChooserWk2From.setDate(dateWk2From);
                    jDateChooserWk2To.setDate(dateWk2To);
                    jDateChooserWk3From.setDate(dateWk3From);
                    jDateChooserWk3To.setDate(dateWk3To);

                } else if ((!("".equals(r.getString(3)))) && (!("".equals(r.getString(5)))) && (!("".equals(r.getString(7))))
                        && (!("".equals(r.getString(9)))) && (r.getString(11) == null)) {
                    java.util.Date dateWk1From = formatter.parse(r.getString(3));
                    java.util.Date dateWk1To = formatter.parse(r.getString(4));
                    java.util.Date dateWk2From = formatter.parse(r.getString(5));
                    java.util.Date dateWk2To = formatter.parse(r.getString(6));
                    java.util.Date dateWk3From = formatter.parse(r.getString(7));
                    java.util.Date dateWk3To = formatter.parse(r.getString(8));
                    java.util.Date dateWk4From = formatter.parse(r.getString(9));
                    java.util.Date dateWk4To = formatter.parse(r.getString(10));

                    jDateChooserWk1From.setDate(dateWk1From);
                    jDateChooserWk1To.setDate(dateWk1To);
                    jDateChooserWk2From.setDate(dateWk2From);
                    jDateChooserWk2To.setDate(dateWk2To);
                    jDateChooserWk3From.setDate(dateWk3From);
                    jDateChooserWk3To.setDate(dateWk3To);
                    jDateChooserWk4From.setDate(dateWk4From);
                    jDateChooserWk4To.setDate(dateWk4To);

                } else {
                    java.util.Date dateWk1From = formatter.parse(r.getString(3));
                    java.util.Date dateWk1To = formatter.parse(r.getString(4));
                    java.util.Date dateWk2From = formatter.parse(r.getString(5));
                    java.util.Date dateWk2To = formatter.parse(r.getString(6));
                    java.util.Date dateWk3From = formatter.parse(r.getString(7));
                    java.util.Date dateWk3To = formatter.parse(r.getString(8));
                    java.util.Date dateWk4From = formatter.parse(r.getString(9));
                    java.util.Date dateWk4To = formatter.parse(r.getString(10));
                    java.util.Date dateWk5From = formatter.parse(r.getString(11));
                    java.util.Date dateWk5To = formatter.parse(r.getString(12));

                    jDateChooserWk1From.setDate(dateWk1From);
                    jDateChooserWk1To.setDate(dateWk1To);
                    jDateChooserWk2From.setDate(dateWk2From);
                    jDateChooserWk2To.setDate(dateWk2To);
                    jDateChooserWk3From.setDate(dateWk3From);
                    jDateChooserWk3To.setDate(dateWk3To);
                    jDateChooserWk4From.setDate(dateWk4From);
                    jDateChooserWk4To.setDate(dateWk4To);
                    jDateChooserWk5From.setDate(dateWk4From);
                    jDateChooserWk5To.setDate(dateWk4To);
                }
                origDate = jDateChooserWk1From.getDate();

            }

            findDocVer();

            st1.executeQuery("SELECT COMMENTS FROM [ClaimsAppSysZvandiri].[dbo].[PlanActTab]"
                    + "where PLAN_REF_NUM =" + jTextRefNum.getText() + " and ACT_REC_STA = 'C'");

            ResultSet r1 = st1.getResultSet();

            while (r1.next()) {
                jTextAreaComments.setText(r1.getString(1));
            }
            //    jTabbedPaneMain.setSelectedIndex(0);
            jTextAreaComments.requestFocusInWindow();
            jTextAreaComments.setFocusable(true);
            jTextAreaComments.setLineWrap(true);
            jTextAreaComments.setWrapStyleWord(true);
            jTextAreaComments.setEditable(false);
            jLabelPostAppMod1.setText("Post Approval Modification");
            jLabelPostAppMod2.setText("Post Approval Modification");
            jLabelPostAppMod3.setText("Post Approval Modification");
            jLabelPostAppMod4.setText("Post Approval Modification");
            jLabelPostAppMod5.setText("Post Approval Modification");
        } catch (Exception e) {

            System.out.println(e);
        }
    }

    void fetchdataWkC1() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] "
                    + "where PLAN_REF_NUM =" + jTextRefNum.getText() + " and ACT_REC_STA = 'C'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk1.insertRow(modelWk1.getRowCount(), new Object[]{r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22), r.getString(23)});

            }

        } catch (Exception e) {

        }
    }

    void fetchdataWkC2() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] "
                    + "where PLAN_REF_NUM =" + jTextRefNum.getText() + " and ACT_REC_STA = 'C'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk2.insertRow(modelWk2.getRowCount(), new Object[]{r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22), r.getString(23)});

            }

        } catch (Exception e) {

        }
    }

    void fetchdataWkC3() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] "
                    + "where PLAN_REF_NUM =" + jTextRefNum.getText() + " and ACT_REC_STA = 'C'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk3.insertRow(modelWk3.getRowCount(), new Object[]{r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22), r.getString(23)});

            }

        } catch (Exception e) {

        }
    }

    void fetchdataWkC4() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab] "
                    + "where PLAN_REF_NUM =" + jTextRefNum.getText() + " and ACT_REC_STA = 'C'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk4.insertRow(modelWk4.getRowCount(), new Object[]{r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22), r.getString(23)});

            }

        } catch (Exception e) {

        }
    }

    void fetchdataWkC5() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab] "
                    + "where PLAN_REF_NUM =" + jTextRefNum.getText() + " and ACT_REC_STA = 'C'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk5.insertRow(modelWk5.getRowCount(), new Object[]{r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22), r.getString(23)});

            }

        } catch (Exception e) {

        }
    }

    void fetchdataGenWk1() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            st.executeQuery("SELECT PROVINCE ,DISTRICT ,WK1_PLAN_START_DATE ,WK1_PLAN_END_DATE,"
                    + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,Wk3_PLAN_START_DATE,Wk3_PLAN_END_DATE,"
                    + "Wk4_PLAN_START_DATE,Wk4_PLAN_END_DATE,Wk5_PLAN_START_DATE,Wk5_PLAN_END_DATE,PLAN_REF_NUM "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab]"
                    + " where PLAN_REF_NUM =" + jTextRefNum.getText() + " and ACT_REC_STA = 'A'");

            ResultSet r = st.getResultSet();

            while (r.next()) {

                jLabelProvince.setText(r.getString(1));
                jLabelDistrict.setText(r.getString(2));
                jLabelRefNum.setText(r.getString(13));

                if ((!("".equals(r.getString(3)))) && (r.getString(5) == null) && (r.getString(7) == null)
                        && (r.getString(9) == null) && (r.getString(11) == null)) {
                    java.util.Date dateWk1From = formatter.parse(r.getString(3));
                    java.util.Date dateWk1To = formatter.parse(r.getString(4));

                    jDateChooserWk1From.setDate(dateWk1From);
                    jDateChooserWk1To.setDate(dateWk1To);
                } else if ((!("".equals(r.getString(3)))) && (!("".equals(r.getString(5)))) && (r.getString(7) == null)
                        && (r.getString(9) == null) && (r.getString(11) == null)) {
                    java.util.Date dateWk1From = formatter.parse(r.getString(3));
                    java.util.Date dateWk1To = formatter.parse(r.getString(4));
                    java.util.Date dateWk2From = formatter.parse(r.getString(5));
                    java.util.Date dateWk2To = formatter.parse(r.getString(6));

                    jDateChooserWk1From.setDate(dateWk1From);
                    jDateChooserWk1To.setDate(dateWk1To);
                    jDateChooserWk2From.setDate(dateWk2From);
                    jDateChooserWk2To.setDate(dateWk2To);

                } else if ((!("".equals(r.getString(3)))) && (!("".equals(r.getString(5)))) && (!("".equals(r.getString(7))))
                        && (r.getString(9) == null) && (r.getString(11) == null)) {

                    java.util.Date dateWk1From = formatter.parse(r.getString(3));
                    java.util.Date dateWk1To = formatter.parse(r.getString(4));
                    java.util.Date dateWk2From = formatter.parse(r.getString(5));
                    java.util.Date dateWk2To = formatter.parse(r.getString(6));
                    java.util.Date dateWk3From = formatter.parse(r.getString(7));
                    java.util.Date dateWk3To = formatter.parse(r.getString(8));

                    jDateChooserWk1From.setDate(dateWk1From);
                    jDateChooserWk1To.setDate(dateWk1To);
                    jDateChooserWk2From.setDate(dateWk2From);
                    jDateChooserWk2To.setDate(dateWk2To);
                    jDateChooserWk3From.setDate(dateWk3From);
                    jDateChooserWk3To.setDate(dateWk3To);

                } else if ((!("".equals(r.getString(3)))) && (!("".equals(r.getString(5)))) && (!("".equals(r.getString(7))))
                        && (!("".equals(r.getString(9)))) && (r.getString(11) == null)) {
                    java.util.Date dateWk1From = formatter.parse(r.getString(3));
                    java.util.Date dateWk1To = formatter.parse(r.getString(4));
                    java.util.Date dateWk2From = formatter.parse(r.getString(5));
                    java.util.Date dateWk2To = formatter.parse(r.getString(6));
                    java.util.Date dateWk3From = formatter.parse(r.getString(7));
                    java.util.Date dateWk3To = formatter.parse(r.getString(8));
                    java.util.Date dateWk4From = formatter.parse(r.getString(9));
                    java.util.Date dateWk4To = formatter.parse(r.getString(10));

                    jDateChooserWk1From.setDate(dateWk1From);
                    jDateChooserWk1To.setDate(dateWk1To);
                    jDateChooserWk2From.setDate(dateWk2From);
                    jDateChooserWk2To.setDate(dateWk2To);
                    jDateChooserWk3From.setDate(dateWk3From);
                    jDateChooserWk3To.setDate(dateWk3To);
                    jDateChooserWk4From.setDate(dateWk4From);
                    jDateChooserWk4To.setDate(dateWk4To);

                } else {
                    java.util.Date dateWk1From = formatter.parse(r.getString(3));
                    java.util.Date dateWk1To = formatter.parse(r.getString(4));
                    java.util.Date dateWk2From = formatter.parse(r.getString(5));
                    java.util.Date dateWk2To = formatter.parse(r.getString(6));
                    java.util.Date dateWk3From = formatter.parse(r.getString(7));
                    java.util.Date dateWk3To = formatter.parse(r.getString(8));
                    java.util.Date dateWk4From = formatter.parse(r.getString(9));
                    java.util.Date dateWk4To = formatter.parse(r.getString(10));
                    java.util.Date dateWk5From = formatter.parse(r.getString(11));
                    java.util.Date dateWk5To = formatter.parse(r.getString(12));

                    jDateChooserWk1From.setDate(dateWk1From);
                    jDateChooserWk1To.setDate(dateWk1To);
                    jDateChooserWk2From.setDate(dateWk2From);
                    jDateChooserWk2To.setDate(dateWk2To);
                    jDateChooserWk3From.setDate(dateWk3From);
                    jDateChooserWk3To.setDate(dateWk3To);
                    jDateChooserWk4From.setDate(dateWk4From);
                    jDateChooserWk4To.setDate(dateWk4To);
                    jDateChooserWk5From.setDate(dateWk5From);
                    jDateChooserWk5To.setDate(dateWk5To);
                }
                origDate = jDateChooserWk1From.getDate();

            }

            findDocVer();

            st1.executeQuery("SELECT COMMENTS FROM [ClaimsAppSysZvandiri].[dbo].[PlanActTab]"
                    + "where PLAN_REF_NUM =" + jTextRefNum.getText() + " and ACT_REC_STA = 'A'");

            ResultSet r1 = st1.getResultSet();

            while (r1.next()) {
                jTextAreaComments.setText(r1.getString(1));
            }
            jTabbedPaneMain.setSelectedIndex(5);
            jTextAreaComments.requestFocusInWindow();
            jTextAreaComments.setFocusable(true);
            jTextAreaComments.setLineWrap(true);
            jTextAreaComments.setWrapStyleWord(true);
            jTextAreaComments.setEditable(false);
        } catch (Exception e) {

            System.out.println(e);
        }
    }

    void fetchdataWk1() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] "
                    + "where PLAN_REF_NUM =" + jTextRefNum.getText() + " and ACT_REC_STA = 'A'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk1.insertRow(modelWk1.getRowCount(), new Object[]{r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22), r.getString(23), r.getString(24), r.getString(25), r.getString(26)});

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdataWk2() {
        try {

            DefaultTableModel modelWk2 = (DefaultTableModel) jTableWk2Activities.getModel();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] "
                    + "where PLAN_REF_NUM =" + jTextRefNum.getText() + " and ACT_REC_STA = 'A'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk2.insertRow(modelWk2.getRowCount(), new Object[]{r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22), r.getString(23), r.getString(24), r.getString(25), r.getString(26)});

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdataWk3() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] "
                    + "where PLAN_REF_NUM =" + jTextRefNum.getText() + " and ACT_REC_STA = 'A'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk3.insertRow(modelWk3.getRowCount(), new Object[]{r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22), r.getString(23), r.getString(24), r.getString(25), r.getString(26)});

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdataWk4() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab] "
                    + "where PLAN_REF_NUM =" + jTextRefNum.getText() + " and ACT_REC_STA = 'A'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk4.insertRow(modelWk4.getRowCount(), new Object[]{r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22), r.getString(23), r.getString(24), r.getString(25), r.getString(26)});

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdataWk5() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab] "
                    + "where PLAN_REF_NUM =" + jTextRefNum.getText() + " and ACT_REC_STA = 'A'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk5.insertRow(modelWk5.getRowCount(), new Object[]{r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22), r.getString(23), r.getString(24), r.getString(25), r.getString(26)});

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchAttDocWk1(String refNum) {
        System.out.println("ref numbb " + refNum);
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            System.out.println("ref num " + refNum);
            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            st.executeQuery("SELECT ACT_ITM,attDesc,fileName FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAttDocJustTabWk1] "
                    + "where ACT_REC_STA='A' and REF_NUM='" + refNum + "'");

            ResultSet r = st.getResultSet();
            while (r.next()) {

                modelAttWk1Main.insertRow(modelAttWk1Main.getRowCount(), new Object[]{r.getString(1), r.getString(2),
                    r.getString(3)});

            }

            st1.executeQuery("SELECT max(ACT_ITM)+1 FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAttDocJustTabWk1] "
                    + "where ACT_REC_STA='A' and REF_NUM='" + refNum + "'");

            ResultSet r1 = st1.getResultSet();
            while (r1.next()) {

                maxItmWk1 = r1.getInt(1);
                System.out.println("wk num " + maxItmWk1);
            }

        } catch (Exception e) {
            System.out.println(e);
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

    void clearFields() {
        try {

            DefaultTableModel modelWk1 = (DefaultTableModel) this.jTableWk1Activities.getModel();
            DefaultTableModel modelWk2 = (DefaultTableModel) this.jTableWk2Activities.getModel();
            DefaultTableModel modelWk3 = (DefaultTableModel) this.jTableWk3Activities.getModel();
            DefaultTableModel modelWk4 = (DefaultTableModel) this.jTableWk4Activities.getModel();
            DefaultTableModel modelWk5 = (DefaultTableModel) this.jTableWk5Activities.getModel();

            jLabelProvince.setText("");
            jLabelDistrict.setText("");
            jLabelRefNum.setText("");
            jDateChooserWk1From.setDate(null);
            jDateChooserWk1To.setDate(null);
            jDateChooserWk2From.setDate(null);
            jDateChooserWk2To.setDate(null);
            jDateChooserWk3From.setDate(null);
            jDateChooserWk3To.setDate(null);
            jDateChooserWk4From.setDate(null);
            jDateChooserWk4To.setDate(null);
            jDateChooserWk5From.setDate(null);
            jDateChooserWk5To.setDate(null);

            while (modelWk1.getRowCount() > 0) {
                modelWk1.removeRow(0);
            }

            while (modelWk2.getRowCount() > 0) {
                modelWk2.removeRow(0);
            }

            while (modelWk3.getRowCount() > 0) {
                modelWk3.removeRow(0);
            }

            while (modelWk4.getRowCount() > 0) {
                modelWk4.removeRow(0);
            }

            while (modelWk5.getRowCount() > 0) {
                modelWk5.removeRow(0);
            }

            while (modelAttWk1Main.getRowCount() > 0) {
                modelAttWk1Main.removeRow(0);
            }
        } catch (Exception e) {
            System.out.println(e);
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

    void addWkItmLine() {
        try {

            String Wk1Brk = "0.00";
            String Wk1Lnch = "0.00";
            String Wk1Dinner = "0.00";
            String Wk1UnProvedAcc = "0.00";
            String WkZimTTECHUnprovedAcc="0.00";
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
            }

//            else {
//                if (!(jTextFieldWk1DialogStaffName1.getText().length() == 0)) {
//                    duplicateUser1Count = null;
//                    planWk = "PlanWk1Tab";
//                    planDate = formatter.format(jDateChooserDialogActivityDateFrom.getDate());
//                    empNamNum1 = "EMP_NAM1";
//                    empNam1 = jTextFieldWk1DialogStaffName1.getText();
//                    empNamNum2 = "EMP_NAM2";
//                    empNam2 = jTextFieldWk1DialogStaffName2.getText();
//                    empNamNum3 = "EMP_NAM3";
//                    empNam3 = jTextFieldWk1DialogStaffName3.getText();
//                    empNamNum4 = "EMP_NAM4";
//                    empNam4 = jTextFieldWk1DialogStaffName4.getText();
//
//                    CheckDuplicateUser1();
//                    CheckDuplicateUser2();
//                    CheckDuplicateUser3();
//                    CheckDuplicateUser4();
//                    CheckDuplicateUser5();
//                }
//            }
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
                    WkZimTTECHUnprovedAcc = unProvedZimTTECH;
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

                    if (Integer.parseInt(beforeFirstDotRT) >= 13) {
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

                    if (("D036 CDC-Zim-TTECH".equals(taskDonor))) {
                        Wk1UnProvedAcc = WkZimTTECHUnprovedAcc;

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

            int[] rows = jTableDocAttWk1.getSelectedRows();
            if (rows.length > 0) {

                for (int i = 0; i < rows.length; i++) {

                    listFetchWk1.add(modelAttWk1Main.getValueAt(rows[i] - i, 0).toString());
                    modelAttWk1Main.removeRow(rows[i] - i);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please note that the table is empty.");

            }
            delWk1 = "Y";
            fetchStringWk1 = "('" + String.join("','", listFetchWk1) + "')";

        }
    }

    void deleteFileAttWk2Main() {
        int selectedOption = JOptionPane.showConfirmDialog(this,
                "Do you want to delete selected casual attachement?",
                "Choose", JOptionPane.YES_NO_OPTION);

        if (selectedOption == JOptionPane.YES_OPTION) {

            int[] rows = jTableDocAttWk2.getSelectedRows();
            if (rows.length > 0) {

                for (int i = 0; i < rows.length; i++) {

                    listFetchWk2.add(modelAttWk2Main.getValueAt(rows[i] - i, 0).toString());
                    modelAttWk2Main.removeRow(rows[i] - i);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please note that the table is empty.");

            }
            delWk2 = "Y";
            fetchStringWk2 = "('" + String.join("','", listFetchWk2) + "')";

        }
    }

    void deleteFileAttWk3Main() {
        int selectedOption = JOptionPane.showConfirmDialog(this,
                "Do you want to delete selected casual attachement?",
                "Choose", JOptionPane.YES_NO_OPTION);

        if (selectedOption == JOptionPane.YES_OPTION) {

            int[] rows = jTableDocAttWk3.getSelectedRows();
            if (rows.length > 0) {

                for (int i = 0; i < rows.length; i++) {

                    listFetchWk3.add(modelAttWk3Main.getValueAt(rows[i] - i, 0).toString());
                    modelAttWk3Main.removeRow(rows[i] - i);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please note that the table is empty.");

            }
            delWk3 = "Y";
            fetchStringWk3 = "('" + String.join("','", listFetchWk3) + "')";

        }
    }

    void deleteFileAttWk4Main() {
        int selectedOption = JOptionPane.showConfirmDialog(this,
                "Do you want to delete selected casual attachement?",
                "Choose", JOptionPane.YES_NO_OPTION);

        if (selectedOption == JOptionPane.YES_OPTION) {

            int[] rows = jTableDocAttWk4.getSelectedRows();
            if (rows.length > 0) {

                for (int i = 0; i < rows.length; i++) {

                    listFetchWk4.add(modelAttWk4Main.getValueAt(rows[i] - i, 0).toString());
                    modelAttWk4Main.removeRow(rows[i] - i);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please note that the table is empty.");

            }
            delWk4 = "Y";
            fetchStringWk4 = "('" + String.join("','", listFetchWk4) + "')";

        }
    }

    void deleteFileAttWk5Main() {
        int selectedOption = JOptionPane.showConfirmDialog(this,
                "Do you want to delete selected casual attachement?",
                "Choose", JOptionPane.YES_NO_OPTION);

        if (selectedOption == JOptionPane.YES_OPTION) {

            int[] rows = jTableDocAttWk5.getSelectedRows();
            if (rows.length > 0) {

                for (int i = 0; i < rows.length; i++) {

                    listFetchWk5.add(modelAttWk5Main.getValueAt(rows[i] - i, 0).toString());
                    modelAttWk5Main.removeRow(rows[i] - i);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please note that the table is empty.");

            }
            delWk5 = "Y";
            fetchStringWk5 = "('" + String.join("','", listFetchWk5) + "')";

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

        modelAttWk1Main.addRow(new Object[]{"", jTextAttDocDescMainWk1.getText(), jTextAttDocNameMainWk1.getText(), jTextAttDocPathMainWk1.getText()});
    }

    void addFileAttWk2Main() {

        modelAttWk2Main.addRow(new Object[]{"", jTextAttDocDescMainWk2.getText(), jTextAttDocNameMainWk2.getText(), jTextAttDocPathMainWk2.getText()});
    }

    void addFileAttWk3Main() {

        modelAttWk3Main.addRow(new Object[]{"", jTextAttDocDescMainWk3.getText(), jTextAttDocNameMainWk3.getText(), jTextAttDocPathMainWk3.getText()});
    }

    void addFileAttWk4Main() {

        modelAttWk4Main.addRow(new Object[]{"", jTextAttDocDescMainWk4.getText(), jTextAttDocNameMainWk4.getText(), jTextAttDocPathMainWk4.getText()});
    }

    void addFileAttWk5Main() {

        modelAttWk5Main.addRow(new Object[]{"", jTextAttDocDescMainWk5.getText(), jTextAttDocNameMainWk5.getText(), jTextAttDocPathMainWk5.getText()});
    }

    void refreshAttFields() {
        jTextAttDocDescMainWk1.setText("");
        jTextAttDocNameMainWk1.setText("");
        jTextAttDocPathMainWk1.setText("");
        jTextAttDocDescMainWk2.setText("");
        jTextAttDocNameMainWk2.setText("");
        jTextAttDocPathMainWk2.setText("");
        jTextAttDocDescMainWk3.setText("");
        jTextAttDocNameMainWk3.setText("");
        jTextAttDocPathMainWk3.setText("");
        jTextAttDocDescMainWk4.setText("");
        jTextAttDocNameMainWk4.setText("");
        jTextAttDocPathMainWk4.setText("");
        jTextAttDocDescMainWk5.setText("");
        jTextAttDocNameMainWk5.setText("");
        jTextAttDocPathMainWk5.setText("");
    }

    void searchName1() {
        try {

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
            System.out.println(e);

        }
    }

    void saveRec() {

        try {
            updatePrevRecord();
            updateWk1Plan();
            updateWk2Plan();
            updateWk3Plan();
            updateWk4Plan();
            updateWk5Plan();
            updateWkPlanPeriod();
            updateWkPlanAction();
            updateAttDocWk1Main();
            updateAttDocWk2Main();
            updateAttDocWk3Main();
            updateAttDocWk4Main();
            updateAttDocWk5Main();
            UsrRecEditUpd();
            jDialogWaiting.setVisible(false);
            JOptionPane.showMessageDialog(this, "<html> Plan Ref No. <b>" + jLabelSerial.getText() + " " + jTextRefNum.getText()
                    + "</b> successfully updated.</html>");
            jDialogWaitingEmail.setVisible(true);

            String mailMsg = "<html><body> Dear " + supNam + "<br><br><b>"
                    + jLabelLineLogNam.getText() + "</b> has submitted a plan for your review and approval.<br><br>"
                    + "Please check your Finance System inbox and action<br><br> Kind Regards <br><br>"
                    + " Finance Management System </body></html>";

            String MailMsgTitle = "Plan Reference No. " + jLabelSerial.getText() + " " + jTextRefNum.getText() + "  Approval.";

            emSend.sendMail(MailMsgTitle, supUsrMail, mailMsg, "");

            jDialogWaitingEmail.setVisible(false);

            jMenuItemClose1.setEnabled(true);

            JOptionPane.showMessageDialog(this, "<html> An email has been sent to <b>" + supNam + "</b> for processing.</html>");

            new JFrameMnthPlanPerDiemEdit(jLabelEmp.getText()).setVisible(true);
            setVisible(false);
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

        jDialogWaiting = new javax.swing.JDialog();
        jDialogWaitingEmail = new javax.swing.JDialog();
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
        jTabbedPaneMain = new javax.swing.JTabbedPane();
        jPanelWkOne = new javax.swing.JPanel();
        jLabelWkDuration = new javax.swing.JLabel();
        jDateChooserWk1From = new com.toedter.calendar.JDateChooser();
        jLabelWk1From = new javax.swing.JLabel();
        jLabelWk1To = new javax.swing.JLabel();
        jDateChooserWk1To = new com.toedter.calendar.JDateChooser();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabelLogo1 = new javax.swing.JLabel();
        jLabelProvince1 = new javax.swing.JLabel();
        jLabelOffice1 = new javax.swing.JLabel();
        jLabelPlanRefNo = new javax.swing.JLabel();
        jLabelProvince = new javax.swing.JLabel();
        jLabelDistrict = new javax.swing.JLabel();
        jTextRefNum = new javax.swing.JTextField();
        jLabelSerial = new javax.swing.JLabel();
        jLabelGenLogNam1 = new javax.swing.JLabel();
        jLabelLineLogNam = new javax.swing.JLabel();
        jLabelLineTime = new javax.swing.JLabel();
        jLabelLineDate = new javax.swing.JLabel();
        jLabelEmp = new javax.swing.JLabel();
        jLabelHeaderGen6 = new javax.swing.JLabel();
        jLabelPostAppMod1 = new javax.swing.JLabel();
        jButtonWk1AddActivity = new javax.swing.JButton();
        jButtonWk1DelActivity = new javax.swing.JButton();
        jPanelDooAttMainWk1 = new javax.swing.JPanel();
        jPanelAttachMainWk1 = new javax.swing.JPanel();
        jLabelConfirmationMainWk1 = new javax.swing.JLabel();
        jTextAttDocDescMainWk1 = new javax.swing.JTextField();
        jButtonSelectFileMainWk1 = new javax.swing.JButton();
        jTextAttDocNameMainWk1 = new javax.swing.JTextField();
        jButtonAddAttWk1 = new javax.swing.JButton();
        jButtonDeleteAttWk1 = new javax.swing.JButton();
        jScrollPaneWk1 = new javax.swing.JScrollPane();
        jTableDocAttWk1 = new javax.swing.JTable();
        jTextAttDocPathMainWk1 = new javax.swing.JTextField();
        jPanelSave = new javax.swing.JPanel();
        jScrollPaneWk6 = new javax.swing.JScrollPane();
        jTableWk1Activities = new javax.swing.JTable();
        jLabelRefNo = new javax.swing.JLabel();
        jLabelRefNum = new javax.swing.JLabel();
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
        jLabelPostAppMod2 = new javax.swing.JLabel();
        jPanelDooAttMainWk2 = new javax.swing.JPanel();
        jPanelAttachMainWk2 = new javax.swing.JPanel();
        jLabelConfirmationMainWk2 = new javax.swing.JLabel();
        jTextAttDocDescMainWk2 = new javax.swing.JTextField();
        jButtonSelectFileMainWk2 = new javax.swing.JButton();
        jTextAttDocNameMainWk2 = new javax.swing.JTextField();
        jButtonAddAttWk2 = new javax.swing.JButton();
        jButtonDeleteAttWk2 = new javax.swing.JButton();
        jScrollPaneWk2 = new javax.swing.JScrollPane();
        jTableDocAttWk2 = new javax.swing.JTable();
        jTextAttDocPathMainWk2 = new javax.swing.JTextField();
        jPanelSave1 = new javax.swing.JPanel();
        jScrollPaneWk7 = new javax.swing.JScrollPane();
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
        jLabelHeaderGen11 = new javax.swing.JLabel();
        jLabelPostAppMod3 = new javax.swing.JLabel();
        jPanelDooAttMainWk3 = new javax.swing.JPanel();
        jPanelAttachMainWk3 = new javax.swing.JPanel();
        jLabelConfirmationMainWk3 = new javax.swing.JLabel();
        jTextAttDocDescMainWk3 = new javax.swing.JTextField();
        jButtonSelectFileMainWk3 = new javax.swing.JButton();
        jTextAttDocNameMainWk3 = new javax.swing.JTextField();
        jButtonAddAttWk3 = new javax.swing.JButton();
        jButtonDeleteAttWk3 = new javax.swing.JButton();
        jScrollPaneWk3 = new javax.swing.JScrollPane();
        jTableDocAttWk3 = new javax.swing.JTable();
        jTextAttDocPathMainWk3 = new javax.swing.JTextField();
        jPanelSave2 = new javax.swing.JPanel();
        jScrollPaneWk8 = new javax.swing.JScrollPane();
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
        jLabelLogo7 = new javax.swing.JLabel();
        jButtonWk4DelActivity = new javax.swing.JButton();
        jLabelGenLogNam9 = new javax.swing.JLabel();
        jLabelLineLogNam6 = new javax.swing.JLabel();
        jLabelLineTime6 = new javax.swing.JLabel();
        jLabelLineDate6 = new javax.swing.JLabel();
        jLabelHeaderGen9 = new javax.swing.JLabel();
        jLabelPostAppMod4 = new javax.swing.JLabel();
        jPanelDooAttMainWk4 = new javax.swing.JPanel();
        jPanelAttachMainWk4 = new javax.swing.JPanel();
        jLabelConfirmationMainWk4 = new javax.swing.JLabel();
        jTextAttDocDescMainWk4 = new javax.swing.JTextField();
        jButtonSelectFileMainWk4 = new javax.swing.JButton();
        jTextAttDocNameMainWk4 = new javax.swing.JTextField();
        jButtonAddAttWk4 = new javax.swing.JButton();
        jButtonDeleteAttWk4 = new javax.swing.JButton();
        jScrollPaneWk4 = new javax.swing.JScrollPane();
        jTableDocAttWk4 = new javax.swing.JTable();
        jTextAttDocPathMainWk4 = new javax.swing.JTextField();
        jPanelSave3 = new javax.swing.JPanel();
        jScrollPaneWk9 = new javax.swing.JScrollPane();
        jTableWk4Activities = new javax.swing.JTable();
        jPanelWkFive = new javax.swing.JPanel();
        jLabelWkDuration5 = new javax.swing.JLabel();
        jDateChooserWk5From = new com.toedter.calendar.JDateChooser();
        jLabelWk5From = new javax.swing.JLabel();
        jLabelWk5To = new javax.swing.JLabel();
        jDateChooserWk5To = new com.toedter.calendar.JDateChooser();
        jButtonWk5AddActivity = new javax.swing.JButton();
        jSeparator45 = new javax.swing.JSeparator();
        jSeparator46 = new javax.swing.JSeparator();
        jLabelLogo5 = new javax.swing.JLabel();
        jButtonWk5DelActivity = new javax.swing.JButton();
        jLabelGenLogNam7 = new javax.swing.JLabel();
        jLabelLineLogNam4 = new javax.swing.JLabel();
        jLabelLineTime4 = new javax.swing.JLabel();
        jLabelLineDate4 = new javax.swing.JLabel();
        jLabelHeaderGen10 = new javax.swing.JLabel();
        jLabelPostAppMod5 = new javax.swing.JLabel();
        jPanelDooAttMainWk5 = new javax.swing.JPanel();
        jPanelAttachMainWk5 = new javax.swing.JPanel();
        jLabelConfirmationMainWk5 = new javax.swing.JLabel();
        jTextAttDocDescMainWk5 = new javax.swing.JTextField();
        jButtonSelectFileMainWk5 = new javax.swing.JButton();
        jTextAttDocNameMainWk5 = new javax.swing.JTextField();
        jButtonAddAttWk5 = new javax.swing.JButton();
        jButtonDeleteAttWk5 = new javax.swing.JButton();
        jScrollPaneWk5 = new javax.swing.JScrollPane();
        jTableDocAttWk5 = new javax.swing.JTable();
        jTextAttDocPathMainWk5 = new javax.swing.JTextField();
        jPanelSave4 = new javax.swing.JPanel();
        jScrollPaneWk10 = new javax.swing.JScrollPane();
        jTableWk5Activities = new javax.swing.JTable();
        jPanelWkComments = new javax.swing.JPanel();
        jSeparator35 = new javax.swing.JSeparator();
        jLabelLogo4 = new javax.swing.JLabel();
        jLabelLineLogNam3 = new javax.swing.JLabel();
        jLabelGenLogNam6 = new javax.swing.JLabel();
        jLabelLineDate3 = new javax.swing.JLabel();
        jLabelLineTime3 = new javax.swing.JLabel();
        jLabelHeaderGen8 = new javax.swing.JLabel();
        jLabelCommentsHeading = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextAreaComments = new javax.swing.JTextArea();
        jSeparator36 = new javax.swing.JSeparator();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuNew = new javax.swing.JMenu();
        jMenuItemPlanPerDiem = new javax.swing.JMenuItem();
        jSeparator13 = new javax.swing.JPopupMenu.Separator();
        jMenuItemPerDiemAcquittal = new javax.swing.JMenuItem();
        jSeparator47 = new javax.swing.JPopupMenu.Separator();
        jMenuEdit = new javax.swing.JMenu();
        jMenuMonPlanEdit = new javax.swing.JMenuItem();
        jSeparator11 = new javax.swing.JPopupMenu.Separator();
        jMenuItemClose1 = new javax.swing.JMenuItem();
        jSeparator55 = new javax.swing.JPopupMenu.Separator();
        jMenuItemClose = new javax.swing.JMenuItem();
        jSeparator48 = new javax.swing.JPopupMenu.Separator();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuRequest = new javax.swing.JMenu();
        jMenuItemSupApp = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAccMgrRev = new javax.swing.JMenuItem();
        jSeparator12 = new javax.swing.JPopupMenu.Separator();
        jMenuItemHeadApp = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSchPerDiem = new javax.swing.JMenuItem();
        jSeparator23 = new javax.swing.JPopupMenu.Separator();
        jMenuItemView = new javax.swing.JMenuItem();
        jMenuAcquittal = new javax.swing.JMenu();
        jMenuItemAcqSupApp = new javax.swing.JMenuItem();
        jSeparator14 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqAccApp = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqHeadApp = new javax.swing.JMenuItem();
        jSeparator17 = new javax.swing.JPopupMenu.Separator();
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
            .addGap(0, 607, Short.MAX_VALUE)
        );
        jDialogWaitingLayout.setVerticalGroup(
            jDialogWaitingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
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
        jLabelWk1DialogActivityDesc.setBounds(10, 300, 100, 20);
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanelWkOne.setBackground(new java.awt.Color(255, 255, 204));
        jPanelWkOne.setLayout(null);

        jLabelWkDuration.setText("Week Duration");
        jPanelWkOne.add(jLabelWkDuration);
        jLabelWkDuration.setBounds(30, 170, 90, 30);

        jDateChooserWk1From.setDateFormatString("yyyy-MM-dd");
        jPanelWkOne.add(jDateChooserWk1From);
        jDateChooserWk1From.setBounds(210, 170, 120, 30);

        jLabelWk1From.setText("From");
        jPanelWkOne.add(jLabelWk1From);
        jLabelWk1From.setBounds(150, 170, 41, 30);

        jLabelWk1To.setText("To");
        jPanelWkOne.add(jLabelWk1To);
        jLabelWk1To.setBounds(390, 170, 41, 30);

        jDateChooserWk1To.setDateFormatString("yyyy-MM-dd");
        jPanelWkOne.add(jDateChooserWk1To);
        jDateChooserWk1To.setBounds(440, 170, 120, 30);
        jPanelWkOne.add(jSeparator1);
        jSeparator1.setBounds(30, 210, 1280, 10);
        jPanelWkOne.add(jSeparator2);
        jSeparator2.setBounds(30, 160, 1280, 5);

        jLabelLogo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelWkOne.add(jLabelLogo1);
        jLabelLogo1.setBounds(10, 5, 220, 115);

        jLabelProvince1.setMinimumSize(new java.awt.Dimension(130, 130));
        jLabelProvince1.setName(""); // NOI18N
        jPanelWkOne.add(jLabelProvince1);
        jLabelProvince1.setBounds(320, 120, 70, 30);

        jLabelOffice1.setMinimumSize(new java.awt.Dimension(130, 130));
        jLabelOffice1.setName(""); // NOI18N
        jPanelWkOne.add(jLabelOffice1);
        jLabelOffice1.setBounds(820, 120, 70, 30);

        jLabelPlanRefNo.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelPlanRefNo.setText("Plan Ref No.");
        jPanelWkOne.add(jLabelPlanRefNo);
        jLabelPlanRefNo.setBounds(30, 120, 100, 30);
        jPanelWkOne.add(jLabelProvince);
        jLabelProvince.setBounds(440, 120, 320, 30);
        jPanelWkOne.add(jLabelDistrict);
        jLabelDistrict.setBounds(930, 120, 230, 30);

        jTextRefNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextRefNumActionPerformed(evt);
            }
        });
        jPanelWkOne.add(jTextRefNum);
        jTextRefNum.setBounds(190, 120, 70, 30);

        jLabelSerial.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelSerial.setText("P");
        jPanelWkOne.add(jLabelSerial);
        jLabelSerial.setBounds(140, 120, 20, 30);

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
        jPanelWkOne.add(jLabelEmp);
        jLabelEmp.setBounds(1040, 130, 70, 30);

        jLabelHeaderGen6.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen6.setText("MONTHLY  PLAN ");
        jPanelWkOne.add(jLabelHeaderGen6);
        jLabelHeaderGen6.setBounds(450, 40, 390, 40);

        jLabelPostAppMod1.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jPanelWkOne.add(jLabelPostAppMod1);
        jLabelPostAppMod1.setBounds(480, 80, 240, 30);

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

        jPanelDooAttMainWk1.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDooAttMainWk1.setLayout(null);

        jPanelAttachMainWk1.setBackground(new java.awt.Color(255, 255, 255));
        jPanelAttachMainWk1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelAttachMainWk1.setLayout(null);

        jLabelConfirmationMainWk1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelConfirmationMainWk1.setText(" Enter Attachment Description below and select File");
        jLabelConfirmationMainWk1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelAttachMainWk1.add(jLabelConfirmationMainWk1);
        jLabelConfirmationMainWk1.setBounds(0, 0, 280, 25);

        jTextAttDocDescMainWk1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocDescMainWk1ActionPerformed(evt);
            }
        });
        jPanelAttachMainWk1.add(jTextAttDocDescMainWk1);
        jTextAttDocDescMainWk1.setBounds(5, 30, 170, 25);

        jButtonSelectFileMainWk1.setText("Select File");
        jButtonSelectFileMainWk1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectFileMainWk1ActionPerformed(evt);
            }
        });
        jPanelAttachMainWk1.add(jButtonSelectFileMainWk1);
        jButtonSelectFileMainWk1.setBounds(180, 30, 95, 25);

        jTextAttDocNameMainWk1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextAttDocNameMainWk1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocNameMainWk1ActionPerformed(evt);
            }
        });
        jPanelAttachMainWk1.add(jTextAttDocNameMainWk1);
        jTextAttDocNameMainWk1.setBounds(5, 60, 270, 25);

        jButtonAddAttWk1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonAddAttWk1.setText("Add File");
        jButtonAddAttWk1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddAttWk1ActionPerformed(evt);
            }
        });
        jPanelAttachMainWk1.add(jButtonAddAttWk1);
        jButtonAddAttWk1.setBounds(30, 95, 73, 20);

        jButtonDeleteAttWk1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonDeleteAttWk1.setText("Delete File");
        jButtonDeleteAttWk1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteAttWk1ActionPerformed(evt);
            }
        });
        jPanelAttachMainWk1.add(jButtonDeleteAttWk1);
        jButtonDeleteAttWk1.setBounds(170, 95, 85, 20);

        jPanelDooAttMainWk1.add(jPanelAttachMainWk1);
        jPanelAttachMainWk1.setBounds(0, 0, 280, 140);

        jTableDocAttWk1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item No", "Description", "File Name", "File Path"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk1.setViewportView(jTableDocAttWk1);

        jPanelDooAttMainWk1.add(jScrollPaneWk1);
        jScrollPaneWk1.setBounds(280, 0, 460, 140);

        jPanelWkOne.add(jPanelDooAttMainWk1);
        jPanelDooAttMainWk1.setBounds(25, 530, 740, 140);

        jTextAttDocPathMainWk1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocPathMainWk1ActionPerformed(evt);
            }
        });
        jPanelWkOne.add(jTextAttDocPathMainWk1);
        jTextAttDocPathMainWk1.setBounds(210, 650, 230, 25);

        jPanelSave.setBackground(new java.awt.Color(255, 255, 255));
        jPanelSave.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelSave.setLayout(null);
        jPanelWkOne.add(jPanelSave);
        jPanelSave.setBounds(765, 530, 555, 140);

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
        jScrollPaneWk6.setViewportView(jTableWk1Activities);

        jPanelWkOne.add(jScrollPaneWk6);
        jScrollPaneWk6.setBounds(30, 220, 1290, 310);

        jLabelRefNo.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelRefNo.setText("Ref No.");
        jPanelWkOne.add(jLabelRefNo);
        jLabelRefNo.setBounds(1090, 70, 50, 30);

        jLabelRefNum.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jPanelWkOne.add(jLabelRefNum);
        jLabelRefNum.setBounds(1170, 70, 70, 30);

        jTabbedPaneMain.addTab("Week One", jPanelWkOne);

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
        jLabelLogo2.setBounds(10, 10, 220, 115);

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

        jLabelPostAppMod2.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jPanelWkTwo.add(jLabelPostAppMod2);
        jLabelPostAppMod2.setBounds(480, 80, 240, 30);

        jPanelDooAttMainWk2.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDooAttMainWk2.setLayout(null);

        jPanelAttachMainWk2.setBackground(new java.awt.Color(255, 255, 255));
        jPanelAttachMainWk2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelAttachMainWk2.setLayout(null);

        jLabelConfirmationMainWk2.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelConfirmationMainWk2.setText(" Enter Attachment Description below and select File");
        jLabelConfirmationMainWk2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelAttachMainWk2.add(jLabelConfirmationMainWk2);
        jLabelConfirmationMainWk2.setBounds(0, 0, 280, 25);

        jTextAttDocDescMainWk2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocDescMainWk2ActionPerformed(evt);
            }
        });
        jPanelAttachMainWk2.add(jTextAttDocDescMainWk2);
        jTextAttDocDescMainWk2.setBounds(5, 30, 170, 25);

        jButtonSelectFileMainWk2.setText("Select File");
        jButtonSelectFileMainWk2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectFileMainWk2ActionPerformed(evt);
            }
        });
        jPanelAttachMainWk2.add(jButtonSelectFileMainWk2);
        jButtonSelectFileMainWk2.setBounds(180, 30, 95, 25);

        jTextAttDocNameMainWk2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextAttDocNameMainWk2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocNameMainWk2ActionPerformed(evt);
            }
        });
        jPanelAttachMainWk2.add(jTextAttDocNameMainWk2);
        jTextAttDocNameMainWk2.setBounds(5, 60, 270, 25);

        jButtonAddAttWk2.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonAddAttWk2.setText("Add File");
        jButtonAddAttWk2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddAttWk2ActionPerformed(evt);
            }
        });
        jPanelAttachMainWk2.add(jButtonAddAttWk2);
        jButtonAddAttWk2.setBounds(30, 95, 73, 20);

        jButtonDeleteAttWk2.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonDeleteAttWk2.setText("Delete File");
        jButtonDeleteAttWk2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteAttWk2ActionPerformed(evt);
            }
        });
        jPanelAttachMainWk2.add(jButtonDeleteAttWk2);
        jButtonDeleteAttWk2.setBounds(170, 95, 85, 20);

        jPanelDooAttMainWk2.add(jPanelAttachMainWk2);
        jPanelAttachMainWk2.setBounds(0, 0, 280, 140);

        jTableDocAttWk2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item No", "Description", "File Name", "File Path"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk2.setViewportView(jTableDocAttWk2);

        jPanelDooAttMainWk2.add(jScrollPaneWk2);
        jScrollPaneWk2.setBounds(280, 0, 460, 140);

        jPanelWkTwo.add(jPanelDooAttMainWk2);
        jPanelDooAttMainWk2.setBounds(25, 530, 740, 140);

        jTextAttDocPathMainWk2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocPathMainWk2ActionPerformed(evt);
            }
        });
        jPanelWkTwo.add(jTextAttDocPathMainWk2);
        jTextAttDocPathMainWk2.setBounds(210, 650, 230, 25);

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
        jScrollPaneWk7.setViewportView(jTableWk2Activities);

        jPanelWkTwo.add(jScrollPaneWk7);
        jScrollPaneWk7.setBounds(25, 220, 1290, 310);

        jTabbedPaneMain.addTab("Week Two", jPanelWkTwo);

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
        jLabelLogo3.setBounds(10, 10, 220, 115);

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

        jLabelHeaderGen11.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen11.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeaderGen11.setText("MONTHLY  PLAN ");
        jPanelWkThree.add(jLabelHeaderGen11);
        jLabelHeaderGen11.setBounds(450, 40, 420, 40);

        jLabelPostAppMod3.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jPanelWkThree.add(jLabelPostAppMod3);
        jLabelPostAppMod3.setBounds(480, 80, 240, 30);

        jPanelDooAttMainWk3.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDooAttMainWk3.setLayout(null);

        jPanelAttachMainWk3.setBackground(new java.awt.Color(255, 255, 255));
        jPanelAttachMainWk3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelAttachMainWk3.setLayout(null);

        jLabelConfirmationMainWk3.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelConfirmationMainWk3.setText(" Enter Attachment Description below and select File");
        jLabelConfirmationMainWk3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelAttachMainWk3.add(jLabelConfirmationMainWk3);
        jLabelConfirmationMainWk3.setBounds(0, 0, 280, 25);

        jTextAttDocDescMainWk3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocDescMainWk3ActionPerformed(evt);
            }
        });
        jPanelAttachMainWk3.add(jTextAttDocDescMainWk3);
        jTextAttDocDescMainWk3.setBounds(5, 30, 170, 25);

        jButtonSelectFileMainWk3.setText("Select File");
        jButtonSelectFileMainWk3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectFileMainWk3ActionPerformed(evt);
            }
        });
        jPanelAttachMainWk3.add(jButtonSelectFileMainWk3);
        jButtonSelectFileMainWk3.setBounds(180, 30, 95, 25);

        jTextAttDocNameMainWk3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextAttDocNameMainWk3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocNameMainWk3ActionPerformed(evt);
            }
        });
        jPanelAttachMainWk3.add(jTextAttDocNameMainWk3);
        jTextAttDocNameMainWk3.setBounds(5, 60, 270, 25);

        jButtonAddAttWk3.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonAddAttWk3.setText("Add File");
        jButtonAddAttWk3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddAttWk3ActionPerformed(evt);
            }
        });
        jPanelAttachMainWk3.add(jButtonAddAttWk3);
        jButtonAddAttWk3.setBounds(30, 95, 73, 20);

        jButtonDeleteAttWk3.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonDeleteAttWk3.setText("Delete File");
        jButtonDeleteAttWk3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteAttWk3ActionPerformed(evt);
            }
        });
        jPanelAttachMainWk3.add(jButtonDeleteAttWk3);
        jButtonDeleteAttWk3.setBounds(170, 95, 85, 20);

        jPanelDooAttMainWk3.add(jPanelAttachMainWk3);
        jPanelAttachMainWk3.setBounds(0, 0, 280, 140);

        jTableDocAttWk3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item No", "Description", "File Name", "File Path"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk3.setViewportView(jTableDocAttWk3);

        jPanelDooAttMainWk3.add(jScrollPaneWk3);
        jScrollPaneWk3.setBounds(280, 0, 460, 140);

        jPanelWkThree.add(jPanelDooAttMainWk3);
        jPanelDooAttMainWk3.setBounds(25, 530, 740, 140);

        jTextAttDocPathMainWk3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocPathMainWk3ActionPerformed(evt);
            }
        });
        jPanelWkThree.add(jTextAttDocPathMainWk3);
        jTextAttDocPathMainWk3.setBounds(210, 650, 230, 25);

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
        jScrollPaneWk8.setViewportView(jTableWk3Activities);

        jPanelWkThree.add(jScrollPaneWk8);
        jScrollPaneWk8.setBounds(25, 220, 1290, 310);

        jTabbedPaneMain.addTab("Week Three", jPanelWkThree);

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

        jLabelLogo7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelWkFour.add(jLabelLogo7);
        jLabelLogo7.setBounds(10, 10, 220, 115);

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

        jLabelGenLogNam9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam9.setForeground(new java.awt.Color(255, 255, 255));
        jLabelGenLogNam9.setText("Logged In");
        jPanelWkFour.add(jLabelGenLogNam9);
        jLabelGenLogNam9.setBounds(1090, 30, 100, 30);

        jLabelLineLogNam6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam6.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFour.add(jLabelLineLogNam6);
        jLabelLineLogNam6.setBounds(1180, 30, 160, 30);

        jLabelLineTime6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime6.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFour.add(jLabelLineTime6);
        jLabelLineTime6.setBounds(1250, 0, 80, 30);

        jLabelLineDate6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate6.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFour.add(jLabelLineDate6);
        jLabelLineDate6.setBounds(1090, 0, 110, 30);

        jLabelHeaderGen9.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen9.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeaderGen9.setText("MONTHLY  PLAN ");
        jPanelWkFour.add(jLabelHeaderGen9);
        jLabelHeaderGen9.setBounds(450, 40, 420, 40);

        jLabelPostAppMod4.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jPanelWkFour.add(jLabelPostAppMod4);
        jLabelPostAppMod4.setBounds(480, 80, 240, 30);

        jPanelDooAttMainWk4.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDooAttMainWk4.setLayout(null);

        jPanelAttachMainWk4.setBackground(new java.awt.Color(255, 255, 255));
        jPanelAttachMainWk4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelAttachMainWk4.setLayout(null);

        jLabelConfirmationMainWk4.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelConfirmationMainWk4.setText(" Enter Attachment Description below and select File");
        jLabelConfirmationMainWk4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelAttachMainWk4.add(jLabelConfirmationMainWk4);
        jLabelConfirmationMainWk4.setBounds(0, 0, 280, 25);

        jTextAttDocDescMainWk4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocDescMainWk4ActionPerformed(evt);
            }
        });
        jPanelAttachMainWk4.add(jTextAttDocDescMainWk4);
        jTextAttDocDescMainWk4.setBounds(5, 30, 170, 25);

        jButtonSelectFileMainWk4.setText("Select File");
        jButtonSelectFileMainWk4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectFileMainWk4ActionPerformed(evt);
            }
        });
        jPanelAttachMainWk4.add(jButtonSelectFileMainWk4);
        jButtonSelectFileMainWk4.setBounds(180, 30, 95, 25);

        jTextAttDocNameMainWk4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextAttDocNameMainWk4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocNameMainWk4ActionPerformed(evt);
            }
        });
        jPanelAttachMainWk4.add(jTextAttDocNameMainWk4);
        jTextAttDocNameMainWk4.setBounds(5, 60, 270, 25);

        jButtonAddAttWk4.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonAddAttWk4.setText("Add File");
        jButtonAddAttWk4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddAttWk4ActionPerformed(evt);
            }
        });
        jPanelAttachMainWk4.add(jButtonAddAttWk4);
        jButtonAddAttWk4.setBounds(30, 95, 73, 20);

        jButtonDeleteAttWk4.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonDeleteAttWk4.setText("Delete File");
        jButtonDeleteAttWk4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteAttWk4ActionPerformed(evt);
            }
        });
        jPanelAttachMainWk4.add(jButtonDeleteAttWk4);
        jButtonDeleteAttWk4.setBounds(170, 95, 85, 20);

        jPanelDooAttMainWk4.add(jPanelAttachMainWk4);
        jPanelAttachMainWk4.setBounds(0, 0, 280, 140);

        jTableDocAttWk4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item No", "Description", "File Name", "File Path"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk4.setViewportView(jTableDocAttWk4);

        jPanelDooAttMainWk4.add(jScrollPaneWk4);
        jScrollPaneWk4.setBounds(280, 0, 460, 140);

        jPanelWkFour.add(jPanelDooAttMainWk4);
        jPanelDooAttMainWk4.setBounds(25, 530, 740, 140);

        jTextAttDocPathMainWk4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocPathMainWk4ActionPerformed(evt);
            }
        });
        jPanelWkFour.add(jTextAttDocPathMainWk4);
        jTextAttDocPathMainWk4.setBounds(210, 650, 230, 25);

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
        jScrollPaneWk9.setViewportView(jTableWk4Activities);

        jPanelWkFour.add(jScrollPaneWk9);
        jScrollPaneWk9.setBounds(25, 220, 1290, 310);

        jTabbedPaneMain.addTab("Week Four", jPanelWkFour);

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
        jPanelWkFive.add(jSeparator45);
        jSeparator45.setBounds(30, 200, 1280, 10);
        jPanelWkFive.add(jSeparator46);
        jSeparator46.setBounds(30, 150, 1280, 10);

        jLabelLogo5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelWkFive.add(jLabelLogo5);
        jLabelLogo5.setBounds(10, 10, 220, 115);

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

        jLabelPostAppMod5.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jPanelWkFive.add(jLabelPostAppMod5);
        jLabelPostAppMod5.setBounds(480, 80, 240, 30);

        jPanelDooAttMainWk5.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDooAttMainWk5.setLayout(null);

        jPanelAttachMainWk5.setBackground(new java.awt.Color(255, 255, 255));
        jPanelAttachMainWk5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelAttachMainWk5.setLayout(null);

        jLabelConfirmationMainWk5.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelConfirmationMainWk5.setText(" Enter Attachment Description below and select File");
        jLabelConfirmationMainWk5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelAttachMainWk5.add(jLabelConfirmationMainWk5);
        jLabelConfirmationMainWk5.setBounds(0, 0, 280, 25);

        jTextAttDocDescMainWk5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocDescMainWk5ActionPerformed(evt);
            }
        });
        jPanelAttachMainWk5.add(jTextAttDocDescMainWk5);
        jTextAttDocDescMainWk5.setBounds(5, 30, 170, 25);

        jButtonSelectFileMainWk5.setText("Select File");
        jButtonSelectFileMainWk5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectFileMainWk5ActionPerformed(evt);
            }
        });
        jPanelAttachMainWk5.add(jButtonSelectFileMainWk5);
        jButtonSelectFileMainWk5.setBounds(180, 30, 95, 25);

        jTextAttDocNameMainWk5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextAttDocNameMainWk5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocNameMainWk5ActionPerformed(evt);
            }
        });
        jPanelAttachMainWk5.add(jTextAttDocNameMainWk5);
        jTextAttDocNameMainWk5.setBounds(5, 60, 270, 25);

        jButtonAddAttWk5.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonAddAttWk5.setText("Add File");
        jButtonAddAttWk5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddAttWk5ActionPerformed(evt);
            }
        });
        jPanelAttachMainWk5.add(jButtonAddAttWk5);
        jButtonAddAttWk5.setBounds(30, 95, 73, 20);

        jButtonDeleteAttWk5.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonDeleteAttWk5.setText("Delete File");
        jButtonDeleteAttWk5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteAttWk5ActionPerformed(evt);
            }
        });
        jPanelAttachMainWk5.add(jButtonDeleteAttWk5);
        jButtonDeleteAttWk5.setBounds(170, 95, 85, 20);

        jPanelDooAttMainWk5.add(jPanelAttachMainWk5);
        jPanelAttachMainWk5.setBounds(0, 0, 280, 140);

        jTableDocAttWk5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item No", "Description", "File Name", "File Path"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk5.setViewportView(jTableDocAttWk5);

        jPanelDooAttMainWk5.add(jScrollPaneWk5);
        jScrollPaneWk5.setBounds(280, 0, 460, 140);

        jPanelWkFive.add(jPanelDooAttMainWk5);
        jPanelDooAttMainWk5.setBounds(25, 530, 740, 140);

        jTextAttDocPathMainWk5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttDocPathMainWk5ActionPerformed(evt);
            }
        });
        jPanelWkFive.add(jTextAttDocPathMainWk5);
        jTextAttDocPathMainWk5.setBounds(210, 650, 230, 25);

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
        jScrollPaneWk10.setViewportView(jTableWk5Activities);

        jPanelWkFive.add(jScrollPaneWk10);
        jScrollPaneWk10.setBounds(25, 220, 1290, 310);

        jTabbedPaneMain.addTab("Week Five", jPanelWkFive);

        jPanelWkComments.setBackground(new java.awt.Color(209, 54, 127));
        jPanelWkComments.setLayout(null);
        jPanelWkComments.add(jSeparator35);
        jSeparator35.setBounds(30, 150, 1280, 10);

        jLabelLogo4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelWkComments.add(jLabelLogo4);
        jLabelLogo4.setBounds(10, 10, 220, 100);

        jLabelLineLogNam3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam3.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkComments.add(jLabelLineLogNam3);
        jLabelLineLogNam3.setBounds(1180, 30, 160, 30);

        jLabelGenLogNam6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam6.setForeground(new java.awt.Color(255, 255, 255));
        jLabelGenLogNam6.setText("Logged In");
        jPanelWkComments.add(jLabelGenLogNam6);
        jLabelGenLogNam6.setBounds(1090, 30, 100, 30);

        jLabelLineDate3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate3.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkComments.add(jLabelLineDate3);
        jLabelLineDate3.setBounds(1090, 0, 110, 30);

        jLabelLineTime3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime3.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkComments.add(jLabelLineTime3);
        jLabelLineTime3.setBounds(1250, 0, 80, 30);

        jLabelHeaderGen8.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen8.setText("MONTHLY  PLAN ");
        jPanelWkComments.add(jLabelHeaderGen8);
        jLabelHeaderGen8.setBounds(450, 40, 420, 40);

        jLabelCommentsHeading.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelCommentsHeading.setForeground(new java.awt.Color(255, 255, 255));
        jLabelCommentsHeading.setText("Areas that require your attention ");
        jPanelWkComments.add(jLabelCommentsHeading);
        jLabelCommentsHeading.setBounds(30, 160, 590, 40);

        jTextAreaComments.setColumns(20);
        jTextAreaComments.setRows(5);
        jScrollPane7.setViewportView(jTextAreaComments);

        jPanelWkComments.add(jScrollPane7);
        jScrollPane7.setBounds(30, 220, 780, 280);
        jPanelWkComments.add(jSeparator36);
        jSeparator36.setBounds(30, 200, 1280, 10);

        jTabbedPaneMain.addTab("Comments", jPanelWkComments);

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
        jMenuFile.add(jSeparator47);

        jMenuEdit.setText("Edit");

        jMenuMonPlanEdit.setText("Per Diem Monthly Plan");
        jMenuMonPlanEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuMonPlanEditActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuMonPlanEdit);

        jMenuFile.add(jMenuEdit);
        jMenuFile.add(jSeparator11);

        jMenuItemClose1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemClose1.setText("Modify");
        jMenuItemClose1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemClose1ActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemClose1);
        jMenuFile.add(jSeparator55);

        jMenuItemClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemClose.setText("Close");
        jMenuItemClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemClose);
        jMenuFile.add(jSeparator48);

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
        jMenuRequest.add(jSeparator12);

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
        jMenuAcquittal.add(jSeparator14);

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
        jMenuAcquittal.add(jSeparator17);

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
            .addComponent(jTabbedPaneMain)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneMain)
        );

        setSize(new java.awt.Dimension(1376, 760));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextRefNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextRefNumActionPerformed
        clearFields();
        regInitCheck();

    }//GEN-LAST:event_jTextRefNumActionPerformed

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

    private void jMenuItemClose1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemClose1ActionPerformed

        regCheck();
    }//GEN-LAST:event_jMenuItemClose1ActionPerformed

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
                jDialogWk1.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonWk2AddActivityActionPerformed

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

    private void jTextAttDocDescMainWk1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocDescMainWk1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocDescMainWk1ActionPerformed

    private void jButtonSelectFileMainWk1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectFileMainWk1ActionPerformed
        if (modelWk1.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "File attachment is not possible if activities have not been added. Please add activities first. ");
        } else {
            savePDFToDB pdfDB = new savePDFToDB();
            pdfDB.pdfChooser();
            String attFileName = pdfDB.fileName;

            File file = new File(attFileName);
            long attFileLength = file.length();
            if (attFileLength < 512000) {
                jTextAttDocNameMainWk1.setText(file.getName());
                jTextAttDocPathMainWk1.setText(attFileName);

            } else if (attFileLength > 512000) {
                JOptionPane.showMessageDialog(this, "File size cannot be greater than 512Kb. ");
            }
        }
    }//GEN-LAST:event_jButtonSelectFileMainWk1ActionPerformed

    private void jTextAttDocNameMainWk1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocNameMainWk1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocNameMainWk1ActionPerformed

    private void jButtonAddAttWk1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddAttWk1ActionPerformed
        if ("".equals(jTextAttDocDescMainWk1.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter attached file description.");
            jTextAttDocDescMainWk1.requestFocusInWindow();
            jTextAttDocDescMainWk1.setFocusable(true);
        } else if ("".equals(jTextAttDocNameMainWk1.getText())) {
            JOptionPane.showMessageDialog(this, "Please note that no file has been selected.");
            jTextAttDocNameMainWk1.requestFocusInWindow();
            jTextAttDocNameMainWk1.setFocusable(true);
        } else {
            addFileAttWk1Main();
            refreshAttFields();

        }
    }//GEN-LAST:event_jButtonAddAttWk1ActionPerformed

    private void jButtonDeleteAttWk1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteAttWk1ActionPerformed
        deleteFileAttWk1Main();
    }//GEN-LAST:event_jButtonDeleteAttWk1ActionPerformed

    private void jTextAttDocPathMainWk1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocPathMainWk1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocPathMainWk1ActionPerformed

    private void jTextAttDocDescMainWk2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocDescMainWk2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocDescMainWk2ActionPerformed

    private void jButtonSelectFileMainWk2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectFileMainWk2ActionPerformed
        if (modelWk1.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "File attachment is not possible if activities have not been added. Please add activities first. ");
        } else {
            savePDFToDB pdfDB = new savePDFToDB();
            pdfDB.pdfChooser();
            String attFileName = pdfDB.fileName;

            File file = new File(attFileName);
            long attFileLength = file.length();
            if (attFileLength < 512000) {
                jTextAttDocNameMainWk2.setText(file.getName());
                jTextAttDocPathMainWk2.setText(attFileName);

            } else if (attFileLength > 512000) {
                JOptionPane.showMessageDialog(this, "File size cannot be greater than 512Kb. ");
            }
        }
    }//GEN-LAST:event_jButtonSelectFileMainWk2ActionPerformed

    private void jTextAttDocNameMainWk2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocNameMainWk2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocNameMainWk2ActionPerformed

    private void jButtonAddAttWk2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddAttWk2ActionPerformed
        if ("".equals(jTextAttDocDescMainWk2.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter attached file description.");
            jTextAttDocDescMainWk2.requestFocusInWindow();
            jTextAttDocDescMainWk2.setFocusable(true);
        } else if ("".equals(jTextAttDocNameMainWk2.getText())) {
            JOptionPane.showMessageDialog(this, "Please note that no file has been selected.");
            jTextAttDocNameMainWk2.requestFocusInWindow();
            jTextAttDocNameMainWk2.setFocusable(true);
        } else {
            addFileAttWk2Main();
            refreshAttFields();

        }
    }//GEN-LAST:event_jButtonAddAttWk2ActionPerformed

    private void jButtonDeleteAttWk2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteAttWk2ActionPerformed
        deleteFileAttWk2Main();
    }//GEN-LAST:event_jButtonDeleteAttWk2ActionPerformed

    private void jTextAttDocPathMainWk2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocPathMainWk2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocPathMainWk2ActionPerformed

    private void jTextAttDocDescMainWk3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocDescMainWk3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocDescMainWk3ActionPerformed

    private void jButtonSelectFileMainWk3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectFileMainWk3ActionPerformed
        if (modelWk1.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "File attachment is not possible if activities have not been added. Please add activities first. ");
        } else {
            savePDFToDB pdfDB = new savePDFToDB();
            pdfDB.pdfChooser();
            String attFileName = pdfDB.fileName;

            File file = new File(attFileName);
            long attFileLength = file.length();
            if (attFileLength < 512000) {
                jTextAttDocNameMainWk3.setText(file.getName());
                jTextAttDocPathMainWk3.setText(attFileName);

            } else if (attFileLength > 512000) {
                JOptionPane.showMessageDialog(this, "File size cannot be greater than 512Kb. ");
            }
        }
    }//GEN-LAST:event_jButtonSelectFileMainWk3ActionPerformed

    private void jTextAttDocNameMainWk3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocNameMainWk3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocNameMainWk3ActionPerformed

    private void jButtonAddAttWk3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddAttWk3ActionPerformed
        if ("".equals(jTextAttDocDescMainWk3.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter attached file description.");
            jTextAttDocDescMainWk3.requestFocusInWindow();
            jTextAttDocDescMainWk3.setFocusable(true);
        } else if ("".equals(jTextAttDocNameMainWk3.getText())) {
            JOptionPane.showMessageDialog(this, "Please note that no file has been selected.");
            jTextAttDocNameMainWk3.requestFocusInWindow();
            jTextAttDocNameMainWk3.setFocusable(true);
        } else {
            addFileAttWk3Main();
            refreshAttFields();

        }
    }//GEN-LAST:event_jButtonAddAttWk3ActionPerformed

    private void jButtonDeleteAttWk3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteAttWk3ActionPerformed
        deleteFileAttWk3Main();
    }//GEN-LAST:event_jButtonDeleteAttWk3ActionPerformed

    private void jTextAttDocPathMainWk3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocPathMainWk3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocPathMainWk3ActionPerformed

    private void jTextAttDocDescMainWk4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocDescMainWk4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocDescMainWk4ActionPerformed

    private void jButtonSelectFileMainWk4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectFileMainWk4ActionPerformed
        if (modelWk1.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "File attachment is not possible if activities have not been added. Please add activities first. ");
        } else {
            savePDFToDB pdfDB = new savePDFToDB();
            pdfDB.pdfChooser();
            String attFileName = pdfDB.fileName;

            File file = new File(attFileName);
            long attFileLength = file.length();
            if (attFileLength < 512000) {
                jTextAttDocNameMainWk4.setText(file.getName());
                jTextAttDocPathMainWk4.setText(attFileName);

            } else if (attFileLength > 512000) {
                JOptionPane.showMessageDialog(this, "File size cannot be greater than 512Kb. ");
            }
        }
    }//GEN-LAST:event_jButtonSelectFileMainWk4ActionPerformed

    private void jTextAttDocNameMainWk4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocNameMainWk4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocNameMainWk4ActionPerformed

    private void jButtonAddAttWk4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddAttWk4ActionPerformed
        if ("".equals(jTextAttDocDescMainWk4.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter attached file description.");
            jTextAttDocDescMainWk4.requestFocusInWindow();
            jTextAttDocDescMainWk4.setFocusable(true);
        } else if ("".equals(jTextAttDocNameMainWk4.getText())) {
            JOptionPane.showMessageDialog(this, "Please note that no file has been selected.");
            jTextAttDocNameMainWk4.requestFocusInWindow();
            jTextAttDocNameMainWk4.setFocusable(true);
        } else {
            addFileAttWk4Main();
            refreshAttFields();

        }
    }//GEN-LAST:event_jButtonAddAttWk4ActionPerformed

    private void jButtonDeleteAttWk4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteAttWk4ActionPerformed
        deleteFileAttWk4Main();
    }//GEN-LAST:event_jButtonDeleteAttWk4ActionPerformed

    private void jTextAttDocPathMainWk4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocPathMainWk4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocPathMainWk4ActionPerformed

    private void jTextAttDocDescMainWk5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocDescMainWk5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocDescMainWk5ActionPerformed

    private void jButtonSelectFileMainWk5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectFileMainWk5ActionPerformed
        if (modelWk1.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "File attachment is not possible if activities have not been added. Please add activities first. ");
        } else {
            savePDFToDB pdfDB = new savePDFToDB();
            pdfDB.pdfChooser();
            String attFileName = pdfDB.fileName;

            File file = new File(attFileName);
            long attFileLength = file.length();
            if (attFileLength < 512000) {
                jTextAttDocNameMainWk5.setText(file.getName());
                jTextAttDocPathMainWk5.setText(attFileName);

            } else if (attFileLength > 512000) {
                JOptionPane.showMessageDialog(this, "File size cannot be greater than 512Kb. ");
            }
        }
    }//GEN-LAST:event_jButtonSelectFileMainWk5ActionPerformed

    private void jTextAttDocNameMainWk5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocNameMainWk5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocNameMainWk5ActionPerformed

    private void jButtonAddAttWk5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddAttWk5ActionPerformed
        if ("".equals(jTextAttDocDescMainWk5.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter attached file description.");
            jTextAttDocDescMainWk5.requestFocusInWindow();
            jTextAttDocDescMainWk5.setFocusable(true);
        } else if ("".equals(jTextAttDocNameMainWk5.getText())) {
            JOptionPane.showMessageDialog(this, "Please note that no file has been selected.");
            jTextAttDocNameMainWk5.requestFocusInWindow();
            jTextAttDocNameMainWk5.setFocusable(true);
        } else {
            addFileAttWk5Main();
            refreshAttFields();

        }
    }//GEN-LAST:event_jButtonAddAttWk5ActionPerformed

    private void jButtonDeleteAttWk5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteAttWk5ActionPerformed
        deleteFileAttWk5Main();
    }//GEN-LAST:event_jButtonDeleteAttWk5ActionPerformed

    private void jTextAttDocPathMainWk5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttDocPathMainWk5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAttDocPathMainWk5ActionPerformed

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

    private void jTextFieldWk1MiscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWk1MiscActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldWk1MiscActionPerformed

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
            java.util.logging.Logger.getLogger(JFrameMnthPlanPerDiemEdit.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthPlanPerDiemEdit.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthPlanPerDiemEdit.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthPlanPerDiemEdit.class
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrameMnthPlanPerDiemEdit().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private cambodia.raven.Time departTime;
    private javax.swing.JButton jButtonAddAttWk1;
    private javax.swing.JButton jButtonAddAttWk2;
    private javax.swing.JButton jButtonAddAttWk3;
    private javax.swing.JButton jButtonAddAttWk4;
    private javax.swing.JButton jButtonAddAttWk5;
    private javax.swing.JButton jButtonCancelFacility;
    private javax.swing.JButton jButtonDeleteAttWk1;
    private javax.swing.JButton jButtonDeleteAttWk2;
    private javax.swing.JButton jButtonDeleteAttWk3;
    private javax.swing.JButton jButtonDeleteAttWk4;
    private javax.swing.JButton jButtonDeleteAttWk5;
    private javax.swing.JButton jButtonDialogWk1Add;
    private javax.swing.JButton jButtonDialogWk1Close;
    private javax.swing.JButton jButtonDialogWk1Reset;
    private javax.swing.JButton jButtonOk1;
    private javax.swing.JButton jButtonOkFacility;
    private javax.swing.JButton jButtonSearch1;
    private javax.swing.JButton jButtonSelectFile;
    private javax.swing.JButton jButtonSelectFileMainWk1;
    private javax.swing.JButton jButtonSelectFileMainWk2;
    private javax.swing.JButton jButtonSelectFileMainWk3;
    private javax.swing.JButton jButtonSelectFileMainWk4;
    private javax.swing.JButton jButtonSelectFileMainWk5;
    private javax.swing.JButton jButtonWk1AddActivity;
    private javax.swing.JButton jButtonWk1DelActivity;
    private javax.swing.JButton jButtonWk2AddActivity;
    private javax.swing.JButton jButtonWk2DelActivity;
    private javax.swing.JButton jButtonWk3AddActivity;
    private javax.swing.JButton jButtonWk3DelActivity;
    private javax.swing.JButton jButtonWk4AddActivity;
    private javax.swing.JButton jButtonWk4DelActivity;
    private javax.swing.JButton jButtonWk5AddActivity;
    private javax.swing.JButton jButtonWk5DelActivity;
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
    private javax.swing.JLabel jLabelCommentsHeading;
    private javax.swing.JLabel jLabelConfirmationMainWk1;
    private javax.swing.JLabel jLabelConfirmationMainWk2;
    private javax.swing.JLabel jLabelConfirmationMainWk3;
    private javax.swing.JLabel jLabelConfirmationMainWk4;
    private javax.swing.JLabel jLabelConfirmationMainWk5;
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
    private javax.swing.JLabel jLabelGenLogNam9;
    private javax.swing.JLabel jLabelHeader1;
    private javax.swing.JLabel jLabelHeaderGen10;
    private javax.swing.JLabel jLabelHeaderGen11;
    private javax.swing.JLabel jLabelHeaderGen6;
    private javax.swing.JLabel jLabelHeaderGen7;
    private javax.swing.JLabel jLabelHeaderGen8;
    private javax.swing.JLabel jLabelHeaderGen9;
    private javax.swing.JLabel jLabelJustfy;
    private javax.swing.JLabel jLabelLineDate;
    private javax.swing.JLabel jLabelLineDate1;
    private javax.swing.JLabel jLabelLineDate2;
    private javax.swing.JLabel jLabelLineDate3;
    private javax.swing.JLabel jLabelLineDate4;
    private javax.swing.JLabel jLabelLineDate6;
    private javax.swing.JLabel jLabelLineLogNam;
    private javax.swing.JLabel jLabelLineLogNam1;
    private javax.swing.JLabel jLabelLineLogNam2;
    private javax.swing.JLabel jLabelLineLogNam3;
    private javax.swing.JLabel jLabelLineLogNam4;
    private javax.swing.JLabel jLabelLineLogNam6;
    private javax.swing.JLabel jLabelLineTime;
    private javax.swing.JLabel jLabelLineTime1;
    private javax.swing.JLabel jLabelLineTime2;
    private javax.swing.JLabel jLabelLineTime3;
    private javax.swing.JLabel jLabelLineTime4;
    private javax.swing.JLabel jLabelLineTime6;
    private javax.swing.JLabel jLabelLogo1;
    private javax.swing.JLabel jLabelLogo2;
    private javax.swing.JLabel jLabelLogo3;
    private javax.swing.JLabel jLabelLogo4;
    private javax.swing.JLabel jLabelLogo5;
    private javax.swing.JLabel jLabelLogo7;
    private javax.swing.JLabel jLabelMOHCCConfirmation;
    private javax.swing.JLabel jLabelOffice1;
    private javax.swing.JLabel jLabelPlanRefNo;
    private javax.swing.JLabel jLabelPostAppMod1;
    private javax.swing.JLabel jLabelPostAppMod2;
    private javax.swing.JLabel jLabelPostAppMod3;
    private javax.swing.JLabel jLabelPostAppMod4;
    private javax.swing.JLabel jLabelPostAppMod5;
    private javax.swing.JLabel jLabelPrjCodeGL;
    private javax.swing.JLabel jLabelPrjCodeProgramming;
    private javax.swing.JLabel jLabelProvince;
    private javax.swing.JLabel jLabelProvince1;
    private javax.swing.JLabel jLabelProvinceFacility;
    private javax.swing.JLabel jLabelRefNo;
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
    private javax.swing.JMenuItem jMenuItemClose1;
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
    private javax.swing.JPanel jPanelAttachMainWk1;
    private javax.swing.JPanel jPanelAttachMainWk2;
    private javax.swing.JPanel jPanelAttachMainWk3;
    private javax.swing.JPanel jPanelAttachMainWk4;
    private javax.swing.JPanel jPanelAttachMainWk5;
    private javax.swing.JPanel jPanelDepartureDetails;
    private javax.swing.JPanel jPanelDooAtt;
    private javax.swing.JPanel jPanelDooAttMainWk1;
    private javax.swing.JPanel jPanelDooAttMainWk2;
    private javax.swing.JPanel jPanelDooAttMainWk3;
    private javax.swing.JPanel jPanelDooAttMainWk4;
    private javax.swing.JPanel jPanelDooAttMainWk5;
    private javax.swing.JPanel jPanelSave;
    private javax.swing.JPanel jPanelSave1;
    private javax.swing.JPanel jPanelSave2;
    private javax.swing.JPanel jPanelSave3;
    private javax.swing.JPanel jPanelSave4;
    private javax.swing.JPanel jPanelSearchID;
    private javax.swing.JPanel jPanelWkComments;
    private javax.swing.JPanel jPanelWkFive;
    private javax.swing.JPanel jPanelWkFour;
    private javax.swing.JPanel jPanelWkOne;
    private javax.swing.JPanel jPanelWkThree;
    private javax.swing.JPanel jPanelWkTwo;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPaneWk1;
    private javax.swing.JScrollPane jScrollPaneWk10;
    private javax.swing.JScrollPane jScrollPaneWk2;
    private javax.swing.JScrollPane jScrollPaneWk3;
    private javax.swing.JScrollPane jScrollPaneWk4;
    private javax.swing.JScrollPane jScrollPaneWk5;
    private javax.swing.JScrollPane jScrollPaneWk6;
    private javax.swing.JScrollPane jScrollPaneWk7;
    private javax.swing.JScrollPane jScrollPaneWk8;
    private javax.swing.JScrollPane jScrollPaneWk9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator11;
    private javax.swing.JPopupMenu.Separator jSeparator12;
    private javax.swing.JPopupMenu.Separator jSeparator13;
    private javax.swing.JPopupMenu.Separator jSeparator14;
    private javax.swing.JPopupMenu.Separator jSeparator17;
    private javax.swing.JPopupMenu.Separator jSeparator18;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator22;
    private javax.swing.JPopupMenu.Separator jSeparator23;
    private javax.swing.JPopupMenu.Separator jSeparator24;
    private javax.swing.JPopupMenu.Separator jSeparator26;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator34;
    private javax.swing.JSeparator jSeparator35;
    private javax.swing.JSeparator jSeparator36;
    private javax.swing.JSeparator jSeparator37;
    private javax.swing.JSeparator jSeparator38;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator45;
    private javax.swing.JSeparator jSeparator46;
    private javax.swing.JPopupMenu.Separator jSeparator47;
    private javax.swing.JPopupMenu.Separator jSeparator48;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator55;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator62;
    private javax.swing.JPopupMenu.Separator jSeparator63;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    private javax.swing.JTabbedPane jTabbedPaneMain;
    private javax.swing.JTable jTableDocAtt;
    private javax.swing.JTable jTableDocAttWk1;
    private javax.swing.JTable jTableDocAttWk2;
    private javax.swing.JTable jTableDocAttWk3;
    private javax.swing.JTable jTableDocAttWk4;
    private javax.swing.JTable jTableDocAttWk5;
    private javax.swing.JTable jTableWk1Activities;
    private javax.swing.JTable jTableWk2Activities;
    private javax.swing.JTable jTableWk3Activities;
    private javax.swing.JTable jTableWk4Activities;
    private javax.swing.JTable jTableWk5Activities;
    private javax.swing.JTextArea jTextAreaComments;
    private javax.swing.JTextArea jTextAreaWk1DialogJustification;
    private javax.swing.JTextField jTextAttDocDesc;
    private javax.swing.JTextField jTextAttDocDescMainWk1;
    private javax.swing.JTextField jTextAttDocDescMainWk2;
    private javax.swing.JTextField jTextAttDocDescMainWk3;
    private javax.swing.JTextField jTextAttDocDescMainWk4;
    private javax.swing.JTextField jTextAttDocDescMainWk5;
    private javax.swing.JTextField jTextAttDocName;
    private javax.swing.JTextField jTextAttDocNameMainWk1;
    private javax.swing.JTextField jTextAttDocNameMainWk2;
    private javax.swing.JTextField jTextAttDocNameMainWk3;
    private javax.swing.JTextField jTextAttDocNameMainWk4;
    private javax.swing.JTextField jTextAttDocNameMainWk5;
    private javax.swing.JTextField jTextAttDocPath;
    private javax.swing.JTextField jTextAttDocPathMainWk1;
    private javax.swing.JTextField jTextAttDocPathMainWk2;
    private javax.swing.JTextField jTextAttDocPathMainWk3;
    private javax.swing.JTextField jTextAttDocPathMainWk4;
    private javax.swing.JTextField jTextAttDocPathMainWk5;
    private javax.swing.JTextField jTextFieldDialogWkSite;
    private javax.swing.JTextField jTextFieldSearchNam1;
    private javax.swing.JTextField jTextFieldWk1DialogActivityDesc;
    private javax.swing.JTextField jTextFieldWk1DialogStaffName1;
    private javax.swing.JTextField jTextFieldWk1DialogStaffName2;
    private javax.swing.JTextField jTextFieldWk1DialogStaffName3;
    private javax.swing.JTextField jTextFieldWk1DialogStaffName4;
    private javax.swing.JTextField jTextFieldWk1Misc;
    private javax.swing.JTextField jTextFieldWk1MiscAmt;
    private javax.swing.JTextField jTextRefNum;
    public javax.swing.JToggleButton jToggleButtonMOHCCConfirmation;
    public javax.swing.JToggleButton jToggleButtonMOHCCConfirmation2;
    private cambodia.raven.Time returnTime;
    // End of variables declaration//GEN-END:variables
}
