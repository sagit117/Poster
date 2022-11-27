package ru.axel.poster;

import org.apache.commons.mail.EmailException;

import javax.mail.MessagingException;

public interface Sender {
    String send(EmailMessage emailMessage) throws MessagingException, EmailException;
}
