package org.thecoders.smarttable

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by frenz on 22.06.2017.
 */

@Module
class AndroidModule(private val context: Context) {

    @Singleton @Provides fun provideContext(): Context = context

}