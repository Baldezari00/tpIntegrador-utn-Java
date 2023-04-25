package org.example.PRODE;

import lombok.*;


public class Pronostico {
    @Getter @Setter
    private Partido partido;
    @Getter
    private ResultadoEnum resultado;

    public Pronostico(ResultadoEnum r){
        resultado=r;
    }

    @Override
    public String toString(){
        String cadena = "aposto por ";
        ResultadoEnum r = getResultado();
        if (r== ResultadoEnum.ganaEquipo1)
            cadena += "el equipo 1 ("+partido.getEquipo1()+")";
        else if (r== ResultadoEnum.ganaEquipo2)
            cadena += "el equipo 2 ("+partido.getEquipo2()+")";
        else
            cadena += "empate";
        return cadena;
    }

}
