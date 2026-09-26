import { api } from './api'

export async function findUserByEmail(email) {
  const users = await api.get('/users')
  const normalized = email.trim().toLowerCase()
  return users.find((u) => u.email.toLowerCase() === normalized) || null
}

export function registerUser({ name, email }) {
  return api.post('/users', { name, email })
}