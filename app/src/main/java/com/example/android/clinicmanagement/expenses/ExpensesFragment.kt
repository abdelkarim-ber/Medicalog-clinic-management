package com.example.android.clinicmanagement.expenses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android.clinicmanagement.ClinicApplication
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.FragmentExpensesBinding
import com.example.android.clinicmanagement.monthPicker.MonthPickerDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ExpensesFragment : Fragment() {

    private lateinit var binding: FragmentExpensesBinding
    private lateinit var expensesViewModel: ExpensesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application = requireNotNull(this.activity).application

        val appContainer = (application as ClinicApplication).appContainer

        val expensesViewModelFactory = ExpensesViewModelFactory(appContainer.expenditureRepository)

        expensesViewModel = ViewModelProvider(
            this, expensesViewModelFactory
        ).get(ExpensesViewModel::class.java)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_expenses, container, false
        )
        binding.viewModel = expensesViewModel
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

        val monthPicker = MonthPickerDialog()
        monthPicker.addOnPositiveButtonClickListener { dateInSeconds ->
            expensesViewModel.setSelectedMonthAndYear(dateInSeconds * 1000)
        }


        binding.toolBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.calendar -> monthPicker.showMonthPicker(childFragmentManager, "tag")
                R.id.history ->  findNavController().navigate(ExpensesFragmentDirections.actionExpensesToExpensesHistoryFragment())
            }
            true
        }

        val adapter = ExpensesCategoryAdapter { position, expensesNumber ->
            expensesViewModel.onCardSelected(position, expensesNumber)
        }
        binding.listExpenditureCategory.adapter = adapter


        // Add an Observer on the state variable for showing the snack bar when we add an expense.
        expensesViewModel.showSnackBarEvent.observe(viewLifecycleOwner) { isShowingSnackBar ->
            if (isShowingSnackBar) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.new_expense_add),
                    Snackbar.LENGTH_LONG
                ).show()
                binding.textAmountPayed.text?.clear()
                expensesViewModel.clearExpensesTypeSelection()
                // Reset state to make sure the snackBar is only shown once, even if the device
                // has a configuration change.
                expensesViewModel.doneShowingSnackBar()
            }

        }

        val whiteColor = ContextCompat.getColor(requireContext(), R.color.white)
        val bluishWhiteColor = ContextCompat.getColor(requireContext(), R.color.bluish_white)
        val midnightColor = ContextCompat.getColor(requireContext(), R.color.midnight)

        viewLifecycleOwner.lifecycleScope.launch {
            //Add a delay to leave the fragment enough time to process the recycler view elements
            //to keep the same state (selected cards) after the configuration change.
            delay(800L)
            // Add an Observer on the state variable to show the card of the corresponding
            // position as selected by changing its appearance.
            expensesViewModel.currentCardSelected.observe(viewLifecycleOwner) { position ->
                position?.let {
                    val cardView =
                        binding.listExpenditureCategory.findViewHolderForAdapterPosition(position)
                            ?.itemView?.findViewById<MaterialCardView>(R.id.card_view)

                    cardView?.setCardBackgroundColor(bluishWhiteColor)
                    cardView?.strokeColor = midnightColor
                }
            }

            // Add an Observer on the state variable to reset the card of the corresponding
            // position to its default appearance.
            expensesViewModel.previousCardSelected.observe(viewLifecycleOwner) { position ->
                position?.let {
                    val cardView =
                        binding.listExpenditureCategory.findViewHolderForAdapterPosition(position)
                            ?.itemView?.findViewById<MaterialCardView>(R.id.card_view)

                    cardView?.setCardBackgroundColor(whiteColor)
                    cardView?.strokeColor = whiteColor
                }
            }
        }


    }


}