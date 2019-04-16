package com.example.posgrad

import android.os.Bundle
import android.util.Log
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

//Lists que armazenam os documentos das seguintes coleções
val atividade_collection  = ArrayList<Atividade>()
val missao_collection = ArrayList<Missao>()
val time_collection = ArrayList<Time>()

//Lista da relação Time - Pontuação(Hash missão - pontuação)
val pontuacao_collection = ArrayList<TimePontuacao>()

//Hash que relaciona o nome da missão com a sua pontuação(soma da pontuação das atividades)
val missaoPontuacaoHashMap = mutableMapOf<String?, Int?>()


class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(SelfServiceFragment())
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
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

}


