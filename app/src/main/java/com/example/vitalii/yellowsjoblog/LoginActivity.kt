package com.example.vitalii.yellowsjoblog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.example.vitalii.yellowsjoblog.api.ServerConnection
import com.example.vitalii.yellowsjoblog.api.Token
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class
LoginActivity : AppCompatActivity() {

    private lateinit var txtLogin:EditText
    private lateinit var txtPassword:EditText
    private lateinit var email:String
    private lateinit var pass:String
    private lateinit var sp:SharedPreferences
    private lateinit var ed:SharedPreferences.Editor

    private val connect = ServerConnection()

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
        ed = sp.edit()
        txtLogin = findViewById(R.id.txt_login)
        txtPassword = findViewById(R.id.txt_pass)
        if(sp.getBoolean("logged",false)){
            goToMain()
        }
        val pInfo = packageManager.getPackageInfo(packageName, 0)
        txtVersion.text = pInfo.versionName
        if (pInfo.versionName.contains("test")){
            btn_login.setOnClickListener(testClick)
        }else{
            btn_login.setOnClickListener(onClick)
        }
    }

    /**
     * Checking if entered text is Email
     * @param editText Text Box with email
     */
    private fun isEmail(editText:EditText): Boolean {
        val email:CharSequence = editText.text.toString()
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches())
    }

    /**
     * Checking if Text Box not empty
     * @param editText Text Box for checking
     */
    private fun isEmpty(editText: EditText):Boolean{
        val str:CharSequence = editText.text.toString()
        return str.isEmpty()
    }

    val onClick = View.OnClickListener{
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
        ed = sp.edit()
        if(isEmpty(txtLogin)){
            txtLogin.error = "Login is required"
        };else if (isEmpty(txtPassword)){
            txtPassword.error = "Password is required"
        };else if(!isEmail(txtLogin)){
            txtLogin.error = "Enter valid Email"
        }; else{
            email = txtLogin.text.toString()
            pass = txtPassword.text.toString()
            sendLogin(email,pass)
            ed.putString("EMAIL",email).apply()
        }
    }

    private val testClick = View.OnClickListener{
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
        ed = sp.edit()
        email = txtLogin.text.toString()
        pass = txtPassword.text.toString()
        sendLogin("","")
        ed.putString("EMAIL",email).apply()
    }

    /**
     * Basic Authentication to server
     * @param username string email
     * @param password string password
     */
    private fun sendLogin(username:String, password:String){
        connect.createService(username,password).basicLogin()
            .enqueue(object : Callback<Token> {
                override fun onResponse(call: Call<Token>, response: Response<Token>) {
                    if (response.body() != null){
                        ed.putString("currentUser",response.body()!!.id).apply()
                        ed.putString("currentUserName",response.body()!!.username).apply()
                        ed.putString("token",response.body()!!.token).apply()
                        ed.putBoolean("logged",true).apply()
//                        finish()
                        goToMain()
                    }else{
                        Toast.makeText(this@LoginActivity,"Answer is null", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Token>, t: Throwable) {
                    Toast.makeText(this@LoginActivity,"ERROR", Toast.LENGTH_SHORT).show()
                }
            })
    }

    /**
     * Redirection to Main Activity
     */
    private fun goToMain(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        loseFocus()
        return super.dispatchTouchEvent(ev)
    }

    private fun loseFocus(){
        if (currentFocus != null){
            val inputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
            currentFocus!!.clearFocus()
        }
    }
}
