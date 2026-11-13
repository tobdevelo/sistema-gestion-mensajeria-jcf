package comparadores;

import modelo.Paquete;
import java.util.Comparator;

// Define el criterio de prioridad para la PriorityQueue de despacho de paquetes
public class ComparadorPrioridadPaquete implements Comparator<Paquete> {

    @Override
    public int compare(Paquete p1, Paquete p2) {
        // 1. Compara primero el nivel numérico de prioridad (1 = URGENTE, 2 = EXPRESS, 3 = ESTANDAR)
        int comparacionPrioridad = Integer.compare(
                p1.getPrioridad().getNivel(),
                p2.getPrioridad().getNivel()
        );

        // Si tienen distinta prioridad, sale primero el de menor número (mayor urgencia)
        if (comparacionPrioridad != 0) {
            return comparacionPrioridad;
        }

        // 2. Si tienen la misma prioridad, se respeta el orden en que llegaron (FIFO)
        return Integer.compare(p1.getOrdenLlegada(), p2.getOrdenLlegada());
    }
}