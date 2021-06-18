package guilherme.ruben.timeowner.mainpackage.fragments.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import guilherme.ruben.timeowner.R
import guilherme.ruben.timeowner.adapters.ViewPagerAdapter


class AddPageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_page, container, false)

        val fragmentList = arrayListOf<Fragment>(
            AddItemFragment(),
            AddRecurrentItemFragment(),
            AddEventFragment()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        view.findViewById<ViewPager2>(R.id.add_page_pager).adapter = adapter

        val tabNames = arrayOf("Unique","Recurrent","Event")

        val tabLayout = view.findViewById<TabLayout>(R.id.add_page_tabLayout)
        TabLayoutMediator(tabLayout, view.findViewById<ViewPager2>(R.id.add_page_pager)) {
                tab, position ->  tab.text = tabNames.get(position)
        }.attach()


        return view
    }
}