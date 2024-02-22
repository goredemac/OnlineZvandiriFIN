/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClaimAppendix1;

import ClaimPlanPerDiem.JFrameMnthPlanPerDiemEdit;
import ClaimPlanPerDiem.JFrameMnthPlanPerDiemCreate;
import javax.swing.JOptionPane;
import ClaimAppendix2.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Timer;
import ClaimReports.*;
import ClaimPlan.*;
import ClaimPlanPerDiem.*;
import java.io.File;
import java.net.InetAddress;
import java.sql.PreparedStatement;
import utils.*;
import java.text.DecimalFormat;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author cgoredema
 */
public class JFrameMain extends javax.swing.JFrame {

    connCred c = new connCred();
    String usrGrp, attFileName, reqType, actionCode, statusCode, sendFrom, dateSent,
            sendToWithDrawEmpNam, sendToWithDrawEmpEmail, empOff, dialogEmpNam, strVehReg,
            vehDistrict;
    savePDFToDB pdfDB = new savePDFToDB();
    connSaveFile attL = new connSaveFile();
    recruitRecSend recSend = new recruitRecSend();
    PreparedStatement pst = null;
    DefaultTableModel modelFuelDetail;
    Date curYear = new Date();
    java.util.Date todayDate = new Date();
    SimpleDateFormat df = new SimpleDateFormat("yyyy");
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String hostName = "";
    String radStatus = "";
    int existCount = 0;
    int existCountVoucher = 0;
    int existCountClr = 0;
    int existCountBat = 0;
    int existCountClrSp = 0;
    int stockTakeCountExist = 0;
    int vehCount = 0;
    int vehMonRepExist = 0;
    int countAdmExist = 0;
    int fleetUsrCount = 0;

    /**
     * Creates new form JFrameMain
     */
    public JFrameMain() {
        initComponents();
        findProvince();
        modelFuelDetail = (DefaultTableModel) jTableFuelDetails.getModel();
    }

    public JFrameMain(String usrNum) {

        initComponents();
        modelFuelDetail = (DefaultTableModel) jTableFuelDetails.getModel();
        jLabelEmp.setText(usrNum);
        jLabelEmp.setVisible(false);
        findUser();
        showDate();
        showTime();

        findUserGrp();
        computerName();

        if (!"Administrator".equals(usrGrp)) {
            jMenuItemUserProfUpd.setEnabled(false);
            jMenuItemUserCreate.setEnabled(false);
        }

    }

    void showTime() {
        new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date d = new Date();
                SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss a");
                jLabelGenTime.setText(s.format(d));

            }
        }) {

        }.start();

    }

    void showDate() {
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        jLabelGenDate.setText(s.format(d));

    }

    void computerName() {

        try {
            java.net.InetAddress addr = InetAddress.getLocalHost();
            hostName = addr.getHostName();

        } catch (Exception ex) {
            System.out.println("Hostname can not be resolved");
        }
    }

    void findUser() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select b.EMP_NAM,a.EMP_OFFICE FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetWS] a  "
                    + "join [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] b on a.EMP_NUM = b.EMP_NUM "
                    + "where b.EMP_NUM = '" + jLabelEmp.getText() + "'");

            while (r.next()) {

                jLabelGenLogNam.setText(r.getString(1));
                empOff = r.getString(2);
                dialogEmpNam = r.getString(1);
                jLabelEmpNam.setText(r.getString(1));

            }

            //                 conn.close();
        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "Failed to Connect to Database (Province1).Try Again", "Error Connection", JOptionPane.WARNING_MESSAGE);
//            System.exit(0);
            System.out.println(e);
        }
    }

    void findUserAdmin() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[ProvStoresAdmTab] where "
                    + "STORE_FUNCTION='Vehicle Approver' and EMP_OFF='Central Office' and EMP_NUM= '" + jLabelEmp.getText() + "'");

            while (r.next()) {

                countAdmExist = r.getInt(1);
            }

            //                 conn.close();
        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "Failed to Connect to Database (Province1).Try Again", "Error Connection", JOptionPane.WARNING_MESSAGE);
