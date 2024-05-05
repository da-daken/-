<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="服务类型" prop="typeId">
        <el-select
          v-model="queryParams.typeId"
          placeholder="用户审核状态"
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
      <el-form-item label="单价" prop="singelPrice">
        <el-input
          v-model="queryParams.singelPrice"
          placeholder="请输入单价"
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
            <img
              slot="extra"
              width="272"
              alt="logo"
              src=""
            />
          </div>
          <div>
            家政员姓名 ：{{ item.username }}
          </div>
          <div>
            服务详情 ：{{ item.content }}
          </div>
          <div>
            单价 ：{{ item.singelPrice }} 元/平方米
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
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="单价" prop="singelPrice">
          <el-input v-model="form.singelPrice" placeholder="请输入单价" />
        </el-form-item>
        <el-form-item label="数量" prop="typeId">
          <el-input v-model="form.counts" placeholder="需要的数量" />
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
import {getProduct, delProduct, addProduct, updateProduct, listProductForRe, getInfoForRe} from "@/api/system/product";
import 'ant-design-vue/dist/antd'
import {createOrder} from "@/api/order/order";

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
        singelPrice: null,
        score: null,
        isDeleted: null
      },
      // 表单参数
      form: {},
      request: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询【请填写功能名称】列表 */
    getList() {
      this.loading = true;
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
              startTime: this.form.startTime,
              endTime: this.form.endTime,
              counts: this.form.counts
            }
            createOrder(this.request).then(response => {
              this.$modal.msgSuccess("预约成功");
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
