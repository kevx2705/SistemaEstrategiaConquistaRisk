package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "carta")
public class Carta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String tipo; // infanteria, caballeria, artilleria, comodin
	private String territorio; // nombre del territorio asociado
	private boolean disponible; // true = está en el mazo, false = la tiene un jugador

	public Carta() {
	}

	public Carta(String tipo, String territorio, boolean disponible) {
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
	public String toString() {
		return "Carta [id=" + id + ", tipo=" + tipo + ", territorio=" + territorio + ", disponible=" + disponible + "]";
	}

}