package com.example.android.clinicmanagement.expenses

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.data.models.Session
import com.example.android.clinicmanagement.databinding.ListItemExpenditureCategorySelectionBinding
import com.google.android.material.card.MaterialCardView

class ExpensesCategoryAdapter(private val expensesTypeClickListener: ExpensesTypeClickListener) : RecyclerView.Adapter<ExpensesCategoryAdapter.ViewHolder>() {

    val data = ExpensesType.values()

    override fun getItemCount() = data.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position],expensesTypeClickListener,position)
    }


    class ViewHolder private constructor(val binding: ListItemExpenditureCategorySelectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ExpensesType,expensesTypeClickListener: ExpensesTypeClickListener,position: Int) {
            binding.expensesType = data
            binding.expensesTypeClickListener = expensesTypeClickListener
            binding.itemPosition = position
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: ListItemExpenditureCategorySelectionBinding = DataBindingUtil.inflate(
                    layoutInflater, R.layout.list_item_expenditure_category_selection, parent, false
                )

                return ViewHolder(binding)
            }
        }

    }


    fun interface ExpensesTypeClickListener {
        fun onClick(position: Int, expensesNumber: Int)
    }




}


