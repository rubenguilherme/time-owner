package guilherme.ruben.timeowner.mainpackage.fragments.onboarding

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import guilherme.ruben.timeowner.R
import guilherme.ruben.timeowner.adapters.ViewPagerAdapter


class BlankFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed(
            {
                val pager = requireActivity().findViewById<ViewPager2>(R.id.on_boarding_pager)
                pager.currentItem = 1
                Handler().postDelayed( { val adapter = (pager.adapter as ViewPagerAdapter)
                if (adapter.retList()[0]::class.java == BlankFragment::class.java)
                adapter.retList().removeAt(0)
                adapter.notifyDataSetChanged() }, 100)

            }
        ,10)
    }


}