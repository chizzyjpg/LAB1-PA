--
-- PostgreSQL database dump
--

-- Dumped from database version 16.8
-- Dumped by pg_dump version 16.8

-- Started on 2025-09-08 15:21:53

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

--
-- TOC entry 869 (class 1247 OID 17323)
-- Name: tipo_asiento; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.tipo_asiento AS ENUM (
    'Turista',
    'Ejecutivo'
);


ALTER TYPE public.tipo_asiento OWNER TO postgres;

--
-- TOC entry 872 (class 1247 OID 17328)
-- Name: tipo_equipaje; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.tipo_equipaje AS ENUM (
    'bolso',
    'mochila',
    'carry-on'
);


ALTER TYPE public.tipo_equipaje OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 215 (class 1259 OID 17335)
-- Name: aerolinea; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.aerolinea (
    nickname character varying(50) NOT NULL,
    descgeneral text,
    linkweb character varying(200)
);


ALTER TABLE public.aerolinea OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 17340)
-- Name: aerolinea_ruta; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.aerolinea_ruta (
    idruta integer,
    nicknameaerolinea character varying(50),
    rutas_idruta integer
);


ALTER TABLE public.aerolinea_ruta OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 17343)
-- Name: aerolinearuta; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.aerolinearuta (
    idaerolinearuta integer NOT NULL,
    idruta integer NOT NULL,
    idvueloespecifico integer NOT NULL
);


ALTER TABLE public.aerolinearuta OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 17346)
-- Name: aerolinearuta_idaerolinearuta_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.aerolinearuta_idaerolinearuta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.aerolinearuta_idaerolinearuta_seq OWNER TO postgres;

--
-- TOC entry 5067 (class 0 OID 0)
-- Dependencies: 218
-- Name: aerolinearuta_idaerolinearuta_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.aerolinearuta_idaerolinearuta_seq OWNED BY public.aerolinearuta.idaerolinearuta;


--
-- TOC entry 219 (class 1259 OID 17347)
-- Name: categoria; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.categoria (
    nombre character varying(255) NOT NULL,
    idruta integer
);


ALTER TABLE public.categoria OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 17350)
-- Name: ciudad; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ciudad (
    idciudad integer NOT NULL,
    idruta integer,
    nombre character varying(80) NOT NULL,
    pais character varying(80) NOT NULL,
    descripcion_aeropuerto character varying(255),
    fecha_alta date NOT NULL,
    nombre_aeropuerto character varying(120) NOT NULL,
    sitio_web character varying(200)
);


ALTER TABLE public.ciudad OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 17355)
-- Name: ciudad_idciudad_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ciudad_idciudad_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ciudad_idciudad_seq OWNER TO postgres;

--
-- TOC entry 5068 (class 0 OID 0)
-- Dependencies: 221
-- Name: ciudad_idciudad_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.ciudad_idciudad_seq OWNED BY public.ciudad.idciudad;


--
-- TOC entry 222 (class 1259 OID 17356)
-- Name: ciudad_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ciudad_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ciudad_seq OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 17357)
-- Name: cliente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cliente (
    nickname character varying(50) NOT NULL,
    apellido character varying(100) NOT NULL,
    nacionalidad character varying(50),
    tipodocumento character varying(255),
    numdocumento character varying(30),
    fechanacimiento timestamp(6) without time zone NOT NULL
);


ALTER TABLE public.cliente OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 17360)
-- Name: comprapaquete; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.comprapaquete (
    id integer NOT NULL,
    costo numeric(12,2) NOT NULL,
    fechacompra date NOT NULL,
    vencimiento date NOT NULL,
    cliente_nickname character varying(50) NOT NULL,
    paquete_nombre character varying(50) NOT NULL
);


ALTER TABLE public.comprapaquete OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 17363)
-- Name: comprapaquete_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.comprapaquete_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.comprapaquete_id_seq OWNER TO postgres;

--
-- TOC entry 5069 (class 0 OID 0)
-- Dependencies: 225
-- Name: comprapaquete_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.comprapaquete_id_seq OWNED BY public.comprapaquete.id;


--
-- TOC entry 226 (class 1259 OID 17364)
-- Name: paquete; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paquete (
    cantrutas integer,
    descripcion character varying(200),
    descuento integer,
    fechacompra timestamp(6) without time zone,
    nombre character varying(50) NOT NULL,
    tipoasiento character varying(255),
    validez integer,
    costo numeric(12,2),
    fechaalta timestamp(6) without time zone,
    CONSTRAINT paquete_tipoasiento_check CHECK (((tipoasiento)::text = ANY (ARRAY[('TURISTA'::character varying)::text, ('EJECUTIVO'::character varying)::text])))
);


ALTER TABLE public.paquete OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 17370)
-- Name: paquete_cupos_por_ruta; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paquete_cupos_por_ruta (
    nombrepaquete_id character varying NOT NULL,
    cupos integer,
    ruta character varying(255) NOT NULL
);


ALTER TABLE public.paquete_cupos_por_ruta OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 17375)
-- Name: paquete_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.paquete_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.paquete_seq OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 17376)
-- Name: pasaje; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pasaje (
    idpasaje integer NOT NULL,
    nombre character varying(50) NOT NULL,
    apellido character varying(50) NOT NULL,
    idreserva integer NOT NULL
);


ALTER TABLE public.pasaje OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 17379)
-- Name: pasaje_idpasaje_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pasaje_idpasaje_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pasaje_idpasaje_seq OWNER TO postgres;

--
-- TOC entry 5070 (class 0 OID 0)
-- Dependencies: 230
-- Name: pasaje_idpasaje_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pasaje_idpasaje_seq OWNED BY public.pasaje.idpasaje;


