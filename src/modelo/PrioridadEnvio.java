package modelo;

// Define los niveles de prioridad para despachar paquetes
// El nivel numérico menor indica mayor urgencia (1 = más urgente)
public enum PrioridadEnvio {
    URGENTE(1),
    EXPRESS(2),
    ESTANDAR(3);

    private final int nivel;

    PrioridadEnvio(int nivel) {
        this.nivel = nivel;
    }

    public int getNivel() {
        return nivel;
    }
}
