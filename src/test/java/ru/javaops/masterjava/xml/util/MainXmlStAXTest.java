package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;
import org.junit.Test;
import ru.javaops.masterjava.xml.MainXmlStAX;

import javax.xml.stream.XMLStreamReader;

public class MainXmlStAXTest {

    @Test
    public void readUsers() throws Exception {
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {
            XMLStreamReader reader = processor.getReader();

            MainXmlStAX mainXmlStAX = new MainXmlStAX("masterjava");
            mainXmlStAX.getProjectWithUsers(reader);
        }
    }
}
