
package org.practica.tresEnRaya;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/TresEnRaya")
public class TresEnRaya extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String coordenada = req.getParameter("coordenada");

        String jugador1 = (String) req.getSession().getAttribute("jugador1");
        String jugador2 = (String) req.getSession().getAttribute("jugador2");
        String marca1 = (String) req.getSession().getAttribute("marca1");
        String marca2 = (String) req.getSession().getAttribute("marca2");

        Boolean empiezaJugador1 = (Boolean) req.getSession().getAttribute("empiezaJugador1");
        Boolean partidaEnCurso = (Boolean) req.getSession().getAttribute("partidaEnCurso");

        String[] tablero = (String[]) req.getSession().getAttribute("Tablero");
        String marca = (String) req.getSession().getAttribute("marca");
        Integer numTurnos = (Integer) req.getSession().getAttribute("numTurnos");
        ArrayList<Partida> partidas = (ArrayList<Partida>) req.getSession().getAttribute("partidas");
        ArrayList<String> movimientos = (ArrayList<String>) req.getSession().getAttribute("movimientos");

        boolean pasarTurno = false;
        boolean reiniciar = false;
        boolean partidaTerminada = false;
        Partida partida = new Partida();
        String marcaVacia = "-";

        if (partidaEnCurso == null || !partidaEnCurso) {
            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "No hay ninguna partida preparada."
            );
            return;
        }

        if(marca1.equals(marcaVacia) || marca2.equals(marcaVacia)) {
            if (marca1.equals("·") || marca2.equals("·")) marcaVacia = "=";
            else marcaVacia = "·";
        }

        if (tablero == null) tablero = new String[]{marcaVacia, marcaVacia, marcaVacia, marcaVacia, marcaVacia, marcaVacia, marcaVacia, marcaVacia, marcaVacia};

        if (marca == null) marca = empiezaJugador1 ? marca1 : marca2;

        if (numTurnos == null) numTurnos = 0;

        if (partidas == null) partidas = new ArrayList<>();

        if (movimientos == null) movimientos = new ArrayList<>();

        if (coordenada != null) {
            if (coordenada.equals("No")) pasarTurno = true;
            if (coordenada.equals("Reiniciar")) reiniciar = true;
        }

        if (reiniciar) {
            tablero = new String[]{marcaVacia, marcaVacia, marcaVacia, marcaVacia, marcaVacia, marcaVacia, marcaVacia, marcaVacia, marcaVacia};
            marca = empiezaJugador1 ? marca1 : marca2;
            numTurnos = 0;
            movimientos = new ArrayList<>();
        }
        else if (pasarTurno) {
            marca = marca.equals(marca1) ? marca2 : marca1;
        }
        else if (coordenada != null) {
            int posicion;

            try {
                posicion = Integer.parseInt(coordenada) - 1;

                if (posicion < 0 || posicion > 8) {
                    resp.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "La coordenada tiene que estar entre 1 y 9."
                    );
                    return;
                }

                if (!tablero[posicion].equals("-")) {
                    resp.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Esa casilla ya está ocupada."
                    );
                    return;
                }

            } catch (NumberFormatException e) {
                resp.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "La coordenada tiene que ser un número del 1 al 9, \"No\" para pasar turno o \"Reiniciar\" para reiniciar."
                );
                return;
            }

            tablero[posicion] = marca;
            numTurnos++;
            movimientos.add((marca.equals(marca1) ? jugador1 : jugador2) + " - " + marca + " - Casilla " + (posicion + 1));

            if (hayGanador(tablero, marca)) {
                String ganador;
                String perdedor;

                if (marca.equals(marca1)) {
                    ganador = jugador1;
                    perdedor = jugador2;
                } else {
                    ganador = jugador2;
                    perdedor = jugador1;
                }

                partida.setJugador1(jugador1);
                partida.setMarca1(marca1);
                partida.setJugador2(jugador2);
                partida.setMarca2(marca2);
                partida.setEmpiezaJugador(empiezaJugador1 ? jugador1 : jugador2);
                partida.setGanador(ganador);
                partida.setPerdedor(perdedor);
                partida.setEmpate(false);
                partida.setNumTurnos(numTurnos);
                partida.setMovimientos(new ArrayList<>(movimientos));

                partidas.add(partida);

                partidaTerminada = true;

            } else if (numTurnos == 9) {
                partida.setJugador1(jugador1);
                partida.setMarca1(marca1);
                partida.setJugador2(jugador2);
                partida.setMarca2(marca2);
                partida.setEmpiezaJugador(empiezaJugador1 ? jugador1 : jugador2);
                partida.setGanador(null);
                partida.setPerdedor(null);
                partida.setEmpate(true);
                partida.setNumTurnos(numTurnos);
                partida.setMovimientos(new ArrayList<>(movimientos));

                partidas.add(partida);


                partidaTerminada = true;
            } else marca = marca.equals(marca1) ? marca2 : marca1;
        }

        String txtTurno;

        if (partidaTerminada) {
            if (hayGanador(tablero, marca)) {
                String ganador = marca.equals(marca1) ? jugador1 : jugador2;

                txtTurno = "Ha ganado: " + ganador;
            } else txtTurno = "La partida ha terminado en empate.";

            txtTurno += "<br>Movimientos realizados: " + numTurnos;

            req.getSession().removeAttribute("Tablero");
            req.getSession().removeAttribute("marca");
            req.getSession().removeAttribute("numTurnos");
            req.getSession().removeAttribute("jugador1");
            req.getSession().removeAttribute("jugador2");
            req.getSession().removeAttribute("marca1");
            req.getSession().removeAttribute("marca2");
            req.getSession().removeAttribute("empiezaJugador1");
            req.getSession().removeAttribute("movimientos");

            req.getSession().setAttribute("partidas", partidas);
            req.getSession().setAttribute("partidaEnCurso", false);
        } else {
            String jugadorTurno = marca.equals(marca1) ? jugador1 : jugador2;

            txtTurno = "Turno de " + jugadorTurno + " con marca: " + marca;
            txtTurno += "<br>Movimientos realizados: " + numTurnos;

            req.getSession().setAttribute("Tablero", tablero);
            req.getSession().setAttribute("marca", marca);
            req.getSession().setAttribute("numTurnos", numTurnos);
            req.getSession().setAttribute("movimientos", movimientos);
        }

        String fila1 = "<p class=\"casilla\">" + tablero[0] + "</p> <p class=\"casilla\">" + tablero[1] + "</p> <p class=\"casilla\">" + tablero[2] + "</p>";
        String fila2 = "<p class=\"casilla\">" + tablero[3] + "</p> <p class=\"casilla\">" + tablero[4] + "</p> <p class=\"casilla\">" + tablero[5] + "</p>";
        String fila3 = "<p class=\"casilla\">" + tablero[6] + "</p> <p class=\"casilla\">" + tablero[7] + "</p> <p class=\"casilla\">" + tablero[8] + "</p>";

        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);

        ctx.setVariable("txtTurno", txtTurno);
        ctx.setVariable("fila1", fila1);
        ctx.setVariable("fila2", fila2);
        ctx.setVariable("fila3", fila3);

        resp.setContentType("text/html;charset=UTF-8");

        ((TemplateEngine) getServletContext().getAttribute("templateEngine")).process("tresEnRaya", ctx, resp.getWriter());
    }

    private boolean hayGanador(String[] tablero, String marca) {
        int[][] combinaciones = {
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8},
                {0, 3, 6},
                {1, 4, 7},
                {2, 5, 8},
                {0, 4, 8},
                {2, 4, 6}
        };

        for (int[] pos : combinaciones) {
            if (tablero[pos[0]].equals(marca) && tablero[pos[1]].equals(marca) && tablero[pos[2]].equals(marca)) return true;
        }

        return false;
    }
}
