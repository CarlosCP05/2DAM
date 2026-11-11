
package org.practica.tresEnRaya;

import java.util.ArrayList;

public class Partida {
    private String jugador1;
    private String marca1;
    private String jugador2;
    private String marca2;
    private String empiezaJugador;
    private String ganador;
    private String perdedor;
    private boolean empate;
    private int numTurnos;
    private ArrayList<String> movimientos = new ArrayList<>();

    public String getJugador1() { return jugador1; }
    public void setJugador1(String jugador1) { this.jugador1 = jugador1; }

    public String getMarca1() { return marca1; }
    public void setMarca1(String marca1) { this.marca1 = marca1; }

    public String getJugador2() { return jugador2; }
    public void setJugador2(String jugador2) { this.jugador2 = jugador2; }

    public String getMarca2() { return marca2; }
    public void setMarca2(String marca2) { this.marca2 = marca2; }

    public String getEmpiezaJugador() { return empiezaJugador; }
    public void setEmpiezaJugador(String empiezaJugador) { this.empiezaJugador = empiezaJugador; }

    public String getGanador() { return ganador; }
    public void setGanador(String ganador) { this.ganador = ganador; }

    public String getPerdedor() { return perdedor; }
    public void setPerdedor(String perdedor) { this.perdedor = perdedor; }

    public boolean isEmpate() { return empate; }
    public void setEmpate(boolean empate) { this.empate = empate; }

    public int getNumTurnos() { return numTurnos; }
    public void setNumTurnos(int numTurnos) { this.numTurnos = numTurnos; }

    public ArrayList<String> getMovimientos() { return movimientos; }
    public void setMovimientos(ArrayList<String> movimientos) { this.movimientos = movimientos; }
}
