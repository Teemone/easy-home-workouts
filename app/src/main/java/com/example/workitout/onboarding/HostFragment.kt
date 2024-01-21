package com.example.workitout.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.workitout.R
import com.example.workitout.adapters.Viewpager2Adapter

class HostFragment : Fragment() {
    private lateinit var viewPagerAdapter: Viewpager2Adapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tvSkip: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_host, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPagerAdapter = Viewpager2Adapter(childFragmentManager, lifecycle)
        viewPagerAdapter.apply {
            addFragment(FirstScreenFragment())
            addFragment(SecondScreenFragment())
            addFragment(ThirdScreenFragment())
        }
        viewPager = view.findViewById(R.id.vpOnboarding)
        viewPager.adapter = viewPagerAdapter
        tvSkip = view.findViewById(R.id.tvSkip)

        tvSkip.setOnClickListener { setPage(2) }


    }

    fun setPage(pageNum: Int) = viewPager.apply { currentItem = pageNum }

}