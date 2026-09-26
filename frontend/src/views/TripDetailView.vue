<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getTripById } from '../services/trips'
import { getMemoriesByTrip } from '../services/memories'
import { getExpensesByTrip } from '../services/expenses'

const route = useRoute()

const trip = ref(null)
const memories = ref([])
const expenses = ref([])
const isLoading = ref(true)
const errorMessage = ref('')
const activeFilter = ref('all')

const statusLabels = {
  planned: 'À venir',
  ongoing: 'En cours',
  completed: 'Terminé'
}

const typeLabels = {
  meal: 'Repas',
  activity: 'Activité',
  accommodation: 'Hébergement',
  place: 'Lieu'
}

const filters = computed(() => [
  { key: 'all', label: `Tous les souvenirs (${memories.value.length})` },
  { key: 'meal', label: `Repas & Cafés (${memories.value.filter((m) => m.type === 'meal').length})` },
  { key: 'other', label: `Activités (${memories.value.filter((m) => m.type !== 'meal').length})` }
])

const filteredMemories = computed(() => {
  if (activeFilter.value === 'all') return memories.value
  if (activeFilter.value === 'meal') return memories.value.filter((m) => m.type === 'meal')
  return memories.value.filter((m) => m.type !== 'meal')
})

const totalSpent = computed(() =>
  expenses.value.reduce((sum, e) => sum + Number(e.amount || 0), 0)
)

const spentByCategory = computed(() => {
  const map = {}
  for (const expense of expenses.value) {
    const name = expense.expenseCategory?.name || 'Autre'
    map[name] = (map[name] || 0) + Number(expense.amount || 0)
  }
  return Object.entries(map)
    .map(([name, amount]) => ({ name, amount }))
    .sort((a, b) => b.amount - a.amount)
})

function formatCurrency(value) {
  return new Intl.NumberFormat('fr-FR', { style: 'currency', currency: 'EUR' }).format(value || 0)
}

function formatDateRange(start, end) {
  if (!start || !end) return '—'
  const options = { day: 'numeric', month: 'short', year: 'numeric' }
  return `${new Date(start).toLocaleDateString('fr-FR', options)} au ${new Date(end).toLocaleDateString('fr-FR', options)}`
}

function formatDate(date) {
  if (!date) return ''
  return new Date(date).toLocaleDateString('fr-FR', { day: 'numeric', month: 'short', year: 'numeric' })
}

async function loadTrip(tripId) {
  isLoading.value = true
  errorMessage.value = ''
  try {
    const [tripData, memoriesData, expensesData] = await Promise.all([
      getTripById(tripId),
      getMemoriesByTrip(tripId),
      getExpensesByTrip(tripId)
    ])
    trip.value = tripData
    memories.value = memoriesData
    expenses.value = expensesData
  } catch (err) {
    errorMessage.value = err.message
  } finally {
    isLoading.value = false
  }
}

onMounted(() => loadTrip(route.params.id))
watch(() => route.params.id, (newId) => loadTrip(newId))
</script>

