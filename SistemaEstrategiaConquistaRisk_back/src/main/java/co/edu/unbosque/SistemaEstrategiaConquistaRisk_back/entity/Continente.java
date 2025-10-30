package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "continente")
public class Continente {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nombre;

	private int refuerzoPorControl; //la cantidad de refuerzo q recibe por conquistar ese continente

	private int territoriosTotales; //territorios q posee el continente

	public Continente() {
	}

	public Continente(String nombre, int refuerzoPorControl, int territoriosTotales) {
		this.nombre = nombre;
		this.refuerzoPorControl = refuerzoPorControl;
		this.territoriosTotales = territoriosTotales;
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

	public int getRefuerzoPorControl() {
		return refuerzoPorControl;
	}

	public void setRefuerzoPorControl(int refuerzoPorControl) {
		this.refuerzoPorControl = refuerzoPorControl;
	}

	public int getTerritoriosTotales() {
		return territoriosTotales;
	}

	public void setTerritoriosTotales(int territoriosTotales) {
		this.territoriosTotales = territoriosTotales;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nombre, refuerzoPorControl, territoriosTotales);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Continente other = (Continente) obj;
		return Objects.equals(id, other.id) && Objects.equals(nombre, other.nombre)
				&& refuerzoPorControl == other.refuerzoPorControl && territoriosTotales == other.territoriosTotales;
	}

	@Override
	public String toString() {
		return "Continente [id=" + id + ", nombre=" + nombre + ", refuerzoPorControl=" + refuerzoPorControl
				+ ", territoriosTotales=" + territoriosTotales + "]";
	}
	
}
