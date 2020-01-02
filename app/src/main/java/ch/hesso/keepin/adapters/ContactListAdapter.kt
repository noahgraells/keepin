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

class ContactListAdapter(val items : ArrayList<PublicUser>) : RecyclerView.Adapter<ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {

        return ContactViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_public_user, parent, false))

    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getUser(username: String) : PublicUser?
    {
        return items.find { user : PublicUser -> user.name == username }
    }

    fun addItem(user: PublicUser)
    {
        if (!items.any { u -> u.name == user.name && u.lastName == user.lastName })
        {
            items.add(user)
            notifyItemInserted(itemCount - 1)
        }
    }

    fun removeItem(name: String, lastname: String)
    {
        for (i in items.indices.reversed()) {
            if (items[i].name == name && items[i].lastName == lastname)
            {
                items.removeAt(i)
                notifyItemRemoved(i)
            }
        }
    }

    fun contains(endpointId: String) : Boolean
    {
        return items.any{x : PublicUser -> x.endpointId == endpointId}
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {

        val pu = items.get(position)

        holder.firstname?.text = pu.name
        holder.lastname?.text = pu.lastName


    }
}

class ContactViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val firstname = view.txv_firstname
    val actionButton = view.btn_action
    val lastname = view.txv_lastname
}