<script setup>
import UserBadgeCard from './UserBadgeCard.vue'

const navItems = [
  { to: '/', icon: 'table-cells-large', label: 'Tableau de bord' },
  { to: '/voyages', icon: 'book', label: 'Tous les Voyages' },
  { to: '/souvenirs/nouveau', icon: 'bullseye', label: 'Ajouter un Souvenir' },
  { to: '/carte', icon: 'location-dot', label: 'Carte des Souvenirs' },
  { to: '/statistiques', icon: 'chart-line', label: 'Statistiques Globales' }
]
</script>

<template>
  <aside class="barre-laterale">
    <div class="barre-laterale-marque">
      <img src="../assets/LogoWandr.png" alt="Logo Wandr" class="barre-laterale-logo" />
      <div>
        <p class="barre-laterale-nom">Wandr</p>
        <p class="barre-laterale-signature">Carnets de voyages</p>
      </div>
    </div>

    <router-link to="/voyages/nouveau" class="barre-laterale-action">
      <font-awesome-icon icon="plus" />
      <span>Créer un voyage</span>
    </router-link>

    <nav class="barre-laterale-navigation">
      <router-link
        v-for="item in navItems"
        :key="item.to"
        :to="item.to"
        class="barre-laterale-lien"
        active-class="barre-laterale-lien-actif"
      >
        <font-awesome-icon :icon="item.icon" fixed-width />
        <span>{{ item.label }}</span>
      </router-link>
    </nav>

    <UserBadgeCard class="barre-laterale-utilisateur" />
  </aside>
</template>

<style scoped>
.barre-laterale {
  width: var(--sidebar-width, 260px);
  flex-shrink: 0;
  height: 100vh;
  position: sticky;
  top: 0;
  background: var(--color-sidebar);
  border-right: 1px solid var(--color-border);
  padding: 24px 20px;
  display: flex;
  flex-direction: column;
}

.barre-laterale-marque {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 24px;
}

.barre-laterale-logo {
  width: 34px;
  height: 34px;
  border-radius: 9px;
  flex-shrink: 0;
}

.barre-laterale-nom {
  font-family: var(--font-display);
  font-size: 1.05rem;
  color: var(--color-text);
  line-height: 1.1;
}

.barre-laterale-signature {
  font-size: 0.62rem;
  letter-spacing: 0.04em;
  color: var(--color-text-muted);
  margin-top: 2px;
}

.barre-laterale-action {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 11px 14px;
  border-radius: var(--radius-sm);
  background: var(--color-accent);
  color: #fff;
  font-size: 0.85rem;
  font-weight: 600;
  margin-bottom: 22px;
  transition: background 0.15s ease;
}

.barre-laterale-action:hover {
  background: var(--color-accent-dark);
}

.barre-laterale-navigation {
  display: flex;
  flex-direction: column;
  gap: 2px;
  flex: 1;
}

.barre-laterale-lien {
  display: flex;
  align-items: center;
  gap: 11px;
  padding: 10px 12px;
  border-radius: var(--radius-sm);
  font-size: 0.85rem;
  color: var(--color-text-muted);
  transition: background 0.15s ease, color 0.15s ease;
}

.barre-laterale-lien:hover {
  background: var(--color-accent-tint);
  color: var(--color-text);
}

.barre-laterale-lien-actif {
  background: var(--color-accent-tint);
  color: var(--color-accent-dark);
  font-weight: 600;
}

.barre-laterale-utilisateur {
  margin-top: 18px;
}
</style>