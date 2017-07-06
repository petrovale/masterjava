package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;
import org.xml.sax.SAXException;
import ru.javaops.masterjava.xml.schema2.*;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainXmlTest2 {
    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    public static void main(String[] args) throws IOException, JAXBException, SAXException {
        String project = "masterjava";
        Payload payload = JAXB_PARSER.unmarshal(Resources.getResource(MainXmlTest2.class, "/payload.xml").openStream());
        String strPayload = JAXB_PARSER.marshal(payload);
        JAXB_PARSER.validate(strPayload);

        List<Object> groups = payload.getProjects().getProject()
                .stream().filter(p->p.getName().equals(project)).findFirst().get().getGroups().getGroup()
                .stream().map(g->g.getName()).collect(Collectors.toList());

        List<User> users = payload.getUsers().getUser();

        List<String> listFullName = new ArrayList<>();

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
