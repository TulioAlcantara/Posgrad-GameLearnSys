package com.example.posgrad.fragments


import android.os.Bundle
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
import com.example.posgrad.data_class.ObjAprendizagem
import com.example.posgrad.recycler_adapters.Self2RecyclerAdapter
import com.example.posgrad.recycler_adapters.Self3RecyclerAdapter
import com.example.posgrad.self_collection
import kotlinx.android.synthetic.main.activity_main.*


class SelfService3Fragment : Fragment() {

    val self_atual = ArrayList<ObjAprendizagem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_self_service3, container, false)

        //Recycler Adapter set
        val self_recyclerview = rootView.findViewById(R.id.self3View) as RecyclerView
        self_recyclerview.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        self_recyclerview.adapter = Self3RecyclerAdapter(self_atual, activity)

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

        //Get bundle
        val bundle = arguments
        val nome = bundle?.getString("nome")

        for(element in self_collection){
            if(element.nome == nome){
                for(item in element.arrayObjAprendizagem){
                    self_atual.add(item)
                }
            }
        }

        super.onActivityCreated(savedInstanceState)
    }

    fun backButtonPressed(){
        (activity as MainActivity).backButtonVisible(0)
        (activity as MainActivity).fragmentTitle?.text = "Self-Service"
        (activity as MainActivity).navigation.visibility = View.VISIBLE
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.remove(SelfService3Fragment())
        fragmentTransaction?.replace(R.id.fragmentContainer, SelfServiceFragment())
        fragmentTransaction?.commit()

    }


}
