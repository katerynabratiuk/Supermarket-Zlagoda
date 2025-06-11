--
-- PostgreSQL database dump
--

-- Dumped from database version 16.9
-- Dumped by pg_dump version 16.9

-- Started on 2025-06-11 11:58:07

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 226 (class 1259 OID 16646)
-- Name: authorities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.authorities (
    username character varying(50) NOT NULL,
    authority character varying(50) NOT NULL
);


ALTER TABLE public.authorities OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 16566)
-- Name: category_number_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.category_number_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.category_number_seq OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 16567)
-- Name: category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.category (
    category_number integer DEFAULT nextval('public.category_number_seq'::regclass) NOT NULL,
    category_name character varying(50) NOT NULL
);


ALTER TABLE public.category OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 16571)
-- Name: category_category_number_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.category_category_number_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.category_category_number_seq OWNER TO postgres;

--
-- TOC entry 4960 (class 0 OID 0)
-- Dependencies: 217
-- Name: category_category_number_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.category_category_number_seq OWNED BY public.category.category_number;


--
-- TOC entry 218 (class 1259 OID 16572)
-- Name: customer_card; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.customer_card (
    card_number character varying(13) NOT NULL,
    cust_surname character varying(50) NOT NULL,
    cust_name character varying(50) NOT NULL,
    cust_patronymic character varying(50),
    phone_number character varying(13) NOT NULL,
    city character varying(50),
    street character varying(50),
    zip_code character varying(9),
    percent smallint NOT NULL
);


ALTER TABLE public.customer_card OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16575)
-- Name: employee; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.employee (
    id_employee character varying(10) NOT NULL,
    empl_surname character varying(50) NOT NULL,
    empl_name character varying(50) NOT NULL,
    empl_patronymic character varying(50),
    empl_role character varying(10) NOT NULL,
    salary numeric(13,4) NOT NULL,
    date_of_birth date NOT NULL,
    date_of_start date NOT NULL,
    phone_number character varying(13) NOT NULL,
    city character varying(50) NOT NULL,
    street character varying(50) NOT NULL,
    zip_code character varying(9) NOT NULL,
    empl_username character varying(30)
);


ALTER TABLE public.employee OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16578)
-- Name: product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product (
    id_product integer NOT NULL,
    category_number integer NOT NULL,
    product_name character varying(50) NOT NULL,
    characteristics character varying(100) NOT NULL
);


ALTER TABLE public.product OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16581)
-- Name: product_id_product_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.product_id_product_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.product_id_product_seq OWNER TO postgres;

--
-- TOC entry 4961 (class 0 OID 0)
-- Dependencies: 221
-- Name: product_id_product_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.product_id_product_seq OWNED BY public.product.id_product;


--
-- TOC entry 222 (class 1259 OID 16582)
-- Name: receipt; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.receipt (
    check_number character varying(10) NOT NULL,
    id_employee character varying(10) NOT NULL,
    card_number character varying(13),
    print_date timestamp without time zone NOT NULL,
    sum_total numeric(13,4) NOT NULL,
    vat numeric(13,4) NOT NULL
);


ALTER TABLE public.receipt OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 16585)
-- Name: sale; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sale (
    upc character varying(12) NOT NULL,
    check_number character varying(10) NOT NULL,
    product_number integer NOT NULL,
    selling_price numeric(13,4) NOT NULL
);


ALTER TABLE public.sale OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 16588)
-- Name: store_product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.store_product (
    upc character varying(12) NOT NULL,
    upc_prom character varying(12),
    id_product integer NOT NULL,
    selling_price numeric(13,4) NOT NULL,
    products_number integer NOT NULL,
    promotional_product boolean NOT NULL
);


ALTER TABLE public.store_product OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 16641)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    username character varying(50) NOT NULL,
    password character varying(68) NOT NULL,
    enabled smallint NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 4770 (class 2604 OID 16591)
-- Name: product id_product; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product ALTER COLUMN id_product SET DEFAULT nextval('public.product_id_product_seq'::regclass);


