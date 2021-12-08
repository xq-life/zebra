package com.cbq.zebra

import android.app.Activity
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import org.json.JSONObject
import android.content.*
import android.os.Bundle
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.EventSink
import io.flutter.plugin.common.EventChannel.StreamHandler
import java.text.SimpleDateFormat
import java.util.*

/** ZebraPlugin */
class ZebraPlugin : FlutterPlugin, MethodCallHandler {
    private val COMMAND_CHANNEL = "com.cbq.zebra/command"
    private val SCAN_CHANNEL = "com.cbq.zebra/scan"

    private val PROFILE_INTENT_ACTION = "com.darryncampbell.datawedgeflutter.SCAN"
    private val PROFILE_INTENT_BROADCAST = "2"

    private val DATAWEDGE_SEND_ACTION = "com.symbol.datawedge.api.ACTION"
    private val DATAWEDGE_RETURN_ACTION = "com.symbol.datawedge.api.RESULT_ACTION"
    private val DATAWEDGE_RETURN_CATEGORY = "android.intent.category.DEFAULT"
    private val DATAWEDGE_SCAN_EXTRA_DATA_STRING = "com.symbol.datawedge.data_string"
    private val DATAWEDGE_SCAN_EXTRA_LABEL_TYPE = "com.symbol.datawedge.label_type"

    private lateinit var channel: MethodChannel
    private lateinit var context: Context

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        context = flutterPluginBinding.applicationContext
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, COMMAND_CHANNEL)
        channel.setMethodCallHandler(this)
        EventChannel(flutterPluginBinding.binaryMessenger, SCAN_CHANNEL).setStreamHandler(
                object : StreamHandler {
                    private var dataWedgeBroadcastReceiver: BroadcastReceiver? = null
                    override fun onListen(arguments: Any?, events: EventSink?) {
                        dataWedgeBroadcastReceiver = createBroadcastReceiver(events)
                        val intentFilter = IntentFilter()
                        intentFilter.addAction(PROFILE_INTENT_ACTION)
                        intentFilter.addAction(DATAWEDGE_RETURN_ACTION)
                        intentFilter.addCategory(DATAWEDGE_RETURN_CATEGORY)
                        context.registerReceiver(dataWedgeBroadcastReceiver, intentFilter)
                    }

                    override fun onCancel(arguments: Any?) {
                        context.unregisterReceiver(dataWedgeBroadcastReceiver)
                        dataWedgeBroadcastReceiver = null
                    }
                }
        )
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == "sendDataWedgeCommandStringParameter") {
            val arguments = JSONObject(call.arguments.toString())
            val command: String = arguments.get("command") as String
            val parameter: String = arguments.get("parameter") as String
            val intent = Intent()
            intent.action = DATAWEDGE_SEND_ACTION
            intent.putExtra(command, parameter)
            context.sendBroadcast(intent)
        } else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    private fun createBroadcastReceiver(events: EventSink?): BroadcastReceiver? {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                var scanData = intent.getStringExtra(DATAWEDGE_SCAN_EXTRA_DATA_STRING)
                println("### Xq Zebra ### scanData:" + scanData)
                events?.success(scanData)
            }
        }
    }

}
