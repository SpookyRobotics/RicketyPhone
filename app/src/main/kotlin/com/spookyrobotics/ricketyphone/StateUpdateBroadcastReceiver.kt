package com.spookyrobotics.ricketyphone

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager

class StateUpdateBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.apply {
            val cellToUpdate = getIntExtra(GRID_CELL_TO_UPDATE, UNKNOWN)
            val updateValue = getIntExtra(UPDATE_VALUE, UNKNOWN)
            if (cellToUpdate != UNKNOWN && updateValue != UNKNOWN) {
                context?.let {
                    sendUpdate(it, cellToUpdate, updateValue)
                }
            }
        }
    }

    private fun sendUpdate(context: Context, cellToUpdate: Int, updateValue: Int) {
        val broadcastManager = LocalBroadcastManager.getInstance(context)
        val intent = buildUpdateIntent(cellToUpdate, updateValue)
        broadcastManager.sendBroadcast(intent)
    }

    companion object {
        val STATE_UPDATE = "STATE_UPDATE"
        val GRID_CELL_TO_UPDATE = "GRID_CELL_TO_UPDATE"
        val UPDATE_VALUE = "UPDATE_VALUE"
        val UNKNOWN = -1

        fun buildUpdateIntent(cellToUpdate: Int, updateValue: Int): Intent {
            return Intent(STATE_UPDATE).apply {
                putExtra(GRID_CELL_TO_UPDATE, cellToUpdate)
                putExtra(UPDATE_VALUE, updateValue)
            }
        }

        fun getGridCellToUpdate(intent: Intent?) : Int? {
            val result =  intent?.getIntExtra(GRID_CELL_TO_UPDATE, UNKNOWN) ?: UNKNOWN
            if (result == UNKNOWN) {
                return null
            }
            return result
        }

        fun getUpdateValue(intent: Intent?) : Int? {
            val result =  intent?.getIntExtra(UPDATE_VALUE, UNKNOWN) ?: UNKNOWN
            if (result == UNKNOWN) {
                return null
            }
            return result
        }

        fun buildUpdateIntentFilter(): IntentFilter {
            return IntentFilter(STATE_UPDATE)
        }
    }
}