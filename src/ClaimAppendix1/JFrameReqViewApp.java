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
import java.text.DecimalFormat;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import utils.connCred;
import utils.connSaveFile;
import utils.savePDFToDB;
import utils.StockVehicleMgt;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.view.JRViewer;

/**
 *
 * @author cgoredema
 */
public class JFrameReqViewApp extends javax.swing.JFrame {

    PreparedStatement pst = null;
    PreparedStatement pst1 = null;
    connCred c = new connCred();
    int itmNum = 1;
    int month, finyear;
    int initdocVersion = 0;
    int clickCount = 0;
    int initactVersion = 0;
    int checkRefCount = 0;
    Date curYear = new Date();
    String sendToProv, sendToAcc, createUsrNam, actVerNum, regDocVersion, rejStatus,
            breakfastAll, lunchAll, lunchNPAll, lunchPAll, dinnerAll, rejComments,
            incidentalAll, unProvedAll, oldDocVersion, statusCodeApp,
            statusCodeDisApp, checkRef, refSearch, SearchRef,
            authUsrNam, authUsrNamAll, authNam1, authNam2, usrGrp, empOff;
    String province = "";
    SimpleDateFormat df = new SimpleDateFormat("yyyy");
    SimpleDateFormat sDateTime = new SimpleDateFormat("ddMMyyyy_HHMMSS");
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
    public JFrameReqViewApp() {
        initComponents();
        showDate();
        showTime();
        jLabelRegYear.setVisible(false);
        jLabelBreakFastSubTot.setVisible(false);
        jLabelBreakFastSub.setVisible(false);

    }

    public JFrameReqViewApp(int yearRef) {
        initComponents();
        showDate();
        showTime();
        modelWk1 = (DefaultTableModel) jTableWk1Activities.getModel();
        jLabelRegYear.setVisible(false);
        jLabelReject.setVisible(false);
        jLabelRejectComments.setVisible(false);
        jLabelBreakFastSubTot.setVisible(false);
        jLabelBreakFastSub.setVisible(false);

    }

    public JFrameReqViewApp(String usrLogNum) {
        initComponents();
        showDate();
        showTime();
        computerName();
        jPanelStatusView.setVisible(false);
        jLabelAuthoriser.setVisible(false);
        jLabelAuthNam1.setVisible(false);
        jLabelAuthNam2.setVisible(false);
        jTextAreaComments.setEnabled(false);
        jTextAreaComments.setWrapStyleWord(true);
        jTextAreaComments.setLineWrap(true);
        jLabelHeaderGen.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jRadioPartAllow.setVisible(false);
        jRadioPerDiem.setSelected(true);
        jLabelBreakFastSubTot.setVisible(false);
        jLabelBreakFastSub.setVisible(false);
        jButtonGenerateReport.setVisible(false);
        jButtonGenerateReport.setBounds(970, 120, 270, 30);
        modelWk1 = (DefaultTableModel) jTableWk1Activities.getModel();
        modelWk2 = (DefaultTableModel) jTableWk2Activities.getModel();
        modelWk3 = (DefaultTableModel) jTableWk3Activities.getModel();
        modelWk4 = (DefaultTableModel) jTableWk4Activities.getModel();
        modelWk5 = (DefaultTableModel) jTableWk5Activities.getModel();
        jLabelRegYear.setVisible(false);
        jLabelEmp.setVisible(false);
        jLabelEmp.setText(usrLogNum);
        jLabelReject.setVisible(false);
        jLabelRejectComments.setVisible(false);
        findUser();
        findUserGrp();

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
                jLabelLineTime2.setText(s.format(d));
                jLabelLineTime1.setText(s.format(d));
                jLabelLineTime3.setText(s.format(d));
                jLabelLineTime4.setText(s.format(d));
            }
        }) {

        }.start();

    }

    void showDate() {
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        jLabelGenDate.setText(s.format(d));
        jLabelLineDate2.setText(s.format(d));
        jLabelLineDate1.setText(s.format(d));
        jLabelLineDate3.setText(s.format(d));
        jLabelLineDate4.setText(s.format(d));
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

    void folderCreate() {
        File tmpDir = new File("C:\\Finance System Reports\\Request PDF");
        boolean exists = tmpDir.exists();
        if (exists) {

            System.out.println("Folder exists");

        } else {
            try {
                Path path = Paths.get("C:\\Finance System Reports\\Request PDF");

                Files.createDirectories(path);

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    void findUser() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select EMP_NAM,SUP_EMP_NUM,SUP_NAM,EMP_SUP_MAIL, "
                    + "EMP_PROVINCE,EMP_OFFICE,EMP_BNK_NAM,ACC_NUM from [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] a "
                    + "join [ClaimsAppSysZvandiri].[dbo].[EmpDetWS] b on a.EMP_NUM=b.EMP_NUM join "
                    + "[ClaimsAppSysZvandiri].[dbo].[EmpBankDetTab] c on a.EMP_NUM=c.EMP_NUM "
                    + "where a.EMP_NUM='" + jLabelEmp.getText() + "'");

            while (r.next()) {
                jLabelGenLogNam.setText(r.getString(1));
                jLabelLinLogNam2.setText(r.getString(1));
                jLabelLinLogNam1.setText(r.getString(1));
                jLabelLinLogNam3.setText(r.getString(1));
                jLabelLinLogNam4.setText(r.getString(1));

            }

            //                 conn.close();
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

    void imgDisplay() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT SERIAL,REF_NUM,ATT_IMAGE,IMG_VERSION "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimDocAttachTab] where concat(SERIAL,REF_NUM)="
                    + "'" + jLabelSerial.getText() + jTextRegNum.getText() + "'"
                    + " and IMG_VERSION = (SELECT MAX(IMG_VERSION) FROM"
                    + "[ClaimsAppSysZvandiri].[dbo].[ClaimDocAttachTab] WHERE concat(SERIAL,REF_NUM)="
                    + "'" + jLabelSerial.getText() + jTextRegNum.getText() + "')");

            while (r.next()) {

                byte[] img = r.getBytes("ATT_IMAGE");

                ImageIcon imageIcon = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(600, 535, Image.SCALE_SMOOTH));
                jLabelImg.setIcon(imageIcon);

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

    void generateView() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver:"
                    + "//" + c.ipAdd + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            String query = "select  x.SERIAL, x.REF_NUM ,Format(REF_DAT, 'dd MMM yyyy') REF_DAT ,EMP_NUM ,"
                    + "EMP_NAM ,EMP_TTL ,EMP_PROV ,EMP_OFF ,EMP_BNK_NAM ,ACC_NUM ,ACT_MAIN_PUR ,ACT_TOT_AMT , "
                    + "x.Breakfast, x.Lunch,x.Dinner,x.[Proven Acc],x.[Unproven Acc],x.Incidental,x.[Misc Amt],"
                    + "y.Creator,y.Supervisor,y.Finance,y.HOD,'" + jLabelGenLogNam.getText() + "' GENUSER from (SELECT  a.SERIAL, a.REF_NUM ,REF_DAT ,EMP_NUM ,"
                    + "EMP_NAM ,EMP_TTL ,EMP_PROV ,EMP_OFF ,EMP_BNK_NAM ,ACC_NUM ,ACT_MAIN_PUR ,ACT_TOT_AMT , "
                    + "SUM(b.BRK_AMT) 'Breakfast',SUM(b.LNC_AMT) 'Lunch',SUM(b.DIN_AMT) 'Dinner',SUM(b.ACC_PROV_AMT) 'Proven Acc',"
                    + "SUM(b.ACC_UNPROV_AMT) 'Unproven Acc',SUM(b.INC_AMT) 'Incidental' ,SUM(b.MSC_AMT) 'Misc Amt' "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab] a JOIN [ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] b "
                    + "on a.SERIAL = b.SERIAL AND a.REF_NUM = b.REF_NUM AND a.DOC_VER = b.DOC_VER AND a.ACT_REC_STA = b.ACT_REC_STA "
                    + "WHERE a.REF_NUM = " + jTextRegNum.getText() + " AND a.SERIAL = 'R' AND a.ACT_REC_STA = 'A' and a.DOC_VER =4 GROUP BY a.SERIAL, a.REF_NUM ,"
                    + "REF_DAT ,EMP_NUM ,EMP_NAM ,EMP_TTL ,EMP_PROV ,EMP_OFF ,EMP_BNK_NAM ,ACC_NUM ,ACT_MAIN_PUR ,ACT_TOT_AMT) x "
                    + "join (select a.REF_NUM,a.creator,b.supervisor,c.Finance, d.HOD from (SELECT distinct REF_NUM,ACTIONED_BY_NAM 'Creator',"
                    + "' ' 'Supervisor',' ' 'Finance',' ' 'Account Manager',' ' 'HOD' FROM [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab]  "
                    + "where DOC_STATUS ='Registered' and REF_NUM = " + jTextRegNum.getText() + "   and SERIAL = 'R') a join (SELECT distinct REF_NUM,'' 'Creator',"
                    + "ACTIONED_BY_NAM 'Supervisor',' ' 'Finance',' ' 'Account Manager',' ' 'HOD' FROM [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab]  "
                    + "where DOC_STATUS ='SupApprove' and REF_NUM = " + jTextRegNum.getText() + "   and SERIAL = 'R' ) b on a.REF_NUM = b.REF_NUM join "
                    + "(SELECT distinct REF_NUM,'' 'Creator','' 'Supervisor',ACTIONED_BY_NAM 'Finance',' ' 'Account Manager',' ' 'HOD' "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab]  where DOC_STATUS ='FinApprove' and REF_NUM = " + jTextRegNum.getText() + "   and SERIAL = 'R') c "
                    + "on b.REF_NUM = c.REF_NUM join (SELECT distinct REF_NUM,'' 'Creator',' ' 'Supervisor',' ' 'Finance',' ' 'Account Manager',"
                    + "ACTIONED_BY_NAM 'HOD' FROM [ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab]  where DOC_STATUS ='HODApprove' and REF_NUM = " + jTextRegNum.getText() + "  and SERIAL = 'R') d "
                    + "on c.REF_NUM = d.REF_NUM) y on x.REF_NUM=y.REF_NUM";

            PreparedStatement preparedStatement = conn.prepareStatement(query);
