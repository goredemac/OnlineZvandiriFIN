/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClaimPlanPerDiem;

import ClaimPlanPerDiem.JFrameMnthPlanPerDiemEdit;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import ClaimAppendix1.*;
import ClaimAppendix2.*;
import ClaimReports.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.Timer;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import ClaimPlan.JFrameMnthFinanceList;
import ClaimPlan.JFrameMnthHODList;
import ClaimPlan.JFrameMnthReqGenList;
import ClaimPlan.JFrameMnthSupList;
import ClaimPlan.JFrameMnthViewList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.timeHost;
import utils.connCred;
import utils.emailSend;
import utils.StockVehicleMgt;
import utils.connSaveFile;

/**
 *
 * @author goredemac
 */
public class JFrameMnthPlanPerDiemSupApprove extends javax.swing.JFrame {

    timeHost tH = new timeHost();
    connCred c = new connCred();
    emailSend emSend = new emailSend();
    DefaultTableModel modelAttWk1, modelAttWk2, modelAttWk3, modelAttWk4, modelAttWk5,
            modelWk1, modelWk2, modelWk3, modelWk4, modelWk5;
    boolean ignoreInput = false;
    String filename = null;
    int charMax = 200;
    int month, finyear;
    Date curYear = new Date();
    SimpleDateFormat df = new SimpleDateFormat("yyyy");
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String wk1Site = "N";
    String wk2Site = "N";
    String wk3Site = "N";
    String hostName = "";
    String breakfastAll, lunchAll, lunchNPAll, lunchPAll, dinnerAll, docVer, docNextVer,
            actNextVer, actVer, createEmpNum,
            docNextVerExpire, usrGrp, supNam, supEmpNum, supUsrMail,
            incidentalAll, unProvedAll, SearchRef, createUsrNam, usrMail, action, status, minDate,
            todayDate, createUsrMail, logUsrNum, finAppMailList;
    PreparedStatement pst = null;
    PreparedStatement pst1 = null;
    int itmNum = 1;
    List<String> list = new ArrayList<>();
    List<String> listFetch = new ArrayList<>();
//    int logUsrNum = 0;

    /**
     * Creates new form JFrameMnthPlanPerDiemCreate
     */
    public JFrameMnthPlanPerDiemSupApprove() {
        initComponents();
        //  jTabbedPaneMain.remove(jTabbedPaneComments);
        jTabbedPaneMain.setEnabledAt(3, false);

    }

    public JFrameMnthPlanPerDiemSupApprove(String ref, String usrLogNam) {
        initComponents();
        SearchRef = ref;
        logUsrNum = usrLogNam;
        computerName();

        Date date = new Date();
        try {
            //   showDate();
            tH.intShowDate();
        } catch (IOException ex) {
            Logger.getLogger(JFrameMnthPlanPerDiemSupApprove.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(JFrameMnthPlanPerDiemSupApprove.class.getName()).log(Level.SEVERE, null, ex);
        }
        jLabelLineDate.setText(tH.internetDate);
        jLabelLineDate1.setText(tH.internetDate);
        jLabelLineDate2.setText(tH.internetDate);
        jLabelLineDate4.setText(tH.internetDate);
        jLabelLineDate5.setText(tH.internetDate);
        jLabelLineDate6.setText(tH.internetDate);
        modelAttWk1 = (DefaultTableModel) jTableDocAttWk1.getModel();
        modelAttWk2 = (DefaultTableModel) jTableDocAttWk2.getModel();
        modelAttWk3 = (DefaultTableModel) jTableDocAttWk3.getModel();
        modelAttWk4 = (DefaultTableModel) jTableDocAttWk4.getModel();
        modelAttWk5 = (DefaultTableModel) jTableDocAttWk5.getModel();
        modelWk1 = (DefaultTableModel) jTableWk1Activities.getModel();
        modelWk2 = (DefaultTableModel) jTableWk2Activities.getModel();
        modelWk3 = (DefaultTableModel) jTableWk3Activities.getModel();
        modelWk4 = (DefaultTableModel) jTableWk4Activities.getModel();
        modelWk5 = (DefaultTableModel) jTableWk5Activities.getModel();
        showTime();
        findUser();
        jLabelEmp.setText(usrLogNam);
        jLabelEmp.setVisible(false);
        jLabelRefNum.setText(SearchRef);
        
        getYear();
        fetchdataWk1();
        fetchdataWk2();
        fetchdataWk3();
        fetchdataWk4();
        fetchdataWk5();
        fetchdataGenWk1();
        fetchAttDoc(SearchRef);
        findCreator();
        findUserGrp();
        findFinApprove();

        jTabbedPaneMain.setEnabledAt(5, false);
//        if (jTextAreaPrevComments.getText().trim().length()==0){
//        jTabbedPaneMain.setEnabledAt(6, false);
//        }
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

//        if (!"National Office".equals(jLabelDistrict.getText())) {
//            jTabbedPaneMain.setTitleAt(0, "Month Plan");
//            jTabbedPaneMain.setEnabledAt(1, false);
//            jTabbedPaneMain.setTitleAt(1, "");
//            jTabbedPaneMain.setEnabledAt(2, false);
//            jTabbedPaneMain.setTitleAt(2, "");
//            jTabbedPaneMain.setEnabledAt(3, false);
//            jTabbedPaneMain.setTitleAt(3, "");
//            jTabbedPaneMain.setEnabledAt(4, false);
//            jTabbedPaneMain.setTitleAt(4, "");
//        }
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

                jLabelLineTime.setText(s.format(d));
                jLabelLineTime1.setText(s.format(d));
                jLabelLineTime2.setText(s.format(d));
                jLabelLineTime3.setText(s.format(d));
                jLabelLineTime4.setText(s.format(d));
                jLabelLineTime5.setText(s.format(d));
                jLabelLineTime6.setText(s.format(d));

            }
        }) {

        }.start();

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
        try {
            GregorianCalendar date = new GregorianCalendar();
            month = date.get(Calendar.MONTH);
            month = month + 1;

            if ((month >= 1) && (month <= 9)) {
                finyear = Integer.parseInt(df.format(curYear));
            } else {
                finyear = Integer.parseInt(df.format(curYear)) + 1;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findUser() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select EMP_NAM,SUP_EMP_NUM,SUP_NAM,EMP_SUP_MAIL from [ClaimsAppSysZvandiri].[dbo]."
                    + "[EmpDetTab] where emp_num ='" + logUsrNum + "'");

            while (r.next()) {

                jLabelLineLogNam.setText(r.getString(1));
                jLabelLineLogNam1.setText(r.getString(1));
                jLabelLineLogNam2.setText(r.getString(1));
                jLabelLineLogNam3.setText(r.getString(1));
                jLabelLineLogNam4.setText(r.getString(1));
                jLabelLineLogNam5.setText(r.getString(1));
                jLabelLineLogNam6.setText(r.getString(1));

            }

        } catch (Exception e) {
            System.out.println(e);
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

    void findMinDate() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select min(ACT_DATE) as minDate from (SELECT [ACT_DATE]FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] where PLAN_REF_NUM = " + SearchRef + " and ACT_REC_STA = 'A' "
                    + "and DOC_VER = 1"
                    + "union SELECT [ACT_DATE] FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] where PLAN_REF_NUM = " + SearchRef + " "
                    + "and ACT_REC_STA = 'A' and DOC_VER = 1 union SELECT  [ACT_DATE] FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] "
                    + "where PLAN_REF_NUM = " + SearchRef + " and ACT_REC_STA = 'A' and DOC_VER = 1 union SELECT [ACT_DATE] FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab] where PLAN_REF_NUM = " + SearchRef + " and ACT_REC_STA = 'A' and DOC_VER = 1 union "
                    + "SELECT [ACT_DATE] FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab] where PLAN_REF_NUM = " + SearchRef + " and "
                    + "ACT_REC_STA = 'A' and DOC_VER = 1) a");
            while (r.next()) {

                minDate = r.getString(1);

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findFinApprove() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            if (modelWk1.getRowCount() > 0) {
                ResultSet r = st.executeQuery("SELECT EMP_MAIL FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] "
                        + "where EMP_NUM in (SELECT EMP_NUM FROM [ClaimsAppSysZvandiri].[dbo].[PrjFinHODTab] "
                        + "where CONCAT(DONOR_CODE,PRJ_CODE_GL)=(SELECT concat(DONOR_CODE,PRJ_CODE) "
                        + "FROM [ClaimsAppSysZvandiri].[dbo].[BudDonPrjTab] where concat(DONOR_DESC,PRJ_DESC) = "
                        + "(SELECT distinct CONCAT(DONOR,PRJ_CODE_GL) FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] "
                        + "where PLAN_REF_NUM =" + SearchRef + "  and ACT_REC_STA = 'A' and ITM_NUM = 1   )) and DEPT ='FINANCE')");

                while (r.next()) {
                    list.add(r.getString(1));
                    System.out.println("list " + r.getString(1));
                }
            } else if (modelWk2.getRowCount() > 0) {
                ResultSet r = st.executeQuery("SELECT EMP_MAIL FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] "
                        + "where EMP_NUM in (SELECT EMP_NUM FROM [ClaimsAppSysZvandiri].[dbo].[PrjFinHODTab] "
                        + "where CONCAT(DONOR_CODE,PRJ_CODE_GL)=(SELECT concat(DONOR_CODE,PRJ_CODE) "
                        + "FROM [ClaimsAppSysZvandiri].[dbo].[BudDonPrjTab] where concat(DONOR_DESC,PRJ_DESC) = "
                        + "(SELECT distinct CONCAT(DONOR,PRJ_CODE_GL) FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] "
                        + "where PLAN_REF_NUM =" + SearchRef + "  and ACT_REC_STA = 'A' and ITM_NUM = 1   )) and DEPT ='FINANCE')");

                while (r.next()) {
                    list.add(r.getString(1));
                    System.out.println("list " + r.getString(1));
                }
            } else if (modelWk3.getRowCount() > 0) {
                ResultSet r = st.executeQuery("SELECT EMP_MAIL FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] "
                        + "where EMP_NUM in (SELECT EMP_NUM FROM [ClaimsAppSysZvandiri].[dbo].[PrjFinHODTab] "
                        + "where CONCAT(DONOR_CODE,PRJ_CODE_GL)=(SELECT concat(DONOR_CODE,PRJ_CODE) "
                        + "FROM [ClaimsAppSysZvandiri].[dbo].[BudDonPrjTab] where concat(DONOR_DESC,PRJ_DESC) = "
                        + "(SELECT distinct CONCAT(DONOR,PRJ_CODE_GL) FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] "
                        + "where PLAN_REF_NUM =" + SearchRef + "  and ACT_REC_STA = 'A' and ITM_NUM = 1   )) and DEPT ='FINANCE')");

                while (r.next()) {
                    list.add(r.getString(1));
                    System.out.println("list " + r.getString(1));
                }
            } else if (modelWk4.getRowCount() > 0) {
                ResultSet r = st.executeQuery("SELECT EMP_MAIL FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] "
                        + "where EMP_NUM in (SELECT EMP_NUM FROM [ClaimsAppSysZvandiri].[dbo].[PrjFinHODTab] "
                        + "where CONCAT(DONOR_CODE,PRJ_CODE_GL)=(SELECT concat(DONOR_CODE,PRJ_CODE) "
                        + "FROM [ClaimsAppSysZvandiri].[dbo].[BudDonPrjTab] where concat(DONOR_DESC,PRJ_DESC) = "
                        + "(SELECT distinct CONCAT(DONOR,PRJ_CODE_GL) FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab] "
                        + "where PLAN_REF_NUM =" + SearchRef + "  and ACT_REC_STA = 'A' and ITM_NUM = 1   )) and DEPT ='FINANCE')");

                while (r.next()) {
                    list.add(r.getString(1));
                    System.out.println("list " + r.getString(1));
                }
            } else if (modelWk5.getRowCount() > 0) {
                ResultSet r = st.executeQuery("SELECT EMP_MAIL FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] "
                        + "where EMP_NUM in (SELECT EMP_NUM FROM [ClaimsAppSysZvandiri].[dbo].[PrjFinHODTab] "
                        + "where CONCAT(DONOR_CODE,PRJ_CODE_GL)=(SELECT concat(DONOR_CODE,PRJ_CODE) "
                        + "FROM [ClaimsAppSysZvandiri].[dbo].[BudDonPrjTab] where concat(DONOR_DESC,PRJ_DESC) = "
                        + "(SELECT distinct CONCAT(DONOR,PRJ_CODE_GL) FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab] "
                        + "where PLAN_REF_NUM =" + SearchRef + "  and ACT_REC_STA = 'A' and ITM_NUM = 1   )) and DEPT ='FINANCE')");

                while (r.next()) {
                    list.add(r.getString(1));
                    System.out.println("list " + r.getString(1));
                }
            }
            finAppMailList = String.join(",", list);
            System.out.println("fin list " + finAppMailList);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findCreator() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT EMP_NAM ,EMP_MAIL,EMP_NUM FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] "
                    + "where EMP_NUM = (select ACTIONED_BY_EMP_NUM from [ClaimsAppSysZvandiri].[dbo]."
                    + "[PlanActTab] where PLAN_REF_NUM =" + SearchRef + " and ACT_VER ="
                    + "(select max(ACT_VER) from [ClaimsAppSysZvandiri].[dbo].[PlanActTab]  where PLAN_REF_NUM =" + SearchRef + ") "
                    + "and DOC_VER = 1)");

            while (r.next()) {

                createUsrNam = r.getString(1);
                createUsrMail = r.getString(2);
                createEmpNum = r.getString(3);
            }
            Statement st1 = conn.createStatement();
            ResultSet r1 = st1.executeQuery("select EMP_MAIL from [ClaimsAppSysZvandiri].[dbo]."
                    + "[EmpDetTab] where EMP_NAM ='" + createUsrNam + "'");

            while (r1.next()) {

                usrMail = r1.getString(1);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findProject(String prjCode) {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT concat(PROJ_ID,' ',PROJ_NAME) "
                    + "FROM [HRLeaveSysZvandiri].[dbo].[ProjectTab] "
                    + " where PROJ_ID='" + prjCode + "'order by 1");

            while (r.next()) {

                jLabelPrjNameDet.setText(r.getString(1));

            }

            conn.close();
        } catch (Exception e) {
            System.out.println();
        }
    }

    void findTask(String taskCode) {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT concat(PRJ_TASK_CODE,' ',TASK_DESC)  "
                    + "FROM [HRLeaveSysZvandiri].[dbo].[ProjectTaskTab]  "
                    + "where PRJ_TASK_CODE ='" + taskCode + "' order by 1");

            while (r.next()) {

                jLabelPrjTaskDet.setText(r.getString(1));

            }

            conn.close();
        } catch (Exception e) {
            System.out.println();
        }
    }

    void resetField() {

        jLabelActWk1Date.setText("");
        jLabelPrjNameDet.setText("");
        jLabelPrjTaskDet.setText("");
        jTextFieldDialogWk1Site.setText("");
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
        jCheckBoxDialogWk1BrkFast.setSelected(false);
        jCheckBoxDialogWk1Lunch.setSelected(false);
        jCheckBoxDialogWk1Dinner.setSelected(false);
        jCheckBoxDialogWk1AccProved.setSelected(false);
        jCheckBoxDialogWk1Inc.setSelected(false);
        jCheckBoxDialogWk1Misc.setSelected(false);
        jCheckBoxDialogWk1AccUnProved.setSelected(true);
        jCheckBoxDialogWk1AccProved.setSelected(false);
        jTextFieldWk1Misc.setVisible(false);
        jLabelWk1Misc.setVisible(false);
        jTextFieldWk1Misc.setText("");
        jLabelWk1MiscAmt.setVisible(false);
        jTextFieldWk1MiscAmt.setVisible(false);
        jTextFieldWk1MiscAmt.setText("");

    }

    void WkPlanActionApproved() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlWkplanperiod = "insert into [ClaimsAppSysZvandiri].[dbo].[PlanActTab] "
                    + "(SERIAL,PLAN_REF_NUM,USR_ACTION,DOC_STATUS,"
                    + "ACTIONED_BY_EMP_NUM,ACTIONED_BY_NAM,SEND_TO_EMP_NUM,SEND_TO_NAM,"
                    + "ACTIONED_ON_DATE,ACTIONED_ON_TIME,"
                    + "ACTIONED_ON_COMPUTER,COMMENTS,ACT_VER,DOC_VER,ACT_REC_STA)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            action = "Plan - Supervisor Approved";
            status = "ApprovedSup";
            pst1 = conn.prepareStatement(sqlWkplanperiod);
            pst1.setString(1, "P");
            pst1.setString(2, SearchRef);
            pst1.setString(3, action);
            pst1.setString(4, status);
            pst1.setString(5, jLabelEmp.getText());
            pst1.setString(6, jLabelLineLogNam.getText());
            pst1.setString(7, "FINANCE");
            pst1.setString(8, "FINANCE");
            pst1.setString(9, jLabelLineDate.getText());
            pst1.setString(10, jLabelLineTime.getText());
            pst1.setString(11, hostName);
            pst1.setString(12, jTextAreaPrevComments.getText());
            pst1.setString(13, "1");
            pst1.setString(14, docNextVer);
            pst1.setString(15, "A");

            pst1.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void WkPlanActionDisApproved() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlWkplanperiod = "insert into [ClaimsAppSysZvandiri].[dbo].[PlanActTab] "
                    + "(SERIAL,PLAN_REF_NUM,USR_ACTION,DOC_STATUS,"
                    + "ACTIONED_BY_EMP_NUM,ACTIONED_BY_NAM,SEND_TO_EMP_NUM,SEND_TO_NAM,"
                    + "ACTIONED_ON_DATE,ACTIONED_ON_TIME,"
                    + "ACTIONED_ON_COMPUTER,COMMENTS,ACT_VER,DOC_VER,ACT_REC_STA)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            action = "Plan - Supervisor Rejected";
            status = "RejectedSup";

            pst1 = conn.prepareStatement(sqlWkplanperiod);
            pst1.setString(1, "P");
            pst1.setString(2, SearchRef);
            pst1.setString(3, action);
            pst1.setString(4, status);
            pst1.setString(5, jLabelEmp.getText());
            pst1.setString(6, jLabelLineLogNam.getText());
            pst1.setString(7, createEmpNum);
            pst1.setString(8, createUsrNam);
            pst1.setString(9, jLabelLineDate.getText());
            pst1.setString(10, jLabelLineTime.getText());
            pst1.setString(11, hostName);
            pst1.setString(12, jTextAreaComments.getText());
            pst1.setString(13, "1");
            pst1.setString(14, docNextVer);
            pst1.setString(15, "C");

            pst1.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updatePrevRecordExpire() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlPlanAct = "update [ClaimsAppSysZvandiri].[dbo].[PlanActTab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' "
                    + " and DOC_VER = " + docVer + "";

            pst = conn.prepareStatement(sqlPlanAct);
            pst.executeUpdate();

            String sqlPlanPeriod = "update [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' "
                    + " and DOC_VER = " + docVer + "";

            pst = conn.prepareStatement(sqlPlanPeriod);
            pst.executeUpdate();

            String sqlPlanWk1 = "update [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' "
                    + " and DOC_VER = " + docVer + "";

            pst = conn.prepareStatement(sqlPlanWk1);
            pst.executeUpdate();

            String sqlPlanWk2 = "update [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' "
                    + " and DOC_VER = " + docVer + "";

            pst = conn.prepareStatement(sqlPlanWk2);
            pst.executeUpdate();

            String sqlPlanWk3 = "update [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' "
                    + " and DOC_VER = " + docVer + "";

            pst = conn.prepareStatement(sqlPlanWk3);
            pst.executeUpdate();

            String sqlPlanWk4 = "update [ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' "
                    + " and DOC_VER = " + docVer + "";

            pst = conn.prepareStatement(sqlPlanWk4);
            pst.executeUpdate();

            String sqlPlanWk5 = "update [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' "
                    + " and DOC_VER = " + docVer + "";

            pst = conn.prepareStatement(sqlPlanWk5);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    void updateWk1PlanExpire() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlwk1plan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] "
                    + "select SERIAL,PLAN_REF_NUM,ACT_DATE,ACT_SITE,SITE_CLASSIFICATION,CONSO_RANK,"
                    + "TRAV_DIST,ACT_DESC,ACT_JUSTIFCATION,PER_DIEM_REQ,BUD_COD,"
                    + "BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,MSC_AMT,ACC_UNPROV_AMT,"
                    + "EMP_NAM1,EMP_NAM2,EMP_NAM3,EMP_NAM4,'1',DOC_VER + 1,'E' from [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] "
                    + " where  ACT_VER = (select max(ACT_VER) from [ClaimsAppSysZvandiri].[dbo].[PlanActTab]"
                    + "where PLAN_REF_NUM =" + SearchRef + ")   and DOC_VER = " + docVer + " and  PLAN_REF_NUM = " + SearchRef;

            pst = conn.prepareStatement(sqlwk1plan);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateWk2PlanExpire() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlwk2plan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] "
                    + "select SERIAL,PLAN_REF_NUM,ACT_DATE,ACT_SITE,SITE_CLASSIFICATION,CONSO_RANK,"
                    + "TRAV_DIST,ACT_DESC,ACT_JUSTIFCATION,PER_DIEM_REQ,BUD_COD,"
                    + "BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,MSC_AMT,ACC_UNPROV_AMT,"
                    + "EMP_NAM1,EMP_NAM2,EMP_NAM3,EMP_NAM4,'1',DOC_VER + 1,'E' from [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] "
                    + " where  ACT_VER = (select max(ACT_VER) from [ClaimsAppSysZvandiri].[dbo].[PlanActTab]"
                    + "where PLAN_REF_NUM =" + SearchRef + ") and DOC_VER = " + docVer + " and PLAN_REF_NUM = " + SearchRef;

            pst = conn.prepareStatement(sqlwk2plan);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateWk3PlanExpire() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlwk3plan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] "
                    + "select SERIAL,PLAN_REF_NUM,ACT_DATE,ACT_SITE,SITE_CLASSIFICATION,CONSO_RANK,"
                    + "TRAV_DIST,ACT_DESC,ACT_JUSTIFCATION,PER_DIEM_REQ,BUD_COD,"
                    + "BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,MSC_AMT,ACC_UNPROV_AMT,"
                    + "EMP_NAM1,EMP_NAM2,EMP_NAM3,EMP_NAM4,'1',DOC_VER + 1,'E' from [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] "
                    + " where  ACT_VER = (select max(ACT_VER) from [ClaimsAppSysZvandiri].[dbo].[PlanActTab]"
                    + "where PLAN_REF_NUM =" + SearchRef + ") and DOC_VER = " + docVer + " and PLAN_REF_NUM = " + SearchRef;

            pst = conn.prepareStatement(sqlwk3plan);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateWk4PlanExpire() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlwk4plan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab] "
                    + "select SERIAL,PLAN_REF_NUM,ACT_DATE,ACT_SITE,SITE_CLASSIFICATION,CONSO_RANK,"
                    + "TRAV_DIST,ACT_DESC,ACT_JUSTIFCATION,PER_DIEM_REQ,BUD_COD,"
                    + "BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,MSC_AMT,ACC_UNPROV_AMT,"
                    + "EMP_NAM1,EMP_NAM2,EMP_NAM3,EMP_NAM4,'1',DOC_VER + 1,'E' from [ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab] "
                    + " where  ACT_VER = (select max(ACT_VER) from [ClaimsAppSysZvandiri].[dbo].[PlanActTab]"
                    + "where PLAN_REF_NUM =" + SearchRef + ") and DOC_VER = " + docVer + " and PLAN_REF_NUM = " + SearchRef;

            pst = conn.prepareStatement(sqlwk4plan);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateWk5PlanExpire() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlwk5plan = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab] "
                    + "select SERIAL,PLAN_REF_NUM,ACT_DATE,ACT_SITE,SITE_CLASSIFICATION,CONSO_RANK,"
                    + "TRAV_DIST,ACT_DESC,ACT_JUSTIFCATION,PER_DIEM_REQ,BUD_COD,"
                    + "BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,MSC_AMT,ACC_UNPROV_AMT,"
                    + "EMP_NAM1,EMP_NAM2,EMP_NAM3,EMP_NAM4,'1',DOC_VER + 1,'E' from [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab] "
                    + " where  ACT_VER = (select max(ACT_VER) from [ClaimsAppSysZvandiri].[dbo].[PlanActTab]"
                    + "where PLAN_REF_NUM =" + SearchRef + ") and DOC_VER = " + docVer + " and PLAN_REF_NUM = " + SearchRef;

            pst = conn.prepareStatement(sqlwk5plan);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateWkPlanPeriodExpire() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlWkplanperiod = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] "
                    + "Select SERIAL,PLAN_REF_NUM,PROVINCE,DISTRICT,Wk1_PLAN_START_DATE,Wk1_PLAN_END_DATE,"
                    + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,Wk3_PLAN_START_DATE,Wk3_PLAN_END_DATE,"
                    + "Wk4_PLAN_START_DATE,Wk4_PLAN_END_DATE,Wk5_PLAN_START_DATE,Wk5_PLAN_END_DATE,ACT_VER+1,DOC_VER+1,'E' "
                    + "from [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab]"
                    + " where  ACT_VER = (select max(ACT_VER) from [ClaimsAppSysZvandiri].[dbo].[PlanActTab]"
                    + "where PLAN_REF_NUM =" + SearchRef + ") and DOC_VER = " + docVer + " and PLAN_REF_NUM = " + SearchRef;

            pst = conn.prepareStatement(sqlWkplanperiod);
            pst.executeUpdate();

