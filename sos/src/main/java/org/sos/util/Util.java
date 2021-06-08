package org.sos.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    public static String getTableLigne(ResultSet rs, int GMT_PLUS) throws Exception {
        ResultSetMetaData metaData = rs.getMetaData();
        String ret = "";
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            ret += "\"" + metaData.getColumnLabel(i) + "\" :";
            if (rs.getMetaData().getColumnType(i) == Types.INTEGER) {
                ret += rs.getInt(i);
            } else if (rs.getMetaData().getColumnType(i) == Types.DOUBLE) {
                ret += rs.getDouble(i);
            } else if (rs.getMetaData().getColumnType(i) == Types.BIGINT) {
                ret += rs.getDouble(i);
            } else {
                ret += "\"" + getField(rs, i, GMT_PLUS) + "\"";
            }
            if (i != metaData.getColumnCount()) {
                ret += ",\n";
            }
        }
        return ret;
    }

    public static String getField(ResultSet rs, int i, int GMT_PLUS) {
        try {
            if (rs.getMetaData().getColumnType(i) == Types.DATE || rs.getMetaData().getColumnType(i) == Types.TIMESTAMP
                    || rs.getMetaData().getColumnType(i) == Types.TIME) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = dateFormat.parse(rs.getString(i));
                return (dateFormat.format(date.getTime() + GMT_PLUS * 3600 * 1000)).replace("\"", "\\\"");
            }
            return rs.getString(i).replace("\"", "\\\"");

        } catch (Exception ee) {
            System.out.println(ee.getStackTrace());
        }
        return "-";
    }

    
}
