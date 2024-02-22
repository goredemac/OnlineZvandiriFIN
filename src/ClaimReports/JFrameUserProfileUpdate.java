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
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import java.util.Properties;
import javax.swing.Timer;
import utils.connCred;
import ClaimAppendix1.*;
import ClaimAppendix2.*;
import ClaimReports.*;
import ClaimPlan.*;
import utils.StockVehicleMgt;

/**
 *
 * @author cgoredema
 */
public class JFrameUserProfileUpdate extends javax.swing.JFrame {

    /**
     * Creates new form JFrameUserCreation
     */
    Connection conn = null;
    connCred c = new connCred();
    PreparedStatement pst = null;
    PreparedStatement pst1 = null;
    PreparedStatement pst2 = null;
    PreparedStatement pst3 = null;
    PreparedStatement pst4 = null;
    PreparedStatement pst5 = null;
    PreparedStatement pst6 = null;
    ResultSet rs = null;
//    String ipAdd = "ophidcldapp.southafricanorth.cloudapp.azure.com:14575";
//    String usrName = "apphr";
//    String usrPass = "33HjrAP%xMBx7Azy";
    String usremail, supemail, hremail, loginname, logVacDate, supNam, headNam,
            curHeadNam, curHeadMail, usrEmpNo, branchCode, usrGrp;
    String jobtitle = "N";
    String HRleaveFun = "N";
    String finFun = "N";
    String stockFun = "N";
    String fleetFun = "N";
    String headFun = "N";
    String hostName = "";
    int counter = 0;
    int store = 0;
    int fleet = 0;
    int fleetAdm = 0;
    int fleetAdmYes = 0;
    int funCOuntExist = 0;
    int month, finyear, curyear;

    SimpleDateFormat df = new SimpleDateFormat("yyyy");

    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dfMon = new SimpleDateFormat("MM");

    public JFrameUserProfileUpdate() {
        initComponents();
        jTextSupEmpNam.setEditable(false);
        showDate();
        showTime();
        computerName();
        findYear();
        jCheckHRAdmin.setText("<html>HR Month-end and TS reports</html>");
        jCheckFleetAdmin.setText("<html>Fleet Admin Central & Reports</html>");
        jCheckFinalApprover.setText("<html>Make Final (Head) Approver</html>");
    }

