package com.example.android.clinicmanagement.statistics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.data.models.TotalSpentByCategory
import com.example.android.clinicmanagement.databinding.ListItemExpenditureCategoryBinding

class ExpenditureListAdapter : ListAdapter<TotalSpentByCategory,
        ExpenditureListAdapter.ViewHolder>(ExpenseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class ViewHolder private constructor(val binding: ListItemExpenditureCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TotalSpentByCategory) {
            binding.totalSpentByCategory = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: ListItemExpenditureCategoryBinding = DataBindingUtil.inflate(
                    layoutInflater, R.layout.list_item_expenditure_category, parent, false
                )
                return ViewHolder(binding)
            }
        }

    }

    class ExpenseDiffCallback : DiffUtil.ItemCallback<TotalSpentByCategory>() {
        override fun areItemsTheSame(
            oldItem: TotalSpentByCategory,
            newItem: TotalSpentByCategory
        ): Boolean {
            return oldItem.expendCategory == newItem.expendCategory
        }

        override fun areContentsTheSame(
            oldItem: TotalSpentByCategory,
            newItem: TotalSpentByCategory
        ): Boolean {
            return oldItem == newItem
        }
    }
}