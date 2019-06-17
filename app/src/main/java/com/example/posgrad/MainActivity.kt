package com.example.posgrad

import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.posgrad.adapters.FireStoreQuery
import com.example.posgrad.data_class.*
import com.example.posgrad.fragments.*
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.squareup.picasso.Picasso

//Lists que armazenam os documentos das seguintes coleções
val atividade_collection  = ArrayList<Atividade>()
val atividade_collection_temporada = ArrayList<Atividade>()
val missao_collection = ArrayList<Missao>()
val time_collection = ArrayList<Time>()
val membros_collection = ArrayList<Membro>()
val self_collection = ArrayList<Self>()
val self_collection_dss = ArrayList<Self>()
val self_collection_espgti = ArrayList<Self>()

//Lista da relação Time - Pontuação(Hash missão - pontuação)
val pontuacao_collection = ArrayList<TimePontuacao>()

//Hash que relaciona o nome da missão com a sua pontuação - Usamos para inicializar os elementos de pontuacao_collection
val missaoPontuacaoHashMap = mutableMapOf<String?, Int?>()

//Pontuação do time do usuário
var timePontuacaoMain = TimePontuacao()

//Modelo do Usuário
var usuario = Membro()

//val atividade_resultado : QuerySnapshot?

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Listener da toolbar
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        //Query que recupera todos os dados do BD
        mainQuery()

        //Listener do avatar do usuário
        userAvatar.setOnClickListener {
            replaceFragment(UserInfoFragment())
        }


    }

    //Controlador da bottom navigation view
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_dashboard -> {
                replaceFragment(DashBoardFragment())
                backButtonVisible(0)
                userAvatar.visibility = View.VISIBLE
                navigation.visibility = View.VISIBLE
                fragmentTitle.text = getString(R.string.title_activity_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_self-> {
                replaceFragment(SelfServiceFragment())
                fragmentTitle.text = getString(R.string.title_activity_selfservice)
                navigation.visibility = View.VISIBLE
                return@OnNavigationItemSelectedListener true
            }/*
            R.id.navigation_news-> {
                //replaceFragment(SelfServiceFragment())
                fragmentTitle.text = getString(R.string.title_activity_news)
                return@OnNavigationItemSelectedListener true
            }*/
            R.id.navigation_times-> {
                replaceFragment(TimesFragment())
                fragmentTitle.text = getString(R.string.title_activity_times)
                navigation.visibility = View.VISIBLE
                return@OnNavigationItemSelectedListener true
            }/*
            R.id.navigation_game-> {
                //replaceFragment(SelfServiceFragment())
                fragmentTitle.text = getString(R.string.title_activity_game)
                return@OnNavigationItemSelectedListener true
            }*/
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

        val getTimesTask = query.getTimes()
        val getMissoesTask = query.getMissoes()
        val getSelfTask = query.getSelf()
        val getMembrosTask = query.getMembros()
        val getAtividadesTask = query.getAtividades()

        // Tasks.whenAll só chama o bloco dentro do onComplete dele quando todas as tasks abaixo retornam onSuccess
        allTask = Tasks.whenAll(getTimesTask, getMissoesTask, getMembrosTask, getAtividadesTask)

        allTask.addOnCompleteListener {
            val times: QuerySnapshot?
            val missoes: QuerySnapshot?
            val self : QuerySnapshot?
            val membros: QuerySnapshot?
            val atividades: QuerySnapshot?

            if (getTimesTask.isSuccessful && getMissoesTask.isSuccessful &&
                getMembrosTask.isSuccessful && getAtividadesTask.isSuccessful && getSelfTask.isSuccessful) {

                times = getTimesTask.result!!
                missoes = getMissoesTask.result!!
                self = getSelfTask.result!!
                membros = getMembrosTask.result!!
                atividades = getAtividadesTask.result!!

                query.resultTime(times)
                query.resultMissoes(missoes)
                query.resultSelf(self)
                query.resultMembros(intent.getStringExtra("user_email"), membros)
                query.resultAtividades(atividades, "1ª Temporada")


                Log.d("times", times.toString())
                Log.d("missoes", missoes.toString())
                Log.d("self", self.toString())
                Log.d("membros", membros.toString())
                Log.d("atividades", atividades.toString())


                progressBar.visibility = View.INVISIBLE

                //Set user avatar
                if(usuario.avatar.isNotEmpty()){
                    Picasso.get().load(usuario.avatar).into(userAvatar)
                }
                else{
                    //userAvatar.setBackgroundResource(R.drawable.ic_misc_user_notfound)
                }

                replaceFragment(DashBoardFragment())
            }

            else {
                Log.d("times", getTimesTask.exception.toString())
                Log.d("missoes", getMissoesTask.exception.toString())
                Log.d("self", getSelfTask.exception.toString())
                Log.d("membros", getMembrosTask.exception.toString())
                Log.d("atividades", getAtividadesTask.exception.toString())
            }
        }

    }

    override fun onBackPressed() {
        finish()
    }
}

