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

class
LoginActivity : AppCompatActivity() {

    private lateinit var txtLogin:EditText
    private lateinit var txtPassword:EditText
    private val KEY_EMAIL:String = ""
    private val KEY_PASSWORD:String = ""
    lateinit var email:String
    private lateinit var pass:String
    private lateinit var sp:SharedPreferences
    private lateinit var ed:SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
        ed = sp.edit()
        txtLogin = findViewById(R.id.txt_login)
        txtPassword = findViewById(R.id.txt_pass)
        email = txtLogin.text.toString()
        pass = txtPassword.text.toString()
        if(sp.getBoolean("logged",false)){
            goToMain()
        }
    }

    /**
     * Checking if entered text is Email
     * @param editText Text Box with email
     */
    fun isEmail(editText:EditText): Boolean {
        val email:CharSequence = editText.text.toString()
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches())
    }

    /**
     * Checking if Text Box not empty
     * @param editText Text Box for checking
     */
    fun isEmpty(editText: EditText):Boolean{
        val str:CharSequence = editText.text.toString()
        return str.isEmpty()
    }

    fun onClick(View: View){
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
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
            ed.putString("EMAIL",email).commit()
            ed.putString(KEY_PASSWORD,pass).apply()
            goToMain()
//        }
    }

    /**
     * Redirection to Main Activity
     */
    private fun goToMain(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}
