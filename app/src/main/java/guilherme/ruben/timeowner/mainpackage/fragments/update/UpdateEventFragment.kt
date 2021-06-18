package guilherme.ruben.timeowner.mainpackage.fragments.update

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import guilherme.ruben.timeowner.R
import guilherme.ruben.timeowner.adapters.PriorityAdapter
import guilherme.ruben.timeowner.data.events.Event
import guilherme.ruben.timeowner.data.events.EventViewModel
import guilherme.ruben.timeowner.mainpackage.MainActivity
import kotlinx.android.synthetic.main.fragment_add_event.view.*
import kotlinx.android.synthetic.main.fragment_update_event.*
import kotlinx.android.synthetic.main.fragment_update_event.view.*
import java.util.*

class UpdateEventFragment : Fragment() {
    private val args by navArgs<UpdateEventFragmentArgs>()
    private var pos = 0
    private val colorArray = arrayOf(R.color.cal_red,R.color.cal_orange,R.color.cal_yellow,R.color.cal_green,R.color.cal_aqua,R.color.cal_blue,R.color.cal_dark_blue,R.color.cal_magenta)
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var groupNames : Array<String>

    private lateinit var eventViewModel: EventViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_event, container, false)

        view.update_event_textInputLayout.setStartIconTintList(context?.let { ContextCompat.getColorStateList(it,colorArray[args.currentEvent.color]) })

        eventViewModel = ViewModelProvider(this).get(EventViewModel::class.java)
        groupNames = (activity as MainActivity).getGroupNames()

        // Set the initial text based on the sharedPreferences
        view.update_event_autoCompleteTextView.setText(groupNames[0])

        // Set the time to current time
        val currentTime = Calendar.getInstance()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            view.update_event_timePicker.hour = currentTime.get(Calendar.HOUR_OF_DAY)
            view.update_event_timePicker.minute = currentTime.get(Calendar.MINUTE)
        } else {
            view.update_event_timePicker.currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
            view.update_event_timePicker.currentMinute = currentTime.get(Calendar.MINUTE)
        }
        view.update_event_timePicker.setIs24HourView(DateFormat.is24HourFormat(context))
        val currentDate = (activity as MainActivity).getCurrentDate()
        view.update_event_datePicker.updateDate(currentDate.get(Calendar.YEAR),currentDate.get(Calendar.MONTH),currentDate.get(Calendar.DAY_OF_MONTH))

        view.update_event_name.setText(args.currentEvent.eventName)
        view.update_event_desc.setText(args.currentEvent.eventDesc)
        pos = args.currentEvent.color
        view.update_event_fab_update.setOnClickListener {
            updateEvent()
        }
        view.update_event_fab_delete.setOnClickListener {
            deleteEvent()
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        val arrayAdapter = PriorityAdapter(requireContext(),R.layout.dropdown_layout,groupNames,colorArray)
        //view?.update_event_autoCompleteTextView?.handler?.removeCallbacksAndMessages(null)
        view?.update_event_autoCompleteTextView?.setAdapter(arrayAdapter)
        view?.update_event_autoCompleteTextView?.setOnItemClickListener{ _, _, i: Int, _ ->
            pos = i
            view?.update_event_textInputLayout?.setStartIconTintList(context?.let { ContextCompat.getColorStateList(it,colorArray[pos]) })
        }
        view?.update_event_autoCompleteTextView?.setText(groupNames[args.currentEvent.color],false)
        view?.update_event_autoCompleteTextView?.setSelection(args.currentEvent.color)
    }

    private fun updateEvent() {
        val eventName = update_event_name.text.toString()
        val eventDesc = update_event_desc.text.toString()
        if (!(TextUtils.isEmpty(eventName))) {
            val calendar = Calendar.getInstance()

            calendar.set(update_event_datePicker.year,update_event_datePicker.month,update_event_datePicker.dayOfMonth)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                calendar.set(Calendar.HOUR_OF_DAY, view?.update_event_timePicker?.hour!!)
                calendar.set(Calendar.MINUTE, view?.update_event_timePicker?.minute!!)
            } else {
                calendar.set(Calendar.HOUR_OF_DAY, view?.update_event_timePicker?.currentHour!!)
                calendar.set(Calendar.MINUTE, view?.update_event_timePicker?.currentMinute!!)
            }
            val updatedEvent = Event(args.currentEvent.id,eventName,eventDesc,calendar.timeInMillis,args.currentEvent.done,pos)
            eventViewModel.updateEvent(updatedEvent)
            Toast.makeText(requireContext(),"Event Updated!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateEventFragment_to_viewPageFragment)
        } else Toast.makeText(requireContext(),"Event Name is empty!", Toast.LENGTH_LONG).show()
    }

    private fun deleteEvent() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            eventViewModel.deleteEvent(args.currentEvent)
            Toast.makeText(requireContext(),"Successfully removed: ${args.currentEvent.eventName}", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateEventFragment_to_viewPageFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete ${args.currentEvent.eventName}?")
        builder.setMessage("Are you sure you want to delete this event?")
        builder.create().show()
    }
}