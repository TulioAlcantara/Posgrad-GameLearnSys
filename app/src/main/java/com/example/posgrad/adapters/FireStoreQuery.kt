package com.example.posgrad.adapters

import android.util.Log
import com.example.posgrad.*
import com.example.posgrad.data_class.*
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.squareup.picasso.Picasso
import kotlin.text.Typography.times

class FireStoreQuery(val db: FirebaseFirestore) {

    fun getMembros(): Task<QuerySnapshot> {
        val membros = db.collection("membros")
        return membros.get()
    }

    fun getMissoes(): Task<QuerySnapshot> {
        val missoes = db.collection("missoes")
        return missoes.get()
    }

    fun getTimes(): Task<QuerySnapshot> {
        val times = db.collection("times")
        return times.get()
    }

    // Caso preciso você cria um "getTempActivities()" que recebe temporada e dá o where e retorna os snapshots
    fun getAtividades(): Task<QuerySnapshot> {
        val atividades = db.collection("atividadesComTime")
        return atividades.get()
    }

    fun resultTime(task: QuerySnapshot?) {
        for (document in task!!.iterator()) {
            Log.d("SuccessTime", document.id + " => " + document.data)
            val time = document.toObject(Time::class.java)

            time.id = document.id
            time.time_ref = document.reference
            time.time_ref_string = document.reference.toString()

            time_collection.add(time)
        }
    }

    fun resultMissoes(task: QuerySnapshot?) {
        for (document in task!!.iterator()) {
            Log.d("SuccessMissao", document.id + " => " + document.data)
            val missao = document.toObject(Missao::class.java)

            missao.missao_ref_string = document.reference.toString()

            missao_collection.add(missao)
            missaoPontuacaoHashMap.put(missao.nome, 0)  // Seto o o time no meu hash com pontuação 0
        }
    }

    fun resultMembros(task: QuerySnapshot?) {
        for (document in task!!.iterator()) {

            Log.d("SuccessMembro", document.id + " => " + document.data)

            val membro = document.toObject(Membro::class.java)
            val time_ref = membro.idtime.toString()

            for (time in time_collection) {
                if (time_ref == time.time_ref_string) {
                    membro.time_string = time.id
                }
            }

            membros_collection.add(membro)

        }
    }

    fun resultAtividades(task: QuerySnapshot?) {

        //var primeiro_loop = 0 //Variavel para garantir que as atividades serão adicionadas na lista apenas um vez

        //Pra cada time, recupero as atividades relacionadas (time_ref de cada atividade)
        for (time in time_collection) {
            val time_hash = HashMap(missaoPontuacaoHashMap) // Cada time recebe a copia de missaoPontuacaoHashMap
            val time_pontuacao = TimePontuacao(time.id, time_hash) // Inicializo time_pontuacao referente ao time

            for (document in task!!.iterator()) {

                Log.d("SuccessAtividade", document.id + " => " + document.data)

                val atividade = document.toObject(Atividade::class.java)

                //Caso a atividade seja referente ao time
                if (atividade.time.toString() == time.time_ref_string) {

                    atividade.time_string = time.id //Armazeno a referência em string para o time da atividade
                    val missao_ref = atividade.missao.toString() //Recupero a referencia a missao da atividade

                    for (missao in missao_collection) {

                        //Busco a missão referente a atividade
                        if (missao.missao_ref_string == missao_ref) {
                            atividade.missao_string = missao.nome //Armazeno a referência em string para a missão da atividade

                            //Verifico a temporada
                            if (missao.temp == "1ª Temporada") {
                                //Atualizo a pontuação
                                val pontuacao_atual = time_pontuacao.pontuacao?.get(missao.nome)?.plus(atividade.pontuacao)
                                time_pontuacao.pontuacao?.put(missao.nome, pontuacao_atual)

                                Log.d("saber", time.id + "" + missao.nome)
                                atividade_collection.add(atividade) //Adiciono a atividade a minha lista
                            }
                        }
                    }
                }
            }

            //Verifico se eh o time do usuário
            if (time.id == "Aquila") {
                timePontuacaoMain.nome = time.id
                timePontuacaoMain.pontuacao = time_hash
            } else {
                pontuacao_collection.add(time_pontuacao) //Adiciono a pontuação do time a minha lista
            }
        }
    }
}


/*
fun membroQuery(){
    //Obtenho o usuário
    val membros = this.db.collection(("membros"))
    membros
        .get()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    Log.d("SuccessMembro", document.id + " => " + document.data)
                    val membro = document.toObject(Membro::class.java)
                    membros_collection.add(membro)
                }
            }
            else{
                Log.d("ErrorMembro", "Error getting documents: ", task.exception)
            }
        }
}

fun timeQuery(){
    //Obtenho os times
    val times = this.db.collection("times")
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
            }
            else {
                Log.d("ErrorTime", "Error getting documents: ", task.exception)
            }
        }
}

fun missaoQuery(){
    //Obtenho as missões e as coloco no meu hashmap
    val missoes = this.db.collection("missoes")
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
}



fun atividadeQuery(){
    val atividades = this.db.collection("atividadesComTime")

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


fun testeQuery(){
    //Obtenho os membros
    val membros = this.db.collection(("membros"))
    membros
        .get()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    Log.d("SuccessMembro", document.id + " => " + document.data)
                    val membro = document.toObject(Membro::class.java)


                    //Acesso o time referente ao membro e armazeno seu nome
                    val time_ref = membro.idtime
                    time_ref?.get()?.addOnCompleteListener{ task2 ->
                        val time_nome = task2.result!!.id
                        membro.time_string = time_nome
                    }

                    membros_collection.add(membro)
                }
            }
            else{
                Log.d("ErrorMembro", "Error getting documents: ", task.exception)
            }
        }

    //Obtenho as missões e as coloco no meu hashmap
    val missoes = this.db.collection("missoes")
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
    val times = this.db.collection("times")
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
                atividadeQuery()
            }
            else {
                Log.d("ErrorTime", "Error getting documents: ", task.exception)
            }
        }
}
 */
