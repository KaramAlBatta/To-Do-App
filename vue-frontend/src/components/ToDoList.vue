<script setup>
import { ref, computed, onMounted } from 'vue'

const API = 'https://to-do-app-bhcc.onrender.com/api/todos'

const todos = ref([])
const newText = ref('')
const query = ref('')

const editingId = ref(null)
const editText = ref('')

const loading = ref(false)
const errorMsg = ref('')

async function loadTodos() {
  loading.value = true
  errorMsg.value = ''
  try {
    const res = await fetch(API)
    if (!res.ok) throw new Error('Konnte To-Dos nicht laden.')
    todos.value = await res.json()
  } catch (e) {
    errorMsg.value = e?.message ?? 'Fehler beim Laden.'
  } finally {
    loading.value = false
  }
}

async function addTodo() {
  const text = newText.value.trim()
  if (!text) return

  errorMsg.value = ''
  try {
    const res = await fetch(API, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ text, done: false }),
    })
    if (!res.ok) throw new Error('Konnte To-Do nicht speichern.')
    const saved = await res.json()
    todos.value.unshift(saved)
    newText.value = ''
  } catch (e) {
    errorMsg.value = e?.message ?? 'Fehler beim Speichern.'
  }
}

function startEdit(todo) {
  editingId.value = todo.id
  editText.value = todo.text
}

function cancelEdit() {
  editingId.value = null
  editText.value = ''
}

async function saveEdit(todo) {
  const text = editText.value.trim()
  if (!text) return

  errorMsg.value = ''
  try {
    const res = await fetch(`${API}/${todo.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ text, done: todo.done }),
    })
    if (!res.ok) throw new Error('Konnte Änderungen nicht speichern.')
    const updated = await res.json()

    const idx = todos.value.findIndex(t => t.id === todo.id)
    if (idx !== -1) todos.value[idx] = updated

    cancelEdit()
  } catch (e) {
    errorMsg.value = e?.message ?? 'Fehler beim Bearbeiten.'
  }
}

async function toggleDone(todo) {
  // sofort UI updaten
  const newDone = !todo.done
  todo.done = newDone

  errorMsg.value = ''
  try {
    const res = await fetch(`${API}/${todo.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ text: todo.text, done: newDone }),
    })
    if (!res.ok) throw new Error('Konnte Status nicht speichern.')
    const updated = await res.json()

    const idx = todos.value.findIndex(t => t.id === todo.id)
    if (idx !== -1) todos.value[idx] = updated
  } catch (e) {
    // rollback bei Fehler
    todo.done = !newDone
    errorMsg.value = e?.message ?? 'Fehler beim Speichern.'
  }
}

async function deleteTodo(todo) {
  errorMsg.value = ''
  const backup = [...todos.value]
  todos.value = todos.value.filter(t => t.id !== todo.id)

  try {
    const res = await fetch(`${API}/${todo.id}`, { method: 'DELETE' })
    if (!res.ok) throw new Error('Konnte To-Do nicht löschen.')
  } catch (e) {
    todos.value = backup
    errorMsg.value = e?.message ?? 'Fehler beim Löschen.'
  }
}

