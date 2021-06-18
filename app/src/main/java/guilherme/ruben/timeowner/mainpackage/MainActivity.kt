package guilherme.ruben.timeowner.mainpackage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import guilherme.ruben.timeowner.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var currentDate : Calendar
    private lateinit var groupNames : Array<String>

    /*
    *  Settings definition
    *
    *   First field - Name of user
    *   Second field - Occupation of user
    *   Third field - Date of birth
    *
    * */
    private lateinit var settings : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Make the app go fullscreen for the splash screen
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        initializeSharedPreferences()
        toolbar.visibility = View.GONE
        toolbar.overflowIcon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_menu)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        currentDate = Calendar.getInstance()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_group_names -> {
            findNavController(R.id.fragment).navigate(R.id.action_viewPageFragment_to_groupNamesFragment)
            true
        }

        R.id.action_help -> {
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }


    private fun initializeSharedPreferences() {
        val settings = getSharedPreferences("SETTINGS", MODE_PRIVATE)
        val prevStarted = settings.getBoolean("appPreviouslyStarted",false)
        groupNames = if (prevStarted) { // App has started, retrieve data from shared preferences
            val eventGroups = getSharedPreferences("EVENT_GROUPS", MODE_PRIVATE)
            arrayOf(eventGroups.getString("red", "Red")!!,
                eventGroups.getString("orange", "Orange")!!,
                eventGroups.getString("yellow", "Yellow")!!,
                eventGroups.getString("green", "Green")!!,
                eventGroups.getString("aqua", "Aqua")!!,
                eventGroups.getString("blue", "Blue")!!,
                eventGroups.getString("dark_blue", "Dark Blue")!!,
                eventGroups.getString("magenta", "Magenta")!!)
        } else { // App has not started, initialize data from shared preferences
            initializeEventGroups()
            arrayOf("Red", "Orange", "Yellow", "Green", "Aqua", "Blue", "Dark Blue", "Magenta")
        }
    }

    private fun initializeEventGroups() {
        val eventGroups = getSharedPreferences("EVENT_GROUPS", MODE_PRIVATE)
        val eventGroupsEditor = eventGroups.edit()
        eventGroupsEditor.putString("red","Red")
        eventGroupsEditor.putString("orange","Orange")
        eventGroupsEditor.putString("yellow","Yellow")
        eventGroupsEditor.putString("green","Green")
        eventGroupsEditor.putString("aqua","Aqua")
        eventGroupsEditor.putString("blue","Blue")
        eventGroupsEditor.putString("dark_blue","Dark Blue")
        eventGroupsEditor.putString("magenta","Magenta")
        eventGroupsEditor.apply()
    }

    fun getCurrentDate() : Calendar {
        return currentDate
    }

    fun setCurrentDate(currentDate : Calendar) {
        this.currentDate = currentDate
    }

    fun getGroupNames() : Array<String> {
        return groupNames
    }

    fun getSettings() : Array<String> {
        return settings
    }

    fun setSettings(a : Array<String> ) {
        settings = a
    }
}