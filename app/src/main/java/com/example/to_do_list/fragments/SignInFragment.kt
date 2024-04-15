package com.example.to_do_list.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.to_do_list.R
import com.example.to_do_list.databinding.FragmentRegisterBinding
import com.example.to_do_list.databinding.FragmentSignInBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }

    private fun registerEvents() {
        binding.btnRegister.setOnClickListener {
            navController.navigate(R.id.action_signInFragment_to_registerFragment)
        }

        binding.btnSignIn.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                        OnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(context, "Đăng nhập thành công!", Toast.LENGTH_LONG)
                                    .show()
                                navController.navigate(R.id.action_signInFragment_to_homeFragment)
                            } else
                                Toast.makeText(context,"Tên đăng nhập hoặc mật khẩu không đúng!", Toast.LENGTH_LONG)
                                    .show()
                        })
            } else
                Toast.makeText(context, "Không được để trống bất kỳ trường nào!", Toast.LENGTH_LONG).show()
        }
    }


}