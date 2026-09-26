import { api } from './api'

export function getUserById(userId) {
  return api.get(`/users/${userId}`)
}