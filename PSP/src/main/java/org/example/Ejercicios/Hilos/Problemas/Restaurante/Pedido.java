package org.example.Ejercicios.Hilos.Problemas.Restaurante;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Pedido {
    static Random rand = new Random();
    private final List<TipoPlato> platos = new ArrayList<TipoPlato>();
    private final int mesa;
    private final Boolean abierto;

    public Pedido(int mesa,  Boolean fin) {
        this.mesa = mesa;
        this.abierto = fin;
        int platosNum = rand.nextInt(10) + 1;
        for(int i = 0; i < platosNum; i++) platos.add(TipoPlato.values()[rand.nextInt(TipoPlato.values().length)]);
    }

    public Nota tomarNota() {
        return new Nota(mesa, platos);
    }

    public int getMesa() { return mesa; }

    public Boolean getAbierto() { return abierto; }
}

