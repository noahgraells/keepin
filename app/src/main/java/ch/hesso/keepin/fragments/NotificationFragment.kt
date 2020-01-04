package ch.hesso.keepin.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.hesso.keepin.MainActivity

import ch.hesso.keepin.R
import ch.hesso.keepin.utils.ConnectionsActivity
import ch.hesso.keepin.utils.MessageReceived
import ch.hesso.keepin.utils.NearbyUsers
import ch.hesso.keepin.enums.MessageType
import ch.hesso.keepin.enums.Status
import ch.hesso.keepin.pojos.Message
import ch.hesso.keepin.pojos.PublicUser

/**
 * Fragment used to display user requests
 */
class NotificationFragment : Fragment(),  MessageReceived {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_notification, container, false)

        (activity as MainActivity).addListener(this)
        var notificationList = view.findViewById(R.id.notification_list) as RecyclerView
        notificationList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))


        var layoutManager = LinearLayoutManager(activity)
        notificationList!!.layoutManager = layoutManager
        notificationList.adapter = NearbyUsers.notificationList

        NearbyUsers.notificationList.addItem(PublicUser("", "Noah Graells", Status.ASKING))
        NearbyUsers.notificationList.addItem(PublicUser("", "Farid Abdalla", Status.ASKING))

        return view
    }

    /**
     * When a message has been received, if it is a request permission, add a notification
     */
    override fun messageReceived(endpoint: ConnectionsActivity.Endpoint?, message: Message) {
        when (message.type)
        {
            MessageType.REQUEST_PERMISSION -> {
                NearbyUsers.notificationList.addItem(PublicUser(endpoint!!.id, endpoint!!.name, Status.ASKED))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.v("Debug", "Notif - onStart");
    }

    override fun onPause() {
        super.onPause()
        Log.v("Debug", "Notif - onPause");
    }

    override fun onResume() {
        super.onResume()
        Log.v("Debug", "Notif - onResume");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v("Debug", "Notif - onDestroy");
    }
}
