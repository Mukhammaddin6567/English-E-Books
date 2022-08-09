package uz.gita.bookapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.gita.bookapp.R
import uz.gita.bookapp.data.model.BookData
import uz.gita.bookapp.databinding.ItemBookBinding

class BookAdapter : ListAdapter<BookData, BookAdapter.Holder>(BookDiffutils) {

    private var onClickBookItemListener: ((bookId: Int) -> Unit)? = null

    inner class Holder(private val viewBinding: ItemBookBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        init {
            itemView.setOnClickListener {
                onClickBookItemListener?.invoke(
                    getItem(
                        absoluteAdapterPosition
                    ).id
                )
            }
        }

        fun bind(): BookData = with(viewBinding) {
            getItem(absoluteAdapterPosition).apply {
                Glide
                    .with(bookImage)
                    .load(image)
                    .into(bookImage)
                bookName.text = name
                bookAuthor.text = author
                if (pagesCount > 0) {
                    bookPages.visibility = VISIBLE
                    bookProgress.visibility = VISIBLE
                    bookProgress.isActivated = false
                    bookPages.text = itemView.resources.getString(
                        R.string.text_book_pages,
                        currentPage + 1,
                        pagesCount
                    )
                    bookProgress.max = pagesCount - 1
                    bookProgress.progress = currentPage
                } else {
                    bookPages.visibility = GONE
                    bookProgress.visibility = GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(
        ItemBookBinding.bind(
            LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        )
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind()
    }


    private object BookDiffutils : DiffUtil.ItemCallback<BookData>() {
        override fun areItemsTheSame(oldItem: BookData, newItem: BookData): Boolean =
            newItem.id == oldItem.id

        override fun areContentsTheSame(oldItem: BookData, newItem: BookData): Boolean =
            newItem == oldItem
    }

    fun setOnClickBookItemListener(block: (bookId: Int) -> Unit) {
        onClickBookItemListener = block
    }

}