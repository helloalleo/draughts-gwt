package online.draughts.rus.server.service;

import com.google.apphosting.api.ApiProxy;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.draughts.rus.server.config.ServerConfiguration;
import online.draughts.rus.server.util.Utils;
import online.draughts.rus.shared.model.Player;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 26.11.15
 * Time: 20:26
 */
@Singleton
public class MailService {

  private final ServerConfiguration config;
  private final Logger logger;

  @Inject
  public MailService(ServerConfiguration config,
                     Logger logger) {
    this.config = config;
    this.logger = logger;
  }

  public void sendNotificationMail(String msg, Player receiver, Player sender) {
    Properties props = new Properties();
    props.put("mail.transport.protocol", "smtp");
    if (config.isDebug()) {
      props.put("mail.smtp.port", "1025");
    } else {
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.starttls.required", "true");
      props.put("mail.smtp.sasl.enable", "false");
    }

    Session mailSession = Session.getInstance(props, null);
    if (config.isDebug()) {
      mailSession.setDebug(true);
    }

    try {
      Transport transport = mailSession.getTransport();

      MimeMessage message = new MimeMessage(mailSession);
      message.setSubject(String.format("Новое сообщение от %s", sender.getPublicName()));
      String fromEmail = "admin@" + ApiProxy.getCurrentEnvironment().getAppId() + ".appspotmail.com";
      message.setFrom(new InternetAddress(fromEmail, "Шашки.Онлайн"));
      message.addRecipient(Message.RecipientType.TO,
          new InternetAddress(receiver.getEmail(), receiver.getPublicName()));

      //
      // This HTML mail have to 2 part, the BODY and the embedded image
      //
      MimeMultipart multipart = new MimeMultipart();

      // first part  (the html)
      BodyPart messageBodyPart = new MimeBodyPart();
      String htmlText = Utils.readFile("mail/new_message/index.html");
      htmlText = htmlText.replace("{{0}}", sender.getPublicName());
      messageBodyPart.setContent(htmlText, "text/html");

      // add it
      multipart.addBodyPart(messageBodyPart);

      // second part (the image)
      messageBodyPart = new MimeBodyPart();
      String text = Utils.readFile("mail/new_message/new_message.txt");
      text = text.replace("{{0}}", sender.getPublicName());
      messageBodyPart.setContent(text, "plain/text");

      // add it
      multipart.addBodyPart(messageBodyPart);

      // put everything together
      message.setContent(multipart);

      transport.connect();
      transport.sendMessage(message,
          message.getRecipients(Message.RecipientType.TO));
      transport.close();
    } catch (AddressException e) {
      e.printStackTrace();
      logger.info("error address");
      logger.log(Level.SEVERE, e.toString());
    } catch (MessagingException e) {
      e.printStackTrace();
      logger.info("error message");
      logger.log(Level.SEVERE, e.toString());
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }
}
