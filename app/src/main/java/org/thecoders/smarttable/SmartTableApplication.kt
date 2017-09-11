package org.thecoders.smarttable

import android.app.Application

/**
 * Created by frenz on 22.06.2017.
 */

class SmartTableApplication : Application() {

    val appComponent: AppComponent = DaggerAppComponent.builder()
            .androidModule(AndroidModule(this))
            .build()

}