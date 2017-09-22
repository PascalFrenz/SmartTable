package org.thecoders.smarttable

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import org.thecoders.smarttable.data.AppDatabase
import javax.inject.Singleton

/**
 * Created by frenz on 22.06.2017.
 */

@Module
class DataModule {

    @Singleton @Provides fun provideAppDatabase(context: Context): AppDatabase
            = AppDatabase.getPersistenceDatabase(context)

    @Singleton @Provides fun provideSharedPreferences(context: Context): SharedPreferences
            = PreferenceManager.getDefaultSharedPreferences(context)

}