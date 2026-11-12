package org.practica.tresEnRaya;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;

@WebServlet("/Inscripcion")
public class Inscripcion extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req,  HttpServletResponse resp) throws IOException {
        String nombre = req.getParameter("nombre");
        String eliminarInp = req.getParameter("Eliminar");

        ArrayList<String> jugadores = (ArrayList<String>) req.getSession().getAttribute("jugadores");
        Boolean inscripcionDisponible = (Boolean) req.getSession().getAttribute("inscripcionDisponible") ;

        if (eliminarInp == null) eliminarInp = "";

        if (inscripcionDisponible == null) inscripcionDisponible = true;

        if (jugadores == null) jugadores = new ArrayList<>();

        boolean coincidencia = false;

        if (inscripcionDisponible) {
            if (nombre == null || nombre.isEmpty()) {
                resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Hay que introducir el nombre del jugador."
                );
                return;
            }

            for (String jugador : jugadores) {
                if (Objects.equals(nombre, jugador)) {
                    if (eliminarInp.equals("Si")) {
                        jugadores.remove(jugador);
                        req.getSession().setAttribute("jugadores", jugadores);
                    }
                    coincidencia = true;
                    break;
                }
            }

            if (!eliminarInp.equals("Si") && !coincidencia) jugadores.add(nombre);
        }

        req.getSession().setAttribute("jugadores", jugadores);
        req.getSession().setAttribute("inscripcionDisponible", inscripcionDisponible);

        resp.setContentType("text/html;charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Inscripción</title>");
        out.println("</head>");
        out.println("<body>");
        if (eliminarInp.equals("Si") && inscripcionDisponible) {
            if (coincidencia) out.println("<h3>Jugador: " + nombre + " eliminado</h3>");
            else out.println("<h3>Jugador: " + nombre + " no encontrado</h3>");
            out.println("</br>");
        }
        else if (coincidencia) {
            out.println("<h3>Jugador: " + nombre + " ya estaba agregado</h3>");
            out.println("</br>");
        }
        else if (!inscripcionDisponible) {
            out.println("<h3>Inscripciones no disponibles</h3>");
            out.println("</br>");
        }
        out.println("<h1>Inscritos:</h1>");
        for (String jugador : jugadores) {
            out.println("<p> · " + jugador + "</p>");
        }
        out.println("</br>");
        out.println("</br>");
        if (inscripcionDisponible) {
            out.println("<span>Para eliminar un jugador inscrito añade al campo \"Eliminar\" con valor \"Si\" poniendo el nombre del jugador que quiera eliminar</span>");
        }
        out.println("</body>");
        out.println("</html>");

        out.close();
    }
}
