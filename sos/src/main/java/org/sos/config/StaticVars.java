package org.sos.config;

import org.sos.database.dataBase;

public class StaticVars {

    public static String getTraduction(String str, String fr) {
        return str;
    }

    /**
     * Creates a new instance of staticVars
     */
    public StaticVars() {
    }

    // DATA BASE CONST
    public static String DataBaseWebDriver = "com.mysql.cj.jdbc.Driver";
    // public static String DataBaseWebUrl = "jdbc:mysql://172.16.1.129:3306/bws";
    // // BWS TEST
    // public static String DataBaseWebUrl = "jdbc:mysql://172.16.1.113:3306/bws";
    // // TOPNET
    public static String DataBaseWebUrl = "jdbc:mysql://127.0.0.1:3306/sos"; //
    // LOCAL BWS TEST
    // public static String DataBaseWebUrl = "jdbc:mysql://127.0.0.1:33113/bws"; //
    // LOCAL TOPNET
    public static String DataBaseWebUserName = "root";
    public static String DataBaseWebPassword = "";
    public static dataBase base = null;

    public static String configPath = "/etc/pghBack.conf";

    // FORMULE SERVER_PORT = GTW_SERVER_PORT - INDEX_SERVER_PORT * 1000 + APP_ID *
    // 10
    public static int SolverTHLRtPort = 28166 - 1000 + 1 * 10;

    public static String SolverHost = "127.0.0.1"; // BWS TEST & LOCAL
    // public static String SolverHost = "172.16.1.117"; // TOPNET

    public static String OdooApiHost = "http://127.0.0.1:5000"; // BWS TEST & LOCAL
    // public static String OdooApiHost = "http://172.16.1.125:5001"; // TOPNET

    public static String FACEBOOK_MESSENGER_GRAPH_API_URL = "https://graph.facebook.com/v3.3/";
    public static String APP_SECRET = "10ad530540608da60ad68a8524024b56";
    public static int GMT_PLUS = 1;

    // public static InputFluxTHLRTWorker solverFluxTHLRt;

    // public static final String SOLVER_THL_APP_ID = "1";

    public static final String BWS_SUPER_USER_PASSWORD = "bws0701";

    public static void connectSolverFluxRt() {

        // if (solverFluxTHLRt == null) {
        // solverFluxTHLRt = new InputFluxTHLRTWorker(SolverHost, SolverTHLRtPort);
        // solverFluxTHLRt.start();
        // } else {
        // if (solverFluxTHLRt.isClosed()) {
        // solverFluxTHLRt = new InputFluxTHLRTWorker(SolverHost, SolverTHLRtPort);
        // solverFluxTHLRt.start();
        // }
        // }

    }
}
