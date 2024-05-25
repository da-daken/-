<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="服务类型" prop="typeId">
        <el-select
          v-model="queryParams.typeId"
          placeholder="服务类型"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        >
          <el-option
            v-for="dict in dict.type.sys_product_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>

      </el-form-item>
      <el-form-item label="家政员" prop="singelPrice">
        <el-input
          v-model="queryParams.username"
          placeholder="请输入家政员"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
      <a-list :grid="{ gutter: 16, column: 4 }" :data-source="productList">
      <a-list-item slot="renderItem" slot-scope="item, index">
        <a-card :title="item.productName">
          <div>
            <el-image
              style="width: 300px; height: 300px"
              :src=item.img
              :preview-src-list=[item.img]>
            </el-image>
          </div>
          <div>
            家政员姓名 ：{{ item.username }}
          </div>
          <div>
            服务详情 ：{{ item.content }}
          </div>
          <div>
            单价 ：{{ item.singelPrice }} /{{ item.unit }}
          </div>
          <div>
            该家政员服务评分 ：{{ item.score }}
          </div>
          <div>
            <a-button type="primary" @click="handleAdd(item)" >立即预约</a-button>
          </div>
        </a-card>
      </a-list-item>
    </a-list>



    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改【请填写功能名称】对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form"  label-width="80px">
        <el-form-item label="数量" prop="typeId" v-if="this.form.typeId === 2">
          <el-input v-model="form.counts" placeholder="需要的数量" />
        </el-form-item>
        <el-form-item label="地址" prop="address" >
          <el-input v-model="form.address" placeholder="预约服务的地址" />
        </el-form-item>
        <div class="block">
          <span class="demonstration">选择预约日期</span>
          <el-date-picker clearable
                          v-model="date1"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择预约日期"
          @change="handleChange">
          </el-date-picker>
        </div>
        <div class="times-grid">
          <el-button
          class="time-slot"
          v-for="(enableTime, index) in enableTimeList"
          :key="index"
          :class="{'disabled' : enableTime.enable === false, 'selected' : isSelectedTime(index)}"
          @click="toggle(index)">
            {{ enableTime.time }}
          </el-button>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {getProduct, delProduct, addProduct, updateProduct, listProductForRe, getInfoForRe} from "@/api/system/product";
import 'ant-design-vue/dist/antd'
import {createOrder, getEnableTime} from "@/api/order/order";

export default {
  name: "Product",
  dicts: ['sys_product_type'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 【请填写功能名称】表格数据
      productList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        typeId: null,
        img: null,
        content: null,
        username: null,
        score: null,
        isDeleted: null
      },
      // 表单参数
      form: {},
      form1: {},
      request: {},
      enableTimeList: [],
      date1: null,
      request1: {
        calDate: null,
        bId: null
      },
      selectedTimeRange: []

    };
  },
  created() {
    this.getList();
    console.log(this.$store.state.user)
  },
  methods: {
    test(){
      console.log('hahahah')
    },
    toggle(index) {
      // 判断时间段是否可选
      if (this.enableTimeList[index].enable === 'false'){
        console.log('true')
        return ;
      }
      // 获取当前时间段是否已经被选中
      const existingIndex = this.selectedTimeRange.indexOf(index)
      // 如果已经选中，则取消选择
      if (existingIndex >= 0) {
        this.selectedTimeRange.splice(existingIndex, 1)
      } else {
        // 未被选中，而且当前时间已选中时间小于2，可用添加
        if (this.selectedTimeRange.length < 2){
          this.selectedTimeRange.push(index)
        } else {
          // 已经有了两个时间段，替换最后一个选中的时间段
          this.selectedTimeRange[1] = index
        }
        // 确保时间段按照顺序存储
        this.selectedTimeRange.sort((a, b) => a - b)
      }
      console.log(this.selectedTimeRange)
    },
    isSelectedTime(index) {
      return this.selectedTimeRange.includes(index)
    },
    getStartTime() {
      // 返回最早的时间
      if (!this.selectedTimeRange.length) return null;
      return this.enableTimeList[this.selectedTimeRange[0]].time
    },
    getEndTime() {
      // 返回最后一个时间
      if (this.selectedTimeRange.length < 2) return null;
      return this.enableTimeList[this.selectedTimeRange[1]].time
    },
    handleChange() {
      this.request1.calDate = this.date1
      this.request1.bId = this.form.userId
      getEnableTime(this.request1).then(resp => {
        console.log(resp.data)
        this.enableTimeList = resp.data
      })
    },
    /** 查询【请填写功能名称】列表 */
    getList() {
      this.loading = true;
      console.log(this.queryParams)
      listProductForRe(this.queryParams).then(response => {
        this.productList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        pId: null,
        bId: null,
        cId: null,
        productId: null,
        counts: null,
        startTime: null,
        endTime: null,
        isDeleted: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams.username = ''
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd(item) {
      this.reset();
      getInfoForRe(item.id).then(response => {
        this.form = response.data;
        console.log(this.form)
        this.open = true;
        this.title = "立即预约";
        this.date1 = new Date()
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getProduct(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改【请填写功能名称】";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id == null) {
            this.form.cId = this.$store.state.user.id;
            updateProduct(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.request = {
              pId: this.form.id,
              bId: this.form.userId,
              cId: this.$store.state.user.id,
              productId: this.form.typeId,
              startTime: this.convertString(this.date1, this.getStartTime()),
              endTime: this.convertString(this.date1, this.getEndTime()),
              counts: Number(this.form.counts),
              address: this.form.address
            }
            if (this.request.startTime === null || this.request.endTime === null) {
              console.log(this.request.startTime)
              console.log(this.request.endTime)
              this.$modal.msgError("请选择正确的两个时间")
              return ;
            }
            console.log(this.request)
            console.log(this.selectedTimeRange)
            createOrder(this.request).then(response => {
              this.$modal.msgSuccess("预约成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    convertString(dateStr, timeStr) {
      // 解析日期和时间字符串
      const [year, month, day] = dateStr.split('-').map(Number);
      const [hours, minutes] = timeStr.split(':').map(Number);

      // 创建一个新的Date对象，结合给定的日期和时间
      const combinedDateTime = new Date(year, month - 1, day, hours, minutes);

      // 格式化日期和时间为指定格式
      const formattedDateTime = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')} ${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:00`;

      return formattedDateTime;
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除【请填写功能名称】编号为"' + ids + '"的数据项？').then(function() {
        return delProduct(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/product/export', {
        ...this.queryParams
      }, `product_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
<style>
.times-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
}

.time-slot {
  padding: 10px;
  text-align: center;
  cursor: pointer;
  border: 1px solid #ddd;
}
.disabled {
  background-color: #eaeaea;
  cursor: not-allowed;
}

.selected {
  background-color: #bada55;
}
</style>
