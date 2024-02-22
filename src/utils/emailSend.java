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
public class emailSend {

    connCred c = new connCred();
    PreparedStatement pst = null;
    Date curYear = new Date();
    SimpleDateFormat df = new SimpleDateFormat("yyyy");
    public String loggedEmpNam, loggedEmpSupNam, loggedEmpSupEmail, loggedEmpEmail,
            ProvFinNam, ProvFinEmail, ProvEmpNum, sendToEmpNam, sendToEmpEmail, sendToEmpNum,
            sendToWithDrawEmpNam, sendToWithDrawEmpEmail, sendToWithDrawEmpNum,
            sendToAssistEmpEmail;

    public void sendMail(String MailMsgTitle, String email, String mailMsg,String ccMail) {
     
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
                    InternetAddress.parse(email));
            message.addRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(ccMail));
            message.setSubject(MailMsgTitle);

            message.setContent(mailMsg, "text/html;charset=utf-8");

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

}
