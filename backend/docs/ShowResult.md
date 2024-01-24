# 正演结果

[TOC]

## 分页

##### 请求方式：`GET`

##### 请求地址：`/show-result/page`

##### 请求参数：

| 名称           | 类型     | 必需   | 描述                     |
| :----------- | :----- | :--- | :--------------------- |
| current      | Number | 否    | 当前页              |
| size     | Number | 否    | 每页记录数              |
| year     | Number | 否    | 年份              |

##### 响应参数：

| 名称                | 类型     | 描述                       |
| :---------------- | :----- | :----------------------- |

##### 请求示例：

```
as-demo-dev.wtzhaopin.com:8087/demo/show-result/page?year=2019&current=1&size=1
```

##### 响应示例：

```json
{
  "records": [
    {
      "id": 2,
      "siteCode": "DHF",
      "year": 2019,
      "month": 10,
      "day": 26,
      "gpp": "24.31",
      "npp": "10.797",
      "nep": "79",
      "ra": "24.31",
      "rh": "10.797",
      "cf": "0.638356",
      "cw": "1.286796",
      "cr": "24.31",
      "cveg": "10.797",
      "csom": "79",
      "af": "0.484223",
      "aw": "0.667158",
      "ar": "24.31",
      "tveg": "10.797",
      "tsoil": "79",
      "swc": null,
      "createTime": null
    }
  ],
  "total": 67,
  "size": 1,
  "current": 1,
  "orders": [],
  "optimizeCountSql": true,
  "hitCount": false,
  "countId": null,
  "maxLimit": null,
  "searchCount": true,
  "pages": 67
}
```