//            System.exit(0);
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

    void findVehicle() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd
                    + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            ResultSet r1 = st1.executeQuery("SELECT count(*)  FROM [OphidLogBook].[dbo].[ophid_vehicles]"
                    + " where upper(license_number) =upper('" + strVehReg + "')");

            while (r1.next()) {

                vehCount = r1.getInt(1);
            }

            findVehicleDistrict();

            if (vehCount == 0) {
                JOptionPane.showMessageDialog(jDialogFuelView, "Invalid Registration Number. Please check and correct.",
                        "Input Error ", JOptionPane.ERROR_MESSAGE);
                jTextVehicleNo.requestFocusInWindow();
                jTextVehicleNo.setFocusable(true);
                jTextVehicleNo.setText("");

            } else if (!empOff.equals(vehDistrict) && fleetUsrCount == 0
                    && !("Administrator".equals(usrGrp))) {
                JOptionPane.showMessageDialog(jDialogFuelView, "<html>Vehicle does not belong to <b>" + empOff + "</b>."
                        + " Please check and correct.</html>",
                        "Input Error ", JOptionPane.ERROR_MESSAGE);
                jTextVehicleNo.requestFocusInWindow();
                jTextVehicleNo.setFocusable(true);
                jTextVehicleNo.setText("");
            } else {
                getFuelDetails();
            }

            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        //   monVehRet();
    }

    void findVehicleDistrict() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd
                    + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            ResultSet r1 = st1.executeQuery("SELECT district_name  FROM [OphidLogBook].[dbo].[ophid_vehicles] a "
                    + " join [OphidLogBook].[dbo].[ophid_districts] b  on a.district_id = b.id "
                    + " where upper(license_number) =upper('" + strVehReg + "')");

            while (r1.next()) {

                vehDistrict = r1.getString(1);

            }

            ResultSet r = st.executeQuery("SELECT count(*)  "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[ProvStoresAdmTab] "
                    + "where STORE_FUNCTION = 'Reports' "
                    + "and EMP_NUM  ='" + jLabelEmp.getText() + "' "
                    + "and EMP_OFF='" + empOff + "'");

            while (r.next()) {

                fleetUsrCount = r.getInt(1);

            }
        } catch (Exception e) {
            System.out.println(e);
        }
        //   monVehRet();
    }

    void getFuelDetails() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();

            st.executeQuery("SELECT  distinct EMP_NAM \"DIVER NAME\", "
                    + "convert(varchar(10),top_up_date_time,23) \"TOPUP DATE\" , convert(varchar(10),top_up_date_time,24) \"TOPUP TIME\","
                    + "fuel_card_no \"CARD NO.\",service_provider \"SERVICE PROVIDER\" ,receipt_number \"RECEIPT NO.\", litres \"LITERS\","
                    + "fuel_cost \"FUEL COST\" ,approval_status "
                    + "FROM [OphidLogBook].[dbo].[fuel_topup] a join [OphidLogBook].[dbo].[ophid_vehicles] b "
                    + "on a.vehicle_number = b.license_number join [OphidLogBook].[dbo].[ophid_districts] c "
                    + "on c.id = b.district_id join [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] d on a.driver = d.EMP_NUM "
                    + "join [ClaimsAppSysZvandiri].[dbo].[BudCodeTab] e  "
                    + "on c.district_name COLLATE DATABASE_DEFAULT = e.Office COLLATE DATABASE_DEFAULT "
                    + "where  convert(varchar(10),top_up_date_time,23) >= '" + formatter.format(jDateFrom.getDate()) + "' "
                    + "and convert(varchar(10),top_up_date_time,23) <= '" + formatter.format(jDateTo.getDate()) + "' "
                    + "and e.[Desc 1] COLLATE DATABASE_DEFAULT ='Fuel' COLLATE DATABASE_DEFAULT "
                    + "and upper(vehicle_number) =upper('" + strVehReg + "') "
                    + "union "
                    + "SELECT  distinct EMP_NAM \"DIVER NAME\", "
                    + "convert(varchar(10),top_up_date_time,23) \"TOPUP DATE\" , convert(varchar(10),top_up_date_time,24) \"TOPUP TIME\","
                    + "fuel_card_no \"CARD NO.\",service_provider \"SERVICE PROVIDER\" ,receipt_number \"RECEIPT NO.\", litres \"LITERS\","
                    + "fuel_cost \"FUEL COST\" ,approval_status "
                    + "FROM [OphidLogBook].[dbo].[fuel_topup] a join [OphidLogBook].[dbo].[ophid_vehicles] b "
                    + "on a.vehicle_number = b.license_number join [OphidLogBook].[dbo].[ophid_districts] c "
                    + "on c.id = b.district_id join [HRLeaveSysZvandiri].[dbo].[CasualEmpTab] d on a.driver = d.EMP_NUM "
                    + "join [ClaimsAppSysZvandiri].[dbo].[BudCodeTab] e  "
                    + "on c.district_name COLLATE DATABASE_DEFAULT = e.Office COLLATE DATABASE_DEFAULT "
                    + "where  convert(varchar(10),top_up_date_time,23) >= '" + formatter.format(jDateFrom.getDate()) + "' "
                    + "and convert(varchar(10),top_up_date_time,23) <= '" + formatter.format(jDateTo.getDate()) + "' "
                    + "and e.[Desc 1] COLLATE DATABASE_DEFAULT ='Fuel' COLLATE DATABASE_DEFAULT "
                    + "and upper(vehicle_number) =upper('" + strVehReg + "')");

            ResultSet r = st.getResultSet();
            while (r.next()) {
                // new DecimalFormat("0.00").format(
                String fuelL = String.format("%.2f", r.getDouble(7));
                String fuelC = String.format("%.2f", r.getDouble(8));

                modelFuelDetail.insertRow(modelFuelDetail.getRowCount(), new Object[]{r.getString(1), r.getString(2), r.getString(3),
                    r.getString(4), r.getString(5), r.getString(6), fuelL, fuelC, r.getString(9)});

            }

            if (jTableFuelDetails.getModel().getRowCount() == 0) {
                JOptionPane.showMessageDialog(jDialogFuelView, "<html>No record exist for the searched entry. "
                        + "Vehicle Reg No. <b>" + strVehReg.toUpperCase() + "</html>");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findProvince() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            jComboProvince.insertItemAt("", 0);
            //      jComboProvince.setSelectedIndex(-1);
            ResultSet r = st.executeQuery("select distinct(province) from [ClaimsAppSysZvandiri].[dbo].[DistFacTab]");

            while (r.next()) {

                jComboProvince.addItem(r.getString("province"));

            }

            //                 conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void normalDataRetrive() {
        try {
            findExistClr(jTextVoucherNumClr.getText());
            if (existCountClr > 0) {
                jLabelRefDateClr.setText("");
                jLabelIssueEmpNumClr.setText("");
                jLabelIssueToNameClr.setText("");
                jLabelWorkstationProvinceClr.setText("");
                jLabelWorkstationDistrictClr.setText("");
                jTextTotAmountClr.setText("");
                jLabelAttDocFilePathClr.setText("");
                jLabelAttDocNameClr.setText("");
                jTextAttActivityClr.setText("");
                jLabelRefNumClr.setText("");
                findExist(jTextVoucherNumClr.getText(), reqType);
                if (existCount == 0) {
                    fetchdataClr(jTextVoucherNumClr.getText());
                    findSendFrom(jTextVoucherNumClr.getText());
                } else if (existCountClr > 0) {
                    JOptionPane.showMessageDialog(jDialogLetterClear, "Cash withdrawal letter has already been Proceesed. Please check and try again.");
                    jTextVoucherNumClr.requestFocusInWindow();
                    jTextVoucherNumClr.setFocusable(true);
                }
            } else {
                JOptionPane.showMessageDialog(jDialogLetterClear, "No cash withdrawal letter exist with this reference number. Please check and try again.");
                jTextVoucherNumClr.requestFocusInWindow();
                jTextVoucherNumClr.setFocusable(true);
                clearFields();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void searchName() {
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
            st.executeQuery("SELECT distinct [EMP_NAM]\n"
                    + "  FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] WHERE [EMP_NAM] like upper(\n" + "'%" + searchNam + "%')");
            ResultSet r = st.getResultSet();

            while (r.next()) {
                jComboBoxSearchResult.addItem(r.getString(1));
                jButtonOk.setVisible(true);
                //                jButtonSearch.setVisible(false);

            }

            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void searchWithDrawName() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            int itemCount = jComboBoxWithdrawSearchResult.getItemCount();

            for (int i = 0; i < itemCount; i++) {
                jComboBoxWithdrawSearchResult.removeItemAt(0);
            }
            jComboBoxWithdrawSearchResult.setSelectedIndex(-1);
            String searchNam = jTextFieldSearchWithdrawNam.getText();
            st.executeQuery("SELECT distinct [EMP_NAM]\n"
                    + "  FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] WHERE [EMP_NAM] like upper(\n" + "'%" + searchNam + "%')");
            ResultSet r = st.getResultSet();

            while (r.next()) {
                jComboBoxWithdrawSearchResult.addItem(r.getString(1));
                jButtonWithdrawOk.setVisible(true);
                //                jButtonSearch.setVisible(false);

            }

            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdata(String searchRef) {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT SERIAL,REF_DAT,EMP_NUM,EMP_NAM,"
                    + "EMP_PROV,EMP_OFF,ACT_TOT_AMT,REF_YEAR FROM ClaimsAppSysZvandiri.dbo.ClaimMeetGenTab"
                    + " where concat(SERIAL,REF_NUM)='" + searchRef + "' and concat(SERIAL,REF_NUM) in "
                    + "(SELECT CONCAT(SERIAL,REF_NUM) "
                    + " FROM [ClaimsAppSysZvandiri].[dbo].[BatRunTab] where serial ='RM') and doc_ver = 5");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                jLabelRefYear.setText(r.getString(8));
                jLabelSerial.setText(r.getString(1));
                jLabelRefDate.setText(r.getString(2));
                jLabelIssueEmpNum.setText(r.getString(3));
                jLabelIssueToName.setText(r.getString(4));
                jLabelWorkstationProvince.setText(r.getString(5));
                jLabelWorkstationDistrict.setText(r.getString(6));
                jLabelTotAmount.setText(r.getString(7));

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdataClr(String searchRef) {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT a.REF_NUM ,a.BAT_NUM ,REF_DATE,a.VOUCHER_NUM ,"
                    + "EMP_NUM ,EMP_NAM ,EMP_PROV ,EMP_OFF ,ACT_TOT_AMT,DATE_SEND_COLLECTED,EMP_NUM_WITHDRAW,"
                    + "EMP_NAM_WITHDRAW,REF_YEAR  "
                    + "  FROM ClaimsAppSysZvandiri.dbo.ClaimLetterMgtTab a join [ClaimsAppSysZvandiri].[dbo].[ClaimLetterMgtWFActTab] b "
                    + "on a.REF_NUM = b.REF_NUM and a.SERIAL = b.SERIAL and a.DOC_VER = b.DOC_VER and a.ACT_REC_STA = b.ACT_REC_STA"
                    + " where UPPER(a.VOUCHER_NUM)=upper('" + searchRef + "') and a.ACT_REC_STA in ('D','SD')");

            ResultSet r = st.getResultSet();
            while (r.next()) {
                if (r.getInt(1) == 0) {
                    jLabelSerialClr.setText("Batch");
                    jLabelRefNumClr.setText(r.getString(2));
                } else {
                    jLabelSerialClr.setText("RM");
                    jLabelRefNumClr.setText(r.getString(1));
                }
                jLabelRefDateClr.setText(r.getString(3));
                jTextVoucherNumClr.setText(r.getString(4));
                jLabelIssueEmpNumClr.setText(r.getString(5));
                jLabelIssueToNameClr.setText(r.getString(6));
                jLabelWorkstationProvinceClr.setText(r.getString(7));
                jLabelWorkstationDistrictClr.setText(r.getString(8));
                dateSent = r.getString(10);
                jLabelWithdrawEmpNum.setText(r.getString(11));
                jLabelIssueToNameWithdrawal.setText(r.getString(12));
                jLabelRefYearClr.setText(r.getString(13));

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdataDispatchView(String searchRef) {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT a.REF_YEAR,a.SERIAL,a.REF_DATE,VOUCHER_NUM,EMP_NAM,EMP_PROV,"
                    + "EMP_OFF,ACT_TOT_AMT,ACTIONED_ON_DATE,FILENAME,FILEFREETEXT"
                    + " FROM [ClaimsAppSysZvandiri].[dbo].[ClaimLetterMgtTab] a "
                    + "join [ClaimsAppSysZvandiri].[dbo].[ClaimLetterMgtWFActTab] b "
                    + "on a.ACT_REC_STA = b.ACT_REC_STA and a.REF_NUM = b.REF_NUM and "
                    + "a.DOC_VER = b.DOC_VER where concat(a.SERIAL,a.REF_NUM)='" + searchRef + "' "
                    + " and a.ACT_REC_STA ='D'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                jLabelRefYearView.setText(r.getString(1));
                jLabelSerialView.setText(r.getString(2));
                jLabelRefDateView.setText(r.getString(3));
                jLabelVoucherNumber.setText(r.getString(4));
                jLabelIssueToNameView.setText(r.getString(5));
                jLabelWorkstationProvinceView.setText(r.getString(6));
                jLabelWorkstationDistrictView.setText(r.getString(7));
                jLabelTotAmountView.setText(r.getString(8));
                jLabelDateDispatchView.setText(r.getString(9));
                jLabelAttDocNameView.setText(r.getString(10));
                jLabelAttActivityView.setText(r.getString(11));
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchdataCollectView(String searchRef) {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT a.REF_YEAR,a.SERIAL,a.REF_DATE,VOUCHER_NUM,EMP_NAM,EMP_PROV,"
                    + "EMP_OFF,ACT_TOT_AMT,ACTIONED_ON_DATE,FILENAME,FILEFREETEXT"
                    + " FROM [ClaimsAppSysZvandiri].[dbo].[ClaimLetterMgtTab] a "
                    + "join [ClaimsAppSysZvandiri].[dbo].[ClaimLetterMgtWFActTab] b "
                    + "on a.ACT_REC_STA = b.ACT_REC_STA and a.REF_NUM = b.REF_NUM and "
                    + "a.DOC_VER = b.DOC_VER where concat(a.SERIAL,a.REF_NUM)='" + searchRef + "' "
                    + " and a.ACT_REC_STA ='C'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                jLabelRefYearViewCollect.setText(r.getString(1));
                jLabelSerialViewCollect.setText(r.getString(2));
                jLabelRefDateViewCollect.setText(r.getString(3));
                jLabelVoucherNumberViewCollect.setText(r.getString(4));
                jLabelIssueToNameViewCollect.setText(r.getString(5));
                jLabelWorkstationProvinceViewCollect.setText(r.getString(6));
                jLabelWorkstationDistrictViewCollect.setText(r.getString(7));
                jLabelTotAmountViewCollect.setText(r.getString(8));
                jLabelDateDispatchViewCollect.setText(r.getString(9));
                jLabelAttDocNameViewCollect.setText(r.getString(10));
                jLabelAttActivityViewCollect.setText(r.getString(11));
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void createLetterDoc() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            if ("D".equals(reqType)) {
                attL.insertPDF3(conn, jLabelRefYear.getText(), jLabelAttDocFilePath.getText(),
                        jLabelAttDocName.getText(), jTextAttActivity.getText(), jLabelSerial.getText(),
                        jTextRefNum.getText(), jLabelIssueEmpNum.getText(), jLabelIssueToName.getText(),
                        jLabelWorkstationProvince.getText(), jLabelWorkstationDistrict.getText(),
                        jLabelTotAmount.getText(), jLabelGenDate.getText(), reqType,
                        (jTextFieldVoucherNum.getText()).toUpperCase().replace(" ", ""),
                        jLabelRefDate.getText(), jLabelWithDrawByName.getText(), jLabelWithDrawByEmpNum.getText());
                jDialogNewDispatch.setVisible(false);
            } //            else if (("C".equals(reqType)) && (jRadioNormal.isSelected())) {
            //                attL.insertPDF3(conn, jLabelRefYearClr.getText(), jLabelAttDocFilePathClr.getText(),
            //                        jLabelAttDocNameClr.getText(), jTextAttActivityClr.getText(), jLabelSerialClr.getText(),
            //                        jTextRefNumClr.getText(), jLabelIssueEmpNumClr.getText(), jLabelIssueToNameClr.getText(),
            //                        jLabelWorkstationProvinceClr.getText(), jLabelWorkstationDistrictClr.getText(),
            //                        jLabelTotAmountClr.getText(), formatter.format(jDateCollected.getDate()),
            //                        reqType, jLabelVoucherNumClr.getText(), jLabelRefDateClr.getText(),
            //                        jLabelWithDrawByName.getText(), jLabelWithDrawByEmpNum.getText());
            //                jDialogLetterClear.setVisible(false);
            //            } 
            else if ("C".equals(reqType)) {
                attL.insertPDF4(conn, jLabelRefNumClr.getText(), jLabelRefYearClr.getText(), jLabelSerialClr.getText(),
                        jTextVoucherNumClr.getText(), jLabelRefDateClr.getText(), jLabelIssueEmpNumClr.getText(),
                        jLabelIssueToNameClr.getText(), jLabelWorkstationProvinceClr.getText(), jLabelWorkstationDistrictClr.getText(),
                        jTextTotAmountClr.getText(), formatter.format(jDateCollected.getDate()), formatter.format(jDateWithdrawn.getDate()),
                        jLabelWithdrawEmpNum.getText(), jLabelIssueToNameWithdrawal.getText(), jLabelAttDocFilePathClr.getText(),
                        jLabelAttDocNameClr.getText(), jTextAttActivityClr.getText(), reqType, jLabelRefDateClr.getText());
                jDialogLetterClear.setVisible(false);
            } else if ("SD".equals(reqType)) {
                attL.insertPDF3(conn, df.format(curYear), jLabelAttDocFilePathSpecial.getText(),
                        jLabelAttDocNameSpecial.getText(), jTextAttActivitySpecial.getText(), "RM",
                        (jTextBatNumSpecial.getText()).toUpperCase().replace(" ", ""), jLabelIssueEmpNumSpecial.getText(), jLabelIssueToNameSpecial.getText(),
                        jComboProvince.getSelectedItem().toString(), jComboProvince.getSelectedItem().toString(),
                        jTextTotAmountSpecial.getText(), formatter.format(todayDate),
                        reqType, (jTextFieldVoucherNumSpecial.getText()).toUpperCase().replace(" ", ""),
                        formatter.format(todayDate), jLabelWithDrawBySPName.getText(), jLabelWithDrawBySPEmpNum.getText());
                jDialogLetterClear.setVisible(false);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void openStockPeriod(String stockYear, String stockMonth) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            String sqlCreRec = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[StockTakePeriod] "
                    + " VALUES (?,?, ?,?, ?, ?, ?)";

            pst = conn.prepareStatement(sqlCreRec);

            Statement st = conn.createStatement();

            st.executeQuery(" SELECT distinct '" + stockYear + "','" + stockMonth + "', EMP_OFF,'Running','1','1','A' "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[StockReceivedTab] where LEN(emp_off) > 0");

            ResultSet rs = st.getResultSet();

            while (rs.next()) {

                pst.setString(1, rs.getString(1));
                pst.setString(2, rs.getString(2));
                pst.setString(3, rs.getString(3));
                pst.setString(4, rs.getString(4));
                pst.setString(5, rs.getString(5));
                pst.setString(6, rs.getString(6));
                pst.setString(7, rs.getString(7));

                pst.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    void createStockTakeOpenAction() {

        try {
//            ClaimAppendix1.JFrameMain claimMain = new ClaimAppendix1.JFrameMain();
//            claimMain.computerName();
//            claimMain.findUser();
            SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss a");

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiriDemoDev;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlcreate = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[StockTakeActTab] "
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            pst = conn.prepareStatement(sqlcreate);

            pst.setString(1, Integer.toString(jYearChooser.getYear()));
            pst.setString(2, jComboMonth.getSelectedItem().toString());
            pst.setString(3, "ALL Districts");
            pst.setString(4, "Stock Take - Open Period");
            pst.setString(5, "Running");
            pst.setString(6, jLabelGenLogNam.getText());
            pst.setString(7, jLabelEmpNam.getText());
            pst.setString(8, formatter.format(todayDate));
            pst.setString(9, s.format(todayDate));
            pst.setString(10, hostName);
            pst.setString(11, "");
            pst.setString(12, "1");
            pst.setString(13, "1");
            pst.setString(14, "A");
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void createAction() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String sqlcreate = "INSERT INTO [ClaimsAppSysZvandiri].[dbo].[ClaimLetterMgtWFActTab] "
                    + "(YEAR,SERIAL,REF_NUM,BAT_NUM,VOUCHER_NUM,USR_ACTION, DOC_STATUS,ACTIONED_BY, "
                    + "SEND_TO, ACTIONED_ON_DATE, ACTIONED_ON_TIME, ACTIONED_ON_COMPUTER,DOC_VER,"
                    + " ACTION_VER,ACT_REC_STA)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            if ("D".equals(reqType) || "SD".equals(reqType)) {
                actionCode = "Letter - Created";
                statusCode = "Dispatched";
            } else if ("C".equals(reqType)) {
                actionCode = "Letter - Cleared";
                statusCode = "Collected";
            }

            pst = conn.prepareStatement(sqlcreate);

            pst.setString(1, df.format(curYear));
//            if ("SD".equals(reqType)) {
            pst.setString(2, "RM");
//            } else if ("C".equals(reqType)) {
//                pst.setString(2, jLabelSerialClr.getText());
//            } else if ("D".equals(reqType)) {
//                pst.setString(2, jLabelSerial.getText());
//            }
            if ("D".equals(reqType)) {
                pst.setString(3, jTextRefNum.getText());
            } else if (("C".equals(reqType)) && ("RM".equals(jLabelSerialClr.getText()))) {
                pst.setString(3, jLabelRefNumClr.getText());
            } else if (("C".equals(reqType)) && ("Batch".equals(jLabelSerialClr.getText()))) {
                pst.setString(3, "0");
            } else if ("SD".equals(reqType)) {
                pst.setString(3, "0");
            }
            if ("D".equals(reqType)) {
                pst.setString(4, "");
            } else if (("C".equals(reqType)) & ("RM".equals(jLabelSerialClr.getText()))) {
                pst.setString(4, "");
            } else if (("C".equals(reqType)) && ("Batch".equals(jLabelSerialClr.getText()))) {
                pst.setString(4, jTextVoucherNumClr.getText());
            } else if ("SD".equals(reqType)) {
                pst.setString(4, jTextBatNumSpecial.getText());
            }
            if ("D".equals(reqType)) {
                pst.setString(5, jTextFieldVoucherNum.getText());
            } else if ("SD".equals(reqType)) {
                pst.setString(5, jTextFieldVoucherNumSpecial.getText());
            } else if ("C".equals(reqType)) {
                pst.setString(5, jTextVoucherNumClr.getText());
            }
            pst.setString(6, actionCode);
            pst.setString(7, statusCode);
            pst.setString(8, jLabelGenLogNam.getText());
            if ("D".equals(reqType)) {
                pst.setString(9, jLabelIssueToName.getText());
            } else if ("C".equals(reqType)) {
                pst.setString(9, sendFrom);
            } else if ("SD".equals(reqType)) {
                pst.setString(9, jLabelIssueToNameSpecial.getText());
            }
            pst.setString(10, jLabelGenDate.getText());
            pst.setString(11, jLabelGenTime.getText());
            pst.setString(12, hostName);
            pst.setString(13, "1");
            pst.setString(14, "1");
            pst.setString(15, reqType);

            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void clearFields() {
        jTextRefNum.setText("");
        jLabelIssueToName.setText("");
        jLabelWorkstationProvince.setText("");
        jLabelWorkstationDistrict.setText("");
        jLabelWithDrawByEmpNum.setText("");
        jLabelTotAmount.setText("");
        jLabelAttDocName.setText("");
        jLabelAttDocFilePath.setText("");
        jTextAttActivity.setText("");
        jLabelIssueEmpNum.setText("");
        jLabelRefDate.setText("");
        jLabelRefYear.setText("");
        jTextVoucherNumClr.setText("");
        jLabelIssueToNameClr.setText("");
        jLabelWorkstationProvinceClr.setText("");
        jLabelWorkstationDistrictClr.setText("");
        jTextTotAmountClr.setText("");
        jLabelAttDocNameClr.setText("");
        jLabelAttDocFilePathClr.setText("");
        jTextAttActivityClr.setText("");
        jLabelIssueEmpNumClr.setText("");
        jLabelRefDateClr.setText("");
        jLabelIssueEmpNumSpecial.setText("");
        jLabelIssueToNameSpecial.setText("");
        jTextTotAmountSpecial.setText("");
        jLabelAttDocFilePathSpecial.setText("");
        jLabelAttDocNameSpecial.setText("");
        jTextAttActivitySpecial.setText("");
        jTextFieldVoucherNumSpecial.setText("");
        jDateCollected.setDate(null);
        jLabelRefNumClr.setText("");
        jLabelWithDrawByName.setText("");
        jLabelWithDrawBySPName.setText("");
    }

    void findExist(String searchRef, String actSta) {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT count(*)   FROM [ClaimsAppSysZvandiri].[dbo].[ClaimLetterMgtTab]"
                    + " where VOUCHER_NUM='" + searchRef + "' and ACT_REC_STA='" + actSta + "'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                existCount = r.getInt(1);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findExistClr(String searchRef) {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT count(*)   FROM [ClaimsAppSysZvandiri].[dbo].[ClaimLetterMgtTab]"
                    + " where VOUCHER_NUM ='" + searchRef + "' and ACT_REC_STA in ('D','SD')");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                existCountClr = r.getInt(1);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void checkRunningSTockTake() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[StockTakePeriod] "
                    + "where STOCK_TAKE_STATUS = 'Running' and ACT_REC_STA = 'A'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                stockTakeCountExist = r.getInt(1);

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findExistSp(String searchRef) {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT count(*)   FROM [ClaimsAppSysDemoDev].[dbo].[ClaimLetterMgtTab] "
                    + "where VOUCHER_NUM='" + searchRef + "' and ACT_REC_STA in ('D','SD')");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                existCountBat = r.getInt(1);

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findExistVoucher(String searchRef) {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT count(*)   FROM [ClaimsAppSysZvandiri].[dbo].[ClaimLetterMgtTab] "
                    + "where VOUCHER_NUM='" + searchRef + "' and ACT_REC_STA in ('D','SD')");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                existCountVoucher = r.getInt(1);

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findExistClrSpCheck(String searchRef) {
        existCountBat = 0;
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT count(*)   FROM [ClaimsAppSysZvandiri].[dbo].[ClaimLetterMgtTab] where BAT_NUM='" + searchRef + "'"
                    + " and ACT_REC_STA='C'");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                existCountBat = r.getInt(1);

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void findSendFrom(String searchRef) {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            st.executeQuery("SELECT ACTIONED_BY FROM [ClaimsAppSysZvandiri].[dbo].[ClaimLetterMgtWFActTab]"
                    + " where upper(VOUCHER_NUM)=upper('" + searchRef + "')  and ACT_REC_STA in ('D','SD')");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                sendFrom = r.getString(1);

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void checkVoucherSPDuplicate() {
        findExistSp(jTextFieldVoucherNumSpecial.getText());
        try {
            if (existCountBat == 0) {
                jTextFieldVoucherNumSpecial.requestFocusInWindow();
                jTextFieldVoucherNumSpecial.setFocusable(true);
            } else if (existCountBat > 0) {
                JOptionPane.showMessageDialog(jDialogNewDispatch, "Cash withdrawal letter has already been Proceesed. Please check and try again.");
                jTextFieldVoucherNumSpecial.requestFocusInWindow();
                jTextFieldVoucherNumSpecial.setFocusable(true);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void checkVoucherDuplicate() {
        findExistVoucher(jTextFieldVoucherNum.getText());
        try {
            if (existCountVoucher == 0) {
                jTextFieldVoucherNum.requestFocusInWindow();
                jTextFieldVoucherNum.setFocusable(true);
            } else if (existCountVoucher > 0) {
                JOptionPane.showMessageDialog(jDialogNewDispatch, "Voucher number was used for cash withdrawal. Please check and try again.");
                jTextRefNum.requestFocusInWindow();
                jTextRefNum.setFocusable(true);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void checkRMDuplicateInvalid() {
        jLabelRefDate.setText("");
        jLabelIssueEmpNum.setText("");
        jLabelIssueToName.setText("");
        jLabelWorkstationProvince.setText("");
        jLabelWorkstationDistrict.setText("");
        jLabelTotAmount.setText("");
        jLabelAttDocFilePath.setText("");
        jLabelAttDocName.setText("");
        jTextAttActivity.setText("");
        jTextFieldVoucherNum.setText("");
        findExist(jLabelSerial.getText() + jTextRefNum.getText(), reqType);
        if (existCount == 0) {
            fetchdata(jLabelSerial.getText() + jTextRefNum.getText());
        } else if (existCount > 0) {
            JOptionPane.showMessageDialog(jDialogNewDispatch, "Cash withdrawal letter has already been Proceesed. Please check and try again.");
            jTextRefNum.requestFocusInWindow();
            jTextRefNum.setFocusable(true);
        } else {
            JOptionPane.showMessageDialog(jDialogNewDispatch, "Invalid reference number for cash withdrawal letter dispatch. Please check and try again.");
            jTextRefNum.requestFocusInWindow();
            jTextRefNum.setFocusable(true);
        }
    }

    void retriveViewCollect() {
        if (!(jTextRefNumViewCollect.getText().trim().length() == 0)) {
            findExist(jLabelSerialViewCollect.getText() + jTextRefNumViewCollect.getText(), reqType);
            if (existCount > 0) {
                fetchdataCollectView(jLabelSerialViewCollect.getText() + jTextRefNumViewCollect.getText());
                jButtonDownloadViewCollect.setVisible(true);
            } else if (existCount == 0) {
                JOptionPane.showMessageDialog(jDialogViewCollect, "Invalid reference number for cash withdrawal letter dispatch. Please check and try again.");
                jTextRefNumViewCollect.requestFocusInWindow();
                jTextRefNumViewCollect.setFocusable(true);
            }
        } else {
            JOptionPane.showMessageDialog(jDialogViewCollect, "Reference number cannot be blank. Please check and try again.");
            jTextRefNumViewCollect.requestFocusInWindow();
            jTextRefNumViewCollect.setFocusable(true);
        }
    }

    void retrieveViewDispatch() {
        if (!(jTextRefNumView.getText().trim().length() == 0)) {
            findExist(jLabelSerialView.getText() + jTextRefNumView.getText(), reqType);
            if (existCount > 0) {
                fetchdataDispatchView(jLabelSerialView.getText() + jTextRefNumView.getText());
                jButtonDownloadView.setVisible(true);
            } else if (existCount == 0) {
                JOptionPane.showMessageDialog(jDialogNewDispatch, "Invalid reference number for cash withdrawal letter dispatch. Please check and try again.");
                jTextRefNumView.requestFocusInWindow();
                jTextRefNumView.setFocusable(true);
            }
        } else {
            JOptionPane.showMessageDialog(jDialogNewDispatch, "Reference number cannot be blank. Please check and try again.");
            jTextRefNumView.requestFocusInWindow();
            jTextRefNumView.setFocusable(true);
        }
    }

    void saveSubmit() {

        if ("D".equals(reqType)) {
            if (((jLabelAttDocFilePath.getText().trim().length()) > 0) && ((jTextRefNum.getText().trim().length()) > 0)
                    && ((jLabelIssueToName.getText().trim().length()) > 0) && ((jTextAttActivity.getText().trim().length()) > 0)
                    && ((jTextFieldVoucherNum.getText().trim().length()) > 0) && ((jLabelTotAmount.getText().trim().length()) > 0)
                    && ((jLabelWithDrawByName.getText().trim().length()) > 0)) {
                findExist(jLabelSerial.getText() + jTextRefNum.getText(), reqType);
                findExistVoucher(jTextFieldVoucherNum.getText());

                if (existCount > 0) {
                    JOptionPane.showMessageDialog(jDialogNewDispatch, "Cash withdrawal letter has already been Proceesed for the RM. Please check and try again.");
                    jTextRefNum.requestFocusInWindow();
                    jTextRefNum.setFocusable(true);
                } else if (existCountVoucher > 0) {
                    JOptionPane.showMessageDialog(jDialogNewDispatch, "Voucher number was used in previous cash withdrawal letter. Please check and try again.");
                    jTextRefNum.requestFocusInWindow();
                    jTextRefNum.setFocusable(true);
                } else {
                    jDialogWaitingEmail.setVisible(true);
                    try {

                        recSend.getSendToWithDrawName(jLabelWithDrawByName.getText());
                        jLabelWithDrawByEmpNum.setText(recSend.sendToWithDrawEmpNum);

                        createLetterDoc();
                        createAction();
                        recSend.getProvFinName(jLabelWorkstationProvince.getText());
                        recSend.getProvMgrName(jLabelWorkstationProvince.getText());
                        recSend.getSendToName(jLabelIssueEmpNum.getText());
                        recSend.getUsrLoggedName(jLabelEmp.getText());
                        recSend.sendMailUsrCreate(recSend.sendToEmpEmail, recSend.ProvFinEmail, recSend.loggedEmpEmail, jLabelSerial.getText(),
                                jTextRefNum.getText(), recSend.sendToEmpNam, jLabelTotAmount.getText(), jLabelAttDocFilePath.getText(),
                                jLabelAttDocName.getText(), jTextAttActivity.getText(), reqType, recSend.sendToWithDrawEmpNam, recSend.sendToWithDrawEmpEmail);
                        jDialogWaitingEmail.setVisible(false);
                        JOptionPane.showMessageDialog(this, "Email has been sent to " + recSend.sendToEmpNam + " cc " + recSend.ProvFinNam
                                + " with payment letter amounting to $ " + jLabelTotAmount.getText() + " from Ref. No."
                                + jLabelRegYear.getText() + " " + jLabelSerial.getText() + " " + jTextRefNum.getText());

                        clearFields();
                        jTextRefNum.setText("");
                        jDialogNewDispatch.setVisible(false);

                    } catch (Exception e) {
                        System.out.println(e);

                    }
                }
            } else {
                JOptionPane.showMessageDialog(jDialogNewDispatch, "Please complete all fields");
                jTextAttActivity.requestFocusInWindow();
                jTextAttActivity.setFocusable(true);
            }
        }

        if ("C".equals(reqType)) {
            if ((jLabelAttDocFilePathClr.getText().trim().length() == 0)
                    || ((jTextVoucherNumClr.getText().trim().length()) == 0) || (jLabelIssueToNameClr.getText().trim().length() == 0)
                    || (jDateCollected.getDate() == null) || (jDateWithdrawn.getDate() == null) || (jTextTotAmountClr.getText().trim().length() == 0)) {
                JOptionPane.showMessageDialog(jDialogLetterClear, "<html> Please complete all fields <br><b>NB: Press enter after capturing the Voucher Number</b></html>");
                jTextVoucherNumClr.requestFocusInWindow();
                jTextVoucherNumClr.setFocusable(true);
            } else if (formatter.format(jDateCollected.getDate()).compareTo(formatter.format(todayDate)) > 0) {
                JOptionPane.showMessageDialog(jDialogLetterClear, "Collection date cannot be greater than today's date. Please check your dates");
                jTextVoucherNumClr.requestFocusInWindow();
                jTextVoucherNumClr.setFocusable(true);
            } else if (formatter.format(jDateCollected.getDate()).compareTo(dateSent) < 0) {
                JOptionPane.showMessageDialog(jDialogLetterClear, "Collection date cannot be lower than dispatch date. Please check your dates");
                jTextVoucherNumClr.requestFocusInWindow();
                jTextVoucherNumClr.setFocusable(true);
            } else if (formatter.format(jDateWithdrawn.getDate()).compareTo(formatter.format(todayDate)) > 0) {
                JOptionPane.showMessageDialog(jDialogLetterClear, "Withrawal date cannot be greater than today's date. Please check your dates");
                jTextVoucherNumClr.requestFocusInWindow();
                jTextVoucherNumClr.setFocusable(true);
            } else if (formatter.format(jDateWithdrawn.getDate()).compareTo(dateSent) < 0) {
                JOptionPane.showMessageDialog(jDialogLetterClear, "Withrawal date cannot be lower than dispatch date. Please check your dates");
                jTextVoucherNumClr.requestFocusInWindow();
                jTextVoucherNumClr.setFocusable(true);
            } else {
                jDialogWaitingEmail.setVisible(true);
                try {
                    createLetterDoc();
                    createAction();
                    recSend.getProvFinName(jLabelWorkstationProvinceClr.getText());
                    recSend.getProvMgrName(jLabelWorkstationProvinceClr.getText());
                    recSend.getOrgSendToName(sendFrom);
                    recSend.getUsrLoggedName(jLabelEmp.getText());
//                        if (jRadioNormal.isSelected()) {
//                            radStatus = "Y";
//                        } else if (jRadioSpecial.isSelected()) {
//                            radStatus = "N";
//                        }

                    recSend.sendMailUsrCollect(recSend.sendToEmpEmail, recSend.loggedEmpEmail, jLabelSerial.getText(),
                            jTextVoucherNumClr.getText(), recSend.sendToEmpNam, jTextTotAmountClr.getText(), jLabelAttDocFilePathClr.getText(),
                            jLabelAttDocNameClr.getText(), jTextAttActivityClr.getText(), radStatus);
                    jDialogWaitingEmail.setVisible(false);

                    JOptionPane.showMessageDialog(this, "Email has been sent to " + recSend.sendToEmpNam
                            + " cc " + recSend.ProvFinNam
                            + " with cash collection confrimation of $ " + jTextTotAmountClr.getText() + " for Voucher. No." + jTextVoucherNumClr.getText());

                    clearFields();

                    jTextVoucherNumClr.setText("");
                    jDialogLetterClear.setVisible(false);

                } catch (Exception e) {
                    System.out.println(e);
                }

            }
        }

        if ("SD".equals(reqType)) {
            if ((jLabelAttDocFilePathSpecial.getText().trim().length()) > 0 && (jTextBatNumSpecial.getText().trim().length()) > 0
                    && ((jLabelIssueToNameSpecial.getText().trim().length()) > 0) && (jLabelIssueToNameSpecial.getText().trim().length()) > 0
                    && (jTextFieldVoucherNumSpecial.getText().trim().length()) > 0 && ((jComboProvince.getSelectedItem().toString().trim().length()) > 0)
                    && ((jLabelWithDrawBySPName.getText().trim().length()) > 0)) {
                findExistSp(jTextFieldVoucherNumSpecial.getText());
                if (existCountBat > 0) {
                    JOptionPane.showMessageDialog(jDialogNewDispatch, "Voucher number was used in previous cash withdrawal. Please check and try again.");
                    jTextFieldVoucherNumSpecial.requestFocusInWindow();
                    jTextFieldVoucherNumSpecial.setFocusable(true);
                } else {
                    jDialogWaitingEmail.setVisible(true);
                    try {
                        //  recSend.getSendToWithDrawName(jLabelWithDrawBySPName.getText());
                        recSend.getSendToNameSD(jLabelIssueToNameSpecial.getText());
                        jLabelIssueEmpNumSpecial.setText(recSend.sendToEmpNum);
                        createLetterDoc();
                        createAction();
                        recSend.getProvFinName(jComboProvince.getSelectedItem().toString());
                        recSend.getProvMgrName(jComboProvince.getSelectedItem().toString());
                        recSend.getUsrLoggedName(jLabelEmp.getText());
                        recSend.sendMailUsrCreate(recSend.sendToEmpEmail, recSend.ProvFinEmail, recSend.loggedEmpEmail,
                                jLabelSerialSpecial.getText(), jTextBatNumSpecial.getText(), recSend.sendToEmpNam,
                                jTextTotAmountSpecial.getText(), jLabelAttDocFilePathSpecial.getText(),
                                jLabelAttDocNameSpecial.getText(), jTextAttActivitySpecial.getText(), reqType,
                                sendToWithDrawEmpNam, sendToWithDrawEmpEmail);
                        jDialogWaitingEmail.setVisible(false);

                        JOptionPane.showMessageDialog(this, "Email has been sent to " + recSend.sendToEmpNam + " cc " + recSend.ProvFinNam
                                + " with special payment letter amounting to $ " + jTextTotAmountSpecial.getText() + " from "
                                + jLabelSerialSpecial.getText() + " " + jTextBatNumSpecial.getText());

                        clearFields();
                        jTextBatNumSpecial.setText("");
                        jDialogNewDispatchSpecial.setVisible(false);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(jDialogNewDispatchSpecial, "Special.Please complete all fields");
                jTextAttActivitySpecial.requestFocusInWindow();
                jTextAttActivitySpecial.setFocusable(true);
            }
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

        jDialogNewDispatch = new javax.swing.JDialog();
        jPanelDispatch = new javax.swing.JPanel();
        jLabelHeader = new javax.swing.JLabel();
        jLabelRefDate = new javax.swing.JLabel();
        jLabelSerial = new javax.swing.JLabel();
        jTextRefNum = new javax.swing.JTextField();
        jLabelTotAmount = new javax.swing.JLabel();
        jLabelRefNo = new javax.swing.JLabel();
        jLabelIssueTo = new javax.swing.JLabel();
        jLabelIssueToName = new javax.swing.JLabel();
        jLabelWorkStation = new javax.swing.JLabel();
        jLabelWorkstationProvince = new javax.swing.JLabel();
        jLabelWorkstationDistrict = new javax.swing.JLabel();
        jLabelAmount = new javax.swing.JLabel();
        jButtonSelectFile = new javax.swing.JButton();
        jTextAttActivity = new javax.swing.JTextField();
        jButtonSelectFile1 = new javax.swing.JButton();
        jButtonSelectFile2 = new javax.swing.JButton();
        jLabelIssueEmpNum = new javax.swing.JLabel();
        jLabelAttDocFilePath = new javax.swing.JLabel();
        jLabelAttDocName = new javax.swing.JLabel();
        jLabelRegYear = new javax.swing.JLabel();
        jLabelRefYear = new javax.swing.JLabel();
        jLabelVoucherNo = new javax.swing.JLabel();
        jTextFieldVoucherNum = new javax.swing.JTextField();
        jLabelWithdrawBy = new javax.swing.JLabel();
        jLabelWithDrawByName = new javax.swing.JLabel();
        jLabelSearch1 = new javax.swing.JLabel();
        jLabelWithDrawByEmpNum = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jDialogWaitingEmail = new javax.swing.JDialog();
        jDialogLetterClear = new javax.swing.JDialog();
        jPanelDispatch1 = new javax.swing.JPanel();
        jLabelHeaderClr = new javax.swing.JLabel();
        jLabelRefDateClr = new javax.swing.JLabel();
        jLabelSerialClr = new javax.swing.JLabel();
        jTextVoucherNumClr = new javax.swing.JTextField();
        jLabelIssueToClr = new javax.swing.JLabel();
        jLabelIssueToNameClr = new javax.swing.JLabel();
        jLabelWorkStationClr = new javax.swing.JLabel();
        jLabelWorkstationProvinceClr = new javax.swing.JLabel();
        jLabelWorkstationDistrictClr = new javax.swing.JLabel();
        jLabelAmountClr = new javax.swing.JLabel();
        jButtonSelectFileClr = new javax.swing.JButton();
        jTextAttActivityClr = new javax.swing.JTextField();
        jButtonSelectFile4 = new javax.swing.JButton();
        jButtonSelectFile5 = new javax.swing.JButton();
        jLabelIssueEmpNumClr = new javax.swing.JLabel();
        jLabelAttDocFilePathClr = new javax.swing.JLabel();
        jLabelAttDocNameClr = new javax.swing.JLabel();
        jLabelRegYear1 = new javax.swing.JLabel();
        jLabelDateCollected = new javax.swing.JLabel();
        jDateCollected = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jLabelComments = new javax.swing.JLabel();
        jLabelRefNumClr = new javax.swing.JLabel();
        jLabelVoucherNumClr1 = new javax.swing.JLabel();
        jLabelDateWithdrawn = new javax.swing.JLabel();
        jDateWithdrawn = new com.toedter.calendar.JDateChooser();
        jLabelWithdrawalby = new javax.swing.JLabel();
        jLabelIssueToNameWithdrawal = new javax.swing.JLabel();
        jLabelWithdrawEmpNum = new javax.swing.JLabel();
        jLabelRefYearClr = new javax.swing.JLabel();
        jTextTotAmountClr = new javax.swing.JTextField();
        jDialogViewDispatch = new javax.swing.JDialog();
        jPanelDispatch2 = new javax.swing.JPanel();
        jLabelHeadView = new javax.swing.JLabel();
        jLabelRefDateView = new javax.swing.JLabel();
        jLabelSerialView = new javax.swing.JLabel();
        jTextRefNumView = new javax.swing.JTextField();
        jLabelTotAmountView = new javax.swing.JLabel();
        jLabelRefNoView = new javax.swing.JLabel();
        jLabelIssueToView = new javax.swing.JLabel();
        jLabelIssueToNameView = new javax.swing.JLabel();
        jLabelWorkStationView = new javax.swing.JLabel();
        jLabelWorkstationProvinceView = new javax.swing.JLabel();
        jLabelWorkstationDistrictView = new javax.swing.JLabel();
        jLabelAmountView = new javax.swing.JLabel();
        jButtonViewCancel = new javax.swing.JButton();
        jButtonSelectView = new javax.swing.JButton();
        jLabelAttDocNameView = new javax.swing.JLabel();
        jLabelRefYearView = new javax.swing.JLabel();
        jLabelAttActivityView = new javax.swing.JLabel();
        jLabelDispatchView = new javax.swing.JLabel();
        jLabelDateDispatchView = new javax.swing.JLabel();
        jButtonDownloadView = new javax.swing.JButton();
        jLabelVoucherNumber = new javax.swing.JLabel();
        jLabelVoucher = new javax.swing.JLabel();
        jDialogViewCollect = new javax.swing.JDialog();
        jPanelDispatch3 = new javax.swing.JPanel();
        jLabelHeadViewCollect = new javax.swing.JLabel();
        jLabelRefDateViewCollect = new javax.swing.JLabel();
        jLabelSerialViewCollect = new javax.swing.JLabel();
        jTextRefNumViewCollect = new javax.swing.JTextField();
        jLabelTotAmountViewCollect = new javax.swing.JLabel();
        jLabelRefNoViewCollect = new javax.swing.JLabel();
        jLabelIssueToViewCollect = new javax.swing.JLabel();
        jLabelIssueToNameViewCollect = new javax.swing.JLabel();
        jLabelWorkStationViewCollect = new javax.swing.JLabel();
        jLabelWorkstationProvinceViewCollect = new javax.swing.JLabel();
        jLabelWorkstationDistrictViewCollect = new javax.swing.JLabel();
        jLabelAmountViewCollect = new javax.swing.JLabel();
        jButtonViewCollectCancel = new javax.swing.JButton();
        jButtonSelectViewCollect = new javax.swing.JButton();
        jLabelAttDocNameViewCollect = new javax.swing.JLabel();
        jLabelRefYearViewCollect = new javax.swing.JLabel();
        jLabelAttActivityViewCollect = new javax.swing.JLabel();
        jLabelDispatchViewCollect = new javax.swing.JLabel();
        jLabelDateDispatchViewCollect = new javax.swing.JLabel();
        jButtonDownloadViewCollect = new javax.swing.JButton();
        jLabelVoucherNumberViewCollect = new javax.swing.JLabel();
        jLabelVoucherViewCollect = new javax.swing.JLabel();
        jDialogNewDispatchSpecial = new javax.swing.JDialog();
        jPanelDispatch4 = new javax.swing.JPanel();
        jLabelHeaderSpecial = new javax.swing.JLabel();
        jLabelSerialSpecial = new javax.swing.JLabel();
        jTextBatNumSpecial = new javax.swing.JTextField();
        jLabelRefNoSpecial = new javax.swing.JLabel();
        jLabelIssueToSpecial = new javax.swing.JLabel();
        jLabelIssueToNameSpecial = new javax.swing.JLabel();
        jLabelWorkStationSpecial = new javax.swing.JLabel();
        jLabelAmountSpecial = new javax.swing.JLabel();
        jButtonSelectFileSpecial = new javax.swing.JButton();
        jTextAttActivitySpecial = new javax.swing.JTextField();
        jButtonCancelSpecial = new javax.swing.JButton();
        jButtonSubmitSpecial = new javax.swing.JButton();
        jLabelIssueEmpNumSpecial = new javax.swing.JLabel();
        jLabelAttDocFilePathSpecial = new javax.swing.JLabel();
        jLabelAttDocNameSpecial = new javax.swing.JLabel();
        jLabelVoucherNoSpecial = new javax.swing.JLabel();
        jTextFieldVoucherNumSpecial = new javax.swing.JTextField();
        jComboProvince = new javax.swing.JComboBox<>();
        jTextTotAmountSpecial = new javax.swing.JTextField();
        jLabelSearch = new javax.swing.JLabel();
        jLabelWithdrawSPBy = new javax.swing.JLabel();
        jLabelSearchSP = new javax.swing.JLabel();
        jLabelWithDrawBySPName = new javax.swing.JLabel();
        jLabelWithDrawBySPEmpNum = new javax.swing.JLabel();
        buttonGroupReference = new javax.swing.ButtonGroup();
        jDialogSearchName = new javax.swing.JDialog();
        jPanelSearchID = new javax.swing.JPanel();
        jTextFieldSearchNam = new javax.swing.JTextField();
        jButtonSearch = new javax.swing.JButton();
        jComboBoxSearchResult = new javax.swing.JComboBox<>();
        jButtonOk = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jDialogSearchWithDrawName = new javax.swing.JDialog();
        jPanelSearchWithdrawID = new javax.swing.JPanel();
        jTextFieldSearchWithdrawNam = new javax.swing.JTextField();
        jButtonWithdrawSearch = new javax.swing.JButton();
        jComboBoxWithdrawSearchResult = new javax.swing.JComboBox<>();
        jButtonWithdrawOk = new javax.swing.JButton();
        jLabelWithdrawHeader = new javax.swing.JLabel();
        jDialogPleaseWait = new javax.swing.JDialog();
        jDialogStockPeriodOpen = new javax.swing.JDialog();
        jPanelRejMain4 = new javax.swing.JPanel();
        jLabelHeader3 = new javax.swing.JLabel();
        jButtonBatchCancel = new javax.swing.JButton();
        jButtonBatchSave = new javax.swing.JButton();
        jLabelEmpNum1 = new javax.swing.JLabel();
        jSeparator27 = new javax.swing.JSeparator();
        jLabelBatchYear = new javax.swing.JLabel();
        jLabelBatchClose = new javax.swing.JLabel();
        jYearChooser = new com.toedter.calendar.JYearChooser();
        jLabelEmpNam = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator33 = new javax.swing.JSeparator();
        jComboMonth = new javax.swing.JComboBox<>();
        jDialogFuelView = new javax.swing.JDialog();
        jLabelheading = new javax.swing.JLabel();
        jLabelDateTo = new javax.swing.JLabel();
        jTextVehicleNo = new javax.swing.JTextField();
        jLabelVehicleNo = new javax.swing.JLabel();
        jLabelDateFrom = new javax.swing.JLabel();
        jButtonRetrieve = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableFuelDetails = new javax.swing.JTable();
        jDateFrom = new com.toedter.calendar.JDateChooser();
        jDateTo = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        jLabelDisPic = new javax.swing.JLabel();
        jLabelDisVision = new javax.swing.JLabel();
        jLabelMissionStat1 = new javax.swing.JLabel();
        jLabelVision = new javax.swing.JLabel();
        jLabelMission = new javax.swing.JLabel();
        jLabellogo = new javax.swing.JLabel();
        jLabelHeaderGen = new javax.swing.JLabel();
        jLabelEmp = new javax.swing.JLabel();
        jLabelGenDate = new javax.swing.JLabel();
        jLabelGenTime = new javax.swing.JLabel();
        jLabelGenLogNam1 = new javax.swing.JLabel();
        jLabelGenLogNam = new javax.swing.JLabel();
        jLabelPEPFARpic = new javax.swing.JLabel();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuNew = new javax.swing.JMenu();
        jMenuItemPlanPerDiem = new javax.swing.JMenuItem();
        jSeparator13 = new javax.swing.JPopupMenu.Separator();
        jMenuItemPerDiemAcquittal = new javax.swing.JMenuItem();
        jSeparator16 = new javax.swing.JPopupMenu.Separator();
        jMenuEdit = new javax.swing.JMenu();
        jMenuMonPlanEdit = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
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
        jSeparator24 = new javax.swing.JPopupMenu.Separator();
        jMenuItemUserCreate = new javax.swing.JMenuItem();
        jSeparator22 = new javax.swing.JPopupMenu.Separator();
        jMenuItemUserProfUpd = new javax.swing.JMenuItem();

        jDialogNewDispatch.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jDialogNewDispatch.setTitle("Payment Disptch");
        jDialogNewDispatch.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jDialogNewDispatch.setLocation(new java.awt.Point(400, 250));
        jDialogNewDispatch.setMinimumSize(new java.awt.Dimension(890, 307));
        jDialogNewDispatch.setResizable(false);

        jPanelDispatch.setAlignmentX(5.0F);
        jPanelDispatch.setAlignmentY(5.0F);
        jPanelDispatch.setMinimumSize(new java.awt.Dimension(890, 307));
        jPanelDispatch.setPreferredSize(new java.awt.Dimension(890, 307));
        jPanelDispatch.setLayout(null);

        jLabelHeader.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelHeader.setForeground(new java.awt.Color(255, 0, 0));
        jLabelHeader.setText("PAYMENT LETTERS DISPATCH");
        jPanelDispatch.add(jLabelHeader);
        jLabelHeader.setBounds(250, 10, 350, 30);
        jPanelDispatch.add(jLabelRefDate);
        jLabelRefDate.setBounds(320, 50, 100, 25);

        jLabelSerial.setText("RM");
        jPanelDispatch.add(jLabelSerial);
        jLabelSerial.setBounds(130, 50, 41, 25);

        jTextRefNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextRefNumActionPerformed(evt);
            }
        });
        jPanelDispatch.add(jTextRefNum);
        jTextRefNum.setBounds(220, 50, 70, 25);

        jLabelTotAmount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch.add(jLabelTotAmount);
        jLabelTotAmount.setBounds(150, 110, 100, 25);

        jLabelRefNo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelRefNo.setText("Reference No.   ");
        jPanelDispatch.add(jLabelRefNo);
        jLabelRefNo.setBounds(20, 50, 100, 25);

        jLabelIssueTo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelIssueTo.setText("Issued To   ");
        jPanelDispatch.add(jLabelIssueTo);
        jLabelIssueTo.setBounds(20, 80, 100, 25);

        jLabelIssueToName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch.add(jLabelIssueToName);
        jLabelIssueToName.setBounds(150, 80, 200, 25);

        jLabelWorkStation.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelWorkStation.setText("Work Station");
        jPanelDispatch.add(jLabelWorkStation);
        jLabelWorkStation.setBounds(360, 80, 100, 25);

        jLabelWorkstationProvince.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch.add(jLabelWorkstationProvince);
        jLabelWorkstationProvince.setBounds(450, 80, 180, 25);

        jLabelWorkstationDistrict.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch.add(jLabelWorkstationDistrict);
        jLabelWorkstationDistrict.setBounds(670, 80, 200, 25);

        jLabelAmount.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAmount.setText("Total Amount :");
        jPanelDispatch.add(jLabelAmount);
        jLabelAmount.setBounds(20, 110, 130, 25);

        jButtonSelectFile.setText("Select File");
        jButtonSelectFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectFileActionPerformed(evt);
            }
        });
        jPanelDispatch.add(jButtonSelectFile);
        jButtonSelectFile.setBounds(20, 140, 95, 30);

        jTextAttActivity.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextAttActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttActivityActionPerformed(evt);
            }
        });
        jPanelDispatch.add(jTextAttActivity);
        jTextAttActivity.setBounds(150, 170, 650, 25);

        jButtonSelectFile1.setText("Cancel");
        jButtonSelectFile1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectFile1ActionPerformed(evt);
            }
        });
        jPanelDispatch.add(jButtonSelectFile1);
        jButtonSelectFile1.setBounds(440, 210, 95, 30);

        jButtonSelectFile2.setText("Submit");
        jButtonSelectFile2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectFile2ActionPerformed(evt);
            }
        });
        jPanelDispatch.add(jButtonSelectFile2);
        jButtonSelectFile2.setBounds(250, 210, 95, 30);
        jPanelDispatch.add(jLabelIssueEmpNum);
        jLabelIssueEmpNum.setBounds(800, 50, 80, 20);

        jLabelAttDocFilePath.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch.add(jLabelAttDocFilePath);
        jLabelAttDocFilePath.setBounds(410, 140, 430, 25);

        jLabelAttDocName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch.add(jLabelAttDocName);
        jLabelAttDocName.setBounds(150, 140, 230, 25);
        jPanelDispatch.add(jLabelRegYear);
        jLabelRegYear.setBounds(170, 50, 0, 25);
        jPanelDispatch.add(jLabelRefYear);
        jLabelRefYear.setBounds(160, 50, 50, 25);

        jLabelVoucherNo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelVoucherNo.setText("Voucher No.");
        jPanelDispatch.add(jLabelVoucherNo);
        jLabelVoucherNo.setBounds(490, 50, 70, 25);

        jTextFieldVoucherNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldVoucherNumActionPerformed(evt);
            }
        });
        jPanelDispatch.add(jTextFieldVoucherNum);
        jTextFieldVoucherNum.setBounds(580, 50, 90, 25);

        jLabelWithdrawBy.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelWithdrawBy.setText("Withdrawal  by");
        jPanelDispatch.add(jLabelWithdrawBy);
        jLabelWithdrawBy.setBounds(360, 110, 100, 25);

        jLabelWithDrawByName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch.add(jLabelWithDrawByName);
        jLabelWithDrawByName.setBounds(520, 110, 200, 25);

        jLabelSearch1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.jpg"))); // NOI18N
        jLabelSearch1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelSearch1MouseClicked(evt);
            }
        });
        jPanelDispatch.add(jLabelSearch1);
        jLabelSearch1.setBounds(460, 110, 25, 25);
        jPanelDispatch.add(jLabelWithDrawByEmpNum);
        jLabelWithDrawByEmpNum.setBounds(760, 114, 70, 25);

        javax.swing.GroupLayout jDialogNewDispatchLayout = new javax.swing.GroupLayout(jDialogNewDispatch.getContentPane());
        jDialogNewDispatch.getContentPane().setLayout(jDialogNewDispatchLayout);
        jDialogNewDispatchLayout.setHorizontalGroup(
            jDialogNewDispatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelDispatch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogNewDispatchLayout.setVerticalGroup(
            jDialogNewDispatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelDispatch, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTextField1.setText("jTextField1");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jDialogWaitingEmail.setTitle("                    Connecting to server and sending email.  Please Wait.");
        jDialogWaitingEmail.setAlwaysOnTop(true);
        jDialogWaitingEmail.setBackground(new java.awt.Color(51, 255, 51));
        jDialogWaitingEmail.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jDialogWaitingEmail.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jDialogWaitingEmail.setForeground(new java.awt.Color(255, 0, 0));
        jDialogWaitingEmail.setIconImages(null);
        jDialogWaitingEmail.setLocation(new java.awt.Point(500, 350));
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

        jDialogLetterClear.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jDialogLetterClear.setTitle("Payment Disptch");
        jDialogLetterClear.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jDialogLetterClear.setLocation(new java.awt.Point(400, 250));
        jDialogLetterClear.setMinimumSize(new java.awt.Dimension(890, 350));
        jDialogLetterClear.setResizable(false);

        jPanelDispatch1.setAlignmentX(5.0F);
        jPanelDispatch1.setAlignmentY(5.0F);
        jPanelDispatch1.setMinimumSize(new java.awt.Dimension(890, 307));
        jPanelDispatch1.setPreferredSize(new java.awt.Dimension(890, 350));
        jPanelDispatch1.setLayout(null);

        jLabelHeaderClr.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelHeaderClr.setForeground(new java.awt.Color(0, 102, 51));
        jLabelHeaderClr.setText("PAYMENT LETTERS CLEARANCE");
        jPanelDispatch1.add(jLabelHeaderClr);
        jLabelHeaderClr.setBounds(290, 10, 310, 30);

        jLabelRefDateClr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelDispatch1.add(jLabelRefDateClr);
        jLabelRefDateClr.setBounds(560, 50, 100, 25);

        jLabelSerialClr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelDispatch1.add(jLabelSerialClr);
        jLabelSerialClr.setBounds(360, 50, 60, 25);

        jTextVoucherNumClr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextVoucherNumClrMouseClicked(evt);
            }
        });
        jTextVoucherNumClr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextVoucherNumClrActionPerformed(evt);
            }
        });
        jTextVoucherNumClr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextVoucherNumClrKeyPressed(evt);
            }
        });
        jPanelDispatch1.add(jTextVoucherNumClr);
        jTextVoucherNumClr.setBounds(150, 50, 70, 25);

        jLabelIssueToClr.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelIssueToClr.setText("Issued To   ");
        jPanelDispatch1.add(jLabelIssueToClr);
        jLabelIssueToClr.setBounds(20, 80, 100, 25);

        jLabelIssueToNameClr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch1.add(jLabelIssueToNameClr);
        jLabelIssueToNameClr.setBounds(150, 80, 200, 25);

        jLabelWorkStationClr.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelWorkStationClr.setText("Work Station");
        jPanelDispatch1.add(jLabelWorkStationClr);
        jLabelWorkStationClr.setBounds(360, 80, 100, 25);

        jLabelWorkstationProvinceClr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch1.add(jLabelWorkstationProvinceClr);
        jLabelWorkstationProvinceClr.setBounds(450, 80, 180, 25);

        jLabelWorkstationDistrictClr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch1.add(jLabelWorkstationDistrictClr);
        jLabelWorkstationDistrictClr.setBounds(670, 80, 200, 25);

        jLabelAmountClr.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAmountClr.setText("<html>Total Amount<br>Collected</html> ");
        jPanelDispatch1.add(jLabelAmountClr);
        jLabelAmountClr.setBounds(20, 105, 130, 30);

        jButtonSelectFileClr.setText("Select File");
        jButtonSelectFileClr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectFileClrActionPerformed(evt);
            }
        });
        jPanelDispatch1.add(jButtonSelectFileClr);
        jButtonSelectFileClr.setBounds(20, 190, 95, 30);

        jTextAttActivityClr.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextAttActivityClr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttActivityClrActionPerformed(evt);
            }
        });
        jPanelDispatch1.add(jTextAttActivityClr);
        jTextAttActivityClr.setBounds(230, 220, 610, 25);

        jButtonSelectFile4.setText("Cancel");
        jButtonSelectFile4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectFile4ActionPerformed(evt);
            }
        });
        jPanelDispatch1.add(jButtonSelectFile4);
        jButtonSelectFile4.setBounds(440, 270, 95, 30);

        jButtonSelectFile5.setText("Submit");
        jButtonSelectFile5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectFile5ActionPerformed(evt);
            }
        });
        jPanelDispatch1.add(jButtonSelectFile5);
        jButtonSelectFile5.setBounds(250, 270, 95, 30);
        jPanelDispatch1.add(jLabelIssueEmpNumClr);
        jLabelIssueEmpNumClr.setBounds(800, 50, 80, 20);

        jLabelAttDocFilePathClr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch1.add(jLabelAttDocFilePathClr);
        jLabelAttDocFilePathClr.setBounds(410, 190, 430, 25);

        jLabelAttDocNameClr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch1.add(jLabelAttDocNameClr);
        jLabelAttDocNameClr.setBounds(150, 190, 230, 25);
        jPanelDispatch1.add(jLabelRegYear1);
        jLabelRegYear1.setBounds(170, 50, 0, 25);

        jLabelDateCollected.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelDateCollected.setText("Date Cash Collected");
        jPanelDispatch1.add(jLabelDateCollected);
        jLabelDateCollected.setBounds(610, 110, 110, 25);
        jPanelDispatch1.add(jDateCollected);
        jDateCollected.setBounds(740, 110, 130, 25);

        jLabel3.setFont(new java.awt.Font("Tahoma", 3, 10)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 51));
        jLabel3.setText("Please attach stamped letter and electronic print as one continous pdf document");
        jPanelDispatch1.add(jLabel3);
        jLabel3.setBounds(20, 170, 470, 25);

        jLabelComments.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelComments.setText("Comments");
        jPanelDispatch1.add(jLabelComments);
        jLabelComments.setBounds(150, 220, 70, 25);

        jLabelRefNumClr.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelRefNumClr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelDispatch1.add(jLabelRefNumClr);
        jLabelRefNumClr.setBounds(450, 50, 100, 25);

        jLabelVoucherNumClr1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelVoucherNumClr1.setText("Voucher Number");
        jPanelDispatch1.add(jLabelVoucherNumClr1);
        jLabelVoucherNumClr1.setBounds(20, 50, 100, 25);

        jLabelDateWithdrawn.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelDateWithdrawn.setForeground(new java.awt.Color(255, 51, 255));
        jLabelDateWithdrawn.setText("Date Cash Withdrawn");
        jPanelDispatch1.add(jLabelDateWithdrawn);
        jLabelDateWithdrawn.setBounds(300, 110, 120, 25);
        jPanelDispatch1.add(jDateWithdrawn);
        jDateWithdrawn.setBounds(430, 110, 130, 25);

        jLabelWithdrawalby.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelWithdrawalby.setText("Withdrawal Done by");
        jPanelDispatch1.add(jLabelWithdrawalby);
        jLabelWithdrawalby.setBounds(20, 140, 130, 25);

        jLabelIssueToNameWithdrawal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch1.add(jLabelIssueToNameWithdrawal);
        jLabelIssueToNameWithdrawal.setBounds(150, 140, 200, 25);

        jLabelWithdrawEmpNum.setText("jLabel1");
        jPanelDispatch1.add(jLabelWithdrawEmpNum);
        jLabelWithdrawEmpNum.setBounds(390, 140, 40, 30);

        jLabelRefYearClr.setText("jLabel1");
        jPanelDispatch1.add(jLabelRefYearClr);
        jLabelRefYearClr.setBounds(290, 50, 50, 20);
        jPanelDispatch1.add(jTextTotAmountClr);
        jTextTotAmountClr.setBounds(150, 110, 100, 25);

        javax.swing.GroupLayout jDialogLetterClearLayout = new javax.swing.GroupLayout(jDialogLetterClear.getContentPane());
        jDialogLetterClear.getContentPane().setLayout(jDialogLetterClearLayout);
        jDialogLetterClearLayout.setHorizontalGroup(
            jDialogLetterClearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelDispatch1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogLetterClearLayout.setVerticalGroup(
            jDialogLetterClearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelDispatch1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialogViewDispatch.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jDialogViewDispatch.setTitle("Payment Disptch");
        jDialogViewDispatch.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jDialogViewDispatch.setLocation(new java.awt.Point(400, 250));
        jDialogViewDispatch.setMinimumSize(new java.awt.Dimension(890, 307));
        jDialogViewDispatch.setResizable(false);

        jPanelDispatch2.setBackground(new java.awt.Color(250, 144, 210));
        jPanelDispatch2.setAlignmentX(5.0F);
        jPanelDispatch2.setAlignmentY(5.0F);
        jPanelDispatch2.setMinimumSize(new java.awt.Dimension(890, 307));
        jPanelDispatch2.setLayout(null);

        jLabelHeadView.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelHeadView.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeadView.setText("PAYMENT LETTERS DISPATCH - VIEW");
        jPanelDispatch2.add(jLabelHeadView);
        jLabelHeadView.setBounds(280, 10, 360, 30);
        jPanelDispatch2.add(jLabelRefDateView);
        jLabelRefDateView.setBounds(320, 50, 100, 25);

        jLabelSerialView.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSerialView.setText("RM");
        jPanelDispatch2.add(jLabelSerialView);
        jLabelSerialView.setBounds(130, 50, 41, 25);

        jTextRefNumView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextRefNumViewActionPerformed(evt);
            }
        });
        jPanelDispatch2.add(jTextRefNumView);
        jTextRefNumView.setBounds(220, 50, 70, 25);

        jLabelTotAmountView.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch2.add(jLabelTotAmountView);
        jLabelTotAmountView.setBounds(150, 110, 100, 25);

        jLabelRefNoView.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelRefNoView.setForeground(new java.awt.Color(255, 255, 255));
        jLabelRefNoView.setText("Reference No.   ");
        jPanelDispatch2.add(jLabelRefNoView);
        jLabelRefNoView.setBounds(20, 50, 100, 25);

        jLabelIssueToView.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelIssueToView.setForeground(new java.awt.Color(255, 255, 255));
        jLabelIssueToView.setText("Issued To   ");
        jPanelDispatch2.add(jLabelIssueToView);
        jLabelIssueToView.setBounds(20, 80, 100, 25);

        jLabelIssueToNameView.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch2.add(jLabelIssueToNameView);
        jLabelIssueToNameView.setBounds(150, 80, 200, 25);

        jLabelWorkStationView.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelWorkStationView.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWorkStationView.setText("Work Station");
        jPanelDispatch2.add(jLabelWorkStationView);
        jLabelWorkStationView.setBounds(360, 80, 100, 25);

        jLabelWorkstationProvinceView.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch2.add(jLabelWorkstationProvinceView);
        jLabelWorkstationProvinceView.setBounds(450, 80, 180, 25);

        jLabelWorkstationDistrictView.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch2.add(jLabelWorkstationDistrictView);
        jLabelWorkstationDistrictView.setBounds(670, 80, 200, 25);

        jLabelAmountView.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAmountView.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAmountView.setText("Total Amount :");
        jPanelDispatch2.add(jLabelAmountView);
        jLabelAmountView.setBounds(20, 110, 130, 25);

        jButtonViewCancel.setText("Close");
        jButtonViewCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewCancelActionPerformed(evt);
            }
        });
        jPanelDispatch2.add(jButtonViewCancel);
        jButtonViewCancel.setBounds(530, 210, 95, 30);

        jButtonSelectView.setText("View");
        jButtonSelectView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectViewActionPerformed(evt);
            }
        });
        jPanelDispatch2.add(jButtonSelectView);
        jButtonSelectView.setBounds(360, 210, 95, 30);

        jLabelAttDocNameView.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch2.add(jLabelAttDocNameView);
        jLabelAttDocNameView.setBounds(150, 140, 360, 25);
        jPanelDispatch2.add(jLabelRefYearView);
        jLabelRefYearView.setBounds(160, 50, 50, 25);

        jLabelAttActivityView.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelDispatch2.add(jLabelAttActivityView);
        jLabelAttActivityView.setBounds(150, 173, 690, 25);

        jLabelDispatchView.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelDispatchView.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDispatchView.setText("Date Dispatched");
        jPanelDispatch2.add(jLabelDispatchView);
        jLabelDispatchView.setBounds(360, 113, 90, 25);

        jLabelDateDispatchView.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelDispatch2.add(jLabelDateDispatchView);
        jLabelDateDispatchView.setBounds(450, 110, 100, 25);

        jButtonDownloadView.setText("Download");
        jButtonDownloadView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDownloadViewActionPerformed(evt);
            }
        });
        jPanelDispatch2.add(jButtonDownloadView);
        jButtonDownloadView.setBounds(550, 140, 95, 30);

        jLabelVoucherNumber.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelDispatch2.add(jLabelVoucherNumber);
        jLabelVoucherNumber.setBounds(650, 50, 90, 25);

        jLabelVoucher.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelVoucher.setText("Voucher Number");
        jPanelDispatch2.add(jLabelVoucher);
        jLabelVoucher.setBounds(550, 50, 90, 25);

        javax.swing.GroupLayout jDialogViewDispatchLayout = new javax.swing.GroupLayout(jDialogViewDispatch.getContentPane());
        jDialogViewDispatch.getContentPane().setLayout(jDialogViewDispatchLayout);
        jDialogViewDispatchLayout.setHorizontalGroup(
            jDialogViewDispatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelDispatch2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogViewDispatchLayout.setVerticalGroup(
            jDialogViewDispatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelDispatch2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialogViewCollect.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jDialogViewCollect.setTitle("Payment Disptch");
        jDialogViewCollect.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jDialogViewCollect.setLocation(new java.awt.Point(400, 250));
        jDialogViewCollect.setMinimumSize(new java.awt.Dimension(890, 307));
        jDialogViewCollect.setResizable(false);

        jPanelDispatch3.setBackground(new java.awt.Color(153, 153, 255));
        jPanelDispatch3.setAlignmentX(5.0F);
        jPanelDispatch3.setAlignmentY(5.0F);
        jPanelDispatch3.setMinimumSize(new java.awt.Dimension(890, 307));
        jPanelDispatch3.setLayout(null);

        jLabelHeadViewCollect.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelHeadViewCollect.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeadViewCollect.setText("PAYMENT COLLECTION - VIEW");
        jPanelDispatch3.add(jLabelHeadViewCollect);
        jLabelHeadViewCollect.setBounds(280, 10, 360, 30);
        jPanelDispatch3.add(jLabelRefDateViewCollect);
        jLabelRefDateViewCollect.setBounds(320, 50, 100, 25);

        jLabelSerialViewCollect.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSerialViewCollect.setText("RM");
        jPanelDispatch3.add(jLabelSerialViewCollect);
        jLabelSerialViewCollect.setBounds(130, 50, 41, 25);

        jTextRefNumViewCollect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextRefNumViewCollectActionPerformed(evt);
            }
        });
        jPanelDispatch3.add(jTextRefNumViewCollect);
        jTextRefNumViewCollect.setBounds(220, 50, 70, 25);

        jLabelTotAmountViewCollect.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch3.add(jLabelTotAmountViewCollect);
        jLabelTotAmountViewCollect.setBounds(150, 110, 100, 25);

        jLabelRefNoViewCollect.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelRefNoViewCollect.setForeground(new java.awt.Color(255, 255, 255));
        jLabelRefNoViewCollect.setText("Reference No.   ");
        jPanelDispatch3.add(jLabelRefNoViewCollect);
        jLabelRefNoViewCollect.setBounds(20, 50, 100, 25);

        jLabelIssueToViewCollect.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelIssueToViewCollect.setForeground(new java.awt.Color(255, 255, 255));
        jLabelIssueToViewCollect.setText("Issued To   ");
        jPanelDispatch3.add(jLabelIssueToViewCollect);
        jLabelIssueToViewCollect.setBounds(20, 80, 100, 25);

        jLabelIssueToNameViewCollect.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch3.add(jLabelIssueToNameViewCollect);
        jLabelIssueToNameViewCollect.setBounds(150, 80, 200, 25);

        jLabelWorkStationViewCollect.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelWorkStationViewCollect.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWorkStationViewCollect.setText("Work Station");
        jPanelDispatch3.add(jLabelWorkStationViewCollect);
        jLabelWorkStationViewCollect.setBounds(360, 80, 100, 25);

        jLabelWorkstationProvinceViewCollect.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch3.add(jLabelWorkstationProvinceViewCollect);
        jLabelWorkstationProvinceViewCollect.setBounds(450, 80, 180, 25);

        jLabelWorkstationDistrictViewCollect.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch3.add(jLabelWorkstationDistrictViewCollect);
        jLabelWorkstationDistrictViewCollect.setBounds(670, 80, 200, 25);

        jLabelAmountViewCollect.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAmountViewCollect.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAmountViewCollect.setText("Total Amount :");
        jPanelDispatch3.add(jLabelAmountViewCollect);
        jLabelAmountViewCollect.setBounds(20, 110, 130, 25);

        jButtonViewCollectCancel.setText("Close");
        jButtonViewCollectCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewCollectCancelActionPerformed(evt);
            }
        });
        jPanelDispatch3.add(jButtonViewCollectCancel);
        jButtonViewCollectCancel.setBounds(530, 210, 95, 30);

        jButtonSelectViewCollect.setText("View");
        jButtonSelectViewCollect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectViewCollectActionPerformed(evt);
            }
        });
        jPanelDispatch3.add(jButtonSelectViewCollect);
        jButtonSelectViewCollect.setBounds(360, 210, 95, 30);

        jLabelAttDocNameViewCollect.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch3.add(jLabelAttDocNameViewCollect);
        jLabelAttDocNameViewCollect.setBounds(150, 140, 230, 25);
        jPanelDispatch3.add(jLabelRefYearViewCollect);
        jLabelRefYearViewCollect.setBounds(160, 50, 50, 25);

        jLabelAttActivityViewCollect.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelDispatch3.add(jLabelAttActivityViewCollect);
        jLabelAttActivityViewCollect.setBounds(150, 173, 690, 25);

        jLabelDispatchViewCollect.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelDispatchViewCollect.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDispatchViewCollect.setText("Date Collected");
        jPanelDispatch3.add(jLabelDispatchViewCollect);
        jLabelDispatchViewCollect.setBounds(360, 113, 90, 25);

        jLabelDateDispatchViewCollect.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelDispatch3.add(jLabelDateDispatchViewCollect);
        jLabelDateDispatchViewCollect.setBounds(450, 110, 100, 25);

        jButtonDownloadViewCollect.setText("Download");
        jButtonDownloadViewCollect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDownloadViewCollectActionPerformed(evt);
            }
        });
        jPanelDispatch3.add(jButtonDownloadViewCollect);
        jButtonDownloadViewCollect.setBounds(450, 140, 95, 30);

        jLabelVoucherNumberViewCollect.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanelDispatch3.add(jLabelVoucherNumberViewCollect);
        jLabelVoucherNumberViewCollect.setBounds(650, 50, 90, 25);

        jLabelVoucherViewCollect.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelVoucherViewCollect.setText("Voucher Number");
        jPanelDispatch3.add(jLabelVoucherViewCollect);
        jLabelVoucherViewCollect.setBounds(550, 50, 90, 25);

        javax.swing.GroupLayout jDialogViewCollectLayout = new javax.swing.GroupLayout(jDialogViewCollect.getContentPane());
        jDialogViewCollect.getContentPane().setLayout(jDialogViewCollectLayout);
        jDialogViewCollectLayout.setHorizontalGroup(
            jDialogViewCollectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelDispatch3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogViewCollectLayout.setVerticalGroup(
            jDialogViewCollectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelDispatch3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialogNewDispatchSpecial.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jDialogNewDispatchSpecial.setTitle("Payment Disptch");
        jDialogNewDispatchSpecial.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jDialogNewDispatchSpecial.setLocation(new java.awt.Point(400, 250));
        jDialogNewDispatchSpecial.setMinimumSize(new java.awt.Dimension(890, 307));
        jDialogNewDispatchSpecial.setResizable(false);

        jPanelDispatch4.setAlignmentX(5.0F);
        jPanelDispatch4.setAlignmentY(5.0F);
        jPanelDispatch4.setMinimumSize(new java.awt.Dimension(890, 307));
        jPanelDispatch4.setLayout(null);

        jLabelHeaderSpecial.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelHeaderSpecial.setForeground(new java.awt.Color(255, 0, 0));
        jLabelHeaderSpecial.setText("SPECIAL PAYMENT LETTERS DISPATCH");
        jPanelDispatch4.add(jLabelHeaderSpecial);
        jLabelHeaderSpecial.setBounds(220, 10, 380, 30);

        jLabelSerialSpecial.setText("Batch");
        jPanelDispatch4.add(jLabelSerialSpecial);
        jLabelSerialSpecial.setBounds(130, 50, 70, 25);

        jTextBatNumSpecial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextBatNumSpecialActionPerformed(evt);
            }
        });
        jPanelDispatch4.add(jTextBatNumSpecial);
        jTextBatNumSpecial.setBounds(220, 50, 70, 25);

        jLabelRefNoSpecial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelRefNoSpecial.setText("Reference No.   ");
        jPanelDispatch4.add(jLabelRefNoSpecial);
        jLabelRefNoSpecial.setBounds(20, 50, 100, 25);

        jLabelIssueToSpecial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelIssueToSpecial.setText("Issued To   ");
        jPanelDispatch4.add(jLabelIssueToSpecial);
        jLabelIssueToSpecial.setBounds(390, 80, 70, 25);

        jLabelIssueToNameSpecial.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch4.add(jLabelIssueToNameSpecial);
        jLabelIssueToNameSpecial.setBounds(530, 80, 200, 25);

        jLabelWorkStationSpecial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelWorkStationSpecial.setText("Work Station");
        jPanelDispatch4.add(jLabelWorkStationSpecial);
        jLabelWorkStationSpecial.setBounds(20, 80, 100, 25);

        jLabelAmountSpecial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAmountSpecial.setText("Total Amount :");
        jPanelDispatch4.add(jLabelAmountSpecial);
        jLabelAmountSpecial.setBounds(20, 110, 130, 25);

        jButtonSelectFileSpecial.setText("Select File");
        jButtonSelectFileSpecial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectFileSpecialActionPerformed(evt);
            }
        });
        jPanelDispatch4.add(jButtonSelectFileSpecial);
        jButtonSelectFileSpecial.setBounds(20, 140, 95, 30);

        jTextAttActivitySpecial.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextAttActivitySpecial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAttActivitySpecialActionPerformed(evt);
            }
        });
        jPanelDispatch4.add(jTextAttActivitySpecial);
        jTextAttActivitySpecial.setBounds(150, 170, 650, 25);

        jButtonCancelSpecial.setText("Cancel");
        jButtonCancelSpecial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelSpecialActionPerformed(evt);
            }
        });
        jPanelDispatch4.add(jButtonCancelSpecial);
        jButtonCancelSpecial.setBounds(440, 210, 95, 30);

        jButtonSubmitSpecial.setText("Submit");
        jButtonSubmitSpecial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSubmitSpecialActionPerformed(evt);
            }
        });
        jPanelDispatch4.add(jButtonSubmitSpecial);
        jButtonSubmitSpecial.setBounds(250, 210, 95, 30);
        jPanelDispatch4.add(jLabelIssueEmpNumSpecial);
        jLabelIssueEmpNumSpecial.setBounds(800, 50, 80, 20);

        jLabelAttDocFilePathSpecial.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch4.add(jLabelAttDocFilePathSpecial);
        jLabelAttDocFilePathSpecial.setBounds(410, 140, 430, 25);

        jLabelAttDocNameSpecial.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch4.add(jLabelAttDocNameSpecial);
        jLabelAttDocNameSpecial.setBounds(150, 140, 230, 25);

        jLabelVoucherNoSpecial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelVoucherNoSpecial.setText("Voucher No.");
        jPanelDispatch4.add(jLabelVoucherNoSpecial);
        jLabelVoucherNoSpecial.setBounds(490, 50, 70, 25);

        jTextFieldVoucherNumSpecial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldVoucherNumSpecialActionPerformed(evt);
            }
        });
        jTextFieldVoucherNumSpecial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldVoucherNumSpecialKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldVoucherNumSpecialKeyReleased(evt);
            }
        });
        jPanelDispatch4.add(jTextFieldVoucherNumSpecial);
        jTextFieldVoucherNumSpecial.setBounds(580, 50, 90, 25);

        jComboProvince.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboProvinceActionPerformed(evt);
            }
        });
        jPanelDispatch4.add(jComboProvince);
        jComboProvince.setBounds(150, 80, 220, 25);

        jTextTotAmountSpecial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextTotAmountSpecialActionPerformed(evt);
            }
        });
        jTextTotAmountSpecial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextTotAmountSpecialKeyPressed(evt);
            }
        });
        jPanelDispatch4.add(jTextTotAmountSpecial);
        jTextTotAmountSpecial.setBounds(150, 110, 110, 25);

        jLabelSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.jpg"))); // NOI18N
        jLabelSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelSearchMouseClicked(evt);
            }
        });
        jPanelDispatch4.add(jLabelSearch);
        jLabelSearch.setBounds(480, 80, 25, 25);

        jLabelWithdrawSPBy.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelWithdrawSPBy.setText("Withdrawn by");
        jPanelDispatch4.add(jLabelWithdrawSPBy);
        jLabelWithdrawSPBy.setBounds(390, 110, 90, 25);

        jLabelSearchSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.jpg"))); // NOI18N
        jLabelSearchSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelSearchSPMouseClicked(evt);
            }
        });
        jPanelDispatch4.add(jLabelSearchSP);
        jLabelSearchSP.setBounds(480, 110, 25, 25);

        jLabelWithDrawBySPName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelDispatch4.add(jLabelWithDrawBySPName);
        jLabelWithDrawBySPName.setBounds(530, 110, 200, 25);
        jPanelDispatch4.add(jLabelWithDrawBySPEmpNum);
        jLabelWithDrawBySPEmpNum.setBounds(760, 110, 70, 25);

        javax.swing.GroupLayout jDialogNewDispatchSpecialLayout = new javax.swing.GroupLayout(jDialogNewDispatchSpecial.getContentPane());
        jDialogNewDispatchSpecial.getContentPane().setLayout(jDialogNewDispatchSpecialLayout);
        jDialogNewDispatchSpecialLayout.setHorizontalGroup(
            jDialogNewDispatchSpecialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelDispatch4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogNewDispatchSpecialLayout.setVerticalGroup(
            jDialogNewDispatchSpecialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelDispatch4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialogSearchName.setTitle("Search Employee ID");
        jDialogSearchName.setAlwaysOnTop(true);
        jDialogSearchName.setAutoRequestFocus(false);
        jDialogSearchName.setBackground(new java.awt.Color(153, 204, 0));
        jDialogSearchName.setLocation(new java.awt.Point(700, 400));
        jDialogSearchName.setMinimumSize(new java.awt.Dimension(400, 200));

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
        jTextFieldSearchNam.setBounds(10, 22, 204, 30);

        jButtonSearch.setText("Search");
        jButtonSearch.setPreferredSize(new java.awt.Dimension(65, 30));
        jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchActionPerformed(evt);
            }
        });
        jPanelSearchID.add(jButtonSearch);
        jButtonSearch.setBounds(256, 22, 90, 30);

        jComboBoxSearchResult.setPreferredSize(new java.awt.Dimension(70, 30));
        jPanelSearchID.add(jComboBoxSearchResult);
        jComboBoxSearchResult.setBounds(10, 70, 204, 30);

        jButtonOk.setText("OK");
        jButtonOk.setPreferredSize(new java.awt.Dimension(73, 30));
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });
        jPanelSearchID.add(jButtonOk);
        jButtonOk.setBounds(256, 70, 90, 30);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Enter search term e.g. Joe and click search button");
        jPanelSearchID.add(jLabel4);
        jLabel4.setBounds(12, 3, 270, 13);

        javax.swing.GroupLayout jDialogSearchNameLayout = new javax.swing.GroupLayout(jDialogSearchName.getContentPane());
        jDialogSearchName.getContentPane().setLayout(jDialogSearchNameLayout);
        jDialogSearchNameLayout.setHorizontalGroup(
            jDialogSearchNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelSearchID, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogSearchNameLayout.setVerticalGroup(
            jDialogSearchNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelSearchID, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jDialogSearchWithDrawName.setTitle("Search Employee ID");
        jDialogSearchWithDrawName.setAlwaysOnTop(true);
        jDialogSearchWithDrawName.setAutoRequestFocus(false);
        jDialogSearchWithDrawName.setBackground(new java.awt.Color(153, 204, 0));
        jDialogSearchWithDrawName.setLocation(new java.awt.Point(700, 400));
        jDialogSearchWithDrawName.setMinimumSize(new java.awt.Dimension(400, 200));

        jPanelSearchWithdrawID.setBackground(new java.awt.Color(255, 153, 153));
        jPanelSearchWithdrawID.setMinimumSize(new java.awt.Dimension(400, 200));
        jPanelSearchWithdrawID.setLayout(null);

        jTextFieldSearchWithdrawNam.setPreferredSize(new java.awt.Dimension(50, 30));
        jTextFieldSearchWithdrawNam.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldSearchWithdrawNamFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldSearchWithdrawNamFocusLost(evt);
            }
        });
        jTextFieldSearchWithdrawNam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldSearchWithdrawNamActionPerformed(evt);
            }
        });
        jPanelSearchWithdrawID.add(jTextFieldSearchWithdrawNam);
        jTextFieldSearchWithdrawNam.setBounds(10, 22, 204, 30);

        jButtonWithdrawSearch.setText("Search");
        jButtonWithdrawSearch.setPreferredSize(new java.awt.Dimension(65, 30));
        jButtonWithdrawSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWithdrawSearchActionPerformed(evt);
            }
        });
        jPanelSearchWithdrawID.add(jButtonWithdrawSearch);
        jButtonWithdrawSearch.setBounds(256, 22, 90, 30);

        jComboBoxWithdrawSearchResult.setPreferredSize(new java.awt.Dimension(70, 30));
        jPanelSearchWithdrawID.add(jComboBoxWithdrawSearchResult);
        jComboBoxWithdrawSearchResult.setBounds(10, 70, 204, 30);

        jButtonWithdrawOk.setText("OK");
        jButtonWithdrawOk.setPreferredSize(new java.awt.Dimension(73, 30));
        jButtonWithdrawOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWithdrawOkActionPerformed(evt);
            }
        });
        jPanelSearchWithdrawID.add(jButtonWithdrawOk);
        jButtonWithdrawOk.setBounds(256, 70, 90, 30);

        jLabelWithdrawHeader.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelWithdrawHeader.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWithdrawHeader.setText("Enter search term e.g. Joe and click search button");
        jPanelSearchWithdrawID.add(jLabelWithdrawHeader);
        jLabelWithdrawHeader.setBounds(12, 3, 270, 13);

        javax.swing.GroupLayout jDialogSearchWithDrawNameLayout = new javax.swing.GroupLayout(jDialogSearchWithDrawName.getContentPane());
        jDialogSearchWithDrawName.getContentPane().setLayout(jDialogSearchWithDrawNameLayout);
        jDialogSearchWithDrawNameLayout.setHorizontalGroup(
            jDialogSearchWithDrawNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelSearchWithdrawID, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogSearchWithDrawNameLayout.setVerticalGroup(
            jDialogSearchWithDrawNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelSearchWithdrawID, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jDialogPleaseWait.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jDialogPleaseWait.setTitle("                    Retrieving  requeeted iinformation.  Please Wait.");
        jDialogPleaseWait.setAlwaysOnTop(true);
        jDialogPleaseWait.setBackground(new java.awt.Color(51, 255, 51));
        jDialogPleaseWait.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jDialogPleaseWait.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jDialogPleaseWait.setForeground(new java.awt.Color(255, 0, 0));
        jDialogPleaseWait.setIconImages(null);
        jDialogPleaseWait.setLocation(new java.awt.Point(650, 375));
        jDialogPleaseWait.setMinimumSize(new java.awt.Dimension(500, 50));
        jDialogPleaseWait.setResizable(false);

        javax.swing.GroupLayout jDialogPleaseWaitLayout = new javax.swing.GroupLayout(jDialogPleaseWait.getContentPane());
        jDialogPleaseWait.getContentPane().setLayout(jDialogPleaseWaitLayout);
        jDialogPleaseWaitLayout.setHorizontalGroup(
            jDialogPleaseWaitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 453, Short.MAX_VALUE)
        );
        jDialogPleaseWaitLayout.setVerticalGroup(
            jDialogPleaseWaitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        jDialogStockPeriodOpen.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialogStockPeriodOpen.setTitle("TIMESHEET REDIRECTION");
        jDialogStockPeriodOpen.setAlwaysOnTop(true);
        jDialogStockPeriodOpen.setLocation(new java.awt.Point(350, 150));
        jDialogStockPeriodOpen.setMinimumSize(new java.awt.Dimension(700, 283));

        jPanelRejMain4.setBackground(new java.awt.Color(204, 204, 0));
        jPanelRejMain4.setMinimumSize(new java.awt.Dimension(700, 450));
        jPanelRejMain4.setLayout(null);

        jLabelHeader3.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabelHeader3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelHeader3.setText("STOCK TAKE PERIOD MANAGEMENT");
        jPanelRejMain4.add(jLabelHeader3);
        jLabelHeader3.setBounds(150, 0, 350, 40);

        jButtonBatchCancel.setText("Cancel");
        jButtonBatchCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBatchCancelActionPerformed(evt);
            }
        });
        jPanelRejMain4.add(jButtonBatchCancel);
        jButtonBatchCancel.setBounds(370, 190, 110, 25);

        jButtonBatchSave.setText("Submit");
        jButtonBatchSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBatchSaveActionPerformed(evt);
            }
        });
        jPanelRejMain4.add(jButtonBatchSave);
        jButtonBatchSave.setBounds(180, 190, 110, 25);
        jPanelRejMain4.add(jLabelEmpNum1);
        jLabelEmpNum1.setBounds(550, 10, 40, 30);

        jSeparator27.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRejMain4.add(jSeparator27);
        jSeparator27.setBounds(0, 50, 700, 2);

        jLabelBatchYear.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelBatchYear.setText("Stock Take  Year");
        jPanelRejMain4.add(jLabelBatchYear);
        jLabelBatchYear.setBounds(5, 100, 110, 25);

        jLabelBatchClose.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelBatchClose.setText("Stock Take Month");
        jPanelRejMain4.add(jLabelBatchClose);
        jLabelBatchClose.setBounds(220, 100, 120, 25);
        jPanelRejMain4.add(jYearChooser);
        jYearChooser.setBounds(120, 100, 50, 25);
        jPanelRejMain4.add(jLabelEmpNam);
        jLabelEmpNam.setBounds(530, 60, 140, 30);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel1.setText("Open Stock Take Period - All Districts");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(1, 0, 200, 25);

        jPanelRejMain4.add(jPanel1);
        jPanel1.setBounds(5, 55, 210, 25);

        jSeparator33.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRejMain4.add(jSeparator33);
        jSeparator33.setBounds(0, 150, 700, 2);

        jComboMonth.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));
        jPanelRejMain4.add(jComboMonth);
        jComboMonth.setBounds(360, 100, 80, 25);

        javax.swing.GroupLayout jDialogStockPeriodOpenLayout = new javax.swing.GroupLayout(jDialogStockPeriodOpen.getContentPane());
        jDialogStockPeriodOpen.getContentPane().setLayout(jDialogStockPeriodOpenLayout);
        jDialogStockPeriodOpenLayout.setHorizontalGroup(
            jDialogStockPeriodOpenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelRejMain4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogStockPeriodOpenLayout.setVerticalGroup(
            jDialogStockPeriodOpenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelRejMain4, javax.swing.GroupLayout.PREFERRED_SIZE, 283, Short.MAX_VALUE)
        );

        jDialogFuelView.setTitle("FUEL VIEW");
        jDialogFuelView.setAlwaysOnTop(true);
        jDialogFuelView.setLocation(new java.awt.Point(350, 100));
        jDialogFuelView.setMinimumSize(new java.awt.Dimension(920, 400));
        jDialogFuelView.getContentPane().setLayout(null);

        jLabelheading.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelheading.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelheading.setText("FUEL LOGGED FOR THE VEHICLE");
        jLabelheading.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jDialogFuelView.getContentPane().add(jLabelheading);
        jLabelheading.setBounds(230, 10, 380, 30);

        jLabelDateTo.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelDateTo.setText("To");
        jDialogFuelView.getContentPane().add(jLabelDateTo);
        jLabelDateTo.setBounds(470, 55, 30, 30);
        jDialogFuelView.getContentPane().add(jTextVehicleNo);
        jTextVehicleNo.setBounds(90, 55, 70, 30);

        jLabelVehicleNo.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelVehicleNo.setText("Vehicle No.");
        jDialogFuelView.getContentPane().add(jLabelVehicleNo);
        jLabelVehicleNo.setBounds(20, 55, 60, 30);

        jLabelDateFrom.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabelDateFrom.setText("From");
        jDialogFuelView.getContentPane().add(jLabelDateFrom);
        jLabelDateFrom.setBounds(200, 55, 50, 30);

        jButtonRetrieve.setBackground(new java.awt.Color(0, 204, 51));
        jButtonRetrieve.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButtonRetrieve.setText("Retrieve");
        jButtonRetrieve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRetrieveActionPerformed(evt);
            }
        });
        jDialogFuelView.getContentPane().add(jButtonRetrieve);
        jButtonRetrieve.setBounds(700, 55, 100, 30);

        jTableFuelDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Driver Name", "Date", "Time", "Fuel Card No", "Service Provider", "Reciept No", "Litres", "Fuel Cost", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTableFuelDetails);

        jDialogFuelView.getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(10, 100, 880, 190);

        jDateFrom.setDateFormatString("d MMM y");
        jDialogFuelView.getContentPane().add(jDateFrom);
        jDateFrom.setBounds(250, 55, 130, 30);

        jDateTo.setDateFormatString("d MMM y");
        jDialogFuelView.getContentPane().add(jDateTo);
        jDateTo.setBounds(500, 55, 130, 30);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PERDIEM REQUEST - MAIN PAGE");
        setBackground(new java.awt.Color(0, 102, 102));

        jPanel2.setBackground(new java.awt.Color(229, 234, 219));
        jPanel2.setLayout(null);

        jLabelDisPic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/mainZvandiri.png"))); // NOI18N
        jPanel2.add(jLabelDisPic);
        jLabelDisPic.setBounds(730, 300, 620, 360);

        jLabelDisVision.setText("Our vision is to expand our Zvandiri model to 20 countries by 2030 to deliver health, happiness and hope to 1 million young people living with HIV.");
        jPanel2.add(jLabelDisVision);
        jLabelDisVision.setBounds(50, 240, 700, 30);

        jLabelMissionStat1.setText("Health, Happiness and Hope for young people living with HIV.");
        jPanel2.add(jLabelMissionStat1);
        jLabelMissionStat1.setBounds(50, 360, 670, 40);

        jLabelVision.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelVision.setText("Our Vision");
        jPanel2.add(jLabelVision);
        jLabelVision.setBounds(50, 200, 100, 30);

        jLabelMission.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelMission.setText("Our Mission");
        jPanel2.add(jLabelMission);
        jLabelMission.setBounds(50, 330, 120, 22);

        jLabellogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanel2.add(jLabellogo);
        jLabellogo.setBounds(5, 0, 104, 115);

        jLabelHeaderGen.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderGen.setForeground(new java.awt.Color(0, 102, 102));
        jLabelHeaderGen.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanel2.add(jLabelHeaderGen);
        jLabelHeaderGen.setBounds(350, 60, 650, 40);
        jPanel2.add(jLabelEmp);
        jLabelEmp.setBounds(1180, 100, 70, 30);

        jLabelGenDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenDate.setForeground(new java.awt.Color(0, 102, 102));
        jPanel2.add(jLabelGenDate);
        jLabelGenDate.setBounds(1070, 0, 130, 30);

        jLabelGenTime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenTime.setForeground(new java.awt.Color(0, 102, 102));
        jPanel2.add(jLabelGenTime);
        jLabelGenTime.setBounds(1240, 0, 100, 30);

        jLabelGenLogNam1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam1.setForeground(new java.awt.Color(0, 102, 102));
        jLabelGenLogNam1.setText("Logged In");
        jPanel2.add(jLabelGenLogNam1);
        jLabelGenLogNam1.setBounds(1070, 40, 100, 30);

        jLabelGenLogNam.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam.setForeground(new java.awt.Color(0, 102, 102));
        jPanel2.add(jLabelGenLogNam);
        jLabelGenLogNam.setBounds(1170, 40, 180, 30);

        jLabelPEPFARpic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/PEPFARLogo.jpg"))); // NOI18N
        jPanel2.add(jLabelPEPFARpic);
        jLabelPEPFARpic.setBounds(5, 520, 250, 150);

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
        jMenuFile.add(jSeparator2);

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
        jMenuReports.add(jSeparator24);

        jMenuItemUserCreate.setText("Profile - User Create");
        jMenuItemUserCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemUserCreateActionPerformed(evt);
            }
        });
        jMenuReports.add(jMenuItemUserCreate);
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
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1354, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 669, Short.MAX_VALUE)
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(1378, 739));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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

    private void jMenuItemReqSubmittedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReqSubmittedActionPerformed
        new JFrameReqAllUserList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemReqSubmittedActionPerformed

    private void jMenuItemReqGenLstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemReqGenLstActionPerformed
        new JFrameMnthReqGenList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemReqGenLstActionPerformed

    private void jMenuItemPlanViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPlanViewActionPerformed
        new JFrameMnthViewList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemPlanViewActionPerformed

    private void jTextRefNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextRefNumActionPerformed

        checkRMDuplicateInvalid();


    }//GEN-LAST:event_jTextRefNumActionPerformed

    private void jButtonSelectFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectFileActionPerformed
        // jDialogNewDispatch.setVisible(false);
        pdfDB.pdfChooser();
        attFileName = pdfDB.fileName;

        File file = new File(attFileName);
        long attFileLength = file.length();
        if (attFileLength < 512000) {
            jLabelAttDocName.setText(file.getName());
            jLabelAttDocFilePath.setText(attFileName);
            // jTextAttDocPath.setVisible(false);
            //  jButtonMOHCCLeaveAtt.setText("Remove");
            jDialogNewDispatch.setVisible(true);
        } else if (attFileLength > 512000) {
            JOptionPane.showMessageDialog(this, "File size cannot be greater than 512Kb. ");
            jDialogNewDispatch.setVisible(true);
        }
    }//GEN-LAST:event_jButtonSelectFileActionPerformed

    private void jTextAttActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttActivityActionPerformed

    }//GEN-LAST:event_jTextAttActivityActionPerformed

    private void jButtonSelectFile1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectFile1ActionPerformed
        jDialogNewDispatch.setVisible(false);
    }//GEN-LAST:event_jButtonSelectFile1ActionPerformed

    private void jButtonSelectFile2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectFile2ActionPerformed
        saveSubmit();

    }//GEN-LAST:event_jButtonSelectFile2ActionPerformed

    private void jTextVoucherNumClrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextVoucherNumClrActionPerformed

        jLabelSerialClr.setVisible(true);

        normalDataRetrive();


    }//GEN-LAST:event_jTextVoucherNumClrActionPerformed

    private void jButtonSelectFileClrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectFileClrActionPerformed

        pdfDB.pdfChooser();
        attFileName = pdfDB.fileName;

        File file = new File(attFileName);
        long attFileLength = file.length();
        if (attFileLength < 512000) {
            jLabelAttDocNameClr.setText(file.getName());
            jLabelAttDocFilePathClr.setText(attFileName);

            jDialogLetterClear.setVisible(true);
        } else if (attFileLength > 512000) {
            JOptionPane.showMessageDialog(this, "File size cannot be greater than 512Kb. ");
            jDialogLetterClear.setVisible(true);
        }
    }//GEN-LAST:event_jButtonSelectFileClrActionPerformed

    private void jTextAttActivityClrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttActivityClrActionPerformed

    }//GEN-LAST:event_jTextAttActivityClrActionPerformed

    private void jButtonSelectFile4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectFile4ActionPerformed
        jDialogLetterClear.setVisible(false);
    }//GEN-LAST:event_jButtonSelectFile4ActionPerformed

    private void jButtonSelectFile5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectFile5ActionPerformed
        try {
//            jLabelSerialClr.setVisible(true);
//
//            normalDataRetrive();

            saveSubmit();

        } catch (Exception e) {
            System.out.println(e);
        }

    }//GEN-LAST:event_jButtonSelectFile5ActionPerformed

    private void jTextRefNumViewCollectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextRefNumViewCollectActionPerformed
        retriveViewCollect();
    }//GEN-LAST:event_jTextRefNumViewCollectActionPerformed

    private void jButtonViewCollectCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewCollectCancelActionPerformed
        jDialogViewCollect.setVisible(false);
    }//GEN-LAST:event_jButtonViewCollectCancelActionPerformed

    private void jButtonSelectViewCollectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectViewCollectActionPerformed
        retriveViewCollect();
    }//GEN-LAST:event_jButtonSelectViewCollectActionPerformed

    private void jButtonDownloadViewCollectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDownloadViewCollectActionPerformed

    }//GEN-LAST:event_jButtonDownloadViewCollectActionPerformed

    private void jTextBatNumSpecialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextBatNumSpecialActionPerformed
