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
import ch.liip.timeforcoffee.backend.Departure;
import ch.liip.timeforcoffee.backend.Station;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DepartureDeserializationTest {

    private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateDeserializer())
            .create();

    @Test
    public void departuresDeserialization_Works() throws UnsupportedEncodingException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("departures.json");
        InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");

        Type listType = new TypeToken<List<Departure>>(){}.getType();
        List<Departure> departures = gson.fromJson(reader, listType);

        assertEquals(3, departures.size());

        Departure departure1 = departures.get(0);
        assertEquals(8583270, departure1.getDestination().getId());
        assertEquals("Sion, Gare Bus Sédunois", departure1.getDestination().getName());
        assertEquals("2018-06-21T10:21:00" , dateTimeFormatter.format(departure1.getDeparture().getScheduled()));
        assertEquals("2018-06-21T10:22:00" , dateTimeFormatter.format(departure1.getDeparture().getRealtime()));
        assertEquals("2018-06-21T10:36:00" , dateTimeFormatter.format(departure1.getArrival().getScheduled()));
        assertEquals("2018-06-21T10:37:00" , dateTimeFormatter.format(departure1.getArrival().getRealtime()));
        assertEquals("1", departure1.getLine());
        assertEquals(null, departure1.getPlatform());
        assertEquals("#000000", departure1.getColor().getFg());
        assertEquals("#FFFFFF", departure1.getColor().getBg());

        Departure departure2 = departures.get(1);
        assertEquals(8583271, departure2.getDestination().getId());
        assertEquals("Sion, Garenne", departure2.getDestination().getName());
        assertEquals("2018-06-21T10:50:00" , dateTimeFormatter.format(departure2.getDeparture().getScheduled()));
        assertEquals("2018-06-21T10:51:00" , dateTimeFormatter.format(departure2.getDeparture().getRealtime()));
        assertEquals("2018-06-21T10:55:00" , dateTimeFormatter.format(departure2.getArrival().getScheduled()));
        assertEquals("2018-06-21T10:56:00" , dateTimeFormatter.format(departure2.getArrival().getRealtime()));
        assertEquals("1", departure2.getLine());
        assertEquals(null, departure2.getPlatform());
        assertEquals("#000000", departure2.getColor().getFg());
        assertEquals("#FFFFFF", departure2.getColor().getBg());

        Departure departure3 = departures.get(2);
        assertEquals(8583270, departure3.getDestination().getId());
        assertEquals("Sion, Gare Bus Sédunois", departure3.getDestination().getName());
        assertEquals("2018-06-21T11:00:00" , dateTimeFormatter.format(departure3.getDeparture().getScheduled()));
        assertEquals(null , departure3.getDeparture().getRealtime());
        assertEquals("2018-06-21T11:15:00" , dateTimeFormatter.format(departure3.getArrival().getScheduled()));
        assertEquals(null , departure3.getArrival().getRealtime());
        assertEquals("1", departure3.getLine());
        assertEquals(null, departure3.getPlatform());
        assertEquals("#000000", departure3.getColor().getFg());
        assertEquals("#FFFFFF", departure3.getColor().getBg());
    }
}
