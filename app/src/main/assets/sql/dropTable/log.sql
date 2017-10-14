create table log (
    _id integer primary key autoincrement,
    payment_date numeric not null,
    amount numeric,
    memo text,
    class_code numeric,
    category_code numeric,
    wallet_code numeric,
    shop_code numeric,
    create
    _date numeric,
    update_date numeric
);
