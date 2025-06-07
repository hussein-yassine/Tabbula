package com.thecodefather.tabbulahost.ui.tabbed_fragments.empty_state

import android.os.Bundle
import com.thecodefather.tabbulahost.databinding.EmptyStateBinding
import com.thecodefather.tabbulahost.ui.tabbed_fragments.BaseTabFragment

class EmptyStateFragment : BaseTabFragment<EmptyStateBinding>(
    EmptyStateBinding::inflate
) {

    private var stateType: String? = null

    companion object {
        private const val ARG_STATE_TYPE = "arg_state_type"

        fun newInstance(stateType: String): EmptyStateFragment {
            val fragment = EmptyStateFragment()
            val args = Bundle()
            args.putString(ARG_STATE_TYPE, stateType)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            stateType = it.getString(ARG_STATE_TYPE)
        }
    }
}