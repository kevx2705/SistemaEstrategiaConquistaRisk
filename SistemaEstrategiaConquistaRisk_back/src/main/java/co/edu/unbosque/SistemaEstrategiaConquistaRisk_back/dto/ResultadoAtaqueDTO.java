package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.dto;

public class ResultadoAtaqueDTO {

    private boolean conquista;
    private int tropasAtacanteRestantes;
    private int tropasDefensorRestantes;
    private int perdidasAtacante;
    private int perdidasDefensor;

    private int[] dadosAtacante;
    private int[] dadosDefensor;

    private Long territorioAtacanteId;
    private Long territorioDefensorId;

    private Long nuevoDueno; // ← Para registrar el nuevo propietario si hay conquista

    // ===========================
    // Getters y Setters
    // ===========================

    public boolean isConquista() {
        return conquista;
    }

    public void setConquista(boolean conquista) {
        this.conquista = conquista;
    }

    public int getTropasAtacanteRestantes() {
        return tropasAtacanteRestantes;
    }

    public void setTropasAtacanteRestantes(int tropasAtacanteRestantes) {
        this.tropasAtacanteRestantes = tropasAtacanteRestantes;
    }

    public int getTropasDefensorRestantes() {
        return tropasDefensorRestantes;
    }

    public void setTropasDefensorRestantes(int tropasDefensorRestantes) {
        this.tropasDefensorRestantes = tropasDefensorRestantes;
    }

    public int getPerdidasAtacante() {
        return perdidasAtacante;
    }

    public void setPerdidasAtacante(int perdidasAtacante) {
        this.perdidasAtacante = perdidasAtacante;
    }

    public int getPerdidasDefensor() {
        return perdidasDefensor;
    }

    public void setPerdidasDefensor(int perdidasDefensor) {
        this.perdidasDefensor = perdidasDefensor;
    }

    public int[] getDadosAtacante() {
        return dadosAtacante;
    }

    public void setDadosAtacante(int[] dadosAtacante) {
        this.dadosAtacante = dadosAtacante;
    }

    public int[] getDadosDefensor() {
        return dadosDefensor;
    }

    public void setDadosDefensor(int[] dadosDefensor) {
        this.dadosDefensor = dadosDefensor;
    }

    public Long getTerritorioAtacanteId() {
        return territorioAtacanteId;
    }

    public void setTerritorioAtacanteId(Long territorioAtacanteId) {
        this.territorioAtacanteId = territorioAtacanteId;
    }

    public Long getTerritorioDefensorId() {
        return territorioDefensorId;
    }

    public void setTerritorioDefensorId(Long territorioDefensorId) {
        this.territorioDefensorId = territorioDefensorId;
    }

    public Long getNuevoDueno() {
        return nuevoDueno;
    }

    public void setNuevoDueno(Long nuevoDueno) {
        this.nuevoDueno = nuevoDueno;
    }
}
