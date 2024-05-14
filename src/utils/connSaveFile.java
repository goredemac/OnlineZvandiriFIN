/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import ClaimAppendix1.JFrameMain;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import utils.connCred;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import ClaimAppendix1.JFrameMain;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author goredemac
 */
public class connSaveFile {

    SimpleDateFormat df = new SimpleDateFormat("yyyy");

    public connSaveFile() {
    }

    public Connection dbConnect(String db_connect_string,
            String db_userid, String db_password) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(
                    db_connect_string, db_userid, db_password);

            return conn;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertPDF2(Connection conn, String refYear, String filename,
            String fileDsc, String serial, String refNo, int itmNum) {

        int len;
        String query;
        PreparedStatement pstmt;
    
        try {
            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            len = (int) file.length();
            query = ("insert into [ClaimsAppSysZvandiri].[dbo].[ClaimMeetAttDocTab] VALUES(?,?,?,?,?,?,?,?,?,?)");
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, refYear);
            pstmt.setString(2, serial);
            pstmt.setString(3, refNo);
            pstmt.setString(4, Integer.toString(itmNum));
            pstmt.setString(5, file.getName());
            pstmt.setInt(6, len);

            //method to insert a stream of bytes
            pstmt.setBinaryStream(7, fis, len);
            pstmt.setString(8, "1");
            pstmt.setString(9, "1");
            pstmt.setString(10, "A");
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void insertPDF3(Connection conn, String refYear, String filePath, String filename,
            String fileDsc, String serial, String refNo, String EmpNum, String EmpNam, String Province,
            String Office, String totAmt, String mvtDate, String actSta, String VoucherNum, String refDate,
            String withdrawerName, String withdrawerEmpNum) {

        int len;
        String query;
        PreparedStatement pstmt;
        JFrameMain disLet = new JFrameMain();

        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            len = (int) file.length();
            query = ("insert into [ClaimsAppSysZvandiri].[dbo].[ClaimLetterMgtTab]"
                    + "( REF_YEAR,SERIAL,REF_NUM,BAT_NUM,REF_DATE,VOUCHER_NUM,EMP_NUM,EMP_NAM,"
                    + "EMP_PROV,EMP_OFF,EMP_NUM_WITHDRAW,EMP_NAM_WITHDRAW,ACT_TOT_AMT,FILENAME,FILELENGTH,FILEIMAGE,"
                    + "FILEFREETEXT,DATE_SEND_COLLECTED,ACT_VER,DOC_VER,ACT_REC_STA) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, refYear);
            pstmt.setString(2, serial);
            if ("SD".equals(actSta)) {
                pstmt.setString(3, "0");
            } else {
                pstmt.setString(3, refNo);
            }
            if ("SD".equals(actSta)) {
                pstmt.setString(4, refNo);
            } else {
                pstmt.setString(4, "");
            }
            pstmt.setString(5, refDate);
            pstmt.setString(6, VoucherNum);
            pstmt.setString(7, EmpNum);
            pstmt.setString(8, EmpNam);
            pstmt.setString(9, Province);
            pstmt.setString(10, Office);
            pstmt.setString(11, withdrawerEmpNum);
            pstmt.setString(12, withdrawerName);
            pstmt.setString(13, totAmt);
            pstmt.setString(14, file.getName());
            pstmt.setInt(15, len);
            pstmt.setBinaryStream(16, fis, len);
            pstmt.setString(17, fileDsc);
            pstmt.setString(18, mvtDate);
            pstmt.setString(19, "1");
            pstmt.setString(20, "1");
            pstmt.setString(21, actSta);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertPDF4(Connection conn, String refNo, String refYear, String serial, String VoucherNum,
            String regDate, String EmpNum, String EmpNam, String Province, String Office,
            String totAmt, String collectedDate, String witdrawDate, String withdrawEmpNum,
            String withdrawEmpNam, String filePath, String filename,
            String fileDsc, String actSta, String refDate) {

        int len;
        String query;
        PreparedStatement pstmt;
        JFrameMain disLet = new JFrameMain();

        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            len = (int) file.length();
            query = ("insert into [ClaimsAppSysZvandiri].[dbo].[ClaimLetterMgtTab]"
                    + "( REF_YEAR,SERIAL,REF_NUM,BAT_NUM,REF_DATE,VOUCHER_NUM,EMP_NUM,EMP_NAM,"
                    + "EMP_PROV,EMP_OFF,ACT_TOT_AMT,DATE_SEND_COLLECTED,DATE_WITHDRAWN,"
                    + "EMP_NUM_WITHDRAW,EMP_NAM_WITHDRAW,FILENAME,FILELENGTH,FILEIMAGE,"
                    + "FILEFREETEXT,ACT_VER,DOC_VER,ACT_REC_STA) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, refYear);
            pstmt.setString(2, "RM");

            if ("Batch".equals(serial)) {
                pstmt.setString(3, "0");
            } else if ("RM".equals(serial)) {
                pstmt.setString(3, refNo);
            }
            if ("Batch".equals(serial)) {
                pstmt.setString(4, VoucherNum);
            } else if ("RM".equals(serial)) {
                pstmt.setString(4, "");
            }
            pstmt.setString(5, refDate);
            pstmt.setString(6, VoucherNum);
            pstmt.setString(7, EmpNum);
            pstmt.setString(8, EmpNam);
            pstmt.setString(9, Province);
            pstmt.setString(10, Office);
            pstmt.setString(11, totAmt);
            pstmt.setString(12, collectedDate);
            pstmt.setString(13, witdrawDate);
            pstmt.setString(14, withdrawEmpNum);
            pstmt.setString(15, withdrawEmpNam);
            pstmt.setString(16, file.getName());
            pstmt.setInt(17, len);
            pstmt.setBinaryStream(18, fis, len);
            pstmt.setString(19, fileDsc);
            pstmt.setString(20, "1");
            pstmt.setString(21, "1");
            pstmt.setString(22, actSta);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertPDF5(Connection conn, String refYear, String filename,
            String fileDsc, String serial, String refNo, int itmNum) {

        System.out.println("dhuhdu " + conn + " " + refYear + " " + filename + " " + fileDsc + " " + serial + " " + refNo + " " + itmNum);
        int len;
        String query;
        PreparedStatement pstmt;

        try {
            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            len = (int) file.length();
            query = ("insert into [ClaimsAppSysZvandiri].[dbo].[RMTAttDocTab] VALUES(?,?,?,?,?,?,?,?,?,?,?)");
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, refYear);
            pstmt.setString(2, serial);
            pstmt.setString(3, refNo);
            pstmt.setString(4, Integer.toString(itmNum));
            pstmt.setString(5, fileDsc);
            pstmt.setString(6, file.getName());
            pstmt.setInt(7, len);

            //method to insert a stream of bytes
            pstmt.setBinaryStream(8, fis, len);
            pstmt.setString(9, "1");
            pstmt.setString(10, "1");
            pstmt.setString(11, "A");
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertPDF6(Connection conn, String refYear, String dateAtt, String fileTyp, String filename,
            String fileDsc, String comments, String serial, String refNo, int itmNum) {

        System.out.println("dhuhdu " + conn + " " + refYear + " " + filename + " " + fileDsc + " " + serial + " " + refNo + " " + itmNum);
        int len;
        String query;
        PreparedStatement pstmt;

        try {
            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            len = (int) file.length();
            query = ("insert into [ClaimsAppSysZvandiri].[dbo].[RMTAttProposalTab] VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, refYear);
            pstmt.setString(2, serial);
            pstmt.setString(3, refNo);
            pstmt.setString(4, Integer.toString(itmNum));
            pstmt.setString(5, dateAtt);
            pstmt.setString(6, fileTyp);
            pstmt.setString(7, fileDsc);
            pstmt.setString(8, file.getName());
            pstmt.setInt(9, len);

            //method to insert a stream of bytes
            pstmt.setBinaryStream(10, fis, len);
            pstmt.setString(11, comments);
            pstmt.setString(12, "1");
            pstmt.setString(13, "1");
            pstmt.setString(14, "A");
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertPDF7(Connection conn, String curYear, String refNum, int itmNum, String filename, String fileDsc, String imgFileName,
            String serial) {

        int len;
        String query;
        PreparedStatement pstmt;

        try {
            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            len = (int) file.length();
            query = ("insert into [ClaimsAppSysZvandiri].[dbo].[ClaimReportAttDocTab] VALUES(?,?,?,?,?,?,?,?,?,?,?)");
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, curYear);
             pstmt.setString(2, serial);
            pstmt.setString(3, refNum);
            pstmt.setString(4, Integer.toString(itmNum));
            pstmt.setString(5, fileDsc);
            pstmt.setString(6, imgFileName);
            pstmt.setInt(7, len);
            pstmt.setBinaryStream(8, fis, len);
            pstmt.setString(9, "1");
            pstmt.setString(10, "1");
            pstmt.setString(11, "Q");
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertAttWkDoc(Connection conn, String filename, String fileDsc, String imgFileName,
            int itmNum, String planRefNum, String actVer, String docVer, String tabNam, int refYear) {

        System.out.println("det " + filename + "  " + fileDsc + " " + imgFileName + " " + itmNum + " " + planRefNum + " " + actVer + " " + docVer);
        int len;
        String query;
        PreparedStatement pstmt;

        try {
            File file = new File(imgFileName);
            FileInputStream fis = new FileInputStream(file);
            len = (int) file.length();
            query = ("insert into [ClaimsAppSysZvandiri].[dbo].[" + tabNam + "] VALUES(?,?,?,?,?,?,?,?,?,?)");
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, Integer.toString(refYear));
            pstmt.setString(2, planRefNum);
            pstmt.setString(3, Integer.toString(itmNum));
            pstmt.setString(4, filename);
            pstmt.setString(5, fileDsc);
            pstmt.setInt(6, len);
            pstmt.setBinaryStream(7, fis, len);
            pstmt.setString(8, actVer);
            pstmt.setString(9, docVer);
            pstmt.setString(10, "A");
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updOldAttDoc(Connection conn, String updList, String refNum, String tabNam) {
        try {

            System.out.println("fff "+updList+" ddd "+refNum+" ff "+tabNam);
            PreparedStatement pstmt;

            String sqlUpdateAttDocTab = "UPDATE [ClaimsAppSysZvandiri].[dbo].[" + tabNam + "] "
                    + "set ACT_REC_STA ='P' where ACT_ITM in " + updList + " and REF_NUM=" + refNum + "";

            pstmt = conn.prepareStatement(sqlUpdateAttDocTab);
            pstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void getPDFData(Connection conn, String voucherNum, String vType) {

        JFrameMain finMain = new JFrameMain();
//        JFrameHR recruitHR = new JFrameHR();

        byte[] fileBytes;
        String query;
        try {
            query = "SELECT FILEIMAGE,FILENAME FROM [ClaimsAppSysZvandiri].[dbo].[ClaimLetterMgtTab] "
                    + "where VOUCHER_NUM ='" + voucherNum + "' and ACT_REC_STA ='" + vType + "'";
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(query);
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");
            fileChooser.setLocation(600, 150);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));
            int userSelection = fileChooser.showSaveDialog(finMain);

            if (rs.next()) {
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();

                    File Nfile = new File(fileToSave.getAbsolutePath() + ".pdf");
                    //boolean exists = fileToSave.exists();
                    if (Nfile.exists()) {
                        JOptionPane.showMessageDialog(finMain, "File already exists.");
                    } else {
                        fileBytes = rs.getBytes(1);
                        OutputStream targetFile = new FileOutputStream(
                                fileToSave.getAbsolutePath() + ".pdf");
                        targetFile.write(fileBytes);
                        targetFile.close();

                    }

                }
            }
            //   recruitHR.jTableCasualAttDoc.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPDFData2(Connection conn, String refNum, String pdfName) {

        byte[] fileBytes;
        String query;
        try {
            query = "SELECT fileImage,fileName FROM [ClaimsAppSysZvandiri].[dbo].[ClaimMeetAttDocTab] "
                    + " where concat(SERIAL,REF_NUM,ACT_ITM) ='" + refNum + "' "
                    + "and ACT_REC_STA = 'A' ";

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

    public void getPDFData3(Connection conn, String refNum, String pdfName) {

        byte[] fileBytes;
        String query;
        try {
            query = "SELECT fileImage,fileName FROM [ClaimsAppSysZvandiri].[dbo].[RMTAttDocTab] "
                    + " where concat(SERIAL,REF_NUM,ACT_ITM) ='" + refNum + "' "
                    + "and ACT_REC_STA = 'A' ";

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPDFData4(Connection conn, String refNum, String pdfName) {
        byte[] fileBytes;
        String query;
       
        try {
            query = "SELECT fileImage,fileName FROM [ClaimsAppSysZvandiri].[dbo].[ClaimReportAttDocTab] "
                    + " where concat(SERIAL,REF_NUM,ACT_ITM) ='" + refNum + "' and ACT_REC_STA = 'A' ";

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

    public void getPDFData5(Connection conn, String refNum, String pdfName, String tabNam) {
        byte[] fileBytes;
        String query;
System.out.println("wk4ertt "+refNum+" fff "+pdfName);
        try {
            query = "SELECT fileImage,fileName FROM [ClaimsAppSysZvandiri].[dbo].[" + tabNam + "] "
                    + " where concat(REF_NUM,ACT_ITM) ='" + refNum + "' and ACT_REC_STA = 'A' ";

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

    public void getImgData(Connection conn, String refNum, String pdfName) {
        byte[] fileBytes;
        String query;

        try {
            query = "SELECT fileImage,fileName FROM [ClaimsAppSysZvandiri].[dbo].[ClaimMeetAttDocTab] "
                    + " where concat(SERIAL,REF_NUM,ACT_ITM) ='" + refNum + "' and ACT_REC_STA = 'A'";

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
