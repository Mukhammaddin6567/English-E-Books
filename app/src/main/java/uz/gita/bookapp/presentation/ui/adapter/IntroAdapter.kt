package uz.gita.bookapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.gita.bookapp.R
import uz.gita.bookapp.data.model.IntroData
import uz.gita.bookapp.databinding.ItemIntroBinding

class IntroAdapter : ListAdapter<IntroData, IntroAdapter.Holder>(IntroAdapterDiffutils) {

    private object IntroAdapterDiffutils : DiffUtil.ItemCallback<IntroData>() {
        override fun areItemsTheSame(oldItem: IntroData, newItem: IntroData): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: IntroData, newItem: IntroData): Boolean =
            oldItem == newItem
    }

    inner class Holder(private val viewBinding: ItemIntroBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(): IntroData = with(viewBinding) {
            getItem(absoluteAdapterPosition).apply {
                imageIntro.setImageResource(image)
                textIntroTitle.text = itemView.context.getString(title)
                textIntroDescription.text = itemView.context.getString(description)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(
        ItemIntroBinding.bind(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_intro, parent, false
            )
        )
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind()
    }

}