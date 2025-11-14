package co.edu.unbosque.estructures;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

/**
 * Utilidad para convertir objetos de tipo {@link MyLinkedList} a JSON y
 * viceversa. Proporciona métodos estáticos para serializar y deserializar
 * listas enlazadas.
 */
public class JsonUtil {

	/**
	 * Instancia estática de {@link Gson} para manejar la serialización y
	 * deserialización de JSON.
	 */
	private static final Gson gson = new Gson();

	/**
	 * Convierte una lista enlazada a su representación en formato JSON.
	 *
	 * @param list Lista enlazada a serializar.
	 * @param <T>  Tipo genérico de los elementos en la lista.
	 * @return Cadena JSON que representa la lista enlazada.
	 */
	public static <T> String toJson(MyLinkedList<T> list) {
		return gson.toJson(list);
	}

	/** constructor vacio */
	public JsonUtil() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Convierte una cadena JSON a una instancia de {@link MyLinkedList}.
	 *
	 * @param json  Cadena JSON a deserializar.
	 * @param clazz Clase del tipo genérico de los elementos en la lista.
	 * @param <T>   Tipo genérico de los elementos en la lista.
	 * @return Instancia de {@link MyLinkedList} con los datos del JSON.
	 */
	public static <T> MyLinkedList<T> fromJson(String json, Class<T> clazz) {
		Type type = TypeToken.getParameterized(MyLinkedList.class, clazz).getType();
		return gson.fromJson(json, type);
	}
}
