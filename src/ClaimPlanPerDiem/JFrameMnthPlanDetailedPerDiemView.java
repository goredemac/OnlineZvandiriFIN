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
import ClaimPlan.*;
import utils.StockVehicleMgt;
import utils.connCred;

/**
 *
 * @author goredemac
 */
public class JFrameMnthPlanDetailedPerDiemView extends javax.swing.JFrame {

    connCred c = new connCred();
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
    public JFrameMnthPlanDetailedPerDiemView() {
        initComponents();
        //  jTabbedPaneMain.remove(jTabbedPaneComments);
        jTabbedPaneMain.setEnabledAt(3, false);

    }

    public JFrameMnthPlanDetailedPerDiemView(String ref, String usrLogNam) {
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
//        fetchdataMeetWk1();
//        fetchdataMeetWk2();
//        fetchdataMeetWk3();
//        fetchdataMeetWk4();
//        fetchdataMeetWk5();
        jLabelEmp.setText(usrLogNam);
        jLabelEmp.setVisible(false);
          jLabelRefNum.setText(SearchRef);
        findUser();
        findCreator();
        findUserGrp();
        
       if (!"Administrator".equals(usrGrp)) {
            jMenuItemUserProfUpd.setEnabled(false);
            jMenuItemUserCreate.setEnabled(false);
        }
        
        if (!"National Office".equals(jLabelDistrict.getText())) {
            jTabbedPaneMain.setTitleAt(0, "Month Plan");
            jTabbedPaneMain.setEnabledAt(1, false);
            jTabbedPaneMain.setTitleAt(1, "");
            jTabbedPaneMain.setEnabledAt(2, false);
            jTabbedPaneMain.setTitleAt(2, "");
            jTabbedPaneMain.setEnabledAt(3, false);
            jTabbedPaneMain.setTitleAt(3, "");
            jTabbedPaneMain.setEnabledAt(4, false);
            jTabbedPaneMain.setTitleAt(4, "");
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
        jLabelLineDate4.setText(s.format(d));

    }

    void findUser() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select EMP_NAM from [ClaimsAppSysZvandiri].[dbo]."
                    + "[EmpDetTab] where emp_num ='" + jLabelEmp.getText() + "'");

            while (r.next()) {

                jLabelLineLogNam.setText(r.getString(1));
                jLabelLineLogNam1.setText(r.getString(1));
                jLabelLineLogNam2.setText(r.getString(1));
                jLabelLineLogNam3.setText(r.getString(1));
                jLabelLineLogNam4.setText(r.getString(1));

            }

            //                 conn.close();
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

    void findCreator() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select ACTIONED_BY_NAM from [ClaimsAppSysZvandiri].[dbo]."
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
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();

            st.executeQuery("SELECT PROVINCE ,DISTRICT ,WK1_PLAN_START_DATE ,WK1_PLAN_END_DATE,"
                    + "Wk2_PLAN_START_DATE,Wk2_PLAN_END_DATE,Wk3_PLAN_START_DATE,Wk3_PLAN_END_DATE,"
                    + "Wk4_PLAN_START_DATE,Wk4_PLAN_END_DATE,Wk5_PLAN_START_DATE,Wk5_PLAN_END_DATE "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[PlanPeriodTab]"
                    + " where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA in ('A','C','E')");

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

