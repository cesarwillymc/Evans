package com.evans.technologies.usuario.Activities.viajes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.evans.technologies.usuario.R
import com.evans.technologies.usuario.Utils.Adapters.adapter_rv_cupon
import com.evans.technologies.usuario.Utils.Adapters.adapter_rv_cupon.click
import com.evans.technologies.usuario.Utils.Adapters.adapter_rv_trip
import com.evans.technologies.usuario.model.DataCupon
import kotlinx.android.synthetic.main.activity_rv_viajes.*
import java.util.*

class ListaViajes : Fragment() {

    var cupones = ArrayList<DataCupon>()
    private var adapterRview: RecyclerView.Adapter<*>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_rv_viajes,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        arv_viajes!!.layoutManager = LinearLayoutManager(requireContext())
        adapterRview =
            adapter_rv_trip(
                requireContext(),
                R.layout.dialog_historial_viajes,
                adapter_rv_trip.OnItemClickListener { vaije, position ->
                    //rv_viajesDirections
                    //val bundle = ListaViajesDirections.actionRvViajes2ToHistorialViaje(vaije)
                   // findNavController().navigate(bundle)
                })
        arv_viajes!!.adapter = adapterRview
        arv_refresh!!.setOnRefreshListener {
            findNavController().navigate(R.id.action_rv_viajes2_to_historial_viaje)
        }

    }
        //,ListaViajesDirections()


}