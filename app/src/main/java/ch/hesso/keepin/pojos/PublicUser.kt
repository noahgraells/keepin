package ch.hesso.keepin.pojos

import ch.hesso.keepin.enums.Status

data class PublicUser(val name: String,
                      val status: Status)