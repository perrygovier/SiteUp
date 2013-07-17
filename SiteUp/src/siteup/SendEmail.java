/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siteup;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {

    Properties prop = new Properties();

    public void main(String[] args) {
        try {
            prop.load(new FileInputStream("config.properties"));
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        final String username = prop.getProperty("fromemail");
        final String password = prop.getProperty("pass");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(prop.getProperty("toemail")));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(args[1]));
            message.setSubject("Site Down!");
            message.setText(args[0]);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
