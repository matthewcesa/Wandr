<script setup>
import { ref, computed, onMounted } from 'vue'
import { getTripsByUser } from '../services/trips'
import { getMemoriesByTrip } from '../services/memories'
import { useCurrentUser } from '../composable/useCurrentUser'

const currentUser = useCurrentUser()

const trips = ref([])
const memoriesCountByTrip = ref({})
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

const tripsCount = computed(() => trips.value.length)

const countriesCount = computed(() => {
  const countries = new Set(trips.value.map((t) => t.country).filter(Boolean))
  return countries.size
})

const totalMemories = computed(() =>
  Object.values(memoriesCountByTrip.value).reduce((sum, n) => sum + n, 0)
)

const totalDays = computed(() =>
  trips.value.reduce((sum, trip) => {
    if (!trip.startDate || !trip.endDate) return sum
    const diff = (new Date(trip.endDate) - new Date(trip.startDate)) / (1000 * 60 * 60 * 24)
    return sum + Math.max(0, Math.round(diff) + 1)
  }, 0)
)

const recentTrips = computed(() =>
  [...trips.value]
    .sort((a, b) => new Date(b.startDate) - new Date(a.startDate))
    .slice(0, 4)
)

function formatDateRange(start, end) {
  if (!start || !end) return '—'
  const options = { day: 'numeric', month: 'short' }
  return `${new Date(start).toLocaleDateString('fr-FR', options)} - ${new Date(end).toLocaleDateString('fr-FR', options)}`
}


onMounted(async () => {
  try {
    trips.value = await getTripsByUser(currentUser.user.userId)

    const counts = await Promise.all(
      trips.value.map(async (trip) => {
        const memories = await getMemoriesByTrip(trip.tripId)
        return [trip.tripId, memories.length]
      })
    )
    memoriesCountByTrip.value = Object.fromEntries(counts)
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
      <h1>Bonjour, {{ currentUser.user?.name?.split(' ')[0] || '...' }}</h1>
      <p>Prêt à immortaliser vos escales et retracer vos doux souvenirs ?</p>
    </header>

    <p v-if="isLoading" class="contenu-en-attente">Chargement du tableau de bord...</p>
    <p v-else-if="errorMessage" class="contenu-en-attente">Erreur : {{ errorMessage }}</p>

    <template v-else>
      <div class="grille-statistiques">
        <div class="carte-statistique">
          <p class="carte-statistique-libelle">Voyages réalisés</p>
          <p class="carte-statistique-valeur">{{ tripsCount }}</p>
        </div>
        <div class="carte-statistique">
          <p class="carte-statistique-libelle">Pays explorés</p>
          <p class="carte-statistique-valeur">{{ countriesCount }}</p>
        </div>
        <div class="carte-statistique">
          <p class="carte-statistique-libelle">Souvenirs notés</p>
          <p class="carte-statistique-valeur">{{ totalMemories }}</p>
        </div>
        <div class="carte-statistique">
          <p class="carte-statistique-libelle">Jours d'aventure</p>
          <p class="carte-statistique-valeur">{{ totalDays }}</p>
        </div>
      </div>

      <div class="carte carte-derniers-voyages">
        <div class="carte-derniers-voyages-entete">
          <h2>Mes Carnets Récents</h2>
          <router-link to="/voyages" class="carte-derniers-voyages-lien">Voir tous les voyages →</router-link>
        </div>

        <p v-if="recentTrips.length === 0" class="contenu-en-attente">
          Aucun voyage pour l'instant.
          <router-link to="/voyages/nouveau">Crée ton premier voyage</router-link>.
        </p>

        <table v-else class="tableau-derniers-voyages">
          <thead>
            <tr>
              <th>Voyage & Pays</th>
              <th>Dates</th>
              <th>Contenu</th>
              <th>Statut</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="trip in recentTrips"
              :key="trip.tripId"
              class="tableau-derniers-voyages-ligne"
              @click="$router.push(`/voyages/${trip.tripId}`)"
            >
              <td>
                <p class="tableau-derniers-voyages-titre">{{ trip.title }}</p>
                <p class="tableau-derniers-voyages-pays">{{ trip.country }}</p>
              </td>
              <td>{{ formatDateRange(trip.startDate, trip.endDate) }}</td>
              <td>{{ memoriesCountByTrip[trip.tripId] || 0 }} souvenirs</td>
              <td>
                <span class="tableau-derniers-voyages-statut" :class="`tableau-derniers-voyages-statut--${statusClassNames[trip.status] || trip.status}`">
                  {{ statusLabels[trip.status] || trip.status }}
                </span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </template>
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

.grille-statistiques {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.carte-statistique {
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  padding: 16px 18px;
}

.carte-statistique-libelle {
  font-size: 0.78rem;
  color: var(--color-text-muted);
  margin-bottom: 6px;
}

.carte-statistique-valeur {
  font-family: var(--font-display);
  font-size: 1.7rem;
  color: var(--color-text);
}

.carte-derniers-voyages {
  padding: 20px;
}

.carte-derniers-voyages-entete {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
}

.carte-derniers-voyages-entete h2 {
  font-size: 1.1rem;
}

.carte-derniers-voyages-lien {
  font-size: 0.8rem;
  color: var(--color-accent);
  font-weight: 600;
}

.tableau-derniers-voyages {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.85rem;
}

.tableau-derniers-voyages th {
  text-align: left;
  font-size: 0.72rem;
  text-transform: uppercase;
  letter-spacing: 0.02em;
  color: var(--color-text-muted);
  padding-bottom: 10px;
  border-bottom: 1px solid var(--color-border);
}

.tableau-derniers-voyages td {
  padding: 12px 0;
  border-bottom: 1px solid var(--color-border);
  vertical-align: middle;
}

.tableau-derniers-voyages-ligne {
  cursor: pointer;
}

.tableau-derniers-voyages-ligne:hover td {
  background: var(--color-accent-tint);
}

.tableau-derniers-voyages-titre {
  font-weight: 600;
  color: var(--color-text);
}

.tableau-derniers-voyages-pays {
  font-size: 0.75rem;
  color: var(--color-text-muted);
  margin-top: 2px;
}

.tableau-derniers-voyages-statut {
  padding: 3px 9px;
  border-radius: 999px;
  font-size: 0.72rem;
  font-weight: 600;
}

.tableau-derniers-voyages-statut--en-cours {
  background: var(--status-progress-bg);
  color: var(--status-progress-text);
}

.tableau-derniers-voyages-statut--termine {
  background: var(--status-done-bg);
  color: var(--status-done-text);
}

.tableau-derniers-voyages-statut--prevu {
  background: var(--status-upcoming-bg);
  color: var(--status-upcoming-text);
}
</style>