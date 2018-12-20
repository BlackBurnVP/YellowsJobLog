package com.example.vitalii.yellowsjoblog

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.EditText

class LoginActivity : AppCompatActivity() {

    private lateinit var txtLogin:EditText
    private lateinit var txtPassword:EditText
    private val KEY_EMAIL:String = ""
    private val KEY_PASSWORD:String = ""
    private val LOGGED:String = ""
    lateinit var email:String
    lateinit var pass:String
    lateinit var sp:SharedPreferences
    lateinit var ed:SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sp = getSharedPreferences("Email", Context.MODE_PRIVATE)
        ed = sp.edit()
        txtLogin = findViewById(R.id.txt_login)
        txtPassword = findViewById(R.id.txt_pass)
        email = txtLogin.text.toString()
        pass = txtPassword.text.toString()
        if(sp.getBoolean("logged",false)){
            goToMain()
        }

    }

    fun isEmail(editText:EditText): Boolean {
        val email:CharSequence = editText.text.toString()
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches())
    }
    fun isEmpty(editText: EditText):Boolean{
        val str:CharSequence = editText.text.toString()
        return str.isEmpty()
    }

    fun onClick(View: View){
        sp = getSharedPreferences("Email", Context.MODE_PRIVATE)
        ed = sp.edit()
//        if(!isEmpty(txtLogin)){
//            txtLogin.error = "Login is required"
//        };else if (!isEmail(txtLogin)){
//            txtLogin.error = "Enter valid Email"
//        };else if(!isEmpty(txtPassword))
//        {
//            txtPassword.error = "Password is required"
//        }; else{
            ed.putBoolean("logged",true)
            ed.putString(KEY_EMAIL,email).apply()
            ed.putString(KEY_PASSWORD,pass).apply()
            goToMain()
//        }
    }

    fun goToMain(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}
