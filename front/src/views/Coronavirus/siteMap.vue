<template>
  <div v-loading="loading" style="width:100%;margin:auto;height:964px" element-loading-background="rgba(0, 0, 0, 0.8)"
    element-loading-text="数据正在加载中" element-loading-spinner="el-icon-loading">
    <el-row>
      <el-col>
        <div id="allmap" class="allmap"></div>
        <div ref="map" class="map">
          <div class="select">
            <el-select v-model="selectValue">
              <el-option v-for="item in selectOptions" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
          <div class="text">
            <el-radio-group v-model="ModeSwitch">
              <el-radio-button label="1">{{ $t('control.map') }}</el-radio-button>
              <el-radio-button label="2">{{ $t('control.satellite') }}</el-radio-button>
              <!-- <el-radio-button label="3">地形</el-radio-button> -->
            </el-radio-group>
          </div>
          <div class="Day">
            <el-radio-group v-model="DaySwitch">
              <el-radio-button label="1">{{ $t('control.year') }}</el-radio-button>
              <el-radio-button label="2">{{ $t('control.month') }}</el-radio-button>
              <el-radio-button label="3">{{ $t('control.day') }}</el-radio-button>
            </el-radio-group>
          </div>
          <div class="DateSwitch">
            <el-date-picker v-model="DateSwitch" type="daterange" align="right" unlink-panels
              :range-separator="$t('control.to')" :start-placeholder="$t('control.start')"
              :end-placeholder="$t('control.end')">
            </el-date-picker>
          </div>
          <div class="charts">
            <div class="chart" v-for="(item, index) in chart">
              <span class="name">{{ pinNum[pinNum.length - 1 - index] }} &lt {{ pinNum[pinNum.length - index - 2]
              }}</span>
              <div class="i" :style='{ background: item.color }'></div>
              <!-- <span>{{ item.name }}</span> -->
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import vueEvent from './mock/vueEvent'
import * as echarts from 'echarts';
import 'echarts/extension/bmap/bmap';
import Cookies from 'js-cookie'

import { forwardResults, getAllData, allstation } from '@/api/login'
import { getMap } from '@/api/Map';

