<template>
  <div class="page">
    <div class="bg-orb orb-1" />
    <div class="bg-orb orb-2" />
    <div class="bg-orb orb-3" />

    <main class="shell">
      <header class="header">
        <div class="badge">✨ Produktiv bleiben</div>
        <h1>Meine To-Do Liste</h1>
        <p class="subtitle">Einträge hinzufügen, abhaken und verwalten — schnell & übersichtlich.</p>
      </header>

      <section class="card">
        <ToDoList />
      </section>

      <footer class="footer">
        <span class="dot" /> <span>Tipp: Enter zum Hinzufügen</span>
      </footer>
    </main>
  </div>
</template>

<script setup>
import ToDoList from './components/ToDoList.vue'
</script>

<style scoped>
/* ---------- Theme Tokens ---------- */
.page {
  --bg: #0b1020;
  --card: rgba(255, 255, 255, 0.08);
  --card2: rgba(255, 255, 255, 0.06);
  --border: rgba(255, 255, 255, 0.14);
  --text: rgba(255, 255, 255, 0.92);
  --muted: rgba(255, 255, 255, 0.70);
  --muted2: rgba(255, 255, 255, 0.55);
  --shadow: 0 18px 70px rgba(0, 0, 0, 0.45);

  min-height: 100vh;
  width: 100%;
  display: flex;
  align-items: center;        /* ← vertikal zentriert */
  justify-content: center;    /* ← horizontal zentriert */
  padding: 0;                 /* ← kein Offset mehr */

  color: var(--text);
  background:
      radial-gradient(1200px 600px at 20% 10%, rgba(99, 102, 241, 0.35), transparent 60%),
      radial-gradient(900px 500px at 90% 20%, rgba(16, 185, 129, 0.25), transparent 65%),
      radial-gradient(900px 500px at 50% 95%, rgba(236, 72, 153, 0.20), transparent 55%),
      linear-gradient(180deg, #070a14, var(--bg));
  overflow: hidden;
}

/* Decorative orbs */
.bg-orb {
  position: absolute;
  filter: blur(45px);
  opacity: 0.85;
  transform: translateZ(0);
  pointer-events: none;
}
.orb-1 {
  width: 420px;
  height: 420px;
  background: radial-gradient(circle at 30% 30%, rgba(99, 102, 241, 0.75), transparent 60%);
  top: -110px;
  left: -110px;
}
.orb-2 {
  width: 520px;
  height: 520px;
  background: radial-gradient(circle at 30% 30%, rgba(16, 185, 129, 0.55), transparent 60%);
  top: -160px;
  right: -160px;
}
.orb-3 {
  width: 560px;
  height: 560px;
  background: radial-gradient(circle at 30% 30%, rgba(236, 72, 153, 0.35), transparent 60%);
  bottom: -240px;
  left: 10%;
}

.shell {
  max-width: 720px;
  width: 100%;
  transform: translateY(-2vh);
}


.header {
  margin: 0 0 14px;
  padding: 18px 18px 12px;
  border: 1px solid transparent;
}
.badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 6px 10px;
  border-radius: 999px;
  border: 1px solid var(--border);
  background: rgba(255, 255, 255, 0.06);
  color: var(--muted);
  font-size: 0.85rem;
  letter-spacing: 0.2px;
}

h1 {
  margin: 10px 0 6px;
  font-size: clamp(1.6rem, 3.2vw, 2.1rem);
  line-height: 1.15;
  letter-spacing: -0.02em;
}

.subtitle {
  margin: 0;
  color: var(--muted);
  font-size: 0.98rem;
  line-height: 1.45;
}

.card {
  background: linear-gradient(180deg, var(--card), var(--card2));
  border: 1px solid var(--border);
  border-radius: 18px;
  box-shadow: var(--shadow);
  padding: 16px;
  backdrop-filter: blur(14px);
  -webkit-backdrop-filter: blur(14px);
}

.footer {
  margin-top: 10px;
  display: flex;
  align-items: center;
  gap: 10px;
  color: var(--muted2);
  font-size: 0.9rem;
  padding: 10px 6px 0;
}

.dot {
  width: 7px;
  height: 7px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.45);
}
</style>
