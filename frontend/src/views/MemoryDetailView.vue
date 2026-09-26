<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getMemoryById } from '../services/memories'
import { getPhotosByMemory } from '../services/photos'
import { getTagsByMemory } from '../services/tags'
import { getRatingsByMemory } from '../services/ratings'
import { getExpensesByTrip, createExpense } from '../services/expenses'
import { getExpenseCategories } from '../services/expenseCategories'

const route = useRoute()

const memory = ref(null)
const photos = ref([])
const tags = ref([])
const ratings = ref([])
const memoryExpenses = ref([])
const categories = ref([])
const isLoading = ref(true)
const errorMessage = ref('')

const typeLabels = {
  meal: 'Repas',
  activity: 'Activité',
  accommodation: 'Hébergement',
  place: 'Lieu'
}

const averageRating = computed(() => {
  if (ratings.value.length === 0) return null
  const scores = ratings.value.flatMap((r) => [
    Number(r.valueForMoney || 0),
    Number(r.atmosphereAndService || 0),
    Number(r.qualityOfActivity || 0)
  ])
  return (scores.reduce((sum, s) => sum + s, 0) / scores.length).toFixed(1)
})

const totalSpentOnMemory = computed(() =>
  memoryExpenses.value.reduce((sum, e) => sum + Number(e.amount || 0), 0)
)

const expenseForm = ref({
  categoryId: null,
  label: '',
  amount: null,
  currency: 'EUR',
  spentAt: ''
})
const isAddingExpense = ref(false)
const isSubmittingExpense = ref(false)
const expenseError = ref('')

function formatCurrency(value) {
  return new Intl.NumberFormat('fr-FR', { style: 'currency', currency: 'EUR' }).format(value || 0)
}

function formatDate(date) {
  if (!date) return '—'
  return new Date(date).toLocaleDateString('fr-FR', { day: 'numeric', month: 'short', year: 'numeric' })
}

async function loadMemory() {
  const { tripId, memoryId } = route.params
  isLoading.value = true
  errorMessage.value = ''
  try {
    const [memoryData, photosData, tagsData, ratingsData, tripExpenses, categoriesData] = await Promise.all([
      getMemoryById(tripId, memoryId),
      getPhotosByMemory(memoryId),
      getTagsByMemory(memoryId),
      getRatingsByMemory(memoryId),
      getExpensesByTrip(tripId),
      getExpenseCategories()
    ])
    memory.value = memoryData
    photos.value = photosData
    tags.value = tagsData
    ratings.value = ratingsData
    categories.value = categoriesData

    memoryExpenses.value = tripExpenses.filter((e) => e.memory?.memoryId === Number(memoryId))
  } catch (err) {
    errorMessage.value = err.message
  } finally {
    isLoading.value = false
  }
}

async function handleAddExpense() {
  if (!expenseForm.value.categoryId || !expenseForm.value.amount) {
    expenseError.value = 'Choisis une catégorie et un montant.'
    return
  }
  isSubmittingExpense.value = true
  expenseError.value = ''
  const { tripId, memoryId } = route.params
  try {
    const created = await createExpense(tripId, expenseForm.value.categoryId, memoryId, {
      label: expenseForm.value.label || null,
      amount: expenseForm.value.amount,
      currency: expenseForm.value.currency,
      spentAt: expenseForm.value.spentAt ? `${expenseForm.value.spentAt}T00:00:00` : null
    })
    memoryExpenses.value.push(created)
    expenseForm.value = { categoryId: null, label: '', amount: null, currency: 'EUR', spentAt: '' }
    isAddingExpense.value = false
  } catch (err) {
    expenseError.value = err.message
  } finally {
    isSubmittingExpense.value = false
  }
}

onMounted(loadMemory)
watch(() => route.params.memoryId, loadMemory)
</script>

