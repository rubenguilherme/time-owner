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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import guilherme.ruben.timeowner.R
import guilherme.ruben.timeowner.adapters.PriorityAdapter
import guilherme.ruben.timeowner.data.items.Item
import guilherme.ruben.timeowner.data.items.ItemViewModel
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*

class UpdateItemFragment : Fragment() {

    private val args by navArgs<UpdateItemFragmentArgs>()
    private var pos = 0
    private val colorArray = arrayOf(R.color.pri_red, R.color.pri_yellow, R.color.pri_green)

    private lateinit var itemViewModel: ItemViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                          savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        view.update_item_textInputLayout.setStartIconTintList(context?.let { ContextCompat.getColorStateList(it,colorArray[args.currentItem.priority]) })

        itemViewModel = ViewModelProvider(this).get(ItemViewModel::class.java)

        view.update_item_name.setText(args.currentItem.itemName)
        view.update_item_desc.setText(args.currentItem.itemDesc)
        pos = args.currentItem.priority
        view.update_item_autoCompleteTextView.setSelection(args.currentItem.priority)
        val priorities = resources.getStringArray(R.array.priorities)
        view.update_item_autoCompleteTextView.setText(priorities[args.currentItem.priority],false)
        view.update_item_fab_update.setOnClickListener {
            updateItem()
        }
        view.update_item_fab_delete.setOnClickListener {
            deleteEvent()
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        val priorities = resources.getStringArray(R.array.priorities)
        val arrayAdapter = PriorityAdapter(requireContext(),R.layout.dropdown_layout,priorities,colorArray)
        view?.update_item_autoCompleteTextView?.setAdapter(arrayAdapter)
        view?.update_item_autoCompleteTextView?.setOnItemClickListener{ _, _, i: Int, _ ->
            pos = i
            view?.update_item_textInputLayout?.setStartIconTintList(context?.let { ContextCompat.getColorStateList(it,colorArray[pos]) })
        }
    }

    private fun updateItem() {
        val itemName = update_item_name.text.toString()
        val itemDesc = update_item_desc.text.toString()
        if (!(TextUtils.isEmpty(itemName))) {
            val updatedItem = Item(args.currentItem.id,itemName,itemDesc,args.currentItem.date,args.currentItem.done,pos,args.currentItem.recId)
            itemViewModel.updateItem(updatedItem)
            Toast.makeText(requireContext(),"Item Updated!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateFragment_to_viewPageFragment)
        } else Toast.makeText(requireContext(),"Item Name is empty!",Toast.LENGTH_LONG).show()
    }

    private fun deleteEvent() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            itemViewModel.deleteItem(args.currentItem)
            Toast.makeText(requireContext(),"Successfully removed: ${args.currentItem.itemName}",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_viewPageFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete ${args.currentItem.itemName}?")
        builder.setMessage("Are you sure you want to delete this item?")
        builder.create().show()
    }

}