package com.evans.technologies.usuario.fragments.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.evans.technologies.usuario.R
import kotlinx.android.synthetic.main.fragment_inicio.*

/**
 * A simple [Fragment] subclass.
 */
class inicioFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fi_btn_signup.setOnClickListener {
            findNavController().navigate(R.id.action_inicioFragment_to_correo)
        }
        fi_btn_signin.setOnClickListener {
            findNavController().navigate(R.id.action_inicioFragment_to_correo)
        }
    }

}
