<template>
  <div class="page">
    <div class="card">
      <h1>Meine To-Do Liste</h1>
      <p class="subtitle">Einträge hinzufügen, abhaken und verwalten.</p>
      <ToDoList />
    </div>
  </div>
</template>

<script setup>
import ToDoList from './components/ToDoList.vue'
</script>

<style scoped>
.page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f3f4f6;
  font-family: system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
}

.card {
  width: 100%;
  max-width: 520px;
  background: #ffffff;
  border-radius: 12px;
  padding: 1.8rem 2rem 2rem;
  box-shadow: 0 10px 25px rgba(15, 23, 42, 0.12);
}

h1 {
  margin: 0 0 0.25rem;
  font-size: 1.6rem;
}

.subtitle {
  margin: 0 0 1.2rem;
  color: #6b7280;
  font-size: 0.9rem;
}
</style>
