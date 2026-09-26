import { api } from './api'

export function getTagsByMemory(memoryId) {
  return api.get(`/memories/${memoryId}/tags`)
}