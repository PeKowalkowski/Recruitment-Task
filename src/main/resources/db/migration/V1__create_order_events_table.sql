-- Create order_events table for storing all order requests
-- Each request becomes one record (audit log approach)

CREATE TABLE IF NOT EXISTS order_events (
                                          id BIGSERIAL PRIMARY KEY,
                                          tracking_number VARCHAR(255) NOT NULL,
  recipient_email VARCHAR(255) NOT NULL,
  recipient_country_code VARCHAR(2) NOT NULL,
  sender_country_code VARCHAR(2) NOT NULL,
  status_code INTEGER NOT NULL,
  received_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  raw_payload TEXT NOT NULL
  );

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_order_events_tracking
  ON order_events(tracking_number);

CREATE INDEX IF NOT EXISTS idx_order_events_received_at
  ON order_events(received_at DESC);

CREATE INDEX IF NOT EXISTS idx_order_events_email
  ON order_events(recipient_email);

-- Composite index for tracking number timeline queries
CREATE INDEX IF NOT EXISTS idx_order_events_tracking_received
  ON order_events(tracking_number, received_at DESC);

-- Constraints
ALTER TABLE order_events
  ADD CONSTRAINT chk_status_code
    CHECK (status_code >= 0 AND status_code <= 100);

ALTER TABLE order_events
  ADD CONSTRAINT chk_country_code_recipient
    CHECK (LENGTH(recipient_country_code) = 2);

ALTER TABLE order_events
  ADD CONSTRAINT chk_country_code_sender
    CHECK (LENGTH(sender_country_code) = 2);

-- Comments for documentation
COMMENT ON TABLE order_events IS 'Audit log of all order requests from e-commerce platforms';
COMMENT ON COLUMN order_events.tracking_number IS 'Shipment tracking identifier';
COMMENT ON COLUMN order_events.status_code IS 'Order status code (0-100 range)';
COMMENT ON COLUMN order_events.raw_payload IS 'Original JSON request for audit purposes';
COMMENT ON COLUMN order_events.received_at IS 'Timestamp when request was received';
