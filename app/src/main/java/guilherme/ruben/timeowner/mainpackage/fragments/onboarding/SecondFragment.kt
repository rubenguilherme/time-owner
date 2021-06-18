package guilherme.ruben.timeowner.mainpackage.fragments.onboarding

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import guilherme.ruben.timeowner.R
import guilherme.ruben.timeowner.mainpackage.MainActivity
import kotlinx.android.synthetic.main.fragment_second.view.*
import java.util.*

class SecondFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onResume() {
        super.onResume()
        requireView().second_right.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.blink))
    }

    override fun onPause() {
        super.onPause()
        saveText()
    }

    private fun saveText() {
        val view = requireView()
        val date = "${view.second_date.dayOfMonth}/${view.second_date.month}/${view.second_date.year}"
        val name = view.second_name.text.toString()
        val occup = view.second_occupation.text.toString()
        var str = ""
        if (TextUtils.isEmpty(name)) str += "Name is empty"
        if (TextUtils.isEmpty(occup)) {
            if (!TextUtils.isEmpty(str))
                str += "\n"
            str += "Occupation is empty"
        }
        if (view.second_date.year >= Calendar.getInstance().get(Calendar.YEAR)) {
            if (!TextUtils.isEmpty(str))
                str += "\n"
            str += "Insert a valid date"
        }
        if (!TextUtils.isEmpty(str)) {
            Toast.makeText(requireContext(),str,Toast.LENGTH_SHORT).show()
        }
        val a = arrayOf(name, occup, date)
        (requireActivity() as MainActivity).setSettings(a)
    }


}