package com.project.kosku.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.project.kosku.R
import com.project.kosku.model.Wallet
import kotlinx.android.synthetic.main.row_cost.view.*
import java.text.NumberFormat
import java.util.*

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

        val locale = Locale("in", "ID")
        val rupiah = NumberFormat.getCurrencyInstance(locale)

        holder.itemView.tvNominal.text = rupiah.format(wallet.nominal!!.toDouble())
        holder.itemView.tvDetail.text = wallet.detail
        holder.itemView.tvDate.text = wallet.date

        holder.itemView.ibDelete.setOnClickListener {
            val builder = AlertDialog.Builder(it.context)

            with(builder)
            {
                setTitle("Hapus Data?")
                setMessage("Yakin ingin hapus data?")
                setPositiveButton("YA") { dialogInterface, i ->
                    if (wallet.tipe == true) {
                        FirebaseDatabase.getInstance().getReference("Wallet").child("Income").child(wallet.year!!)
                            .child(wallet.month!!).child(wallet.id!!).removeValue()
                            .addOnCompleteListener(object : OnCompleteListener<Void> {
                                override fun onComplete(task: Task<Void>) {
                                    if (task.isSuccessful) Toast.makeText(
                                        it.context,
                                        "Berhasil menghapus data",
                                        Toast.LENGTH_SHORT
                                    ).show() else Toast.makeText(
                                        it.context,
                                        "Gagal menghapus data",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })

                        FirebaseDatabase.getInstance().getReference("Wallet").child("All").child(wallet.year!!)
                            .child(wallet.month!!).child(wallet.id!!).removeValue()
                            .addOnCompleteListener(object : OnCompleteListener<Void> {
                                override fun onComplete(task: Task<Void>) {
                                    if (task.isSuccessful) Toast.makeText(
                                        it.context,
                                        "Berhasil menghapus data",
                                        Toast.LENGTH_SHORT
                                    ).show() else Toast.makeText(
                                        it.context,
                                        "Gagal menghapus data",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                    } else {
                        FirebaseDatabase.getInstance().getReference("Wallet").child("Outcome").child(wallet.year!!)
                            .child(wallet.month!!).child(wallet.id!!).removeValue()
                            .addOnCompleteListener(object : OnCompleteListener<Void> {
                                override fun onComplete(task: Task<Void>) {
                                    if (task.isSuccessful) Toast.makeText(
                                        it.context,
                                        "Berhasil menghapus data",
                                        Toast.LENGTH_SHORT
                                    ).show() else Toast.makeText(
                                        it.context,
                                        "Gagal menghapus data",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })

                        FirebaseDatabase.getInstance().getReference("Wallet").child("All").child(wallet.year!!)
                            .child(wallet.month!!).child(wallet.id!!).removeValue()
                            .addOnCompleteListener(object : OnCompleteListener<Void> {
                                override fun onComplete(task: Task<Void>) {
                                    if (task.isSuccessful) Toast.makeText(
                                        it.context,
                                        "Berhasil menghapus data",
                                        Toast.LENGTH_SHORT
                                    ).show() else Toast.makeText(
                                        it.context,
                                        "Gagal menghapus data",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })

                    }
                }
                setNegativeButton("TIDAK") { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                show()
            }
        }
    }

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view)
}