package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;
import java.util.Map;

public class CityTestData {
    public static final City SPB = new City("spb", "Санкт-Петербург");
    public static final City MOV = new City("mow","Москва");
    public static final City KIV = new City("kiv", "Киев");
    public static final City MNSK = new City("mnsk", "Минск");

    public static final Map<String, City> CITIES = ImmutableMap.of(
            KIV.getRef(), KIV,
            MNSK.getRef(), MNSK,
            MOV.getRef(), MOV,
            SPB.getRef(), SPB);

    public static void setUp() {
        CityDao dao = DBIProvider.getDao(CityDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            CITIES.values().forEach(dao::insert);
        });
    }
}

