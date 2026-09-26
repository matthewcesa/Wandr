<template>
  <div>
    <header class="entete-page">
      <h1>Bonjour, Matthew</h1>
      <p>Prêt à immortaliser vos escales et retracer vos doux souvenirs ?</p>
    </header>

    <div class="contenu-en-attente">
      Contenu du tableau de bord à venir (stats, carnets récents, aperçu carte...)
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