--
-- TOC entry 231 (class 1259 OID 17380)
-- Name: pasaje_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pasaje_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pasaje_seq OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 17381)
-- Name: reserva; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reserva (
    idreserva integer NOT NULL,
    fechareserva timestamp(6) without time zone,
    tipoasiento character varying,
    cantequipajeextra integer DEFAULT 0,
    costototal real,
    idcliente character varying(50),
    cantpasajes integer,
    equipaje character varying(255),
    vueloespecifico_idvueloespecifico integer,
    aerolinea character varying(255),
    apepasajero character varying(255),
    nompasajero character varying(255),
    rutasvuelo_idruta integer,
    CONSTRAINT reserva_equipaje_check CHECK (((equipaje)::text = ANY (ARRAY[('BOLSO'::character varying)::text, ('MOCHILAYCARRYON'::character varying)::text])))
);


ALTER TABLE public.reserva OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 17389)
-- Name: reserva_idreserva_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.reserva_idreserva_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.reserva_idreserva_seq OWNER TO postgres;

--
-- TOC entry 5071 (class 0 OID 0)
-- Dependencies: 233
-- Name: reserva_idreserva_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.reserva_idreserva_seq OWNED BY public.reserva.idreserva;


--
-- TOC entry 234 (class 1259 OID 17390)
-- Name: reserva_pasaje; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reserva_pasaje (
    reserva_idreserva integer NOT NULL,
    pasajes_idpasaje integer NOT NULL
);


ALTER TABLE public.reserva_pasaje OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 17393)
-- Name: reserva_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.reserva_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.reserva_seq OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 17394)
-- Name: ruta; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ruta (
    idruta integer NOT NULL,
    nombre character varying(100) NOT NULL,
    descripcion text,
    hora integer NOT NULL,
    fechaalta date NOT NULL,
    costoturista numeric(38,2) NOT NULL,
    costoequipajeextra integer DEFAULT 0,
    categoria_nombre character varying(255),
    destino_idciudad integer NOT NULL,
    origen_idciudad integer NOT NULL,
    costoejecutivo numeric(38,2) DEFAULT 0 NOT NULL
);


ALTER TABLE public.ruta OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 17401)
-- Name: ruta_categoria; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ruta_categoria (
    ruta_id integer NOT NULL,
    categoria_nombre character varying(255) NOT NULL
);


ALTER TABLE public.ruta_categoria OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 17404)
-- Name: ruta_idruta_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ruta_idruta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ruta_idruta_seq OWNER TO postgres;

--
-- TOC entry 5072 (class 0 OID 0)
-- Dependencies: 238
-- Name: ruta_idruta_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.ruta_idruta_seq OWNED BY public.ruta.idruta;


--
-- TOC entry 239 (class 1259 OID 17405)
-- Name: ruta_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ruta_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ruta_seq OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 17406)
-- Name: ruta_vueloespecifico; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ruta_vueloespecifico (
    ruta_idruta integer NOT NULL,
    vuelosespecificos_idvueloespecifico integer NOT NULL,
    idruta integer NOT NULL,
    idvueloespecifico integer NOT NULL
);


ALTER TABLE public.ruta_vueloespecifico OWNER TO postgres;

--
-- TOC entry 241 (class 1259 OID 17409)
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuario (
    nickname character varying(50) NOT NULL,
    nombre character varying(100) NOT NULL,
    email character varying(100) NOT NULL
);


ALTER TABLE public.usuario OWNER TO postgres;

--
-- TOC entry 242 (class 1259 OID 17412)
-- Name: vueloespecifico; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.vueloespecifico (
    idvueloespecifico integer NOT NULL,
    nombre character varying(100) NOT NULL,
    fecha timestamp(6) without time zone NOT NULL,
    duracion integer NOT NULL,
    maxasientostur integer NOT NULL,
    maxasientosejec integer NOT NULL,
    fechaalta timestamp(6) without time zone NOT NULL,
    idruta integer
);


ALTER TABLE public.vueloespecifico OWNER TO postgres;

--
-- TOC entry 243 (class 1259 OID 17415)
-- Name: vueloespecifico_idvueloespecifico_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.vueloespecifico_idvueloespecifico_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.vueloespecifico_idvueloespecifico_seq OWNER TO postgres;

--
-- TOC entry 5073 (class 0 OID 0)
-- Dependencies: 243
-- Name: vueloespecifico_idvueloespecifico_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.vueloespecifico_idvueloespecifico_seq OWNED BY public.vueloespecifico.idvueloespecifico;


--
-- TOC entry 244 (class 1259 OID 17416)
-- Name: vueloespecifico_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.vueloespecifico_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.vueloespecifico_seq OWNER TO postgres;

--
-- TOC entry 4817 (class 2604 OID 17417)
-- Name: aerolinearuta idaerolinearuta; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.aerolinearuta ALTER COLUMN idaerolinearuta SET DEFAULT nextval('public.aerolinearuta_idaerolinearuta_seq'::regclass);


--
-- TOC entry 4818 (class 2604 OID 17418)
-- Name: ciudad idciudad; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ciudad ALTER COLUMN idciudad SET DEFAULT nextval('public.ciudad_idciudad_seq'::regclass);


--
-- TOC entry 4819 (class 2604 OID 17419)
-- Name: comprapaquete id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comprapaquete ALTER COLUMN id SET DEFAULT nextval('public.comprapaquete_id_seq'::regclass);


--
-- TOC entry 4820 (class 2604 OID 17420)
-- Name: pasaje idpasaje; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pasaje ALTER COLUMN idpasaje SET DEFAULT nextval('public.pasaje_idpasaje_seq'::regclass);


--
-- TOC entry 4821 (class 2604 OID 17421)
-- Name: reserva idreserva; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reserva ALTER COLUMN idreserva SET DEFAULT nextval('public.reserva_idreserva_seq'::regclass);


