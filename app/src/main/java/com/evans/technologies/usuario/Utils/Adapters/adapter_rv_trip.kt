package com.evans.technologies.usuario.Utils.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.evans.technologies.usuario.R
import com.evans.technologies.usuario.model.modelTrip.Historial

class adapter_rv_trip(
    var context: Context,
    var layoutResources: Int,
    private val Listen: OnItemClickListener
) : RecyclerView.Adapter<adapter_rv_trip.mismensajes>() {
    var viaje: List<Historial>? = null
    var user: View? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): mismensajes {
        user = LayoutInflater.from(context).inflate(layoutResources, parent, false)
        return mismensajes(user!!)
    }

    fun setData(viaje: List<Historial>?) {
        this.viaje = viaje
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(
        holder: mismensajes,
        position: Int
    ) {
        holder.bind(viaje!![position])
    }

    override fun getItemCount(): Int {
        return if (viaje == null) {
            0
        } else viaje!!.size
    }

    inner class mismensajes(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var origen: TextView
        var fecha: TextView
        var hora: TextView
        var estado: TextView
        var vermas: TextView
        var destino: TextView
        fun bind(mismensajes: Historial) {
            origen.text = "De:  ${mismensajes.startAddress}"
            fecha.text= mismensajes.dateTrip
            hora.visibility=View.GONE
            destino.text= "A: ${mismensajes.destinationAddress}"
            if (mismensajes.tripFinalized) {
                estado.text="Estado: Viaje Finalizado"
            }else{
                estado.text="Estado: Viaje Cancelado"
            }

            itemView.setOnClickListener { Listen.OnClickListener(mismensajes, adapterPosition) }
        }

        init {
            origen = itemView.findViewById(R.id.dhv_txt_origen)
            fecha = itemView.findViewById(R.id.dhv_txt_fecha)
            hora = itemView.findViewById(R.id.dhv_txt_hora)
            estado = itemView.findViewById(R.id.dhv_txt_estado)
            vermas = itemView.findViewById(R.id.dhv_txt_detalle)
            destino = itemView.findViewById(R.id.dhv_txt_destino)
            //
        }
    }

    interface OnItemClickListener {
        fun OnClickListener(vaije: Historial?, position: Int)
    }

}