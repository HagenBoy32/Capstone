package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener


class ElectionsFragment: Fragment() {



    private val viewModel: ElectionsViewModel by lazy {
        val application = requireNotNull(this.activity).application
        val viewModelFactory = ElectionsViewModelFactory(application)
        ViewModelProvider(this,viewModelFactory)
            .get(ElectionsViewModel::class.java)
    }

    private lateinit var upcomingElectionListAdapter: ElectionListAdapter
    private lateinit var savedElectionListAdapter: ElectionListAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {



        val binding: FragmentElectionBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_election,
            container,
            false)


        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        upcomingElectionListAdapter = ElectionListAdapter(ElectionListener {
            findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(it)
            )
        })


        binding.upcomingElectionsRecyclerView.adapter = upcomingElectionListAdapter

        savedElectionListAdapter = ElectionListAdapter(ElectionListener {
            findNavController().navigate(
                ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(it)
            )
        })

        binding.savedElectionsRecyclerView.adapter = savedElectionListAdapter

        viewModel.upcomingElections.observe(viewLifecycleOwner, Observer { elections ->
            elections?.apply {
                upcomingElectionListAdapter.elections = elections
            }
        })

        viewModel.savedElections.observe(viewLifecycleOwner, Observer { elections ->
            elections?.apply {
                savedElectionListAdapter.elections = elections
            }
        })

        return binding.root

    }


}
