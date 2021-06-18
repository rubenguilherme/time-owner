package guilherme.ruben.timeowner.mainpackage.fragments

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import guilherme.ruben.timeowner.R
import guilherme.ruben.timeowner.adapters.EventAdapter
import guilherme.ruben.timeowner.adapters.ListAdapter
import guilherme.ruben.timeowner.data.events.Event
import guilherme.ruben.timeowner.data.events.EventViewModel
import guilherme.ruben.timeowner.data.items.Item
import guilherme.ruben.timeowner.data.items.ItemViewModel
import guilherme.ruben.timeowner.data.recurrents.RecurrentItem
import guilherme.ruben.timeowner.data.recurrents.RecurrentItemViewModel
import guilherme.ruben.timeowner.mainpackage.MainActivity
import kotlinx.android.synthetic.main.fragment_to_do_list.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ToDoListFragment : Fragment() {

    private lateinit var itemViewModel: ItemViewModel
    private lateinit var recurrentItemViewModel: RecurrentItemViewModel
    private lateinit var eventViewModel: EventViewModel
    private lateinit var toAddList: List<Item>
    private lateinit var recList: List<RecurrentItem>
    private lateinit var adapterToDo : ListAdapter
    private val expandedArray = Array(8) {true}

    // Event Lists
    private var redGroup : List<Event> = emptyList()
    private var orangeGroup : List<Event> = emptyList()
    private var yellowGroup : List<Event> = emptyList()
    private var greenGroup : List<Event> = emptyList()
    private var aquaGroup : List<Event> = emptyList()
    private var blueGroup : List<Event> = emptyList()
    private var darkBlueGroup : List<Event> = emptyList()
    private var magentaGroup : List<Event> = emptyList()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Disable app's fullscreen mode
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_to_do_list, container, false)

        // ViewModels
        itemViewModel = ViewModelProvider(this).get(ItemViewModel::class.java)
        recurrentItemViewModel = ViewModelProvider(this).get(RecurrentItemViewModel::class.java)
        eventViewModel = ViewModelProvider(this).get(EventViewModel::class.java)

        // Recycler View
        adapterToDo = ListAdapter(requireContext(), itemViewModel)
        val recyclerViewToDo = view.to_do_recyclerView
        recyclerViewToDo.adapter = adapterToDo
        recyclerViewToDo.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    override fun onResume() {
        super.onResume()
        // Read the data from database and categorize
        readItemDatabase(adapterToDo)
        readEventDatabase()

        //Set current date
        view?.to_do_current_date?.text = DateFormat.getDateInstance().format(Date((activity as MainActivity).getCurrentDate().timeInMillis))
        // Add new item button if not before the current day
        if(DateUtils.isToday((activity as MainActivity).getCurrentDate().timeInMillis) || (activity as MainActivity).getCurrentDate().after(Calendar.getInstance()))
        {
            view?.floatingActionButton?.visibility = View.VISIBLE
            view?.floatingActionButton?.setOnClickListener {
                findNavController().navigate(R.id.action_viewPageFragment_to_addPageFragment)
            }
        }
        else view?.floatingActionButton?.visibility = View.GONE
    }

    private fun readEventDatabase() {
        eventViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            event ->
            val eventList = event.filter { val fmt = SimpleDateFormat("yyyyMMdd",Locale.US)
                fmt.format(Date(it.date)).equals(fmt.format(Date((activity as MainActivity).getCurrentDate().timeInMillis))) }

            // Reset the arrays that keep the events
            redGroup = emptyList()
            orangeGroup = emptyList()
            yellowGroup = emptyList()
            greenGroup = emptyList()
            aquaGroup = emptyList()
            blueGroup = emptyList()
            darkBlueGroup = emptyList()
            magentaGroup = emptyList()

            // Add events to respective array
            for (i in eventList) {
                when (i.color) {
                    0 -> if (!redGroup.contains(i)) redGroup = redGroup.plus(i)
                    1 -> if (!orangeGroup.contains(i)) orangeGroup = orangeGroup.plus(i)
                    2 -> if (!yellowGroup.contains(i)) yellowGroup = yellowGroup.plus(i)
                    3 -> if (!greenGroup.contains(i)) greenGroup = greenGroup.plus(i)
                    4 -> if (!aquaGroup.contains(i)) aquaGroup = aquaGroup.plus(i)
                    5 -> if (!blueGroup.contains(i)) blueGroup = blueGroup.plus(i)
                    6 -> if (!darkBlueGroup.contains(i)) darkBlueGroup = darkBlueGroup.plus(i)
                    7 -> if (!magentaGroup.contains(i)) magentaGroup = magentaGroup.plus(i)
                }
            }

            val groupNames = (activity as MainActivity).getGroupNames()

            // For each group configure all the layout to be able to show the events and to become visible. Only if the array is not empty
            if (redGroup.isNotEmpty()) {
                view?.to_do_red_group_name?.text = groupNames[0]
                view?.to_do_red_layout?.visibility = View.VISIBLE
                val redRecyclerView = view?.to_do_red_recyclerView
                val adapterRed = EventAdapter(eventViewModel)
                adapterRed.setData(redGroup)
                redRecyclerView?.adapter = adapterRed
                redRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
                view?.to_do_red_expand_layout?.setOnClickListener {
                    if (expandedArray[0]) {
                        view?.to_do_red_expand_icon?.setImageResource(R.drawable.ic_expand_more)
                        view?.to_do_red_recyclerView?.visibility = View.GONE
                        expandedArray[0] = false
                    } else {
                        view?.to_do_red_expand_icon?.setImageResource(R.drawable.ic_expand_less)
                        view?.to_do_red_recyclerView?.visibility = View.VISIBLE
                        expandedArray[0] = true
                    }
                }
            }
            if (orangeGroup.isNotEmpty()) {
                view?.to_do_orange_group_name?.text = groupNames[1]
                view?.to_do_orange_layout?.visibility = View.VISIBLE
                val orangeRecyclerView = view?.to_do_orange_recyclerView
                val adapterOrange = EventAdapter(eventViewModel)
                adapterOrange.setData(orangeGroup)
                orangeRecyclerView?.adapter = adapterOrange
                orangeRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
                view?.to_do_orange_expand_layout?.setOnClickListener {
                    if (expandedArray[1]) {
                        view?.to_do_orange_expand_icon?.setImageResource(R.drawable.ic_expand_more)
                        view?.to_do_orange_recyclerView?.visibility = View.GONE
                        expandedArray[1] = false
                    } else {
                        view?.to_do_orange_expand_icon?.setImageResource(R.drawable.ic_expand_less)
                        view?.to_do_orange_recyclerView?.visibility = View.VISIBLE
                        expandedArray[1] = true
                    }
                }
            }
            if (yellowGroup.isNotEmpty()) {
                view?.to_do_yellow_group_name?.text = groupNames[2]
                view?.to_do_yellow_layout?.visibility = View.VISIBLE
                val yellowRecyclerView = view?.to_do_yellow_recyclerView
                val adapterYellow = EventAdapter(eventViewModel)
                adapterYellow.setData(yellowGroup)
                yellowRecyclerView?.adapter = adapterYellow
                yellowRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
                view?.to_do_yellow_expand_layout?.setOnClickListener {
                    if (expandedArray[2]) {
                        view?.to_do_yellow_expand_icon?.setImageResource(R.drawable.ic_expand_more)
                        view?.to_do_yellow_recyclerView?.visibility = View.GONE
                        expandedArray[2] = false
                    } else {
                        view?.to_do_yellow_expand_icon?.setImageResource(R.drawable.ic_expand_less)
                        view?.to_do_yellow_recyclerView?.visibility = View.VISIBLE
                        expandedArray[2] = true
                    }
                }
            }
            if (greenGroup.isNotEmpty()) {
                view?.to_do_green_group_name?.text = groupNames[3]
                view?.to_do_green_layout?.visibility = View.VISIBLE
                val greenRecyclerView = view?.to_do_green_recyclerView
                val adapterGreen = EventAdapter(eventViewModel)
                adapterGreen.setData(greenGroup)
                greenRecyclerView?.adapter = adapterGreen
                greenRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
                view?.to_do_green_expand_layout?.setOnClickListener {
                    if (expandedArray[3]) {
                        view?.to_do_green_expand_icon?.setImageResource(R.drawable.ic_expand_more)
                        view?.to_do_green_recyclerView?.visibility = View.GONE
                        expandedArray[3] = false
                    } else {
                        view?.to_do_green_expand_icon?.setImageResource(R.drawable.ic_expand_less)
                        view?.to_do_green_recyclerView?.visibility = View.VISIBLE
                        expandedArray[3] = true
                    }
                }
            }
            if (aquaGroup.isNotEmpty()) {
                view?.to_do_aqua_group_name?.text = groupNames[4]
                view?.to_do_aqua_layout?.visibility = View.VISIBLE
                val aquaRecyclerView = view?.to_do_aqua_recyclerView
                val adapterAqua = EventAdapter(eventViewModel)
                adapterAqua.setData(aquaGroup)
                aquaRecyclerView?.adapter = adapterAqua
                aquaRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
                view?.to_do_aqua_expand_layout?.setOnClickListener {
                    if (expandedArray[4]) {
                        view?.to_do_aqua_expand_icon?.setImageResource(R.drawable.ic_expand_more)
                        view?.to_do_aqua_recyclerView?.visibility = View.GONE
                        expandedArray[4] = false
                    } else {
                        view?.to_do_aqua_expand_icon?.setImageResource(R.drawable.ic_expand_less)
                        view?.to_do_aqua_recyclerView?.visibility = View.VISIBLE
                        expandedArray[4] = true
                    }
                }
            }
            if (blueGroup.isNotEmpty()) {
                view?.to_do_blue_group_name?.text = groupNames[5]
                view?.to_do_blue_layout?.visibility = View.VISIBLE
                val blueRecyclerView = view?.to_do_blue_recyclerView
                val adapterBlue = EventAdapter(eventViewModel)
                adapterBlue.setData(blueGroup)
                blueRecyclerView?.adapter = adapterBlue
                blueRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
                view?.to_do_blue_expand_layout?.setOnClickListener {
                    if (expandedArray[5]) {
                        view?.to_do_blue_expand_icon?.setImageResource(R.drawable.ic_expand_more)
                        view?.to_do_blue_recyclerView?.visibility = View.GONE
                        expandedArray[5] = false
                    } else {
                        view?.to_do_blue_expand_icon?.setImageResource(R.drawable.ic_expand_less)
                        view?.to_do_blue_recyclerView?.visibility = View.VISIBLE
                        expandedArray[5] = true
                    }
                }
            }
            if (darkBlueGroup.isNotEmpty()) {
                view?.to_do_dark_blue_group_name?.text = groupNames[6]
                view?.to_do_dark_blue_layout?.visibility = View.VISIBLE
                val darkBlueRecyclerView = view?.to_do_dark_blue_recyclerView
                val adapterDarkBlue = EventAdapter(eventViewModel)
                adapterDarkBlue.setData(darkBlueGroup)
                darkBlueRecyclerView?.adapter = adapterDarkBlue
                darkBlueRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
                view?.to_do_dark_blue_expand_layout?.setOnClickListener {
                    if (expandedArray[6]) {
                        view?.to_do_dark_blue_expand_icon?.setImageResource(R.drawable.ic_expand_more)
                        view?.to_do_dark_blue_recyclerView?.visibility = View.GONE
                        expandedArray[6] = false
                    } else {
                        view?.to_do_dark_blue_expand_icon?.setImageResource(R.drawable.ic_expand_less)
                        view?.to_do_dark_blue_recyclerView?.visibility = View.VISIBLE
                        expandedArray[6] = true
                    }
                }
            }
            if (magentaGroup.isNotEmpty()) {
                view?.to_do_magenta_group_name?.text = groupNames[7]
                view?.to_do_magenta_layout?.visibility = View.VISIBLE
                val magentaRecyclerView = view?.to_do_magenta_recyclerView
                val adapterMagenta = EventAdapter(eventViewModel)
                adapterMagenta.setData(magentaGroup)
                magentaRecyclerView?.adapter = adapterMagenta
                magentaRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
                view?.to_do_magenta_expand_layout?.setOnClickListener {
                    if (expandedArray[7]) {
                        view?.to_do_magenta_expand_icon?.setImageResource(R.drawable.ic_expand_more)
                        view?.to_do_magenta_recyclerView?.visibility = View.GONE
                        expandedArray[7] = false
                    } else {
                        view?.to_do_magenta_expand_icon?.setImageResource(R.drawable.ic_expand_less)
                        view?.to_do_magenta_recyclerView?.visibility = View.VISIBLE
                        expandedArray[7] = true
                    }
                }
            }
        })
        // If the array is empty, hide all the layout for that color of event
        if (redGroup.isEmpty()) view?.to_do_red_layout?.visibility = View.GONE
        if (orangeGroup.isEmpty()) view?.to_do_orange_layout?.visibility = View.GONE
        if (yellowGroup.isEmpty()) view?.to_do_yellow_layout?.visibility = View.GONE
        if (greenGroup.isEmpty()) view?.to_do_green_layout?.visibility = View.GONE
        if (aquaGroup.isEmpty()) view?.to_do_aqua_layout?.visibility = View.GONE
        if (blueGroup.isEmpty()) view?.to_do_blue_layout?.visibility = View.GONE
        if (darkBlueGroup.isEmpty()) view?.to_do_dark_blue_layout?.visibility = View.GONE
        if (magentaGroup.isEmpty()) view?.to_do_magenta_layout?.visibility = View.GONE
    }

    private fun readItemDatabase(adapterToDo: ListAdapter) {
        var readFlag = true
        itemViewModel.readAllData.observe(viewLifecycleOwner, Observer { item ->
            toAddList = item.filter {
                val fmt = SimpleDateFormat("yyyyMMdd",Locale.US)
                fmt.format(Date(it.date)).equals(fmt.format(Date((activity as MainActivity).getCurrentDate().timeInMillis)))
            }
            if((DateUtils.isToday((activity as MainActivity).getCurrentDate().timeInMillis) || (activity as MainActivity).getCurrentDate().after(Calendar.getInstance())))
            recurrentItemViewModel.readAllData.observe(viewLifecycleOwner, Observer { recItem ->
                if (readFlag) {
                    recList = recItem
                    readFlag = false
                    val today = (activity as MainActivity).getCurrentDate()
                    val day = today.get(Calendar.DAY_OF_WEEK)
                    for (recI in recList) {
                        // There's a recurrent item for this day of week
                        if (Character.getNumericValue(recI.days[day - 1]) == 1) {
                            var flag = true
                            for (i in toAddList) {
                                // Item was already added
                                if (i.itemName == recI.itemName) {
                                    flag = false // We don't want to add it again
                                    break
                                }
                            }
                            if (flag) { // There was no item with the same name. Now we add it to the database with this time and add it for the adapter
                                val itemAux = Item(0, recI.itemName, recI.itemDesc, today.time.time, false, recI.priority, recI.id)
                                toAddList = toAddList.plus(itemAux)
                                itemViewModel.addItem(itemAux)
                            }
                        }
                    }
                }
            })
            adapterToDo.setData(toAddList)
        })
    }

}