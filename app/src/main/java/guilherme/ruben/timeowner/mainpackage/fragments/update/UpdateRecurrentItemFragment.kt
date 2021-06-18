package guilherme.ruben.timeowner.mainpackage.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import guilherme.ruben.timeowner.R
import guilherme.ruben.timeowner.adapters.PriorityAdapter
import guilherme.ruben.timeowner.data.items.Item
import guilherme.ruben.timeowner.data.items.ItemViewModel
import guilherme.ruben.timeowner.data.recurrents.RecurrentItem
import guilherme.ruben.timeowner.data.recurrents.RecurrentItemViewModel
import kotlinx.android.synthetic.main.fragment_update_recurrent.*
import kotlinx.android.synthetic.main.fragment_update_recurrent.view.*
import java.util.*

class UpdateRecurrentItemFragment : Fragment() {

    private val args by navArgs<UpdateRecurrentItemFragmentArgs>()
    private var pos = 0
    private val colorArray = arrayOf(R.color.pri_red, R.color.pri_yellow, R.color.pri_green)

    private lateinit var itemViewModel: ItemViewModel
    private lateinit var recurrentItemViewModel: RecurrentItemViewModel
    private var recItem: RecurrentItem? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_recurrent, container, false)

        view.update_recurrent_item_textInputLayout.setStartIconTintList(context?.let { ContextCompat.getColorStateList(it,colorArray[args.currentItem.priority]) })

        itemViewModel = ViewModelProvider(this).get(ItemViewModel::class.java)
        recurrentItemViewModel = ViewModelProvider(this).get(RecurrentItemViewModel::class.java)

        view.update_recurrent_item_name.setText(args.currentItem.itemName)
        view.update_recurrent_item_desc.setText(args.currentItem.itemDesc)
        pos = args.currentItem.priority
        view.update_recurrent_item_autoCompleteTextView.setSelection(args.currentItem.priority)
        val priorities = resources.getStringArray(R.array.priorities)
        view.update_recurrent_item_autoCompleteTextView.setText(priorities[args.currentItem.priority],false)

        recurrentItemViewModel.readSingleItem(args.currentItem.recId).observe(viewLifecycleOwner, Observer {
            item ->
            if (item != null) {
                recItem = item
                // Set the days
                view.update_recurrent_item_sun.isChecked = Integer.parseInt(recItem!!.days[0].toString()) == 1
                view.update_recurrent_item_mon.isChecked = Integer.parseInt(recItem!!.days[1].toString()) == 1
                view.update_recurrent_item_tue.isChecked = Integer.parseInt(recItem!!.days[2].toString()) == 1
                view.update_recurrent_item_wed.isChecked = Integer.parseInt(recItem!!.days[3].toString()) == 1
                view.update_recurrent_item_thu.isChecked = Integer.parseInt(recItem!!.days[4].toString()) == 1
                view.update_recurrent_item_fri.isChecked = Integer.parseInt(recItem!!.days[5].toString()) == 1
                view.update_recurrent_item_sat.isChecked = Integer.parseInt(recItem!!.days[6].toString()) == 1
            }
        })

        view.update_recurrent_item_fab_update.setOnClickListener {
            updateItem()
        }
        view.update_recurrent_item_fab_delete.setOnClickListener {
            deleteItem()
        }



        return view
    }

    override fun onResume() {
        super.onResume()
        val priorities = resources.getStringArray(R.array.priorities)
        val arrayAdapter = PriorityAdapter(requireContext(),R.layout.dropdown_layout,priorities,colorArray)
        view?.update_recurrent_item_autoCompleteTextView?.setAdapter(arrayAdapter)
        view?.update_recurrent_item_autoCompleteTextView?.setOnItemClickListener{ _, _, i: Int, _ ->
            pos = i
            view?.update_recurrent_item_textInputLayout?.setStartIconTintList(context?.let { ContextCompat.getColorStateList(it,colorArray[pos]) })
        }
    }

    private fun updateItem() {
        val itemName = update_recurrent_item_name.text.toString()
        val itemDesc = update_recurrent_item_desc.text.toString()
        if (!(TextUtils.isEmpty(itemName))) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes") { _, _ ->
            // Get the days of the week
            val days = (if (update_recurrent_item_sun.isChecked) "1" else "0").plus(
                (if (update_recurrent_item_mon.isChecked) "1" else "0").plus(
                    (if (update_recurrent_item_tue.isChecked) "1" else "0").plus(
                        (if (update_recurrent_item_wed.isChecked) "1" else "0").plus(
                            (if (update_recurrent_item_thu.isChecked) "1" else "0").plus(
                                (if (update_recurrent_item_fri.isChecked) "1" else "0").plus(
                                    if (update_recurrent_item_sat.isChecked) "1" else "0"))))))

            // Update the item only if today's day of the week is checked
            val today = Calendar.getInstance()
            val day = today.get(Calendar.DAY_OF_WEEK)
            if (Integer.parseInt(days[day - 1].toString()) == 1 || recItem == null) {
                val updatedItem = Item(
                    args.currentItem.id,
                    itemName,
                    itemDesc,
                    args.currentItem.date,
                    args.currentItem.done,
                    pos,
                    args.currentItem.recId
                )
                itemViewModel.updateItem(updatedItem)
            } else itemViewModel.deleteItem(args.currentItem)

            // Update the recurrent item
            if (recItem != null) {
                val updatedRecItem = RecurrentItem(
                        recItem!!.id,
                        itemName,
                        itemDesc,
                        days,
                        pos
                )
                recurrentItemViewModel.updateItem(updatedRecItem)
            }

            Toast.makeText(requireContext(),"Item Updated!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateRecurrentItemFragment_to_viewPageFragment)
            }
            builder.setNegativeButton("No") { _, _ -> }
            builder.setTitle("Update Recurrent Item?")
            builder.setMessage("Are you sure you want to update this recurrent item?")
            builder.create().show()
        } else Toast.makeText(requireContext(),"Item Name is empty!", Toast.LENGTH_LONG).show()
    }

    private fun deleteItem() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            if (recItem != null)
                recurrentItemViewModel.deleteItem(recItem!!)
            itemViewModel.deleteItem(args.currentItem)
            Toast.makeText(requireContext(),"Successfully removed: ${args.currentItem.itemName}",
                Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateRecurrentItemFragment_to_viewPageFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete ${args.currentItem.itemName}?")
        builder.setMessage("Are you sure you want to delete this item?")
        builder.create().show()
    }
}