export default {
  name: 'MyHead',
  data() {
    return {
      loading: true,
      map: undefined,
      routeStatuChange: true,
      mapData: {
        Gpp: 0,
        Npp: 0,
        Nep: 0,
        Cveg: 0,
        Csom: 0
      },
      siteInfo: [],
      points: [
        // { "lng": 123.418261, "lat": 41.921984, "count": 50 },
        // { "lng": 112.179863, "lat": 23.528986, "count": 100 }
      ],
      // 地图模式切换
      ModeSwitch: '1',
      // 根据年月日数据切换
      DaySwitch: '1',
      // 快捷选择器
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
      // 选择的日期
      DateSwitch: "",
      // 图例
      chart: [
        {
          name: '>200',
          color: '#b03a5b'
        },
        {
          name: '>200',
          color: '#d94e5d'
        },
        {
          name: '>200',
          color: '#ed2d2d'
        },
        {
          name: '150 - 200',
          color: '#de7152'
        },
        {
          name: '120 - 150',
          color: '#e39347'
        },
        {
          name: '90 - 120',
          color: '#e8b63c'
        },
        {
          name: '60 - 90',
          color: '#d4c249'
        },
        {
          name: '30 - 60',
          color: '#a8b86f'
        },
        {
          name: '5 - 30',
          color: '#7cad94'
        },
        {
          name: '<5',
          color: '#50a3ba'
        },
      ],
      // 选择器
      selectValue: 'NEP',
      selectOptions: [{
        value: 'GPP',
        label: 'GPP'
      }, {
        value: 'RE',
        label: 'RE'
      }, {
        value: 'NEP',
        label: 'NEP'
      }, {
        value: 'VEG',
        label: 'VEG'
      }, {
        value: 'Csom',
        label: 'Csom'
      }, {
        value: 'C_store',
        label: 'C_store'
      }, {
        value: 'C_store_addclit',
        label: 'C_store_addclit'
      }],
      // 热力点数据
      mapPoint: [],
      pointCollection: {},
      // 平均阈值
      pinNum: []
    }
  },
  watch: {
    // 模式切换
    ModeSwitch(newval, oldval) {
      // this.initMap()
      // this.initHeatMap()
      // this.allstation()
      if (newval == "1") {
        this.map.setMapType(BMAP_NORMAL_MAP);
      } else if (newval == "2") {
        this.map.setMapType(BMAP_SATELLITE_MAP);
      } else {

      }
    },
    // 年月日均切换
    DaySwitch(newval, oldval) {
      // this.initMap()
      // this.allstation()
      // this.initHeatMap()
      // 此处判断选择的选项进行数据请求
    },
    // 日期选择切换
    DateSwitch(newval, oldval) {
      // console.log(newval);
    },
    // 物质选择器切换
    selectValue(newval, oldval) {
      this.loading = true;
      let data = {
        // HistoryId: 77,
        // HistoryId: '223944761378213888',
        HistoryId: '226112251365560320',
        para: this.selectValue
      }
      getMap(data).then(res => {
        // this.administrationRegion(this.map)
        this.mapPoint = res.data.data
        this.initHeatMap()
      })
    }
  },
  created() {
  },
  mounted() {
    this.initMap()
    this.allstation()
    let data = {
      // HistoryId: 77,
      // HistoryId: '223944761378213888',
      HistoryId: '226112251365560320',
      para: this.selectValue
    }
    getMap(data).then(res => {
      this.mapPoint = res.data.data
      this.initHeatMap()
    })
    // 添加热力图
    // this.initHeatMap()
    // this.getAllData()
    vueEvent.$on('jumpWeb', (data) => {
      this.jumpSite(data)
    })
  },
  methods: {
    allstation() {
      allstation().then((res) => {
        // console.log(this.map);
        if (res.data.code === '10000') {
          this.siteInfo = res.data.data
          this.administrationRegion(this.map)
          this.points = res.data.data.map(val => {
            return {
              "lng": val.longitude,
              "lat": val.latitude,
              "count": val.id
            }
          })
          // 添加热力图
          // this.initHeatMap()
        }
      })
    },
    // getAllData() {
    //   getAllData().then((res) => {
    //     this.treeDataSpell(res)
    //   })
    // },
    jumpSite(info) {
      var isen = false;
      Cookies.get('language') == 'en' ? isen = true : isen = false;
      var name = ''
      if (info.title === 'dfs') {
        isen ? name = 'Dinghushan' : name = '鼎湖山'
      } else if (info.title === 'gz') {
        isen ? name = 'Guangzhou' : name = '广州'
      } else if (info.title === 'qyz') {
        isen ? name = 'Qianyanzhou' : name = '千烟洲'
      } else if (info.title === 'htf') {
        isen ? name = 'Huitong' : name = '会同'
      } else if (info.title === 'jff') {
        isen ? name = 'Jianfengling' : name = '尖峰岭'
      }
      if (isen) {
        var tip = `Whether to jump to the data prediction page of ${name}?`
      } else {
        var tip = `是否跳转到${name}站点的数据预测页面?`
      }
      this.$confirm(tip, this.$t('colony.colony_37'), {
        confirmButtonText: this.$t('colony.colony_38'),
        cancelButtonText: this.$t('colony.colony_39'),
        type: 'warning'
      }).then(() => {
        vueEvent.$emit('changePlatform', name)
        this.$router.push({
          name: 'siteDataPrediction',
          query: {
            code: info.code
          }
        })
      }).catch(() => {
        // map.openInfoWindow(infoWindow, point) // 开启信息窗口
        return
      })
    },
    initMap() {
      // 创建百度地图
      // if (this.ModeSwitch == '3') {
      //   var map = new BMapGL.Map('allmap', { enableMapClick: false })
      //   map.centerAndZoom(new BMapGL.Point(112.404, 39.915), 6);
      //   // 定义XYZLayer图层
      //   var wms = new BMapGL.XYZLayer({
      //     useThumbData: true,
      //     tileUrlTemplate: 'https://ows.mundialis.de/services/service?&service=WMS&' +
      //       'request=GetMap&layers=TOPO-WMS%2COSM-Overlay-WMS&styles=&format=image%2Fjpeg&' +
      //       'transparent=false&version=1.1.1&width=256&height=256&srs=EPSG%3A3857&bbox=[b]',
      //   });
      //   map.addTileLayer(wms);
      //   this.allstation()
      // } else {
      var map = new BMap.Map('allmap', { enableMapClick: false })
      map.centerAndZoom(new BMap.Point(108.179863, 33.528986), 6)
      // map.centerAndZoom(new BMap.Point(112.179863, 23.528986), 6)
      // }
      this.map = map
      map.enableScrollWheelZoom(true)

      var cityCtrl = new BMap.CityListControl({
        anchor: BMAP_ANCHOR_TOP_LEFT,
        offset: new BMapGL.Size(20, 20)
      });  // 添加城市列表控件
      map.addControl(cityCtrl);

      // var map = new BMapGL.Map('allmap'); // 创建Map实例
      // map.centerAndZoom(new BMapGL.Point(112.404, 39.915), 6); // 初始化地图,设置中心点坐标和地图级别
      // map.enableScrollWheelZoom(true); // 开启鼠标滚轮缩放

      // // 定义XYZLayer图层
      // var wms = new BMapGL.XYZLayer({
      //   useThumbData: true,
      //   tileUrlTemplate: 'https://ows.mundialis.de/services/service?&service=WMS&' +
      //     'request=GetMap&layers=TOPO-WMS%2COSM-Overlay-WMS&styles=&format=image%2Fjpeg&' +
      //     'transparent=false&version=1.1.1&width=256&height=256&srs=EPSG%3A3857&bbox=[b]',
      // });
      // map.addTileLayer(wms);


      // 缩放尺
      var zoomCtrl = new BMapGL.ZoomControl();  // 添加缩放控件
      map.addControl(zoomCtrl);
      // map.setMaxZoom(18);
      map.setMinZoom(6);
      // this.map.addEventListener("zoomend", this.initHeatMap);

      // map.addControl(new BMap.MapTypeControl({mapTypes: [BMAP_NORMAL_MAP,BMAP_SATELLITE_MAP ]}));
      // map.addControl(new BMap.MapTypeControl());// 地图类型
      // map.addControl(new BMap.NavigationControl());

      // 自定义控件
      function MessageControl() {
        // 默认停靠位置和偏移量
        this.defaultAnchor = BMAP_ANCHOR_TOP_RIGHT
        this.defaultOffset = new BMap.Size(10, 10)
      }

      // 通过JavaScript的prototype属性继承于BMap.Control
      MessageControl.prototype = new BMap.Control()

      // 自定义控件必须实现自己的initialize方法,并且将控件的DOM元素返回
      // 在本方法中创建个div元素作为控件的容器,并将其添加到地图容器中
      MessageControl.prototype.initialize = function (map) {
        // 创建一个DOM元素
        var div = document.createElement('div')
        // 添加文字说明
        div.appendChild(document.createTextNode('站点位置'))
        // 设置样式
        div.style.cursor = 'pointer'
        div.style.padding = '7px 10px'
        div.style.boxShadow = '0 2px 6px 0 rgba(27, 142, 236, 0.5)'
        div.style.borderRadius = '5px'
        div.style.backgroundColor = 'white'
        // 绑定事件,点击一次放大两级
        div.onclick = function (e) {
          map.centerAndZoom('肇庆市', 6)
        }
        // 添加DOM元素到地图中
        map.getContainer().appendChild(div)
        // 将DOM元素返回
        return div
      }
      // 创建控件
      const messageCtrl = new MessageControl()

      // 添加到地图当中
      // map.addControl(messageCtrl)


      // 缩略图
      // var overView = new BMap.OverviewMapControl();
      // map.addControl(overView);

      // 默认打开缩略图
      // var overViewOpen = new BMap.OverviewMapControl({isOpen:true, anchor: BMAP_ANCHOR_BOTTOM_RIGHT});
      // map.addControl(overViewOpen);      //右下角，打开

      // this.administrationRegion(map, this.mapData)

    },

    // 热力图
    initHeatMap() {
      this.map.clearOverlays()
      this.administrationRegion(this.map)
      // 清除图层(每次重新调用需要清除上一个覆盖物图层)
      // this.map.clearOverlays();
      // 添加热力覆盖物
      // this.map.addOverlay(this.heatmapOverlay);
      // this.map.addOverlay(this.heatmapOverlay1);
      if (this.selectValue == "NEP") {
        let num = this.mapPoint.map(item => item.value[2])
        // console.log(num);
        let max = Math.max(...num)
        let min = Math.min(...num)
        num.splice(num.indexOf(max), 1)
        num.splice(num.indexOf(min), 1)
        max = Math.max(...num)
        min = Math.min(...num)

        // console.log(max, min);
        let pin = (max - min) / 10
        // console.log(max);
        this.pinNum = []
        for (let i = min; i < max; i += pin) {
          if (i >= min + pin * 5) {
            i += pin * 5
            this.pinNum.push(Math.floor(i))
          } else {
            if (i != min) {
              i -= pin - 3
              this.pinNum.push(Math.floor(i))
            } else {
              this.pinNum.push(Math.floor(i))
              i += pin * 2
            }
          }

        }
        console.log(this.pinNum);
        let data = [[], [], [], [], [], [], [], [], [], []]
        this.mapPoint.map(item => {
          if (item.value[2] < this.pinNum[1] && item.value[2] > this.pinNum[0]) {
            data[0].push({
              lng: item.value[0],
              lat: item.value[1],
              count: item.value[2]
            })
          } else if (item.value[2] >= this.pinNum[1] && item.value[2] <= this.pinNum[2]) {
            data[1].push({
              lng: item.value[0],
              lat: item.value[1],
              count: item.value[2]
            })
          } else if (item.value[2] >= this.pinNum[2] && item.value[2] <= this.pinNum[3]) {
            data[2].push({
              lng: item.value[0],
              lat: item.value[1],
              count: item.value[2]
            })
          } else if (item.value[2] >= this.pinNum[3] && item.value[2] <= this.pinNum[4]) {
            data[3].push({
              lng: item.value[0],
              lat: item.value[1],
              count: item.value[2]
            })
          } else if (item.value[2] >= this.pinNum[4] && item.value[2] <= this.pinNum[5]) {
            data[4].push({
              lng: item.value[0],
              lat: item.value[1],
              count: item.value[2]
            })
          } else if (item.value[2] >= this.pinNum[5] && item.value[2] <= this.pinNum[6]) {
            data[5].push({
              lng: item.value[0],
              lat: item.value[1],
              count: item.value[2]
            })
          } else if (item.value[2] >= this.pinNum[6] && item.value[2] <= this.pinNum[7]) {
            data[6].push({
              lng: item.value[0],
              lat: item.value[1],
              count: item.value[2]
            })
          } else if (item.value[2] >= this.pinNum[7] && item.value[2] <= this.pinNum[8]) {
            data[7].push({
              lng: item.value[0],
              lat: item.value[1],
              count: item.value[2]
            })
          }
          //  else if (item.value[2] >= this.pinNum[8] && item.value[2] <= this.pinNum[9]) {
          //   data[8].push({
          //     lng: item.value[0],
          //     lat: item.value[1],
          //     count: item.value[2]
          //   })
          // } else {
          //   data[9].push({
          //     lng: item.value[0],
          //     lat: item.value[1],
          //     count: item.value[2]
          //   })
          // }
        })
        // console.log(data[0]);
        let pointCollection = new BMap.PointCollection(data[0], { color: 'rgb(80, 163, 186)' })
        let pointCollection1 = new BMap.PointCollection(data[1], { color: 'rgb(124, 173, 148)' });
        let pointCollection2 = new BMap.PointCollection(data[2], { color: 'rgb(102,255,3)' });
        let pointCollection3 = new BMap.PointCollection(data[3], { color: 'rgb(212, 194, 73)' });
        let pointCollection4 = new BMap.PointCollection(data[4], { color: 'rgb(232, 182, 60)' });
        let pointCollection5 = new BMap.PointCollection(data[5], { color: 'rgb(227, 147, 71)' });
        let pointCollection6 = new BMap.PointCollection(data[6], { color: 'rgb(222, 113, 82)' });
        let pointCollection7 = new BMap.PointCollection(data[7], { color: 'rgb(237, 45, 45)' });
        // let pointCollection8 = new BMap.PointCollection(data[8], { color: 'rgb(217, 78, 93)' });
        // let pointCollection9 = new BMap.PointCollection(data[9], { color: 'rgb(176, 58, 91)' });


        this.map.addOverlay(pointCollection);
        this.map.addOverlay(pointCollection1);
        this.map.addOverlay(pointCollection2);
        this.map.addOverlay(pointCollection3);
        this.map.addOverlay(pointCollection4);
        this.map.addOverlay(pointCollection5);
        this.map.addOverlay(pointCollection6);
        this.map.addOverlay(pointCollection7);
        // this.map.addOverlay(pointCollection8);
        // this.map.addOverlay(pointCollection9);
      } else {
        let num = this.mapPoint.map(item => item.value[2])
        // console.log(num);
        let max = Math.max(...num)
        let min = Math.min(...num)
        num.splice(num.indexOf(max), 1)
        num.splice(num.indexOf(min), 1)
        max = Math.max(...num)
        min = Math.min(...num)

        // console.log(max, min);
        let pin = (max - min) / 10
        // console.log(pin, 'pin');
        this.pinNum = []
        for (let i = min; i < max + 1; i += pin) {
          // console.log(Math.floor(i), 'i');
          this.pinNum.push(Math.floor(i))
        }
        // console.log(this.pinNum.length);
        let data = [[], [], [], [], [], [], [], [], [], []]
        this.mapPoint.map(item => {
          if (item.value[2] < this.pinNum[1]) {
            data[0].push({
              lng: item.value[0],
              lat: item.value[1],
              count: item.value[2]
            })
          } else if (item.value[2] >= this.pinNum[1] && item.value[2] <= this.pinNum[2]) {
            data[1].push({
              lng: item.value[0],
              lat: item.value[1],
              count: item.value[2]
            })
          } else if (item.value[2] >= this.pinNum[2] && item.value[2] <= this.pinNum[3]) {
            data[2].push({
              lng: item.value[0],
              lat: item.value[1],
              count: item.value[2]
            })
          } else if (item.value[2] >= this.pinNum[3] && item.value[2] <= this.pinNum[4]) {
            data[3].push({
              lng: item.value[0],
              lat: item.value[1],
              count: item.value[2]
            })
          } else if (item.value[2] >= this.pinNum[4] && item.value[2] <= this.pinNum[5]) {
            data[4].push({
              lng: item.value[0],
              lat: item.value[1],
              count: item.value[2]
            })
          } else if (item.value[2] >= this.pinNum[5] && item.value[2] <= this.pinNum[6]) {
            data[5].push({
              lng: item.value[0],
              lat: item.value[1],
              count: item.value[2]
            })
          } else if (item.value[2] >= this.pinNum[6] && item.value[2] <= this.pinNum[7]) {
            data[6].push({
              lng: item.value[0],
              lat: item.value[1],
              count: item.value[2]
            })
          } else if (item.value[2] >= this.pinNum[7] && item.value[2] <= this.pinNum[8]) {
            data[7].push({
              lng: item.value[0],
              lat: item.value[1],
              count: item.value[2]
            })
          } else if (item.value[2] >= this.pinNum[8] && item.value[2] <= this.pinNum[9]) {
            data[8].push({
              lng: item.value[0],
              lat: item.value[1],
              count: item.value[2]
            })
          } else {
            data[9].push({
              lng: item.value[0],
              lat: item.value[1],
              count: item.value[2]
            })
          }
        })
        // console.log(data);
        let pointCollection = new BMap.PointCollection(data[0], { color: 'rgb(80, 163, 186)' })
        let pointCollection1 = new BMap.PointCollection(data[1], { color: 'rgb(124, 173, 148)' });
        let pointCollection2 = new BMap.PointCollection(data[2], { color: 'rgb(102,255,3)' });
        let pointCollection3 = new BMap.PointCollection(data[3], { color: 'rgb(212, 194, 73)' });
        let pointCollection4 = new BMap.PointCollection(data[4], { color: 'rgb(232, 182, 60)' });
        let pointCollection5 = new BMap.PointCollection(data[5], { color: 'rgb(227, 147, 71)' });
        let pointCollection6 = new BMap.PointCollection(data[6], { color: 'rgb(222, 113, 82)' });
        let pointCollection7 = new BMap.PointCollection(data[7], { color: 'rgb(237, 45, 45)' });
        let pointCollection8 = new BMap.PointCollection(data[8], { color: 'rgb(217, 78, 93)' });
        let pointCollection9 = new BMap.PointCollection(data[9], { color: 'rgb(176, 58, 91)' });


        this.map.addOverlay(pointCollection);
        this.map.addOverlay(pointCollection1);
        this.map.addOverlay(pointCollection2);
        this.map.addOverlay(pointCollection3);
        this.map.addOverlay(pointCollection4);
        this.map.addOverlay(pointCollection5);
        this.map.addOverlay(pointCollection6);
        this.map.addOverlay(pointCollection7);
        this.map.addOverlay(pointCollection8);
        this.map.addOverlay(pointCollection9);
      }

      this.loading = false
    },

    administrationRegion(map, mapData) {
      // 为地图添加行政区域（红色区域）
      var that = this
      var bdary = new BMap.Boundary()
      // console.log(bdary);
      // bdary.setZIndex(10)
      for (let i = 0; i < that.siteInfo.length; i++) {
        var city = that.siteInfo[i].city
        var name = ''
        if (city === 'zq') {
          name = '肇庆'
        } else if (city === 'sz') {
          name = '深圳'
        } else if (city === 'qyz') {
          name = '吉安'
        } else if (city === 'htf') {
          name = '怀化'
        } else if (city === 'jff') {
          name = '东方'
        }

        bdary.get(name, function (rs) {
          // 获取行政区域
          // map.clearOverlays() // 清除地图覆盖物,会清除上一次描绘的点
          var count = rs.boundaries.length // 行政区域的点有多少个
          if (count === 0) {
            alert('未能获取当前输入行政区域')
            return
          }
          var pointArray = []
          for (var j = 0; j < count; j++) {
            var ply = new BMap.Polyline(rs.boundaries[j], {
              strokeWeight: 5,
              strokeColor: '#ff0000'
            }) // 建立多边形覆盖物
            map.addOverlay(ply) // 添加覆盖物
          }
          var point = new BMap.Point(that.siteInfo[i].longitude, that.siteInfo[i].latitude)
          var marker = new BMap.Marker(point)
          map.addOverlay(marker)
          marker.addEventListener('click', function () {
            vueEvent.$emit('jumpWeb', that.siteInfo[i])
          })
          // setTimeout(() => {
          //   that.loading = false
          // }, 1000)
        })
      }

      // for (let i = 0; i < that.siteInfo.length; i++) {
      //   var point = new BMap.Point(that.siteInfo[i].longitude, that.siteInfo[i].latitude)
      //   var marker = new BMap.Marker(point)
      //   map.addOverlay(marker)
      //   marker.addEventListener('click', function() {
      //     vueEvent.$emit('jumpWeb', that.siteInfo[i])
      //   })
      // }
    }
    // treeDataSpell(res) {
    //   this.mapData.Gpp = Number(res.data[res.data.length - 1].gpp).toFixed(3)
    //   this.mapData.Npp = Number(res.data[res.data.length - 1].npp).toFixed(3)
    //   this.mapData.Nep = Number(res.data[res.data.length - 1].nep).toFixed(3)
    //   this.mapData.Cveg = Number(res.data[res.data.length - 1].cveg).toFixed(
    //     3
    //   )
    //   this.mapData.Csom = Number(res.data[res.data.length - 1].csom).toFixed(
    //     3
    //   )
    //   this.administrationRegion(this.map, this.mapData)
    // }
  }
}
</script>

