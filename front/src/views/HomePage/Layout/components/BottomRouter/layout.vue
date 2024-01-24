<template>
  <div class="page-container">
    <page-header/>
    <page-main/>
  </div>
</template>

<script>
import { PageHeader, PageFooter, PageMain } from '../.././components'

export default {
  name: 'Buttomlayout',
  components: {
    PageHeader,
    PageFooter,
    PageMain
  }
}
</script>
