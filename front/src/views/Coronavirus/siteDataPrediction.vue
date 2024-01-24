<template>
  <div v-loading="loading" style="margin:auto;padding-left:20px;padding-right:20px;"
    element-loading-background="rgba(0, 0, 0, 0.8)" element-loading-text="数据正在加载中"
    element-loading-spinner="el-icon-loading">
    <el-row :gutter="20">
      <el-col :span="3" class="menuClass">
        <el-menu default-active="1" class="el-menu-class" @open="handleOpen" @close="handleClose">
          <el-submenu v-for="(item, index) in $t('menuData')" v-if="index < 2 ? true : false" :key="item.index"
            :index="item.id + ''">
            <template slot="title">
              <i :class="[index < 1 ? 'el-icon-s-data' : 'el-icon-cloudy']" style="margin-right:0px" />
              <span style="font-weight:600;font-size: 14px; color: #368C7C; line-height: 22px;">{{ item.name }}</span>
            </template>
            <el-menu-item-group v-for="(opt, index2) in item.childList" :key="index2">
              <el-menu-item :index="item.id + '' + opt.id" class="menuitem_Class"
                style="min-width:0px;height:40px;padding:0px;text-align:center;font-weight:600"
                @click="Jumpmethod(opt.id)">{{ opt.name }}</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-menu-item v-for="(item, index) in $t('menuData')" v-if="index < 2 ? false : true" :key="index"
            :index="index = 2 ? index + '' : ''" style="padding-left:16px;" @click="jumpSite2('treeDiagram1')">
            <span slot="title" style="font-weight:600;font-size: 14px;  line-height: 22px;">{{ item.name }}</span>
          </el-menu-item>
        </el-menu>&nbsp
      </el-col>
      <el-col :span="21">
        <el-row :gutter="24" style="margin-top:104px">
          <el-col :span="12" style="margin-top:24px">
            <p id="nepMyCharts1" style="  position: relative; height:0px;top: -64px;overflow: hidden;margin:0px" />
            <div style="box-shadow: 2px 0px 6px 0px rgba(0,21,41,0.12);">
              <p class="titleP">{{ $t('leftNav.NetEcosystem') }}(NEP){{ $t('leftNav.prediction') }}</p>
              <line-chart id="nepMyCharts" :options="nep" style="width:100%" />
            </div>
          </el-col>
          <el-col :span="12" style="margin-top:24px">
            <p id="gppMyCharts1" style="  position: relative; height:0px;top: -64px;overflow: hidden;margin:0px" />
            <div style="box-shadow: 2px 0px 6px 0px rgba(0,21,41,0.12);">
              <p class="titleP">{{ $t('leftNav.GrossPrimary') }}(GPP){{ $t('leftNav.prediction') }}</p>
              <line-chart id="gppMyCharts" :options="gpp" style="width:100%" />
            </div>
          </el-col>
          <el-col :span="12" style="margin-top:24px">
            <p id="nppMyCharts1" style="  position: relative; height:0px;top: -64px;overflow: hidden;margin:0px" />
            <div style="box-shadow: 2px 0px 6px 0px rgba(0,21,41,0.12);">
              <p class="titleP">{{ $t('leftNav.NetPrimary') }}(NPP){{ $t('leftNav.prediction') }}</p>
              <line-chart id="nppMyCharts" :options="npp" style="width:100%" />
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <!-- <p style="font-size:24px;font-family: Microsoft YaHei;margin-bottom:8px;margin-top:56px">驱动数据预测</p> -->
          <p style="margin-top:24px" />
          <el-col :span="12" style="margin-top:24px">
            <p id="taMyCharts1" style="  position: relative; height:0px;top: -64px;overflow: hidden;margin:0px" />
            <div style="box-shadow: 2px 0px 6px 0px rgba(0,21,41,0.12);">
              <p class="titleP">{{ $t('leftNav.DailyAverage') }}(Ta){{ $t('leftNav.prediction') }}</p>
              <line-chart id="taMyCharts" :options="ta" style="width:100%" />
            </div>
          </el-col>
          <el-col :span="12">
            <p id="parMyCharts1"
              style="  position: relative; height:0px;top: -64px;overflow: hidden;margin:0px;margin-top:24px" />
            <div style="box-shadow: 2px 0px 6px 0px rgba(0,21,41,0.12);">
              <p class="titleP">{{ $t('leftNav.Photosynthetically') }}(PAR){{ $t('leftNav.prediction') }} </p>
              <line-chart id="parMyCharts" :options="par" style="width:100%;" />
            </div>
          </el-col>
          <el-col :span="12">
            <p id="rhMyCharts1"
              style="  position: relative; height:0px;top: -64px;overflow: hidden;margin:0px;margin-top:24px" />
            <div style="box-shadow: 2px 0px 6px 0px rgba(0,21,41,0.12);">
              <p class="titleP">{{ $t('leftNav.Relative') }}(Rh){{ $t('leftNav.prediction') }}</p>
              <line-chart id="rhMyCharts" :options="rh" style="width:100%" />
            </div>
          </el-col>
          <el-col :span="12" style="margin-top:24px">
            <p id="vpdMyCharts1" style="  position: relative; height:0px;top: -64px;overflow: hidden;margin:0px" />
            <div style="box-shadow: 2px 0px 6px 0px rgba(0,21,41,0.12);">
              <p class="titleP">{{ $t('leftNav.Vapor') }}(VPD){{ $t('leftNav.prediction') }}</p>
              <line-chart id="vpdMyCharts" :options="vpd" style="width:100%" />
            </div>
          </el-col>
          <el-col :span="12" style="margin-top:24px">
            <p id="swcMyCharts1" style="  position: relative; height:0px;top: -64px;overflow: hidden;margin:0px" />
            <div style="box-shadow: 2px 0px 6px 0px rgba(0,21,41,0.12);">
              <p class="titleP">{{ $t('leftNav.SoilMoisture') }}(SWC){{ $t('leftNav.prediction') }}</p>
              <line-chart id="swcMyCharts" :options="swc" style="width:100%" />
            </div>
          </el-col>
        </el-row>

        <el-row v-if="language == 'zh'">
          <el-col id="elcol_id" :span="24" style="margin-top:24px">
            <div style="box-shadow: 2px 0px 6px 0px rgba(0,21,41,0.12);">
              <p id="treeDiagram1" style="  position: relative; height:0px;top: -64px;overflow: hidden;margin:0px" />
              <p class="titleP">{{ $t('leftNav.cycle') }}</p>
              <el-row style="width:975px;height:600px;margin:auto">
                <el-col id="treeDiagram" style="width:100%;height:100%" class="background">
                  <div style="margin-top:10px;text-align:center">
                    <span v-for="(item, index) in fileData" :key="index"
                      :class="[treeDateNum === index ? 'treeDateScrolling' : 'treeDate']">{{
                        item.year + "-" + item.month + "-" + item.day }}
                    </span>
                  </div>
                  <ul style="color: #3A8A23;position:absolute;left:550px;top:100px;font-size:16px;opacity:1;">
                    <li style="float:left;list-style:none">↓ {{ $t('tree.GPP') }}(GPP):</li>
                    <li style="float:left;list-style:none">{{ treeData.gpp }} g C m⁻² day⁻¹ </li>
                  </ul>
                  <ul style="color: #3A8A23;position:absolute;top:90px;left:60px;font-size:16px;opacity:1">
                    <li style="float:left;list-style:none">↓ {{ $t('tree.NPP') }}(NPP):</li>
                    <li style="float:left;list-style:none">{{ treeData.npp }} g C m⁻² day⁻¹ </li>
                  </ul>
                  <ul style="color: #3A8A23;position:absolute;left:300px;top:30px;font-size:16px;opacity:1">
                    <li style="float:left;list-style:none">{{ $t('tree.NEP') }}(NEP):</li>
                    <li style="float:left;list-style:none">{{ treeData.nep }} g C m⁻² day⁻¹</li>
                  </ul>
                  <ul style="color: #5470C6;position:absolute;left:580px;top:200px;font-size:16px;opacity:1">
                    <li style="float:left;list-style:none">{{ $t('tree.Cveg') }}(Cveg):</li>
                    <li style="float:left;list-style:none">{{ treeData.cveg }} g C m⁻²</li>
                  </ul>
                  <ul style="color: #5470C6;position:absolute;font-size:16px;left:580px;top:475px;opacity:1">
                    <li style="float:left;list-style:none">{{ $t('tree.Csom') }}(Csom):</li>
                    <li style="float:left;list-style:none">{{ treeData.csom }} g C m⁻²</li>
                  </ul>
                  <ul style="color: #3A8A23;position:absolute;left:550px;top:150px;font-size:16px;opacity:1">
                    <li style="float:left;list-style:none">↑ {{ $t('tree.Ra') }}(Ra):</li>
                    <li style="float:left;list-style:none">{{ treeData.ra }} g C m⁻² day⁻¹ </li>
                  </ul>
                  <ul style="color: #3A8A23;position:absolute;font-size:16px;left:570px;top:450px;opacity:1">
                    <li style="float:left;list-style:none">↑ {{ $t('tree.Rh') }}(Rh):</li>
                    <li style="float:left;list-style:none">{{ treeData.rh }} g C m⁻² day⁻¹ </li>
                  </ul>
                  <ul style="color: #5470C6;position:absolute;top:200px;left:90px;font-size:16px;opacity:1">
                    <li style="float:left;list-style:none">{{ $t('tree.Cf') }}(Cf):</li>
                    <li style="float:left;list-style:none">{{ treeData.cf }} g C m⁻²</li>
                  </ul>
                  <ul style="color: #5470C6;position:absolute;top:420px;left:100px;font-size:16px;opacity:1">
                    <li style="float:left;list-style:none">{{ $t('tree.Cw') }}(Cw):</li>
                    <li style="float:left;list-style:none">{{ treeData.cw }} g C m⁻²</li>
                  </ul>
                  <ul style="color: #5470C6;position:absolute; top:480px;;left:100px;font-size:16px;opacity:1">
                    <li style="float:left;list-style:none">{{ $t('tree.Cr') }}(Cr):</li>
                    <li style="float:left;list-style:none">{{ treeData.cr }} g C m⁻²</li>
                  </ul>
                  <ul style="color: #F4606C;position:absolute; top:150px;left:100px;font-size:16px;opacity:1">
                    <li style="float:left;list-style:none">{{ $t('tree.Af') }}(Af):</li>
                    <li style="float:left;list-style:none">{{ treeData.af }} g C m⁻² day⁻¹</li>
                  </ul>
                  <ul style="color: #F4606C;position:absolute;top:390px;left:165px;font-size:16px;opacity:1">
                    <li style="float:left;list-style:none">{{ $t('tree.Aw') }}(Aw):</li>
                    <li style="float:left;list-style:none">{{ treeData.aw }} g C m⁻² day⁻¹</li>
                  </ul>
                  <ul style="color: #F4606C;position:absolute;top:450px;left:80px;font-size:16px;opacity:1">
                    <li style="float:left;list-style:none">{{ $t('tree.Ar') }}(Ar):</li>
                    <li style="float:left;list-style:none">{{ treeData.ar }} g C m⁻² day⁻¹</li>
                  </ul>
                  <ul style="color: #F4606C;position:absolute;left:610px;top:250px;font-size:16px;opacity:1">
                    <li style="float:left;list-style:none">{{ $t('tree.τveg') }}(τveg):</li>
                    <li style="float:left;list-style:none">{{ treeData.tveg }} yr</li>
                  </ul>
                  <ul style="color: #F4606C;position:absolute;font-size:16px;left:590px;top:500px;opacity:1">
                    <li style="float:left;list-style:none;">{{ $t('tree.τsoil') }}(τsoil):</li>
                    <li style="float:left;list-style:none">{{ treeData.tsoil }} yr</li>
                  </ul>
                  <hr class="hr_Class" />
                  <!-- </hr> -->
                </el-col>
              </el-row>
            </div>
          </el-col>
        </el-row>
        <el-row v-else>
          <el-col id="elcol_id" :span="24" style="margin-top:24px">
            <div style="box-shadow: 2px 0px 6px 0px rgba(0,21,41,0.12);">
              <p id="treeDiagram1" style="  position: relative; height:0px;top: -64px;overflow: hidden;margin:0px" />
              <p class="titleP">{{ $t('leftNav.cycle') }}</p>
              <el-row style="width:975px;height:600px;margin:auto">
                <el-col id="treeDiagram" style="width:100%;height:100%" class="background">
                  <div style="margin-top:10px;text-align:center">
                    <span v-for="(item, index) in fileData" :key="index"
                      :class="[treeDateNum === index ? 'treeDateScrolling' : 'treeDate']">{{
                        item.year + "-" + item.month + "-" + item.day }}
                    </span>
                  </div>
                  <ul style="color: #3A8A23;position:absolute;left:550px;top:100px;font-size:16px;opacity:1;">
                    <li style="float:left;list-style:none">↓ {{ $t('tree.GPP') }}(GPP):</li>
                    <li style="float:left;list-style:none">{{ treeData.gpp }} g C m⁻² day⁻¹ </li>
                  </ul>
                  <ul style="color: #3A8A23;position:absolute;top:90px;left:27px;font-size:16px;opacity:1">
                    <li style="float:left;list-style:none">↓ {{ $t('tree.NPP') }}(NPP):</li>
                    <li style="float:left;list-style:none">{{ treeData.npp }} g C m⁻² day⁻¹ </li>
                  </ul>
                  <ul style="color: #3A8A23;position:absolute;left:300px;top:30px;font-size:16px;opacity:1">
                    <li style="float:left;list-style:none">{{ $t('tree.NEP') }}(NEP):</li>
                    <li style="float:left;list-style:none">{{ treeData.nep }} g C m⁻² day⁻¹</li>
                  </ul>
                  <ul style="color: #5470C6;position:absolute;left:610px;top:200px;font-size:16px;opacity:1">
                    <li style="float:left;list-style:none">{{ $t('tree.Cveg') }}(Cveg):</li>
                    <li style="float:left;list-style:none">{{ treeData.cveg }} g C m⁻²</li>
                  </ul>
                  <ul style="color: #5470C6;position:absolute;font-size:16px;left:580px;top:475px;opacity:1">
                    <li style="float:left;list-style:none">{{ $t('tree.Csom') }}(Csom):</li>
                    <li style="float:left;list-style:none">{{ treeData.csom }} g C m⁻²</li>
                  </ul>
                  <ul style="color: #3A8A23;position:absolute;left:550px;top:150px;font-size:16px;opacity:1">
                    <li style="float:left;list-style:none">↑ {{ $t('tree.Ra') }}(Ra):</li>
                    <li style="float:left;list-style:none">{{ treeData.ra }} g C m⁻² day⁻¹ </li>
                  </ul>
                  <ul style="color: #3A8A23;position:absolute;font-size:16px;left:570px;top:450px;opacity:1">
                    <li style="float:left;list-style:none">↑ {{ $t('tree.Rh') }}(Rh):</li>
                    <li style="float:left;list-style:none">{{ treeData.rh }} g C m⁻² day⁻¹ </li>
                  </ul>
                  <ul style="color: #5470C6;position:absolute;top:200px;left:34px;font-size:16px;opacity:1">
                    <li style="float:left;list-style:none">{{ $t('tree.Cf') }}(Cf):</li>
                    <li style="float:left;list-style:none">{{ treeData.cf }} g C m⁻²</li>
                  </ul>
                  <ul style="color: #5470C6;position:absolute;top:420px;left:100px;font-size:16px;opacity:1">
                    <li style="float:left;list-style:none">{{ $t('tree.Cw') }}(Cw):</li>
                    <li style="float:left;list-style:none">{{ treeData.cw }} g C m⁻²</li>
                  </ul>
                  <ul style="color: #5470C6;position:absolute; top:480px;;left:40px;font-size:16px;opacity:1">
                    <li style="float:left;list-style:none">{{ $t('tree.Cr') }}(Cr):</li>
                    <li style="float:left;list-style:none">{{ treeData.cr }} g C m⁻²</li>
                  </ul>
                  <ul style="color: #F4606C;position:absolute; top:150px;left:23px;font-size:16px;opacity:1">
                    <li style="float:left;list-style:none">{{ $t('tree.Af') }}(Af):</li>
                    <li style="float:left;list-style:none">{{ treeData.af }} g C m⁻² day⁻¹</li>
                  </ul>
                  <ul style="color: #F4606C;position:absolute;top:390px;left:47px;font-size:16px;opacity:1">
                    <li style="float:left;list-style:none">{{ $t('tree.Aw') }}(Aw):</li>
                    <li style="float:left;list-style:none">{{ treeData.aw }} g C m⁻² day⁻¹</li>
                  </ul>
                  <ul style="color: #F4606C;position:absolute;top:450px;left:10px;font-size:16px;opacity:1">
                    <li style="float:left;list-style:none">{{ $t('tree.Ar') }}(Ar):</li>
                    <li style="float:left;list-style:none">{{ treeData.ar }} g C m⁻² day⁻¹</li>
                  </ul>
                  <ul style="color: #F4606C;position:absolute;left:610px;top:250px;font-size:16px;opacity:1">
                    <li style="float:left;list-style:none">{{ $t('tree.τveg') }}(τveg):</li>
                    <li style="float:left;list-style:none">{{ treeData.tveg }} yr</li>
                  </ul>
                  <ul style="color: #F4606C;position:absolute;font-size:16px;left:590px;top:500px;opacity:1">
                    <li style="float:left;list-style:none;">{{ $t('tree.τsoil') }}(τsoil):</li>
                    <li style="float:left;list-style:none">{{ treeData.tsoil }} yr</li>
                  </ul>
                  <hr class="hr_Class" />
                  <!-- </hr> -->
                </el-col>
              </el-row>
            </div>
          </el-col>
        </el-row>

        <el-row>
          <el-col>
            <p id="allmap1" style="  position: relative; height:0px;top: -64px;overflow: hidden;margin:0px" />
            <div id="allmap" class="allmap" />
            <div ref="map" class="map" />
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Cookies from 'js-cookie'
import vueEvent from './mock/vueEvent'
import { forwardResults, getAllData, getDriverData, errorbar, allstation, ecologyerrorbar, getEcologyData } from '@/api/login'
import decimalPoint from './mock/func'
import { lineDataSpell, DriverDataSpell, addErrorbar, DeleteSwcChart, addErrorbar2 } from './mock/func'
import ECharts from 'vue-echarts/components/ECharts'
import * as echarts from 'echarts'
import 'echarts/lib/chart/bar'
import 'echarts/lib/chart/line'
import 'echarts/lib/component/dataZoom'
import 'echarts/lib/component/legend'
import 'echarts/lib/component/tooltip'
import 'echarts/lib/component/visualMap'
import 'echarts/lib/chart/pie'
import 'echarts/lib/chart/gauge'
export default {
  name: 'MyHead',
  components: {
    'line-chart': ECharts
  },
  computed: {
    language() {
      return this.$store.getters.language
    }
  },
  data() {
    return {
      siteInfo: [],
      siteCode: '',
      ratio: 0,
      menuWidth: 0,
      treeRotation: undefined,
      fileData: [],
      treeDateNum: 0,
      treeDate: [],
      mapData: {
        Gpp: 0,
        Npp: 0,
        Nep: 0,
        Cveg: 0,
        Csom: 0
      },
      loading: true,
      totalData: {},
      treeData: {},
      gpp: {},
      npp: {},
      nep: {},
      cveg: {},
      csom: {},
      par: {},
      rh: {},
      swc: {},
      ta: {},
      vpd: {},
      errorbarData: [],
      alldata: [],
      ecologyerrorbarData: [],
      menuData: [
        {
          name: '生态数据预测',
          id: 1,
          childList: [
            { name: '净生态系统生产力', id: 'nepMyCharts1' },
            { name: '总初级生产力', id: 'gppMyCharts1' },
            { name: '净初级生产力', id: 'nppMyCharts1' }
          ]
        },
        {
          name: '气象数据预测',
          id: 2,
          childList: [
            { name: '日均气温', id: 'taMyCharts1' },
            { name: '光合有效辐射', id: 'parMyCharts1' },
            { name: '相对湿度', id: 'rhMyCharts1' },
            { name: '饱和水汽压差', id: 'vpdMyCharts1' },
            { name: '土壤含水量', id: 'swcMyCharts1' }
          ]
        },
        {
          name: '碳循环全组分预测',
          id: 3,
          site: 'allmap1'
        }
      ],
      bar: {
        title: {
          text: '',
          padding: [28, 0, 0, 230]
          // x:'center', //水平安放位置，默认为'left'，可选为：'center' | 'left' | 'right' | {number}（x坐标，单位px）
          // y: 23, //垂直安放位置，默认为top，可选为：'top' | 'bottom' | 'center' | {number}（y坐标，单位px）
        },
        tooltip: {
          trigger: 'axis',
          formatter: '{b0} : {c0}'
        },
        grid: {
          left: '8%',
          right: '3%',
          bottom: '3%',
          top: '10%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,

          axisLine: {
            lineStyle: {
              width: 2
            }
          },
          axisLabel: {
            inside: false,
            interval: 6,
            rotate: 35,
            textStyle: {
              color: ' rgba(0,0,0,0.65)',
              fontSize: 14,
              padding: [10, 0, 0, 0],
              fontWeight: 500,
              fontFamily: 'Helvetica Neue'
            }
          },
          data: [
          ]
        },
        yAxis: [
          {
            name: '碳通量(g Cm⁻²d⁻¹)',
            type: 'value',
            splitLine: {
              show: true,
              lineStyle: {
                type: 'dashed'
              }
            },
            axisLabel: {
              inside: false,
              textStyle: {
                color: ' rgba(0,0,0,0.65)',
                fontSize: 14,
                fontWeight: 500
              }
            },
            axisLine: {
              lineStyle: {
                width: 2
              }
            },
            nameTextStyle: {
              padding: [0, 0, 0, 0],
              color: 'rgba(0,0,0,0.65)',
              fontFamily: 'Microsoft YaHei',
              fontSize: 14,
              fontWeight: 500
            }
          }
        ],
        visualMap: [
          {
            // 区间内控制显示颜色
            show: false,
            dimension: 0,
            // 60-67这个区间的数值显示紫色（#9a60b4）
            pieces: [{ gte: 0, lte: 0, color: '#9a60b4' }],
            // 其他的区域显示蓝色#5470c6
            outOfRange: {
              color: '#5470c6'
            },
            seriesIndex: 0
          }
        ],
        series: [
          {
            name: '实际值',
            type: 'line',
            color: '#5470c6',
            showSymbol: false,
            areaStyle: {
              // 折线图渐变色
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  // 0，0，0，1从右下左上的其中一个方向开始渐变
                  offset: 0,
                  color: 'rgba(255, 255, 255,0)'
                },
                {
                  offset: 1,
                  color: 'rgba(255, 255, 255,0)'
                }
              ])
            },
            markArea: {
              itemStyle: {
                color: 'rgba(84, 112, 198, 0.2)'
              },
              data: [
                [
                  {
                    xAxis: '20121225'
                  },
                  {
                    xAxis: '20121231'
                  }
                ]
              ]
            },
            data: [
            ]
          },
          {
            name: '预测值',
            type: 'line',
            color: '#9A60B4',
            showSymbol: false,
            areaStyle: {
              // 折线图渐变色
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  // 0，0，0，1从右下左上的其中一个方向开始渐变
                  offset: 0,
                  color: 'rgba(255, 255, 255,0)'
                },
                {
                  offset: 1,
                  color: 'rgba(255, 255, 255,0)'
                }
              ])
            },
            markArea: {
              itemStyle: {
                color: 'rgba(84, 112, 198, 0.2)'
              },
              data: [
                [
                  {
                    xAxis: '20121225'
                  },
                  {
                    xAxis: '20121231'
                  }
                ]
              ]
            },
            data: [
            ]
          }
        ]
      }
    }
  },
  watch: {
    '$route'(to, from) { // 切換站点的时候监听CODE参数，如果CODE参数改变了就把页面数据清空，重新加载新站点的数据
      if (this.$route.query.code) {
        this.siteCode = this.$route.query.code
        this.loading = true
        this.par = {}
        this.rh = {}
        this.swc = {}
        this.ta = {}
        this.vpd = {}
        this.gpp = {}
        this.npp = {}
        this.nep = {}
        this.cveg = {}
        this.csom = {}
        this.fileData = []
        this.treeData = {}
        this.initMap()// 地图初始化
        this.allstation()// 获取多站点数据
        this.errorbar()// 获取误差棒与预测数据

        // 生态数据
        // this.ecologyerrorbar()
      }
    },
    language: {
      handler(newval, oldval) {
        if (newval == 'en') {
          this.bar.series[0].name = 'actualValue'
          this.bar.series[1].name = 'predictiveValue'
        } else {
          this.bar.series[0].name = '实际值'
          this.bar.series[1].name = '预测值'
        }
        this.getDriverData()
        // this.getEcologyData()
        this.ecologyerrorbar()
      },
      // immediate: true,
    },
  },
  created() {
    if (this.language == 'en') {
      this.bar.series[0].name = 'actualValue'
      this.bar.series[1].name = 'predictiveValue'
    } else {
      this.bar.series[0].name = '实际值'
      this.bar.series[1].name = '预测值'
    }
  },
  mounted() {
    if (this.$route.query.code) {
      this.siteCode = this.$route.query.code
    } else {
      this.$router.push('/siteMap')
      return
    }

    this.initMap()// 地图初始化
    this.allstation()// 获取多站点数据
    this.errorbar()// 获取误差棒与预测数据
    // if (this.siteCode == 'DHF') {
    this.ecologyerrorbar()//获取生态数据误差棒
    // }

    vueEvent.$on('jumpWeb', (data) => {
      this.jumpSite(data)
    })
    vueEvent.$on('scroll', data => {
      this.scroll(data)
    })
    this.$nextTick(_ => {
      window.addEventListener('resize', this.echartsAdaptive, true)
    })
    this.$nextTick(() => {
      window.addEventListener('resize', this.menuAdaptive, true)
    })
    // this.$nextTick(() => {
    //   window.addEventListener('resize', this.dottedLine, true)
    // })
    $('.head').css({
      'position': 'fixed',
      'margin-left': '-17px'
    })
    this.menuWidth = $('.menuClass')[0].clientWidth - 20
    this.menuClass()
  },
  destroyed() {
    // 销毁监听
    window.removeEventListener('resize', this.echartsAdaptive, true)
    window.removeEventListener('resize', this.menuAdaptive, true)
  },
  methods: {
    errorbar() {
      errorbar(this.siteCode).then((res) => {
        this.errorbarData = res.data
        this.getDriverData()
        this.getAllData()
      })
    },
    // 生态数据误差棒
    ecologyerrorbar() {
      ecologyerrorbar(this.siteCode).then(res => {
        console.log(res.data);
        this.ecologyerrorbarData = res.data
        // console.log(res.data[0].date[0], res.data[0].date.pop());
        this.getEcologyData(res.data[0].date[0], res.data[0].date.pop())
      })
    },
    // dottedLine() {
    //   var a = ($('#elcol_id'))[0].clientWidth
    //   var b = (a - 975) / 2
    //   $('.hr_Class').css({
    //     width: a + 'px',
    //     'margin-left': '-' + b + 'px'
    //   })
    // },
    echartsAdaptive() {
      var myChart1 = echarts.init(document.getElementById('parMyCharts'))
      var myChart2 = echarts.init(document.getElementById('rhMyCharts'))
      var myChart3 = echarts.init(document.getElementById('swcMyCharts'))
      var myChart4 = echarts.init(document.getElementById('taMyCharts'))
      var myChart5 = echarts.init(document.getElementById('vpdMyCharts'))
      var myChart6 = echarts.init(document.getElementById('gppMyCharts'))
      var myChart7 = echarts.init(document.getElementById('nppMyCharts'))
      var myChart8 = echarts.init(document.getElementById('nepMyCharts'))
      myChart1.resize()
      myChart2.resize()
      myChart3.resize()
      myChart4.resize()
      myChart5.resize()
      myChart6.resize()
      myChart7.resize()
      myChart8.resize()
    },
    menuAdaptive() {
      this.ratio = this.ratioChange()
      this.menuWidth = $('.menuClass')[0].clientWidth - 20
      $('.el-menu-class').css({
        width: this.menuWidth + 'px'
      })
    },
    ratioChange() {
      var ratio = 0,
        screen = window.screen,
        ua = navigator.userAgent.toLowerCase()

      if (window.devicePixelRatio !== undefined) {
        ratio = window.devicePixelRatio
      } else if (~ua.indexOf('msie')) {
        if (screen.deviceXDPI && screen.logicalXDPI) {
          ratio = screen.deviceXDPI / screen.logicalXDPI
        }
      } else if (
        window.outerWidth !== undefined &&
        window.innerWidth !== undefined
      ) {
        ratio = window.outerWidth / window.innerWidth
      }

      if (ratio) {
        ratio = Math.round(ratio * 100)
      }
      return ratio
    },
    scroll(data) {
      if (data < 64) {
        $('.el-menu-class').css({
          position: 'static',
          'margin-top': '128px',
          width: '100%'
        })
      } else if (data > 64) {
        $('.el-menu-class').css({
          position: 'fixed',
          'margin-top': '64px',
          width: this.menuWidth + 'px'
        })
      }
    },
    getAllData() {
      getAllData(this.siteCode).then(res => {
        if (res.data.length === 0) {
          return
        } else {
          this.alldata = res.data
          // if (this.siteCode == 'DHF') {
          // this.EcologyData()
          // } else {
          //   this.dataSpell(res.data)
          // }
          this.treeDataSpell(res)
        }
      })
    },
    getDriverData() {
      getDriverData(this.siteCode).then(res => {
        if (res.data.length === 0) {
          this.$alert('没有查询到当前站点的预测数据', '警告提示', {    //GZ站没有数据，只有鼎湖山有数据
            confirmButtonText: '确定',
            callback: action => {
              this.loading = false
              this.$router.push('/siteMap')
              return
            }
          })
        } else {
          this.DriverData(res.data)
        }
      })
    },

    getEcologyData(start, end) {
      getEcologyData(this.siteCode, start, end).then(res => {
        this.EcologyData(res.data)
        if (res.data.length === 0) {
          // this.$alert('没有查询到当前站点的预测数据', '警告提示', {    //GZ站没有数据，只有鼎湖山有数据
          //   confirmButtonText: '确定',
          //   callback: action => {
          //     this.loading = false
          //     this.$router.push('/siteMap')
          //     return
          //   }
          // })
        } else {
        }
      })
    },
    initMap() {
      // 创建百度地图
      var map = new BMap.Map('allmap', { enableMapClick: false })
      this.map = map
      map.centerAndZoom(new BMap.Point(112.179863, 23.528986), 6)
      map.enableScrollWheelZoom(true)
      function MessageControl() {
        // 默认停靠位置和偏移量
        this.defaultAnchor = BMAP_ANCHOR_TOP_RIGHT
        this.defaultOffset = new BMap.Size(20, 20)
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
      map.addControl(messageCtrl)
      // this.administrationRegion(map, this.mapData)
      // 下面的代码可以封装
    },

    administrationRegion(map, mapData) {
      // 为地图添加行政区域（红色区域）
      var that = this
      var bdary = new BMap.Boundary()
      for (let i = 0; i < that.siteInfo.length; i++) {
        var city = that.siteInfo[i].city
        var name = ''
        if (city === 'zq') {   //客户方数据库无法返回中文暂时改为判断
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
          map.setViewport(pointArray) // 调整视野
          var point = new BMap.Point(that.siteInfo[i].longitude, that.siteInfo[i].latitude)
          var marker = new BMap.Marker(point)
          map.addOverlay(marker)
          marker.addEventListener('click', function () {
            vueEvent.$emit('jumpWeb', that.siteInfo[i])
          })
        })
      }
    },
    dataSpell(data) {
      // 实际预测数据
      this.gpp = lineDataSpell(data, this.bar, this.gpp, 'GPP')
      this.npp = lineDataSpell(data, this.bar, this.npp, 'NPP')
      this.nep = lineDataSpell(data, this.bar, this.nep, 'NEP')
      // this.cveg = lineDataSpell(data, this.bar, this.cveg, 'Cveg')
      // this.csom = lineDataSpell(data, this.bar, this.csom, 'Csom')

      // 准确值预写
      // addErrorbar(this.gpp, this.errorbarData, 'GPP')
      // addErrorbar(this.npp, this.errorbarData, 'NPP')
      // addErrorbar(this.nep, this.errorbarData, 'NEP')
      // addErrorbar(this.cveg, this.errorbarData, 'Cveg')
      // addErrorbar(this.csom, this.errorbarData, 'Csom')
    },
    allstation() {
      allstation().then((res) => {
        if (res.data.code === '10000') {
          this.siteInfo = res.data.data
          this.administrationRegion(this.map)
        }
      })
    },
    // mapDataSpell(res) {
    //   this.mapData.Gpp = Number(res.data[res.data.length - 1].gpp).toFixed(3)
    //   this.mapData.Npp = Number(res.data[res.data.length - 1].npp).toFixed(3)
    //   this.mapData.Nep = Number(res.data[res.data.length - 1].nep).toFixed(3)
    //   this.mapData.Cveg = Number(res.data[res.data.length - 1].cveg).toFixed(3)
    //   this.mapData.Csom = Number(res.data[res.data.length - 1].csom).toFixed(3)
    //   this.administrationRegion(this.map, this.mapData) // 先将所有数据保留三位小数点，然后添加至百度地图
    // },
    treeDataSpell(res) {
      // 树图数据转动
      this.fileData = []
      for (let i = res.data.length - 7; i < res.data.length; i++) {
        this.fileData.push(res.data[i])
      }
      this.treeData = decimalPoint(this.fileData[0])
      this.treeDateNum = 0
      this.loading = false
      this.treeRotation = setInterval(() => {
        if (this.treeDateNum < this.fileData.length - 1) {
          this.treeDateNum++
          this.treeData = decimalPoint(this.fileData[this.treeDateNum])
        } else if (this.treeDateNum === this.fileData.length - 1) {
          this.treeDateNum = 0
          this.treeData = decimalPoint(this.fileData[this.treeDateNum])
        }
      }, 3000)
    },
    DriverData(data) {
      let long = this.errorbarData[0].date.length - data.length

      this.par = DriverDataSpell(data, this.bar, this.par, 'par')
      this.rh = DriverDataSpell(data, this.bar, this.rh, 'rh')
      this.swc = DriverDataSpell(data, this.bar, this.swc, 'swc')
      this.ta = DriverDataSpell(data, this.bar, this.ta, 'ta')
      this.vpd = DriverDataSpell(data, this.bar, this.vpd, 'vpd')

      addErrorbar(this.par, this.errorbarData, 'Par', long)
      addErrorbar(this.rh, this.errorbarData, 'Rh', long)
      addErrorbar(this.swc, this.errorbarData, 'Swc', long)
      addErrorbar(this.ta, this.errorbarData, 'Ta', long)
      addErrorbar(this.vpd, this.errorbarData, 'Vpd', long)
    },

    EcologyData(data) {
      let long = this.ecologyerrorbarData[0].date.length - data.length
      // console.log(this.ecologyerrorbarData[0].date);
      // console.log(data);
      // 生态实际数据
      // this.gpp = lineDataSpell(data, this.bar, this.gpp, 'GPP')
      // this.npp = lineDataSpell(data, this.bar, this.npp, 'NPP')
      // this.nep = lineDataSpell(data, this.bar, this.nep, 'NEP')

      // 测试只有nep使用真实数据写法
      // this.gpp = lineDataSpell(this.alldata, this.bar, this.gpp, 'GPP', this.siteCode)
      // this.npp = lineDataSpell(this.alldata, this.bar, this.npp, 'NPP', this.siteCode)
      console.log(data, this.bar, this.gpp, 'GPP', this.siteCode);
      this.gpp = lineDataSpell(data, this.bar, this.gpp, 'GPP', this.siteCode)
      this.npp = lineDataSpell(data, this.bar, this.npp, 'NPP', this.siteCode)
      this.nep = lineDataSpell(data, this.bar, this.nep, 'NEP', this.siteCode, long)
      console.log(this.nep);
      // 后续可能需要改成此种写法(用于真实数据)
      // this.gpp = DriverDataSpell(data, this.bar, this.gpp, 'GPP')
      // this.npp = DriverDataSpell(data, this.bar, this.npp, 'NPP')
      // this.nep = DriverDataSpell(data, this.bar, this.nep, 'NEP')

      addErrorbar2(this.gpp, this.ecologyerrorbarData, 'Gpp')
      addErrorbar2(this.npp, this.ecologyerrorbarData, 'Npp')
      // addErrorbar(this.nep, this.ecologyerrorbarData, 'Nep', long)
      addErrorbar2(this.nep, this.ecologyerrorbarData, 'Nep', long)
    },

    jumpSite(data) {
      var isen = false;
      Cookies.get('language') == 'en' ? isen = true : isen = false;
      var name = ''
      if (data.title === 'dfs') {
        isen ? name = 'Dinghushan' : name = '鼎湖山'
      } else if (data.title === 'gz') {
        isen ? name = 'Guangzhou' : name = '广州'
      } else if (data.title === 'qyz') {
        isen ? name = 'Qianyanzhou' : name = '千烟洲'
      } else if (data.title === 'htf') {
        isen ? name = 'Huitong' : name = '会同'
      } else if (data.title === 'jff') {
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
      })
        .then(() => {
          vueEvent.$emit('changePlatform', name)
          if (data.code === this.siteCode) {
            this.$message({
              type: 'error',
              message: '请勿跳转相同站点',
              showClose: true
            })
          } else {
            this.$router.push({
              name: 'siteDataPrediction',
              query: {
                code: data.code
              }
            })
          }
        })
        .catch(() => {
          return
        })
    },
    Jumpmethod(id) {
      document.getElementById(id).scrollIntoView({
        behavior: 'smooth', // 平滑过渡
        block: 'start' // 上边框与视窗顶部平齐。默认值
      })
    },
    handleOpen(key, keyPath) {
    },
    handleClose(key, keyPath) {
    },
    menuClass() {
      $('.el-menu-item-group__title').css({
        padding: '4px 0 4px 20px'
      })
      $('.el-submenu__title').css({
        'padding-left': '0px',
        height: '40px',
        'margin-bottom': '8px'
      })
      $('.el-submenu__icon-arrow').css({
        'margin-top': '0px',
        right: '10px'
      })
    },
    jumpSite2(data) {
      document.getElementById(data).scrollIntoView({
        behavior: 'smooth', // 平滑过渡
        block: 'start' // 上边框与视窗顶部平齐。默认值
      })
    }
  },
  beforeRouteEnter(to, form, next) {
    $('.head').css({
      'position': 'fixed',
      'margin-left': '-17px'
    })
    next()
  },
  beforeRouteLeave(to, form, next) {
    clearInterval(this.treeRotation)
    $('.head').css({
      position: 'static',
      'margin-left': '0px'
    })
    vueEvent.$emit('changePlatform', '')
    // 离开当前路由时，清除树形图预测轮转
    next()
  }
}
</script>

