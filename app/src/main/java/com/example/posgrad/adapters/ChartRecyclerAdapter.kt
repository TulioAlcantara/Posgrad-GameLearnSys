package com.example.posgrad.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.posgrad.MainActivity
//import com.example.posgrad.R
import com.example.posgrad.data_class.TimePontuacao
import kotlinx.android.synthetic.main.layout_chart.view.*


var chartType : Int = 0
val chartSet = ChartSet()

class ChartAdapter(val time_pontuacao : ArrayList<TimePontuacao>, val context: FragmentActivity?)  : RecyclerView.Adapter<ChartAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ChartAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(com.example.posgrad.R.layout.layout_chart, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return time_pontuacao.size
    }

    override fun onBindViewHolder(holder: ChartAdapter.ViewHolder, position: Int) {
        holder.nome.text = time_pontuacao.get(position).nome
        chartSet.setBarChart(holder.barChart, holder.lineChart, time_pontuacao.get(position), holder.button, 0, context)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val barChart = itemView.barChart
        val lineChart = itemView.lineChart
        val nome = itemView.timeNome
        val button = itemView.changeChart
    }


}