package com.example.drevmass.presentation.course

import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.TextAppearanceSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.drevmass.R
import com.example.drevmass.databinding.FragmentCourseAdapterBinding
import com.example.drevmass.presentation.course.interfaces.RcViewClickCourseCallback
import com.example.drevmass.data.model.courseModel.CourseItemResponse
import java.text.NumberFormat
import java.util.Locale

class CourseAdapter : RecyclerView.Adapter<CourseAdapter.QuestionnaireViewHolder>() {

    private val diffCallback =
        object : DiffUtil.ItemCallback<CourseItemResponse>() {
            override fun areItemsTheSame(
                oldItem: CourseItemResponse,
                newItem: CourseItemResponse
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: CourseItemResponse,
                newItem: CourseItemResponse
            ): Boolean {
                return oldItem == newItem
            }

        }

    private val differ = AsyncListDiffer(this, diffCallback)

    private var listenerClick: RcViewClickCourseCallback? = null

    fun setOnPlayClickListener(listener: RcViewClickCourseCallback) {
        this.listenerClick = listener
    }

    inner class QuestionnaireViewHolder(private var binding: FragmentCourseAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun itemBind(item: CourseItemResponse, position: Int) {
            if (!item.imageSrc.isNullOrEmpty()) {
                Log.d("AAA", "itemBind: ${"http://drevmasstestapi.mobydev.kz/" + item.imageSrc}")
                Glide.with(itemView.context)
                    .load("http://drevmasstestapi.mobydev.kz/" + item.imageSrc)
                    .into(binding.imgCoursePhoto)
            } else {
                binding.imgCoursePhoto.setImageResource(R.drawable.ill_epmty_busket_112)
            }
            binding.tvTitleCourse.text = item.name
            textSpannable(item.lessonCnt.toString(),(item.duration/60).toString(), binding)
            if (item.bonusInfo != null) {
                val numberFormat = NumberFormat.getInstance(Locale.getDefault())
                numberFormat.minimumFractionDigits = 0
                val formattedPrice = numberFormat.format(item.bonusInfo.price)
                binding.tvBonusSection.visibility = ViewGroup.VISIBLE
                binding.tvCountBonusForCourse.text = "+"+formattedPrice.toString()
            } else {
                binding.tvBonusSection.visibility = ViewGroup.GONE
            }
            binding.root.setOnClickListener {
                listenerClick?.onRecyclerViewDirectionClick(item.id)
            }
        }
        }

    fun submitList(list: List<CourseItemResponse>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionnaireViewHolder {
        return QuestionnaireViewHolder(
            FragmentCourseAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    private fun textSpannable(
        lessonCount:String,
        durationCount:String, binding: FragmentCourseAdapterBinding
    ){
        val spannableBuilder = SpannableStringBuilder()
        val lessonCountText = SpannableString(lessonCount)
        lessonCountText.setSpan(
            TextAppearanceSpan(binding.root.context, R.style.TextCourseAdapterBold),
            0,
            lessonCountText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val lesson = SpannableString(" уроков")
        lesson.setSpan(
            TextAppearanceSpan(binding.root.context, R.style.TextCourseAdapterRegular),
            0,
            lesson.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val spanSeparator = SpannableString(" · ")
        spanSeparator.setSpan(
            TextAppearanceSpan(binding.root.context, R.style.TextCourseAdapterBold),
            0,
            spanSeparator.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val durationCountText = SpannableString(durationCount)
        durationCountText.setSpan(
            TextAppearanceSpan(binding.root.context, R.style.TextCourseAdapterBold),
            0,
            durationCountText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val duration = SpannableString(" минут")
        duration.setSpan(
            TextAppearanceSpan(binding.root.context, R.style.TextCourseAdapterRegular),
            0,
            duration.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableBuilder.append(lessonCountText)
        spannableBuilder.append(lesson)
        spannableBuilder.append(spanSeparator)
        spannableBuilder.append(durationCountText)
        spannableBuilder.append(duration)

        binding.tvInfoCourse.text = spannableBuilder
    }

    override fun onBindViewHolder(holder: QuestionnaireViewHolder, position: Int) {
        holder.itemBind(differ.currentList[position], position)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}
