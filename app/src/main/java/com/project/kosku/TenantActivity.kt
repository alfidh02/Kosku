package com.project.kosku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.project.kosku.model.Tenant
import kotlinx.android.synthetic.main.activity_tenant.*

class TenantActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tenant)
        setToolbar()

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val item = intent.getParcelableExtra<Tenant>("dataKos")

        tvName.setText(item!!.name)
        tvHp.setText(item!!.noHp)
        tvTggl.setText(item!!.tgglMasuk)
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