package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import one.util.streamex.StreamEx;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.Project;

import java.util.List;
import java.util.Map;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class ProjectDao implements AbstractDao {
    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("TRUNCATE project CASCADE")
    @Override
    public abstract void clean();

    @SqlQuery("SELECT * FROM project ORDER BY name")
    public abstract List<Project> getAll();

    public Map<String, Project> getAsMap() {
        return StreamEx.of(getAll()).toMap(Project::getName, g -> g);
    }

    @SqlUpdate("INSERT INTO project (name, description) VALUES (:name, :description)")
    @GetGeneratedKeys
    public abstract int insertGeneratedId(@BindBean Project project);

    public void insert(Project project) {
        int id = insertGeneratedId(project);
        project.setId(id);
    }
}
