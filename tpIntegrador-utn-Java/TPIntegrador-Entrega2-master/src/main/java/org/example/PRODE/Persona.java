package org.example.PRODE;

import lombok.*;

import java.util.ArrayList;
public class Persona {
    @Getter @Setter
    private String nombre;
    @Setter @Getter
    private ArrayList<Pronostico> pronosticos;
    @Getter
    private int puntos;
    @Setter @Getter
    private boolean acertoTodasRondas;
    public Persona(String n){
        nombre = n;
        pronosticos = new ArrayList<>();
    }
    public void agregarPronostico(Pronostico p){
        pronosticos.add(p);
    }

    @Override
    public String toString(){
        return nombre;
    }

    public void setPartido(int i,Partido partido){
        pronosticos.get(i).setPartido(partido);
    }

    //devuelve los pronosticos que la persona hizo, unicamente de la ronda que se le pide
    public ArrayList<Pronostico> getPronosticosRonda(int numeroRonda){
        ArrayList<Pronostico> pronosticosRonda = new ArrayList<>();
        for (Pronostico p : pronosticos) {
            if (p.getPartido().getNRonda() == numeroRonda)
                pronosticosRonda.add(p);
        }
        return pronosticosRonda;
    }

    public void sumarPuntos(int n){
        puntos+=n;
    }


}