            st2.executeQuery("SELECT USR_ACTION FROM [ClaimsAppSysZvandiri].[dbo].[PlanActTab]"
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA in ('A','C','E')");

            ResultSet r2 = st2.getResultSet();

            while (r2.next()) {
                planStatus = r2.getString(1);

                jLabelStatus.setText("Status : " + planStatus);
                jLabelStatus1.setText("Status : " + planStatus);
                jLabelStatus2.setText("Status : " + planStatus);
                jLabelStatus3.setText("Status : " + planStatus);
                jLabelStatus4.setText("Status : " + planStatus);

            }

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
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA in ('A','C','E')");

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

    void fetchdataWk2() {
        try {

            DefaultTableModel modelWk2 = (DefaultTableModel) jTableWk2Activities.getModel();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] "
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA in ('A','C','E')");

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

    void fetchdataWk3() {
        try {

            DefaultTableModel modelWk3 = (DefaultTableModel) jTableWk3Activities.getModel();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] "
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA in ('A','C','E')");

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

    void fetchdataWk4() {
        try {

            DefaultTableModel modelWk4 = (DefaultTableModel) jTableWk4Activities.getModel();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab] "
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA in ('A','C','E')");

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

    void fetchdataWk5() {
        try {

            DefaultTableModel modelWk5 = (DefaultTableModel) jTableWk5Activities.getModel();
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab] "
                    + "where PLAN_REF_NUM =" + SearchRef + " and ACT_REC_STA in ('A','C','E')");

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



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        jLabelStatus = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
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
        jLabelStatus1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
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
        jLabelStatus2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableWk3Activities = new javax.swing.JTable();
        jPanelWkFour = new javax.swing.JPanel();
        jLabelWkDuration3 = new javax.swing.JLabel();
        jLabelWk4From = new javax.swing.JLabel();
        jLabelWk4To = new javax.swing.JLabel();
        jSeparator12 = new javax.swing.JSeparator();
        jSeparator35 = new javax.swing.JSeparator();
        jLabelLogo4 = new javax.swing.JLabel();
        jLabelWk4DateTo = new javax.swing.JLabel();
        jLabelWk4DateFrom = new javax.swing.JLabel();
        jLabelLineLogNam3 = new javax.swing.JLabel();
        jLabelGenLogNam6 = new javax.swing.JLabel();
        jLabelLineDate3 = new javax.swing.JLabel();
        jLabelLineTime3 = new javax.swing.JLabel();
        jLabelHeaderGen5 = new javax.swing.JLabel();
        jLabelStatus3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableWk4Activities = new javax.swing.JTable();
        jPanelWkFive = new javax.swing.JPanel();
        jLabelWkDuration4 = new javax.swing.JLabel();
        jLabelWk5From = new javax.swing.JLabel();
        jLabelWk5To = new javax.swing.JLabel();
        jSeparator36 = new javax.swing.JSeparator();
        jSeparator37 = new javax.swing.JSeparator();
        jLabelLogo5 = new javax.swing.JLabel();
        jLabelWk5DateTo = new javax.swing.JLabel();
        jLabelWk5DateFrom = new javax.swing.JLabel();
        jLabelLineLogNam4 = new javax.swing.JLabel();
        jLabelGenLogNam7 = new javax.swing.JLabel();
        jLabelLineDate4 = new javax.swing.JLabel();
        jLabelLineTime4 = new javax.swing.JLabel();
        jLabelHeaderGen8 = new javax.swing.JLabel();
        jLabelStatus4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTableWk5Activities = new javax.swing.JTable();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuNew = new javax.swing.JMenu();
        jMenuItemPlanPerDiem = new javax.swing.JMenuItem();
        jSeparator13 = new javax.swing.JPopupMenu.Separator();
        jMenuItemPerDiemAcquittal = new javax.swing.JMenuItem();
        jSeparator16 = new javax.swing.JPopupMenu.Separator();
        jMenuEdit = new javax.swing.JMenu();
        jMenuMonPlanEdit = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItemClose = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuRequest = new javax.swing.JMenu();
        jMenuItemSupApp = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAccMgrRev = new javax.swing.JMenuItem();
        jSeparator11 = new javax.swing.JPopupMenu.Separator();
        jMenuItemHeadApp = new javax.swing.JMenuItem();
        jSeparator18 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSchPerDiem = new javax.swing.JMenuItem();
        jSeparator23 = new javax.swing.JPopupMenu.Separator();
        jMenuItemView = new javax.swing.JMenuItem();
        jMenuAcquittal = new javax.swing.JMenu();
        jMenuItemAcqSupApp = new javax.swing.JMenuItem();
        jSeparator33 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqAccApp = new javax.swing.JMenuItem();
        jSeparator43 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqHeadApp = new javax.swing.JMenuItem();
        jSeparator44 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqSchGen = new javax.swing.JMenuItem();
        jSeparator45 = new javax.swing.JPopupMenu.Separator();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanelWkOne.setBackground(new java.awt.Color(255, 255, 204));
        jPanelWkOne.setLayout(null);

        jLabelWkDuration.setText("Week Duration");
        jPanelWkOne.add(jLabelWkDuration);
        jLabelWkDuration.setBounds(30, 170, 90, 30);

        jLabelWk1From.setText("From");
        jPanelWkOne.add(jLabelWk1From);
        jLabelWk1From.setBounds(150, 170, 41, 30);

        jLabelWk1To.setText("To");
        jPanelWkOne.add(jLabelWk1To);
        jLabelWk1To.setBounds(390, 170, 41, 30);
        jPanelWkOne.add(jSeparator1);
        jSeparator1.setBounds(30, 200, 1280, 10);
        jPanelWkOne.add(jSeparator2);
        jSeparator2.setBounds(30, 160, 1280, 10);

        jLabelLogo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ophidlog.jpg"))); // NOI18N
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
        jLabelWk1DateTo.setBounds(440, 170, 100, 30);
        jPanelWkOne.add(jLabelWk1DateFrom);
        jLabelWk1DateFrom.setBounds(200, 170, 100, 30);
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

        jLabelStatus.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanelWkOne.add(jLabelStatus);
        jLabelStatus.setBounds(430, 90, 350, 30);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("PLANNED ACTIVITIES");
        jPanelWkOne.add(jLabel1);
        jLabel1.setBounds(30, 225, 150, 15);

        jTableWk1Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Branch", "Project Code", "Task Code", "Site to Visit", "Activity", "Justification for Choice of Activity ", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Staff Member 1", "Staff Member 2", "Staff Member 3", "Staff Member 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
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

        jLabelLogo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ophidlog.jpg"))); // NOI18N
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

        jLabelStatus1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jPanelWkTwo.add(jLabelStatus1);
        jLabelStatus1.setBounds(470, 100, 290, 30);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("PLANNED ACTIVITIES");
        jPanelWkTwo.add(jLabel3);
        jLabel3.setBounds(30, 210, 150, 15);

        jTableWk2Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Branch", "Project Code", "Task Code", "Site to Visit", "Activity", "Justification for Choice of Activity ", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Staff Member 1", "Staff Member 2", "Staff Member 3", "Staff Member 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
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
        jScrollPane11.setViewportView(jTableWk2Activities);

        jPanelWkTwo.add(jScrollPane11);
        jScrollPane11.setBounds(30, 230, 1290, 430);

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

        jLabelLogo3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ophidlog.jpg"))); // NOI18N
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

        jLabelStatus2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelStatus2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkThree.add(jLabelStatus2);
        jLabelStatus2.setBounds(470, 100, 290, 30);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("PLANNED ACTIVITIES");
        jPanelWkThree.add(jLabel5);
        jLabel5.setBounds(30, 210, 150, 15);

        jTableWk3Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Branch", "Project Code", "Task Code", "Site to Visit", "Activity", "Justification for Choice of Activity ", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Staff Member 1", "Staff Member 2", "Staff Member 3", "Staff Member 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
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
        jScrollPane3.setViewportView(jTableWk3Activities);

        jPanelWkThree.add(jScrollPane3);
        jScrollPane3.setBounds(30, 240, 1290, 420);

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
        jPanelWkFour.add(jSeparator12);
        jSeparator12.setBounds(30, 200, 1280, 10);
        jPanelWkFour.add(jSeparator35);
        jSeparator35.setBounds(30, 150, 1280, 10);

        jLabelLogo4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ophidlog.jpg"))); // NOI18N
        jPanelWkFour.add(jLabelLogo4);
        jLabelLogo4.setBounds(10, 10, 220, 100);
        jPanelWkFour.add(jLabelWk4DateTo);
        jLabelWk4DateTo.setBounds(440, 160, 100, 30);
        jPanelWkFour.add(jLabelWk4DateFrom);
        jLabelWk4DateFrom.setBounds(200, 160, 100, 30);

        jLabelLineLogNam3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam3.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFour.add(jLabelLineLogNam3);
        jLabelLineLogNam3.setBounds(1180, 30, 160, 30);

        jLabelGenLogNam6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam6.setForeground(new java.awt.Color(255, 255, 255));
        jLabelGenLogNam6.setText("Logged In");
        jPanelWkFour.add(jLabelGenLogNam6);
        jLabelGenLogNam6.setBounds(1090, 30, 100, 30);

        jLabelLineDate3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate3.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFour.add(jLabelLineDate3);
        jLabelLineDate3.setBounds(1090, 0, 110, 30);

        jLabelLineTime3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime3.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFour.add(jLabelLineTime3);
        jLabelLineTime3.setBounds(1250, 0, 80, 30);

        jLabelHeaderGen5.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen5.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeaderGen5.setText("MONTHLY  PLAN ");
        jPanelWkFour.add(jLabelHeaderGen5);
        jLabelHeaderGen5.setBounds(450, 40, 420, 40);

        jLabelStatus3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelStatus3.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFour.add(jLabelStatus3);
        jLabelStatus3.setBounds(470, 100, 290, 30);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("PLANNED ACTIVITIES");
        jPanelWkFour.add(jLabel7);
        jLabel7.setBounds(30, 210, 150, 15);

        jTableWk4Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Branch", "Project Code", "Task Code", "Site to Visit", "Activity", "Justification for Choice of Activity ", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Staff Member 1", "Staff Member 2", "Staff Member 3", "Staff Member 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
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
        jScrollPane5.setViewportView(jTableWk4Activities);

        jPanelWkFour.add(jScrollPane5);
        jScrollPane5.setBounds(30, 240, 1290, 420);

        jTabbedPaneMain.addTab("Week Four", jPanelWkFour);

        jPanelWkFive.setBackground(new java.awt.Color(217, 192, 250));
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
        jPanelWkFive.add(jSeparator36);
        jSeparator36.setBounds(30, 200, 1280, 10);
        jPanelWkFive.add(jSeparator37);
        jSeparator37.setBounds(30, 150, 1280, 10);

        jLabelLogo5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ophidlog.jpg"))); // NOI18N
        jPanelWkFive.add(jLabelLogo5);
        jLabelLogo5.setBounds(10, 10, 220, 100);
        jPanelWkFive.add(jLabelWk5DateTo);
        jLabelWk5DateTo.setBounds(440, 160, 100, 30);
        jPanelWkFive.add(jLabelWk5DateFrom);
        jLabelWk5DateFrom.setBounds(200, 160, 100, 30);

        jLabelLineLogNam4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam4.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFive.add(jLabelLineLogNam4);
        jLabelLineLogNam4.setBounds(1180, 30, 160, 30);

        jLabelGenLogNam7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam7.setForeground(new java.awt.Color(255, 255, 255));
        jLabelGenLogNam7.setText("Logged In");
        jPanelWkFive.add(jLabelGenLogNam7);
        jLabelGenLogNam7.setBounds(1090, 30, 100, 30);

        jLabelLineDate4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate4.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFive.add(jLabelLineDate4);
        jLabelLineDate4.setBounds(1090, 0, 110, 30);

        jLabelLineTime4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime4.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFive.add(jLabelLineTime4);
        jLabelLineTime4.setBounds(1250, 0, 80, 30);

        jLabelHeaderGen8.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen8.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeaderGen8.setText("MONTHLY  PLAN ");
        jPanelWkFive.add(jLabelHeaderGen8);
        jLabelHeaderGen8.setBounds(450, 40, 420, 40);

        jLabelStatus4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelStatus4.setForeground(new java.awt.Color(255, 255, 255));
        jPanelWkFive.add(jLabelStatus4);
        jLabelStatus4.setBounds(470, 100, 290, 30);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("PLANNED ACTIVITIES");
        jPanelWkFive.add(jLabel9);
        jLabel9.setBounds(30, 210, 150, 15);

        jTableWk5Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Branch", "Project Code", "Task Code", "Site to Visit", "Activity", "Justification for Choice of Activity ", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Staff Member 1", "Staff Member 2", "Staff Member 3", "Staff Member 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
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
        jScrollPane9.setViewportView(jTableWk5Activities);

        jPanelWkFive.add(jScrollPane9);
        jScrollPane9.setBounds(30, 240, 1290, 420);

        jTabbedPaneMain.addTab("Week Five", jPanelWkFive);

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
        jMenuFile.add(jSeparator3);

        jMenuItemClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemClose.setText("Close");
        jMenuItemClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemClose);
        jMenuFile.add(jSeparator6);

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
        jMenuRequest.add(jSeparator10);

        jMenuItemAccMgrRev.setText("Finance Team Approval");
        jMenuItemAccMgrRev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAccMgrRevActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemAccMgrRev);
        jMenuRequest.add(jSeparator11);

        jMenuItemHeadApp.setText("Project HOD Approval");
        jMenuItemHeadApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemHeadAppActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemHeadApp);
        jMenuRequest.add(jSeparator18);

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
        jMenuAcquittal.add(jSeparator33);

        jMenuItemAcqAccApp.setText("Finance Team Approval");
        jMenuItemAcqAccApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqAccAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqAccApp);
        jMenuAcquittal.add(jSeparator43);

