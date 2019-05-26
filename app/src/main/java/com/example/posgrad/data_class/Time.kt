package com.example.posgrad.data_class

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QueryDocumentSnapshot

data class Time(
    var id : String = "",
    val avatar : String = "",
    var time_ref : DocumentReference? = null,
    var time_ref_string: String = ""
)