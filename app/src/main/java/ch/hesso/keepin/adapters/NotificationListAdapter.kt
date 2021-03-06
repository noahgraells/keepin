package ch.hesso.keepin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.hesso.keepin.R
import ch.hesso.keepin.utils.NearbyUsers
import ch.hesso.keepin.pojos.PublicUser
import kotlinx.android.synthetic.main.item_notification_user.view.*
import kotlinx.android.synthetic.main.item_public_user.view.txv_firstname

/**
 * Adapter used to map items to notification inside the notification list
 */
class NotificationListAdapter(val items : ArrayList<PublicUser>) : RecyclerView.Adapter<NotificationViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {

        return NotificationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_notification_user, parent, false))
    }

    /**
     * Get the number of item inside the list
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * Add a user to the list and update the notification  badge
     */
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

    /**
     * Remove an item from the list and adapt the notification badge accordingly
     */
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