const filteredTodos = computed(() => {
  const q = query.value.trim().toLowerCase()
  if (!q) return todos.value
  return todos.value.filter(t => (t.text ?? '').toLowerCase().includes(q))
})

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

      <div class="search">
        <input v-model="query" class="search-input" type="text" placeholder="Suchen…" />
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
        />
      </div>

      <button type="submit" class="todo-button">
        <span class="btn-glow" />
        Hinzufügen
      </button>
    </form>

    <div v-if="errorMsg" class="error">
      ⚠️ {{ errorMsg }}
    </div>

    <div v-if="loading" class="hint">Lade…</div>

    <div v-else-if="filteredTodos.length === 0" class="empty">
      <div class="empty-icon">🔎</div>
      <div class="empty-title">Keine Treffer.</div>
      <div class="empty-sub">Versuch einen anderen Suchbegriff oder füge ein neues To-Do hinzu.</div>
    </div>

    <ul v-else class="todo-list">
      <li
          v-for="todo in filteredTodos"
          :key="todo.id"
          class="todo-item"
          :class="{ isDone: todo.done }"
      >
        <div class="left">
          <button
              class="checkBtn"
              type="button"
              :aria-label="todo.done ? 'Als offen markieren' : 'Als erledigt markieren'"
              @click="toggleDone(todo)"
          >
            <span class="checkDot" :class="{ active: todo.done }"></span>
          </button>

          <div class="content">
            <!-- VIEW -->
            <template v-if="editingId !== todo.id">
              <div class="text" :class="{ done: todo.done }">{{ todo.text }}</div>
              <div class="meta">
                <span class="pill" :class="todo.done ? 'pill-done' : 'pill-open'">
                  {{ todo.done ? 'Erledigt' : 'Offen' }}
                </span>
              </div>
            </template>

            <!-- EDIT -->
            <template v-else>
              <input
                  v-model="editText"
                  class="edit-input"
                  type="text"
                  @keydown.enter.prevent="saveEdit(todo)"
                  @keydown.esc.prevent="cancelEdit"
              />
              <div class="edit-actions">
                <button class="smallBtn primary" type="button" @click="saveEdit(todo)">
                  Speichern
                </button>
                <button class="smallBtn" type="button" @click="cancelEdit">
                  Abbrechen
                </button>
              </div>
            </template>
          </div>
        </div>

        <div class="actions" v-if="editingId !== todo.id">
          <button class="iconBtn" type="button" @click="startEdit(todo)" aria-label="Bearbeiten">
            ✏️
          </button>
          <button class="iconBtn danger" type="button" @click="deleteTodo(todo)" aria-label="Löschen">
            🗑️
          </button>
        </div>
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
}

/* Top */
.topbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 10px;
  flex-wrap: wrap;
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
  font-weight: 800;
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

.search {
  flex: 1;
  min-width: 180px;
  display: flex;
  justify-content: flex-end;
}
.search-input {
  width: min(260px, 100%);
  padding: 10px 12px;
  border-radius: 14px;
  border: 1px solid var(--border);
  background: rgba(0, 0, 0, 0.18);
  color: var(--text);
  outline: none;
}
.search-input::placeholder { color: rgba(255,255,255,0.45); }
.search-input:focus {
  border-color: rgba(99, 102, 241, 0.65);
  box-shadow: 0 0 0 4px rgba(99, 102, 241, 0.18);
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
}
.todo-input::placeholder { color: rgba(255,255,255,0.45); }
.todo-input:focus {
  border-color: rgba(99, 102, 241, 0.65);
  box-shadow: 0 0 0 4px rgba(99, 102, 241, 0.18);
  background: rgba(0, 0, 0, 0.24);
}

.todo-button {
  position: relative;
  padding: 12px 16px;
  border-radius: 14px;
  border: 1px solid rgba(255, 255, 255, 0.14);
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.95), rgba(16, 185, 129, 0.85));
  color: rgba(255, 255, 255, 0.95);
  font-size: 0.95rem;
  font-weight: 900;
  cursor: pointer;
  overflow: hidden;
  white-space: nowrap;
}
.todo-button:hover { filter: brightness(1.03); transform: translateY(-1px); }
.btn-glow {
  position: absolute;
  inset: -60px;
  background: radial-gradient(circle at 30% 30%, rgba(255, 255, 255, 0.35), transparent 55%);
  opacity: 0.65;
  pointer-events: none;
}

/* Info */
.error {
  margin: 8px 0 10px;
  padding: 10px 12px;
  border-radius: 14px;
  border: 1px solid rgba(239, 68, 68, 0.35);
  background: rgba(239, 68, 68, 0.10);
  color: rgba(255,255,255,0.9);
}
.hint {
  color: var(--muted);
  padding: 8px 2px 12px;
}

