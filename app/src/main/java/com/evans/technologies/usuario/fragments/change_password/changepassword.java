package com.evans.technologies.usuario.fragments.change_password;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.evans.technologies.usuario.Activities.LoginActivity;
import com.evans.technologies.usuario.R;
import com.evans.technologies.usuario.Retrofit.RetrofitClient;
import com.evans.technologies.usuario.model.user;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.evans.technologies.usuario.Utils.UtilsKt.getIDrecuperar;
import static com.evans.technologies.usuario.Utils.UtilsKt.gettokenrecuperar;
import static com.evans.technologies.usuario.Utils.UtilsKt.toastLong;
import static org.jetbrains.anko.ToastsKt.toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class changepassword extends Fragment {

    View view;
    SharedPreferences navFragment;
    @BindView(R.id.fcp_btn_confirm)
    Button next;
    @BindView(R.id.fcp_edtxt_pw1)
    EditText pw1;
    @BindView(R.id.fcp_edtxt_pw2)
    EditText pw2;
    String p1,p2;
    @BindView(R.id.progressBar_pass)
    ProgressBar progressbar;
    public changepassword() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navFragment= getContext().getSharedPreferences("navFragment", Context.MODE_PRIVATE);
        view=  inflater.inflate(R.layout.fragment_changepassword, container, false);
        ButterKnife.bind(this,view);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p1=pw1.getText().toString().trim();
                p2=pw2.getText().toString().trim();
                if (comprobarcampos()){
                    progressbar.setVisibility(View.VISIBLE);
                    Call<user> sendCorreo= RetrofitClient.getInstance().getApi().sendContraseña_recuperar(getIDrecuperar(navFragment),gettokenrecuperar(navFragment),p1);
                    sendCorreo.enqueue(new Callback<user>() {
                        @Override
                        public void onResponse(Call<user> call, Response<user> response) {
                            Log.e("change_set",response.code()+"");
                            if (response.isSuccessful()){
                                progressbar.setVisibility(View.GONE);
                                toastLong(getActivity(),"La contraseña se cambio");
                                navFragment.edit().clear().apply();
                                Intent intent = new Intent(getContext(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else{
                                progressbar.setVisibility(View.GONE);
                                toast(getActivity(),"Contraseña no valida");
                            }
                        }

                        @Override
                        public void onFailure(Call<user> call, Throwable t) {
                            progressbar.setVisibility(View.GONE);
                        }
                    });
                }
            }


        });
        return view;
    }

    private boolean comprobarcampos() {
        if (p1.isEmpty()||p2.isEmpty()){
            pw1.setError("Los campos estan vacios");
            pw2.setError("Los campos estan vacios");
            return false;
        }
        if (p1.length()<6){
            pw1.setError("La contraseña no puede ser menor a 6 digitos");
            return false;
        }
        if (p2.length()<6){
            pw2.setError("La contraseña no puede ser menor a 6 digitos");
            return false;
        }
        if (!p1.equals(p2)){
            pw1.setError("Las contraseñas no coinciden");
            pw2.setError("Las contraseñas no coinciden");
            return false;
        }

        return true;
    }

}
