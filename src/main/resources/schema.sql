

CREATE TABLE if NOT EXISTS users
(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TYPE if not exists booking_status as ENUM ('WAITING', 'APPROVED', 'REJECTED', 'CANCELED');

CREATE TABLE if NOT EXISTS requests
(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY primary key,
    description VARCHAR(200) NOT NULL,
    requestor_id INTEGER,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_requestor_requests FOREIGN KEY (requestor_id) REFERENCES users (id)
);

CREATE TABLE if NOT EXISTS items
(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(200) NOT NULL,
    available BOOLEAN,
    owner_id INTEGER references users (id),
    request_id INTEGER references requests (id),
    CONSTRAINT pk_item PRIMARY KEY (id)
);

CREATE TABLE if NOT EXISTS bookings
(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY primary key,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    item_id INTEGER references items (id),
    booker_id INTEGER references users (id),
    status booking_status NOT NULL,
    CONSTRAINT pk_booking PRIMARY KEY (id)

);

CREATE TABLE if NOT EXISTS comments
(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY primary key,
    text VARCHAR(200) NOT NULL,
    item_id INTEGER references items (id),
    author_id INTEGER references users (id),
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_comments PRIMARY KEY (id)
);
