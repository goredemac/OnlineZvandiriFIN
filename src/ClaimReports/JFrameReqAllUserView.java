/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClaimReports;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import ClaimAppendix2.*;
import ClaimReports.*;
import java.awt.Image;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.ImageIcon;
import ClaimAppendix1.*;
import javax.swing.WindowConstants;
import ClaimPlan.*;



/**
 *
 * @author cgoredema
 */
public class JFrameReqAllUserView extends javax.swing.JFrame {

    PreparedStatement pst = null;
    PreparedStatement pst1 = null;
    int itmNum = 1;
    int month, finyear;
    int initdocVersion = 0;
    int initactVersion = 0;
    int checkRefCount = 0;
    Date curYear = new Date();
    String sendToFin, createUsrNam, supUsrMail, reqUsrMail, reqFinOffMail, reqFinOffNam,
            actVerNum, docVersion, oldDocVersion,
            statusCodeApp, statusCodeDisApp, oldComments, checkRef,
            authNam1, authNam2, usrGrp;
    String province = "";
    String checker = "";
    String mailUsrNam = "finance@ophid.co.zw";
    String mailUsrPass = "MgqM5utyUr43x#";
    String ipAdd =    "localhost:14575"       ;
    String usrName = "appfin";
    String usrPass = "542ytDYvynv$TVYb";
    SimpleDateFormat df = new SimpleDateFormat("yyyy");
    DefaultTableModel model;
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

    /**
     * Creates new form JFrameTabApp1
     */
    public JFrameReqAllUserView() {
        initComponents();
        showDate();
        showTime();
//        findProvince();
        model = (DefaultTableModel) jTableActivityReqVerify.getModel();
        fetchdata();
        mainPageTotUpdate();
        jLabelRegYear.setVisible(false);

    }

    public JFrameReqAllUserView(String yearRef) {
        initComponents();
        showDate();
        showTime();
//        findProvince();
        model = (DefaultTableModel) jTableActivityReqVerify.getModel();
        fetchdata();
        mainPageTotUpdate();
        jLabelRegYear.setVisible(false);
        jLabelStatusView.setText("");

    }

    public JFrameReqAllUserView(String yearRef, String usrLogNum) {

        initComponents();
        showDate();
        showTime();
        computerName();
        // jLabelComments.setVisible(false);
        jTextAreaComments.setWrapStyleWord(true);
        jTextAreaComments.setLineWrap(true);
        jLabelAuthoriser.setVisible(false);
        jLabelAuthNam1.setVisible(false);
        jLabelAuthNam2.setVisible(false);

        model = (DefaultTableModel) jTableActivityReqVerify.getModel();
        jLabelRegNum.setText(yearRef);
        jLabelRegYear.setVisible(false);
        jLabelEmp.setVisible(false);
 jMenuItemRequest.setEnabled(false);
        jMenuItemSpecialAcquittal.setEnabled(false);
        jLabelEmp.setText(usrLogNum);
        checkRef = yearRef;

        findUser();
        findUserGrp();
        findAuthorisation();
        fetchdata();
        mainPageTotUpdate();
        findApplicantUser();
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

    void showDate() {
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        jLabelGenDate.setText(s.format(d));
        jLabelLineDate.setText(s.format(d));

    }

    void findUserGrp() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT *  FROM [ClaimsAppSysZvandiri].[dbo].[EmpLogGrpTab]"
                    + "where emp_num ='" + jLabelEmp.getText() + "'");

            while (r.next()) {

                usrGrp = r.getString(3);

            }

            if ("usrGenSp".equals(usrGrp)) {

                jMenuItemSupApp.setEnabled(false);
                jMenuItemAccMgrRev.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuPlanApproval.setEnabled(false);
                jMenuItemSchGen.setEnabled(false);
                jMenuItemAcqSupApp.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuItemAcqFinApp.setEnabled(false);
                jMenuItemAcqSchGen.setEnabled(false);
                jMenuAgeAlluser.setEnabled(false);
                jMenuAgeByUser.setEnabled(false);

            }

            if ("usrGen".equals(usrGrp)) {

                jMenuItemSupApp.setEnabled(false);
                jMenuItemAccMgrRev.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuPlanApproval.setEnabled(false);
                jMenuItemSchGen.setEnabled(false);
                jMenuItemAcqSupApp.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuItemAcqFinApp.setEnabled(false);
                jMenuItemAcqSchGen.setEnabled(false);
                jMenuAgeAlluser.setEnabled(false);
                jMenuAgeByUser.setEnabled(false);
                jMenuItemSpecialAcquittal.setEnabled(false);
                jMenuProcessTimeline.setEnabled(false);
                jMenuMonPlanEdit.setEnabled(false);
                jMenuItem3WkPlan.setEnabled(false);
               
                jMenuItemPlanView.setEnabled(false);
            }

            if ("usrGenPlan".equals(usrGrp)) {

                jMenuItemSupApp.setEnabled(false);
                jMenuItemAccMgrRev.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuPlanApproval.setEnabled(false);
                jMenuItemSchGen.setEnabled(false);
                jMenuItemAcqSupApp.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuItemAcqFinApp.setEnabled(false);
                jMenuItemAcqSchGen.setEnabled(false);
                jMenuAgeAlluser.setEnabled(false);
                jMenuAgeByUser.setEnabled(false);
                jMenuItemSpecialAcquittal.setEnabled(false);
                jMenuProcessTimeline.setEnabled(false);
               
                jMenuItemPlanView.setEnabled(false);
            }

            if ("usrSup".equals(usrGrp)) {

                jMenuItemAccMgrRev.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuPlanApproval.setEnabled(false);
                jMenuItemSchGen.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuItemAcqFinApp.setEnabled(false);
                jMenuItemAcqSchGen.setEnabled(false);
            }

            if ("usrProvMgr".equals(usrGrp)) {

                jMenuItemAccMgrRev.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuPlanApproval.setEnabled(false);
                jMenuItemSchGen.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuItemAcqFinApp.setEnabled(false);
                jMenuItemAcqSchGen.setEnabled(false);

            }

            if ("usrSupAcc".equals(usrGrp)) {

                jMenuItemAccMgrRev.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
               

            }

            if ("usrAccMgr".equals(usrGrp)) {

                jMenuItemSupApp.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuPlanApproval.setEnabled(false);
                jMenuItemSchGen.setEnabled(false);
                jMenuItemAcqSupApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuItemAcqFinApp.setEnabled(false);
                jMenuItemAcqSchGen.setEnabled(false);
               

            }

            if ("usrHead".equals(usrGrp)) {

                jMenuItemAccMgrRev.setEnabled(false);
                jMenuPlanApproval.setEnabled(false);
                jMenuItemSchGen.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqFinApp.setEnabled(false);
                jMenuItemAcqSchGen.setEnabled(false);
            }

            if ("usrFinReq".equals(usrGrp)) {

                jMenuItemSupApp.setEnabled(false);
                jMenuItemAccMgrRev.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuItemAcqSupApp.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuMonPlanEdit.setEnabled(false);
                jMenuItem3WkPlan.setEnabled(false);
               
                jMenuItemPlanView.setEnabled(false);

            }

