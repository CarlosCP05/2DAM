package com.example.pruebas.personajes.ui.personajes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmprueba.databinding.ItemPersonajeBinding
import com.example.pruebas.personajes.domain.model.Personaje

class PersonajeAdapter (
    val delPersonaje: (Personaje) -> Unit,
): ListAdapter<Personaje, PersonajeAdapter.PersonajeViewHolder>(PersonajeDiffCallback())
{
    override fun onCreateViewHolder(
        parent: ViewGroup,
            viewType: Int
        ): PersonajeViewHolder {
        val binding = ItemPersonajeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PersonajeViewHolder(binding, delPersonaje)
    }

    override fun onBindViewHolder(
        holder: PersonajeViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }


    class PersonajeViewHolder(
        private val bindign: ItemPersonajeBinding,
        private val onClickDeleted: (Personaje) -> Unit,
    ): RecyclerView.ViewHolder(bindign.root)
    {
        fun bind(personaje: Personaje) {
            with(bindign) {
                txtNombrePersonaje.text = personaje.nombre
                // No tengo boton para borrar
                root.setOnClickListener {
                    onClickDeleted(personaje)
                }
            }
        }
    }
}

class PersonajeDiffCallback: DiffUtil.ItemCallback<Personaje>() {
    override fun areItemsTheSame(oldItem: Personaje, newItem: Personaje): Boolean = oldItem.nombre == newItem.nombre
    override fun areContentsTheSame(oldItem: Personaje, newItem: Personaje): Boolean = oldItem == newItem
}