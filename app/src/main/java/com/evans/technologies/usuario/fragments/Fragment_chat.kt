package com.evans.technologies.usuario.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.evans.technologies.usuario.Activities.MainActivity
import com.evans.technologies.usuario.R
import com.evans.technologies.usuario.Utils.*
import com.evans.technologies.usuario.Utils.Adapters.adapter_rv_chat
import com.evans.technologies.usuario.Utils.timeCallback.OnclickItemListener
import com.evans.technologies.usuario.model.chats
import com.evans.technologies.usuario.model.sugerenciasLocale
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_fragment_chat.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class Fragment_chat : Fragment(),
    View.OnClickListener {
    private var adapterRview: RecyclerView.Adapter<*>? = null
    lateinit var  viewModel :viewModelMain

    var prefs: SharedPreferences? = null
    var dataDriver: SharedPreferences? = null
    var receiver: BroadcastReceiver? = null

    lateinit var chatAdapter:adapter_rv_chat
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? { // Inflate the layout for this fragment
        viewModel = requireActivity().run {
            ViewModelProviders.of(this)[viewModelMain::class.java]
        }
        return inflater.inflate(R.layout.fragment_fragment_chat, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        dataDriver = requireContext().getSharedPreferences("datadriver", Context.MODE_PRIVATE)


        ffc_btn_enviar_msg!!.setOnClickListener(this)
        ffc_rv_lista_chats!!.layoutManager = LinearLayoutManager(context)
        //ffc_rv_lista_chats.smoothScrollToPosition(adapterRview.getItemCount());
         chatAdapter=adapter_rv_chat(
            context,
            R.layout.dialog_layout_rv_chat_user,
            R.layout.dialog_layout_rv_chat_other,
            object : OnclickItemListener {
                override fun itemClickNotify(
                    notificaciones: Map<String, Any>,
                    adapterPosition: Int
                ) {
                }

                override fun itemClickChat(chat: chats, adapterPosition: Int) {}
                override fun itemClickSuggestion(
                    sugerenciasLocale: sugerenciasLocale,
                    adapterPosition: Int,
                    tipe_data: Boolean
                ) {
                }
            })
        adapterRview = chatAdapter
        ffc_rv_lista_chats!!.adapter = adapterRview
        escucharChat()
    }

    private fun escucharChat() {
        viewModel.chatValue.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it){
                is Resource.Success->{
                    var mensajes = ArrayList<chats>()
                    for (dato in it.data.children) {
                        val android = dato.getValue(chats::class.java)!!
                        mensajes.add(android)
                    }
                    Log.e("chats",mensajes.toString())
                    chatAdapter.setDataChat(mensajes)
                    try {
                       // ffc_rv_lista_chats!!.smoothScrollToPosition(adapterRview!!.itemCount - 1)
                    } catch (e: Exception) {
                    }
                }
                is Resource.Failure->{
                    Toast.makeText(requireContext(),it.exception.message,Toast.LENGTH_LONG).show()
                }
            }
        })
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.ffc_btn_enviar_msg -> if (!ffc_edit_txt_mensaje_send!!.text.toString().isEmpty()) {
                try {
                    val view = requireActivity().currentFocus
                    requireView().clearFocus()
                    if (view != null) {
                        val imm =
                            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                } catch (e: Exception) {
                }
                val send = ffc_edit_txt_mensaje_send!!.text.toString().trim { it <= ' ' }
                val id = getUserId_Prefs(prefs!!)
                val nombre = getUserName(prefs!!)
                val date = Date()
                val hourFormat: DateFormat =
                    SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                val map: MutableMap<String, Any?> =
                    HashMap()
                map["mensajeChat"] = send
                map["nombreChat"] = nombre
                map["idUserChat"] = id
                map["fechaChat"] = hourFormat.format(date)
                FirebaseDatabase.getInstance().reference.child("chatsFirebase")
                    .child(getDriverId(dataDriver!!)!!).push().setValue(map)
                ffc_edit_txt_mensaje_send!!.setText("")
            } else {
                requireActivity().toastLong("No se puede enviar mensajes vacios")
            }
        }
    }
}