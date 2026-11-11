
package org.practica.tresEnRaya;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

@WebServlet("/Estadisticas")
public class Estadisticas extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String nombre = req.getParameter("nombre");
        String numPartida = req.getParameter("partida");

        ArrayList<Partida> partidas = (ArrayList<Partida>) req.getSession().getAttribute("partidas");

        if (partidas == null) partidas = new ArrayList<>();

        resp.setContentType("text/html;charset=utf-8");
        resp.setCharacterEncoding("utf-8");

        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Estadísticas</title>");
        out.println("</head>");
        out.println("<body>");

        if (numPartida != null && !numPartida.isEmpty()) {
            int partidaSel;

            try { partidaSel = Integer.parseInt(numPartida); }
            catch (NumberFormatException e) { partidaSel = -1; }

            if (partidaSel < 1 || partidaSel > partidas.size()) {
                out.println("<h3>" + (partidaSel < 1 ? "Error al introducir la partida " : "La partida indicada no existe") + ".</h3>");
            }
            else {
                Partida partida = partidas.get(partidaSel - 1);

                out.println("<h1>Partida " + partidaSel + "</h1>");
                out.println("<h3>Jugadores:</h3>");
                out.println("<p>" + partida.getJugador1() + " - " + partida.getMarca1() + "</p>");
                out.println("<p>" + partida.getJugador2() + " - " + partida.getMarca2() + "</p>");
                out.println("<h3>Empieza: " + partida.getEmpiezaJugador() + "</h3>");
                if (partida.isEmpate()) {
                    out.println("<h2>Resultado: Empate</h2>");
                } else {
                    out.println("<h2>Ganador: " + partida.getGanador() + "</h2>");
                    out.println("<h3>Perdedor: " + partida.getPerdedor() + "</h3>");
                }
                out.println("<h3>Movimientos: " + partida.getNumTurnos() + "</h3>");
                out.println("<h2>Historial de movimientos:</h2>");
                for (String movimiento : partida.getMovimientos()) {
                    out.println("<p>" + movimiento + "</p>");
                }
            }
        } else if (nombre != null && !nombre.isEmpty()) {
            boolean encontrado = false;
            out.println("<h1>Partidas de " + nombre + "</h1>");

            for (int i = 0; i < partidas.size(); i++) {
                Partida partida = partidas.get(i);
                if (Objects.equals(nombre, partida.getJugador1()) || Objects.equals(nombre, partida.getJugador2())) {
                    encontrado = true;

                    String marca;
                    String contrincante;
                    String resultado;

                    if (Objects.equals(nombre, partida.getJugador1())) {
                        marca = partida.getMarca1();
                        contrincante = partida.getJugador2();
                    } else {
                        marca = partida.getMarca2();
                        contrincante = partida.getJugador1();
                    }

                    if (partida.isEmpate()) resultado = "Empate";
                    else if (Objects.equals(nombre, partida.getGanador())) resultado = "Victoria";
                    else resultado = "Derrota";

                    out.println("<h3>Partida " + (i + 1) + "</h3>");
                    out.println("<p>Marca utilizada: " + marca + "</p>");
                    out.println("<p>Resultado: " + resultado + "</p>");
                    out.println("<p>Contrincante: " + contrincante + "</p>");
                    out.println("<p>Movimientos: " + partida.getNumTurnos() + "</p>");
                }
            }

            if (!encontrado) {
                out.println("<h3>Este jugador no ha participado en ninguna partida.</h3>");
            }

        } else {
            LinkedHashMap<String, Integer> victorias = new LinkedHashMap<>();

            for (Partida partida : partidas) {

                if (!victorias.containsKey(partida.getJugador1())) victorias.put(partida.getJugador1(), 0);

                if (!victorias.containsKey(partida.getJugador2())) victorias.put(partida.getJugador2(), 0);

                if (!partida.isEmpate()) {
                    victorias.put(
                            partida.getGanador(),
                            victorias.get(partida.getGanador()) + 1
                    );
                }
            }

            out.println("<h1>Estadísticas del torneo</h1>");
            out.println("<h2>Victorias por jugador:</h2>");
            for (String jugador : victorias.keySet()) {
                out.println("<p>" + jugador + ": " + victorias.get(jugador) + " victorias</p>");
            }
            int empates = 0;

            for (Partida partida : partidas) {
                if (partida.isEmpate()) empates++;
            }

            out.println("<h3>Partidas jugadas: " + partidas.size() + "</h3>");
            out.println("<h3>Empates: " + empates + "</h3>");
        }

        out.println("</body>");
        out.println("</html>");

        out.close();
    }
}
