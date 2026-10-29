package com.example.pruebas.personajes.ui.main

import android.os.Bundle
import android.view.WindowInsets
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmprueba.R

import com.example.mvvmprueba.databinding.MainPersonajesBinding
import com.example.pruebas.personajes.ui.main.MainViewModel
import com.example.pruebas.personajes.di.AppModule
import com.example.pruebas.personajes.domain.model.Personaje
import com.example.pruebas.personajes.ui.personajes.PersonajeAdapter


class MainActivity : AppCompatActivity() {
    private val binding: MainPersonajesBinding by lazy { MainPersonajesBinding.inflate(layoutInflater) }

    private val viewModel : MainViewModel by viewModels { MainViewModelFactory(
        AppModule.dameMentirosoUseCase,
        AppModule.obtenerPersonajesUseCase,
        AppModule.addPersonajeUseCase,
        AppModule.delPersonajeUseCase,
    ) }

    private lateinit var adapter: PersonajeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewModel.handleObtenerPersonajes()

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        configRecycler()
        observarEstado()
        setupEventos()
    }

    private fun configRecycler() {
        binding.listado.layoutManager = LinearLayoutManager(this)

        adapter = PersonajeAdapter({personaje ->
            viewModel.handleDel(personaje)
        })
        binding.listado.adapter = adapter
    }

    private fun observarEstado() {
        viewModel.state.observe(this, {state ->
            state?.mentiroso.let{
                //No tengo un textView configurado en el xml
            // binding.textView.text = it
            }

            state?.personajes.let{
                adapter.submitList(it)
            }

            state?.error?.let { error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                viewModel.handleLimpiarError()
            }
        })
    }

    private fun setupEventos(){
        with(binding) {
            /* No tengo un boton en el XML
            button.setOnClickListener{
                viewModel.handleDameMentiroso()
                viewModel.handleObtenerPersonajes()
            }

            buttonAdd.setOnClickListener {
                var p = Personaje(nombre = "Oveja", true)

                viewModel.handleAdd(p)
            }
            */
        }
    }
}