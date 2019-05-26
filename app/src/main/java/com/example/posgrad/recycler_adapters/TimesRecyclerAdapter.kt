package com.example.posgrad.recycler_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
//import com.example.posgrad.R
import com.example.posgrad.data_class.Membro
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_rectangle_card.view.*


class TimesRecyclerAdapter(val membros : ArrayList<Membro>, val context: FragmentActivity?)  : RecyclerView.Adapter<TimesRecyclerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(com.example.posgrad.R.layout.layout_rectangle_card, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return membros.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nomeRectangle.text = membros.get(position).nome
        if(membros.get(position).avatar.isNotEmpty()){
            Picasso.get().load(membros.get(position).avatar).into(holder.avatarRectangle)
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val nomeRectangle = itemView.rectangle_nome
        val avatarRectangle = itemView.rectangle_avatar
    }
}