package co.edu.unbosque.model;

/**
 * Utilidad estática que mantiene una referencia al {@link Usuario} autenticado actualmente
 * en la sesión de la aplicación. Simplifica el acceso global al usuario activo
 * sin necesidad de inyectar un bean de sesión en cada componente.
 *
 * Nota: En aplicaciones más grandes se recomienda un manejo de sesión más
 * robusto para evitar estados globales y facilitar pruebas.
 */
public class UsuarioActual {

	/** Referencia al usuario autenticado actual. */
	private static Usuario usuarioActual;

	/**
	 * Obtiene el usuario autenticado actual.
	 * @return Usuario actual o null si no hay sesión establecida.
	 */
	public static Usuario getUsuarioActual() {
		if (usuarioActual == null) {
			return null;
		}
		return usuarioActual;
	}

	/**
	 * Establece el usuario autenticado actual.
	 * @param usuarioActual Usuario autenticado (puede ser null para limpiar sesión).
	 */
	public static void setUsuarioActual(Usuario usuarioActual) {
		UsuarioActual.usuarioActual = usuarioActual;
	}
}
