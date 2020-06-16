package com.example.simpleconnectmysql

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.*


val read = Scanner(System.`in`)
fun main(args: Array<String>) {


    var userInput: String

    while (true) { //Print the options for the user to choose from
        println("*****Apotek Huru hara*****")
        println("*. Press 1 to Login")
        println("*. Press 2 to Register")
        println("*. Press 3 to exit")
        // Prompt the use to make a choice
        println("Enter your choice:")
        //Capture the user input in scanner object and store it in a pre decalred variable
        userInput = read.next()
        when (userInput) {
            "1" ->  //do the job number 1
                login()
            "2" ->  //do the job number 2
                register()
            "3" -> {
                //exit from the program
                println("Exiting...")
                System.exit(0)
                //inform user in case of invalid choice.
                println("Invalid choice. Read the options carefully...")
            }
            else -> println("Invalid choice. Read the options carefully...")
        }
    }


}

fun login() {
    println("Masukkan Username: ")
    var username = read.next()

    println("Masukkan Password: ")
    var password = read.next()

    try {

        var conn = getMySqlConnection()
        val st: PreparedStatement =
            conn!!.prepareStatement("Select username, password from users_new where username=? and password=?") as PreparedStatement
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

fun register() {

    println("Masukkan Username: ")
    var username = read.next()

    println("Masukkan Password: ")
    var password = read.next()

    println("Masukkan Email: ")
    var email = read.next()

    try {
        var conn = getMySqlConnection()
        var statement = conn!!.createStatement()
        val query =
            (" insert into users_new (username, password, email)"
                    + " values (?, ?, ?)")
        val preparedStmt = conn.prepareStatement(query)
        preparedStmt.setString(1, username)
        preparedStmt.setString(2, password)
        preparedStmt.setString(3, email)
//        preparedStmt.execute()
        val i: Int = preparedStmt.executeUpdate()
        if (i > 0) {
            println("Berhasil daftar")
        } else {
            println("Gagal daftar")
        }

        conn!!.close()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

fun getDataObat() {
    var records = ""
    var userInput: String
    try {

        var conn = getMySqlConnection()
        var statement = conn!!.createStatement()
        var resultSet = statement.executeQuery("SELECT * FROM tb_student_new")
        println("Kode obat: " + "\t" + "Nama obat: " + "\t" + "Harga obat: " + "\t" + "Jenis obat: " + "\t" + "Jumlah obat: ")
        while (resultSet.next()) {
            println(
                resultSet.getInt(1).toString() + "\t\t" + resultSet.getString(2) + "\t\t" + resultSet.getString(
                    3
                ) + "\t\t" + resultSet.getString(4) + "\t\t" + resultSet.getString(5)
            );

        }
        conn!!.close()

        while (true) { //Print the options for the user to choose from
            println("*****CRUD Obat*****")
            println("*. Press 1 to Sell")
            println("*. Press 2 to Create")
            println("*. Press 3 to Update")
            println("*. Press 4 to Delete")
            println("*. Press 5 to exit")
            // Prompt the use to make a choice
            println("Enter your choice:")
            //Capture the user input in scanner object and store it in a pre decalred variable
            userInput = read.next()
            when (userInput) {
                "1" ->  //do the job number 1
                    jualObat()
                "2" ->  //do the job number 1
                    createObat()
                "3" ->  //do the job number 2
                    updateObat()
                "4" ->  //do the job number 2
                    deleteObat()
                "5" -> {
                    //exit from the program
                    println("Exiting...")
                    System.exit(0)
                    //inform user in case of invalid choice.
                    println("Invalid choice. Read the options carefully...")
                }
                else -> println("Invalid choice. Read the options carefully...")
            }
        }


    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

fun jualObat(){
    println("Masukkan Kode Obat Yang akan di jual: ")
    var kode_obat = read.nextInt()

    println("Masukkan Jumlah Obat Yang akan di jual: ")
    var jumlah_obat = read.next()

    try {
        var conn = getMySqlConnection()
        var statement = conn!!.createStatement()

        var resultSet = statement.executeQuery("SELECT * FROM tb_student_new where code_obat =$kode_obat")

        if (resultSet.next()) {
            var nilaiawal = (resultSet.getString(5).toInt() - jumlah_obat.toInt())
            val query =
                "Update tb_student_new set jumlah_obat=? where code_obat =$kode_obat"
            val preparedStmt = conn.prepareStatement(query)
            preparedStmt.setString(1, nilaiawal.toString())
//        preparedStmt.execute()
            val i: Int = preparedStmt.executeUpdate()
            if (i > 0) {
                println("Berhasil menjual obat")
            } else {
                println("Gagal menjual obat")
            }

        }



        conn!!.close()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

fun createObat() {
    println("Masukkan Kode Obat: ")
    var kode_obat = read.nextInt()

    println("Masukkan Nama Obat: ")
    var nama_obat = read.next()

    println("Masukkan Harga Obat: ")
    var harga_obat = read.next()

    println("Masukkan Jenis Obat: ")
    var jenis_obat = read.next()

    println("Masukkan Jumlah Obat: ")
    var jumlah_obat = read.next()

    try {
        var conn = getMySqlConnection()
        var statement = conn!!.createStatement()
        val query =
            (" insert into tb_student_new (code_obat, nama_obat, harga_obat, jenis_obat, jumlah_obat)"
                    + " values (?, ?, ?, ?, ?)")
        val preparedStmt = conn.prepareStatement(query)
        preparedStmt.setInt(1, kode_obat)
        preparedStmt.setString(2, nama_obat)
        preparedStmt.setString(3, harga_obat)
        preparedStmt.setString(4, jenis_obat)
        preparedStmt.setString(5, jumlah_obat)
//        preparedStmt.execute()
        val i: Int = preparedStmt.executeUpdate()
        if (i > 0) {
            println("Berhasil membuat data obat")
        } else {
            println("Gagal membuat data obat")
        }

        conn!!.close()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

fun updateObat() {
    println("Masukkan Kode Obat Yang akan di edit: ")
    var kode_obat = read.nextInt()

    println("Masukkan Nama Obat: ")
    var nama_obat = read.next()

    println("Masukkan Harga Obat: ")
    var harga_obat = read.next()

    println("Masukkan Jenis Obat: ")
    var jenis_obat = read.next()

    println("Masukkan Jumlah Obat: ")
    var jumlah_obat = read.next()



    try {
        var conn = getMySqlConnection()
        var statement = conn!!.createStatement()
        val query =
            "Update tb_student_new set nama_obat=?,harga_obat=?,jenis_obat=?,jumlah_obat=? where code_obat =$kode_obat"
        val preparedStmt = conn.prepareStatement(query)
        preparedStmt.setString(1, nama_obat)
        preparedStmt.setString(2, harga_obat)
        preparedStmt.setString(3, jenis_obat)
        preparedStmt.setString(4, jumlah_obat)
//        preparedStmt.execute()
        val i: Int = preparedStmt.executeUpdate()
        if (i > 0) {
            println("Berhasil edit data obat")
        } else {
            println("Gagal edit data obat")
        }

        conn!!.close()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

fun deleteObat() {
    println("Masukkan Kode Obat Yang akan di hapus: ")
    var kode_obat = read.nextInt()


    try {
        var conn = getMySqlConnection()
        var statement = conn!!.createStatement()
        val query =
            "delete from tb_student_new where code_obat =$kode_obat"
        val preparedStmt = conn.prepareStatement(query)
//        preparedStmt.execute()
        val i: Int = preparedStmt.executeUpdate()
        if (i > 0) {
            println("Berhasil hapus data obat")
        } else {
            println("Gagal hapus data obat")
        }

        conn!!.close()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}


fun getMySqlConnection(): Connection? {
    var ret: Connection? = null
    try { /* Register for jdbc driver class. */
        Class.forName("com.mysql.jdbc.Driver")
        /* Create connection url. */
        val mysqlConnUrl = "jdbc:mysql://192.168.0.2:3306/db_simplecrud"
        /* db user name. */
        val mysqlUserName = "leoa"
        /* db password. *///        println("Database Name is $dbName")
//        println("Database Version is $dbVersion")
//        println("Database Connection Url is $dbUrl")
//        println("Database User Name is $userName")
//        println("Database Driver Name is $driverName")
        val mysqlPassword = "Sgtleo1917"
        /* Get the Connection object. */ret =
            DriverManager.getConnection(mysqlConnUrl, mysqlUserName, mysqlPassword)
        /* Get related meta data for this mysql server to verify db connect successfully.. */
        val dbmd = ret.metaData
        val dbName = dbmd.databaseProductName
        val dbVersion = dbmd.databaseProductVersion
        val dbUrl = dbmd.url
        val userName = dbmd.userName
        val driverName = dbmd.driverName

    } catch (ex: java.lang.Exception) {
        ex.printStackTrace()
    } finally {
        return ret
    }
}
