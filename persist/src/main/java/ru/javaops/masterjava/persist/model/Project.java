package ru.javaops.masterjava.persist.model;

import lombok.*;

import java.util.List;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Project extends BaseEntity {
    private @NonNull String name;
    private @NonNull String description;
    private @NonNull List<Group> groups;

    public Project(Integer id, String name, String description, List<Group> groups) {
        this(name, description, groups);
        this.id=id;
    }
}