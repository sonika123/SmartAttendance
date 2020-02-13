package com.sonika.smartattendance.login

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import butterknife.BindView
import butterknife.OnClick
import com.mobsandgeeks.saripaar.annotation.Email
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.mobsandgeeks.saripaar.annotation.Password
import com.rengwuxian.materialedittext.MaterialEditText
import com.sonika.smartattendance.MainActivity
import com.sonika.smartattendance.R
import com.sonika.smartattendance.RegistrationFragment.RegistrationFragment
import com.sonika.smartattendance.UserInfo
import com.sonika.smartattendance.WelcomeFragment
import com.sonika.smartattendance.base.BaseActivity
import com.sonika.smartattendance.base.BaseFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_login.*

class LoginFragment : BaseFragment() {

    @NotEmpty
    @Email
    @BindView(R.id.emailEditText)
    lateinit var emailEditText: MaterialEditText

    @NotEmpty
    @Password
    @BindView(R.id.passwordEditText)
    lateinit var passwordEditText: MaterialEditText


    override val layoutId: Int
        get() = R.layout.fragment_login

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(UserInfo.loginStatus){
            val fragment = WelcomeFragment.newInstance()
            fragmentManager?.popBackStack()
            changeFragment(fragment)
        }
        loginButton.setOnClickListener {
            mValidator?.validate()
        }

        signUpTextView.setOnClickListener {
            changeFragment(RegistrationFragment.newInstance())
        }

    }

    override fun onValidationSucceeded() {
        super.onValidationSucceeded()
        requestAuthentication()
    }

    private fun requestAuthentication() {
        val auth = (activity as MainActivity).auth
        showProgressDialog()
        auth.signInWithEmailAndPassword(
            emailEditText.text?.trim().toString(),
            passwordEditText.text?.trim().toString()
        ).addOnCompleteListener { task ->
            dismissProgressDialog()
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                val user = auth.currentUser
                UserInfo.apply {
                    loginStatus = true
                    email = user?.email.toString()
                    userId = user?.uid.toString()
                }
                Toasty.success(context!!, "Login Successful : ${user?.email}").show()
                val fragment = WelcomeFragment.newInstance()
                fragmentManager?.popBackStack()
                changeFragment(fragment)
            } else {
                // If sign in fails, display a message to the user.
                Log.w("TAG", "createUserWithEmail:failure", task.exception)
                dialog("Failed : ${task.exception?.message}")
            }


        }
    }

    override fun onResume() {
        super.onResume()
        //reset the text fields
        emailEditText.setText("")
        passwordEditText.setText("")
    }

    companion object {
        fun newInstance(): LoginFragment {
            val fragment = LoginFragment()
            return fragment
        }
    }
}
