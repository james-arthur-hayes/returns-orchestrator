-- Create the table only if it doesn't already exist
-- Always use the IF NOT EXISTS command on these table creation scripts
-- Since my application.yml has sql.init.mode: always, Spring runs this on start.

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='return_manifests' AND xtype='U')
BEGIN
CREATE TABLE return_manifests (
                                  id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
                                  tracking_id VARCHAR(50) NOT NULL,
                                  sku VARCHAR(100) NOT NULL,
                                  quantity INT NOT NULL,
                                  customer_email VARCHAR(255),
                                  order_id VARCHAR(100),
                                  status VARCHAR(50),
                                  created_at DATETIME2 DEFAULT GETDATE()
)
END; -- Only one semicolon here to tell Spring the whole block is one command