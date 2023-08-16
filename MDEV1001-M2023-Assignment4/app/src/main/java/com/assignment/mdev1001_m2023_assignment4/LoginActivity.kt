package com.assignment.mdev1001_m2023_assignment4

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.assignment.mdev1001_m2023_assignment4.databinding.ActivityLoginBinding
import com.assignment.mdev1001_m2023_assignment4.extension.SharedPreferenceManager
import com.assignment.mdev1001_m2023_assignment4.firebase.FirebaseController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityLoginBinding
    var isShown = false
    val db = Firebase.firestore
    var firebaseController = FirebaseController()
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        auth = Firebase.auth
        if (SharedPreferenceManager.getString("ISLOGIN", "").equals("TRUE")) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        } else {

            mBinding.btnLogin.setOnClickListener {
                if (mBinding.edtUsername.text.isNullOrEmpty() || mBinding.edtPassword.text.isNullOrEmpty()) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Username or Password is missing",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    loginUser()
                }
            }

            mBinding.btnSignUp.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }

            mBinding.imgSignPass.setOnClickListener {
                if (isShown) {
                    mBinding.imgSignPass.setImageResource(R.drawable.ic_closed_eye)
                    mBinding.edtPassword.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    isShown = false
                } else {
                    isShown = true
                    mBinding.edtPassword.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    mBinding.imgSignPass.setImageResource(R.drawable.ic_open_eye)
                }

            }
        }
    }

    private fun loginUser() {
        firebaseController.loginUser(
            db,
            auth,
            mBinding.edtUsername.text.toString(),
            mBinding.edtPassword.text.toString(),
            this
        ) {
            if (it) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                SharedPreferenceManager.putString("ISLOGIN", "TRUE")
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}