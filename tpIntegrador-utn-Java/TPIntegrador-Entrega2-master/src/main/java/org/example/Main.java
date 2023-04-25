package org.example;

import org.example.PRODE.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws IOException, DatosErroneosException {
        // conexion DB
        String url = "jdbc:mysql://localhost:3306/db_tpintegrador_utn?serverTimezone=UTC";
        String username = "root";
        String password = "BaldeDB1qsefthukoñ12$7bdzr";


        // array partido
        String partidoDb;
        ArrayList<String> arrayPartidoDb = new ArrayList<>();
        try{
            Connection conn = DriverManager.getConnection(url,username,password);
            Statement statement = conn.createStatement();
            ResultSet rsR = statement.executeQuery("SELECT * FROM partido");

            //leer la tabla partido y almacenar los datos
            while (rsR.next()){
                partidoDb = rsR.getString(1)+";"+rsR.getString(2)+";"+rsR.getString(3)+";"+rsR.getString(4)+";"+rsR.getString(5);
                arrayPartidoDb.add(partidoDb);
            }
        }catch (Exception e) {
            e.getMessage();
        }
        // array pronostico
         String pronosticoDb;
         ArrayList<String> arrayPronosticoDb = new ArrayList<>();

//----------------------------------------------------------------------------------------------------------------------

        try{
            Connection conn = DriverManager.getConnection(url,username,password);
            Statement statement = conn.createStatement();
            ResultSet rsP = statement.executeQuery("SELECT * FROM pronostico");

            // leer la tabla pronostico y almacenar los datos
            while (rsP.next()){
                pronosticoDb = rsP.getString(1)+";"+rsP.getString(2)+";"+rsP.getString(3)+";"+rsP.getString(4)+";"+rsP.getString(5)+";"+rsP.getString(6);
                arrayPronosticoDb.add(pronosticoDb);
            }

            conn.close();
            statement.close();
            rsP.close();

        }catch (SQLException e){
            e.getMessage();
        }catch (Exception e){
            e.getMessage();
        }
//----------------------------------------------------------------------------------------------------------------------


        //Aca se almacenaran los equipos 1 de los x partidos
        ArrayList<Equipo> equipos1 = new ArrayList<>();
        //Aca se almacenaran los equipos 2 de los x partidos
        ArrayList<Equipo> equipos2 = new ArrayList<>();
        //Aca se almacenaran los equipos 2 de los x partidos
        ArrayList<Partido> partidos = new ArrayList<>();
        //Aca se almacenaran todos los partidos
        ArrayList<Ronda> rondas = new ArrayList<>();
        HashMap<String,Persona> personas = new HashMap<>();

        //Creamos los equipos con la informacion obtenida
        for (String s : arrayPartidoDb) {
            equipos1.add(new Equipo(s.split(";")[1]));
        }
        for (String s : arrayPartidoDb) {
            equipos2.add(new Equipo(s.split(";")[4]));
        }

        //creamos los partidos con su numero de ronda, los equipos y los goles
        for (int i=0;i<arrayPartidoDb.size();i++){
            partidos.add(new Partido(Integer.parseInt(arrayPartidoDb.get(i).split(";")[0]),equipos1.get(i),equipos2.get(i)));
            partidos.get(i).setGoles1(Integer.parseInt(arrayPartidoDb.get(i).split(";")[2])); //Cargo los goles del equipo 1 del partido i
            partidos.get(i).setGoles2(Integer.parseInt(arrayPartidoDb.get(i).split(";")[3])); //Cargo los goles del equipo 2 del partido i
        }

        //creamos las rondas correspondientes
        for(int i=0;i<partidos.size();i++){
            if (rondas.size()==0)
                rondas.add(new Ronda(1));
            else if(partidos.get(i).getNRonda()!=partidos.get(i-1).getNRonda()){
                int numero = rondas.size()+1;
                rondas.add(new Ronda(numero));
            }
        }

        //asignamos los partidos de las rondas
        for (Ronda r : rondas)
            for (Partido p : partidos) {
                if (p.getNRonda() == r.getNumero())
                    r.setPartido(p);
            }

        for (String s : arrayPronosticoDb) {
            String nombrePersona = s.split(";")[0];
            personas.put(nombrePersona,new Persona(nombrePersona));
        }

        //Cargamos los pronosticos de cada uno de los partidos de cada una de las personas
        for (String s : arrayPronosticoDb) {
            String nombrePersona = s.split(";")[0];
            personas.get(nombrePersona).agregarPronostico(new Pronostico(obtenerResultado(s.split(";"))));
        }

        //Le asignamos los partidos a cada uno de estos pronosticos de las personas
        for (Persona p : personas.values()){
            for (int j=0;j< partidos.size();j++){
                p.setPartido(j,partidos.get(j));//le asignamos el partido sobre el cual hizo el pronostico
            }
        }

        //imprimimos los pronosticos por ronda
        for (Ronda r : rondas){
            System.out.println("\t\t\tRONDA NUMERO "+r.getNumero()+":"); //imprimimos de cada ronda
            for (int i=0;i< r.getPartidos().size();i++){
                System.out.println();
                System.out.print("Partido nº"+(i+1)+": ("+r.getPartidos().get(i).getEquipo1()+ " - "+r.getPartidos().get(i).getEquipo2()+"): " + r.getPartidos().get(i).getResultado() +"\n");
                for (Persona p : personas.values()){
                    System.out.println("\t_ " + p + " " + p.getPronosticosRonda(r.getNumero()).get(i)); //imprimimos el pronostico de cada persona
                }

            }
            //imprimimos el resultado
            System.out.println();
            for (Persona p : personas.values()) {
                System.out.print("\t" + p + " sumo " + r.getPuntos(p.getPronosticosRonda(r.getNumero())) + " punto/s en la ronda " + r.getNumero());

                p.sumarPuntos(r.getPuntos(p.getPronosticosRonda(r.getNumero())));
                if (r.getPuntos(p.getPronosticosRonda(r.getNumero()))== 4){
                    System.out.println("\t"+"( +2 puntos extra por haber acertado todos los resultados)");
                }

                // al multiplicar la cantidad de rondas por la cantidad de partidos obtenemos la cantidad de partidos y si a esa cantidad la
                // multiplicamos *2 obtenemos el resultado de lo que sería acertar todos los partidos y obtener los puntos extra
                if (p.getPuntos() == rondas.size() * r.getPartidos().size() * 2) {
                    p.sumarPuntos(5);
                    p.setAcertoTodasRondas(true);

                }
            }
            System.out.println();
            System.out.println("-----------------------------------------------------------------------------");
            // mensaje felicitando si acerto todas las rondas
            for (Persona p :personas.values() ) {
                if (p.isAcertoTodasRondas()== true) {
                    System.out.println("\t"+"Felicidades "+p.getNombre()+"! ganaste 5 puntos extra por haber acertado todas las rondas");
                    System.out.println();

                }
            }
        }


        //imprimimos el total
        System.out.printf("%17s| %17s| %17s|","NOMBRE", "PUNTOS", "ACIERTOS");
        System.out.println();
        for(Persona p : personas.values()) {
            System.out.printf("%17s  %17s  %17s ", p, p.getPuntos(), p.getPuntos());
            System.out.println();
        }
    }
    public static ResultadoEnum obtenerResultado(String[] apuesta) { //Verifica en que "celda" la persona marco para apostar por dicho equipo y lo establece como resultado en un Enum
        ResultadoEnum resultado;
        if (apuesta[2].equals("1") )
            resultado = ResultadoEnum.ganaEquipo1;
        else if (apuesta[4].equals("1"))
            resultado = ResultadoEnum.ganaEquipo2;
        else
            resultado = ResultadoEnum.empate;
        return resultado;
    }
}
