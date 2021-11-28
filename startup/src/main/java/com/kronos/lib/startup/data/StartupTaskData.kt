package com.kronos.lib.startup.data

import android.os.Parcel
import android.os.Parcelable
import android.os.SystemClock

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/27
 *
 */
data class StartupTaskData(
    val taskName: String?,
    val message: String?,
    val dependencies: MutableList<String> = mutableListOf()
) : Parcelable {
    private var taskStart: Long = SystemClock.elapsedRealtime()

    var duration: Long = 0

    fun taskFinish() {
        duration = SystemClock.elapsedRealtime() - taskStart
    }



    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
    ) {
        parcel.readStringList(dependencies)
        taskStart = parcel.readLong()
        duration = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(taskName)
        parcel.writeString(message)
        parcel.writeStringList(dependencies)
        parcel.writeLong(taskStart)
        parcel.writeLong(duration)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StartupTaskData> {
        override fun createFromParcel(parcel: Parcel): StartupTaskData {
            return StartupTaskData(parcel)
        }

        override fun newArray(size: Int): Array<StartupTaskData?> {
            return arrayOfNulls(size)
        }
    }

}
