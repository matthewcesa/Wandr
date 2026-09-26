import { createRouter, createWebHistory } from 'vue-router';

const routes = [
  { path: '/', 
    name: 'dashboard', 
    component: () => import('../views/DashboardView.vue') 
  },
  { path: '/voyages', 
    name: 'trips', 
    component: () => import('../views/AllTripsView.vue') 
  },
  {
    path: '/voyages/:id',
    name: 'trip-detail',
    component: () => import('../views/TripDetailView.vue'),
  },
  {
    path: '/souvenirs/nouveau',
    name: 'add-memory',
    component: () => import('../views/AddMemoryView.vue'),
  },
  { path: '/carte', 
    name: 'map', 
    component: () => import('../views/SouvenirMapView.vue') 
  },
  { path: '/statistiques', 
    name: 'stats', 
    component: () => import('../views/StatsView.vue') 
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 };
  },
});

export default router;
