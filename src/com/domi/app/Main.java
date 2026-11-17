package com.domi.app;

import com.domi.modelo.*;
import com.domi.servicio.DomiService;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DomiService servicio = new DomiService();
        Scanner scanner = new Scanner(System.in);
        int opcion = -1;

        do {
            System.out.println("\n=========================================");
            System.out.println("          DOMI · OPERATOR");
            System.out.println("=========================================");
            if (servicio.isModoLluvia()) {
                System.out.println(" 🌧️ MODO LLUVIA ACTIVO");
            }
            System.out.println("CLIENTES");
            System.out.println(" 1. Registrar             2. Listar");
            System.out.println(" 3. Editar                4. Eliminar");
            System.out.println("REPARTIDORES");
            System.out.println(" 5. Registrar             6. Listar");
            System.out.println(" 7. Cambiar disponibilidad");
            System.out.println("PEDIDOS");
            System.out.println(" 8. Crear pedido (cotiza y asigna)");
            System.out.println(" 9. Entregar             10. Cancelar");
            System.out.println(" 11. Listar pedidos");
            System.out.println("OPERACIÓN");
            System.out.println(" 12. Activar / desactivar lluvia");
            System.out.println(" 13. Cierre del día");
            System.out.println(" 0. Salir");
            System.out.println("=========================================");
            System.out.print("Elija una opción: ");

            try {
                String input = scanner.nextLine();
                opcion = Integer.parseInt(input);

                switch (opcion) {
                    case 1:
                        System.out.print("Cédula: "); String ced = scanner.nextLine();
                        System.out.print("Nombre: "); String nom = scanner.nextLine();
                        System.out.print("Teléfono: "); String tel = scanner.nextLine();
                        System.out.print("Barrio: "); String bar = scanner.nextLine();
                        System.out.print("Dirección: "); String dir = scanner.nextLine();
                        servicio.registrarCliente(ced, nom, tel, bar, dir);
                        System.out.println("¡Cliente registrado con éxito!");
                        break;
                    case 2:
                        System.out.println("\n--- LISTA DE CLIENTES ---");
                        if (servicio.listarClientes().isEmpty()) {
                            System.out.println("No hay clientes registrados.");
                        } else {
                            for (Cliente c : servicio.listarClientes()) {
                                System.out.println("Cédula: " + c.getCedula() + " | Nombre: " + c.getNombre() + " | Tel: " + c.getTelefono() + " | Barrio: " + c.getBarrio() + " | Dir: " + c.getDireccion());
                            }
                        }
                        break;
                    case 3:
                        System.out.print("Cédula del cliente a editar: "); String cEdit = scanner.nextLine();
                        System.out.print("Nuevo teléfono: "); String nTel = scanner.nextLine();
                        System.out.print("Nuevo barrio: "); String nBar = scanner.nextLine();
                        System.out.print("Nueva dirección: "); String nDir = scanner.nextLine();
                        servicio.editarCliente(cEdit, nTel, nBar, nDir);
                        System.out.println("¡Cliente actualizado!");
                        break;
                    case 4:
                        System.out.print("Cédula del cliente a eliminar: "); String cDel = scanner.nextLine();
                        servicio.eliminarCliente(cDel);
                        System.out.println("¡Cliente eliminado!");
                        break;
                    case 5:
                        System.out.print("Código (ej. R-04): "); String rCod = scanner.nextLine();
                        System.out.print("Nombre: "); String rNom = scanner.nextLine();
                        System.out.print("Tipo (bici/moto/carro/dron): "); String rTipo = scanner.nextLine();
                        servicio.registrarRepartidor(rCod, rNom, rTipo);
                        System.out.println("¡Repartidor registrado!");
                        break;
                    case 6:
                        System.out.println("\n--- LISTA DE REPARTIDORES ---");
                        for (Repartidor r : servicio.listarRepartidores()) {
                            System.out.println(r.getCodigo() + " - " + r.getNombre() + " (" + r.getClass().getSimpleName() + ") | Disp: " + (r.isDisponible() ? "Sí" : "No") + " | Promedio: " + r.obtenerPromedio());
                        }
                        break;
                    case 7:
                        System.out.print("Código del repartidor: "); String rDisp = scanner.nextLine();
                        servicio.cambiarDisponibilidad(rDisp);
                        System.out.println("¡Disponibilidad cambiada!");
                        break;
                    case 8:
                        System.out.print("Cédula del cliente: "); String cPed = scanner.nextLine();
                        System.out.print("Descripción del pedido: "); String desc = scanner.nextLine();
                        System.out.print("¿Es un pedido grande? (s/n): "); boolean grande = scanner.nextLine().equalsIgnoreCase("s");
                        Pedido nuevo = servicio.crearPedido(cPed, desc, grande);
                        System.out.println("Pedido #" + nuevo.getNumero() + " asignado a " + nuevo.getRepartidorAsignado().getNombre() + " (" + nuevo.getRepartidorAsignado().getClass().getSimpleName() + ") - Costo: $" + nuevo.getCosto() + " - Tiempo: " + nuevo.getTiempo() + " min [EN_CAMINO]");
                        break;
                    case 9:
                        System.out.print("Número del pedido a entregar: "); int nEnt = Integer.parseInt(scanner.nextLine());
                        System.out.print("Calificación del cliente (1 a 5): "); int calif = Integer.parseInt(scanner.nextLine());
                        servicio.entregarPedido(nEnt, calif);
                        System.out.println("¡Pedido marcado como ENTREGADO y calificado!");
                        break;
                    case 10:
                        System.out.print("Número del pedido a cancelar: "); int nCan = Integer.parseInt(scanner.nextLine());
                        servicio.cancelarPedido(nCan);
                        System.out.println("¡Pedido CANCELADO y repartidor liberado!");
                        break;
                    case 11:
                        System.out.println("\n--- LISTA DE PEDIDOS ---");
                        for (Pedido p : servicio.listarPedidos()) {
                            System.out.println("Pedido #" + p.getNumero() + " | Cliente: " + p.getCliente().getNombre() + " | Estado: " + p.getEstado() + " | Costo: $" + p.getCosto());
                        }
                        break;
                    case 12:
                        servicio.activarModoLluvia(!servicio.isModoLluvia());
                        System.out.println("Modo lluvia cambiado. Estado actual: " + (servicio.isModoLluvia() ? "ACTIVO" : "INACTIVO"));
                        break;
                    case 13:
                        var reporte = servicio.cierreDelDia();
                        System.out.println("\n=== CIERRE DEL DÍA ===");
                        System.out.println("Total de pedidos: " + reporte.get("total"));
                        System.out.println("Entregados: " + reporte.get("entregados"));
                        System.out.println("Cancelados: " + reporte.get("cancelados"));
                        System.out.println("Ingresos totales (Entregados): $" + reporte.get("ingresosTotales"));
                        System.out.println("Repartidor estrella: " + reporte.get("estrella"));
                        break;
                    case 0:
                        System.out.println("Saliendo de la aplicación... ¡Buen trabajo!");
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("[Error] Debe ingresar un número válido.");
            } catch (IllegalArgumentException e) {
                System.out.println("[Alerta de Negocio] " + e.getMessage());
            } catch (Exception e) {
                System.out.println("[Error Inesperado] " + e.getMessage());
            }

        } while (opcion != 0);

        scanner.close();
    }
}