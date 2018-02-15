Select distinct couse_id 
from section 
where semester = ’fall’ and years = 2009 and course_id in
(select course_id from section where semester = ‘spring’ and years = ‘2010’);