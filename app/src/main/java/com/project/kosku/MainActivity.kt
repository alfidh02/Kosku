package com.project.kosku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.project.kosku.fragment.CostFragment
import com.project.kosku.fragment.HomeFragment
import com.project.kosku.fragment.ReportFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            btmNavbar.setItemSelected(R.id.home, true)
            supportFragmentManager.beginTransaction().replace(R.id.containerFrame, HomeFragment()).commit()
        }

        btmNavbar.setOnItemSelectedListener { id ->
            var fragment : Fragment? = null
            when (id) {
                R.id.home -> fragment = HomeFragment()
                R.id.money -> fragment = CostFragment()
                R.id.report -> fragment = ReportFragment()
            }
            if (fragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.containerFrame,fragment).commit()
            } else {
                Log.e("TAG", "Error membuat fragment")
            }
        }
    }
}