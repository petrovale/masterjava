package ru.javaops.masterjava.xml.util;

import org.junit.Test;
import ru.javaops.masterjava.xml.MainXml;

public class MainXmlTest {

    @Test
    public void testMainXml() throws Exception {
        MainXml mainXml = new MainXml("masterjava");
        mainXml.getUserNames();
    }
}
