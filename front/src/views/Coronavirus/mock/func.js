import Cookie from "js-cookie";
export default function decimalPoint(data) {
  // 实时预测_接口数据保留三位小数
  data.gpp = Number(data.gpp).toFixed(2);
  data.nep = Number(data.nep).toFixed(2);
  data.ra = Number(data.ra).toFixed(2);
  data.npp = Number(data.npp).toFixed(2);
  data.rh = Number(data.rh).toFixed(2);
  data.cf = Number(data.cf).toFixed(2);
  data.cw = Number(data.cw).toFixed(2);
  data.cr = Number(data.cr).toFixed(2);
  data.cveg = Number(data.cveg).toFixed(2);
  data.csom = Number(data.csom).toFixed(2);
  data.af = Number(data.af).toFixed(2);
  data.aw = Number(data.aw).toFixed(2);
  data.ar = Number(data.ar).toFixed(2);
  data.tveg = Number(data.tveg).toFixed(2);
  data.tsoil = Number(data.tsoil).toFixed(2);
  return data;
}

function linePredictionResults(data, str) {
  this.line.series.push({
    name: str,
    type: "line",
    symbol: "none",
    sampling: "lttb",
    itemStyle: {
      color: "blue"
    },
    data: data
  });
}
function predictionResults(data, bar, str) {
  bar.series[0].data = [];
  var lineData = JSON.parse(JSON.stringify(bar));
  if (str === "litterfall") {
    for (let i = 1; i < data.length; i++) {
      lineData.series[0].data.push(parseFloat(data[i].litterfall));
      lineData.series[0].name = str;
      lineData.yAxis[0].name = str;
      lineData.xAxis.data.push(
        data[i].year + "/" + data[i].month + "/" + data[i].day
      );
    }
  } else if (str === "cf") {
    for (let i = 1; i < data.length; i++) {
      lineData.series[0].data.push(parseFloat(data[i].cf));
      lineData.series[0].name = str;
      lineData.yAxis[0].name = str;
      lineData.xAxis.data.push(
        data[i].year + "/" + data[i].month + "/" + data[i].day
      );
    }
  } else if (str === "cr") {
    for (let i = 1; i < data.length; i++) {
      lineData.series[0].data.push(parseFloat(data[i].cr));
      lineData.series[0].name = str;
      lineData.yAxis[0].name = str;
      lineData.xAxis.data.push(
        data[i].year + "/" + data[i].month + "/" + data[i].day
      );
    }
  } else if (str === "cw") {
    for (let i = 1; i < data.length; i++) {
      lineData.series[0].data.push(parseFloat(data[i].cw));
      lineData.series[0].name = str;
      lineData.yAxis[0].name = str;
      lineData.xAxis.data.push(
        data[i].year + "/" + data[i].month + "/" + data[i].day
      );
    }
  } else if (str === "csom") {
    for (let i = 1; i < data.length; i++) {
      lineData.series[0].data.push(parseFloat(data[i].csom));
      lineData.series[0].name = str;
      lineData.yAxis[0].name = str;
      lineData.xAxis.data.push(
        data[i].year + "/" + data[i].month + "/" + data[i].day
      );
    }
  } else if (str === "lai") {
    for (let i = 1; i < data.length; i++) {
      lineData.series[0].data.push(parseFloat(data[i].lai));
      lineData.series[0].name = str;
      lineData.yAxis[0].name = str;
      lineData.xAxis.data.push(
        data[i].year + "/" + data[i].month + "/" + data[i].day
      );
    }
  } else if (str === "rs") {
    for (let i = 1; i < data.length; i++) {
      lineData.series[0].data.push(parseFloat(data[i].rs));
      lineData.series[0].name = str;
      lineData.yAxis[0].name = str;
      lineData.xAxis.data.push(
        data[i].year + "/" + data[i].month + "/" + data[i].day
      );
    }
  } else if (str === "nee") {
    for (let i = 1; i < data.length; i++) {
      lineData.series[0].data.push(parseFloat(data[i].nee));
      lineData.series[0].name = str;
      lineData.yAxis[0].name = str;
      lineData.xAxis.data.push(
        data[i].year + "/" + data[i].month + "/" + data[i].day
      );
    }
  }
  return lineData;
}

