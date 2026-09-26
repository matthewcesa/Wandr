import { createRouter, createWebHistory } from 'vue-router';
import { ensureSessionLoaded, useCurrentUser } from '../composable/useCurrentUser'

const routes = [
  { path: '/connexion', 
    name: 'login', component: () => import('../views/LoginView.vue'), 
    meta: { public: true } 
  },
  { path: '/inscription', 
    name: 'register', component: () => import('../views/RegisterView.vue'), 
    meta: { public: true } 
  },
  { path: '/', 
    name: 'dashboard', 
    component: () => import('../views/DashboardView.vue') 
  },
  { path: '/voyages', 
    name: 'trips', 
    component: () => import('../views/AllTripsView.vue') 
  },
  { path: '/voyages/nouveau', 
    name: 'add-trip', 
    component: () => import('../views/AddTripView.vue') 
  },
  {
    path: '/voyages/:id',
    name: 'trip-detail',
    component: () => import('../views/TripDetailView.vue'),
  },
  { path: '/voyages/:tripId/souvenirs/:memoryId', 
    name: 'memory-detail', 
    component: () => import('../views/MemoryDetailView.vue') 
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
    return { top: 0 }
  }
})

router.beforeEach(async (to) => {
  await ensureSessionLoaded()
  const isLoggedIn = useCurrentUser().user !== null

  if (!to.meta.public && !isLoggedIn) {
    return { path: '/connexion', query: { redirect: to.fullPath } }
  }
  if (to.meta.public && isLoggedIn) {
    return { path: '/' }
  }
  return true
})

export default router

