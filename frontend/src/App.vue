<script setup>
import { ref } from 'vue'

const trips = [
  { city: 'Lisbonne', country: 'Portugal', date: '12 - 18 juin', color: 'coral', icon: 'L' },
  { city: 'Kyoto', country: 'Japon', date: '04 - 15 octobre', color: 'teal', icon: 'K' },
  { city: 'Reykjavik', country: 'Islande', date: '22 - 29 novembre', color: 'blue', icon: 'R' },
]

const activeTrip = ref(0)
const showForm = ref(false)
const search = ref('')

function selectTrip(index) {
  activeTrip.value = index
}
</script>

<template>
  <main class="app-shell">
    <nav class="topbar">
      <a class="brand" href="#" aria-label="Wandr accueil">
        <span class="brand-mark">w</span>
        <span>wandr</span>
      </a>
      <div class="nav-links">
        <a class="nav-link active" href="#voyages">Mes voyages</a>
        <a class="nav-link" href="#souvenirs">Souvenirs</a>
        <button class="profile-button" type="button" aria-label="Ouvrir le profil">AM</button>
      </div>
    </nav>

    <section class="hero" id="voyages">
      <div>
        <p class="eyebrow">Bonjour, Alex <span class="sun">☀</span></p>
        <h1>Vos prochaines<br /><em>échappées</em></h1>
        <p class="hero-copy">Gardez vos itinéraires, vos souvenirs et les petits détails qui rendent chaque voyage unique.</p>
      </div>
      <div class="hero-stamp" aria-hidden="true">
        <span>COLLECT</span>
        <strong>MEMORIES</strong>
        <span>✳</span>
      </div>
    </section>

    <section class="toolbar" aria-label="Actions sur les voyages">
      <label class="search-box">
        <span aria-hidden="true">⌕</span>
        <input v-model="search" type="search" placeholder="Rechercher un voyage" />
      </label>
      <button class="primary-button" type="button" @click="showForm = !showForm">
        <span>+</span> Nouveau voyage
      </button>
    </section>

    <form v-if="showForm" class="trip-form" @submit.prevent="showForm = false">
      <label>Nom du voyage <input required placeholder="Ex. Un week-end à Rome" /></label>
      <label>Date de départ <input required type="date" /></label>
      <button class="primary-button" type="submit">Ajouter</button>
    </form>

    <section class="trip-section">
      <div class="section-heading">
        <h2>Vos voyages <span>{{ trips.length }}</span></h2>
        <a href="#tous">Voir tout <span>→</span></a>
      </div>
      <div class="trip-grid">
        <button
          v-for="(trip, index) in trips"
          :key="trip.city"
          class="trip-card"
          :class="[{ selected: activeTrip === index }, trip.color]"
          type="button"
          @click="selectTrip(index)"
        >
          <span class="trip-icon">{{ trip.icon }}</span>
          <span class="trip-card-content">
            <strong>{{ trip.city }}</strong>
            <small>{{ trip.country }}</small>
            <small class="trip-date">{{ trip.date }}</small>
          </span>
          <span class="card-arrow">↗</span>
        </button>
      </div>
    </section>

    <section class="memory-banner" id="souvenirs">
      <div>
        <p class="eyebrow">Votre journal de bord</p>
        <h2>Un endroit pour<br /><em>ne rien oublier.</em></h2>
        <button class="text-button" type="button">Ajouter un souvenir <span>→</span></button>
      </div>
      <div class="memory-doodle" aria-hidden="true">✦</div>
      <div class="memory-photo" aria-hidden="true"><span>✈</span></div>
    </section>
  </main>
</template>
