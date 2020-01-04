package ch.hesso.keepin

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import ch.hesso.keepin.utils.ConnectionsActivity
import ch.hesso.keepin.utils.MessageReceived
import ch.hesso.keepin.utils.NearbyUsers
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


class MainActivity : ConnectionsActivity() {

    var myUserInformations : UserInformations = UserInformations()
    private var profileCreated : Boolean = false

    private var txvToolbar : TextView ?= null

    private val STRATEGY = Strategy.P2P_CLUSTER
    private var SERVICE_ID = ""

    private val listeners = ArrayList<MessageReceived>()

    /**
     * Add a listener to the list to be notified of a message received
     */
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

        txvToolbar = findViewById(R.id.toolbarTextView)

        toolbar.title = ""
        setSupportActionBar(toolbar)

        bottomNavigationView.setOnNavigationItemSelectedListener { item -> bottomNavigationItemSelected(item)}

        if (profileCreated)
        {
            bottomNavigationView.selectedItemId = R.id.navigation_discover
        }
        else
        {
            bottomNavigationView.selectedItemId = R.id.navigation_profile
        }


        SERVICE_ID = packageName
    }

    /**
     * Load the user informations that have been saved on the phone
     */
    private fun loadUserInformations()
    {
        val mPrefs = getPreferences(Context.MODE_PRIVATE)
        val gson = Gson()
        val json = mPrefs.getString(getString(R.string.saved_informations_key), "")
        val obj = gson.fromJson(json, UserInformations::class.java)?: return
        myUserInformations = obj
        profileCreated = true
    }

    /**
     * Load the previously accepted contacts
     */
    private fun loadContacts()
    {
        val mPrefs = getPreferences(Context.MODE_PRIVATE)
        val gson = Gson()
        val json = mPrefs.getString(getString(R.string.saved_contacts_key), "")

        val type = object : TypeToken<ArrayList<UserInformations>>() {}.type

        val obj = gson.fromJson<ArrayList<UserInformations>>(json, type) ?: return
        NearbyUsers.contacts = obj
    }

    /**
     * Save the list of contacts in the shared preferences
     */
    private fun saveContacts()
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

    /**
     * Called when an item has been selected on the bottom navigation view. Change the main fragment accordingly
     */
    fun bottomNavigationItemSelected(item : MenuItem) : Boolean {

        var selectedFragment : Fragment ?= null
        var elevation : Float = 4f

        when(item.itemId) {

            R.id.navigation_profile -> {
                selectedFragment = ProfileFragment()
                txvToolbar?.text = getString(R.string.title_profile)
            }
            R.id.navigation_contacts -> {
                selectedFragment = ContactsFragment()
                txvToolbar?.text = getString(R.string.title_contacts)
            }
            else ->  {
                selectedFragment = DiscoverFragment()
                txvToolbar?.text = getString(R.string.app_name)
                elevation = 0f
            }
        }

        supportActionBar?.elevation = elevation
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
        return true
    }

    /**
     * The name that is used when connecting to a nearby device
     */
    override fun getName(): String {
        return myUserInformations.firstName
    }

    /**
     * The service ID used to identify the app (here it's the package name)
     */
    override fun getServiceId(): String {
        return SERVICE_ID
    }

    /**
     * Get the strategy used for the communication
     */
    override fun getStrategy(): Strategy {
        return STRATEGY
    }

    /**
     * Called when an endpoint has been discovered. If we are not already connected to it, we try
     * the connection
     */
    override fun onEndpointDiscovered(endpoint: Endpoint) {
        // We found an advertiser!
        stopDiscovering()
        if (!NearbyUsers.userList.contains(endpoint.id))
        {
            connectToEndpoint(endpoint)
        }
    }

    /**
     * When the discovery has started, display a message so the user is aware that it is working
     */
    override fun startDiscovering() {
        super.startDiscovering()

        Toast.makeText(
            this, getString(R.string.toast_discover), Toast.LENGTH_LONG
        ).show()
    }

    /**
     * When the connection has been initiated with a device, automatically accept the connection
     */
    override fun onConnectionInitiated(endpoint: Endpoint, connectionInfo: ConnectionInfo) {
        // We accept the connection immediately.
        acceptConnection(endpoint)
    }

    /**
     * When we are connected to a device, add it to the list of connected devices
     */
    override fun onEndpointConnected(endpoint: Endpoint) {
        Toast.makeText(
            this, getString(R.string.toast_connected, endpoint.name), Toast.LENGTH_SHORT
        ).show()

        NearbyUsers.userList.addItem(PublicUser(endpoint.id, endpoint.name, Status.CONNECTED))
        startDiscovering()
    }

    /**
     * When a device has been disconnected, we remove it from the list of connected devices
     */
    override fun onEndpointDisconnected(endpoint: Endpoint) {
        Toast.makeText(
            this, getString(R.string.toast_disconnected, endpoint.name), Toast.LENGTH_SHORT
        ).show()

        NearbyUsers.userList.removeItem(endpoint.id)
    }

    /**
     * When the connection has failed, we restart the discovery
     */
    override fun onConnectionFailed(endpoint: Endpoint) {
        // Let's try someone else.
        if (!isDiscovering) {
            startDiscovering()
        }
    }

    /**
     * Called when a user has been selected inside the search fragment
     * If the user informations have not been requested yet, we send a request. Otherwise they
     * have accepted our request and their informations are displayed
     */
    fun userSelected(view : View)
    {
        val vwParentRow = view as LinearLayout
        val firstname = (vwParentRow.getChildAt(0) as TextView).text.toString()

        var clickedUser = NearbyUsers.userList.getUser(firstname)

        val endpointId = clickedUser?.endpointId

        if (clickedUser?.status == Status.CONNECTED)
        {
            sendMessage(Message(MessageType.REQUEST_PERMISSION, null), endpointId.orEmpty())
            NearbyUsers.userList.modifyStatus(clickedUser, Status.ASKED)
        }
        else if (clickedUser?.status == Status.ACCTEPTED)
        {
            val lastname = NearbyUsers.contacts.find { u -> u.firstName == firstname }?.lastName

            displayContact(firstname, lastname)
        }
    }

    /**
     * When a contact has been selected in the contact list, display it
     */
    fun contactSelected(view: View)
    {
        val vwParentRow = view as LinearLayout
        val firstName = (vwParentRow.getChildAt(1) as TextView).text.toString()
//        val lastName = (vwParentRow.getChildAt(2) as TextView).text.toString()

        val lastName = NearbyUsers.contacts.find { u -> u.firstName == firstName }?.lastName

        displayContact(firstName, lastName)

    }

    /**
     * Display a contact. Start a new fragment with the first and last name of the user to retrieve it
     */
    private fun displayContact(firstname: String?, lastname: String?)
    {
        var fragment : Fragment? = SelectedUserFragment()
        val args = Bundle()
        args.putString(getString(R.string.firstname_key), firstname)
        args.putString(getString(R.string.lastname_key), lastname)
        fragment!!.arguments = args

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit()
    }

    /**
     * When the permission to access the user information has been accepted, send a message containing
     * the user informations and remove the notification
     */
    fun requestAccept(view : View)
    {
        val vwParentRow = view.parent as LinearLayout
        val endpointId = (vwParentRow.getChildAt(1) as TextView).text.toString()

        sendMessage(Message(MessageType.USER_INFORMATIONS, myUserInformations), endpointId)

        removeNotification(endpointId)
    }

    /**
     * When the request has been refused, send a message and remove the notification
     */
    fun requestRefuse(view : View)
    {
        val vwParentRow = view.parent as LinearLayout
        val endpointId = (vwParentRow.getChildAt(1) as TextView).text.toString()

        sendMessage(Message(MessageType.PERMISSION_REFUSED, myUserInformations), endpointId)

        removeNotification(endpointId)
    }

    /**
     * Remove the notification from the list
     */
    private fun removeNotification(endpointId: String)
    {
        NearbyUsers.notificationList.removeItem(endpointId)
    }

    /**
     * Helper method used to convert a message to a payload and send it
     */
    fun sendMessage(message: Message, endpointId: String)
    {
        val payload = Payload.fromBytes(SerializationUtils.serialize(message))
        send(payload, endpointId)
    }

    /**
     * Called when a message has been received.
     * Convert the payload into a message and act accordingly.
     * Also notify the listeners that have been subscribed to this event
     */
    override fun onReceive(endpoint: Endpoint?, payload: Payload?) {
        val bytes = payload?.asBytes() ?:return
        val message = SerializationUtils.deserialize<Message>(bytes)

        if (message.type == MessageType.USER_INFORMATIONS)
        {
            var userInformations = message.content as UserInformations
            addContact(userInformations)
            NearbyUsers.userList.modifyStatus(NearbyUsers.userList.getUser(userInformations.firstName), Status.ACCTEPTED)
        }
        else if (message.type == MessageType.PERMISSION_REFUSED)
        {
            NearbyUsers.userList.modifyStatus(NearbyUsers.userList.getUserWithEndpointId(endpoint!!.id), Status.CONNECTED)
        }

        for (listener in listeners)
            listener.messageReceived(endpoint, message)
    }

    /**
     * Add a contact to the list
     */
    private fun addContact(userInformations: UserInformations)
    {
        if (!NearbyUsers.contacts.any{ u -> u.firstName == userInformations.firstName && u.lastName == userInformations.lastName})
        {
            NearbyUsers.contacts.add(userInformations)
        }
    }

}
