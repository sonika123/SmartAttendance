package com.sonika.smartattendance

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import co.infinum.goldfinger.Goldfinger
import com.google.firebase.auth.FirebaseAuth
import com.sonika.smartattendance.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_welcome.*
import androidx.annotation.NonNull
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.sonika.smartattendance.utils.getCurrentTime
import com.sonika.smartattendance.utils.getFormattedTime
import com.sonika.smartattendance.utils.getTodaysDate
import es.dmoral.toasty.Toasty


class WelcomeFragment : BaseFragment() {
    lateinit var db: FirebaseFirestore
    private var params: Goldfinger.PromptParams? = null
    private var goldfinger: Goldfinger? = null
    override val layoutId: Int get() = R.layout.fragment_welcome

    var isUserCheckedIn: Boolean = false
    var isCheckedOutAlready = false

    companion object {
        fun newInstance(): WelcomeFragment {
            val fragment = WelcomeFragment()
            return fragment
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userEmailTextView.text = UserInfo.email
        db = FirebaseFirestore.getInstance()
        initBiometric()
        setOnClickListeners()
        readCheckInStatus()
    }

    private fun readCheckInStatus() {
        showProgressDialog()
        val docRef =
            db.collection("attendance_record").document(UserInfo.userId)
                .collection(getTodaysDate().toString()).document("Record")
        docRef.get().addOnSuccessListener { document ->
            dismissProgressDialog()
            if (document.data != null) {
                val checkInTime = document.data?.get("CheckInTime")
                val chekOutTime = document.data?.get("CheckOutTime")
                Log.d("TAG", "DocumentSnapshot data: ${document.data.toString()}")
                isUserCheckedIn = checkInTime != null //if user is checked in or not
                isCheckedOutAlready =
                    checkInTime != null && chekOutTime != null //if user has already checked in and checked out
                handleUserStatus()
            } else {
                isUserCheckedIn = false
                Log.d("TAG", "No such document")
                handleUserStatus()
            }
        }.addOnFailureListener { e ->
            dismissProgressDialog()
            Log.w("TAG", "Error writing document", e)
        }
    }

    private fun handleUserStatus(){
        if(isCheckedOutAlready){
            btn_checkin.text = "THANKS"
        }else{
            if(isUserCheckedIn){
                btn_checkin.text = "CHECKOUT"
            }else{
                btn_checkin.text = "CHECKIN"
            }
        }
    }

    private fun setOnClickListeners() {
        attendButton.setOnClickListener {
            if (isCheckedOutAlready)

            else
                startBiometricAuth()
        }

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            UserInfo.clear()
            startActivity(Intent(context, MainActivity::class.java))
            activity?.finish()
        }
    }

    private fun initBiometric() {
        goldfinger = Goldfinger.Builder(context!!).build()
        params = Goldfinger.PromptParams.Builder(activity!!)
            .title("Please verify yourself.")
            .negativeButtonText("Cancel")
            .description("Use any biometric authentication available in your device.")
            .subtitle("Same authentication is required while you checkout.")
            .confirmationRequired(true)
            .build()
    }

    private fun startBiometricAuth() {

        if (goldfinger!!.canAuthenticate()) {
            /* Authenticate */
            goldfinger!!.authenticate(params!!, object : Goldfinger.Callback {
                override fun onError(e: Exception) {
                    dialog(e?.message)
                }

                override fun onResult(result: Goldfinger.Result) {
                    if (result.reason() == Goldfinger.Reason.AUTHENTICATION_SUCCESS) {
                        Toasty.success(context!!, "Finger print success").show()
                        handleFingerPrintSuccess()
                    } else if (result.reason() == Goldfinger.Reason.AUTHENTICATION_FAIL) {
                        Toasty.error(context!!, "Finger print failed").show()
                        goldfinger!!.cancel()
                    }

                }
            })


        }

    }

    private fun handleFingerPrintSuccess() {
        if (isUserCheckedIn) {
            //do checkout
            showProgressDialog()
            val checkInData = hashMapOf(
                "CheckOutTime" to getCurrentTime()
            )
            db.collection("attendance_record").document(UserInfo.userId)
                .collection(getTodaysDate().toString()).document("Record")
                .set(checkInData)
                .addOnSuccessListener {
                    dismissProgressDialog()
                    btn_checkin.text = "THANKS"
                    isCheckedOutAlready = true
                    Log.d("TAG", "DocumentSnapshot successfully written!")
                }
                .addOnFailureListener { e ->
                    dismissProgressDialog()
                    Log.w("TAG", "Error writing document", e)
                }
        } else {
            //do check in
            showProgressDialog()
            val checkInData = hashMapOf(
                "CheckInTime" to getCurrentTime()
            )
            db.collection("attendance_record").document(UserInfo.userId)
                .collection(getTodaysDate().toString()).document("Record")
                .set(checkInData)
                .addOnSuccessListener {
                    dismissProgressDialog()
                    btn_checkin.text = "CHECKOUT"
                    isUserCheckedIn = true
                    Log.d("TAG", "DocumentSnapshot successfully written!")

                }
                .addOnFailureListener { e ->
                    dismissProgressDialog()
                    Log.w("TAG", "Error writing document", e)
                }
        }
    }

}