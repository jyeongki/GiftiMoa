package com.example.giftimoa.bottom_nav_fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.giftimoa.Home_gift_add_activity
import com.example.giftimoa.Home_gift_add_info_activity
import com.example.giftimoa.R
import com.example.giftimoa.ViewModel.Gificon_ViewModel
import com.example.giftimoa.home_fragment_List.Search_gift_activity
import com.example.giftimoa.adpater_list.Banner_Adapter
import com.example.giftimoa.adpater_list.RecyclerViewHomeGiftAdapter
import com.example.giftimoa.dto.Home_gift
import com.google.android.material.floatingactionbutton.FloatingActionButton
import me.relex.circleindicator.CircleIndicator3
import java.util.Timer
import java.util.TimerTask

class Home_Fragment : Fragment() {

    private lateinit var mPager: ViewPager2
    private lateinit var pagerAdapter: FragmentStateAdapter
    private val numPage = 4
    private lateinit var mIndicator: CircleIndicator3

    private lateinit var giftViewModel: Gificon_ViewModel
    private lateinit var RecyclerViewHomeGiftAdapter: RecyclerViewHomeGiftAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var activityResult: ActivityResultLauncher<Intent>


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val homeGift = result.data!!.extras!!.get("gift") as Home_gift
                giftViewModel.addGift(homeGift)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) // 프래그먼트에서 옵션 메뉴 사용을 활성화
        giftViewModel = ViewModelProvider(requireActivity()).get(Gificon_ViewModel::class.java)
    }

    //홈 메뉴 및 배너 생성
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home_, container, false)

        val toolbar = rootView.findViewById<Toolbar>(R.id.my_toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "GIFTIMOA"

        //커피 아이콘 인텐트
        val Home_menu_list_coffee = rootView.findViewById<ImageView>(R.id.home_ic_coffee)
        Home_menu_list_coffee.setOnClickListener(getCommonClickListener(requireActivity(), 0))
        //치킨 아이콘 인텐트
        val Home_menu_list_chiken = rootView.findViewById<ImageView>(R.id.home_ic_chicken)
        Home_menu_list_chiken.setOnClickListener(getCommonClickListener(requireActivity(), 1))
        //피자 아이콘 인텐트
        val Home_menu_list_pizza = rootView.findViewById<ImageView>(R.id.home_ic_pizza)
        Home_menu_list_pizza.setOnClickListener(getCommonClickListener(requireActivity(), 2))
        //패스트푸드 아이콘 인텐트
        val Home_menu_list_fastfood = rootView.findViewById<ImageView>(R.id.home_ic_festfood)
        Home_menu_list_fastfood.setOnClickListener(getCommonClickListener(requireActivity(), 3))
        //마트,편의점 아이콘 인텐트
        val Home_menu_list_mart = rootView.findViewById<ImageView>(R.id.home_ic_mart)
        Home_menu_list_mart.setOnClickListener(getCommonClickListener(requireActivity(), 4))
        //베이커리 아이콘 인텐트
        val Home_menu_list_bakery = rootView.findViewById<ImageView>(R.id.home_ic_bread)
        Home_menu_list_bakery.setOnClickListener(getCommonClickListener(requireActivity(), 5))
        //아이스크림 아이콘 인텐트
        val Home_menu_list_icecream = rootView.findViewById<ImageView>(R.id.home_ic_icecream)
        Home_menu_list_icecream.setOnClickListener(getCommonClickListener(requireActivity(), 6))
        //스낵 아이콘 인텐트
        val Home_menu_list_snack = rootView.findViewById<ImageView>(R.id.home_ic_snack)
        Home_menu_list_snack.setOnClickListener(getCommonClickListener(requireActivity(), 7))
        //영화 아이콘 인텐트
        val Home_menu_list_movie = rootView.findViewById<ImageView>(R.id.home_ic_movie)
        Home_menu_list_movie.setOnClickListener(getCommonClickListener(requireActivity(), 8))
        //상품권 아이콘 인텐트
        val Home_menu_list_giftcard = rootView.findViewById<ImageView>(R.id.home_ic_giftcard)
        Home_menu_list_giftcard.setOnClickListener(getCommonClickListener(requireActivity(), 9))

        // 배너 ViewPager2
        mPager = rootView.findViewById(R.id.viewpager)
        // Adapter
        pagerAdapter = Banner_Adapter(requireActivity(), numPage)
        mPager.adapter = pagerAdapter
        // Indicator
        mIndicator = rootView.findViewById(R.id.indicator)
        mIndicator.setViewPager(mPager)
        mIndicator.createIndicators(numPage, 0)
        // ViewPager 설정
        mPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        mPager.setCurrentItem(1000, true) // 시작 지점 (두 번째 인수로 부드러운 스크롤을 사용하려면 true로 설정)
        mPager.offscreenPageLimit = 4 // 최대 이미지 수

        mPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (positionOffsetPixels == 0) {
                    mPager.setCurrentItem(position, false)
                }
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mIndicator.animatePageSelected(position % numPage)
            }
        })

// 자동 슬라이드 코드 추가
        var currentPage = 1000
        val handler = Handler(Looper.getMainLooper())
        val update = Runnable {
            if (currentPage == numPage) {
                currentPage = 0
            }
            mPager.setCurrentItem(currentPage++, true)
        }

        Timer().schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 500, 3000) // 500은 시작 전 대기 시간(0.5초 후 시작), 3000은 각 슬라이드 간의 시간 간격(3초마다 업데이트)

        return rootView
    }
    fun getCommonClickListener(activity: Activity, tabIndex: Int): View.OnClickListener {
        return View.OnClickListener {
            val intent = Intent(activity, Search_gift_activity::class.java)
            intent.putExtra("tabIndex", tabIndex)
            activity.startActivity(intent)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById<RecyclerView>(R.id.rv_Gift_Home)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireActivity(), 2)
        recyclerView.layoutManager = layoutManager

        // 어댑터 초기화 및 RecyclerView에 연결
        RecyclerViewHomeGiftAdapter = RecyclerViewHomeGiftAdapter(mutableListOf()) { gift ->
            // Start the new activity
            val intent = Intent(requireContext(), Home_gift_add_info_activity::class.java)
            intent.putExtra("gift", gift)
            startActivity(intent)
        }
        recyclerView.adapter = RecyclerViewHomeGiftAdapter

        // 플로팅 버튼 클릭 시 다음 화면의 액티비티로 이동
        view.findViewById<FloatingActionButton>(R.id.fab_btn).setOnClickListener {
            startCollectGiftAddActivity()
        }

        giftViewModel.homeGifts.observe(viewLifecycleOwner, { gifts ->
            // 어댑터에 데이터 업데이트
            RecyclerViewHomeGiftAdapter.setGiftList(gifts.toMutableList())
            RecyclerViewHomeGiftAdapter.notifyDataSetChanged()
            Log.d("로그", "기프티콘: $gifts")
        })
    }

    private fun startCollectGiftAddActivity() {
        val intent = Intent(requireContext(), Home_gift_add_activity::class.java)
        activityResult.launch(intent)
    }

}