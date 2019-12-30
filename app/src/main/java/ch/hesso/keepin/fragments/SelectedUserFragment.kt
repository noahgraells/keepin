package ch.hesso.keepin.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import ch.hesso.keepin.MainActivity
import ch.hesso.keepin.R


/**
 * A simple [Fragment] subclass.
 */
class SelectedUserFragment : Fragment() {

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_selected_user, container, false)

        val args = arguments
        val endpointId = args!!.getString(getString(R.string.endpoint_id_key), "")

        val btnRequestInformation = view.findViewById<Button>(R.id.btnRequestInformation)
        btnRequestInformation!!.setOnClickListener{v -> (activity as MainActivity).sendMessage(getString(R.string.message_ask_for_permission), endpointId)}


        return view
    }


}
