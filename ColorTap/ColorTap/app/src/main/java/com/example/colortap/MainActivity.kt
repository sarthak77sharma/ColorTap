package com.example.colortap

import android.app.Dialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.colortap.databinding.ActivityMainBinding
import com.example.colortap.databinding.GameOverDialogBinding

import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), ItemClickListener {
    var binding : ActivityMainBinding? = null
    var adapter : MyAdapter? = null
    var arrayList = ArrayList<MyModel>()
    var score = 0
    var tempArrayList = ArrayList<MyModel>()
    var handler = Handler(Looper.getMainLooper())
    var isSelected : Boolean = false
    var isFirstTime : Boolean = false
    var colorList = listOf(Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN,Color.CYAN,Color.MAGENTA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        setUpData()
        binding!!.rv.visibility = View.GONE
        binding!!.score.visibility = View.GONE
        adapter = MyAdapter(arrayList,this)
        binding!!.rv.adapter = adapter
        binding!!.tvStartGame.setOnClickListener {
            binding!!.rv.visibility = View.VISIBLE
            binding!!.score.visibility = View.VISIBLE

            binding!!.tvStartGame.visibility = View.GONE
            adapter!!.startGame(true)
            score = 0
            binding!!.score.text = "Score : ${score}"
            isSelected = true

            isFirstTime = false
            randomSelection(adapter!!)
        }
    }

    private fun randomSelection(adapter: MyAdapter) {

        if (!isSelected){
            gameOver()
        }else {

            tempArrayList.clear()

            setUpData()

            for ((i, item) in arrayList.withIndex()) {
                if (!item.isSelected){
                    tempArrayList.add(item)
                }
            }

            tempArrayList.shuffle()

            handler.postDelayed({

                binding!!.tvStartGame.isEnabled = false
                binding!!.tvStartGame.alpha = 1f

                for ((i, item) in arrayList.withIndex()) {
                    if (tempArrayList.size > 0 ){
                        arrayList[i].isSelected = tempArrayList[0].index == item.index
                    }
                }

                adapter.notifyDataSetChanged()


                if (isFirstTime){
                    isSelected = false
                }
                isFirstTime = true
                randomSelection(adapter)
            }, 700)
        }

    }

    private fun setUpData() {

        if (arrayList.isEmpty()){
            for ((i, item) in colorList.withIndex()) {
                val model = MyModel(i, false, item)
                arrayList.add(model)
            }
        }

    }

    override fun click(position: Int) {
        isFirstTime = false
        if (arrayList[position].isSelected){
            score++
            isSelected = true
            binding!!.score.text = "Score : ${score}"
        }else{
            gameOver()
        }

    }

    private fun gameOver() {
        adapter!!.startGame(false)
        binding!!.rv.visibility = View.GONE
        binding!!.score.visibility = View.GONE


        showPopup()
        binding!!.score.text = "Score : 0"

        handler.removeCallbacksAndMessages(null)
        binding!!.tvStartGame.isEnabled = true
        binding!!.tvStartGame.alpha = 1f


        for ((i, item) in arrayList.withIndex()) {
            arrayList[i].isSelected = false
        }

        adapter!!.notifyDataSetChanged()

    }

    private fun showPopup() {
        Toast.makeText(this, "GAME OVER", Toast.LENGTH_SHORT).show()

        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val dialogBinding = GameOverDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.finalScore.setText("FINAL SCORE : ${score}")
        dialogBinding.continueBtn.setOnClickListener {
            dialog.dismiss()
            binding!!.tvStartGame.visibility = View.VISIBLE

        }
        dialog.show()
    }
}