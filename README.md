# chacana.tomas-fernandez.victoria-Proyecto.2025.1
ELO239 Proyecto 2025-1

## Integrantes

Victoria Fernandez
Jossefa Zamora
Tomás Chacana

## Generar Documentación

Para generar la documentación del proyecto en formato HTML, ejecute el siguiente comando desde la raíz del proyecto. La documentación se creará en la carpeta `docs/`.

```bash
javadoc --module-path "/opt/javafx-sdk-21.0.7/lib" --add-modules javafx.controls,javafx.fxml -d docs -sourcepath src/main/java -subpackages cl.usm.gestorinventario
```
Luego:

```bash
xdg-open docs/index.html
```

Documento de Requerimientos: Casos de Uso
Proyecto: Sistema Gestor de Inventario
Versión: 1.0
Fecha: 24 de junio de 2025

1. Introducción

Este documento detalla los requerimientos funcionales del Sistema Gestor de Inventario a través de tres casos de uso principales. Estos casos describen las interacciones clave entre los actores (usuarios) y el sistema, y servirán como base para el diseño, desarrollo y pruebas de la aplicación.
Caso de Uso 1: Agregar Nuevo Producto al Inventario

    ID: CU-01

    Nombre del Caso de Uso: Agregar Nuevo Producto al Inventario

    Actor(es) Principal(es): Administrador

    Propósito: Permitir al Administrador registrar un nuevo artículo en el sistema de inventario, incluyendo sus atributos específicos según el tipo de producto.

    Pre-condiciones: El Administrador debe haber iniciado sesión en el sistema con sus credenciales.

    Curso Normal de Eventos (Flujo Básico):
        El Administrador selecciona la opción "Agregar Producto" en la interfaz principal.
        El Sistema presenta un formulario para ingresar los datos del nuevo producto. El formulario solicita atributos comunes (ID, Nombre, Precio, Stock Inicial) y permite seleccionar el tipo de producto (ej. Libro, Ropa).
        Al seleccionar un tipo de producto, el Sistema muestra campos adicionales específicos para ese tipo (ej. "Autor" e "ISBN" para Libro; "Talla" y "Color" para Ropa).
        El Administrador completa todos los campos requeridos.
        El Administrador presiona el botón "Guardar".
        El Sistema valida que el ID del producto no exista previamente y que todos los datos numéricos (precio, stock) sean válidos (ej. no negativos).
        El Sistema crea una nueva instancia del objeto Producto (o de su subclase correspondiente, ej. Libro) y la almacena en el inventario.
        El Sistema muestra un mensaje de confirmación: "Producto agregado exitosamente".
        El Sistema actualiza la tabla principal del inventario para mostrar el nuevo producto.

    Cursos Alternativos (Flujos de Excepción):
        6a. ID de producto duplicado: Si el ID ingresado ya existe en el inventario, el Sistema no guarda el producto y muestra un mensaje de error: "Error: El ID del producto ya existe. Por favor, ingrese un ID único".
        6b. Datos inválidos: Si se ingresa un precio o stock negativo, el Sistema no guarda el producto y muestra un mensaje de error específico: "Error: El precio y el stock no pueden ser negativos".

    Post-condiciones: El nuevo producto está registrado y disponible en el inventario para futuras operaciones como ventas o reportes.

Caso de Uso 2: Registrar Venta de Producto

    ID: CU-02

    Nombre del Caso de Uso: Registrar Venta de Producto

    Actor(es) Principal(es): Vendedor

    Propósito: Permitir al Vendedor registrar la venta de uno o más productos, actualizando automáticamente el stock y registrando la transacción.

    Pre-condiciones: El Vendedor ha iniciado sesión. Debe existir al menos un producto en el inventario.

    Curso Normal de Eventos (Flujo Básico):
        El Vendedor selecciona un producto de la lista en la interfaz principal.
        El Vendedor presiona el botón "Registrar Venta".
        El Sistema muestra una ventana de diálogo solicitando la cantidad de unidades a vender.
        El Vendedor ingresa una cantidad numérica positiva y presiona "Confirmar".
        El Sistema verifica que la cantidad solicitada sea menor o igual al stock actual del producto seleccionado.
        El Sistema descuenta la cantidad vendida del stock del producto.
        El Sistema crea un registro de la transacción (ej. objeto Venta) con los detalles (producto, cantidad, fecha, total).
        El Sistema muestra un mensaje de éxito: "Venta registrada. Nuevo stock para [Nombre del Producto]: [Stock restante]".
        El Sistema actualiza la tabla principal para reflejar el nuevo stock del producto.

    Cursos Alternativos (Flujos de Excepción):
        4a. Cantidad inválida: Si el Vendedor ingresa una cantidad no numérica o menor a 1, el Sistema muestra un mensaje de error: "Error: La cantidad debe ser un número positivo".
        5a. Stock insuficiente: Si la cantidad solicitada es mayor que el stock disponible, el Sistema no realiza la venta y muestra un mensaje de error: "Error: Stock insuficiente. Unidades disponibles: [Stock actual]".

    Post-condiciones: El stock del producto vendido ha disminuido. La transacción de venta ha sido registrada en el sistema.

Caso de Uso 3: Generar Reporte de Stock Bajo

    ID: CU-03

    Nombre del Caso de Uso: Generar Reporte de Stock Bajo

    Actor(es) Principal(es): Administrador

    Propósito: Proveer al Administrador una vista filtrada de todos los productos cuyo nivel de stock ha caído por debajo de un umbral predefinido, para facilitar las decisiones de reposición.

    Pre-condiciones: El Administrador ha iniciado sesión en el sistema.

    Curso Normal de Eventos (Flujo Básico):
        El Administrador selecciona la opción "Ver Reporte de Stock Bajo" en la interfaz.
        El Sistema (utilizando un umbral predefinido, ej. 10 unidades) recorre la lista completa de productos en el inventario.
        El Sistema filtra y recopila todos los productos cuyo stock actual es menor o igual al umbral.
        El Sistema muestra una nueva ventana o una vista especial con una tabla que contiene únicamente los productos con stock bajo, mostrando su ID, nombre y stock actual.

    Cursos Alternativos (Flujos de Excepción):
        4a. No hay productos con stock bajo: Si ningún producto cumple con el criterio, el Sistema muestra un mensaje informativo en la nueva vista: "No se encontraron productos con stock bajo".

    Post-condiciones: El Administrador ha visualizado el reporte. El estado de los productos en el inventario no se modifica.