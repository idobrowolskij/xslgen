export interface ValidationError {
    line: number;
    column: number;
    message: string;
}