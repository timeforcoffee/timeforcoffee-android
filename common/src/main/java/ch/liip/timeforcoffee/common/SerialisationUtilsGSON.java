package ch.liip.timeforcoffee.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by nicolas on 31/08/16.
 */
public class SerialisationUtilsGSON {
    public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

    public static String serialize(Object obj) {
        return gson.toJson(obj);
    }

    public static Object deserialize(Class className, String string) {
        return gson.fromJson(string, className);
    }

    public static Object deserialize(Type type, String string) {
        return gson.fromJson(string, TypeToken.get(type).getType());
    }
}