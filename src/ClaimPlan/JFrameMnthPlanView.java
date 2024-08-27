/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClaimPlan;

import ClaimPlanPerDiem.JFrameMnthPlanPerDiemEdit;
import ClaimPlanPerDiem.JFrameMnthPlanPerDiemCreate;
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
import java.util.Date;
import javax.swing.Timer;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import utils.StockVehicleMgt;
import utils.connCred;

/**
 *
 * @author goredemac
 */
public class JFrameMnthPlanView extends javax.swing.JFrame {

    connCred c = new connCred();
    boolean ignoreInput = false;
    String filename = null;
    int charMax = 200;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String wk1Site = "N";
    String wk2Site = "N";
    String wk3Site = "N";
    String hostName = "";
    String breakfastAll, lunchAll, lunchNPAll, lunchPAll, dinnerAll, docVer, docNextVer, usrGrp,
            incidentalAll, unProvedAll, SearchRef, createUsrNam, usrMail, action, status, planStatus;
    PreparedStatement pst = null;
    PreparedStatement pst1 = null;
    int itmNum = 1;

    /**
     * Creates new form JFrameMnthPlanPerDiemCreate
     */
    public JFrameMnthPlanView() {
        initComponents();
        //  jTabbedPaneMain.remove(jTabbedPaneComments);
        jTabbedPaneMain.setEnabledAt(3, false);

    }

    public JFrameMnthPlanView(String ref, String usrLogNam) {
        initComponents();
        SearchRef = ref;
        showDate();
        showTime();
        fetchdataGenWk1();
        fetchdataWk1();
        fetchdataWk2();
        fetchdataWk3();
        fetchdataWk4();
        fetchdataWk5();
          jLabelEmp.setText(usrLogNam);
        jLabelEmp.setVisible(false);
        jLabelRefNum.setText(SearchRef);
        findUser();
        findCreator();
        findUserGrp();
        jTabbedPaneMain.setEnabledAt(3, false);
        
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

    void showDate() {
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");

        jLabelLineDate.setText(s.format(d));
        jLabelLineDate1.setText(s.format(d));
        jLabelLineDate2.setText(s.format(d));
        jLabelLineDate3.setText(s.format(d));
        jLabelLineDate5.setText(s.format(d));
        jLabelLineDate6.setText(s.format(d));

    }

    void findUser() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password="+ c.usrPFin +";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select * from [ClaimsAppSysZvandiri].[dbo]."
                    + "[EmpDetTab] where emp_num ='" + jLabelEmp.getText() + "'");

            while (r.next()) {

                jLabelLineLogNam.setText(r.getString(2));
                jLabelLineLogNam1.setText(r.getString(2));
                jLabelLineLogNam2.setText(r.getString(2));
                jLabelLineLogNam3.setText(r.getString(2));
                jLabelLineLogNam5.setText(r.getString(2));
                jLabelLineLogNam6.setText(r.getString(2));

//                sendTo = r.getString(5);
//                createUsrNam = r.getString(2);
//                supUsrMail = r.getString(6);
            }

