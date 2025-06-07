package com.thecodefather.tabbulahost.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.thecodefather.tabbula.TabbulaFragmentModel
import com.thecodefather.tabbula.TabbulaHost
import com.thecodefather.tabbula.TabsLoadingMethod
import com.thecodefather.tabbulahost.MyTabFragmentType
import com.thecodefather.tabbulahost.databinding.FragmentHomeBinding
import com.thecodefather.tabbulahost.ui.tabbed_fragments.empty_state.DashboardFragment
import com.thecodefather.tabbulahost.ui.tabbed_fragments.empty_state.EmptyStateFragment
import com.thecodefather.tabbulahost.ui.tabbed_fragments.empty_state.ProfileFragment
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var selectedTab = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tabbulaHost.initialize(
            fragmentManager = childFragmentManager,
            loadingMethod = TabsLoadingMethod.LOAD_ONE_BY_ONE,
            listener = object : TabbulaHost.Listener {
                override fun selectedTabAtIndex(index: Int) {
                    //save the selected index in the fragment's view model
                    selectedTab = index
                }
            }
        )

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                populateTabbulaHost()
            }
        }
    }

    private fun populateTabbulaHost() {
        binding.tabbulaHost.removeAllTabs()
        binding.tabbulaHost.clearFragments()
        binding.tabbulaHost.populate(
            index = selectedTab,
            list = listOf(
                TabbulaFragmentModel(
                    fragmentType = MyTabFragmentType.Dashboard,
                    tabTitle = "Dashboard"
                ),
                TabbulaFragmentModel(
                    fragmentType = MyTabFragmentType.Profile,
                    tabTitle = "Profile"
                ),
                TabbulaFragmentModel(
                    fragmentType = MyTabFragmentType.Default,
                    tabTitle = "Coming Soon"
                )
            )
        ) { fragmentType ->
            when (fragmentType) {
                is MyTabFragmentType.Dashboard -> DashboardFragment.newInstance()
                is MyTabFragmentType.Profile -> ProfileFragment.newInstance()
                is MyTabFragmentType.Default -> EmptyStateFragment.newInstance(fragmentType.toString())
            }

        }
    }

    override fun onStop() {
        super.onStop()
        selectedTab = binding.tabbulaHost.getSelectedTabPo()
        binding.tabbulaHost.clearFragments()
    }

    override fun onDestroyView() {
        binding.tabbulaHost.destroy()
        super.onDestroyView()
        _binding = null
    }
}