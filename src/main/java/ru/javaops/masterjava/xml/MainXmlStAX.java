package ru.javaops.masterjava.xml;

import ru.javaops.masterjava.xml.stax.Project;
import ru.javaops.masterjava.xml.stax.User;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.util.ArrayList;
import java.util.List;

public class MainXmlStAX {

    private String project;

    public MainXmlStAX(String project) {
        this.project = project;
    }

    public void getProjectWithUsers(XMLStreamReader reader) throws XMLStreamException {
        List<User> users = new ArrayList<>();
        List<Project> projects = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLEvent.START_ELEMENT) {
                if ("Users".equals(reader.getLocalName())) {
                    users = readUsers(reader);
                }
                if ("Projects".equals(reader.getLocalName())) {
                    projects = readProjects(reader);
                }
            }
        }

        List<String> groups = projects.stream().filter(p->p.getNameProject().equals(project)).findFirst().get().getGroups();

        System.out.println("Участники проекта " + project + ":");
        for (User user : users) {
            if (user.getGroups() != null) {
                if (user.getGroups().stream().anyMatch(userGroup->groups.contains(userGroup))) {
                    System.out.println("Email: " + user.getEmail() + " Name: " + user.getFullName());
                }

            }
        }
    }

    public List<User> readUsers(XMLStreamReader reader) throws XMLStreamException {
        List<User> users = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLEvent.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    if (elementName.equals("User"))
                        users.add(readUser(reader));
                    break;
                case XMLEvent.END_ELEMENT:
                    return users;
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

    private User readUser(XMLStreamReader reader) throws XMLStreamException {
        User user = new User("", "", null);
        user.setEmail(reader.getAttributeValue(null, "email"));

        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLEvent.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    if (elementName.equals("groups"))
                        user.setGroups(readGroups(reader));
                    else if (elementName.equals("fullName"))
                        user.setFullName(reader.getElementText());
                    break;
                case XMLEvent.END_ELEMENT:
                    return user;
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

    private List<String> readGroups(XMLStreamReader reader) throws XMLStreamException {
        List<String> groups = new ArrayList<>();
        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLEvent.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    if (elementName.equals("group"))
                        groups.add(readGroup(reader));
                    break;
                case XMLEvent.END_ELEMENT:
                    return groups;
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

    private String readGroup(XMLStreamReader reader) throws XMLStreamException {
        StringBuilder result = new StringBuilder();
        while (reader.hasNext()) {
            result.append(reader.getAttributeValue(null, "name"));
            int event = reader.next();
            if (event == XMLEvent.END_ELEMENT)
                return result.toString();
        }
        throw new XMLStreamException("Premature end of file");
    }

    public List<Project> readProjects(XMLStreamReader reader) throws XMLStreamException {
        List<Project> projects = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLEvent.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    if (elementName.equals("Project"))
                        projects.add(readProject(reader));
                    break;
                case XMLEvent.END_ELEMENT:
                    return projects;
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

    private Project readProject(XMLStreamReader reader) throws XMLStreamException {
        Project project = new Project("", "", null);

        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLEvent.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    if (elementName.equals("groups"))
                        project.setGroups(readGroups(reader));
                    if (elementName.equals("description"))
                        project.setDescription(reader.getElementText());
                    else if (elementName.equals("name"))
                        project.setNameProject(reader.getElementText());
                    break;
                case XMLEvent.END_ELEMENT:
                    return project;
            }
        }
        throw new XMLStreamException("Premature end of file");
    }
}