function lineDataSpell(data, bar, lineData, str, f, long = 0) {
  // 实时预测_实际数据预测折线图属性
  bar.series[0].data = [];
  bar.xAxis.data = [];
  lineData = JSON.parse(JSON.stringify(bar)); // 深拷贝数据

  // lineData.series[0].name = str
  for (let i = 0; i < data.length; i++) {
    // lineData.xAxis.data.push(data[i].year + '' + data[i].month + '' + data[i].day)
    lineData.series[0].markArea.data[0][0].xAxis =
      data[data.length - 7].year +
      "/" +
      data[data.length - 7].month +
      "/" +
      data[data.length - 7].day; // 折线图右侧区域高亮
    lineData.series[0].markArea.data[0][1].xAxis =
      data[data.length - 1].year +
      "/" +
      data[data.length - 1].month +
      "/" +
      data[data.length - 1].day;
    if (str === "GPP") {
      // if (f == "DHF") {
      if (data.length > 67 ? i > data.length - 66 : i < data.length) {
        // if(i < data.length - 6){
        // lineData.series[0].data.push(Number(data[i].gpp).toFixed(2)); // 最近66天数据
        // }
        lineData.xAxis.data.push(
          data[i].year + "/" + data[i].month + "/" + data[i].day
        ); // X轴坐标单位
        lineData.visualMap[0].outOfRange.color = "#F2457D"; // 右侧高亮外其他区域的线条颜色
        // lineData.series[0].areaStyle.color.colorStops[1].color = 'rgba(242, 69, 125,1)' // 线条下方渐变颜色
        lineData.yAxis[0].nameTextStyle.padding[3] = 105; // Y轴单位位置偏移
        // lineData.series[0].name = 'gpp(总初级生产力)' // 变量名称
        lineData.yAxis[0].name = "GPP(g C m⁻² day⁻¹)"; // Y轴单位
      }
      // } else {
      //   if (i > data.length - 67) {
      //     // if (data.length > 67 ? i > data.length - 66 : i < data.length) {
      //     // if(i < data.length - 6){
      //     lineData.series[0].data.push(Number(data[i].gpp).toFixed(2)); // 最近66天数据
      //     // }
      //     lineData.xAxis.data.push(
      //       data[i].year + "/" + data[i].month + "/" + data[i].day
      //     ); // X轴坐标单位
      //     lineData.visualMap[0].outOfRange.color = "#F2457D"; // 右侧高亮外其他区域的线条颜色
      //     // lineData.series[0].areaStyle.color.colorStops[1].color = 'rgba(242, 69, 125,1)' // 线条下方渐变颜色
      //     lineData.yAxis[0].nameTextStyle.padding[3] = 105; // Y轴单位位置偏移
      //     // lineData.series[0].name = 'gpp(总初级生产力)' // 变量名称
      //     lineData.yAxis[0].name = "GPP(g C m⁻² day⁻¹)"; // Y轴单位
      //   }
      // }
    } else if (str === "NPP") {
      // if (f == "DHF") {
      // if (i > data.length - 67) {
      if (data.length > 67 ? i > data.length - 66 : i < data.length) {
        // if(i < data.length - 6){
        // lineData.series[0].data.push(Number(data[i].npp).toFixed(2));
        // }
        lineData.xAxis.data.push(
          data[i].year + "/" + data[i].month + "/" + data[i].day
        );
        lineData.visualMap[0].outOfRange.color = "#ECAD9E";
        // lineData.series[0].areaStyle.color.colorStops[1].color = 'rgba(236, 173, 158,1)'
        lineData.yAxis[0].nameTextStyle.padding[3] = 90;
        // lineData.series[0].name = 'npp(净初级生产力)'
        lineData.yAxis[0].name = "NPP(g C m⁻² day⁻¹)";
      }
      // } else {
      //   if (i > data.length - 67) {
      //     // if(i < data.length - 6){
      //     lineData.series[0].data.push(Number(data[i].npp).toFixed(2));
      //     // }
      //     lineData.xAxis.data.push(
      //       data[i].year + "/" + data[i].month + "/" + data[i].day
      //     );
      //     lineData.visualMap[0].outOfRange.color = "#ECAD9E";
      //     // lineData.series[0].areaStyle.color.colorStops[1].color = 'rgba(236, 173, 158,1)'
      //     lineData.yAxis[0].nameTextStyle.padding[3] = 90;
      //     // lineData.series[0].name = 'npp(净初级生产力)'
      //     lineData.yAxis[0].name = "NPP(g C m⁻² day⁻¹)";
      //   }
      // }
    } else if (str === "NEP") {
      // if (f == "DHF") {
      // console.log(long);
      if (data.length > 67 ? i > data.length - 66 : i < data.length) {
        // if (i > 6 - long) {
        // lineData.series[0].data.push(Number(data[i].nep).toFixed(2));
        // }
        lineData.xAxis.data.push(
          data[i].year + "/" + data[i].month + "/" + data[i].day
        );
        // lineData.visualMap[0].outOfRange.color = "#D1BA74";
        // lineData.series[0].areaStyle.color.colorStops[1].color = 'rgba(209, 186, 116,1)'
        lineData.yAxis[0].nameTextStyle.padding[3] = 80;
        // lineData.series[0].name = 'nep(净生态系统生产力)'
        lineData.yAxis[0].name = "NEP(g C m⁻² day⁻¹)";
        let jsonLegend = {};
        jsonLegend.orient = "horizontal";
        jsonLegend.x = "middle";
        jsonLegend.y = "top";
        // jsonLegend.data = ["实际值", "预测值"];
        // lineData.legend = jsonLegend;
        // if (Cookie.get("language") == "en") {
        //   lineData.legend.data = ["actualValue", "predictiveValue"];
        // } else {
        //   lineData.legend.data = ["实际值", "预测值"];
        // }
      }
      // lineData.series[0].data.push(Number(data[i].nep).toFixed(2));
      // lineData.xAxis.data.push(
      //   data[i].year + "/" + data[i].month + "/" + data[i].day
      // );
      // lineData.visualMap[0].outOfRange.color = "#D1BA74";
      // // lineData.series[0].areaStyle.color.colorStops[1].color = 'rgba(209, 186, 116,1)'
      // lineData.yAxis[0].nameTextStyle.padding[3] = 80;
      // // lineData.series[0].name = 'nep(净生态系统生产力)'
      // lineData.yAxis[0].name = "NEP(g C m⁻² day⁻¹)";
      // } else {
      //   if (i > data.length - 67) {
      //     lineData.series[0].data.push(Number(data[i].nep).toFixed(2));
      //     lineData.xAxis.data.push(
      //       data[i].year + "/" + data[i].month + "/" + data[i].day
      //     );
      //     lineData.visualMap[0].outOfRange.color = "#D1BA74";
      //     // lineData.series[0].areaStyle.color.colorStops[1].color = 'rgba(209, 186, 116,1)'
      //     lineData.yAxis[0].nameTextStyle.padding[3] = 80;
      //     // lineData.series[0].name = 'nep(净生态系统生产力)'
      //     lineData.yAxis[0].name = "NEP(g C m⁻² day⁻¹)";
      //   }
      // }
    }
    //  else if (str === 'Cveg') {
    //   if (i > data.length - 66) {
    //     lineData.xAxis.data.push(data[i].year + '/' + data[i].month + '/' + data[i].day)
    //     lineData.series[0].data.push(Number(data[i].cveg).toFixed(2))
    //     lineData.visualMap[0].outOfRange.color = '#A0EEE1'
    //     lineData.yAxis[0].nameTextStyle.padding[3] = 5
    //     // lineData.series[0].name = 'cveg(植被碳)'
    //     lineData.yAxis[0].name = 'Cveg(g C m⁻²)'
    //     lineData.yAxis[0].min = 10000
    //   }
    // }
    // else if (str === 'Csom') {
    //   if (i > data.length - 66) {
    //     lineData.xAxis.data.push(data[i].year + '/' + data[i].month + '/' + data[i].day)
    //     lineData.series[0].data.push(Number(data[i].csom).toFixed(2))
    //     lineData.visualMap[0].outOfRange.color = '#3A8A23'
    //     lineData.yAxis[0].nameTextStyle.padding[3] = 25
    //     // lineData.series[0].name = 'csom(土壤碳)'
    //     lineData.yAxis[0].name = 'Csom(g C m⁻²)'
    //     lineData.yAxis[0].min = 9000
    //   }
    // }
    // lineData.visualMap[0].pieces[0].gte = lineData.series[0].data.length - 7 // 最测高亮区域最后7天线条颜色区域
    // lineData.visualMap[0].pieces[0].lte = lineData.series[0].data.length - 1
  }
  return lineData;
}
function DriverDataSpell(data, bar, lineData, str) {
  // 实时预测_驱动数据预测折线图属性
  bar.series[0].data = [];
  bar.xAxis.data = [];
  lineData = JSON.parse(JSON.stringify(bar));
  var jsonLegend = {};
  // lineData.series[0].name = str
  for (let i = 0; i < data.length; i++) {
    lineData.series[0].markArea.data[0][0].xAxis =
      data[data.length - 8].year +
      "/" +
      data[data.length - 8].month +
      "/" +
      data[data.length - 8].day;
    lineData.series[0].markArea.data[0][1].xAxis =
      data[data.length - 1].year +
      "/" +
      data[data.length - 1].month +
      "/" +
      data[data.length - 1].day;
    if (str === "par") {
      if (data.length > 67 ? i > data.length - 66 : i < data.length) {
        if (i < 60) {
          lineData.series[0].data.push(Number(data[i].par).toFixed(2));
        }
        lineData.xAxis.data.push(
          data[i].year + "/" + data[i].month + "/" + data[i].day
        );
        // lineData.series[0].areaStyle.color.colorStops[1].color = 'rgba(84, 112, 198,1)'
        // lineData.series[0].name = 'par(光合有效辐射)'
        lineData.yAxis[0].name = "PAR(mol day⁻¹)";
        // lineData.series[0].name = ''
        lineData.yAxis[0].nameTextStyle.padding[3] = 60;
        // 放入legend上方浮动标签
        jsonLegend = {};
        jsonLegend.orient = "horizontal";
        jsonLegend.x = "middle";
        jsonLegend.y = "top";
        jsonLegend.data = ["实际值", "预测值"];
        lineData.legend = jsonLegend;
      }
    } else if (str === "rh") {
      if (data.length > 67 ? i > data.length - 66 : i < data.length) {
        if (i < 60) {
          lineData.series[0].data.push(Number(data[i].rh).toFixed(2));
        }
        lineData.xAxis.data.push(
          data[i].year + "/" + data[i].month + "/" + data[i].day
        );
        //lineData.visualMap[0].outOfRange.color = '#91CC75'
        lineData.yAxis[0].name = "Rh(%)";
        // lineData.series[0].name = 'rh(相对湿度)'
        lineData.yAxis[0].nameTextStyle.padding[3] = 5;
        // lineData.series[0].name = 'rh(相对湿度)'
        // 放入legend上方浮动标签
        jsonLegend = {};
        jsonLegend.orient = "horizontal";
        jsonLegend.x = "middle";
        jsonLegend.y = "top";
        jsonLegend.data = ["实际值", "预测值"];
        lineData.legend = jsonLegend;
      }
    } else if (str === "swc") {
      if (data.length > 67 ? i > data.length - 66 : i < data.length) {
        if (i < 60) {
          lineData.series[0].data.push(Number(data[i].swc).toFixed(2));
        }
        lineData.xAxis.data.push(
          data[i].year + "/" + data[i].month + "/" + data[i].day
        );
        //lineData.visualMap[0].outOfRange.color = '#FAC858'
        // lineData.series[0].areaStyle.color.colorStops[1].color = 'rgba(250, 200, 88,1)'
        lineData.yAxis[0].name = "SWC(m³ m⁻³)";
        // lineData.series[0].name = 'swc(土壤含水量)'
        lineData.yAxis[0].nameTextStyle.padding[3] = 20;
        // 放入legend上方浮动标签
        jsonLegend = {};
        jsonLegend.orient = "horizontal";
        jsonLegend.x = "middle";
        jsonLegend.y = "top";
        jsonLegend.data = ["实际值", "预测值"];
        lineData.legend = jsonLegend;
      }
    } else if (str === "ta") {
      if (data.length > 67 ? i > data.length - 66 : i < data.length) {
        if (i < 60) {
          lineData.series[0].data.push(Number(data[i].ta).toFixed(2));
        }
        lineData.xAxis.data.push(
          data[i].year + "/" + data[i].month + "/" + data[i].day
        );
        //lineData.visualMap[0].outOfRange.color = '#EE6666'
        lineData.yAxis[0].name = "Ta(℃)";
        // lineData.series[0].name = 'ta(日均气温)'
        lineData.yAxis[0].nameTextStyle.padding[3] = 20;
        // 放入legend上方浮动标签
        jsonLegend = {};
        jsonLegend.orient = "horizontal";
        jsonLegend.x = "middle";
        jsonLegend.y = "top";
        jsonLegend.data = ["实际值", "预测值"];
        lineData.legend = jsonLegend;
      }
    } else if (str === "vpd") {
      if (data.length > 67 ? i > data.length - 66 : i < data.length) {
        if (i < 60) {
          lineData.series[0].data.push(Number(data[i].vpd).toFixed(2));
        }
        lineData.xAxis.data.push(
          data[i].year + "/" + data[i].month + "/" + data[i].day
        );
        //lineData.visualMap[0].outOfRange.color = '#73C0DE'
        lineData.yAxis[0].name = "VPD(kPa)";
        lineData.yAxis[0].nameTextStyle.padding[3] = 20;
        // lineData.series[0].name = 'vpd(饱和水汽压差)'
        // 放入legend上方浮动标签
        jsonLegend = {};
        jsonLegend.orient = "horizontal";
        jsonLegend.x = "middle";
        jsonLegend.y = "top";
        jsonLegend.data = ["实际值", "预测值"];
        lineData.legend = jsonLegend;
      }
    } else if (str === "GPP") {
      if (data.length > 67 ? i > data.length - 66 : i < data.length) {
        // if (i < 60) {
        lineData.series[0].data.push(Number(data[i].gpp).toFixed(2));
        // }
        lineData.xAxis.data.push(
          data[i].year + "/" + data[i].month + "/" + data[i].day
        );
        //lineData.visualMap[0].outOfRange.color = '#91CC75'
        lineData.yAxis[0].name = "GPP(g C m⁻² day⁻¹)"; // Y轴单位
        // lineData.series[0].name = 'GPP(相对湿度)'
        lineData.yAxis[0].nameTextStyle.padding[3] = 5;
        // lineData.series[0].name = 'GPP(相对湿度)'
        // 放入legend上方浮动标签
        jsonLegend = {};
        jsonLegend.orient = "horizontal";
        jsonLegend.x = "middle";
        jsonLegend.y = "top";
        jsonLegend.data = ["实际值", "预测值"];
        lineData.legend = jsonLegend;
      }
    } else if (str === "NPP") {
      if (data.length > 67 ? i > data.length - 66 : i < data.length) {
        // if (i < 60) {
        lineData.series[0].data.push(Number(data[i].npp).toFixed(2));
        // }
        lineData.xAxis.data.push(
          data[i].year + "/" + data[i].month + "/" + data[i].day
        );
        //lineData.visualMap[0].outOfRange.color = '#91CC75'
        lineData.yAxis[0].name = "NPP(g C m⁻² day⁻¹)";
        // lineData.series[0].name = 'NPP(相对湿度)'
        lineData.yAxis[0].nameTextStyle.padding[3] = 5;
        // lineData.series[0].name = 'NPP(相对湿度)'
        // 放入legend上方浮动标签
        jsonLegend = {};
        jsonLegend.orient = "horizontal";
        jsonLegend.x = "middle";
        jsonLegend.y = "top";
        jsonLegend.data = ["实际值", "预测值"];
        lineData.legend = jsonLegend;
      }
    } else if (str === "NEP") {
      if (data.length > 67 ? i > data.length - 66 : i < data.length) {
        // if (i < 60) {
        lineData.series[0].data.push(Number(data[i].nep).toFixed(2));
        // }
        lineData.xAxis.data.push(
          data[i].year + "/" + data[i].month + "/" + data[i].day
        );
        //lineData.visualMap[0].outOfRange.color = '#91CC75'
        lineData.yAxis[0].name = "NEP(g C m⁻² day⁻¹)";
        // lineData.series[0].name = 'NEP(相对湿度)'
        lineData.yAxis[0].nameTextStyle.padding[3] = 5;
        // lineData.series[0].name = 'NEP(相对湿度)'
        // 放入legend上方浮动标签
        jsonLegend = {};
        jsonLegend.orient = "horizontal";
        jsonLegend.x = "middle";
        jsonLegend.y = "top";
        jsonLegend.data = ["实际值", "预测值"];
        lineData.legend = jsonLegend;
      }
    }
    // 高亮预测数据，注释掉
    //lineData.visualMap[0].pieces[0].gte = lineData.series[0].data.length - 7
    //lineData.visualMap[0].pieces[0].lte = lineData.series[0].data.length - 1
  }
  if (Cookie.get("language") == "en") {
    lineData.legend.data = ["actualValue", "predictiveValue"];
    // lineData.series[0].name = 'actualValue'
    // lineData.series[3].name = 'predictiveValue'
  } else {
    lineData.legend.data = ["实际值", "预测值"];
  }
  return lineData;
}

