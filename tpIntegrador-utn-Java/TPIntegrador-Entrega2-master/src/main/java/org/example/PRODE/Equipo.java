package org.example.PRODE;

import lombok.*;
@RequiredArgsConstructor
public class Equipo {

    @Getter @Setter @NonNull
    private String nombre;
    @Getter @Setter
    private String descripcion;


    @Override
    public String toString(){
        return nombre;
    }

}
