package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto;

import java.util.Objects;

public class ContinenteDTO {
	private Long id;

	private String nombre;

	private int refuerzoPorControl;

	private int territoriosTotales;
	
	public ContinenteDTO() {
		// TODO Auto-generated constructor stub
	}

	public ContinenteDTO(String nombre, int refuerzoPorControl, int territoriosTotales) {
		super();
		this.nombre = nombre;
		this.refuerzoPorControl = refuerzoPorControl;
		this.territoriosTotales = territoriosTotales;
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
		ContinenteDTO other = (ContinenteDTO) obj;
		return Objects.equals(id, other.id) && Objects.equals(nombre, other.nombre)
				&& refuerzoPorControl == other.refuerzoPorControl && territoriosTotales == other.territoriosTotales;
	}

	@Override
	public String toString() {
		return "ContinenteDTO [id=" + id + ", nombre=" + nombre + ", refuerzoPorControl=" + refuerzoPorControl
				+ ", territoriosTotales=" + territoriosTotales + "]";
	}
	
	
	

}
