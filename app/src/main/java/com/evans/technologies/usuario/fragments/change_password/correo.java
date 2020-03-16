package com.evans.technologies.usuario.fragments.change_password;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.evans.technologies.usuario.R;
import com.evans.technologies.usuario.Retrofit.RetrofitClient;
import com.evans.technologies.usuario.model.user;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.evans.technologies.usuario.Utils.UtilsKt.setIDrecuperar;
import static com.evans.technologies.usuario.Utils.UtilsKt.setNavFragment;
import static org.jetbrains.anko.ToastsKt.toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class correo extends Fragment {

    View view;
    SharedPreferences navFragment;
    @BindView(R.id.fc_btn_correo_ic)
    Button next;
    @BindView(R.id.c_edtxt_email_confirm)
    EditText email2;
    @BindView(R.id.c_edtxt_email)
    EditText email1;
    String e1,e2;
    @BindView(R.id.progressBar_correo)
    ProgressBar progressbar;
    public correo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navFragment= getContext().getSharedPreferences("navFragment", Context.MODE_PRIVATE);
        navFragment.edit().clear().apply();
        view= inflater.inflate(R.layout.fragment_correo, container, false);
        ButterKnife.bind(this,view);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1=email1.getText().toString().trim();
                e2=email2.getText().toString().trim();
                if (comprobarcampos()){
                    progressbar.setVisibility(View.VISIBLE);
                    Call<user> sendCorreo= RetrofitClient.getInstance().getApi().sendCorreo_recuperar(e1);
                    sendCorreo.enqueue(new Callback<user>() {
                        @Override
                        public void onResponse(Call<user> call, Response<user> response) {
                            Log.e("correo_set",response.code()+"");
                            if (response.isSuccessful()){
                                progressbar.setVisibility(View.GONE);
                                if (response.body()!=null){
                                    Log.e("correo_set",response.body().getUser());
                                    setIDrecuperar(navFragment,response.body().getUser());
                                    setNavFragment(navFragment,new correo().toString());
                                    FragmentManager manager = getActivity().getSupportFragmentManager();
                                    manager.beginTransaction().replace(R.id.recuperar_frag,
                                            new set_codigo(false,false)
                                    ).commit();
                                }
                            }else{
                                progressbar.setVisibility(View.GONE);
                                toast(getActivity(),"El correo no existe");
                            }
                        }

                        @Override
                        public void onFailure(Call<user> call, Throwable t) {
                            progressbar.setVisibility(View.GONE);
                            toast(getActivity(),"Error al enviar el codigo.");
                        }
                    });
                }
            }


        });

        return view;
    }
    private boolean comprobarcampos() {
        if (e1.isEmpty()||e2.isEmpty()){
            email1.setError("Campos vacios");
            email2.setError("Campos vacios");
            return  false;
        }
        if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(e1).matches())){
            email1.setError("No es un correo valido");
            return  false;
        }
        if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(e2).matches())){
            email2.setError("No es un correo valido");
            return  false;
        }
        if (!e1.equals(e2)){
            email1.setError("Los correos no coinciden");
            email2.setError("Los correos no coinciden");
            return  false;
        }

        return true;
    }

}
