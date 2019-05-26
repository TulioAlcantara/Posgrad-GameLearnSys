package com.example.posgrad

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.UiThread
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.component2
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.posgrad.adapters.FireStoreQuery
import com.example.posgrad.data_class.*
import com.example.posgrad.fragments.DashBoardFragment
import com.example.posgrad.fragments.SelfServiceFragment
import com.example.posgrad.fragments.TimesFragment
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.squareup.picasso.Picasso
import org.jetbrains.anko.UI
import org.jetbrains.anko.activityUiThread
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

//Lists que armazenam os documentos das seguintes coleções
val atividade_collection  = ArrayList<Atividade>()
val atividade_collection_temporada = ArrayList<Atividade>()
val missao_collection = ArrayList<Missao>()
val time_collection = ArrayList<Time>()
val membros_collection = ArrayList<Membro>()

//Lista da relação Time - Pontuação(Hash missão - pontuação)
val pontuacao_collection = ArrayList<TimePontuacao>()

//Hash que relaciona o nome da missão com a sua pontuação - Usamos para inicializar os elementos de pontuacao_collection
val missaoPontuacaoHashMap = mutableMapOf<String?, Int?>()

//Pontuação do time do usuário
val timePontuacaoMain = TimePontuacao()

//Modelo do Usuário
var usuario = Membro()

//val atividade_resultado : QuerySnapshot?

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        //Set user avatar
        Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(userAvatar)
        mainQuery()
    }

    //Controlador da bottom navigation view
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_dashboard -> {
                replaceFragment(DashBoardFragment())
                backButtonVisible(0)
                fragmentTitle.text = getString(R.string.title_activity_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_self-> {
                replaceFragment(SelfServiceFragment())
                fragmentTitle.text = getString(R.string.title_activity_selfservice)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_news-> {
                //replaceFragment(SelfServiceFragment())
                fragmentTitle.text = getString(R.string.title_activity_news)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_times-> {
                replaceFragment(TimesFragment())
                fragmentTitle.text = getString(R.string.title_activity_times)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_game-> {
                //replaceFragment(SelfServiceFragment())
                fragmentTitle.text = getString(R.string.title_activity_game)
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

    //Controla a visibilidade do Widget do canto superior direito
    fun backButtonVisible(active : Int){
        if(active == 1){
            spinner.visibility = View.INVISIBLE
            backButton.visibility = View.VISIBLE
        }

        else if(active == 0){
            spinner.visibility = View.VISIBLE
            backButton.visibility = View.INVISIBLE
        }

        else if(active == -1){
            spinner.visibility = View.INVISIBLE
            backButton.visibility = View.INVISIBLE
        }
    }

    fun mainQuery(){
        val allTask: Task<Void>
        val db = FirebaseFirestore.getInstance()
        val query = FireStoreQuery(db)

        val gettimesTask = query.getTimes()
        val getmissoesTask = query.getMissoes()
        val getmembrosTask = query.getMembros()
        val getAtividadesTask = query.getAtividades()

        // Tasks.whenAll só chama o bloco dentro do onComplete dele quando todas as tasks abaixo retornam onSuccess
        allTask = Tasks.whenAll(gettimesTask, getmissoesTask, getmembrosTask, getAtividadesTask)

        allTask.addOnCompleteListener {
            val times: QuerySnapshot?
            val missoes: QuerySnapshot?
            val membros: QuerySnapshot?
            val atividades: QuerySnapshot?

            if (gettimesTask.isSuccessful && getmissoesTask.isSuccessful &&
                getmembrosTask.isSuccessful && getAtividadesTask.isSuccessful) {

                times = gettimesTask.result!!
                missoes = getmissoesTask.result!!
                membros = getmembrosTask.result!!
                atividades = getAtividadesTask.result!!

                query.resultTime(times)
                query.resultMissoes(missoes)
                query.resultMembros(membros)
                query.resultAtividades(atividades, "1ª Temporada")

                Log.d("times", times.toString())
                Log.d("missoes", missoes.toString())
                Log.d("membros", membros.toString())
                Log.d("atividades", atividades.toString())


                progressBar.visibility = View.INVISIBLE
                replaceFragment(DashBoardFragment())
            }

            else {
                Log.d("times", gettimesTask.exception.toString())
                Log.d("missoes", getmissoesTask.exception.toString())
                Log.d("membros", getmembrosTask.exception.toString())
                Log.d("atividades", getAtividadesTask.exception.toString())
            }
        }

    }
}

