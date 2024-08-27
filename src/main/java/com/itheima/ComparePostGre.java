package com.itheima;

import com.alibaba.fastjson.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComparePostGre {


    public static final String SQLForGetTables = "select * from pg_tables WHERE schemaname='public'";
    public static int tableAccount = 0;
    public static int columnAccount = 0;
    public static String tableMessage1 ;
    public static String columnMessage ;
    public static String errTable;
    public static String db1 = "dmp";
    public static String db2 = "dmp";
    public static String IP1 = "0.0.0.14:30757";
    public static String IP2 = "0.0.0.45:31150";

    public static void main(String[] args) throws Exception {
        //开发环境
        Connection con1 = getConnection(IP1, db1, "postgres", "hello1234");
        //生产环境
        Connection con2 = getConnection(IP2, db2, "postgres", "hello1234");
        // 查询出字段名称
        List list = getInfos(con1, SQLForGetTables);
        List list2 = getInfos(con2, SQLForGetTables);
        System.out.println("将"+IP1+"的数据库"+db1+"与"+IP2+"数据库"+db2+"进行比较");
        tableMessage1="数据库"+db2+"缺少的表为： ";
        String res = checkTables(list, list2, con1, con2);
        System.out.println(tableMessage1);
        System.out.println("-------------------------比较完成");
        System.out.println();
        System.out.println("将"+IP2+"的数据库"+db2+"与"+IP1+"与数据库"+db1+"进行比较");
        tableMessage1="数据库"+db1+"缺少的表为：";
        res = checkTables(list2, list, con2, con1);
        System.out.println(tableMessage1);
        System.out.println("-------------------------比较完成");
        System.out.println("这两个数据库中 有"+tableAccount+"个表不相同");
        System.out.println("这两个数据库中 有"+columnAccount+"个字段不相同");
    }

    private static Connection getConnection(String ip, String dbName, String user, String pwd) throws ClassNotFoundException {
        Connection con = null;

        //String url1 = "jdbc:postgresql://10.70.21.14:30757/dmp";
        String url1 = "jdbc:postgresql://"+ip+"/"+dbName+"";
        Class.forName("org.postgresql.Driver");
        try {

            con = DriverManager.getConnection(url1, user, pwd);//如何用一个参数连接

        } catch (SQLException e) {
            System.out.println("开发环境数据库连接失败：" + e.getMessage());
        }
        return con;
    }


    // 比较两个LIST中的数据是否一致
    @SuppressWarnings("rawtypes")
    private static String checkTables(List list, List list2, Connection con1, Connection con2) {
        // 先将数据库1的数据与2比较
        int table = 0;
        int colum = 0;
        for (Object object : list) {
            table = 0;
            for (Object object2 : list2) {
                if (object.equals(object2)) {
                    colum = checkDetail(object, object2, con1, con2);
                    table = 1;
                    continue;
                }
            }
            if (table == 0) {
                errTable = getName(object);
                tableAccount++;
                tableMessage1 = tableMessage1 + getName(object) + "; ";
                table = 1;
                continue;
            }
        }
        if (table == 0 && colum == 0) {
            return "两个数据库中表和字段一致";
        }
        if (table == 0 && colum == 1) {
            return "两个数据库中表一致 字段不一致";
        }
        if (table == 1 && colum == 0) {
            return "两个数据库中表不一致 字段一致";
        } else
            return "两个数据库中表不一致 字段也不一致";
    }

    // 比较两个表字段是否一致
    @SuppressWarnings("rawtypes")
    private static int checkDetail(Object object, Object object2, Connection con1, Connection con2) {
        // 获得表的名字
        String table = getName(object);
        errTable = table;
        String sql = "SELECT A.attname FROM pg_catalog.pg_attribute A WHERE 1 = 1 AND A.attrelid = ( SELECT oid FROM pg_class WHERE relname = '" + table + "' ) AND A.attnum > 0 AND NOT A.attisdropped ORDER BY A.attnum";
        // 查询出字段名称
        List list = getInfos(con1, sql);
        List list2 = getInfos(con2, sql);
        return checkColunm(list, list2);
    }

    // 检查表中的字段是否一致
    @SuppressWarnings("rawtypes")
    private static int checkColunm(List list, List list2) {
        int res = 0;
        for (Object object : list) {
            int result = 0;
            for (Object object2 : list2) {
                // 如果这个字段在全部数据都匹配不到 则证明这个字段不匹配
                if (object.equals(object2)) {
                    result = 1;
                    break;
                }
            }
            if (result == 0) {
                columnAccount++;
                System.out.println("请注意 !表" + errTable + "中的" + getName(object) + "字段在后者数据库中没有");
                res = 1;
            }
        }
        return res;
    }

// 根据OBJECT获得name的值

    private static String getName(Object object) {

        Map map = JSONObject.parseObject(JSONObject.toJSONString(object), Map.class);
        return map.get("tablename")==null?map.get("attname")==null?"":map.get("attname").toString():map.get("tablename").toString();

    }



    // 查询数据库里面的表信息
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static List getInfos(Connection con1, String sql) {

        Statement stmt;

        List list = new ArrayList();
        try {
            stmt = con1.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            while (rs.next()) {
                Map map = new HashMap();
                for (int i = 1; i <= columnCount; i++) {
                    map.put(md.getColumnName(i), rs.getObject(i));
                }
                list.add(map);
            }

        } catch (SQLException e) {
            System.out.println("查询数据库里面的表信息失败:" + e.getMessage());
            e.printStackTrace();
        }

        return list;

    }
}
