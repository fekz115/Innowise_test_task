alter table if exists document_users
    add constraint FKebr6pfci7l6l9rweci69bxl0s
    foreign key (users_id)
    references usr;

alter table if exists document_users
    add constraint FKhtyon02oybqj6ggf0sh998uk4
    foreign key (document_id)
    references document;

alter table if exists user_role
    add constraint FKfpm8swft53ulq2hl11yplpr5
    foreign key (user_id)
    references usr;