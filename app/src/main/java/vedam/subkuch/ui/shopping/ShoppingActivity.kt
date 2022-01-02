package vedam.subkuch.ui.shopping

import android.location.Location
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import vedam.subkuch.R
import vedam.subkuch.base.BaseActivity
import vedam.subkuch.network.DataFetcher.updateLocation
import vedam.subkuch.network.models.AddEventResponse

class ShoppingActivity : BaseActivity(), FragmentManager.OnBackStackChangedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_directory)
        setToolbarBackButton()
        setTitle(R.string.shopping)
        requestLocation(false)
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

    override fun onLocationChanged(location: Location?) {
        updateLocation(this, null, AddEventResponse::class.java, null, location!!.latitude.toString(), location.longitude.toString())
    }
}