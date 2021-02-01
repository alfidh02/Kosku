package com.project.kosku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.kosku.R
import com.project.kosku.model.Tenant
import kotlinx.android.synthetic.main.row_user.view.*

class HomeAdapter (private val tenants : ArrayList<Tenant>) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_user,parent,false)
        )
    }

    override fun getItemCount() = tenants.size

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val tenant = tenants[position]

        holder.itemView.tvName.text = tenant.name
        holder.itemView.tvNo.text = tenant.noHp
    }

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view)

}