package guilherme.ruben.timeowner.mainpackage.fragments

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import guilherme.ruben.timeowner.R
import guilherme.ruben.timeowner.mainpackage.MainActivity
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_splash.*
import kotlinx.android.synthetic.main.fragment_splash.view.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

class SplashFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_splash, container, false)

        // We're gonna check if the app was previously started. If this is the first time we move to the On Boarding Screen Fragment
        val settings = requireActivity().getSharedPreferences("SETTINGS", AppCompatActivity.MODE_PRIVATE)
        val prevStarted = settings.getBoolean("appPreviouslyStarted",false)
        if(!prevStarted) {
            findNavController().navigate(R.id.action_splashFragment_to_onBoardingPagerFragment)
        } else {
            view.splash_username.append(", " + (requireActivity() as MainActivity).getSettings()[0])
            view.setOnClickListener {
                findNavController().navigate(R.id.action_splashFragment_to_viewPageFragment)
            }
        }

        // Define the transition to use when leaving this fragment
        val tInflater = TransitionInflater.from(requireContext())
        exitTransition = tInflater.inflateTransition(R.transition.fade_right)

        // Setup the animations for the Logo and the Tip
        animateLogo(view)
        animateTip(view)

        // Make the "Tap To Enter" blink to catch the user's eye
        view.splash_tap_text.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.blink))

        // Choose the tip randomly and show it to the user
        view.splash_tip.text = getTip()

        return view
    }

    private fun getTip() : String {
        val inpS = requireContext().assets.open("tips.txt")
        val buffReader = BufferedReader(InputStreamReader(inpS))
        val numberOfTips = buffReader.readLine().toInt()
        val tipNumber = Random(Calendar.getInstance().timeInMillis).nextInt(numberOfTips - 1)
        for(i in 0..tipNumber) {
            val s = buffReader.readLine()
            if(i == tipNumber)
                return s
        }

        return "ERRO"
    }

    private fun animateTip(view: View) {
        ObjectAnimator.ofFloat(view.splash_tip_t, "translationY", 10.0f).apply {
            duration = 500
            repeatCount = -1
            repeatMode = ObjectAnimator.REVERSE
            start()
        }
        ObjectAnimator.ofFloat(view.splash_tip_i, "translationY", 10.0f).apply {
            duration = 500
            startDelay = 250
            repeatCount = -1
            repeatMode = ObjectAnimator.REVERSE
            start()
        }
        ObjectAnimator.ofFloat(view.splash_tip_p, "translationY", 10.0f).apply {
            duration = 500
            startDelay = 500
            repeatCount = -1
            repeatMode = ObjectAnimator.REVERSE
            start()
        }
    }

    private fun animateLogo(view: View) {
        ObjectAnimator.ofFloat(view.splash_owner_layout, "translationX", -500f).apply {
            duration = 0
            start()
        }

        ObjectAnimator.ofFloat(view.splash_owner_layout, "translationX", 0f).apply {
            duration = 500
            startDelay = 200
            start()
        }

        ObjectAnimator.ofFloat(view.splash_o, "rotation", 360f).apply {
            duration = 600
            startDelay = 1000
            repeatCount = 2
            start()
        }
    }
}