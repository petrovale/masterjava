package ru.javaops.masterjava.export;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.util.ArrayList;
import java.util.Map;

/**
 * gkislin
 * 15.11.2016
 */
@Slf4j
public class CityImporter {
    private final CityDao cityDao = DBIProvider.getDao(CityDao.class);
    public Map<String, City> process(StaxStreamProcessor processor) throws XMLStreamException {
        val map = cityDao.getAsMap();
        val newCities = new ArrayList<City>();
        String element;

        while ((element = processor.doUntilAny(XMLEvent.START_ELEMENT, "City", "Users")) != null) {
            if (element.equals("Users")) break;
            val ref = processor.getAttribute("id");
            if (!map.containsKey(ref)) {
                newCities.add(new City(null, ref, processor.getText()));
            }
        }
        log.info("Insert batch " + newCities);
        cityDao.insertBatch(newCities);
        return cityDao.getAsMap();
    }
}