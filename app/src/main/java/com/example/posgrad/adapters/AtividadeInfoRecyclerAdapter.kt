package com.example.posgrad.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.posgrad.MainActivity
import com.example.posgrad.data_class.Atividade
//import com.example.posgrad.R
import com.example.posgrad.data_class.TimePontuacao
import com.example.posgrad.fragments.atividades_locais
import kotlinx.android.synthetic.main.layout_atividade_info.view.*
import kotlinx.android.synthetic.main.layout_chart.view.*
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri


class AtividadeInfoRecyclerAdapter(val atividades : ArrayList<Atividade>, val context: FragmentActivity?)  : RecyclerView.Adapter<AtividadeInfoRecyclerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): AtividadeInfoRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(com.example.posgrad.R.layout.layout_atividade_info, parent, false)
        Log.d("pikachu", atividades.toString())
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return atividades.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nomeAtividade.text = atividades.get(position).nome
        holder.pontuacaoAtividade.text = ("Pontuação: " + atividades.get(position).pontuacao.toString())
        holder.avaliadorAtividade.text = ("Avaliador: " + atividades.get(position).avaliador)
        holder.feedbackAtividade.text = ("Feedback: " + atividades.get(position).feedback)

        if( atividades.get(position).feedback == "ND"){
            holder.feedbackLink.visibility = View.VISIBLE
            holder.feedbackLink.setOnClickListener{
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(atividades.get(position).arquivo))
                context?.startActivity(intent)
            }
        }
        else{
            holder.feedbackLink.visibility = View.GONE
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val nomeAtividade = itemView.nomeAtividade
        val pontuacaoAtividade = itemView.pontuacaoAtividade
        val avaliadorAtividade = itemView.avaliadorAtividade
        val feedbackAtividade = itemView.feedbackAtividade
        val feedbackLink = itemView.feedbackLink
    }


}