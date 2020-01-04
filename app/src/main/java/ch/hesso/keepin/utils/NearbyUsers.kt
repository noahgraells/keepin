package ch.hesso.keepin.utils

import ch.hesso.keepin.adapters.NotificationListAdapter
import ch.hesso.keepin.adapters.SearchListAdapter
import ch.hesso.keepin.pojos.PublicUser
import ch.hesso.keepin.pojos.UserInformations
import com.google.android.material.tabs.TabLayout

/**
 * Static class holding informations that are shared across the fragments / activity
 */
class NearbyUsers {

    companion object {
        var publicUsers = ArrayList<PublicUser>()
        var notificationUsers = ArrayList<PublicUser>()
        var userList = SearchListAdapter(publicUsers)
        var notificationList = NotificationListAdapter(notificationUsers)

        var contacts = ArrayList<UserInformations>()

        var notifTab: TabLayout.Tab? = null
    }
}