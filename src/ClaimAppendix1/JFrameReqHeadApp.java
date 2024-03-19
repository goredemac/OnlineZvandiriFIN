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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import ClaimAppendix2.*;
import ClaimReports.*;
import ClaimPlan.*;
import java.awt.Image;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.WindowConstants;
import utils.connCred;
import utils.connSaveFile;
import utils.savePDFToDB;
import utils.StockVehicleMgt;
import utils.emailSend;

/**
 *
 * @author cgoredema
 */
public class JFrameReqHeadApp extends javax.swing.JFrame {

    connCred c = new connCred();
    emailSend emSend = new emailSend();
    PreparedStatement pst = null;
    PreparedStatement pst1 = null;
    List<String> list = new ArrayList<>();
    int itmNum = 1;
    int month, finyear;
    int initdocVersion = 0;
    int initactVersion = 0;
    int checkRefCount = 0;
    int countGen = 0;
    int countItm = 0;
    int countAct = 0;
    Date curYear = new Date();
    String sendToProv, sendToFin, reqUsrMail, finUsrMail, reqUsrNam, reqEmpNum, createUsrNam, docVersion,
            oldDocVersion, actVersion, statusCodeApp, statusCodeDisApp, checkRef,
            authNam1, authNam2, usrGrp, empOff, searchRef, finAppMailList;
    String province = "";
    SimpleDateFormat df = new SimpleDateFormat("yyyy");
    DefaultTableModel modelWk1, modelWk2, modelWk3, modelWk4, modelWk5;
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
    public JFrameReqHeadApp() {
        initComponents();
        showDate();
        showTime();
        modelWk1 = (DefaultTableModel) jTableWk1Activities.getModel();
        modelWk2 = (DefaultTableModel) jTableWk2Activities.getModel();
        modelWk3 = (DefaultTableModel) jTableWk3Activities.getModel();
        modelWk4 = (DefaultTableModel) jTableWk4Activities.getModel();
        modelWk5 = (DefaultTableModel) jTableWk5Activities.getModel();
        jLabelRegYear.setVisible(false);

    }

