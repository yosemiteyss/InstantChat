package com.yosemitedev.instantchat.ui.overview.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yosemitedev.instantchat.databinding.FragmentContactEditBinding
import com.yosemitedev.instantchat.utils.AutoClearedValue
import dagger.android.support.DaggerFragment

class ContactEditFragment : DaggerFragment() {

    private var binding by AutoClearedValue<FragmentContactEditBinding>(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}