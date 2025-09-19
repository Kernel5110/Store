# Inventario-Miscelanea
Proyecto de Conmutación

# Planteamiento
En las misceláneas se presenta un problema recurrente al momento de buscar productos almacenados, ya sea en la bodega o en un almacén. La falta de organización y orden genera complicaciones, sobre todo con los productos perecederos que requieren venderse en un tiempo determinado para evitar pérdidas económicas.

Actualmente, el negocio lleva el control de inventario de manera manual o mediante herramientas básicas como hojas de cálculo. Este enfoque presenta varias limitaciones que afectan la eficiencia operativa. El registro, seguimiento y análisis del inventario consumen demasiado tiempo del personal, lo que resta atención a tareas clave como la atención al cliente o la planificación de compras. Además, el control manual es propenso a errores como registros duplicados, omisiones o discrepancias en las cantidades de productos, lo que provoca faltantes o sobrantes de mercancía.

Otro problema importante es la falta de un sistema que alerte sobre niveles bajos de stock, lo que ocasiona quiebres de inventario, afectando la satisfacción del cliente y, en algunos casos, la pérdida de los mismos. También resulta difícil conocer el estado real del inventario en tiempo oportuno, lo que limita la toma de decisiones rápidas, como el reabastecimiento de productos o la identificación de tendencias de venta que ayuden al crecimiento del negocio.

A esto se suma la falta de control de proveedores. Sin un registro adecuado de los proveedores y de sus condiciones de abastecimiento, se dificulta la gestión de compras, los tiempos de entrega y la negociación de precios. Esto puede derivar en retrasos en el reabastecimiento, sobrecostos o dependencia de un solo proveedor.

# Diagrama UML
![Diagrama de clases UML](https://github.com/user-attachments/assets/bf5f3867-785e-4afe-9e25-eec8285d739b)

# Diagrama E-R
![Diagramas_Miscelanea](https://github.com/user-attachments/assets/7bd00758-2e59-4fc4-8e53-d9bebc0236d5)

# Código BDD

```sql
--Creación de la bdd
CREATE DATABASE IF NOT EXISTS miselanea;
USE miselanea;

-- Creación de tablas
CREATE TABLE `usuario` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `numero_telefono` varchar(20) DEFAULT NULL,
  `correo_electronico` varchar(100) DEFAULT NULL,
  `rol` varchar(20) NOT NULL DEFAULT 'VENDEDOR',
  PRIMARY KEY (`id_usuario`)
);

CREATE TABLE `administrador` (
  `id_administrador` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  PRIMARY KEY (`id_administrador`),
  KEY `usuario_id` (`usuario_id`)
);

CREATE TABLE `almacen` (
  `id_almacen` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `ubicacion` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_almacen`)
);

CREATE TABLE `proveedor` (
  `id_proveedor` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `empresa` varchar(100) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `precio_minimo` decimal(10,2) DEFAULT NULL,
  `plazo_entrega` int DEFAULT NULL,
  `contacto` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_proveedor`)
);

CREATE TABLE `compra` (
  `id_compra` int NOT NULL AUTO_INCREMENT,
  `proveedor_id` int NOT NULL,
  `fecha` date NOT NULL DEFAULT (curdate()),
  PRIMARY KEY (`id_compra`),
  KEY `proveedor_id` (`proveedor_id`)
);

CREATE TABLE `producto` (
  `id_producto` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `tipo` varchar(20) NOT NULL DEFAULT 'General',
  `fecha_caducidad` date DEFAULT NULL,
  `fecha_compra` date DEFAULT NULL,
  PRIMARY KEY (`id_producto`)
);

CREATE TABLE `compra_producto` (
  `compra_id` int NOT NULL,
  `producto_id` int NOT NULL,
  `cantidad` int NOT NULL,
  PRIMARY KEY (`compra_id`,`producto_id`),
  KEY `producto_id` (`producto_id`)
);

CREATE TABLE `noperecedero` (
  `id_no_perecedero` int NOT NULL AUTO_INCREMENT,
  `fecha_compra` date NOT NULL,
  `producto_id` int NOT NULL,
  PRIMARY KEY (`id_no_perecedero`),
  KEY `producto_id` (`producto_id`)
);

CREATE TABLE `perecedero` (
  `id_perecedero` int NOT NULL AUTO_INCREMENT,
  `fecha_caducidad` date NOT NULL,
  `producto_id` int NOT NULL,
  PRIMARY KEY (`id_perecedero`),
  KEY `producto_id` (`producto_id`)
);

CREATE TABLE `vendedor` (
  `id_vendedor` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  PRIMARY KEY (`id_vendedor`),
  KEY `usuario_id` (`usuario_id`)
);

CREATE TABLE `venta` (
  `id_venta` int NOT NULL AUTO_INCREMENT,
  `fecha` date NOT NULL DEFAULT (curdate()),
  `vendedor_id` int NOT NULL,
  PRIMARY KEY (`id_venta`),
  KEY `vendedor_id` (`vendedor_id`)
);

CREATE TABLE `venta_producto` (
  `venta_id` int NOT NULL,
  `producto_id` int NOT NULL,
  `cantidad` int NOT NULL,
  PRIMARY KEY (`venta_id`,`producto_id`),
  KEY `producto_id` (`producto_id`)
);
```
