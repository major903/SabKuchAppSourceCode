package vedam.subkuch.ui.ask

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import com.android.volley.Response
import com.google.gson.Gson
import vedam.subkuch.R
import vedam.subkuch.base.BaseActivity
import vedam.subkuch.databinding.ActivityAddQuestionBinding
import vedam.subkuch.helpers.Constants
import vedam.subkuch.network.DataFetcher
import vedam.subkuch.network.DataFetcher.addQuestion
import vedam.subkuch.network.DataFetcher.getAskCategories
import vedam.subkuch.network.models.AddEventResponse
import vedam.subkuch.network.models.wallet.WalletResponse
import vedam.subkuch.ui.ask.models.AskCategory
import vedam.subkuch.ui.ask.models.AskCategoryResponse
import vedam.subkuch.ui.shopping.show
import vedam.subkuch.utils.AppPrefs
import vedam.subkuch.utils.AppUtil
import vedam.subkuch.utils.UiUtil

class AddQuestionActivity : BaseActivity() {
    private var etQuestion: EditText? = null
    private var binding: ActivityAddQuestionBinding? = null
    private var categoryId: String? = null
    private var walletResponse: WalletResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_add_question)
        setToolbarBackButton()
        setTitle(R.string.add_question)
        getCategories()
        getWalletDetails()
        etQuestion = findViewById(R.id.et_question)
        binding?.btSubmit?.setOnClickListener { view: View? ->
            val errorMessage = validateErrorMessage()
            if (errorMessage == 0) {
                withdraw()
            } else UiUtil.showDialog(this, getString(errorMessage), true)
        }
    }

    override fun moneyWithdrawn() {
        submit()
    }

    private fun getWalletDetails() {
        UiUtil.showProgressDialog(this, R.string.please_wait)
        DataFetcher.getWalletDetails(
            this,
            onWalletSuccessListener,
            WalletResponse::class.java,
            onErrorListener
        )
    }

    private val onWalletSuccessListener = Response.Listener { response: WalletResponse? ->
        walletResponse = response
        loadUI()
    }

    private fun loadUI() {
        UiUtil.cancelProgressDialog()
        val balance =
            (walletResponse?.returnData?.wallet?.availableBalance?.split(".")?.get(1))?.trim()
                ?.toIntOrNull() ?: 0
        if (balance >= 10) {
            binding?.tvMessage?.show()
            binding?.tvMessage?.text = getString(R.string.yes_money_ask, balance)
            binding?.linearLayout?.show()
            binding?.btSubmit?.show()
        } else {
            binding?.tvMessage?.show()
            val ss = SpannableString(getString(R.string.no_money_ask, balance))
            val clickableSpan: ClickableSpan = object : ClickableSpan() {
                override fun onClick(textView: View) {
                    AppUtil.openUrl(this@AddQuestionActivity, "https://vedam-it.com/sabkuch.html")
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = true
                }
            }
            ss.setSpan(
                clickableSpan,
                ss.indexOf("Click"),
                ss.indexOf("Click") + 10,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding?.tvMessage?.movementMethod = LinkMovementMethod.getInstance()
            binding?.tvMessage?.text = ss
            binding?.tvMessage?.highlightColor = Color.TRANSPARENT
        }
    }


    private fun getCategories() {
//        UiUtil.showProgressDialog(this, R.string.please_wait)
        getAskCategories(
            this,
            onAskCategorySuccessListener,
            AskCategoryResponse::class.java,
            onErrorListener
        )
    }

    private val onAskCategorySuccessListener = Response.Listener { response: AskCategoryResponse? ->
        UiUtil.cancelProgressDialog()
        if (response != null && response.returnMessage.equals(
                Constants.SUCCESS,
                ignoreCase = true
            )
        ) {
            setAskCategories(response.returnData)
        } else UiUtil.showToast(this, getString(R.string.no_data))
    }

    private fun setAskCategories(askCategories: ArrayList<AskCategory>) {
        val askCategory = AskCategory()
        askCategory.categoryname = getString(R.string.select_a_category)
        askCategories.add(0, askCategory)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item, askCategories
        )
        binding!!.spCategory.adapter = adapter
        binding!!.spCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    categoryId = (parent.getItemAtPosition(position) as AskCategory).id
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        binding!!.spCategory.setSelection(0)
    }

    private fun submit() {
        UiUtil.showProgressDialog(this, getString(R.string.please_wait))
        val request: MutableMap<String, String?> = HashMap()
        val userId =
            AppPrefs.getInstance(this).sharedPreferences.getString(AppPrefs.PREFS_USER_ID, "")
        request[Constants.topic] = etQuestion!!.text.toString()
        request[Constants.userid] = userId
        request[Constants.categoryid] = categoryId
        addQuestion(
            this,
            Gson().toJson(request),
            onAddQuestionSuccessListener,
            AddEventResponse::class.java,
            onErrorListener
        )
    }

    private val onAddQuestionSuccessListener = Response.Listener { response: AddEventResponse? ->
        UiUtil.cancelProgressDialog()
        if (response != null && !TextUtils.isEmpty(response.returnMessage)) {
            UiUtil.showToast(this, response.returnMessage)
            setResult(RESULT_OK)
            finish()
        } else UiUtil.showToast(this, this.getString(R.string.err_occurred))
    }

    private fun validateErrorMessage(): Int {
        var errorMessage = 0
        if (TextUtils.isEmpty(categoryId)) errorMessage =
            R.string.select_a_category else if (TextUtils.isEmpty(
                etQuestion!!.text.toString().trim { it <= ' ' })
        ) errorMessage = R.string.enter_question
        return errorMessage
    }
}