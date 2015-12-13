package online.draughts.rus.server.service;

import com.google.apphosting.api.ApiProxy;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.draughts.rus.server.config.ServerConfiguration;
import online.draughts.rus.server.domain.GameMessage;
import online.draughts.rus.server.domain.Player;
import online.draughts.rus.server.util.Utils;
import online.draughts.rus.shared.util.StringUtils;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 26.11.15
 * Time: 20:26
 */
@Singleton
public class MailService {

  private final ServerConfiguration config;
  private final Logger logger = Logger.getLogger(MailService.class);

  private final PlayerService playerService;
  private final ResourceBundle resourceBundle;

  @Inject
  public MailService(ServerConfiguration config,
                     PlayerService playerService) {
    this.config = config;
    this.playerService = playerService;
    this.resourceBundle = ResourceBundle.getBundle("ServerMessages");
  }

  private boolean sendNotificationMail(String msg, Player receiver, Player sender) {
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
      htmlText = htmlText.replace("{{0}}", msg);
      htmlText = htmlText.replace("{{1}}", sender.getPublicName());
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
      return true;
    } catch (AddressException e) {
      e.printStackTrace();
      logger.info("error address");
      logger.error(e.toString(), e);
    } catch (MessagingException | UnsupportedEncodingException e) {
      e.printStackTrace();
      logger.info("error message");
      logger.error(e.toString(), e);
    }
    return false;
  }

  public boolean sendNotification(GameMessage message) {
    Player receiver = message.getReceiver();
    Player sender = message.getSender();

    if (null == receiver || null == sender) {
      return false;
    }

    // если получатель не имеет сообщений от данного отправителя, тогда инициализируем Map и увеличиваем счетчика
    // на единицу
    if (!receiver.getFriendUnreadMessagesMap().containsKey(sender.getId())) {
      receiver.getFriendUnreadMessagesMap().put(sender.getId(), 0);
    }
    final Integer unreadMessages = receiver.getFriendUnreadMessagesMap().get(sender.getId());
    receiver.getFriendUnreadMessagesMap().put(sender.getId(), unreadMessages + 1);
    playerService.save(receiver);

    return !receiver.isLoggedIn()
        && StringUtils.isNotEmpty(receiver.getEmail())
        && sendNotificationMail(resourceBundle.getString("new_unread_message"), receiver, sender);
  }
}