--
-- TOC entry 4823 (class 2604 OID 17422)
-- Name: ruta idruta; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ruta ALTER COLUMN idruta SET DEFAULT nextval('public.ruta_idruta_seq'::regclass);


--
-- TOC entry 4826 (class 2604 OID 17423)
-- Name: vueloespecifico idvueloespecifico; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vueloespecifico ALTER COLUMN idvueloespecifico SET DEFAULT nextval('public.vueloespecifico_idvueloespecifico_seq'::regclass);


--
-- TOC entry 5032 (class 0 OID 17335)
-- Dependencies: 215
-- Data for Name: aerolinea; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.aerolinea (nickname, descgeneral, linkweb) VALUES ('jet', 'jet', 'jet.com');
INSERT INTO public.aerolinea (nickname, descgeneral, linkweb) VALUES ('plu', 'jaja nos fundimos', 'pluna.com');
INSERT INTO public.aerolinea (nickname, descgeneral, linkweb) VALUES ('bus', 'asasa', 'bus.com');


--
-- TOC entry 5033 (class 0 OID 17340)
-- Dependencies: 216
-- Data for Name: aerolinea_ruta; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.aerolinea_ruta (idruta, nicknameaerolinea, rutas_idruta) VALUES (1053, 'jet', NULL);
INSERT INTO public.aerolinea_ruta (idruta, nicknameaerolinea, rutas_idruta) VALUES (1102, 'bus', NULL);
INSERT INTO public.aerolinea_ruta (idruta, nicknameaerolinea, rutas_idruta) VALUES (1152, 'plu', NULL);
INSERT INTO public.aerolinea_ruta (idruta, nicknameaerolinea, rutas_idruta) VALUES (1202, 'jet', NULL);
INSERT INTO public.aerolinea_ruta (idruta, nicknameaerolinea, rutas_idruta) VALUES (1252, 'plu', NULL);
INSERT INTO public.aerolinea_ruta (idruta, nicknameaerolinea, rutas_idruta) VALUES (1302, 'bus', NULL);
INSERT INTO public.aerolinea_ruta (idruta, nicknameaerolinea, rutas_idruta) VALUES (1352, 'bus', NULL);


--
-- TOC entry 5034 (class 0 OID 17343)
-- Dependencies: 217
-- Data for Name: aerolinearuta; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5036 (class 0 OID 17347)
-- Dependencies: 219
-- Data for Name: categoria; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.categoria (nombre, idruta) VALUES ('INTERNACIONAL', NULL);
INSERT INTO public.categoria (nombre, idruta) VALUES ('NACIONAL', NULL);
INSERT INTO public.categoria (nombre, idruta) VALUES ('ECONOMICA', NULL);


--
-- TOC entry 5037 (class 0 OID 17350)
-- Dependencies: 220
-- Data for Name: ciudad; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.ciudad (idciudad, idruta, nombre, pais, descripcion_aeropuerto, fecha_alta, nombre_aeropuerto, sitio_web) VALUES (602, NULL, 'Montevideo', 'Uruguay', 'pamba', '2025-09-01', 'Carrasco', 'carrasco.com');
INSERT INTO public.ciudad (idciudad, idruta, nombre, pais, descripcion_aeropuerto, fecha_alta, nombre_aeropuerto, sitio_web) VALUES (603, NULL, 'Buenos Aires', 'Argentina', 'lleve', '2025-09-01', 'Ezeiza', 'ezeiza.com');
INSERT INTO public.ciudad (idciudad, idruta, nombre, pais, descripcion_aeropuerto, fecha_alta, nombre_aeropuerto, sitio_web) VALUES (702, NULL, 'Madrid', 'España', 'barajita', '2025-09-01', 'Adolfo Suárez', 'adolf.com');
INSERT INTO public.ciudad (idciudad, idruta, nombre, pais, descripcion_aeropuerto, fecha_alta, nombre_aeropuerto, sitio_web) VALUES (752, NULL, 'Sao Pablo', 'Brasil', 'SOPA DO MACACO', '2025-08-15', 'Guarulhos', 'guaru.com');
INSERT INTO public.ciudad (idciudad, idruta, nombre, pais, descripcion_aeropuerto, fecha_alta, nombre_aeropuerto, sitio_web) VALUES (802, NULL, 'Salto', 'Uruguay', 'jijia', '2025-09-03', 'Nueva Hespérides', 'hespe.com');
INSERT INTO public.ciudad (idciudad, idruta, nombre, pais, descripcion_aeropuerto, fecha_alta, nombre_aeropuerto, sitio_web) VALUES (852, NULL, 'Barcelona', 'España', 'asasa', '2025-09-03', 'Aeropuerto', 'aero.com');


--
-- TOC entry 5040 (class 0 OID 17357)
-- Dependencies: 223
-- Data for Name: cliente; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.cliente (nickname, apellido, nacionalidad, tipodocumento, numdocumento, fechanacimiento) VALUES ('crisrey', 'Reyes', 'Uruguay', 'CEDULA', '1234567', '2025-09-02 00:00:00');
INSERT INTO public.cliente (nickname, apellido, nacionalidad, tipodocumento, numdocumento, fechanacimiento) VALUES ('mmarceee', 'Marcenal', 'Uruguay', 'CEDULA', '54708129', '2004-09-19 01:00:00');


