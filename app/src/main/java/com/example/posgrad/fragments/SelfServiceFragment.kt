package com.example.posgrad.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.posgrad.R
import com.github.mikephil.charting.data.*
import kotlinx.android.synthetic.main.fragment_self.*
import java.security.KeyStore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SelfServiceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_self, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        /*
        val entries = ArrayList<BarEntry>()
        val entry1 : BarEntry = BarEntry(0f, 30f)

        entries.add(entry1)
        val entry2 : BarEntry = BarEntry(1f, 40f)
        entries.add(entry2)
        val entry3 : BarEntry = BarEntry(2f, 50f)
        entries.add(entry3)

        val data1 : BarDataSet= BarDataSet(entries, "teste")
        val dataset : BarData = BarData(data1)

        chart1.data = dataset
        chart1.invalidate()
        */
        super.onActivityCreated(savedInstanceState)
    }
}