    public JFrameReqHeadApp(String ref, String usrLogNum) {
        initComponents();
        showDate();
        showTime();
        computerName();
        jButtonSave.setVisible(false);
        jTextAreaComments.setWrapStyleWord(true);
        jTextAreaComments.setLineWrap(true);
        jButtonClearBud.setVisible(false);
        jLabelBudUpd.setVisible(false);
        jLabelBudCod.setVisible(false);
        modelWk1 = (DefaultTableModel) jTableWk1Activities.getModel();
        modelWk2 = (DefaultTableModel) jTableWk2Activities.getModel();
        modelWk3 = (DefaultTableModel) jTableWk3Activities.getModel();
        modelWk4 = (DefaultTableModel) jTableWk4Activities.getModel();
        modelWk5 = (DefaultTableModel) jTableWk5Activities.getModel();
        jTableWk1Activities.getColumnModel().getColumn(0).setMinWidth(0);
        jTableWk1Activities.getColumnModel().getColumn(0).setMaxWidth(0);
        jTableWk2Activities.getColumnModel().getColumn(0).setMinWidth(0);
        jTableWk2Activities.getColumnModel().getColumn(0).setMaxWidth(0);
        jTableWk3Activities.getColumnModel().getColumn(0).setMinWidth(0);
        jTableWk3Activities.getColumnModel().getColumn(0).setMaxWidth(0);
        jTableWk4Activities.getColumnModel().getColumn(0).setMinWidth(0);
        jTableWk4Activities.getColumnModel().getColumn(0).setMaxWidth(0);
        jTableWk5Activities.getColumnModel().getColumn(0).setMinWidth(0);
        jTableWk5Activities.getColumnModel().getColumn(0).setMaxWidth(0);
        jTableWk1Activities.getTableHeader().setReorderingAllowed(false);
        jTableWk2Activities.getTableHeader().setReorderingAllowed(false);
        jTableWk3Activities.getTableHeader().setReorderingAllowed(false);
        jTableWk4Activities.getTableHeader().setReorderingAllowed(false);
        jTableWk5Activities.getTableHeader().setReorderingAllowed(false);
        jLabelRegYear.setVisible(false);
        jLabelEmp.setVisible(false);
        jLabelEmp.setText(usrLogNum);
        searchRef = ref;
        findUser();
        findUserGrp();
        fetchGenData();
        fetchdataWk1();
        fetchdataWk2();
        fetchdataWk3();
        fetchdataWk4();
        fetchdataWk5();
        imgDisplay();
        mainPageTotInsert();
        findApplicantUser();
        findFinApprove();

        if (!"Administrator".equals(usrGrp)) {
            jMenuItemUserProfUpd.setEnabled(false);
        }
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
                jLabelLineTime2.setText(s.format(d));
                jLabelLineTime3.setText(s.format(d));
            }
        }) {

        }.start();

    }

    void showDate() {
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        jLabelGenDate.setText(s.format(d));
        jLabelLineDate.setText(s.format(d));
        jLabelLineDate2.setText(s.format(d));
        jLabelLineDate3.setText(s.format(d));

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
                        + "(SELECT CONCAT(DONOR,PRJ_CODE_GL) FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                        + "where concat(SERIAL,REF_NUM)='" + searchRef + "'   and ACT_REC_STA = 'A' and ITM_NUM = 1   )) and DEPT ='FINANCE')");

                while (r.next()) {
                    list.add(r.getString(1));
                    System.out.println("list " + r.getString(1));
                }
            } else if (modelWk2.getRowCount() > 0) {
                ResultSet r = st.executeQuery("SELECT EMP_MAIL FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] "
                        + "where EMP_NUM in (SELECT EMP_NUM FROM [ClaimsAppSysZvandiri].[dbo].[PrjFinHODTab] "
                        + "where CONCAT(DONOR_CODE,PRJ_CODE_GL)=(SELECT concat(DONOR_CODE,PRJ_CODE) "
                        + "FROM [ClaimsAppSysZvandiri].[dbo].[BudDonPrjTab] where concat(DONOR_DESC,PRJ_DESC) = "
                        + "(SELECT CONCAT(DONOR,PRJ_CODE_GL) FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk2Tab] "
                        + "where concat(SERIAL,REF_NUM)='" + searchRef + "'  and ACT_REC_STA = 'A' and ITM_NUM = 1   )) and DEPT ='FINANCE')");

                while (r.next()) {
                    list.add(r.getString(1));
                    System.out.println("list " + r.getString(1));
                }
            } else if (modelWk3.getRowCount() > 0) {
                ResultSet r = st.executeQuery("SELECT EMP_MAIL FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] "
                        + "where EMP_NUM in (SELECT EMP_NUM FROM [ClaimsAppSysZvandiri].[dbo].[PrjFinHODTab] "
                        + "where CONCAT(DONOR_CODE,PRJ_CODE_GL)=(SELECT concat(DONOR_CODE,PRJ_CODE) "
                        + "FROM [ClaimsAppSysZvandiri].[dbo].[BudDonPrjTab] where concat(DONOR_DESC,PRJ_DESC) = "
                        + "(SELECT CONCAT(DONOR,PRJ_CODE_GL) FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk3Tab] "
                        + "where concat(SERIAL,REF_NUM)='" + searchRef + "'   and ACT_REC_STA = 'A' and ITM_NUM = 1   )) and DEPT ='FINANCE')");

                while (r.next()) {
                    list.add(r.getString(1));
                    System.out.println("list " + r.getString(1));
                }
            } else if (modelWk4.getRowCount() > 0) {
                ResultSet r = st.executeQuery("SELECT EMP_MAIL FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] "
                        + "where EMP_NUM in (SELECT EMP_NUM FROM [ClaimsAppSysZvandiri].[dbo].[PrjFinHODTab] "
                        + "where CONCAT(DONOR_CODE,PRJ_CODE_GL)=(SELECT concat(DONOR_CODE,PRJ_CODE) "
                        + "FROM [ClaimsAppSysZvandiri].[dbo].[BudDonPrjTab] where concat(DONOR_DESC,PRJ_DESC) = "
                        + "(SELECT CONCAT(DONOR,PRJ_CODE_GL) FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk4Tab] "
                        + "where concat(SERIAL,REF_NUM)='" + searchRef + "'   and ACT_REC_STA = 'A' and ITM_NUM = 1   )) and DEPT ='FINANCE')");

                while (r.next()) {
                    list.add(r.getString(1));
                    System.out.println("list " + r.getString(1));
                }
            } else if (modelWk5.getRowCount() > 0) {
                ResultSet r = st.executeQuery("SELECT EMP_MAIL FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] "
                        + "where EMP_NUM in (SELECT EMP_NUM FROM [ClaimsAppSysZvandiri].[dbo].[PrjFinHODTab] "
                        + "where CONCAT(DONOR_CODE,PRJ_CODE_GL)=(SELECT concat(DONOR_CODE,PRJ_CODE) "
                        + "FROM [ClaimsAppSysZvandiri].[dbo].[BudDonPrjTab] where concat(DONOR_DESC,PRJ_DESC) = "
                        + "(SELECT CONCAT(DONOR,PRJ_CODE_GL) FROM [ClaimsAppSysZvandiri].[dbo].[PlanWk5Tab] "
                        + "where concat(SERIAL,REF_NUM)='" + searchRef + "'   and ACT_REC_STA = 'A' and ITM_NUM = 1   )) and DEPT ='FINANCE')");

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

    void findApplicantUser() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select EMP_NAM,EMP_MAIL,EMP_NUM from [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] "
                    + "where EMP_NUM=(SELECT ACTIONED_BY_EMP_NUM FROM [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab]"
                    + " where concat(SERIAL,REF_NUM)='" + searchRef + "'  and DOC_VER=1)");

            while (r.next()) {
                reqUsrNam = r.getString(1);
                reqUsrMail = r.getString(2);
                reqEmpNum = r.getString(3);

            }

            //                 conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findUser() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select EMP_NAM from [ClaimsAppSysZvandiri].[dbo]."
                    + "[EmpDetTab] where emp_num ='" + jLabelEmp.getText() + "'");

            while (r.next()) {

                jLabelGenLogNam.setText(r.getString(1));
                jLabelLinLogNam.setText(r.getString(1));
                jLabelLineLogNam1.setText(r.getString(1));
                jLabelLinLogNam2.setText(r.getString(1));
                jLabelLinLogNam3.setText(r.getString(1));
                createUsrNam = r.getString(1);

            }

            //                .close();
        } catch (Exception e) {
            System.out.println(e);
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

//    void sendMail() {
//
//        final String username = mailUsrNam;
//        final String password = mailUsrPass;
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", "smtp.office365.com");
//        props.put("mail.smtp.port", "587");
//        Session session = Session.getInstance(props,
//                new javax.mail.Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(username, password);
//            }
//        });
//        try {
//            jDialogWaitingEmail.setVisible(true);
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(mailUsrNam));
//            message.setRecipients(Message.RecipientType.TO,
//                    InternetAddress.parse(finUsrMail));
//            message.addRecipients(Message.RecipientType.CC,
//                    InternetAddress.parse(reqUsrMail));
//
//            message.setSubject("Checking Request.Perdiem Request Application Reference # R " + jLabelRegNum.getText());
//
//            message.setContent("<html><body> Dear " + sendToFin + "<br><br> A perdiem request No. R " + jLabelRegNum.getText()
//                    + " for " + jLabelEmpNam.getText() + " has been sent to your inbox for checking. <br><br>Please check "
//                    + "your finance officer inbox list and action.<br><br> Kind Regards <br><br>"
//                    + " Perdiem Management System </body></html>", "text/html;charset=utf-8");
//
//            Transport.send(message);
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    void sendMailReject() {
//
//        final String username = mailUsrNam;
//        final String password = mailUsrPass;
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", "smtp.office365.com");
//        props.put("mail.smtp.port", "587");
//        Session session = Session.getInstance(props,
//                new javax.mail.Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(username, password);
//            }
//        });
//        try {
//            jDialogWaitingEmail.setVisible(true);
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(mailUsrNam));
//            message.setRecipients(Message.RecipientType.TO,
//                    InternetAddress.parse(reqUsrMail));
//
//            message.setSubject("Request Rejection.Perdiem Request Application Reference # R " + jLabelRegNum.getText());
//
//            message.setContent("<html><body> Dear " + jLabelEmpNam.getText() + "<br><br> Your perdiem request No. R " + jLabelRegNum.getText()
//                    + " has been rejected by " + jLabelGenLogNam.getText() + ".<br><br>"
//                    + "<b><u>Reason for rejection </u>: </b> " + jTextReasonReject.getText() + "<br><br><br> Kind Regards <br><br>"
//                    + " Perdiem Management System </body></html>", "text/html;charset=utf-8");
//
//            Transport.send(message);
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//
//    }
    void imgDisplay() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT SERIAL,REF_NUM,ATT_IMAGE,IMG_VERSION "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimDocAttachTab] where concat(SERIAL,REF_NUM)="
                    + "'" + jLabelSerial.getText() + jLabelRegNum.getText() + "'"
                    + " and IMG_VERSION = (SELECT MAX(IMG_VERSION) FROM"
                    + "[ClaimsAppSysZvandiri].[dbo].[ClaimDocAttachTab] WHERE concat(SERIAL,REF_NUM)="
                    + "'" + jLabelSerial.getText() + jLabelRegNum.getText() + "')");

            while (r.next()) {

                byte[] img = r.getBytes("ATT_IMAGE");

                ImageIcon imageIcon = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(600, 535, Image.SCALE_SMOOTH));
                jLabelImg.setIcon(imageIcon);

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void PlanReqClearUpd() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String planRefNum = "";

            Statement st = conn.createStatement();

            st.executeQuery("SELECT PLAN_REF_NUM FROM [ClaimsAppSysZvandiri].[dbo].[PlanReqClearTab]"
                    + " where concat(REQ_SERIAL,REF_NUM) = '" + jLabelSerial.getText() + jLabelRegNum.getText() + "'");

            ResultSet r = st.getResultSet();

            while (r.next()) {

                planRefNum = r.getString(1);
            }

            String sqlUpdPlanReqClear = "update [ClaimsAppSysZvandiri].[dbo].[PlanReqClearTab] set "
                    + "ACT_REC_STA ='P' where PLAN_REF_NUM ='" + planRefNum + "' and ACT_REC_STA = 'A' ";

            pst = conn.prepareStatement(sqlUpdPlanReqClear);
            pst.executeUpdate();

            String sqlUpdPlanUsrRecTab = "update [ClaimsAppSysZvandiri].[dbo].[PlanUsrRecTab] set "
                    + "ACT_REC_STA = 'A' where PLAN_REF_NUM =" + planRefNum + " and ACT_REC_STA = 'P' and"
                    + " EMP_NAM='" + jLabelEmpNam.getText() + "'";

            pst = conn.prepareStatement(sqlUpdPlanUsrRecTab);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updRecTab() {

        try {
            jButtonSave.setEnabled(false);

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlGenUpdate = "UPDATE [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] set ACT_REC_STA ='P' where "
                    + "REF_NUM='" + jLabelRegNum.getText() + "' and DOC_VER ='" + oldDocVersion + "' and SERIAL ='R'";
            pst = conn.prepareStatement(sqlGenUpdate);
            pst.executeUpdate();

            String sqlItmUpdate = "UPDATE [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] set ACT_REC_STA ='P' where "
                    + "REF_NUM='" + jLabelRegNum.getText() + "' and DOC_VER ='" + oldDocVersion + "' and SERIAL ='R'";

            pst = conn.prepareStatement(sqlItmUpdate);
            pst.executeUpdate();

            String sqlWFUpdate = "UPDATE [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] set ACT_REC_STA ='P' where "
                    + "REF_NUM='" + jLabelRegNum.getText() + "' and DOC_VER ='" + oldDocVersion + "' and SERIAL ='R'";
            pst = conn.prepareStatement(sqlWFUpdate);
            pst.executeUpdate();

            insGenTab();

            insItmWk1Tab();

            insItmWk2Tab();

            insItmWk3Tab();

            insItmWk4Tab();

            insItmWk5Tab();

            createAction();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void updateRejComments() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlRejUpdate = "UPDATE [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] set REJECT_COMMENTS ='" + jTextReasonReject.getText() + "' where "
                    + "REF_NUM='" + jLabelRegNum.getText() + "' and DOC_VER ='" + docVersion + "' and SERIAL ='R'";

            pst = conn.prepareStatement(sqlRejUpdate);
            pst.executeUpdate();

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

    void mainPageTotInsert() {

        DecimalFormat numFormat;
        numFormat = new DecimalFormat("000.##");
        double breakfastsubtotal = 0;
        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            double breakfastamount = Double.parseDouble((String) jTableWk1Activities.getValueAt(i, 11));
            breakfastsubtotal += breakfastamount;
        }

        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            double breakfastamount = Double.parseDouble((String) jTableWk2Activities.getValueAt(i, 11));
            breakfastsubtotal += breakfastamount;
        }

        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            double breakfastamount = Double.parseDouble((String) jTableWk3Activities.getValueAt(i, 11));
            breakfastsubtotal += breakfastamount;
        }

        for (int i = 0; i < jTableWk4Activities.getRowCount(); i++) {
            double breakfastamount = Double.parseDouble((String) jTableWk4Activities.getValueAt(i, 11));
            breakfastsubtotal += breakfastamount;
        }

        for (int i = 0; i < jTableWk5Activities.getRowCount(); i++) {
            double breakfastamount = Double.parseDouble((String) jTableWk5Activities.getValueAt(i, 11));
            breakfastsubtotal += breakfastamount;
        }

        jLabelBreakFastSubTot.setText(String.format("%.2f", breakfastsubtotal));

        double lunchsubtotal = 0;
        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            double lunchamount = Double.parseDouble((String) jTableWk1Activities.getValueAt(i, 12));
            lunchsubtotal += lunchamount;
        }

        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            double lunchamount = Double.parseDouble((String) jTableWk2Activities.getValueAt(i, 12));
            lunchsubtotal += lunchamount;
        }

        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            double lunchamount = Double.parseDouble((String) jTableWk3Activities.getValueAt(i, 12));
            lunchsubtotal += lunchamount;
        }

        for (int i = 0; i < jTableWk4Activities.getRowCount(); i++) {
            double lunchamount = Double.parseDouble((String) jTableWk4Activities.getValueAt(i, 12));
            lunchsubtotal += lunchamount;
        }

        for (int i = 0; i < jTableWk5Activities.getRowCount(); i++) {
            double lunchamount = Double.parseDouble((String) jTableWk5Activities.getValueAt(i, 12));
            lunchsubtotal += lunchamount;
        }

        jLabelLunchSubTot.setText(String.format("%.2f", lunchsubtotal));

        double dinnersubtotal = 0;
        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            double dinneramount = Double.parseDouble((String) jTableWk1Activities.getValueAt(i, 13));
            dinnersubtotal += dinneramount;
        }

        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            double dinneramount = Double.parseDouble((String) jTableWk2Activities.getValueAt(i, 13));
            dinnersubtotal += dinneramount;
        }

        //jLabelDinnerSubTot.setText(Double.toString(dinnersubtotal));
        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            double dinneramount = Double.parseDouble((String) jTableWk3Activities.getValueAt(i, 13));
            dinnersubtotal += dinneramount;
        }

        for (int i = 0; i < jTableWk4Activities.getRowCount(); i++) {
            double dinneramount = Double.parseDouble((String) jTableWk4Activities.getValueAt(i, 13));
            dinnersubtotal += dinneramount;
        }

        for (int i = 0; i < jTableWk5Activities.getRowCount(); i++) {
            double dinneramount = Double.parseDouble((String) jTableWk5Activities.getValueAt(i, 13));
            dinnersubtotal += dinneramount;
        }

        jLabelDinnerSubTot.setText(String.format("%.2f", dinnersubtotal));

        double incidentalsubtotal = 0;
        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            double incidentalamount = Double.parseDouble((String) jTableWk1Activities.getValueAt(i, 14));
            incidentalsubtotal += incidentalamount;
        }

        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            double incidentalamount = Double.parseDouble((String) jTableWk2Activities.getValueAt(i, 14));
            incidentalsubtotal += incidentalamount;
        }

        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            double incidentalamount = Double.parseDouble((String) jTableWk3Activities.getValueAt(i, 14));
            incidentalsubtotal += incidentalamount;
        }

        for (int i = 0; i < jTableWk4Activities.getRowCount(); i++) {
            double incidentalamount = Double.parseDouble((String) jTableWk4Activities.getValueAt(i, 14));
            incidentalsubtotal += incidentalamount;
        }

        for (int i = 0; i < jTableWk5Activities.getRowCount(); i++) {
            double incidentalamount = Double.parseDouble((String) jTableWk5Activities.getValueAt(i, 14));
            incidentalsubtotal += incidentalamount;
        }

        jLabelIncidentalSubTot.setText(String.format("%.2f", incidentalsubtotal));

        double miscSubTot = 0;
        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            double miscamount = Double.parseDouble((String) jTableWk1Activities.getValueAt(i, 16));
            miscSubTot += miscamount;
        }

        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            double miscamount = Double.parseDouble((String) jTableWk2Activities.getValueAt(i, 16));
            miscSubTot += miscamount;
        }

        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            double miscamount = Double.parseDouble((String) jTableWk3Activities.getValueAt(i, 16));
            miscSubTot += miscamount;
        }

        for (int i = 0; i < jTableWk4Activities.getRowCount(); i++) {
            double miscamount = Double.parseDouble((String) jTableWk4Activities.getValueAt(i, 16));
            miscSubTot += miscamount;
        }

        for (int i = 0; i < jTableWk5Activities.getRowCount(); i++) {
            double miscamount = Double.parseDouble((String) jTableWk5Activities.getValueAt(i, 16));
            miscSubTot += miscamount;
        }

        jLabelMiscSubTot.setText(String.format("%.2f", miscSubTot));

        double unprovedSubTot = 0;
        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            double unprovedamount = Double.parseDouble((String) jTableWk1Activities.getValueAt(i, 17));
            unprovedSubTot += unprovedamount;
        }

        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            double unprovedamount = Double.parseDouble((String) jTableWk2Activities.getValueAt(i, 17));
            unprovedSubTot += unprovedamount;
        }

        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            double unprovedamount = Double.parseDouble((String) jTableWk3Activities.getValueAt(i, 17));
            unprovedSubTot += unprovedamount;
        }

        for (int i = 0; i < jTableWk4Activities.getRowCount(); i++) {
            double unprovedamount = Double.parseDouble((String) jTableWk4Activities.getValueAt(i, 17));
            unprovedSubTot += unprovedamount;
        }

        for (int i = 0; i < jTableWk5Activities.getRowCount(); i++) {
            double unprovedamount = Double.parseDouble((String) jTableWk5Activities.getValueAt(i, 17));
            unprovedSubTot += unprovedamount;
        }
        jLabelAccUnprovedSubTot.setText(String.format("%.2f", unprovedSubTot));

        double provedSubTot = 0;
        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            double provedamount = Double.parseDouble((String) jTableWk1Activities.getValueAt(i, 18));
            provedSubTot += provedamount;
        }

        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            double provedamount = Double.parseDouble((String) jTableWk2Activities.getValueAt(i, 18));
            provedSubTot += provedamount;
        }

        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            double provedamount = Double.parseDouble((String) jTableWk3Activities.getValueAt(i, 18));
            provedSubTot += provedamount;
        }

        for (int i = 0; i < jTableWk4Activities.getRowCount(); i++) {
            double provedamount = Double.parseDouble((String) jTableWk4Activities.getValueAt(i, 18));
            provedSubTot += provedamount;
        }

        for (int i = 0; i < jTableWk5Activities.getRowCount(); i++) {
            double provedamount = Double.parseDouble((String) jTableWk5Activities.getValueAt(i, 18));
            provedSubTot += provedamount;
        }
        jLabelAccProvedSubTot.setText(String.format("%.2f", provedSubTot));

        double allTotal = unprovedSubTot + miscSubTot + incidentalsubtotal
                + dinnersubtotal + lunchsubtotal + breakfastsubtotal + provedSubTot;
        numFormat.format(allTotal);

        jLabelAppTotReqCost.setText(String.format("%.2f", allTotal));

    }

    void fetchGenData() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            try {

                Statement st1 = conn.createStatement();

                st1.executeQuery("SELECT distinct a.REF_YEAR,a.SERIAL,a.REF_NUM,REF_DAT,a.EMP_NUM,"
                        + "a.EMP_NAM,a.EMP_TTL, a.EMP_PROV,a.EMP_OFF,a.EMP_BNK_NAM,a.ACC_NUM,a.ACT_MAIN_PUR, "
                        + "a.ACT_TOT_AMT,a.REG_MOD_VER,a.DOC_VER,a.DOC_VER +1  "
                        + "FROM ClaimsAppSysZvandiri.dbo.ClaimAppGenTab a,[ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] b "
                        + "where  ( a.REF_NUM = b.REF_NUM and a.DOC_VER=b.DOC_VER) and concat(a.SERIAL,a.REF_NUM)='" + searchRef + "'"
                        + " and a.DOC_VER =(select max(DOC_VER) from ClaimsAppSysZvandiri.dbo.ClaimAppGenTab "
                        + "where concat(SERIAL,REF_NUM)='" + searchRef + "') and a.ACT_REC_STA = 'A'");

                ResultSet r1 = st1.getResultSet();

                while (r1.next()) {
                    jLabelRegYear.setText(r1.getString(1));
                    jLabelSerial.setText(r1.getString(2));
                    jLabelRegNum.setText(r1.getString(3));
                    jLabelRegDate.setText(r1.getString(4));
                    jLabelEmpNum.setText(r1.getString(5));
                    jLabelEmpNam.setText(r1.getString(6));
                    jLabelEmpTitle.setText(r1.getString(7));
                    jLabelEmpProvince.setText(r1.getString(8));
                    jLabelEmpOffice.setText(r1.getString(9));
                    jLabelBankName.setText(r1.getString(10));
                    jLabelEmpAccNum.setText(r1.getString(11));
                    jLabelActMainPurpose.setText(r1.getString(12));
                    jLabelAppTotReqCost.setText(r1.getString(13));
                    actVersion = r1.getString(14);
                    oldDocVersion = r1.getString(15);
                    docVersion = r1.getString(16);

                }
            } catch (Exception e) {
                System.out.println(e);
            }

        } catch (Exception e1) {

        }
    }

    void fetchdataWk1() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                    + "where concat(SERIAL,REF_NUM)='" + searchRef + "' and ACT_REC_STA = 'A' "
                    + "and PLAN_WK=1 ");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk1.insertRow(modelWk1.getRowCount(), new Object[]{r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22), r.getString(23)});

            }

            if (modelWk1.getRowCount() == 0) {
                jTabbedPaneAppSys.setEnabledAt(1, false);
            } else {
                jLabelLinSerial.setText(jLabelSerial.getText());
                jLabelLinRegNum.setText(jLabelRegNum.getText());
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdataWk2() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                    + "where concat(SERIAL,REF_NUM)='" + searchRef + "' and ACT_REC_STA = 'A' "
                    + "and PLAN_WK=2 ");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk2.insertRow(modelWk2.getRowCount(), new Object[]{r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22), r.getString(23)});

            }

            if (modelWk2.getRowCount() == 0) {
                jTabbedPaneAppSys.setEnabledAt(2, false);
            } else {
                jLabelLinSerial1.setText(jLabelSerial.getText());
                jLabelLinRegNum1.setText(jLabelRegNum.getText());
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

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                    + "where concat(SERIAL,REF_NUM)='" + searchRef + "' and ACT_REC_STA = 'A' "
                    + "and PLAN_WK=3 ");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk3.insertRow(modelWk3.getRowCount(), new Object[]{r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22), r.getString(23)});

            }

            if (modelWk3.getRowCount() == 0) {
                jTabbedPaneAppSys.setEnabledAt(3, false);
            } else {
                jLabelLinSerial2.setText(jLabelSerial.getText());
                jLabelLinRegNum2.setText(jLabelRegNum.getText());
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

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                    + "where concat(SERIAL,REF_NUM)='" + searchRef + "' and ACT_REC_STA = 'A' "
                    + "and PLAN_WK=4 ");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk4.insertRow(modelWk4.getRowCount(), new Object[]{r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22), r.getString(23)});

            }

            if (modelWk4.getRowCount() == 0) {
                jTabbedPaneAppSys.setEnabledAt(4, false);
            } else {
                jLabelLinSerial3.setText(jLabelSerial.getText());
                jLabelLinRegNum3.setText(jLabelRegNum.getText());
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

            st.executeQuery("SELECT * FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                    + "where concat(SERIAL,REF_NUM)='" + searchRef + "' and ACT_REC_STA = 'A' "
                    + "and PLAN_WK=5 ");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk5.insertRow(modelWk5.getRowCount(), new Object[]{r.getString(4), r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22), r.getString(23)});

            }

            if (modelWk5.getRowCount() == 0) {
                jTabbedPaneAppSys.setEnabledAt(5, false);
            } else {
                jLabelLinSerial4.setText(jLabelSerial.getText());
                jLabelLinRegNum4.setText(jLabelRegNum.getText());
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void regFail() {
        try {
            jButtonSave.setEnabled(true);
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlupdGen = "update [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] "
                    + "set ACT_REC_STA = 'A' where REF_NUM =" + jLabelRegNum.getText() + ""
                    + " and SERIAL = 'R' and DOC_VER =" + oldDocVersion + " and REG_MOD_VER =" + actVersion;
            pst = conn.prepareStatement(sqlupdGen);
            pst.executeUpdate();

            String sqlDeleteGen = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] "
                    + "where REF_NUM =" + jLabelRegNum.getText() + " "
                    + "and SERIAL = 'R' and DOC_VER =" + docVersion + " and ACT_REC_STA = 'A'";
            pst = conn.prepareStatement(sqlDeleteGen);
            pst.executeUpdate();

            String sqlDeleteWkClr = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab] "
                    + "where REF_NUM =" + jLabelRegNum.getText() + " "
                    + "and SERIAL = 'R'  and ACQ_STA = 'A'";
            pst = conn.prepareStatement(sqlDeleteWkClr);
            pst.executeUpdate();

            String sqlupdItm = "update [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                    + "set ACT_REC_STA = 'A' where REF_NUM =" + jLabelRegNum.getText() + ""
                    + " and SERIAL = 'R' and DOC_VER =" + oldDocVersion + " and REG_MOD_VER =" + actVersion;
            pst = conn.prepareStatement(sqlupdItm);
            pst.executeUpdate();

            String sqlDeleteItm = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                    + "where REF_NUM =" + jLabelRegNum.getText() + " and SERIAL = 'R' "
                    + "and DOC_VER =" + docVersion + " and ACT_REC_STA = 'A'";
            pst = conn.prepareStatement(sqlDeleteItm);
            pst.executeUpdate();

            String sqlupdAct = "update [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] "
                    + "set ACT_REC_STA = 'A' where REF_NUM =" + jLabelRegNum.getText() + " "
                    + "and SERIAL = 'R' and DOC_VER =" + oldDocVersion + " and REG_MOD_VER =" + actVersion;
            pst = conn.prepareStatement(sqlupdAct);
            pst.executeUpdate();

            String sqlDeleteAct = "delete [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] "
                    + "where REF_NUM =" + jLabelRegNum.getText() + " and SERIAL = 'R' "
                    + "and DOC_VER =" + docVersion + " and ACT_REC_STA = 'A'";
            pst = conn.prepareStatement(sqlDeleteAct);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);

        }
    }

    void checkUpdate() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();

            st.executeQuery("SELECT count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] where"
                    + " REF_NUM =" + jLabelRegNum.getText() + " and SERIAL = 'R' and DOC_VER ="
                    + docVersion + " and ACT_REC_STA = 'A'");

            ResultSet r = st.getResultSet();
            while (r.next()) {
                countGen = r.getInt(1);
            }

            st1.executeQuery("SELECT count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] where"
                    + " REF_NUM =" + jLabelRegNum.getText() + " and SERIAL = 'R' and DOC_VER ="
                    + docVersion + " and ACT_REC_STA = 'A'");

            ResultSet r1 = st1.getResultSet();
            while (r1.next()) {
                countItm = r1.getInt(1);
            }

            st2.executeQuery("SELECT count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] where"
                    + " REF_NUM =" + jLabelRegNum.getText() + " and SERIAL = 'R' and DOC_VER ="
                    + docVersion + " and ACT_REC_STA = 'A'");

            ResultSet r2 = st2.getResultSet();
            while (r2.next()) {
                countAct = r2.getInt(1);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void insGenTab() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sql = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] "
                    + "(REF_YEAR,SERIAL,REF_NUM,REF_DAT,EMP_NUM,EMP_NAM,EMP_TTL,EMP_PROV,EMP_OFF,"
                    + "EMP_BNK_NAM,ACC_NUM,ACT_MAIN_PUR,ACT_TOT_AMT,REG_MOD_VER,DOC_VER,ACT_REC_STA) "
                    + " VALUES (?,?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";

            pst = conn.prepareStatement(sql);

            pst.setString(1, String.valueOf(jLabelRegYear.getText()));
            pst.setString(2, jLabelSerial.getText());
            pst.setString(3, String.valueOf(jLabelRegNum.getText()));
            pst.setString(4, jLabelRegDate.getText());
            pst.setString(5, jLabelEmpNum.getText());
            pst.setString(6, jLabelEmpNam.getText());
            pst.setString(7, jLabelEmpTitle.getText());
            pst.setString(8, jLabelEmpProvince.getText());
            pst.setString(9, jLabelEmpOffice.getText());
            pst.setString(10, jLabelBankName.getText());
            pst.setString(11, jLabelEmpAccNum.getText());
            pst.setString(12, jLabelActMainPurpose.getText());
            pst.setString(13, jLabelAppTotReqCost.getText());
            pst.setString(14, "1");
            pst.setString(15, docVersion);
            pst.setString(16, "A");
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void insItmWk1Tab() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {

                String sqlitm = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                        + " VALUES (?,?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlitm);

                pst1.setString(1, String.valueOf(jLabelRegYear.getText()));
                pst1.setString(2, jLabelSerial.getText());
                pst1.setString(3, String.valueOf(jLabelRegNum.getText()));
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
                pst1.setString(15, jTableWk1Activities.getValueAt(i, 11).toString());
                pst1.setString(16, jTableWk1Activities.getValueAt(i, 12).toString());
                pst1.setString(17, jTableWk1Activities.getValueAt(i, 13).toString());
                pst1.setString(18, jTableWk1Activities.getValueAt(i, 14).toString());
                pst1.setString(19, jTableWk1Activities.getValueAt(i, 15).toString());
                pst1.setString(20, jTableWk1Activities.getValueAt(i, 16).toString());
                pst1.setString(21, jTableWk1Activities.getValueAt(i, 17).toString());
                pst1.setString(22, jTableWk1Activities.getValueAt(i, 18).toString());
                pst1.setString(23, jTableWk1Activities.getValueAt(i, 19).toString());
                pst1.setString(24, "1");
                pst1.setString(25, "1");
                pst1.setString(26, docVersion);
                pst1.setString(27, "A");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void insItmWk2Tab() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {

                String sqlitm = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                        + " VALUES (?,?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlitm);

                pst1.setString(1, String.valueOf(jLabelRegYear.getText()));
                pst1.setString(2, jLabelSerial.getText());
                pst1.setString(3, String.valueOf(jLabelRegNum.getText()));
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
                pst1.setString(15, jTableWk2Activities.getValueAt(i, 11).toString());
                pst1.setString(16, jTableWk2Activities.getValueAt(i, 12).toString());
                pst1.setString(17, jTableWk2Activities.getValueAt(i, 13).toString());
                pst1.setString(18, jTableWk2Activities.getValueAt(i, 14).toString());
                pst1.setString(19, jTableWk2Activities.getValueAt(i, 15).toString());
                pst1.setString(20, jTableWk2Activities.getValueAt(i, 16).toString());
                pst1.setString(21, jTableWk2Activities.getValueAt(i, 17).toString());
                pst1.setString(22, jTableWk2Activities.getValueAt(i, 18).toString());
                pst1.setString(23, jTableWk2Activities.getValueAt(i, 19).toString());
                pst1.setString(24, "1");
                pst1.setString(25, "1");
                pst1.setString(26, docVersion);
                pst1.setString(27, "A");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void insItmWk3Tab() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {

                String sqlitm = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                        + " VALUES (?,?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlitm);

                pst1.setString(1, String.valueOf(jLabelRegYear.getText()));
                pst1.setString(2, jLabelSerial.getText());
                pst1.setString(3, String.valueOf(jLabelRegNum.getText()));
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
                pst1.setString(15, jTableWk3Activities.getValueAt(i, 11).toString());
                pst1.setString(16, jTableWk3Activities.getValueAt(i, 12).toString());
                pst1.setString(17, jTableWk3Activities.getValueAt(i, 13).toString());
                pst1.setString(18, jTableWk3Activities.getValueAt(i, 14).toString());
                pst1.setString(19, jTableWk3Activities.getValueAt(i, 15).toString());
                pst1.setString(20, jTableWk3Activities.getValueAt(i, 16).toString());
                pst1.setString(21, jTableWk3Activities.getValueAt(i, 17).toString());
                pst1.setString(22, jTableWk3Activities.getValueAt(i, 18).toString());
                pst1.setString(23, jTableWk3Activities.getValueAt(i, 19).toString());
                pst1.setString(24, "1");
                pst1.setString(25, "1");
                pst1.setString(26, docVersion);
                pst1.setString(27, "A");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void insItmWk4Tab() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            for (int i = 0; i < jTableWk4Activities.getRowCount(); i++) {

                String sqlitm = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                        + " VALUES (?,?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlitm);

                pst1.setString(1, String.valueOf(jLabelRegYear.getText()));
                pst1.setString(2, jLabelSerial.getText());
                pst1.setString(3, String.valueOf(jLabelRegNum.getText()));
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
                pst1.setString(15, jTableWk4Activities.getValueAt(i, 11).toString());
                pst1.setString(16, jTableWk4Activities.getValueAt(i, 12).toString());
                pst1.setString(17, jTableWk4Activities.getValueAt(i, 13).toString());
                pst1.setString(18, jTableWk4Activities.getValueAt(i, 14).toString());
                pst1.setString(19, jTableWk4Activities.getValueAt(i, 15).toString());
                pst1.setString(20, jTableWk4Activities.getValueAt(i, 16).toString());
                pst1.setString(21, jTableWk4Activities.getValueAt(i, 17).toString());
                pst1.setString(22, jTableWk4Activities.getValueAt(i, 18).toString());
                pst1.setString(23, jTableWk4Activities.getValueAt(i, 19).toString());
                pst1.setString(24, "1");
                pst1.setString(25, "1");
                pst1.setString(26, docVersion);
                pst1.setString(27, "A");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void insItmWk5Tab() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            for (int i = 0; i < jTableWk5Activities.getRowCount(); i++) {

                String sqlitm = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                        + " VALUES (?,?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pst1 = conn.prepareStatement(sqlitm);

                pst1.setString(1, String.valueOf(jLabelRegYear.getText()));
                pst1.setString(2, jLabelSerial.getText());
                pst1.setString(3, String.valueOf(jLabelRegNum.getText()));
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
                pst1.setString(15, jTableWk5Activities.getValueAt(i, 11).toString());
                pst1.setString(16, jTableWk5Activities.getValueAt(i, 12).toString());
                pst1.setString(17, jTableWk5Activities.getValueAt(i, 13).toString());
                pst1.setString(18, jTableWk5Activities.getValueAt(i, 14).toString());
                pst1.setString(19, jTableWk5Activities.getValueAt(i, 15).toString());
                pst1.setString(20, jTableWk5Activities.getValueAt(i, 16).toString());
                pst1.setString(21, jTableWk5Activities.getValueAt(i, 17).toString());
                pst1.setString(22, jTableWk5Activities.getValueAt(i, 18).toString());
                pst1.setString(23, jTableWk5Activities.getValueAt(i, 19).toString());
                pst1.setString(24, "1");
                pst1.setString(25, "1");
                pst1.setString(26, docVersion);
                pst1.setString(27, "A");

                pst1.executeUpdate();
                itmNum = itmNum + 1;

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void insReqAcqTab() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlReqAcq = "insert into [ClaimsAppSysZvandiri].[dbo].[ClaimWkReqAcqTab] "
                    + "(SERIAL,REF_NUM,PLAN_WK,CLEARED_AMT,REQ_STA,ACQ_STA,DOC_VER)"
                    + " SELECT distinct SERIAL,REF_NUM,PLAN_WK,sum(ACT_ITM_TOT),'R', 'A','1' "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
                    + "where concat(SERIAL,REF_NUM)='" + jLabelSerial.getText() + jLabelRegNum.getText() + "'"
                    + " and DOC_VER =" + docVersion + " and ACT_REC_STA = 'A' group by PLAN_WK,SERIAL,REF_NUM";

            pst = conn.prepareStatement(sqlReqAcq);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void createAction() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlcreate = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] "
                    + "(YEAR,SERIAL,REF_NUM,USR_ACTION, DOC_STATUS,ACTIONED_BY_EMP_NUM ,"
                    + "ACTIONED_BY_NAM ,SEND_TO_EMP_NUM,SEND_TO_NAM,ACTIONED_ON_DATE, ACTIONED_ON_TIME,"
                    + "ACTIONED_ON_COMPUTER,REG_MOD_VER,ACTION_VER, DOC_VER ,ACT_REC_STA)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            String actionCode = "Request - HOD Approved";
            String actionCodeReject = "Request - HOD Rejected";

            statusCodeApp = "HODApprove";
            statusCodeDisApp = "HODDisApprove";
            pst1 = conn.prepareStatement(sqlcreate);

            pst1.setString(1, df.format(curYear));
            pst1.setString(2, jLabelSerial.getText());
            pst1.setString(3, jLabelRegNum.getText());
            if (jCheckBoxAgreed.isSelected()) {
                pst1.setString(4, actionCode);
                pst1.setString(5, statusCodeApp);

            } else if (jCheckBoxDisAgree.isSelected()) {
                pst1.setString(4, actionCodeReject);
                pst1.setString(5, statusCodeDisApp);

            }

            pst1.setString(6, jLabelEmp.getText());
            pst1.setString(7, createUsrNam);
            if (jCheckBoxAgreed.isSelected()) {
                pst1.setString(8, "FINANCE");
                pst1.setString(9, "FINANCE");

            } else if (jCheckBoxDisAgree.isSelected()) {
                pst1.setString(8, reqEmpNum);
                pst1.setString(9, reqUsrNam);

            }
            pst1.setString(10, jLabelGenDate.getText());
            pst1.setString(11, jLabelGenTime.getText());
            pst1.setString(12, hostName);
            pst1.setString(13, "1");
            pst1.setString(14, "1");
            pst1.setString(15, docVersion);
            pst1.setString(16, "A");
            pst1.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    void saveSupDet() {
        try {
            if ((!(jCheckBoxAgreed.isSelected())) && (!(jCheckBoxDisAgree.isSelected()))) {

                JOptionPane.showMessageDialog(this, "Please select if you Agree or Disagree "
                        + "with the perdiem request application before proceeding",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jCheckBoxAgreed.requestFocusInWindow();
                jCheckBoxAgreed.setFocusable(true);
            } else if (((!("".equals(jLabelBudUpd.getText()))) || (!("".equals(jTextActMainPurpose.getText())))) && ("".equals(jTextAreaComments.getText()))) {
                JOptionPane.showMessageDialog(this, "Please record comments for the update you did.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jTextAreaComments.requestFocusInWindow();
                jTextAreaComments.setFocusable(true);
            } else {
                if (jCheckBoxAgreed.isSelected()) {
                    updRecTab();
                    checkUpdate();
                    updateFinalise();
                } else if (jCheckBoxDisAgree.isSelected()) {
                    jDialogEmailComments.setVisible(true);
                    jDialogEmailComments.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
//                jButtonReasonDialogOk.setVisible(false);
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void updateFinalise() {
        try {
            if (countGen == 0 || countItm == 0 || countAct == 0) {

                JOptionPane.showMessageDialog(null, "<html> Registration failure. "
                        + "Registration falure can be caused by slow network speeds.<br><br>"
                        + " Please try again. If the problem persist contact IT.</html>");

                regFail();

            } else {
                JOptionPane.showMessageDialog(this, "Perdiem Request Application No. R "
                        + jLabelRegNum.getText() + " of " + jLabelRegDate.getText()
                        + " saved sucessfully.");

                if (jCheckBoxAgreed.isSelected()) {

                    jDialogWaitingEmail.setVisible(true);
                    insReqAcqTab();
                    String mailMsg = "<html><body> Dear Finance Team <br><br> <b>HOD "
                            + jLabelGenLogNam.getText() + "</b> has approved per diem request No. R " + jLabelRegNum.getText() + ".<br><br>"
                            + "Please check your Finance System inbox and process.<br><br> Kind Regards <br><br>"
                            + " Finance Management System </body></html>";

                    String MailMsgTitle = "Per Diem Approval Request - Reference No. " + jLabelSerial.getText() + " " + jLabelRegNum.getText() + " ";

                    emSend.sendMail(MailMsgTitle, finAppMailList, mailMsg, reqUsrMail);

                    jDialogWaitingEmail.setVisible(false);

                    JOptionPane.showMessageDialog(this, "<html>Email notification has been send to <b>Finance</b> for processing. </html>");

                    new JFrameReqHeadAppList(jLabelEmp.getText()).setVisible(true);
                    setVisible(false);
                } else {
                    PlanReqClearUpd();
                    updateRejComments();

                    jDialogWaitingEmail.setVisible(true);

                    String mailMsg = "<html><body> Dear " + reqUsrNam + " <br><br>HOD <b>"
                            + jLabelGenLogNam.getText() + "</b> has rejected your per diem request No. R " + jLabelRegNum.getText() + ".<br><br>"
                            + "<b>HOD Reject Comments</b><br>" + jTextReasonReject.getText() + "<br><br> "
                            + "Kind Regards <br><br>"
                            + " Finance Management System </body></html>";

                    String MailMsgTitle = "Per Diem Rejected - Reference No. " + jLabelSerial.getText() + " " + jLabelRegNum.getText() + " ";
// HOD email
                    emSend.sendMail(MailMsgTitle, reqUsrMail, mailMsg, "");

                    jDialogWaitingEmail.setVisible(false);

                    JOptionPane.showMessageDialog(this, "Email notification has been send to " + reqUsrNam + " for rejection of requistion No. "
                            + jLabelSerial.getText() + " " + jLabelRegNum.getText());

                    new JFrameReqHeadAppList(jLabelEmp.getText()).setVisible(true);
                    setVisible(false);
                }
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

        buttonGroupSupAgree = new javax.swing.ButtonGroup();
        jDialogDetUpd = new javax.swing.JDialog();
        jPanelDetUpd = new javax.swing.JPanel();
        jLabelProvince1 = new javax.swing.JLabel();
        jComboBudgetCode = new javax.swing.JComboBox<>();
        jLabelBudgetCode1 = new javax.swing.JLabel();
        jLabelActMainPurpose1 = new javax.swing.JLabel();
        jButtonDialogOk = new javax.swing.JButton();
        jButtonDialogCancel = new javax.swing.JButton();
        jTextActMainPurpose = new javax.swing.JTextField();
        jButtonUpdateBud = new javax.swing.JButton();
        jLabelBudUpd = new javax.swing.JLabel();
        jLabelBudCod = new javax.swing.JLabel();
        jButtonClearBud = new javax.swing.JButton();
        jDialogWaitingEmail = new javax.swing.JDialog();
        jDialogEmailComments = new javax.swing.JDialog();
        jPanelDetUpd1 = new javax.swing.JPanel();
        jLabelRejectHeader = new javax.swing.JLabel();
        jLabelReasonDesc = new javax.swing.JLabel();
        jLabelReasonReject = new javax.swing.JLabel();
        jButtonReasonDialogOk = new javax.swing.JButton();
        jTextReasonReject = new javax.swing.JTextField();
        jButtonCancel = new javax.swing.JButton();
        jDialogWk1 = new javax.swing.JDialog();
        jPanel8 = new javax.swing.JPanel();
        jLabelDialogWk1Site = new javax.swing.JLabel();
        jTextFieldDialogWk1Site = new javax.swing.JTextField();
        jTextFieldWk1DialogActivityDesc = new javax.swing.JTextField();
        jLabelWk1DialogActivityDesc = new javax.swing.JLabel();
        jSeparator22 = new javax.swing.JSeparator();
        jLabelWk1DialogBudget = new javax.swing.JLabel();
        jTextFieldWk1DialogBudget = new javax.swing.JTextField();
        jCheckBoxDialogWk1BrkFast = new javax.swing.JCheckBox();
        jCheckBoxDialogWk1Lunch = new javax.swing.JCheckBox();
        jCheckBoxDialogWk1Dinner = new javax.swing.JCheckBox();
        jCheckBoxDialogWk1Acc = new javax.swing.JCheckBox();
        jLabelDialogWk1Dis = new javax.swing.JLabel();
        jLabelWk1DialogActDis = new javax.swing.JLabel();
        jLabelDialogWk1Dis1 = new javax.swing.JLabel();
        jLabelRemain = new javax.swing.JLabel();
        jCheckBoxDialogWk1Misc = new javax.swing.JCheckBox();
        jCheckBoxDialogWk1Inc = new javax.swing.JCheckBox();
        jLabelWk1Misc = new javax.swing.JLabel();
        jTextFieldWk1Misc = new javax.swing.JTextField();
        jLabelWk1MiscAmt = new javax.swing.JLabel();
        jTextFieldWk1MiscAmt = new javax.swing.JTextField();
        jLabelWk1LnActivity = new javax.swing.JLabel();
        jDialogWk3 = new javax.swing.JDialog();
        jPanel6 = new javax.swing.JPanel();
        jLabelDialogWk3Site = new javax.swing.JLabel();
        jTextFieldDialogWk3Site = new javax.swing.JTextField();
        jTextFieldWk3DialogActivityDesc = new javax.swing.JTextField();
        jLabelWk3DialogActivityDesc = new javax.swing.JLabel();
        jSeparator36 = new javax.swing.JSeparator();
        jLabelWk3DialogBudget = new javax.swing.JLabel();
        jTextFieldWk3DialogBudget = new javax.swing.JTextField();
        jCheckBoxDialogWk3BrkFast = new javax.swing.JCheckBox();
        jCheckBoxDialogWk3Lunch = new javax.swing.JCheckBox();
        jCheckBoxDialogWk3Dinner = new javax.swing.JCheckBox();
        jCheckBoxDialogWk3Acc = new javax.swing.JCheckBox();
        jLabelDialogWk2Dis3 = new javax.swing.JLabel();
        jLabelWk3DialogActDis = new javax.swing.JLabel();
        jLabelDialogWk3Dis = new javax.swing.JLabel();
        jLabelRemain3 = new javax.swing.JLabel();
        jCheckBoxDialogWk3Misc = new javax.swing.JCheckBox();
        jCheckBoxDialogWk3Inc = new javax.swing.JCheckBox();
        jLabelWk3Misc = new javax.swing.JLabel();
        jTextFieldWk3Misc = new javax.swing.JTextField();
        jLabelWk3MiscAmt = new javax.swing.JLabel();
        jTextFieldWk3MiscAmt = new javax.swing.JTextField();
        jLabelWk3LnActivity = new javax.swing.JLabel();
        jDialogWk2 = new javax.swing.JDialog();
        jPanel10 = new javax.swing.JPanel();
        jLabelDialogWk2Site = new javax.swing.JLabel();
        jTextFieldDialogWk2Site = new javax.swing.JTextField();
        jTextFieldWk2DialogActivityDesc = new javax.swing.JTextField();
        jLabelWk2DialogActivityDesc = new javax.swing.JLabel();
        jSeparator27 = new javax.swing.JSeparator();
        jLabelWk2DialogBudget = new javax.swing.JLabel();
        jTextFieldWk2DialogBudget = new javax.swing.JTextField();
        jCheckBoxDialogWk2BrkFast = new javax.swing.JCheckBox();
        jCheckBoxDialogWk2Lunch = new javax.swing.JCheckBox();
        jCheckBoxDialogWk2Dinner = new javax.swing.JCheckBox();
        jCheckBoxDialogWk2Acc = new javax.swing.JCheckBox();
        jLabelDialogWk2Dis2 = new javax.swing.JLabel();
        jLabelWk2DialogActDis = new javax.swing.JLabel();
        jLabelDialogWk2Dis = new javax.swing.JLabel();
        jLabelRemain1 = new javax.swing.JLabel();
        jCheckBoxDialogWk2Misc = new javax.swing.JCheckBox();
        jCheckBoxDialogWk2Inc = new javax.swing.JCheckBox();
        jLabelWk2Misc = new javax.swing.JLabel();
        jTextFieldWk2Misc = new javax.swing.JTextField();
        jLabelWk2MiscAmt = new javax.swing.JLabel();
        jTextFieldWk2MiscAmt = new javax.swing.JTextField();
        jLabelWk2LnActivity = new javax.swing.JLabel();
        jTabbedPaneAppSys = new javax.swing.JTabbedPane();
        jPanelGen = new javax.swing.JPanel();
        jLabelLogo = new javax.swing.JLabel();
        jLabelHeaderGen = new javax.swing.JLabel();
        jLabelGenDate = new javax.swing.JLabel();
        jLabelGenTime = new javax.swing.JLabel();
        jLabelGenLogNam1 = new javax.swing.JLabel();
        jLabelGenLogNam = new javax.swing.JLabel();
        jLabelEmpNo = new javax.swing.JLabel();
        jLabelEmpNam = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabelSepDetails = new javax.swing.JLabel();
        jLabelProvince = new javax.swing.JLabel();
        jLabelBank = new javax.swing.JLabel();
        jLabelBankName = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabelEmpTitle = new javax.swing.JLabel();
        jLabelFinDetails = new javax.swing.JLabel();
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
        jLabelEmpAccNum = new javax.swing.JLabel();
        jLabelACC = new javax.swing.JLabel();
        jLabelNum = new javax.swing.JLabel();
        jLabelActMainPurpose = new javax.swing.JLabel();
        jLabelEmpProvince = new javax.swing.JLabel();
        jLabelEmpOffice = new javax.swing.JLabel();
        jLabelEmpNum = new javax.swing.JLabel();
        jLabelRegDate = new javax.swing.JLabel();
        jLabelRegNum = new javax.swing.JLabel();
        jLabelRegYear = new javax.swing.JLabel();
        jLabelSerial = new javax.swing.JLabel();
        jLabelEmp = new javax.swing.JLabel();
        jLabelComments = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaComments = new javax.swing.JTextArea();
        jCheckBoxAgreed = new javax.swing.JCheckBox();
        jCheckBoxDisAgree = new javax.swing.JCheckBox();
        jButtonSave = new javax.swing.JButton();
        jLabelHODApp = new javax.swing.JLabel();
        jPanelRequestWk1 = new javax.swing.JPanel();
        jLabelLogo1 = new javax.swing.JLabel();
        jLabelHeaderLine = new javax.swing.JLabel();
        jLabelLineDate = new javax.swing.JLabel();
        jLabelLineTime = new javax.swing.JLabel();
        jLabellogged = new javax.swing.JLabel();
        jLabelLinLogNam = new javax.swing.JLabel();
        jLabelLinSerial = new javax.swing.JLabel();
        jLabelLinRegNum = new javax.swing.JLabel();
        jLabelNum1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPaneWk8 = new javax.swing.JScrollPane();
        jTableWk1Activities = new javax.swing.JTable();
        jPanelRequestWk2 = new javax.swing.JPanel();
        jLabelLogo2 = new javax.swing.JLabel();
        jLabelHeaderLine1 = new javax.swing.JLabel();
        jLabelLineDate2 = new javax.swing.JLabel();
        jLabelLineTime2 = new javax.swing.JLabel();
        jLabellogged1 = new javax.swing.JLabel();
        jLabelLinLogNam1 = new javax.swing.JLabel();
        jLabelLinSerial1 = new javax.swing.JLabel();
        jLabelLinRegNum1 = new javax.swing.JLabel();
        jLabelNum2 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPaneWk9 = new javax.swing.JScrollPane();
        jTableWk2Activities = new javax.swing.JTable();
        jPanelRequestWk3 = new javax.swing.JPanel();
        jLabelLogo4 = new javax.swing.JLabel();
        jLabelHeaderLine3 = new javax.swing.JLabel();
        jLabelLineDate3 = new javax.swing.JLabel();
        jLabelLineTime3 = new javax.swing.JLabel();
        jLabellogged3 = new javax.swing.JLabel();
        jLabelLinLogNam2 = new javax.swing.JLabel();
        jLabelLinSerial2 = new javax.swing.JLabel();
        jLabelLinRegNum2 = new javax.swing.JLabel();
        jLabelNum3 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPaneWk10 = new javax.swing.JScrollPane();
        jTableWk3Activities = new javax.swing.JTable();
        jPanelRequestWk4 = new javax.swing.JPanel();
        jLabelLogo5 = new javax.swing.JLabel();
        jLabelHeaderLine4 = new javax.swing.JLabel();
        jLabelLineDate4 = new javax.swing.JLabel();
        jLabelLineTime4 = new javax.swing.JLabel();
        jLabellogged4 = new javax.swing.JLabel();
        jLabelLinLogNam3 = new javax.swing.JLabel();
        jLabelLinSerial3 = new javax.swing.JLabel();
        jLabelLinRegNum3 = new javax.swing.JLabel();
        jLabelNum4 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPaneWk11 = new javax.swing.JScrollPane();
        jTableWk4Activities = new javax.swing.JTable();
        jPanelRequestWk5 = new javax.swing.JPanel();
        jLabelLogo6 = new javax.swing.JLabel();
        jLabelHeaderLine5 = new javax.swing.JLabel();
        jLabelLineDate5 = new javax.swing.JLabel();
        jLabelLineTime5 = new javax.swing.JLabel();
        jLabellogged5 = new javax.swing.JLabel();
        jLabelLinLogNam4 = new javax.swing.JLabel();
        jLabelLinSerial4 = new javax.swing.JLabel();
        jLabelLinRegNum4 = new javax.swing.JLabel();
        jLabelNum5 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPaneWk12 = new javax.swing.JScrollPane();
        jTableWk5Activities = new javax.swing.JTable();
        jPanelDocAttach = new javax.swing.JPanel();
        jLabelLogo3 = new javax.swing.JLabel();
        jLabelHeaderLine2 = new javax.swing.JLabel();
        jLabelLineDate1 = new javax.swing.JLabel();
        jLabelLineTime1 = new javax.swing.JLabel();
        jLabellogged2 = new javax.swing.JLabel();
        jLabelLineLogNam1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel9 = new javax.swing.JPanel();
        jLabelImg = new javax.swing.JLabel();
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
        jSeparator31 = new javax.swing.JPopupMenu.Separator();
        jMenuItemUserProfUpd = new javax.swing.JMenuItem();

        jDialogDetUpd.setTitle("Detials Modification");
        jDialogDetUpd.setLocation(new java.awt.Point(450, 150));
        jDialogDetUpd.setMinimumSize(new java.awt.Dimension(890, 480));
        jDialogDetUpd.setResizable(false);

        jPanelDetUpd.setAlignmentX(5.0F);
        jPanelDetUpd.setAlignmentY(5.0F);
        jPanelDetUpd.setMinimumSize(new java.awt.Dimension(890, 480));
        jPanelDetUpd.setPreferredSize(new java.awt.Dimension(890, 480));
        jPanelDetUpd.setLayout(null);

        jLabelProvince1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelProvince1.setForeground(new java.awt.Color(0, 0, 255));
        jLabelProvince1.setText("BUDGET AND ACTIVITY MAIN PURPOSE MODIFICATION SCREEN");
        jPanelDetUpd.add(jLabelProvince1);
        jLabelProvince1.setBounds(140, 10, 630, 50);

        jComboBudgetCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jComboBudgetCodeFocusGained(evt);
            }
        });
        jComboBudgetCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jComboBudgetCodeMouseEntered(evt);
            }
        });
        jPanelDetUpd.add(jComboBudgetCode);
        jComboBudgetCode.setBounds(140, 80, 630, 30);

        jLabelBudgetCode1.setText("Budget Code");
        jPanelDetUpd.add(jLabelBudgetCode1);
        jLabelBudgetCode1.setBounds(10, 80, 80, 30);

        jLabelActMainPurpose1.setText("Activity Main Purpose");
        jPanelDetUpd.add(jLabelActMainPurpose1);
        jLabelActMainPurpose1.setBounds(10, 200, 130, 30);

        jButtonDialogOk.setText("OK");
        jButtonDialogOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDialogOkActionPerformed(evt);
            }
        });
        jPanelDetUpd.add(jButtonDialogOk);
        jButtonDialogOk.setBounds(290, 300, 90, 30);

        jButtonDialogCancel.setLabel("Cancel");
        jButtonDialogCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDialogCancelActionPerformed(evt);
            }
        });
        jPanelDetUpd.add(jButtonDialogCancel);
        jButtonDialogCancel.setBounds(510, 300, 80, 30);
        jPanelDetUpd.add(jTextActMainPurpose);
        jTextActMainPurpose.setBounds(140, 200, 690, 30);

        jButtonUpdateBud.setBackground(new java.awt.Color(255, 0, 102));
        jButtonUpdateBud.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonUpdateBud.setForeground(new java.awt.Color(255, 255, 255));
        jButtonUpdateBud.setText("Upload");
        jButtonUpdateBud.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpdateBudActionPerformed(evt);
            }
        });
        jPanelDetUpd.add(jButtonUpdateBud);
        jButtonUpdateBud.setBounds(790, 80, 80, 30);

        jLabelBudUpd.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 2, true));
        jPanelDetUpd.add(jLabelBudUpd);
        jLabelBudUpd.setBounds(140, 130, 630, 30);

        jLabelBudCod.setText("Updated Budget Code");
        jPanelDetUpd.add(jLabelBudCod);
        jLabelBudCod.setBounds(10, 130, 130, 30);

        jButtonClearBud.setBackground(new java.awt.Color(0, 153, 0));
        jButtonClearBud.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonClearBud.setForeground(new java.awt.Color(255, 255, 255));
        jButtonClearBud.setText("Clear");
        jButtonClearBud.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearBudActionPerformed(evt);
            }
        });
        jPanelDetUpd.add(jButtonClearBud);
        jButtonClearBud.setBounds(790, 130, 80, 30);

        javax.swing.GroupLayout jDialogDetUpdLayout = new javax.swing.GroupLayout(jDialogDetUpd.getContentPane());
        jDialogDetUpd.getContentPane().setLayout(jDialogDetUpdLayout);
        jDialogDetUpdLayout.setHorizontalGroup(
            jDialogDetUpdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelDetUpd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogDetUpdLayout.setVerticalGroup(
            jDialogDetUpdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelDetUpd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialogWaitingEmail.setTitle("                    Connecting to server and sending email.  Please Wait.");
        jDialogWaitingEmail.setAlwaysOnTop(true);
        jDialogWaitingEmail.setBackground(new java.awt.Color(51, 255, 51));
        jDialogWaitingEmail.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jDialogWaitingEmail.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jDialogWaitingEmail.setForeground(new java.awt.Color(255, 0, 0));
        jDialogWaitingEmail.setIconImages(null);
        jDialogWaitingEmail.setLocation(new java.awt.Point(500, 400));
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

        jDialogEmailComments.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jDialogEmailComments.setTitle("Rejection Comments");
        jDialogEmailComments.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jDialogEmailComments.setFocusTraversalPolicyProvider(true);
        jDialogEmailComments.setLocation(new java.awt.Point(300, 150));
        jDialogEmailComments.setMinimumSize(new java.awt.Dimension(890, 480));
        jDialogEmailComments.setResizable(false);

        jPanelDetUpd1.setAlignmentX(5.0F);
        jPanelDetUpd1.setAlignmentY(5.0F);
        jPanelDetUpd1.setMinimumSize(new java.awt.Dimension(890, 480));
        jPanelDetUpd1.setLayout(null);

        jLabelRejectHeader.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelRejectHeader.setForeground(new java.awt.Color(204, 0, 0));
        jLabelRejectHeader.setText("PERDIEM REQUEST REJECTION COMMENTS");
        jPanelDetUpd1.add(jLabelRejectHeader);
        jLabelRejectHeader.setBounds(200, 10, 410, 50);

        jLabelReasonDesc.setText("Please record reason for rejecting this request. The applicant will recieve these comments.");
        jPanelDetUpd1.add(jLabelReasonDesc);
        jLabelReasonDesc.setBounds(190, 70, 560, 30);

        jLabelReasonReject.setText("Reason");
        jPanelDetUpd1.add(jLabelReasonReject);
        jLabelReasonReject.setBounds(20, 140, 120, 30);

        jButtonReasonDialogOk.setBackground(new java.awt.Color(0, 51, 51));
        jButtonReasonDialogOk.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonReasonDialogOk.setForeground(new java.awt.Color(255, 255, 255));
        jButtonReasonDialogOk.setText("Continue");
        jButtonReasonDialogOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReasonDialogOkActionPerformed(evt);
            }
        });
        jPanelDetUpd1.add(jButtonReasonDialogOk);
        jButtonReasonDialogOk.setBounds(270, 200, 120, 30);

        jTextReasonReject.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextReasonRejectKeyTyped(evt);
            }
        });
        jPanelDetUpd1.add(jTextReasonReject);
        jTextReasonReject.setBounds(150, 140, 690, 30);

        jButtonCancel.setBackground(new java.awt.Color(204, 0, 0));
        jButtonCancel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonCancel.setForeground(new java.awt.Color(255, 255, 255));
        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        jPanelDetUpd1.add(jButtonCancel);
        jButtonCancel.setBounds(455, 200, 120, 30);

        javax.swing.GroupLayout jDialogEmailCommentsLayout = new javax.swing.GroupLayout(jDialogEmailComments.getContentPane());
        jDialogEmailComments.getContentPane().setLayout(jDialogEmailCommentsLayout);
        jDialogEmailCommentsLayout.setHorizontalGroup(
            jDialogEmailCommentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelDetUpd1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogEmailCommentsLayout.setVerticalGroup(
            jDialogEmailCommentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelDetUpd1, javax.swing.GroupLayout.PREFERRED_SIZE, 307, Short.MAX_VALUE)
        );

        jDialogWk1.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialogWk1.setTitle("VIEW ACTIVITY DETAILS");
        jDialogWk1.setAlwaysOnTop(true);
        jDialogWk1.setBackground(new java.awt.Color(255, 255, 204));
        jDialogWk1.setLocation(new java.awt.Point(450, 100));
        jDialogWk1.setName("dialogWk1"); // NOI18N
        jDialogWk1.setSize(new java.awt.Dimension(825, 490));

        jPanel8.setBackground(new java.awt.Color(237, 235, 92));
        jPanel8.setPreferredSize(new java.awt.Dimension(800, 490));
        jPanel8.setLayout(null);

        jLabelDialogWk1Site.setText("Site");
        jPanel8.add(jLabelDialogWk1Site);
        jLabelDialogWk1Site.setBounds(20, 100, 40, 13);

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
        jPanel8.add(jTextFieldDialogWk1Site);
        jTextFieldDialogWk1Site.setBounds(20, 130, 420, 30);
        jPanel8.add(jTextFieldWk1DialogActivityDesc);
        jTextFieldWk1DialogActivityDesc.setBounds(20, 350, 480, 30);

        jLabelWk1DialogActivityDesc.setText("Activity Descrpition");
        jPanel8.add(jLabelWk1DialogActivityDesc);
        jLabelWk1DialogActivityDesc.setBounds(20, 310, 130, 30);

        jSeparator22.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel8.add(jSeparator22);
        jSeparator22.setBounds(530, 90, 20, 310);

        jLabelWk1DialogBudget.setText("Budget Line");
        jPanel8.add(jLabelWk1DialogBudget);
        jLabelWk1DialogBudget.setBounds(20, 230, 130, 30);

        jTextFieldWk1DialogBudget.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk1DialogBudgetMouseClicked(evt);
            }
        });
        jTextFieldWk1DialogBudget.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk1DialogBudgetKeyTyped(evt);
            }
        });
        jPanel8.add(jTextFieldWk1DialogBudget);
        jTextFieldWk1DialogBudget.setBounds(20, 270, 480, 30);

        jCheckBoxDialogWk1BrkFast.setText(" Breakfast");
        jPanel8.add(jCheckBoxDialogWk1BrkFast);
        jCheckBoxDialogWk1BrkFast.setBounds(540, 100, 90, 21);

        jCheckBoxDialogWk1Lunch.setText("Lunch");
        jPanel8.add(jCheckBoxDialogWk1Lunch);
        jCheckBoxDialogWk1Lunch.setBounds(660, 100, 80, 21);

        jCheckBoxDialogWk1Dinner.setText(" Dinner");
        jPanel8.add(jCheckBoxDialogWk1Dinner);
        jCheckBoxDialogWk1Dinner.setBounds(540, 150, 90, 21);

        jCheckBoxDialogWk1Acc.setText(" Unproved Acc");
        jPanel8.add(jCheckBoxDialogWk1Acc);
        jCheckBoxDialogWk1Acc.setBounds(660, 150, 130, 21);

        jLabelDialogWk1Dis.setText("Km");
        jPanel8.add(jLabelDialogWk1Dis);
        jLabelDialogWk1Dis.setBounds(280, 180, 50, 30);
        jPanel8.add(jLabelWk1DialogActDis);
        jLabelWk1DialogActDis.setBounds(210, 180, 70, 30);

        jLabelDialogWk1Dis1.setText("Distance From Base");
        jPanel8.add(jLabelDialogWk1Dis1);
        jLabelDialogWk1Dis1.setBounds(20, 180, 130, 30);

        jLabelRemain.setFont(new java.awt.Font("Tahoma", 3, 10)); // NOI18N
        jPanel8.add(jLabelRemain);
        jLabelRemain.setBounds(20, 440, 250, 20);

        jCheckBoxDialogWk1Misc.setText("Miscellaneous");
        jCheckBoxDialogWk1Misc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDialogWk1MiscActionPerformed(evt);
            }
        });
        jPanel8.add(jCheckBoxDialogWk1Misc);
        jCheckBoxDialogWk1Misc.setBounds(660, 200, 140, 21);

        jCheckBoxDialogWk1Inc.setText("Incidental");
        jPanel8.add(jCheckBoxDialogWk1Inc);
        jCheckBoxDialogWk1Inc.setBounds(540, 200, 110, 21);

        jLabelWk1Misc.setText("Miscellaneous Desc");
        jPanel8.add(jLabelWk1Misc);
        jLabelWk1Misc.setBounds(550, 260, 160, 30);
        jPanel8.add(jTextFieldWk1Misc);
        jTextFieldWk1Misc.setBounds(550, 290, 110, 30);

        jLabelWk1MiscAmt.setText("$");
        jPanel8.add(jLabelWk1MiscAmt);
        jLabelWk1MiscAmt.setBounds(680, 260, 30, 30);
        jPanel8.add(jTextFieldWk1MiscAmt);
        jTextFieldWk1MiscAmt.setBounds(720, 290, 70, 30);

        jLabelWk1LnActivity.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jPanel8.add(jLabelWk1LnActivity);
        jLabelWk1LnActivity.setBounds(220, 20, 460, 30);

        javax.swing.GroupLayout jDialogWk1Layout = new javax.swing.GroupLayout(jDialogWk1.getContentPane());
        jDialogWk1.getContentPane().setLayout(jDialogWk1Layout);
        jDialogWk1Layout.setHorizontalGroup(
            jDialogWk1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
        );
        jDialogWk1Layout.setVerticalGroup(
            jDialogWk1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogWk1Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jDialogWk3.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialogWk3.setTitle("VIEW ACTIVITY DETAILS");
        jDialogWk3.setAlwaysOnTop(true);
        jDialogWk3.setBackground(new java.awt.Color(255, 255, 204));
        jDialogWk3.setLocation(new java.awt.Point(300, 100));
        jDialogWk3.setName("dialogWk1"); // NOI18N
        jDialogWk3.setSize(new java.awt.Dimension(825, 490));

        jPanel6.setBackground(new java.awt.Color(84, 110, 240));
        jPanel6.setPreferredSize(new java.awt.Dimension(800, 650));
        jPanel6.setLayout(null);

        jLabelDialogWk3Site.setText("Site");
        jPanel6.add(jLabelDialogWk3Site);
        jLabelDialogWk3Site.setBounds(20, 90, 40, 13);

        jTextFieldDialogWk3Site.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldDialogWk3SiteMouseClicked(evt);
            }
        });
        jTextFieldDialogWk3Site.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldDialogWk3SiteActionPerformed(evt);
            }
        });
        jTextFieldDialogWk3Site.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldDialogWk3SiteKeyTyped(evt);
            }
        });
        jPanel6.add(jTextFieldDialogWk3Site);
        jTextFieldDialogWk3Site.setBounds(20, 120, 420, 30);
        jPanel6.add(jTextFieldWk3DialogActivityDesc);
        jTextFieldWk3DialogActivityDesc.setBounds(20, 370, 410, 30);

        jLabelWk3DialogActivityDesc.setText("Activity Descrpition");
        jPanel6.add(jLabelWk3DialogActivityDesc);
        jLabelWk3DialogActivityDesc.setBounds(20, 330, 130, 30);

        jSeparator36.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel6.add(jSeparator36);
        jSeparator36.setBounds(500, 100, 20, 340);

        jLabelWk3DialogBudget.setText("Budget Line");
        jPanel6.add(jLabelWk3DialogBudget);
        jLabelWk3DialogBudget.setBounds(20, 240, 130, 30);

        jTextFieldWk3DialogBudget.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk3DialogBudgetMouseClicked(evt);
            }
        });
        jTextFieldWk3DialogBudget.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk3DialogBudgetKeyTyped(evt);
            }
        });
        jPanel6.add(jTextFieldWk3DialogBudget);
        jTextFieldWk3DialogBudget.setBounds(20, 280, 410, 30);

        jCheckBoxDialogWk3BrkFast.setText(" Breakfast");
        jPanel6.add(jCheckBoxDialogWk3BrkFast);
        jCheckBoxDialogWk3BrkFast.setBounds(550, 110, 90, 21);

        jCheckBoxDialogWk3Lunch.setText("Lunch");
        jPanel6.add(jCheckBoxDialogWk3Lunch);
        jCheckBoxDialogWk3Lunch.setBounds(670, 110, 80, 21);

        jCheckBoxDialogWk3Dinner.setText(" Dinner");
        jPanel6.add(jCheckBoxDialogWk3Dinner);
        jCheckBoxDialogWk3Dinner.setBounds(550, 160, 90, 21);

        jCheckBoxDialogWk3Acc.setText(" Unproved Acc");
        jPanel6.add(jCheckBoxDialogWk3Acc);
        jCheckBoxDialogWk3Acc.setBounds(670, 160, 130, 21);

        jLabelDialogWk2Dis3.setText("Km");
        jPanel6.add(jLabelDialogWk2Dis3);
        jLabelDialogWk2Dis3.setBounds(280, 190, 50, 30);
        jPanel6.add(jLabelWk3DialogActDis);
        jLabelWk3DialogActDis.setBounds(210, 190, 70, 30);

        jLabelDialogWk3Dis.setText("Distance From Base");
        jPanel6.add(jLabelDialogWk3Dis);
        jLabelDialogWk3Dis.setBounds(20, 190, 130, 30);

        jLabelRemain3.setFont(new java.awt.Font("Tahoma", 3, 10)); // NOI18N
        jPanel6.add(jLabelRemain3);
        jLabelRemain3.setBounds(20, 440, 250, 20);

        jCheckBoxDialogWk3Misc.setText("Miscellaneous");
        jCheckBoxDialogWk3Misc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDialogWk3MiscActionPerformed(evt);
            }
        });
        jPanel6.add(jCheckBoxDialogWk3Misc);
        jCheckBoxDialogWk3Misc.setBounds(670, 210, 140, 21);

        jCheckBoxDialogWk3Inc.setText("Incidental");
        jPanel6.add(jCheckBoxDialogWk3Inc);
        jCheckBoxDialogWk3Inc.setBounds(550, 210, 110, 21);

        jLabelWk3Misc.setText("Miscellaneous Desc");
        jPanel6.add(jLabelWk3Misc);
        jLabelWk3Misc.setBounds(550, 280, 160, 30);
        jPanel6.add(jTextFieldWk3Misc);
        jTextFieldWk3Misc.setBounds(550, 330, 110, 30);

        jLabelWk3MiscAmt.setText("$");
        jPanel6.add(jLabelWk3MiscAmt);
        jLabelWk3MiscAmt.setBounds(680, 330, 30, 30);
        jPanel6.add(jTextFieldWk3MiscAmt);
        jTextFieldWk3MiscAmt.setBounds(720, 330, 70, 30);

        jLabelWk3LnActivity.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jLabelWk3LnActivity.setForeground(new java.awt.Color(255, 255, 255));
        jPanel6.add(jLabelWk3LnActivity);
        jLabelWk3LnActivity.setBounds(220, 20, 460, 30);

        javax.swing.GroupLayout jDialogWk3Layout = new javax.swing.GroupLayout(jDialogWk3.getContentPane());
        jDialogWk3.getContentPane().setLayout(jDialogWk3Layout);
        jDialogWk3Layout.setHorizontalGroup(
            jDialogWk3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
        );
        jDialogWk3Layout.setVerticalGroup(
            jDialogWk3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jDialogWk2.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialogWk2.setTitle("VIEW ACTIVITY DETAILS");
        jDialogWk2.setAlwaysOnTop(true);
        jDialogWk2.setBackground(new java.awt.Color(255, 255, 204));
        jDialogWk2.setLocation(new java.awt.Point(450, 100));
        jDialogWk2.setName("dialogWk1"); // NOI18N
        jDialogWk2.setSize(new java.awt.Dimension(825, 490));

        jPanel10.setBackground(new java.awt.Color(204, 255, 204));
        jPanel10.setPreferredSize(new java.awt.Dimension(800, 650));
        jPanel10.setLayout(null);

        jLabelDialogWk2Site.setText("Site");
        jPanel10.add(jLabelDialogWk2Site);
        jLabelDialogWk2Site.setBounds(20, 80, 40, 13);

        jTextFieldDialogWk2Site.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldDialogWk2SiteMouseClicked(evt);
            }
        });
        jTextFieldDialogWk2Site.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldDialogWk2SiteActionPerformed(evt);
            }
        });
        jTextFieldDialogWk2Site.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldDialogWk2SiteKeyTyped(evt);
            }
        });
        jPanel10.add(jTextFieldDialogWk2Site);
        jTextFieldDialogWk2Site.setBounds(20, 110, 420, 30);
        jPanel10.add(jTextFieldWk2DialogActivityDesc);
        jTextFieldWk2DialogActivityDesc.setBounds(20, 370, 410, 30);

        jLabelWk2DialogActivityDesc.setText("Activity Descrpition");
        jPanel10.add(jLabelWk2DialogActivityDesc);
        jLabelWk2DialogActivityDesc.setBounds(20, 330, 130, 30);

        jSeparator27.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel10.add(jSeparator27);
        jSeparator27.setBounds(530, 90, 20, 340);

        jLabelWk2DialogBudget.setText("Budget Line");
        jPanel10.add(jLabelWk2DialogBudget);
        jLabelWk2DialogBudget.setBounds(20, 220, 130, 30);

        jTextFieldWk2DialogBudget.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldWk2DialogBudgetMouseClicked(evt);
            }
        });
        jTextFieldWk2DialogBudget.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldWk2DialogBudgetKeyTyped(evt);
            }
        });
        jPanel10.add(jTextFieldWk2DialogBudget);
        jTextFieldWk2DialogBudget.setBounds(20, 260, 410, 30);

        jCheckBoxDialogWk2BrkFast.setText(" Breakfast");
        jPanel10.add(jCheckBoxDialogWk2BrkFast);
        jCheckBoxDialogWk2BrkFast.setBounds(560, 110, 90, 21);

        jCheckBoxDialogWk2Lunch.setText("Lunch");
        jPanel10.add(jCheckBoxDialogWk2Lunch);
        jCheckBoxDialogWk2Lunch.setBounds(680, 110, 80, 21);

        jCheckBoxDialogWk2Dinner.setText(" Dinner");
        jPanel10.add(jCheckBoxDialogWk2Dinner);
        jCheckBoxDialogWk2Dinner.setBounds(560, 160, 90, 21);

        jCheckBoxDialogWk2Acc.setText(" Unproved Acc");
        jPanel10.add(jCheckBoxDialogWk2Acc);
        jCheckBoxDialogWk2Acc.setBounds(680, 160, 130, 21);

        jLabelDialogWk2Dis2.setText("Km");
        jPanel10.add(jLabelDialogWk2Dis2);
        jLabelDialogWk2Dis2.setBounds(280, 170, 50, 30);
        jPanel10.add(jLabelWk2DialogActDis);
        jLabelWk2DialogActDis.setBounds(210, 170, 70, 30);

        jLabelDialogWk2Dis.setText("Distance From Base");
        jPanel10.add(jLabelDialogWk2Dis);
        jLabelDialogWk2Dis.setBounds(20, 170, 130, 30);

        jLabelRemain1.setFont(new java.awt.Font("Tahoma", 3, 10)); // NOI18N
        jPanel10.add(jLabelRemain1);
        jLabelRemain1.setBounds(20, 440, 250, 20);

        jCheckBoxDialogWk2Misc.setText("Miscellaneous");
        jCheckBoxDialogWk2Misc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDialogWk2MiscActionPerformed(evt);
            }
        });
        jPanel10.add(jCheckBoxDialogWk2Misc);
        jCheckBoxDialogWk2Misc.setBounds(680, 210, 140, 21);

        jCheckBoxDialogWk2Inc.setText("Incidental");
        jPanel10.add(jCheckBoxDialogWk2Inc);
        jCheckBoxDialogWk2Inc.setBounds(560, 210, 110, 21);

        jLabelWk2Misc.setText("Miscellaneous Desc");
        jPanel10.add(jLabelWk2Misc);
        jLabelWk2Misc.setBounds(570, 280, 160, 30);
        jPanel10.add(jTextFieldWk2Misc);
        jTextFieldWk2Misc.setBounds(570, 310, 110, 30);

        jLabelWk2MiscAmt.setText("$");
        jPanel10.add(jLabelWk2MiscAmt);
        jLabelWk2MiscAmt.setBounds(700, 310, 30, 30);
        jPanel10.add(jTextFieldWk2MiscAmt);
        jTextFieldWk2MiscAmt.setBounds(740, 310, 70, 30);

        jLabelWk2LnActivity.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jPanel10.add(jLabelWk2LnActivity);
        jLabelWk2LnActivity.setBounds(220, 20, 460, 30);

        javax.swing.GroupLayout jDialogWk2Layout = new javax.swing.GroupLayout(jDialogWk2.getContentPane());
        jDialogWk2.getContentPane().setLayout(jDialogWk2Layout);
        jDialogWk2Layout.setHorizontalGroup(
            jDialogWk2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
        );
        jDialogWk2Layout.setVerticalGroup(
            jDialogWk2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PERDIEM REQUEST - FINANCE APPROVAL");
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
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

        jLabelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelGen.add(jLabelLogo);
        jLabelLogo.setBounds(10, 10, 220, 115);

        jLabelHeaderGen.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelGen.add(jLabelHeaderGen);
        jLabelHeaderGen.setBounds(350, 40, 610, 40);

        jLabelGenDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelGen.add(jLabelGenDate);
        jLabelGenDate.setBounds(1020, 0, 110, 30);

        jLabelGenTime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelGen.add(jLabelGenTime);
        jLabelGenTime.setBounds(1270, 0, 80, 30);

        jLabelGenLogNam1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam1.setText("Logged In");
        jPanelGen.add(jLabelGenLogNam1);
        jLabelGenLogNam1.setBounds(1030, 40, 100, 30);

        jLabelGenLogNam.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelGenLogNam);
        jLabelGenLogNam.setBounds(1200, 40, 150, 30);

        jLabelEmpNo.setText("Employee Number");
        jPanelGen.add(jLabelEmpNo);
        jLabelEmpNo.setBounds(20, 200, 110, 30);

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

        jLabelFinDetails.setForeground(new java.awt.Color(0, 0, 204));
        jLabelFinDetails.setText("Financial Details");
        jPanelGen.add(jLabelFinDetails);
        jLabelFinDetails.setBounds(20, 310, 110, 30);

        jLabelMainPurpose.setText("Activity Main Purpose");
        jPanelGen.add(jLabelMainPurpose);
        jLabelMainPurpose.setBounds(20, 360, 130, 30);

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
        jPanel1.setBounds(20, 400, 250, 150);

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
        jPanel3.setBounds(360, 400, 250, 150);

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
        jPanel4.setBounds(700, 400, 250, 150);

        jLabelAppTotReqCost.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelAppTotReqCost.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAppTotReqCost.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelAppTotReqCost.setText("0.00");
        jPanelGen.add(jLabelAppTotReqCost);
        jLabelAppTotReqCost.setBounds(1270, 560, 70, 30);

        jLabelAppTotReq.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelAppTotReq.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAppTotReq.setText("Total ");
        jPanelGen.add(jLabelAppTotReq);
        jLabelAppTotReq.setBounds(1090, 560, 140, 30);

        jLabelOffice.setText("Office");
        jPanelGen.add(jLabelOffice);
        jLabelOffice.setBounds(580, 230, 70, 30);

        jLabelEmpAccNum.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelEmpAccNum);
        jLabelEmpAccNum.setBounds(710, 260, 130, 30);

        jLabelACC.setText("Account No.");
        jPanelGen.add(jLabelACC);
        jLabelACC.setBounds(580, 260, 80, 30);

        jLabelNum.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNum.setText("Reference No.");
        jPanelGen.add(jLabelNum);
        jLabelNum.setBounds(20, 130, 100, 30);

        jLabelActMainPurpose.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelActMainPurpose);
        jLabelActMainPurpose.setBounds(160, 360, 750, 30);

        jLabelEmpProvince.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelEmpProvince);
        jLabelEmpProvince.setBounds(130, 230, 210, 30);

        jLabelEmpOffice.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelEmpOffice);
        jLabelEmpOffice.setBounds(710, 230, 210, 30);

        jLabelEmpNum.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelEmpNum);
        jLabelEmpNum.setBounds(160, 200, 60, 30);

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
        jLabelEmp.setBounds(1000, 70, 50, 20);

        jLabelComments.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelComments.setText("Comments");
        jPanelGen.add(jLabelComments);
        jLabelComments.setBounds(1070, 430, 170, 20);

        jTextAreaComments.setColumns(20);
        jTextAreaComments.setRows(5);
        jScrollPane2.setViewportView(jTextAreaComments);

        jPanelGen.add(jScrollPane2);
        jScrollPane2.setBounds(1070, 450, 260, 110);

        buttonGroupSupAgree.add(jCheckBoxAgreed);
        jCheckBoxAgreed.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jCheckBoxAgreed.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBoxAgreed.setText(" I Agree with the activities that are listed above. The application should be paid in full.");
        jCheckBoxAgreed.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jCheckBoxAgreedFocusGained(evt);
            }
        });
        jCheckBoxAgreed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxAgreedActionPerformed(evt);
            }
        });
        jPanelGen.add(jCheckBoxAgreed);
        jCheckBoxAgreed.setBounds(5, 570, 580, 25);

        buttonGroupSupAgree.add(jCheckBoxDisAgree);
        jCheckBoxDisAgree.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        jCheckBoxDisAgree.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBoxDisAgree.setText(" I DO NOT Agree with the activities that are listed above.Application REJECTED.");
        jCheckBoxDisAgree.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jCheckBoxDisAgreeFocusGained(evt);
            }
        });
        jCheckBoxDisAgree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDisAgreeActionPerformed(evt);
            }
        });
        jPanelGen.add(jCheckBoxDisAgree);
        jCheckBoxDisAgree.setBounds(5, 600, 580, 25);

        jButtonSave.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonSave.setBorderPainted(false);
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        jPanelGen.add(jButtonSave);
        jButtonSave.setBounds(600, 580, 73, 40);

        jLabelHODApp.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabelHODApp.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHODApp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelHODApp.setText("Project HOD Approval");
        jLabelHODApp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelGen.add(jLabelHODApp);
        jLabelHODApp.setBounds(560, 90, 220, 20);

        jTabbedPaneAppSys.addTab("User and Accounting Details", jPanelGen);

        jPanelRequestWk1.setBackground(new java.awt.Color(0, 102, 102));
        jPanelRequestWk1.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jPanelRequestWk1.setPreferredSize(new java.awt.Dimension(1280, 677));
        jPanelRequestWk1.setLayout(null);

        jLabelLogo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelRequestWk1.add(jLabelLogo1);
        jLabelLogo1.setBounds(10, 10, 220, 115);

        jLabelHeaderLine.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelRequestWk1.add(jLabelHeaderLine);
        jLabelHeaderLine.setBounds(400, 40, 610, 40);

        jLabelLineDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelRequestWk1.add(jLabelLineDate);
        jLabelLineDate.setBounds(1070, 0, 110, 30);

        jLabelLineTime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelRequestWk1.add(jLabelLineTime);
        jLabelLineTime.setBounds(1240, 0, 80, 30);

        jLabellogged.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged.setText("Logged In");
        jPanelRequestWk1.add(jLabellogged);
        jLabellogged.setBounds(1070, 40, 80, 30);

        jLabelLinLogNam.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinLogNam.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk1.add(jLabelLinLogNam);
        jLabelLinLogNam.setBounds(1160, 40, 200, 30);

        jLabelLinSerial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinSerial.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk1.add(jLabelLinSerial);
        jLabelLinSerial.setBounds(1240, 70, 20, 30);

        jLabelLinRegNum.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinRegNum.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk1.add(jLabelLinRegNum);
        jLabelLinRegNum.setBounds(1270, 70, 70, 30);

        jLabelNum1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNum1.setText("Reference No.");
        jPanelRequestWk1.add(jLabelNum1);
        jLabelNum1.setBounds(1070, 70, 90, 30);

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Week One");
        jPanelRequestWk1.add(jLabel1);
        jLabel1.setBounds(630, 80, 120, 25);

        jTableWk1Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item No.", "Date", "Account Code", "Donor", "Project Code GL", "Project Code Program", "Project Name Program", "Budget Line", "Budget Code", "Site to Visit", "Activity", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Line Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk8.setViewportView(jTableWk1Activities);

        jPanelRequestWk1.add(jScrollPaneWk8);
        jScrollPaneWk8.setBounds(10, 160, 1340, 460);

        jTabbedPaneAppSys.addTab("Perdiem Request - Week 1", jPanelRequestWk1);

        jPanelRequestWk2.setBackground(new java.awt.Color(0, 102, 102));
        jPanelRequestWk2.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jPanelRequestWk2.setPreferredSize(new java.awt.Dimension(1280, 677));
        jPanelRequestWk2.setLayout(null);

        jLabelLogo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelRequestWk2.add(jLabelLogo2);
        jLabelLogo2.setBounds(10, 10, 220, 100);

        jLabelHeaderLine1.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine1.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelRequestWk2.add(jLabelHeaderLine1);
        jLabelHeaderLine1.setBounds(400, 40, 610, 40);

        jLabelLineDate2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelRequestWk2.add(jLabelLineDate2);
        jLabelLineDate2.setBounds(1070, 0, 110, 30);

        jLabelLineTime2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelRequestWk2.add(jLabelLineTime2);
        jLabelLineTime2.setBounds(1240, 0, 80, 30);

        jLabellogged1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged1.setText("Logged In");
        jPanelRequestWk2.add(jLabellogged1);
        jLabellogged1.setBounds(1070, 40, 80, 30);

        jLabelLinLogNam1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinLogNam1.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk2.add(jLabelLinLogNam1);
        jLabelLinLogNam1.setBounds(1160, 40, 200, 30);

        jLabelLinSerial1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinSerial1.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk2.add(jLabelLinSerial1);
        jLabelLinSerial1.setBounds(1240, 70, 20, 30);

        jLabelLinRegNum1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinRegNum1.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk2.add(jLabelLinRegNum1);
        jLabelLinRegNum1.setBounds(1270, 70, 70, 30);

        jLabelNum2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNum2.setText("Reference No.");
        jPanelRequestWk2.add(jLabelNum2);
        jLabelNum2.setBounds(1070, 70, 90, 30);

        jLabel2.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Week Two");
        jPanelRequestWk2.add(jLabel2);
        jLabel2.setBounds(630, 80, 120, 25);

        jTableWk2Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item No.", "Date", "Account Code", "Donor", "Project Code GL", "Project Code Program", "Project Name Program", "Budget Line", "Budget Code", "Site to Visit", "Activity", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Line Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk9.setViewportView(jTableWk2Activities);

        jPanelRequestWk2.add(jScrollPaneWk9);
        jScrollPaneWk9.setBounds(10, 160, 1340, 460);

        jTabbedPaneAppSys.addTab("Perdiem Request - Week 2", jPanelRequestWk2);

        jPanelRequestWk3.setBackground(new java.awt.Color(0, 102, 102));
        jPanelRequestWk3.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jPanelRequestWk3.setPreferredSize(new java.awt.Dimension(1280, 677));
        jPanelRequestWk3.setLayout(null);

        jLabelLogo4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelRequestWk3.add(jLabelLogo4);
        jLabelLogo4.setBounds(10, 10, 220, 115);

        jLabelHeaderLine3.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine3.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelRequestWk3.add(jLabelHeaderLine3);
        jLabelHeaderLine3.setBounds(400, 40, 610, 40);

        jLabelLineDate3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelRequestWk3.add(jLabelLineDate3);
        jLabelLineDate3.setBounds(1070, 0, 110, 30);

        jLabelLineTime3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelRequestWk3.add(jLabelLineTime3);
        jLabelLineTime3.setBounds(1240, 0, 80, 30);

        jLabellogged3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged3.setText("Logged In");
        jPanelRequestWk3.add(jLabellogged3);
        jLabellogged3.setBounds(1070, 40, 80, 30);

        jLabelLinLogNam2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinLogNam2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk3.add(jLabelLinLogNam2);
        jLabelLinLogNam2.setBounds(1160, 40, 200, 30);

        jLabelLinSerial2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinSerial2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk3.add(jLabelLinSerial2);
        jLabelLinSerial2.setBounds(1240, 70, 20, 30);

        jLabelLinRegNum2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinRegNum2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk3.add(jLabelLinRegNum2);
        jLabelLinRegNum2.setBounds(1270, 70, 70, 30);

        jLabelNum3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNum3.setText("Reference No.");
        jPanelRequestWk3.add(jLabelNum3);
        jLabelNum3.setBounds(1070, 70, 90, 30);

        jLabel3.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Week Three");
        jPanelRequestWk3.add(jLabel3);
        jLabel3.setBounds(630, 80, 120, 25);

        jTableWk3Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item No.", "Date", "Account Code", "Donor", "Project Code GL", "Project Code Program", "Project Name Program", "Budget Line", "Budget Code", "Site to Visit", "Activity", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Line Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk10.setViewportView(jTableWk3Activities);

        jPanelRequestWk3.add(jScrollPaneWk10);
        jScrollPaneWk10.setBounds(10, 160, 1340, 460);

        jTabbedPaneAppSys.addTab("Perdiem Request - Week 3", jPanelRequestWk3);

        jPanelRequestWk4.setBackground(new java.awt.Color(0, 102, 102));
        jPanelRequestWk4.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jPanelRequestWk4.setPreferredSize(new java.awt.Dimension(1280, 677));
        jPanelRequestWk4.setLayout(null);

        jLabelLogo5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelRequestWk4.add(jLabelLogo5);
        jLabelLogo5.setBounds(10, 10, 220, 115);

        jLabelHeaderLine4.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine4.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelRequestWk4.add(jLabelHeaderLine4);
        jLabelHeaderLine4.setBounds(400, 40, 610, 40);

        jLabelLineDate4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelRequestWk4.add(jLabelLineDate4);
        jLabelLineDate4.setBounds(1070, 0, 110, 30);

        jLabelLineTime4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelRequestWk4.add(jLabelLineTime4);
        jLabelLineTime4.setBounds(1240, 0, 80, 30);

        jLabellogged4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged4.setText("Logged In");
        jPanelRequestWk4.add(jLabellogged4);
        jLabellogged4.setBounds(1070, 40, 80, 30);

        jLabelLinLogNam3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinLogNam3.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk4.add(jLabelLinLogNam3);
        jLabelLinLogNam3.setBounds(1160, 40, 200, 30);

        jLabelLinSerial3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinSerial3.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk4.add(jLabelLinSerial3);
        jLabelLinSerial3.setBounds(1240, 70, 20, 30);

        jLabelLinRegNum3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinRegNum3.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk4.add(jLabelLinRegNum3);
        jLabelLinRegNum3.setBounds(1270, 70, 70, 30);

        jLabelNum4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNum4.setText("Reference No.");
        jPanelRequestWk4.add(jLabelNum4);
        jLabelNum4.setBounds(1070, 70, 90, 30);

        jLabel4.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Week  Four");
        jPanelRequestWk4.add(jLabel4);
        jLabel4.setBounds(630, 80, 120, 25);

        jTableWk4Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item No.", "Date", "Account Code", "Donor", "Project Code GL", "Project Code Program", "Project Name Program", "Budget Line", "Budget Code", "Site to Visit", "Activity", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Line Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk11.setViewportView(jTableWk4Activities);

        jPanelRequestWk4.add(jScrollPaneWk11);
        jScrollPaneWk11.setBounds(10, 160, 1340, 460);

        jTabbedPaneAppSys.addTab("Perdiem Request - Week 4", jPanelRequestWk4);

        jPanelRequestWk5.setBackground(new java.awt.Color(0, 102, 102));
        jPanelRequestWk5.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jPanelRequestWk5.setPreferredSize(new java.awt.Dimension(1280, 677));
        jPanelRequestWk5.setLayout(null);

        jLabelLogo6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelRequestWk5.add(jLabelLogo6);
        jLabelLogo6.setBounds(10, 10, 220, 115);

        jLabelHeaderLine5.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine5.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelRequestWk5.add(jLabelHeaderLine5);
        jLabelHeaderLine5.setBounds(400, 40, 610, 40);

        jLabelLineDate5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelRequestWk5.add(jLabelLineDate5);
        jLabelLineDate5.setBounds(1070, 0, 110, 30);

        jLabelLineTime5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelRequestWk5.add(jLabelLineTime5);
        jLabelLineTime5.setBounds(1240, 0, 80, 30);

        jLabellogged5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged5.setText("Logged In");
        jPanelRequestWk5.add(jLabellogged5);
        jLabellogged5.setBounds(1070, 40, 80, 30);

        jLabelLinLogNam4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinLogNam4.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk5.add(jLabelLinLogNam4);
        jLabelLinLogNam4.setBounds(1160, 40, 200, 30);

        jLabelLinSerial4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinSerial4.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk5.add(jLabelLinSerial4);
        jLabelLinSerial4.setBounds(1240, 70, 20, 30);

        jLabelLinRegNum4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinRegNum4.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk5.add(jLabelLinRegNum4);
        jLabelLinRegNum4.setBounds(1270, 70, 70, 30);

        jLabelNum5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNum5.setText("Reference No.");
        jPanelRequestWk5.add(jLabelNum5);
        jLabelNum5.setBounds(1070, 70, 90, 30);

        jLabel5.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Week Five");
        jPanelRequestWk5.add(jLabel5);
        jLabel5.setBounds(630, 80, 120, 25);

        jTableWk5Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item No.", "Date", "Account Code", "Donor", "Project Code GL", "Project Code Program", "Project Name Program", "Budget Line", "Budget Code", "Site to Visit", "Activity", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Line Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk12.setViewportView(jTableWk5Activities);

        jPanelRequestWk5.add(jScrollPaneWk12);
        jScrollPaneWk12.setBounds(10, 160, 1340, 460);

        jTabbedPaneAppSys.addTab("Perdiem Request - Week 5", jPanelRequestWk5);

        jPanelDocAttach.setBackground(new java.awt.Color(204, 204, 204));
        jPanelDocAttach.setLayout(null);

        jLabelLogo3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelDocAttach.add(jLabelLogo3);
        jLabelLogo3.setBounds(10, 5, 220, 105);

        jLabelHeaderLine2.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine2.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelDocAttach.add(jLabelHeaderLine2);
        jLabelHeaderLine2.setBounds(350, 40, 610, 40);

        jLabelLineDate1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate1.setText("29 November 2018");
        jPanelDocAttach.add(jLabelLineDate1);
        jLabelLineDate1.setBounds(1080, 0, 110, 30);

        jLabelLineTime1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime1.setText("15:20:30");
        jPanelDocAttach.add(jLabelLineTime1);
        jLabelLineTime1.setBounds(1250, 0, 80, 30);

        jLabellogged2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged2.setText("Logged In");
        jPanelDocAttach.add(jLabellogged2);
        jLabellogged2.setBounds(1080, 40, 80, 30);

        jLabelLineLogNam1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineLogNam1.setText("User Name");
        jPanelDocAttach.add(jLabelLineLogNam1);
        jLabelLineLogNam1.setBounds(1180, 40, 180, 30);

        jPanel9.setPreferredSize(new java.awt.Dimension(1219, 608));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(365, 365, 365)
                .addComponent(jLabelImg, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
                .addGap(341, 341, 341))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabelImg, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                .addGap(0, 78, Short.MAX_VALUE))
        );

        jScrollPane3.setViewportView(jPanel9);

        jPanelDocAttach.add(jScrollPane3);
        jScrollPane3.setBounds(0, 115, 1355, 535);

        jTabbedPaneAppSys.addTab("Perdiem Request Attachments", jPanelDocAttach);

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
        jMenuFile.add(jSeparator9);

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
        jMenuReports.add(jSeparator31);

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
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jTabbedPaneAppSys, javax.swing.GroupLayout.PREFERRED_SIZE, 1500, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneAppSys)
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


    private void jComboBudgetCodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBudgetCodeFocusGained

    }//GEN-LAST:event_jComboBudgetCodeFocusGained

    private void jComboBudgetCodeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBudgetCodeMouseEntered

    }//GEN-LAST:event_jComboBudgetCodeMouseEntered

    private void jButtonDialogCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDialogCancelActionPerformed
        jDialogDetUpd.setVisible(false);
    }//GEN-LAST:event_jButtonDialogCancelActionPerformed

    private void jButtonUpdateBudActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpdateBudActionPerformed
        jLabelBudUpd.setText(jComboBudgetCode.getSelectedItem().toString());
        jButtonUpdateBud.setVisible(false);
        jLabelBudUpd.setVisible(true);
        jLabelBudCod.setVisible(true);
        jButtonClearBud.setVisible(true);
    }//GEN-LAST:event_jButtonUpdateBudActionPerformed

    private void jButtonDialogOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDialogOkActionPerformed
