package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.ProjectTestData;
import ru.javaops.masterjava.persist.model.Project;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static ru.javaops.masterjava.persist.ProjectTestData.PROJECTS;


public class ProjectDaoTest extends AbstractDaoTest<ProjectDao> {

    public ProjectDaoTest() {
        super(ProjectDao.class);
    }

    @Before
    public void setUp() throws Exception {
        ProjectTestData.setUp();
    }

    @Test
    public void getAll() {
        final Map<String, Project> projects = dao.getAsMap();
        assertEquals(PROJECTS, projects);
        System.out.println(projects.values());
    }
}
