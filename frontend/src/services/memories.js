import { api } from './api'

export function getMemoriesByTrip(tripId) {
  return api.get(`/trips/${tripId}/memories`)
}

export function getMemoryById(tripId, memoryId) {
  return api.get(`/trips/${tripId}/memories/${memoryId}`)
}

export function createMemory(tripId, memory) {
  return api.post(`/trips/${tripId}/memories`, memory)
}

export function updateMemory(tripId, memoryId, memory) {
  return api.put(`/trips/${tripId}/memories/${memoryId}`, memory)
}

export function deleteMemory(tripId, memoryId) {
  return api.del(`/trips/${tripId}/memories/${memoryId}`)
}