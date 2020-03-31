package com.example.tema1.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.tema1.R
import com.example.tema1.activities.SecondActivity
import kotlinx.android.synthetic.main.first_activity_static_fragment.*

class FirstActivityStaticFragment : Fragment() {

    companion object {
        fun newInstance() = FirstActivityStaticFragment();
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.first_activity_static_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_to_second_activity.setOnClickListener {
            startActivity(Intent(activity, SecondActivity::class.java))
            activity!!.finish()

        }
    }


}
