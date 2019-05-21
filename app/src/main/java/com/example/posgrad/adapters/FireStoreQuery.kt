package com.example.posgrad.adapters

import android.util.Log
import com.example.posgrad.*
import com.example.posgrad.data_class.*
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class FireStoreQuery(){

    fun MainQuery(){
        val db = FirebaseFirestore.getInstance()

        val missoes = db.collection("missoes")
        val times = db.collection("times")
        val membros = db.collection(("membros"))

        //Obtenho o usuário
        membros
            .whereEqualTo("email", "heitorbatista.oliveira@gmail.com")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        usuario = document.toObject(Membro::class.java)
                    }
                }
            }


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
                            atividade.time_string = time.id //Armazeno a referência em string para o time da atividade

                            //Recupero a referencia a missao da atividade
                            val missao_ref = document.getDocumentReference("missao")

                            missao_ref?.get()?.addOnCompleteListener { task2 ->
                                val missao_nome = task2.result!!.getString("nome")

                                atividade.missao_string = missao_nome.orEmpty() //Armazeno a referência em string para a missão da atividade

                                //Verifico a temporada
                                if(task2.result!!.getString("temp") == "1ª Temporada"){
                                    //Atualizo a pontuação
                                    val pontuacao_atual = time_pontuacao.pontuacao?.get(missao_nome)?.plus(atividade.pontuacao)
                                    time_pontuacao.pontuacao?.put(missao_nome, pontuacao_atual)
                                }
                            }

                            atividade_collection.add(atividade) //Adiciono a atividade a minha lista
                        }
                    }
                    else {
                        Log.d("ErrorAtividade", "Error getting documents: ", task.exception)
                    }
                }

            //Verifico se eh o time do usuário
            if(time.id =="Aquila"){
                timePontuacaoMain.nome = time.id
                timePontuacaoMain.pontuacao = time_hash
            }
            else{
                pontuacao_collection.add(time_pontuacao) //Adiciono a pontuação do time a minha lista
            }
        }
    }
}