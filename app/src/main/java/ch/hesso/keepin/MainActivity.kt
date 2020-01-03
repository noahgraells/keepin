package ch.hesso.keepin

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import ch.hesso.keepin.Utils.ConnectionsActivity
import ch.hesso.keepin.Utils.MessageReceived
import ch.hesso.keepin.Utils.NearbyUsers
import ch.hesso.keepin.enums.Status
import ch.hesso.keepin.fragments.ContactsFragment
import ch.hesso.keepin.fragments.DiscoverFragment
import ch.hesso.keepin.fragments.ProfileFragment
import ch.hesso.keepin.fragments.SelectedUserFragment
import ch.hesso.keepin.pojos.PublicUser
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.Strategy
import com.google.android.material.bottomnavigation.BottomNavigationView
import ch.hesso.keepin.enums.MessageType
import ch.hesso.keepin.pojos.Message
import ch.hesso.keepin.pojos.UserInformations
import org.apache.commons.lang3.SerializationUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_contacts.*


class MainActivity : ConnectionsActivity() {

    var myUserInformations : UserInformations = UserInformations()

    private val STRATEGY = Strategy.P2P_CLUSTER

    private var SERVICE_ID = ""

    private val listeners = ArrayList<MessageReceived>()

    fun addListener(toAdd: MessageReceived) {
        listeners.add(toAdd)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.DarkAppTheme)
        setContentView(R.layout.activity_main)

        loadUserInformations()
        loadContacts()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        toolbar.title = ""
        setSupportActionBar(toolbar)

        bottomNavigationView.setOnNavigationItemSelectedListener { item -> bottomNavigationItemSelected(item)}
        bottomNavigationView.selectedItemId = R.id.navigation_profile

        SERVICE_ID = packageName
    }

    fun loadUserInformations()
    {
        val mPrefs = getPreferences(Context.MODE_PRIVATE)
        val gson = Gson()
        val json = mPrefs.getString(getString(R.string.saved_informations_key), "")
        val obj = gson.fromJson(json, UserInformations::class.java)?: return
        myUserInformations = obj
    }

    fun loadContacts()
    {
        val mPrefs = getPreferences(Context.MODE_PRIVATE)
        val gson = Gson()
        val json = mPrefs.getString(getString(R.string.saved_contacts_key), "")

        val type = object : TypeToken<ArrayList<UserInformations>>() {}.type

        val obj = gson.fromJson<ArrayList<UserInformations>>(json, type) ?: return
        NearbyUsers.contacts = obj

        Log.d("Keepin","LENGTH : " + NearbyUsers.contacts.size + " ::: " + json)
    }

    fun saveContacts()
    {
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        val prefsEditor = sharedPref.edit()
        val gson = Gson()
        val json = gson.toJson(NearbyUsers.contacts)
        prefsEditor.putString(getString(R.string.saved_contacts_key), json)
        prefsEditor.commit()
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

    override fun onDestroy() {
        super.onDestroy()
        saveContacts()
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
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
        return true
    }

    override fun getName(): String {
        return myUserInformations.firstName
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
        if (!NearbyUsers.userList.contains(endpoint.id))
        {
            connectToEndpoint(endpoint)
        }
    }

    override fun startDiscovering() {
        super.startDiscovering()

        Toast.makeText(
            this, getString(R.string.toast_discover), Toast.LENGTH_LONG
        ).show()
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
        ).show()
//        setState(State.CONNECTED)

        NearbyUsers.userList.addItem(PublicUser(endpoint.id, endpoint.name, Status.CONNECTED))
        startDiscovering()
    }

    override fun onEndpointDisconnected(endpoint: Endpoint) {
        Toast.makeText(
            this, getString(R.string.toast_disconnected, endpoint.name), Toast.LENGTH_SHORT
        ).show()
//        setState(State.SEARCHING)

        NearbyUsers.userList.removeItem(endpoint.id)
    }

    override fun onConnectionFailed(endpoint: Endpoint) {
        // Let's try someone else.
        if (!isDiscovering) {
            startDiscovering()
        }
    }

    fun userSelected(view : View)
    {
        val vwParentRow = view as LinearLayout
        val child = vwParentRow.getChildAt(1) as TextView
        val btnChild = vwParentRow.getChildAt(2) as ImageButton

        val endpointId = NearbyUsers.userList.getUser(child.text.toString())?.endpointId

        var fragment : Fragment? = SelectedUserFragment()
        val args = Bundle()
        args.putString(getString(R.string.endpoint_id_key), endpointId)
        fragment!!.arguments = args

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit()
    }

    fun contactSelected(view: View)
    {
        val vwParentRow = view as LinearLayout
        val image = vwParentRow.getChildAt(0) as ImageView
        val firstName = (vwParentRow.getChildAt(1) as TextView).text.toString()
        val lastName = (vwParentRow.getChildAt(2) as TextView).text.toString()


        var fragment : Fragment? = SelectedUserFragment()
        val args = Bundle()
        args.putString(getString(R.string.firstname_key), firstName)
        args.putString(getString(R.string.lastname_key), lastName)
        fragment!!.arguments = args

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit()
    }

    fun requestAccept(view : View)
    {
        val vwParentRow = view.parent as LinearLayout
        val image = vwParentRow.getChildAt(0) as ImageView
        val firstName = vwParentRow.getChildAt(1) as TextView
        val endpointId = (vwParentRow.getChildAt(2) as TextView).text.toString()

        sendMessage(Message(MessageType.USER_INFORMATIONS, myUserInformations), endpointId)

        removeNotification(endpointId)
    }

    fun requestRefuse(view : View)
    {
        val vwParentRow = view.parent as LinearLayout
        val image = vwParentRow.getChildAt(0) as ImageView
        val firstName = vwParentRow.getChildAt(1) as TextView
        val endpointId = (vwParentRow.getChildAt(2) as TextView).text.toString()

        removeNotification(endpointId)
    }

    private fun removeNotification(endpointId: String)
    {
        NearbyUsers.notificationList.removeItem(endpointId)
    }

    fun sendMessage(message: Message, endpointId: String)
    {
        val payload = Payload.fromBytes(SerializationUtils.serialize(message))
        send(payload, endpointId)

    }


    override fun onReceive(endpoint: Endpoint?, payload: Payload?) {
        val bytes = payload?.asBytes() ?:return
        val message = SerializationUtils.deserialize<Message>(bytes)

        if (message.type == MessageType.USER_INFORMATIONS)
        {
            var userInformations = message.content as UserInformations
            addContact(userInformations)
        }

        for (listener in listeners)
            listener.messageReceived(endpoint, message)
    }

    private fun addContact(userInformations: UserInformations)
    {
        if (!NearbyUsers.contacts.any{ u -> u.firstName == userInformations.firstName && u.lastName == userInformations.lastName})
        {
            NearbyUsers.contacts.add(userInformations)
        }
    }

}
