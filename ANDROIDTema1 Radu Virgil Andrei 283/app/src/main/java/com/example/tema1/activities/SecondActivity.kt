package com.example.tema1.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tema1.R
import com.example.tema1.fragments.SecondActivityFirstFragment

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)
        openFirstFragment()

    }

    private fun openFirstFragment() {
        val firstFragment = SecondActivityFirstFragment.newInstance()
            supportFragmentManager.beginTransaction()
            .add(R.id.second_activity,firstFragment,"second_activity_first_fragment")
            .addToBackStack(SecondActivityFirstFragment.toString())
            .commit()
    }


}
