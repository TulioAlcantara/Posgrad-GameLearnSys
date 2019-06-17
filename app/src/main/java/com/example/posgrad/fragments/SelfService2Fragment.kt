package com.example.posgrad.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.posgrad.MainActivity
import com.example.posgrad.R
import com.example.posgrad.data_class.Self
import com.example.posgrad.recycler_adapters.Self1RecyclerAdapter
import com.example.posgrad.recycler_adapters.Self2RecyclerAdapter
import com.example.posgrad.self_collection_dss
import com.example.posgrad.self_collection_espgti
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_self_service2.*

val self_atual = ArrayList<Self>()

class SelfService2Fragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_self_service2, container, false)

        //Recycler Adapter set
        val self_recyclerview = rootView.findViewById(R.id.self2View) as RecyclerView
        self_recyclerview.layoutManager = GridLayoutManager(activity, 2)
        self_recyclerview.adapter = Self2RecyclerAdapter(self_atual, activity)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        //Back Button visivel
        (activity as MainActivity).backButtonVisible(1)
        (activity as MainActivity).backButton.setOnClickListener {
            backButtonPressed()
        }

        //Navigation bar invisivel
        (activity as MainActivity).navigation.visibility = View.GONE

        self_atual.clear()

        val bundle = arguments
        val ciclo = bundle?.getString("ciclo")
        val enfase = bundle?.getString("enfase")
        val formacao = bundle?.getString("formacao")

        if(formacao == "DSS-BI"){
            for(item in self_collection_dss){
                if(item.ciclo == ciclo && item.enfase == enfase){
                    self_atual.add(item)
                }
            }
        }

        else if(formacao == "ESPGTI"){
            for(item in self_collection_espgti){
                if(item.ciclo == ciclo && item.enfase == enfase){
                    self_atual.add(item)
                }
            }
        }

        //Log.d("self_atual", self_atual.toString())

        self2View.adapter?.notifyDataSetChanged()

        super.onActivityCreated(savedInstanceState)
    }

    fun backButtonPressed(){
        (activity as MainActivity).backButtonVisible(0)
        (activity as MainActivity).fragmentTitle?.text = "Self-Service"
        (activity as MainActivity).navigation.visibility = View.VISIBLE
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.fragmentContainer, SelfServiceFragment())
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()
    }
}
