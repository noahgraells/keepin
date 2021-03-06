package ch.hesso.keepin.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import ch.hesso.keepin.R
import ch.hesso.keepin.utils.NearbyUsers
import ch.hesso.keepin.adapters.DiscoverPagerAdapter
import ch.hesso.keepin.pojos.PublicUser
import com.google.android.material.tabs.TabLayout


/**
 * Fragment used to hold the two fragments (search and notification)
 */
class DiscoverFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_discover, container, false)

        // Setting ViewPager for each Tabs
        val viewPager = view.findViewById<View>(R.id.viewpager) as ViewPager
        var tabs = view.findViewById<View>(R.id.result_tabs) as TabLayout

        setupViewPager(viewPager)
        tabs.setupWithViewPager(viewPager)

        NearbyUsers.notifTab = tabs.getTabAt(1)

        return view;
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = DiscoverPagerAdapter(childFragmentManager)
        adapter.addFragment(SearchFragment(), "SEARCH")
        adapter.addFragment(NotificationFragment(), "NOTIF.")
        viewPager.adapter = adapter
    }
}
