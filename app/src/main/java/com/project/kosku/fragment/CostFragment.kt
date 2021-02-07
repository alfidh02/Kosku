package com.project.kosku.fragment

import android.animation.Animator
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.*
import com.project.kosku.R
import com.project.kosku.adapter.TabAdapter
import com.project.kosku.model.Wallet
import kotlinx.android.synthetic.main.fragment_cost.*
import kotlinx.android.synthetic.main.layout_dialog.view.*
import kotlinx.android.synthetic.main.layout_wallet.view.*
import java.text.SimpleDateFormat
import java.util.*

class CostFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    var isFabOpen :Boolean = false
    lateinit var tNominal: String
    lateinit var tDetail: String
    lateinit var dateTransaction: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cost, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mFirebaseDatabase = mFirebaseInstance.getReference("Wallet")

        val adapter = TabAdapter(childFragmentManager, tabLayout.tabCount)
        adapter.addFragment(IncomeFragment(), "Pemasukan")
        adapter.addFragment(OutcomeFragment(), "Pengeluaran")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        fabAdd.setOnClickListener {
            if (!isFabOpen) showFabMenu() else closeFabMenu()
        }

        fabLayout1.setOnClickListener {
            dialogAdd()
        }
    }

    private fun dialogAdd() {

        val view: View = layoutInflater.inflate(R.layout.layout_wallet, null)
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
            tNominal = view.etNominal.text.toString().trim()
            tDetail = view.etDetail.text.toString().trim()

            if (tNominal.equals("") && tDetail.equals("")) {
                view.etName.error = "Nama tidak boleh kosong"
                view.etNo.error = "No. HP masa tidak punya?!"
            } else if (tNominal.equals("")) {
                view.etName.error = "Nama tidak boleh kosong"
                view.etName.requestFocus()
            } else if (tDetail.equals("")) {
                view.etNo.error = "No. HP masa tidak punya?!"
                view.etNo.requestFocus()
            } else {
                saveOutcome(tNominal, tDetail, dateTransaction)
                alertDialog.dismiss()
            }
        }
    }

    private fun saveOutcome(tNominal: String, tDetail: String, dateTransaction: String) {

        val wallet = Wallet()
        wallet.nominal = tNominal
        wallet.detail = tDetail
        wallet.date = dateTransaction
        wallet.tipe = false

        mFirebaseDatabase.child("Income").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                mFirebaseDatabase.child("Income").setValue(wallet)
                succesAnimation()
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, "${p0.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun showFabMenu() {
        isFabOpen = true
        fabLayout1.visibility = View.VISIBLE
        fabLayout2.visibility = View.VISIBLE

        fabAdd.animate().rotationBy(135F)
        fabLayout1.animate().translationY(-resources.getDimension(R.dimen.standard_70))
        fabLayout2.animate().translationY(-resources.getDimension(R.dimen.standard_120))
    }

    private fun closeFabMenu() {
        isFabOpen = false

        fabAdd.animate().rotation(0F)
        fabLayout1.animate().translationY(0F)
        fabLayout2.animate().translationY(0F)
        fabLayout2.animate().translationY(0F).setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}

            override fun onAnimationEnd(animator: Animator?) {
                if (!isFabOpen) {
                    fabLayout1.visibility = View.GONE
                    fabLayout2.visibility = View.GONE
                }
            }

            override fun onAnimationCancel(p0: Animator?) {}

            override fun onAnimationStart(p0: Animator?) {}

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
        dateTransaction = dateFormat.format(calendar.time)
    }

    private fun succesAnimation() {
        val  dialogBuilder = AlertDialog.Builder(context!!)
        val layoutView : View = layoutInflater.inflate(R.layout.layout_success, null)
        val dialogButton : Button = layoutView.findViewById(R.id.btnDialog)
        dialogBuilder.setView(layoutView)

        val  alertDialog = dialogBuilder.create()
        alertDialog.show()
        dialogButton.setOnClickListener {
            alertDialog.dismiss()
        }
    }
}