<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="家政员" prop="bId">
        <el-input
          v-model="queryParams.bId"
          placeholder="请输入家政员id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="服务" prop="productId">
        <el-input
          v-model="queryParams.productId"
          placeholder="请输入服务类型id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开始时间" prop="startTime">
        <el-date-picker clearable
          v-model="queryParams.startTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="订单状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="订单状态"
          clearable
          style="width: 240px"
        >
          <el-option
            v-for="dict in dict.type.sys_order_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['order:order:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['order:order:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['order:order:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['order:order:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订单id" align="center" prop="id" />
      <el-table-column label="家政员" align="center" prop="bName" />
      <el-table-column label="购买用户" align="center" prop="cName" />
      <el-table-column label="服务类型" align="center" prop="productName" />
      <el-table-column label="服务地址" align="center" prop="address" />
      <el-table-column label="创建时间" align="center" prop="createTime" />
      <el-table-column label="开始时间" align="center" prop="startTime" width="180">
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="180">
      </el-table-column>
      <el-table-column label="订单总价" align="center" prop="totalPrice" />
      <el-table-column label="订单单项数量" align="center" prop="count" />
      <el-table-column label="订单状态" align="center" prop="status" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_order_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="6位数的订单核销码" align="center" prop="code" >
        <template slot-scope="scope">
          {{ getBCode(scope.row) }}
        </template>
      </el-table-column>
      <el-table-column label="该订单服务的得分" align="center" prop="score" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status !== 4 && scope.row.status !== 2"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >下一步操作</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改用户、家政员对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item v-if="this.$store.state.user.roles.at(0) === 'service' && form.status === 1" label="6位数的订单核销码" prop="code">
          <el-input v-model="form.code" @change="checkOperate" placeholder="请输入6位数的订单核销码" />
        </el-form-item>
        <el-form-item v-if="this.$store.state.user.roles.at(0) === 'normal' && form.status === 3" label="服务打分" prop="score">
          <el-input v-model="form.score" @change="commitOperate" placeholder="请输入该订单服务的得分（1-5分，填数字即可）" />
        </el-form-item>
        <el-form-item v-if="this.$store.state.user.roles.at(0) === 'normal' && (form.status === 1 || form.status === 0)" label="取消 or 支付" >
<!--          <el-radio-group v-model="form.status" @change="onStatusChange(this.form.status)">-->
<!--            <el-radio-->
<!--              v-for="dict in dict.type.sys_yes_on"-->
<!--              :key="dict.value"-->
<!--              :label="dict.value"-->
<!--            >{{dict.label}}</el-radio>-->
<!--          </el-radio-group>-->
          <el-button @click="cancelOperate"> 取消 </el-button>
          <el-button v-if="this.form.status === 0" @click="payOperate"> 支付 </el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOrder, getOrder, delOrder, addOrder, updateOrder, createOrder, payOrder, generateToken, cancelOrder, commitOrder, getRoleInfo}from "@/api/order/order";
export default {
  name: "Order",
  dicts: ['sys_order_status', 'sys_yes_on'],
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
      // 用户、家政员表格数据
      orderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        bId: null,
        cId: null,
        productId: null,
        dateTime: null,
        startTime: null,
        endTime: null,
        count: null,
        status: null,
        code: null,
        pId:null,
        score: null,
        roleId: null
      },
      // 表单参数
      form: {},
      operate: null,
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getBCode(row) {
      console.log(row)
      if (this.$store.state.user.roles.at(0) !== 'service'){
        return row.code
      } else if (row.status === 3 || row.status === 4){
        return row.code
      } else {
        return '******'
      }
    },
    /** 查询用户、家政员列表 */
    getList() {
      this.loading = true;
      this.queryParams.roleId = this.$store.state.user.roles.at(0)
      listOrder(this.queryParams).then(response => {
        this.orderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    cancelOperate() {
      this.operate = 'cancel'
    },
    payOperate() {
      this.operate ='pay'
    },
    checkOperate() {
      this.operate = 'check'
    },
    commitOperate() {
      this.operate = 'commit'
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
        bId: null,
        cId: null,
        productId: null,
        dateTime: null,
        startTime: null,
        endTime: null,
        count: null,
        createTime: null,
        status: null,
        code: null,
        score: null,
        operate:null
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
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加用户、家政员";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "进行下一步操作";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            this.form.operate = this.operate
            updateOrder(this.form).then(response => {
              this.$modal.msgSuccess("操作成功");
              this.open = false;
              this.getList();
            });
          } else {
            addOrder(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除用户、家政员编号为"' + ids + '"的数据项？').then(function() {
        return delOrder(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('order/order/export', {
        ...this.queryParams
      }, `order_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
