package ch.hesso.keepin.Utils

import ch.hesso.keepin.adapters.ContactListAdapter
import ch.hesso.keepin.adapters.NotificationListAdapter
import ch.hesso.keepin.adapters.SearchListAdapter
import ch.hesso.keepin.pojos.PublicUser
import ch.hesso.keepin.pojos.UserInformations
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout

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