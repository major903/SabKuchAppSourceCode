package vedam.subkuch.ui.wallet

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import vedam.subkuch.R
import vedam.subkuch.network.models.referral.MyReferral
import vedam.subkuch.utils.AppUtil

class ReferralsAdapter : ListAdapter<MyReferral, ReferralsAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_wallet_referral, parent, false) as TextView
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = AppUtil.getFullName(
            getItem(position).firstName,
            getItem(position).lastName
        )
    }

    class ViewHolder(val name: TextView) : RecyclerView.ViewHolder(name)

    private companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MyReferral>() {
            override fun areItemsTheSame(oldItem: MyReferral, newItem: MyReferral): Boolean {
                val oldCode = oldItem.refferalCode
                val newCode = newItem.refferalCode

                // Some referral responses do not include a referral code. Treating two
                // missing codes as the same item causes DiffUtil to drop or reuse rows as
                // the visible page grows. The same object is safe to retain between pages;
                // otherwise let DiffUtil insert a distinct row.
                return if (oldCode.isNullOrBlank() || newCode.isNullOrBlank()) {
                    oldItem === newItem
                } else {
                    oldCode == newCode
                }
            }

            override fun areContentsTheSame(oldItem: MyReferral, newItem: MyReferral): Boolean =
                oldItem.firstName == newItem.firstName &&
                    oldItem.lastName == newItem.lastName &&
                    oldItem.mobile == newItem.mobile &&
                    oldItem.profileImage == newItem.profileImage
        }
    }
}
