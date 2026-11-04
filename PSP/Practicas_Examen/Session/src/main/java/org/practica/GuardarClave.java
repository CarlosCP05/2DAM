package org.practica;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/Guardar")
public class GuardarClave extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String clavePed = req.getParameter("clave");
        String clave = (String) req.getSession().getAttribute("clave");

        if (clavePed == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"Tienes que introducir el campo \"clave\"");
            return;
        }


        if (clavePed.length() != 3) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"Tienes que introducir 3 numeros");
            return;
        }

        try{
            Integer.parseInt(clavePed);
        }
        catch (Exception e){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"Tienes que introducir solo numeros");
            return;
        }

        clave = clavePed;

        req.getSession().setAttribute("clave", clave);

        resp.setContentType("text/html;charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Guardar</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Clave guardada</h1>");
        out.println("</body>");
        out.println("</html>");
        out.close();

    }
}
