import { api } from './api'

export function getRatingsByMemory(memoryId) {
  return api.get(`/memories/${memoryId}/ratings`)
}