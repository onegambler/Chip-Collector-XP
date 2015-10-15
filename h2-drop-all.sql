alter table casino drop constraint if exists fk_casino_location_id;
drop index if exists ix_casino_location_id;

alter table location drop constraint if exists fk_location_country_id;
drop index if exists ix_location_country_id;

alter table poker_chip drop constraint if exists fk_poker_chip_casino;
drop index if exists ix_poker_chip_casino;

alter table poker_chip drop constraint if exists fk_poker_chip_front_image_id;
drop index if exists ix_poker_chip_front_image_id;

alter table poker_chip drop constraint if exists fk_poker_chip_back_image_id;
drop index if exists ix_poker_chip_back_image_id;

drop table if exists poker_chip_images;
drop sequence if exists POKER_CHIP_IMAGES_seq;

drop table if exists casino;
drop sequence if exists CASINO_seq;

drop table if exists country;
drop sequence if exists COUNTRY_seq;

drop table if exists location;
drop sequence if exists LOCATION_seq;

drop table if exists poker_chip;
drop sequence if exists POKER_CHIP_seq;

