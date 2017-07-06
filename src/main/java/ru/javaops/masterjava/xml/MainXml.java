package ru.javaops.masterjava.xml;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema2.ObjectFactory;
import ru.javaops.masterjava.xml.schema2.Payload;
import ru.javaops.masterjava.xml.schema2.User;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainXml {

    private List<String> listFullName;
    private String project;

    public MainXml(String project) throws JAXBException, IOException {
        this.project = project;
    }

    public void getUserNames() throws Exception {
        final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
        Payload payload = JAXB_PARSER.unmarshal(Resources.getResource(MainXml.class, "/payload.xml").openStream());
        String strPayload = JAXB_PARSER.marshal(payload);
        JAXB_PARSER.validate(strPayload);

        List<Object> groups = payload.getProjects().getProject()
                .stream().filter(p->p.getName().equals(project)).findFirst().get().getGroups().getGroup()
                .stream().map(g->g.getName()).collect(Collectors.toList());

        List<User> users = payload.getUsers().getUser();

        listFullName = new ArrayList<>();

        for (User user : users) {
            if (user.getGroups() != null) {
                if (user.getGroups().getGroup().stream().anyMatch(userGroup->userGroup != null && groups.contains(userGroup.getName()))) {
                    listFullName.add(user.getFullName());
                }

            }
        }

        System.out.println(listFullName.stream().sorted().collect(Collectors.toList()));
    }
}
