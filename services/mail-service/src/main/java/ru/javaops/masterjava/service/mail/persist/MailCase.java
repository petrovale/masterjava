package ru.javaops.masterjava.service.mail.persist;

import com.bertoncelj.jdbi.entitymapper.Column;
import com.google.common.base.Joiner;
import lombok.*;
import ru.javaops.masterjava.persist.model.BaseEntity;
import ru.javaops.masterjava.service.mail.Addressee;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode // compare without id
@ToString
public class MailCase extends BaseEntity {
    private @Column("list_to") String listTo;
    private @Column("list_cc") String listCc;
    private String subject;
    private String body;
    private String state;
    private Date date;

    public static MailCase of(List<Addressee> to, List<Addressee> cc, String subject, String body, String state){
        return new MailCase(Joiner.on(", ").join(to), Joiner.on(", ").join(cc), subject, body, state, new Date());
    }
}