package com.dave.springchatapp.tools

import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by David Hakobyan on 7/30/21.
 */
class WeakList<E> : AbstractList<E>() {
    private val items: ArrayList<WeakReference<E>> = ArrayList()
    override fun add(element: E): Boolean {
        items.add(WeakReference(element))
        return true
    }

    override fun add(index: Int, element: E) {
        items.add(index, WeakReference(element))
    }

    override fun get(index: Int): E? {
        return items[index].get()
    }

    override fun remove(element: E): Boolean {
        for (item in items) {
            if (item.get() === element) {
                items.remove(item)
                break
            }
        }
        return true
    }

    override fun contains(element: E): Boolean {
        for (item in items) {
            if (item.get() == element) {
                return true
            }
        }
        return false
    }

    private fun removeReleased() {
        for (i in items.indices.reversed()) {
            if (items[i].get() == null) {
                items.removeAt(i)
            }
        }
    }

    override val size: Int
        get() {
            removeReleased()
            return items.size
        }
}