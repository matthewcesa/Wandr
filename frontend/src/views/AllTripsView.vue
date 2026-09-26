<template>
  <div>
    <header class="entete-page">
      <h1>Tous mes Voyages</h1>
      <p>L'intégralité de vos carnets, récits et explorations mémorables.</p>
    </header>

    <div class="contenu-en-attente">
      Contenu de la liste des voyages à venir (filtres, grille de carnets...)
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