//        if (("".equals(jLabelBudUpd.getText())) && (!("".equals(jTextActMainPurpose.getText())))) {
//            jLabelActMainPurpose.setText(jTextActMainPurpose.getText());
//            jLabelComments.setVisible(true);
//            jTextAreaComments.setVisible(true);
//        } else if ((!("".equals(jLabelBudUpd.getText()))) && (("".equals(jTextActMainPurpose.getText())))) {
//            jLabelReqBudCode.setText(jLabelBudUpd.getText());
//            jLabelComments.setVisible(true);
//            jTextAreaComments.setVisible(true);
//        } else if ((!("".equals(jLabelBudUpd.getText()))) && (!("".equals(jTextActMainPurpose.getText())))) {
//            jLabelReqBudCode.setText(jLabelBudUpd.getText());
//            jLabelComments.setVisible(true);
//            jTextAreaComments.setVisible(true);
//            jLabelActMainPurpose.setText(jTextActMainPurpose.getText());
//        } else if ((("".equals(jLabelBudUpd.getText()))) && (("".equals(jTextActMainPurpose.getText())))) {
//            jLabelReqBudCode.setText(jLabelReqBudCode.getText());
//            jLabelActMainPurpose.setText(jLabelActMainPurpose.getText());
//            jLabelComments.setVisible(false);
//            jTextAreaComments.setVisible(false);
//        }

        jDialogDetUpd.setVisible(false);
    }//GEN-LAST:event_jButtonDialogOkActionPerformed

    private void jButtonClearBudActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearBudActionPerformed
        jLabelBudUpd.setText("");
        jButtonUpdateBud.setVisible(true);
        jButtonClearBud.setVisible(false);
        jLabelBudUpd.setVisible(false);
        jLabelBudCod.setVisible(false);
    }//GEN-LAST:event_jButtonClearBudActionPerformed

    private void jMenuPlanApprovalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuPlanApprovalActionPerformed

    }//GEN-LAST:event_jMenuPlanApprovalActionPerformed

    private void jButtonReasonDialogOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReasonDialogOkActionPerformed
        try {
            if ("".equals(jTextReasonReject.getText())) {
                JOptionPane.showMessageDialog(jDialogWaitingEmail, "Please enter reason for rejection before proceeding");

                jTextReasonReject.requestFocusInWindow();
                jTextReasonReject.setFocusable(true);
            } else {
                jDialogEmailComments.setVisible(false);
                updRecTab();
                checkUpdate();
                updateFinalise();

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_jButtonReasonDialogOkActionPerformed

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered


    }//GEN-LAST:event_formMouseEntered

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
    }//GEN-LAST:event_formMousePressed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed

    }//GEN-LAST:event_formKeyPressed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked

    }//GEN-LAST:event_formMouseClicked

    private void jTextReasonRejectKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextReasonRejectKeyTyped
