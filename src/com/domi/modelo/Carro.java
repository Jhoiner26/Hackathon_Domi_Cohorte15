package com.domi.modelo;

public class Carro extends Repartidor {
    public Carro(String codigo, String nombre) { super(codigo, nombre); }

    @Override
    public double calcularCosto(double km, boolean modoLluvia) {
        double costo = 5000 + (km * 1200);
        return modoLluvia ? costo * 1.2 : costo;
    }

    @Override
    public double calcularTiempo(double km) { return (km / 25.0) * 60.0; }

    @Override
    public boolean puedeLlevar(double km, boolean esGrande, boolean modoLluvia) { return true; }
}