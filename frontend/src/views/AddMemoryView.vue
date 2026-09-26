<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getTripsByUser } from '../services/trips'
import { createMemory } from '../services/memories'
import { createExpense } from '../services/expenses'
import { getExpenseCategories } from '../services/expenseCategories'
import { useCurrentUser } from '../composable/useCurrentUser'

const route = useRoute()
const router = useRouter()
const currentUser = useCurrentUser()

const trips = ref([])
const categories = ref([])
const isLoadingTrips = ref(true)
const selectedTripId = ref(route.query.tripId ? Number(route.query.tripId) : null)

const form = ref({
  title: '',
  type: 'meal',
  description: '',
  visitedAt: '',
  address: '',
  isFavorite: false
})

const costForm = ref({
  hasCost: false,
  categoryId: null,
  amount: null
})

const isSubmitting = ref(false)
const errorMessage = ref('')

onMounted(async () => {
  try {
    const [tripsData, categoriesData] = await Promise.all([
      getTripsByUser(currentUser.user.userId),
      getExpenseCategories()
    ])
    trips.value = tripsData
    categories.value = categoriesData
    if (!selectedTripId.value && trips.value.length > 0) {
      selectedTripId.value = trips.value[0].tripId
    }
  } catch (err) {
    errorMessage.value = "Impossible de charger les données : " + err.message
  } finally {
    isLoadingTrips.value = false
  }
})

async function handleSubmit() {
  if (!selectedTripId.value) {
    errorMessage.value = 'Choisis un voyage avant de continuer.'
    return
  }
  if (costForm.value.hasCost && (!costForm.value.categoryId || !costForm.value.amount)) {
    errorMessage.value = 'Renseigne une catégorie et un montant pour le coût, ou décoche la case.'
    return
  }

  isSubmitting.value = true
  errorMessage.value = ''
  try {
    const memory = await createMemory(selectedTripId.value, form.value)

    if (costForm.value.hasCost) {
      await createExpense(selectedTripId.value, costForm.value.categoryId, memory.memoryId, {
        label: form.value.title,
        amount: costForm.value.amount,
        currency: 'EUR',
        spentAt: form.value.visitedAt ? `${form.value.visitedAt}T00:00:00` : null
      })
    }

    router.push(`/voyages/${selectedTripId.value}/souvenirs/${memory.memoryId}`)
  } catch (err) {
    errorMessage.value = err.message
  } finally {
    isSubmitting.value = false
  }
}
</script>

<template>
  <div>
    <header class="entete-page">
      <h1>Ajouter un Souvenir</h1>
      <p>Enrichissez votre carnet d'une nouvelle expérience culinaire ou d'une belle activité.</p>
    </header>

    <p v-if="isLoadingTrips" class="contenu-en-attente">Chargement de tes voyages...</p>

    <p v-else-if="trips.length === 0" class="contenu-en-attente">
      Tu dois d'abord <router-link to="/voyages/nouveau">créer un voyage</router-link> avant d'ajouter un souvenir.
    </p>

    <form v-else class="carte formulaire" @submit.prevent="handleSubmit">
      <label v-if="!route.query.tripId" class="formulaire-champ">
        <span>Voyage concerné *</span>
        <select v-model.number="selectedTripId" required>
          <option v-for="trip in trips" :key="trip.tripId" :value="trip.tripId">
            {{ trip.title }}
          </option>
        </select>
      </label>

      <div class="formulaire-ligne">
        <label class="formulaire-champ">
          <span>Nom du souvenir *</span>
          <input v-model="form.title" type="text" required placeholder="Ex: Dégustation de ramen à Gion" />
        </label>

        <label class="formulaire-champ">
          <span>Type *</span>
          <select v-model="form.type" required>
            <option value="meal">Repas / Café</option>
            <option value="activity">Activité</option>
            <option value="accommodation">Hébergement</option>
            <option value="place">Lieu / Visite</option>
          </select>
        </label>
      </div>

      <label class="formulaire-champ">
        <span>Votre récit & impressions *</span>
        <textarea
          v-model="form.description"
          rows="4"
          required
          placeholder="Racontez l'ambiance, les saveurs, les anecdotes vécues à cet endroit mémorable..."
        ></textarea>
      </label>

      <div class="formulaire-ligne">
        <label class="formulaire-champ">
          <span>Lieu précis</span>
          <input v-model="form.address" type="text" placeholder="Ex: Kyoto, quartier historique" />
        </label>

        <label class="formulaire-champ">
          <span>Date de visite</span>
          <input v-model="form.visitedAt" type="date" />
        </label>
      </div>

      <label class="formulaire-case">
        <input v-model="form.isFavorite" type="checkbox" />
        <span>Coup de cœur absolu</span>
      </label>

      <div class="formulaire-cout">
        <label class="formulaire-case">
          <input v-model="costForm.hasCost" type="checkbox" />
          <span>Ce souvenir a coûté quelque chose</span>
        </label>

        <div v-if="costForm.hasCost" class="formulaire-ligne">
          <label class="formulaire-champ">
            <span>Catégorie de dépense *</span>
            <select v-model.number="costForm.categoryId">
              <option :value="null" disabled>Choisir...</option>
              <option v-for="cat in categories" :key="cat.expenseCategoryId" :value="cat.expenseCategoryId">
                {{ cat.icon }} {{ cat.name }}
              </option>
            </select>
          </label>

          <label class="formulaire-champ">
            <span>Montant (€) *</span>
            <input v-model.number="costForm.amount" type="number" min="0" step="0.01" />
          </label>
        </div>
      </div>

      <p v-if="errorMessage" class="formulaire-erreur">{{ errorMessage }}</p>

      <div class="formulaire-actions">
        <router-link to="/voyages" class="formulaire-annuler">Annuler</router-link>
        <button type="submit" class="formulaire-valider" :disabled="isSubmitting">
          {{ isSubmitting ? 'Enregistrement...' : 'Enregistrer le souvenir' }}
        </button>
      </div>
    </form>
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

.formulaire {
  padding: 24px;
  max-width: 640px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.formulaire-ligne {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.formulaire-champ {
  display: flex;
  flex-direction: column;
  gap: 6px;
  font-size: 0.82rem;
  color: var(--color-text);
}

.formulaire-champ input,
.formulaire-champ select,
.formulaire-champ textarea {
  font-family: var(--font-body);
  font-size: 0.85rem;
  padding: 9px 12px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  background: var(--color-surface);
  color: var(--color-text);
}

.formulaire-champ input:focus,
.formulaire-champ select:focus,
.formulaire-champ textarea:focus {
  outline: none;
  border-color: var(--color-accent);
}

.formulaire-case {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.85rem;
  color: var(--color-text);
}

.formulaire-cout {
  border-top: 1px solid var(--color-border);
  padding-top: 16px;
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.formulaire-erreur {
  color: #b3413a;
  font-size: 0.82rem;
}

.formulaire-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 8px;
}

.formulaire-annuler {
  padding: 10px 16px;
  border-radius: var(--radius-sm);
  font-size: 0.85rem;
  color: var(--color-text-muted);
  border: 1px solid var(--color-border);
}

.formulaire-valider {
  padding: 10px 18px;
  border-radius: var(--radius-sm);
  font-size: 0.85rem;
  font-weight: 600;
  background: var(--color-accent);
  color: #fff;
}

.formulaire-valider:hover {
  background: var(--color-accent-dark);
}

.formulaire-valider:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>