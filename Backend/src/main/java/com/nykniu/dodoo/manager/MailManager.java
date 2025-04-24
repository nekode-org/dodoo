package com.nykniu.dodoo.manager;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import org.apache.catalina.Globals;

public class MailManager {

    public static void sendEmail(String emailAddress, String emailSubject, String emailBody, HashMap<String, byte[]> fileAttatchments) throws MessagingException, UnsupportedEncodingException {
        String mailId = "laxelott2@gmail.com";
        String appPassword = "dfgd nozc utji ylky";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.debug", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        		
        //create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailId, appPassword);
			}
		};

        Session session = Session.getInstance(props, auth);
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(mailId));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailAddress));
        message.setSubject(MimeUtility.encodeText(emailSubject, "UTF-8", "Q"));

        MimeBodyPart htmlPart;
        Multipart mp;
        MimeBodyPart att;
        ByteArrayDataSource bds;
        for (String key : fileAttatchments.keySet()) {
            mp = new MimeMultipart();
            htmlPart = new MimeBodyPart();
            att = new MimeBodyPart();
            bds = new ByteArrayDataSource(fileAttatchments.get(key), "application/pdf");
            att.setDataHandler(new DataHandler(bds));
            att.setFileName(key);
            htmlPart.setContent(emailBody, "text/html; charset=utf-8");
            mp.addBodyPart(htmlPart);
            mp.addBodyPart(att);
            message.setContent(mp);
        }
        
        Transport.send(message);
    }

}
