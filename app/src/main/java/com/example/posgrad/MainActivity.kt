package com.example.posgrad

import android.os.Bundle
import android.util.Log
import androidx.annotation.UiThread
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.component2
import androidx.fragment.app.Fragment
import com.example.posgrad.data_class.Atividade
import com.example.posgrad.data_class.Missao
import com.example.posgrad.data_class.Time
import com.example.posgrad.data_class.TimePontuacao
import com.example.posgrad.fragments.DashBoardFragment
import com.example.posgrad.fragments.SelfServiceFragment
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

//Lists que armazenam os documentos das seguintes coleções
val atividade_collection  = ArrayList<Atividade>()
val missao_collection = ArrayList<Missao>()
val time_collection = ArrayList<Time>()

//Lista da relação Time - Pontuação(Hash missão - pontuação)
val pontuacao_collection = ArrayList<TimePontuacao>()

//Hash que relaciona o nome da missão com a sua pontuação - Usamos para inicializar os elementos de pontuacao_collection
val missaoPontuacaoHashMap = mutableMapOf<String?, Int?>()

//Pontuação do time do usuário
val timePontuacaoMain = TimePontuacao()


class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val db = FirebaseFirestore.getInstance()
        fireStoreQuery(db)
        replaceFragment(SelfServiceFragment())
    }

    //Controlador da bottom navigation view
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_dashboard -> {
                replaceFragment(DashBoardFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_self-> {
                replaceFragment(SelfServiceFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    //Substitui o fragmento atual
    fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
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

            //Verifico se eh o time do usuário
            if(time.id =="Dev"){
                timePontuacaoMain.nome = time.id
                timePontuacaoMain.pontuacao = time_hash
            }
            else{
                pontuacao_collection.add(time_pontuacao) //Adiciono a pontuação do time a minha lista
            }
        }
    }
}

