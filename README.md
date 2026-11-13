# sistema-gestion-mensajeria-jcf
# Diseño de un Sistema de Gestión utilizando Java Collections Framework (Caso 24: Gestión de Mensajería)

- **Actividad:** Unidad III — Actividad 1: Protocolo Individual
- **Asignatura:** Estructuras de Datos
- **Programa:** Ingeniería de Software — Universidad de Cartagena
- **Estudiante:** Jorge Taján Acevedo
- **Tutor:** John Carlos Arrieta Arrieta
- **Versión de Java:** OpenJDK 17+

## Descripción del problema
Aplicación de consola en Java que administra el registro, búsqueda, priorización, actualización y cancelación de paquetes en una empresa de mensajería, relacionando paquetes (`Paquete`), clientes (`Cliente`) y rutas de entrega (`RutaEntrega`) mediante colecciones nativas del Java Collections Framework sincronizadas entre sí.

## Colecciones principales utilizadas
- **`List<Paquete>` (`ArrayList<Paquete>`):** Registro histórico general de los paquetes ingresados en orden cronológico de llegada.
- **`Map<String, Paquete>` (`HashMap<String, Paquete>`):** Índice asociativo por código único de paquete (ej. `PKG-101`) para consultas directas en tiempo constante $O(1)$[cite: 7].
- **`Set<String>` (`HashSet<String>`):** Control de unicidad de códigos de rutas de entrega, impidiendo registrar rutas repetidas[cite: 7].
- **`Set<String>` (`TreeSet<String>`):** Catálogo automáticamente ordenado alfabéticamente de ciudades de destino cubiertas[cite: 7].
- **`Queue<Paquete>` (`PriorityQueue<Paquete>`):** Cola de despacho prioritario gobernada por `ComparadorPrioridadPaquete` (`URGENTE` → `EXPRESS` → `ESTANDAR`)[cite: 7].

## Instrucciones para compilar y ejecutar
1. Clonar el repositorio público:
   ```bash
   git clone <URL_DE_TU_REPOSITORIO>