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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.giftimoa.Collect_gift_add_activity
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
            val collectGift = result.data!!.getSerializableExtra("gift") as Collect_Gift
            // Add the gift to the ViewModel
            giftViewModel.addGift(collectGift)
        }
    }

    private fun startCollectGiftAddActivity() {
        val intent = Intent(requireContext(), Collect_gift_add_activity::class.java)
        // Launch Collect_gift_add_activity and register the callback to receive the result
        activityResult.launch(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_Gift_Collect)
        recyclerViewCollectGiftAdapter = RecyclerViewCollectGiftAdapter(mutableListOf())
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireActivity(), 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerViewCollectGiftAdapter

        giftViewModel.collectGifts.observe(viewLifecycleOwner, { gifts ->
            // Clone the gift list and convert to MutableList
            val giftList = gifts.toMutableList()

            // Update the adapter's data
            recyclerViewCollectGiftAdapter.setGiftList(giftList)
            recyclerViewCollectGiftAdapter.notifyDataSetChanged()
            Log.d("Collect_fragment", "Gift list updated. Total gifts: ${giftList.size}")
        })

        // 플로팅 버튼 클릭 시 다음 화면의 액티비티로 이동
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
