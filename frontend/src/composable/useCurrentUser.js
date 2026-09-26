import { reactive } from 'vue'
import { getUserById } from '../services/users'

const STORAGE_KEY = 'wandr_current_user_id'

const state = reactive({
  user: null,
  isLoading: false,
  initialized: false
})

function getStoredUserId() {
  const raw = localStorage.getItem(STORAGE_KEY)
  return raw ? Number(raw) : null
}

function persistUserId(userId) {
  if (userId) {
    localStorage.setItem(STORAGE_KEY, String(userId))
  } else {
    localStorage.removeItem(STORAGE_KEY)
  }
}


export async function ensureSessionLoaded() {
  if (state.initialized) return
  state.isLoading = true
  const storedId = getStoredUserId()
  if (storedId) {
    try {
      state.user = await getUserById(storedId)
    } catch {
      // Le compte n'existe peut-être plus
      persistUserId(null)
      state.user = null
    }
  }
  state.isLoading = false
  state.initialized = true
}

export function setCurrentUser(user) {
  state.user = user
  persistUserId(user?.userId ?? null)
}

export function logout() {
  state.user = null
  persistUserId(null)
}

export function useCurrentUser() {
  return state
}