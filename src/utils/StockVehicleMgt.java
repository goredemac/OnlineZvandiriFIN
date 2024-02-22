/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileInputStream;
import utils.connCred;
import java.awt.Image;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author CanGoredema
 */
public class StockVehicleMgt {

    public String fetchString, empOff, sTYear, sTMonth;
    public int countVerify = 0;
    public int countOpen = 0;
    public int countStockRecord = 0;
    public int countStockExcessShortage = 0;
    public int countExcesShortExist = 0;
    public int countOpenPeriod = 0;
    public int countSysRole = 0;
    connCred c = new connCred();
    PreparedStatement pst = null;
    List<String> list = new ArrayList<>();
    List<String> listFetch = new ArrayList<>();

    void getRanList(String stockEmpOff, String stockYear, String stockMonth) {

        try {

            int listCount = 0;
            int numberOfElements;
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT PROD_ID  FROM [ClaimsAppSysZvandiri].[dbo].[StockTakeMonEndTab] where "
                    + "EMP_OFF ='" + stockEmpOff + "' and YEAR='" + stockYear + "' "
                    + "and STOCK_MONTH='" + stockMonth + "' and ACT_REC_STA = 'A' and QTY_VARIANCE=0");

            while (r.next()) {

                list.add(r.getString(1));

            }

            ResultSet r1 = st1.executeQuery("SELECT count(*)  FROM [ClaimsAppSysZvandiri].[dbo].[StockTakeMonEndTab] where "
                    + "EMP_OFF ='" + stockEmpOff + "' and YEAR='" + stockYear + "' "
                    + "and STOCK_MONTH='" + stockMonth + "' and ACT_REC_STA = 'A' and QTY_VARIANCE=0");

            while (r1.next()) {
                listCount = r1.getInt(1);

            }

            StockVehicleMgt obj = new StockVehicleMgt();
            ArrayList Names = new ArrayList();
            // boundIndex for select in sub list
            if (listCount <= 14) {
//                numberOfElements = listCount / 2;
                numberOfElements = 4;
                Names.add(obj.getRandomElement(list, numberOfElements));
            } else {
                numberOfElements = 6;
                Names.add(obj.getRandomElement(list, numberOfElements));
            }

            for (String val : obj.getRandomElement(list, numberOfElements)) {

                listFetch.add(val);
                System.out.println("one sixc " + listFetch);

            }

        } catch (Exception e) {
            System.out.println(e);
        }

        fetchString = "('" + String.join("','", listFetch) + "')";

