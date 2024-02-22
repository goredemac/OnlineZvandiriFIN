/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 *
 * @author goredemac
 */
public class finMail {
    connCred c= new connCred();
    expirePlan exp= new expirePlan();
      public void sendMailExpire(String usrMail,String refNum, String sendToExpire) {

   
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(c.FinMailUsrN, c.FinMailUsrP);
            }
        });
        try {
          
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(c.FinMailUsrN));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(usrMail));
            message.setSubject("Plan Approval Failure.Plan Reference No. P" + refNum);

            message.setContent("<html><body>Good Day <br><br> "
                    + "Your plan could not be approved by " + sendToExpire + ""
                    + " as some or all of the activities dates on the plan have passed.</b> <br><br>"
                    + " Perdiem Management System </body></html>", "text/html;charset=utf-8");

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
