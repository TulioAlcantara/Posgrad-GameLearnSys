package com.example.posgrad.recycler_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import com.example.posgrad.R
import com.example.posgrad.fragments.SelfService2Fragment
import kotlinx.android.synthetic.main.layout_self_service1.view.*


class Self1RecyclerAdapter(val self : Array<String>, val formacao : String,  val context: FragmentActivity?)  : RecyclerView.Adapter<Self1RecyclerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(com.example.posgrad.R.layout.layout_self_service1, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return self.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ciclo.text = self.get(position)

        val ciclo =  self.get(position)
        holder.item1.setOnClickListener {
            val fragmentoAlvo = SelfService2Fragment()
            val bundle = Bundle()

            bundle.putString("ciclo", ciclo)
            bundle.putString("enfase", holder.item1.text.toString())

            if(formacao == "DSS-BI"){
                bundle.putString("formacao", "DSS-BI")
            }
            else{
                bundle.putString("formacao", "ESPGTI")
            }

            fragmentoAlvo.arguments = bundle

            val fragmentTransaction = context?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainer, fragmentoAlvo)
            fragmentTransaction?.commit()
        }

        holder.item2.setOnClickListener {
            val fragmentoAlvo = SelfService2Fragment()
            val bundle = Bundle()

            bundle.putString("ciclo", ciclo)
            bundle.putString("enfase", holder.item2.text.toString())

            if(formacao == "DSS-BI"){
                bundle.putString("formacao", "DSS-BI")
            }
            else{
                bundle.putString("formacao", "ESPGTI")
            }

            fragmentoAlvo.arguments = bundle

            val fragmentTransaction = context?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainer, fragmentoAlvo)
            fragmentTransaction?.commit()
        }

        holder.item3.setOnClickListener {
            val fragmentoAlvo = SelfService2Fragment()
            val bundle = Bundle()

            bundle.putString("ciclo", ciclo)
            bundle.putString("enfase", holder.item3.text.toString())

            if(formacao == "DSS-BI"){
                bundle.putString("formacao", "DSS-BI")
            }
            else{
                bundle.putString("formacao", "ESPGTI")
            }

            fragmentoAlvo.arguments = bundle

            val fragmentTransaction = context?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainer, fragmentoAlvo)
            fragmentTransaction?.commit()
        }

        fun itemListener(){

        }

    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val ciclo = itemView.self_titulo
        val item1 = itemView.self_item1
        val item2 = itemView.self_item2
        val item3 = itemView.self_item3
    }
}