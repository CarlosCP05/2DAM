package org.practica;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/Empezar")
public class Empezar extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String clave = req.getParameter("clave");
        String estado = req.getParameter("estado");

        String claveDef = "lasNuecesMatan";
        Boolean carreraActiva = (Boolean) req.getSession().getAttribute("carreraActiva");

        if (clave == null || estado == null){
            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Hay que introducir los parámetros de \"clave\" y \"estado\""
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

        if (carreraActiva == null) carreraActiva = false;

        if (estado.equals("Empieza")) carreraActiva = true;

        req.getSession().setAttribute("carreraActiva", carreraActiva);

        resp.setContentType("text/html;charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Empezar</title>");
        out.println("</head>");
        out.println("<body>");
        if (carreraActiva){
            out.println("<h1>¡¡¡La carrera comienza!!!</h1>");
        }
        else {
            out.println("<h1>La carrera aun no ha empezado, para empezar pon en el estado \"Empieza\"</h1>");
        }
        out.println("</body>");
        out.println("</html>");

        out.close();
    }
}
