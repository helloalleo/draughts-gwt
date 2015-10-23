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