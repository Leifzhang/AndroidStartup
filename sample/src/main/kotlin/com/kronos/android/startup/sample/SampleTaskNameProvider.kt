package com.kronos.android.startup.sample

import com.kronos.startup.TaskNameProvider

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/31
 *
 */
sealed class SampleTaskNameProvider : TaskNameProvider

object AsyncTask1Provider : SampleTaskNameProvider()

object SimpleTask1Provider : SampleTaskNameProvider()

object SimpleTask2Provider : SampleTaskNameProvider()

object SimpleTask3Provider : SampleTaskNameProvider()

object MyAnchorTaskProvider : SampleTaskNameProvider()


