<script setup>
defineProps({
  name: {
    type: String,
    default: 'Utilisateur backend '
  },
  title: {
    type: String,
    default: 'Titre backend'
  }
})
</script>

<template>
  <div class="carte-utilisateur">
    <div class="carte-utilisateur-avatar" aria-hidden="true"></div>
    <div class="carte-utilisateur-identite">
      <p class="carte-utilisateur-nom">{{ name }}</p>
      <p class="carte-utilisateur-role">{{ title }}</p>
    </div>
  </div>
</template>

<style scoped>
.carte-utilisateur {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 14px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  background: var(--color-surface);
}

.carte-utilisateur-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: linear-gradient(160deg, var(--color-accent), var(--color-accent-dark));
  flex-shrink: 0;
}

.carte-utilisateur-identite {
  min-width: 0;
}

.carte-utilisateur-nom {
  font-size: 0.85rem;
  font-weight: 600;
  color: var(--color-text);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.carte-utilisateur-role {
  font-size: 0.75rem;
  color: var(--color-accent);
  margin-top: 1px;
}
</style>