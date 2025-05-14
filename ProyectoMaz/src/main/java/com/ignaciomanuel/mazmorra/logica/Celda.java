package com.ignaciomanuel.mazmorra.logica;

import com.ignaciomanuel.mazmorra.Principal;
import com.ignaciomanuel.mazmorra.RecursosGraficos;
import com.ignaciomanuel.mazmorra.logica.actores.Actor;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Celda {
    private final MapaJuego mapa;
    private final int x;
    private final int y;
    private TipoCelda tipo;
    private Actor actor;

    public Celda(MapaJuego mapa, int x, int y, TipoCelda tipo) {
        this.mapa = mapa;
        this.x = x;
        this.y = y;
        this.tipo = tipo;
    }

    public Celda(TipoCelda tipo) {
        this(null, 0, 0, tipo);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public TipoCelda getTipo() {
        return tipo;
    }

    public void setTipo(TipoCelda tipo) {
        this.tipo = tipo;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public MapaJuego getMapa() {
        return mapa;
    }

    public void dibujar(GraphicsContext gc) {
        int px = x * Principal.TAMANO_CELDA;
        int py = y * Principal.TAMANO_CELDA;

        switch (tipo) {
            case PARED:
                gc.drawImage(RecursosGraficos.imagenPared, px, py, 32, 32);
                break;
            case SUELO:
                gc.drawImage(RecursosGraficos.imagenSuelo, px, py, 32, 32);
                break;
            case SALIDA:
                gc.drawImage(RecursosGraficos.imagenMeta, px, py, 32, 32);
                break;
            case TRAMPA:
                gc.setFill(Color.RED);
                gc.fillRect(px, py, 32, 32);
                break;
        }

        if (actor != null) {
            String nombre = actor.getNombre().toLowerCase();
            Image imagenActor = RecursosGraficos.getImagen(nombre);
            if (imagenActor != null) {
                gc.drawImage(imagenActor, px, py, 32, 32);
            }
        }
    }
}
