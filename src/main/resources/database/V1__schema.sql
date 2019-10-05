CREATE TABLE account (account_number varchar NOT NULL,
sort_code varchar NOT NULL,
PRIMARY KEY (account_number, sort_code)) ;

CREATE TABLE account_balance (account_number varchar NOT NULL,
sort_code varchar NOT NULL,
available_balance numeric (13, 2) NOT NULL,
PRIMARY KEY (account_number, sort_code),
FOREIGN KEY (account_number, sort_code) REFERENCES account (account_number, sort_code)) ;

CREATE
TYPE transaction_type AS ENUM ( 'DIRECT_PAYMENT' , 'DIRECT_DEPOSIT' ) ;

CREATE TABLE transaction ( id SERIAL NOT NULL ,
account_number varchar NOT NULL ,
sort_code varchar NOT NULL ,
type transaction_type NOT NULL ,
amount numeric ( 13 , 2 ) NOT NULL ,
date_time TIMESTAMP NOT NULL ,
PRIMARY KEY ( id , account_number , sort_code ) ,
FOREIGN KEY ( account_number , sort_code ) REFERENCES account ( account_number , sort_code ) ) ;
