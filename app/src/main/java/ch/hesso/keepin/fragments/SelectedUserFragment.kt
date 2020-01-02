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
import ch.hesso.keepin.Utils.ConnectionsActivity
import ch.hesso.keepin.Utils.MessageReceived
import ch.hesso.keepin.Utils.NearbyUsers
import ch.hesso.keepin.databinding.FragmentSelectedUserBinding
import ch.hesso.keepin.enums.MessageType
import ch.hesso.keepin.pojos.Message
import ch.hesso.keepin.pojos.UserInformations


/**
 * A simple [Fragment] subclass.
 */
class SelectedUserFragment : Fragment(), MessageReceived {

    var userInfo = UserInformations()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_selected_user, container, false)
        (activity as MainActivity).addListener(this)

        val binding = DataBindingUtil.bind<FragmentSelectedUserBinding>(view)

        binding!!.user = userInfo

        val args = arguments
        val endpointId = args!!.getString(getString(R.string.endpoint_id_key), "")


        if (!endpointId.isNullOrBlank())
        {
            val btnRequestInformation = view.findViewById<Button>(R.id.btnRequestInformation)
            btnRequestInformation!!.setOnClickListener{_ -> (activity as MainActivity).sendMessage(
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

    private fun fillUser(firstname: String, lastname: String)
    {
        var user = NearbyUsers.contacts.find { u -> u.firstName == firstname && u.lastName == lastname } ?: return

        user.canSendRequest = false
        fillUserInformations(user)
    }

    override fun messageReceived(endpoint: ConnectionsActivity.Endpoint?, message: Message) {
        when (message.type)
        {
            MessageType.USER_INFORMATIONS -> {
                fillUserInformations(message.content as UserInformations)
            }
        }
    }

    fun fillUserInformations(userInformations: UserInformations)
    {
        userInfo.copyFrom(userInformations)
    }


}
