package com.kronos.android.startup.sample.di

import com.kronos.startup.sample.report.di.ReportInitDelegate
import org.koin.dsl.module

/**
 *
 *  @Author LiABao
 *  @Since 2022/2/4
 *
 */
val appModule = module {
    single<ReportInitDelegate> {
        ReportInitDelegateImp()
    }
}

private class ReportInitDelegateImp : ReportInitDelegate {
    override fun getAppName(): String {
        return "123444556"
    }

}