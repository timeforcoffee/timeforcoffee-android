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
      final JsonArray passList = passListJsonElement.getAsJsonArray();
      final JsonElement passListItem = passList.size() > 0 ? passList.get(0) : null;

      // PassListItem can be an Array of Checkpoint or directly a CheckPoint.
      // We only get the first Array of Checkpoint by convention. 
      if (passListItem != null) {
        if (passListItem.isJsonObject()) {
          checkPoints.add(fromJsonCheckPointList(passList));
          return ConnectionsResponse.fromCheckPointList(checkPoints);
        } else if (passListItem.isJsonArray()) {
          checkPoints.add(fromJsonCheckPointList(passListItem.getAsJsonArray()));
          return ConnectionsResponse.fromCheckPointList(checkPoints);
        }
      }
    }
    throw new JsonParseException("Failed to parse passList in ConnectionsResponse");
  }

  private List<CheckPoint> fromJsonCheckPointList(JsonArray jsonCheckpoints) {
    List<CheckPoint> checkPoints = new ArrayList<>();

    for ( JsonElement crtJsonElement : jsonCheckpoints) {
      checkPoints.add(mGson.fromJson(crtJsonElement, CheckPoint.class));
    }

    return checkPoints;
  }
}
