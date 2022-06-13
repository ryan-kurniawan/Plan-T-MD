package com.bangkit2022.plantplanningyourplants

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bangkit2022.plantplanningyourplants.databinding.RegisterActivityBinding

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private var _registActivityBinding : RegisterActivityBinding? = null
    private val registActivityBinding get() = _registActivityBinding!!

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        _registActivityBinding = RegisterActivityBinding.inflate(layoutInflater)
        setContentView(registActivityBinding.root)

        viewSet()
    }

    private fun viewSet() {
        with(registActivityBinding) {
            btnRegister.setOnClickListener(this@RegisterActivity)
            imgBackRegister.setOnClickListener(this@RegisterActivity)
        }
    }

    override fun onClick(view : View?) {
        when (view) {
            registActivityBinding.btnRegister -> startActivity(Intent(this, LoginActivity::class.java))
            registActivityBinding.imgBackRegister -> startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}