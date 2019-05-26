package com.example.posgrad.data_class

import com.google.firebase.firestore.DocumentReference

data class Membro(
    val avatar : String = "",
    val curso : String = "",
    val email : String = "",
    var idtime : DocumentReference? = null,
    val nome : String = "",
    var time_string : String = ""
)