function nodeSort(mockData) {
  // 工作流_节点排序
  for (var i = 0; i < mockData.nodes.length; i++) {
    for (var j = 0; j < mockData.nodes.length - 1; j++) {
      // 节点排序
      if (parseInt(mockData.nodes[j].id) > parseInt(mockData.nodes[j + 1].id)) {
        var temp = mockData.nodes[j];
        mockData.nodes[j] = mockData.nodes[j + 1];
        mockData.nodes[j + 1] = temp;
      }
    }
  }
  return mockData;
}
function nodeLineSort(mockData) {
  // 工作流_节点连线排序
  for (var x = 0; x < mockData.edges.length; x++) {
    for (var y = 0; y < mockData.edges.length - 1; y++) {
      if (
        parseInt(mockData.edges[y].sourceNode) >
        parseInt(mockData.edges[y + 1].sourceNode)
      ) {
        var temp2 = mockData.edges[y];
        mockData.edges[y] = mockData.edges[y + 1];
        mockData.edges[y + 1] = temp2;
      } else if (
        parseInt(mockData.edges[y].sourceNode) ===
          parseInt(mockData.edges[y + 1].sourceNode) &&
        parseInt(mockData.edges[y].targetNode) >
          parseInt(mockData.edges[y + 1].targetNode)
      ) {
        var temp3 = mockData.edges[y];
        mockData.edges[y] = mockData.edges[y + 1];
        mockData.edges[y + 1] = temp3;
      }
    }
  }
  return mockData;
}

