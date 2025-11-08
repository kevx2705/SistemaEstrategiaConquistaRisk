package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto;

import java.util.Objects;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity.Carta;
import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;

public class JugadorDTO {

	private Long id;
	private String nombre;

	private String correo;

	private String color;

	private int tropasDisponibles;

	private int territoriosControlados;

	private boolean activo;
	private MyLinkedList<CartaDTO> cartas = new MyLinkedList<>();

	public JugadorDTO() {
		// TODO Auto-generated constructor stub
	}

	public JugadorDTO(String nombre, String correo, String color, int tropasDisponibles, int territoriosControlados,
			boolean activo, MyLinkedList<CartaDTO> cartas) {
		super();
		this.nombre = nombre;
		this.correo = correo;
		this.color = color;
		this.tropasDisponibles = tropasDisponibles;
		this.territoriosControlados = territoriosControlados;
		this.activo = activo;
		this.cartas = cartas;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public MyLinkedList<CartaDTO> getCartas() {
		return cartas;
	}

	public void setCartas(MyLinkedList<CartaDTO> cartas) {
		this.cartas = cartas;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getTropasDisponibles() {
		return tropasDisponibles;
	}

	public void setTropasDisponibles(int tropasDisponibles) {
		this.tropasDisponibles = tropasDisponibles;
	}

	public int getTerritoriosControlados() {
		return territoriosControlados;
	}

	public void setTerritoriosControlados(int territoriosControlados) {
		this.territoriosControlados = territoriosControlados;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(activo, color, id, nombre, territoriosControlados, tropasDisponibles);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JugadorDTO other = (JugadorDTO) obj;
		return activo == other.activo && Objects.equals(color, other.color) && Objects.equals(id, other.id)
				&& Objects.equals(nombre, other.nombre) && territoriosControlados == other.territoriosControlados
				&& tropasDisponibles == other.tropasDisponibles;
	}

	@Override
	public String toString() {
		return "JugadorDTO [id=" + id + ", nombre=" + nombre + ", color=" + color + ", tropasDisponibles="
				+ tropasDisponibles + ", territoriosControlados=" + territoriosControlados + ", activo=" + activo + "]";
	}

}
