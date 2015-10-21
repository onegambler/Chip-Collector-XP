create table countries (
  id                            integer primary key autoincrement,
  name                          varchar(50),
  flag_image                    blob
);

create table poker_chip_images (
  id                            integer primary key autoincrement,
  image                         blob not null,
  thumbnail                     blob not null
);

create table locations (
  id                            integer primary key autoincrement,
  city                          varchar(255) not null,
  state                         varchar(255) not null,
  country_id                    integer not null,
  foreign key (country_id)      references countries (id) on delete restrict on update restrict
);

create table casinos (
  id                            integer primary key autoincrement,
  name                          varchar(255) not null,
  type                          varchar(255),
  web_site                      varchar(255),
  open_date                     date,
  close_date                    date,
  theme                         varchar(255),
  location_id                   integer not null,
  foreign key (location_id)     references countries (id) on delete restrict on update restrict
);

create table poker_chips (
  id                            integer primary key autoincrement,
  casino_id                     integer not null,
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
  front_image_id                integer,
  back_image_id                 integer,
  obsolete                      integer(1),
  constraint uq_poker_chips_tcr_id unique (tcr_id),
  foreign key (casino_id)       references casinos (id) on delete restrict on update restrict,
  foreign key (front_image_id)  references poker_chip_images (id) on delete restrict on update restrict,
  foreign key (back_image_id)   references poker_chip_images (id) on delete restrict on update restrict
);

PRAGMA foreign_keys = ON;