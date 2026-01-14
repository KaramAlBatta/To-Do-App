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
      <input
          v-model="newText"
          type="text"
          placeholder="Neues ToDo eingeben…"
          class="todo-input"
      />
      <button type="submit" class="todo-button">
        Hinzufügen
      </button>
    </form>

    <ul class="todo-list">
      <li v-for="todo in todos" :key="todo.id" class="todo-item">
        <label class="todo-label">
          <input type="checkbox" v-model="todo.done" />
          <span :class="{ done: todo.done }">
            {{ todo.text }}
          </span>
        </label>
      </li>
    </ul>
  </div>
</template>

<style scoped>
.todo {
  width: 100%;
}

.todo-form {
  display: flex;
  gap: 0.6rem;
  margin-bottom: 1.2rem;
}

.todo-input {
  flex: 1;
  padding: 0.5rem 0.7rem;
  border-radius: 6px;
  border: 1px solid #d1d5db;
  font-size: 0.95rem;
}

.todo-input:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 1px rgba(59, 130, 246, 0.35);
}

.todo-button {
  padding: 0.5rem 0.9rem;
  border-radius: 6px;
  border: none;
  background: #3b82f6;
  color: #ffffff;
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  white-space: nowrap;
}

.todo-button:hover {
  background: #2563eb;
}

.todo-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.todo-item + .todo-item {
  margin-top: 0.4rem;
}

.todo-label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.95rem;
}

.done {
  text-decoration: line-through;
  color: #9ca3af;
}
</style>
