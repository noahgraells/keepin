package ch.hesso.keepin.pojos

import ch.hesso.keepin.enums.Status

/**
 * Data class holding informations to be displayed in the search fragment
 */
data class PublicUser(val endpointId : String,
                      val name: String,
                      var status: Status,
                      val lastName: String = "")