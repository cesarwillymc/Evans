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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_pagos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val efectivo =
            DataCupon("Cesar Willy", "efectivo", "Luis rivarola", "Puno", "Puno")
        tipepays.add(efectivo)
        ap_rv_metodos_pay!!.layoutManager = LinearLayoutManager(requireContext())
        adapterRview = adapter_rv_cupon(
            requireContext(),
            R.layout.dialog_rv_metodos_pago,
            "metodopay",
            tipepays,
            click { data, adapterPosition -> })
//        ap_txt_add_pagos!!.setOnClickListener(this)
        ap_txt_add_promo!!.setOnClickListener{
            startActivity(
                Intent(
                    requireContext(),
                    AddCodePromocional::class.java
                )
            )
        }
        ap_ll_cupones!!.setOnClickListener{
            startActivity(Intent(requireContext(), ap_ll_cupones::class.java))
        }
        ap_rv_metodos_pay!!.adapter = adapterRview
    }



}