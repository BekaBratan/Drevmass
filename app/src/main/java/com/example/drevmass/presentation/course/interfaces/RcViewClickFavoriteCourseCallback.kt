package kz.mobydev.drevmass.presentation.course.interfaces

interface RcViewClickFavoriteCourseCallback {
    fun onItemFavoriteClick(lesson_id: Int)
    fun onItemUnFavoriteClick(lesson_id: Int)
}