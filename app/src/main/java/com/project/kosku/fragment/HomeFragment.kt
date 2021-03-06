package com.project.kosku.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.project.kosku.R
import com.project.kosku.adapter.HomeAdapter
import com.project.kosku.model.Tenant
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_dialog.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    lateinit var kName: String
    lateinit var kNo: String
    lateinit var dateRegist: String

    private var dataList = ArrayList<Tenant>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        view.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                etSearch.clearFocus()
                hideKeyboard()
            }
            true
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mFirebaseDatabase = mFirebaseInstance.getReference("Tenant")

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                ivClear.visibility = View.VISIBLE
                filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        ivClear.setOnClickListener {
            etSearch.setText("")
            ivClear.visibility = View.GONE
        }

        setDate()
        fabAdd.setOnClickListener {
            dialogAdd()
        }

        rvList.layoutManager = LinearLayoutManager(context!!)
        rvList.adapter = HomeAdapter(dataList)

        loadData()
    }

    private fun filter(text: String) {
        val temp = ArrayList<Tenant>()
        for (d in dataList) {
            if (d.name!!.toLowerCase().contains(text.toLowerCase())) {
                temp.add(d)
            }
        }
        (rvList.adapter as HomeAdapter).updateList(temp)
    }

    private fun hideKeyboard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
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
                    tenant?.let { dataList.add(it) }
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

        pickDate.setOnClickListener {
            showDatePicker()
        }

        val dialog = AlertDialog.Builder(context!!)
        dialog.apply {
            setTitle("Data Kos")
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
            kName = view.etName.text.toString().trim()
            kNo = view.etNo.text.toString().trim()

            if (kName.equals("") && kNo.equals("")) {
                view.etName.error = "Nama tidak boleh kosong"
                view.etNo.error = "No. HP masa tidak punya?!"
            } else if (kName.equals("")) {
                view.etName.error = "Nama tidak boleh kosong"
                view.etName.requestFocus()
            } else if (kNo.equals("")) {
                view.etNo.error = "No. HP masa tidak punya?!"
                view.etNo.requestFocus()
            } else {
                saveData(kName, kNo, dateRegist)
                alertDialog.dismiss()
            }
        }
    }

    private fun saveData(kName: String, kNo: String, dateRegist: String) {

        val tenant = Tenant()
        tenant.name = kName
        tenant.noHp = kNo
        tenant.tgglMasuk = dateRegist

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
        var calendar = Calendar.getInstance()
        calendar.set(p1, p2, p3)

        var dateFormat = SimpleDateFormat("dd/MM/yyyy")
        dateRegist = dateFormat.format(calendar.time)
    }

}
