package ch.hesso.keepin

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import ch.hesso.keepin.fragments.ContactsFragment
import ch.hesso.keepin.fragments.DiscoverFragment
import ch.hesso.keepin.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.DarkAppTheme)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById(R.id.bottom_navigation) as BottomNavigationView
        val toolbar = findViewById(R.id.toolbar) as Toolbar

        toolbar.setTitle("");
        setSupportActionBar(toolbar)

        bottomNavigationView.setOnNavigationItemSelectedListener { item -> bottomNavigationItemSelected(item)}
        bottomNavigationView.setSelectedItemId(R.id.navigation_discover);
    }

    fun bottomNavigationItemSelected(item : MenuItem) : Boolean {

        var selectedFragment : Fragment ?= null
        var elevation : Float = 4f

        when(item.itemId) {

            R.id.navigation_profile -> {
                selectedFragment = ProfileFragment()
            }
            R.id.navigation_contacts -> {
                selectedFragment = ContactsFragment()
            }
            else ->  {
                selectedFragment = DiscoverFragment()
                elevation = 0f
            }
        }

        supportActionBar?.elevation = elevation
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment!!).commit()
        return true;
    }
}
