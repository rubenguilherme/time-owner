package guilherme.ruben.timeowner.mainpackage.fragments.onboarding

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import guilherme.ruben.timeowner.R
import kotlinx.android.synthetic.main.fragment_first.view.*

class FirstFragment : Fragment() {

    var flag = true

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onResume() {
        super.onResume()
        requireView().first_right.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.blink))
    }


}