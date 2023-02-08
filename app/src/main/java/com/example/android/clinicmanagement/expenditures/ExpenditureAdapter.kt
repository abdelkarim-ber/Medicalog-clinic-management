package com.example.android.clinicmanagement.expenditures

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.ListItemExpenditureCategoryBinding

class ExpenditureAdapter : RecyclerView.Adapter<ExpenditureAdapter.ViewHolder>() {
    var data = listOf<Expenditure>()
    set(value){
        field = value
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data.get(position))
    }


    class ViewHolder private constructor(val binding: ListItemExpenditureCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Expenditure) {
            binding.imageIcon.setImageResource(when(data.category){
                "Utilities" -> R.drawable.ic_expenditure_receipt
                "Rent" -> R.drawable.ic_expenditure_home
                "Repair" -> R.drawable.ic_expenditure_home_repair
                "Insurance" -> R.drawable.ic_expenditure_insurance
                "Wages" -> R.drawable.ic_expenditure_wallet
                "Tax" -> R.drawable.ic_expenditure_tax
                "Supplies" -> R.drawable.ic_expenditure_medical_supplies
                "Divers" -> R.drawable.ic_expenditure_divers
                else -> R.drawable.ic_expenditure_divers
            })
            binding.textExpenditureCategory.text = data.category
            binding.textExpenditureAmount.text = "${data.amount} DH"
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


}