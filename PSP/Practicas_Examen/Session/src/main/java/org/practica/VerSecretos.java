package org.practica;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/Secretos")
public class VerSecretos extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> secretos = (Map<String, String>) req.getSession().getAttribute("secretos");

        String clave = (String) req.getSession().getAttribute("clave");
        String claveAct = (String) req.getSession().getAttribute("claveAct");

        if (!clave.equals(claveAct)){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"Clave no validada");
            return;
        }

        resp.setContentType("text/html;charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Añadir secreto</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Secretos guardados</h1>");
        secretos.forEach( (k, v) -> {
            out.println("<p>Secreto con nombre: " + k + " y valor: " + v + "</p>");
        });
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
