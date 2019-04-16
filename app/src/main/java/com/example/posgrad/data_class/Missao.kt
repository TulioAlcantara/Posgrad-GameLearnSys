package com.example.posgrad.data_class

import com.google.firebase.firestore.DocumentReference

data class Missao(
    val descricao : String = "",
    val nome : String = "",
    val order : Int = 0,
    var id : String = "",
    val temporada : DocumentReference? = null
)