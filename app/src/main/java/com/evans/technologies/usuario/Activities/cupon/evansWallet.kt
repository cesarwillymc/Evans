package com.evans.technologies.usuario.Activities.cupon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.evans.technologies.usuario.R

/**
 * A simple [Fragment] subclass.
 */
class evansWallet : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_evans_wallet, container, false)
    }

}
