<!-- src/views/TripDetailView.vue -->
<template>
  <div>
    <header class="entete-page">
      <h1>Détail du Voyage</h1>
      <p>Parcourez le détail complet de ce carnet de voyage.</p>
    </header>

    <div class="contenu-en-attente">
      Contenu du détail de voyage à venir (bannière, liste des souvenirs, tags...)
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
</style>