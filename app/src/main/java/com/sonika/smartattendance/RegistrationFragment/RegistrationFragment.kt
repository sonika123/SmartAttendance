package com.sonika.smartattendance.RegistrationFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import butterknife.BindView
import com.mobsandgeeks.saripaar.annotation.*
import com.rengwuxian.materialedittext.MaterialEditText
import com.sonika.smartattendance.MainActivity
import com.sonika.smartattendance.R
import com.sonika.smartattendance.base.BaseFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_registration.*
import org.jetbrains.anko.support.v4.toast


class RegistrationFragment : BaseFragment() {

    @NotEmpty
    @Email
    @BindView(R.id.emailEditText)
    lateinit var emailEditText: MaterialEditText

    @NotEmpty
    @Password
    @BindView(R.id.passwordEditText)
    lateinit var passwordEditText: MaterialEditText

    @NotEmpty
    @ConfirmPassword
    @BindView(R.id.confirmPasswordEditText)
    lateinit var confirmPasswordEditText: MaterialEditText

    override val layoutId: Int
        get() = R.layout.fragment_registration

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginTextView.setOnClickListener {
            fragmentManager?.popBackStack()
        }
        registerButton.setOnClickListener {
            mValidator?.validate()
        }

    }


    override fun onValidationSucceeded() {
        super.onValidationSucceeded()
        requestUserRegistration()
    }

    private fun requestUserRegistration() {
        val auth = (activity as MainActivity).auth
        showProgressDialog()
        auth.createUserWithEmailAndPassword(
            emailEditText.text?.trim().toString(),
            passwordEditText.text?.trim().toString()
        ).addOnCompleteListener { task ->
            dismissProgressDialog()
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("TAG", "createUserWithEmail:success")
                val user = auth.currentUser
                Toasty.success(context!!, "User created : ${user?.email}").show()
                fragmentManager?.popBackStack()
            } else {
                // If sign in fails, display a message to the user.
                Log.w("TAG", "createUserWithEmail:failure", task.exception)
                dialog("Failed : ${task.exception?.message}")
            }
        }

    }

    companion object {
        fun newInstance(): RegistrationFragment {
            val fragment = RegistrationFragment()
            return fragment
        }
    }
}