//            preparedStatement.setString(1, employeeNumber);
            ResultSet r = preparedStatement.executeQuery();
//            ResultSet r = st.getResultSet();
//
            while (r.next()) {
                System.out.println(" ff " + r.getString(1));

            }
            folderCreate();

//            InputStream input = new FileInputStream(new File("src/JasperReports/ZimTTECH_Request.jrxml"));
//            JasperDesign jdesign = JRXmlLoader.load(input);
//
//            JRDesignQuery updateQuery = new JRDesignQuery();
//
//            updateQuery.setText(query);
//
//            jdesign.setQuery(updateQuery);
//
//            JasperReport jreport = JasperCompileManager.compileReport(jdesign);
//
//            JasperPrint jprint = JasperFillManager.fillReport(jreport, null, conn);
//
//            JasperExportManager.exportReportToPdfFile(jprint, "C:\\Finance System Reports\\Request PDF\\Request Report " + jLabelSerial.getText() + " " + jTextRegNum.getText() + ".pdf");
//            JasperViewer jv = new JasperViewer(jprint, false);
//            jv.setTitle("ZimTTECH Request ");
//            jv.setVisible(true);
//            JasperViewer.viewReport(jprint);
//            JRViewer viewer = new JRViewer(jprint);
//            JFrame frame = new JFrame("ZimTTECH Request");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.getContentPane().add(viewer);
//            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
////            frame.setAlwaysOnTop(true); 
//            frame.pack();
//            frame.setVisible(true);
//            InputStream input = new FileInputStream(new File("http://apps.ophid.co.zw:8080/hr/ZimTTECH_Request.jrxml"));
//            Date today = new Date();
//
//            URL url = new URL("http://service.zimttech.org:8080/zimttech/zimttechfin/ZimTTECH_Request.jrxml");
//            URLConnection connection = url.openConnection();
//            InputStream input = connection.getInputStream();
////            JasperDesign jdesign = JRXmlLoader.load(input);
//
//            JasperDesign jdesign = JRXmlLoader.load(input);
//
//            JRDesignQuery updateQuery = new JRDesignQuery();
//
//            updateQuery.setText(query);
//
//            jdesign.setQuery(updateQuery);
//
//            JasperReport jreport = JasperCompileManager.compileReport(jdesign);
//
//            JasperPrint jprint = JasperFillManager.fillReport(jreport, null, conn);
//
//            JasperExportManager.exportReportToPdfFile(jprint, "C:\\Finance System Reports\\Request PDF\\Request Report " + jLabelSerial.getText() + " " + jTextRegNum.getText() + " - Generated " + sDateTime.format(today) + ".pdf");
//
//            JRViewer viewer = new JRViewer(jprint);
//            JFrame frame = new JFrame("ZimTTECH Acquittal");
//            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//            frame.getContentPane().add(viewer);
//            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
////            frame.setAlwaysOnTop(true); 
//            frame.pack();
//            frame.setVisible(true);
            Date today = new Date();

            // Load the JRXML from URL
            URL url = new URL("http://apps.ophid.co.zw:8080/ZvandiriFin/ZvandiriFin/Zvandiri_Request.jrxml");
            URLConnection connection = url.openConnection();
            InputStream input = connection.getInputStream();

            // URL for the image
            String imageURL = "http://apps.ophid.co.zw:8080/ZvandiriFin/ZvandiriFin/COYLogo.jpg";

            // Set parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("ImageURL", imageURL);

            // Load the design
            JasperDesign jdesign = JRXmlLoader.load(input);

            // Update the query (assuming 'query' is a valid SQL query string defined elsewhere)
            JRDesignQuery updateQuery = new JRDesignQuery();
            updateQuery.setText(query);
            jdesign.setQuery(updateQuery);

            // Compile the report
            JasperReport jreport = JasperCompileManager.compileReport(jdesign);

            // Fill the report
            JasperPrint jprint = JasperFillManager.fillReport(jreport, parameters, conn);

            // Export the report to a PDF file
            JasperExportManager.exportReportToPdfFile(jprint, "C:\\Finance System Reports\\Request PDF\\Request Report " + jLabelSerial.getText() + " " + jTextRegNum.getText() + " - Generated " + sDateTime.format(today) + ".pdf");

            // View the report
            JRViewer viewer = new JRViewer(jprint);
            JFrame frame = new JFrame("ZimTTECH Acquittal");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.getContentPane().add(viewer);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.pack();
            frame.setVisible(true);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void fetchGenData() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st2 = conn.createStatement();
            st2.executeQuery("SELECT distinct SERIAL,REF_NUM FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[ClaimAppGenTab]"
                    + "where concat(SERIAL,REF_NUM) ='" + jLabelSerial.getText() + jTextRegNum.getText() + "'"
                    + " and  ACT_REC_STA = 'A'");

            ResultSet r2 = st2.getResultSet();

            while (r2.next()) {

                refSearch = r2.getString(2);
            }

            if (!(jTextRegNum.getText().equals(refSearch))) {
                JOptionPane.showMessageDialog(this, "Invalid requested number. Please chack and try again");

            } else {
                try {
                    System.out.println("ff " + refSearch);
                    Statement st1 = conn.createStatement();
                    Statement st = conn.createStatement();
                    Statement st3 = conn.createStatement();
                    Statement st4 = conn.createStatement();
                    Statement st5 = conn.createStatement();
                    Statement st6 = conn.createStatement();
                    st1.executeQuery("SELECT distinct a.REF_YEAR,a.SERIAL,a.REF_NUM,REF_DAT,a.EMP_NUM,"
                            + "a.EMP_NAM,a.EMP_TTL, a.EMP_PROV,a.EMP_OFF,a.EMP_BNK_NAM,a.ACC_NUM,a.ACT_MAIN_PUR, "
                            + "a.ACT_TOT_AMT,a.ACT_REC_STA,a.DOC_VER,b.USR_ACTION,b.REJECT_COMMENTS  "
                            + "FROM ClaimsAppSysZvandiri.dbo.ClaimAppGenTab a,[ClaimsAppSysZvandiri].[dbo].[ClaimsWFActTab] b "
                            + "where  ( a.REF_NUM = b.REF_NUM and a.DOC_VER=b.DOC_VER) and concat(a.SERIAL,a.REF_NUM)='" + jLabelSerial.getText() + jTextRegNum.getText() + "'"
                            + " and a.DOC_VER =(select max(DOC_VER) from ClaimsAppSysZvandiri.dbo.ClaimAppGenTab "
                            + "where concat(SERIAL,REF_NUM)='" + jLabelSerial.getText() + jTextRegNum.getText() + "') and a.ACT_REC_STA = 'A'");

                    ResultSet r1 = st1.getResultSet();

                    while (r1.next()) {
                        jLabelRegYear.setText(r1.getString(1));
                        jLabelSerial.setText(r1.getString(2));
                        jTextRegNum.setText(r1.getString(3));
                        jLabelReqEmpNum.setText(r1.getString(5));
                        jLabelEmpNam.setText(r1.getString(6));
                        jLabelEmpTitle.setText(r1.getString(7));
                        jLabelReqProvince.setText(r1.getString(8));
                        jLabelReqOffice.setText(r1.getString(9));
                        jLabelBankName.setText(r1.getString(10));
                        jLabelReqAccNum.setText(r1.getString(11));
                        jLabelActMainPurpose.setText(r1.getString(12));
                        jLabelAppTotReqCost.setText(r1.getString(13));
                        jLabelRegDate.setText(r1.getString(4));
                        regDocVersion = r1.getString(15);
                        rejStatus = r1.getString(16);
                        rejComments = r1.getString(17);

                        jLabelStatusView.setText(r1.getString(16));
//                        if ("Request - HOD Approved".equals(jLabelStatusView.getText())) {
//                            jLabelStatusView.setVisible(true);
//                            jPanelStatusView.setVisible(true);
//                            jLabelStatusView.setForeground(new java.awt.Color(0, 153, 0));
//                            jPanelStatusView.setVisible(true);
//                        } else {
//                            jLabelStatusView.setVisible(true);
//                            jPanelStatusView.setVisible(true);
//                            jLabelStatusView.setForeground(new java.awt.Color(255, 0, 0));
//                            jPanelStatusView.setVisible(true);
//                        }
                        if (("Request - HOD Approved".equals(jLabelStatusView.getText())) && (("usrProvMgr".equals(usrGrp)
                                || ("usrAccMgr".equals(usrGrp)) || ("usrFinReq".equals(usrGrp)) || ("usrFinSup".equals(usrGrp)))
                                || ("usrFinAcq".equals(usrGrp)) || ("Administrator".equals(usrGrp)))) {
                            jLabelStatusView.setVisible(false);
                            jPanelStatusView.setVisible(false);
                            jLabelStatusView.setForeground(new java.awt.Color(0, 153, 0));
                            jPanelStatusView.setVisible(false);
                            jButtonGenerateReport.setVisible(true);
                            jButtonGenerateReport.setBounds(970, 160, 270, 30);
                        } else if ("Request - HOD Approved".equals(jLabelStatusView.getText())) {
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

                        if (("Request - Supervisor Rejected".equals(rejStatus)) || ("Request - Finance Rejected".equals(rejStatus))
                                || ("Request - HOD Rejected".equals(rejStatus))) {
                            jLabelReject.setVisible(true);
                            jLabelRejectComments.setVisible(true);
                            jLabelRejectComments.setText(rejComments);

                        }

                    }

                    /**
                     * Week 1 *
                     */
//                    st.executeQuery("SELECT ACT_DATE,ACT_DEST,"
//                            + "ACT_DIST,ACT_ITM_PUR,LN_BUD_CODE,BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,"
//                            + "MSC_AMT,ACC_UNPROV_AMT,ACT_ITM_TOT FROM "
//                            + "[ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
//                            + "  where SERIAL = 'R' and concat(SERIAL,REF_NUM)='" + jLabelSerial.getText() + jTextRegNum.getText() + "'and "
//                            + "DOC_VER =(select max(DOC_VER) from ClaimsAppSysZvandiri.dbo.ClaimAppGenTab "
//                            + "where concat(SERIAL,REF_NUM)='" + jLabelSerial.getText() + jTextRegNum.getText() + "') and ACT_REC_STA='A' and PLAN_WK = 1"
//                            + "order by ACT_DATE");
//
//                    ResultSet r = st.getResultSet();
//
//                    while (r.next()) {
//                        modelWk1.insertRow(modelWk1.getRowCount(), new Object[]{r.getString(1), r.getString(2), r.getString(3),
//                            r.getString(4), r.getString(5), r.getString(6), r.getString(7), r.getString(8), r.getString(9),
//                            r.getString(10), r.getString(11), r.getString(12), r.getString(13)});
//
//                    }
//
//                    if (modelWk1.getRowCount() == 0) {
//                        jTabbedPaneAppSys.setEnabledAt(1, false);
//                    }
//
//                    /**
//                     * Week 2 *
//                     */
//                    st3.executeQuery("SELECT ACT_DATE,ACT_DEST,"
//                            + "ACT_DIST,ACT_ITM_PUR,LN_BUD_CODE,BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,"
//                            + "MSC_AMT,ACC_UNPROV_AMT,ACT_ITM_TOT FROM "
//                            + "[ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
//                            + "  where SERIAL = 'R' and concat(SERIAL,REF_NUM)='" + jLabelSerial.getText() + jTextRegNum.getText() + "'and "
//                            + "DOC_VER =(select max(DOC_VER) from ClaimsAppSysZvandiri.dbo.ClaimAppGenTab "
//                            + "where concat(SERIAL,REF_NUM)='" + jLabelSerial.getText() + jTextRegNum.getText() + "') and ACT_REC_STA='A' and PLAN_WK = 2"
//                            + "order by ACT_DATE");
//
//                    ResultSet r3 = st3.getResultSet();
//
//                    while (r3.next()) {
//                        modelWk2.insertRow(modelWk2.getRowCount(), new Object[]{r3.getString(1), r3.getString(2), r3.getString(3),
//                            r3.getString(4), r3.getString(5), r3.getString(6), r3.getString(7), r3.getString(8), r3.getString(9),
//                            r3.getString(10), r3.getString(11), r3.getString(12), r3.getString(13)});
//
//                    }
//
//                    if (modelWk2.getRowCount() == 0) {
//                        jTabbedPaneAppSys.setEnabledAt(2, false);
//                    }
//
//                    /**
//                     * Week 3 *
//                     */
//                    st4.executeQuery("SELECT ACT_DATE,ACT_DEST,"
//                            + "ACT_DIST,ACT_ITM_PUR,LN_BUD_CODE,BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,"
//                            + "MSC_AMT,ACC_UNPROV_AMT,ACT_ITM_TOT FROM "
//                            + "[ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
//                            + "  where SERIAL = 'R' and concat(SERIAL,REF_NUM)='" + jLabelSerial.getText() + jTextRegNum.getText() + "'and "
//                            + "DOC_VER =(select max(DOC_VER) from ClaimsAppSysZvandiri.dbo.ClaimAppGenTab "
//                            + "where concat(SERIAL,REF_NUM)='" + jLabelSerial.getText() + jTextRegNum.getText() + "') and ACT_REC_STA='A' and PLAN_WK = 3"
//                            + "order by ACT_DATE");
//
//                    ResultSet r4 = st4.getResultSet();
//
//                    while (r4.next()) {
//                        modelWk3.insertRow(modelWk3.getRowCount(), new Object[]{r4.getString(1), r4.getString(2), r4.getString(3),
//                            r4.getString(4), r4.getString(5), r4.getString(6), r4.getString(7), r4.getString(8), r4.getString(9),
//                            r4.getString(10), r4.getString(11), r4.getString(12), r4.getString(13)});
//
//                    }
//
//                    if (modelWk3.getRowCount() == 0) {
//                        jTabbedPaneAppSys.setEnabledAt(3, false);
//                    }
//
//                    /**
//                     * Week 4 *
//                     */
//                    st5.executeQuery("SELECT ACT_DATE,ACT_DEST,"
//                            + "ACT_DIST,ACT_ITM_PUR,LN_BUD_CODE,BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,"
//                            + "MSC_AMT,ACC_UNPROV_AMT,ACT_ITM_TOT FROM "
//                            + "[ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
//                            + "  where SERIAL = 'R' and concat(SERIAL,REF_NUM)='" + jLabelSerial.getText() + jTextRegNum.getText() + "'and "
//                            + "DOC_VER =(select max(DOC_VER) from ClaimsAppSysZvandiri.dbo.ClaimAppGenTab "
//                            + "where concat(SERIAL,REF_NUM)='" + jLabelSerial.getText() + jTextRegNum.getText() + "') and ACT_REC_STA='A' and PLAN_WK = 4"
//                            + "order by ACT_DATE");
//
//                    ResultSet r5 = st5.getResultSet();
//
//                    while (r5.next()) {
//                        modelWk4.insertRow(modelWk4.getRowCount(), new Object[]{r5.getString(1), r5.getString(2), r5.getString(3),
//                            r5.getString(4), r5.getString(5), r5.getString(6), r5.getString(7), r5.getString(8), r5.getString(9),
//                            r5.getString(10), r5.getString(11), r5.getString(12), r5.getString(13)});
//
//                    }
//
//                    if (modelWk4.getRowCount() == 0) {
//                        jTabbedPaneAppSys.setEnabledAt(4, false);
//                    }
//
//                    /**
//                     * Week 5 *
//                     */
//                    st6.executeQuery("SELECT ACT_DATE,ACT_DEST,"
//                            + "ACT_DIST,ACT_ITM_PUR,LN_BUD_CODE,BRK_AMT,LNC_AMT,DIN_AMT,INC_AMT,MSC_ACT,"
//                            + "MSC_AMT,ACC_UNPROV_AMT,ACT_ITM_TOT FROM "
//                            + "[ClaimsAppSysZvandiri].[dbo].[ClaimAppItmTab] "
//                            + "  where SERIAL = 'R' and concat(SERIAL,REF_NUM)='" + jLabelSerial.getText() + jTextRegNum.getText() + "'and "
//                            + "DOC_VER =(select max(DOC_VER) from ClaimsAppSysZvandiri.dbo.ClaimAppGenTab "
//                            + "where concat(SERIAL,REF_NUM)='" + jLabelSerial.getText() + jTextRegNum.getText() + "') and ACT_REC_STA='A' and PLAN_WK = 5"
//                            + "order by ACT_DATE");
//
//                    ResultSet r6 = st6.getResultSet();
//
//                    while (r6.next()) {
//                        modelWk5.insertRow(modelWk5.getRowCount(), new Object[]{r6.getString(1), r6.getString(2), r6.getString(3),
//                            r6.getString(4), r6.getString(5), r6.getString(6), r6.getString(7), r6.getString(8), r6.getString(9),
//                            r6.getString(10), r6.getString(11), r6.getString(12), r6.getString(13)});
//
//                    }
//
//                    if (modelWk5.getRowCount() == 0) {
//                        jTabbedPaneAppSys.setEnabledAt(5, false);
//                    }
//
//                    imgDisplay();
                } catch (Exception e) {
                    System.out.println(e);
                }
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
                    + "where concat(SERIAL,REF_NUM)='" + SearchRef + "' and ACT_REC_STA = 'A' "
                    + "and PLAN_WK=1 order by 1");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk1.insertRow(modelWk1.getRowCount(), new Object[]{r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22), r.getString(23)});

            }

            if (modelWk1.getRowCount() == 0) {
                jTabbedPaneAppSys.setEnabledAt(1, false);
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
                    + "where concat(SERIAL,REF_NUM)='" + SearchRef + "' and ACT_REC_STA = 'A' "
                    + "and PLAN_WK=2 order by 1");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk2.insertRow(modelWk2.getRowCount(), new Object[]{r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22), r.getString(23)});

            }

            if (modelWk2.getRowCount() == 0) {
                jTabbedPaneAppSys.setEnabledAt(2, false);
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
                    + "where concat(SERIAL,REF_NUM)='" + SearchRef + "' and ACT_REC_STA = 'A' "
                    + "and PLAN_WK=3 order by 1");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk3.insertRow(modelWk3.getRowCount(), new Object[]{r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22), r.getString(23)});

            }

            if (modelWk3.getRowCount() == 0) {
                jTabbedPaneAppSys.setEnabledAt(3, false);
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
                    + "where concat(SERIAL,REF_NUM)='" + SearchRef + "' and ACT_REC_STA = 'A' "
                    + "and PLAN_WK=4 order by 1");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk4.insertRow(modelWk4.getRowCount(), new Object[]{r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22), r.getString(23)});

            }

            if (modelWk4.getRowCount() == 0) {
                jTabbedPaneAppSys.setEnabledAt(4, false);
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
                    + "where concat(SERIAL,REF_NUM)='" + SearchRef + "' and ACT_REC_STA = 'A' "
                    + "and PLAN_WK=5 order by 1");

            ResultSet r = st.getResultSet();

            while (r.next()) {
                modelWk5.insertRow(modelWk5.getRowCount(), new Object[]{r.getString(5),
                    r.getString(6), r.getString(7), r.getString(8), r.getString(9), r.getString(10), r.getString(11), r.getString(12),
                    r.getString(13), r.getString(14), r.getString(15), r.getString(16), r.getString(17), r.getString(18), r.getString(19),
                    r.getString(20), r.getString(21), r.getString(22), r.getString(23)});

            }

            if (modelWk5.getRowCount() == 0) {
                jTabbedPaneAppSys.setEnabledAt(5, false);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void mainPageTotUpdate() {

        DecimalFormat numFormat;
        numFormat = new DecimalFormat("000.##");
        double breakfastsubtotal = 0;
        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            double breakfastamount = Double.parseDouble((String) jTableWk1Activities.getValueAt(i, 10));
            breakfastsubtotal += breakfastamount;
        }

        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            double breakfastamount = Double.parseDouble((String) jTableWk2Activities.getValueAt(i, 10));
            breakfastsubtotal += breakfastamount;
        }

        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            double breakfastamount = Double.parseDouble((String) jTableWk3Activities.getValueAt(i, 10));
            breakfastsubtotal += breakfastamount;
        }

        for (int i = 0; i < jTableWk4Activities.getRowCount(); i++) {
            double breakfastamount = Double.parseDouble((String) jTableWk4Activities.getValueAt(i, 10));
            breakfastsubtotal += breakfastamount;
        }

        for (int i = 0; i < jTableWk5Activities.getRowCount(); i++) {
            double breakfastamount = Double.parseDouble((String) jTableWk5Activities.getValueAt(i, 10));
            breakfastsubtotal += breakfastamount;
        }

        jLabelBreakFastSubTot.setText(String.format("%.2f", breakfastsubtotal));

        double lunchsubtotal = 0;
        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            double lunchamount = Double.parseDouble((String) jTableWk1Activities.getValueAt(i, 11));
            lunchsubtotal += lunchamount;
        }

        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            double lunchamount = Double.parseDouble((String) jTableWk2Activities.getValueAt(i, 11));
            lunchsubtotal += lunchamount;
        }

        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            double lunchamount = Double.parseDouble((String) jTableWk3Activities.getValueAt(i, 11));
            lunchsubtotal += lunchamount;
        }

        for (int i = 0; i < jTableWk4Activities.getRowCount(); i++) {
            double lunchamount = Double.parseDouble((String) jTableWk4Activities.getValueAt(i, 11));
            lunchsubtotal += lunchamount;
        }

        for (int i = 0; i < jTableWk5Activities.getRowCount(); i++) {
            double lunchamount = Double.parseDouble((String) jTableWk5Activities.getValueAt(i, 11));
            lunchsubtotal += lunchamount;
        }

        jLabelLunchSubTot.setText(String.format("%.2f", lunchsubtotal));

        double dinnersubtotal = 0;
        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            double dinneramount = Double.parseDouble((String) jTableWk1Activities.getValueAt(i, 12));
            dinnersubtotal += dinneramount;
        }

        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            double dinneramount = Double.parseDouble((String) jTableWk2Activities.getValueAt(i, 12));
            dinnersubtotal += dinneramount;
        }

        //jLabelDinnerSubTot.setText(Double.toString(dinnersubtotal));
        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            double dinneramount = Double.parseDouble((String) jTableWk3Activities.getValueAt(i, 12));
            dinnersubtotal += dinneramount;
        }

        for (int i = 0; i < jTableWk4Activities.getRowCount(); i++) {
            double dinneramount = Double.parseDouble((String) jTableWk4Activities.getValueAt(i, 12));
            dinnersubtotal += dinneramount;
        }

        for (int i = 0; i < jTableWk5Activities.getRowCount(); i++) {
            double dinneramount = Double.parseDouble((String) jTableWk5Activities.getValueAt(i, 12));
            dinnersubtotal += dinneramount;
        }

        jLabelDinnerSubTot.setText(String.format("%.2f", dinnersubtotal));

        double incidentalsubtotal = 0;
        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            double incidentalamount = Double.parseDouble((String) jTableWk1Activities.getValueAt(i, 13));
            incidentalsubtotal += incidentalamount;
        }

        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            double incidentalamount = Double.parseDouble((String) jTableWk2Activities.getValueAt(i, 13));
            incidentalsubtotal += incidentalamount;
        }

        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            double incidentalamount = Double.parseDouble((String) jTableWk3Activities.getValueAt(i, 13));
            incidentalsubtotal += incidentalamount;
        }

        for (int i = 0; i < jTableWk4Activities.getRowCount(); i++) {
            double incidentalamount = Double.parseDouble((String) jTableWk4Activities.getValueAt(i, 13));
            incidentalsubtotal += incidentalamount;
        }

        for (int i = 0; i < jTableWk5Activities.getRowCount(); i++) {
            double incidentalamount = Double.parseDouble((String) jTableWk5Activities.getValueAt(i, 13));
            incidentalsubtotal += incidentalamount;
        }

        jLabelIncidentalSubTot.setText(String.format("%.2f", incidentalsubtotal));

        double miscSubTot = 0;
        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            double miscamount = Double.parseDouble((String) jTableWk1Activities.getValueAt(i, 15));
            miscSubTot += miscamount;
        }

        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            double miscamount = Double.parseDouble((String) jTableWk2Activities.getValueAt(i, 15));
            miscSubTot += miscamount;
        }

        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            double miscamount = Double.parseDouble((String) jTableWk3Activities.getValueAt(i, 15));
            miscSubTot += miscamount;
        }

        for (int i = 0; i < jTableWk4Activities.getRowCount(); i++) {
            double miscamount = Double.parseDouble((String) jTableWk4Activities.getValueAt(i, 15));
            miscSubTot += miscamount;
        }

        for (int i = 0; i < jTableWk5Activities.getRowCount(); i++) {
            double miscamount = Double.parseDouble((String) jTableWk5Activities.getValueAt(i, 15));
            miscSubTot += miscamount;
        }

        jLabelMiscSubTot.setText(String.format("%.2f", miscSubTot));

        double unprovedSubTot = 0;
        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            double unprovedamount = Double.parseDouble((String) jTableWk1Activities.getValueAt(i, 16));
            unprovedSubTot += unprovedamount;
        }

        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            double unprovedamount = Double.parseDouble((String) jTableWk2Activities.getValueAt(i, 16));
            unprovedSubTot += unprovedamount;
        }

        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            double unprovedamount = Double.parseDouble((String) jTableWk3Activities.getValueAt(i, 16));
            unprovedSubTot += unprovedamount;
        }

        for (int i = 0; i < jTableWk4Activities.getRowCount(); i++) {
            double unprovedamount = Double.parseDouble((String) jTableWk4Activities.getValueAt(i, 16));
            unprovedSubTot += unprovedamount;
        }

        for (int i = 0; i < jTableWk5Activities.getRowCount(); i++) {
            double unprovedamount = Double.parseDouble((String) jTableWk5Activities.getValueAt(i, 16));
            unprovedSubTot += unprovedamount;
        }
        jLabelAccUnprovedSubTot.setText(String.format("%.2f", unprovedSubTot));

        double provedSubTot = 0;
        for (int i = 0; i < jTableWk1Activities.getRowCount(); i++) {
            double provedamount = Double.parseDouble((String) jTableWk1Activities.getValueAt(i, 17));
            provedSubTot += provedamount;
        }

        for (int i = 0; i < jTableWk2Activities.getRowCount(); i++) {
            double provedamount = Double.parseDouble((String) jTableWk2Activities.getValueAt(i, 17));
            provedSubTot += provedamount;
        }

        for (int i = 0; i < jTableWk3Activities.getRowCount(); i++) {
            double provedamount = Double.parseDouble((String) jTableWk3Activities.getValueAt(i, 17));
            provedSubTot += provedamount;
        }

        for (int i = 0; i < jTableWk4Activities.getRowCount(); i++) {
            double provedamount = Double.parseDouble((String) jTableWk4Activities.getValueAt(i, 17));
            provedSubTot += provedamount;
        }

        for (int i = 0; i < jTableWk5Activities.getRowCount(); i++) {
            double provedamount = Double.parseDouble((String) jTableWk5Activities.getValueAt(i, 17));
            provedSubTot += provedamount;
        }

        jLabelAccProvedSubTot.setText(String.format("%.2f", provedSubTot));

        double allTotal = unprovedSubTot + miscSubTot + incidentalsubtotal
                + dinnersubtotal + lunchsubtotal + breakfastsubtotal + provedSubTot;
        numFormat.format(allTotal);

        jLabelAppTotReqCost.setText(String.format("%.2f", allTotal));

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code.EMP The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupPartAllowance = new javax.swing.ButtonGroup();
        jDialogPleaseWait = new javax.swing.JDialog();
        jDialogPleaseWaitReport = new javax.swing.JDialog();
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
        jLabelLunchSubTot = new javax.swing.JLabel();
        jLabelDinnerSubTot = new javax.swing.JLabel();
        jLabelAllReq = new javax.swing.JLabel();
        jLabelBreakFastSub = new javax.swing.JLabel();
        jLabelBreakFastSubTot = new javax.swing.JLabel();
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
        jLabelReqEmpNum = new javax.swing.JLabel();
        jLabelRegDate = new javax.swing.JLabel();
        jLabelRegYear = new javax.swing.JLabel();
        jLabelSerial = new javax.swing.JLabel();
        jLabelEmp = new javax.swing.JLabel();
        jLabelComments = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaComments = new javax.swing.JTextArea();
        jLabelAuthoriser = new javax.swing.JLabel();
        jLabelAuthNam1 = new javax.swing.JLabel();
        jLabelAuthNam2 = new javax.swing.JLabel();
        jTextRegNum = new javax.swing.JTextField();
        jPanelStatusView = new javax.swing.JPanel();
        jLabelStatusView = new javax.swing.JLabel();
        jLabelReject = new javax.swing.JLabel();
        jLabelRejectComments = new javax.swing.JLabel();
        jRadioPartAllow = new javax.swing.JRadioButton();
        jRadioPerDiem = new javax.swing.JRadioButton();
        jButtonGenerateReport = new javax.swing.JButton();
        jPanelRequestWk1 = new javax.swing.JPanel();
        jLabelLogo2 = new javax.swing.JLabel();
        jLabelHeaderLine1 = new javax.swing.JLabel();
        jLabelLineDate2 = new javax.swing.JLabel();
        jLabelLineTime2 = new javax.swing.JLabel();
        jLabellogged1 = new javax.swing.JLabel();
        jLabelLinLogNam2 = new javax.swing.JLabel();
        jLabelLinSerial1 = new javax.swing.JLabel();
        jLabelLinRegNum1 = new javax.swing.JLabel();
        jLabelNum2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPaneWk8 = new javax.swing.JScrollPane();
        jTableWk1Activities = new javax.swing.JTable();
        jPanelRequestWk2 = new javax.swing.JPanel();
        jLabelLogo4 = new javax.swing.JLabel();
        jLabelHeaderLine3 = new javax.swing.JLabel();
        jLabelLineDate3 = new javax.swing.JLabel();
        jLabelLineTime3 = new javax.swing.JLabel();
        jLabellogged3 = new javax.swing.JLabel();
        jLabelLinLogNam3 = new javax.swing.JLabel();
        jLabelLinSerial2 = new javax.swing.JLabel();
        jLabelLinRegNum2 = new javax.swing.JLabel();
        jLabelNum3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPaneWk9 = new javax.swing.JScrollPane();
        jTableWk2Activities = new javax.swing.JTable();
        jPanelRequestWk3 = new javax.swing.JPanel();
        jLabelLogo5 = new javax.swing.JLabel();
        jLabelHeaderLine4 = new javax.swing.JLabel();
        jLabelLineDate4 = new javax.swing.JLabel();
        jLabelLineTime4 = new javax.swing.JLabel();
        jLabellogged4 = new javax.swing.JLabel();
        jLabelLinLogNam4 = new javax.swing.JLabel();
        jLabelLinSerial3 = new javax.swing.JLabel();
        jLabelLinRegNum3 = new javax.swing.JLabel();
        jLabelNum4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPaneWk10 = new javax.swing.JScrollPane();
        jTableWk3Activities = new javax.swing.JTable();
        jPanelRequestWk4 = new javax.swing.JPanel();
        jLabelLogo6 = new javax.swing.JLabel();
        jLabelHeaderLine5 = new javax.swing.JLabel();
        jLabelLineDate5 = new javax.swing.JLabel();
        jLabelLineTime5 = new javax.swing.JLabel();
        jLabellogged5 = new javax.swing.JLabel();
        jLabelLinLogNam5 = new javax.swing.JLabel();
        jLabelLinSerial4 = new javax.swing.JLabel();
        jLabelLinRegNum4 = new javax.swing.JLabel();
        jLabelNum5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPaneWk11 = new javax.swing.JScrollPane();
        jTableWk4Activities = new javax.swing.JTable();
        jPanelRequestWk5 = new javax.swing.JPanel();
        jLabelLogo7 = new javax.swing.JLabel();
        jLabelHeaderLine6 = new javax.swing.JLabel();
        jLabelLineDate6 = new javax.swing.JLabel();
        jLabelLineTime6 = new javax.swing.JLabel();
        jLabellogged6 = new javax.swing.JLabel();
        jLabelLinLogNam6 = new javax.swing.JLabel();
        jLabelLinSerial5 = new javax.swing.JLabel();
        jLabelLinRegNum5 = new javax.swing.JLabel();
        jLabelNum6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPaneWk12 = new javax.swing.JScrollPane();
        jTableWk5Activities = new javax.swing.JTable();
        jPanelDocAttach = new javax.swing.JPanel();
        jLabelLogo3 = new javax.swing.JLabel();
        jLabelHeaderLine2 = new javax.swing.JLabel();
        jLabelLineDate1 = new javax.swing.JLabel();
        jLabelLineTime1 = new javax.swing.JLabel();
        jLabellogged2 = new javax.swing.JLabel();
        jLabelLinLogNam1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel9 = new javax.swing.JPanel();
        jLabelImg = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
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
        jSeparator22 = new javax.swing.JPopupMenu.Separator();
        jMenuItemUserProfUpd = new javax.swing.JMenuItem();

        jDialogPleaseWait.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jDialogPleaseWait.setTitle("                    Retrieving requested information.  Please Wait.");
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

        jDialogPleaseWaitReport.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jDialogPleaseWaitReport.setTitle("                    Generating report.  Please Wait.....");
        jDialogPleaseWaitReport.setAlwaysOnTop(true);
        jDialogPleaseWaitReport.setBackground(new java.awt.Color(51, 255, 51));
        jDialogPleaseWaitReport.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jDialogPleaseWaitReport.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jDialogPleaseWaitReport.setForeground(new java.awt.Color(255, 0, 0));
        jDialogPleaseWaitReport.setIconImages(null);
        jDialogPleaseWaitReport.setLocation(new java.awt.Point(650, 375));
        jDialogPleaseWaitReport.setMinimumSize(new java.awt.Dimension(500, 50));
        jDialogPleaseWaitReport.setResizable(false);

        javax.swing.GroupLayout jDialogPleaseWaitReportLayout = new javax.swing.GroupLayout(jDialogPleaseWaitReport.getContentPane());
        jDialogPleaseWaitReport.getContentPane().setLayout(jDialogPleaseWaitReportLayout);
        jDialogPleaseWaitReportLayout.setHorizontalGroup(
            jDialogPleaseWaitReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 453, Short.MAX_VALUE)
        );
        jDialogPleaseWaitReportLayout.setVerticalGroup(
            jDialogPleaseWaitReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PERDIEM REQUEST -VIEW STATUS");
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        jTabbedPaneAppSys.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPaneAppSys.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTabbedPaneAppSysFocusGained(evt);
            }
        });

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
        jLabelLogo.setBounds(10, 5, 220, 115);

        jLabelHeaderGen.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jPanelGen.add(jLabelHeaderGen);
        jLabelHeaderGen.setBounds(290, 40, 680, 40);

        jLabelGenDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelGen.add(jLabelGenDate);
        jLabelGenDate.setBounds(1060, 0, 110, 30);

        jLabelGenTime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelGen.add(jLabelGenTime);
        jLabelGenTime.setBounds(1230, 0, 80, 30);

        jLabelGenLogNam1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelGenLogNam1.setText("Logged In");
        jPanelGen.add(jLabelGenLogNam1);
        jLabelGenLogNam1.setBounds(1060, 40, 100, 30);

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

        jLabelMainPurpose.setText("Activity Main Purpose");
        jPanelGen.add(jLabelMainPurpose);
        jLabelMainPurpose.setBounds(30, 370, 130, 30);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPanel1FocusGained(evt);
            }
        });
        jPanel1.setLayout(null);

        jLabelIncidentalSub.setText("Incidental");
        jPanel1.add(jLabelIncidentalSub);
        jLabelIncidentalSub.setBounds(10, 90, 60, 25);

        jLabelIncidentalSubTot.setText("0.00");
        jPanel1.add(jLabelIncidentalSubTot);
        jLabelIncidentalSubTot.setBounds(100, 90, 50, 25);

        jLabelLunchSub.setText("Lunch");
        jPanel1.add(jLabelLunchSub);
        jLabelLunchSub.setBounds(10, 30, 60, 25);

        jLabelDinnerSub.setText("Dinner");
        jPanel1.add(jLabelDinnerSub);
        jLabelDinnerSub.setBounds(10, 60, 60, 25);

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

        jLabelLunchSubTot.setText("0.00");
        jPanel1.add(jLabelLunchSubTot);
        jLabelLunchSubTot.setBounds(100, 30, 50, 25);

        jLabelDinnerSubTot.setText("0.00");
        jPanel1.add(jLabelDinnerSubTot);
        jLabelDinnerSubTot.setBounds(100, 60, 50, 25);

        jLabelAllReq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAllReq.setForeground(new java.awt.Color(0, 102, 0));
        jLabelAllReq.setText("Req");
        jPanel1.add(jLabelAllReq);
        jLabelAllReq.setBounds(100, 5, 22, 25);

        jLabelBreakFastSub.setText("Breakfast");
        jPanel1.add(jLabelBreakFastSub);
        jLabelBreakFastSub.setBounds(10, 110, 60, 25);

        jLabelBreakFastSubTot.setText("0.00");
        jPanel1.add(jLabelBreakFastSubTot);
        jLabelBreakFastSubTot.setBounds(100, 110, 50, 25);

        jPanelGen.add(jPanel1);
        jPanel1.setBounds(10, 430, 250, 150);

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
        jPanel3.setBounds(360, 430, 250, 150);

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
        jPanel4.setBounds(700, 430, 250, 150);

        jLabelAppTotReqCost.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelAppTotReqCost.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAppTotReqCost.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelAppTotReqCost.setText("0.00");
        jPanelGen.add(jLabelAppTotReqCost);
        jLabelAppTotReqCost.setBounds(1270, 570, 70, 30);

        jLabelAppTotReq.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelAppTotReq.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAppTotReq.setText("Total ");
        jPanelGen.add(jLabelAppTotReq);
        jLabelAppTotReq.setBounds(1090, 570, 140, 30);

        jLabelOffice.setText("Office");
        jPanelGen.add(jLabelOffice);
        jLabelOffice.setBounds(580, 230, 70, 30);

        jLabelReqAccNum.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelReqAccNum);
        jLabelReqAccNum.setBounds(710, 260, 130, 30);

        jLabelACC.setText("Account No.");
        jPanelGen.add(jLabelACC);
        jLabelACC.setBounds(580, 260, 80, 30);

        jLabelNum.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNum.setText("Reference No.");
        jPanelGen.add(jLabelNum);
        jLabelNum.setBounds(310, 130, 100, 30);

        jLabelActMainPurpose.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelActMainPurpose);
        jLabelActMainPurpose.setBounds(170, 370, 750, 30);

        jLabelReqProvince.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelReqProvince);
        jLabelReqProvince.setBounds(130, 230, 210, 30);

        jLabelReqOffice.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelReqOffice);
        jLabelReqOffice.setBounds(710, 230, 210, 30);

        jLabelReqEmpNum.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelReqEmpNum);
        jLabelReqEmpNum.setBounds(130, 200, 60, 30);

        jLabelRegDate.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelRegDate);
        jLabelRegDate.setBounds(560, 130, 160, 30);
        jPanelGen.add(jLabelRegYear);
        jLabelRegYear.setBounds(700, 130, 50, 30);

        jLabelSerial.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSerial.setText("R");
        jPanelGen.add(jLabelSerial);
        jLabelSerial.setBounds(420, 130, 30, 30);
        jPanelGen.add(jLabelEmp);
        jLabelEmp.setBounds(1110, 70, 50, 20);

        jLabelComments.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelComments.setText("Comments");
        jPanelGen.add(jLabelComments);
        jLabelComments.setBounds(1080, 430, 170, 20);

        jTextAreaComments.setColumns(20);
        jTextAreaComments.setRows(5);
        jScrollPane2.setViewportView(jTextAreaComments);

        jPanelGen.add(jScrollPane2);
        jScrollPane2.setBounds(1080, 450, 260, 110);

        jLabelAuthoriser.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelAuthoriser.setText("Authoriser/s");
        jPanelGen.add(jLabelAuthoriser);
        jLabelAuthoriser.setBounds(1070, 340, 160, 25);

        jLabelAuthNam1.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAuthNam1);
        jLabelAuthNam1.setBounds(1070, 370, 270, 25);

        jLabelAuthNam2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelGen.add(jLabelAuthNam2);
        jLabelAuthNam2.setBounds(1070, 400, 230, 25);

        jTextRegNum.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextRegNumFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextRegNumFocusLost(evt);
            }
        });
        jTextRegNum.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextRegNumMouseClicked(evt);
            }
        });
        jTextRegNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextRegNumActionPerformed(evt);
            }
        });
        jTextRegNum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextRegNumKeyTyped(evt);
            }
        });
        jPanelGen.add(jTextRegNum);
        jTextRegNum.setBounds(470, 130, 50, 30);

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

        jLabelReject.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelReject.setForeground(new java.awt.Color(255, 255, 255));
        jLabelReject.setText("Reasons for Rejection");
        jPanelGen.add(jLabelReject);
        jLabelReject.setBounds(10, 590, 170, 25);

        jLabelRejectComments.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelRejectComments.setForeground(new java.awt.Color(204, 0, 0));
        jPanelGen.add(jLabelRejectComments);
        jLabelRejectComments.setBounds(170, 590, 780, 25);

        jRadioPartAllow.setBackground(new java.awt.Color(255, 51, 0));
        jRadioPartAllow.setForeground(new java.awt.Color(255, 255, 255));
        jRadioPartAllow.setText("Participants Allowances");
        jRadioPartAllow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioPartAllowActionPerformed(evt);
            }
        });
        jPanelGen.add(jRadioPartAllow);
        jRadioPartAllow.setBounds(130, 130, 170, 30);

        jRadioPerDiem.setBackground(new java.awt.Color(255, 255, 255));
        jRadioPerDiem.setText("Per Diem");
        jRadioPerDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioPerDiemActionPerformed(evt);
            }
        });
        jPanelGen.add(jRadioPerDiem);
        jRadioPerDiem.setBounds(10, 130, 100, 30);

        jButtonGenerateReport.setText("HOD Approved - Click to Generate Report");
        jButtonGenerateReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenerateReportActionPerformed(evt);
            }
        });
        jPanelGen.add(jButtonGenerateReport);
        jButtonGenerateReport.setBounds(970, 120, 270, 30);

        jTabbedPaneAppSys.addTab("User and Accounting Details", jPanelGen);

        jPanelRequestWk1.setBackground(new java.awt.Color(0, 102, 102));
        jPanelRequestWk1.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jPanelRequestWk1.setPreferredSize(new java.awt.Dimension(1280, 677));
        jPanelRequestWk1.setLayout(null);

        jLabelLogo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelRequestWk1.add(jLabelLogo2);
        jLabelLogo2.setBounds(10, 10, 220, 115);

        jLabelHeaderLine1.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine1.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelRequestWk1.add(jLabelHeaderLine1);
        jLabelHeaderLine1.setBounds(400, 40, 610, 40);

        jLabelLineDate2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelRequestWk1.add(jLabelLineDate2);
        jLabelLineDate2.setBounds(1070, 0, 110, 30);

        jLabelLineTime2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelRequestWk1.add(jLabelLineTime2);
        jLabelLineTime2.setBounds(1240, 0, 80, 30);

        jLabellogged1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged1.setText("Logged In");
        jPanelRequestWk1.add(jLabellogged1);
        jLabellogged1.setBounds(1070, 40, 80, 30);

        jLabelLinLogNam2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinLogNam2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk1.add(jLabelLinLogNam2);
        jLabelLinLogNam2.setBounds(1160, 40, 200, 30);

        jLabelLinSerial1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinSerial1.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk1.add(jLabelLinSerial1);
        jLabelLinSerial1.setBounds(1240, 70, 20, 30);

        jLabelLinRegNum1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinRegNum1.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk1.add(jLabelLinRegNum1);
        jLabelLinRegNum1.setBounds(1270, 70, 70, 30);

        jLabelNum2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNum2.setText("Reference No.");
        jPanelRequestWk1.add(jLabelNum2);
        jLabelNum2.setBounds(1070, 70, 90, 30);

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Week One");
        jPanelRequestWk1.add(jLabel1);
        jLabel1.setBounds(630, 80, 120, 25);

        jTableWk1Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Account Code", "Donor", "Project Code GL", "Project Code Program", "Project Name Program", "Budget Line", "Budget Code", "Site to Visit", "Activity", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Line Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk8.setViewportView(jTableWk1Activities);

        jPanelRequestWk1.add(jScrollPaneWk8);
        jScrollPaneWk8.setBounds(10, 170, 1340, 470);

        jTabbedPaneAppSys.addTab("Perdiem Request - Week 1", jPanelRequestWk1);

        jPanelRequestWk2.setBackground(new java.awt.Color(0, 102, 102));
        jPanelRequestWk2.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jPanelRequestWk2.setPreferredSize(new java.awt.Dimension(1280, 677));
        jPanelRequestWk2.setLayout(null);

        jLabelLogo4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelRequestWk2.add(jLabelLogo4);
        jLabelLogo4.setBounds(10, 10, 220, 115);

        jLabelHeaderLine3.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine3.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelRequestWk2.add(jLabelHeaderLine3);
        jLabelHeaderLine3.setBounds(400, 40, 610, 40);

        jLabelLineDate3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelRequestWk2.add(jLabelLineDate3);
        jLabelLineDate3.setBounds(1070, 0, 110, 30);

        jLabelLineTime3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelRequestWk2.add(jLabelLineTime3);
        jLabelLineTime3.setBounds(1240, 0, 80, 30);

        jLabellogged3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged3.setText("Logged In");
        jPanelRequestWk2.add(jLabellogged3);
        jLabellogged3.setBounds(1070, 40, 80, 30);

        jLabelLinLogNam3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinLogNam3.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk2.add(jLabelLinLogNam3);
        jLabelLinLogNam3.setBounds(1160, 40, 200, 30);

        jLabelLinSerial2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinSerial2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk2.add(jLabelLinSerial2);
        jLabelLinSerial2.setBounds(1240, 70, 20, 30);

        jLabelLinRegNum2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinRegNum2.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk2.add(jLabelLinRegNum2);
        jLabelLinRegNum2.setBounds(1270, 70, 70, 30);

        jLabelNum3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNum3.setText("Reference No.");
        jPanelRequestWk2.add(jLabelNum3);
        jLabelNum3.setBounds(1070, 70, 90, 30);

        jLabel2.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Week Two");
        jPanelRequestWk2.add(jLabel2);
        jLabel2.setBounds(630, 80, 120, 25);

        jTableWk2Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Account Code", "Donor", "Project Code GL", "Project Code Program", "Project Name Program", "Budget Line", "Budget Code", "Site to Visit", "Activity", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Line Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk9.setViewportView(jTableWk2Activities);

        jPanelRequestWk2.add(jScrollPaneWk9);
        jScrollPaneWk9.setBounds(10, 170, 1340, 470);

        jTabbedPaneAppSys.addTab("Perdiem Request - Week 2", jPanelRequestWk2);

        jPanelRequestWk3.setBackground(new java.awt.Color(0, 102, 102));
        jPanelRequestWk3.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jPanelRequestWk3.setPreferredSize(new java.awt.Dimension(1280, 677));
        jPanelRequestWk3.setLayout(null);

        jLabelLogo5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelRequestWk3.add(jLabelLogo5);
        jLabelLogo5.setBounds(10, 10, 220, 115);

        jLabelHeaderLine4.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine4.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelRequestWk3.add(jLabelHeaderLine4);
        jLabelHeaderLine4.setBounds(400, 40, 610, 40);

        jLabelLineDate4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelRequestWk3.add(jLabelLineDate4);
        jLabelLineDate4.setBounds(1070, 0, 110, 30);

        jLabelLineTime4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelRequestWk3.add(jLabelLineTime4);
        jLabelLineTime4.setBounds(1240, 0, 80, 30);

        jLabellogged4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged4.setText("Logged In");
        jPanelRequestWk3.add(jLabellogged4);
        jLabellogged4.setBounds(1070, 40, 80, 30);

        jLabelLinLogNam4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinLogNam4.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk3.add(jLabelLinLogNam4);
        jLabelLinLogNam4.setBounds(1160, 40, 200, 30);

        jLabelLinSerial3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinSerial3.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk3.add(jLabelLinSerial3);
        jLabelLinSerial3.setBounds(1240, 70, 20, 30);

        jLabelLinRegNum3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinRegNum3.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk3.add(jLabelLinRegNum3);
        jLabelLinRegNum3.setBounds(1270, 70, 70, 30);

        jLabelNum4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNum4.setText("Reference No.");
        jPanelRequestWk3.add(jLabelNum4);
        jLabelNum4.setBounds(1070, 70, 90, 30);

        jLabel3.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Week Three");
        jPanelRequestWk3.add(jLabel3);
        jLabel3.setBounds(630, 80, 120, 25);

        jTableWk3Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Account Code", "Donor", "Project Code GL", "Project Code Program", "Project Name Program", "Budget Line", "Budget Code", "Site to Visit", "Activity", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Line Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk10.setViewportView(jTableWk3Activities);

        jPanelRequestWk3.add(jScrollPaneWk10);
        jScrollPaneWk10.setBounds(10, 170, 1340, 470);

        jTabbedPaneAppSys.addTab("Perdiem Request - Week 3", jPanelRequestWk3);

        jPanelRequestWk4.setBackground(new java.awt.Color(0, 102, 102));
        jPanelRequestWk4.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jPanelRequestWk4.setPreferredSize(new java.awt.Dimension(1280, 677));
        jPanelRequestWk4.setLayout(null);

        jLabelLogo6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelRequestWk4.add(jLabelLogo6);
        jLabelLogo6.setBounds(10, 10, 220, 115);

        jLabelHeaderLine5.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine5.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelRequestWk4.add(jLabelHeaderLine5);
        jLabelHeaderLine5.setBounds(400, 40, 610, 40);

        jLabelLineDate5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelRequestWk4.add(jLabelLineDate5);
        jLabelLineDate5.setBounds(1070, 0, 110, 30);

        jLabelLineTime5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelRequestWk4.add(jLabelLineTime5);
        jLabelLineTime5.setBounds(1240, 0, 80, 30);

        jLabellogged5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged5.setText("Logged In");
        jPanelRequestWk4.add(jLabellogged5);
        jLabellogged5.setBounds(1070, 40, 80, 30);

        jLabelLinLogNam5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinLogNam5.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk4.add(jLabelLinLogNam5);
        jLabelLinLogNam5.setBounds(1160, 40, 200, 30);

        jLabelLinSerial4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinSerial4.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk4.add(jLabelLinSerial4);
        jLabelLinSerial4.setBounds(1240, 70, 20, 30);

        jLabelLinRegNum4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinRegNum4.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk4.add(jLabelLinRegNum4);
        jLabelLinRegNum4.setBounds(1270, 70, 70, 30);

        jLabelNum5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNum5.setText("Reference No.");
        jPanelRequestWk4.add(jLabelNum5);
        jLabelNum5.setBounds(1070, 70, 90, 30);

        jLabel4.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Week Four");
        jPanelRequestWk4.add(jLabel4);
        jLabel4.setBounds(630, 80, 120, 25);

        jTableWk4Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Account Code", "Donor", "Project Code GL", "Project Code Program", "Project Name Program", "Budget Line", "Budget Code", "Site to Visit", "Activity", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Line Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk11.setViewportView(jTableWk4Activities);

        jPanelRequestWk4.add(jScrollPaneWk11);
        jScrollPaneWk11.setBounds(10, 170, 1340, 470);

        jTabbedPaneAppSys.addTab("Perdiem Request - Week 4", jPanelRequestWk4);

        jPanelRequestWk5.setBackground(new java.awt.Color(0, 102, 102));
        jPanelRequestWk5.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jPanelRequestWk5.setPreferredSize(new java.awt.Dimension(1280, 677));
        jPanelRequestWk5.setLayout(null);

        jLabelLogo7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelRequestWk5.add(jLabelLogo7);
        jLabelLogo7.setBounds(10, 10, 220, 115);

        jLabelHeaderLine6.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine6.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelRequestWk5.add(jLabelHeaderLine6);
        jLabelHeaderLine6.setBounds(400, 40, 610, 40);

        jLabelLineDate6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelRequestWk5.add(jLabelLineDate6);
        jLabelLineDate6.setBounds(1070, 0, 110, 30);

        jLabelLineTime6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanelRequestWk5.add(jLabelLineTime6);
        jLabelLineTime6.setBounds(1240, 0, 80, 30);

        jLabellogged6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged6.setText("Logged In");
        jPanelRequestWk5.add(jLabellogged6);
        jLabellogged6.setBounds(1070, 40, 80, 30);

        jLabelLinLogNam6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinLogNam6.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk5.add(jLabelLinLogNam6);
        jLabelLinLogNam6.setBounds(1160, 40, 200, 30);

        jLabelLinSerial5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinSerial5.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk5.add(jLabelLinSerial5);
        jLabelLinSerial5.setBounds(1240, 70, 20, 30);

        jLabelLinRegNum5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinRegNum5.setForeground(new java.awt.Color(255, 255, 255));
        jPanelRequestWk5.add(jLabelLinRegNum5);
        jLabelLinRegNum5.setBounds(1270, 70, 70, 30);

        jLabelNum6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelNum6.setText("Reference No.");
        jPanelRequestWk5.add(jLabelNum6);
        jLabelNum6.setBounds(1070, 70, 90, 30);

        jLabel5.setFont(new java.awt.Font("Tahoma", 3, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Week Five");
        jPanelRequestWk5.add(jLabel5);
        jLabel5.setBounds(630, 80, 120, 25);

        jTableWk5Activities.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Account Code", "Donor", "Project Code GL", "Project Code Program", "Project Name Program", "Budget Line", "Budget Code", "Site to Visit", "Activity", "Breakfast", "Lunch", "Dinner", "Incidental", "Misc Desc", "Misc Amt", "Unproved Acc", "Proved Acc", "Line Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneWk12.setViewportView(jTableWk5Activities);

        jPanelRequestWk5.add(jScrollPaneWk12);
        jScrollPaneWk12.setBounds(10, 170, 1340, 470);

        jTabbedPaneAppSys.addTab("Perdiem Request - Week 5", jPanelRequestWk5);

        jPanelDocAttach.setBackground(new java.awt.Color(204, 204, 204));
        jPanelDocAttach.setLayout(null);

        jLabelLogo3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/COYLogo.jpg"))); // NOI18N
        jPanelDocAttach.add(jLabelLogo3);
        jLabelLogo3.setBounds(10, 10, 220, 100);

        jLabelHeaderLine2.setFont(new java.awt.Font("Times New Roman", 1, 34)); // NOI18N
        jLabelHeaderLine2.setText("TRAVEL AND SUBSISTENCE CLAIM");
        jPanelDocAttach.add(jLabelHeaderLine2);
        jLabelHeaderLine2.setBounds(350, 40, 610, 40);

        jLabelLineDate1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineDate1.setText("29 November 2018");
        jPanelDocAttach.add(jLabelLineDate1);
        jLabelLineDate1.setBounds(1090, 0, 110, 30);

        jLabelLineTime1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLineTime1.setText("15:20:30");
        jPanelDocAttach.add(jLabelLineTime1);
        jLabelLineTime1.setBounds(1260, 0, 80, 30);

        jLabellogged2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabellogged2.setText("Logged In");
        jPanelDocAttach.add(jLabellogged2);
        jLabellogged2.setBounds(1090, 40, 80, 30);

        jLabelLinLogNam1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelLinLogNam1.setText("User Name");
        jPanelDocAttach.add(jLabelLinLogNam1);
        jLabelLinLogNam1.setBounds(1190, 40, 160, 30);

        jScrollPane3.setFocusable(false);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(321, 321, 321)
                .addComponent(jLabelImg, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
                .addContainerGap(425, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabelImg, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                .addGap(0, 196, Short.MAX_VALUE))
        );

        jScrollPane3.setViewportView(jPanel9);

        jPanelDocAttach.add(jScrollPane3);
        jScrollPane3.setBounds(150, 115, 1200, 530);

        jButton1.setBackground(new java.awt.Color(0, 102, 0));
        jButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("<html><center>Print </center> Attachment</html>");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanelDocAttach.add(jButton1);
        jButton1.setBounds(2, 115, 140, 50);

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
            .addComponent(jTabbedPaneAppSys, javax.swing.GroupLayout.PREFERRED_SIZE, 1500, javax.swing.GroupLayout.PREFERRED_SIZE)
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


    private void jMenuPlanApprovalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuPlanApprovalActionPerformed

    }//GEN-LAST:event_jMenuPlanApprovalActionPerformed

    private void jTextRegNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextRegNumActionPerformed
        if ((!(jRadioPerDiem.isSelected())) && (!(jRadioPartAllow.isSelected()))) {

            JOptionPane.showMessageDialog(this, "Please select type of transaction to view");
            jTextRegNum.requestFocusInWindow();
            jTextRegNum.setFocusable(true);
        } else if (jRadioPerDiem.isSelected()) {
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

            jLabelAuthoriser.setVisible(false);
//        jLabelAuthNam.setVisible(false);
//        jLabelAuthAllNam.setVisible(false);
            jLabelStatusView.setVisible(false);
            jPanelStatusView.setVisible(false);
            jLabelRegDate.setText("");
            jLabelReqEmpNum.setText("");
            jLabelEmpNam.setText("");
            jLabelEmpTitle.setText("");
            jLabelReqProvince.setText("");
            jLabelReqOffice.setText("");
            jLabelBankName.setText("");
            jLabelReqAccNum.setText("");
            jTabbedPaneAppSys.setEnabledAt(1, true);
            jTabbedPaneAppSys.setEnabledAt(2, true);
            jTabbedPaneAppSys.setEnabledAt(3, true);
            jLabelActMainPurpose.setText("");
            jLabelImg.setIcon(null);
            jLabelReject.setVisible(false);
            jLabelRejectComments.setVisible(false);
            SearchRef = jLabelSerial.getText() + jTextRegNum.getText();
            fetchGenData();
            fetchdataWk1();
            fetchdataWk2();
            fetchdataWk3();
            fetchdataWk4();
            fetchdataWk5();
            imgDisplay();
            mainPageTotUpdate();
            System.out.println("hhjhd " + SearchRef);
        }

    }//GEN-LAST:event_jTextRegNumActionPerformed

    private void jTextRegNumFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextRegNumFocusLost

    }//GEN-LAST:event_jTextRegNumFocusLost

    private void jTabbedPaneAppSysFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTabbedPaneAppSysFocusGained
        jTextAreaComments.requestFocusInWindow();
        jTextAreaComments.setFocusable(true);
    }//GEN-LAST:event_jTabbedPaneAppSysFocusGained

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Requsition");

        job.setPrintable(new Printable() {
            public int print(Graphics pg, PageFormat pf, int pageNum) {

                if (pageNum > 0) {
                    return Printable.NO_SUCH_PAGE;
                }
                Graphics2D g2 = (Graphics2D) pg;
                g2.translate(pf.getImageableX(), pf.getImageableY());
                g2.scale(1, 1.2);

                jLabelImg.paint(g2);
                return Printable.PAGE_EXISTS;

            }
        });
        boolean ok = job.printDialog();
        if (ok)
            try {
            job.print();
        } catch (PrinterException ex) {

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextRegNumFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextRegNumFocusGained

    }//GEN-LAST:event_jTextRegNumFocusGained

    private void jTextRegNumMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextRegNumMouseClicked

    }//GEN-LAST:event_jTextRegNumMouseClicked

    private void jTextRegNumKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextRegNumKeyTyped

    }//GEN-LAST:event_jTextRegNumKeyTyped

    private void jRadioPerDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioPerDiemActionPerformed
        jLabelSerial.setText("R");
        jRadioPartAllow.setSelected(false);
        jLabelHeaderGen.setText("TRAVEL AND SUBSISTENCE CLAIM");
    }//GEN-LAST:event_jRadioPerDiemActionPerformed

    private void jRadioPartAllowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioPartAllowActionPerformed
        jLabelSerial.setText("MA");
        jRadioPerDiem.setSelected(false);
