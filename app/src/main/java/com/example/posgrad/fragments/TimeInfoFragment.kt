package com.example.posgrad.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.posgrad.MainActivity

import com.example.posgrad.R
import com.example.posgrad.data_class.Membro
import com.example.posgrad.membros_collection
import com.example.posgrad.recycler_adapters.TimesRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_time_info.*

class TimeInfoFragment : Fragment() {

    val membros_locais = ArrayList<Membro>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_time_info, container, false)

        //Recycler Adapter set
        val rectangle_recyclerview = rootView.findViewById(R.id.rectangleView) as RecyclerView
        rectangle_recyclerview.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        rectangle_recyclerview.adapter = TimesRecyclerAdapter(membros_locais, activity)

        membros_locais.clear()

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        //Recupero o nome do time clicado e obtenho os membros relacionados ao mesmo
        val bundle = arguments
        val time_local = bundle?.getString("nome_time")
        for(membro in membros_collection){

            if(membro.time_string == time_local){
                Log.d("agumon", membro.toString())
                membros_locais.add(membro)
            }
        }

        //Seto os elementos da UI
        (activity as MainActivity).fragmentTitle?.text = time_local
        (activity as MainActivity).backButtonVisible(1)

        //Função do backButton
        (activity as MainActivity).backButton.setOnClickListener {
            backButtonPressed()
        }

        rectangleView.adapter?.notifyDataSetChanged()
        super.onActivityCreated(savedInstanceState)
    }


    fun backButtonPressed(){
        (activity as MainActivity).backButtonVisible(0)
        (activity as MainActivity).fragmentTitle?.text = "Times"
        (activity as MainActivity).navigation.visibility = View.VISIBLE
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.remove(TimeInfoFragment())
        fragmentTransaction?.replace(R.id.fragmentContainer, TimesFragment())
        fragmentTransaction?.commit()
    }
}
