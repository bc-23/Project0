SELECT * FROM bank.bankaccount;
use bank;
CREATE TABLE trans (CustomerID int, lastTran float, FOREIGN KEY (CustomerID) REFERENCES bankaccount(CustomerID));
SELECT CustomerID FROM bankaccount;
CREATE TABLE trans2 (CustomerID int, lastTran float);
