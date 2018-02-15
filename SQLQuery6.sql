select i_id 
from teaches 
where sec_id in(select sec_id 
				from section 
				where building in(select building 
								  from department 
								  where department_name = ’CHM’));