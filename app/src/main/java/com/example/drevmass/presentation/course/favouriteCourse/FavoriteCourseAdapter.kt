package com.example.drevmass.presentation.course.favouriteCourse

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.drevmass.data.model.courseModel.FavoriteCourseListResponseItem
import com.example.drevmass.databinding.FavoriteCourseAdapterBinding
import com.example.drevmass.presentation.course.interfaces.RcViewClickCourseCallback
import kz.mobydev.drevmass.presentation.course.interfaces.RcViewClickCourseLessonCallback
import kz.mobydev.drevmass.presentation.course.interfaces.RcViewClickFavoriteCourseCallback

class FavoriteCourseAdapter : RecyclerView.Adapter<FavoriteCourseAdapter.RcViewHolder>() {

    private val diffCallback =
        object : DiffUtil.ItemCallback<FavoriteCourseListResponseItem>() {
            override fun areItemsTheSame(
                oldItem: FavoriteCourseListResponseItem,
                newItem: FavoriteCourseListResponseItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: FavoriteCourseListResponseItem,
                newItem: FavoriteCourseListResponseItem
            ): Boolean {
                return oldItem == newItem
            }

        }

    private val differ = AsyncListDiffer(this, diffCallback)

    private var listenerClick: RcViewClickCourseLessonCallback? = null
    private var listenerFavorite: RcViewClickFavoriteCourseCallback? = null

    fun setOnPlayClickListener(listener: RcViewClickCourseLessonCallback) {
        this.listenerClick = listener
    }
    fun setOnFavoriteClickListener(listener: RcViewClickFavoriteCourseCallback) {
        this.listenerFavorite = listener
    }


    inner class RcViewHolder(private var binding: FavoriteCourseAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun itemBind(item: FavoriteCourseListResponseItem, position: Int) {
            binding.tvTitleFavoriteCourse.text = item.courseName
            val adapterFavoriteLesson = FavoriteLessonAdapter()
            binding.rvLessonsFavoriteDrevmass.adapter= adapterFavoriteLesson
            adapterFavoriteLesson.submitList(item.lessons)

            adapterFavoriteLesson.setOnFavoriteClickListener(object : RcViewClickFavoriteCourseCallback {
                override fun onItemFavoriteClick(lesson_id: Int) {
                    listenerFavorite?.onItemFavoriteClick(lesson_id)
                    Log.d("AAA", "Favorite Clicked in CourseAdapter +")
                }
                override fun onItemUnFavoriteClick(lesson_id: Int) {
                    listenerFavorite?.onItemUnFavoriteClick(lesson_id)
                    Log.d("AAA", "Favorite Clicked in CourseAdapter -")
                }
            })
            adapterFavoriteLesson.setOnPlayClickListener(object : RcViewClickCourseCallback {
                override fun onRecyclerViewDirectionClick(id: Int) {
                    listenerClick?.onRecyclerViewDirectionClick(item.courseId,id)
                }

            })
        }
    }


    fun submitList(list: List<FavoriteCourseListResponseItem>) {
        differ.submitList(list)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RcViewHolder {
        return RcViewHolder(
            FavoriteCourseAdapterBinding.inflate(
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