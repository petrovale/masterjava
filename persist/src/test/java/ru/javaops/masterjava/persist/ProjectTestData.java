package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableMap;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ProjectTestData {

    public static Project TOPJAVA = new Project("topjava", "Topjava");
    public static Project MASTERJAVA = new Project("masterjava", "Masterjava");
    public static final Map<String, Project> PROJECTS = ImmutableMap.of(
            TOPJAVA.getName(), TOPJAVA,
            MASTERJAVA.getName(), MASTERJAVA);

    public static int TOPJAVA_ID;
    public static int MASTERJAVA_ID;

    public static void setUp() {
        ProjectDao dao = DBIProvider.getDao(ProjectDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            PROJECTS.values().forEach(dao::insert);
        });
        TOPJAVA_ID = TOPJAVA.getId();
        MASTERJAVA_ID = MASTERJAVA.getId();
    }
}
