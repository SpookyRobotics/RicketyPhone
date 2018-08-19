package com.spookyrobotics.ricketyphone

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.ViewGroup
import com.spookyrobotics.graphpaper.RelativeGraphView
import com.spookyrobotics.graphpaper.offsetShapes.Triangle

class MainActivity : FragmentActivity() {
    val graphView : MutableList<RelativeGraphView> = mutableListOf()
    private var broadcastReceiver: BroadcastReceiver = buildBroadcastReceiver()
    private lateinit var localBroadcastManager: LocalBroadcastManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val view: ViewGroup = findViewById(R.id.content) as ViewGroup
        IntRange(0, view.childCount-1)
                .map {view.getChildAt(it) as RelativeGraphView }
                .forEach {
                    it.drawColumns = false
                    it.drawRows = false
                    graphView.add(it)
                }
        localBroadcastManager = LocalBroadcastManager.getInstance(this)

    }

    override fun onStart() {
        super.onStart()
        registerUpdateReceiver()
    }

    override fun onStop() {
        super.onStop()
        unregisterUpdateReceiverf()
    }

    private fun registerUpdateReceiver() {
        localBroadcastManager.registerReceiver(broadcastReceiver, StateUpdateBroadcastReceiver.buildUpdateIntentFilter())
    }

    private fun unregisterUpdateReceiverf() {
        localBroadcastManager.unregisterReceiver(broadcastReceiver)
    }

    private fun buildBroadcastReceiver(): BroadcastReceiver {
        return object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val cellToUpdate = StateUpdateBroadcastReceiver.getGridCellToUpdate(intent)
                val updateValue = StateUpdateBroadcastReceiver.getUpdateValue(intent)
                if (cellToUpdate != null && updateValue!= null) {
                    updateCell(cellToUpdate, updateValue)
                }
            }

        }
    }

    private fun updateCell(cellToUpdate: Int, updateValue: Int) {
        if (cellToUpdate >= graphView.size) {
            Log.e(MainActivity::class.java.simpleName, "Received updated for non existing cell")
            return
        }
        val cell = graphView.get(cellToUpdate)
        executeUpdateAction(cell, updateValue)
    }

    private fun executeUpdateAction(cell: RelativeGraphView, updateValue: Int) {
        if (updateValue == 1) {
            cell.addStaticOffsetDrawing(Triangle(cell,1f, 0.5f , false))
            cell.addStaticOffsetDrawing(Triangle(cell,0.8f, 0.45f , false))
            cell.addStaticOffsetDrawing(Triangle(cell,0.4f, 0.2f , false))

        } else {
            cell.removeOffsetInteractions()
        }
    }


}