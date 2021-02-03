package com.project.kosku

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.project.kosku.model.Tenant
import kotlinx.android.synthetic.main.activity_tenant.*

class TenantActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tenant)
        setToolbar()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val item = intent.getParcelableExtra<Tenant>("dataKos")

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