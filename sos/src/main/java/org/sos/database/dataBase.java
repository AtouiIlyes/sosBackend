package org.sos.database;

import java.sql.*;
import java.util.Properties;
import org.sos.config.StaticVars;

/**
 *
 * @author Administrateur
 */
public class dataBase {

    static int NbrConnections = 0;
    static int NbrFailConnections = 0;

    String DataBaseUserName;
    String DataBasePassword;
    String DataBaseDriver;
    String DataBaseUrl;
    public Connection conn = null;

    /**
     * Creates a new instance of dataBase
     */
    public dataBase() {
        // new checkConfig();
    }

    public boolean isStillConnected() {
        Statement stmt = null;
        ResultSet resultset = null;
        try {
            stmt = createStatement();
            resultset = stmt.executeQuery("SHOW DATABASES;");

            if (stmt.execute("SHOW DATABASES;")) {
                resultset = stmt.getResultSet();
            }
            resultset.last();
            int count = resultset.getRow();

            if (count > 0) {
                return true;
            }
        } catch (SQLException ex) {
            // handle any errors
            ex.printStackTrace();
        } finally {
            // release resources
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }

            if (resultset != null) {
                try {
                    resultset.close();
                } catch (SQLException sqlEx) {
                }
                resultset = null;
                return true;
            }
        }
        return false;
    }

    public boolean connectWeb(boolean affiche) {
        DataBaseUserName = StaticVars.DataBaseWebUserName;
        DataBasePassword = StaticVars.DataBaseWebPassword;
        DataBaseDriver = StaticVars.DataBaseWebDriver;
        DataBaseUrl = StaticVars.DataBaseWebUrl;
        return connect(affiche);
    }

    public boolean connect(boolean affiche) {
        if (DataBaseUserName == null) {
            return connectWeb(affiche);
        }
        NbrConnections++;
        try {
            // if(NbrFailConnections>10) return false;
            Properties props = new Properties();
            props.setProperty("characterEncoding", "UTF-8");
            props.put("charset", "UTF8");
            props.put("lc_ctype", "UTF8");
            props.setProperty("user", DataBaseUserName);
            props.setProperty("password", DataBasePassword);

            if (!(NbrFailConnections > 1)) {
                Class.forName(DataBaseDriver).newInstance(); // Load Driver
            }

            conn = DriverManager.getConnection(DataBaseUrl, // URL
                    props);

            // if (affiche)
            System.out.println("Database connections established " + NbrConnections);
            NbrFailConnections = 0;

        } catch (Exception e) {

            System.err.println("Can not connect to database server >> " + DataBaseUrl);
            NbrFailConnections++;
            return false;
        }
        return true;
    }

    public Statement createStatement() {

        try {
            if (conn != null) {
                if (conn.isClosed()) {
                    if (!connect(true)) {
                        // System.out.println ("createStatement:[1]");
                        return null;
                    }
                }
            } else {
                if (!connect(true)) {
                    // System.out.println ("createStatement:[2]");
                    return null;
                }
            }
            // System.out.println ("createStatement:[OK]");
            return conn.createStatement();
        } catch (Exception e) {
            return null;
        }
    }

    public Statement createStatementUpdate() {
        try {
            if (conn != null) {
                if (conn.isClosed()) {
                    if (!connect(true)) {
                        // System.out.println ("createStatement:[1]");
                        return null;
                    }
                }
            } else {
                if (!connect(true)) {
                    // System.out.println ("createStatement:[2]");
                    return null;
                }
            }
            // System.out.println ("createStatement:[OK]");
            return conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean insert(String gprmc) {
        return insert(gprmc, false);
    }

    public boolean insert(String gprmc, boolean afficherErreur) {
        Statement stmt = null;

        try {
            if (conn != null) {
                if (conn.isClosed()) {
                    if (!connect(true)) {
                        return false;
                    }
                }
            } else {
                if (!connect(true)) {
                    return false;
                }
            }
            stmt = conn.createStatement();

            stmt.executeUpdate(gprmc);
        } catch (Exception e) {
            // System.err.println(gprmc);
            if (afficherErreur) {
                e.printStackTrace();
            }
            return false;
        } finally {

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                    /* ignore */ }
                stmt = null;
            }
        }
        return true;
    }

    public int insertQueryGetId(String query) {
        int risultato = -1;
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                risultato = rs.getInt(1);
            }
            rs.close();

            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();

            risultato = -1;
        }
        return risultato;
    }
}
