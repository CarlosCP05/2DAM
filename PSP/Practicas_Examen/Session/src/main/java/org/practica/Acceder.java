package org.practica;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/Acceso")
public class Acceder extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String digito = req.getParameter("digito");

        String clave = (String) req.getSession().getAttribute("clave");
        String claveAct = (String) req.getSession().getAttribute("claveAct");

        if (clave == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"Tienes que introducir la clave con \"digito\"");
            return;
        }

        if (digito == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"Tienes que introducir el siguiente digito");
            return;
        }

        if(claveAct == null) claveAct = digito;
        else claveAct += digito;

        for(int i = 0; i < claveAct.length(); i++){
            if (claveAct.charAt(i) != clave.charAt(i)){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"Has introducido la clave mal, vuelve a empezar");
                claveAct = null;
                req.getSession().setAttribute("claveAct", claveAct);
                return;
            }
        }

        if (claveAct.equals(clave)){
            resp.setContentType("text/html;charset=utf-8");
            resp.setCharacterEncoding("utf-8");
            PrintWriter out = resp.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Acceder</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Clave comprobada con exito, puede guardar secretos</h1>");
            out.println("</body>");
            out.println("</html>");
            out.close();
        }

        else {
            resp.setContentType("text/html;charset=utf-8");
            resp.setCharacterEncoding("utf-8");
            PrintWriter out = resp.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Guardar</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Siga introduciendo la clave</h1>");
            out.println("</body>");
            out.println("</html>");
            out.close();
        }

        req.getSession().setAttribute("claveAct", claveAct);
    }
}
