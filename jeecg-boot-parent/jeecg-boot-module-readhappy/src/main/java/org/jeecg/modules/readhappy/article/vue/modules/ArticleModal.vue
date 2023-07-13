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

        <a-form-item label="文章分类" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['artCate']" placeholder="请输入文章分类" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="文章标题" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['artTitle']" placeholder="请输入文章标题"></a-input>
        </a-form-item>
        <a-form-item label="文章发表用户id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['artUid']" placeholder="请输入文章发表用户id" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="文章内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['artContent']" rows="4" placeholder="请输入文章内容"/>
        </a-form-item>
        <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['artCreatetime']" placeholder="请输入创建时间" style="width: 100%"/>
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
    name: "ArticleModal",
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
        },
        url: {
          add: "/article/article/add",
          edit: "/article/article/edit",
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
          this.form.setFieldsValue(pick(this.model,'artCate','artTitle','artUid','artContent','artCreatetime'))
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
        this.form.setFieldsValue(pick(row,'artCate','artTitle','artUid','artContent','artCreatetime'))
      },

      
    }
  }
</script>