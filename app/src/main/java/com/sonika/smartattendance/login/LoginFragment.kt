package com.sonika.smartattendance.login

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import butterknife.BindView
import butterknife.OnClick
import com.sonika.smartattendance.R
import com.sonika.smartattendance.RegistrationFragment.RegistrationFragment
import com.sonika.smartattendance.WelcomeFragment
import com.sonika.smartattendance.base.BaseFragment
import kotlinx.android.synthetic.main.activity_login.*

class LoginFragment : BaseFragment() {

    override val layoutId: Int
        get() = R.layout.fragment_login

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginButton.setOnClickListener{
            val fragment = WelcomeFragment.newInstance()
            changeFragment(fragment)
        }

        signUpTextView.setOnClickListener {
            changeFragment(RegistrationFragment.newInstance())
        }

    }

    override fun onResume() {
        super.onResume()
        
    }

    companion object {
        fun newInstance(): LoginFragment {
            val fragment = LoginFragment()
            return fragment
        }
    }
}
