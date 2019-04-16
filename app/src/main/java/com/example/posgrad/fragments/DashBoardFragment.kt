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

class DashBoardFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //Consulta ao FireBase
        val db = FirebaseFirestore.getInstance()
        //fireStoreQuery(db)
        testData(db)
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        chartView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        chartView.adapter = ChartAdapter(pontuacao_collection)
        super.onActivityCreated(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    fun testData(db : FirebaseFirestore){
        val hash_test = missaoPontuacaoHashMap

        hash_test.put("missao1", 60)
        hash_test.put("missao2", 80)
        hash_test.put("missao3", 70)
        val data_test : TimePontuacao = TimePontuacao("TIME TESTE", hash_test)
        pontuacao_collection.add(data_test)
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

                        missao.id = document.id
                        Log.d("MissaoID", missao.id)
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
                        Log.d("timeRef", time.time_ref.toString())
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

        //Pra cada time, recupero as atividades relacionadas
        for(time in time_collection){
            Log.d("homer", "time_ref : " + time.id)
            atividades
                .whereEqualTo("time", time.time_ref)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            Log.d("SuccessAtividade", document.id + " => " + document.data)
                            val atividade  = document.toObject(Atividade::class.java)
                            atividade_collection.add(atividade)

                            //Recupero o nome da missão referente a atividade
                            val missao_ref = document.getDocumentReference("missao")
                            missao_ref?.get()?.addOnCompleteListener { task2 ->
                                val missao_nome = task2.result!!.getString("nome")

                                //Adiciono a pontuação da atividade atual a pontuação total da missão
                                val pontuacao_atual = missaoPontuacaoHashMap.get(missao_nome)
                                missaoPontuacaoHashMap.put(missao_nome,pontuacao_atual!!.plus(atividade.pontuacao))

                                Log.d("AtividadeList","time: " + time.id  + "atividade: " + atividade.nome + " / missao: "+ missao_nome + " / pontuacao: " + atividade.pontuacao)
                            }
                        }
                    }
                    else {
                        Log.d("ErrorAtividade", "Error getting documents: ", task.exception)
                    }
                }

            //Adiciono o nome do time e pontuação total de cada missão a minha List(pontuacao_collection)
            val time_pontuacao : TimePontuacao = TimePontuacao(time.id, missaoPontuacaoHashMap)

            //if(time_pontuacao.nome == user.time){
                //setMainChart(time_pontuacao)

            Log.d("timePontuacao", "Time: " + time_pontuacao.nome)
            pontuacao_collection.add(time_pontuacao)

            //Reseto a pontuação do HashMap
            for(element in missaoPontuacaoHashMap){
                element.setValue(0)
                Log.d("resetHash", "missao: " + element.component1() + " / pontuação: " + element.component2())
            }
        }
    }

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