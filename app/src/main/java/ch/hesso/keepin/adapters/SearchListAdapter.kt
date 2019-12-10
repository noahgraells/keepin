package ch.hesso.keepin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.hesso.keepin.R
import ch.hesso.keepin.enums.Status
import ch.hesso.keepin.pojos.PublicUser
import kotlinx.android.synthetic.main.item_public_user.view.*

class SearchListAdapter(val items : ArrayList<PublicUser>) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_public_user, parent, false))

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val pu = items.get(position)

        holder.firstname?.text = pu.name

        when(pu.status){
            Status.CONNECTED -> holder.actionButton.setImageResource(R.drawable.ic_ask_24dp)
            Status.ASKED -> holder.actionButton.setImageResource(R.drawable.ic_wait_24dp)
            Status.ACCTEPTED -> holder.actionButton.setImageResource(R.drawable.ic_valid_24dp)
            Status.REFUSED -> holder.actionButton.setImageResource(R.drawable.ic_refuse_24dp)
        }


    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val firstname = view.txv_firstname
    val actionButton = view.btn_action
}