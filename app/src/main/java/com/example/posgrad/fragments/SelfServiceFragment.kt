package com.example.posgrad.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.posgrad.MainActivity
import com.example.posgrad.R
import com.example.posgrad.recycler_adapters.Self1RecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_self_service1.*

val self_ciclo = arrayOf("Ciclo 1", "Ciclo 2")
val spinner_items = arrayOf("DSS-BI", "ESPGTI")

class SelfServiceFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_self_service1, container, false)

        //Recycler Adapter set
        val self_recyclerview = rootView.findViewById(R.id.selfDssView) as RecyclerView
        self_recyclerview.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        self_recyclerview.adapter = Self1RecyclerAdapter(self_ciclo, "DSS-BI",  activity)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        selfText.text = "DSS-BI"

        //Fitlro Visível
        (activity as MainActivity).backButtonVisible(0)

        //Escondo o botão de perfil
        (activity as MainActivity).userAvatar.visibility = View.INVISIBLE
        super.onActivityCreated(savedInstanceState)

        //Spinner Adapter
        (activity as MainActivity).spinner.adapter = ArrayAdapter(
            activity, // Context
            android.R.layout.simple_spinner_dropdown_item, // Layout
            spinner_items // Array
        )

        //Spinner Click
        (activity as MainActivity).spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                (view as TextView).setText(null)
                selfDssView.adapter = Self1RecyclerAdapter(self_ciclo, spinner_items.get(position),  activity)
                selfDssView.adapter?.notifyDataSetChanged()
                selfText.text = spinner_items.get(position)
            }
        }
    }


}
