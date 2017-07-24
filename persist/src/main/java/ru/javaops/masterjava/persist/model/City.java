package ru.javaops.masterjava.persist.model;

import lombok.*;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class City extends BaseEntity {
    private @NonNull String ref;
    private @NonNull String name;

    public City(Integer id, String ref, String name) {
        this(ref, name);
        this.id=id;
    }
}
