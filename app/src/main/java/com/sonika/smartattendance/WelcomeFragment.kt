package com.sonika.smartattendance

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
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
        userEmailTextView.text = UserInfo.email
        btn_checkin.setOnClickListener {
            val intent = Intent(context, FingerprintActivity::class.java)
            startActivity(intent)
        }

        btn_checkout.setOnClickListener {
            val intent = Intent(context, FingerprintActivity::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(context, MainActivity::class.java))
            activity?.finish()
        }
    }
}