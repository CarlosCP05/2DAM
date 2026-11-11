
package org.practica.tresEnRaya;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;

@WebServlet("/BorrarRegistro")
public class BorrarRegistro extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String nombre = req.getParameter("nombre");

        ArrayList<String> jugadores = (ArrayList<String>) req.getSession().getAttribute("jugadores");
        ArrayList<Partida> partidas = (ArrayList<Partida>) req.getSession().getAttribute("partidas");

        if (jugadores == null) jugadores = new ArrayList<>();
        if (partidas == null) partidas = new ArrayList<>();

        boolean encontrado = false;
        String error = "";

        if (nombre == null || nombre.isEmpty()) error = "Hay que introducir el nombre del jugador.";
        else if (!jugadores.contains(nombre)) error = "El jugador no está inscrito.";
        else {
            jugadores.remove(nombre);
            encontrado = true;

            for (int i = partidas.size() - 1; i >= 0; i--) {
                Partida partida = partidas.get(i);

                if (Objects.equals(nombre, partida.getJugador1()) ||
                        Objects.equals(nombre, partida.getJugador2())) {
                    partidas.remove(i);
                }
            }

            req.getSession().setAttribute("jugadores", jugadores);
            req.getSession().setAttribute("partidas", partidas);
        }

        resp.setContentType("text/html;charset=utf-8");
        resp.setCharacterEncoding("utf-8");

        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Borrar registro</title>");
        out.println("</head>");
        out.println("<body>");
        if (encontrado) {
            out.println("<h3>Jugador: " + nombre + " eliminado del torneo.</h3>");
            out.println("<h3>Sus partidas también han sido eliminadas.</h3>");
        } else {
            out.println("<h3>" + error + "</h3>");
        }
        out.println("<h1>Jugadores inscritos:</h1>");

        for (String jugador : jugadores) {
            out.println("<p> · " + jugador + "</p>");
        }
        out.println("</body>");
        out.println("</html>");

        out.close();
    }
}
