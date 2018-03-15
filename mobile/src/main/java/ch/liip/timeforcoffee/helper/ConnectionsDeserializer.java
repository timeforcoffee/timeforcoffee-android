package ch.liip.timeforcoffee.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import ch.liip.timeforcoffee.zvv.CheckPoint;
import ch.liip.timeforcoffee.zvv.ConnectionsResponse;

public class ConnectionsDeserializer implements JsonDeserializer<ConnectionsResponse> {

  private Gson mGson;

  public ConnectionsDeserializer() {
    mGson = new GsonBuilder().registerTypeAdapter(Date.class, new DateDeserializer()).create();
  }

  @Override
  public ConnectionsResponse deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      JsonElement passListJsonElement = jsonElement.getAsJsonObject().get("passlist");

      List<List<CheckPoint>> checkPoints = new ArrayList<>();

      if (passListJsonElement != null && passListJsonElement.isJsonArray()) {
        JsonArray passList = passListJsonElement.getAsJsonArray();

        JsonElement passListItem = passList.get(0);

        // PassListItem can be an Array of Checkpoint or directly a CheckPoint.
        if (passListItem.isJsonArray()) {
          Iterator<JsonElement> iter = passList.iterator();
          while (iter.hasNext()) {
            passListItem = iter.next();
            checkPoints.add(fromJsonCheckPointList(passListItem.getAsJsonArray()));
          }
        } else {
          checkPoints.add(fromJsonCheckPointList(passList));
        }
      } else throw new JsonParseException("Unparseable passList in ConnectionsResponse");

      return ConnectionsResponse.fromCheckPointList(checkPoints);
  }

  private List<CheckPoint> fromJsonCheckPointList(JsonArray jsonCheckpoints) {
    List<CheckPoint> checkPoints = new ArrayList<>();
    Iterator<JsonElement> iter = jsonCheckpoints.iterator();

    while (iter.hasNext()) {
      checkPoints.add(mGson.fromJson(iter.next(), CheckPoint.class));
    }
    return checkPoints;
  }
}
