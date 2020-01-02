package ch.hesso.keepin.pojos

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import ch.hesso.keepin.BR
import java.io.Serializable
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.full.memberProperties

data class UserInformations(private var _userName: String = "",
                            private var _firstName: String = "",
                            private var _lastName : String = "",
                            private var _email : String = "",
                            private var _canSendRequest : Boolean = true): BaseObservable(),  Serializable
{

    var userName: String
        @Bindable get() = _userName
        set(value) {
            _userName = value
            notifyPropertyChanged(BR.userName)
        }

    var firstName: String
        @Bindable get() = _firstName
        set(value) {
            _firstName = value
            notifyPropertyChanged(BR.firstName)
        }

    var lastName: String
        @Bindable get() = _lastName
        set(value) {
            _lastName = value
            notifyPropertyChanged(BR.lastName)
        }

    var email: String
        @Bindable get() = _email
        set(value) {
            _email = value
            notifyPropertyChanged(BR.email)
        }

    var canSendRequest: Boolean
        @Bindable get() = _canSendRequest
        set(value) {
            _canSendRequest = value
            notifyPropertyChanged(BR.canSendRequest)
        }

    fun copyFrom(userInformations: UserInformations)
    {
        this.userName = userInformations.userName
        this.firstName = userInformations.firstName
        this.lastName = userInformations.lastName
        this.email = userInformations.email
        this.canSendRequest = userInformations.canSendRequest
    }
}