
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

@WebServlet(name = "CategoryServlet", urlPatterns = { "/api/infra/category" })
public class CategoryServlet extends HttpServlet {

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
        Object id_sub_user = httpSession.getAttribute("id_sub_user");
        Object sub_user_espaces = httpSession.getAttribute("sub_users_espaces");

        Object GMT = httpSession.getAttribute("GMT_PLUS");
        String user = request.getParameter("user");
        int GMT_PLUS = 0;
        String idUser = null;
        String idSubUser = null;
        String subUserEspaces = null;

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

        if (id_sub_user != null) {
            idSubUser = id_sub_user.toString();
            if (sub_user_espaces != null) {
                subUserEspaces = sub_user_espaces.toString();
            }
        }

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
                    getCategories(idUser, idSubUser, GMT_PLUS, out);
                    break;
                case 1:
                    addCategory(idUser, idSubUser, dataObj, GMT_PLUS, out);
                    break;
                case 2:
                    updateCategory(idUser, idSubUser, dataObj, GMT_PLUS, out);
                    break;
                case 3:
                    deleteCategory(idUser, idSubUser, dataObj, GMT_PLUS, out);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getCategories(String idUser, String idSubUser, int GMT_PLUS, PrintWriter out) {
        String req = "select * from bw_" + idUser + ".frigo_categories order by nom";
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

    void addCategory(String idUser, String idSubUser, JSONObject data, int GMT_PLUS, PrintWriter out) {
        String req = "insert into  bw_" + idUser + ".frigo_categories (nom) values ('" + data.getString("nom") + "')";
        System.out.println(req);
        int id = StaticVars.base.insertQueryGetId(req);
        if (id != -1) {
            getCategories(idUser, idSubUser, GMT_PLUS, out);
        } else {
            out.println("{\"rep\" : \"erreur\"}");
        }
    }

    void updateCategory(String idUser, String idSubUser, JSONObject data, int GMT_PLUS, PrintWriter out) {
        String req = "update bw_" + idUser + ".frigo_categories  set nom = '" + data.getString("nom") + "'"
                + " where id = " + data.getInt("id");
        System.out.println(req);
        boolean b = StaticVars.base.insert(req);
        if (b) {
            getCategories(idUser, idSubUser, GMT_PLUS, out);
        } else {
            out.println("{\"rep\" : \"erreur\"}");
        }
    }

    void deleteCategory(String idUser, String idSubUser, JSONObject data, int GMT_PLUS, PrintWriter out) {
        String req = "delete from bw_" + idUser + ".frigo_categories  where id = " + data.getInt("id") + " limit 1";
        System.out.println(req);
        boolean b = StaticVars.base.insert(req);
        if (b) {
            updateFrigoToDefaultCategory(data.getInt("id"));
            getCategories(idUser, idSubUser, GMT_PLUS, out);
        } else {
            out.println("{\"rep\" : \"erreur\"}");
        }
    }

    void updateFrigoToDefaultCategory(int id) {
        String req = "update bw_pgh.frigo set id_category = 1  where id_category = " + id;
        System.out.println(req);
        boolean b = StaticVars.base.insert(req);

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
