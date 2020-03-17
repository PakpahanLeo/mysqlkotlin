package com.example.simpleconnectmysql

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.simpleconnectmysql.Helper.JSONParser
import com.example.simpleconnectmysql.Model.BaseParam
import com.example.simpleconnectmysql.Server.ApiEndPoint
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    companion object {

        var URL = ApiEndPoint.AUTH

        var jsonParser: JSONParser = JSONParser()

        var i = 0

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.title = "Login"
        val builder = StrictMode.ThreadPolicy.Builder().permitAll()
        StrictMode.setThreadPolicy(builder.build())
        btnSignIn.setOnClickListener {
            val attemptLogin = AttemptLogin(this)
            attemptLogin.execute(
                editName.getText().toString(),
                editPassword.getText().toString(),
                ""
            )
        }

        btnRegister.setOnClickListener {
            if (i === 0) {
                i = 1
                editEmail.visibility = View.VISIBLE
                btnSignIn.visibility = View.GONE
                btnRegister.text = "CREATE ACCOUNT"
            } else {
                btnRegister.text = "REGISTER"
                editEmail.visibility = View.GONE
                btnSignIn.visibility = View.VISIBLE
                i = 0
                val attemptLogin = AttemptLogin(this)
                attemptLogin.execute(
                    editName.text.toString(),
                    editPassword.text.toString(),
                    editEmail.text.toString()
                )
            }
        }

    }

    class AttemptLogin(private val context: Context) : AsyncTask<String?, String?, JSONObject?>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }


        override fun doInBackground(vararg args: String?): JSONObject? {
            var email: String? = args!!.get(2)
            var password: String? = args.get(1)
            var name: String? = args.get(0)

            val params = mutableListOf<BaseParam>()


            params.add(BaseParam("username", name!!))
            params.add(BaseParam("password", password!!))
            if (email!!.length > 0) params.add(BaseParam("email", email))


            return jsonParser.makeHttpRequest(URL, "POST", params)
        }

        override fun onPostExecute(result: JSONObject?) { // dismiss the dialog once product deleted
//Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            try {
                if (result != null) {
                    val i = Intent(context, MainActivity::class.java)
                    Toast.makeText(
                        context,
                        result.getString("message"),
                        Toast.LENGTH_LONG
                    ).show()
                    context.startActivity(i)

                } else {
                    Toast.makeText(
                        context,
                        "Unable to retrieve any data from server",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

    }


}

