package com.example.drevmass.presentation.course.courseInfo

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.drevmass.R
import com.example.drevmass.data.model.courseModel.LessonResponse
import com.example.drevmass.databinding.FragmentCourseInfoAdapterBinding
import com.example.drevmass.presentation.course.interfaces.RcViewClickCourseCallback
import com.example.drevmass.presentation.course.interfaces.RcViewClickFavoriteCourseCallback

class CourseInfoAdapter : RecyclerView.Adapter<CourseInfoAdapter.RcViewHolder>() {

    private val diffCallback =
        object : DiffUtil.ItemCallback<LessonResponse>() {
            override fun areItemsTheSame(
                oldItem: LessonResponse,
                newItem: LessonResponse
            ): Boolean {
                return oldItem.isFavorite == newItem.isFavorite
            }

            override fun areContentsTheSame(
                oldItem: LessonResponse,
                newItem: LessonResponse
            ): Boolean {
                return oldItem.id == newItem.id
            }

        }

    private val differ = AsyncListDiffer(this, diffCallback)

    private var listenerClick: RcViewClickCourseCallback? = null
    private var listenerFavorite: RcViewClickFavoriteCourseCallback? = null

    fun setOnPlayClickListener(listener: RcViewClickCourseCallback) {
        this.listenerClick = listener
    }
    fun setOnFavoriteClickListener(listener: RcViewClickFavoriteCourseCallback) {
        this.listenerFavorite = listener
    }

    inner class RcViewHolder(private var binding: FragmentCourseInfoAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun itemBind(item: LessonResponse, position: Int) {
            Log.d("AAA", "Lesson img: ${item.imageSrc}")
            if (!item.imageSrc.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load("http://drevmasstestapi.mobydev.kz/" + item.imageSrc)
                    .into(binding.imgCoursePhoto)
            } else {
                Glide.with(itemView.context)
                    .load("https://www.youtube.com/watch?v="+"${item.videoSrc}"+"/maxresdefault.jpg")
                    .into(binding.imgCoursePhoto)
            }
            binding.tvCourseFULLTiltle.text = item.title
            /**  Count SYSTEM FOR LESSON  */
            binding.tvCountLesson.text = (position+1).toString()
            binding.tvTimeLesson.text = (item.duration/60).toString()

            if(item.isFavorite){
                binding.btnFavoriteLessons.setImageResource(R.drawable.ic_favorite_selected)
            }else{
                binding.btnFavoriteLessons.setImageResource(R.drawable.ic_favorite_unselected)
            }
            if (item.completed) {
                binding.tvCountLesson.setTextColor(binding.root.context.getColor(R.color.succes_green))
                binding.tvCountLessonDescription.setTextColor(binding.root.context.getColor(R.color.succes_green))
                binding.imgCheckLesson.visibility = View.VISIBLE
            }

            binding.btnFavoriteLessons.setOnClickListener {
                if (item.isFavorite) {
                    binding.btnFavoriteLessons.setImageResource(R.drawable.ic_favorite_selected)
                    listenerFavorite?.onItemUnFavoriteClick(item.id)
                    Log.d("AAA", "Favorite Clicked in LessonAdapter -")
                } else {
                    binding.btnFavoriteLessons.setImageResource(R.drawable.ic_favorite_unselected)
                    listenerFavorite?.onItemFavoriteClick(item.id)
                    Log.d("AAA", "Favorite Clicked in LessonAdapter +")
                }
                Log.d("AAA", "Favorite Clicked in CourseAdapter")
            }
            binding.btnPlayLesson.setOnClickListener {
                listenerClick?.onRecyclerViewDirectionClick(item.id)
                Log.d("AAA", "Play Clicked in LessonAdapter")
            }
            binding.btnPlayLesson.setOnClickListener {
                listenerClick?.onRecyclerViewDirectionClick(item.id)
                Log.d("AAA", "Play Clicked in LessonAdapter")
            }
            binding.imgCoursePhoto.setOnClickListener {
                listenerClick?.onRecyclerViewDirectionClick(item.id)
                Log.d("AAA", "Play Clicked in LessonAdapter")
            }
            if (item.completed) {
                binding.imgCheckLesson.visibility = View.VISIBLE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.tvCountLesson.setTextColor(itemView.context.getColor(R.color.succes_green))
                }
            }
        }
        }

    fun submitList(list: List<LessonResponse>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RcViewHolder {
        return RcViewHolder(
            FragmentCourseInfoAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RcViewHolder, position: Int) {
        holder.itemBind(differ.currentList[position], position)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}
