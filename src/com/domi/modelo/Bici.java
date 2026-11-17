package com.domi.modelo;

public class Bici extends Repartidor {
    public Bici(String codigo, String nombre) { super(codigo, nombre); }

    @Override
    public double calcularCosto(double km, boolean modoLluvia) {
        double costo = 2000 + (km * 800);
        return modoLluvia ? costo * 1.2 : costo;
    }

    @Override
    public double calcularTiempo(double km) { return (km / 15.0) * 60.0; }

    @Override
    public boolean puedeLlevar(double km, boolean esGrande, boolean modoLluvia) {
        if (modoLluvia) return false;
        return km <= 5.0 && !esGrande;
    }
}