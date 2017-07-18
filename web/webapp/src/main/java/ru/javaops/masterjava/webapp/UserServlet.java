package ru.javaops.masterjava.webapp;

import org.thymeleaf.context.WebContext;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ru.javaops.masterjava.webapp.ThymeleafListener.engine;

@WebServlet("/")
public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale());

        UserDao dao = DBIProvider.getDao(UserDao.class);
        List<User> users = dao.getWithLimit(20);

        webContext.setVariable("users", users);
        engine.process("webapp", webContext, resp.getWriter());
    }
}
