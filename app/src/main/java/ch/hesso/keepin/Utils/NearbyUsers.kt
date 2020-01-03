package ch.hesso.keepin.Utils

import ch.hesso.keepin.adapters.ContactListAdapter
import ch.hesso.keepin.adapters.NotificationListAdapter
import ch.hesso.keepin.adapters.SearchListAdapter
import ch.hesso.keepin.pojos.PublicUser
import ch.hesso.keepin.pojos.UserInformations

class NearbyUsers {

    companion object {
        var publicUsers = ArrayList<PublicUser>()
        var notificationUsers = ArrayList<PublicUser>()
        var userList = SearchListAdapter(publicUsers)
        var notificationList = NotificationListAdapter(notificationUsers)

        var contacts = ArrayList<UserInformations>()
    }
}