package com.example.tema1.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tema1.R
import com.example.tema1.activities.FirstActivity
import kotlinx.android.synthetic.main.second_activity_second_fragment.*

class SecondActivitySecondFragment : Fragment() {
    companion object {
        fun newInstance()=SecondActivitySecondFragment();
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.second_activity_second_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_one.setOnClickListener{
            replaceFragment();
        }
        btn_two.setOnClickListener{
            removeLeftFragment();
        }
        btn_three.setOnClickListener {

            closeActivity();
        }
    }

    private fun closeActivity() {
        startActivity(Intent(activity,FirstActivity::class.java))
        activity!!.finish()
    }

    private fun removeLeftFragment() {
       activity!!.supportFragmentManager
           .beginTransaction()
           .remove(fragmentManager!!.findFragmentByTag("second_activity_first_fragment")!!)
           .commit()
    }

    private fun replaceFragment() {
        val thirdFragment = SecondActivityThirdFragment.newInstance()
        activity!!.supportFragmentManager
            .beginTransaction()
            .replace(R.id.second_activity,thirdFragment)
            .addToBackStack(SecondActivityThirdFragment.toString())
            .commit()

    }
}