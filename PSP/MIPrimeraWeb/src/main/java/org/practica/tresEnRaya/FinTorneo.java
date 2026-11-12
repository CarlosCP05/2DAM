
package org.practica.tresEnRaya;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@WebServlet("/FinTorneo")
public class FinTorneo extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String estado = req.getParameter("estado");

        Boolean finTorneo = (Boolean) req.getSession().getAttribute("finTorneo");

        if (finTorneo == null) finTorneo = false;

        if (estado != null && estado.equals("Fin") && !finTorneo) {
            finTorneo = true;
            req.getSession().setAttribute("finTorneo", true);
            req.getSession().setAttribute("puestoPodio", 3);
        }

        resp.setContentType("text/html;charset=utf-8");
        resp.setCharacterEncoding("utf-8");

        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Fin del torneo</title>");
        out.println("</head>");
        out.println("<body>");

        if (!finTorneo) {
            out.println("<h1>El torneo no ha acabado</h1>");
            out.println("<p>Para acabarlo introduce Fin en el parámetro estado.</p>");
        }
        else {
            ArrayList<Partida> partidas = (ArrayList<Partida>) req.getSession().getAttribute("partidas");

            if (partidas == null) partidas = new ArrayList<>();

            LinkedHashMap<String, Integer> victorias = new LinkedHashMap<>();

            for (Partida partida : partidas) {
                if (!victorias.containsKey(partida.getJugador1())) victorias.put(partida.getJugador1(), 0);
                if (!victorias.containsKey(partida.getJugador2())) victorias.put(partida.getJugador2(), 0);

                if (!partida.isEmpate()) {
                    victorias.put(partida.getGanador(), victorias.get(partida.getGanador()) + 1);
                }
            }

            ArrayList<String> jugadores = new ArrayList<>(victorias.keySet());
            ArrayList<Integer> numVictorias = new ArrayList<>(victorias.values());

            for (int i = 0; i < jugadores.size(); i++) {
                for (int j = i + 1; j < jugadores.size(); j++) {
                    if (numVictorias.get(j) > numVictorias.get(i)) {
                        String auxJugador = jugadores.get(i);
                        jugadores.set(i, jugadores.get(j));
                        jugadores.set(j, auxJugador);

                        int auxVictorias = numVictorias.get(i);
                        numVictorias.set(i, numVictorias.get(j));
                        numVictorias.set(j, auxVictorias);
                    }
                }
            }

            Integer puestoPodio = (Integer) req.getSession().getAttribute("puestoPodio");

            if (puestoPodio == null) puestoPodio = 3;

            if (puestoPodio > 0) {
                out.println("<h1>Podio del torneo</h1>");

                if (puestoPodio <= jugadores.size()) {
                    int posicion = puestoPodio - 1;

                    if (puestoPodio == 3) out.println("<h2>Tercer puesto</h2>");
                    else if (puestoPodio == 2) out.println("<h2>Segundo puesto</h2>");
                    else out.println("<h2>Campeón</h2>");

                    out.println("<h3>" + jugadores.get(posicion) + "</h3>");
                    out.println("<p>Victorias: " + numVictorias.get(posicion) + "</p>");
                }
                else {
                    out.println("<h2>No hay suficientes jugadores para este puesto.</h2>");
                }

                puestoPodio--;
                req.getSession().setAttribute("puestoPodio", puestoPodio);

                if (puestoPodio > 0) {
                    out.println("<a href=\"" + req.getContextPath() + "/FinTorneo\">Siguiente puesto</a>");
                }
                else {
                    out.println("<h3>El torneo ha finalizado.</h3>");
                }
            }
            else {
                out.println("<h1>El podio ya se ha mostrado.</h1>");
            }
        }
        out.println("</body>");
        out.println("</html>");

        out.close();
    }
}
