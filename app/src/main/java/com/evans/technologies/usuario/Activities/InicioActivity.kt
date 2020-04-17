package com.evans.technologies.usuario.Activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.evans.technologies.usuario.R
import kotlinx.android.synthetic.main.toolbarwhite.*

class InicioActivity : AppCompatActivity() {
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
         val  navFragment= getSharedPreferences("navFragment", Context.MODE_PRIVATE)
        navFragment.edit().clear().apply()
        navController= findNavController(R.id.ai_frag)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id){
                R.id.inicioFragment->{
                    tw_img_evans.visibility= View.VISIBLE
                    tw_img_back.visibility= View.GONE
                }
                else->{
                    tw_img_evans.visibility= View.GONE
                    tw_img_back.visibility= View.VISIBLE
                }
            }
        }
        tw_img_back.setOnClickListener {
            findNavController(R.id.ai_frag).navigateUp()
        }
    }


}
