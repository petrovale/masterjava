package ru.javaops.masterjava;

import com.google.common.base.Splitter;
import com.google.common.io.Resources;
import j2html.tags.ContainerTag;
import one.util.streamex.StreamEx;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.schema.Payload;
import ru.javaops.masterjava.xml.schema.Project;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Strings.nullToEmpty;
import static j2html.TagCreator.*;


/**
 * User: gkislin
 */
public class MainXml {
    private static final Comparator<User> USER_COMPARATOR = Comparator.comparing(User::getValue).thenComparing(User::getEmail);

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Format: projectName");
            System.exit(1);
        }
        String projectName = args[0];
        URL payloadUrl = Resources.getResource("payload.xml");

        Set<User> users = parseByJaxb(projectName, payloadUrl);
        users.forEach(System.out::println);

        String html = toHtml(users, projectName);
        System.out.println(html);
        try (Writer writer = Files.newBufferedWriter(Paths.get("out/users.html"))) {
            writer.write(html);
        }

        System.out.println();
        users = processByStax(projectName, payloadUrl);
        users.forEach(System.out::println);
    }

    private static Set<User> parseByJaxb(String projectName, URL payloadUrl) throws Exception {
        JaxbParser parser = new JaxbParser(ObjectFactory.class);
        parser.setSchema(Schemas.ofClasspath("payload.xsd"));
        Payload payload;
        try (InputStream is = payloadUrl.openStream()) {
            payload = parser.unmarshal(is);
        }

        Project project = StreamEx.of(payload.getProjects().getProject())
                .filter(p -> p.getName().equals(projectName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Invalid project name '" + projectName + '\''));

        final Set<Project.Group> groups = new HashSet<>(project.getGroup());  // identity compare
        return StreamEx.of(payload.getUsers().getUser())
                .filter(u -> !Collections.disjoint(groups, u.getGroupRefs()))
                .collect(Collectors.toCollection(() -> new TreeSet<>(USER_COMPARATOR)));
    }

    private static Set<User> processByStax(String projectName, URL payloadUrl) throws Exception {

        try (InputStream is = payloadUrl.openStream()) {
            StaxStreamProcessor processor = new StaxStreamProcessor(is);
            final Set<String> groupNames = new HashSet<>();

            // Projects loop
            projects:
            while (processor.doUntil(XMLEvent.START_ELEMENT, "Project")) {
                if (projectName.equals(processor.getAttribute("name"))) {
                    // Groups loop
                    String element;
                    while ((element = processor.doUntilAny(XMLEvent.START_ELEMENT, "Project", "Group", "Users")) != null) {
                        if (!element.equals("Group")) {
                            break projects;
                        }
                        groupNames.add(processor.getAttribute("name"));
                    }
                }
            }
            if (groupNames.isEmpty()) {
                throw new IllegalArgumentException("Invalid " + projectName + " or no groups");
            }

            // Users loop
            Set<User> users = new TreeSet<>(USER_COMPARATOR);

            while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
                String groupRefs = processor.getAttribute("groupRefs");
                if (!Collections.disjoint(groupNames, Splitter.on(' ').splitToList(nullToEmpty(groupRefs)))) {
                    User user = new User();
                    user.setEmail(processor.getAttribute("email"));
                    user.setValue(processor.getText());
                    users.add(user);
                }
            }
            return users;
        }
    }

    private static String toHtml(Set<User> users, String projectName) {
        final ContainerTag table = table().with(
                tr().with(th("FullName"), th("email")))
                .attr("border", "1")
                .attr("cellpadding", "8")
                .attr("cellspacing", "0");

        users.forEach(u -> table.with(tr().with(td(u.getValue()), td(u.getEmail()))));

        return html().with(
                head().with(title(projectName + " users")),
                body().with(h1(projectName + " users"), table)
        ).render();
    }
}
