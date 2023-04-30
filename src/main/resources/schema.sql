create table if not exists diploma.organization
(
    id          varchar not null
        constraint organization_pk
            primary key,
    name        varchar,
    address     varchar,
    email       varchar,
    short_desc  varchar,
    description varchar,
    created_at  timestamp default now(),
    image_id    integer,
    phone       varchar,
    site        varchar,
    username    varchar
);

alter table diploma.organization
    owner to postgres;

create unique index if not exists organization_id_uindex
    on diploma.organization (id);

create unique index if not exists organization_username_uindex
    on diploma.organization (username);

create table if not exists diploma.event
(
    id                   varchar not null
        constraint event_pk
            primary key,
    name                 varchar,
    description          varchar,
    short_description    varchar,
    location             varchar,
    created_at           timestamp default now(),
    started_at           date,
    ended_at             date,
    constraints          varchar,
    organization         varchar
        constraint org
            references diploma.organization,
    image_id             varchar,
    is_main              boolean   default false,
    day                  varchar not null,
    type                 varchar   default 'general'::character varying,
    time                 varchar   default '00:00'::character varying,
    running_time         integer   default 0,
    limit_participant    integer   default 0,
    current_participants integer   default 0
);

alter table diploma.event
    owner to postgres;

create unique index if not exists event_id_uindex
    on diploma.event (id);

create table if not exists diploma.images
(
    id   varchar not null
        constraint images_pk
            primary key,
    img  bytea,
    name varchar,
    type varchar
);

alter table diploma.images
    owner to postgres;

create table if not exists diploma."user"
(
    id            varchar not null
        constraint user_pk
            primary key,
    first_name    varchar,
    last_name     varchar,
    birth_date    date,
    email         varchar,
    about         varchar,
    phone_numbers varchar,
    created_at    timestamp default now(),
    image_id      varchar
        constraint user_images_id_fk
            references diploma.images,
    username      varchar
);

alter table diploma."user"
    owner to postgres;

create unique index if not exists user_email_uindex
    on diploma."user" (email);

create unique index if not exists user_id_uindex
    on diploma."user" (id);

create unique index if not exists images_id_uindex
    on diploma.images (id);

create table if not exists diploma.user_event
(
    id           varchar not null
        constraint user_event_pk
            primary key,
    user_id      varchar
        constraint event_user__fk
            references diploma."user",
    event_id     varchar
        constraint user_event__fk
            references diploma.event,
    "isFavorite" boolean default false
);

alter table diploma.user_event
    owner to postgres;

create unique index if not exists user_event_id_uindex
    on diploma.user_event (id);

create table if not exists diploma.comment
(
    id       varchar not null
        constraint comment_pk
            primary key,
    user_id  varchar,
    event_id varchar,
    text     varchar,
    date     timestamp default now()
);

alter table diploma.comment
    owner to postgres;

create unique index if not exists comment_id_uindex
    on diploma.comment (id);

create table if not exists diploma.authorities
(
    username  varchar not null
        constraint authorities_pk
            primary key,
    authority varchar not null
);

alter table diploma.authorities
    owner to postgres;

create unique index authorities_username_uindex
    on diploma.authorities (username);

create table if not exists diploma.oauth_access_token
(
    token_id          varchar(255) not null
        primary key,
    token             bytea,
    authentication_id varchar(255) default NULL::character varying,
    user_name         varchar(255) default NULL::character varying,
    client_id         varchar(255) default NULL::character varying,
    authentication    bytea,
    refresh_token     varchar(255) default NULL::character varying
);

alter table diploma.oauth_access_token
    owner to postgres;

create table if not exists diploma.oauth_refresh_token
(
    token_id       varchar(255) not null
        primary key,
    token          bytea,
    authentication bytea
);

alter table diploma.oauth_refresh_token
    owner to postgres;

create table if not exists diploma.users
(
    username varchar not null
        constraint users_pk
            primary key,
    password varchar not null,
    enabled  boolean default true,
    kind     varchar default 'person'::character varying
);

alter table diploma.users
    owner to postgres;

create unique index if not exists users_username_uindex
    on diploma.users (username);

