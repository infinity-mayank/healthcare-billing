import { describe, it, expect } from 'vitest';
import { render, screen } from '@testing-library/react';
import { MainLayout } from './MainLayout';

describe('MainLayout', () => {
    it('renders without crashing', () => {
        render(<MainLayout><h1>Main Layout</h1></MainLayout>);
        const container = screen.getByRole('heading', { level: 1 });
        expect(container).toBeDefined();
    });
});