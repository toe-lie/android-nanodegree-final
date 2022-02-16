package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionItemClickListener
import com.example.android.politicalpreparedness.models.Election
import com.example.android.politicalpreparedness.util.autoCleared
import com.example.android.politicalpreparedness.util.extensions.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ElectionsFragment : Fragment() {

    private var binding by autoCleared<FragmentElectionBinding>()

    val viewModel by viewModels<ElectionsViewModel>()

    private var upcomingElectionsAdapter by autoCleared<ElectionListAdapter>()
    private var savedElectionsAdapter by autoCleared<ElectionListAdapter>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentElectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setupUi()
        observeViewModel()
    }

    private fun initViewModel() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupUi() {
        setupUpcomingElectionsList()
    }

    private fun setupUpcomingElectionsList() {
        upcomingElectionsAdapter = ElectionListAdapter(object : ElectionItemClickListener {
            override fun onItemClick(election: Election) {
                navigateToElectionDetailScreen(election)
            }
        })
        binding.upcomingElectionRecyclerView.adapter = upcomingElectionsAdapter
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.uiState.map { it.upcomingElections }.distinctUntilChanged()
                    .collect { list ->
                        upcomingElectionsAdapter.submitList(list)
                    }
            }
        }
    }

    private fun navigateToElectionDetailScreen(election: Election) {
        findNavController().navigate(
            ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                election.name,
                election.id,
                election.division
            )
        )
    }
}