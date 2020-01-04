package ch.hesso.keepin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.hesso.keepin.R
import ch.hesso.keepin.enums.Status
import ch.hesso.keepin.pojos.PublicUser
import kotlinx.android.synthetic.main.item_contact_user.view.*
import kotlinx.android.synthetic.main.item_public_user.view.*
import kotlinx.android.synthetic.main.item_public_user.view.btn_action

/**
 * Used to map elements from the contact list
 */
class ContactListAdapter(val items : ArrayList<PublicUser>) : RecyclerView.Adapter<ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {

        return ContactViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_contact_user, parent, false))

    }

    /**
     * Get the number of item inside the list
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * Return whether or not a user with the specified endpointId is inside the list
     */
    fun contains(endpointId: String) : Boolean
    {
        return items.any{x : PublicUser -> x.endpointId == endpointId}
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {

        val pu = items.get(position)

        holder.fullname?.text = pu.name + " " + pu.lastName
        //holder.lastname?.text = pu.lastName
    }
}

class ContactViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val fullname = view.txv_contact_fullname
    val actionButton = view.btn_action
    //val lastname = view.txv_contact_lastname
}