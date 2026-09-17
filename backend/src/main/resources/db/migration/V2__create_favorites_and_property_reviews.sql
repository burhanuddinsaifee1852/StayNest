create table favorites (
    id uuid primary key,
    user_id uuid not null references users(id) on delete cascade,
    property_id uuid not null,
    created_at timestamp with time zone not null,
    constraint uq_favorites_user_property unique (user_id, property_id)
);

create index idx_favorites_user_created_at on favorites (user_id, created_at desc);

create table property_reviews (
    id uuid primary key,
    user_id uuid not null references users(id) on delete cascade,
    property_id uuid not null,
    rating smallint not null check (rating between 1 and 5),
    comment varchar(2000) not null,
    visible boolean not null default true,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    constraint uq_property_reviews_property_user unique (property_id, user_id)
);

create index idx_property_reviews_property_created_at
    on property_reviews (property_id, created_at desc);
