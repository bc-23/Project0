CREATE DATABASE bank;
use bank;
CREATE TABLE Bankaccount(
CustomerId INT NOT NULL, 
LastName varchar(255) NOT NULL,
FirstName varchar(255) NOT NULL,
PhoneNumber varchar(255) NOT NULL,
balance float,
PRIMARY KEY (CustomerId)
);
SHOW tables; 
INSERT INTO Bankaccount (CustomerId, LastName, FirstName, PhoneNumber, balance) VALUES ("001", "Anderson", "Simon", "800-555-1212", "1000");
SELECT * FROM Bankaccount;
DELETE FROM Bankaccount WHERE CustomerId = '001';
SHOW tables;
ALTER TABLE Bankaccount ADD acctStatus CHAR;

INSERT INTO Bankaccount (CustomerId, LastName, FirstName, PhoneNumber, balance) VALUES ("002", "Blight", "Aex", "480-444-1212", "2000");
use bank;
SELECT * FROM bankaccount
UPDATE Bankaccount
SET balance = 1;
ALTER TABLE Bankaccount DROP acctStatus;