package ru.axel.poster;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

public final class Poster implements IPoster {
    private final static IPoster instance = new Poster();
    private final List<EmailMessage> queue = new ArrayList<>();
    private final HtmlEmail htmlEmail = new HtmlEmail();
    private String charset = "utf-8";
    private long delayTimeout = 1000;
    private Mailer mailer;
    private String altMsg = "Your email client does not support HTML messages";

    private Poster() {
    }

    public static IPoster getInstance() {
        return Poster.instance;
    }

    @Override
    public IPoster setDelay(long delay) {
        delayTimeout = delay;
        return instance;
    }

    @Override
    public IPoster setAltMessage(String message) {
        altMsg = message;
        return instance;
    }

    @Override
    public IPoster setHostName(String hostName) {
        htmlEmail.setHostName(hostName);
        return instance;
    }

    @Override
    public IPoster setSmtpPort(int smtpPort) {
        htmlEmail.setSmtpPort(smtpPort);
        return instance;
    }

    @Override
    public IPoster setAuthenticator(String user, char[] password) {
        DefaultAuthenticator authenticator = new DefaultAuthenticator(user, String.valueOf(password));
        htmlEmail.setAuthenticator(authenticator);
        return instance;
    }

    @Override
    public IPoster setSSLOnConnect(boolean sslOnConnect) {
        htmlEmail.setSSLOnConnect(sslOnConnect);
        return instance;
    }

    @Override
    public IPoster setFrom(String from) throws EmailException {
        htmlEmail.setFrom(from);
        return instance;
    }

    @Override
    public IPoster setCharSet(String charSet) {
        htmlEmail.setCharset(charSet);
        charset = charSet;
        return instance;
    }

    @Override
    public IPoster setDebug(boolean debug) {
        htmlEmail.setDebug(debug);
        return instance;
    }

    @Override
    public void addQueue(EmailMessage emailMessage) {
        queue.add(emailMessage);
    }

    @Override
    public void build() {
        mailer = new Mailer(queue, delayTimeout, this::send);
        mailer.start();
    }

    @Override
    public void stop() {
        mailer.stopProcessing();
    }

    private String send(EmailMessage message) throws EmailException, MessagingException {
        if (htmlEmail.getMimeMessage() == null) {
            htmlEmail.setSubject(message.subject());
            htmlEmail.setMsg(message.msgHtml());
            htmlEmail.setTextMsg(altMsg);
            htmlEmail.addTo(message.emails());

            htmlEmail.buildMimeMessage();
        } else {
            htmlEmail.getMimeMessage().setSubject(message.subject());
            htmlEmail.getMimeMessage().setContent(message.msgHtml(), "text/html; charset=" + charset);
//            htmlEmail.getMimeMessage().setText(message.altMsgText(), charset); // заменяет setContent

            htmlEmail.getMimeMessage().setRecipients(Message.RecipientType.TO, new Address[]{});
            for(String email : message.emails()) {
                htmlEmail.getMimeMessage().addRecipients(Message.RecipientType.TO, email);
            }

            htmlEmail.getMimeMessage().saveChanges();
        }

        return htmlEmail.sendMimeMessage();
    }
}
