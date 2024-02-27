/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClaimReports;

import ClaimPlanPerDiem.JFrameMnthPlanPerDiemCreate;
import ClaimAppendix1.*;
import ClaimAppendix2.*;
import ClaimPlan.*;
import ClaimPlanPerDiem.*;
import java.net.InetAddress;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import javax.swing.Timer;
import utils.StockVehicleMgt;
import utils.connCred;
import utils.timeHost;
import utils.recruitRecSend;
import utils.connSaveFile;
import utils.emailSend;
import utils.passwordGenerator;
import utils.savePDFToDB;

/**
 *
 * @author cgoredema
 */
public class JFrameFixedUserCreation extends javax.swing.JFrame {

    /**
     * Creates new form JFrameFixedUserCreation
     */
    connCred c = new connCred();
    emailSend emSend = new emailSend();
    Connection conn = null;
    PreparedStatement pst = null;
    PreparedStatement pst1 = null;
    PreparedStatement pst2 = null;
    PreparedStatement pst3 = null;
    PreparedStatement pst4 = null;
    PreparedStatement pst5 = null;
    PreparedStatement pst6 = null;
    ResultSet rs = null;
    String hostName = "";
    String usremail, supemail, hremail, usrEmpNo, loginname, logVacDate, supNam,
            usrPass, usrPassTmp, supEmpNum, usrGrp;
    int counter = 0;
    int counterUsr = 0;
    int month, finyear, curyear;
    SimpleDateFormat df = new SimpleDateFormat("yyyy");
    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dfMon = new SimpleDateFormat("MM");
    int funCOuntExist = 0;

    public JFrameFixedUserCreation() {
        initComponents();
        jTextSearchEmpNam.setEditable(false);
        showDate();
        showTime();
        findComboField();
        findYear();
        findJobTitle();
    }

    public JFrameFixedUserCreation(String empNum) {
        initComponents();
        jTextSearchEmpNam.setEditable(false);

        jLabelEmp.setText(empNum);
        usrEmpNo = empNum;
        jLabelEmp.setVisible(false);
//        jLabelLogNam.setText(recSend.loggedEmpNam);
        showDate();
        showTime();
        computerName();
        findComboField();
        findYear();
        findJobTitle();
        findLogUser();
        findUserGrp();

        if (!"Administrator".equals(usrGrp)) {
            jMenuItemUserProfUpd.setEnabled(false);
            jMenuItemUserCreate.setEnabled(false);
        }

    }

    void showDate() {
        Date d = new Date();
        jLabelDateCur.setText(s.format(d));

    }

