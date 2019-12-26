package ch.hesso.keepin

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import ch.hesso.keepin.Utils.ConnectionsActivity
import ch.hesso.keepin.Utils.NearbyUsers
import ch.hesso.keepin.enums.Status
import ch.hesso.keepin.fragments.ContactsFragment
import ch.hesso.keepin.fragments.DiscoverFragment
import ch.hesso.keepin.fragments.ProfileFragment
import ch.hesso.keepin.pojos.PublicUser
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.Strategy
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : ConnectionsActivity() {

    private var userName = "Farid"

    private val STRATEGY = Strategy.P2P_CLUSTER

    private var SERVICE_ID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById(R.id.bottom_navigation) as BottomNavigationView
        val toolbar = findViewById(R.id.toolbar) as Toolbar

        toolbar.setTitle("");
        setSupportActionBar(toolbar)

        bottomNavigationView.setOnNavigationItemSelectedListener { item -> bottomNavigationItemSelected(item)}
        bottomNavigationView.setSelectedItemId(R.id.navigation_discover);

        SERVICE_ID = packageName
    }

    override fun onStart() {
        super.onStart()

        startDiscovering()
        startAdvertising()
    }

    override fun onStop() {
        super.onStop()

        stopDiscovering()
        stopAdvertising()
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

    override fun getName(): String {
        return userName
    }

    override fun getServiceId(): String {
        return SERVICE_ID
    }

    override fun getStrategy(): Strategy {
        return STRATEGY
    }

    override fun onEndpointDiscovered(endpoint: Endpoint) {
        // We found an advertiser!
        stopDiscovering()
        connectToEndpoint(endpoint)
    }

    override fun onConnectionInitiated(endpoint: Endpoint, connectionInfo: ConnectionInfo) {
        // A connection to another device has been initiated! We'll use the auth token, which is the
        // same on both devices, to pick a color to use when we're connected. This way, users can
        // visually see which device they connected with.

        // We accept the connection immediately.
        acceptConnection(endpoint)
    }

    override fun onEndpointConnected(endpoint: Endpoint) {
        Toast.makeText(
            this, getString(R.string.toast_connected, endpoint.name), Toast.LENGTH_SHORT
        )
            .show()
//        setState(State.CONNECTED)

        NearbyUsers.userList.addItem(PublicUser(endpoint.id, endpoint.name, Status.CONNECTED))
    }

    override fun onEndpointDisconnected(endpoint: Endpoint) {
        Toast.makeText(
            this, getString(R.string.toast_disconnected, endpoint.name), Toast.LENGTH_SHORT
        )
            .show()
//        setState(State.SEARCHING)

        NearbyUsers.userList.removeItem(endpoint.id)
    }

    override fun onConnectionFailed(endpoint: Endpoint) {
        // Let's try someone else.
//        if (getState() == State.SEARCHING) {
            startDiscovering()
//        }
    }
}
