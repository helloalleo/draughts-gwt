UPDATE game_message
SET message_type = 'PLAY_OPPONENT_MOVE'
WHERE message_type = 'PLAY_MOVE';
