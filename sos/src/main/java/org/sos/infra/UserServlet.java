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

import java.sql.ResultSetMetaData;

import org.sos.database.dataBase;
import org.sos.config.StaticVars;
import org.sos.util.Util;
import org.json.JSONObject;

@WebServlet(name = "UserServlet", urlPatterns = { "/api/infra/users" })
public class UserServlet extends HttpServlet {

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
					getUsers(idUser, GMT_PLUS, out);
					break;
				case 1:
					addUser(idUser, dataObj, GMT_PLUS, out);
					break;
				case 2:
					updateUser(idUser, dataObj, GMT_PLUS, out);
					break;
				case 3:
					getHealthCard(idUser, GMT_PLUS, out);
					break;
				case 4:
					updateHealthCard(idUser, dataObj, GMT_PLUS, out);
					break;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void getUsers(String idUser, int GMT_PLUS, PrintWriter out) {
		String req = "select * from users order by firstName,lastName";
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
				out.println(",\"user\":");
				getUserInfo(rs.getString("id"), GMT_PLUS, out);
				out.println(",\"health\":");
				getUserHealthCard(rs.getString("id"), GMT_PLUS, out);
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

	void getUserInfo(String idUser, int GMT_PLUS, PrintWriter out) {
		String req = "select * from users where id = " + idUser;
		try {

			Statement stmt = StaticVars.base.createStatement();
			ResultSet rs = stmt.executeQuery(req);

			rs.last();
			int count = rs.getRow();
			rs.first();
			if (count > 0) {
				out.println("{");
				out.println(Util.getTableLigne(rs, GMT_PLUS));
				out.println("}");
			} else {
				out.println("{}");
			}
		} catch (Exception ex) {
			out.println("{}");
			System.out.println(req);
			ex.printStackTrace();
		}
	}

	void getUserHealthCard(String idUser, int GMT_PLUS, PrintWriter out) {
		String req = "select * from health_card where id = " + idUser;
		try {

			Statement stmt = StaticVars.base.createStatement();
			ResultSet rs = stmt.executeQuery(req);

			rs.last();
			int count = rs.getRow();
			rs.first();
			if (count > 0) {
				out.println("{");
				out.println(Util.getTableLigne(rs, GMT_PLUS));
				out.println("}");
			} else {
				out.println("{");
				out.println("\"id\" :" + idUser + ",");
				out.println("\"anaphylaxis\" :0,");
				out.println("\"epipen\" :0,");
				out.println("\"diabetes\" :0,");
				out.println("\"organ_donor\" :0,");
				out.println("\"family_doctor\" :0,");
				out.println("\"blood_group\" :\"-\",");
				out.println("\"doctor\":\"-\"");
				out.println("}");
			}
		} catch (Exception ex) {
			out.println("{}");
			System.out.println(req);
			ex.printStackTrace();
		}
	}

	void getUser(String idUser, int GMT_PLUS, PrintWriter out) {
		String req = "select * from users where id = " + idUser + " order by firstName,lastName";
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

	void getHealthCard(String idUser, int GMT_PLUS, PrintWriter out) {
		String req = "select * from health_card where id = " + idUser;
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

	void addUser(String idUser, JSONObject data, int GMT_PLUS, PrintWriter out) {
		String req = "insert into  users (id, email, firstName, lastName, pwd) values (" + data.getString("id") + ",'"
				+ data.getString("email") + "','" + data.getString("firstName") + "','" + data.getString("lastName")
				+ "','" + data.getString("pwd") + "')";
		System.out.println(req);
		int id = StaticVars.base.insertQueryGetId(req);
		if (id != -1) {
			// getDepartments(idUser, GMT_PLUS, out);
		} else {
			out.println("{\"rep\" : \"erreur\"}");
		}
	}

	void updateUser(String idUser, JSONObject data, int GMT_PLUS, PrintWriter out) {
		String req = "update users  set firstName = '" + data.getString("firstName") + "', lastName = '"
				+ data.getString("lastName") + "', address = '" + data.getString("address") + "', city = '"
				+ data.getString("city") + "', phone = '" + data.getString("phone") + "', birthDate = '"
				+ data.getString("birthDate") + "' where id = " + data.getInt("id");
		System.out.println(req);
		boolean b = StaticVars.base.insert(req);
		if (b) {
			String req2 = "select * from users where id = " + data.getInt("id");

			try {

				Statement stmt = StaticVars.base.createStatement();
				ResultSet rs = stmt.executeQuery(req2);
				rs.last();
				int count = rs.getRow();
				if (count == 1) {
					rs.first();
					out.println("{");
					out.println(Util.getTableLigne(rs, GMT_PLUS));
					out.println("}");

					rs.next();
				} else {
					out.println("{\"rep\" : \"erreur\"}");
				}
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		} else {
			out.println("{\"rep\" : \"erreur\"}");
		}
	}

	void updateHealthCard(String idUser, JSONObject data, int GMT_PLUS, PrintWriter out) {
		String req = "update health_card  set anaphylaxis = '" + data.getInt("anaphylaxis") + "', epipen = '"
				+ data.getInt("epipen") + "', diabetes = '" + data.getInt("diabetes") + "', organ_donor = '"
				+ data.getInt("organ_donor") + "', blood_group = '" + data.getString("blood_group")
				+ "', family_doctor = '" + data.getInt("family_doctor") + "', doctor = '" + data.getString("doctor")
				+ "' where id = " + data.getInt("id");
		System.out.println(req);
		boolean b = StaticVars.base.insert(req);
		if (b) {
			String req2 = "select * from health_card where id = " + data.getInt("id");

			try {

				Statement stmt = StaticVars.base.createStatement();
				ResultSet rs = stmt.executeQuery(req2);
				rs.last();
				int count = rs.getRow();
				if (count == 1) {
					rs.first();
					out.println("{");
					out.println(Util.getTableLigne(rs, GMT_PLUS));
					out.println("}");

					rs.next();
				} else {
					out.println("{\"rep\" : \"erreur\"}");
				}
			} catch (Exception ee) {
				ee.printStackTrace();
			}
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
