package com.assignment.mdev1001_m2023_assignment4

import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.assignment.mdev1001_m2023_assignment4.databinding.ActivityRegisterBinding
import com.assignment.mdev1001_m2023_assignment4.firebase.FirebaseController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    var firebaseController = FirebaseController()
    var isShown = false
    var isShownConfPass = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        initView()
    }

    private fun initView() {
        auth = Firebase.auth
        mBinding.btnRegister.setOnClickListener {
            if (mBinding.edtFirstName.text.isNullOrEmpty()
                || mBinding.edtLastName.text.isNullOrEmpty()
                || mBinding.edtEmailId.text.isNullOrEmpty()
                || mBinding.edtUsername.text.isNullOrEmpty()
                || mBinding.edtPassword.text.isNullOrEmpty()
                || mBinding.edtConfPassword.text.isNullOrEmpty()
            ) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Please add all fields to register",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                registerUser()
            }
        }

        mBinding.imPassEye.setOnClickListener {
            if (isShown) {
                mBinding.imPassEye.setImageResource(R.drawable.ic_closed_eye)
                mBinding.edtPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                isShown = false
            } else {
                isShown = true
                mBinding.edtPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                mBinding.imPassEye.setImageResource(R.drawable.ic_open_eye)
            }

        }

        mBinding.imgConfPassEye.setOnClickListener {
            if (isShownConfPass) {
                mBinding.imgConfPassEye.setImageResource(R.drawable.ic_closed_eye)
                mBinding.edtConfPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                isShownConfPass = false
            } else {
                isShownConfPass = true
                mBinding.edtConfPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                mBinding.imgConfPassEye.setImageResource(R.drawable.ic_open_eye)
            }

        }
    }

    private fun registerUser() {
        firebaseController.registerUser(
            db,
            "usernames",
            auth,
            mBinding.edtEmailId.text.toString(),
            mBinding.edtPassword.text.toString(),
            mBinding.edtUsername.text.toString(),
            this
        ) {
            if (it) {
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }


}