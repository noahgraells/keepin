package ch.hesso.keepin.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import ch.hesso.keepin.MainActivity
import ch.hesso.keepin.R
import ch.hesso.keepin.utils.ConnectionsActivity
import ch.hesso.keepin.utils.MessageReceived
import ch.hesso.keepin.utils.NearbyUsers
import ch.hesso.keepin.databinding.FragmentSelectedUserBinding
import ch.hesso.keepin.enums.MessageType
import ch.hesso.keepin.pojos.Message
import ch.hesso.keepin.pojos.UserInformations
import android.provider.ContactsContract
import android.content.Intent




/**
 * A simple [Fragment] subclass.
 */
class SelectedUserFragment : Fragment(), MessageReceived {

    private var userInfo = UserInformations()
    private var btnRequestInformations : Button? = null
    var btnSaveContact : Button? = null

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_selected_user, container, false)
        (activity as MainActivity).addListener(this)

        val binding = DataBindingUtil.bind<FragmentSelectedUserBinding>(view)

        binding!!.user = userInfo

        val args = arguments
        val endpointId = args!!.getString(getString(R.string.endpoint_id_key), "")

        btnSaveContact = view.findViewById<Button>(R.id.btnSaveContact)
        btnSaveContact!!.setOnClickListener{ saveContact() }


        btnRequestInformations = view.findViewById<Button>(R.id.btnRequestInformation)

        if (!endpointId.isNullOrBlank())
        {
            btnRequestInformations!!.setOnClickListener{_ -> (activity as MainActivity).sendMessage(
                Message(MessageType.REQUEST_PERMISSION, null), endpointId)}
        }
        else
        {
            val firstname = args!!.getString(getString(R.string.firstname_key), "")
            val lastname = args!!.getString(getString(R.string.lastname_key), "")

            fillUser(firstname, lastname)
        }
        return view
    }

    /**
     * Enable the user to save the informations inside his phone as a contact
     */
    private fun saveContact()
    {
        val contactIntent = Intent(ContactsContract.Intents.Insert.ACTION)
        contactIntent.type = ContactsContract.RawContacts.CONTENT_TYPE

        contactIntent
            .putExtra(ContactsContract.Intents.Insert.NAME, userInfo.firstName + " " + userInfo.lastName)
            .putExtra(ContactsContract.Intents.Insert.EMAIL, userInfo.email)

        startActivityForResult(contactIntent, 1)
    }

    /**
     * Fill the informations about the specified user
     */
    private fun fillUser(firstname: String, lastname: String)
    {
        var user = NearbyUsers.contacts.find { u -> u.firstName == firstname && u.lastName == lastname } ?: return

        fillUserInformations(user)
    }

    /**
     * When we receive the user information, fill the fields accordingly
     */
    override fun messageReceived(endpoint: ConnectionsActivity.Endpoint?, message: Message) {
        when (message.type)
        {
            MessageType.USER_INFORMATIONS -> {
                fillUserInformations(message.content as UserInformations)
            }
        }
    }

    /**
     * Fill the fields with the user information in parameter
     */
    private fun fillUserInformations(userInformations: UserInformations)
    {
        userInfo.copyFrom(userInformations)
        btnRequestInformations!!.visibility = View.GONE
        btnSaveContact!!.visibility = View.VISIBLE
    }


}
