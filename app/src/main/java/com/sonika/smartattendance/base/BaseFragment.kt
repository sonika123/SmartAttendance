package com.sonika.smartattendance.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.Unbinder
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import org.jetbrains.anko.support.v4.toast

abstract class BaseFragment : Fragment(), Validator.ValidationListener {
    abstract val layoutId: Int

    protected var mValidator: Validator? = null
    private var unbinder: Unbinder? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mValidator = Validator(this)
        mValidator?.apply {
            validationMode = Validator.Mode.BURST
            setValidationListener(this@BaseFragment)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(layoutId, container, false)
        unbinder = ButterKnife.bind(this, rootView)
        return rootView
    }

    fun changeFragment(fragment: Fragment) {
        (activity as BaseActivity).changeFragment(fragment)
    }

    override fun onValidationFailed(errors: MutableList<ValidationError>?) {
        var count = 0
        if (errors != null)
            for (error in errors) {
                val view = error.view
                var message: String? = error.getCollatedErrorMessage(context)
                // Display error messages ;)
                if (view is EditText) {
                    view.error = message
                } else {
                    //only show single toast error in case
                    toast(message!!)
                    break
                }
                count++
            }
    }

    override fun onValidationSucceeded() {
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder?.unbind()
    }


}