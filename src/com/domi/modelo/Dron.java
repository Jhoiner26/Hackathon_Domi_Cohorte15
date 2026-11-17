package com.domi.modelo;

public class Dron extends Repartidor {
    public Dron(String codigo, String nombre) { super(codigo, nombre); }

    @Override
    public double calcularCosto(double km, boolean modoLluvia) { return 4000 + (km * 600); }

    @Override
    public double calcularTiempo(double km) { return (km / 50.0) * 60.0; }

    @Override
    public boolean puedeLlevar(double km, boolean esGrande, boolean modoLluvia) {
        if (modoLluvia) return false;
        return km <= 8.0 && !esGrande;
    }
}