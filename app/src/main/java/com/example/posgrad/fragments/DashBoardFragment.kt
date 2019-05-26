package com.example.posgrad.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.posgrad.*
import com.example.posgrad.adapters.*
import com.example.posgrad.recycler_adapters.ChartRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dashboard.*


//val chart_recyclerView : RecyclerView

class DashBoardFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_dashboard, container, false)

        //Recycler Adapter set
        val chart_recyclerView = rootView.findViewById(R.id.chartView) as RecyclerView
        chart_recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        chart_recyclerView.adapter = ChartRecyclerAdapter(pontuacao_collection, activity)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val chartSet = ChartSet()

        (activity as MainActivity).fragmentTitle.text = ("Dashboard")

        for(element in pontuacao_collection){
            Log.d("pontuacao_collection", element.nome + ": " + element.pontuacao?.values)
        }

        //Set main Chart
        timeNomeMain.text = timePontuacaoMain.nome
        chartSet.setBarChart(barChartMain, lineChartMain, timePontuacaoMain, changeChartMain, 1, activity)

        //chartView.adapter?.notifyDataSetChanged()

        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        //chartView.adapter = ChartAdapter(pontuacao_collection, activity!!.applicationContext)
        super.onResume()
    }
}