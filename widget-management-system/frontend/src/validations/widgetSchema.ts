// src/lib/widgetSchema.ts
import * as yup from 'yup';

// Define the Yup schema for widget validation
export const widgetSchema = yup.object({
    name: yup
        .string()
        .min(3, 'Name must be at least 3 characters')
        .max(100, 'Name cannot exceed 100 characters')
        .required('Name is required'),
    description: yup
        .string()
        .min(5, 'Description must be at least 5 characters')
        .max(1000, 'Description cannot exceed 1000 characters')
        .required('Description is required'),
    price: yup
        .number()
        .typeError('Price must be a number')
        .min(1, 'Price must be at least 1')
        .max(20000, 'Price must be at most 20000')
        .test('is-decimal', 'Price must have 2 decimal places', (value) =>
            /^\d+(\.\d{1,2})?$/.test(String(value))
        )
        .required('Price is required'),
});

// Derive the TypeScript type from the schema
export type WidgetFormData = yup.InferType<typeof widgetSchema>;