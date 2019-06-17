package com.example.posgrad.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.posgrad.MainActivity

import com.example.posgrad.R
import com.example.posgrad.usuario
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_user_info.*
import kotlinx.android.synthetic.main.fragment_user_info.userAvatar

class UserInfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        //Seto as infos do usuário
        userEmail.text = usuario.email
        userNome.text = usuario.nome
        userCurso.text = usuario.curso
        if(usuario.avatar.isNotEmpty()){
            Picasso.get().load(usuario.avatar).into(userAvatar)
        }
        else{
            //userAvatar.setBackgroundResource(R.drawable.ic_misc_user_notfound)
        }

        //Seto os elementos da UI
        (activity as MainActivity).fragmentTitle?.text = ("Perfil")
        (activity as MainActivity).backButtonVisible(1)
        (activity as MainActivity).navigation.visibility = View.INVISIBLE
        (activity as MainActivity).userAvatar.visibility = View.INVISIBLE

        //Função do backButton
        (activity as MainActivity).backButton.setOnClickListener {
            backButtonPressed()
        }

        super.onActivityCreated(savedInstanceState)
    }

    fun backButtonPressed(){
        (activity as MainActivity).backButtonVisible(0)
        (activity as MainActivity).navigation.visibility = View.VISIBLE
        (activity as MainActivity).userAvatar.visibility = View.VISIBLE
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.remove(UserInfoFragment())
        fragmentTransaction?.replace(R.id.fragmentContainer, DashBoardFragment())
        fragmentTransaction?.commit()
    }


}
