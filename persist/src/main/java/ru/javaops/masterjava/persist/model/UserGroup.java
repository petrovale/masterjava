package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class UserGroup {
    private @NonNull @Column("user_id") Integer userId;
    private @NonNull @Column("group_id") Integer groupId;
}
