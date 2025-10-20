<template>
  <div>
    <el-row :gutter="10">
      <el-col :span="12">
        <el-card>
          <div id="line" style="width: 100%; height: 400px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div id="bar" style="width: 100%; height: 400px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="10" style="margin: 0 10px">
      <el-col :span="12">
        <el-card>
          <div id="pie" style="width: 100%; height: 400px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
const option = {
  title:{
    text:'订单金额柱状图'
  },
  tooltip:{
    trigger:'axis'
  },
  xAxis: {
    type: 'category',
    data: []
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name:'金额',
      data: [],
      type: 'line'
    },
  ]
};
const option1 = {
  title:{
    text:'订单金额趋势图'
  },
  tooltip:{
    trigger:'axis'
  },
  xAxis: {
    type: 'category',
    data: []
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name:'金额',
      data: [],
      type: 'bar'
    },

  ]
};

const option2 = {
  title: {
    text: 'Referer of a Website',
    subtext: 'Fake Data',
    left: 'center'
  },
  tooltip: {
    trigger: 'item'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: '金额',
      type: 'pie',
      radius: '50%',
      data: [
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
};
import request from '@/utils/request';
export default ({
  name: "ChartsView",
  data(){
    return{

    }
  },
  mounted() {
    let lineDom = document.getElementById('line');
    let lineChart = echarts.init(lineDom)

    let barDom = document.getElementById('bar');
    let barChart = echarts.init(barDom)
    barChart.setOption(option1)

    let pieDom = document.getElementById('pie');
    let pieChart = echarts.init(pieDom)
    pieChart.setOption(option2)

    request.get('/charts').then(res=>{
      option.xAxis.data=res.data?.line?.map(v=>v.date) || []
      option.series[0].data=res.data?.line?.map(v=>v.value) || []
      lineChart.setOption(option)

      option1.xAxis.data=res.data?.bar?.map(v=>v.name) || []
      option1.series[0].data=res.data?.bar?.map(v=>v.value) || []
      barChart.setOption(option1)

      option2.series[0].data=res.data?.bar|| []
      pieChart.setOption(option2)
    })
  },
  methods:{}
})
</script>


<style scoped>

</style>