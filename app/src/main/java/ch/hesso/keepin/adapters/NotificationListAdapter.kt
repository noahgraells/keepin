package ch.hesso.keepin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.hesso.keepin.R
import ch.hesso.keepin.Utils.NearbyUsers
import ch.hesso.keepin.enums.Status
import ch.hesso.keepin.pojos.PublicUser
import kotlinx.android.synthetic.main.fragment_selected_user.view.*
import kotlinx.android.synthetic.main.item_notification_user.view.*
import kotlinx.android.synthetic.main.item_public_user.view.*
import kotlinx.android.synthetic.main.item_public_user.view.txv_firstname

class NotificationListAdapter(val items : ArrayList<PublicUser>) : RecyclerView.Adapter<NotificationViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {

        return NotificationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_notification_user, parent, false))
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
        if (!items.any { u -> u.endpointId == user.endpointId})
        {
            items.add(user)
            notifyItemInserted(itemCount - 1)
        }

        if (NearbyUsers.notifTab != null)
        {
            NearbyUsers.notifTab!!.showBadge().number = itemCount
        }
    }

    fun removeItem(endpointId : String)
    {
        for (i in items.indices.reversed()) {
            if (items[i].endpointId == endpointId)
            {
                items.removeAt(i)
                notifyItemRemoved(i)
            }
        }

        if (NearbyUsers.notifTab != null)
        {
            NearbyUsers.notifTab!!.showBadge().number = itemCount
            if (itemCount == 0)
            {
                NearbyUsers.notifTab!!.removeBadge()
            }
        }
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {

        val pu = items.get(position)

        holder.firstname?.text = pu.name
        holder.endpoint_id?.text = pu.endpointId
    }
}

class NotificationViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val firstname = view.txv_firstname
    val endpoint_id = view.txv_endpoint_id
    val refuseButton = view.btn_refuse
    val acceptButton = view.btn_accept
}