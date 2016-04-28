package online.draughts.rus.server.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 28.04.16
 * Time: 11:43
 */
@JsonIgnoreProperties(value = {"MessageAttributes"})
public class SNSMessage {
  @JsonProperty("Message")
  private String message;
  @JsonProperty("MessageId")
  private String messageId;
  @JsonProperty("Subject")
  private String subject;
  @JsonProperty("Timestamp")
  private String timestamp;
  @JsonProperty("TopicArn")
  private String topicArn;
  @JsonProperty("Type")
  private String type;
  @JsonProperty("SubscribeURL")
  private String subscribeURL;
  @JsonProperty("UnsubscribeURL")
  private String unsubscribeURL;
  @JsonProperty("Token")
  private String token;
  @JsonProperty("SigningCertURL")
  private String signingCertURL;
  @JsonProperty("Signature")
  private String signature;
  @JsonProperty("SignatureVersion")
  private String signatureVersion;

  @JsonCreator
  public SNSMessage(@JsonProperty("Message") String message,
                    @JsonProperty("MessageId") String messageId,
                    @JsonProperty("Subject") String subject,
                    @JsonProperty("Timestamp") String timestamp,
                    @JsonProperty("TopicArn") String topicArn,
                    @JsonProperty("Type") String type,
                    @JsonProperty("SubscribeURL") String subscribeURL,
                    @JsonProperty("UnsubscribeURL") String unsubscribeURL,
                    @JsonProperty("Token") String token,
                    @JsonProperty("SigningCertURL") String signingCertURL,
                    @JsonProperty("Signature") String signature,
                    @JsonProperty("SignatureVersion") String signatureVersion) {
    this.message = message;
    this.messageId = messageId;
    this.subject = subject;
    this.timestamp = timestamp;
    this.topicArn = topicArn;
    this.type = type;
    this.subscribeURL = subscribeURL;
    this.unsubscribeURL = unsubscribeURL;
    this.token = token;
    this.signingCertURL = signingCertURL;
    this.signature = signature;
    this.signatureVersion = signatureVersion;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getTopicArn() {
    return topicArn;
  }

  public void setTopicArn(String topicArn) {
    this.topicArn = topicArn;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getSubscribeURL() {
    return subscribeURL;
  }

  public void setSubscribeURL(String subscribeURL) {
    this.subscribeURL = subscribeURL;
  }

  public String getUnsubscribeURL() {
    return unsubscribeURL;
  }

  public void setUnsubscribeURL(String unsubscribeURL) {
    this.unsubscribeURL = unsubscribeURL;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getSigningCertURL() {
    return signingCertURL;
  }

  public void setSigningCertURL(String signingCertURL) {
    this.signingCertURL = signingCertURL;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public String getSignatureVersion() {
    return signatureVersion;
  }

  public void setSignatureVersion(String signatureVersion) {
    this.signatureVersion = signatureVersion;
  }
}
