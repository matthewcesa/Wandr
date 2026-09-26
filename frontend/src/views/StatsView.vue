<script setup>
import { ref, computed, onMounted } from 'vue'
import { getTripsByUser } from '../services/trips'
import { getExpensesByTrip } from '../services/expenses'
import { useCurrentUser } from '../composable/useCurrentUser'

const currentUser = useCurrentUser()

const trips = ref([])
const allExpenses = ref([])
const isLoading = ref(true)
const errorMessage = ref('')

const totalSpent = computed(() =>
  allExpenses.value.reduce((sum, e) => sum + Number(e.amount || 0), 0)
)

const totalPlanned = computed(() =>
  trips.value.reduce((sum, t) => sum + Number(t.plannedBudget || 0), 0)
)

const countriesCount = computed(() => {
  const set = new Set(trips.value.map((t) => t.country).filter(Boolean))
  return set.size
})

const ratedTrips = computed(() => trips.value.filter((t) => t.rating != null))

const averageRating = computed(() => {
  if (ratedTrips.value.length === 0) return null
  const sum = ratedTrips.value.reduce((acc, t) => acc + Number(t.rating), 0)
  return (sum / ratedTrips.value.length).toFixed(2)
})

const favoriteTrip = computed(() => {
  if (ratedTrips.value.length === 0) return null
  return [...ratedTrips.value].sort((a, b) => Number(b.rating) - Number(a.rating))[0]
})

const spentByCategory = computed(() => {
  const map = {}
  for (const expense of allExpenses.value) {
    const name = expense.expenseCategory?.name || 'Autre'
    map[name] = (map[name] || 0) + Number(expense.amount || 0)
  }
  const total = totalSpent.value || 1
  return Object.entries(map)
    .map(([name, amount]) => ({ name, amount, percent: Math.round((amount / total) * 100) }))
    .sort((a, b) => b.amount - a.amount)
})

const recentExpenses = computed(() =>
  [...allExpenses.value]
    .filter((e) => e.spentAt)
    .sort((a, b) => new Date(b.spentAt) - new Date(a.spentAt))
    .slice(0, 6)
)

function formatCurrency(value) {
  return new Intl.NumberFormat('fr-FR', { style: 'currency', currency: 'EUR' }).format(value || 0)
}

function tripTitleForExpense(expense) {
  const trip = trips.value.find((t) => t.tripId === expense.trip?.tripId)
  return trip?.title || expense.trip?.title || '—'
}

