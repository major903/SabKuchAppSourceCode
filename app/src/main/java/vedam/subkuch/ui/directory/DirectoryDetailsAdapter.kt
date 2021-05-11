package vedam.subkuch.ui.directory

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import vedam.subkuch.databinding.FragmentDirectoryDetailsListItemBinding
import vedam.subkuch.interfaces.OnListViewItemClickListener
import vedam.subkuch.ui.directory.models.Business
import vedam.subkuch.ui.directory.models.BusinessAddress
import vedam.subkuch.utils.*
import java.util.*

class DirectoryDetailsAdapter constructor(
    private val listener: OnListViewItemClickListener?
) :
    androidx.recyclerview.widget.ListAdapter<Business, DirectoryDetailsAdapter.ViewHolder>(
        DirectoryDiffCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentDirectoryDetailsListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val business = getItem(position)
        holder.bind(business, listener)
    }

    class ViewHolder(val binding: FragmentDirectoryDetailsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(directoryDetail: Business, listener: OnListViewItemClickListener?) {

            if (directoryDetail.Addresses?.isNotEmpty() == true) {
                val businessAddress: BusinessAddress = directoryDetail.Addresses[0]
                businessAddress.City = directoryDetail.City
                setText(binding.tvWebsite, "Website :", directoryDetail.Website ?: "")
                binding.tvName.text = directoryDetail.BusinessName
                UiUtil.setTextView(businessAddress.Distance, "KMs away", binding.tvDistance)
                setText(binding.tvDealingIn, "Dealing in :", businessAddress.DealingIn)
                val formattedAddress = AppUtil.getFormattedAddress(businessAddress)
                UiUtil.setTextView(binding.directionContainer.tvAddress, formattedAddress)
                setText(binding.tvPhone, "Ph :", businessAddress.PhoneNo)
                setText(binding.tvMobile, "Mobile :", businessAddress.Mobile1)
                setText(binding.tvEmail, "Email :", businessAddress.Email)
                setText(binding.tvContactPerson, "Contact Person :", businessAddress.ContactPerson)
                UiUtil.setTextView(binding.tvLine1, businessAddress.InfoLine1)
                UiUtil.setTextView(binding.tvLine2, businessAddress.InfoLine2)
                val noOfReviews: Int = directoryDetail.Reviews?.size ?: 0
                if (noOfReviews > 0) {
                    binding.llRatings.visibility = View.VISIBLE
                    UiUtil.setTextView(
                        binding.tvReviews, String.format(
                            Locale.US, "(%d %s)", noOfReviews,
                            AppUtil.getSingularOrPluralString("Review", noOfReviews)
                        )
                    )
                    if (!TextUtils.isEmpty(directoryDetail.AvegrageOfRating) && AppUtil.isNumeric(
                            directoryDetail.AvegrageOfRating
                        )
                    ) {
                        binding.rbRating.visibility = View.VISIBLE
                        binding.rbRating.rating =
                            java.lang.Float.valueOf(directoryDetail.AvegrageOfRating ?: "0")
                    } else binding.rbRating.visibility = View.GONE
                } else binding.llRatings.visibility = View.GONE


                binding.root.setOnClickListener { view: View? ->
                    listener?.onItemClick(
                        directoryDetail,
                        0,
                        view,
                        ListItemClickAction.SELECT
                    )
                }
                if (!TextUtils.isEmpty(businessAddress.latitude) && !TextUtils.isEmpty(
                        businessAddress.longitude
                    )
                ) {
                    binding.directionContainer.ibDirection.visibility = View.VISIBLE
                    binding.directionContainer.ibDirection.setOnClickListener {
                        val webURL =
                            "https://www.google.com/maps/dir/?api=1&" + "destination=" + businessAddress.latitude + "%2C" + businessAddress.longitude
                        AppUtil.openUrl(binding.root.context, webURL)
                    }
                } else binding.directionContainer.ibDirection.visibility = View.GONE
            }
        }

        private fun setText(tv: TextView, prefix: String?, text: String?) {
            if (TextUtils.isEmpty(text)) {
                tv.visibility = View.GONE
            } else {
                tv.visibility = View.VISIBLE
                tv.text = String.format("%s %s", prefix, text)
            }
        }
    }

    fun setChat(chats: List<Business>) {
        submitList(chats)
//        notifyDataSetChanged()
    }

    private class DirectoryDiffCallback : DiffUtil.ItemCallback<Business>() {

        override fun areItemsTheSame(oldItem: Business, newItem: Business): Boolean {
            return oldItem.BusinessID == newItem.BusinessID
        }

        override fun areContentsTheSame(oldItem: Business, newItem: Business): Boolean {
            return oldItem == newItem
        }
    }
}