//        jLabelIssueEmpNumSpecial.setText("");
//        jLabelIssueToNameSpecial.setText("");
//        jTextTotAmountSpecial.setText("");
//        jLabelAttDocFilePathSpecial.setText("");
//        jLabelAttDocNameSpecial.setText("");
//        jTextAttActivitySpecial.setText("");
//        jTextFieldVoucherNumSpecial.setText("");
//        findExistSp(jTextFieldVoucherNumSpecial.getText());
//        try {
//            if (existCountBat == 0) {
//                jTextFieldVoucherNumSpecial.requestFocusInWindow();
//                jTextFieldVoucherNumSpecial.setFocusable(true);
//            } else if (existCountBat > 0) {
//                JOptionPane.showMessageDialog(jDialogNewDispatch, "Cash withdrawal letter has already been Proceesed. Please check and try again.");
//                jTextRefNum.requestFocusInWindow();
//                jTextRefNum.setFocusable(true);
//            } else {
//                JOptionPane.showMessageDialog(jDialogNewDispatch, "Invalid reference number for cash withdrawal letter dispatch. Please check and try again.");
//                jTextRefNum.requestFocusInWindow();
//                jTextRefNum.setFocusable(true);
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }
    }//GEN-LAST:event_jTextBatNumSpecialActionPerformed

    private void jButtonSelectFileSpecialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectFileSpecialActionPerformed
        // jDialogNewDispatch.setVisible(false);
        pdfDB.pdfChooser();
        attFileName = pdfDB.fileName;

        File file = new File(attFileName);
        long attFileLength = file.length();
        if (attFileLength < 512000) {
            jLabelAttDocNameSpecial.setText(file.getName());
            jLabelAttDocFilePathSpecial.setText(attFileName);
            // jTextAttDocPath.setVisible(false);
            //  jButtonMOHCCLeaveAtt.setText("Remove");

        } else if (attFileLength > 512000) {
            JOptionPane.showMessageDialog(jDialogNewDispatchSpecial, "File size cannot be greater than 512Kb. ");
            //jDialogNewDispatch.setVisible(true);
        }
    }//GEN-LAST:event_jButtonSelectFileSpecialActionPerformed

    private void jTextAttActivitySpecialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAttActivitySpecialActionPerformed

    }//GEN-LAST:event_jTextAttActivitySpecialActionPerformed

    private void jButtonCancelSpecialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelSpecialActionPerformed
        jDialogNewDispatchSpecial.setVisible(false);
    }//GEN-LAST:event_jButtonCancelSpecialActionPerformed

    private void jButtonSubmitSpecialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSubmitSpecialActionPerformed

        saveSubmit();
    }//GEN-LAST:event_jButtonSubmitSpecialActionPerformed

    private void jComboProvinceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboProvinceActionPerformed
