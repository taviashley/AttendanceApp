package com.example.attendanceapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.andrognito.pinlockview.PinLockListener
import com.example.attendanceapp.databinding.EnterUserPinFragmentBinding
import com.example.attendanceapp.viewModel.UserPinViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import timber.log.Timber


class UserPinFragment : Fragment() {
    private lateinit var binding: EnterUserPinFragmentBinding
    private lateinit var enterUserPinViewModel: UserPinViewModel
    private var mainActivity: AttendanceActivity? = null
    private var bottomDialog: BottomSheetDialog? = null
    private var isIndicatorDots = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainActivity = activity as AttendanceActivity
        // Inflate the layout for this fragment
        binding = EnterUserPinFragmentBinding.inflate(inflater, container, false)
        bottomDialog = mainActivity?.applicationContext?.let { BottomSheetDialog(it) }
        return binding.root

    }

    private fun initialize() {

        clearNonTouchableFlag()

        binding.indicatorDots.pinLength = 4
        binding.pinLockView.attachIndicatorDots(binding.indicatorDots)
        binding.pinLockView.setPinLockListener(object : PinLockListener {
            override fun onComplete(pin: String) {
                if (!isIndicatorDots) {

                    showProgressDialog()
                    //Set a flag to prevent user from entering otp after an otp has already been entered

                    //Set a flag to prevent user from entering otp after an otp has already been entered
                    activity!!.window.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    )
                    enterUserPinViewModel.requestPin(1234)
                    isIndicatorDots = true
                    setupObservers()
                }
            }

            override fun onEmpty() {
                Timber.i("Empty pin lock")
            }

            override fun onPinChange(pinLength: Int, intermediatePin: String) {
                Timber.i("Pin length is: %d and intPin = %s", pinLength, intermediatePin)
            }
        })
    }

    private fun setupObservers() {

        enterUserPinViewModel.userPinResponse.observe(
            viewLifecycleOwner,
            Observer {
                hideProgressDialog()

                // Clear flag to allow user to interact again
                clearNonTouchableFlag()
                binding.pinLockView.resetPinLockView()

                clockInAlertDialog()

            })

        enterUserPinViewModel.userPinError.observe(
            viewLifecycleOwner,
            Observer {
               hideProgressDialog()

                // Clear flag to allow user to interact again
                clearNonTouchableFlag()
                binding.pinLockView.resetPinLockView()
            })
    }

    override fun onPause() {
        super.onPause()
        binding.pinLockView.resetPinLockView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.pinLockView.resetPinLockView()
    }

    override fun onStart() {
        super.onStart()
        enterUserPinViewModel = ViewModelProvider(this).get(UserPinViewModel::class.java)
        enterUserPinViewModel.clearData()
        initialize()
    }

    private fun clearNonTouchableFlag() {
        try {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } catch (ex: NullPointerException) {
            Timber.e(ex)
        }
    }

    fun clockInAlertDialog() {
        //Instantiate builder variable
        val builder = AlertDialog.Builder(activity)
        // set title
        builder.setTitle("Welcome to my Company")
        //set content area
        builder.setMessage("Clock-in Successful \n Have a nice day!")
        //set neutral button
        builder.setNeutralButton("Close") { dialog, id ->
            // User Click on close button
            dialog.dismiss()
        }
        builder.show()
        binding.pinLockView.resetPinLockView()
    }

    fun showProgressDialog() {
        binding.spinKit.visibility = View.VISIBLE
    }

    fun hideProgressDialog() {
        binding.spinKit.visibility = View.GONE
    }
}