    void showTime() {
        new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date d = new Date();
                SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss a");
                jLabelTimeCur.setText(s.format(d));
            }
        }) {

        }.start();

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

    void findJobTitle() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            jComboJobTitle.setSelectedIndex(-1);

            ResultSet r = st.executeQuery("SELECT distinct (JOB_TITLE) FROM [HRLeaveSysZvandiri].[dbo].[RecJobTitleTab]");

            while (r.next()) {

                jComboJobTitle.addItem(r.getString("JOB_TITLE"));

            }
            conn.close();
        } catch (Exception e) {
            System.out.println();
        }
    }

    void findComboField() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();
            Statement st3 = conn.createStatement();

            jComboProvince.setSelectedIndex(-1);
            ResultSet r = st.executeQuery("select distinct(province) from [ClaimsAppSysZvandiri].[dbo].[ProvOffTab]");

            while (r.next()) {

                jComboProvince.addItem(r.getString("Province"));

            }

            ResultSet r1 = st1.executeQuery("SELECT DISTINCT FUN_GRP FROM [HRLeaveSysZvandiri].[dbo].[Function_Tab] where FUN_APP = 'Finance' order by 1");

            while (r1.next()) {

                jComboFinFunction.addItem(r1.getString(1));

            }
            ResultSet r2 = st2.executeQuery("SELECT DISTINCT FUN_GRP FROM  [HRLeaveSysZvandiri].[dbo].[Function_Tab]  WHERE FUN_APP = 'HR'");

            while (r2.next()) {

                jComboLeaveFunction.addItem(r2.getString(1));

            }

            ResultSet r3 = st3.executeQuery("SELECT Distinct [Bank_Name] FROM [ClaimsAppSysZvandiri].[dbo].[BankBranchCodeTab]");

            while (r3.next()) {

                jComboBank.addItem(r3.getString(1));

            }

            //                 conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findYear() {
        GregorianCalendar date = new GregorianCalendar();
        Date d = new Date();
        month = Integer.parseInt(dfMon.format(d));
        curyear = Integer.parseInt(df.format(d));
        finyear = Integer.parseInt(df.format(d));

        if (month > 9) {
            finyear = curyear + 1;
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

    void findLogUser() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT EMP_NAM FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] where EMP_NUM = '" + usrEmpNo + "'");

            ResultSet rs = st.getResultSet();

            while (rs.next()) {

                jLabelLogNam.setText(rs.getString(1));

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void empDetails() {
        try {
            empSupEmail();

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");

            String sqlempDetFin = "insert into [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] "
                    + "values (?,?,?,?,?,?,?)";
            pst3 = conn.prepareStatement(sqlempDetFin);
            pst3.setString(1, jTextEmpNum.getText());
            pst3.setString(2, jTextFName.getText() + " " + jTextSName.getText());
            pst3.setString(3, jComboJobTitle.getSelectedItem().toString());
            pst3.setString(4, jTextUsrEmail.getText());
            pst3.setString(5, supEmpNum);
            pst3.setString(6, supNam);
            pst3.setString(7, supemail);

            pst3.executeUpdate();

            String sqlempDetUpdate = "update [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] "
                    + "set EMP_NAM = UPPER(EMP_NAM),SUP_NAM = UPPER(SUP_NAM),EMP_TTL = UPPER(EMP_TTL)";
            pst2 = conn.prepareStatement(sqlempDetUpdate);
            pst2.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void empDetailsDelete() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");

            String sqlDelUsrEmpDetTab = "delete [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] where "
                    + "Emp_Num ='" + jTextEmpNum.getText() + "'";
            pst = conn.prepareStatement(sqlDelUsrEmpDetTab);
            pst.executeUpdate();

            String sqlDelUsrLogGrp = "delete [ClaimsAppSysZvandiri].[dbo].[EmpLogGrpTab] where "
                    + "Emp_Num ='" + jTextEmpNum.getText() + "'";
            pst = conn.prepareStatement(sqlDelUsrLogGrp);
            pst.executeUpdate();

            String sqlDelUsrBank = "delete [ClaimsAppSysZvandiri].[dbo].[EmpBankDetTab] where "
                    + "Emp_Num ='" + jTextEmpNum.getText() + "'";
            pst = conn.prepareStatement(sqlDelUsrBank);
            pst.executeUpdate();

            String sqlDelVacdays = "delete [HRLeaveSysZvandiri].[dbo].[CumulatedVacDays] where "
                    + "EmpNum ='" + jTextEmpNum.getText() + "'";
            pst = conn.prepareStatement(sqlDelVacdays);
            pst.executeUpdate();

            String sqlDetWS = "delete [ClaimsAppSysZvandiri].[dbo].[EmpDetWS] where "
                    + "Emp_Num ='" + jTextEmpNum.getText() + "'";
            pst = conn.prepareStatement(sqlDetWS);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void vacDays() {
        try {
            empSupEmail();

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");

            String sqlusrdet = "insert into [HRLeaveSysZvandiri].[dbo].[CumulatedVacDays] "
                    + "(EmpNum,TotDays,FinYear) "
                    + "values (?,?,?)";

            pst1 = conn.prepareStatement(sqlusrdet);
            pst1.setString(1, jTextEmpNum.getText());
            pst1.setString(2, jTextDays.getText());
            pst1.setString(3, Integer.toString(finyear));
            pst1.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void empBankDet() {
        try {
            empSupEmail();

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");

            String sqlusrdet = "insert into [ClaimsAppSysZvandiri].[dbo].[EmpBankDetTab] "
                    + "values (?,?,?,?,?)";

            pst1 = conn.prepareStatement(sqlusrdet);
            pst1.setString(1, jTextEmpNum.getText());
            pst1.setString(2, jComboBank.getSelectedItem().toString());
            pst1.setString(3, jTextBankAcc.getText());
            pst1.setString(4, "0000");
            pst1.setString(5, jTextEmpVendorNum.getText());
            pst1.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void empDetWS() {
        try {
            empSupEmail();

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");

            String sqlusrdet = "insert into [ClaimsAppSysZvandiri].[dbo].[EmpDetWS] "
                    + "values (?,?,?)";

            pst1 = conn.prepareStatement(sqlusrdet);
            pst1.setString(1, jTextEmpNum.getText());
            pst1.setString(2, jComboProvince.getSelectedItem().toString());
            pst1.setString(3, jComboDistrict.getSelectedItem().toString());
            pst1.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void empSupEmail() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT EMP_MAIL ,EMP_NAM,EMP_NUM  FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab]  "
                    + "where concat(EMP_NUM,' ',EMP_NAM) = '" + jTextSearchEmpNam.getText() + "'");
            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                supemail = rs.getString(1);
                supNam = rs.getString(2);
                supEmpNum = rs.getString(3);
                System.out.println("num " + supEmpNum);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void profileCrtAud() {
        try {
            System.out.println("one");
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");
            String sqlUpdFleGrp = "insert into [HRLeaveSysZvandiri].[dbo].[AuditUsrProfUpdTab] "
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            System.out.println("two");
            pst = conn.prepareStatement(sqlUpdFleGrp);
            System.out.println("three");
            pst.setString(1, jTextEmpNum.getText());
            pst.setString(2, jTextLogNam.getText());
            pst.setString(3, "New");
            pst.setString(4, jTextFName.getText().toUpperCase() + " " + jTextSName.getText().toUpperCase());
            pst.setString(5, jTextUsrEmail.getText());
            pst.setString(6, jComboJobTitle.getSelectedItem().toString());
            pst.setString(7, supEmpNum);
            pst.setString(8, supNam);
            pst.setString(9, supemail);
            pst.setString(10, jTextDays.getText());
            pst.setString(11, jComboProvince.getSelectedItem().toString());
            pst.setString(12, jComboDistrict.getSelectedItem().toString());
            pst.setString(13, jComboBank.getSelectedItem().toString());
            pst.setString(14, jTextBankAcc.getText());
            pst.setString(15, "0000");
            pst.setString(16, jComboLeaveFunction.getSelectedItem().toString());
            pst.setString(17, jComboFinFunction.getSelectedItem().toString());
            pst.setString(18, "");
            pst.setString(19, "");
            pst.setString(20, "No");
            pst.setString(21, "No");
            pst.setString(22, "");
            pst.setString(23, "");
            pst.setString(24, "");
            pst.setString(25, jLabelLogNam.getText());
            pst.setString(26, jLabelDateCur.getText());
            pst.setString(27, jLabelTimeCur.getText());
            pst.setString(28, hostName);
            System.out.println("ofour");
            pst.executeUpdate();
            System.out.println("five");

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void login() {
        try {
            passwordGenerator pGen = new passwordGenerator();
            pGen.randomGenerator();
            usrPass = pGen.genPass;
            pGen.randomGenerator();
            usrPassTmp = pGen.genPass;
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");

            String sqlusrlogin = "insert into [HRLeaveSysZvandiri].[dbo].[usrlogin] "
                    + "(usrnam,leaveTitle,EmpNum,createddate,createdTime,empStatus) values (?,?,?,?,?,?)";

            pst2 = conn.prepareStatement(sqlusrlogin);
            pst2.setString(1, jTextLogNam.getText().trim());
            pst2.setString(2, jComboLeaveFunction.getSelectedItem().toString());
            pst2.setString(3, jTextEmpNum.getText().trim());
            pst2.setString(4, jLabelDateCur.getText());
            pst2.setString(5, jLabelTimeCur.getText());
            pst2.setString(6, "1");
            pst2.executeUpdate();

            String sqlupdate = "update[HRLeaveSysZimTTECH].[dbo].[usrlogin] set "
                    + "updencrypass = ENCRYPTBYPASSPHRASE('password','" + usrPass + "'),"
                    + "usrencrypass = ENCRYPTBYPASSPHRASE('password','" + usrPassTmp + "')"
                    + "where EmpNum ='" + jTextEmpNum.getText() + "'";

            pst3 = conn.prepareStatement(sqlupdate);
            pst3.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void vacInitDetails() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");

            String sqlvacdays = "insert into [HRLeaveSysZvandiri].[dbo].[VacationDetails]"
                    + "(RefNum,Year,FinYear,EmpNum,DaysTaken,DaysTakenModify)  values (?,?,?,?,?,?) ";

            pst4 = conn.prepareStatement(sqlvacdays);
            pst4.setString(1, "0");
            pst4.setString(2, Integer.toString(curyear));
            pst4.setString(3, Integer.toString(finyear));
            pst4.setString(4, jTextEmpNum.getText());
            pst4.setString(5, "0");
            pst4.setString(6, "0");
//            pst4.setString(7, jLabelDateCur.getText());
            pst4.executeUpdate();

            String sqlvacupdate = "update [HRLeaveSysZvandiri].[dbo].[VacationDetails] set "
                    + "DateTaken = (select SYSDATETIME()) "
                    + "where empNum ='" + jTextEmpNum.getText() + "'";
            pst6 = conn.prepareStatement(sqlvacupdate);
            pst6.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void finGrp() {
        try {
            findYear();

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");

            String sqlvacdays = "insert into [ClaimsAppSysZvandiri].[dbo].[EmpLogGrpTab] "
                    + "(EMP_NUM,EMP_GRP) values (?,?) ";

            pst = conn.prepareStatement(sqlvacdays);
            pst.setString(1, jTextEmpNum.getText());
            pst.setString(2, jComboFinFunction.getSelectedItem().toString());

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

        jDialogSearchID = new javax.swing.JDialog();
        jPanelSearchID = new javax.swing.JPanel();
        jTextFieldSearchNam = new javax.swing.JTextField();
        jButtonSearch = new javax.swing.JButton();
        jComboBoxSearchResult = new javax.swing.JComboBox<>();
        jButtonOk = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jDialogWaitingEmail = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabellogo = new javax.swing.JLabel();
        jLabelSName = new javax.swing.JLabel();
        jTextEmpNum = new javax.swing.JTextField();
        jLabelEmpNum = new javax.swing.JLabel();
        jTextSName = new javax.swing.JTextField();
        jTextFName = new javax.swing.JTextField();
        jLabelFName = new javax.swing.JLabel();
        jTextUsrEmail = new javax.swing.JTextField();
        jLabelSupEmail = new javax.swing.JLabel();
        jLabelUsrEmail = new javax.swing.JLabel();
        jComboLeaveFunction = new javax.swing.JComboBox<>();
        jLabelLeaveFunction = new javax.swing.JLabel();
        jLabelDays = new javax.swing.JLabel();
        jButtonCancel = new javax.swing.JButton();
        jButtonSubmit = new javax.swing.JButton();
        jTextDays = new javax.swing.JTextField();
        jLabelusrNam = new javax.swing.JLabel();
        jTextLogNam = new javax.swing.JTextField();
        jTextSearchEmpNam = new javax.swing.JTextField();
        jButtonSearchEmp = new javax.swing.JButton();
        jLabelFinFunction = new javax.swing.JLabel();
        jComboFinFunction = new javax.swing.JComboBox<>();
        jLabelUsrEmail1 = new javax.swing.JLabel();
        jComboProvince = new javax.swing.JComboBox<>();
        jLabelProvince = new javax.swing.JLabel();
        jLabelBank = new javax.swing.JLabel();
        jComboBank = new javax.swing.JComboBox<>();
        jTextBankAcc = new javax.swing.JTextField();
        jLabelUsrEmail2 = new javax.swing.JLabel();
        jLabelDateCur = new javax.swing.JLabel();
        jLabelTimeCur = new javax.swing.JLabel();
        jComboDistrict = new javax.swing.JComboBox<>();
        jLabelProvince1 = new javax.swing.JLabel();
        jLabelLog = new javax.swing.JLabel();
        jLabelLogNam = new javax.swing.JLabel();
        jLabelEmp = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jComboJobTitle = new javax.swing.JComboBox<>();
        jTextEmpVendorNum = new javax.swing.JTextField();
        jLabelEmpVendorNum = new javax.swing.JLabel();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuNew = new javax.swing.JMenu();
        jMenuItemPlanPerDiem = new javax.swing.JMenuItem();
        jSeparator13 = new javax.swing.JPopupMenu.Separator();
        jMenuItemPlanPerDiem1 = new javax.swing.JMenuItem();
        jSeparator14 = new javax.swing.JPopupMenu.Separator();
        jMenuItemPerDiemAcquittal = new javax.swing.JMenuItem();
        jSeparator16 = new javax.swing.JPopupMenu.Separator();
        jMenuEdit = new javax.swing.JMenu();
        jMenuMonPlanEdit = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSubmit = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
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
        jMenuItemUserCreate = new javax.swing.JMenuItem();
        jSeparator24 = new javax.swing.JPopupMenu.Separator();
        jMenuItemUserProfUpd = new javax.swing.JMenuItem();

        jDialogSearchID.setTitle("Search Employee ID");
        jDialogSearchID.setAlwaysOnTop(true);
        jDialogSearchID.setBackground(new java.awt.Color(153, 204, 0));
        jDialogSearchID.setLocation(new java.awt.Point(700, 400));
        jDialogSearchID.setMinimumSize(new java.awt.Dimension(400, 200));

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
        jTextFieldSearchNam.setBounds(10, 22, 230, 30);

        jButtonSearch.setText("Search");
        jButtonSearch.setPreferredSize(new java.awt.Dimension(65, 30));
        jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchActionPerformed(evt);
            }
        });
        jPanelSearchID.add(jButtonSearch);
        jButtonSearch.setBounds(280, 20, 90, 30);

        jComboBoxSearchResult.setPreferredSize(new java.awt.Dimension(70, 30));
        jPanelSearchID.add(jComboBoxSearchResult);
        jComboBoxSearchResult.setBounds(10, 70, 230, 30);

        jButtonOk.setText("OK");
        jButtonOk.setPreferredSize(new java.awt.Dimension(73, 30));
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });
        jPanelSearchID.add(jButtonOk);
        jButtonOk.setBounds(280, 70, 90, 30);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Enter search term e.g. Joe and click search button");
        jPanelSearchID.add(jLabel4);
        jLabel4.setBounds(12, 3, 270, 13);

        javax.swing.GroupLayout jDialogSearchIDLayout = new javax.swing.GroupLayout(jDialogSearchID.getContentPane());
        jDialogSearchID.getContentPane().setLayout(jDialogSearchIDLayout);
        jDialogSearchIDLayout.setHorizontalGroup(
            jDialogSearchIDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogSearchIDLayout.createSequentialGroup()
                .addComponent(jPanelSearchID, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jDialogSearchIDLayout.setVerticalGroup(
            jDialogSearchIDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelSearchID, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        setTitle("New User Creation");

        jPanel1.setPreferredSize(new java.awt.Dimension(800, 600));
        jPanel1.setLayout(null);

        jLabellogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanel1.add(jLabellogo);
        jLabellogo.setBounds(30, 0, 230, 120);

        jLabelSName.setText("Surname");
        jPanel1.add(jLabelSName);
        jLabelSName.setBounds(700, 220, 80, 30);

        jTextEmpNum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextEmpNumKeyTyped(evt);
            }
        });
        jPanel1.add(jTextEmpNum);
        jTextEmpNum.setBounds(180, 180, 60, 30);

        jLabelEmpNum.setText("Employee Number");
        jPanel1.add(jLabelEmpNum);
        jLabelEmpNum.setBounds(60, 180, 110, 30);

        jTextSName.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jPanel1.add(jTextSName);
        jTextSName.setBounds(850, 220, 290, 30);

        jTextFName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFNameActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFName);
        jTextFName.setBounds(180, 220, 250, 30);

        jLabelFName.setText("First Name");
        jPanel1.add(jLabelFName);
        jLabelFName.setBounds(60, 220, 110, 30);
        jPanel1.add(jTextUsrEmail);
        jTextUsrEmail.setBounds(180, 270, 250, 30);

        jLabelSupEmail.setText("Supervisor ");
        jPanel1.add(jLabelSupEmail);
        jLabelSupEmail.setBounds(60, 330, 80, 30);

        jLabelUsrEmail.setText("User email");
        jPanel1.add(jLabelUsrEmail);
        jLabelUsrEmail.setBounds(60, 270, 110, 30);

        jPanel1.add(jComboLeaveFunction);
        jComboLeaveFunction.setBounds(600, 400, 160, 30);

        jLabelLeaveFunction.setText("Leave Function");
        jPanel1.add(jLabelLeaveFunction);
        jLabelLeaveFunction.setBounds(490, 400, 110, 30);

        jLabelDays.setText("Remaining Vacation Days");
        jPanel1.add(jLabelDays);
        jLabelDays.setBounds(690, 330, 140, 30);

        jButtonCancel.setText("Cancel");
        jPanel1.add(jButtonCancel);
        jButtonCancel.setBounds(720, 570, 90, 30);

        jButtonSubmit.setText("Submit");
        jButtonSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSubmitActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonSubmit);
        jButtonSubmit.setBounds(460, 570, 100, 30);

        jTextDays.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextDaysActionPerformed(evt);
            }
        });
        jTextDays.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextDaysKeyTyped(evt);
            }
        });
        jPanel1.add(jTextDays);
        jTextDays.setBounds(850, 330, 50, 30);

        jLabelusrNam.setText("login Name");
        jPanel1.add(jLabelusrNam);
        jLabelusrNam.setBounds(700, 180, 80, 30);

        jTextLogNam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextLogNamActionPerformed(evt);
            }
        });
        jPanel1.add(jTextLogNam);
        jTextLogNam.setBounds(850, 180, 290, 30);
        jPanel1.add(jTextSearchEmpNam);
        jTextSearchEmpNam.setBounds(180, 330, 250, 30);

        jButtonSearchEmp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.jpg"))); // NOI18N
        jButtonSearchEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchEmpActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonSearchEmp);
        jButtonSearchEmp.setBounds(450, 330, 30, 25);

        jLabelFinFunction.setText("Finance Function");
        jPanel1.add(jLabelFinFunction);
        jLabelFinFunction.setBounds(870, 390, 110, 30);

        jPanel1.add(jComboFinFunction);
        jComboFinFunction.setBounds(990, 390, 160, 30);

        jLabelUsrEmail1.setText("Job Title");
        jPanel1.add(jLabelUsrEmail1);
        jLabelUsrEmail1.setBounds(700, 270, 100, 30);

        jComboProvince.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboProvinceActionPerformed(evt);
            }
        });
        jPanel1.add(jComboProvince);
        jComboProvince.setBounds(180, 400, 210, 30);

        jLabelProvince.setText("Province");
        jPanel1.add(jLabelProvince);
        jLabelProvince.setBounds(60, 400, 70, 30);

        jLabelBank.setText("Bank");
        jPanel1.add(jLabelBank);
        jLabelBank.setBounds(490, 460, 80, 30);

        jPanel1.add(jComboBank);
        jComboBank.setBounds(600, 460, 240, 30);
        jPanel1.add(jTextBankAcc);
        jTextBankAcc.setBounds(990, 460, 240, 30);

        jLabelUsrEmail2.setText("Bank Account");
        jPanel1.add(jLabelUsrEmail2);
        jLabelUsrEmail2.setBounds(880, 460, 100, 30);

        jLabelDateCur.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelDateCur.setText("Date");
        jLabelDateCur.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                jLabelDateCurComponentAdded(evt);
            }
        });
        jPanel1.add(jLabelDateCur);
        jLabelDateCur.setBounds(1040, 0, 110, 30);

        jLabelTimeCur.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelTimeCur.setText("Time");
        jPanel1.add(jLabelTimeCur);
        jLabelTimeCur.setBounds(1190, 0, 100, 30);

        jPanel1.add(jComboDistrict);
        jComboDistrict.setBounds(180, 460, 210, 30);

        jLabelProvince1.setText("Office");
        jPanel1.add(jLabelProvince1);
        jLabelProvince1.setBounds(60, 460, 70, 30);

        jLabelLog.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabelLog.setText("Logged in");
        jPanel1.add(jLabelLog);
        jLabelLog.setBounds(1040, 30, 70, 25);

        jLabelLogNam.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelLogNam.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(jLabelLogNam);
        jLabelLogNam.setBounds(1140, 30, 160, 25);
        jPanel1.add(jLabelEmp);
        jLabelEmp.setBounds(1120, 70, 80, 30);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("USER CREDENTIALS MANAGEMENT");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(370, 30, 460, 50);

        jComboJobTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboJobTitleActionPerformed(evt);
            }
        });
        jPanel1.add(jComboJobTitle);
        jComboJobTitle.setBounds(850, 270, 330, 30);

        jTextEmpVendorNum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextEmpVendorNumKeyTyped(evt);
            }
        });
        jPanel1.add(jTextEmpVendorNum);
        jTextEmpVendorNum.setBounds(440, 180, 60, 30);

        jLabelEmpVendorNum.setText("Employee Vendor Number");
        jPanel1.add(jLabelEmpVendorNum);
        jLabelEmpVendorNum.setBounds(290, 180, 140, 30);

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

        jMenuItemPlanPerDiem1.setText("Plan - Partcipants Allowances");
        jMenuItemPlanPerDiem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPlanPerDiem1ActionPerformed(evt);
            }
        });
        jMenuNew.add(jMenuItemPlanPerDiem1);
        jMenuNew.add(jSeparator14);

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

        jMenuItemSubmit.setText("Submit");
        jMenuItemSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSubmitActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemSubmit);
        jMenuFile.add(jSeparator7);

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
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1315, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 748, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1331, 807));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextDaysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextDaysActionPerformed

    }//GEN-LAST:event_jTextDaysActionPerformed

    private void jButtonSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSubmitActionPerformed
        if ((jTextEmpNum.getText().length() == 0) || (jTextEmpVendorNum.getText().length() == 0) || (jTextLogNam.getText().length() == 0)
                || (jTextFName.getText().length() == 0) || (jTextSName.getText().length() == 0)
                || (jTextUsrEmail.getText().length() == 0) || (jTextSearchEmpNam.getText().length() == 0)
                || (jTextDays.getText().length() == 0) || (jTextBankAcc.getText().length() == 0)) {
            JOptionPane.showMessageDialog(this, "All fields are manditory.Please complete ALL fields");
            jTextEmpNum.requestFocusInWindow();
            jTextEmpNum.setFocusable(true);
        } else {
            try {

                Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                        + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");

                Statement st = conn.createStatement();
                Statement st1 = conn.createStatement();

                st.executeQuery("SELECT count(*)  FROM [HRLeaveSysZvandiri].[dbo].[usrlogin] where EmpNum = '"
                        + jTextEmpNum.getText() + "'");
                ResultSet rs = st.getResultSet();

                while (rs.next()) {
                    counter = rs.getInt(1);
                }

                st1.executeQuery("SELECT count(*)  FROM [HRLeaveSysZvandiri].[dbo].[usrlogin] where usrnam = '"
                        + jTextLogNam.getText() + "'");
                ResultSet rs1 = st1.getResultSet();

                while (rs1.next()) {
                    counterUsr = rs1.getInt(1);
                }

                if (counterUsr > 0) {
                    JOptionPane.showMessageDialog(this, "Log in name " + jTextLogNam.getText() + " already exist in the database");
                    jTextLogNam.requestFocusInWindow();
                    jTextLogNam.setFocusable(true);
                } else if (counter > 0) {
                    JOptionPane.showMessageDialog(this, "Employee Number " + jTextEmpNum.getText() + " already exist in the database");
                    jTextEmpNum.requestFocusInWindow();
                    jTextEmpNum.setFocusable(true);
                } else {

                    empDetailsDelete();
                    login();
                    empDetails();
                    vacInitDetails();
                    finGrp();
                    vacDays();
                    empBankDet();
                    empDetWS();
                    profileCrtAud();
                    JOptionPane.showMessageDialog(this, " User " + jTextFName.getText().toUpperCase() + " " + jTextSName.getText().toUpperCase() + " Login Credentials Creation Completed.");
//                    jDialogEmailWaiting.setVisible(true);
//                    recSend.sendMailUsrCreate(jTextUsrEmail.getText(), jTextFName.getText().toUpperCase(), jTextSName.getText().toUpperCase(), jTextusrNam.getText(), usrPass);
//                    jDialogEmailWaiting.setVisible(false);
//                    new userMgt.FixedUserMgt.JFrameFixedUserCreation(jLabelLogEmpNum.getText()).setVisible(true);;

                    jDialogWaitingEmail.setVisible(true);

                    String mailMsg = "<html><body> Dear <b>" + jTextFName.getText().toUpperCase() + " " + jTextSName.getText().toUpperCase() + "</b> <br><br>"
                            + " Your username and password for the HR and Finance system has been created. <br><br>"
                            + "Your login name is " + jTextLogNam.getText().toUpperCase() + " and password is " + usrPass + " <br><br> "
                            + "If you need any assistance please contact HR on saashr@zimttech.org.<br><br>"
                            + " Change your password as soon as possible. "
                            + "<br><br> Kind Regards <br><br>"
                            + " HR Management System </body></html>";

                    String MailMsgTitle = "Login Credentials creation for " + jTextFName.getText().toUpperCase() + " " + jTextSName.getText().toUpperCase();

                    emSend.sendMail(MailMsgTitle, jTextUsrEmail.getText(), mailMsg, "");
                    
                       jDialogWaitingEmail.setVisible(false);
                    
                  JOptionPane.showMessageDialog(this, "An email notification has been sent to " + jTextFName.getText().toUpperCase() + " " + jTextSName.getText().toUpperCase() + ".");


                 
                    new JFrameFixedUserCreation(jLabelEmp.getText()).setVisible(true);
                    setVisible(false);

                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }//GEN-LAST:event_jButtonSubmitActionPerformed


    private void jTextLogNamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextLogNamActionPerformed

    }//GEN-LAST:event_jTextLogNamActionPerformed

    private void jButtonSearchEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchEmpActionPerformed
        jDialogSearchID.setVisible(true);
    }//GEN-LAST:event_jButtonSearchEmpActionPerformed

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
            st.executeQuery("SELECT distinct EMP_NUM,EMP_NAM"
                    + "  FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] WHERE [EMP_NAM] like upper(\n" + "'%" + searchNam + "%')");
            ResultSet r = st.getResultSet();

            while (r.next()) {
                jComboBoxSearchResult.addItem(r.getString(1) + " " + r.getString(2));
                jButtonOk.setVisible(true);
                //                jButtonSearch.setVisible(false);

            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonSearchActionPerformed

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        String str = jComboBoxSearchResult.getSelectedItem().toString();
        jTextSearchEmpNam.setText(str);
        jTextFieldSearchNam.setText("");
        jDialogSearchID.dispose();
        empSupEmail();
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jLabelDateCurComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_jLabelDateCurComponentAdded

    }//GEN-LAST:event_jLabelDateCurComponentAdded

    private void jTextFNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFNameActionPerformed

    }//GEN-LAST:event_jTextFNameActionPerformed

    private void jComboProvinceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboProvinceActionPerformed
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            int itemCount = jComboDistrict.getItemCount();

            for (int i = 0; i < itemCount; i++) {
                jComboDistrict.removeItemAt(0);
            }
            jComboDistrict.setSelectedIndex(-1);
            String ProvNam = jComboProvince.getSelectedItem().toString();
            st.executeQuery("SELECT distinct [district]\n"
                    + "  FROM [ClaimsAppSysZvandiri].[dbo].[DistFacTab] WHERE [province] = \n" + "'" + ProvNam + "'");
            ResultSet r = st.getResultSet();

            while (r.next()) {

                jComboDistrict.addItem(r.getString("district"));
            }

            conn.close();
        } catch (Exception e) {
            System.out.println();
        }
    }//GEN-LAST:event_jComboProvinceActionPerformed

    private void jTextEmpNumKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextEmpNumKeyTyped
//        char c = evt.getKeyChar();
//        if (!(Character.isDigit(c))) {
//            getToolkit().beep();
//            evt.consume();
//        }
    }//GEN-LAST:event_jTextEmpNumKeyTyped

    private void jTextDaysKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextDaysKeyTyped

    }//GEN-LAST:event_jTextDaysKeyTyped

    private void jComboJobTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboJobTitleActionPerformed

    }//GEN-LAST:event_jComboJobTitleActionPerformed

    private void jTextEmpVendorNumKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextEmpVendorNumKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextEmpVendorNumKeyTyped

    private void jMenuItemPlanPerDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPlanPerDiemActionPerformed
        new JFrameMnthPlanPerDiemCreate(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemPlanPerDiemActionPerformed

    private void jMenuItemPlanPerDiem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPlanPerDiem1ActionPerformed
       
    }//GEN-LAST:event_jMenuItemPlanPerDiem1ActionPerformed

    private void jMenuItemPerDiemAcquittalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPerDiemAcquittalActionPerformed
        new JFrameAppAcquittal(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemPerDiemAcquittalActionPerformed

    private void jMenuMonPlanEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuMonPlanEditActionPerformed
        new JFrameMnthPlanPerDiemEdit(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuMonPlanEditActionPerformed

    private void jMenuItemSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSubmitActionPerformed
        try {
            if ((jTextEmpNum.getText().length() == 0) || (jTextEmpVendorNum.getText().length() == 0) || (jTextLogNam.getText().length() == 0)
                    || (jTextFName.getText().length() == 0) || (jTextSName.getText().length() == 0)
                    || (jTextUsrEmail.getText().length() == 0) || (jTextSearchEmpNam.getText().length() == 0)
                    || (jTextDays.getText().length() == 0) || (jTextBankAcc.getText().length() == 0)) {
                JOptionPane.showMessageDialog(this, "All fields are manditory.Please complete ALL fields");
                jTextEmpNum.requestFocusInWindow();
                jTextEmpNum.setFocusable(true);
            } else {
                try {

                    Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                            + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");

                    Statement st = conn.createStatement();
                    Statement st1 = conn.createStatement();

                    st.executeQuery("SELECT count(*)  FROM [HRLeaveSysZvandiri].[dbo].[usrlogin] where EmpNum = '"
                            + jTextEmpNum.getText() + "'");
                    ResultSet rs = st.getResultSet();

                    while (rs.next()) {
                        counter = rs.getInt(1);
                    }

                    st1.executeQuery("SELECT count(*)  FROM [HRLeaveSysZvandiri].[dbo].[usrlogin] where usrnam = '"
                            + jTextLogNam.getText() + "'");
                    ResultSet rs1 = st1.getResultSet();

                    while (rs1.next()) {
                        counterUsr = rs1.getInt(1);
                    }

                    if (counterUsr > 0) {
                        JOptionPane.showMessageDialog(this, "Log in name " + jTextLogNam.getText() + " already exist in the database");
                        jTextLogNam.requestFocusInWindow();
                        jTextLogNam.setFocusable(true);
                    } else if (counter > 0) {
                        JOptionPane.showMessageDialog(this, "Employee Number " + jTextEmpNum.getText() + " already exist in the database");
                        jTextEmpNum.requestFocusInWindow();
                        jTextEmpNum.setFocusable(true);
                    } else {
                        empDetailsDelete();
                        login();
                        empDetails();
                        vacInitDetails();
                        finGrp();
                        vacDays();
                        empBankDet();
                        System.out.println("startq");
                        empDetWS();
                        System.out.println("start");
                        profileCrtAud();
                        System.out.println("end");
                        jDialogWaitingEmail.setVisible(true);

                        String mailMsg = "<html><body> Dear <b>" + jTextFName.getText().toUpperCase() + " " + jTextSName.getText().toUpperCase() + "</b> <br><br>"
                                + " Your username and password for the HR and Finance system has been created. <br><br>"
                                + "Your login name is " + jTextLogNam.getText().toUpperCase() + " and password is " + usrPass + " <br><br> "
                                + "If you need any assistance please contact HR on saashr@zimttech.org.<br><br>"
                                + " Change your password as soon as possible. "
                                + "<br><br> Kind Regards <br><br>"
                                + " HR Management System </body></html>";

                        String MailMsgTitle = "Login Credentials creation for " + jTextFName.getText().toUpperCase() + " " + jTextSName.getText().toUpperCase();

                        emSend.sendMail(MailMsgTitle, jTextUsrEmail.getText(), mailMsg, "");

                        jDialogWaitingEmail.setVisible(false);
                        new JFrameFixedUserCreation(jLabelEmp.getText()).setVisible(true);
                        setVisible(false);

                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        } catch (Exception e1) {
            System.out.println(e1);

        }
    }//GEN-LAST:event_jMenuItemSubmitActionPerformed

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
            java.util.logging.Logger.getLogger(JFrameFixedUserCreation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameFixedUserCreation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameFixedUserCreation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameFixedUserCreation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrameFixedUserCreation().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JButton jButtonSearchEmp;
    private javax.swing.JButton jButtonSubmit;
    private javax.swing.JComboBox<String> jComboBank;
    private javax.swing.JComboBox<String> jComboBoxSearchResult;
    private javax.swing.JComboBox<String> jComboDistrict;
    private javax.swing.JComboBox<String> jComboFinFunction;
    private javax.swing.JComboBox<String> jComboJobTitle;
    private javax.swing.JComboBox<String> jComboLeaveFunction;
    private javax.swing.JComboBox<String> jComboProvince;
    private javax.swing.JDialog jDialogSearchID;
    private javax.swing.JDialog jDialogWaitingEmail;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelBank;
    private javax.swing.JLabel jLabelDateCur;
    private javax.swing.JLabel jLabelDays;
    private javax.swing.JLabel jLabelEmp;
    private javax.swing.JLabel jLabelEmpNum;
    private javax.swing.JLabel jLabelEmpVendorNum;
    private javax.swing.JLabel jLabelFName;
    private javax.swing.JLabel jLabelFinFunction;
    private javax.swing.JLabel jLabelLeaveFunction;
    private javax.swing.JLabel jLabelLog;
    public javax.swing.JLabel jLabelLogNam;
    private javax.swing.JLabel jLabelProvince;
    private javax.swing.JLabel jLabelProvince1;
    private javax.swing.JLabel jLabelSName;
    private javax.swing.JLabel jLabelSupEmail;
    private javax.swing.JLabel jLabelTimeCur;
    private javax.swing.JLabel jLabelUsrEmail;
    private javax.swing.JLabel jLabelUsrEmail1;
    private javax.swing.JLabel jLabelUsrEmail2;
    private javax.swing.JLabel jLabellogo;
    private javax.swing.JLabel jLabelusrNam;
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
    private javax.swing.JMenuItem jMenuItemPlanPerDiem1;
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelSearchID;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator11;
    private javax.swing.JPopupMenu.Separator jSeparator13;
    private javax.swing.JPopupMenu.Separator jSeparator14;
    private javax.swing.JPopupMenu.Separator jSeparator16;
    private javax.swing.JPopupMenu.Separator jSeparator18;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator22;
    private javax.swing.JPopupMenu.Separator jSeparator23;
    private javax.swing.JPopupMenu.Separator jSeparator24;
    private javax.swing.JPopupMenu.Separator jSeparator26;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator34;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator62;
    private javax.swing.JPopupMenu.Separator jSeparator63;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JTextField jTextBankAcc;
    private javax.swing.JTextField jTextDays;
    private javax.swing.JTextField jTextEmpNum;
    private javax.swing.JTextField jTextEmpVendorNum;
    private javax.swing.JTextField jTextFName;
    private javax.swing.JTextField jTextFieldSearchNam;
    private javax.swing.JTextField jTextLogNam;
    private javax.swing.JTextField jTextSName;
    private javax.swing.JTextField jTextSearchEmpNam;
    private javax.swing.JTextField jTextUsrEmail;
    // End of variables declaration//GEN-END:variables
}