<template>
  <div>
    <p v-if="isLoading" class="contenu-en-attente">Chargement du souvenir...</p>
    <p v-else-if="errorMessage" class="contenu-en-attente">Erreur : {{ errorMessage }}</p>

    <template v-else-if="memory">
      <router-link :to="`/voyages/${route.params.tripId}`" class="lien-retour">
        ← Retour au voyage
      </router-link>

      <div class="mise-en-page-souvenir">
        <div class="contenu-principal-souvenir">
          <div class="carte entete-souvenir">
            <div class="entete-souvenir-haut">
              <span class="entete-souvenir-type">{{ typeLabels[memory.type] || memory.type }}</span>
              <span v-if="memory.isFavorite" class="entete-souvenir-favori">♥ Coup de cœur</span>
            </div>

            <h1 class="entete-souvenir-titre">{{ memory.title }}</h1>
            <p class="entete-souvenir-adresse">{{ memory.address }}</p>
            <p class="entete-souvenir-date">Visité le {{ formatDate(memory.visitedAt) }}</p>

            <p class="entete-souvenir-description">{{ memory.description }}</p>

            <div v-if="tags.length > 0" class="entete-souvenir-etiquettes">
              <span
                v-for="tag in tags"
                :key="tag.tagId"
                class="etiquette-souvenir"
                :style="tag.color ? { backgroundColor: tag.color + '22', color: tag.color } : {}"
              >
                #{{ tag.name }}
              </span>
            </div>
          </div>

          <div v-if="photos.length > 0" class="carte carte-photos">
            <h2 class="carte-photos-titre">Photos</h2>
            <div class="grille-photos">
              <img
                v-for="photo in photos"
                :key="photo.photoId"
                :src="photo.url"
                :alt="photo.caption || memory.title"
                class="grille-photos-element"
              />
            </div>
          </div>
        </div>

        <aside class="barre-laterale-souvenir">
          <div v-if="averageRating" class="carte carte-note">
            <p class="carte-note-valeur">{{ averageRating }} / 5</p>
            <p class="carte-note-libelle">Note moyenne</p>
          </div>

          <div class="carte carte-depenses">
            <div class="carte-depenses-entete">
              <h2>Dépenses liées</h2>
              <button type="button" class="carte-depenses-bascule" @click="isAddingExpense = !isAddingExpense">
                {{ isAddingExpense ? 'Annuler' : '+ Ajouter' }}
              </button>
            </div>

            <p class="carte-depenses-total">Total : {{ formatCurrency(totalSpentOnMemory) }}</p>

            <ul v-if="memoryExpenses.length > 0" class="liste-depenses">
              <li v-for="expense in memoryExpenses" :key="expense.expenseId" class="liste-depenses-element">
                <span>{{ expense.label || expense.expenseCategory?.name }}</span>
                <span>{{ formatCurrency(expense.amount) }}</span>
              </li>
            </ul>
            <p v-else class="carte-depenses-vide">Aucune dépense enregistrée pour ce souvenir.</p>

            <form v-if="isAddingExpense" class="formulaire-depenses" @submit.prevent="handleAddExpense">
              <label class="formulaire-depenses-champ">
                <span>Catégorie *</span>
                <select v-model.number="expenseForm.categoryId" required>
                  <option :value="null" disabled>Choisir...</option>
                  <option v-for="cat in categories" :key="cat.expenseCategoryId" :value="cat.expenseCategoryId">
                    {{ cat.icon }} {{ cat.name }}
                  </option>
                </select>
              </label>

              <label class="formulaire-depenses-champ">
                <span>Libellé</span>
                <input v-model="expenseForm.label" type="text" placeholder="Ex: Billet d'entrée" />
              </label>

              <label class="formulaire-depenses-champ">
                <span>Montant (€) *</span>
                <input v-model.number="expenseForm.amount" type="number" min="0" step="0.01" required />
              </label>

              <label class="formulaire-depenses-champ">
                <span>Date</span>
                <input v-model="expenseForm.spentAt" type="date" />
              </label>

              <p v-if="expenseError" class="formulaire-depenses-erreur">{{ expenseError }}</p>

              <button type="submit" class="formulaire-depenses-valider" :disabled="isSubmittingExpense">
                {{ isSubmittingExpense ? 'Ajout...' : 'Ajouter la dépense' }}
              </button>
            </form>
          </div>
        </aside>
      </div>
    </template>
  </div>
