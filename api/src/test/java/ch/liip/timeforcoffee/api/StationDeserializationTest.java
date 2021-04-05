package ch.liip.timeforcoffee.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import ch.liip.timeforcoffee.api.deserializers.DateDeserializer;
import ch.liip.timeforcoffee.backend.Station;
import ch.liip.timeforcoffee.backend.Stations;

import static org.junit.Assert.assertEquals;

public class StationDeserializationTest {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateDeserializer())
            .create();

    @Test
    public void stationsForLocationDeserialization_Works() throws UnsupportedEncodingException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("stations_for_location.json");
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        Stations stations = gson.fromJson(reader, Stations.class);

        assertEquals(4, stations.getStations().size());

        Station station1 = stations.getStations().get(0);
        assertEquals(8589154, station1.getId());
        assertEquals("Fribourg, Industrie", station1.getName());
        assertEquals((Double) 46.796343, (Double) station1.getCoordinate().getX());
        assertEquals((Double)7.155319, (Double) station1.getCoordinate().getY());

        Station station2 = stations.getStations().get(1);
        assertEquals(8588858, station2.getId());
        assertEquals("Fribourg, J. Vogt", station2.getName());
        assertEquals((Double) 46.798704, (Double) station2.getCoordinate().getX());
        assertEquals((Double) 7.153067, (Double) station2.getCoordinate().getY());

        Station station3 = stations.getStations().get(2);
        assertEquals(8577741, station3.getId());
        assertEquals("Fribourg, Charmettes", station3.getName());
        assertEquals((Double) 46.794549, (Double) station3.getCoordinate().getX());
        assertEquals((Double) 7.157359, (Double) station3.getCoordinate().getY());

        Station station4 = stations.getStations().get(3);
        assertEquals(8589155, station4.getId());
        assertEquals("Fribourg, Fries", station4.getName());
        assertEquals((Double) 46.800573, (Double) station4.getCoordinate().getX());
        assertEquals((Double) 7.152193, (Double) station4.getCoordinate().getY());
    }

    @Test
    public void stationsForSearchQueryDeserialization_Works() throws UnsupportedEncodingException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("stations_for_query.json");
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        Stations stations = gson.fromJson(reader, Stations.class);

        assertEquals(3, stations.getStations().size());


        Station station1 = stations.getStations().get(0);
        assertEquals(8587062, station1.getId());
        assertEquals("Genève, Jonction", station1.getName());
        assertEquals((Double) 46.200806, (Double) station1.getCoordinate().getX());
        assertEquals((Double) 6.129676, (Double) station1.getCoordinate().getY());

        Station station2 = stations.getStations().get(1);
        assertEquals(8504975, station2.getId());
        assertEquals("Marly, Jonction", station2.getName());
        assertEquals((Double) 46.780724, (Double) station2.getCoordinate().getX());
        assertEquals((Double) 7.150412, (Double) station2.getCoordinate().getY());

        Station station3 = stations.getStations().get(2);
        assertEquals(8583274, station3.getId());
        assertEquals("Sion, Jonction", station3.getName());
        assertEquals((Double) 46.225727, (Double) station3.getCoordinate().getX());
        assertEquals((Double) 7.336374, (Double) station3.getCoordinate().getY());
    }
}
