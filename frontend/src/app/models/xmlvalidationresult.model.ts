import { ValidationError } from './validationerror.model';

export interface XmlValidationResult {
  valid: boolean;
  error?: ValidationError | null;
}