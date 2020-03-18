package com.example.simpleconnectmysql

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.*


fun main(args: Array<String>) {

    val read = Scanner(System.`in`)

    println("Masukkan Username: ")
    var username = read.next()

    println("Masukkan Password: ")
    var password = read.next()



    try {

        var conn = getMySqlConnection()
        val st: PreparedStatement =
            conn!!.prepareStatement("Select username, password from users where username=? and password=?") as PreparedStatement
        st.setString(1, username);
        st.setString(2, password);
        val rs: ResultSet = st.executeQuery()
        if (rs.next()) {
            println("Berhasil login")
            getDataObat()
        } else {
            println("Gagal login")
        }

        conn!!.close()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }


}

fun getDataObat() {
    var records = ""
    try {

        var conn = getMySqlConnection()
        var statement = conn!!.createStatement()
        var resultSet = statement.executeQuery("SELECT * FROM tb_student")
        println("Kode obat: " + "\t" + "Nama obat: " + "\t" + "Harga obat: "+ "\t" + "Jenis obat: ")
        while (resultSet.next()) {
            records += resultSet.getString(1) + " " + resultSet.getString(2) + "\n"
//            println("Daftar nama obat")
//            println(resultSet.getString(2))
            println(resultSet.getInt(1).toString() + "\t\t" + resultSet.getString(2)+ "\t\t" + resultSet.getString(3)+ "\t\t" + resultSet.getString(4));

        }
        conn!!.close()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

fun getMySqlConnection(): Connection? { //        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
/* Declare and initialize a sql Connection variable. */
//    var policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
//    StrictMode.setThreadPolicy(policy)
    var ret: Connection? = null
    try { /* Register for jdbc driver class. */
        Class.forName("com.mysql.jdbc.Driver")
        /* Create connection url. */
        val mysqlConnUrl = "jdbc:mysql://192.168.0.11:3306/db_simplecrud"
        /* db user name. */
        val mysqlUserName = "root"
        /* db password. */
        val mysqlPassword = "Sgtleo19"
        /* Get the Connection object. */ret =
            DriverManager.getConnection(mysqlConnUrl, mysqlUserName, mysqlPassword)
        /* Get related meta data for this mysql server to verify db connect successfully.. */
        val dbmd = ret.metaData
        val dbName = dbmd.databaseProductName
        val dbVersion = dbmd.databaseProductVersion
        val dbUrl = dbmd.url
        val userName = dbmd.userName
        val driverName = dbmd.driverName
//        println("Database Name is $dbName")
//        println("Database Version is $dbVersion")
//        println("Database Connection Url is $dbUrl")
//        println("Database User Name is $userName")
//        println("Database Driver Name is $driverName")
    } catch (ex: java.lang.Exception) {
        ex.printStackTrace()
    } finally {
        return ret
    }
}
