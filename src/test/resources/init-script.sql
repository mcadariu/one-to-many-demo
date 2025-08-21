create table authors (
    id bigint primary key generated always as identity,
    name varchar(255) not null,
    bio text
);

create table books (
    id bigint primary key generated always as identity,
    title varchar(255) not null,
    isbn varchar(13),
    published_year integer,
    author_id bigint not null,
    constraint fk_books_author foreign key (author_id) references authors(id)
);

insert into authors (name, bio)
select
    'Author ' || seq,
    'Bio for author ' || seq
from generate_series(1, 1000) seq;

INSERT INTO books (author_id, title, isbn, published_year)
SELECT
    a.id AS author_id,
    'Book ' || gs AS title,
    LPAD((FLOOR(random() * 1e13))::bigint::text, 13, '0') AS isbn,
    (2000 + FLOOR(random() * 25))::int AS published_year
FROM authors a, generate_series(1, 30) AS gs;