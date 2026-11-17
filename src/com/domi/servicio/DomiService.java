package com.domi.servicio;

import com.domi.modelo.*;
import java.util.*;

public class DomiService {
    private HashMap<String, Cliente> clientes;
    private HashSet<String> barriosCobertura;
    private HashMap<String, Double> distanciasBarrios;
    private ArrayList<Repartidor> repartidores;
    private ArrayList<Pedido> pedidos;
    private boolean modoLluvia;
    private int contadorPedidos;

    public DomiService() {
        this.clientes = new HashMap<>();
        this.barriosCobertura = new HashSet<>();
        this.distanciasBarrios = new HashMap<>();
        this.repartidores = new ArrayList<>();
        this.pedidos = new ArrayList<>();
        this.modoLluvia = false;
        this.contadorPedidos = 1;
        cargarDatosArranque();
    }

    private void cargarDatosArranque() {
        Object[][] datosBarrios = {
                {"Chapinero", 1.5}, {"Teusaquillo", 3.0}, {"La Candelaria", 4.5},
                {"Usaquén", 6.0}, {"Cedritos", 8.0}, {"Fontibón", 11.0},
                {"Suba", 12.0}, {"Kennedy", 14.0}, {"Bosa", 18.0}
        };
        for (Object[] d : datosBarrios) {
            String barrio = ((String) d[0]).toLowerCase();
            double km = (Double) d[1];
            barriosCobertura.add(barrio);
            distanciasBarrios.put(barrio, km);
        }

        repartidores.add(new Bici("R-01", "Camila Torres"));
        repartidores.add(new Moto("R-02", "Andrés Mejía"));
        repartidores.add(new Carro("R-03", "Jorge Pardo"));

        clientes.put("1020304050", new Cliente("1020304050", "Laura Gómez", "3001234567", "Teusaquillo", "Calle 53 # 20-10"));
        clientes.put("1030405060", new Cliente("1030405060", "Mateo Rincón", "3109876543", "Usaquén", "Carrera 7 # 140-20"));
    }

    public boolean tieneCobertura(String barrio) {
        if (barrio == null) return false;
        return barriosCobertura.contains(barrio.trim().toLowerCase());
    }

    public double obtenerDistancia(String barrio) {
        if (barrio == null) return -1.0;
        return distanciasBarrios.getOrDefault(barrio.trim().toLowerCase(), -1.0);
    }

    public void registrarCliente(String cedula, String nombre, String telefono, String barrio, String direccion) {
        if (clientes.containsKey(cedula)) throw new IllegalArgumentException("Ya existe un cliente con la cédula " + cedula);
        if (!tieneCobertura(barrio)) throw new IllegalArgumentException("El barrio '" + barrio + "' no tiene cobertura.");
        clientes.put(cedula, new Cliente(cedula, nombre, telefono, barrio, direccion));
    }

    public Collection<Cliente> listarClientes() { return clientes.values(); }

    public void editarCliente(String cedula, String nuevoTelefono, String nuevoBarrio, String nuevaDireccion) {
        Cliente c = clientes.get(cedula);
        if (c == null) throw new IllegalArgumentException("Cliente no encontrado.");
        if (!tieneCobertura(nuevoBarrio)) throw new IllegalArgumentException("El barrio '" + nuevoBarrio + "' no tiene cobertura.");
        c.setTelefono(nuevoTelefono);
        c.setBarrio(nuevoBarrio);
        c.setDireccion(nuevaDireccion);
    }

    public void eliminarCliente(String cedula) {
        if (!clientes.containsKey(cedula)) throw new IllegalArgumentException("Cliente no encontrado.");
        for (Pedido p : pedidos) {
            if (p.getCliente().getCedula().equals(cedula) && p.getEstado().equals("EN_CAMINO")) {
                throw new IllegalArgumentException("No se puede eliminar el cliente porque tiene un pedido en camino.");
            }
        }
        clientes.remove(cedula);
    }

    public void registrarRepartidor(String codigo, String nombre, String tipo) {
        for (Repartidor r : repartidores) {
            if (r.getCodigo().equalsIgnoreCase(codigo)) throw new IllegalArgumentException("El código ya existe.");
        }
        Repartidor nuevo;
        switch (tipo.toLowerCase()) {
            case "bici": nuevo = new Bici(codigo, nombre); break;
            case "moto": nuevo = new Moto(codigo, nombre); break;
            case "carro": nuevo = new Carro(codigo, nombre); break;
            case "dron": nuevo = new Dron(codigo, nombre); break;
            default: throw new IllegalArgumentException("Tipo inválido (bici, moto, carro, dron).");
        }
        repartidores.add(nuevo);
    }

