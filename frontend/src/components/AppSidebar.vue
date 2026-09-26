<script setup>
import UserBadgeCard from './UserBadgeCard.vue'

const navItems = [
  { to: '/', icon: 'table-cells-large', label: 'Tableau de bord' },
  { to: '/voyages', icon: 'book', label: 'Tous les Voyages' },
  { to: '/voyages/1', icon: 'eye', label: "Détail d'un voyage" },
  { to: '/souvenirs/nouveau', icon: 'bullseye', label: 'Ajouter un Souvenir' },
  { to: '/carte', icon: 'location-dot', label: 'Carte des Souvenirs' },
  { to: '/statistiques', icon: 'chart-line', label: 'Statistiques Globales' }
]
</script>

<template>
  <aside class="barre-laterale">
    <div class="identite-marque">
      <img src="../assets/LogoWandr.png" alt="Logo Wandr" class="sidebar-logo"/>
      <div>
        <p class="nom-marque">Wandr</p>
        <p class="slogan-marque">Carnets de voyages</p>
      </div>
    </div>

    <router-link to="/souvenirs/nouveau" class="bouton-creer-voyage">
      <font-awesome-icon icon="plus" />
      <span>Créer un voyage</span>
    </router-link>

    <nav class="navigation-principale">
      <router-link
        v-for="item in navItems"
        :key="item.to"
        :to="item.to"
        class="lien-navigation"
        active-class="lien-navigation--actif"
      >
        <font-awesome-icon :icon="item.icon" fixed-width />
        <span>{{ item.label }}</span>
      </router-link>
    </nav>

    <UserBadgeCard class="profil-utilisateur" />
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

.identite-marque {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 24px;
}

.sidebar-logo {
  width: 34px;
  height: 34px;
  border-radius: 9px;
  object-fit: cover;
  flex-shrink: 0;
}

.nom-marque {
  font-family: var(--font-display);
  font-size: 1.05rem;
  color: var(--color-text);
  line-height: 1.1;
}

.slogan-marque {
  font-size: 0.62rem;
  letter-spacing: 0.04em;
  color: var(--color-text-muted);
  margin-top: 2px;
}

.bouton-creer-voyage {
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

.bouton-creer-voyage:hover {
  background: var(--color-accent-dark);
}

.navigation-principale {
  display: flex;
  flex-direction: column;
  gap: 2px;
  flex: 1;
}

.lien-navigation {
  display: flex;
  align-items: center;
  gap: 11px;
  padding: 10px 12px;
  border-radius: var(--radius-sm);
  font-size: 0.85rem;
  color: var(--color-text-muted);
  transition: background 0.15s ease, color 0.15s ease;
}

.lien-navigation:hover {
  background: var(--color-accent-tint);
  color: var(--color-text);
}

.lien-navigation--actif {
  background: var(--color-accent-tint);
  color: var(--color-accent-dark);
  font-weight: 600;
}

.profil-utilisateur {
  margin-top: 18px;
}
</style>