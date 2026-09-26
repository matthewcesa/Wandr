import { api } from './api'

export function getExpenseCategories() {
  return api.get('/expense-categories')
}