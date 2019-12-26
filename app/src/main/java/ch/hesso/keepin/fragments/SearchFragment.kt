package ch.hesso.keepin.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.hesso.keepin.adapters.SearchListAdapter
import ch.hesso.keepin.enums.Status
import ch.hesso.keepin.pojos.PublicUser
import ch.hesso.keepin.R
import ch.hesso.keepin.Utils.NearbyUsers


class SearchFragment : Fragment() {

    var layoutManager : LinearLayoutManager ?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        var searchList = view.findViewById(R.id.search_list) as RecyclerView

//        publicUsers.add(PublicUser("Farid", Status.CONNECTED))
//        publicUsers.add(PublicUser("Noah", Status.ASKED))
//        publicUsers.add(PublicUser("Kevin", Status.ACCTEPTED))
//        publicUsers.add(PublicUser("Lewis",Status.REFUSED ))


        layoutManager = LinearLayoutManager(activity)
        searchList!!.layoutManager = layoutManager
        searchList.adapter = NearbyUsers.userList



//        Log.i("Debug", publicUsers.size.toString())

        return view
    }

    override fun onStart() {
        super.onStart()
        Log.v("Debug", "Search - onStart");
    }

    override fun onPause() {
        super.onPause()
        Log.v("Debug", "Search - onPause");
    }

    override fun onResume() {
        super.onResume()
        Log.v("Debug", "Search - onResume");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v("Debug", "Search - onDestroy");
    }
}
