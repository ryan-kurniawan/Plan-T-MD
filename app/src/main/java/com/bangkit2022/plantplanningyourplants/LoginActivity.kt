package com.bangkit2022.plantplanningyourplants

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bangkit2022.plantplanningyourplants.databinding.LoginActivityBinding

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private var _loginActivityBinding : LoginActivityBinding? = null
    private val loginActivityBinding get() = _loginActivityBinding!!

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        _loginActivityBinding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(loginActivityBinding.root)

        viewSet()
    }

    private fun viewSet() {
        with(loginActivityBinding) {
            btnRegisterLogin.setOnClickListener(this@LoginActivity)
            btnLogin.setOnClickListener(this@LoginActivity)
        }
    }

    override fun onClick(view : View?) {
        when (view) {
            loginActivityBinding.btnLogin -> startActivity(Intent(this, MainActivity::class.java))
            loginActivityBinding.btnRegisterLogin -> startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}