<template>
  <el-form ref="form" :model="form" label-width="80px">
    <el-form-item label="周一" >
      <div>
        <el-time-select
          placeholder="起始时间"
          v-model="form.mon.startTime"
          :picker-options="{
      start: '07:00',
      step: '00:30',
      end: '24:00'
    }">
        </el-time-select>
        <el-time-select
          placeholder="结束时间"
          v-model="form.mon.endTime"
          :picker-options="{
      start: '07:00',
      step: '00:30',
      end: '24:00',
      minTime: form.mon.startTime
    }">
        </el-time-select>
      </div>
    </el-form-item>
    <el-form-item label="周二" >
      <div>
        <el-time-select
          placeholder="起始时间"
          v-model="form.tues.startTime"
          :picker-options="{
      start: '07:00',
      step: '00:30',
      end: '24:00'
    }">
        </el-time-select>
        <el-time-select
          placeholder="结束时间"
          v-model="form.tues.endTime"
          :picker-options="{
      start: '07:00',
      step: '00:30',
      end: '24:00',
      minTime: form.tues.startTime
    }">
        </el-time-select>
      </div>
    </el-form-item>
    <el-form-item label="周三" >
      <div>
        <el-time-select
          placeholder="起始时间"
          v-model="form.wed.startTime"
          :picker-options="{
      start: '07:00',
      step: '00:30',
      end: '24:00'
    }">
        </el-time-select>
        <el-time-select
          placeholder="结束时间"
          v-model="form.wed.endTime"
          :picker-options="{
      start: '07:00',
      step: '00:30',
      end: '24:00',
      minTime: form.wed.startTime
    }">
        </el-time-select>
      </div>
    </el-form-item>
    <el-form-item label="周四" >
      <div>
        <el-time-select
          placeholder="起始时间"
          v-model="form.thur.startTime"
          :picker-options="{
      start: '07:00',
      step: '00:30',
      end: '24:00'
    }">
        </el-time-select>
        <el-time-select
          placeholder="结束时间"
          v-model="form.thur.endTime"
          :picker-options="{
      start: '07:00',
      step: '00:30',
      end: '24:00',
      minTime: form.thur.startTime
    }">
        </el-time-select>
      </div>
    </el-form-item>
    <el-form-item label="周五" >
      <div>
        <el-time-select
          placeholder="起始时间"
          v-model="form.fri.startTime"
          :picker-options="{
      start: '07:00',
      step: '00:30',
      end: '24:00'
    }">
        </el-time-select>
        <el-time-select
          placeholder="结束时间"
          v-model="form.fri.endTime"
          :picker-options="{
      start: '07:00',
      step: '00:30',
      end: '24:00',
      minTime: form.fri.startTime
    }">
        </el-time-select>
      </div>
    </el-form-item>
    <el-form-item label="周六" >
      <div>
        <el-time-select
          placeholder="起始时间"
          v-model="form.sat.startTime"
          :picker-options="{
      start: '07:00',
      step: '00:30',
      end: '24:00'
    }">
        </el-time-select>
        <el-time-select
          placeholder="结束时间"
          v-model="form.sat.endTime"
          :picker-options="{
      start: '07:00',
      step: '00:30',
      end: '24:00',
      minTime: form.sat.startTime
    }">
        </el-time-select>
      </div>
    </el-form-item>
    <el-form-item label="周日" >
      <div>
        <el-time-select
          placeholder="起始时间"
          v-model="form.sun.startTime"
          :picker-options="{
      start: '07:00',
      step: '00:30',
      end: '24:00'
    }">
        </el-time-select>
        <el-time-select
          placeholder="结束时间"
          v-model="form.sun.endTime"
          :picker-options="{
      start: '07:00',
      step: '00:30',
      end: '24:00',
      minTime: form.sun.startTime
    }">
        </el-time-select>
      </div>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">保存工作时间</el-button>
    </el-form-item>
  </el-form>


</template>

<script>


import {addWorkTime, getWorkInfo} from "@/api/system/user";

export default {
  props: {
    workTime: {
      type: Object
    }
  },
name: "setWorkTime",
  data() {
    return {
      form: {
        mon: {
          startTime: '',
          endTime: ''
        },
        tues:{
          startTime: '',
          endTime: ''
        },
        wed:{
          startTime: '',
          endTime: ''
        },
        thur:{
          startTime: '',
          endTime: ''
        },
        fri:{
          startTime: '',
          endTime: ''
        },
        sat:{
          startTime: '',
          endTime: ''
        },
        sun:{
          startTime: '',
          endTime: ''
        }

      },

    };
  },
  watch: {
    workTime: {
      handler(workTime) {
        if (workTime){
          this.form = {mon: workTime.mon, tues: workTime.tues, wed: workTime.wed, thur: workTime.thur, fri: workTime.fri, sat: workTime.sat, sun: workTime.sun};
        }
      },
      immediate: true
    }
  },
  async created() {
    const resp = await getWorkInfo(this.$store.state.user.id);
    this.form.mon = resp.data.mon === '' ? JSON.parse("{}") : JSON.parse(resp.data.mon);
    this.form.tues = resp.data.tues === '' ? JSON.parse("{}") : JSON.parse(resp.data.tues);
    this.form.wed = resp.data.wed === '' ? JSON.parse("{}") : JSON.parse(resp.data.wed);
    this.form.thur = resp.data.thur === '' ? JSON.parse("{}") : JSON.parse(resp.data.thur);
    this.form.fri = resp.data.fri === '' ? JSON.parse("{}") : JSON.parse(resp.data.fri);
    this.form.sat = resp.data.sat === '' ? JSON.parse("{}") : JSON.parse(resp.data.sat);
    this.form.sun = resp.data.sun === '' ? JSON.parse("{}") : JSON.parse(resp.data.sun);
    console.log(this.form)
    console.log(resp)
  },
  methods: {
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log(this.$store.state.user.id)
          this.form.bId = this.$store.state.user.id
          this.form.mon = JSON.stringify(this.form.mon)
          this.form.tues = JSON.stringify(this.form.tues)
          this.form.wed = JSON.stringify(this.form.wed)
          this.form.thur = JSON.stringify(this.form.thur)
          this.form.fri = JSON.stringify(this.form.fri)
          this.form.sat = JSON.stringify(this.form.sat)
          this.form.sun = JSON.stringify(this.form.sun)
          addWorkTime(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
          });
          this.$router.go(0)
        }
      });
    }
  }

}
</script>

<style scoped>

</style>
