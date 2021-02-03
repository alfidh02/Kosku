package com.project.kosku

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.project.kosku.utility.ProgressDialog
import com.google.firebase.database.*
import com.project.kosku.model.Payment
import com.project.kosku.model.Tenant
import kotlinx.android.synthetic.main.activity_tenant.*
import kotlinx.android.synthetic.main.layout_payment.view.*
import java.util.*

class TenantActivity : AppCompatActivity() {
    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    lateinit var pNominal: String
    lateinit var pDetail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tenant)
        setToolbar()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val item = intent.getParcelableExtra<Tenant>("dataKos")

        var locale = Locale("id", "ID")

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mFirebaseDatabase = mFirebaseInstance.getReference("Payment").child(Calendar.getInstance().get(Calendar.YEAR)
            .toString()).child(Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, locale))

        tvName.setText(item!!.name)
        tvHp.setText(item!!.noHp)
        tvTggl.setText(item!!.tgglMasuk)

        if (item!!.status == false) {
            tvStatus.setTextColor(ResourcesCompat.getColor(resources, R.color.colorDanger, null))
            tvStatus.setText("BELUM BAYAR")
            btnBayar.visibility = View.VISIBLE
        } else {
            tvStatus.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBtn, null))
            tvStatus.setText("SUDAH BAYAR")
        }

        btnBayar.setOnClickListener {
            addPayment()
        }
    }

    private fun addPayment() {
        val view: View = layoutInflater.inflate(R.layout.layout_payment, null)
        val dialog = AlertDialog.Builder(this)
        dialog.apply {
            setTitle("Data Pembayaran")
            setNegativeButton("Batal") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            setPositiveButton("Tambah", null)
        }

        dialog.setView(view)

        val alertDialog = dialog.create()
        alertDialog.show()

        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            pNominal = view.etNominal.text.toString().trim()
            pDetail = view.etDetail.text.toString().trim()

            if (pNominal.equals("") && pDetail.equals("")) {
                view.etNominal.error = "Nominal tidak boleh kosong"
                view.etDetail.error = "Harap isi keterangan"
            } else if (pNominal.equals("")) {
                view.etNominal.error = "Nominal tidak boleh kosong"
                view.etNominal.requestFocus()
            } else if (pDetail.equals("")) {
                view.etDetail.error = "Harap isi keterangan"
                view.etDetail.requestFocus()
            } else {
                saveData(pNominal, pDetail)
                alertDialog.dismiss()
            }
        }
    }

    private fun saveData(pNominal: String, pDetail: String) {
        var pDialog = ProgressDialog.progressDialog(this)
        pDialog.show()

        val payment = Payment()
        payment.nominal = pNominal
        payment.detail = pDetail

        mFirebaseDatabase.child(tvName.text.toString()).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@TenantActivity, "${error.message}", Toast.LENGTH_SHORT).show()
                pDialog.dismiss()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mFirebaseDatabase.child(tvName.text.toString()).setValue(payment)
                pDialog.dismiss()
            }

        })

    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("Detail Anak Kos")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}