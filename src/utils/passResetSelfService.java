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
import java.sql.Statement;
import ClaimAppendix1.JFrameFinSysLogin;
import javax.swing.JOptionPane;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 *
 * @author goredemac
 */
public class passResetSelfService {

    connCred c = new connCred();
    public String empNam, empMail, logNam;
    public int logCount = 0;

    //leaveapp.JFrameLeaveLogin test= new leaveapp.JFrameLeaveLogin();
    public void getDetails(String empNum) {
        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=HRLeaveSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();
            System.out.println("emp " + empNum);
            st.executeQuery("SELECT EMP_NAM,EMP_MAIL FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] "
                    + "where upper(Emp_Num) = upper('" + empNum + "')");
            ResultSet rs = st.getResultSet();

            while (rs.next()) {
                empNam = rs.getString(1);
                empMail = rs.getString(2);
            }

            st1.executeQuery("SELECT usrnam FROM [HRLeaveSysZvandiri].[dbo].[usrlogin] where EmpNum = '" + empNum + "'");
            ResultSet rs1 = st1.getResultSet();

            while (rs1.next()) {
                logNam = rs1.getString(1);
            }

            st2.executeQuery("SELECT count(*) FROM [HRLeaveSysZvandiri].[dbo].[usrlogin] where EmpNum = '" + empNum + "'");
            ResultSet rs2 = st2.getResultSet();

            while (rs1.next()) {
                logCount = rs2.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void sendMail(String pID) {

        final String username = c.HRmailUsrN;
        final String password = c.HRmailUsrP;
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");
     
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(empMail));
            message.setSubject("Login Credentials management for " + empNam);

            message.setContent("<html><body> Dear " + empNam + "<br><br> "
                    + "Your password has been reset/created as per your request. "
                    + "<br><br>Your login name is <b>" + logNam + "</b> and password is <b>" + pID + "</b>"
                    + "<br><br>Change your password as soon as possible."
                    + "<br><br>If you have not requested a password reset/creation please contact HR on finance@zimttech.org or +263-0242-2701500 / 2700585 ."
                    + "<br><br> Kind Regards <br><br>"
                    + " HR Management System </body></html>", "text/html;charset=utf-8");


                Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