--
-- TOC entry 5041 (class 0 OID 17360)
-- Dependencies: 224
-- Data for Name: comprapaquete; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.comprapaquete (id, costo, fechacompra, vencimiento, cliente_nickname, paquete_nombre) VALUES (3, 140.00, '2025-09-04', '2025-10-19', 'mmarceee', 'Paquete6');
INSERT INTO public.comprapaquete (id, costo, fechacompra, vencimiento, cliente_nickname, paquete_nombre) VALUES (4, 140.00, '2025-09-04', '2025-10-19', 'mmarceee', 'Paquete6');
INSERT INTO public.comprapaquete (id, costo, fechacompra, vencimiento, cliente_nickname, paquete_nombre) VALUES (5, 140.00, '2025-09-04', '2025-10-19', 'crisrey', 'Paquete6');
INSERT INTO public.comprapaquete (id, costo, fechacompra, vencimiento, cliente_nickname, paquete_nombre) VALUES (6, 120.00, '2025-09-08', '2025-10-08', 'mmarceee', 'Paquete 8');
INSERT INTO public.comprapaquete (id, costo, fechacompra, vencimiento, cliente_nickname, paquete_nombre) VALUES (7, 190.00, '2025-09-08', '2025-10-08', 'mmarceee', 'Nuevito');
INSERT INTO public.comprapaquete (id, costo, fechacompra, vencimiento, cliente_nickname, paquete_nombre) VALUES (8, 190.00, '2025-09-08', '2025-10-08', 'crisrey', 'Nuevito');


--
-- TOC entry 5043 (class 0 OID 17364)
-- Dependencies: 226
-- Data for Name: paquete; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.paquete (cantrutas, descripcion, descuento, fechacompra, nombre, tipoasiento, validez, costo, fechaalta) VALUES (0, 'lalala', 20, NULL, 'Paquete1', NULL, 45, NULL, NULL);
INSERT INTO public.paquete (cantrutas, descripcion, descuento, fechacompra, nombre, tipoasiento, validez, costo, fechaalta) VALUES (0, 'lelelel', 10, NULL, 'Paquete 2', NULL, 30, NULL, NULL);
INSERT INTO public.paquete (cantrutas, descripcion, descuento, fechacompra, nombre, tipoasiento, validez, costo, fechaalta) VALUES (1, 'asas', 3, NULL, 'asa', 'TURISTA', 30, 97.00, NULL);
INSERT INTO public.paquete (cantrutas, descripcion, descuento, fechacompra, nombre, tipoasiento, validez, costo, fechaalta) VALUES (1, 'asasas', 20, NULL, 'PAQUETE5', 'EJECUTIVO', 30, 480.00, NULL);
INSERT INTO public.paquete (cantrutas, descripcion, descuento, fechacompra, nombre, tipoasiento, validez, costo, fechaalta) VALUES (1, 'desc', 30, NULL, 'Paquete6', 'TURISTA', 45, 140.00, NULL);
INSERT INTO public.paquete (cantrutas, descripcion, descuento, fechacompra, nombre, tipoasiento, validez, costo, fechaalta) VALUES (1, 'lalall', 20, NULL, 'Paquete 8', 'TURISTA', 30, 120.00, NULL);
INSERT INTO public.paquete (cantrutas, descripcion, descuento, fechacompra, nombre, tipoasiento, validez, costo, fechaalta) VALUES (2, 'somos', 5, NULL, 'Nuevito', 'TURISTA', 30, 574.75, NULL);


--
-- TOC entry 5044 (class 0 OID 17370)
-- Dependencies: 227
-- Data for Name: paquete_cupos_por_ruta; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.paquete_cupos_por_ruta (nombrepaquete_id, cupos, ruta) VALUES ('52', 1, 'brrrr');
INSERT INTO public.paquete_cupos_por_ruta (nombrepaquete_id, cupos, ruta) VALUES ('202', 3, 'ruta1');
INSERT INTO public.paquete_cupos_por_ruta (nombrepaquete_id, cupos, ruta) VALUES ('Paquete6', 2, 'ruta1');
INSERT INTO public.paquete_cupos_por_ruta (nombrepaquete_id, cupos, ruta) VALUES ('Paquete 8', 3, 'local');
INSERT INTO public.paquete_cupos_por_ruta (nombrepaquete_id, cupos, ruta) VALUES ('Nuevito', 3, 'si anda tiramos cohetes');
INSERT INTO public.paquete_cupos_por_ruta (nombrepaquete_id, cupos, ruta) VALUES ('Nuevito', 1, 'tatantatan');


--
-- TOC entry 5046 (class 0 OID 17376)
-- Dependencies: 229
-- Data for Name: pasaje; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.pasaje (idpasaje, nombre, apellido, idreserva) VALUES (1, 'Cristian', 'Reyes', 302);
INSERT INTO public.pasaje (idpasaje, nombre, apellido, idreserva) VALUES (2, 'Cristian', 'Reyes', 303);
INSERT INTO public.pasaje (idpasaje, nombre, apellido, idreserva) VALUES (3, 'Lucas', 'Ottonello', 303);
INSERT INTO public.pasaje (idpasaje, nombre, apellido, idreserva) VALUES (52, 'Ezequiel', 'Marcenal Criado', 352);
INSERT INTO public.pasaje (idpasaje, nombre, apellido, idreserva) VALUES (53, 'Lucas', 'Ottonello', 352);


