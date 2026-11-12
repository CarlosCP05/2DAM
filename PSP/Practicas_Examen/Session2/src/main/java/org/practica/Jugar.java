package org.practica;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Random;

@WebServlet("/Jugar")
public class Jugar extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Random random = new Random();
        Utils ut = new Utils();

        String nombre = req.getParameter("nombre");

        Integer longitud = (Integer) Integer.parseInt(req.getSession().getAttribute("longitud").toString());
        HashMap<String, HashMap<Integer, Integer>> jugadores = (HashMap<String, HashMap<Integer, Integer>>) req.getSession().getAttribute("jugadores");
        Integer turno = (Integer) req.getSession().getAttribute("turno");
        Boolean carreraActiva = (Boolean) req.getSession().getAttribute("carreraActiva");
        String ganador = (String) req.getSession().getAttribute("ganador");

        HashMap<Integer, Integer> infoTurno = new HashMap<>();
        int tirada = random.nextInt(6) +1;

        if (!carreraActiva) {
            if (nombre == null){
                resp.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "La carrera no ha empezado"
                );
                return;
            }
        }

        if (jugadores == null) jugadores = new HashMap<>();

        if (ganador == null) ganador = "";

        if(!jugadores.containsKey(nombre)){
            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "No hay jugadores con ese nombre"
            );
            return;
        }

        if (turno == null) turno = 1;

        if (turno > jugadores.get(nombre).size()){
            if (!jugadores.get(nombre).isEmpty()) infoTurno = jugadores.get(nombre);

            infoTurno.put(turno, tirada);
        }
        else {
            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Este jugador ya ha tirado este turno, espera que todos hagan sus tiradas"
            );
            return;
        }

        jugadores.put(nombre, infoTurno);

        if (ut.todosTurnoActual(jugadores, turno)) turno++;

        if ((longitud - ut.reccoridoJugador(jugadores, nombre)) <= 0 && ganador.isEmpty()){
            req.getSession().setAttribute("ganador", nombre);
            req.getSession().setAttribute("ganador", nombre);
        } else req.getSession().setAttribute("ganador", "");

        req.getSession().setAttribute("turno", turno);
        req.getSession().setAttribute("jugadores", jugadores);

        resp.setContentType("text/html;charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Configuración</title>");
        out.println("</head>");
        out.println("<body>");
        if (!ganador.isEmpty()){
            out.println("<h1>Ha ganado" + ganador + "</h1>");
        }
        out.println("<h1>" + nombre + " ha recorrido " + tirada + " esta a " + (longitud - ut.reccoridoJugador(jugadores, nombre)) + " casillas de acabar</h1>");
        out.println("<h3>Turno actual: " + turno + "</h3>");
        out.println("<h3>Clasificación</h3>");
        for (String jugador: jugadores.keySet()){
            out.println("<p> - " + jugador + " esta a " + (longitud - ut.reccoridoJugador(jugadores, jugador)) + " casillas de acabar en el turno: " + jugadores.get(jugador).size() + "</p>");
        }
        out.println("</body>");
        out.println("</html>");

        out.close();
    }
}
