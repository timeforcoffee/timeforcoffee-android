package ch.liip.timeforcoffee.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import ch.liip.timeforcoffee.api.deserializers.DateDeserializer;
import ch.liip.timeforcoffee.opendata.Location;
import ch.liip.timeforcoffee.opendata.LocationsResponse;
import ch.liip.timeforcoffee.backend.Station;
import ch.liip.timeforcoffee.backend.StationsResponse;

import static org.junit.Assert.assertEquals;

public class StationDeserializationTest {

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateDeserializer())
            .create();

    @Test
    public void stationsForLocationDeserialization_Works() throws UnsupportedEncodingException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("stationsForLocation.json");
        InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");

        LocationsResponse locationsResponse = gson.fromJson(reader, LocationsResponse.class);

        List<Location> stations = locationsResponse.getStations();
        assertEquals(3, stations.size());

        // The first one is the current position and will not be used
        Location station1 = stations.get(0);
        assertEquals(null, station1.getId());
        assertEquals("Place Georges Python (Haltestelle), Fribourg", station1.getName());
        assertEquals("WGS84", station1.getCoordinate().getType());
        assertEquals(null, station1.getCoordinate().getX());
        assertEquals(null, station1.getCoordinate().getY());
        assertEquals((Float) 23.0f, station1.getDistance());

        Location station2 = stations.get(1);
        assertEquals("8577810", station2.getId());
        assertEquals("Fribourg, Place Georges Python", station2.getName());
        assertEquals("WGS84", station2.getCoordinate().getType());
        assertEquals((Float) 46.8048f, station2.getCoordinate().getX());
        assertEquals((Float) 7.155668f, station2.getCoordinate().getY());
        assertEquals((Float) 23.0f, station2.getDistance());

        Location station3 = stations.get(2);
        assertEquals("8589161", station3.getId());
        assertEquals("Fribourg, St-Pierre", station3.getName());
        assertEquals("WGS84", station3.getCoordinate().getType());
        assertEquals((Float) 46.803963f, station3.getCoordinate().getX());
        assertEquals((Float) 7.15545f, station3.getCoordinate().getY());
        assertEquals((Float) 116.0f, station3.getDistance());
    }

    @Test
    public void stationsForSearchQueryDeserialization_Works() throws UnsupportedEncodingException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("stationsForSearchQuery.json");
        InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");

        StationsResponse stationsResponse = gson.fromJson(reader, StationsResponse.class);

        List<Station> stations = stationsResponse.getStations();
        assertEquals(5, stations.size());

        Station station1 = stations.get(0);
        assertEquals("008504086", station1.getId());
        assertEquals("Bulle", station1.getName());
        assertEquals((Double)46.618193, (Double)station1.getLocation().getLat());
        assertEquals((Double)7.05504, (Double)station1.getLocation().getLng());

        Station station2 = stations.get(1);
        assertEquals("008577725", station2.getId());
        assertEquals("Bulle, gare", station2.getName());
        assertEquals((Double)46.61778, (Double)station2.getLocation().getLat());
        assertEquals((Double)7.054024, (Double)station2.getLocation().getLng());

        Station station3 = stations.get(2);
        assertEquals("008593485", station3.getId());
        assertEquals("Bulle, Stade", station3.getName());
        assertEquals((Double)46.622283, (Double)station3.getLocation().getLat());
        assertEquals((Double)7.065818, (Double)station3.getLocation().getLng());

        Station station4 = stations.get(3);
        assertEquals("008593285", station4.getId());
        assertEquals("Bulle, Sionge", station4.getName());
        assertEquals((Double)46.620063, (Double)station4.getLocation().getLat());
        assertEquals((Double)7.056811, (Double)station4.getLocation().getLng());

        Station station5 = stations.get(4);
        assertEquals("008595125", station5.getId());
        assertEquals("Bulle, Prila", station5.getName());
        assertEquals((Double)46.625717, (Double)station5.getLocation().getLat());
        assertEquals((Double)7.038482, (Double)station5.getLocation().getLng());
    }
}
