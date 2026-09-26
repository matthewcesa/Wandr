<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { createTrip } from '../services/trips'
import { useCurrentUser } from '../composable/useCurrentUser'

const router = useRouter()
const currentUser = useCurrentUser()

const form = ref({
  title: '',
  country: '',
  city: '',
  startDate: '',
  endDate: '',
  description: '',
  cover: '',
  plannedBudget: null,
  status: 'planned'
})

const isSubmitting = ref(false)
const errorMessage = ref('')

async function handleSubmit() {
  isSubmitting.value = true
  errorMessage.value = ''
  try {
    const trip = await createTrip(currentUser.user.userId, form.value)
    router.push(`/voyages/${trip.tripId}`)
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
      <h1>Créer un voyage</h1>
      <p>Renseigne les informations principales de ton prochain carnet.</p>
    </header>

    <form class="carte formulaire" @submit.prevent="handleSubmit">
      <div class="formulaire-ligne">
        <label class="formulaire-champ">
          <span>Titre *</span>
          <input v-model="form.title" type="text" required placeholder="Ex: Week-end à Lisbonne" />
        </label>

        <label class="formulaire-champ">
          <span>Statut</span>
          <select v-model="form.status">
            <option value="planned">À venir</option>
            <option value="ongoing">En cours</option>
            <option value="completed">Terminé</option>
          </select>
        </label>
      </div>

      <div class="formulaire-ligne">
        <label class="formulaire-champ">
          <span>Pays</span>
          <input v-model="form.country" type="text" placeholder="Ex: Portugal" />
        </label>

        <label class="formulaire-champ">
          <span>Ville</span>
          <input v-model="form.city" type="text" placeholder="Ex: Lisbonne" />
        </label>
      </div>

      <div class="formulaire-ligne">
        <label class="formulaire-champ">
          <span>Date de début *</span>
          <input v-model="form.startDate" type="date" required />
        </label>

        <label class="formulaire-champ">
          <span>Date de fin *</span>
          <input v-model="form.endDate" type="date" required />
        </label>
      </div>

      <label class="formulaire-champ">
        <span>Description</span>
        <textarea v-model="form.description" rows="3" placeholder="Raconte ce voyage en quelques mots..."></textarea>
      </label>

      <div class="formulaire-ligne">
        <label class="formulaire-champ">
          <span>Image de couverture (chemin)</span>
          <input v-model="form.cover" type="text" placeholder="/images/lisbonne.jpg" />
        </label>

        <label class="formulaire-champ">
          <span>Budget prévu (€)</span>
          <input v-model.number="form.plannedBudget" type="number" min="0" step="0.01" />
        </label>
      </div>

      <p v-if="errorMessage" class="formulaire-erreur">{{ errorMessage }}</p>

      <div class="formulaire-actions">
        <router-link to="/voyages" class="formulaire-annuler">Annuler</router-link>
        <button type="submit" class="formulaire-valider" :disabled="isSubmitting">
          {{ isSubmitting ? 'Création...' : 'Créer le voyage' }}
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