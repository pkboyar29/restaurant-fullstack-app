CREATE TABLE menu_sections (
	id SERIAL PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	descr VARCHAR(400)
)

INSERT INTO menu_sections (name, descr) VALUES
('Закуски', 'Легкие и аппетитные блюда, идеальные для начала приема пищи. Включают в себя сырые овощи, дипы и маринованные продукты'),
('Супы и Бульоны','Теплые и насыщенные блюда для тех, кто предпочитает начать обед или ужин с чего-то горячего. Включают в себя разнообразные супы и бульоны'),
('Салаты','Легкие и освежающие блюда, включающие свежие овощи, зеленые листья и разнообразные добавки. Отличный выбор для тех, кто следит за здоровьем'),
('Основные блюда из мяса','Изысканные блюда из мяса, приготовленные различными способами. Включают в себя стейки, жаркое, гриль и другие варианты'),
('Основные блюда из рыбы и морепродуктов','Разнообразные блюда из свежей рыбы и морепродуктов. Подходит для любителей легких и диетических блюд.'),
('Паста и Ризотто','Автентичные итальянские блюда, включая разнообразные виды пасты и ризотто с различными добавками и соусами.'),
('Пицца','Популярные итальянские блюда с разнообразными тестами и начинками. Отличный выбор для разделения с друзьями.'),
('Вегетарианское меню','Богатое и разнообразное меню для вегетарианцев, включая блюда с использованием свежих овощей, зерен и соусов.'),
('Десерты и сладости','Искушения для любителей сладкого. Включают в себя торты, пироги, мороженое и другие десерты.'),
('Напитки','Различные напитки, включая алкогольные и безалкогольные. Винная карта, коктейли, чай и кофе.')


CREATE TABLE menu_positions (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    descr VARCHAR(400),
    availability BOOLEAN NOT NULL DEFAULT TRUE,
    date_entered_in_menu DATE NOT NULL DEFAULT CURRENT_DATE,
    price INTEGER NOT NULL,
    portion VARCHAR(40) NOT NULL,
    menu_section INTEGER NOT NULL,
    CONSTRAINT fk_menusection FOREIGN KEY (menu_section) REFERENCES menu_sections(id)
);

INSERT INTO menu_positions (name, descr, price, portion, menu_section) VALUES
('Цезарь с курицей и авокадо','Свежий салат Цезарь с кусочками сочной куриной грудки, листьями хрустящего айсберга, ломтями авокадо, подмаринованными в деликатном соусе Цезарь с добавлением хрустящих крошек хлеба. Подаётся с классическим соусом Цезарь', 390, '25 грамм', 3),
('Греческий салат с оливками и фетой','Классический греческий салат с кубиками ароматного фета, красными помидорами, огурцами, красным луком, оливками и свежими зелеными травами. Подается с лимонным оливковым маслом.',350,'200 грамм',3),
('Классический куриный бульон','Нежный куриный бульон, приготовленный на медленном огне с использованием отборного мяса кур и ароматных трав. Подается с лапшой и зеленым луком.',250,'300 грамм',2),
('Томатный крем-суп с базиликом','Насыщенный крем-суп, приготовленный из спелых помидоров с добавлением нежного коктейля из свежего базилика. Подается с хрустящими гренками.',280,'350 грамм',2),
('Лососевый чау-чау с лимонграссом','Утонченный азиатский суп с кусочками свежего лосося, ароматным лимонграссом, кокосовым молоком и рисовыми лапшами.',320,'400 грамм',2),
('Тирамису','Классический итальянский десерт, состоящий из слоев нежных бисквитов, пропитанных кофе и амаретто, и великолепного маскарпоне-крема, посыпанный какао-порошком.',350,'150 грамм',9),
('Шоколадный Фондан','Идеальное сочетание хрустящей внешней оболочки и мягкого, текучего внутреннего шоколадного кекса. Подается с шариком ванильного мороженого.',450,'200 грамм',9),
('Фруктовый Чизкейк','Легкий чизкейк с освежающими фруктами (клубника, маракуйя, киви) на вершине. Нежное тесто, крем-сыр и сочные фрукты создают гармоничный вкус.',380,'180 грамм',9),
('Эспрессо Мартини','Классический коктейль с крепким эспрессо, водкой и кофейным ликером. Идеальное сочетание энергии кофе и насыщенности коктейля.',400,'150 миллилитров',10),
('Манго-текила смеш','Освежающий коктейль с традиционным мексиканским текилой и сладким манго. Легкая кислинка и фруктовая сладость создают идеальный баланс.',350,'200 миллилитров',10)