--
-- TOC entry 4954 (class 0 OID 16646)
-- Dependencies: 226
-- Data for Name: authorities; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.authorities (username, authority) FROM stdin;
pavlo	ROLE_MANAGER
pavlo	ROLE_CASHIER
maryna	ROLE_MANAGER
maryna	ROLE_CASHIER
oksana	ROLE_CASHIER
oleh	ROLE_MANAGER
oleh	ROLE_CASHIER
nazar	ROLE_MANAGER
nazar	ROLE_CASHIER
\.


--
-- TOC entry 4944 (class 0 OID 16567)
-- Dependencies: 216
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.category (category_number, category_name) FROM stdin;
1	Dairy
2	Baked
3	Vegetables
4	Fruit
5	Meat
6	Fish
7	Sweets
8	Non-Alcoholic Drinks
9	Alcohol
10	Coffee and Tea
11	Hygiene
12	Home
\.


--
-- TOC entry 4946 (class 0 OID 16572)
-- Dependencies: 218
-- Data for Name: customer_card; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.customer_card (card_number, cust_surname, cust_name, cust_patronymic, phone_number, city, street, zip_code, percent) FROM stdin;
000000000001	Ivanenko	Oleh	Ivanovych	+380971234567	Kyiv	Khreshchatyk street, 10	01001	5
000000000002	Petrova	Mariia	Serhiivna	+380937654321	Lviv	Franka street, 22	79000	10
000000000003	Kovalenko	Andrii	Petrovych	+380509876543	Odesa	Derybasivska street, 5	65000	3
000000000004	Smyrnov	Yurii	Mykolaiovych	+380671112233	Kharkiv	Nauky avenue, 45	61000	7
000000000005	Shevchenko	Iryna	\N	+380733344556	Dnipro	Polia street, 12	49000	0
C0000000001	Leshkiv	Oleksiy	Vasylyovych	+380978716281	Kyiv	Oleksandry Ekster, 14B	02064	5
C0000000002	Martynyuk	Petro	Volodymyrovych	+380639218318	Kyiv	Malokyitaiska, 6	03028	2
C0000000003	Voloshkiv	Oksana	Maksymivna	+380508372898	Kyiv	Vyphleemka, 4	02105	1
C0000000004	Boryschenko	Petro	Oleksandrovych	+380502938931	Kyiv	Nyvska street, 20A	03062	1
C0000000005	Zahray	Lyudmyla	Petrivna	+380984461112	Kyiv	Kharkiv highway, 19B	02157	4
C0000000006	Abramets	Borys	\N	+380638282991	Kyiv	Chaikivska street, 13	03179	5
C0000000007	Bohych	Svitlana	Oleksandrivna	+380971838828	Kyiv	Sklliarenka Semena streer	04120	5
C0000000008	Tereshyn	Olha	Sergiivna	+380982828282	Kyiv	Entuziastiv street, 23	02154	2
C0000000009	Symchyshyn	Oleksandra	Volodymyrivna	+380638283838	Dnipro	Korotka street, 76	49081	5
C0000000010	Ivanshyn	Anna	Borysivna	+380685993939	Kyiv	Zhmerynskyi lane, 4	03148	1
\.


--
-- TOC entry 4947 (class 0 OID 16575)
-- Dependencies: 219
-- Data for Name: employee; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.employee (id_employee, empl_surname, empl_name, empl_patronymic, empl_role, salary, date_of_birth, date_of_start, phone_number, city, street, zip_code, empl_username) FROM stdin;
6	Ivanchuk	Nazar	\N	Manager	180000.0000	1998-07-10	2025-01-13	+380668267184	Vyshneve	Viacheslava Chornovola Street, 44	08132	nazar
1	Myhailenko	Maryna	Oleksandrivna	Cashier	20000.0000	1991-04-20	2024-03-18	+380951234561	Kyiv	Oleksandra Ekster, 28	02064	maryna
2	Shevchenko	Pavlo	Volodymyrovych	Cashier	20000.0000	1986-09-01	2024-03-12	+380671112234	Kyiv	Verbova street, 23	04073	pavlo
3	Pysmychenko	Oksana	\N	Manager	24000.0000	1992-11-12	2023-09-23	+380981991733	Odesa	Malynska street, 6	03164	oksana
5	Pysmychenko	Oleh	\N	Cashier	20000.0000	1992-11-12	2023-09-23	+380971931730	Kyiv	Malynska street, 6	03164	oleh
\.