</template>

<style scoped>
.lien-retour {
  display: inline-block;
  font-size: 0.82rem;
  color: var(--color-text-muted);
  margin-bottom: 16px;
}

.lien-retour:hover {
  color: var(--color-accent-dark);
}

.mise-en-page-souvenir {
  display: grid;
  grid-template-columns: 1fr 280px;
  gap: 24px;
  align-items: start;
}

.entete-souvenir,
.carte-photos {
  padding: 22px;
  margin-bottom: 20px;
}

.entete-souvenir-haut {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.entete-souvenir-type {
  font-size: 0.68rem;
  text-transform: uppercase;
  letter-spacing: 0.03em;
  background: var(--color-accent-tint);
  color: var(--color-accent-dark);
  padding: 3px 9px;
  border-radius: 999px;
  font-weight: 600;
}

.entete-souvenir-favori {
  font-size: 0.8rem;
  color: #b3413a;
}

.entete-souvenir-titre {
  font-size: 1.5rem;
  margin-bottom: 4px;
}

.entete-souvenir-adresse,
.entete-souvenir-date {
  font-size: 0.82rem;
  color: var(--color-text-muted);
}

.entete-souvenir-description {
  margin-top: 14px;
  font-size: 0.9rem;
  line-height: 1.5;
  color: var(--color-text);
}

.entete-souvenir-etiquettes {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 16px;
}

.etiquette-souvenir {
  font-size: 0.75rem;
  padding: 4px 10px;
  border-radius: 999px;
  background: var(--color-accent-tint);
  color: var(--color-accent-dark);
}

.carte-photos-titre {
  font-size: 1rem;
  margin-bottom: 12px;
}

.grille-photos {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 10px;
}

.grille-photos-element {
  width: 100%;
  height: 130px;
  object-fit: cover;
  border-radius: var(--radius-sm);
}

.carte-note {
  padding: 18px;
  text-align: center;
  margin-bottom: 16px;
}

.carte-note-valeur {
  font-family: var(--font-display);
  font-size: 1.8rem;
  color: var(--color-accent-dark);
}

.carte-note-libelle {
  font-size: 0.75rem;
  color: var(--color-text-muted);
  margin-top: 2px;
}

.carte-depenses {
  padding: 18px;
}

.carte-depenses-entete {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.carte-depenses-entete h2 {
  font-size: 1rem;
}

.carte-depenses-bascule {
  background: none;
  font-size: 0.78rem;
  color: var(--color-accent);
  font-weight: 600;
}

.carte-depenses-total {
  font-size: 0.85rem;
  font-weight: 600;
  margin-bottom: 12px;
}

.liste-depenses {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 14px;
}

.liste-depenses-element {
  display: flex;
  justify-content: space-between;
  font-size: 0.8rem;
  color: var(--color-text);
}

.carte-depenses-vide {
  font-size: 0.8rem;
  color: var(--color-text-muted);
  margin-bottom: 14px;
}

.formulaire-depenses {
  display: flex;
  flex-direction: column;
  gap: 10px;
  border-top: 1px solid var(--color-border);
  padding-top: 14px;
}

.formulaire-depenses-champ {
  display: flex;
  flex-direction: column;
  gap: 4px;
  font-size: 0.78rem;
}

.formulaire-depenses-champ input,
.formulaire-depenses-champ select {
  font-family: var(--font-body);
  font-size: 0.82rem;
  padding: 7px 10px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  background: var(--color-surface);
  color: var(--color-text);
}

.formulaire-depenses-erreur {
  font-size: 0.78rem;
  color: #b3413a;
}

.formulaire-depenses-valider {
  padding: 8px 14px;
  border-radius: var(--radius-sm);
  background: var(--color-accent);
  color: #fff;
  font-size: 0.82rem;
  font-weight: 600;
}

.formulaire-depenses-valider:hover {
  background: var(--color-accent-dark);
}

.formulaire-depenses-valider:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>