package ru.axel.poster;

import org.apache.commons.mail.EmailException;

public interface IPoster {
    IPoster setDelay(long delay);

    IPoster setAltMessage(String message);

    IPoster setHostName(String hostName);

    IPoster setSmtpPort(int smtpPort);

    IPoster setAuthenticator(String user, String password);

    IPoster setSSLOnConnect(boolean sslOnConnect);

    IPoster setFrom(String from) throws EmailException;

    IPoster setCharSet(String charSet);

    IPoster setDebug(boolean debug);

    void addQueue(EmailMessage emailMessage);

    void build();

    void stop();
}
