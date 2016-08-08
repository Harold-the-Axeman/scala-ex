

update comment set like_count = (select count(1) from comment_like where comment_id = comment.id);

update url set submit_count = (select count(1) from submit where url_id = url.id);
update url set like_count = (select count(1) from url_like where url_id = url.id);
update url set comment_count = (select count(1) from comment where url_id = url.id);

update user set like_count = (select count(1) from user_relation where `from` = user.id);
update user set submit_count = (select count(1) from submit where user_id = user.id);
update user set comment_count = (select count(1) from comment where user_id = user.id);





