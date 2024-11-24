package com.example.drevmass.presentation.course.courseInfo

import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.drevmass.R
import com.example.drevmass.data.model.notifiation.NotificationRequest
import com.example.drevmass.data.util.SharedProvider
import com.example.drevmass.databinding.FragmentCourseInfoBinding
import com.example.drevmass.presentation.course.interfaces.RcViewClickCourseCallback
import com.example.drevmass.presentation.utils.provideNavigationHost
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import kz.mobydev.drevmass.presentation.course.interfaces.RcViewClickFavoriteCourseCallback
import java.util.Calendar

class CourseInfoFragment : Fragment() {

    private lateinit var binding: FragmentCourseInfoBinding

    private var selectedNotificationDays = mutableListOf<String>()

    private val args: CourseInfoFragmentArgs by navArgs()

    private val viewModel: CourseInfoViewModel by viewModels()

    private val courseInfoAdapter = CourseInfoAdapter()

    private var isFavorite = false

    private var userId = ""

    private fun getSharedPreferencesForUser(userId: String): SharedPreferences {
        return requireContext().getSharedPreferences("dialog_state_$userId", Context.MODE_PRIVATE)
    }

    private val isDialogShownKey = "is_dialog_shown"

    private fun isDialogShownForUser(userId: String): Boolean {
        val sharedPreferences = getSharedPreferencesForUser(userId)
        return sharedPreferences.getBoolean(isDialogShownKey, false)
    }

    private fun setIsDialogShownForUser(userId: String, value: Boolean) {
        val sharedPreferences = getSharedPreferencesForUser(userId)
        sharedPreferences.edit().putBoolean(isDialogShownKey, value).apply()
    }

    lateinit var alarmManager: AlarmManager

