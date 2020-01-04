package ch.hesso.keepin.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ch.hesso.keepin.MainActivity
import ch.hesso.keepin.R
import ch.hesso.keepin.databinding.FragmentProfileBinding

import ch.hesso.keepin.databinding.FragmentSelectedUserBinding
import ch.hesso.keepin.pojos.UserInformations
import com.google.gson.Gson


/**
 * Fragment used to enter the personal informations
 */
class ProfileFragment : Fragment() {

    private var userInfo : UserInformations? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        userInfo = (activity as MainActivity).myUserInformations

        val binding = DataBindingUtil.bind<FragmentProfileBinding>(view)

        binding!!.user = userInfo

        return view
    }

    /**
     * Save the profile of the user inside the shared preferences
     */
    private fun saveProfileData()
    {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val prefsEditor = sharedPref.edit()
        val gson = Gson()
        val json = gson.toJson(userInfo)
        prefsEditor.putString(getString(R.string.saved_informations_key), json)
        prefsEditor.commit()
    }

    override fun onStart() {
        super.onStart()

        Log.v("Debug", "Profile - onStart");
    }

    override fun onPause() {
        super.onPause()

        saveProfileData()
        Log.v("Debug", "Profile - onPause");
    }

    override fun onResume() {
        super.onResume()
        Log.v("Debug", "Profile - onResume");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v("Debug", "Profile - onDestroy");
    }
}
