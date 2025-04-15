import React, { useState } from 'react'
import Stack from '@mui/material/Stack'
import Button from '@mui/material/Button'
import Box from '@mui/material/Box'
import TextField from '@mui/material/TextField'
import Divider from '@mui/material/Divider'
import Typography from '@mui/material/Typography'

import WidgetList from './components/WidgetList'
import WidgetForm from './components/WidgetForm'
import DisplayWidget from './components/WidgetDisplay'

import {
  Widget,
  createWidget,
  fetchWidgetByName,
  updateWidget,
  deleteWidget,
} from './lib/apiConnect'

type View = 'list' | 'create' | 'find' | 'edit'

const App = (): JSX.Element => {
  const [view, setView] = useState<View>('list')
  const [selectedWidget, setSelectedWidget] = useState<Widget | null>(null)
  const [searchName, setSearchName] = useState<string>('')

  const handleFind = async () => {
    try {
      const widget = await fetchWidgetByName(searchName)
      setSelectedWidget(widget)
    } catch (err) {
      console.error('Widget not found:', err)
      setSelectedWidget(null)
    }
  }

  const handleDelete = async (name: string) => {
    await deleteWidget(name)
    alert('Widget deleted!')
    setView('list')
  }

  return (
    <Stack spacing={4} sx={{ maxWidth: 900, margin: 'auto', paddingTop: 4 }}>
      <Typography variant="h3" align="center">Widget Management</Typography>

      <Stack direction="row" spacing={2} justifyContent="center">
        <Button variant="outlined" onClick={() => setView('list')}>List Widgets</Button>
        <Button variant="outlined" onClick={() => setView('create')}>Create Widget</Button>
        <Button variant="outlined" onClick={() => setView('find')}>Find by Name</Button>
      </Stack>

      <Divider />

      {view === 'list' && <WidgetList />}

      {view === 'create' && (
        <WidgetForm
          onSubmit={async (widget) => {
            await createWidget(widget)
            alert('Widget created!')
            setView('list')
          }}
        />
      )}

      {view === 'find' && (
        <Box>
          <Stack direction="row" spacing={2} sx={{ paddingBottom: 2 }}>
            <TextField
              label="Widget name"
              value={searchName}
              onChange={(e) => setSearchName(e.target.value)}
            />
            <Button variant="contained" onClick={handleFind}>Search</Button>
          </Stack>

          {selectedWidget && (
            <Box>
              <DisplayWidget widget={selectedWidget} />

              <Stack direction="row" spacing={2} sx={{ marginTop: 2 }}>
                <Button
                  variant="contained"
                  onClick={() => setView('edit')}
                >
                  Edit Widget
                </Button>
                <Button
                  variant="outlined"
                  color="error"
                  onClick={() => handleDelete(selectedWidget.name)}
                >
                  Delete Widget
                </Button>
              </Stack>
            </Box>
          )}
        </Box>
      )}

      {view === 'edit' && selectedWidget && (
        <WidgetForm
          isEdit
          defaultValues={selectedWidget}
          onSubmit={async (data) => {
            await updateWidget(data.name, {
              name: data.name,
              description: data.description,
              price: data.price,
            })
            alert('Widget updated!')
            setView('list')
          }}
        />
      )}
    </Stack>
  )
}

export default App
