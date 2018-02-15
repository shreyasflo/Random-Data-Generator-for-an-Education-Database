Select p.i_id , p.name, p.department_name, p.salary 
from instructor as p,instructor as q 
where p.department_name = q.department_name group by q.department_name having p.salary < (0.35*max(q.salary));