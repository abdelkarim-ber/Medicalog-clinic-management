package com.example.android.clinicmanagement.session

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.navigation.fragment.findNavController
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.FragmentNewSessionBinding
import com.example.android.clinicmanagement.databinding.FragmentPatientProfileBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.logging.SimpleFormatter


class NewSessionFragment : Fragment() {
    lateinit var binding: FragmentNewSessionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            setAllContainerColors(ContextCompat.getColor(requireContext(), R.color.white))
            duration =
                resources.getInteger(R.integer.clinicmanagement_motion_duration_medium).toLong()
            interpolator = FastOutSlowInInterpolator()
            fadeMode = MaterialContainerTransform.FADE_MODE_IN
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new_session, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText(getString(R.string.session_time_selection_title))
                .build()

        timePicker.addOnPositiveButtonClickListener {
            binding.textFieldTime.setText("${timePicker.hour}:${timePicker.minute}")

        }

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()




        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.textLayoutTime.setEndIconOnClickListener {
            // Respond to end icon presses
            timePicker.show(childFragmentManager,"tag")

        }
        binding.textLayoutDate.setEndIconOnClickListener {
            // Respond to end icon presses
            datePicker.show(childFragmentManager,"tag")

        }
        datePicker.addOnPositiveButtonClickListener {

            val date = Date(it)
            var formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault() )
            var formattedDate = formatter.format(date)
            binding.textFieldDate.setText(formattedDate)

        }


    }
}