package com.project.kosku

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.project.kosku.adapter.HistoryAdapter
import com.project.kosku.model.Payment
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {
    private var dataList = ArrayList<Payment>()
    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }

        mFirebaseInstance = FirebaseDatabase.getInstance()

        val nameT = intent.getStringExtra("namaKos")

        tvTitle.text = "RIWAYAT PEMBAYARAN ${nameT?.toUpperCase()}"
        mFirebaseDatabase = mFirebaseInstance.getReference("Payment").child(nameT.toString())

        rvHistory.layoutManager = LinearLayoutManager(this)
        rvHistory.adapter = HistoryAdapter(dataList)

        loadData()
    }

    private fun loadData() {
        mFirebaseDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HistoryActivity, "${error.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataList.clear()

                for (getData in dataSnapshot.children) {
                    val payment = getData.getValue(Payment::class.java)
                    dataList.add(payment!!)
                }

                if (dataList.isEmpty()) {
                    tvNoPayment.visibility = View.VISIBLE
                } else {
                    tvNoPayment.visibility = View.GONE
                }

                rvHistory.adapter = HistoryAdapter(dataList)
            }

        })
    }
}