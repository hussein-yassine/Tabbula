package com.thecodefather.tabbula

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.tabs.TabLayout
import com.thecodefather.tabbula.extensions.px
import kotlin.collections.forEach
import kotlin.collections.forEachIndexed
import kotlin.collections.isNotEmpty
import kotlin.collections.isNullOrEmpty
import kotlin.collections.map
import kotlin.let
import kotlin.ranges.until
import kotlin.toString

class TabbulaHost @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val tabLayout: TabLayout by lazy { findViewById(R.id.tab_layout) }

    companion object{
        private const val MAXIMUM_FIXED_TABS_COUNT = 2
    }

    private var loadingMethod = TabsLoadingMethod.LOAD_ALL_AT_ONCE

    private var fragmentManager: FragmentManager? = null
    private var listener: Listener? = null

    private var tabs: List<String>? = null
        set(value) {
            field = value
            initTabs()
        }
    private var shownFragments: Array<Fragment?>? = null
    private var fragments: List<Fragment> = emptyList()
        set(value) {
            field = value
            shownFragments = arrayOfNulls(value.size)
            initTabHostFragments()
        }
    private var selectedIndex = 0
    private var addMarginsToTabs = true

    init {
        LayoutInflater.from(context).inflate(R.layout.tabbula_fragment_host, this)
    }

    //initialize
    fun initialize(
        fragmentManager: FragmentManager,
        loadingMethod: TabsLoadingMethod,
        addMarginsToTabs: Boolean = this.addMarginsToTabs,
        listener: Listener
    ){
        this.fragmentManager = fragmentManager
        this.loadingMethod = loadingMethod
        this.addMarginsToTabs = addMarginsToTabs
        this.listener = listener


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                try {
                    selectedIndex = tab.position

                    listener?.selectedTabAtIndex(selectedIndex)
                    if (fragments.isNotEmpty()){
                        if (selectedIndex >= 0 && selectedIndex < fragments!!.size) {
                            val fragment = shownFragments?.get(selectedIndex)
                            val transaction = fragmentManager.beginTransaction()
                            transaction.setCustomAnimations(R.anim.enter_animation, R.anim.exit_animation)

                            when(loadingMethod){
                                TabsLoadingMethod.LOAD_ALL_AT_ONCE -> {
                                    transaction.show(fragment!!)
                                }
                                TabsLoadingMethod.LOAD_ONE_BY_ONE -> {
                                    if (fragment == null){
                                        shownFragments?.set(selectedIndex,
                                            fragments?.get(selectedIndex))
                                        transaction.add(R.id.container, shownFragments?.get(selectedIndex)!!)
                                    }
                                    transaction.show(shownFragments?.get(selectedIndex)!!)
                                }
                            }

                            transaction.commitNowAllowingStateLoss()
                        }
                    }
                    tabLayout.getTabAt(selectedIndex)?.select()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Do nothing
                try {
                    if (fragments.isNotEmpty()){
                        val unselectedIndex = tab.position
                        if (unselectedIndex >= 0 && unselectedIndex < fragments!!.size) {

                            val fragment = shownFragments?.get(unselectedIndex)!!
                            val transaction = fragmentManager.beginTransaction()
                            transaction.setCustomAnimations(
                                R.anim.enter_animation,
                                R.anim.exit_animation
                            )
                            transaction.hide(fragment)
                            transaction.commitNowAllowingStateLoss()
                        }
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Do nothing
            }
        })
    }

    //<editor-fold desc="populating component">
    fun <T : TabFragmentType> populate(
        index:Int?,
        list: List<TabbulaFragmentModel<T>>,
        fragmentFactory: (T) -> Fragment
    ) {
        tabs = list.map {it.tabTitle }
        fragments = list.map { fragmentFactory(it.fragmentType) }
        index?.let {
            this.selectedIndex = it
            tabLayout.getTabAt(selectedIndex)?.select()
        }

        if (tabs != null && tabs?.size!! <= MAXIMUM_FIXED_TABS_COUNT) {
            tabLayout.tabMode = TabLayout.MODE_FIXED
        } else {
            tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        }

        if (addMarginsToTabs) addTabsMargins()
    }

    private fun addTabsMargins(){

        for (i in 0 until tabLayout.tabCount) {
            val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as MarginLayoutParams
            p.setMargins(2.px, 2.px, 2.px, 2.px)
            tab.requestLayout()
        }
    }

    private fun initTabs() {
        if (!isTabsTheSame() && !tabs.isNullOrEmpty()){
            for (tab in tabs!!) {
                val newTab = tabLayout.newTab().setText(tab)
                tabLayout.addTab(newTab)
            }
        }
    }

    private fun initTabHostFragments(){
        try {
            fragmentManager?.let { fm ->
                val transaction = fm.beginTransaction()
                if (selectedIndex >= fragments.size) {
                    selectedIndex = 0
                }
                when(loadingMethod){
                    TabsLoadingMethod.LOAD_ALL_AT_ONCE -> {
                        fragments.forEachIndexed { index, fragment ->
                            shownFragments?.set(index, fragment)
                            transaction.add(R.id.container, fragment).hide(fragment)
                        }
                    }
                    TabsLoadingMethod.LOAD_ONE_BY_ONE -> {
                        shownFragments?.set(selectedIndex, fragments[selectedIndex])
                        transaction.add(R.id.container, shownFragments?.get(selectedIndex)!!)

                    }
                }
                transaction.show(shownFragments?.get(selectedIndex)!!)
                transaction.commitNowAllowingStateLoss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    //</editor-fold>

    //<editor-fold desc="destroying section">
    fun removeAllTabs(){
        selectedIndex = 0
        tabs = null
        tabLayout.removeAllTabs()
    }

    fun clearFragments() {
        if (fragmentManager != null && fragments.isNotEmpty()) {
            val transaction = fragmentManager!!.beginTransaction()
            transaction.setCustomAnimations(R.anim.enter_animation, R.anim.exit_animation)
            fragments.forEach {
                transaction.remove(it)
            }
            transaction.commitNowAllowingStateLoss()
        }
    }
    fun destroy(){
        clearFragments()
        removeAllTabs()
        this.fragmentManager = null
        this.listener = null
        this.fragments = emptyList()
        this.shownFragments = null
    }
    //</editor-fold>

    //<editor-fold desc="util functions">
    private fun isTabsTheSame(): Boolean {
        val tabCount = tabLayout.tabCount
        var isSame = true

        try {
            for (i in 0 until tabCount) {
                val tabTitle = tabLayout.getTabAt(i)?.text.toString()
                val passedTabTitle = tabs?.get(i)
                if (tabTitle != passedTabTitle) {
                    isSame = false
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            isSame = true
        }

        return isSame && tabCount == tabs?.size
    }
    //</editor-fold>

    fun getSelectedTabPo() = selectedIndex

    // listener
    interface Listener {
        fun selectedTabAtIndex(index:Int)
    }
}