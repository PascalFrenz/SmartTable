/*
 * The MIT License
 * Copyright (c) 2011 Santiago Lezica (slezica89@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package org.thecoders.smarttable

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ListView

class RearrangeableListView : ListView {

    interface RearrangeListener {
        fun onGrab(index: Int)
        fun onRearrangeRequested(fromIndex: Int, toIndex: Int): Boolean
        fun onDrop()
    }

    /* To avoid burdening the ListView with data management logic,
     * the user must provide a RearrangeListener that will be:
     *
     * a) Notified when an item is grabbed.
     *
     * b) Notified when an item is supposed to be rearranged,
     *    and given the chance to allow or disallow the movement
     *    by returning true/false
     *
     * c) Notified when the grabbed item is dropped.
     *
     * EXAMPLE IMPLEMENTATION (WITH AN ARRAY ADAPTER IN MIND):
     *
     *  final RearrangeListener mRearrangeListener = new RearrangeListener () {
     *      @Override
     *       public void onGrab(int index) {
     *           getItem(index).doSomething();
     *           notifyDataSetChanged();
     *       }
     *
     *       public boolean onRearrangeRequested(int fromIndex, int toIndex) {
     *
     *           if (toIndex >= 0 && toIndex < getCount()) {
     *               Object item = getItem(fromIndex);
     *
     *                  remove(item);
     *                  insert(item, toIndex);
     *                  notifyDataSetChanged();
     *
     *                  return true;
     *              }
     *
     *              return false;
     *          }
     *
     *          @Override
     *          public void onDrop() {
     *              doSomethingElse();
     *              notifyDataSetChanged();
     *          }
     *      };
     *  }
     */

    interface MovableView {
        fun onGrabAttempt(x: Int, y: Int): Boolean
        fun onRelease()
    }

    private var mHeldItemIndex = -1
    private var mRearrangeEnabled = false

    var rearrangeListener: RearrangeListener? = null

    private var mScrollState = AUTO_SCROLL_DISABLED

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context) : super(context)

    fun setRearrangeEnabled(value: Boolean) {
        mRearrangeEnabled = value
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        /* We want to steal the DOWN event from the list items */

        if (rearrangeListener != null && mRearrangeEnabled) {
            val action = ev.action and MotionEvent.ACTION_MASK

            if (action == MotionEvent.ACTION_DOWN) {

                val x = ev.x.toInt()
                val y = ev.y.toInt()

                return grabItemAt(x, y)
            }
        }

        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (rearrangeListener == null || !mRearrangeEnabled || mHeldItemIndex < 0)
            return super.onTouchEvent(ev)

        val action = ev.action and MotionEvent.ACTION_MASK

        when (action) {

            MotionEvent.ACTION_MOVE -> {

                val x = ev.x.toInt()
                val y = ev.y.toInt()

                val otherIndex = itemIndexAt(x, y)

                if (otherIndex < 0)
                    return true

                val view = getChildAt(mHeldItemIndex - firstVisiblePosition)

                if (otherIndex != mHeldItemIndex) {
                    moveHeldItem(otherIndex)

                } else {
                    /* We may be at the top, or the bottom, and need to scroll

                     * Note that, while we'll check if this item is the first or last
                     * one visible, we won't verify if it's the first or last in our
                     * backing dataset. We leave that task to our listener, since
                     * it may want to create additional items and insert them while
                     * rearranging.
                     */
                    if (mHeldItemIndex == firstVisiblePosition && y < view.height / 2) {

                        /* The user wants to rearrange the item upwards, but this is
                         * the first item visible to him. If the move is approved by our
                         * listener, we'll need to scroll up.
                         */
                        setAutoScroll(AUTO_SCROLL_UP)

                    } else if (mHeldItemIndex == lastVisiblePosition && y > height - view.height / 2) {

                        /* Same logic as above, only, you know, down */
                        setAutoScroll(AUTO_SCROLL_DOWN)

                    } else {
                        /* We are at the top/bottom, but the user doesn't seem to
                         * want to keep dragging the view upwards/downwards.
                         */
                        setAutoScroll(AUTO_SCROLL_DISABLED)
                    }
                }

            }

            MotionEvent.ACTION_UP -> {

                dropHeldItem()
                setAutoScroll(AUTO_SCROLL_DISABLED)

            }
        }

        return true
    }

    private fun grabItemAt(x: Int, y: Int): Boolean {
        /* We'll return whether we successfully grabbed the item */

        val itemIndex = itemIndexAt(x, y)

        if (itemIndex >= 0) {
            val itemView = getChildAt(itemIndex - firstVisiblePosition)

            if (itemView is MovableView) {
                /* Views implementing this interface can
                 * catch this event and decide whether they'll
                 * allow us to move them.
                 */

                val childX = x - itemView.left
                val childY = y - itemView.top

                val allowed = itemView.onGrabAttempt(childX, childY)

                if (allowed) {
                    /* View says yes! Yee-ha!*/
                    grabItemByIndex(itemIndex)
                }

                println("Allowed: " + allowed)
                return allowed

            } else {
                /* Ironically, views that don't implement MovableView are
                 * assumed to be ok with this.
                */

                grabItemByIndex(itemIndex)
                println("Allowed: true")
                return true
            }
        }

        println("Allowed: false")
        return false /* itemIndexAt returned invalid index */
    }

    private fun grabItemByIndex(itemIndex: Int) {
        /* No validations are made. We assume the index is correct */
        mHeldItemIndex = itemIndex
        rearrangeListener!!.onGrab(itemIndex)
    }

    private fun moveHeldItem(toIndex: Int): Boolean {
        val allowed = rearrangeListener!!.onRearrangeRequested(mHeldItemIndex, toIndex)

        if (allowed) {
            /* The rearrangement took place, according to the listener */
            mHeldItemIndex = toIndex
        }

        return allowed
    }

    private fun dropHeldItem() {
        val itemView = getChildAt(mHeldItemIndex - firstVisiblePosition)

        if (itemView is MovableView) {
            itemView.onRelease()
        }

        rearrangeListener!!.onDrop()
        mHeldItemIndex = -1
    }

    /* We'll use this Rect to avoid creating a new one in each call to itemIndexAt() */
    private val itemIndexAt_tempRect = Rect()

    private fun itemIndexAt(x: Int, y: Int): Int {
        val hitRect = itemIndexAt_tempRect

        val childCount = childCount

        for (i in 0 until childCount) {
            getChildAt(i).getHitRect(hitRect)

            if (hitRect.contains(x, y))
                return i + firstVisiblePosition
        }

        return -1
    }


    private val mAutoScroll: Runnable = object : Runnable {
        override fun run() {

            if (mScrollState != AUTO_SCROLL_DISABLED && moveHeldItem(mHeldItemIndex + mScrollState)) {

                if (mScrollState == AUTO_SCROLL_UP)
                    setSelection(firstVisiblePosition - 1)
                else
                    setSelection(firstVisiblePosition + 1)

                postDelayed(this, AUTO_SCROLL_DELAY.toLong())
            }
        }
    }

    private fun setAutoScroll(scrollState: Int) {

        if (mScrollState == scrollState)
            return  /* Nothing to do */

        if (mScrollState == AUTO_SCROLL_DISABLED) {
            /* We have to start scrolling */
            mScrollState = scrollState
            mAutoScroll.run()

        } else {
            /* We were already scrolling */
            mScrollState = scrollState

            if (mScrollState == AUTO_SCROLL_DISABLED)
                removeCallbacks(mAutoScroll)
        }

    }

    companion object {

        /* Some Views may want to define areas from which they can be
     * dragged. That logic doesn't concern us either. Views in this
     * list that implement MovableView will be:
     *
     * a) Notified upon a grab attempt, and asked if they'll allow
     *    it given the touch coordinates (x, y) (relative to their own
     *    top-left).
     *
     * b) Notified when they are released.
     *
     * EXAMPLE IMPLEMENTATION (View with a grab-handle in mind):
     *
     *  @Override
     *  public boolean onGrabAttempt(int x, int y) {
     *      Rect hitRect = new Rect();
     *      this.mMyHandleView.getHitRect(hitRect);
     *
     *      return hitRect.contains(x, y);
     *  }
     *
     *  @Override
     *  public void onRelease() {}
     *
     */

        private val AUTO_SCROLL_DELAY = 300

        private val AUTO_SCROLL_UP = -1
        private val AUTO_SCROLL_DOWN = 1
        private val AUTO_SCROLL_DISABLED = 0
    }
}