            //                 conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Connect to Database (Province).Try Again", "Error Connection", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
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
                jMenuItemPlanView.setEnabled(false);
                jMenuItemPlanSupApproval.setEnabled(false);
                jMenuItemPlanHODApproval.setEnabled(false);

            }

            if ("usrFinSup".equals(usrGrp)) {
                
                
            
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

    void findCreator() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select ACTION_BY from [ClaimsAppSysZvandiri].[dbo]."
                    + "[PlanActTab] where PLAN_REF_NUM =" + SearchRef + " and ACT_VER = " + docVer);

            while (r.next()) {

                createUsrNam = r.getString(1);
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

    void fetchdataGenWk1() {
        try {

            DefaultTableModel modelWk1 = (DefaultTableModel) jTableWk1Activities.getModel();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password="+ c.usrPFin +";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();

            st.executeQuery("SELECT PROVINCE ,DISTRICT ,WK1_PLAN_START_DATE ,WK1_PLAN_END_DATE,"
                    + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,Wk3_PLAN_START_DATE,Wk3_PLAN_END_DATE,"
                    + "Wk4_PLAN_START_DATE,Wk4_PLAN_END_DATE,Wk5_PLAN_START_DATE,Wk5_PLAN_END_DATE "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab]"
                    + " where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA in ('A','E','C')");

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

            st1.executeQuery("SELECT distinct ACT_VER,ACT_VER + 1  FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] "
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA in ('A','E','C')");

            ResultSet r1 = st1.getResultSet();

            while (r1.next()) {
                docVer = r1.getString(1);
                docNextVer = r1.getString(2);
            }

            if (Integer.parseInt(docVer) == 1) {
                jTabbedPaneMain.remove(jPanelWkPrevComments);
            }

            st2.executeQuery("SELECT COMMENTS FROM [ClaimsAppSysZvandiri].[dbo].[PlanActTab]"
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA in ('A','E','C')");

            ResultSet r2 = st2.getResultSet();

            while (r2.next()) {
                jTextAreaPrevComments.setText(r2.getString(1));

            }

            jTextAreaPrevComments.setLineWrap(true);
            jTextAreaPrevComments.setWrapStyleWord(true);
            jTextAreaPrevComments.setEditable(false);
        } catch (Exception e) {

        }
    }

    void fetchdataWk1() {
        try {

            DefaultTableModel modelWk1 = (DefaultTableModel) jTableWk1Activities.getModel();
           Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password="+ c.usrPFin +";");
           
            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk1Tab] "
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA in ('A','E','C') order by 4");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk1.insertRow(modelWk1.getRowCount(), new Object[]{r.getString(3), r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22)});

            }

        } catch (Exception e) {

        }
    }

    void fetchdataWk2() {
        try {

            DefaultTableModel modelWk2 = (DefaultTableModel) jTableWk2Activities.getModel();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password="+ c.usrPFin +";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] "
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA in ('A','E','C') order by 4");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk2.insertRow(modelWk2.getRowCount(), new Object[]{r.getString(3), r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22)});

            }

        } catch (Exception e) {

        }
    }

    void fetchdataWk3() {
        try {

            DefaultTableModel modelWk3 = (DefaultTableModel) jTableWk3Activities.getModel();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password="+ c.usrPFin +";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] "
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA in ('A','E','C') order by 4");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk3.insertRow(modelWk3.getRowCount(), new Object[]{r.getString(3), r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22)});

            }

        } catch (Exception e) {

        }
    }

    void fetchdataWk4() {
        try {

            DefaultTableModel modelWk4 = (DefaultTableModel) jTableWk4Activities.getModel();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password="+ c.usrPFin +";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab] "
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA in ('A','E','C') order by 4");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk4.insertRow(modelWk4.getRowCount(), new Object[]{r.getString(3), r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22)});

            }

        } catch (Exception e) {

        }
    }

    void fetchdataWk5() {
        try {

            DefaultTableModel modelWk5 = (DefaultTableModel) jTableWk5Activities.getModel();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password="+ c.usrPFin +";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab] "
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA in ('A','E','C') order by 4");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk5.insertRow(modelWk5.getRowCount(), new Object[]{r.getString(3), r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22)});

            }

        } catch (Exception e) {

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
        jLabelHeaderGen6 = new javax.swing.JLabel();
        jLabelRef = new javax.swing.JLabel();
        jLabelSerial = new javax.swing.JLabel();
        jLabelRefNum = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableWk1Activities = new javax.swing.JTable();
        jPanelWkTwo = new javax.swing.JPanel();
        jLabelWkDuration1 = new javax.swing.JLabel();
        jLabelWk1From1 = new javax.swing.JLabel();
        jLabelWk1To1 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableWk2Activities = new javax.swing.JTable();
        jLabelLogo2 = new javax.swing.JLabel();
        jLabelWk2DateTo = new javax.swing.JLabel();
        jLabelWk2DateFrom = new javax.swing.JLabel();
        jLabelLineDate1 = new javax.swing.JLabel();
        jLabelLineTime1 = new javax.swing.JLabel();
        jLabelGenLogNam3 = new javax.swing.JLabel();
        jLabelLineLogNam1 = new javax.swing.JLabel();
        jLabelHeaderGen7 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTableMeetReqWk2 = new javax.swing.JTable();
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
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableWk3Activities = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTableMeetReqWk3 = new javax.swing.JTable();
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
        jScrollPane9 = new javax.swing.JScrollPane();
        jTableWk4Activities = new javax.swing.JTable();
        jScrollPane16 = new javax.swing.JScrollPane();
        jTableMeetReqWk4 = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
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
        jScrollPane10 = new javax.swing.JScrollPane();
        jTableWk5Activities = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane17 = new javax.swing.JScrollPane();
        jTableMeetReqWk5 = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jPanelWkComments = new javax.swing.JPanel();
        jSeparator35 = new javax.swing.JSeparator();
        jLabelLogo4 = new javax.swing.JLabel();
        jLabelLineLogNam3 = new javax.swing.JLabel();
        jLabelGenLogNam6 = new javax.swing.JLabel();
        jLabelLineDate3 = new javax.swing.JLabel();
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
        jSeparator11 = new javax.swing.JPopupMenu.Separator();
        jMenuItemClose = new javax.swing.JMenuItem();
        jSeparator33 = new javax.swing.JPopupMenu.Separator();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuRequest = new javax.swing.JMenu();
        jMenuItemSupApp = new javax.swing.JMenuItem();
        jSeparator47 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAccMgrRev = new javax.swing.JMenuItem();
        jSeparator48 = new javax.swing.JPopupMenu.Separator();
        jMenuItemHeadApp = new javax.swing.JMenuItem();
        jSeparator49 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSchPerDiem = new javax.swing.JMenuItem();
        jSeparator23 = new javax.swing.JPopupMenu.Separator();
        jMenuItemView = new javax.swing.JMenuItem();
        jMenuAcquittal = new javax.swing.JMenu();
        jMenuItemAcqSupApp = new javax.swing.JMenuItem();
        jSeparator50 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqAccApp = new javax.swing.JMenuItem();
        jSeparator51 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqHeadApp = new javax.swing.JMenuItem();
        jSeparator52 = new javax.swing.JPopupMenu.Separator();
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

        jPanelWkOne.setBackground(new java.awt.Color(255, 255, 204));
        jPanelWkOne.setLayout(null);

        jLabelWkDuration.setText("Week Duration");
        jPanelWkOne.add(jLabelWkDuration);
        jLabelWkDuration.setBounds(30, 180, 90, 30);

        jLabelWk1From.setText("From");
        jPanelWkOne.add(jLabelWk1From);
        jLabelWk1From.setBounds(150, 180, 41, 30);

        jLabelWk1To.setText("To");
        jPanelWkOne.add(jLabelWk1To);
        jLabelWk1To.setBounds(390, 180, 41, 30);
        jPanelWkOne.add(jSeparator1);
        jSeparator1.setBounds(30, 215, 1280, 10);
        jPanelWkOne.add(jSeparator2);
        jSeparator2.setBounds(30, 170, 1280, 10);

        jLabelLogo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelWkOne.add(jLabelLogo1);
        jLabelLogo1.setBounds(10, 10, 220, 100);

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
        jPanelWkOne.add(jLabelProvince);
        jLabelProvince.setBounds(150, 120, 320, 30);
        jPanelWkOne.add(jLabelDistrict);
        jLabelDistrict.setBounds(640, 120, 230, 30);
        jPanelWkOne.add(jLabelWk1DateTo);
        jLabelWk1DateTo.setBounds(440, 180, 100, 30);
        jPanelWkOne.add(jLabelWk1DateFrom);
        jLabelWk1DateFrom.setBounds(200, 180, 100, 30);
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

        jTableWk1Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item No", "Date", "Branch", "Project Code", "Task Code", "Site to Visit", "Activity", "Justification for Choice of Activity ", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Staff Member 1", "Staff Member 2", "Staff Member 3", "Staff Member 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableWk1Activities.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableWk1ActivitiesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableWk1Activities);

        jPanelWkOne.add(jScrollPane1);
        jScrollPane1.setBounds(30, 240, 1290, 420);

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

        jTableWk2Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Site", "Site Classification", "Consortium Rank", "Distance", "Activity", "Justification for Choice of Activity ", "Perdiem Required", "Budget Line", "B", "L", "D", "I", "Misc Desc", "M", "A", "OPHID Staff 1", "OPHID Staff 2", "OPHID Staff 3", "OPHID Staff 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableWk2Activities.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableWk2ActivitiesMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTableWk2Activities);

        jPanelWkTwo.add(jScrollPane3);
        jScrollPane3.setBounds(30, 240, 1290, 230);

        jLabelLogo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelWkTwo.add(jLabelLogo2);
        jLabelLogo2.setBounds(10, 10, 220, 100);
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
        jLabel3.setBounds(30, 225, 150, 15);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("PLANNED MEETINGS");
        jPanelWkTwo.add(jLabel4);
        jLabel4.setBounds(30, 480, 130, 15);

        jTableMeetReqWk2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jTableMeetReqWk2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Project", "Date From", "Date To", "Budget Line", "Activity Main Purpose", "Activity Detail", "Allowance", "Unit Cost", "No. of Persons", "No. of Days", "Line Total", "Pesron Responsible"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane14.setViewportView(jTableMeetReqWk2);

        jPanelWkTwo.add(jScrollPane14);
        jScrollPane14.setBounds(30, 500, 1290, 160);

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
        jLabelLogo3.setBounds(10, 10, 220, 100);
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
        jLabel5.setBounds(30, 225, 150, 15);

        jTableWk3Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Site", "Site Classification", "Consortium Rank", "Distance", "Activity", "Justification for Choice of Activity ", "Perdiem Required", "Budget Line", "B", "L", "D", "I", "Misc Desc", "M", "A", "OPHID Staff 1", "OPHID Staff 2", "OPHID Staff 3", "OPHID Staff 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableWk3Activities.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableWk3ActivitiesMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jTableWk3Activities);

        jPanelWkThree.add(jScrollPane5);
        jScrollPane5.setBounds(30, 240, 1290, 230);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("PLANNED MEETINGS");
        jPanelWkThree.add(jLabel6);
        jLabel6.setBounds(30, 480, 130, 15);

        jTableMeetReqWk3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jTableMeetReqWk3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Project", "Date From", "Date To", "Budget Line", "Activity Main Purpose", "Activity Detail", "Allowance", "Unit Cost", "No. of Persons", "No. of Days", "Line Total", "Pesron Responsible"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane15.setViewportView(jTableMeetReqWk3);

        jPanelWkThree.add(jScrollPane15);
        jScrollPane15.setBounds(30, 500, 1290, 160);

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
        jLabelLogo6.setBounds(10, 10, 220, 100);
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
        jLabel7.setBounds(30, 225, 150, 15);

        jTableWk4Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Site", "Site Classification", "Consortium Rank", "Distance", "Activity", "Justification for Choice of Activity ", "Perdiem Required", "Budget Line", "B", "L", "D", "I", "Misc Desc", "M", "A", "OPHID Staff 1", "OPHID Staff 2", "OPHID Staff 3", "OPHID Staff 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableWk4Activities.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableWk4ActivitiesMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(jTableWk4Activities);

        jPanelWkFour.add(jScrollPane9);
        jScrollPane9.setBounds(30, 240, 1290, 230);

        jTableMeetReqWk4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jTableMeetReqWk4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Project", "Date From", "Date To", "Budget Line", "Activity Main Purpose", "Activity Detail", "Allowance", "Unit Cost", "No. of Persons", "No. of Days", "Line Total", "Pesron Responsible"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane16.setViewportView(jTableMeetReqWk4);

        jPanelWkFour.add(jScrollPane16);
        jScrollPane16.setBounds(30, 500, 1290, 160);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("PLANNED MEETINGS");
        jPanelWkFour.add(jLabel8);
        jLabel8.setBounds(30, 480, 130, 15);

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
        jLabelLogo7.setBounds(10, 10, 220, 100);
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

        jTableWk5Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Site", "Site Classification", "Consortium Rank", "Distance", "Activity", "Justification for Choice of Activity ", "Perdiem Required", "Budget Line", "B", "L", "D", "I", "Misc Desc", "M", "A", "OPHID Staff 1", "OPHID Staff 2", "OPHID Staff 3", "OPHID Staff 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableWk5Activities.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableWk5ActivitiesMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(jTableWk5Activities);

        jPanelWkFive.add(jScrollPane10);
        jScrollPane10.setBounds(30, 240, 1290, 230);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("PLANNED MEETINGS");
        jPanelWkFive.add(jLabel11);
        jLabel11.setBounds(30, 480, 130, 15);

        jTableMeetReqWk5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jTableMeetReqWk5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Project", "Date From", "Date To", "Budget Line", "Activity Main Purpose", "Activity Detail", "Allowance", "Unit Cost", "No. of Persons", "No. of Days", "Line Total", "Pesron Responsible"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane17.setViewportView(jTableMeetReqWk5);

        jPanelWkFive.add(jScrollPane17);
        jScrollPane17.setBounds(30, 500, 1290, 160);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("PLANNED ACTIVITIES");
        jPanelWkFive.add(jLabel9);
        jLabel9.setBounds(30, 225, 150, 15);

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
        jLabelLogo5.setBounds(10, 10, 220, 100);

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
        jMenuFile.add(jSeparator11);

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
        jMenuRequest.add(jSeparator47);

        jMenuItemAccMgrRev.setText("Finance Team Approval");
        jMenuItemAccMgrRev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAccMgrRevActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemAccMgrRev);
        jMenuRequest.add(jSeparator48);

        jMenuItemHeadApp.setText("Project HOD Approval");
        jMenuItemHeadApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemHeadAppActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemHeadApp);
        jMenuRequest.add(jSeparator49);

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
        jMenuAcquittal.add(jSeparator50);

        jMenuItemAcqAccApp.setText("Finance Team Approval");
        jMenuItemAcqAccApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqAccAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqAccApp);
        jMenuAcquittal.add(jSeparator51);

        jMenuItemAcqHeadApp.setText("Project HOD Approval");
        jMenuItemAcqHeadApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqHeadAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqHeadApp);
        jMenuAcquittal.add(jSeparator52);

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
            .addComponent(jTabbedPaneMain)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneMain)
        );

        setSize(new java.awt.Dimension(1376, 760));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTableWk2ActivitiesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableWk2ActivitiesMouseClicked
