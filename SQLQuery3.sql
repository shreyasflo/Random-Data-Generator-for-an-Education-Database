Select count (*) as num_instructors 
from department as d1, instructor as i, department as d2
where d1.department_name = i.department_name and d2.department_name=’MTH’ and d2.building = d1.building;