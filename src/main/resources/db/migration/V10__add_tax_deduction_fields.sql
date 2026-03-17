-- Add expense_deduction and total_allowance columns to tax_records table
ALTER TABLE tax_records 
ADD COLUMN expense_deduction NUMERIC(12, 2),
ADD COLUMN total_allowance NUMERIC(12, 2);

-- Update existing records to set default values (0) for new columns
UPDATE tax_records 
SET expense_deduction = 0, 
    total_allowance = 0 
WHERE expense_deduction IS NULL OR total_allowance IS NULL;
