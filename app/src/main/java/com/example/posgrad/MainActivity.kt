package com.example.posgrad

import android.os.Bundle
import android.util.Log
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
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import org.jetbrains.anko.UI
import org.jetbrains.anko.activityUiThread
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

//Modelo do Usuário
var usuario = Membro()


class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        replaceFragment(SelfServiceFragment())

        //Set user avatar
        Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(userAvatar)


        //FireStore Query
        val query = FireStoreQuery()
        query.MainQuery()

    }

    //Controlador da bottom navigation view
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_dashboard -> {
                replaceFragment(DashBoardFragment())
                fragmentTitle.text = getString(R.string.title_activity_dashboard)
                spinner.isVisible = true
                backButton.isVisible = false
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_self-> {
                replaceFragment(SelfServiceFragment())
                fragmentTitle.text = getString(R.string.title_activity_selfservice)
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
}

