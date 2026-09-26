import { api } from './api';

export function getExpensesByTrip(tripId) {
  return api.get(`/trips/${tripId}/expenses`);
}

export function getExpenseById(tripId, expenseId) {
  return api.get(`/trips/${tripId}/expenses/${expenseId}`);
}

export function createExpense(tripId, expenseCategoryId, memoryId, expense) {
  const params = new URLSearchParams({ expenseCategoryId });
  if (memoryId) params.append('memoryId', memoryId);
  return api.post(`/trips/${tripId}/expenses?${params.toString()}`, expense);
}

export function updateExpense(tripId, expenseId, expense) {
  return api.put(`/trips/${tripId}/expenses/${expenseId}`, expense);
}

export function deleteExpense(tripId, expenseId) {
  return api.del(`/trips/${tripId}/expenses/${expenseId}`);
}
