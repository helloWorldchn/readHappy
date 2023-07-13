<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="openid" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['uOpenid', validatorRules.uOpenid]" placeholder="请输入openid"></a-input>
        </a-form-item>
        <a-form-item label="用户昵称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['uName', validatorRules.uName]" placeholder="请输入用户昵称"></a-input>
        </a-form-item>
        <a-form-item label="用户头像" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['uFace', validatorRules.uFace]" placeholder="请输入用户头像"></a-input>
        </a-form-item>
        <a-form-item label="用户随机码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['uRandom', validatorRules.uRandom]" placeholder="请输入用户随机码"></a-input>
        </a-form-item>
        <a-form-item label="积分" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['uIntegral']" placeholder="请输入积分" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="余额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['uRemainder']" placeholder="请输入余额" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="用户注册时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['uRegtime', validatorRules.uRegtime]" placeholder="请输入用户注册时间" style="width: 100%"/>
        </a-form-item>

      </a-form>
    </a-spin>
  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'


  export default {
    name: "MemberModal",
    components: { 
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
          uOpenid: {
            rules: [
              { required: true, message: '请输入openid!'},
            ]
          },
          uName: {
            rules: [
              { required: true, message: '请输入用户昵称!'},
            ]
          },
          uFace: {
            rules: [
              { required: true, message: '请输入用户头像!'},
            ]
          },
          uRandom: {
            rules: [
              { required: true, message: '请输入用户随机码!'},
            ]
          },
          uRegtime: {
            rules: [
              { required: true, message: '请输入用户注册时间!'},
            ]
          },
        },
        url: {
          add: "/member/member/add",
          edit: "/member/member/edit",
        }
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'uOpenid','uName','uFace','uRandom','uIntegral','uRemainder','uRegtime'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }
         
        })
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'uOpenid','uName','uFace','uRandom','uIntegral','uRemainder','uRegtime'))
      },

      
    }
  }
</script>