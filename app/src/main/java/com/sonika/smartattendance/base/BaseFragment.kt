package com.sonika.smartattendance.base

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.Unbinder
import com.afollestad.materialdialogs.MaterialDialog
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import org.jetbrains.anko.support.v4.toast

abstract class BaseFragment : Fragment(), Validator.ValidationListener {
    abstract val layoutId: Int

    protected var mValidator: Validator? = null
    private var unbinder: Unbinder? = null
    protected var mProgressDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(layoutId, container, false)
        unbinder = ButterKnife.bind(this, rootView)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mValidator = Validator(this)
        mValidator?.apply {
            validationMode = Validator.Mode.BURST
            setValidationListener(this@BaseFragment)
        }
        mProgressDialog = ProgressDialog(context)
        mProgressDialog?.setMessage("Please wait...")
        mProgressDialog?.setCancelable(false)
    }

    fun showProgressDialog() {
        if (mProgressDialog != null && !mProgressDialog!!.isShowing)
            mProgressDialog!!.show()
    }


    fun dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing)
            mProgressDialog!!.dismiss()
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

    fun dialog(message: String?) {
        if(message == null)
            return
        val materialDialog = MaterialDialog.Builder(context!!)
            .content(message)
        materialDialog.apply {
            positiveText("OK")
            onPositive { dialog, _ ->
                dialog.dismiss()
            }
        }
        materialDialog.show()
    }

    fun hideSoftKeyboard() {
        val inputMethodManager =
            activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity!!.currentFocus!!.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder?.unbind()
    }


}