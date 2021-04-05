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
import ch.liip.timeforcoffee.backend.Departure;
import ch.liip.timeforcoffee.backend.Departures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DepartureDeserializationTest {

    private final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateDeserializer())
            .create();

    @Test
    public void departuresDeserialization_Works() throws UnsupportedEncodingException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("departures.json");
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        Departures departures = gson.fromJson(reader, Departures.class);

        assertEquals(3, departures.getDepartures().size());

        Departure departure1 = departures.getDepartures().get(0);
        assertEquals(8583270, departure1.getId());
        assertEquals("Sion, Gare Bus Sédunois", departure1.getTo());
        assertEquals("2018-06-21T10:21:00" , dateTimeFormatter.format(departure1.getDeparture().getScheduled()));
        assertEquals("2018-06-21T10:22:00" , dateTimeFormatter.format(departure1.getDeparture().getRealtime()));
        assertEquals("2018-06-21T10:36:00" , dateTimeFormatter.format(departure1.getArrival().getScheduled()));
        assertEquals("2018-06-21T10:37:00" , dateTimeFormatter.format(departure1.getArrival().getRealtime()));
        assertEquals("1", departure1.getName());
        assertNull(departure1.getPlatform());
        assertEquals("#000000", departure1.getColors().getFg());
        assertEquals("#FFFFFF", departure1.getColors().getBg());

        Departure departure2 = departures.getDepartures().get(1);
        assertEquals(8583271, departure2.getId());
        assertEquals("Sion, Garenne", departure2.getTo());
        assertEquals("2018-06-21T10:50:00" , dateTimeFormatter.format(departure2.getDeparture().getScheduled()));
        assertEquals("2018-06-21T10:51:00" , dateTimeFormatter.format(departure2.getDeparture().getRealtime()));
        assertEquals("2018-06-21T10:55:00" , dateTimeFormatter.format(departure2.getArrival().getScheduled()));
        assertEquals("2018-06-21T10:56:00" , dateTimeFormatter.format(departure2.getArrival().getRealtime()));
        assertEquals("1", departure2.getName());
        assertNull(departure2.getPlatform());
        assertEquals("#000000", departure2.getColors().getFg());
        assertEquals("#FFFFFF", departure2.getColors().getBg());

        Departure departure3 = departures.getDepartures().get(2);
        assertEquals(8583270, departure3.getId());
        assertEquals("Sion, Gare Bus Sédunois", departure3.getTo());
        assertEquals("2018-06-21T11:00:00" , dateTimeFormatter.format(departure3.getDeparture().getScheduled()));
        assertNull(departure3.getDeparture().getRealtime());
        assertEquals("2018-06-21T11:15:00" , dateTimeFormatter.format(departure3.getArrival().getScheduled()));
        assertNull(departure3.getArrival().getRealtime());
        assertEquals("1", departure3.getName());
        assertNull(departure3.getPlatform());
        assertEquals("#000000", departure3.getColors().getFg());
        assertEquals("#FFFFFF", departure3.getColors().getBg());
    }
}
