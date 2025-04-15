import axios from 'axios'

export interface Widget {
  name: string
  description: string
  price: number
}

const api = axios.create({
  baseURL: 'http://localhost:9000/v1/widgets',
  headers: {
    'Content-Type': 'application/json',
  },
})

// 🧠 Global error handler
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const message =
      error.response?.data?.message ||
      error.response?.statusText ||
      error.message ||
      'Unknown error'
    console.error('API Error:', message)
    return Promise.reject(new Error(message))
  }
)

// 🎯 API methods

export const fetchAllWidgets = async (): Promise<Widget[]> => {
  const res = await api.get('')
  return res.data
}

export const fetchWidgetByName = async (name: string): Promise<Widget> => {
  const res = await api.get(`/${encodeURIComponent(name)}`)
  return res.data
}

export const createWidget = async (widget: Widget): Promise<void> => {
  await api.post('', widget)
}

export const updateWidget = async (
  name: string,
  updates: Partial<Pick<Widget, 'name' | 'description' | 'price'>>
): Promise<void> => {
  await api.put(`/${encodeURIComponent(name)}`, updates)
}

export const deleteWidget = async (name: string): Promise<void> => {
  await api.delete(`/${encodeURIComponent(name)}`)
}
