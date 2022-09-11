package com.biocatch.compose1_2_1test

import android.app.Application
import android.content.Context
import android.util.Log
import com.biocatch.compose1_2_1test.SDKManager.SDK_TAG
import com.biocatch.compose1_2_1test.configurations.FragmentConfigurations
import com.biocatch.client.android.sdk.contract.ExtendedOptions
import com.biocatch.client.android.sdk.contract.events.NewSessionStartedEvent
import com.biocatch.client.android.sdk.contract.events.StateChangedEvent
import java.util.*

class App : Application(), Observer {

    override fun onCreate() {
        super.onCreate()

        startBioCatch()
        // ...
        // ...
        // other application code
    }


    /**
     * Starts the data collection process.
     *
     * Best practice:
     *
     * for maximum protection we strongly recommend starting the SDK as soon as possible.
     *
     * There are options for us to handle cases where we can't start the SDK early
     * which are demonstrated in a different module, yet if possible - use this
     * method
     */

    private fun startBioCatch() {
        // This is just where we store the WUPS server url for our demo app.
        val preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        val serverUrl = preferences.getString(FragmentConfigurations.SERVER_URL_ARGS, "https://wup-4ff4f23f.eu.v2.we-stats.com")
        val customerID = preferences.getString(FragmentConfigurations.CUSTOMER_ID_ARGS, "dummy")
        val csid = UUID.randomUUID().toString()
        val extendedOptions = ExtendedOptions()
        extendedOptions.externalModulesList =
            listOf("com.biocatch.android.jetpackcomposesdk.JetpackComposeFeature")

        // Here we actually register this class as an observer of changes and then
        // start the SDK for data collection.
        SDKManager.addObserver(this)
        SDKManager.start(serverUrl!!, customerID!!, this, csid = csid, extendedOptions = extendedOptions)
    }

    override fun update(changedObject: Observable?, event: Any?) {
        when {
            changedObject is SDKManager && event is StateChangedEvent -> Log.d(SDK_TAG, "Current SDK state: ${event.state!!.name}")
            changedObject is SDKManager && event is NewSessionStartedEvent -> Log.d(SDK_TAG, "New SDK Session with id: ${event.sessionID}")
        }
    }
}