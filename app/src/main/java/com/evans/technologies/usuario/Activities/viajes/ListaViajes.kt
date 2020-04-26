package com.evans.technologies.usuario.Activities.viajes

import android.content.Context
import android.content.SharedPreferences
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
import com.evans.technologies.usuario.Utils.Adapters.adapter_rv_trip
import com.evans.technologies.usuario.Utils.getUserId_Prefs
import com.evans.technologies.usuario.model.DataCupon
import com.evans.technologies.usuario.model.modelTrip.Historial
import com.evans.technologies.usuario.model.modelTrip.trip
import kotlinx.android.synthetic.main.activity_rv_viajes.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ListaViajes : Fragment() {
    lateinit var prefs: SharedPreferences
    private var adapterRview: RecyclerView.Adapter<*>? = null
    private var dataArray:List<Historial>?=null
    lateinit var  adapterCreate:adapter_rv_trip
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_rv_viajes,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE)



        arv_viajes!!.layoutManager = LinearLayoutManager(requireContext())
        adapterCreate=adapter_rv_trip(
            requireContext(),
            R.layout.dialog_historial_viajes,
            object:adapter_rv_trip.OnItemClickListener{
                override fun OnClickListener(vaije: Historial?, position: Int) {
                    val next= ListaViajesDirections.actionRvViajes2ToHistorialViaje(vaije!!.id)
                    findNavController().navigate(next)
                }
            }
        )
        adapterRview =adapterCreate
        arv_viajes!!.adapter = adapterRview
        dataSave()
        arv_refresh!!.setOnRefreshListener {
            dataSave()
        }
//findNavController().navigate(R.id.action_rv_viajes2_to_historial_viaje)
    }
        //,ListaViajesDirections()
    private fun dataSave(){
            RetrofitClient.getInstance().api.getHistorialAll(getUserId_Prefs(prefs)!!,1).enqueue(object:
                Callback<trip> {
                override fun onFailure(call: Call<trip>, t: Throwable) {
                    try{
                        arv_refresh!!.isRefreshing=false
                    }catch (e:Exception){

                    }
                    Log.e("historial",t.message)
                }

                override fun onResponse(call: Call<trip>, response: Response<trip>) {
                    try{
                        arv_refresh!!.isRefreshing=false
                    }catch (e:Exception){

                    }
                    if (response.isSuccessful){
                        dataArray=response.body()!!.datos
                        Log.e("historial",response.body()!!.datos.toString())
                        adapterCreate.setData(dataArray!!)
                    }else{
                        Log.e("historial",response.code().toString())
                    }
                }

            })
        }


}