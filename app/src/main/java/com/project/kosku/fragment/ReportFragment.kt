package com.project.kosku.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.project.kosku.R
import com.project.kosku.adapter.ReportAdapter
import com.project.kosku.model.Wallet
import kotlinx.android.synthetic.main.fragment_report.*
import java.util.*
import kotlin.collections.ArrayList


class ReportFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    private var monthSelected: String = ""
    private var yearSelected: String = ""
    private var dataList = ArrayList<Wallet>()
    private var years = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFirebaseInstance = FirebaseDatabase.getInstance()

        var thisYear = Calendar.getInstance().get(Calendar.YEAR)
        for (i in 2020..thisYear) {
            years.add(i.toString())
        }

        val adaptYear = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, years)
        adaptYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerYear.adapter = adaptYear
        spinnerYear.onItemSelectedListener = this

        val adaptMonth = ArrayAdapter.createFromResource(
            context!!,
            R.array.months,
            android.R.layout.simple_spinner_item
        )
        adaptMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMonth.adapter = adaptMonth
        spinnerMonth.onItemSelectedListener = this

        rvTable.layoutManager = LinearLayoutManager(context!!)
        rvTable.adapter = ReportAdapter(dataList)
    }

    private fun loadData(yearSelected: String, monthSelected: String) {

        mFirebaseDatabase =
            mFirebaseInstance.getReference("Wallet").child("All").child(yearSelected)
                .child(monthSelected)

        mFirebaseDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, "${p0.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataList.isEmpty()) dataList.clear()

                for (getData in dataSnapshot.children) {
                    val report = getData.getValue(Wallet::class.java)
                    dataList.add(report!!)
                }
                rvTable.adapter = ReportAdapter(dataList)
            }

        })
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val spinner: Spinner = p0 as Spinner

        if (spinner.id == R.id.spinnerMonth) monthSelected = p0?.getItemAtPosition(p2).toString()
        else if (spinner.id == R.id.spinnerYear) yearSelected = p0?.getItemAtPosition(p2).toString()

        Toast.makeText(context, "Sukses ganti ke $monthSelected $yearSelected", Toast.LENGTH_SHORT)
            .show()
        loadData(yearSelected, monthSelected)
    }

}