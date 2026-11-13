package modelo;

// Define los estados posibles por los que pasa un paquete en la mensajería
public enum EstadoPaquete {
    EN_BODEGA,
    EN_RUTA,
    ENTREGADO,
    CANCELADO
}