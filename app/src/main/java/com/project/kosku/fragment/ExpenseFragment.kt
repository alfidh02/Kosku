package com.project.kosku.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.project.kosku.R
import com.project.kosku.adapter.WalletAdapter
import com.project.kosku.model.Wallet
import kotlinx.android.synthetic.main.fragment_expenditure.*
import java.util.*


class ExpenseFragment : Fragment() {
    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    private var dataList = ArrayList<Wallet>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_expenditure, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var locale = Locale("id", "ID")

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mFirebaseDatabase = mFirebaseInstance.getReference("Wallet").child("Outcome").child(
            Calendar.getInstance().get(
                Calendar.YEAR
            ).toString()
        ).child(Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, locale))

        rvExpenditure.layoutManager = LinearLayoutManager(context!!)
        rvExpenditure.adapter = WalletAdapter(dataList)

        loadData()
    }

    private fun loadData() {
        mFirebaseDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "${error.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataList.clear()

                for (getData in dataSnapshot.children) {
                    val wallet = getData.getValue(Wallet::class.java)
                    dataList.add(wallet!!)
                }

                rvExpenditure.adapter = WalletAdapter(dataList)
            }

        })
    }

}