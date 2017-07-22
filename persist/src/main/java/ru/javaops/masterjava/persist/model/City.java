package ru.javaops.masterjava.persist.model;

import lombok.*;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class City extends BaseEntity {
    private @NonNull String name;

    public City(Integer id, String name) {
        this(name);
        this.id=id;
    }
}
