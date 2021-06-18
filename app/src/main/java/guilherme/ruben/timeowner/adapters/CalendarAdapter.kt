package guilherme.ruben.timeowner.adapters

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import guilherme.ruben.timeowner.R
import guilherme.ruben.timeowner.mainpackage.MainActivity
import kotlinx.android.synthetic.main.calendar_square.view.*
import kotlinx.android.synthetic.main.to_do_list_row.view.*
import java.text.SimpleDateFormat
import java.util.*

class CalendarAdapter(val context: Context, private val activity: MainActivity, var viewPager: ViewPager2): RecyclerView.Adapter<CalendarAdapter.MyViewHolder>() {

    private var calendarMatrix = Array(6) { Array(7) {0} }
    private var colorMatrix = Array(6) { Array(7) {Array(8) {false} } }
    private var currentSelection = -1

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.calendar_square,parent,false))
    }

    override fun getItemCount(): Int {
        return calendarMatrix.size * calendarMatrix[0].size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currDay = calendarMatrix[position / 7][(position) % 7]
        holder.itemView.calendar_day.text = if (currDay == 0) "" else currDay.toString()
        val fmt = SimpleDateFormat("yyyyMMdd",Locale.US)
        val auxDate = activity.getCurrentDate().clone() as Calendar
        auxDate.set(Calendar.DAY_OF_MONTH, currDay)
        if (fmt.format(Date(Calendar.getInstance().timeInMillis)).equals(fmt.format(Date(auxDate.timeInMillis)))) {
            val typedValue = TypedValue()
            activity.theme.resolveAttribute(R.attr.today_color,typedValue,true)
            holder.itemView.calendar_day.setTextColor(ContextCompat.getColor(context, typedValue.resourceId))
        }
        if (currDay != 0)
            holder.itemView.setOnClickListener {
                val curr = activity.getCurrentDate()
                curr.set(Calendar.DAY_OF_MONTH,calendarMatrix[position / 7][(position) % 7])
                activity.setCurrentDate(curr)
                viewPager.currentItem = 0
                currentSelection = position
                Handler(Looper.getMainLooper()).postDelayed({
                    notifyDataSetChanged()
                }, 200)
            }

        if (currentSelection == position)
            holder.itemView.calendar_square.background = ContextCompat.getDrawable(context,R.drawable.current_day)
        else holder.itemView.calendar_square.backgroundTintList = null

        // Color Group
        val currColors = colorMatrix[position / 7][(position) % 7]
        holder.itemView.cal_red.visibility = if (currColors[0]) View.VISIBLE else View.GONE
        holder.itemView.cal_orange.visibility = if (currColors[1]) View.VISIBLE else View.GONE
        holder.itemView.cal_yellow.visibility = if (currColors[2]) View.VISIBLE else View.GONE
        holder.itemView.cal_green.visibility = if (currColors[3]) View.VISIBLE else View.GONE
        holder.itemView.cal_aqua.visibility = if (currColors[4]) View.VISIBLE else View.GONE
        holder.itemView.cal_blue.visibility = if (currColors[5]) View.VISIBLE else View.GONE
        holder.itemView.cal_dark_blue.visibility = if (currColors[6]) View.VISIBLE else View.GONE
        holder.itemView.cal_magenta.visibility = if (currColors[7]) View.VISIBLE else View.GONE
    }

    fun setData(matrix : Array<Array<Int>>, color : Array<Array<Array<Boolean>>>) {
        colorMatrix = color
        calendarMatrix = matrix
        val currDay = activity.getCurrentDate().get(Calendar.DAY_OF_MONTH)
        for (position in 0..(calendarMatrix.size * calendarMatrix[0].size))
            if (calendarMatrix[position / 7][(position) % 7] == currDay) {
                currentSelection = position
                break
            }
        notifyDataSetChanged()
    }



}