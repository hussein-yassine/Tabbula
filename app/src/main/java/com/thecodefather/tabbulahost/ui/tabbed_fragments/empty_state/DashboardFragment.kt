package com.thecodefather.tabbulahost.ui.tabbed_fragments.empty_state

import com.thecodefather.tabbulahost.databinding.FragmentDashboardBinding
import com.thecodefather.tabbulahost.ui.tabbed_fragments.BaseTabFragment

class DashboardFragment : BaseTabFragment<FragmentDashboardBinding>(
    FragmentDashboardBinding::inflate
) {
    companion object {
        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }
}