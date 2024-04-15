package com.example.to_do_list.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.to_do_list.databinding.ItemBinding

class Adapter(private val list: MutableList<ToDoData>) :
    RecyclerView.Adapter<Adapter.ToDoViewHolder>() {

    interface AdapterClick {
        fun onClickDelete(toDoData: ToDoData)
        fun onClickEdit(toDoData: ToDoData)
    }

    private var listener: AdapterClick? = null

    fun setListener(listener: AdapterClick) {
        this.listener = listener
    }

    inner class ToDoViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToDoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                binding.toDo.text = this.task

                binding.delete.setOnClickListener {
                    listener?.onClickDelete(this)
                }

                binding.edit.setOnClickListener {
                    listener?.onClickEdit(this)
                }
            }
        }
    }
}