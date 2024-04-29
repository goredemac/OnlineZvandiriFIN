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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import utils.connCred;

/**
 *
 * @author goredemac
 */
public class recruitRecSend {

    connCred c = new connCred();
    PreparedStatement pst = null;
    Date curYear = new Date();
    SimpleDateFormat df = new SimpleDateFormat("yyyy");
    public String loggedEmpNam, loggedEmpSupNam, loggedEmpSupEmail, loggedEmpEmail,
            ProvFinNam, ProvFinEmail, ProvEmpNum, sendToEmpNam, sendToEmpEmail, sendToEmpNum,
            sendToWithDrawEmpNam, sendToWithDrawEmpEmail, sendToWithDrawEmpNum,
            sendToAssistEmpEmail;

    public void getUsrLoggedName(String empNum) {

        String loggedEmpNum = empNum;
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT EMP_NAM,EMP_MAIL FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[EmpDetTab] "
                    + "where EMP_NUM = '" + loggedEmpNum + "'");

            while (r.next()) {
                loggedEmpNam = r.getString(1);
                loggedEmpEmail = r.getString(2);
            }
            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void getProvMgrName(String selProv) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT EMP_NAM ,EMP_MAIL FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] where "
                    + "EMP_NUM = (SELECT EMP_NUM FROM [ClaimsAppSysZvandiri].[dbo].[ProvManTab] where  province ='" + selProv + "')");

            while (r.next()) {
                loggedEmpSupNam = r.getString(1);
                loggedEmpSupEmail = r.getString(2);

            }
            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void getSendToName(String empNum) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT EMP_NAM,EMP_MAIL FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[EmpDetTab] "
                    + "where EMP_NUM = '" + empNum + "'");

            while (r.next()) {
                sendToEmpNam = r.getString(1);
                sendToEmpEmail = r.getString(2);
            }
            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void getSendToWithDrawName(String empNam) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT EMP_NAM,EMP_MAIL,EMP_NUM FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[EmpDetTab] "
                    + "where EMP_NAM = '" + empNam + "'");

            while (r.next()) {
                sendToWithDrawEmpNam = r.getString(1);
                sendToWithDrawEmpEmail = r.getString(2);
                sendToWithDrawEmpNum = r.getString(3);
            }
            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void getSendToAssistName(String empNam) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT EMP_MAIL FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[EmpDetTab] "
                    + "where EMP_NAM = '" + empNam + "'");

            while (r.next()) {
                sendToAssistEmpEmail = r.getString(1);
                
            }
            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

   
    
    public void getSendToNameSD(String empNam) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT EMP_NUM,EMP_NAM,EMP_MAIL FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[EmpDetTab] "
                    + "where EMP_NAM = '" + empNam + "'");

            while (r.next()) {
                sendToEmpNum = r.getString(1);
                sendToEmpNam = r.getString(2);
                sendToEmpEmail = r.getString(3);
            }
            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void getOrgSendToName(String empNam) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT EMP_NAM,EMP_MAIL FROM "
                    + "[ClaimsAppSysZvandiri].[dbo].[EmpDetTab] "
                    + "where EMP_NAM = '" + empNam + "'");

            while (r.next()) {
                sendToEmpNam = r.getString(1);
                sendToEmpEmail = r.getString(2);
            }
            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void getProvFinName(String provNam) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + c.ipAdd + ";"
                    + "DataBaseName=ClaimsAppSysZvandiri;user=" + c.usrNFin + ";password=" + c.usrPFin + ";");

            Statement st = conn.createStatement();

            ResultSet r = st.executeQuery("SELECT EMP_NAM ,EMP_MAIL,EMP_NUM FROM [ClaimsAppSysZvandiri].[dbo].[EmpDetTab] where "
                    + "EMP_NUM = (SELECT EMP_NUM FROM [ClaimsAppSysZvandiri].[dbo].[ProvFinAdmTab] where  province ='" + provNam + "')");

            while (r.next()) {
                ProvFinNam = r.getString(1);
                ProvFinEmail = r.getString(2);
                ProvEmpNum = r.getString(3);

            }
            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void sendMailUsrCreate(String sendToemail, String finProvEmail, String FinCentralEmail,
            String refSerial, String refNum, String sendToName, String letterAmt, String filePath, String fileName,
            String finComments, String actSta, String WithDrawNam, String WithDrawMail) {

        String CCEmail = finProvEmail + "," + FinCentralEmail + "," + loggedEmpSupEmail + "," + WithDrawMail;
        String[] ccAddressList = {finProvEmail, FinCentralEmail};
        final String username = c.FinMailUsrN;
        final String password = c.FinMailUsrP;
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
                    InternetAddress.parse(sendToemail));
            message.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(CCEmail));
            if ("D".equals(actSta)) {
                message.setSubject("Bank Withdrawal Letter Ref. No. " + refSerial + " " + refNum);

                BodyPart messageBodyPart = new MimeBodyPart();

                messageBodyPart.setContent("<html><body> Dear <b>" + sendToName + "</b><br><br> "
                        + "Please find attached cash withdrawal letter for Reference No. " + refSerial + " " + refNum + ""
                        + " total amount $" + letterAmt + ".<br><br> The cash withdrawal shall be done by " + WithDrawNam + ".<br><br>"
                        + "<b>Finance comments :</b> " + finComments + " <br><br>"
                        + "Kind Regards <br><br> Finance Management System </body></html>", "text/html;charset=utf-8");
                Multipart multipart = new MimeMultipart();

                multipart.addBodyPart(messageBodyPart);

                messageBodyPart = new MimeBodyPart();

                DataSource source = new FileDataSource(filePath);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(fileName);
                multipart.addBodyPart(messageBodyPart);

                message.setContent(multipart);
            } else {
                message.setSubject("Bank Special Withdrawal Letter " + refSerial + " " + refNum);

                BodyPart messageBodyPart = new MimeBodyPart();

                messageBodyPart.setContent("<html><body> Dear <b>" + sendToName + "</b><br><br> "
                        + "Please find attached cash withdrawal letter for " + refSerial + " " + refNum + ""
                        + " total amount $" + letterAmt + ".<br><br> The cash withdrawal shall be done by " + WithDrawNam + ". <br><br>"
                        + "<b>Finance comments :</b> " + finComments + " <br><br> "
                        + "Kind Regards <br><br> Finance Management System </body></html>", "text/html;charset=utf-8");
                Multipart multipart = new MimeMultipart();

                multipart.addBodyPart(messageBodyPart);

                messageBodyPart = new MimeBodyPart();

                DataSource source = new FileDataSource(filePath);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(fileName);
                multipart.addBodyPart(messageBodyPart);

                message.setContent(multipart);
            }

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    public void sendMailUsrCollect(String sendToemail, String FinCentralEmail,
            String refSerial, String refNum, String sendToName, String letterAmt,
            String filePath, String fileName, String provComments, String radStatus) {

        final String username = c.FinMailUsrN;
        final String password = c.FinMailUsrP;
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
                    InternetAddress.parse(sendToemail));

            if ("Y".equals(radStatus)) {

                message.setSubject("Cash Collection Confirmation Ref. No. " + refSerial + " " + refNum);

                BodyPart messageBodyPart = new MimeBodyPart();

                messageBodyPart.setContent("<html><body> Dear <b>" + sendToName + "</b><br><br> "
                        + "Please find attached cash withdrawal confirmation for Reference No. " + refSerial + " " + refNum + ""
                        + " total amount $" + letterAmt + ".<br><br>"
                        + "<b>Province comments :</b> " + provComments + " <br><br>"
                        + " Kind Regards <br><br> Finance Management System </body></html>", "text/html;charset=utf-8");

                Multipart multipart = new MimeMultipart();

                multipart.addBodyPart(messageBodyPart);

                messageBodyPart = new MimeBodyPart();

                DataSource source = new FileDataSource(filePath);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(fileName);
                multipart.addBodyPart(messageBodyPart);

                message.setContent(multipart);
            } else if ("N".equals(radStatus)) {
                message.setSubject("Cash Collection Confirmation Batch No.  " + refNum);

                BodyPart messageBodyPart = new MimeBodyPart();

                messageBodyPart.setContent("<html><body> Dear <b>" + sendToName + "</b><br><br> "
                        + "Please find attached cash withdrawal confirmation for Batch No.  " + refNum + ""
                        + " total amount $" + letterAmt + ".<br><br>"
                        + "<b>Province comments :</b> " + provComments + " <br><br>"
                        + " Kind Regards <br><br> Finance Management System </body></html>", "text/html;charset=utf-8");

                Multipart multipart = new MimeMultipart();

                multipart.addBodyPart(messageBodyPart);

                messageBodyPart = new MimeBodyPart();

                DataSource source = new FileDataSource(filePath);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(fileName);
                multipart.addBodyPart(messageBodyPart);

                message.setContent(multipart);
            }
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    public void sendMailUsrAssist(String sendToAssistemail, String stockYear, String stockMonth,
            String stockOff,String mailMsg) {

        final String username = c.FinMailUsrN;
        final String password = c.FinMailUsrP;
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
                    InternetAddress.parse(sendToAssistemail));

            message.setSubject(stockMonth + " " + stockYear + " Stock Take - " + stockOff);
            

            message.setContent("<html><body>"+mailMsg+" <br><br>Kind Regards <br><br> Finance Management System </body></html>", "text/html;charset=utf-8");
            
