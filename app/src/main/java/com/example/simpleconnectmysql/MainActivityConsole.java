package com.example.simpleconnectmysql;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivityConsole extends AppCompatActivity {
    String ip = "192.168.0.11";
    String db = "db_simplecrud";
    String un = "root";
    String password = "password";
    String records = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_console);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
//        new Async().execute();

        try {
            MainActivityConsole mySqlExample = new MainActivityConsole();

            Connection conn = mySqlExample.getMySqlConnection();

            Statement statement = conn.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM tb_student");

            while (resultSet.next()) {

                records += resultSet.getString(1) + " " + resultSet.getString(2) + "\n";
                System.out.println("Data dari tabel " + resultSet.getString(2) );

            }
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        CONN();
    }

    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnURL = "jdbc: jtds: sqlserver://" + ip + "/" + db + ";user=" + un + ";password=" + password + ";";

            conn = DriverManager.getConnection(ConnURL);

        } catch (SQLException se) {
            Log.e("error here 1 : ", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("error here 2 : ", e.getMessage());
        } catch (Exception e) {
            Log.e("error here 3 : ", e.getMessage());
        }

        return conn;
    }


    public Connection getMySqlConnection() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
//        StrictMode.ThreadPolicy.Builder().permitAll().build()
//        StrictMode.setThreadPolicy(policy)

        /* Declare and initialize a sql Connection variable. */
        Connection ret = null;

        try {

            /* Register for jdbc driver class. */
            Class.forName("com.mysql.jdbc.Driver");

            /* Create connection url. */
            String mysqlConnUrl = "jdbc:mysql://192.168.0.11:3306/db_simplecrud";

            /* db user name. */
            String mysqlUserName = "root";

            /* db password. */
            String mysqlPassword = "Sgtleo19";

            /* Get the Connection object. */
            ret = DriverManager.getConnection(mysqlConnUrl, mysqlUserName, mysqlPassword);

            /* Get related meta data for this mysql server to verify db connect successfully.. */
            DatabaseMetaData dbmd = ret.getMetaData();

            String dbName = dbmd.getDatabaseProductName();

            String dbVersion = dbmd.getDatabaseProductVersion();

            String dbUrl = dbmd.getURL();

            String userName = dbmd.getUserName();

            String driverName = dbmd.getDriverName();

            System.out.println("Database Name is " + dbName);

            System.out.println("Database Version is " + dbVersion);

            System.out.println("Database Connection Url is " + dbUrl);

            System.out.println("Database User Name is " + userName);

            System.out.println("Database Driver Name is " + driverName);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return ret;
        }
    }


    class Async extends AsyncTask<Void, Void, Void> {


        String records = "", error = "";

        @Override

        protected Void doInBackground(Void... voids) {

            try {
                System.out.println("DISINI1");
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println("DISINI2");
                Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.0.11:3306/db_simplecrud", "root", "");
                System.out.println("DISINI3");

                Statement statement = connection.createStatement();

                ResultSet resultSet = statement.executeQuery("SELECT * FROM tb_student");

                while (resultSet.next()) {

                    records += resultSet.getString(1) + " " + resultSet.getString(2) + "\n";
                    System.out.println("DATANYAAA" + records);

                }

            } catch (Exception e) {

                error = e.toString();

            }

            return null;

        }


        @Override

        protected void onPostExecute(Void aVoid) {


            super.onPostExecute(aVoid);

        }


    }
}
