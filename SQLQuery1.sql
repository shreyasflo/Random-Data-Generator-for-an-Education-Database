Select d.department_name, count(i.department_name) as total_professors 
from department as d,instructor as i 
where i.department_name = d.department_name group by department_name;