package com.project.kosku.fragment

import android.animation.Animator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.kosku.R
import kotlinx.android.synthetic.main.fragment_cost.*

class CostFragment : Fragment() {
    var isFabOpen :Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cost, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fabAdd.setOnClickListener {
            if (!isFabOpen) showFabMenu() else closeFabMenu()
        }
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

}