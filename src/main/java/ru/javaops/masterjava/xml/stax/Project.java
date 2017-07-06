package ru.javaops.masterjava.xml.stax;

import java.util.List;

public class Project {
    private String nameProject;
    private String description;
    private List<String> groups;

    public Project(String nameProject, String description, List<String> groups) {
        this.nameProject = nameProject;
        this.description = description;
        this.groups = groups;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNameProject() {
        return nameProject;
    }

    public void setNameProject(String nameProject) {
        this.nameProject = nameProject;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "Project{" +
                "nameProject='" + nameProject + '\'' +
                ", description='" + description + '\'' +
                ", groups=" + groups +
                '}';
    }
}
