package com.project.kosku.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.kosku.R
import com.project.kosku.TenantActivity
import com.project.kosku.model.Tenant
import kotlinx.android.synthetic.main.row_user.view.*

class HomeAdapter(private var tenants: ArrayList<Tenant>) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_user, parent, false)
        )
    }

    override fun getItemCount() = tenants.size

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val tenant = tenants[position]
        var url = "https://api.whatsapp.com/send?phone=62${tenant.noHp}"

        holder.itemView.setOnClickListener {
            it.context.startActivity(
                Intent(it.context, TenantActivity::class.java)
            )
                //Passing the Parcelable
                //
                //val intent = Intent(this, AnotherActivity::class.java)
                //intent.putExtra("extra_item", item)
                //
                //Retrieving the Parcelable
                //
                //val item = intent.getParcelableExtra("extra_item")
                //// Do something with the item (example: set Item title and price)
        }

        holder.itemView.tvName.text = tenant.name

        holder.itemView.tvNo.text = tenant.noHp
        holder.itemView.tvNo.setOnClickListener {
            it.context.startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)))
        }

        holder.itemView.tvDate.text = tenant.tgglMasuk
    }

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view)

    fun updateList(list: ArrayList<Tenant>) {
        tenants = list
        notifyDataSetChanged()
    }
}