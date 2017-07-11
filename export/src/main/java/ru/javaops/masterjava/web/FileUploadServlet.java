package ru.javaops.masterjava.web;

import ru.javaops.masterjava.xml.schema.FlagType;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class FileUploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Set<User> users = new TreeSet<>();
        Part filePart = request.getPart("file");
        try (InputStream is = filePart.getInputStream()) {
            users = processByStax(is);
            users.forEach(System.out::println);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        request.setAttribute("users", users);
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/upload.jsp").forward(request, response);
    }

    private static Set<User> processByStax(InputStream is) throws Exception {
        final Comparator<User> USER_COMPARATOR = Comparator.comparing(User::getValue).thenComparing(User::getEmail);

            StaxStreamProcessor processor = new StaxStreamProcessor(is);

            // Users loop
            Set<User> users = new TreeSet<>(USER_COMPARATOR);

            while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
                User user = new User();
                user.setEmail(processor.getAttribute("email"));
                user.setFlag(FlagType.fromValue(processor.getAttribute("flag")));
                user.setValue(processor.getText());
                users.add(user);
            }
            return users;
    }
}
