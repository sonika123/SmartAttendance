package com.sonika.smartattendance

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import co.infinum.goldfinger.Goldfinger
import com.google.firebase.auth.FirebaseAuth
import com.sonika.smartattendance.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_welcome.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.sonika.smartattendance.utils.getCurrentTime
import com.sonika.smartattendance.utils.getTodaysDate
import es.dmoral.toasty.Toasty
import java.net.NetworkInterface
import java.util.*
import android.net.wifi.WifiManager
import android.opengl.Visibility


class WelcomeFragment : BaseFragment() {
    private var wifiInfo: WifiInfo? = null
    lateinit var db: FirebaseFirestore
    private var params: Goldfinger.PromptParams? = null
    private var goldfinger: Goldfinger? = null
    override val layoutId: Int get() = R.layout.fragment_welcome

    var isUserCheckedIn: Boolean = false
    var isCheckedOutAlready = false

    var checkInTime: String? = null
    var checkOutTime: String? = null

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
        wifiInfo = getWifiInfo()
        initBiometric()
        setOnClickListeners()
        checkWifiValidation()
    }

    /**
     * Check wifi validation with current connected and saved in database
     */
    private fun checkWifiValidation() {
        showProgressDialog()
        val docRef =
            db.collection("settings").document("wifiInfo")
        docRef.get().addOnSuccessListener { document ->
            dismissProgressDialog()
            if (document.data != null) {
                val dbMacAddress = document.data?.get("wifi_mac_address")?.toString()
                val dbSsid = document.data?.get("wifi_ssid")?.toString()
                Log.d("TAG", "DocumentSnapshot data: ${document.data.toString()}")
                handleWifiStatus(dbMacAddress, dbSsid)
            } else {

            }
        }.addOnFailureListener { e ->
            dismissProgressDialog()
            Log.w("TAG", "Error writing document", e)
        }
    }

    private fun handleWifiStatus(dbMacAddress: String?, dbSsid: String?) {
        if (dbMacAddress == wifiInfo?.macAddress && dbSsid == wifiInfo?.ssid){
            //wifi validation success
            wifiValidationFailedMessage.visibility = View.GONE
            attendButton.visibility = View.VISIBLE
            todaysRecord.visibility = View.VISIBLE
            readCheckInStatus()
        }else{
            //wifi validation failed
            wifiValidationFailedMessage.visibility = View.VISIBLE
            attendButton.visibility = View.GONE
            todaysRecord.visibility = View.GONE
        }
    }

    private fun readCheckInStatus() {
        showProgressDialog()
        val docRef =
            db.collection("attendance_record").document(UserInfo.userId)
                .collection(getTodaysDate().toString()).document("Record")
        docRef.get().addOnSuccessListener { document ->
            dismissProgressDialog()
            if (document.data != null) {
                checkInTime = document.data?.get("CheckInTime")?.toString()
                checkOutTime = document.data?.get("CheckOutTime")?.toString()
                Log.d("TAG", "DocumentSnapshot data: ${document.data.toString()}")
                isUserCheckedIn = checkInTime != null //if user is checked in or not
                isCheckedOutAlready =
                    checkInTime != null && checkOutTime != null //if user has already checked in and checked out
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

    private fun handleUserStatus() {
        if (isCheckedOutAlready) {
            btn_checkin.text = "THANKS"
        } else {
            if (isUserCheckedIn) {
                btn_checkin.text = "CHECKOUT"
            } else {
                btn_checkin.text = "CHECKIN"
            }
        }
        var todaysRecordText = ""
        if (checkInTime?.isNotEmpty() == true) {
            todaysRecordText += "CheckIn Time : $checkInTime"
        }
        if (checkOutTime?.isNotEmpty() == true) {
            todaysRecordText += "\nCheckOut Time : $checkOutTime"
        }
        todaysRecord.text = todaysRecordText
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
        logo.setOnClickListener {
            dialog("${wifiInfo?.macAddress}\n${wifiInfo?.ssid}")
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
                .set(checkInData, SetOptions.merge())
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

    fun getWifiInfo(): WifiInfo {
        val wifiInfo = WifiInfo()
        try {
            val interfaceName = "wlan0"
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                if (!intf.getName().equals(interfaceName, true)) {
                    continue
                }

                val mac = intf.getHardwareAddress()

                val buf = StringBuilder()
                for (aMac in mac) {
                    buf.append(String.format("%02X:", aMac))
                }
                if (buf.length > 0) {
                    buf.deleteCharAt(buf.length - 1)
                }
                wifiInfo.macAddress = buf.toString()
            }
        } catch (ex: Exception) {
        }
        val wifiManager =
            activity?.application?.applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiMACaddress = wifiManager.connectionInfo.ssid
        wifiInfo.ssid = wifiMACaddress.replace("\"", "")
        return wifiInfo
    }


}