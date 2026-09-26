<template>
  <header class="barre-superieure">
    <label class="barre-superieure-recherche">
      <font-awesome-icon icon="magnifying-glass" />
      <input type="text" placeholder="Rechercher un souvenir..." />
    </label>

    <button class="barre-superieure-notifications" type="button" aria-label="Notifications">
      <font-awesome-icon icon="bell" />
    </button>
  </header>
</template>

<style scoped>
.barre-superieure {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
}

.barre-superieure-recherche {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 9px 14px;
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: 999px;
  color: var(--color-text-muted);
}

.barre-superieure-recherche input {
  border: none;
  background: transparent;
  font-family: var(--font-body);
  font-size: 0.82rem;
  width: 190px;
  color: var(--color-text);
}

.barre-superieure-recherche input:focus {
  outline: none;
}

.barre-superieure-recherche input::placeholder {
  color: var(--color-text-muted);
}

.barre-superieure-notifications {
  width: 38px;
  height: 38px;
  border-radius: 50%;
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--color-text);
  transition: background 0.15s ease;
}

.barre-superieure-notifications:hover {
  background: var(--color-accent-tint);
}
</style>