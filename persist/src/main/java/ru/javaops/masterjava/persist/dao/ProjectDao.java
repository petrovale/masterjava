package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.exceptions.TransactionFailedException;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.Project;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class ProjectDao implements AbstractDao {

    public Project insert(Project project) {
        if (project.isNew()) {
            int id = insertGeneratedId(project);
            project.setId(id);
            if (project.getGroups()!= null) {
                insertGroupsForProject(project.getName(), project.getGroups());
            }
        } else {
            insertWitId(project);
        }
        return project;
    }

    @Transaction
    public void insertGroupsForProject(String nameProject, List<Group> groups) {
        int projectId = getProjectId(nameProject);

        if (projectId == 0) {
            throw new TransactionFailedException("No project found");
        }

        for (Group group : groups) {
            group.setProjectId(projectId);
            insertGroup(group);
        }
    }

    @SqlUpdate("INSERT INTO groups (name, type, project_id) VALUES (:name, CAST(:type AS GROUP_TYPE), :projectId)")
    @GetGeneratedKeys
    abstract int insertGroup(@BindBean Group group);

    @SqlQuery("SELECT * FROM groups WHERE project_id = :it ORDER BY name")
    public abstract List<Group> getGroupsForProject(@Bind int projectId);

    @SqlQuery("SELECT id FROM projects WHERE name = :name")
    abstract int getProjectId(@Bind("name") String name);

    @SqlUpdate("INSERT INTO projects (name, description) VALUES (:name, :description)")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean Project project);

    @SqlUpdate("INSERT INTO projects (id, full_name, email, flag) VALUES (:id, :fullName, :email, CAST(:flag AS USER_FLAG)) ")
    abstract void insertWitId(@BindBean Project project);

    @SqlQuery("SELECT * FROM projects ORDER BY name LIMIT :it")
    public abstract List<Project> getWithLimit(@Bind int limit);

    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("TRUNCATE projects CASCADE")
    @Override
    public abstract void clean();
}
