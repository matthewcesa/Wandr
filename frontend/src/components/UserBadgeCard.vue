<script setup>
import { useRouter } from 'vue-router'
import { useCurrentUser, logout } from '../composable/useCurrentUser'

const router = useRouter()
const currentUser = useCurrentUser()

function handleLogout() {
  logout()
  router.push('/connexion')
}
</script>

<template>
  <div class="carte-utilisateur">
    <div class="carte-utilisateur-avatar" aria-hidden="true"></div>

    <div class="carte-utilisateur-identite">
      <p class="carte-utilisateur-nom">{{ currentUser.user?.name || '...' }}</p>
      <p class="carte-utilisateur-courriel">{{ currentUser.user?.email }}</p>
    </div>

    <button type="button" class="carte-utilisateur-deconnexion" title="Se déconnecter" @click="handleLogout">
      <font-awesome-icon icon="right-from-bracket" />
    </button>
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
  flex: 1;
}

.carte-utilisateur-nom {
  font-size: 0.85rem;
  font-weight: 600;
  color: var(--color-text);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.carte-utilisateur-courriel {
  font-size: 0.72rem;
  color: var(--color-text-muted);
  margin-top: 1px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.carte-utilisateur-deconnexion {
  background: none;
  color: var(--color-text-muted);
  flex-shrink: 0;
  padding: 4px;
  border-radius: var(--radius-sm);
}

.carte-utilisateur-deconnexion:hover {
  color: var(--color-accent-dark);
  background: var(--color-accent-tint);
}
</style>