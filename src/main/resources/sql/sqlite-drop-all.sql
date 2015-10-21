alter table casinos drop constraint if exists fk_casinos_location_id;
drop index if exists ix_casinos_location_id;

alter table locations drop constraint if exists fk_locations_country_id;
drop index if exists ix_locations_country_id;

alter table poker_chips drop constraint if exists fk_poker_chips_casino_id;
drop index if exists ix_poker_chips_casino_id;

alter table poker_chips drop constraint if exists fk_poker_chips_front_image_id;
drop index if exists ix_poker_chips_front_image_id;

alter table poker_chips drop constraint if exists fk_poker_chips_back_image_id;
drop index if exists ix_poker_chips_back_image_id;

drop table if exists poker_chip_images;

drop table if exists casinos;

drop table if exists countries;

drop table if exists locations;

drop table if exists poker_chips;

