package ch.liip.timeforcoffee.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import ch.liip.timeforcoffee.api.deserializers.DateDeserializer;
import ch.liip.timeforcoffee.backend.Station;

import static org.junit.Assert.assertEquals;

public class StationDeserializationTest {

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateDeserializer())
            .create();

    @Test
    public void stationsForLocationDeserialization_Works() throws UnsupportedEncodingException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("stations_for_location.json");
        InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");

        Type listType = new TypeToken<List<Station>>(){}.getType();
        List<Station> stations = gson.fromJson(reader, listType);

        assertEquals(4, stations.size());

        Station station1 = stations.get(0);
        assertEquals(8589154, station1.getId());
        assertEquals("Fribourg, Industrie", station1.getName());
        assertEquals((Double) 46.796343, (Double) station1.getLocation().getX());
        assertEquals((Double)7.155319, (Double) station1.getLocation().getY());

        Station station2 = stations.get(1);
        assertEquals(8588858, station2.getId());
        assertEquals("Fribourg, J. Vogt", station2.getName());
        assertEquals((Double) 46.798704, (Double) station2.getLocation().getX());
        assertEquals((Double) 7.153067, (Double) station2.getLocation().getY());

        Station station3 = stations.get(2);
        assertEquals(8577741, station3.getId());
        assertEquals("Fribourg, Charmettes", station3.getName());
        assertEquals((Double) 46.794549, (Double) station3.getLocation().getX());
        assertEquals((Double) 7.157359, (Double) station3.getLocation().getY());

        Station station4 = stations.get(3);
        assertEquals(8589155, station4.getId());
        assertEquals("Fribourg, Fries", station4.getName());
        assertEquals((Double) 46.800573, (Double) station4.getLocation().getX());
        assertEquals((Double) 7.152193, (Double) station4.getLocation().getY());
    }

    @Test
    public void stationsForSearchQueryDeserialization_Works() throws UnsupportedEncodingException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("stations_for_query.json");
        InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");

        Type listType = new TypeToken<List<Station>>(){}.getType();
        List<Station> stations = gson.fromJson(reader, listType);

        assertEquals(3, stations.size());


        Station station1 = stations.get(0);
        assertEquals(8587062, station1.getId());
        assertEquals("Genève, Jonction", station1.getName());
        assertEquals((Double) 46.200806, (Double) station1.getLocation().getX());
        assertEquals((Double) 6.129676, (Double) station1.getLocation().getY());

        Station station2 = stations.get(1);
        assertEquals(8504975, station2.getId());
        assertEquals("Marly, Jonction", station2.getName());
        assertEquals((Double) 46.780724, (Double) station2.getLocation().getX());
        assertEquals((Double) 7.150412, (Double) station2.getLocation().getY());

        Station station3 = stations.get(2);
        assertEquals(8583274, station3.getId());
        assertEquals("Sion, Jonction", station3.getName());
        assertEquals((Double) 46.225727, (Double) station3.getLocation().getX());
        assertEquals((Double) 7.336374, (Double) station3.getLocation().getY());
    }
}
