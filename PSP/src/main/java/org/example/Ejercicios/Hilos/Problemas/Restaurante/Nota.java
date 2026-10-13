package org.example.Ejercicios.Hilos.Problemas.Restaurante;

import java.util.ArrayList;
import java.util.List;

public class Nota {
    private final int mesa;
    private final List<TipoPlato> platos = new ArrayList<>();

    public Nota(int mesa, List<TipoPlato> platos) {
        this.mesa = mesa;
        this.platos.addAll(platos);
    }

    public int getMesa() { return mesa; }
    public List<TipoPlato> getPlatos() { return platos; }
}