//            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void WkPlanActionApprovedExpire() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlWkplanperiod = "insert into [ClaimsAppSysZvandiri].[dbo].[PlanActTab] "
                    + "select SERIAL,PLAN_REF_NUM,'Plan - Expired','Expired',"
                    + "ACTIONED_BY_NAM,SEND_TO,ACTIONED_ON_DATE,ACTIONED_ON_TIME,"
                    + "ACTIONED_ON_COMPUTER,' ','1',DOC_VER+1,'E' "
                    + "from [ClaimsAppSysZvandiri].[dbo].[PlanActTab]"
                    + " where  ACT_VER = (select max(ACT_VER) from [ClaimsAppSysZvandiri].[dbo].[PlanActTab]"
                    + "where PLAN_REF_NUM =" + SearchRef + ") and DOC_VER = " + docVer + " and PLAN_REF_NUM = " + SearchRef;

            pst = conn.prepareStatement(sqlWkplanperiod);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void WkPlanActionAmendRequest() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlWkplanperiod = "insert into [ClaimsAppSysZvandiri].[dbo].[PlanActTab] "
                    + "(SERIAL,PLAN_REF_NUM,USR_ACTION,DOC_STATUS,"
                    + "ACTIONED_BY_EMP_NUM,ACTIONED_BY_NAM,SEND_TO_EMP_NUM,SEND_TO_NAM,"
                    + "ACTIONED_ON_DATE,ACTIONED_ON_TIME,"
                    + "ACTIONED_ON_COMPUTER,COMMENTS,ACT_VER,DOC_VER,ACT_REC_STA)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            action = "Plan - Supervisor Amend Request";
            status = "AmendRequestSup";

            pst1 = conn.prepareStatement(sqlWkplanperiod);
            pst1.setString(1, "P");
            pst1.setString(2, SearchRef);
            pst1.setString(3, action);
            pst1.setString(4, status);
            pst1.setString(5, jLabelEmp.getText());
            pst1.setString(6, jLabelLineLogNam.getText());
            pst1.setString(7, createEmpNum);
            pst1.setString(8, createUsrNam);
            pst1.setString(9, jLabelLineDate.getText());
            pst1.setString(10, jLabelLineTime.getText());
            pst1.setString(11, hostName);
            pst1.setString(12, jTextAreaComments.getText());
            pst1.setString(13, actNextVer);
            pst1.setString(14, docVer);
            pst1.setString(15, "A");

            pst1.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchAttDoc(String refNum) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();
            Statement st3 = conn.createStatement();
            Statement st4 = conn.createStatement();

            st.executeQuery("SELECT ACT_ITM,attDesc,fileName FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAttDocJustTabWk1] "
                    + "where ACT_REC_STA='A' and REF_NUM='" + refNum + "'");

            ResultSet r = st.getResultSet();
            while (r.next()) {

                modelAttWk1.insertRow(modelAttWk1.getRowCount(), new Object[]{r.getString(1), r.getString(2),
                    r.getString(3)});

            }

            st1.executeQuery("SELECT ACT_ITM,attDesc,fileName FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAttDocJustTabWk2] "
                    + "where ACT_REC_STA='A' and REF_NUM='" + refNum + "'");

            ResultSet r1 = st1.getResultSet();
            while (r1.next()) {

                modelAttWk2.insertRow(modelAttWk2.getRowCount(), new Object[]{r1.getString(1), r1.getString(2),
                    r1.getString(3)});

            }

            st2.executeQuery("SELECT ACT_ITM,attDesc,fileName FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAttDocJustTabWk3] "
                    + "where ACT_REC_STA='A' and REF_NUM='" + refNum + "'");

            ResultSet r2 = st2.getResultSet();
            while (r2.next()) {

                modelAttWk3.insertRow(modelAttWk3.getRowCount(), new Object[]{r2.getString(1), r2.getString(2),
                    r2.getString(3)});

            }

            st3.executeQuery("SELECT ACT_ITM,attDesc,fileName FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAttDocJustTabWk4] "
                    + "where ACT_REC_STA='A' and REF_NUM='" + refNum + "'");

            ResultSet r3 = st3.getResultSet();
            while (r3.next()) {

                modelAttWk4.insertRow(modelAttWk4.getRowCount(), new Object[]{r3.getString(1), r3.getString(2),
                    r3.getString(3)});

            }

            st4.executeQuery("SELECT ACT_ITM,attDesc,fileName FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAttDocJustTabWk5] "
                    + "where ACT_REC_STA='A' and REF_NUM='" + refNum + "'");

            ResultSet r4 = st4.getResultSet();
            while (r4.next()) {

                modelAttWk5.insertRow(modelAttWk5.getRowCount(), new Object[]{r4.getString(1), r4.getString(2),
                    r4.getString(3)});

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdataGenWk1() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();

            st.executeQuery("SELECT PROVINCE ,DISTRICT ,WK1_PLAN_START_DATE ,WK1_PLAN_END_DATE,"
                    + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,Wk3_PLAN_START_DATE,Wk3_PLAN_END_DATE,"
                    + "Wk4_PLAN_START_DATE,Wk4_PLAN_END_DATE,Wk5_PLAN_START_DATE,Wk5_PLAN_END_DATE "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab]"
                    + " where PLAN_REF_NUM =" + SearchRef + " and DOC_VER = 1 and ACT_REC_STA = 'A'");

            ResultSet r = st.getResultSet();

            while (r.next()) {

                jLabelProvince.setText(r.getString(1));
                jLabelDistrict.setText(r.getString(2));
                jLabelWk1DateFrom.setText(r.getString(3));
                jLabelWk1DateTo.setText(r.getString(4));
                jLabelWk2DateFrom.setText(r.getString(5));
                jLabelWk2DateTo.setText(r.getString(6));
                jLabelWk3DateFrom.setText(r.getString(7));
                jLabelWk3DateTo.setText(r.getString(8));
                jLabelWk4DateFrom.setText(r.getString(9));
                jLabelWk4DateTo.setText(r.getString(10));
                jLabelWk5DateFrom.setText(r.getString(11));
                jLabelWk5DateTo.setText(r.getString(12));
            }

            if (jTableWk1Activities.getRowCount() > 0) {

                st1.executeQuery("SELECT distinct DOC_VER,DOC_VER + 1, ACT_VER,ACT_VER + 1  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] "
                        + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A'");

                ResultSet r1 = st1.getResultSet();

                while (r1.next()) {
                    docVer = r1.getString(1);
                    docNextVer = r1.getString(2);
                    actVer = r1.getString(3);
                    actNextVer = r1.getString(4);
                }
            } else if (jTableWk2Activities.getRowCount() > 0) {

                st1.executeQuery("SELECT distinct DOC_VER,DOC_VER + 1, ACT_VER,ACT_VER + 1  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] "
                        + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A'");

                ResultSet r1 = st1.getResultSet();

                while (r1.next()) {
                    docVer = r1.getString(1);
                    docNextVer = r1.getString(2);
                    actVer = r1.getString(3);
                    actNextVer = r1.getString(4);
                }
            } else if (jTableWk3Activities.getRowCount() > 0) {

                st1.executeQuery("SELECT distinct DOC_VER,DOC_VER + 1, ACT_VER,ACT_VER + 1  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] "
                        + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A'");

                ResultSet r1 = st1.getResultSet();

                while (r1.next()) {
                    docVer = r1.getString(1);
                    docNextVer = r1.getString(2);
                    actVer = r1.getString(3);
                    actNextVer = r1.getString(4);
                }
            } else if (jTableWk4Activities.getRowCount() > 0) {

                st1.executeQuery("SELECT distinct DOC_VER,DOC_VER + 1, ACT_VER,ACT_VER + 1  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab] "
                        + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A'");

                ResultSet r1 = st1.getResultSet();

                while (r1.next()) {
                    docVer = r1.getString(1);
                    docNextVer = r1.getString(2);
                    actVer = r1.getString(3);
                    actNextVer = r1.getString(4);
                }
            } else if (jTableWk5Activities.getRowCount() > 0) {

                st1.executeQuery("SELECT distinct DOC_VER,DOC_VER + 1, ACT_VER,ACT_VER + 1  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab] "
                        + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A'");

                ResultSet r1 = st1.getResultSet();

                while (r1.next()) {
                    docVer = r1.getString(1);
                    docNextVer = r1.getString(2);
                    actVer = r1.getString(3);
                    actNextVer = r1.getString(4);
                }
            }

            if (Integer.parseInt(docVer) == 1) {
                jTabbedPaneMain.remove(jPanelWkPrevComments);
            }

            st2.executeQuery("SELECT COMMENTS FROM [ClaimsAppSysZvandiri].[dbo].[PlanActTab]"
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' "
                    + "and DOC_VER =" + docVer);

            ResultSet r2 = st2.getResultSet();

            while (r2.next()) {
                jTextAreaPrevComments.setText(r2.getString(1));
            }

            jTextAreaPrevComments.setLineWrap(true);
            jTextAreaPrevComments.setWrapStyleWord(true);
            jTextAreaPrevComments.setEditable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void fetchdataWk1() {
        try {

            DefaultTableModel modelWk1 = (DefaultTableModel) jTableWk1Activities.getModel();

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] "
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' "
                    + "and DOC_VER = 1 order by 4");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk1.insertRow(modelWk1.getRowCount(), new Object[]{r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22), r.getString(23), r.getString(24), r.getString(25), r.getString(26)});

            }

            if (modelWk1.getRowCount() == 0) {
                jTabbedPaneMain.setEnabledAt(0, false);
            }

        } catch (Exception e) {

        }
    }

    void fetchdataWk2() {
        try {

            DefaultTableModel modelWk2 = (DefaultTableModel) jTableWk2Activities.getModel();

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] "
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' "
                    + "and DOC_VER = 1 order by 4");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk2.insertRow(modelWk2.getRowCount(), new Object[]{r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22), r.getString(23), r.getString(24), r.getString(25), r.getString(26)});

            }

            if (modelWk2.getRowCount() == 0) {
                jTabbedPaneMain.setEnabledAt(1, false);
            }
        } catch (Exception e) {

        }
    }

    void fetchdataWk3() {
        try {

            DefaultTableModel modelWk3 = (DefaultTableModel) jTableWk3Activities.getModel();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] "
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' "
                    + "and DOC_VER = 1 order by 4");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk3.insertRow(modelWk3.getRowCount(), new Object[]{r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22), r.getString(23), r.getString(24), r.getString(25), r.getString(26)});

            }

            if (modelWk3.getRowCount() == 0) {
                jTabbedPaneMain.setEnabledAt(2, false);
            }

        } catch (Exception e) {

        }
    }

    void fetchdataWk4() {
        try {

            DefaultTableModel modelWk4 = (DefaultTableModel) jTableWk4Activities.getModel();

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab] "
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' "
                    + "and DOC_VER = 1 order by 4");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk4.insertRow(modelWk4.getRowCount(), new Object[]{r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22), r.getString(23), r.getString(24), r.getString(25), r.getString(26)});

            }

            if (modelWk4.getRowCount() == 0) {
                jTabbedPaneMain.setEnabledAt(3, false);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdataWk5() {
        try {

            DefaultTableModel modelWk5 = (DefaultTableModel) jTableWk5Activities.getModel();

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab] "
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA = 'A' "
                    + "and DOC_VER = 1 order by 4");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk5.insertRow(modelWk5.getRowCount(), new Object[]{r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22), r.getString(23), r.getString(24), r.getString(25), r.getString(26)});

            }

            if (modelWk5.getRowCount() == 0) {
                jTabbedPaneMain.setEnabledAt(4, false);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void regCheck() {
        try {

            jMenuItemSupSubmit.setEnabled(false);
            if ((("Request Amendment".equals(jComboSupApproval.getSelectedItem().toString()))
                    || ("Not Approved".equals(jComboSupApproval.getSelectedItem().toString())))
                    && "".equals(jTextAreaComments.getText())) {
                JOptionPane.showMessageDialog(this, "<html><b>Comments Tab cannot be blank.Please record comments.</html>");
                jTabbedPaneMain.setSelectedIndex(3);
                jTextAreaComments.requestFocusInWindow();
                jTextAreaComments.setFocusable(true);
                jMenuItemSupSubmit.setEnabled(true);
            } else if ("Approved".equals(jComboSupApproval.getSelectedItem().toString())) {
                jMenuItemSupSubmit.setEnabled(false);
                updatePrevRecord();
                updateWk1Plan();
                updateWk2Plan();
                updateWk3Plan();
                updateWk4Plan();
                updateWk5Plan();
                updateWkPlanPeriod();
                WkPlanActionApproved();
                JOptionPane.showMessageDialog(this, "<html>Plan reference No. <b>" + jLabelSerial.getText() + " " + jLabelRefNum.getText()
                        + "</b> successfully updated.</html>");

                jDialogWaitingEmail.setVisible(true);

                String mailMsg = "<html><body> Dear Finance Team <br><br>"
                        + "Supervisor <b>"
                        + jLabelLineLogNam.getText() + "</b> has approved plan reference No. <b>" + jLabelSerial.getText() + " "
                        + "" + jLabelRefNum.getText() + "</b>, and the plan requires your approval.<br><br>"
                        + "Please check your Finance System inbox and action.<br><br> Kind Regards <br><br>"
                        + " Finance Management System </body></html>";

                String MailMsgTitle = "Plan Reference No. " + jLabelSerial.getText() + " " + jLabelRefNum.getText() + "  Approval.";

                emSend.sendMail(MailMsgTitle, finAppMailList, mailMsg, createUsrMail);

                jDialogWaitingEmail.setVisible(false);

                JOptionPane.showMessageDialog(this, "<html> An email has been sent to <b>Finance</b> for processing.</html>");

                new JFrameMnthSupList(jLabelEmp.getText()).setVisible(true);
                setVisible(false);

            } else if ("Request Amendment".equals(jComboSupApproval.getSelectedItem().toString())) {
                // jDialogAmendReqComments.setVisible(true);
                jMenuItemSupSubmit.setEnabled(false);

                updatePrevRecord();
                updateWk1Plan();
                updateWk2Plan();
                updateWk3Plan();
                updateWk4Plan();
                updateWk5Plan();
                updateWkPlanPeriod();
                WkPlanActionAmendRequest();
                JOptionPane.showMessageDialog(this, "<html>Plan reference No. <b>" + jLabelSerial.getText() + " " + jLabelRefNum.getText()
                        + "</b> successfully updated.</html>");

                jDialogWaitingEmail.setVisible(true);

                String mailMsg = "<html><body> Dear " + createUsrNam + " <br><br>"
                        + "Supervisor <b>"
                        + jLabelLineLogNam.getText() + "</b> has requested that you ammend"
                        + " plan reference No. <b>" + jLabelSerial.getText() + " "
                        + "" + jLabelRefNum.getText() + "</b>.<br><br>"
                        + "<b>Supervisor Comments</b><br>" + jTextAreaComments.getText() + "<br><br>"
                        + "Please check your Finance System inbox and action.<br><br> Kind Regards <br><br>"
                        + " Finance Management System </body></html>";

                String MailMsgTitle = "Plan Reference No. " + jLabelSerial.getText() + " " + jLabelRefNum.getText() + "  Amendment Required.";

                emSend.sendMail(MailMsgTitle, createUsrMail, mailMsg, "");

                jDialogWaitingEmail.setVisible(false);

                JOptionPane.showMessageDialog(this, "<html>An email notification has been sent to <b>" + createUsrNam + "</b> with status.</html>");

                new JFrameMnthSupList(jLabelEmp.getText()).setVisible(true);
                setVisible(false);

            } else if ("Not Approved".equals(jComboSupApproval.getSelectedItem().toString())) {
                updatePrevRecord();
                updateWk1Plan();
                updateWk2Plan();
                updateWk3Plan();
                updateWkPlanPeriod();
                WkPlanActionDisApproved();
                JOptionPane.showMessageDialog(this, "<html>Plan reference No. <b>" + jLabelSerial.getText() + " " + jLabelRefNum.getText()
                        + "</b> successfully updated.</html>");

                jDialogWaitingEmail.setVisible(true);

                String mailMsg = "<html><body> Dear " + createUsrNam + " <br><br>"
                        + "Supervisor <b>"
                        + jLabelLineLogNam.getText() + "</b> has rejected your"
                        + " plan reference No. <b>" + jLabelSerial.getText() + " "
                        + "" + jLabelRefNum.getText() + "</b>.<br><br>"
                        + "<b>Supervisor Comments</b><br>" + jTextAreaComments.getText() + "<br><br>"
                        + " Finance Management System </body></html>";

                String MailMsgTitle = "Plan Reference No. " + jLabelSerial.getText() + " " + jLabelRefNum.getText() + "  Rejected.";

                emSend.sendMail(MailMsgTitle, createUsrMail, mailMsg, "");

                jDialogWaitingEmail.setVisible(false);

                JOptionPane.showMessageDialog(this, "<html>An email notification has been sent to <b>" + createUsrNam + "</b> with status.</html>");

                new JFrameMnthSupList(jLabelEmp.getText()).setVisible(true);
                setVisible(false);
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
                if ("Request Amendment".equals(jComboSupApproval.getSelectedItem().toString())) {
                    pst1.setString(27, actNextVer);
                    pst1.setString(28, docVer);
                    pst1.setString(29, "A");
                } else if (("Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
                    pst1.setString(27, "1");
                    pst1.setString(28, docNextVer);
                    pst1.setString(29, "A");
                } else if (("Not Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
                    pst1.setString(27, "1");
                    pst1.setString(28, docNextVer);
                    pst1.setString(29, "C");
                }
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
                if ("Request Amendment".equals(jComboSupApproval.getSelectedItem().toString())) {
                    pst1.setString(27, actNextVer);
                    pst1.setString(28, docVer);
                    pst1.setString(29, "A");
                } else if (("Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
                    pst1.setString(27, "1");
                    pst1.setString(28, docNextVer);
                    pst1.setString(29, "A");
                } else if (("Not Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
                    pst1.setString(27, "1");
                    pst1.setString(28, docNextVer);
                    pst1.setString(29, "C");
                }
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
                if ("Request Amendment".equals(jComboSupApproval.getSelectedItem().toString())) {
                    pst1.setString(27, actNextVer);
                    pst1.setString(28, docVer);
                    pst1.setString(29, "A");
                } else if (("Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
                    pst1.setString(27, "1");
                    pst1.setString(28, docNextVer);
                    pst1.setString(29, "A");
                } else if (("Not Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
                    pst1.setString(27, "1");
                    pst1.setString(28, docNextVer);
                    pst1.setString(29, "C");
                }
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
                if ("Request Amendment".equals(jComboSupApproval.getSelectedItem().toString())) {
                    pst1.setString(27, actNextVer);
                    pst1.setString(28, docVer);
                    pst1.setString(29, "A");
                } else if (("Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
                    pst1.setString(27, "1");
                    pst1.setString(28, docNextVer);
                    pst1.setString(29, "A");
                } else if (("Not Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
                    pst1.setString(27, "1");
                    pst1.setString(28, docNextVer);
                    pst1.setString(29, "C");
                }
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
                if ("Request Amendment".equals(jComboSupApproval.getSelectedItem().toString())) {
                    pst1.setString(27, actNextVer);
                    pst1.setString(28, docVer);
                    pst1.setString(29, "A");
                } else if (("Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
                    pst1.setString(27, "1");
                    pst1.setString(28, docNextVer);
                    pst1.setString(29, "A");
                } else if (("Not Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
                    pst1.setString(27, "1");
                    pst1.setString(28, docNextVer);
                    pst1.setString(29, "C");
                }
                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateWkPlanPeriod() {
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
                pst1.setString(2, SearchRef);
                pst1.setString(3, jLabelProvince.getText());
                pst1.setString(4, jLabelDistrict.getText());
                pst1.setString(5, jLabelWk1DateFrom.getText());
                pst1.setString(6, jLabelWk1DateTo.getText());
                if ("Request Amendment".equals(jComboSupApproval.getSelectedItem().toString())) {
                    pst1.setString(7, actNextVer);
                    pst1.setString(8, docVer);
                    pst1.setString(9, "A");
                } else if (("Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
                    pst1.setString(7, "1");
                    pst1.setString(8, docNextVer);
                    pst1.setString(9, "A");
                } else if (("Not Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
                    pst1.setString(7, "1");
                    pst1.setString(8, docNextVer);
                    pst1.setString(9, "C");
                }

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
                pst1.setString(2, SearchRef);
                pst1.setString(3, jLabelProvince.getText());
                pst1.setString(4, jLabelDistrict.getText());
                pst1.setString(5, jLabelWk1DateFrom.getText());
                pst1.setString(6, jLabelWk1DateTo.getText());
                pst1.setString(7, jLabelWk2DateFrom.getText());
                pst1.setString(8, jLabelWk2DateTo.getText());
                if ("Request Amendment".equals(jComboSupApproval.getSelectedItem().toString())) {
                    pst1.setString(9, actNextVer);
                    pst1.setString(10, docVer);
                    pst1.setString(11, "A");
                } else if (("Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
                    pst1.setString(9, "1");
                    pst1.setString(10, docNextVer);
                    pst1.setString(11, "A");
                } else if (("Not Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
                    pst1.setString(9, "1");
                    pst1.setString(10, docNextVer);
                    pst1.setString(11, "C");
                }

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
                pst1.setString(2, SearchRef);
                pst1.setString(3, jLabelProvince.getText());
                pst1.setString(4, jLabelDistrict.getText());
                pst1.setString(5, jLabelWk1DateFrom.getText());
                pst1.setString(6, jLabelWk1DateTo.getText());
                pst1.setString(7, jLabelWk2DateFrom.getText());
                pst1.setString(8, jLabelWk2DateTo.getText());
                pst1.setString(9, jLabelWk3DateFrom.getText());
                pst1.setString(10, jLabelWk3DateTo.getText());
                if ("Request Amendment".equals(jComboSupApproval.getSelectedItem().toString())) {
                    pst1.setString(11, actNextVer);
                    pst1.setString(12, docVer);
                    pst1.setString(13, "A");
                } else if (("Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
                    pst1.setString(11, "1");
                    pst1.setString(12, docNextVer);
                    pst1.setString(13, "A");
                } else if (("Not Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
                    pst1.setString(11, "1");
                    pst1.setString(12, docNextVer);
                    pst1.setString(13, "C");
                }

                pst1.executeUpdate();
            } else if ((jTableWk4Activities.getRowCount() > 0)
                    && jTableWk5Activities.getRowCount() == 0) {
                String sqlWkplanperiod = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] "
                        + "(SERIAL,PLAN_REF_NUM,PROVINCE,DISTRICT,Wk1_PLAN_START_DATE,Wk1_PLAN_END_DATE,"
                        + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,Wk3_PLAN_START_DATE,Wk3_PLAN_END_DATE,"
                        + "Wk4_PLAN_START_DATE,Wk4_PLAN_END_DATE,ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWkplanperiod);
                pst1.setString(1, "P");
                pst1.setString(2, SearchRef);
                pst1.setString(3, jLabelProvince.getText());
                pst1.setString(4, jLabelDistrict.getText());
                pst1.setString(5, jLabelWk1DateFrom.getText());
                pst1.setString(6, jLabelWk1DateTo.getText());
                pst1.setString(7, jLabelWk2DateFrom.getText());
                pst1.setString(8, jLabelWk2DateTo.getText());
                pst1.setString(9, jLabelWk3DateFrom.getText());
                pst1.setString(10, jLabelWk3DateTo.getText());
                pst1.setString(11, jLabelWk4DateFrom.getText());
                pst1.setString(12, jLabelWk4DateTo.getText());
                if ("Request Amendment".equals(jComboSupApproval.getSelectedItem().toString())) {
                    pst1.setString(13, actNextVer);
                    pst1.setString(14, docVer);
                    pst1.setString(15, "A");
                } else if (("Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
                    pst1.setString(13, "1");
                    pst1.setString(14, docNextVer);
                    pst1.setString(15, "A");
                } else if (("Not Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
                    pst1.setString(13, "1");
                    pst1.setString(14, docNextVer);
                    pst1.setString(15, "C");
                }

                pst1.executeUpdate();
            } else if ((jTableWk5Activities.getRowCount() > 0)) {

                String sqlWkplanperiod = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab] "
                        + "(SERIAL,PLAN_REF_NUM,PROVINCE,DISTRICT,Wk1_PLAN_START_DATE,Wk1_PLAN_END_DATE,"
                        + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,Wk3_PLAN_START_DATE,Wk3_PLAN_END_DATE,"
                        + "Wk4_PLAN_START_DATE,Wk4_PLAN_END_DATE,Wk5_PLAN_START_DATE,Wk5_PLAN_END_DATE,ACT_VER,DOC_VER,ACT_REC_STA)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlWkplanperiod);
                pst1.setString(1, "P");
                pst1.setString(2, SearchRef);
                pst1.setString(3, jLabelProvince.getText());
                pst1.setString(4, jLabelDistrict.getText());
                pst1.setString(5, jLabelWk1DateFrom.getText());
                pst1.setString(6, jLabelWk1DateTo.getText());
                pst1.setString(7, jLabelWk2DateFrom.getText());
                pst1.setString(8, jLabelWk2DateTo.getText());
                pst1.setString(9, jLabelWk3DateFrom.getText());
                pst1.setString(10, jLabelWk3DateTo.getText());
                pst1.setString(11, jLabelWk4DateFrom.getText());
                pst1.setString(12, jLabelWk4DateTo.getText());
                pst1.setString(13, jLabelWk5DateFrom.getText());
                pst1.setString(14, jLabelWk5DateTo.getText());
                if ("Request Amendment".equals(jComboSupApproval.getSelectedItem().toString())) {
                    pst1.setString(15, actNextVer);
                    pst1.setString(16, docVer);
                    pst1.setString(17, "A");
                } else if (("Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
                    pst1.setString(15, "1");
                    pst1.setString(16, docNextVer);
                    pst1.setString(17, "A");
                } else if (("Not Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
                    pst1.setString(15, "1");
                    pst1.setString(16, docNextVer);
                    pst1.setString(17, "C");
                }

                pst1.executeUpdate();

            }

        } catch (Exception e) {
            System.out.println(e);
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
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(usrMail));
            message.setSubject("Plan Amendment Request.Plan Reference No. "
                    + jLabelSerial.getText() + " " + jLabelRefNum.getText());

            message.setContent("<html><body> Dear " + createUsrNam + "<br><br> "
                    + "Your plan has been examined by " + jLabelLineLogNam.getText() + " "
                    + "with outcome <b> plan " + status + ".</b> <br><br>Please check "
                    + "your inbox and action.<br><br> Kind Regards <br><br>"
                    + " Perdiem Management System </body></html>", "text/html;charset=utf-8");

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
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

        jDialogWaitingEmail = new javax.swing.JDialog();
        jDialogAmendReqComments = new javax.swing.JDialog();
        jPanelDetUpd2 = new javax.swing.JPanel();
        jLabelRejectHeader1 = new javax.swing.JLabel();
        jLabelAmmendDesc1 = new javax.swing.JLabel();
        jButtonAmmendDialogOk1 = new javax.swing.JButton();
        jTextAmmendRequest = new javax.swing.JTextField();
        jDialogWk1 = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabelDialogWk1ActivityDate = new javax.swing.JLabel();
        jLabelDialogWk1Site = new javax.swing.JLabel();
        jTextFieldDialogWk1Site = new javax.swing.JTextField();
        jLabelWk1DialogJustification = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldWk1DialogActivityDesc = new javax.swing.JTextField();
        jLabelWk1DialogActivityDesc = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaWk1DialogJustification = new javax.swing.JTextArea();
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
        jCheckBoxDialogWk1BrkFast = new javax.swing.JCheckBox();
        jCheckBoxDialogWk1Lunch = new javax.swing.JCheckBox();
        jCheckBoxDialogWk1Dinner = new javax.swing.JCheckBox();
        jLabelRemain = new javax.swing.JLabel();
        jCheckBoxDialogWk1Misc = new javax.swing.JCheckBox();
        jCheckBoxDialogWk1Inc = new javax.swing.JCheckBox();
        jLabelWk1Misc = new javax.swing.JLabel();
        jTextFieldWk1Misc = new javax.swing.JTextField();
        jLabelWk1MiscAmt = new javax.swing.JLabel();
        jTextFieldWk1MiscAmt = new javax.swing.JTextField();
        jLabelProjectName = new javax.swing.JLabel();
        jLabelProjectTask = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jCheckBoxDialogWk1AccUnProved = new javax.swing.JCheckBox();
        jCheckBoxDialogWk1AccProved = new javax.swing.JCheckBox();
        jCheckBoxNoAcc = new javax.swing.JCheckBox();
        jLabelPrjTaskDet = new javax.swing.JLabel();
        jLabelPrjNameDet = new javax.swing.JLabel();
        jLabelActWk1Date = new javax.swing.JLabel();
        jTabbedPaneMain = new javax.swing.JTabbedPane();
        jPanelWkOne = new javax.swing.JPanel();
        jLabelWkDuration = new javax.swing.JLabel();
        jLabelWk1From = new javax.swing.JLabel();
        jLabelWk1To = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabelLogo1 = new javax.swing.JLabel();
        jLabelProvince1 = new javax.swing.JLabel();
        jLabelOffice1 = new javax.swing.JLabel();
        jLabelProvince = new javax.swing.JLabel();
        jLabelDistrict = new javax.swing.JLabel();
        jLabelWk1DateTo = new javax.swing.JLabel();
        jLabelWk1DateFrom = new javax.swing.JLabel();
        jLabelEmp = new javax.swing.JLabel();
        jLabelGenLogNam1 = new javax.swing.JLabel();
        jLabelLineDate = new javax.swing.JLabel();
        jLabelLineTime = new javax.swing.JLabel();
        jLabelLineLogNam = new javax.swing.JLabel();
        jComboSupApproval = new javax.swing.JComboBox<>();
        jLabelHeaderGen6 = new javax.swing.JLabel();
        jLabelRef = new javax.swing.JLabel();
        jLabelSerial = new javax.swing.JLabel();
        jLabelRefNum = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanelAttDocWk1 = new javax.swing.JPanel();
        jScrollPaneWk1 = new javax.swing.JScrollPane();
        jTableDocAttWk1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPaneWk6 = new javax.swing.JScrollPane();
        jTableWk1Activities = new javax.swing.JTable();
        jPanelWkTwo = new javax.swing.JPanel();
        jLabelWkDuration1 = new javax.swing.JLabel();
        jLabelWk1From1 = new javax.swing.JLabel();
        jLabelWk1To1 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jLabelLogo2 = new javax.swing.JLabel();
        jLabelWk2DateTo = new javax.swing.JLabel();
        jLabelWk2DateFrom = new javax.swing.JLabel();
        jLabelLineDate1 = new javax.swing.JLabel();
        jLabelLineTime1 = new javax.swing.JLabel();
        jLabelGenLogNam3 = new javax.swing.JLabel();
        jLabelLineLogNam1 = new javax.swing.JLabel();
        jLabelHeaderGen7 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanelAttDocWk2 = new javax.swing.JPanel();
        jScrollPaneWk2 = new javax.swing.JScrollPane();
        jTableDocAttWk2 = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jPanelWk2 = new javax.swing.JPanel();
        jScrollPaneWk7 = new javax.swing.JScrollPane();
        jTableWk2Activities = new javax.swing.JTable();
        jPanelWkThree = new javax.swing.JPanel();
        jLabelWkDuration2 = new javax.swing.JLabel();
        jLabelWk3From = new javax.swing.JLabel();
        jLabelWk3To = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jLabelLogo3 = new javax.swing.JLabel();
        jLabelWk3DateTo = new javax.swing.JLabel();
        jLabelWk3DateFrom = new javax.swing.JLabel();
        jLabelLineLogNam2 = new javax.swing.JLabel();
        jLabelGenLogNam5 = new javax.swing.JLabel();
        jLabelLineDate2 = new javax.swing.JLabel();
        jLabelLineTime2 = new javax.swing.JLabel();
        jLabelHeaderGen4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanelAttDocWk3 = new javax.swing.JPanel();
        jScrollPaneWk3 = new javax.swing.JScrollPane();
        jTableDocAttWk3 = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jPanelWk3 = new javax.swing.JPanel();
        jScrollPaneWk8 = new javax.swing.JScrollPane();
        jTableWk3Activities = new javax.swing.JTable();
        jPanelWkFour = new javax.swing.JPanel();
        jLabelWkDuration3 = new javax.swing.JLabel();
        jLabelWk4From = new javax.swing.JLabel();
        jLabelWk4To = new javax.swing.JLabel();
        jSeparator39 = new javax.swing.JSeparator();
        jSeparator40 = new javax.swing.JSeparator();
        jLabelLogo6 = new javax.swing.JLabel();
        jLabelWk4DateTo = new javax.swing.JLabel();
        jLabelWk4DateFrom = new javax.swing.JLabel();
        jLabelLineLogNam5 = new javax.swing.JLabel();
        jLabelGenLogNam8 = new javax.swing.JLabel();
        jLabelLineDate5 = new javax.swing.JLabel();
        jLabelLineTime5 = new javax.swing.JLabel();
        jLabelHeaderGen5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanelAttDocWk4 = new javax.swing.JPanel();
        jScrollPaneWk4 = new javax.swing.JScrollPane();
        jTableDocAttWk4 = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jPanelWk4 = new javax.swing.JPanel();
        jScrollPaneWk9 = new javax.swing.JScrollPane();
        jTableWk4Activities = new javax.swing.JTable();
        jPanelWkFive = new javax.swing.JPanel();
        jLabelWkDuration4 = new javax.swing.JLabel();
        jLabelWk5From = new javax.swing.JLabel();
        jLabelWk5To = new javax.swing.JLabel();
        jSeparator41 = new javax.swing.JSeparator();
        jSeparator42 = new javax.swing.JSeparator();
        jLabelLogo7 = new javax.swing.JLabel();
        jLabelWk5DateTo = new javax.swing.JLabel();
        jLabelWk5DateFrom = new javax.swing.JLabel();
        jLabelLineLogNam6 = new javax.swing.JLabel();
        jLabelGenLogNam9 = new javax.swing.JLabel();
        jLabelLineDate6 = new javax.swing.JLabel();
        jLabelLineTime6 = new javax.swing.JLabel();
        jLabelHeaderGen10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanelAttDocWk5 = new javax.swing.JPanel();
        jScrollPaneWk5 = new javax.swing.JScrollPane();
        jTableDocAttWk5 = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jPanelWk5 = new javax.swing.JPanel();
        jScrollPaneWk10 = new javax.swing.JScrollPane();
        jTableWk5Activities = new javax.swing.JTable();
        jPanelWkComments = new javax.swing.JPanel();
        jSeparator35 = new javax.swing.JSeparator();
        jLabelLogo4 = new javax.swing.JLabel();
        jLabelLineLogNam3 = new javax.swing.JLabel();
        jLabelGenLogNam6 = new javax.swing.JLabel();
        jLabelLineTime3 = new javax.swing.JLabel();
        jLabelHeaderGen8 = new javax.swing.JLabel();
        jLabelCommentsHeading = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextAreaComments = new javax.swing.JTextArea();
        jSeparator36 = new javax.swing.JSeparator();
        jPanelWkPrevComments = new javax.swing.JPanel();
        jSeparator37 = new javax.swing.JSeparator();
        jLabelLogo5 = new javax.swing.JLabel();
        jLabelLineLogNam4 = new javax.swing.JLabel();
        jLabelGenLogNam7 = new javax.swing.JLabel();
        jLabelLineDate4 = new javax.swing.JLabel();
        jLabelLineTime4 = new javax.swing.JLabel();
        jLabelHeaderGen9 = new javax.swing.JLabel();
        jLabelCommentsHeading1 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextAreaPrevComments = new javax.swing.JTextArea();
        jSeparator38 = new javax.swing.JSeparator();
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
        jMenuItemSupSubmit = new javax.swing.JMenuItem();
        jSeparator15 = new javax.swing.JPopupMenu.Separator();
        jMenuItemClose = new javax.swing.JMenuItem();
        jSeparator12 = new javax.swing.JPopupMenu.Separator();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuRequest = new javax.swing.JMenu();
        jMenuItemSupApp = new javax.swing.JMenuItem();
        jSeparator14 = new javax.swing.JPopupMenu.Separator();
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
        jSeparator11 = new javax.swing.JPopupMenu.Separator();
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

        jDialogAmendReqComments.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jDialogAmendReqComments.setTitle("Rejection Comments");
        jDialogAmendReqComments.setAlwaysOnTop(true);
        jDialogAmendReqComments.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jDialogAmendReqComments.setLocation(new java.awt.Point(300, 150));
        jDialogAmendReqComments.setMinimumSize(new java.awt.Dimension(890, 480));
        jDialogAmendReqComments.setResizable(false);

        jPanelDetUpd2.setAlignmentX(5.0F);
        jPanelDetUpd2.setAlignmentY(5.0F);
        jPanelDetUpd2.setMinimumSize(new java.awt.Dimension(890, 480));
        jPanelDetUpd2.setLayout(null);

        jLabelRejectHeader1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelRejectHeader1.setForeground(new java.awt.Color(0, 0, 255));
        jLabelRejectHeader1.setText("PLAN AMMENDMENT REQUEST ");
        jPanelDetUpd2.add(jLabelRejectHeader1);
        jLabelRejectHeader1.setBounds(280, 20, 350, 50);

        jLabelAmmendDesc1.setText("Your plan has been examined by the Provincial Manager and needs to be ammended as shown below:");
        jPanelDetUpd2.add(jLabelAmmendDesc1);
        jLabelAmmendDesc1.setBounds(60, 100, 780, 30);

        jButtonAmmendDialogOk1.setBackground(new java.awt.Color(0, 51, 51));
        jButtonAmmendDialogOk1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonAmmendDialogOk1.setForeground(new java.awt.Color(51, 153, 0));
        jButtonAmmendDialogOk1.setText("Click Here to Proceed");
        jButtonAmmendDialogOk1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAmmendDialogOk1ActionPerformed(evt);
            }
        });
        jPanelDetUpd2.add(jButtonAmmendDialogOk1);
        jButtonAmmendDialogOk1.setBounds(360, 200, 180, 30);

        jTextAmmendRequest.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextAmmendRequestKeyTyped(evt);
            }
        });
        jPanelDetUpd2.add(jTextAmmendRequest);
        jTextAmmendRequest.setBounds(60, 140, 780, 30);

        javax.swing.GroupLayout jDialogAmendReqCommentsLayout = new javax.swing.GroupLayout(jDialogAmendReqComments.getContentPane());
        jDialogAmendReqComments.getContentPane().setLayout(jDialogAmendReqCommentsLayout);
        jDialogAmendReqCommentsLayout.setHorizontalGroup(
            jDialogAmendReqCommentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelDetUpd2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogAmendReqCommentsLayout.setVerticalGroup(
            jDialogAmendReqCommentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelDetUpd2, javax.swing.GroupLayout.PREFERRED_SIZE, 307, Short.MAX_VALUE)
        );

        jDialogWk1.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialogWk1.setTitle("ACTIVITY PLANNER WEEK 1");
        jDialogWk1.setAlwaysOnTop(true);
        jDialogWk1.setBackground(new java.awt.Color(255, 255, 204));
        jDialogWk1.setLocation(new java.awt.Point(300, 100));
        jDialogWk1.setName("dialogWk1"); // NOI18N
        jDialogWk1.setResizable(false);
        jDialogWk1.setSize(new java.awt.Dimension(850, 650));

        jPanel1.setBackground(new java.awt.Color(237, 235, 92));
        jPanel1.setPreferredSize(new java.awt.Dimension(890, 650));
        jPanel1.setVerifyInputWhenFocusTarget(false);
        jPanel1.setLayout(null);

        jLabelDialogWk1ActivityDate.setText("Activity Date");
        jPanel1.add(jLabelDialogWk1ActivityDate);
        jLabelDialogWk1ActivityDate.setBounds(10, 80, 90, 30);

        jLabelDialogWk1Site.setText("Site");
        jPanel1.add(jLabelDialogWk1Site);
        jLabelDialogWk1Site.setBounds(10, 240, 40, 30);

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
        jTextFieldDialogWk1Site.setBounds(70, 240, 440, 30);

        jLabelWk1DialogJustification.setText("Justification for Choice of Activity ");
        jPanel1.add(jLabelWk1DialogJustification);
        jLabelWk1DialogJustification.setBounds(10, 360, 340, 30);

        jLabel10.setText("4.");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(540, 180, 20, 30);
        jPanel1.add(jTextFieldWk1DialogActivityDesc);
        jTextFieldWk1DialogActivityDesc.setBounds(10, 330, 490, 30);

        jLabelWk1DialogActivityDesc.setText("Activity Descrpition");
        jPanel1.add(jLabelWk1DialogActivityDesc);
        jLabelWk1DialogActivityDesc.setBounds(10, 290, 130, 30);

        jTextAreaWk1DialogJustification.setColumns(20);
        jTextAreaWk1DialogJustification.setRows(5);
        jTextAreaWk1DialogJustification.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextAreaWk1DialogJustificationKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(jTextAreaWk1DialogJustification);

        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(10, 400, 500, 96);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel1.add(jSeparator3);
        jSeparator3.setBounds(530, 10, 20, 490);

        jLabelDialogPerDiem.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelDialogPerDiem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelDialogPerDiem.setText("Activity Details");
        jPanel1.add(jLabelDialogPerDiem);
        jLabelDialogPerDiem.setBounds(10, 20, 460, 30);

        jLabelWk1DialogStaffName.setText("Staff Details");
        jPanel1.add(jLabelWk1DialogStaffName);
        jLabelWk1DialogStaffName.setBounds(540, 30, 130, 30);

        jLabelWk1DialogStaffName1.setText("1.");
        jPanel1.add(jLabelWk1DialogStaffName1);
        jLabelWk1DialogStaffName1.setBounds(540, 60, 20, 30);

        jLabelWk1DialogStaffName2.setText("2.");
        jPanel1.add(jLabelWk1DialogStaffName2);
        jLabelWk1DialogStaffName2.setBounds(540, 100, 20, 30);

        jLabel17.setText("3.");
        jPanel1.add(jLabel17);
        jLabel17.setBounds(540, 140, 20, 30);

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
        jPanel1.add(jTextFieldWk1DialogStaffName4);
        jTextFieldWk1DialogStaffName4.setBounds(560, 180, 220, 30);

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
        jPanel1.add(jTextFieldWk1DialogStaffName1);
        jTextFieldWk1DialogStaffName1.setBounds(560, 60, 220, 30);

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
        jPanel1.add(jTextFieldWk1DialogStaffName2);
        jTextFieldWk1DialogStaffName2.setBounds(560, 100, 220, 30);

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
        jPanel1.add(jTextFieldWk1DialogStaffName3);
        jTextFieldWk1DialogStaffName3.setBounds(560, 140, 220, 30);

        jCheckBoxDialogWk1BrkFast.setText(" Breakfast");
        jPanel1.add(jCheckBoxDialogWk1BrkFast);
        jCheckBoxDialogWk1BrkFast.setBounds(550, 230, 90, 21);

        jCheckBoxDialogWk1Lunch.setText("Lunch");
        jPanel1.add(jCheckBoxDialogWk1Lunch);
        jCheckBoxDialogWk1Lunch.setBounds(670, 230, 80, 21);

        jCheckBoxDialogWk1Dinner.setText(" Dinner");
        jPanel1.add(jCheckBoxDialogWk1Dinner);
        jCheckBoxDialogWk1Dinner.setBounds(550, 270, 90, 21);

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
        jCheckBoxDialogWk1Misc.setBounds(550, 350, 110, 21);

        jCheckBoxDialogWk1Inc.setText("Incidental");
        jPanel1.add(jCheckBoxDialogWk1Inc);
        jCheckBoxDialogWk1Inc.setBounds(550, 310, 110, 21);

        jLabelWk1Misc.setText("Miscellaneous Desc");
        jPanel1.add(jLabelWk1Misc);
        jLabelWk1Misc.setBounds(560, 370, 160, 30);

        jTextFieldWk1Misc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldWk1MiscActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFieldWk1Misc);
        jTextFieldWk1Misc.setBounds(560, 400, 110, 30);

        jLabelWk1MiscAmt.setText("$");
        jPanel1.add(jLabelWk1MiscAmt);
        jLabelWk1MiscAmt.setBounds(690, 400, 30, 30);
        jPanel1.add(jTextFieldWk1MiscAmt);
        jTextFieldWk1MiscAmt.setBounds(730, 400, 70, 30);

        jLabelProjectName.setText("Project Name");
        jPanel1.add(jLabelProjectName);
        jLabelProjectName.setBounds(10, 140, 80, 30);

        jLabelProjectTask.setText("Project Task");
        jPanel1.add(jLabelProjectTask);
        jLabelProjectTask.setBounds(10, 190, 80, 30);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(null);

        jCheckBoxDialogWk1AccUnProved.setText(" Unproved Acc");
        jPanel3.add(jCheckBoxDialogWk1AccUnProved);
        jCheckBoxDialogWk1AccUnProved.setBounds(0, 0, 130, 21);

        jCheckBoxDialogWk1AccProved.setText(" Proved Acc");
        jPanel3.add(jCheckBoxDialogWk1AccProved);
        jCheckBoxDialogWk1AccProved.setBounds(0, 40, 130, 21);

        jCheckBoxNoAcc.setText("No Acc Required");
        jPanel3.add(jCheckBoxNoAcc);
        jCheckBoxNoAcc.setBounds(0, 80, 130, 21);

        jPanel1.add(jPanel3);
        jPanel3.setBounds(680, 270, 130, 100);

        jLabelPrjTaskDet.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel1.add(jLabelPrjTaskDet);
        jLabelPrjTaskDet.setBounds(110, 190, 380, 30);

        jLabelPrjNameDet.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel1.add(jLabelPrjNameDet);
        jLabelPrjNameDet.setBounds(110, 140, 380, 30);

        jLabelActWk1Date.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel1.add(jLabelActWk1Date);
        jLabelActWk1Date.setBounds(110, 80, 110, 30);

        javax.swing.GroupLayout jDialogWk1Layout = new javax.swing.GroupLayout(jDialogWk1.getContentPane());
        jDialogWk1.getContentPane().setLayout(jDialogWk1Layout);
        jDialogWk1Layout.setHorizontalGroup(
            jDialogWk1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
        );
        jDialogWk1Layout.setVerticalGroup(
            jDialogWk1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Supervisor Approval");

        jPanelWkOne.setBackground(new java.awt.Color(255, 255, 204));
        jPanelWkOne.setLayout(null);

        jLabelWkDuration.setText("Week Duration");
        jPanelWkOne.add(jLabelWkDuration);
        jLabelWkDuration.setBounds(20, 180, 90, 30);

        jLabelWk1From.setText("From");
        jPanelWkOne.add(jLabelWk1From);
        jLabelWk1From.setBounds(140, 180, 41, 30);

        jLabelWk1To.setText("To");
        jPanelWkOne.add(jLabelWk1To);
        jLabelWk1To.setBounds(380, 180, 41, 30);
        jPanelWkOne.add(jSeparator1);
        jSeparator1.setBounds(20, 220, 1280, 10);
        jPanelWkOne.add(jSeparator2);
        jSeparator2.setBounds(20, 170, 1280, 10);

        jLabelLogo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelWkOne.add(jLabelLogo1);
        jLabelLogo1.setBounds(10, 5, 220, 115);

        jLabelProvince1.setText("Province");
        jLabelProvince1.setMinimumSize(new java.awt.Dimension(130, 130));
        jLabelProvince1.setName(""); // NOI18N
        jPanelWkOne.add(jLabelProvince1);
        jLabelProvince1.setBounds(20, 120, 70, 30);

        jLabelOffice1.setText("District");
        jLabelOffice1.setMinimumSize(new java.awt.Dimension(130, 130));
        jLabelOffice1.setName(""); // NOI18N
        jPanelWkOne.add(jLabelOffice1);
        jLabelOffice1.setBounds(520, 120, 70, 30);
        jPanelWkOne.add(jLabelProvince);
        jLabelProvince.setBounds(140, 120, 320, 30);
        jPanelWkOne.add(jLabelDistrict);
        jLabelDistrict.setBounds(630, 120, 230, 30);
        jPanelWkOne.add(jLabelWk1DateTo);
        jLabelWk1DateTo.setBounds(430, 180, 100, 30);
        jPanelWkOne.add(jLabelWk1DateFrom);
        jLabelWk1DateFrom.setBounds(190, 180, 100, 30);
        jPanelWkOne.add(jLabelEmp);
        jLabelEmp.setBounds(1110, 60, 50, 20);

        jLabelGenLogNam1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam1.setText("Logged In");
        jPanelWkOne.add(jLabelGenLogNam1);
        jLabelGenLogNam1.setBounds(1090, 30, 100, 30);

        jLabelLineDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkOne.add(jLabelLineDate);
        jLabelLineDate.setBounds(1090, 0, 110, 30);

        jLabelLineTime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkOne.add(jLabelLineTime);
        jLabelLineTime.setBounds(1250, 0, 80, 30);

        jLabelLineLogNam.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkOne.add(jLabelLineLogNam);
        jLabelLineLogNam.setBounds(1180, 30, 160, 30);

        jComboSupApproval.setBackground(new java.awt.Color(0, 102, 0));
        jComboSupApproval.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jComboSupApproval.setForeground(new java.awt.Color(255, 255, 255));
        jComboSupApproval.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Approved", "Request Amendment", "Not Approved" }));
        jComboSupApproval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboSupApprovalActionPerformed(evt);
            }
        });
        jPanelWkOne.add(jComboSupApproval);
        jComboSupApproval.setBounds(1060, 180, 200, 30);

        jLabelHeaderGen6.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen6.setText("MONTHLY  PLAN ");
        jPanelWkOne.add(jLabelHeaderGen6);
        jLabelHeaderGen6.setBounds(450, 40, 420, 40);

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

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("PLANNED ACTIVITIES");
        jPanelWkOne.add(jLabel1);
        jLabel1.setBounds(30, 225, 150, 15);

        jPanelAttDocWk1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanelAttDocWk1.setLayout(null);

        jTableDocAttWk1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "File Itm No", "Attachment Description", "Attachement File Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableDocAttWk1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDocAttWk1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTableDocAttWk1MouseEntered(evt);
            }
        });
        jScrollPaneWk1.setViewportView(jTableDocAttWk1);

        jPanelAttDocWk1.add(jScrollPaneWk1);
        jScrollPaneWk1.setBounds(10, 10, 900, 105);

        jLabel2.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        jLabel2.setText("*NB: Double Click to View the PDF Document");
        jPanelAttDocWk1.add(jLabel2);
        jLabel2.setBounds(10, 160, 450, 13);

        jPanelWkOne.add(jPanelAttDocWk1);
        jPanelAttDocWk1.setBounds(30, 550, 920, 120);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel4.setLayout(null);
        jPanelWkOne.add(jPanel4);
        jPanel4.setBounds(950, 550, 370, 120);

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
        jScrollPaneWk6.setBounds(30, 240, 1290, 310);

        jTabbedPaneMain.addTab("Week One", jPanelWkOne);

        jPanelWkTwo.setBackground(new java.awt.Color(95, 222, 184));
        jPanelWkTwo.setLayout(null);

        jLabelWkDuration1.setText("Week Duration");
        jPanelWkTwo.add(jLabelWkDuration1);
        jLabelWkDuration1.setBounds(30, 160, 90, 30);

        jLabelWk1From1.setText("From");
        jPanelWkTwo.add(jLabelWk1From1);
        jLabelWk1From1.setBounds(150, 160, 41, 30);

        jLabelWk1To1.setText("To");
        jPanelWkTwo.add(jLabelWk1To1);
        jLabelWk1To1.setBounds(390, 160, 41, 30);
        jPanelWkTwo.add(jSeparator4);
        jSeparator4.setBounds(30, 200, 1280, 10);
        jPanelWkTwo.add(jSeparator5);
        jSeparator5.setBounds(30, 150, 1280, 10);

        jLabelLogo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelWkTwo.add(jLabelLogo2);
        jLabelLogo2.setBounds(10, 10, 220, 115);
        jPanelWkTwo.add(jLabelWk2DateTo);
        jLabelWk2DateTo.setBounds(440, 160, 100, 30);
        jPanelWkTwo.add(jLabelWk2DateFrom);
        jLabelWk2DateFrom.setBounds(200, 160, 100, 30);

        jLabelLineDate1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkTwo.add(jLabelLineDate1);
        jLabelLineDate1.setBounds(1090, 0, 110, 30);

        jLabelLineTime1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelWkTwo.add(jLabelLineTime1);
        jLabelLineTime1.setBounds(1250, 0, 80, 30);

        jLabelGenLogNam3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam3.setText("Logged In");
        jPanelWkTwo.add(jLabelGenLogNam3);
        jLabelGenLogNam3.setBounds(1090, 30, 100, 30);

        jLabelLineLogNam1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam1.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkTwo.add(jLabelLineLogNam1);
        jLabelLineLogNam1.setBounds(1180, 30, 160, 30);

        jLabelHeaderGen7.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen7.setText("MONTHLY  PLAN ");
        jPanelWkTwo.add(jLabelHeaderGen7);
        jLabelHeaderGen7.setBounds(450, 40, 420, 40);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("PLANNED ACTIVITIES");
        jPanelWkTwo.add(jLabel3);
        jLabel3.setBounds(30, 210, 150, 15);

        jPanelAttDocWk2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanelAttDocWk2.setLayout(null);

        jTableDocAttWk2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "File Itm No", "Attachment Description", "Attachement File Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableDocAttWk2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDocAttWk2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTableDocAttWk2MouseEntered(evt);
            }
        });
        jScrollPaneWk2.setViewportView(jTableDocAttWk2);

        jPanelAttDocWk2.add(jScrollPaneWk2);
        jScrollPaneWk2.setBounds(10, 10, 900, 105);

        jLabel6.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        jLabel6.setText("*NB: Double Click to View the PDF Document");
        jPanelAttDocWk2.add(jLabel6);
        jLabel6.setBounds(10, 160, 450, 13);

        jPanelWkTwo.add(jPanelAttDocWk2);
        jPanelAttDocWk2.setBounds(30, 545, 920, 120);

        jPanelWk2.setBackground(new java.awt.Color(255, 255, 255));
        jPanelWk2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelWk2.setLayout(null);
        jPanelWkTwo.add(jPanelWk2);
        jPanelWk2.setBounds(950, 545, 370, 120);

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
        jScrollPaneWk7.setBounds(30, 240, 1290, 305);

        jTabbedPaneMain.addTab("Week Two", jPanelWkTwo);

        jPanelWkThree.setBackground(new java.awt.Color(29, 109, 222));
        jPanelWkThree.setLayout(null);

        jLabelWkDuration2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWkDuration2.setForeground(java.awt.Color.white);
        jLabelWkDuration2.setText("Week Duration");
        jPanelWkThree.add(jLabelWkDuration2);
        jLabelWkDuration2.setBounds(30, 160, 110, 30);

        jLabelWk3From.setText("From");
        jPanelWkThree.add(jLabelWk3From);
        jLabelWk3From.setBounds(150, 160, 41, 30);

        jLabelWk3To.setText("To");
        jPanelWkThree.add(jLabelWk3To);
        jLabelWk3To.setBounds(390, 160, 41, 30);
        jPanelWkThree.add(jSeparator7);
        jSeparator7.setBounds(30, 200, 1280, 10);
        jPanelWkThree.add(jSeparator8);
        jSeparator8.setBounds(30, 150, 1280, 10);

        jLabelLogo3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelWkThree.add(jLabelLogo3);
        jLabelLogo3.setBounds(10, 10, 220, 115);
        jPanelWkThree.add(jLabelWk3DateTo);
        jLabelWk3DateTo.setBounds(440, 160, 100, 30);
        jPanelWkThree.add(jLabelWk3DateFrom);
        jLabelWk3DateFrom.setBounds(200, 160, 100, 30);

        jLabelLineLogNam2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkThree.add(jLabelLineLogNam2);
        jLabelLineLogNam2.setBounds(1180, 30, 160, 30);

        jLabelGenLogNam5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam5.setForeground(new java.awt.Color(255, 255, 255));
        jLabelGenLogNam5.setText("Logged In");
        jPanelWkThree.add(jLabelGenLogNam5);
        jLabelGenLogNam5.setBounds(1090, 30, 100, 30);

        jLabelLineDate2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkThree.add(jLabelLineDate2);
        jLabelLineDate2.setBounds(1090, 0, 110, 30);

        jLabelLineTime2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkThree.add(jLabelLineTime2);
        jLabelLineTime2.setBounds(1250, 0, 80, 30);

        jLabelHeaderGen4.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen4.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeaderGen4.setText("MONTHLY  PLAN ");
        jPanelWkThree.add(jLabelHeaderGen4);
        jLabelHeaderGen4.setBounds(450, 40, 420, 40);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("PLANNED ACTIVITIES");
        jPanelWkThree.add(jLabel5);
        jLabel5.setBounds(30, 210, 150, 15);

        jPanelAttDocWk3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanelAttDocWk3.setLayout(null);

        jTableDocAttWk3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "File Itm No", "Attachment Description", "Attachement File Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableDocAttWk3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDocAttWk3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTableDocAttWk3MouseEntered(evt);
            }
        });
        jScrollPaneWk3.setViewportView(jTableDocAttWk3);

        jPanelAttDocWk3.add(jScrollPaneWk3);
        jScrollPaneWk3.setBounds(10, 10, 900, 105);

        jLabel8.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        jLabel8.setText("*NB: Double Click to View the PDF Document");
        jPanelAttDocWk3.add(jLabel8);
        jLabel8.setBounds(10, 160, 450, 13);

        jPanelWkThree.add(jPanelAttDocWk3);
        jPanelAttDocWk3.setBounds(30, 550, 920, 120);

        jPanelWk3.setBackground(new java.awt.Color(255, 255, 255));
        jPanelWk3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelWk3.setLayout(null);
        jPanelWkThree.add(jPanelWk3);
        jPanelWk3.setBounds(950, 550, 370, 120);

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
        jScrollPaneWk8.setBounds(30, 240, 1290, 305);

        jTabbedPaneMain.addTab("Week Three", jPanelWkThree);

        jPanelWkFour.setBackground(new java.awt.Color(255, 204, 204));
        jPanelWkFour.setLayout(null);

        jLabelWkDuration3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWkDuration3.setForeground(java.awt.Color.white);
        jLabelWkDuration3.setText("Week Duration");
        jPanelWkFour.add(jLabelWkDuration3);
        jLabelWkDuration3.setBounds(30, 160, 110, 30);

        jLabelWk4From.setText("From");
        jPanelWkFour.add(jLabelWk4From);
        jLabelWk4From.setBounds(150, 160, 41, 30);

        jLabelWk4To.setText("To");
        jPanelWkFour.add(jLabelWk4To);
        jLabelWk4To.setBounds(390, 160, 41, 30);
        jPanelWkFour.add(jSeparator39);
        jSeparator39.setBounds(30, 200, 1280, 10);
        jPanelWkFour.add(jSeparator40);
        jSeparator40.setBounds(30, 150, 1280, 10);

        jLabelLogo6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelWkFour.add(jLabelLogo6);
        jLabelLogo6.setBounds(10, 10, 220, 115);
        jPanelWkFour.add(jLabelWk4DateTo);
        jLabelWk4DateTo.setBounds(440, 160, 100, 30);
        jPanelWkFour.add(jLabelWk4DateFrom);
        jLabelWk4DateFrom.setBounds(200, 160, 100, 30);

        jLabelLineLogNam5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam5.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFour.add(jLabelLineLogNam5);
        jLabelLineLogNam5.setBounds(1180, 30, 160, 30);

        jLabelGenLogNam8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam8.setForeground(new java.awt.Color(255, 255, 255));
        jLabelGenLogNam8.setText("Logged In");
        jPanelWkFour.add(jLabelGenLogNam8);
        jLabelGenLogNam8.setBounds(1090, 30, 100, 30);

        jLabelLineDate5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate5.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFour.add(jLabelLineDate5);
        jLabelLineDate5.setBounds(1090, 0, 110, 30);

        jLabelLineTime5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime5.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFour.add(jLabelLineTime5);
        jLabelLineTime5.setBounds(1250, 0, 80, 30);

        jLabelHeaderGen5.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen5.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeaderGen5.setText("MONTHLY  PLAN ");
        jPanelWkFour.add(jLabelHeaderGen5);
        jLabelHeaderGen5.setBounds(450, 40, 420, 40);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("PLANNED ACTIVITIES");
        jPanelWkFour.add(jLabel7);
        jLabel7.setBounds(30, 210, 150, 15);

        jPanelAttDocWk4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanelAttDocWk4.setLayout(null);

        jTableDocAttWk4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "File Itm No", "Attachment Description", "Attachement File Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableDocAttWk4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDocAttWk4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTableDocAttWk4MouseEntered(evt);
            }
        });
        jScrollPaneWk4.setViewportView(jTableDocAttWk4);

        jPanelAttDocWk4.add(jScrollPaneWk4);
        jScrollPaneWk4.setBounds(10, 10, 900, 105);

        jLabel11.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        jLabel11.setText("*NB: Double Click to View the PDF Document");
        jPanelAttDocWk4.add(jLabel11);
        jLabel11.setBounds(10, 160, 450, 13);

        jPanelWkFour.add(jPanelAttDocWk4);
        jPanelAttDocWk4.setBounds(30, 550, 920, 125);

        jPanelWk4.setBackground(new java.awt.Color(255, 255, 255));
        jPanelWk4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelWk4.setLayout(null);
        jPanelWkFour.add(jPanelWk4);
        jPanelWk4.setBounds(950, 550, 370, 120);

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
        jScrollPaneWk9.setBounds(30, 240, 1290, 305);

        jTabbedPaneMain.addTab("Week Four", jPanelWkFour);

        jPanelWkFive.setBackground(new java.awt.Color(204, 204, 255));
        jPanelWkFive.setLayout(null);

        jLabelWkDuration4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelWkDuration4.setForeground(java.awt.Color.white);
        jLabelWkDuration4.setText("Week Duration");
        jPanelWkFive.add(jLabelWkDuration4);
        jLabelWkDuration4.setBounds(30, 160, 110, 30);

        jLabelWk5From.setText("From");
        jPanelWkFive.add(jLabelWk5From);
        jLabelWk5From.setBounds(150, 160, 41, 30);

        jLabelWk5To.setText("To");
        jPanelWkFive.add(jLabelWk5To);
        jLabelWk5To.setBounds(390, 160, 41, 30);
        jPanelWkFive.add(jSeparator41);
        jSeparator41.setBounds(30, 200, 1280, 10);
        jPanelWkFive.add(jSeparator42);
        jSeparator42.setBounds(30, 150, 1280, 10);

        jLabelLogo7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelWkFive.add(jLabelLogo7);
        jLabelLogo7.setBounds(10, 10, 220, 115);
        jPanelWkFive.add(jLabelWk5DateTo);
        jLabelWk5DateTo.setBounds(440, 160, 100, 30);
        jPanelWkFive.add(jLabelWk5DateFrom);
        jLabelWk5DateFrom.setBounds(200, 160, 100, 30);

        jLabelLineLogNam6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam6.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFive.add(jLabelLineLogNam6);
        jLabelLineLogNam6.setBounds(1180, 30, 160, 30);

        jLabelGenLogNam9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam9.setForeground(new java.awt.Color(255, 255, 255));
        jLabelGenLogNam9.setText("Logged In");
        jPanelWkFive.add(jLabelGenLogNam9);
        jLabelGenLogNam9.setBounds(1090, 30, 100, 30);

        jLabelLineDate6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate6.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFive.add(jLabelLineDate6);
        jLabelLineDate6.setBounds(1090, 0, 110, 30);

        jLabelLineTime6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime6.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFive.add(jLabelLineTime6);
        jLabelLineTime6.setBounds(1250, 0, 80, 30);

        jLabelHeaderGen10.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen10.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeaderGen10.setText("MONTHLY  PLAN ");
        jPanelWkFive.add(jLabelHeaderGen10);
        jLabelHeaderGen10.setBounds(450, 40, 420, 40);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("PLANNED ACTIVITIES");
        jPanelWkFive.add(jLabel9);
        jLabel9.setBounds(30, 210, 150, 15);

        jPanelAttDocWk5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanelAttDocWk5.setLayout(null);

        jTableDocAttWk5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "File Itm No", "Attachment Description", "Attachement File Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableDocAttWk5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDocAttWk5MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTableDocAttWk5MouseEntered(evt);
            }
        });
        jScrollPaneWk5.setViewportView(jTableDocAttWk5);

        jPanelAttDocWk5.add(jScrollPaneWk5);
        jScrollPaneWk5.setBounds(10, 10, 900, 105);

        jLabel12.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        jLabel12.setText("*NB: Double Click to View the PDF Document");
        jPanelAttDocWk5.add(jLabel12);
        jLabel12.setBounds(10, 160, 450, 13);

        jPanelWkFive.add(jPanelAttDocWk5);
        jPanelAttDocWk5.setBounds(30, 550, 920, 120);

        jPanelWk5.setBackground(new java.awt.Color(255, 255, 255));
        jPanelWk5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelWk5.setLayout(null);
        jPanelWkFive.add(jPanelWk5);
        jPanelWk5.setBounds(950, 550, 370, 120);

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
        jScrollPaneWk10.setBounds(30, 240, 1290, 305);

        jTabbedPaneMain.addTab("Week Five", jPanelWkFive);

        jPanelWkComments.setBackground(new java.awt.Color(209, 54, 127));
        jPanelWkComments.setLayout(null);
        jPanelWkComments.add(jSeparator35);
        jSeparator35.setBounds(30, 150, 1280, 10);

        jLabelLogo4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelWkComments.add(jLabelLogo4);
        jLabelLogo4.setBounds(10, 10, 220, 115);

        jLabelLineLogNam3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam3.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkComments.add(jLabelLineLogNam3);
        jLabelLineLogNam3.setBounds(1180, 30, 160, 30);

        jLabelGenLogNam6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam6.setForeground(new java.awt.Color(255, 255, 255));
        jLabelGenLogNam6.setText("Logged In");
        jPanelWkComments.add(jLabelGenLogNam6);
        jLabelGenLogNam6.setBounds(1090, 30, 100, 30);

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
        jLabelCommentsHeading.setText("Please specify areas that require ammendments in the space provided below");
        jPanelWkComments.add(jLabelCommentsHeading);
        jLabelCommentsHeading.setBounds(30, 160, 590, 40);

        jTextAreaComments.setColumns(20);
        jTextAreaComments.setRows(5);
        jScrollPane6.setViewportView(jTextAreaComments);

        jPanelWkComments.add(jScrollPane6);
        jScrollPane6.setBounds(30, 220, 780, 280);
        jPanelWkComments.add(jSeparator36);
        jSeparator36.setBounds(30, 200, 1280, 10);

        jTabbedPaneMain.addTab("Comments", jPanelWkComments);

        jPanelWkPrevComments.setBackground(new java.awt.Color(217, 192, 250));
        jPanelWkPrevComments.setLayout(null);
        jPanelWkPrevComments.add(jSeparator37);
        jSeparator37.setBounds(30, 150, 1280, 10);

        jLabelLogo5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelWkPrevComments.add(jLabelLogo5);
        jLabelLogo5.setBounds(10, 10, 220, 115);

        jLabelLineLogNam4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam4.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkPrevComments.add(jLabelLineLogNam4);
        jLabelLineLogNam4.setBounds(1180, 30, 160, 30);

        jLabelGenLogNam7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam7.setForeground(new java.awt.Color(255, 255, 255));
        jLabelGenLogNam7.setText("Logged In");
        jPanelWkPrevComments.add(jLabelGenLogNam7);
        jLabelGenLogNam7.setBounds(1090, 30, 100, 30);

        jLabelLineDate4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate4.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkPrevComments.add(jLabelLineDate4);
        jLabelLineDate4.setBounds(1090, 0, 110, 30);

        jLabelLineTime4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime4.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkPrevComments.add(jLabelLineTime4);
        jLabelLineTime4.setBounds(1250, 0, 80, 30);

        jLabelHeaderGen9.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen9.setText("MONTHLY  PLAN ");
        jPanelWkPrevComments.add(jLabelHeaderGen9);
        jLabelHeaderGen9.setBounds(450, 40, 420, 40);

        jLabelCommentsHeading1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelCommentsHeading1.setForeground(new java.awt.Color(255, 255, 255));
        jLabelCommentsHeading1.setText("Please find below previous comments and/or amendment requests.");
        jPanelWkPrevComments.add(jLabelCommentsHeading1);
        jLabelCommentsHeading1.setBounds(30, 160, 590, 40);

        jTextAreaPrevComments.setColumns(20);
        jTextAreaPrevComments.setRows(5);
        jScrollPane8.setViewportView(jTextAreaPrevComments);

        jPanelWkPrevComments.add(jScrollPane8);
        jScrollPane8.setBounds(30, 220, 780, 280);
        jPanelWkPrevComments.add(jSeparator38);
        jSeparator38.setBounds(30, 200, 1280, 10);

        jTabbedPaneMain.addTab("Previous Comments", jPanelWkPrevComments);

        jMenuFile.setText("File");
        jMenuFile.setPreferredSize(new java.awt.Dimension(60, 19));
        jMenuFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuFileActionPerformed(evt);
            }
        });

        jMenuNew.setText("New");
        jMenuNew.setPreferredSize(new java.awt.Dimension(155, 24));

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

        jMenuItemSupSubmit.setText("Submit");
        jMenuItemSupSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSupSubmitActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemSupSubmit);
        jMenuFile.add(jSeparator15);

        jMenuItemClose.setText("Close");
        jMenuItemClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemClose);
        jMenuFile.add(jSeparator12);

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
        jMenuRequest.add(jSeparator14);

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
        jMenuAcquittal.add(jSeparator11);

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
            .addComponent(jTabbedPaneMain)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneMain)
        );

        setSize(new java.awt.Dimension(1376, 760));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuPlanApprovalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuPlanApprovalActionPerformed

    }//GEN-LAST:event_jMenuPlanApprovalActionPerformed

    private void jButtonAmmendDialogOk1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAmmendDialogOk1ActionPerformed

        if ("".equals(jTextAmmendRequest.getText())) {
            JOptionPane.showMessageDialog(this, "Please indicate areas that require ammendements.");

            jTextAmmendRequest.requestFocusInWindow();
            jTextAmmendRequest.setFocusable(true);
        } else {
//            jDialogTSAmmendComments.setVisible(false);
            //ammendRequest();

            //       sendMailAmmnend();
//            jDialogWaitingEmail.setVisible(false);
//
//            JOptionPane.showMessageDialog(this, "<html>TimeSheet successfully actioned with status<b> "
//                + jComboBoxSupApprove.getSelectedItem().toString() + "</html>");
//            new JFrameTSSupList(Integer.parseInt(jLabelLogEmpNum.getText())).setVisible(true);
//            setVisible(false);
        }
    }//GEN-LAST:event_jButtonAmmendDialogOk1ActionPerformed

    private void jTextAmmendRequestKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAmmendRequestKeyTyped
        //   jButtonAmmendDialogOk.setVisible(true);
    }//GEN-LAST:event_jTextAmmendRequestKeyTyped

    private void jComboSupApprovalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboSupApprovalActionPerformed
        if (("Request Amendment".equals(jComboSupApproval.getSelectedItem().toString()))
                || ("Not Approved".equals(jComboSupApproval.getSelectedItem().toString()))) {
            jTabbedPaneMain.setEnabledAt(5, true);
            JOptionPane.showMessageDialog(this, "<html><b>Please record comments and/or amendment request in the comments tab.</html>");
            jTabbedPaneMain.setSelectedIndex(5);
            jTextAreaComments.requestFocusInWindow();
            jTextAreaComments.setFocusable(true);
            jTextAreaComments.setLineWrap(true);
            jTextAreaComments.setWrapStyleWord(true);
        } else {
            jTabbedPaneMain.setEnabledAt(5, false);
        }
    }//GEN-LAST:event_jComboSupApprovalActionPerformed

    private void jTextFieldDialogWk1SiteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk1SiteMouseClicked

    }//GEN-LAST:event_jTextFieldDialogWk1SiteMouseClicked

    private void jTextFieldDialogWk1SiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk1SiteActionPerformed

    }//GEN-LAST:event_jTextFieldDialogWk1SiteActionPerformed

    private void jTextFieldDialogWk1SiteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk1SiteKeyTyped

    }//GEN-LAST:event_jTextFieldDialogWk1SiteKeyTyped

    private void jTextAreaWk1DialogJustificationKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAreaWk1DialogJustificationKeyTyped

    }//GEN-LAST:event_jTextAreaWk1DialogJustificationKeyTyped

    private void jTextFieldWk1DialogStaffName4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName4MouseClicked

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName4MouseClicked

    private void jTextFieldWk1DialogStaffName4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName4ActionPerformed

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName4ActionPerformed

    private void jTextFieldWk1DialogStaffName4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName4KeyTyped

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName4KeyTyped

    private void jTextFieldWk1DialogStaffName1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName1MouseClicked

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName1MouseClicked

    private void jTextFieldWk1DialogStaffName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName1ActionPerformed

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName1ActionPerformed

    private void jTextFieldWk1DialogStaffName1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName1KeyTyped

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName1KeyTyped

    private void jTextFieldWk1DialogStaffName2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName2MouseClicked

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName2MouseClicked

    private void jTextFieldWk1DialogStaffName2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName2KeyTyped

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName2KeyTyped

    private void jTextFieldWk1DialogStaffName3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName3MouseClicked

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName3MouseClicked

    private void jTextFieldWk1DialogStaffName3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogStaffName3KeyTyped

    }//GEN-LAST:event_jTextFieldWk1DialogStaffName3KeyTyped

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

    private void jMenuItemSupSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSupSubmitActionPerformed
        regCheck();
    }//GEN-LAST:event_jMenuItemSupSubmitActionPerformed

    private void jTableDocAttWk1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDocAttWk1MouseClicked
        if (evt.getClickCount() == 2) {
            connSaveFile pdfRetrive = new connSaveFile();
            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                        + "DataBaseName=ClaimsAppSys;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
                int row = jTableDocAttWk1.getSelectedRow();

                int col = 0;
                int col1 = 2;

                Object id = jTableDocAttWk1.getValueAt(row, col);
                Object id1 = jTableDocAttWk1.getValueAt(row, col1);

                String refItmNum = jLabelRefNum.getText() + id.toString();
                String pdfName = id1.toString();

                pdfRetrive.getPDFData5(conn, refItmNum, pdfName, "ClaimAttDocJustTabWk1");

            } catch (Exception e) {
                System.out.println(e);
            }

        }
    }//GEN-LAST:event_jTableDocAttWk1MouseClicked

    private void jTableDocAttWk1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDocAttWk1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableDocAttWk1MouseEntered

    private void jTableDocAttWk2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDocAttWk2MouseClicked
        if (evt.getClickCount() == 2) {
            connSaveFile pdfRetrive = new connSaveFile();
            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                        + "DataBaseName=ClaimsAppSys;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
                int row = jTableDocAttWk2.getSelectedRow();

                int col = 0;
                int col1 = 2;

                Object id = jTableDocAttWk2.getValueAt(row, col);
                Object id1 = jTableDocAttWk2.getValueAt(row, col1);

                String refItmNum = jLabelRefNum.getText() + id.toString();
                String pdfName = id1.toString();

                pdfRetrive.getPDFData5(conn, refItmNum, pdfName, "ClaimAttDocJustTabWk2");

            } catch (Exception e) {
                System.out.println(e);
            }

        }
    }//GEN-LAST:event_jTableDocAttWk2MouseClicked

    private void jTableDocAttWk2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDocAttWk2MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableDocAttWk2MouseEntered

    private void jTableDocAttWk3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDocAttWk3MouseClicked
        if (evt.getClickCount() == 2) {
            connSaveFile pdfRetrive = new connSaveFile();
            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                        + "DataBaseName=ClaimsAppSys;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
                int row = jTableDocAttWk3.getSelectedRow();

                int col = 0;
                int col1 = 2;

                Object id = jTableDocAttWk3.getValueAt(row, col);
                Object id1 = jTableDocAttWk3.getValueAt(row, col1);

                String refItmNum = jLabelRefNum.getText() + id.toString();
                String pdfName = id1.toString();

                pdfRetrive.getPDFData5(conn, refItmNum, pdfName, "ClaimAttDocJustTabWk3");

            } catch (Exception e) {
                System.out.println(e);
            }

        }
    }//GEN-LAST:event_jTableDocAttWk3MouseClicked

    private void jTableDocAttWk3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDocAttWk3MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableDocAttWk3MouseEntered

    private void jTableDocAttWk4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDocAttWk4MouseClicked
        if (evt.getClickCount() == 2) {
            connSaveFile pdfRetrive = new connSaveFile();
            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                        + "DataBaseName=ClaimsAppSys;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
                int row = jTableDocAttWk4.getSelectedRow();

                int col = 0;
                int col1 = 2;

                Object id = jTableDocAttWk4.getValueAt(row, col);
                Object id1 = jTableDocAttWk4.getValueAt(row, col1);

                String refItmNum = jLabelRefNum.getText() + id.toString();
                String pdfName = id1.toString();
                System.out.println("wk4 " + refItmNum + " fff " + pdfName);
                pdfRetrive.getPDFData5(conn, refItmNum, pdfName, "ClaimAttDocJustTabWk4");

            } catch (Exception e) {
                System.out.println(e);
            }

        }
    }//GEN-LAST:event_jTableDocAttWk4MouseClicked

    private void jTableDocAttWk4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDocAttWk4MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableDocAttWk4MouseEntered

    private void jTableDocAttWk5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDocAttWk5MouseClicked
        if (evt.getClickCount() == 2) {
            connSaveFile pdfRetrive = new connSaveFile();
            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                        + "DataBaseName=ClaimsAppSys;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
                int row = jTableDocAttWk5.getSelectedRow();

                int col = 0;
                int col1 = 2;

                Object id = jTableDocAttWk5.getValueAt(row, col);
                Object id1 = jTableDocAttWk5.getValueAt(row, col1);

                String refItmNum = jLabelRefNum.getText() + id.toString();
                String pdfName = id1.toString();

                pdfRetrive.getPDFData5(conn, refItmNum, pdfName, "ClaimAttDocJustTabWk5");

            } catch (Exception e) {
                System.out.println(e);
            }

        }
    }//GEN-LAST:event_jTableDocAttWk5MouseClicked

    private void jTableDocAttWk5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDocAttWk5MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableDocAttWk5MouseEntered

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
            java.util.logging.Logger.getLogger(JFrameMnthPlanPerDiemSupApprove.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthPlanPerDiemSupApprove.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthPlanPerDiemSupApprove.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthPlanPerDiemSupApprove.class
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrameMnthPlanPerDiemSupApprove().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAmmendDialogOk1;
    private javax.swing.JCheckBox jCheckBoxDialogWk1AccProved;
    private javax.swing.JCheckBox jCheckBoxDialogWk1AccUnProved;
    private javax.swing.JCheckBox jCheckBoxDialogWk1BrkFast;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Dinner;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Inc;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Lunch;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Misc;
    private javax.swing.JCheckBox jCheckBoxNoAcc;
    private javax.swing.JComboBox<String> jComboSupApproval;
    private javax.swing.JDialog jDialogAmendReqComments;
    private javax.swing.JDialog jDialogWaitingEmail;
    private javax.swing.JDialog jDialogWk1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelActWk1Date;
    private javax.swing.JLabel jLabelAmmendDesc1;
    private javax.swing.JLabel jLabelCommentsHeading;
    private javax.swing.JLabel jLabelCommentsHeading1;
    private javax.swing.JLabel jLabelDialogPerDiem;
    private javax.swing.JLabel jLabelDialogWk1ActivityDate;
    private javax.swing.JLabel jLabelDialogWk1Site;
    private javax.swing.JLabel jLabelDistrict;
    private javax.swing.JLabel jLabelEmp;
    private javax.swing.JLabel jLabelGenLogNam1;
    private javax.swing.JLabel jLabelGenLogNam3;
    private javax.swing.JLabel jLabelGenLogNam5;
    private javax.swing.JLabel jLabelGenLogNam6;
    private javax.swing.JLabel jLabelGenLogNam7;
    private javax.swing.JLabel jLabelGenLogNam8;
    private javax.swing.JLabel jLabelGenLogNam9;
    private javax.swing.JLabel jLabelHeaderGen10;
    private javax.swing.JLabel jLabelHeaderGen4;
    private javax.swing.JLabel jLabelHeaderGen5;
    private javax.swing.JLabel jLabelHeaderGen6;
    private javax.swing.JLabel jLabelHeaderGen7;
    private javax.swing.JLabel jLabelHeaderGen8;
    private javax.swing.JLabel jLabelHeaderGen9;
    private javax.swing.JLabel jLabelLineDate;
    private javax.swing.JLabel jLabelLineDate1;
    private javax.swing.JLabel jLabelLineDate2;
    private javax.swing.JLabel jLabelLineDate4;
    private javax.swing.JLabel jLabelLineDate5;
    private javax.swing.JLabel jLabelLineDate6;
    private javax.swing.JLabel jLabelLineLogNam;
    private javax.swing.JLabel jLabelLineLogNam1;
    private javax.swing.JLabel jLabelLineLogNam2;
    private javax.swing.JLabel jLabelLineLogNam3;
    private javax.swing.JLabel jLabelLineLogNam4;
    private javax.swing.JLabel jLabelLineLogNam5;
    private javax.swing.JLabel jLabelLineLogNam6;
    private javax.swing.JLabel jLabelLineTime;
    private javax.swing.JLabel jLabelLineTime1;
    private javax.swing.JLabel jLabelLineTime2;
    private javax.swing.JLabel jLabelLineTime3;
    private javax.swing.JLabel jLabelLineTime4;
    private javax.swing.JLabel jLabelLineTime5;
    private javax.swing.JLabel jLabelLineTime6;
    private javax.swing.JLabel jLabelLogo1;
    private javax.swing.JLabel jLabelLogo2;
    private javax.swing.JLabel jLabelLogo3;
    private javax.swing.JLabel jLabelLogo4;
    private javax.swing.JLabel jLabelLogo5;
    private javax.swing.JLabel jLabelLogo6;
    private javax.swing.JLabel jLabelLogo7;
    private javax.swing.JLabel jLabelOffice1;
    private javax.swing.JLabel jLabelPrjNameDet;
    private javax.swing.JLabel jLabelPrjTaskDet;
    private javax.swing.JLabel jLabelProjectName;
    private javax.swing.JLabel jLabelProjectTask;
    private javax.swing.JLabel jLabelProvince;
    private javax.swing.JLabel jLabelProvince1;
    private javax.swing.JLabel jLabelRef;
    private javax.swing.JLabel jLabelRefNum;
    private javax.swing.JLabel jLabelRejectHeader1;
    private javax.swing.JLabel jLabelRemain;
    private javax.swing.JLabel jLabelSerial;
    private javax.swing.JLabel jLabelWk1DateFrom;
    private javax.swing.JLabel jLabelWk1DateTo;
    private javax.swing.JLabel jLabelWk1DialogActivityDesc;
    private javax.swing.JLabel jLabelWk1DialogJustification;
    private javax.swing.JLabel jLabelWk1DialogStaffName;
    private javax.swing.JLabel jLabelWk1DialogStaffName1;
    private javax.swing.JLabel jLabelWk1DialogStaffName2;
    private javax.swing.JLabel jLabelWk1From;
    private javax.swing.JLabel jLabelWk1From1;
    private javax.swing.JLabel jLabelWk1Misc;
    private javax.swing.JLabel jLabelWk1MiscAmt;
    private javax.swing.JLabel jLabelWk1To;
    private javax.swing.JLabel jLabelWk1To1;
    private javax.swing.JLabel jLabelWk2DateFrom;
    private javax.swing.JLabel jLabelWk2DateTo;
    private javax.swing.JLabel jLabelWk3DateFrom;
    private javax.swing.JLabel jLabelWk3DateTo;
    private javax.swing.JLabel jLabelWk3From;
    private javax.swing.JLabel jLabelWk3To;
    private javax.swing.JLabel jLabelWk4DateFrom;
    private javax.swing.JLabel jLabelWk4DateTo;
    private javax.swing.JLabel jLabelWk4From;
    private javax.swing.JLabel jLabelWk4To;
    private javax.swing.JLabel jLabelWk5DateFrom;
    private javax.swing.JLabel jLabelWk5DateTo;
    private javax.swing.JLabel jLabelWk5From;
    private javax.swing.JLabel jLabelWk5To;
    private javax.swing.JLabel jLabelWkDuration;
    private javax.swing.JLabel jLabelWkDuration1;
    private javax.swing.JLabel jLabelWkDuration2;
    private javax.swing.JLabel jLabelWkDuration3;
    private javax.swing.JLabel jLabelWkDuration4;
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
    private javax.swing.JMenuItem jMenuItemSupSubmit;
    private javax.swing.JMenuItem jMenuItemUserProfUpd;
    private javax.swing.JMenuItem jMenuItemView;
    private javax.swing.JMenuItem jMenuMonPlanEdit;
    private javax.swing.JMenu jMenuMonthlyPlan;
    private javax.swing.JMenu jMenuNew;
    private javax.swing.JMenu jMenuPlanApproval;
    private javax.swing.JMenu jMenuReports;
    private javax.swing.JMenu jMenuRequest;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelAttDocWk1;
    private javax.swing.JPanel jPanelAttDocWk2;
    private javax.swing.JPanel jPanelAttDocWk3;
    private javax.swing.JPanel jPanelAttDocWk4;
    private javax.swing.JPanel jPanelAttDocWk5;
    private javax.swing.JPanel jPanelDetUpd2;
    private javax.swing.JPanel jPanelWk2;
    private javax.swing.JPanel jPanelWk3;
    private javax.swing.JPanel jPanelWk4;
    private javax.swing.JPanel jPanelWk5;
    private javax.swing.JPanel jPanelWkComments;
    private javax.swing.JPanel jPanelWkFive;
    private javax.swing.JPanel jPanelWkFour;
    private javax.swing.JPanel jPanelWkOne;
    private javax.swing.JPanel jPanelWkPrevComments;
    private javax.swing.JPanel jPanelWkThree;
    private javax.swing.JPanel jPanelWkTwo;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane8;
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
    private javax.swing.JSeparator jSeparator35;
    private javax.swing.JSeparator jSeparator36;
    private javax.swing.JSeparator jSeparator37;
    private javax.swing.JSeparator jSeparator38;
    private javax.swing.JSeparator jSeparator39;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator40;
    private javax.swing.JSeparator jSeparator41;
    private javax.swing.JSeparator jSeparator42;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator62;
    private javax.swing.JPopupMenu.Separator jSeparator63;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    private javax.swing.JTabbedPane jTabbedPaneMain;
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
    private javax.swing.JTextField jTextAmmendRequest;
    private javax.swing.JTextArea jTextAreaComments;
    private javax.swing.JTextArea jTextAreaPrevComments;
    private javax.swing.JTextArea jTextAreaWk1DialogJustification;
    private javax.swing.JTextField jTextFieldDialogWk1Site;
    private javax.swing.JTextField jTextFieldWk1DialogActivityDesc;
    private javax.swing.JTextField jTextFieldWk1DialogStaffName1;
    private javax.swing.JTextField jTextFieldWk1DialogStaffName2;
    private javax.swing.JTextField jTextFieldWk1DialogStaffName3;
    private javax.swing.JTextField jTextFieldWk1DialogStaffName4;
    private javax.swing.JTextField jTextFieldWk1Misc;
    private javax.swing.JTextField jTextFieldWk1MiscAmt;
    // End of variables declaration//GEN-END:variables
}
