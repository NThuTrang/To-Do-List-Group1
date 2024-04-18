package com.example.to_do_list.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.to_do_list.R
import com.example.to_do_list.databinding.FragmentAddToDoBinding
import com.example.to_do_list.databinding.FragmentHomeBinding
import com.example.to_do_list.utils.ToDoData
import com.google.android.material.textfield.TextInputEditText

class AddToDoFragment : DialogFragment() {
    interface btnNextClickListener {
        fun onSaveTask(todo: String, form: TextInputEditText)
        fun onUpdateTask(toDoData: ToDoData, form: TextInputEditText)
    }

    private lateinit var binding: FragmentAddToDoBinding
    private lateinit var listener: btnNextClickListener
    private var toDoData: ToDoData? = null

    fun setListener(listener: btnNextClickListener) {
        this.listener = listener
    }

    companion object {
        const val TAG = "AddToDoFragment"

        @JvmStatic
        fun newInstance(taskID: String, task: String) = AddToDoFragment().apply {
            arguments = Bundle().apply {
                putString("taskID", taskID)
                putString("task", task)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddToDoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            toDoData = ToDoData(
                arguments?.getString("taskID").toString(),
                arguments?.getString("task").toString()
            )
            binding.toDo.setText(toDoData?.task)
        }
        registerEvents()
    }

    private fun registerEvents() {
        binding.btnNext.setOnClickListener {
            val toDo = binding.toDo.text?.toString()?.trim()

            if (toDo != null) {
                if (toDo.isNotEmpty()) {
                    if (toDoData == null)
                        listener.onSaveTask(toDo, binding.toDo as TextInputEditText)
                    else {
                        toDoData?.task = toDo
                        listener.onUpdateTask(toDoData!!, binding.toDo as TextInputEditText)
                    }
                } else
                    Toast.makeText(context, "Không được để trống tiêu đề!", Toast.LENGTH_LONG)
                        .show()
            }
        }

        binding.btnClose.setOnClickListener { dismiss() }
    }
}