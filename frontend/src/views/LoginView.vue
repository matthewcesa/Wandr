<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { findUserByEmail } from '../services/auth'
import { setCurrentUser } from '../composable/useCurrentUser'

const route = useRoute()
const router = useRouter()

const email = ref('')
const isSubmitting = ref(false)
const errorMessage = ref('')

async function handleSubmit() {
  isSubmitting.value = true
  errorMessage.value = ''
  try {
    const user = await findUserByEmail(email.value)
    if (!user) {
      errorMessage.value = 'Aucun compte ne correspond à cet email.'
      return
    }
    setCurrentUser(user)
    router.push(route.query.redirect || '/')
  } catch (err) {
    errorMessage.value = err.message
  } finally {
    isSubmitting.value = false
  }
}
</script>

<template>
  <div class="carte-authentification carte">
    <div class="carte-authentification-marque">
      <img class="carte-authentification-logo" src="../assets/LogoWandr.png" alt="" aria-hidden="true" />
      <div>
        <p class="carte-authentification-nom">Wandr</p>
        <p class="carte-authentification-signature">Carnets de voyages</p>
      </div>
    </div>

    <h1 class="carte-authentification-titre">Content de te revoir</h1>
    <p class="carte-authentification-sous-titre">Connecte-toi avec ton email pour retrouver tes carnets.</p>

    <form class="formulaire-authentification" @submit.prevent="handleSubmit">
      <label class="formulaire-authentification-champ">
        <span>Adresse email</span>
        <input v-model="email" type="email" required placeholder="matthewcesa@gmail.com" autofocus />
      </label>

      <p v-if="errorMessage" class="formulaire-authentification-erreur">{{ errorMessage }}</p>

      <button type="submit" class="formulaire-authentification-valider" :disabled="isSubmitting">
        {{ isSubmitting ? 'Connexion...' : 'Se connecter' }}
      </button>
    </form>

    <p class="carte-authentification-pied">
      Pas encore de compte ? <router-link to="/inscription">Crée-en un</router-link>
    </p>
  </div>
</template>

<style scoped>
.carte-authentification {
  width: 100%;
  max-width: 380px;
  padding: 32px;
}

.carte-authentification-marque {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 24px;
}

.carte-authentification-logo {
  width: 40px;
  height: 34px;
  border-radius: 9px;
  flex-shrink: 0;
}

.carte-authentification-nom {
  font-family: var(--font-display);
  font-size: 1.05rem;
  line-height: 1.1;
}

.carte-authentification-signature {
  font-size: 0.62rem;
  letter-spacing: 0.04em;
  color: var(--color-text-muted);
  margin-top: 2px;
}

.carte-authentification-titre {
  font-size: 1.4rem;
  margin-bottom: 6px;
}

.carte-authentification-sous-titre {
  font-size: 0.85rem;
  color: var(--color-text-muted);
  margin-bottom: 24px;
}

.formulaire-authentification {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.formulaire-authentification-champ {
  display: flex;
  flex-direction: column;
  gap: 6px;
  font-size: 0.82rem;
  color: var(--color-text);
}

.formulaire-authentification-champ input {
  font-family: var(--font-body);
  font-size: 0.88rem;
  padding: 10px 12px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  background: var(--color-surface);
  color: var(--color-text);
}

.formulaire-authentification-champ input:focus {
  outline: none;
  border-color: var(--color-accent);
}

.formulaire-authentification-erreur {
  font-size: 0.8rem;
  color: #b3413a;
}

.formulaire-authentification-valider {
  padding: 11px 16px;
  border-radius: var(--radius-sm);
  background: var(--color-accent);
  color: #fff;
  font-size: 0.88rem;
  font-weight: 600;
}

.formulaire-authentification-valider:hover {
  background: var(--color-accent-dark);
}

.formulaire-authentification-valider:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.carte-authentification-pied {
  margin-top: 20px;
  font-size: 0.82rem;
  color: var(--color-text-muted);
  text-align: center;
}

.carte-authentification-pied a {
  color: var(--color-accent);
  font-weight: 600;
}
</style>14