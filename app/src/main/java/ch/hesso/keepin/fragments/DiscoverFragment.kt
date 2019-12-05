package ch.hesso.keepin.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import ch.hesso.keepin.R
import ch.hesso.keepin.adapters.DiscoverPagerAdapter
import com.google.android.material.tabs.TabLayout

class DiscoverFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_discover, container, false)

        // Setting ViewPager for each Tabs
        val viewPager = view.findViewById<View>(R.id.viewpager) as ViewPager
        val tabs = view.findViewById<View>(R.id.result_tabs) as TabLayout

        setupViewPager(viewPager)
        tabs.setupWithViewPager(viewPager)

        return view;
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = DiscoverPagerAdapter(childFragmentManager)
        adapter.addFragment(SearchFragment(), "SEARCH")
        adapter.addFragment(NotificationFragment(), "NOTIF.")
        viewPager.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        Log.v("Debug", "Discover - onStart");
    }

    override fun onPause() {
        super.onPause()
        Log.v("Debug", "Discover - onPause");
    }

    override fun onResume() {
        super.onResume()
        Log.v("Debug", "Discover - onResume");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v("Debug", "Discover - onDestroy");
    }

}