            if ("usrFinAcq".equals(usrGrp)) {

                jMenuItemSupApp.setEnabled(false);
                jMenuItemAccMgrRev.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuPlanApproval.setEnabled(false);
                jMenuItemSchGen.setEnabled(false);
                jMenuItemAcqSupApp.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuMonPlanEdit.setEnabled(false);
                jMenuItem3WkPlan.setEnabled(false);
               
                jMenuItemPlanView.setEnabled(false);

            }

            if ("usrAdm".equals(usrGrp)) {

                jMenuItemSupApp.setEnabled(false);
                jMenuItemAccMgrRev.setEnabled(false);
                jMenuItemHeadApp.setEnabled(false);
                jMenuPlanApproval.setEnabled(false);
                jMenuItemSchGen.setEnabled(false);
                jMenuItemAcqSupApp.setEnabled(false);
                jMenuItemAcqAccApp.setEnabled(false);
                jMenuItemAcqHeadApp.setEnabled(false);
                jMenuItemAcqFinApp.setEnabled(false);
                jMenuItemAcqSchGen.setEnabled(false);

            }
        } catch (Exception e) {
            //  JOptionPane.showMessageDialog(null, "Failed to Connect to Database (Province).Try Again", "Error Connection", JOptionPane.WARNING_MESSAGE);
            System.out.println(e);
        }
    }

    void findApplicantUser() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select * from [ClaimsAppSysZvandiri].[dbo]."
                    + "[EmpDetTab] where emp_num ='" + jLabelReqEmpNum.getText() + "'");

            while (r.next()) {

                reqUsrMail = r.getString(4);
                supUsrMail = r.getString(6);
                reqFinOffMail = r.getString(21);
                reqFinOffNam = r.getString(20);

            }

            //                 conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Connect to Database (Province)."
                    + "Try Again", "Error Connection", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
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
                jLabelLinLogNam.setText(r.getString(2));
                sendToFin = r.getString(11);
                createUsrNam = r.getString(2);

            }

            //                 conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Connect to Database (Province)."
                    + "Try Again", "Error Connection", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
    }

    void findAuthorisation() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT count(*),AUTH_NAM,AUTH_ALL_NAM FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] where (AUTH_NAM != ' '  "
                    + "or AUTH_ALL_NAM != ' ') and  concat(YEAR,SERIAL,REF_NUM) ='" + checkRef + "' "
                    + "group by AUTH_NAM,AUTH_ALL_NAM");

            while (r.next()) {

                checkRefCount = r.getInt(1);
                authNam1 = r.getString(2);
                authNam2 = r.getString(3);

            }

            if (checkRefCount > 0) {

                jLabelAuthoriser.setVisible(true);
                jLabelAuthNam1.setVisible(true);
                jLabelAuthNam2.setVisible(true);

                if (authNam1 == null) {
                    jLabelAuthNam1.setText(authNam2);

                    JOptionPane.showMessageDialog(null, "<html><font color=\"red\"  size=\"3\">Perdiem request <b><font color=\"green\">" + checkRef
                            + "</font> </b>has authorisation from <b><font color=\"green\"> " + authNam2 + "</font> for allowances were travel is less than 100km.</font></html>");

                } else if (authNam2 == null) {
                    jLabelAuthNam1.setText(authNam1);

                    JOptionPane.showMessageDialog(null, "<html><font color=\"red\"  size=\"3\">Perdiem request <b><font color=\"green\">" + checkRef
                            + "</font> </b>has authorisation from <b><font color=\"green\"> " + authNam1 + "</font> for allowances were travel is less than 100km.</font></html>");

                } else {
                    jLabelAuthNam1.setText(authNam1);
                    jLabelAuthNam2.setText(authNam2);

                    JOptionPane.showMessageDialog(null, "<html><font color=\"red\"  size=\"3\">Perdiem request <b><font color=\"green\">" + checkRef
                            + "</font> </b>has authorisation from <b><font color=\"green\"> " + authNam1 + " and " + authNam2 + " </font> for allowances were travel is less than 100km.</font></html>");
                }
            }
            //                 conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Connect to Database (Province)."
                    + "Try Again", "Error Connection", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
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

    void imgDisplay() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT SERIAL,REF_NUM,ATT_IMAGE,IMG_VERSION "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[DocAttachTab] where concat(SERIAL,REF_NUM)="
                    + "'" + checkRef + "'"
                    + " and IMG_VERSION = (SELECT MAX(IMG_VERSION) FROM"
                    + "[ClaimsAppSysZvandiri].[dbo].[DocAttachTab] WHERE concat(SERIAL,REF_NUM)="
                    + "'" + checkRef + "')");

            while (r.next()) {

                byte[] img = r.getBytes("ATT_IMAGE");

                ImageIcon imageIcon = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(600, 535, Image.SCALE_SMOOTH));
                //  jLabelImg.setIcon(imageIcon);

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
        for (int i = 0; i < jTableActivityReqVerify.getRowCount(); i++) {
            double breakfastamount = Double.parseDouble((String) jTableActivityReqVerify.getValueAt(i, 4));
            breakfastsubtotal += breakfastamount;

        }
        jLabelBreakFastSubTot.setText(Double.toString(breakfastsubtotal));

        lunchsubtotal = 0;
        for (int i = 0; i < jTableActivityReqVerify.getRowCount(); i++) {
            double lunchamount = Double.parseDouble((String) jTableActivityReqVerify.getValueAt(i, 5));
            lunchsubtotal += lunchamount;
        }
        jLabelLunchSubTot.setText(Double.toString(lunchsubtotal));

        dinnersubtotal = 0;
        for (int i = 0; i < jTableActivityReqVerify.getRowCount(); i++) {
            double dinneramount = Double.parseDouble((String) jTableActivityReqVerify.getValueAt(i, 6));
            dinnersubtotal += dinneramount;
        }
        jLabelDinnerSubTot.setText(Double.toString(dinnersubtotal));

        incidentalsubtotal = 0;
        for (int i = 0; i < jTableActivityReqVerify.getRowCount(); i++) {
            double incidentalamount = Double.parseDouble((String) jTableActivityReqVerify.getValueAt(i, 7));
            incidentalsubtotal += incidentalamount;
        }
        jLabelIncidentalSubTot.setText(Double.toString(incidentalsubtotal));

        miscSubTot = 0;
        for (int i = 0; i < jTableActivityReqVerify.getRowCount(); i++) {
            double miscamount = Double.parseDouble((String) jTableActivityReqVerify.getValueAt(i, 9));
            miscSubTot += miscamount;
        }
        jLabelMiscSubTot.setText(Double.toString(miscSubTot));

        provedSubTot = 0;
        for (int i = 0; i < jTableActivityReqVerify.getRowCount(); i++) {
            double provedamount = Double.parseDouble((String) jTableActivityReqVerify.getValueAt(i, 10));
            provedSubTot += provedamount;
        }
        jLabelAccProvedSubTot.setText(Double.toString(provedSubTot));

        unprovedSubTot = 0;
        for (int i = 0; i < jTableActivityReqVerify.getRowCount(); i++) {
            double unprovedamount = Double.parseDouble((String) jTableActivityReqVerify.getValueAt(i, 11));
            unprovedSubTot += unprovedamount;
        }
        jLabelAccUnprovedSubTot.setText(Double.toString(unprovedSubTot));

        allTotal = 0;
        allTotal = unprovedSubTot + provedSubTot + miscSubTot + incidentalsubtotal
                + dinnersubtotal + lunchsubtotal + breakfastsubtotal;
        jLabelAppTotReqCost.setText(Double.toString(allTotal));
    }

    void fetchdata() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();
            st.executeQuery("SELECT ACT_DATE,ACT_DEST,"
                    + "ACT_DIST,ACT_ITM_PUR,BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,"
                    + "MSC_AMT,ACC_PRO_AMT,ACC_UNPROV_AMT,ACT_ITM_TOT FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                    + "  where SERIAL = 'R' and concat(SERIAL,REF_NUM)='" + checkRef + "'and "
                    + "DOC_VER =(select max(DOC_VER) from ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab "
                    + "where concat(SERIAL,REF_NUM)='" + checkRef + "')"
                    + "order by ACT_DATE");
            // + "  where REF_NUM='38'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                model.insertRow(model.getRowCount(), new Object[]{r.getString(1), r.getString(2), r.getString(3),
                    r.getString(4), r.getString(5), r.getString(6), r.getString(7), r.getString(8), r.getString(9),
                    r.getString(10), r.getString(11), r.getString(12), r.getString(13)});

            }

            st1.executeQuery("SELECT REF_YEAR,SERIAL,REF_NUM,REF_DAT,EMP_NUM,EMP_NAM,EMP_TTL,"
                    + "EMP_PROV,EMP_OFF,EMP_BNK_NAM,ACC_NUM,BUD_COD,ACT_MAIN_PUR,"
                    + "ACT_TOT_AMT,ACT_REC_STA,DOC_VER,DOC_VER +1   "
                    + "FROM ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab where "
                    + "concat(SERIAL,REF_NUM)='" + checkRef + "' and "
                    + "DOC_VER =(select max(DOC_VER) from ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab "
                    + "where concat(SERIAL,REF_NUM)='" + checkRef + "') ");
            //  + "  where REF_NUM='38'");
            ResultSet r1 = st1.getResultSet();

            while (r1.next()) {
                jLabelRegYear.setText(r1.getString(1));
                jLabelSerial.setText(r1.getString(2));
                jLabelLinSerial.setText(r1.getString(2));
                jLabelRegNum.setText(r1.getString(3));
                jLabelLinRegNum.setText(r1.getString(3));
                jLabelReqEmpNum.setText(r1.getString(5));
                jLabelEmpNam.setText(r1.getString(6));
                jLabelEmpTitle.setText(r1.getString(7));
                jLabelReqProvince.setText(r1.getString(8));
                jLabelReqOffice.setText(r1.getString(9));
                jLabelBankName.setText(r1.getString(10));
                jLabelReqAccNum.setText(r1.getString(11));
                jLabelReqBudCode.setText(r1.getString(12));
                jLabelActMainPurpose.setText(r1.getString(13));
                jLabelAppTotReqCost.setText(r1.getString(14));
                jLabelRegDate.setText(r1.getString(4));
                docVersion = r1.getString(17);
                oldDocVersion = r1.getString(16);

            }

            st2.executeQuery("SELECT COMMENTS,USR_ACTION  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] where "
                    + "concat(SERIAL,REF_NUM)='" + checkRef + "' and "
                    + "DOC_VER =(select max(DOC_VER) from ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab "
                    + "where concat(SERIAL,REF_NUM)='" + checkRef + "') ");

            ResultSet r2 = st2.getResultSet();

            while (r2.next()) {
                jTextAreaComments.setText(r2.getString(1));
                oldComments = r2.getString(1);
                jLabelStatusView.setText(r2.getString(2));
                if ("Request - Head Approved".equals(jLabelStatusView.getText())) {
                    jLabelStatusView.setVisible(true);
                    jPanelStatusView.setVisible(true);
                    jLabelStatusView.setForeground(new java.awt.Color(0, 153, 0));
                    jPanelStatusView.setVisible(true);
                } else {
                    jLabelStatusView.setVisible(true);
                    jPanelStatusView.setVisible(true);
                    jLabelStatusView.setForeground(new java.awt.Color(255, 0, 0));
                    jPanelStatusView.setVisible(true);
                }
            }
            imgDisplay();
        } catch (Exception e) {

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

        jTabbedPaneAppSys = new javax.swing.JTabbedPane();
        jPanelGen = new javax.swing.JPanel();
        jLabelLogo = new javax.swing.JLabel();
        jLabelHeaderGen = new javax.swing.JLabel();
        jLabelGenDate = new javax.swing.JLabel();
        jLabelGenTime = new javax.swing.JLabel();
        jLabelGenLogNam1 = new javax.swing.JLabel();
        jLabelGenLogNam = new javax.swing.JLabel();
        jLabelEmpNum = new javax.swing.JLabel();
        jLabelEmpNam = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabelSepDetails = new javax.swing.JLabel();
        jLabelProvince = new javax.swing.JLabel();
        jLabelBank = new javax.swing.JLabel();
        jLabelBankName = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabelEmpTitle = new javax.swing.JLabel();
        jLabelFinDetails = new javax.swing.JLabel();
        jLabelBudgetCode = new javax.swing.JLabel();
        jLabelMainPurpose = new javax.swing.JLabel();
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
        jLabelLunchSubTot = new javax.swing.JLabel();
        jLabelDinnerSubTot = new javax.swing.JLabel();
        jLabelBreakFastSubTot = new javax.swing.JLabel();
        jLabelAllReq = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabelMiscSubTot = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabelMscSub = new javax.swing.JLabel();
        jLabelMiscReq = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabelAccUnprovedSubTot = new javax.swing.JLabel();
        jLabelAccProvedSub = new javax.swing.JLabel();
        jLabelAccProvedSubTot = new javax.swing.JLabel();
        jLabelAccUnprovedSub = new javax.swing.JLabel();
        jLabelAccReq = new javax.swing.JLabel();
        jLabelAppTotReqCost = new javax.swing.JLabel();
        jLabelAppTotReq = new javax.swing.JLabel();
        jLabelOffice = new javax.swing.JLabel();
        jLabelReqAccNum = new javax.swing.JLabel();
        jLabelACC = new javax.swing.JLabel();
        jLabelNum = new javax.swing.JLabel();
        jLabelActMainPurpose = new javax.swing.JLabel();
        jLabelReqProvince = new javax.swing.JLabel();
        jLabelReqOffice = new javax.swing.JLabel();
        jLabelReqBudCode = new javax.swing.JLabel();
        jLabelReqEmpNum = new javax.swing.JLabel();
        jLabelRegDate = new javax.swing.JLabel();
        jLabelRegNum = new javax.swing.JLabel();
        jLabelRegYear = new javax.swing.JLabel();
        jLabelSerial = new javax.swing.JLabel();
        jLabelEmp = new javax.swing.JLabel();
        jLabelComments = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaComments = new javax.swing.JTextArea();
        jLabelAuthoriser = new javax.swing.JLabel();
        jLabelAuthNam1 = new javax.swing.JLabel();
        jLabelAuthNam2 = new javax.swing.JLabel();
        jPanelStatusView = new javax.swing.JPanel();
        jLabelStatusView = new javax.swing.JLabel();
        jPanelRequest = new javax.swing.JPanel();
        jLabelLogo1 = new javax.swing.JLabel();
        jLabelHeaderLine = new javax.swing.JLabel();
        jLabelLineDate = new javax.swing.JLabel();
        jLabelLineTime = new javax.swing.JLabel();
        jLabellogged = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableActivityReqVerify = new javax.swing.JTable();
        jLabelNum1 = new javax.swing.JLabel();
        jLabelLinSerial = new javax.swing.JLabel();
        jLabelLinRegNum = new javax.swing.JLabel();
        jLabelLinLogNam = new javax.swing.JLabel();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuNew = new javax.swing.JMenu();
        jMenuItemRequest = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcquittal = new javax.swing.JMenuItem();
        jSeparator14 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSpecialAcquittal = new javax.swing.JMenuItem();
        jSeparator21 = new javax.swing.JPopupMenu.Separator();
        jMenuItemRequestMOH = new javax.swing.JMenuItem();
        jSeparator28 = new javax.swing.JPopupMenu.Separator();
        jMenuItemMeetAcquittal = new javax.swing.JMenuItem();
        jSeparator31 = new javax.swing.JPopupMenu.Separator();
        jMenuItem3WkPlan = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        jMenuEdit = new javax.swing.JMenu();
        jMenuReqEdit = new javax.swing.JMenuItem();
        jSeparator29 = new javax.swing.JPopupMenu.Separator();
        jMenuAcqEdit = new javax.swing.JMenuItem();
        jSeparator30 = new javax.swing.JPopupMenu.Separator();
        jMenuMonPlanEdit = new javax.swing.JMenuItem();
        jSeparator15 = new javax.swing.JPopupMenu.Separator();
        jMenuItemClose = new javax.swing.JMenuItem();
        jSeparator16 = new javax.swing.JPopupMenu.Separator();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuRequest = new javax.swing.JMenu();
        jMenuItemSupApp = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAccMgrRev = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        jMenuItemHeadApp = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        jMenuPlanApproval = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSchGen = new javax.swing.JMenuItem();
        jSeparator13 = new javax.swing.JPopupMenu.Separator();
        jMenuItemView = new javax.swing.JMenuItem();
        jMenuAcquittal = new javax.swing.JMenu();
        jMenuItemAcqSupApp = new javax.swing.JMenuItem();
        jSeparator11 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqAccApp = new javax.swing.JMenuItem();
        jSeparator12 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqHeadApp = new javax.swing.JMenuItem();
        jSeparator17 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqFinApp = new javax.swing.JMenuItem();
        jSeparator18 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqSchGen = new javax.swing.JMenuItem();
        jSeparator19 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqView = new javax.swing.JMenuItem();
        jMenuMonthlyPlan = new javax.swing.JMenu();
        jMenuItemProvMgrApproval = new javax.swing.JMenuItem();
        jSeparator26 = new javax.swing.JPopupMenu.Separator();
        jMenuItemReqGenLst = new javax.swing.JMenuItem();
        jSeparator34 = new javax.swing.JPopupMenu.Separator();
        jMenuItemMeetGenLst = new javax.swing.JMenuItem();
        jSeparator41 = new javax.swing.JPopupMenu.Separator();
        jMenuItemPlanView = new javax.swing.JMenuItem();
        jMenuReports = new javax.swing.JMenu();
        jMenuItemReqSubmitted = new javax.swing.JMenuItem();
        jSeparator23 = new javax.swing.JPopupMenu.Separator();
        jMenuAgeAlluser = new javax.swing.JMenuItem();
        jSeparator20 = new javax.swing.JPopupMenu.Separator();
        jMenuAgeByUser = new javax.swing.JMenuItem();
        jSeparator24 = new javax.swing.JPopupMenu.Separator();
        jMenuProcessTimeline = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("USER PERDIEM REQUEST DETAILS");
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        jTabbedPaneAppSys.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        jPanelGen.setBackground(new java.awt.Color(0, 102, 102));
        jPanelGen.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jPanelGen.setPreferredSize(new java.awt.Dimension(1280, 680));
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

        jLabelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ophidlog.jpg"))); // NOI18N
        jPanelGen.add(jLabelLogo);
        jLabelLogo.setBounds(10, 10, 220, 100);

        jLabelHeaderGen.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelGen.add(jLabelHeaderGen);
        jLabelHeaderGen.setBounds(350, 40, 610, 40);

        jLabelGenDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelGen.add(jLabelGenDate);
        jLabelGenDate.setBounds(1070, 0, 110, 30);

        jLabelGenTime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelGen.add(jLabelGenTime);
        jLabelGenTime.setBounds(1240, 0, 80, 30);

        jLabelGenLogNam1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam1.setText("Logged In");
        jPanelGen.add(jLabelGenLogNam1);
        jLabelGenLogNam1.setBounds(1070, 40, 100, 30);

        jLabelGenLogNam.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelGenLogNam);
        jLabelGenLogNam.setBounds(1150, 40, 200, 30);

        jLabelEmpNum.setText("Employee Number");
        jPanelGen.add(jLabelEmpNum);
        jLabelEmpNum.setBounds(20, 200, 110, 30);

        jLabelEmpNam.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelEmpNam);
        jLabelEmpNam.setBounds(370, 200, 340, 30);
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

        jLabelBankName.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelBankName);
        jLabelBankName.setBounds(130, 260, 220, 30);
        jPanelGen.add(jSeparator2);
        jSeparator2.setBounds(10, 340, 1340, 2);

        jLabelEmpTitle.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelEmpTitle);
        jLabelEmpTitle.setBounds(750, 200, 400, 30);

        jLabelFinDetails.setText("Financial Details");
        jPanelGen.add(jLabelFinDetails);
        jLabelFinDetails.setBounds(20, 310, 110, 30);

        jLabelBudgetCode.setText("Budget Code");
        jPanelGen.add(jLabelBudgetCode);
        jLabelBudgetCode.setBounds(20, 350, 80, 30);

        jLabelMainPurpose.setText("Activity Main Purpose");
        jPanelGen.add(jLabelMainPurpose);
        jLabelMainPurpose.setBounds(20, 420, 130, 30);

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

        jLabelLunchSubTot.setText("0.00");
        jPanel1.add(jLabelLunchSubTot);
        jLabelLunchSubTot.setBounds(100, 60, 50, 25);

        jLabelDinnerSubTot.setText("0.00");
        jPanel1.add(jLabelDinnerSubTot);
        jLabelDinnerSubTot.setBounds(100, 90, 50, 25);

        jLabelBreakFastSubTot.setText("0.00");
        jPanel1.add(jLabelBreakFastSubTot);
        jLabelBreakFastSubTot.setBounds(100, 30, 50, 25);

        jLabelAllReq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAllReq.setForeground(new java.awt.Color(0, 102, 0));
        jLabelAllReq.setText("Req");
        jPanel1.add(jLabelAllReq);
        jLabelAllReq.setBounds(100, 5, 22, 25);

        jPanelGen.add(jPanel1);
        jPanel1.setBounds(10, 470, 250, 150);

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

        jLabelMiscReq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelMiscReq.setForeground(new java.awt.Color(0, 102, 0));
        jLabelMiscReq.setText("Req");
        jPanel3.add(jLabelMiscReq);
        jLabelMiscReq.setBounds(110, 5, 22, 25);

        jPanelGen.add(jPanel3);
        jPanel3.setBounds(350, 470, 250, 150);

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

        jLabelAccReq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAccReq.setForeground(new java.awt.Color(0, 102, 0));
        jLabelAccReq.setText("Req");
        jPanel4.add(jLabelAccReq);
        jLabelAccReq.setBounds(110, 5, 30, 25);

        jPanelGen.add(jPanel4);
        jPanel4.setBounds(690, 470, 250, 150);

        jLabelAppTotReqCost.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelAppTotReqCost.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAppTotReqCost.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelAppTotReqCost.setText("0.00");
        jPanelGen.add(jLabelAppTotReqCost);
        jLabelAppTotReqCost.setBounds(1260, 600, 70, 30);

        jLabelAppTotReq.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelAppTotReq.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAppTotReq.setText("Total ");
        jPanelGen.add(jLabelAppTotReq);
        jLabelAppTotReq.setBounds(1080, 600, 140, 30);

        jLabelOffice.setText("Office");
        jPanelGen.add(jLabelOffice);
        jLabelOffice.setBounds(550, 230, 70, 30);

        jLabelReqAccNum.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelReqAccNum);
        jLabelReqAccNum.setBounds(680, 260, 130, 30);

        jLabelACC.setText("Account No.");
        jPanelGen.add(jLabelACC);
        jLabelACC.setBounds(550, 260, 80, 30);

        jLabelNum.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNum.setText("Reference No.");
        jPanelGen.add(jLabelNum);
        jLabelNum.setBounds(20, 130, 100, 30);

        jLabelActMainPurpose.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelActMainPurpose);
        jLabelActMainPurpose.setBounds(160, 420, 750, 30);

        jLabelReqProvince.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelReqProvince);
        jLabelReqProvince.setBounds(130, 230, 210, 30);

        jLabelReqOffice.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelReqOffice);
        jLabelReqOffice.setBounds(680, 230, 210, 30);

        jLabelReqBudCode.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelReqBudCode);
        jLabelReqBudCode.setBounds(160, 350, 750, 30);

        jLabelReqEmpNum.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelReqEmpNum);
        jLabelReqEmpNum.setBounds(130, 200, 60, 30);

        jLabelRegDate.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelRegDate);
        jLabelRegDate.setBounds(240, 130, 160, 30);

        jLabelRegNum.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelRegNum);
        jLabelRegNum.setBounds(150, 130, 70, 30);
        jPanelGen.add(jLabelRegYear);
        jLabelRegYear.setBounds(700, 130, 50, 30);

        jLabelSerial.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelSerial);
        jLabelSerial.setBounds(120, 130, 20, 30);
        jPanelGen.add(jLabelEmp);
        jLabelEmp.setBounds(1110, 70, 50, 20);

        jLabelComments.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelComments.setText("Comments");
        jPanelGen.add(jLabelComments);
        jLabelComments.setBounds(1080, 470, 170, 20);

        jTextAreaComments.setColumns(20);
        jTextAreaComments.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jTextAreaComments.setRows(5);
        jScrollPane2.setViewportView(jTextAreaComments);

        jPanelGen.add(jScrollPane2);
        jScrollPane2.setBounds(1070, 490, 260, 110);

        jLabelAuthoriser.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAuthoriser.setText("Authoriser/s");
        jPanelGen.add(jLabelAuthoriser);
        jLabelAuthoriser.setBounds(1080, 350, 160, 25);

        jLabelAuthNam1.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAuthNam1);
        jLabelAuthNam1.setBounds(1080, 370, 270, 25);

        jLabelAuthNam2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAuthNam2);
        jLabelAuthNam2.setBounds(1080, 400, 230, 25);

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

        jTabbedPaneAppSys.addTab("User and Accounting Details", jPanelGen);

        jPanelRequest.setBackground(new java.awt.Color(0, 102, 102));
        jPanelRequest.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jPanelRequest.setPreferredSize(new java.awt.Dimension(1280, 677));
        jPanelRequest.setLayout(null);

        jLabelLogo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ophidlog.jpg"))); // NOI18N
        jPanelRequest.add(jLabelLogo1);
        jLabelLogo1.setBounds(10, 10, 220, 100);

        jLabelHeaderLine.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelRequest.add(jLabelHeaderLine);
        jLabelHeaderLine.setBounds(350, 40, 610, 40);

        jLabelLineDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelRequest.add(jLabelLineDate);
        jLabelLineDate.setBounds(1070, 0, 110, 30);

        jLabelLineTime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelRequest.add(jLabelLineTime);
        jLabelLineTime.setBounds(1240, 0, 80, 30);

        jLabellogged.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged.setText("Logged In");
        jPanelRequest.add(jLabellogged);
        jLabellogged.setBounds(1070, 40, 80, 30);

        jTableActivityReqVerify.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Destination", "Distance", "Purpose", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Activity", "Misc Amt", "Proved", "Unproved", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableActivityReqVerify.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableActivityReqVerify.setGridColor(new java.awt.Color(255, 255, 255));
        jTableActivityReqVerify.setMinimumSize(new java.awt.Dimension(180, 64));
        jTableActivityReqVerify.setRowHeight(30);
        jTableActivityReqVerify.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTableActivityReqVerifyFocusLost(evt);
            }
        });
        jTableActivityReqVerify.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableActivityReqVerifyMouseClicked(evt);
            }
        });
        jTableActivityReqVerify.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTableActivityReqVerifyKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTableActivityReqVerifyKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(jTableActivityReqVerify);

        jPanelRequest.add(jScrollPane1);
        jScrollPane1.setBounds(0, 115, 1355, 530);

        jLabelNum1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNum1.setText("Reference No.");
        jPanelRequest.add(jLabelNum1);
        jLabelNum1.setBounds(1070, 70, 90, 30);

        jLabelLinSerial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinSerial.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequest.add(jLabelLinSerial);
        jLabelLinSerial.setBounds(1220, 70, 20, 30);

        jLabelLinRegNum.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinRegNum.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequest.add(jLabelLinRegNum);
        jLabelLinRegNum.setBounds(1240, 70, 70, 30);

        jLabelLinLogNam.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinLogNam.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequest.add(jLabelLinLogNam);
        jLabelLinLogNam.setBounds(1160, 40, 200, 30);

        jTabbedPaneAppSys.addTab("Perdiem Request", jPanelRequest);

        jMenuFile.setText("File");
        jMenuFile.setPreferredSize(new java.awt.Dimension(60, 19));
        jMenuFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuFileActionPerformed(evt);
            }
        });

        jMenuNew.setText("New");

        jMenuItemRequest.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemRequest.setText("Perdiem Request");
        jMenuItemRequest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRequestActionPerformed(evt);
            }
        });
        jMenuNew.add(jMenuItemRequest);
        jMenuNew.add(jSeparator3);

        jMenuItemAcquittal.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemAcquittal.setText("Perdiem Acquittal");
        jMenuItemAcquittal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcquittalActionPerformed(evt);
            }
        });
        jMenuNew.add(jMenuItemAcquittal);
        jMenuNew.add(jSeparator14);

        jMenuItemSpecialAcquittal.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSpecialAcquittal.setText("Perdiem Acquittal - Special");
        jMenuItemSpecialAcquittal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSpecialAcquittalActionPerformed(evt);
            }
        });
        jMenuNew.add(jMenuItemSpecialAcquittal);
        jMenuNew.add(jSeparator21);

        jMenuItemRequestMOH.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemRequestMOH.setText("Acquittal - MOHCC Personnel");
        jMenuItemRequestMOH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRequestMOHActionPerformed(evt);
            }
        });
        jMenuNew.add(jMenuItemRequestMOH);
        jMenuNew.add(jSeparator28);

        jMenuItemMeetAcquittal.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemMeetAcquittal.setText("Acquittal - Participants Allowances");
        jMenuItemMeetAcquittal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemMeetAcquittalActionPerformed(evt);
            }
        });
        jMenuNew.add(jMenuItemMeetAcquittal);
        jMenuNew.add(jSeparator31);

        jMenuItem3WkPlan.setText("Monthly Plan");
        jMenuItem3WkPlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3WkPlanActionPerformed(evt);
            }
        });
        jMenuNew.add(jMenuItem3WkPlan);

        jMenuFile.add(jMenuNew);
        jMenuFile.add(jSeparator9);

        jMenuEdit.setText("Edit");

        jMenuReqEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuReqEdit.setText("Request");
        jMenuReqEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuReqEditActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuReqEdit);
        jMenuEdit.add(jSeparator29);

        jMenuAcqEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuAcqEdit.setText("Acquittal");
        jMenuAcqEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuAcqEditActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuAcqEdit);
        jMenuEdit.add(jSeparator30);

        jMenuMonPlanEdit.setText("Monthly Plan");
        jMenuMonPlanEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuMonPlanEditActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuMonPlanEdit);

        jMenuFile.add(jMenuEdit);
        jMenuFile.add(jSeparator15);

        jMenuItemClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemClose.setText("Close");
        jMenuItemClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemClose);
        jMenuFile.add(jSeparator16);

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

        jMenuItemSupApp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSupApp.setText("Supervisor Approval");
        jMenuItemSupApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSupAppActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemSupApp);
        jMenuRequest.add(jSeparator6);

        jMenuItemAccMgrRev.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemAccMgrRev.setText("Account Manager Verification");
        jMenuItemAccMgrRev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAccMgrRevActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemAccMgrRev);
        jMenuRequest.add(jSeparator7);

        jMenuItemHeadApp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemHeadApp.setText("Head Approval");
        jMenuItemHeadApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemHeadAppActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemHeadApp);
        jMenuRequest.add(jSeparator8);

        jMenuPlanApproval.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuPlanApproval.setText("Central Finance Payment ");
        jMenuPlanApproval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuPlanApprovalActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuPlanApproval);
        jMenuRequest.add(jSeparator10);

        jMenuItemSchGen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSchGen.setText("Schedule Generation");
        jMenuItemSchGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSchGenActionPerformed(evt);
            }
        });
        jMenuRequest.add(jMenuItemSchGen);
        jMenuRequest.add(jSeparator13);

        jMenuItemView.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
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

        jMenuItemAcqSupApp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        jMenuItemAcqSupApp.setText("Supervisor Approval");
        jMenuItemAcqSupApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqSupAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqSupApp);
        jMenuAcquittal.add(jSeparator11);

        jMenuItemAcqAccApp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        jMenuItemAcqAccApp.setText("Account Manager Verification");
        jMenuItemAcqAccApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqAccAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqAccApp);
        jMenuAcquittal.add(jSeparator12);

        jMenuItemAcqHeadApp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        jMenuItemAcqHeadApp.setText("Head Approval");
        jMenuItemAcqHeadApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqHeadAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqHeadApp);
        jMenuAcquittal.add(jSeparator17);

        jMenuItemAcqFinApp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        jMenuItemAcqFinApp.setText("Central Finance Payment ");
        jMenuItemAcqFinApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqFinAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqFinApp);
        jMenuAcquittal.add(jSeparator18);

        jMenuItemAcqSchGen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        jMenuItemAcqSchGen.setText("Acquittals Schedule Generation");
        jMenuItemAcqSchGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqSchGenActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqSchGen);
        jMenuAcquittal.add(jSeparator19);

        jMenuItemAcqView.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
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

        jMenuItemProvMgrApproval.setText("Provincial Mgr Approval");
        jMenuItemProvMgrApproval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemProvMgrApprovalActionPerformed(evt);
            }
        });
        jMenuMonthlyPlan.add(jMenuItemProvMgrApproval);
        jMenuMonthlyPlan.add(jSeparator26);

        jMenuItemReqGenLst.setText("Request Generation list");
        jMenuItemReqGenLst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemReqGenLstActionPerformed(evt);
            }
        });
        jMenuMonthlyPlan.add(jMenuItemReqGenLst);
        jMenuMonthlyPlan.add(jSeparator34);

        jMenuItemMeetGenLst.setText("Meetings Generation list");
        jMenuItemMeetGenLst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemMeetGenLstActionPerformed(evt);
            }
        });
        jMenuMonthlyPlan.add(jMenuItemMeetGenLst);
        jMenuMonthlyPlan.add(jSeparator41);

        jMenuItemPlanView.setText("View Plan");
        jMenuItemPlanView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPlanViewActionPerformed(evt);
            }
        });
        jMenuMonthlyPlan.add(jMenuItemPlanView);

        jMenuBar.add(jMenuMonthlyPlan);

        jMenuReports.setText("Reports");

        jMenuItemReqSubmitted.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemReqSubmitted.setText("Staff Perdiem Requests ");
        jMenuItemReqSubmitted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemReqSubmittedActionPerformed(evt);
            }
        });
        jMenuReports.add(jMenuItemReqSubmitted);
        jMenuReports.add(jSeparator23);

        jMenuAgeAlluser.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuAgeAlluser.setText("Age Analysis All Users");
        jMenuAgeAlluser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuAgeAlluserActionPerformed(evt);
            }
        });
        jMenuReports.add(jMenuAgeAlluser);
        jMenuReports.add(jSeparator20);

        jMenuAgeByUser.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuAgeByUser.setText("Age Analysis by User");
        jMenuAgeByUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuAgeByUserActionPerformed(evt);
            }
        });
        jMenuReports.add(jMenuAgeByUser);
        jMenuReports.add(jSeparator24);

        jMenuProcessTimeline.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuProcessTimeline.setText("Request Process Timeline");
        jMenuProcessTimeline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuProcessTimelineActionPerformed(evt);
            }
        });
        jMenuReports.add(jMenuProcessTimeline);

        jMenuBar.add(jMenuReports);

        setJMenuBar(jMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jTabbedPaneAppSys, javax.swing.GroupLayout.PREFERRED_SIZE, 1500, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneAppSys, javax.swing.GroupLayout.DEFAULT_SIZE, 666, Short.MAX_VALUE)
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


    private void jMenuItemSupAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSupAppActionPerformed
        new JFrameSupAppList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemSupAppActionPerformed

    private void jMenuItemAccMgrRevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAccMgrRevActionPerformed
        new JFrameAccMgrAppList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemAccMgrRevActionPerformed

    private void jMenuItemHeadAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemHeadAppActionPerformed
        new JFrameReqHeadAppList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemHeadAppActionPerformed

    private void jMenuPlanApprovalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuPlanApprovalActionPerformed
       
        setVisible(false);
    }//GEN-LAST:event_jMenuPlanApprovalActionPerformed

    private void jMenuItemSchGenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSchGenActionPerformed
        new JFrameReqHQPayScheduleApp(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemSchGenActionPerformed

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

    private void jMenuItemAcqFinAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAcqFinAppActionPerformed
     
        setVisible(false);
    }//GEN-LAST:event_jMenuItemAcqFinAppActionPerformed

    private void jMenuItemAcqSchGenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAcqSchGenActionPerformed

        new JFrameReqHQAcqScheduleApp(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemAcqSchGenActionPerformed

    private void jMenuItemAcqViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAcqViewActionPerformed
        new JFrameAppAcquittalView(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemAcqViewActionPerformed

    private void jTableActivityReqVerifyKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableActivityReqVerifyKeyTyped
       
    }//GEN-LAST:event_jTableActivityReqVerifyKeyTyped

    private void jTableActivityReqVerifyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableActivityReqVerifyKeyReleased

        int row = jTableActivityReqVerify.getSelectedRow();
        int col = jTableActivityReqVerify.getSelectedColumn();
        Object id = jTableActivityReqVerify.getValueAt(row, col);

        totSeg1 = 0;
        totSeg2 = 0;
        for (int i = 4; i < 8; i++) {

            if (jTableActivityReqVerify.getValueAt(row, i).toString().trim().length() == 0) {
                jTableActivityReqVerify.setValueAt("0", row, i);
            }
            totSeg1 = totSeg1 + (Double.parseDouble((String) jTableActivityReqVerify.getValueAt(row, i)));

        }
        for (int i = 9; i < 12; i++) {

            if (jTableActivityReqVerify.getValueAt(row, i).toString().trim().length() == 0) {
                jTableActivityReqVerify.setValueAt("0", row, i);
            }

            totSeg2 = totSeg2 + (Double.parseDouble((String) jTableActivityReqVerify.getValueAt(row, i)));

        }
        jTableActivityReqVerify.setValueAt((totSeg1 + totSeg2), row, 12);
    }//GEN-LAST:event_jTableActivityReqVerifyKeyReleased

    private void jTableActivityReqVerifyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableActivityReqVerifyMouseClicked

        int row = jTableActivityReqVerify.getSelectedRow();
        int col = jTableActivityReqVerify.getSelectedColumn();
        Object id = jTableActivityReqVerify.getValueAt(row, col);

        totSeg1 = 0;
        totSeg2 = 0;
        for (int i = 4; i < 8; i++) {

            if (jTableActivityReqVerify.getValueAt(row, i).toString().trim().length() == 0) {
                jTableActivityReqVerify.setValueAt("0", row, i);
            }
            totSeg1 = totSeg1 + (Double.parseDouble((String) jTableActivityReqVerify.getValueAt(row, i)));

        }
        for (int i = 9; i < 12; i++) {

            if (jTableActivityReqVerify.getValueAt(row, i).toString().trim().length() == 0) {
                jTableActivityReqVerify.setValueAt("0", row, i);
            }

            totSeg2 = totSeg2 + (Double.parseDouble((String) jTableActivityReqVerify.getValueAt(row, i)));

        }
        jTableActivityReqVerify.setValueAt((totSeg1 + totSeg2), row, 12);

    }//GEN-LAST:event_jTableActivityReqVerifyMouseClicked

    private void jTableActivityReqVerifyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTableActivityReqVerifyFocusLost
        mainPageTotUpdate();
    }//GEN-LAST:event_jTableActivityReqVerifyFocusLost

    private void jMenuItemReqSubmittedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReqSubmittedActionPerformed
        new JFrameReqAllUserList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemReqSubmittedActionPerformed

    private void jMenuAgeAlluserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuAgeAlluserActionPerformed
       
    }//GEN-LAST:event_jMenuAgeAlluserActionPerformed

    private void jMenuAgeByUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuAgeByUserActionPerformed
       
    }//GEN-LAST:event_jMenuAgeByUserActionPerformed

    private void jMenuProcessTimelineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuProcessTimelineActionPerformed
       
    }//GEN-LAST:event_jMenuProcessTimelineActionPerformed

    private void jMenuItemRequestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRequestActionPerformed
      
    }//GEN-LAST:event_jMenuItemRequestActionPerformed

    private void jMenuItemAcquittalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAcquittalActionPerformed
        new JFrameAppAcquittal(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemAcquittalActionPerformed

    private void jMenuItemSpecialAcquittalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSpecialAcquittalActionPerformed
      
        setVisible(false);

    }//GEN-LAST:event_jMenuItemSpecialAcquittalActionPerformed

    private void jMenuItemRequestMOHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRequestMOHActionPerformed
      
    }//GEN-LAST:event_jMenuItemRequestMOHActionPerformed

    private void jMenuItemMeetAcquittalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemMeetAcquittalActionPerformed
       
    }//GEN-LAST:event_jMenuItemMeetAcquittalActionPerformed

    private void jMenuItem3WkPlanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3WkPlanActionPerformed
        new JFrameMnthPlanPerDiemCreate(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItem3WkPlanActionPerformed

    private void jMenuReqEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuReqEditActionPerformed
   
        setVisible(false);
    }//GEN-LAST:event_jMenuReqEditActionPerformed

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

    private void jMenuItemProvMgrApprovalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemProvMgrApprovalActionPerformed
        new JFrameMnthSupList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemProvMgrApprovalActionPerformed

    private void jMenuItemReqGenLstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReqGenLstActionPerformed
        new JFrameMnthReqGenList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemReqGenLstActionPerformed

    private void jMenuItemMeetGenLstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemMeetGenLstActionPerformed
        
    }//GEN-LAST:event_jMenuItemMeetGenLstActionPerformed

    private void jMenuItemPlanViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPlanViewActionPerformed
        new JFrameMnthViewList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemPlanViewActionPerformed

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
            java.util.logging.Logger.getLogger(JFrameReqAllUserView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameReqAllUserView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameReqAllUserView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameReqAllUserView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new JFrameReqAllUserView().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabelACC;
    private javax.swing.JLabel jLabelAccProvedSub;
    private javax.swing.JLabel jLabelAccProvedSubTot;
    private javax.swing.JLabel jLabelAccReq;
    private javax.swing.JLabel jLabelAccUnprovedSub;
    private javax.swing.JLabel jLabelAccUnprovedSubTot;
    private javax.swing.JLabel jLabelActMainPurpose;
    private javax.swing.JLabel jLabelAllReq;
    private javax.swing.JLabel jLabelAppTotReq;
    private javax.swing.JLabel jLabelAppTotReqCost;
    private javax.swing.JLabel jLabelAuthNam1;
    private javax.swing.JLabel jLabelAuthNam2;
    private javax.swing.JLabel jLabelAuthoriser;
    private javax.swing.JLabel jLabelBank;
    private javax.swing.JLabel jLabelBankName;
    private javax.swing.JLabel jLabelBreakFastSub;
    private javax.swing.JLabel jLabelBreakFastSubTot;
    private javax.swing.JLabel jLabelBudgetCode;
    private javax.swing.JLabel jLabelComments;
    private javax.swing.JLabel jLabelDinnerSub;
    private javax.swing.JLabel jLabelDinnerSubTot;
    private javax.swing.JLabel jLabelEmp;
    private javax.swing.JLabel jLabelEmpNam;
    private javax.swing.JLabel jLabelEmpNum;
    private javax.swing.JLabel jLabelEmpTitle;
    private javax.swing.JLabel jLabelFinDetails;
    private javax.swing.JLabel jLabelGenDate;
    private javax.swing.JLabel jLabelGenLogNam;
    private javax.swing.JLabel jLabelGenLogNam1;
    private javax.swing.JLabel jLabelGenTime;
    private javax.swing.JLabel jLabelHeaderGen;
    private javax.swing.JLabel jLabelHeaderLine;
    private javax.swing.JLabel jLabelIncidentalSub;
    private javax.swing.JLabel jLabelIncidentalSubTot;
    private javax.swing.JLabel jLabelLinLogNam;
    private javax.swing.JLabel jLabelLinRegNum;
    private javax.swing.JLabel jLabelLinSerial;
    private javax.swing.JLabel jLabelLineDate;
    private javax.swing.JLabel jLabelLineTime;
    private javax.swing.JLabel jLabelLogo;
    private javax.swing.JLabel jLabelLogo1;
    private javax.swing.JLabel jLabelLunchSub;
    private javax.swing.JLabel jLabelLunchSubTot;
    private javax.swing.JLabel jLabelMainPurpose;
    private javax.swing.JLabel jLabelMiscReq;
    private javax.swing.JLabel jLabelMiscSubTot;
    private javax.swing.JLabel jLabelMscSub;
    private javax.swing.JLabel jLabelNum;
    private javax.swing.JLabel jLabelNum1;
    private javax.swing.JLabel jLabelOffice;
    private javax.swing.JLabel jLabelProvince;
    private javax.swing.JLabel jLabelRegDate;
    private javax.swing.JLabel jLabelRegNum;
    private javax.swing.JLabel jLabelRegYear;
    private javax.swing.JLabel jLabelReqAccNum;
    private javax.swing.JLabel jLabelReqBudCode;
    private javax.swing.JLabel jLabelReqEmpNum;
    private javax.swing.JLabel jLabelReqOffice;
    private javax.swing.JLabel jLabelReqProvince;
    private javax.swing.JLabel jLabelSepDetails;
    private javax.swing.JLabel jLabelSerial;
    private javax.swing.JLabel jLabelStatusView;
    private javax.swing.JLabel jLabellogged;
    private javax.swing.JMenuItem jMenuAcqEdit;
    private javax.swing.JMenu jMenuAcquittal;
    private javax.swing.JMenuItem jMenuAgeAlluser;
    private javax.swing.JMenuItem jMenuAgeByUser;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenu jMenuEdit;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenuItem jMenuItem3WkPlan;
    private javax.swing.JMenuItem jMenuItemAccMgrRev;
    private javax.swing.JMenuItem jMenuItemAcqAccApp;
    private javax.swing.JMenuItem jMenuItemAcqFinApp;
    private javax.swing.JMenuItem jMenuItemAcqHeadApp;
    private javax.swing.JMenuItem jMenuItemAcqSchGen;
    private javax.swing.JMenuItem jMenuItemAcqSupApp;
    private javax.swing.JMenuItem jMenuItemAcqView;
    private javax.swing.JMenuItem jMenuItemAcquittal;
    private javax.swing.JMenuItem jMenuItemClose;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuPlanApproval;
    private javax.swing.JMenuItem jMenuItemHeadApp;
    private javax.swing.JMenuItem jMenuItemMeetAcquittal;
    private javax.swing.JMenuItem jMenuItemMeetGenLst;
    private javax.swing.JMenuItem jMenuItemPlanView;
    private javax.swing.JMenuItem jMenuItemProvMgrApproval;
    private javax.swing.JMenuItem jMenuItemReqGenLst;
    private javax.swing.JMenuItem jMenuItemReqSubmitted;
    private javax.swing.JMenuItem jMenuItemRequest;
    private javax.swing.JMenuItem jMenuItemRequestMOH;
    private javax.swing.JMenuItem jMenuItemSchGen;
    private javax.swing.JMenuItem jMenuItemSpecialAcquittal;
    private javax.swing.JMenuItem jMenuItemSupApp;
    private javax.swing.JMenuItem jMenuItemView;
    private javax.swing.JMenuItem jMenuMonPlanEdit;
    private javax.swing.JMenu jMenuMonthlyPlan;
    private javax.swing.JMenu jMenuNew;
    private javax.swing.JMenuItem jMenuProcessTimeline;
    private javax.swing.JMenu jMenuReports;
    private javax.swing.JMenuItem jMenuReqEdit;
    private javax.swing.JMenu jMenuRequest;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelGen;
    private javax.swing.JPanel jPanelRequest;
    private javax.swing.JPanel jPanelStatusView;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
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
    private javax.swing.JPopupMenu.Separator jSeparator20;
    private javax.swing.JPopupMenu.Separator jSeparator21;
    private javax.swing.JPopupMenu.Separator jSeparator23;
    private javax.swing.JPopupMenu.Separator jSeparator24;
    private javax.swing.JPopupMenu.Separator jSeparator26;
    private javax.swing.JPopupMenu.Separator jSeparator28;
    private javax.swing.JPopupMenu.Separator jSeparator29;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator30;
    private javax.swing.JPopupMenu.Separator jSeparator31;
    private javax.swing.JPopupMenu.Separator jSeparator34;
    private javax.swing.JPopupMenu.Separator jSeparator41;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    public javax.swing.JTabbedPane jTabbedPaneAppSys;
    private javax.swing.JTable jTableActivityReqVerify;
    private javax.swing.JTextArea jTextAreaComments;
    // End of variables declaration//GEN-END:variables
}
