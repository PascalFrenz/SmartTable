package org.thecoders.smarttable.helpers

import android.support.v7.widget.RecyclerView

/**
 * Created by frenz on 22.11.2017.
 */

interface AbstractAdapterInterface {
    fun onObjectEditRequest(toEdit: Any)
    fun onObjectDeleteRequest(toDelete: Any, adapter: RecyclerView.Adapter<*>)
}