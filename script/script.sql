SELECT shashist_black.player_name
FROM game
  LEFT JOIN shashist shashist_black ON shashist_black.id = game.player_black_id;

SELECT
  player0_.id                AS id1_6_,
  player0_.auth_provider     AS auth_pro2_6_,
  player0_.email             AS email3_6_,
  player0_.fb_id             AS fb_id4_6_,
  player0_.first_name        AS first_na5_6_,
  player0_.google_sub        AS google_s6_6_,
  player0_.include_in_rating AS include_7_6_,
  player0_.last_name         AS last_nam8_6_,
  player0_.last_visited      AS last_vis9_6_,
  player0_.logged_in         AS logged_10_6_,
  player0_.online            AS online11_6_,
  player0_.player_name       AS player_12_6_,
  player0_.playing           AS playing13_6_,
  player0_.rating            AS rating14_6_,
  player0_.register_date     AS registe15_6_,
  player0_.session_id        AS session16_6_,
  player0_.visit_counter     AS visit_c17_6_,
  player0_.vk_id             AS vk_id18_6_
FROM player player0_;

SELECT
  player2_.id            AS id1_6_,
  player2_.auth_provider AS auth_pro2_6_,
  player2_.email         AS email3_6_,
  player2_.fb_id         AS fb_id4_6_,
  player2_.first_name    AS first_na5_6_,
  player2_.game_draw     AS game_dra6_6_,
  player2_.game_lose     AS game_los7_6_,
  player2_.game_played   AS game_pla8_6_,
  player2_.game_win      AS game_win9_6_,
  player2_.google_sub    AS google_10_6_,
  player2_.last_name     AS last_na11_6_,
  player2_.last_visited  AS last_vi12_6_,
  player2_.logged_in     AS logged_13_6_,
  player2_.online        AS online14_6_,
  player2_.player_name   AS player_15_6_,
  player2_.playing       AS playing16_6_,
  player2_.rating        AS rating17_6_,
  player2_.register_date AS registe18_6_,
  player2_.session_id    AS session19_6_,
  player2_.visit_counter AS visit_c20_6_,
  player2_.vk_id         AS vk_id21_6_
FROM player player0_ LEFT OUTER JOIN friend friends1_ ON player0_.id = friends1_.friend_of_id
  INNER JOIN player player2_ ON friends1_.friend_id = player2_.id
WHERE player0_.id = 2;