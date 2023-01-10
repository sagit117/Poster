package ru.axel.poster;

import org.apache.commons.mail.EmailException;

public class Main {
    public static void main(String[] args) throws EmailException, InterruptedException {
        final IPoster poster = Poster.getInstance()
            .setHostName("smtp.mail.ru")
            .setSmtpPort(465)
            .setAuthenticator("zerozone.mailer@mail.ru", "")
            .setSSLOnConnect(true)
            .setFrom("zerozone.mailer@mail.ru")
            .setCharSet("utf-8")
            .setDebug(true)
            .setDelay(1000L);
        poster.build();

        poster.addQueue(
            new EmailMessage(
                "test subj",
                "<h1>Text H1</h1>",
                new String[]{"sagit117@gmail.com", "sagit117+1@gmail.com"}
            )
        );

        poster.addQueue(
            new EmailMessage(
                "test2 subj",
                "<h1>Text2 H1</h1>",
                new String[]{"sagit117@gmail.com", "sagit117+1@gmail.com"}
            )
        );


        Thread.sleep(5000L);
        poster.stop();
        Thread.sleep(5000L);

        poster.addQueue(
            new EmailMessage(
                "test stop",
                "<h1>Text STOP H1</h1>",
                new String[]{"sagit117@gmail.com", "sagit117+1@gmail.com"}
            )
        );

        Thread.sleep(5000L);
    }
}