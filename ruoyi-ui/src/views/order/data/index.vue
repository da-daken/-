<template>
  <div class="app-container">
    今年的服务种类需求量:
    <div id="main" style="width: 600px;height:400px;">

    </div>
  </div>
</template>


<script>
import * as echarts from 'echarts/core';
import {
  ToolboxComponent,
  TooltipComponent,
  GridComponent,
  LegendComponent
} from 'echarts/components';
import { BarChart } from 'echarts/charts';
import { CanvasRenderer } from 'echarts/renderers';
import { hotService } from "@/api/order/order"

export default {

  data() {
    return {
      dataList: [],
      sum1: [],
      sum2: []
    }
  },

  // async created() {
   created() {

    echarts.use([
      ToolboxComponent,
      TooltipComponent,
      GridComponent,
      LegendComponent,
      BarChart,
      CanvasRenderer
    ]);

  },
  methods: {
    async getData() {
      const resp = await hotService(new Date())
      this.dataList = resp.data
    }
  },
  async mounted() {
    await this.getData()
    this.sum1 = this.dataList.map(item => item.sum1)
    this.sum2 = this.dataList.map(item => item.sum2)
    console.log( this.sum1)
    console.log(this.sum2)
    var app = {};

    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);
    var option;
    const posList = [
      'left',
      'right',
      'top',
      'bottom',
      'inside',
      'insideTop',
      'insideLeft',
      'insideRight',
      'insideBottom',
      'insideTopLeft',
      'insideTopRight',
      'insideBottomLeft',
      'insideBottomRight'
    ];
    app.configParameters = {
      rotate: {
        min: -90,
        max: 90
      },
      align: {
        options: {
          left: 'left',
          center: 'center',
          right: 'right'
        }
      },
      verticalAlign: {
        options: {
          top: 'top',
          middle: 'middle',
          bottom: 'bottom'
        }
      },
      position: {
        options: posList.reduce(function (map, pos) {
          map[pos] = pos;
          return map;
        }, {})
      },
      distance: {
        min: 0,
        max: 100
      }
    };
    app.config = {
      rotate: 90,
      align: 'left',
      verticalAlign: 'middle',
      position: 'insideBottom',
      distance: 15,
      onChange: function () {
        const labelOption = {
          rotate: app.config.rotate,
          align: app.config.align,
          verticalAlign: app.config.verticalAlign,
          position: app.config.position,
          distance: app.config.distance
        };
        myChart.setOption({
          series: [
            {
              label: labelOption
            },
            {
              label: labelOption
            },
            {
              label: labelOption
            },
            {
              label: labelOption
            }
          ]
        });
      }
    };
    const labelOption = {
      show: true,
      position: app.config.position,
      distance: app.config.distance,
      align: app.config.align,
      verticalAlign: app.config.verticalAlign,
      rotate: app.config.rotate,
      formatter: '{c}  {name|{a}}',
      fontSize: 16,
      rich: {
        name: {}
      }
    };

    option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      legend: {
        data: ['Forest', 'Steppe', 'Desert', 'Wetland']
      },
      toolbox: {
        show: true,
        orient: 'vertical',
        left: 'right',
        top: 'center',
        feature: {
          mark: { show: true },
          dataView: { show: true, readOnly: false },
          magicType: { show: true, type: ['line', 'bar', 'stack'] },
          restore: { show: true },
          saveAsImage: { show: true }
        }
      },
      xAxis: [
        {
          type: 'category',
          axisTick: { show: false },
          data: [
            '1月',
            '2月',
            '3月',
            '4月',
            '5月',
            '6月',
            '7月',
            '8月',
            '9月',
            '10月',
            '11月',
            '12月'
          ]
        }
      ],
      yAxis: [
        {
          type: 'value'
        }
      ],
      series: [
        {
          name: '日常保洁',
          type: 'bar',
          barGap: 0,
          label: labelOption,
          emphasis: {
            focus: 'series'
          },
          data: this.sum1,
        },
        {
          name: '擦玻璃',
          type: 'bar',
          label: labelOption,
          emphasis: {
            focus: 'series'
          },
          data: this.sum2,
        }
      ]
    };

    option && myChart.setOption(option);
  }
}
</script>

