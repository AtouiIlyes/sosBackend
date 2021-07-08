/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sos.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.sos.config.StaticVars;
import org.sos.database.dataBase;

@WebServlet(name = "Login", urlPatterns = { "/api/login" })
public class Login extends HttpServlet {

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
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        HttpSession httpSession = request.getSession();

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        String email = request.getParameter("email");
        int GMT_PLUS = 1;
        String password = request.getParameter("password");
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = request.getRemoteAddr();
        String nomWebService = "/api/login";
        boolean isSuperUser = false;

        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("{");
            login(email, password, GMT_PLUS, isSuperUser, httpSession, out, userAgent, ipAddress, nomWebService);
            out.println("}");
        } finally {
            out.close();
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

    public void login(String login, String pass, int GMT_PLUS, boolean isSuperUser, HttpSession httpSession,
            PrintWriter out, String userAgent, String ipAddress, String nomWebService) {
        String req = "select * from users where email like '" + login + "'";
        if (StaticVars.base == null) {
            StaticVars.base = new dataBase();
            StaticVars.base.connect(true);
        } else {
            if (!StaticVars.base.isStillConnected()) {
                StaticVars.base.connect(true);
            }
        }
        Statement st = StaticVars.base.createStatement();
        try {
            ResultSet rs = st.executeQuery(req);
            rs.last();
            int count = rs.getRow();
            if (count == 1) {
                rs.first();
                if (rs.getString("pwd").equals(pass) || isSuperUser) {
                    out.println("\"Result\":1,");
                    out.println(getTableLigne(rs));
                } else {
                    out.println("\"Result\":0");

                }
            } else {
                out.println("\"Result\":0");
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    String getTableLigne(ResultSet rs) throws Exception {
        ResultSetMetaData metaData = rs.getMetaData();
        String ret = "";
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            ret += "\"" + metaData.getColumnName(i) + "\" :";

            ret += "\"" + getField(rs, i) + "\"";

            if (i != metaData.getColumnCount()) {
                ret += ",\n";
            }
        }
        return ret;
    }

    String getTableLigneValue(ResultSet rs) throws Exception {
        ResultSetMetaData metaData = rs.getMetaData();
        String ret = "";
        for (int i = 1; i <= metaData.getColumnCount(); i++) {

            ret += "\"" + getField(rs, i) + "\"";

            if (i != metaData.getColumnCount()) {
                ret += ",\n";
            }
        }
        return ret;
    }

    String getField(ResultSet rs, int i) {
        try {
            return rs.getString(i).replace("\"", "\\\"");
        } catch (Exception ee) {
        }
        return "-";
    }
}
