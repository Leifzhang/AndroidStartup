package com.kronos.startup.dag.sql

import androidx.room.TypeConverter
import org.json.JSONArray
import org.json.JSONObject

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/14
 *
 */
class PathTypeConvert {

    @TypeConverter
    fun toString(path: MutableList<StartupPathDataInfo>): String {
        val jsonArray = JSONArray()
        path.forEach {
            val json = JSONObject()
            json.put("name", it.name)
            json.put("threadName", it.threadName)
            jsonArray.put(json)
        }
        return jsonArray.toString()
    }

    @TypeConverter
    fun toList(path: String): MutableList<StartupPathDataInfo> {
        val jsonArray = JSONArray(path)
        val list = mutableListOf<StartupPathDataInfo>()
        for (index in 0 until jsonArray.length()) {
            val json = jsonArray.optJSONObject(index)
            val data = StartupPathDataInfo(json.optString("name"), json.optString("threadName"))
            list.add(data)
        }
        return list
    }
}
