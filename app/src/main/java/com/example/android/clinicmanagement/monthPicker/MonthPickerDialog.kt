package com.example.android.clinicmanagement.monthPicker


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.*

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.DialogFragmentMonthPickerBinding

import java.text.SimpleDateFormat
import java.util.*


class MonthPickerDialog : DialogFragment() {
    lateinit var binding: DialogFragmentMonthPickerBinding
    private val _selectedYear = MutableLiveData<String>()
    private val _selectedMonth = MutableLiveData<String>()
    private var onPositiveButtonClick: ((Long) -> Unit)? = null
    private val years = mutableListOf<String>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.example.android.clinicmanagement.R.layout.dialog_fragment_month_picker,
            container,
            false
        )

        binding.month = _selectedMonth
        binding.year = _selectedYear
        binding.setLifecycleOwner { viewLifecycleOwner.lifecycle }
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMonthPicker()
        setUpYearPicker()
        binding.buttonOk.setOnClickListener {
            onPositiveButtonClick?.invoke(getSelectedDateInSeconds())
            dismiss()
        }
        binding.buttonCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val window = dialog!!.window!!
        val params: ViewGroup.LayoutParams = window.attributes

        val dialogMargin = resources.getDimensionPixelSize(R.dimen.margin_16dp)

        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = params as WindowManager.LayoutParams
        window.setBackgroundDrawable(
            InsetDrawable(
                ColorDrawable(Color.TRANSPARENT),
                dialogMargin,
                0,
                dialogMargin,
                0
            )
        )
    }


    fun showMonthPicker(manager: FragmentManager, tag: String) {
        val ft: FragmentTransaction? = manager.beginTransaction()
        ft?.setReorderingAllowed(true)
        ft?.add(this, tag)
        ft?.commit()
    }

    private fun setUpMonthPicker() {
        val currentDate = Calendar.getInstance()

        val locale = Locale.getDefault()
        val monthFormatter = SimpleDateFormat("MMM", locale)
        val currentMonth = monthFormatter.format(currentDate.time)
        _selectedMonth.value = currentMonth

        val months = mutableListOf<String>()
        for (i in Calendar.JANUARY..Calendar.DECEMBER) {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.MONTH, i)
            months.add(monthFormatter.format(calendar.time))
        }
        binding.pickerMonth.displayedValues = months.toTypedArray()
        binding.pickerMonth.maxValue = 11
        binding.pickerMonth.minValue = 0
        binding.pickerMonth.wrapSelectorWheel = false
        binding.pickerMonth.value = months.indexOf(currentMonth)

        binding.pickerMonth.setOnValueChangedListener { picker, oldVal, newVal ->
            val selectedMonthValue = binding.pickerMonth.value
            _selectedMonth.value = months[selectedMonthValue]
        }

    }

    private fun setUpYearPicker() {
        val currentDate = Calendar.getInstance()

        val currentYear = currentDate.get(Calendar.YEAR)

        _selectedYear.value = currentYear.toString()


        for (i in 3 downTo 0) {
            years.add(currentYear.minus(i).toString())
        }

        binding.pickerYear.displayedValues = years.toTypedArray()
        binding.pickerYear.maxValue = 3
        binding.pickerYear.minValue = 0
        binding.pickerYear.wrapSelectorWheel = false
        binding.pickerYear.value = years.indexOf(currentYear.toString())

        binding.pickerYear.setOnValueChangedListener { picker, oldVal, newVal ->
            val selectedYearValue = binding.pickerYear.value
            _selectedYear.value = years[selectedYearValue]
        }

    }

    fun addOnPositiveButtonClickListener(onPositiveButtonClick: (Long) -> Unit) {
        this.onPositiveButtonClick = onPositiveButtonClick
    }

    private fun getSelectedDateInSeconds():Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, binding.pickerMonth.value)
        calendar.set(Calendar.YEAR, years[binding.pickerYear.value].toInt())
        //To avoid gaps for ex when it is the month of february.
        calendar.set(Calendar.DAY_OF_MONTH, 10)
        return calendar.timeInMillis / 1000
    }
}

