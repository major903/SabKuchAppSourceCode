package vedam.subkuch.ui.shopping

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import vedam.subkuch.R
import vedam.subkuch.base.BaseActivity

class ShoppingActivity : BaseActivity(), FragmentManager.OnBackStackChangedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_directory)
        setToolbarBackButton()
        setTitle(R.string.shopping)
        bindCallbacks()
        addFragment(R.id.content_frame, ShoppingFragment.newInstance())
    }

    private fun bindCallbacks() {
        supportFragmentManager.addOnBackStackChangedListener(this)
    }

    override fun onBackStackChanged() {
        val fragment = supportFragmentManager.findFragmentById(R.id.content_frame)
        if (fragment is ShoppingFragment) title = getString(R.string.shopping)
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.removeOnBackStackChangedListener(this)
    }
}