    public JFrameUserProfileUpdate(String empNum) {
        initComponents();
        jLabelEmp.setText(empNum);
        usrEmpNo = empNum;
        jLabelEmp.setVisible(false);
        findLogUser();
        findUserGrp();
        showDate();
        showTime();
        computerName();
        findYear();
        jCheckHRAdmin.setText("<html>HR Month-end and TS reports</html>");
        jCheckFleetAdmin.setText("<html>Fleet Admin Central & Reports</html>");
        jCheckFinalApprover.setText("<html>Make Final (Head) Approver</html>");

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

    void findProvince() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select distinct(province) from [ClaimsAppSysZvandiri].[dbo].[DistFacTab]");

            while (r.next()) {

                jComboProvince.addItem(r.getString("province"));

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findBankDet() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT distinct upper(BANK_NAME) FROM [ClaimsAppSysZvandiri].[dbo].[BankBranchCodeTab] order by 1");

            while (r.next()) {

                jComboBankName.addItem(r.getString(1));

            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findBankCode() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT distinct upper(Bank_Code) FROM [ClaimsAppSysZvandiri].[dbo].[BankBranchCodeTab] "
                    + "where Bank_Name='" + jComboBankName.getSelectedItem().toString() + "'");

            while (r.next()) {

                jTextBranchCode.setText(r.getString(1));

            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findJobTitle() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT upper(JOB_TITLE) FROM [HRLeaveSysZvandiri].[dbo].[RecJobTitleTab] "
                    + " where JOB_GRADE not in  ('Casual','Locum')  order by 1");

            while (r.next()) {

                jComboGenItem.addItem(r.getString(1));

            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findHRLeaveFun() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT DISTINCT FUN_GRP FROM  [HRLeaveSysZvandiri].[dbo].[Function_Tab]  WHERE FUN_APP = 'HR' order by 1");

            while (r.next()) {

                jComboGenItem.addItem(r.getString(1));

            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findFinFun() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT DISTINCT FUN_GRP FROM [HRLeaveSysZvandiri].[dbo].[Function_Tab] "
                    + "where FUN_APP = 'Finance' order by 1");

            while (r.next()) {

                jComboGenItem.addItem(r.getString(1));

            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findHeadFun() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT distinct PROJ_BRANCH FROM [HRLeaveSysZvandiri].[dbo].[ProjectTab] order by 1");

            while (r.next()) {

                jComboGenItem.addItem(r.getString(1));

            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findStockFun() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT DISTINCT FUN_GRP FROM [HRLeaveSysZvandiri].[dbo].[Function_Tab] where FUN_APP = 'blank' "
                    + "union SELECT DISTINCT FUN_GRP FROM [HRLeaveSysZvandiri].[dbo].[Function_Tab]"
                    + " where FUN_APP = 'Stock' order by 1");

            while (r.next()) {

                jComboGenItem.addItem(r.getString(1));

            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findVehFun() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT DISTINCT FUN_GRP FROM [HRLeaveSysZvandiri].[dbo].[Function_Tab] where FUN_APP = 'blank' "
                    + "union SELECT DISTINCT FUN_GRP FROM [HRLeaveSysZvandiri].[dbo].[Function_Tab]"
                    + " where FUN_APP = 'Fleet' order by 1");

            while (r.next()) {

                jComboGenItem.addItem(r.getString(1));

            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void clearCombo() {

        int itemCount = jComboGenItem.getItemCount();

        for (int i = 0; i < itemCount; i++) {
            jComboGenItem.removeItemAt(0);
        }
    }

    public void getLogNam() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT usrnam ,leaveTitle,sysReport,empStatus "
                    + "FROM [HRLeaveSysZvandiri].[dbo].[usrlogin] where "
                    + "EmpNum = '" + jTextEmpNum.getText() + "'");
            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                jTextusrLogNam.setText(rs.getString(1));
                jTextLeaveFunction.setText(rs.getString(2));
                if (("HRCreator".equals(rs.getString(3))) || ("Admin".equals(rs.getString(3)))) {
                    jCheckHRAdmin.setSelected(true);
                }

                if (rs.getInt(4) == 1) {
                    jTextAccStatus.setText("Active");
                    jCheckSuspendStatus.setText("Suspend");

                } else {
                    jTextAccStatus.setText("Suspended");
                    jCheckSuspendStatus.setText("Activate");
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void getUsrDet() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            st.executeQuery("SELECT EMP_NAM,EMP_TTL,EMP_MAIL,SUP_NAM,EMP_SUP_MAIL,SUP_EMP_NUM "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] where "
                    + "Emp_Num = '" + jTextEmpNum.getText() + "'");
            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                jTextFullName.setText(rs.getString(1));
                jTextJobTitle.setText(rs.getString(2));
                jTextUsrEmail.setText(rs.getString(3));
                jTextSupEmpNam.setText(rs.getString(4));
                jTextSupEmail.setText(rs.getString(5));
                jTextSupEmpNum.setText(rs.getString(6));

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void getVacDays() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT TotDays FROM [HRLeaveSysZvandiri].[dbo].[CumulatedVacDays] where "
                    + "EmpNum = '" + jTextEmpNum.getText() + "' and FinYear=" + finyear);
            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                jTextAnnualDays.setText(rs.getString(1));
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void getProvDist() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT EMP_PROVINCE,EMP_OFFICE FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetWS] where "
                    + "Emp_Num = '" + jTextEmpNum.getText() + "' ");
            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                jTextProvince.setText(rs.getString(1));
                jTextDistrict.setText(rs.getString(2));
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void getBankDet() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT EMP_BNK_NAM,ACC_NUM,BNK_CODE,VENDOR_NUM FROM [ClaimsAppSysZvandiri].[dbo].[EmpBankDetTab] where "
                    + "Emp_Num = '" + jTextEmpNum.getText() + "' ");
            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                jTextBankName.setText(rs.getString(1));
                jTextBankAcc.setText(rs.getString(2));
                jTextBankCode.setText(rs.getString(3));
                jTextEmpVendorNum.setText(rs.getString(4));
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void getFinFun() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT EMP_GRP  FROM [ClaimsAppSysZvandiri].[dbo].[EmpLogGrpTab] where "
                    + "Emp_Num = '" + jTextEmpNum.getText() + "' ");
            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                jTextFinFunction.setText(rs.getString(1));

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void getStockFun() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT STORE_FUNCTION FROM [ClaimsAppSysZvandiri].[dbo].[ProvStoresAdmTab] where "
                    + "Emp_Num = '" + jTextEmpNum.getText() + "' and STORE_FUNCTION in "
                    + "(SELECT fun_grp FROM [HRLeaveSysZvandiri].[dbo].[Function_Tab] where FUN_APP ='Stock') ");
            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                jTextStockFunction.setText(rs.getString(1));

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findBranch(String taskCode) {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT distinct BRANCH  FROM [ClaimsAppSysZvandiri].[dbo].[BudCodeTab] "
                    + "where BRANCH ='" + taskCode + "' order by 1");

            while (r.next()) {

                branchCode = r.getString(1);

            }

            conn.close();
        } catch (Exception e) {
            System.out.println();
        }
    }

    void getProjectAward() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT distinct AWARD_NAME FROM [HRLeaveSysZvandiri].[dbo].[ProjectTab] "
                    + "where PROJ_BRANCH = '" + jComboGenItem.getSelectedItem().toString() + "'order by 1");

            while (r.next()) {

                jLabelProjectAward.setText(r.getString(1));

            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void getProjectHOD() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT EMP_NAM FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] "
                    + "where EMP_NUM = (SELECT EMP_NUM FROM [ClaimsAppSysZvandiri].[dbo].[ProjHODTab] "
                    + "WHERE PROJ_BRANCH ='" + jLabelProjectHOD.getText() + "') ");
            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                headNam = rs.getString(1);

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void getFleetFun() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT STORE_FUNCTION FROM [ClaimsAppSysZvandiri].[dbo].[ProvStoresAdmTab] where "
                    + "Emp_Num = '" + jTextEmpNum.getText() + "' and STORE_FUNCTION in "
                    + "(SELECT fun_grp FROM [HRLeaveSysZvandiri].[dbo].[Function_Tab] where FUN_APP ='Fleet') ");
            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                jTextFleetFunction.setText(rs.getString(1));

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void getFleetAdmFun() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT STORE_FUNCTION FROM [ClaimsAppSysZvandiri].[dbo].[ProvStoresAdmTab] where "
                    + "Emp_Num = '" + jTextEmpNum.getText() + "' and STORE_FUNCTION in ('Reports') ");
            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                if ("Reports".equals(rs.getString(1))) {
                    jCheckFleetAdmin.setSelected(true);
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void disableFields() {
        jTextAccStatus.setEnabled(true);
        jTextEmpNum.setEnabled(true);
        jTextusrLogNam.setEnabled(true);
        jTextFullName.setEnabled(true);
        jTextUsrEmail.setEnabled(true);
        jTextJobTitle.setEnabled(true);
        jTextSupEmpNam.setEnabled(true);
        jTextSupEmail.setEnabled(true);
        jTextSupEmpNum.setEnabled(true);
//        jTextAnnualDays.setEnabled(true);
        jTextProvince.setEnabled(true);
        jTextDistrict.setEnabled(true);
        jTextBankName.setEnabled(true);
        jTextBankAcc.setEnabled(true);
        jTextBankCode.setEnabled(true);
        jTextLeaveFunction.setEnabled(true);
        jTextLeaveFunction.setEnabled(true);
        jTextFinFunction.setEnabled(true);
//        jTextStockFunction.setEnabled(true);
//        jTextFleetFunction.setEnabled(true);
//        jTextHRAdminFunction.setEnabled(true);
//        jTextFleetAdminFunction.setEnabled(true);
//        jCheckFinalApprover.setEnabled(true);
        jTextAccStatus.setEditable(false);
//        jTextEmpNum.setEditable(false);
        jTextusrLogNam.setEditable(false);
        jTextFullName.setEditable(false);
        jTextUsrEmail.setEditable(false);
        jTextJobTitle.setEditable(false);
        jTextSupEmpNam.setEditable(false);
        jTextSupEmail.setEditable(false);
        jTextSupEmpNum.setEditable(false);
        jTextProvince.setEditable(false);
        jTextDistrict.setEditable(false);
        jTextBankName.setEditable(false);
        jTextBankAcc.setEditable(false);
        jTextBankCode.setEditable(false);
        jTextLeaveFunction.setEditable(false);
        jTextLeaveFunction.setEditable(false);
        jTextFinFunction.setEditable(false);
        jTextEmpVendorNum.setEditable(false);
//        jTextStockFunction.setEditable(false);
//        jTextFleetFunction.setEditable(false);
//        jTextHRAdminFunction.setEditable(false);
//        jTextFleetAdminFunction.setEditable(false);
        // jCheckFinalApprover.setEnabled(false);
    }

    void resetFields() {
        jTextAccStatus.setText("");
//        jTextEmpNum.setText("");
        jTextusrLogNam.setText("");
        jTextFullName.setText("");
        jTextUsrEmail.setText("");
        jTextJobTitle.setText("");
        jTextSupEmpNam.setText("");
        jTextSupEmail.setText("");
        jTextSupEmpNum.setText("");
        jTextAnnualDays.setText("");
        jTextProvince.setText("");
        jTextDistrict.setText("");
        jTextBankName.setText("");
        jTextBankAcc.setText("");
        jTextBankCode.setText("");
        jTextLeaveFunction.setText("");
        jTextLeaveFunction.setText("");
        jTextFinFunction.setText("");
        jTextStockFunction.setText("");
        jTextFleetFunction.setText("");
//        jTextHRAdminFunction.setText("");
//        jTextFleetAdminFunction.setText("");
//        jCheckFinalApprover.setText("");
        jTextAccStatus.setText("");
//        jTextEmpNum.setText("");
        jTextusrLogNam.setText("");
        jTextFullName.setText("");
        jTextUsrEmail.setText("");
        jTextJobTitle.setText("");
        jTextSupEmpNam.setText("");
        jTextSupEmail.setText("");
        jTextSupEmpNum.setText("");
        jTextProvince.setText("");
        jTextDistrict.setText("");
        jTextBankName.setText("");
        jTextBankAcc.setText("");
        jTextBankCode.setText("");
        jTextLeaveFunction.setText("");
        jTextLeaveFunction.setText("");
        jTextFinFunction.setText("");
        jTextStockFunction.setText("");
        jTextFleetFunction.setText("");
        jTextEmpVendorNum.setText("");
        jLabelProjectAward.setText("");
//        jTextHRAdminFunction.setText("");
//        jTextFleetAdminFunction.setText("");
        // jCheckFinalApprover.setEnabled(false);
        jCheckHRAdmin.setSelected(false);
        jCheckFleetAdmin.setSelected(false);
        jCheckFinalApprover.setSelected(false);
        jCheckSuspendStatus.setSelected(false);
    }

    void disableSuspendFields() {
        jTextAccStatus.setEnabled(false);
        jTextEmpNum.setEnabled(false);
        jTextusrLogNam.setEnabled(false);
        jTextFullName.setEnabled(false);
        jTextUsrEmail.setEnabled(false);
        jTextJobTitle.setEnabled(false);
        jTextSupEmpNam.setEnabled(false);
        jTextSupEmail.setEnabled(false);
        jTextSupEmpNum.setEnabled(false);
        jTextAnnualDays.setEnabled(false);
        jTextProvince.setEnabled(false);
        jTextDistrict.setEnabled(false);
        jTextBankName.setEnabled(false);
        jTextBankAcc.setEnabled(false);
        jTextBankCode.setEnabled(false);
        jTextLeaveFunction.setEnabled(false);
        jTextLeaveFunction.setEnabled(false);
        jTextFinFunction.setEnabled(false);
//        jTextStockFunction.setEnabled(false);
//        jTextFleetFunction.setEnabled(false);
////        jTextHRAdminFunction.setEnabled(false);
////        jTextFleetAdminFunction.setEnabled(false);
//        jCheckFinalApprover.setEnabled(false);
//        jCheckHRAdmin.setEnabled(false);
//        jCheckFleetAdmin.setEnabled(false);
    }

//    void enableFields() {
//        jTextAccStatus.setEnabled(true);
//        jTextEmpNum.setEnabled(true);
//        jTextusrLogNam.setEnabled(true);
//        jTextusrLogNam.setEnabled(true);
//        jTextFullName.setEnabled(true);
//        jTextUsrEmail.setEnabled(true);
//        jTextJobTitle.setEnabled(true);
//        jTextSupEmpNam.setEnabled(true);
//        jTextSupEmail.setEnabled(true);
//        jTextSupEmpNum.setEnabled(true);
//        jTextAnnualDays.setEnabled(true);
//        jTextProvince.setEnabled(true);
//        jTextDistrict.setEnabled(true);
//        jTextBankName.setEnabled(true);
//        jTextBankAcc.setEnabled(true);
//        jTextBankCode.setEnabled(true);
//        jTextLeaveFunction.setEnabled(true);
//        jTextLeaveFunction.setEnabled(true);
//        jTextFinFunction.setEnabled(true);
//        jTextStockFunction.setEnabled(true);
//        jTextFleetFunction.setEnabled(true);
//        jTextHRAdminFunction.setEnabled(true);
//        jTextFleetAdminFunction.setEnabled(true);
//        jCheckFinalApprover.setEnabled(true);
//    }
    void searchSupName() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            int itemCount = jComboBoxSearchResult.getItemCount();

            for (int i = 0; i < itemCount; i++) {
                jComboBoxSearchResult.removeItemAt(0);
            }

            jComboBoxSearchResult.setSelectedIndex(-1);
            String searchNam = jTextFieldSearchNam.getText();

            System.out.println("name " + searchNam);
            st.executeQuery("SELECT EMP_NUM,EMP_NAM  FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] "
                    + "where upper(EMP_NAM) like upper('%" + searchNam + "%')");
            ResultSet r = st.getResultSet();

            while (r.next()) {
                jComboBoxSearchResult.addItem(r.getString(1) + " " + r.getString(2));

                jButtonOk.setVisible(true);
            }

            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void searchSupDetails(String supStr) {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            System.out.println("name " + supStr);
            st.executeQuery("SELECT EMP_NAM,EMP_MAIL,EMP_NUM FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] "
                    + "where upper(CONCAT(EMP_NUM,' ',EMP_NAM))  = upper('" + supStr + "')");
            ResultSet r = st.getResultSet();

            while (r.next()) {
                jTextSupEmpNam.setText(r.getString(1));
                jTextSupEmail.setText(r.getString(2));
                jTextSupEmpNum.setText(r.getString(3));

            }

            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void profileUdpAud(){
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");
            String sqlUpdFleGrp = "insert into [HRLeaveSysZvandiri].[dbo].[AuditUsrProfUpdTab] "
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            pst = conn.prepareStatement(sqlUpdFleGrp);
            pst.setString(1, jTextEmpNum.getText().toUpperCase());
            pst.setString(2, jTextusrLogNam.getText());
            pst.setString(3, jTextAccStatus.getText());
            pst.setString(4, jTextFullName.getText());
            pst.setString(5, jTextUsrEmail.getText());
            pst.setString(6, jTextJobTitle.getText());
            pst.setString(7, jTextSupEmpNum.getText().toUpperCase());
            pst.setString(8, jTextSupEmail.getText());
            pst.setString(9, jTextSupEmpNam.getText());
            pst.setString(10, jTextAnnualDays.getText());
            pst.setString(11, jTextProvince.getText());
            pst.setString(12, jTextDistrict.getText());
            pst.setString(13, jTextBankName.getText());
            pst.setString(14, jTextBankAcc.getText());
            pst.setString(15, jTextBankCode.getText());
            pst.setString(16, jTextLeaveFunction.getText());
            pst.setString(17, jTextFinFunction.getText());
            pst.setString(18, jTextStockFunction.getText());
            pst.setString(19, jTextFleetFunction.getText());
            if (!jCheckHRAdmin.isSelected()) {
                pst.setString(20, "No");
            } else {
                pst.setString(20, "Yes");
            }

            if (!jCheckFleetAdmin.isSelected()) {
                pst.setString(21, "No");
            } else {
                pst.setString(21, "Yes");
            }

            if (jCheckFinalApprover.isSelected()) {
                pst.setString(22, jTextEmpNum.getText().toUpperCase());
                pst.setString(23, jTextFullName.getText());
                pst.setString(24, jLabelProjectHOD.getText());
            } else {
                pst.setString(22, "");
                pst.setString(23, "");
                pst.setString(24, "");
            }

            pst.setString(25, jLabelLogNam.getText());
            pst.setString(26, jLabelDateCur.getText());
            pst.setString(27, jLabelTimeCur.getText());
            pst.setString(28, hostName);

            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    void updEmpRec() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");
            if (jCheckSuspendStatus.isSelected() && "Suspend".equals(jCheckSuspendStatus.getText())) {
                String sqlUpdUsrSta = "update  [HRLeaveSysZvandiri].[dbo].[usrlogin] set "
                        + "empStatus='0' where "
                        + "EmpNum ='" + jTextEmpNum.getText() + "'";
                pst = conn.prepareStatement(sqlUpdUsrSta);
                pst.executeUpdate();

            } else {
                String sqlUpdUsrSta = "update  [HRLeaveSysZvandiri].[dbo].[usrlogin] set "
                        + "empStatus='1' where "
                        + "EmpNum ='" + jTextEmpNum.getText() + "'";
                pst = conn.prepareStatement(sqlUpdUsrSta);
                pst.executeUpdate();

                String sqlUpdUsrEmpDetTab = "update [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] set "
                        + " SUP_EMP_NUM='" + jTextSupEmpNum.getText() + "',"
                        + " SUP_NAM='" + jTextSupEmpNam.getText() + "',"
                        + "EMP_SUP_MAIL='" + jTextSupEmail.getText() + "',"
                        + "EMP_TTL='" + jTextJobTitle.getText() + "' where "
                        + "Emp_Num ='" + jTextEmpNum.getText() + "'";
                pst = conn.prepareStatement(sqlUpdUsrEmpDetTab);
                pst.executeUpdate();
            }

            String sqlUpdProv = "update [ClaimsAppSysZvandiri].[dbo].[EmpDetWS] set "
                    + "EMP_PROVINCE ='" + jTextProvince.getText() + "',"
                    + "EMP_OFFICE='" + jTextDistrict.getText() + "' where "
                    + "Emp_Num ='" + jTextEmpNum.getText() + "'";
            pst = conn.prepareStatement(sqlUpdProv);
            pst.executeUpdate();

            String sqlUpdFinGrp = "update [ClaimsAppSysZvandiri].[dbo].[EmpLogGrpTab] set "
                    + "EMP_GRP='" + jTextFinFunction.getText() + "' where "
                    + "Emp_Num ='" + jTextEmpNum.getText() + "'";
            pst = conn.prepareStatement(sqlUpdFinGrp);
            pst.executeUpdate();

            if (jCheckHRAdmin.isSelected()) {
                String sqlUpdHRAdm = "update [HRLeaveSysZvandiri].[dbo].[usrlogin] set "
                        + "sysReport='HRCreator' where "
                        + "EmpNum ='" + jTextEmpNum.getText() + "'";
                pst = conn.prepareStatement(sqlUpdHRAdm);
                pst.executeUpdate();
            } else {
                String sqlUpdHRAdm = "update [HRLeaveSysZvandiri].[dbo].[usrlogin] set "
                        + "sysReport='' where "
                        + "EmpNum ='" + jTextEmpNum.getText() + "'";
                pst = conn.prepareStatement(sqlUpdHRAdm);
                pst.executeUpdate();
            }

            String sqlUpdUsrLogGrp = "update  [HRLeaveSysZvandiri].[dbo].[usrlogin] set "
                    + "leaveTitle='" + jTextLeaveFunction.getText() + "' where "
                    + "EmpNum ='" + jTextEmpNum.getText() + "'";
            pst = conn.prepareStatement(sqlUpdUsrLogGrp);
            pst.executeUpdate();

            String sqlDelUsrBnk = "delete [ClaimsAppSysZvandiri].[dbo].[EmpBankDetTab] where "
                    + "Emp_Num ='" + jTextEmpNum.getText() + "'";
            pst = conn.prepareStatement(sqlDelUsrBnk);
            pst.executeUpdate();
             
            String sqlusrdet = "insert into [ClaimsAppSysZvandiri].[dbo].[EmpBankDetTab] "
                    + "values (?,?,?,?,?)";
            System.out.println("ggg");
            pst1 = conn.prepareStatement(sqlusrdet);
            pst1.setString(1, jTextEmpNum.getText());
            pst1.setString(2, jTextBankName.getText());
            pst1.setString(3, jTextBankAcc.getText());
            pst1.setString(4, "0000");
            pst1.setString(5, jTextEmpVendorNum.getText());
         
             pst1.executeUpdate();

            if (jCheckFinalApprover.isSelected()) {
                findBranch(jLabelProjectHOD.getText());
                String sqlUpdPrjHOD = "delete [ClaimsAppSysZvandiri].[dbo].[ProjHODTab] where "
                        + "PROJ_BRANCH ='" + jLabelProjectHOD.getText() + "'";
                pst = conn.prepareStatement(sqlUpdPrjHOD);
                pst.executeUpdate();

                String sqlUpdFleGrp = "insert into [ClaimsAppSysZvandiri].[dbo].[ProjHODTab] values (?,?,?)";

                pst = conn.prepareStatement(sqlUpdFleGrp);
                pst.setString(1, jTextEmpNum.getText().toUpperCase());
                pst.setString(2, jLabelProjectHOD.getText());
                pst.setString(3, jLabelProjectAward.getText());
                pst.executeUpdate();
            }

//                String sqlUpdVacdays = "update [HRLeaveSysZvandiri].[dbo].[CumulatedVacDays] set "
//                        + "TotDays='" + jTextAnnualDays.getText() + "' where "
//                        + "EmpNum ='" + jTextEmpNum.getText() + "' and "
//                        + "FinYear='" + finyear + "'";
//                pst = conn.prepareStatement(sqlUpdVacdays);
//                pst.executeUpdate();
//                if (jCheckFleetAdmin.isSelected()) {
//                    try {
//
//                        Statement st = conn.createStatement();
//
//                        st.executeQuery("SELECT count(*) FROM [ClaimsAppSysZvandiri].[dbo].[ProvStoresAdmTab] where "
//                                + "Emp_Num = '" + jTextEmpNum.getText() + "' and STORE_FUNCTION in ('Reports') ");
//                        ResultSet r = st.getResultSet();
//
//                        while (r.next()) {
//                            fleetAdmYes = r.getInt(1);
//                        }
//
//                    } catch (Exception e) {
//                        System.out.println(e);
//                    }
//                }
//                if (jCheckFleetAdmin.isSelected() && fleetAdmYes == 0) {
//                    String sqlUpdFleGrp = "insert into [ClaimsAppSysZvandiri].[dbo].[ProvStoresAdmTab] values (?,?,?,?)";
//
//                    pst = conn.prepareStatement(sqlUpdFleGrp);
//                    pst.setString(1, jTextEmpNum.getText());
//                    pst.setString(2, jTextProvince.getText());
//                    pst.setString(3, jTextDistrict.getText());
//                    pst.setString(4, "Reports");
//
//                    pst.executeUpdate();
//                } else if (!jCheckFleetAdmin.isSelected()) {
//                    String sqlUpdFleGrp = "delete [ClaimsAppSysZvandiri].[dbo].[ProvStoresAdmTab] where "
//                            + "STORE_FUNCTION='Reports' and "
//                            + "Emp_Num ='" + jTextEmpNum.getText() + "'";
//                    pst = conn.prepareStatement(sqlUpdFleGrp);
//                    pst.executeUpdate();
//                }
//                if (jTextFleetFunction.getText().trim().length() > 0) {
//                    try {
//
//                        Statement st = conn.createStatement();
//
//                        st.executeQuery("SELECT count(*) FROM [ClaimsAppSysZvandiri].[dbo].[ProvStoresAdmTab] where "
//                                + "Emp_Num = '" + jTextEmpNum.getText() + "' and STORE_FUNCTION in "
//                                + "(SELECT fun_grp FROM [HRLeaveSysZvandiri].[dbo].[Function_Tab] where FUN_APP ='Fleet') ");
//                        ResultSet r = st.getResultSet();
//
//                        while (r.next()) {
//                            fleet = r.getInt(1);
//                        }
//
//                    } catch (Exception e) {
//                        System.out.println(e);
//                    }
//                }
//
//                if (jTextFleetFunction.getText().trim().length() > 0 && fleet == 0) {
//                    String sqlUpdFleGrp = "insert into [ClaimsAppSysZvandiri].[dbo].[ProvStoresAdmTab] values (?,?,?,?)";
//
//                    pst = conn.prepareStatement(sqlUpdFleGrp);
//                    pst.setString(1, jTextEmpNum.getText());
//                    pst.setString(2, jTextProvince.getText());
//                    pst.setString(3, jTextDistrict.getText());
//                    pst.setString(4, jTextFleetFunction.getText());
//
//                    pst.executeUpdate();
//                } else if (jTextFleetFunction.getText().trim().length() == 0) {
//                    String sqlUpdFleGrp = "delete [ClaimsAppSysZvandiri].[dbo].[ProvStoresAdmTab] where "
//                            + "STORE_FUNCTION in (SELECT fun_grp FROM [HRLeaveSysZvandiri].[dbo].[Function_Tab] "
//                            + "where FUN_APP ='Fleet') and "
//                            + "Emp_Num ='" + jTextEmpNum.getText() + "'";
//                    pst = conn.prepareStatement(sqlUpdFleGrp);
//                    pst.executeUpdate();
//                }
//
//                if (jTextStockFunction.getText().trim().length() > 0) {
//                    try {
//
//                        Statement st = conn.createStatement();
//
//                        st.executeQuery("SELECT count(*) FROM [ClaimsAppSysZvandiri].[dbo].[ProvStoresAdmTab] where "
//                                + "Emp_Num = '" + jTextEmpNum.getText() + "' and STORE_FUNCTION in "
//                                + "(SELECT fun_grp FROM [HRLeaveSysZvandiri].[dbo].[Function_Tab] where FUN_APP ='Fleet') ");
//                        ResultSet r = st.getResultSet();
//
//                        while (r.next()) {
//                            store = r.getInt(1);
//                        }
//
//                    } catch (Exception e) {
//                        System.out.println(e);
//                    }
//                }
//
//                if (jTextStockFunction.getText().trim().length() > 0 && store == 0) {
//                    String sqlUpdFleGrp = "insert into [ClaimsAppSysZvandiri].[dbo].[ProvStoresAdmTab] values (?,?,?,?)";
//
//                    pst = conn.prepareStatement(sqlUpdFleGrp);
//                    pst.setString(1, jTextEmpNum.getText());
//                    pst.setString(2, jTextProvince.getText());
//                    pst.setString(3, jTextDistrict.getText());
//                    pst.setString(4, jTextStockFunction.getText());
//
//                    pst.executeUpdate();
//                } else if (jTextStockFunction.getText().trim().length() == 0) {
//                    String sqlUpdFleGrp = "delete [ClaimsAppSysZvandiri].[dbo].[ProvStoresAdmTab] where "
//                            + "STORE_FUNCTION in (SELECT fun_grp FROM [HRLeaveSysZvandiri].[dbo].[Function_Tab] "
//                            + "where FUN_APP ='Stock') and "
//                            + "Emp_Num ='" + jTextEmpNum.getText() + "'";
//                    pst = conn.prepareStatement(sqlUpdFleGrp);
//                    pst.executeUpdate();
//                }
//
//            }
            JOptionPane.showMessageDialog(this, "<html> <b>" + jTextFullName.getText() + "</b> profile update complete.</html>");
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
        jDialogDistrict = new javax.swing.JDialog();
        jPanel10 = new javax.swing.JPanel();
        jLabelHeader1 = new javax.swing.JLabel();
        jButtonOkDistrict = new javax.swing.JButton();
        jButtonCancelDistrict = new javax.swing.JButton();
        jLabelProvinceFacility = new javax.swing.JLabel();
        jComboProvince = new javax.swing.JComboBox<>();
        jComboDistrict = new javax.swing.JComboBox<>();
        jLabelDistrictFacility = new javax.swing.JLabel();
        jDialogBankDetail = new javax.swing.JDialog();
        jPanel12 = new javax.swing.JPanel();
        jLabelHeader = new javax.swing.JLabel();
        jButtonOkBank = new javax.swing.JButton();
        jButtonCancelBank = new javax.swing.JButton();
        jLabelBankName = new javax.swing.JLabel();
        jComboBankName = new javax.swing.JComboBox<>();
        jLabelBranchName = new javax.swing.JLabel();
        jTextBranchCode = new javax.swing.JTextField();
        jTextBankAccNo = new javax.swing.JTextField();
        jLabelBranchName1 = new javax.swing.JLabel();
        jTextBranchVendorNum = new javax.swing.JTextField();
        jLabelBranchName2 = new javax.swing.JLabel();
        jDialogGen = new javax.swing.JDialog();
        jPanel13 = new javax.swing.JPanel();
        jLabelHeaderName = new javax.swing.JLabel();
        jButtonOkGen = new javax.swing.JButton();
        jButtonCancelGen = new javax.swing.JButton();
        jLabelItemName = new javax.swing.JLabel();
        jComboGenItem = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jLabellogo = new javax.swing.JLabel();
        jTextEmpNum = new javax.swing.JTextField();
        jLabelEmpNum = new javax.swing.JLabel();
        jTextFullName = new javax.swing.JTextField();
        jLabelFName = new javax.swing.JLabel();
        jTextUsrEmail = new javax.swing.JTextField();
        jLabelSupNam = new javax.swing.JLabel();
        jLabelUsrEmail = new javax.swing.JLabel();
        jLabelLeaveFunction = new javax.swing.JLabel();
        jLabelAnnualDays = new javax.swing.JLabel();
        jTextAnnualDays = new javax.swing.JTextField();
        jLabelusrLogNam = new javax.swing.JLabel();
        jTextusrLogNam = new javax.swing.JTextField();
        jTextSupEmpNam = new javax.swing.JTextField();
        jLabelFinFunction = new javax.swing.JLabel();
        jLabelJobTitle = new javax.swing.JLabel();
        jTextStockFunction = new javax.swing.JTextField();
        jLabelBank = new javax.swing.JLabel();
        jTextBankAcc = new javax.swing.JTextField();
        jLabelBankAcc = new javax.swing.JLabel();
        jLabelDateCur = new javax.swing.JLabel();
        jLabelTimeCur = new javax.swing.JLabel();
        jLabelProvince = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabelStockFunction = new javax.swing.JLabel();
        jTextJobTitle = new javax.swing.JTextField();
        jTextProvince = new javax.swing.JTextField();
        jTextLeaveFunction = new javax.swing.JTextField();
        jTextFinFunction = new javax.swing.JTextField();
        jLabelFleetFunction = new javax.swing.JLabel();
        jTextBankCode = new javax.swing.JTextField();
        jTextFleetFunction = new javax.swing.JTextField();
        jTextSupEmail = new javax.swing.JTextField();
        jLabelSupEmail = new javax.swing.JLabel();
        jLabelDistrict = new javax.swing.JLabel();
        jTextDistrict = new javax.swing.JTextField();
        jTextBankName = new javax.swing.JTextField();
        jTextSupEmpNum = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextAccStatus = new javax.swing.JTextField();
        jCheckSuspendStatus = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jCheckHRAdmin = new javax.swing.JCheckBox();
        jCheckFleetAdmin = new javax.swing.JCheckBox();
        jCheckFinalApprover = new javax.swing.JCheckBox();
        jLabelLog = new javax.swing.JLabel();
        jLabelLogNam = new javax.swing.JLabel();
        jLabelEmp = new javax.swing.JLabel();
        jLabelProjectHOD = new javax.swing.JLabel();
        jLabelProjectAward = new javax.swing.JLabel();
        jLabelEmpVendorNum = new javax.swing.JLabel();
        jTextEmpVendorNum = new javax.swing.JTextField();
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
        jDialogSearchID.setLocation(new java.awt.Point(400, 400));
        jDialogSearchID.setMinimumSize(new java.awt.Dimension(500, 200));

        jPanelSearchID.setBackground(new java.awt.Color(7, 163, 163));
        jPanelSearchID.setMinimumSize(new java.awt.Dimension(500, 200));
        jPanelSearchID.setPreferredSize(new java.awt.Dimension(500, 200));
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
        jButtonSearch.setBounds(330, 20, 90, 30);

        jComboBoxSearchResult.setPreferredSize(new java.awt.Dimension(70, 30));
        jPanelSearchID.add(jComboBoxSearchResult);
        jComboBoxSearchResult.setBounds(10, 70, 290, 30);

        jButtonOk.setText("OK");
        jButtonOk.setPreferredSize(new java.awt.Dimension(73, 30));
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });
        jPanelSearchID.add(jButtonOk);
        jButtonOk.setBounds(330, 70, 90, 30);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Enter search term e.g. Joe and click search button");
        jPanelSearchID.add(jLabel4);
        jLabel4.setBounds(12, 3, 270, 13);

        javax.swing.GroupLayout jDialogSearchIDLayout = new javax.swing.GroupLayout(jDialogSearchID.getContentPane());
        jDialogSearchID.getContentPane().setLayout(jDialogSearchIDLayout);
        jDialogSearchIDLayout.setHorizontalGroup(
            jDialogSearchIDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelSearchID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogSearchIDLayout.setVerticalGroup(
            jDialogSearchIDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelSearchID, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jDialogDistrict.setAlwaysOnTop(true);
        jDialogDistrict.setLocation(new java.awt.Point(450, 300));
        jDialogDistrict.setMinimumSize(new java.awt.Dimension(600, 230));
        jDialogDistrict.setResizable(false);

        jPanel10.setBackground(new java.awt.Color(204, 153, 14));
        jPanel10.setLayout(null);

        jLabelHeader1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelHeader1.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeader1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelHeader1.setText("Employee District");
        jPanel10.add(jLabelHeader1);
        jLabelHeader1.setBounds(170, 10, 230, 40);

        jButtonOkDistrict.setText("Ok ");
        jButtonOkDistrict.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkDistrictActionPerformed(evt);
            }
        });
        jPanel10.add(jButtonOkDistrict);
        jButtonOkDistrict.setBounds(180, 160, 80, 21);

        jButtonCancelDistrict.setText("Cancel");
        jButtonCancelDistrict.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelDistrictActionPerformed(evt);
            }
        });
        jPanel10.add(jButtonCancelDistrict);
        jButtonCancelDistrict.setBounds(300, 160, 80, 21);

        jLabelProvinceFacility.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelProvinceFacility.setText("Province");
        jPanel10.add(jLabelProvinceFacility);
        jLabelProvinceFacility.setBounds(20, 70, 70, 30);

        jComboProvince.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jComboProvinceMouseEntered(evt);
            }
        });
        jComboProvince.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboProvinceActionPerformed(evt);
            }
        });
        jComboProvince.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboProvinceKeyPressed(evt);
            }
        });
        jPanel10.add(jComboProvince);
        jComboProvince.setBounds(150, 70, 230, 30);

        jComboDistrict.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jComboDistrictMouseEntered(evt);
            }
        });
        jComboDistrict.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboDistrictActionPerformed(evt);
            }
        });
        jPanel10.add(jComboDistrict);
        jComboDistrict.setBounds(150, 110, 230, 30);

        jLabelDistrictFacility.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelDistrictFacility.setText("District");
        jPanel10.add(jLabelDistrictFacility);
        jLabelDistrictFacility.setBounds(20, 110, 70, 30);

        javax.swing.GroupLayout jDialogDistrictLayout = new javax.swing.GroupLayout(jDialogDistrict.getContentPane());
        jDialogDistrict.getContentPane().setLayout(jDialogDistrictLayout);
        jDialogDistrictLayout.setHorizontalGroup(
            jDialogDistrictLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
        );
        jDialogDistrictLayout.setVerticalGroup(
            jDialogDistrictLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
        );

        jDialogBankDetail.setAlwaysOnTop(true);
        jDialogBankDetail.setLocation(new java.awt.Point(450, 300));
        jDialogBankDetail.setMinimumSize(new java.awt.Dimension(600, 230));
        jDialogBankDetail.setResizable(false);

        jPanel12.setBackground(new java.awt.Color(102, 153, 255));
        jPanel12.setLayout(null);

        jLabelHeader.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelHeader.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeader.setText("BANK DETAILS");
        jPanel12.add(jLabelHeader);
        jLabelHeader.setBounds(170, 10, 230, 40);

        jButtonOkBank.setText("Ok ");
        jButtonOkBank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkBankActionPerformed(evt);
            }
        });
        jPanel12.add(jButtonOkBank);
        jButtonOkBank.setBounds(180, 160, 80, 21);

        jButtonCancelBank.setText("Cancel");
        jButtonCancelBank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelBankActionPerformed(evt);
            }
        });
        jPanel12.add(jButtonCancelBank);
        jButtonCancelBank.setBounds(300, 160, 80, 21);

        jLabelBankName.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelBankName.setForeground(new java.awt.Color(255, 255, 255));
        jLabelBankName.setText("Bank Name");
        jPanel12.add(jLabelBankName);
        jLabelBankName.setBounds(10, 70, 110, 30);

        jComboBankName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jComboBankNameMouseEntered(evt);
            }
        });
        jComboBankName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBankNameActionPerformed(evt);
            }
        });
        jComboBankName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBankNameKeyPressed(evt);
            }
        });
        jPanel12.add(jComboBankName);
        jComboBankName.setBounds(140, 70, 230, 30);

        jLabelBranchName.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelBranchName.setForeground(new java.awt.Color(255, 255, 255));
        jLabelBranchName.setText("Bank Code");
        jPanel12.add(jLabelBranchName);
        jLabelBranchName.setBounds(380, 70, 80, 30);

        jTextBranchCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextBranchCodeActionPerformed(evt);
            }
        });
        jPanel12.add(jTextBranchCode);
        jTextBranchCode.setBounds(500, 70, 80, 30);
        jPanel12.add(jTextBankAccNo);
        jTextBankAccNo.setBounds(140, 110, 230, 30);

        jLabelBranchName1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelBranchName1.setForeground(new java.awt.Color(255, 255, 255));
        jLabelBranchName1.setText("Bank Account No.");
        jPanel12.add(jLabelBranchName1);
        jLabelBranchName1.setBounds(10, 110, 130, 30);

        jTextBranchVendorNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextBranchVendorNumActionPerformed(evt);
            }
        });
        jPanel12.add(jTextBranchVendorNum);
        jTextBranchVendorNum.setBounds(500, 110, 80, 30);

        jLabelBranchName2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelBranchName2.setForeground(new java.awt.Color(255, 255, 255));
        jLabelBranchName2.setText("Vendor No.");
        jPanel12.add(jLabelBranchName2);
        jLabelBranchName2.setBounds(380, 110, 80, 30);

        javax.swing.GroupLayout jDialogBankDetailLayout = new javax.swing.GroupLayout(jDialogBankDetail.getContentPane());
        jDialogBankDetail.getContentPane().setLayout(jDialogBankDetailLayout);
        jDialogBankDetailLayout.setHorizontalGroup(
            jDialogBankDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
        );
        jDialogBankDetailLayout.setVerticalGroup(
            jDialogBankDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
        );

        jDialogGen.setAlwaysOnTop(true);
        jDialogGen.setLocation(new java.awt.Point(450, 300));
        jDialogGen.setMinimumSize(new java.awt.Dimension(520, 180));
        jDialogGen.setResizable(false);

        jPanel13.setBackground(new java.awt.Color(0, 153, 153));
        jPanel13.setMinimumSize(new java.awt.Dimension(520, 180));
        jPanel13.setLayout(null);

        jLabelHeaderName.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelHeaderName.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeaderName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelHeaderName.setText("BANK DETAILS");
        jPanel13.add(jLabelHeaderName);
        jLabelHeaderName.setBounds(80, 10, 320, 40);

        jButtonOkGen.setText("Ok ");
        jButtonOkGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkGenActionPerformed(evt);
            }
        });
        jPanel13.add(jButtonOkGen);
        jButtonOkGen.setBounds(170, 110, 80, 21);

        jButtonCancelGen.setText("Cancel");
        jButtonCancelGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelGenActionPerformed(evt);
            }
        });
        jPanel13.add(jButtonCancelGen);
        jButtonCancelGen.setBounds(290, 110, 80, 21);

        jLabelItemName.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabelItemName.setForeground(new java.awt.Color(255, 255, 255));
        jLabelItemName.setText("Bank Name");
        jPanel13.add(jLabelItemName);
        jLabelItemName.setBounds(20, 60, 110, 30);

        jComboGenItem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        jComboGenItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jComboGenItemMouseEntered(evt);
            }
        });
        jComboGenItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboGenItemActionPerformed(evt);
            }
        });
        jComboGenItem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboGenItemKeyPressed(evt);
            }
        });
        jPanel13.add(jComboGenItem);
        jComboGenItem.setBounds(150, 60, 340, 30);

        javax.swing.GroupLayout jDialogGenLayout = new javax.swing.GroupLayout(jDialogGen.getContentPane());
        jDialogGen.getContentPane().setLayout(jDialogGenLayout);
        jDialogGenLayout.setHorizontalGroup(
            jDialogGenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jDialogGenLayout.setVerticalGroup(
            jDialogGenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("OPHID - New User Creation");

        jPanel1.setPreferredSize(new java.awt.Dimension(800, 600));
        jPanel1.setLayout(null);

        jLabellogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanel1.add(jLabellogo);
        jLabellogo.setBounds(10, 0, 230, 120);

        jTextEmpNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextEmpNumActionPerformed(evt);
            }
        });
        jPanel1.add(jTextEmpNum);
        jTextEmpNum.setBounds(120, 170, 60, 30);

        jLabelEmpNum.setText("Employee Number");
        jPanel1.add(jLabelEmpNum);
        jLabelEmpNum.setBounds(10, 170, 110, 30);

        jTextFullName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFullNameActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFullName);
        jTextFullName.setBounds(120, 210, 250, 30);

        jLabelFName.setText("User Name");
        jPanel1.add(jLabelFName);
        jLabelFName.setBounds(10, 210, 90, 30);
        jPanel1.add(jTextUsrEmail);
        jTextUsrEmail.setBounds(490, 210, 220, 30);

        jLabelSupNam.setText("Supervisor Name");
        jPanel1.add(jLabelSupNam);
        jLabelSupNam.setBounds(10, 250, 90, 30);

        jLabelUsrEmail.setText("User email");
        jPanel1.add(jLabelUsrEmail);
        jLabelUsrEmail.setBounds(410, 210, 70, 30);

        jLabelLeaveFunction.setText("Leave");
        jPanel1.add(jLabelLeaveFunction);
        jLabelLeaveFunction.setBounds(10, 370, 110, 30);

        jLabelAnnualDays.setText("Annual Vacaion Days");
        jPanel1.add(jLabelAnnualDays);
        jLabelAnnualDays.setBounds(10, 290, 110, 30);

        jTextAnnualDays.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAnnualDaysActionPerformed(evt);
            }
        });
        jPanel1.add(jTextAnnualDays);
        jTextAnnualDays.setBounds(120, 290, 50, 30);

        jLabelusrLogNam.setText("login Name");
        jPanel1.add(jLabelusrLogNam);
        jLabelusrLogNam.setBounds(390, 170, 80, 30);

        jTextusrLogNam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextusrLogNamActionPerformed(evt);
            }
        });
        jPanel1.add(jTextusrLogNam);
        jTextusrLogNam.setBounds(490, 170, 290, 30);

        jTextSupEmpNam.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextSupEmpNamMouseClicked(evt);
            }
        });
        jTextSupEmpNam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextSupEmpNamActionPerformed(evt);
            }
        });
        jPanel1.add(jTextSupEmpNam);
        jTextSupEmpNam.setBounds(120, 250, 250, 30);

        jLabelFinFunction.setText("Finance");
        jPanel1.add(jLabelFinFunction);
        jLabelFinFunction.setBounds(320, 370, 90, 30);

        jLabelJobTitle.setText("Job Title");
        jPanel1.add(jLabelJobTitle);
        jLabelJobTitle.setBounds(760, 210, 70, 30);

        jTextStockFunction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextStockFunctionMouseClicked(evt);
            }
        });
        jTextStockFunction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextStockFunctionActionPerformed(evt);
            }
        });
        jPanel1.add(jTextStockFunction);
        jTextStockFunction.setBounds(710, 370, 150, 30);

        jLabelBank.setText("Bank");
        jPanel1.add(jLabelBank);
        jLabelBank.setBounds(10, 330, 80, 30);
        jPanel1.add(jTextBankAcc);
        jTextBankAcc.setBounds(580, 330, 240, 30);

        jLabelBankAcc.setText("Bank Account No.");
        jPanel1.add(jLabelBankAcc);
        jLabelBankAcc.setBounds(480, 330, 100, 30);

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
        jLabelTimeCur.setBounds(1170, 0, 100, 30);

        jLabelProvince.setText("Province");
        jPanel1.add(jLabelProvince);
        jLabelProvince.setBounds(270, 290, 50, 30);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("USER PROFILE UPDATE");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(460, 50, 360, 40);

        jLabelStockFunction.setText("Stocks");
        jPanel1.add(jLabelStockFunction);
        jLabelStockFunction.setBounds(620, 370, 80, 30);

        jTextJobTitle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextJobTitleMouseClicked(evt);
            }
        });
        jTextJobTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextJobTitleActionPerformed(evt);
            }
        });
        jPanel1.add(jTextJobTitle);
        jTextJobTitle.setBounds(890, 210, 250, 30);

        jTextProvince.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextProvinceMouseClicked(evt);
            }
        });
        jTextProvince.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextProvinceActionPerformed(evt);
            }
        });
        jPanel1.add(jTextProvince);
        jTextProvince.setBounds(330, 290, 240, 30);

        jTextLeaveFunction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextLeaveFunctionMouseClicked(evt);
            }
        });
        jTextLeaveFunction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextLeaveFunctionActionPerformed(evt);
            }
        });
        jPanel1.add(jTextLeaveFunction);
        jTextLeaveFunction.setBounds(120, 370, 150, 30);

        jTextFinFunction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFinFunctionMouseClicked(evt);
            }
        });
        jTextFinFunction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFinFunctionActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFinFunction);
        jTextFinFunction.setBounds(410, 370, 150, 30);

        jLabelFleetFunction.setText("Fleet ");
        jPanel1.add(jLabelFleetFunction);
        jLabelFleetFunction.setBounds(910, 370, 80, 30);
        jPanel1.add(jTextBankCode);
        jTextBankCode.setBounds(890, 330, 150, 30);

        jTextFleetFunction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFleetFunctionMouseClicked(evt);
            }
        });
        jTextFleetFunction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFleetFunctionActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFleetFunction);
        jTextFleetFunction.setBounds(1000, 370, 150, 30);
        jPanel1.add(jTextSupEmail);
        jTextSupEmail.setBounds(510, 250, 250, 30);

        jLabelSupEmail.setText("Supervisor email");
        jPanel1.add(jLabelSupEmail);
        jLabelSupEmail.setBounds(410, 250, 90, 30);

        jLabelDistrict.setText("District");
        jPanel1.add(jLabelDistrict);
        jLabelDistrict.setBounds(700, 290, 50, 30);

        jTextDistrict.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextDistrictMouseClicked(evt);
            }
        });
        jTextDistrict.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextDistrictActionPerformed(evt);
            }
        });
        jPanel1.add(jTextDistrict);
        jTextDistrict.setBounds(760, 290, 240, 30);

        jTextBankName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextBankNameMouseClicked(evt);
            }
        });
        jPanel1.add(jTextBankName);
        jTextBankName.setBounds(120, 330, 250, 30);
        jPanel1.add(jTextSupEmpNum);
        jTextSupEmpNum.setBounds(830, 250, 70, 30);

        jLabel2.setText("Account Current Status ");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(860, 170, 130, 25);
        jPanel1.add(jTextAccStatus);
        jTextAccStatus.setBounds(990, 170, 100, 30);

        jCheckSuspendStatus.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jCheckSuspendStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckSuspendStatusActionPerformed(evt);
            }
        });
        jPanel1.add(jCheckSuspendStatus);
        jCheckSuspendStatus.setBounds(1140, 170, 90, 30);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(null);

        jCheckHRAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckHRAdminActionPerformed(evt);
            }
        });
        jPanel2.add(jCheckHRAdmin);
        jCheckHRAdmin.setBounds(20, 10, 220, 40);
        jPanel2.add(jCheckFleetAdmin);
        jCheckFleetAdmin.setBounds(350, 10, 210, 40);

        jCheckFinalApprover.setText(" Make Final (Head) Approver");
        jCheckFinalApprover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckFinalApproverActionPerformed(evt);
            }
        });
        jPanel2.add(jCheckFinalApprover);
        jCheckFinalApprover.setBounds(690, 10, 210, 40);

        jPanel1.add(jPanel2);
        jPanel2.setBounds(120, 440, 930, 60);

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

        jLabelProjectHOD.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel1.add(jLabelProjectHOD);
        jLabelProjectHOD.setBounds(1060, 440, 110, 30);

        jLabelProjectAward.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel1.add(jLabelProjectAward);
        jLabelProjectAward.setBounds(1060, 480, 250, 30);

        jLabelEmpVendorNum.setText("Vendor Number");
        jPanel1.add(jLabelEmpVendorNum);
        jLabelEmpVendorNum.setBounds(210, 170, 90, 30);

        jTextEmpVendorNum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextEmpVendorNumKeyTyped(evt);
            }
        });
        jPanel1.add(jTextEmpVendorNum);
        jTextEmpVendorNum.setBounds(310, 170, 60, 30);

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
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1317, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 737, Short.MAX_VALUE)
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(1331, 807));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextAnnualDaysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAnnualDaysActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAnnualDaysActionPerformed


    private void jTextusrLogNamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextusrLogNamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextusrLogNamActionPerformed

    private void jTextFieldSearchNamFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldSearchNamFocusGained

    }//GEN-LAST:event_jTextFieldSearchNamFocusGained

    private void jTextFieldSearchNamFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldSearchNamFocusLost

    }//GEN-LAST:event_jTextFieldSearchNamFocusLost

    private void jTextFieldSearchNamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldSearchNamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldSearchNamActionPerformed

    private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchActionPerformed

        try {
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//
//            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
//                    + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNHR + ";password=" + c.usrPHR + ";");
//
//            Statement st = conn.createStatement();
//
//            int itemCount = jComboBoxSearchResult.getItemCount();
//
//            for (int i = 0; i < itemCount; i++) {
//                jComboBoxSearchResult.removeItemAt(0);
//            }
//            jComboBoxSearchResult.setSelectedIndex(-1);
//            String searchNam = jTextFieldSearchNam.getText();
//            st.executeQuery("SELECT distinct [USRNAM] FROM [HRLeaveSysZvandiri].[dbo].[empdetails] "
//                    + "WHERE [USRNAM] like upper('%" + searchNam + "%')");
//            ResultSet r = st.getResultSet();
//
//            while (r.next()) {
//                jComboBoxSearchResult.addItem(r.getString(1));
//                jButtonOk.setVisible(true);
//                //                jButtonSearch.setVisible(false);
//
//            }
//
//            conn.close();
            searchSupName();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonSearchActionPerformed

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        String str = jComboBoxSearchResult.getSelectedItem().toString();
        searchSupDetails(str);
//        jTextSupEmpNam.setText(str);
        jTextFieldSearchNam.setText("");
        jDialogSearchID.dispose();
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jLabelDateCurComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_jLabelDateCurComponentAdded

    }//GEN-LAST:event_jLabelDateCurComponentAdded

    private void jTextFullNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFullNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFullNameActionPerformed

    private void jTextEmpNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextEmpNumActionPerformed
        resetFields();
        getLogNam();
        getUsrDet();
