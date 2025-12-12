<script setup>
import { ref, onMounted } from 'vue'

const todos = ref([])
const newText = ref('')

async function loadTodos() {
  const response = await fetch('https://to-do-app-bhcc.onrender.com/api/todos')
  todos.value = await response.json()
}

async function addTodo() {
  if (!newText.value.trim()) return

  const response = await fetch('https://to-do-app-bhcc.onrender.com/api/todos', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      text: newText.value,
      done: false
    })
  })

  const saved = await response.json()
  todos.value.push(saved)
  newText.value = ''
}

onMounted(loadTodos)
</script>

<template>
  <div>
    <form @submit.prevent="addTodo">
      <input v-model="newText" placeholder="Neues ToDo" />
      <button type="submit">Hinzufügen</button>
    </form>

    <ul>
      <li v-for="todo in todos" :key="todo.id">
        <input type="checkbox" v-model="todo.done" />
        {{ todo.text }}
      </li>
    </ul>
  </div>
</template>
