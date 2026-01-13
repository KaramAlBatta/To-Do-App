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
  <div class="todo">
    <form @submit.prevent="addTodo" class="todo-form">
      <input v-model="newText" placeholder="Neues ToDo" />
      <button type="submit">Hinzufügen</button>
    </form>

    <ul class="todo-list">
      <li v-for="todo in todos" :key="todo.id">
        <label>
          <input type="checkbox" v-model="todo.done" />
          <span :class="{ done: todo.done }">{{ todo.text }}</span>
        </label>
      </li>
    </ul>
  </div>
</template>

<style scoped>
.todo {
  max-width: 400px;
}

.todo-form {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.todo-form input {
  flex: 1;
  padding: 0.4rem 0.6rem;
}

.todo-form button {
  padding: 0.4rem 0.8rem;
  cursor: pointer;
}

.todo-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.todo-list li {
  margin: 0.4rem 0;
}

.done {
  text-decoration: line-through;
  color: #666;
}
</style>
