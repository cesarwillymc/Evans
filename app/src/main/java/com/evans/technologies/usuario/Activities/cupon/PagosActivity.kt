package com.evans.technologies.usuario.Activities.cupon

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.evans.technologies.usuario.R
import com.evans.technologies.usuario.Retrofit.RetrofitClient
import com.evans.technologies.usuario.Utils.Adapters.adapter_rv_cupon
import com.evans.technologies.usuario.Utils.Adapters.adapter_rv_cupon.click
import com.evans.technologies.usuario.Utils.getUserId_Prefs
import com.evans.technologies.usuario.model.DataCupon
import com.evans.technologies.usuario.model.modelTrip.trip
import kotlinx.android.synthetic.main.activity_pagos.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
        val prefs = requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE)

        RetrofitClient.getInstance().api.getMoneyEvansWallet(getUserId_Prefs(prefs)!!).enqueue(object:
            Callback<trip>{
            override fun onFailure(call: Call<trip>, t: Throwable) {
                Log.e("evansW",t.message)
            }

            override fun onResponse(call: Call<trip>, response: Response<trip>) {
                Log.e("evansW",response.code().toString())
                if(response.isSuccessful){
                    dew_txt_wallet.text= "PEN ${response.body()!!.ewallet.value}"
                }else{

                }
            }

        })
        tipepays.add(efectivo)

        ap_rv_metodos_pay!!.layoutManager = LinearLayoutManager(requireContext())
        val adapterclass = adapter_rv_cupon(
            requireContext(),
            R.layout.dialog_rv_metodos_pago,
            click { data, adapterPosition ->
                findNavController().navigate(R.id.action_nav_wallet_to_efectivoInfo)
            })
//        ap_txt_add_pagos!!.setOnClickListener(this)
        adapterRview=adapterclass
        ap_ll_cupones!!.setOnClickListener{
            findNavController().navigate(R.id.action_nav_wallet_to_cupones)
        }
        adapterclass.getData(tipepays, "metodopay")

        ap_rv_metodos_pay!!.adapter = adapterRview

        cuponinfo.setOnClickListener {

        }
        dew_txt_help.setOnClickListener {
            findNavController().navigate(R.id.action_nav_wallet_to_evansWallet)
        }
    }



}