
/***** QUERY 1 *****/
select m.name, u.first_name, u.last_name
from user u
inner join preference p
on u.user_id = p.user_id
and u.user_id = 25
inner join movie m
on p.category_id = m.category_id
where m.movie_id not in (select wm2.movie_id
                                     from watched_movie wm2
                                     where wm2.user_id = u.user_id)
and p.category_id in 
     (select m2.category_id
      from movie m2
      inner join watched_movie wm
      on wm.movie_id = m2.movie_id
      inner join rating r
      on wm.movie_id = r.movie_id
      where wm.position = 'Full'
      and wm.user_id = u.user_id
      and r.user_id = u.user_id
      and r.star_rating in (4,5));
/***** QUERY 1 *****/



/***** QUERY 2 *****/
select m.name, u.first_name, u.last_name
from user u
inner join preference p
on u.user_id = p.user_id
and u.user_id = 25
inner join movie m
on p.category_id = m.category_id
where m.movie_id not in (select wm2.movie_id
                                     from watched_movie wm2
                                     where wm2.user_id = u.user_id)
and p.category_id in 
     (select m2.category_id
      from movie m2
      inner join watched_movie wm
      on wm.movie_id = m2.movie_id
      inner join rating r
      on wm.movie_id = r.movie_id
      where wm.position = 'Full'
      and wm.user_id = u.user_id
      and r.user_id = u.user_id
      and r.star_rating in (4,5)
      or wm.position = 'Partial'
      and wm.user_id = u.user_id
      and r.user_id = u.user_id
      and r.star_rating in (4,5));
/***** QUERY 2 *****/



/***** QUERY 3 *****/
select distinct m2.name, u.first_name, u.last_name
from user as u
inner join watched_movie as wm
on u.user_id = wm.user_id
and u.user_id = 25
inner join rating r
on wm.movie_id = r.movie_id
inner join movie m
on wm.movie_id = m.movie_id
inner join movie m2
on m.category_id = m2.category_id
where wm.position = 'Partial'
      and wm.user_id = 25
      and r.user_id = u.user_id
      and r.star_rating = 1
      and m2.movie_id not in (select movie_id from watched_movie
                                             where user_id = u.user_id)
      and m2.movie_id not in (select category_id from preference
                                             where user_id = u.user_id);
/***** QUERY 3 *****/




/***** QUERY 4 *****/
select name, 'Love' as Level from
(select m.name, u.first_name, u.last_name
from user u
inner join preference p
on u.user_id = p.user_id
and u.user_id = 25
inner join movie m
on p.category_id = m.category_id
where m.movie_id not in (select wm2.movie_id
                                     from watched_movie wm2
                                     where wm2.user_id = u.user_id)
and p.category_id in 
     (select m2.category_id
      from movie m2
      inner join watched_movie wm
      on wm.movie_id = m2.movie_id
      inner join rating r
      on wm.movie_id = r.movie_id
      where wm.position = 'Full'
      and wm.user_id = u.user_id
      and r.user_id = u.user_id
      and r.star_rating = 5)) as group_one
union 
select name, 'Likely to like' as Level from 
(select m.name, u.first_name, u.last_name
from user u
inner join preference p
on u.user_id = p.user_id
and u.user_id = 25
inner join movie m
on p.category_id = m.category_id
where m.movie_id not in (select wm2.movie_id
                                     from watched_movie wm2
                                     where wm2.user_id = u.user_id)
and p.category_id in 
     (select m2.category_id
      from movie m2
      inner join watched_movie wm
      on wm.movie_id = m2.movie_id
      inner join rating r
      on wm.movie_id = r.movie_id
      where wm.position = 'Partial'
      and wm.user_id = u.user_id
      and r.user_id = u.user_id
      and r.star_rating in (3,4))) as group_two
union
select name, 'Less likely to like' as Level from
(select distinct m2.name, u.first_name, u.last_name
from user as u
inner join watched_movie as wm
on u.user_id = wm.user_id
and u.user_id = 25 
inner join rating r
on wm.movie_id = r.movie_id
inner join movie m
on wm.movie_id = m.movie_id
inner join movie m2
on m.category_id = m2.category_id
where wm.position = 'Partial'
      and wm.user_id = 25
      and r.user_id = u.user_id
      and r.star_rating = 2
      and m2.movie_id not in (select movie_id from watched_movie
                                             where user_id = u.user_id)
      and m2.movie_id not in (select category_id from preference
                                             where user_id = u.user_id)) as group_three
union 
select name, 'Hate' as Level from
(select distinct m2.name, u.first_name, u.last_name
from user as u
inner join watched_movie as wm
on u.user_id = wm.user_id
and u.user_id = 25 
inner join rating r
on wm.movie_id = r.movie_id
inner join movie m
on wm.movie_id = m.movie_id
inner join movie m2
on m.category_id = m2.category_id
where wm.position = 'Partial'
      and wm.user_id = 25
      and r.user_id = u.user_id
      and r.star_rating = 1
      and m2.movie_id not in (select movie_id from watched_movie
                                             where user_id = u.user_id)
      and m2.movie_id not in (select category_id from preference
                                             where user_id = u.user_id)) as group_four;
/***** QUERY 4 *****/













