package com.example.posgrad.adapters

import android.content.Context
import androidx.core.view.isVisible
import com.example.posgrad.R
import com.example.posgrad.data_class.TimePontuacao
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.layout_chart.view.*

val missao_nome = ArrayList<String>()

class ChartSet(){

    //Formatter dos valores do eixo X
    class MyxAxisFormatter : ValueFormatter(){
        override fun getFormattedValue(value: Float): String {
            return missao_nome[value.toInt()]
        }
    }

    fun setBarChart(barChart : BarChart, lineChart : LineChart, time_pontuacao : TimePontuacao, button : FloatingActionButton){
        lineChart.isVisible = false
        barChart.isVisible = true

        val barEntries = ArrayList<BarEntry>() // Array com os pontos
        var x = 0

        //Adiciono a pontuação de cada missão no meu array
        for(missao in time_pontuacao.pontuacao.orEmpty()){
            missao_nome.add(missao.key?.substring(7).orEmpty()) //Removo a palavra 'Missão'
            val entry : BarEntry = BarEntry(x.toFloat(), missao.value!!.toFloat())
            barEntries.add(entry)
            x = x+1
        }

        val dataset = BarDataSet(barEntries, "bar")
        dataset.color = R.color.darkGray

        val data = BarData(dataset)
        data.setValueTextSize(12.toFloat())

        //Propriedades gerais do chart
        barChart.data = data
        barChart.invalidate()
        barChart.setFitBars(true)
        barChart.setScaleEnabled(false)
        barChart.isHighlightPerDragEnabled = false
        barChart.isHighlightPerTapEnabled = false
        barChart.extraBottomOffset = 5.toFloat()
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.animateY(500)

        //Propriedades do xAxis
        barChart.xAxis.valueFormatter = MyxAxisFormatter()
        barChart.xAxis.textSize = 12.toFloat()
        barChart.xAxis.granularity = 1f
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        //Propriedades do leftAxis
        barChart.axisRight.isEnabled = false

        //Propriedades do rightAxis
        barChart.axisLeft.isEnabled = true
        barChart.axisLeft.axisMinimum = 0.toFloat()
        barChart.axisLeft.axisMaximum = 120.toFloat()

        //SET FAB LISTENER ---------------------------------------------------------------------
        button.setOnClickListener {
            setLineChart(barChart, lineChart, time_pontuacao, button)
        }
        //--------------------------------------------------------------------------------------
    }

    fun setLineChart(barChart : BarChart, lineChart : LineChart, time_pontuacao : TimePontuacao, button : FloatingActionButton){
        lineChart.isVisible = true
        barChart.isVisible = false

        val lineEntries = ArrayList<Entry>() // Array com os pontos
        var x = 0

        //Adiciono a pontuação de cada missão no meu array
        for(missao in time_pontuacao.pontuacao.orEmpty()){
            missao_nome.add(missao.key?.substring(7).orEmpty()) //Removo a palavra 'Missão'
            val entry : BarEntry = BarEntry(x.toFloat(), missao.value!!.toFloat())
            lineEntries.add(entry)
            x = x+1
        }

        val dataset : LineDataSet = LineDataSet(lineEntries, "line")
        dataset.setDrawValues(false)
        dataset.color = R.color.darkGray



        val data = LineData(dataset)
        data.setValueTextSize(12.toFloat())

        //Propriedades gerais do chart
        lineChart.data = data
        lineChart.invalidate()
        lineChart.isHighlightPerDragEnabled = false
        lineChart.isHighlightPerTapEnabled = false
        lineChart.description.isEnabled = false
        lineChart.extraBottomOffset = 5.toFloat()
        lineChart.extraRightOffset = 22.toFloat()
        lineChart.legend.isEnabled = false
        lineChart.setScaleEnabled(false)
        lineChart.animateY(500)

        //Propriedades do xAxis
        lineChart.xAxis.valueFormatter = MyxAxisFormatter()
        lineChart.xAxis.textSize = 12.toFloat()
        lineChart.xAxis.granularity = 1f
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        //Propriedades do leftAxis
        lineChart.axisRight.isEnabled = false

        //Propriedades do rightAxis
        lineChart.axisLeft.isEnabled = true
        lineChart.axisLeft.axisMinimum = 0.toFloat()
        lineChart.axisLeft.axisMaximum = 120.toFloat()

        //SET FAB LISTENER ---------------------------------------------------------------------
        button.setOnClickListener {
            setBarChart(barChart, lineChart, time_pontuacao, button)
        }
        //--------------------------------------------------------------------------------------
    }

}



//Mudo a cor de cada barra
//val gray = ContextCompat.getColor(ChartAdapter, android.R.color.darker_gray)
//val colors : ArrayList<Int> = arrayListOf(404040, 404040, 404040, 404040)
//dataset.setColors(colors)
