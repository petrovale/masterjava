package ru.javaops.masterjava.persist;

import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectTestData {
    public static final int TOPJAVA_ID = 100000;
    public static final int MASTERJAVA_ID = TOPJAVA_ID + 1;

    public static Project TOPJAVA;
    public static Project MASTERJAVA;
    public static List<Project> FIST2_PROJECTS;

    public static Group TOPJAVA06;
    public static Group TOPJAVA07;
    public static Group TOPJAVA08;
    public static Group MASTERJAVA01;

    public static void init() {
        TOPJAVA06 = new Group("topjava06", GroupType.finished, TOPJAVA_ID);
        TOPJAVA07 = new Group("topjava07", GroupType.finished, TOPJAVA_ID);
        TOPJAVA08 = new Group("topjava08", GroupType.current, TOPJAVA_ID);

        MASTERJAVA01 = new Group("masterjava01", GroupType.current, MASTERJAVA_ID);

        TOPJAVA = new Project("topjava", "Topjava", new ArrayList<>(Arrays.asList(TOPJAVA06, TOPJAVA07, TOPJAVA08)));
        MASTERJAVA = new Project("masterjava", "Masterjava", new ArrayList<>(Arrays.asList(MASTERJAVA01)));
        FIST2_PROJECTS = new ArrayList<>(Arrays.asList(MASTERJAVA, TOPJAVA));
    }

    public static void setUp() {
        ProjectDao dao = DBIProvider.getDao(ProjectDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            FIST2_PROJECTS.forEach(dao::insert);
        });
    }
}
