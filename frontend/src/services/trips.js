import { api } from './api'

export function getTripsByUser(userId) {
  return api.get(`/trips/user/${userId}`)
}

export function getTripById(tripId) {
  return api.get(`/trips/${tripId}`)
}

export function createTrip(userId, trip) {
  return api.post(`/trips/user/${userId}`, trip)
}

export function updateTrip(tripId, trip) {
  return api.put(`/trips/${tripId}`, trip)
}

export function deleteTrip(tripId) {
  return api.del(`/trips/${tripId}`)
}