//        if (evt.getClickCount() == 2) {
//            int row = jTableWk2Activities.getSelectedRow();
//
//            int col = 0;
//            int col1 = 1;
//            int col2 = 2;
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
//            int col20 = 20;
//
//            Object id = jTableWk2Activities.getValueAt(row, col);
//            Object id1 = jTableWk2Activities.getValueAt(row, col1);
//            Object id2 = jTableWk2Activities.getValueAt(row, col2);
//            Object id3 = jTableWk2Activities.getValueAt(row, col3);
//            Object id4 = jTableWk2Activities.getValueAt(row, col4);
//            Object id5 = jTableWk2Activities.getValueAt(row, col5);
//            Object id6 = jTableWk2Activities.getValueAt(row, col6);
//            Object id7 = jTableWk2Activities.getValueAt(row, col7);
//            Object id8 = jTableWk2Activities.getValueAt(row, col8);
//            Object id9 = jTableWk2Activities.getValueAt(row, col9);
//            Object id10 = jTableWk2Activities.getValueAt(row, col10);
//            Object id11 = jTableWk2Activities.getValueAt(row, col11);
//            Object id12 = jTableWk2Activities.getValueAt(row, col12);
//            Object id13 = jTableWk2Activities.getValueAt(row, col13);
//            Object id14 = jTableWk2Activities.getValueAt(row, col14);
//            Object id15 = jTableWk2Activities.getValueAt(row, col15);
//            Object id16 = jTableWk2Activities.getValueAt(row, col16);
//            Object id17 = jTableWk2Activities.getValueAt(row, col17);
//            Object id18 = jTableWk2Activities.getValueAt(row, col18);
//            Object id19 = jTableWk2Activities.getValueAt(row, col19);
//            //      Object id20 = jTableWk2Activities.getValueAt(row, col20);
//
//            jLabelActWk2Date.setText(id.toString());
//            jTextFieldDialogWk2Site.setText(id1.toString());
//            jLabelDialogWk2ActSiteClass.setText(id2.toString());
//            jLabelWk2DialogActRank.setText(id3.toString());
//            jLabelWk2DialogActDis.setText(id4.toString());
//            jTextFieldWk2DialogActivityDesc.setText(id5.toString());
//            jTextAreaWk2DialogJustification.setText(id6.toString());
//            jLabelStaffWk2.setText(id7.toString());
//            jTextFieldWk2DialogBudget.setText(id8.toString());
//            if ((Double.parseDouble(id9.toString())) > 0) {
//                jCheckBoxDialogWk2BrkFast.setSelected(true);
//            }
//            if ((Double.parseDouble(id10.toString())) > 0) {
//                jCheckBoxDialogWk2Lunch.setSelected(true);
//            }
//            if ((Double.parseDouble(id11.toString())) > 0) {
//                jCheckBoxDialogWk2Dinner.setSelected(true);
//            }
//            if ((Double.parseDouble(id12.toString())) > 0) {
//                jCheckBoxDialogWk2Inc.setSelected(true);
//            }
//
//            if ((Double.parseDouble(id14.toString())) > 0) {
//                jCheckBoxDialogWk2Misc.setSelected(true);
//            }
//            jTextFieldWk2Misc.setText(id13.toString());
//            jTextFieldWk2MiscAmt.setText(id14.toString());
//            if ((Double.parseDouble(id15.toString())) > 0) {
//                jCheckBoxDialogWk2Acc.setSelected(true);
//
//            }
//            jTextFieldWk2DialogStaffName1.setText(id16.toString());
//            jTextFieldWk2DialogStaffName2.setText(id17.toString());
//            jTextFieldWk2DialogStaffName3.setText(id18.toString());
//            jTextFieldWk2DialogStaffName4.setText(id19.toString());
//
//            jDialogWk2.setVisible(true);
//            jTextAreaWk2DialogJustification.setLineWrap(true);
//            jTextAreaWk2DialogJustification.setWrapStyleWord(true);
//            jTextFieldDialogWk2Site.setEditable(false);
//            jTextFieldWk2DialogBudget.setEditable(false);
//            jTextFieldWk2DialogActivityDesc.setEditable(false);
//            jTextAreaWk2DialogJustification.setEditable(false);
//            jTextFieldWk2DialogStaffName1.setEditable(false);
//            jTextFieldWk2DialogStaffName2.setEditable(false);
//            jTextFieldWk2DialogStaffName3.setEditable(false);
//            jTextFieldWk2DialogStaffName4.setEditable(false);
//            jTextFieldWk2Misc.setEditable(false);
//            jTextFieldWk2MiscAmt.setEditable(false);
//            jCheckBoxDialogWk2BrkFast.setEnabled(false);
//            jCheckBoxDialogWk2Lunch.setEnabled(false);
//            jCheckBoxDialogWk2Dinner.setEnabled(false);
//            jCheckBoxDialogWk2Inc.setEnabled(false);
//            jCheckBoxDialogWk2Acc.setEnabled(false);
//            jCheckBoxDialogWk2Misc.setEnabled(false);
//
//        }
    }//GEN-LAST:event_jTableWk2ActivitiesMouseClicked

    private void jMenuPlanApprovalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuPlanApprovalActionPerformed
        
    }//GEN-LAST:event_jMenuPlanApprovalActionPerformed

    private void jTableWk3ActivitiesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableWk3ActivitiesMouseClicked
