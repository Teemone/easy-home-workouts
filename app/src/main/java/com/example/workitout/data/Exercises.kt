package com.example.workitout.data

import android.os.Parcel
import android.os.Parcelable

data class Exercises(
    val id: Int,
    val name: String?,
    val image: Int,
    val duration: Long = Constants.DEFAULT_DURATION,
    var isCompleted: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readLong()
    ) {
    }

    fun durationAsInt(): Int{
        return (duration/1000).toInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(image)
        parcel.writeLong(duration)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Exercises> {
        override fun createFromParcel(parcel: Parcel): Exercises {
            return Exercises(parcel)
        }

        override fun newArray(size: Int): Array<Exercises?> {
            return arrayOfNulls(size)
        }
    }
}
