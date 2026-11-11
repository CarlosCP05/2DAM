
package org.practica.tresEnRaya;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Objects;

@WebServlet("/Preparacion")
public class Preparacion extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Random random = new Random();

        String jugador1 = req.getParameter("jugador1");
        String jugador2 = req.getParameter("jugador2");
        String marca1 = req.getParameter("marca1");
        String marca2 = req.getParameter("marca2");

        ArrayList<String> jugadores = (ArrayList<String>) req.getSession().getAttribute("jugadores");

        if (jugadores == null) jugadores = new ArrayList<>();

        boolean correcto = false;

        Boolean empiezaJugador1 = (Boolean) req.getSession().getAttribute("empiezaJugador1");
        Boolean partidaEnCurso = (Boolean) req.getSession().getAttribute("partidaEnCurso");

        String error = "";

        if (partidaEnCurso == null) partidaEnCurso = false;
        if(!partidaEnCurso) empiezaJugador1 = random.nextBoolean();

        if(!partidaEnCurso) {
            if (jugador1 == null || jugador1.isEmpty() ||
                    jugador2 == null || jugador2.isEmpty() ||
                    marca1 == null || marca1.isEmpty() ||
                    marca2 == null || marca2.isEmpty()) {

                error = "Todos los campos son obligatorios.";
            } else if (Objects.equals(jugador1, jugador2)) {
                error = "No se puede seleccionar el mismo jugador dos veces.";
            } else if (Objects.equals(marca1, marca2)) {
                error = "Los dos jugadores no pueden tener la misma marca.";
            } else if (!jugadores.contains(jugador1) || !jugadores.contains(jugador2)) {
                error = "Uno o ambos jugadores no están inscritos.";
            } else correcto = true;

            if (correcto) {
                req.getSession().setAttribute("jugador1", jugador1);
                req.getSession().setAttribute("jugador2", jugador2);
                req.getSession().setAttribute("marca1", marca1);
                req.getSession().setAttribute("marca2", marca2);
                req.getSession().setAttribute("empiezaJugador1", empiezaJugador1);
                req.getSession().setAttribute("partidaEnCurso", true);
                req.getSession().setAttribute("inscripcionDisponible", false);
            }
        } else correcto = true;

        resp.setContentType("text/html;charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Preparación</title>");
        out.println("</head>");
        out.println("<body>");
        if (!correcto) {
            out.println("<h3>" + error + "</h3>");
        }
        else {
            out.println("<h1>Preparación de la partida</h1>");
            out.println("<p>" + jugador1 + " - " + marca1 + "</p>");
            out.println("<p>" + jugador2 + " - " + marca2 + "</p>");
            out.println("<h2>Empieza la partida:</h2>");
            out.println("<h3>Empieza poniendo: " + (empiezaJugador1 ? jugador1 : jugador2) + "</h3>");
        }
        out.println("</body>");
        out.println("</html>");

        out.close();
    }
}