// function addErrorbar(option, data, name) {
//   var averageValue = []
//   var value = []
//   for (let i = 0; i < data.length; i++) {
//     if (data[i].name === name) {
//       averageValue = data[i].averageValue.slice(6)
//       value = data[i].value.slice(6)
//     }
//   }
//   // 增加误差棒
//   option.series.push({
//     name: 'boxplot',
//     type: 'boxplot',
//     data: [],
//     datasetIndex: 3
//   })
//   option.series[1].data = value

//   // 增加平均线
//   option.series.push({
//     data: [],
//     type: 'line',
//     smooth: false
//   })
//   option.series[2].data = averageValue
//   console.log(option)
// }

async function addErrorbar(option, data, name, long = 0) {
  var value = [];
  var averageValue = [];
  var max = [];
  var min = [];
  for (let i = 0; i < data.length; i++) {
    if (name === data[i].name) {
      if (long > 0) {
        // value = data[i].value.slice(long);
        // averageValue = data[i].averageValue.slice(long);
        value = data[i].value.slice(long);
        averageValue = data[i].averageValue.slice(long);
      } else {
        value = data[i].value;
        averageValue = data[i].averageValue;
      }
    }
  }
  // if (name == "Nep") {
  //   let valLength = value.length;
  //   let averLength = averageValue.length;
  //   if (value.length < 67) {
  //     for (let i = 0; i < 67 - valLength; i++) {
  //       value.unshift([""]);
  //     }
  //   }
  //   if (averageValue.length < 67) {
  //     for (let j = 0; j < 67 - averLength; j++) {
  //       // averageValue.unshift((Math.random()+1))
  //       averageValue.unshift("");
  //     }
  //   }
  //   // 替换时间到时去掉
  //   let date = data[0].date;
  //   option.xAxis.data = date;
  //   // console.log(value, averageValue);
  // }
  // console.log(value,min,max);
  // let date = data[0].date.map(str => {
  //   return str.replace(/-/g, "/");
  // });
  // option.xAxis.data = date;
  await getMinMaxNumber(value, min, max);

  option.series.push({
    name: "预测值",
    type: "line",
    // 平均线
    data: averageValue,
    itemStyle: {
      color: "#9A60B4"
    },
    showSymbol: false
  });
  option.series.push({
    name: "L",
    type: "line",
    // 最小值
    data: min,
    lineStyle: {
      opacity: 0
    },
    stack: "confidence-band",
    symbol: "none"
  });
  option.series.push({
    name: "U",
    type: "line",
    // 放入最大值-最小值（数值会堆叠）
    data: max,
    lineStyle: {
      opacity: 0
    },
    //让最大值与最小值中间的区域高亮
    areaStyle: {
      color: "#CCC"
    },
    stack: "confidence-band",
    symbol: "none"
  });

  console.log(option.series);
}