//        if (existCountBat == 0) {
//            recSend.getProvFinName(jComboProvince.getSelectedItem().toString());
//            jLabelIssueToNameSpecial.setText(recSend.ProvFinNam);
//            jLabelIssueEmpNumSpecial.setText(recSend.ProvEmpNum);
//        }
    }//GEN-LAST:event_jComboProvinceActionPerformed

    private void jTextFieldVoucherNumSpecialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldVoucherNumSpecialActionPerformed
        checkVoucherSPDuplicate();
    }//GEN-LAST:event_jTextFieldVoucherNumSpecialActionPerformed

    private void jTextTotAmountSpecialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextTotAmountSpecialActionPerformed

    }//GEN-LAST:event_jTextTotAmountSpecialActionPerformed

    private void jTextTotAmountSpecialKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextTotAmountSpecialKeyPressed
        int key = evt.getKeyCode();
        if ((!((key >= evt.VK_0 && key <= evt.VK_9) || (key >= evt.VK_NUMPAD0 && key <= evt.VK_NUMPAD9)))
                && !(key == evt.VK_PERIOD) && !(key == evt.VK_DECIMAL) && !(key == evt.VK_ENTER) && !(key == evt.VK_BACK_SPACE)) {
            JOptionPane.showMessageDialog(jDialogNewDispatchSpecial, "Field allows only digits.");
            jTextTotAmountSpecial.setText("");
            jTextTotAmountSpecial.requestFocusInWindow();
            jTextTotAmountSpecial.setFocusable(true);
        }
    }//GEN-LAST:event_jTextTotAmountSpecialKeyPressed

    private void jTextVoucherNumClrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextVoucherNumClrKeyPressed

    }//GEN-LAST:event_jTextVoucherNumClrKeyPressed

    private void jTextVoucherNumClrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextVoucherNumClrMouseClicked

    }//GEN-LAST:event_jTextVoucherNumClrMouseClicked

    private void jTextFieldSearchNamFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldSearchNamFocusGained

    }//GEN-LAST:event_jTextFieldSearchNamFocusGained

    private void jTextFieldSearchNamFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldSearchNamFocusLost

    }//GEN-LAST:event_jTextFieldSearchNamFocusLost

    private void jTextFieldSearchNamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldSearchNamActionPerformed

    }//GEN-LAST:event_jTextFieldSearchNamActionPerformed

    private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchActionPerformed

        searchName();
    }//GEN-LAST:event_jButtonSearchActionPerformed

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed

        String str = jComboBoxSearchResult.getSelectedItem().toString();

        jTextFieldSearchNam.setText("");
        jLabelIssueToNameSpecial.setText(str);
        jDialogSearchName.dispose();
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jLabelSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelSearchMouseClicked
        jDialogSearchName.setVisible(true);
    }//GEN-LAST:event_jLabelSearchMouseClicked

    private void jButtonDownloadViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDownloadViewActionPerformed

//        try {
//            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
//                + "//" + ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + usrName + ";password=" + usrPass + ";");
//            attL.getPDFData(conn, jTextRefNumView.getText());
//        } catch (Exception e) {
//            System.out.println();
//        }
    }//GEN-LAST:event_jButtonDownloadViewActionPerformed

    private void jButtonSelectViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectViewActionPerformed
        retrieveViewDispatch();
    }//GEN-LAST:event_jButtonSelectViewActionPerformed

    private void jButtonViewCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewCancelActionPerformed
        jDialogViewDispatch.setVisible(false);
    }//GEN-LAST:event_jButtonViewCancelActionPerformed

    private void jTextRefNumViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextRefNumViewActionPerformed
        retrieveViewDispatch();
    }//GEN-LAST:event_jTextRefNumViewActionPerformed

    private void jTextFieldSearchWithdrawNamFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldSearchWithdrawNamFocusGained

    }//GEN-LAST:event_jTextFieldSearchWithdrawNamFocusGained

    private void jTextFieldSearchWithdrawNamFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldSearchWithdrawNamFocusLost

    }//GEN-LAST:event_jTextFieldSearchWithdrawNamFocusLost

    private void jTextFieldSearchWithdrawNamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldSearchWithdrawNamActionPerformed

    }//GEN-LAST:event_jTextFieldSearchWithdrawNamActionPerformed

    private void jButtonWithdrawSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWithdrawSearchActionPerformed
        searchWithDrawName();
    }//GEN-LAST:event_jButtonWithdrawSearchActionPerformed

    private void jButtonWithdrawOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWithdrawOkActionPerformed

        String str = jComboBoxWithdrawSearchResult.getSelectedItem().toString();
        if ("RM".equals(jLabelSerial.getText())) {
            jTextFieldSearchWithdrawNam.setText("");
            jLabelWithDrawByName.setText(str);
        }
        if ("Batch".equals(jLabelSerialSpecial.getText())) {
            jTextFieldSearchWithdrawNam.setText("");
            jLabelWithDrawBySPName.setText(str);
            recSend.getSendToWithDrawName(jLabelWithDrawBySPName.getText());
            jLabelWithDrawBySPEmpNum.setText(recSend.sendToWithDrawEmpNum);
            sendToWithDrawEmpNam = recSend.sendToWithDrawEmpNam;
            sendToWithDrawEmpEmail = recSend.sendToWithDrawEmpEmail;

        }
        jDialogSearchWithDrawName.dispose();
    }//GEN-LAST:event_jButtonWithdrawOkActionPerformed

    private void jLabelSearch1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelSearch1MouseClicked
        jDialogSearchWithDrawName.setVisible(true);
    }//GEN-LAST:event_jLabelSearch1MouseClicked

    private void jLabelSearchSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelSearchSPMouseClicked
        jDialogSearchWithDrawName.setVisible(true);
    }//GEN-LAST:event_jLabelSearchSPMouseClicked

    private void jTextFieldVoucherNumSpecialKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldVoucherNumSpecialKeyReleased

    }//GEN-LAST:event_jTextFieldVoucherNumSpecialKeyReleased

    private void jTextFieldVoucherNumSpecialKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldVoucherNumSpecialKeyPressed

    }//GEN-LAST:event_jTextFieldVoucherNumSpecialKeyPressed

    private void jTextFieldVoucherNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldVoucherNumActionPerformed
        checkVoucherDuplicate();
    }//GEN-LAST:event_jTextFieldVoucherNumActionPerformed

    private void jButtonBatchCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBatchCancelActionPerformed
        jDialogStockPeriodOpen.setVisible(false);
    }//GEN-LAST:event_jButtonBatchCancelActionPerformed

    private void jButtonBatchSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBatchSaveActionPerformed
        try {
            {

                int selectedOption = JOptionPane.showConfirmDialog(jDialogStockPeriodOpen,
                        "Are you sure that you want to open stock period for " + jComboMonth.getSelectedItem().toString() + " " + jYearChooser.getYear() + " ",
                        "Choose",
                        JOptionPane.YES_NO_OPTION);
                if (selectedOption == JOptionPane.YES_OPTION) {
                    checkRunningSTockTake();
                    if (stockTakeCountExist > 0) {
                        JOptionPane.showMessageDialog(jDialogStockPeriodOpen,
                                "One or more stock take still running. Please check and try again.");
                        jYearChooser.requestFocusInWindow();
                        jYearChooser.setFocusable(true);
                    } else {
                        openStockPeriod(Integer.toString(jYearChooser.getYear()), jComboMonth.getSelectedItem().toString());
                        createStockTakeOpenAction();
                        //send mail

                        JOptionPane.showMessageDialog(jDialogStockPeriodOpen,
                                "<html> Stock take period <b> " + jComboMonth.getSelectedItem().toString() + ""
                                + " " + jYearChooser.getYear() + "</b> for ALL Districts now open </html>");
                        jDialogStockPeriodOpen.setVisible(false);

                    }

                } else {

                }
            }

        } catch (Exception e) {

            System.out.println(e);
        }
    }//GEN-LAST:event_jButtonBatchSaveActionPerformed

    private void jButtonRetrieveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRetrieveActionPerformed
        try {
            while (modelFuelDetail.getRowCount() > 0) {
                modelFuelDetail.removeRow(0);
            }

            if (jTextVehicleNo.getText().trim().length() == 0) {
                JOptionPane.showMessageDialog(jDialogFuelView, "Vehicle registration number cannot be blank. Please check and correct.");
            } else if (jDateFrom.getDate() == null) {
                JOptionPane.showMessageDialog(jDialogFuelView, "Date cannot be blank. Please check your dates");
                jDateFrom.requestFocusInWindow();
                jDateFrom.setFocusable(true);
            } else if (jDateTo.getDate() == null) {
                JOptionPane.showMessageDialog(jDialogFuelView, "Date cannot be blank. Please check your dates");
                jDateTo.requestFocusInWindow();
                jDateTo.setFocusable(true);
//            
            } else if (jDateFrom.getDate().after(jDateTo.getDate())) {
                JOptionPane.showMessageDialog(jDialogFuelView, "End Date cannot be lower than start date.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                jDateFrom.setDate(null);
                jDateTo.setDate(null);
                jDateFrom.requestFocusInWindow();
                jDateFrom.setFocusable(true);
            } else {

                strVehReg = jTextVehicleNo.getText();
                strVehReg = strVehReg.replaceAll("\\s", "");
                findVehicle();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_jButtonRetrieveActionPerformed

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

    private void jMenuItemViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemViewActionPerformed
        new JFrameReqViewApp(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemViewActionPerformed

    private void jMenuItemSchPerDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSchPerDiemActionPerformed
        new JFrameReqHQPayScheduleApp(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemSchPerDiemActionPerformed

    private void jMenuItemHeadAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemHeadAppActionPerformed
        //        JOptionPane.showMessageDialog(this, "Please note that approval check boxes have been moved to the first tab (User and Accounting Details)");
        new JFrameReqHeadAppList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemHeadAppActionPerformed

    private void jMenuItemAccMgrRevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAccMgrRevActionPerformed
        //        JOptionPane.showMessageDialog(this, "Please note that approval check boxes have been moved to the first tab (User and Accounting Details)");
        new JFrameAccMgrAppList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemAccMgrRevActionPerformed

    private void jMenuItemSupAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSupAppActionPerformed
        //        JOptionPane.showMessageDialog(this, "Please note that approval check boxes have been moved to the first tab (User and Accounting Details)");
        new JFrameSupAppList(jLabelEmp.getText()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemSupAppActionPerformed

    private void jMenuFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuFileActionPerformed

    }//GEN-LAST:event_jMenuFileActionPerformed

    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItemExitActionPerformed

    private void jMenuItemCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCloseActionPerformed
        new JFrameFinSysLogin().setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jMenuItemCloseActionPerformed

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
            java.util.logging.Logger.getLogger(JFrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

                new JFrameMain().setVisible(true);

            }

        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupReference;
    private javax.swing.JButton jButtonBatchCancel;
    private javax.swing.JButton jButtonBatchSave;
    private javax.swing.JButton jButtonCancelSpecial;
    private javax.swing.JButton jButtonDownloadView;
    private javax.swing.JButton jButtonDownloadViewCollect;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JButton jButtonRetrieve;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JButton jButtonSelectFile;
    private javax.swing.JButton jButtonSelectFile1;
    private javax.swing.JButton jButtonSelectFile2;
    private javax.swing.JButton jButtonSelectFile4;
    private javax.swing.JButton jButtonSelectFile5;
    private javax.swing.JButton jButtonSelectFileClr;
    private javax.swing.JButton jButtonSelectFileSpecial;
    private javax.swing.JButton jButtonSelectView;
    private javax.swing.JButton jButtonSelectViewCollect;
    private javax.swing.JButton jButtonSubmitSpecial;
    private javax.swing.JButton jButtonViewCancel;
    private javax.swing.JButton jButtonViewCollectCancel;
    private javax.swing.JButton jButtonWithdrawOk;
    private javax.swing.JButton jButtonWithdrawSearch;
    private javax.swing.JComboBox<String> jComboBoxSearchResult;
    private javax.swing.JComboBox<String> jComboBoxWithdrawSearchResult;
    private javax.swing.JComboBox<String> jComboMonth;
    private javax.swing.JComboBox<String> jComboProvince;
    private com.toedter.calendar.JDateChooser jDateCollected;
    private com.toedter.calendar.JDateChooser jDateFrom;
    private com.toedter.calendar.JDateChooser jDateTo;
    private com.toedter.calendar.JDateChooser jDateWithdrawn;
    public javax.swing.JDialog jDialogFuelView;
    private javax.swing.JDialog jDialogLetterClear;
    private javax.swing.JDialog jDialogNewDispatch;
    private javax.swing.JDialog jDialogNewDispatchSpecial;
    private javax.swing.JDialog jDialogPleaseWait;
    private javax.swing.JDialog jDialogSearchName;
    private javax.swing.JDialog jDialogSearchWithDrawName;
    public javax.swing.JDialog jDialogStockPeriodOpen;
    private javax.swing.JDialog jDialogViewCollect;
    private javax.swing.JDialog jDialogViewDispatch;
    private javax.swing.JDialog jDialogWaitingEmail;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelAmount;
    private javax.swing.JLabel jLabelAmountClr;
    private javax.swing.JLabel jLabelAmountSpecial;
    private javax.swing.JLabel jLabelAmountView;
    private javax.swing.JLabel jLabelAmountViewCollect;
    private javax.swing.JLabel jLabelAttActivityView;
    private javax.swing.JLabel jLabelAttActivityViewCollect;
    private javax.swing.JLabel jLabelAttDocFilePath;
    private javax.swing.JLabel jLabelAttDocFilePathClr;
    private javax.swing.JLabel jLabelAttDocFilePathSpecial;
    private javax.swing.JLabel jLabelAttDocName;
    private javax.swing.JLabel jLabelAttDocNameClr;
    private javax.swing.JLabel jLabelAttDocNameSpecial;
    private javax.swing.JLabel jLabelAttDocNameView;
    private javax.swing.JLabel jLabelAttDocNameViewCollect;
    private javax.swing.JLabel jLabelBatchClose;
    private javax.swing.JLabel jLabelBatchYear;
    private javax.swing.JLabel jLabelComments;
    private javax.swing.JLabel jLabelDateCollected;
    private javax.swing.JLabel jLabelDateDispatchView;
    private javax.swing.JLabel jLabelDateDispatchViewCollect;
    private javax.swing.JLabel jLabelDateFrom;
    private javax.swing.JLabel jLabelDateTo;
    private javax.swing.JLabel jLabelDateWithdrawn;
    private javax.swing.JLabel jLabelDisPic;
    private javax.swing.JLabel jLabelDisVision;
    private javax.swing.JLabel jLabelDispatchView;
    private javax.swing.JLabel jLabelDispatchViewCollect;
    private javax.swing.JLabel jLabelEmp;
    private javax.swing.JLabel jLabelEmpNam;
    private javax.swing.JLabel jLabelEmpNum1;
    private javax.swing.JLabel jLabelGenDate;
    private javax.swing.JLabel jLabelGenLogNam;
    private javax.swing.JLabel jLabelGenLogNam1;
    private javax.swing.JLabel jLabelGenTime;
    private javax.swing.JLabel jLabelHeadView;
    private javax.swing.JLabel jLabelHeadViewCollect;
    private javax.swing.JLabel jLabelHeader;
    private javax.swing.JLabel jLabelHeader3;
    private javax.swing.JLabel jLabelHeaderClr;
    private javax.swing.JLabel jLabelHeaderGen;
    private javax.swing.JLabel jLabelHeaderSpecial;
    private javax.swing.JLabel jLabelIssueEmpNum;
    private javax.swing.JLabel jLabelIssueEmpNumClr;
    private javax.swing.JLabel jLabelIssueEmpNumSpecial;
    private javax.swing.JLabel jLabelIssueTo;
    private javax.swing.JLabel jLabelIssueToClr;
    private javax.swing.JLabel jLabelIssueToName;
    private javax.swing.JLabel jLabelIssueToNameClr;
    private javax.swing.JLabel jLabelIssueToNameSpecial;
    private javax.swing.JLabel jLabelIssueToNameView;
    private javax.swing.JLabel jLabelIssueToNameViewCollect;
    private javax.swing.JLabel jLabelIssueToNameWithdrawal;
    private javax.swing.JLabel jLabelIssueToSpecial;
    private javax.swing.JLabel jLabelIssueToView;
    private javax.swing.JLabel jLabelIssueToViewCollect;
    private javax.swing.JLabel jLabelMission;
    private javax.swing.JLabel jLabelMissionStat1;
    private javax.swing.JLabel jLabelPEPFARpic;
    private javax.swing.JLabel jLabelRefDate;
    private javax.swing.JLabel jLabelRefDateClr;
    private javax.swing.JLabel jLabelRefDateView;
    private javax.swing.JLabel jLabelRefDateViewCollect;
    private javax.swing.JLabel jLabelRefNo;
    private javax.swing.JLabel jLabelRefNoSpecial;
    private javax.swing.JLabel jLabelRefNoView;
    private javax.swing.JLabel jLabelRefNoViewCollect;
    private javax.swing.JLabel jLabelRefNumClr;
    private javax.swing.JLabel jLabelRefYear;
    private javax.swing.JLabel jLabelRefYearClr;
    private javax.swing.JLabel jLabelRefYearView;
    private javax.swing.JLabel jLabelRefYearViewCollect;
    private javax.swing.JLabel jLabelRegYear;
    private javax.swing.JLabel jLabelRegYear1;
    private javax.swing.JLabel jLabelSearch;
    private javax.swing.JLabel jLabelSearch1;
    private javax.swing.JLabel jLabelSearchSP;
    private javax.swing.JLabel jLabelSerial;
    private javax.swing.JLabel jLabelSerialClr;
    private javax.swing.JLabel jLabelSerialSpecial;
    private javax.swing.JLabel jLabelSerialView;
    private javax.swing.JLabel jLabelSerialViewCollect;
    private javax.swing.JLabel jLabelTotAmount;
    private javax.swing.JLabel jLabelTotAmountView;
    private javax.swing.JLabel jLabelTotAmountViewCollect;
    private javax.swing.JLabel jLabelVehicleNo;
    private javax.swing.JLabel jLabelVision;
    private javax.swing.JLabel jLabelVoucher;
    private javax.swing.JLabel jLabelVoucherNo;
    private javax.swing.JLabel jLabelVoucherNoSpecial;
    private javax.swing.JLabel jLabelVoucherNumClr1;
    private javax.swing.JLabel jLabelVoucherNumber;
    private javax.swing.JLabel jLabelVoucherNumberViewCollect;
    private javax.swing.JLabel jLabelVoucherViewCollect;
    private javax.swing.JLabel jLabelWithDrawByEmpNum;
    private javax.swing.JLabel jLabelWithDrawByName;
    private javax.swing.JLabel jLabelWithDrawBySPEmpNum;
    private javax.swing.JLabel jLabelWithDrawBySPName;
    private javax.swing.JLabel jLabelWithdrawBy;
    private javax.swing.JLabel jLabelWithdrawEmpNum;
    private javax.swing.JLabel jLabelWithdrawHeader;
    private javax.swing.JLabel jLabelWithdrawSPBy;
    private javax.swing.JLabel jLabelWithdrawalby;
    private javax.swing.JLabel jLabelWorkStation;
    private javax.swing.JLabel jLabelWorkStationClr;
    private javax.swing.JLabel jLabelWorkStationSpecial;
    private javax.swing.JLabel jLabelWorkStationView;
    private javax.swing.JLabel jLabelWorkStationViewCollect;
    private javax.swing.JLabel jLabelWorkstationDistrict;
    private javax.swing.JLabel jLabelWorkstationDistrictClr;
    private javax.swing.JLabel jLabelWorkstationDistrictView;
    private javax.swing.JLabel jLabelWorkstationDistrictViewCollect;
    private javax.swing.JLabel jLabelWorkstationProvince;
    private javax.swing.JLabel jLabelWorkstationProvinceClr;
    private javax.swing.JLabel jLabelWorkstationProvinceView;
    private javax.swing.JLabel jLabelWorkstationProvinceViewCollect;
    private javax.swing.JLabel jLabelheading;
    private javax.swing.JLabel jLabellogo;
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelDispatch;
    private javax.swing.JPanel jPanelDispatch1;
    public javax.swing.JPanel jPanelDispatch2;
    public javax.swing.JPanel jPanelDispatch3;
    private javax.swing.JPanel jPanelDispatch4;
    public javax.swing.JPanel jPanelRejMain4;
    private javax.swing.JPanel jPanelSearchID;
    private javax.swing.JPanel jPanelSearchWithdrawID;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator11;
    private javax.swing.JPopupMenu.Separator jSeparator13;
    private javax.swing.JPopupMenu.Separator jSeparator16;
    private javax.swing.JPopupMenu.Separator jSeparator18;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator22;
    private javax.swing.JPopupMenu.Separator jSeparator23;
    private javax.swing.JPopupMenu.Separator jSeparator24;
    private javax.swing.JPopupMenu.Separator jSeparator26;
    private javax.swing.JSeparator jSeparator27;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JSeparator jSeparator33;
    private javax.swing.JPopupMenu.Separator jSeparator34;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator62;
    private javax.swing.JPopupMenu.Separator jSeparator63;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JTable jTableFuelDetails;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextAttActivity;
    private javax.swing.JTextField jTextAttActivityClr;
    private javax.swing.JTextField jTextAttActivitySpecial;
    private javax.swing.JTextField jTextBatNumSpecial;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextFieldSearchNam;
    private javax.swing.JTextField jTextFieldSearchWithdrawNam;
    private javax.swing.JTextField jTextFieldVoucherNum;
    private javax.swing.JTextField jTextFieldVoucherNumSpecial;
    private javax.swing.JTextField jTextRefNum;
    private javax.swing.JTextField jTextRefNumView;
    private javax.swing.JTextField jTextRefNumViewCollect;
    private javax.swing.JTextField jTextTotAmountClr;
    private javax.swing.JTextField jTextTotAmountSpecial;
    private javax.swing.JTextField jTextVehicleNo;
    private javax.swing.JTextField jTextVoucherNumClr;
    private com.toedter.calendar.JYearChooser jYearChooser;
    // End of variables declaration//GEN-END:variables
}