<style lang="scss" scoped>
.allmap {
  height: 500px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  font-family: "微软雅黑";
  margin-bottom: 30px;
  margin-top: 24px;
}

.background {
  background: url("~@/assets/Workflow/background-begin-5.jpg") no-repeat center;
  background-size: 500px 500px;
  opacity: 0.9;
}

.treeDate {
  font-size: 16px;
  margin-left: 5px;
  color: rgba(0, 0, 0, 0.65);
  padding: 3px 3px 3px 3px;
  margin-top: 5px;
  font-weight: 400;
}

.treeDateScrolling {
  font-size: 16px;
  margin-left: 5px;
  background: #368b22;
  color: #ffffff;
  padding: 3px 3px 3px 3px;
  font-weight: 500;
}

.treeNumTitle {
  font-size: 16px;
  text-align: center;
  margin-top: 15px;
  font-family: Microsoft YaHei;
}

.treeNum {
  font-size: 12px;
  text-align: center;
  font-family: Microsoft YaHei;
}

.echartsColLeft {
  width: 500px;
  margin-top: 24px;
  box-shadow: 2px 0px 6px 0px rgba(0, 21, 41, 0.12);
}

.echartsColRight {
  width: 500px;
  margin-left: 24px;
  margin-top: 24px;
  box-shadow: 2px 0px 6px 0px rgba(0, 21, 41, 0.12);
}

