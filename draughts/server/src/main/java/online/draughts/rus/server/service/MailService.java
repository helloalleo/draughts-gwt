package online.draughts.rus.server.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import online.draughts.rus.server.config.Config;
import online.draughts.rus.server.domain.GameMessage;
import online.draughts.rus.server.domain.Player;
import online.draughts.rus.server.util.Utils;
import online.draughts.rus.shared.util.StringUtils;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 26.11.15
 * Time: 20:26
 */
@Singleton
public class MailService {

  private final Logger logger = Logger.getLogger(MailService.class);

  private final PlayerService playerService;

  @Inject
  public MailService(PlayerService playerService) {
    this.playerService = playerService;
  }

  private boolean sendNotificationMail(String receivedMessage, Player receiver, Player sender) {
    // TODO Обработать отправку письма админам когда получатель или отправитель ранвы нал
    Properties props = new Properties();
    props.put("mail.transport.protocol", "smtp");
    if (Config.DEBUG) {
      props.put("mail.smtp.port", "1025");
    } else {
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.starttls.required", "true");
      props.put("mail.smtp.sasl.enable", "false");
    }

    Session mailSession = Session.getInstance(props, null);
    if (Config.DEBUG) {
      mailSession.setDebug(true);
    }

    try {
      MimeMessage message = new MimeMessage(mailSession);
      message.setSubject(String.format("Новое сообщение от %s", sender == null ? "не указано" : sender.getPublicName()), "UTF-8");
      message.setFrom(new InternetAddress(Config.FROM_EMAIL, "shashki.online", "UTF-8"));
      message.addRecipient(Message.RecipientType.TO,
          new InternetAddress(receiver == null ? Config.ADMIN_MAIL : receiver.getEmail(),
              receiver == null ? "Админ" : receiver.getPublicName(), "UTF-8"));

      //
      // This HTML mail have to 2 part, the BODY and the embedded image
      //
      MimeMultipart multipart = new MimeMultipart("alternative");

      // first part  (the html)
      BodyPart htmlMessagePart = new MimeBodyPart();
      String htmlText = Utils.readFile("mail/new_message/index.html");
      htmlText = htmlText.replace("{{0}}", sender.getPublicName());
      htmlText = htmlText.replace("{{1}}", receivedMessage);
      htmlMessagePart.setHeader("Content-Type", "text/plain; charset=UTF-8");
      htmlMessagePart.setHeader("Content-Transfer-Encoding", "quoted-printable");
      htmlMessagePart.setContent(htmlText, "text/html; charset=UTF-8");

      // add it
      multipart.addBodyPart(htmlMessagePart);

      // text part
      BodyPart textMessagePart = new MimeBodyPart();
      String text = Utils.readFile("mail/new_message/new_message.txt");
      text = text.replace("{{0}}", sender.getPublicName());
      text = text.replace("{{1}}", receivedMessage);
      textMessagePart.setHeader("Content-Type", "text/plain; charset=UTF-8");
      textMessagePart.setHeader("Content-Transfer-Encoding", "quoted-printable");
      textMessagePart.setContent(text, "text/plain; charset=UTF-8");

      multipart.addBodyPart(textMessagePart);

      // put everything together
      message.setContent(multipart);

      Transport.send(message);
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
    Player receiver = playerService.find(message.getReceiver().getId());
    Player sender = playerService.find(message.getSender().getId());

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

    return !(receiver.isLoggedIn() && receiver.isOnline())
        && receiver.isSubscribeOnNewsletter()
        && StringUtils.isNotEmpty(receiver.getEmail())
        && sendNotificationMail(message.getMessage(), receiver, sender);
  }

  public void sendToAdmins(GameMessage message) {
    Player receiver = null;
    if (message.getReceiver() != null) {
      receiver = playerService.find(message.getReceiver().getId());
    }
    Player sender = null;
    if (message.getSender() != null) {
      sender = playerService.find(message.getSender().getId());
    }

    // если получатель не имеет сообщений от данного отправителя, тогда инициализируем Map и увеличиваем счетчика
    // на единицу
    sendNotificationMail(message.getMessage(), receiver, sender);
  }
}
