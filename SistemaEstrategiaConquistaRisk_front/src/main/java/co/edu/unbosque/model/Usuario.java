package co.edu.unbosque.model;

import java.util.Objects;

/**
 * Clase Usuario que representa a un usuario del sistema de entrenamiento.
 */
public abstract class Usuario {

    /**
     * Atributo identificador único de la clase Usuario.
     */
    private Long id;

    /**
     * Atributo nombre de la clase Usuario.
     */
    private String nombre;

    /**
     * Atributo correo de la clase Usuario.
     */
    private String correo;

    /**
     * Atributo edad de la clase Usuario.
     */
    private int edad;

    /**
     * Atributo contraseña de la clase Usuario.
     */
    private String contrasena;

    /**
     * Constructor vacío de la clase Usuario.
     */
    public Usuario() {
        // TODO Auto-generated constructor stub
    }


    /**
     * Constructor con parámetros (atributos) de la clase Usuario, excepto el id.
     *
     * @param nombre      Nombre del usuario.
     * @param correo      Correo electrónico del usuario.
     * @param edad        Edad del usuario.
     * @param contrasena  Contraseña del usuario.
     */
    public Usuario(String nombre, String correo, int edad, String contrasena) {
        super();
        this.nombre = nombre;
        this.correo = correo;
        this.edad = edad;
        this.contrasena = contrasena;
    }

    /**
     * Getter del atributo id.
     *
     * @return id del usuario.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter del atributo id.
     *
     * @param id Identificador único del usuario.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter del atributo nombre.
     *
     * @return nombre del usuario.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Setter del atributo nombre.
     *
     * @param nombre Nombre del usuario.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Getter del atributo correo.
     *
     * @return correo del usuario.
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Setter del atributo correo.
     *
     * @param correo Correo electrónico del usuario.
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Getter del atributo edad.
     *
     * @return edad del usuario.
     */
    public int getEdad() {
        return edad;
    }

    /**
     * Setter del atributo edad.
     *
     * @param edad Edad del usuario.
     */
    public void setEdad(int edad) {
        this.edad = edad;
    }

    /**
     * Getter del atributo contraseña.
     *
     * @return contraseña del usuario.
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Setter del atributo contraseña.
     *
     * @param contrasena Contraseña del usuario.
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * Método toString de la clase Usuario.
     *
     * @return String con la información del usuario.
     */
    @Override
    public String toString() {
        return "Usuario [nombre=" + nombre + ", correo=" + correo + ", contraseña=" + contrasena + "]";
    }

    /**
     * Método hashCode de la clase Usuario.
     *
     * @return código hash del usuario.
     */
    @Override
    public int hashCode() {
        return Objects.hash(contrasena, correo, edad, id, nombre);
    }

    /**
     * Método equals de la clase Usuario.
     *
     * @param obj Objeto a comparar con este usuario.
     * @return true si ambos usuarios son equivalentes, false en caso contrario.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Usuario other = (Usuario) obj;
        return Objects.equals(contrasena, other.contrasena) && Objects.equals(correo, other.correo)
                && edad == other.edad && Objects.equals(id, other.id) && Objects.equals(nombre, other.nombre);
    }

}
