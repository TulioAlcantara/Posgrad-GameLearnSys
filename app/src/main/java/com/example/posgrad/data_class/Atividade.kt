package com.example.posgrad.data_class

import com.google.firebase.firestore.DocumentReference

data class Atividade(
    var arquivo : String = "",
    var avaliador : String = "",
    var feedback : String = "",
    var membro : DocumentReference? = null,
    var missao : DocumentReference? = null,
    var missao_string : String = "",
    var nome : String = "",
    var pontuacao : Int = 0,
    var time : DocumentReference? = null,
    var time_string : String = "",
    var tipo : String = ""
)
