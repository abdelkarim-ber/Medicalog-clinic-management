package com.example.android.clinicmanagement.patientsList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.clinicmanagement.R


class LoadStateAdapter : LoadStateAdapter<com.example.android.clinicmanagement.patientsList.LoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        LoadStateViewHolder(parent)

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) =
        holder.bind(loadState)


    class LoadStateViewHolder(
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_load_state, parent, false)
    ) {
        private val loadingImage:View = itemView.findViewById(R.id.img_loading_anim)

        fun bind(loadState: LoadState) {
            loadingImage.visibility = toVisibility(loadState is LoadState.Loading)
        }

        private fun toVisibility(constraint: Boolean): Int = if (constraint) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }



}