.titleP {
  text-align: center;
  font-size: 16px;
  font-family: Microsoft YaHei;
  border-bottom: 1px solid #e9e9e9;
  margin-top: 0px;
  margin-bottom: 0px;
  text-align: left;
  padding: 16px 0px 13px 24px;
  color: #368c7c;
  font-weight: 500;
  background: #ebf3f2;
}

.echarts {
  width: 500px;
  height: 417px;
}

.el-menu-class::-webkit-scrollbar {
  //::-webkit-scrollbar — 整个滚动条.
  width: 5px;
  /*高宽分别对应横竖滚动条的尺寸*/
  height: 1px;
}

.el-menu-class::-webkit-scrollbar-thumb {
  //::-webkit-scrollbar-thumb — 滚动条上的滚动滑块.
  border-radius: 10px;
  -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  background: #368c7c;
}

.el-menu-class::-webkit-scrollbar-track {
  //::-webkit-scrollbar-track — 滚动条轨道.
  -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  border-radius: 10px;
  background: #EDEDED;
}

.el-menu-class {
  scrollbar-color: #368c7c #EDEDED;
  /* 滑块颜色  滚动条背景颜色 */
  scrollbar-width: thin;
  /* 滚动条宽度有三种：thin、auto、none */
}

.menuitem_Class {
  .el-menu-item {
    background-color: red
  }
}

.el-menu-item:focus,
.el-menu-item:hover {
  background-color: #368c7c;
  color: #ffffff
}

.el-menu-item.is-active {
  color: #ffffff;
  background: #368c7c;
}

/deep/.el-submenu__title:hover {
  background: #ebf3f2;
}

.el-menu-class {
  width: 100%;
  box-shadow: 2px 0px 6px 0px rgba(0, 21, 41, 0.12);
  overflow-x: auto;
  // max-height: 55%;
  //左侧导航栏滚动条限高
  margin-top: 128px;
}

/deep/.el-loading-mask {
  z-index: 1990;
}

.hr_Class {
  border: 0.1px dashed #000;
  height: 0px;
  margin-top: 430px;
  width: 80%
}

.labelUnit {
  cursor: url("../../assets/icon/afiado_ask.svg") 16 16, auto;
}
</style>