//        if (evt.getClickCount() == 2) {
//            int row = jTableWk3Activities.getSelectedRow();
//
//            int col = 0;
//            int col1 = 1;
//            int col2 = 2;
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
//            int col20 = 20;
//
//            Object id = jTableWk3Activities.getValueAt(row, col);
//            Object id1 = jTableWk3Activities.getValueAt(row, col1);
//            Object id2 = jTableWk3Activities.getValueAt(row, col2);
//            Object id3 = jTableWk3Activities.getValueAt(row, col3);
//            Object id4 = jTableWk3Activities.getValueAt(row, col4);
//            Object id5 = jTableWk3Activities.getValueAt(row, col5);
//            Object id6 = jTableWk3Activities.getValueAt(row, col6);
//            Object id7 = jTableWk3Activities.getValueAt(row, col7);
//            Object id8 = jTableWk3Activities.getValueAt(row, col8);
//            Object id9 = jTableWk3Activities.getValueAt(row, col9);
//            Object id10 = jTableWk3Activities.getValueAt(row, col10);
//            Object id11 = jTableWk3Activities.getValueAt(row, col11);
//            Object id12 = jTableWk3Activities.getValueAt(row, col12);
//            Object id13 = jTableWk3Activities.getValueAt(row, col13);
//            Object id14 = jTableWk3Activities.getValueAt(row, col14);
//            Object id15 = jTableWk3Activities.getValueAt(row, col15);
//            Object id16 = jTableWk3Activities.getValueAt(row, col16);
//            Object id17 = jTableWk3Activities.getValueAt(row, col17);
//            Object id18 = jTableWk3Activities.getValueAt(row, col18);
//            Object id19 = jTableWk3Activities.getValueAt(row, col19);
//            //      Object id20 = jTableWk3Activities.getValueAt(row, col20);
//
//            jLabelActWk3Date.setText(id.toString());
//            jTextFieldDialogWk3Site.setText(id1.toString());
//            jLabelDialogWk3ActSiteClass.setText(id2.toString());
//            jLabelWk3DialogActRank.setText(id3.toString());
//            jLabelWk3DialogActDis.setText(id4.toString());
//            jTextFieldWk3DialogActivityDesc.setText(id5.toString());
//            jTextAreaWk3DialogJustification.setText(id6.toString());
//            jLabelStaffWk3.setText(id7.toString());
//            jTextFieldWk3DialogBudget.setText(id8.toString());
//            if ((Double.parseDouble(id9.toString())) > 0) {
//                jCheckBoxDialogWk3BrkFast.setSelected(true);
//            }
//            if ((Double.parseDouble(id10.toString())) > 0) {
//                jCheckBoxDialogWk3Lunch.setSelected(true);
//            }
//            if ((Double.parseDouble(id11.toString())) > 0) {
//                jCheckBoxDialogWk3Dinner.setSelected(true);
//            }
//            if ((Double.parseDouble(id12.toString())) > 0) {
//                jCheckBoxDialogWk3Inc.setSelected(true);
//            }
//
//            if ((Double.parseDouble(id14.toString())) > 0) {
//                jCheckBoxDialogWk3Misc.setSelected(true);
//            }
//            jTextFieldWk3Misc.setText(id13.toString());
//            jTextFieldWk3MiscAmt.setText(id14.toString());
//            if ((Double.parseDouble(id15.toString())) > 0) {
//                jCheckBoxDialogWk3Acc.setSelected(true);
//
//            }
//            jTextFieldWk3DialogStaffName1.setText(id16.toString());
//            jTextFieldWk3DialogStaffName2.setText(id17.toString());
//            jTextFieldWk3DialogStaffName3.setText(id18.toString());
//            jTextFieldWk3DialogStaffName4.setText(id19.toString());
//
//            jDialogWk3.setVisible(true);
//            jTextAreaWk1DialogJustification.setLineWrap(true);
//            jTextAreaWk1DialogJustification.setWrapStyleWord(true);
//            jTextFieldDialogWk3Site.setEditable(false);
//            jTextFieldWk3DialogBudget.setEditable(false);
//            jTextFieldWk3DialogActivityDesc.setEditable(false);
//            jTextAreaWk3DialogJustification.setEditable(false);
//            jTextFieldWk3DialogStaffName1.setEditable(false);
//            jTextFieldWk3DialogStaffName2.setEditable(false);
//            jTextFieldWk3DialogStaffName3.setEditable(false);
//            jTextFieldWk3DialogStaffName4.setEditable(false);
//            jTextFieldWk3Misc.setEditable(false);
//            jTextFieldWk3MiscAmt.setEditable(false);
//            jCheckBoxDialogWk3BrkFast.setEnabled(false);
//            jCheckBoxDialogWk3Lunch.setEnabled(false);
//            jCheckBoxDialogWk3Dinner.setEnabled(false);
//            jCheckBoxDialogWk3Inc.setEnabled(false);
//            jCheckBoxDialogWk3Acc.setEnabled(false);
//            jCheckBoxDialogWk3Misc.setEnabled(false);
//
//        }
    }//GEN-LAST:event_jTableWk3ActivitiesMouseClicked

    private void jTableWk4ActivitiesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableWk4ActivitiesMouseClicked
       
    }//GEN-LAST:event_jTableWk4ActivitiesMouseClicked

    private void jTableWk5ActivitiesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableWk5ActivitiesMouseClicked
       
    }//GEN-LAST:event_jTableWk5ActivitiesMouseClicked

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

    private void jTableWk1ActivitiesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableWk1ActivitiesMouseClicked
