package com.example.tema1.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tema1.R
import kotlinx.android.synthetic.main.second_activity_first_fragment.*

class SecondActivityFirstFragment : Fragment() {
    companion object {
        fun newInstance() = SecondActivityFirstFragment();
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.second_activity_first_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_open_menu.setOnClickListener {
            addSecondFragment();
        }
    }

    private fun addSecondFragment() {
        val secondFragment = SecondActivitySecondFragment.newInstance()
        activity!!.supportFragmentManager
            .beginTransaction()
            .add(R.id.second_activity,secondFragment,"second_activity_second_fragment")
            .addToBackStack(SecondActivitySecondFragment.toString())
            .commit()


    }
}