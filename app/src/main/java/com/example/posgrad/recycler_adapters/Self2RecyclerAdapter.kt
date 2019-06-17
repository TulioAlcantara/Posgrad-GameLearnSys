package com.example.posgrad.recycler_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import com.example.posgrad.R
import com.example.posgrad.data_class.Self
import com.example.posgrad.fragments.SelfService2Fragment
import com.example.posgrad.fragments.SelfService3Fragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_self_service1.view.*
import kotlinx.android.synthetic.main.layout_square_card.view.*


class Self2RecyclerAdapter(val self : ArrayList<Self>,  val context: FragmentActivity?)  : RecyclerView.Adapter<Self2RecyclerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(com.example.posgrad.R.layout.layout_square_card, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return self.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titulo.text = self.get(position).nome
        val nome = self.get(position).nome
        if(self.get(position).img.isNotEmpty()){
            Picasso.get().load(self.get(position).img).into(holder.avatar)
        }

        holder.avatar.setOnClickListener {
            val fragmentoAlvo = SelfService3Fragment()
            val bundle = Bundle()

            bundle.putString("nome", nome)
            fragmentoAlvo.arguments = bundle

            val fragmentTransaction = context?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainer, fragmentoAlvo)
            fragmentTransaction?.commit()
        }

    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val titulo = itemView.square_nome
        val avatar = itemView.square_avatar
    }
}