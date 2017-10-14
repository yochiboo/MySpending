select
 payment_date as payment_date,
 sum(amount) as amount
from log
where payment_date between %d and %d
 and log.class_code=0
group by log.payment_date
order by log.payment_date desc