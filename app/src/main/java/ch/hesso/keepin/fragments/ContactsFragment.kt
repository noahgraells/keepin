package ch.hesso.keepin.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ch.hesso.keepin.R

class ContactsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)

        return view
    }

    override fun onStart() {
        super.onStart()
        Log.v("Debug", "Contacts - onStart");
    }

    override fun onPause() {
        super.onPause()
        Log.v("Debug", "Contacts - onPause");
    }

    override fun onResume() {
        super.onResume()
        Log.v("Debug", "Contacts - onResume");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v("Debug", "Contacts - onDestroy");
    }
}
