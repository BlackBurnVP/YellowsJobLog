package com.example.vitalii.yellowsjoblog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.vitalii.yellowsjoblog.api.ServerConnection

class MainActivity : AppCompatActivity() {

    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mNavView:NavigationView
    private lateinit var mNavController: NavController

    private lateinit var sp: SharedPreferences
    private lateinit var ed: SharedPreferences.Editor

    private val connect = ServerConnection()

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
        ed = sp.edit()

        mDrawerLayout = findViewById(R.id.drawer_layout)
        mNavView = findViewById(R.id.nav_view)
        mNavController = this.findNavController(R.id.myNavHostFragment)
        val headerView = mNavView.getHeaderView(0)
        val text:TextView = headerView.findViewById(R.id.txt_UserName)

        text.text = sp.getString("EMAIL","")

        NavigationUI.setupActionBarWithNavController(this,mNavController,mDrawerLayout)
        NavigationUI.setupWithNavController(mNavView,mNavController)

        println(sp.getBoolean("logged",false))
        println(sp.getString("EMAIL","email is empty"))
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(mDrawerLayout,mNavController)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        loseFocus()
        return super.dispatchTouchEvent(ev)
    }

    fun loseFocus(){
        if (currentFocus != null){
            val inputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
            currentFocus!!.clearFocus()
        }
    }

}