        jMenuItemAcqHeadApp.setText("Project HOD Approval");
        jMenuItemAcqHeadApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqHeadAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqHeadApp);
        jMenuAcquittal.add(jSeparator44);

        jMenuItemAcqSchGen.setText("Acquittals Schedule Generation");
        jMenuItemAcqSchGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqSchGenActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqSchGen);
        jMenuAcquittal.add(jSeparator45);

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

    private void jMenuPlanApprovalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuPlanApprovalActionPerformed
       
    }//GEN-LAST:event_jMenuPlanApprovalActionPerformed

    private void jTableWk5ActivitiesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableWk5ActivitiesMouseClicked

    }//GEN-LAST:event_jTableWk5ActivitiesMouseClicked

    private void jTableWk4ActivitiesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableWk4ActivitiesMouseClicked

    }//GEN-LAST:event_jTableWk4ActivitiesMouseClicked

    private void jTableWk3ActivitiesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableWk3ActivitiesMouseClicked

    }//GEN-LAST:event_jTableWk3ActivitiesMouseClicked

    private void jTableWk2ActivitiesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableWk2ActivitiesMouseClicked

    }//GEN-LAST:event_jTableWk2ActivitiesMouseClicked

    private void jTableWk1ActivitiesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableWk1ActivitiesMouseClicked

    }//GEN-LAST:event_jTableWk1ActivitiesMouseClicked

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
            java.util.logging.Logger.getLogger(JFrameMnthPlanDetailedPerDiemView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthPlanDetailedPerDiemView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthPlanDetailedPerDiemView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameMnthPlanDetailedPerDiemView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new JFrameMnthPlanDetailedPerDiemView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelDistrict;
    private javax.swing.JLabel jLabelEmp;
    private javax.swing.JLabel jLabelGenLogNam1;
    private javax.swing.JLabel jLabelGenLogNam3;
    private javax.swing.JLabel jLabelGenLogNam5;
    private javax.swing.JLabel jLabelGenLogNam6;
    private javax.swing.JLabel jLabelGenLogNam7;
    private javax.swing.JLabel jLabelHeaderGen4;
    private javax.swing.JLabel jLabelHeaderGen5;
    private javax.swing.JLabel jLabelHeaderGen6;
    private javax.swing.JLabel jLabelHeaderGen7;
    private javax.swing.JLabel jLabelHeaderGen8;
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
    private javax.swing.JLabel jLabelOffice1;
    private javax.swing.JLabel jLabelProvince;
    private javax.swing.JLabel jLabelProvince1;
    private javax.swing.JLabel jLabelRef;
    private javax.swing.JLabel jLabelRefNum;
    private javax.swing.JLabel jLabelSerial;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JLabel jLabelStatus1;
    private javax.swing.JLabel jLabelStatus2;
    private javax.swing.JLabel jLabelStatus3;
    private javax.swing.JLabel jLabelStatus4;
    private javax.swing.JLabel jLabelWk1DateFrom;
    private javax.swing.JLabel jLabelWk1DateTo;
    private javax.swing.JLabel jLabelWk1From;
    private javax.swing.JLabel jLabelWk1From1;
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
    private javax.swing.JMenuItem jMenuItemUserCreate;
    private javax.swing.JMenuItem jMenuItemUserProfUpd;
    private javax.swing.JMenuItem jMenuItemView;
    private javax.swing.JMenuItem jMenuMonPlanEdit;
    private javax.swing.JMenu jMenuMonthlyPlan;
    private javax.swing.JMenu jMenuNew;
    private javax.swing.JMenu jMenuPlanApproval;
    private javax.swing.JMenu jMenuReports;
    private javax.swing.JMenu jMenuRequest;
    private javax.swing.JPanel jPanelWkFive;
    private javax.swing.JPanel jPanelWkFour;
    private javax.swing.JPanel jPanelWkOne;
    private javax.swing.JPanel jPanelWkThree;
    private javax.swing.JPanel jPanelWkTwo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JPopupMenu.Separator jSeparator13;
    private javax.swing.JPopupMenu.Separator jSeparator16;
    private javax.swing.JPopupMenu.Separator jSeparator18;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator22;
    private javax.swing.JPopupMenu.Separator jSeparator23;
    private javax.swing.JPopupMenu.Separator jSeparator24;
    private javax.swing.JPopupMenu.Separator jSeparator26;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator33;
    private javax.swing.JPopupMenu.Separator jSeparator34;
    private javax.swing.JSeparator jSeparator35;
    private javax.swing.JSeparator jSeparator36;
    private javax.swing.JSeparator jSeparator37;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator43;
    private javax.swing.JPopupMenu.Separator jSeparator44;
    private javax.swing.JPopupMenu.Separator jSeparator45;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator62;
    private javax.swing.JPopupMenu.Separator jSeparator63;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JTabbedPane jTabbedPaneMain;
    private javax.swing.JTable jTableWk1Activities;
    private javax.swing.JTable jTableWk2Activities;
    private javax.swing.JTable jTableWk3Activities;
    private javax.swing.JTable jTableWk4Activities;
    private javax.swing.JTable jTableWk5Activities;
    // End of variables declaration//GEN-END:variables
}
