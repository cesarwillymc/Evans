package com.evans.technologies.usuario.Activities.cupon

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.evans.technologies.usuario.R
import com.evans.technologies.usuario.Retrofit.RetrofitClient
import com.evans.technologies.usuario.Utils.Adapters.adapter_rv_cupon
import com.evans.technologies.usuario.Utils.Adapters.adapter_rv_cupon.click
import com.evans.technologies.usuario.Utils.getUserId_Prefs
import com.evans.technologies.usuario.Utils.toastLong
import com.evans.technologies.usuario.model.DataCupon
import com.evans.technologies.usuario.model.data
import kotlinx.android.synthetic.main.activity_cupones.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class Cupones : Fragment() {
    private var adapterRview: RecyclerView.Adapter<*>? = null

    var cupones = ArrayList<DataCupon>()
    var prefs: SharedPreferences? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_cupones, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE)

        ac_rv_cupones!!.layoutManager = LinearLayoutManager(requireContext())
        adapterRview =
            adapter_rv_cupon(
                requireContext(),
                R.layout.dialog_rv_cupon,
                "cupon",
                cupones,
                click { data, adapterPosition -> })
        ac_rv_cupones!!.adapter = adapterRview
        cuponesData
        swipeRefresh!!.setOnRefreshListener {
            // Esto se ejecuta cada vez que se realiza el gesto
            cuponesData
        }
    }
    private val cuponesData: Unit
        private get() {
            val cuponesAll =
                RetrofitClient.getInstance().api.getListCoupon(getUserId_Prefs(prefs!!)!!)
            cuponesAll.enqueue(object : Callback<data> {
                override fun onResponse(
                    call: Call<data>,
                    response: Response<data>
                ) {
                    Log.e(
                        "getCupon",
                        response.code().toString() + "  " + response.message()
                    )
                    swipeRefresh!!.isRefreshing = false
                    if (response.isSuccessful) {
                        try {
                            cupones.clear()
                            ac_rv_cupones!!.visibility = View.VISIBLE
                            cupones.addAll(response.body()!!.messageCoupon)
                            adapterRview!!.notifyDataSetChanged()
                        } catch (e: Exception) {
                            activity!!.toastLong("Error al cargar los cupones")
                        }
                    }
                }

                override fun onFailure(
                    call: Call<data>,
                    t: Throwable
                ) {
                    swipeRefresh!!.isRefreshing = false
                    Log.e("getCupon", t.message)
                }
            })
        }
}