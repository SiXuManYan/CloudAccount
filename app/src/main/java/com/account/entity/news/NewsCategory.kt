package com.account.entity.news

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * 资讯 tab 分类
 */
@Entity(tableName = "news_category")
class NewsCategory constructor() {


    @PrimaryKey
    var id: Long = 1

    @ColumnInfo(name = "create_time")
    var createTime: String = ""
    var name: String = ""
    var type: String = ""

    @ColumnInfo(name = "type_name")
    var typeName: String = ""
    var value: String = ""

    @Ignore
    constructor(
        createTime: String = "",
        id: String = "0",
        name: String = "",
        type: String = "",
        typeName: String = "",
        value: String = ""
    ) : this() {
        this.createTime = createTime
        this.name = name
        this.type = type
        this.typeName = typeName
        this.id = id.toLong()
        this.value = value
    }


}

/*

{
    "id": "95",
    "value": "N1",
    "createTime": "2020-05-13 12:54:55",
    "type": "news_type",
    "typeName": "资讯类型",
    "name": "平台资讯"
}
*/

