package ru.javaops.masterjava.xml.stax;

import java.util.List;

public class User {
    private String email;
    private String fullName;
    private List<String> groups;

    public User(String email, String fullName, List<String> groups) {
        this.email = email;
        this.fullName = fullName;
        this.groups = groups;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", groups=" + groups +
                '}';
    }
}
