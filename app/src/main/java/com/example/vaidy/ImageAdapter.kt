import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vaidy.R
import com.example.vaidy.databinding.ItemImageBinding

class ImageAdapter(private val imageList: List<String>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageList[position]
        holder.binding.imageView.showImage(imageUrl)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    inner class ImageViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }
}

// Create an extension function to load the image using Glide
fun ImageView.showImage(imageUrl: String) {
    Glide.with(this)
        .load(imageUrl)
        .error(R.drawable.reload_icon_vector_set_reset_600w_1941591169)
        .into(this)
}
