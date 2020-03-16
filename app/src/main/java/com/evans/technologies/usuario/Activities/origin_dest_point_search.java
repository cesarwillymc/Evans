package com.evans.technologies.usuario.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.evans.technologies.usuario.R;
import com.evans.technologies.usuario.Retrofit.RetrofitClient;
import com.evans.technologies.usuario.Utils.Adapters.adapter_rv_suggestion;
import com.evans.technologies.usuario.Utils.timeCallback.OnclickItemListener;
import com.evans.technologies.usuario.model.chats;
import com.evans.technologies.usuario.model.sugerenciasLocale;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.evans.technologies.usuario.Utils.UtilsKt.setDataActivityOrigin;
import static com.evans.technologies.usuario.Utils.UtilsKt.toastLong;

public class origin_dest_point_search extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.aodps_imgbtn_back)
    ImageButton imgbtn_back;
    @BindView(R.id.aodps_imgbtn_save)
    ImageButton imgbtn_save;
    @BindView(R.id.aodps_edtxt_destino)
    EditText edtxt_destino;
    @BindView(R.id.aodps_edtxt_origin)
    EditText edtxt_origin;
    @BindView(R.id.aodps_txt_name)
    TextView txt_name;
    @BindView(R.id.aodps_rv_options)
    RecyclerView rv_options;
    @BindView(R.id.aodps_rv_suggestions)
    RecyclerView rv_suggestions;
    BroadcastReceiver searchSuggestion;
    private RecyclerView.Adapter adapterRview_options;
    private RecyclerView.Adapter adapterRview_suggestion;
    ArrayList<sugerenciasLocale> sugerenciasLocales_options=new ArrayList<>();
    ArrayList<sugerenciasLocale> sugerenciasLocales_suggestion=new ArrayList<>();
    Boolean statusorigin=true,statusdest=false;
    sugerenciasLocale segg_origin=new sugerenciasLocale();
    sugerenciasLocale segg_dest= new sugerenciasLocale();
    SharedPreferences datadriver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_origin_dest_point_search);
        datadriver = getSharedPreferences("datadriver", Context.MODE_PRIVATE);
        ButterKnife.bind(this);

        getDataIntent();
        try{
            View view = getCurrentFocus();
            view.clearFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE) ;
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }catch (Exception e){

        }
        imgbtn_back.setOnClickListener(this);
        imgbtn_save.setOnClickListener(this);
        traerData_suggestion();
        traerData_options();
        edtxt_destino.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edtxt_destino.setTextColor(getColor(R.color.black));
                edtxt_origin.setTextColor(getColor(R.color.plomo));
                statusdest=true;
                statusorigin=false;
                if (s.length()>0){
                    Intent intent = new Intent("searchSuggestion");
                    intent.putExtra("searchSuggestionString",s);
                    LocalBroadcastManager broadcaster= LocalBroadcastManager.getInstance(origin_dest_point_search.this);
                    broadcaster.sendBroadcast(intent);
                }
            }
        });
        edtxt_origin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                statusdest=false;
                statusorigin=true;
                edtxt_destino.setTextColor(getColor(R.color.plomo));
                edtxt_origin.setTextColor(getColor(R.color.black));
                if (s.length()>0){

                }
            }
        });
        final View activityRootView = findViewById(R.id.activity_root);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > dpToPx(origin_dest_point_search.this, 200)) { // if more than 200 dp, it's probably a keyboard...
                    rv_options.setVisibility(View.GONE);
                }else{
                    rv_options.setVisibility(View.VISIBLE);
                }
            }
        });
        cargarAdapter();
        cargarAdapter2();
        searchSuggestion = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String datassss=intent.getExtras().getString("data");
                Log.e("mensajeNotify", "llego "+datassss);
                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                ArrayList<sugerenciasLocale> datanew=new ArrayList<>();
                try {
                    jsonObject = new JSONObject(datassss);
                    jsonArray = jsonObject.getJSONArray("suggestion");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            Gson gson = new Gson();
                            sugerenciasLocale topic = gson.fromJson(jsonArray.get(i).toString(), sugerenciasLocale.class);
                            Log.e("chats", "array \n"+jsonArray.get(i));
                            datanew.add( topic);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    sugerenciasLocales_suggestion=datanew;
                    try {
                        adapterRview_suggestion.notifyDataSetChanged();
                    }catch (Exception e){

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("chats", "error \n"+e.getMessage());
                }
            }
        };
    }
    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }
    private void getDataIntent() {
        if (getIntent().getExtras()!=null){
            if (!getIntent().getStringExtra("name_origin").isEmpty()){
                edtxt_origin.setText(getIntent().getStringExtra("name_origin"));
                edtxt_origin.setTextColor(getColor(R.color.plomo));
                statusorigin=true;
            }else if (!getIntent().getStringExtra("name_dest").isEmpty()){
                edtxt_destino.setText(getIntent().getStringExtra("name_dest"));
                edtxt_destino.setTextColor(getColor(R.color.plomo));
                statusdest=true;
            }
        }

    }

    private void traerData_suggestion() {
        Call<ArrayList<sugerenciasLocale>> traer_suggestion= RetrofitClient.getInstance().getApi().suggestion_user_direction();
        traer_suggestion.enqueue(new Callback<ArrayList<sugerenciasLocale>>() {
            @Override
            public void onResponse(Call<ArrayList<sugerenciasLocale>> call, Response<ArrayList<sugerenciasLocale>> response) {
                sugerenciasLocales_suggestion=response.body();
            }

            @Override
            public void onFailure(Call<ArrayList<sugerenciasLocale>> call, Throwable t) {

            }
        });

    }

    private void traerData_options() {
        sugerenciasLocale uGuardadas=new sugerenciasLocale();
        uGuardadas.setTitulo("Ubicaciones guardadas");
        uGuardadas.setTipe("save");
        sugerenciasLocale uTop=new sugerenciasLocale();
        uTop.setTitulo("Ubicaciones top");
        uTop.setTipe("top");
        sugerenciasLocale addDirecction=new sugerenciasLocale();
        addDirecction.setTitulo("Agregar direccion");
        addDirecction.setTipe("add");
        sugerenciasLocales_options.add(uTop);
        sugerenciasLocales_options.add(uGuardadas);
        sugerenciasLocales_options.add(addDirecction);

    }

    private void cargarAdapter() {
        rv_options.setLayoutManager( new LinearLayoutManager(this) );
        adapterRview_options = new adapter_rv_suggestion(this, R.layout.dialog_suggestion_direction,"options",true, sugerenciasLocales_options, new OnclickItemListener() {
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
        rv_options.setAdapter(adapterRview_options);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((searchSuggestion),
                new IntentFilter("origin_dest_point_search")
        );
    }

    private void cargarAdapter2() {
        rv_suggestions.setLayoutManager( new LinearLayoutManager(this) );
        adapterRview_suggestion = new adapter_rv_suggestion(this, R.layout.dialog_suggestion_direction,"suggestion",statusorigin, sugerenciasLocales_suggestion, new OnclickItemListener() {
            @Override
            public void itemClickNotify(Map<String, Object> notificaciones, int adapterPosition) {


            }

            @Override
            public void itemClickChat(chats chat, int adapterPosition) {

            }

            @Override
            public void itemClickSuggestion(sugerenciasLocale sugerenciasLocale, int adapterPosition, Boolean status_origin) {
                if (status_origin){
                    edtxt_origin.setText(sugerenciasLocale.getTitulo());
                    segg_origin=sugerenciasLocale;
                }else{
                    edtxt_destino.setText(sugerenciasLocale.getTitulo());
                    segg_dest=sugerenciasLocale;
                }
            }
        });
        rv_suggestions.setAdapter(adapterRview_suggestion);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.aodps_imgbtn_back:
                finish();
                break;
            case R.id.aodps_imgbtn_save:
                if (getIntent().getStringExtra("name_origin").isEmpty()&&getIntent().getStringExtra("name_dest").isEmpty()){
                    if (segg_dest!=null&&segg_origin!=null){
                        setDataActivityOrigin(datadriver,true);
                    }else{
                        toastLong(this,"complete los campos por favor");
                    }
                }else{
                    if (getIntent().getStringExtra("name_origin").isEmpty()){
                        if (segg_dest!=null){
                            setDataActivityOrigin(datadriver,true);
                        }else{
                            toastLong(this,"complete el destino por favor");
                        }
                    }else{
                        if (segg_origin!=null){
                            setDataActivityOrigin(datadriver,true);
                        }else{
                            toastLong(this,"complete el origen por favor");
                        }
                    }
                }

                break;
        }
    }
}
