package co.edu.unbosque.estructures;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;


public class JsonUtil {

    private static final Gson gson = new Gson();

    public static <T> String toJson(MyLinkedList<T> list) {
        return gson.toJson(list);
    }

    public static <T> MyLinkedList<T> fromJson(String json, Class<T> clazz) {
        Type type = TypeToken.getParameterized(MyLinkedList.class, clazz).getType();
        return gson.fromJson(json, type);
    }
}
