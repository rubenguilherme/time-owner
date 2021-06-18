package guilherme.ruben.timeowner.mainpackage.fragments.add

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.text.format.DateFormat
import android.text.format.DateUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import guilherme.ruben.timeowner.R
import guilherme.ruben.timeowner.adapters.PriorityAdapter
import guilherme.ruben.timeowner.data.events.Event
import guilherme.ruben.timeowner.data.events.EventViewModel
import guilherme.ruben.timeowner.mainpackage.MainActivity
import kotlinx.android.synthetic.main.fragment_add_event.*
import kotlinx.android.synthetic.main.fragment_add_event.view.*
import kotlinx.android.synthetic.main.fragment_add_item.view.*
import kotlinx.android.synthetic.main.fragment_update_event.view.*
import java.util.*

class AddEventFragment : Fragment() {
    private lateinit var eventViewModel: EventViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private var pos = 0
    private val colorArray = arrayOf(R.color.cal_red,R.color.cal_orange,R.color.cal_yellow,R.color.cal_green,R.color.cal_aqua,R.color.cal_blue,R.color.cal_dark_blue,R.color.cal_magenta)
    private lateinit var groupNames : Array<String>

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_event, container, false)

        // On Click Listener to add event to database
        view.add_event_fab.setOnClickListener {
            insertDataToDatabase()
        }

        // Initialize variables
        eventViewModel = ViewModelProvider(this).get(EventViewModel::class.java)
        groupNames = (activity as MainActivity).getGroupNames()

        // Set the initial text based on the sharedPreferences
        view.add_event_autoCompleteTextView.setText(groupNames[0])

        // Set the time to current time
        val currentTime = Calendar.getInstance()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            view.add_event_timePicker.hour = currentTime.get(Calendar.HOUR_OF_DAY)
            view.add_event_timePicker.minute = currentTime.get(Calendar.MINUTE)
        } else {
            view.add_event_timePicker.currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
            view.add_event_timePicker.currentMinute = currentTime.get(Calendar.MINUTE)
        }
        view.add_event_timePicker.setIs24HourView(DateFormat.is24HourFormat(context))
        val currentDate = (activity as MainActivity).getCurrentDate()
        view.add_event_datePicker.updateDate(currentDate.get(Calendar.YEAR),currentDate.get(Calendar.MONTH),currentDate.get(Calendar.DAY_OF_MONTH))

        return view
    }

    override fun onResume() {
        super.onResume()
        val arrayAdapter = PriorityAdapter(requireContext(),R.layout.dropdown_layout,groupNames,colorArray)
        view?.add_event_autoCompleteTextView?.setAdapter(arrayAdapter)
        view?.add_event_autoCompleteTextView?.setOnItemClickListener{ _, _, i: Int, _ ->
            pos = i
            view?.add_event_textInputLayout?.setStartIconTintList(context?.let { ContextCompat.getColorStateList(it,colorArray[pos]) })
        }
    }

    private fun insertDataToDatabase() {
        val eventName = add_event_name.text.toString()
        val eventDesc = add_event_desc.text.toString()
        val calendar = Calendar.getInstance()

        calendar.set(add_event_datePicker.year,add_event_datePicker.month,add_event_datePicker.dayOfMonth)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            calendar.set(Calendar.HOUR_OF_DAY, view?.add_event_timePicker?.hour!!)
            calendar.set(Calendar.MINUTE, view?.add_event_timePicker?.minute!!)
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, view?.add_event_timePicker?.currentHour!!)
            calendar.set(Calendar.MINUTE, view?.add_event_timePicker?.currentMinute!!)
        }


        if (!(TextUtils.isEmpty(eventName)) && (DateUtils.isToday(calendar.timeInMillis) || calendar.after(Calendar.getInstance()))) {
            val event = Event(0,eventName,eventDesc,calendar.timeInMillis,false,pos)
            eventViewModel.addEvent(event)
            Toast.makeText(requireContext(),"Event Added!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_addPageFragment_to_viewPageFragment)
        } else Toast.makeText(requireContext(),"Event Name is empty!", Toast.LENGTH_LONG).show()
    }
}