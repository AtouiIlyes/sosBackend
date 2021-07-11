package org.sos.infra;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.sos.config.StaticVars;
import org.sos.database.dataBase;
import org.sos.util.Util;

@WebServlet(name = "FamilyMembers", urlPatterns = { "/api/family_member" })
public class FamilyMembers extends HttpServlet {

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

		if (id == null && user == null) {
			response.sendError(403);
			return;
		} else if (id != null) {
			idUser = id.toString();
			GMT_PLUS = 1;
		} else {
			idUser = user;
		}
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
					getFamilyMembers(idUser, GMT_PLUS, out);
					break;
				case 1:
					addFamilyMember(idUser, dataObj, GMT_PLUS, out);
					break;
				case 2:
					deleteFamilyMember(idUser, dataObj, GMT_PLUS, out);
					break;
				case 3:
					getMessage(idUser, GMT_PLUS, out);
					break;
				case 4:
					updateMessage(idUser, dataObj, GMT_PLUS, out);
					break;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void getFamilyMembers(String idUser, int GMT_PLUS, PrintWriter out) {
		String req = "select * from family_group where idUser = " + idUser + " order by name";
		try {

			Statement stmt = StaticVars.base.createStatement();
			ResultSet rs = stmt.executeQuery(req);

			rs.last();
			int count = rs.getRow();
			rs.first();
			out.println("[");
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

	void getMessage(String idUser, int GMT_PLUS, PrintWriter out) {
		String req = "select * from urgence_message where idUser = " + idUser + "";
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
		} catch (Exception ex) {
			out.println("[]");
			System.out.println(req);
			ex.printStackTrace();
		}
	}

	void addFamilyMember(String idUser, JSONObject data, int GMT_PLUS, PrintWriter out) {
		String req = "insert into  family_group (idUser, phone_number, name) values (" + idUser + ",'"
				+ data.getString("phone_number") + "','" + data.getString("name") + "')";
		System.out.println(req);
		boolean b = StaticVars.base.insert(req);
		if (b) {
			getFamilyMembers(idUser, GMT_PLUS, out);
		} else {
			out.println("{\"rep\" : \"erreur\"}");
		}
	}

	void deleteFamilyMember(String idUser, JSONObject data, int GMT_PLUS, PrintWriter out) {
		String req = "delete from family_group  where idUser = " + idUser + " and phone_number = "
				+ data.getInt("phone_number") + " limit 1";

		System.out.println(req);
		boolean b = StaticVars.base.insert(req);
		if (b) {
			getFamilyMembers(idUser, GMT_PLUS, out);
		} else {
			out.println("{\"rep\" : \"erreur\"}");
		}
	}

	void updateMessage(String idUser, JSONObject data, int GMT_PLUS, PrintWriter out) {
		String req = "insert into  urgence_message (idUser, message) values (" + idUser + ",'"
				+ data.getString("message") + "') on duplicate key update message = '" + data.getString("message")
				+ "'";
		System.out.println(req);
		boolean b = StaticVars.base.insert(req);
		if (b) {
			getMessage(idUser, GMT_PLUS, out);
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
