package com.example.vitalii.yellowsjoblog

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.vitalii.yellowsjoblog.api.JobLogService
import com.example.vitalii.yellowsjoblog.api.ServerConnection
import com.example.vitalii.yellowsjoblog.api.Token
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class
LoginActivity : AppCompatActivity() {

    private lateinit var txtLogin:EditText
    private lateinit var txtPassword:EditText
    lateinit var email:String
    private lateinit var pass:String
    private lateinit var sp:SharedPreferences
    private lateinit var ed:SharedPreferences.Editor

    private val connect = ServerConnection()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
        ed = sp.edit()
        txtLogin = findViewById(R.id.txt_login)
        txtPassword = findViewById(R.id.txt_pass)
//        email = txtLogin.text.toString()
//        pass = txtPassword.text.toString()
//        ed.putBoolean("logged",false)
        ed.putBoolean("logged",false)
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
            sendLogin("vitalii.pshenychniuk@gmail.com","123415z")
            email = txtLogin.text.toString()
            pass = txtPassword.text.toString()
            //ed.putBoolean("logged",true)
            ed.putString("EMAIL",email).apply()

//        }
    }

    private fun sendLogin(username:String, password:String){
        connect.createService(username,password).basicLogin()
            .enqueue(object : Callback<Token> {
                override fun onResponse(call: Call<Token>, response: Response<Token>) {
                    Toast.makeText(this@LoginActivity,"Successful response", Toast.LENGTH_SHORT).show()
                    println("Response IS ${response.body()!!.token}")
                    val token = response.body()!!.token
                    val currentUser = response.body()!!.username
                    ed.putString("currentUser",currentUser).apply()
                    ed.putString("token",token).apply()
                    ed.putBoolean("logged",true)
                    goToMain()
                    finish()
                    if (response.body() == null){
                        Toast.makeText(this@LoginActivity,"Answer is null", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@LoginActivity,"Answer not null", Toast.LENGTH_SHORT).show()
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
        //startActivity(intent)
    }
}
