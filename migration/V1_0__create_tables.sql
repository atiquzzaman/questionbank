create table public.system_user
(
    id           serial
        primary key,
    active       boolean,
    created_at   timestamp not null,
    email        varchar(255)
        unique,
    name         varchar(255),
    passwordhash varchar(255),
    passwordsalt varchar(255),
    type         varchar(255),
    updated_at   timestamp not null,
    created_by   bigint
        constraint fk_system_user_created_by
            references public.system_user,
    updated_by   bigint
        constraint fk_system_user_updated_by
            references public.system_user
);

alter table public.system_user
    owner to question_bank;

create table public.category
(
    id          serial
        primary key,
    active      boolean,
    created_at  timestamp not null,
    description varchar(255),
    name        varchar(255),
    updated_at  timestamp not null,
    created_by  bigint
        constraint fk_category_created_by
            references public.system_user,
    updated_by  bigint
        constraint fk_category_updated_by
            references public.system_user
);

alter table public.category
    owner to question_bank;

create table public.question
(
    id              serial
        primary key,
    active          boolean,
    approval_status varchar(255),
    created_at      timestamp not null,
    level           varchar(255),
    problem         varchar(255),
    solution        varchar(255),
    updated_at      timestamp not null,
    category_id     bigint
        constraint fk_question_category_id
            references public.category,
    created_by      bigint
        constraint fk_question_created_by
            references public.system_user,
    updated_by      bigint
        constraint fk_question_updated_by
            references public.system_user
);

alter table public.question
    owner to question_bank;

create table public.reviewer_decision
(
    id              serial
        primary key,
    approval_status varchar(255),
    created_at      timestamp not null,
    updated_at      timestamp not null,
    created_by      bigint
        constraint fk_reviewer_decision_created_by
            references public.system_user,
    question_id     bigint
        constraint fk_reviewer_decision_question_id
            references public.question,
    reviewer_id     bigint
        constraint fk_reviewer_decision_reviewer_id
            references public.system_user,
    updated_by      bigint
        constraint fk_reviewer_decision_updated_by
            references public.system_user
);

alter table public.reviewer_decision
    owner to question_bank;

create table public.contributor
(
    category_id bigint not null
        constraint fk_contributor_category_id
            references public.category,
    user_id     bigint not null
        constraint fk_contributor_user_id
            references public.system_user,
    primary key (category_id, user_id)
);

alter table public.contributor
    owner to question_bank;

create table public.reviewer
(
    category_id bigint not null
        constraint fk_reviewer_category_id
            references public.category,
    user_id     bigint not null
        constraint fk_reviewer_user_id
            references public.system_user,
    primary key (category_id, user_id)
);

alter table public.reviewer
    owner to question_bank;

