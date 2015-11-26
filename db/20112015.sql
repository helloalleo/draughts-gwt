UPDATE game_message
SET message_type = 'PLAY_OPPONENT_MOVE'
WHERE message_type = 'PLAY_MOVE';


SELECT
  player0_.id            AS id1_6_,
  player0_.auth_provider AS auth_pro2_6_,
  player0_.banned        AS banned3_6_,
  player0_.email         AS email4_6_,
  player0_.fb_id         AS fb_id5_6_,
  player0_.first_name    AS first_na6_6_,
  player0_.game_draw     AS game_dra7_6_,
  player0_.game_lose     AS game_los8_6_,
  player0_.game_played   AS game_pla9_6_,
  player0_.game_win      AS game_wi10_6_,
  player0_.google_sub    AS google_11_6_,
  player0_.last_name     AS last_na12_6_,
  player0_.last_visited  AS last_vi13_6_,
  player0_.logged_in     AS logged_14_6_,
  player0_.online        AS online15_6_,
  player0_.player_name   AS player_16_6_,
  player0_.playing       AS playing17_6_,
  player0_.rating        AS rating18_6_,
  player0_.register_date AS registe19_6_,
  player0_.session_id    AS session20_6_,
  player0_.subscribed    AS subscri21_6_,
  player0_.visit_counter AS visit_c22_6_,
  player0_.vk_id         AS vk_id23_6_
FROM player player0_ INNER JOIN unreaded_messages friendunre1_ ON player0_.id = friendunre1_.id
WHERE player0_.session_id = 'zesc4eqdd0x';
