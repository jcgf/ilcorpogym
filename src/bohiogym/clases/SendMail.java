/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym.clases;

import entity.GymUsuarios;
import entity.MailConfig;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManagerFactory;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import jpa.MailConfigJpaController;

/**
 *
 * @author Juan
 */
public class SendMail {
    
    private EntityManagerFactory factory;
    private final Properties properties = new Properties();
    private Session session;
    
    private void init(EntityManagerFactory factory) {
        this.factory = factory;
        MailConfigJpaController mcjc = new MailConfigJpaController(factory);
        MailConfig mailConfig = mcjc.findMailConfig(1);
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", 587);
        properties.put("mail.smtp.mail.sender", mailConfig.getEmail());
        properties.put("mail.smtp.user", "Il Corpo");
        properties.put("mail.smtp.password", mailConfig.getPassword());
        properties.put("mail.smtp.auth", "true");
        session = Session.getDefaultInstance(properties);
    }
    
    public void sendEmail(ArrayList<String> usuarios, ArrayList<String> fecha, String mensaje, EntityManagerFactory factory) {
        init(factory);
        try {
            for (int i = 0; i < usuarios.size(); i++) {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
                BodyPart texto = new MimeBodyPart();
                texto.setText(mensaje + fecha.get(i));
                MimeMultipart multiParte = new MimeMultipart();
                multiParte.addBodyPart(texto);
                message.addRecipients(Message.RecipientType.TO, usuarios.get(i));
                message.setSubject("Finalizacion de Membrecia");
                message.setContent(multiParte);
                Transport t = session.getTransport("smtp");
                t.connect((String) properties.get("mail.smtp.mail.sender"), (String) properties.get("mail.smtp.password"));
                t.sendMessage(message, message.getAllRecipients());
                t.close();
            }
        } catch (MessagingException me) {
            JOptionPane.showMessageDialog(null, "Error al enviar mensajes: \n" + me.getMessage());
        }
    }
    
    public void enviaEmail(ArrayList<GymUsuarios> usuarios, String mensaje, EntityManagerFactory factory) {
        init(factory);
        GymUsuarios u = null;
        try {
            InternetAddress[] recipientAddress = new InternetAddress[usuarios.size()];
            for (int i = 0; i < usuarios.size(); i++) {
                u = usuarios.get(i);
                System.out.println(i + "-" + u.getIdentificacion() + "-" + u.getEmail());
                recipientAddress[i] = new InternetAddress(u.getEmail());
            }
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
            BodyPart texto = new MimeBodyPart();
            texto.setText(mensaje);
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);
            message.addRecipients(Message.RecipientType.TO, recipientAddress);
            message.setSubject("Ilcorpo News");
            message.setContent(multiParte);
            Transport t = session.getTransport("smtp");
            t.connect((String) properties.get("mail.smtp.mail.sender"), (String) properties.get("mail.smtp.password"));
            t.sendMessage(message, message.getAllRecipients());
            t.close();
        } catch (MessagingException me) {
            JOptionPane.showMessageDialog(null, "Error " + me.getMessage() + " \nCorreo Receptor: " + "<<" + u.getEmail() + ">>" + "\n" + u.getIdentificacion()
                    + " - " + u.getNombres() + " " + u.getApellidos());
        }
    }
    
}
