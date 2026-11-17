package com.domi.modelo;

import java.util.ArrayList;
import java.util.List;

public abstract class Repartidor implements Calificable {
    private String codigo;
    private String nombre;
    private boolean disponible;
    private List<Integer> calificaciones;

    public Repartidor(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.disponible = true;
        this.calificaciones = new ArrayList<>();
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public abstract double calcularCosto(double km, boolean modoLluvia);
    public abstract double calcularTiempo(double km);
    public abstract boolean puedeLlevar(double km, boolean esGrande, boolean modoLluvia);

    @Override
    public void agregarCalificacion(int calificacion) {
        if (calificacion < 1 || calificacion > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5.");
        }
        calificaciones.add(calificacion);
    }

    @Override
    public double obtenerPromedio() {
        if (calificaciones.isEmpty()) return 0.0;
        int suma = 0;
        for (int c : calificaciones) suma += c;
        return (double) suma / calificaciones.size();
    }
}