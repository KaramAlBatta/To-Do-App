<template>
  <div>
    <h2>Aufgaben</h2>
    <ToDoItem
        v-for="todo in todos"
        :key="todo.id"
        :todo="todo"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import ToDoItem from './ToDoItem.vue'

const todos = ref([])

onMounted(async () => {
  const res = await fetch('http://localhost:8080/api/todos')
  todos.value = await res.json()
})
</script>
