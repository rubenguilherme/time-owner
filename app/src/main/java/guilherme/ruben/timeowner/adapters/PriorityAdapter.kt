package guilherme.ruben.timeowner.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import guilherme.ruben.timeowner.R

class PriorityAdapter(
    cont: Context,
    val id: Int,
    private val stringArray: Array<String>,
    private val colorArray: Array<Int>
) :
    ArrayAdapter<String>(cont, id, stringArray) {

    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return stringArray.size
    }

    override fun getItem(position: Int): String? {
        return stringArray[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var vi = convertView
        if (vi == null)
            vi = inflater.inflate(R.layout.dropdown_layout,null)
        val textView = vi?.findViewById<TextView>(R.id.dropdown_textView)
        val imageView = vi?.findViewById<ImageView>(R.id.dropdown_icon)
        textView?.text = stringArray[position]
        if (stringArray.size > 3) imageView?.setImageResource(R.drawable.ic_priority_square)
        imageView?.setColorFilter(ContextCompat.getColor(context,colorArray[position]))
        return vi!!
    }
}