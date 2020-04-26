package com.evans.technologies.usuario.Activities.cupon

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.evans.technologies.usuario.R
import com.evans.technologies.usuario.Utils.Adapters.adapter_rv_cupon
import com.evans.technologies.usuario.Utils.Adapters.adapter_rv_cupon.click
import com.evans.technologies.usuario.model.DataCupon
import kotlinx.android.synthetic.main.activity_pagos.*
import java.util.*

class PagosActivity : Fragment() {


    private var adapterRview: RecyclerView.Adapter<*>? = null
    var tipepays = ArrayList<DataCupon>()
    val efectivo =
        DataCupon("Cesar Willy", "efectivo", "Luis rivarola", "Puno", "Puno")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_pagos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (tipepays!=null)
            tipepays.clear()
        tipepays.add(efectivo)

        ap_rv_metodos_pay!!.layoutManager = LinearLayoutManager(requireContext())
        val adapterclass = adapter_rv_cupon(
            requireContext(),
            R.layout.dialog_rv_metodos_pago,
            click { data, adapterPosition ->

            })
//        ap_txt_add_pagos!!.setOnClickListener(this)
        adapterRview=adapterclass
        ap_ll_cupones!!.setOnClickListener{
            findNavController().navigate(R.id.action_nav_wallet_to_cupones)
        }
        adapterclass.getData(tipepays, "metodopay")

        ap_rv_metodos_pay!!.adapter = adapterRview
    }



}