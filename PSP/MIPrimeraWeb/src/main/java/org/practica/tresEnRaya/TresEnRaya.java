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

@WebServlet("/TresEnRaya")
public class TresEnRaya extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nombre = req.getParameter("nombre");
        String coordenada = req.getParameter("coordenada");
        boolean pasarTurno = false;
        boolean reiniciar = false;


        String marca = (String) req.getSession().getAttribute("marca");
        String[] tablero = (String[])req.getSession().getAttribute("Tablero");
        String jugador1 = (String) req.getSession().getAttribute("jugador1");
        String jugador2 = (String) req.getSession().getAttribute("jugador2");

        if (coordenada == null) {
            resp.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Hay que introducir la coordenada (del 1 al 9, \"No\" para pasar turno o \"Reiniciar\" para volver a empezar)"
            );
            return;
        }

        if (coordenada.equals("No")) pasarTurno = true;
        if (coordenada.equals("Reiniciar")) reiniciar = true;


        if (tablero == null || reiniciar) {
            tablero = new String[]{"-", "-", "-", "-", "-", "-", "-", "-", "-"};
        }

        if (marca == null || reiniciar) marca = "X";

        if (reiniciar) {
            jugador1 = "Jugador 1";
            jugador2 = "Jugador 2";
        } else {
            if (jugador1 == null) jugador1 = "Jugador 1";
            if (jugador2 == null) jugador2 = "Jugador 2";
        }

        if (nombre != null) {
            if (marca.equals("X")) jugador1 = nombre;
            if (marca.equals("O")) jugador2 = nombre;
        }

        if (!pasarTurno && !reiniciar) {
            try {
                tablero[Integer.parseInt(coordenada) - 1] = marca;
                marca = marca.equals("X") ? "O" : "X";
            } catch (NumberFormatException e) {
                resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "La coordenada tiene que ser entre 1 y 9, \"No\" para parar el turno o \"Reiniciar\" para reiniciar la partida"
                );
                return;
            }
        }

        String txtTurno = "Turno de " + (marca.equals("X") ? jugador1 : jugador2) + " con marca: " + marca + "<br>Siguiente jugador: " + (marca.equals("O") ? jugador1 : jugador2);
        String fila1 = "<p class=\"casilla\">" + tablero[0] + "</p> <p class=\"casilla\">" + tablero[1] + "</p> <p class=\"casilla\">" + tablero[2] + "</p>";
        String fila2 = "<p class=\"casilla\">" + tablero[3] + "</p> <p class=\"casilla\">" + tablero[4] + "</p> <p class=\"casilla\">" + tablero[5] + "</p>";
        String fila3 = "<p class=\"casilla\">" + tablero[6] + "</p> <p class=\"casilla\">" + tablero[7] + "</p> <p class=\"casilla\">" + tablero[8] + "</p>";

        req.getSession().setAttribute("marca", marca);
        req.getSession().setAttribute("jugador1", jugador1);
        req.getSession().setAttribute("jugador2", jugador2);
        req.getSession().setAttribute("Tablero", tablero);

        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        ctx.setVariable("txtTurno", txtTurno);
        ctx.setVariable("fila1", fila1);
        ctx.setVariable("fila2", fila2);
        ctx.setVariable("fila3", fila3);

        resp.setContentType("text/html;charset=UTF-8");
        ((TemplateEngine) getServletContext()
                .getAttribute("templateEngine"))
                .process("tresEnRaya", ctx, resp.getWriter());
    }
}