//            message.setContent("<html><body> Dear <b>" + UserAssistName + "</b><br><br> "
//                    + "Stock take record for " + stockMonth + " " + stockYear + " that you assisted on has been sent to you for verification. <br><br>"
//                    + "Kind Regards <br><br> Finance Management System </body></html>", "text/html;charset=utf-8");


            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    public void sendMailVehMgt(String sendToemail, String mailHeader,String mailMsg) {

        final String username = c.FinMailUsrN;
        final String password = c.FinMailUsrP;
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
                    InternetAddress.parse(sendToemail));
            message.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse("bmukandi@ophid.co.zw"));

            message.setSubject(mailHeader);
            

            message.setContent("<html><body>"+mailMsg+" <br><br>Kind Regards <br><br> Finance Management System </body></html>", "text/html;charset=utf-8");
            
//            message.setContent("<html><body> Dear <b>" + UserAssistName + "</b><br><br> "
//                    + "Stock take record for " + stockMonth + " " + stockYear + " that you assisted on has been sent to you for verification. <br><br>"
//                    + "Kind Regards <br><br> Finance Management System </body></html>", "text/html;charset=utf-8");


            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
    
     public void sendMailExtract(String mailTitle,String fileFullPathName,String bodyMsg,String fileName) {

        final String username = c.FinMailUsrN;
        final String password = c.FinMailUsrP;
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

            String filename = fileName;
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(c.FinMailUsrN));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(c.FinGrpMail));
            message.setSubject(mailTitle);

            DataSource fds;

            fds = new FileDataSource(fileFullPathName);

            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setContent("<html><body> Good Day Team <br><br> "+bodyMsg+""
                                        + "<br><br>Kind Regards<br><br>Perdiem Management System</body></html>", "text/html;charset=utf-8");

            MimeBodyPart mbp2 = new MimeBodyPart();
            mbp2.setDataHandler(new DataHandler(fds));
            mbp2.setFileName(filename);

            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);
            mp.addBodyPart(mbp2);
            message.setContent(mp);
            message.saveChanges();
            message.setSentDate(new java.util.Date());
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
