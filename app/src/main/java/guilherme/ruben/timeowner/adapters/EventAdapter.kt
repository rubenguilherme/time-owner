package guilherme.ruben.timeowner.adapters

import android.graphics.Paint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import guilherme.ruben.timeowner.R
import guilherme.ruben.timeowner.data.events.Event
import guilherme.ruben.timeowner.data.events.EventViewModel
import guilherme.ruben.timeowner.mainpackage.fragments.MainPageFragmentDirections
import kotlinx.android.synthetic.main.event_row.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class EventAdapter(private val eventViewModel: EventViewModel): RecyclerView.Adapter<EventAdapter.MyViewHolder>() {

    private var eventList = emptyList<Event>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.event_row,parent,false))

    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentEvent = eventList[position]
        holder.itemView.event_name.text = currentEvent.eventName
        if(TextUtils.isEmpty(currentEvent.eventDesc)) {
            holder.itemView.event_desc.visibility = View.GONE
        } else {
            holder.itemView.event_desc.text = currentEvent.eventDesc
            holder.itemView.event_desc.visibility = View.VISIBLE
        }

        // Set the date
        val simpleDateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm", Locale.getDefault())
        holder.itemView.event_date.text = simpleDateFormat.format(Date(currentEvent.date))

        if (currentEvent.done) {
            holder.itemView.event_desc.paintFlags = holder.itemView.event_desc.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.itemView.event_name.paintFlags = holder.itemView.event_name.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.itemView.event_button.setImageResource(R.drawable.ic_done)
            holder.itemView.event_item_layout.setBackgroundResource(R.drawable.item_border_done)
        } else {
            holder.itemView.event_desc.paintFlags = holder.itemView.event_desc.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.itemView.event_name.paintFlags = holder.itemView.event_name.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.itemView.event_button.setImageResource(R.drawable.ic_to_do)
            holder.itemView.event_item_layout.setBackgroundResource(R.drawable.item_border_to_do)
        }

        holder.itemView.event_item_layout.setOnClickListener {
            val action = MainPageFragmentDirections.actionViewPageFragmentToUpdateEventFragment(currentEvent)
            holder.itemView.findNavController().navigate(action)
        }
        holder.itemView.event_button.setOnClickListener {
            currentEvent.done = !currentEvent.done
            eventViewModel.updateEvent(currentEvent)
        }
    }

    fun setData(item: List<Event>) {
        this.eventList = item
        notifyDataSetChanged()
    }


}