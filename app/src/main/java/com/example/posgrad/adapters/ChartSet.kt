package com.example.posgrad.adapters

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import com.example.posgrad.R
import com.example.posgrad.data_class.TimePontuacao
import com.example.posgrad.fragments.MissaoInfoFragment
import com.example.posgrad.missao_collection
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.floatingactionbutton.FloatingActionButton


val missao_nome = ArrayList<String>()

class ChartSet(){

    //Formatter dos valores do eixo X
    class MyxAxisFormatter : ValueFormatter(){
        override fun getFormattedValue(value: Float): String {
            return missao_nome[value.toInt()]
        }
    }

    fun setBarChart(barChart : BarChart, lineChart : LineChart, time_pontuacao : TimePontuacao, button : FloatingActionButton, isMain : Int, context: FragmentActivity?){
        lineChart.isVisible = false
        barChart.isVisible = true

        val selector = ChartSelection(context, time_pontuacao) //Objeto Listener de cada item do chart

        button.setImageResource(R.drawable.ic_chart_line) //Imagem do FAB

        val barEntries = ArrayList<BarEntry>() // Array com os pontos
        var x = 0

        //Adiciono a pontuação de cada missão no meu array
        for(missao in time_pontuacao.pontuacao.orEmpty()){
            missao_nome.add(missao.key?.substring(7).orEmpty()) //Removo a palavra 'Missão'
            val entry  = BarEntry(x.toFloat(), missao.value!!.toFloat())
            barEntries.add(entry)
            x = x+1
        }

        //Propriedades do DataSet
        val dataset = BarDataSet(barEntries, "bar")
        dataset.color = Color.DKGRAY

        //Propriedades do Data (conjunto de DataSets)
        val data = BarData(dataset)
        data.setValueTextSize(14.toFloat())
        data.setValueTypeface(Typeface.DEFAULT_BOLD)

        //Propriedades gerais do chart
        barChart.data = data
        barChart.setTouchEnabled(true)
        //barChart.invalidate()
        barChart.setFitBars(true)
        barChart.setScaleEnabled(false)
        barChart.isHighlightPerDragEnabled = false
        //barChart.isHighlightPerTapEnabled = false
        barChart.extraBottomOffset = 5.toFloat()
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.setOnChartValueSelectedListener(selector)
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

        //Propriedades do chart Main (time do usuários)
        if(isMain==1){
            barChart.xAxis.textColor = Color.WHITE
            barChart.axisLeft.textColor = Color.WHITE
            dataset.color = Color.WHITE
            data.setValueTextColor(Color.WHITE)
        }

        //SET FAB LISTENER ---------------------------------------------------------------------
        button.setOnClickListener {
            setLineChart(barChart, lineChart, time_pontuacao, button, isMain, context)
        }
        //--------------------------------------------------------------------------------------
    }

    fun setLineChart(barChart : BarChart, lineChart : LineChart, time_pontuacao : TimePontuacao, button : FloatingActionButton, isMain: Int, context: FragmentActivity?){
        lineChart.isVisible = true
        barChart.isVisible = false

        val selector = ChartSelection(context, time_pontuacao) //Objeto Listener de cada item do chart

        button.setImageResource(R.drawable.ic_chart_bar) //Imagem do FAB

        val lineEntries = ArrayList<Entry>() // Array com os pontos
        var x = 0

        //Adiciono a pontuação de cada missão no meu array
        for(missao in time_pontuacao.pontuacao.orEmpty()){
            missao_nome.add(missao.key?.substring(7).orEmpty()) //Removo a palavra 'Missão'
            val entry : BarEntry = BarEntry(x.toFloat(), missao.value!!.toFloat())
            lineEntries.add(entry)
            x = x+1
        }

        //Propriedades do DataSet
        val dataset : LineDataSet = LineDataSet(lineEntries, "line")
        dataset.color = Color.DKGRAY
        dataset.setGradientColor(Color.DKGRAY, Color.WHITE)

        //Propriedades do Data (conjunto de DataSets)
        val data = LineData(dataset)
        data.setValueTextSize(14.toFloat())
        data.setValueTypeface(Typeface.DEFAULT_BOLD)

        //Propriedades gerais do chart
        lineChart.data = data
        lineChart.invalidate()
        lineChart.setScaleEnabled(false)
        lineChart.legend.isEnabled = false
        //lineChart.isHighlightPerDragEnabled = false
        lineChart.isHighlightPerTapEnabled = false
        lineChart.setTouchEnabled(true)
        lineChart.description.isEnabled = false
        lineChart.extraBottomOffset = 5.toFloat()
        lineChart.extraRightOffset = 35.toFloat()
        lineChart.extraLeftOffset = 35.toFloat()
        lineChart.setOnChartValueSelectedListener(selector)
        lineChart.animateY(500)

        //Propriedades do xAxis
        lineChart.xAxis.valueFormatter = MyxAxisFormatter()
        lineChart.xAxis.textSize = 12.toFloat()
        lineChart.xAxis.granularity = 1f
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        //Propriedades do leftAxis
        lineChart.axisRight.isEnabled = false

        //Propriedades do rightAxis
        lineChart.axisLeft.isEnabled = false
        /*
        lineChart.axisLeft.axisMinimum = 0.toFloat()
        lineChart.axisLeft.axisMaximum = 120.toFloat()
        */

        //Propriedades do chart Main (time do usuários)
        if(isMain==1){
            lineChart.xAxis.textColor = Color.WHITE
            lineChart.axisLeft.textColor = Color.WHITE
            dataset.color = Color.WHITE
            data.setValueTextColor(Color.WHITE)
        }

        //SET FAB LISTENER ---------------------------------------------------------------------
        button.setOnClickListener {
            setBarChart(barChart, lineChart, time_pontuacao, button, isMain, context)
        }
        //--------------------------------------------------------------------------------------
    }


    //Listener para cada valor do chart
    class ChartSelection(val context: FragmentActivity?, val time_pontuacao: TimePontuacao) : OnChartValueSelectedListener{

        override fun onValueSelected(e: Entry?, h: Highlight?) {

            val fragmento = MissaoInfoFragment()

            //Argumentos que serão recebidos no fragmento MissaoInfoFragment
            val bundle = Bundle()
            val missaoNome = missao_nome[e?.x!!.toInt()]
            val pontuacao  = time_pontuacao.pontuacao!!["Missão " + missaoNome]

            bundle.putString("missao", missaoNome) //Nome da missão
            bundle.putString("time", time_pontuacao.nome) //Nome do time
            bundle.putInt("pontuacao", pontuacao!!) //Pontuação da missão

            for(missao in missao_collection){
                if(missao.nome == "Missão " + missao_nome[e.x.toInt()]){
                    bundle.putString("descricao", missao.descricao)
                }
            }
            fragmento.arguments = bundle

            //Transação para o fragmento que mostra as informações da missão
            val activity = this.context
            val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainer, fragmento)
            fragmentTransaction?.commit()
        }

        override fun onNothingSelected() {
        }
    }

}
