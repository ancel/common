package com.work.common.demo;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import javax.swing.filechooser.FileSystemView;
 
/**
 * 
 * 创建人：wanghaibo <br>
 * 创建时间：2015-4-27 下午5:44:13 <br>
 * 功能描述： <br>
 * 获取表信息
 * 
	TABLE_CAT String => 表类别（可为null）
	TABLE_SCHEM String => 表模式（可为null）
	TABLE_NAME String => 表名称
	COLUMN_NAME String => 列名称
	DATA_TYPE int => 来自 java.sql.Types 的 SQL 类型
	TYPE_NAME String => 数据源依赖的类型名称，对于 UDT，该类型名称是完全限定的
	COLUMN_SIZE int => 列的大小。
	BUFFER_LENGTH 未被使用。
	DECIMAL_DIGITS int => 小数部分的位数。对于 DECIMAL_DIGITS 不适用的数据类型，则返回 Null。
	NUM_PREC_RADIX int => 基数（通常为 10 或 2）
	NULLABLE int => 是否允许使用 NULL。
	columnNoNulls - 可能不允许使用NULL值
	columnNullable - 明确允许使用NULL值
	columnNullableUnknown - 不知道是否可使用 null
	REMARKS String => 描述列的注释（可为null）
	COLUMN_DEF String => 该列的默认值，当值在单引号内时应被解释为一个字符串（可为null）
	SQL_DATA_TYPE int => 未使用
	SQL_DATETIME_SUB int => 未使用
	CHAR_OCTET_LENGTH int => 对于 char 类型，该长度是列中的最大字节数
	ORDINAL_POSITION int => 表中的列的索引（从 1 开始）
	IS_NULLABLE String => ISO 规则用于确定列是否包括 null。
	YES --- 如果参数可以包括 NULL
	NO --- 如果参数不可以包括 NULL
	空字符串 --- 如果不知道参数是否可以包括 null
	SCOPE_CATLOG String => 表的类别，它是引用属性的作用域（如果 DATA_TYPE 不是 REF，则为null）
	SCOPE_SCHEMA String => 表的模式，它是引用属性的作用域（如果 DATA_TYPE 不是 REF，则为null）
	SCOPE_TABLE String => 表名称，它是引用属性的作用域（如果 DATA_TYPE 不是 REF，则为null）
	SOURCE_DATA_TYPE short => 不同类型或用户生成 Ref 类型、来自 java.sql.Types 的 SQL 类型的源类型（如果 DATA_TYPE 不是 DISTINCT 或用户生成的 REF，则为null）
	IS_AUTOINCREMENT String => 指示此列是否自动增加
	YES --- 如果该列自动增加
	NO --- 如果该列不自动增加
	空字符串 --- 如果不能确定该列是否是自动增加参数 
 * 版本： <br>
 * ====================================== <br>
 * 修改记录 <br>
 * ====================================== <br>
 * 序号 姓名 日期 版本 简单描述 <br>
 * 
 */  

public class DBHelpInfo {
     
    /**
     * 这里是Oracle连接方法
     *private static final String driver = "oracle.jdbc.driver.OracleDriver";
     *private static final String url = "jdbc:oracle:thin:@localhost:1521:orcl";
     *private static final String uid = "system";
     *private static final String pwd = "sys";
     *这里是SQL Server连接方法
     *private static final String url = "jdbc:sqlserver://localhost:1433;DateBaseName=数据库名";
     *private static final String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
     *private static final String uid = "sa";
     *private static final String pwd = "sa";
     *
     *
     * 这里是MySQL连接方法
     */
    private static final String driver="com.mysql.jdbc.Driver";
    private static final String pwd="root";
    private static final String user="root";
    private static final String url = "jdbc:mysql://localhost:3306/spider_backstage"
            + "?user=" + user + "&password=" + pwd
            + "&useUnicode=true&characterEncoding=UTF-8";
    private static Connection getConnection=null;
 
    public static void main(String[] args) {
        FileSystemView fsv=FileSystemView.getFileSystemView();
        String path=fsv.getHomeDirectory().toString();//获取当前用户桌面路径
        System.out.println(path);
        getConnection=getConnections();
        try {
            DatabaseMetaData dbmd=getConnection.getMetaData();
            ResultSet resultSet = dbmd.getTables(null, "%", "%", new String[] { "TABLE" });
            while (resultSet.next()) {
                String tableName=resultSet.getString("TABLE_NAME");
                //System.out.println(tableName);
                if(tableName.equals("spider_pri_subject")){
                    //ResultSet rs =getConnection.getMetaData().getColumns(null, getXMLConfig.getSchema(),tableName.toUpperCase(), "%");//其他数据库不需要这个方法的，直接传null，这个是oracle和db2这么用
                    ResultSet rs = dbmd.getColumns(null, "%", tableName, "%");
                    File directory = new File(path);
                    FileWriter fw = new FileWriter(directory+ "\\"+tableName.toUpperCase()+".xml");
                    PrintWriter pw = new PrintWriter(fw);
                    System.out.println("表名："+tableName+"\t\n表字段信息：");
                    pw.write("<p filid=\"xx\" table=\""+tableName.toUpperCase()+"\" zj=\"xx\"/>\n");
                    while(rs.next()){
                        pw.write("\t<p code=\""+rs.getString("COLUMN_NAME").toUpperCase()+"\" name=\""+rs.getString("REMARKS")+"\"/>\n");
                        System.out.println("字段名："+rs.getString("COLUMN_NAME")+"\t字段注释："+rs.getString("REMARKS")+"\t字段数据类型："+rs.getString("TYPE_NAME"));
                    }
                    pw.write("</p>");
                    pw.flush();
                    pw.close();
                }
            }
            System.out.println("生成成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnections(){
        try {
            //Properties props =new Properties();
            //props.put("remarksReporting","true");
            Class.forName(driver);
            getConnection=DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getConnection;
    }
     
     public static String getSchema() throws Exception {
            String schema;
            schema =getConnection.getMetaData().getUserName();
            if ((schema == null) || (schema.length() == 0)) {
                throw new Exception("ORACLE数据库模式不允许为空");
            }
            return schema.toUpperCase().toString();
 
    }
 
}