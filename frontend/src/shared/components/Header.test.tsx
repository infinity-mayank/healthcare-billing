import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { Header } from './Header';
import { APP_CONFIG } from '../../constants';

describe('Header', () => {
    it('should render the app title from config', () => {
        render(<Header />);

        const heading = screen.getByRole('heading', { level: 1 });
        expect(heading).toHaveTextContent(APP_CONFIG.APP_TITLE);
    });

    it('should render the health icon', () => {
        const { container } = render(<Header />);

        const icon = container.querySelector('svg');
        expect(icon).toBeInTheDocument();
    });
});
