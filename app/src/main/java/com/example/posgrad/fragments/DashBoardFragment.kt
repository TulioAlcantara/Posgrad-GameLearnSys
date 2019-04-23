package com.example.posgrad.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.posgrad.*
import com.example.posgrad.adapters.ChartAdapter
import com.example.posgrad.data_class.Atividade
import com.example.posgrad.data_class.Missao
import com.example.posgrad.data_class.Time
import com.example.posgrad.data_class.TimePontuacao
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class DashBoardFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val db = FirebaseFirestore.getInstance()
        doAsync {
            fireStoreQuery(db)
            uiThread {
                progressBar.visibility = View.INVISIBLE
                chartView.adapter?.notifyDataSetChanged()
                for(element in pontuacao_collection){
                    Log.d("pontuacao_collection", element.nome + ": " + element.pontuacao?.values)
                }

            }
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val chart_recyclerView = rootView.findViewById(R.id.chartView) as RecyclerView
        chart_recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        chart_recyclerView.adapter = ChartAdapter(pontuacao_collection)
        return rootView
    }

    override fun onResume() {
        chartView.adapter = ChartAdapter(pontuacao_collection)
        super.onResume()
    }

    fun fireStoreQuery(db : FirebaseFirestore){
        val missoes = db.collection("missoes")
        val times = db.collection("times")

        //Obtenho as missões e as coloco no meu hashmap
        missoes
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d("SuccessMissao", document.id + " => " + document.data)
                        val missao = document.toObject(Missao::class.java)

                        missao_collection.add(missao)
                        missaoPontuacaoHashMap.put(missao.nome, 0)  // Seto o o time no meu hash com pontuação 0
                    }
                }
                else {
                    Log.d("ErrorMissao", "Error getting documents: ", task.exception)
                }
            }

        //Obtenho os times
        times
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d("SuccessTime", document.id + " => " + document.data)
                        val time = document.toObject(Time::class.java)

                        time.id = document.id
                        time.time_ref = document.reference
                        time_collection.add(time)
                    }
                    atividadeQuery(db)
                }
                else {
                    Log.d("ErrorTime", "Error getting documents: ", task.exception)
                }
            }
    }

    fun atividadeQuery(db : FirebaseFirestore){
        val atividades = db.collection("atividadesComTime")

        //Pra cada time, recupero as atividades relacionadas (time_ref de cada atividade)
        for(time in time_collection){
            val time_hash = HashMap(missaoPontuacaoHashMap) // Cada time recebe a copia de missaoPontuacaoHashMap
            val time_pontuacao = TimePontuacao(time.id, time_hash)

            atividades
                .whereEqualTo("time", time.time_ref)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            Log.d("SuccessAtividade", document.id + " => " + document.data)

                            val atividade  = document.toObject(Atividade::class.java)
                            atividade_collection.add(atividade)

                            //Recupero a referencia a missao da atividade
                            val missao_ref = document.getDocumentReference("missao")

                            missao_ref?.get()?.addOnCompleteListener { task2 ->
                                val missao_nome = task2.result!!.getString("nome")

                                //Verifico a temporada
                                if(task2.result!!.getString("temp") == "1ª Temporada"){
                                    //Atualizo a pontuação
                                    val pontuacao_atual = time_pontuacao.pontuacao?.get(missao_nome)?.plus(atividade.pontuacao)
                                    time_pontuacao.pontuacao?.put(missao_nome, pontuacao_atual)
                                }
                            }
                        }
                    }
                    else {
                        Log.d("ErrorAtividade", "Error getting documents: ", task.exception)
                    }
                }
            pontuacao_collection.add(time_pontuacao) //Adiciono a pontuação do time a minha lista
        }
    }

    fun setMainChart(){

    }
    /*
    fun testData(db : FirebaseFirestore){
        val hash_test = missaoPontuacaoHashMap

        hash_test.put("missao1", 60)
        hash_test.put("missao2", 80)
        hash_test.put("missao3", 70)
        val data_test : TimePontuacao = TimePontuacao("TIME TESTE", hash_test)
        pontuacao_collection.add(data_test)
    }
    */
    /*
    fun setMainChart(main_time : TimePontuacao){
        val entries = ArrayList<BarEntry>() // Array com os pontos
        var x = 0
        //Adiciono a pontuação de cada missão no meu array
        for(missao in main_time.pontuacao){
            val entry : BarEntry = BarEntry(x.toFloat(), missao.value!!.toFloat())
            entries.add(entry)
            x = x+1
        }

        val dataset : BarDataSet = BarDataSet(entries, "teste")
        val data : BarData = BarData(dataset)

        //Propriedades do chart
        chart.data = data
        chart.invalidate()
        chart.setFitBars(true)
    }
    */
}