--
-- TOC entry 5049 (class 0 OID 17381)
-- Dependencies: 232
-- Data for Name: reserva; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.reserva (idreserva, fechareserva, tipoasiento, cantequipajeextra, costototal, idcliente, cantpasajes, equipaje, vueloespecifico_idvueloespecifico, aerolinea, apepasajero, nompasajero, rutasvuelo_idruta) VALUES (302, '2025-09-08 14:47:56.02', 'TURISTA', 0, 100, 'crisrey', 0, 'BOLSO', 452, NULL, NULL, NULL, NULL);
INSERT INTO public.reserva (idreserva, fechareserva, tipoasiento, cantequipajeextra, costototal, idcliente, cantpasajes, equipaje, vueloespecifico_idvueloespecifico, aerolinea, apepasajero, nompasajero, rutasvuelo_idruta) VALUES (303, '2025-09-08 14:48:45.041', 'TURISTA', 0, 24, 'crisrey', 0, 'BOLSO', 702, NULL, NULL, NULL, NULL);
INSERT INTO public.reserva (idreserva, fechareserva, tipoasiento, cantequipajeextra, costototal, idcliente, cantpasajes, equipaje, vueloespecifico_idvueloespecifico, aerolinea, apepasajero, nompasajero, rutasvuelo_idruta) VALUES (352, '2025-09-08 15:03:02.69', 'TURISTA', 1, 25, 'mmarceee', 0, 'MOCHILAYCARRYON', 702, NULL, NULL, NULL, NULL);


--
-- TOC entry 5051 (class 0 OID 17390)
-- Dependencies: 234
-- Data for Name: reserva_pasaje; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5053 (class 0 OID 17394)
-- Dependencies: 236
-- Data for Name: ruta; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.ruta (idruta, nombre, descripcion, hora, fechaalta, costoturista, costoequipajeextra, categoria_nombre, destino_idciudad, origen_idciudad, costoejecutivo) VALUES (852, 'Saltini', 'sasas', 16, '2025-09-03', 50.00, 20, 'NACIONAL', 802, 602, 100.00);
INSERT INTO public.ruta (idruta, nombre, descripcion, hora, fechaalta, costoturista, costoequipajeextra, categoria_nombre, destino_idciudad, origen_idciudad, costoejecutivo) VALUES (902, 'Mvd', 'lllll', 16, '2025-09-03', 100.00, 50, 'NACIONAL', 602, 802, 200.00);
INSERT INTO public.ruta (idruta, nombre, descripcion, hora, fechaalta, costoturista, costoequipajeextra, categoria_nombre, destino_idciudad, origen_idciudad, costoejecutivo) VALUES (952, 'Madrid', 'papapap', 11, '2025-09-03', 500.00, 200, 'INTERNACIONAL', 702, 602, 1000.00);
INSERT INTO public.ruta (idruta, nombre, descripcion, hora, fechaalta, costoturista, costoequipajeextra, categoria_nombre, destino_idciudad, origen_idciudad, costoejecutivo) VALUES (1002, 'Anda', 'asas', 17, '2025-09-03', 50.00, 10, 'NACIONAL', 802, 602, 100.00);
INSERT INTO public.ruta (idruta, nombre, descripcion, hora, fechaalta, costoturista, costoequipajeextra, categoria_nombre, destino_idciudad, origen_idciudad, costoejecutivo) VALUES (1053, 'Brrrr', 'sasas', 16, '2025-09-03', 100.00, 50, 'INTERNACIONAL', 752, 602, 200.00);
INSERT INTO public.ruta (idruta, nombre, descripcion, hora, fechaalta, costoturista, costoequipajeextra, categoria_nombre, destino_idciudad, origen_idciudad, costoejecutivo) VALUES (1102, 'Ruta1', 'asas', 15, '2025-09-04', 100.00, 50, 'INTERNACIONAL', 852, 602, 200.00);
INSERT INTO public.ruta (idruta, nombre, descripcion, hora, fechaalta, costoturista, costoequipajeextra, categoria_nombre, destino_idciudad, origen_idciudad, costoejecutivo) VALUES (1152, 'Local', 'Salto mvd', 16, '2025-09-06', 50.00, 20, 'NACIONAL', 802, 602, 100.00);
INSERT INTO public.ruta (idruta, nombre, descripcion, hora, fechaalta, costoturista, costoequipajeextra, categoria_nombre, destino_idciudad, origen_idciudad, costoejecutivo) VALUES (1202, 'Esp-Mvd', 'aaaaaa', 15, '2025-09-06', 400.00, 100, 'INTERNACIONAL', 602, 852, 800.00);
INSERT INTO public.ruta (idruta, nombre, descripcion, hora, fechaalta, costoturista, costoequipajeextra, categoria_nombre, destino_idciudad, origen_idciudad, costoejecutivo) VALUES (1252, 'zaaza', 'aaa', 16, '2025-09-06', 12.00, 1, 'INTERNACIONAL', 852, 602, 141.00);
INSERT INTO public.ruta (idruta, nombre, descripcion, hora, fechaalta, costoturista, costoequipajeextra, categoria_nombre, destino_idciudad, origen_idciudad, costoejecutivo) VALUES (1302, 'si anda tiramos cohetes', 'sisisi', 15, '2025-09-08', 200.00, 100, 'INTERNACIONAL', 603, 852, 400.00);
INSERT INTO public.ruta (idruta, nombre, descripcion, hora, fechaalta, costoturista, costoequipajeextra, categoria_nombre, destino_idciudad, origen_idciudad, costoejecutivo) VALUES (1352, 'tatantatan', 'lalala', 15, '2025-09-08', 5.00, 1, 'NACIONAL', 702, 852, 6.00);


--
-- TOC entry 5054 (class 0 OID 17401)
-- Dependencies: 237
-- Data for Name: ruta_categoria; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5057 (class 0 OID 17406)
-- Dependencies: 240
-- Data for Name: ruta_vueloespecifico; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5058 (class 0 OID 17409)
-- Dependencies: 241
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.usuario (nickname, nombre, email) VALUES ('jet', 'JetSmart', 'jet@gmail.com');
INSERT INTO public.usuario (nickname, nombre, email) VALUES ('plu', 'Pluna', 'pluna@gmail.com');
INSERT INTO public.usuario (nickname, nombre, email) VALUES ('mmarceee', 'Ezequiel', 'ema@gmail.com');
INSERT INTO public.usuario (nickname, nombre, email) VALUES ('crisrey', 'Cristian', 'cris@gmail.com');
INSERT INTO public.usuario (nickname, nombre, email) VALUES ('bus', 'JetBus', 'bus@gmail.com');


