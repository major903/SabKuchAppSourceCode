package vedam.subkuch.ui.shopping

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import vedam.subkuch.databinding.ProductListItemBinding
import vedam.subkuch.interfaces.OnListViewItemClickListener
import vedam.subkuch.network.models.shopping.Product
import vedam.subkuch.utils.ImageSetter
import vedam.subkuch.utils.ListItemClickAction
import vedam.subkuch.utils.UiUtil

class ProductAdapter constructor(private val listener: OnListViewItemClickListener?) : ListAdapter<Product, ProductAdapter.ViewHolder>(ProductDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                ProductListItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product, listener)
    }

    class ViewHolder(val binding: ProductListItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product?, listener: OnListViewItemClickListener?) {

            UiUtil.setImageView(ImageSetter.ImageBuilder(binding.root.context)
                    .setImageLink(product?.Image2)
                    .setDefaults()
                    .setTarget(binding.ivProduct)
                    .build())
            UiUtil.setTextView(binding.tvName, "${product?.ItemName ?: ""}")
//            UiUtil.setTextView(binding.tvDescription, product?.ItemDescriptionShort)
//            binding.tvDescription.hide()
            UiUtil.setTextView(binding.tvBrand, product?.BrandName)
            UiUtil.setTextView(binding.tvVendorName, product?.VendorName)
            var price = ""
            if (!product?.Price.isNullOrBlank())
                price = "Rs. ${product?.Price ?: ""}"
            UiUtil.setTextView(binding.tvPrice, price.trim())
            var kmsAway = ""
            if (!product?.Distance.isNullOrBlank())
                kmsAway= " ${product?.Distance ?: ""} Kms away"
            UiUtil.setTextView(binding.tvKmsAway, kmsAway.trim())

            binding.root.setOnClickListener {
                listener?.onItemClick(
                        product,
                        0,
                        it,
                        ListItemClickAction.SELECT
                )
            }
        }

    }

    private class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.ShoppingId == newItem.ShoppingId
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
} 