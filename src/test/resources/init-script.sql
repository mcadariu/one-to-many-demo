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

create index idx_books_author_id ON books(author_id);

insert into authors (name, bio)
select
    'Author ' || seq,
    'Bio for author ' || seq
from generate_series(1, 1000) seq;

insert into books (author_id, title, isbn, published_year)
select
    a.id as author_id,
    'Book ' || gs as title,
    random()::bigint::text as isbn,
    (2000 + FLOOR(random() * 25))::int as published_year
from authors a, generate_series(1, 30) as gs;