//        jButtonReasonDialogOk.setVisible(true);
    }//GEN-LAST:event_jTextReasonRejectKeyTyped

    private void jTextFieldDialogWk1SiteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk1SiteMouseClicked

    }//GEN-LAST:event_jTextFieldDialogWk1SiteMouseClicked

    private void jTextFieldDialogWk1SiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk1SiteActionPerformed

    }//GEN-LAST:event_jTextFieldDialogWk1SiteActionPerformed

    private void jTextFieldDialogWk1SiteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk1SiteKeyTyped

    }//GEN-LAST:event_jTextFieldDialogWk1SiteKeyTyped

    private void jTextFieldWk1DialogBudgetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogBudgetMouseClicked

    }//GEN-LAST:event_jTextFieldWk1DialogBudgetMouseClicked

    private void jTextFieldWk1DialogBudgetKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk1DialogBudgetKeyTyped

    }//GEN-LAST:event_jTextFieldWk1DialogBudgetKeyTyped

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

    private void jTextFieldDialogWk3SiteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk3SiteMouseClicked

    }//GEN-LAST:event_jTextFieldDialogWk3SiteMouseClicked

    private void jTextFieldDialogWk3SiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk3SiteActionPerformed

    }//GEN-LAST:event_jTextFieldDialogWk3SiteActionPerformed

    private void jTextFieldDialogWk3SiteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk3SiteKeyTyped

    }//GEN-LAST:event_jTextFieldDialogWk3SiteKeyTyped

    private void jTextFieldWk3DialogBudgetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogBudgetMouseClicked

    }//GEN-LAST:event_jTextFieldWk3DialogBudgetMouseClicked

    private void jTextFieldWk3DialogBudgetKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk3DialogBudgetKeyTyped

    }//GEN-LAST:event_jTextFieldWk3DialogBudgetKeyTyped

    private void jCheckBoxDialogWk3MiscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxDialogWk3MiscActionPerformed

    }//GEN-LAST:event_jCheckBoxDialogWk3MiscActionPerformed

    private void jTextFieldDialogWk2SiteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk2SiteMouseClicked

    }//GEN-LAST:event_jTextFieldDialogWk2SiteMouseClicked

    private void jTextFieldDialogWk2SiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk2SiteActionPerformed

    }//GEN-LAST:event_jTextFieldDialogWk2SiteActionPerformed

    private void jTextFieldDialogWk2SiteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDialogWk2SiteKeyTyped

    }//GEN-LAST:event_jTextFieldDialogWk2SiteKeyTyped

    private void jTextFieldWk2DialogBudgetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogBudgetMouseClicked

    }//GEN-LAST:event_jTextFieldWk2DialogBudgetMouseClicked

    private void jTextFieldWk2DialogBudgetKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldWk2DialogBudgetKeyTyped

    }//GEN-LAST:event_jTextFieldWk2DialogBudgetKeyTyped

    private void jCheckBoxDialogWk2MiscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxDialogWk2MiscActionPerformed

    }//GEN-LAST:event_jCheckBoxDialogWk2MiscActionPerformed

    private void jCheckBoxAgreedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxAgreedActionPerformed
        if (jCheckBoxAgreed.isSelected()) {
            jButtonSave.setVisible(true);
            jButtonSave.setText("Accept");
            jButtonSave.setBackground(new java.awt.Color(102, 255, 51));
            jButtonSave.setForeground(new java.awt.Color(0, 0, 255));
        }
    }//GEN-LAST:event_jCheckBoxAgreedActionPerformed

    private void jCheckBoxAgreedFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jCheckBoxAgreedFocusGained

        jCheckBoxAgreed.setForeground(Color.green);
        jCheckBoxDisAgree.setForeground(Color.black);
    }//GEN-LAST:event_jCheckBoxAgreedFocusGained

    private void jCheckBoxDisAgreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxDisAgreeActionPerformed
        if (jCheckBoxDisAgree.isSelected()) {
            jButtonSave.setVisible(true);
            jButtonSave.setText("Decline");
            jButtonSave.setBackground(new java.awt.Color(255, 0, 0));
            jButtonSave.setForeground(new java.awt.Color(255, 255, 255));

        }
    }//GEN-LAST:event_jCheckBoxDisAgreeActionPerformed

    private void jCheckBoxDisAgreeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jCheckBoxDisAgreeFocusGained

        jCheckBoxDisAgree.setForeground(Color.red);
        jCheckBoxAgreed.setForeground(Color.black);
    }//GEN-LAST:event_jCheckBoxDisAgreeFocusGained

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        saveSupDet();
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        jTextReasonReject.setText("");
        jDialogEmailComments.setVisible(false);
        jButtonSave.setEnabled(true);
    }//GEN-LAST:event_jButtonCancelActionPerformed

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
            java.util.logging.Logger.getLogger(JFrameReqHeadApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameReqHeadApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameReqHeadApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameReqHeadApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrameReqHeadApp().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupSupAgree;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonClearBud;
    private javax.swing.JButton jButtonDialogCancel;
    private javax.swing.JButton jButtonDialogOk;
    private javax.swing.JButton jButtonReasonDialogOk;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonUpdateBud;
    private javax.swing.JCheckBox jCheckBoxAgreed;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Acc;
    private javax.swing.JCheckBox jCheckBoxDialogWk1BrkFast;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Dinner;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Inc;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Lunch;
    private javax.swing.JCheckBox jCheckBoxDialogWk1Misc;
    private javax.swing.JCheckBox jCheckBoxDialogWk2Acc;
    private javax.swing.JCheckBox jCheckBoxDialogWk2BrkFast;
    private javax.swing.JCheckBox jCheckBoxDialogWk2Dinner;
    private javax.swing.JCheckBox jCheckBoxDialogWk2Inc;
    private javax.swing.JCheckBox jCheckBoxDialogWk2Lunch;
    private javax.swing.JCheckBox jCheckBoxDialogWk2Misc;
    private javax.swing.JCheckBox jCheckBoxDialogWk3Acc;
    private javax.swing.JCheckBox jCheckBoxDialogWk3BrkFast;
    private javax.swing.JCheckBox jCheckBoxDialogWk3Dinner;
    private javax.swing.JCheckBox jCheckBoxDialogWk3Inc;
    private javax.swing.JCheckBox jCheckBoxDialogWk3Lunch;
    private javax.swing.JCheckBox jCheckBoxDialogWk3Misc;
    private javax.swing.JCheckBox jCheckBoxDisAgree;
    private javax.swing.JComboBox<String> jComboBudgetCode;
    private javax.swing.JDialog jDialogDetUpd;
    private javax.swing.JDialog jDialogEmailComments;
    private javax.swing.JDialog jDialogWaitingEmail;
    private javax.swing.JDialog jDialogWk1;
    private javax.swing.JDialog jDialogWk2;
    private javax.swing.JDialog jDialogWk3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabelACC;
    private javax.swing.JLabel jLabelAccProvedSub;
    private javax.swing.JLabel jLabelAccProvedSubTot;
    private javax.swing.JLabel jLabelAccReq;
    private javax.swing.JLabel jLabelAccUnprovedSub;
    private javax.swing.JLabel jLabelAccUnprovedSubTot;
    private javax.swing.JLabel jLabelActMainPurpose;
    private javax.swing.JLabel jLabelActMainPurpose1;
    private javax.swing.JLabel jLabelAllReq;
    private javax.swing.JLabel jLabelAppTotReq;
    private javax.swing.JLabel jLabelAppTotReqCost;
    private javax.swing.JLabel jLabelBank;
    private javax.swing.JLabel jLabelBankName;
    private javax.swing.JLabel jLabelBreakFastSub;
    private javax.swing.JLabel jLabelBreakFastSubTot;
    private javax.swing.JLabel jLabelBudCod;
    private javax.swing.JLabel jLabelBudUpd;
    private javax.swing.JLabel jLabelBudgetCode1;
    private javax.swing.JLabel jLabelComments;
    private javax.swing.JLabel jLabelDialogWk1Dis;
    private javax.swing.JLabel jLabelDialogWk1Dis1;
    private javax.swing.JLabel jLabelDialogWk1Site;
    private javax.swing.JLabel jLabelDialogWk2Dis;
    private javax.swing.JLabel jLabelDialogWk2Dis2;
    private javax.swing.JLabel jLabelDialogWk2Dis3;
    private javax.swing.JLabel jLabelDialogWk2Site;
    private javax.swing.JLabel jLabelDialogWk3Dis;
    private javax.swing.JLabel jLabelDialogWk3Site;
    private javax.swing.JLabel jLabelDinnerSub;
    private javax.swing.JLabel jLabelDinnerSubTot;
    private javax.swing.JLabel jLabelEmp;
    private javax.swing.JLabel jLabelEmpAccNum;
    private javax.swing.JLabel jLabelEmpNam;
    private javax.swing.JLabel jLabelEmpNo;
    private javax.swing.JLabel jLabelEmpNum;
    private javax.swing.JLabel jLabelEmpOffice;
    private javax.swing.JLabel jLabelEmpProvince;
    private javax.swing.JLabel jLabelEmpTitle;
    private javax.swing.JLabel jLabelFinDetails;
    private javax.swing.JLabel jLabelGenDate;
    private javax.swing.JLabel jLabelGenLogNam;
    private javax.swing.JLabel jLabelGenLogNam1;
    private javax.swing.JLabel jLabelGenTime;
    private javax.swing.JLabel jLabelHODApp;
    private javax.swing.JLabel jLabelHeaderGen;
    private javax.swing.JLabel jLabelHeaderLine;
    private javax.swing.JLabel jLabelHeaderLine1;
    private javax.swing.JLabel jLabelHeaderLine2;
    private javax.swing.JLabel jLabelHeaderLine3;
    private javax.swing.JLabel jLabelHeaderLine4;
    private javax.swing.JLabel jLabelHeaderLine5;
    private javax.swing.JLabel jLabelImg;
    private javax.swing.JLabel jLabelIncidentalSub;
    private javax.swing.JLabel jLabelIncidentalSubTot;
    private javax.swing.JLabel jLabelLinLogNam;
    private javax.swing.JLabel jLabelLinLogNam1;
    private javax.swing.JLabel jLabelLinLogNam2;
    private javax.swing.JLabel jLabelLinLogNam3;
    private javax.swing.JLabel jLabelLinLogNam4;
    private javax.swing.JLabel jLabelLinRegNum;
    private javax.swing.JLabel jLabelLinRegNum1;
    private javax.swing.JLabel jLabelLinRegNum2;
    private javax.swing.JLabel jLabelLinRegNum3;
    private javax.swing.JLabel jLabelLinRegNum4;
    private javax.swing.JLabel jLabelLinSerial;
    private javax.swing.JLabel jLabelLinSerial1;
    private javax.swing.JLabel jLabelLinSerial2;
    private javax.swing.JLabel jLabelLinSerial3;
    private javax.swing.JLabel jLabelLinSerial4;
    private javax.swing.JLabel jLabelLineDate;
    private javax.swing.JLabel jLabelLineDate1;
    private javax.swing.JLabel jLabelLineDate2;
    private javax.swing.JLabel jLabelLineDate3;
    private javax.swing.JLabel jLabelLineDate4;
    private javax.swing.JLabel jLabelLineDate5;
    private javax.swing.JLabel jLabelLineLogNam1;
    private javax.swing.JLabel jLabelLineTime;
    private javax.swing.JLabel jLabelLineTime1;
    private javax.swing.JLabel jLabelLineTime2;
    private javax.swing.JLabel jLabelLineTime3;
    private javax.swing.JLabel jLabelLineTime4;
    private javax.swing.JLabel jLabelLineTime5;
    private javax.swing.JLabel jLabelLogo;
    private javax.swing.JLabel jLabelLogo1;
    private javax.swing.JLabel jLabelLogo2;
    private javax.swing.JLabel jLabelLogo3;
    private javax.swing.JLabel jLabelLogo4;
    private javax.swing.JLabel jLabelLogo5;
    private javax.swing.JLabel jLabelLogo6;
    private javax.swing.JLabel jLabelLunchSub;
    private javax.swing.JLabel jLabelLunchSubTot;
    private javax.swing.JLabel jLabelMainPurpose;
    private javax.swing.JLabel jLabelMiscReq;
    private javax.swing.JLabel jLabelMiscSubTot;
    private javax.swing.JLabel jLabelMscSub;
    private javax.swing.JLabel jLabelNum;
    private javax.swing.JLabel jLabelNum1;
    private javax.swing.JLabel jLabelNum2;
    private javax.swing.JLabel jLabelNum3;
    private javax.swing.JLabel jLabelNum4;
    private javax.swing.JLabel jLabelNum5;
    private javax.swing.JLabel jLabelOffice;
    private javax.swing.JLabel jLabelProvince;
    private javax.swing.JLabel jLabelProvince1;
    private javax.swing.JLabel jLabelReasonDesc;
    private javax.swing.JLabel jLabelReasonReject;
    private javax.swing.JLabel jLabelRegDate;
    private javax.swing.JLabel jLabelRegNum;
    private javax.swing.JLabel jLabelRegYear;
    private javax.swing.JLabel jLabelRejectHeader;
    private javax.swing.JLabel jLabelRemain;
    private javax.swing.JLabel jLabelRemain1;
    private javax.swing.JLabel jLabelRemain3;
    private javax.swing.JLabel jLabelSepDetails;
    private javax.swing.JLabel jLabelSerial;
    private javax.swing.JLabel jLabelWk1DialogActDis;
    private javax.swing.JLabel jLabelWk1DialogActivityDesc;
    private javax.swing.JLabel jLabelWk1DialogBudget;
    private javax.swing.JLabel jLabelWk1LnActivity;
    private javax.swing.JLabel jLabelWk1Misc;
    private javax.swing.JLabel jLabelWk1MiscAmt;
    private javax.swing.JLabel jLabelWk2DialogActDis;
    private javax.swing.JLabel jLabelWk2DialogActivityDesc;
    private javax.swing.JLabel jLabelWk2DialogBudget;
    private javax.swing.JLabel jLabelWk2LnActivity;
    private javax.swing.JLabel jLabelWk2Misc;
    private javax.swing.JLabel jLabelWk2MiscAmt;
    private javax.swing.JLabel jLabelWk3DialogActDis;
    private javax.swing.JLabel jLabelWk3DialogActivityDesc;
    private javax.swing.JLabel jLabelWk3DialogBudget;
    private javax.swing.JLabel jLabelWk3LnActivity;
    private javax.swing.JLabel jLabelWk3Misc;
    private javax.swing.JLabel jLabelWk3MiscAmt;
    private javax.swing.JLabel jLabellogged;
    private javax.swing.JLabel jLabellogged1;
    private javax.swing.JLabel jLabellogged2;
    private javax.swing.JLabel jLabellogged3;
    private javax.swing.JLabel jLabellogged4;
    private javax.swing.JLabel jLabellogged5;
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
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelDetUpd;
    private javax.swing.JPanel jPanelDetUpd1;
    private javax.swing.JPanel jPanelDocAttach;
    private javax.swing.JPanel jPanelGen;
    private javax.swing.JPanel jPanelRequestWk1;
    private javax.swing.JPanel jPanelRequestWk2;
    private javax.swing.JPanel jPanelRequestWk3;
    private javax.swing.JPanel jPanelRequestWk4;
    private javax.swing.JPanel jPanelRequestWk5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPaneWk10;
    private javax.swing.JScrollPane jScrollPaneWk11;
    private javax.swing.JScrollPane jScrollPaneWk12;
    private javax.swing.JScrollPane jScrollPaneWk8;
    private javax.swing.JScrollPane jScrollPaneWk9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator11;
    private javax.swing.JPopupMenu.Separator jSeparator13;
    private javax.swing.JPopupMenu.Separator jSeparator16;
    private javax.swing.JPopupMenu.Separator jSeparator18;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator22;
    private javax.swing.JPopupMenu.Separator jSeparator23;
    private javax.swing.JPopupMenu.Separator jSeparator26;
    private javax.swing.JSeparator jSeparator27;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator31;
    private javax.swing.JPopupMenu.Separator jSeparator34;
    private javax.swing.JSeparator jSeparator36;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator62;
    private javax.swing.JPopupMenu.Separator jSeparator63;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    public javax.swing.JTabbedPane jTabbedPaneAppSys;
    private javax.swing.JTable jTableWk1Activities;
    private javax.swing.JTable jTableWk2Activities;
    private javax.swing.JTable jTableWk3Activities;
    private javax.swing.JTable jTableWk4Activities;
    private javax.swing.JTable jTableWk5Activities;
    private javax.swing.JTextField jTextActMainPurpose;
    private javax.swing.JTextArea jTextAreaComments;
    private javax.swing.JTextField jTextFieldDialogWk1Site;
    private javax.swing.JTextField jTextFieldDialogWk2Site;
    private javax.swing.JTextField jTextFieldDialogWk3Site;
    private javax.swing.JTextField jTextFieldWk1DialogActivityDesc;
    private javax.swing.JTextField jTextFieldWk1DialogBudget;
    private javax.swing.JTextField jTextFieldWk1Misc;
    private javax.swing.JTextField jTextFieldWk1MiscAmt;
    private javax.swing.JTextField jTextFieldWk2DialogActivityDesc;
    private javax.swing.JTextField jTextFieldWk2DialogBudget;
    private javax.swing.JTextField jTextFieldWk2Misc;
    private javax.swing.JTextField jTextFieldWk2MiscAmt;
    private javax.swing.JTextField jTextFieldWk3DialogActivityDesc;
    private javax.swing.JTextField jTextFieldWk3DialogBudget;
    private javax.swing.JTextField jTextFieldWk3Misc;
    private javax.swing.JTextField jTextFieldWk3MiscAmt;
    private javax.swing.JTextField jTextReasonReject;
    // End of variables declaration//GEN-END:variables
}
