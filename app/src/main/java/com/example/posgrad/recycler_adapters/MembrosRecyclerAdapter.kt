package com.example.posgrad.recycler_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
//import com.example.posgrad.R
import android.os.Bundle
import com.example.posgrad.R
import com.example.posgrad.data_class.Time
import com.example.posgrad.fragments.TimeInfoFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_square_card.view.*


class MembrosRecyclerAdapter(val times : ArrayList<Time>, val context: FragmentActivity?)  : RecyclerView.Adapter<MembrosRecyclerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(com.example.posgrad.R.layout.layout_square_card, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return times.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nomeSquare.text = times.get(position).id
        if(times.get(position).avatar.isNotEmpty()){
            Picasso.get().load(times.get(position).avatar).into(holder.avatarSquare)
        }

        holder.avatarSquare.setOnClickListener{
            val fragmentoAlvo = TimeInfoFragment()
            val bundle = Bundle()

            bundle.putString("nome_time", times.get(position).id)

            fragmentoAlvo.arguments = bundle

            val fragmentTransaction = context?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainer, fragmentoAlvo)
            fragmentTransaction?.commit()
        }
    }


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val nomeSquare = itemView.square_nome
        val avatarSquare = itemView.square_avatar
    }


}