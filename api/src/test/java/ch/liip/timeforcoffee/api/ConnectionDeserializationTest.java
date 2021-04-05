package ch.liip.timeforcoffee.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import ch.liip.timeforcoffee.api.deserializers.DateDeserializer;
import ch.liip.timeforcoffee.backend.Connection;
import ch.liip.timeforcoffee.backend.Connections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ConnectionDeserializationTest {

    private final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateDeserializer())
            .create();

    @Test
    public void connectionsDeserialization_Works() throws UnsupportedEncodingException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("connections.json");
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        Connections connections = gson.fromJson(reader, Connections.class);

        assertEquals(1, connections.getConnections().size());
        assertEquals(5, connections.getConnections().get(0).size());

        Connection connection1 = connections.getConnections().get(0).get(0);
        assertEquals(8583274, connection1.getId());
        assertEquals("Sion, Jonction", connection1.getName());
        assertEquals((Double) 46.225724, (Double) connection1.getLocation().getLatitude());
        assertEquals((Double) 7.336384, (Double) connection1.getLocation().getLongitude());
        assertEquals("2018-06-21T10:50:00" , dateTimeFormatter.format(connection1.getDeparture().getScheduled()));
        assertEquals("2018-06-21T10:51:00" , dateTimeFormatter.format(connection1.getDeparture().getRealtime()));
        assertEquals("2018-06-21T10:50:00" , dateTimeFormatter.format(connection1.getArrival().getScheduled()));
        assertEquals("2018-06-21T10:51:00" , dateTimeFormatter.format(connection1.getArrival().getRealtime()));

        Connection connection2 = connections.getConnections().get(0).get(1);
        assertEquals(8583269, connection2.getId());
        assertEquals("Sion, Furet", connection2.getName());
        assertEquals((Double) 46.224097, (Double) connection2.getLocation().getLatitude());
        assertEquals((Double) 7.332564, (Double) connection2.getLocation().getLongitude());
        assertEquals("2018-06-21T10:51:00" , dateTimeFormatter.format(connection2.getDeparture().getScheduled()));
        assertEquals("2018-06-21T10:52:00" , dateTimeFormatter.format(connection2.getDeparture().getRealtime()));
        assertEquals("2018-06-21T10:51:00" , dateTimeFormatter.format(connection2.getArrival().getScheduled()));
        assertEquals("2018-06-21T10:52:00" , dateTimeFormatter.format(connection2.getArrival().getRealtime()));

        Connection connection3 = connections.getConnections().get(0).get(2);
        assertEquals(8583264, connection3.getId());
        assertEquals("Sion, Châteauneuf", connection3.getName());
        assertEquals((Double) 46.22203, (Double) connection3.getLocation().getLatitude());
        assertEquals((Double) 7.328627, (Double) connection3.getLocation().getLongitude());
        assertEquals("2018-06-21T10:52:00" , dateTimeFormatter.format(connection3.getDeparture().getScheduled()));
        assertNull(connection3.getDeparture().getRealtime());
        assertEquals("2018-06-21T10:52:00" , dateTimeFormatter.format(connection3.getArrival().getScheduled()));
        assertNull(connection3.getArrival().getRealtime());

        Connection connection4 = connections.getConnections().get(0).get(3);
        assertEquals(8583370, connection4.getId());
        assertEquals("Sion, Treille", connection4.getName());
        assertEquals((Double) 46.220681, (Double) connection4.getLocation().getLatitude());
        assertEquals((Double) 7.324986, (Double) connection4.getLocation().getLongitude());
        assertEquals("2018-06-21T10:53:00" , dateTimeFormatter.format(connection4.getDeparture().getScheduled()));
        assertNull(connection4.getDeparture().getRealtime());
        assertEquals("2018-06-21T10:53:00" , dateTimeFormatter.format(connection4.getArrival().getScheduled()));
        assertNull(connection4.getArrival().getRealtime());

        Connection connection5 = connections.getConnections().get(0).get(4);
        assertEquals(8583271, connection5.getId());
        assertEquals("Sion, Garenne", connection5.getName());
        assertEquals((Double) 46.219998, (Double) connection5.getLocation().getLatitude());
        assertEquals((Double) 7.321669, (Double) connection5.getLocation().getLongitude());
        assertEquals("2018-06-21T10:55:00" , dateTimeFormatter.format(connection5.getDeparture().getScheduled()));
        assertNull( connection5.getDeparture().getRealtime());
        assertEquals("2018-06-21T10:55:00" , dateTimeFormatter.format(connection5.getArrival().getScheduled()));
        assertNull( connection5.getArrival().getRealtime());
    }
}
