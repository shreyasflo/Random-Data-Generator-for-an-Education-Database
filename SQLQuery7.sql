select d.department_name,d.budget 
from department as d,instructor as i 
where i.department_name=d.department_name 
	  group by department_name 
	  having avg(salary)>90000;