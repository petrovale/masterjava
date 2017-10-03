package ru.javaops.masterjava.service.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * gkislin
 * 15.11.2016
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Addressee {
    private String email;
    private String name;

    public Addressee(String email) {
        email = email.trim();
        int idx = email.indexOf('<');
        if (idx == -1) {
            this.email = email;
        } else {
            this.name = email.substring(0, idx).trim();
            this.email = email.substring(idx + 1, email.length() - 1).trim();
        }
    }

    @Override
    public String toString() {
        return name == null ? email : name + " <" + email + '>';
    }
}
