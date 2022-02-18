package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.util.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

//class VoterInfoFragment : Fragment() {
//
//    override fun onCreateView(inflater: LayoutInflater,
//                              container: ViewGroup?,
//                              savedInstanceState: Bundle?): View? {
//
//
//
//        //TODO: Populate voter info -- hide views without provided data.
//        /**
//        Hint: You will need to ensure proper data is provided from previous fragment.
//        */
//
//
//        //TODO: Handle loading of URLs
//
//        //TODO: Handle save button UI state
//        //TODO: cont'd Handle save button clicks
//        return TextView(requireContext())
//    }
//
//    //TODO: Create method to load URL intents
//
//}
@OptIn(ExperimentalCoroutinesApi::class)
@AndroidEntryPoint
class VoterInfoFragment : Fragment() {

    private var binding by autoCleared<FragmentVoterInfoBinding>()

    val viewModel by viewModels<VoterInfoViewModel>()

    val args by navArgs<VoterInfoFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVoterInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setupUi()
    }

    private fun initViewModel() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.setQuery(args.argElectionId, args.argDivision)
    }

    private fun setupUi() {
     setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.followButton.setOnClickListener {
            viewModel.toggleFollowing()
        }
    }

}