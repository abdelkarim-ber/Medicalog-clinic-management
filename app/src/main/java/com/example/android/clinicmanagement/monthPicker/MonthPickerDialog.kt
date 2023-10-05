package com.example.android.clinicmanagement.monthPicker


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.util.Log
import android.view.*

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.DialogFragmentMonthPickerBinding
import com.example.android.clinicmanagement.utilities.getCalendarWith

import java.text.SimpleDateFormat
import java.util.*


class MonthPickerDialog : DialogFragment() {
    lateinit var binding: DialogFragmentMonthPickerBinding

    /**
     * Year displayed in title section
     */
    private val _selectedYear = MutableLiveData<String>()
    /**
     * Month displayed in title section
     */
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
            R.layout.dialog_fragment_month_picker,
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

        val locale = Locale.getDefault()
        val monthFormatter = SimpleDateFormat("MMMM", locale)
        val monthFormatterShort = SimpleDateFormat("MMM", locale)

        val currentMonth = monthFormatter.format(System.currentTimeMillis()).replaceFirstChar { it.uppercase(locale) }
        _selectedMonth.value = monthFormatterShort.format(System.currentTimeMillis()).replaceFirstChar { it.uppercase(locale) }

        val monthsList = mutableListOf<String>()

        for (month in Calendar.JANUARY..Calendar.DECEMBER) {
            monthsList.add(
                monthFormatter.format(getCalendarWith(month).time)
                    .replaceFirstChar { it.uppercase(locale) }
            )
        }


        binding.pickerMonth.apply {
            displayedValues = monthsList.toTypedArray()
            minValue = Calendar.JANUARY
            maxValue = Calendar.DECEMBER
            wrapSelectorWheel = false
            value = monthsList.indexOf(currentMonth)

            setOnValueChangedListener { picker, oldVal, newVal ->
                val selectedMonthIndex = binding.pickerMonth.value
                _selectedMonth.value =
                    monthFormatterShort.format(getCalendarWith(selectedMonthIndex).time)
                        .replaceFirstChar { it.uppercase(locale) }
            }
        }



    }

    private fun setUpYearPicker() {
        val currentDate = Calendar.getInstance()

        val currentYear = currentDate.get(Calendar.YEAR)

        _selectedYear.value = currentYear.toString()


        for (i in 2 downTo 0) {
            years.add(currentYear.minus(i).toString())
        }

        binding.pickerYear.apply {

            displayedValues = years.toTypedArray()
            minValue = 0
            maxValue = 2
            wrapSelectorWheel = false
            value = years.indexOf(currentYear.toString())

            setOnValueChangedListener { picker, oldVal, newVal ->
                val selectedYearValue = binding.pickerYear.value
                _selectedYear.value = years[selectedYearValue]
            }
        }
    }

    fun addOnPositiveButtonClickListener(onPositiveButtonClick: (Long) -> Unit) {
        this.onPositiveButtonClick = onPositiveButtonClick
    }

    private fun getSelectedDateInSeconds():Long {
        return getCalendarWith(
            month = binding.pickerMonth.value,
            year = years[binding.pickerYear.value].toInt()
        ).timeInMillis / 1000
    }
}

