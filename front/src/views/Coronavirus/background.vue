<template>
  <div class="background">
    <router-view />
  </div>
</template>

<script>

export default {
  name: 'Maopao',
  data() {
    return {
    }
  },
  methods: {
  }
}
</script>
<style lang="scss" scoped>
.background {
  background: url("~@/assets/Workflow/loginback.jpg") no-repeat center;
  background-size: 100% 100%;
  opacity: 0.9;
  width:100%;
  height:100%;
  min-width: 0px !important
}
</style>