onMounted(async () => {
  try {
    trips.value = await getTripsByUser(currentUser.user.userId)
    const expensesPerTrip = await Promise.all(
      trips.value.map((trip) => getExpensesByTrip(trip.tripId))
    )
    allExpenses.value = expensesPerTrip.flat()
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
      <h1>Statistiques et temps forts de mes voyages</h1>
      <p>Analysez l'historique complet et les points d'orgue de vos différents voyages.</p>
    </header>

    <p v-if="isLoading" class="contenu-en-attente">Chargement des statistiques...</p>
    <p v-else-if="errorMessage" class="contenu-en-attente">Erreur : {{ errorMessage }}</p>

    <template v-else-if="trips.length === 0">
      <p class="contenu-en-attente">
        Aucun voyage pour l'instant. <router-link to="/voyages/nouveau">Crée ton premier voyage</router-link>.
      </p>
    </template>

    <template v-else>
      <div class="grille-statistiques">
        <div class="carte-statistique">
          <p class="carte-statistique-libelle">Note globale</p>
          <p class="carte-statistique-valeur">{{ averageRating ? `${averageRating} / 5` : '—' }}</p>
          <p class="carte-statistique-complement">Sur {{ ratedTrips.length }} voyage(s) noté(s)</p>
        </div>

        <div class="carte-statistique">
          <p class="carte-statistique-libelle">Voyage préféré</p>
          <p class="carte-statistique-valeur carte-statistique-valeur-texte">{{ favoriteTrip?.title || '—' }}</p>
          <p class="carte-statistique-complement">{{ favoriteTrip ? `Note ${favoriteTrip.rating}/5` : 'Aucune note pour l\'instant' }}</p>
        </div>

        <div class="carte-statistique">
          <p class="carte-statistique-libelle">Budget total investi</p>
          <p class="carte-statistique-valeur">{{ formatCurrency(totalSpent) }}</p>
          <p class="carte-statistique-complement">Sur {{ formatCurrency(totalPlanned) }} prévus</p>
        </div>

        <div class="carte-statistique">
          <p class="carte-statistique-libelle">Pays explorés</p>
          <p class="carte-statistique-valeur">{{ countriesCount }}</p>
          <p class="carte-statistique-complement">Sur {{ trips.length }} voyage(s)</p>
        </div>
      </div>

      <div class="colonnes-statistiques">
        <div class="carte repartition-budget">
          <h2>Dépenses par catégorie</h2>

          <p v-if="spentByCategory.length === 0" class="contenu-en-attente">
            Aucune dépense enregistrée pour l'instant.
          </p>

          <template v-else>
            <div class="barre-budget">
              <div
                v-for="cat in spentByCategory"
                :key="cat.name"
                class="barre-budget-segment"
                :style="{ width: cat.percent + '%' }"
                :title="`${cat.name} : ${cat.percent}%`"
              ></div>
            </div>

            <ul class="repartition-budget-liste">
              <li v-for="cat in spentByCategory" :key="cat.name" class="repartition-budget-element">
                <span>{{ cat.name }}</span>
                <span class="repartition-budget-pourcentage">{{ cat.percent }}%</span>
                <span class="repartition-budget-montant">{{ formatCurrency(cat.amount) }}</span>
              </li>
            </ul>
          </template>
        </div>

        <div class="carte depenses-recentes">
          <h2>Dépenses récentes</h2>

          <p v-if="recentExpenses.length === 0" class="contenu-en-attente">
            Aucune dépense datée pour l'instant.
          </p>

          <ul v-else class="depenses-recentes-liste">
            <li v-for="expense in recentExpenses" :key="expense.expenseId" class="depenses-recentes-element">
              <div>
                <p class="depenses-recentes-libelle">{{ expense.label || expense.expenseCategory?.name }}</p>
                <p class="depenses-recentes-voyage">{{ tripTitleForExpense(expense) }}</p>
              </div>
              <p class="depenses-recentes-montant">{{ formatCurrency(expense.amount) }}</p>
            </li>
          </ul>
        </div>
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
}

.grille-statistiques {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
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
  font-size: 1.6rem;
  color: var(--color-text);
}

.carte-statistique-valeur-texte {
  font-size: 1.1rem;
  line-height: 1.3;
}

.carte-statistique-complement {
  font-size: 0.72rem;
  color: var(--color-text-muted);
  margin-top: 4px;
}

.colonnes-statistiques {
  display: grid;
  grid-template-columns: 1.4fr 1fr;
  gap: 20px;
}

.repartition-budget,
.depenses-recentes {
  padding: 20px;
}

.repartition-budget h2,
.depenses-recentes h2 {
  font-size: 1.05rem;
  margin-bottom: 16px;
}

.barre-budget {
  display: flex;
  height: 10px;
  border-radius: 999px;
  overflow: hidden;
  margin-bottom: 16px;
  background: var(--color-accent-tint);
}

.barre-budget-segment {
  height: 100%;
  background: var(--color-accent);
  border-right: 2px solid var(--color-surface);
}

.barre-budget-segment:nth-child(2n) {
  background: var(--color-accent-dark);
}

.repartition-budget-liste {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.repartition-budget-element {
  display: grid;
  grid-template-columns: 1fr auto auto;
  gap: 12px;
  font-size: 0.85rem;
  color: var(--color-text);
}

.repartition-budget-pourcentage {
  color: var(--color-text-muted);
  width: 40px;
  text-align: right;
}

.repartition-budget-montant {
  font-weight: 600;
  width: 90px;
  text-align: right;
}

.depenses-recentes-liste {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.depenses-recentes-element {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 10px;
  border-bottom: 1px solid var(--color-border);
}

.depenses-recentes-libelle {
  font-size: 0.85rem;
  font-weight: 600;
  color: var(--color-text);
}

.depenses-recentes-voyage {
  font-size: 0.72rem;
  color: var(--color-text-muted);
  margin-top: 2px;
}

.depenses-recentes-montant {
  font-size: 0.85rem;
  font-weight: 600;
  color: var(--color-accent-dark);
}
</style>