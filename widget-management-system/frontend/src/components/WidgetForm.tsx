// src/WidgetForm.tsx
import React from 'react';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { Box, Button, Stack, TextField, Typography } from '@mui/material';
import { Widget } from '../lib/apiConnect';
import { widgetSchema, WidgetFormData } from '../validations/widgetSchema';

interface WidgetFormProps {
    onSubmit: (widget: Widget) => void;
    defaultValues?: Partial<Widget>;
    isEdit?: boolean;
}

const WidgetForm = ({
    onSubmit,
    defaultValues,
    isEdit = false,
}: WidgetFormProps): JSX.Element => {
    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm<WidgetFormData>({
        resolver: yupResolver(widgetSchema),
        defaultValues: defaultValues,
    });

    return (
        <Box
            component="form"
            onSubmit={handleSubmit((data) => onSubmit(data as Widget))}
            sx={{ maxWidth: 600, margin: 'auto', padding: 4 }}
        >
            <Stack spacing={3}>
                <Typography variant="h4">{isEdit ? 'Edit Widget' : 'Create Widget'}</Typography>

                <TextField
                    label="Name"
                    {...register('name')}
                    error={!!errors.name}
                    helperText={errors.name?.message}
                    disabled={isEdit} // prevent editing name during update
                />

                <TextField
                    label="Description"
                    multiline
                    rows={4}
                    {...register('description')}
                    error={!!errors.description}
                    helperText={errors.description?.message}
                />

                <TextField
                    label="Price"
                    type="number"
                    inputProps={{ step: '0.01' }}
                    {...register('price')}
                    error={!!errors.price}
                    helperText={errors.price?.message}
                />

                <Button variant="contained" type="submit">
                    {isEdit ? 'Update Widget' : 'Create Widget'}
                </Button>
            </Stack>
        </Box>
    );
};

export default WidgetForm;