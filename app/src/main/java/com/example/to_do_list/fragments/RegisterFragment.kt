package com.example.to_do_list.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.to_do_list.R
import com.example.to_do_list.databinding.FragmentRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
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
        binding.btnSignIn.setOnClickListener {
            navController.navigate(R.id.action_registerFragment_to_signInFragment)
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val rePassword = binding.rePassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty() && rePassword.isNotEmpty()) {
                if (password == rePassword) {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                        OnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(context, "Đăng ký thành công!", Toast.LENGTH_LONG)
                                    .show()
                                navController.navigate(R.id.action_registerFragment_to_homeFragment)
                            } else
                                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_LONG)
                                    .show()
                        })
                } else {
                    val animation =
                        AnimationUtils.loadAnimation(requireContext(), R.anim.vibrate_control)
                    binding.password.startAnimation(animation)
                    binding.rePassword.startAnimation(animation)
                    Toast.makeText(context, "Mật khẩu không khớp!", Toast.LENGTH_LONG).show()
                }
            } else {
                val animation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.vibrate_control)
                binding.email.startAnimation(animation)
                binding.password.startAnimation(animation)
                binding.rePassword.startAnimation(animation)
                Toast.makeText(context, "Không được để trống bất kỳ trường nào!", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}
