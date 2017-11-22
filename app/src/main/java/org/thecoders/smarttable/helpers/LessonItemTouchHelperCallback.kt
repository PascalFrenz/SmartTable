package org.thecoders.smarttable.helpers

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper


/**
 * Created by frenz on 14.09.2017.
 */
class LessonItemTouchHelperCallback(private val mAdapter: LessonItemTouchHelperAdapter) : ItemTouchHelper.Callback() {

    private val ALPHA_FULL = 1.0f

    //Swipe not enabled
    override fun isItemViewSwipeEnabled(): Boolean = false

    override fun isLongPressDragEnabled(): Boolean = true

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return if (recyclerView.layoutManager is GridLayoutManager) {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            val swipeFlags = 0
            ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
        } else {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = 0          //We don't want any swipe gestures yet, so this is disabled
            ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
        }
    }

    override fun onMove(recyclerView: RecyclerView, source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        if (source.itemViewType != target.itemViewType)
            return false

        mAdapter.onItemMove(source.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //Not needed because swipe should not be supported
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {

        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if(viewHolder is LessonItemTouchHelperViewHolder) {
                val itemViewHolder = viewHolder as LessonItemTouchHelperViewHolder
                itemViewHolder.onItemSelected()
            }
        }

        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        viewHolder.itemView.alpha = ALPHA_FULL

        if(viewHolder is LessonItemTouchHelperViewHolder) {
            val itemViewHolder = viewHolder as LessonItemTouchHelperViewHolder
            itemViewHolder.onItemClear()
        }
    }
}