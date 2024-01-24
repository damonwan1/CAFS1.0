<template>
  <div id="global-uploader">
    <!-- 上传 -->
    <uploader :options="options" :autoStart="true">
      <uploader-drop>
        <uploader-btn :single="true">选择文件</uploader-btn>
      </uploader-drop>
      <uploader-list></uploader-list>
    </uploader>
  </div>
</template>
<script>
export default {
  data() {
    return {
      options: {

      }
    }
  }
}
</script>