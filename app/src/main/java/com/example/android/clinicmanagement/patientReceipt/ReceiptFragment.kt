package com.example.android.clinicmanagement.patientReceipt

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.Slide
import com.example.android.clinicmanagement.ClinicApplication
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.FragmentReceiptBinding
import com.example.android.clinicmanagement.domain.PrepareReceiptForPrintUseCase


class ReceiptFragment : Fragment() {

    private lateinit var binding: FragmentReceiptBinding
    private val args: ReceiptFragmentArgs by navArgs()
    private lateinit var receiptViewModel: ReceiptViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application = requireNotNull(this.activity).application

        val appContainer = (application as ClinicApplication).appContainer

        val viewModelFactory = ReceiptViewModelFactory(
            args.patientKey,
            ReceiptType.findReceiptTypeWithNumber(args.receiptType)!!,
            appContainer.quotationRepository,
            appContainer.invoiceRepository,
            application
        )

        receiptViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(ReceiptViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_receipt, container, false
        )
        binding.apply {
            viewModel = receiptViewModel
            receiptType = ReceiptType.findReceiptTypeWithNumber(args.receiptType)!!
            lifecycleOwner = viewLifecycleOwner
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Add an Observer on the state variable for printing the patient's receipt
        //when we click the print button.
        receiptViewModel.printPatientReceipt.observe(viewLifecycleOwner){ isPrinting ->
            if (isPrinting){
                PrepareReceiptForPrintUseCase().invoke(requireContext())
                receiptViewModel.receiptPrinted()
            }
        }

    }
}