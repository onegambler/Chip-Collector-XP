create table poker_chip_images (
  id                            bigint not null,
  image_byte_array              blob,
  constraint pk_poker_chip_images primary key (id)
);
create sequence POKER_CHIP_IMAGES_seq;

create table casino (
  id                            bigint not null,
  name                          varchar(255) not null,
  type                          varchar(255),
  web_site                      varchar(255),
  open_date                     date,
  close_date                    date,
  theme                         varchar(255),
  location_id                   bigint,
  constraint pk_casino primary key (id)
);
create sequence CASINO_seq;

create table country (
  id                            bigint not null,
  name                          varchar(50),
  flag_image                    blob,
  constraint pk_country primary key (id)
);
create sequence COUNTRY_seq;

create table location (
  id                            bigint not null,
  city                          varchar(255) not null,
  state                         varchar(255) not null,
  country_id                    bigint,
  constraint pk_location primary key (id)
);
create sequence LOCATION_seq;

create table poker_chip (
  id                            integer not null,
  casino                        bigint not null,
  denom                         varchar(50),
  year                          varchar(4),
  color                         varchar(50),
  inserts                       varchar(50),
  mold                          varchar(50),
  tcr_id                        varchar(10),
  issue                         integer,
  inlay                         varchar(50),
  rarity                        varchar(4),
  condition                     varchar(15),
  category                      varchar(25),
  acquisition_date              date,
  notes                         varchar(4000),
  front_image_id                bigint,
  back_image_id                 bigint,
  obsolete                      boolean,
  constraint uq_poker_chip_tcr_id unique (tcr_id),
  constraint pk_poker_chip primary key (id)
);
create sequence POKER_CHIP_seq;

alter table casino add constraint fk_casino_location_id foreign key (location_id) references location (id) on delete restrict on update restrict;
create index ix_casino_location_id on casino (location_id);

alter table location add constraint fk_location_country_id foreign key (country_id) references country (id) on delete restrict on update restrict;
create index ix_location_country_id on location (country_id);

alter table poker_chip add constraint fk_poker_chip_casino foreign key (casino) references casino (id) on delete restrict on update restrict;
create index ix_poker_chip_casino on poker_chip (casino);

alter table poker_chip add constraint fk_poker_chip_front_image_id foreign key (front_image_id) references poker_chip_images (id) on delete restrict on update restrict;
create index ix_poker_chip_front_image_id on poker_chip (front_image_id);

alter table poker_chip add constraint fk_poker_chip_back_image_id foreign key (back_image_id) references poker_chip_images (id) on delete restrict on update restrict;
create index ix_poker_chip_back_image_id on poker_chip (back_image_id);

