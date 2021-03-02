package com.project.kosku

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.project.kosku.fragment.CostFragment
import com.project.kosku.fragment.HomeFragment
import com.project.kosku.fragment.ReportFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            ViewCompat.setOnApplyWindowInsetsListener(
                this.window.decorView,
                object : OnApplyWindowInsetsListener {
                    override fun onApplyWindowInsets(
                        v: View,
                        insets: WindowInsetsCompat
                    ): WindowInsetsCompat? {
                        v.setPadding(
                            0,
                            0,
                            0,
                            v.paddingBottom + insets.systemWindowInsetBottom - 40
                        )
                        return insets
                    }
                })
        }


        if (savedInstanceState == null) {
            btmNavbar.selectedItemId = R.id.home
            addFragment(HomeFragment())
        }

        btmNavbar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    addFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.money -> {
                    addFragment(CostFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.report -> {
                    addFragment(ReportFragment())
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.containerFrame, fragment, fragment.javaClass.getSimpleName())
            .commit()
    }
}