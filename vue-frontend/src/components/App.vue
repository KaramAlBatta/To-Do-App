<template>
  <div class="container">
    <h1>Meine To-Do Liste</h1>
    <ToDoList />
  </div>
</template>

<script setup>
import ToDoList from './components/ToDoList.vue'
</script>

<style scoped>
.container {
  max-width: 600px;
  margin: 2rem auto;
  font-family: Arial, sans-serif;
}
</style>
