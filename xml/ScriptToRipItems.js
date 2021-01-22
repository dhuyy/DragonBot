let rows = $($0).find('tbody').children();
let items = ``;

$.each(rows, (_, row) => {
  let _this = $(row);
  let columns = _this.children();
  let name = columns.eq(0).children().eq(0).text().trim();
  let price = columns.eq(2).text();
  let template = `<Item><id>000</id><name>${name}</name><price>${price}</price><buy>000</buy></Item>`;

  items += template;
});

copy(items);

// ============================== JUST CP ====================================

let rows = $($0).find('tbody').children();
let items = ``;

$.each(rows, (_, row) => {
  let _this = $(row);
  let columns = _this.children();
  let name = columns.eq(0).children().eq(0).text().trim();
  let type = columns.eq(1).children().eq(0).text().trim();
  let price = columns.eq(2).text();
  let template = `<Item><id>000</id><name>${name}</name><price>${price}</price><buy>000</buy></Item>`;

  if (type === 'Plants, Animal Products, Food and Drink') {
    items += template;
  }
});

copy(items);
