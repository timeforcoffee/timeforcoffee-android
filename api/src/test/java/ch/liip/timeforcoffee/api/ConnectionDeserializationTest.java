package ch.liip.timeforcoffee.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ch.liip.timeforcoffee.api.deserializers.DateDeserializer;
import ch.liip.timeforcoffee.backend.Connection;
import ch.liip.timeforcoffee.backend.Departure;

import static org.junit.Assert.assertEquals;

public class ConnectionDeserializationTest {

    private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateDeserializer())
            .create();

    @Test
    public void connectionsDeserialization_Works() throws UnsupportedEncodingException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("connections.json");
        InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");

        Type listType = new TypeToken<List<Connection>>(){}.getType();
        List<Connection> connections = gson.fromJson(reader, listType);

        assertEquals(5, connections.size());

        Connection connection1 = connections.get(0);
        assertEquals(8583274, connection1.getStation().getId());
        assertEquals("Sion, Jonction", connection1.getStation().getName());
        assertEquals((Double) 46.225724, (Double) connection1.getStation().getLocation().getX());
        assertEquals((Double) 7.336384, (Double) connection1.getStation().getLocation().getY());
        assertEquals("2018-06-21T10:50:00" , dateTimeFormatter.format(connection1.getTime().getScheduled()));
        assertEquals("2018-06-21T10:51:00" , dateTimeFormatter.format(connection1.getTime().getRealtime()));

        Connection connection2 = connections.get(1);
        assertEquals(8583269, connection2.getStation().getId());
        assertEquals("Sion, Furet", connection2.getStation().getName());
        assertEquals((Double) 46.224097, (Double) connection2.getStation().getLocation().getX());
        assertEquals((Double) 7.332564, (Double) connection2.getStation().getLocation().getY());
        assertEquals("2018-06-21T10:51:00" , dateTimeFormatter.format(connection2.getTime().getScheduled()));
        assertEquals("2018-06-21T10:52:00" , dateTimeFormatter.format(connection2.getTime().getRealtime()));

        Connection connection3 = connections.get(2);
        assertEquals(8583264, connection3.getStation().getId());
        assertEquals("Sion, Châteauneuf", connection3.getStation().getName());
        assertEquals((Double) 46.22203, (Double) connection3.getStation().getLocation().getX());
        assertEquals((Double) 7.328627, (Double) connection3.getStation().getLocation().getY());
        assertEquals("2018-06-21T10:52:00" , dateTimeFormatter.format(connection3.getTime().getScheduled()));
        assertEquals(null, connection3.getTime().getRealtime());

        Connection connection4 = connections.get(3);
        assertEquals(8583370, connection4.getStation().getId());
        assertEquals("Sion, Treille", connection4.getStation().getName());
        assertEquals((Double) 46.220681, (Double) connection4.getStation().getLocation().getX());
        assertEquals((Double) 7.324986, (Double) connection4.getStation().getLocation().getY());
        assertEquals("2018-06-21T10:53:00" , dateTimeFormatter.format(connection4.getTime().getScheduled()));
        assertEquals(null, connection4.getTime().getRealtime());

        Connection connection5 = connections.get(4);
        assertEquals(8583271, connection5.getStation().getId());
        assertEquals("Sion, Garenne", connection5.getStation().getName());
        assertEquals((Double) 46.219998, (Double) connection5.getStation().getLocation().getX());
        assertEquals((Double) 7.321669, (Double) connection5.getStation().getLocation().getY());
        assertEquals("2018-06-21T10:55:00" , dateTimeFormatter.format(connection5.getTime().getScheduled()));
        assertEquals(null, connection5.getTime().getRealtime());
    }
}
