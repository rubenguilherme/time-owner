package guilherme.ruben.timeowner.mainpackage.fragments.add

import android.os.Bundle
import android.text.TextUtils
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
import guilherme.ruben.timeowner.data.items.Item
import guilherme.ruben.timeowner.data.items.ItemViewModel
import guilherme.ruben.timeowner.mainpackage.MainActivity
import kotlinx.android.synthetic.main.fragment_add_item.*
import kotlinx.android.synthetic.main.fragment_add_item.view.*
import kotlinx.android.synthetic.main.fragment_update_event.view.*
import java.util.*

class AddItemFragment : Fragment() {

    private lateinit var itemViewModel: ItemViewModel
    private var pos = 0
    private val colorArray = arrayOf(R.color.pri_red, R.color.pri_yellow, R.color.pri_green)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_item, container, false)

        view.add_item_textInputLayout?.setStartIconTintList(context?.let { ContextCompat.getColorStateList(it,colorArray[pos]) })

        view.add_item_fab.setOnClickListener {
            insertDataToDatabase()
        }

        val currentDate = (activity as MainActivity).getCurrentDate()
        view.add_item_datePicker.updateDate(currentDate.get(Calendar.YEAR),currentDate.get(Calendar.MONTH),currentDate.get(Calendar.DAY_OF_MONTH))

        itemViewModel = ViewModelProvider(this).get(ItemViewModel::class.java)

        return view
    }

    override fun onResume() {
        super.onResume()
        val priorities = resources.getStringArray(R.array.priorities)
        val arrayAdapter = PriorityAdapter(requireContext(),R.layout.dropdown_layout,priorities,colorArray)
        view?.add_item_autoCompleteTextView?.setAdapter(arrayAdapter)
        view?.add_item_autoCompleteTextView?.setOnItemClickListener{ _, _, i: Int, _ ->
            pos = i
            view?.add_item_textInputLayout?.setStartIconTintList(context?.let { ContextCompat.getColorStateList(it,colorArray[pos]) })
        }
    }

    private fun insertDataToDatabase() {
        val itemName = add_event_name.text.toString()
        val itemDesc = add_event_desc.text.toString()
        val calendar = (activity as MainActivity).getCurrentDate().clone() as Calendar
        calendar.set(add_item_datePicker.year,add_item_datePicker.month,add_item_datePicker.dayOfMonth)
        val date = calendar.timeInMillis

        if (!(TextUtils.isEmpty(itemName)) && (DateUtils.isToday(date) || calendar.after(Calendar.getInstance()))) {
            val item = Item(0,itemName,itemDesc,date,false,pos,-1)
            itemViewModel.addItem(item)
            Toast.makeText(requireContext(),"Item Added!",Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_addPageFragment_to_viewPageFragment)
        } else Toast.makeText(requireContext(),"Item Name is empty!",Toast.LENGTH_LONG).show()
    }

}