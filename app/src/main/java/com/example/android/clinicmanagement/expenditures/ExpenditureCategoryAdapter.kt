package com.example.android.clinicmanagement.expenditures

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.ListItemExpenditureCategorySelectionBinding
import com.example.android.clinicmanagement.expenditures.ExpenditureCategoryAdapter.ViewHolder.Companion.previousItemPosition

class ExpenditureCategoryAdapter : RecyclerView.Adapter<ExpenditureCategoryAdapter.ViewHolder>() {


    private var data = listOf(
        "Utilities",
        "Rent",
        "Repair",
        "Insurance",
        "Wages",
        "Tax",
        "Supplies",
        "Divers"
    )

    override fun getItemCount() = data.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }


    class ViewHolder private constructor(val binding: ListItemExpenditureCategorySelectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String) {
            binding.imageIcon.setImageResource(
                when (data) {
                    "Utilities" -> R.drawable.ic_expenditure_receipt
                    "Rent" -> R.drawable.ic_expenditure_home
                    "Repair" -> R.drawable.ic_expenditure_home_repair
                    "Insurance" -> R.drawable.ic_expenditure_insurance
                    "Wages" -> R.drawable.ic_expenditure_wallet
                    "Tax" -> R.drawable.ic_expenditure_tax
                    "Supplies" -> R.drawable.ic_expenditure_medical_supplies
                    "Divers" -> R.drawable.ic_expenditure_divers
                    else -> R.drawable.ic_expenditure_divers
                }
            )
            binding.textExpenditureCategory.text = data
            binding.cardView.setOnClickListener {
                selectionColor?.let { color ->
                    it.backgroundTintList = ColorStateList.valueOf(color)
                }

                Log.v("recyclerview","previousitemposition $previousItemPosition ")
                previousItemPosition?.let {
                    val viewHolder = recyclerView?.findViewHolderForAdapterPosition(it)
                    viewHolder?.itemView?.findViewById<View>(R.id.cardView)?.backgroundTintList = null
                    Log.v("recyclerview","recyclerview ${if(recyclerView == null) "null" else "not null"}")
                    Log.v("recyclerview","viewHolder ${if(viewHolder == null) "null" else "not null"}")
                    Log.v("recyclerview","viewHolder.itemView ${if(viewHolder?.itemView == null) "null" else "not null"}")
                }
                selectedItem = data
                previousItemPosition = bindingAdapterPosition

            }
        }

        companion object {
            var selectionColor: Int? = null
            var recyclerView: RecyclerView? = null
            var previousItemPosition: Int? = null
            var selectedItem :String? = null
            fun from(parent: ViewGroup): ViewHolder {
                if (recyclerView == null) {
                    recyclerView = parent as RecyclerView

                    selectionColor = Color.parseColor(
                        "#" + Integer.toHexString(
                            ContextCompat.getColor(
                                parent.context,
                                R.color.color_secondary_variant_20
                            )
                        )
                    )

                }
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: ListItemExpenditureCategorySelectionBinding = DataBindingUtil.inflate(
                    layoutInflater, R.layout.list_item_expenditure_category_selection, parent, false
                )

                return ViewHolder(binding)
            }
            fun clean(){
                selectionColor= null
                recyclerView= null
                previousItemPosition = null
                selectedItem = null
            }
        }

    }
}


