package com.project.kosku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.kosku.R
import com.project.kosku.model.Payment
import kotlinx.android.synthetic.main.row_history.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryAdapter(private var payments: ArrayList<Payment>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_history, parent, false)
        )
    }

    override fun getItemCount() = payments.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val payment = payments[position]

        val locale = Locale("in","ID")
        val rupiah = NumberFormat.getCurrencyInstance(locale)

        holder.itemView.tvNominal.text = rupiah.format(payment.nominal!!.toDouble())
        holder.itemView.tvDetail.text = payment.detail
        holder.itemView.tvDate.text = payment.date
    }

    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view)
}