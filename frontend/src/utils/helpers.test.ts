import { describe, it, expect } from 'vitest';
import { validateRequired } from './helpers';

describe('validateRequired', () => {
    it('validates required fields correctly', () => {
        expect(validateRequired('', 'First name')).toBe('First name is required');
        expect(validateRequired('John', 'First name')).toBeNull();
    });
});
