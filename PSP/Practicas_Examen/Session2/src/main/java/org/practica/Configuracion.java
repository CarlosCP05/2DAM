package org.practica;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/Configuracion")
public class Configuracion extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String clave = req.getParameter("clave");
        String longitud = req.getParameter("longitud");

        String claveDef = "lasNuecesMatan";

        if (clave == null || longitud == null){
            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Hay que introducir los parámetros de \"clave\" y \"longitud\""
            );
            return;
        }

        if (!claveDef.equals(clave)) {
            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "La clave es erronea"
            );
            return;
        }

        try{
            Integer.parseInt(longitud);
        }
        catch (Exception e){
            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Solo se pueden introducir numeros para la longitud"
            );
            return;
        }

        req.getSession().setAttribute("longitud", longitud);

        resp.setContentType("text/html;charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Configuración</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Longitud establecida en " + longitud + "</h1>");
        out.println("</body>");
        out.println("</html>");

        out.close();

    }
}
