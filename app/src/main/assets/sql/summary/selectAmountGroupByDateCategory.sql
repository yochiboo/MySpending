select
 log.payment_date as payment_date,
 log.category_code as category_code,
 category.name as category_name,
 sum(amount) as amount
from log
left outer join category on log.category_code = category.code
where log.payment_date between %d and %d
 and log.class_code=0
group by log.payment_date, log.category_code, category.name
order by log.payment_date desc, log.category_code