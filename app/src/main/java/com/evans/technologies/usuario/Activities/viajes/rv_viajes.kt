package com.evans.technologies.usuario.Activities.viajes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.activity_rv_viajes.*
import java.util.*

class rv_viajes : Fragment() {

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
            adapter_rv_cupon(
                requireContext(),
                R.layout.dialog_rv_cupon,
                "cupon",
                cupones,
                click { data, adapterPosition -> })
        arv_viajes!!.adapter = adapterRview
        arv_refresh!!.setOnRefreshListener {
            // Esto se ejecuta cada vez que se realiza el gesto
// getCuponesData();
        }
    }

}