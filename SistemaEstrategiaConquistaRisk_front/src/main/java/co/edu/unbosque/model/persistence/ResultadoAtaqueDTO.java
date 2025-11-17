package co.edu.unbosque.model.persistence;

/**
 * Clase DTO que representa el resultado de un ataque en el juego Risk.
 * Contiene información sobre el éxito o fracaso de la conquista, las tropas restantes,
 * las pérdidas de ambos bandos, los dados lanzados, y los territorios involucrados.
 */
public class ResultadoAtaqueDTO {

    /** Indica si el territorio defensor fue conquistado por el atacante. */
    private boolean conquista;

    /** Cantidad de tropas restantes del atacante después del ataque. */
    private int tropasAtacanteRestantes;

    /** Cantidad de tropas restantes del defensor después del ataque. */
    private int tropasDefensorRestantes;

    /** Cantidad de tropas perdidas por el atacante durante el ataque. */
    private int perdidasAtacante;

    /** Cantidad de tropas perdidas por el defensor durante el ataque. */
    private int perdidasDefensor;

    /** Valores de los dados lanzados por el atacante. */
    private int[] dadosAtacante;

    /** Valores de los dados lanzados por el defensor. */
    private int[] dadosDefensor;

    /** Identificador del territorio atacante. */
    private Long territorioAtacanteId;

    /** Identificador del territorio defensor. */
    private Long territorioDefensorId;

    /** Identificador del nuevo dueño del territorio (en caso de conquista). */
    private Long nuevoDueno;

    /**
     * Verifica si el territorio defensor fue conquistado por el atacante.
     *
     * @return true si el territorio fue conquistado, false en caso contrario.
     */
    public boolean isConquista() {
        return conquista;
    }

    /**
     * Establece si el territorio defensor fue conquistado por el atacante.
     *
     * @param conquista true si el territorio fue conquistado, false en caso contrario.
     */
    public void setConquista(boolean conquista) {
        this.conquista = conquista;
    }

    /**
     * Obtiene la cantidad de tropas restantes del atacante después del ataque.
     *
     * @return La cantidad de tropas restantes del atacante.
     */
    public int getTropasAtacanteRestantes() {
        return tropasAtacanteRestantes;
    }

    /**
     * Establece la cantidad de tropas restantes del atacante después del ataque.
     *
     * @param tropasAtacanteRestantes La cantidad de tropas restantes a establecer.
     */
    public void setTropasAtacanteRestantes(int tropasAtacanteRestantes) {
        this.tropasAtacanteRestantes = tropasAtacanteRestantes;
    }

    /**
     * Obtiene la cantidad de tropas restantes del defensor después del ataque.
     *
     * @return La cantidad de tropas restantes del defensor.
     */
    public int getTropasDefensorRestantes() {
        return tropasDefensorRestantes;
    }

    /**
     * Establece la cantidad de tropas restantes del defensor después del ataque.
     *
     * @param tropasDefensorRestantes La cantidad de tropas restantes a establecer.
     */
    public void setTropasDefensorRestantes(int tropasDefensorRestantes) {
        this.tropasDefensorRestantes = tropasDefensorRestantes;
    }

    /**
     * Obtiene la cantidad de tropas perdidas por el atacante durante el ataque.
     *
     * @return La cantidad de tropas perdidas por el atacante.
     */
    public int getPerdidasAtacante() {
        return perdidasAtacante;
    }

    /**
     * Establece la cantidad de tropas perdidas por el atacante durante el ataque.
     *
     * @param perdidasAtacante La cantidad de tropas perdidas a establecer.
     */
    public void setPerdidasAtacante(int perdidasAtacante) {
        this.perdidasAtacante = perdidasAtacante;
    }

    /**
     * Obtiene la cantidad de tropas perdidas por el defensor durante el ataque.
     *
     * @return La cantidad de tropas perdidas por el defensor.
     */
    public int getPerdidasDefensor() {
        return perdidasDefensor;
    }

    /**
     * Establece la cantidad de tropas perdidas por el defensor durante el ataque.
     *
     * @param perdidasDefensor La cantidad de tropas perdidas a establecer.
     */
    public void setPerdidasDefensor(int perdidasDefensor) {
        this.perdidasDefensor = perdidasDefensor;
    }

    /**
     * Obtiene los valores de los dados lanzados por el atacante.
     *
     * @return Un arreglo con los valores de los dados del atacante.
     */
    public int[] getDadosAtacante() {
        return dadosAtacante;
    }

    /**
     * Establece los valores de los dados lanzados por el atacante.
     *
     * @param dadosAtacante Un arreglo con los valores de los dados a establecer.
     */
    public void setDadosAtacante(int[] dadosAtacante) {
        this.dadosAtacante = dadosAtacante;
    }

    /**
     * Obtiene los valores de los dados lanzados por el defensor.
     *
     * @return Un arreglo con los valores de los dados del defensor.
     */
    public int[] getDadosDefensor() {
        return dadosDefensor;
    }

    /**
     * Establece los valores de los dados lanzados por el defensor.
     *
     * @param dadosDefensor Un arreglo con los valores de los dados a establecer.
     */
    public void setDadosDefensor(int[] dadosDefensor) {
        this.dadosDefensor = dadosDefensor;
    }

    /**
     * Obtiene el identificador del territorio atacante.
     *
     * @return El identificador del territorio atacante.
     */
    public Long getTerritorioAtacanteId() {
        return territorioAtacanteId;
    }

    /**
     * Establece el identificador del territorio atacante.
     *
     * @param territorioAtacanteId El identificador del territorio atacante a establecer.
     */
    public void setTerritorioAtacanteId(Long territorioAtacanteId) {
        this.territorioAtacanteId = territorioAtacanteId;
    }

    /**
     * Obtiene el identificador del territorio defensor.
     *
     * @return El identificador del territorio defensor.
     */
    public Long getTerritorioDefensorId() {
        return territorioDefensorId;
    }

    /**
     * Establece el identificador del territorio defensor.
     *
     * @param territorioDefensorId El identificador del territorio defensor a establecer.
     */
    public void setTerritorioDefensorId(Long territorioDefensorId) {
        this.territorioDefensorId = territorioDefensorId;
    }

    /**
     * Obtiene el identificador del nuevo dueño del territorio (en caso de conquista).
     *
     * @return El identificador del nuevo dueño del territorio.
     */
    public Long getNuevoDueno() {
        return nuevoDueno;
    }

    /**
     * Establece el identificador del nuevo dueño del territorio (en caso de conquista).
     *
     * @param nuevoDueno El identificador del nuevo dueño a establecer.
     */
    public void setNuevoDueno(Long nuevoDueno) {
        this.nuevoDueno = nuevoDueno;
    }
}