//        jLabelHeaderGen.setText("PARTICIPANTS ALLOWANCES CLAIM");
    }//GEN-LAST:event_jRadioPartAllowActionPerformed

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

    private void jButtonGenerateReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenerateReportActionPerformed
        jDialogPleaseWaitReport.setVisible(true);
        generateView();
        jDialogPleaseWaitReport.setVisible(false);
        setState(JFrame.ICONIFIED);
    }//GEN-LAST:event_jButtonGenerateReportActionPerformed

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
            java.util.logging.Logger.getLogger(JFrameReqViewApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameReqViewApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameReqViewApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameReqViewApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new JFrameReqViewApp().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupPartAllowance;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonGenerateReport;
    private javax.swing.JDialog jDialogPleaseWait;
    private javax.swing.JDialog jDialogPleaseWaitReport;
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
    private javax.swing.JLabel jLabelHeaderLine1;
    private javax.swing.JLabel jLabelHeaderLine2;
    private javax.swing.JLabel jLabelHeaderLine3;
    private javax.swing.JLabel jLabelHeaderLine4;
    private javax.swing.JLabel jLabelHeaderLine5;
    private javax.swing.JLabel jLabelHeaderLine6;
    private javax.swing.JLabel jLabelImg;
    private javax.swing.JLabel jLabelIncidentalSub;
    private javax.swing.JLabel jLabelIncidentalSubTot;
    private javax.swing.JLabel jLabelLinLogNam1;
    private javax.swing.JLabel jLabelLinLogNam2;
    private javax.swing.JLabel jLabelLinLogNam3;
    private javax.swing.JLabel jLabelLinLogNam4;
    private javax.swing.JLabel jLabelLinLogNam5;
    private javax.swing.JLabel jLabelLinLogNam6;
    private javax.swing.JLabel jLabelLinRegNum1;
    private javax.swing.JLabel jLabelLinRegNum2;
    private javax.swing.JLabel jLabelLinRegNum3;
    private javax.swing.JLabel jLabelLinRegNum4;
    private javax.swing.JLabel jLabelLinRegNum5;
    private javax.swing.JLabel jLabelLinSerial1;
    private javax.swing.JLabel jLabelLinSerial2;
    private javax.swing.JLabel jLabelLinSerial3;
    private javax.swing.JLabel jLabelLinSerial4;
    private javax.swing.JLabel jLabelLinSerial5;
    private javax.swing.JLabel jLabelLineDate1;
    private javax.swing.JLabel jLabelLineDate2;
    private javax.swing.JLabel jLabelLineDate3;
    private javax.swing.JLabel jLabelLineDate4;
    private javax.swing.JLabel jLabelLineDate5;
    private javax.swing.JLabel jLabelLineDate6;
    private javax.swing.JLabel jLabelLineTime1;
    private javax.swing.JLabel jLabelLineTime2;
    private javax.swing.JLabel jLabelLineTime3;
    private javax.swing.JLabel jLabelLineTime4;
    private javax.swing.JLabel jLabelLineTime5;
    private javax.swing.JLabel jLabelLineTime6;
    private javax.swing.JLabel jLabelLogo;
    private javax.swing.JLabel jLabelLogo2;
    private javax.swing.JLabel jLabelLogo3;
    private javax.swing.JLabel jLabelLogo4;
    private javax.swing.JLabel jLabelLogo5;
    private javax.swing.JLabel jLabelLogo6;
    private javax.swing.JLabel jLabelLogo7;
    private javax.swing.JLabel jLabelLunchSub;
    private javax.swing.JLabel jLabelLunchSubTot;
    private javax.swing.JLabel jLabelMainPurpose;
    private javax.swing.JLabel jLabelMiscReq;
    private javax.swing.JLabel jLabelMiscSubTot;
    private javax.swing.JLabel jLabelMscSub;
    private javax.swing.JLabel jLabelNum;
    private javax.swing.JLabel jLabelNum2;
    private javax.swing.JLabel jLabelNum3;
    private javax.swing.JLabel jLabelNum4;
    private javax.swing.JLabel jLabelNum5;
    private javax.swing.JLabel jLabelNum6;
    private javax.swing.JLabel jLabelOffice;
    private javax.swing.JLabel jLabelProvince;
    private javax.swing.JLabel jLabelRegDate;
    private javax.swing.JLabel jLabelRegYear;
    private javax.swing.JLabel jLabelReject;
    private javax.swing.JLabel jLabelRejectComments;
    private javax.swing.JLabel jLabelReqAccNum;
    private javax.swing.JLabel jLabelReqEmpNum;
    private javax.swing.JLabel jLabelReqOffice;
    private javax.swing.JLabel jLabelReqProvince;
    private javax.swing.JLabel jLabelSepDetails;
    private javax.swing.JLabel jLabelSerial;
    private javax.swing.JLabel jLabelStatusView;
    private javax.swing.JLabel jLabellogged1;
    private javax.swing.JLabel jLabellogged2;
    private javax.swing.JLabel jLabellogged3;
    private javax.swing.JLabel jLabellogged4;
    private javax.swing.JLabel jLabellogged5;
    private javax.swing.JLabel jLabellogged6;
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
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelDocAttach;
    private javax.swing.JPanel jPanelGen;
    private javax.swing.JPanel jPanelRequestWk1;
    private javax.swing.JPanel jPanelRequestWk2;
    private javax.swing.JPanel jPanelRequestWk3;
    private javax.swing.JPanel jPanelRequestWk4;
    private javax.swing.JPanel jPanelRequestWk5;
    private javax.swing.JPanel jPanelStatusView;
    private javax.swing.JRadioButton jRadioPartAllow;
    private javax.swing.JRadioButton jRadioPerDiem;
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
    private javax.swing.JPopupMenu.Separator jSeparator22;
    private javax.swing.JPopupMenu.Separator jSeparator23;
    private javax.swing.JPopupMenu.Separator jSeparator26;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator34;
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
    private javax.swing.JTextArea jTextAreaComments;
    private javax.swing.JTextField jTextRegNum;
    // End of variables declaration//GEN-END:variables
}