--
-- TOC entry 5059 (class 0 OID 17412)
-- Dependencies: 242
-- Data for Name: vueloespecifico; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.vueloespecifico (idvueloespecifico, nombre, fecha, duracion, maxasientostur, maxasientosejec, fechaalta, idruta) VALUES (2, 'Eshhhhpaña tio', '2025-09-07 00:00:00', 12, 12, 10, '2025-09-04 00:00:00', NULL);
INSERT INTO public.vueloespecifico (idvueloespecifico, nombre, fecha, duracion, maxasientostur, maxasientosejec, fechaalta, idruta) VALUES (52, 'aaaaaaaa', '2025-09-06 00:00:00', 112, 1, 1, '2025-09-06 11:48:35.241', NULL);
INSERT INTO public.vueloespecifico (idvueloespecifico, nombre, fecha, duracion, maxasientostur, maxasientosejec, fechaalta, idruta) VALUES (102, 'zazaza', '2025-09-06 11:56:23.542', 12, 1, 1, '2025-09-06 11:56:27.408', NULL);
INSERT INTO public.vueloespecifico (idvueloespecifico, nombre, fecha, duracion, maxasientostur, maxasientosejec, fechaalta, idruta) VALUES (452, 'zaza', '2025-09-06 13:49:37.109', 11, 1, 1, '2025-09-06 00:00:00', 1102);
INSERT INTO public.vueloespecifico (idvueloespecifico, nombre, fecha, duracion, maxasientostur, maxasientosejec, fechaalta, idruta) VALUES (702, 'pamba', '2025-09-07 00:00:00', 12, 11, 12, '2025-09-06 14:35:36.191', 1252);
INSERT INTO public.vueloespecifico (idvueloespecifico, nombre, fecha, duracion, maxasientostur, maxasientosejec, fechaalta, idruta) VALUES (752, 'pappapapa', '2025-09-08 00:00:00', 12, 1, 1, '2025-09-08 11:23:18.941', 1102);


--
-- TOC entry 5074 (class 0 OID 0)
-- Dependencies: 218
-- Name: aerolinearuta_idaerolinearuta_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.aerolinearuta_idaerolinearuta_seq', 1, false);


--
-- TOC entry 5075 (class 0 OID 0)
-- Dependencies: 221
-- Name: ciudad_idciudad_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.ciudad_idciudad_seq', 1, false);


--
-- TOC entry 5076 (class 0 OID 0)
-- Dependencies: 222
-- Name: ciudad_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.ciudad_seq', 901, true);


--
-- TOC entry 5077 (class 0 OID 0)
-- Dependencies: 225
-- Name: comprapaquete_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.comprapaquete_id_seq', 8, true);


--
-- TOC entry 5078 (class 0 OID 0)
-- Dependencies: 228
-- Name: paquete_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.paquete_seq', 251, true);


--
-- TOC entry 5079 (class 0 OID 0)
-- Dependencies: 230
-- Name: pasaje_idpasaje_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pasaje_idpasaje_seq', 1, false);


--
-- TOC entry 5080 (class 0 OID 0)
-- Dependencies: 231
-- Name: pasaje_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pasaje_seq', 101, true);


--
-- TOC entry 5081 (class 0 OID 0)
-- Dependencies: 233
-- Name: reserva_idreserva_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.reserva_idreserva_seq', 1, false);


--
-- TOC entry 5082 (class 0 OID 0)
-- Dependencies: 235
-- Name: reserva_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.reserva_seq', 401, true);


--
-- TOC entry 5083 (class 0 OID 0)
-- Dependencies: 238
-- Name: ruta_idruta_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.ruta_idruta_seq', 1, false);


--
-- TOC entry 5084 (class 0 OID 0)
-- Dependencies: 239
-- Name: ruta_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.ruta_seq', 1401, true);


--
-- TOC entry 5085 (class 0 OID 0)
-- Dependencies: 243
-- Name: vueloespecifico_idvueloespecifico_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.vueloespecifico_idvueloespecifico_seq', 1, false);


--
-- TOC entry 5086 (class 0 OID 0)
-- Dependencies: 244
-- Name: vueloespecifico_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.vueloespecifico_seq', 801, true);


--
-- TOC entry 4830 (class 2606 OID 17425)
-- Name: aerolinea aerolinea_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.aerolinea
    ADD CONSTRAINT aerolinea_pkey PRIMARY KEY (nickname);


--
-- TOC entry 4832 (class 2606 OID 17427)
-- Name: aerolinearuta aerolinearuta_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.aerolinearuta
    ADD CONSTRAINT aerolinearuta_pkey PRIMARY KEY (idaerolinearuta);


--
-- TOC entry 4834 (class 2606 OID 17429)
-- Name: categoria categoria_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categoria
    ADD CONSTRAINT categoria_pkey PRIMARY KEY (nombre);


--
-- TOC entry 4836 (class 2606 OID 17431)
-- Name: ciudad ciudad_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ciudad
    ADD CONSTRAINT ciudad_pkey PRIMARY KEY (idciudad);


--
-- TOC entry 4838 (class 2606 OID 17433)
-- Name: cliente cliente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT cliente_pkey PRIMARY KEY (nickname);


--
-- TOC entry 4840 (class 2606 OID 17435)
-- Name: comprapaquete comprapaquete_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comprapaquete
    ADD CONSTRAINT comprapaquete_pkey PRIMARY KEY (id);


