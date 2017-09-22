package org.thecoders.smarttable

import android.content.SharedPreferences
import dagger.Component
import javax.inject.Singleton

/**
 * Created by frenz on 21.09.2017.
 */

@Singleton
@Component(modules = arrayOf(DataModule::class, AndroidModule::class))
interface PreferenceComponent {

    fun getSharedPreferences(): SharedPreferences

}