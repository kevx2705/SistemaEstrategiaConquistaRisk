package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto;

import java.util.Objects;

public class CartaDTO {
	private Long id;

	private String tipo; // infanteria, caballeria, artilleria, comodin
	private String territorio; // nombre del territorio asociado
	private boolean disponible;

	public CartaDTO() {
		// TODO Auto-generated constructor stub
	}

	public CartaDTO(String tipo, String territorio, boolean disponible) {
		super();
		this.tipo = tipo;
		this.territorio = territorio;
		this.disponible = disponible;
	}

	public Long getId() {
		return id;
	}

	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTerritorio() {
		return territorio;
	}

	public void setTerritorio(String territorio) {
		this.territorio = territorio;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, territorio, tipo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CartaDTO other = (CartaDTO) obj;
		return Objects.equals(id, other.id) && Objects.equals(territorio, other.territorio)
				&& Objects.equals(tipo, other.tipo);
	}

	@Override
	public String toString() {
		return "CartaDTO [id=" + id + ", tipo=" + tipo + ", territorio=" + territorio + ", disponible=" + disponible
				+ "]";
	}

}
