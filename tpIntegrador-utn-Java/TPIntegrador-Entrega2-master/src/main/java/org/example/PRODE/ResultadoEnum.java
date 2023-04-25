package org.example.PRODE;

public enum ResultadoEnum {
    ganaEquipo1("El equipo 1 gano el partido"), ganaEquipo2("El equipo 2 gano el partido"), empate("El partido termino en empate");
    private String descripcion;
    ResultadoEnum(String descripcion) {
        this.descripcion = descripcion;
    }

    public String toString(){
        return descripcion;
    }
}
