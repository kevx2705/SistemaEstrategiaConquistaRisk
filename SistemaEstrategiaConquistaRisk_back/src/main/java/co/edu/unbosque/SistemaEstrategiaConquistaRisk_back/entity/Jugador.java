package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity;

import java.util.Objects;

import co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.estrucutres.MyLinkedList;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "jugador")
public class Jugador {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	private String nombre;

	private String color;

	private int tropasDisponibles;

	private int territoriosControlados;

	private boolean activo;
	private MyLinkedList<Carta> cartas;

	public Jugador() {
		// TODO Auto-generated constructor stub
	}

	public Jugador(String nombre, String color) {
		this.nombre = nombre;
		this.color = color;
		this.tropasDisponibles = 0;
		this.territoriosControlados = 0;
		this.activo = true;
	}

	public MyLinkedList<Carta> getCartas() {
		return cartas;
	}

	public void setCartas(MyLinkedList<Carta> cartas) {
		this.cartas = cartas;
	}

	public Long getId() {
		return id;
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
		Jugador other = (Jugador) obj;
		return activo == other.activo && Objects.equals(color, other.color) && Objects.equals(id, other.id)
				&& Objects.equals(nombre, other.nombre) && territoriosControlados == other.territoriosControlados
				&& tropasDisponibles == other.tropasDisponibles;
	}

	@Override
	public String toString() {
		return "Jugador [id=" + id + ", nombre=" + nombre + ", color=" + color + ", tropasDisponibles="
				+ tropasDisponibles + ", territoriosControlados=" + territoriosControlados + ", activo=" + activo + "]";
	}

}