/* Empty */
.empty {
  border: 1px dashed rgba(255, 255, 255, 0.18);
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.035);
  padding: 18px;
  text-align: center;
}
.empty-icon { font-size: 1.6rem; margin-bottom: 6px; }
.empty-title { font-weight: 900; margin-bottom: 2px; }
.empty-sub { color: var(--muted); font-size: 0.95rem; }

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
  padding: 12px;
  border-radius: 16px;
  border: 1px solid var(--border);
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.055), rgba(255, 255, 255, 0.03));
  box-shadow: 0 10px 22px rgba(0, 0, 0, 0.22);
}
.todo-item.isDone { opacity: 0.88; }

.left { display: flex; align-items: flex-start; gap: 10px; min-width: 0; flex: 1; }
.content { min-width: 0; }
.text { font-size: 0.98rem; line-height: 1.35; word-break: break-word; }
.done { text-decoration: line-through; color: rgba(255,255,255,0.55); }
.meta { margin-top: 6px; display: flex; gap: 8px; flex-wrap: wrap; }

.pill {
  padding: 6px 10px;
  border-radius: 999px;
  font-size: 0.82rem;
  font-weight: 900;
  border: 1px solid rgba(255, 255, 255, 0.14);
}
.pill-open { background: rgba(99, 102, 241, 0.14); }
.pill-done { background: rgba(16, 185, 129, 0.14); }

.actions { display: flex; gap: 8px; }
.iconBtn {
  border: 1px solid rgba(255,255,255,0.14);
  background: rgba(255,255,255,0.05);
  border-radius: 12px;
  padding: 8px 10px;
  cursor: pointer;
}
.iconBtn:hover { background: rgba(255,255,255,0.08); }
.iconBtn.danger:hover { background: rgba(239,68,68,0.16); border-color: rgba(239,68,68,0.35); }

.checkBtn {
  border: 1px solid rgba(255,255,255,0.14);
  background: rgba(255,255,255,0.04);
  border-radius: 12px;
  width: 38px;
  height: 38px;
  display: grid;
  place-items: center;
  cursor: pointer;
  flex: 0 0 auto;
}
.checkBtn:hover { background: rgba(255,255,255,0.07); }
.checkDot {
  width: 12px;
  height: 12px;
  border-radius: 999px;
  border: 2px solid rgba(255,255,255,0.55);
}
.checkDot.active {
  border-color: rgba(16, 185, 129, 0.95);
  background: rgba(16, 185, 129, 0.65);
  box-shadow: 0 0 0 4px rgba(16, 185, 129, 0.18);
}

/* Edit */
.edit-input {
  width: min(520px, 68vw);
  max-width: 100%;
  padding: 10px 12px;
  border-radius: 14px;
  border: 1px solid var(--border);
  background: rgba(0,0,0,0.18);
  color: var(--text);
  outline: none;
}
.edit-input:focus {
  border-color: rgba(99, 102, 241, 0.65);
  box-shadow: 0 0 0 4px rgba(99, 102, 241, 0.18);
}
.edit-actions {
  display: flex;
  gap: 8px;
  margin-top: 8px;
  flex-wrap: wrap;
}
.smallBtn {
  border: 1px solid rgba(255,255,255,0.14);
  background: rgba(255,255,255,0.05);
  border-radius: 12px;
  padding: 8px 10px;
  cursor: pointer;
  color: rgba(255,255,255,0.9);
  font-weight: 800;
}
.smallBtn:hover { background: rgba(255,255,255,0.08); }
.smallBtn.primary {
  background: rgba(99, 102, 241, 0.22);
  border-color: rgba(99, 102, 241, 0.35);
}
.smallBtn.primary:hover { background: rgba(99, 102, 241, 0.28); }

@media (max-width: 520px) {
  .todo-button { width: 100%; }
  .search { justify-content: stretch; }
  .search-input { width: 100%; }
}
</style>
