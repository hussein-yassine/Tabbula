package com.thecodefather.tabbulahost.ui.tabbed_fragments.empty_state

import com.thecodefather.tabbulahost.databinding.FragmentProfileBinding
import com.thecodefather.tabbulahost.ui.tabbed_fragments.BaseTabFragment

class ProfileFragment : BaseTabFragment<FragmentProfileBinding>(
    FragmentProfileBinding::inflate
){
    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }
}