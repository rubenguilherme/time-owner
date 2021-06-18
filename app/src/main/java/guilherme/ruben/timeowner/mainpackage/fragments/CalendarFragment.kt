package guilherme.ruben.timeowner.mainpackage.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager

import androidx.viewpager2.widget.ViewPager2
import guilherme.ruben.timeowner.R
import guilherme.ruben.timeowner.adapters.CalendarAdapter
import guilherme.ruben.timeowner.data.events.EventViewModel
import guilherme.ruben.timeowner.mainpackage.MainActivity
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import java.util.*
import kotlin.collections.HashMap

class CalendarFragment : Fragment() {

    private lateinit var adapterCalendar: CalendarAdapter
    private val calendarMatrix = Array(6) { Array(7) {0} }
    private var colorMatrix = Array(6) { Array(7) {Array(8) {false} } }
    private lateinit var currentDate : Calendar
    private lateinit var eventViewModel : EventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        currentDate = (activity as MainActivity).getCurrentDate()

        val viewPager = activity?.findViewById(R.id.pager) as ViewPager2

        eventViewModel = ViewModelProvider(this).get(EventViewModel::class.java)

        // Recycler View
        adapterCalendar = CalendarAdapter(requireContext(),requireActivity() as MainActivity,viewPager)
        val recyclerViewCalendar = view.calendar_recyclerView
        recyclerViewCalendar.adapter = adapterCalendar
        recyclerViewCalendar.layoutManager = GridLayoutManager(requireContext(),7)

        // Set the month to be displayed using the current time
        setMonth(adapterCalendar, view)

        // Create the on click listeners to change the month
        view.calendar_month_right.setOnClickListener {
            currentDate.set(Calendar.DAY_OF_MONTH,1)
            if (currentDate.get(Calendar.MONTH) == 11) {
                currentDate.set(Calendar.MONTH,0)
                currentDate.set(Calendar.YEAR, (currentDate.get(Calendar.YEAR) + 1))
            }
            else currentDate.set(Calendar.MONTH,currentDate.get(Calendar.MONTH) + 1)
            ObjectAnimator.ofFloat(recyclerViewCalendar, "translationX", 1000f).apply {
                duration = 200
                start()
            }
            setMonth(adapterCalendar, view)
            ObjectAnimator.ofFloat(recyclerViewCalendar, "translationX", 0f).apply {
                startDelay = 200
                duration = 0
                start()
            }
        }
        view.calendar_month_left.setOnClickListener {
            currentDate.set(Calendar.DAY_OF_MONTH,1)
            if (currentDate.get(Calendar.MONTH) == 0) {
                currentDate.set(Calendar.MONTH, 11)
                currentDate.set(Calendar.YEAR, (currentDate.get(Calendar.YEAR) - 1))
            }
            else currentDate.set(Calendar.MONTH, (currentDate.get(Calendar.MONTH) - 1))
            ObjectAnimator.ofFloat(recyclerViewCalendar, "translationX", -1000f).apply {
                duration = 200
                start()
            }
            setMonth(adapterCalendar, view)
            ObjectAnimator.ofFloat(recyclerViewCalendar, "translationX", 0f).apply {
                startDelay = 200
                duration = 0
                start()
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        val viewPager = activity?.findViewById(R.id.pager) as ViewPager2
        adapterCalendar.viewPager = viewPager

        val groupNames = (activity as MainActivity).getGroupNames()

        // Add the labels for custom group names
        if (groupNames[0] == "Red") view?.cal_red_subtitle?.visibility = View.GONE
        else view?.cal_red_subtitle_text?.text = groupNames[0]
        if (groupNames[1] == "Orange") view?.cal_orange_subtitle?.visibility = View.GONE
        else view?.cal_orange_subtitle_text?.text = groupNames[1]
        if (groupNames[2] == "Yellow") view?.cal_yellow_subtitle?.visibility = View.GONE
        else view?.cal_yellow_subtitle_text?.text = groupNames[2]
        if (groupNames[3] == "Green") view?.cal_green_subtitle?.visibility = View.GONE
        else view?.cal_green_subtitle_text?.text = groupNames[3]
        if (groupNames[4] == "Aqua") view?.cal_aqua_subtitle?.visibility = View.GONE
        else view?.cal_aqua_subtitle_text?.text = groupNames[4]
        if (groupNames[5] == "Blue") view?.cal_blue_subtitle?.visibility = View.GONE
        else view?.cal_blue_subtitle_text?.text = groupNames[5]
        if (groupNames[6] == "Dark Blue") view?.cal_dark_blue_subtitle?.visibility = View.GONE
        else view?.cal_dark_blue_subtitle_text?.text = groupNames[6]
        if (groupNames[7] == "Magenta") view?.cal_magenta_subtitle?.visibility = View.GONE
        else view?.cal_magenta_subtitle_text?.text = groupNames[7]
    }

    // Set the month to be displayed in the recycler view
    private fun setMonth(adapterCalendar : CalendarAdapter, view: View) {
        synchronized(currentDate) {
            val firstDayOfMonth = currentDate.clone() as Calendar
            firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1)
            val nDays = firstDayOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH)

            val dayOfWeek = firstDayOfMonth.get(Calendar.DAY_OF_WEEK)

            var cnt = 1

            // Add the days to each of the positions in the matrix
            for (position in 0 until calendarMatrix.size * calendarMatrix[0].size) {
                if ((dayOfWeek - 1) <= position && position < nDays + dayOfWeek - 1)
                    calendarMatrix[position / 7][position % 7] = cnt++
                else calendarMatrix[position / 7][position % 7] = 0
            }

            // For each of the positions in the recycler view add the stripes representing an event in that day
            colorMatrix = Array(6) { Array(7) {Array(8) {false} } }
            eventViewModel.readAllData.observe(viewLifecycleOwner, Observer {
                events ->
                val cal = Calendar.getInstance()
                val monthsEvents = events.filter {
                    cal.timeInMillis = it.date
                    cal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH) && cal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
                }
                val hashMap = HashMap<Int,Array<Boolean>>()

                for (e in monthsEvents) {
                    cal.timeInMillis = e.date
                    val day = cal.get(Calendar.DAY_OF_MONTH)
                    var curr = hashMap[day]
                    hashMap[day] = if (curr == null) {
                        curr = Array(8) {false}
                        curr[e.color] = true
                        curr
                    }
                    else {
                        curr[e.color] = true
                        curr
                    }
                }
                var cnt2 = 1
                for (position in 0 until calendarMatrix.size * calendarMatrix[0].size) {
                    if ((dayOfWeek - 1) <= position && position < nDays + dayOfWeek - 1) {
                        val aux = hashMap[cnt2++]
                        if (aux != null) {
                            colorMatrix[position / 7][position % 7] = aux
                            colorMatrix[position / 7][position % 7].toString()
                        }
                    }
                }
                adapterCalendar.setData(calendarMatrix, colorMatrix)
            })
            adapterCalendar.setData(calendarMatrix, colorMatrix)
            val months = resources.getStringArray(R.array.months)
            val date = months[currentDate.get(Calendar.MONTH)] + ", " + currentDate.get(Calendar.YEAR).toString()
            view.calendar_date.text = date
        }
    }
}