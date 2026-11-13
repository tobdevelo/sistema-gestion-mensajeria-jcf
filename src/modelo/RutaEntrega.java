package modelo;

import java.util.Objects;

// Representa una ruta logística entre una ciudad origen y una ciudad destino
public class RutaEntrega {

    private final String codigoRuta;
    private final String ciudadOrigen;
    private final String ciudadDestino;

    public RutaEntrega(String codigoRuta, String ciudadOrigen, String ciudadDestino) {
        this.codigoRuta = codigoRuta;
        this.ciudadOrigen = ciudadOrigen;
        this.ciudadDestino = ciudadDestino;
    }

    public String getCodigoRuta() { return codigoRuta; }
    public String getCiudadOrigen() { return ciudadOrigen; }
    public String getCiudadDestino() { return ciudadDestino; }

    // Dos rutas son iguales si comparten el mismo código único de ruta
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RutaEntrega)) return false;
        RutaEntrega that = (RutaEntrega) o;
        return Objects.equals(codigoRuta, that.codigoRuta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoRuta);
    }

    @Override
    public String toString() {
        return codigoRuta + " [" + ciudadOrigen + " -> " + ciudadDestino + "]";
    }
}