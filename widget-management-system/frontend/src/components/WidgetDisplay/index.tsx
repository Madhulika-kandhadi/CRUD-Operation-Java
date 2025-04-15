import React from 'react'
import Card from '@mui/material/Card'
import CardContent from '@mui/material/CardContent'
import Grid from '@mui/material/Grid'
import Stack from '@mui/material/Stack'
import Typography from '@mui/material/Typography'

import { Widget } from '../../lib/apiConnect'

export interface DisplayWidgetProps {
  widget: Widget
}

const DisplayWidget = ({ widget }: DisplayWidgetProps): JSX.Element => {
  const { description, name, price } = widget
  return (
    <Grid item xs={6}>
      <Card>
        <CardContent>
          <Stack spacing={2}>
            <Typography component="div" gutterBottom variant="h4">
              {name}
            </Typography>
            <Typography component="div" gutterBottom variant="h5">
              ${price}
            </Typography>
            <Typography color="text.secondary" variant="body2">
              {description}
            </Typography>
          </Stack>
        </CardContent>
      </Card>
  </Grid>)
}

export default DisplayWidget
