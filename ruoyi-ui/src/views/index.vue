<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="24">
        <blockquote class="text-warning" style="font-size: 14px">
          欢迎光临 daken 的家政小店
          <br />
        </blockquote>

        <hr />
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <h2>daken 的家政小店</h2>
        <p>
          这个平台可以满足你对日常家政服务预约的需求，有很多金牌阿姨！预约响应快！
        </p>
        <el-col :sm="24" :lg="12" style="padding-left: 50px">
          <el-row>
            <el-col :span="12">
              <h2>服务类型</h2>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <h4>日常保洁</h4>
              <ul>
                <li>点击跳转（待实现）</li>
              </ul>
            </el-col>
            <el-col :span="6">
              <h4>擦玻璃</h4>
              <ul>
                <li>点击跳转（待实现）</li>
              </ul>
            </el-col>
          </el-row>
        </el-col>
      </el-col>
    </el-row>
    <el-divider />
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>日常通告</span>
          </div>
          <div class="body">
            <p>
            ***************
            </p>
          </div>
          <div class="clearfix">
            <span>
              可以每月展示各个服务下的金牌家政员
            </span>
          </div>
        </el-card>
        <el-card class="update-log">
          <div class="body">
            <p>
              ***************
            </p>
          </div>
          <div slot="header" class="clearfix">
            <span>
              可以每月展示各个服务下的金牌家政员
            </span>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
      </el-col>
    </el-row>

  </div>
</template>

<script>
export default {
  name: "Index",
  data() {
    return {
      // 版本号
      version: "3.8.7"
    };
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    }
  }
};
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>

