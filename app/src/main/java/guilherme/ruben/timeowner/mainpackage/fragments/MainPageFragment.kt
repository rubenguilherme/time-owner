package guilherme.ruben.timeowner.mainpackage.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import guilherme.ruben.timeowner.R
import guilherme.ruben.timeowner.adapters.ViewPagerAdapter


class MainPageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_page, container, false)

        // Setup the action bar and the transitions from and to this fragment
        (activity as AppCompatActivity).supportActionBar?.show()
        val tInflater = TransitionInflater.from(requireContext())
        enterTransition = tInflater.inflateTransition(R.transition.slide_left)
        exitTransition = tInflater.inflateTransition(R.transition.slide_right)


        // Setup the view pager
        val fragmentList = arrayListOf<Fragment>(
            ToDoListFragment(),
            CalendarFragment()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )
        view.findViewById<ViewPager2>(R.id.pager).adapter = adapter

        val tabIcons = arrayOf(R.drawable.ic_checklist, R.drawable.ic_calendar)

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        TabLayoutMediator(tabLayout, view.findViewById<ViewPager2>(R.id.pager)) {
                tab, position ->
            tab.icon = ContextCompat.getDrawable(requireContext(), tabIcons[position])
        }.attach()



        return view
    }


}