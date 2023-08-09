package com.example.android.clinicmanagement.expensesHistory

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.data.models.Expenditure
import com.example.android.clinicmanagement.data.models.Session
import com.example.android.clinicmanagement.databinding.ListItemExpensesHistoryBinding


class ExpensesHistoryAdapter : PagingDataAdapter<Expenditure, ExpensesHistoryAdapter.ViewHolder>(EXPENSES_DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }


    class ViewHolder private constructor(val binding: ListItemExpensesHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Expenditure) {
            binding.expenditure = data
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: ListItemExpensesHistoryBinding = DataBindingUtil.inflate(
                    layoutInflater, R.layout.list_item_expenses_history, parent, false
                )

                return ViewHolder(binding)
            }
        }

    }

    companion object {
        private val EXPENSES_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Expenditure>() {
            override fun areItemsTheSame(oldItem: Expenditure, newItem: Expenditure): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Expenditure,
                newItem: Expenditure
            ): Boolean {
                return oldItem == newItem
            }
        }
    }






}
