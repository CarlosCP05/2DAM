package org.practica;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/Estadisticas")
public class Estadisticas extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utils ut = new Utils();

        Boolean carreraActiva = (Boolean) req.getSession().getAttribute("carreraActiva");
        HashMap<String, HashMap<Integer, Integer>> jugadores = (HashMap<String, HashMap<Integer, Integer>>) req.getSession().getAttribute("jugadores");
        int longitud = (int) req.getSession().getAttribute("longitud");
        String ganador = (String) req.getSession().getAttribute("ganador");

        Integer turno = (Integer) req.getSession().getAttribute("turno");

        if (!carreraActiva) {
            resp.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "La carrera no ha empezado"
            );
            return;
        }

        resp.setContentType("text/html;charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Estadisticas</title>");
        out.println("</head>");
        out.println("<body>");
        if (!ganador.isEmpty()){
            out.println("<h1>Ha ganado" + ganador + "</h1>");
        }
        out.println("<h1>Clasificación</h1>");
        out.println("<h2>Turno actual de la carrera: " + turno + "</h3>");
        out.println("<br>");
        out.println("<h2>Jugadores</h3>");
        for (String jugador: jugadores.keySet()){
            out.println("<h3> - " + jugador + " esta a " + (longitud - ut.reccoridoJugador(jugadores, jugador)) + " casillas de acabar en el turno: " + jugadores.get(jugador).size() + "</h3>");
            for(int i = 1; i <= jugadores.get(jugador).size(); i++){
                out.println("<h4>   - El turno" + i + " ha movido " + jugadores.get(jugador).get(i) + " casillas</h4>");
            }
            out.println("<p><br>=================================<br></p>");
        }
        out.println("</body>");
        out.println("</html>");

        out.close();
    }
}
