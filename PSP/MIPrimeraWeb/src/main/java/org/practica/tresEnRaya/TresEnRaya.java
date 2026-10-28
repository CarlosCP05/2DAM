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

@WebServlet("TresEnRaya")
public class TresEnRaya extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String marca = req.getParameter("marca");
        String coordenada = req.getParameter("coordenada");

        if (marca == null) resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hay que introducir la marca");
        if (coordenada == null) resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hay que introducir la coordenada");
        if (marca == null || coordenada == null) return;

        String[] tablero = (String[])req.getSession().getAttribute("Tablero");
        if (tablero == null){
            tablero = new String[]{"-", "-", "-", "-", "-", "-", "-", "-", "-"};
            req.getSession().setAttribute("Tablero", tablero);
        }

        tablero[Integer.parseInt(coordenada)-1] = marca;

        String fila1 = tablero[0] + " " + tablero[1] + " " + tablero[2];
        String fila2 = tablero[3] + " " + tablero[4] + " " + tablero[5];
        String fila3 = tablero[6] + " " + tablero[7] + " " + tablero[8];

        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        ctx.setVariable("fila1", fila1);
        ctx.setVariable("fila2", fila2);
        ctx.setVariable("fila3", fila3);

        resp.setContentType("text/html;charset=UTF-8");
        ((TemplateEngine)getServletContext().getAttribute("noSe")).process("tresEnRaya", ctx, resp.getWriter());
    }
}
