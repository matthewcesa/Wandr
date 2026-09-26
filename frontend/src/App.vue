<script setup>
import AppSidebar from './components/AppSidebar.vue'
import AppTopbar from './components/AppTopbar.vue'
</script>

<template>
  <div class="mise-en-page">
    <AppSidebar />

    <main class="mise-en-page-contenu">
      <AppTopbar />
      <router-view />
    </main>
  </div>
</template>

<style>
:root {
  --color-bg: #f3eee4;
  --color-sidebar: #faf7f1;
  --color-surface: #ffffff;
  --color-border: #e8e0d1;
  --color-text: #2b2822;
  --color-text-muted: #8c8575;
  --color-accent: #b97a54;
  --color-accent-dark: #9c6544;
  --color-accent-tint: #f1e2d5;

  --status-progress-bg: #ece4c8;
  --status-progress-text: #7a6d3a;
  --status-done-bg: #dde6d6;
  --status-done-text: #4d6b41;
  --status-upcoming-bg: #f4ded0;
  --status-upcoming-text: #a05a34;

  --font-display: 'Fraunces', Georgia, serif;
  --font-body: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;

  --radius-md: 12px;
  --radius-sm: 8px;
  --shadow-card: 0 1px 2px rgba(43, 40, 34, 0.04);
}

* {
  box-sizing: border-box;
}

html, body {
  margin: 0;
  padding: 0;
}

body {
  font-family: var(--font-body);
  background: var(--color-bg);
  color: var(--color-text);
  -webkit-font-smoothing: antialiased;
}

h1, h2, h3 {
  font-family: var(--font-display);
  margin: 0;
  color: var(--color-text);
  font-weight: 500;
}

p {
  margin: 0;
}

a {
  text-decoration: none;
  color: inherit;
}

button {
  font-family: var(--font-body);
  cursor: pointer;
  border: none;
}

ul {
  list-style: none;
  margin: 0;
  padding: 0;
}

a:focus-visible,
button:focus-visible,
input:focus-visible {
  outline: 2px solid var(--color-accent);
  outline-offset: 2px;
}

.contenu-en-attente {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 300px;
  color: var(--color-text-muted);
  font-size: 0.95rem;
  border: 1px dashed var(--color-border);
  border-radius: var(--radius-md);
  background: var(--color-surface);
}
</style>

<style scoped>
.mise-en-page {
  display: flex;
  min-height: 100vh;
}

.mise-en-page-contenu {
  flex: 1;
  padding: 28px 32px 48px;
  min-width: 0;
}
</style>