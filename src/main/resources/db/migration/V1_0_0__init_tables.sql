CREATE TABLE failed_calls (
        id      serial primary key,
        client_id        VARCHAR(36) NOT NULL,
        request_type     VARCHAR(36) NOT NULL,
        payload     character varying NOT NULL,
        reason      character varying NOT NULL,
        time        timestamp NOT NULL,
        processed   boolean default false
);

CREATE TABLE affiliate_client_map (
       id      serial primary key,
       client_id        VARCHAR(36) NOT NULL,
       referral_code    VARCHAR(36),
       click_id         character varying NOT NULL,
       user_agent       character varying NOT NULL,
       ip               VARCHAR(64) NOT NULL,
       creation_date    timestamp NOT NULL
);


CREATE TABLE affiliate_transactions (
     id      serial primary key,
 conversion_id          INTEGER NOT NULL,
     client_id          VARCHAR(36) NOT NULL,
     referral_code      VARCHAR(36),
     order_id           character varying NOT NULL,
     currency           VARCHAR(3) NOT NULL,
     order_amount       integer NOT NULL,
     conversion_amount  integer NOT NULL,
     transaction_type   VARCHAR(16) NOT NULL,
     creation_date      timestamp NOT NULL
);