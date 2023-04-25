package org.example.PRODE;

import lombok.*;

@RequiredArgsConstructor
public class Partido {
    @Setter @Getter @NonNull
    private int nRonda;
    @NonNull
    private Equipo equipo1;
    @NonNull
    private Equipo equipo2;
    @Setter @Getter
    private int goles1;
    @Getter
    private int goles2;
    @Getter
    private ResultadoEnum resultado;


    private void setResultado(){ //compara la cantidad de goles y establece el resultado como un enum
        if (goles1>goles2)
            resultado = ResultadoEnum.ganaEquipo1;
        else if (goles1<goles2)
            resultado = ResultadoEnum.ganaEquipo2;
        else
            resultado = ResultadoEnum.empate;
    }

    public String getEquipo1() {
        return equipo1.getNombre();
    }

    public String getEquipo2() {
        return equipo2.getNombre();
    }

    public void setGoles2(int goles2){
        this.goles2 = goles2;
        setResultado();
    }
}

