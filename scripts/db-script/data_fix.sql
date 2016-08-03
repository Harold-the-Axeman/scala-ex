

update comment set like_count = (select count(1) from comment_like where comment_id = comment.id);



