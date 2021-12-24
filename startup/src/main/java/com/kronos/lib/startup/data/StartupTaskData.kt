package com.kronos.lib.startup.data

import android.os.Parcel
import android.os.Parcelable
import android.os.SystemClock
import com.kronos.lib.startup.StartupAwaitTask
import com.kronos.lib.startup.StartupTask

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/27
 *
 */
data class StartupTaskData(
    val taskName: String?,
    val message: String?,
    var threadName: String? = null,
    val begin: Long = SystemClock.elapsedRealtime(),
    val dependencies: MutableList<String> = mutableListOf()
) : Parcelable {

    private var taskStart: Long = begin

    var duration: Long = 0
    var taskAwaitDuration: Long = 0

    fun getTaskEndTime(): Long {
        return begin + duration
    }

    fun taskFinish(task: StartupTask) {
        if (task is StartupAwaitTask) {
            taskAwaitDuration = task.awaitDuration
        }
        threadName = Thread.currentThread().name
        duration = SystemClock.elapsedRealtime() - taskStart
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong()
    ) {
        parcel.readStringList(dependencies)
        taskStart = parcel.readLong()
        duration = parcel.readLong()
        taskAwaitDuration = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(taskName)
        parcel.writeString(message)
        parcel.writeString(threadName)
        parcel.writeLong(begin)
        parcel.writeStringList(dependencies)
        parcel.writeLong(taskStart)
        parcel.writeLong(duration)
        parcel.writeLong(taskAwaitDuration)
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
