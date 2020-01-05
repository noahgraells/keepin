package ch.hesso.keepin.pojos

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import ch.hesso.keepin.BR
import java.io.Serializable

/**
 * Data class holding informations about the user
 */
data class UserInformations(private var _firstName: String = "",
                            private var _lastName : String = "",
                            private var _email : String = "",
                            private var _phone: String="",
                            private var _facebook: String="",
                            private var _twitter: String="",
                            private var _instagram: String="",
                            private var _linkedin: String=""): BaseObservable(),  Serializable
{
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

    var phone: String
        @Bindable get() = _phone
        set(value) {
            _phone = value
            notifyPropertyChanged(BR.phone)
        }

    var facebook: String
        @Bindable get() = _facebook
        set(value) {
            _facebook = value
            notifyPropertyChanged(BR.facebook)
        }

    var twitter: String
        @Bindable get() = _twitter
        set(value) {
            _twitter = value
            notifyPropertyChanged(BR.twitter)
        }

    var instagram: String
        @Bindable get() = _instagram
        set(value) {
            _instagram = value
            notifyPropertyChanged(BR.instagram)
        }

    var linkedin: String
        @Bindable get() = _linkedin
        set(value) {
            _linkedin = value
            notifyPropertyChanged(BR.linkedin)
        }

    fun copyFrom(userInformations: UserInformations)
    {
        this.firstName = userInformations.firstName
        this.lastName = userInformations.lastName
        this.email = userInformations.email
        this.phone = userInformations.phone
        this.facebook = userInformations.facebook
        this.twitter = userInformations.twitter
        this.instagram = userInformations.instagram
        this.linkedin = userInformations.linkedin

    }
}