    lateinit var pendingIntent: PendingIntent
    private lateinit var calSet: Calendar
    private var selectedNotificationTime = ""
    private var notificationRequest: NotificationRequest? = null
    private var priceBonus = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCourseInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun setNotification(week: Int, hour: Int, minute: Int) {
        calSet = Calendar.getInstance()
        calSet.set(Calendar.DAY_OF_WEEK, week)
        calSet.set(Calendar.HOUR_OF_DAY, hour)
        calSet.set(Calendar.MINUTE, minute)
        calSet.set(Calendar.SECOND, 0)
        calSet.set(Calendar.MILLISECOND, 0)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calSet.timeInMillis,
            AlarmManager.INTERVAL_DAY * 7,
            pendingIntent
        )
    }

    fun calculateResponseDayWeekForView(
        mon: Boolean,
        tue: Boolean,
        wed: Boolean,
        thu: Boolean,
        fri: Boolean,
        sat: Boolean,
        sun: Boolean
    ): List<String> {
        var selectedDays = mutableListOf<String>()
        if (mon) {
            selectedDays.add("Пн")
        }
        if (tue) {
            selectedDays.add("Вт")
        }
        if (wed) {
            selectedDays.add("Ср")
        }
        if (thu) {
            selectedDays.add("Чт")
        }
        if (fri) {
            selectedDays.add("Пт")
        }
        if (sat) {
            selectedDays.add("Сб")
        }
        if (sun) {
            selectedDays.add("Вс")
        }
        return selectedDays
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        screenMode()
        scrollSystemCollapsingToolbar()
        val shared = SharedProvider(requireContext())
        viewModel.getUserCourseId(shared.getToken(), args.courseId)
        viewModel.getNotificationDaysInCourse(shared.getToken(), args.courseId)
        viewModel.responseMessageCourseFragment.observe(viewLifecycleOwner) {
            viewModel.getUserCourseId(shared.getToken(), args.courseId)
        }
        viewModel.responseSuccessUpdateNotificationDaysInCourse.observe(viewLifecycleOwner) {
            viewModel.getNotificationDaysInCourse(shared.getToken(), args.courseId)
        }

        viewModel.getUser(shared.getToken())
        viewModel.responseGetUser.observe(viewLifecycleOwner) {
            userId = it.id.toString()
        }

        viewModel.responseNotificationDaysInCourse.observe(viewLifecycleOwner) { it ->
            binding.btnSwitchCalendarSystem.isChecked = it.notificationIsSelected
            binding.tvDayCourse.text = calculateResponseDayWeekForView(
                it.mon,
                it.tue,
                it.wed,
                it.thu,
                it.fri,
                it.sat,
                it.sun
            ).joinToString(", ") { it }
            val timeString = it.time
            var hour = 0
            var minute = 0
            if (timeString.isEmpty() || timeString.isBlank()|| timeString == "null" || timeString == "") {
                hour=18
                minute=0
                binding.tvTimeCourse.text = "18:00"
            }else{
                val parts = timeString.split(":")
                hour = parts[0].toInt()
                minute = parts[1].toInt()
                binding.tvTimeCourse.text = it.time
            }
            if (it.mon) {
                setNotification(Calendar.MONDAY, hour, minute)
            }
            if (it.tue) {
                setNotification(Calendar.TUESDAY, hour, minute)
            }
            if (it.wed) {
                setNotification(Calendar.WEDNESDAY, hour, minute)
            }
            if (it.thu) {
                setNotification(Calendar.THURSDAY, hour, minute)
            }
            if (it.fri) {
                setNotification(Calendar.FRIDAY, hour, minute)
            }
            if (it.sat) {
                setNotification(Calendar.SATURDAY, hour, minute)
            }
            if (it.sun) {
                setNotification(Calendar.SUNDAY, hour, minute)
            }
        }
        viewModel.responseDeleteNotificationByDay.observe(viewLifecycleOwner) {
            alarmManager.cancel(pendingIntent)
            binding.btnSwitchCalendarSystem.isChecked = false
        }

        binding.btnShareCourseCollapsing.setOnClickListener {
            shareCourse()
        }
        binding.btnShareCourseFragment.setOnClickListener {
            shareCourse()
        }
        binding.btnBackCourseCollapsing.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.responseCourseById.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.run {
                    binding.tvTitleCourseCollapsing.text = it.course!!.name
                    if (!it.course!!.imageSrc.isNullOrEmpty()) {
                        Glide.with(requireContext())
                            .load("http://drevmasstestapi.mobydev.kz/" + it.course.imageSrc)
                            .into(binding.imgCourseBanner)
                    } else {
                        binding.imgCourseBanner.setImageResource(R.drawable.ill_epmty_busket_112)
                    }

                    tvTitleCourse.text = it.course.name
                    tvDescriptionCourse.text = it.course.description

                    binding.tvDescriptionCourse.post {
                        // Получаем количество строк текста
                        val lineCount = binding.tvDescriptionCourse.lineCount

                        // Выводим информацию в лог
                        Log.d("TextView", "Количество строк текста: $lineCount")

                        // Сравниваем количество строк с максимальным допустимым количеством строк (в вашем случае 3)
                        if (lineCount <= 3) {
                            // Если текст умещается в 3 строки или меньше, скрываем кнопку "подробнее"
                            binding.btnShowAllTextDescription.visibility = View.GONE
                            Log.d("TextView", "Текст умещается в 3 строки или меньше. Кнопка 'подробнее' скрыта.")
                        } else {
                            // Если текст занимает больше 3 строк, показываем кнопку "подробнее"
                            binding.btnShowAllTextDescription.visibility = View.VISIBLE
                            Log.d("TextView", "Текст занимает больше 3 строк. Кнопка 'подробнее' показана.")

                            // Обработчик нажатия на кнопку "подробнее"
                            binding.btnShowAllTextDescription.setOnClickListener {
                                // Убираем ограничение на количество строк, позволяя тексту развернуться полностью
                                binding.tvDescriptionCourse.maxLines = Integer.MAX_VALUE
                                Log.d("TextView", "Текст развернут полностью.")
                                // Скрываем кнопку "подробнее", так как текст уже полностью развернут
                                binding.btnShowAllTextDescription.visibility = View.GONE
                                Log.d("Button", "Кнопка 'подробнее' скрыта.")
                            }
                        }
                    }

                    tvCountLesson.text = it.course.lessonCnt.toString()
                    tvTimeLesson.text = (it.course.duration/60).toString()
                    Log.d("AAA", "Course bONUS: ${it.bonusInfo}")
                    val bonusBody = it.course.bonusInfo
                    if (bonusBody == null) {
                        tvBonusSectionCourse.visibility = View.GONE
                    } else {
                        tvBonusSectionCourse.visibility = View.VISIBLE
                        tvCountDescriptionBonus.text = bonusBody.description
                        tvCountBonusForCourse.text = bonusBody.price.toString()
                    }

//                 TODO: добавить проверку на начатый курс
                    Log.d("AAA", "Course is Started: ${it.course.isStarted}")
                    if (it.course.isStarted) {
                        btnStartCourse.visibility = View.GONE
                        sectionProgressCourse.visibility = View.VISIBLE
                    } else {
                        btnStartCourse.visibility = View.VISIBLE
                        sectionProgressCourse.visibility = View.GONE
                    }

                    binding.tvProgressStatus.text = "${it.completedLessons} из ${it.allLessons}"

                    if (it.completedLessons == it.allLessons && !isDialogShownForUser(userId)) {
                        priceBonus = bonusBody?.price!!
                        showCustomDialogBox()
                        setIsDialogShownForUser(userId, true) // Установите флаг, чтобы помнить, что диалог был показан для этого пользователя
                    }

                    val progress =
                        (it.completedLessons.toFloat() / it.allLessons.toFloat() * 100).toInt()
                    binding.progressBarCourse.progress = progress

                    rvLessonsDrevmass.adapter = courseInfoAdapter
                    if (!it.course.lessons.isNullOrEmpty()) {
                        courseInfoAdapter.submitList(it.course.lessons)
                    }
                }
            }

        }
        courseInfoAdapter.setOnPlayClickListener(object : RcViewClickCourseCallback {
            override fun onRecyclerViewDirectionClick(id: Int) {
                val action =
                    CourseInfoFragmentDirections.actionCourseInfoFragmentToLessonFragment(
                        args.courseId,
                        id
                    )
                findNavController().navigate(action)
            }
        })
        courseInfoAdapter.setOnFavoriteClickListener(object : RcViewClickFavoriteCourseCallback {
            override fun onItemFavoriteClick(lesson_id: Int) {
                Log.d("AAA", "Favorite Clicked in CourseAdapter")
                viewModel.addFavoriteLesson(shared.getToken(), args.courseId, lesson_id)
            }

            override fun onItemUnFavoriteClick(lesson_id: Int) {
                viewModel.removeFavoriteLesson(shared.getToken(), args.courseId, lesson_id)
            }

        })
        viewModel.responseCourseLessons.observe(viewLifecycleOwner) {
            courseInfoAdapter.submitList(it)
            courseInfoAdapter.notifyDataSetChanged()
        }
        viewModel.errorResponse.observe(viewLifecycleOwner) {
            Log.d("AAA", "Error: $it")
        }

        viewModel.responseMessageCourseFragment.observe(viewLifecycleOwner) {
            viewModel.getUserCourseId(shared.getToken(), args.courseId)
        }
        /** Кнопки которых можно нажимать **/
        binding.btnStartCourse.setOnClickListener {
            Log.d("AAA", "Course is Started CLICKED ${args.courseId}")
            viewModel.startCourse(shared.getToken(), args.courseId)
        }

        binding.btnShowAllTextDescription.setOnClickListener {
            binding.tvDescriptionCourse.maxLines = Int.MAX_VALUE
            binding.btnShowAllTextDescription.visibility = View.GONE
        }
        binding.btnSwitchCalendarSystem.setOnCheckedChangeListener { _, On ->
            if (On) {
                binding.sectionPlannerCalendar.visibility = View.VISIBLE
                binding.btnSwitchCalendarSystem.backColor = ColorStateList.valueOf(resources.getColor(R.color.woody))
            } else {
                binding.sectionPlannerCalendar.visibility = View.GONE
                binding.btnSwitchCalendarSystem.backColor = ColorStateList.valueOf(resources.getColor(R.color.switchOff))
            }
        }
        binding.btnSelectDayCourse.setOnClickListener {
            val selectDayBottomSheet = SelectDayBottomSheet() { selectedDays ->
                Log.d("AAA", "selected Days : $selectedDays")
                binding.tvDayCourse.text = selectedDays.joinToString(", ") { it }
                selectedNotificationDays = selectedDays.toMutableList()
            }
            selectDayBottomSheet.show(parentFragmentManager, "")
        }
        binding.btnBackCourseFragment.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSelectTimeCourse.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val timePicker = TimePickerDialog(
                requireContext(),
                R.style.CustomTimePickerDialogTheme,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                    binding.tvTimeCourse.text = selectedTime
                    binding.tvDayCourse.text = selectedNotificationDays.joinToString(", ") { it }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
            timePicker.show()
        }
    }

    fun showCustomDialogBox() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.fragment_course_end_bottom_sheet_dialog)
        val textView: TextView = dialog.findViewById(R.id.textBonus) // Замените textViewId на реальный идентификатор вашего TextView

        // Пример установки текста в TextView
        textView.text = "Вы успешно прошли курс и получаете $priceBonus бонусов"
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val btnDismiss : AppCompatButton = dialog.findViewById(R.id.btnDismiss)

        btnDismiss.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun scrollSystemCollapsingToolbar() {
        val collapsingToolbarLayout: CollapsingToolbarLayout = binding.collapsingToolbar
        val appBarLayout: AppBarLayout = binding.appBar

        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow: Boolean = false
            var scrollRange: Int = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }

                if (scrollRange + verticalOffset == 0) {
                    binding.toolbarDescriptionSection.visibility = View.VISIBLE
                    collapsingToolbarLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                    isShow = true
                } else if (isShow) {
                    binding.toolbarDescriptionSection.visibility = View.GONE
                    isShow = false
                }
            }
        })
    }

    private fun shareCourse() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Попробуй этот курс! ${binding.tvTitleCourse.text}, ${binding.tvDescriptionCourse.text}"
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun screenMode() {
        provideNavigationHost()?.hideBottomNavigationBar(false)
        provideNavigationHost()?.fullScreenMode(true)
    }

    override fun onPause() {
        super.onPause()
        screenMode()
    }

}