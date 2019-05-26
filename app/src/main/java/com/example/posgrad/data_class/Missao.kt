package com.example.posgrad.data_class

import com.google.firebase.firestore.DocumentReference

data class Missao(
    val descricao : String = "",
    val nome : String = "",
    val order : Long = 0,
    var temp : String = "",
    val temporada : DocumentReference? = null,
    var missao_ref_string : String = ""
)