<style lang="scss" scoped>
.allmap {
  height: 964px;
  overflow: hidden;
  margin: 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  font-family: "微软雅黑";
  position: relative;
}

/deep/svg {
  z-index: 800;
}

// 自定义城市选择器区域
/deep/.BMap_CityListCtrl {
  .ui_city_change_top {
    display: flex;
    align-items: center;

    .ui_city_change_inner {
      display: flex;
      justify-content: space-around;
      height: 30px;
      line-height: 30px;
      align-items: center;
      width: 100px;

      i {
        top: 0;
        left: 0;
      }
    }

    .ui_city_change_inner::before {
      display: inline-block;
      // content: url(~@/assets/Workflow/icon-positioning-1.png);
      content: "";
      width: 20px;
      height: 20px;
      background: url(~@/assets/Workflow/icon-positioning-1.png);
      background-size: 20px 20px;
    }
  }
}

/deep/.citylist_popup_main {
  .city_content_top {
    padding-top: 7px;

    #city_ctrl_form {
      top: 5px;
    }
  }
}


/deep/.el-range-separator {
  width: 7%;
}


.select {
  position: absolute;
  right: 20px;
  top: 40px;
  z-index: 999;
  box-shadow: 0px 3px 5px rgba(0, 0, 0, 0.25);
}

.text {
  position: absolute;
  right: 20px;
  bottom: 40px;
  z-index: 999;
  box-shadow: 0px 3px 5px rgba(0, 0, 0, 0.25);
}


.Day {
  position: absolute;
  left: 20px;
  bottom: 90px;
  z-index: 999;
  box-shadow: 0px 3px 5px rgba(0, 0, 0, 0.25);
}

.DateSwitch {
  position: absolute;
  left: 20px;
  bottom: 40px;
  z-index: 999;
  box-shadow: 0px 3px 5px rgba(0, 0, 0, 0.25);
}

.charts {
  position: absolute;
  left: 20px;
  bottom: 150px;
  // width: 400px;
  background-color: #fff;
  padding: 1rem;
  border-radius: 5px;
  box-shadow: 0px 3px 5px rgba(0, 0, 0, 0.25);


  .chart {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 0.5rem;
    padding: 0 0.5rem;

    .i {
      // background-color: #d94e5d;
      width: 50px;
      height: 20px;
      border-radius: 5px;
      // margin-right: 1rem;
      margin-left: 1rem;
    }
  }
}

/deep/.el-loading-mask {
  z-index: 1990
}
</style>
