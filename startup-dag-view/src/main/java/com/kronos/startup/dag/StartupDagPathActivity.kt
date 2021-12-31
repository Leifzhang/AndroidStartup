package com.kronos.startup.dag

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kronos.startup.dag.sql.StartupDatabaseHelper
import com.kronos.startup.dag.sql.StartupPathDataInfo
import com.kronos.startup.dag.utils.getSimpleName
import com.kronos.startup.dag.utils.postUI
import kotlin.concurrent.thread

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/30
 *
 */
class StartupDagPathActivity : AppCompatActivity(R.layout.startup_activity_dag_path) {
    private lateinit var textView: TextView
    private lateinit var lastDagView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        textView = findViewById(R.id.currentDag)
        lastDagView = findViewById(R.id.lastDag)
        thread {
            val dao = StartupDatabaseHelper.databaseInstance.startupDao()
            val data = dao.getStartupPathInfo(StartupDagInstallProvider.formatKey)
            data?.apply {
                val oldDag = dao.selectNotSame(pathHashCode)
                postUI {
                    lastDagView.text = oldDag?.dagPath.text()
                    textView.text = this.dagPath.text()
                }
            }
        }

    }
}


fun Context.startDagPathActivity() {
    val intent = Intent(this, StartupDagPathActivity::class.java)
    if (this !is Activity) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}

fun MutableList<StartupPathDataInfo>?.text(): String {
    val stringBuilder = StringBuilder()
    this?.forEach {
        stringBuilder.append("threadName:${it.name?.getSimpleName()} isMain:${it.mainThread}")
            .append("\r\n")
    }
    return stringBuilder.toString()
}