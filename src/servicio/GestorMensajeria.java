package servicio;

import comparadores.ComparadorPrioridadPaquete;
import modelo.*;

import java.util.*;

// Clase de servicio que administra las colecciones de Java y mantiene su consistencia
public class GestorMensajeria {

    // 1. List (ArrayList): Registro histórico general en orden de llegada
    private final List<Paquete> historialPaquetes;

    // 2. Map (HashMap): Índice O(1) para localizar paquetes rápidamente por su código único
    private final Map<String, Paquete> paquetesPorCodigo;

    // 3. Set (HashSet): Control de unicidad para impedir códigos de ruta duplicados
    private final Set<String> codigosRutasUnicos;

    // 4. Set ordenado (TreeSet): Catálogo de ciudades de destino ordenadas alfabéticamente
    private final Set<String> ciudadesDestinoOrdenadas;

    // 5. Queue con prioridad (PriorityQueue): Cola de despacho según urgencia del envío
    private final Queue<Paquete> colaDespachoPrioritario;

    private int contadorLlegada = 0;

    public GestorMensajeria() {
        this.historialPaquetes = new ArrayList<>();
        this.paquetesPorCodigo = new HashMap<>();
        this.codigosRutasUnicos = new HashSet<>();
        this.ciudadesDestinoOrdenadas = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        this.colaDespachoPrioritario = new PriorityQueue<>(new ComparadorPrioridadPaquete());
        cargarDatosIniciales();
    }

