package com.evans.technologies.usuario.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.evans.technologies.usuario.Activities.MainActivity;
import com.evans.technologies.usuario.R;
import com.evans.technologies.usuario.Utils.Adapters.adapter_rv_chat;
import com.evans.technologies.usuario.Utils.timeCallback.ComunicateFrag;
import com.evans.technologies.usuario.Utils.timeCallback.OnclickItemListener;
import com.evans.technologies.usuario.model.chats;
import com.evans.technologies.usuario.model.sugerenciasLocale;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.evans.technologies.usuario.Utils.UtilsKt.getDriverId;
import static com.evans.technologies.usuario.Utils.UtilsKt.getUserId_Prefs;
import static com.evans.technologies.usuario.Utils.UtilsKt.getUserName;
import static com.evans.technologies.usuario.Utils.UtilsKt.setClaseActual;
import static com.evans.technologies.usuario.Utils.UtilsKt.toastLong;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_chat extends Fragment implements View.OnClickListener {

    private View view;
    private RecyclerView.Adapter adapterRview;
    @BindView(R.id.ffc_rv_lista_chats)
    RecyclerView rv_chat;
    @BindView(R.id.ffc_btn_enviar_msg)
    ImageButton send_chat;
    @BindView(R.id.ffc_btn_select_photo)
    ImageButton select_photo;
    @BindView(R.id.ffc_edit_txt_mensaje_send)
    EditText typing_mensaje;

    SharedPreferences prefs,dataDriver;
    BroadcastReceiver receiver;
    ArrayList<chats> mensajes= new ArrayList<>(  );


    public Fragment_chat(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        prefs = getContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        dataDriver = getActivity().getSharedPreferences("datadriver", Context.MODE_PRIVATE);
        setClaseActual(prefs,new Fragment_chat().toString());
        view= inflater.inflate(R.layout.fragment_fragment_chat, container, false);
        ButterKnife.bind(this,view);
        send_chat.setOnClickListener(this);
        rv_chat.setLayoutManager( new LinearLayoutManager(getContext()) );

        //rv_chat.smoothScrollToPosition(adapterRview.getItemCount());
        adapterRview = new adapter_rv_chat(getContext(), R.layout.dialog_layout_rv_chat_user,R.layout.dialog_layout_rv_chat_other, mensajes, new OnclickItemListener() {
            @Override
            public void itemClickNotify(Map<String, Object> notificaciones, int adapterPosition) {

            }

            @Override
            public void itemClickChat(chats chat, int adapterPosition) {

            }

            @Override
            public void itemClickSuggestion(sugerenciasLocale sugerenciasLocale, int adapterPosition, Boolean tipe_data) {

            }
        });

        rv_chat.setAdapter(adapterRview);
        ((MainActivity) Objects.requireNonNull(getActivity())).updateComuChat(new ComunicateFrag.chat() {

            @Override
            public void sendDtaMessage(DataSnapshot dataSnapshot) {
                llenarData(dataSnapshot);
            }
        });
        return view;
    }

    private void llenarData(DataSnapshot dataSnapshot) {
        if (dataSnapshot!=null){
            if (dataSnapshot.exists()){
                mensajes.clear();
                for (DataSnapshot dato:dataSnapshot.getChildren()){
                    chats android= dato.getValue(chats.class);
                    mensajes.add(android);
                }
                adapterRview.notifyDataSetChanged();
                try {
                    rv_chat.smoothScrollToPosition(adapterRview.getItemCount()-1);
                }catch (Exception e){

                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ffc_btn_enviar_msg:
                if (!typing_mensaje.getText().toString().isEmpty()) {
                    try{
                        View view = getActivity().getCurrentFocus();
                        view.clearFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    }catch (Exception e ){

                    }
                    String send=typing_mensaje.getText().toString().trim();
                    String id=getUserId_Prefs(prefs);
                    String nombre= getUserName(prefs);
                    Date date = new Date();
                    DateFormat hourFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("mensajeChat", send );
                    map.put("nombreChat", nombre);
                    map.put("idUserChat", id);
                    map.put("fechaChat", hourFormat.format(date));
                    FirebaseDatabase.getInstance().getReference().child("chatsFirebase").child(getDriverId(dataDriver)).push().setValue(map);
                    typing_mensaje.setText("");
                }else{
                    toastLong(getActivity(),"No se puede enviar mensajes vacios");
                }

                break;
        }
    }
}
