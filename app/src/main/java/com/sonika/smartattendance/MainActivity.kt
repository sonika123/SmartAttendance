package com.sonika.smartattendance

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.sonika.smartattendance.base.BaseActivity
import com.sonika.smartattendance.login.LoginFragment

class MainActivity : BaseActivity() {
    override val layoutId: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var fragment: Fragment? = null
        fragment = LoginFragment.newInstance()
        changeFragment(fragment, cleanStack = true, addToBackStack = false)
    }
        /*loginButton.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "logged in", Toast.LENGTH_SHORT).show()
            var loginIntent = Intent(this, WelcomeFragment::class.java)
            startActivity(loginIntent)
        })*/

}