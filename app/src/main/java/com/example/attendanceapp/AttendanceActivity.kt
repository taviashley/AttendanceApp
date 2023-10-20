package com.example.attendanceapp

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.attendanceapp.databinding.ActivityMainBinding

class AttendanceActivity : AppCompatActivity() {

    private lateinit var navigationController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)


        //Defines CourseActivity as the navHost
        navigationController = findNavController(R.id.main_activity)
        NavigationUI.setupActionBarWithNavController(this, navigationController)


        supportActionBar?.hide()

    }
}