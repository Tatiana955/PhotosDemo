package com.example.photosdemo.ui.fragments.login

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LoginViewPagerAdapter(
    fragment: LoginFragment
): FragmentStateAdapter(fragment) {

    private val listFrag = listOf(
        SignInFragment(),
        SignUpFragment()
    )

    override fun getItemCount(): Int {
        return listFrag.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                listFrag[position]
            }
            1 -> {
                listFrag[position]
            }
            else -> SignInFragment()
        }
    }
}