// 鼎湖山备份方法
async function addErrorbar2(option, data, name, long) {
  var value = [];
  var averageValue = [];
  var max = [];
  var min = [];
  for (let i = 0; i < data.length; i++) {
    //   if (name === data[i].name) {
    //     if (long > 0) {
    //       var dateBt = data[0].date.slice(long - 7);
    //       value = data[i].value.slice(long);
    //       averageValue = data[i].averageValue.slice(long);
    //       // value = data[i].value.slice(0);
    //       // averageValue = data[i].averageValue.slice(0);
    //     } else {
    //       value = data[i].value;
    //       averageValue = data[i].averageValue;
    //       // console.log(value, averageValue);
    //     }
    //   }
    if (name === data[i].name) {
      value = data[i].value;
      averageValue = data[i].averageValue;
    }
  }
  // if (name == "Nep") {
  // let valLength = value.length;
  // let averLength = averageValue.length;
  // if (value.length < 67) {
  //   for (let i = 0; i < 67 - valLength; i++) {
  //     value.unshift([""]);
  //   }
  // }
  // if (averageValue.length < 67) {
  //   for (let j = 0; j < 67 - averLength; j++) {
  //     // averageValue.unshift((Math.random()+1))
  //     averageValue.unshift("");
  //   }
  // }
  // 替换时间到时去掉
  let date = data[0].date.map(str => {
    return str.replace(/-/g, "/");
  });
  option.xAxis.data = date;
  option.xAxis.type = "category";
  option.xAxis.axisLine.onZero = false;

  option.series[0].markArea.data[0][0].xAxis = date[date.length - 8];

  option.series[0].markArea.data[0][1].xAxis = date[date.length - 1];

  // option.dataZoom = {
  //   type: "inside",
  //   orient: "vertical"
  // };

  let dataMin = value.map(item => {
    return Math.min(...item);
  });
  if (Math.min(...dataMin) > 0) {
    await getMinMaxNumber(value, min, max);
    option.series.push({
      name: "预测值",
      type: "line",
      // 平均线
      data: averageValue,
      itemStyle: {
        color: "#9A60B4"
      },
      showSymbol: false
    });
    option.series.push({
      name: "L",
      type: "line",
      // 最小值
      data: min,
      lineStyle: {
        opacity: 0
      },
      stack: "confidence-band",
      symbol: "none"
    });
    option.series.push({
      name: "U",
      type: "line",
      // 放入最大值-最小值（数值会堆叠）
      data: max,
      lineStyle: {
        opacity: 0
      },
      //让最大值与最小值中间的区域高亮
      areaStyle: {
        color: "#CCC"
      },
      stack: "confidence-band",
      symbol: "none"
    });
  } else {
    await getMinMaxNumber(value, min, max, dataMin);
    option.series.push({
      name: "L",
      type: "line",
      // 最小值
      lineStyle: {
        opacity: 0
      },
      stack: "confidence-band",
      areaStyle: {
        // color: "blue",
        opacity: 0
        // origin: "start"
      },
      data: min,
      symbol: "none"
    });
    option.series.push({
      name: "U",
      type: "line",
      // 放入最大值-最小值（数值会堆叠）
      lineStyle: {
        opacity: 0
      },
      //让最大值与最小值中间的区域高亮
      stack: "confidence-band",
      areaStyle: {
        // origin: "end",
        color: "#CCC"
        // opacity: 0
      },
      data: max,
      symbol: "none"
    });
    option.series.push({
      name: "预测值",
      type: "line",
      // 平均线
      data: averageValue.map(item => item - Math.min(...dataMin)),
      itemStyle: {
        color: "#9A60B4"
      },
      showSymbol: false
    });
    option.yAxis = {
      axisLabel: {
        formatter: function(val) {
          console.log(val);
          return (val + Math.min(...dataMin)).toFixed(2);
        }
      },
      axisPointer: {
        label: {
          formatter: function(params) {
            console.log(params);
            return (params.value + Math.min(...dataMin)).toFixed(2);
          }
        }
      }
    };
    option.tooltip = {
      formatter: function(params) {
        return (
          params[2].name + " : " + (params[2].value + Math.min(...dataMin))
        );
      }
    };
  }
}

