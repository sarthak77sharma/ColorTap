package com.example.colortap

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.colortap.databinding.ListItemBinding

class MyAdapter(private val arrayList: ArrayList<MyModel>,private val clickListener: ItemClickListener) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    var binding : ListItemBinding? = null
    var startGame : Boolean = false

    fun startGame(startGame : Boolean){
        Log.w("TAG", "startGame: "+ startGame.toString() )
        this.startGame = startGame
    }

    class MyViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = ListItemBinding.inflate(LayoutInflater.from(parent.context))
        return MyViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (arrayList[position].isSelected){
            holder.binding.box.setBackgroundColor(Color.GRAY)
        }else{
            holder.binding.box.setBackgroundColor(arrayList[position].colorCode)
        }
        if (startGame){
            holder.binding.box.setOnClickListener({
                clickListener.click(position)
            })
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}