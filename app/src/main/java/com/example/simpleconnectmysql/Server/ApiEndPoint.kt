package com.example.simpleconnectmysql.Server

class ApiEndPoint {
    companion object {
        val SERVER = "http://192.168.0.11:8686/simplecrud/"
        val CREATE = SERVER + "create.php"
        val READ = SERVER + "read.php"
        val DELETE = SERVER + "delete.php"
        val UPDATE = SERVER + "update.php"
        val AUTH = SERVER + "auth/index.php"
    }
}