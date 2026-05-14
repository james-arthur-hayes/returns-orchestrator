-- 1. Drop the table if it exists so we start with a perfect schema every time
IF OBJECT_ID('return_manifests', 'U') IS NOT NULL
DROP TABLE return_manifests;

-- 2. Create it once with all the columns we need
CREATE TABLE return_manifests (
                                  id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
                                  tracking_id VARCHAR(50) NOT NULL UNIQUE,
                                  sku VARCHAR(100) NOT NULL,
                                  quantity INT NOT NULL,
                                  customer_email VARCHAR(255),
                                  order_id VARCHAR(100),
                                  status VARCHAR(50),
                                  label_url VARCHAR(500), -- Put it here!
                                  created_at DATETIME2 DEFAULT GETDATE()
);