CREATE TABLE menu_position_images (
	id SERIAL PRIMARY KEY,
	link VARCHAR(200) NOT NULL,
	order_number INTEGER NOT NULL CHECK(order_number >= 1 AND order_number <= 4),
	menu_position INTEGER NOT NULL,
	CONSTRAINT FK_MenuPosition FOREIGN KEY (menu_position) REFERENCES menu_positions(id)
)

INSERT INTO menu_position_images (link, order_number, menu_position)
VALUES ('1.jpg', 1, 1),
('2.jpg', 2, 1),
('3.jpg', 3, 1),
('o1.jpg', 1, 2),
('forg.jpg', 2, 2)

CREATE TABLE order_discounts (
	id SERIAL PRIMARY KEY,
	required_number_orders INT NOT NULL,
	discount INT NOT NULL
)

INSERT INTO order_discounts (required_number_orders, discount) VALUES
(0, 5), (10, 15), (30, 25), (60, 35)

CREATE TABLE clients (
	id SERIAL PRIMARY KEY,
	username VARCHAR(20) NOT NULL UNIQUE,
	first_name VARCHAR(30) NOT NULL,
	last_name VARCHAR(30) NOT NULL,
	patronymic VARCHAR(30) NOT NULL,
	gender CHAR(3) NOT NULL CHECK (gender IN ('жен', 'муж')),
	phone VARCHAR(12) NOT NULL UNIQUE CHECK (LENGTH(phone) = 11),
	email VARCHAR(60) NOT NULL UNIQUE,
	password VARCHAR(255) NOT NULL,
	date_last_login TIMESTAMP NULL,
	number_orders INT DEFAULT 0 NOT NULL,
	order_discount INT DEFAULT 1 NOT NULL,
	CONSTRAINT fk_order_discount FOREIGN KEY (order_discount) REFERENCES order_discounts(id) -- добавить скидку
)

CREATE TABLE takeaway_orders (
	id SERIAL PRIMARY KEY,
	client_name VARCHAR(30) NOT NULL,
	client_phone VARCHAR(12) NOT NULL,
	client_username VARCHAR(20) NULL DEFAULT NULL,
	requirements VARCHAR(100) NULL,
	cost INT NOT NULL DEFAULT 0 CHECK (cost >= 0),
	discounted_cost INT NOT NULL DEFAULT 0 CHECK (discounted_cost >= 0),
	payment_method VARCHAR(20) NOT NULL CHECK (payment_method IN ('безналичный расчет', 'наличный расчет')),
	order_date TIMESTAMP NOT NULL,
	receipt_date TIMESTAMP NOT NULL,
	receipt_option VARCHAR(10) NOT NULL CHECK (receipt_option IN ('самовывоз', 'доставка'))
)

CREATE TABLE takeaway_order_positions (
	id SERIAL PRIMARY KEY,
	menu_position INT NOT NULL,
	number INT NOT NULL CHECK (number >= 0),
	total_price INT NOT NULL DEFAULT 0,
	takeaway_order INT NOT NULL,
	CONSTRAINT fk_menu_position FOREIGN KEY (menu_position) REFERENCES menu_positions(id),
	CONSTRAINT fk_takeaway_order FOREIGN KEY (takeaway_order) REFERENCES takeaway_orders(id)
)