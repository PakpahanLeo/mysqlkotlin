package com.example.simpleconnectmysql

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.simpleconnectmysql.Adapter.RVAdapterStudent
import com.example.simpleconnectmysql.Model.Students
import com.example.simpleconnectmysql.Server.ApiEndPoint
import com.facebook.stetho.okhttp3.StethoInterceptor
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.net.ConnectException
import java.net.InetSocketAddress
import java.net.Socket


class MainActivity : AppCompatActivity() {

    var arrayList = ArrayList<Students>()

    companion object {
        val REQUEST_ID_MULTIPLE_PERMISSIONS = 1

        fun isPortOpen(ip: String?, port: Int, timeout: Int): Boolean {
            return try {
                val socket = Socket()
                socket.connect(InetSocketAddress(ip, port), timeout)
                socket.close()
                println("PORT MASUK")
                true
            } catch (ce: ConnectException) {
                ce.printStackTrace()
                println("PORT GAGAL")
                false
            } catch (ex: Exception) {
                ex.printStackTrace()
                false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val builder = StrictMode.ThreadPolicy.Builder().permitAll()
        StrictMode.setThreadPolicy(builder.build())


        setContentView(R.layout.activity_main)




        isPortOpen("192.168.0.161", 8686, 3000)

        val okHttpClient = OkHttpClient().newBuilder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()
        AndroidNetworking.initialize(applicationContext, okHttpClient)

        supportActionBar?.title = "Data Obat"
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        mFloatingActionButton.setOnClickListener {
            startActivity(Intent(this, ManageStudentActivity::class.java))
        }

        if (checkPermissions(
                applicationContext
            )
        ) {
            println("SUDAH BISA")
        } else {
            checkAndRequestPermissions()
        }
    }


    fun checkPermissions(context: Context?): Boolean {
        return (ActivityCompat.checkSelfPermission(
            context!!,
            Manifest.permission.INTERNET
        ) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_NETWORK_STATE
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun checkAndRequestPermissions(): Boolean {
        val writeExternalPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_NETWORK_STATE
        )
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET)
        }
        if (writeExternalPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_NETWORK_STATE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        val builder = StrictMode.ThreadPolicy.Builder().permitAll()
        StrictMode.setThreadPolicy(builder.build())
        loadAllStudents()
    }


    private fun loadAllStudents() {
        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()
        AndroidNetworking.enableLogging()
        AndroidNetworking.get(ApiEndPoint.READ)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {

                override fun onResponse(response: JSONObject?) {
                    arrayList.clear()

                    val jsonArray = response?.optJSONArray("result")
                    Log.d("jsonResponse", "jsonArray : " + jsonArray);

                    if (jsonArray?.length() == 0) {
                        loading.dismiss()
                        Toast.makeText(
                            applicationContext, "Student data is empty, Add the data first",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    for (i in 0 until jsonArray?.length()!!) {

                        val jsonObject = jsonArray?.optJSONObject(i)
                        arrayList.add(
                            Students(
                                jsonObject.getString("code_obat"),
                                jsonObject.getString("nama_obat"),
                                jsonObject.getString("harga_obat"),
                                jsonObject.getString("jenis_obat")
                            )
                        )

                        if (jsonArray?.length() - 1 == i) {

                            loading.dismiss()
                            val adapter = RVAdapterStudent(arrayList)
//                            mRecyclerView.setLayoutManager(
//                                LinearLayoutManager(
//                                    this@MainActivity, RecyclerView.VERTICAL,
//                                    false
//                                )
//                            )
                            mRecyclerView.apply {
                                layoutManager = GridLayoutManager(context as Activity, 2)
                            }

                            adapter.notifyDataSetChanged()
                            mRecyclerView.adapter = adapter
                            mRecyclerView.setHasFixedSize(true)
                            mRecyclerView.setRecycledViewPool(RecyclerView.RecycledViewPool())

                        }

                    }

                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR", anError?.toString())
                    Toast.makeText(applicationContext, "Connection Failure", Toast.LENGTH_SHORT)
                        .show()
                }
            })


    }
}
