package com.github.wnebyte.chess.util

import android.os.Bundle
import android.os.Parcel

class BundleUtil
{
    companion object
    {
        /**
         * Returns the size of the bundle in bytes.
         */
        fun getSize(bundle: Bundle) : Int
        {
            val parcel = Parcel.obtain()
            var size = 0

            parcel.writeBundle(bundle)
            size = parcel.dataSize()
            parcel.recycle()

            return size
        }
    }
}