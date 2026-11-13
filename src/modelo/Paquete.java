package modelo;

import java.util.Objects;

// Entidad principal del sistema: representa un paquete asignado a un cliente y una ruta
public class Paquete implements Comparable<Paquete> {

    private final String codigo;
    private final Cliente cliente;
    private final RutaEntrega ruta;
    private final double pesoKg;
    private PrioridadEnvio prioridad;
    private EstadoPaquete estado;
    private final int ordenLlegada; // Sirve para desempatar si tienen igual prioridad

    public Paquete(String codigo, Cliente cliente, RutaEntrega ruta, double pesoKg,
                   PrioridadEnvio prioridad, int ordenLlegada) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.ruta = ruta;
        this.pesoKg = pesoKg;
        this.prioridad = prioridad;
        this.estado = EstadoPaquete.EN_BODEGA;
        this.ordenLlegada = ordenLlegada;
    }

    public String getCodigo() { return codigo; }
    public Cliente getCliente() { return cliente; }
    public RutaEntrega getRuta() { return ruta; }
    public double getPesoKg() { return pesoKg; }
    public PrioridadEnvio getPrioridad() { return prioridad; }
    public void setPrioridad(PrioridadEnvio prioridad) { this.prioridad = prioridad; }
    public EstadoPaquete getEstado() { return estado; }
    public void setEstado(EstadoPaquete estado) { this.estado = estado; }
    public int getOrdenLlegada() { return ordenLlegada; }

    // Orden natural: ordena alfabéticamente por código de paquete
    @Override
    public int compareTo(Paquete otro) {
        return this.codigo.compareToIgnoreCase(otro.codigo);
    }

    // Dos paquetes son iguales si tienen el mismo código único
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paquete)) return false;
        Paquete paquete = (Paquete) o;
        return Objects.equals(codigo, paquete.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return String.format("[%s] Cliente: %s | Ruta: %s | Peso: %.1f kg | Prioridad: %s | Estado: %s",
                codigo, cliente.getNombre(), ruta.toString(), pesoKg, prioridad, estado);
    }
}