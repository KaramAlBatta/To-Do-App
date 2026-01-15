<script setup>
import { ref, computed, onMounted } from 'vue'

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
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ text: newText.value, done: false }),
  })

  const saved = await response.json()
  todos.value.unshift(saved)
  newText.value = ''
}

const remaining = computed(() => todos.value.filter(t => !t.done).length)
const doneCount = computed(() => todos.value.filter(t => t.done).length)

onMounted(loadTodos)
</script>

<template>
  <div class="todo">
    <div class="topbar">
      <div class="stats">
        <span class="chip">
          <span class="chip-dot" /> Offen: <b>{{ remaining }}</b>
        </span>
        <span class="chip muted">
          Erledigt: <b>{{ doneCount }}</b>
        </span>
      </div>
    </div>

    <form @submit.prevent="addTodo" class="todo-form">
      <div class="input-wrap">
        <span class="spark">＋</span>
        <input
            v-model="newText"
            type="text"
            placeholder="Neues To-Do eingeben…"
            class="todo-input"
            aria-label="Neues To-Do"
        />
      </div>

      <button type="submit" class="todo-button">
        <span class="btn-glow" />
        Hinzufügen
      </button>
    </form>

    <div v-if="todos.length === 0" class="empty">
      <div class="empty-icon">✅</div>
      <div class="empty-title">Alles leer — nice.</div>
      <div class="empty-sub">Füge oben ein To-Do hinzu, um loszulegen.</div>
    </div>

    <ul v-else class="todo-list">
      <li v-for="todo in todos" :key="todo.id" class="todo-item" :class="{ isDone: todo.done }">
        <label class="todo-label">
          <input class="todo-check" type="checkbox" v-model="todo.done" />
          <span class="todo-text" :class="{ done: todo.done }">{{ todo.text }}</span>
        </label>

        <span class="pill" :class="todo.done ? 'pill-done' : 'pill-open'">
          {{ todo.done ? 'Erledigt' : 'Offen' }}
        </span>
      </li>
    </ul>
  </div>
</template>

<style scoped>
.todo {
  width: 100%;
  --border: rgba(255, 255, 255, 0.14);
  --text: rgba(255, 255, 255, 0.92);
  --muted: rgba(255, 255, 255, 0.70);
  --muted2: rgba(255, 255, 255, 0.55);
  --glass: rgba(255, 255, 255, 0.06);
  --glass2: rgba(255, 255, 255, 0.04);
  --shadow: 0 16px 40px rgba(0, 0, 0, 0.25);
}

/* Top chips */
.topbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
}
.stats {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}
.chip {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 7px 10px;
  border-radius: 999px;
  border: 1px solid var(--border);
  background: rgba(255, 255, 255, 0.05);
  color: var(--muted);
  font-size: 0.9rem;
}
.chip b {
  color: var(--text);
  font-weight: 700;
}
.chip.muted {
  background: rgba(255, 255, 255, 0.035);
  color: var(--muted2);
}
.chip-dot {
  width: 8px;
  height: 8px;
  border-radius: 999px;
  background: rgba(99, 102, 241, 0.95);
  box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.25);
}

/* Form */
.todo-form {
  display: flex;
  gap: 10px;
  margin-bottom: 14px;
  flex-wrap: wrap;
}

.input-wrap {
  position: relative;
  flex: 1;
  min-width: 220px;
}

.spark {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  width: 28px;
  height: 28px;
  display: grid;
  place-items: center;
  border-radius: 10px;
  border: 1px solid var(--border);
  background: rgba(255, 255, 255, 0.04);
  color: rgba(255, 255, 255, 0.75);
  font-weight: 700;
}

.todo-input {
  width: 100%;
  padding: 12px 12px 12px 52px;
  border-radius: 14px;
  border: 1px solid var(--border);
  background: rgba(0, 0, 0, 0.18);
  color: var(--text);
  font-size: 0.98rem;
  outline: none;
  transition: transform 140ms ease, box-shadow 140ms ease, border-color 140ms ease,
  background 140ms ease;
}

.todo-input::placeholder {
  color: rgba(255, 255, 255, 0.45);
}

.todo-input:focus {
  border-color: rgba(99, 102, 241, 0.65);
  box-shadow: 0 0 0 4px rgba(99, 102, 241, 0.18);
  background: rgba(0, 0, 0, 0.24);
}

/* Button */
.todo-button {
  position: relative;
  padding: 12px 16px;
  border-radius: 14px;
  border: 1px solid rgba(255, 255, 255, 0.14);
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.95), rgba(16, 185, 129, 0.85));
  color: rgba(255, 255, 255, 0.95);
  font-size: 0.95rem;
  font-weight: 800;
  letter-spacing: 0.2px;
  cursor: pointer;
  box-shadow: 0 14px 30px rgba(0, 0, 0, 0.25);
  overflow: hidden;
  transition: transform 140ms ease, box-shadow 140ms ease, filter 140ms ease;
  white-space: nowrap;
}

.todo-button:hover {
  transform: translateY(-1px);
  filter: brightness(1.03);
  box-shadow: 0 18px 40px rgba(0, 0, 0, 0.32);
}

.todo-button:active {
  transform: translateY(0px);
}

.btn-glow {
  position: absolute;
  inset: -60px;
  background: radial-gradient(circle at 30% 30%, rgba(255, 255, 255, 0.35), transparent 55%);
  transform: translateZ(0);
  opacity: 0.65;
  pointer-events: none;
}

/* Empty state */
.empty {
  border: 1px dashed rgba(255, 255, 255, 0.18);
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.035);
  padding: 18px;
  text-align: center;
}
.empty-icon {
  font-size: 1.6rem;
  margin-bottom: 6px;
}
.empty-title {
  font-weight: 800;
  letter-spacing: -0.01em;
  margin-bottom: 2px;
}
.empty-sub {
  color: var(--muted);
  font-size: 0.95rem;
}

/* List */
.todo-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: grid;
  gap: 10px;
}

.todo-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 12px 12px;
  border-radius: 16px;
  border: 1px solid var(--border);
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.055), rgba(255, 255, 255, 0.03));
  box-shadow: 0 10px 22px rgba(0, 0, 0, 0.22);
  transition: transform 140ms ease, border-color 140ms ease, background 140ms ease;
}

.todo-item:hover {
  transform: translateY(-1px);
  border-color: rgba(255, 255, 255, 0.22);
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.07), rgba(255, 255, 255, 0.035));
}

.todo-item.isDone {
  opacity: 0.88;
}

.todo-label {
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 0;
}

.todo-text {
  color: var(--text);
  font-size: 0.98rem;
  line-height: 1.35;
  word-break: break-word;
}

.done {
  text-decoration: line-through;
  color: rgba(255, 255, 255, 0.55);
}

/* Checkbox styling */
.todo-check {
  width: 18px;
  height: 18px;
  accent-color: rgb(99, 102, 241);
  cursor: pointer;
}

/* Status pill */
.pill {
  flex: 0 0 auto;
  padding: 6px 10px;
  border-radius: 999px;
  font-size: 0.82rem;
  font-weight: 800;
  border: 1px solid rgba(255, 255, 255, 0.14);
}
.pill-open {
  background: rgba(99, 102, 241, 0.14);
  color: rgba(255, 255, 255, 0.9);
}
.pill-done {
  background: rgba(16, 185, 129, 0.14);
  color: rgba(255, 255, 255, 0.9);
}

/* Better mobile spacing */
@media (max-width: 520px) {
  .todo-form {
    gap: 8px;
  }
  .todo-button {
    width: 100%;
  }
}
</style>
