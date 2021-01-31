package com.project.kosku.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.project.kosku.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_dialog.*
import java.util.*

class HomeFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var locale = Locale("id", "ID")

        tvMonth.setText(
            Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, locale)
        )
        tvYear.setText(Calendar.getInstance().get(Calendar.YEAR).toString())

        fabAdd.setOnClickListener {
            dialogAdd()
        }

    }

    private fun dialogAdd() {
        val view: View = layoutInflater.inflate(R.layout.layout_dialog, null)
        val pickDate : Button = view.findViewById(R.id.btnDate)

        val dialog = AlertDialog.Builder(context!!)
        dialog.apply {
            setTitle("Data Penyewa")
            setNegativeButton("Batal") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            setPositiveButton("Tambah") { dialogInterface, i ->

            }
        }

        pickDate.setOnClickListener {
            showDatePicker()
        }

        dialog.setView(view)
        dialog.show()
    }

    private fun showDatePicker() {

        val datePickerDialog = DatePickerDialog (
            context!!,
            this,
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        val date:String = "tanggal sekarang : " + p3 + "/" + p2+1 + "/" +p1
    }

}
