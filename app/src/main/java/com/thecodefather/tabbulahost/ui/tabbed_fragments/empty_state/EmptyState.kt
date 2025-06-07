package com.thecodefather.tabbulahost.ui.tabbed_fragments.empty_state

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.thecodefather.tabbulahost.R


/**
 * Steps to add an empty state:
 * 1- declare it in the designated fragment:
 *          private var emptyState: EmptyState? = null
 *
 * 2- add it to the fragment xml:
 *          emptyState = EmptyState(EmptyState.Companion.StateType.Reminders, requireContext, this.rootView) //rootView is the name of the view containing the recyclerView
 *
 * 3- on returned response:
 *          if(noContent) emptyState?.attach() else emptyState?.detach()
 *
 * 4- attach it on error
 *          if(response.code == 404) emptyState?.attach()
 */

class EmptyState(
    val type: EmptyStateType,
    val context: Context,
    val root: ViewGroup?,
    val listener: Listener? = null
) {

    var view: View

    init {
        val layoutInflater = LayoutInflater.from(context)
        when (type) {
            EmptyStateType.Default -> {
                view = layoutInflater.inflate(R.layout.empty_state, root, false)
                setData(type)
            }
        }
    }

    private fun setData(emptyStateType: EmptyStateType) {
        val tvErrorMessage = view.findViewById<TextView>(R.id.tvErrorMessage)
        when(emptyStateType){
            EmptyStateType.Default -> {
                tvErrorMessage.text = "No Matches Found"
            }
        }
    }

    fun attach() {
        detach()
        root?.addView(view)
    }

    fun detach() {
        if (view.rootView != null) {
            view.let {
                root?.removeView(view)
            }
        }
    }

    interface Listener{
        fun clickedButton(isBackToHome: Boolean ? = false) {}
    }
}

enum class EmptyStateType {
    Default
}