package ru.axel.poster;

import org.apache.commons.mail.EmailException;

import javax.mail.MessagingException;
import java.util.List;

public final class Mailer extends Thread {
    private final List<EmailMessage> messages;
    private final long delayTimeout;
    private final Sender senderMethod;
    private boolean isStop = false;

    public Mailer(List<EmailMessage> queue, long delay, Sender sender) {
        messages = queue;
        delayTimeout = delay;
        senderMethod = sender;

        this.setDaemon(true);
    }

    public void stopProcessing() {
        isStop = true;
    }

    @Override
    public void run() {
        while (!isStop) {
            try {
                Thread.sleep(delayTimeout);

                if (messages.size() > 0) {
                    EmailMessage first = messages.get(0);
                    senderMethod.send(first);

                    messages.remove(0);
                }
            } catch (InterruptedException | MessagingException | EmailException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
