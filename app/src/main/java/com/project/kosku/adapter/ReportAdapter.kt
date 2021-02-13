package com.project.kosku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.kosku.R
import com.project.kosku.model.Wallet
import kotlinx.android.synthetic.main.row_table.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ReportAdapter(private var reports: ArrayList<Wallet>) :
    RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        return ReportViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_table, parent, false)
        )
    }

    override fun getItemCount() = reports.size

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = reports[position]

        val locale = Locale("in","ID")
        val rupiah = NumberFormat.getCurrencyInstance(locale)

        holder.itemView.tvNomor.setText((position + 1).toString())
        holder.itemView.tvKet.setText(report.detail)
        holder.itemView.tvTanggal.setText(report.date)
        if (report.tipe == true) holder.itemView.tvNominal.setText("+ ${rupiah.format(report.nominal!!.toDouble())}") else holder.itemView.tvNominal.setText(
            "- ${rupiah.format(report.nominal!!.toDouble())}")
    }

    class ReportViewHolder(view: View) : RecyclerView.ViewHolder(view)
}