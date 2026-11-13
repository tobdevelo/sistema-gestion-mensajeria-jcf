import modelo.*;
import servicio.GestorMensajeria;

import java.util.Scanner;

// Clase principal con menú interactivo adaptado al dominio de gestión de mensajería
public class Main {

    public static void main(String[] args) {
        GestorMensajeria gestor = new GestorMensajeria();
        Scanner scanner = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n=============================================================");
            System.out.println("      SISTEMA DE GESTIÓN DE MENSAJERÍA (JCF - CASO 24)");
            System.out.println("=============================================================");
            System.out.println("1. Registrar nuevo paquete");
            System.out.println("2. Listar historial general de paquetes (ArrayList)");
            System.out.println("3. Buscar paquete por código único (HashMap)");
            System.out.println("4. Registrar / consultar códigos de ruta únicos (HashSet)");
            System.out.println("5. Mostrar ciudades de destino ordenadas (TreeSet)");
            System.out.println("6. Consultar próximo paquete a despachar (PriorityQueue - peek)");
            System.out.println("7. Despachar siguiente paquete prioritario (PriorityQueue - poll)");
            System.out.println("8. Reasignar prioridad de un paquete en bodega");
            System.out.println("9. Cancelar y eliminar paquete del sistema");
            System.out.println("10. Ver estado y conteo de todas las colecciones");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Ingrese un número del menú.");
                continue;
            }

            switch (opcion) {
                case 1 -> {
                    System.out.print("Código del paquete (ej. PKG-117): ");
                    String codigo = scanner.nextLine();
                    System.out.print("Documento del cliente: ");
                    String doc = scanner.nextLine();
                    System.out.print("Nombre del cliente: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Teléfono del cliente: ");
                    String tel = scanner.nextLine();
                    System.out.print("Código de ruta (ej. RUT-CTG-BOG): ");
                    String codRuta = scanner.nextLine();
                    System.out.print("Ciudad origen: ");
                    String origen = scanner.nextLine();
                    System.out.print("Ciudad destino: ");
                    String destino = scanner.nextLine();

                    double peso;
                    try {
                        System.out.print("Peso en kg: ");
                        peso = Double.parseDouble(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Error: El peso debe ser un número válido.");
                        break;
                    }

                    System.out.print("Prioridad (1 = URGENTE, 2 = EXPRESS, 3 = ESTANDAR): ");
                    PrioridadEnvio prioridad = leerPrioridad(scanner.nextLine().trim());
                    if (prioridad == null) {
                        System.out.println("Error: Prioridad no válida.");
                        break;
                    }

                    Cliente cliente = new Cliente(doc, nombre, tel);
                    RutaEntrega ruta = new RutaEntrega(codRuta, origen, destino);
                    System.out.println(gestor.registrarPaquete(codigo, cliente, ruta, peso, prioridad));
                }
                case 2 -> {
                    System.out.println("\n--- HISTORIAL GENERAL DE PAQUETES (ArrayList) ---");
                    for (Paquete p : gestor.getHistorialPaquetes()) {
                        System.out.println(p);
                    }
                }
                case 3 -> {
                    System.out.print("Ingrese el código del paquete a buscar: ");
                    String codBuscar = scanner.nextLine();
                    Paquete encontrado = gestor.buscarPorCodigo(codBuscar);
                    if (encontrado != null) {
                        System.out.println("Resultado en HashMap: " + encontrado);
                    } else {
                        System.out.println("No existe ningún paquete registrado con el código '" + codBuscar + "'.");
                    }
                }
                case 4 -> {
                    System.out.println("Rutas únicas actuales en HashSet: " + gestor.getCodigosRutasUnicos());
                    System.out.print("Ingrese un código de ruta para registrar/probar unicidad: ");
                    String nuevaRuta = scanner.nextLine();
                    System.out.print("Ciudad destino asociada (opcional): ");
                    String ciudadDest = scanner.nextLine();
                    System.out.println(gestor.registrarCodigoRuta(nuevaRuta, ciudadDest));
                }
                case 5 -> {
                    System.out.println("\n--- CIUDADES DE DESTINO ORDENADAS ALFABÉTICAMENTE (TreeSet) ---");
                    for (String ciudad : gestor.getCiudadesDestinoOrdenadas()) {
                        System.out.println(" - " + ciudad);
                    }
                }
                case 6 -> {
                    Paquete proximo = gestor.consultarSiguienteDespacho();
                    if (proximo != null) {
                        System.out.println("Próximo en cola de prioridad (sin retirar): " + proximo);
                    } else {
                        System.out.println("No hay paquetes pendientes en la cola de despacho.");
                    }
                }
                case 7 -> {
                    Paquete despachado = gestor.despacharSiguientePaquete();
                    if (despachado != null) {
                        System.out.println("Paquete despachado (EN_RUTA): " + despachado);
                    } else {
                        System.out.println("No hay paquetes pendientes por despachar.");
                    }
                }
                case 8 -> {
                    System.out.print("Código del paquete a reasignar prioridad: ");
                    String codMod = scanner.nextLine();
                    System.out.print("Nueva prioridad (1 = URGENTE, 2 = EXPRESS, 3 = ESTANDAR): ");
                    PrioridadEnvio nuevaPrio = leerPrioridad(scanner.nextLine().trim());
                    if (nuevaPrio == null) {
                        System.out.println("Error: Prioridad inválida.");
                    } else {
                        System.out.println(gestor.actualizarPrioridad(codMod, nuevaPrio));
                    }
                }
                case 9 -> {
                    System.out.print("Código del paquete a eliminar del sistema: ");
                    String codElim = scanner.nextLine();
                    System.out.println(gestor.eliminarPaquete(codElim));
                }
                case 10 -> {
                    System.out.println("\n--- RESUMEN DE COLECCIONES ACTIVAS ---");
                    System.out.println("1. ArrayList (Historial total): " + gestor.getHistorialPaquetes().size() + " paquetes");
                    System.out.println("2. HashMap (Índice por código): " + gestor.getHistorialPaquetes().size() + " entradas");
                    System.out.println("3. HashSet (Rutas únicas): " + gestor.getCodigosRutasUnicos().size() + " rutas -> " + gestor.getCodigosRutasUnicos());
                    System.out.println("4. TreeSet (Destinos ordenados): " + gestor.getCiudadesDestinoOrdenadas().size() + " ciudades -> " + gestor.getCiudadesDestinoOrdenadas());
                    System.out.println("5. PriorityQueue (Pendientes por despachar): " + gestor.getTamanoColaPrioridad() + " paquetes");
                }
                case 0 -> System.out.println("Cerrando Sistema de Gestión de Mensajería...");
                default -> System.out.println("Opción no reconocida.");
            }
        }
        scanner.close();
    }

    private static PrioridadEnvio leerPrioridad(String entrada) {
        return switch (entrada) {
            case "1", "URGENTE" -> PrioridadEnvio.URGENTE;
            case "2", "EXPRESS" -> PrioridadEnvio.EXPRESS;
            case "3", "ESTANDAR" -> PrioridadEnvio.ESTANDAR;
            default -> null;
        };
    }
}
