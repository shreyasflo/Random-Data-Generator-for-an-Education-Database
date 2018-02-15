Select department_name,max(salary),min(salary),avg(salary) 
from instructor 
group by department_name;