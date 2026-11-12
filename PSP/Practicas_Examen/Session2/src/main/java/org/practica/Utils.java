package org.practica;

import java.util.HashMap;

public class Utils {
    protected boolean todosTurnoActual(HashMap<String, HashMap<Integer, Integer>> jugadores, int turno) {
        String[] nombres = jugadores.keySet().toArray(new String[0]);
        for (String jugador: nombres){
            if(jugadores.get(jugador).size() < turno) return false;
        }
        return true;
    }

    protected int reccoridoJugador(HashMap<String, HashMap<Integer, Integer>> jugadores, String nombre) {
        int recorrido = 0;
        for (int i = 1; i <= jugadores.get(nombre).size(); i++) recorrido += jugadores.get(nombre).get(i);
        return recorrido;
    }
}