function getMinMaxNumber(data, min, max, dataMin = null) {
  if (dataMin) {
    for (let i = 0; i < data.length; i++) {
      data[i] = data[i].map(item => {
        return Number(item);
      });
      min.push(Math.min(...data[i]) - Math.min(...dataMin));
      max.push(Math.max(...data[i]) - Math.min(...data[i]));
      // min.push(Math.min(...data[i]));
      // if (Math.min(...data[i]) < 0 && Math.max(...data[i]) >= 0) {
      //   min.push(Math.min(...data[i]));
      //   max.push(Math.max(...data[i]));
      // } else if (Math.min(...data[i]) < 0 && Math.max(...data[i]) < 0) {
      //   // min.push(Math.max(...data[i]));
      //   min.push(Math.max(...data[i]));
      //   max.push(Math.min(...data[i]) - Math.max(...data[i]));
      //   // max.push();
      // } else {
      //   min.push(Math.min(...data[i]));
      //   max.push(Math.max(...data[i]) - Math.min(...data[i]));
      // }
    }
  } else {
    for (let i = 0; i < data.length; i++) {
      data[i] = data[i].map(item => {
        return Number(item);
      });
      // if (Math.min(...data[i]) < 0 && Math.max(...data[i]) < 0) {
      //   min.push(Math.max(...data[i]));
      //   max.push(Math.min(...data[i]) - Math.max(...data[i]));
      // } else if (Math.min(...data[i]) < 0 && Math.max(...data[i]) >= 0) {
      //   min.push(Math.max(...data[i]));
      //   max.push(Math.min(...data[i]) - Math.max(...data[i]));
      // } else {
      min.push(Math.min(...data[i]));
      max.push(Math.max(...data[i]) - Math.min(...data[i]));
      // }
    }
  }
  // 已经取到了最小值，
}

export {
  // import导入要加花括号
  // export命令对外接口是有名称的且import命令从模块导入的变量名与被导入模块对外接口的名称相同，而export default命令对外输出的变量名可以是任意的，这时import命令后面，不使用大括号。
  lineDataSpell,
  DriverDataSpell,
  nodeSort,
  nodeLineSort,
  workflowTemplate,
  predictionResults,
  linePredictionResults,
  addErrorbar,
  addErrorbar2
};