    // Registra un paquete validando datos obligatorios y manteniendo sincronizadas las colecciones
    public String registrarPaquete(String codigo, Cliente cliente, RutaEntrega ruta,
                                   double pesoKg, PrioridadEnvio prioridad) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return "Error: El código del paquete no puede estar vacío.";
        }
        String codNormalizado = codigo.trim().toUpperCase();

        if (paquetesPorCodigo.containsKey(codNormalizado)) {
            return "Error de duplicidad: Ya existe un paquete con el código " + codNormalizado;
        }
        if (pesoKg <= 0 || pesoKg > 500) {
            return "Error: El peso debe estar entre 0.1 y 500 kg.";
        }

        contadorLlegada++;
        Paquete nuevo = new Paquete(codNormalizado, cliente, ruta, pesoKg, prioridad, contadorLlegada);

        // Inserción sincronizada en las estructuras correspondientes
        historialPaquetes.add(nuevo);
        paquetesPorCodigo.put(codNormalizado, nuevo);
        codigosRutasUnicos.add(ruta.getCodigoRuta().toUpperCase());
        ciudadesDestinoOrdenadas.add(ruta.getCiudadDestino());
        colaDespachoPrioritario.offer(nuevo);

        return "Paquete " + codNormalizado + " registrado correctamente en todas las colecciones.";
    }

    // Intenta registrar un código de ruta único en el HashSet para demostrar control de duplicados
    public String registrarCodigoRuta(String codigoRuta, String ciudadDestino) {
        if (codigoRuta == null || codigoRuta.trim().isEmpty()) {
            return "Error: El código de ruta no puede estar vacío.";
        }
        String rutaNorm = codigoRuta.trim().toUpperCase();
        boolean agregada = codigosRutasUnicos.add(rutaNorm);
        if (!agregada) {
            return "Duplicado rechazado: La ruta '" + rutaNorm + "' ya se encuentra registrada en el HashSet.";
        }
        if (ciudadDestino != null && !ciudadDestino.trim().isEmpty()) {
            ciudadesDestinoOrdenadas.add(ciudadDestino.trim());
        }
        return "Ruta '" + rutaNorm + "' registrada exitosamente.";
    }

    // Consulta rápida O(1) mediante HashMap
    public Paquete buscarPorCodigo(String codigo) {
        if (codigo == null) return null;
        return paquetesPorCodigo.get(codigo.trim().toUpperCase());
    }

    // Actualiza la prioridad y reordena la PriorityQueue para mantener consistencia
    public String actualizarPrioridad(String codigo, PrioridadEnvio nuevaPrioridad) {
        Paquete paquete = buscarPorCodigo(codigo);
        if (paquete == null) {
            return "No se encontró ningún paquete con código " + codigo;
        }
        if (paquete.getEstado() != EstadoPaquete.EN_BODEGA) {
            return "Solo se puede cambiar la prioridad de paquetes EN_BODEGA (Estado actual: " + paquete.getEstado() + ").";
        }

        // Para que PriorityQueue recalcule su posición en el montículo, se retira y se reingresa
        colaDespachoPrioritario.remove(paquete);
        paquete.setPrioridad(nuevaPrioridad);
        colaDespachoPrioritario.offer(paquete);

        return "Prioridad de " + paquete.getCodigo() + " actualizada a " + nuevaPrioridad + " y reubicada en la cola.";
    }

    // Consulta el siguiente paquete prioritario sin sacarlo de la cola
    public Paquete consultarSiguienteDespacho() {
        return colaDespachoPrioritario.peek();
    }

    // Despacha (extrae) el paquete más urgente de la PriorityQueue y actualiza su estado
    public Paquete despacharSiguientePaquete() {
        Paquete siguiente = colaDespachoPrioritario.poll();
        if (siguiente != null) {
            siguiente.setEstado(EstadoPaquete.EN_RUTA);
        }
        return siguiente;
    }

    // Elimina un paquete sincronizando ArrayList, HashMap y PriorityQueue
    public String eliminarPaquete(String codigo) {
        if (codigo == null) return "Código inválido.";
        String codNorm = codigo.trim().toUpperCase();
        Paquete eliminado = paquetesPorCodigo.remove(codNorm);

        if (eliminado == null) {
            return "No existe el paquete " + codNorm + " para eliminar.";
        }

        historialPaquetes.remove(eliminado);
        colaDespachoPrioritario.remove(eliminado);

        return "Paquete " + codNorm + " eliminado de HashMap, ArrayList y PriorityQueue sin dejar huérfanos.";
    }

    public List<Paquete> getHistorialPaquetes() {
        return Collections.unmodifiableList(historialPaquetes);
    }

    public Set<String> getCodigosRutasUnicos() {
        return Collections.unmodifiableSet(codigosRutasUnicos);
    }

    public Set<String> getCiudadesDestinoOrdenadas() {
        return Collections.unmodifiableSet(ciudadesDestinoOrdenadas);
    }

    public int getTamanoColaPrioridad() {
        return colaDespachoPrioritario.size();
    }

    // Precarga 16 paquetes reales con diferentes prioridades, rutas y clientes para las pruebas
    private void cargarDatosIniciales() {
        Cliente c1 = new Cliente("1047412001", "Laura Gómez", "3001112233");
        Cliente c2 = new Cliente("1047412002", "Carlos Mendoza", "3012223344");
        Cliente c3 = new Cliente("1047412003", "Sofía Herrera", "3023334455");
        Cliente c4 = new Cliente("1047412004", "Andrés Ríos", "3104445566");
        Cliente c5 = new Cliente("1047412005", "Valentina Castro", "3155556677");

        RutaEntrega r1 = new RutaEntrega("RUT-CTG-BOG", "Cartagena", "Bogotá");
        RutaEntrega r2 = new RutaEntrega("RUT-CTG-MED", "Cartagena", "Medellín");
        RutaEntrega r3 = new RutaEntrega("RUT-CTG-BAQ", "Cartagena", "Barranquilla");
        RutaEntrega r4 = new RutaEntrega("RUT-CTG-CAL", "Cartagena", "Cali");
        RutaEntrega r5 = new RutaEntrega("RUT-CTG-SMR", "Cartagena", "Santa Marta");
        RutaEntrega r6 = new RutaEntrega("RUT-CTG-BUC", "Cartagena", "Bucaramanga");

        registrarPaquete("PKG-101", c1, r1, 4.5, PrioridadEnvio.ESTANDAR);
        registrarPaquete("PKG-102", c2, r2, 1.2, PrioridadEnvio.URGENTE);
        registrarPaquete("PKG-103", c3, r3, 8.0, PrioridadEnvio.EXPRESS);
        registrarPaquete("PKG-104", c4, r4, 12.5, PrioridadEnvio.ESTANDAR);
        registrarPaquete("PKG-105", c5, r5, 0.8, PrioridadEnvio.URGENTE);
        registrarPaquete("PKG-106", c1, r6, 3.4, PrioridadEnvio.EXPRESS);
        registrarPaquete("PKG-107", c2, r1, 15.0, PrioridadEnvio.ESTANDAR);
        registrarPaquete("PKG-108", c3, r2, 2.1, PrioridadEnvio.URGENTE);
        registrarPaquete("PKG-109", c4, r3, 6.7, PrioridadEnvio.ESTANDAR);
        registrarPaquete("PKG-110", c5, r4, 1.5, PrioridadEnvio.EXPRESS);
        registrarPaquete("PKG-111", c1, r5, 9.2, PrioridadEnvio.ESTANDAR);
        registrarPaquete("PKG-112", c2, r6, 2.9, PrioridadEnvio.URGENTE);
        registrarPaquete("PKG-113", c3, r1, 5.0, PrioridadEnvio.EXPRESS);
        registrarPaquete("PKG-114", c4, r2, 11.3, PrioridadEnvio.ESTANDAR);
        registrarPaquete("PKG-115", c5, r3, 0.5, PrioridadEnvio.URGENTE);
        registrarPaquete("PKG-116", c1, r4, 7.8, PrioridadEnvio.EXPRESS);
    }
}