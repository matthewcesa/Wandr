<script setup>
import { ref, onMounted } from 'vue'
import { getTripsByUser } from '../services/trips'
import { useCurrentUser } from '../composable/useCurrentUser'

const currentUser = useCurrentUser()

const trips = ref([])
const isLoading = ref(true)
const errorMessage = ref('')

const statusLabels = {
  planned: 'À venir',
  ongoing: 'En cours',
  completed: 'Terminé'
}

const statusClassNames = {
  planned: 'prevu',
  ongoing: 'en-cours',
  completed: 'termine'
}

function formatDateRange(start, end) {
  const options = { day: 'numeric', month: 'short' }
  const startLabel = new Date(start).toLocaleDateString('fr-FR', options)
  const endLabel = new Date(end).toLocaleDateString('fr-FR', options)
  return `${startLabel} - ${endLabel}`
}

onMounted(async () => {
  try {
    trips.value = await getTripsByUser(currentUser.user.userId)
  } catch (err) {
    errorMessage.value = err.message
  } finally {
    isLoading.value = false
  }
})
</script>

<template>
  <div>
    <header class="entete-page">
      <h1>Tous mes Voyages</h1>
      <p>L'intégralité de vos carnets, récits et explorations mémorables.</p>
    </header>

    <p v-if="isLoading" class="contenu-en-attente">Chargement des voyages...</p>

    <p v-else-if="errorMessage" class="contenu-en-attente">
      Impossible de charger les voyages : {{ errorMessage }}
    </p>

    <p v-else-if="trips.length === 0" class="contenu-en-attente">
      Aucun voyage pour l'instant.
    </p>

    <div v-else class="grille-voyages">
      <router-link
        v-for="trip in trips"
        :key="trip.tripId"
        :to="`/voyages/${trip.tripId}`"
        class="carte-voyage"
      >
        <img v-if="trip.cover" :src="trip.cover" :alt="trip.title" class="carte-voyage-couverture" />

        <div class="carte-voyage-contenu">
          <div class="carte-voyage-metadonnees">
            <span class="carte-voyage-pays">{{ trip.country }}</span>
            <span class="carte-voyage-statut" :class="`carte-voyage-statut--${statusClassNames[trip.status] || trip.status}`">
              {{ statusLabels[trip.status] || trip.status }}
            </span>
          </div>

          <h2 class="carte-voyage-titre">{{ trip.title }}</h2>
          <p class="carte-voyage-dates">{{ formatDateRange(trip.startDate, trip.endDate) }}</p>
        </div>
      </router-link>
    </div>
  </div>
</template>

<style scoped>
.entete-page {
  margin-bottom: 24px;
}

.entete-page h1 {
  font-size: 1.6rem;
}

.entete-page p {
  margin-top: 6px;
  font-size: 0.88rem;
  color: var(--color-text-muted);
  max-width: 46ch;
}

.grille-voyages {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 20px;
}

.carte-voyage {
  display: block;
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  overflow: hidden;
  transition: box-shadow 0.15s ease;
}

.carte-voyage:hover {
  box-shadow: 0 4px 14px rgba(43, 40, 34, 0.08);
}

.carte-voyage-couverture {
  width: 100%;
  height: 140px;
  object-fit: cover;
  display: block;
  background: var(--color-accent-tint);
}

.carte-voyage-contenu {
  padding: 16px;
}

.carte-voyage-metadonnees {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.7rem;
  text-transform: uppercase;
  letter-spacing: 0.03em;
  margin-bottom: 8px;
}

.carte-voyage-pays {
  color: var(--color-text-muted);
}

.carte-voyage-statut {
  padding: 3px 9px;
  border-radius: 999px;
  font-weight: 600;
  text-transform: none;
}

.carte-voyage-statut--en-cours {
  background: var(--status-progress-bg);
  color: var(--status-progress-text);
}

.carte-voyage-statut--termine {
  background: var(--status-done-bg);
  color: var(--status-done-text);
}

.carte-voyage-statut--prevu {
  background: var(--status-upcoming-bg);
  color: var(--status-upcoming-text);
}

.carte-voyage-titre {
  font-size: 1.05rem;
  margin-bottom: 4px;
}

.carte-voyage-dates {
  font-size: 0.8rem;
  color: var(--color-text-muted);
}
</style>