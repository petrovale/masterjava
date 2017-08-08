package ru.javaops.masterjava.service.mail;

import com.typesafe.config.Config;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import ru.javaops.masterjava.config.Configs;

import javax.mail.Authenticator;
import java.nio.charset.StandardCharsets;

public class MailConfig {
    private static final MailConfig INSTANCE =
            new MailConfig(Configs.getConfig("mail.conf", "mail"));

    final private String host;
    final private int port;
    final private boolean useSSL;
    final private boolean useTLS;
    final private boolean debug;
    final private String username;
    final private Authenticator auth;
    final private String fromName;

    private MailConfig(Config config) {
        host = config.getString("host");
        port = config.getInt("port");
        username = config.getString("username");
        auth = new DefaultAuthenticator(username, config.getString("password"));
        useSSL = config.getBoolean("useSSL");
        useTLS = config.getBoolean("useTLS");
        debug = config.getBoolean("debug");
        fromName = config.getString("fromName");
    }

    public <T extends Email> T prepareEmail(T email) throws EmailException {
        email.setFrom(username, fromName);
        email.setHostName(host);
        if (useSSL) {
            email.setSslSmtpPort(String.valueOf(port));
        } else {
            email.setSmtpPort(port);
        }
        email.setSSLOnConnect(useSSL);
        email.setStartTLSEnabled(useTLS);
        email.setDebug(debug);
        email.setAuthenticator(auth);
        email.setCharset(StandardCharsets.UTF_8.name());
        return email;
    }

    public static HtmlEmail createHtmlEmail() throws EmailException {
        return INSTANCE.prepareEmail(new HtmlEmail());
    }

    @Override
    public String toString() {
        return "\nhost='" + host + '\'' +
                "\nport=" + port +
                "\nuseSSL=" + useSSL +
                "\nuseTLS=" + useTLS +
                "\ndebug=" + debug +
                "\nusername='" + username + '\'' +
                "\nfromName='" + fromName + '\'';
    }
}
