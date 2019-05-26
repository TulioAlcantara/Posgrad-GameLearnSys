package com.example.posgrad.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.posgrad.MainActivity

import com.example.posgrad.R
import com.example.posgrad.recycler_adapters.MembrosRecyclerAdapter
import com.example.posgrad.time_collection
import kotlinx.android.synthetic.main.fragment_times.*


class TimesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_times, container, false)

        //Recycler Adapter set
        val square_recyclerview = rootView.findViewById(R.id.squareView) as RecyclerView
        square_recyclerview.layoutManager = GridLayoutManager(activity, 2)
        square_recyclerview.adapter = MembrosRecyclerAdapter(time_collection, activity)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        //Widget superior direito invisível
        (activity as MainActivity).backButtonVisible(-1)

        squareView.adapter?.notifyDataSetChanged()
        super.onActivityCreated(savedInstanceState)
    }
}
