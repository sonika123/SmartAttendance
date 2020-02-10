package com.sonika.smartattendance

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sonika.smartattendance.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_welcome.*

class WelcomeFragment : BaseFragment() {
    override val layoutId: Int
        get() = R.layout.fragment_welcome

    companion object {
        fun newInstance(): WelcomeFragment {
            val fragment = WelcomeFragment()
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_checkin.setOnClickListener {
            val intent = Intent(context, FingerprintActivity::class.java)
            startActivity(intent)
        }

        btn_checkout.setOnClickListener {
            val intent = Intent(context, FingerprintActivity::class.java)
            startActivity(intent)
        }
    }
   /* @OnClick(R.id.btn_checkin)
    fun onButtonClicked(view: View?) {
        when (view?.id) {
            R.id.btn_checkin -> {
                val intent = Intent(context, FingerprintActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_checkout -> {
                val intent = Intent(context, FingerprintActivity::class.java)
                startActivity(intent)
            }
        }
    }*/
}