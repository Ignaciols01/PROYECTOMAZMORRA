package com.ignaciomanuel.mazmorra.logica.actores;

import com.ignaciomanuel.mazmorra.Principal;
import com.ignaciomanuel.mazmorra.logica.Celda;
import com.ignaciomanuel.mazmorra.logica.TipoCelda;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Protagonista extends Actor {
    private final int maxSalud;
    private int fuerza;
    private int defensa;
    private int velocidad;

    public Protagonista(Celda celda, int salud, int fuerza, int defensa, int velocidad) {
        super(celda);
        this.maxSalud = salud;
        this.setSalud(salud); // usa el método del padre
        this.fuerza = fuerza;
        this.defensa = defensa;
        this.velocidad = velocidad;
    }

    // Getters
    public int getSalud()     { return super.getSalud(); }
    public int getFuerza()    { return fuerza; }
    public int getDefensa()   { return defensa; }
    public int getVelocidad() { return velocidad; }
    public int getMaxSalud()  { return maxSalud; }

    // Setters
    public void setSalud(int salud)         { super.setSalud(salud); }
    public void setFuerza(int fuerza)       { this.fuerza = fuerza; }
    public void setDefensa(int defensa)     { this.defensa = defensa; }
    public void setVelocidad(int velocidad) { this.velocidad = velocidad; }

    /** Daño recibido con mensaje visible */
    @Override
    public void recibirDanio(int cantidad) {
        super.recibirDanio(cantidad);
        Principal.registrarEvento("💢 Protagonista recibió " + cantidad + " de daño. Salud restante: " + getSalud());
    }

    /** Ataca a un enemigo reduciendo su salud */
    public void atacar(Enemigo enemigo) {
        int reduccion = enemigo.getDefensa() / 2;
        int daño = this.fuerza - reduccion;
        if (daño < 1) daño = 1;

        enemigo.recibirDanio(daño);
        Principal.registrarEvento("🗡️ Protagonista ataca por " + daño + " de daño.");
        if (enemigo.getSalud() <= 0) {
            Principal.registrarEvento("🏆 ¡Enemigo derrotado!");
            Celda c = enemigo.getCelda();
            c.setActor(null);
            Principal.eliminarActor(enemigo);
        }
    }

    /** Movimiento con chequeo de trampa y salida */
    @Override
    public void mover(int dx, int dy) {
        int nx = celda.getX() + dx;
        int ny = celda.getY() + dy;
        Celda destino = celda.getMapa().getCelda(nx, ny);
        if (destino == null) return;

        // Trampa
        if (destino.getTipo() == TipoCelda.TRAMPA) {
            this.recibirDanio(10);
            if (!this.estaVivo()) {
                Principal.gameOver();
                return;
            }
        }

        // Salida
        if (destino.getTipo() == TipoCelda.SALIDA) {
            boolean quedan = Principal.getActores().stream().anyMatch(a -> a instanceof Enemigo);
            if (quedan) {
                Principal.registrarEvento("🔒 Elimina todos los enemigos antes de usar la salida.");
            } else {
                Principal.registrarEvento("🎉 ¡Has ganado la partida!");
                Platform.runLater(() -> {
                    Alert alerta = new Alert(AlertType.INFORMATION);
                    alerta.setTitle("¡Victoria!");
                    alerta.setHeaderText(null);
                    alerta.setContentText("Enhorabuena, has ganado.");
                    alerta.showAndWait();
                    Platform.exit();
                });
            }
            return;
        }

        // Movimiento o ataque
        if (destino.getTipo().esTransitable()) {
            Actor occ = destino.getActor();
            if (occ == null) {
                celda.setActor(null);
                destino.setActor(this);
                this.celda = destino;
            } else if (occ instanceof Enemigo) {
                atacar((Enemigo) occ);
            }
        }
    }

    @Override
    public String getNombre() {
        return "Protagonista";
    }
}
