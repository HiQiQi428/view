## 7-6
#### 1.根据url下载图片的功能
加入了包spring-boot-starter-thymeleaf，这个包自动扫描resources目录完成静态资源配置。所以我只需要把上传的图片写入这个目录就行了。
#### 2.neo4j查询返回了不符合条件的关联节点
是因为我主动RETURN了这个节点。。
#### 3.如何返回pig的fatherId和motherId字段
没有，最后我取消了trasient修饰符，但是也修改了cypher语句使得只有在单个查询的时候才返回关联节点

## 7:25.pm 开始测试
#### 1.pig/getStrainMap
{
    "statusCode": 0,
    "data": {
            "1":"花猪"
        }
}
#### 2.pig/addPig
成功登记到数据库
#### 3.pig/fetchAllPigs
{
    "statusCode": 0,
    "data": [
        {
            "appetite": "",
            "beMale": false,
            "birthdate": "",
            "children": [],
            "eatingHabits": "",
            "father": null,
            "health": "good",
            "id": 0,
            "mother": null,
            "name": "pig2",
            "strain": 1,
            "userId": "lun"
        }
    ]
}
#### 4.pig/queryById
{
    "statusCode": 0,
    "data": {
        "appetite": "",
        "beMale": false,
        "birthdate": "2018-07-06 16:36:39",
        "children": [],
        "eatingHabits": "",
        "father": {
            "appetite": "",
            "beMale": false,
            "birthdate": "xxxx-xx-xx xx:xx:xx",
            "children": [],
            "eatingHabits": "",
            "father": null,
            "health": "good",
            "id": 20,
            "mother": null,
            "name": "pigx",
            "strain": 1,
            "userId": "lun"
        },
        "health": "good",
        "id": 40,
        "mother": null,
        "name": "pigy",
        "strain": 1,
        "userId": "lun"
    }
}
#### 5.pig/queryByName
{
    "statusCode": 0,
    "data": [
        {
            "appetite": "",
            "beMale": false,
            "birthdate": "xxxx-xx-xx xx:xx:xx",
            "children": [],
            "eatingHabits": "",
            "father": null,
            "health": "good",
            "id": 20,
            "mother": null,
            "name": "pigx",
            "strain": 1,
            "userId": "lun"
        }
    ]
}
#### 6.pig/queryByStrain
{
    "statusCode": 0,
    "data": [
        {
            "appetite": "",
            "beMale": false,
            "birthdate": "",
            "children": [],
            "eatingHabits": "",
            "father": null,
            "health": "good",
            "id": 0,
            "mother": null,
            "name": "pig2",
            "strain": 1,
            "userId": "lun"
        },
        {
            "appetite": "",
            "beMale": false,
            "birthdate": "xxxx-xx-xx xx:xx:xx",
            "children": [],
            "eatingHabits": "",
            "father": null,
            "health": "good",
            "id": 20,
            "mother": null,
            "name": "pigx",
            "strain": 1,
            "userId": "lun"
        }
    ]
}
#### 7.pig/deleteById
{
    "statusCode": 0
}
#### 8.pig/record/addRecord
成功添加到数据库，照片也上传成功:P
图片能否访问？不能
存放数据库的图片path不正确
#### 9.pig/record/fetchAllRecords
{
    "pageNum": 0,
    "pageSize": 10,
    "size": 1,
    "startRow": 1,
    "endRow": 1,
    "total": 1,
    "pages": 1,
    "list": [
        {
            "id": 1,
            "pigId": 12,
            "description": "good",
            "timestamp": 1530865414000,
            "picPath": "null"
        }
    ],
    "prePage": 0,
    "nextPage": 1,
    "isFirstPage": false,
    "isLastPage": false,
    "hasPreviousPage": false,
    "hasNextPage": true,
    "navigatePages": 8,
    "navigatepageNums": [
        1
    ],
    "navigateFirstPage": 1,
    "navigateLastPage": 1,
    "lastPage": 1,
    "firstPage": 1
}
#### 10.pig/record/fetchLastWeekRecords
同上
#### 11.pig/record/fetchLast3WeekRecords
同上