package ch.hesso.keepin.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import ch.hesso.keepin.R
import ch.hesso.keepin.utils.NearbyUsers
import ch.hesso.keepin.adapters.ContactListAdapter
import ch.hesso.keepin.enums.Status
import ch.hesso.keepin.pojos.PublicUser

/**
 * Fragment used to display the contacts
 */
class ContactsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)

        var contactList = view.findViewById(R.id.contact_list) as RecyclerView

        var contacts = ArrayList<PublicUser>()
        for (user in NearbyUsers.contacts)
        {
            contacts.add(PublicUser("", user.firstName, Status.ACCTEPTED, user.lastName))
        }

        var layoutManager = LinearLayoutManager(activity)
        contactList!!.layoutManager = layoutManager
        contactList.adapter = ContactListAdapter(contacts)

        return view
    }
}
