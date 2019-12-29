package ch.hesso.keepin.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import ch.hesso.keepin.R

class ProfileFragment : Fragment() {

    var edtUsername : EditText? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        edtUsername = view.findViewById<View>(R.id.edtUsername) as EditText

        return view
    }

    fun loadProfileData()
    {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        edtUsername!!.setText(sharedPref.getString(getString(R.string.saved_username_key), ""))
    }

    fun saveProfileData()
    {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(getString(R.string.saved_username_key), edtUsername!!.text.toString())
            commit()
        }
    }

    override fun onStart() {
        super.onStart()
        loadProfileData()

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
