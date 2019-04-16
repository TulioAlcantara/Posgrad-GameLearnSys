package com.example.posgrad.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.example.posgrad.R
import com.example.posgrad.data_class.TimePontuacao
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import kotlinx.android.synthetic.main.fragment_self.*
import kotlinx.android.synthetic.main.layout_chart.view.*

class ChartAdapter(val time_pontuacao : ArrayList<TimePontuacao>)  : RecyclerView.Adapter<ChartAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ChartAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_chart, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return time_pontuacao.size
    }

    override fun onBindViewHolder(holder: ChartAdapter.ViewHolder, position: Int) {
        holder.nome.text = time_pontuacao.get(position).nome

        val entries = ArrayList<BarEntry>() // Array com os pontos
        var x = 0
        //Adiciono a pontuação de cada missão no meu array
        for(missao in time_pontuacao.get(position).pontuacao.orEmpty()){
            val entry : BarEntry = BarEntry(x.toFloat(), missao.value!!.toFloat())
            entries.add(entry)
            x = x+1
        }

        val dataset : BarDataSet = BarDataSet(entries, "teste")
        val data : BarData = BarData(dataset)

        //Propriedades do chart
        holder.chart.data = data
        holder.chart.invalidate()
        holder.chart.setFitBars(true)

    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val chart = itemView.chart
        val nome = itemView.timeNome
    }
}