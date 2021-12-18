package vedam.subkuch.ui.shopping

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import vedam.subkuch.R
import vedam.subkuch.base.BaseActivity
import vedam.subkuch.ui.public_utility.AddPublicUtilityFragment

class ShoppingActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_directory)
        setToolbarBackButton()
        setTitle(R.string.shopping)

        addFragment(R.id.content_frame, ShoppingFragment.newInstance())
    }
}