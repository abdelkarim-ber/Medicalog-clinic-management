package com.example.android.clinicmanagement.expensesHistory

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.android.clinicmanagement.ClinicApplication
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.FragmentExpensesHistoryBinding
import com.example.android.clinicmanagement.patientsList.LoadStateAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ExpensesHistoryFragment : Fragment() {

    private lateinit var binding: FragmentExpensesHistoryBinding
    private lateinit var expensesHistoryViewModel: ExpensesHistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application = requireNotNull(this.activity).application

        val appContainer = (application as ClinicApplication).appContainer

        val expensesHistoryViewModelFactory =
            ExpensesHistoryViewModelFactory(appContainer.expenditureRepository)

        expensesHistoryViewModel = ViewModelProvider(
            this, expensesHistoryViewModelFactory
        ).get(ExpensesHistoryViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_expenses_history, container, false
        )
        binding.viewModel = expensesHistoryViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }


        binding.toolBar.setNavigationOnClickListener { findNavController().navigateUp() }


        val singleItems = arrayOf(
            resources.getString(ExpensesOrderType.DATE.textResId),
            resources.getString(ExpensesOrderType.RECENTLY_ADDED.textResId)
        )
        var checkedItem = ExpensesOrderType.DATE.index
        binding.buttonOrder.setOnClickListener {
            MaterialAlertDialogBuilder(
                requireContext(),
                R.style.ThemeOverlay_ClinicManagement_MaterialDialog
            )
                .setTitle(resources.getString(R.string.dialog_sort_title))
                .setPositiveButton(resources.getString(R.string.dialog_cancel)) { dialog, which ->
                    dialog.dismiss()
                }
                .setSingleChoiceItems(singleItems, checkedItem) { dialog, which ->
                    checkedItem = which
                    val expensesOrderType = ExpensesOrderType.findOrderTypeWithIndex(which)
                    expensesOrderType?.let { expensesHistoryViewModel.changeExpensesHistoryOrderWith(it) }
                    dialog.dismiss()
                }
                .show()

        }


        val expensesHistoryAdapter = ExpensesHistoryAdapter()

        //Collecting the load state flow.
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                expensesHistoryAdapter.loadStateFlow.collect { loadStates ->
                    // We check if it's the initial load or a refresh load after changing the order type of the list.
                    // if it's a refresh after a deletion we let item change animations be displayed
                    // instead of displaying the loading screen.
                    if (loadStates.refresh is LoadState.Loading && !expensesHistoryViewModel.isDeletionUpdate) {
                        expensesHistoryViewModel.showLoadingScreen()
                        //Here we delay for a second to give the animations of showing/hiding loading
                        // and showing/hiding content enough time to run.
                        delay(1000L)
                        //After each refresh we scroll to position zero.
                        binding.listExpensesHistory.scrollToPosition(0)

                    }

                    //We check if the list has finished loading
                    //Then corresponding to the list item count we either show the place holder screen
                    // or the actual content.
                    if (loadStates.refresh is LoadState.NotLoading) {
                        if (expensesHistoryAdapter.itemCount == 0) {
                            expensesHistoryViewModel.showEmptyScreen()
                        } else {
                            expensesHistoryViewModel.showContent()
                        }
                    }


                }
            }
        }

        //Display the load state for Header and footer
        binding.listExpensesHistory.adapter = expensesHistoryAdapter.withLoadStateHeaderAndFooter(
            header = LoadStateAdapter(),
            footer = LoadStateAdapter()
        )



        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val myViewHolder = viewHolder as? ExpensesHistoryAdapter.ViewHolder
                myViewHolder?.let {
                    expensesHistoryViewModel.deleteExpense(it.binding.expenditure!!)
                }
            }

        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.listExpensesHistory)



        // Add an Observer for expenses history list when a new list
        // with a new order type is requested.
        expensesHistoryViewModel.expensesList.observe(viewLifecycleOwner) { expensesHistoryList ->
                expensesHistoryAdapter.submitData(lifecycle,expensesHistoryList)
        }

        // Add an Observer on the state variable for showing the snack bar when
        // we delete an expense from the list.
        expensesHistoryViewModel.showSnackBarEvent.observe(viewLifecycleOwner) { isShowingSnackBar ->
            if (isShowingSnackBar) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.expense_history_deletion),
                    Snackbar.LENGTH_LONG
                ).show()
                // Reset state to make sure the snackBar is only shown once, even if the device
                // has a configuration change.
                expensesHistoryViewModel.doneShowingSnackBar()
            }

        }






    }
}