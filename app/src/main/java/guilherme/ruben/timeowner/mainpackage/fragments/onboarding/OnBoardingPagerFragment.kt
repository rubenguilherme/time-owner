package guilherme.ruben.timeowner.mainpackage.fragments.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import guilherme.ruben.timeowner.R
import guilherme.ruben.timeowner.adapters.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_on_boarding_pager.view.*

class OnBoardingPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_on_boarding_pager, container, false)

        val fragmentList = arrayListOf<Fragment>(
            FirstFragment(),
            SecondFragment(),
            ThirdFragment()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        view.on_boarding_pager.adapter = adapter

        return view
    }
}