--
-- TOC entry 4948 (class 0 OID 16578)
-- Dependencies: 220
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.product (id_product, category_number, product_name, characteristics) FROM stdin;
1	1	Milk "Good Cow"	3.5% fat, 1L, 14-day shelf life.
2	1	Cottage Cheese "Good Cow"	12g protein/100g, 4% fat. Calcium, phosphorus. 200g tub.
3	1	Yogurt "Happy Cow"	3.5g protein/100g, 3% fat. Probiotics, calcium. 500g pot.
4	1	Sour Cream "Happy Cow"	20% fat, 2.5g carbs/100g. Fermented dairy. 250ml tub.
5	2	Bread "Golden Crust"	Whole grain, 250g, 3g fiber/100g.
6	3	Carrot "Fresh Harvest"	Vitamin A, 1kg, 41kcal/100g.
7	4	Apple "Red Delicious"	52kcal/100g, vitamin C, 1kg.
8	5	Chicken Breast "Farm Stye"	23g protein/100g, 1.5% fat. 500g.
9	6	Salmon Fillet "Ocean Catch"	20g protein/100g, omega-3. 300g.
10	7	Chocolate "Sweet Bliss"	33% cocoa, 540kcal/100g, 100g bar.
11	8	Juice "Orange Burst"	100% pure, vitamin C, 1L.
12	9	Wine "Red Velvet"	13% alcohol, dry, 750ml.
13	9	Beer "Happy Bear"	10% alcohol, 500 ml.
14	7	Chocolate Cake "Prague"	Chocolate sponge, buttercream, apricot. 1kg, 380kcal/100g. 5-day shelf life.
15	9	Water "Morshynska"	1.5 L
16	3	Sweet Yellow Pepper	1 kg
\.


--
-- TOC entry 4950 (class 0 OID 16582)
-- Dependencies: 222
-- Data for Name: receipt; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.receipt (check_number, id_employee, card_number, print_date, sum_total, vat) FROM stdin;
0000000001	1	C0000000001	2025-01-15 00:00:00	117.8000	23.5600
0000000010	2	\N	2025-02-19 00:00:00	43.0000	8.6000
0000000011	2	C0000000006	2025-02-19 00:00:00	425.0000	85.0000
0000000012	2	C0000000008	2025-02-19 00:00:00	1.0000	0.2000
0000000013	2	C0000000010	2025-02-19 00:00:00	60.0000	12.0000
0000000014	1	C0000000007	2025-02-19 00:00:00	267.0000	53.4000
0000000015	2	C0000000008	2025-02-20 00:00:00	255.0000	51.0000
0000000002	1	\N	2025-01-15 00:00:00	88.0000	17.6000
0000000003	1	C0000000002	2025-01-15 00:00:00	214.8000	42.9600
0000000004	2	\N	2025-02-17 00:00:00	171.2000	34.2400
0000000005	2	C0000000009	2025-02-17 00:00:00	236.0000	47.2000
0000000006	2	C0000000001	2025-02-17 00:00:00	35.0000	7.0000
0000000007	2	C0000000008	2025-02-18 00:00:00	403.0000	80.6000
0000000008	2	C0000000008	2025-02-18 00:00:00	498.0000	99.6000
0000000009	2	C0000000006	2025-02-18 00:00:00	277.0000	55.4000
\.


