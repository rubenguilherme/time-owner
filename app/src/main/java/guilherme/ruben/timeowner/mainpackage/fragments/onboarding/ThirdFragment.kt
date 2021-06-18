package guilherme.ruben.timeowner.mainpackage.fragments.onboarding

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import guilherme.ruben.timeowner.R
import guilherme.ruben.timeowner.mainpackage.MainActivity
import kotlinx.android.synthetic.main.fragment_second.view.*
import kotlinx.android.synthetic.main.fragment_third.view.*
import java.util.*

class ThirdFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_third, container, false)

        setIconClick(view)

        return view
    }

    private fun setIconClick(view: View) {
        view.third_image.setOnClickListener {
            val a = (requireActivity() as MainActivity).getSettings()
            var str = ""
            if (TextUtils.isEmpty(a[0])) str += "Name is empty"
            if (TextUtils.isEmpty(a[1])) {
                if (!TextUtils.isEmpty(str))
                    str += "\n"
                str += "Occupation is empty"
            }
            if ((a[2]).split("/")[2].toInt() >= Calendar.getInstance().get(Calendar.YEAR)) {
                if (!TextUtils.isEmpty(str))
                    str += "\n"
                str += "Insert a valid date"
            }
            if (!TextUtils.isEmpty(str)) { // There was some data wrong, don't go to splash
                Toast.makeText(requireContext(),str, Toast.LENGTH_SHORT).show()
            } else {
                val settings = requireActivity().getSharedPreferences("SETTINGS", AppCompatActivity.MODE_PRIVATE)
                val editor = settings.edit()
                editor.putBoolean("appPreviouslyStarted", true)
                editor.apply()
                findNavController().navigate(R.id.action_onBoardingPagerFragment_to_splashFragment)
            }
        }
    }
}