package com.example.simpleconnectmysql.Adapter

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

    companion object {
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
        holder.view.lbNimList.text = "Kode obat : " + arrayList?.get(position)?.code_obat
        holder.view.lbNameList.text = "Nama obat : " + arrayList?.get(position)?.nama_obat
        holder.view.lbAddressList.text = "Harga obat : " + arrayList?.get(position)?.harga_obat
        holder.view.lbGenderList.text = "Jenis obat : " + arrayList?.get(position)?.jenis_obat

        holder.view.cvList.setOnClickListener {


            val i = Intent(context, ManageStudentActivity::class.java)
            i.putExtra("editmode", "1")
            i.putExtra("kode_obat", arrayList?.get(position)?.code_obat)
            i.putExtra("nama_obat", arrayList?.get(position)?.nama_obat)
            i.putExtra("harga_obat", arrayList?.get(position)?.harga_obat)
            i.putExtra("jenis_obat", arrayList?.get(position)?.jenis_obat)
            context!!.startActivity(i)
        }
    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

}