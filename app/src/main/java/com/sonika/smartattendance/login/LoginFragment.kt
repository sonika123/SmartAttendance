package com.sonika.smartattendance.login

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import butterknife.BindView
import butterknife.OnClick
import com.sonika.smartattendance.R
import com.sonika.smartattendance.WelcomeFragment
import com.sonika.smartattendance.base.BaseFragment
import kotlinx.android.synthetic.main.activity_login.*

class LoginFragment : BaseFragment() {

//    @BindView(R.id.loginButton)
//    lateinit var loginButton: Button

    override val layoutId: Int
        get() = R.layout.fragment_login

   /* @OnClick(R.id.loginButton)
    fun onLoginButtonClicked (view: View?) {
        when (view?.id) {
            R.id.loginButton -> {
                Toast.makeText(context, "hdah", Toast.LENGTH_SHORT).show()
                val fragment = WelcomeFragment.newInstance()
                changeFragment(fragment)

            }
        }
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginButton.setOnClickListener{
            val fragment = WelcomeFragment.newInstance()
            changeFragment(fragment)
        }

    }

    companion object {
        fun newInstance(): LoginFragment {
            val fragment = LoginFragment()
            return fragment
        }
    }
}
