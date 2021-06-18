package guilherme.ruben.timeowner.adapters

import android.content.Context
import android.graphics.Paint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import guilherme.ruben.timeowner.R
import guilherme.ruben.timeowner.data.items.Item
import guilherme.ruben.timeowner.data.items.ItemViewModel
import guilherme.ruben.timeowner.mainpackage.fragments.MainPageFragmentDirections
import kotlinx.android.synthetic.main.to_do_list_row.view.*

class ListAdapter(val context: Context, private val itemViewModel: ItemViewModel): RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var itemList = emptyList<Item>()
    private val colorArray = arrayOf(R.color.pri_red, R.color.pri_yellow, R.color.pri_green)

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.to_do_list_row,parent,false))

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.itemView.to_do_name.text = currentItem.itemName
        if(TextUtils.isEmpty(currentItem.itemDesc)) {
            holder.itemView.to_do_desc.visibility = View.GONE
        } else {
            holder.itemView.to_do_desc.text = currentItem.itemDesc
            holder.itemView.to_do_desc.visibility = View.VISIBLE
        }

        if (currentItem.recId != (-1)) holder.itemView.to_do_rec.visibility = View.VISIBLE
        else holder.itemView.to_do_rec.visibility = View.GONE

        holder.itemView.to_do_priority.setColorFilter(ContextCompat.getColor(context,colorArray[currentItem.priority]))
        if (currentItem.done) {
            holder.itemView.to_do_desc.paintFlags = holder.itemView.to_do_desc.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.itemView.to_do_name.paintFlags = holder.itemView.to_do_name.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.itemView.to_do_button.setImageResource(R.drawable.ic_done)
            holder.itemView.to_do_item_layout.setBackgroundResource(R.drawable.item_border_done)
        } else {
            holder.itemView.to_do_desc.paintFlags = holder.itemView.to_do_desc.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.itemView.to_do_name.paintFlags = holder.itemView.to_do_name.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.itemView.to_do_button.setImageResource(R.drawable.ic_to_do)
            holder.itemView.to_do_item_layout.setBackgroundResource(R.drawable.item_border_to_do)
        }

        holder.itemView.to_do_item_layout.setOnClickListener {
            val action = if (currentItem.recId != (-1)) MainPageFragmentDirections.actionViewPageFragmentToUpdateRecurrentItemFragment(currentItem)
            else MainPageFragmentDirections.actionViewPageFragmentToUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
        holder.itemView.to_do_button.setOnClickListener {
            currentItem.done = !currentItem.done
            itemViewModel.updateItem(currentItem)
        }
    }

    fun setData(item: List<Item>) {
        this.itemList = item
        notifyDataSetChanged()
    }


}