        System.out.println(fetchString);
    }

    public void randomGen(String stockEmpOff, String stockYear, String stockMonth) {

        getRanList(stockEmpOff, stockYear, stockMonth);

    }

    public void checkVerifyRecord() {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            ResultSet r = st.executeQuery("SELECT count(*) FROM [ClaimsAppSysZvandiri].[dbo].[StockTakeActTab] "
                    + "where ACT_REC_STA ='A' and DOC_STATUS = 'Count Correct'  and "
                    + "EMP_OFF ='" + empOff + "' and YEAR='" + sTYear + "' and STOCK_MONTH='" + sTMonth + "' ");

            while (r.next()) {

                countVerify = r.getInt(1);

            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void findSysRole(String empNo) {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd
                    + ";DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT  count(distinct STORE_FUNCTION )"
                    + " FROM [ClaimsAppSysZvandiri].[dbo].[ProvStoresAdmTab] "
                    + "where emp_num = '" + empNo + "' and STORE_FUNCTION = 'Reports'");

            while (r.next()) {

                countSysRole = r.getInt(1);

            }

            //                 conn.close();
        } catch (Exception e) {

            System.out.println(e);
        }
    }

    public void checkStockRecord(String docStatus, String sendEmpName, String stockYear, String StockMonth) {
        try {

           
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();
            ResultSet r = st.executeQuery("SELECT count(*) FROM [ClaimsAppSysZvandiri].[dbo].[StockTakeActTab] "
                    + "  where ACT_REC_STA = 'A' and DOC_STATUS ='" + docStatus + "' "
                    + "and SEND_TO='" + sendEmpName + "' and YEAR='" + stockYear + "' "
                    + "and STOCK_MONTH='" + StockMonth + "'");

            while (r.next()) {

                countStockRecord = r.getInt(1);

            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void checkStockExcessShortage(String stockEmpOff, String status) {
        try {

//            
            String variance;
            if (status.equals("Shortage")) {
                variance = "QTY_PHY_COUNT_VARIANCE < 0";
            } else {
                variance = "QTY_PHY_COUNT_VARIANCE > 0";
            }

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");
            Statement st = conn.createStatement();
            ResultSet r = st.executeQuery("SELECT count(*) FROM  [ClaimsAppSysZvandiri].[dbo].[StockTakeExcessShortageTab] "
                    + "  where ACT_REC_STA = 'A' and EMP_OFF ='" + stockEmpOff + "'"
                    + " and " + variance + "");

            while (r.next()) {

                countStockExcessShortage = r.getInt(1);

            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    void stockTakeStatus(String empOff) {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT YEAR,STOCK_MONTH "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[StockTakePeriod] where STOCK_TAKE_STATUS ='Running' "
                    + "and EMP_OFF='" + empOff + "'");

            while (r.next()) {
                sTYear = r.getString(1);
                sTMonth = r.getString(2);

            }

            //                 conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        checkVerifyRecord();
    }

    void monthEndReportStatus(String empOff) {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT count(*) "
                    + "FROM [OphidLogBook].[dbo].[vehicle_report_period] where REPORT_STATUS ='Running' "
                    + "and EMP_OFF='" + empOff + "'");

            while (r.next()) {
                countOpenPeriod = r.getInt(1);

            }

            System.out.println("period count " + countOpenPeriod);

            if (countOpenPeriod > 0) {
                ResultSet r1 = st1.executeQuery("SELECT YEAR,REPORT_MONTH "
                        + "FROM [OphidLogBook].[dbo].[vehicle_report_period] where REPORT_STATUS ='Running' "
                        + "and EMP_OFF='" + empOff + "'");

                while (r1.next()) {
                    sTYear = r1.getString(1);
                    sTMonth = r1.getString(2);

                }
//System.out.println("sklksl lsls");
            }

            //                 conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    void monthEndGetReportPeriod(String empOff) {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st1 = conn.createStatement();

            ResultSet r1 = st1.executeQuery("SELECT YEAR,REPORT_MONTH "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[vehicle_report_period] where REPORT_STATUS ='Running' "
                    + "and EMP_OFF='" + empOff + "'");

            while (r1.next()) {
                sTYear = r1.getString(1);
                sTMonth = r1.getString(2);
                System.out.println("period " + sTMonth + " " + sTYear);

            }

            //                 conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void findUser(String logEmpNum, String checkState) {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select b.EMP_NAM,a.EMP_OFFICE FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetWS] a  "
                    + "join [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] b on a.EMP_NUM = b.EMP_NUM "
                    + "where b.EMP_NUM = '" + logEmpNum + "'");

            while (r.next()) {

                empOff = r.getString(2);

            }

            if ("Period Opened".equals(checkState)) {
                stockTakeStatus();
            } else if ("Vehicle Period Opened".equals(checkState)) {
                System.out.println("period off " + empOff);
                monthEndReportStatus(empOff);
            } else {
                stockTakeStatus(empOff);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void findUser(String logEmpNum) {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("select b.EMP_NAM,a.EMP_OFFICE FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetWS] a  "
                    + "join [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] b on a.EMP_NUM = b.EMP_NUM "
                    + "where b.EMP_NUM = '" + logEmpNum + "'");

            while (r.next()) {

                empOff = r.getString(2);

            }

            stockTakeStatus(empOff);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void stockTakeStatus() {
        try {

//            
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT count(*) "
                    + "FROM [ClaimsAppSysZvandiri].[dbo].[StockTakePeriod] where STOCK_TAKE_STATUS ='Running' "
                    + "and EMP_OFF='" + empOff + "'");

            while (r.next()) {
                countOpen = r.getInt(1);

            }

            ResultSet r1 = st1.executeQuery("SELECT count(*) FROM [ClaimsAppSysZvandiri].[dbo].[StockTakeExcessShortageTab] "
                    + "where ACT_REC_STA = 'A' and EMP_OFF = '" + empOff + "'");

            while (r1.next()) {
                countExcesShortExist = r1.getInt(1);

            }
            //                 conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Function select an element base on index and return
    // an element
    public List<String>
            getRandomElement(List<String> list, int totalItems) {
        Random rand = new Random();

        // create a temporary list for storing
        // selected element
        List<String> newList = new ArrayList<>();
        for (int i = 0; i < totalItems; i++) {

            // take a random index between 0 to size
            // of given List
            int randomIndex = rand.nextInt(list.size());

//             System.out.println("liii 1 " + list.get(i));
            // add element in temporary list
            newList.add(list.get(randomIndex));

            // Remove selected element from original list
            list.remove(randomIndex);
//             System.out.println("liii 3 " + list.get(i));
//                System.out.println("liii 2 " + list.get(i));
        }
        return newList;
    }

    public void insertPDF(Connection conn, String filename, String fileDsc, String imgFileName, String stockYear,
            String stockMonth, String stockOffice, int itmNum, String repNam) {

        int len;
        String query;
        PreparedStatement pstmt;

        try {

            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            len = (int) file.length();
            query = ("insert into [ClaimsAppSysZvandiri].[dbo].[stockShortExcessAttDocTab] VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, stockYear);
            pstmt.setString(2, stockMonth);
            pstmt.setString(3, stockOffice);
            pstmt.setString(4, repNam);
            pstmt.setString(5, Integer.toString(itmNum));
            pstmt.setString(6, fileDsc);
            pstmt.setString(7, imgFileName);
            pstmt.setInt(8, len);
            pstmt.setBinaryStream(9, fis, len);
            pstmt.setString(10, "1");
            pstmt.setString(11, "1");
            pstmt.setString(12, "A");
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertMaintainPDF(Connection conn, String filename, String invAmt, String fileDsc,
            String imgFileName, String refYear, String refNum, String serial, String itmNum, String docNam) {

        int len;
        String query;
        PreparedStatement pstmt;

        try {

            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            len = (int) file.length();
            query = ("insert into [OphidLogBook].[dbo].[vehicle_maintain_attdoc] VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, refYear);
            pstmt.setString(2, serial);
            pstmt.setString(3, refNum);
            pstmt.setString(4, docNam);
            pstmt.setString(5, itmNum);
            pstmt.setString(6, fileDsc);
            pstmt.setString(7, invAmt);
            pstmt.setString(8, imgFileName);
            pstmt.setInt(9, len);
            pstmt.setBinaryStream(10, fis, len);
            pstmt.setString(11, "1");
            pstmt.setString(12, "1");
            pstmt.setString(13, "A");
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPDFData(Connection conn, String refNum, String pdfName, String attType) {
        byte[] fileBytes;
        String query;

        try {
            query = "SELECT fileImage,fileName FROM [ClaimsAppSysZvandiri].[dbo].[stockShortExcessAttDocTab] "
                    + " where concat(REF_YEAR,STOCK_MONTH,EMP_OFF,ACT_ITM) ='" + refNum + "' and ACT_REC_STA = 'A' "
                    + "AND REP_NAM ='" + attType + "'";

            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(query);

            if (rs.next()) {
                File Nfile = new File(pdfName);
                fileBytes = rs.getBytes(1);
                OutputStream targetFile = new FileOutputStream(
                        pdfName);
                targetFile.write(fileBytes);
                Runtime.getRuntime().exec("rundll32 url.dll, FileProtocolHandler " + Nfile);
                targetFile.close();

            }
            //   recruitHR.jTableCasualAttDoc.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getVehPDFData(Connection conn, String refNum, String pdfName, String docName) {
        byte[] fileBytes;
        String query;
        System.out.println("values " + refNum + " " + pdfName + " " + docName);
        try {
            query = "SELECT fileImage,fileName FROM [OphidLogBook].[dbo].[vehicle_maintain_attdoc] "
                    + " where concat(SERIAL,' ',REF_NUM,ACT_ITM) ='" + refNum + "' and ACT_REC_STA = 'A'"
                    + " and DOC_NAM='" + docName + "' ";

            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(query);

            if (rs.next()) {
                File Nfile = new File(pdfName);
                fileBytes = rs.getBytes(1);
                OutputStream targetFile = new FileOutputStream(
                        pdfName);
                targetFile.write(fileBytes);
                Runtime.getRuntime().exec("rundll32 url.dll, FileProtocolHandler " + Nfile);
                targetFile.close();

            }
            //   recruitHR.jTableCasualAttDoc.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getVehPDFToll(Connection conn, String fileImg, String pdfName, String imgID) {
        try {
            //   System.out.println("Connected");

            //PreparedStatement ps=conn.prepareStatement("select * from tollgate_receipts where transaction_number=55"); 
            PreparedStatement ps = conn.prepareStatement("select * from [OphidLogBook].[dbo].[tollgate_receipts] where id='" + imgID + "'");
            //PreparedStatement ps=conn.prepareStatement("select * from tollgate_receipts where CAST(receipt_image AS VARCHAR(MAX))="+x);  

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
//                    System.out.print("The location is " + rs.getString("vehicle_number") + rs.getString("receipt_image"));
                Blob blob = rs.getBlob("receipt_image");
                byte[] data = blob.getBytes(1, (int) blob.length());
                BufferedImage img = null;
                try {

                    img = ImageIO.read(new ByteArrayInputStream(data));
                    JFrame f = new JFrame("Tollgate Reciept");
                    JPanel p = new JPanel();
                    JLabel l = new JLabel();
                    l.setIcon(new ImageIcon(img));
                    p.add(l);
                    f.add(p);
                    //f.setSize(200,300);
                    f.pack();
                    JScrollPane scrollBar = new JScrollPane(p, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                    f.add(scrollBar);
                    f.setVisible(true);

                    System.out.print(" Done");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void getVehPDFFuel(Connection conn, String recNum) {
        try {

            PreparedStatement ps = conn.prepareStatement("select * from [OphidLogBook].[dbo].[fuel_topup] where receipt_number='" + recNum + "'");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Blob blob = rs.getBlob("receipt_image");
                byte[] data = blob.getBytes(1, (int) blob.length());
                BufferedImage img = null;
                try {
                    img = ImageIO.read(new ByteArrayInputStream(data));
                    JFrame f = new JFrame("Fuel Reciept");
                    JPanel p = new JPanel();
                    JLabel l = new JLabel();
                    Image newimg = img.getScaledInstance(250, 250, java.awt.Image.SCALE_DEFAULT);
                    l.setIcon(new ImageIcon(newimg));
//                    l.setIcon(new ImageIcon(img));
                    p.add(l);
                    f.add(p);
                    //    f.setSize(200, 300);
                    f.pack();
                    JScrollPane scrollBar = new JScrollPane(p, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                    f.add(scrollBar);
                    f.setVisible(true);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
