package com.example.to_do_list.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.to_do_list.R
import com.example.to_do_list.databinding.FragmentHomeBinding
import com.example.to_do_list.utils.Adapter
import com.example.to_do_list.utils.ToDoData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeFragment : Fragment(), AddToDoFragment.btnNextClickListener, Adapter.AdapterClick {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var navController: NavController
    private var addToDo: AddToDoFragment? = null
    private lateinit var adapter: Adapter
    private lateinit var list: MutableList<ToDoData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        getDataFromFireBase()
        registerEvents()
    }

    private fun init(view: View) {
        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().reference.child("Tasks")
            .child(auth.currentUser?.uid.toString())
        navController = Navigation.findNavController(view)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        list = mutableListOf()
        adapter = Adapter(list)
        adapter.setListener(this)
        binding.recyclerView.adapter = adapter
    }

    private fun getDataFromFireBase() {
        dbRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (task in snapshot.children) {
                    val toDo = task.key?.let {
                        ToDoData(it, task.value.toString())
                    }
                    if (toDo != null)
                        list.add(toDo)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Đã xảy ra lỗi vui lòng thử lại!", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun registerEvents() {
        binding.btnAdd.setOnClickListener {
            if (addToDo != null)
                childFragmentManager.beginTransaction().remove(addToDo!!).commit()

            addToDo = AddToDoFragment()
            addToDo!!.setListener(this)
            addToDo!!.show(childFragmentManager, AddToDoFragment.TAG)
        }
    }

    override fun onSaveTask(todo: String, form: TextInputEditText) {
        dbRef.push().setValue(todo).addOnCompleteListener {
            if (it.isSuccessful)
                Toast.makeText(context, "Lưu thành công!", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(context, "Đã xảy ra lỗi vui lòng thử lại!", Toast.LENGTH_LONG).show()
            form.text = null
            addToDo!!.dismiss()
        }
    }

    override fun onUpdateTask(toDoData: ToDoData, form: TextInputEditText) {
        val map = HashMap<String, Any>()
        map[toDoData.taskID] = toDoData.task
        dbRef.updateChildren(map).addOnCompleteListener {
            if (it.isSuccessful)
                Toast.makeText(context, "Sửa thành công!", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(context, "Sửa thất bại!", Toast.LENGTH_LONG).show()
            form.text = null
            addToDo!!.dismiss()
        }
    }

    override fun onClickDelete(toDoData: ToDoData) {
        dbRef.child(toDoData.taskID).removeValue().addOnCompleteListener {
            if (it.isSuccessful)
                Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(context, "Xóa thất bại!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onClickEdit(toDoData: ToDoData) {
        if (addToDo != null)
            childFragmentManager.beginTransaction().remove(addToDo!!).commit()

        addToDo = AddToDoFragment.newInstance(toDoData.taskID, toDoData.task)
        addToDo!!.setListener(this)
        addToDo!!.show(childFragmentManager, AddToDoFragment.TAG)
    }
}