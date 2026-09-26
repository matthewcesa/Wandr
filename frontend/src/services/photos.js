import { api } from './api'

export function getPhotosByMemory(memoryId) {
  return api.get(`/memories/${memoryId}/photos`)
}