--
-- TOC entry 4844 (class 2606 OID 17437)
-- Name: paquete_cupos_por_ruta paquete_cupos_por_ruta_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paquete_cupos_por_ruta
    ADD CONSTRAINT paquete_cupos_por_ruta_pkey PRIMARY KEY (nombrepaquete_id, ruta);


--
-- TOC entry 4842 (class 2606 OID 17439)
-- Name: paquete paquete_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paquete
    ADD CONSTRAINT paquete_pkey PRIMARY KEY (nombre);


--
-- TOC entry 4846 (class 2606 OID 17441)
-- Name: pasaje pasaje_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pasaje
    ADD CONSTRAINT pasaje_pkey PRIMARY KEY (idpasaje);


--
-- TOC entry 4848 (class 2606 OID 17443)
-- Name: reserva reserva_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reserva
    ADD CONSTRAINT reserva_pkey PRIMARY KEY (idreserva);


--
-- TOC entry 4850 (class 2606 OID 17445)
-- Name: ruta ruta_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ruta
    ADD CONSTRAINT ruta_pkey PRIMARY KEY (idruta);


--
-- TOC entry 4852 (class 2606 OID 17447)
-- Name: ruta rutas_nombre_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ruta
    ADD CONSTRAINT rutas_nombre_unique UNIQUE (nombre);


--
-- TOC entry 4854 (class 2606 OID 17449)
-- Name: ruta_categoria uk_mhws86ud0n4h98e2iu7i964t1; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ruta_categoria
    ADD CONSTRAINT uk_mhws86ud0n4h98e2iu7i964t1 UNIQUE (categoria_nombre);


--
-- TOC entry 4856 (class 2606 OID 17451)
-- Name: usuario usuario_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_email_key UNIQUE (email);


--
-- TOC entry 4858 (class 2606 OID 17453)
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (nickname);


--
-- TOC entry 4860 (class 2606 OID 17455)
-- Name: vueloespecifico vueloespecifico_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vueloespecifico
    ADD CONSTRAINT vueloespecifico_pkey PRIMARY KEY (idvueloespecifico);


--
-- TOC entry 4869 (class 2606 OID 17456)
-- Name: cliente cliente_nickname_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT cliente_nickname_fkey FOREIGN KEY (nickname) REFERENCES public.usuario(nickname) ON DELETE CASCADE;


--
-- TOC entry 4879 (class 2606 OID 17461)
-- Name: ruta fk1f0xdd24sop1nxkmodt5yak3m; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ruta
    ADD CONSTRAINT fk1f0xdd24sop1nxkmodt5yak3m FOREIGN KEY (origen_idciudad) REFERENCES public.ciudad(idciudad);


--
-- TOC entry 4862 (class 2606 OID 17466)
-- Name: aerolinea_ruta fk3bpco816x8fwkyt8hmx28rlm8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.aerolinea_ruta
    ADD CONSTRAINT fk3bpco816x8fwkyt8hmx28rlm8 FOREIGN KEY (idruta) REFERENCES public.ruta(idruta);


--
-- TOC entry 4884 (class 2606 OID 17476)
-- Name: ruta_vueloespecifico fk5lq0h0ldq88k0wwoh1pq0lw4c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ruta_vueloespecifico
    ADD CONSTRAINT fk5lq0h0ldq88k0wwoh1pq0lw4c FOREIGN KEY (vuelosespecificos_idvueloespecifico) REFERENCES public.vueloespecifico(idvueloespecifico);


--
-- TOC entry 4885 (class 2606 OID 17481)
-- Name: ruta_vueloespecifico fk7jpf882c9mirdj6afp4ugky52; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ruta_vueloespecifico
    ADD CONSTRAINT fk7jpf882c9mirdj6afp4ugky52 FOREIGN KEY (idruta) REFERENCES public.ruta(idruta);


--
-- TOC entry 4863 (class 2606 OID 17486)
-- Name: aerolinea_ruta fk7rbv3itlie3s5h19xvrqiopfy; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.aerolinea_ruta
    ADD CONSTRAINT fk7rbv3itlie3s5h19xvrqiopfy FOREIGN KEY (nicknameaerolinea) REFERENCES public.aerolinea(nickname);


--
-- TOC entry 4880 (class 2606 OID 17491)
-- Name: ruta fk831sysbfgwlgq1pubuqmmd8eh; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ruta
    ADD CONSTRAINT fk831sysbfgwlgq1pubuqmmd8eh FOREIGN KEY (destino_idciudad) REFERENCES public.ciudad(idciudad);


--
-- TOC entry 4874 (class 2606 OID 17639)
-- Name: reserva fk9qocf2nu6bsuu5lqsyuoh2t03; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reserva
    ADD CONSTRAINT fk9qocf2nu6bsuu5lqsyuoh2t03 FOREIGN KEY (rutasvuelo_idruta) REFERENCES public.ruta(idruta);


--
-- TOC entry 4865 (class 2606 OID 17501)
-- Name: aerolinearuta fk_aerolinearuta_ruta; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.aerolinearuta
    ADD CONSTRAINT fk_aerolinearuta_ruta FOREIGN KEY (idruta) REFERENCES public.ruta(idruta);


--
-- TOC entry 4866 (class 2606 OID 17506)
-- Name: aerolinearuta fk_aerolinearuta_vuelo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.aerolinearuta
    ADD CONSTRAINT fk_aerolinearuta_vuelo FOREIGN KEY (idvueloespecifico) REFERENCES public.vueloespecifico(idvueloespecifico);


--
-- TOC entry 4868 (class 2606 OID 17511)
-- Name: ciudad fk_ciudad_ruta; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ciudad
    ADD CONSTRAINT fk_ciudad_ruta FOREIGN KEY (idruta) REFERENCES public.ruta(idruta) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 4872 (class 2606 OID 17516)
