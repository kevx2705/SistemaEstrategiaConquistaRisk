package co.edu.unbosque.model;

public class Carta {
    private Long id;
    private String nombre;
    private String tipo;
    private boolean disponible;

    public Carta() {}

    public Carta(Long id, String nombre, String tipo, boolean disponible) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.disponible = disponible;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}
