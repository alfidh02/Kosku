package com.project.kosku.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.project.kosku.R
import com.project.kosku.adapter.HomeAdapter
import com.project.kosku.model.Tenant
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

class HomeFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    lateinit var kName: String
    lateinit var kNo: String

    private var dataList = ArrayList<Tenant>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mFirebaseDatabase = mFirebaseInstance.getReference("Tenant")

        setDate()

        fabAdd.setOnClickListener {
            dialogAdd()
        }

        setupRecycler()
    }

    private fun setupRecycler() {
        rvList.layoutManager = LinearLayoutManager(context!!.applicationContext)
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
                    val tenant = getData.getValue(Tenant::class.java)
                    dataList.add(tenant!!)
                }

                rvList.adapter = HomeAdapter(dataList)
            }

        })
    }

    private fun setDate() {
        var locale = Locale("id", "ID")

        tvMonth.setText(
            Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, locale)
        )
        tvYear.setText(Calendar.getInstance().get(Calendar.YEAR).toString())
    }

    private fun dialogAdd() {
        val view: View = layoutInflater.inflate(R.layout.layout_dialog, null)
        val pickDate: Button = view.findViewById(R.id.btnDate)
        val etName: EditText = view.findViewById(R.id.etName)
        val etNo: EditText = view.findViewById(R.id.etNo)

        val dialog = AlertDialog.Builder(context!!)
        dialog.apply {
            setTitle("Data Kos")
            setNegativeButton("Batal") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            setPositiveButton("Tambah") { dialogInterface, i ->
                kName = etName.text.toString()
                kNo = etNo.text.toString()

                if (kName.equals("")) {
                    Toast.makeText(context, "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                    etName.requestFocus()
                } else if (kNo.equals("")) {
                    Toast.makeText(context, "Nomor HP masa tidak punya?!", Toast.LENGTH_SHORT)
                        .show()
                    etNo.requestFocus()
                } else {
                    saveData(kName, kNo)
                }
            }
        }

        pickDate.setOnClickListener {
            showDatePicker()
        }

        dialog.setView(view)
        dialog.show()
    }

    private fun saveData(kName: String, kNo: String) {

        val tenant = Tenant()
        tenant.name = kName
        tenant.noHp = kNo

        mFirebaseDatabase.child(kName).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mFirebaseDatabase.child(kName).setValue(tenant)
                Toast.makeText(context, "Tambah data berhasil!", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun showDatePicker() {

        val datePickerDialog = DatePickerDialog(
            context!!,
            this,
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        val date: String = "tanggal sekarang : " + p3 + "/" + p2 + 1 + "/" + p1
    }

}
