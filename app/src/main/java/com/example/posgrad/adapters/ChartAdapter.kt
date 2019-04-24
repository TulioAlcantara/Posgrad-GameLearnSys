package com.example.posgrad.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.example.posgrad.R
import com.example.posgrad.data_class.TimePontuacao
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
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

        //SET CHART -----------------------------------------------------------------------------
        val entries = ArrayList<BarEntry>() // Array com os pontos
        var x = 0

        //Adiciono a pontuação de cada missão no meu array
        for(missao in time_pontuacao.get(position).pontuacao.orEmpty()){
            val entry : BarEntry = BarEntry(x.toFloat(), missao.value!!.toFloat())
            entries.add(entry)
            x = x+1
        }
        val dataset : BarDataSet = BarDataSet(entries, "teste")

        //Mudo a cor de cada barra
        //val gray = ContextCompat.getColor(ChartAdapter, android.R.color.darker_gray)
        //val colors : ArrayList<Int> = arrayListOf(404040, 404040, 404040, 404040)
        //dataset.setColors(colors)

        val data : BarData = BarData(dataset)
        data.setValueTextSize(12.toFloat())

        //Propriedades gerais do chart
        holder.chart.data = data
        holder.chart.invalidate()
        holder.chart.setFitBars(true)
        holder.chart.description.isEnabled = false
        holder.chart.legend.isEnabled = false
        holder.chart.animateY(1000)

        //Propriedades do xAxis
        holder.chart.xAxis.valueFormatter = MyxAxisFormatter()
        holder.chart.xAxis.granularity = 1f
        holder.chart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        //Propriedades do leftAxis
        holder.chart.axisRight.isEnabled = false

        //Propriedades do rightAxis
        holder.chart.axisLeft.isEnabled = true
        holder.chart.axisLeft.axisMinimum = 0.toFloat()
        holder.chart.axisLeft.axisMaximum = 120.toFloat()
        //--------------------------------------------------------------------------------------

        //SET FAB LISTENER ---------------------------------------------------------------------
        //--------------------------------------------------------------------------------------
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val chart = itemView.chart
        val nome = itemView.timeNome
    }

    class setChart(){

    }

    //Formatter dos valores do eixo X
    class MyxAxisFormatter : ValueFormatter(){
        val missao_nome: ArrayList<String> = arrayListOf("Discovery", "Startup", "Passport", "Curiosity")
        override fun getFormattedValue(value: Float, axis: AxisBase?): String {
            return missao_nome[value.toInt()]
        }
    }
}