-- Name: paquete_cupos_por_ruta fk_nombrepaqueteid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paquete_cupos_por_ruta
    ADD CONSTRAINT fk_nombrepaqueteid FOREIGN KEY (nombrepaquete_id) REFERENCES public.paquete(nombre) NOT VALID;


--
-- TOC entry 4875 (class 2606 OID 17521)
-- Name: reserva fk_reserva_cliente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reserva
    ADD CONSTRAINT fk_reserva_cliente FOREIGN KEY (idcliente) REFERENCES public.cliente(nickname);


--
-- TOC entry 4888 (class 2606 OID 17536)
-- Name: vueloespecifico fk_rutavueloespecifico; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vueloespecifico
    ADD CONSTRAINT fk_rutavueloespecifico FOREIGN KEY (idruta) REFERENCES public.ruta(idruta) NOT VALID;


--
-- TOC entry 4870 (class 2606 OID 17541)
-- Name: comprapaquete fkci4e9lh6d1pt5ciko858dqvy6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comprapaquete
    ADD CONSTRAINT fkci4e9lh6d1pt5ciko858dqvy6 FOREIGN KEY (cliente_nickname) REFERENCES public.cliente(nickname);


--
-- TOC entry 4871 (class 2606 OID 17546)
-- Name: comprapaquete fketh7dfu5gr4dyr6w7kcpwada0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comprapaquete
    ADD CONSTRAINT fketh7dfu5gr4dyr6w7kcpwada0 FOREIGN KEY (paquete_nombre) REFERENCES public.paquete(nombre);


--
-- TOC entry 4881 (class 2606 OID 17551)
-- Name: ruta fkfg3ocp3qr623l79rtcnllqpg2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ruta
    ADD CONSTRAINT fkfg3ocp3qr623l79rtcnllqpg2 FOREIGN KEY (categoria_nombre) REFERENCES public.categoria(nombre);


--
-- TOC entry 4882 (class 2606 OID 17566)
-- Name: ruta_categoria fkhn0bvbrfb1lw6160hiceuwb3f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ruta_categoria
    ADD CONSTRAINT fkhn0bvbrfb1lw6160hiceuwb3f FOREIGN KEY (categoria_nombre) REFERENCES public.categoria(nombre);


--
-- TOC entry 4861 (class 2606 OID 17571)
-- Name: aerolinea fki3dliy59om8aar2is5r8o2kje; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.aerolinea
    ADD CONSTRAINT fki3dliy59om8aar2is5r8o2kje FOREIGN KEY (nickname) REFERENCES public.usuario(nickname);


--
-- TOC entry 4886 (class 2606 OID 17576)
-- Name: ruta_vueloespecifico fkk47oc9rflhm1te9iaqedihelx; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ruta_vueloespecifico
    ADD CONSTRAINT fkk47oc9rflhm1te9iaqedihelx FOREIGN KEY (ruta_idruta) REFERENCES public.ruta(idruta);


--
-- TOC entry 4877 (class 2606 OID 17581)
-- Name: reserva_pasaje fklpi3coa9o94piej5we177rnwd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reserva_pasaje
    ADD CONSTRAINT fklpi3coa9o94piej5we177rnwd FOREIGN KEY (reserva_idreserva) REFERENCES public.reserva(idreserva);


--
-- TOC entry 4876 (class 2606 OID 17586)
-- Name: reserva fkmbo1ligtkftn4fqrnprfds1td; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reserva
    ADD CONSTRAINT fkmbo1ligtkftn4fqrnprfds1td FOREIGN KEY (vueloespecifico_idvueloespecifico) REFERENCES public.vueloespecifico(idvueloespecifico);


--
-- TOC entry 4887 (class 2606 OID 17591)
-- Name: ruta_vueloespecifico fkntggneuc9oml3eggni3ele0ao; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ruta_vueloespecifico
    ADD CONSTRAINT fkntggneuc9oml3eggni3ele0ao FOREIGN KEY (idvueloespecifico) REFERENCES public.vueloespecifico(idvueloespecifico);


--
-- TOC entry 4878 (class 2606 OID 17596)
-- Name: reserva_pasaje fkod2209npr52ywo4uq3owmmvnj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reserva_pasaje
    ADD CONSTRAINT fkod2209npr52ywo4uq3owmmvnj FOREIGN KEY (pasajes_idpasaje) REFERENCES public.pasaje(idpasaje);


--
-- TOC entry 4883 (class 2606 OID 17601)
-- Name: ruta_categoria fkpol08i0b63nhuj32w9fo3dj30; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ruta_categoria
    ADD CONSTRAINT fkpol08i0b63nhuj32w9fo3dj30 FOREIGN KEY (ruta_id) REFERENCES public.ruta(idruta);


--
-- TOC entry 4864 (class 2606 OID 17606)
-- Name: aerolinea_ruta fkpy2f489md7o6fh1k83dw68db6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.aerolinea_ruta
    ADD CONSTRAINT fkpy2f489md7o6fh1k83dw68db6 FOREIGN KEY (rutas_idruta) REFERENCES public.ruta(idruta);


--
-- TOC entry 4867 (class 2606 OID 17611)
-- Name: categoria idruta; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categoria
    ADD CONSTRAINT idruta FOREIGN KEY (idruta) REFERENCES public.ruta(idruta);


--
-- TOC entry 4873 (class 2606 OID 17616)
-- Name: pasaje pasaje_idreserva_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pasaje
    ADD CONSTRAINT pasaje_idreserva_fkey FOREIGN KEY (idreserva) REFERENCES public.reserva(idreserva);


-- Completed on 2025-09-08 15:21:54

--
-- PostgreSQL database dump complete
--

