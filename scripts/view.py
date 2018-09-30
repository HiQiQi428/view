#coding=utf-8
import sys
import pymysql
from requests import get
import argparse
from os import path, getcwd, mkdir, removedirs, listdir, remove, rmdir

downloadAPI = 'http://ai.uppfind.com:80/view/generic/loadImage?picName='
dbinfo = {
    'host': 'luncert.cn',
    'user': 'viewUser',
    'password': 'MobileAI403',
    'database': 'view'
}

def rm(basePath):
    if path.isdir(basePath):
        for file in listdir(basePath):
            rm(path.join(basePath, file))
        rmdir(basePath)
    else:
        remove(basePath)

def connectDB():
    global dbinfo
    return pymysql.connect(dbinfo['host'], dbinfo['user'], dbinfo['password'], dbinfo['database'])

def info():
    db = connectDB()
    cursor = db.cursor()
    cursor.execute("select pigId, count(picName) as picNum from Records group by pigId")
    data = {}
    for row in cursor.fetchall():
        pigId = row[0]
        picNum = row[1]
        data[pigId] = picNum
    print('total pig num: %d' % (len(data)))
    print('-' * 14)
    print('pigId | picNum')
    for item in data.items():
        print(str(item[0]).center(5), '|', str(item[1]).center(5))
    db.close()

def download(pigId):
    # get data
    db = connectDB()
    cursor = db.cursor()
    cursor.execute("select picName, timestamp from Records where pigId = '%d'" % (pigId))
    data = []
    for row in cursor.fetchall():
        data.append({
            'picName': row[0],
            'timestamp': row[1]
        })
    db.close()
    totalNum = len(data)
    if totalNum == 0:
        print(u'无数据')
        return
    # make dir
    storePath = path.join(getcwd(), 'pig-' + str(pigId))
    if not path.exists(storePath): mkdir(storePath)
    # download
    global downloadAPI
    for i in range(totalNum):
        picName = data[i]['picName']
        timestamp = data[i]['timestamp'].replace(':', '-')
        print('Downloading %d/%d: %s' % (i + 1, totalNum, picName), end = '\r')
        with open(path.join(storePath, timestamp + '.jpg'), 'wb') as f:
            f.write(get(downloadAPI + picName).content)

def downloadAll():
    # get data
    db = connectDB()
    cursor = db.cursor()
    cursor.execute("select pigId, picName, timestamp from Records")
    data = {}
    picNum = 0
    for row in cursor.fetchall():
        pigId = row[0]
        if not pigId in data: data[pigId] = []
        data[pigId].append({
            'picName': row[1],
            'timestamp': row[2]
        })
        picNum += 1
    if len(data) == 0:
        print(u'无数据')
        return
    # make dir
    basePath = path.join(getcwd(), 'images')
    if path.exists(basePath): rm(basePath)
    mkdir(basePath)
    # download
    global downloadAPI
    for pigId in data:
        pig = data[pigId]
        totalNum = len(pig)
        storePath = path.join(basePath, 'pig-' + str(pigId))
        mkdir(storePath)
        for i in range(totalNum):
            picName = pig[i]['picName']
            timestamp = pig[i]['timestamp'].replace(':', '-')
            print('Downloading %d/%d: %s' % (i + 1, totalNum, picName), end = '\r')
            with open(path.join(storePath, timestamp + '.jpg'), 'wb') as f:
                f.write(get(downloadAPI + picName).content)

parser = argparse.ArgumentParser(description = '图片库工具', prog = 'view')
parser.add_argument('-i', '--info', action = 'store_const', const = True, help = '获取图片库信息')
parser.add_argument('-f', '--fetch', dest = 'pigId', type = int, help = '下载指定猪的图片库')
parser.add_argument('-fa', '--fetchAll', action = 'store_const', const = True, help = '下载整个图片库')
args = parser.parse_args()

if args.info: info()
elif args.pigId: download(args.pigId)
elif args.fetchAll: downloadAll()
else: parser.print_usage()