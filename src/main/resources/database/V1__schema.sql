CREATE TABLE account (
account_number varchar NOT NULL,
sort_code varchar NOT NULL,
CONSTRAINT account_pk PRIMARY KEY (sort_code, account_number)) ;

CREATE TABLE account_balance (account_number varchar NOT NULL,
sort_code varchar NOT NULL,
available_balance numeric (13, 2) NOT NULL,
CONSTRAINT account_balance_pk PRIMARY KEY (account_number, sort_code)) ;
