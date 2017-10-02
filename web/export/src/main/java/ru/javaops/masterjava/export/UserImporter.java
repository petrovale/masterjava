package ru.javaops.masterjava.export;

import com.google.common.base.Splitter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import one.util.streamex.StreamEx;
import ru.javaops.masterjava.export.PayloadImporter.FailedEmail;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.dao.UserGroupDao;
import ru.javaops.masterjava.persist.model.*;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * gkislin
 * 14.10.2016
 */
@Slf4j
public class UserImporter {

    private static final int NUMBER_THREADS = 4;
    private final ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_THREADS);
    private final UserDao userDao = DBIProvider.getDao(UserDao.class);
    private UserGroupDao userGroupDao = DBIProvider.getDao(UserGroupDao.class);

    public List<FailedEmail> process(StaxStreamProcessor processor, Map<String, Group> groups, Map<String, City> cities, int chunkSize) throws XMLStreamException {
        log.info("Start proseccing with chunkSize=" + chunkSize);

        @Value
        class ChunkItem {
            private User user;
            private StreamEx<UserGroup> userGroups;
        }

        return new Callable<List<FailedEmail>>() {
            class ChunkFuture {
                String emailRange;
                Future<List<String>> future;

                public ChunkFuture(List<User> chunk, Future<List<String>> future) {
                    this.future = future;
                    this.emailRange = chunk.get(0).getEmail();
                    if (chunk.size() > 1) {
                        this.emailRange += '-' + chunk.get(chunk.size() - 1).getEmail();
                    }
                }
            }

            @Override
            public List<FailedEmail> call() throws XMLStreamException {
                val futures = new ArrayList<ChunkFuture>();

                int id = userDao.getSeqAndSkip(chunkSize);
                List<ChunkItem> chunk = new ArrayList<>(chunkSize);
                val failed = new ArrayList<FailedEmail>();

                while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
                    final String email = processor.getAttribute("email");
                    String cityRef = processor.getAttribute("city");
                    City city = cities.get(cityRef);
                    if (city == null) {
                        failed.add(new FailedEmail(email, "City '" + cityRef + "' is not present in DB"));
                    } else {
                        val groupRefs = processor.getAttribute("groupRefs");
                        List<String> groupNames = (groupRefs == null) ?
                                Collections.emptyList() :
                                Splitter.on(' ').splitToList(groupRefs);

                        if (!groups.keySet().containsAll(groupNames)) {
                            failed.add(new FailedEmail(email, "One of group from '" + groupRefs + "' is not present in DB"));
                        } else {
                            final UserFlag flag = UserFlag.valueOf(processor.getAttribute("flag"));
                            final String fullName = processor.getText();
                            final User user = new User(id++, fullName, email, flag, city.getId());
                            StreamEx<UserGroup> userGroups = StreamEx.of(groupNames).map(name -> new UserGroup(user.getId(), groups.get(name).getId()));
                            chunk.add(new ChunkItem(user, userGroups));
                            if (chunk.size() == chunkSize) {
                                futures.add(submit(chunk));
                                chunk = new ArrayList<>(chunkSize);
                                id = userDao.getSeqAndSkip(chunkSize);
                            }
                        }
                    }
                }

                if (!chunk.isEmpty()) {
                    futures.add(submit(chunk));
                }

                futures.forEach(cf -> {
                    try {
                        failed.addAll(StreamEx.of(cf.future.get()).map(email -> new FailedEmail(email, "already present")).toList());
                        log.info(cf.emailRange + " successfully executed");
                    } catch (Exception e) {
                        log.error(cf.emailRange + " failed", e);
                        failed.add(new FailedEmail(cf.emailRange, e.toString()));
                    }
                });
                return failed;
            }

            private ChunkFuture submit(List<ChunkItem> chunk) {
                val users = StreamEx.of(chunk).map(ChunkItem::getUser).toList();
                ChunkFuture chunkFuture = new ChunkFuture(
                        users,
                        executorService.submit(() -> {
                            List<User> alreadyPresents = userDao.insertAndGetConflictEmails(users);
                            Set<Integer> alreadyPresentsIds = StreamEx.of(alreadyPresents).map(User::getId).toSet();
                            userGroupDao.insertBatch(
                                    StreamEx.of(chunk).flatMap(ChunkItem::getUserGroups)
                                            .filter(ug -> !alreadyPresentsIds.contains(ug.getUserId()))
                                            .toList()
                            );
                            return StreamEx.of(alreadyPresents).map(User::getEmail).toList();
                        })
                );
                log.info("Submit " + chunkFuture.emailRange);
                return chunkFuture;
            }
        }.call();
    }
}