//        getVacDays();
        getProvDist();
        getBankDet();
        getFinFun();
//        getStockFun();
//        getFleetFun();
//        getFleetAdmFun();
        jTextStockFunction.setEnabled(false);
        jTextFleetFunction.setEnabled(false);
        jCheckHRAdmin.setEnabled(false);
        jCheckFleetAdmin.setEnabled(false);
        jTextAnnualDays.setEnabled(false);

        disableFields();

        if ("Suspended".equals(jTextAccStatus.getText())) {
            jCheckSuspendStatus.setText("Activate");
            disableSuspendFields();
        } else {
            jCheckSuspendStatus.setText("Suspend");
        }


    }//GEN-LAST:event_jTextEmpNumActionPerformed

    private void jTextSupEmpNamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextSupEmpNamActionPerformed

    }//GEN-LAST:event_jTextSupEmpNamActionPerformed

    private void jTextSupEmpNamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextSupEmpNamMouseClicked
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            if ((!jCheckSuspendStatus.isSelected()) && jTextFullName.getText().trim().length() > 0) {
                jDialogSearchID.setVisible(true);
            } else if (("Activate".equals(jCheckSuspendStatus.getText())) && (jCheckSuspendStatus.isSelected())
                    && jTextFullName.getText().trim().length() > 0) {
                jDialogSearchID.setVisible(true);
            } else if (jTextFullName.getText().trim().length() == 0) {
                JOptionPane.showMessageDialog(this, "Note that record is empty. Please retrieve record first.");
            }
        }
    }//GEN-LAST:event_jTextSupEmpNamMouseClicked

    private void jButtonOkDistrictActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkDistrictActionPerformed

        jTextProvince.setText(jComboProvince.getSelectedItem().toString());
        jTextDistrict.setText(jComboDistrict.getSelectedItem().toString());
        jDialogDistrict.setVisible(false);

    }//GEN-LAST:event_jButtonOkDistrictActionPerformed

    private void jButtonCancelDistrictActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelDistrictActionPerformed
        jDialogDistrict.setVisible(false);
    }//GEN-LAST:event_jButtonCancelDistrictActionPerformed

    private void jComboProvinceMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboProvinceMouseEntered
        //        jComboDistrict.removeAllItems();
        //        jComboFacility.removeAllItems();
    }//GEN-LAST:event_jComboProvinceMouseEntered

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

        }
    }//GEN-LAST:event_jComboProvinceActionPerformed

    private void jComboProvinceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboProvinceKeyPressed

    }//GEN-LAST:event_jComboProvinceKeyPressed

    private void jComboDistrictMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboDistrictMouseEntered
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
    }//GEN-LAST:event_jComboDistrictMouseEntered

    private void jComboDistrictActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboDistrictActionPerformed

    }//GEN-LAST:event_jComboDistrictActionPerformed

    private void jTextProvinceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextProvinceActionPerformed

    }//GEN-LAST:event_jTextProvinceActionPerformed

    private void jTextDistrictActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextDistrictActionPerformed

    }//GEN-LAST:event_jTextDistrictActionPerformed

    private void jTextProvinceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextProvinceMouseClicked
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            if ((!jCheckSuspendStatus.isSelected()) && jTextFullName.getText().trim().length() > 0) {
                findProvince();
                jDialogDistrict.setVisible(true);
            } else if (("Activate".equals(jCheckSuspendStatus.getText())) && (jCheckSuspendStatus.isSelected())
                    && jTextFullName.getText().trim().length() > 0) {
                findProvince();
                jDialogDistrict.setVisible(true);
            } else if (jTextFullName.getText().trim().length() == 0) {
                JOptionPane.showMessageDialog(this, "Note that record is empty. Please retrieve record first.");
            }
        }

    }//GEN-LAST:event_jTextProvinceMouseClicked

    private void jTextDistrictMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextDistrictMouseClicked
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            if ((!jCheckSuspendStatus.isSelected()) && jTextFullName.getText().trim().length() > 0) {
                findProvince();
                jDialogDistrict.setVisible(true);
            } else if (("Activate".equals(jCheckSuspendStatus.getText())) && (jCheckSuspendStatus.isSelected())
                    && jTextFullName.getText().trim().length() > 0) {
                findProvince();
                jDialogDistrict.setVisible(true);
            } else if (jTextFullName.getText().trim().length() == 0) {
                JOptionPane.showMessageDialog(this, "Note that record is empty. Please retrieve record first.");
            }
        }
    }//GEN-LAST:event_jTextDistrictMouseClicked

    private void jCheckFinalApproverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckFinalApproverActionPerformed
//        try {
//            if (jCheckFinalApprover.isSelected() && jTextFullName.getText().trim().length() > 0) {
//                int selectedOption = JOptionPane.showConfirmDialog(null,
//                        "<html><b>" + headNam + "</b> is the current final approver.Do you want to make <b>"
//                        + jTextFullName.getText() + "</b> final approver.</html>",
//                        "Choose",
//                        JOptionPane.YES_NO_OPTION);
//
//                if (selectedOption == JOptionPane.YES_OPTION) {
////            new JFrameUserProfileUpdate().setVisible(true);
////            setVisible(false);
//                } else {
//
//                    jCheckFinalApprover.setSelected(false);
//                }
//
//            } else if ((jCheckFinalApprover.isSelected()) && jTextFullName.getText().trim().length() == 0) {
//                JOptionPane.showMessageDialog(this, "Note that record is empty. Please retrieve record first.");
//                jCheckFinalApprover.setSelected(false);
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }

        try {
            if (jCheckFinalApprover.isSelected() && jTextFullName.getText().trim().length() > 0) {
                clearCombo();
                if ((!jCheckSuspendStatus.isSelected()) && jTextFullName.getText().trim().length() > 0) {
                    headFun = "Y";
                    findHeadFun();
                    jDialogGen.setVisible(true);
                    jLabelHeaderName.setText("Employement Details");
                    jLabelItemName.setText("Project ");
                } else if (("Activate".equals(jCheckSuspendStatus.getText())) && (jCheckSuspendStatus.isSelected())
                        && jTextFullName.getText().trim().length() > 0) {
                    headFun = "Y";
                    findHeadFun();;
                    jDialogGen.setVisible(true);
                    jLabelHeaderName.setText("Employement Details");
                    jLabelItemName.setText("Project");
                } else if (jTextFullName.getText().trim().length() == 0) {
                    JOptionPane.showMessageDialog(this, "Note that record is empty. Please retrieve record first.");
                }
            } else {
                jLabelProjectHOD.setVisible(true);
                jLabelProjectHOD.setText("");
                jLabelProjectAward.setText("");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_jCheckFinalApproverActionPerformed

    private void jCheckSuspendStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckSuspendStatusActionPerformed
        try {
            if ((jCheckSuspendStatus.isSelected()) && ("Active".equals(jTextAccStatus.getText())) && jTextFullName.getText().trim().length() > 0) {
                int selectedOption = JOptionPane.showConfirmDialog(null,
                        "<html>Are you sure you want to suspend the account?</html>",
                        "Choose",
                        JOptionPane.YES_NO_OPTION);

                if (selectedOption == JOptionPane.YES_OPTION) {

                    disableSuspendFields();

                } else {
                    jCheckSuspendStatus.setSelected(false);

                }
            } else if ((jCheckSuspendStatus.isSelected()) && jTextFullName.getText().trim().length() == 0) {
                JOptionPane.showMessageDialog(this, "Note that record is empty. Please retrieve record first.");
                jCheckSuspendStatus.setSelected(false);
            } else if ((!jCheckSuspendStatus.isSelected()) && jTextFullName.getText().trim().length() > 0) {

                System.out.println("testing");
                disableFields();
                jCheckFinalApprover.setEnabled(true);
                jCheckHRAdmin.setEnabled(true);
                jCheckFleetAdmin.setEnabled(true);
            } else if (("Activate".equals(jCheckSuspendStatus.getText())) && (jCheckSuspendStatus.isSelected())
                    && jTextFullName.getText().trim().length() > 0) {

                System.out.println("testing2");
                disableFields();
                jCheckFinalApprover.setEnabled(true);
                jCheckHRAdmin.setEnabled(true);
                jCheckFleetAdmin.setEnabled(true);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_jCheckSuspendStatusActionPerformed

    private void jTextBankNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextBankNameMouseClicked
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            if ((!jCheckSuspendStatus.isSelected()) && jTextFullName.getText().trim().length() > 0) {
                findBankDet();
                jDialogBankDetail.setVisible(true);
            } else if (("Activate".equals(jCheckSuspendStatus.getText())) && (jCheckSuspendStatus.isSelected())
                    && jTextFullName.getText().trim().length() > 0) {
                findBankDet();
                jDialogBankDetail.setVisible(true);
            } else if (jTextFullName.getText().trim().length() == 0) {
                JOptionPane.showMessageDialog(this, "Note that record is empty. Please retrieve record first.");
            }
        }
    }//GEN-LAST:event_jTextBankNameMouseClicked

    private void jButtonOkBankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkBankActionPerformed
        jTextBankName.setText(jComboBankName.getSelectedItem().toString());
        jTextBankAcc.setText(jTextBankAccNo.getText());
        jTextBankCode.setText(jTextBranchCode.getText());
        jTextEmpVendorNum.setText(jTextBranchVendorNum.getText());
        jDialogBankDetail.setVisible(false);

    }//GEN-LAST:event_jButtonOkBankActionPerformed

    private void jButtonCancelBankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelBankActionPerformed
        jDialogBankDetail.setVisible(false);
    }//GEN-LAST:event_jButtonCancelBankActionPerformed

    private void jComboBankNameMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBankNameMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBankNameMouseEntered

    private void jComboBankNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBankNameActionPerformed
        findBankCode();
    }//GEN-LAST:event_jComboBankNameActionPerformed

    private void jComboBankNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBankNameKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBankNameKeyPressed

    private void jTextBranchCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextBranchCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextBranchCodeActionPerformed

    private void jButtonOkGenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkGenActionPerformed

        if ("Y".equals(jobtitle)) {
            jTextJobTitle.setText(jComboGenItem.getSelectedItem().toString());
            jDialogGen.dispose();
            jobtitle = "N";
        } else if ("Y".equals(HRleaveFun)) {
            jTextLeaveFunction.setText(jComboGenItem.getSelectedItem().toString());
            jDialogGen.dispose();
            HRleaveFun = "N";
        } else if ("Y".equals(finFun)) {
            jTextFinFunction.setText(jComboGenItem.getSelectedItem().toString());
            jDialogGen.dispose();
            finFun = "N";
        } else if ("Y".equals(stockFun)) {
            jTextStockFunction.setText(jComboGenItem.getSelectedItem().toString());
            jDialogGen.dispose();
            stockFun = "N";
        } else if ("Y".equals(fleetFun)) {
            jTextFleetFunction.setText(jComboGenItem.getSelectedItem().toString());
            jDialogGen.dispose();
            fleetFun = "N";

        } else if ("Y".equals(headFun)) {
            jLabelProjectHOD.setText(jComboGenItem.getSelectedItem().toString());
            jDialogGen.dispose();
            headFun = "N";
            jLabelProjectHOD.setVisible(true);
            getProjectAward();
            getProjectHOD();
            int selectedOption = JOptionPane.showConfirmDialog(null,
                    "<html><b>" + headNam + "</b> is the current final approver.Do you want to make <b>"
                    + jTextFullName.getText() + "</b> final approver.</html>",
                    "Choose",
                    JOptionPane.YES_NO_OPTION);

            if (selectedOption == JOptionPane.YES_OPTION) {
//            new JFrameUserProfileUpdate().setVisible(true);
//            setVisible(false);
            } else {

                jCheckFinalApprover.setSelected(false);

                jLabelProjectHOD.setVisible(true);
                jLabelProjectHOD.setText("");
                jLabelProjectAward.setText("");

            }

        }
    }//GEN-LAST:event_jButtonOkGenActionPerformed

    private void jButtonCancelGenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelGenActionPerformed
        jDialogGen.dispose();
        jobtitle = "N";
        HRleaveFun = "N";
        finFun = "N";
        stockFun = "N";
    }//GEN-LAST:event_jButtonCancelGenActionPerformed

    private void jComboGenItemMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboGenItemMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboGenItemMouseEntered

    private void jComboGenItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboGenItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboGenItemActionPerformed

    private void jComboGenItemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboGenItemKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboGenItemKeyPressed

    private void jTextJobTitleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextJobTitleMouseClicked
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            clearCombo();
            if ((!jCheckSuspendStatus.isSelected()) && jTextFullName.getText().trim().length() > 0) {
                jobtitle = "Y";
                findJobTitle();
                jDialogGen.setVisible(true);
                jLabelHeaderName.setText("Employement Details");
                jLabelItemName.setText("Job Title");
            } else if (jTextFullName.getText().trim().length() == 0) {
                JOptionPane.showMessageDialog(this, "Note that record is empty. Please retrieve record first.");
            }
        }
    }//GEN-LAST:event_jTextJobTitleMouseClicked

    private void jTextLeaveFunctionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextLeaveFunctionMouseClicked
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            clearCombo();
            if ((!jCheckSuspendStatus.isSelected()) && jTextFullName.getText().trim().length() > 0) {
                HRleaveFun = "Y";
                findHRLeaveFun();
                jDialogGen.setVisible(true);
                jLabelHeaderName.setText("Employement Details");
                jLabelItemName.setText("HR Function");
            } else if (jTextFullName.getText().trim().length() == 0) {
                JOptionPane.showMessageDialog(this, "Note that record is empty. Please retrieve record first.");
            }
        }
    }//GEN-LAST:event_jTextLeaveFunctionMouseClicked

    private void jTextLeaveFunctionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextLeaveFunctionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextLeaveFunctionActionPerformed

    private void jTextJobTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextJobTitleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextJobTitleActionPerformed

    private void jTextFinFunctionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFinFunctionMouseClicked
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            clearCombo();
            if ((!jCheckSuspendStatus.isSelected()) && jTextFullName.getText().trim().length() > 0) {
                finFun = "Y";
                findFinFun();
                jDialogGen.setVisible(true);
                jLabelHeaderName.setText("Employement Details");
                jLabelItemName.setText("Fin Function");
            } else if (("Activate".equals(jCheckSuspendStatus.getText())) && (jCheckSuspendStatus.isSelected())
                    && jTextFullName.getText().trim().length() > 0) {
                finFun = "Y";
                findFinFun();
                jDialogGen.setVisible(true);
                jLabelHeaderName.setText("Employement Details");
                jLabelItemName.setText("Fin Function");
            } else if (jTextFullName.getText().trim().length() == 0) {
                JOptionPane.showMessageDialog(this, "Note that record is empty. Please retrieve record first.");
            }
        }
    }//GEN-LAST:event_jTextFinFunctionMouseClicked

    private void jTextStockFunctionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextStockFunctionMouseClicked
//        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
//            clearCombo();
//            if ((!jCheckSuspendStatus.isSelected()) && jTextFullName.getText().trim().length() > 0) {
//                stockFun = "Y";
//                findStockFun();
//                jDialogGen.setVisible(true);
//                jLabelHeaderName.setText("Employement Details");
//                jLabelItemName.setText("Stock Function");
//            } else if (("Activate".equals(jCheckSuspendStatus.getText())) && (jCheckSuspendStatus.isSelected())
//                    && jTextFullName.getText().trim().length() > 0) {
//                stockFun = "Y";
//                findStockFun();
//                jDialogGen.setVisible(true);
//                jLabelHeaderName.setText("Employement Details");
//                jLabelItemName.setText("Stock Function");
//
//            } else if (jTextFullName.getText().trim().length() == 0) {
//                JOptionPane.showMessageDialog(this, "Note that record is empty. Please retrieve record first.");
//            }
//        }
    }//GEN-LAST:event_jTextStockFunctionMouseClicked

    private void jCheckHRAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckHRAdminActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckHRAdminActionPerformed

    private void jTextFleetFunctionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFleetFunctionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFleetFunctionActionPerformed

    private void jTextFleetFunctionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFleetFunctionMouseClicked
//        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
//            clearCombo();
//            if ((!jCheckSuspendStatus.isSelected()) && jTextFullName.getText().trim().length() > 0) {
//                fleetFun = "Y";
//                findVehFun();
//                jDialogGen.setVisible(true);
//                jLabelHeaderName.setText("Employement Details");
//                jLabelItemName.setText("Fleet Function");
//            } else if (("Activate".equals(jCheckSuspendStatus.getText())) && (jCheckSuspendStatus.isSelected())
//                    && jTextFullName.getText().trim().length() > 0) {
//                fleetFun = "Y";
//                findVehFun();
//                jDialogGen.setVisible(true);
//                jLabelHeaderName.setText("Employement Details");
//                jLabelItemName.setText("Fleet Function");
//            } else if (jTextFullName.getText().trim().length() == 0) {
//                JOptionPane.showMessageDialog(this, "Note that record is empty. Please retrieve record first.");
//            }
//        }
    }//GEN-LAST:event_jTextFleetFunctionMouseClicked

    private void jTextFinFunctionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFinFunctionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFinFunctionActionPerformed

    private void jTextStockFunctionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextStockFunctionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextStockFunctionActionPerformed

    private void jMenuItemCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCloseActionPerformed
        new JFrameFinSysLogin().setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemCloseActionPerformed

    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItemExitActionPerformed

    private void jMenuFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuFileActionPerformed

    }//GEN-LAST:event_jMenuFileActionPerformed

    private void jMenuItemSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSubmitActionPerformed
        try {
            if (!jCheckSuspendStatus.isSelected() && "Suspended".equals(jTextAccStatus.getText())) {

                JOptionPane.showMessageDialog(this, "<html> Please request for profile activtaion before proceeding by clicking on <b>activate</b>.</html>");
                jTextEmpNum.requestFocusInWindow();
            } else {
                updEmpRec();
                profileUdpAud();

                new JFrameUserProfileUpdate(jLabelEmp.getText()).setVisible(true);
                setVisible(false);
            }

        } catch (Exception e) {
            System.out.println(e);

        }
    }//GEN-LAST:event_jMenuItemSubmitActionPerformed

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

    private void jMenuItemUserCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemUserCreateActionPerformed
        new JFrameFixedUserCreation(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemUserCreateActionPerformed

    private void jTextEmpVendorNumKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextEmpVendorNumKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextEmpVendorNumKeyTyped

    private void jTextBranchVendorNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextBranchVendorNumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextBranchVendorNumActionPerformed

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
            java.util.logging.Logger.getLogger(JFrameUserProfileUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameUserProfileUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameUserProfileUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameUserProfileUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrameUserProfileUpdate().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancelBank;
    private javax.swing.JButton jButtonCancelDistrict;
    private javax.swing.JButton jButtonCancelGen;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JButton jButtonOkBank;
    private javax.swing.JButton jButtonOkDistrict;
    private javax.swing.JButton jButtonOkGen;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JCheckBox jCheckFinalApprover;
    private javax.swing.JCheckBox jCheckFleetAdmin;
    private javax.swing.JCheckBox jCheckHRAdmin;
    private javax.swing.JCheckBox jCheckSuspendStatus;
    private javax.swing.JComboBox<String> jComboBankName;
    private javax.swing.JComboBox<String> jComboBoxSearchResult;
    private javax.swing.JComboBox<String> jComboDistrict;
    private javax.swing.JComboBox<String> jComboGenItem;
    private javax.swing.JComboBox<String> jComboProvince;
    private javax.swing.JDialog jDialogBankDetail;
    private javax.swing.JDialog jDialogDistrict;
    private javax.swing.JDialog jDialogGen;
    private javax.swing.JDialog jDialogSearchID;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelAnnualDays;
    private javax.swing.JLabel jLabelBank;
    private javax.swing.JLabel jLabelBankAcc;
    private javax.swing.JLabel jLabelBankName;
    private javax.swing.JLabel jLabelBranchName;
    private javax.swing.JLabel jLabelBranchName1;
    private javax.swing.JLabel jLabelBranchName2;
    private javax.swing.JLabel jLabelDateCur;
    private javax.swing.JLabel jLabelDistrict;
    private javax.swing.JLabel jLabelDistrictFacility;
    private javax.swing.JLabel jLabelEmp;
    private javax.swing.JLabel jLabelEmpNum;
    private javax.swing.JLabel jLabelEmpVendorNum;
    private javax.swing.JLabel jLabelFName;
    private javax.swing.JLabel jLabelFinFunction;
    private javax.swing.JLabel jLabelFleetFunction;
    private javax.swing.JLabel jLabelHeader;
    private javax.swing.JLabel jLabelHeader1;
    private javax.swing.JLabel jLabelHeaderName;
    private javax.swing.JLabel jLabelItemName;
    private javax.swing.JLabel jLabelJobTitle;
    private javax.swing.JLabel jLabelLeaveFunction;
    private javax.swing.JLabel jLabelLog;
    public javax.swing.JLabel jLabelLogNam;
    private javax.swing.JLabel jLabelProjectAward;
    private javax.swing.JLabel jLabelProjectHOD;
    private javax.swing.JLabel jLabelProvince;
    private javax.swing.JLabel jLabelProvinceFacility;
    private javax.swing.JLabel jLabelStockFunction;
    private javax.swing.JLabel jLabelSupEmail;
    private javax.swing.JLabel jLabelSupNam;
    private javax.swing.JLabel jLabelTimeCur;
    private javax.swing.JLabel jLabelUsrEmail;
    private javax.swing.JLabel jLabellogo;
    private javax.swing.JLabel jLabelusrLogNam;
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
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
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
    private javax.swing.JTextField jTextAccStatus;
    private javax.swing.JTextField jTextAnnualDays;
    private javax.swing.JTextField jTextBankAcc;
    private javax.swing.JTextField jTextBankAccNo;
    private javax.swing.JTextField jTextBankCode;
    private javax.swing.JTextField jTextBankName;
    private javax.swing.JTextField jTextBranchCode;
    private javax.swing.JTextField jTextBranchVendorNum;
    private javax.swing.JTextField jTextDistrict;
    private javax.swing.JTextField jTextEmpNum;
    private javax.swing.JTextField jTextEmpVendorNum;
    private javax.swing.JTextField jTextFieldSearchNam;
    private javax.swing.JTextField jTextFinFunction;
    private javax.swing.JTextField jTextFleetFunction;
    private javax.swing.JTextField jTextFullName;
    private javax.swing.JTextField jTextJobTitle;
    private javax.swing.JTextField jTextLeaveFunction;
    private javax.swing.JTextField jTextProvince;
    private javax.swing.JTextField jTextStockFunction;
    private javax.swing.JTextField jTextSupEmail;
    private javax.swing.JTextField jTextSupEmpNam;
    private javax.swing.JTextField jTextSupEmpNum;
    private javax.swing.JTextField jTextUsrEmail;
    private javax.swing.JTextField jTextusrLogNam;
    // End of variables declaration//GEN-END:variables
}
