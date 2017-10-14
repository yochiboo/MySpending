select
 log._id as id,
 log.payment_date as payment_date,
 log.amount as amount,
 log.memo as memo,
 log.class_code as class,
 category.name as category
from log
left outer join category on log.category_code = category.code
where log.payment_date between %d and %d
 and log.class_code=0
order by log.create_date desc
