package com.sonika.smartattendance

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.sonika.smartattendance.base.BaseActivity
import com.sonika.smartattendance.login.LoginFragment

class MainActivity : BaseActivity() {
    override val layoutId: Int
        get() = R.layout.activity_main

    lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        var fragment: Fragment? = null
        fragment = LoginFragment.newInstance()
        changeFragment(fragment, cleanStack = true, addToBackStack = false)
    }

}