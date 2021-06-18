package guilherme.ruben.timeowner.mainpackage.fragments.add

import android.os.Bundle
import android.text.TextUtils
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
import guilherme.ruben.timeowner.data.items.ItemViewModel
import guilherme.ruben.timeowner.data.recurrents.RecurrentItem
import guilherme.ruben.timeowner.data.recurrents.RecurrentItemViewModel
import kotlinx.android.synthetic.main.fragment_add_recurrent_item.*
import kotlinx.android.synthetic.main.fragment_add_recurrent_item.view.*
import java.util.*


class AddRecurrentItemFragment : Fragment() {


    private lateinit var recurrentItemViewModel: RecurrentItemViewModel
    private lateinit var itemViewModel: ItemViewModel
    private var pos = 0
    private val colorArray = arrayOf(R.color.pri_red, R.color.pri_yellow, R.color.pri_green)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_recurrent_item, container, false)

        view.add_recurrent_item_textInputLayout.setStartIconTintList(context?.let { ContextCompat.getColorStateList(it,colorArray[pos]) })

        view.add_recurrent_item_fab.setOnClickListener {
            insertDataToDatabase()
        }

        recurrentItemViewModel = ViewModelProvider(this).get(RecurrentItemViewModel::class.java)

        return view
    }

    override fun onResume() {
        super.onResume()
        val priorities = resources.getStringArray(R.array.priorities)
        val arrayAdapter = PriorityAdapter(requireContext(),R.layout.dropdown_layout,priorities,colorArray)
        view?.add_recurrent_item_autoCompleteTextView?.setAdapter(arrayAdapter)
        view?.add_recurrent_item_autoCompleteTextView?.setOnItemClickListener{ _, _, i: Int, _ ->
            pos = i
            view?.add_recurrent_item_textInputLayout?.setStartIconTintList(context?.let { ContextCompat.getColorStateList(it,colorArray[pos]) })
        }
    }

    private fun insertDataToDatabase() {
        val itemName = add_recurrent_item_name.text.toString()
        if (!(TextUtils.isEmpty(itemName))) {
            val itemDesc = add_recurrent_item_desc.text.toString()
            val days = (if (add_recurrent_item_sun.isChecked) "1" else "0").plus(
                    (if (add_recurrent_item_mon.isChecked) "1" else "0").plus(
                            (if (add_recurrent_item_tue.isChecked) "1" else "0").plus(
                                    (if (add_recurrent_item_wed.isChecked) "1" else "0").plus(
                                            (if (add_recurrent_item_thu.isChecked) "1" else "0").plus(
                                                    (if (add_recurrent_item_fri.isChecked) "1" else "0").plus(
                                                            if (add_recurrent_item_sat.isChecked) "1" else "0"))))))
            val today = Calendar.getInstance()
            val day = today.get(Calendar.DAY_OF_WEEK)
            /*if (Character.getNumericValue(days[day - 1]) == 1) {
                // Add the recurrent item to the database for today because this day of the week is already marked
                itemViewModel = ViewModelProvider(this).get(ItemViewModel::class.java)
                val item = Item(0,itemName,itemDesc,today.time.time,false,pos,true)
                itemViewModel.addItem(item)
            }*/
            val recItem = RecurrentItem(0,itemName,itemDesc,days,pos)
            recurrentItemViewModel.addItem(recItem)
            Toast.makeText(requireContext(),"Item Added!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_addPageFragment_to_viewPageFragment)
        } else Toast.makeText(requireContext(),"Item Name is empty!", Toast.LENGTH_LONG).show()
    }

}