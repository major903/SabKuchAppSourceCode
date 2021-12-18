package vedam.subkuch.ui.shopping

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import vedam.subkuch.databinding.FragmentDirectoryDetailsListItemBinding
import vedam.subkuch.databinding.ShoppingSubcategoryListItemBinding
import vedam.subkuch.interfaces.OnListViewItemClickListener
import vedam.subkuch.network.models.shopping.ShoppingSubCategory
import vedam.subkuch.ui.directory.DirectoryDetailsAdapter
import vedam.subkuch.ui.directory.models.Business
import vedam.subkuch.utils.ImageSetter
import vedam.subkuch.utils.ListItemClickAction
import vedam.subkuch.utils.UiUtil

class SubCatAdapter constructor(private val listener: OnListViewItemClickListener?) : ListAdapter<ShoppingSubCategory, SubCatAdapter.ViewHolder>(SubCatDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                ShoppingSubcategoryListItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subCat = getItem(position)
        holder.bind(subCat, listener)
    }

    class ViewHolder(val binding: ShoppingSubcategoryListItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(subCat: ShoppingSubCategory?, listener: OnListViewItemClickListener?) {

            UiUtil.setImageView(ImageSetter.ImageBuilder(binding.root.context)
                    .setImageLink(subCat?.Image)
                    .setDefaults()
                    .setTarget(binding.iv)
                    .build())
            binding.tvName.text = subCat?.Name
            binding.root.setOnClickListener {
                listener?.onItemClick(
                        subCat,
                        0,
                        it,
                        ListItemClickAction.SELECT
                )
            }
        }

    }

    private class SubCatDiffCallback : DiffUtil.ItemCallback<ShoppingSubCategory>() {

        override fun areItemsTheSame(oldItem: ShoppingSubCategory, newItem: ShoppingSubCategory): Boolean {
            return oldItem.ShoppingSubcatid == newItem.ShoppingSubcatid
        }

        override fun areContentsTheSame(oldItem: ShoppingSubCategory, newItem: ShoppingSubCategory): Boolean {
            return oldItem == newItem
        }
    }
}