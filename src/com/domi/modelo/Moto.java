package com.domi.modelo;

public class Moto extends Repartidor {
    public Moto(String codigo, String nombre) { super(codigo, nombre); }

    @Override
    public double calcularCosto(double km, boolean modoLluvia) {
        double costo = 3000 + (km * 1000);
        return modoLluvia ? costo * 1.2 : costo;
    }

    @Override
    public double calcularTiempo(double km) { return (km / 30.0) * 60.0; }

    @Override
    public boolean puedeLlevar(double km, boolean esGrande, boolean modoLluvia) {
        return km <= 15.0 && !esGrande;
    }
}