    public ArrayList<Repartidor> listarRepartidores() { return repartidores; }

    public void cambiarDisponibilidad(String codigo) {
        for (Repartidor r : repartidores) {
            if (r.getCodigo().equalsIgnoreCase(codigo)) {
                r.setDisponible(!r.isDisponible());
                return;
            }
        }
        throw new IllegalArgumentException("Repartidor no encontrado.");
    }

    public void activarModoLluvia(boolean activar) { this.modoLluvia = activar; }
    public boolean isModoLluvia() { return modoLluvia; }

    public Pedido crearPedido(String cedulaCliente, String descripcion, boolean esGrande) {
        Cliente cliente = clientes.get(cedulaCliente);
        if (cliente == null) throw new IllegalArgumentException("Cliente no encontrado.");
        double km = obtenerDistancia(cliente.getBarrio());

        Repartidor mejorRepartidor = null;
        double menorCosto = Double.MAX_VALUE;
        double menorTiempo = Double.MAX_VALUE;
        double costoSeleccionado = 0;
        double tiempoSeleccionado = 0;

        for (Repartidor r : repartidores) {
            if (r.isDisponible() && r.puedeLlevar(km, esGrande, modoLluvia)) {
                double costo = r.calcularCosto(km, modoLluvia);
                double tiempo = r.calcularTiempo(km);

                if (costo < menorCosto || (costo == menorCosto && tiempo < menorTiempo)) {
                    menorCosto = costo;
                    menorTiempo = tiempo;
                    mejorRepartidor = r;
                    costoSeleccionado = costo;
                    tiempoSeleccionado = tiempo;
                }
            }
        }

        if (mejorRepartidor == null) throw new IllegalArgumentException("No hay ningún repartidor disponible para este pedido.");

        mejorRepartidor.setDisponible(false);
        Pedido nuevo = new Pedido(contadorPedidos++, cliente, descripcion, esGrande, km, mejorRepartidor, costoSeleccionado, tiempoSeleccionado);
        pedidos.add(nuevo);
        return nuevo;
    }

    public void entregarPedido(int numeroPedido, int calificacion) {
        Pedido p = buscarPedido(numeroPedido);
        if (!p.getEstado().equals("EN_CAMINO")) throw new IllegalArgumentException("El pedido no está EN_CAMINO.");
        p.setEstado("ENTREGADO");
        p.getRepartidorAsignado().setDisponible(true);
        p.getRepartidorAsignado().agregarCalificacion(calificacion);
    }

    public void cancelarPedido(int numeroPedido) {
        Pedido p = buscarPedido(numeroPedido);
        if (!p.getEstado().equals("EN_CAMINO")) throw new IllegalArgumentException("El pedido no está EN_CAMINO.");
        p.setEstado("CANCELADO");
        p.getRepartidorAsignado().setDisponible(true);
    }

    public ArrayList<Pedido> listarPedidos() { return pedidos; }

    private Pedido buscarPedido(int numero) {
        for (Pedido p : pedidos) {
            if (p.getNumero() == numero) return p;
        }
        throw new IllegalArgumentException("Pedido no encontrado.");
    }

    public HashMap<String, Object> cierreDelDia() {
        int total = pedidos.size();
        int entregados = 0, cancelados = 0;
        double ingresosTotales = 0;
        HashMap<String, Double> ingresosPorTipo = new HashMap<>();

        for (Pedido p : pedidos) {
            if (p.getEstado().equals("ENTREGADO")) {
                entregados++;
                ingresosTotales += p.getCosto();
                String tipo = p.getRepartidorAsignado().getClass().getSimpleName();
                ingresosPorTipo.put(tipo, ingresosPorTipo.getOrDefault(tipo, 0.0) + p.getCosto());
            } else if (p.getEstado().equals("CANCELADO")) {
                cancelados++;
            }
        }

        Repartidor estrella = null;
        double mejorPromedio = -1;
        for (Repartidor r : repartidores) {
            double prom = r.obtenerPromedio();
            if (prom > mejorPromedio) {
                mejorPromedio = prom;
                estrella = r;
            }
        }

        HashMap<String, Object> reporte = new HashMap<>();
        reporte.put("total", total);
        reporte.put("entregados", entregados);
        reporte.put("cancelados", cancelados);
        reporte.put("ingresosTotales", ingresosTotales);
        reporte.put("estrella", estrella != null && mejorPromedio > 0 ? estrella.getNombre() + " (" + mejorPromedio + ")" : "Sin calificaciones");
        return reporte;
    }
}
