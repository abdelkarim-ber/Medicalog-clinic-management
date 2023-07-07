package com.example.android.clinicmanagement.patientForm

import android.content.Context
import android.icu.util.ULocale.getCountry
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.doOnLayout
import androidx.core.view.doOnPreDraw
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.Slide
import com.airbnb.lottie.utils.Logger.error
import com.example.android.clinicmanagement.ClinicApplication
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.data.models.Patient
import com.example.android.clinicmanagement.databinding.FragmentPatientFormBinding
import com.example.android.clinicmanagement.patientProfile.PatientProfileFragmentArgs
import com.example.android.clinicmanagement.statistics.StatisticsViewModel
import com.example.android.clinicmanagement.statistics.StatisticsViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import java.text.SimpleDateFormat
import java.util.*


class PatientFormFragment : Fragment() {

    lateinit var binding: FragmentPatientFormBinding
    private val args: PatientFormFragmentArgs by navArgs()
    lateinit var patientFormViewModel: PatientFormViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            setAllContainerColors(ContextCompat.getColor(requireContext(), R.color.white))
            setPathMotion(MaterialArcMotion())
            duration =
                resources.getInteger(R.integer.clinicmanagement_motion_duration_medium).toLong()
            interpolator = FastOutSlowInInterpolator()
            fadeMode = MaterialContainerTransform.FADE_MODE_IN
        }
        sharedElementReturnTransition = null

        //In the update patient info version we want to keep the transition defined in navigation.xml file
        //In the add patient version we want to do this following return transition
        if (args.patientKey == -1L) {
            returnTransition = Slide().apply {
                duration =
                    resources.getInteger(R.integer.clinicmanagement_motion_duration_small).toLong()
                addTarget(R.id.patient_form)
            }
        }

        val application = requireNotNull(this.activity).application

        val appContainer = (application as ClinicApplication).appContainer

        val patientFormViewModelFactory = PatientFormViewModelFactory(
            args.patientKey,
            appContainer.submitPatientInfoUseCase,
            appContainer.loadPatientInfoUseCase
        )

        patientFormViewModel = ViewModelProvider(
            this, patientFormViewModelFactory
        ).get(PatientFormViewModel::class.java)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_patient_form, container, false
        )
        binding.viewModel = patientFormViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }

        patientFormViewModel.showSnackBarEvent.observe(viewLifecycleOwner) {
            if (it == true) { // Observed state is true.
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(if (args.patientKey == -1L) R.string.form_patient_add else R.string.form_patient_update),
                    Snackbar.LENGTH_LONG // How long to display the message.
                ).show()
                // Reset state to make sure the snackBar is only shown once, even if the device
                // has a configuration change.
                patientFormViewModel.doneShowingSnackBar()
            }
        }
        patientFormViewModel.clearFormEvent.observe(viewLifecycleOwner) { isClearState ->
            if (isClearState) {
                with(binding) {
                    dropdownGender.text?.clear()
                    val editTextList: List<TextInputEditText> = listOf(
                        textFirstName,
                        textLastName,
                        textAge,
                        textPhoneNumber,
                        textConsultationDate,
                        textDoctorFullName,
                        textDiagnostic,
                        textFrequency,
                        textSessionCount,
                        textSessionPrice,
                        textSocialCoverage
                    )
                    editTextList.forEach { it.text?.clear() }
                    textFirstName.requestFocus()
                }
                patientFormViewModel.formFieldsCleared()
                //in the update case when we save info we return automatically
                if (args.patientKey != -1L) {
                    findNavController().popBackStack()
                }
            }
        }

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.dialog_date_picker_title))
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.addOnPositiveButtonClickListener {
            var formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            binding.textConsultationDate.setText(formatter.format(it))
        }
        binding.layoutConsultationDate.setStartIconOnClickListener {
            datePicker.show(childFragmentManager, "tag")
        }

        val telephonyManager =
            requireContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val countryCode = telephonyManager.simCountryIso.uppercase(Locale.ROOT)

        binding.textPhoneNumber.addTextChangedListener(PhoneNumberFormattingTextWatcher(countryCode))

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.list_item_gender,
            listOf(getString(R.string.male), getString(R.string.female))
        )
        binding.dropdownGender.setAdapter(adapter)
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        if (args.patientKey != -1L) {
            binding.toolBar.title = getString(R.string.update_info)
            binding.buttonSave.text = getString(R.string.update)
        }
        setTotalCalculation()
    }


    private fun setTotalCalculation() {
        with(binding) {
            textSessionCount.doOnTextChanged { text, start, before, count ->
                if (textSessionPrice.text.toString()
                        .isNotEmpty() && text != null && text.isNotEmpty()
                ) {
                    val total = Integer.parseInt(text.toString()) *
                            Integer.parseInt(textSessionPrice.text.toString())

                    textTotal.setText(total.toString())
                } else {
                    textTotal.setText("")
                }
            }

            textSessionPrice.doOnTextChanged { text, start, before, count ->
                if (textSessionCount.text.toString()
                        .isNotEmpty() && text != null && text.isNotEmpty()
                ) {
                    val total = Integer.parseInt(text.toString()) *
                            Integer.parseInt(textSessionCount.text.toString())
                    textTotal.setText(total.toString())
                } else {
                    textTotal.setText("")
                }
            }

        }
    }
}