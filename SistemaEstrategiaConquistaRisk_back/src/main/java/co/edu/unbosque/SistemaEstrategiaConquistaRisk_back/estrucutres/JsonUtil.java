package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

/**
 * Utilidad para convertir objetos de tipo {@code MyLinkedList<T>} a JSON y viceversa.
 * Proporciona métodos estáticos para serializar y deserializar listas enlazadas personalizadas.
 */
public class JsonUtil {

    /** Instancia estática de Gson para la serialización y deserialización de JSON. */
    private static final Gson gson = new Gson();

    /**
     * Convierte una lista enlazada personalizada a su representación en formato JSON.
     *
     * @param <T>  Tipo genérico de los elementos en la lista.
     * @param list Lista enlazada personalizada a convertir.
     * @return Representación en formato JSON de la lista.
     */
    public static <T> String toJson(MyLinkedList<T> list) {
        return gson.toJson(list);
    }

    /**
     * Convierte una representación en formato JSON a una lista enlazada personalizada.
     *
     * @param <T>   Tipo genérico de los elementos en la lista.
     * @param json  Representación en formato JSON de la lista.
     * @param clazz Clase del tipo genérico de los elementos en la lista.
     * @return Lista enlazada personalizada construida a partir del JSON.
     */
    public static <T> MyLinkedList<T> fromJson(String json, Class<T> clazz) {
        Type type = TypeToken.getParameterized(MyLinkedList.class, clazz).getType();
        return gson.fromJson(json, type);
    }
}