--
-- TOC entry 4951 (class 0 OID 16585)
-- Dependencies: 223
-- Data for Name: sale; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sale (upc, check_number, product_number, selling_price) FROM stdin;
10000001	0000000003	2	35.0000
10000001	0000000006	1	35.0000
10000002	0000000011	2	28.0000
10000002	0000000012	2	28.0000
10000002	0000000009	3	28.0000
10000003	0000000012	1	22.0000
10000003	0000000015	2	22.0000
10000003	0000000005	4	22.0000
10000003	0000000008	2	22.0000
10000003	0000000009	2	22.0000
10000004	0000000010	1	25.0000
10000004	0000000015	3	25.0000
10000005	0000000010	1	18.0000
10000005	0000000011	2	18.0000
10000005	0000000012	2	18.0000
10000005	0000000004	2	18.0000
10000005	0000000009	2	18.0000
10000006	0000000011	3	16.0000
10000006	0000000015	2	16.0000
10000006	0000000009	2	16.0000
10000007	0000000011	2	27.0000
10000007	0000000012	3	27.0000
10000007	0000000009	3	27.0000
10000008	0000000001	1	55.0000
10000008	0000000011	1	55.0000
10000008	0000000014	2	55.0000
10000008	0000000015	1	55.0000
10000008	0000000008	2	55.0000
10000009	0000000011	2	68.0000
10000009	0000000015	2	68.0000
10000009	0000000004	1	68.0000
10000010	0000000001	1	30.0000
10000010	0000000013	2	30.0000
10000010	0000000005	2	30.0000
10000010	0000000008	1	30.0000
10000011	0000000001	1	20.0000
10000011	0000000011	2	20.0000
10000011	0000000003	5	20.0000
10000011	0000000007	2	20.0000
10000013	0000000014	2	78.5000
10000013	0000000008	4	78.5000
10000014	0000000007	1	275.0000
20000001	0000000003	2	22.4000
20000001	0000000004	3	22.4000
20000006	0000000001	1	12.8000
20000012	0000000002	1	88.0000
20000012	0000000005	1	88.0000
20000012	0000000007	1	88.0000
\.


--
-- TOC entry 4952 (class 0 OID 16588)
-- Dependencies: 224
-- Data for Name: store_product; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.store_product (upc, upc_prom, id_product, selling_price, products_number, promotional_product) FROM stdin;
10000001	\N	1	35.0000	20	f
10000002	\N	2	28.0000	15	f
10000003	\N	3	22.0000	30	f
10000004	\N	4	25.0000	10	f
10000005	\N	5	18.0000	12	f
10000006	\N	6	16.0000	50	f
10000007	\N	7	27.0000	35	f
10000008	\N	8	55.0000	25	f
10000009	\N	9	68.0000	18	f
10000010	\N	10	30.0000	45	f
10000011	\N	11	20.0000	40	f
10000012	\N	12	110.0000	22	f
10000013	\N	13	78.5000	50	f
10000014	\N	14	275.0000	20	f
20000001	10000002	2	22.4000	10	t
20000006	10000006	6	12.8000	30	t
20000012	10000012	12	88.0000	12	t
10000015	10000015	15	25.0000	54	t
10000016	10000016	16	164.9500	37	t
\.


--
-- TOC entry 4953 (class 0 OID 16641)
-- Dependencies: 225
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (username, password, enabled) FROM stdin;
maryna	$2a$12$9iSCJqs7FQDqiMGpzshhluMoXPqLmNupcsryFoK.6kpDUOKU1lrk2	1
nazar	$2a$12$9iSCJqs7FQDqiMGpzshhluMoXPqLmNupcsryFoK.6kpDUOKU1lrk2	1
oksana	$2a$12$9iSCJqs7FQDqiMGpzshhluMoXPqLmNupcsryFoK.6kpDUOKU1lrk2	1
oleh	$2a$12$9iSCJqs7FQDqiMGpzshhluMoXPqLmNupcsryFoK.6kpDUOKU1lrk2	1
pavlo	$2a$12$9iSCJqs7FQDqiMGpzshhluMoXPqLmNupcsryFoK.6kpDUOKU1lrk2	1
\.


--
-- TOC entry 4962 (class 0 OID 0)
-- Dependencies: 217
-- Name: category_category_number_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.category_category_number_seq', 10, true);


--
-- TOC entry 4963 (class 0 OID 0)
-- Dependencies: 215
-- Name: category_number_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.category_number_seq', 12, true);


