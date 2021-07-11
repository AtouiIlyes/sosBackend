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

import org.json.JSONObject;
import org.sos.config.StaticVars;
import org.sos.database.dataBase;
import org.sos.util.Util;

@WebServlet(name = "UrgenceServlet", urlPatterns = { "/api/urgence" })
public class UrgenceServlet extends HttpServlet {

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
					getUrgences(idUser, GMT_PLUS, out);
					break;
				case 1:
					addUrgence(idUser, dataObj, GMT_PLUS, out);
					break;
				case 2:
					urgenceDone(idUser, dataObj, GMT_PLUS, out);
					break;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void getUrgences(String idUser, int GMT_PLUS, PrintWriter out) {
		String req = "select * from urgence u,users ur,health_card h where u.idUser = ur.id and h.id = ur.id and response = 0  order by date";
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

	void addUrgence(String idUser, JSONObject data, int GMT_PLUS, PrintWriter out) {
		String req = "insert into  urgence (idUser, type, lat, lon) values (" + idUser + ",'" + data.getString("typeUrgence")
				+ "'," + data.getLong("lat") + "," + data.getLong("lon") + ")";
		System.out.println(req);
		int id = StaticVars.base.insertQueryGetId(req);
		if (id != -1) {
			out.println("{\"rep\" : \"ok\"}");
		} else {
			out.println("{\"rep\" : \"erreur\"}");
		}
	}

	void urgenceDone(String idUser, JSONObject data, int GMT_PLUS, PrintWriter out) {
		String req = "update urgence  set response = 1 where id = " + data.getString("id");
		System.out.println(req);
		boolean b = StaticVars.base.insert(req);
		if (b) {
			getUrgences(idUser, GMT_PLUS, out);
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
