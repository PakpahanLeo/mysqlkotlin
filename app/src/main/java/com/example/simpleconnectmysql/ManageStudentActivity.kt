package com.example.simpleconnectmysql

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.simpleconnectmysql.Server.ApiEndPoint
import kotlinx.android.synthetic.main.activity_manage_student.*
import org.json.JSONObject

class ManageStudentActivity : AppCompatActivity() {
    lateinit var i: Intent
    private var gender = "Pria"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_student)
        i = intent
        if (i.hasExtra("editmode")) {
            if (i.getStringExtra("editmode").equals("1")) {
                onEditMode()
            }
        }

        rgGender.setOnCheckedChangeListener { radioGroup, i ->

            when (i) {

                R.id.radioBoy -> {
                    gender = "Pria"
                }

                R.id.radioGirl -> {
                    gender = "Wanita"
                }

            }

        }

        btnCreate.setOnClickListener {
            create()
        }

        btnUpdate.setOnClickListener {
            update()
        }

        btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Hapus data ini ?")
                .setPositiveButton("HAPUS", DialogInterface.OnClickListener { dialogInterface, i ->
                    delete()
                })
                .setNegativeButton("BATAL", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                })
                .show()
        }


    }

    private fun onEditMode() {
        txNim.setText(i.getStringExtra("nim"))
        txName.setText(i.getStringExtra("name"))
        txAddress.setText(i.getStringExtra("address"))
        txNim.isEnabled = false

        btnCreate.visibility = View.GONE
        btnUpdate.visibility = View.VISIBLE
        btnDelete.visibility = View.VISIBLE

        gender = i.getStringExtra("gender")

        if (gender.equals("Pria")) {
            rgGender.check(R.id.radioBoy)
        } else {
            rgGender.check(R.id.radioGirl)
        }

    }

    private fun delete() {

        val loading = ProgressDialog(this)
        loading.setMessage("Menghapus data...")
        loading.show()

        AndroidNetworking.get(ApiEndPoint.DELETE + "?nim=" + txNim.text.toString())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {

                override fun onResponse(response: JSONObject?) {

                    loading.dismiss()
                    Toast.makeText(
                        applicationContext,
                        response?.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()

                    if (response?.getString("message")?.contains("successfully")!!) {
                        this@ManageStudentActivity.finish()
                    }

                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR", anError?.errorDetail?.toString())
                    Toast.makeText(applicationContext, "Connection Failure", Toast.LENGTH_SHORT)
                        .show()
                }


            })

    }

    private fun update() {

        val loading = ProgressDialog(this)
        loading.setMessage("Mengubah data...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.UPDATE)
            .addBodyParameter("nim", txNim.text.toString())
            .addBodyParameter("name", txName.text.toString())
            .addBodyParameter("address", txAddress.text.toString())
            .addBodyParameter("gender", gender)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {

                override fun onResponse(response: JSONObject?) {

                    loading.dismiss()
                    Toast.makeText(
                        applicationContext,
                        response?.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()

                    if (response?.getString("message")?.contains("successfully")!!) {
                        this@ManageStudentActivity.finish()
                    }

                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR", anError?.errorDetail?.toString())
                    Toast.makeText(applicationContext, "Connection Failure", Toast.LENGTH_SHORT)
                        .show()
                }


            })

    }

    private fun create() {

        val loading = ProgressDialog(this)
        loading.setMessage("Menambahkan data...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.CREATE)
            .addBodyParameter("nim", txNim.text.toString())
            .addBodyParameter("name", txName.text.toString())
            .addBodyParameter("address", txAddress.text.toString())
            .addBodyParameter("gender", gender)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {

                override fun onResponse(response: JSONObject?) {

                    loading.dismiss()
                    Toast.makeText(
                        applicationContext,
                        response?.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()

                    if (response?.getString("message")?.contains("successfully")!!) {
                        finish()
                    }

                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR", anError?.errorDetail?.toString())
                    Toast.makeText(applicationContext, "Connection Failure", Toast.LENGTH_SHORT)
                        .show()
                }


            })

    }
}
