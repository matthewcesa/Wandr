# Wandr frontend

Interface web de Wandr construite avec Vue 3, JavaScript et Vite.

## Développement local

```bash
npm install
npm run start
```

Ouvrez ensuite `http://localhost:5173/`.

## Docker Compose

Depuis la racine du projet :

```bash
docker compose up --build
```

Le frontend est disponible sur `http://localhost:5173/`.

## Build de production

```bash
npm run build
```

Les fichiers générés sont placés dans `dist/`.
