package com.ignaciomanuel.mazmorra.logica.actores;

import com.ignaciomanuel.mazmorra.logica.Celda;

public abstract class Actor {
    protected Celda celda;
    protected int salud; // ✅ salud agregada

    public Actor(Celda celda) {
        this.celda = celda;
        this.celda.setActor(this);
    }

    public abstract int getVelocidad();
    public abstract String getNombre();

    public Celda getCelda() {
        return celda;
    }

    public void mover(int dx, int dy) {
        int nuevoX = celda.getX() + dx;
        int nuevoY = celda.getY() + dy;
        Celda destino = celda.getMapa().getCelda(nuevoX, nuevoY);

        if (destino != null && destino.getTipo().esTransitable() && destino.getActor() == null) {
            celda.setActor(null);
            destino.setActor(this);
            this.celda = destino;
        }
    }

    // ✅ Método para recibir daño
    public void recibirDanio(int cantidad) {
        salud -= cantidad;
        if (salud < 0) salud = 0;
    }

    // ✅ Método para comprobar si sigue vivo
    public boolean estaVivo() {
        return salud > 0;
    }

    // ✅ Getter y setter de salud por si se necesitan
    public int getSalud() {
        return salud;
    }

    public void setSalud(int salud) {
        this.salud = salud;
    }
}
