
package org.sos.infra;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.sos.database.dataBase;
import org.sos.config.StaticVars;
import org.sos.util.Util;
import org.json.JSONObject;

@WebServlet(name = "UrgenceDepartmentsServlet", urlPatterns = { "/api/infra/departments" })
public class UrgenceDepartmentsServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        HttpSession httpSession = request.getSession();
        Object id = httpSession.getAttribute("id_user");

        Object GMT = httpSession.getAttribute("GMT_PLUS");
        String user = request.getParameter("user");
        int GMT_PLUS = 0;
        String idUser = null;

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

        // if (id == null && user == null) {
        // response.sendError(403);
        // return;
        // } else if (id != null) {
        // idUser = id.toString();
        // GMT_PLUS = (int) httpSession.getAttribute("GMT_PLUS");
        // } else {
        // idUser = user;
        // GMT_PLUS = Integer.parseInt(request.getParameter("GMT_PLUS"));
        // }
        idUser = "pgh";
        PrintWriter out = response.getWriter();

        String data = request.getParameter("data");

        try {
            if (StaticVars.base == null) {
                StaticVars.base = new dataBase();
                StaticVars.base.connect(true);
            } else {
                if (!StaticVars.base.isStillConnected()) {
                    StaticVars.base.connect(true);
                }
            }
            JSONObject dataObj = new JSONObject(data);
            int type = dataObj.getInt("type");
            switch (type) {
                case 0:
                    getDepartments(idUser, GMT_PLUS, out);
                    break;
                case 1:
                    addDepartment(idUser, dataObj, GMT_PLUS, out);
                    break;
                case 2:
                    updateDepartment(idUser, dataObj, GMT_PLUS, out);
                    break;
                case 3:
                    deleteDepartment(idUser, dataObj, GMT_PLUS, out);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getDepartments(String idUser, int GMT_PLUS, PrintWriter out) {
        String req = "select * from deparments order by name";
        out.println("[");
        try {

            Statement stmt = StaticVars.base.createStatement();
            ResultSet rs = stmt.executeQuery(req);

            rs.last();
            int count = rs.getRow();
            rs.first();
            for (int i = 0; i < count; i++) {
                out.println("{");
                out.println(Util.getTableLigne(rs, GMT_PLUS));
                out.println("}");
                if (i < count - 1) {
                    out.println(",");
                }
                rs.next();
            }
            out.println("]");
        } catch (Exception ex) {
            out.println("[]");
            System.out.println(req);
            ex.printStackTrace();
        }
    }

    void addDepartment(String idUser, JSONObject data, int GMT_PLUS, PrintWriter out) {
        String req = "insert into  deparments (name, lat, lon, role) values ('" + data.getString("name") + "',"
                + data.getString("lat") + "," + data.getString("lon") + ",'" + data.getString("role") + "')";
        System.out.println(req);
        int id = StaticVars.base.insertQueryGetId(req);
        if (id != -1) {
            getDepartments(idUser, GMT_PLUS, out);
        } else {
            out.println("{\"rep\" : \"erreur\"}");
        }
    }

    void updateDepartment(String idUser, JSONObject data, int GMT_PLUS, PrintWriter out) {
        String req = "update deparments  set name = '" + data.getString("name") + "', lat = " + data.getLong("lat")
                + ", lon = " + data.getLong("lon") + ", role = '" + data.getString("role") + "' where id = "
                + data.getInt("id");
        System.out.println(req);
        boolean b = StaticVars.base.insert(req);
        if (b) {
            getDepartments(idUser, GMT_PLUS, out);
        } else {
            out.println("{\"rep\" : \"erreur\"}");
        }
    }

    
    void deleteDepartment(String idUser, JSONObject data, int GMT_PLUS, PrintWriter out) {
        String req = "delete from deparments  where id = " + data.getInt("id") + " limit 1";
        System.out.println(req);
        boolean b = StaticVars.base.insert(req);
        if (b) {
            getDepartments(idUser, GMT_PLUS, out);
        } else {
            out.println("{\"rep\" : \"erreur\"}");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
    // + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
