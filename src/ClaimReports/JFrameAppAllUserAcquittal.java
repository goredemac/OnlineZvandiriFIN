/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClaimReports;

import ClaimPlanPerDiem.JFrameMnthPlanPerDiemEdit;
import ClaimPlanPerDiem.JFrameMnthPlanPerDiemCreate;
import ClaimAppendix2.*;
import ClaimAppendix1.JFrameAccMgrAppList;

import ClaimAppendix1.JFrameReqHeadAppList;
import ClaimAppendix1.JFrameReqHQPayScheduleApp;
import ClaimAppendix1.JFrameSupAppList;
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
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import ClaimAppendix1.*;
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
import javax.swing.WindowConstants;
import ClaimPlan.*;
import utils.connCred;

/**
 *
 * @author cgoredema
 */
public class JFrameAppAllUserAcquittal extends javax.swing.JFrame {

    PreparedStatement pst = null;
    PreparedStatement pst1 = null;
    connCred c = new connCred();
    int itmNum = 1;
    int month, finyear, count;
    Date curYear = new Date();
    int initdocVersion = 0;
    int checkRefCount = 0;
    int pdCount;
    SimpleDateFormat df = new SimpleDateFormat("yyyy");
    DefaultTableModel model;
    DefaultTableModel modelAcq;
    String sendTo, sendToProv, sendToFin, createUsrNam, acqFinOffMail, reqUsrMail,
            supUsrMail, acqFinOffNam, breakfastAll, lunchAll,
            docVersion, oldDocVersion, preModNum, dinnerAll, incidentalAll, unProvedAll,
            searchRef, statusCodeApp, statusCodeDisApp, oldComments, checkRef,
            authNam1, authNam2, usrGrp;
    String province = "";
    String checker = "";
    String mailUsrNam = "finance@ophid.co.zw";
    String mailUsrPass = "MgqM5utyUr43x#";
    String ipAdd = "localhost:14575";
    String usrName = "appfin";
    String usrPass = "542ytDYvynv$TVYb";
    String actRef = "";
    String actRef1 = "";
    String actRef2 = "";
    String actRef3 = "";
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
    String hostName = "";
    int oldImgAttVer = 0;

    /**
     * Creates new form JFrameTabApp1
     */
    public JFrameAppAllUserAcquittal() {
        initComponents();
        showDate();
        showTime();
        computerName();
        model = (DefaultTableModel) jTableActivityReq.getModel();
        modelAcq = (DefaultTableModel) jTableActivityAcq.getModel();
        fetchdata();
        mainPageTotUpdate();
        mainPageTotUpdateAcq();
    }

    public JFrameAppAllUserAcquittal(String yearRef, String usrLogNam) {
        initComponents();
        showDate();
        showTime();
        computerName();

        jLabelAuthoriser.setVisible(false);
        jLabelAuthNam1.setVisible(false);
        jLabelAuthNam2.setVisible(false);
        jTextAreaComments.setWrapStyleWord(true);
        jTextAreaComments.setLineWrap(true);
        jLabelEmp.setText(usrLogNam);
        jLabelEmp.setVisible(false);
            jLabelStatusView.setText("");
        jLabelReqRefNum.setText(yearRef);
        model = (DefaultTableModel) jTableActivityReq.getModel();
        modelAcq = (DefaultTableModel) jTableActivityAcq.getModel();
        checkRef = yearRef;
        findUser();
        findUserGrp();
        findAuthorisation();
        if ("S".equals(checkRef.substring(0, 1))) {
            jLabelReqSerial.setText("S");
            fetchdataSpecial();
        } else {
            fetchdata();
        }
        if (("S".equals(checkRef.substring(0, 1))) && (Integer.parseInt(jLabelReqRefNum.getText()) <= 24)) {
            jLabelReqSerial.setText("S");
            jLabelSpecial.setText("Special Perdiem Acquittal");
            jLabelSpecial1.setText("Special Perdiem Acquittal");
            jLabelSpecial2.setText("Special Perdiem Acquittal");
            jLabelComments.setText("Special Acquittal Reason/s");
            jLabelAcqYear.setVisible(false);
            jLabelAcqSerial.setVisible(false);
            jLabelAcqRefNum.setVisible(false);
            jLabelAcqRegDate.setVisible(false);
            jLabelNumAcq.setVisible(false);
            jLabelNum.setText(jLabelNumAcq.getText());

        }
        if (("S".equals(checkRef.substring(0, 1))) && (Integer.parseInt(jLabelReqRefNum.getText()) > 24)) {
            jLabelReqSerial.setText("S");
            jLabelHeaderGen.setText("        MOHCC ACQUITTAL");
            jLabelHeaderLine.setText("        MOHCC ACQUITTAL");
            jLabelHeaderLine1.setText("        MOHCC ACQUITTAL");
            jLabelComments.setText("Special Acquittal Reason/s");
            jLabelAcqYear.setVisible(false);
            jLabelAcqSerial.setVisible(false);
            jLabelAcqRefNum.setVisible(false);
            jLabelAcqRegDate.setVisible(false);
            jLabelNumAcq.setVisible(false);
            jLabelNum.setText(jLabelNumAcq.getText());
            jLabelSpecial.setVisible(false);
            jLabelSpecial1.setVisible(false);
            jLabelSpecial2.setVisible(false);

        } else {
            jLabelSpecial.setVisible(false);
            jLabelSpecial1.setVisible(false);
            jLabelSpecial2.setVisible(false);

        }

        findChgPaid();
        mainPageTotUpdate();
        mainPageTotUpdateAcq();
        findApplicantUser();

    }

    void showTime() {
        new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date d = new Date();
                SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss a");
                jLabelGenTime.setText(s.format(d));
                jLabelLineTime.setText(s.format(d));
                jLabelLineTime1.setText(s.format(d));

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

    void findApplicantUser() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select * from [ClaimsAppSysZvandiri].[dbo]."
                    + "[EmpDetTab] where emp_num ='" + jLabelAcqEmpNum.getText() + "'");

            while (r.next()) {

                reqUsrMail = r.getString(4);
                acqFinOffNam = r.getString(22);
                acqFinOffMail = r.getString(23);
                supUsrMail = r.getString(6);
                sendToFin = r.getString(13);

            }

