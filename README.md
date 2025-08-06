# 🎨 Chinelín - Sistema de Gestión Artesanal

## 🧾 Resumen Ejecutivo

### 📌 Descripción
Chinelín es una empresa familiar dedicada a la elaboración y venta de figuras artesanales mexicanas. Este proyecto desarrolla una solución digital para gestionar las operaciones internas de la empresa, mejorando el control de clientes, productos y ventas.

### 🧩 Problema Identificado
La empresa manejaba sus registros en papel y hojas de cálculo, generando errores humanos, pérdida de información y duplicidad de datos.

### 💡 Solución Propuesta
Desarrollo de una aplicación de escritorio con interfaz gráfica en Java que permita la gestión eficiente de los datos de la empresa, conectada a una base de datos MySQL local.

### � Arquitectura
- **Frontend**: Interfaz gráfica en Java (Swing / JavaFX)
- **Backend**: Lógica en Java (POO)
- **Base de datos**: MySQL local
- **Conexión**: JDBC con controladores MySQL
- **Ambiente de ejecución**: Local 

## 📚 Tabla de Contenidos
- [Requerimientos](#-requerimientos)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Uso](#-uso)
- [Contribución](#-contribución)
- [Roadmap](#-roadmap)

## ✅ Requerimientos

### 🖥️ Hardware
- Computadora con al menos 2 GB de RAM
- Sistema operativo Windows, Linux o macOS

### 🔧 Software
- Java JDK versión 17 o superior
- MySQL Server (versión 8.0 o superior)
- IDE recomendada: NetBeans o IntelliJ
- Driver JDBC para MySQL

## 🔧 Instalación

### 1. Clonar el Repositorio

git clone https://github.com/usuario/chinelin.git
cd chinelin
## 2. Preparar el Ambiente
Instalar Java JDK 17 o superior

Instalar MySQL Server y Workbench

Crear la base de datos chinelin y ejecutar el script chinelin.sql ubicado en la carpeta /database

## 3. Ejecutar el Proyecto
Abrir el proyecto en tu IDE

Configurar el archivo de conexión a la base de datos (DBConnection.java o similar)

Ejecutar la clase Main.java

## ⚙️ Configuración
### Archivo de Configuración de la Conexión

String url = "jdbc:mysql://localhost:3306/chinelin";
String user = "root";
String password = "tu_contraseña";
## 📘 Uso

### Para Usuarios Finales
Al iniciar, se muestra la pantalla de login

El usuario puede ingresar y ver menús como:

- Registro de clientes
- Registro de productos artesanales
- Registro de ventas
- Reportes

### Para Administradores
- Acceso a funciones de mantenimiento (actualizar/eliminar clientes)
- Vista de reportes completos de ventas
- Control del inventario artesanal

## 🤝 Contribución

- git clone https://github.com/usuario/chinelin.git
- git checkout -b nueva-funcionalidad
- git commit -m "Agregar funcionalidad X"
- git push origin nueva-funcionalidad
- Abre un Pull Request desde GitHub y espera revisión.
