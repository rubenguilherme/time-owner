package guilherme.ruben.timeowner.mainpackage.fragments.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import guilherme.ruben.timeowner.R
import guilherme.ruben.timeowner.mainpackage.MainActivity
import kotlinx.android.synthetic.main.fragment_group_names.view.*

class GroupNamesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_group_names, container, false)

        val groupNames = (activity as MainActivity).getGroupNames()

        view.group_names_red.setText(groupNames[0])
        view.group_names_orange.setText(groupNames[1])
        view.group_names_yellow.setText(groupNames[2])
        view.group_names_green.setText(groupNames[3])
        view.group_names_aqua.setText(groupNames[4])
        view.group_names_blue.setText(groupNames[5])
        view.group_names_dark_blue.setText(groupNames[6])
        view.group_names_magenta.setText(groupNames[7])

        view.group_names_fab.setOnClickListener {
            groupNames[0] = view.group_names_red.text.toString()
            groupNames[1] = view.group_names_orange.text.toString()
            groupNames[2] = view.group_names_yellow.text.toString()
            groupNames[3] = view.group_names_green.text.toString()
            groupNames[4] = view.group_names_aqua.text.toString()
            groupNames[5] = view.group_names_blue.text.toString()
            groupNames[6] = view.group_names_dark_blue.text.toString()
            groupNames[7] = view.group_names_magenta.text.toString()
            val eventGroups = activity?.getSharedPreferences("EVENT_GROUPS", AppCompatActivity.MODE_PRIVATE)
            val eventGroupsEditor = eventGroups?.edit()
            eventGroupsEditor?.putString("red",groupNames[0])
            eventGroupsEditor?.putString("orange",groupNames[1])
            eventGroupsEditor?.putString("yellow",groupNames[2])
            eventGroupsEditor?.putString("green",groupNames[3])
            eventGroupsEditor?.putString("aqua",groupNames[4])
            eventGroupsEditor?.putString("blue",groupNames[5])
            eventGroupsEditor?.putString("dark_blue",groupNames[6])
            eventGroupsEditor?.putString("magenta",groupNames[7])
            eventGroupsEditor?.apply()
            findNavController().navigate(R.id.action_groupNamesFragment_to_viewPageFragment)
        }

        return view
    }

}