<template>
  <div>
    <p v-if="isLoading" class="contenu-en-attente">Chargement du voyage...</p>
    <p v-else-if="errorMessage" class="contenu-en-attente">Erreur : {{ errorMessage }}</p>

    <template v-else-if="trip">
      <div
        class="bandeau-voyage"
        :style="trip.cover ? { backgroundImage: `url(${trip.cover})` } : {}"
      >
        <div class="bandeau-voyage-voile">
          <p class="bandeau-voyage-pays">{{ trip.country }}</p>
          <h1 class="bandeau-voyage-titre">{{ trip.title }}</h1>
          <p class="bandeau-voyage-dates">{{ formatDateRange(trip.startDate, trip.endDate) }}</p>

          <div class="bandeau-voyage-statistiques">
            <div>
              <p class="bandeau-voyage-statistique-libelle">Budget dépensé</p>
              <p class="bandeau-voyage-statistique-valeur">{{ formatCurrency(totalSpent) }}</p>
            </div>
            <div>
              <p class="bandeau-voyage-statistique-libelle">Souvenirs</p>
              <p class="bandeau-voyage-statistique-valeur">{{ memories.length }}</p>
            </div>
            <div>
              <p class="bandeau-voyage-statistique-libelle">Statut</p>
              <p class="bandeau-voyage-statistique-valeur">{{ statusLabels[trip.status] || trip.status }}</p>
            </div>
          </div>
        </div>
      </div>

      <div class="mise-en-page-voyage">
        <div class="contenu-principal-voyage">
          <div class="onglets-voyage">
            <div class="onglets-voyage-liste">
              <button
                v-for="filter in filters"
                :key="filter.key"
                class="onglets-voyage-onglet"
                :class="{ 'onglets-voyage-onglet-actif': activeFilter === filter.key }"
                type="button"
                @click="activeFilter = filter.key"
              >
                {{ filter.label }}
              </button>
            </div>

            <router-link
              :to="{ path: '/souvenirs/nouveau', query: { tripId: trip.tripId } }"
              class="onglets-voyage-ajouter"
            >
              <font-awesome-icon icon="plus" />
              <span>Ajouter un souvenir</span>
            </router-link>
          </div>

          <p v-if="filteredMemories.length === 0" class="contenu-en-attente">
            Aucun souvenir dans cette catégorie pour l'instant.
          </p>

          <div v-else class="liste-souvenirs">
            <router-link
              v-for="memory in filteredMemories"
              :key="memory.memoryId"
              :to="`/voyages/${trip.tripId}/souvenirs/${memory.memoryId}`"
              class="carte-souvenir carte"
            >
              <div class="carte-souvenir-entete">
                <span class="carte-souvenir-type">{{ typeLabels[memory.type] || memory.type }}</span>
                <span v-if="memory.isFavorite" class="carte-souvenir-favori">♥ Coup de cœur</span>
              </div>

              <h3 class="carte-souvenir-titre">{{ memory.title }}</h3>
              <p class="carte-souvenir-description">{{ memory.description }}</p>

              <div class="carte-souvenir-pied">
                <span>{{ memory.address }}</span>
                <span>{{ formatDate(memory.visitedAt) }}</span>
              </div>
            </router-link>
          </div>
        </div>

        <aside class="barre-laterale-voyage">
          <div class="carte carte-budget">
            <h2 class="carte-budget-titre">Budget</h2>

            <p class="carte-budget-montants">
              <span class="carte-budget-depense">{{ formatCurrency(totalSpent) }}</span>
              <span class="carte-budget-prevu">
                sur {{ formatCurrency(trip.plannedBudget) }} prévus
              </span>
            </p>

            <div class="carte-budget-jauge">
              <div
                class="carte-budget-remplissage"
                :style="{ width: Math.min(100, (totalSpent / (trip.plannedBudget || 1)) * 100) + '%' }"
              ></div>
            </div>

            <ul v-if="spentByCategory.length > 0" class="carte-budget-liste">
              <li v-for="cat in spentByCategory" :key="cat.name" class="carte-budget-element">
                <span>{{ cat.name }}</span>
                <span>{{ formatCurrency(cat.amount) }}</span>
              </li>
            </ul>

            <p v-else class="carte-budget-vide">Aucune dépense enregistrée.</p>
          </div>
        </aside>
      </div>
    </template>
  </div>
</template>

<style scoped>
.bandeau-voyage {
  height: 220px;
  border-radius: var(--radius-md);
  background-color: var(--color-accent-tint);
  background-size: cover;
  background-position: center;
  position: relative;
  margin-bottom: 24px;
  overflow: hidden;
}

.bandeau-voyage-voile {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, rgba(20, 16, 10, 0.1), rgba(20, 16, 10, 0.65));
  color: #fff;
  padding: 20px 24px;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
}

