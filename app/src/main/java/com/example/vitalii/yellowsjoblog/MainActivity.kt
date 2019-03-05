package com.example.vitalii.yellowsjoblog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.vitalii.yellowsjoblog.api.Reports
import com.example.vitalii.yellowsjoblog.api.ServerConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mNavView:NavigationView
    private lateinit var mNavController: NavController

    private lateinit var sp: SharedPreferences
    private lateinit var ed: SharedPreferences.Editor

    @SuppressLint("NewApi", "CommitPrefEdits")
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

        text.text = sp.getString("currentUserName","")

        NavigationUI.setupActionBarWithNavController(this,mNavController,mDrawerLayout)
        NavigationUI.setupWithNavController(mNavView,mNavController)

        //mNavController.addOnNavigatedListener{nc:NavController, nd:NavDestination}
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
