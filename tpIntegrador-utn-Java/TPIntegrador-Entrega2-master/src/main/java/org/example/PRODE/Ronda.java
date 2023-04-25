package org.example.PRODE;

import lombok.*;

import java.util.ArrayList;


public class Ronda {
    @Setter @Getter
    private int numero;
    @Setter @Getter
    private ArrayList <Partido> partidos;
    public Ronda(int numeroRonda){
        this.numero = numeroRonda;
        partidos = new ArrayList<>();
    }
    public int getPuntos(ArrayList<Pronostico> pronosticos){
        int puntos=0;
        for (int i =0;i<partidos.size();i++){
            if (partidos.get(i).getResultado().equals(pronosticos.get(i).getResultado()))
                puntos++;
        }
        if (puntos==2){
            puntos = puntos+2;


        }
        return puntos;
    }

    public int puntosExtra(ArrayList<Pronostico> pronosticos,int puntos){
        if (puntos==2){
            puntos = puntos+2;
        }
        return puntos;
    }

    public void setPartido(Partido p){
        partidos.add(p);
    }
}
