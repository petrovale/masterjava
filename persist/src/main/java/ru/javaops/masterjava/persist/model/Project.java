package ru.javaops.masterjava.persist.model;

import lombok.*;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class Project extends BaseEntity {
    private @NonNull String name;
    private @NonNull String description;

    public Project(Integer id, String name, String description) {
        this(name, description);
        this.id=id;
    }
}