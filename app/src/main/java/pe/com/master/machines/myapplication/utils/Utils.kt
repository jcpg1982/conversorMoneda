package pe.com.master.machines.myapplication.utils

import androidx.fragment.app.FragmentManager

class Utils {

    companion object {

        fun recursivePopBackStack(fragmentManager: FragmentManager): Boolean {
            if (fragmentManager.fragments != null) {
                for (fragment in fragmentManager.fragments) {
                    if (fragment != null && fragment.isVisible) {
                        val popped = recursivePopBackStack(fragment.childFragmentManager)
                        if (popped)
                            return true
                    }
                }
            }
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
                return true;
            }
            return false
        }
    }
}