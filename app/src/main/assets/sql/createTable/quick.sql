create table quick (
    _id integer primary key autoincrement,
    amount numeric,
    memo text,
    class_code numeric,
    category_code numeric,
    wallet_code numeric,
    shop_code numeric,
    view_order numeric,
    icon_src text
);
<!-- 初期データ -->
insert into category (amount, memo, class_code, category_code, wallet_code, shop_code, view_order, icon_src) values
    (120, 'ドリンク', 0, -1, 0, '');
