package com.sonika.smartattendance.RegistrationFragment

import android.os.Bundle
import android.view.View
import android.widget.EditText
import butterknife.BindView
import com.mobsandgeeks.saripaar.annotation.*
import com.rengwuxian.materialedittext.MaterialEditText
import com.sonika.smartattendance.R
import com.sonika.smartattendance.WelcomeFragment
import com.sonika.smartattendance.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_registration.*
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.annotations.NotNull


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
        toast("Validation success")
    }

    companion object {
        fun newInstance(): RegistrationFragment {
            val fragment = RegistrationFragment()
            return fragment
        }
    }
}
