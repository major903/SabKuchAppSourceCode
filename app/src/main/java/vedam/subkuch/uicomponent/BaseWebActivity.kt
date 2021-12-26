package vedam.subkuch.uicomponent

import android.os.Bundle
import vedam.subkuch.R
import vedam.subkuch.base.BaseActivity

class BaseWebActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_directory)
        setToolbarBackButton()
        addFragment(R.id.content_frame, BaseWebFragment.newInstance(intent.extras))
    }
}