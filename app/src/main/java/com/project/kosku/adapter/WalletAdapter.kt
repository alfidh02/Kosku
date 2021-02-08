package com.project.kosku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.project.kosku.R
import com.project.kosku.model.Wallet
import kotlinx.android.synthetic.main.row_cost.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class WalletAdapter(private var wallets: ArrayList<Wallet>) :
    RecyclerView.Adapter<WalletAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_cost, parent, false)
        )
    }

    override fun getItemCount() = wallets.size

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val wallet = wallets[position]

        if (wallet.tipe == false) holder.itemView.ivType.setImageDrawable(
            ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.ic_expenditure
            )
        ) else holder.itemView.ivType.setImageDrawable(
            ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.ic_income
            )
        )

        val locale = Locale("in","ID")
        val rupiah = NumberFormat.getCurrencyInstance(locale)

        holder.itemView.tvNominal.text = rupiah.format(wallet.nominal!!.toDouble())
        holder.itemView.tvDetail.text = wallet.detail
        holder.itemView.tvDate.text = wallet.date
    }

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view)

    fun updateList(list: ArrayList<Wallet>) {
        wallets = list
        notifyDataSetChanged()
    }
}