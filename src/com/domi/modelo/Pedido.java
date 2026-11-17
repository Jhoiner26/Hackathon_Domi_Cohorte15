package com.domi.modelo;

public class Pedido {
    private int numero;
    private Cliente cliente;
    private String descripcion;
    private boolean esGrande;
    private double km;
    private Repartidor repartidorAsignado;
    private double costo;
    private double tiempo;
    private String estado;

    public Pedido(int numero, Cliente cliente, String descripcion, boolean esGrande, double km, Repartidor repartidorAsignado, double costo, double tiempo) {
        this.numero = numero;
        this.cliente = cliente;
        this.descripcion = descripcion;
        this.esGrande = esGrande;
        this.km = km;
        this.repartidorAsignado = repartidorAsignado;
        this.costo = costo;
        this.tiempo = tiempo;
        this.estado = "EN_CAMINO";
    }

    public int getNumero() { return numero; }
    public Cliente getCliente() { return cliente; }
    public String getDescripcion() { return descripcion; }
    public boolean isEsGrande() { return esGrande; }
    public double getKm() { return km; }
    public Repartidor getRepartidorAsignado() { return repartidorAsignado; }
    public double getCosto() { return costo; }
    public double getTiempo() { return tiempo; }
    public String getEstado() { return estado; }

    public void setEstado(String estado) {
        if (!estado.equals("EN_CAMINO") && !estado.equals("ENTREGADO") && !estado.equals("CANCELADO")) {
            throw new IllegalArgumentException("Estado inválido.");
        }
        this.estado = estado;
    }
}