package com.evans.technologies.usuario.Activities.cupon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.evans.technologies.usuario.R;
import com.evans.technologies.usuario.Retrofit.RetrofitClient;
import com.evans.technologies.usuario.Utils.Adapters.adapter_rv_cupon;
import com.evans.technologies.usuario.model.DataCupon;
import com.evans.technologies.usuario.model.data;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.evans.technologies.usuario.Utils.UtilsKt.getUserId_Prefs;
import static com.evans.technologies.usuario.Utils.UtilsKt.toastLong;

public class Cupones extends AppCompatActivity {
    @BindView(R.id.ac_imagebutton_back)
    ImageButton back;
    private RecyclerView.Adapter adapterRview;
    @BindView(R.id.ac_rv_cupones)
    RecyclerView metodos_pay;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout refreshLayout;
    ArrayList<DataCupon> cupones= new ArrayList<>();
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_cupones);
        ButterKnife.bind(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        metodos_pay.setLayoutManager( new LinearLayoutManager(Cupones.this) );
        adapterRview = new adapter_rv_cupon(this, R.layout.dialog_rv_cupon,"cupon",cupones, new adapter_rv_cupon.click() {
            @Override
            public void itemClick(DataCupon data, int adapterPosition) {

            }
        });
        metodos_pay.setAdapter(adapterRview);
        getCuponesData();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Esto se ejecuta cada vez que se realiza el gesto
                getCuponesData();
            }
        });
    }
    private void getCuponesData(){
        Call<data> cuponesAll= RetrofitClient.getInstance().getApi().getListCoupon(getUserId_Prefs(prefs));
        cuponesAll.enqueue(new Callback<data>() {
            @Override
            public void onResponse(Call<data> call, Response<data> response) {
                Log.e("getCupon",response.code()+"  "+response.message());
                refreshLayout.setRefreshing(false);
                if (response.isSuccessful()){
                    try {

                        cupones.clear();
                        metodos_pay.setVisibility(View.VISIBLE);
                        cupones.addAll(response.body().getMessageCoupon());
                        adapterRview.notifyDataSetChanged();
                    }catch (Exception e){
                        toastLong(Cupones.this,"Error al cargar los cupones");
                    }

                }
            }

            @Override
            public void onFailure(Call<data> call, Throwable t) {
                refreshLayout.setRefreshing(false);
                Log.e("getCupon",t.getMessage());
            }
        });
    }
}
