-- Findify migration v1 -> v2
-- Run this on an EXISTING findify_db. New installs can just run database.sql.

USE findify_db;

ALTER TABLE claims
    MODIFY COLUMN status ENUM('PENDING','APPROVED','REJECTED','COLLECTED')
    NOT NULL DEFAULT 'PENDING';

ALTER TABLE claims
    ADD COLUMN rejection_reason VARCHAR(500) NULL AFTER ai_reasoning,
    ADD COLUMN collected_at TIMESTAMP NULL DEFAULT NULL AFTER rejection_reason,
    ADD COLUMN handled_by INT NULL AFTER collected_at;

CREATE INDEX idx_claims_found_status ON claims(found_id, status);
CREATE INDEX idx_claims_claimant ON claims(claimant_id);

-- Back-fill: any found item that already has an APPROVED claim is CLAIMED
UPDATE found_items f
SET f.status = 'CLAIMED'
WHERE EXISTS (
    SELECT 1 FROM claims c
    WHERE c.found_id = f.found_id
      AND c.status IN ('APPROVED','COLLECTED')
);