--
-- TOC entry 4964 (class 0 OID 0)
-- Dependencies: 221
-- Name: product_id_product_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.product_id_product_seq', 16, true);


--
-- TOC entry 4788 (class 2606 OID 16663)
-- Name: authorities authorities_idx_1; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authorities
    ADD CONSTRAINT authorities_idx_1 UNIQUE (username, authority);


--
-- TOC entry 4790 (class 2606 OID 16665)
-- Name: authorities authorities_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authorities
    ADD CONSTRAINT authorities_pk PRIMARY KEY (username, authority);


--
-- TOC entry 4772 (class 2606 OID 16593)
-- Name: category category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (category_number);


--
-- TOC entry 4774 (class 2606 OID 16595)
-- Name: customer_card customer_card_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer_card
    ADD CONSTRAINT customer_card_pkey PRIMARY KEY (card_number);


--
-- TOC entry 4776 (class 2606 OID 16597)
-- Name: employee employee_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT employee_pkey PRIMARY KEY (id_employee);


--
-- TOC entry 4778 (class 2606 OID 16599)
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id_product);


--
-- TOC entry 4780 (class 2606 OID 16601)
-- Name: receipt receipt_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.receipt
    ADD CONSTRAINT receipt_pkey PRIMARY KEY (check_number);


--
-- TOC entry 4782 (class 2606 OID 16603)
-- Name: sale sale_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale
    ADD CONSTRAINT sale_pkey PRIMARY KEY (upc, check_number);


--
-- TOC entry 4784 (class 2606 OID 16605)
-- Name: store_product store_product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.store_product
    ADD CONSTRAINT store_product_pkey PRIMARY KEY (upc);


--
-- TOC entry 4786 (class 2606 OID 16645)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (username);


--
-- TOC entry 4792 (class 2606 OID 16606)
-- Name: product product_category_number_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_category_number_fkey FOREIGN KEY (category_number) REFERENCES public.category(category_number) ON UPDATE CASCADE;


--
-- TOC entry 4793 (class 2606 OID 16611)
-- Name: receipt receipt_card_number_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.receipt
    ADD CONSTRAINT receipt_card_number_fkey FOREIGN KEY (card_number) REFERENCES public.customer_card(card_number) ON UPDATE CASCADE;


--
-- TOC entry 4794 (class 2606 OID 16616)
-- Name: receipt receipt_id_employee_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.receipt
    ADD CONSTRAINT receipt_id_employee_fkey FOREIGN KEY (id_employee) REFERENCES public.employee(id_employee) ON UPDATE CASCADE;


--
-- TOC entry 4795 (class 2606 OID 16621)
-- Name: sale sale_check_number_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale
    ADD CONSTRAINT sale_check_number_fkey FOREIGN KEY (check_number) REFERENCES public.receipt(check_number) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 4796 (class 2606 OID 16626)
-- Name: sale sale_upc_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale
    ADD CONSTRAINT sale_upc_fkey FOREIGN KEY (upc) REFERENCES public.store_product(upc) ON UPDATE CASCADE;


--
-- TOC entry 4797 (class 2606 OID 16631)
-- Name: store_product store_product_id_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.store_product
    ADD CONSTRAINT store_product_id_product_fkey FOREIGN KEY (id_product) REFERENCES public.product(id_product) ON UPDATE CASCADE;


--
-- TOC entry 4798 (class 2606 OID 16636)
-- Name: store_product store_product_upc_prom_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.store_product
    ADD CONSTRAINT store_product_upc_prom_fkey FOREIGN KEY (upc_prom) REFERENCES public.store_product(upc) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 4799 (class 2606 OID 16671)
-- Name: authorities username_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authorities
    ADD CONSTRAINT username_fk FOREIGN KEY (username) REFERENCES public.users(username) ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;


--
-- TOC entry 4791 (class 2606 OID 16676)
-- Name: employee username_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT username_fk FOREIGN KEY (empl_username) REFERENCES public.users(username) ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;


-- Completed on 2025-06-11 11:58:07

--
-- PostgreSQL database dump complete
--

