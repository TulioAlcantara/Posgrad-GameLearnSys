package com.example.posgrad.recycler_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import com.example.posgrad.R
import com.example.posgrad.data_class.ObjAprendizagem
import com.example.posgrad.data_class.Self
import com.example.posgrad.fragments.SelfService2Fragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_rectangle_card.view.*
import kotlinx.android.synthetic.main.layout_self_service1.view.*
import kotlinx.android.synthetic.main.layout_square_card.view.*


class Self3RecyclerAdapter(val self : ArrayList<ObjAprendizagem>,  val context: FragmentActivity?)  : RecyclerView.Adapter<Self3RecyclerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(com.example.posgrad.R.layout.layout_rectangle_card, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return self.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titulo.text = self.get(position).nome

        if(self.get(position).formato == "pdf"){
            //holder.avatar
        }
        else if(self.get(position).formato == "video"){
           //holder.avatar.setBackgroundResource(R.drawable.img_youtube)
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val titulo = itemView.rectangle_nome
        val avatar = itemView.rectangle_avatar
    }
}