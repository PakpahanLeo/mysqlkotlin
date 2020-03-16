package com.example.simpleconnectmysql.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleconnectmysql.ManageStudentActivity
import com.example.simpleconnectmysql.Model.Students
import com.example.simpleconnectmysql.R
import kotlinx.android.synthetic.main.student_list.view.*

class RVAdapterStudent(private val arrayList: ArrayList<Students>) :
    RecyclerView.Adapter<RVAdapterStudent.Holder>() {

    companion object{
        var context: Context? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        context = parent.context
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.student_list,
                parent,
                false
            )
        )
    }


    override fun getItemCount(): Int = arrayList!!.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.lbNimList.text = arrayList?.get(position)?.nim
        holder.view.lbNameList.text = "Nama : " + arrayList?.get(position)?.name
        holder.view.lbAddressList.text = "Alamat : " + arrayList?.get(position)?.address
        holder.view.lbGenderList.text = "Jenkel : " + arrayList?.get(position)?.gender

        holder.view.cvList.setOnClickListener {


            val i = Intent(context, ManageStudentActivity::class.java)
            i.putExtra("editmode", "1")
            i.putExtra("nim", arrayList?.get(position)?.nim)
            i.putExtra("name", arrayList?.get(position)?.name)
            i.putExtra("address", arrayList?.get(position)?.address)
            i.putExtra("gender", arrayList?.get(position)?.gender)
            context!!.startActivity(i)
        }
    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

}