.bandeau-voyage-pays {
  font-size: 0.72rem;
  letter-spacing: 0.05em;
  text-transform: uppercase;
  opacity: 0.85;
}

.bandeau-voyage-titre {
  font-size: 1.8rem;
  color: #fff;
  margin: 4px 0;
}

.bandeau-voyage-dates {
  font-size: 0.85rem;
  opacity: 0.9;
}

.bandeau-voyage-statistiques {
  display: flex;
  gap: 28px;
  margin-top: 14px;
}

.bandeau-voyage-statistique-libelle {
  font-size: 0.68rem;
  text-transform: uppercase;
  opacity: 0.75;
}

.bandeau-voyage-statistique-valeur {
  font-weight: 600;
  font-size: 0.95rem;
}

.mise-en-page-voyage {
  display: grid;
  grid-template-columns: 1fr 280px;
  gap: 24px;
  align-items: start;
}

.onglets-voyage {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid var(--color-border);
  margin-bottom: 20px;
  padding-bottom: 10px;
}

.onglets-voyage-liste {
  display: flex;
  gap: 18px;
}

.onglets-voyage-onglet {
  background: none;
  font-size: 0.85rem;
  color: var(--color-text-muted);
  padding: 6px 0;
}

.onglets-voyage-onglet-actif {
  color: var(--color-accent-dark);
  font-weight: 600;
  border-bottom: 2px solid var(--color-accent);
}

.onglets-voyage-ajouter {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 14px;
  background: var(--color-accent);
  color: #fff;
  border-radius: var(--radius-sm);
  font-size: 0.82rem;
  font-weight: 600;
}

.onglets-voyage-ajouter:hover {
  background: var(--color-accent-dark);
}

.liste-souvenirs {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.carte-souvenir {
  display: block;
  padding: 18px;
  transition: box-shadow 0.15s ease;
}

.carte-souvenir:hover {
  box-shadow: 0 4px 14px rgba(43, 40, 34, 0.08);
}

.carte-souvenir-entete {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.carte-souvenir-type {
  font-size: 0.68rem;
  text-transform: uppercase;
  letter-spacing: 0.03em;
  background: var(--color-accent-tint);
  color: var(--color-accent-dark);
  padding: 3px 9px;
  border-radius: 999px;
  font-weight: 600;
}

.carte-souvenir-favori {
  font-size: 0.78rem;
  color: #b3413a;
}

.carte-souvenir-titre {
  font-size: 1.05rem;
  margin-bottom: 6px;
  color: var(--color-text);
}

.carte-souvenir-description {
  font-size: 0.85rem;
  color: var(--color-text-muted);
  margin-bottom: 10px;
}

.carte-souvenir-pied {
  display: flex;
  justify-content: space-between;
  font-size: 0.75rem;
  color: var(--color-text-muted);
}

.carte-budget {
  padding: 18px;
}

.carte-budget-titre {
  font-size: 1rem;
  margin-bottom: 12px;
}

.carte-budget-montants {
  display: flex;
  align-items: baseline;
  gap: 6px;
  margin-bottom: 8px;
}

.carte-budget-depense {
  font-family: var(--font-display);
  font-size: 1.3rem;
  color: var(--color-text);
}

.carte-budget-prevu {
  font-size: 0.75rem;
  color: var(--color-text-muted);
}

.carte-budget-jauge {
  height: 6px;
  border-radius: 999px;
  background: var(--color-accent-tint);
  overflow: hidden;
  margin-bottom: 16px;
}

.carte-budget-remplissage {
  height: 100%;
  background: var(--color-accent);
}

.carte-budget-liste {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.carte-budget-element {
  display: flex;
  justify-content: space-between;
  font-size: 0.8rem;
  color: var(--color-text);
}

.carte-budget-vide {
  font-size: 0.8rem;
  color: var(--color-text-muted);
}
</style>