package com.project.kosku

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.database.*
import com.project.kosku.model.Payment
import com.project.kosku.model.Tenant
import com.project.kosku.model.Wallet
import com.project.kosku.utility.ProgressDialog
import kotlinx.android.synthetic.main.activity_tenant.*
import kotlinx.android.synthetic.main.layout_payment.view.*
import java.text.SimpleDateFormat
import java.util.*

class TenantActivity : AppCompatActivity() {
    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mTenantDatabase: DatabaseReference
    private lateinit var mWalletDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    lateinit var pNominal: String
    lateinit var pDetail: String
    val timeStamp: String = System.currentTimeMillis().toString()
    var locale = Locale("id", "ID")
    lateinit var item : Tenant

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tenant)
        setToolbar()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }

        item = intent.getParcelableExtra("dataKos")!!

        tvName.setText(item.name)
        tvHp.setText(item.noHp)
        tvTggl.setText(item.tgglMasuk)

        if (item.status == false) {
            tvStatus.setTextColor(ResourcesCompat.getColor(resources, R.color.colorDanger, null))
            tvStatus.setText("BELUM BAYAR")
            btnBayar.visibility = View.VISIBLE
        } else {
            tvStatus.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBtn, null))
            tvStatus.setText("SUDAH BAYAR")
        }

        swipeRefresh.setOnRefreshListener {
            setData()
        }

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mTenantDatabase =
            mFirebaseInstance.getReference("Tenant").child(tvName.text.toString().trim())

        mWalletDatabase = mFirebaseInstance.getReference("Wallet")

        mFirebaseDatabase = mFirebaseInstance.getReference("Payment")

        btnBayar.setOnClickListener {
            addPayment()
        }
    }

    private fun setData() {

        mTenantDatabase.child("status").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@TenantActivity, "${p0.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.value == false) {
                    tvStatus.setTextColor(
                        ResourcesCompat.getColor(
                            resources,
                            R.color.colorDanger,
                            null
                        )
                    )
                    tvStatus.setText("BELUM BAYAR")
                    btnBayar.visibility = View.VISIBLE
                } else {
                    tvStatus.setTextColor(
                        ResourcesCompat.getColor(
                            resources,
                            R.color.colorBtn,
                            null
                        )
                    )
                    tvStatus.setText("SUDAH BAYAR")
                    btnBayar.visibility = View.GONE
                }
            }

        })

        swipeRefresh.isRefreshing = false
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
        payment.nama = tvName.text.toString().trim()
        payment.date = SimpleDateFormat("dd MMMM yyyy").format(Calendar.getInstance().time)

        mFirebaseDatabase.child(tvName.text.toString()).child(timeStamp)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@TenantActivity, "${error.message}", Toast.LENGTH_SHORT)
                        .show()
                    pDialog.dismiss()
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    mFirebaseDatabase.child(tvName.text.toString()).child(timeStamp).setValue(payment)
                    pDialog.dismiss()
                    Toast.makeText(
                        this@TenantActivity,
                        "Silahkan refresh halaman ^^",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })

        FirebaseDatabase.getInstance().getReference("Tenant").child(tvName.text.toString())
            .child("status").setValue(true)

        val wallet = Wallet()
        wallet.detail = "$pDetail dari ${tvName.text.toString().trim()}"
        wallet.nominal = pNominal
        wallet.tipe = true
        wallet.date = SimpleDateFormat("dd MMMM yyyy").format(Calendar.getInstance().time)

        mWalletDatabase.child("Income").child(
            Calendar.getInstance().get(
                Calendar.YEAR
            ).toString()
        ).child(Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, locale))
            .child(timeStamp).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                mWalletDatabase.child("Income").child(
                    Calendar.getInstance().get(
                        Calendar.YEAR
                    ).toString()
                ).child(
                    Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, locale)
                ).child(timeStamp).setValue(wallet)
            }
        })

        mWalletDatabase.child("All").child(Calendar.getInstance().get(Calendar.YEAR).toString())
            .child(Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, locale)).child(timeStamp).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                mWalletDatabase.child("All").child(Calendar.getInstance().get(Calendar.YEAR).toString())
                    .child(Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, locale)).child(timeStamp).setValue(wallet)
            }

        })
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("Detail Anak Kos")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tenant_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.history -> {
                startActivity(Intent(this,HistoryActivity::class.java).putExtra("namaKos",this.item.name))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}