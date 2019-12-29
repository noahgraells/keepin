package ch.hesso.keepin.Utils

import ch.hesso.keepin.adapters.SearchListAdapter
import ch.hesso.keepin.pojos.PublicUser

class NearbyUsers {

    companion object {
        var publicUsers = ArrayList<PublicUser>()
        var userList = SearchListAdapter(publicUsers)
    }
}