            //                 conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Connect to Database (Province)."
                    + "Try Again", "Error Connection", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
    }

    void findChgPaid() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            st.executeQuery("SELECT count(*) FROM[ClaimsAppSysZimTTECH].[dbo].[ChgPaidTab]"
                    + " where concat(PREV_SERIAL,PREV_REF_NUM) = "
                    + "'" + jLabelReqSerial.getText() + jLabelReqRefNum.getText() + "' "
                    + "and REG_MOD_VER = (select REG_MOD_VER FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] "
                    + "where concat(PREV_SERIAL,PREV_REF_NUM) ="
                    + "'" + jLabelReqSerial.getText() + jLabelReqRefNum.getText() + "' and ACT_REC_STA = 'Q' "
                    + "and concat (SERIAL,REF_NUM) = '" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "' )");

            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                pdCount = rs.getInt(1);

            }

            if (pdCount > 0) {

                st1.executeQuery("SELECT *  FROM [ClaimsAppSysZvandiri].[dbo].[ChgPaidTab] "
                        + "  Where concat(PREV_SERIAL,PREV_REF_NUM) ="
                        + " '" + jLabelReqSerial.getText() + jLabelReqRefNum.getText()
                        + "'  and concat (SERIAL,REF_NUM) = '" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "' "
                        + "and REG_MOD_VER = (select max(REG_MOD_VER) from [ClaimsAppSysZvandiri].[dbo].[ChgPaidTab] "
                        + "where concat(PREV_SERIAL,PREV_REF_NUM) = "
                        + "'" + jLabelReqSerial.getText() + jLabelReqRefNum.getText() + "' "
                        + "and concat (SERIAL,REF_NUM) = '" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "')");

                ResultSet rs1 = st1.getResultSet();

                while (rs1.next()) {
                    jLabelAcqPayBack.setText("Paid Back A/C " + rs1.getString(6));
                    jLabelAcqAmtPayBack.setText(rs1.getString(7));
                }

            }

        } catch (Exception e) {
            System.out.println(e);
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
                jLabelLineLogNam.setText(r.getString(2));
                jLabelLineLogNam1.setText(r.getString(2));

                createUsrNam = r.getString(2);

            }

            //                 conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Connect to Database (Province).Try Again", "Error Connection", JOptionPane.WARNING_MESSAGE);
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
                    + "or AUTH_ALL_NAM != ' ') and  concat(SERIAL,REF_NUM) ='" + checkRef + "' "
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

                    JOptionPane.showMessageDialog(null, "<html><font color=\"red\"  size=\"3\">Perdiem Aquittal has an allowance request authorisation granted by "
                            + "<b><font color=\"green\"> " + authNam2 + "</font> </html>");

                } else if (authNam2 == null) {
                    jLabelAuthNam1.setText(authNam1);

                    JOptionPane.showMessageDialog(null, "<html><font color=\"red\"  size=\"3\">Perdiem Aquittal has an allowance request authorisation granted by "
                            + "<b><font color=\"green\"> " + authNam1 + "</font> </html>");

                } else {
                    jLabelAuthNam1.setText(authNam1);
                    jLabelAuthNam2.setText(authNam2);

                    JOptionPane.showMessageDialog(null, "<html><font color=\"red\"  size=\"3\">Perdiem Aquittal has an allowance request authorisation granted by "
                            + "<b><font color=\"green\"> " + authNam1 + "   " + authNam2 + " </html>");
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

    void mainPageTotUpdate() {
        breakfastsubtotal = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double breakfastamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 4));
            breakfastsubtotal += breakfastamount;

        }
        jLabelBreakFastSubTot.setText(Double.toString(breakfastsubtotal));

        lunchsubtotal = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double lunchamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 5));
            lunchsubtotal += lunchamount;
        }
        jLabelLunchSubTot.setText(Double.toString(lunchsubtotal));

        dinnersubtotal = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double dinneramount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 6));
            dinnersubtotal += dinneramount;
        }
        jLabelDinnerSubTot.setText(Double.toString(dinnersubtotal));

        incidentalsubtotal = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double incidentalamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 7));
            incidentalsubtotal += incidentalamount;
        }
        jLabelIncidentalSubTot.setText(Double.toString(incidentalsubtotal));

        miscSubTot = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double miscamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 9));
            miscSubTot += miscamount;
        }
        jLabelMiscSubTot.setText(Double.toString(miscSubTot));

        provedSubTot = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double provedamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 10));
            provedSubTot += provedamount;
        }
        jLabelAccProvedSubTot.setText(Double.toString(provedSubTot));

        unprovedSubTot = 0;
        for (int i = 0; i < jTableActivityReq.getRowCount(); i++) {
            double unprovedamount = Double.parseDouble((String) jTableActivityReq.getValueAt(i, 11));
            unprovedSubTot += unprovedamount;
        }
        jLabelAccUnprovedSubTot.setText(Double.toString(unprovedSubTot));

        allTotal = 0;
        allTotal = unprovedSubTot + provedSubTot + miscSubTot + incidentalsubtotal
                + dinnersubtotal + lunchsubtotal + breakfastsubtotal;

    }

    void mainPageTotUpdateAcq() {
        breakfastsubtotalAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double breakfastamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 4));
            breakfastsubtotalAcq += breakfastamountAcq;

        }
        jLabelAcqBreakFastSubTot.setText(Double.toString(breakfastsubtotalAcq));

        lunchsubtotalAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double lunchamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 5));
            lunchsubtotalAcq += lunchamountAcq;

        }
        jLabelAcqLunchSubTot.setText(Double.toString(lunchsubtotalAcq));

        dinnersubtotalAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double dinneramountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 6));
            dinnersubtotalAcq += dinneramountAcq;
        }
        jLabelAcqDinnerSubTot.setText(Double.toString(dinnersubtotalAcq));

        incidentalsubtotalAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double incidentalamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 7));
            incidentalsubtotalAcq += incidentalamountAcq;
        }
        jLabelAcqIncidentalSubTot.setText(Double.toString(incidentalsubtotalAcq));

        miscSubTotAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double miscamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 9));
            miscSubTotAcq += miscamountAcq;
        }
        jLabelAcqMiscSubTot.setText(Double.toString(miscSubTotAcq));

        provedSubTotAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double provedamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 10));
            provedSubTotAcq += provedamountAcq;
        }
        jLabelAcqAccProvedSubTot.setText(Double.toString(provedSubTotAcq));

        unprovedSubTotAcq = 0;
        for (int i = 0; i < jTableActivityAcq.getRowCount(); i++) {
            double unprovedamountAcq = Double.parseDouble((String) jTableActivityAcq.getValueAt(i, 11));
            unprovedSubTotAcq += unprovedamountAcq;
        }
        jLabelAcqAccUnprovedSubTotBal.setText(Double.toString(unprovedSubTotAcq));

        allTotalAcq = unprovedSubTotAcq + provedSubTotAcq + miscSubTotAcq + incidentalsubtotalAcq
                + dinnersubtotalAcq + lunchsubtotalAcq + breakfastsubtotalAcq;

        jLabelAcqBreakFastSubTotBal.setText(Double.toString(breakfastsubtotal - breakfastsubtotalAcq));
        jLabelAcqLunchSubTotBal.setText(Double.toString(lunchsubtotal - lunchsubtotalAcq));
        jLabelAcqDinnerSubTotBal.setText(Double.toString(dinnersubtotal - dinnersubtotalAcq));
        jLabelAcqIncidentalSubTotBal.setText(Double.toString(incidentalsubtotal - incidentalsubtotalAcq));
        jLabelAcqMiscSubTotBal.setText(Double.toString(miscSubTot - miscSubTotAcq));
        jLabelAcqAccUnprovedSubTotBal.setText(Double.toString(unprovedSubTot - unprovedSubTotAcq));
        jLabelAcqAccProvedSubTotBal.setText(Double.toString(provedSubTot - provedSubTotAcq));

        if (Integer.parseInt(jLabelReqRefNum.getText()) <= 24) {
            jLabelAcqAppTotReqCost.setText(Double.toString(allTotal - allTotalAcq));
            double totDiff = allTotal - allTotalAcq;
            if (totDiff > 0) {
                jLabelAppTotReq.setText("Total (Change)");
            } else if (totDiff < 0) {
                jLabelAppTotReq.setText("Total (Shortfall)");
            } else if (totDiff == 0) {
                jLabelAppTotReq.setText("Total (Balancing)");
            } else {
                jLabelAcqAppTotReqCost.setText(Double.toString(allTotalAcq));
            }
        }

    }

    void fetchdataSpecial() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();
            Statement st3 = conn.createStatement();
            Statement st4 = conn.createStatement();

            st1.executeQuery("SELECT top 1 ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_YEAR,"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_DAT, "
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.EMP_NUM,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.EMP_NAM,"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.EMP_TTL, ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.EMP_PROV,"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.EMP_OFF,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.EMP_BNK_NAM,"
                    + " ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.ACC_NUM,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.BUD_COD,"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.ACT_MAIN_PUR,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.ACT_TOT_AMT,"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.ACT_REC_STA,ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.SPECIAL_REASON"
                    + " FROM ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab , ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab , ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab "
                    + "where (ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM = ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.REF_NUM "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.SERIAL "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER =ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.DOC_VER "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM = ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.REF_NUM "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.SERIAL "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER =ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.DOC_VER)"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.ACT_REC_STA = 'Q'  "
                    + "and concat(ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM) "
                    + "='" + jLabelReqRefNum.getText() + "'");

            ResultSet r1 = st1.getResultSet();

            while (r1.next()) {
                jLabelReqYear.setText(r1.getString(1));
                jLabelReqRefNum.setText(r1.getString(2));
                jLabelAcqEmpNum.setText(r1.getString(4));
                jLabelAcqEmpNam.setText(r1.getString(5));
                jLabelAcqEmpTitle.setText(r1.getString(6));
                jLabelAcqProvince.setText(r1.getString(7));
                jLabelAcqOffice.setText(r1.getString(8));
                jLabelAcqBankName.setText(r1.getString(9));
                jLabelAcqAccNum.setText(r1.getString(10));
                jLabelAcqBudCode.setText(r1.getString(11));
                jLabelAcqActMainPurpose.setText(r1.getString(12));
                jLabelAcqAppTotReqCost.setText(r1.getString(13));
                jLabelRegDate.setText(r1.getString(3));
                province = r1.getString(7);
                searchRef = r1.getString(2);
                jTextAreaComments.setText(r1.getString(15));

            }
            st3.executeQuery("SELECT distinct ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_YEAR,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL,"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_DAT,"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER + 1,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER FROM ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab, "
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab , ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab where "
                    + "(ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM = ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.REF_NUM "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.SERIAL "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER =ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.DOC_VER "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM = ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.REF_NUM "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.SERIAL "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER =ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.DOC_VER) "
                    + "and (ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.ACT_REC_STA = 'Q' "
                    + " and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = 'S')   and concat(ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL,"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM) "
                    + "= '" + jLabelReqSerial.getText() + jLabelReqRefNum.getText() + "'");

            ResultSet r3 = st3.getResultSet();

            while (r3.next()) {
                jLabelAcqYear.setText(r3.getString(1));
                jLabelAcqRefNum.setText(r3.getString(3));
                jLabelAcqRegDate.setText(r3.getString(4));
                docVersion = r3.getString(5);
                oldDocVersion = r3.getString(6);
            }

            model.insertRow(model.getRowCount(), new Object[]{"1901-01-01", "Special", "0",
                "Special", "0", "0", "0", "0", "",
                "0", "0", "0", "0"});

            st2.executeQuery("SELECT distinct ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_YEAR,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_DAT, ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ITM_NUM,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_DATE,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_DEST, \n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_DIST,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_ITM_PUR,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.BRK_AMT,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.LNC_AMT,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.DIN_AMT,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.INC_AMT,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.MSC_ACT,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.MSC_AMT,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACC_PRO_AMT, ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACC_UNPROV_AMT, \n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_ITM_TOT,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.PREV_REF_NUM,\n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.PREV_REF_DAT FROM ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab, \n"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab , ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab where \n"
                    + "(ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM = ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.REF_NUM \n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.SERIAL \n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER =ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.DOC_VER \n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM = ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.REF_NUM \n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.SERIAL \n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER =ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.DOC_VER\n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REG_MOD_VER= ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.REG_MOD_VER\n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REG_MOD_VER= ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.REG_MOD_VER) and  \n"
                    + "(ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.ACT_REC_STA = 'Q' \n"
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = 'S')  and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REG_MOD_VER =(select max(REG_MOD_VER) from ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab\n"
                    + "where concat(SERIAL,REF_NUM)='" + jLabelReqSerial.getText() + jLabelReqRefNum.getText() + "')"
                    + "and concat(ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM)='" + jLabelReqSerial.getText() + jLabelReqRefNum.getText() + "' order by ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_DATE");

            ResultSet r2 = st2.getResultSet();

            while (r2.next()) {
                modelAcq.insertRow(modelAcq.getRowCount(), new Object[]{r2.getString(5), r2.getString(6), r2.getString(7),
                    r2.getString(8), r2.getString(9), r2.getString(10), r2.getString(11), r2.getString(12), r2.getString(13),
                    r2.getString(14), r2.getString(15), r2.getString(16), r2.getString(17)});

            }

            st4.executeQuery("SELECT COMMENTS,USR_ACTION  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] where "
                    + "concat(SERIAL,REF_NUM)='" + checkRef + "' and "
                    + "DOC_VER =(select max(DOC_VER) from ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab "
                    + "where concat(SERIAL,REF_NUM)='" + checkRef + "') ");

            ResultSet r4 = st4.getResultSet();

            while (r4.next()) {
                jTextAreaComments.setText(r4.getString(1));
                oldComments = r4.getString(1);
                jLabelStatusView.setText(r4.getString(2));

                if ("Acquittal- Head Approved".equals(jLabelStatusView.getText())) {
                    jLabelStatusView.setForeground(new java.awt.Color(0, 153, 0));
                } else {
                    jLabelStatusView.setForeground(new java.awt.Color(255, 0, 0));

                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdata() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();
            Statement st3 = conn.createStatement();
            Statement st4 = conn.createStatement();
            Statement st5 = conn.createStatement();

//            st1.executeQuery("SELECT top 1 ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_YEAR,"
//                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_DAT, "
//                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.EMP_NUM,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.EMP_NAM,"
//                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.EMP_TTL, ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.EMP_PROV,"
//                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.EMP_OFF,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.EMP_BNK_NAM,"
//                    + " ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.ACC_NUM,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.BUD_COD,"
//                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.ACT_MAIN_PUR,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.ACT_TOT_AMT,"
//                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.ACT_REC_STA"
//                    + " FROM ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab , ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab , ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab "
//                    + "where (ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM = ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.REF_NUM "
//                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.SERIAL "
//                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER =ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.DOC_VER "
//                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM = ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.REF_NUM "
//                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.SERIAL "
//                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER =ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.DOC_VER) "
//                    + "and ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.DOC_STATUS='HeadApprove' and "
//                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.ACT_REC_STA = 'A' and ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.DOC_STATUS='HeadApprove' "
//                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.ACT_REC_STA = 'A'  "
//                    + "and concat(ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM) "
//                    + "='" + jLabelReqRefNum.getText() + "'");
            st1.executeQuery("SELECT REF_YEAR,SERIAL,REF_NUM,REF_DAT,EMP_NUM,EMP_NAM,EMP_TTL,"
                    + "EMP_PROV,EMP_OFF,EMP_BNK_NAM,ACC_NUM,BUD_COD,ACT_MAIN_PUR,"
                    + "ACT_TOT_AMT,ACT_REC_STA,DOC_VER,DOC_VER +1   "
                    + "FROM ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab where "
                    + "concat(SERIAL,REF_NUM)='" + checkRef + "' and "
                    + "DOC_VER =(select max(DOC_VER) from ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab "
                    + "where concat(SERIAL,REF_NUM)='" + checkRef + "') ");
            ResultSet r1 = st1.getResultSet();

            while (r1.next()) {
                jLabelReqYear.setText(r1.getString(1));
                jLabelReqSerial.setText(r1.getString(2));
                jLabelReqRefNum.setText(r1.getString(3));
                jLabelRegDate.setText(r1.getString(4));
                jLabelAcqEmpNum.setText(r1.getString(5));
                jLabelAcqEmpNam.setText(r1.getString(6));
                jLabelAcqEmpTitle.setText(r1.getString(7));
                jLabelAcqProvince.setText(r1.getString(8));
                jLabelAcqOffice.setText(r1.getString(9));
                jLabelAcqBankName.setText(r1.getString(10));
                jLabelAcqAccNum.setText(r1.getString(11));
                jLabelAcqBudCode.setText(r1.getString(12));
                jLabelAcqActMainPurpose.setText(r1.getString(13));
                jLabelAcqAppTotReqCost.setText(r1.getString(14));

                province = r1.getString(8);

                searchRef = r1.getString(3);

            }

            st.executeQuery("SELECT distinct ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_YEAR,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM,"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_DAT, ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ITM_NUM,"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_DATE,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_DEST, "
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_DIST,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_ITM_PUR,"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.BRK_AMT,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.LNC_AMT,"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.DIN_AMT,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.INC_AMT,"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.MSC_ACT,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.MSC_AMT,"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACC_PRO_AMT, ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACC_UNPROV_AMT, "
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_ITM_TOT FROM ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab, "
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab , ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab where "
                    + "(ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM = ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.REF_NUM "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.SERIAL "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER =ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.DOC_VER "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM = ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.REF_NUM "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.SERIAL "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER =ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.DOC_VER) and "
                    + "ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.DOC_STATUS='HeadApprove' and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.ACT_REC_STA = 'A' "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.DOC_STATUS='HeadApprove' and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.ACT_REC_STA = 'A'  "
                    + "and concat(ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_YEAR,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM) "
                    + "= '" + jLabelReqYear.getText() + jLabelReqSerial.getText() + jLabelReqRefNum.getText() + "' order by "
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_DATE");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                model.insertRow(model.getRowCount(), new Object[]{r.getString(5), r.getString(6), r.getString(7),
                    r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12), r.getString(13),
                    r.getString(14), r.getString(15), r.getString(16), r.getString(17)});

            }

            st2.executeQuery("SELECT distinct ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_YEAR,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM,"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_DAT, ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ITM_NUM,"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_DATE,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_DEST, "
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_DIST,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_ITM_PUR,"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.BRK_AMT,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.LNC_AMT,"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.DIN_AMT,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.INC_AMT,"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.MSC_ACT,ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.MSC_AMT,"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACC_PRO_AMT, ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACC_UNPROV_AMT, "
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_ITM_TOT,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.PREV_REF_NUM,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.PREV_REF_DAT FROM ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab, "
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab , ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab where "
                    + "(ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM = ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.REF_NUM "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.SERIAL "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER =ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.DOC_VER "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM = ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.REF_NUM "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.SERIAL "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER =ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.DOC_VER) and  "
                    + "(ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.ACT_REC_STA = 'Q' "
                    + " and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = 'A')   and concat(ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.PREV_SERIAL,"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.PREV_REF_NUM) "
                    + "= '" + jLabelReqSerial.getText() + jLabelReqRefNum.getText() + "' order by"
                    + " ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.ACT_DATE");

            ResultSet r2 = st2.getResultSet();

            while (r2.next()) {
                modelAcq.insertRow(modelAcq.getRowCount(), new Object[]{r2.getString(5), r2.getString(6), r2.getString(7),
                    r2.getString(8), r2.getString(9), r2.getString(10), r2.getString(11), r2.getString(12), r2.getString(13),
                    r2.getString(14), r2.getString(15), r2.getString(16), r2.getString(17)});

            }

            st3.executeQuery("SELECT distinct ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_YEAR,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL,"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_DAT,"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER + 1,ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER FROM ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab, "
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab , ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab where "
                    + "(ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM = ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.REF_NUM "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.SERIAL "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER =ClaimsAppSysZimTTECH.dbo.ClaimAppItmTab.DOC_VER "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.REF_NUM = ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.REF_NUM "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.SERIAL "
                    + "and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.DOC_VER =ClaimsAppSysZimTTECH.dbo.ClaimsWFActTab.DOC_VER) and  "
                    + "(ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.ACT_REC_STA = 'Q' "
                    + " and ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.SERIAL = 'A')   and concat(ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.PREV_SERIAL,"
                    + "ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab.PREV_REF_NUM) "
                    + "= '" + jLabelReqSerial.getText() + jLabelReqRefNum.getText() + "'");

            ResultSet r3 = st3.getResultSet();

            while (r3.next()) {
                jLabelAcqYear.setText(r3.getString(1));
                jLabelAcqRefNum.setText(r3.getString(3));
                jLabelAcqRegDate.setText(r3.getString(4));
                docVersion = r3.getString(5);
                oldDocVersion = r3.getString(6);
            }

            st4.executeQuery("SELECT COMMENTS,USR_ACTION  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] where "
                    + "concat(SERIAL,REF_NUM)='" + checkRef + "' and "
                    + "DOC_VER =(select max(DOC_VER) from ClaimsAppSysZimTTECH.dbo.ClaimAppGenTab "
                    + "where concat(SERIAL,REF_NUM)='" + checkRef + "') ");

            ResultSet r4 = st4.getResultSet();

            while (r4.next()) {
                jTextAreaComments.setText(r4.getString(1));
                oldComments = r4.getString(1);
                jLabelStatusView.setText(r4.getString(2));

                if ("Acquittal- Head Approved".equals(jLabelStatusView.getText())) {
                    jLabelStatusView.setForeground(new java.awt.Color(0, 153, 0));
                } else {
                    jLabelStatusView.setForeground(new java.awt.Color(255, 0, 0));

                }

            }

            st5.executeQuery("SELECT max(REG_MOD_VER)  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab]"
                    + " where  concat(SERIAL,REF_NUM) = '" + jLabelAcqSerial.getText() + jLabelAcqRefNum.getText() + "'");

            ResultSet r5 = st5.getResultSet();

            while (r5.next()) {
                preModNum = r5.getString(1);
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
        jLabelBudgetCode = new javax.swing.JLabel();
        jLabelActMainPurposeDesc = new javax.swing.JLabel();
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
        jLabelAcqAppTotReqCost = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabelAppTotReq = new javax.swing.JLabel();
        jLabelOffice = new javax.swing.JLabel();
        jLabelAcqAccNum = new javax.swing.JLabel();
        jLabelACC = new javax.swing.JLabel();
        jLabelNum = new javax.swing.JLabel();
        jLabelReqSerial = new javax.swing.JLabel();
        jLabelAcqActMainPurpose = new javax.swing.JLabel();
        jLabelAcqProvince = new javax.swing.JLabel();
        jLabelAcqOffice = new javax.swing.JLabel();
        jLabelAcqBudCode = new javax.swing.JLabel();
        jLabelAcqEmpNum = new javax.swing.JLabel();
        jLabelRegDate = new javax.swing.JLabel();
        jLabelEmp = new javax.swing.JLabel();
        jLabelReqYear = new javax.swing.JLabel();
        jLabelAcqRegDate = new javax.swing.JLabel();
        jLabelAcqRefNum = new javax.swing.JLabel();
        jLabelAcqSerial = new javax.swing.JLabel();
        jLabelNumAcq = new javax.swing.JLabel();
        jLabelAcqYear = new javax.swing.JLabel();
        jLabelReqRefNum = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaComments = new javax.swing.JTextArea();
        jLabelComments = new javax.swing.JLabel();
        jLabelAuthoriser = new javax.swing.JLabel();
        jLabelAuthNam1 = new javax.swing.JLabel();
        jLabelAuthNam2 = new javax.swing.JLabel();
        jLabelAcqAmtPayBack = new javax.swing.JLabel();
        jLabelAcqPayBack = new javax.swing.JLabel();
        jLabelSpecial = new javax.swing.JLabel();
        jPanelStatusView = new javax.swing.JPanel();
        jLabelStatusView = new javax.swing.JLabel();
        jPanelRequest = new javax.swing.JPanel();
        jLabelLogo1 = new javax.swing.JLabel();
        jLabelHeaderLine = new javax.swing.JLabel();
        jLabelLineDate = new javax.swing.JLabel();
        jLabelLineTime = new javax.swing.JLabel();
        jLabellogged = new javax.swing.JLabel();
        jLabelLineLogNam = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableActivityReq = new javax.swing.JTable();
        jLabelSpecial1 = new javax.swing.JLabel();
        jPanelAcquittal = new javax.swing.JPanel();
        jLabelLogo2 = new javax.swing.JLabel();
        jLabelHeaderLine1 = new javax.swing.JLabel();
        jLabelLineDate1 = new javax.swing.JLabel();
        jLabelLineTime1 = new javax.swing.JLabel();
        jLabellogged1 = new javax.swing.JLabel();
        jLabelLineLogNam1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableActivityAcq = new javax.swing.JTable();
        jLabelSpecial2 = new javax.swing.JLabel();
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
        jSeparator10 = new javax.swing.JPopupMenu.Separator();
        jMenuItemClose = new javax.swing.JMenuItem();
        jSeparator11 = new javax.swing.JPopupMenu.Separator();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuRequest = new javax.swing.JMenu();
        jMenuItemSupApp = new javax.swing.JMenuItem();
        jSeparator12 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAccMgrRev = new javax.swing.JMenuItem();
        jSeparator17 = new javax.swing.JPopupMenu.Separator();
        jMenuItemHeadApp = new javax.swing.JMenuItem();
        jSeparator24 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSchPerDiem = new javax.swing.JMenuItem();
        jSeparator23 = new javax.swing.JPopupMenu.Separator();
        jMenuItemView = new javax.swing.JMenuItem();
        jMenuAcquittal = new javax.swing.JMenu();
        jMenuItemAcqSupApp = new javax.swing.JMenuItem();
        jSeparator25 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqAccApp = new javax.swing.JMenuItem();
        jSeparator27 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAcqHeadApp = new javax.swing.JMenuItem();
        jSeparator36 = new javax.swing.JPopupMenu.Separator();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("USER PERDIEM ACQUITTAL DETAILS");
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
        jLabelGenTime.setBounds(1210, 0, 80, 30);

        jLabelGenLogNam1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam1.setText("Logged In");
        jPanelGen.add(jLabelGenLogNam1);
        jLabelGenLogNam1.setBounds(1070, 40, 100, 30);

        jLabelGenLogNam.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam.setForeground(new java.awt.Color(255, 255, 255));
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

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 204));
        jLabel4.setText("Financial Details");
        jPanelGen.add(jLabel4);
        jLabel4.setBounds(20, 310, 110, 30);

        jLabelBudgetCode.setText("Budget Code");
        jPanelGen.add(jLabelBudgetCode);
        jLabelBudgetCode.setBounds(20, 350, 80, 30);

        jLabelActMainPurposeDesc.setText("Activity Main Purpose");
        jPanelGen.add(jLabelActMainPurposeDesc);
        jLabelActMainPurposeDesc.setBounds(20, 410, 130, 30);

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

        jLabelAcqBreakFastSubTot.setForeground(new java.awt.Color(255, 0, 0));
        jLabelAcqBreakFastSubTot.setText("0.00");
        jPanel1.add(jLabelAcqBreakFastSubTot);
        jLabelAcqBreakFastSubTot.setBounds(170, 30, 50, 25);

        jLabelLunchSubTot.setText("0.00");
        jPanel1.add(jLabelLunchSubTot);
        jLabelLunchSubTot.setBounds(100, 60, 50, 25);

        jLabelDinnerSubTot.setText("0.00");
        jPanel1.add(jLabelDinnerSubTot);
        jLabelDinnerSubTot.setBounds(100, 90, 50, 25);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel1.add(jSeparator3);
        jSeparator3.setBounds(150, 20, 5, 120);

        jLabelBreakFastSubTot.setText("0.00");
        jPanel1.add(jLabelBreakFastSubTot);
        jLabelBreakFastSubTot.setBounds(100, 30, 50, 25);

        jLabelAcqLunchSubTot.setForeground(new java.awt.Color(255, 51, 51));
        jLabelAcqLunchSubTot.setText("0.00");
        jPanel1.add(jLabelAcqLunchSubTot);
        jLabelAcqLunchSubTot.setBounds(170, 60, 50, 25);

        jLabelAcqDinnerSubTot.setForeground(new java.awt.Color(255, 51, 51));
        jLabelAcqDinnerSubTot.setText("0.00");
        jPanel1.add(jLabelAcqDinnerSubTot);
        jLabelAcqDinnerSubTot.setBounds(170, 90, 50, 25);

        jLabelAcqIncidentalSubTot.setForeground(new java.awt.Color(255, 51, 51));
        jLabelAcqIncidentalSubTot.setText("0.00");
        jPanel1.add(jLabelAcqIncidentalSubTot);
        jLabelAcqIncidentalSubTot.setBounds(170, 120, 50, 25);

        jLabelAllReq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAllReq.setForeground(new java.awt.Color(0, 102, 0));
        jLabelAllReq.setText("Req");
        jPanel1.add(jLabelAllReq);
        jLabelAllReq.setBounds(100, 5, 22, 25);

        jLabelAllAcq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAllAcq.setForeground(new java.awt.Color(255, 0, 0));
        jLabelAllAcq.setText("Acq");
        jPanel1.add(jLabelAllAcq);
        jLabelAllAcq.setBounds(170, 5, 21, 25);

        jLabelAllBal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAllBal.setText("Balance");
        jPanel1.add(jLabelAllBal);
        jLabelAllBal.setBounds(240, 5, 60, 25);

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel1.add(jSeparator6);
        jSeparator6.setBounds(220, 20, 2, 120);

        jLabelAcqBreakFastSubTotBal.setText("0.00");
        jPanel1.add(jLabelAcqBreakFastSubTotBal);
        jLabelAcqBreakFastSubTotBal.setBounds(230, 30, 50, 25);

        jLabelAcqLunchSubTotBal.setText("0.00");
        jPanel1.add(jLabelAcqLunchSubTotBal);
        jLabelAcqLunchSubTotBal.setBounds(230, 60, 50, 25);

        jLabelAcqDinnerSubTotBal.setText("0.00");
        jPanel1.add(jLabelAcqDinnerSubTotBal);
        jLabelAcqDinnerSubTotBal.setBounds(230, 90, 50, 25);

        jLabelAcqIncidentalSubTotBal.setText("0.00");
        jPanel1.add(jLabelAcqIncidentalSubTotBal);
        jLabelAcqIncidentalSubTotBal.setBounds(230, 120, 50, 25);

        jPanelGen.add(jPanel1);
        jPanel1.setBounds(30, 460, 310, 150);

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

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel3.add(jSeparator4);
        jSeparator4.setBounds(168, 20, 5, 120);

        jLabelAcqMiscSubTot.setForeground(new java.awt.Color(255, 51, 51));
        jLabelAcqMiscSubTot.setText("0.00");
        jPanel3.add(jLabelAcqMiscSubTot);
        jLabelAcqMiscSubTot.setBounds(180, 30, 50, 25);

        jLabelMiscReq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelMiscReq.setForeground(new java.awt.Color(0, 102, 0));
        jLabelMiscReq.setText("Req");
        jPanel3.add(jLabelMiscReq);
        jLabelMiscReq.setBounds(110, 5, 22, 25);

        jLabelMiscAcq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelMiscAcq.setForeground(new java.awt.Color(255, 0, 0));
        jLabelMiscAcq.setText("Acq");
        jPanel3.add(jLabelMiscAcq);
        jLabelMiscAcq.setBounds(180, 5, 21, 25);

        jSeparator7.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel3.add(jSeparator7);
        jSeparator7.setBounds(230, 20, 2, 120);

        jLabelMscBal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelMscBal.setText("Balance");
        jPanel3.add(jLabelMscBal);
        jLabelMscBal.setBounds(250, 5, 50, 25);

        jLabelAcqMiscSubTotBal.setForeground(new java.awt.Color(51, 51, 51));
        jLabelAcqMiscSubTotBal.setText("0.00");
        jPanel3.add(jLabelAcqMiscSubTotBal);
        jLabelAcqMiscSubTotBal.setBounds(250, 30, 50, 25);

        jPanelGen.add(jPanel3);
        jPanel3.setBounds(380, 460, 310, 150);

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

        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel4.add(jSeparator5);
        jSeparator5.setBounds(160, 20, 5, 120);

        jLabelAcqAccUnprovedSubTotBal.setText("0.00");
        jPanel4.add(jLabelAcqAccUnprovedSubTotBal);
        jLabelAcqAccUnprovedSubTotBal.setBounds(250, 30, 50, 25);

        jLabelAcqAccProvedSubTot.setForeground(new java.awt.Color(255, 51, 51));
        jLabelAcqAccProvedSubTot.setText("0.00");
        jPanel4.add(jLabelAcqAccProvedSubTot);
        jLabelAcqAccProvedSubTot.setBounds(170, 60, 50, 25);

        jSeparator8.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel4.add(jSeparator8);
        jSeparator8.setBounds(240, 20, 5, 120);

        jLabelAccAcq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAccAcq.setForeground(new java.awt.Color(255, 51, 51));
        jLabelAccAcq.setText("Acq");
        jPanel4.add(jLabelAccAcq);
        jLabelAccAcq.setBounds(170, 5, 21, 25);

        jLabelAccBal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAccBal.setText("Balance");
        jPanel4.add(jLabelAccBal);
        jLabelAccBal.setBounds(252, 5, 60, 25);

        jLabelAccReq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAccReq.setForeground(new java.awt.Color(0, 102, 0));
        jLabelAccReq.setText("Req");
        jPanel4.add(jLabelAccReq);
        jLabelAccReq.setBounds(110, 5, 30, 25);

        jLabelAcqAccUnprovedSubTot.setForeground(new java.awt.Color(255, 51, 51));
        jLabelAcqAccUnprovedSubTot.setText("0.00");
        jPanel4.add(jLabelAcqAccUnprovedSubTot);
        jLabelAcqAccUnprovedSubTot.setBounds(170, 30, 50, 25);

        jLabelAcqAccProvedSubTotBal.setText("0.00");
        jPanel4.add(jLabelAcqAccProvedSubTotBal);
        jLabelAcqAccProvedSubTotBal.setBounds(250, 60, 50, 25);

        jPanelGen.add(jPanel4);
        jPanel4.setBounds(720, 460, 320, 150);

        jLabelAcqAppTotReqCost.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelAcqAppTotReqCost.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAcqAppTotReqCost.setText("0");
        jPanelGen.add(jLabelAcqAppTotReqCost);
        jLabelAcqAppTotReqCost.setBounds(1260, 590, 60, 30);

        jLabel35.setText("Miscellaneous");
        jPanelGen.add(jLabel35);
        jLabel35.setBounds(8, 30, 70, 25);

        jLabelAppTotReq.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelAppTotReq.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAppTotReq.setText("Total ");
        jPanelGen.add(jLabelAppTotReq);
        jLabelAppTotReq.setBounds(1100, 590, 140, 30);

        jLabelOffice.setText("Office");
        jPanelGen.add(jLabelOffice);
        jLabelOffice.setBounds(550, 230, 70, 30);

        jLabelAcqAccNum.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqAccNum);
        jLabelAcqAccNum.setBounds(680, 260, 130, 30);

        jLabelACC.setText("Account No.");
        jPanelGen.add(jLabelACC);
        jLabelACC.setBounds(550, 260, 80, 30);

        jLabelNum.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNum.setText("Request No.");
        jPanelGen.add(jLabelNum);
        jLabelNum.setBounds(20, 130, 90, 30);

        jLabelReqSerial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelReqSerial.setForeground(new java.awt.Color(255, 255, 255));
        jLabelReqSerial.setText("R");
        jPanelGen.add(jLabelReqSerial);
        jLabelReqSerial.setBounds(160, 130, 20, 30);

        jLabelAcqActMainPurpose.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqActMainPurpose);
        jLabelAcqActMainPurpose.setBounds(160, 410, 750, 30);

        jLabelAcqProvince.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqProvince);
        jLabelAcqProvince.setBounds(130, 230, 210, 30);

        jLabelAcqOffice.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqOffice);
        jLabelAcqOffice.setBounds(680, 230, 210, 30);

        jLabelAcqBudCode.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqBudCode);
        jLabelAcqBudCode.setBounds(160, 350, 750, 30);

        jLabelAcqEmpNum.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAcqEmpNum);
        jLabelAcqEmpNum.setBounds(130, 200, 60, 30);

        jLabelRegDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelRegDate.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelRegDate);
        jLabelRegDate.setBounds(220, 130, 110, 30);
        jPanelGen.add(jLabelEmp);
        jLabelEmp.setBounds(1140, 80, 80, 30);

        jLabelReqYear.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelReqYear.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelReqYear);
        jLabelReqYear.setBounds(110, 130, 40, 30);

        jLabelAcqRegDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqRegDate.setForeground(new java.awt.Color(255, 0, 0));
        jPanelGen.add(jLabelAcqRegDate);
        jLabelAcqRegDate.setBounds(820, 130, 110, 30);

        jLabelAcqRefNum.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqRefNum.setForeground(new java.awt.Color(255, 0, 0));
        jPanelGen.add(jLabelAcqRefNum);
        jLabelAcqRefNum.setBounds(770, 130, 50, 30);

        jLabelAcqSerial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqSerial.setForeground(new java.awt.Color(255, 0, 0));
        jLabelAcqSerial.setText("A");
        jPanelGen.add(jLabelAcqSerial);
        jLabelAcqSerial.setBounds(750, 130, 20, 30);

        jLabelNumAcq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNumAcq.setText("Acquittal  No.");
        jPanelGen.add(jLabelNumAcq);
        jLabelNumAcq.setBounds(610, 130, 90, 30);

        jLabelAcqYear.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAcqYear.setForeground(new java.awt.Color(255, 0, 0));
        jPanelGen.add(jLabelAcqYear);
        jLabelAcqYear.setBounds(700, 130, 40, 30);

        jLabelReqRefNum.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelReqRefNum.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelReqRefNum);
        jLabelReqRefNum.setBounds(180, 130, 30, 30);

        jTextAreaComments.setColumns(20);
        jTextAreaComments.setRows(5);
        jScrollPane3.setViewportView(jTextAreaComments);

        jPanelGen.add(jScrollPane3);
        jScrollPane3.setBounds(1060, 460, 280, 130);

        jLabelComments.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelComments.setText("Comments");
        jPanelGen.add(jLabelComments);
        jLabelComments.setBounds(1070, 435, 170, 20);

        jLabelAuthoriser.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAuthoriser.setText("Authoriser/s");
        jPanelGen.add(jLabelAuthoriser);
        jLabelAuthoriser.setBounds(1060, 340, 150, 25);

        jLabelAuthNam1.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAuthNam1);
        jLabelAuthNam1.setBounds(1060, 370, 260, 25);

        jLabelAuthNam2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAuthNam2);
        jLabelAuthNam2.setBounds(1060, 400, 220, 25);

        jLabelAcqAmtPayBack.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabelAcqAmtPayBack.setForeground(new java.awt.Color(255, 180, 40));
        jPanelGen.add(jLabelAcqAmtPayBack);
        jLabelAcqAmtPayBack.setBounds(1260, 620, 60, 30);

        jLabelAcqPayBack.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabelAcqPayBack.setForeground(new java.awt.Color(255, 180, 40));
        jPanelGen.add(jLabelAcqPayBack);
        jLabelAcqPayBack.setBounds(1080, 620, 170, 30);

        jLabelSpecial.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabelSpecial.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSpecial.setText("Special  Perdiem Acquittal");
        jPanelGen.add(jLabelSpecial);
        jLabelSpecial.setBounds(550, 80, 290, 30);

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

        jPanelRequest.setBackground(new java.awt.Color(100, 100, 247));
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
        jLabelLineDate.setText("29 November 2018");
        jPanelRequest.add(jLabelLineDate);
        jLabelLineDate.setBounds(1070, 0, 110, 30);

        jLabelLineTime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime.setText("15:20:30");
        jPanelRequest.add(jLabelLineTime);
        jLabelLineTime.setBounds(1210, 0, 80, 30);

        jLabellogged.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged.setText("Logged In");
        jPanelRequest.add(jLabellogged);
        jLabellogged.setBounds(1070, 40, 80, 30);

        jLabelLineLogNam.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam.setText("User Name");
        jPanelRequest.add(jLabelLineLogNam);
        jLabelLineLogNam.setBounds(1155, 40, 190, 30);

        jTableActivityReq.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jTableActivityReq.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableActivityReq.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableActivityReq.setGridColor(new java.awt.Color(255, 255, 255));
        jTableActivityReq.setMinimumSize(new java.awt.Dimension(180, 64));
        jTableActivityReq.setRowHeight(40);
        jTableActivityReq.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableActivityReqMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableActivityReq);

        jPanelRequest.add(jScrollPane1);
        jScrollPane1.setBounds(0, 115, 1340, 525);

        jLabelSpecial1.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabelSpecial1.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSpecial1.setText("Special  Perdiem Acquittal");
        jPanelRequest.add(jLabelSpecial1);
        jLabelSpecial1.setBounds(550, 80, 290, 30);

        jTabbedPaneAppSys.addTab("Perdiem Request", jPanelRequest);

        jPanelAcquittal.setBackground(new java.awt.Color(100, 100, 247));
        jPanelAcquittal.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jPanelAcquittal.setPreferredSize(new java.awt.Dimension(1280, 677));
        jPanelAcquittal.setLayout(null);

        jLabelLogo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ophidlog.jpg"))); // NOI18N
        jPanelAcquittal.add(jLabelLogo2);
        jLabelLogo2.setBounds(10, 10, 220, 100);

        jLabelHeaderLine1.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine1.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelAcquittal.add(jLabelHeaderLine1);
        jLabelHeaderLine1.setBounds(350, 40, 610, 40);

        jLabelLineDate1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate1.setText("29 November 2018");
        jPanelAcquittal.add(jLabelLineDate1);
        jLabelLineDate1.setBounds(1030, 0, 120, 30);

        jLabelLineTime1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime1.setText("15:20:30");
        jPanelAcquittal.add(jLabelLineTime1);
        jLabelLineTime1.setBounds(1170, 0, 90, 30);

        jLabellogged1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged1.setText("Logged In");
        jPanelAcquittal.add(jLabellogged1);
        jLabellogged1.setBounds(1040, 40, 90, 30);

        jLabelLineLogNam1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam1.setText("User Name");
        jPanelAcquittal.add(jLabelLineLogNam1);
        jLabelLineLogNam1.setBounds(1155, 40, 190, 30);

        jTableActivityAcq.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jTableActivityAcq.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableActivityAcq.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableActivityAcq.setGridColor(new java.awt.Color(255, 255, 255));
        jTableActivityAcq.setMinimumSize(new java.awt.Dimension(180, 64));
        jTableActivityAcq.setRowHeight(40);
        jTableActivityAcq.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTableActivityAcqFocusLost(evt);
            }
        });
        jTableActivityAcq.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableActivityAcqMouseClicked(evt);
            }
        });
        jTableActivityAcq.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTableActivityAcqKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTableActivityAcqKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(jTableActivityAcq);

        jPanelAcquittal.add(jScrollPane2);
        jScrollPane2.setBounds(0, 115, 1340, 530);

        jLabelSpecial2.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabelSpecial2.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSpecial2.setText("Special  Perdiem Acquittal");
        jPanelAcquittal.add(jLabelSpecial2);
        jLabelSpecial2.setBounds(485, 80, 290, 30);

        jTabbedPaneAppSys.addTab("Perdiem Acquittal", jPanelAcquittal);

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
        jMenuFile.add(jSeparator10);

        jMenuItemClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemClose.setText("Close");
        jMenuItemClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemClose);
        jMenuFile.add(jSeparator11);

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
        jMenuRequest.add(jSeparator12);

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
        jMenuRequest.add(jSeparator24);

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
        jMenuAcquittal.add(jSeparator25);

        jMenuItemAcqAccApp.setText("Finance Team Approval");
        jMenuItemAcqAccApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqAccAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqAccApp);
        jMenuAcquittal.add(jSeparator27);

        jMenuItemAcqHeadApp.setText("Project HOD Approval");
        jMenuItemAcqHeadApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcqHeadAppActionPerformed(evt);
            }
        });
        jMenuAcquittal.add(jMenuItemAcqHeadApp);
        jMenuAcquittal.add(jSeparator36);

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jTabbedPaneAppSys, javax.swing.GroupLayout.DEFAULT_SIZE, 1350, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneAppSys, javax.swing.GroupLayout.DEFAULT_SIZE, 680, Short.MAX_VALUE)
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


    private void jTableActivityReqMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableActivityReqMouseClicked

        int row = jTableActivityReq.getSelectedRow();
        int col = jTableActivityReq.getSelectedColumn();
        Object id = jTableActivityReq.getValueAt(row, col);
        String tt = id.toString();


    }//GEN-LAST:event_jTableActivityReqMouseClicked

    private void jTableActivityAcqFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTableActivityAcqFocusLost
        mainPageTotUpdateAcq();
    }//GEN-LAST:event_jTableActivityAcqFocusLost

    private void jTableActivityAcqMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableActivityAcqMouseClicked
        int row = jTableActivityAcq.getSelectedRow();
        totSeg1 = 0;
        totSeg2 = 0;
        for (int i = 4; i < 8; i++) {

            if (jTableActivityAcq.getValueAt(row, i).toString().trim().length() == 0) {
                jTableActivityAcq.setValueAt("0", row, i);
            }
            totSeg1 = totSeg1 + (Double.parseDouble((String) jTableActivityAcq.getValueAt(row, i)));

        }
        for (int i = 9; i < 12; i++) {

            if (jTableActivityAcq.getValueAt(row, i).toString().trim().length() == 0) {
                jTableActivityAcq.setValueAt("0", row, i);
            }

            totSeg2 = totSeg2 + (Double.parseDouble((String) jTableActivityAcq.getValueAt(row, i)));

        }
        jTableActivityAcq.setValueAt((totSeg1 + totSeg2), row, 12);
    }//GEN-LAST:event_jTableActivityAcqMouseClicked

    private void jTableActivityAcqKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableActivityAcqKeyTyped

    }//GEN-LAST:event_jTableActivityAcqKeyTyped

    private void jTableActivityAcqKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableActivityAcqKeyReleased
        int row = jTableActivityAcq.getSelectedRow();
        totSeg1 = 0;
        totSeg2 = 0;
        for (int i = 4; i < 8; i++) {

            if (jTableActivityAcq.getValueAt(row, i).toString().trim().length() == 0) {
                jTableActivityAcq.setValueAt("0", row, i);
            }
            totSeg1 = totSeg1 + (Double.parseDouble((String) jTableActivityAcq.getValueAt(row, i)));

        }
        for (int i = 9; i < 12; i++) {

            if (jTableActivityAcq.getValueAt(row, i).toString().trim().length() == 0) {
                jTableActivityAcq.setValueAt("0", row, i);

            }

            totSeg2 = totSeg2 + (Double.parseDouble((String) jTableActivityAcq.getValueAt(row, i)));

        }
        jTableActivityAcq.setValueAt((totSeg1 + totSeg2), row, 12);
    }//GEN-LAST:event_jTableActivityAcqKeyReleased

    private void jMenuPlanApprovalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuPlanApprovalActionPerformed

        setVisible(false);
    }//GEN-LAST:event_jMenuPlanApprovalActionPerformed

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
            java.util.logging.Logger.getLogger(JFrameAppAllUserAcquittal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameAppAllUserAcquittal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameAppAllUserAcquittal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameAppAllUserAcquittal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new JFrameAppAllUserAcquittal().setVisible(true);

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
    private javax.swing.JLabel jLabelAcqAmtPayBack;
    private javax.swing.JLabel jLabelAcqAppTotReqCost;
    private javax.swing.JLabel jLabelAcqBankName;
    private javax.swing.JLabel jLabelAcqBreakFastSubTot;
    private javax.swing.JLabel jLabelAcqBreakFastSubTotBal;
    private javax.swing.JLabel jLabelAcqBudCode;
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
    private javax.swing.JLabel jLabelAcqPayBack;
    private javax.swing.JLabel jLabelAcqProvince;
    private javax.swing.JLabel jLabelAcqRefNum;
    private javax.swing.JLabel jLabelAcqRegDate;
    private javax.swing.JLabel jLabelAcqSerial;
    private javax.swing.JLabel jLabelAcqYear;
    private javax.swing.JLabel jLabelActMainPurposeDesc;
    private javax.swing.JLabel jLabelAllAcq;
    private javax.swing.JLabel jLabelAllBal;
    private javax.swing.JLabel jLabelAllReq;
    private javax.swing.JLabel jLabelAppTotReq;
    private javax.swing.JLabel jLabelAuthNam1;
    private javax.swing.JLabel jLabelAuthNam2;
    private javax.swing.JLabel jLabelAuthoriser;
    private javax.swing.JLabel jLabelBank;
    private javax.swing.JLabel jLabelBreakFastSub;
    private javax.swing.JLabel jLabelBreakFastSubTot;
    private javax.swing.JLabel jLabelBudgetCode;
    private javax.swing.JLabel jLabelComments;
    private javax.swing.JLabel jLabelDinnerSub;
    private javax.swing.JLabel jLabelDinnerSubTot;
    private javax.swing.JLabel jLabelEmp;
    private javax.swing.JLabel jLabelEmpNum;
    private javax.swing.JLabel jLabelGenDate;
    private javax.swing.JLabel jLabelGenLogNam;
    private javax.swing.JLabel jLabelGenLogNam1;
    private javax.swing.JLabel jLabelGenTime;
    private javax.swing.JLabel jLabelHeaderGen;
    private javax.swing.JLabel jLabelHeaderLine;
    private javax.swing.JLabel jLabelHeaderLine1;
    private javax.swing.JLabel jLabelIncidentalSub;
    private javax.swing.JLabel jLabelIncidentalSubTot;
    private javax.swing.JLabel jLabelLineDate;
    private javax.swing.JLabel jLabelLineDate1;
    private javax.swing.JLabel jLabelLineLogNam;
    private javax.swing.JLabel jLabelLineLogNam1;
    private javax.swing.JLabel jLabelLineTime;
    private javax.swing.JLabel jLabelLineTime1;
    private javax.swing.JLabel jLabelLogo;
    private javax.swing.JLabel jLabelLogo1;
    private javax.swing.JLabel jLabelLogo2;
    private javax.swing.JLabel jLabelLunchSub;
    private javax.swing.JLabel jLabelLunchSubTot;
    private javax.swing.JLabel jLabelMiscAcq;
    private javax.swing.JLabel jLabelMiscReq;
    private javax.swing.JLabel jLabelMiscSubTot;
    private javax.swing.JLabel jLabelMscBal;
    private javax.swing.JLabel jLabelMscSub;
    private javax.swing.JLabel jLabelNum;
    private javax.swing.JLabel jLabelNumAcq;
    private javax.swing.JLabel jLabelOffice;
    private javax.swing.JLabel jLabelProvince;
    private javax.swing.JLabel jLabelRegDate;
    private javax.swing.JLabel jLabelReqRefNum;
    private javax.swing.JLabel jLabelReqSerial;
    private javax.swing.JLabel jLabelReqYear;
    private javax.swing.JLabel jLabelSepDetails;
    private javax.swing.JLabel jLabelSpecial;
    private javax.swing.JLabel jLabelSpecial1;
    private javax.swing.JLabel jLabelSpecial2;
    private javax.swing.JLabel jLabelStatusView;
    private javax.swing.JLabel jLabellogged;
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelAcquittal;
    private javax.swing.JPanel jPanelGen;
    private javax.swing.JPanel jPanelRequest;
    private javax.swing.JPanel jPanelStatusView;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator11;
    private javax.swing.JPopupMenu.Separator jSeparator12;
    private javax.swing.JPopupMenu.Separator jSeparator13;
    private javax.swing.JPopupMenu.Separator jSeparator16;
    private javax.swing.JPopupMenu.Separator jSeparator17;
    private javax.swing.JPopupMenu.Separator jSeparator18;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator22;
    private javax.swing.JPopupMenu.Separator jSeparator23;
    private javax.swing.JPopupMenu.Separator jSeparator24;
    private javax.swing.JPopupMenu.Separator jSeparator25;
    private javax.swing.JPopupMenu.Separator jSeparator26;
    private javax.swing.JPopupMenu.Separator jSeparator27;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator30;
    private javax.swing.JPopupMenu.Separator jSeparator34;
    private javax.swing.JPopupMenu.Separator jSeparator36;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator62;
    private javax.swing.JPopupMenu.Separator jSeparator63;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    public javax.swing.JTabbedPane jTabbedPaneAppSys;
    private javax.swing.JTable jTableActivityAcq;
    private javax.swing.JTable jTableActivityReq;
    private javax.swing.JTextArea jTextAreaComments;
    // End of variables declaration//GEN-END:variables
}
