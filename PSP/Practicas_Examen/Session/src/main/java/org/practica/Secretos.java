package org.practica;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/Secreto")
public class Secretos extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nombre = req.getParameter("nombre");
        String valor = req.getParameter("valor");

        Map<String, String> secretos = (Map<String, String>) req.getSession().getAttribute("secretos");

        String clave = (String) req.getSession().getAttribute("clave");
        String claveAct = (String) req.getSession().getAttribute("claveAct");

        if (!clave.equals(claveAct)){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"Clave no validada");
            return;
        }

        if (nombre == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"Tienes que introducir el nombre con \"nombre\"");
            return;
        }

        if (valor == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"Tienes que introducir un valor con \"valor\"");
            return;
        }

        secretos.put(nombre, valor);
        req.getSession().setAttribute("secretos", secretos);

        resp.setContentType("text/html;charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Añadir secreto</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Secreto guardado</h1>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
