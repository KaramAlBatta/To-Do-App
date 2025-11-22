<script setup>
import { ref, onMounted } from 'vue'

const todos = ref([])

onMounted(async () => {
  const response = await fetch('http://localhost:8080/api/todos')
  todos.value = await response.json()
})
</script>

<template>
  <ul>
    <li v-for="todo in todos" :key="todo.id">
      <input type="checkbox" v-model="todo.done" />
      {{ todo.text }}
    </li>
  </ul>
</template>

<style scoped>
ul {
  list-style: none;
  padding: 0;
}

li {
  margin: 0.5rem 0;
}
</style>
