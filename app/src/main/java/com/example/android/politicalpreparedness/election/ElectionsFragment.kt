package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.util.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ElectionsFragment : Fragment() {

    val viewModel by viewModels<ElectionsViewModel>()

    var binding by autoCleared<FragmentElectionBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentElectionBinding.inflate(inflater, container, false)

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters
        return binding.root
    }

    //TODO: Refresh adapters when fragment loads

}