//        if (evt.getClickCount() == 2) {
//      
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
//            if (("National Office".equals(jLabelDistrict.getText()))) {
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
    }//GEN-LAST:event_jTableWk1ActivitiesMouseClicked

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
            java.util.logging.Logger.getLogger(JFrameMnthPlanView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthPlanView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthPlanView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthPlanView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new JFrameMnthPlanView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBoxDialogWk1AccProved;
    private javax.swing.JCheckBox jCheckBoxDialogWk1AccUnProved;
    private javax.swing.JCheckBox jCheckBoxDialogWk1BrkFast;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Dinner;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Inc;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Lunch;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Misc;
    private javax.swing.JCheckBox jCheckBoxNoAcc;
    private javax.swing.JDialog jDialogWk1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelActWk1Date;
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
    private javax.swing.JLabel jLabelLineDate3;
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
    private javax.swing.JPanel jPanelWkComments;
    private javax.swing.JPanel jPanelWkFive;
    private javax.swing.JPanel jPanelWkFour;
    private javax.swing.JPanel jPanelWkOne;
    private javax.swing.JPanel jPanelWkPrevComments;
    private javax.swing.JPanel jPanelWkThree;
    private javax.swing.JPanel jPanelWkTwo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator11;
    private javax.swing.JPopupMenu.Separator jSeparator13;
    private javax.swing.JPopupMenu.Separator jSeparator16;
    private javax.swing.JPopupMenu.Separator jSeparator18;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator22;
    private javax.swing.JPopupMenu.Separator jSeparator23;
    private javax.swing.JPopupMenu.Separator jSeparator26;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator33;
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
    private javax.swing.JPopupMenu.Separator jSeparator47;
    private javax.swing.JPopupMenu.Separator jSeparator48;
    private javax.swing.JPopupMenu.Separator jSeparator49;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator50;
    private javax.swing.JPopupMenu.Separator jSeparator51;
    private javax.swing.JPopupMenu.Separator jSeparator52;
    private javax.swing.JPopupMenu.Separator jSeparator62;
    private javax.swing.JPopupMenu.Separator jSeparator63;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JTabbedPane jTabbedPaneMain;
    private javax.swing.JTable jTableMeetReqWk2;
    private javax.swing.JTable jTableMeetReqWk3;
    private javax.swing.JTable jTableMeetReqWk4;
    private javax.swing.JTable jTableMeetReqWk5;
    private javax.swing.JTable jTableWk1Activities;
    private javax.swing.JTable jTableWk2Activities;
    private javax.swing.JTable jTableWk3Activities;
    private javax.swing.JTable jTableWk4Activities;
    private javax.swing.JTable jTableWk5Activities;
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
