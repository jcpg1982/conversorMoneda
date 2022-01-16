package pe.com.master.machines.myapplication.ui.activities.baseActivity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import pe.com.master.machines.myapplication.R
import pe.com.master.machines.myapplication.di.dependencies.AppComponent
import pe.com.master.machines.myapplication.ui.Application
import pe.com.master.machines.myapplication.utils.Utils

abstract class BaseActivity : AppCompatActivity() {

    private val TAG = BaseActivity::class.java.simpleName

    abstract fun getFragManager(): FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun getAppComponent(): AppComponent {
        val app = (application as Application)
        return app.getAppComponent()
    }

    fun onAddFragmentToStack(
        fragmentOrigen: Fragment?,
        fragmentDestino: Fragment,
        title: CharSequence,
        subTitle: CharSequence?,
        addToBackStack: Boolean,
        isAnimated: Boolean,
        tag: String
    ) {
        val ft = getFragManager().beginTransaction()

        ft.setBreadCrumbTitle(title)
        ft.setBreadCrumbShortTitle(subTitle)

        if (isAnimated) {
            ft.setCustomAnimations(
                R.anim.transition_slide_right_in,
                R.anim.transition_slide_left_out,
                android.R.anim.slide_in_left,
                R.anim.transition_slide_right_out
            )
        }

        if (fragmentDestino.isAdded) {
            if (fragmentOrigen != null) {
                ft.hide(fragmentOrigen)
                    .show(fragmentDestino)
            } else {
                ft.show(fragmentDestino)
            }
        } else {
            if (fragmentOrigen != null) {
                ft.hide(fragmentOrigen)
                    .add(R.id.fragment_container, fragmentDestino, tag)
            } else {
                ft.add(R.id.fragment_container, fragmentDestino, tag)
            }
        }

        if (addToBackStack) ft.addToBackStack(null)

        try {
            ft.commit()
        } catch (e: Exception) {
            e.printStackTrace()
            ft.commitAllowingStateLoss()
        }
    }

    fun onRemoveFragmentToStack(withBackPressed: Boolean) {
        if (withBackPressed) {
            if (Utils.recursivePopBackStack(getFragManager())) return
            super.onBackPressed()
        } else {
            Utils.recursivePopBackStack(getFragManager())
        }
    }
}