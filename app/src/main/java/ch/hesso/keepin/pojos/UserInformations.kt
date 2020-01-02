package ch.hesso.keepin.pojos

import java.io.Serializable

data class UserInformations (val firstName : String,
                             val lastName : String,
                             val email : String) : Serializable