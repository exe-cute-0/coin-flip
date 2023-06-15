create table round
(
    round_id     int unsigned auto_increment
        primary key,
    created      datetime(3) not null,
    flipped      datetime(3) null,
    outcome      char        null,
    player_heads varchar(20) null,
    player_tails varchar(20) null
);

create index round_created_index
    on round (created);

create index round_flipped_index
    on round (flipped);

