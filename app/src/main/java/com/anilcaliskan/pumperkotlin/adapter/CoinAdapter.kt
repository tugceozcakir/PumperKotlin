package com.anilcaliskan.pumperkotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anilcaliskan.pumperkotlin.R
import com.anilcaliskan.pumperkotlin.model.CoinModel

class CoinAdapter(private val coinList: ArrayList<CoinModel>) : RecyclerView.Adapter<CoinAdapter.CoinHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): CoinHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_row, parent, false)
        return CoinHolder(view)

    }


    override fun onBindViewHolder(holder: CoinHolder,position: Int) {
        val currentItem = coinList[position]
        holder.coinName.text = currentItem.coinName
        holder.currentPoint.text = currentItem.currentPoint.toString()
        holder.refPoint.text = currentItem.refPoint.toString()
        holder.percentage.text = currentItem.percentage.toString()
    }

    override fun getItemCount(): Int {
        return coinList.size
    }


    class CoinHolder(view: View): RecyclerView.ViewHolder(view) {


            val coinName: TextView = itemView.findViewById(R.id.coinNameText)
            val refPoint: TextView = itemView.findViewById(R.id.refPointText)
            val currentPoint: TextView = itemView.findViewById(R.id.currentPointText)
            val percentage: TextView = itemView.findViewById(R.id.percentageText)

    }


}

