package com.example.giftimoa.bottom_nav_fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.giftimoa.Collect_gift_add_activity
import com.example.giftimoa.Collect_gift_add_info_activity
import com.example.giftimoa.R
import com.example.giftimoa.ViewModel.Gificon_ViewModel
import com.example.giftimoa.adpater_list.RecyclerViewCollectGiftAdapter
import com.example.giftimoa.dto.Collect_Gift
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Collect_fragment : Fragment() {

    private lateinit var giftViewModel: Gificon_ViewModel
    private lateinit var recyclerViewCollectGiftAdapter: RecyclerViewCollectGiftAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) // 프래그먼트에서 옵션 메뉴 사용을 활성화
        giftViewModel = ViewModelProvider(requireActivity()).get(Gificon_ViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_collect, container, false)

        //툴바 활성화
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "GIFTIMOA"

        return view
    }
    private val activityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            // Get the gift from the result Intent
            val collectGift = result.data!!.extras!!.get("gift") as Collect_Gift
            // Add the gift to the ViewModel
            giftViewModel.addGift(collectGift)
        }
    }

    private fun startCollectGiftAddActivity() {
        val intent = Intent(requireContext(), Collect_gift_add_activity::class.java)
        activityResult.launch(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noGifticonTextView = view.findViewById<TextView>(R.id.tv_noGifticon)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_Gift_Collect)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireActivity(), 2)
        recyclerView.layoutManager = layoutManager

        // 뷰모델을 이용해 기프티콘 등록
        giftViewModel.collectGifts.observe(viewLifecycleOwner, { gifts ->
            // Clone the gift list and convert to MutableList
            val giftList = gifts.toMutableList()

            // 어댑터를 생성하고 아이템 클릭 리스너를 설정합니다.
            recyclerViewCollectGiftAdapter = RecyclerViewCollectGiftAdapter(giftList) { gift ->
                // Start the new activity
                val intent = Intent(requireContext(), Collect_gift_add_info_activity::class.java)
                intent.putExtra("gift", gift)
                startActivity(intent)
            }

            recyclerView.adapter = recyclerViewCollectGiftAdapter

            // 어댑터에 데이터 추가
            recyclerViewCollectGiftAdapter.setGiftList(giftList)
            recyclerViewCollectGiftAdapter.notifyDataSetChanged()
            Log.d("Collect_fragment", "Gift list updated. Total gifts: ${giftList.size}")

            // 만약 기프티콘이 등록 되면 등록안내 문구 hide 아니면 show
            if (giftList.isEmpty()) {
                noGifticonTextView.visibility = View.VISIBLE
            } else {
                noGifticonTextView.visibility = View.GONE
            }
        })

        // 플로팅 버튼 클릭 시 다음 화면의 액티비티로 이동(기프티콘 등록)
        view.findViewById<FloatingActionButton>(R.id.fab_btn).setOnClickListener {
            startCollectGiftAddActivity()
        }
    }


    //액션바 옵션(검색)
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.collect_fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}
