package com.domi.modelo;

public class Cliente {
    private String cedula;
    private String nombre;
    private String telefono;
    private String barrio;
    private String direccion;

    public Cliente(String cedula, String nombre, String telefono, String barrio, String direccion) {
        setCedula(cedula);
        setNombre(nombre);
        setTelefono(telefono);
        setBarrio(barrio);
        setDireccion(direccion);
    }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) throw new IllegalArgumentException("Cédula vacía.");
        this.cedula = cedula.trim();
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) throw new IllegalArgumentException("Nombre vacío.");
        this.nombre = nombre.trim();
    }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) throw new IllegalArgumentException("Teléfono vacío.");
        String limpio = telefono.trim();
        if (limpio.length() < 7 || limpio.length() > 10) throw new IllegalArgumentException("Debe tener entre 7 y 10 dígitos.");
        for (int i = 0; i < limpio.length(); i++) {
            if (!Character.isDigit(limpio.charAt(i))) throw new IllegalArgumentException("El teléfono solo debe contener números.");
        }
        this.telefono = limpio;
    }

    public String getBarrio() { return barrio; }
    public void setBarrio(String barrio) {
        if (barrio == null || barrio.trim().isEmpty()) throw new IllegalArgumentException("Barrio vacío.");
        this.barrio = barrio.trim();
    }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) {
        if (direccion == null || direccion.trim().isEmpty()) throw new IllegalArgumentException("Dirección vacía.");
        this.direccion = direccion.trim();
    }
}