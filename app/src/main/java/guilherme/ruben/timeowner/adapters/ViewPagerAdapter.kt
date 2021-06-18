package guilherme.ruben.timeowner.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    list : ArrayList<Fragment>,
    fw : FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fw, lifecycle) {

    private val fragmentList = list

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun retList() : ArrayList<Fragment> {
        return fragmentList
    }
}