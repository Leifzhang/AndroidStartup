package com.kronos.android.startup.sample

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kronos.lib.startup.startUp
import com.kronos.lib.startup.step.StepStartupPack
import com.kronos.startup.annotation.Lifecycle

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/9
 *
 */
class WebActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.textView).text = "WebActivity"
        startUp(this.application) {
            StepStartupPack.getTaskGroup(Lifecycle.AfterUserPrivacy)?.forEach {
                addProcTaskGroup(it)
            }
        }.build().start()
    }
}